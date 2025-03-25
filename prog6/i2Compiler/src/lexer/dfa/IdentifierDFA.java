package lexer.dfa;

import symbols.Letters;
import symbols.Token;
import lexer.AbstractDFA;

/**
 * DFA recognizing identifiers.
 */
public class IdentifierDFA extends AbstractDFA {

	/**
	 * Construct a new DFA that recognizes every alphanumerical identifier that
	 * starts with a letter and continues with letter, numbers or underscores.
	 * 
	 * @param token
	 *            The token corresponding to the identifier.
	 */
	public IdentifierDFA(Token token) {
		super(token, 3);

		int finalState = 1;

		for (int i = 0; i < Letters.alpha.length; i++) {
			super.addTransition(super.getInitialState(), Letters.alpha[i], finalState);
			super.addTransition(finalState, Letters.alpha[i], finalState);
		}
		for (int i = 0; i < Letters.underScoreNumerical.length; i++) {
			super.addTransition(finalState, Letters.underScoreNumerical[i], finalState);
		}

		super.addFinalState(finalState);
		super.computeProductiveStates();
	}
}
