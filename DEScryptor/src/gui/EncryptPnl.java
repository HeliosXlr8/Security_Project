package gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;
import javax.swing.filechooser.FileNameExtensionFilter;

import cipher.HashGenerator;
import main.ResLoader;
import net.miginfocom.swing.MigLayout;

public class EncryptPnl extends JPanel
{
	/**
	 * JPanel that contains the elements for the encrypting tab
	 */
	private static final long serialVersionUID = 2474921857423394588L;
	
	private MainWindow parent;
	
	private JPanel infoPnl;
	private JPanel messagePnl;
	private JPanel optionsPnl;
	private JPanel messagePathChooserPnl;
	private JPanel frameBtnPnl;
	
	private JTextField myPrivateKeyField;
	private JTextField receiverPublicKeyField;
	private JTextField messagePathField;
	
	private JButton choosePtKBtn;	// "choose my private key" button
	private JButton choosePcKBtn;	// "choose receiver's public key" button
	private JButton getFromKeypairBtn;
	private JButton chooseMessagePathBtn;
	private JButton exportBtn;
	private JButton sendBtn;
	
	private JRadioButton textRadBtn;
	private JRadioButton fileRadBtn;
	private JCheckBox useHashingChkBox;
	private JTextArea messageArea;
	private JScrollPane messageAreaPane;
	
	private JFileChooser openFile;
	private JFileChooser saveFile;
	
	private FileNameExtensionFilter textFilter;
	
	public EncryptPnl(MainWindow parent)
	{
		this.parent = parent;
		setLayout(new MigLayout());
		
		initPanels();
		initComponents();
		initFunctionality();
	}
	
	private void initFunctionality()
	{
		openFile = new JFileChooser();
		saveFile = new JFileChooser();
		
		textFilter = new FileNameExtensionFilter("Text files (*.txt)", "txt");
		saveFile.addChoosableFileFilter(textFilter);
		
		getFromKeypairBtn.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				String key = parent.getMyPrivateKey();
				if (!key.isEmpty())
				{
					myPrivateKeyField.setText(parent.getMyPrivateKey());
				}
			}
		});
		
		textRadBtn.addItemListener(new ItemListener()
		{
			@Override
			public void itemStateChanged(ItemEvent e)
			{
				if (e.getStateChange() == ItemEvent.SELECTED)
				{
					removeMessagePathChooserPnl();
					addMessageAreaPane();
				}
			}
		});
		
		fileRadBtn.addItemListener(new ItemListener()
		{
			@Override
			public void itemStateChanged(ItemEvent e)
			{
				if (e.getStateChange() == ItemEvent.SELECTED)
				{
					removeMessageAreaPane();
					addMessagePathChooserPnl();
				}
			}
		});
		
		exportBtn.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				// encrypten
				// [is nog niet af]
				
				// hashen
				String resultaatUitEncryptie = "test";
				HashGenerator hashGen = new HashGenerator();
				
				String hash = hashGen.stringHash(resultaatUitEncryptie);
				if (saveFile.showSaveDialog(parent) == 0)
				{		
					File fileToSaveTo = saveFile.getSelectedFile();
					String filename = fileToSaveTo.toString();
					if (saveFile.getFileFilter().toString().contains("*.txt"))
					{
						// selected extension: text file
						if (!filename.endsWith(".txt"))
						{
							fileToSaveTo = new File(filename+".txt");
						}
					}
					
					try(FileWriter fw = new FileWriter(fileToSaveTo))
					{
						//...
						fw.write(hash);
					}
					catch (IOException ex)
					{
						ex.printStackTrace();
						JOptionPane.showMessageDialog(parent, ex, "An error occurred", JOptionPane.ERROR_MESSAGE);
					}
				}
			}
		});
		
		sendBtn.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				// heeft geen zin als het netwerkgedeelte er nog niet is (sockets etc...)
			}
		});
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
		
		JLabel receiverPublicKeyLbl = new JLabel("receiver's public key: ");
		receiverPublicKeyLbl.setFont(ResLoader.getDefaultFont());
		infoPnl.add(receiverPublicKeyLbl);
		
		receiverPublicKeyField = new JTextField();
		receiverPublicKeyField.setFont(ResLoader.getDefaultFont());
		infoPnl.add(receiverPublicKeyField, "width 100:500");
		
		choosePcKBtn = new JButton("choose...");
		choosePcKBtn.setFont(ResLoader.getDefaultFont());
		infoPnl.add(choosePcKBtn);
		
		// messagePnl components
		ButtonGroup textOrFileBtnGrp = new ButtonGroup();
		
		textRadBtn = new JRadioButton("text message");
		textRadBtn.setFont(ResLoader.getDefaultFont());
		textRadBtn.setSelected(true);
		textOrFileBtnGrp.add(textRadBtn);
		optionsPnl.add(textRadBtn, "wrap");
		
		fileRadBtn = new JRadioButton("file");
		fileRadBtn.setFont(ResLoader.getDefaultFont());
		textOrFileBtnGrp.add(fileRadBtn);
		optionsPnl.add(fileRadBtn, "wrap");
		
		useHashingChkBox = new JCheckBox("use hashing");
		useHashingChkBox.setFont(ResLoader.getDefaultFont());
		optionsPnl.add(useHashingChkBox);
		
		messageArea = new JTextArea();
		messageArea.setFont(ResLoader.getMonoFont());
		messageAreaPane = new JScrollPane(messageArea);
		addMessageAreaPane();
		
		// messagePathChooserPnl components
		messagePathChooserPnl = new JPanel(new MigLayout());
		
		JLabel messagePathLbl = new JLabel("path: ");
		messagePathLbl.setFont(ResLoader.getDefaultFont());
		messagePathChooserPnl.add(messagePathLbl);
		
		messagePathField = new JTextField();
		messagePathField.setFont(ResLoader.getDefaultFont());
		messagePathChooserPnl.add(messagePathField, "width 100:500");
		
		chooseMessagePathBtn = new JButton("choose...");
		chooseMessagePathBtn.setFont(ResLoader.getDefaultFont());
		messagePathChooserPnl.add(chooseMessagePathBtn);
		
		// frame components
		frameBtnPnl = new JPanel(new MigLayout());
		add(frameBtnPnl, "align right, newline push");
		
		exportBtn = new JButton("export...");
		exportBtn.setFont(ResLoader.getDefaultFont());
		frameBtnPnl.add(exportBtn);
		
		sendBtn = new JButton("send");
		sendBtn.setFont(ResLoader.getDefaultFont());
		frameBtnPnl.add(sendBtn);
	}
	
	private void removeMessagePathChooserPnl()
	{
		messagePnl.remove(messagePathChooserPnl);
		messagePnl.revalidate();
		repaint();
	}
	
	private void addMessagePathChooserPnl()
	{
		messagePnl.add(messagePathChooserPnl, "grow, gapleft 32");
		messagePnl.revalidate();
		repaint();
	}
	
	private void removeMessageAreaPane()
	{
		messagePnl.remove(messageAreaPane);
		messagePnl.revalidate();
		repaint();
	}
	
	private void addMessageAreaPane()
	{
		messagePnl.add(messageAreaPane, "width 150:5000, height 150:5000");
		messagePnl.revalidate();
		repaint();
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
		
		optionsPnl = new JPanel(new MigLayout());
		optionsPnl.setBorder(BorderFactory.createEmptyBorder());
		messagePnl.add(optionsPnl, "grow");
	}
}
