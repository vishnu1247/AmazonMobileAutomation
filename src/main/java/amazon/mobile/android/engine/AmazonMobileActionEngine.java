package amazon.mobile.android.engine;

import java.util.Random;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;

import amazon.config.gblConstants;
import amazon.core.accelerators.AbstractTestEngine;
import amazon.core.accelerators.AppiumActionEngine;
import amazon.core.accelerators.AppiumActionEngine.DIRECTION;
import amazon.core.logs.LogManager;
import amazon.core.reports.ExtentManager;
import io.appium.java_client.TouchAction;
import io.appium.java_client.pagefactory.AndroidFindBy;
import io.appium.java_client.touch.offset.PointOption;

public class AmazonMobileActionEngine extends AppiumActionEngine {
	
//	public static ExtentManager extentMngr=null;
	public static ExtentTest tcReport;	
	
	public void loadPage() {
		// TODO Auto-generated method stub
	}
	
    public void reportStatus(Status result, String strLogMsg){
		//Update Logger
		switch (result) {
			case WARNING:
			case PASS:			
				LogManager.logInfo(result.name(), strLogMsg);
				break;
			case INFO:			
				LogManager.logInfo(result.name(), strLogMsg);
				break;
			case FATAL:				
			case FAIL:
				LogManager.logError(result.name(), strLogMsg);
				AbstractTestEngine.extentMngr.attachScreenshot(this.tcReport, result, 
						captureScreenShot(gblConstants.reportsPath, "Mobile"));
				break;
			default:
		}
		
    } 
    
    
	@SuppressWarnings("unchecked")
	public void click(By by, String logmessage) {                 
                click(driver.findElement(by),logmessage);
	}
	
	@SuppressWarnings("unchecked")
	public void click(WebElement by, String logmessage) {    	    
        try
        {
        	
        	if (waitForElementPresent(by, gblConstants.wdWaitTimeout)) {
        		by.click();
        	    reportStatus(Status.PASS, "Clicked on "+ logmessage);
        	}
        }
        //catch (StaleElementReferenceException | NoSuchElementException e)
        catch (Exception e)
        {		
        	reportStatus(Status.FAIL, "Failed to click on " + logmessage);
            e.printStackTrace();
        }  
    }
	
	
	
	@SuppressWarnings("unchecked")
	public boolean LoginButtonClick(WebElement by, String logmessage) {    
		boolean res= false; 
        try
        {
        	
        		by.click();
        	    reportStatus(Status.PASS, "Clicked on "+ logmessage);
        	    res= true;
        }
        //catch (StaleElementReferenceException | NoSuchElementException e)
        catch (NullPointerException e)
        {		
        	
        }  
        return res; 
    }
	
	
	public boolean waitForElementVisibles(WebElement element, int seconds) {
		boolean res = false;
		WebDriverWait wait = new WebDriverWait(driver, seconds);
		try {
			wait.until(ExpectedConditions.visibilityOf(element));
			reportStatus(Status.PASS, "Waiting for element");
			res= true;
		}
		catch (Exception e)
		{
			reportStatus(Status.FAIL, "Waiting for element");
			e.printStackTrace();            
		}
		return res;
	}
	
	public boolean waitForElementPresent(WebElement by, int seconds) {
		seconds = 1;
		boolean res = false;
		WebDriverWait wait = new WebDriverWait(driver, seconds);
		try {
			wait.until(ExpectedConditions.visibilityOf(by));
			reportStatus(Status.PASS, "Waiting for element");
			res= true;
		}
		catch (NullPointerException e)
		{  
			
		}
		return res ;
	}
	
	public void enterText(WebElement by, String text){
	
		try {
			waitForElementVisible(by, gblConstants.pageLoadTimeout*2);
			driver.hideKeyboard();
			by.sendKeys(text);
        	reportStatus(Status.PASS, "Entered text " + text);
        
		}
		catch(Exception e){
        	e.printStackTrace();
        	reportStatus(Status.PASS, "Failed to enter text in " + text);
		}
	
	}
	
	
	public boolean enterTxt(WebElement by, String text){
		boolean res = false;
		try {
			waitForElementVisible(by, gblConstants.pageLoadTimeout*2);
			//driver.hideKeyboard();
			
			by.sendKeys(text);
        	reportStatus(Status.PASS, "Entered text " + text);
            res = true;
		}
		catch(Exception e){
        	e.printStackTrace();
        	reportStatus(Status.PASS, "Failed to enter text in " + text);
		}
	return res;
	}
	
	
	
	
	
	
	public String getVisibleText(By by){
		
		return driver.findElement(by).getText();
	}
	
	public String getVisibleText(WebElement by){
		waitForElementVisible(by, gblConstants.pageLoadTimeout*2);
		return by.getText();
	}
	
	public void validateTextDisplayed(WebElement element, String message, String compare) {
		
		if(element.isDisplayed()&&element.getText().equalsIgnoreCase(compare))
			reportStatus(Status.PASS, message + " ("+compare+" equals "+element.getText()+")");
		else
			reportStatus(Status.FAIL, message + " ("+compare+" !equals "+element.getText()+")");		
	
}
	
	public void validateElementDisplayed(WebElement element, String message) {
		
		if(element.isDisplayed())
			reportStatus(Status.PASS, message);
		else
			reportStatus(Status.FAIL, message);				
}
	
	public void validateFindByXpath(By by){
		 try{driver.findElement(by);
				reportStatus(Status.PASS, "Element with text = "+driver.findElement(by).getText()+" shown");}		 
		catch(Exception e){
	        	e.printStackTrace();
				reportStatus(Status.FAIL, "Element with xpath = "+by+" is not shown");}
	}
	

	 public boolean isElementPresent(WebElement element, String locatorName,boolean expected) throws Throwable{
		boolean status = false;
		try
		{
		
			//swipe(this.driver, DIRECTION.LEFT);
			status = element.isDisplayed();
	
			//this.reporter.SuccessReport("isElementPresent" , this.msgIsElementFoundSuccess + locatorName);
		
		} 
		catch(Exception e)
		{
				status = false;
			//LOG.info(e.getMessage());
			if(expected == status)
			{
				//this.reporter.SuccessReport("isElementPresent", "isElementPresent");
			}
			else
			{
				//this.reporter.failureReport("isElementPresent", this.msgIsElementFoundFailure + locatorName, this.appiumDriver);
			}
		}
	return status;
	}
	 
	 
	 
		@SuppressWarnings("rawtypes")
		public boolean SwipeAnElemnet(WebElement elestarting,WebElement eleEnding) {
			boolean res= false;
			try {	
				
				//**** Starting point to ending point Swipe.****
				
				waitForElementVisibles(elestarting,gblConstants.pageLoadTimeout);
				waitForElementVisibles(eleEnding,gblConstants.pageLoadTimeout);	 
				Thread.sleep(6000);
				new TouchAction(driver).press(PointOption.point(elestarting.getLocation()))
				                                      .moveTo(PointOption.point(eleEnding.getLocation()))
				                                      .release()
				                                      .perform();
				  res= true;
			}catch (Exception e) {
				// LogManager.logInfo(HomePage.class.getName(), "Failed SwipeAnElemnet-"+e);	
			}
			return res;
			}
		
}
