/**
 * 
 */
package com.lister.ldap.auth.rest.api;

import java.util.List;

import com.lister.ldap.auth.model.Group;

/**
 * Perform privileged operations. 
 * For now we support read only operations. It can be extended in next release.
 * @author vamsikrishna
 *
 */
public interface AdminResource {

	/**
	 * List down all the users who are present in an LDAP group
	 * @param groupName
	 */
	Group listGroupMembers(String groupName);
	
	/**
	 * Fetch the details of a list of users (Look for ldap.properties for configuring the return attributes)
	 * @param uids
	 */
	void fetchUsers(List<String> uids);
}
