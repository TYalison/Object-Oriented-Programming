package Taxiruf;

public class BFS
{
	volatile static int[][] adj_matrix = new int[6400][6400];
	volatile static int[][] adj_const = new int[6400][6400];

	public boolean repOK()
	{
		/* Requires:none
		 * Modifies:none
		 * Effects:examine invariance
		 *		   when 0=<i<6400 and 0=<j<6400 and adj_matrix[i][j]>=0 and adj_const[i][j],return true
		 *		   otherwise,return false */
		for(int i=0; i<6400; i++)
		{
			for(int j=0; j<6400; j++)
			{
				if(adj_matrix[i][j] < 0 || adj_const[i][j] < 0)
				{
					return false;
				}
			}
		}
		return true;
	}
	
	static void setMatrix()
	{
		/* Requires:none
		 * Modifies:adj_matrix
		 * Effects:according to city_map,set adj_matrix
		 * 		   if i equals j,set adj_matrix[i][j] as 0
		 * 		   else if i is connected to j,set adj_matrix[i][j] as 1
		 * 		   else,set adj_matrix[i][j] as Main.MAX_INT
		 * 		   the same as adj_const */
		for(int i=0; i<6400; i++)
		{
			for(int j=0; j<6400; j++)
			{
				if(i == j)
				{
					adj_matrix[i][j] = 0;
				}
				else
				{
					adj_matrix[i][j] = Main.MAX_INT;
				}
			}
		}
		for(int i=0; i<80; i++)
		{
			for(int j=0; j<80; j++)
			{
				switch(Map.city_map[i][j])
				{
				case 0 :
					break;
				case 1 :
					if(i*80+j+1 < 6400)
					{
						adj_matrix[i*80+j][i*80+j+1] = 1;
						adj_matrix[i*80+j+1][i*80+j] = 1;
					}
					break;
				case 2 :
					if((i+1)*80+j < 6400)
					{
						adj_matrix[(i+1)*80+j][i*80+j] = 1;
						adj_matrix[i*80+j][(i+1)*80+j] = 1;
					}
					break;
				case 3 :
					if((i+1)*80+j < 6400 && i*80+j+1 < 6400)
					{
						adj_matrix[i*80+j][i*80+j+1] = 1;
						adj_matrix[i*80+j+1][i*80+j] = 1;
						adj_matrix[(i+1)*80+j][i*80+j] = 1;
						adj_matrix[i*80+j][(i+1)*80+j] = 1;
					}
					break;
				}
			}
		}
		adj_const = adj_matrix;
	}

	static void setShortest(int v_begin,int[] shortest_path,int[][] adj_temp)
	{
		/* Requires:v_begin is an integer and 0=<v_begin<6400
		 *          shortest_path.length>=6400
		 * Modifies:none
		 * Effects:shortest_path[i] represents the closest vertex to i along the shortest path between v_begin and i */
		int[] visited = new int[6400];
		int[] dist = new int[6400];
		for(int i=0; i<6400; i++)
		{
			shortest_path[i] = -1;
			dist[i] = adj_temp[v_begin][i];
		}
		visited[v_begin] = 1;
		int mark = 0;
		for(int i=0; i<6400; i++)
		{
			if(visited[i]==0 && adj_temp[v_begin][i]!=Main.MAX_INT)
			{
				shortest_path[i] = v_begin;
			}	
		}
		for(int i=0; i<6400; i++)
		{
			int temp = Main.MAX_INT;
			for(int j=0; j<6400; j++)
			{
				if(visited[j]==0 && dist[j]<temp)
				{
					temp = dist[j];
					mark = j;
				}
			}
			visited[mark] = 1;
			for(int j=0; j<6400; j++)
			{
				if(visited[j]==0 && adj_temp[mark][j]+dist[mark]<dist[j])
				{
					dist[j] = adj_temp[mark][j] + dist[mark];
					shortest_path[j] = mark;
				}
			}
		}
	}
	
	static int getShortest(int v_begin,int v_end,int[][] adj_temp)
	{
		/* Requires:v_begin is an integer and 0=<v_begin<6400
		 *          v_end is an integer and 0=<v_end<6400
		 * Modifies:none
		 * Effects:return the shortest length from v_begin to v_end
		 * 		   specially,returning Main.MAX_INT suggests that v_begin is not connected to v_end */
		int[] visited = new int[6400];
		int[] dist = new int[6400];
		for(int i=0; i<6400; i++)
		{
			dist[i] = adj_temp[v_begin][i];
		}
		visited[v_begin] = 1;
		int mark = 0;
		for(int i=0; i<6400; i++)
		{
			int temp = Main.MAX_INT;
			for(int j=0; j<6400; j++)
			{
				if(visited[j]==0 && dist[j]<temp)
				{
					temp = dist[j];
					mark = j;
				}
			}
			visited[mark] = 1;
			for(int j=0; j<6400; j++)
			{
				if(visited[j]==0 && adj_temp[mark][j]+dist[mark]<dist[j])
				{
					dist[j] = adj_temp[mark][j] + dist[mark];
				}
			}
		}
		return dist[v_end];
	}
}

