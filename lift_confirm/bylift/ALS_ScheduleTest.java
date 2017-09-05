package bylift;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class ALS_ScheduleTest
{
	private ALS_Schedule AS;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {}

	@Before
	public void setUp() throws Exception
	{
		AS = new ALS_Schedule();
	}

	@After
	public void tearDown() throws Exception
	{
		if(!AS.AR.isEmpty())
		{
			AS.AR.clear();
		}
	}

	@Test
	public void testRepOK()
	{
		assertEquals(true, AS.repOK());
	}

	@Test
	public void testClassifyRQ() {
		try
		{
			System.out.println("======INPUT REQUEST======");
			/*
			 * Example:
			 * (FR,6,UP,0)
			 * (ER,9,1)
			 * (FR,5,UP,2)
			 * (ER,8,5)
			 * ()
			 * END
			 */
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
		}
		catch(Exception e)
		{
			fail();
		}
	}

	@Test
	public void testToString()
	{
		assertEquals(null, "ALS_Schedule [AR=" + AS.AR + "]", AS.toString());
	}

	@Test
	public void testGuideRunning()
	{
		try
		{
			AS.guideRunning();
		}
		catch(Exception e)
		{
			fail();
		}
	}

	@Test
	public void testMoveConsole()
	{
		try
		{
			System.out.println("======INPUT REQUEST======");
			/*
			 * Example:
			 * (FR,6,UP,0)
			 * (ER,9,1)
			 * (FR,5,UP,2)
			 * (ER,8,5)
			 * ()
			 * END
			 */
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
		catch(Exception e)
		{
			fail();
		}
	}
}
