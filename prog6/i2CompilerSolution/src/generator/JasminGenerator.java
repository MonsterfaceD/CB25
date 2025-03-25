package generator;

import java.util.HashMap;
import java.util.List;

import checker.AST;
import checker.ASTNode;
import symbols.WhileAlphabet;

/**
 * Generator which converts an abstract syntax tree into the Jasmin language.
 *
 */
public class JasminGenerator {

	// The symbol table mapping an identifier to its id
	private HashMap<String, Integer> symbolTable = new HashMap<String, Integer>();
	// Counters for nested operators
	private int ifCount = 0;
	private int loopCount = 0;
	private int negCount = 0;
	private int relCount = 0;

	/**
	 * Given an abstract syntax tree with respect to WhileGrammar, this method
	 * translates it to the Jasmin language which is a textual representation of
	 * Java-Bytecode.
	 * 
	 * @param name
	 *            Name of the program
	 * @param ast
	 *            The abstract syntax tree
	 * @return Jasmin program as a String
	 * @throws GeneratorException
	 *             Exception while generation the Jasmin code
	 */
	public String translateWHILE(String name, AST ast) throws GeneratorException {
		StringBuilder result = new StringBuilder();

		// Define a class with the given name which is a subclass of Object.
		appendString(result, ".class public " + name);
		appendString(result, ".super java/lang/Object");
		appendString(result, ";");
		// Define the standard constructor which calls super().
		appendString(result, "; standard initializer");
		appendString(result, ".method public <init>()V");
		appendString(result, "  aload_0");
		appendString(result, "  invokenonvirtual java/lang/Object/<init>()V");
		appendString(result, "  return");
		appendString(result, ".end method");
		appendString(result, "");
		// Then start building the main method.
		appendString(result, ".method public static main([Ljava/lang/String;)V");
		
		// Walk the abstract syntax tree in-order and translate it to Jasmin
		// code
		// At the same time the symbol table is generated
		ASTNode root = ast.getRoot();
		assert (WhileAlphabet.isStart(root.getAlphabet()));
		assert (root.getChildren().size() == 2);
		String programCode = translateProgram(root.getChildren().get(0));
		
		// Now that we know how many variables we need, output the header first...
		appendString(result, "  ; set limits used by this method");
		appendString(result, "  .limit locals " + (symbolTable.size() + 1));
		appendString(result, "  .limit stack " + (symbolTable.size() + 1));

		// ... and the code itself
		result.append(programCode);
		
		// here the main method ends
		appendString(result, "; done");
		appendString(result, "return");
		appendString(result, "");
		appendString(result, ".end method");

		return result.toString();
	}

	/**
	 * Generate Jasmin code for a program.
	 * 
	 * @param node
	 *            The node in the AST.
	 * @return Jasmin code as string.
	 * @throws GeneratorException
	 *             Exception while generating.
	 */
	private String translateProgram(ASTNode node) throws GeneratorException {
		assert (WhileAlphabet.isProgram(node.getAlphabet()));
		StringBuilder result = new StringBuilder();

		switch (node.getChildren().size()) {
		case 1:
			// program -> statement
			result.append(translateStatement(node.getChildren().get(0)));
			break;
		case 2:
			// program -> statement program
			result.append(translateStatement(node.getChildren().get(0)));
			result.append(translateProgram(node.getChildren().get(1)));
			break;
		default:
			throw new GeneratorException("Grammar is inconsistent for the program rule: " + node.getAlphabet()
					+ " with children " + node.getChildren());
		}

		return result.toString();
	}

	/**
	 * Generate Jasmin code for a statement.
	 * 
	 * @param node
	 *            The node in the AST.
	 * @return Jasmin code as string.
	 * @throws GeneratorException
	 *             Exception while generating.
	 */
	private String translateStatement(ASTNode node) throws GeneratorException {
		assert (WhileAlphabet.isStatement(node.getAlphabet()));
		StringBuilder result = new StringBuilder();

		assert (node.getChildren().size() >= 1);
		ASTNode child = node.getChildren().get(0);

		if (WhileAlphabet.isDeclaration(child.getAlphabet())) {
			// statement -> declaration SEM
			result.append(translateDecl(child));
		} else if (WhileAlphabet.isAssignment(child.getAlphabet())) {
			// statement -> assignment SEM
			result.append(translateAssign(child));
		} else if (WhileAlphabet.isBranch(child.getAlphabet())) {
			// statement -> branch
			result.append(translateBranch(child));
		} else if (WhileAlphabet.isLoop(child.getAlphabet())) {
			// statement -> loop
			result.append(translateLoop(child));
		} else if (WhileAlphabet.isOut(child.getAlphabet())) {
			// statement -> out SEM
			result.append(translateWrite(child));
		} else {
			throw new GeneratorException("Grammar is inconsistent for the statement rule.");
		}

		return result.toString();
	}

	/**
	 * Generate Jasmin code for a branch.
	 * 
	 * @param node
	 *            The node in the AST.
	 * @return Jasmin code as string.
	 * @throws GeneratorException
	 *             Exception while generating.
	 */
	private String translateBranch(ASTNode node) throws GeneratorException {
		assert (WhileAlphabet.isBranch(node.getAlphabet()));
		StringBuilder result = new StringBuilder();

		ifCount++;
		int localIFnumber = ifCount; // the counter may be increased in nested
										// if-statements so we keep a local copy
										// of the current value

		result.append(translateGuard(node.getChildren().get(2)));

		switch (node.getChildren().size()) {
		case 7:
			// branch -> IF LBRAC guard RBRAC LCBRAC prog RCBRAC (7 children)
			// if guard is false jump to the end of the if structure ...
			appendString(result, "ifeq ENDIF" + localIFnumber);
			// ... otherwise execute the body
			result.append(translateProgram(node.getChildren().get(5)));
			break;
		case 11:
			// branch -> IF LBRAC guard RBRAC LCBRAC prog RCBRAC ELSE LCBRAC
			// prog RCBRAC (11 children)
			// if guard is false jump to else branch ...
			appendString(result, "ifeq ELSE" + localIFnumber);
			// ... otherwise it is true, execute the if branch ...
			result.append(translateProgram(node.getChildren().get(5)));
			// ... and jump to the end of the if structure
			appendString(result, "goto ENDIF" + localIFnumber);
			// Here is the else branch
			appendString(result, "ELSE" + localIFnumber + ":");
			result.append(translateProgram(node.getChildren().get(9)));
			break;
		default:
			throw new GeneratorException("Grammar is inconsistent for the branch rule.");
		}

		appendString(result, "ENDIF" + localIFnumber + ":");

		return result.toString();
	}

	/**
	 * Generate Jasmin code for a loop.
	 * 
	 * @param node
	 *            The node in the AST.
	 * @return Jasmin code as string.
	 * @throws GeneratorException
	 *             Exception while generating.
	 */
	private String translateLoop(ASTNode node) throws GeneratorException {
		assert (WhileAlphabet.isLoop(node.getAlphabet()));
		StringBuilder result = new StringBuilder();

		loopCount++;
		int localLOOPnumber = loopCount; // the counter may be increased in
											// nested loops so we keep a local
											// copy of the current value

		// loop -> WHILE LBRAC guard RBRAC LCBRAC prog RCBRAC
		// create loop header label
		appendString(result, "LOOP" + localLOOPnumber + ":");
		// if guard fails, jump to the end of the loop ...
		result.append(translateGuard(node.getChildren().get(2)));
		appendString(result, "ifeq ENDLOOP" + localLOOPnumber);
		// ...otherwise execute the body ...
		result.append(translateProgram(node.getChildren().get(5)));
		// ...and jump back to the loop head
		appendString(result, "goto LOOP" + localLOOPnumber);
		// create loop end label
		appendString(result, "ENDLOOP" + localLOOPnumber + ":");

		return result.toString();
	}

	//
	/**
	 * Generate Jasmin code for a guard. Guards are translated such that after
	 * execution of the resulting Jasmin code a positive value is on top of the
	 * operand stack if guard was true and 0 is on top if guard was false. This
	 * way disjunction is coded as addition, conjunction as multiplication, and
	 * negation as (not b) := b?0:1
	 * 
	 * @param node
	 *            The node in the AST.
	 * @return Jasmin code as string.
	 * @throws GeneratorException
	 *             Exception while generating.
	 */
	private String translateGuard(ASTNode node) throws GeneratorException {
		assert (WhileAlphabet.isGuard(node.getAlphabet()));
		StringBuilder result = new StringBuilder();

		List<ASTNode> children = node.getChildren();
		if(children.size() == 1 && WhileAlphabet.isSubguard(children.get(0).getAlphabet())){
			// guard -> subguard
			result.append(translateSubguard(children.get(0)));
		}else if(children.size() == 3
			&& WhileAlphabet.isGuard(children.get(0).getAlphabet())
			&& WhileAlphabet.isSubguard(children.get(2).getAlphabet())){
				// guard -> subguard AND guard | subguard OR guard
				result.append(translateGuard(children.get(0)));
				result.append(translateSubguard(children.get(2)));

			if(WhileAlphabet.isAND(children.get(1).getAlphabet())){
				appendString(result, "imul");
			} else if(WhileAlphabet.isOR(children.get(1).getAlphabet())){
				appendString(result, "iadd");
			}else {
				throw new GeneratorException("Grammar is inconsistent for the guard rule.");
			}
		} else {
			throw new GeneratorException("Grammar is inconsistent for the guard rule.");
		}

		return result.toString();
	}

	/**
	 * Generate Jasmin code for a subguard.
	 * 
	 * @param node
	 *            The node in the AST.
	 * @return Jasmin code as string.
	 * @throws GeneratorException
	 *             Exception while generating.
	 */
	private String translateSubguard(ASTNode node) throws GeneratorException {
		assert(WhileAlphabet.isSubguard(node.getAlphabet()));
		StringBuilder result = new StringBuilder();

		List<ASTNode> children = node.getChildren();
		if(children.size() == 1 && WhileAlphabet.isRelation(children.get(0).getAlphabet())){
			// subguard -> relation
			result.append(translateRelation(children.get(0)));
		}else if(children.size() == 4 && WhileAlphabet.isNOT(children.get(0).getAlphabet())){
			// subguard -> NOT LPAP guard RPAR
			result.append(translateGuard(children.get(2)));
			negCount++;// no copy needed as there are no nested translateGuard
			// calls in between
			appendString(result, "ifle INVERT" + negCount); // if it is zero
			// (false) jump to
			// ldc 1
			appendString(result, "ldc 0"); // we reach this if the value was
			// above 0
			appendString(result, "goto ENDINVERT" + negCount);
			appendString(result, "INVERT" + negCount + ":");
			appendString(result, "ldc 1");
			appendString(result, "ENDINVERT" + negCount + ":");
		} else if(children.size() == 3 && WhileAlphabet.isLPAR(children.get(0).getAlphabet())){
			// subguard -> LPAR guard RPAR
			result.append(translateGuard(children.get(1)));
		} else {
			throw new GeneratorException("Grammar is inconsistent for the subguard rule.");
		}

		return result.toString();

	}

	/**
	 * Generate Jasmin code for a relation.
	 * 
	 * @param node
	 *            The node in the AST.
	 * @return Jasmin code as string.
	 * @throws GeneratorException
	 *             Exception while generating.
	 */
	private String translateRelation(ASTNode node) throws GeneratorException {
		assert (WhileAlphabet.isRelation(node.getAlphabet()));
		StringBuilder result = new StringBuilder();

		List<ASTNode> children = node.getChildren();
		assert (children.size() == 3);
		result.append(translateExpr(children.get(0)));
		result.append(translateExpr(children.get(2)));

		relCount++;
		ASTNode operand = node.getChildren().get(1);
		if (WhileAlphabet.isLT(operand.getAlphabet())) {
			// relation -> expr LT expr
			// jump to label if the integer2 greater than integer1
			appendString(result, "if_icmplt REL" + relCount);
		} else if (WhileAlphabet.isLEQ(operand.getAlphabet())) {
			// relation -> expr LEQ expr
			appendString(result, "if_icmple REL" + relCount);
		} else if (WhileAlphabet.isEQ(operand.getAlphabet())) {
			// relation -> expr EQ expr
			appendString(result, "if_icmpeq REL" + relCount);
		} else if (WhileAlphabet.isNEQ(operand.getAlphabet())) {
			// relation -> expr NEQ expr
			appendString(result, "if_icmpne REL" + relCount);
		} else if (WhileAlphabet.isGEQ(operand.getAlphabet())) {
			// relation -> expr GEQ expr
			appendString(result, "if_icmpge REL" + relCount);
		} else if (WhileAlphabet.isGT(operand.getAlphabet())) {
			// relation -> expr GT expr
			appendString(result, "if_icmpgt REL" + relCount);
		} else {
			throw new GeneratorException("Grammar is inconsistent for the relation rule.");
		}

		appendString(result, "ldc 0");
		appendString(result, "goto RELEND" + relCount);
		appendString(result, "REL" + relCount + ":");
		appendString(result, "ldc 1");
		appendString(result, "RELEND" + relCount + ":");

		return result.toString();
	}

	/**
	 * Generate Jasmin code for an assignment.
	 * 
	 * @param node
	 *            The node in the AST.
	 * @return Jasmin code as string.
	 * @throws GeneratorException
	 *             Exception while generating.
	 */
	private String translateAssign(ASTNode node) throws GeneratorException {
		assert (WhileAlphabet.isAssignment(node.getAlphabet()));
		StringBuilder result = new StringBuilder();

		List<ASTNode> children = node.getChildren();
		String id = children.get(0).getAttribute();

		switch (children.size()) {
		case 3:
			// assignment -> ID ASSIGN expr
			result.append(translateExpr(children.get(2)));
			break;
		case 5:
			// assignment -> ID ASSIGN READ LBRAC RBRAC
			result.append(translateReadInt());
			break;
		default:
			throw new GeneratorException("Grammar is inconsistent for the statement rule.");
		}

		appendString(result, "istore " + symbolTable.get(id));

		return result.toString();
	}

	//
	/**
	 * Generate Jasmin code for a declaration. We do not distinguish scopes and
	 * treat all variables as global to keep it simple. this method actually
	 * returns an empty string,
	 * 
	 * @param node
	 *            The node in the AST.
	 * @return Empty string, since the new variable is just added to the symbol
	 *         table.
	 * @throws GeneratorException
	 *             Exception while generating.
	 */
	private String translateDecl(ASTNode node) {
		assert (WhileAlphabet.isDeclaration(node.getAlphabet()));
		StringBuilder result = new StringBuilder();

		int currentSymbolCount = symbolTable.size();

		// declaration -> INT ID
		assert (node.getChildren().size() == 2);

		String id = node.getChildren().get(1).getAttribute();
		symbolTable.put(id, currentSymbolCount + 1);
		
		// Zero-initialize variable
		appendString(result, "bipush 0");
		appendString(result, "istore " + (currentSymbolCount + 1));

		return result.toString();
	}

	/**
	 * Generate Jasmin code for an expression.
	 * 
	 * @param node
	 *            The node in the AST.
	 * @return Jasmin code as string.
	 * @throws GeneratorException
	 *             Exception while generating.
	 */
	private String translateExpr(ASTNode node) throws GeneratorException {
		assert (WhileAlphabet.isExpr(node.getAlphabet()));
		StringBuilder result = new StringBuilder();

		List<ASTNode> children = node.getChildren();
		assert (children.size() >= 1);

		if(children.size() == 1 && WhileAlphabet.isExprmult(children.get(0).getAlphabet())){
			// expr -> exprmult
			result.append(translateExprmult(children.get(0)));
		}else if(children.size() == 3
				&& WhileAlphabet.isExpr(children.get(0).getAlphabet())
				&& WhileAlphabet.isExprmult(children.get(2).getAlphabet())){
			result.append(translateExpr(children.get(0)));
			result.append(translateExprmult(children.get(2)));
			ASTNode child = children.get(1);
			if (WhileAlphabet.isPLUS(child.getAlphabet())) {
				// expr -> expr PLUS exprmult
				appendString(result, "iadd");
			} else if (WhileAlphabet.isMINUS(child.getAlphabet())) {
				// expr -> expr MINUS exprmult
				appendString(result, "isub");
			} else {
				throw new GeneratorException("Grammar is inconsistent for the expression rule.");
			}
		} else {
			throw new GeneratorException("Grammar is inconsistent for the expression rule.");
		}

		return result.toString();
	}

	/**
	 * Generate Jasmin code for an multiplicative expression.
	 *
	 * @param node
	 *            The node in the AST.
	 * @return Jasmin code as string.
	 * @throws GeneratorException
	 *             Exception while generating.
	 */
	private String translateExprmult(ASTNode node) throws GeneratorException {
		assert (WhileAlphabet.isExprmult(node.getAlphabet()));
		StringBuilder result = new StringBuilder();

		List<ASTNode> children = node.getChildren();
		assert (children.size() >= 1);

		if(children.size() == 1 && WhileAlphabet.isSubexpr(children.get(0).getAlphabet())){
			// exprmult -> subexpr
			result.append(translateSubexpr(children.get(0)));
		}else if(children.size() == 3
				&& WhileAlphabet.isExprmult(children.get(0).getAlphabet())
				&& WhileAlphabet.isSubexpr(children.get(2).getAlphabet())){
			result.append(translateExprmult(children.get(0)));
			result.append(translateSubexpr(children.get(2)));
			ASTNode child = children.get(1);
			if (WhileAlphabet.isTIMES(child.getAlphabet())) {
				// subexpr -> expr TIMES expr
				appendString(result, "imul");
			} else if (WhileAlphabet.isDIV(child.getAlphabet())) {
				// subexpr -> expr DIV expr
				appendString(result, "idiv");
			} else if (WhileAlphabet.isMOD(child.getAlphabet())) {
				// subexpr -> expr MOD expr
				appendString(result, "irem");
			} else {
				throw new GeneratorException("Grammar is inconsistent for the expression rule.");
			}
		} else {
			throw new GeneratorException("Grammar is inconsistent for the expression rule.");
		}

		return result.toString();
	}

	/**
	 * Generate Jasmin code for a subexpression.
	 * 
	 * @param node
	 *            The node in the AST.
	 * @return Jasmin code as string.
	 * @throws GeneratorException
	 *             Exception while generating.
	 */
	private String translateSubexpr(ASTNode node) throws GeneratorException {
		assert (WhileAlphabet.isSubexpr(node.getAlphabet()));
		StringBuilder result = new StringBuilder();

		List<ASTNode> children = node.getChildren();

		ASTNode child = node.getChildren().get(0);
		if (children.size() == 1 && WhileAlphabet.isNumber(child.getAlphabet())) {
			// subexpr -> NUM
			String number = child.getAttribute();
			appendString(result, "ldc " + number); // Push a single word
													// constant.
		} else if (children.size() == 1 && WhileAlphabet.isId(child.getAlphabet())) {
			// subexpr -> ID
			String id = child.getAttribute();
			appendString(result, "iload " + symbolTable.get(id));
		} else if (children.size() == 3 && WhileAlphabet.isLPAR(child.getAlphabet())){
			// subexpr -> LPAR expr RPAR
			String expr = translateExpr(children.get(1));
			result.append(expr);
		} else {
			throw new GeneratorException("Grammar is inconsistent for the sub-expression rule.");
		}

		return result.toString();
	}

	/**
	 * Generate Jasmin code for reading an int from the console.
	 * 
	 * @return Jasmin code as string.
	 */
	private String translateReadInt() {
		StringBuilder result = new StringBuilder();
		appendString(result, "; int n = Integer.parseInt(System.console().readLine());");
		appendString(result, "; Console c = System.console();");
		appendString(result, "invokestatic java/lang/System/console()Ljava/io/Console;");
		appendString(result, "; Reads one line and stores in a String");
		appendString(result, "invokevirtual java/io/Console/readLine()Ljava/lang/String;");
		appendString(result, "; Parse String to int, do not handle exceptions");
		appendString(result, "invokestatic java/lang/Integer/parseInt(Ljava/lang/String;)I");
		return result.toString();
	}

	/**
	 * Generate Jasmin code for writing a string on the console.
	 * 
	 * @param node
	 *            The node in the AST
	 * @return Jasmin code as string.
	 * @throws GeneratorException
	 *             Exception while generating.
	 */
	private String translateWrite(ASTNode node) throws GeneratorException {
		assert (WhileAlphabet.isOut(node.getAlphabet()));
		StringBuilder result = new StringBuilder();

		assert (node.getChildren().size() == 4);
		ASTNode child = node.getChildren().get(2);
		if (WhileAlphabet.isString(child.getAlphabet())) {
			// out -> WRITE LBRAC STRING RBRAC
			// push PrintStream object
			appendString(result, "getstatic java/lang/System/out Ljava/io/PrintStream;");
			// push String
			// the extra quotes are already part of the string
			appendString(result, "ldc " + child.getAttribute());
			// print to command line
			appendString(result, "invokevirtual java/io/PrintStream/println(Ljava/lang/String;)V");
		} else {
			// out -> WRITE LBRAC expr RBRAC
			assert (WhileAlphabet.isExpr(child.getAlphabet()));
			// evaluate expression

			result.append(translateExpr(child));

			// the result is now on the top of the operand stack
			// cast it to string and write to console
			appendString(result, "invokestatic java/lang/String/valueOf(I)Ljava/lang/String;");
			appendString(result, "; begin syso");
			appendString(result, "astore 0 	; store string object in register 0");
			appendString(result, "getstatic java/lang/System/out Ljava/io/PrintStream;");
			appendString(result, "aload 0   ; load the string");
			appendString(result, "invokevirtual java/io/PrintStream/println(Ljava/lang/String;)V");
			appendString(result, "; end syso");
		}

		return result.toString();
	}

	/**
	 * Append string with newline at the end.
	 * 
	 * @param builder
	 *            Stringbuilder
	 * @param s
	 *            String to append
	 */
	private void appendString(StringBuilder builder, String s) {
		builder.append(s + System.lineSeparator());
	}
}
