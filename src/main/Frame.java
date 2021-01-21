package main;

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
		this.setSize(w+20, h+45);
		this.setVisible(false);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
	}
	@Override
	public void paint(Graphics g)
	{
		Generator.render(g);
	}
}
