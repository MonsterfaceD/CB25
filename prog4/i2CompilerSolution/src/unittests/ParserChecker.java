package unittests;

import parser.*;
import parser.grammar.AbstractGrammar;
import symbols.Alphabet;
import symbols.NonTerminal;
import symbols.Symbol;
import util.MapSet;
import util.Pair;

import java.lang.reflect.Field;
import java.util.*;

public class ParserChecker {

	public class Result {

		private String errorMessage;
		private boolean correct;

		public Result() {
			this.correct = true;
			this.errorMessage = "";
		}

		public void addError(String errorMessage) {
			this.correct = false;
			this.errorMessage += errorMessage + "\n";
		}

		public void add(Result result) {
			this.correct = this.correct && result.isCorrect();
			this.errorMessage += result.getErrors();
		}

		public boolean isCorrect() {
			return this.correct;
		}

		public String getErrors() {
			return this.errorMessage;
		}
	}

	private AbstractGrammar grammar;

	LR0SetGenerator lr0generator;

	LR0SetGeneratorReference lr0reference;

	LR1SetGenerator lr1generator;

	LR1SetGeneratorReference lr1reference;

	public ParserChecker(AbstractGrammar grammar) {
		this.grammar = grammar;
	}

	public String checkAll(boolean validLR0, boolean validLR1) {
		String output = "";
		try {
			lr0generator = new LR0SetGenerator(grammar);
			lr0reference = new LR0SetGeneratorReference(grammar);
			Result resultLR0Sets = checkLR0Sets();
			if (resultLR0Sets.isCorrect()) {
				output += "LR0 sets correct.\n";
			} else {
				output += resultLR0Sets.getErrors();
			}
			Result resultLR0Conflicts = checkLR0Conflicts(!validLR0);
			if (resultLR0Conflicts.isCorrect()) {
				output += "LR0 conflicts correct.\n";
			} else {
				output += resultLR0Conflicts.getErrors();
			}
			Result resultFirst = checkFirst();
			if (resultFirst.isCorrect()) {
				output += "First sets correct.\n";
			} else {
				output += resultFirst.getErrors();
			}
			lr1generator = new LR1SetGenerator(grammar);
			lr1reference = new LR1SetGeneratorReference(grammar);
			Result resultLR1Sets = checkLR1Sets();
			if(resultLR1Sets.isCorrect()) {
				output += "LR1 sets correct.\n";
			} else {
				output += resultLR1Sets.getErrors();
			}
			Result resultLR1Conflicts = checkLR1Conflicts(!validLR1);
			if (resultLR1Sets.isCorrect()){
				output += "LR1 conflicts correct.\n";
			} else {
				output += resultLR1Conflicts.getErrors();
			}
		} catch (Exception e) {
			output += "Exception occured: " + e.getMessage();
			e.printStackTrace();
		}
		return output;
	}

	private Result checkLR0Sets() {
		Result result = new Result();

		try {
			// Get states
			Field fieldStates = lr0generator.getClass().getDeclaredField("states");
			fieldStates.setAccessible(true);
			@SuppressWarnings("unchecked")
			HashSet<LR0Set> states = new HashSet<LR0Set>((HashSet<LR0Set>) fieldStates.get(lr0generator));
			HashSet<LR0Set> checkerStates = new HashSet<LR0Set>(lr0reference.states);

			// Get transitions
			Field fieldTransitions = lr0generator.getClass().getDeclaredField("transitions");
			fieldTransitions.setAccessible(true);
			@SuppressWarnings("unchecked")
			HashMap<Pair<LR0Set, Alphabet>, LR0Set> transitions = new HashMap<Pair<LR0Set, Alphabet>, LR0Set>(
					(HashMap<Pair<LR0Set, Alphabet>, LR0Set>) fieldTransitions.get(lr0generator));

			// Check states
			if (lr0generator.nrStates() != lr0reference.nrStates()) {
				result.addError("Nr of LR0 sets is wrong.");
			}
			for (Iterator<LR0Set> iter = states.iterator(); iter.hasNext();) {
				LR0Set element = iter.next();
				if (checkerStates.remove(element)) {
					iter.remove();
				}
			}
			if (!states.isEmpty()) {
				result.addError("Wrong LR0 states: " + Arrays.toString(states.toArray()));
			}
			if (!checkerStates.isEmpty()) {
				result.addError("LR0 States missing: " + Arrays.toString(checkerStates.toArray()));
			}

			// Check transitions

		} catch (Exception e) {
			result.addError("Exception occured: " + e.getMessage());
			e.printStackTrace();
		}

		return result;
	}

	private Result checkLR1Sets() {
		Result result = new Result();

		try {
			// Get states
			Field fieldStates = lr1generator.getClass().getDeclaredField("states");
			fieldStates.setAccessible(true);
			@SuppressWarnings("unchecked")
			HashSet<LR1Set> states = new HashSet<LR1Set>((HashSet<LR1Set>) fieldStates.get(lr1generator));
			HashSet<LR1Set> checkerStates = new HashSet<LR1Set>(lr1reference.states);

			// Get transitions
			Field fieldTransitions = lr1generator.getClass().getDeclaredField("transitions");
			fieldTransitions.setAccessible(true);
			@SuppressWarnings("unchecked")
			HashMap<Pair<LR1Set, Alphabet>, LR1Set> transitions = new HashMap<Pair<LR1Set, Alphabet>, LR1Set>(
					(HashMap<Pair<LR1Set, Alphabet>, LR1Set>) fieldTransitions.get(lr1generator));

			// Check states
			if (lr1generator.nrStates() != lr1reference.nrStates()) {
				result.addError("Nr of LR1 sets is wrong.");
			}
			for (Iterator<LR1Set> iter = states.iterator(); iter.hasNext();) {
				LR1Set element = iter.next();
				if (checkerStates.remove(element)) {
					iter.remove();
				}
			}
			if (!states.isEmpty()) {
				result.addError("Wrong LR1 states: " + Arrays.toString(states.toArray()));
			}
			if (!checkerStates.isEmpty()) {
				result.addError("LR1 States missing: " + Arrays.toString(checkerStates.toArray()));
			}

			// Check transitions

		} catch (Exception e) {
			result.addError("Exception occured: " + e.getMessage());
			e.printStackTrace();
		}

		return result;
	}

	private Result checkLR0Conflicts(boolean expectConflicts) {
		Result result = new Result();

		try {
			// Get states
			Field fieldStates = lr0generator.getClass().getDeclaredField("states");
			fieldStates.setAccessible(true);
			@SuppressWarnings("unchecked")
			HashSet<LR0Set> states = new HashSet<LR0Set>((HashSet<LR0Set>) fieldStates.get(lr0generator));
			HashSet<LR0Set> checkerStates = new HashSet<LR0Set>(lr0reference.states);

			// Only keep conflicting sets
			for (Iterator<LR0Set> iter = states.iterator(); iter.hasNext();) {
				LR0Set element = iter.next();
				if (!element.hasConflicts()) {
					iter.remove();
				}
			}
			for (Iterator<LR0Set> iter = checkerStates.iterator(); iter.hasNext();) {
				LR0Set element = iter.next();
				if (!lr0reference.hasConflicts(element)) {
					iter.remove();
				}
			}

			// Check conflicts
			if (expectConflicts) {
				if (states.isEmpty()) {
					result.addError("Expected conflicts but none were found.");
				}
				if (checkerStates.isEmpty()) {
					result.addError("Expected conflicts but none were found in reference.");
				}
			} else {
				if (!states.isEmpty()) {
					result.addError("Expected no conflicts but some were found.");
				}
				if (!checkerStates.isEmpty()) {
					result.addError("Expected no conflicts but some were found in reference.");
				}
			}
			for (Iterator<LR0Set> iter = states.iterator(); iter.hasNext();) {
				LR0Set element = iter.next();
				if (checkerStates.remove(element)) {
					iter.remove();
				}
			}
			if (!states.isEmpty()) {
				result.addError("Wrong LR0 conflicts: " + Arrays.toString(states.toArray()));
			}
			if (!checkerStates.isEmpty()) {
				result.addError("LR0 conflicts missing: " + Arrays.toString(checkerStates.toArray()));
			}
		} catch (Exception e) {
			result.addError("Exception occured: " + e.getMessage());
			e.printStackTrace();
		}

		return result;
	}

	private Result checkLR1Conflicts(boolean expectConflicts) {
		Result result = new Result();

		try {
			// Get states
			Field fieldStates = lr1generator.getClass().getDeclaredField("states");
			fieldStates.setAccessible(true);
			@SuppressWarnings("unchecked")
			HashSet<LR1Set> states = new HashSet<LR1Set>((HashSet<LR1Set>) fieldStates.get(lr1generator));
			HashSet<LR1Set> checkerStates = new HashSet<LR1Set>(lr1reference.states);

			// Only keep conflicting sets
			for (Iterator<LR1Set> iter = states.iterator(); iter.hasNext();) {
				LR1Set element = iter.next();
				if (!element.hasConflicts()) {
					iter.remove();
				}
			}
			for (Iterator<LR1Set> iter = checkerStates.iterator(); iter.hasNext();) {
				LR1Set element = iter.next();
				if (!lr1reference.hasConflicts(element)) {
					iter.remove();
				}
			}

			// Check conflicts
			if (expectConflicts) {
				if (states.isEmpty()) {
					result.addError("Expected conflicts but none were found.");
				}
				if (checkerStates.isEmpty()) {
					result.addError("Expected conflicts but none were found in reference.");
				}
			} else {
				if (!states.isEmpty()) {
					result.addError("Expected no conflicts but some were found.");
				}
				if (!checkerStates.isEmpty()) {
					result.addError("Expected no conflicts but some were found in reference.");
				}
			}
			for (Iterator<LR1Set> iter = states.iterator(); iter.hasNext();) {
				LR1Set element = iter.next();
				if (checkerStates.remove(element)) {
					iter.remove();
				}
			}
			if (!states.isEmpty()) {
				result.addError("Wrong LR1 conflicts: " + Arrays.toString(states.toArray()));
			}
			if (!checkerStates.isEmpty()) {
				result.addError("LR1 conflicts missing: " + Arrays.toString(checkerStates.toArray()));
			}
		} catch (Exception e) {
			result.addError("Exception occured: " + e.getMessage());
			e.printStackTrace();
		}

		return result;
	}

	private Result checkFirst() {
		Result result = new Result();

		LookAheadGenerator generatorLookAhead = new LookAheadGenerator(grammar);
		LookAheadGeneratorReference lookAheadReference = new LookAheadGeneratorReference(grammar);

		try {
			// Get first set
			Field fieldFirst = generatorLookAhead.getClass().getDeclaredField("first");
			fieldFirst.setAccessible(true);
			@SuppressWarnings("unchecked")
			MapSet<NonTerminal, Alphabet> first = (MapSet<NonTerminal, Alphabet>) fieldFirst.get(generatorLookAhead);
			MapSet<NonTerminal, Alphabet> firstReference = lookAheadReference.first;

			// Check first
			result.add(checkMapSetEquality("first", first, firstReference));
		} catch (Exception e) {
			result.addError("Exception occured: " + e.getMessage());
			e.printStackTrace();
		}

		return result;
	}

	public String checkLR0Parsing(List<Symbol> input, boolean expectValid) {
		String result = "";
		LR0Parser parserLR0 = new LR0Parser(grammar);
		List<Rule> analysisLR0 = null;
		try {
			analysisLR0 = parserLR0.parse(new ArrayList<Symbol>(input));
		} catch (ParserException e) {
			if (expectValid) {
				return "LR0 ParseErr: " + e.getMessage() + ", " + e.getAnalysisBeforeFailure() + "\n";
			} else {
				return "LR0 parsing correct.\n";
			}
		}
		if (!expectValid) {
			return "LR0 parsing should not be successful: " + analysisLR0 + "\n";
		}

		LR0ParserReference parserReference = new LR0ParserReference(grammar);
		List<Rule> analysisReference = null;
		try {
			analysisReference = parserReference.parse(input);
		} catch (ParserException e) {
			return "LR0 ParseErr of Reference: " + e.getMessage() + ", " + e.getAnalysisBeforeFailure()
					+ "\nStudent implementation parsed: " + analysisLR0;
		}

		// Check analysis
		if (analysisLR0.size() != analysisReference.size()) {
			result += "Different LR0 analysis sizes.\n";
		}
		for (int i = 0; i < analysisLR0.size() && i < analysisReference.size(); ++i) {
			if (!analysisLR0.get(i).equals(analysisReference.get(i))) {
				return "Incorrect LR0 parsing on index " + i + ": " + analysisLR0.get(i) + "\n";
			}
		}

		if (result.isEmpty()) {
			return "LR0 parsing correct.\n";
		}
		return result;
	}

	public String checkLR1Parsing(List<Symbol> input, boolean expectValid) {
		String result = "";
		LR1Parser parserLR1 = new LR1Parser(grammar);
		List<Rule> analysisLR1 = null;
		try {
			analysisLR1 = parserLR1.parse(new ArrayList<Symbol>(input));
		} catch (ParserException e) {
			if (expectValid) {
				return "LR1 ParseErr: " + e.getMessage() + ", " + e.getAnalysisBeforeFailure() + "\n";
			} else {
				return "LR1 parsing correct.\n";
			}
		}
		if (!expectValid) {
			return "LR1 parsing should not be successful: " + analysisLR1 + "\n";
		}

		LR1ParserReference parserReference = new LR1ParserReference(grammar);
		List<Rule> analysisReference = null;
		try {
			analysisReference = parserReference.parse(input);
		} catch (ParserException e) {
			return "LR1 ParseErr of Reference: " + e.getMessage() + ", " + e.getAnalysisBeforeFailure()
					+ "\nStudent implementation parsed: " + analysisLR1;
		}

		// Check analysis
		if (analysisLR1.size() != analysisReference.size()) {
			result += "Different LR1 analysis sizes.\n";
		}
		for (int i = 0; i < analysisLR1.size() && i < analysisReference.size(); ++i) {
			if (!analysisLR1.get(i).equals(analysisReference.get(i))) {
				return "Incorrect LR1 parsing on index " + i + ": " + analysisLR1.get(i) + "\n";
			}
		}

		if (result.isEmpty()) {
			return "LR1 parsing correct.\n";
		}
		return result;
	}

	private Result checkMapSetEquality(String name, MapSet<NonTerminal, Alphabet> custom,
			MapSet<NonTerminal, Alphabet> reference) {
		Result result = new Result();

		for (NonTerminal nonTerminal : reference.keySet()) {
			if (!custom.containsKey(nonTerminal)) {
				result.addError("Missing non-terminal in " + name + ": " + nonTerminal);
			}
			result.add(checkSetEquality(name, new HashSet<Alphabet>(custom.get(nonTerminal)),
					new HashSet<Alphabet>(reference.get(nonTerminal))));
		}

		for (NonTerminal nonTerminal : custom.keySet()) {
			if (!reference.containsKey(nonTerminal)) {
				result.addError("Wrong non-terminal in " + name + ": " + nonTerminal);
			}
		}

		return result;
	}

	private Result checkSetEquality(String name, Set<Alphabet> custom, Set<Alphabet> reference) {
		Result result = new Result();

		for (Iterator<Alphabet> iter = custom.iterator(); iter.hasNext();) {
			Alphabet element = iter.next();
			if (reference.remove(element)) {
				iter.remove();
			}
		}
		if (!custom.isEmpty()) {
			result.addError(custom.size() + " elements too many in " + name);
		}
		if (!reference.isEmpty()) {
			result.addError(reference.size() + " elements missing in " + name);
		}
		return result;
	}

}
