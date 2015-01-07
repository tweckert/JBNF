package de.nexum.bnf;

/**
 * @author <a href="mailto:thomas.weckert@nexum.de">Thomas Weckert</a>
 */
public class BnfElement {

	private BnfElementType type;
	private BnfElementLink link;
	private String terminal;
	private String symbol;
	private BnfElement content;
	private BnfElement next;
	
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
				case QUANTIFIER_ZERO_OR_MORE_TIMES:
					buf.append("{").append(currentElement.content).append("}");
					break;
				case TERMINAL:
					buf.append("'").append(currentElement.terminal).append("'");
					break;
				case SYMBOL:
					buf.append(currentElement.symbol);
					break;
			}
			
			if (BnfElementLink.OR.equals(currentElement.link)) {
				buf.append("|");
			}
		}
		
		return buf.toString();
	}
	
}
