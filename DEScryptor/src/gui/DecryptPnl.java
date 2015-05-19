package gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.security.GeneralSecurityException;

import javax.crypto.SecretKey;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.apache.commons.io.IOUtils;

import cipher.HashGenerator;
import DES.DESencrypter;
import RSA.RSACipher;
import main.ResLoader;
import net.miginfocom.swing.MigLayout;

public class DecryptPnl extends JPanel
{
	/**
	 * JPanel that contains the elements for the decrypting tab
	 */
	private static final long serialVersionUID = -1665345338497275072L;
	
	private MainWindow parent;
	
	private JPanel infoPnl;
	private JPanel messagePnl;
	private JPanel verificationPnl;
	private JPanel frameBtnPnl;
	
	public JTextField myPrivateKeyField;
	public JTextField senderPublicKeyField;
	private JTextField messagePathField;
	private JTextField messageKeyPathField;
	
	private JButton openPtKBtn;	// "open my private key" button
	private JButton openPcKBtn;	// "open sender's public key" button
	private JButton getFromKeypairBtn;
	private JButton openHashFileBtn;
	private JButton chooseMessagePathBtn;
	private JButton openAsTextBtn;
	private JButton saveAsBtn;
	
	private String hash = null;
	private String originalMessage = "";
	
	private final String privateKeyPathName = "bin/private.key";
	private final String publicKeyPathName = "bin/public.key";
	private final String transformation = "RSA/ECB/PKCS1Padding";
	private final String encoding = "UTF-8";
	
	private JFileChooser openFile;
	private JFileChooser saveFile;
	
	private FileNameExtensionFilter textFilter;
	
	private JLabel genuinityChkResultLbl;
	private String[] genuinityChkResults;
	
	private JLabel hashChkResultLbl;
	private String[] hashChkResults;
	
	public DecryptPnl(MainWindow parent)
	{
		this.parent = parent;
		setLayout(new MigLayout());
		
		initPanels();
		initComponents();
		initFunctionality();
	}
	
	public void setHashResult(int result)
	{
//		< 0 : not possible to do a check
//   	  0 : got negative result
//   	> 1 : got positive result
		if (result < 0)
		{
			hashChkResultLbl.setText("[no hash provided]");
			hashChkResultLbl.setForeground(Color.GRAY);
		}
		else if (result == 0)
		{
			hashChkResultLbl.setText(hashChkResults[result]);
			hashChkResultLbl.setForeground(Color.RED);
		}
		else
		{
			hashChkResultLbl.setText(hashChkResults[result]);
			hashChkResultLbl.setForeground(new Color(76,196,23));
		}
	}
	
	private void setGenuinityChkResult(int result)
	{
//		< 0 : not possible to do a check
//   	  0 : got negative result
//   	> 1 : got positive result
		if (result < 0)
		{
			genuinityChkResultLbl.setText("[sender's public key not provided]");
			genuinityChkResultLbl.setForeground(Color.GRAY);
		}
		else if (result == 0)
		{
			genuinityChkResultLbl.setText(genuinityChkResults[result]);
			genuinityChkResultLbl.setForeground(Color.RED);
		}
		else
		{
			genuinityChkResultLbl.setText(genuinityChkResults[result]);
			genuinityChkResultLbl.setForeground(new Color(76,196,23));
		}
	}
	
	private void initFunctionality()
	{
		openFile = new JFileChooser();
		saveFile = new JFileChooser();
		textFilter = new FileNameExtensionFilter("TEXT FILES", "txt", "text");
		openFile.addChoosableFileFilter(textFilter);
		//openFile.setAcceptAllFileFilterUsed(false);
		
		openPtKBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (openFile.showOpenDialog(parent) == 0) {
					File fileToReadFrom = openFile.getSelectedFile();

					try {
						byte[] content = Files.readAllBytes(fileToReadFrom
								.toPath());
						parent.setMyPrivateKey(new String(content, Charset
								.forName("UTF-8")));
					} catch (IOException ex) {
						ex.printStackTrace();
						JOptionPane.showMessageDialog(parent, ex,
								"An error occurred", JOptionPane.ERROR_MESSAGE);
					}
				}
			}
		});
		
		openPcKBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (openFile.showOpenDialog(parent) == 0) {
					File fileToReadFrom = openFile.getSelectedFile();

					try {
						byte[] content = Files.readAllBytes(fileToReadFrom
								.toPath());
						parent.setSendersPublicKey(new String(content, Charset
								.forName("UTF-8")));
					} catch (IOException ex) {
						ex.printStackTrace();
						JOptionPane.showMessageDialog(parent, ex,
								"An error occurred", JOptionPane.ERROR_MESSAGE);
					}
				}
			}
		});
		
		getFromKeypairBtn.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				String key = ((MainWindow) parent).getMyPrivateKey();
				if (!key.isEmpty())
				{
					myPrivateKeyField.setText(((MainWindow) parent).getMyPrivateKey());
				}
			}
		});
		
		chooseMessagePathBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (openFile.showOpenDialog(parent) == 0) {
					messagePathField.setText(openFile.getSelectedFile()
							.getAbsolutePath());
				}
				if (openFile.showOpenDialog(parent) == 0) {
					messageKeyPathField.setText(openFile.getSelectedFile()
							.getAbsolutePath());
				}
			}
		});
		
		openHashFileBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (openFile.showOpenDialog(parent) == 0) {
					File fileToReadFrom = openFile.getSelectedFile();

					try {
						byte[] content = Files.readAllBytes(fileToReadFrom
								.toPath());
						hash = (new String(content, Charset
								.forName("UTF-8")));
					} catch (IOException ex) {
						ex.printStackTrace();
						JOptionPane.showMessageDialog(parent, ex,
								"An error occurred", JOptionPane.ERROR_MESSAGE);
					}
				}
				
				RSACipher rsaCipher = new RSACipher();
				String decryptedHash = "";
				try {
					decryptedHash = rsaCipher.decryptWPublic(hash, publicKeyPathName,transformation, encoding);
					setGenuinityChkResult(1);
				} catch (IOException | GeneralSecurityException e1) {
					setGenuinityChkResult(0);
					// TODO Auto-generated catch block
					System.out.println("hash");
					e1.printStackTrace();
				}
				HashGenerator hashGen = new HashGenerator();
				String testHash = hashGen.stringHash(originalMessage);
				
				if (testHash == decryptedHash) {
					setHashResult(1);
				}
				else {
					setHashResult(0);
				}
			}
		});
		
		openAsTextBtn.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				String text = totalDecrypt(false);
				originalMessage = text;
				
				JTextArea messageArea = new JTextArea("");
				messageArea.setFont(ResLoader.getMonoFont());
				JScrollPane messageAreaPane = new JScrollPane(messageArea);
				
				messageArea.setText(text);
				
				JFrame textWindow = new JFrame("text");
				textWindow.setSize(new Dimension(480,320));
				textWindow.setLocationRelativeTo(parent);
				textWindow.add(messageAreaPane);
				
				textWindow.setVisible(true);
			}
		});
		
		saveAsBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				
				String text = totalDecrypt(true);
				originalMessage = text;

			}
		});
		
		genuinityChkResults = new String[]
		{
			"message not original", 
			"message confirmed as original"
		};
		
		hashChkResults = new String[]
		{
			"wrong hash or message altered after transmission", 
			"message confirmed as intact"
		};
	}
	
	private String totalDecrypt(boolean toFile) {
		String encryptedMessage = "";
		String encryptedKey = "";
		SecretKey DESKey = null;
		String DESKeyStr = "";
		String message = "";
		
		if (messagePathField.getText() != "" && messageKeyPathField.getText() != "") {
			FileInputStream inputStream1 = null;
			FileInputStream inputStream2 = null;
			try {
				inputStream1 = new FileInputStream(new File(messagePathField.getText()));
				inputStream2 = new FileInputStream(new File(messageKeyPathField.getText()));
			} catch (FileNotFoundException e1) {
				e1.printStackTrace();
			}
			byte[] byteArrayKey = null;
			String strbyteArrayKey ="";
			try {
				encryptedMessage = IOUtils.toString(inputStream1);
				encryptedKey = IOUtils.toString(inputStream2);
				System.out.println("first key:" + encryptedKey + " msg: " + encryptedMessage);
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			
			RSACipher rsaCipher = new RSACipher();
			try {
				//DESKeyStr = rsaCipher.decrypt(encryptedKey, publicKeyPathName,transformation, encoding);
				//System.out.println("first DES key: " + DESKeyStr);
				DESKeyStr = rsaCipher.decrypt(encryptedKey, privateKeyPathName,transformation, encoding);
				System.out.println("second DES key: " + DESKeyStr);
			} catch (IOException e1) {
				System.out.println("error bij AES");
				e1.printStackTrace();
			} catch (GeneralSecurityException e1) {
				System.out.println("error bij AES");
				e1.printStackTrace();
			}
			DESencrypter DES = new DESencrypter();
			DES.setKeyStr(DESKeyStr);
			
			try {
				message = DES.decrypt(encryptedMessage);
			} catch (Exception e) {
				System.out.println("error bij DES");
				e.printStackTrace();
			}
		}
		if (toFile == true) {
			if (saveFile.showSaveDialog(parent) == 0) {
				File fileToSaveTo = saveFile.getSelectedFile();
				String filename = fileToSaveTo.toString();

				try (FileWriter fw = new FileWriter(fileToSaveTo)) {
					// ...
					fw.write(message);
				} catch (IOException ex) {
					ex.printStackTrace();
					JOptionPane.showMessageDialog(parent, ex,
							"An error occurred",
							JOptionPane.ERROR_MESSAGE);
				}
			}
			return "was sent to file";
		}
		else {
			return message;
		}
			
	}
	
	private void initComponents()
	{
		// infoPnl components
		JLabel myPrivateKeyLbl = new JLabel("my private key: ");
		myPrivateKeyLbl.setFont(ResLoader.getDefaultFont());
		infoPnl.add(myPrivateKeyLbl);
		
		myPrivateKeyField = new JTextField();
		myPrivateKeyField.setFont(ResLoader.getDefaultFont());
		infoPnl.add(myPrivateKeyField, "width 100:500");
		
		openPtKBtn = new JButton("open...");
		openPtKBtn.setFont(ResLoader.getDefaultFont());
		infoPnl.add(openPtKBtn);
		
		getFromKeypairBtn = new JButton("get from keypair");
		getFromKeypairBtn.setFont(ResLoader.getDefaultFont());
		infoPnl.add(getFromKeypairBtn, "wrap");
		
		JLabel receiverPublicKeyLbl = new JLabel("sender's public key: ");
		receiverPublicKeyLbl.setFont(ResLoader.getDefaultFont());
		infoPnl.add(receiverPublicKeyLbl);
		
		senderPublicKeyField = new JTextField();
		senderPublicKeyField.setFont(ResLoader.getDefaultFont());
		infoPnl.add(senderPublicKeyField, "width 100:500");
		
		openPcKBtn = new JButton("open...");
		openPcKBtn.setFont(ResLoader.getDefaultFont());
		infoPnl.add(openPcKBtn);
		
		// messagePnl components
		JLabel messagePathLbl = new JLabel("path: ");
		messagePathLbl.setFont(ResLoader.getDefaultFont());
		messagePnl.add(messagePathLbl);
		
		messagePathField = new JTextField();
		messagePathField.setFont(ResLoader.getDefaultFont());
		messagePnl.add(messagePathField, "width 100:500");
		
		JLabel messageKeyPathLbl = new JLabel("key: ");
		messageKeyPathLbl.setFont(ResLoader.getDefaultFont());
		messagePnl.add(messageKeyPathLbl);
		
		messageKeyPathField = new JTextField();
		messageKeyPathField.setFont(ResLoader.getDefaultFont());
		messagePnl.add(messageKeyPathField, "width 100:500");
		
		chooseMessagePathBtn = new JButton("choose...");
		chooseMessagePathBtn.setFont(ResLoader.getDefaultFont());
		messagePnl.add(chooseMessagePathBtn);
		
		// verificationPnl components
		JLabel genuinityChkResultCaptionLbl = new JLabel("genuinity check result: ");
		genuinityChkResultCaptionLbl.setFont(ResLoader.getDefaultFont());
		verificationPnl.add(genuinityChkResultCaptionLbl);
		
		genuinityChkResultLbl = new JLabel();
		genuinityChkResultLbl.setFont(ResLoader.getDefaultFont());
		verificationPnl.add(genuinityChkResultLbl, "wrap");
		setGenuinityChkResult(-1);
		
		JLabel hashChkResultCaptionLbl = new JLabel("hash-check result: ");
		hashChkResultCaptionLbl.setFont(ResLoader.getDefaultFont());
		verificationPnl.add(hashChkResultCaptionLbl);
		
		hashChkResultLbl = new JLabel();
		hashChkResultLbl.setFont(ResLoader.getDefaultFont());
		verificationPnl.add(hashChkResultLbl, "wrap");
		setHashResult(-1);
		
		openHashFileBtn = new JButton("open hash file...");
		openHashFileBtn.setFont(ResLoader.getDefaultFont());
		verificationPnl.add(openHashFileBtn, "gaptop 14");
		
		// frame components
		openAsTextBtn = new JButton("open as text");
		openAsTextBtn.setFont(ResLoader.getDefaultFont());
		frameBtnPnl.add(openAsTextBtn);
		
		saveAsBtn = new JButton("save as...");
		saveAsBtn.setFont(ResLoader.getDefaultFont());
		frameBtnPnl.add(saveAsBtn);
	}
	
	private void initPanels()
	{
		infoPnl = new JPanel(new MigLayout());
		infoPnl.setBorder(BorderFactory.createTitledBorder("info"));
		((TitledBorder) infoPnl.getBorder()).setTitleFont(ResLoader.getBoldFont());
		add(infoPnl, "wrap, width 450:5000");
		
		messagePnl = new JPanel(new MigLayout());
		messagePnl.setBorder(BorderFactory.createTitledBorder("message"));
		((TitledBorder) messagePnl.getBorder()).setTitleFont(ResLoader.getBoldFont());
		add(messagePnl, "wrap, width 450:5000");
		
		verificationPnl = new JPanel(new MigLayout());
		verificationPnl.setBorder(BorderFactory.createTitledBorder("verification"));
		((TitledBorder) verificationPnl.getBorder()).setTitleFont(ResLoader.getBoldFont());
		add(verificationPnl, "wrap, width 450:5000");
		
		frameBtnPnl = new JPanel(new MigLayout());
		frameBtnPnl.setFont(ResLoader.getDefaultFont());
		add(frameBtnPnl, "align right, newline push");
	}
}
