package Taxiruf;

public class Taxi implements Runnable
{
	volatile int id;
	volatile String status = "wait";//wait || soon || stop || serve
	volatile Coordinate loc;//location
	volatile Coordinate des;
	volatile int credit = 0;
	volatile Passenger passenger = null;
	volatile boolean meet_light = false;
	boolean isTracker = false;
	
	public boolean repOK()
	{
		/* Requires:none
		 * Modifies:none
		 * Effects:examine invariance
		 *		   when 0<id<=100 and status{"wait","soon","stop","serve"} and loc(80*80) and des(80*80) and credit>=0,return true
		 *		   otherwise,return false */
		if(id<=0 || id>100)
		{
			return false;
		}
		if(!(status.equals("wait") || status.equals("soon") || status.equals("stop") || status.equals("serve")))
		{
			return false;
		}
		if(!loc.insideScope() || !des.insideScope())
		{
			return false;
		}
		if(credit < 0)
		{
			return false;
		}
		return true;
	}
	
	Taxi(int id_temp)
	{
		/* Requires:id_temp is an integer and 0<id_temp<=100
		 * Modifies:id
		 * 			loc
		 * Effects:initialize a new taxi
		 * 		   set its id as id_temp
		 * 		   set its initial location randomly within city_map
		 *  */
		id = id_temp;
		loc = new Coordinate(Main.rand.nextInt(80),Main.rand.nextInt(80));
	}

	boolean leaveLight(Light light_temp,int last_direction,int direction)
	{
		/* Requires:light_temp is existent and 0<=direction<3
		 * Modifies:none
		 * Effects:when taxi is going straight or turning left or turning around
		 *		   return true if it meets "green"
		 *		   return false if it meets "red" */
		switch(last_direction)
		{
		case 0 ://face up
			if(direction==3 || light_temp.status_NS.equals("green"))
			{
				return true;
			}
			break;
		case 1 ://face down
			if(direction==2 || light_temp.status_NS.equals("green"))
			{
				return true;
			}
			break;
		case 2 ://face left
			if(direction==0 || light_temp.status_WE.equals("green"))
			{
				return true;
			}
			break;
		case 3 ://face right
			if(direction==1 || light_temp.status_WE.equals("green"))
			{
				return true;
			}
			break;
		}
		return false;
	}
	
	void driveTaxi()
	{
		/* Requires:none
		 * Modifies:BFS.flows
		 * 			loc
		 * 			status
		 * 			credit
		 * 			des
		 * 			passenger
		 * Effects:while status is "soon" or "serve",choose the shortest path with minimum flow to drive along every 100ms
		 * 		   while status is "wait",choose the path with minimum flow randomly to drive along every 100ms
		 * 		   if status is "soon" and having arrived at passenger's location,set status as "stop" and stop for 1s,then set status as "serve"
		 * 		   else if status is "soon" and having arrived at passenger's destination,set its status as "stop" and stop for 1s
		 * 		   else if status is "wait" and lasting for 20s,set its status as "stop" and stop for 1s
		 * 		   else,set status as "wait" */
		try
		{
			long time = System.currentTimeMillis();
			int last_direction = 0;
			while(System.currentTimeMillis()-time < 20000 && status.equals("wait"))
			{
				int direction = -1;
				int flow_min = Main.MAX_INT;
				if((loc.i-1)>=0 && BFS.adj_matrix[(loc.i-1)*80+loc.j][loc.i*80+loc.j]==1 && Flow.flows[loc.i-1][loc.j][1] < flow_min)
				{
					direction = 0;//up
					flow_min = Flow.flows[loc.i-1][loc.j][1];
				}
				if((loc.i+1)<80 && BFS.adj_matrix[(loc.i+1)*80+loc.j][loc.i*80+loc.j]==1 && Flow.flows[loc.i][loc.j][1] < flow_min)
				{	
					direction = 1;//down
					flow_min = Flow.flows[loc.i][loc.j][1];
				}
				if((loc.j-1)>=0 && BFS.adj_matrix[loc.i*80+loc.j][loc.i*80+loc.j-1]==1 && Flow.flows[loc.i][loc.j-1][0] < flow_min)
				{	
					direction = 2;//left
					flow_min = Flow.flows[loc.i][loc.j-1][0];
				}
				if((loc.j+1)<80 && BFS.adj_matrix[loc.i*80+loc.j][loc.i*80+loc.j+1]==1 && Flow.flows[loc.i][loc.j][0] < flow_min)
				{
					direction = 3;//right
					flow_min = Flow.flows[loc.i][loc.j][0];
				}
				Light light = Traffic.findLight(loc.i,loc.j);
				if(light!=null)
				{
					while(!leaveLight(light,last_direction,direction))
					{
						meet_light = true;
						Thread.sleep(100);
					}
					meet_light = false;
				}
				switch(direction)
				{
				case 0 ://up
					Flow.flows[loc.i-1][loc.j][1]++;
					loc.i -= 1;
					break;
				case 1 ://down
					Flow.flows[loc.i][loc.j][1]++;
					loc.i += 1;
					break;
				case 2 ://left
					Flow.flows[loc.i][loc.j-1][0]++;
					loc.j -= 1;
					break;
				case 3 ://right
					Flow.flows[loc.i][loc.j][0]++;
					loc.j += 1;
					break;
				}
				Thread.sleep(100);
				last_direction = direction;
			}
			if(status.equals("wait"))
			{
				status = "stop";
				Thread.sleep(1000);//stop
				status = "wait";
			}
			else
			{
				transformStatus();
			}
		}
		catch(Exception e)
		{
			System.out.println("Sorry to catch Exception!");
		}
	}

	void doService(int loc_temp,int des_temp)
	{
		/* Requires: des_temp is an integer and 0=<des_temp<6400
		 * Modifies:loc
		 * Effects: according to the shortest path,drive across one road every 100ms from loc_temp and print out its location
		 * 			when having arrived at des_temp,print it out */
		try
		{
			int last_direction = 0;
			int[] shortest_path = new int[6400];
			int j_current = loc_temp % 80;
			int i_current = loc_temp / 80;
			BFS.setShortest(des_temp,shortest_path,BFS.adj_matrix);
			int temp = shortest_path[loc_temp];
			while(temp != -1)
			{
				int length_min = BFS.getShortest(temp, des_temp,BFS.adj_matrix);
				int direction = -1;
				int flow_min = Main.MAX_INT;
				if((i_current-1)>=0 && BFS.adj_matrix[(i_current-1)*80+j_current][i_current*80+j_current]==1 && Flow.flows[i_current-1][j_current][1]<flow_min)
				{
					int length_temp = BFS.getShortest((i_current-1)*80+j_current, des_temp, BFS.adj_matrix);
					if(length_temp <= length_min)
					{
						direction = 0;//up
						flow_min = Flow.flows[i_current-1][j_current][1];
						length_min = length_temp;
					}
				}
				if((i_current+1)<80 && BFS.adj_matrix[(i_current+1)*80+j_current][i_current*80+j_current]==1 && Flow.flows[i_current][j_current][1]<flow_min)
				{
					int length_temp = BFS.getShortest((i_current+1)*80+j_current, des_temp, BFS.adj_matrix);
					if(length_temp <= length_min)
					{
						direction = 1;//down
						flow_min = Flow.flows[i_current][j_current][1];
						length_min = length_temp;
					}	
				}
				if((j_current-1)>=0 && BFS.adj_matrix[i_current*80+j_current-1][i_current*80+j_current]==1 && Flow.flows[i_current][j_current-1][0]<flow_min)
				{	
					int length_temp = BFS.getShortest(i_current*80+j_current-1, des_temp, BFS.adj_matrix);
					if(length_temp <= length_min)
					{
						direction = 2;//left
						flow_min = Flow.flows[i_current][j_current-1][0];
						length_min = length_temp;
					}
					
				}
				if((j_current+1)<80 && BFS.adj_matrix[i_current*80+j_current+1][i_current*80+j_current]==1 && Flow.flows[i_current][j_current][0]<flow_min)
				{
					int length_temp = BFS.getShortest(i_current*80+j_current+1, des_temp, BFS.adj_matrix);
					if(length_temp <= length_min)
					{
						direction = 3;//right
						flow_min = Flow.flows[i_current][j_current][0];
						length_min = length_temp;
					}	
				}
				Light light = Traffic.findLight(i_current,j_current);
				if(light != null)
				{
					boolean first = true;
					while(!leaveLight(light,last_direction,direction))
					{
						meet_light = true;
						if(first)
						{
							System.out.println("Taxi-" + id + loc + ": Waiting at a traffic light");
							first = false;
						}
						Thread.sleep(100);
					}
					meet_light = false;
					System.out.println("Taxi-" + id + loc + ": Going through a traffic light");
				}
				switch(direction)
				{
				case 0 ://up
					Flow.flows[i_current-1][j_current][1]++;
					i_current -= 1;
					break;
				case 1 ://down
					Flow.flows[i_current][j_current][1]++;
					i_current += 1;
					break;
				case 2 ://left
					Flow.flows[i_current][j_current-1][0]++;
					j_current -= 1;
					break;
				case 3 ://right
					Flow.flows[i_current][j_current][0]++;
					j_current += 1;
					break;
				}
				Thread.sleep(100);
				last_direction = direction;
				loc = new Coordinate(i_current,j_current);
				System.out.println("Taxi-" + id + loc);
				temp = shortest_path[i_current*80+j_current];
			}		
			System.out.println("Passenger" + passenger.loc + passenger.des + ": Taxi-" + id + " arrives at " + des);
		}
		catch(Exception e)
		{
			System.out.println("Sorry to catch Exception!");;
		}
	}

	void transformStatus()
	{
		/* Requires:none
		 * Modifies:passenger
		 * 			status
		 * 			des
		 * 			loc
		 * 			credit
		 * Effects:according to the shortest path,drive across one road every 100ms from loc to passenger's des
		 * 		   when having arrived at des,credit plus 3
		 * 		   reset status as "wait" and passenger as null */
		try
		{
			if(passenger != null && status.equals("soon"))
			{
				des = passenger.loc;
				doService(loc.i*80+loc.j,des.i*80+des.j);
				status = "stop";
				Thread.sleep(1000);
				status = "serve";
				des = passenger.des;
				doService(loc.i*80+loc.j,des.i*80+des.j);
				status = "stop";
				Thread.sleep(1000);
				credit += 3;
				//refresh:
				status = "wait";
				passenger = null;
			}
		}
		catch(Exception e)
		{
			System.out.println("Sorry to catch Exception!");
		}
	}
	
	@Override
	public void run()
	{
		try
		{
			while(!Request.passengers.isEmpty() || !Main.end)
			{
				driveTaxi();
			}
		}
		catch(Exception e)
		{
			System.out.print("Sorry to catch Exception!");
		}
	}
}
