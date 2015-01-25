package site1;

import java.util.Random;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import Utility.ActionUtility;
import Utility.WaitForUtility;
import pageObject.BasePageObject;


public class SiteBasePageObject extends BasePageObject{

	protected SiteBasePageObject(WebDriver driver, String url,
			boolean isAbsoluteUrl) {
		super(driver, url, isAbsoluteUrl);
	}

	protected SiteBasePageObject(WebDriver driver, String url) {
		super(driver, url);
	}

	protected SiteBasePageObject(WebDriver driver) {
		super(driver);
	}

	/**
	 * Accept Alert
	 */
	public void acceptAlert() {

		driver.switchTo().alert().accept();
	}

	/**
	 * Accept Alert
	 */
	public void dismissAlert() {

		driver.switchTo().alert().dismiss();
	}

	/**
	 * Return Alert Popup Text.
	 * @return
	 */
	public String getAlertMessage() {

		String alertMessage = driver.switchTo().alert().getText();
		return alertMessage;
	}

	/**
	 * return Page title
	 * @return
	 */
	public String getPageTitle() {

		String pagetitle = driver.getTitle();
		return pagetitle;
	}

	/**
	 * Function to return Suceess or Error Message on all campaign pages.
	 * @return
	 */
	public String getSuccessOrErrorMessage() {

		Utility.WaitForUtility.waitForSeconds(driver, 50);
		Utility.WaitForUtility.waitForElementToBeVisible(driver, By.xpath("//*[@class='messageBox confirmation' or @class='messageBox error']"));
		Utility.ActionUtility.captureScreenShot(driver);        
		return driver.findElement(By.xpath("//*[@class='messageBox confirmation' or @class='messageBox error']//p")).getText();
	}

	public String logOfferSuccessMessage() {
		String message = driver.findElement(By.xpath("//*[@class='messageBox confirmation' or @class='messageBox error']//p")).getText();
		ActionUtility.logStep("**************************************************************");
		ActionUtility.logPassColorStep(message);
		ActionUtility.logStep("**************************************************************");
		System.out.println(message);
		return message;
	}

	/**
	 * Function to return Suceess or Error Message on all campaign pages.
	 * @return
	 */
	public String getFailureMessage() {

		WaitForUtility.waitForSeconds(driver, 30);
		WaitForUtility.waitForElementToBeVisible(driver, By.xpath("//div[@id='failure']"));
		ActionUtility.captureScreenShot(driver);        
		return driver.findElement(By.xpath("//div[@id='failure']//p")).getText();
	}

	public int randomNumber(){
		Random rnd = new Random();
		int number = 100000 + rnd.nextInt(900000);
		return number;
	}
}
