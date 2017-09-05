package Taxiruf;

public class Coordinate//position
{
	int i;//row
	int j;//column
	
	public boolean repOK()
	{
		/* Requires:none
		 * Modifies:none
		 * Effects:examine invariance
		 *		   when 0=<i<80 and 0=<j<80,return true
		 *		   otherwise,return false */
		if(i<0 || i>=80)
		{
			return false;
		}
		if(j<0 || j>=80)
		{
			return false;
		}
		return true;
	}
	
	Coordinate(int i_temp,int j_temp)
	{
		/* Requires:i_temp is an integer
		 * 			j_temp is an integer
		 * Modifies:i
		 * 			j
		 * Effects:set i as i_temp
		 * 		   set j as j_temp */
		i = i_temp;
		j = j_temp;
	}
	
	Coordinate(int i_temp,int j_temp,boolean if_adjust)
	{
		/* Requires:i_temp is an integer
		 * 			j_temp is an integer
		 * Modifies:i
		 * 			j
		 * Effects: if i_temp<0 then set i=0
		 * 			if i_temp>=80 then set i=79
		 * 			if j_temp<0 then set j=0
		 *			if j_temp>=80 then set j=79 */
		i = adjustRange(i_temp);
		j = adjustRange(j_temp);
	}
	
	@Override
	public String toString()
	{
		/* Requires:none
		 * Modifies:none
		 * Effects:return a special string format */
		return "[" + i + "," + j + "]";
	}

	private int adjustRange(int temp)
	{
		/* Requires:temp is an integer
		 * Modifies:temp
		 * Effects:return temp
		 * 		   if temp<0 then set temp=0
		 *         if temp>=80 then set temp=79 */
		if(temp < 0)
		{
			temp = 0;
		}
		else if(temp >= 80)
		{
			temp = 79;
		}
		return temp;
	}
	
	public boolean insideScope()
	{
		/* Requires:none
		 * Modifies:none
		 * Effects:if 0=<i<80 and 0=<j<80,return true
		 * 		   else,return false */
		if(i>=0 && i<80 && j>=0 && j<80)
		{
			return true;
		}
		return false;
	}
}
