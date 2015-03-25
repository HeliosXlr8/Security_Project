package main;

import javax.swing.JOptionPane;
import javax.swing.UIManager;

import gui.MainWindow;

public class Main
{
	public static void main(String[] args)
	{
		setLookAndFeel();
		ResLoader.LoadResources();
		new MainWindow();
	}
	
	private static void setLookAndFeel()
	{
		try
		{
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
			JOptionPane.showMessageDialog(null, ex, "An error occurred", JOptionPane.ERROR_MESSAGE);
		}
	}
}
