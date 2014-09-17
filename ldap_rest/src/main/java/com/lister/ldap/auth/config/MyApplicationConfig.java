/**
 * 
 */
package com.lister.ldap.auth.config;

import java.util.Set;

import com.lister.ldap.auth.rest.impl.UserValidationResourceImpl;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.ServerProperties;
import org.glassfish.jersey.server.spring.scope.RequestContextFilter;

import javax.ws.rs.ApplicationPath;

/**
 * Register all the resources for Jersey Application.
 * @author vamsikrishna
 *
 */
@ApplicationPath("/")
public class MyApplicationConfig extends ResourceConfig {

	/**
	 * 
	 */
	public MyApplicationConfig() {
		register(RequestContextFilter.class);
        register(JacksonFeature.class);
        packages("com.lister.ldap.auth.rest");
        property(ServerProperties.BV_SEND_ERROR_IN_RESPONSE,true);
        property(ServerProperties.BV_DISABLE_VALIDATE_ON_EXECUTABLE_OVERRIDE_CHECK, true);
        register(ValidationConfigurationContextResolver.class);
	}

}
