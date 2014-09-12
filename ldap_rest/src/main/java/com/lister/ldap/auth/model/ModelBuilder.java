/**
 * 
 */
package com.lister.ldap.auth.model;

import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.lister.ldap.auth.constants.ApplicationConstants;

/**
 * @author vamsikrishna
 *
 */

public class ModelBuilder {
	
	
	public static User getUserModel(Map<String, String> userDetails){
		User user = new User();
		user.setCommonName(getMapValue(userDetails, "cn"));
		user.setDisplayName(getMapValue(userDetails, "displayName"));
		user.setEmail(getMapValue(userDetails, "mail"));
		user.setEmployeeNumber(getMapValue(userDetails, "employeeNumber"));
		user.setMobile(getMapValue(userDetails, "mobile"));
		return user;
		
	}
	
	private static String getMapValue(Map<String, String> userDetails, String key){
		return userDetails.get(key)!=null ? userDetails.get(key) : null;
	}
	

}
