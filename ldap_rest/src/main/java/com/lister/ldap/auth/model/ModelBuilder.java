/**
 * 
 */
package com.lister.ldap.auth.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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

    public static List<User> getUserModelList(List<Map<String, String>> usersMap){
        List<User> users = new ArrayList<User>(usersMap.size());
        for(Map<String, String> userDetail: usersMap){
            users.add(getUserModel(userDetail));
        }
        return users;
    }
	
	private static String getMapValue(Map<String, String> userDetails, String key){
		return userDetails.get(key)!=null ? userDetails.get(key) : null;
	}
	

}
