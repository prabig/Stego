package WavEmbed;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

import Embed.ByteArrayBuilder;
import Embed.CarrierFile;
import Embed.Embed;
import Embed.Payload;
import Embed.RecoveredPayload;

public class WavEmbed extends Embed{
	public static final File NO_PAD = null;
	public WavEmbed() {
		
	}
	public boolean embedPayload(Payload payload, CarrierFile file, String outputFileName) throws Exception {
		PCMWavFile wavFile = (PCMWavFile)file;
		
		byte[] buffer = new byte[wavFile.header.bitsPerSample/8];
		FileOutputStream fstream = new FileOutputStream(outputFileName);
		
		try {
			
			fstream.write(wavFile.header.getHeaderBytes());
			int b,len=0;
			while((b=payload.nextBit()) != -1) {
				len = wavFile.getNext(buffer); //0000000x
				if(len == -1) {
					break;//EOF
				}
				buffer[0] = (byte) (buffer[0] & ~1);
				buffer[0] = (byte) (buffer[0] | b); //0000000x|11111110=1111111x; buffer[0] & 1111111x = yyyyyyyx
				fstream.write(buffer,0,len);
				
			}

			
			if(len == -1) {
				throw new Exception("TOO BIG!");
				//return false; //didn't finish properly
			} 
			else {	
				try {
					fstream.write(wavFile.getRemainingBytes());
				} catch (Exception e) {
					if(e.getMessage().equals("No more bytes.")){
						fstream.close();
					}
				}
				fstream.close();
				return true;
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
	}
	
	public boolean embedPayload(Payload payload, PCMWavFile wavFile) throws Exception {
		return embedPayload(payload, wavFile,"out_" + wavFile.fileName);
	}
	
	public RecoveredPayload unembedPayload(CarrierFile file, String password, File externalPad) throws Exception {
		PCMWavFile wavFile = (PCMWavFile)file;
		ByteArrayBuilder builder = new ByteArrayBuilder();
		int numBits;
		
		int b, count=0;
		byte[] buffer = new byte[wavFile.header.bitsPerSample/8];
		while(count++<32) {
			if((b=wavFile.getNext(buffer)) == -1)
				break;	
			builder.write(buffer[0]&1);	
		}
		
		int header = ByteBuffer.wrap(builder.toByteArray()).getInt();
		numBits = (header&0x3FFFFFF) * 8 + getPasswordHashLength(header);
		
		for(int i=0; i<numBits; i++) {
			if((b=wavFile.getNext(buffer)) == -1)
				break;
			builder.write(buffer[0]&1);
		}
		
		byte[] pad = RecoveredPayload.NO_PAD; 
		if(externalPad != NO_PAD) {
			pad = new byte[(int)externalPad.length()];
			FileInputStream tmpFile = new FileInputStream(externalPad);
			tmpFile.read(pad);
			tmpFile.close();
		}

		return new RecoveredPayload(builder.toByteArray(), password, pad);
	}
}
