package Utility;



	import java.util.StringTokenizer;
	import org.openqa.selenium.JavascriptExecutor;
	import org.openqa.selenium.WebDriver;
	import org.openqa.selenium.firefox.FirefoxDriver;
	import org.testng.Assert;
	import org.testng.annotations.Test;



	public class SEOUtility {
	                /**
	                * Global Variables
	                */
	                public static String pageURL ="http://searchfr.fr.vip.paradise.qa.ebay.com/i.html?LH_PrefLoc=1&cmd=blend&_nkw=ipod";
	                public WebDriver driver;

	                
	                public String getPageTitle (){
	            		return driver.getTitle();
	            
	            	}
	                
	                /**
	                * Method executes the JS to extract the Meta Tag Keywords 
	                 * @return : Meta Tag Keyword Content //meta[@contents]
	                * @throws Exception 
	                 */
	                public String getMetaKeyWords() throws Exception{
	                                String keyWords = "";
	                                JavascriptExecutor js = (JavascriptExecutor) driver;
	                                keyWords = (String)js.executeScript(" return document.getElementsByName('keywords')[0].getAttribute('content');");
	                                driver.close();
	                                return keyWords;
	                }

	                /**
		                * Method executes the JS to extract the Meta Tag description 
		                 * @return : Meta Tag Keyword Content //meta[@contents]
		                * @throws Exception 
		                 */
		                public String getMetaDescription() throws Exception{
		                                String description = "";
		                                JavascriptExecutor js = (JavascriptExecutor) driver;
		                                description = (String)js.executeScript(" return document.getElementsByName('description')[0].getAttribute('content');");
		                                driver.close();
		                                return description;
		                }
	                
	                @Test( description ="SEO Test : Check Meta Tag Keywords ", groups = { "LOCAL" })
	                public void testMetaKeyWords() throws Exception {
	                                int lowerChar = 0;
	                                driver = new FirefoxDriver();
	                                driver.get(pageURL);
	                                StringTokenizer st = new StringTokenizer(getMetaKeyWords(),",");
	                                while(st.hasMoreTokens()){
	                                                char [] ch = st.nextToken().trim().toCharArray();
	                                                for(int i=0;i<ch.length;i++){
	                                                                if(Character.isUpperCase(ch[i])) {
	                                                                                lowerChar ++;
	                                                                }
	                                                }
	                                }
	                                Assert.assertEquals(lowerChar,0,"Meta Tag KeyWords have lower case chars !");
	                                driver.quit();
	                }
	}
