package bylift;

import java.util.ArrayList;
import java.util.Scanner;

public class queue
/*
 * Overview：描述一个队列的基本实现，包括判断是否满足入队条件、入队等基本操作
 * 表示对象：请求字符串队列RQ
 * 不变式：none
 * 抽象函数：AF(RQ)={RQ.add(str_temp) if str_temp!=null
 */
{
	Scanner in = new Scanner(System.in);;
	ArrayList<String> RQ = new ArrayList<String>();
	
	public boolean repOK()
	/*
	 * Requires：none
	 * Modifies：none
	 * Effects：examine invariance
	 */
	{
		return true;
	}
	
	int addQueue()
	/*
	 * Requires：none
	 * Modifies：RQ
	 * Effects：若输入结束则返回-1
	 * 			反之，入队成功返回1，否则返回0
	 */
	{
		String s = in.nextLine();
		s = s.replace(" ","");
		if(s.equals("END"))
		{
			return -1;
		}
		else
		{
			 if(s.length() > 3)
			 {
				 if(!(s.charAt(0) == '(' && s.charAt(s.length()-1) == ')'))
				 {
					 return 0;
				 }
				 s = s.substring(1, s.length() - 1);
				 RQ.add(s);
				 return 1;
			 }
			 else
			 {
				 return 0;
			 }
		}
	}
	
	void judgeQueue()
	/*
	 * Requires：none
	 * Modifies：RQ
	 * Effects：读入字符串请求并判断是否有效
	 * 			若RQ为空则提示输入中不包含有效请求
	 * 			针对入队失败的请求输出提示信息
	 */
	{
		int[] IR = new int[100_000];
		int cnt = 0;
		int index = 0;
		boolean sign = true;
		
		while(sign)
		{
			switch(addQueue())
			{
			case 1:
				cnt++;
				break;
			case 0:
				cnt++;
				IR[index++] = cnt;
				break;
			default:
				sign = false;
				break;
			}
		}
		if(cnt == 0)
		{
			System.out.println("未输入任何请求！");
		}
		else
		{
			for(int i = 0; i < index; i++)
			{
				System.out.println("第" + IR[i] + "条请求是无效的！");
			}
		}
	}
}