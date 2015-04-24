package Embed;

import java.io.File;

import WavEmbed.PCMWavFile;

public abstract class Embed {
	public abstract boolean embedPayload(Payload payload, CarrierFile carrier, String outputFileName) throws Exception;
	public abstract RecoveredPayload unembedPayload(CarrierFile file, String password, File externalPad) throws Exception;
	
	public static int getPasswordHashLength(int header) {
		return ((header&0x10000000)>0)?128:0;
	}
}
