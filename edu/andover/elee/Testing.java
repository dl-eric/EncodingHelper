package edu.andover.elee;

import java.io.UnsupportedEncodingException;

public class Testing {

	public static void main(String[] args)
	{
		long codepoint = 1000000;
		String str = Long.toBinaryString(codepoint);
		
		int length = str.length();
		if(length < 8)
		{
			String utf = String.format("%08d", Integer.parseInt(str));
			System.out.println(utf);
			
			String[] utfArray = {utf.substring(0, 8)};
			String totalStrHex = "";
			for(int i = 0; i < utfArray.length; i++){
				int decimal = Integer.parseInt(utfArray[i], 2);
				String hexStr = Integer.toString(decimal, 16);
				totalStrHex = totalStrHex + hexStr;
			}
			System.out.println(totalStrHex);
		}
		else if (length < 12)
		{
			String padded = String.format("%011d", Integer.parseInt(str));
			String firstHalf = padded.substring(0,5);
			String secondHalf = padded.substring(5, 11);
			String utf = "110" + firstHalf + "10" + secondHalf;
			System.out.println(utf);
			
			String[] utfArray = {utf.substring(0, 8), utf.substring(8, 16)};
			String totalStrHex = "";
			for(int i = 0; i < utfArray.length; i++){
				int decimal = Integer.parseInt(utfArray[i], 2);
				String hexStr = Integer.toString(decimal, 16);
				totalStrHex = totalStrHex + hexStr;
			}
			System.out.println(totalStrHex);
		}
		else if (length < 17)
		{
			String padded = String.format("%016d", Long.parseLong(str));
			String firstPart = padded.substring(0,4);
			String secondPart = padded.substring(4,10);
			String thirdPart = padded.substring(10, 16);
			String utf = "1110" + firstPart + "10" + secondPart + "10" + thirdPart;
			System.out.println(utf);
			
			String[] utfArray = {utf.substring(0, 8), utf.substring(8, 16), utf.substring(16, 24)};
			String totalStrHex = "";
			for(int i = 0; i < utfArray.length; i++){
				int decimal = Integer.parseInt(utfArray[i], 2);
				String hexStr = Integer.toString(decimal, 16);
				totalStrHex = totalStrHex + hexStr;
			}
			System.out.println(totalStrHex);
		}
		else if (length < 22)
		{
			String strFirst = str.substring(0, str.length() - 11);
			String strSecond = str.substring(str.length() - 11, str.length());
			
			String paddedFirst = String.format("%011d", Long.parseLong(strFirst));
			String padded = paddedFirst + strSecond;
			String firstPart = padded.substring(0, 3);
			String secondPart = padded.substring(3, 9);
			String thirdPart = padded.substring(9, 15);
			String fourthPart = padded.substring(15, 21);
			String utf = "11110" + firstPart + "10" + secondPart + "10" + thirdPart + "10" + fourthPart;
			System.out.println(utf);
			
			String[] utfArray = {utf.substring(0, 8), utf.substring(8, 16), utf.substring(16, 24), utf.substring(24, 32)};
			String totalStrHex = "";
			for(int i = 0; i < utfArray.length; i++){
				int decimal = Integer.parseInt(utfArray[i], 2);
				String hexStr = Integer.toString(decimal, 16);
				totalStrHex = totalStrHex + hexStr + ",";
			}
			System.out.println(totalStrHex);
		}
		else if (length < 27)
		{
			String strFirst = str.substring(0, str.length() - 13);
			String strSecond = str.substring(str.length() - 13, str.length());
			
			String paddedFirst = String.format("%013d", Long.parseLong(strFirst));
			String padded = paddedFirst + strSecond;

			String firstPart = padded.substring(0, 2);
			String secondPart = padded.substring(2, 8);
			String thirdPart = padded.substring(8, 14);
			String fourthPart = padded.substring(14, 20);
			String fifthPart = padded.substring(20, 26);
			String utf = "111110" + firstPart + "10" + secondPart + "10" + thirdPart + "10" + fourthPart +
					"10" + fifthPart;
			System.out.println(utf);
			
			String[] utfArray = {utf.substring(0, 8), utf.substring(8, 16), utf.substring(16, 24), utf.substring(24, 32), utf.substring(32, 40)};
			String totalStrHex = "";
			for(int i = 0; i < utfArray.length; i++){
				int decimal = Integer.parseInt(utfArray[i], 2);
				String hexStr = Integer.toString(decimal, 16);
				totalStrHex = totalStrHex + hexStr + ",";
			}
			System.out.println(totalStrHex);
		}
		else if (length < 32)
		{
			String strFirst = str.substring(0, str.length() - 16);
			String strSecond = str.substring(str.length() - 16, str.length());
			
			String paddedFirst = String.format("%016d", Long.parseLong(strFirst));
			String padded = paddedFirst + strSecond;
			
			String firstPart = padded.substring(0, 1);
			String secondPart = padded.substring(1, 7);
			String thirdPart = padded.substring(7, 13);
			String fourthPart = padded.substring(13, 19);
			String fifthPart = padded.substring(19, 25);
			String sixthPart = padded.substring(25, 31);
			String utf = "1111110" + firstPart + "10" + secondPart + "10" + thirdPart + "10" + fourthPart +
					"10" + fifthPart + "10" + sixthPart;
			System.out.println(utf);
			String[] utfArray = {utf.substring(0, 8), utf.substring(8, 16), utf.substring(16, 24), utf.substring(24, 32), utf.substring(32, 40), utf.substring(40, 48)};
			String totalStrHex = "";
			for(int i = 0; i < utfArray.length; i++){
				int decimal = Integer.parseInt(utfArray[i], 2);
				String hexStr = Integer.toString(decimal, 16);
				totalStrHex = totalStrHex + hexStr + ",";
			}
			System.out.println(totalStrHex);
		}
		else
		{
			System.out.println("FAIL");
		}
	}
}
