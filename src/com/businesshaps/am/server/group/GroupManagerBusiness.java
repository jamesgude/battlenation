/**
 * <p>Title: GroupManagerBusiness</p>
 * <p>Description:</p>
 * @author bgude
 * @version 1.0
 */

package com.businesshaps.am.server.group;

import com.businesshaps.am.businessobjects.AppGroup;
import com.businesshaps.am.server.AppProperties;

public class GroupManagerBusiness implements GroupManager {
    private AppProperties props;
    private GroupManagerDAO dao;
    private GroupManager cache;

    public GroupManagerBusiness(AppProperties props) {
        this.props = props;
        this.reload();
    }

    public AppGroup[] getGroup() {
        return cache.getGroup();
    }

    public AppGroup getGroupByAlphaId(String groupAlphaId) {
        return cache.getGroupByAlphaId(groupAlphaId);
    }

    public AppGroup getGroupByGroupId(Integer groupId) {
        return cache.getGroupByGroupId(groupId);
    }

    public AppGroup setGroup(AppGroup group) {
        group = dao.setGroup(group);
        cache.setGroup(group);
        return group;
    }

    public void reload() {
        dao = new GroupManagerDAO(props, props.getJdbc());
        GroupManagerCache tmp_cache = new GroupManagerCache();

        for (AppGroup group: dao.getGroup()) {
            tmp_cache.setGroup(group);
        }
        cache = tmp_cache;
    }

    public AppGroup[] getGroupByGroupTypeAlphaId(String groupTypeAlphaId) {
        return cache.getGroupByGroupTypeAlphaId(groupTypeAlphaId);
    }

    public AppGroup[] getGroupByFilter(String parentGroupAlphaIds, String groupTypeAlphaIds, String textSearch) {
        return cache.getGroupByFilter(parentGroupAlphaIds, groupTypeAlphaIds, textSearch);
    }

    public AppGroup getGroupByURL(String url) {
        return cache.getGroupByURL(url);
    }

    public void delete(AppGroup group) {
        cache.delete(group);
        dao.delete(group);
        this.reload();
    }
    public AppGroup[] getGroupByParentGroupAlphaId(String groupAlphaId) {
        return cache.getGroupByParentGroupAlphaId(groupAlphaId);
    }

    public AppGroup[] getChildren(String groupAlphaId) {
        return cache.getChildren(groupAlphaId);
    }

    public AppGroup[] getGroupByOwner(String owner) {
        return cache.getGroupByOwner(owner);
    }
}
