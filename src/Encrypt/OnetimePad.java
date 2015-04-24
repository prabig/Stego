package Encrypt;

import java.nio.ByteBuffer;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

public abstract class OnetimePad {
	
	public static void main(String[] args) {
		String str = "Patrick is kewl!";
		byte[] s = str.getBytes();
		System.out.println(new String(s));
		String padString = "98348273393993939";
		byte[] pad = padString.getBytes();
		encrypt(s, pad);
		System.out.println(new String(s));
		decrypt(s,pad);
		System.out.println(new String(s));
		
		encrypt(s,"");
		System.out.println(new String(s));
		decrypt(s,"");
		System.out.println(new String(s));
		
	}
	
	public static void encrypt(byte[] input, byte[] pad) {
		if(input.length <= pad.length) {
			for(int i=0; i<input.length; i++) {
				input[i] ^= pad[i];
			}
		}
	}
	
	public static void encrypt(byte[] input, String password) {
		try {
			MessageDigest md1 = MessageDigest.getInstance("SHA-256");
			MessageDigest md2 = MessageDigest.getInstance("MD5");
			
			String salt = "1290983829204848xmksl949288skjlkasd88jdjkh8";
			
			md1.update((salt+password).getBytes());
			md2.update((salt+password).getBytes());
			
			long[] randSeed = new long[6];
			ByteBuffer buffer = ByteBuffer.wrap(md1.digest());
			randSeed[1] = buffer.getLong(0);
			randSeed[3] = buffer.getLong(1);
			randSeed[5] = buffer.getLong(2);
			randSeed[0] = buffer.getLong(3);
			buffer = ByteBuffer.wrap(md2.digest());
			randSeed[2] = buffer.getLong(0);
			randSeed[4] = buffer.getLong(1);
			
			Random[] rands = new Random[6];
			
			for(int i=0; i<6; i++) {
				rands[i] = new Random(randSeed[i]);
			}
			
			for(int i=0; i<input.length; i++) {
				byte b = (byte)rands[i%6].nextInt();
				input[i] ^= b;
			}
			System.out.println();
			
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void decrypt(byte[] input, byte[] pad) {
		encrypt(input, pad);
	}
	
	public static void decrypt(byte[] input, String password) {
		encrypt(input, password);
	}	
}
