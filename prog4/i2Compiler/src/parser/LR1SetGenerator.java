package parser;

import parser.grammar.AbstractGrammar;
import symbols.Alphabet;
import symbols.NonTerminal;
import symbols.Tokens;
import util.Pair;

import java.util.*;
import java.util.Map.Entry;

/**
 * Generator all LR(0) sets for a given grammar.
 */
public class LR1SetGenerator {

    // Grammar.
    private AbstractGrammar grammar;

    // Complete state space.
    private HashSet<LR1Set> states;

    // Initial state.
    private LR1Set initialState;

    // Transitions.
    private HashMap<Pair<LR1Set, Alphabet>, LR1Set> transitions;

    // LookAheadGenerator
    private LookAheadGenerator lookaheadGen;

    /**
     * Constructor.
     *
     * @param grammar
     *            Grammar.
     */
    public LR1SetGenerator(AbstractGrammar grammar) {
        this.grammar = grammar;
        this.states = new HashSet<LR1Set>();
        this.transitions = new HashMap<Pair<LR1Set, Alphabet>, LR1Set>();
        this.lookaheadGen = new LookAheadGenerator(grammar);
        generateLR1StateSpace();
    }

    /**
     * Add new LR(1) set
     *
     * @param state
     *            LR(1) set
     */
    private void addState(LR1Set state) {
        assert (!states.contains(state));
        states.add(state);
    }

    /**
     * Add new transition.
     *
     * @param source
     *            Source LR(1) set
     * @param letter
     *            Letter
     * @param target
     *            Target LR(1) set
     */
    private void addTransition(LR1Set source, Alphabet letter, LR1Set target) {
        assert (states.contains(source));
        assert (states.contains(target));
        assert (!transitions.containsKey(new Pair<LR1Set, Alphabet>(source, letter)));
        transitions.put(new Pair<LR1Set, Alphabet>(source, letter), target);
    }

    /**
     * Get successor LR(1) set.
     *
     * @param source
     *            Source LR(1) set.
     * @param letter
     *            Letter.
     * @return Returns the letter-successor of source, or null if no such
     *         mapping exists.
     */
    public LR1Set getSuccessor(LR1Set source, Alphabet letter) {
        return transitions.get(new Pair<LR1Set, Alphabet>(source, letter));
    }

    /**
     * Generate all LR(1) sets and the corresponding automaton for the given
     * grammar.
     */
    private void generateLR1StateSpace() {
        // TODO implement state space generation

    }

    /**
     * Compute the epsilon closure for the given LR(1) set.
     *
     * @param set
     *            LR(1) set
     * @return LR(1) set representing the epsilon closure.
     */
    private LR1Set epsilonClosure(LR1Set set) {
        // TODO it might be helpful to implement this method.
        return null;
    }

    /**
     * From a set of rules of the form A -> ab | aC create the "fresh" items A
     * -> * ab, A -> * aC
     *
     * @param lhs
     *            Non-terminal on left-hand side
     * @param lookahead
     *            lookahead for the item
     * @return A set of items with nothing left of the marker
     */
    private LR1Set freshItems(NonTerminal lhs,Alphabet lookahead) {
        LR1Set result = new LR1Set();
        List<Rule> rules = grammar.getRules(lhs);
        for (Rule rule : rules) {
            result.add(LR1Item.freshItem(rule,lookahead));
        }
        return result;
    }

    /**
     * Get number of conflicts.
     *
     * @return Number of conflicts.
     */
    public int nrConflicts() {
        int counter = 0;
        for (LR1Set lr1set : states) {
            if (lr1set.hasConflicts())
                counter++;
        }
        return counter;
    }

    /**
     * Get number of states.
     *
     * @return Number of states.
     */
    public int nrStates() {
        return states.size();
    }

    /**
     * Get initial LR(0) set.
     *
     * @return Initial LR(0) set
     */
    public LR1Set getInitialState() {
        return initialState;
    }

    /**
     * Print all LR(1) sets.
     */
    public void printLR1Sets() {
        for (LR1Set set : states) {
            System.out.println(set.getSequenceAsString() + ": " + set);
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see java.lang.Object#toString()
     */
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("digraph{");
        for (LR1Set state : states) {
            builder.append(state.getSequenceAsString());
            builder.append(" [label=\"");
            builder.append(state.toString());
            builder.append("\"];\n");
        }
        builder.append("initial state: ");
        builder.append(initialState.getSequenceAsString());
        builder.append("\n");

        Iterator<Entry<Pair<LR1Set, Alphabet>, LR1Set>> it = transitions.entrySet().iterator();
        while (it.hasNext()) {
            Entry<Pair<LR1Set, Alphabet>, LR1Set> entry = it.next();
            Pair<LR1Set, Alphabet> pair = entry.getKey();
            builder.append(pair.getFirst().getSequenceAsString());
            builder.append(" -> ");
            builder.append(entry.getValue().getSequenceAsString());
            builder.append(" [label=\"");
            builder.append(pair.getSecond());
            builder.append("\"];\n");
        }
        builder.append("}");
        return builder.toString();
    }
}
