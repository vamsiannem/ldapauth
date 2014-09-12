/**
 * 
 */
package com.lister.ldap.auth.exception;

/**
 * This exception is thrown When a user tries to invoke an API for which he does not have access to.
 * @author vamsikrishna
 *
 */
public class UnAuthorizedOperationException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6540032064494611618L;

	/**
	 * 
	 */
	public UnAuthorizedOperationException() {
	}

	/**
	 * @param message
	 */
	public UnAuthorizedOperationException(String message) {
		super(message);
	}

	/**
	 * @param cause
	 */
	public UnAuthorizedOperationException(Throwable cause) {
		super(cause);
	}

	/**
	 * @param message
	 * @param cause
	 */
	public UnAuthorizedOperationException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * @param message
	 * @param cause
	 * @param enableSuppression
	 * @param writableStackTrace
	 */
	public UnAuthorizedOperationException(String message, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
