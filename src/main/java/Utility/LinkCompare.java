package Utility;

import static Utility.ActionUtility.logFailureStep;
import static org.testng.Assert.assertEquals;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;


public class LinkCompare {

	public boolean compareExpectedLinksWithActuals(String scrURL, String destURL, WebDriver driver) throws Exception {
		
	
		boolean isContentSame = true;
		
		String[] expected = getSourcePageLinks(scrURL, driver);
		String[] actual = getDestinationPageLinks(destURL, driver);
		
		for (int j = 0, i = 0; j < expected.length; j++, i++) {
			if (!actual[i].equals(expected[j])) {
				logFailureStep("FOUND: " + actual[i] + " INSTEAD OF:"
						+ expected[j]);
				isContentSame = false;
			}
		}
		assertEquals(actual.length, expected.length,
				"The actual number of links is not"
						+ " same as expected no of links.");
				
		return isContentSame;
	}
	
	public boolean compareExpectedLinksWithActualsNew(String scrURL, String destURL, WebDriver driver) throws Exception {
		
		
		boolean isContentSame = false;
		
		String[] expected = getSourcePageLinks(scrURL, driver);
		String[] actual = getDestinationPageLinks(destURL, driver);
		
		     
                for(int j = 0 ; j < actual.length ; j++)   
                {   
                	
                	isContentSame = Arrays.asList(expected).contains(actual[j]);

                    
                       
                    	logFailureStep("FOUND: " + actual[j] + " INSTEAD OF:");
                     
                }   
            
				
		return isContentSame;
	}
	

	public String[] getSourcePageLinks1(String scrURL, WebDriver driver) throws Exception {
		
		 driver.get(scrURL);
		 //driver.get(UrlHelper.convertUrl(scrURL));
		List<WebElement> links = driver.findElements(By.xpath("//a"));
		StringBuffer sb = new StringBuffer();
		for (WebElement link : links) {
								
			sb.append(link.getAttribute("href")+ "\n");
		}
		String sourceText = sb.toString();
		
		return getLinksTextAndTargetsAsArray(sourceText);
	}
	
	
	

	public String[] getDestinationPageLinks1(String destURL, WebDriver driver) throws Exception {
		
		driver.get(destURL);
		// driver.get(UrlHelper.convertUrl(destURL));
		List<WebElement> links = driver.findElements(By.xpath("//a"));
		StringBuffer sb = new StringBuffer();
		for (WebElement link : links) {
			
		     sb.append(link.getAttribute("href")+ "\n");
		}
		String destinationText = sb.toString();
		return getLinksTextAndTargetsAsArray(destinationText);
	}
	
	public String[] getSourcePageLinks(String scrURL, WebDriver driver) throws Exception {
		
		 driver.get(scrURL);
		 //driver.get(UrlHelper.convertUrl(scrURL));
		List<WebElement> links = driver.findElements(By.xpath("//a"));
		StringBuffer sb = new StringBuffer();
		for (WebElement link : links) {
								

			
			String convertedUrl="";
			if(link.getAttribute("href").contains("cmd"))
			{
				
				Pattern p2 = Pattern.compile("cmd");
				
				String[] result2 = p2.split(link.getAttribute("href"));
				convertedUrl = result2[1];
			}
			
			else
			{
				convertedUrl = link.getAttribute("href");
			}
			sb.append(link.getText() + ":" + convertedUrl + "\n");
		
		}
		String sourceText = sb.toString();
		
		return getLinksTextAndTargetsAsArray(sourceText);
	}
	
	
	

	public String[] getDestinationPageLinks(String destURL, WebDriver driver) throws Exception {
		
		driver.get(destURL);
		// driver.get(UrlHelper.convertUrl(destURL));
		List<WebElement> links = driver.findElements(By.xpath("//a"));
		StringBuffer sb = new StringBuffer();
		for (WebElement link : links) {
			
			String convertedUrl="";
			if(link.getAttribute("href").contains("cmd"))
			{
				
				Pattern p2 = Pattern.compile("cmd");
				
				String[] result2 = p2.split(link.getAttribute("href"));
				convertedUrl = result2[1];
			}
			
			else
			{
				convertedUrl = link.getAttribute("href");
			}
			sb.append(link.getText() + ":" + convertedUrl + "\n");
		}
		String destinationText = sb.toString();
		return getLinksTextAndTargetsAsArray(destinationText);
	}
	
	
	public static String[] getLinksTextAndTargetsAsArray(String s) {
		String[] sArray = s.split("\n");
		return sArray;
	}
	
	
	public static String[] getHomePageLeftNavLinkTextAndTargets(String URL,  WebDriver driver) {
		
		driver.get(URL);
		List<WebElement> links = driver.findElements(By.xpath("//a"));
		StringBuffer sb = new StringBuffer();
		for (WebElement link : links) {
			String convertedUrl="";
			if(link.getAttribute("href").contains("cmd"))
			{
				
				Pattern p2 = Pattern.compile("cmd");
				
				String[] result2 = p2.split(link.getAttribute("href"));
				convertedUrl = result2[1];
			}
			
			else
			{
				convertedUrl = link.getAttribute("href");
			}
			sb.append(link.getText() + ":" + convertedUrl + "\n");
		}
		String targetLinks = sb.toString();
		
		return getLinksTextAndTargetsAsArray(targetLinks);
	}
	
	//TODO:aravink remove this after testing.
	  public static void main(String[] a) {
		  WebDriver driver = new HtmlUnitDriver();
		  System.out.println(getHomePageLeftNavLinkTextAndTargets("https://www.paypal.com/ca/webapps/mpp/security-center/chargeback-faq", driver));
	  } 
}

