package Utility;

import static Utility.ActionUtility.logFailureStep;
import static org.testng.Assert.assertEquals;

import java.util.Arrays;
import java.util.List;

import Utility.StringToHex;

public class StringHelper {

	public static String RemoveSpecialChars(String input){
	    return input.replaceAll("[^0-9a-zA-Z]", "");
	}

	public static boolean isAlpha(String stringToCheck)
	{
		stringToCheck = stringToCheck.toUpperCase();
	  for (int i = 0; i < stringToCheck.length(); i ++)
	  {
	    int c = (int) stringToCheck.charAt(i);
	    if (c < 65 || c > 90)
	      return false;
	  }

	  return true;
	}
	
	 public static boolean isNumbers(String str) {
	   //check for null or empty
	   if (str == null || str.length() == 0)
	     return false;
	   for (int i = 0; i < str.length(); i++) {
	  	 if (!Character.isDigit(str.charAt(i)))
	  		 return false;
	   }
	   return true;
	 }

	public static String convertToSpecialChars(String input) {
		String out = input;
		if (input.indexOf('%') >= 0) {
			out = input.substring(0,input.indexOf('%')) +
	    	   	  StringToHex.convertHexToString(input.substring(input.indexOf('%')+1));
		}
	    return out;
	}
	
	public static String[] getStringAsArray(String s,String separator){
    	String [] sArray = s.split(separator);
    	return sArray;
  }
	
	public static List<String> getStringAsList(String s,String separator){
    return Arrays.asList(getStringAsArray(s, separator));
  }

	public boolean compareExpectedArrayContentWithActuals(String[] expected,
      String[] actual) {
    boolean isContentSame = true;
    for (int j = 0, i = 0; j < expected.length; j++, i++) {
      if (!actual[i].equals(expected[j])) {
        logFailureStep("FOUND: " + actual[i] + " INSTEAD OF:"
            + expected[j]);
        isContentSame = false;
      }
    }
    assertEquals(actual.length, expected.length, "The actual number of elements is not"
        + " same as expected no of elements.");
    return isContentSame;
  }

	/**
	 * Gets the word at specified position from the passed in sentence.
	 *  <p> The words in the sentences are separated by single space only.
	 *  </p>
	 * @param sentence from which the word needs to be extracted
	 * @param nthWord position of the word which needs to be extracted from the sentence 
	 * @return the word occurring in nthWord position in the sentence.
	 */
	public static String getWordFromString(String sentence, int nthWord){
		String[] words = sentence.trim().split(" ");
		return words[nthWord-1];
	}
}
