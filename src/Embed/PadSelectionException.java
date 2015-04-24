package Embed;

public class PadSelectionException extends Exception {
	public PadSelectionException() {
		super("You must choose a file as your pad in order to use External One Time Pad encryption!");
	}
}
