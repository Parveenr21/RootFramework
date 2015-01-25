package Utility;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;



import static Utility.ActionUtility.logStep;

public class VerifyUtility {

	public static boolean verifySourcePresent(WebDriver driver, String sExpectedValue) {
		String sSource = driver.getPageSource();
		String sEvent = "";
		if (sSource.indexOf(sExpectedValue) != -1) {
			sEvent = "Passed: " + sExpectedValue + " is Present in Source";
			logStep(sEvent);
			return true;
		} else {
			sEvent = "{FAILED} | Expected Result: " + sExpectedValue
					+ " should be Present in Source";
			logStep(sEvent);
			return false;
		}
	}

	public static boolean verifySourceNotPresent(WebDriver driver, String sExpectedValue) {
		String sSource = "";
		sSource = driver.getPageSource();
		String sEvent = "";
		if (sSource.indexOf(sExpectedValue) == -1) {
			sEvent = "Passed: " + sExpectedValue + " is NOT Present in Source";
			logStep(sEvent);
			return true;
		} else {
			sEvent = "{FAILED} | Expected Result: " + sExpectedValue
					+ " should not be Present in Source";
			logStep(sEvent);
			return false;
		}
	}

	public static boolean verifySourcePresent(String sSource, String sExpectedValue) {
		String sEvent = "";
		if (sSource.indexOf(sExpectedValue) != -1) {
			sEvent = "Passed: " + sExpectedValue + " is Present in Source";
			logStep(sEvent);
			return true;
		} else {
			sEvent = "{FAILED} | Expected Result: " + sExpectedValue
					+ " should be Present in Source";
			logStep(sEvent);
			return false;
		}
	}

	public static void verifySourceNotPresent(String sSource, String sExpectedValue) {
		String sEvent = "";
		if (sSource.indexOf(sExpectedValue) == -1) {
			sEvent = "Passed: " + sExpectedValue + " is NOT Present in Source";
			logStep(sEvent);
		} else {
			sEvent = "{FAILED} | Expected Result: " + sExpectedValue
					+ " should not be Present in Source";
			logStep(sEvent);
		}
	}

	public static void verifyLocationPresent(WebDriver driver, String sExpectedLocation, String sComment) {
		String sLocation = driver.getCurrentUrl();
		String sEvent = "";
		if (sLocation.indexOf(sExpectedLocation) != -1) {
			sEvent = "Passed: " + sExpectedLocation + " is Present in URL: "
					+ sLocation;
			logStep(sEvent);
		} else {
			sEvent = "{FAILED} " + sComment + " | Expected Result: "
					+ sExpectedLocation + " should be Present in URL:" + sLocation;
			logStep(sEvent);
		}
	}

	public static void verifyLocationNotPresent(WebDriver driver, String sExpectedLocation,
			String sComment) {
		String sLocation = driver.getCurrentUrl();
		String sEvent = "";
		if (sLocation.indexOf(sExpectedLocation) == -1) {
			sEvent = "Passed: " + sExpectedLocation
					+ " is Not Present in URL: " + sLocation;
			logStep(sEvent);
		} else {
			sEvent = "{FAILED} " + sComment + " | Expected Result: "
					+ sExpectedLocation + " is Present in URL:" + sLocation;
			logStep(sEvent);
		}
	}

	public static void verifyLocationExact(WebDriver driver, String sExpectedLocation, String sComment) {
		String sLocation = driver.getCurrentUrl();
		String sEvent = "";
		if (sLocation.equalsIgnoreCase(sExpectedLocation)) {
			sEvent = "Passed: " + sExpectedLocation + " URL is Present";
			logStep(sEvent);
		} else {
			sEvent = "{FAILED} " + sComment + " | Expected Result: "
					+ sExpectedLocation
					+ " URL should be Present | Actual Result: " + sLocation;
			logStep(sEvent);
		}
	}

	public static void verifyLocationNotExact(WebDriver driver, String sExpectedLocation,
			String sComment) {
		String sLocation = driver.getCurrentUrl();
		String sEvent = "";
		if (!sLocation.equalsIgnoreCase(sExpectedLocation)) {
			sEvent = "Passed: " + sExpectedLocation
					+ " URL is NOT Present | Actual URL: " + sLocation;
			logStep(sEvent);
		} else {
			sEvent = "{FAILED} " + sComment + " | Expected Result: "
					+ sExpectedLocation + " URL should not Present";
			logStep(sEvent);
		}
	}

	public static void isEnabled(WebElement eElement, String sComment) {
		String sEvent = "";
		try {
			if (eElement.isEnabled()) {
				sEvent = sComment + " is Enabled";
				logStep(sEvent);
			} else {
				sEvent = sComment + " is not Enabled";
				logStep(sEvent);
			}
		} catch (Exception e) {
			sEvent = "Not able to find the editable Element: " + sComment;
			System.out.println(e);
			logStep(sEvent);
		}
	}

	public static void isSelected(WebElement eElement, String sComment) {
		String sEvent = "";
		try {
			if (eElement.isSelected()) {
				sEvent = sComment + " is Selected";
				logStep(sEvent);
			} else {
				sEvent = sComment + " is not Selected";
				logStep(sEvent);
			}
		} catch (Exception e) {
			sEvent = "Not able to find the editable Element: " + sComment;
			System.out.println(e);
			logStep(sEvent);
		}
	}

	public static void isVisible(WebElement eElement, String sComment) {
		String sEvent = "";
		try {
			if (eElement.isDisplayed()) {
				sEvent = sComment + " is Visible";
				logStep(sEvent);
			} else {
				sEvent = sComment + " is not Visible";
				logStep(sEvent);
			}
			System.out.println(sEvent);
		} catch (Exception e) {
			sEvent = "Not able to find the Element: " + sComment;
			System.out.println(e);
			logStep(sEvent);
		}
	}

	public static void compareText(String sExpectedValue, String sActualValue, String sComment) {
		String sEvent = "";
		if (sExpectedValue.equalsIgnoreCase(sActualValue)) {
			sEvent = "Passed: " + sComment + " is " + sExpectedValue;
			logStep(sEvent);
		} else {
			sEvent = "{FAILED} " + sComment + " | Expected Result: "
					+ sExpectedValue + " Actual Result: " + sActualValue;
			logStep(sEvent);
		}
	}
	
		
public static boolean verifyOptionPresent(WebElement webElement, String optionToCheck) {
    
  boolean isOptionFound = false;
    List<WebElement> options = webElement.findElements(By.tagName("option"));
    for (WebElement option : options) {
      if (optionToCheck.equals(option.getText())) {
        logStep("Passed: " + optionToCheck + " is Present in dropdown: ");
        isOptionFound = true;
        break;
        }
        
     } 
    return isOptionFound;
  }

}