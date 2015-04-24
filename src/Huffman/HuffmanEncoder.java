package Huffman;

import java.io.IOException;
import java.util.List;


public class HuffmanEncoder {
	
	private BitOutputStream output;
	
	public CodeTree codeTree;
	
	
	
	public HuffmanEncoder(BitOutputStream out) {
		if (out == null)
			throw new NullPointerException("NULL ARG");
		output = out;
	}
	
	
	
	public void write(int symbol) throws IOException {
		if (codeTree == null)
			throw new NullPointerException("NULL TREE");
		
		List<Integer> bits = codeTree.getCode(symbol);
		for (int b : bits)
			output.write(b);
	}
	
}
