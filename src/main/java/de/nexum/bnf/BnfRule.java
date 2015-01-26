package de.nexum.bnf;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author <a href="mailto:thomas.weckert@nexum.de">Thomas Weckert</a>
 */
public class BnfRule {

	private String symbol;
	private BnfElement firstRuleElement;
	private BnfRule nextRule;
	private List<String> values = new ArrayList<String>();
	
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
	
	public List<String> getValues() {
		return values != null ? Collections.unmodifiableList(values) : null;
	}
	
	public void addValues(List<String> values) {
		this.values.addAll(values);
	}
	
	public void addValue(String value) {
		this.values.add(value);
	}
	
	@Override
	public String toString() {		
		return new StringBuffer().append(String.valueOf(symbol)).append(" := ")
				.append(String.valueOf(firstRuleElement)).append(";")
				.toString();
	}
	
}
