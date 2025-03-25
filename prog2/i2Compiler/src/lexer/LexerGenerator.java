package lexer;

import java.util.Iterator;
import java.util.List;

import symbols.Symbol;
import symbols.Tokens.Token;

/**
 * The lexer. Knows about the recognized alphabet and tokens and performs the
 * lexer analysis.
 */
public class LexerGenerator {

	/**
	 * Perform the lexer analysis.
	 * 
	 * @param input
	 *            The input program.
	 * @return Recognized symbols.
	 * @throws LexerException
	 *             Exception from the lexer.
	 */
	public static List<Symbol> analyse(String input) throws LexerException {
		return analyse(input, true);
	}

	/**
	 * Perform the lexer analysis.
	 * 
	 * @param input
	 *            The input program.
	 * @param suppressBlankAndComments
	 *            If true, blanks and comments are ignored.
	 * @return Recognized symbols.
	 * @throws LexerException
	 *             Exception from the lexer.
	 */
	public static List<Symbol> analyse(String input, boolean suppressBlankAndComments) throws LexerException {
		BacktrackingDFA bdfa = new BacktrackingDFA();
		List<Symbol> analysis = null;
		analysis = bdfa.run(input);

		if (suppressBlankAndComments) {
			Iterator<Symbol> it = analysis.iterator();
			while (it.hasNext()) {
				Token t = it.next().getToken();
				if (Token.BLANK == t || Token.COMMENT == t) {
					it.remove();
				}
			}
		}

		return analysis;
	}

}
