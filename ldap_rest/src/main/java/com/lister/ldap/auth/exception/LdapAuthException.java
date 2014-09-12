/**
 * 
 */
package com.lister.ldap.auth.exception;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

/**
 * @author vamsikrishna
 *
 */
public class LdapAuthException extends WebApplicationException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1620101650478206896L;

	/**
	 * 
	 */
	public LdapAuthException() {
	}

	/**
	 * @param message
	 */
	public LdapAuthException(String message) {
		super(message);
	}

	/**
	 * @param response
	 */
	public LdapAuthException(Response response) {
		super(response);
	}

	/**
	 * @param status
	 */
	public LdapAuthException(int status) {
		super(status);
	}

	/**
	 * @param status
	 */
	public LdapAuthException(Status status) {
		super(status);
	}

	/**
	 * @param cause
	 */
	public LdapAuthException(Throwable cause) {
		super(cause);
	}

	/**
	 * @param message
	 * @param response
	 */
	public LdapAuthException(String message, Response response) {
		super(message, response);
	}

	/**
	 * @param message
	 * @param status
	 */
	public LdapAuthException(String message, int status) {
		super(message, status);
	}

	/**
	 * @param message
	 * @param status
	 */
	public LdapAuthException(String message, Status status) {
		super(message, status);
	}

	/**
	 * @param message
	 * @param cause
	 */
	public LdapAuthException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * @param cause
	 * @param response
	 */
	public LdapAuthException(Throwable cause, Response response) {
		super(cause, response);
	}

	/**
	 * @param cause
	 * @param status
	 */
	public LdapAuthException(Throwable cause, int status) {
		super(cause, status);
	}

	/**
	 * @param cause
	 * @param status
	 * @throws IllegalArgumentException
	 */
	public LdapAuthException(Throwable cause, Status status)
			throws IllegalArgumentException {
		super(cause, status);
	}

	/**
	 * @param message
	 * @param cause
	 * @param response
	 */
	public LdapAuthException(String message, Throwable cause, Response response) {
		super(message, cause, response);
	}

	/**
	 * @param message
	 * @param cause
	 * @param status
	 */
	public LdapAuthException(String message, Throwable cause, int status) {
		super(message, cause, status);
	}

	/**
	 * @param message
	 * @param cause
	 * @param status
	 * @throws IllegalArgumentException
	 */
	public LdapAuthException(String message, Throwable cause, Status status)
			throws IllegalArgumentException {
		super(message, cause, status);
	}

}
