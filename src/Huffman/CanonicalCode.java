package Huffman;

import java.util.*;



/**
 * A canonical Huffman code stores bit length of symbol in order of symbols.
 * Ex:
 * {{A,1}, {B, 2}, {C,0},{D,3}...} converts to {{A, 0}, {B, 10}, {C,No Symbol},  {D, 110}...}
 * <a href="http://en.wikipedia.org/wiki/Canonical_Huffman_code">Read this for more info</a>
 *
 */
public class CanonicalCode {
	
	private int[] codeLengths;
	
	/**
	 * Constructor
	 * @param codeLengths Length of Huffman code for a given symbol
	 */
	public CanonicalCode(int[] codeLengths) {
		if (codeLengths == null)
			throw new NullPointerException("Argument is null");
		this.codeLengths = codeLengths.clone();
		for (int x : codeLengths) {
			if (x < 0)
				throw new IllegalArgumentException("Illegal code length");
		}
	}
	
	
	/**
	 * Constructor.
	 * @param tree CodeTree
	 * @param symbolLimit Maximum number of symbols
	 */
	public CanonicalCode(CodeTree codeTree, int symbolLimit) {
		codeLengths = new int[symbolLimit];
		buildCodeLengths(codeTree.getRoot(), 0);
	}
	
	/**
	 * 
	 * @param node 
	 * @param depth
	 */
	private void buildCodeLengths(Node node, int depth) {
		if (node instanceof InternalNode) {
			InternalNode internalNode = (InternalNode)node;
			buildCodeLengths(internalNode.leftChild , depth + 1);
			buildCodeLengths(internalNode.rightChild, depth + 1);
		} else if (node instanceof SymbolNode) {
			int symbol = ((SymbolNode)node).symbol;
			if (codeLengths[symbol] != 0)
				throw new AssertionError("Symbol has more than one code");  // Because CodeTree has a checked constraint that disallows a symbol in multiple leaves
			if (symbol >= codeLengths.length)
				throw new IllegalArgumentException("Symbol exceeds symbol limit");
			codeLengths[symbol] = depth;
		} else {
			throw new AssertionError("Illegal node type");
		}
	}
	
	
	/**
	 * 
	 * @return length
	 */
	public int getSymbolLimit() {
		return codeLengths.length;
	}
	
	/**
	 * 
	 * @param symbol
	 * @return
	 */
	public int getCodeLength(int symbol) {
		if (symbol < 0 || symbol >= codeLengths.length)
			throw new IllegalArgumentException("Symbol out of range");
		return codeLengths[symbol];
	}
	
	/**
	 * 
	 * @param array
	 * @return
	 */
	private static int max(int[] array) {
		int max = array[0];
		for (int i : array) {
			max = (i>max) ? i : max;
		}
		return max;
	}
	
	/**
	 * Make canonical codde into a tree
	 * @return CodeTree of canonical values
	 */
	public CodeTree toCodeTree() {
		List<Node> nodes = new ArrayList<Node>();
		for (int i = max(codeLengths); i >= 1; i--) {  // Descend through positive code lengths
			List<Node> newNodes = new ArrayList<Node>();
			
			// Add leaves for symbols with code length i
			for (int j = 0; j < codeLengths.length; j++) {
				if (codeLengths[j] == i)
					newNodes.add(new SymbolNode(j));
			}
			
			// Merge nodes from the previous deeper layer
			for (int j = 0; j < nodes.size(); j += 2)
				newNodes.add(new InternalNode(nodes.get(j), nodes.get(j + 1)));
			
			nodes = newNodes;
			if (nodes.size() % 2 != 0)
				throw new IllegalStateException("This canonical code does not represent a Huffman code tree");
		}
		
		if (nodes.size() != 2)
			throw new IllegalStateException("This canonical code does not represent a Huffman code tree");
		return new CodeTree(new InternalNode(nodes.get(0), nodes.get(1)), codeLengths.length);
	}
	
	

	
}
