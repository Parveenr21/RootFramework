package Utility;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;

import Utility.ActionUtility;

/**
 * Util to check the validity of all links and images on a page.
 * Also checks if any qa resources are used in production if the url to be tested is prod url.
 * 
 * @author prsakharkar (prakash sakharkar)
 * 
 */
public class LinksChecker {
  private static Map<String, String> badLinks = new HashMap<String, String>();
  private static int noOfLinks;
  private static String pageUrl;
  private static String TIME_TAKEN = "Time taken to complete testing ";
  private static final int GOOD_RESPONSE_CODE = HttpURLConnection.HTTP_OK;
  private static final int MOVED_PERM_RESPONSE_CODE =HttpURLConnection.HTTP_MOVED_PERM;
  private static final int MOVED_TEMP_RESPONSE_CODE =HttpURLConnection.HTTP_MOVED_TEMP;
  private static final String NO_OF_LINKS = "Total no of links to be validated:";
  private static final String NO_OF_IMAGES = "Total no of images to be validated:";
  private static final String PAGE_TO_TEST = "Page to be tested:";
  private static final String BORDER = "___________________________"
      + "_____________________________________";
  private static final String TESTING_LINK = "Testing:";
  private static final String EXCLUSIONS = "javascript:";
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
  public static boolean checkLinksAndImagesOnPage(String url) {
    WebDriver driver = new FirefoxDriver();
   // HtmlUnitDriver driver = new HtmlUnitDriver();
    
    logStepOnConsoleAndReport(PAGE_TO_TEST + url);
    driver.get(url);
    pageUrl = url;
    boolean isPageGood = (validateAllImagesOnPage(driver) && validateAllLinksOnPage(driver));
    driver.quit();
    return isPageGood;
  }

  /**
   * Checks if all the links on page accessed by url are good.Also
   * checks for qa links if the tests are run on prod.
   * 
   * @param url
   *          Page on which the links are to be validated
   * @return true if all the links on the page are good.
   */
  public static boolean checkLinksOnPage(String url) {
	    //WebDriver driver = new FirefoxDriver();
	    HtmlUnitDriver driver = new HtmlUnitDriver();
	    
	    logStepOnConsoleAndReport(PAGE_TO_TEST + url);
	    driver.get(url);
	    pageUrl = url;
	    boolean isPageGood = (validateAllLinksOnPage(driver));
	    driver.quit();
	    return isPageGood;
	  }
  
  /**
   * Checks if all image src on page accessed by url are good.Also
   * checks for qa links if the tests are run on prod.
   * 
   * @param url
   *          Page on which the links are to be validated
   * @return true if all the links on the page are good.
   */
  public static boolean checkImagesOnPage(String url) {
    //WebDriver driver = new FirefoxDriver();
    HtmlUnitDriver driver = new HtmlUnitDriver();
    
    logStepOnConsoleAndReport(PAGE_TO_TEST + url);
    driver.get(url);
    pageUrl = url;
    boolean isPageGood = (validateAllImagesOnPage(driver));
    driver.quit();
    return isPageGood;
  }
  
  
  /**
   * Validates the images in the page by getting the img tag.
   * @param driver
   * @return true if all images are good
   */
  private static boolean validateAllImagesOnPage(WebDriver driver) {
    long startTime = System.currentTimeMillis();
    boolean areAllImagesOnPageGood = false;
    logStepOnConsoleAndReport("Testing images on page.");
    List<WebElement> allImagesOnPage = driver.findElements(By.xpath("//img"));
    logStepOnConsoleAndReport(NO_OF_IMAGES + allImagesOnPage.size());
    for (WebElement img : allImagesOnPage) {
      String urlToTest = img.getAttribute("src");
      String resourceName = img.getAttribute("alt");
      logStepOnConsoleAndReport(BORDER);
      logStepOnConsoleAndReport("Testing image:" + resourceName + " : " + urlToTest);
      // TODO:aravindk add ability to add more exclusions
      if (urlToTest == null || urlToTest.contains(EXCLUSIONS)) {
    	  logStepOnConsoleAndReport("Excluding js and null urls.." + resourceName + ":" + urlToTest);
        areAllImagesOnPageGood = true;
      } else {
        areAllImagesOnPageGood = validateResponseCode(urlToTest, resourceName);
      }
      logStepOnConsoleAndReport(BORDER);
      //logStepOnConsoleAndReport("\n");
    }
    logStepOnConsoleAndReport(COMPLETED_TESTING + pageUrl);
    logStepOnConsoleAndReport(PRINTING_BAD_IMAGES);
    printBadLinks();
    logStepOnConsoleAndReport(DONE_PRINTING_BAD_IMAGES);
    long endTime = System.currentTimeMillis();
    long timeToRun = endTime - startTime;
    logStepOnConsoleAndReport(TIME_TAKEN + allImagesOnPage.size() + " images in secs is:"
        + timeToRun * 0.001);
    return areAllImagesOnPageGood;
  }

  /**
   * Validates all links on the page by getting them using the anchor tag.
   * @param driver
   * @return true if all links on page is good.
   */
  private static boolean validateAllLinksOnPage(WebDriver driver) {
    long startTime = System.currentTimeMillis();
    boolean areAllLingsOnPageGood = false;
    List<WebElement> allLinksOnPage = driver.findElements(By.xpath("//a"));
    noOfLinks = allLinksOnPage.size();
    logStepOnConsoleAndReport(NO_OF_LINKS + noOfLinks);
    for (WebElement link : allLinksOnPage) {
      String urlToTest = link.getAttribute("href");
      String resourceName = link.getText();
      logStepOnConsoleAndReport(BORDER);
      logStepOnConsoleAndReport(TESTING_LINK + resourceName + ":" + urlToTest);
      // TODO:aravindk add ability to add more exclusions
      if (urlToTest == null || urlToTest.contains(EXCLUSIONS)
          || urlToTest.contains("mailto:")) {
    	  logStepOnConsoleAndReport("Excluding js and null urls.." + resourceName + ":" + urlToTest);
        areAllLingsOnPageGood = true;
      } else {
        areAllLingsOnPageGood = validateResponseCode(urlToTest, resourceName);
      }
      logStepOnConsoleAndReport(BORDER);
      logStepOnConsoleAndReport("\n");
    }
    logStepOnConsoleAndReport(COMPLETED_TESTING + pageUrl);
    logStepOnConsoleAndReport(PRINTING_BAD_LINKS);
    printBadLinks();
    logStepOnConsoleAndReport(DONE_PRINTING_BAD_LINKS);
    long endTime = System.currentTimeMillis();
    long timeToRun = endTime - startTime;
    logStepOnConsoleAndReport(TIME_TAKEN + noOfLinks + "links in secs is:" + timeToRun * 0.001);
    return areAllLingsOnPageGood;
  }

  /**
   * Validates the given WebElement's link/img src by checking the ResponseCode.
   * 
   * @param link
   * @param tag
   * @return true if reponse code is OK.
   */
  private static boolean validateResponseCode(String urlToTest,
      String resourceName) {
    boolean isResponseOk = false;
    if (!pageUrl.contains(".qa.") && urlToTest.contains(".qa.")) {
    	logStepOnConsoleAndReport(urlToTest + " points to qa environment.");
      badLinks.put("Resource Name:" + resourceName + " urlToTest:" + urlToTest,
          " points to qa environment.");
      return false;
    }
    int responseCode = 0;
    try {
      URL url = new URL(urlToTest);
      HttpURLConnection urlConn = (HttpURLConnection) url.openConnection();
      urlConn.connect();
      responseCode = urlConn.getResponseCode();
      isResponseOk = (GOOD_RESPONSE_CODE == responseCode || MOVED_PERM_RESPONSE_CODE == responseCode) ? true : false;
      //isResponseOk = (GOOD_RESPONSE_CODE == responseCode) || (MOVED_PERM_RESPONSE_CODE == responseCode) || (MOVED_TEMP_RESPONSE_CODE ==responseCode) ? true : false;
    } catch (MalformedURLException urlException) {
    	logStepOnConsoleAndReport("MalformedURLException for " + urlToTest);
      logStepOnConsoleAndReport(RESPONSE_CODE + responseCode);
      isResponseOk = false;
    } catch (IOException e) {
    	logStepOnConsoleAndReport("Error creating HTTP connection for " + resourceName + " url:"
          + urlToTest);
      logStepOnConsoleAndReport(RESPONSE_CODE + responseCode);
      isResponseOk = false;
    }
    String testResult = isResponseOk ? "Url is good"
        : "Link response code not OK:" + responseCode;
    logStepOnConsoleAndReport(testResult);
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

//TODO: remove this after testing.
 public static void main(String[] a) {
   checkLinksAndImagesOnPage("https://cms.paypal.com/us/cgi-bin/marketingweb?cmd=_render-content&content_ID=security/buyer_protection");
 }

 public static void logStepOnConsole(String m) {
   System.out.println(m);
 }

 public static void logStepOnConsoleAndReport(String m) {
   System.out.println(m);
   ActionUtility.logStep(m);
 }
}
