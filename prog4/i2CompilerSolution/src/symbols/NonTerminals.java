package symbols;

import java.util.HashSet;

/**
 * All non-terminals in a grammar.
 */
public class NonTerminals extends HashSet<NonTerminal> {

	private static final long serialVersionUID = 1L;

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.AbstractMap#toString()
	 */
	public String toString() {
		String str = "[";
		boolean first = true;
		for (NonTerminal entry : this) {
			if (first) {
				first = false;
			} else {
				str += ", ";
			}
			str += entry;
		}
		str += "]";
		return str;
	}
}
