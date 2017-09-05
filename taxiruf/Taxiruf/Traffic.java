package Taxiruf;

import java.util.ArrayList;

public class Traffic implements Runnable
{
	volatile static ArrayList<Light> lights = new ArrayList<Light>(); 
	
	public boolean repOK()
	{
		/* Requires:none
		 * Modifies:none
		 * Effects:examine invariance
		 *         if any light in lights meets invariance,return true
		 *         else,return false */
		for(int i=0; i<lights.size(); i++)
		{
			if(!lights.get(i).repOK())
			{
				return false;
			}
		}
		return true;
	}
	
	static Light findLight(int loc_i,int loc_j)
	{
		/* Requires:0=<loc_i<80
		 * 			0=<loc_j<80
		 * Modifies:none
		 * Effects:find and return the traffic light located at [loc_i,loc_j] */
		for(Light light_temp:lights)
		{
			if(light_temp.loc.i==loc_i && light_temp.loc.j==loc_j)
			{
				return light_temp;
			}
		}
		return null;
	}

	static int shapeRoad(int i_temp,int j_temp)
	{
		/* Requires:0=<i_temp<80
		 * 			0=<j_temp<80
		 * Modifies:none
		 * Effects:set connects as the number of edges connected and return connects */
		int connects = 0;
		if(i_temp-1>=0 && BFS.adj_matrix[i_temp*80+j_temp][(i_temp-1)*80+j_temp]==1)//up
		{
			connects++;
		}
		if(i_temp+1<80 && BFS.adj_matrix[i_temp*80+j_temp][(i_temp+1)*80+j_temp]==1)//down
		{
			connects++;
		}
		if(j_temp-1>=0 && BFS.adj_matrix[i_temp*80+j_temp-1][i_temp*80+j_temp]==1)//left
		{
			connects++;
		}
		if(j_temp+1<80 && BFS.adj_matrix[i_temp*80+j_temp+1][i_temp*80+j_temp]==1)//right
		{
			connects++;
		}
		return connects;
	}
	
	static void setLight()
	{
		/* Requires:none
		 * Modifies:lights
		 * Effects:according to Map.road_layout and shapeRoad(...),set traffic lights properly
		 * 		   only if there is a road intersects on a plane,a traffic light can be set */
		for(int i=0; i<80; i++)
		{
			for(int j=0; j<80; j++)
			{
				if(Map.road_layout[i][j]==1 && shapeRoad(i,j)>2)
				{
					Light light_temp = new Light(i,j);
					lights.add(light_temp);
				}
			}
		}
	}
	
	@Override
	public void run()
	{
		try
		{
			while(!Main.end)
			{
				Thread.sleep(300);
				for(int i=0; i<lights.size(); i++)
				{
					lights.get(i).changeColor();
				}
			}
		}
		catch(Exception e)
		{
			System.out.println("Sorry to catch Exception!");
		}	
	}
}
