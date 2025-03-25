package parser;

import java.util.Arrays;

import symbols.Alphabet;
import symbols.NonTerminal;
import symbols.Token;
import symbols.Tokens;

/**
 * Represents an LR(1) item as [lhs -> first MARKER last, lookahead]
 */
public class LR1Item extends LR0Item {
	
	// Lookahead token for this LR(1) item
	private final Alphabet lookahead;

	/**
	 * Constructor.
	 * 
	 * @param lhs
	 *            Non-terminal on left-hand side.
	 * @param rhs
	 *            Right-hand side.
	 * @param marker
	 *            Position of marker.
	 * @param lookahead
	 *            the lookahead for this item
	 */
	public LR1Item(NonTerminal lhs, Alphabet[] rhs, int marker, Alphabet lookahead) {
		super(lhs, rhs, marker);
		this.lookahead = lookahead;
	}

	/**
	 * Check if a shift over a terminal is possible.
	 *
	 * @return True iff the marker is followed by a terminal symbol
	 */
	public boolean canShiftOverTerminal(Alphabet lookahead) {
		return super.canShiftOverTerminal() && lookahead.equals(getRhs()[marker]);
	}

	/**
	 * @deprecated this method can not be used for LR1Items
	 * @throws UnsupportedOperationException since LR1Items require a lookahead
	 */
	public boolean canShiftOverTerminal(){
		throw new UnsupportedOperationException();
	}

	/**
	 * Check if a reduce is possible.
	 *
	 * @param lookahead
	 * 				lookahead to check against
	 *
	 * @return True iff there comes nothing after the marker
	 */
	public boolean canReduce(Alphabet lookahead) {
		if(super.canReduce() && lookahead == null && this.lookahead.equals(Tokens.EPSILON))
			return true; // lookahead is EPSILON
		return super.canReduce() && lookahead.equals(this.lookahead);
	}

	/**
	 * @deprecated LR1Item need a lookahead
	 * @throws UnsupportedOperationException since LR1Items cannot be reduced without a lookahead
	 */
	public boolean canReduce() {
		throw new UnsupportedOperationException();
	}

	/**
	 * Get new item after one shift.
	 *
	 * @return New LR(1) item
	 */
	public LR1Item getShiftedItem() {
		assert (canShift());
		return new LR1Item(getLhs(), getRhs(), marker + 1, this.lookahead);
	}

	/**
	 * Get the lookahead sequence to compute the first set for
	 *
	 * @return the lookahead sequence
	 */
	public Alphabet[] getLookaheadSequence() {
		Alphabet[] loSequence = Arrays.copyOfRange(getRhs(),marker+1,getRhs().length+1);
		loSequence[loSequence.length-1] = this.lookahead;
		return loSequence;
	}

	/**
	 * Get lookahead
	 *
	 * @return the lookahead
	 */
	public Alphabet getLookahead(){
		return lookahead;
	}

	/**
	 * Given a grammar production rule like S -> alpha return an item [S ->
	 * .alpha, lookahead]
	 * 
	 * @param rule
	 *            Rule
	 * @return LR(1) item created from rule.
	 */
	public static LR1Item freshItem(Rule rule, Alphabet lookahead) {
		return new LR1Item(rule.getLhs(), rule.getRhs(), 0, lookahead);
	}

	/**
	 * @deprecated LR1Item need a lookahead
	 * @throws UnsupportedOperationException since LR1Items cannot be created without a lookahead
	 */
	public static LR1Item freshItem(Rule rule){
		throw new UnsupportedOperationException();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode() {
		return super.hashCode()+lookahead.hashCode();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object a) {
		if (a instanceof LR1Item) {
			LR1Item other = (LR1Item) a;
			return 	   other.getLhs().equals(getLhs())
					&& other.marker == marker
					&& Arrays.equals(other.getRhs(), getRhs())
					&& lookahead.equals(other.lookahead);
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		StringBuilder result = new StringBuilder();
		result.append("[ ");
		result.append(getLhs());
		result.append(" -> ");
		for (int i = 0; i < marker; i++) {
			result.append(getRhs()[i]);
			result.append(" ");
		}
		result.append(". ");
		for (int i = marker; i < getRhs().length; i++) {
			result.append(getRhs()[i]);
			result.append(" ");
		}
		result.append(", ");
		result.append(lookahead);
		result.append("]");
		return result.toString();
	}
}
