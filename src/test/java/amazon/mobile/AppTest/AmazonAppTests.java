package amazon.mobile.AppTest;




import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import org.testng.ITestContext;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.aventstack.extentreports.Status;

import amazon.mobile.android.pages.*;
import amazon.core.accelerators.TestDataProviders;
import amazon.core.logs.LogManager;
import amazon.mobile.android.engine.AmazonMobileTestEngine;


public class AmazonAppTests extends AmazonMobileTestEngine {
	
	private AmazonAppScreens appscreen;
	private String className = AmazonAppTests.class.getName();;
	
	@BeforeClass
	public void login() throws Throwable {
		try {
			tcReport = super.tcReport;
//			if (loginUsingDefaultUser()) {
//				LogManager.logInfo(this.className ,"Successfully logged into Amazon Mobile App");
//				
//			} else {
//				LogManager.logInfo(this.className,"Unable to Log Into Amazon");
//			}
		} catch (Exception e) {
			e.printStackTrace();
			LogManager.logException(e, className, "Exception to login into  Amazon application");
		}
	} 
	
	
	
	@DataProvider(name="AmazonAppTestData")
	public static Object[][] AmazonTestDataSet() throws FileNotFoundException, IOException{
		String inpFileName = "AmazonAPPTestData.xlsx";
		String inpSheetName = "AppTestData";
		Object[][] data = TestDataProviders.loadExcelTable(inpFileName, inpSheetName);		
		return data;		
	}

	@Test(dataProvider="AmazonAppTestData", dataProviderClass = AmazonAppTests.class)
	public void TC01AddToCartAndVerify(HashMap<String, String> inpDetails,ITestContext testContext) throws Throwable {
		boolean res = false;	
		// Read Test Data 
		
		String productName = inpDetails.get("ProductName").trim();

		appscreen = new AmazonAppScreens(this.appActionEngine);		
		try {	
			//appscreen.clickSkipSignIn();

		if (appscreen.waitForHomePage()) {
			reportStatus(true,"Successfully Loaded Home Page-Amazon","Home page");
			attachScreenshot(Status.INFO, "info");
			res= appscreen.searchProduct(productName);
			reportStatus(res,"Successfully Searched product- "+productName,"product search failed- "+productName);
			// Select Product from Search results
			
			
			res= appscreen.selectSearchedResults(productName);
			reportStatus(res,"Successfully product selected- "+productName,"Product Fail to select- "+productName);
			String ProductName1 = appscreen.GetProductName();
			reportStatus(res,"Successfully Got Product Name From Details Page "+ProductName1,"Fail togot product name from details page "+ProductName1);
			String price1 = appscreen.GetPrice();
			reportStatus(res,"Successfully Got Product -Price From Details Page "+price1,"Fail togot product price from details page "+price1);
			attachScreenshot(Status.INFO, "info");
			appscreen.fullViewProductDetailScreen();
			res = appscreen.clickOnCart();
			reportStatus(res,"Successfully Naviagte to Cart","Fail to navigate to cart");
			res= appscreen.CompareProductDetails(ProductName1, price1);
			reportStatus(res,"Successfully Verified Product detais-Search page vs Check out screen","Fail to verify Product details search screen vs Check out screen");
			attachScreenshot(Status.INFO, "info");
				 // attachScreenshot(Status.INFO, "info");
			}else {
				reportStatus(false,"Create Asset Page ","fail to navigate to Create asset screen");
			}
			
		}catch(Exception e) {
			reportStatus(false,"TC01AddToCartAndVerify","TC01AddToCartAndVerify fail to initiate");
			e.printStackTrace();
		}
	}
	

}
