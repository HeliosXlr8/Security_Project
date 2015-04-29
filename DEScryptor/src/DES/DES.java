package DES;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.swing.*;

public class DES extends JFrame{
	
	public DES(SecretKey key, int mode){

		System.out.println("Entering EncFile()");
		
		ShowFileDialog fc = new ShowFileDialog();
		String file = fc.getFile();
		String strOutputName;
		
		if(file != null){
			if(mode == Cipher.ENCRYPT_MODE){
				strOutputName = file + "enc";
			}else{
				strOutputName = file.substring(0,file.length()-3);
			}
			
			FileInputStream fis;
			FileOutputStream fos;
			
			System.out.println(file);
			System.out.println(strOutputName);
			
			try {
				fis = new FileInputStream(file);
				fos = new FileOutputStream(strOutputName);
				encryptOrDecrypt(key, mode, fis, fos);
				fos.close();
				fis.close();
				
				if(mode == Cipher.ENCRYPT_MODE){
					System.out.println(file + " encrypted and saved to " + strOutputName);
				}else{
					System.out.println(file + " decrypted and saved to " + strOutputName);
				}
			
			} catch (FileNotFoundException e) {
				System.out.println("File not found.");
			} catch (Throwable e){
				System.out.println("Error");
			}
		}else{
			
		}
	}
	
	
	public static void encryptOrDecrypt(SecretKey key, int mode, InputStream is, OutputStream os) throws Throwable {
		
		System.out.println("Starting encryption/decryption");
		
		DESKeySpec dks = new DESKeySpec(key.toString().getBytes("UTF-8"));
		SecretKeyFactory skf = SecretKeyFactory.getInstance("DES");
		SecretKey desKey = skf.generateSecret(dks);
		Cipher cipher = Cipher.getInstance("DES"); // DES/ECB/PKCS5Padding for SunJCE

		if (mode == Cipher.ENCRYPT_MODE) {
			cipher.init(Cipher.ENCRYPT_MODE, desKey);
			CipherInputStream cis = new CipherInputStream(is, cipher);
			doCopy(cis, os);
		} else if (mode == Cipher.DECRYPT_MODE) {
			cipher.init(Cipher.DECRYPT_MODE, desKey);
			CipherOutputStream cos = new CipherOutputStream(os, cipher);
			doCopy(is, cos);
		}
	}
	
	public static void doCopy(InputStream is, OutputStream os) throws IOException {
		
		System.out.println("Do copy");
		
		byte[] bytes = new byte[64];
		int numBytes;
		while ((numBytes = is.read(bytes)) != -1) {
			os.write(bytes, 0, numBytes);
		}
		os.flush();
		os.close();
		is.close();
		
		System.out.println("Do copy done");
		
	}

}