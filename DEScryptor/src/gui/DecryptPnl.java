package gui;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;

import main.ResLoader;
import net.miginfocom.swing.MigLayout;

public class DecryptPnl extends JPanel
{
	/**
	 * JPanel that contains the elements for the decrypting tab
	 */
	private static final long serialVersionUID = -1665345338497275072L;

	private JPanel infoPnl;
	private JPanel messagePnl;
	private JPanel verificationPnl;
	private JPanel frameBtnPnl;
	
	private JTextField myPrivateKeyField;
	private JTextField senderPublicKeyField;
	private JTextField messagePathField;
	
	private JButton choosePtKBtn;	// "choose my private key" button
	private JButton choosePcKBtn;	// "choose sender's public key" button
	private JButton getFromKeypairBtn;
	private JButton chooseHashFileBtn;
	private JButton chooseMessagePathBtn;
	private JButton openAsTextBtn;
	private JButton saveAsBtn;
	
	private JLabel genuinityChkResultLbl;
	private String[] genuinityChkResults;
	
	private JLabel hashChkResultLbl;
	private String[] hashChkResults;
	
	private JFrame parent;
	
	public DecryptPnl(JFrame parent)
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
		getFromKeypairBtn.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				myPrivateKeyField.setText(((MainWindow) parent).getMyPrivateKey());
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
		
		choosePtKBtn = new JButton("choose...");
		choosePtKBtn.setFont(ResLoader.getDefaultFont());
		infoPnl.add(choosePtKBtn);
		
		getFromKeypairBtn = new JButton("get from keypair");
		getFromKeypairBtn.setFont(ResLoader.getDefaultFont());
		infoPnl.add(getFromKeypairBtn, "wrap");
		
		JLabel receiverPublicKeyLbl = new JLabel("sender's public key: ");
		receiverPublicKeyLbl.setFont(ResLoader.getDefaultFont());
		infoPnl.add(receiverPublicKeyLbl);
		
		senderPublicKeyField = new JTextField();
		senderPublicKeyField.setFont(ResLoader.getDefaultFont());
		infoPnl.add(senderPublicKeyField, "width 100:500");
		
		choosePcKBtn = new JButton("choose...");
		choosePcKBtn.setFont(ResLoader.getDefaultFont());
		infoPnl.add(choosePcKBtn);
		
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
		
		chooseHashFileBtn = new JButton("choose hash file...");
		chooseHashFileBtn.setFont(ResLoader.getDefaultFont());
		verificationPnl.add(chooseHashFileBtn, "gaptop 14");
		
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
