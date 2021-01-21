package main;

import java.awt.Graphics;

public class Tile 
{
	public int dir;
	public Tile(int dir,int x,int y) 
	{
		setDir(dir,x,y);
	}
	public Tile() 
	{
		dir = -1;
	}
	public void setDir(int dir, int x ,int y) 
	{
		switch(dir) 
		{
			case 0: 
			{
				if(getDirOfTile(x,y-1) == 2) setDir((int)(Math.random()*4),x,y);
				else this.dir = dir;break;
			}
			case 1: 
			{
				if(getDirOfTile(x+1,y) == 3) setDir((int)(Math.random()*4),x,y);
				else this.dir = dir;;break;
			}
			case 2: 
			{
				if(getDirOfTile(x,y+1) == 0) setDir((int)(Math.random()*4),x,y);
				else this.dir = dir;break;
			}
			case 3: 
			{
				if(getDirOfTile(x-1,y) == 1) setDir((int)(Math.random()*4),x,y);
				else this.dir = dir;break;
			}
		}
	}
	public int getDirOfTile(int x, int y) 
	{
		if(x < 0 || y < 0 || x>=Generator.width || y>=Generator.height)return -1;
		return Generator.tiles[x][y].dir;
	}
	public void render(Graphics g, int x, int y, int w, int h, int xs, int ys) 
	{
		float xw = (float)xs/(float)w;
		float xp = (float)xw*(float)x;
		float yh = (float)ys/(float)h;
		float yp = (float)yh*(float)y;
		switch(dir) 
		{
			case 0: g.drawLine((int)xp, (int)yp, (int)xp, (int)(yp-yh));break;
			case 1: g.drawLine((int)xp, (int)yp, (int)(xp+xw), (int)yp);break;
			case 2: g.drawLine((int)xp, (int)yp, (int)xp, (int)(yp+yh));break;
			case 3: g.drawLine((int)xp, (int)yp, (int)(xp-xw), (int)yp);break;
		}
	}
}
