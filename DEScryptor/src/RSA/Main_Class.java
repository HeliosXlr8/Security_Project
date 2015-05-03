package RSA;

public class Main_Class {

	public static void main(String[] args) {
		EncryptDecryptFile en = new EncryptDecryptFile();
		
		/*
		 * en.encrypteAndDecrypt() kan een parameter bevatten met de locationstring naar de textfile
		 * LET OF!!! Deze file mag niet meer zijn dan 245 bytes anders gooit die een exception
		 * De standaard File kan je veranderen in EncryptDecryptFile.java, dit staat 
		 * ingesteld naar een file op mijn PC, dus als je het programma nu runt krijg je een 
		 * filechooser zolang je een valid file selecteerd (any file < 245bytes)
		 */
		en.encryptAndDecrypt();
	}
}
