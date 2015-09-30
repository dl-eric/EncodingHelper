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
 * 
 * Implementation by Eric and Diana
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
		if(isValidUtf8ByteArray(utf8Bytes)) {
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
		else
			throw new IllegalArgumentException("Illegal Byte Sequence");
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
		String utf8Hex = convertToUtf8HexString(codepoint);

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

			if (codepoint == unicodePoint 
					&& unicodeData[r][1].equals("<control>")){
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
	 * ************************************************************************
	 * Helper Functions
	 * ************************************************************************
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
			System.out.println("File not found! Make sure it's in: " 
					+ System.getProperty("user.dir"));
			e.printStackTrace();
		}

		return arrayOfData;
	}

	public boolean isValidCodepoint(int codepoint) {
		return (codepoint >= 0 && codepoint <= 1114111);
	}

	public String hexToBin(String b) {
		return new BigInteger(b, 16).toString(2);
	}
	public boolean isValidUtf8ByteArray (byte[] b) {
		int expectedLength;

		//Check length
		if      (b.length == 0)
			return false;
		else if ((b[0] & 0b10000000) == 0b00000000) 
			expectedLength = 1;
		else if ((b[0] & 0b11100000) == 0b11000000) 
			expectedLength = 2;
		else if ((b[0] & 0b11110000) == 0b11100000) 
			expectedLength = 3;
		else if ((b[0] & 0b11111000) == 0b11110000) 
			expectedLength = 4;
		else if ((b[0] & 0b11111100) == 0b11111000) 
			expectedLength = 5;
		else if ((b[0] & 0b11111110) == 0b11111100) 
			expectedLength = 6;
		else    
			return false;

		if (expectedLength != b.length) 
			return false;

		//Looks for 10xxxxxx headers
		for (int i = 1; i < b.length; i++) {
			if ((b[i] & 0b11000000) != 0b10000000)
				return false;
		}

		//Check for overlong
		switch(expectedLength) {
		case 2:
			if((b[0] & 0b11011110) == 0b11000000)
				return false;
			return !((b[0] & 0b11011111) == 0b11000000);
		case 3:
			if((b[0] & 0b11101111) == 0b11100000){
				if((b[1] & 0b10100000) == 0b10000000){
					return false;
				}
			}
		case 4:
			if((b[0] & 0b11110111) == 0b11110000) {
				if((b[1] & 0b10110000) == 0b10000000) {
					return false;
				}
			}
		case 5:
			if((b[0] & 0b11111011) == 0b11111000) {
				if((b[1] & 0b10111000) == 0b10000000) {
					return false;
				}
			}
		case 6:
			if((b[0] & 0b11111101) == 0b11111100) {
				if((b[1] & 0b10111100) == 0b10000000) {
					return false;
				}
			}
		}
		return true;
	}

	public String convertToUtf8HexString(int codepoint) {
		char c = (char)codepoint;
		String temp = c + "";
		byte[] tempByteArray = temp.getBytes(Charset.forName("UTF-8"));
		
		return javax.xml.bind.DatatypeConverter.printHexBinary(tempByteArray);
	}
	
	public byte[] convertToUtf8HexByteArray(int codepoint) {
		char c = (char)codepoint;
		String temp = c + "";
		return temp.getBytes(Charset.forName("UTF-8"));
	}
}