import java.util.Set;
import java.util.Stack;
import java.util.Map;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

/**
 * Class representing an NFA
 * TODO implement the acceptWord method. Notice that you are free to change the
 * class, or do not use the methods at all.
 */
public class Automaton {

	// Set of states
	Set<State> states;
	// Alphabet
	Set<Symbol> symbols;
	// Initial states, must be a subset of states
	Set<State> initial;
	// Accepting states, must be a subset of states.
	Set<State> accepting;
	// Transitions of an NFA: State -> Symbol -> Set[States]
	Map<State, Map<Symbol, Set<State>>> transitions;

	/**
	 * Constructor.
	 */
	Automaton() {
		symbols = new HashSet<Symbol>();
		states = new HashSet<State>();
		initial = new HashSet<State>();
		accepting = new HashSet<State>();
		transitions = new HashMap<State, Map<Symbol, Set<State>>>();
	}

	/**
	 * Get the set of initial states
	 * 
	 * @return The set of initial states
	 */
	public Set<State> getInitial() {
		return initial;
	}

	/**
	 * Get the set of accepting states
	 * 
	 * @return The set of accepting states
	 */
	public Set<State> getAccepting() {
		return accepting;
	}

	/**
	 * Adds a new state to the automaton.
	 * 
	 * @param init: True iff state is initial state for this automaton.
	 * @param accept: True iff state is accepting state for this automaton.
	 * @return
	 */
	public State addNewState(Boolean init, Boolean accept) {
		State s = new State();
		states.add(s);
		if (init) {
			initial.add(s);
		}
		if (accept) {
			accepting.add(s);
		}
		transitions.put(s, new HashMap<Symbol, Set<State>>());
		return s;
	}

	/**
	 * Extend this automaton by the states and transitions of another automaton.
	 * Important: Initial/Accepting states in other are no longer initial or
	 * accepting.
	 * 
	 * @param other Another automaton.
	 */
	public void addAutomaton(Automaton other) {
		for (State s : other.states) {
			if (states.contains(s)) {
				throw new IllegalArgumentException("States of the two automata must be disjoint when merging");
			}
			states.add(s);
		}
		symbols.addAll(other.symbols);
		transitions.putAll(other.transitions);
	}

	/**
	 * Adds a transition from source to target, labels the transition with the
	 * symbol
	 * 
	 * @param source Source state
	 * @param symbol Symbol
	 * @param target Target state
	 */
	public void addTransition(State source, Symbol symbol, State target) {
		Map<Symbol, Set<State>> outTransitions = transitions.get(source);
		symbols.add(symbol);
		if (outTransitions.containsKey(symbol)) {
			outTransitions.get(symbol).add(target);
		} else {
			Set<State> set = new HashSet<State>();
			set.add(target);
			outTransitions.put(symbol, set);
		}
	}

	/**
	 * Mark a given state which is already in the automaton as initial
	 * 
	 * @param s State
	 */
	public void makeInitial(State s) {
		if (states.contains(s)) {
			initial.add(s);
		} else {
			throw new IllegalArgumentException("State can only be initial if it is part of the state space");
		}
	}

	/**
	 * Mark a given state which is already in the automaton accepting.
	 * 
	 * @param s State
	 */
	public void makeAccepting(State s) {
		if (states.contains(s)) {
			accepting.add(s);
		} else {
			throw new IllegalArgumentException("State can only be initial if it is part of the state space");
		}
	}

	/**
	 * Removes unreachable states.
	 */
	public void removeUnreachable() {
		Set<State> marked = new HashSet<State>();
		Stack<State> S = new Stack<State>();
		S.addAll(initial);
		while (!S.isEmpty()) {
			State v = S.pop();
			if (!marked.contains(v)) {
				marked.add(v);
				for (Set<State> targetsPerSymbol : transitions.get(v).values()) {
					S.addAll(targetsPerSymbol);
				}
			}
		}
		for (State s : states) {
			if (!marked.contains(s)) {
				transitions.remove(s);
			}
		}
		states.retainAll(marked);
		accepting.retainAll(marked);

	}

	/**
	 * Removes epsilon transitions
	 */
	public void removeEpsilonTransitions() {
		for (State s : states) {
			Set<State> marked = new HashSet<State>();
			marked.add(s);
			while (true) {
				// System.out.println(s.getId());
				Set<State> epsReach = transitions.get(s).getOrDefault(Symbol.EPS, new HashSet<State>());
				Set<State> epsReachPrime = new HashSet<State>();
				Iterator<State> it = epsReach.iterator();
				while (it.hasNext()) {
					State t = it.next();
					if (t.equals(s)) {
						continue;
					}
					marked.add(t);
					for (Map.Entry<Symbol, Set<State>> aX : transitions.get(t).entrySet()) {
						if (aX.getKey().equals(Symbol.EPS)) {
							for (State tp : aX.getValue()) {
								if (!marked.contains(tp)) {
									epsReachPrime.add(tp);
								}
							}
						} else {
							if (transitions.get(s).containsKey(aX.getKey())) {
								transitions.get(s).get(aX.getKey()).addAll(transitions.get(t).get(aX.getKey()));
							} else {
								transitions.get(s).put(aX.getKey(), aX.getValue());
							}
						}
					}
					if (accepting.contains(t) && !accepting.contains(s)) {
						accepting.add(s);
					}
					marked.add(t);
				}
				transitions.get(s).put(Symbol.EPS, epsReachPrime);
				if (epsReachPrime.isEmpty()) {
					break;
				}
			}
		}
		// removeUnreachable();
	}

	/**
	 * @param word String representation of a word using the alphabet of the
	 *             automaton
	 * @return Accept(List<Symbol> symword), where symword is created by translating
	 *         word character by character
	 */
	public Boolean accept(String word) {
		Map<String, Symbol> symMap = new HashMap<String, Symbol>();
		for (Symbol s : symbols) {
			symMap.put(s.getRepresentation(), s);
		}
		List<Symbol> symword = new ArrayList<Symbol>();
		for (char c : word.toCharArray()) {
			if (!symMap.containsKey(Character.toString(c))) {
				return false;
				// throw new IllegalArgumentException(String.format("Word contains unknown
				// character '%s'!", Character.toString(c)));
			}
			symword.add(symMap.get(Character.toString(c)));
		}
		return accept(symword);
	}

	/**
	 * Gets a list of symbols, decides whether the word is in the language
	 * represented by the automaton
	 * 
	 * @param word List of symbols
	 * @return true if word is in the language
	 */
	public Boolean accept(List<Symbol> word) {
		// TODO implement
		return false;
	}

	/**
	 * Exports the automaton in the dot format. Feel free to change or extend this
	 * to your taste. Currently, initial states are blue, accepting states are red.
	 * Epsilons are not labeled.
	 * 
	 * @return A string in the dot format.
	 */
	public String dotFormat() {
		StringBuilder result = new StringBuilder();
		result.append("digraph autom {");

		for (State s : states) {
			result.append("\t");
			result.append("s");
			result.append(s.getId());
			if (initial.contains(s)) {
				result.append(" [color=\"blue\"]");
			}
			if (accepting.contains(s)) {
				result.append(" [color=\"red\"]");
			}
			result.append(";\n");
		}

		for (Map.Entry<State, Map<Symbol, Set<State>>> transitionEntry : transitions.entrySet()) {
			for (Map.Entry<Symbol, Set<State>> targetEntry : transitionEntry.getValue().entrySet()) {
				for (State targetState : targetEntry.getValue()) {
					result.append("\t");
					result.append("s");
					result.append(transitionEntry.getKey().getId());
					result.append(" -> ");
					result.append("s");
					result.append(targetState.getId());

					result.append(" [label=\"");
					result.append(targetEntry.getKey().getRepresentation());
					result.append("\"];");
					result.append("\n");
				}
			}
		}
		result.append("}");

		return result.toString();
	}
}
