package edu.andover.elee;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

public class EncodingHelperParser {
	char[] charArrayOfStringData;
	String stringData;
	String[] codepointData;
	String[] utf8Data;
	EncodingHelperChar[] ehcArray;

	/*
	 * Constructors
	 */
	public EncodingHelperParser(String s) {
		stringData = s;
	}
	public EncodingHelperParser(String input, String data) {
		switch(input) {
		case "codepoint": 
			String[] dataArray = data.split(" ");
			for(int j = 0; j < dataArray.length; j++) {
				dataArray[j] = dataArray[j].substring(2, dataArray[j].length());
			}

			ehcArray = new EncodingHelperChar[dataArray.length];

			stringData = "";
			for(int i = 0; i < ehcArray.length; i++) {
				int codepoint = Integer.parseInt(dataArray[i], 16);
				ehcArray[i] = new EncodingHelperChar(codepoint);
				try {
					stringData = stringData + new String(ehcArray[i].toUtf8Bytes(),
							"UTF-8");
				} catch (UnsupportedEncodingException e) {
					System.out.println("Internal Error: "
							+ "UnsupportedEncodingException");
					e.printStackTrace();
				}
			}
			break;
		case "utf8":
			String[] temp = data.split("\\\\x");
			String[] utf8Numbers = new String[temp.length - 1];
			int numOfHexPairs = 0;
			for(int i = 0; i < temp.length; i++){
				if(!temp[i].equals("")) {
					utf8Numbers[numOfHexPairs] = temp[i];
					numOfHexPairs++;
				}
			}

			ArrayList<EncodingHelperChar> ehcList = new ArrayList<>();
			byte[] utf8ByteArray = new byte[numOfHexPairs];

			//Fill utf8ByteArray
			for(int j = 0; j < numOfHexPairs; j++) {
				utf8ByteArray[j] = (byte)(Integer.parseInt(utf8Numbers[j], 16));
			}
			ArrayList<Byte> utf8ByteList = new ArrayList<>();
			//Make EncodingHelperChars
			for(int k = 0; k < utf8ByteArray.length; k++)
			{
				utf8ByteList.add(utf8ByteArray[k]);
				Byte[] tempByteArray = new Byte[utf8ByteList.size()];
				tempByteArray = utf8ByteList.toArray(tempByteArray);

				byte[] tempPrimByteArray = new byte[utf8ByteList.size()];
				int l = 0;
				for(Byte b: tempByteArray)
					tempPrimByteArray[l++] = b.byteValue();
				if(isValidUtf8ByteArray(tempPrimByteArray)) {
					ehcList.add(new EncodingHelperChar(tempPrimByteArray));
					utf8ByteList.clear();
				}
			}
			ehcArray = new EncodingHelperChar[ehcList.size()];
			ehcArray = ehcList.toArray(ehcArray);
			
			stringData = "";
			for(int r = 0; r < ehcArray.length; r++) {
				try {
					stringData = stringData 
							+ new String(ehcArray[r].toUtf8Bytes(),
							"UTF-8");
				} catch (UnsupportedEncodingException e) {
					System.out.println("Internal Error: "
							+ "UnsupportedEncodingException");
					e.printStackTrace();
				}
			}
			break;
		case "string":
			stringData = data;
			char[] tempCharArray = data.toCharArray();
			ehcArray = new EncodingHelperChar[tempCharArray.length];
			for(int i = 0; i < tempCharArray.length; i++) {
				ehcArray[i] = new EncodingHelperChar(tempCharArray[i]);
			}
			break;
		}
	}
	public void printSummary() {
		String stringAppend = "";
		String codepointAppend = "";
		String name = "";
		String utf8Append = "";

		if(stringData.length() == 1) {
			try {
				stringAppend = stringAppend 
						+ new String(ehcArray[0].toUtf8Bytes(), "UTF-8");
			} catch (UnsupportedEncodingException e) {
				System.out.println("Internal Error: "
						+ "UnsupportedEncodingException");
				e.printStackTrace();
			}
			name = ehcArray[0].getCharacterName();
			codepointAppend = codepointAppend + ehcArray[0].toCodepointString() + " ";
			utf8Append = utf8Append + ehcArray[0].toUtf8String();

			System.out.println("String: " + stringAppend);
			System.out.println("Code points: " + codepointAppend);
			System.out.println("Name: " + name);
			System.out.println("UTF-8: " + utf8Append);
		} else {
			for(int i = 0; i < ehcArray.length; i++) {
				try {
					stringAppend = stringAppend 
							+ new String(ehcArray[i].toUtf8Bytes(), "UTF-8");
				} catch (UnsupportedEncodingException e) {
					System.out.println("Internal Error: "
							+ "UnsupportedEncodingException");
					e.printStackTrace();
				}
				codepointAppend = codepointAppend + ehcArray[i].toCodepointString() + " ";
				utf8Append = utf8Append + ehcArray[i].toUtf8String();
			}
			System.out.println("String: " + stringAppend);
			System.out.println("Code points: " + codepointAppend);
			System.out.println("UTF-8: " + utf8Append);
		}
	}

	/*
	 * Three printSummary method for if only one argument: data
	 */
	public String printSummaryFromCharArray() {
		char[] charArray = stringData.toCharArray();
		if(charArray.length == 1) {
			return printSummaryOfCharacter(charArray[0]);
		} else {
			return printSummaryOfCharArray(charArray);
		}
	}

	public String printSummaryOfCharacter(char c) {
		EncodingHelperChar ehc = new EncodingHelperChar(c);
		System.out.println("Character: " + c);
		System.out.println("Code point: " + ehc.toCodepointString());
		System.out.println("Name: " + ehc.getCharacterName());
		System.out.println("UTF-8: " + ehc.toUtf8String());
		
		return c + ehc.toCodepointString() + ehc.getCharacterName() 
		+ ehc.toUtf8String();
	}

	public String printSummaryOfCharArray(char[] c) {
		String characterAppend = "";
		String codepointAppend = "";
		String utf8Append = "";

		for(int i = 0; i < c.length; i++) {
			EncodingHelperChar ehc = new EncodingHelperChar(c[i]);
			characterAppend = characterAppend + c[i];
			codepointAppend = codepointAppend + ehc.toCodepointString() + " ";
			utf8Append = utf8Append + ehc.toUtf8String();
		}
		System.out.println("String: " + characterAppend);
		System.out.println("Code points: " + codepointAppend);
		System.out.println("UTF-8: " + utf8Append);
		return characterAppend + codepointAppend + utf8Append;
	}

	/*
	 * ************************************************************************
	 */

	public String printCodepoint() {
		String codepointAppend = "";

		for(int i = 0; i < ehcArray.length; i++) {
			codepointAppend = codepointAppend + ehcArray[i].toCodepointString() 
					+ " ";
		}
		System.out.println(codepointAppend);
		return codepointAppend;
	}

	public String printString() {
		String stringAppend = "";
		for(int i = 0; i < ehcArray.length; i++) {
			EncodingHelperChar ehc = ehcArray[i];
			try {
				stringAppend = stringAppend 
						+ new String(ehc.toUtf8Bytes(), "UTF-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}

		System.out.println(stringAppend);
		return stringAppend;
	}

	public String printStringFromCharArray() {
		String stringAppend = "";
		charArrayOfStringData = stringData.toCharArray();
		for(int i = 0; i < charArrayOfStringData.length; i++) {
			stringAppend = stringAppend + charArrayOfStringData[i];
		}
		System.out.println(stringAppend);
		return stringAppend;
	}

	public String printUtf8() {
		String utf8Append = "";

		for(int i = 0; i < ehcArray.length; i++) {
			utf8Append = utf8Append + ehcArray[i].toUtf8String();
		}
		System.out.println(utf8Append);
		return utf8Append;
	}

	private boolean isValidUtf8ByteArray (byte[] b) {
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

}
