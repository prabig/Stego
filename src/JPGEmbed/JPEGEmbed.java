package JPGEmbed;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import javax.imageio.ImageIO;

import Embed.ByteArrayBuilder;
import Embed.CarrierFile;
import Embed.Embed;
import Embed.FilePayload;
import Embed.MessagePayload;
import Embed.Payload;
import Embed.RecoveredPayload;
import JPGEmbed.jpegdecode.JPEGDecoder;
import JPGEmbed.jpegdecode.JPEGDecoder.PixelArray;

public class JPEGEmbed extends Embed{
	
	public static void main(String args[]) {
		JPEGEmbed emb = new JPEGEmbed();
		try {
//			emb.embedPayload(null, null, null);
			RecoveredPayload rpay = emb.unembedPayload(null, null, null);
//			
			if(rpay.containsFile()) {
				rpay.saveFileAs(new File("JPEG_" + rpay.getDefaultFilename()));
			} else{
				System.out.print(rpay.retrieveOriginalTextMessage());
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public boolean embedPayload(Payload payload, CarrierFile carrier,
			String outputFileName) throws Exception {
		
		JPEGFile jpgFile = (JPEGFile)carrier;

		
		FileOutputStream outFile = new FileOutputStream(outputFileName);
		JPEGEncode encoder = new JPEGEncode(jpgFile.jpgImage, 90, new FileOutputStream(outputFileName), payload);
		encoder.Compress();
		outFile.close();
		return true;
	}

	@Override
	public RecoveredPayload unembedPayload(CarrierFile file, String password,
			File externalPadFile) throws Exception {
		ByteArrayOutputStream dctCoefficients = new ByteArrayOutputStream();
		JPEGFile jpgFile = (JPEGFile)file;
		
		JPEGDecoder decoder = new JPEGDecoder(new BufferedOutputStream(dctCoefficients));
		decoder.decode(new FileInputStream(jpgFile.file));
		
		
		ByteBuffer byteBuffer = ByteBuffer.wrap(dctCoefficients.toByteArray());
		
		ByteArrayBuilder builder = new ByteArrayBuilder();
		int b;
		int count = 0; //count which int
		while(builder.getBitCount() < 32) {
			b = byteBuffer.getInt();
			if((b<0 || b>1) && count%64!=0) {
				builder.write(b&1);
			}
			count++;
		}
		
		int header = ByteBuffer.wrap(builder.toByteArray()).getInt();
		int totalBits = (header&0x03FFFFFF)*8 + getPasswordHashLength(header);
		
		//System.out.println(header);
		for(int i=0; i<totalBits;) {
			b = byteBuffer.getInt();
			if((b<0 || b>1) && count%64!=0) {
				builder.write(b&1);
				i++;
			}
			count++;
		}
		
		return new RecoveredPayload(builder.toByteArray(), password, externalPadFile);
	}	
}
