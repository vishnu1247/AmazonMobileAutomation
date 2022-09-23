package amazon.mobile.android.pages;

import io.appium.java_client.android.Activity;
import io.appium.java_client.android.AndroidKeyCode;
import io.appium.java_client.android.AndroidKeyMetastate;
import io.appium.java_client.android.nativekey.KeyEvent;
import io.appium.java_client.pagefactory.AndroidFindBy;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;

import java.awt.Robot;
import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import amazon.config.gblConstants;
import amazon.core.logs.LogManager;
import amazon.mobile.android.engine.AmazonMobileActionEngine;
import amazon.mobile.android.engine.AmazonMobileTestEngine;

public class AmazonAppScreens extends AmazonMobileTestEngine{

	
	private AmazonMobileActionEngine appEngine = null;
	

//***************************** Start Locators ********************************************************************************	

	@FindBy(id = "com.amazon.mShop.android.shopping:id/sign_in_button")
	private WebElement signin;
	
	@FindBy(id = "ap_email_login")
	private WebElement userName_box;
	


	@FindBy(id = "ap_password")
	private WebElement passWord_box;
	@FindBy(id = "signInSubmit")
	private WebElement btnLogin;
	@FindBy(id = "in.amazon.mShop.android.shopping:id/chrome_search_hint_view")
	private WebElement searchBox;
	
	@FindBy(id = "in.amazon.mShop.android.shopping:id/rs_search_src_text")
	private WebElement EntersearchBox;
	
	
	@FindBy(id = "com.amazon.mShop.android.shopping:id/item_title")
	private WebElement searchedResults;
	
	
	@FindBy(id = "title_feature_div")
	private WebElement des_title;

	@FindBy(id = "atfRedesign_priceblock_priceToPay")
	private WebElement des_price;
	//Cart
	@FindBy(id = "com.amazon.mShop.android.shopping:id/action_bar_cart_count")
	private WebElement cart;
	
	
	public final String ProdDetais_CheckoutScreen = "xpath=//*[contains(@resource-id,'sc-item')]//android.view.View[contains(@text,'"+ gblConstants.replaceText+"')]";
	
	
	@FindBy(xpath = "//*[@text='Skip sign in']")
	private WebElement skip;
	
	public final String searchedRes = "com.amazon.mShop.android.shopping:id/item_title";
//****************************** End Locators *********************************************************************************

	public AmazonAppScreens(AmazonMobileActionEngine actEng) {
		appEngine = actEng;

		// Page Factory for accessing elements
		PageFactory.initElements(appEngine.getDriver(), this);
		
		

	}

	public boolean waitForLoginSelectionScreen() {
		boolean res = false;
		try {
			res = this.appEngine.waitForElementVisibles(signin, gblConstants.pageLoadTimeout * 2);
		} catch (Exception e) {
			LogManager.logError(AmazonAppScreens.class.getName(), "Fail to Load Home Screen ");
		}
		return res;
	}
	
	public boolean waitForLoginscreen() {
		boolean res = false;
		try {
			res = this.appEngine.waitForElementVisibles(userName_box, gblConstants.pageLoadTimeout * 2);
		} catch (Exception e) {
			LogManager.logError(AmazonAppScreens.class.getName(), "Fail to navigate to sign in page ");
		}
		return res;
	}
	
	public boolean waitForHomePage() {
		boolean res = false;
		try {
			res = this.appEngine.waitForElementVisibles(searchBox, gblConstants.pageLoadTimeout * 2);
		} catch (Exception e) {
			LogManager.logError(AmazonAppScreens.class.getName(), "Fail to navigate to sign in page ");
		}
		return res;
	}

	/**
	 * @Purpose To Login To Amazon APP
	 * @Constraints
	 * @Input String String userid, String password
	 * @Output boolean : If Login successfully return TRUE, else FALSE
	 * @Author-vishnu 
	 */
	public boolean LoginAmazonApp(String userid, String password) throws Throwable {
		boolean res = false;
		try {
			if (waitForHomePage()) {
				//reportStatus(true,"Successfully Loaded Home Page-Amazon - Login detals Saved in device Already","Home page");
				LogManager.logInfo(AmazonAppScreens.class.getName(), "uccessfully Loaded Home Page-Amazon - Login detals Saved in device Already " + userid);
				res = true ;
			}
			else if (waitForLoginSelectionScreen()){
				this.appEngine.click(signin);
				res = this.appEngine.enterTxt(userName_box, userid);
				//reportStatus(res,"Successfully Entered UserName- "+userid,"Fail to Enter UserName- "+userid);
				//this.appEngine.click(btnContinue);
				res = this.appEngine.enterTxt(passWord_box, password);
				//reportStatus(res,"Successfully Entered PassWord- "+password,"Fail to Enter PassWord- "+password);
				res = this.appEngine.click(btnLogin);
				//reportStatus(res,"Successfully Loaded Home Page-Amazon","Home page");
			}
			
		} catch (Exception e) {
			LogManager.logError(AmazonAppScreens.class.getName(), "Failed to Log in to Amazon with User- " + userid);
		}
		return res;
	}

	
	public boolean searchProduct(String productName) {
		boolean res = false ;
		try {
			
			this.appEngine.click(searchBox,"SearchBox");
			res =this.appEngine.enterTxt(EntersearchBox, productName);
			
			Robot robot = new Robot();
			
		} catch (Exception e) {
			LogManager.logError(AmazonAppScreens.class.getName(), "Fail to searchProduct ");
		}
		return res;
	}
	
	
	
	public boolean clickSkipSignIn() {
		boolean res = false ;
		try {
			this.appEngine.click(skip,"SkipSignIn");
			
			
		} catch (Exception e) {
			LogManager.logError(AmazonAppScreens.class.getName(), "Fail to clickSkipSignIn ");
		}
		return res;
	}
	
	
	public String GetProductName() {
		// For ArrayList 
        String ProductName = null;
		try {
		
			ProductName= des_title.findElement(By.xpath("//android.view.View")).getText();

		} catch (Exception e) {
			LogManager.logError(AmazonAppScreens.class.getName(), "Fail to GetProductName ");
		}
		return ProductName;
	}
	
	public String GetPrice() {
		// For ArrayList 
        String Price = null;
		try {
			Price = des_price.findElement(By.xpath("//android.widget.EditText")).getText();
		} catch (Exception e) {
			LogManager.logError(AmazonAppScreens.class.getName(), "Fail to GetPrice ");
		}
		return Price;
	}
	
	public boolean CompareProductDetails(String Name,String Price) throws Throwable {
		boolean res = false;
		
		try {
			String ele= ProdDetais_CheckoutScreen .replace(gblConstants.replaceText, Name.split(" ")[0]);
			res = this.appEngine.waitForElementVisibles(this.appEngine.getElement(ele), gblConstants.pageLoadTimeout * 2);
			res = this.appEngine.isElementPresent(this.appEngine.getElement(ele), "Product Name", true);
			
			String ele1= ProdDetais_CheckoutScreen .replace(gblConstants.replaceText, Price.split(" ")[1]);
			res = this.appEngine.isElementPresent(this.appEngine.getElement(ele1), "Price", true);
		} catch (Exception e) {
			LogManager.logError(AmazonAppScreens.class.getName(), "Fail to GetProductName ");
		}
		return res;
	}
	
	public boolean fullViewProductDetailScreen() {
		boolean res= false;
		try {
		  	
			 res = this.appEngine.ScroolElementVisible("productTitleGroupAnchor","Add to Cart");
	  
		}catch (Exception e) {
			 LogManager.logInfo(AmazonAppScreens.class.getName(), "Failed fullViewProductDetailScreen method");	
		}
		return res;
	}
	
	public boolean clickOnCart() {
        boolean res  = true;
		try {
			res= this.appEngine.click(cart);
			
		} catch (Exception e) {
			LogManager.logError(AmazonAppScreens.class.getName(), "Fail to click On Cart ");
		}
		return res;
	}
	
	
	
	
	public boolean selectSearchedResults(String text) throws Throwable {
		boolean res = false;
		try {
			// get List of elements
			 @SuppressWarnings("unchecked")
			List<WebElement> ele= this.appEngine.driver.findElementsById(searchedRes);
			// Loop it from first to last
			for (int i = 0 ; i <ele.size();i++) {
				//get the Each element text
				String resulName= ele.get(i).getText();
				if(resulName.contains(text)) {
					ele.get(i).click();
					res=true;
					break;
				}
			}
			
		} catch (Exception e) {
			LogManager.logError(AmazonAppScreens.class.getName(), "Failed to select Searched result " + text);
		}
		return res;
	}

	
	
	
	public String readOTP() throws Throwable {
		boolean res = false;
		String otp = "" ;
		try {
			Activity activity = new Activity("com.android.mms", "com.android.mms.ui.ConversationList");
			activity.setAppWaitPackage("com.android.mms");
			activity.setAppWaitActivity("com.android.mms.ui.ConversationList");
			// this.appEngine.driver.startActivity(activity);
			 otp = this.appEngine.driver.findElementByXPath("//*[contains(@text,'is')]").getText().split("code:" )[0];
		} catch (Exception e) {
			LogManager.logError(AmazonAppScreens.class.getName(), "Failed to get otp" );
		}
		return otp;
	}
	


}
