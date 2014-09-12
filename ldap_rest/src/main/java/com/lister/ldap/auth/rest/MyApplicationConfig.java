/**
 * 
 */
package com.lister.ldap.auth.rest;

import java.util.Set;

import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.spring.scope.RequestContextFilter;

/**
 * Register all the resources for Jersey Application.
 * @author vamsikrishna
 *
 */
public class MyApplicationConfig extends ResourceConfig {

	/**
	 * 
	 */
	public MyApplicationConfig() {
		register(RequestContextFilter.class);
        register(JacksonFeature.class);        
	}

	/**
	 * @param classes
	 */
	public MyApplicationConfig(Set<Class<?>> classes) {
		super(classes);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param classes
	 */
	public MyApplicationConfig(Class<?>... classes) {
		super(classes);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param original
	 */
	public MyApplicationConfig(ResourceConfig original) {
		super(original);
		// TODO Auto-generated constructor stub
	}

}
