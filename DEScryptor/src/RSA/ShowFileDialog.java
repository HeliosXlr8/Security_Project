package RSA;

import java.awt.Button;
import java.awt.FileDialog;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JOptionPane;

public class ShowFileDialog extends Frame {
	private FileDialog fc;

	public ShowFileDialog() {
		super("FileDialog");
		setSize(200, 200);
	}

	public String getFile() {

		fc = new FileDialog(this, "Please select a valid DES Key",
				FileDialog.LOAD);
		fc.setDirectory("C:\\");
		fc.setVisible(true);
		
		String fn = fc.getFile();
		String returnString = fc.getDirectory() + fc.getFile();
		
		if (fn == null) {
			JOptionPane.showMessageDialog(this,
					"You must select a valid file to proceed...!",
					"Wrong File!", JOptionPane.ERROR_MESSAGE);
			return "";
		} else {
			return returnString;
		}
	}
}
