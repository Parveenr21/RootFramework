package baseWebDriver;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.openqa.selenium.By;

/**
 * Switch for various application frameworks where tests can run.
 * 
 */
public class LocatorSwitch {

  private LocatorType appFrameworkType = null;
  private final Map<String, By> LOCATOR_MAP = new HashMap<String, By>();
  private boolean isAppFrameworkValidationComplete = false;

  public LocatorType getAppFrameworkType() {
    return appFrameworkType;
  }

  private void setAppFrameworkType(LocatorType appFrameworkType) {
    this.appFrameworkType = appFrameworkType;
  }

  public void setLocator(LocatorType pageType, String elementName, By locator) {
    validateAndAssignPageType();
    LOCATOR_MAP.put(pageType.toString() + elementName, locator);
  }

  /**
   * Returns the {@link By} locator for the given elementString.
   */
  public By getLocator(String elementString) {
        By locator = grabLocatorFromLocatorMap(elementString, LocatorType.COMMON_LOCATOR);
        if (locator == null) {
            locator = grabLocatorFromLocatorMap(elementString, getAppFrameworkType());
        }
        return locator;
  }
  
  private By grabLocatorFromLocatorMap(String elementString, LocatorType pageType) {
    final Iterator iterator = LOCATOR_MAP.keySet().iterator();
    // Iterate through the LOCATOR_MAP and try to find the elementString based
    // on the pagetype
    while (iterator.hasNext()) {
      final String key = iterator.next().toString();
      if (key.equals(pageType.toString() + elementString)) {
        return LOCATOR_MAP.get(key);
      }
    }
    // If the locator cannot be found in the LOCATOR_MAP check if the PageType
    // is COMMON_LOCATOR and return null.
    if (pageType.equals(LocatorType.COMMON_LOCATOR))
      return null;
    // If the locator cannot be found in the LOCATOR_MAP and if it is not a
    // COMMON_LOCATOR,
    // then the locator is not found.So throw a RuntimeException saying so.
    throw new RuntimeException("Could not find Locators for '" + elementString
        + "' in:" + pageType.toString()
        + ". Please verify if the locator is set in "
        + new Throwable().fillInStackTrace().getStackTrace()[3].getClassName());
  }

  /**
   * Validates the APP_FRAMEWORK flag for tests.
   */
  private void validateAndAssignPageType() {
    String pageTypeFromSystemProperty = null;
    if (!isAppFrameworkValidationComplete) {
      pageTypeFromSystemProperty = System.getProperty("APPLICATION_FRAMEWORK");
      for (LocatorType pageType : LocatorType.values()) {
        if (pageType.toString().equalsIgnoreCase(pageTypeFromSystemProperty)) {
          setAppFrameworkType(pageType);
          isAppFrameworkValidationComplete = true;
          return;
        }
      }
      isAppFrameworkValidationComplete = true;
      throw new RuntimeException(
          "Invalid APPLICATION_FRAMEWORK code:"
              + pageTypeFromSystemProperty
              + ". Supported FRAMEWORK codes are: CLASSIC, RAPTOR OR BETA. " 
              + "Did you use setLocator and forgot to specify the APPLICATION_FRAMEWORK.");
    }
  }
  
  public static void main(String[] a){
    new LocatorSwitch().validateAndAssignPageType();
  }
}
