package main;

public class Main
{
	public static Frame f;

	public static void main(String[] args)
	{
		f = new Frame(1000, 1000);
		Generator.generate(100, 100, 1000, 1000, "Maze");
		Generator.prepareMaze(false);
		Generator.save();
		f.repaint();
	}
}
