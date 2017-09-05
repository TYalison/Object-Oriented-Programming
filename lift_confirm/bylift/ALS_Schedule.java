package bylift;

import java.util.ArrayList;

public class ALS_Schedule extends FoolSchedule implements status
/*
 * Overview：增加顺路捎带的电梯调度
 * 表示对象：请求队列AR
 *           上行up
 *           下行down
 *           当前楼层loc_n
 *           系统相对时间t
 * 不变式：1=<loc_n<=10
 *         t>=0
 * 抽象函数：AF(AR)={AR.add(req_temp) if req_temp is valid and new; AR.remove if a req is finished}
 *           AF(up)={up=true only if FR.UP or ER.des_n>loc_n; up=false otherwise}
 *           AF(down)={down=true only if FR.DOWN or ER.des_n<loc_n; down=false otherwise}
 *           AF(loc_n)={increase when UP_finished; reduce when DOWN_finished}
 *           AF(t)={t=t_temp when new req began; t+=0.5 when loc_n+=1 or loc_n-=1; t+=1 when lift open or close}
 */
{
	ArrayList<request> AR = new ArrayList<request>();
	boolean up = false;
	boolean down = false;
	int loc_n = 1;
	double t = 0;
	
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
	
	public ALS_Schedule()
	/*
	 * Requires：none
	 * Modifies：none
	 * Effects：initialization
	 */
	{
		super();
	}
	
	public static void main(String[] args)
	/*
	 * Requires：none
	 * Modifies：Q
	 * 			 AR
	 * Effects：main方法，实现捎带功能的电梯调度，输出请求捎带信息和电梯停靠信息
	 *          当输入请求无效时输出提示信息
	 */
	{
		ALS_Schedule AS = new ALS_Schedule();
        AS.Q.judgeQueue();
		for(int i = 0; i < AS.Q.RQ.size(); i++)
		{
			if(!AS.R.judgeRequest(AS.Q.RQ.get(i)))
			{
				System.out.println("第" + (i + 1) + "条请求是无效的！");
			}
		}
		while(AS.Q.RQ.size() != 0)
		{
			AS.classifyRQ();
		}
		if(!AS.AR.isEmpty())
		{
			AS.moveConsole();
		}
	}
	
	@Override
	public void classifyRQ()
	/*
	 * Requires：none
	 * Modifies：Q
	 * 			 AR
	 * 			 t
	 * 			 up
	 * 			 down
	 * 			 E
	 * Effects：根据请求时间的先后和楼层相对位置比较输出请求捎带信息，完成请求队列的重排序
	 */
	{
		double maxT = 0;
		double priT = 0;
		int priDN = 1;
		int priLN = 1;
		int j = 0;
		int order = 0;
		R = new request();
		if(R.judgeRequest(Q.RQ.get(0)))
		{
			boolean Trans = false;
			boolean found_master = false;
			int flag = 0;
			for(request tmpR : AR)
			{
				if(R.equals(tmpR))
				{
					found_master = true;
					break;
				}
			}
			if(!found_master)
			{
				AR.add(R);
			}
			if(R.t < t)
			{
				R.t = t;
			}
			System.out.print("请求捎带信息：(" + R.s + ")");
			guideRunning();
			R.up = up;
			R.down = down;
			priLN = E.loc_n;
			priDN = R.des_n;
			priT = R.t;
			maxT = R.t + 0.5 * Math.abs(R.des_n - E.loc_n);
			Q.RQ.remove(0);
			while(j < Q.RQ.size())
			{
				boolean found_slave = false;
				R = new request();
				if(R.judgeRequest(Q.RQ.get(j)))
				{
					if(up)
					{
						R.up = true;
						R.down = false;
						if(R.tag[0].equals("FR") && R.tag[2].equals("UP") || R.tag[0].equals("ER"))
						{
							if(!R.tag[0].equals("ER"))
							{
								maxT += 1;
							}
							E.loc_n = priLN + (int)((R.t - priT) * 2);
							if(E.loc_n > priDN)
							{
								E.loc_n = priDN;
							}
							if(R.des_n >= E.loc_n && R.t <= maxT)
							{
								for(request tmpR : AR)
								{
									if(R.equals(tmpR))
									{
										found_slave = true;
										break;
									}
								}
								if(!found_slave)
								{
									AR.add(R);
								}
								flag++;
								if(flag == 1)
								{
									System.out.print("(");
								}
								System.out.print("(" + R.s + ")");
								if(R.t + 0.5 * Math.abs(R.des_n - E.loc_n) < maxT)
								{
									maxT += 1;
									if(Trans)
									{
										String str = Q.RQ.get(j);
										Q.RQ.remove(j);
										Q.RQ.add(order, str);
										order++;
									}
									else
									{
										Q.RQ.remove(j);
										j--;
									}
								}
								else
								{
									Trans = true;
									String str = Q.RQ.get(j);
									Q.RQ.remove(j);
									Q.RQ.add(order, str);
									order++;
								}
							}
						}
					}
					else if(down)
					{
						R.up = false;
						R.down = true;
						if(R.tag[0].equals("FR") && R.tag[2].equals("DOWN") || R.tag[0].equals("ER"))
						{
							E.loc_n = priLN - (int)((R.t - priT) * 2);
							if(E.loc_n < priDN)
							{
								E.loc_n = priDN;
							}
							if(R.des_n <= E.loc_n && R.t <= maxT)
							{
								AR.add(R);
								flag++;
								if(flag == 1)
								{
									System.out.print("(");
								}
								System.out.print("(" + R.s + ")");
								if(R.t + 0.5 * Math.abs(R.des_n - E.loc_n) < maxT)
								{
									maxT += 1;
									Q.RQ.remove(j);
									j--;
								}
								else
								{ 
									String str = Q.RQ.get(j);
									Q.RQ.remove(j);
									j--;
									Q.RQ.add(order, str);
									order++;
								}
							}
						}
					}
					else
					{
						Q.RQ.remove(j);
						j--;
					}
				}
				else
				{
					Q.RQ.remove(j);
					j--;
				}
				j++;
			}
			if(flag > 0)
			{
				System.out.print(")");
			}
			System.out.print("\n");
		}
		else
		{
			Q.RQ.remove(0);
		}
		t = maxT;
	}
	
	@Override
	public String toString()
	/*
	 * Requires：
	 * Modifies：
	 * Effects：以一定格式返回ALS调度中的AR信息
	 */
	{
		return "ALS_Schedule [AR=" + AR + "]";
	}

	@Override
	public void guideRunning()
	/*
	 * Requires：none
	 * Modifies：up
	 * 			 down
	 * Effects：根据电梯当前楼层和请求目标楼层设置方向
	 * 			若当前楼层低于目标楼层，令up=true\down=false
	 * 			若当前楼层高于目标楼层，令up=false\down=true
	 * 			否则，up=down=false
	 */
	{
		if(R.des_n > E.loc_n)
		{
			up = true;
			down = false;
		}
		else if(R.des_n < E.loc_n)
		{
			up = false;
			down = true;
		}
		else
		{
			up = false;
			down = false;
		}
	}
	
	@Override
	public void moveConsole()
	/*
	 * Requires：none
	 * Modifies：AR
	 *           E
	 *           loc_n
	 * Effects：根据捎带信息排序后的请求队列完成电梯调度
	 */
	{
		int ult_des_n = 1;
		int flag;
		double t = AR.get(0).t;
		String s = new String();
		if(E.t > t)
		{
			t = E.t;
		}
		while(AR.size() > 0)
		{
			flag = 0;
			ult_des_n = AR.get(0).des_n;
			if(AR.get(0).up)
			{
				s = "UP";
			}
			else if(AR.get(0).down)
			{
				s = "DOWN";
			}
			for(int j = 1; j < AR.size(); j++)
			{
				if(AR.get(j).up && AR.get(j).des_n < ult_des_n)
				{
					ult_des_n = AR.get(j).des_n;
					flag = j;
				}
				else if(AR.get(j).down && AR.get(j).des_n > ult_des_n)
				{
					ult_des_n = AR.get(j).des_n;
					flag = j;
				}
			}
			if(ult_des_n != loc_n)
			{
				t += 1 + 0.5 * Math.abs(ult_des_n - loc_n);
				E.t = t;
				System.out.println("电梯停靠信息：(" + ult_des_n + "," + s + "," + t + ")");
				loc_n = ult_des_n;
			}
			AR.remove(flag);
		}
	}
}