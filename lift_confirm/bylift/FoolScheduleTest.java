package bylift;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class FoolScheduleTest {
	private FoolSchedule FS;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {}

	@Before
	public void setUp() throws Exception
	{
		FS = new FoolSchedule();
	}

	@After
	public void tearDown() throws Exception {}

	@Test
	public void testRepOK()
	{
		assertEquals(true, FS.repOK());
	}
	
	@Test
	public void testF_Schedule()
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
		FS.Q.judgeQueue();
		FS.F_Schedule();
	}
}
