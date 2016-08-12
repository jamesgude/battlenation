/**
 * <p>Title: GroupUserManagerDAO</p>
 * <p>Description:</p>
 * @author bgude
 * @version 1.0
 */

package com.businesshaps.am.server.group.groupuser;

import com.businesshaps.am.businessobjects.AppGroupUser;
import com.businesshaps.am.server.AppProperties;
import com.businesshaps.oi.ObInject;

import java.util.Arrays;
import java.util.Comparator;

public class GroupUserManagerDAO implements GroupUserManager{
    private AppProperties props;
    private ObInject obInject;

    public GroupUserManagerDAO(AppProperties props, String jdbc) {
        this.props = props;
        obInject = ObInject.getInstance(jdbc);
    }

    public AppGroupUser[] getGroupUserByGroupAlphaId(String groupAlphaId) {
        return new AppGroupUser[0];
    }

    public AppGroupUser[] getGroupByUserId(Integer userId, boolean isGroup) {
        return new AppGroupUser[0];
    }

    public AppGroupUser[] getGroupUserByEmail(String email) {
        return new AppGroupUser[0];
    }

    public AppGroupUser[] getGroupUser() {
        obInject.syncTable(AppGroupUser.class);
        AppGroupUser[] rtns = (AppGroupUser[]) obInject.get(AppGroupUser.class, "");
        Arrays.sort(rtns, new Comparator<AppGroupUser>() {
            public int compare(AppGroupUser o1, AppGroupUser o2) {
            	return new Integer(o2.getId()).compareTo(o1.getId());
            }
        });
        return rtns;
    } 

    public AppGroupUser setGroupUser(AppGroupUser groupUser) {
        groupUser = (AppGroupUser) obInject.set(groupUser);
        return groupUser;
    }

    public void reload() {

    }

    public AppGroupUser getGroupUser(String groupAlphaId, Integer userId, boolean isGroup) {
        return null;
    }

    public AppGroupUser getGroupUser(String groupAlphaId, String email) {
        return null;
    }

    public void delete(AppGroupUser groupUser) {
        obInject.executeUpdate("delete from APPGROUPUSER where id = " + groupUser.getId());
//         groupUser = (AppGroupUser) obInject.set(AppGroupUser.class, groupUser);
    }

    public boolean isGroupUser(String groupAlphaId, Integer userId, boolean isGroup) {
        return false;
    }

    public AppGroupUser getGroupUser(Integer groupUserId) {
        return null;
    }
}
