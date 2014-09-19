package com.lister.ldap.auth.config;

import com.lister.ldap.auth.constants.ApplicationConstants;

import javax.naming.NameNotFoundException;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;

/**
 * Created by vamsikrishna on 19/9/14.
 */
public class NameNotFoundExceptionMapper implements ExceptionMapper<org.springframework.ldap.NameNotFoundException> {

    @Override
    public Response toResponse(org.springframework.ldap.NameNotFoundException exception) {
        Throwable th = exception.getRootCause();
        if(th instanceof NameNotFoundException){
            NameNotFoundException ex = (NameNotFoundException) exception.getCause();
            return javax.ws.rs.core.Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(ApplicationConstants.LDAP_EX + "Name not found: "+ ex.getRemainingName())
                    .type("text/plain")
                    .build();
        }

        return javax.ws.rs.core.Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(ApplicationConstants.LDAP_EX + "Contact Admin for more details !!! ")
                .type("text/plain")
                .build();


    }
}
