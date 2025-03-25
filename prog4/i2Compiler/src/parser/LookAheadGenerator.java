package parser;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import parser.grammar.AbstractGrammar;
import symbols.Alphabet;
import symbols.NonTerminal;
import symbols.Token;
import symbols.Tokens;
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
	public void computeFirst() {
		first = new MapSet<NonTerminal, Alphabet>();
		// TODO implement

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
