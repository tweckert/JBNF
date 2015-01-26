package de.nexum.bnf;

/**
 * @author <a href="mailto:thomas.weckert@nexum.de">Thomas Weckert</a>
 */
public enum BnfElementType {

	/** '[...]' : BNF element may appear once or not at all */
	QUANTIFIER_ONCE_OR_NOT_AT_ALL,
	/** '{...}' : BNF element may appear zero or more times */
	QUANTIFIER_ZERO_OR_MORE_TIMES,
	/** '(...)' : groups several BNF elements */
	GROUP,
	/** '...' : BNF terminal */
	TERMINAL,
	/** BNF symbol that references another BNF rule */
	SYMBOL_REF,
	/** BNF symbol that calls another BNF rule */
	SYMBOL_CALL
	
}
