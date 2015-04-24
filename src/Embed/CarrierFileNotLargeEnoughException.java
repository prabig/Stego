package Embed;

public class CarrierFileNotLargeEnoughException extends Exception {
	public CarrierFileNotLargeEnoughException() {
		super("The carrier file you are trying to use is to small to contain " 
			  + "the payload you are trying to embed. Please select another file.");
				
	}
}
