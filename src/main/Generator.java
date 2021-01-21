package main;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.SynchronousQueue;

import javax.imageio.ImageIO;

public class Generator 
{
	public static Tile[][] tiles;
	public static BufferedImage maze;
	public static boolean[][] map;
	public static int width,height,xsize,ysize;
	public static boolean[][] ils;
	public static Coord[][] testArea;
	public static ArrayList<Coord> recent, current;
	public static String name;
	public static void generate(int w, int h, int xs, int ys, String name) 
	{
		Generator.name = name;
		width = w;
		height = h;
		xsize = xs;
		ysize = ys;
		tiles  = new Tile[w][h];
		testArea = new Coord[w*2+1][h*2+1];
		recent = new ArrayList<Coord>();
		current = new ArrayList<Coord>();
		for(int x = 0; x < w; x++) 
		{
			for(int y = 0; y < h; y++) 
			{
				tiles[x][y] = new Tile();
			}
		}
		maze = new BufferedImage(xs,ys,BufferedImage.TYPE_INT_RGB);
		for(int x = 1; x < h; x++) 
		{
			for(int y = 1; y < h; y++) 
			{
				int r = (int)(Math.random()*4);
				tiles[x][y] = new Tile(r,x,y);	
			}
		}
		System.out.println("generated maze");
		generateMap();
		ils = new boolean[Generator.width*2+1][Generator.height*2+1];
		for(int i = 0; i < Generator.width*2+1; i++) 
		{
			for(int j = 0; j < Generator.height*2+1; j++) 
			{
				ils[i][j] = true;
			}
		}
		System.out.println("generated map");
		System.out.println("searching for islands...");
		ArrayList<Path> islands = new ArrayList<Path>();
		for(int x = 0; x < width*2; x++) 
		{
			long started = System.currentTimeMillis();
			for(int y = 0; y < height*2; y++) 
			{
				if(isAnIsland((short)x, (short)y)) 
				{
					islands.add(new Path(x,y,null));
				}else 
				{
					if(!map[x][y])ils[x][y] = false;
				}
			}
			System.out.println("column: "+(x+1)+"/"+width*2+", checked "+(x+1)*height*2+" tiles ("+(System.currentTimeMillis()-started)+" millis)");
		}
		System.out.println("found "+islands.size()+" islands");
		System.out.println("removing islands");
		Main.f.setVisible(true);
		prepareMaze(true);
		Main.f.repaint();
		int k = 0;
		long time = System.currentTimeMillis();
		while(islands.size()>0) 
		{
			time = System.currentTimeMillis();
			for(Path p: islands) 
			{
				int r = (int)(Math.random()*5);
				if(r==0)tiles[p.x/2-1][p.y/2-1] = new Tile((int)(Math.random()*4),p.x/2-1,p.y/2-1);
				if(r==1)tiles[p.x/2-1][p.y/2] = new Tile((int)(Math.random()*4),p.x/2-1,p.y/2);
				if(r==2)tiles[p.x/2][p.y/2-1] = new Tile((int)(Math.random()*4),p.x/2,p.y/2-1);
				if(r==3)tiles[p.x/2][p.y/2] = new Tile((int)(Math.random()*4),p.x/2,p.y/2);
				if(r==4) 
				{
					r = (int)(Math.random()*4);
					try
					{
						if(r==0)tiles[p.x/2-2][p.y/2] = new Tile((int)(Math.random()*4),p.x/2-2,p.y/2);
						if(r==1)tiles[p.x/2][p.y/2+1] = new Tile((int)(Math.random()*4),p.x/2,p.y/2+1);
						if(r==2)tiles[p.x/2][p.y/2-2] = new Tile((int)(Math.random()*4),p.x/2,p.y/2-2);
						if(r==3)tiles[p.x/2+1][p.y/2] = new Tile((int)(Math.random()*4),p.x/2+1,p.y/2);
					}
					catch(ArrayIndexOutOfBoundsException e)
					{
						
					}
				}
			}
			islands = new ArrayList<Path>();
			for(int i = 0; i < Generator.width*2+1; i++) 
			{
				for(int j = 0; j < Generator.height*2+1; j++) 
				{
					ils[i][j] = true;
				}
			}
			//TODO
			generateMap();
			for(int x = 0; x < width*2; x++) 
			{
				for(int y = 0; y < height*2; y++) 
				{
					if(isAnIsland((short)x, (short)y)) 
					{
						islands.add(new Path(x,y,null));
					}else 
					{
						if(!map[x][y])ils[x][y] = false;
					}
				}
			}
			prepareMaze(true);
			Main.f.repaint();
			System.out.println("step "+(k+1)+", "+islands.size()+" islands left ("+(System.currentTimeMillis()-time)/1000+"s)");
			k++;
		}
	}
	public static boolean isAnIsland(short x, short y) 
	{
		if(map[x][y])return false;
		if(x<=1||y<=1||x>=width*2||y>=height*2)return false;
		if((!ils[x][y-1])||(!ils[x][y+1])||(!ils[x-1][y])||(!ils[x+1][y]))return false;
		for(int i = 0; i < width*2+1; i++) 
		{
			for(int j = 0; j < height*2+1; j++) 
			{
				testArea[i][j] = null;
			}
		}
		recent.clear();
		testArea[x][y] = new Coord(x,y);
		recent.add(testArea[x][y]);
		while(true) 
		{
			current.clear();
			for(int i = 0; i < recent.size(); i++) 
			{
				if((!map[recent.get(i).x][recent.get(i).y-1])&&(testArea[recent.get(i).x][recent.get(i).y-1]==null)) 
				{
					Coord p = new Coord(recent.get(i).x, (short) (recent.get(i).y-1));
					current.add(p);
					testArea[p.x][p.y]= p;
				}
				if((!map[recent.get(i).x+1][recent.get(i).y])&&(testArea[recent.get(i).x+1][recent.get(i).y]==null)) 
				{
					Coord p = new Coord((short) (recent.get(i).x+1), recent.get(i).y);
					current.add(p);
					testArea[p.x][p.y]= p;
				}
				if((!map[recent.get(i).x][recent.get(i).y+1])&&(testArea[recent.get(i).x][recent.get(i).y+1]==null)) 
				{
					Coord p = new Coord(recent.get(i).x, (short) (recent.get(i).y+1));
					current.add(p);
					testArea[p.x][p.y]= p;
				}
				if((!map[recent.get(i).x-1][recent.get(i).y])&&(testArea[recent.get(i).x-1][recent.get(i).y]==null)) 
				{
					Coord p = new Coord((short) (recent.get(i).x-1), recent.get(i).y);
					current.add(p);
					testArea[p.x][p.y]= p;
				}
			}
			recent.clear();
			for(Coord p: current) 
			{
				if(p.x<=1||p.y<=1||p.x>=width*2||p.y>=height*2)return false;
				if((!ils[p.x][p.y-1])||(!ils[p.x][p.y+1])||(!ils[p.x-1][p.y])||(!ils[p.x+1][p.y])) return false;
				recent.add(p);
			}
			if(recent.size()==0)return true;
		}
	}
	public static void generateMap() 
	{
		map = new boolean[width*2+1][height*2+1];
		for(int x = 1; x < height; x++) 
		{
			for(int y = 1; y < height; y++) 
			{
				map[x*2+1][y*2+1] = true; 
				if(tiles[x][y].dir == 0)map[x*2+1][y*2] = true;
				if(tiles[x][y].dir == 1)map[x*2+2][y*2+1] = true;
				if(tiles[x][y].dir == 2)map[x*2+1][y*2+2] = true;
				if(tiles[x][y].dir == 3)map[x*2][y*2+1] = true;
			}
		}
	}
	public static void prepareMaze(boolean b) 
	{
		Graphics g = maze.getGraphics();
		g.setColor(Color.white);
		g.fillRect(0, 0, xsize, ysize);
		g.setColor(Color.BLACK);
		g.drawRect(0, 0, xsize-1, ysize-1);
		float xw = (float)xsize/(float)width;
		float yh = (float)ysize/(float)height;
		for(int x = 0; x < width; x++) 
		{
			for(int y = 0; y < height; y++) 
			{
				tiles[x][y].render(g, x,y, width, height, xsize, ysize);
			}
		}
		if(b) 
		{
			for(int x = 0; x < width*2; x++) 
			{
				for(int y = 0; y < height*2; y++) 
				{
					g.setColor(Color.RED);
					float xp = (float)xw*(float)x;
					float yp = (float)yh*(float)y;
					xp/=2;
					yp/=2;
					if(!map[x][y]&&ils[x][y])g.drawRect((int)(xp-xw/2-xw/4), (int)(yp-yh/4-yh/2),(int)(xw/2),(int)(yh/2));
					g.setColor(Color.BLACK);
				}
			}
		}	
	}
	public static void render (Graphics fin) 
	{
		fin.drawImage(maze, 10, 35, Frame.w, Frame.h, null);
	}
	public static void save() 
	{
		try {
			ImageIO.write(maze, "png", new File(name+width+"x"+height+".png"));
			System.out.println("Succesfully saved image!");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
