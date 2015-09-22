package edu.andover.elee;

import static org.junit.Assert.*;

import java.util.Arrays;

import org.junit.Test;

public class EncodingHelperCharTest {

	@Test
	public void toUtf8BytesShouldReturnCorrectByteSequence() {
		EncodingHelperChar encodingHelperChar = new EncodingHelperChar(0x80);
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
	public void overlongByteSequenceShouldThrow() {
		try {
			byte[] byteArray = {(byte)0xC1, (byte)0xBD};
			EncodingHelperChar encodingHelperChar = new EncodingHelperChar(byteArray);
			fail("Did not throw valid byte sequence overlong");
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
		EncodingHelperChar encodingHelperChar = new EncodingHelperChar(0xA01D);

		assertEquals("YI SYLLABLE BIEP", encodingHelperChar.getCharacterName());
	}

	@Test
	public void getCharacterNameCodepoint0000ShouldBeControlNull() {
		EncodingHelperChar encodingHelperChar = new EncodingHelperChar(0000);
		String specific = encodingHelperChar.getCharacterName();
		assertEquals ("<control> NULL", specific);
	}

	@Test
	public void getCharacterNameCodepoint0012ShouldBeControlDeviceControlTwo() {
		EncodingHelperChar encodingHelperChar = new EncodingHelperChar(0x12);
		String specific = encodingHelperChar.getCharacterName();
		assertEquals ("<control> DEVICE CONTROL TWO", specific);
	}

	@Test
	public void getCharacterNameCodepoint0001FShouldBeIF1() {
		EncodingHelperChar encodingHelperChar = new EncodingHelperChar(0x1C); //TODO: Check
		String specific = encodingHelperChar.getCharacterName();
		assertEquals ("<control> INFORMATION SEPARATOR ONE", specific);
	}

	@Test
	public void toCodepointStringShouldBeginWithUPlusAndNotNull() {
		EncodingHelperChar encodingHelperChar = new EncodingHelperChar(123);
		String test = encodingHelperChar.toCodepointString();

		assertEquals("U+", test.substring(0, 2));
	}

	@Test
	public void toUtf8StringShouldBeEqualProperReturnAndNotNull() {
		EncodingHelperChar encodingHelperChar = new EncodingHelperChar(123);
		String utf8String = encodingHelperChar.toUtf8String();

		assertEquals("\\x7B", utf8String);
	}

	@Test
	public void toUtf8StringShouldBeSplitBySlashSlashXAtFirstIndex() {
		EncodingHelperChar encodingHelperChar = new EncodingHelperChar(1461);
		String utf8String = encodingHelperChar.toUtf8String();
		//   \\x05\\xB5
		assertEquals("\\x", utf8String.substring(0, 2));
	}
	
	@Test
	public void toUtf8StringShouldBeSplitBySlashSlashXAtSecondIndex() {
		EncodingHelperChar encodingHelperChar = new EncodingHelperChar(1461);
		String utf8String = encodingHelperChar.toUtf8String();
		//\\x05\\xB5
		assertEquals("\\x", utf8String.substring(4, 6));
	}

	@Test
	public void toUtf8StringShouldNotBeginWithUPlusAndNotNull() {
		EncodingHelperChar encodingHelperChar = new EncodingHelperChar(123);
		String test = encodingHelperChar.toUtf8String();

		assertNotEquals("U+", test.substring(0, 2));
	}

	@Test
	public void encodingHelperCharParameterShouldBeInRange() {
		int x = -1;
		try {
			EncodingHelperChar encodingHelperChar = new EncodingHelperChar(x);
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
	public void encodingHelperCharBytesCodepointShouldNotBeNullAndEqualByteArray() {
		byte[] byteArray = {0x43};
		EncodingHelperChar encodingHelperChar = new EncodingHelperChar(byteArray);

		assertEquals(67, encodingHelperChar.getCodepoint());
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
	public void encodingHelperCharCharCodepointShouldNotBeNullAndEqualParameter() {
		char ch = 'c';
		EncodingHelperChar encodingHelperChar = new EncodingHelperChar(ch);

		assertEquals(231, encodingHelperChar.getCodepoint());
	}

	@Test
	public void encodingHelperCharIntCodepointShouldNotBeNullAndEqualInt() {
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
