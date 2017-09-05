package Taxiruf;

public class Flow implements Runnable
{
	volatile static int[][][] flows = new int[80][80][2];
	
	public boolean repOK()
	{
		/* Requires:none
		 * Modifies:none
		 * Effects:examine invariance
		 *		   when 0=<i<80 and 0=<j<80 and 0=<k<2 and flows[i][j][k]>=0,return true
		 *		   otherwise,return false */
		for(int i=0; i<80; i++)
		{
			for(int j=0; j<80; j++)
			{
				for(int k=0; k<2; k++)
				{
					if(flows[i][j][k] < 0)
					{
						return false;
					}
				}
			}
		}
		return true;
	}
	
	@Override
	public void run() {
		while(!Main.end)
		{
			try
			{
				Thread.sleep(50);
				flows = new int[80][80][2];
			}
			catch (Exception e)
			{
				System.out.println("Sorry to catch Exception!");
			}
			
		}
		
	}
	
}
