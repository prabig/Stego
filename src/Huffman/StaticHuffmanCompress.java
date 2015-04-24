package Huffman;

import java.io.*;

/**
 * This uses fixed Huffman tables. Useful for text input in English.
 * 
 * @version 1.3
 * @since 10/30/2014
 */
public abstract class StaticHuffmanCompress {

	/**
	 * Test function
	 * 
	 * @param args sourcefile destination [writeCanonicalCode <filename>]
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {

		boolean writeCanonicalCode = !Boolean.valueOf(args[2]);

		// Read input, count symbols, make tree
		FrequencyTable frequencyTable = getFrequencies(new File(args[0]));
		frequencyTable.increment(256); // EOF symbol gets a frequency of 1
		CodeTree code = frequencyTable.buildTree();
		CanonicalCode canonicalCode = new CanonicalCode(code, 257);
		code = canonicalCode.toCodeTree(); // Replace code tree with canonical
											// one.

		// Compress input file w/ Huffman tree
		InputStream inputFile = new BufferedInputStream(new FileInputStream(
				new File(args[0])));
		BitOutputStream outputFile = new BitOutputStream(
				new BufferedOutputStream(new FileOutputStream(
						new File(args[1]), false)));
		try {

			if (writeCanonicalCode) {
				// Write cananonical code to separate file
				String canonicalHeaderFile = args[3];
				BitOutputStream canonicalOut = new BitOutputStream(
						new BufferedOutputStream(new FileOutputStream(
								canonicalHeaderFile, false)));

				writeCode(canonicalOut, canonicalCode);

				canonicalOut.close();
			} else
				writeCode(outputFile, canonicalCode);

			compress(code, inputFile, outputFile);
		} finally {
			outputFile.close();
			inputFile.close();
		}
	}

	private static FrequencyTable getFrequencies(File file) throws IOException {
		FrequencyTable frequencyTable = new FrequencyTable(new int[257]);
		InputStream input = new BufferedInputStream(new FileInputStream(file));
		try {
			while (true) {
				int b = input.read();
				if (b == -1)
					break;
				frequencyTable.increment(b);
			}
		} finally {
			input.close();
		}
		return frequencyTable;
	}

	public static void compress(CodeTree code, InputStream inputFile,
			BitOutputStream outputFile) throws IOException {
		HuffmanEncoder enc = new HuffmanEncoder(outputFile);
		enc.codeTree = code;
		while (true) {
			int b = inputFile.read();
			if (b == -1)
				break;
			enc.write(b);
		}
		enc.write(256); 
	}

	/**
	 * Convert to Huffman codes and write
	 * @param outputFile
	 * @param canonicalCode
	 * @throws IOException
	 */
	private static void writeCode(BitOutputStream outputFile,
			CanonicalCode canonicalCode) throws IOException {

		for (int i = 0; i < canonicalCode.getSymbolLimit(); i++) {
			int val = canonicalCode.getCodeLength(i);
			if (val >= 256)
				throw new RuntimeException("The code for a symbol is too long");

			for (int j = 7; j >= 0; j--)
				outputFile.write((val >>> j) & 1);
		}
	}

}
