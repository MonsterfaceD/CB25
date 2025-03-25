package lexer.dfa;

import lexer.AbstractDFA;
import symbols.Tokens.Token;

/**
 * DFA recognizing comments.
 */
public class CommentDFA extends AbstractDFA {

	/**
	 * Construct a new DFA that recognizes comments within source code. There
	 * are two kinds of comments: A single line comment starts with // and ends
	 * with a newline and a multiline comment that starts with /* and ends with
	 * * / (without the space)
	 */
	public CommentDFA() {
		// TODO: fill in correct number of states
		super(Token.COMMENT, 0 /* noStates */);

		// TODO: build DFA recognizing comments
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
		// TODO: implement accordingly
	}
}
