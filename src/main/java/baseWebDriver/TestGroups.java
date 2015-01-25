package baseWebDriver;

/**
 * Contains the groups to be used in test groups as constants.
 * 
 * @author 
 * 
 */
public class TestGroups {

	 //For tests that fail due to bugs in app.Please mark the tests that fail due to app bug as. 
  public static final String APP_BUG = "AppBug";

	// Mark your tests as NOT_IE_READY so they wont run on the testngIE suite.
	public static final String NOT_IE_READY = "NotIEReady";

 //Mark your tests as NOT_IE_READY so they wont run on the testngIE suite.
  public static final String PC_TEST = "PC_TEST";

	 //Mark your tests as Dev while the test is in development and to test them during tests development.
	public static final String DEV = "Dev";

	// Mark your tests as Broken so any broken tests can be excluded from a run.
	public static final String BROKEN = "Broken";

	// Mark your tests as NOT_ CHROME _READY.
	public static final String NOT_CHROME_READY = "NotChromeReady";

	// Mark the tests that needs to be run as BVT suite.
	public static final String BVT = "BVT";

	// Mark the tests that needs to be run as a part of regression suite.
	public static final String REGRESSION = "Regression";

	 //Mark the tests that needs to be run as monitors.These would report to gyro reporting.
	public static final String MONITOR = "Monitor";

	// Mark the tests that needs to be run as Feature suite.
	public static final String FEATURE = "Feature";

	// Mark the data tests with this group so all data tests can be run when needed.
	public static final String DATA_TEST = "DataTest";

	//Mark the tests that needs to are large and takes more than 2 minutes to excecute.
	public static final String LARGE_TEST = "LargeTest";
	
	//Mark tests that are only for RAPTOR with this group.
	public static final String RAPTOR_TEST = "RaptorTest";
	
  //Mark tests that are only for BETA with this group.
  public static final String BETA = "BetaTest";

}