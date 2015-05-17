package gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.security.PrivateKey;
import java.security.PublicKey;

import javax.swing.border.TitledBorder;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import sun.misc.BASE64Encoder;
import DES.DESencrypter;
import RSA.RSAKeyPair;
import main.ResLoader;
import net.miginfocom.swing.MigLayout;

public class KeysPnl extends JPanel {
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
	private JButton openPrKbtn;
	private JButton savePrKbtn;
	private JButton openPuKbtn;
	private JButton savePuKbtn;

	private JFileChooser openFile;
	private JFileChooser saveFile;

	private PrivateKey privateKey;
	private PublicKey publicKey;

	public KeysPnl(MainWindow parent) {
		this.parent = parent;
		setLayout(new MigLayout());

		initPanels();
		initComponents();
		initFunctionality();
	}

	private void initFunctionality() {
		openFile = new JFileChooser();
		saveFile = new JFileChooser();

		generateSymKeyBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				parent.des = new DESencrypter();
				symmetricKeyField.setText(parent.des.getKeyStr());
			}
		});

		generateKeypairBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				parent.rsaKp = new RSAKeyPair(512);
				privateKey = parent.rsaKp.getPrivateKey();
				publicKey = parent.rsaKp.getPublicKey();
				myPrivateKeyField.setText(parent.rsaKp.getPrivateKeyStr());
				myPublicKeyField.setText(parent.rsaKp.getPublicKeyStr());
				try {
					parent.rsaKp.toFileSystem("bin/private.key",
							"bin/public.key");
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
					JOptionPane.showMessageDialog(parent, e1,
							"An error occurred", JOptionPane.ERROR_MESSAGE);
				}
			}
		});

		openPrKbtn.addActionListener(new ActionListener() {
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

		savePrKbtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (saveFile.showSaveDialog(parent) == 0) {
					File fileToSaveTo = saveFile.getSelectedFile();

					try (FileWriter fw = new FileWriter(fileToSaveTo)) {
						fw.write(parent.getMyPrivateKey());
					} catch (IOException ex) {
						ex.printStackTrace();
						JOptionPane.showMessageDialog(parent, ex,
								"An error occurred", JOptionPane.ERROR_MESSAGE);
					}
				}
			}
		});

		openPuKbtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (openFile.showOpenDialog(parent) == 0) {
					File fileToReadFrom = openFile.getSelectedFile();

					try {
						byte[] content = Files.readAllBytes(fileToReadFrom
								.toPath());
						parent.setMyPublicKey(new String(content, Charset
								.forName("UTF-8")));
					} catch (IOException ex) {
						ex.printStackTrace();
						JOptionPane.showMessageDialog(parent, ex,
								"An error occurred", JOptionPane.ERROR_MESSAGE);
					}
				}
			}
		});

		savePuKbtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (saveFile.showSaveDialog(parent) == 0) {
					File fileToSaveTo = saveFile.getSelectedFile();

					try (FileWriter fw = new FileWriter(fileToSaveTo)) {
						fw.write(parent.getMyPublicKey());
					} catch (IOException ex) {
						ex.printStackTrace();
						JOptionPane.showMessageDialog(parent, ex,
								"An error occurred", JOptionPane.ERROR_MESSAGE);
					}
				}
			}
		});
	}

	private void initComponents() {
		JLabel myPrivateKeyLbl = new JLabel("my private key: ");
		myPrivateKeyLbl.setFont(ResLoader.getDefaultFont());
		keypairPnl.add(myPrivateKeyLbl);

		symmetricKeyField = new JTextField();
		symmetricKeyField.setFont(ResLoader.getDefaultFont());
		symKeyPnl.add(symmetricKeyField, "wrap, width 100:500");

		myPrivateKeyField = new JTextField();
		myPrivateKeyField.setFont(ResLoader.getDefaultFont());
		keypairPnl.add(myPrivateKeyField, "width 100:500");

		openPrKbtn = new JButton("open...");
		openPrKbtn.setFont(ResLoader.getDefaultFont());
		keypairPnl.add(openPrKbtn);

		savePrKbtn = new JButton("save...");
		savePrKbtn.setFont(ResLoader.getDefaultFont());
		keypairPnl.add(savePrKbtn, "wrap");

		JLabel myPublicKeyLbl = new JLabel("my public key: ");
		myPublicKeyLbl.setFont(ResLoader.getDefaultFont());
		keypairPnl.add(myPublicKeyLbl);

		myPublicKeyField = new JTextField();
		myPublicKeyField.setFont(ResLoader.getDefaultFont());
		keypairPnl.add(myPublicKeyField, "width 100:500");

		openPuKbtn = new JButton("open...");
		openPuKbtn.setFont(ResLoader.getDefaultFont());
		keypairPnl.add(openPuKbtn);

		savePuKbtn = new JButton("save...");
		savePuKbtn.setFont(ResLoader.getDefaultFont());
		keypairPnl.add(savePuKbtn, "wrap");

		generateSymKeyBtn = new JButton("generate now");
		generateSymKeyBtn.setFont(ResLoader.getDefaultFont());
		symKeyPnl.add(generateSymKeyBtn, "gaptop 14");

		generateKeypairBtn = new JButton("generate now");
		generateKeypairBtn.setFont(ResLoader.getDefaultFont());
		keypairPnl.add(generateKeypairBtn, "gaptop 14");
	}

	private void initPanels() {
		symKeyPnl = new JPanel(new MigLayout());
		symKeyPnl.setBorder(BorderFactory
				.createTitledBorder("symmetric key (DES)"));
		((TitledBorder) symKeyPnl.getBorder()).setTitleFont(ResLoader
				.getBoldFont());
		add(symKeyPnl, "wrap, width 450:5000");

		keypairPnl = new JPanel(new MigLayout());
		keypairPnl.setBorder(BorderFactory.createTitledBorder("keypair (RSA)"));
		((TitledBorder) keypairPnl.getBorder()).setTitleFont(ResLoader
				.getBoldFont());
		add(keypairPnl, "wrap, width 450:5000");

		frameBtnPnl = new JPanel(new MigLayout());
		frameBtnPnl.setFont(ResLoader.getDefaultFont());
		add(frameBtnPnl, "align right, newline push");
	}

	public PrivateKey getPrivateKey() {
		return privateKey;
	}

	public PublicKey getPublicKey() {
		return publicKey;
	}

}
