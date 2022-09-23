package amazon.config;

import java.io.File;
import java.util.List;
import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.InvalidJsonException;
import com.jayway.jsonpath.JsonPath;
import amazon.core.accelerators.JsonReader;


public class SuiteConfigReader {

	Object jssuiteConfig = null;
	String currTestApp = "";
	String currTestEnv = "";
	String currTestDevice = "";
	
	/*
	 * Purpose:To load and read SuiteConfig.json
	 * author-date: Vishnu 
	 * reviewer-date:
	 */
	public SuiteConfigReader() {
		// TODO Auto-generated constructor stub		
		String suiteConfig = JsonReader.readJSONFile(new File(System.getProperty("user.dir")+ File.separator + gblConstants.suiteConfigName));
		jssuiteConfig = Configuration.defaultConfiguration().jsonProvider().parse(suiteConfig);
	}

	public void setAppUnderTest(String appName) {
		currTestApp = appName.trim();
	}

	public void setEnvUnderTest(String envName) {
		currTestEnv = envName.trim();
	}

	public void setMobileUnderTest(String devName) {
		currTestDevice = devName.trim();
	}

	/*
	 * Purpose:To get  app login user value from suite config.json
	 * author-date: Vishnu 
	 * reviewer-date:
	 */	
	public String getTestAppLoginUser(String appName, String envName) {

		String respValue = null;
		try {
			if (appName.length() > 0 && envName.length() > 0) {
				List<List<String>> env = JsonPath.read(jssuiteConfig, "$.apps[?(@.name == '"+appName.trim()+"')].environments");
				List<String> resp = JsonPath.read(env.get(0).toString(), "$[?(@.name == '"+ envName.trim() +"')].username");
				respValue = resp.get(0).trim();					
			}else {
				List<List<String>> env = JsonPath.read(jssuiteConfig, "$.apps[?(@.name == '"+currTestApp.trim()+"')].environments");
				List<String> resp = JsonPath.read(env.get(0).toString(), "$[?(@.name == '"+ currTestEnv.trim() +"')].username");
				respValue = resp.get(0).trim();					
			}			
		}catch(InvalidJsonException jsExp) {
			jsExp.printStackTrace();
		}
		return respValue;
	}

	/*
	 * Purpose:To get  app login password value from suite config.json
	 * author-date: Vishnu 
	 * reviewer-date:
	 */		
	public String getTestAppLoginPwd(String appName, String envName) {

		String respValue = null;
		try {
			if (appName.length() > 0 && envName.length() > 0) {
				List<List<String>> env = JsonPath.read(jssuiteConfig, "$.apps[?(@.name == '"+appName.trim()+"')].environments");
				List<String> resp = JsonPath.read(env.get(0).toString(), "$[?(@.name == '"+ envName.trim() +"')].password");
				respValue = resp.get(0).trim();			
			}else {
				List<List<String>> env = JsonPath.read(jssuiteConfig, "$.apps[?(@.name == '"+currTestApp.trim()+"')].environments");
				List<String> resp = JsonPath.read(env.get(0).toString(), "$[?(@.name == '"+ currTestEnv.trim() +"')].password");
				respValue = resp.get(0).trim();			
			}			
		}catch(InvalidJsonException jsExp) {
			jsExp.printStackTrace();
		}
		return respValue;				
	}



	/*
	 * Purpose:To get mobile device name of given device from suite config.json
	 * author-date: Vishnu 
	 * reviewer-date:
	 */	
	public String getMobileDeviceName(String appName, String devName) {		

		String respValue = null;
		try {
			if (appName.length() > 0 && devName.length() > 0) {			
				List<List<String>> dev = JsonPath.read(jssuiteConfig, "$.apps[?(@.name == '"+appName.trim()+"')].devices");
				List<String> resp = JsonPath.read(dev.get(0).toString(), "$[?(@.name == '"+ devName.trim() +"')].['device.name']");
				respValue = resp.get(0).trim();					
			}else {
				List<List<String>> dev = JsonPath.read(jssuiteConfig, "$.apps[?(@.name == '"+currTestApp.trim()+"')].devices");
				respValue = JsonPath.read(dev.get(0).toString(), "$[?(@.name == '"+ currTestDevice.trim() +"')].['device.name']");					
			}			
		}catch(InvalidJsonException jsExp) {
			jsExp.printStackTrace();
		}
		return respValue;						
	}	


	/*
	 * Purpose:To get mobile device os type of given device from suite config.json
	 * author-date: Vishnu 
	 * reviewer-date:
	 */	
	public String getMobileDevicePlatform(String appName, String devName) {		

		String respValue = null;
		try {

			if (appName.length() > 0 && devName.length() > 0) {
				List<List<String>> dev = JsonPath.read(jssuiteConfig, "$.apps[?(@.name == '"+appName.trim()+"')].devices");
				List<String> resp = JsonPath.read(dev.get(0).toString(), "$[?(@.name == '"+ devName.trim() +"')].['platform.name']");
				respValue = resp.get(0).trim();	
			}else {
				List<List<String>> dev = JsonPath.read(jssuiteConfig, "$.apps[?(@.name == '"+currTestApp.trim()+"')].devices");
				respValue = JsonPath.read(dev.get(0).toString(), "$[?(@.name == '"+ currTestDevice.trim() +"')].['platform.name']");					
			}			
		}catch(InvalidJsonException jsExp) {
			jsExp.printStackTrace();
		}
		return respValue;						
	}	

	

	/*
	 * Purpose:To get mobile device OS version of given device from suite config.json
	 * author-date: Vishnu 
	 * reviewer-date:
	 */		
	public String getMobileDevicePlatformVer(String appName, String devName) {		

		String respValue = null;
		try {
			if (appName.length() > 0 && devName.length() > 0) {
				List<List<String>> dev = JsonPath.read(jssuiteConfig, "$.apps[?(@.name == '"+appName.trim()+"')].devices");
				List<String> resp = JsonPath.read(dev.get(0).toString(), "$[?(@.name == '"+ devName.trim() +"')].['platform.version']");
				respValue = resp.get(0).trim();
			}else {
				List<List<String>> dev = JsonPath.read(jssuiteConfig, "$.apps[?(@.name == '"+currTestApp.trim()+"')].devices");
				respValue = JsonPath.read(dev.get(0).toString(), "$[?(@.name == '"+ currTestDevice.trim() +"')].['platform.version']");					
			}			
		}catch(InvalidJsonException jsExp) {
			jsExp.printStackTrace();
		}
		return respValue;						
	}
	

	/*
	 * Purpose:To get mobile device user id of given device from suite config.json
	 * author-date: Vishnu 
	 * reviewer-date:
	 */		

	public String getMobileDeviceUserId(String appName, String devName) {		

		String respValue = null;
		try {

			if (appName.length() > 0 && devName.length() > 0) {
				List<List<String>> dev = JsonPath.read(jssuiteConfig, "$.apps[?(@.name == '"+appName.trim()+"')].devices");
				List<String> resp = JsonPath.read(dev.get(0).toString(), "$[?(@.name == '"+ devName.trim() +"')].['userid.number']");
				respValue = resp.get(0).trim();
			}else {
				List<List<String>> dev = JsonPath.read(jssuiteConfig, "$.apps[?(@.name == '"+currTestApp.trim()+"')].devices");
				respValue = JsonPath.read(dev.get(0).toString(), "$[?(@.name == '"+ currTestDevice.trim() +"')].['userid.number']");					
			}			
		}catch(InvalidJsonException jsExp) {
			jsExp.printStackTrace();
		}
		return respValue;						
	}



	public String getAppiumVer(String appName) {				
		String respValue = (appName.length() > 0 ) ?
				evaluateJSONPathToString("$.apps[?(@.name == '"+appName.trim()+"')].['appium.version']"):
				evaluateJSONPathToString("$.apps[?(@.name == '"+currTestApp.trim()+"')].['appium.version']");
		return respValue;						
	}



	public String getAppiumPortNo(String appName) {				

		String respValue = (appName.length() > 0 ) ?

				evaluateJSONPathToString("$.apps[?(@.name == '"+appName.trim()+"')].['localhost.port']"):

					evaluateJSONPathToString("$.apps[?(@.name == '"+currTestApp.trim()+"')].['localhost.port']");

		return respValue;

	}

	

	public String getAndroidAppPackageName(String appName) {		

		String respValue = (appName.length() > 0 ) ?

				evaluateJSONPathToString("$.apps[?(@.name == '"+appName.trim()+"')].['app.package']"):

					evaluateJSONPathToString("$.apps[?(@.name == '"+currTestApp.trim()+"')].['app.package']");

		return respValue;						

	}

	

	public String getAndroidAppPackageActivity(String appName) {		

		String respValue = (appName.length() > 0 ) ?

				evaluateJSONPathToString("$.apps[?(@.name == '"+appName.trim()+"')].['app.activity']"):

					evaluateJSONPathToString("$.apps[?(@.name == '"+currTestApp.trim()+"')].['app.activity']");

		return respValue;						

	}



	public String getAndroidAutomationName(String appName) {		

		String respValue = (appName.length() > 0 ) ?

				evaluateJSONPathToString("$.apps[?(@.name == '"+appName.trim()+"')].['automation.name']"):

					evaluateJSONPathToString("$.apps[?(@.name == '"+currTestApp.trim()+"')].['automation.name']");

		return respValue;						

	}

	
	private String evaluateJSONPathToString(String jsPath) {
		String respValue = null;
		try {
			List<String> respColl = JsonPath.read(jssuiteConfig, jsPath); 
			return respColl.get(0);			
		}catch(InvalidJsonException jsExp) {
			jsExp.printStackTrace();
		}
		return respValue;		
	}

	/*
	 * Purpose: To get database type of given database name from suite config.json
	 * author-date: Vishnu 
	 * reviewer-date: 
	 */
	public String getdatabaseType(String dbName) {

		if(dbName.length() > 0) {
			String dbType = evaluateJSONPathToString("$.databases[?(@.dbname == '"+dbName+"')].dbtype");
			return dbType;
		}else {
			return null;
		}
	}

		
}

