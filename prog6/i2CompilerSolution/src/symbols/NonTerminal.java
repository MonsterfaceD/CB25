package symbols;

/**
 * Non-terminal class.
 */
public class NonTerminal implements Alphabet {

	// Name
	private String name;

	/**
	 * Constructor
	 * 
	 * @param name
	 *            Name.
	 */
	public NonTerminal(String name) {
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
		if (other instanceof NonTerminal) {
			NonTerminal otherToken = (NonTerminal) other;
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
