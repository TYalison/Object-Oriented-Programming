package bylift;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collection;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

@RunWith(Parameterized.class)
public class elevatorTest
{
	private elevator E;
	private String s;
	
	@Parameters
    public static Collection data()
    {
		/* You can add more parameters here */
		return Arrays.asList(new Object[][] { 
            { 6,2,0,"ER" },
            { 5,4,2,"FR" },
            { 10,4,3,"ER" }
            								});
    }
	
    public elevatorTest(int des_n, int loc_n, double t, String s)
    {
    	E = new elevator();
    	E.des_n = des_n;
    	E.loc_n = loc_n;
    	E.t = t;
    	this.s = s;
    }

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {}

	@Before
	public void setUp() throws Exception {}

	@After
	public void tearDown() throws Exception {}

	@Test
	public void testRepOK()
	{
		assertEquals(true, E.repOK());
	}

	@Test
	public void testRecordResponse()
	{
		try
		{
			int res = E.recordResponse(s, new floor());
			boolean flag = false;
			if(E.des_n > E.loc_n && res==1)
			{
				flag = true;
				assertEquals(true, flag);
			}
			else if(E.des_n < E.loc_n && res==-1)
			{
				flag = true;
				assertEquals(true, flag);
			}
			else if(E.des_n == E.loc_n && res==0)
			{
				flag = true;
				assertEquals(true, flag);
			}
		}
		catch(Exception e)
		{
			fail();
		}
	}

}
