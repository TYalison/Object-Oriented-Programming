package Taxiruf;

import java.util.concurrent.LinkedBlockingQueue;
//import java.util.regex.Pattern;

public class Request implements Runnable
{
	volatile static LinkedBlockingQueue<Passenger> passengers = new LinkedBlockingQueue<Passenger>(300);

	public boolean repOK()
	{
		/* Requires:none
		 * Modifies:none
		 * Effects:examine invariance
		 *		   when 0=<i<passengers.size and passengers[i].loc(80*80) and passengers[i].des(80*80),return true
		 *		   otherwise,return false */
		for(Passenger pass_temp:passengers)
		{
			if(!pass_temp.judgeValid())
			{
				return false;
			}
		}
		return true;
	}
	
	@Override
	public void run()
	{
		try
		{
			while(!Main.end)
			{
				passengers.put(new Passenger());
			}
		}
		catch(Exception e)
		{
			System.out.println("Sorry to catch Exception!");
		}
	}
	
//	boolean isNumeric(String str_temp)
//	{
//		/* Requires:str_temp is not empty
//		 * Modifies:none
//		 * Effects:judge whether str_temp represents number or not */
//	    Pattern pattern = Pattern.compile("[0-9]*");  
//	    return pattern.matcher(str_temp).matches();     
//	}
//	
//	boolean insideScope(int temp_1,int temp_2,int temp_3,int temp_4)
//	{
//		/* Requires:none
//		 * Modifies:none
//		 * Effects:judge whether both [temp_1,temp_2] and [temp_3,temp_4] are within city_map */
//		if(temp_1<80 && temp_1>=0 && temp_2<80 && temp_2>=0 && temp_3<80 && temp_3>=0 && temp_4<80 && temp_4>=0)
//		{
//			return true;
//		}
//		return false;
//	}
//	
//	Passenger setPass(String str_temp)
//	{
//		/* Requires:str_temp is supposed to be "loc.i,loc,j,des.i,des.j" with no space
//		 * 			0=<loc.i<80
//		 * 			0=<loc.j<80
//		 * 			0=<des.i<80
//		 * 			0=<des.j<80
//		 * Modifies:none
//		 * Effects:if input is in correct format,return fixed passenger
//		 *		   else,return null */
//		if(!str_temp.isEmpty())
//		{
//			String[] tag = str_temp.split("\\,");
//			if(tag.length == 4)
//			{
//				if(tag[0].length()<=2 && tag[1].length()<=2 && tag[2].length()<=2 && tag[3].length()<=2)
//				{
//					if(isNumeric(tag[0]) && isNumeric(tag[1]) && isNumeric(tag[2]) && isNumeric(tag[3]))
//					{
//						if(insideScope(Integer.parseInt(tag[0]),Integer.parseInt(tag[1]),Integer.parseInt(tag[2]),Integer.parseInt(tag[3])))
//						{
//							return new Passenger(Integer.parseInt(tag[0]),Integer.parseInt(tag[1]),Integer.parseInt(tag[2]),Integer.parseInt(tag[3]));
//						}
//					}
//				}
//			}
//		}
//		System.out.println("Request: Invalid input!");
//		return null;
//	}
}