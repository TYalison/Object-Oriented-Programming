package bylift;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class queueTest
{
	private queue Q;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {}

	@Before
	public void setUp() throws Exception
	{
		Q = new queue();
	}

	@After
	public void tearDown() throws Exception {}

	@Test
	public void testRepOK()
	{
		assertEquals(true, Q.repOK());
	}

	@Test
	public void testAddQueue()
	{
		try
		{
			System.out.println("======INPUT REQUEST======");
			/*
			 * Example:
			 * FR,6,UP,0
			 */
			int res = Q.addQueue();
			int pre_size = Q.RQ.size();
			if(Q.RQ.size() > pre_size)
			{
				assertEquals(1, res);
			}
		}
		catch(Exception e)
		{
			fail();
		}
	}

	@Test
	public void testJudgeQueue()
	{
		try
		{
			System.out.println("======INPUT REQUEST======");
			/*
			 * Example:
			 * FR,6,UP,0
			 * END
			 */
			Q.judgeQueue();
		}
		catch(Exception e) {}
	}

}
