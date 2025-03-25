package lexer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

import lexer.dfa.CommentDFA;
import lexer.dfa.IdentifierDFA;
import lexer.dfa.NumberDFA;
import lexer.dfa.StringDFA;
import lexer.dfa.WordDFA;
import symbols.Letters;
import symbols.Symbol;
import symbols.Token;
import symbols.Tokens;
import util.Pair;

public class BacktrackingDFA {

	// List of all automata
	private List<AbstractDFA> automata;
	private HashMap<Pair<String, Character>, int[]> transitions;
	private Map<String, Token> recognisedToken;
	// Initial state in each automata
	private int[] initialState;
	// Current state in each automata
	private int[] currentState;

	/**
	 * Constructor.
	 */
	/**
	 * Constructor.
	 * 
	 * @param tokens
	 *            Tokens.
	 * @throws Exception
	 *             Exception from the BacktrackingDFA.
	 */
	public BacktrackingDFA(Tokens tokens) throws Exception {
		generateDFAforTokens(tokens);
		generateTransitions();
	}

	/**
	 * .
	 */
	/**
	 * This method creates an array of DFAs, one for every given token (and
	 * symbol). Those automata will run in parallel and are controlled by the
	 * doStep(), isProductive() and resetToState() methods
	 * 
	 * @param tokens
	 *            Tokens.
	 * @throws Exception
	 *             Exception from the lexer.
	 */
	public void generateDFAforTokens(Tokens tokens) throws Exception {
		automata = new ArrayList<AbstractDFA>();
		// generate all automata
		for (Pair<Token, String> entry : tokens) {
			Token token = entry.getFirst();
			String word = entry.getSecond();
			if (word.equals("")) {
				// Special tokens
				String name = token.getName();
				if (name.equals("BLANK")) {
					automata.add(new WordDFA(" ", token));
					automata.add(new WordDFA("\t", token));
					automata.add(new WordDFA("\r", token));
					automata.add(new WordDFA("\n", token));
				} else if (name.equals("ID")) {
					automata.add(new IdentifierDFA(token));
				} else if (name.equals("STRING")) {
					automata.add(new StringDFA(token));
				} else if (name.equals("NUMBER")) {
					automata.add(new NumberDFA(token));
				} else if (name.equals("COMMENT")) {
					automata.add(new CommentDFA(token));
				} else if (name.equals("EPSILON")) {
					// ignore
				} else {
					throw new Exception("Token: " + name + " unknown.");
				}
			} else {
				automata.add(new WordDFA(word, token));
			}
		}

		initialState = new int[automata.size()];
		Arrays.fill(initialState, 0);
		currentState = new int[initialState.length];
	}

	/**
	 * Generate all transitions by exploring the state space.
	 */
	private void generateTransitions() {
		transitions = new HashMap<Pair<String, Character>, int[]>();
		recognisedToken = new HashMap<String, Token>();

		// Create array of relevant alphabet
		char[] relevantAlphabet = new char[Letters.alpha.length + Letters.underScoreNumerical.length
				+ Letters.special.length];
		System.arraycopy(Letters.alpha, 0, relevantAlphabet, 0, Letters.alpha.length);
		System.arraycopy(Letters.underScoreNumerical, 0, relevantAlphabet, Letters.alpha.length,
				Letters.underScoreNumerical.length);
		System.arraycopy(Letters.special, 0, relevantAlphabet,
				Letters.alpha.length + Letters.underScoreNumerical.length, Letters.special.length);

		Queue<int[]> statesToExpand = new LinkedList<int[]>();

		Set<String> visitedStates = new HashSet<String>();

		int[] state = new int[initialState.length];
		System.arraycopy(initialState, 0, state, 0, initialState.length);
		statesToExpand.add(state);
		visitedStates.add(hashState(state));

		// Explore possible states
		while (!statesToExpand.isEmpty()) {
			state = statesToExpand.remove();
			// Consider all possible transitions
			for (char letter : relevantAlphabet) {
				int[] tempState = new int[initialState.length];
				for (int i = 0; i < automata.size(); i++) {
					AbstractDFA automaton = automata.get(i);
					automaton.setCurrentState(state[i]);
					automaton.doStep(letter);
					tempState[i] = automaton.getCurrentState();
				}
				if (!visitedStates.contains(hashState(tempState))) {
					// New state needs exploration
					statesToExpand.add(tempState);
					visitedStates.add(hashState(tempState));
				}
				transitions.put(new Pair<String, Character>(hashState(state), letter), tempState);
			}
			// Check final states
			setToken(state);
		}
	}

	/**
	 * Construct a hash for the given state.
	 * 
	 * @param state
	 *            The state of the backtracking DFA.
	 * @return The hash for the state.
	 */
	private String hashState(int[] state) {
		StringBuilder builder = new StringBuilder();
		for (int i = 0; i < state.length - 1; i++) {
			builder.append(state[i]);
			builder.append(',');
		}
		builder.append(state[state.length - 1]);
		return builder.toString();
	}

	/**
	 * Set the token for the given state
	 * 
	 * @param state
	 *            State to set token for.
	 */
	private void setToken(int[] state) {
		for (int i = 0; i < state.length; i++) {
			automata.get(i).setCurrentState(state[i]);
			if (automata.get(i).isAccepting()) {
				recognisedToken.put(hashState(state), automata.get(i).getToken());
				// already found an accepting state => proceed with next
				// state in transitions
				break;
			}
		}
	}

	/**
	 * Do a step in the backtracking DFA.
	 * 
	 * @param letter
	 *            The current character.
	 * @return The recognized token.
	 * @throws Exception
	 *             Exception if step could not be performed.
	 */
	public Token doStep(char letter) throws Exception {
		currentState = transitions.get(new Pair<String, Character>(hashState(currentState), letter));
		if (currentState == null) {
			throw new Exception("Symbol: " + letter + " not part of the alphabet.");
		}
		// for (int i = 0; i < automata.size(); i++) {
		// automata.get(i).doStep(letter);
		// currentState[i] = automata.get(i).getCurrentState();
		// }

		return recognisedToken.get(hashState(currentState));
	}

	/**
	 * Given a string of lexemes, chop them up to the corresponding symbols,
	 * i.e. a list of (token, attribute) pairs. Note that since all keywords and
	 * symbols are represented by their own token, the attribute only really
	 * matters for identifiers and numbers.
	 * 
	 * @param word
	 *            The input program to analyze.
	 * @return List of symbols.
	 * @throws LexerException
	 *             Exception from the lexer if the analysis is not successful.
	 */
	public List<Symbol> run(String word) throws LexerException {
		List<Symbol> result = new ArrayList<Symbol>();

		char[] wordAsChar = word.toCharArray();
		Token backtrackToken = null;
		Token currentToken = null;
		int backtrackPointer = 0;
		int currentPointer = 0;
		System.arraycopy(initialState, 0, currentState, 0, initialState.length);

		// Initialize automata
		resetToState(initialState);

		// Run backtracking DFA
		while (backtrackPointer < wordAsChar.length) {
			String value = Character.toString(wordAsChar[currentPointer]);
			while (currentPointer < wordAsChar.length && isProductive()) {
				try {
					currentToken = doStep(wordAsChar[currentPointer]);
				} catch (Exception e) {
					throw new LexerException(e.getMessage(), result);
				}
				if (currentToken != null) {
					// New token found
					value += new String(Arrays.copyOfRange(wordAsChar, backtrackPointer + 1, currentPointer + 1));
					backtrackToken = currentToken;
					backtrackPointer = currentPointer;
				}
				currentPointer++;
			}
			if (backtrackToken != null) {
				result.add(new Symbol(backtrackToken, value));
				currentPointer = backtrackPointer + 1;
				resetToState(initialState);
				backtrackToken = null;
				backtrackPointer++;
			} else {
				throw new LexerException("Last backtrack position is: " + backtrackPointer
						+ "\nScanned before failure: " + word.substring(0, backtrackPointer + 1), result);
			}
		}

		return result;
	}

	/**
	 * Check if the current state is productive.
	 * 
	 * @return True iff the current state of any component is productive.
	 */
	private boolean isProductive() {
		for (AbstractDFA automaton : automata) {
			if (automaton.isProductive())
				return true;
		}
		return false;
	}

	/**
	 * Reset the current state to a previous state.
	 * 
	 * @param state
	 *            The state to reset to.
	 */
	public void resetToState(int[] state) {
		for (int i = 0; i < automata.size(); i++) {
			currentState[i] = state[i];
			automata.get(i).setCurrentState(currentState[i]);
		}
	}

}
