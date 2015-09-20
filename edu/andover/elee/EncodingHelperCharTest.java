package edu.andover.elee;

import static org.junit.Assert.*;

import org.junit.Test;

public class EncodingHelperCharTest {

	@Test
	public void testEncodingHelperCharInt() {
		int codepoint = 123;
		EncodingHelperChar encodingHelperChar = new EncodingHelperChar(codepoint);
		// Make sure codepoint is valid
		
		fail("Not yet implemented");
	}

	@Test
	public void testEncodingHelperCharByteArray() {
		byte[] byteArray = {127, -128, 0};
		EncodingHelperChar encodingHelperChar = new EncodingHelperChar(byteArray);
		// Make sure byteArray is valid
		fail("Not yet implemented");
	}

	@Test
	public void testEncodingHelperCharChar() {
		fail("Not yet implemented");
	}
	
	@Test
	public void testGetCodepoint() {
		EncodingHelperChar encodingHelperChar = new EncodingHelperChar('c');
		int codepoint = encodingHelperChar.getCodepoint();
		
		assertTrue(isValidCodepoint(codepoint));
	}

	@Test
	public void testSetCodepoint() {
		EncodingHelperChar encodingHelperChar = new EncodingHelperChar('c');
		int codepoint = 1234;
		encodingHelperChar.setCodepoint(codepoint);
		assertEquals(codepoint, encodingHelperChar.getCodepoint());
	}

	@Test
	public void testToUtf8Bytes() {
		/*
		 * 1. Get codepoint
		 * 2. Convert codepoint to UTF-8 Hex
		 * 3. Return the Hex
		 */
		EncodingHelperChar encodingHelperChar = new EncodingHelperChar('c');
		int codepoint = encodingHelperChar.getCodepoint();
		
		assertTrue(isValidCodepoint(codepoint));
		
		String hexStr = convertToHex(codepoint);
		String[] hexStrArray = hexStr.split(",");
		
		for(int i = 0; i < hexStrArray.length; i++)
		{
			assertTrue(isValidHex(hexStrArray[i]));
		}
	}

	@Test
	public void testToCodepointString() {
		fail("Not yet implemented");
	}

	@Test
	public void testToUtf8String() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetCharacterName() {
		fail("Not yet implemented");
	}
	
	/*
	 * *************************************************************************************
	 * End of Tests. Function code starts here
	 * *************************************************************************************
	 */
	
	public boolean isValidCodepoint(int cp)
	{
		return !(cp < 0 || cp > 1114111);
	}
	
	public boolean isValidHex(String hex) {
		int temp = Integer.parseInt(hex, 16);
		return (temp < 255 || temp > 0);
	}
	
	public String convertToHex(int codepoint) {
		String str = Long.toBinaryString(codepoint);
		String totalStrHex = "";
		
		int length = str.length();
		if(length < 8)
		{
			String utf = String.format("%08d", Integer.parseInt(str));
			System.out.println(utf);
			
			String[] utfArray = {utf.substring(0, 8)};
			
			for(int i = 0; i < utfArray.length; i++){
				int decimal = Integer.parseInt(utfArray[i], 2);
				String hexStr = Integer.toString(decimal, 16);
				totalStrHex = totalStrHex + hexStr + ",";
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
			
			for(int i = 0; i < utfArray.length; i++){
				int decimal = Integer.parseInt(utfArray[i], 2);
				String hexStr = Integer.toString(decimal, 16);
				totalStrHex = totalStrHex + hexStr + ",";
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
		
			for(int i = 0; i < utfArray.length; i++){
				int decimal = Integer.parseInt(utfArray[i], 2);
				String hexStr = Integer.toString(decimal, 16);
				totalStrHex = totalStrHex + hexStr + ",";
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
		
			for(int i = 0; i < utfArray.length; i++){
				int decimal = Integer.parseInt(utfArray[i], 2);
				String hexStr = Integer.toString(decimal, 16);
				totalStrHex = totalStrHex + hexStr + ",";
			}
			System.out.println(totalStrHex);
		}
		else
		{
			return "FAILURE";
		}
		return totalStrHex;
	}
	

}
