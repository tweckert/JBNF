package javax.bnf;


/**
 * @author Thomas Weckert
 */
public class BnfRule {

	private String symbol;
	private BnfElement firstElement;
	private BnfRule nextRule;
	
	public String getSymbol() {
		return symbol;
	}
	
	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}
	
	public BnfElement getFirstElement() {
		return firstElement;	
	}
	
	public void setFirstElement(BnfElement firstElement) {
		this.firstElement = firstElement;
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
				.append(String.valueOf(firstElement)).append(";")
				.toString();
	}
	
}
