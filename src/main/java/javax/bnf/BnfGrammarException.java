package javax.bnf;

/**
 * Thrown when an error occurs while a BNF grammar is parsed.
 * 
 * @author Thomas Weckert
 */
public class BnfGrammarException extends Exception {

	private static final long serialVersionUID = 2305580352394242406L;

	/**
	 * Creates a new exception with no detail message or root cause.
	 */
	public BnfGrammarException() {
		// intentionally left blank
	}

	/**
	 * Creates a new exception with a root cause.
	 * 
	 * @param rootCause the root cause
	 */
	public BnfGrammarException(final Throwable rootCause) {
		super(rootCause);
	}

	/**
	 * Creates a new exception with a detail message.
	 * 
	 * @param message the detail message
	 */
	public BnfGrammarException(final String message) {
		super(message);
	}

	/**
	 * Creates a new exception with a detail message and root cause.
	 * 
	 * @param message the detail message
	 * @param rootCause the cause of the exception
	 */
	public BnfGrammarException(String message, Throwable rootCause) {
		super(message, rootCause);
	}

}
