package amazon.core.accelerators;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileBy;
import io.appium.java_client.MobileElement;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.remote.MobileCapabilityType;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.TimeUnit;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;
import com.aventstack.extentreports.Status;


import amazon.config.gblConstants;
import amazon.core.logs.LogManager;

public class AppiumActionEngine {

	public AppiumDriver driver;
	
	 
	//public DesiredCapabilities capabilities;

	public AppiumDriver getDriver() {
		return driver;
	}


	public void launchAppiumDriver(String appiumServerUrl, DesiredCapabilities caps) {	
			
		String platformName = caps.getCapability(MobileCapabilityType.PLATFORM_NAME).toString();
		appiumServerUrl = (appiumServerUrl.toLowerCase().contains("/wd/hub") )? appiumServerUrl: appiumServerUrl + "/wd/hub"; 
		
		try {
			switch (platformName.trim().toUpperCase()) {
			case "ANDROID":
				
				URL serverUrl = new URL(appiumServerUrl);
				driver = new AndroidDriver(serverUrl, caps);
				driver.manage().timeouts().implicitlyWait(0, TimeUnit.MILLISECONDS);								
				((AndroidDriver) driver).unlockDevice();
		
			default:
				//return null;
			}			
		}catch(MalformedURLException me) {
			me.printStackTrace();
		}catch(Exception e) {
			e.printStackTrace();
		}
		
	//	return driver;
	}
	
	protected boolean isElementPresent(By by) {
		try {
			driver.findElement(by);
			return true;
		} catch (NoSuchElementException e) {
			return false;
		}
	}

	public void hideKeyboard() {
		try {
			driver.hideKeyboard();
			LogManager.logInfo(AppiumActionEngine.class.getName(), "Hiding Keyboard...");
		} catch (Exception e) {
			LogManager.logInfo(AppiumActionEngine.class.getName(), "Failed to hide keyboard...");
		}
	}


	public enum DIRECTION {
	    DOWN, UP, LEFT, RIGHT;
	}


		public void waitForElementVisible(WebElement element, int seconds) {
		WebDriverWait wait = new WebDriverWait(driver, seconds);
		try {
			wait.until(ExpectedConditions.visibilityOf(element));
			LogManager.logInfo(AppiumActionEngine.class.getName(), "Waiting for element");
		}
		catch (Exception e)
		{
			LogManager.logInfo(Status.FAIL.name(), "Element could not be located.");
			e.printStackTrace();            
		}
		
	}
	
	@SuppressWarnings("unchecked")
	public void waitUntilClickable(WebElement by) {

        @SuppressWarnings("rawtypes")
		Wait wait = new FluentWait(driver)
        		.withTimeout(60, TimeUnit.SECONDS)
        		.pollingEvery(3, TimeUnit.SECONDS)
        		.ignoring(StaleElementReferenceException.class);
        	    
                try
                {   wait.until(ExpectedConditions.refreshed(
        		        ExpectedConditions.elementToBeClickable(by)));
                }
                catch (Exception e)
                {		
                    e.printStackTrace();
                }  
	}
	
	
    public String captureScreenShot(String strReportsPath, String strSSName) {
    	try{
        	TakesScreenshot ts = (TakesScreenshot) this.driver;
            File source = ts.getScreenshotAs(OutputType.FILE);
            String epoch2 = String.valueOf(System.currentTimeMillis() / 1000);
            if(strSSName.length()<20) {
            strSSName = strSSName + "_" + epoch2 + ".png";
            }else {
            	strSSName = strSSName.substring(0, 20) + "_" + epoch2 + ".png";
            }
            String dest = strReportsPath + File.separator + gblConstants.screenshotDirName + File.separator + strSSName;
            File destination = new File(dest);
            FileUtils.copyFile(source, destination);
            
            return strSSName;
    	}catch(Exception e){
    		LogManager.logException(e, AppiumActionEngine.class.getName(), "Exception to capture screenshot");
    		return "";
    	}
    }

	
	public void shortPause(){
		try {
			Thread.sleep(gblConstants.defSleepTime*2);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	

	
	@SuppressWarnings("unchecked")
	public void click(By by) {
        click(driver.findElement(by));
	}
	
	@SuppressWarnings("unchecked")
	public boolean click(WebElement by) {
		boolean res= false; 
		waitForElementVisible(by, gblConstants.pageLoadTimeout/20);  
	    waitUntilClickable(by);        	    
        try
        {
        	//driver.hideKeyboard();
        	by.click();
        	res= true;
        }
        catch (Exception e)
        {		
        	LogManager.logInfo(AppiumActionEngine.class.getName(), "Click on element "+ by.getTagName() + " failed");
            e.printStackTrace();
        } 
        return res;
	}
	
	
	public void enterText(WebElement by, String text){
		try {
			by.sendKeys(text);
			LogManager.logInfo(AppiumActionEngine.class.getName(), "Entered text " + text + " in " + by.getTagName());
		}
		catch(Exception e){
        	e.printStackTrace();
        	LogManager.logInfo(AppiumActionEngine.class.getName(), "Failed to enter text in " + text + " in " + by.getTagName());
		}
	}
	
	
	
	
	/**
	 * Binding to get Xpath, CSS, Link, Partial link element
	 *
	 * @param locator locator of element in xpath=locator; css=locator etc
	 * @return found WebElement
	 */
	public WebElement getElement(final String locator) {
		return getElement(locator, true);
	}


	/**
	 * Get "By" object to locate element
	 *
	 * @param locator locator of element in xpath=locator; css=locator etc
	 * @return by object
	 */
	public By byLocator(final String locator) {
		String prefix = locator.substring(0, locator.indexOf('='));
		String suffix = locator.substring(locator.indexOf('=') + 1);

		switch (prefix) {
		case "xpath":
			return By.xpath(suffix);
		case "css":
			return By.cssSelector(suffix);
		case "link":
			return By.linkText(suffix);
		case "partLink":
			return By.partialLinkText(suffix);
		case "id":
			return By.id(suffix);
		case "name":
			return By.name(suffix);
		case "tag":
			return By.tagName(suffix);
		case "className":
			return By.className(suffix);
		default:
			return null;
		}
	}


/**
	 * @param locator          locator of element in xpath=locator; css=locator etc
	 * @param screenShotOnFail make screenshot on failed attempt
	 * @return found WebElement
	 */
	protected WebElement getElement(final String locator, boolean screenShotOnFail) {
		try {
			return driver.findElement(byLocator(locator));
		} catch (Exception e) {
			if (screenShotOnFail) ;
			throw e;
		}
	}
	
	public void closeDriver() {
		driver.quit();
		driver=null;
	}
	
	
	public boolean ScroolElementVisible(String parentResourceID,String TextTOScrol) {
		boolean res= false;
		try {	
		
			
			MobileElement element = (MobileElement) driver.findElement(MobileBy.AndroidUIAutomator(
					"new UiScrollable(new UiSelector().resourceId(\""+parentResourceID+"\")).scrollIntoView("
					+ "new UiSelector().text(\""+TextTOScrol+"\"))"));
			res= true;

		}catch (Exception e) {
			// LogManager.logInfo(HomePage.class.getName(), "Failed ScroolElementVisible");	
		}
		return res;
	}


	
}