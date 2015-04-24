package Embed;

public class NoFileInPayloadException extends Exception {
	public NoFileInPayloadException() {
		super("This payloa does NOT contain a file!");
	}
}
