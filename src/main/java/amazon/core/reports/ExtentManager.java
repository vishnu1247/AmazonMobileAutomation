package amazon.core.reports;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
//import java.text.SimpleDateFormat;
//import java.util.Date;
//import java.util.HashMap;
//import java.util.Map;
import java.util.ArrayList;

import org.apache.log4j.Logger;

import java.io.File;
import java.io.IOException;

import com.aventstack.extentreports.AnalysisStrategy;
import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.MediaEntityModelProvider;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;
import com.aventstack.extentreports.reporter.configuration.ChartLocation;
import com.aventstack.extentreports.reporter.configuration.Theme;

import amazon.config.gblConstants;
import amazon.core.logs.LogManager;

public class ExtentManager {

	private static ExtentReports reporter;
	private static ExtentHtmlReporter htmlReporter = null;
	private String strReportPath= "";
	private String strScreenshotPath = "";
	private String strLogName = ExtentManager.class.getName();	
	
	public String strAnalysisStrategy = "";
	public String strTimeStampFormat = "";
	public String strReportName = "";
	public String strReportTitle = "";
	public String strLogoText = "";
	public ArrayList<String> arrEnvDetails = new ArrayList<String>();
	private boolean blnUpdatedClientLogo = false;
	
	/*
	 * Initialize Extent Reports & Attach html reporter
	 */
	public ExtentManager(String strRepName, String strReportLoc){	
		
		if(ExtentManager.reporter == null ) {		
			//Archive previous reports, if required
			File folReport = new File(strReportLoc);
			if (folReport.exists()){
				String strReportArchive = folReport.getParent() + File.separator + folReport.getName() + "_Archive";
				archivePreviousReports(strReportLoc, strReportArchive);
			}else{
				folReport.mkdirs();
			}
			 
			//Create screenshots folder for reports
			this.strScreenshotPath = strReportLoc + File.separator + "screenshots";
			//create containing folder & screenshots directory, if doesn't exist
			setScreenshotPath(this.strScreenshotPath);
			
			//Configure extent reports html path
			this.strReportPath = strReportLoc + File.separator + strRepName + ".html";		
			logMessage("Configured extent reports path to " + this.strReportPath);		
			
			//Create & configure Html Reporter
			htmlReporter = new ExtentHtmlReporter(strReportPath);
			htmlReporter.config().setTestViewChartLocation(ChartLocation.TOP);
			htmlReporter.config().setChartVisibilityOnOpen(false);
			htmlReporter.config().setTimeStampFormat("MM/dd/yyyy hh:mm:ss a");
			htmlReporter.config().setEncoding("utf-8");
			setExtentLogoTextAs("cognizant");
					
			reporter = new ExtentReports();
			reporter.attachReporter(htmlReporter);
			addClientLogo();
			//addCignitiLogo();
		}
	}
	
	/*
	 * Post logger information
	 */
	private void logMessage(String strLogMessage){
		LogManager.logInfo(strLogName, strLogMessage);
	}		
	
	/*
	 * Set screenshots path
	 */	
	private boolean setScreenshotPath(String strSSPath){
		boolean blnRes = false;
		try{
			File folSSPath = new File(strSSPath);
			
			if(!folSSPath.exists()){
				folSSPath.mkdirs();
			}
			this.strScreenshotPath = folSSPath.getAbsolutePath();
			logMessage("Configured extent reports screenshots path to " + this.strScreenshotPath);
			
			blnRes = folSSPath.exists();
		}catch (Exception e) {
			e.printStackTrace();
			blnRes = false;
		}
		return blnRes;
	}
	
	/*
	 * get Screenshot location
	 */	
	public String getScreenshotPath(){
		return this.strScreenshotPath;
	}
	
	/*
	 * Update System Info like HostName, O.S, Browser, AppUrl, AppEnv, AppVersion, etc..
	 */
	public void setSystemInfo(String strKey, String strValue){
		reporter.setSystemInfo(strKey.trim(), strValue.trim());
		arrEnvDetails.add(strKey + " =  " + strValue);
		logMessage("Updated environment " + strKey + " as "+ strValue + " in extent reports");
	}

	/*
	 * Get System Info
	 */	
//	public String getSystemInfo(){		
//		return arrEnvDetails.toString();
//	}

	/*
	 * Set Analysis strategy for logging results Class or Suite or Test.. 
	 */	
	public void setAnalysisStrategy(AnalysisStrategy objStrategy){
		reporter.setAnalysisStrategy(objStrategy);
		this.strAnalysisStrategy = objStrategy.name();
		logMessage("Updated analysis strategy as " + objStrategy.name());
	}

	
	/*
	 * Configure Document Title for html  
	 */		
	public void configReportTitle(String strTitle) {
		htmlReporter.config().setDocumentTitle(strTitle.trim());
		this.strReportTitle = strTitle;
		logMessage("Configured extent reports html document title as as " + strTitle);
	}
	
	
	/*
	 * Configure Report name for html 
	 */		
	public void configReportName(String strRepName){
		htmlReporter.config().setReportName(strRepName.trim());
		this.strReportName = strRepName;
		logMessage("Configured extent report name as " + strRepName);
		
		if (this.blnUpdatedClientLogo){
			addClientLogo();
		}
	}
	
	
	/*
	 * Configure Report Theme 
	 */		
	public void configTheme(Theme objInpTheme){
		htmlReporter.config().setTheme(objInpTheme);
		logMessage("Conifgured analysis strategy as " + objInpTheme.name());
	}
	
	/*
	 * Configure date and time format in report 
	 */		
	public void cofigTimeStampFormat(String strSimpleTimeFormat){
		htmlReporter.config().setTimeStampFormat(strSimpleTimeFormat);
		this.strTimeStampFormat = strSimpleTimeFormat;
		logMessage("Configured time stamp format as " + strSimpleTimeFormat);
	}
	
	public void setExtentLogoTextAs(String strInpText){
		htmlReporter.config().setJS("$(document).ready(function() { $('.brand-logo').text('" + strInpText + "'); });");
		htmlReporter.config().setCSS("a.brand-logo {font-size: 16px}; }");
		this.strLogoText = strInpText;
	}

	/*
	 * Create new test  
	 */			
	public synchronized ExtentTest addTest(String strName, String StrDesc){
		ExtentTest objTest = null;		
		objTest = reporter.createTest(strName, StrDesc);		
		logMessage("Created new test - " + strName + ", in extent report");
		return objTest;
	}
	

	
	/*
	 * Create a new child test for given test 
	 */			
	public synchronized ExtentTest addChildTest(ExtentTest objParentTest, String strName, String strDesc){
		ExtentTest objTest = null;
		objTest = objParentTest.createNode(strName, strDesc);
		logMessage("Created new child test - " + strName + ", in extent report");
		return objTest;
	}
	
	public synchronized void removeTest(ExtentTest objTest){
		reporter.removeTest(objTest);
		logMessage("Removed test " + objTest.getClass().getName() + " in extent report");
	}
	
	/*
	 * Log result for a given test 
	 */			
	public synchronized void logResult(ExtentTest objTest, Status objStatus, String strMsg){
		objTest.log(objStatus, strMsg);
	}

	public synchronized boolean attachScreenshot(ExtentTest objTest, Status objStatus, String strSSFileName){
		boolean blnRes = false;
		try{
			Thread.sleep(3000);
			String relativeSSPath = "." + File.separator + gblConstants.screenshotDirName + File.separator + strSSFileName;
			
			
			System.out.println("ScreenShot Path- "+this.strScreenshotPath + File.separator + strSSFileName);
			//this.strScreenshotPath = "C:\\A2B-Mobile\\a2bMobileAutomation -06-june-2019\\a2bMobileAutomation\\Reports\\screenshots";
			
			MediaEntityModelProvider image = null;
			image = MediaEntityBuilder.createScreenCaptureFromPath(relativeSSPath).build();
			objTest.log(objStatus, "Aplication Snapshot: " + strSSFileName , image);
			blnRes = true;
		}catch(IOException io){
			io.printStackTrace();
			blnRes = false;
		}catch(Exception e){
			e.printStackTrace();
			blnRes = false;
		}
		
		return blnRes;
	}
	
	/*
	 * flush results to file 
	 */		
	public synchronized void flushReport(){
		reporter.flush();		
	}
	
	/*
	 * Add given client logo image in report name 
	 */
	private void addClientLogo(){
		this.blnUpdatedClientLogo = true;
		
		// Modified : vishnu
		String strClientLogoRelPath = ".." + File.separator + gblConstants.configDirName + File.separator 
				+ gblConstants.logosDirName + File.separator + gblConstants.clientLogoName;
		htmlReporter.config().setReportName("<img src='" + strClientLogoRelPath + "' />" + this.strReportName);
		
		htmlReporter.config().setCSS(".report-name { padding-left: 10px; } .report-name > img { float: right;height: 90%;margin-left: 30px;margin-top: 2px;width: auto; }");	
	}
	
	
	
	/*
	 * Add given client logo image in report name 
	 * @Author :: vishnu
	 */
	private void addCignitiLogo(){
		this.blnUpdatedClientLogo = true;
		
		// Modified : vishnu
		String strClientLogoRelPath = ".." + File.separator + gblConstants.configDirName + File.separator 
				+ gblConstants.logosDirName + File.separator + gblConstants.cognizantLogoName;
		htmlReporter.config().setReportName("<img1 src='" + strClientLogoRelPath + "' />" + "Naresh");
		htmlReporter.config().setCSS(".report-name { padding-left: 30px; } .report-name > img1 { float: left;height: 90%;margin-right: 40px;margin-top: 2px;width: auto; }");	
	}
	
	
	
	
    public void archivePreviousReports(String repPath, String repArchivePath){
        File folReport = new File(repPath);
        
        //Check if reports already exists
        if (folReport.exists()){
            
        	File folReportArchive = new File(repArchivePath);
            
            //Create Reports Archive folder, if doesn't exists
            if (!folReportArchive.exists()){
            	folReportArchive.mkdirs();
            }
            
            //Move contents from reports to reports archive
            if (folReport.listFiles().length > 0){
                for (File repFile:folReport.listFiles()){
                	if(repFile.isDirectory()){
                		File folDestDir = new File(folReportArchive.getAbsolutePath() + File.separator + repFile.getName());
                		if (!folDestDir.exists()){
                			folDestDir.mkdirs();
                		}
                		for(File ssFile:repFile.listFiles()){
                			ssFile.renameTo(new File( folReportArchive.getAbsolutePath() + File.separator + repFile.getName() + File.separator + ssFile.getName() ));
                		}
                	}else{
                    	repFile.renameTo(new File(folReportArchive.getAbsolutePath() + File.separator + repFile.getName()));                		
                	}
                }                	
            }        	
        }

    }
	
}
