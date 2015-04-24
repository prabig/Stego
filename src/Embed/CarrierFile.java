package Embed;

import java.io.IOException;

public abstract class CarrierFile {
	
	public abstract int getNext(byte[] b) throws IOException;
	public abstract byte[] getRemainingBytes() throws Exception;
}
