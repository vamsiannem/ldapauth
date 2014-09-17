/**
 * 
 */
package com.lister.ldap.auth.rest.api;

import java.util.List;

import com.lister.ldap.auth.exception.InvalidUserException;
import com.lister.ldap.auth.model.Group;
import com.lister.ldap.auth.model.User;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;


/**
 * Rest API to perform user operations with LDAP.
 * This is a read only API which does not perform any edits for LDAP users/groups.
 * @author vamsikrishna
 *
 */

public interface UserValidationResource {

	/**
	 * Check if the user is a valid Lister Employee
	 *
     * @param userId - username
     * @param pwd - user password
     * @return {@link User} object if he is a valid lister employee else throw InvalidUserException with a message.
	 * @throws InvalidUserException 
	 */
	User authenticateUser( @NotNull String userId,  @NotNull String pwd) throws InvalidUserException;
	
	/**
	 * Check if the user is part of the Group.
	 * @param userId
	 * @return true is user part of the group else return false.
	 */
	boolean authorizeUser(@NotNull String userId, @NotNull String groupName);
	
	/**
	 * Perform both authentication and authorization of a user. 
	 * @param userId
	 * @param pwd
	 * @param groupName
	 * @return {@link User} object if he is valid else return throw InvalidUserException with a message.
	 */
	User validateUser(@NotNull String userId, @NotNull String pwd, String groupName, boolean isAuthorizationRequired);
	
	
	
}
