package Huffman;

import java.io.Serializable;
import java.util.*;


/**
 * Creates a CodeTree to traverse while reading bits from a compressed source.
 * <a href="https://www.siggraph.org/education/materials/HyperGraph/video/mpeg/mpegfaq/huffman_tutorial.html">More info</a>
 *
 */
public class CodeTree implements Serializable {
	
	private final InternalNode root; 
	private List<List<Integer>> codes;
	
	
	
	/**
	 * Constructor.
	 * @param root Root node
	 * @param symbolLimit Largest number bits in a symbol
	 */
	public CodeTree(InternalNode root, int symbolLimit) {
		if (root == null)
			throw new NullPointerException();
		this.root = root;
		
		codes = new ArrayList<List<Integer>>();  // Initially all null
		for (int i = 0; i < symbolLimit; i++)
			codes.add(null);
		buildCodeList(root, new ArrayList<Integer>());  // Fills 'codes' with appropriate data
	}
	
	/**
	 * Build code list of symbols
	 * @param node
	 * @param prefix
	 */
	private void buildCodeList(Node node, List<Integer> prefix) {
		if (node instanceof InternalNode) {
			InternalNode internalNode = (InternalNode)node;
			
			prefix.add(0);
			buildCodeList(internalNode.leftChild , prefix);
			prefix.remove(prefix.size() - 1);
			
			prefix.add(1);
			buildCodeList(internalNode.rightChild, prefix);
			prefix.remove(prefix.size() - 1);
			
		} else if (node instanceof SymbolNode) {
			SymbolNode leaf = (SymbolNode)node;
			if (leaf.symbol >= codes.size())
				throw new IllegalArgumentException("Symbol exceeds symbol limit");
			if (codes.get(leaf.symbol) != null)
				throw new IllegalArgumentException("Symbol has more than one code");
			codes.set(leaf.symbol, new ArrayList<Integer>(prefix));
			
		} else {
			throw new AssertionError("Illegal node type");
		}
	}
	
	/**
	 * 
	 * @return Root node
	 */
	public InternalNode getRoot() {
		return root;
	}
	
	/**
	 * Retrieve code with symbol
	 * @param symbol
	 * @return Code
	 */
	public List<Integer> getCode(int symbol) {
		if (symbol < 0)
			throw new IllegalArgumentException("Illegal symbol");
		else if (codes.get(symbol) == null)
			throw new IllegalArgumentException("No code for given symbol");
		else
			return codes.get(symbol);
	}
	
	
	/**
	 * Return string version of tree to debug
	 */
	public String toString() {
		StringBuilder sb = new StringBuilder();
		toString("", root, sb);
		return sb.toString();
	}
	
	/**
	 * Build string
	 * @param prefix
	 * @param node
	 * @param sb
	 */
	private static void toString(String prefix, Node node, StringBuilder sb) {
		if (node instanceof InternalNode) {
			InternalNode internalNode = (InternalNode)node;
			toString(prefix + "0", internalNode.leftChild , sb);
			toString(prefix + "1", internalNode.rightChild, sb);
		} else if (node instanceof SymbolNode) {
			sb.append(String.format("Code %s: Symbol %d%n", prefix, ((SymbolNode)node).symbol));
		} else {
			throw new AssertionError("Illegal node type");
		}
	}
	
    public  char getNextCharFromBitStream(BitInputStream stream) {
        return 'a';
    }
}
