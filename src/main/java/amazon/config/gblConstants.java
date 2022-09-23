package amazon.config;


import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

public class gblConstants {
	
	public enum ComparisionType
	{
	    NOT_EQUAL, EQUAL, EQUAL_IGNORECASE, CONTAINS, SUBSTRING_OF;
	}
	
	public final static String logsDirName = "Logs";	
	
	public final static String testDataDirName = "TestData";
	public final static String toolDirName = "tools";
	public final static String reportsDirName = "Reports";
	public final static String reportsArchiveDirName = "Reports_Archive";
	public final static String downloadsDirName = "Downloads";
	public final static String librayDirName = "commonLibs";
	public final static String screenshotDirName = "screenshots";
	public final static String configDirName = "configurations";
	public final static String logosDirName = "logos";
	public final static String cognizantLogoName = "cognizant-logo.png";
	public final static String clientLogoName = "Client_logo.png";
	public final static String suiteConfigName = "SuiteConfig.json"; //"SuiteHarness.json";
	
	//*****Project Name
	public final static String projMobile = "Amazon-Android";
	public final static String rootDir = getRootDir();
	public final static String projWorkingDir = projWorkingDir();
	public final static String currSuiteName = getSuiteName();
	public final static String logFolderPath = getLogFolderPath();;
	public final static String reportsPath = getRepFolderPath();
	public final static String reportsArchivePath = getRepArchivePath();
	public final static String downloadsPath = getRepFolderPath() + File.separator + downloadsDirName;
	public final static SuiteConfigReader suiteConfig = new SuiteConfigReader();
	
	//Wait Timeouts
	public static final String startTimeStamp = new SimpleDateFormat("dd_MMM_yyyy_hh_mm_ss_SSS").format(new Date());
	public static final int defSleepTime = 2000; //Microseconds
	public static final int wdWaitTimeout = 20; //seconds
	public static final int pageLoadTimeout = 60; //seconds
	public static final int implicitWaitTimeout = 30;	//Seconds
	

	public static final String replaceText = "<<ReplaceText>>";	
	
	
	private static String getRootDir(){
		String strProjDir = System.getProperty("user.dir");
		return new File(strProjDir).getParent();
	}
	
	
	
	private static String projWorkingDir() {
		return System.getProperty("user.dir");
	}
	
	private static String getSuiteName() {
		return new File(System.getProperty("user.dir")).getName();
	}
	
	private static String getLogFolderPath() {
		return System.getProperty("user.dir") + File.separator + logsDirName;
	}
	
	private static String getRepFolderPath() {
		return System.getProperty("user.dir") + File.separator + reportsDirName;
		
		
	}
	
	private static String getRepArchivePath() {
		return System.getProperty("user.dir") + File.separator + reportsArchiveDirName;
		
		
	}

}
