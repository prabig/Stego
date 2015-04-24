package Embed;

public class IncorrectPasswordException extends Exception {
	
	public IncorrectPasswordException() {
		super("Incorrect password entered. Please try another password!");
	}
}
