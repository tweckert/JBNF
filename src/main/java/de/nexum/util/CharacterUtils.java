package de.nexum.util;

/**
 * @author <a href="mailto:thomas.weckert@nexum.de">Thomas Weckert</a>
 */
public class CharacterUtils {

	public static boolean isNewLine(Character c) {
		return (c == '\n' || c == '\r');
	}
	
	public static boolean isSpace(Character c) {
		return (c == ' ' || c == '\t');
	}
	
}
