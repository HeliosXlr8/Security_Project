package RSA;

//import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.IOUtils;

import java.io.FileInputStream;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.security.KeyFactory;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import javax.crypto.Cipher;
import java.util.Base64;

public class RSACipher {
	
	private String publicKeyName = "//public.key";
	private String privateKeyName = "//private.key";

    public String encrypt(String rawText, String publicKeyPath, String transformation, String encoding)
            throws IOException, GeneralSecurityException {

        X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(IOUtils.toByteArray(new FileInputStream(publicKeyPath /* + publicKeyName*/)));

        Cipher cipher = Cipher.getInstance(transformation);
        cipher.init(Cipher.ENCRYPT_MODE, KeyFactory.getInstance("RSA").generatePublic(x509EncodedKeySpec));

        return Base64.getEncoder().encodeToString((cipher.doFinal(rawText.getBytes(encoding))));
    }
    
    public String encryptWPrivate(String rawText, String publicKeyPath, String transformation, String encoding)
            throws IOException, GeneralSecurityException {

        PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(IOUtils.toByteArray(new FileInputStream(publicKeyPath /* + privateKeyName*/)));

        Cipher cipher = Cipher.getInstance(transformation);
        cipher.init(Cipher.ENCRYPT_MODE, KeyFactory.getInstance("RSA").generatePrivate(pkcs8EncodedKeySpec));

        return new String(cipher.doFinal(Base64.getDecoder().decode(rawText)), encoding);
    }

    public String decrypt(String cipherText, String privateKeyPath, String transformation, String encoding)
            throws IOException, GeneralSecurityException {

        PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(IOUtils.toByteArray(new FileInputStream(privateKeyPath /* + privateKeyName*/)));

        Cipher cipher = Cipher.getInstance(transformation);
        cipher.init(Cipher.DECRYPT_MODE, KeyFactory.getInstance("RSA").generatePrivate(pkcs8EncodedKeySpec));

        return new String(cipher.doFinal(Base64.getDecoder().decode(cipherText)), encoding);
    }
    
    public String decryptWPublic(String cipherText, String privateKeyPath, String transformation, String encoding)
            throws IOException, GeneralSecurityException {

        X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(IOUtils.toByteArray(new FileInputStream(privateKeyPath /* + publicKeyName*/)));

        Cipher cipher = Cipher.getInstance(transformation);
        cipher.init(Cipher.DECRYPT_MODE, KeyFactory.getInstance("RSA").generatePublic(x509EncodedKeySpec));

        return Base64.getEncoder().encodeToString(cipher.doFinal(cipherText.getBytes(encoding)));
    }
}
