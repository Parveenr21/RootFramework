package baseWebDriver;

import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.firefox.internal.ProfilesIni;

/**
 * Contains {@link Browser} enum and method to return Browser enums for the
 * BROWSER param specified in fashion/pom.xml.
 *  
 * 
 */
public class BrowserType {

	private static final String BROWSER = "BROWSER";
	private static final String BROWSER_PROFILE_NAME = "BROWSER_PROFILE_NAME";

	/**
	 * Returns {@link Browser}based on the BROWSER pram that is mentioned in the
	 * fashion/pom.xml.
	 * <p>
	 * This method could be used in tests to write tests based on browsers.
	 * </p>
	 */
	public static Browser getBrowserType() {
		String browser = System.getProperty(BROWSER);
		if (browser == null || browser.isEmpty()
				|| browser.equalsIgnoreCase(Browser.FF.toString())) {
			return Browser.FF;
		} else if (browser.equalsIgnoreCase(Browser.IE.toString())) {
			return Browser.IE;
		} else if (browser.equalsIgnoreCase(Browser.CHROME.toString())) {
			return Browser.CHROME;
		} else if (browser.equalsIgnoreCase(Browser.IPHONE.toString())
				|| browser.equalsIgnoreCase(Browser.IPAD.toString())) {
			return Browser.IPHONE;
		} else if (browser.equalsIgnoreCase(Browser.HTML_UNIT.toString())) {
			return Browser.HTML_UNIT;
		} else {
			return Browser.UNKNOWN;
		}
	}

	public static FirefoxProfile getCustomProfile(String profileName) {
		FirefoxProfile ffProfile = null;
		if (profileName != null && !profileName.isEmpty()) {
			ProfilesIni profiles = new ProfilesIni();
			ffProfile = profiles.getProfile(profileName);
			// must check if profile exists on test machine
			if (ffProfile != null) {
				ffProfile.setAcceptUntrustedCertificates(false);
				ffProfile.setAssumeUntrustedCertificateIssuer(true);
			}
		}
		return ffProfile;
	}
	
	public static FirefoxProfile getProxyProfile(String proxy, String proxyHost) {
		FirefoxProfile ffProfile = new FirefoxProfile();
		ffProfile.setPreference("network.proxy.type", 1);
		ffProfile.setPreference("network.proxy.http", proxy);
		ffProfile.setPreference("network.proxy.http_port", proxyHost);
		return ffProfile;
	}
	
	/**
	 * Enum for various browser types.
	 *  
	 */
	public enum Browser {

		FF("FF"), IE("IE"), CHROME("CHROME"), IPHONE("IPHONE"), IPAD("IPAD"), HTML_UNIT(
				"HTML_UNIT"), UNKNOWN("UNKNOWN");

		private String browser;

		/**
		 * Constructor to set browser.
		 * 
		 * @param String
		 *            browser
		 */
		private Browser(String browser) {
			this.browser = browser;
		}

		/**
		 * Returns the enum as String.
		 */
		@Override
		public String toString() {
			return browser;
		}
	}
}
