/**
 * <p>Title: GroupTypeManagerBusiness</p>
 * <p>Description:</p>
 * @author bgude
 * @version 1.0
 */

package com.businesshaps.am.server.group.grouptype;

import com.businesshaps.am.businessobjects.AppGroupType;
import com.businesshaps.am.server.AppProperties;

public class GroupTypeManagerBusiness implements GroupTypeManager {
    private AppProperties props;
    private GroupTypeManagerDAO dao;
    private GroupTypeManager cache;

    public GroupTypeManagerBusiness(AppProperties props) {
        this.props = props;
        this.reload();
    }


    public AppGroupType[] getGroupType() {
        return cache.getGroupType();
    }

    public AppGroupType getGroupTypeByGroupTypeAlphaId(String groupTypeAlphaId) {
        return cache.getGroupTypeByGroupTypeAlphaId(groupTypeAlphaId);
    }

    public AppGroupType getGroupTypeByGroupTypeId(Integer groupTypeId) {
        return cache.getGroupTypeByGroupTypeId(groupTypeId);
    }

    public AppGroupType setGroupType(AppGroupType groupType) {
        groupType = dao.setGroupType(groupType);
        cache.setGroupType(groupType);
        return groupType;
    }

    public void reload() {
        dao = new GroupTypeManagerDAO(props, props.getJdbc());
        GroupTypeManagerCache tmp_cache = new GroupTypeManagerCache();

        for (AppGroupType groupType : dao.getGroupType()) {
            tmp_cache.setGroupType(groupType);
        }
        cache = tmp_cache;

    }
}
