package Embed;

public class NoMessageInPayloadException extends Exception {
	public NoMessageInPayloadException() {
		super("There is no message contained in the payload!");
	}
}
