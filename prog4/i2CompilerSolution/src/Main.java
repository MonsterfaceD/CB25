import java.io.IOException;
import java.util.List;

import lexer.LexerException;
import lexer.LexerGenerator;
import parser.*;
import parser.grammar.AbstractGrammar;
import parser.grammar.Grammar;
import parser.grammar.WhileGrammar;
import symbols.Symbol;
import symbols.Tokens;
import util.FileUtil;
import util.Pair;

/**
 * Main class for running the compiler.
 */
public class Main {

	/**
	 * Starting method.
	 * 
	 * @param args
	 *            Arguments which should contain the path to the text file to
	 *            compile.
	 */
	public static void main(String[] args) {
		// If args is not a path to a text file, show help.
		// Otherwise open the file

		String inputProgram = "";
		String tokenFile = "";
		String grammarFile = "";

		if (args.length == 0) {
			showHelp();
			System.exit(1);
		} else {
			try {
				inputProgram = FileUtil.fileToString(args[0]);
			} catch (IOException e) {
				e.printStackTrace();
				showHelp();
				System.exit(1);
			}
			Pair<String, String> optionalFiles = parseOptionalCliArguments(args);
			tokenFile = optionalFiles.getFirst();
			grammarFile = optionalFiles.getSecond();
		}

		// Append symbol for EOF
		inputProgram += "$";

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

		// Lexical Analysis
		List<Symbol> symbols = null;
		try {
			symbols = LexerGenerator.analyse(tokens, inputProgram);
			System.out.println("Symbol stream: " + symbols);
		} catch (LexerException e) {
			System.out.println("LexErr");
			System.out.println(e.getMessage());
			System.out.println(e.getAnalysisBeforeFailure());
		}

		// Syntactical Analysis
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

		System.out.println("Grammar:");
		System.out.println(grammar);

		// Compute LR(0) sets
		System.out.println("Exercise (a)");
		LR0SetGenerator generatorLR0 = new LR0SetGenerator(grammar);
		System.out.println("LR(0) sets:");
		generatorLR0.printLR0Sets();
		System.out.println("There are " + generatorLR0.nrStates() + " LR(0) sets.");
		System.out.println();

		System.out.println("Exercise (b)");
		System.out.println(generatorLR0.nrConflicts() + " conflicts were detected.");
		System.out.println();

		// LR(0) parser
		System.out.println("Exercise (c)");
		LR0Parser parserLR0 = new LR0Parser(grammar);
		List<Rule> analysisLR0 = null;
		try {
			analysisLR0 = parserLR0.parse(symbols);
			System.out.print("LR(0) parsing result: ");
			System.out.println(analysisLR0);
		} catch (ParserException e) {
			System.out.println("ParseErr");
			System.out.println(e.getMessage());
			System.out.println(e.getAnalysisBeforeFailure());
		}
		System.out.println();

		// Compute first and follow sets
		System.out.println("Exercise (d)");
		LookAheadGenerator generatorLookAhead = new LookAheadGenerator(grammar);
		System.out.println("First sets:");
		generatorLookAhead.printFirstSets();
		System.out.println();

		// Compute LR(1) sets
		System.out.println("Exercise (e)");
		LR1SetGenerator generatorLR1 = new LR1SetGenerator(grammar);
		System.out.println("LR(1) sets:");
		generatorLR1.printLR1Sets();
		System.out.println("There are " + generatorLR1.nrStates() + " LR(1) sets.");
		System.out.println();

		System.out.println("Exercise (f)");
		System.out.println(generatorLR1.nrConflicts() + " conflicts were detected.");
		System.out.println();

		// LR(1) parser
		System.out.println("Exercise (g)");
		LR1Parser parserLR1 = new LR1Parser(grammar);
		List<Rule> analysisLR1 = null;
		try {
			analysisLR1 = parserLR1.parse(symbols);
			System.out.print("LR(1) parsing result: ");
			System.out.println(analysisLR1);
		} catch (ParserException e) {
			System.out.println("ParseErr");
			System.out.println(e.getMessage());
			System.out.println(e.getAnalysisBeforeFailure());
		}

		// Semantical Analysis

		// Byte Code Generation

	}

	/**
	 * Show help.
	 */
	public static void showHelp() {
		System.out.println("Usage: java Main PATH_TO_SOURCE_FILE");
		System.out.println("Optional arguments:");
		System.out.println("  --tokens   File containing definitions of additional tokens");
		System.out.println("  --grammar  File containing grammar definition");
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
			} else {
				System.out.println("Unknown argument '" + args[i] + "'");
				System.exit(1);
			}
		}
		return new Pair<String, String>(tokenFile, grammarFile);
	}

}
