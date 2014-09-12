/**
 * 
 */
package com.lister.ldap.auth.rest.impl;

import javax.annotation.Resource;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;

import org.springframework.context.annotation.Scope;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Component;

import com.lister.ldap.auth.LdapUtil;
import com.lister.ldap.auth.exception.InvalidUserException;
import com.lister.ldap.auth.exception.LdapAuthResponses;
import com.lister.ldap.auth.model.ModelBuilder;
import com.lister.ldap.auth.model.User;
import com.lister.ldap.auth.rest.api.UserValidationResource;

/**
 * Rest API to perform user operations with LDAP.
 * This is a read only API which does not perform any edits for LDAP users/groups.
 * @author vamsikrishna
 *
 */

@Path("user/{userId}")
@Component
@Scope("request") 
public class UserValidationResourceImpl implements UserValidationResource {

	@Resource
	private LdapUtil ldapUtil;
	

	/* (non-Javadoc)
	 * @see com.lister.ldap.auth.rest.api.LdapResource#autheticateUser(java.lang.String, java.lang.String)
	 */	
	@POST
	@Path("authenticate")
	public User autheticateUser(@PathParam("userId") String userId, @FormParam("password") String pwd) {
		try {
		boolean isAuthenticUser = ldapUtil.authenticateUser(userId, pwd.getBytes());		
		if(isAuthenticUser){
			return ModelBuilder.getUserModel(ldapUtil.fetchUserDetails(userId));
		} else {
			throw new InvalidUserException("User with Id:"+ userId +" is not a Lister Employee");
		}	
		} catch (DataAccessException dae){
			throw LdapAuthResponses.respondToException(dae);
		} catch (InvalidUserException iue){
			throw LdapAuthResponses.respondToException(iue);
		}
	}

	/* (non-Javadoc)
	 * @see com.lister.ldap.auth.rest.api.LdapResource#authorizeUser(java.lang.String, java.lang.String)
	 */
	@GET
	@Path("authorize")
	public boolean authorizeUser(@PathParam("userId") String userId, @QueryParam("groupName") String groupName) {
		return false;
	}

	/* (non-Javadoc)
	 * @see com.lister.ldap.auth.rest.api.LdapResource#validateUser(java.lang.String, java.lang.String, java.lang.String)
	 */
	@POST
	@Path("validate")
	public User validateUser(String userId, String pwd, String groupName) {
		// TODO Auto-generated method stub
		return null;
	}

}
