package amazon.core.accelerators;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.ITestContext;
import org.testng.asserts.SoftAssert;

import com.aventstack.extentreports.AnalysisStrategy;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.configuration.Theme;

import amazon.config.gblConstants;
import amazon.config.gblConstants.ComparisionType;
import amazon.core.logs.LogManager;
import amazon.core.reports.ExtentManager;
//import a2b.core.testmanagement.TestRailsAPIAccess;
import io.appium.java_client.remote.AndroidMobileCapabilityType;
import io.appium.java_client.remote.MobileCapabilityType;

public abstract class AbstractTestEngine {

                //To store suite execution start time stamp
                public static String suiteStartTime = "";

                //Reporting references
                public static ExtentManager extentMngr=null;
                public static ExtentTest tcReport = null;              

                //System parameters
                public String hostName="";
                public String hostIP = "";
                public String osName = System.getProperty("os.name");

              
                //Mobile execution params
                public String appiumVerNo = "";
                public String appiumServerUrl = "";
                public DesiredCapabilities appiumDesiredCaps  = new DesiredCapabilities();
                //Mobile platform execution params       
                public String deviceUnderTest = null;

                //App parameters
                public String appUnderTest = null;
                public String testEnvName = null;
                
                protected SoftAssert testngAssertion = new SoftAssert();
                
                /*
                *purpose: To Initialize extent reports and update system info
                *author-date : 
                *reviewer-date:
                */
                public void initializeReport() {

                                try {
                                                suiteStartTime = new SimpleDateFormat("dd_MMM_yyyy_hh_mm_ss_SSS").format(new Date());
                        extentMngr = new ExtentManager(gblConstants.currSuiteName + "_" + suiteStartTime, gblConstants.reportsPath);
                        extentMngr.setAnalysisStrategy(AnalysisStrategy.TEST);
                        extentMngr.configReportTitle(this.appUnderTest + "-Test Results");
                        extentMngr.configReportName(this.appUnderTest);
                        extentMngr.configTheme(Theme.STANDARD);          

                                //Capture testing host machine details
                        InetAddress myHost;
                                                myHost = InetAddress.getLocalHost(); 
                        hostName = myHost.getHostName();
                        hostIP = InetAddress.getLocalHost().getHostAddress();

                        //Update system information in extent reports        
                        extentMngr.setSystemInfo("Host O.S", this.osName);
                        extentMngr.setSystemInfo("Host Name - ", this.hostName);
                        extentMngr.setSystemInfo("Host I.P- ", this.hostIP);                

                        extentMngr.flushReport();
                                }catch (UnknownHostException e) {
                                                e.printStackTrace();
                                }                              
                }

                
               
                /*
                * Purpose: To update web application under test name & its environment details
                *author-date : Vishnu 
                *reviewer-date: 
                 */
                public abstract void reportTestAppEnvironmentSummary();
                
          /*      * Purpose: To update web application under test name & its environment details
                * author-date : Vishnu 
                * reviewer-date: */
                 
                public void reportMobileEnvironmentSummary() {
        
                                extentMngr.setSystemInfo("Appium Ver No", this.appiumVerNo);
        extentMngr.setSystemInfo("Appium Sever Url", this.appiumServerUrl);
                                extentMngr.setSystemInfo("App Under Test", appUnderTest);
        extentMngr.setSystemInfo("App Test Environment", testEnvName);
        extentMngr.setSystemInfo("Mobile Under Test", this.deviceUnderTest);
        extentMngr.setSystemInfo("Mobile OS Type", gblConstants.suiteConfig.getMobileDevicePlatform(this.appUnderTest, this.deviceUnderTest));
        extentMngr.setSystemInfo("Mobile OS Version", gblConstants.suiteConfig.getMobileDevicePlatformVer(this.appUnderTest, this.deviceUnderTest));

                }

                

                /*
                * Purpose: To initialize desired capabilities for appium driver
                * author: Vishnu 
                * reviewer: 
                 */
                public DesiredCapabilities initializeAppiumDesiredCapabilities() {                               

                                String platformType = gblConstants.suiteConfig.getMobileDevicePlatform(this.appUnderTest, this.deviceUnderTest);

                                switch(platformType.trim().toUpperCase()) {
                                                case "ANDROID":
                                                                //Define desired capabilities
                                                               // appiumDesiredCaps.setCapability(MobileCapabilityType.APPIUM_VERSION, this.appiumVerNo);
                                                                appiumDesiredCaps.setCapability(MobileCapabilityType.PLATFORM_NAME, gblConstants.suiteConfig.getMobileDevicePlatform(this.appUnderTest, this.deviceUnderTest));                  
                                                                appiumDesiredCaps.setCapability(MobileCapabilityType.PLATFORM_VERSION, gblConstants.suiteConfig.getMobileDevicePlatformVer(this.appUnderTest, this.deviceUnderTest));
                                                                appiumDesiredCaps.setCapability(MobileCapabilityType.DEVICE_NAME, gblConstants.suiteConfig.getMobileDeviceName(this.appUnderTest, this.deviceUnderTest));
                                                                appiumDesiredCaps.setCapability(AndroidMobileCapabilityType.APP_PACKAGE, gblConstants.suiteConfig.getAndroidAppPackageName(this.appUnderTest));
                                                                appiumDesiredCaps.setCapability(AndroidMobileCapabilityType.APP_ACTIVITY, gblConstants.suiteConfig.getAndroidAppPackageActivity(this.appUnderTest));
                                                                //appiumDesiredCaps.setCapability("automationName", gblConstants.suiteConfig.getAndroidAutomationName(this.appUnderTest));
                                                                appiumDesiredCaps.setCapability("skipUnlock", "true");
                                                                appiumDesiredCaps.setCapability("noReset", "true");  
                                                                appiumDesiredCaps.setCapability("newCommandTimeout", 999999);  
                                                                break;
                                                default:
                                                                appiumDesiredCaps = null;
                                }
                                return appiumDesiredCaps;
                }
                
                /*
                *Purpose: To get input execution parameters from command line or testng xml
                *Author-date: Vishnu 
                *reviewer-date:  
                 */          
                protected String getExecutionParameter(ITestContext testContext, String paramName) {                
                                String paramValue = null;
                                try {
                                                //Check if execution parameter is passed through command line
                                                if (System.getProperty(paramName) !=null && System.getProperty(paramName).trim().length() > 0 ) {                                                   
                                                                paramValue = System.getProperty(paramName).trim();
                                                //Read execution parameter value from testng xml
                                                }else if (testContext.getCurrentXmlTest().getParameter(paramName) != null) {
                                                                paramValue = testContext.getCurrentXmlTest().getParameter(paramName).trim();
                                                }                                                                                                              
                                }catch(Exception e) {
                                                LogManager.logException(e, AbstractTestEngine.class.getName(), "Exception to read value of xml parameter " + paramName);
                                                e.printStackTrace();
                                }
                                return paramValue;
                }

 
                /*
                *Purpose: To update extent reports and test rails with given test case report log
                *Author-date: Vishnu 
                *reviewer-date:  
                 */
                public void endTestCaseReport(ExtentTest tcReportLog) {
                    this.extentMngr.flushReport();
                    this.tcReport = null;
                }

                
                /*
                * Purpose: To report status to current tcReport and debugLog
                */
    public void reportStatus(Status result, String strLogMsg){

                                //Update HTML Report
                                this.tcReport.log(result, strLogMsg);

                                //Update Logger
                                switch (result) {
                                                case WARNING:
                                                case PASS:                          
                                                                testngAssertion.assertTrue(true, strLogMsg);
                                                                LogManager.logInfo(result.name(), strLogMsg);
                                                                break;
                                                case FATAL:                                                        
                                                case FAIL:
                                                                testngAssertion.assertTrue(false,strLogMsg);
                                                                LogManager.logError(result.name(), strLogMsg);
                                                                break;
                                                default:
                                }
    } 

    

                /*
                * Purpose: To report status to current tcReport and debugLog
                */
    public void reportStatus(boolean blnRes, String successMsg, String failureMsg){

                                if (blnRes) {
                                                reportStatus(Status.PASS, successMsg);
                                }else {
                                                reportStatus(Status.FAIL, failureMsg);
                                                attachScreenshot(Status.FAIL, failureMsg); // Added to attach screen shot on failure
                                }
    }         
    

                /*
                * Purpose: To report status status with a screenshot
                * author-date : Vishnu 
                */   
    public void reportStatus(Status result, String strLogMsg, String strScreenShotName ){                                  

                                //Update HTML Report
                reportStatus(result, strLogMsg);
                attachScreenshot(result, strScreenShotName);
    }    

                /*
                * Purpose: To report status status based on comparison type
                * author-date : Vishnu 
                */       
    public Boolean reportStatus(String objName, String actual, ComparisionType Type, String expected, String strScreenShotName)  {
                Boolean status = false;
                switch (Type) {
                                case EQUAL :
                                                if (actual.equals(expected)) {
                                                                reportStatus(Status.PASS, "Verification of value [" + objName + "] : Actual ->[" + actual + "] equal to Expected ->[" + expected + "]", strScreenShotName);
                                                                status = true;
                                                } else {
                                                                reportStatus(Status.FAIL, "Verification of value [" + objName + "] : Actual ->[" + actual + "] not equal to Expected ->[" + expected + "]", strScreenShotName);     
                                                                status = false;
                                                } 
                                                break;
                                                
                                case EQUAL_IGNORECASE:          
                                               if (actual.equalsIgnoreCase(expected)) {
                                                                reportStatus(Status.PASS, "Verification of value [" + objName + "] : Actual ->[" + actual + "] equal to ignorecase to Expected ->[" + expected + "]", strScreenShotName);
                                                                status = true;
                                                } else{
                                                                reportStatus(Status.FAIL, "Verification of value [" + objName + "] : Actual ->[" + actual + "] not equal to ignorecase to Expected ->[" + expected + "]", strScreenShotName);         
                                                                status = false;
                                                } 
                                                break;
                                                
                                case CONTAINS:               
                                                if (actual.contains(expected)) {
                                                                reportStatus(Status.PASS, "Verification of value [" + objName + "] : Actual ->[" + actual + "] contains Expected ->[" + expected + "]", strScreenShotName);
                                                                status = true;
                                                } else{
                                                                reportStatus(Status.FAIL, "Verification of value [" + objName + "] : Actual ->[" + actual + "] not contains Expected ->[" + expected + "]", strScreenShotName);    
                                                                status = false;
                                                } 
                                                break;
                                                
                                case SUBSTRING_OF:     
                                                if (expected.contains(actual)) {
                                                                reportStatus(Status.PASS, "Verification of value [" + objName + "] : Actual ->[" + actual + "] is substring of Expected ->[" + expected + "]", strScreenShotName);
                                                                status = true;
                                                } else{
                                                                reportStatus(Status.FAIL, "Verification of value [" + objName + "] : Actual ->[" + actual + "] is not substring of Expected ->[" + expected + "]", strScreenShotName);          
                                                                status = false;
                                                } 
                                                break;
                                                
                                case NOT_EQUAL:           
                                                if (!actual.equals(expected)) {
                                                                reportStatus(Status.PASS, "Verification of value [" + objName + "] : Actual ->[" + actual + "] not equal to Expected ->[" + expected + "]", strScreenShotName);
                                                                status = true;
                                                } else{
                                                                reportStatus(Status.FAIL, "Verification of value [" + objName + "] : Actual ->[" + actual + "] equal to Expected ->[" + expected + "]", strScreenShotName);             
                                                                status = false;
                                                } 
                                                break;
                }
                return status;
    }
    
                /*
                * Purpose: To report status status based on comparison type
                * author-date : Vishnu 
                */           
    public Boolean reportStatus(String objName, String actual, ComparisionType Type, String expected)  {
                Boolean status = false;
                String strScreenShotName;
                if (objName == null || objName.isEmpty() )  {
                                objName = "";
                                Date date= new Date();
                                strScreenShotName = Long.toString(date.getTime());
                } else {
                                strScreenShotName = objName;
                }
                switch (Type) {
                                case EQUAL :
                                                if (actual.equals(expected)) {
                                                                reportStatus(Status.PASS, "Verification of value [" + objName + "] : Actual ->[" + actual + "] equal to Expected ->[" + expected + "]");
                                                                status = true;
                                                } else {
                                                                reportStatus(Status.FAIL, "Verification of value [" + objName + "] : Actual ->[" + actual + "] not equal to Expected ->[" + expected + "]", strScreenShotName);     
                                                                status = false;
                                                } 
                                                break;
                                                
                                case EQUAL_IGNORECASE:          
                                                if (actual.equalsIgnoreCase(expected)) {
                                                                reportStatus(Status.PASS, "Verification of value [" + objName + "] : Actual ->[" + actual + "] equal to ignorecase to Expected ->[" + expected + "]");
                                                                status = true;
                                                } else{
                                                                reportStatus(Status.FAIL, "Verification of value [" + objName + "] : Actual ->[" + actual + "] not equal to ignorecase to Expected ->[" + expected + "]", strScreenShotName);         
                                                                status = false;
                                                } 
                                                break;
                                                
                                case CONTAINS:               
                                               if (actual.contains(expected)) {
                                                                reportStatus(Status.PASS, "Verification of value [" + objName + "] : Actual ->[" + actual + "] contains Expected ->[" + expected + "]");
                                                                status = true;
                                                } else{
                                                                reportStatus(Status.FAIL, "Verification of value [" + objName + "] : Actual ->[" + actual + "] not contains Expected ->[" + expected + "]", strScreenShotName);    
                                                                status = false;
                                                } 
                                                break;
                                                
                                case SUBSTRING_OF:     
                                                if (expected.contains(actual)) {
                                                                reportStatus(Status.PASS, "Verification of value [" + objName + "] : Actual ->[" + actual + "] is substring of Expected ->[" + expected + "]");
                                                                status = true;
                                                } else{
                                                                reportStatus(Status.FAIL, "Verification of value [" + objName + "] : Actual ->[" + actual + "] is not substring of Expected ->[" + expected + "]", strScreenShotName);          
                                                                status = false;
                                                } 
                                                break;
                                                
                                case NOT_EQUAL:           
                                                if (!actual.equals(expected)) {
                                                                reportStatus(Status.PASS, "Verification of value [" + objName + "] : Actual ->[" + actual + "] not equal to Expected ->[" + expected + "]");
                                                                status = true;
                                                } else{
                                                                reportStatus(Status.FAIL, "Verification of value [" + objName + "] : Actual ->[" + actual + "] equal to Expected ->[" + expected + "]", strScreenShotName);             
                                                                status = false;
                                                } 
                                                break;
                }
                return status;
    }    
    
                /*
                * Purpose: To check for instances of given process and kill them
                * author-date : Vishnu 
                */  
    public boolean killProcess(String processName) {
                                boolean flag=false;
                                
                                try {
                                                if(osName.toUpperCase().contains("WINDOWS")) {
                                                                //Get running proceses list
                                                                Process pro = Runtime.getRuntime().exec("tasklist");
                                                                BufferedReader reader = new BufferedReader(new InputStreamReader(pro.getInputStream()));
                                                                String line;
                                                                
                                                                //Iterate through list and kill given process
                                                                while ((line = reader.readLine()) != null) {
                                                                                if (line.startsWith(processName)) {
                                                                                                Runtime.getRuntime().exec("Taskkill /IM "+processName+".exe /F");
                                                                                                Thread.sleep(gblConstants.defSleepTime);
                                                                                                flag=true;
                                                                                }
                                                                }
                                                                
                                                                //Verify and report process
                                                                if(flag) {
                                                                                LogManager.logInfo(AbstractTestEngine.class.getName(), processName+ " process is killed.");
                                                                }else {
                                                                                LogManager.logInfo(AbstractTestEngine.class.getName(),  processName+ " process is NOT running");
                                                                }                                                              
                                                }
                
                                } catch (Exception e) {
                                                flag=false;
                                                e.printStackTrace();
                                                LogManager.logException(e, AbstractTestEngine.class.getName(), "Exception to kill process....");
                                }
                                
                                return flag;
                } 
    
                /*
                * Purpose: To login application under test using default credentials from SuiteConfig.json
                * author-date : 
                */   
    public abstract boolean attachScreenshot(Status result, String screenshotName);    

                /*
                * Purpose: To initialize app execution env, browser, app under test, app test environment, device under, test rails plan
                * author-date : 
                */   
    public abstract void beforeSuite(String execEnvName, String testBrowser, String appUT, 
                                                String appTestEnv, String devUT);

                /*
                * Purpose: To report status to current tcReport and debugLog
                * author-date : Vishnu- 
                */   
    public abstract void afterSuite();


                /*
                * Purpose: To initialize extent reports test case, corresponding to its test case id 
                 * author-date : 
                */   
    public abstract void beforeTest(ITestContext testContext, String testRailsTCId);
    
    //public abstract void beforeTest(ITestContext testContext, String testRailsTCId, String execEnvName, String appTestEnv, String trPlanId);

                /*
                * Purpose: To report test case results back to the report 
                 * author-date : 
                */   
    public abstract void afterTest();

                /*
                * Purpose: To login application under test using default credentials from SuiteConfig.json
                * author-date : Vishnu - 
                */   
    public abstract boolean loginUsingDefaultUser() throws Throwable;



	public void beforeTest(ITestContext testContext) {
		// TODO Auto-generated method stub
		
	}

}






