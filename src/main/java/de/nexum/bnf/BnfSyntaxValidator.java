package de.nexum.bnf;

import java.util.ArrayList;
import java.util.List;

import de.nexum.util.Position;

/**
 * @author <a href="mailto:thomas.weckert@nexum.de">Thomas Weckert</a>
 */
public class BnfSyntaxValidator {
	
	public BnfSyntaxValidator() {
		
		super();
	}

	public boolean checkSyntax(String inputString, BnfRule firstBnfRule) throws BnfSyntaxValidationException {
		return checkSyntax(new Position(), new Position(), inputString, firstBnfRule.getFirstRuleElement(), firstBnfRule, new ArrayList<String>());
	}
	
	private boolean checkSyntax(Position startPosition, Position endPosition, String inputString, BnfElement startBnfElement, BnfRule currentBnfRule, List<String> values) throws BnfSyntaxValidationException {
		
		Position tmpPosition = new Position(startPosition);
		
		for (BnfElement currentBnfElement = startBnfElement; currentBnfElement != null; currentBnfElement = currentBnfElement.getNext()) {
			
			if (BnfElementLink.OR.equals(startBnfElement.getLink())) {
				
				boolean foundTerminal = false;
				for (BnfElement currentOredBnfElement = currentBnfElement; !foundTerminal && BnfElementLink.OR.equals(currentOredBnfElement.getLink()); currentOredBnfElement = currentOredBnfElement.getNext()) {
					
					BnfElement tempBnfElement = currentOredBnfElement.clone();
					tempBnfElement.setLink(BnfElementLink.DEFAULT);
					tempBnfElement.setNext(null);
					
					if (foundTerminal = checkSyntax(startPosition, tmpPosition, inputString, tempBnfElement, currentBnfRule, values)) {
						endPosition.setPosition(tmpPosition.getPosition());
					}
				}
								
				return foundTerminal;
			} else switch (currentBnfElement.getType()) {
			
				case SYMBOL_CALL:
					if (!checkSyntax(startPosition, endPosition, inputString, currentBnfElement.getContent(), currentBnfElement.getContent().getRule(), values)) {
						return false;
					}
					
					String value = inputString.substring(startPosition.getPosition(), endPosition.getPosition());
					currentBnfRule.addValue(value);
					break;
				case QUANTIFIER_ONCE_OR_NOT_AT_ALL:
					break;
				case QUANTIFIER_ZERO_OR_MORE_TIMES:
					break;
				case GROUP:
					return checkSyntax(startPosition, endPosition, inputString, currentBnfElement.getContent(), currentBnfRule, values);
				case TERMINAL:
					
					String terminal = currentBnfElement.getTerminal();
					for (int i = 0; i < terminal.length(); i++) {
						
						char inputChar = inputString.charAt(startPosition.getPosition() + i);
						char terminalChar = terminal.charAt(i);
						if (inputChar != terminalChar) {
							return false;
						}
					}
					
					endPosition.increment(terminal.length());
					break;
				case SYMBOL_REF:
					return false;
			}
		}
		
		return true;				
	}
	
}
