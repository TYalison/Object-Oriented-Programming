package bylift;

public class FoolSchedule
/*
 * Overview：描述单电梯傻瓜调度的基本实现，包括楼层、电梯、请求和队列等基本对象，以及响应请求完成调度等基本操作
 * 表示对象：楼层F
 * 			 电梯E
 * 			 请求R
 * 			 请求队列Q
 * 不变式：none
 * 抽象函数：AF(F)={E.set when FR.req_response or req_finished}
 * 			 AF(E)={E.set when req_response or req_finished}
 * 			 AF(R)={R.set when a new req is read}
 * 			 AF(Q)={Q.add(req) when req is new and valid; Q.remove() when a req is finished}
 */
{
	floor F = new floor();
	elevator E = new elevator();
    request R = new request();
	queue Q = new queue();
	
	public boolean repOK()
	/*
	 * Requires：none
	 * Modifies：none
	 * Effects：examine invariance
	 */
	{
		if(!F.repOK() || !E.repOK() || !R.repOK() || !Q.repOK())
		{
			return false;
		}
		return true;
	}
	
	void F_Schedule()
	/*
	 * Requires：none
	 * Modifies：Q
	 * 			 R
	 * 			 F
	 * 			 E
	 * Effects：实现傻瓜调度
	 * 			若请求无效则输出提示信息
	 * 			否则按请求时间排序逐条响应完成调度
	 */
	{
		int i = 0;
		
		while(i < Q.RQ.size())
		{
			if(R.judgeRequest(Q.RQ.get(i)))
			{
				if(R.realizeRequest(F, E))
				{
					switch(E.recordResponse(R.tag[0], F))
					{
					case 1:
						System.out.println("(" + E.des_n + ",UP," + E.t + ")");
						break;
					case -1:
						System.out.println("(" + E.des_n + ",DOWN," + E.t + ")");
						break;
					case 0:
						System.out.println("Invalid request!");
						break;
					}
				}
				else
				{
					System.out.println("Invalid request!");
				}
			}
			else
			{
				System.out.println("Invalid request!");
			}
			i++;
		}
	}

	public static void main(String[] args)
	/*
	 * Requires：none
	 * Modifies：FS
	 * Effects：main方法，实现傻瓜调度功能的电梯实例
	 */
	{
		FoolSchedule FS = new FoolSchedule();
		FS.Q.judgeQueue();
		FS.F_Schedule();
	}
}
