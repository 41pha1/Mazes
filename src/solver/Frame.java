package solver;

import java.awt.Graphics;

import javax.swing.JFrame;

@SuppressWarnings("serial")
public class Frame extends JFrame
{
	public static int w,h;
	public Frame (int width, int height) 
	{
		w = width;
		h = height;
		this.setSize(w, h);
		this.setVisible(true);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
	}
	@Override
	public void paint(Graphics g)
	{
		Main.render(g);
	}
}
