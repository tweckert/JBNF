package de.nexum.bnf;

/**
 * @author <a href="mailto:thomas.weckert@nexum.de">Thomas Weckert</a>
 */
public class BnfSyntaxValidator {
	
	private int pos;
	
	public BnfSyntaxValidator() {
		
		super();
		this.pos = 0;
	}

	public boolean checkSyntax(String inputString, BnfRule firstBnfRule) throws BnfSyntaxValidationException {
		pos = 0;
		return checkSyntax(inputString, firstBnfRule.getFirstRuleElement(), new StringBuffer(), firstBnfRule);
	}
	
	private boolean checkSyntax(String inputString, BnfElement startBnfElement, StringBuffer writeBuffer, BnfRule currentBnfRule) throws BnfSyntaxValidationException {
		
		for (BnfElement currentBnfElement = startBnfElement; currentBnfElement != null; currentBnfElement = currentBnfElement.getNext()) {
			
			if (BnfElementLink.OR.equals(startBnfElement.getLink())) {
				
				boolean isOredValid = true;
				for (BnfElement currentOredBnfElement = currentBnfElement; BnfElementLink.OR.equals(currentOredBnfElement.getLink()); currentOredBnfElement = currentOredBnfElement.getNext()) {
					
					if (isOredValid) {
						
						BnfElement tempBnfElement = currentOredBnfElement.clone();
						tempBnfElement.setLink(BnfElementLink.DEFAULT);
						tempBnfElement.setNext(null);
						
						isOredValid = checkSyntax(inputString, tempBnfElement, writeBuffer, currentBnfRule);
					}
				}
				
				return isOredValid;
			} else switch (currentBnfElement.getType()) {
			
				case QUANTIFIER_ONCE_OR_NOT_AT_ALL:
					break;
				case QUANTIFIER_ZERO_OR_MORE_TIMES:
					break;
				case GROUP:
					return checkSyntax(inputString, currentBnfElement.getContent(), writeBuffer, currentBnfRule);
				case TERMINAL:
					
					String terminal = currentBnfElement.getTerminal();
					for (int i = 0; i < terminal.length(); i++) {
						if (inputString.charAt(pos + i) != terminal.charAt(i)) {
							return false;
						}
					}
					
					pos += terminal.length();
					break;
				case SYMBOL:
					break;
			}
		}
		
		return true;				
	}
	
}
