package Utility;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

/**
* This class contains methods to perform mouseactions on a webpage using webdriver. 
*
*/
public class MouseActionUtility {

	  public static void doMouseActionUsingJS(String mouseEvent, By hoverOn, WebDriver webDriver) {
		    WebElement element = webDriver.findElement(hoverOn);
			((JavascriptExecutor) webDriver).executeScript(
					"var event = document.createEvent('MouseEvents');"
					    + "event.initEvent('" + mouseEvent + "', true, true);"
					    + "var element = arguments[0];"
					    + "element.dispatchEvent(event);", element);
	  }
	  
  public static void doMouseActionUsingJS(String mouseEvent,
      WebElement hoverOnElement, WebDriver webDriver) {
    ((JavascriptExecutor) webDriver).executeScript(
        "var event = document.createEvent('MouseEvents');"
            + "event.initEvent('" + mouseEvent + "', true, true);"
            + "var element = arguments[0];" + "element.dispatchEvent(event);",
        hoverOnElement);
  }
}