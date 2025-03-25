package parser;

import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.HashSet;

import parser.grammar.AbstractGrammar;
import parser.grammar.WhileGrammar;
import symbols.*;
import util.MapSet;

/**
 * Generator for first and follow sets for a given grammar.
 */
public class LookAheadGenerator {

	// Grammar.
	private AbstractGrammar grammar;

	// First sets for each non-terminal
	private MapSet<NonTerminal, Alphabet> first;

	/**
	 * Constructor.
	 * 
	 * @param grammar
	 *            Grammar.
	 */
	public LookAheadGenerator(AbstractGrammar grammar) {
		this.grammar = grammar;
		computeFirst();
	}

	/**
	 * Compute the first set for each non-terminal.
	 */
	private void computeFirst() {
		first = new MapSet<NonTerminal, Alphabet>();
		List<Rule> rules = grammar.getRules();
		Iterator<Rule> iter = rules.iterator();
		while (iter.hasNext()) {
			Rule rule = iter.next();
			if (rule.getRhs().length > 0) {
				// Add terminal symbol to first set
				if (rule.getRhs()[0] instanceof Token) {
					first.add(rule.getLhs(), rule.getRhs()[0]);
					// Rule is not important anymore
					iter.remove();
				}
			} else {
				// Add epsilon to first set
				first.add(rule.getLhs(), Tokens.EPSILON);
				// Rule is not important anymore
				iter.remove();
			}
		}

		// Repeat until fix point is reached
		boolean changed = true;
		while (changed) {
			changed = false;

			for (Rule rule : rules) {
				NonTerminal nonTerminal = rule.getLhs();
				// Iterate over all symbols
				for (int i = 0; i < rule.getRhs().length; i++) {
					if (rule.getRhs()[i] instanceof Token) {
						if (!first.contains(nonTerminal, rule.getRhs()[i])) {
							// The symbol is a terminal and we take it
							first.add(nonTerminal, rule.getRhs()[i]);
							changed = true;
						}
						break;
					} else {
						// A -> B and B -> a ==> a in first(A)
						boolean foundEpsilon = false;
						assert (rule.getRhs()[i] instanceof NonTerminal);
						NonTerminal newNonTerminal = (NonTerminal) rule.getRhs()[i];
						for (Alphabet symbol : first.get(newNonTerminal)) {
							if (!first.contains(nonTerminal, symbol)) {
								first.add(nonTerminal, symbol);
								changed = true;
							}
							if (symbol.equals(Tokens.EPSILON)) {
								foundEpsilon = true;
							}
						}
						if (!foundEpsilon) {
							break;
						}
					}
				}
			}
		}
	}

	/**
	 * Check if the first set for the given non-terminal contains the given
	 * symbol.
	 * 
	 * @param nonTerminal
	 *            Non-terminal
	 * @param symbol
	 *            Symbol
	 * @return True iff the follow set contains the symbol
	 */
	public boolean containsFirst(NonTerminal nonTerminal, Alphabet symbol) {
		return first.contains(nonTerminal, symbol);
	}

	/**
	 * Computes the first set for the given alphabet list
	 *
	 * @param symbolList
	 * 			list of alphabet symbols
	 * @return the first set of the array of alphabet symbols
	 */
	public Set<Alphabet> computeFirst(Alphabet[] symbolList){
		Set<Alphabet> firstSet = new HashSet<>();
		for(Alphabet symbol: symbolList){
			if(symbol instanceof Token){
				firstSet.add(symbol);
				break;
			}else if(symbol instanceof NonTerminal){
				firstSet.addAll(first.get((NonTerminal)symbol));
				if(!first.get((NonTerminal)symbol).contains(Tokens.EPSILON)){
					break;
				}
			}
		}
		return firstSet;
	}

	/**
	 * Print first sets.
	 */
	public void printFirstSets() {
		for (NonTerminal nonTerminal : first.keySet()) {
			System.out.print("fi(" + nonTerminal + "): {");
			Iterator<Alphabet> iter = first.get(nonTerminal).iterator();
			while (iter.hasNext()) {
				System.out.print(iter.next());
				if (iter.hasNext()) {
					System.out.print(", ");
				}
			}
			System.out.println("}");
		}
	}
}
