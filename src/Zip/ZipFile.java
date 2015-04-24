package Zip;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

public class ZipFile {
	private ByteArrayOutputStream bstream;
	private ZipOutputStream zout;
	private FileInputStream fstream;
	private ZipInputStream zin;
	private ZipEntry currentEntry;
	
	public ZipFile(File file) throws IOException {
		bstream = new ByteArrayOutputStream();
		zout = new ZipOutputStream(bstream);
		fstream = new FileInputStream(file);
		
		
		ZipEntry zentry = new ZipEntry(file.getName());
		zout.putNextEntry(zentry);
		
		int len;
		byte[] buffer = new byte[2048];
		while((len = fstream.read(buffer)) != -1) {
			zout.write(buffer,0,len);
		}
		
		zout.closeEntry();
		zout.close();
	}
	
	public ZipFile(byte[] bytes) throws IOException {
		zin = new ZipInputStream(new ByteArrayInputStream(bytes));
	}
	
	public byte[] toByteArray() {
		if(bstream != null)
			return bstream.toByteArray();
		else
			return null;
	}
	
	public void nextEntry() throws IOException {
		if(zin != null) {
			currentEntry = zin.getNextEntry();
		}
	}
	
	public String currentEntryName() {
		if(currentEntry != null)
			return currentEntry.getName();
		else
			return null;
	}
	
	public byte[] unzipNext() throws IOException {
		if(zin != null) {
			ByteArrayOutputStream tmpStream = new ByteArrayOutputStream();
			byte[] buffer = new byte[2048];
			int len;
			while ((len = zin.read(buffer)) != -1) {
				tmpStream.write(buffer, 0, len);
			}
			if(tmpStream.size()>0)
				return tmpStream.toByteArray();
			else
				return null;
		}
		else {
			return null;
		}
	}
	
	public void close() throws IOException {
		zin.close();
	}
}
