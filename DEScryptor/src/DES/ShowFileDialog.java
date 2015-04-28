package DES;

import java.awt.FileDialog;

import javax.swing.JFrame;

public class ShowFileDialog extends JFrame {
	private FileDialog fc;

	public ShowFileDialog() {
		super("Kies de file die u wilt encrypteren.");
		setSize(200, 200);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
	}

	public String getFile() {

		String username, returnString;
		username = System.getProperty("user.name");
		
		fc = new FileDialog(this, "Please select a file", FileDialog.LOAD);
		fc.setDirectory("C:\\Users\\" + username + "\\Documents");
		fc.setVisible(true);
		
		System.out.println(fc.getFile());
		
		if(fc.getFile() != null){
			returnString = fc.getDirectory() + fc.getFile();
		}else{
			returnString = null;
		}
		
		System.out.println(returnString);
		return returnString;
	}
}