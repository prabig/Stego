package Huffman;

/**
 * Symbol value
 */
public class SymbolNode extends Node {

	public final int symbol;

	public SymbolNode(int symbol) {
		if (symbol < 0)
			throw new IllegalArgumentException("Illegal symbol value");
		this.symbol = symbol;
	}

}
