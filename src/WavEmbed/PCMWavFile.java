package WavEmbed;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import Embed.CarrierFile;


public class PCMWavFile extends CarrierFile{
	private FileInputStream wavFile;
	protected WavHeader header;
	protected WavData data;
	protected String fileName;
	
	
	public static void main(String[] args) {
		PCMWavFile file = new PCMWavFile("c:\\Users\\prabi_000\\Desktop\\uncomp.wav");
		System.out.println(file.header.sampleRate);
		System.out.println(file.header.bitsPerSample);
		System.out.println(file.header.numChannels);
	}
	
	public PCMWavFile(String fileName) {
		this.fileName = fileName;
		try {
			wavFile = new FileInputStream(new File(fileName));
			header = new WavHeader(wavFile);
			data = new WavData(wavFile, header.bitsPerSample,header.subchunk2Size,header.headerSize);
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public int getNext(byte[] b) throws IOException {
		return data.getNextSample(b);
	}
	
	public byte[] getRemainingBytes() throws Exception {
		return data.getRemainingBytes();
	}
	
	public long position() throws IOException {
		return data.position();
	}
	
	public void position(long p) {
		data.position(p);
	}
}
