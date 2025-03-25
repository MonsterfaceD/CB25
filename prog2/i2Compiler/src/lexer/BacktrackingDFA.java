package lexer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import lexer.dfa.CommentDFA;
import lexer.dfa.IdentifierDFA;
import lexer.dfa.NumberDFA;
import lexer.dfa.StringDFA;
import lexer.dfa.WordDFA;
import symbols.Letters;
import symbols.Symbol;
import symbols.Tokens.Token;
import util.Pair;

public class BacktrackingDFA {

	// List of all automata
	private List<AbstractDFA> automata;
	// Initial state in each automata
	private int[] initialState;
	// Remember backtrack state in each automata
	private int[] backtrackState;
	// Current state in each automata
	private int[] currentState;
	
	//TODO Possible own data structures

	/**
	 * Constructor.
	 */
	public BacktrackingDFA() {
		generateDFAforTokens();
	}

	/**
	 * This method creates an array of DFAs, one for every token (and symbol).
	 * Those automata will run in parallel and are controlled by the doStep(),
	 * isProductive() and resetToState() methods.
	 */
	public void generateDFAforTokens() {
		automata = new ArrayList<AbstractDFA>();
		// generate all automata
		automata.add(new WordDFA("while", Token.WHILE));
		automata.add(new WordDFA("write", Token.WRITE));
		automata.add(new WordDFA("read", Token.READ));
		automata.add(new WordDFA("int", Token.INT));
		automata.add(new WordDFA("if", Token.IF));
		automata.add(new WordDFA("else", Token.ELSE));
		automata.add(new WordDFA("true", Token.TRUE));
		automata.add(new WordDFA("false", Token.FALSE));
		automata.add(new WordDFA("(", Token.LPAR));
		automata.add(new WordDFA(")", Token.RPAR));
		automata.add(new WordDFA("{", Token.LBRACE));
		automata.add(new WordDFA("}", Token.RBRACE));
		automata.add(new WordDFA("+", Token.PLUS));
		automata.add(new WordDFA("-", Token.MINUS));
		automata.add(new WordDFA("*", Token.TIMES));
		automata.add(new WordDFA("/", Token.DIV));
		automata.add(new WordDFA("%", Token.MOD));
		automata.add(new WordDFA("<=", Token.LEQ));
		automata.add(new WordDFA("<", Token.LT));
		automata.add(new WordDFA(">=", Token.GEQ));
		automata.add(new WordDFA(">", Token.GT));
		automata.add(new WordDFA("==", Token.EQ));
		automata.add(new WordDFA("=", Token.ASSIGN));
		automata.add(new WordDFA("!=", Token.NEQ));
		automata.add(new WordDFA("&&", Token.AND));
		automata.add(new WordDFA("||", Token.OR));
		automata.add(new WordDFA("!", Token.NOT));
		automata.add(new WordDFA("++", Token.INC));
		automata.add(new WordDFA("--", Token.DEC));
		automata.add(new WordDFA(";", Token.SEMICOLON));
		automata.add(new WordDFA("$", Token.EOF));
		automata.add(new IdentifierDFA());
		automata.add(new NumberDFA());
		automata.add(new CommentDFA());
		automata.add(new StringDFA());
		automata.add(new WordDFA(" ", Token.BLANK));
		automata.add(new WordDFA("\t", Token.BLANK));
		automata.add(new WordDFA("\r", Token.BLANK));
		automata.add(new WordDFA("\n", Token.BLANK));

		initialState = new int[automata.size()];
		Arrays.fill(initialState, 0);
		backtrackState = new int[initialState.length];
		currentState = new int[initialState.length];
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
		// TODO update currentState
		
		throw new Exception("Symbol: " + letter + " not part of the alphabet.");
		
		// TODO return recognized token or null
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

		// TODO: implement the backtracking automaton.

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
