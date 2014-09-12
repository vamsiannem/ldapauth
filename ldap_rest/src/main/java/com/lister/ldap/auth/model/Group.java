/**
 * 
 */
package com.lister.ldap.auth.model;

import java.util.List;

/**
 * @author vamsikrishna
 *
 */
public class Group {

	private String name;
	private List<String> members;
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * @return the members
	 */
	public List<String> getMembers() {
		return members;
	}
	/**
	 * @param members the members to set
	 */
	public void setMembers(List<String> members) {
		this.members = members;
	}
	
	
}
