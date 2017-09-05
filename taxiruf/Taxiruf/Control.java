package Taxiruf;

public class Control implements Runnable
{
	volatile Coordinate scope_max;
	volatile Coordinate scope_min;
	volatile Passenger passenger = null;
	volatile int[] taxi_num = new int[100];
	volatile int index = 0;

	public boolean repOK()
	{
		/* Requires:none
		 * Modifies:none
		 * Effects:examine invariance
		 *		   when scope_min(80*80) and scope_max(80*80) and index>=0 and 0<chosen_taxi_id<=100,return true
		 *		   otherwise,return false */
		if(!scope_max.insideScope() || !scope_min.insideScope())
		{
			return false;
		}
		if(index < 0)
		{
			return false;
		}
		for(int i=0; i<index; i++)
		{
			if(taxi_num[i] <= 0)
			{
				return false;
			}
		}
		return true;
	}
	
	int getShortest(Taxi taxi_temp,int v_begin,int v_end)
	{
		/* Requires:taxi_temp!=null
		 *          0=<v_begin<79*80+80
		 *          0=<v_end<79*80+80
		 * Modifies:none
		 * Effects:return the shortest length from v_begin to v_end according to the taxi type */
		if(taxi_temp.isTracker)
		{
			return BFS.getShortest(v_begin,v_end,BFS.adj_const);
		}
		else
		{
			return BFS.getShortest(v_begin,v_end,BFS.adj_matrix);
		}
	}
	
	int chooseTaxi()
	{
		/* Requires:none
		 * Modifies:none
		 * Effects:according to credit_max then path_shortest,return chosen taxi
		 * 		   specially,returning -1 suggests no taxi available now */
		int target_taxi = -1;
		try
		{
			boolean first_taxi = true;
			int path_min = 0;
			int credit_max = 0;
			for(int i=0; i<index; i++)
			{
				Main.taxis.get(taxi_num[i]).credit += 1;
				if(Main.taxis.get(taxi_num[i]).status.equals("wait"))
				{
					int v_begin = passenger.loc.i*80+passenger.loc.j;
					int v_end = Main.taxis.get(taxi_num[i]).loc.i*80+Main.taxis.get(taxi_num[i]).loc.j;
					int credit_temp = Main.taxis.get(taxi_num[i]).credit;
					int path_temp = getShortest(Main.taxis.get(taxi_num[i]), v_begin, v_end);
					if(first_taxi)
					{
						path_min = path_temp;
						credit_max = credit_temp;
						target_taxi = taxi_num[i];
						first_taxi = false;
					}
					else
					{
						if(credit_temp > credit_max)
						{
							path_min = path_temp;
							credit_max = credit_temp;
							target_taxi = taxi_num[i];
						}
						else if(credit_temp == credit_max)
						{
							if(path_temp < path_min)
							{
								target_taxi = taxi_num[i];
								path_min = path_temp;
							}
						}
					}
				}
			}
		}
		catch(Exception e)
		{
			System.out.println("Sorry to catch Exception!");
		}
		return target_taxi;
	}

	void accessTaxi()
	{
		/* Requires:none
		 * Modifies:taxi_num
		 * 			index
		 * Effects:mark all the taxis in the passenger's 4*4 scope */
		try
		{
			long time = System.currentTimeMillis();
			do
			{
				for(int i=0; i<100; i++)
				{
					if(Main.taxis.get(i).loc.i>=scope_min.i && Main.taxis.get(i).loc.j>=scope_min.j && Main.taxis.get(i).loc.i<=scope_max.i && Main.taxis.get(i).loc.j<=scope_max.j)
					{
						if(Main.taxis.get(i).status.equals("wait"))
						{
							boolean taxi_found = false;
							if(index > 0)
							{
								for(int j : taxi_num)
								{
									if(i == j)
									{
										taxi_found = true;
										break;
									}
								}
							}
							if(!taxi_found)
							{
								taxi_num[index++] = i;
							}
						}
					}
				}
			}while(System.currentTimeMillis()-time<3000);
			
		}
		catch(Exception e)
		{
			System.out.println("Sorry to catch Exception!");
		}
	}

	
	void determineScope()
	{
		/* Requires:none
		 * Modifies:scope_min
		 * 			scope_max
		 * Effects:set passenger's 4*4 scope */
		scope_min = new Coordinate((passenger.loc.i-2),(passenger.loc.j-2),true);
		scope_max = new Coordinate((passenger.loc.i+2),(passenger.loc.j+2),true);
	}
	
	void getReady(int taxi)
	{
		/* Requires:taxi is an integer and taxi must be less than 100
		 * Modifies:taxis.status
		 * 			taxi.passenger
		 * Effects:if taxi=-1,return no taxi available now
		 * 		   else,set current passenger as chosen taxi's passenger and return coming */
		if(taxi != -1)
		{
			Main.taxis.get(taxi).status = "soon";
			Main.taxis.get(taxi).passenger = passenger;
			System.out.println("Passenger" + passenger.loc + passenger.des + ": Taxi-" + Main.taxis.get(taxi).id + Main.taxis.get(taxi).loc + " is coming soon");
		}
		else
		{
			System.out.println("Passenger" + passenger.loc + passenger.des + ": No taxi available now");
		}
	}
	
	void refreshCtrl()
	{
		/* Requires:none
		 * Modifies:index
		 * 			taxi_num
		 * 			passenger
		 * Effects:clear marked taxis in the last choice
		 * 		   set current passenger as null and index as 0 */
		index = 0;
		for(int i=0; i<100; i++)
		{
			taxi_num[i] = -1;
		}
		passenger = null;
	}
	
	@Override
	public void run()
	{
		try
		{
			while(!Request.passengers.isEmpty() || !Main.end)
			{
				refreshCtrl();
				passenger = Request.passengers.take();
				if(passenger != null)
				{
					if(passenger.judgeValid())
					{
						determineScope();
						accessTaxi();
						getReady(chooseTaxi());
					}
					else
					{
						System.out.println("Passenger" + passenger.loc + passenger.des + ": Invalid request out of city_map");
					}
				}
			}
		}
		catch(Exception e)
		{
			System.out.println("Sorry to catch Exception!");
		}
	}
}