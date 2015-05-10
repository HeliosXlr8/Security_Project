package DES;

import java.nio.charset.Charset;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

public class DESencrypter
{
	private SecretKey key;
	private Cipher ecipher;
	private Cipher dcipher;

	public DESencrypter()
	{
		try
		{
			key = KeyGenerator.getInstance("DES").generateKey();
			ecipher = Cipher.getInstance("DES");
			dcipher = Cipher.getInstance("DES");
			ecipher.init(Cipher.ENCRYPT_MODE, key);
			dcipher.init(Cipher.DECRYPT_MODE, key);
		}
		catch (NoSuchAlgorithmException nsaEx)
		{
			nsaEx.printStackTrace();
		}
		catch (NoSuchPaddingException nspEx)
		{
			nspEx.printStackTrace();
		}
		catch (InvalidKeyException ikEx)
		{
			ikEx.printStackTrace();
		}
	}

	public String encrypt(String str) throws Exception
	{
		// Encode the string into bytes using utf-8
		byte[] utf8 = str.getBytes("UTF-8");

		// Encrypt
		byte[] enc = ecipher.doFinal(utf8);

		// Encode bytes to base64 to get a string
		return new BASE64Encoder().encode(enc);
	}

	public String decrypt(String str) throws Exception
	{
		// Decode base64 to get bytes
		byte[] dec = new BASE64Decoder().decodeBuffer(str);

		byte[] utf8 = dcipher.doFinal(dec);

		// Decode using utf-8
		return new String(utf8, Charset.forName("UTF-8"));
	}
	
	public SecretKey getKey()
	{
		return key;
	}
	
	public String getKeyStr()
	{	
		return new BASE64Encoder().encode(key.getEncoded());
	}
}