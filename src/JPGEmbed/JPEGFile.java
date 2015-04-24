package JPGEmbed;

import java.awt.Component;
import java.awt.Image;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

import javax.imageio.ImageIO;

import Embed.CarrierFile;
import JPGEmbed.jpegdecode.JPEGDecoder;

public class JPEGFile extends CarrierFile {
	
	protected Image jpgImage;
	protected File file;
	
	public JPEGFile(File imgLocation) throws Exception{
		this.jpgImage = ImageIO.read(imgLocation);
		file = imgLocation;
	}
	
	public JPEGFile(String filename) throws Exception {
		this(new File(filename));
	}

	@Override
	public int getNext(byte[] b) throws IOException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public byte[] getRemainingBytes() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}
	
}