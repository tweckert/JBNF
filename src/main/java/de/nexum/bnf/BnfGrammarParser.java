package de.nexum.bnf;

import de.nexum.util.CharacterUtils;
import de.nexum.util.Position;

/**
 * @author <a href="mailto:thomas.weckert@nexum.de">Thomas Weckert</a>
 */
public class BnfGrammarParser {
	
	//private static final Log LOG = LogFactory.getLog(BnfGrammarParser.class);
	
	private static final Character[] TERMINAL_TERMINATORS = {'\'', '\"'};
	private static final Character[] SYMBOL_TERMINATORS = {' '};
	
	private Character nextCharacter(CharSequence bnfGrammarText, Position position) {
		
		if (position.getPosition() >= bnfGrammarText.length()) {
			return null;
		}
		
		Character nextCharacter = null;
		for (nextCharacter = bnfGrammarText.charAt(position.getPosition()); position.getPosition() <= bnfGrammarText.length(); position.increment()) {
			
			nextCharacter = bnfGrammarText.charAt(position.getPosition());
			if (CharacterUtils.isSpace(nextCharacter) || CharacterUtils.isNewLine(nextCharacter)) {
				continue;
			} else {
				break;
			}
		}
		
		return nextCharacter;
	}
	
	private Character nextLine(CharSequence bnfGrammarText, Position position) {

		Character nextCharacter = null;
		while (!CharacterUtils.isNewLine((nextCharacter = bnfGrammarText.charAt(position.getPosition())))) {
			position.increment();
		}
		
		while (CharacterUtils.isNewLine((nextCharacter = bnfGrammarText.charAt(position.getPosition())))) {
			position.increment();
		}
		
		return nextCharacter;
	}
	
	private BnfElement buildBnfRule(CharSequence bnfGrammarText, Position position) throws BnfGrammarParseException {
		
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
			
			switch (nextCharacter(bnfGrammarText, position)) {
				case '(':
					// a group of BNF elements
					currentElement.setType(BnfElementType.GROUP);
					position.increment();
					currentElement.setContent(buildBnfRule(bnfGrammarText, position));
					if (bnfGrammarText.charAt(position.increment()) != ')') {
						throw new BnfGrammarParseException("')' expected!");
					}
					break;
				case '[':
					// a BNF element that may appear once or not at all
					currentElement.setType(BnfElementType.QUANTIFIER_ONCE_OR_NOT_AT_ALL);
					position.increment();
					currentElement.setContent(buildBnfRule(bnfGrammarText, position));
					if (bnfGrammarText.charAt(position.increment()) != ']') {
						throw new BnfGrammarParseException("']' expected!");
					}
					break;
				case '{':
					// a BNF element that may appear zero or more times
					currentElement.setType(BnfElementType.QUANTIFIER_ZERO_OR_MORE_TIMES);
					position.increment();
					currentElement.setContent(buildBnfRule(bnfGrammarText, position));
					if (bnfGrammarText.charAt(position.increment()) != '}') {
						throw new BnfGrammarParseException("'}' expected!");
					}
					break;
				case '\'':
				case '\"':
					// a BNF terminal
					currentElement.setType(BnfElementType.TERMINAL);
					position.increment();
					currentElement.setTerminal(scanTerminal(bnfGrammarText, position));
					position.increment();
					break;
				default:
					// a BNF symbol
					if (Character.isLetterOrDigit(bnfGrammarText.charAt(position.getPosition()))) {
						currentElement.setType(BnfElementType.SYMBOL_REF);
						currentElement.setSymbol(scanSymbol(bnfGrammarText, position));	
					} else {
						// end of this BNF rule
						if (previousElement != null) {
							previousElement.setNext(null);
						}
						return firstElement;
					}
					break;
			}
			
			if (nextCharacter(bnfGrammarText, position) == '|') {
				currentElement.setLink(BnfElementLink.OR);
				position.increment();
			}
			
			previousElement = currentElement;
		}
	}
	
	private String scanTerminal(CharSequence bnfGrammarText, Position position) throws BnfGrammarParseException {
		
		StringBuffer buf = new StringBuffer();
		for (; position.getPosition() <= bnfGrammarText.length(); position.increment()) {
			
			Character c = bnfGrammarText.charAt(position.getPosition());
			for (Character nextTerminator : TERMINAL_TERMINATORS) {
				if (c == nextTerminator) {
					return buf.toString();
				}
			}

			buf.append(c);
		}
		
		throw new BnfGrammarParseException("Terminator expected!");
	}
	
	private String scanSymbol(CharSequence bnfGrammarText, Position position) throws BnfGrammarParseException {
		
		StringBuffer buf = new StringBuffer();
		for (; position.getPosition() <= bnfGrammarText.length(); position.increment()) {
			
			Character c = bnfGrammarText.charAt(position.getPosition());
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
	public BnfRule buildBnfGrammar(CharSequence bnfGrammarText) throws BnfGrammarParseException {
		
		Position position = new Position(0);
		
		BnfRule firstRule = null;
		BnfRule currentRule = null;
		
		while (nextCharacter(bnfGrammarText, position) != null) {
			
			if (currentRule != null) {
				currentRule.setNextRule(new BnfRule());
				currentRule = currentRule.getNextRule();
			} else {
				firstRule = currentRule = new BnfRule();
			}
		
			// skip comment lines
			while(nextCharacter(bnfGrammarText, position) == '#') {
				nextLine(bnfGrammarText, position);
			}
			
			// on the left side of the assignment ':=' is the BNF symbol placed
			if (!Character.isLetterOrDigit(nextCharacter(bnfGrammarText, position))) {
				throw new BnfGrammarParseException("BNF symbol expected!");
			}
			
			StringBuffer buf = new StringBuffer();
			for (Character c = nextCharacter(bnfGrammarText, position); Character.isLetterOrDigit((c = bnfGrammarText.charAt(position.getPosition()))); position.increment()) {
				buf.append(c);
			}
			
			String symbol = buf.toString();
			
			// then the assignment ':=' follows
			if (nextCharacter(bnfGrammarText, position) != ':' && bnfGrammarText.charAt(position.getPosition() + 1) != '=') {
				throw new BnfGrammarParseException("BNF assignment expected!");
			}
			position.add(2);
			
			// after the assignment ':=' the BNF rule follows
			BnfElement firstRuleElement = buildBnfRule(bnfGrammarText, position);
			
			// the line ends with a semicolon ';'
			if ((nextCharacter(bnfGrammarText, position)) != ';') {
				throw new BnfGrammarParseException("';' expected!");
			}
			
			// store the BNF symbol and it's rule(s)
			currentRule.setSymbol(symbol);
			currentRule.setFirstRuleElement(firstRuleElement);
			
			// continue with the next rule
			position.increment();
		}
		
		// derive the BNF rules and their BNF elements
		deriveBnfRules(firstRule);
		
		return firstRule;
	}
	
}
