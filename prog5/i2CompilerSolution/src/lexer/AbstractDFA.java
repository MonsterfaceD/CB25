package lexer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

import symbols.Token;
import util.Pair;

/**
 * Abstract class for Deterministic Finite Automata.
 */
public abstract class AbstractDFA {

	// Token that is recognized by this automaton
	private Token token;
	// Initial state
	private final int initialState = 0;
	// Sink state
	private int sinkState;
	// Set of final states
	private ArrayList<Integer> finalStates = new ArrayList<Integer>();
	// Transition representation as mapping from (state, character) to nextState
	private HashMap<Pair<Integer, Character>, Integer> transitions;
	// Current state
	private int currentState = 0;
	// Indicator if state is productive
	protected boolean[] productive;

	/**
	 * Constructor for Abstract DFA.
	 * 
	 * @param token
	 *            Token recognized by this automaton.
	 * @param noStates
	 *            Number of states in the DFA.
	 */
	public AbstractDFA(Token token, int noStates) {
		this.token = token;
		transitions = new HashMap<Pair<Integer, Character>, Integer>();
		sinkState = noStates - 1;
		productive = new boolean[noStates];
		Arrays.fill(productive, false);
	}

	/**
	 * Get target state after performing transition.
	 * 
	 * @param state
	 *            Source state.
	 * @param letter
	 *            Input letter.
	 * @return Target state or null, if no transition was found.
	 */
	protected Integer getTransitionTarget(int state, char letter) {
		return transitions.get(new Pair<Integer, Character>(state, letter));
	}

	/**
	 * Add a new transition to the DFA.
	 * 
	 * @param state
	 *            Source state.
	 * @param letter
	 * @param nextState
	 *            Target state.
	 */
	protected void addTransition(int state, char letter, int nextState) {
		transitions.put(new Pair<Integer, Character>(state, letter), nextState);
	}

	/**
	 * Get the current state.
	 * 
	 * @return The id of the current state.
	 */
	public int getCurrentState() {
		return currentState;
	}

	/**
	 * Set current state.
	 * 
	 * @param state
	 *            State.
	 */
	public void setCurrentState(int state) {
		assert (0 <= state && state < productive.length);
		currentState = state;
	}

	/**
	 * Add state to final states.
	 * 
	 * @param state
	 *            State.
	 */
	protected void addFinalState(int state) {
		finalStates.add(state);
	}

	/**
	 * Get initial state.
	 * 
	 * @return Initial state.
	 */
	protected int getInitialState() {
		return initialState;
	}

	/**
	 * Get sink state.
	 * 
	 * @return Sink state.
	 */
	protected int getSinkState() {
		return sinkState;
	}

	/**
	 * Reset the automaton to the initial state.
	 */
	public void reset() {
		setCurrentState(initialState);
	}

	/**
	 * Performs one step of the DFA for a given letter. If there is a transition
	 * for the given letter, then the automaton proceeds to the successor state.
	 * Otherwise it goes to the sink state. By construction it will stay in the
	 * sink for every input letter.
	 * 
	 * @param letter
	 *            The current input.
	 */
	public void doStep(char letter) {
		Integer nextState = getTransitionTarget(currentState, letter);
		if (nextState == null) {
			setCurrentState(sinkState);
		} else {
			setCurrentState(nextState);
		}
	}

	/**
	 * Run the DFA on the input.
	 * 
	 * @param inputWord
	 *            String that contains the input word
	 * @return True, if if the word is accepted by this automaton
	 */
	public boolean run(String inputWord) {
		this.reset();
		char[] inputCharWord = inputWord.toCharArray();
		for (char letter : inputCharWord) {
			doStep(letter);
		}
		return isAccepting();
	}

	/**
	 * Check if the automaton is currently accepting.
	 * 
	 * @return True, if the automaton is currently in an accepting state.
	 */
	public boolean isAccepting() {
		return finalStates.contains(currentState);
	}

	/**
	 * Checks if the final state can be reached from the current state.
	 * 
	 * @return True, if the state is productive, i.e. the final state can be
	 *         reached.
	 */
	public boolean isProductive() {
		assert currentState < productive.length;
		return productive[currentState];
	}

	/**
	 * Perform backward BFS from the final states to find all productive states
	 * and initialize the corresponding data structure.
	 * 
	 */
	protected void computeProductiveStates() {
		// Temporary save backward transitions to be able to perform backward
		// search
		ArrayList<HashSet<Integer>> backwardTransitions = new ArrayList<HashSet<Integer>>();
		for (int i = 0; i < productive.length; ++i) {
			backwardTransitions.add(new HashSet<Integer>());
		}
		for (Map.Entry<Pair<Integer, Character>, Integer> entry : transitions.entrySet()) {
			int target = entry.getValue();
			int source = entry.getKey().getFirst();
			assert target < backwardTransitions.size();
			backwardTransitions.get(target).add(source);
		}

		// Backward search from final states to find all productive states
		Queue<Integer> queue = new LinkedList<Integer>();
		queue.addAll(finalStates);
		HashSet<Integer> visited = new HashSet<Integer>();

		while (!queue.isEmpty()) {
			int state = queue.poll();
			productive[state] = true;
			visited.add(state);
			for (int newState : backwardTransitions.get(state)) {
				// Add new states to visit
				if (!visited.contains(newState)) {
					queue.add(newState);
				}
			}
		}
	}

	/**
	 * @return The Token that this automaton recognizes
	 */
	public Token getToken() {
		return token;
	}
}
