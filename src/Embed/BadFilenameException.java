package Embed;

public class BadFilenameException extends Exception {
	public BadFilenameException() {
		super("Only .wav, .png, and .bmp currently supported.");
	}
}
