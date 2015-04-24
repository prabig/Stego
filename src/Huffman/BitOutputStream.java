package Huffman;

import java.io.*;


/**
 * A stream where bits can be written to.
 */
public class BitOutputStream {
	
	
	private OutputStream outputStream; 
	private int currentByte, bitsInCurrentByte; 
	
	/**
	 * Constructor
	 * @param outputStream OutputStream to write to
	 */
	public BitOutputStream(OutputStream outputStream) {
		if (outputStream == null)
			throw new NullPointerException("Argument is null");
		this.outputStream = outputStream;
		currentByte = 0;
		bitsInCurrentByte = 0;
	}
	
	
	/**
	 * Write bit to stream.
	 * @param b Bit to write
	 * @throws IOException
	 */
	public void write(int b) throws IOException {
		if (!(b == 0 || b == 1))
			throw new IllegalArgumentException("Argument must be 0 or 1");
		currentByte = currentByte << 1 | b;
		bitsInCurrentByte++;
		if (bitsInCurrentByte == 8) {
			outputStream.write(currentByte);
			bitsInCurrentByte = 0;
		}
	}
		
	/**
	 * Close stream.
	 * @throws IOException
	 */
	public void close() throws IOException {
		while (bitsInCurrentByte != 0)
			write(0);
		outputStream.close();
	}
	
}
