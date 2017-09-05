package bylift;

public class elevator
/*
 * Overview：描述一部电梯的基本实现
 *           包含当前所处楼层位置、目标停靠位置和当前系统相对时间等基本对象
 *           以及响应请求即搭载乘客等基本操作
 * 表示对象：电梯当前所处楼层loc_n
 *           运行目标楼层des_n
 *           当前系统相对时间t
 * 不变式：1=<loc_n<=10
 *         1=<des_n<=10
 *         t>=0
 * 抽象函数：AF(loc_n)={increase when lift up; reduce when lift down}
 * 			AF(des_n)={des_n=FR.loc_n; des_n=ER.des_n}
 * 			AF(t)={t=max(t,FR.t,ER.t); t+=1 when lift close or open; t+=0.5 when loc_n+=1 or loc_n-=1}
 */
{
	int loc_n = 1;
	int des_n = 1;
	double t = 0;
	
	public boolean repOK()
	/*
	 * Requires：none
	 * Modifies：none
	 * Effects：examine invariance
	 */
	{
		if(loc_n>10 || loc_n<1 || des_n>10 || des_n<1 || t<0)
		{
			return false;
		}
		return true;
	}
	
	int recordResponse(String str,floor F)
	/*
	 * Requires：str==“ER” or "FR"
	 *           F!=null
	 * Modifies：des_n
	 * 			 t
	 * 			 loc_n
	 * Effects：返回当前电梯运行方向，依据请求修改电梯状态信息
	 * 			1=UP
	 * 			-1=DOWN
	 * 			0=STATIC
	 */
	{
		if(!str.equals("ER") && !str.equals("FR"))
		{
			return 0;
		}
		if(str.equals("FR") && F!=null)
		{
			des_n = F.loc_n;
			if(F.t > t)
			{
				t = F.t;
			}
		}
		int	cnt = des_n - loc_n;
		if(cnt > 0)
		{
			t += 1 + 0.5 * Math.abs(cnt);
			loc_n = des_n;
			return 1;
		}
		else if(cnt < 0)
		{
			t += 1 + 0.5 * Math.abs(cnt);
			loc_n = des_n;
			return -1;
		}
		else
		{
			return 0;
		}
	}
}
