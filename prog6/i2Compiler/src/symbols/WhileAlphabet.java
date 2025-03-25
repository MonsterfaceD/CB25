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

	/**
	 * Check if given symbol is token AND.
	 * 
	 * @param alphabet
	 *            Alphabet.
	 * @return True if correct symbol.
	 */
	public static boolean isAND(Alphabet alphabet) {
		if (!(alphabet instanceof Token)) {
			return false;
		} else {
			Token token = (Token) alphabet;
			return token.getName() == "AND";
		}
	}

	/**
	 * Check if given symbol is token OR.
	 * 
	 * @param alphabet
	 *            Alphabet.
	 * @return True if correct symbol.
	 */
	public static boolean isOR(Alphabet alphabet) {
		if (!(alphabet instanceof Token)) {
			return false;
		} else {
			Token token = (Token) alphabet;
			return token.getName() == "OR";
		}
	}

	/**
	 * Check if given symbol is token NOT.
	 * 
	 * @param alphabet
	 *            Alphabet.
	 * @return True if correct symbol.
	 */
	public static boolean isNOT(Alphabet alphabet) {
		if (!(alphabet instanceof Token)) {
			return false;
		} else {
			Token token = (Token) alphabet;
			return token.getName() == "NOT";
		}
	}

	/**
	 * Check if given symbol is token LPAR.
	 *
	 * @param alphabet
	 *            Alphabet.
	 * @return True if correct symbol.
	 */
	public static boolean isLPAR(Alphabet alphabet) {
		if (!(alphabet instanceof Token)) {
			return false;
		} else {
			Token token = (Token) alphabet;
			return token.getName() == "LPAR";
		}
	}

	/**
	 * Check if given symbol is token RPAR.
	 *
	 * @param alphabet
	 *            Alphabet.
	 * @return True if correct symbol.
	 */
	public static boolean isRPAR(Alphabet alphabet) {
		if (!(alphabet instanceof Token)) {
			return false;
		} else {
			Token token = (Token) alphabet;
			return token.getName() == "RPAR";
		}
	}

	/**
	 * Check if given symbol is token LT.
	 * 
	 * @param alphabet
	 *            Alphabet.
	 * @return True if correct symbol.
	 */
	public static boolean isLT(Alphabet alphabet) {
		if (!(alphabet instanceof Token)) {
			return false;
		} else {
			Token token = (Token) alphabet;
			return token.getName() == "LT";
		}
	}

	/**
	 * Check if given symbol is token LEQ.
	 * 
	 * @param alphabet
	 *            Alphabet.
	 * @return True if correct symbol.
	 */
	public static boolean isLEQ(Alphabet alphabet) {
		if (!(alphabet instanceof Token)) {
			return false;
		} else {
			Token token = (Token) alphabet;
			return token.getName() == "LEQ";
		}
	}

	/**
	 * Check if given symbol is token GT.
	 * 
	 * @param alphabet
	 *            Alphabet.
	 * @return True if correct symbol.
	 */
	public static boolean isGT(Alphabet alphabet) {
		if (!(alphabet instanceof Token)) {
			return false;
		} else {
			Token token = (Token) alphabet;
			return token.getName() == "GT";
		}
	}

	/**
	 * Check if given symbol is token GEQ.
	 * 
	 * @param alphabet
	 *            Alphabet.
	 * @return True if correct symbol.
	 */
	public static boolean isGEQ(Alphabet alphabet) {
		if (!(alphabet instanceof Token)) {
			return false;
		} else {
			Token token = (Token) alphabet;
			return token.getName() == "GEQ";
		}
	}

	/**
	 * Check if given symbol is token EQ.
	 * 
	 * @param alphabet
	 *            Alphabet.
	 * @return True if correct symbol.
	 */
	public static boolean isEQ(Alphabet alphabet) {
		if (!(alphabet instanceof Token)) {
			return false;
		} else {
			Token token = (Token) alphabet;
			return token.getName() == "EQ";
		}
	}

	/**
	 * Check if given symbol is token NEQ.
	 * 
	 * @param alphabet
	 *            Alphabet.
	 * @return True if correct symbol.
	 */
	public static boolean isNEQ(Alphabet alphabet) {
		if (!(alphabet instanceof Token)) {
			return false;
		} else {
			Token token = (Token) alphabet;
			return token.getName() == "NEQ";
		}
	}

	/**
	 * Check if given symbol is token PLUS.
	 * 
	 * @param alphabet
	 *            Alphabet.
	 * @return True if correct symbol.
	 */
	public static boolean isPLUS(Alphabet alphabet) {
		if (!(alphabet instanceof Token)) {
			return false;
		} else {
			Token token = (Token) alphabet;
			return token.getName() == "PLUS";
		}
	}

	/**
	 * Check if given symbol is token MINUS.
	 * 
	 * @param alphabet
	 *            Alphabet.
	 * @return True if correct symbol.
	 */
	public static boolean isMINUS(Alphabet alphabet) {
		if (!(alphabet instanceof Token)) {
			return false;
		} else {
			Token token = (Token) alphabet;
			return token.getName() == "MINUS";
		}
	}

	/**
	 * Check if given symbol is token TIMES.
	 * 
	 * @param alphabet
	 *            Alphabet.
	 * @return True if correct symbol.
	 */
	public static boolean isTIMES(Alphabet alphabet) {
		if (!(alphabet instanceof Token)) {
			return false;
		} else {
			Token token = (Token) alphabet;
			return token.getName() == "TIMES";
		}
	}

	/**
	 * Check if given symbol is token DIV.
	 * 
	 * @param alphabet
	 *            Alphabet.
	 * @return True if correct symbol.
	 */
	public static boolean isDIV(Alphabet alphabet) {
		if (!(alphabet instanceof Token)) {
			return false;
		} else {
			Token token = (Token) alphabet;
			return token.getName() == "DIV";
		}
	}

	/**
	 * Check if given symbol is token MOD.
	 * 
	 * @param alphabet
	 *            Alphabet.
	 * @return True if correct symbol.
	 */
	public static boolean isMOD(Alphabet alphabet) {
		if (!(alphabet instanceof Token)) {
			return false;
		} else {
			Token token = (Token) alphabet;
			return token.getName() == "MOD";
		}
	}

}
