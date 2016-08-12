/**
 * <p>Title: GroupManager</p>
 * <p>Description:</p>
 * @author bgude
 * @version 1.0
 */

package com.businesshaps.am.server.group;

import com.businesshaps.am.businessobjects.AppGroup;
import com.businesshaps.am.server.AppComponent;

public interface GroupManager extends AppComponent {
    public AppGroup[] getGroup();

    public AppGroup getGroupByAlphaId(String groupAlphaId);

    public AppGroup[] getGroupByGroupTypeAlphaId(String groupTypeAlphaId);

    public AppGroup[] getGroupByOwner(String owner);

    public AppGroup getGroupByGroupId(Integer groupId);

    public AppGroup setGroup(AppGroup group);

    public AppGroup[] getGroupByFilter(String parentGroupAlphaIds, String groupTypeAlphaIds, String textSearch);

    public AppGroup getGroupByURL(String url);

    public AppGroup[] getGroupByParentGroupAlphaId(String groupAlphaId);

    public void delete(AppGroup appGroup);

    public AppGroup[] getChildren(String groupAlphaId);


}
