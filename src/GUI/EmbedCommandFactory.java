package GUI;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import BMPEmbed.BMPEmbed;
import BMPEmbed.BMPFile;
import Embed.BadFilenameException;
import Embed.CarrierFile;
import Embed.Embed;
import Embed.FilePayload;
import Embed.MessagePayload;
import Embed.PadSelectionException;
import Embed.Payload;
import Embed.Types;
import JPGEmbed.JPEGEmbed;
import JPGEmbed.JPEGFile;
import PNGEmbed.PNGEmbed;
import PNGEmbed.PNGFile;
import WavEmbed.PCMWavFile;
import WavEmbed.WavEmbed;

public abstract class EmbedCommandFactory {
	
	public static void clickUnembedFile() {
		UnembedWindow window = new UnembedWindow();
		window.setUndecorated(true);
		window.setVisible(true);
	}
	
	public static File clickBrowseCarrierInput() throws NullPointerException {
		JFileChooser chooser = new JFileChooser();
		chooser.setFileFilter(new ImageFileFilter());
		chooser.setCurrentDirectory(new File("."));
		chooser.showOpenDialog(null);
		chooser.setVisible(true);
		return chooser.getSelectedFile();
	}
	
	public static File clickBrowseSaveTo() throws NullPointerException {
		JFileChooser chooser = new JFileChooser();
		chooser.setFileFilter(new ImageFileFilter());
		chooser.setCurrentDirectory(new File("."));
		chooser.showSaveDialog(null);
		chooser.setVisible(true);
		return chooser.getSelectedFile();
	}
	
	public static File clickBrowseToPickEmbedPayload() {
		JFileChooser chooser = new JFileChooser();
		chooser.setCurrentDirectory(new File("."));
		chooser.showOpenDialog(null);
		chooser.setVisible(true);
		return chooser.getSelectedFile();
	}
	public static boolean clickEmbed(String whatWillBeEmbeded, String message,
			String payloadFileName, String compressionStr, String encryptionStr,
			boolean passwordSelected, String password, String carrierFileName,
			String saveTo) {
		int fileOrMsg, compression, encryption;
		Payload payload;
		
		
		fileOrMsg = Types.valueOf(whatWillBeEmbeded).payloadConstant;
		try{
			compression = Types.valueOf(compressionStr).payloadConstant;
		}
		catch (NullPointerException e) {
			compression = 3;
		}
		encryption = Types.valueOf(encryptionStr).payloadConstant;
		
		if(fileOrMsg == Types.File.payloadConstant) {
			try {
				payload = new FilePayload(new File(payloadFileName), password, encryption, null);
			} catch (Exception e) {
				JOptionPane.showMessageDialog(null, e.getMessage());
				return false;
			}
		}
		return true;
	}
	
	public static boolean embedFile(String payloadFileName, String carrierFilename, String outputFilename, String password, String encryptionStr) throws Exception {
		int encryption;
		Payload payload;
		
		if(encryptionStr == null) {
			encryption = Payload.PLAINTEXT;
		}
		else
		{
			encryption = Types.valueOf(encryptionStr).payloadConstant;
		}
		
		
		if(encryption == Payload.EXTERNAL_PAD) {
			byte[] pad;
			try {
				pad = padBytes();
			} catch (Exception e1) {
				throw new PadSelectionException();
			}
			
			try {
				payload = new FilePayload(new File(payloadFileName), password, encryption, pad);
			} catch (Exception e) {
				return false;
			}
		}
		else {
			try {
				payload = new FilePayload(new File(payloadFileName), password, encryption);
			} catch (Exception e) {
				return false;
			}
		}
		
		
		boolean isSuccessful = embedPayload(payload, carrierFilename, outputFilename);
		
		
		return isSuccessful;

	}
	
	
	
	public static boolean embedMessage(String message, String carrierFilename, String outputFilename, String password, String encryptionStr, String compressionStr) throws Exception {
		int encryption = 0;
		int compression = MessagePayload.NO_COMPRESSION;
		
		if(encryptionStr == null) {
			encryption = Payload.PLAINTEXT;
		}
		else {
			encryption = Types.valueOf(encryptionStr).payloadConstant;
		}
		
		if(compressionStr != null){
			compression = Types.valueOf(compressionStr).payloadConstant;
		}
		
		byte[] pad = null;
		
		if(encryption == Payload.EXTERNAL_PAD) {
			try {
				pad = padBytes();
			} catch (Exception e1) {
				return false;
			}
		}
		
		Payload payload = null;
		try {
			payload = new MessagePayload(message, password, encryption, compression, pad);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		
		boolean isSuccessful = embedPayload(payload, carrierFilename, outputFilename);

		return isSuccessful;
	}
	

	public static byte[] padBytes() throws IOException  {
		JFileChooser fc = new JFileChooser();
		fc.showOpenDialog(null);
		fc.setVisible(true);
		
		FileInputStream fstream = null;
		fstream = new FileInputStream(fc.getSelectedFile());
		
		ByteArrayOutputStream bstream = new ByteArrayOutputStream();
		
		byte[] buffer = new byte[2048];
		int len;
		while((len = fstream.read(buffer)) != -1) {
			bstream.write(buffer,0,len);
		}
		
		fstream.close();
		
		byte[] pad = bstream.toByteArray();
		
		return pad;
	}
	
	private static boolean embedPayload(Payload payload, String carrierFilename, String outputFilename) throws Exception {
		CarrierFile carrier = null;
		Embed embedder = null;
		
		int suffixStart = carrierFilename.lastIndexOf(".");
		String suffix = carrierFilename.substring(suffixStart);
		
		
		if(suffix.equalsIgnoreCase(".wav")) {
			carrier = new PCMWavFile(carrierFilename);
			embedder = new WavEmbed();
		}
		else if (suffix.equalsIgnoreCase(".bmp")) {
			carrier = new BMPFile(carrierFilename);
			embedder = new BMPEmbed();
		}
		else if (suffix.equalsIgnoreCase(".png")){
			carrier = new PNGFile(carrierFilename);
			embedder = new PNGEmbed();
		}
		else if (suffix.equalsIgnoreCase(".jpg") || suffix.equalsIgnoreCase(".jpeg")) {
			carrier = new JPEGFile(carrierFilename);
			embedder = new JPEGEmbed();
		}
		else {
			throw new BadFilenameException();
		}
		
		try {
			return embedder.embedPayload(payload, carrier, outputFilename);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	
}
