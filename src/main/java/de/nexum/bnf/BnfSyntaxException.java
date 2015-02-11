package de.nexum.bnf;

/**
 * Thrown when an error occurs while the syntax of an input string is validated against a BNF grammar.
 * 
 * @author <a href="mailto:thomas.weckert@nexum.de">Thomas Weckert</a>
 */
public class BnfSyntaxException extends Exception {

	private static final long serialVersionUID = 1271432780551500490L;

	/**
	 * Creates a new exception with no detail message or root cause.
	 */
	public BnfSyntaxException() {

		super();
	}

	/**
	 * Creates a new exception with a root cause.
	 * 
	 * @param rootCause the root cause
	 */
	public BnfSyntaxException(Throwable rootCause) {

		super(rootCause);
	}

	/**
	 * Creates a new exception with a detail message.
	 * 
	 * @param message the detail message
	 */
	public BnfSyntaxException(String message) {

		super(message);
	}

	/**
	 * Creates a new exception with a detail message and root cause.
	 * 
	 * @param message the detail message
	 * @param rootCause the cause of the exception
	 */
	public BnfSyntaxException(String message, Throwable rootCause) {

		super(message, rootCause);
	}
	
}
