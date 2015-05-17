package gui;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;
import javax.swing.filechooser.FileNameExtensionFilter;

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
	
	private JButton openPtKBtn;	// "open my private key" button
	private JButton openPcKBtn;	// "open sender's public key" button
	private JButton getFromKeypairBtn;
	private JButton openHashFileBtn;
	private JButton chooseMessagePathBtn;
	private JButton openAsTextBtn;
	private JButton saveAsBtn;
	
	private String hash;
	
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
		openFile.setAcceptAllFileFilterUsed(false);
		
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
				if (saveFile.showSaveDialog(parent) == 0) {
					messagePathField.setText(saveFile.getSelectedFile()
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
				
				// now do a hash check
				//...
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
