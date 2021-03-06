package DES;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.nio.charset.Charset;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class DESencrypter implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
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
		byte[] utf8 = str.getBytes("UTF8");

		// Encrypt
		byte[] enc = ecipher.doFinal(utf8);

		// Encode bytes to base64 to get a string
		return new String(Base64.getEncoder().encode(enc));
	}

	public String decrypt(String str) throws Exception
	{
		// Decode base64 to get bytes
		byte[] dec = Base64.getDecoder().decode(str);

		byte[] utf8 = dcipher.doFinal(dec);

		// Decode using utf-8
		return new String(utf8, Charset.forName("UTF8"));
	}
	
	public SecretKey getKey()
	{
		return key;
	}
	
	public String getKeyStr()
	{	
		return new String(Base64.getEncoder().encode(key.getEncoded()));
	}
	
	public void setKeyStr(String key){
		// decode the base64 encoded string
		byte[] decodedKey = Base64.getDecoder().decode(key);
		// rebuild key using SecretKeySpec
		SecretKey originalKey = new SecretKeySpec(decodedKey, 0, decodedKey.length, "DES");
		this.key = originalKey;
		try {
			ecipher.init(Cipher.ENCRYPT_MODE, this.key);
			dcipher.init(Cipher.DECRYPT_MODE, this.key);
		} catch (InvalidKeyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
    public void setKey(SecretKey key) {
		this.key = key;
		try {
			ecipher.init(Cipher.ENCRYPT_MODE, this.key);
			dcipher.init(Cipher.DECRYPT_MODE, this.key);
		} catch (InvalidKeyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public final void toFileSystem(SecretKey key)
            throws IOException {
        
        FileOutputStream privateKeyOutputStream = null;
        FileOutputStream publicKeyOutputStream = null;
        
        try {
        	/*
            File privateKeyFile = new File(privateKeyPathName);
            File publicKeyFile = new File(publicKeyPathName);
            
            if(!privateKeyFile.exists()){
            	privateKeyFile.mkdir();
            }            
            if(!publicKeyFile.exists()){
            	publicKeyFile.mkdir();
            }
			*/
        	
            privateKeyOutputStream = new FileOutputStream("bin/DESKey.key" /*privateKeyFile + privateKeyName*/);
            privateKeyOutputStream.write(key.getEncoded());
            
        } catch(IOException ioException) {
            throw ioException;
        } finally {
        
            try {
                
                if (privateKeyOutputStream != null) {
                    privateKeyOutputStream.close();
                }
                if (publicKeyOutputStream != null) {
                    publicKeyOutputStream.close();
                }   
                
            } catch(IOException ioException) {
                throw ioException;
            }
        }
    }
}