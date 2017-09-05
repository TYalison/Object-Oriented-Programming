package bylift;

public class request
/*
 * Overview：描述请求的基本实现，包括目标楼层、请求发出时间、方向等基本对象，以及判断请求是否有效等基本操作
 * 表示对象：请求各项tag
 * 			 目标楼层des_n
 * 			 请求发出时间t
 * 			 请求字符串s
 * 			 是否请求上行up
 * 			 是否请求下行down
 * 不变式：3=<tag.length<=4
 * 		   1=<des_n<=10
 *         t>=0
 * 抽象函数：AF(tag)={tag=s.split("\\,")|s!=null}
 * 			 AF(des_n)={des_n=tag[1]}
 * 			 AF(t)={t=ER.tag[2]; t=FR.tag[3]}
 * 			 AF(s)={s=rep_str when req_str is valid}
 * 			 AF(up)={up=true when FR.UP or ER.des_n>loc_n; up=false otherwise}
 * 			 AF(down)={down=true when FR.DOWN or ER.des_n<loc_n; up=false otherwise}
 */
{
	String[] tag;
	int des_n = 1;
	double t = 0;
	String s = new String();
	boolean up = false;
	boolean down = false;
	
	public boolean repOK()
	/*
	 * Requires：none
	 * Modifies：none
	 * Effects：examine invariance
	 */
	{
		if(des_n>10 || des_n<1 || t<0)
		{
			return false;
		}
		return true;
	}
	
	boolean judgeRequest(String str)
	/*
	 * Requires：str!=null
	 * Modifies：tag
	 * 			 s
	 * 			 des_n
	 * 			 t
	 * Effects：判断请求是否有效
	 * 			若有效返回true，并设置请求基本状态参数
	 * 			否则返回false		
	 */
	{
		int i = 0;
		if(str.isEmpty())
		{
			return false;
		}
		tag = str.split("\\,");
		if(tag.length < 3 || tag.length > 4)
		{
			return false;
		}
		s = str;
		switch(i)
		{
		case 0:
			if(!(tag[i].equals("ER") || tag[i].equals("FR")))
			{
				return false;
			}
			else
			{
				if(tag[i].equals("ER") && tag.length != 3 || tag[i].equals("FR") &&tag.length != 4)
				{
					return false;
				}
				i++;
			}
		case 1:
			for(int j = 0;j < tag[i].length();j++)
			{
				if(tag[i].charAt(j) < 48 || tag[i].charAt(j) > 57 || tag[i].length() > 2)
				{
					return false;
				}
				else if(j == tag[i].length() - 1)
				{
					des_n = Integer.parseInt(tag[i]);
					if(des_n < 1 || des_n > 10)
					{
						return false;
					}
					i++;
					break;
				}
			}
		case 2:
			if(tag[0].equals("FR"))
			{
				if(!(tag[i].equals("UP") || tag[i].equals("DOWN")))
				{
					return false;
				}
				else
				{
					if(des_n == 1 && tag[2].equals("DOWN") || des_n == 10 && tag[2].equals("UP"))
					{
						return false;
					}
					i++;
				}
			}
			else if(tag[0].equals("ER"))
			{
				for(int j = 0;j < tag[i].length();j++)
				{
					if(tag[i].charAt(j) < 48 || tag[i].charAt(j) > 57)
					{
						return false;
					}
				}
				if(tag.length == 3)
				{
					if(Double.parseDouble(tag[i]) < 0 || Double.parseDouble(tag[i]) < t)
					{
						return false;
					}
					t = Double.parseDouble(tag[i]);
					return true;
				}
				else
				{
					return false;
				}
			}
		case 3:
			for(int j = 0;j < tag[i].length();j++)
			{
				if(tag[i].charAt(j) < 48 || tag[i].charAt(j) > 57)
				{
					return false;
				}
			}
			if(tag.length == 4)
			{
				if(Double.parseDouble(tag[i]) < 0 || Double.parseDouble(tag[i]) < t)
				{
					return false;
				}
				t = Double.parseDouble(tag[i]);
				return true;
			}
			else
			{
				return false;
			}
		default:
			return false;
	    }
    }
	
	boolean realizeRequest(floor F,elevator E)
	/*
	 * Requires：F!=null
	 * 			 E!=null
	 * Modifies：F
	 * 			 E
	 * Effects：判断当前请求是否可行
	 * 			若可行则返回true，并根据请求类型设置好楼层和电梯的相关状态参数
	 * 			否则返回false
	 */
	{
		if(F==null || E==null)
		{
			return false;
		}
		if(tag[0].equals("FR"))
		{
			F.loc_n = des_n;
			F.t = t;
		}
		else if(tag[0].equals("ER"))
		{
			if(Double.parseDouble(tag[2]) >= E.t)
			{
				E.des_n = des_n;
				E.t = t;
			}
			else
			{
				return false;
			}
		}
		else
		{
			return false;
		}
		return true;
	}

	@Override
	public boolean equals(Object o)
	/*
	 * Requires：o!=null
	 * Modifies：none
	 * Effects：通过请求字符串判断两个请求是否相同
	 */
	{
		request tmpR = (request) o;
		if(tmpR.s.equals(s))
		{
			return true;
		}
		return false;
	}
}