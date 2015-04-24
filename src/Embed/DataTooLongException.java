package Embed;

public final class DataTooLongException extends Exception {
	
	public DataTooLongException(){
		super("File is bigger than 64 MB!");
	}
}
