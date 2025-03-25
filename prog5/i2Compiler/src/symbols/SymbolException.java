package symbols;

/**
 * Exception thrown by when accessing symbols.
 */
public class SymbolException extends Exception {

	private static final long serialVersionUID = 1L;

	/**
	 * Constructor.
	 * 
	 * @param s
	 *            Text explaining the exception.
	 */
	public SymbolException(String s) {
		super(s);
	}
}
