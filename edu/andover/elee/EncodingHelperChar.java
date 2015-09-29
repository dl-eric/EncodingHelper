package edu.andover.elee;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.nio.charset.Charset;
import java.util.Scanner;

/**
 * The main model class for the EncodingHelper project in Software Design,
 * Fall 2015, Phillips Academy.
 * 
 * Each object of this class represents a single Unicode character.  The class's
 * methods generate various representations of the character.
 * 
 * Every method that takes an argument may throw an IllegalArgumentException if
 * given an illegal input.  This includes codepoints that are out of the legal
 * range for Unicode, byte arrays that are invalid encodings for a single
 * character, etc.
 * 
 * @author Jadrian Miles
 */
class EncodingHelperChar {
	private int codepoint;
	private String[][] unicodeData = getUnicodeData();

	public EncodingHelperChar(int codepoint) {
		if(isValidCodepoint(codepoint))
			this.codepoint = codepoint;
		else
			throw new IllegalArgumentException("Illegal Codepoint");
	}

	public EncodingHelperChar(byte[] utf8Bytes) {
		//TODO: isValidUtf8Bytes
		try {
			String bytesAsString = new String(utf8Bytes, "UTF-8");

			char d = bytesAsString.charAt(0);
			int decimal = (int)d;
			if (isValidCodepoint(decimal))
				codepoint = decimal;
			else
				throw new IllegalArgumentException("Illegal Byte Sequence");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}

	public EncodingHelperChar(char ch) {
		if(isValidCodepoint((int)ch))
			codepoint = (int)ch;
		else
			throw new IllegalArgumentException("Illegal Character");
	}

	public int getCodepoint() {
		return codepoint;
	}

	public void setCodepoint(int codepoint) {
		if(isValidCodepoint(codepoint))
			this.codepoint = codepoint;
		else
			throw new IllegalArgumentException("Illegal Codepoint");
	}

	/**
	 * Converts this character into an array of the bytes in its UTF-8
	 * representation.
	 *   For example, if this character is a lower-case letter e with an acute
	 * accent, whose UTF-8 form consists of the two-byte sequence C3 A9, then
	 * this method returns a byte[] of length 2 containing C3 and A9.
	 * 
	 * @return the UTF-8 byte array for this character
	 */
	public byte[] toUtf8Bytes() {
		char c = (char)codepoint;
		String temp = c + "";
		return temp.getBytes(Charset.forName("UTF-8"));
	}

	/**
	 * Generates the conventional 4-digit hexadecimal code point notation for
	 * this character.
	 *   For example, if this character is a lower-case letter e with an acute
	 * accent, then this method returns the string U+00E9 (no quotation marks in
	 * the returned String).
	 *
	 * @return the U+ string for this character
	 */
	public String toCodepointString() {
		int indexOfCodepoint = 0;
		boolean found = false;
		for(int r = 0; r < 29215; r++) {
			int unicodePoint = Integer.parseInt(unicodeData[r][0], 16);

			if (codepoint == unicodePoint) {
				indexOfCodepoint = r;
				found = true;
			}
		}
		if(!found)
			return "U+" + Integer.toHexString(codepoint).toUpperCase();

		return "U+" + unicodeData[indexOfCodepoint][0];
	}

	/**
	 * Generates a hexadecimal representation of this character suitable for
	 * pasting into a string literal in languages that support hexadecimal byte
	 * escape sequences in string literals (e.g. C, C++, and Python).
	 *   For example, if this character is a lower-case letter e with an acute
	 * accent (U+00E9), then this method returns the string \xC3\xA9. Note that
	 * quotation marks should not be included in the returned String.
	 *
	 * @return the escaped hexadecimal byte string
	 */
	public String toUtf8String() {
		String utf8Hex = convertToUtf8Hex(codepoint);

		String utf8HexToSplit = utf8Hex.replaceAll("..(?!$)", "$0:");
		String[] utf8HexSplit = utf8HexToSplit.split(":");

		String utf8String = "";
		for(int i = 0; i < utf8HexSplit.length; i++) {
			utf8String = utf8String + "\\x" + utf8HexSplit[i].toUpperCase();
		}

		return utf8String;
	}

	/**
	 * Generates the official Unicode name for this character.
	 *   For example, if this character is a lower-case letter e with an acute
	 * accent, then this method returns the string "LATIN SMALL LETTER E WITH
	 * ACUTE" (without quotation marks).
	 *
	 * @return this character's Unicode name
	 */
	public String getCharacterName() {
		int temp = 0;
		boolean found = false;
		for(int r = 0; r < 29215; r++) {
			int unicodePoint = Integer.parseInt(unicodeData[r][0], 16);

			if (codepoint == unicodePoint && unicodeData[r][1].equals("<control>")){
				return unicodeData[r][1] + " " + unicodeData[r][10];
			}
			if (codepoint == unicodePoint) {
				found = true;
				temp = r;
			}
		}

		if(!found)
			return "<unknown> " + toCodepointString();

		return unicodeData[temp][1];
	}

	/*
	 * ****************************************************************************
	 * Helper Functions
	 * ****************************************************************************
	 */

	public String[][] getUnicodeData() {
		String[][] arrayOfData = new String[29215][14];
		File dataFile = new File("UnicodeData.txt");

		try {
			Scanner scanner = new Scanner(dataFile);

			scanner.useDelimiter(";");

			for(int r = 0; r < 29215; r++) {
				for(int c = 0; c < 14; c++) {
					if(c != 13) {
						arrayOfData[r][c] = scanner.next();
					}
					else {
						arrayOfData[r][c] = scanner.nextLine();
						arrayOfData[r][c] = arrayOfData[r][c].replace(";", "");
					}
				}
			}
			scanner.close();
		} catch (FileNotFoundException e) {
			System.out.println("File not found! Make sure it's in: " + System.getProperty("user.dir"));
			e.printStackTrace();
		}

		return arrayOfData;
	}

	public boolean isValidCodepoint(int codepoint) {
		return codepoint > 0 || codepoint <= 1114111;
	}

	public String hexToBin(String b) {
		return new BigInteger(b, 16).toString(2);
	}
	public boolean isValidUtf8Byte (String b) {

		String binString = hexToBin(b);
		//	     1 pair:
		//	         [0-7] [0-f]
		if (b.length() == 2) {
			String firstHexDigit = binString.substring(0, 4);
			int integerRep1 = Integer.parseInt(firstHexDigit);
			if (integerRep1 > 0 && integerRep1 < 0111) {
				return true;
			} else {
				throw new IllegalArgumentException("First digit is not somewhere from 0-7");
			}
		} 
		//	     2 pairs:
		//	         [c-d] [2-f], [8-b] [0-f]
		else if (b.length() == 4) {
			String firstHexDigit = binString.substring(0, 4);
			int integerRep1 = Integer.parseInt(firstHexDigit);
			if (integerRep1 > 1100 && integerRep1 < 1101) {
				String secondHexDigit = binString.substring(4, 8);
				int integerRep2 = Integer.parseInt(secondHexDigit);
				if (integerRep2 > 0010 && integerRep2 < 1111) {
					String thirdHexDigit = binString.substring(8, 12);
					int integerRep3 = Integer.parseInt(thirdHexDigit);
					if (integerRep3 > 1000 && integerRep3 < 1011) {
						return true;
					} else {
						throw new IllegalArgumentException("Third digit is not somewhere from 8-b");
					}
				} else {
					throw new IllegalArgumentException("Second digit is not somewhere from 2-f");
				}
			} else {
				throw new IllegalArgumentException("Third digit is not somewhere from c-d");
			}
		}

		//	     3 pairs:
		//	         e [0-7], [a-b] [0-f], [8-b] [0-f]

		else if (b.length() == 6) {
			String firstHexDigit = binString.substring(0, 4);
			int integerRep1 = Integer.parseInt(firstHexDigit);
			if (integerRep1 == 1110) {
				String secondHexDigit = binString.substring(4, 8);
				int integerRep2 = Integer.parseInt(secondHexDigit);
				if (integerRep2 > 0000 && integerRep2 < 0111) {
					String thirdHexDigit = binString.substring(8, 12);
					int integerRep3 = Integer.parseInt(thirdHexDigit);
					if (integerRep3 > 1010 && integerRep3 < 1011) {
						String fifthHexDigit = binString.substring(16, 20);
						int integerRep5 = Integer.parseInt(fifthHexDigit);
						if (integerRep5 > 1000 && integerRep5 < 1011) {
							return true;
						} else {
							throw new IllegalArgumentException("Fifth digit is not somewhere from 8-b");
						}
					} else {
						throw new IllegalArgumentException("Third digit is not somewhere from a-b");
					}
				} else {
					throw new IllegalArgumentException("Second digit is not somewhere from 0-7");
				}
			} else {
				throw new IllegalArgumentException("First digit is not e");
			}
		} 

		// 4 pairs:
		// f [0-7], [9-b] [0-f], [8-b] [0-f], [8-b] [0-f]
		else if (b.length() == 8) {
			String firstHexDigit = binString.substring(0, 4);
			int integerRep1 = Integer.parseInt(firstHexDigit);
			if (integerRep1 == 1111) {
				String secondHexDigit = binString.substring(4, 8);
				int integerRep2 = Integer.parseInt(secondHexDigit);
				if (integerRep2 > 0000 && integerRep2 < 0111) {
					String thirdHexDigit = binString.substring(8, 12);
					int integerRep3 = Integer.parseInt(thirdHexDigit);
					if (integerRep3 > 1001 && integerRep3 < 1011) {
						String fifthHexDigit = binString.substring(16, 20);
						int integerRep5 = Integer.parseInt(fifthHexDigit);
						if (integerRep5 > 1000 & integerRep5 < 1011) {
							String seventhHexDigit = binString.substring(24, 28);
							int integerRep7 = Integer.parseInt(seventhHexDigit);
							if (integerRep7 > 1000 && integerRep7 < 1011) {
								return true;
							} else {
								throw new IllegalArgumentException("Seventh digit is not somewhere from 8-b");
							}
						} else {
							throw new IllegalArgumentException("Fifth digit is not somewhere from 8-b");
						}
					} else {
						throw new IllegalArgumentException("Third digit is not somewhere from 9-b");
					}
				} else {
					throw new IllegalArgumentException("Second digit is not somewhere from 0-7");
				}
			} else {
				throw new IllegalArgumentException("First digit is not f");
			}
		}
		return false;
	}

	public String convertToUtf8Hex(int codepoint) {
		String str = Long.toBinaryString(codepoint);

		int length = str.length();
		if(length < 8)
		{
			String utf = String.format("%08d", Integer.parseInt(str));

			String[] utfArray = {utf.substring(0, 8)};
			String totalStrHex = "";
			for(int i = 0; i < utfArray.length; i++){
				int decimal = Integer.parseInt(utfArray[i], 2);
				String hexStr = Integer.toString(decimal, 16);
				totalStrHex = totalStrHex + hexStr;
			}
			return totalStrHex;
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
			return totalStrHex;
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
			return totalStrHex;
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
			return totalStrHex;
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
			return totalStrHex;
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
			return totalStrHex;
		}
		else
		{
			throw new IllegalArgumentException("Invalid Codepoint");
		}
	}
}