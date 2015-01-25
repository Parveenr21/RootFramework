package baseWebDriver;

/**
 * Enum for various application frameworks where tests can run.
 *
 */
public enum LocatorType {
	CLASSIC_LOCATOR("CLASSIC"), 
	COMMON_LOCATOR("COMMON"), 
	RAPTOR_LOCATOR("RAPTOR"),
	BETA_LOCATOR("BETA");
	
	private String pageType;

	private LocatorType(final String locatoryType) {
		this.pageType = locatoryType;
	}

	@Override
	public String toString() {
		return pageType;
	}
}
