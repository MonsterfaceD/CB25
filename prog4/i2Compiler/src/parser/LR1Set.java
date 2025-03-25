package parser;

import symbols.Alphabet;
import symbols.NonTerminal;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Represents LR0Sets for a given symbol sequence.
 */
public class LR1Set extends HashSet<LR1Item> {

    private static final long serialVersionUID = 1L;

    // Symbol sequence
    private ArrayList<Alphabet> sequence;

    /**
     * Constructor.
     */
    public LR1Set() {
        this.sequence = new ArrayList<Alphabet>();
    }

    /**
     * Constructor.
     *
     * @param sequence
     *            Symbol sequence for LR(0) set.
     */
    public LR1Set(ArrayList<Alphabet> sequence) {
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
    public LR1Set(ArrayList<Alphabet> sequence, Alphabet symbol) {
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
        //TODO implement check for conflicts

        return false;
    }

    /**
     * Get all shiftable symbols.
     *
     * @return Set of shiftable symbols.
     */
    public HashSet<Alphabet> getShiftableSymbols() {
        HashSet<Alphabet> symbols = new HashSet<Alphabet>();
        for (LR1Item item : this) {
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
    public LR1Set getShiftedItemsFor(Alphabet symbol) {
        LR1Set result = new LR1Set(getSequence(), symbol);
        for (LR1Item item : this) {
            if (item.canShift() && item.getShiftableSymbolName().equals(symbol)) {
                result.add(item.getShiftedItem());
            }
        }
        return result;
    }


    /**
     * Check if the set contains a complete item of the form [A -> alpha., lookahead].
     *
     * @param lookahead the lookahead to check for
     *
     * @return True iff the set contains a complete item
     */
    public boolean containsCompleteItem(Alphabet lookahead) {
        for (LR1Item item : this) {
            if (item.canReduce(lookahead)) {
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
        for(LR1Item item : getCompleteItems()){
            if (item.getLhs() == start) {
                return true;
            }
        }
        return false;
    }

    /**
     * Return the set of completed items
     *
     * @return Complete items
     */
    public Set<LR1Item> getCompleteItems() {
        HashSet<LR1Item> items = new HashSet<LR1Item>();
        for (LR1Item item : this) {
            if (!item.canShift()) {
                items.add(item);
            }
        }
        return items;
    }

    /**
     * Returns the completed item for a given lookahead (assuming it is unique) and null if there is no
     *
     * @return complete item or null
     */
    public LR1Item getCompleteItem(Alphabet lookahead){
        for (LR1Item item : this) {
            if(item.canReduce(lookahead)){
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
        Iterator<LR1Item> it = this.iterator();
        while (it.hasNext()) {
            result.append(it.next());
            if (it.hasNext()) {
                result.append(", ");
            }
        }
        return result.toString();
    }
}
