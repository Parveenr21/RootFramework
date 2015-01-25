package baseWebDriver;

import static baseWebDriver.BrowserType.getBrowserType;
import static baseWebDriver.Capability.getCapabilities;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.io.IOUtils;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.Reporter;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;

import baseWebDriver.BrowserType.Browser;

/**
 * BaseTest for all tests that includes the call to create and close the
 * WebDriver instance. Uses {@link ThreadLocal} to store and pass along the
 * WebDriver instance.
 * 
 */
public class BaseTest {

	private final String REMOTE_URL = "REMOTE_URL";
	private final String BACKUP_GRID_URL = "BACKUP_GRID_URL";
	private final String HTTP = "http://";
	private final String BROWSER_PROFILE_NAME = "BROWSER_PROFILE_NAME";
	private final String PROXY = "PROXY";
	private final String PROXY_HOST = "PROXY_HOST";
	private final int WAIT_SECS = 2;
	private final int MAX_NO_RETRY = 5;
	private final String mainGridUrl = System.getProperty(REMOTE_URL);
	private final String backUpGridUrl = System.getProperty(BACKUP_GRID_URL);
	private ChromeDriverService service;
	private File chromeDriver;
	protected ThreadLocal<WebDriver> driverThread = new ThreadLocal<WebDriver>();

	@BeforeClass(groups = { TestGroups.BVT, TestGroups.DATA_TEST, TestGroups.DEV,
			TestGroups.FEATURE, TestGroups.LARGE_TEST, TestGroups.MONITOR,
			TestGroups.RAPTOR_TEST, TestGroups.REGRESSION, TestGroups.BETA })
	public void startChromeServiceIfBrowserIsChrome() throws IOException{
		if(getBrowserType().equals(Browser.CHROME) && !isRemoteWebDriverRequested(getRemoteUrl())){
			chromeDriver = new File("chromedriver.exe");
			if(!chromeDriver.exists()){	
				InputStream is = BaseTest.class.getResourceAsStream("chromedriver.exe");
				OutputStream out=new FileOutputStream(chromeDriver);
				IOUtils.copy(is, out);
				out.close();
				is.close();
			}
			service = new ChromeDriverService.Builder()
			.usingDriverExecutable(chromeDriver)
			.usingAnyFreePort()
			.build();
			service.start();
			logInConsoleAndReport("Started Chrome Service.");
		}
	}

	@AfterClass(groups = { TestGroups.BVT, TestGroups.DATA_TEST, TestGroups.DEV,
      TestGroups.FEATURE, TestGroups.LARGE_TEST, TestGroups.MONITOR,
      TestGroups.RAPTOR_TEST, TestGroups.REGRESSION, TestGroups.BETA })
	public void stopChromeServiceIfBrowserIsChrome(){
		if(getBrowserType().equals(Browser.CHROME) && !isRemoteWebDriverRequested(getRemoteUrl())){
			service.stop();
			logInConsoleAndReport("Stopping Chrome Service.");
		}
	}

	/**
	 * Runs before every test method annotated with @Test and that are within a
	 * class that extends {@link BaseTest} .
	 * <p>
	 * This method gets the {@link WebDriver} instance and sets it in
	 * {@link ThreadLocal}.
	 * </p>
	 */
	@BeforeMethod(groups = { TestGroups.BVT, TestGroups.DATA_TEST, TestGroups.DEV,
      TestGroups.FEATURE, TestGroups.LARGE_TEST, TestGroups.MONITOR,
      TestGroups.RAPTOR_TEST, TestGroups.REGRESSION, TestGroups.BETA })
	public void before() {
		String driverFailReason = "";
		int initialAttemptCount = 1;
		WebDriver driver = null;
		driver = getDriverWithRetryEnabled(initialAttemptCount, false);
		if (driver == null) {
			if(isRemoteWebDriverRequested(backUpGridUrl)){
				logInConsoleAndReport("Running test on backup grid:" + backUpGridUrl);
				try {
				  driver = getDriverWithRetryEnabled(initialAttemptCount, true);
				} catch (Exception e) {
					logInConsoleAndReport("ATTEMPT TO RUN ON BACK UP GRID FAILED TOO :" + e.getMessage() + " Reason: " + driverFailReason);
				}

			}
		}
		driverThread.set(driver);
	}
	
	/**
	 * Obtains driver from local if REMOTE_URL is not specified.If the local driver/mail grid fails,
	 *  then tries to get driver from back up grid.All effort to get driver has retry enabled.
	 *  
	 * @param retryCount
	 * @param isBackUpGridRequested
	 * @return
	 */
	private WebDriver getDriverWithRetryEnabled(int retryCount, boolean isBackUpGridRequested) {
	  WebDriver driver = null;
	  String gridUrl = "Local Driver";
	  if (mainGridUrl!= null && mainGridUrl.trim().length()!=0) {
	    gridUrl = (isBackUpGridRequested) ? backUpGridUrl : mainGridUrl;
	  }
	   
	  while (retryCount != MAX_NO_RETRY) {
      try {
        if(isBackUpGridRequested) {
          driver = getRemoteWebDriver(backUpGridUrl);
        } else{
          driver = getWebDriver();
        }
        
        if (driver == null) {
          waitForSeconds(WAIT_SECS);
          retryCount++;
        } else {
          retryCount = MAX_NO_RETRY;
        }
      } catch (Exception e) {
        logInConsoleAndReport("Attempt No " + retryCount + " to create driver failed. - " + gridUrl);
        logInConsoleAndReport("Driver fail reason :" + e);
        waitForSeconds(WAIT_SECS);
        retryCount++;
      }
	  }
	  return driver;
	}

	/**
	 * Runs after every test method annotated with @Test and that are within a
	 * class that extends {@link BaseTest}.
	 * <p>
	 * This method gets the {@link WebDriver} instance from {@link ThreadLocal}
	 * and kills it.
	 * </p>
	 */
	@AfterMethod(groups = { TestGroups.BVT, TestGroups.DATA_TEST, TestGroups.DEV,
      TestGroups.FEATURE, TestGroups.LARGE_TEST, TestGroups.MONITOR,
      TestGroups.RAPTOR_TEST, TestGroups.REGRESSION, TestGroups.BETA })
	public void after() {
		String driverQuitFailReason = "";
		int retryCount = 1;
		WebDriver driver = null;
		while (retryCount != MAX_NO_RETRY) {
			if (driverThread != null) {
				driver = driverThread.get();
			}
			try {
				if (driver != null) {
					driver.quit();
					driverThread.remove();
					retryCount = MAX_NO_RETRY;
				} else {
					logInConsoleAndReport(
							"Driver quit failed permanently since driver"
							+ " is null or driver was never instantiated.");
					retryCount = MAX_NO_RETRY;
				}
			} catch (Exception e) {
				System.out.println("Attempt No " + retryCount + " to quit driver failed.");
				System.out.println("Driver quit fail reason :" + e);
				driverQuitFailReason = e.toString();
				if (retryCount == MAX_NO_RETRY)
					logInConsoleAndReport("Driver quit failed after " + MAX_NO_RETRY
							+ " attempts." + driverQuitFailReason);
				waitForSeconds(WAIT_SECS);
				retryCount++;
			}
		}
	}

	/**
	 * Returns Remote WebDriver if the REMOTE_URL is valid, otherwise a local
	 * webdriver. is returned based on the browser type.
	 * 
	 */
	private WebDriver getWebDriver() throws RuntimeException {
		String remoteUrl = getRemoteUrl();
		return isRemoteWebDriverRequested(remoteUrl) ? getRemoteWebDriver(remoteUrl)
				: getLocalWebDriver();
	}

	private String getRemoteUrl(){
		return mainGridUrl;
	}

	/**
	 * Returns a {@link RemoteWebDriver} instance.The browser type is used to
	 * select the DesiredCapabilities.
	 * 
	 * @param remoteUrl
	 *          REMOTE_URL set as system properties in pom.xml or CI param.
	 * @throws RuntimeException
	 */
	private RemoteWebDriver getRemoteWebDriver(String remoteUrl)
	throws RuntimeException {
		try {
			DesiredCapabilities capabilities = getCapabilities();
			capabilities.setJavascriptEnabled(true);
			URL url = new URL(remoteUrl);
			return new RemoteWebDriver(url, capabilities);
		} catch (MalformedURLException urlException) {
			throw new RuntimeException("Error converting " + remoteUrl + " to URL: "
					+ urlException);
		} catch (Exception e) {
			throw new RuntimeException("Failed while instantiating remote "
					+ "webdriver... " + e);
		}
	}

	/**
	 * Returns the {@link WebDriver} instance based on Browser.
	 * 
	 * @throws RuntimeException
	 */
	private WebDriver getLocalWebDriver() throws RuntimeException {
		Browser browser = getBrowserType();
		try {
			if (browser.equals(Browser.FF)) {
				FirefoxDriver  ffDriver = new FirefoxDriver();
				return ffDriver;
			} else if (browser.equals(Browser.IE)) {
			 	InternetExplorerDriver ieDriver = new InternetExplorerDriver();
				return ieDriver;
			} else if (browser.equals(Browser.CHROME)) {
				return getChromeDriver();
			} else if (browser.equals(Browser.HTML_UNIT)) {
				HtmlUnitDriver htmlUnitDriver = new HtmlUnitDriver(true);
				return htmlUnitDriver;
			} else if (browser.equals(Browser.IPHONE)) {
				throw new RuntimeException(
						"IPHONE/IPAD tests can be run only when a remote"
						+ " url is entered.");
			} else {
				throw new RuntimeException(
						"Browser type not supported.It should be one of these"
						+ " FF or IE or chrome or iphone or ipad or html_unit.");
			}
		} catch (Exception e) {
			throw new RuntimeException(
					"Error while creating local webdriver instance for " + browser + ": "
					+ e);
		}
	}

	/**
	 * Returns true if REMOTE_URL is set in system property.
	 * 
	 * @param url
	 */
	boolean isRemoteWebDriverRequested(String url) {
		return (url != null && !url.isEmpty() && url.contains(HTTP)) ? true : false;
	}

	/**
	 * Returns the {@link FirefoxDriver} with custom profile if the profile exists
	 * in the machine or returns a {@link FirefoxDriver} instance.
	 */
	private FirefoxDriver getFirefoxDriver() {
		// check in parameters received pom.xml
		String profileName = System.getProperty(BROWSER_PROFILE_NAME.toUpperCase());
		if (profileName != null && !profileName.isEmpty()) {
			return new FirefoxDriver(BrowserType.getCustomProfile(profileName));
		}
		String proxy = System.getProperty(PROXY.toUpperCase());
		String proxyHost = System.getProperty(PROXY_HOST.toUpperCase());
		if (proxy != null && !proxy.isEmpty() && 
				proxyHost != null && !proxyHost.isEmpty()) {
			return new FirefoxDriver(BrowserType.getProxyProfile(proxy, proxyHost));
		}
		return new FirefoxDriver();
	}

	/**
	 * Returns the {@link WebDriver} for chrome browser.
	 */
	private WebDriver getChromeDriver() {
		return new RemoteWebDriver(service.getUrl(), DesiredCapabilities.chrome());
	}

	/**
	 * Waits for seconds.
	 * 
	 */
	protected void waitForSeconds(int seconds) {
		long waitUntilTime = System.currentTimeMillis() + (seconds * 1000);
		System.out.println("Waiting for " + seconds + " seconds before next retry....");
		while (System.currentTimeMillis() < waitUntilTime) {
			// do nothing just wait for seconds.
		}
	}

	public void logInConsoleAndReport(final String msg) {
		String logDetails = String.format("%s %s  %s", new SimpleDateFormat(
		"HH:mm ss-SSS").format(new Date()), ":", msg);
		System.out.println(logDetails);
		Reporter.log(logDetails);
	}



}
