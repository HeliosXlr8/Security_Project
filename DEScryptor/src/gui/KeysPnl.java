package gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.border.TitledBorder;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import DES.DESencrypter;
import RSA.RSAKeyPair;
import main.ResLoader;
import net.miginfocom.swing.MigLayout;

public class KeysPnl extends JPanel
{
	/**
	 * JPanel that contains the elements for the keys tab
	 */
	private static final long serialVersionUID = 1424556237875499064L;
	
	private MainWindow parent;
	
	private JPanel symKeyPnl;
	private JPanel keypairPnl;
	private JPanel frameBtnPnl;
	
	public JTextField symmetricKeyField;
	public JTextField myPrivateKeyField;
	public JTextField myPublicKeyField;
	
	private JButton generateSymKeyBtn;
	private JButton generateKeypairBtn;
	private JButton openKeypairBtn;
	private JButton saveKeypairBtn;
	
	private JFileChooser openFile;
	private JFileChooser saveFile;
	
	public KeysPnl(MainWindow parent)
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
		
		generateSymKeyBtn.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				parent.des = new DESencrypter();
				symmetricKeyField.setText(parent.des.getKeyStr());
			}
		});
		
		generateKeypairBtn.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				parent.rsaKp = new RSAKeyPair(512);
				myPrivateKeyField.setText(parent.rsaKp.getPrivateKeyStr());
				myPublicKeyField.setText(parent.rsaKp.getPublicKeyStr());
			}
		});
		
		openKeypairBtn.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				if (saveFile.showOpenDialog(parent) == 0)
				{
					
				}
			}
		});
		
		saveKeypairBtn.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				if (saveFile.showSaveDialog(parent) == 0)
				{
					File fileToSaveTo = saveFile.getSelectedFile();
					
					try(FileWriter fw = new FileWriter(fileToSaveTo))
					{
						// TODO: opsplitsing tss private en public key, beide moeten in aparte files
						fw.write(parent.getMyPrivateKey());
						//fw.write(System.lineSeparator());
						//fw.write(parent.getMyPublicKey());
					}
					catch (IOException ex)
					{
						ex.printStackTrace();
						JOptionPane.showMessageDialog(parent, ex, "An error occurred", JOptionPane.ERROR_MESSAGE);
					}
				}
			}
		});
	}
	
	private void initComponents()
	{
		JLabel myPrivateKeyLbl = new JLabel("my private key: ");
		myPrivateKeyLbl.setFont(ResLoader.getDefaultFont());
		keypairPnl.add(myPrivateKeyLbl);
		
		symmetricKeyField = new JTextField();
		symmetricKeyField.setFont(ResLoader.getDefaultFont());
		symKeyPnl.add(symmetricKeyField, "wrap, width 100:500");
		
		myPrivateKeyField = new JTextField();
		myPrivateKeyField.setFont(ResLoader.getDefaultFont());
		keypairPnl.add(myPrivateKeyField, "wrap, width 100:500");
		
		JLabel myPublicKeyLbl = new JLabel("my public key: ");
		myPublicKeyLbl.setFont(ResLoader.getDefaultFont());
		keypairPnl.add(myPublicKeyLbl);
		
		myPublicKeyField = new JTextField();
		myPublicKeyField.setFont(ResLoader.getDefaultFont());
		keypairPnl.add(myPublicKeyField, "wrap, width 100:500");
		
		generateSymKeyBtn = new JButton("generate now");
		generateSymKeyBtn.setFont(ResLoader.getDefaultFont());
		symKeyPnl.add(generateSymKeyBtn, "gaptop 14");
		
		generateKeypairBtn = new JButton("generate now");
		generateKeypairBtn.setFont(ResLoader.getDefaultFont());
		keypairPnl.add(generateKeypairBtn, "gaptop 14");
		
		openKeypairBtn = new JButton("open keypair...");
		openKeypairBtn.setFont(ResLoader.getDefaultFont());
		frameBtnPnl.add(openKeypairBtn);
		
		saveKeypairBtn = new JButton("save keypair...");
		saveKeypairBtn.setFont(ResLoader.getDefaultFont());
		frameBtnPnl.add(saveKeypairBtn);
	}
	
	private void initPanels()
	{
		symKeyPnl = new JPanel(new MigLayout());
		symKeyPnl.setBorder(BorderFactory.createTitledBorder("symmetric key (DES)"));
		((TitledBorder) symKeyPnl.getBorder()).setTitleFont(ResLoader.getBoldFont());
		add(symKeyPnl, "wrap, width 450:5000");
		
		keypairPnl = new JPanel(new MigLayout());
		keypairPnl.setBorder(BorderFactory.createTitledBorder("keypair (RSA)"));
		((TitledBorder) keypairPnl.getBorder()).setTitleFont(ResLoader.getBoldFont());
		add(keypairPnl, "wrap, width 450:5000");
		
		frameBtnPnl = new JPanel(new MigLayout());
		frameBtnPnl.setFont(ResLoader.getDefaultFont());
		add(frameBtnPnl, "align right, newline push");
	}
}
