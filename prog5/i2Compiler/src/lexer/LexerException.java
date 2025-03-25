package lexer;

import java.util.List;

import symbols.Symbol;

/**
 * Exception thrown by the lexer.
 */
public class LexerException extends Exception {

	private static final long serialVersionUID = 1L;

	private List<Symbol> analysisBeforeFailure = null;

	/**
	 * Constructor.
	 * 
	 * @param s
	 *            Text explaining the exception.
	 * @param analysis
	 *            Read symbols up to the exception.
	 */
	public LexerException(String s, List<Symbol> analysis) {
		super(s);
		this.analysisBeforeFailure = analysis;
	}

	/**
	 * Gets the read symbols up to the exception.
	 * 
	 * @return List of symbols read before the failure.
	 */
	public List<Symbol> getAnalysisBeforeFailure() {
		return analysisBeforeFailure;
	}
}
