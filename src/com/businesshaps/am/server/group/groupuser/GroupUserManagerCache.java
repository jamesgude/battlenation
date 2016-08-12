/**
 * <p>Title: GroupUserManagerCache</p>
 * <p>Description:</p>
 * @author bgude
 * @version 1.0
 */

package com.businesshaps.am.server.group.groupuser;

import com.businesshaps.am.businessobjects.AppGroup;
import com.businesshaps.am.businessobjects.AppGroupUser;
import com.businesshaps.am.businessobjects.AppUser;
import com.businesshaps.am.server.AppServer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Hashtable;

public class GroupUserManagerCache implements GroupUserManager{
    private Hashtable<Integer, AppGroupUser> groupUsers;
    private Hashtable<String, ArrayList<AppGroupUser>> groupUsersByGroupAlphaId;
    private Hashtable<Integer, ArrayList<AppGroupUser>> groupUsersByUserId;
    private Hashtable<String, ArrayList<AppGroupUser>> groupUsersByEmail;
    private Hashtable<Integer, ArrayList<AppGroupUser>> groupUsersByGroupId;

    public GroupUserManagerCache() {
        this.reload();
    }

    public AppGroupUser[] getGroupUserByGroupAlphaId(String groupAlphaId) {
        ArrayList<AppGroupUser> superlists;
        if (groupUsersByGroupAlphaId.containsKey(groupAlphaId.toUpperCase())) {
            superlists = groupUsersByGroupAlphaId.get(groupAlphaId.toUpperCase());
        } else {
            superlists = new ArrayList<AppGroupUser>();
        }
        AppGroupUser[] rtns = superlists.toArray(new AppGroupUser[superlists.size()]);
        Arrays.sort(rtns, new Comparator<AppGroupUser>() {
            public int compare(AppGroupUser o1, AppGroupUser o2) {
                return o1.getGroupAlphaId().compareTo(o2.getGroupAlphaId());
            }
        });

 
        return rtns;
    }

    public AppGroupUser[] getGroupByUserId(Integer userId, boolean isGroup) {
        ArrayList<AppGroupUser> superlists;
        Hashtable<Integer, ArrayList<AppGroupUser>> table;
        if (isGroup) {
            table = groupUsersByGroupId;
        } else {
            table = groupUsersByUserId;
        }
        if (table.containsKey(userId)) {
            superlists = table.get(userId);
        } else {
            superlists = new ArrayList<AppGroupUser>();
        }
        return superlists.toArray(new AppGroupUser[superlists.size()]);
    }

    public AppGroupUser[] getGroupUserByEmail(String email) {
        ArrayList<AppGroupUser> superlists;
        Hashtable<String, ArrayList<AppGroupUser>> table;
            table = groupUsersByEmail;
        if (table.containsKey(email)) {
            superlists = table.get(email);
        } else {
            superlists = new ArrayList<AppGroupUser>();
        }
        return superlists.toArray(new AppGroupUser[superlists.size()]);
    }


    public AppGroupUser[] getGroupByEmail(String email) {
        ArrayList<AppGroupUser> superlists;
        Hashtable<String, ArrayList<AppGroupUser>> table;
            table = groupUsersByEmail;

        if (table.containsKey(email)) {
            superlists = table.get(email);
        } else {
            superlists = new ArrayList<AppGroupUser>();
        }
        return superlists.toArray(new AppGroupUser[superlists.size()]);
    }

    public AppGroupUser[] getGroupUser() {
        return groupUsers.values().toArray(new AppGroupUser[groupUsers.values().size()]);
    }

    public AppGroupUser setGroupUser(AppGroupUser groupUser) {
        ArrayList<AppGroupUser> superlists;
        if (groupUsers.containsKey(groupUser.getId())) {
            groupUsers.remove(groupUser.getId());
        }
        groupUsers.put(groupUser.getId(), groupUser);

        Hashtable<Integer, ArrayList<AppGroupUser>> table;
        if (groupUser.isGroupAccess()) {
            table = groupUsersByGroupId;
        } else {
            table = groupUsersByUserId;
        }
        if (table.containsKey(groupUser.getUserId())) {
            superlists = table.get(groupUser.getUserId());
        } else {
            superlists = new ArrayList<AppGroupUser>();
        }
        for (AppGroupUser gus : superlists) {
            if (gus.getId()==groupUser.getId()) {
                    superlists.remove(gus);
                break;
            }
        }
        superlists.add(groupUser);


        table.put(groupUser.getUserId(), superlists);
        return groupUser;

    }

    public AppGroupUser getGroupUser(Integer groupUserId) {
        return groupUsers.get(groupUserId);
    }

    public void reload() {
        this.groupUsers = new Hashtable<Integer, AppGroupUser>();
        this.groupUsersByGroupAlphaId = new Hashtable<String, ArrayList<AppGroupUser>>();
        this.groupUsersByUserId = new Hashtable<Integer, ArrayList<AppGroupUser>>();
        this.groupUsersByEmail = new Hashtable<String, ArrayList<AppGroupUser>>();
        this.groupUsersByGroupId = new Hashtable<Integer, ArrayList<AppGroupUser>>();
    }

    public AppGroupUser getGroupUser(String groupAlphaId, Integer userId, boolean isGroup) {
        AppGroupUser[] groupUsers = this.getGroupByUserId(userId, isGroup);
        AppGroupUser rtn = null;
        for (AppGroupUser groupUser : groupUsers) {
            if (groupUser.getGroupAlphaId().toUpperCase().equals(groupAlphaId.toUpperCase())) {
                rtn = groupUser;
                break;
            }
        }
        return rtn;
    }

    public AppGroupUser getGroupUser(String groupAlphaId, String email) {
        AppGroupUser[] groupUsers = this.getGroupByEmail(email);
        AppGroupUser rtn = null;
        for (AppGroupUser groupUser : groupUsers) {
            if (groupUser.getGroupAlphaId().toUpperCase().equals(groupAlphaId.toUpperCase())) {
                rtn = groupUser;
                break;
            }
        }
        return rtn;
    }

    public void delete(AppGroupUser groupUser) {
        groupUsers.remove(groupUser.getId());
        groupUsersByGroupAlphaId.get(groupUser.getGroupAlphaId().toUpperCase()).remove(groupUser);
        if (!groupUser.isGroupAccess()) {
            groupUsersByUserId.get(groupUser.getUserId()).remove(groupUser);
        } else {
            groupUsersByGroupId.get(groupUser.getUserId()).remove(groupUser);
        }
        if (groupUser.getEmail()!=null) {
            ArrayList<AppGroupUser> tbl = groupUsersByEmail.get(groupUser.getEmail());
            for (AppGroupUser agu : tbl) {
                if (agu.getId()==groupUser.getId()) {
                    tbl.remove(agu);
                    break;
                }
            }
        }

    }


    public boolean isGroupUser(String groupAlphaId, Integer userId, boolean isGroup) {

        AppGroupUser[] groupUsers = null;
        boolean rtn = false;
        if (!isGroup) {
            AppUser appUser = AppServer.getInstance().getUserManager().getUserByUserId(userId);
            if (appUser!=null&&!appUser.isPending()) {

                AppGroup group = AppServer.getInstance().getGroupManager().getGroupByAlphaId(groupAlphaId);
                if (group!=null) {
                    if (group.getOwner()!=null&&group.getOwner().equals(appUser.getUsername())) {
                        rtn = true;
                        return true;
                    }
                }
                if (appUser.getEmail() != null) {
                    groupUsers = this.getGroupUserByGroupAlphaId(groupAlphaId);
                    for (AppGroupUser gp : groupUsers) {
                        if (gp.getEmail() != null&&!gp.isDisabled()) {
                            if (gp.getEmail().equals(appUser.getEmail())) {
                                rtn = true;
                                return true;
                            }
                        }
                    }
                }
            }
            groupUsers = this.getGroupByUserId(userId, false);
            for (AppGroupUser groupUser : groupUsers) {
                if (groupUser.getGroupAlphaId().toUpperCase().equals(groupAlphaId.toUpperCase())&&!groupUser.isDisabled()) {
                    rtn = true;
                    return true;
                }
            }



            //TODO: this.getGroupByEmail(appUser.getEmail(), false);
        }
        if (!rtn) {
            groupUsers = this.getGroupUserByGroupAlphaId(groupAlphaId.toUpperCase());
            AppGroupUser[] groups;
            for (AppGroupUser groupUser : groupUsers) {
                if (groupUser.isGroupAccess()) {
                groups = this.getGroupUserByGroupAlphaId(AppServer.getInstance().getGroupManager().getGroupByGroupId(groupUser.getUserId()).getGroupAlphaId().toUpperCase());
                for (AppGroupUser groupUser2 : groups) {
//                    System.out.println(groupUser2.getGroupAlphaId() + " - " + groupUser.getGroupAlphaId());
                    if (isGroupUser(groupUser2.getGroupAlphaId().toUpperCase(), userId, false)&&!groupUser2.isDisabled()) {
                        rtn = true ;
                        return true;
                    }
                }
                }
            }
        }
        return rtn;
    }
}
