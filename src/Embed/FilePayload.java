package Embed;

import java.io.File;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

import Zip.ZipFile;

public class FilePayload extends Payload{
	
	private FilePayload(byte[] data, String password, int encryption, byte[] onetimePad) throws DataTooLongException, InvalidEncryptionSchemeException, NoOnetimePadProvidedException {
		super(data, password, encryption,onetimePad);
		encryptData();
		makeHeader();
		createPasswordHash();
		embedHeaderHashAndDataIntoByteStream();
	}
	public FilePayload(File file, String password, int encryption, byte[] onetimePad) throws DataTooLongException, InvalidEncryptionSchemeException, NoOnetimePadProvidedException, IOException {
		this(zip(file), password, encryption, onetimePad);
	}
	
	public FilePayload(File file, String password, int encryption) throws DataTooLongException, InvalidEncryptionSchemeException, NoOnetimePadProvidedException, IOException {
		this(file,password,encryption, NO_PAD);
	}
	
	/**
	 * Returns zip file as an array to store in payload.
	 * @param file File we will zip up (what we want to put in the payload file)
	 * @return byte array of zip file
	 * @throws IOException
	 */
	private static byte[] zip(File file) throws IOException {
		ZipFile zipFile = new ZipFile(file);
		return zipFile.toByteArray();
	}

	private void makeHeader() throws DataTooLongException, InvalidEncryptionSchemeException, NoOnetimePadProvidedException {
		super.makeHeader(FILE);
	}
	
	public boolean containsFile() {
		return true;
	}
}
