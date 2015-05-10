package RSA;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;

import sun.misc.BASE64Encoder;

public final class RSAKeyPair {
    
    private int keyLength;
    private PrivateKey privateKey;
    private PublicKey publicKey;
    private String publicKeyName = "//public.key";
    private String privateKeyName = "//private.key";
    
    public RSAKeyPair(int keyLength) {
        try
        {
        	this.keyLength = keyLength;
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
            keyPairGenerator.initialize(this.keyLength);
            KeyPair keyPair = keyPairGenerator.generateKeyPair();
            privateKey = keyPair.getPrivate();
            publicKey = keyPair.getPublic();
        }
    	catch (GeneralSecurityException gsEx)
        {
    		gsEx.printStackTrace();
        }
    }
    
    public final PrivateKey getPrivateKey() {
    		return privateKey;
    }
    
    public String getPrivateKeyStr()
    {
    	return new BASE64Encoder().encode(privateKey.getEncoded());
    }
    
    public final PublicKey getPublicKey() {
        return publicKey;
    }
    
    public String getPublicKeyStr()
    {
    	return new BASE64Encoder().encode(publicKey.getEncoded());
    }
    
    public final void toFileSystem(String privateKeyPathName, String publicKeyPathName)
            throws IOException {
        
        FileOutputStream privateKeyOutputStream = null;
        FileOutputStream publicKeyOutputStream = null;
        
        try {
        	
            File privateKeyFile = new File(privateKeyPathName);
            File publicKeyFile = new File(publicKeyPathName);
            
            if(!privateKeyFile.exists()){
            	privateKeyFile.mkdir();
            }            
            if(!publicKeyFile.exists()){
            	publicKeyFile.mkdir();
            }

            privateKeyOutputStream = new FileOutputStream(privateKeyFile + privateKeyName);
            privateKeyOutputStream.write(privateKey.getEncoded());

            publicKeyOutputStream = new FileOutputStream(publicKeyFile + publicKeyName);
            publicKeyOutputStream.write(publicKey.getEncoded());
            
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
