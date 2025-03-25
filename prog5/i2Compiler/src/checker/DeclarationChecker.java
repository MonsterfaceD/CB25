package checker;

import java.util.List;

import parser.Rule;
import symbols.Symbol;
import symbols.Token;
import symbols.WhileAlphabet;

/**
 * Checks if identifiers are declared before using them.
 */
public class DeclarationChecker {

	// AST
	private AST ast;

	/**
	 * Constructor. Requires a right most analysis from the parser to initiate a
	 * new abstract syntax tree. This tree is then used for subsequent semantic
	 * checks.
	 * 
	 * @param symbols
	 *            List of symbols.
	 * @param analysis
	 *            Right most analysis of the parser.
	 */
	public DeclarationChecker(List<Symbol> symbols, List<Rule> analysis) {
		ast = new AST(symbols, analysis);
	}

	/**
	 * Get AST
	 * 
	 * @return AST
	 */
	public AST getAst() {
		return ast;
	}

	/**
	 * Check if every identifier which is used has been declared before.
	 * 
	 * @return True, if everything is correct, false if an identifier used
	 *         before its declaration.
	 */
	public boolean checkDeclaredBeforeUsed() {
		// TODO implement

		return false;
	}

}
