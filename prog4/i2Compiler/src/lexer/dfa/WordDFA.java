package lexer.dfa;

import lexer.AbstractDFA;
import symbols.Token;

/**
 * DFA recognizing a given word.
 */
public class WordDFA extends AbstractDFA {

	/**
	 * Construct a new DFA that recognizes exactly the given word. Given a word
	 * "foo" the constructed automaton looks like: -> () -f-> () -o-> () -o-> []
	 * from every state (including the final one) every other input letter leads
	 * to a distinguished sink state in which the automaton then remains
	 * 
	 * @param word
	 *            A string that the automaton should recognize
	 * @param token
	 *            The token corresponding to the recognized word.
	 */
	public WordDFA(String word, Token token) {
		super(token, word.length() + 2);

		assert (word.length() > 0);

		for (int i = 0; i < word.length(); i++) {
			super.addTransition(i, word.charAt(i), i + 1);
		}

		super.addFinalState(word.length());
		super.computeProductiveStates();
	}
}
