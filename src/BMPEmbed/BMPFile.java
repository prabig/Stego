package BMPEmbed;

import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.DataBufferByte;
import java.awt.image.DataBufferInt;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;

import javax.imageio.ImageIO;

import Embed.CarrierFile;

public class BMPFile extends CarrierFile {
	protected BufferedImage in;
	protected ColorModel model;
	protected int height;
	protected int width;
	private int count;
	
	//RGB
	public BMPFile(String filename) {
		count = 0;
		try {
			in = ImageIO.read(new File(filename));
			height = in.getHeight();
			width = in.getWidth();
			model = in.getColorModel();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public int getNext(byte[] b) throws IOException {
		if(count < in.getHeight()*in.getWidth()) {
			int[] size = getDimension();
			ByteBuffer.wrap(b).putInt(in.getRGB(size[0], size[1]));
			count++;
			return 4;
		}
		else {
			return -1;
		}
	}

	@Override
	public byte[] getRemainingBytes() throws Exception {
		ByteArrayOutputStream bstream = new ByteArrayOutputStream();
		while(count < in.getHeight()*in.getWidth()) {
			int[] size = getDimension();
			byte[] bytes = ByteBuffer.allocate(4).putInt(in.getRGB(size[0], size[1])).array();
			count++;
			bstream.write(bytes);
		}
		return bstream.toByteArray();
	}
	
	private int[] getDimension() {
		int x = count%in.getWidth();
		int y = count/in.getWidth();
		return new int[]{x, y};
	}

	
	public byte[] getImageData() {
		WritableRaster wraster = in.getRaster();
		DataBufferByte buf = (DataBufferByte)wraster.getDataBuffer();
		return buf.getData();
	}
}
