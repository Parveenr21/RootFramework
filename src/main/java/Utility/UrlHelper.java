package Utility;

import java.util.regex.Matcher;
import java.util.regex.Pattern;



/**
 * This is a helper class which can validate and convert a QATE URL 
 * given a target pool and site. Test case authors should be able to simply
 * copy the URLS from selenese and use them without bothering to construct 
 * them inside the test case. It will also help control usage of 3rd party
 * tools. It can compare the URL with a standard set of 3rd Party URLS and
 * throw Exception if the tool is not in the list.
 * 
 * 
 * 
 */
public final class UrlHelper {

	public static final String STAGETYPE = "STAGETYPE";
	public static final String SITE = "SITE";
	public static final String STAGE = "STAGE";
		
	// Include all valid (reliable/maintained) 3rd party tools here
	final static String[] PAYPAL_VALID_3RD_PARTY_URL_PATTERN = { 
		"http://dct/",
		"http://tools/", "http://qatools/",
		"http://qawebbatch.corp.ebay.com:8080/",
		"http://d-sjc-wbace2.corp.ebay.com:8080/apiwebtool/",
		"http://bom1.corp.ebay.com:9080/",
		"http://c-elvis-v3c.qa.ebay.com:8080/",
		"http://10.249.72.27",
		"https://graph.facebook.com/",
		"http://qa-cdcauto5:8080/",
		"https://cms.paypal.com/"
		//"https?://[\\w\\-]*\\.((\\w*\\.){0,3})paypal\\.(com|\\w*).*"
		};

	public final static String PAYPAL_VALID_QATE_URL_PATTERN = "(https?://[\\w\\-]*\\.)((\\w*\\.){0,3})(qa|corp)?\\.paypal(static|rtm|partnernetwork)?\\.(com|\\w*).*";
	public final static String[] PAYPAL_FORBIDDEN_URL_PATTERNS = {"https?://.*/UpdateConfigCategoryXml\\?.*"}; 
	public final static String PAYPAL_VALID_POOL_PATTERN = "(fp(\\d)+|stage(\\d)+|paradise|__POOLNAME__)\\.";
	
	
	private static Pattern pattern = Pattern
			.compile(PAYPAL_VALID_QATE_URL_PATTERN);
	private static Pattern validPoolPattern = Pattern
			.compile(PAYPAL_VALID_POOL_PATTERN);

	
	/**
	 * Check for forbidden url patterns
	 * @param url
	 * @return
	 */
	public static boolean isForbiddenURL(String url){
		
		if(url != null){
			//Check for forbidden patterns
			for (int i = 0; i < PAYPAL_FORBIDDEN_URL_PATTERNS.length; i++) {
				if(url.contains(PAYPAL_FORBIDDEN_URL_PATTERNS[i]))
					return true;
			}
		}
		return false;
	}
	
	/**
	 * Make sure the URL is authentic and translate it for the specified pool
	 * and site.
	 * 
	 * @param url
	 * @param pool
	 * @param site
	 * @return
	 */
	public static String convertURL(String url, String stageType, String stage, String site)
			throws RuntimeException {
		
		//if the mandatory parameters are not specified
		if (stageType == null || stageType.trim().length() <= 0 ||
				stage == null || stageType.trim().length() <= 0 ||
			site == null || stageType.trim().length() <= 0) {
			return url;
		}

		//Non eBay URLs do not convert
		if(!("qa".equalsIgnoreCase(stageType) 
				|| "corp".equalsIgnoreCase(stageType)
				|| "dev".equalsIgnoreCase(stageType)
				|| "sandbox".equalsIgnoreCase(stageType)
				|| "prod".equalsIgnoreCase(stageType)
				)){
			return url;
		}
		
		Matcher matcher = pattern.matcher(url);

		//Check for forbidden patterns
		if(isForbiddenURL(url)){
				throw new RuntimeException("Forbidden URL " + url + " found.");
		}
		
		// Check and validate tools URL, make sure that only standard tools are
		// used in test cases
		boolean foundPattern = false;
		for (int i = 0; i < PAYPAL_VALID_3RD_PARTY_URL_PATTERN.length; i++) {
			if (url.startsWith(PAYPAL_VALID_3RD_PARTY_URL_PATTERN[i])
					|| Pattern.matches(PAYPAL_VALID_3RD_PARTY_URL_PATTERN[i], url)) {
				foundPattern = true;
				break;
			}
		}

		// URL does not confirm to QATE or listed tools
		if (!foundPattern && !matcher.matches()) {
			throw new RuntimeException("Invalid QATE / Tools URL " + url
					+ " found.");
		}else if(foundPattern){
			return url;	
		}

		StringBuffer sbURL = new StringBuffer();
		String prefix = matcher.group(1); 

			sbURL.append(prefix);

			// For group 2 i.e. (\\w*\\.){0,3}, it can catch things like
			// "uk.paradise."
			// as well as "shop.fp001.". So we have to distinguish between these
			// two cases
			// and for the second case we need to ensure that "shop" does not
			// get replaced.

			String siteAndPool = matcher.group(2);

			int indexOfDot = 0;
			if (siteAndPool != null && siteAndPool.length() > 0) {
				if ((indexOfDot = siteAndPool.indexOf(".")) < siteAndPool
						.length() - 1) {
									
				} else if (!validPoolPattern.matcher(siteAndPool).matches()) {
					// This can contain a string like "paradise." or "fpXXX." OR
					// "shop." etc.
					sbURL.append(siteAndPool);
				}
			}

			// Now let's add the site
			if (site != null && 
				site.trim().length() > 0 && 
				!"US".equalsIgnoreCase(site)) {
				sbURL.append(site.toLowerCase()).append(".");
			}

			// Now let's add the pool
			if (!stage.contains("{") &&
					stage != null && 
							stage.trim().length() > 0 && 
				!"staging".equalsIgnoreCase(stage)) {
				sbURL.append(stage.toLowerCase()).append(".");
			}
		    
			// Now let's add the paradise for staging pool
			if (!stage.contains("{") &&
					stage != null && stage.trim().length() > 0 && 
				"staging".equalsIgnoreCase(stage) && 
				!site.equalsIgnoreCase("US")) {
				sbURL.append("paradise.");
			}

		int index = 0;
		if((index = url.indexOf("qa.paypal")) > -1){
			index = index + "qa".length();
		}else if((index = url.indexOf("corp.paypal")) > -1){
			index = index + "corp".length();
		}else if((index = url.indexOf("sjc.paypal")) > -1){
			index = index + "sjc".length();
		}else if((index = url.indexOf("dev.paypal")) > -1){
			index = index + "dev".length();			
		}else{
			throw new RuntimeException("Invalid QATE / Tools URL " + url + " found.");
		}

		// Finally let's add the suffix with query string
		if(!"prod".equals(stageType))
			sbURL.append(stageType + ".");
		
		sbURL.append(url.substring(index+1));

		return sbURL.toString();
	}

	/**
	 * Make sure the URL is authentic and translate it for the default pool
	 * and site.
	 * 
	 * @param url
	 * @return
	 */
	public static String convertUrl(String url)
			throws Exception {
		return convertURL(url,  
				System.getProperty(STAGETYPE), 
				System.getProperty(STAGE), 
				System.getProperty(SITE));
	}
	
	public static String convertUrl(String url, String site)
	    throws Exception {
    return convertURL(url,  
		    System.getProperty(STAGETYPE), 
		    System.getProperty(STAGE), 
		    site);
	}
	
	 /**
   * Returns Offer Version id or fron page url.
   * https://sjcitdev16.sjc.its.ebay.com:8443/campaignstudio/createoffer.htm?message=offer.creation.success&createdOfferId=11427&createdOfferVersionId=72965
   */
  public static String getVersionIdFromSucessMessage() {
    
    String message = "Ad unit created successfully. Offer Id: 11628 Ad Unit Id: 73595";
    String[] createdOfferDetails = message.split(": ");
    String VersionId = createdOfferDetails[2].replaceAll("[a-zA-Z=]","");
  
    return VersionId;
  }
	
    
    public static void main(String[] args) {
    	
//    	System.setProperty("STAGE", "stage2vm1020");
//    	System.setProperty("STAGETYPE", "qa");
//    	System.setProperty("SITE", "US");
//    	try {
//        	System.out.println(convertUrl("https://admin.qa.paypal.com"));	
//		} catch (Exception e) {
//			e.printStackTrace();			
//		}
    	
      //vreXpYrS=1436889534&vteXpYrS=1342220557&vr=8278d02f1380a0f244a3e663ffffffdf&vt=8278d02f1380a0f244a3e663ffffffde
    //	String guid = "82a5fbe11380a0f244a3e663ffffff8d";
     //   	System.out.println(Math.abs(guid.hashCode() % 100));
    
        	System.out.println(getVersionIdFromSucessMessage());
      
	}
	
}
