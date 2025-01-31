package script;



import org.testng.Reporter;
import org.testng.annotations.Test;

import generic.BaseTest;

public class DemoTest2 extends BaseTest
{

	@Test
	public void testInvalidLogIn() 
	{
		String title=driver.getTitle();
		Reporter.log(title,true);
		test.info("Title: "+title);
			
	}
}
