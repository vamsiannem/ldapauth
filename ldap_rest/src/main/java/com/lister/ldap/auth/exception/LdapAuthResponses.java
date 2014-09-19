/**
 * 
 */
package com.lister.ldap.auth.exception;

import org.springframework.dao.DataAccessException;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.Response.Status;

/**
 * @author vamsikrishna
 *
 */
public class LdapAuthResponses {

    private static final String LDAP_EXCEPTION = "LDAP Exception:";
	/**
	 * 
	 */
	public LdapAuthResponses() {
		// TODO Auto-generated constructor stub
	}
	public static LdapAuthException respondToException(InvalidUserException e) {
		ResponseBuilder bldr = Response.status(Status.UNAUTHORIZED);
		bldr.entity(LDAP_EXCEPTION + ": " + e.getMessage()).type(MediaType.TEXT_PLAIN_TYPE);
		return new LdapAuthException(bldr.build());
	}

	public static LdapAuthException respondToException(DataAccessException e) {
		ResponseBuilder bldr = Response.status(Status.INTERNAL_SERVER_ERROR);
		bldr.entity(LDAP_EXCEPTION + ": " +e.getMessage()).type(MediaType.TEXT_PLAIN_TYPE);
		return new LdapAuthException(bldr.build());
	}

    public static LdapAuthException respondToException(String msg, Status status) {
        ResponseBuilder bldr = Response.status(status);
        bldr.entity(msg).type(MediaType.TEXT_PLAIN_TYPE);
        return new LdapAuthException(bldr.build());
    }

}
