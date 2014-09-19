package com.lister.ldap.auth;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.List;
import java.util.Map;

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
		List<String> membersList = ldapUtils.listGroupMembers("user");
        String[] abc = membersList.toArray(new String[membersList.size()]);
		List<Map<String, String>> users = ldapUtils.fetchUserList(false,abc);
		
		//System.out.println(membersList.toString()); 
		System.out.println(userDetails.toString());
	}
}
