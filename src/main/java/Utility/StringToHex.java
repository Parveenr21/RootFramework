package Utility;

import Utility.StringToHex;

public class StringToHex {

	public static String convertStringToHex(String str) {
		char[] chars = str.toCharArray();
		StringBuilder hex = new StringBuilder();
		for (int i = 0; i < chars.length; i++) {
			hex.append(Integer.toHexString((int) chars[i]));
		}
		return hex.toString();
	}

	public static String convertHexToString(String hex) {

		StringBuilder sb = new StringBuilder();
		StringBuilder temp = new StringBuilder();

		// 49204c6f7665204a617661 split into two characters 49, 20, 4c...
		for (int i = 0; i < hex.length() - 1; i += 2) {

			// grab the hex in pairs
			String output = hex.substring(i, (i + 2));
			// convert hex to decimal
			int decimal = Integer.parseInt(output, 16);
			// convert the decimal to character
			sb.append((char) decimal);

			temp.append(decimal);
		}
		//System.out.println("Decimal : " + temp.toString());

		return sb.toString();
	}

	public static String convertMultiHexToString(String in) {
		StringBuilder sb = new StringBuilder("");
		String out;
		while (in.indexOf('%') >= 0) {
			out = in.substring(0,in.indexOf('%'));
			sb.append(out);
			in = in.substring(in.indexOf('%'));
			out = in.substring(in.indexOf('%')+1,in.indexOf('%')+3);
			try {
				out = StringToHex.convertHexToString(out);
				in = in.substring(in.indexOf('%')+3);
			} catch (Exception e) {}
			sb.append(out);
		}
		sb.append(in);
		String s = sb.toString();
		sb = null;
		return s;
	}
	
	public static void main(String[] args) {

		System.out.println("\n***** Convert ASCII to Hex *****");
		String str = "I Love Java! ";
		System.out.println("Original input : " + str);

		String hex = StringToHex.convertStringToHex(str);

		System.out.println("Hex : " + hex);

		System.out.println("\n***** Convert Hex to ASCII *****");
		System.out.println("Hex : " + hex);
		System.out.println("ASCII : " + StringToHex.convertHexToString("22"));
		System.out.println("ASCII : " + StringToHex.convertHexToString("20"));
		System.out.println("ASCII : " + StringToHex.convertHexToString("26"));
	
		System.out.println("Hex to String: ");
		
		String in = "Big%20%26%20Tall";
		System.out.println(StringToHex.convertMultiHexToString(in));
		
		String in2 = "Big%26Tall";
		System.out.println(StringToHex.convertMultiHexToString(in2));
		
		String in3 = "Big%20Tall";
		System.out.println(StringToHex.convertMultiHexToString(in3));

		String in4 = "Regular";
		System.out.println(StringToHex.convertMultiHexToString(in4));

		System.out.println("ASCII : " + StringToHex.convertHexToString("2F"));
		
		String in6 = "21%2F1";
		System.out.println(StringToHex.convertMultiHexToString(in6));
		
		String in5 = "21%201%2F2";
		System.out.println(StringToHex.convertMultiHexToString(in5));

	}
	
	public static void mains(String[] args) {
		char a = 'A';
		char b = (char) (a + 1);
		System.out.println(a + b);
		System.out.println("a + b is " + a + b);
		int x = 75;
		char y = (char) x;
		char half = '\u00BD';
		System.out.println("y is " + y + " and half is " + half);
	}
}
