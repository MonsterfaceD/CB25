package symbols;

import java.io.IOException;
import java.util.ArrayList;

import util.FileUtil;
import util.Pair;

/**
 * All recognized tokens. We use a list here as the ordering is important for
 * the FLM analysis.
 */
public class Tokens extends ArrayList<Pair<Token, String>> {

	private static final long serialVersionUID = 1L;

	// Special tokens
	public static Token BLANK;
	public static Token COMMENT;
	public static Token EPSILON;

	/**
	 * Constructor.
	 */
	public Tokens() {
		// Special tokens which are always needed
		BLANK = new Token("BLANK");
		this.add(BLANK, "");
		COMMENT = new Token("COMMENT");
		this.add(COMMENT, "");
		EPSILON = new Token("EPSILON");
		this.add(EPSILON, "");
		this.add(new Token("EOF"), "$");
	}

	public void add(Token token, String s) {
		add(new Pair<Token, String>(token, s));
	}

	/**
	 * Get token corresponding to the given name.
	 * 
	 * @param name
	 *            Name.
	 * @return Token
	 * @throws SymbolException
	 *             Exception if token is unknown
	 */
	public Token get(String name) throws SymbolException {
		for (Pair<Token, String> entry : this) {
			Token token = entry.getFirst();
			if (token.getName().equals(name)) {
				return token;
			}
		}
		// Token not found
		throw new SymbolException("Token '" + name + "' unknown");
	}

	/**
	 * Set default tokens.
	 */
	public void setDefault() {
		// Tokens corresponding to a word
		this.add(new Token("WHILE"), "while");
		this.add(new Token("WRITE"), "write");
		this.add(new Token("READ"), "read");
		this.add(new Token("INT"), "int");
		this.add(new Token("IF"), "if");
		this.add(new Token("ELSE"), "else");
		this.add(new Token("TRUE"), "true");
		this.add(new Token("FALSE"), "false");
		this.add(new Token("LPAR"), "(");
		this.add(new Token("RPAR"), ")");
		this.add(new Token("LBRACE"), "{");
		this.add(new Token("RBRACE"), "}");
		this.add(new Token("PLUS"), "+");
		this.add(new Token("MINUS"), "-");
		this.add(new Token("TIMES"), "*");
		this.add(new Token("DIV"), "/");
		this.add(new Token("MOD"), "%");
		this.add(new Token("LEQ"), "<=");
		this.add(new Token("LT"), "<");
		this.add(new Token("GEQ"), ">=");
		this.add(new Token("GT"), ">");
		this.add(new Token("EQ"), "==");
		this.add(new Token("NEQ"), "!=");
		this.add(new Token("ASSIGN"), "=");
		this.add(new Token("AND"), "&&");
		this.add(new Token("OR"), "||");
		this.add(new Token("NOT"), "!");
		this.add(new Token("INC"), "++");
		this.add(new Token("DEC"), "--");
		this.add(new Token("SEMICOLON"), ";");

		// Special tokens
		this.add(new Token("ID"), "");
		this.add(new Token("STRING"), "");
		this.add(new Token("NUMBER"), "");
	}

	/**
	 * Add tokens defined in a given file.
	 * 
	 * @param tokenFile
	 *            File containing the token definitions.
	 * @throws SymbolException
	 *             Exception when the token definition is not correct.
	 * @throws IOException
	 *             Exception when file could not be read.
	 */
	public void setFromFile(String tokenFile) throws SymbolException, IOException {
		ArrayList<String> lines = FileUtil.fileToStringLines(tokenFile);
		for (String line : lines) {
			String[] parts = line.split("\\s+");
			if (parts.length != 2) {
				throw new SymbolException("Expected declaration of form 'token word' in line: '" + line + "'");
			}
			this.add(new Token(parts[0]), parts[1]);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.AbstractMap#toString()
	 */
	public String toString() {
		String str = "[";
		boolean first = true;
		for (Pair<Token, String> entry : this) {
			if (first) {
				first = false;
			} else {
				str += ", ";
			}
			str += "(" + entry.getFirst() + ", " + entry.getSecond() + ")";
		}
		str += "]";
		return str;
	}
}