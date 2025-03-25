package checker;

import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Queue;
import java.util.Stack;

import parser.Rule;
import symbols.Alphabet;
import symbols.NonTerminal;
import symbols.Symbol;
import symbols.Token;

/**
 * Abstract syntax tree. Contains the root node of the AST. The tree is built of
 * ASTNode objects. Each ASTNode maintains a list of its direct successors
 * (children nodes).
 */
public class AST {

	// Root node.
	private ASTNode root;

	/**
	 * Constructs the corresponding AST for a given list of symbols and the
	 * sequence of derivations.
	 * 
	 * @param symbols
	 *            List of symbols from the lexer.
	 * @param rightMostDerivation
	 *            Sequence of (right-most) derivations from the parser.
	 */
	public AST(List<Symbol> symbols, List<Rule> rightMostDerivation) {
		Stack<ASTNode> innerNodes = new Stack<ASTNode>();
		ListIterator<Rule> derivationIterator = rightMostDerivation.listIterator(rightMostDerivation.size());
		ListIterator<Symbol> symbolsIterator = symbols.listIterator(symbols.size());

		// Set root node
		assert (!rightMostDerivation.isEmpty());
		Rule rootRule = derivationIterator.previous();
		root = new ASTNode(rootRule.getLhs());
		for (Alphabet rhs : rootRule.getRhs()) {
			ASTNode child = new ASTNode(rhs);
			root.addChild(child);
			innerNodes.add(child);
		}

		// Insert all nodes from stack
		while (innerNodes.size() > 0) {
			ASTNode stackTop = innerNodes.pop();
			if (stackTop.getAlphabet() instanceof NonTerminal) {
				// Insert all nodes from rule
				Rule rule = derivationIterator.previous();
				assert (rule.getLhs() instanceof NonTerminal);
				for (Alphabet rhs : rule.getRhs()) {
					ASTNode child = new ASTNode(rhs);
					stackTop.addChild(child);
					innerNodes.add(child);
				}
			} else {
				// Insert token
				assert (stackTop.getAlphabet() instanceof Token);
				assert (symbolsIterator.hasPrevious());
				stackTop.setAttribute(symbolsIterator.previous().getAttribute());
			}
		}
		assert (!derivationIterator.hasPrevious());
		assert (!symbolsIterator.hasPrevious());
	}

	/**
	 * Get root node.
	 * 
	 * @return Root node.
	 */
	public ASTNode getRoot() {
		return root;
	}

	/**
	 * Generate dot output.
	 * 
	 * @return String with dot output.
	 */
	public String toDotOutput() {
		Queue<ASTNode> queue = new LinkedList<ASTNode>();
		queue.add(root);
		StringBuilder nodeBuilder = new StringBuilder();
		StringBuilder transitionBuilder = new StringBuilder();

		// Iterate over all nodes
		while (!queue.isEmpty()) {
			// Output for node
			ASTNode node = queue.poll();
			nodeBuilder.append(node.hashCode());
			nodeBuilder.append(" [label=\"" + node.getAlphabet());
			if (node.getAlphabet() instanceof Token) {
				nodeBuilder.append(" " + node.getAttribute().replace("\"", ""));
			}
			nodeBuilder.append("\"];\n");

			// Output for transitions
			for (ASTNode child : node.getChildren()) {
				transitionBuilder.append(node.hashCode());
				transitionBuilder.append(" -> ");
				transitionBuilder.append(child.hashCode());
				transitionBuilder.append(";\n");
			}
			queue.addAll(node.getChildren());
		}
		return "digraph{\n" + nodeBuilder.toString() + transitionBuilder.toString() + "\n}";
	}

}
