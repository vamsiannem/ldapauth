package com.lister.ldap.auth.config;

import org.springframework.security.access.AccessDeniedException;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;

/**
 * Created by vamsikrishna on 17/9/14.
 */

public class AccessDeniedExMapper implements ExceptionMapper<AccessDeniedException> {
    public javax.ws.rs.core.Response toResponse(AccessDeniedException ex) {
        return javax.ws.rs.core.Response.status(Response.Status.UNAUTHORIZED)
                .entity(ex.getMessage())
                .type("text/plain")
                .build();
    }
}
