package Embed;

import java.io.ByteArrayOutputStream;

public class ByteArrayBuilder {
	private ByteArrayOutputStream bstream;
	private byte bbyte;
	private int bitCount;
	
	public ByteArrayBuilder() {
		bbyte = 0;
		bitCount = 0;
		bstream = new ByteArrayOutputStream();
	}
	
	public void write(int b) throws Exception { //0000000x
		if(b != 0 && b != 1) {
			throw new Exception("NOT A VALID BIT!");
		}
		else {
			bbyte |= b<<(7-bitCount%8);
			
			if(bitCount%8 == 7) {
				bstream.write(bbyte);
				bbyte = 0;
			}
			
			bitCount++;
		}
	}
	
	public void write2(int b) throws Exception {
		this.write(b&2>>1);
		this.write(b&1);
	}
	
	public byte[] toByteArray() throws Exception {
		if(bitCount%8 != 0) {
			//throw new Exception("INVALID NUMBER OF BITS! CAN'T MAKE A FULL BYTE!");
		}
		return bstream.toByteArray();
	}
	
	public int getBitCount() {
		return this.bitCount;
	}
	
}
