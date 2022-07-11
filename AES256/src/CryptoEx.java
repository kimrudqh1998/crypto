import java.nio.ByteBuffer;
import java.security.AlgorithmParameters;
import java.security.SecureRandom;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

public class CryptoEx {
	static String key = "PBKDF2WithHmacSHA1";
	
	//암호화
	public static String encodedText(String originalTXT, String Key) throws Exception{
		 SecureRandom random = new SecureRandom();
		 byte bytes[] = new byte[20];
		 random.nextBytes(bytes);
		 byte[] Bytes = bytes;
		 
		 SecretKeyFactory fac = SecretKeyFactory.getInstance(Key);
		 
		 PBEKeySpec spec = new PBEKeySpec(key.toCharArray(),Bytes,70000,256);
		 
		 SecretKey scKey = fac.generateSecret(spec);
		 SecretKeySpec scSpec = new SecretKeySpec(scKey.getEncoded(),"AES");
		 
		 Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
		 cipher.init(Cipher.ENCRYPT_MODE, scSpec);
		 AlgorithmParameters params = cipher.getParameters();
		 
		 byte [] ivBytes =  params.getParameterSpec(IvParameterSpec.class).getIV();
		 byte [] encryptedTextBytes = cipher.doFinal(originalTXT.getBytes("UTF-8"));
		 byte [] buffer = new byte[bytes.length + ivBytes.length + encryptedTextBytes.length];
		 System.arraycopy(bytes, 0, buffer, 0, bytes.length);
		 System.arraycopy(ivBytes, 0, buffer, bytes.length, ivBytes.length);
		 System.arraycopy(encryptedTextBytes, 0, buffer, bytes.length + ivBytes.length,encryptedTextBytes.length);
		
		return Base64.getEncoder().encodeToString(buffer);
	}
	//복호화
	public static String decodedText(String encTXT, String Key) throws Exception {
		 Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
		 ByteBuffer buffer = ByteBuffer.wrap(Base64.getDecoder().decode(encTXT));
		 
		byte[] saltBytes = new byte[20];
		buffer.get(saltBytes, 0, saltBytes.length);
		byte[] ivBytes = new byte[cipher.getBlockSize()];
		buffer.get(ivBytes, 0, ivBytes.length);
		byte[] encryptedTextBytes = new byte[buffer.capacity() - saltBytes.length - ivBytes.length];
		buffer.get(encryptedTextBytes);
		
		SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
		PBEKeySpec spec = new PBEKeySpec(key.toCharArray(), saltBytes, 70000, 256);
	    SecretKey secretKey = factory.generateSecret(spec);
	    SecretKeySpec secret = new SecretKeySpec(secretKey.getEncoded(), "AES");
	    cipher.init(Cipher.DECRYPT_MODE, secret, new IvParameterSpec(ivBytes));
	    byte[] decryptedTextBytes = cipher.doFinal(encryptedTextBytes);

	    return new String(decryptedTextBytes);
	}
}
