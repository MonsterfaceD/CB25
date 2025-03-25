package parser.grammar;

import symbols.Alphabet;
import symbols.NonTerminal;
import symbols.NonTerminals;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import parser.Rule;

/**
 * Abstract class representing a context free grammar.
 */
public abstract class AbstractGrammar {

	// All rules in the grammar.
	private HashMap<NonTerminal, List<List<Alphabet>>> rules;

	// Start symbol.
	private NonTerminal start;

	// Non-terminals
	private NonTerminals nonTerminals;

	/**
	 * Constructor.
	 */
	public AbstractGrammar() {
		rules = new HashMap<NonTerminal, List<List<Alphabet>>>();
		nonTerminals = new NonTerminals();
	}

	/**
	 * Set start symbol.
	 * 
	 * @param start
	 *            Start symbol.
	 */
	protected void setStart(NonTerminal start) {
		this.start = start;
	}

	/**
	 * Get start symbol.
	 * 
	 * @return Start symbol.
	 */
	public NonTerminal getStart() {
		return start;
	}

	/**
	 * Add non-terminal.
	 * 
	 * @param name
	 *            Name.
	 * @return Newly created non-terminal.
	 */
	protected NonTerminal addNonTerminal(String name) {
		NonTerminal nonTerminal = new NonTerminal(name);
		nonTerminals.add(nonTerminal);
		return nonTerminal;
	}

	/**
	 * Get non-terminal with given name.
	 * 
	 * @param name
	 *            Name.
	 * @return Non-terminal or null if no non-terminal with the name exists.
	 */
	protected NonTerminal getNonTerminal(String name) {
		NonTerminal nonTerminal = new NonTerminal(name);
		if (nonTerminals.contains(nonTerminal)) {
			return nonTerminal;
		} else {
			return null;
		}
	}

	/**
	 * Add rule to grammar.
	 * 
	 * @param name
	 *            NonTermininal on left-hand side.
	 * @param choice
	 *            One choice of the right-hand side.
	 */
	public void addRule(NonTerminal name, List<Alphabet> choice) {
		List<List<Alphabet>> alternatives = null;
		if (rules.containsKey(name)) {
			alternatives = rules.get(name);
		} else {
			alternatives = new ArrayList<List<Alphabet>>();
			rules.put(name, alternatives);
		}

		alternatives.add(choice);
	}

	/**
	 * Get all rules for a given non-terminal.
	 * 
	 * @param name
	 *            Non-terminal on left-hand side.
	 * @return All rules.
	 */
	public List<Rule> getRules(NonTerminal name) {
		List<Rule> result = new ArrayList<Rule>();
		if (rules.containsKey(name)) {
			for (List<Alphabet> rhs : rules.get(name)) {
				result.add(new Rule(name, rhs));
			}
		}
		return result;
	}

	/**
	 * Get all rules.
	 * 
	 * @return List of all rules.
	 */
	public List<Rule> getRules() {
		List<Rule> result = new ArrayList<Rule>();
		for (NonTerminal nonTerminal : rules.keySet()) {
			for (List<Alphabet> rhs : rules.get(nonTerminal)) {
				result.add(new Rule(nonTerminal, rhs));
			}
		}
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		StringBuilder result = new StringBuilder();
		for (NonTerminal nonTerminal : rules.keySet()) {
			result.append(nonTerminal);
			result.append(" -> ");
			List<List<Alphabet>> allRules = rules.get(nonTerminal);
			for (int i = 0; i < allRules.size(); i++) {
				List<Alphabet> alternatives = allRules.get(i);
				for (Alphabet symbol : alternatives) {
					result.append(symbol);
					result.append(" ");
				}
				if (i < allRules.size() - 1) {
					result.append(" | ");
				}
			}
			result.append("\n");
		}
		return result.toString();
	}
}