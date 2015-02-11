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
	
	public static void getLinePosition(CharSequence str, int position, Position line, Position linePosition) {
		
		line.setPosition(1);
		linePosition.setPosition(1);
		
		Character lastChar = null;
		int lastNewLinePos = 0;
		
		for (int i = 0; i < position && i < str.length(); i++) {
			
			Character c = str.charAt(i);
			if (c == '\r') {
				line.increment();
				lastNewLinePos = i;
			} else if (c == '\n' && lastChar != '\r') {
				line.increment();
				lastNewLinePos = i;
			}
			
			lastChar = c;
		}
		
		linePosition.setPosition(position - lastNewLinePos - 1);
	}
	
}
