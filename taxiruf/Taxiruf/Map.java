package Taxiruf;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.Reader;

public class Map
{
	static int[][] city_map = new int[80][80];
	static int[][] road_layout = new int[80][80];
	
	public boolean repOK()
	{
		/* Requires:none
		 * Modifies:none
		 * Effects:examine invariance
		 *		   when 0=<i<80 and 0=<j<80 and city_map[i][j]{0,1,2,3} and road_layout[i][j]{0,1},return true
		 *		   otherwise,return false */
		for(int i=0; i<80; i++)
		{
			for(int j=0; j<80; j++)
			{
				if(city_map[i][j]<0 || city_map[i][j]>3)
				{
					return false;
				}
				if(road_layout[i][j]!=0 && road_layout[i][j]!=1)
				{
					return false;
				}
			}
		}
		return true;
	}
	
	static void readMap(String file_path)
	{
		/* Requires:file_path should be valid path of a file.txt
		 * Modifies:city_map
		 * Effects:if file_path is invalid then print "Unable to read city_map!"
		 * 		   else,read map successfully */
		try
		{
			File file = new File(file_path);
			Reader reader = null;
			if(file.exists() && file.canRead())
			{
				reader = new InputStreamReader(new FileInputStream(file));
				int char_temp;
				for(int i=0; i<80; i++)
				{
					for(int j=0; j<80; j++)
					{
						char_temp = reader.read();
						if(char_temp != -1 && (char)char_temp >= '0' && (char)char_temp <= '3')
						{
							city_map[i][j] = Integer.parseInt(((char)char_temp)+"");
						}
						else
						{
							j--;
						}
					}
				}
				System.out.println("Read city_map!");
			}
			else
			{
				System.out.println("Unable to read city_map!");
			}
		}
		catch(Exception e)
		{
			System.out.println("Sorry to catch exception!");
		}
	}
	
	static void readRoad(String file_path)
	{
		/* Requires:file_path should be valid path of a file.txt
		 * Modifies:road_layout
		 * Effects:if file_path is invalid then print "Unable to read road_layout!"
		 * 		   else,set road successfully */
		try
		{
			File file = new File(file_path);
			Reader reader = null;
			if(file.exists() && file.canRead())
			{
				reader = new InputStreamReader(new FileInputStream(file));
				int char_temp;
				for(int i=0; i<80; i++)
				{
					for(int j=0; j<80; j++)
					{
						char_temp = reader.read();
						if(char_temp != -1 && ((char)char_temp == '0' || (char)char_temp == '1'))
						{
							road_layout[i][j] = Integer.parseInt(((char)char_temp)+"");
						}
						else
						{
							j--;
						}
					}
				}
				System.out.println("Read road_layout!");
			}
			else
			{
				System.out.println("Unable to read road_layout!");
			}
		}
		catch(Exception e)
		{
			System.out.println("Sorry to catch exception!");
		}
	}
}