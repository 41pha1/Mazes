package main;

public class Path 
{
	public Path parent;
	public int x, y;
	public Path(int x, int y, Path parent) 
	{
		this.x = x;
		this.y = y;
		this.parent = parent;
	}
}
