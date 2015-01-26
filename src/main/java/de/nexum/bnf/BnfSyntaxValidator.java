package de.nexum.bnf;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import de.nexum.util.Position;

/**
 * @author <a href="mailto:thomas.weckert@nexum.de">Thomas Weckert</a>
 */
public class BnfSyntaxValidator {

	public boolean checkSyntax(String inputString, BnfRule firstBnfRule, Map<String,List<String>> valuesBySmbol) throws BnfSyntaxValidationException {
		return checkSyntax(new Position(0), new Position(0), inputString, firstBnfRule.getFirstRuleElement(), firstBnfRule, valuesBySmbol);
	}
	
	private boolean checkSyntax(Position startPosition, Position endPosition, String inputString, BnfElement startBnfElement, BnfRule currentBnfRule, Map<String, List<String>> valuesBySmbol) throws BnfSyntaxValidationException {		
		
		boolean isValid = false;
		
		// validate all the chained BNF elements for the current rule
		for (BnfElement currentBnfElement = startBnfElement; currentBnfElement != null; currentBnfElement = currentBnfElement.getNext()) {
			
			if (BnfElementLink.OR.equals(startBnfElement.getLink())) {
				
				isValid = false;
				for (BnfElement currentOredBnfElement = currentBnfElement; !isValid && currentOredBnfElement != null; currentOredBnfElement = currentOredBnfElement.getNext()) {
			
					// create a temporary BNF element in order to validate just this element
					BnfElement tempBnfElement = currentOredBnfElement.clone();
					tempBnfElement.setLink(BnfElementLink.DEFAULT);
					tempBnfElement.setNext(null);
					
					Position tempEndPosition = new Position(startPosition);
					
					if (isValid = checkSyntax(startPosition, tempEndPosition, inputString, tempBnfElement, currentBnfRule, valuesBySmbol)) {						
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
					
					if (!checkSyntax(startPosition, endPosition, inputString, firstElement, derivedRule, valuesBySmbol)) {
						// the derived rule is invalid
						return false;
					}
					
					// store the contents of the derived rule for debugging purposes
					String value = inputString.substring(originalStartPosition.getPosition(), endPosition.getPosition());

					List<String> symbolValues = null;
					if ((symbolValues = valuesBySmbol.get(derivedRule.getSymbol())) == null) {

						symbolValues = new ArrayList<String>();
						valuesBySmbol.put(derivedRule.getSymbol(), symbolValues);
					}
					
					symbolValues.add(value);
					
					// continue validating the input string from the end position
					startPosition.setPosition(endPosition.getPosition());
					break;
					
				case QUANTIFIER_ONCE_OR_NOT_AT_ALL:
					
					int counter = 0;
					for (counter = 0, isValid = true; isValid;) {
						isValid = checkSyntax(startPosition, endPosition, inputString, currentBnfElement.getContent(), currentBnfElement.getContent().getRule(), valuesBySmbol);
					}
					
					if (counter > 1) {
						return false;
					}
					
					break;
					
				case QUANTIFIER_ZERO_OR_MORE_TIMES:
									
					for (isValid = true; isValid;) {
						isValid = checkSyntax(startPosition, endPosition, inputString, currentBnfElement.getContent(), currentBnfElement.getContent().getRule(), valuesBySmbol);
					}
					
					break;
					
				case GROUP:
					
					// simply validate the BNF element(s) within the group
					if (!checkSyntax(startPosition, endPosition, inputString, currentBnfElement.getContent(), currentBnfElement.getContent().getRule(), valuesBySmbol)) {
						return false;
					}
					break;
					
				case TERMINAL:
					
					// compare the BNF terminal character by character with the input string
					String terminal = currentBnfElement.getTerminal();
					for (int i = 0; i < terminal.length(); i++) {
												
						char inputChar = inputString.charAt(startPosition.getPosition());
						char terminalChar = terminal.charAt(i);
						if (inputChar != terminalChar) {
							return false;
						}
					}
					
					endPosition.add(terminal.length());
					startPosition.setPosition(endPosition);
					break;	
					
				case SYMBOL_REF:
					throw new BnfSyntaxValidationException("Undefined BNF symbol '" + String.valueOf(currentBnfElement.getSymbol()) + "' found!");
			}
		}
		
		return true;				
	}
	
}
