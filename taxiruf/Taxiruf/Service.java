package Taxiruf;

import java.util.ArrayList;

public class Service
{
	Passenger passenger;
	ArrayList<Coordinate> path = new ArrayList<Coordinate>();
	
	Service(Passenger pass)
	{
		/* Requires:none
		 * Modifies:none
		 * Effects:examine invariance
		 *		   when 0<id<=100 and status{"wait","soon","stop","serve"} and loc(80*80) and des(80*80) and credit>=0,return true
		 *		   otherwise,return false */
		passenger = pass;
	}
	
	public boolean repOK()
	{
		/* Requires:none
		 * Modifies:none
		 * Effects:examine invariance
		 *		   when loc(80*80) and passenger.loc(80*80) and passenger.des(80*80),return true
		 *		   otherwise,return false */
		if(!passenger.loc.insideScope() || !passenger.des.insideScope())
		{
			return false;
		}
		return true;
	}
}
