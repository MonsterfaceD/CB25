package lexer.dfa;

import java.util.Arrays;

import lexer.AbstractDFA;
import symbols.Tokens.Token;

/**
 * DFA recognizing comments.
 */
public class CommentDFA extends AbstractDFA {

	private final int slashRead = 1;
	private final int singleLine = 2;
	private final int multiLine = 3;
	private final int maybeEndMultiLine = 4;
	private final int finalState = 5;
	private final int macFinalState = 6;

	/**
	 * Construct a new DFA that recognizes comments within source code. There
	 * are two kinds of comments: A single line comment starts with // and ends
	 * with a newline and a multiline comment that starts with /* and ends with
	 * * / (without the space)
	 */
	public CommentDFA() {
		super(Token.COMMENT, 8);

		super.addTransition(super.getInitialState(), '/', slashRead);
		super.addTransition(slashRead, '/', singleLine);
		super.addTransition(slashRead, '*', multiLine);
		super.addTransition(singleLine, '\n', finalState);
		super.addTransition(singleLine, '\r', macFinalState);
		super.addTransition(macFinalState, '\n', finalState);
		super.addTransition(multiLine, '*', maybeEndMultiLine);
		super.addTransition(maybeEndMultiLine, '*', maybeEndMultiLine);
		super.addTransition(maybeEndMultiLine, '/', finalState);

		super.addFinalState(finalState);
		super.addFinalState(macFinalState);
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
			switch (super.getCurrentState()) {
			case singleLine:
			case multiLine:
				// stay here
				break;
			case maybeEndMultiLine:
				super.setCurrentState(multiLine);
				break;
			default:
				super.setCurrentState(super.getSinkState());
			}
		} else
			super.setCurrentState(nextState);
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
