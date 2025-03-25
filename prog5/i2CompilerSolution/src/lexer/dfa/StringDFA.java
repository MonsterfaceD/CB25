package lexer.dfa;

import java.util.Arrays;

import lexer.AbstractDFA;
import symbols.Token;

/**
 * DFA recognizing string constants.
 */
public class StringDFA extends AbstractDFA {

	private final int readLetters = 1;

	/**
	 * Construct a new DFA that recognizes string constants
	 * 
	 * @param token
	 *            The token corresponding to the comment.
	 * 
	 */
	public StringDFA(Token token) {
		super(token, 4);

		// there are only 4 states, state 0 is the initial state
		int finalState = 2;

		super.addTransition(super.getInitialState(), '"', readLetters);
		super.addTransition(readLetters, '"', finalState);

		super.addFinalState(finalState);
		computeProductiveStates();
	}

	/**
	 * Performs one step of the DFA for a given letter. This method works
	 * differently than in the superclass AbstractDFA.
	 * 
	 * @param letter
	 *            The current input.
	 */
	@Override
	public void doStep(char letter) {
		Integer nextState = super.getTransitionTarget(super.getCurrentState(), letter);
		if (nextState == null) {
			if (super.getCurrentState() == readLetters && letter != '"') {
				// stay there
			} else {
				super.setCurrentState(super.getSinkState());
			}
		} else {
			super.setCurrentState(nextState);
		}
	}

	/**
	 * Compute productive states. This method works differently than in the
	 * superclass AbstractDFA as not all possible steps are directly encoded as
	 * transitions.
	 */
	@Override
	protected void computeProductiveStates() {
		Arrays.fill(super.productive, true);
		super.productive[super.getSinkState()] = false;
	}

}
