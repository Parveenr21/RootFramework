package Utility;


import static Utility.ActionUtility.logStep;

import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.NotFoundException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;

import Utility.ActionUtility;

import com.google.common.base.Function;

/**
 * It would be better to have this usage in the pageObjects class than in test
 * if we can.
 * 
 *  
 */
public class WaitForUtility {

  static long TIMEOUT_S = 60;
  static int INT_TIMEOUT_S = Integer.parseInt(Long.toString(TIMEOUT_S));
  static long WAIT_TIMEOUT = 30L;

  protected static Function<WebDriver, WebElement> presenceOfElementLocated(
      final By locator) {
    return new Function<WebDriver, WebElement>() {
      public WebElement apply(WebDriver driver) {
        return driver.findElement(locator);
      }
    };
  }

  /**
   * Waits for the element to be visible until a timeout of 30 secs.
   * 
   * @param driver
   * @param locator
   */
  public static void waitForElementToBeVisible(final WebDriver driver,
      final By locator) throws RuntimeException{
    Wait<WebDriver> wait = new WebDriverWait(driver, WAIT_TIMEOUT);
    try {
    	wait.until(new ExpectedCondition<WebElement>() {
    	      public WebElement apply(WebDriver driver) {
//    	        driver.switchTo().defaultContent();
    	        WebElement element = driver.findElement(locator);
    	        if (element.isDisplayed()) {
    	          return element;
    	        }
    	        return null;
    	      }
    	    });
	  } catch (Exception e) {
	   // captureScreenShot(driver);
		  throw new RuntimeException("Exception while waiting for " + locator +
		      ". Exception:" + e + " on " + driver.getCurrentUrl());
	  } 
  }

  /**
   * Waits for the element to be visible until a timeout of 30 secs.
   * 
   * @param driver
   * @param locator
   */
  public static void waitForElementToBeVisible(final WebDriver driver,
      final WebElement webElement) throws RuntimeException{
    Wait<WebDriver> wait = new WebDriverWait(driver, WAIT_TIMEOUT);
    try {
      wait.until(new ExpectedCondition<WebElement>() {
            public WebElement apply(WebDriver driver) {
//              driver.switchTo().defaultContent();
              WebElement element = webElement;
              if (element.isDisplayed()) {
                return element;
              }
              return null;
            }
          });
    } catch (Exception e) {
     // captureScreenShot(driver);
      throw new RuntimeException("Exception while waiting for " + webElement +
          ". Exception:" + e + " on " + driver.getCurrentUrl());
    } 
  }
  public static WebElement waitForElementAndReturnElement(final WebDriver driver,
	      final By locator) throws RuntimeException {
	  waitForElementToBeVisible(driver, locator);
	  return driver.findElement(locator);
  }

  /**
   * Waits for the element to be visible until the specified timeout.
   * 
   * @param driver {@link WebDriver}
   * @param locator {@link By}
   * @param timeOut long
   */
  public static void waitForElementToBeVisible(final WebDriver driver,
      final By locator, long timeOut) throws RuntimeException{
    Wait<WebDriver> wait = new WebDriverWait(driver, timeOut);
    try {
    	wait.until(new ExpectedCondition<WebElement>() {
    	      public WebElement apply(WebDriver driver) {
//    	        driver.switchTo().defaultContent();
    	        WebElement element = driver.findElement(locator);
    	        if (element.isDisplayed()) {
    	          return element;
    	        }
    	        return null;
    	      }
    	    });
	  } catch (Exception e) {
	   // captureScreenShot(driver);
		  throw new RuntimeException("Exception while waiting for " +
		      locator + ". Exception:" + e);
	  } 
  }

  /**
   * Waits for the given text until timing out at 30 secs.
   * 
   * @param driver
   * @param locator
   * @param text
   */
  public static void waitForText(final WebDriver driver, final By locator,
      final String text) {
    Wait<WebDriver> wait = new WebDriverWait(driver, WAIT_TIMEOUT);
    try {
    	wait.until(new ExpectedCondition<Boolean>() {
    	      public Boolean apply(WebDriver webDriver) {
    	        String currentText = "";
    	        try {
    	          currentText = driver.findElement(locator).getText();
    	        } catch (Exception e) {
    	          // ignore if element is not present.
    	        }
    	        logStep("Waiting for:" + text + " Found:" + currentText);
    	        return currentText.contains(text);
    	      }
    	    });

	  } catch (Exception e) {
	   // captureScreenShot(driver);
		  throw new RuntimeException("Exception while waiting for text " +
		      text + " in " + locator + ". Exception:" + e);
	  }
  }

  /**
   * Waits until the given element is either hidden or deleted.
   * 
   * @param locator
   * @param timeout
   */
  public static void waitUntilElementDisappears(final WebDriver driver,
      final By locator) {
    ActionUtility.logFailureStep("IN WAIT FOR DISAPPEAR");
    Wait<WebDriver> wait = new WebDriverWait(driver, WAIT_TIMEOUT);
    try{
    wait.until(new ExpectedCondition<Boolean>() {
        public Boolean apply(WebDriver driver) {
          try {
            WebElement element = driver.findElement(locator);
            logStep(locator + " spotted..");
            return Boolean.valueOf(!element.isDisplayed());
          } catch (NotFoundException e) {
            logStep(locator + " disappeared..");
            return Boolean.TRUE;
          } catch (StaleElementReferenceException se) {
            logStep(locator + " disappeared..");
            return Boolean.TRUE;
          }
        }
      });
    } catch (Exception e) {
     // captureScreenShot(driver);
      ActionUtility.logFailureStep(locator + " did not disappear.");
      throw new RuntimeException(locator + " did not disappear.");
    }
  }

  public static void waitForTitleStartingWithString(final WebDriver driver, final String title){
    try {
      (new WebDriverWait(driver, WAIT_TIMEOUT)).until(new ExpectedCondition<Boolean>() {
        public Boolean apply(WebDriver d) {
            return d.getTitle().startsWith(title);
        }
    });
    } catch (Exception e) {
     // captureScreenShot(driver);
      throw new RuntimeException(title + " did not show up after polling for " + WAIT_TIMEOUT + " secs.");
    }
  }

  public static void waitForTitleContainingString(final WebDriver driver, final String title){
    try {
      (new WebDriverWait(driver, WAIT_TIMEOUT)).until(new ExpectedCondition<Boolean>() {
        public Boolean apply(WebDriver d) {
            return d.getTitle().contains(title);
        }
    });
    } catch (Exception e) {
      //captureScreenShot(driver);
      throw new RuntimeException(title + " did not show up after polling for " + WAIT_TIMEOUT + " secs.");
    }
  }

  public static void waitForCurrentUrlToContainString(final WebDriver driver, final String url){
    try {
      (new WebDriverWait(driver, WAIT_TIMEOUT)).until(new ExpectedCondition<Boolean>() {
        public Boolean apply(WebDriver d) {
            return d.getCurrentUrl().contains(url);
        }
    });
    } catch (Exception e) {
      String currUrl = driver.getCurrentUrl();
      if (currUrl.contains(url)) {
        return;
      }
     // captureScreenShot(driver);
      throw new RuntimeException(url + " was not present in current url: " + currUrl +
          " after polling for " + WAIT_TIMEOUT + " secs.");
    }
  }

  public static void waitForCurrentUrlToMatchString(final WebDriver driver, final String url){
    try {
      (new WebDriverWait(driver, WAIT_TIMEOUT)).until(new ExpectedCondition<Boolean>() {
        public Boolean apply(WebDriver d) {
            return d.getCurrentUrl().trim().equals(url.trim());
        }
    });
    } catch (Exception e) {
      String currUrl = driver.getCurrentUrl().trim();
      if (currUrl.equals(url.trim())) {
        return;
      }
     // captureScreenShot(driver);
      throw new RuntimeException(url + " did not match current url: " + currUrl +
          " after polling for " + WAIT_TIMEOUT + " secs.");
    }
  }

  /**
   * Explicitly waits for the specified milliseconds.Use this method only if
   * absolutely neccessary.
   * 
   * @param milliSeconds
   */
  public static void holdUntil(long milliSeconds) {
    long waitUntilTime = System.currentTimeMillis() + (milliSeconds);
    while (System.currentTimeMillis() < waitUntilTime) {
      // do nothing just wait for seconds.
    }
  }
  
  public static void waitForSeconds(WebDriver driver, int waitTime){
    driver.manage().timeouts().implicitlyWait(waitTime, TimeUnit.SECONDS);
  }

}
