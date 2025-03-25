import java.io.IOException;
import java.util.List;

import checker.DeclarationChecker;
import generator.GeneratorException;
import generator.JasminGenerator;
import lexer.LexerException;
import lexer.LexerGenerator;
import parser.ParserException;
import parser.Rule;
import parser.LR1Parser;
import parser.grammar.AbstractGrammar;
import parser.grammar.Grammar;
import parser.grammar.WhileGrammar;
import symbols.Symbol;
import symbols.Tokens;
import util.FileUtil;

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
		String dotFile = "";
		String jasminOutFile = "";

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
			String[] optionalFiles = parseOptionalCliArguments(args);
			assert (optionalFiles.length == 4);
			tokenFile = optionalFiles[0];
			grammarFile = optionalFiles[1];
			dotFile = optionalFiles[2];
			jasminOutFile = optionalFiles[3];
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
			System.out.println("Lexer output: " + symbols);
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
				System.exit(3);
			}
		} else {
			try {
				grammar = new Grammar(grammarFile, tokens);
			} catch (IOException e) {
				System.out.println("Error while reading grammar file");
				System.out.println(e);
				System.exit(3);
			}
		}
		// Parse
		LR1Parser parser = new LR1Parser(grammar);
		List<Rule> analysis = null;
		try {
			analysis = parser.parse(symbols);
			System.out.println("Parser output: " + analysis);
		} catch (ParserException e) {
			System.out.println("ParseErr");
			System.out.println(e.getMessage());
			System.out.println(e.getAnalysisBeforeFailure());
		}

		// Semantical Analysis
		DeclarationChecker checker = new DeclarationChecker(symbols, analysis);
		if (!dotFile.isEmpty()) {
			try {
				FileUtil.stringToFile(dotFile, checker.getAst().toDotOutput());
			} catch (IOException e) {
				System.out.println("Error while writing to dot file.");
				System.out.println(e);
				System.exit(4);
			}
		}
		System.out.println("Every variable was declared before use: " + checker.checkDeclaredBeforeUsed());

		// Byte Code Generation
		JasminGenerator jasminGenerator = new JasminGenerator();
		String jasminCode = "";
		try {
			jasminCode = jasminGenerator.translateWHILE(FileUtil.getFileName(jasminOutFile), checker.getAst());
		} catch (GeneratorException e) {
			System.out.println("GeneratorErr");
			System.out.println(e.getMessage());
			System.exit(5);
		}
		// System.out.println("JASMIN code:");
		// System.out.println(jasminCode);

		try {
			FileUtil.stringToFile(jasminOutFile, jasminCode);
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(6);
		}
		System.out.println("Jasmin code written to " + jasminOutFile);

	}

	/**
	 * Show help.
	 */
	public static void showHelp() {
		System.out.println("Usage: java Main PATH_TO_SOURCE_FILE");
		System.out.println("Optional arguments:");
		System.out.println("  --tokens   File containing definitions of additional tokens");
		System.out.println("  --grammar  File containing grammar definition");
		System.out.println("  --dot      Output file for dot output");
		System.out.println("  --out      Output file for jasmin output");
	}

	/**
	 * Parse optional arguments given on the command line.
	 * 
	 * @param args
	 *            Cli arguments.
	 * @return List of [token file, grammar file, dot file, jasminOut file].
	 */
	private static String[] parseOptionalCliArguments(String[] args) {
		String tokenFile = "";
		String grammarFile = "";
		String dotFile = "";
		String jasminOutFile = "";

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
			} else if (args[i].equals("--dot")) {
				++i;
				if (i < args.length) {
					dotFile = args[i];
				} else {
					System.out.println("No dot filename given");
					System.exit(1);
				}
			} else if (args[i].equals("--out")) {
				++i;
				if (i < args.length) {
					jasminOutFile = args[i];
				} else {
					System.out.println("No jasmin output filename given");
					System.exit(1);
				}
			} else {
				System.out.println("Unknown argument '" + args[i] + "'");
				System.exit(1);
			}
		}
		return new String[] { tokenFile, grammarFile, dotFile, jasminOutFile };
	}

}
