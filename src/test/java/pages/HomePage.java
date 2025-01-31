package pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Reporter;

public class HomePage {
	
	@FindBy(xpath="//a[text()='Logout']")
	private WebElement logoutLink;
	
	public  HomePage(WebDriver driver)
	{
		PageFactory.initElements(driver, this);
	}
	
	public boolean verifyHomePageIsDisplayed(WebDriverWait wait)
	{
		try {
			wait.until(ExpectedConditions.visibilityOf(logoutLink));
			Reporter.log("HomePage is  Displayed",true);
			return true;
		}
		catch(Exception e)
		{
			e.printStackTrace();
			Reporter.log("HomePage is not Displayed",true);
			return false;
		}
		
	}

}
