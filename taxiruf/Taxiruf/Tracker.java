package Taxiruf;

import java.util.ArrayList;

public class Tracker extends Taxi
{	
	volatile int ser_times = 0;
	volatile ArrayList<Service> services = new ArrayList<Service>();
	
	public boolean repOK()
	{
		/* Requires:none
		 * Modifies:none
		 * Effects:examine invariance
		 *		   when 0<id<=100 and status{"wait","soon","stop","serve"} and loc(80*80) and des(80*80) and credit>=0 and isTracker and ser_times>=0,return true
		 *		   otherwise,return false */
		if(id<=0 || id>100)
		{
			return false;
		}
		if(!(status.equals("wait") || status.equals("soon") || status.equals("stop") || status.equals("serve")))
		{
			return false;
		}
		if(!loc.insideScope() || !des.insideScope() || !isTracker)
		{
			return false;
		}
		if(credit < 0 || ser_times < 0)
		{
			return false;
		}
		return true;
	}
	
	Tracker(int id_temp)
	{
		/* Requires:id_temp is an integer and 0<id_temp<=100
		 * Modifies:id
		 * 			loc
		 * 			isTracker
		 * Effects:initialize a new taxi
		 * 		   set its id as id_temp
		 * 		   set its initial location randomly within city_map
		 * 		   set its isTracker as true */
		super(id_temp);
		isTracker = true;
	}
	
	void driveTracker()
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
				if((loc.i-1)>=0 && BFS.adj_const[(loc.i-1)*80+loc.j][loc.i*80+loc.j]==1 && Flow.flows[loc.i-1][loc.j][1] < flow_min)
				{
					direction = 0;//up
					flow_min = Flow.flows[loc.i-1][loc.j][1];
				}
				if((loc.i+1)<80 && BFS.adj_const[(loc.i+1)*80+loc.j][loc.i*80+loc.j]==1 && Flow.flows[loc.i][loc.j][1] < flow_min)
				{	
					direction = 1;//down
					flow_min = Flow.flows[loc.i][loc.j][1];
				}
				if((loc.j-1)>=0 && BFS.adj_const[loc.i*80+loc.j][loc.i*80+loc.j-1]==1 && Flow.flows[loc.i][loc.j-1][0] < flow_min)
				{	
					direction = 2;//left
					flow_min = Flow.flows[loc.i][loc.j-1][0];
				}
				if((loc.j+1)<80 && BFS.adj_const[loc.i*80+loc.j][loc.i*80+loc.j+1]==1 && Flow.flows[loc.i][loc.j][0] < flow_min)
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
					if(BFS.adj_matrix[(loc.i-1)*80+loc.j][loc.i*80+loc.j] == 1)//open_road
					{
						Flow.flows[loc.i-1][loc.j][1]++;
					}
					loc.i -= 1;
					break;
				case 1 ://down
					if(BFS.adj_matrix[(loc.i+1)*80+loc.j][loc.i*80+loc.j] == 1)//open_road
					{
						Flow.flows[loc.i][loc.j][1]++;
					}
					loc.i += 1;
					break;
				case 2 ://left
					if(BFS.adj_matrix[loc.i*80+loc.j][loc.i*80+loc.j-1] == 1)//open_road
					{
						Flow.flows[loc.i][loc.j-1][0]++;
					}
					loc.j -= 1;
					break;
				case 3 ://right
					if(BFS.adj_matrix[loc.i*80+loc.j][loc.i*80+loc.j+1] == 1)//open_road
					{
						Flow.flows[loc.i][loc.j][0]++;
					}
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
				transStatus();
			}
		}
		catch(Exception e)
		{
			System.out.println("Sorry to catch Exception!");
		}
	}

	void makeService(int loc_temp,int des_temp)
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
			BFS.setShortest(des_temp,shortest_path,BFS.adj_const);
			int temp = shortest_path[loc_temp];
			while(temp != -1)
			{
				int length_min = BFS.getShortest(temp, des_temp,BFS.adj_const);
				int direction = -1;
				int flow_min = Main.MAX_INT;
				if((i_current-1)>=0 && BFS.adj_const[(i_current-1)*80+j_current][i_current*80+j_current]==1 && Flow.flows[i_current-1][j_current][1]<flow_min)
				{
					int length_temp = BFS.getShortest((i_current-1)*80+j_current, des_temp, BFS.adj_const);
					if(length_temp <= length_min)
					{
						direction = 0;//up
						flow_min = Flow.flows[i_current-1][j_current][1];
						length_min = length_temp;
					}
				}
				if((i_current+1)<80 && BFS.adj_const[(i_current+1)*80+j_current][i_current*80+j_current]==1 && Flow.flows[i_current][j_current][1]<flow_min)
				{
					int length_temp = BFS.getShortest((i_current+1)*80+j_current, des_temp, BFS.adj_const);
					if(length_temp <= length_min)
					{
						direction = 1;//down
						flow_min = Flow.flows[i_current][j_current][1];
						length_min = length_temp;
					}	
				}
				if((j_current-1)>=0 && BFS.adj_const[i_current*80+j_current-1][i_current*80+j_current]==1 && Flow.flows[i_current][j_current-1][0]<flow_min)
				{	
					int length_temp = BFS.getShortest(i_current*80+j_current-1, des_temp, BFS.adj_const);
					if(length_temp <= length_min)
					{
						direction = 2;//left
						flow_min = Flow.flows[i_current][j_current-1][0];
						length_min = length_temp;
					}
					
				}
				if((j_current+1)<80 && BFS.adj_const[i_current*80+j_current+1][i_current*80+j_current]==1 && Flow.flows[i_current][j_current][0]<flow_min)
				{
					int length_temp = BFS.getShortest(i_current*80+j_current+1, des_temp, BFS.adj_const);
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
					if(BFS.adj_matrix[(i_current-1)*80+j_current][i_current*80+j_current] == 1)//open_road
					{
						Flow.flows[i_current-1][j_current][1]++;
					}
					i_current -= 1;
					break;
				case 1 ://down
					if(BFS.adj_matrix[(i_current+1)*80+j_current][i_current*80+j_current] == 1)//open_road
					{
						Flow.flows[i_current][j_current][1]++;
					}
					i_current += 1;
					break;
				case 2 ://left
					if(BFS.adj_matrix[i_current*80+j_current][i_current*80+j_current-1] == 1)//open_road
					{
						Flow.flows[i_current][j_current-1][0]++;
					}
					j_current -= 1;
					break;
				case 3 ://right
					if(BFS.adj_matrix[i_current*80+j_current][i_current*80+j_current+1] == 1)//open_road
					{
						Flow.flows[i_current][j_current][0]++;
					}
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
	
	void makeService(int loc_temp,int des_temp,ArrayList<Coordinate> path)
	{
		/* Requires: des_temp is an integer and 0=<des_temp<6400
		 * Modifies:loc
		 * Effects: according to the shortest path,drive across one road every 100ms from loc_temp and print out its location
		 * 			when having arrived at des_temp,print it out
		 * 			service paths are saved in path */
		try
		{
			int last_direction = 0;
			int[] shortest_path = new int[6400];
			int j_current = loc_temp % 80;
			int i_current = loc_temp / 80;
			BFS.setShortest(des_temp,shortest_path,BFS.adj_const);
			int temp = shortest_path[loc_temp];
			while(temp != -1)
			{
				int length_min = BFS.getShortest(temp, des_temp,BFS.adj_const);
				int direction = -1;
				int flow_min = Main.MAX_INT;
				if((i_current-1)>=0 && BFS.adj_const[(i_current-1)*80+j_current][i_current*80+j_current]==1 && Flow.flows[i_current-1][j_current][1]<flow_min)
				{
					int length_temp = BFS.getShortest((i_current-1)*80+j_current, des_temp, BFS.adj_const);
					if(length_temp <= length_min)
					{
						direction = 0;//up
						flow_min = Flow.flows[i_current-1][j_current][1];
						length_min = length_temp;
					}
				}
				if((i_current+1)<80 && BFS.adj_const[(i_current+1)*80+j_current][i_current*80+j_current]==1 && Flow.flows[i_current][j_current][1]<flow_min)
				{
					int length_temp = BFS.getShortest((i_current+1)*80+j_current, des_temp, BFS.adj_const);
					if(length_temp <= length_min)
					{
						direction = 1;//down
						flow_min = Flow.flows[i_current][j_current][1];
						length_min = length_temp;
					}	
				}
				if((j_current-1)>=0 && BFS.adj_const[i_current*80+j_current-1][i_current*80+j_current]==1 && Flow.flows[i_current][j_current-1][0]<flow_min)
				{	
					int length_temp = BFS.getShortest(i_current*80+j_current-1, des_temp, BFS.adj_const);
					if(length_temp <= length_min)
					{
						direction = 2;//left
						flow_min = Flow.flows[i_current][j_current-1][0];
						length_min = length_temp;
					}
					
				}
				if((j_current+1)<80 && BFS.adj_const[i_current*80+j_current+1][i_current*80+j_current]==1 && Flow.flows[i_current][j_current][0]<flow_min)
				{
					int length_temp = BFS.getShortest(i_current*80+j_current+1, des_temp, BFS.adj_const);
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
					if(BFS.adj_matrix[(i_current-1)*80+j_current][i_current*80+j_current] == 1)//open_road
					{
						Flow.flows[i_current-1][j_current][1]++;
					}
					i_current -= 1;
					break;
				case 1 ://down
					if(BFS.adj_matrix[(i_current+1)*80+j_current][i_current*80+j_current] == 1)//open_road
					{
						Flow.flows[i_current][j_current][1]++;
					}
					i_current += 1;
					break;
				case 2 ://left
					if(BFS.adj_matrix[i_current*80+j_current][i_current*80+j_current-1] == 1)//open_road
					{
						Flow.flows[i_current][j_current-1][0]++;
					}
					j_current -= 1;
					break;
				case 3 ://right
					if(BFS.adj_matrix[i_current*80+j_current][i_current*80+j_current+1] == 1)//open_road
					{
						Flow.flows[i_current][j_current][0]++;
					}
					j_current += 1;
					break;
				}
				Thread.sleep(100);
				last_direction = direction;
				loc = new Coordinate(i_current,j_current);
				path.add(loc);
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

	void transStatus()
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
				Service s_temp = new Service(passenger);
				des = passenger.loc;
				makeService(loc.i*80+loc.j,des.i*80+des.j);
				status = "stop";
				Thread.sleep(1000);
				status = "serve";
				des = passenger.des;
				s_temp.path.add(loc);
				makeService(loc.i*80+loc.j,des.i*80+des.j,s_temp.path);
				status = "stop";
				Thread.sleep(1000);
				credit += 3;
				ser_times += 1;
				services.add(s_temp);
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
				driveTracker();
			}
		}
		catch(Exception e)
		{
			System.out.print("Sorry to catch Exception!");
		}
	}
}
