package solver;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import main.Path;

public class Main
{
	static BufferedImage maze;
	static boolean[][] map;
	static Frame f;
	static ArrayList<Path> solvedPath;
	static Path[][] testArea;
	static int[][] d;
	static BufferedImage screen;
	static String FileName = "Maze100x100";
	static int sx, sy, dx, dy;
	static boolean b = true;

	public static void main(String[] args) throws IOException
	{
		solvedPath = new ArrayList<Path>();
		maze = ImageIO.read(new File(FileName + ".png"));
		d = new int[maze.getWidth() - 2][maze.getWidth() - 2];
		int w = maze.getWidth() - 2;
		int h = maze.getHeight() - 2;
		sx = w;
		sy = h;
		dx = 1;
		dy = 1;
		testArea = new Path[maze.getWidth()][maze.getHeight()];
		f = new Frame(maze.getWidth() + 20, maze.getHeight() + 45);
		screen = new BufferedImage(f.getWidth(), f.getHeight(), BufferedImage.TYPE_INT_RGB);
		map = new boolean[maze.getWidth()][maze.getHeight()];
		for (int i = 0; i < maze.getWidth(); i++)
		{
			for (int j = 0; j < maze.getHeight(); j++)
			{
				map[i][j] = new Color(maze.getRGB(i, j)).getBlue() == 0;
			}
		}
		solvedPath = solve();
		b = false;
		f.repaint();
		try
		{
			Thread.sleep(1000);
		} catch (InterruptedException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// ImageIO.write(screen, "png", new File(FileName+"Solved.png"));
		// System.out.println("Succesfully saved image!");
	}

	public static ArrayList<Path> solve()
	{
		ArrayList<Path> recent = new ArrayList<Path>();
		testArea[sx][sy] = new Path(sx, sy, null);
		recent.add(testArea[sx][sy]);
		boolean solved = false;
		while (!solved)
		{
			ArrayList<Path> current = new ArrayList<Path>();
			for (int i = 0; i < recent.size(); i++)
			{

				if ((!map[recent.get(i).x][recent.get(i).y - 1]) && (testArea[recent.get(i).x][recent.get(i).y - 1] == null))
				{
					Path p = new Path(recent.get(i).x, recent.get(i).y - 1, recent.get(i));
					current.add(p);
					testArea[p.x][p.y] = p;
				}
				if ((!map[recent.get(i).x + 1][recent.get(i).y]) && (testArea[recent.get(i).x + 1][recent.get(i).y] == null))
				{
					Path p = new Path(recent.get(i).x + 1, recent.get(i).y, recent.get(i));
					current.add(p);
					testArea[p.x][p.y] = p;
				}
				if ((!map[recent.get(i).x][recent.get(i).y + 1]) && (testArea[recent.get(i).x][recent.get(i).y + 1] == null))
				{
					Path p = new Path(recent.get(i).x, recent.get(i).y + 1, recent.get(i));
					current.add(p);
					testArea[p.x][p.y] = p;
				}
				if ((!map[recent.get(i).x - 1][recent.get(i).y]) && (testArea[recent.get(i).x - 1][recent.get(i).y] == null))
				{
					Path p = new Path(recent.get(i).x - 1, recent.get(i).y, recent.get(i));
					current.add(p);
					testArea[p.x][p.y] = p;
				}
				// if((!map[recent.get(i).x+1][recent.get(i).y+1])&&(testArea[recent.get(i).x+1][recent.get(i).y+1]==null))
				// {
				// Path p = new Path(recent.get(i).x+1, recent.get(i).y+1, recent.get(i));
				// current.add(p);
				// testArea[p.x][p.y]= p;
				// }
				// if((!map[recent.get(i).x-1][recent.get(i).y-1])&&(testArea[recent.get(i).x-1][recent.get(i).y-1]==null))
				// {
				// Path p = new Path(recent.get(i).x-1, recent.get(i).y-1, recent.get(i));
				// current.add(p);
				// testArea[p.x][p.y]= p;
				// }
				// if((!map[recent.get(i).x-1][recent.get(i).y+1])&&(testArea[recent.get(i).x-1][recent.get(i).y+1]==null))
				// {
				// Path p = new Path(recent.get(i).x-1, recent.get(i).y+1, recent.get(i));
				// current.add(p);
				// testArea[p.x][p.y]= p;
				// }
				// if((!map[recent.get(i).x+1][recent.get(i).y-1])&&(testArea[recent.get(i).x+1][recent.get(i).y-1]==null))
				// {
				// Path p = new Path(recent.get(i).x+1, recent.get(i).y-1, recent.get(i));
				// current.add(p);
				// testArea[p.x][p.y]= p;
				// }
				// f.repaint();
				// for(int j = 0; j < 100000; j++);
			}

			recent = new ArrayList<Path>();
			if (current.size() == 0)
			{
				System.err.println("Unsolveable");
				/**/solved = true;
			}
			for (Path p : current)
			{
				// if(p.x == dx && p.y == dy)
				// {
				// solved=true;
				// break;
				// }
				recent.add(p);
			}
		}
		for (int i = 1; i < maze.getWidth() - 2; i++)
		{
			System.out.println(i);
			f.repaint();
			for (int j = 1; j < maze.getHeight() - 2; j++)
			{
				ArrayList<Path> path = new ArrayList<Path>();
				boolean backtracked = false;
				path.add(testArea[i][j]);
				while (!backtracked)
				{
					if (map[i][j])
					{
						path.clear();
						break;
					}
					if (path.get(path.size() - 1) == null)
					{
						path.clear();
						break;
					}
					Path p = path.get(path.size() - 1).parent;
					if (p == null)
					{
						path.clear();
						break;
					}
					path.add(testArea[p.x][p.y]);
					if (path.get(path.size() - 1).x == sx && path.get(path.size() - 1).y == sy)
						backtracked = true;
				}
				d[i][j] = path.size();
			}
		}
		ArrayList<Path> path = new ArrayList<Path>();
		boolean backtracked = false;
		path.add(testArea[dx][dy]);
		while (!backtracked)
		{
			Path p = path.get(path.size() - 1).parent;
			path.add(testArea[p.x][p.y]);
			if (path.get(path.size() - 1).x == sx && path.get(path.size() - 1).y == sy)
				backtracked = true;
		}
		System.out.println("Pathlength: " + path.size());
		return path;
	}

	public static void render(Graphics fin)
	{
		Graphics g = screen.getGraphics();
		g.setColor(new Color(230, 230, 230));
		g.fillRect(0, 0, maze.getWidth() * 2, maze.getHeight() * 2);
		g.setColor(Color.BLACK);
		for (int i = 0; i < maze.getWidth(); i++)
		{
			for (int j = 0; j < maze.getHeight(); j++)
			{
				if (map[i][j])
					g.drawLine(10 + i, 35 + j, 10 + i, 35 + j);
			}
		}
		if (b)
		{
			g.setColor(Color.GRAY);
			for (int i = 0; i < maze.getWidth(); i++)
			{
				for (int j = 0; j < maze.getHeight(); j++)
				{
					if (testArea[i][j] != null)
					{
						g.drawLine(10 + i, 35 + j, 10 + i, 35 + j);
					}

				}
			}
		}
		for (int i = 1; i < maze.getWidth() - 2; i++)
		{
			for (int j = 1; j < maze.getHeight() - 2; j++)
			{
				if (d[i][j] != 0)
				{
					int c = d[i][j] / 30;
					c = c % 255;
					g.setColor(Color.getHSBColor(c / 255f, 0.8f, 0.8f));
					g.drawLine(10 + i, 35 + j, 10 + i, 35 + j);
				}

			}
		}
		g.setColor(Color.RED);
		for (Path p : solvedPath)
		{
			g.fillRect(10 + p.x, 35 + p.y, 1, 1);
		}
		fin.drawImage(screen, 0, 0, null);
	}
}
