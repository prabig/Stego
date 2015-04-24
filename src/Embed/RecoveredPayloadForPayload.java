package Embed;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JFileChooser;

import Encrypt.OnetimePad;
import Zip.ZipFile;

public class RecoveredPayloadForPayload {
	private int header;
	private int totalBytes;
	private byte[] data;
	private int currentBit;
	private static final String salt = "129768765865ljhkljhklj776276";
	
	private int fileOrMsg, compression, password, encryption;
	private byte[] passwordHash;
	private byte[] originalMessage;
	private String defaultFileName;
	
	public static final int FILE = 1, MESSAGE = 0, NO_COMPRESSION = 0, FIXED_HUFFMAN = 1, 
							DYNAMIC_HUFFMAN = 2, ZIP = 3, PLAINTEXT = 0, EXTERNAL_PAD = 1,
							INTERNAL_RANDOM = 2;
	public static final String NO_PASSWORD = null;
	public static final byte[] NO_PAD = null;
	
//	file(1) or message(0) - 1 bit 
//	compression - none 0, huffman fixed 1, huffman dynamic 2, zip 3 (2 bits)
//	password 0/1 (1 bit)
//	encryption - plaintext 0, one-time pad external 1, pseudo one time pad 2, 
//	numbytes - variable 26 bits

	public RecoveredPayloadForPayload(byte[] data, String passwordAttempt, byte[] externalPad) throws Exception {
		header = ByteBuffer.wrap(data).getInt();
		isFile();
		findCompression();
		hasPassword();
		findEncryption();
		findSize();
//		if(!passErrorCheck()){
//			throw new Exception("Not a valid payload or file corrupt.");
//		}
		if(password == 1){
			findPasswordHash(data);
			if(!hashMatch(passwordAttempt)) {
				throw new Exception("Invalid password!");
			}
		}
		this.data = Arrays.copyOfRange(data,4+16*password, totalBytes);
		data = this.data;
		
		if(encryption == EXTERNAL_PAD) {
			if(externalPad == NO_PAD) {
				throw new OnetimePadException("This file requires an external one-time pad to be decrypted.");
			}
			else {
				OnetimePad.decrypt(this.data, externalPad);
			}
		}
		else if (encryption == INTERNAL_RANDOM) {
			OnetimePad.decrypt(this.data, passwordAttempt);
		}
		
		//fileOrMsg
		if(fileOrMsg == FILE) {
			ZipFile zip = new ZipFile(this.data);
			zip.nextEntry();
			this.data = zip.unzipNext();
			defaultFileName = zip.currentEntryName();
			//FileOutputStream s = new FileOutputStream("new_"+zip.currentEntryName());
		}
	}
	
	public RecoveredPayloadForPayload(byte[] data) throws Exception {
		this(data, NO_PASSWORD, NO_PAD);
	}
	
	public String retrieveOriginalTextMessage() {
		return new String(data);
	}

	public void saveFileAs(String name) throws IOException {
		if(name != null && containsFile()) {
			int i = defaultFileName.lastIndexOf(".");
			
			String extension;
			if(i != -1)
				extension = defaultFileName.substring(i);
			else
				extension = null;
			if(!extension.equals("") && !name.endsWith(extension))
				name = name + extension;
				
			FileOutputStream newFile = new FileOutputStream(name);
			newFile.write(data);
			newFile.close();
		}	
	}
	
	public String getDefaultFileName() {
		return defaultFileName;
	}
	
	public boolean containsFile() {
		return fileOrMsg==1;
	}
	
	private void isFile() {
		fileOrMsg = (header>>>31)&1;
	}
	private void findCompression() {
		compression = (header>>>29)&3;// yXXyyyy...y_0 ->00000...yXX
	}
	private void hasPassword() {
		password = (header>>>28)&1;
	}
	private void findEncryption() {
		encryption = (header>>>26)&3;
	}
	private void findSize() {
		totalBytes = header & 0x3FFFFFF;
	}
	private void findPasswordHash(byte[] data) {
		passwordHash = Arrays.copyOfRange(data, 4, 20);
	}
	private boolean passErrorCheck() {
		return (fileOrMsg != 0 && fileOrMsg != 1) || (compression < 0 || compression > 3) ||(password != 0 && password != 1) ||
				(encryption < 0 || encryption > 2) || (fileOrMsg == 1 && compression != 3);
	}
	private boolean hashMatch(String passwordAttempt) throws NoSuchAlgorithmException {
		MessageDigest md1 = MessageDigest.getInstance("MD5");
		md1.update((salt+passwordAttempt+header).getBytes());
		return Arrays.equals(md1.digest(), passwordHash);
	}
	private String promptForLocation() {
		JFileChooser chooser = new JFileChooser();
		
		return null;
	}
}
