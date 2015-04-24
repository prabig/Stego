package Embed;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import Encrypt.OnetimePad;

public class Payload {
	protected byte[] 			passwordHash = null,
								data,
								onetimePad = null,
								byteStreamWithHeaderAndHash;
	protected int 				payloadHeader = 0, 
								encryption,
								bitIndex,
								numBits;
	
	protected String 			password = null;
	
	public static final int 	FILE = 1<<31, 
								MESSAGE = 0, 
								PASSWORD_PRESENT = 1<<28, 
								NO_PASSWORD = 0, 
								PLAINTEXT = 0, 
								EXTERNAL_PAD = 1<<26, 
								INTERNAL_RANDOM = 2<<26,  
								MAX_DATA_LENGTH = 1<<26;
	
	public static final byte[]	NO_PAD = null;
	private static final String salt = "129768765865ljhkljhklj776276";
	
	protected Payload(byte[] data, String password, int encryption, byte[] onetimePad) {
		this.onetimePad = onetimePad;
		this.data = data;
		this.encryption = encryption;
		this.password = password;
	}
	
	public boolean containsFile() {
		return false;
	}
	
	protected void createPasswordHash() {
		if(password != null && password.length()>0) {
			try {
				MessageDigest md1 = MessageDigest.getInstance("MD5");
				md1.update((salt + password).getBytes());
				passwordHash = md1.digest();
			} catch (NoSuchAlgorithmException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * Payload header integer made into four bytes on payload data. Looks like: TCCPEELLLLLLLLLLLLLLLLLLLLLLLLLL
	 * T=type bit (File or message), 0 for msg 1 for file
	 * C=compression bits, 00 none, 01 static huffman, 10 dynamic huffman, 11 LZW
	 * P=password present bit, 0 no, 1 yes has password
	 * L=Datalength bits, represented in bytes
	 * @param dataLength
	 * @param password
	 * @param compression
	 * @param encryption
	 * @throws DataTooLongException
	 * @throws InvalidEncryptionSchemeException 
	 */
	protected void makeHeader(int fileOrMsg) throws NoOnetimePadProvidedException, InvalidEncryptionSchemeException, DataTooLongException {
		payloadHeader |= fileOrMsg;
		
		//set password present bit
		
		payloadHeader |= (password != null && password.length() > 0)?PASSWORD_PRESENT:0;
		
		switch(encryption) { //check if valid encryption type
		case PLAINTEXT:
		case INTERNAL_RANDOM:
			payloadHeader |= encryption;
			break;
		case EXTERNAL_PAD:
			if(onetimePad == null)
				throw new NoOnetimePadProvidedException();
		default:
			throw new InvalidEncryptionSchemeException();	
		}
		
		if(data.length > MAX_DATA_LENGTH) {
			throw new DataTooLongException();
		}
		payloadHeader |= data.length;
	}
	
	protected void embedHeaderHashAndDataIntoByteStream() {
		ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
		try {
			// write payload header
			byte[] headerIntAsBytes = ByteBuffer.allocate(4)
					.putInt(payloadHeader).array();
			byteStream.write(headerIntAsBytes);

			// write password hash if exists
			if (passwordHash != null)
				byteStream.write(passwordHash);

			// write data
			byteStream.write(data);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
		byteStreamWithHeaderAndHash = byteStream.toByteArray();
		
		numBits = byteStreamWithHeaderAndHash.length*8;
						  
	}
	
	protected void encryptData() throws InvalidEncryptionSchemeException {
		switch(encryption) {
		case PLAINTEXT:
			return;
		case EXTERNAL_PAD:
			OnetimePad.encrypt(data, onetimePad);
			break;
		case INTERNAL_RANDOM:
			OnetimePad.encrypt(data, password);
			break;
		default:
			throw new InvalidEncryptionSchemeException();
		}
	}
	
	public int nextBit() {
		if(bitIndex>=numBits) { //end of bit stream
			return -1; 
		}
		else {
			int b = byteStreamWithHeaderAndHash[bitIndex/8]>>>(7-bitIndex%8)&1;
			bitIndex++;
			return b;
		}
	}

	public int nextTwoBits() {
		int x;
		if((x=nextBit()) != -1) {
			x<<=1;
			x |= nextBit();
		}
		return x;
	}

}

