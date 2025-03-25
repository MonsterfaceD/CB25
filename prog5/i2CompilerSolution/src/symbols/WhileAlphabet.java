package symbols;

/**
 * Helper methods for checking type of non-terminals and tokens in the WHILE
 * language.
 */
public class WhileAlphabet {

	/**
	 * Check if given symbol is non-terminal start.
	 * 
	 * @param alphabet
	 *            Alphabet.
	 * @return True if correct symbol.
	 */
	public static boolean isStart(Alphabet alphabet) {
		if (!(alphabet instanceof NonTerminal)) {
			return false;
		} else {
			NonTerminal nonTerminal = (NonTerminal) alphabet;
			return nonTerminal.getName() == "start";
		}
	}

	/**
	 * Check if given symbol is non-terminal program.
	 * 
	 * @param alphabet
	 *            Alphabet.
	 * @return True if correct symbol.
	 */
	public static boolean isProgram(Alphabet alphabet) {
		if (!(alphabet instanceof NonTerminal)) {
			return false;
		} else {
			NonTerminal nonTerminal = (NonTerminal) alphabet;
			return nonTerminal.getName() == "program";
		}
	}

	/**
	 * Check if given symbol is non-terminal statement.
	 * 
	 * @param alphabet
	 *            Alphabet.
	 * @return True if correct symbol.
	 */
	public static boolean isStatement(Alphabet alphabet) {
		if (!(alphabet instanceof NonTerminal)) {
			return false;
		} else {
			NonTerminal nonTerminal = (NonTerminal) alphabet;
			return nonTerminal.getName() == "statement";
		}
	}

	/**
	 * Check if given symbol is non-terminal declaration.
	 * 
	 * @param alphabet
	 *            Alphabet.
	 * @return True if correct symbol.
	 */
	public static boolean isDeclaration(Alphabet alphabet) {
		if (!(alphabet instanceof NonTerminal)) {
			return false;
		} else {
			NonTerminal nonTerminal = (NonTerminal) alphabet;
			return nonTerminal.getName() == "declaration";
		}
	}

	/**
	 * Check if given symbol is non-terminal assignment.
	 * 
	 * @param alphabet
	 *            Alphabet.
	 * @return True if correct symbol.
	 */
	public static boolean isAssignment(Alphabet alphabet) {
		if (!(alphabet instanceof NonTerminal)) {
			return false;
		} else {
			NonTerminal nonTerminal = (NonTerminal) alphabet;
			return nonTerminal.getName() == "assignment";
		}
	}

	/**
	 * Check if given symbol is non-terminal out.
	 * 
	 * @param alphabet
	 *            Alphabet.
	 * @return True if correct symbol.
	 */
	public static boolean isOut(Alphabet alphabet) {
		if (!(alphabet instanceof NonTerminal)) {
			return false;
		} else {
			NonTerminal nonTerminal = (NonTerminal) alphabet;
			return nonTerminal.getName() == "out";
		}
	}

	/**
	 * Check if given symbol is non-terminal branch.
	 * 
	 * @param alphabet
	 *            Alphabet.
	 * @return True if correct symbol.
	 */
	public static boolean isBranch(Alphabet alphabet) {
		if (!(alphabet instanceof NonTerminal)) {
			return false;
		} else {
			NonTerminal nonTerminal = (NonTerminal) alphabet;
			return nonTerminal.getName() == "branch";
		}
	}

	/**
	 * Check if given symbol is non-terminal loop.
	 * 
	 * @param alphabet
	 *            Alphabet.
	 * @return True if correct symbol.
	 */
	public static boolean isLoop(Alphabet alphabet) {
		if (!(alphabet instanceof NonTerminal)) {
			return false;
		} else {
			NonTerminal nonTerminal = (NonTerminal) alphabet;
			return nonTerminal.getName() == "loop";
		}
	}

	/**
	 * Check if given symbol is non-terminal expr.
	 * 
	 * @param alphabet
	 *            Alphabet.
	 * @return True if correct symbol.
	 */
	public static boolean isExpr(Alphabet alphabet) {
		if (!(alphabet instanceof NonTerminal)) {
			return false;
		} else {
			NonTerminal nonTerminal = (NonTerminal) alphabet;
			return nonTerminal.getName() == "expr";
		}
	}
	
	/**
	 * Check if given symbol is non-terminal exprmult.
	 * 
	 * @param alphabet
	 *            Alphabet.
	 * @return True if correct symbol.
	 */
	public static boolean isExprmult(Alphabet alphabet) {
		if (!(alphabet instanceof NonTerminal)) {
			return false;
		} else {
			NonTerminal nonTerminal = (NonTerminal) alphabet;
			return nonTerminal.getName() == "exprmult";
		}
	}

	/**
	 * Check if given symbol is non-terminal subexpr.
	 * 
	 * @param alphabet
	 *            Alphabet.
	 * @return True if correct symbol.
	 */
	public static boolean isSubexpr(Alphabet alphabet) {
		if (!(alphabet instanceof NonTerminal)) {
			return false;
		} else {
			NonTerminal nonTerminal = (NonTerminal) alphabet;
			return nonTerminal.getName() == "subexpr";
		}
	}

	/**
	 * Check if given symbol is non-terminal guard.
	 * 
	 * @param alphabet
	 *            Alphabet.
	 * @return True if correct symbol.
	 */
	public static boolean isGuard(Alphabet alphabet) {
		if (!(alphabet instanceof NonTerminal)) {
			return false;
		} else {
			NonTerminal nonTerminal = (NonTerminal) alphabet;
			return nonTerminal.getName() == "guard";
		}
	}

	/**
	 * Check if given symbol is non-terminal subguard.
	 * 
	 * @param alphabet
	 *            Alphabet.
	 * @return True if correct symbol.
	 */
	public static boolean isSubguard(Alphabet alphabet) {
		if (!(alphabet instanceof NonTerminal)) {
			return false;
		} else {
			NonTerminal nonTerminal = (NonTerminal) alphabet;
			return nonTerminal.getName() == "subguard";
		}
	}

	/**
	 * Check if given symbol is non-terminal relation.
	 * 
	 * @param alphabet
	 *            Alphabet.
	 * @return True if correct symbol.
	 */
	public static boolean isRelation(Alphabet alphabet) {
		if (!(alphabet instanceof NonTerminal)) {
			return false;
		} else {
			NonTerminal nonTerminal = (NonTerminal) alphabet;
			return nonTerminal.getName() == "relation";
		}
	}

	/**
	 * Check if given symbol is token ID.
	 * 
	 * @param alphabet
	 *            Alphabet.
	 * @return True if correct symbol.
	 */
	public static boolean isId(Alphabet alphabet) {
		if (!(alphabet instanceof Token)) {
			return false;
		} else {
			Token token = (Token) alphabet;
			return token.getName() == "ID";
		}
	}

	/**
	 * Check if given symbol is token STRING.
	 * 
	 * @param alphabet
	 *            Alphabet.
	 * @return True if correct symbol.
	 */
	public static boolean isString(Alphabet alphabet) {
		if (!(alphabet instanceof Token)) {
			return false;
		} else {
			Token token = (Token) alphabet;
			return token.getName() == "STRING";
		}
	}

	/**
	 * Check if given symbol is token NUMBER.
	 * 
	 * @param alphabet
	 *            Alphabet.
	 * @return True if correct symbol.
	 */
	public static boolean isNumber(Alphabet alphabet) {
		if (!(alphabet instanceof Token)) {
			return false;
		} else {
			Token token = (Token) alphabet;
			return token.getName() == "NUMBER";
		}
	}

}
