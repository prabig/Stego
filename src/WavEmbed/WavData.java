package WavEmbed;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;

public class WavData {
	private FileInputStream wavFile;
	protected int bytesPerSample, 
				numBytes;
	private long dataStart;
	
	
	public WavData(FileInputStream wavFile, int bitsPerSample, int numBytes, long dataStart) throws IOException {
		this.wavFile = wavFile;
		this.bytesPerSample = bitsPerSample/8;
		this.numBytes = numBytes;
		this.dataStart = dataStart;
		
		
	}
	
	public int getNextSample(byte[] b) throws IOException {
		return wavFile.read(b);
	}
	
	public long position() throws IOException {
		return wavFile.getChannel().position();
	}
	
	public void position(long p) {
		try {
			wavFile.getChannel().position(dataStart + p);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public byte[] getRemainingBytes() throws Exception {
		ByteArrayOutputStream buff = new ByteArrayOutputStream();
		
		byte[] buffer = new byte[2048];
		int len;
		while((len=wavFile.read(buffer)) != -1) {
			buff.write(buffer, 0, len);
		}
		if(buff.size() == 0) {
			throw new Exception("No more bytes.");
		}
		return buff.toByteArray();
	}
}
