package Taxiruf;

import java.util.ArrayList;
import java.util.ListIterator;
import java.util.Random;
import java.util.Scanner;

public class Main implements test
{
	static final int MAX_INT = 999_999_999;
	static final Random rand = new Random();
	static long Sys_start;
	static ArrayList<Taxi> taxis = new ArrayList<Taxi>(); 
	volatile static boolean end = false;

	public boolean repOK()
	{
		/* Requires:none
		 * Modifies:none
		 * Effects:examine invariance
		 *		   if taxis.size=100,return true
		 * 		   else,return false */
		if(taxis.size() != 100)
		{
			return false;
		}
		return true;
	}
	
	public double normalizeTime(long time)
	{
		/* Requires:time>=0
		 * Modifies:time
		 * Effects:return normalized time on 100ms basis by rounding-off method */
		long r_mod = time % 100;
		if(r_mod > 0)
		{
			time -= r_mod;
			if(r_mod >= 50)
			{
				time += 100;
			}
		}
		return time/1000.0;
	}
	
	public static void produceTaxi()
	{
		/* Requires:none
		 * Modifies:taxis
		 * Effects:produce 100 taxis including 30 trackers randomly */
		int[] tra_num = new int[30];
		for(int i=0; i<30; i++)
		{
			boolean found = false;
			int id_rand = rand.nextInt(100) + 1;
			for(int id_temp : tra_num)
			{
				if(id_temp == id_rand)
				{
					found = true;
					i--;
					break;
				}
			}
			if(!found)
			{
				tra_num[i] = id_rand;
			}
		}
		for(int i=1; i<=100; i++)
		{
			boolean isTracker = false;
			for(int id_temp : tra_num)
			{
				if(i == id_temp)
				{
					isTracker = true;
					taxis.add(new Tracker(i));
					break;
				}
			}
			if(!isTracker)
			{
				taxis.add(new Taxi(i));
			}
		}
	}
	
	public static void main(String[] args)
	{
		/* Requires:none
		 * Modifies:none
		 * Effects:instantiation */
		try
		{
			Scanner in = new Scanner(System.in);
			//set city_map:
			System.out.println("Please input the filepath of city_map: ");
			Map.readMap(in.nextLine());
			//set road_layout:
			System.out.println("Please input the filepath of road_layout: ");
			Map.readRoad(in.nextLine());
			BFS.setMatrix();
			//system start time:
			Sys_start = System.currentTimeMillis();
			//lights:
			Traffic.setLight();
			new Thread(new Traffic()).start();
			//passengers:
			new Thread(new Request()).start();
			//100 taxis:
			produceTaxi();
			for(int i=0; i<100; i++)
			{
				if(taxis.get(i).isTracker)
				{
					new Thread((Tracker)taxis.get(i),"Taxi-"+(i+1)).start(); 
				}
				else
				{
					new Thread(taxis.get(i),"Taxi-"+(i+1)).start();
				}
			}
			//system control:
			new Thread(new Control()).start();
			//traffic_flow:
			new Thread(new Flow()).start();
			if(in.nextLine().equals("END"))
			{
				in.close();
				end = true;
//				Thread.sleep(1000);
//				System.out.println("");
//				for(int i=0; i<100; i++)
//				{
//					if(taxis.get(i).isTracker)
//					{
//						ListIterator<Coordinate> temp = new Main().showTracker(i+1, 1);
//						while(temp!=null && temp.hasNext())
//						{
//							System.out.print(temp.next());
//							if(temp.hasNext())
//							{
//								System.out.print("->");
//							}
//							else
//							{
//								System.out.println("");
//							}
//						}
//					}
//				}
				System.exit(1);
			}
		}
		catch(Exception e)
		{
			System.out.println("Sorry to catch Exception!");
		}
	}

	@Override
	public void showTaxi(int taxi_id)
	{
		/* Requires:taxi_id is an integer and 1<=taxi_id<=100
		 * Modifies:none
		 * Effects: if taxi<1 or taxi>100 then inaccessible
		 * 			else,show assigned taxi's information */
		if(taxi_id>=1 && taxi_id<=100)
		{
			System.out.println("Current system time: " + normalizeTime(System.currentTimeMillis()-Sys_start));
			Taxi taxi_temp = Main.taxis.get(taxi_id-1);
			System.out.println("Taxi-" + taxi_id + taxi_temp.loc + ": status[" + taxi_temp.status + "], credit[" + taxi_temp.credit + "]");
			if(taxi_temp.meet_light)
			{
				System.out.println("Taxi-" + taxi_id + ": Waiting at a traffic light");
			}
		}
		else
		{
			System.out.println("Taxi-" + taxi_id + ": No access");
		}
	}

	
	@Override
	public void maintainRoad(int tag,Coordinate x, Coordinate y)
	{
		/* Requires:tag must be 0 or 1
		 *          0=<x.i<80 and 0=<x.j<80
		 *          0=<y.i<80 and 0=<y.i<80
		 *          Road[x-y] should have already existed
		 * Modifies:BFS.adj_matrix
		 * Effects: open or close a road
		 * 			if tag is neither 0 nor 1 then invalid
		 *          else if tag=0, then close Road[x-y]
		 *          else,open Road[x-y]
		 *          specially,if Road[x-y] has not existed before, then the operation may result in unconnected city_map */
		switch(tag)
		{
		case 0 ://close
			BFS.adj_matrix[x.i*80+x.j][y.i*80+y.j] = MAX_INT;
			BFS.adj_matrix[y.i*80+y.j][x.i*80+x.j] = MAX_INT;
			System.out.println("Road" + x + y + ":  Closed");
			break;
		case 1 ://open
			 BFS.adj_matrix[x.i*80+x.j][y.i*80+y.j] = 1;
			 BFS.adj_matrix[y.i*80+y.j][x.i*80+x.j] = 1;
			 System.out.println("Road" + x + y + ": Open");
			 break;
		default:
			System.out.println("Road" + x + y + ": Invalid action");
		}	 	
	}

	@Override
	public void showWaiting()
	{
		/* Requires:none
		 * Modifies:none
		 * Effects:show all the taxis whose status is "wait" */
		System.out.println("Current system time: " + normalizeTime(System.currentTimeMillis()-Sys_start));
		for(int i=0; i<100; i++)
		{
			Taxi taxi_temp = Main.taxis.get(i);
			if(taxi_temp.status.equals("wait"))
			{
				System.out.println("Taxi-" + taxi_temp.id + taxi_temp.loc + ": Waiting");
			}
		}
	}

	@Override
	public ListIterator<Coordinate> showTracker(int taxi_id,int times)
	{
		/* Requires:1=<taxi_id<=100
		 * Modifies:none
		 * Effects:if aimed taxi is a tracker,show its service information
		 * 		   else,print corresponding tips */
		if(taxi_id>=1 && taxi_id<=100)
		{
			if(Main.taxis.get(taxi_id-1).isTracker)
			{
				Tracker taxi_temp = (Tracker)Main.taxis.get(taxi_id-1);
				System.out.println("Tracker_info:");
				System.out.println("Taxi-" + taxi_temp.id + ": times_all[" + taxi_temp.ser_times + "]");
				if(times <= taxi_temp.ser_times && times > 0)
				{
					Service s_temp = taxi_temp.services.get(times-1);
					System.out.println("Taxi-" + taxi_temp.id + ": service[" + times + "]-"+ s_temp.passenger.loc + s_temp.passenger.des);
					return s_temp.path.listIterator();
				}
				else
				{
					System.out.println("Taxi-" + taxi_id + ": Unable to access service[" + times + "]");
				}
			}
			else
			{
				System.out.println("Taxi-" + taxi_id + ": Not a tracker");
			}
		}
		else
		{
			System.out.println("Taxi-" + taxi_id + ": No access");
		}
		return null;
	}
}