package symbols;

/**
 * Token class.
 */
public class Token implements Alphabet {

	// Name
	private String name;

	/**
	 * Constructor
	 * 
	 * @param name
	 *            Name.
	 */
	public Token(String name) {
		this.name = name;
	}

	/**
	 * Get name.
	 * 
	 * @return Name.
	 */
	public String getName() {
		return name;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode() {
		return name.hashCode();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object other) {
		if (other instanceof Token) {
			Token otherToken = (Token) other;
			return this.getName().equals(otherToken.getName());
		}

		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return name;
	}
}
