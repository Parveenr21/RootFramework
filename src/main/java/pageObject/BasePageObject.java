package pageObject;

import java.util.HashSet;
import java.util.Set;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.LoadableComponent;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;

import baseWebDriver.LocatorSwitch;
import baseWebDriver.LocatorType;

/**
 * Class to be extended by pageobjects.
 * <p>1.Sets driver to page.<br>
 * 2.Sets url.<br>
 * 3.Loads the page.<br>
 * 4.Inits the elements in the page.Helps in {@link FindBy} elements and also locators {@link By}<br>
 * 5.Detects application framework (RAPTOR or CLASSIC) where the tests are to be run on
 *  and decides which elements to load with respect to the application framework.</p>
 * 
 *
 */
public abstract class BasePageObject extends LoadableComponent<BasePageObject> {

  private static final long TIMEOUT_IN_SECONDS = 10;
  protected WebDriver driver = null;
  protected String url = null;
  protected String site = null;
  protected boolean isAbsoluteUrl = false;
  protected Set<By> loadedConditions = new HashSet<By>();
  private LocatorSwitch locatorSwitch = null;

  /**
   * Sets driver and intialized page elements.
   * 
   * @param driver
   */
  protected BasePageObject(WebDriver driver) {
    if (driver == null)
      throw new RuntimeException("Failed due to Driver/Grid Issues.");
    this.driver = driver;
    PageFactory.initElements(this.driver, this);
    setLocators();
  }

  /**
   * Sets driver, url.Loads url and initializes elements on the page.
   * 
   * @param driver
   * @param page
   */
  protected BasePageObject(WebDriver driver, String url) {
    this.driver = driver;
    this.url = url;
    load();
    PageFactory.initElements(this.driver, this);
  }

  /**
   * Sets driver, url and
   * 
   * @param driver
   * @param url
   * @param isAbsoluteUrl
   *          TODO: Check the usage of this method and remove it if possible.
   */
  protected BasePageObject(WebDriver driver, String url, boolean isAbsoluteUrl) {
    this.driver = driver;
    this.url = url;
    this.isAbsoluteUrl = isAbsoluteUrl;
    load();
    PageFactory.initElements(this.driver, this);
  }

  /**
   * Add a constraint that must be met in order for the page to be considered
   * loaded. This should be used by implementing page objects in a domain
   * specific manner.
   * 
   * @param matchCondition
   *          .
   */
  protected void addLoadedCondition(By matchCondition) {
    loadedConditions.add(matchCondition);
  }

  @Override
  protected void load() {
    if (driver == null)
      throw new RuntimeException("Failed due to Driver/Grid Issues. "
          + System.getProperty("REMOTE_URL"));
    try {
      driver.get(url);
    } catch (Exception e) {
      throw new RuntimeException("Failed to open the url on browser: " + e);
    }
    setLocators();
  }

  @Override
  protected void isLoaded() throws Error {
    for (final By condition : loadedConditions) {
      Wait<WebDriver> wait = new WebDriverWait(driver, TIMEOUT_IN_SECONDS);
      wait.until(new ExpectedCondition<Boolean>() {
        public Boolean apply(WebDriver driver) {
          driver.findElement(condition);
          return Boolean.valueOf(true);
        }
      });
    }
  }

  protected void setLocators() {
  }

  protected void setLocator(LocatorType pageType, String elementName, By locator) {
    getLocatorSwitchInstance().setLocator(pageType, elementName, locator);
  }

  /**
   * Returns the {@link By} locator for the given elementString.
   */
  protected By getLocator(String elementString) {
    return getLocatorSwitchInstance().getLocator(elementString);
  }

  public LocatorSwitch getLocatorSwitchInstance(){
    if (locatorSwitch == null) {
      locatorSwitch = new LocatorSwitch();
    }
    return locatorSwitch;
  }
  
}