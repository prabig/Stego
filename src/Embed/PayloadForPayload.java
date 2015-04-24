package Embed;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.security.MessageDigest;
import java.util.Arrays;

import Zip.ZipFile;
import Encrypt.OnetimePad;

public class PayloadForPayload {
	private int header;
	private int totalBits;
	private byte[] data;
	private int currentBit;
	private static final String salt = "129768765865ljhkljhklj776276";
	
	public static final int FILE = 1<<31, MESSAGE = 0, NO_COMPRESSION = 0, FIXED_HUFFMAN = 1<<29, 
							DYNAMIC_HUFFMAN = 2<<29, ZIP = 3<<29, PASSWORD_PRESENT = 1<<28, PLAINTEXT = 0, EXTERNAL_PAD = 1<<26,
							INTERNAL_RANDOM = 2<<26,  DATA_LENGTH = 1<<26;
	public static final String NO_PASSWORD = null;
	public static final byte[] NO_PAD = null;
	
	public PayloadForPayload(byte[] data, int fileOrMsg, int compression, String password, int encryption, byte[] externalPad) throws Exception {
		header = 0;
		if(fileOrMsg==FILE || fileOrMsg==MESSAGE) //1 bit
			header |= fileOrMsg;
		else
			throw new Exception("You must have a file or message!");
		
		if( Arrays.binarySearch(new int[]{NO_COMPRESSION, FIXED_HUFFMAN, DYNAMIC_HUFFMAN, ZIP}, compression) != -1) //2 bits
			header |= compression;
		else
			throw new Exception("Not a valid compression mode.");
		
		
		if(Arrays.binarySearch(new int[]{PLAINTEXT, EXTERNAL_PAD, INTERNAL_RANDOM}, 0) != -1)
			header |= encryption; //2 bits
		else
			throw new Exception("Not a valid encryption mode.");
		
		if(data.length < DATA_LENGTH)  //can have upto 2^26 bytes
			header |= data.length;
		else
			throw new Exception("File larger than 64 MB!");
		
		byte[] passwordHash = null;
		MessageDigest ds = MessageDigest.getInstance("MD5");
		if(password != null && !password.equals("")) {
			header |= PASSWORD_PRESENT; //1 bit
			ds.update((salt+password+header).getBytes());
			passwordHash = ds.digest();
		}
		
		if(encryption == INTERNAL_RANDOM) {
			if(password == NO_PASSWORD)
				OnetimePad.encrypt(data, "defaultPassword");
			else
				OnetimePad.encrypt(data, password);
		}
		else if(encryption == EXTERNAL_PAD) {
			if(externalPad == NO_PAD) {
				throw new Exception("No pad chosen! Please select a one-time pad if you wish to use one-time pad encryption.");
			}
			else
				OnetimePad.encrypt(this.data, externalPad);
		}
		
		if(compression == FIXED_HUFFMAN) {
			
		}
		
		currentBit = 0;
		totalBits = data.length*8 + 32;
		ByteArrayOutputStream bstream = new ByteArrayOutputStream();
		bstream.write(ByteBuffer.allocate(4).putInt(header).array());
		if(passwordHash != null)
			bstream.write(passwordHash);
		bstream.write(data);
		this.data = bstream.toByteArray();
	}
	
	public PayloadForPayload(byte[] data) throws Exception {
		this(data, MESSAGE, FIXED_HUFFMAN, null, PLAINTEXT, NO_PAD);
	}
	
	public PayloadForPayload(File file, String password, int encryption, byte[] pad) throws Exception {
		this((new ZipFile(file)).toByteArray(), FILE, ZIP, password, encryption, pad);
	}
	
	public PayloadForPayload(File file) throws Exception {
		this(file, NO_PASSWORD, PLAINTEXT, NO_PAD);
	}
	
	public PayloadForPayload(File file, String password) throws Exception {
		this(file, password, INTERNAL_RANDOM, NO_PAD);
	}
	
	public PayloadForPayload(File file, String password, int encryption) throws Exception {
		this(file, password, encryption, NO_PAD);
		if(encryption == EXTERNAL_PAD) {
			throw new Exception("No pad included! Need pad for encryption.");
		}
	}
	
	public PayloadForPayload(String message, String password, int encryption, int compression, byte[] pad) throws Exception {
		this(message.getBytes(), MESSAGE, compression, password, encryption, NO_PAD);
	}
	
	public int nextBit() {
		int c;
		if(currentBit < totalBits) { //xyyyyyyy
			c = (data[currentBit/8]>>>(7-(currentBit++%8))) & 1;
			return c;
		}
		else
			return -1;
	}
}
