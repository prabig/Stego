package Zip;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class UnzippedFile {
	private ZipInputStream 			zippedInput;
	
	private byte[] 					zippedData,
									unzippedData;
	
	private String 					originalFilename;
	
	public UnzippedFile(byte[] zippedData) throws IOException {
		this.zippedData = zippedData;
		zippedInput = new ZipInputStream(new ByteArrayInputStream(zippedData));
		ZipEntry zipEntry = zippedInput.getNextEntry();
		originalFilename = zipEntry.getName();
		
		ByteArrayOutputStream byteOutputStream = new ByteArrayOutputStream();
		
		byte[] buffer = new byte[2048];
		int len;
		while((len = zippedInput.read(buffer)) != -1) {
			byteOutputStream.write(buffer, 0, len);
		}
		unzippedData = byteOutputStream.toByteArray();
	}
	
	public byte[] getOriginalFileBytes() {
		return unzippedData;
	}
	
	public String getOriginalFilename() {
		return originalFilename;
	}
}
