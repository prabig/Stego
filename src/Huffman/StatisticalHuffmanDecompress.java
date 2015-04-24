package Huffman;

import java.io.*;
import java.util.Arrays;


public abstract class StatisticalHuffmanDecompress {
	
	public static void main(String[] args) throws IOException {
		
		// Compress
		BitInputStream inputFile = new BitInputStream(new BufferedInputStream(new FileInputStream(new File(args[0]))));
		OutputStream out = new BufferedOutputStream(new FileOutputStream(new File(args[1])));
		try {
			decompress(inputFile, out);
		} finally {
			out.close();
			inputFile.close();
		}
	}
	
	
	static void decompress(BitInputStream inputFile, OutputStream out) throws IOException {
		int[] initFreqs = new int[257];
		Arrays.fill(initFreqs, 1);
		
		FrequencyTable freqTable = new FrequencyTable(initFreqs);
		HuffmanDecoder dec = new HuffmanDecoder(inputFile);
		dec.codeTree = freqTable.buildTree();
		int count = 0;
		while (true) {
			int symbol = dec.read();
			if (symbol == 256)  // EOF symbol
				break;
			out.write(symbol);
			
			freqTable.increment(symbol);
			count++;
			if (count < 262144 && isPowerOf2(count) || count % 262144 == 0)  // Update code tree
				dec.codeTree = freqTable.buildTree();
			if (count % 262144 == 0)  // Reset frequency table
				freqTable = new FrequencyTable(initFreqs);
		}
	}
	
	
	private static boolean isPowerOf2(int x) {
		return x > 0 && (x & -x) == x;
	}
	
}
