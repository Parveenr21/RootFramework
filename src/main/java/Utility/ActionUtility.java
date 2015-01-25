package Utility;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.ElementNotVisibleException;
import org.openqa.selenium.HasInputDevices;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriver.Options;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.internal.Locatable;
import org.openqa.selenium.remote.Augmenter;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Reporter;

import Utility.ActionUtility;
import Utility.MouseActionUtility;
import Utility.StringHelper;
import Utility.WaitForUtility;

import com.google.common.base.Function;

/**
 * Contains methods that allow to perform most of the actions on page.
 * 
 *  
 */
public class ActionUtility {

  private static final String OPEN = "Opened ";
  private static final String CLICK_EVENT = "Clicked ";
  private static final String TYPED = "Typed ";
  private static final String CLEARED = "Cleared ";
  private static final String SUBMITTED = "Submitted ";
  private static final String SEPARATOR = " - ";
  private static final String HOVERED = "Hovered ";
  private static final String CURRENT_URL = "Current url is:";
  private static final String FOUND = " found ";
  private static final String COOKIES = " cookies ";
  private static final String ADDED = " Added ";
  private static final long WAIT_TIMEOUT = 30L;
  private static final String SELECT_EVENT = "Selected ";

  /**
   * Opens the url and logs it.
   * 
   * @param driver
   * @param url
   */
  public static void open(final WebDriver driver, String url) {
    driver.get(url);
    logStep(OPEN + url);
  }

  /**
   * Sets cookie to the current browser instance.
   * @param driver current browser instance
   * @param cookieName cookie that has to be added.
   * @param cookieValue value of the cookie to be added.
   */
  public static void setCookie(WebDriver driver, String cookieName, String cookieValue) {
    Cookie cookie = new Cookie(cookieName, cookieValue);
    driver.manage().addCookie(cookie);
    logStep(ADDED + COOKIES + ": " + cookieName + "|" + cookieValue);
  }

  /**
   * Clear all cookies. 
   * @param driver
   */
  public static void clearAllCookies(WebDriver driver) {
    driver.manage().deleteAllCookies();
    logStep(CLEARED + COOKIES);
  }

  /**
   * Deletes the specified cookiesName.
   * @param driver
   * @param cookieName
   * @param cookieValue
   */
  public static void deleteCookie(WebDriver driver, String cookieName, String cookieValue) {
    driver.manage().deleteCookie(new Cookie(cookieName, cookieValue));
    logStep(CLEARED + COOKIES + ": " + cookieName);
  }

  /**
   * Returns the current url in the active window for the given driver.
   * 
   * @param driver
   * @return
   */
  public static String getCurrentUrl(WebDriver driver) {
    String currentUrl = driver.getCurrentUrl();
    logStep(CURRENT_URL + currentUrl);
    return currentUrl;
  }

  /**
   * Switches to window with the specified title when there are multiple
   * windows.
   * 
   * @param driver
   * @param windowTitle
   */
  public static void switchToWindowUsingTitle(WebDriver driver, String windowTitle) {
    Set<String> handlers = driver.getWindowHandles();
    if (driver.getWindowHandles().size() >= 1) {
      for (String handler : handlers) {
        driver.switchTo().window(handler);
        if (driver.getTitle().contains(windowTitle)) {
          break;
        }
      }
    }
  }

  /**
   * Switches to window with the specified locator when there are multiple
   * windows.
   * 
   * @param driver
   * @param windowTitle
   */
  public static void switchToWindowUsingLocator(WebDriver driver, By locator) {
    Set<String> handlers = driver.getWindowHandles();
    if (driver.getWindowHandles().size() >= 1) {
      for (String handler : handlers) {
        driver.switchTo().window(handler);
        if (isElementPresent(driver, locator)) {
          break;
        }
      }
    }
  }

  /**
   * Switches to window with the specified handle
   * 
   * @param driver
   * @param handle
   */
  public static void switchToWindowHandle(WebDriver driver, String handle) {
    if (!driver.getWindowHandle().equals(handle)) {
      driver.switchTo().window(handle);
    }
  }

  /**
   * Captures the screenshot of the current page when the SCREENSHOT system
   * property is set to 'on'. eg:SCREENSHOT=ON in bat and your pom.xml should
   * have the SCREENSHOT property defined.
   * <p>
   * This method is designed to work both on RemoteWebDriver and local driver.
   * </p>
   * 
   * @param driver
   */
  public static void captureScreenShot(WebDriver driver) {
    if ("on".equalsIgnoreCase(System.getProperty("SCREENSHOT"))) {
      try {
        // If the driver is RemoteWebDriver then augment it to enable screen
        // shots on it.
        if (driver.getClass().getName().contains("RemoteWebDriver")) {
          driver = new Augmenter().augment(driver);
        }
        final DateFormat df = new SimpleDateFormat("HH_mm_ss");
        final DateFormat dfFolder = new SimpleDateFormat("yyyy_MM_dd");
        File scrFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        String browser = System.getProperty("BROWSER");
        String fileName = "target/surefire-reports/screenshots/" + dfFolder.format(new Date())
            + "/" + browser + "/" + df.format(new Date()) + ".png";
        FileUtils.copyFile(scrFile, new File(fileName));
        String currentDir = new File(".").getAbsolutePath().replace(".", "");
        currentDir = "file:///" + (currentDir + fileName).replace("\\", "/");
        String machineName = "fusion.paypal.com/jenkins/view/QA_Marketing/job/Campaign_Studio_Regression/ws/";
        if (currentDir.contains("hudson") || currentDir.contains("HUDSON")
            || currentDir.contains("JENKINS") || currentDir.contains("FUSION")) {
          currentDir = currentDir.replace("file:///", "http://").replace("jobs", "job").replace(
              "C:/EbayCI/HUDSON-HOME", machineName).replace("/x/hudson/workspace/Campaign_Studio_Regression/", machineName)
              .replace("workspace", "ws");
        }
        logStep("<b><a href='" + currentDir + "' onclick=\"window.open('" + currentDir
            + "','popup','" + "width=800,height=1500,toolbar=no,directories=no,location=no,"
            + "menubar=no,status=no,left=0,top=0'); return false\">" + "Click to see screen shot"
            + "</a></b> Browser Size:" + getBrowserSize(driver));
      } catch (Exception e) {
        logFailureStep("Could not capture screenshot due to:" + e);
      }
    } else {
      logStep("Screenshot not enabled." + "set flag SCREENSHOT=on to capture screenshot.");
    }
  }
  
  //file:////x/hudson/workspace/Campaign_Studio_Regression/target/surefire-reports/screenshots/2012_07_20/FF/16_08_30.png
  //http://fusion.paypal.com/jenkins/view/QA_Marketing/job/Campaign_Studio_Regression/ws/target/surefire-reports/screenshots/2012_07_20/FF/16_08_30.png
 // http://fusion.paypal.com/jenkins/view/QA_Marketing/job/Campaign_Studio_Regression/ws/Campaign_Studio_Regression/target/surefire-reports/screenshots/2012_07_20/FF/16_53_29.png

  public static String getBrowserSize(WebDriver driver) {
    Options options = driver.manage();
    Dimension dimension = options.window().getSize();
    return "W: " + dimension.getWidth() + " H: " + dimension.getHeight();
  }

  /**
   * Polls to a max of 30 seconds for a element and clicks it.
   * 
   * @param driver
   * @param locator
   *          to locate the element.
   */
  public static void waitAndClick(final WebDriver driver, final By locator) throws RuntimeException {
    WebElement element = getElement(driver, locator);
    String text = element.getText();
    String elementName = (text.trim().length()==0 && element.getAttribute("value") != null) ? element.getAttribute("value") : text;
    element.click();
    logStep(CLICK_EVENT + SEPARATOR + elementName);
  }

  /**
   * Polls to a max of 30 seconds for a locator with attribute 'attribute' that contains 'containingString' and clicks it.
   */
  public static void waitAndClickElementContainingAttributePartly(WebDriver driver, String attribute, String containingString, By locator) throws RuntimeException {
    WebElement element = getElementsContainingAttributePartly(driver, attribute, containingString, locator);
    String text = element.getText();
    String elementName = (text.trim().length()==0) ? element.getAttribute("value") : text;
    element.click();
    logStep(CLICK_EVENT + SEPARATOR + elementName);
  }

  /**
   * Polls to a max of 30 seconds for a element and gets text from it.
   * 
   * @return String text
   * @param driver
   * @param locator
   *          to locate the element.
   */
  public static String waitAndGetText(final WebDriver driver, final By locator) {
    return getElement(driver, locator).getText();

  }

  /**
   * Gets text from the locator.
   * <p>
   * This does not poll for the element.Returns null if the element is not
   * present.
   * </p>
   * 
   * @return String text
   * @param driver
   * @param locator
   *          to locate the element.
   */
  public static String getText(final WebDriver driver, final By locator) {
    if (isElementPresent(driver, locator)) {
      return driver.findElement(locator).getText();
    
    }
    throw new RuntimeException("Element could not be located at:" + locator);
  }

  /**
   * Use waitAndGetText(final WebDriver driver, final By locator) instead. Note
   * the 'G' instead of 'g' in the method name.
   */
  @Deprecated
  public static String waitAndgetText(final WebDriver driver, final By locator) {
    return getElement(driver, locator).getText();

  }

  /**
   * Polls to a max of 30 seconds to locate the image by the specified locator
   * and gets alt from it.
   * <p>
   * Note: This methods is intended for images that has 'alt' attribute in the
   * image tag. In some cases the image seen on UI may be defined in a anchor
   * tag and the mouse over text could be defined in 'title' attribute.
   * </p>
   * 
   * @param driver
   * @param imageLocator
   * @return String the alt text from the image.
   */
  public static String getImageAltText(final WebDriver driver, final By imageLocator) {
    return getElement(driver, imageLocator).getAttribute("alt");
  }

  /**
   * Gets the text seen on UI while performing a mouse over on a element.
   * <p>
   * Note: This method is written to get the 'title' attribute from anchor tags
   * which are widely used in ebay for links and buttons and images sometimes.
   * </p>
   * 
   * @param driver
   * @param elementText
   *          Text seen on the image button or link
   * @param locator
   * @return String mouseover text seen while hovering on image button or link
   */
  public static String getMouseOverText(final WebDriver driver, final String elementText,
      final By locator) {
    for (WebElement element : getAllElementsWithSameLocator(driver, locator)) {
      if (element.getText().contains(elementText)) {
        return element.getAttribute("title");
      }
    }
    throw new RuntimeException("No hover text found for " + elementText + " identified by:"
        + locator);
  }

  /**
   * Polls to a max of 30 seconds for a element and gets alt from it.
   * 
   * @param driver
   * @param imageLocator
   * @return String the alt text from the image.
   */
  public static List<String> getImageAltTextFromAllElementsWithSameLocator(final WebDriver driver,
      final By imageLocator) {
    WaitForUtility.waitForElementToBeVisible(driver, imageLocator);
    List<WebElement> elements = driver.findElements(imageLocator);
    List<String> altValuesList = new ArrayList<String>();
    for (WebElement element : elements) {
      altValuesList.add(element.getAttribute("alt"));
    }
    return altValuesList;
  }

  /**
   * Polls to a max of 30 seconds for a element and gets alt from it.
   * 
   * @param driver
   * @param imageLocator
   * @return String the alt text from the image.
   */
  public static List<String> getImageSrcTextFromAllElementsWithSameLocator(final WebDriver driver,
      final By imageLocator) {
    WaitForUtility.waitForElementToBeVisible(driver, imageLocator);
    List<WebElement> elements = driver.findElements(imageLocator);
    List<String> srcValuesList = new ArrayList<String>();
    for (WebElement element : elements) {
      srcValuesList.add(element.getAttribute("src"));
    }
    return srcValuesList;
  }

  /**
   * Polls to a max of 30 seconds for a element and gets href value from it.
   * 
   * @param driver
   * @param linkLocator
   * @return String the href value from the link.
   */
  public static String getHrefFromLink(final WebDriver driver, final By linkLocator) {
    return getElement(driver, linkLocator).getAttribute("href");
  }

  /**
   * Polls to a max of 30 seconds for a element and gets href value from it.
   * 
   * @param driver
   * @param linkLocator
   * @return String the href value from the link.
   */
  public static List<String> getHrefFromAllElementsWithSameLocator(final WebDriver driver,
      final By linkLocator) {
    WaitForUtility.waitForElementToBeVisible(driver, linkLocator);
    List<WebElement> elements = driver.findElements(linkLocator);
    List<String> hrefValuesList = new ArrayList<String>();
    for (WebElement element : elements) {
      hrefValuesList.add(element.getAttribute("href"));
    }
    return hrefValuesList;
  }

  /**
   * Polls to a max of 30 seconds for a element and gets style value from it.
   * 
   * @param driver
   * @param linkLocator
   * @return String the href value from the link.
   */
  public static String getStyleFromElement(final WebDriver driver, final By linkLocator) {
    return getElement(driver, linkLocator).getAttribute("style");
  }

  /**
   * Polls to a max of 30 seconds for a element and gets style value from it.
   * 
   * @param driver
   * @param linkLocator
   * @return String the href value from the link.
   */
  public static List<String> getStyleFromAllElementsWithSameLocator(final WebDriver driver,
      final By linkLocator) {
    WaitForUtility.waitForElementToBeVisible(driver, linkLocator);
    List<WebElement> elements = driver.findElements(linkLocator);
    List<String> valuesList = new ArrayList<String>();
    for (WebElement element : elements) {
      valuesList.add(element.getAttribute("style"));
    }
    return valuesList;
  }

  /**
   * Polls to a max of 30 seconds for a element and gets value from it.
   * 
   * @param driver
   * @param linkLocator
   * @return String the href value from the link.
   */
  public static String getValueFromElement(final WebDriver driver, final By linkLocator) {
    return getElement(driver, linkLocator).getAttribute("value");
  }

  /**
   * Polls to a max of 30 seconds for a element and gets value from it.
   * 
   * @param driver
   * @param linkLocator
   * @return String the href value from the link.
   */
  public static List<String> getValueFromAllElementsWithSameLocator(final WebDriver driver,
      final By linkLocator) {
    WaitForUtility.waitForElementToBeVisible(driver, linkLocator);
    List<WebElement> elements = driver.findElements(linkLocator);
    List<String> valuesList = new ArrayList<String>();
    for (WebElement element : elements) {
      valuesList.add(element.getAttribute("value"));
    }
    return valuesList;
  }

  /**
   * Polls to a max of 30 seconds for a element and gets src value from it.
   * 
   * @param driver
   * @param linkLocator
   * @return String src value from the image.
   */
  public static String getSrcFromImageTag(final WebDriver driver, final By imageLocator) {
    return getElement(driver, imageLocator).getAttribute("src");
  }

  /**
   * Returns the width of the image located by specified imageLocator.
   * 
   * @param driver
   * @param imageLocator
   * @return String width of the image(eg:300px)
   */
  public static String getWidthOfImage(final WebDriver driver, final By imageLocator) {
    final String style = getElement(driver, imageLocator).getAttribute("style");
    if (!style.equals("") && style != null && !style.isEmpty()) {
      for (String string : StringHelper.getStringAsList(style, ";")) {
        if (string.contains("width:"))
          return string.replace("width:", "").trim();
      }
      throw new RuntimeException("Width not set for image located by: " + imageLocator);
    }
    return getElement(driver, imageLocator).getAttribute("width");
  }

  /**
   * Returns the height of the image located by specified imageLocator.
   * 
   * @param driver
   * @param imageLocator
   * @return String height of the image(eg:225px)
   */
  public static String getHeightOfImage(final WebDriver driver, final By imageLocator) {
    final String style = getElement(driver, imageLocator).getAttribute("style");
    if (!style.equals("") && style != null && !style.isEmpty()) {
      for (String string : StringHelper.getStringAsList(style, ";")) {
        if (string.contains("height:"))
          return string.replace("height:", "").trim();
      }
      throw new RuntimeException("Height not set for image located by: " + imageLocator);
    }
    return getElement(driver, imageLocator).getAttribute("height");
  }

  /**
   * Clicks on a element and logs the event.
   * 
   * @param webElement
   */
  public static void click(final WebElement webElement) {
    String text = webElement.getText();
    String tagName = webElement.getTagName();
    if (tagName.contains("input")) {
      tagName = webElement.getAttribute("value");
    }
    try {
      webElement.click();
      logStep(CLICK_EVENT + tagName + SEPARATOR + text);
    } catch (ElementNotVisibleException e) {
      logFailureStep("ElementNotFoundException thrown at click" + e.toString());
    } catch (StaleElementReferenceException e) {
      logFailureStep("StaleElementReferenceException thrown at " + e.toString());
    }
  }

  /**
   * Clicks on a element logs the event and returns the element back.
   * 
   * @param webElement
   * @return
   */
  @Deprecated
  public static WebElement clickAndReturnElement(final WebElement webElement) {
    String text = webElement.getText();
    String tagName = webElement.getTagName();
    if (tagName.contains("input")) {
      tagName = webElement.getAttribute("value");
    }
    try {
      webElement.click();
      logStep(CLICK_EVENT + tagName + SEPARATOR + text);
      return webElement;
    } catch (ElementNotVisibleException e) {
      logFailureStep("ElementNotVisibleException - clickAndReturnElement" + e.toString());
      return null;
    } catch (StaleElementReferenceException e) {
      logFailureStep("StaleElementReferenceException - clickAndReturnElement" + e.toString());
      return null;
    }
  }

  /**
   * Types the 'textToBeTyped' in the specified webElement and logs the event.
   * 
   * @param webElement
   * @param textToBeTyped
   */
  public static void sendKeys(final WebElement webElement, final String textToBeTyped) {
    webElement.sendKeys(textToBeTyped);
    logStep(TYPED + textToBeTyped);
  }

  /**
   * Types the 'textToBeTyped' in the specified alert and logs the event.
   * 
   * @param alert
   * @param textToBeTyped
   */
  public static void sendKeys(final Alert alert, final String textToBeTyped) {
    alert.sendKeys(textToBeTyped);
    logStep(TYPED + textToBeTyped);
  }

  /**
   * Types the 'textToBeTyped' in the specified webElement and logs the event.
   * 
   * @param webElement
   * @param textToBeTyped
   */
  public static void pollForTextBoxAndSendKeys(final WebDriver driver, final By locator,
      final String textToBeTyped) {
    getElement(driver, locator).sendKeys(textToBeTyped);
    logStep(TYPED + textToBeTyped);
  }

  /**
   * Clears the value in webElement and logs the event.
   * 
   * @param webElement
   */
  public static void clear(final WebElement webElement) {
    String text = webElement.getText();
    webElement.clear();
    logStep(CLEARED + SEPARATOR + text);
  }

  /**
   * Clears the value in locator and logs the event.
   * 
   * @param webElement
   */
  public static void clear(final WebDriver driver, final By locator) {
    WebElement element = getElement(driver, locator);
    logStep("Value in field before clearing:" + element.getAttribute("value"));
    element.clear();
    logStep(CLEARED + SEPARATOR + element.getText());
    logStep("Value in field after clearing:" + element.getAttribute("value"));
  }

  /**
   * Clears the value in webElement and logs the event.
   * 
   * @param webElement
   */
  public static void clearFieldAndSendText(final WebDriver driver, final By locator,
      final String textToBeTyped) {
    WebElement element = getElement(driver, locator);
    element.clear();
    element.click();
    pollForTextBoxAndSendKeys(driver, locator, textToBeTyped);
  }

  /**
   * Submits the form in webElement and logs the event.
   * 
   * @param webElement
   */
  public static void submit(final WebElement webElement) {
    String text = webElement.getText();
    webElement.submit();
    logStep(SUBMITTED + SEPARATOR + text);
  }

  /**
   * Hover mouse over the "By" locator identified by 'hoverOn' and polls for
   * 'locatorToFindOnPageAfterHovering' and logs the event.
   * 
   * @param hoverOn
   * @param locatorToFindOnPageAfterHovering
   * @param driver
   */
  public static void mouseOver(final By hoverOn, final By locatorToFindOnPageAfterHovering,
      final WebDriver driver) {
    String text = driver.findElement(hoverOn).getText();
    String tagName = getElement(driver, hoverOn).getTagName();

    MouseActionUtility.doMouseActionUsingJS("mouseover", hoverOn, driver);
    WaitForUtility.waitForElementToBeVisible(driver, locatorToFindOnPageAfterHovering);
    logStep(HOVERED + "<" + tagName + ">" + SEPARATOR + text + FOUND
        + locatorToFindOnPageAfterHovering);
  }

  /**
   * Hover mouse over the WebElement identified by 'hoverOnElement' and polls
   * for 'locatorToFindOnPageAfterHovering' and logs the event.
   * 
   * @param hoverOnElement
   * @param locatorToFindOnPageAfterHovering
   * @param driver
   */
  public static void mouseOver(WebElement hoverOnElement,
      final By locatorToFindOnPageAfterHovering, final WebDriver driver) {
    String text = hoverOnElement.getText();
    String tagName = hoverOnElement.getTagName();

    MouseActionUtility.doMouseActionUsingJS("mouseover", hoverOnElement, driver);
    WaitForUtility.waitForElementToBeVisible(driver, locatorToFindOnPageAfterHovering);
    logStep(HOVERED + "<" + tagName + ">" + SEPARATOR + text + FOUND
        + locatorToFindOnPageAfterHovering);
  }

  /**
   * Selects option from drop down if the select box uses select tag and options
   * uses option tags like shown. <select> <option> optionToSelect </option>
   * </select>
   * 
   * @param driver
   * @param optionToSelect
   */
  public static void selectOptionFromDropDown(WebDriver driver, By selectLocator,
      String optionToSelect) {
    WebElement select = getElement(driver, selectLocator);
    List<WebElement> options = select.findElements(By.tagName("option"));
    for (WebElement option : options) {
      if (optionToSelect.equals(option.getText())) {
        option.click();
        break;
      }
    }
  }

  /**
   * Gets all the options from the specified select box as {@link List} of
   * Strings .
   */
  public static List<String> getAllOptionsFromDropDown(WebDriver driver, By selectBoxLocator) {
    WebElement select = getElement(driver, selectBoxLocator);
    List<WebElement> options = select.findElements(By.tagName("option"));
    List<String> optionTexts = new ArrayList<String>();
    for (WebElement option : options) {
      optionTexts.add(option.getText());
    }
    return optionTexts;
  }

  /**
   * Gets all the options from the specified select box as {@link List} of
   * Strings .
   */
  public static List<String> getAllOptionsFromDropDownLocatedByName(WebDriver driver,
      String selectBoxName) {
    List<WebElement> selectOptions = getAllElementsWithSameLocator(driver, By
        .cssSelector("select[name=\"" + selectBoxName.trim() + "\"]>option"));
    List<String> allOptions = new ArrayList<String>();
    for (WebElement selectOption : selectOptions) {
      allOptions.add(selectOption.getText());
    }
    return allOptions;
  }

  /**
   * Selects option from drop down if the select box uses select tag and options
   * uses option tags and has name associated with the select box. <select>
   * <option> optionToSelect </option> </select>
   * 
   * @param driver
   * @param optionToSelect
   * @throws RuntimeException
   *           if the optionToSelect is not available.
   */
  public static void selectOptionByLocatingDropDownWithName(WebDriver driver, String selectBoxName,
      String optionToSelect) throws RuntimeException {
    List<WebElement> selectOptions = getAllElementsWithSameLocator(driver, By
        .cssSelector("select[name=\"" + selectBoxName.trim() + "\"]>option"));
    String availableOptions = "";
    for (WebElement selectOption : selectOptions) {
      availableOptions = selectOption.getText() + ", " + availableOptions;
      if (selectOption.getText().trim().equals(optionToSelect.trim())) {
        ActionUtility.logStep("Selecting option:" + optionToSelect);
        selectOption.click();
        return;
      }
    }
    throw new RuntimeException("Options: " + optionToSelect
        + " is not available, Available options are:" + availableOptions);
  }

  /**
   * Get all select box labels and their respective options in the current page,
   * as map.
   * 
   * <p>
   * <b>Note:</b> The select boxes should have name attribute for this method to
   * work. This is the case with most of the select boxes.
   * </p>
   * 
   * @return {@code Map<String, List<String>>}
   */
  public Map<String, List<String>> getSelectBoxAndOptionsOnPageAsMap(WebDriver driver) {
    List<WebElement> allSelectBoxes = getAllElementsWithSameLocator(driver, By
        .cssSelector("select"));
    Map<String, List<String>> mapOfSelectBoxAndValues = new LinkedHashMap<String, List<String>>();
    for (WebElement selectBox : allSelectBoxes) {
      String selectBoxLabel = selectBox.getAttribute("name");
      List<String> options = getTextsFromAllElementsWithSameLocator(driver, By
          .cssSelector("select[name=\"" + selectBoxLabel + "\"]>option"));
      mapOfSelectBoxAndValues.put(selectBoxLabel, options);
    }
    return mapOfSelectBoxAndValues;
  }

  /**
   * Returns selection option from drop down if the select box uses select tag
   * and options uses option tags like shown. <select> <option> optionToSelect
   * </option> </select>
   * 
   * @param driver
   * @param optionToSelect
   */
  public static WebElement getSelectedOptionFromDropDown(WebDriver driver, By selectLocator) {
    WebElement select = getElement(driver, selectLocator);
    List<WebElement> options = select.findElements(By.tagName("option"));
    for (WebElement option : options) {
      if (option.isSelected()) {
        return option;
      }
    }
    return options.get(0);
  }

  /**
   * Gets texts for all the elements sharing the same locator and return them in
   * a single string.
   * 
   * @return text of all elements sharing a locator as a String
   */
  public static String getTextForAllElementsWithSameLocator(WebDriver driver, By locator) {
    WaitForUtility.waitForElementToBeVisible(driver, locator);
    List<WebElement> elements = driver.findElements(locator);
    StringBuffer textSB = new StringBuffer();
    for (WebElement element : elements) {
      textSB.append(element.getText());
    }
    return textSB.toString();
  }

  /**
   * Gets texts for all the elements sharing the same locator and return them in
   * a {@link List}.
   * 
   * @return {@link List} of all elements sharing a locator
   */
  public static List<String> getTextsFromAllElementsWithSameLocator(WebDriver driver, By locator) {
    WaitForUtility.waitForElementToBeVisible(driver, locator);
    List<WebElement> elements = driver.findElements(locator);
    List<String> textList = new ArrayList<String>();
    for (WebElement element : elements) {
      textList.add(element.getText());
    }
    return textList;
  }

  /**
   * Waits for the locator and gets all the elements sharing same locator.
   * 
   * @return {@link List} of all elements sharing a locator
   */
  public static List<WebElement> getAllElementsWithSameLocator(WebDriver driver, By locator)
      throws RuntimeException {
    try {
      WaitForUtility.waitForElementToBeVisible(driver, locator);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }

    return driver.findElements(locator);
  }

  /**
   * Locates a element or several elements by locator(eg:xpath-> //input or css
   * -> input) and iterates through all elements then gets the attribute and
   * checks if the containingString is a part of the attribute. If so returns
   * the element.
   * 
   * <b>USAGE: getElementsContainingAttributePartly(driver, "class", "qnty",
   * LOCATOR)</b>
   */
  public static WebElement getElementsContainingAttributePartly(WebDriver driver, String attribute,
      String containingString, By locator) throws RuntimeException {
    List<WebElement> elementsContainingAttribute = getAllElementsWithSameLocator(driver, locator);
    for (WebElement webElement : elementsContainingAttribute) {
      String value = webElement.getAttribute(attribute);
      if ((value != null) && (value.length() != 0) && (value.contains(containingString))) {
        return webElement;
      }
    }
    throw new RuntimeException("None of the elements located by '" + locator + "' contained the '"
        + attribute + "' attribute having value containing '" + containingString + "'.");
  }

  /**
   * Gets the number of elements that share the same locator.
   * 
   * @return int # of all elements sharing a locator
   */
  public static int getCountForElementsWithSameLocator(WebDriver driver, By locator) {
    WaitForUtility.waitForElementToBeVisible(driver, locator);
    return driver.findElements(locator).size();
  }

  /**
   * Polls for the element for a max of 30 seconds and returns the elements if
   * it is found.
   * 
   * @return {@link List} of all elements sharing a locator
   */
  public static WebElement getElement(WebDriver driver, By locator) throws RuntimeException {
    WaitForUtility.waitForElementToBeVisible(driver, locator);
    return driver.findElement(locator);
  }

  /**
   * Logs the message to be seen from testng report.
   * 
   * @param message
   */
  public static void logStep(final String message) {
    Reporter.log(message);
  }

  /**
   * Logs the message in <b>bold</b> to be seen from testng report.
   * 
   * @param message
   */
  public static void logFailureStep(final String message) {
    Reporter.log("<b>" + message + "</b>" + "\n");
  }

  /**
   * Logs the message in <b>bold</b> to be seen from testng report.
   * 
   * @param message
   */
  public static void logPassColorStep(final String message) {
    Reporter.log("<font color='GREEN'><b>" + message + "</b></font>" + "\n");
  }
  
  /**
   * Logs the message in <b>bold</b> to be seen from testng report.
   * 
   * @param message
   */
  public static void logFailureColorStep(final String message) {
    Reporter.log("<font color='RED'><b>" + message + "</b></font>" + "\n");
  }
  
  /**
   * This method does not work anymore since, js is blocked in modern browsers
   * and it may not work for all browsers.
   * <p>
   * There is a bug logged in webdriver to build this in webdriver code base.
   * {@link http://code.google.com/p/selenium/issues/detail?id=174}
   * </p>
   * 
   * <b>Use resizeBrowserAndRefresh or resizeBrowser(driver, int, int)
   * instead.</b>
   * 
   * @param driver
   * @param windowWidth
   * @param windowHeight
   */
  @Deprecated
  public static void resizeBrowser(WebDriver driver, String windowWidth, String windowHeight) {
    ((JavascriptExecutor) driver).executeScript("window.resizeTo(" + windowWidth + ","
        + windowHeight + ");");
  }

  /**
   * Resizes the browser to the size specified and refreshes the page.
   * 
   * <p>
   * Use in cases where a refresh is needed after resizing the browser for the
   * UI to adjust.
   * <p>
   * 
   * @param driver
   * @param int windowWidth
   * @param int windowHeight
   */
//  public static void resizeBrowserAndRefresh(WebDriver driver, int windowWidth, int windowHeight) {
//    Options options = driver.manage();
//    options.window().setPosition(new Point(0, 0));
//    options.window().setSize(new Dimension(windowWidth, windowHeight));
//    driver.navigate().refresh();
//  }

  /**
   * Resizes the browser to test on various resolutions.
   * <p>
   * If you need the app to refresh after resizing, use {@code
   * resizeBrowserAndRefresh(driver, int, int)}
   * </p>
   * 
   * @param driver
   * @param int windowWidth
   * @param int windowHeight
   */
//  public static void resizeBrowser(WebDriver driver, int windowWidth, int windowHeight) {
//    Options options = driver.manage();
//    options.window().setPosition(new Point(0, 0));
//    options.window().setSize(new Dimension(windowWidth, windowHeight));
//  }

  public static WebElement clickAndVerifyElementWhenReady(final By locator,
      final By locatorToVerify, final WebDriver driver) {
    WaitForUtility.waitForElementToBeVisible(driver, locator);
    WebDriverWait wait = new WebDriverWait(driver, WAIT_TIMEOUT);
    return wait.until(new Function<WebDriver, WebElement>() {
      public WebElement apply(WebDriver driver) {
        WebElement element = driver.findElement(locator);
        element.click();
        if (element.isDisplayed()) {
          try {
            WaitForUtility.waitForElementToBeVisible(driver, locatorToVerify);
          } catch (Exception e) {
            return null;
          }
          return element;
        }
        return null;
      }
    });
  }

  /**
   * Returns true if element is present.
   * 
   * @param driver
   * @param locator
   *          to find the element
   * @return true if element is present.
   */
  public static boolean isElementPresent(WebDriver driver, By locator) {
    try {
      driver.findElement(locator);
      return true;
    } catch (Exception e) {
      ActionUtility.logStep(e.getMessage());
      return false;
    }
  }

  /**
   * Poll for the maximum timeout of 30 secs to find the element. Returns true
   * if the element is found any time on or before timeout.
   * 
   * @param driver
   * @param locator
   *          to find the element
   * @return true if element is present.
   */
  public static boolean pollForElementAndCheckIfPresent(WebDriver driver, By locator) {
    try {
      WaitForUtility.waitForElementToBeVisible(driver, locator);
      return true;
    } catch (Exception e) {
      return false;
    }
  }

  /**
   * Returns true if element is present.Polls for the specified timeout.
   * 
   * @param driver
   *          {@link WebDriver}
   * @param locator
   *          {@link By}
   * @param timeOutInSeconds
   *          {@link Long}
   * @return
   */
  public static boolean pollForElementAndCheckIfPresent(WebDriver driver, By locator,
      long timeOutInSeconds) {
    try {
      WaitForUtility.waitForElementToBeVisible(driver, locator, timeOutInSeconds);
      return true;
    } catch (Exception e) {
      return false;
    }
  }

  /**
   * Enters text on a prompt box and accepts it.
   * 
   * @param driver
   * @param textToBeTyped
   */
  public static void enterTextOnPromptAndAccept(WebDriver driver, String textToBeTyped) {
    Alert prompt = driver.switchTo().alert();
    logStep("Prompt Text:" + prompt.getText());
    sendKeys(prompt, textToBeTyped);
    prompt.accept();
  }

  /**
   * Accepts/Dismisses the confirmation alert.
   * 
   * @param driver
   * @param boolean accepts the alert if true and dismisses the alert if false
   */
  public static void acceptOnConfirmationBox(WebDriver driver, boolean accept) {
    Alert confirm = driver.switchTo().alert();
    logStep(confirm.getText());
    if (accept) {
      confirm.accept();
      logStep("Accepted alert.");
    } else {
      confirm.dismiss();
      logStep("Dismissed alert.");
    }
  }

  /**
   * Scrolls the page to the specifed pixel position in xAxis and yAxis.
   * <p>
   * <b>USAGE:</b><br>
   * 1.To scroll down to a position at pixel 1000 : {@code scrollPage(driver, 0,
   * 1000)}<br>
   * 2.To scroll right to a position at pixel 500 : {@code scrollPage(driver,
   * 500, 0)}<br>
   * 3.To scroll right to a position at pixel 500 and scroll down to a position
   * at pixel 2500 : {@code scrollPage(driver, 500, 2500)}
   * </p>
   * 
   * @param driver
   * @param xAxis
   *          String
   * @param yAxis
   *          String
   */
  public static void scrollPage(WebDriver driver, String xAxis, String yAxis) {
    ((JavascriptExecutor) driver).executeScript("window.scrollBy(" + xAxis + "," + yAxis + ")");
  }

  /**
   * Scroll back to top of the page
   * 
   * @param driver
   * @param xAxis
   * @param yAxis
   * @return
   */
  public static Long scrollTop(WebDriver driver) {
    return (Long) ((JavascriptExecutor) driver)
        .executeScript("return document.documentElement.scrollTop");
  }

  /**
   * Click scrollbar like slider for product configurations
   * 
   * @author haiyang
   * 
   * @param driver
   * @param width
   * @param sliderBarMemory
   */
  public static void clickScrollBar(WebDriver driver, int width, WebElement sliderBarMemory) {
    // "- 10 , 5" is offset args to make sure the mouse point is on the
    // sliderBar.
    ((HasInputDevices) driver).getMouse().mouseMove(((Locatable) sliderBarMemory).getCoordinates(),
        width - 10, 5);
    ((HasInputDevices) driver).getMouse().click(null);
  }

  /**
   * Maximizes the window to the available screen width.
   * 
   * @param driver
   */
  public static void maximizeWindow(WebDriver driver) {
    ((JavascriptExecutor) driver).executeScript("if (window.screen){window.moveTo(0, 0);"
        + "window.resizeTo(window.screen.availWidth,window.screen.availHeight);}");
  }

  /**
   * Method to check if element is rendered TODO(Sumathi): Remove this method.
   */
  @Deprecated
  public static boolean isElementRendered(final WebDriver driver, final By locator, String linkName) {
    try {

      Wait<WebDriver> wait = new WebDriverWait(driver, WAIT_TIMEOUT);
      wait.until(new ExpectedCondition<WebElement>() {
        public WebElement apply(WebDriver driver) {
          WebElement element = driver.findElement(locator);
          return element.isDisplayed() ? element : null;
        }
      });
      return true;
    } catch (Exception e) {
      logFailureStep(driver.getCurrentUrl() + linkName
          + " is not rendered properly.Please take a look - the new one");
      return false;
    }
  }

  /**
   * Method to validate and click the leftnav links in DCP and return h1 tag
   * TODO(Sumathi): Remove this method.
   */
  @Deprecated
  public static WebElement findXPathIsValid(String id, WebDriver driver) {

    try {
      WebElement element = null;
      element = driver.findElement(By.xpath(id));
      if (element != null) {
        element.click();
        return element;
      }

    } catch (Exception e) {
      logFailureStep("Verifyh1Text" + e.toString());
      return null;
    }

    return null;
  }

  /**
   * Polls to a max of 30 seconds for a element and gets the attribute from the
   * parameter
   * 
   * @param driver
   * @param locator
   * @param attribute
   * @return List of values from the attribute
   */
  public static List<String> getAttributeFromAllElementsWithSameLocator(final WebDriver driver,
      final By locator, String attribute) {
    WaitForUtility.waitForElementToBeVisible(driver, locator);
    List<WebElement> elements = driver.findElements(locator);
    List<String> attributeList = new ArrayList<String>();
    for (WebElement element : elements) {
      attributeList.add(element.getAttribute(attribute));
    }
    return attributeList;
  }

  /**
   * Polls to a max of 30 seconds for a element and gets the attribute from the parameter
   *
   * @param driver
   * @param locator
   * @param attribute
   * @return value of attribute
   */
  public static String getAttributeFromElement(final WebDriver driver,
      final By locator, String attribute) {
    WaitForUtility.waitForElementToBeVisible(driver, locator);
    WebElement element = driver.findElement(locator); 
    return element.getAttribute(attribute);
  }

  /**
   * Returns the X location of the element.
   * 
   * @param driver
   * @param locator
   * @return
   */
  public static int getElementPositionX(WebDriver driver, By locator) {
    WaitForUtility.waitForElementToBeVisible(driver, locator);
    WebElement element = driver.findElement(locator);
    return element.getLocation().getX();
  }

  /**
   * Returns the Y position of the element.
   * 
   * @param driver
   * @param locator
   * @return
   */
  public static int getElementPositionY(WebDriver driver, By locator) {
    WaitForUtility.waitForElementToBeVisible(driver, locator);
    WebElement element = driver.findElement(locator);
    return element.getLocation().getY();
  }
  
  /**
   * return screen resolution width
   * 
   * @param driver
   * @param xAxis
   * @param yAxis
   * @return
   */
  public static Long getScreenResolutionWidth(WebDriver driver) {
    return (Long) ((JavascriptExecutor) driver).executeScript("return screen.width");
  }

  /**
   * return screen resolution height
   * 
   * @param driver
   * @param xAxis
   * @param yAxis
   * @return
   */
  public static Long getScreenResolutionHeifht(WebDriver driver) {
    return (Long) ((JavascriptExecutor) driver).executeScript("return screen.height");
  }

  
  public static void selectOptionFromDropDown(WebDriver driver, WebElement webElement, String optionToSelect) throws InterruptedException {
    
    WaitForUtility.waitForElementToBeVisible(driver, webElement);
    WaitForUtility.waitForSeconds(driver, 30);
    boolean isOptionFound = false;
    WebElement select = webElement;
    
    List<WebElement> options = select.findElements(By.tagName("option"));
    for (WebElement option : options) {
      if (optionToSelect.equalsIgnoreCase(option.getText())) {
        try {
          
          if(select.isEnabled() || select.isDisplayed()) {                   
              option.click(); 
              logStep(SELECT_EVENT + SEPARATOR + optionToSelect);
              isOptionFound = true;
              break;  
          }
          else {
            logStep(SELECT_EVENT + SEPARATOR + optionToSelect + "is not enabled yet");
          }
          
        }
        catch (ElementNotVisibleException e) {
          logFailureStep("ElementNotFoundException thrown at click" + e.toString());
        } catch (StaleElementReferenceException e) {
          logFailureStep("StaleElementReferenceException thrown at " + e.toString());
        }
      }
    }
    if (!isOptionFound)
      throw new RuntimeException(optionToSelect + " not found in dropdown");
  }
  
  /**
   * Clicks on a element and logs the event.
   * 
   * @param webElement
   */
  public static void click1(final WebElement webElement) {
    String text = webElement.getText();
    String tagName = webElement.getTagName();
    if (tagName.contains("input")) {
      tagName = webElement.getAttribute("value");
    }
    try {
      webElement.click();
      logStep(CLICK_EVENT + tagName + SEPARATOR + text);
    } catch (ElementNotVisibleException e) {
      logFailureStep("ElementNotFoundException thrown at click" + e.toString());
    } catch (StaleElementReferenceException e) {
      logFailureStep("StaleElementReferenceException thrown at " + e.toString());
    }
  }

}
