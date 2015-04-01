package TestProject;

import java.io.File;

import project.ByteConverter;
import project.RSACipher;
import project.RSAKeyPair;

import org.junit.Assert;
import org.junit.Test;

public class TestRSACipher{

    private final String privateKeyPathName = "C://temp//private.key";
    private final String publicKeyPathName = "C://temp//public.key";
    private final String transformation = "RSA/ECB/PKCS1Padding";
    private final String encoding = "UTF-8";
    
    private byte[] byteArray = null;
    
    @Test
    public void testEncryptDecryptWithKeyPairFiles()
            throws Exception {
    
        try {
     
        	ByteConverter byteconverter = new ByteConverter(new File("D:\\Maurice\\Bureaublad\\random.txt"));
        	byteArray = byteconverter.getByteArray();
        	String byteString = new String(byteArray, "UTF-8"); // for UTF-8 encoding
        	
            RSAKeyPair rsaKeyPair = new RSAKeyPair(2048);
            rsaKeyPair.toFileSystem(privateKeyPathName, publicKeyPathName);

            RSACipher rsaCipher = new RSACipher();
            String encrypted = rsaCipher.encrypt(byteString, publicKeyPathName, transformation, encoding);
            String decrypted = rsaCipher.decrypt(encrypted, privateKeyPathName, transformation, encoding);
            System.out.println("Encrypted: \n" + encrypted  + " \n------------\nend of encrpytion\n---------");
            System.out.println("");
            System.out.println("ByteString " + byteString  + " \n------------\nend of bytestring\n---------");
            System.out.println("");
            System.out.println("Decrypted: " + decrypted + " \n------------\nend of decryption\n---------");
            Assert.assertEquals(decrypted, byteString);   
            
        } catch(Exception exception) {
            Assert.fail("The test failed because: " + exception.getMessage());
        }
    }
}
