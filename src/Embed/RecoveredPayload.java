package Embed;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

import Encrypt.OnetimePad;
import Zip.UnzippedFile;
import Zip.ZipFile;

public class RecoveredPayload {
	private int						header,
									encryption,
									compression,
									dataStart;
	
	private boolean					hasPassword,
									hasFile;
	
	private String 					originalFilename = null,
									passwordAttempt;
	
	private byte[]					headerHashDataByteStream,
									passwordHash = null,
									onetimePad = null,
									data;
	
	public static final int 		FILE = 1<<31, 
									MESSAGE = 0, 
									PASSWORD_PRESENT = 1<<28, 
									NO_PASSWORD = 0, 
									PLAINTEXT = 0, 
									EXTERNAL_PAD = 1<<26, 
									INTERNAL_RANDOM = 2<<26,  
									MAX_DATA_LENGTH = 1<<26,
									NO_COMPRESSION = 0, 
									FIXED_HUFFMAN = 1<<29, 
									DYNAMIC_HUFFMAN = 2<<29, 
									LZW = 3<<29;
	
	public static final byte[]		NO_PAD = null;		
	private static final String 	salt = "129768765865ljhkljhklj776276";
	
	
	public RecoveredPayload(byte[] headerHashDataByteStream, String passwordAttempt, byte[] onetimePad) throws IOException, NoOnetimePadProvidedException, InvalidEncryptionSchemeException, InvalidCompressionSchemeException, IncorrectPasswordException {
		this.headerHashDataByteStream = headerHashDataByteStream;
		this.passwordAttempt = passwordAttempt;
		this.onetimePad = onetimePad;
		
		decodeHeader();
		checkPassword();
		findWhereDataStarts();
		retrieveDataFromPayload();
		decryptData();
		unzipIfFile();
		decompressIfMessage();
	}
	
	public RecoveredPayload(byte[] headerHashDataByteStream, String passwordAttempt, File onetimePadFile) throws IOException, NoOnetimePadProvidedException, InvalidEncryptionSchemeException, InvalidCompressionSchemeException, IncorrectPasswordException {
		this(headerHashDataByteStream, passwordAttempt, fileToBytes(onetimePadFile));
	}
	
	public static byte[] fileToBytes(File oneTimePadFile) throws IOException {
		if(oneTimePadFile == null)
			return null;
		ByteArrayOutputStream bstream = new ByteArrayOutputStream();
		FileInputStream fstream = new FileInputStream(oneTimePadFile);
		byte[] buffer = new byte[2048];
		int len;
		while((len = fstream.read(buffer)) != -1) {
			bstream.write(buffer);
		}
		return bstream.toByteArray();
	}
	
	/**
	 * Payload header integer made into four bytes on payload data. Looks like: TCCPEELLLLLLLLLLLLLLLLLLLLLLLLLL
	 * T=type bit (File or message), 0 for msg 1 for file
	 * C=compression bits, 00 none, 01 static huffman, 10 dynamic huffman, 11 LZW
	 * P=password present bit, 0 no, 1 yes has password
	 * L=Datalength bits, represented in bytes
	**/
	private void decodeHeader() {
		header = ByteBuffer.wrap(headerHashDataByteStream).getInt();
		isFileOrMessage();
		findEncryptionType();
		findCompressionType();
		doesItHavePassword();
	}
	private void isFileOrMessage() {
		hasFile = ((header & 0x80000000) < 0);
	}
	private void findEncryptionType() {
		encryption = header & 0x0C000000;
	}
	private void findCompressionType() {
		compression = header & 0x60000000;
	}
	
	private void doesItHavePassword() {
		hasPassword = ((header & 0x10000000) > 0);
		
		if(hasPassword) {
			passwordHash = Arrays.copyOfRange(headerHashDataByteStream, 4, 20);
		}
	}
	
	private void checkPassword() throws IncorrectPasswordException {
		if(!hasPassword) {
			if(passwordAttempt != null && !passwordAttempt.equals("")) {
				throw new IncorrectPasswordException();
			}
			else{
				return;
			}
		}
		
		try {
			MessageDigest md1 = MessageDigest.getInstance("MD5");
			md1.update((salt+passwordAttempt).getBytes());
			if(!Arrays.equals(md1.digest(), passwordHash)){
				throw new IncorrectPasswordException();
			}
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		
	}
	
	private void findWhereDataStarts() {
		dataStart = 4; //offset for header, 4 bytes
		if(hasPassword) {
			dataStart += 16; //128 bit MD5, or 16 bytes
		}
	}
	
	private void retrieveDataFromPayload() {
		data = Arrays.copyOfRange(headerHashDataByteStream, dataStart, headerHashDataByteStream.length);
	}
	
	private void decryptData() throws NoOnetimePadProvidedException, InvalidEncryptionSchemeException {
		switch(encryption) {
		case PLAINTEXT:
			return;
		case EXTERNAL_PAD:
			if(onetimePad == null)
				throw new NoOnetimePadProvidedException();
			else 
				OnetimePad.decrypt(data, onetimePad);
			break;
		case INTERNAL_RANDOM:
			OnetimePad.decrypt(data, passwordAttempt);
			break;
		default:
			throw new InvalidEncryptionSchemeException();
		}
	}
	
	private void decompressIfMessage() throws InvalidCompressionSchemeException {
		if(!hasFile) {
			switch(compression) {
			case NO_COMPRESSION:
				return;
			case FIXED_HUFFMAN:
				//
				break;
			case DYNAMIC_HUFFMAN:
				//
				break;
			case LZW:
				//
				break;
			default:
				throw new InvalidCompressionSchemeException();
			}
		}
	}
	
	private void unzipIfFile() throws IOException {
		if(hasFile) {
			UnzippedFile unzippedFile = new UnzippedFile(this.data);
			originalFilename = unzippedFile.getOriginalFilename();
			data = unzippedFile.getOriginalFileBytes();
		}
	}
	
	public boolean containsFile() {
		return hasFile;
	}
	
	public byte[] getData() {
		return this.data;
	}
	
	public boolean saveFileAs(File file) throws NoFileInPayloadException, IOException {
		if(!hasFile) {
			throw new NoFileInPayloadException();
		}
		FileOutputStream fileToSaveTo = new FileOutputStream(file);
		fileToSaveTo.write(data);
		fileToSaveTo.close();
		return true;
	}
	
	public String getDefaultFilename() {
		return originalFilename;
	}
	
	public boolean saveFileAs(String name) throws NoFileInPayloadException, IOException {
		return saveFileAs(new File(name));
	}

	public String retrieveOriginalTextMessage() throws NoMessageInPayloadException {	
		if(!hasFile)
			return new String(data);
		else
			throw new NoMessageInPayloadException();
	}
}
