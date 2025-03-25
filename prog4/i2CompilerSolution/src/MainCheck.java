
import java.io.IOException;
import java.util.List;

import lexer.LexerException;
import lexer.LexerGenerator;
import parser.grammar.AbstractGrammar;
import parser.grammar.Grammar;
import parser.grammar.WhileGrammar;
import symbols.Symbol;
import symbols.Tokens;
import unittests.ParserChecker;
import util.FileUtil;
import util.Pair;

/**
 * Main class for running the compiler.
 */
public class MainCheck {

	/**
	 * Starting method.
	 * 
	 * @param args
	 *            Arguments which should contain the path to the text file to
	 *            compile.
	 */
	public static void main(String[] args) {
		String inputProgram = "";
		String tokenFile = "";
		String grammarFile = "";
		assert (args.length == 3 || args.length == 5);

		// Input file will be loaded later
		String inputFile = args[0];
		// Set expected validity of input file
		boolean expectValidLR0 = Boolean.parseBoolean(args[args.length - 2]);
		boolean expectValidInput = Boolean.parseBoolean(args[args.length - 1]);

		// Load token and grammar files
		Pair<String, String> optionalFiles = parseOptionalCliArguments(args);
		tokenFile = optionalFiles.getFirst();
		grammarFile = optionalFiles.getSecond();

		// Initialize tokens
		Tokens tokens = new Tokens();
		if (tokenFile.isEmpty()) {
			// Load default tokens
			tokens.setDefault();
		} else {
			try {
				tokens.setFromFile(tokenFile);
			} catch (Exception e) {
				System.out.println("Error while reading token file");
				System.out.println(e);
				System.exit(2);
			}
		}

		// Load grammar
		AbstractGrammar grammar = null;
		if (grammarFile.isEmpty()) {
			// Load default grammar
			try {
				grammar = new WhileGrammar(tokens);
			} catch (Exception e) {
				System.out.println("Error while constructing default grammar");
				System.out.println(e);
				System.exit(2);
			}
		} else {
			try {
				grammar = new Grammar(grammarFile, tokens);
			} catch (IOException e) {
				System.out.println("Error while reading grammar file");
				System.out.println(e);
				System.exit(2);
			}
		}

		// Initialize checker
		ParserChecker checker = new ParserChecker(grammar);

		// Check LR0 sets, conflicts and first and follow sets
		String output = checker.checkAll(expectValidLR0,true);
		System.out.print(output);

		// Load input
		try {
			inputProgram = FileUtil.fileToString(inputFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
		inputProgram += "$";

		// Lexical Analysis
		List<Symbol> input = null;
		try {
			input = LexerGenerator.analyse(tokens, inputProgram);
		} catch (LexerException e) {
			System.out.println("LexErr");
			System.out.println(e.getMessage());
			System.out.println(e.getAnalysisBeforeFailure());
		}

		// Check LR0 Parsing
		String result = "";
		try {
			result = checker.checkLR0Parsing(input, expectValidLR0 && expectValidInput);
		} catch (Exception e) {
			result += "Exception occured: " + e.getMessage() + "\n";
			e.printStackTrace();
		}
		System.out.print(result);

		// Check LR1 Parsing
		result = "";
		try {
			result = checker.checkLR1Parsing(input, expectValidInput);
		} catch (Exception e) {
			result += "Exception occured: " + e.getMessage() + "\n";
			e.printStackTrace();
		}
		System.out.print(result);
	}

	/**
	 * Parse optional arguments given on the command line.
	 * 
	 * @param args
	 *            Cli arguments.
	 * @return Pair of token file and grammar file.
	 */
	private static Pair<String, String> parseOptionalCliArguments(String[] args) {
		String tokenFile = "";
		String grammarFile = "";

		for (int i = 1; i < args.length; ++i) {
			if (args[i].equals("--tokens")) {
				++i;
				if (i < args.length) {
					tokenFile = args[i];
				} else {
					System.out.println("No token file provided");
					System.exit(1);
				}
			} else if (args[i].equals("--grammar")) {
				++i;
				if (i < args.length) {
					grammarFile = args[i];
				} else {
					System.out.println("No grammar file provided");
					System.exit(1);
				}
			}
		}
		return new Pair<String, String>(tokenFile, grammarFile);
	}

}
