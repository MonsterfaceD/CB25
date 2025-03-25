package lexer.dfa;

import symbols.Letters;
import symbols.Token;
import lexer.AbstractDFA;

/**
 * DFA recognizing integers.
 */
public class NumberDFA extends AbstractDFA {

	/**
	 * Construct a new DFA that recognizes integers.
	 * 
	 * @param token
	 *            The token corresponding to the number.
	 * 
	 */
	public NumberDFA(Token token) {
		super(token, 4);

		// there are only 4 states, state 0 is the initial state
		int finalState = 1;
		int firstNull = 2;

		for (int i = 0; i < Letters.numbers.length; i++) {
			if (Letters.numbers[i] == '0') {
				super.addTransition(super.getInitialState(), Letters.numbers[i], firstNull);
			} else {
				super.addTransition(super.getInitialState(), Letters.numbers[i], finalState);
			}
			super.addTransition(finalState, Letters.numbers[i], finalState);
		}

		super.addFinalState(finalState);
		super.addFinalState(firstNull);
		super.computeProductiveStates();
	}
}
