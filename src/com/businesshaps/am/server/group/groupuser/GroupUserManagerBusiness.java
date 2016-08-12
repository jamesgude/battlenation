/**
 * <p>Title: GroupUserManagerBusiness</p>
 * <p>Description:</p>
 * @author bgude
 * @version 1.0
 */

package com.businesshaps.am.server.group.groupuser;

import com.businesshaps.am.businessobjects.AppGroupUser;
import com.businesshaps.am.server.AppProperties;

public class GroupUserManagerBusiness implements GroupUserManager {
    private AppProperties props;
    private GroupUserManagerDAO dao;
    private GroupUserManager cache;

    public GroupUserManagerBusiness(AppProperties props) {
        this.props = props;
        this.reload();
    }

    public AppGroupUser[] getGroupUserByGroupAlphaId(String groupAlphaId) {
        return cache.getGroupUserByGroupAlphaId(groupAlphaId);
    }

    public AppGroupUser[] getGroupByUserId(Integer userId, boolean isGroup) {
        return cache.getGroupByUserId(userId, isGroup);
    }

    public AppGroupUser[] getGroupUserByEmail(String email) {
        return cache.getGroupUserByEmail(email);
    }
 
    public AppGroupUser[] getGroupUser() {
        return cache.getGroupUser();
    }

    public AppGroupUser setGroupUser(AppGroupUser groupUser) {
        groupUser = dao.setGroupUser(groupUser);
        cache.setGroupUser(groupUser);
        return groupUser;
    }

    public void reload() {
        dao = new GroupUserManagerDAO(props, props.getJdbc());
        GroupUserManagerCache tmp_cache = new GroupUserManagerCache();

        for (AppGroupUser groupUser : dao.getGroupUser()) {
            tmp_cache.setGroupUser(groupUser);
        }
        cache = tmp_cache;
    }

    public AppGroupUser getGroupUser(String groupAlphaId, Integer userId, boolean isGroup) {
        return cache.getGroupUser(groupAlphaId, userId, isGroup);
    }

    public AppGroupUser getGroupUser(String groupAlphaId, String email) {
        return cache.getGroupUser(groupAlphaId, email);
    }

    public void delete(AppGroupUser groupUser) {
        cache.delete(groupUser);
        dao.delete(groupUser);
//        this.reload();
    }

    public AppGroupUser getGroupUser(Integer groupUserId) {
        return cache.getGroupUser(groupUserId);
    }

    public boolean isGroupUser(String groupAlphaId, Integer userId, boolean isGroup) {
        return cache.isGroupUser(groupAlphaId, userId, isGroup);
    }
}
