package lexer.dfa;

import lexer.AbstractDFA;
import symbols.Tokens.Token;

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
	 *            A String that the automaton should recognize
	 * @param token
	 *            The token corresponding to the recognized word.
	 */
	public WordDFA(String word, Token token) {
		// TODO: fill in correct number of states
		super(token, 0 /*noStates*/);
		
		assert (word.length() > 0);

		// TODO: build DFA recognizing the given word
	}
}
