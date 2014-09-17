/**
 *
 */
package com.lister.ldap.auth.rest.impl;

import javax.annotation.Resource;
import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.springframework.context.annotation.Scope;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.security.access.annotation.Secured;
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

@Path("/user/{userId}")
@Component
@Scope("request")
@Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
@Secured("ROLE_ADMIN")
public class UserValidationResourceImpl implements UserValidationResource {

    @Resource
    private LdapUtil ldapUtil;


    /* (non-Javadoc)
     * @see com.lister.ldap.auth.rest.api.LdapResource#autheticateUser(java.lang.String, java.lang.String)
     */
    @POST
    @Path("authenticate")
    @Consumes({MediaType.APPLICATION_FORM_URLENCODED})
    public User authenticateUser(@NotNull @PathParam("userId") String userId, @NotNull @FormParam("password") String pwd) throws InvalidUserException{
        try {
            boolean isAuthenticUser = ldapUtil.authenticateUser(userId, pwd.getBytes());
            if(isAuthenticUser){
                return ModelBuilder.getUserModel(ldapUtil.fetchUserDetails(userId));
            } else {
                throw new InvalidUserException("Either invalid credentials are supplied  OR the User is not a Lister Employee !!!");
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
    public boolean authorizeUser( @NotNull @PathParam("userId") String userId, @NotNull @QueryParam("groupName") String groupName) {
        boolean isAuthorizedUser = false;
        try {
            isAuthorizedUser = ldapUtil.authorizeUser(userId, groupName);

        } catch (EmptyResultDataAccessException e ){
            throw LdapAuthResponses.respondToException("User:"+ userId + " is not part of the Group:" + groupName, Response.Status.UNAUTHORIZED);
        } catch (IncorrectResultSizeDataAccessException e){
            throw LdapAuthResponses.respondToException("User:"+ userId + " is not part of the Group:" + groupName, Response.Status.UNAUTHORIZED);
        } catch (DataAccessException dae) {
            throw LdapAuthResponses.respondToException(dae);
        }

        return isAuthorizedUser;
    }

    /* (non-Javadoc)
     * @see com.lister.ldap.auth.rest.api.LdapResource#validateUser(java.lang.String, java.lang.String, java.lang.String)
     */
    @POST
    @Path("validate")
    @Consumes({MediaType.APPLICATION_FORM_URLENCODED})
    public User validateUser(@NotNull @PathParam("userId")String userId, @NotNull @FormParam("password") String pwd,
                             @FormParam("groupName") String groupName, @FormParam("isAuthorizationRequired")@DefaultValue("false") boolean isAuthorizationRequired ) {
        boolean isAuthorized = false;
        boolean isAuthenticUser = false;
        try {
            isAuthenticUser = ldapUtil.authenticateUser(userId, pwd.getBytes());
            if(isAuthorizationRequired) {
                if(isAuthenticUser) {
                    isAuthorized = ldapUtil.authorizeUser(userId, groupName);
                }
                if(isAuthorized) {
                    return ModelBuilder.getUserModel(ldapUtil.fetchUserDetails(userId));
                } else if(isAuthenticUser) {
                    throw  new InvalidUserException("User:"+ userId + " is not part of the Group:" + groupName);
                } else {
                    throw new InvalidUserException("Either invalid credentials are supplied  OR the User is not a Lister Employee !!!");
                }
            } else {
                if (isAuthenticUser) {
                    return ModelBuilder.getUserModel(ldapUtil.fetchUserDetails(userId));
                } else {
                    throw new InvalidUserException("Either invalid credentials are supplied  OR the User is not a Lister Employee !!!");
                }
            }

        } catch (DataAccessException dae){
            throw LdapAuthResponses.respondToException(dae);
        } catch (InvalidUserException ive){
            throw LdapAuthResponses.respondToException(ive);
        }
    }

    public void setLdapUtil(LdapUtil ldapUtil) {
        this.ldapUtil = ldapUtil;
    }
}
