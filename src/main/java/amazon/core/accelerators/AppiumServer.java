package amazon.core.accelerators;

import io.appium.java_client.service.local.AppiumDriverLocalService;
import io.appium.java_client.service.local.AppiumServiceBuilder;
import io.appium.java_client.service.local.flags.GeneralServerFlag;
import java.io.*;
import java.net.*;

public class AppiumServer {


	private AppiumDriverLocalService service;
	private AppiumServiceBuilder builder;
	
	public void startServer(int portnumber) {

		//Build the Appium service
		builder = new AppiumServiceBuilder();
		builder.withIPAddress("0.0.0.0");
		builder.usingPort(portnumber);
		builder.withArgument(GeneralServerFlag.SESSION_OVERRIDE);
		builder.withArgument(GeneralServerFlag.LOG_LEVEL,"error");
		
		//Start the server with the builder
		service = AppiumDriverLocalService.buildService(builder);
		service.start();
	}
	
	public void stopServer() {
		service.stop();
	}
 
	public boolean checkIfServerIsRunnning(int portnumber) {
		
		boolean isServerRunning = false;
		ServerSocket serverSocket;
		try {
			serverSocket = new ServerSocket(portnumber);
			serverSocket.close();
		} catch (IOException e) {
			isServerRunning = true;
		} finally {
			serverSocket = null;
		}
		return isServerRunning;
	}	
 
}