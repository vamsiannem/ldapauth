/**
 * 
 */
package com.lister.ldap.auth.exception;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.Response.Status;

import org.springframework.dao.DataAccessException;

/**
 * @author vamsikrishna
 *
 */
public class LdapAuthResponses {

	/**
	 * 
	 */
	public LdapAuthResponses() {
		// TODO Auto-generated constructor stub
	}
	public static LdapAuthException respondToException(InvalidUserException e) {
		ResponseBuilder bldr = Response.status(Status.NOT_FOUND);
		bldr.entity(e.getMessage()).type(MediaType.TEXT_PLAIN_TYPE);
		return new LdapAuthException(bldr.build());
	}

	public static LdapAuthException respondToException(DataAccessException e) {
		ResponseBuilder bldr = Response.status(Status.NOT_FOUND);
		bldr.entity(e.getMessage()).type(MediaType.TEXT_PLAIN_TYPE);
		return new LdapAuthException(bldr.build());
	}

}
