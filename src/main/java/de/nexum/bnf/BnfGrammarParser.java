package de.nexum.bnf;

import de.nexum.util.CharacterUtils;

/**
 * @author <a href="mailto:thomas.weckert@nexum.de">Thomas Weckert</a>
 */
public class BnfGrammarParser {
	
	//private static final Log LOG = LogFactory.getLog(BnfGrammarParser.class);
	
	private static final Character[] TERMINAL_TERMINATORS = {'\'', '\"'};
	private static final Character[] SYMBOL_TERMINATORS = {' '};
	
	private int pos;
	private CharSequence bnfGrammarText;
	
	public BnfGrammarParser(CharSequence bnfGrammarText) {
		
		super();		
		this.pos = 0;
		this.bnfGrammarText = bnfGrammarText;
	}
	
	private Character nextCharacter() {
		
		if (pos >= bnfGrammarText.length()) {
			return null;
		}
		
		Character nextCharacter = null;
		for (nextCharacter = bnfGrammarText.charAt(pos); pos <= bnfGrammarText.length(); pos++) {
			
			nextCharacter = bnfGrammarText.charAt(pos);
			if (CharacterUtils.isSpace(nextCharacter) || CharacterUtils.isNewLine(nextCharacter)) {
				continue;
			} else {
				break;
			}
		}
		
		return nextCharacter;
	}
	
	private Character nextLine() {

		Character nextCharacter = null;
		while (!CharacterUtils.isNewLine((nextCharacter = bnfGrammarText.charAt(pos)))) {
			pos++;
		}
		
		while (CharacterUtils.isNewLine((nextCharacter = bnfGrammarText.charAt(pos)))) {
			pos++;
		}
		
		return nextCharacter;
	}
	
	private BnfElement buildBnfRule() throws BnfGrammarParseException {
		
		BnfElement currentElement = null;
		BnfElement previousElement = null;
		BnfElement firstElement = null;
		
		while (true) {
			
			if (currentElement != null) {
				currentElement.setNext(new BnfElement());
				currentElement = currentElement.getNext();
			} else {
				firstElement = currentElement = new BnfElement();
			}
			
			currentElement.setNext(null);
			currentElement.setLink(BnfElementLink.DEFAULT);
			
			switch (nextCharacter()) {
				case '(':
					// a group of BNF elements
					currentElement.setType(BnfElementType.GROUP);
					pos++;
					currentElement.setContent(buildBnfRule());
					if (bnfGrammarText.charAt(pos++) != ')') {
						throw new BnfGrammarParseException("')' expected!");
					}
					break;
				case '[':
					// a BNF element that may appear once or not at all
					currentElement.setType(BnfElementType.QUANTIFIER_ONCE_OR_NOT_AT_ALL);
					pos++;
					currentElement.setContent(buildBnfRule());
					if (bnfGrammarText.charAt(pos++) != ']') {
						throw new BnfGrammarParseException("']' expected!");
					}
					break;
				case '{':
					// a BNF element that may appear zero or more times
					currentElement.setType(BnfElementType.QUANTIFIER_ZERO_OR_MORE_TIMES);
					pos++;
					currentElement.setContent(buildBnfRule());
					if (bnfGrammarText.charAt(pos++) != '}') {
						throw new BnfGrammarParseException("'}' expected!");
					}
					break;
				case '\'':
				case '\"':
					// a BNF terminal
					currentElement.setType(BnfElementType.TERMINAL);
					pos++;
					currentElement.setTerminal(scanTerminal());
					pos++;
					break;
				default:
					// a BNF symbol
					if (Character.isLetterOrDigit(bnfGrammarText.charAt(pos))) {
						currentElement.setType(BnfElementType.SYMBOL_REF);
						currentElement.setSymbol(scanSymbol());	
					} else {
						// end of this BNF rule
						if (previousElement != null) {
							previousElement.setNext(null);
						}
						return firstElement;
					}
					break;
			}
			
			if (nextCharacter() == '|') {
				currentElement.setLink(BnfElementLink.OR);
				pos++;
			}
			
			previousElement = currentElement;
		}
	}
	
	private String scanTerminal() throws BnfGrammarParseException {
		
		StringBuffer buf = new StringBuffer();
		for (; pos <= bnfGrammarText.length(); pos++) {
			
			Character c = bnfGrammarText.charAt(pos);
			for (Character nextTerminator : TERMINAL_TERMINATORS) {
				if (c == nextTerminator) {
					return buf.toString();
				}
			}

			buf.append(c);
		}
		
		throw new BnfGrammarParseException("Terminator expected!");
	}
	
	private String scanSymbol() throws BnfGrammarParseException {
		
		StringBuffer buf = new StringBuffer();
		for (; pos <= bnfGrammarText.length(); pos++) {
			
			Character c = bnfGrammarText.charAt(pos);
			for (Character nextTerminator : SYMBOL_TERMINATORS) {
				if (c == nextTerminator) {
					return buf.toString();
				}
			}
			if (!Character.isLetterOrDigit(c)) {
				return buf.toString();
			}
			
			buf.append(c);
		}
		
		throw new BnfGrammarParseException("Terminator expected!");
	}
	
	/**
	 * Derives the BNF elements of a rule.
	 * 
	 * Each BNF symbol is derived to it's corresponding BNF rule.
	 * 
	 * @param firstRule the first rule of the entire BNF grammar
	 * @param firstElement the first BNF element of the current rule
	 * @throws BnfGrammarParseException
	 */
	private void deriveBnfElements(BnfRule firstRule, BnfElement firstElement) throws BnfGrammarParseException {
		
		BnfElement currentElement = null;
		for (currentElement = firstElement; currentElement != null; currentElement = currentElement.getNext()) {
			
			if (BnfElementType.SYMBOL_REF.equals(currentElement.getType())) {
				
				// we found a BNF symbol, now try to find the corresponding BNF rule for that symbol
				boolean foundRule = false;
				for (BnfRule currentRule = firstRule; currentRule != null && foundRule == false; currentRule = currentRule.getNextRule()) {

					if (currentRule.getSymbol().equals(currentElement.getSymbol())) {
						
						// store the reference to the corresponding BNF rule
						currentElement.setContent(currentRule.getFirstRuleElement());
						// store a "backwards"-reference from the BND element to it's rule as well
						currentElement.setRule(currentRule);
						// change the type of the BNF element into a valid symbol call
						currentElement.setType(BnfElementType.SYMBOL_CALL);
						foundRule = true;
					}
				}
				
				if (!foundRule) {
					throw new BnfGrammarParseException("No rule found for BNF symbol '" + String.valueOf(currentElement.getSymbol()) + "'!");
				}
			} else if (BnfElementType.GROUP.equals(currentElement.getType()) 
					|| BnfElementType.QUANTIFIER_ONCE_OR_NOT_AT_ALL.equals(currentElement.getType()) 
					|| BnfElementType.QUANTIFIER_ZERO_OR_MORE_TIMES.equals(currentElement.getType())) {
				
				// derive the "sub-element(s)" within the current element
				deriveBnfElements(firstRule, currentElement.getContent());
			}
		}
	}

	/**
	 * Derives the BNF rules.
	 * 
	 * Each BNF symbol is derived to it's corresponding BNF rule.
	 * 
	 * @param firstRule the first rule of the entire BNF grammar
	 * @throws BnfGrammarParseException
	 */
	private void deriveBnfRules(BnfRule firstRule) throws BnfGrammarParseException {
		
		BnfRule currentRule = null;
		for (currentRule = firstRule; currentRule != null; currentRule = currentRule.getNextRule()) {
			deriveBnfElements(firstRule, currentRule.getFirstRuleElement());
		}
	}
	
	/**
	 * Builds the BNF grammar as {@link BnfRule}(s).
	 * 
	 * @return the first BNF rule in the grammar
	 * @throws BnfGrammarParseException
	 */
	public BnfRule buildBnfGrammar() throws BnfGrammarParseException {
		
		pos = 0;
		
		BnfRule firstRule = null;
		BnfRule currentRule = null;
		
		while (nextCharacter() != null) {
			
			if (currentRule != null) {
				currentRule.setNextRule(new BnfRule());
				currentRule = currentRule.getNextRule();
			} else {
				firstRule = currentRule = new BnfRule();
			}
		
			// skip comment lines
			while(nextCharacter() == '#') {
				nextLine();
			}
			
			// on the left side of the assignment ':=' is the BNF symbol placed
			if (!Character.isLetterOrDigit(nextCharacter())) {
				throw new BnfGrammarParseException("BNF symbol expected!");
			}
			
			StringBuffer buf = new StringBuffer();
			for (Character c = nextCharacter(); Character.isLetterOrDigit((c = bnfGrammarText.charAt(pos))); pos++) {
				buf.append(c);
			}
			
			String symbol = buf.toString();
			
			// then the assignment ':=' follows
			if (nextCharacter() != ':' && bnfGrammarText.charAt(pos + 1) != '=') {
				throw new BnfGrammarParseException("BNF assignment expected!");
			}
			pos += 2;
			
			// after the assignment ':=' the BNF rule follows
			BnfElement firstRuleElement = buildBnfRule();
			
			// the line ends with a semicolon ';'
			if ((nextCharacter()) != ';') {
				throw new BnfGrammarParseException("';' expected!");
			}
			
			// store the BNF symbol and it's rule(s)
			currentRule.setSymbol(symbol);
			currentRule.setFirstRuleElement(firstRuleElement);
			
			// continue with the next rule
			pos++;
		}
		
		// derive the BNF rules and their BNF elements
		deriveBnfRules(firstRule);
		
		return firstRule;
	}
	
}
