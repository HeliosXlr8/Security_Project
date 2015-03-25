package gui;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.SwingConstants;

import main.ResLoader;

public class MainWindow extends JFrame
{
	/**
	 * Main window
	 */
	private static final long serialVersionUID = 6352370266727183750L;
	private static final String NAME = "DEScryptor";
	private static final String VERSION = "0.1";
	
	private KeysPnl keysPnl;
	private EncryptPnl encryptPnl;
	private DecryptPnl decryptPnl;
	
	private JPanel mainPnl;
	private JTabbedPane tPane;
	private JLabel[] tLabels;
	private int tHeight = 30;
	
	public MainWindow()
	{
		setTitle(NAME+" "+VERSION);
		setName(getTitle());
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(new Dimension(960,640));
		setIconImage(ResLoader.getAppIcon());
		
		initPanels();
		initTabs();
		
		setVisible(true);
	}
	
	public void setMyPublicKey(String key)
	{
		keysPnl.myPublicKeyField.setText(key);
	}
	
	public String getMyPublicKey()
	{
		return keysPnl.myPublicKeyField.getText();
	}
	
	public void setMyPrivateKey(String key)
	{
		keysPnl.myPrivateKeyField.setText(key);
	}
	
	public String getMyPrivateKey()
	{
		return keysPnl.myPrivateKeyField.getText();
	}
	
	private void initTabs()
	{
		tLabels = new JLabel[]{new JLabel("keys"), new JLabel("encrypt"), new JLabel("decrypt")};
		for (JLabel lbl : tLabels)
		{
			lbl.setPreferredSize(new Dimension(200, tHeight));
			lbl.setFont(ResLoader.getDefaultFont());
			lbl.setHorizontalAlignment(SwingConstants.CENTER);
		}
		
		tPane = new JTabbedPane();
		tPane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
		tPane.add(keysPnl);
		tPane.add(encryptPnl);
		tPane.add(decryptPnl);
		tPane.setTabComponentAt(0, tLabels[0]);
		tPane.setTabComponentAt(1, tLabels[1]);
		tPane.setTabComponentAt(2, tLabels[2]);
		
		mainPnl.add(tPane);
		add(mainPnl);
	}
	
	private void initPanels()
	{
		mainPnl = new JPanel();
		mainPnl.setLayout(new GridLayout(1,1));
		mainPnl.addComponentListener(new ComponentAdapter()
		{
			public void componentResized(ComponentEvent evt)
			{
				Component c = (Component) evt.getSource();
				int width = c.getSize().width;
				width = width / 3;
				for (JLabel lbl : tLabels)
				{
					lbl.setPreferredSize(new Dimension(width-12, tHeight));
				}
			}
		});
		
		keysPnl = new KeysPnl();
		encryptPnl = new EncryptPnl(this);
		decryptPnl = new DecryptPnl(this);
	}
	
	public static String getVersion()
	{
		return VERSION;
	}
	
	public static String getNameOfProgram()
	{
		return NAME;
	}
}
