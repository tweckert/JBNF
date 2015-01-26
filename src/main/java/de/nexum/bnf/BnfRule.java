package de.nexum.bnf;


/**
 * @author <a href="mailto:thomas.weckert@nexum.de">Thomas Weckert</a>
 */
public class BnfRule {

	private String symbol;
	private BnfElement firstRuleElement;
	private BnfRule nextRule;
	
	public String getSymbol() {
		return symbol;
	}
	
	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}
	
	public BnfElement getFirstRuleElement() {
		return firstRuleElement;	
	}
	
	public void setFirstRuleElement(BnfElement firstElement) {
		this.firstRuleElement = firstElement;
	}
	
	public BnfRule getNextRule() {
		return nextRule;
	}
	
	public void setNextRule(BnfRule next) {
		this.nextRule = next;
	}	
	
	@Override
	public String toString() {		
		return new StringBuffer().append(String.valueOf(symbol)).append(" := ")
				.append(String.valueOf(firstRuleElement)).append(";")
				.toString();
	}
	
}
