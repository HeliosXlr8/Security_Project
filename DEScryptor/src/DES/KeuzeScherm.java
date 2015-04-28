package DES;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
import javax.swing.border.*;

public class KeuzeScherm extends JFrame{

	private JLabel lblInfo = new JLabel();
	private JTextArea txtName = new JTextArea(1,30);
	
	public KeuzeScherm(){
		
		Init();
		
	}
	
	public void Init(){
		
		setTitle("Patiënten");
		setSize(500,200);
		setBackground(Color.gray);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		
		Theme();
		
		JPanel panel = new JPanel();
		JPanel panButtons = new JPanel();
		JPanel panName = new JPanel();
		JPanel panInfo = new JPanel();
		
		panel.setLayout(new GridLayout(3,1));
		panel.setBorder(new EmptyBorder(10, 10, 10, 10));
		
		this.add(panel);
		
		//Buttons
		panButtons.setLayout(new FlowLayout());
		
		JButton Encrypt = new JButton("Encrypt file");
		JButton Decrypt = new JButton("Decrypt file");
		
		ButtonHandler handler = new ButtonHandler();
		
		Encrypt.addActionListener(handler);
		Decrypt.addActionListener(handler);
		
		panButtons.add(Encrypt);
		panButtons.add(Decrypt);
		
		//filename textarea
		Border border = BorderFactory.createLoweredBevelBorder();
		txtName.setBorder(border);
		txtName.setSize(350, 20);
		txtName.setVisible(true);
		JLabel lblName = new JLabel("Output filepath and name (optional): ");
		
		panName.add(lblName);
		panName.add(txtName);
		
		panInfo.add(lblInfo);
		
		panel.add(panButtons);
		panel.add(panName);
		panel.add(panInfo);
		
		panel.setVisible(true);
	}
	
	private void Theme(){
		
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} 
		catch (UnsupportedLookAndFeelException e) {
			// handle exception
		}
		catch (ClassNotFoundException e) {
			// handle exception
		}
		catch (InstantiationException e) {
			// handle exception
		}
		catch (IllegalAccessException e) {
			// handle exception
		}
		
	}
	
	public void CipherFile(String key, int mode){

		System.out.println("Entering EncFile()");
		lblInfo.setText("Select file for encryption.");
		
		ShowFileDialog fc = new ShowFileDialog();
		String file = fc.getFile();
		String strOutputName = null;
		
		System.out.println(file);
		
		if(file != null){
			
			//check for filename
			if (txtName.getText().replace(" ", "") != ""){
				strOutputName = txtName.getText();
			}else{
				if(mode == Cipher.ENCRYPT_MODE){
					strOutputName = file.substring(0, file.length()-5) + "-encr";
				}else{
					strOutputName = file.substring(0, file.length()-5) + "-d";
				}
			}

			lblInfo.setText("File selected. Encrypting...");
			
			FileInputStream fis;
			FileOutputStream fos;
			
			try {
				System.out.println(file);
				System.out.println(strOutputName);
				fis = new FileInputStream(file);
				fos = new FileOutputStream(strOutputName);
				encryptOrDecrypt(key, mode, fis, fos);
				fos.close();
				fis.close();
				
				if(mode == Cipher.ENCRYPT_MODE){
					System.out.println(file + " encrypted and saved to " + strOutputName);
					lblInfo.setText(file + " encrypted and saved to " + strOutputName);
				}else{
					System.out.println(file + " decrypted and saved to " + strOutputName);
					lblInfo.setText(file + " decrypted and saved to " + strOutputName);
				}
			} catch (FileNotFoundException e) {
				System.out.println("File not found.");
				lblInfo.setText("File not found");
			} catch (Throwable e){
				System.out.println("Error");
				lblInfo.setText("An unanticipated error has occurred");
			}
		}else{
			lblInfo.setText("");
		}
	}
	
	
	public static void encryptOrDecrypt(String key, int mode, InputStream is, OutputStream os) throws Throwable {
		
		System.out.println("Starting encryption/decryption");
		
		DESKeySpec dks = new DESKeySpec(key.getBytes());
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

	//event handlers
	private class ButtonHandler implements ActionListener{
		
		@Override
		public void actionPerformed(ActionEvent event) {
			
			String key = "squirrel123"; // needs to be at least 8 characters for DES
			
			switch (event.getActionCommand()){
				case "Encrypt file" : 
					CipherFile(key, Cipher.ENCRYPT_MODE);
					break;
				case "Decrypt file" :
					CipherFile(key, Cipher.DECRYPT_MODE);
					break;
			}
			
		}
		
	}
	
	

}