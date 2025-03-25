package parser;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.stream.Collectors;

import symbols.Alphabet;
import symbols.NonTerminal;

/**
 * Represents LR0Sets for a given symbol sequence.
 */
public class LR0Set extends HashSet<LR0Item> {

	private static final long serialVersionUID = 1L;

	// Symbol sequence
	private ArrayList<Alphabet> sequence;

	/**
	 * Constructor.
	 */
	public LR0Set() {
		this.sequence = new ArrayList<Alphabet>();
	}

	/**
	 * Constructor.
	 * 
	 * @param sequence
	 *            Symbol sequence for LR(0) set.
	 */
	public LR0Set(ArrayList<Alphabet> sequence) {
		this.sequence = new ArrayList<Alphabet>(sequence);
	}

	/**
	 * Constructor.
	 * 
	 * @param sequence
	 *            Symbol sequence for LR(0) set.
	 * @param symbol
	 *            Additional symbol.
	 */
	public LR0Set(ArrayList<Alphabet> sequence, Alphabet symbol) {
		this.sequence = new ArrayList<Alphabet>(sequence);
		this.sequence.add(symbol);
	}

	/**
	 * Get the symbol sequence.
	 * 
	 * @return Name
	 */
	public ArrayList<Alphabet> getSequence() {
		return sequence;
	}

	/**
	 * Get sequence as string.
	 * 
	 * @return String representation of sequence.
	 */
	public String getSequenceAsString() {
		return sequence.stream().map(Object::toString).collect(Collectors.joining(", "));
	}

	/**
	 * Check for conflicts.
	 * 
	 * @return True iff the set has conflicts
	 */
	public boolean hasConflicts() {
		int canShift = 0;
		int canReduce = 0;

		for (LR0Item item : this) {
			if (item.canShiftOverTerminal())
				canShift++;
			if (item.canReduce())
				canReduce++;
		}

		if (canReduce >= 2) {
			System.out.println("RED/RED conflict " + this);
			return true; // reduce/reduce conflict
		}
		if (canReduce >= 1 && canShift >= 1) {
			System.out.println("SFT/RED conflict " + this);
			return true; // shift/reduce conflict
		}

		return false;
	}

	/**
	 * Get all shiftable symbols.
	 * 
	 * @return Set of shiftable symbols.
	 */
	public HashSet<Alphabet> getShiftableSymbols() {
		HashSet<Alphabet> symbols = new HashSet<Alphabet>();
		for (LR0Item item : this) {
			if (item.canShift()) {
				symbols.add(item.getShiftableSymbolName());
			}
		}
		return symbols;
	}

	/**
	 * Get new LR(0) items after shift with given symbol.
	 * 
	 * @param symbol
	 *            Symbol to shift.
	 * @return A subset (of the current LR(0) set) which contains those items
	 *         that allow for a shift with the given symbol
	 */
	public LR0Set getShiftedItemsFor(Alphabet symbol) {
		LR0Set result = new LR0Set(getSequence(), symbol);
		for (LR0Item item : this) {
			if (item.canShift() && item.getShiftableSymbolName().equals(symbol)) {
				result.add(item.getShiftedItem());
			}
		}
		return result;
	}

	/**
	 * Check if the set contains a complete item of the form [A -> alpha.].
	 * 
	 * @return True iff the set contains a complete item
	 */
	public boolean containsCompleteItem() {
		for (LR0Item item : this) {
			if (!item.canShift()) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Check if the set contains a final item.
	 * 
	 * @param start
	 *            Start symbol of grammar
	 * @return True iff the set contains a final item.
	 */
	public boolean containsFinalItem(NonTerminal start) {
		LR0Item item = getCompleteItem();
		if (item != null) {
			if (item.getLhs() == start) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Assuming there is one complete item in this set, return it. Returns null
	 * if there is no complete item.
	 * 
	 * @return Complete item
	 */
	public LR0Item getCompleteItem() {
		for (LR0Item item : this) {
			if (!item.canShift()) {
				return item;
			}
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.AbstractCollection#toString()
	 */
	public String toString() {
		StringBuilder result = new StringBuilder();
		Iterator<LR0Item> it = this.iterator();
		while (it.hasNext()) {
			result.append(it.next());
			if (it.hasNext()) {
				result.append(", ");
			}
		}
		return result.toString();
	}
}
