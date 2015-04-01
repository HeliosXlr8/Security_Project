package cipher;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class HashGenerator {

	public enum Hash {
	    MD5, SHA1, SHA256
	}
	
	public enum CSet {
		UTF8, UTF16
	}
	
	private String hashType;
	private String characterSet;
	
	public HashGenerator () {
		this(Hash.SHA256, CSet.UTF8);
	}
	
	public HashGenerator (Hash hashType) {
		this(hashType, CSet.UTF8);
	}
	
	public HashGenerator (CSet characterSet) {
		this(Hash.SHA256, characterSet);
	}
	
	public HashGenerator (Hash hashType, CSet characterSet) {
		switch (hashType) {
		case MD5:
			this.hashType = "MD5";
			break;
		case SHA1:
			this.hashType = "SHA-1";
			break;
		case SHA256:
			this.hashType = "SHA-256";
			break;
		}
		switch (characterSet) {
		case UTF8:
			this.characterSet = "UTF-8";
			break;
		case UTF16:
			this.characterSet = "UTF-16";
			break;
		}
	}
	
	public String stringHash(String text) {
		byte[] byteHash = null;
		BigInteger bi = null;
		try {
			MessageDigest md = MessageDigest.getInstance(hashType);
			md.update(text.getBytes(characterSet));
			byteHash = md.digest();
			bi = new BigInteger(1, byteHash);
		}
		catch (UnsupportedEncodingException ex) {
			ex.printStackTrace();
		}
		catch (NoSuchAlgorithmException ex) {
			ex.printStackTrace();
		}
		return bi.toString(16);
	}
	
	public String fileHash(File file) throws FileNotFoundException, IOException {
		MessageDigest md = null;
		try {
			md = MessageDigest.getInstance(hashType);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		 
		InputStream input= new FileInputStream(file);
	 
		byte[] buffer = new byte[8192];
        int read=0;
 
        while( (read = input.read(buffer)) > 0)
                md.update(buffer, 0, read);
 
        byte[] byteHash = md.digest();
        BigInteger bi = new BigInteger(1, byteHash);
 
        input.close();
        return bi.toString(16);
	}
	
}
