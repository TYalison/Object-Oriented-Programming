package Taxiruf;

public class Passenger
{
	Coordinate loc;//location
	Coordinate des;//destination
	
	public boolean repOK()
	{
		/* Requires:none
		 * Modifies:none
		 * Effects:examine invariance
		 *		   when loc(80*80) and des(80*80),return true
		 *		   otherwise,return false */
		if(!loc.insideScope() || !des.insideScope())
		{
			return false;
		}
		return true;
	}
	
	Passenger()
	{
		/* Requires:none
		 * Modifies:loc
		 * 			des
		 * Effects:set a new passenger with random location and destination
		 * 		   make sure that loc is different from des */
		loc = new Coordinate(Main.rand.nextInt(80),Main.rand.nextInt(80));
		des = new Coordinate(Main.rand.nextInt(80),Main.rand.nextInt(80));
		while(loc.i == des.i && loc.j == des.j)
		{
			des = new Coordinate(Main.rand.nextInt(80),Main.rand.nextInt(80));
		}
	}
	
	boolean judgeValid()
	{
		/* Requires:none
		 * Modifies:none
		 * Effects:judge whether either passenger or its request is inside city_map
		 * 		   if 0=<loc.i<80 and 0=<loc.j<80 and 0=<des.i<80 and 0=<des.j<80,return true
		 * 		   else,return false */
		if(loc.i>=0 && loc.i<80 && loc.j>=0 && loc.j<80 && des.i>=0 && des.i<80 && des.j>=0 && des.j<80)
		{
			return true;
		}
		return false;
		
	}

//	Passenger(int loc_i,int loc_j,int des_i,int des_j)
//	{
//		/* Requires:0=<loc_i<80
//	 	* 			0=<loc_j<80
//	 	* 			0=<des_i<80
//	 	* 			0=<des_j<80
//	 	* Modifies:loc
//		* 		   des
//	 	* Effects:if loc_i<0 then set temp=0 elif loc_i>=80 then set temp=79
// 		*         if loc_j<0 then set temp=0 elif loc_j>=80 then set temp=79
//		*         if des_i<0 then set temp=0 elif des_i>=80 then set temp=79
//		*         if des_j<0 then set temp=0 elif des_j>=80 then set temp=79none */
//		loc = new Coordinate(loc_i,loc_j);
//		des = new Coordinate(des_i,des_j);
//	}
}