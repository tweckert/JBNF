package de.nexum.bnf;


/**
 * @author <a href="mailto:thomas.weckert@nexum.de">Thomas Weckert</a>
 */
public class BnfElement implements Cloneable {

	private BnfElementType type;
	private BnfElementLink link;
	private String terminal;
	private String symbol;
	private BnfElement content;
	private BnfElement next;
	private BnfRule rule;
	
	public BnfElement() {
		super();
	}
	
	private BnfElement(BnfElement bnfElement) {
		
		super();
		
		this.type = bnfElement.type;
		this.link = bnfElement.link;
		this.terminal = bnfElement.terminal;
		this.symbol = bnfElement.symbol;
		this.content = bnfElement.content;
		this.next = bnfElement.next;
		this.rule = bnfElement.rule;
	}
	
	public BnfElementType getType() {
		return type;
	}
	
	public void setType(BnfElementType type) {
		this.type = type;
	}
	
	public String getTerminal() {
		return terminal;
	}
	
	public void setTerminal(String terminal) {
		this.terminal = terminal;
	}
	
	public String getSymbol() {
		return symbol;
	}
	
	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}

	public BnfElement getContent() {
		return content;
	}

	public void setContent(BnfElement content) {
		this.content = content;
	}

	public BnfElement getNext() {
		return next;
	}

	public void setNext(BnfElement next) {
		this.next = next;
	}

	public BnfElementLink getLink() {
		return link;
	}

	public void setLink(BnfElementLink link) {
		this.link = link;
	}
	
	public BnfRule getRule() {
		return rule;
	}

	public void setRule(BnfRule rule) {
		this.rule = rule;
	}

	@Override
	public String toString() {		

		StringBuffer buf = new StringBuffer();
		
		for (BnfElement currentElement = this; currentElement != null; currentElement = currentElement.next) {
		
			switch (currentElement.type) {
				case GROUP:
					buf.append("(").append(currentElement.content).append(")");
					break;
				case QUANTIFIER_ONCE_OR_NOT_AT_ALL:
					buf.append("[").append(currentElement.content).append("]");
					break;
				case QUANTIFIER_ZERO_OR_MORE_TIMES:
					buf.append("{").append(currentElement.content).append("}");
					break;
				case TERMINAL:
					buf.append("'").append(currentElement.terminal).append("'");
					break;
				case SYMBOL_REF:
				case SYMBOL_CALL:
					buf.append(currentElement.symbol);
					break;
			}
			
			if (BnfElementLink.OR.equals(currentElement.link)) {
				buf.append("|");
			}
		}
		
		return buf.toString();
	}
	
	@Override
	public BnfElement clone() {
	    return new BnfElement(this);
	}
	
}
