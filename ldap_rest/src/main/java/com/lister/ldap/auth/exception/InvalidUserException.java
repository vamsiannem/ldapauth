/**
 * 
 */
package com.lister.ldap.auth.exception;

/**
 * Throw this exception when the userId is not authenticated/authorized based on the given credentials.
 * @author vamsikrishna
 *
 */
public class InvalidUserException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2502110779765506535L;

	/**
	 * 
	 */
	public InvalidUserException() {
		super();
	}

	/**
	 * @param message
	 */
	public InvalidUserException(String message) {
		super(message);		
	}

	/**
	 * @param cause
	 */
	public InvalidUserException(Throwable cause) {
		super(cause);
	}

	/**
	 * @param message
	 * @param cause
	 */
	public InvalidUserException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * @param message
	 * @param cause
	 * @param enableSuppression
	 * @param writableStackTrace
	 */
	public InvalidUserException(String message, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
