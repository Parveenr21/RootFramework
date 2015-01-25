package baseWebDriver;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.Augmenter;
import org.testng.Assert;
import org.testng.Reporter;

/**
 * Class to help the tests use softasserts and still continue to have
 *  multiple assertions and gain speed in test excecution.
 *
 */
public class SoftAssertor {
  
  private List<String> softAssertFailures = new ArrayList<String>();
  
  
  /**
   * Soft assert that logs the failure reason and returns false when condition is not met.
   */
  public boolean softAssert(boolean condition , String failureMessage){
    if(!condition){
      softAssertFailures.add(failureMessage);
    }
    return condition;
  }
  /**
   * Soft assert that logs the failure reason and returns false when condition is not met.
   */
  public boolean softAssert(boolean condition , String failureMessage, WebDriver driver){
    if (!condition) {
      softAssertFailures.add(failureMessage);
      captureScreenShot(driver);
    }
    return condition;
  }

  public void logSoftAssertFailures(){
    StringBuffer failures = new StringBuffer("\n");
    if (softAssertFailures.size() != 0) {
      int counter = 1;
      for (String failure : softAssertFailures) {
        failures.append(counter + ") " + failure + "\n");
        counter ++;
      }
      Assert.fail(failures.toString());
    }
  }

  /**
   * Captures the screenshot of the current page when the SCREENSHOT system
   * property is set to 'on'.
   * eg:SCREENSHOT=ON in bat and your pom.xml should have the SCREENSHOT property defined.
   * <p>This method is designed to work both on RemoteWebDriver and local driver.</p>
   *
   * @param driver
   */
  private void captureScreenShot(WebDriver driver) {
    if ("on".equalsIgnoreCase(System.getProperty("SCREENSHOT"))) {
      try {
        //If the driver is RemoteWebDriver then augment it to enable screen shots on it.
        if (driver.getClass().getName().contains("RemoteWebDriver")) {
          driver = new Augmenter().augment(driver);
        }
        final DateFormat df = new SimpleDateFormat("HH_mm_ss");
        final DateFormat dfFolder = new SimpleDateFormat("yyyy_MM_dd");
        File scrFile = ((TakesScreenshot) driver)
            .getScreenshotAs(OutputType.FILE);
        String browser = System.getProperty("BROWSER");
        String fileName = "target/surefire-reports/screenshots/"
            + dfFolder.format(new Date()) + "/" + browser + "/"
            + df.format(new Date()) + ".png";
        FileUtils.copyFile(scrFile, new File(fileName));
        String currentDir = new File(".").getAbsolutePath().replace(".", "");
        Reporter.log("Screen shot taken and placed at: "
            + currentDir + fileName.replace("/", "\\"));
        Reporter.log("<b><a href='file:///" + (currentDir + fileName).replace("\\", "/") +
            "' onclick=\"window.open('file:///" + (currentDir + fileName).replace("\\", "/") +
            "','popup','" +
            "width=800,height=1500,toolbar=no,directories=no,location=no," +
            "menubar=no,status=no,left=0,top=0'); return false\">" + "Click to see screen shot." + "</a></b>");
      } catch (Exception e) {
        Reporter.log("<b>Could not capture screenshot due to:</b>" + e);
      }
    } else {
      Reporter.log("Screenshot not enabled." +
          "set flag SCREENSHOT=on to capture screenshot.");
    }
  }

}
