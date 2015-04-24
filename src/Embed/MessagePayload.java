package Embed;

public class MessagePayload extends Payload {
	private int 				compression = NO_COMPRESSION;
	
	public static final int		NO_COMPRESSION = 0, 
								FIXED_HUFFMAN = 1<<29, 
								DYNAMIC_HUFFMAN = 2<<29, 
								LZW = 3<<29;

	public MessagePayload(String message, String password, int encryption, int compression, byte[] onetimePad)
			throws NoOnetimePadProvidedException,
			InvalidEncryptionSchemeException, DataTooLongException, InvalidCompressionSchemeException {
		
		super(message.getBytes(), password, encryption, onetimePad);
		this.compression = compression;
		//compressData();
		encryptData();
		makeHeader();
		createPasswordHash();
		embedHeaderHashAndDataIntoByteStream();
	}

	private void makeHeader() throws NoOnetimePadProvidedException, InvalidEncryptionSchemeException, DataTooLongException, InvalidCompressionSchemeException {
		super.makeHeader(MESSAGE);
		switch(compression) {
		case NO_COMPRESSION:
		case FIXED_HUFFMAN:
		case DYNAMIC_HUFFMAN:
		case LZW:
			payloadHeader |= compression;
			break;
		default:
			throw new InvalidCompressionSchemeException();
		}
		payloadHeader |= compression;
	}
	
	private void compressData() throws InvalidCompressionSchemeException {
		switch(compression) {
		case NO_COMPRESSION:
			return;
		case FIXED_HUFFMAN:
			//data = huffman, overwrite this.data
			break;
		case DYNAMIC_HUFFMAN:
			//overwrite this.data
			break;
		case LZW:
			//overwrite this.data
			break;
		default:
			throw new InvalidCompressionSchemeException();
		}
	}
}
