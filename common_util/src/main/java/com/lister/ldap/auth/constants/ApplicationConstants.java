package com.lister.ldap.auth.constants;

/**
 * 
 * @author vamsikrishna
 *
 */
public interface ApplicationConstants {
	String[] USER_ATTRIBUTES = {"displayName", "email"};
	String[] GROUP_ATTIBUTES= {"member", "cn"};
    String LDAP_EX = "LDAP Exception: ";
	String MEMBER_OF = "memberUid";
}
