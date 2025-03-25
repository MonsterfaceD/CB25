package parser.grammar;

import java.util.Arrays;

import symbols.NonTerminal;
import symbols.Token;
import symbols.Tokens;

/**
 * Represents the grammar of the WHILE language.
 *
 * start       -> program EOF
 * program     -> statement program | statement
 * statement   -> declaration SEMICOLON | assignment SEMICOLON | branch | loop | out SEMICOLON
 * declaration -> INT ID
 * assignment  -> ID ASSIGN expr | ID ASSIGN READ LPAR RPAR
 * out         -> WRITE LPAR expr RPAR | WRITE LPAR STRING RPAR
 * branch      -> IF LPAR guard RPAR LBRACE program RBRACE |
 *                IF LPAR guard RPAR LBRACE program RBRACE ELSE LBRACE program RBRACE
 * loop        -> WHILE LPAR guard RPAR LBRACE program RBRACE
 * expr        -> expr PLUS exprmult | expr MINUS exprmult | exprmult
 * exprmult    -> exprmult TIMES subexpr | exprmult DIV subexpr | exprmult MOD subexpr |
 * subexpr     -> NUMBER | ID | LPAR expr RPAR
 * guard       -> guard AND subguard | guard OR subguard | subguard
 * subguard    -> relation | NOT LPAR guard RPAR | LPAR guard RPAR
 * relation    -> expr LT expr | expr LEQ expr | expr EQ expr | expr NEQ expr | expr GEQ expr | expr GT expr
 */
public class WhileGrammar extends AbstractGrammar {

	/**
	 * Create the default grammar representing the WHILE language.
	 * 
	 * @param tokens
	 *            Tokens.
	 * @throws Exception
	 *             Exception if tokens are unknown.
	 */
	public WhileGrammar(Tokens tokens) throws Exception {
		// Add non-terminals
		NonTerminal start = addNonTerminal("start");
		setStart(start);
		NonTerminal program = addNonTerminal("program");
		NonTerminal statement = addNonTerminal("statement");
		NonTerminal declaration = addNonTerminal("declaration");
		NonTerminal assignment = addNonTerminal("assignment");
		NonTerminal out = addNonTerminal("out");
		NonTerminal branch = addNonTerminal("branch");
		NonTerminal loop = addNonTerminal("loop");
		NonTerminal expr = addNonTerminal("expr");
		NonTerminal exprmult = addNonTerminal("exprmult");
		NonTerminal subexpr = addNonTerminal("subexpr");
		NonTerminal guard = addNonTerminal("guard");
		NonTerminal subguard = addNonTerminal("subguard");
		NonTerminal relation = addNonTerminal("relation");

		// Add grammar rules

		// start -> program $
		this.addRule(start, Arrays.asList(program, tokens.get("EOF")));

		// program -> statement program | statement
		this.addRule(program, Arrays.asList(statement, program));
		this.addRule(program, Arrays.asList(statement));

		// statement -> declaration SEMICOLON | assignment SEMICOLON | branch | loop | out
		// SEMICOLON
		this.addRule(statement, Arrays.asList(declaration, new Token("SEMICOLON")));
		this.addRule(statement, Arrays.asList(assignment, tokens.get("SEMICOLON")));
		this.addRule(statement, Arrays.asList(branch));
		this.addRule(statement, Arrays.asList(loop));
		this.addRule(statement, Arrays.asList(out, tokens.get("SEMICOLON")));

		// declaration -> INT ID
		this.addRule(declaration, Arrays.asList(tokens.get("INT"), tokens.get("ID")));

		// assignment -> ID ASSIGN expr | ID ASSIGN READ LPAR RPAR
		this.addRule(assignment, Arrays.asList(tokens.get("ID"), tokens.get("ASSIGN"), expr));
		this.addRule(assignment, Arrays.asList(tokens.get("ID"), tokens.get("ASSIGN"), tokens.get("READ"),
				tokens.get("LPAR"), tokens.get("RPAR")));

		// out -> WRITE LPAR expr RPAR | WRITE LPAR STRING RPAR
		this.addRule(out, Arrays.asList(tokens.get("WRITE"), tokens.get("LPAR"), expr, tokens.get("RPAR")));
		this.addRule(out,
				Arrays.asList(tokens.get("WRITE"), tokens.get("LPAR"), tokens.get("STRING"), tokens.get("RPAR")));

		// branch -> IF LPAR guard RPAR LBRACE program RBRACE |
		// IF LPAR guard RPAR LBRACE program RBRACE ELSE LBRACE program RBRACE
		this.addRule(branch, Arrays.asList(tokens.get("IF"), tokens.get("LPAR"), guard, tokens.get("RPAR"),
				tokens.get("LBRACE"), program, tokens.get("RBRACE")));
		this.addRule(branch,
				Arrays.asList(tokens.get("IF"), tokens.get("LPAR"), guard, tokens.get("RPAR"), tokens.get("LBRACE"),
						program, tokens.get("RBRACE"), tokens.get("ELSE"), tokens.get("LBRACE"), program,
						tokens.get("RBRACE")));

		// loop -> WHILE LPAR guard RPAR LBRACE program RBRACE
		this.addRule(loop, Arrays.asList(tokens.get("WHILE"), tokens.get("LPAR"), guard, tokens.get("RPAR"),
				tokens.get("LBRACE"), program, tokens.get("RBRACE")));

		// expr -> exprmult PLUS expr | exprmult MINUS expr | exprmult
		this.addRule(expr, Arrays.asList(expr, tokens.get("PLUS"), exprmult));
		this.addRule(expr, Arrays.asList(expr, tokens.get("MINUS"), exprmult));
		this.addRule(expr, Arrays.asList(exprmult));

		// exprmult -> subexpr TIMES expr | subexpr DIV expr | subexpr MOD expr
		this.addRule(exprmult, Arrays.asList(exprmult, tokens.get("TIMES"), subexpr));
		this.addRule(exprmult, Arrays.asList(exprmult, tokens.get("DIV"), subexpr));
		this.addRule(exprmult, Arrays.asList(exprmult, tokens.get("MOD"), subexpr));
		this.addRule(exprmult, Arrays.asList(subexpr));

		// subexpr -> NUMBER | ID | LPAR expr RPAR
		this.addRule(subexpr, Arrays.asList(tokens.get("NUMBER")));
		this.addRule(subexpr, Arrays.asList(tokens.get("ID")));
		this.addRule(subexpr, Arrays.asList(tokens.get("LPAR"), expr, tokens.get("RPAR")));

		// guard -> guard AND subguard | guard OR subguard | subguard
		this.addRule(guard, Arrays.asList(guard, tokens.get("AND"), subguard));
		this.addRule(guard, Arrays.asList(guard, tokens.get("OR"), subguard));
		this.addRule(guard, Arrays.asList(subguard));

		// subguard -> relation | NOT LPAR guard RPAR | LPAR guard RPAR
		this.addRule(subguard, Arrays.asList(relation));
		this.addRule(subguard, Arrays.asList(tokens.get("NOT"), tokens.get("LPAR"), guard, tokens.get("RPAR")));
		this.addRule(subguard, Arrays.asList(tokens.get("LPAR"), guard, tokens.get("RPAR")));

		// relation -> expr LT expr | expr LEQ expr | expr EQ expr | expr NEQ
		// expr | expr GEQ expr | expr GT expr
		this.addRule(relation, Arrays.asList(expr, tokens.get("LT"), expr));
		this.addRule(relation, Arrays.asList(expr, tokens.get("LEQ"), expr));
		this.addRule(relation, Arrays.asList(expr, tokens.get("EQ"), expr));
		this.addRule(relation, Arrays.asList(expr, tokens.get("NEQ"), expr));
		this.addRule(relation, Arrays.asList(expr, tokens.get("GEQ"), expr));
		this.addRule(relation, Arrays.asList(expr, tokens.get("GT"), expr));
	}

}
