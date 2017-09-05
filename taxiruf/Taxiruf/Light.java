package Taxiruf;

public class Light
{
	Coordinate loc;
	String status_NS = "green";//North and South
	String status_WE = "red";//West and East

	public boolean repOK()
	{
		/* Requires:none
		 * Modifies:none
		 * Effects:examine invariance
		 *		   if loc(80*80) and status_NS{"green","red"} and status_WE{"green","red"},return true
		 * 		   else,return false */
		if(!loc.insideScope())
		{
			return false;
		}
		if(!status_NS.equals("green") && !status_NS.equals("red"))
		{
			return false;
		}
		if(!status_WE.equals("green") && !status_WE.equals("red"))
		{
			return false;
		}
		if(status_NS.equals(status_WE))
		{
			return false;
		}
		return true;
	}
	
	Light(int loc_i,int loc_j)
	{
		/* Requires:0=<loc_i<80
		 * 			0=<loc_j<80
		 * Modifies:loc
		 * Effects:set loc.i as loc_i
		 * 		   set loc.j as loc_j */
		loc = new Coordinate(loc_i, loc_j);
	}
	
	void changeColor()
	{
		/* Requires:none
		 * Modifies:status_NS
		 * 			status_WE
		 * Effects:change the color of traffic light
		 * 		   if status is "red",set it as "green"
		 * 		   else,set it as "red" */
		switch(status_NS)
		{
		case "green" :
			status_NS = "red";
			break;
		case "red" :
			status_NS = "green";
			break;
		}
		switch(status_WE)
		{
		case "green" :
			status_WE = "red";
			break;
		case "red" :
			status_WE = "green";
			break;
		}
	}

}
