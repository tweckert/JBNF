package de.nexum.bnf;

import de.nexum.util.Position;

/**
 * @author <a href="mailto:thomas.weckert@nexum.de">Thomas Weckert</a>
 */
public class BnfSyntaxValidator {

	public boolean checkSyntax(String inputString, BnfRule firstBnfRule) throws BnfSyntaxValidationException {
		return checkSyntax(new Position(0), new Position(0), inputString, firstBnfRule.getFirstRuleElement(), firstBnfRule);
	}
	
	private boolean checkSyntax(Position startPosition, Position endPosition, String inputString, BnfElement startBnfElement, BnfRule currentBnfRule) throws BnfSyntaxValidationException {		
		
		for (BnfElement currentBnfElement = startBnfElement; currentBnfElement != null; currentBnfElement = currentBnfElement.getNext()) {
			
			if (BnfElementLink.OR.equals(startBnfElement.getLink())) {
				
				boolean foundTerminal = false;
				for (BnfElement currentOredBnfElement = currentBnfElement; !foundTerminal && BnfElementLink.OR.equals(currentOredBnfElement.getLink()); currentOredBnfElement = currentOredBnfElement.getNext()) {
					
					BnfElement tempBnfElement = currentOredBnfElement.clone();
					tempBnfElement.setLink(BnfElementLink.DEFAULT);
					tempBnfElement.setNext(null);
					
					Position tempEndPosition = new Position(startPosition);
					
					if (foundTerminal = checkSyntax(startPosition, tempEndPosition, inputString, tempBnfElement, currentBnfRule)) {						
						endPosition.setPosition(tempEndPosition.getPosition());
					}
				}
								
				return foundTerminal;
			} else switch (currentBnfElement.getType()) {
			
				case SYMBOL_CALL:
					
					BnfRule calledRule = currentBnfElement.getRule();
					BnfElement firstElement = currentBnfElement.getContent();
					Position originalStartPosition = new Position(startPosition);
					
					if (!checkSyntax(startPosition, endPosition, inputString, firstElement, calledRule)) {
						return false;
					}
					
					String value = inputString.substring(originalStartPosition.getPosition(), endPosition.getPosition());
					calledRule.addValue(value);
					
					startPosition.setPosition(endPosition.getPosition());
					break;
					
				case QUANTIFIER_ONCE_OR_NOT_AT_ALL:
					break;
					
				case QUANTIFIER_ZERO_OR_MORE_TIMES:
					
					Position tempStartPosition = new Position(startPosition);
					boolean foundTerminal = false;
					
					do {
						foundTerminal = checkSyntax(tempStartPosition, endPosition, inputString, currentBnfElement.getContent(), currentBnfElement.getContent().getRule());
					} while (foundTerminal);
					
					break;
					
				case GROUP:
					return checkSyntax(startPosition, endPosition, inputString, currentBnfElement.getContent(), currentBnfRule);
					
				case TERMINAL:
					
					String terminal = currentBnfElement.getTerminal();
					for (int i = 0; i < terminal.length(); i++) {
												
						char inputChar = inputString.charAt(startPosition.getPosition());
						char terminalChar = terminal.charAt(i);
						if (inputChar != terminalChar) {
							return false;
						}
					}
					
					endPosition.increment(terminal.length());
					startPosition.setPosition(endPosition);
					break;	
					
				case SYMBOL_REF:
					return false;
			}
		}
		
		return true;				
	}
	
}
