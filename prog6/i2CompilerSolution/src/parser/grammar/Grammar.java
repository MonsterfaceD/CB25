package parser.grammar;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import symbols.Alphabet;
import symbols.NonTerminal;
import symbols.Token;
import symbols.Tokens;
import util.FileUtil;
import util.Pair;

/**
 * Represents a grammar loaded from a file.
 */
public class Grammar extends AbstractGrammar {

	/**
	 * Create grammar defined in a file.
	 * 
	 * @param grammarFile
	 *            File containing the grammar definition.
	 * @param tokens
	 *            All available tokens
	 * @throws IOException
	 *             FileNotFoundExeption when the file does not exist or cannot
	 *             be read
	 */
	public Grammar(String grammarFile, Tokens tokens) throws IOException {
		ArrayList<String> lines = FileUtil.fileToStringLines(grammarFile);
		ArrayList<Pair<NonTerminal, String>> tmpRules = new ArrayList<Pair<NonTerminal, String>>();
		boolean first = true;

		// Gather non-terminals and temporarily store right-hand sides
		for (String line : lines) {
			String[] parts = line.split("->");
			if (parts.length != 2) {
				throw new IOException("Expected declaration of form 'Nonterminal -> rules' in line: '" + line + "'");
			}
			NonTerminal nonTerminal = addNonTerminal(parts[0].trim());
			if (first) {
				// Set start symbol
				setStart(nonTerminal);
				first = false;
			}
			tmpRules.add(new Pair<NonTerminal, String>(nonTerminal, parts[1]));
		}

		// Create rules from right-hand sides
		for (Pair<NonTerminal, String> pair : tmpRules) {
			String[] choices = pair.getSecond().split("\\|");
			for (String strChoice : choices) {
				List<Alphabet> choice = getChoice(strChoice.trim(), tokens);
				this.addRule(pair.getFirst(), choice);
			}
		}

	}

	/**
	 * Generate choice from given string representation.
	 * 
	 * @param strChoice
	 *            Choice as string.
	 * @param tokens
	 *            List of tokens.
	 * @return Choice in terms of non-terminals and tokens.
	 * @throws IOException
	 *             If symbol is not found.
	 */
	private List<Alphabet> getChoice(String strChoice, Tokens tokens) throws IOException {
		String[] alphabets = strChoice.split("\\s+");
		List<Alphabet> choice = new ArrayList<Alphabet>();
		for (String s : alphabets) {
			try {
				Token token = tokens.get(s);
				choice.add(token);
				continue;
			} catch (Exception e) {
				// token not found
			}
			NonTerminal nonTerminal = getNonTerminal(s);
			if (nonTerminal != null) {
				choice.add(nonTerminal);
			} else {
				throw new IOException("Symbol '" + s + "' not known.");
			}
		}

		return choice;
	}

}
