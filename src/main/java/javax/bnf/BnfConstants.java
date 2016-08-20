package javax.bnf;

/**
 * @author Thomas Weckert
 */
public class BnfConstants {

	public static final Character RULE_TERMINATOR = ';';
	
	public static final Character SPACE_CHAR = '\u0020';
	
	public static final Character[] TERMINAL_TERMINATORS = {'\'', '\"'};
	public static final Character[] SYMBOL_TERMINATORS = {'\t', '\r', '\n', SPACE_CHAR};
	
	private BnfConstants() {
		// intentionally left blank
	}

}
