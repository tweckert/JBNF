package javax.bnf;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.util.Position;

/**
 * @author Thomas Weckert
 */
public class BnfSyntaxValidator {

	public boolean isValid(final String inputString, final BnfRule startRule,
						   final Map<String,List<String>> valuesBySymbol)
			throws BnfSyntaxException {
		
		Position startPosition = new Position(0);
		Position endPosition = new Position(0);
		
		boolean isValid = isValid(startPosition, endPosition, inputString, startRule.getFirstElement(), startRule, valuesBySymbol);
		isValid &= (endPosition.getPosition() == inputString.length());
		
		return isValid;
	}
	
	private boolean isValid(final Position startPosition, final Position endPosition, final String inputString,
							final BnfElement startElement, final BnfRule currentBnfRule,
							final Map<String, List<String>> valuesBySymbol) throws BnfSyntaxException {
		
		boolean isValid = false;
		
		// validate all the chained BNF elements for the current rule
		for (BnfElement currentBnfElement = startElement; currentBnfElement != null; currentBnfElement = currentBnfElement.getNext()) {
			
			if (BnfElementLink.OR.equals(startElement.getLink())) {
				
				isValid = false;
				for (BnfElement currentOredBnfElement = currentBnfElement; !isValid && currentOredBnfElement != null; currentOredBnfElement = currentOredBnfElement.getNext()) {
			
					// create a temporary BNF element in order to validate just this element
					BnfElement tempBnfElement = currentOredBnfElement.clone();
					tempBnfElement.setLink(BnfElementLink.DEFAULT);
					tempBnfElement.setNext(null);

					final Position tempEndPosition = new Position(startPosition);
					
					if (isValid = isValid(startPosition, tempEndPosition, inputString, tempBnfElement, currentBnfRule, valuesBySymbol)) {
						endPosition.setPosition(tempEndPosition.getPosition());
					}
				}
								
				return isValid;
			} else switch (currentBnfElement.getType()) {
			
				case SYMBOL_CALL:
					
					// the current BNF element is a call for another BNF rule, so validate this "derived" rule next
					BnfRule derivedRule = currentBnfElement.getRule();
					BnfElement firstElement = currentBnfElement.getContent();
					Position originalStartPosition = new Position(startPosition);
					
					if (!isValid(startPosition, endPosition, inputString, firstElement, derivedRule, valuesBySymbol)) {
						// the derived rule is invalid
						return false;
					}
					
					// store the contents of the derived rule for debugging purposes
					final String value = inputString.substring(originalStartPosition.getPosition(), endPosition.getPosition());

					List<String> symbolValues = null;
					if ((symbolValues = valuesBySymbol.get(derivedRule.getSymbol())) == null) {

						symbolValues = new ArrayList<String>();
						valuesBySymbol.put(derivedRule.getSymbol(), symbolValues);
					}
					
					symbolValues.add(value);
					
					// continue validating the input string from the end position
					startPosition.setPosition(endPosition.getPosition());
					break;
					
				case QUANTIFIER_ONCE_OR_NOT_AT_ALL:
					
					int counter = 0;
					for (counter = 0, isValid = true; isValid; counter++) {
						
						if (counter > 1) {
							return false;
						}
						
						isValid = isValid(startPosition, endPosition, inputString, currentBnfElement.getContent(), currentBnfElement.getContent().getRule(), valuesBySymbol);
					}
					
					break;
					
				case QUANTIFIER_ZERO_OR_MORE_TIMES:
									
					for (isValid = true; isValid;) {
						isValid = isValid(startPosition, endPosition, inputString, currentBnfElement.getContent(), currentBnfElement.getContent().getRule(), valuesBySymbol);
					}
					
					break;
					
				case GROUP:
					
					// simply validate the BNF element(s) within the group
					if (!isValid(startPosition, endPosition, inputString, currentBnfElement.getContent(), currentBnfElement.getContent().getRule(), valuesBySymbol)) {
						return false;
					}
					break;
					
				case TERMINAL:
					
					String terminal = currentBnfElement.getTerminal();
					for (int i = 0; i < terminal.length(); i++) {
						
						// (remaining) input string is shorter than the terminal string
						if ((startPosition.getPosition() + i) >= inputString.length()) {
							return false;
						}
						
						// compare the terminal string with the input string
						char inputChar = inputString.charAt(startPosition.getPosition() + i);
						char terminalChar = terminal.charAt(i);
						if (inputChar != terminalChar) {
							return false;
						}
					}
					
					endPosition.add(terminal.length());
					startPosition.setPosition(endPosition);
					break;	
					
				case SYMBOL_REF:
					throw new BnfSyntaxException("Undefined BNF symbol '" + String.valueOf(currentBnfElement.getSymbol()) + "' found!");
			}
		}
		
		return true;				
	}
	
}
