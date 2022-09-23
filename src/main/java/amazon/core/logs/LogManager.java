package amazon.core.logs;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.FileAppender;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;

import amazon.config.gblConstants;

public class LogManager {
	
	private static Logger logger=null;	
	private static FileAppender appender=null;	
	private static String strDefaultLocation = System.getProperty("user.dir") + File.separator + gblConstants.logsDirName;
	private static String strDefaultLogName = new File(System.getProperty("user.dir")).getName() + gblConstants.startTimeStamp + ".log";	
	
	public LogManager(String name, String loc) {
		
	}
	
	/**
	 * @author vishnu
	 * @return Logger
	 */
	public static void initialize(){
		
		try{			
			if (logger == null) {			
				//Create logFile folder, if it doesn't exist
				File folLogs = new File(strDefaultLocation);
				if(!folLogs.exists()){
					folLogs.mkdirs();
				}
				
				//Change pattern
				logger = Logger.getLogger(LogManager.class);	
				PatternLayout layout = new PatternLayout("%d{MM/dd/yyyy HH:mm:ss}\t%-5p  %c %x - %m%n");
				appender = new FileAppender(layout,strDefaultLocation.trim() + File.separator + strDefaultLogName ,false);
				logger.addAppender(appender);
			}						
		}catch(Exception e){
			e.printStackTrace();
		}
	}			
	
	private static Logger updateLogger(String LogName){
		try{			
			logger = Logger.getLogger(LogName);									
			logger.addAppender(appender);	
		}catch(Exception e){
			e.printStackTrace();
		}
		return logger;
	}	

	public static synchronized void logDebug(String strLogName, String strLogMessage){
		logger = updateLogger(strLogName);
		logger.debug(strLogMessage);
		System.out.println("LOG DEBUG::" + strLogMessage);
	}
	
	public static synchronized void logInfo(String strLogName, String strLogMessage){
		logger = updateLogger(strLogName);
		logger.info(strLogMessage);
		System.out.println("LOG INFO::" + strLogMessage);
	}
	
	public static synchronized void logWarining(String strLogName, String strLogMessage){
		logger = updateLogger(strLogName);
		logger.warn(strLogMessage);
		System.out.println("LOG WARNING:: " + strLogMessage);
	}

	public static synchronized void logError(String strLogName, String strLogMessage){
		logger = updateLogger(strLogName);
		logger.error(strLogMessage);
		System.out.println("LOG ERROR:: " + strLogMessage);
	}

	public static synchronized void logFatal(String strLogName, String strLogMessage){
		logger = updateLogger(strLogName);
		logger.fatal(strLogMessage);
		System.out.println("LOG FATAL::" + strLogMessage);
	}
	
	public static synchronized void logException(Exception e, String strLogName, String strLogMessage){
		logger = updateLogger(strLogName);
		logger.error(strLogMessage, e);
		System.out.println("LOG EXCEPTION::" + strLogMessage);
	}	
}
