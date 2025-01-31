package generic;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;

import java.net.URL;
import java.time.Duration;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;


import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;

public class BaseTest implements IAutoConst
{

	public WebDriver driver;
	public WebDriverWait wait;
	public ExtentTest test;
	
	
	
	@BeforeSuite
	public void initReport()
	{
		
		ExtentSparkReporter spark=new ExtentSparkReporter("./target/spark.html");
		extent.attachReporter(spark);
	}
	
	@AfterSuite
	public void publishReport()
	{
		extent.flush();
	}
	
	@Parameters({"browser","grid","gridURL"})
	@BeforeMethod
	public void preCondition(Method method,@Optional(Browser) String browser,
			@Optional(GRID) String grid,@Optional(GRID_URL) String gridURL) throws Exception
	{
		String testName = method.getName();
		test = extent.createTest(testName);
		
		String AppURL = Utility.getProperties(CONFIG_PATH, "APPURL");
		test.info("APPURL: "+AppURL);
		
		String strITO = Utility.getProperties(CONFIG_PATH, "ITO");
		Long lngITO=Long.parseLong(strITO);
		test.info("ITO: "+lngITO);
		
		String strETO = Utility.getProperties(CONFIG_PATH, "ETO");
		Long lngETO=Long.parseLong(strETO);
		test.info("ETO: "+lngETO);
		
		if(grid.equalsIgnoreCase("yes"))
		{
			if(browser.equalsIgnoreCase("chrome"))
			{
				driver=new RemoteWebDriver(new URL(gridURL),new ChromeOptions());
				test.info("Open Chrome Browser in Remote System");
			}
			
			else if(browser.equalsIgnoreCase("firefox"))
			{
				driver=new RemoteWebDriver(new URL(gridURL),new FirefoxOptions());
				test.info("Open firefox Browser in Remote System");
			}
			
			else
			{
				driver=new RemoteWebDriver(new URL(gridURL),new EdgeOptions());
				test.info("Open Edge Browser in Remote System");
			}
		}
		else
		{
			if(browser.equalsIgnoreCase("chrome"))
			{
				driver=new ChromeDriver();
				test.info("Open Chrome Browser in Local System");
			}
			
			else if(browser.equalsIgnoreCase("firefox"))
			{
				driver=new FirefoxDriver();
				test.info("Open firefox Browser in Local System");
			}
			
			else
			{
				driver=new EdgeDriver();
				test.info("Open Edge Browser in Local System");
			}
		}
		
		driver.get(AppURL);
		test.info("Enter the URL: "+AppURL);
		
		driver.manage().window().maximize();
		test.info("Maximizing the Browser");
		
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(lngITO));
		test.info("Setting ITO: "+lngITO);
		
		wait=new WebDriverWait(driver, Duration.ofSeconds(lngETO));
		test.info("Setting ETO: "+lngETO);
	}
	
	@AfterMethod
	public void postCondition(ITestResult testresult) throws IOException 
	{
		String testname = testresult.getName();
		int status = testresult.getStatus();
		
		if(status==2)
		{
			TakesScreenshot t=(TakesScreenshot) driver;
			File srcImage = t.getScreenshotAs(OutputType.FILE);
			File dstImage=new File("./target/screenshots/"+testname+".png");
			FileUtils.copyFile(srcImage, dstImage);
			String msg = testresult.getThrowable().getMessage();
			test.fail(msg,MediaEntityBuilder.createScreenCaptureFromPath("./screenshots/"+testname+".png").build());
		}
		
		driver.quit();
		test.info("Close the Browser");
	}
}
