package BMPEmbed;

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
import Embed.CarrierFileNotLargeEnoughException;
import Embed.Embed;
import Embed.FilePayload;
import Embed.MessagePayload;
import Embed.Payload;
import Embed.RecoveredPayload;

public class BMPEmbed extends Embed{
	public static final File NO_PAD = null;
	BufferedImage out;
	BMPFile bmpFile;
	int count;
	
	public BMPEmbed() {
		//out = new BufferedImage();
		count = 0;
	}

	public static void main(String[] args) {
		
		BMPEmbed em = new BMPEmbed();
		try {
			//Payload payload = new FilePayload(new File("PLChw4.docx"), "p", Payload.INTERNAL_RANDOM);
			Payload payload = new MessagePayload("Hello world!", "pw", Payload.PLAINTEXT, MessagePayload.NO_COMPRESSION, Payload.NO_PAD);
			em.embedPayload(payload, new BMPFile("red.bmp"), "holaaa.bmp");
			
			RecoveredPayload rp = em.unembedPayload(new BMPFile("holaaa.bmp"),"pw", NO_PAD);
			if(rp.containsFile()) {
				rp.saveFileAs("out____________"+rp.getDefaultFilename());
			} else {
				System.out.println(rp.retrieveOriginalTextMessage());
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public boolean embedPayload(Payload payload, CarrierFile carrier,
			String outputFileName) throws Exception {
		// TODO Auto-generated method stub
		bmpFile = (BMPFile)carrier;

		byte[] pictureData = getImageData();

		int b, count = 0, rgb;
		if(bmpFile.in.getAlphaRaster() != null) {
			
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
					throw new CarrierFileNotLargeEnoughException();
				}
				pictureData[count] = (byte) (pictureData[count] & ~1);
				pictureData[count] = (byte) (pictureData[count] | b);
				count++;
			}
		}

		
		if(b != -1) {
			return false;
		}
		
		return ImageIO.write(bmpFile.in, "bmp", new File(outputFileName));
	}

	@Override
	public RecoveredPayload unembedPayload(CarrierFile carrier, String password,
			File externalPad) throws Exception {
		
		bmpFile = (BMPFile)carrier;
		
		byte[] pictureData = getImageData();
		
		ByteArrayBuilder builder = new ByteArrayBuilder();
		
		int b = 0, count = 0;
		
		if(bmpFile.in.getAlphaRaster() != null) {
			while(builder.toByteArray().length < 1) {
				if(count%4 == 0)
					count++;
				builder.write(pictureData[count] & 1);
				count++;
			}		
			
			int header = ByteBuffer.wrap(pictureData).getInt();
		
			int totalBits = (header&0x3FFFFFF) * 8;
			
			for (int i=0; i<totalBits; i++, count++) {
				if(count%4 == 0)
					count++;
				builder.write(pictureData[count] & 1);
			}
		}
		
		else {
			while(builder.getBitCount() < 32) {
				b = (pictureData[count]&1);
				builder.write((byte)b);
				count++;
			}		
			
			
			int header = ByteBuffer.wrap(builder.toByteArray()).getInt();
		
			int totalBits = (header&0x03FFFFFF) * 8 + super.getPasswordHashLength(header);
			
			for (int i=0; i<totalBits; i++, count++) {
				b = pictureData[count]&1;
				builder.write(b);
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
			istream.close();
		} catch (NullPointerException e){
			pad = null;
		}
		
		return new RecoveredPayload(builder.toByteArray(), password, pad);
		
	}
	private int getX(int c) {
		return c % bmpFile.width;
	}
	
	private int getY(int c) {
		return c / bmpFile.width;
	}
	
	private byte[] getImageData() {
		return bmpFile.getImageData();
	}


	
}
