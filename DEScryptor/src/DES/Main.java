package DES;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class Main {

	public static void main(String[] args) {
		KeuzeScherm panel = new KeuzeScherm();
		panel.setVisible(true);
		
		
		panel.addWindowListener(new WindowAdapter()
		{
		    public void windowClosing(WindowEvent e)
		    {
		        System.out.println("shutting down");
		        System.exit(0);
		    }
		});
		
		
		
	}
	
	

}
