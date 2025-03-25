package symbols;

/**
 * All recognized letters.
 */
public class Letters {

	// Individual letters
	final static public char[] alpha = { 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p',
			'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K',
			'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z' };

	// Individual digits
	final static public char[] numbers = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9' };

	// Underscore and digits
	final static public char[] underScoreNumerical = { '_', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9' };

	// Special characters like parenthesis, line breaks, ...
	final static public char[] special = { ':', '=', '(', ')', '{', '}', '+', '-', '*', '/', '<', '>', '!', '%', '$',
			'&', '|', ';', '"', ' ', '\t', '\r', '\n', ',', '.' };

}
