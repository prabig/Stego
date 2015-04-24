package Huffman;

import java.io.*;
import java.util.Arrays;

/**
 * Compress message with a custom made Huffman tree based on input
 *
 */
public abstract class StatisticalHuffmanCompress {
	final boolean TEST = true;

	public static void main(String[] args) throws IOException {

		// Begin compression
		InputStream inputFile = new BufferedInputStream(new FileInputStream(
				new File(args[0])));
		BitOutputStream outputFile = new BitOutputStream(
				new BufferedOutputStream(
						new FileOutputStream(new File(args[1]))));
		try {
			compress(inputFile, outputFile);
		} finally {
			outputFile.close();
			inputFile.close();
		}
	}

	public static void compress(InputStream inputStream,
			BitOutputStream outputStream) throws IOException {
		int[] initFreqs = new int[257];
		Arrays.fill(initFreqs, 1);

		FrequencyTable frequencyTable = new FrequencyTable(initFreqs);
		HuffmanEncoder encoder = new HuffmanEncoder(outputStream);
		encoder.codeTree = frequencyTable.buildTree(); 

		int i = 0;
		while (true) {
			int b = inputStream.read();
			if (b == -1)
				break;
			encoder.write(b);

			frequencyTable.increment(b);
			i++;
			if (i < 262144 && (i > 0 && (i & -i) == i)
					|| i % 262144 == 0) // Update tree
				encoder.codeTree = frequencyTable.buildTree();
			if (i % 262144 == 0) // Reset freq table
				frequencyTable = new FrequencyTable(initFreqs);
		}
		// EOF
		encoder.write(256); 
	}

}
