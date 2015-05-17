package gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;

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

import org.apache.commons.io.IOUtils;

import DES.DESencrypter;
import RSA.RSACipher;
import cipher.HashGenerator;
import main.ResLoader;
import net.miginfocom.swing.MigLayout;

public class EncryptPnl extends JPanel {
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

	public JTextField myPrivateKeyField;
	public JTextField receiverPublicKeyField;
	private JTextField messagePathField;

	private JButton openPtKBtn; // "open my private key" button
	private JButton openPcKBtn; // "open receiver's public key" button
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

	private final String privateKeyPathName = "bin/private.key";
	private final String publicKeyPathName = "bin/public.key";
	private final String transformation = "RSA/ECB/PKCS1Padding";
	private final String encoding = "UTF-8";

	private byte[] byteArray = null;

	public EncryptPnl(MainWindow parent) {
		this.parent = parent;
		setLayout(new MigLayout());

		initPanels();
		initComponents();
		initFunctionality();
	}

	private void initFunctionality() {
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
						parent.setReceiversPublicKey(new String(content, Charset
								.forName("UTF-8")));
					} catch (IOException ex) {
						ex.printStackTrace();
						JOptionPane.showMessageDialog(parent, ex,
								"An error occurred", JOptionPane.ERROR_MESSAGE);
					}
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

		getFromKeypairBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String key = parent.getMyPrivateKey();
				if (!key.isEmpty()) {
					myPrivateKeyField.setText(parent.getMyPrivateKey());
				}
			}
		});

		textRadBtn.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED) {
					removeMessagePathChooserPnl();
					addMessageAreaPane();
				}
			}
		});

		fileRadBtn.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED) {
					removeMessageAreaPane();
					addMessagePathChooserPnl();
				}
			}
		});

		exportBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				boolean encryptSucces = false;
				//mindfucked door dees if -->
				if (myPrivateKeyField.getText() != "Insert key" || receiverPublicKeyField.getText() != "Insert key") {
					// encrypten
					DESencrypter DES = new DESencrypter();
					DES.setKeyStr(parent.des.getKeyStr());
					System.out.println(parent.des.getKeyStr());
					FileInputStream inputStream = null;
					String EncryptedText = "";
					try {
						if (fileRadBtn.isSelected() && messagePathField.getText() != "Set a path...") {
							inputStream = new FileInputStream(messagePathField.getText());
							String everything = IOUtils.toString(inputStream);
							EncryptedText = DES.encrypt(everything);
						} else if (messageArea.getText() != "Type your message") {
							EncryptedText = DES.encrypt(messageArea.getText());
						} else {
							throw new Exception("Not every area is filled");
						}

						System.out.println("----------------");
						System.out.println("Text encrypted.");
						System.out.println("----------------");
						RSACipher rsaCipher = new RSACipher();
						String encryptedDESKey = rsaCipher.encrypt(
								DES.getKeyStr(), publicKeyPathName,
								transformation, encoding);
						System.out.println("Key encrypted.");
						encryptSucces = true;

					} catch (Exception exception) {
						exception.printStackTrace();
						JOptionPane.showMessageDialog(
								parent,
								exception,
								"Encryption failed due to: "
										+ exception.getMessage(),
								JOptionPane.ERROR_MESSAGE);
					} finally {
						if (inputStream != null) {
							try {
								inputStream.close();
							} catch (IOException e1) {
								e1.printStackTrace();
							}
						}
					}
				} else {
					try {
						throw new Exception("No keys entered");
					} catch (Exception e1) {
						JOptionPane.showMessageDialog(parent, e1,
								"No keys entered " + e1.getMessage(),
								JOptionPane.ERROR_MESSAGE);
						encryptSucces = false;
						e1.printStackTrace();
					}
				}

				// hashen
				if (encryptSucces) {
					String resultaatUitEncryptie = "test";
					HashGenerator hashGen = new HashGenerator();

					String hash = hashGen.stringHash(resultaatUitEncryptie);
					if (saveFile.showSaveDialog(parent) == 0) {
						File fileToSaveTo = saveFile.getSelectedFile();
						String filename = fileToSaveTo.toString();
						if (saveFile.getFileFilter().toString()
								.contains("*.txt")) {
							// selected extension: text file
							if (!filename.endsWith(".txt")) {
								fileToSaveTo = new File(filename + ".txt");
							}
						}

						try (FileWriter fw = new FileWriter(fileToSaveTo)) {
							// ...
							fw.write(hash);
						} catch (IOException ex) {
							ex.printStackTrace();
							JOptionPane.showMessageDialog(parent, ex,
									"An error occurred",
									JOptionPane.ERROR_MESSAGE);
						}
					}
				}
			}
		});

		sendBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

			}
		});
	}

	private void initComponents() {
		// infoPnl components
		JLabel myPrivateKeyLbl = new JLabel("my private key: ");
		myPrivateKeyLbl.setFont(ResLoader.getDefaultFont());
		infoPnl.add(myPrivateKeyLbl);

		myPrivateKeyField = new JTextField("Insert key");
		myPrivateKeyField.setFont(ResLoader.getDefaultFont());
		infoPnl.add(myPrivateKeyField, "width 100:500");

		openPtKBtn = new JButton("open...");
		openPtKBtn.setFont(ResLoader.getDefaultFont());
		infoPnl.add(openPtKBtn);

		getFromKeypairBtn = new JButton("get from keypair");
		getFromKeypairBtn.setFont(ResLoader.getDefaultFont());
		infoPnl.add(getFromKeypairBtn, "wrap");

		JLabel receiverPublicKeyLbl = new JLabel("receiver's public key: ");
		receiverPublicKeyLbl.setFont(ResLoader.getDefaultFont());
		infoPnl.add(receiverPublicKeyLbl);

		receiverPublicKeyField = new JTextField("Insert key");
		receiverPublicKeyField.setFont(ResLoader.getDefaultFont());
		infoPnl.add(receiverPublicKeyField, "width 100:500");

		openPcKBtn = new JButton("open...");
		openPcKBtn.setFont(ResLoader.getDefaultFont());
		infoPnl.add(openPcKBtn);

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

		messageArea = new JTextArea("Type your message");
		messageArea.setFont(ResLoader.getMonoFont());
		messageAreaPane = new JScrollPane(messageArea);
		addMessageAreaPane();

		// messagePathChooserPnl components
		messagePathChooserPnl = new JPanel(new MigLayout());

		JLabel messagePathLbl = new JLabel("path: ");
		messagePathLbl.setFont(ResLoader.getDefaultFont());
		messagePathChooserPnl.add(messagePathLbl);

		messagePathField = new JTextField("Set a path...");
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

	private void removeMessagePathChooserPnl() {
		messagePnl.remove(messagePathChooserPnl);
		messagePnl.revalidate();
		repaint();
	}

	private void addMessagePathChooserPnl() {
		messagePnl.add(messagePathChooserPnl, "grow, gapleft 32");
		messagePnl.revalidate();
		repaint();
	}

	private void removeMessageAreaPane() {
		messagePnl.remove(messageAreaPane);
		messagePnl.revalidate();
		repaint();
	}

	private void addMessageAreaPane() {
		messagePnl.add(messageAreaPane, "width 150:5000, height 150:5000");
		messagePnl.revalidate();
		repaint();
	}

	private void initPanels() {
		infoPnl = new JPanel(new MigLayout());
		infoPnl.setBorder(BorderFactory.createTitledBorder("info"));
		((TitledBorder) infoPnl.getBorder()).setTitleFont(ResLoader
				.getBoldFont());
		add(infoPnl, "wrap, width 450:5000");

		messagePnl = new JPanel(new MigLayout());
		messagePnl.setBorder(BorderFactory.createTitledBorder("message"));
		((TitledBorder) messagePnl.getBorder()).setTitleFont(ResLoader
				.getBoldFont());
		add(messagePnl, "wrap, width 450:5000");

		optionsPnl = new JPanel(new MigLayout());
		optionsPnl.setBorder(BorderFactory.createEmptyBorder());
		messagePnl.add(optionsPnl, "grow");
	}
}
