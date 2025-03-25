/**
 * Class representing a symbol in a language. Its representation can be an
 * arbitrary non-empty string, the empty string is reserved for Symbol.EPS. Two
 * symbols are considered equal if they have the same representation. Feel free
 * to add hash and equals methods!
 */
public class Symbol {
	/**
	 * The empty string.
	 */
	static public Symbol EPS = new Symbol("");

	/**
	 * Representation of symbol.
	 */
	private String representation;

	/**
	 * Constructor.
	 * 
	 * @param repr Representation.
	 */
	Symbol(String repr) {
		representation = repr;
	}

	/**
	 * Getter
	 * 
	 * @return Representation.
	 */
	public String getRepresentation() {
		return representation;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object other) {
		if (other instanceof Symbol) {
			Symbol otherSymbol = (Symbol) other;
			return representation.equals(otherSymbol.getRepresentation());
		}
		return false;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode() {
		return representation.hashCode();
	}
}
