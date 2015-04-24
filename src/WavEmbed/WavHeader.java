package WavEmbed;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Arrays;

public class WavHeader {
	protected int chunkSize, waveFormat, subchunk1ID, subchunk1Size,
			audioFormat, numChannels, sampleRate, byteRate, blockAlign,
			bitsPerSample, subchunk2ID, subchunk2Size;
	protected long headerSize;

	private FileInputStream wavFile;
	

	public WavHeader(FileInputStream wavFile) throws Exception {
		this.wavFile = wavFile;

		long currentPosition = wavFile.getChannel().position();

		wavFile.getChannel().position(0); // start from beginning
		RIFF(); // check for RIFF tag
		CHUNKSIZE();
		WAVE();
		SUBCHUNK1ID();
		SUBCHUNK1SIZE();
		AUDIOFORMAT();
		NUMCHANNELS();
		SAMPLERATE();
		BYTERATE();
		BLOCKALIGN();
		BITSPERSAMPLE();
		SUBCHUNK2ID();
		SUBCHUNK2SIZE();
		headerSize = currentPosition();
	}
	
	public byte[] getHeaderBytes() throws IOException {
		byte[] tmp = new byte[(int) headerSize];
		long curPosition = currentPosition();
		currentPosition(0);
		wavFile.read(tmp);
		currentPosition(curPosition);
		return tmp;
	}

	private void RIFF() throws IOException, Exception {
		// read 4 bytes
		byte[] tmp = new byte[4];
		wavFile.read(tmp);
		bigEndian(tmp);
		if (!"RIFF".equals(new String(tmp))) {
			throw new Exception("NOT A VALID WAVE FILE!");
		}
	}

	private void CHUNKSIZE() throws IOException {
		byte[] tmp = new byte[4];
		wavFile.read(tmp);
		littleEndian(tmp);
		chunkSize = ByteBuffer.wrap(tmp).getInt();
	}

	private void WAVE() throws Exception {
		int b;
		byte[] tmp = new byte[4];

		while ((b = wavFile.read()) != -1) {
			if ((char) b == 'W') {
				long curPosition = currentPosition();
				currentPosition(curPosition - 1);
				wavFile.read(tmp);
				if ("WAVE".equals(new String(bigEndian(tmp)))) {
					break;
				} else {
					currentPosition(curPosition);
				}
			}
		}

		if (b == -1) {
			throw new Exception("NOT A VALID WAV FILE!");
		} else {
			waveFormat = ByteBuffer.wrap(bigEndian(tmp)).getInt();
		}
	}

	private void SUBCHUNK1ID() throws Exception {
		int b;
		byte[] tmp = new byte[4];
		while ((b = wavFile.read()) != -1) {
			if ((char) b == 'f') { // "f"
				long curPosition = currentPosition(); // preserve in case of
														// false positive
				currentPosition(curPosition - 1);
				wavFile.read(tmp);
				if ("fmt ".equals(new String(tmp))) {
					subchunk1ID = ByteBuffer.wrap(tmp).getShort();
					break;
				} else {
					currentPosition(curPosition); // rewind on false positive
				}
			}
		}
		if (b == -1) {
			throw new Exception("NOT A VALID WAV FILE!");
		}
	}

	private void SUBCHUNK1SIZE() throws IOException {
		byte[] tmp = new byte[4];
		wavFile.read(tmp);
		littleEndian(tmp);
		subchunk1Size = ByteBuffer.wrap(tmp).getInt();
	}

	private void AUDIOFORMAT() throws Exception {
		byte[] tmp = new byte[2];
		wavFile.read(tmp);
		littleEndian(tmp);
		audioFormat = (int) ByteBuffer.wrap(tmp).getShort();

		if (audioFormat != 1) {
			throw new Exception("ERROR: THIS IS NOT A PCM WAV FILE!");
		}
	}

	private void NUMCHANNELS() throws IOException {
		byte[] tmp = new byte[2];
		wavFile.read(tmp);
		littleEndian(tmp);
		numChannels = (int) ByteBuffer.wrap(tmp).getShort();
	}

	private void SAMPLERATE() throws IOException {
		byte[] tmp = new byte[4];
		wavFile.read(tmp);
		littleEndian(tmp);
		sampleRate = ByteBuffer.wrap(tmp).getInt();
	}

	private void BYTERATE() throws IOException {
		byte[] tmp = new byte[4];
		wavFile.read(tmp);
		littleEndian(tmp);
		byteRate = ByteBuffer.wrap(tmp).getInt();
	}

	private void BLOCKALIGN() throws IOException {
		byte[] tmp = new byte[2];
		wavFile.read(tmp);
		littleEndian(tmp);
		blockAlign = (int) ByteBuffer.wrap(tmp).getShort();
	}

	private void BITSPERSAMPLE() throws IOException {
		byte[] tmp = new byte[2];
		wavFile.read(tmp);
		littleEndian(tmp);
		bitsPerSample = (int) ByteBuffer.wrap(tmp).getShort();
	}

	private void SUBCHUNK2ID() throws Exception {
		int b;
		byte[] tmp = new byte[4];
		while ((b = wavFile.read()) != -1) {
			if ((char) b == 'd') {
				long curPosition = currentPosition();
				currentPosition(curPosition - 1);
				wavFile.read(tmp);
				if ("data".equals(new String(tmp))) {
					break;
				} else {
					currentPosition(curPosition);
				}
			}
		}

		if (b == -1) {
			throw new Exception("ERROR: NOT A VALID WAV FILE!");
		} else {
			subchunk2ID = ByteBuffer.wrap(bigEndian(tmp)).getInt();
		}
	}

	private void SUBCHUNK2SIZE() throws IOException {
		byte[] tmp = new byte[4];
		wavFile.read(tmp);
		littleEndian(tmp);
		subchunk2Size = ByteBuffer.wrap(tmp).getInt();
	}

	private byte[] bigEndian(byte[] data) {
		return data;
	}

	private byte[] littleEndian(byte[] data) {
		int i = 0, j = data.length - 1;
		while (i < j) {
			if (data[i] != data[j]) {
				data[i] ^= data[j];
				data[j] ^= data[i];
				data[i] ^= data[j];
			}
			i++;
			j--;
		}
		return data;
	}

	private long currentPosition() throws IOException {
		return (int) wavFile.getChannel().position();
	}

	private void currentPosition(long p) throws IOException {
		wavFile.getChannel().position(p);
	}
}
