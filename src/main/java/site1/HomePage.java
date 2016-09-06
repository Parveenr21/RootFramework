package site1;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;

import Utility.ActionUtility;


public class HomePage extends SiteBasePageObject {

  public static final String PAGE_URL = System.getProperty("URLvar")+"/login.htm";
  
	
	
  public HomePage(WebDriver driver) {
    super(driver,PAGE_URL);
  
  }

  /**
   * Added one more constructor with dummy param to bypass opening a new page  
   * 
   */
  public HomePage(WebDriver driver,boolean DoNotOpenANewPage) {
    super(driver);
  }
  
  //xpath for Channel dropdown.
  @FindBy(how = How.XPATH, using = "/")
  public WebElement selChannel;  
  
  //xpath for Asset Type dropdown.
  @FindBy(how = How.XPATH, using = "//select[@id='contentAsset.type']")
  public WebElement selAssetType;
 
  //xpath for Creative name text box.
  @FindBy(how = How.XPATH, using = "//input[@id='contentAsset.name']")
  public WebElement txtCreativeName;  
  
  //xpath for Language dropdown.
  @FindBy(how = How.XPATH, using = "//select[@id='contentAsset.locale']")
  public WebElement selLanguage;
  
  //xpath for Presentation type dropdown.
  @FindBy(how = How.XPATH, using = "//select[@id='contentAsset.presentationType']")
  public WebElement selPresentationType;
   
  //xpath for DefaultImageURL text box.
  @FindBy(how = How.XPATH, using = "//input[@id='contentAsset.defaultImageURL']")
  public WebElement txtDefaultImageURL;
 
  //xpath for AlternateImageURL text box.
  @FindBy(how = How.XPATH, using = "//input[@id='contentAsset.alternateImageURL']")
  public WebElement txtAlternateImageURL;
  
  //xpath for txtTargetLandingURL text box.
  @FindBy(how = How.XPATH, using = "//input[@id='contentAsset.targetLandingURL']")
  public WebElement txtTargetLandingURL;
  
  //xpath for txtTargetLandingURL text box.
  @FindBy(how = How.XPATH, using = "//input[@id='contentAsset.alternativeText']")
  public WebElement txtAlternativeText;
    
  //xpath for banner Open New Window Checkbox.
  @FindBy(how = How.XPATH, using = "//input[@id='bannerOpenNewWindow']")
  public WebElement chkBannerOpenNewWindow;
   
  //xpath for Click Tracking checkbox.
  @FindBy(how = How.XPATH, using = "//input[@id='clickTracking']")
  public WebElement chkClickTracking;
  
  //xpath for Create Heml button.
  @FindBy(how = How.XPATH, using = "//input[@value='Create Html']")
  public WebElement btnCreateHtml;
 
  //xpath for Save Button.
  @FindBy(how = How.XPATH, using = "//input[@id='assetSaveButton']")
  public WebElement btnSave;
   
  //Xpath for Mediaplex textbox.
  @FindBy(how = How.XPATH, using = "//input[@id='mediaplexIdInput']")
  public WebElement txtMediaplexId; 
  
  //xpath For Create Mediaplex button.
  @FindBy(how = How.XPATH, using = "//input[@value='Create Mediaplex Html']")
  public WebElement btnCreateMediaplexHtml; 
  
  //Save and add new language version button on asset Deatils page
  @FindBy(how = How.XPATH, using = "//input[@id='assetsaveversionbutton']")
  public WebElement assetSaveVersionButton;
  
  //Edit link on asset search page
  @FindBy(how = How.XPATH, using = "//*[@id='assetTableBody']//tr//td[3]//a[1]")
  public WebElement lnkEdit;
      
  //Delete link on asset search page
  @FindBy(how = How.XPATH, using = "//a[contains(text(),'Delete')]")
  public WebElement lnkDelete;
 
//Second Delete link after save on asset page
  @FindBy(how = How.XPATH, using = "//*[@id='assetTableBody']//tr[2]//td[3]//a[2]")
  public WebElement lnkSecondDelete;  
  
  

//First Radio button Yes
  @FindBy(how = How.XPATH, using = "//input[@type='radio' and @value='Yes']")
  public WebElement radiobuttonYes;  
  


//First Radio button No
  @FindBy(how = How.XPATH, using = "//input[@type='radio' and @value='No']")
  public WebElement radiobuttonNo;
  
  

  //xpath for LightBox Opt-in Label type dropdown.
  @FindBy(how = How.XPATH, using = "//*[@id='contentAsset.optInLabel']")
  public WebElement selOptIn;
  
//xpath for LightBox Soft Decline Label type dropdown.
  @FindBy(how = How.XPATH, using = "//*[@id='contentAsset.softDeclineLabel']")
  public WebElement selSoftDecline;
  
  
  
  
      /**
       * Function for Creat New Onsite Asset <BR>
       * USE :
       * 
       */
      public void CreateOnsiteSystemEmailAsset(String Channel, String AssetType, String CreativeName, String Language, String PresentationType,String ImageURLOrMediaPlexId,
          String LandingURL) throws Exception
      {
        
        ActionUtility.selectOptionFromDropDown(driver, selChannel, Channel);
        ActionUtility.selectOptionFromDropDown(driver, selAssetType, AssetType);   
        ActionUtility.sendKeys(txtCreativeName, CreativeName); 
        ActionUtility.selectOptionFromDropDown(driver, selLanguage, Language);
        ActionUtility.selectOptionFromDropDown(driver, selPresentationType, PresentationType);
        
          if (AssetType.equalsIgnoreCase("PayPal ad")) {
            
            ActionUtility.sendKeys(txtDefaultImageURL, ImageURLOrMediaPlexId);
            ActionUtility.sendKeys(txtTargetLandingURL, LandingURL);   
            
			
				try
				{		  
					if(txtAlternativeText.isDisplayed())
					ActionUtility.sendKeys(txtAlternativeText, Channel+"_AD"); 
				}
				catch(Exception e)
				{
					System.out.println(" Alternate Text field is not present");
				}
		           
            
            ActionUtility.click(btnCreateHtml);
          }
          
          if(AssetType.equalsIgnoreCase("PayPal ad with Mediaplex tracking")) {
            
            ActionUtility.sendKeys(txtMediaplexId, ImageURLOrMediaPlexId);
            ActionUtility.click(btnCreateMediaplexHtml); 
          }
        		
        ActionUtility.click(btnSave);
       
      }
      
      
      
      /**
       * Overloaded Function for Creat New Onsite Asset for Shopping.com and Where.com <BR>
       * USE :
       * 
       */
      public void CreateOnsiteSystemEmailAsset(String Channel, String AssetType, String CreativeName, String Language, String PresentationType) throws Exception
      {
        
        ActionUtility.selectOptionFromDropDown(driver, selChannel, Channel);
        ActionUtility.selectOptionFromDropDown(driver, selAssetType, AssetType);   
        ActionUtility.sendKeys(txtCreativeName, CreativeName); 
        ActionUtility.selectOptionFromDropDown(driver, selLanguage, Language);
        ActionUtility.selectOptionFromDropDown(driver, selPresentationType, PresentationType);
        
          
            
           	try
				{		  
					if(txtAlternativeText.isDisplayed())
					ActionUtility.sendKeys(txtAlternativeText, Channel+"_AD"); 
				}
				catch(Exception e)
				{
					System.out.println(" Alternate Text field is not present");
				}
		           
            
            
          
          
          
        		
        ActionUtility.click(btnSave);
       
      }
      
      
      /**
       * Function for Creat New Onsite Asset with custom Alt Text <BR>
       * USE :
       * 
       */
      public void CreateOnsiteSystemEmailAsset(String Channel, String AssetType, String CreativeName, String Language, String PresentationType,String ImageURLOrMediaPlexId,
          String LandingURL, String altText) throws Exception
      {
        
        ActionUtility.selectOptionFromDropDown(driver, selChannel, Channel);
        ActionUtility.selectOptionFromDropDown(driver, selAssetType, AssetType);   
        ActionUtility.sendKeys(txtCreativeName, CreativeName); 
        ActionUtility.selectOptionFromDropDown(driver, selLanguage, Language);
        ActionUtility.selectOptionFromDropDown(driver, selPresentationType, PresentationType);
        
          if (AssetType.equalsIgnoreCase("PayPal ad")) {
            
            ActionUtility.sendKeys(txtDefaultImageURL, ImageURLOrMediaPlexId);
            ActionUtility.sendKeys(txtTargetLandingURL, LandingURL);   
            
			
				try
				{		  
					if(txtAlternativeText.isDisplayed())
					ActionUtility.sendKeys(txtAlternativeText, altText); 
				}
				catch(Exception e)
				{
					System.out.println(" Alternate Text field is not present");
				}
		           
            
            ActionUtility.click(btnCreateHtml);
          }
          
          if(AssetType.equalsIgnoreCase("PayPal ad with Mediaplex tracking")) {
            
            ActionUtility.sendKeys(txtMediaplexId, ImageURLOrMediaPlexId);
            ActionUtility.click(btnCreateMediaplexHtml); 
          }
        		
        ActionUtility.click(btnSave);
       
      }
      
      
      
      /**
       * Creating New Asset by clicking Save New Version Button.<BR>
       * 
       */
      public void SaveOnsiteSystemEmailAsset(String Channel, String AssetType, String CreativeName, String Language, String PresentationType,String ImageURLOrMediaPlexId,
          String LandingURL) throws Exception
      {
        
        ActionUtility.selectOptionFromDropDown(driver, selChannel, Channel);
        ActionUtility.selectOptionFromDropDown(driver, selAssetType, AssetType);   
        ActionUtility.sendKeys(txtCreativeName, CreativeName); 
        ActionUtility.selectOptionFromDropDown(driver, selLanguage, Language);
        ActionUtility.selectOptionFromDropDown(driver, selPresentationType, PresentationType);
        
          if (AssetType.equalsIgnoreCase("PayPal ad")) {
            
            ActionUtility.sendKeys(txtDefaultImageURL, ImageURLOrMediaPlexId);
            ActionUtility.sendKeys(txtTargetLandingURL, LandingURL);  
            try
			{		  
				if(txtAlternativeText.isDisplayed())
				ActionUtility.sendKeys(txtAlternativeText, Channel+"_AD"); 
			}
			catch(Exception e)
			{
				System.out.println(" Alternate Text field is not present");
			}
            
            ActionUtility.click(btnCreateHtml);
          }
          
          if(AssetType.equalsIgnoreCase("PayPal ad with Mediaplex tracking")) {
            
            ActionUtility.sendKeys(txtMediaplexId, ImageURLOrMediaPlexId);
            ActionUtility.click(btnCreateMediaplexHtml); 
          }
            
          ActionUtility.click(assetSaveVersionButton);
       
      }          
     
      
      /**
       * Function for Creat New Checkout Asset <BR>
       * USE :
       * 
       */
      public void CreateCheckoutAsset(String CreativeName, String Language, String PresentationType,String DefImageURL,
        String AltImageURL,  String LandingURL) throws Exception
      {
        
        ActionUtility.selectOptionFromDropDown(driver, selChannel, "CHECKOUT");
        ActionUtility.sendKeys(txtCreativeName, CreativeName); 
        ActionUtility.selectOptionFromDropDown(driver, selLanguage, Language);
        ActionUtility.selectOptionFromDropDown(driver, selPresentationType, PresentationType);
        ActionUtility.sendKeys(txtDefaultImageURL, DefImageURL);
        ActionUtility.sendKeys(txtAlternateImageURL, AltImageURL);
        ActionUtility.sendKeys(txtTargetLandingURL, LandingURL);  
        try
		{		  
			if(txtAlternativeText.isDisplayed())
			ActionUtility.sendKeys(txtAlternativeText, "CHECKOUT_AD"); 
		}
		catch(Exception e)
		{
			System.out.println(" Alternate Text field is not present");
		}
        
        		  
			if(PresentationType.equalsIgnoreCase("LightBox"))
			
			{
				ActionUtility.selectOptionFromDropDown(driver, selOptIn, "Apply Now");	
				ActionUtility.selectOptionFromDropDown(driver, selSoftDecline, "Not Now");
			}
			
		   
        
        
        
        ActionUtility.click(btnSave);
       
      }
      
      
      /**
       * Function for Create New Checkout Asset with customized values for Opt-In and Soft Decline values passed <BR>
       * USE :
       * 
       */
      public void CreateCheckoutAsset(String CreativeName, String Language, String PresentationType,String DefImageURL,
        String AltImageURL,  String LandingURL, String Optin, String Softdecline ) throws Exception
      {
        
        ActionUtility.selectOptionFromDropDown(driver, selChannel, "CHECKOUT");
        ActionUtility.sendKeys(txtCreativeName, CreativeName); 
        ActionUtility.selectOptionFromDropDown(driver, selLanguage, Language);
        ActionUtility.selectOptionFromDropDown(driver, selPresentationType, PresentationType);
        ActionUtility.sendKeys(txtDefaultImageURL, DefImageURL);
        ActionUtility.sendKeys(txtAlternateImageURL, AltImageURL);
        ActionUtility.sendKeys(txtTargetLandingURL, LandingURL);  
        try
		{		  
			if(txtAlternativeText.isDisplayed())
			ActionUtility.sendKeys(txtAlternativeText, "CHECKOUT_AD"); 
		}
		catch(Exception e)
		{
			System.out.println(" Alternate Text field is not present");
		}
        
        		  
			
				ActionUtility.selectOptionFromDropDown(driver, selOptIn, Optin);	
				ActionUtility.selectOptionFromDropDown(driver, selSoftDecline, Softdecline);
			
			
		   
        
        
        
        ActionUtility.click(btnSave);
       
      }
      
      
      /**
       * returns instance of Webdriver 
       */
      public WebDriver getWebdriverInstance() {
        return driver;
      }
      
      
      
      /**
       * Get Local value From Left Nav.
       * @return
     `  */
      public String getLocalValueFromLeftNav() {
        return driver.findElement(By.xpath("//*[@id='assetTableBody']//tr[1]//td[2]")).getText();
      }

  
}
