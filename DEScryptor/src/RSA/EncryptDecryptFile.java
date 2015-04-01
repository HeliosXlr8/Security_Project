package RSA;

import java.io.File;
import java.io.FileNotFoundException;

public class EncryptDecryptFile {

	private final String privateKeyPathName = "C://Temp///";
	private final String publicKeyPathName = "C://Temp///";
	private final String transformation = "RSA/ECB/PKCS1Padding";
	private final String encoding = "UTF-8";
	private String fileString = "D:\\Users\\Maurice\\Desktop\\shit2.txt";

	private byte[] byteArray = null;

	public EncryptDecryptFile() {

	}
	
	public EncryptDecryptFile(String fileString) {
		this.fileString = fileString;
	}

	public void encryptAndDecrypt() {
		try {
			
			File file = new File(fileString);
			
			while(file == null || !file.exists()){
				ShowFileDialog dg = new ShowFileDialog();
				fileString = dg.getFile();
				file = new File(fileString);
			}

			ByteConverter byteconverter = new ByteConverter(file);
			byteArray = byteconverter.getByteArray();
			String byteString = new String(byteArray, "UTF-8"); // for UTF-8
																// encoding

			RSAKeyPair rsaKeyPair = new RSAKeyPair(2048);
			rsaKeyPair.toFileSystem(privateKeyPathName, publicKeyPathName);

			RSACipher rsaCipher = new RSACipher();
			String encrypted = rsaCipher.encrypt(byteString, publicKeyPathName,
					transformation, encoding);
			String decrypted = rsaCipher.decrypt(encrypted, privateKeyPathName,
					transformation, encoding);
			System.out.println("Encrypted: \n" + encrypted
					+ " \n------------\nend of encrpytion\n---------");
			System.out.println("");
			System.out.println("ByteString \n" + byteString
					+ " \n------------\nend of bytestring\n---------");
			System.out.println("");
			System.out.println("Decrypted: \n" + decrypted
					+ " \n------------\nend of decryption\n---------");

		} catch(FileNotFoundException exception){
			exception.printStackTrace();
			
		} catch (Exception exception) {
			exception.printStackTrace();
		}
	}
}
