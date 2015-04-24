package Embed;

public class InvalidEncryptionSchemeException extends Exception {

	public InvalidEncryptionSchemeException() {
		super("That is not a valid encryption scheme!");
	}
}
