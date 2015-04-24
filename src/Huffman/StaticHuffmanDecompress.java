package Huffman;

import java.io.*;


/**
 * Decompress file written in Static Huffman codes into uncompressed bytecode
 * 
 */
public class StaticHuffmanDecompress {
	
	/**
	 * test main
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {
		
		boolean useSeperateCanonicalFile = Boolean.valueOf(args[2]);
		
		BitInputStream in = new BitInputStream(new BufferedInputStream(new FileInputStream(new File(args[0]))));
		OutputStream out = new BufferedOutputStream(new FileOutputStream( new File(args[1])));
		try {
			CanonicalCode canonCode;
			
			if (useSeperateCanonicalFile)
			{
				String headerInFile = args[3];
				BitInputStream headerIn = new BitInputStream(new BufferedInputStream(new FileInputStream(headerInFile)));
				
				canonCode = readCode(headerIn);
				
				headerIn.close();
			}
			else{
				canonCode = readCode(in);
			}
			
			CodeTree code = canonCode.toCodeTree();
			decompress(code, in, out);
		} finally {
			out.close();
			in.close();
		}
	}
	
	/**
	 * Retrieve canonical codes if present
	 * @param in
	 * @return
	 * @throws IOException
	 */
	private static CanonicalCode readCode(BitInputStream in) throws IOException {
		int[] codeLengths = new int[257];
		for (int i = 0; i < codeLengths.length; i++) {
			int val = 0;
			for (int j = 0; j < 8; j++) 
				val = val << 1 | in.readNoEof();
			codeLengths[i] = val;
		}
		return new CanonicalCode(codeLengths);
	}
	
	/**
	 * Decompress file
	 * @param codeTree Static tree built in
	 * @param in InputStream to read
	 * @param out OutputStream to write
	 * @throws IOException
	 */
	static void decompress(CodeTree codeTree, BitInputStream inputStream, OutputStream outputStream) throws IOException {
		HuffmanDecoder dec = new HuffmanDecoder(inputStream);
		dec.codeTree = codeTree;
		while (true) {
			int symbol = dec.read();
			if (symbol == 256)  // EOF
				break;
			outputStream.write(symbol);
		}
	}
	
}
