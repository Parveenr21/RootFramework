package Utility;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;

import static Utility.ActionUtility.logFailureStep;
import static Utility.ActionUtility.logStep;

import org.openqa.selenium.WebElement;

import java.util.List;

import org.openqa.selenium.By;

import pageObject.BasePageObject;

public class CheckPageContentUtility extends BasePageObject{

	
	
	public CheckPageContentUtility(WebDriver driver) {
		super(driver);
		// TODO Auto-generated constructor stub
	}
	
	public CheckPageContentUtility(WebDriver driver, String url) {
		super(driver, url);
	}
	
	

	 public String getPageTitle (){
		
 		return driver.getTitle();
 
 	}
	 
	 /**
      * Method executes the JS to extract the Meta Tag Keywords 
       * @return : Meta Tag Keyword Content //meta[@contents]
      * @throws Exception 
       */
      public String getMetaKeyWords() {
            	  
    	  String keyWords = "";
          JavascriptExecutor js = (JavascriptExecutor) driver;
          keyWords = (String)js.executeScript(" return document.getElementsByName('keywords')[0].getAttribute('content');");
          return keyWords;
      }

      /**
          * Method executes the JS to extract the Meta Tag description 
           * @return : Meta Tag Keyword Content //meta[@contents]
          * @throws Exception 
           */
          public String getMetaDescription() {
        	  String description = "";
              JavascriptExecutor js = (JavascriptExecutor) driver;
              description = (String)js.executeScript(" return document.getElementsByName('description')[0].getAttribute('content');");
              return description;
          }
	/**
	 * Gets all the H2 Content names in page.
	 * 
	 * @return
	 */
	public String[] getH2PageContent() {
		//WaitForUtility.waitForElementToBeVisible(driver, By.xpath("//h2"));
		List<WebElement> h2List = driver.findElements(By.xpath("//h2"));
		StringBuffer sb = new StringBuffer();
		for (WebElement aspect : h2List) {
			if(!aspect.getText().isEmpty()){
			sb.append(aspect.getText()+ "\n");
			}
		}
		
		String targetText = sb.toString();
		logStep(sb.toString());
		
		return getLinksTextAndTargetsAsArray(targetText);
		
	}
	
	/**
	 * Gets all the H2 Content names in page.
	 * 
	 * @return
	 */
	public String[] getH3PageContent() {
		//WaitForUtility.waitForElementToBeVisible(driver, By.xpath("//h3"));
		List<WebElement> h3List = driver.findElements(By.xpath("//h3"));
		StringBuffer sb = new StringBuffer();
		for (WebElement aspect : h3List) {
			if(!aspect.getText().isEmpty()){
			sb.append(aspect.getText()+ "\n");
			}
		}
		
		String targetText = sb.toString();
		logStep(sb.toString());
		
		return getLinksTextAndTargetsAsArray(targetText);
		
	}
	
	/**
	 * Gets all the H4 Content names in page.
	 * 
	 * @return
	 */
	public String[] getH4PageContent() {
		//WaitForUtility.waitForElementToBeVisible(driver, By.xpath("//h4"));
		List<WebElement> h4List = driver.findElements(By.xpath("//h4"));
		StringBuffer sb = new StringBuffer();
		for (WebElement aspect : h4List) {
			if(!aspect.getText().isEmpty()){
			sb.append(aspect.getText()+ "\n");
		}
	}
		
		String targetText = sb.toString();
		logStep(sb.toString());
		
		return getLinksTextAndTargetsAsArray(targetText);
		
	}
	
	/**
	 * Gets all the paragraph Content names in page.
	 * 
	 * @return
	 */
	public String[] getParagraphPageContent() {
		//WaitForUtility.waitForElementToBeVisible(driver, By.xpath("//*[@id='content']//p"));
		List<WebElement> paragraphList = driver.findElements(By.xpath("//p"));
		StringBuffer sb = new StringBuffer();
		for (WebElement aspect : paragraphList) {
			if(!aspect.getText().isEmpty()){
			sb.append(aspect.getText()+ "\n");
		}
	}
		
		String targetText = sb.toString();
		logStep(sb.toString());
		
		return getLinksTextAndTargetsAsArray(targetText);
		
	}
	
	/**
	 * Gets all the LI Content names in page.
	 * 
	 * @return
	 */
	public String[] getLiPageContent() {
		//WaitForUtility.waitForElementToBeVisible(driver, By.xpath("//*[@id='content']//li"));
		List<WebElement> liList = driver.findElements(By.xpath("//li"));
		StringBuffer sb = new StringBuffer();
		

		for (WebElement aspect : liList) {
			if(!aspect.getText().isEmpty()){
				String result = aspect.getText().trim();
				
			    sb.append(result + "\n");
		}
	}
		
		String targetText = sb.toString();
		logStep(sb.toString());
		
		return getLinksTextAndTargetsAsArray(targetText);
		
	}
	
	/**
	 * Gets all the Link text Content names in page.
	 * 
	 * @return
	 */
	public String[] getlinkText() {
		//WaitForUtility.waitForElementToBeVisible(driver, By.xpath("//*[@id='content']//a"));
		 List<WebElement> allLinksOnPage = driver.findElements(By.xpath("//*[@id='content']//a"));
		
		StringBuffer sb = new StringBuffer();
		for (WebElement link : allLinksOnPage) {
			if(!link.getText().isEmpty()){
							
				String result = link.getText().trim();
				
			    sb.append(result + "\n");
		}
	}
		
		String linkText = sb.toString();
		logStep(linkText);
		
		return getLinksTextAndTargetsAsArray(linkText);
		
	}
	
	
	/**
	 * Gets all the security Tools text in page.
	 * 
	 * @return
	 */
	public String[] getsecurityToolsText() {
		//WaitForUtility.waitForElementToBeVisible(driver, By.xpath("//*[@id='securityTools']//li/a"));
		 List<WebElement> allsecurityToolsLinksOnPage = driver.findElements(By.xpath("//*[@id='securityTools']//li/a"));
		
		StringBuffer sb = new StringBuffer();
		for (WebElement link : allsecurityToolsLinksOnPage) {
			if(!link.getText().isEmpty()){
							
				String result = link.getText().trim();
				
			    sb.append(result + "\n");
		}
	}
		
		
		String linkText = sb.toString();
		logStep(sb.toString());
		
		return getLinksTextAndTargetsAsArray(linkText);
		
	}
	 public boolean isContentPresent(String pageSource, String[] expected){
		   
		 boolean isContentPresent = true;
		 
		 String planePageSource = pageSource.replaceAll("\\<.*?>","");

		   for(int i = 0;i<expected.length; i++){
			   if (expected[i].contains("&")){
				   String msg = expected[i].replaceAll("&", "&amp;");
				   if (!planePageSource.contains(msg)){
						  
						  logFailureStep("New Mpp Page Not Contain : " + msg);
						  isContentPresent = false;
					  }
			   }
			   else if (!planePageSource.contains(expected[i])){
				  
				  logFailureStep("New Mpp Page Not Contain : " + expected[i]);
				  isContentPresent = false;
			  }
		  }	  
		   return isContentPresent;
	  }
	
	 
	public String[] getLinksTextAndTargetsAsArray(String s) {
		String[] sArray = s.split("\n");
		return sArray;
	}
	
	public static void logStepOnConsole(String m) {
	    System.out.println(m);
	  }
}
