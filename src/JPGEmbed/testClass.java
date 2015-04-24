package JPGEmbed;

import java.awt.Image;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.Arrays;

import javax.imageio.ImageIO;

import Embed.FilePayload;
import Embed.MessagePayload;
import Embed.Payload;
import JPGEmbed.jpegdecode.JPEGDecoder;
import JPGEmbed.jpegdecode.JPEGDecoder.PixelArray;

public class testClass implements PixelArray{

	public static void main(String[] args) throws FileNotFoundException, Exception {
		// TODO Auto-generated method stub
		
		
//		ByteArrayOutputStream stream = new ByteArrayOutputStream();
//		
//		JPEGDecoder decoder = new JPEGDecoder(new BufferedOutputStream(stream));
//		decoder.decode(new FileInputStream("mount.jpg"), new testClass());
		
		Image image = ImageIO.read(new File("DSC_0197.png"));
		//Payload p = new MessagePayload("hello", "world", 0,0,null);
		Payload p = new FilePayload(new File("rudolph.mp3"), "world", Payload.INTERNAL_RANDOM);
		JPEGEncode encode = new JPEGEncode(image, 50, new FileOutputStream("writ.jpg"), p);
		encode.Compress();
		
	}

	@Override
	public void setSize(int width, int height) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setPixel(int x, int y, int argb) {
		// TODO Auto-generated method stub
		
	}

}
