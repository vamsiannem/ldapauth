package com.lister.ldap.auth;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ldap.core.AttributesMapper;
import org.springframework.ldap.core.ContextMapper;
import org.springframework.ldap.core.DirContextOperations;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.filter.AndFilter;
import org.springframework.ldap.filter.EqualsFilter;
import org.springframework.ldap.query.ContainerCriteria;
import org.springframework.ldap.query.LdapQuery;
import org.springframework.ldap.query.LdapQueryBuilder;
import org.springframework.ldap.support.LdapUtils;

import com.lister.ldap.auth.constants.ApplicationConstants;
/**
 *  A smart utility class that interacts with your LDAP server
 *  and perform authentication/authorization of users.
 * 
 * @author vamsikrishna
 *
 */

public class LdapUtil {

	private static final Logger LOGGER = LoggerFactory.getLogger(LdapUtil.class);

	private LdapTemplate ldapTemplate;

	private String[] defaultFetchAttributes = ApplicationConstants.USER_ATTRIBUTES;
		
	private String defaultGroupFetchAttr = ApplicationConstants.GROUP_ATTIBUTES;
	
	private String adminGroup;

	public LdapUtil(LdapTemplate ldapTemplate){
		super();
		this.ldapTemplate = ldapTemplate;		
	}

	/**
	 * Check if the user is a valid user for the system.
	 * 
	 * @param uid
	 * @param pwd
	 * @return
	 */
	public boolean authenticateUser(String uid, byte[] pwd){
		LOGGER.info("Received Request to authenticate User:"+ uid);
		boolean isValid = false;
		AndFilter filter = new AndFilter();
		filter.and(new EqualsFilter("uid", uid)).and(new EqualsFilter("objectClass", "person"));		
		isValid = ldapTemplate.authenticate(LdapUtils.emptyLdapName(), filter.toString(), new String(pwd));
		LOGGER.info("User: %s , isValid: %s", uid, String.valueOf(isValid));
		return isValid;
	}

	/**
	 * Fetch the user details from ldap server
	 * @param uid
	 * @return
	 */
	public Map<String, String> fetchUserDetails(String uid){
		LdapQuery query = LdapQueryBuilder.query().base(LdapUtils.emptyLdapName())
				.attributes(defaultFetchAttributes)
				.filter(new EqualsFilter("uid", uid));
		List<Map<String, String>> users =	ldapTemplate.search(query, new AttributesMapper<Map<String, String>>() {

			public Map<String, String> mapFromAttributes(Attributes attributes)
					throws NamingException {
				Map<String, String> returnAttribs = new HashMap<String, String>();
				NamingEnumeration<? extends Attribute> attribs = attributes.getAll();
				while(attribs.hasMoreElements()){
					Attribute userAttribute = attribs.nextElement();
					returnAttribs.put(userAttribute.getID(), userAttribute.get(0).toString());
				}
				return returnAttribs;
			}
		});
		if(users!=null){
			LOGGER.info("Fetched User Details");
			return users.get(0);
		}
		return null;
	}
	/**
	 * Check if the user is present on a particular group or not.
	 * @param uid
	 * @param group
	 * @return
	 */
	public boolean authorizeUser(final String uid, final String group) {
		final String userDn = getUserDn(uid);
		LdapQuery query = LdapQueryBuilder.query().base(LdapUtils.emptyLdapName()).attributes(defaultGroupFetchAttr)
				.where("objectClass").is("groupOfNames").and("cn").is(group).and(defaultGroupFetchAttr).like(userDn);
		List<Boolean> usersGroups = ldapTemplate.search(query, new AttributesMapper<Boolean>() {

			public Boolean mapFromAttributes(Attributes attributes)
					throws NamingException {
				if(attributes.get(defaultGroupFetchAttr).contains(userDn)){
					return true;
				}
				LOGGER.debug("User - %s is not present in the Group - %s", uid, group);
				return false;
			}
		});
		if(usersGroups.get(0)){
			return true;
		}
		return false;

	}

	public String getUserDn(String uid){
		LdapQuery query = LdapQueryBuilder.query().base(LdapUtils.emptyLdapName())
				.where("uid").is(uid);
		return ldapTemplate.searchForObject(query, new ContextMapper<String>() {

			public String mapFromContext(Object ctx) throws NamingException {
				DirContextOperations adapter = (DirContextOperations) ctx;
				return adapter.getNameInNamespace();
			}
		});
	}

	public List<String> listGroupMembers(String group){
		LdapQuery query = LdapQueryBuilder.query().base(LdapUtils.emptyLdapName()).attributes(defaultGroupFetchAttr)
				.where("objectClass").is("groupOfNames").and("cn").is(group);
		return ldapTemplate.searchForObject(query, new ContextMapper<List<String>>() {

			public List<String> mapFromContext(Object ctx)
					throws NamingException {
				DirContextOperations adapter = (DirContextOperations) ctx;
				String[] groupMembers = adapter.getStringAttributes(defaultGroupFetchAttr);
				return Arrays.asList(groupMembers);				
			}
		}); 

	}

	public List<Map<String,String>> fetchUserList(String... uids){
		if( uids == null || uids.length ==0 ) {
			throw new NullPointerException("User array cannot be null or blank");
		}
		ContainerCriteria query = LdapQueryBuilder.query().base(LdapUtils.emptyLdapName())
				.attributes(defaultFetchAttributes).where("uid").is(uids[0]);	
		for(String uid : uids){
			query = query.or("uid").is(uid);
		}
		List<Map<String, String>> users =	ldapTemplate.search(query, new AttributesMapper<Map<String, String>>() {

			public Map<String, String> mapFromAttributes(Attributes attributes)
					throws NamingException {
				Map<String, String> returnAttribs = new HashMap<String, String>();
				NamingEnumeration<? extends Attribute> attribs = attributes.getAll();
				while(attribs.hasMoreElements()){
					Attribute userAttribute = attribs.nextElement();
					returnAttribs.put(userAttribute.getID(), userAttribute.get(0).toString());
				}
				return returnAttribs;
			}
		});
		return users;

	}
	
	
	/**
	 * @param defaultFetchAttributes the defaultFetchAttributes to set
	 */
	public void setDefaultFetchAttributes(String defaultFetchAttributes) {
		if(defaultFetchAttributes !=null && !defaultFetchAttributes.trim().equals("")) {
			this.defaultFetchAttributes = defaultFetchAttributes.split(",");
		}		
	}

	/**
	 * @param defaultGroupFetchAttr the defaultGroupFetchAttr to set
	 */
	public void setDefaultGroupFetchAttr(String defaultGroupFetchAttr) {
		this.defaultGroupFetchAttr = defaultGroupFetchAttr;
	}

	/**
	 * @param adminGroup the adminGroup to set
	 */
	public void setAdminGroup(String adminGroup) {
		this.adminGroup = adminGroup;
	}


}
