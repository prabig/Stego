package Embed;

public enum Types {
	//File/message
	File	(1<<31, 1),
	Message (0, 0),
	//compression
	None (0,0),
	FixedHuffman (1<<29,1),
	StatisticalHuffman (2<<29, 2),
	LZW (3<<29,3),
	//encryption
	Plaintext (0, 0),
	ExternalOnetimePad (1<<26, 1),
	InternalPadding (2<<26, 2);
	
	
	
	public final int payloadConstant;
	public final int unembedConstant;
	Types(int payloadConstant, int unembedConstant) {
		this.payloadConstant = payloadConstant;
		this.unembedConstant = unembedConstant;
	}
	
}
