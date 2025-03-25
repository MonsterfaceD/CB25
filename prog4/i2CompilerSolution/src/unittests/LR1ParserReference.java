package unittests;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

import parser.*;
import parser.grammar.AbstractGrammar;
import symbols.Symbol;
import symbols.Token;
import symbols.NonTerminal;
import symbols.Tokens;

/**
 * LR(0) parser.
 */
public class LR1ParserReference {

	// Generator for LR(0) sets
	private LR1SetGeneratorReference generatorLR1;

	// Start symbol of grammar
	private NonTerminal start;

	/**
	 * Constructor.
	 *
	 * @param grammar
	 *            Grammar.
	 */
	public LR1ParserReference(AbstractGrammar grammar) {
		this.generatorLR1 = new LR1SetGeneratorReference(grammar);
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
		Stack<LR1Set> stack = new Stack<LR1Set>();
		stack.push(generatorLR1.getInitialState());
		Token currentToken = null;

		while (!stack.isEmpty()) {
			LR1Set currentSet = stack.peek();
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
					analysis.add(currentSet.getCompleteItem(Tokens.EPSILON));
					return analysis;
				} else {
					// Parser finished but there is more input
					throw new ParserException("Parser finished but there is more unprocessed input!", analysis);
				}
			} else {
				// if stack top has a complete item => reduce
				if (currentSet.containsCompleteItem(currentToken)) {
					// Find matching item of the form [A -> alpha *]
					LR1Item completeItem = null;
					for (LR1Item item : currentSet) {
						if (item.canReduce(currentToken)) {
							// Found rule
							if (completeItem != null) {
								throw new ParserException("LR(1) conflict: reducing with rule " + completeItem
										+ " possible, but also reducing with " + item + " using the lookahead "+currentToken, analysis);
							}
							completeItem = item;
						}
					}
					if (completeItem != null) {
						// Check that no shift is possible
						LR1Set shiftState = generatorLR1.getSuccessor(currentSet, currentToken);
						if (shiftState != null) {
							throw new ParserException("LR(1) conflict: reducing with rule " + completeItem
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
						LR1Set succState = generatorLR1.getSuccessor(currentSet, completeItem.getLhs());
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
					LR1Set succState = generatorLR1.getSuccessor(currentSet, currentToken);
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
