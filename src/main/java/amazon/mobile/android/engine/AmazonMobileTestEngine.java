package amazon.mobile.android.engine;

import java.io.IOException;


import org.testng.ITestContext;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.AfterTest;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import com.aventstack.extentreports.Status;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import amazon.mobile.android.pages.AmazonAppScreens;
import amazon.config.gblConstants;
import amazon.core.accelerators.AbstractTestEngine;
import amazon.core.accelerators.AppiumServer;
import amazon.core.logs.LogManager;


public class AmazonMobileTestEngine extends AbstractTestEngine {


	public AmazonMobileActionEngine appActionEngine = null;
	public AppiumServer appiumServer = null;
	
	public String appiumHost="";
	public int appiumPortNo=-1;
	Random rand = new Random();
	
	private AmazonAppScreens loginPage;
	private String adminusername;
	private String adminpassword;
	
	@Override
	@BeforeSuite(alwaysRun=true)
	@Parameters({"AppiumVerNo","AppiumServerUrl","AppUT","AppTestEnv","DeviceUT"})
	public void beforeSuite(@Optional("") String appiumVerNo, @Optional("")String appiumServerUrl, @Optional("")String appUT, 
			@Optional("") String appTestEnv, @Optional("")String devUT) {		
		//initialize 		
		this.appiumVerNo = appiumVerNo.trim();
		this.appiumServerUrl = appiumServerUrl.trim();
		this.appUnderTest = appUT.trim();
		this.testEnvName = appTestEnv.trim();
		this.deviceUnderTest = devUT.trim();
		//this.execEnv = (appiumHost.equals("0.0.0.0") || appiumHost.equals("127.0.0.1") )? "local":"remote";
		//App data--
		//this.appURL = gblConstants.suiteConfig.getTestAppUrl(this.appUnderTest, this.testEnvName);
		this.adminusername = gblConstants.suiteConfig.getTestAppLoginUser(this.appUnderTest, this.testEnvName);
		this.adminpassword = gblConstants.suiteConfig.getTestAppLoginPwd(this.appUnderTest, this.testEnvName);
		initializeReport();	
		appiumHost = appiumServerUrl.substring(appiumServerUrl.indexOf("//")+2, appiumServerUrl.indexOf(":", appiumServerUrl.indexOf("//")+1));
		appiumPortNo = Integer.valueOf(appiumServerUrl.substring(appiumServerUrl.indexOf(":", appiumServerUrl.indexOf("//"))+1, appiumServerUrl.replace("/wd/hub","").length()));						
		
		try {	
			appiumServer = new AppiumServer();
			if (!appiumServer.checkIfServerIsRunnning(appiumPortNo)){
				appiumServer.startServer(appiumPortNo);
				
			}
			//appiumServer.startServer(appiumPortNo);	
		}catch(Exception e) {
			e.printStackTrace();
		}
	}


	@Override
	@AfterSuite(alwaysRun=true)
	public void afterSuite() {
		this.extentMngr.flushReport();
		testngAssertion.assertAll();
	}
	
	@Override
	@BeforeTest
	@Parameters("testRailsId")
	public void beforeTest(ITestContext testContext, @Optional("")String testRailsTCId)  {
		
		this.appiumVerNo = getExecutionParameter(testContext, "AppiumVerNo").trim();
		this.appiumServerUrl = getExecutionParameter(testContext, "AppiumServerUrl").trim();
		this.appUnderTest = getExecutionParameter(testContext, "AppUT").trim();
		this.testEnvName = getExecutionParameter(testContext, "AppTestEnv").trim();
		this.deviceUnderTest = getExecutionParameter(testContext, "DeviceUT").trim();
		appActionEngine = new AmazonMobileActionEngine();
	
		appActionEngine.launchAppiumDriver(this.appiumServerUrl, initializeAppiumDesiredCapabilities());
		reportTestAppEnvironmentSummary();	
		
	}

	@Override
	@AfterTest
	public void afterTest() {
		this.extentMngr.flushReport();
		endTestCaseReport(this.tcReport);
		this.tcReport = null;	
	}

	@BeforeMethod
	@Parameters({"testCaseName"})
	public void beforeMethod(String tcName,ITestContext testContext) {
		this.tcReport = extentMngr.addTest(tcName, "Amazon Mobile Testing");
	}
	
	@AfterMethod
	public void flushReport() {
		this.tcReport=null;
		this.extentMngr.flushReport();
	}
	
	@AfterClass
	public void afterClass() throws IOException {
		try {
		this.extentMngr.flushReport();
		appActionEngine.closeDriver();
		}
		catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Override
	public void reportTestAppEnvironmentSummary() {
		reportMobileEnvironmentSummary();
	}

	@Override
	public boolean attachScreenshot(Status result, String screenshotName) {
		boolean blnRes = false;	
		blnRes = this.extentMngr.attachScreenshot(this.tcReport, result, 
				appActionEngine.captureScreenShot(gblConstants.reportsPath, screenshotName));		
		return blnRes;
	}
	
	@Override
	public boolean loginUsingDefaultUser() throws Throwable {
		boolean res = false ;
		loginPage = new AmazonAppScreens(this.appActionEngine);
		res= loginPage.LoginAmazonApp(adminusername, adminpassword);
		try {	

		}catch (Exception e) {
			LogManager.logError(AmazonAppScreens.class.getName(), "Failed method loginUsingDefaultUser");
		}	
		return res;
	}
	

	public String getTimeStamp(){
		return new SimpleDateFormat("MM_dd_HH_mm_ss").format(new Date());
	}
	
    public String getRandomizedAmount(int number){    	
    	return ""+(rand.nextInt(500)+number*100);
    }
    
    
    
    

}
