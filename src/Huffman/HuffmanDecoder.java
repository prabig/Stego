package Huffman;

import java.io.IOException;

/**
 * Decode bitstream of Huffman values into uncompress bytes
 *
 */
public class HuffmanDecoder {
	
	private BitInputStream inputStream;
	public CodeTree codeTree;

	public HuffmanDecoder(BitInputStream inputStream) {
		if (inputStream == null)
			throw new NullPointerException("NULL BITSTREAM");
		this.inputStream = inputStream;
	}

	public int read() throws IOException {
		if (codeTree == null)
			throw new NullPointerException("NULL TREE");
		
		InternalNode currentNode = codeTree.getRoot();
		while (true) {
			int temp = inputStream.readNoEof();
			Node nextNode;
			if      (temp == 0) nextNode = currentNode.leftChild;
			else if (temp == 1) nextNode = currentNode.rightChild;
			else throw new AssertionError();
			
			if (nextNode instanceof SymbolNode)
				return ((SymbolNode)nextNode).symbol;
			else if (nextNode instanceof InternalNode)
				currentNode = (InternalNode)nextNode;
			else
				throw new AssertionError();
		}
	}
	
}