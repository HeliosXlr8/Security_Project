package gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.IOException;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;
import javax.swing.filechooser.FileNameExtensionFilter;

import cipher.SteganographyConverter;
import main.ResLoader;
import net.miginfocom.swing.MigLayout;

public class SteganographyPnl extends JPanel
{
	/**
	 * JPanel that contains the elements for the keys tab
	 */
	private static final long serialVersionUID = 1424556237875499064L;
	
	private JFrame parent;
	
	private JPanel imgPnl;
	private JPanel messagePnl;
	private JPanel messagePathChooserPnl;
	private JPanel optionsPnl;
	private JPanel frameBtnPnl;
	
	private JTextField imgPathFieldOriginal;
	private JTextField imgPathFieldNew;
	private JTextField messagePathField;
	
	private JButton chooseImgPathOriginalBtn;
	private JButton chooseImgPathNewBtn;
	private JButton chooseMessagePathBtn;
	
	private JRadioButton textRadBtn;
	private JRadioButton fileRadBtn;
	
	private JTextArea messageArea;
	private JScrollPane messageAreaPane;
	
	private JButton encodeBtn;
	private JButton decodeBtn1;
	private JButton decodeBtn2;
	
	private JFileChooser openFile;
	private JFileChooser saveFile;
	private FileNameExtensionFilter pngFilter;
	
	public SteganographyPnl(JFrame parent)
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
		pngFilter = new FileNameExtensionFilter("Images (*.png & *.jpg)", "png", "jpg");
		openFile.addChoosableFileFilter(pngFilter);
		openFile.setAcceptAllFileFilterUsed(false);
		
		chooseImgPathOriginalBtn.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				if (openFile.showOpenDialog(parent) == 0)
				{
					imgPathFieldOriginal.setText(openFile.getSelectedFile().getAbsolutePath());
				}
			}
		});
		
		chooseImgPathNewBtn.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				if (openFile.showOpenDialog(parent) == 0)
				{
					imgPathFieldNew.setText(openFile.getSelectedFile().getAbsolutePath());
				}
			}
		});
		
		encodeBtn.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				SteganographyConverter sc = new SteganographyConverter();
				if (textRadBtn.isSelected()) {					
					sc.encodeMessage(messageArea.getText(), imgPathFieldOriginal.getText());
				}
				else {
					try {
						sc.encodeFile(messagePathField.getText(), imgPathFieldOriginal.getText());
					} catch (IOException e1) {
						JOptionPane.showMessageDialog(null, 
								"There is a problem with the path of the file you want to hide!","Error",
								JOptionPane.ERROR_MESSAGE);
					}
				}
			}
		});
		
		decodeBtn1.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				SteganographyConverter sc = new SteganographyConverter();
				String message = sc.decodeMessage(imgPathFieldNew.getText());
				messageArea.setText(message);
			}
		});
		
		decodeBtn2.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				//[start decoding]
				
				//[save message to provided path]
				if (saveFile.showSaveDialog(parent) == 0)
				{
					// write decoded message to path
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
	}
	
	private void initComponents()
	{
		JLabel imgPathFieldOriginalLbl = new JLabel("original image path: ");
		imgPathFieldOriginalLbl.setFont(ResLoader.getDefaultFont());
		imgPnl.add(imgPathFieldOriginalLbl);
		
		imgPathFieldOriginal = new JTextField();
		imgPathFieldOriginal.setFont(ResLoader.getDefaultFont());
		imgPnl.add(imgPathFieldOriginal, "width 100:500");
		
		chooseImgPathOriginalBtn = new JButton("choose...");
		chooseImgPathOriginalBtn.setFont(ResLoader.getDefaultFont());
		imgPnl.add(chooseImgPathOriginalBtn, "wrap");
		
		JLabel imgPathFieldNewLabel = new JLabel("transcoded image path: ");
		imgPathFieldNewLabel.setFont(ResLoader.getDefaultFont());
		imgPnl.add(imgPathFieldNewLabel);
		
		imgPathFieldNew = new JTextField();
		imgPathFieldNew.setFont(ResLoader.getDefaultFont());
		imgPnl.add(imgPathFieldNew, "width 100:500");
		
		chooseImgPathNewBtn = new JButton("choose...");
		chooseImgPathNewBtn.setFont(ResLoader.getDefaultFont());
		imgPnl.add(chooseImgPathNewBtn, "wrap");
		
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
		
		messageArea = new JTextArea();
		messageArea.setFont(ResLoader.getMonoFont());
		messageAreaPane = new JScrollPane(messageArea);
		addMessageAreaPane();
		
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
		
		frameBtnPnl = new JPanel(new MigLayout());
		add(frameBtnPnl, "align right, newline push");
		
		encodeBtn = new JButton("encode");
		encodeBtn.setFont(ResLoader.getDefaultFont());
		frameBtnPnl.add(encodeBtn);
		
		decodeBtn1 = new JButton("decode and open as text");
		decodeBtn1.setFont(ResLoader.getDefaultFont());
		frameBtnPnl.add(decodeBtn1);
		
		decodeBtn2 = new JButton("decode and save as...");
		decodeBtn2.setFont(ResLoader.getDefaultFont());
		frameBtnPnl.add(decodeBtn2);
	}
	
	private void initPanels()
	{
		imgPnl = new JPanel(new MigLayout());
		imgPnl.setBorder(BorderFactory.createTitledBorder("image files I/O"));
		((TitledBorder) imgPnl.getBorder()).setTitleFont(ResLoader.getBoldFont());
		add(imgPnl, "wrap, width 450:5000");
		
		messagePnl = new JPanel(new MigLayout());
		messagePnl.setBorder(BorderFactory.createTitledBorder("message"));
		((TitledBorder) messagePnl.getBorder()).setTitleFont(ResLoader.getBoldFont());
		add(messagePnl, "wrap, width 450:5000");
		
		optionsPnl = new JPanel(new MigLayout());
		optionsPnl.setBorder(BorderFactory.createEmptyBorder());
		messagePnl.add(optionsPnl, "grow");
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
}
