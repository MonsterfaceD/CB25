import java.io.IOException;

import lexer.AbstractDFA;
import lexer.dfa.CommentDFA;
import lexer.dfa.WordDFA;
import symbols.Tokens.Token;
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

		// Lexical Analysis
		System.out.println("Input: " + inputProgram);
		// Try to recognize with automaton for "while"
		AbstractDFA whileDFA = new WordDFA("while", Token.WHILE);
		System.out.println("WHILE: " + whileDFA.run(inputProgram));

		// Try to recognize with automaton for comments
		AbstractDFA commentDFA = new CommentDFA();
		System.out.println("COMMENT: " + commentDFA.run(inputProgram));

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
