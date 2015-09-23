package edu.andover.elee;

import static org.junit.Assert.*;

import java.util.Arrays;

import org.junit.Test;

public class EncodingHelperCharTest {

	@Test
	public void toUtf8BytesShouldReturnCorrectByteSequence() {
		EncodingHelperChar encodingHelperChar = new EncodingHelperChar(49792);
		byte[] byteArray = encodingHelperChar.toUtf8Bytes();
		byte[] byteArrayValid = {(byte)0xC2, (byte)0x80};

		assertArrayEquals(byteArrayValid, byteArray);
	}

	@Test
	public void setCodepointShouldThrowInvalidCodepoint() {
		try {
			EncodingHelperChar encodingHelperChar = new EncodingHelperChar(123);
			encodingHelperChar.setCodepoint(1114112);
			fail("Setting an invalid codepoint did not throw");
		} catch (IllegalArgumentException e) {
			// :)
		}
	}

	@Test
	public void overlongIntSequenceShouldThrow() {
		try {
			EncodingHelperChar encodingHelperChar = new EncodingHelperChar(49597);
			fail("Did not throw invalid byte sequence overlong");
		} catch (IllegalArgumentException e){
			// :]
		}
	}

	@Test
	public void overlongNullIntSequenceShouldThrow() {
		try {
			EncodingHelperChar encodingHelperChar = new EncodingHelperChar(49280);
			fail("Did not throw invalid byte sequence null overlong");
		} catch (IllegalArgumentException e){
			// :]
		}
	}

	@Test
	public void overlongByteSequenceShouldThrow() {
		try {
			byte[] byteArray = {(byte)0xC1, (byte)0xBD};
			EncodingHelperChar encodingHelperChar = new EncodingHelperChar(byteArray);
			fail("Did not throw invalid byte sequence overlong");
		} catch (IllegalArgumentException e){
			// :]
		}
	}

	@Test
	public void overlongNullByteSequenceShouldThrow() {
		try {
			byte[] byteArray = {(byte)0xC0, (byte)0x80};
			EncodingHelperChar encodingHelperChar = new EncodingHelperChar(byteArray);
			fail("Did not throw the null overlong");
		} catch (IllegalArgumentException e) {
			// :]
		}
	}

	@Test
	public void getCharacterNameShouldReturnCorrectName() {
		EncodingHelperChar encodingHelperChar = new EncodingHelperChar(40989);

		assertEquals("YI SYLLABLE BIEP", encodingHelperChar.getCharacterName());
	}

	@Test
	public void getCharacterNameCodepoint0000ShouldBeControlNull() {
		EncodingHelperChar encodingHelperChar = new EncodingHelperChar(0000);
		String specific = encodingHelperChar.getCharacterName();
		assertEquals("<control> NULL", specific);
	}

	@Test
	public void getCharacterNameCodepoint1FShouldBeControlInformationSeperatorOne() {
		EncodingHelperChar encodingHelperChar = new EncodingHelperChar(0x1F);
		String specific = encodingHelperChar.getCharacterName();
		assertEquals("<control> INFORMATION SEPARATOR ONE", specific);
	}

	@Test
	public void getCharacterNameCodepoint0020ShouldBeSpace() {
		EncodingHelperChar encodingHelperChar = new EncodingHelperChar(0x20);
		assertEquals("SPACE", encodingHelperChar.getCharacterName());
	}

	//Error Because .toCodepointString does not return proper value because
	//Constructor is not valid. Therefore, the indexes in test.substring(0,2) will be out of bounds.
	//For a working EncodingHelperChar, this test should be valid (Not an Error).
	@Test
	public void toCodepointStringShouldBeginWithUPlus() {
		EncodingHelperChar encodingHelperChar = new EncodingHelperChar(123);
		String test = encodingHelperChar.toCodepointString();

		assertEquals("U+", test.substring(0, 2));
	}

	// Passes right now because EncodingHelperChar's .toCodepointString returns an empty string
	// which length is < 8.
	@Test
	public void toCodepointStringShouldNotReturnStringLongerThanEightDigits() {
		EncodingHelperChar encodingHelperChar = new EncodingHelperChar(123);
		String returnedStatement = encodingHelperChar.toCodepointString();

		boolean lessThanEightDigits = returnedStatement.length() < 8;


		assertTrue(lessThanEightDigits);
	}

	@Test
	public void toCodepointStringShouldNotReturnStringLessThanTwoDigits() {
		EncodingHelperChar encodingHelperChar = new EncodingHelperChar(123);
		String returnedStatement = encodingHelperChar.toCodepointString();

		boolean moreThanTwoDigits = returnedStatement.length() > 2;

		assertTrue(moreThanTwoDigits);
	}

	@Test
	public void toCodepointStringShouldNotBeEmpty() {
		EncodingHelperChar encodingHelperChar = new EncodingHelperChar(123);
		String temp = encodingHelperChar.toCodepointString();

		assertFalse(temp.isEmpty());
	}

	@Test
	public void toUtf8BytesShouldHaveThreeHexPairsForE() {
		EncodingHelperChar encodingHelperChar = new EncodingHelperChar(31780);
		byte[] bytes = encodingHelperChar.toUtf8Bytes();

		assertTrue(bytes.length == 3);
	}

	@Test
	public void toUtf8StringShouldBeEqualProperReturn() {
		EncodingHelperChar encodingHelperChar = new EncodingHelperChar(123);
		String utf8String = encodingHelperChar.toUtf8String();

		assertEquals("\\x7B", utf8String);
	}

	//Error Because .toUtf8String does not return proper value because
	//Constructor is not valid. Therefore, the indexes in test.substring(0,2) will be out of bounds.
	//For a working EncodingHelperChar, this test should be valid (Not an Error).
	@Test
	public void toUtf8StringShouldBeSplitBySlashSlashXAtFirstIndex() {
		EncodingHelperChar encodingHelperChar = new EncodingHelperChar(1461);
		String utf8String = encodingHelperChar.toUtf8String();
		//   1461 = \\x05\\xB5 = 0101 1011 0101 -ToUtf8> 11010110 10110101 > D6 B5
		assertEquals("\\x", utf8String.substring(0, 2));
	}

	//Error Because .toUtf8String does not return proper value because
	//Constructor is not valid. Therefore, the indexes in test.substring(4,6) will be out of bounds.
	//For a working EncodingHelperChar, this test should be valid (Not an Error).
	@Test
	public void toUtf8StringShouldBeSplitBySlashSlashXAtSecondIndex() {
		EncodingHelperChar encodingHelperChar = new EncodingHelperChar(1461);
		String utf8String = encodingHelperChar.toUtf8String();
		//   1461 = \\x05\\xB5 = 0101 1011 0101 -ToUtf8> 11010110 10110101 > D6 B5
		assertEquals("\\x", utf8String.substring(4, 6));
	}

	//Error Because .toUtf8String does not return proper value because
	//Constructor is not valid. Therefore, the indexes in test.substring(0,2) will be out of bounds.
	//For a working EncodingHelperChar, this test should be valid (Not an Error).
	@Test
	public void toUtf8StringShouldNotBeginWithUPlus() {
		EncodingHelperChar encodingHelperChar = new EncodingHelperChar(123);
		String test = encodingHelperChar.toUtf8String();

		assertNotEquals("U+", test.substring(0, 2));
	}

	@Test
	public void encodingHelperCharParameterShouldNotBeInRangeForNegative() {
		int x = -1;
		try {
			EncodingHelperChar encodingHelperChar = new EncodingHelperChar(x);
			fail("Out of range Parameter did not throw");
		} catch (IllegalArgumentException expectedException) {
			//Good!
		}
	}

	@Test
	public void encodingHelperCharIntParameterShouldBeInRangeForPositive() {
		int x = 1114112;
		try {
			EncodingHelperChar encodingHelperChar = new EncodingHelperChar(x);
			fail("Out of range Parameter did not throw");
		} catch (IllegalArgumentException expectedException) {
			//Good!
		}
	}

	@Test
	public void encodingHelperCharByteParameterShouldBeInRangeForPositive() {
		byte[] bytes = {(byte)0x11, 0x00, 0x00};
		try {
			EncodingHelperChar encodingHelperChar = new EncodingHelperChar(bytes);
			fail("Out of range Parameter did not throw");
		} catch (IllegalArgumentException expectedException) {
			//Good!
		}
	}

	@Test
	public void encodingHelperCharIntShouldSetCodepoint() {
		int codepoint = 123;
		EncodingHelperChar encodingHelperChar = new EncodingHelperChar(codepoint);

		assertEquals(codepoint, encodingHelperChar.getCodepoint());
	}

	@Test
	public void invalidBytesInConstructorShouldThrowException() {
		byte[] byteArray = {(byte)0xA7};
		try {
			EncodingHelperChar encodingHelperChar = new EncodingHelperChar(byteArray);
			fail("Did not throw invalid byte");
		} catch (IllegalArgumentException expectedException) {
			//Nothing
		}
	}

	@Test
	public void toUtf8StringShouldNotBeEmpty() {
		EncodingHelperChar encodingHelperChar = new EncodingHelperChar(123);
		String temp = encodingHelperChar.toUtf8String();

		assertFalse(temp.isEmpty());
	}

	@Test
	public void getCodepointForByteConstructorShouldReturnAppropriateCodepoint() {
		byte[] byteArray = {0x43};
		EncodingHelperChar encodingHelperChar = new EncodingHelperChar(byteArray);

		assertEquals(67, encodingHelperChar.getCodepoint());
	}

	@Test
	public void getCodepointForCharConstructorShouldReturnAppropriateCodepoint() {
		char ch = 'c';
		EncodingHelperChar encodingHelperChar = new EncodingHelperChar(ch);

		assertEquals(231, encodingHelperChar.getCodepoint());
	}

	@Test
	public void getCodepointForIntConstructorShouldReturnAppropriateCodepoint() {
		EncodingHelperChar encodingHelperChar = new EncodingHelperChar(123);
		int codepoint = encodingHelperChar.getCodepoint();

		assertEquals(codepoint, 123);
	}

	@Test
	public void setCodepointShouldSetCodepoint() {
		EncodingHelperChar encodingHelperChar = new EncodingHelperChar('c');
		int codepoint = 1234;

		encodingHelperChar.setCodepoint(codepoint);

		assertEquals(codepoint, encodingHelperChar.getCodepoint());
	}
}
