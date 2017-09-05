package bylift;

public class floor
/*
 * Overview：描述一个楼层的基本实现
 *           包括楼层号和时间等基本对象
 * 表示对象：楼层位置loc_n
 * 			 当前系统相对时间t
 * 不变式：1=<loc_n<=10
 * 		   t>=0
 * 抽象函数：none
 */
{
	int loc_n = 1;
	double t = 0.;
	
	public boolean repOK()
	/*
	 * Requires：none
	 * Modifies：none
	 * Effects：examine invariance
	 */
	{
		if(loc_n<1 || loc_n>10 || t<0)
		{
			return false;
		}
		return true;
	}
}
