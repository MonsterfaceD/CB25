package lexer.dfa;

import symbols.Tokens.Token;

import java.util.Arrays;

import lexer.AbstractDFA;

/**
 * DFA recognizing string constants.
 */
public class StringDFA extends AbstractDFA {

	private final int readLetters = 1;

	/**
	 * Construct a new DFA that recognizes string constants
	 */
	public StringDFA() {
		super(Token.STRING, 4);

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
