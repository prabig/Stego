package GUI;

import java.awt.Component;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import PNGEmbed.PNGEmbed;
import PNGEmbed.PNGFile;
import WavEmbed.PCMWavFile;
import WavEmbed.WavEmbed;
import BMPEmbed.BMPEmbed;
import BMPEmbed.BMPFile;
import Embed.CarrierFile;
import Embed.Embed;
import Embed.NoFileInPayloadException;
import Embed.NoMessageInPayloadException;
import Embed.OnetimePadException;
import Embed.RecoveredPayload;
import JPGEmbed.JPEGEmbed;
import JPGEmbed.JPEGFile;

public abstract class UnembedCommandFactory {
	
	public static void clickEmbedFile() {
		EmbedWindow window = new EmbedWindow();
		window.setUndecorated(true);
		window.setVisible(true);
	}
	
	public static boolean clickOnUnembed(String filename, String password, UnembedWindow comp) throws Exception {
		
		
		int lastDot = filename.lastIndexOf(".");
		String suffix = filename.substring(lastDot);
		
		Embed embed = null;
		CarrierFile carrier = null;
		
		if(suffix.equalsIgnoreCase(".wav")) {
			embed = new WavEmbed();
			carrier = new PCMWavFile(filename);
		}
		else if(suffix.equalsIgnoreCase(".bmp")) 
		{
			embed = new BMPEmbed();
			carrier = new BMPFile(filename);
		}
		else if(suffix.equalsIgnoreCase(".png")) {
			embed = new PNGEmbed();
			carrier = new PNGFile(filename);
		}
		else if(suffix.equalsIgnoreCase(".jpg") || suffix.equalsIgnoreCase("jpeg")) {
			embed = new JPEGEmbed();
			carrier = new JPEGFile(filename);
		}
		
		RecoveredPayload rePayload = null;
		try {
			rePayload = embed.unembedPayload(carrier, password, null);
		} catch (OnetimePadException e) {
			File externalPad = getPad(comp);
			try {
				JOptionPane.showMessageDialog(comp, e.getMessage());
				rePayload = embed.unembedPayload(carrier, password, externalPad);
			} catch (Exception e1) {
				e1.printStackTrace();
				return false;
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if(rePayload.containsFile()) {
			JOptionPane.showMessageDialog(comp, "There is a file in the payload. You must save it!");
			JFileChooser chooser = new JFileChooser(rePayload.getDefaultFilename());
			chooser.setSelectedFile(new File(rePayload.getDefaultFilename()));
			int result = chooser.showSaveDialog(null);
			chooser.setVisible(true);
			if(result == chooser.CANCEL_OPTION) {
				return false;
			}
			try {
				rePayload.saveFileAs(chooser.getSelectedFile().getAbsolutePath());
			} catch (IOException e) {
				return false;
			}
			
		}
		else {
			comp.messageTextArea.setText(rePayload.retrieveOriginalTextMessage());
		}
		return true;
	}
	
	private static File getPad(Component comp)  {
		JOptionPane.showMessageDialog(comp, "This file requires an external one-time pad. Please select from the file selector.");
		JFileChooser chooser = new JFileChooser();
		chooser.showOpenDialog(null);
		chooser.setVisible(true);
		return chooser.getSelectedFile();
	}
}
