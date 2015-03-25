package gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.border.TitledBorder;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import main.ResLoader;
import net.miginfocom.swing.MigLayout;

public class KeysPnl extends JPanel
{
	/**
	 * JPanel that contains the elements for the keys tab
	 */
	private static final long serialVersionUID = 1424556237875499064L;
	
	private JPanel keypairPnl;
	private JPanel frameBtnPnl;
	
	public JTextField myPrivateKeyField;
	public JTextField myPublicKeyField;
	
	private JButton generateNowBtn;
	private JButton openKeypairBtn;
	private JButton saveKeypairBtn;
	
	public KeysPnl()
	{
		setLayout(new MigLayout());
		
		initPanels();
		initComponents();
		initFunctionality();
	}
	
	private void initFunctionality()
	{
		generateNowBtn.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				//[start keypair generation]
			}
		});
	}
	
	private void initComponents()
	{
		JLabel myPrivateKeyLbl = new JLabel("my private key: ");
		myPrivateKeyLbl.setFont(ResLoader.getDefaultFont());
		keypairPnl.add(myPrivateKeyLbl);
		
		myPrivateKeyField = new JTextField();
		myPrivateKeyField.setFont(ResLoader.getDefaultFont());
		keypairPnl.add(myPrivateKeyField, "wrap, width 100:500");
		
		JLabel myPublicKeyLbl = new JLabel("my public key: ");
		myPublicKeyLbl.setFont(ResLoader.getDefaultFont());
		keypairPnl.add(myPublicKeyLbl);
		
		myPublicKeyField = new JTextField();
		myPublicKeyField.setFont(ResLoader.getDefaultFont());
		keypairPnl.add(myPublicKeyField, "wrap, width 100:500");
		
		generateNowBtn = new JButton("generate now");
		generateNowBtn.setFont(ResLoader.getDefaultFont());
		keypairPnl.add(generateNowBtn, "gaptop 14");
		
		openKeypairBtn = new JButton("open keypair...");
		openKeypairBtn.setFont(ResLoader.getDefaultFont());
		frameBtnPnl.add(openKeypairBtn);
		
		saveKeypairBtn = new JButton("save keypair...");
		saveKeypairBtn.setFont(ResLoader.getDefaultFont());
		frameBtnPnl.add(saveKeypairBtn);
	}
	
	private void initPanels()
	{
		keypairPnl = new JPanel(new MigLayout());
		keypairPnl.setBorder(BorderFactory.createTitledBorder("keypair"));
		((TitledBorder) keypairPnl.getBorder()).setTitleFont(ResLoader.getBoldFont());
		add(keypairPnl, "wrap, width 450:5000");
		
		frameBtnPnl = new JPanel(new MigLayout());
		frameBtnPnl.setFont(ResLoader.getDefaultFont());
		add(frameBtnPnl, "align right, newline push");
	}
}
