import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.antlr.v4.runtime.*;

/**
 * Main class for running the regex grammar.
 * Do not change functionality.
 */
public class RegexGrammarRunner {
	/**
	 * Main method.
	 * 
	 * @param args Arguments.
	 * @throws Exception Throws IOException and IllegalArgumentException.
	 */
	public static void main(String[] args) throws Exception {
		if (args.length == 0) {
			System.out.println("Input regex:");
			// If no arguments are given, read regex from command line.
			CharStream input = CharStreams.fromStream(System.in);
			// Print the parsed regex
			// Obtain the automaton
			Automaton aut = getAutomaton(input, true);
			// Print the dot format of the automaton.
			System.out.print(aut.dotFormat());
			System.out.println("");
			return;
		}

		List<String> regexandwords = new ArrayList<String>();
		if (args.length == 1) {
			// If it is one argument, it should be a path to a file.
			Path file = Paths.get(args[0]);
			// Read the file, put entries in regexandwords
			try (InputStream in = Files.newInputStream(file);
					BufferedReader reader = new BufferedReader(new InputStreamReader(in))) {
				String line = null;
				while ((line = reader.readLine()) != null) {
					regexandwords.add(line);
				}
				if (regexandwords.size() % 2 == 1) {
					throw new IllegalArgumentException("Expect an even number of entries: Regex/word pairs");
				}
			} catch (IOException x) {
				System.err.println(x);
			}
		} else {
			// Arguments are given via command line
			if (args.length % 2 == 1) {
				throw new IllegalArgumentException("Expect an even number of arguments: Regex/word pairs");
			}
			regexandwords = Arrays.asList(args);
		}

		// regexandwords is expected to be an alternating list of regex and
		// words.
		for (int i = 0; i < regexandwords.size(); ++i) {
			String regex = regexandwords.get(i++);
			String word = regexandwords.get(i);
			CharStream input = CharStreams.fromString(regex);
			Automaton aut = getAutomaton(input, true);
			Boolean member = acceptWord(aut, word);

			System.out.println("'" + word + "' is a member of '" + regex + "'? " + member);
		}
	}

	/**
	 * Build an automaton for the given regex.
	 * 
	 * @param regex      Given a regex as a String
	 * @param printRegex A flag which indicates whether the parsed regex should be
	 *                   printed for debugging purposes.
	 * @return The automaton accepting the language described by the regex.
	 */
	public static Automaton getAutomaton(CharStream regex, Boolean printRegex) {
		// TODO: implement

		// Parse the CharStream to some form of Syntax Tree.
		// You HAVE to use ANTLRv4
		// Typically, you will use the auto-generated lexer to generate tokens,
		// and use a automatically generated parser to construct a ParseTree

		if (printRegex) {
			// Here, it is helpful to write some code which prints the regex
			// after parsing.

		}

		// Now, create the automaton.
		// You might want to use the Thompson construction

		Automaton aut = new Automaton();
		return aut;
	}

	/**
	 * Check if the automaton accepts the given word.
	 * 
	 * @param aut  Automaton.
	 * @param word Word.
	 * @return True iff the automaton accepts the word.
	 */
	public static Boolean acceptWord(Automaton aut, String word) {
		// We suggest you use the following call here, and implement the
		// Automaton::accept(List<Symbol>) method.
		return aut.accept(word);
	}

}
