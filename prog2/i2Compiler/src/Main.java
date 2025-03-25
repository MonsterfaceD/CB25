import java.io.IOException;
import java.util.List;

import lexer.LexerException;
import lexer.LexerGenerator;
import symbols.Symbol;
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

		if (args.length != 1) {
			showHelp();
			System.exit(0);
		} else {
			try {
				inputProgram = FileUtil.fileToString(args[0]);
			} catch (IOException e) {
				e.printStackTrace();
				showHelp();
				System.exit(0);
			}
		}

		// Append symbol for EOF
		inputProgram += "$";

		// Lexical Analysis
		List<Symbol> symbols = null;
		try {
			symbols = LexerGenerator.analyse(inputProgram);
			System.out.println("Symbol stream: " + symbols);
		} catch (LexerException e) {
			System.out.println("LexErr");
			System.out.println(e.getMessage());
			System.out.println(e.getAnalysisBeforeFailure());
		}

		// Syntactical Analysis

		// Semantical Analysis

		// Byte Code Generation

	}

	/**
	 * Show help.
	 */
	public static void showHelp() {
		System.out.println("Usage: java Main PATH_TO_SOURCE_FILE");
	}

}
