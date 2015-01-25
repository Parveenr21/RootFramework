package Utility;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;

import Utility.ActionUtility;

/**
 * Util to check the validity of all links and images on a page.
 * Also checks if any qa resources are used in production if the url to be tested is prod url.
 * 
 * 
 */
public class LinksCheckerCss {
  private static Map<String, String> badLinks = new HashMap<String, String>();
  private static int noOfLinks;
  private static String pageUrl;
  private static String TIME_TAKEN = "Time taken to complete testing ";
  private static final int GOOD_RESPONSE_CODE = HttpURLConnection.HTTP_OK;
  private static final int REDIRECT_RESPONSE_CODE = HttpURLConnection.HTTP_MOVED_TEMP;
  private static final String NO_OF_LINKS = "Total no of links to be validated:";
  private static final String NO_OF_IMAGES = "Total no of images to be validated:";
  private static final String BORDER = "___________________________"
      + "_____________________________________";
  private static final String TESTING_LINK = "Testing:";
  private static final String JS = "javascript:";
  private static final String COMPLETED_TESTING = "Completed testing:";
  private static final String PRINTING_BAD_LINKS = "PRINTING BAD LINKS....";
  private static final String DONE_PRINTING_BAD_LINKS = "DONE PRINTING BAD LINKS";
  private static final String PRINTING_BAD_IMAGES = "PRINTING BAD IMAGES....";
  private static final String DONE_PRINTING_BAD_IMAGES = "DONE PRINTING BAD IMAGES";
  private static final String RESPONSE_CODE = "Response Code:";

  /**
   * Checks if all the links and image src on page accessed by url are good.Also
   * checks for qa links if the tests are run on prod.
   * 
   * @param url
   *          Page on which the links are to be validated
   * @return true if all the links on the page are good.
   */
  public boolean checkLinksAndImagesOnPage(String url, WebDriver driver) {
  
    driver.get(url);
    pageUrl = url;
    //boolean areAllLinksInCssGood = validateLinksInCssOnPage(driver, null);
    boolean areAllImagesGood = validateAllImagesOnPage(driver, null);
    boolean areAllLinksGood = validateAllLinksOnPage(driver, null);
   
    return areAllImagesGood && areAllLinksGood;
  }
  
  public static boolean checkLinksOnPage(String url, WebDriver driver) {
    driver.get(url);
    pageUrl = url;
    boolean areAllLinksGood = validateAllLinksOnPage(driver, null);
   
    return areAllLinksGood;
  }
  
  public static boolean checkImagesOnPage(String url, WebDriver driver) {
    driver.get(url);
    pageUrl = url;
    boolean areAllImagesGood = validateAllImagesOnPage(driver, null);
   
    return areAllImagesGood;
  }
  
  public static boolean checkCSSLinksOnPage(String url, WebDriver driver) {
    driver.get(url);
    pageUrl = url;
    boolean areAllLinksInCssGood = validateLinksInCssOnPage(driver, null);
   
    return areAllLinksInCssGood;
  }

  /**
   * Checks if all the links and image src on page accessed by url are good.Also
   * checks for qa links if the tests are run on prod.
   * 
   * @param url
   *          Page on which the links are to be validated
   * @return true if all the links on the page are good.
   */
  public static boolean checkLinksAndImagesOnPage(WebDriver driver) {
    String url = driver.getCurrentUrl();
    pageUrl = url;
    boolean areAllLinksInCssGood = validateLinksInCssOnPage(driver, null);
    boolean areAllImagesGood = validateAllImagesOnPage(driver, null);
    boolean areAllLinksGood = validateAllLinksOnPage(driver, null);
    return areAllImagesGood && areAllLinksGood && areAllLinksInCssGood;  }

  /**
   * Validates the images in the page by getting the img tag.
   * @param driver
   * @return true if all images are good
   */
  public static boolean validateAllImagesOnPage(WebDriver driver, List<String> exclusionsList) {
	  boolean urlContainsExclusions = false;
    long startTime = System.currentTimeMillis();
    List<String> areAllImagesOnPageGood = new ArrayList<String>();
    logStepOnConsole("Testing images on page.");
    List<WebElement> allImagesOnPage = driver.findElements(By.xpath("//img"));
    logStepOnConsoleAndReport(NO_OF_IMAGES + allImagesOnPage.size());
    for (WebElement img : allImagesOnPage) {
    	
   String convertedUrl="";
    	if(img.getAttribute("src").contains("www") && img.getAttribute("src").contains("paypal"))
		{
			Pattern p1 = Pattern.compile("www");
			Pattern p2 = Pattern.compile("paypal");
			String[] result1 = p1.split(img.getAttribute("src"));
			String[] result2 = p2.split(img.getAttribute("src"));
			convertedUrl = result1[0] + "www.paypal" + result2[1];
		}
		else
		{
			convertedUrl = img.getAttribute("src");
		}
    	
      String urlToTest = convertedUrl;
      
      
      String resourceName = img.getAttribute("alt");
      logStepOnConsole(BORDER);
      logStepOnConsole("Testing image:" + resourceName + " : " + urlToTest);
      // TODO:prsakharkar add ability to add more exclusions
      if(urlToTest!=null && !urlToTest.contains(JS)){
        if(exclusionsList!=null && exclusionsList.size()!=0){
          for (String exclusion : exclusionsList) {
            if(urlToTest.contains(exclusion)){
              urlContainsExclusions = true;
            }
          }
        }
        if(!urlContainsExclusions){
          String isLinkGood = validateResponseCode(urlToTest, resourceName) ? "Y" : "N";
          areAllImagesOnPageGood.add(isLinkGood);
        }
      }else{
        logStepOnConsole("Excluding js.." + resourceName + ":" + urlToTest);
      }
      logStepOnConsole(BORDER);
      logStepOnConsole("\n");
    }
    logStepOnConsoleAndReport(COMPLETED_TESTING + pageUrl);
    logStepOnConsoleAndReport("<b>" + PRINTING_BAD_IMAGES + "</b>");
    printBadLinks();
    logStepOnConsoleAndReport("<b>" + DONE_PRINTING_BAD_IMAGES + "</b>");
    long endTime = System.currentTimeMillis();
    long timeToRun = endTime - startTime;
    logStepOnConsole(TIME_TAKEN + allImagesOnPage.size() + " images in secs is:"
        + timeToRun * 0.001);
    if(areAllImagesOnPageGood.contains("N")){
      return false;
    } else {
      return true;
    }
  }

  /**
   * Validates all links on the page by getting them using the anchor tag.
   * @param driver
   * @return true if all links on page is good.
   */
  public static boolean validateAllLinksOnPage(WebDriver driver, List<String> exclusionsList) {
    boolean urlContainsExclusions = false;
    long startTime = System.currentTimeMillis();
    List<String> areAllLinksOnPageGood = new ArrayList<String>();
    List<WebElement> allLinksOnPage = driver.findElements(By.xpath("//a"));
    noOfLinks = allLinksOnPage.size();
    logStepOnConsoleAndReport(NO_OF_LINKS + noOfLinks);
    for (WebElement link : allLinksOnPage) {
    	
    	String convertedUrl="";
    	if(link.getAttribute("href").contains("www") && link.getAttribute("href").contains("paypal"))
		{
			Pattern p1 = Pattern.compile("www");
			Pattern p2 = Pattern.compile("paypal");
			String[] result1 = p1.split(link.getAttribute("href"));
			String[] result2 = p2.split(link.getAttribute("href"));
			convertedUrl = result1[0] + "www.paypal" + result2[1];
		}
		else
		{
			convertedUrl = link.getAttribute("href");
		}
    	
      String urlToTest = convertedUrl;
      String resourceName = link.getText();
      logStepOnConsole(BORDER);
      logStepOnConsole(TESTING_LINK + resourceName + ":" + urlToTest);
      if(urlToTest!=null && !urlToTest.contains(JS) && !urlToTest.contains("mailto:")){
        if(exclusionsList!=null && exclusionsList.size()!=0){
          for (String exclusion : exclusionsList) {
            if(urlToTest.contains(exclusion)){
              urlContainsExclusions = true;
            }
          }
        }
        if(!urlContainsExclusions){
          String isLinkGood = validateResponseCode(urlToTest, resourceName) ? "Y" : "N";
          areAllLinksOnPageGood.add(isLinkGood);
        }
      }else{
        logStepOnConsole("Excluding js.." + resourceName + ":" + urlToTest);
      }
      logStepOnConsole(BORDER);
      logStepOnConsole("\n");
    }

    logStepOnConsole(COMPLETED_TESTING + pageUrl);
    logStepOnConsoleAndReport("<b>" + PRINTING_BAD_LINKS + "</b>");
    printBadLinks();
    logStepOnConsoleAndReport("<b>" + DONE_PRINTING_BAD_LINKS + "</b>");
    
    long endTime = System.currentTimeMillis();
    long timeToRun = endTime - startTime;
    logStepOnConsole(TIME_TAKEN + noOfLinks + "links in secs is:" + timeToRun * 0.001);
    if(areAllLinksOnPageGood.contains("N")){
      return false;
    } else {
      return true;
    }
  }

  /**
   * Validates all links on the page by getting them using the anchor tag.
   * @param driver
   * @return true if all links on page is good.
   */
  public static boolean validateLinksInCssOnPage(WebDriver driver, List<String> exclusionsList) {
    long startTime = System.currentTimeMillis();
    List<String> areLinksOnCssGood = new ArrayList<String>();
    List<WebElement> allLinkTags= driver.findElements(By.tagName("link"));
    List<String> extractedLinks = new ArrayList<String>();
    logStepOnConsole("List of css files found in page:");
    for (WebElement linkTag : allLinkTags) {
      String href = linkTag.getAttribute("href");
      if(href.contains(".css")){
        logStepOnConsole(href);
        extractedLinks = extractUrlsFromCss(href);
      }
    }
    logStepOnConsole("Testing links extracted from css files.");
    for (String link : extractedLinks) {
      logStepOnConsole(link);
      String isLinkGood = validateResponseCode(link, "Links from css.") ? "Y" : "N";
      areLinksOnCssGood.add(isLinkGood);
    }
    long endTime = System.currentTimeMillis();
    long timeToRun = endTime - startTime;
    logStepOnConsole(TIME_TAKEN + extractedLinks.size() + " links in css is (secs):" + timeToRun * 0.001);
    if(areLinksOnCssGood.contains("N")){
      return false;
    } else {
      return true;
    }
   
    
  }

  private static List<String> extractUrlsFromCss(String cssUrl){
    WebDriver driver = new HtmlUnitDriver();
    driver.get(cssUrl);
    String cssSource = driver.getPageSource();
    List<String> urlsFromCss = new ArrayList<String>();
    String startString = "url(";
    String endString = ")";
    while(cssSource.contains(startString)){
      int startIndex = cssSource.indexOf(startString);
      int endIndex = cssSource.substring(startIndex).indexOf(endString) + startIndex;
      String url = cssSource.substring(startIndex, endIndex).trim();
      String cleanUrl = url.replace(startString, "").replaceAll("\"", "");
      urlsFromCss.add(cleanUrl);
      
      if(cssSource.contains(url)){
        cssSource = cssSource.replace(url,"");
      }
    }

    driver.quit();
    return urlsFromCss;
  }

  /**
   * TODO Method to validate acceptable inline CSS in page.
   * @param listOfAcceptableInlineCss
   * @return
   */
  public static boolean validateInlineJsOnPage(List<String> listOfAcceptableInlineCss){
    return true;
  }

  
  /**
   * Validates the given WebElement's link/img src by checking the ResponseCode.
   * 
   * @param link
   * @param tag
   * @return true if reponse code is OK.
   */
  public static boolean validateResponseCode(String urlToTest,
      String resourceName) {
    boolean isResponseOk = false;
    if(pageUrl != null){
        if (!pageUrl.contains(".qa.") && urlToTest.contains(".qa.")) {
          logStepOnConsoleAndReport(urlToTest + " points to qa environment.");
          badLinks.put("Resource Name:" + resourceName + " urlToTest:" + urlToTest,
              " points to qa environment.");
          return false;
        }
    }
    int responseCode = 0;
    try {
      URL url = new URL(urlToTest);
      HttpURLConnection urlConn = (HttpURLConnection) url.openConnection();
      urlConn.connect();
      responseCode = urlConn.getResponseCode();
      isResponseOk = (GOOD_RESPONSE_CODE == responseCode)|| (REDIRECT_RESPONSE_CODE == responseCode) ? true : false;
    } catch (MalformedURLException urlException) {
      logStepOnConsoleAndReport("MalformedURLException for " + urlToTest);
      isResponseOk = false;
    } catch (IOException e) {
      logStepOnConsoleAndReport("Error creating HTTP connection for " + resourceName + " url:"
          + urlToTest);
      isResponseOk = false;
    }
    String testResult = isResponseOk ? "Url is good"
        : "Link response code not OK:" + responseCode;
    logStepOnConsole(testResult);
    if (!isResponseOk) {
      badLinks.put("Link Name:" + resourceName + " LinkHref:" + urlToTest,
          RESPONSE_CODE + responseCode);
    }
    return isResponseOk;
  }

  /**
   * Prints all the bad links.
   */
  private static void printBadLinks() {
    if (!badLinks.isEmpty()) {
      for (Map.Entry entry : badLinks.entrySet()) {
        logStepOnConsoleAndReport(entry.getKey() + ", " + entry.getValue());
      }
    } else {
      logStepOnConsoleAndReport("LGTM!");
    }
  }

  public String[] getHomePageLeftNavLinkTextAndTargets(String URL,  WebDriver driver) {
		
		driver.get(URL);
		List<WebElement> links = driver.findElements(By.xpath("//a"));
		StringBuffer sb = new StringBuffer();
		for (WebElement link : links) {
			String convertedUrl="";
			if(link.getAttribute("href").contains("?cmd"))
			{
				
				Pattern p2 = Pattern.compile("?cmd");
				
				String[] result2 = p2.split(link.getAttribute("href"));
				convertedUrl = result2[1];
			}
			
			else
			{
				convertedUrl = link.getAttribute("href");
			}
			sb.append(link.getText() + ":" + convertedUrl + "\n");
		}
		String targetLinks = sb.toString();
		
		return getLinksTextAndTargetsAsArray(targetLinks);
	}
  
  public String[] getLinksTextAndTargetsAsArray(String s) {
		String[] sArray = s.split("\n");
		return sArray;
	}
  
  // TODO: Remove this after testing.
  public void main(String[] a) {
	  WebDriver driver = new HtmlUnitDriver();
    checkLinksAndImagesOnPage("https://cms.paypal.com/us/cgi-bin/marketingweb?cmd=_render-content&content_ID=security/buyer_protection", driver);
  }

  public static void logStepOnConsole(String m) {
    System.out.println(m);
  }

  public static void logStepOnConsoleAndReport(String m) {
    System.out.println(m);
    ActionUtility.logStep(m);
  }

}
