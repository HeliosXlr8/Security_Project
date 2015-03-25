package main;

import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JOptionPane;

public class ResLoader
{
	private static GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
	
	private static Font defaultFont;
	private static Font boldFont;
	private static Font monoFont;
	
	private static BufferedImage appIcon;
	
	public static void LoadResources()
	{
		loadFont("res/Ubuntu.ttf");
		loadFont("res/UbuntuMono.ttf");
		
		loadImage("res/appIcon.png");
		
		initResources();
	}
	
	public static BufferedImage getAppIcon()
	{
		return appIcon;
	}
	
	public static Font getMonoFont()
	{
		return monoFont;
	}
	
	public static Font getBoldFont()
	{
		return boldFont;
	}
	
	public static Font getDefaultFont()
	{
		return defaultFont;
	}
	
	private static void initResources()
	{
		defaultFont = new Font("Ubuntu", Font.PLAIN, 24);
		boldFont = new Font("Ubuntu", Font.BOLD, 24);
		monoFont = new Font("Ubuntu Mono", Font.PLAIN, 24);
	}
	
	private static void loadFont(String pathName)
	{
		FileInputStream fontStream = null;
		
		try
		{
			fontStream = new FileInputStream(pathName);
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
			JOptionPane.showMessageDialog(null, ex, "An error occurred", JOptionPane.ERROR_MESSAGE);
		}

		Font font = null;
		try
		{
			font = Font.createFont(Font.TRUETYPE_FONT, fontStream);
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
			JOptionPane.showMessageDialog(null, ex, "An error occurred", JOptionPane.ERROR_MESSAGE);
		}
		
		if (font != null)
		{
			ge.registerFont(font);
		}
	}
	
	private static void loadImage(String pathName)
	{
		FileInputStream imgStream = null;
		try
		{
			imgStream = new FileInputStream(pathName);
		}
		catch (FileNotFoundException ex)
		{
			ex.printStackTrace();
			JOptionPane.showMessageDialog(null, ex, "An error occurred", JOptionPane.ERROR_MESSAGE);
		}
		
		BufferedImage img = null;
		try
		{
			img = ImageIO.read(imgStream);
		}
		catch (IOException ex)
		{
			ex.printStackTrace();
			JOptionPane.showMessageDialog(null, ex, "An error occurred", JOptionPane.ERROR_MESSAGE);
		}
		
		if (img != null)
		{
			appIcon = img;
		}
	}
}
