package Huffman;

import java.io.*;

/**
 * A stream of bits that can be read.
 */
public class BitInputStream {

	private InputStream inputStream;
	private int nextBits, bitsRemaining;
	private boolean isEndOfStream;

	/**
	 * Constructor
	 * 
	 * @param in InputStream to read from.
	 */
	public BitInputStream(InputStream inputStream) {
		if (inputStream == null)
			throw new NullPointerException("Argument is null");
		this.inputStream = inputStream;
		bitsRemaining = 0;
		isEndOfStream = false;
	}

	/**
	 * Read bit from stream until EOF
	 * 
	 * @return 0 or 1 bit
	 * @throws IOException
	 */
	public int readNoEof() throws IOException {
		int result = readBit();
		if (result != -1)
			return result;
		else
			throw new EOFException("End of stream!");
	}

	/**
	 * Read bit from stream until EOF. Returns -1 at EOF
	 * 
	 * @return 0 or 1 bit, -1 at EOF
	 * @throws IOException
	 */
	public int readBit() throws IOException {
		if (isEndOfStream)
			return -1;
		if (bitsRemaining == 0) {
			nextBits = inputStream.read();
			if (nextBits == -1) {
				isEndOfStream = true;
				return -1;
			}
			bitsRemaining = 8;
		}
		bitsRemaining--;
		return (nextBits >>> bitsRemaining) & 1;
	}

	/**
	 * Close stream.
	 * 
	 * @throws IOException
	 */
	public void close() throws IOException {
		inputStream.close();
	}

}
