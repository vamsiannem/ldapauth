/**
 *
 */
package com.lister.ldap.auth.rest.impl;

import com.lister.ldap.auth.LdapUtil;
import com.lister.ldap.auth.exception.InvalidUserException;
import com.lister.ldap.auth.exception.LdapAuthResponses;
import com.lister.ldap.auth.model.ModelBuilder;
import com.lister.ldap.auth.model.User;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiResponse;
import com.wordnik.swagger.annotations.ApiResponses;
import org.springframework.context.annotation.Scope;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

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
@Api(value = "/user/{userId}", description = "Validate a User against Lister's Ldap Server.")
//@PermitAll
//@Secured("ROLE_ADMIN")

public class UserValidationResourceImpl /*implements UserValidationResource*/ {

    @Resource
    private LdapUtil ldapUtil;


    /* (non-Javadoc)
     * @see com.lister.ldap.auth.rest.api.LdapResource#autheticateUser(java.lang.String, java.lang.String)
     */
    @ApiOperation(value = "Authenticate the Given User", notes = "Authenticate user by the password supplied. ")
    @ApiResponses({
            @ApiResponse(code = 200 , message = "User is a valid lister employee. Return User details."),
            @ApiResponse(code = 401 , message = "User credentials are invalid."),
            @ApiResponse(code = 500 , message = "An internal error has occurred while serving the request."),
            @ApiResponse(code = 400, message = "Input data validation error, Check the response for more details.")
    })
    @POST
    @Path("/authenticate")
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
    @ApiOperation(value = "Authorize the User against an LDAP Group.", notes = "Check whether the user is present in the specified Group.")
    @ApiResponses({
            @ApiResponse(code = 200 , message = "true if the user is part of the group else it will be false."),
            @ApiResponse(code = 500 , message = "An internal error has occurred while serving the request."),
            @ApiResponse(code = 400, message = "Input data validation error, Check the response for more details.")
    })
    @GET
    @Path("/authorize")
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
    @ApiOperation(value = "Validate the User (Authenticate and authorize).", notes = "Check whether the user is valid.")
    @ApiResponses({
            @ApiResponse(code = 200 , message = "User is a valid lister employee. Return User details."),
            @ApiResponse(code = 500 , message = "An internal error has occurred while serving the request."),
            @ApiResponse(code = 401 , message = "User credentials are invalid OR he is not part of the specified Group."),
            @ApiResponse(code = 400, message = "Input data validation error, Check the response for more details.")
    })
    @POST
    @Path("/validate")
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
