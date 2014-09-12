package com.lister.ldap.auth;
import java.util.List;
import java.util.Map;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * 
 */

/**
 * @author vamsikrishna
 *
 */

public class LdapUtilTest {

	
	public static void main(String[] args) {
		@SuppressWarnings("resource")
		ApplicationContext ctx = new ClassPathXmlApplicationContext("spring_ldap.xml");
		LdapUtil ldapUtils = ctx.getBean(LdapUtil.class);
		boolean isValidUser = ldapUtils.authenticateUser("vamsikrishna", "vamsi".getBytes());
		System.out.println("User: vamsikrishnan, isvalidUser:"+ isValidUser );
		Map<String, String> userDetails = ldapUtils.fetchUserDetails("vamsikrishna");
		//ldapUtils.authorizeUser("admin", "admin");
	//	List<String> membersList = ldapUtils.listGroupMembers("user");
		List<Map<String, String>> users = ldapUtils.fetchUserList("admin", "vamsikrishna");
		
		//System.out.println(membersList.toString()); 
		System.out.println(userDetails.toString());
	}
}
