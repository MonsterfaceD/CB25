package lexer;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import symbols.Symbol;
import symbols.Token;
import symbols.Tokens;

/**
 * The lexer. Knows about the recognized alphabet and tokens and performs the
 * lexer analysis.
 */
public class LexerGenerator {

	public LexerGenerator() {

	}

	/**
	 * Perform the lexer analysis.
	 * 
	 * @param tokens
	 *            Tokens
	 * @param input
	 *            The input program.
	 * @return Recognized symbols.
	 * @throws LexerException
	 *             Exception from the lexer.
	 */
	public static List<Symbol> analyse(Tokens tokens, String input) throws LexerException {
		return analyse(tokens, input, true);
	}

	/**
	 * Perform the lexer analysis.
	 * 
	 * @param tokens
	 *            Tokens
	 * @param input
	 *            The input program.
	 * @param suppressBlankAndComments
	 *            If true, blanks and comments are ignored.
	 * @return Recognized symbols.
	 * @throws LexerException
	 *             Exception from the lexer.
	 */
	public static List<Symbol> analyse(Tokens tokens, String input, boolean suppressBlankAndComments)
			throws LexerException {
		BacktrackingDFA bdfa;
		try {
			bdfa = new BacktrackingDFA(tokens);
		} catch (Exception e) {
			throw new LexerException(e.getMessage(), new ArrayList<Symbol>());
		}
		List<Symbol> analysis = null;
		analysis = bdfa.run(input);

		if (suppressBlankAndComments) {
			Iterator<Symbol> it = analysis.iterator();
			while (it.hasNext()) {
				Token token = it.next().getToken();
				if (token == Tokens.BLANK || token == Tokens.COMMENT) {
					it.remove();
				}
			}
		}

		return analysis;
	}

}
