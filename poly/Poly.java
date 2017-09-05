package poly;

import java.util.Scanner;

public class Poly {
	final int maxsize = 100_000_000;
	int[] coef = new int[maxsize];
	int[] expo = new int[maxsize];
	Scanner in = new Scanner(System.in);
	String s = new String(in.nextLine());
	
	void backSpace()
	{
		s = s.replace(" ", "");
	}
	
	int getPoly()
	{
		int i = 0,m = 0,n = 0;
		char op = '+';
		boolean cofound = false,exfound = false;
		while(i < s.length())
		{
			String strco = "";
			String strex = "";
			if(s.charAt(i) == '{' && s.charAt(i+1) != '(' && s.charAt(i+1) != '}')
			{
				System.out.println("Wrong input format! Retype your data!");
		    	return -1;
			}
			if(s.charAt(i) == '+' || s.charAt(i) =='-')
			{
				if(i != 0 && s.charAt(i-1) != '}' || s.charAt(i+1) != '{')
				{
					System.out.println("Wrong input format!  Retype your data!");
					return -1;
				}
				op = s.charAt(i);
			}
			if(!cofound)
			{
				if(s.charAt(i) < 48 || s.charAt(i) > 57)
				{
					i++;
					if(i == s.length())
					{
						break;
					}
				}
				while(s.charAt(i) >= 48 && s.charAt(i) <= 57)
				{
					if(s.charAt(i-1) != '(')
					{
						System.out.println("Wrong input format! Retype your data!");
						return -1;
					}
					cofound = true;
					strco += s.charAt(i);
					i++;
					if(s.charAt(i) < 48 || s.charAt(i) > 57)
					{
						if(s.charAt(i) != ',')
						{
							System.out.println("Wrong input format! Retype your data!");
							return -1;
						}
						coef[m++] = Integer.parseInt(strco);
						if(op == '-')
						{
							coef[m-1] = -coef[m-1];
						}
					}
				}
			}
			else
			{
				while(s.charAt(i) >= 48 && s.charAt(i) <= 57)
				{
					exfound = true;
					cofound = false;
					strex += s.charAt(i);
					i++;
					if(s.charAt(i) < 48 || s.charAt(i) > 57)
					{
						if(s.charAt(i) != ')')
						{
							System.out.println("Wrong input format! Retype your data!");
							return -1;
						}
						expo[n++] = Integer.parseInt(strex);
					}
				}
				if(!exfound)
				{
					i++;
				}
				exfound = false;
			}
		}
		return m;
	}
	
	void getResult(int amount)
	{
		for(int i = 0; i < amount; i++)
		{
			for(int j = i + 1; j < amount; j++)
			{
				if(expo[i] == expo[j])
				{
					coef[i] += coef[j];
					coef[j] = 0;
				}
			}
		}
		if(amount >= 0)
		{
			System.out.print("{");
			for(int i = 0; i <amount; i++)
			{
				if(coef[i] != 0)
				{
					System.out.print(" (" + coef[i] + "," + expo[i] + ") ");
				}
			}
			System.out.println("}");
		}
	}

	public static void main(String[] args) {
		Poly polycal = new Poly();
		polycal.backSpace();
		int amount;
		amount = polycal.getPoly();
		polycal.getResult(amount);	
	}

}
