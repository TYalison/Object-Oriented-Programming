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
public class requestTest
{
	private request R;
	private String r_str;
	private floor F;
	private elevator E;
	
	@Parameters
    public static Collection data()
    {
		/* You can add more parameters here */
		String s1 = "FR,6,UP,0";
		String s2 = "ER,9,1";
		String s3 = "FR,5,UP,2";
		String s4 = "ER,8,5";
		
		return Arrays.asList(new Object[][] { 
            { s1 },
            { s2 },
            { s3 },
            { s4 }
            								});
    }

    public requestTest(String s)
    {
    	r_str = s;
    }

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {}

	@Before
	public void setUp() throws Exception
	{
		R = new request();
		F = new floor();
    	E = new elevator();
	}

	@After
	public void tearDown() throws Exception {}

	@Test
	public void testRepOK()
	{
		assertEquals(true, R.repOK());
	}

	@Test
	public void testJudgeRequest()
	{
		try
		{
			R.judgeRequest(r_str);
		}
		catch(Exception e)
		{
			fail();
		}
	}

	@Test
	public void testRealizeRequest()
	{
		try
		{
			R.judgeRequest(r_str);
			R.realizeRequest(F, E);
		}
		catch(Exception e)
		{
			fail();
		}
	}

	@Test
	public void testEqualsObject()
	{
		R.judgeRequest(r_str);
		request R_temp = new request();
		assertEquals(false, R.equals(R_temp));
	}

}
