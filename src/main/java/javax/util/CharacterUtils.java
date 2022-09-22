package javax.util;

import javax.bnf.BnfConstants;


/**
 * @author Thomas Weckert
 */
public class CharacterUtils {

	public static boolean isNewLine(final Character c) {
		return (c == '\n' || c == '\r');
	}
	
	public static boolean isSpace(final Character c) {
		return (c == BnfConstants.SPACE_CHAR || c == '\t');
	}
	
	public static void getLinePosition(final CharSequence str, final int position,
									   final Position line, final Position linePosition) {
		
		line.setPosition(1);
		linePosition.setPosition(1);
		
		Character lastChar = null;
		int lastNewLinePos = 0;
		
		for (int i = 0; i < position && i < str.length(); i++) {

			final Character c = str.charAt(i);
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
