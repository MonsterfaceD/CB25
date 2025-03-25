package checker;

import java.util.ArrayList;
import java.util.List;

import symbols.Alphabet;
import symbols.Token;

/**
 * Represents a node in an AST.
 *
 */
public class ASTNode {

	private static int maxId = 0;

	// A list of the node's children
	// It is essential that the children are stored in an ordered list and not
	// e.g. a set. Reading the leaves from left to right then gives us the same
	// sequence as given by the lexer.
	private List<ASTNode> children = new ArrayList<ASTNode>();

	private int id;

	// Token or non-terminal
	private Alphabet alphabet;

	// The attribute of the token, only used when the alphabet is a token.
	private String attribute;

	/**
	 * Constructor.
	 * 
	 * @param alphabet
	 *            Token or non-terminal.
	 */
	public ASTNode(Alphabet alphabet) {
		this.id = maxId++;
		this.alphabet = alphabet;
		this.attribute = null;
	}

	/**
	 * Get alphabet.
	 * 
	 * @return Token or non-terminal.
	 */
	public Alphabet getAlphabet() {
		return alphabet;
	}

	/**
	 * Get the attribute.
	 * 
	 * @return Attribute.
	 */
	public String getAttribute() {
		assert (alphabet instanceof Token);
		return attribute;
	}

	/**
	 * Set the attribute.
	 * 
	 * @param attribute
	 *            Attribute.
	 */
	public void setAttribute(String attribute) {
		assert (alphabet instanceof Token);
		this.attribute = attribute;
	}

	/**
	 * Get all children.
	 * 
	 * @return List of all children.
	 */
	public List<ASTNode> getChildren() {
		return children;
	}

	/**
	 * Append a list of child nodes to this node
	 * 
	 * @param children
	 *            List of children.
	 */
	public void addChildren(List<ASTNode> children) {
		this.children.addAll(children);
	}

	/**
	 * Append a single child to this node
	 * 
	 * @param child
	 *            Child.
	 */
	public void addChild(ASTNode child) {
		this.children.add(child);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object other) {
		if (other instanceof ASTNode) {
			ASTNode otherNode = (ASTNode) other;
			return otherNode.id == this.id;
		}

		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode() {
		return id;
	}

}
