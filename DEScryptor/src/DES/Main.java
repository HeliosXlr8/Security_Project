package DES;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

public class Main {

	public static void main(String[] args) {
		String key = "testkeystring";
		DES des = new DES(getSecretKey(key), Cipher.DECRYPT_MODE);
	}
	
	private static SecretKey getSecretKey(String secretPassword) {
		  
		SecretKey key = null;
		try {
			  DESKeySpec keySpec = new DESKeySpec(secretPassword.getBytes("UTF8"));
			  SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
			  key = keyFactory.generateSecret(keySpec);
			  System.out.println();
		} catch (Exception e) {
			  e.printStackTrace();
			  System.out.println("Error in generating the secret Key");
		}
		return key;
	}
	

}
