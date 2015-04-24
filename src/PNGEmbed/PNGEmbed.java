package PNGEmbed;

import java.awt.color.ColorSpace;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.nio.ByteBuffer;

import javax.imageio.ImageIO;

import Embed.ByteArrayBuilder;
import Embed.CarrierFile;
import Embed.Embed;
import Embed.FilePayload;
import Embed.Payload;
import Embed.RecoveredPayload;

public class PNGEmbed extends Embed{
	public static final File NO_PAD = null;
	BufferedImage out;
	PNGFile pngFile;
	int count;
	
	public PNGEmbed() {
		//out = new BufferedImage();
		count = 0;
	}

	public static void main(String[] args) {
		
		PNGEmbed em = new PNGEmbed();
		try {
			Payload payload = new FilePayload(new File("PLChw4.docx"), "pw", Payload.INTERNAL_RANDOM);
			em.embedPayload(payload, new PNGFile("Untitled.png"), "holaaa.png");
			
			RecoveredPayload rp = em.unembedPayload(new PNGFile("holaaa.png"), "pw", NO_PAD);
			if(rp.containsFile()) {
				rp.saveFileAs("out"+rp.getDefaultFilename());
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public boolean embedPayload(Payload payload, CarrierFile carrier,
			String outputFileName) throws Exception {
		// TODO Auto-generated method stub
		pngFile = (PNGFile)carrier;

		byte[] pictureData = getImageData();

		int b, count = 0, rgb;
		if(pngFile.in.getAlphaRaster() != null) {
			
			while((b = payload.nextBit()) != -1) {
				if(pictureData.length <= count) {
					break;
				}
				if(count%4 == 0)
					count++;
				pictureData[count] = (byte) (pictureData[count] & ~1);
				pictureData[count] = (byte) (pictureData[count] | b);
				count++;
			}
		}
		else {
			while((b = payload.nextBit()) != -1) {
				if(pictureData.length < count) {
					break;
				}
				pictureData[count] = (byte) (pictureData[count] & ~1);
				pictureData[count] = (byte) (pictureData[count] | b);
				count++;
			}
		}

		
		if(b != -1) {
			return false;
		}
		
		return ImageIO.write(pngFile.in, "png", new File(outputFileName));
	}

	@Override
	public RecoveredPayload unembedPayload(CarrierFile carrier, String password,
			File externalPad) throws Exception {
		
		pngFile = (PNGFile)carrier;
		
		byte[] pictureData = getImageData();
		
		ByteArrayBuilder builder = new ByteArrayBuilder();
		
		int b = 0, count = 0;
		
		if(pngFile.in.getAlphaRaster() != null) {
			while(builder.getBitCount() < 32) {
				if(count%4 == 0)
					count++;
				builder.write(pictureData[count] & 1);
				count++;
			}		
			
			int header = ByteBuffer.wrap(builder.toByteArray()).getInt();
		
			int totalBits = (header&0x3FFFFFF) * 8;
			
			for (int i=0; i<totalBits; i++, count++) {
				if(count%4 == 0)
					count++;
				builder.write(pictureData[count] & 1);
			}
		}
		
		else {
			while(builder.getBitCount() < 32) {
				builder.write(pictureData[count]&1);
				count++;
			}		
			
			int header = ByteBuffer.wrap(builder.toByteArray()).getInt();
		
			int totalBits = (header&0x03FFFFFF) * 8 + getPasswordHashLength(header);
			
			for (int i=0; i<totalBits; i++, count++) {
				builder.write(pictureData[count]&1);
			}
		}
		

		byte[] pad = null;
		try {
			FileInputStream istream = new FileInputStream(externalPad);
			ByteArrayOutputStream bstream = new ByteArrayOutputStream();
			
			byte[] buffer = new byte[2048];
			int len;
			while((len = istream.read(buffer)) != -1){
				bstream.write(buffer,0,len);
			}
			pad = bstream.toByteArray();
		} catch (NullPointerException e){
			pad = null;
		}
		
		return new RecoveredPayload(builder.toByteArray(), password, pad);
		
	}
	private int getX(int c) {
		return c % pngFile.width;
	}
	
	private int getY(int c) {
		return c / pngFile.width;
	}
	
	private byte[] getImageData() {
		return pngFile.getImageData();
	}


	
}
