package Embed;

public final class InvalidCompressionSchemeException extends Exception {
	private static final long serialVersionUID = 1L;

	public InvalidCompressionSchemeException() {
		super("This is not a valid compression scheme!");
	}
}
