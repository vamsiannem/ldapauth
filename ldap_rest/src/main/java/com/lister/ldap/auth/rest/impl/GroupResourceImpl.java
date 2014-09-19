package com.lister.ldap.auth.rest.impl;

import com.lister.ldap.auth.LdapUtil;
import com.lister.ldap.auth.model.ModelBuilder;
import com.lister.ldap.auth.model.User;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.validation.constraints.NotNull;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.List;
import java.util.Map;

/**
 * This resource is used to perform ldap group operations (READ ONLY).
 * Created by vamsikrishna on 19/9/14.
 */
@Path("group/{groupId}")
@Component
@Scope("request")
@Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
//@RolesAllowed("ROLE_ADMIN")
public class GroupResourceImpl {

    @Resource
    private LdapUtil ldapUtil;

    /**
     * Fetch the list of group members
     * @param groupName
     * @return List of users
     */
    @Path("/member")
    @GET
    public List<User> getGroupMemberDetails(@PathParam("groupId") @NotNull String groupName){
        List<String> groupMemberDNList = ldapUtil.listGroupMembers(groupName);
        String[] groupMemberDNArr = groupMemberDNList.toArray(new String[groupMemberDNList.size()]);
        List<Map<String, String>> usersMap = ldapUtil.fetchUserList(false, groupMemberDNArr);
        return ModelBuilder.getUserModelList(usersMap);
    }

    /*@Path("/member")
    @GET
    public List<Group> getGroupDetails(@PathParam("groupId") @NotNull String groupName){
        List<String> groupMemberDNList = ldapUtil.listGroupMembers(groupName);
        String[] groupMemberDNArr = groupMemberDNList.toArray(new String[groupMemberDNList.size()]);
        List<Map<String, String>> usersMap = ldapUtil.fetchUserList(false, groupMemberDNArr);
        return ModelBuilder.getUserModelList(usersMap);
    }*/
}
