package unittests;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

import parser.LR0Item;
import parser.LR0Set;
import parser.ParserException;
import parser.Rule;
import parser.grammar.AbstractGrammar;
import symbols.Symbol;
import symbols.Token;
import symbols.NonTerminal;

/**
 * LR(0) parser.
 */
public class LR0ParserReference {

	// Generator for LR(0) sets
	private LR0SetGeneratorReference generatorLR0;

	// Start symbol of grammar
	private NonTerminal start;

	/**
	 * Constructor.
	 * 
	 * @param grammar
	 *            Grammar.
	 */
	public LR0ParserReference(AbstractGrammar grammar) {
		this.generatorLR0 = new LR0SetGeneratorReference(grammar);
		this.start = grammar.getStart();
	}

	/**
	 * Parse the input via LR(0) parsing.
	 * 
	 * @param lexOutput
	 *            List of symbols
	 * @return A list of rules which corresponds to the right-most analysis
	 * @throws ParserException
	 *             Parser exception
	 */
	public List<Rule> parse(List<Symbol> lexOutput) throws ParserException {
		List<Rule> analysis = new LinkedList<Rule>();

		Iterator<Symbol> it = lexOutput.iterator();
		Stack<LR0Set> stack = new Stack<LR0Set>();
		stack.push(generatorLR0.getInitialState());
		Token currentToken = null;

		while (!stack.isEmpty()) {
			LR0Set currentSet = stack.peek();
			// check if current token has been consumed by a previous iteration
			// and if so read one more token
			if (currentToken == null && it.hasNext()) {
				// read one more token if there are any
				currentToken = it.next().getToken();
			}

			// Finish parsing if the stack top contains [start -> alpha *]
			if (currentSet.containsFinalItem(start)) {
				if (currentToken == null) {
					// input completely read
					analysis.add(currentSet.getCompleteItem());
					return analysis;
				} else {
					// Parser finished but there is more input
					throw new ParserException("Parser finished but there is more unprocessed input!", analysis);
				}
			} else {
				// if stack top has a complete item => reduce
				if (currentSet.containsCompleteItem()) {
					// Find matching item of the form [A -> alpha *]
					LR0Item completeItem = null;
					for (LR0Item item : currentSet) {
						if (!item.canShift()) {
							// Found rule
							if (completeItem != null) {
								throw new ParserException("LR(0) conflict: reducing with rule " + completeItem
										+ " possible, but also reducing with " + item, analysis);
							}
							completeItem = item;
						}
					}
					if (completeItem != null) {
						// Check that no shift is possible
						LR0Set shiftState = generatorLR0.getSuccessor(currentSet, currentToken);
						if (shiftState != null) {
							throw new ParserException("LR(0) conflict: reducing with rule " + completeItem
									+ " possible, but also shifting with " + currentToken + " to set " + shiftState,
									analysis);
						}

						// Perform reduce step
						// remove |alpha| elements from the stack
						for (int i = 0; i < completeItem.getRhs().length; i++) {
							stack.pop();
						}
						// I' := Top(stack)
						currentSet = stack.peek();
						// J := delta(I', A)
						LR0Set succState = generatorLR0.getSuccessor(currentSet, completeItem.getLhs());
						if (succState == null) {
							throw new ParserException("Tried reducing with rule " + completeItem
									+ " but could not find a successor delta(" + currentSet + ", "
									+ completeItem.getLhs() + ")", analysis);
						}
						stack.push(succState);
						// append the applied rule (ensures correct order)
						analysis.add(completeItem);
						continue;
					}
				}

				// Reduce was not possible => try to shift
				if (currentToken == null) {
					// nothing more to read, nothing to reduce and no final item
					throw new ParserException("Only shift operation possible but the input terminated", analysis);
				} else {
					LR0Set succState = generatorLR0.getSuccessor(currentSet, currentToken);
					if (succState == null) {
						throw new ParserException("Tried shifting " + currentToken
								+ " onto the stack but could not find a successor delta(" + currentSet + ", "
								+ currentToken + ")", analysis);
					} else {
						stack.push(succState);
						// mark current token as consumed
						currentToken = null;
					}
				}
			}
		}

		return analysis;
	}
}
