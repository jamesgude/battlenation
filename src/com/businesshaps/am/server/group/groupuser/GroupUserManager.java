/**
 * <p>Title: GroupUserManager</p>
 * <p>Description:</p>
 * @author bgude
 * @version 1.0
 */

package com.businesshaps.am.server.group.groupuser;

import com.businesshaps.am.businessobjects.AppGroupUser;
import com.businesshaps.am.server.AppComponent;

public interface GroupUserManager extends AppComponent {
    public AppGroupUser[] getGroupUser();

    public AppGroupUser[] getGroupUserByGroupAlphaId(String groupAlphaId);

    public AppGroupUser[] getGroupByUserId(Integer userId, boolean isGroup);

    public AppGroupUser getGroupUser(Integer groupUserId);

    public AppGroupUser[] getGroupUserByEmail(String email);

    public AppGroupUser getGroupUser(String groupAlphaId, Integer userId, boolean isGroup);

    public AppGroupUser getGroupUser(String groupAlphaId, String email);

    public boolean isGroupUser(String groupAlphaId, Integer userId, boolean isGroup);

    public AppGroupUser setGroupUser(AppGroupUser groupUser);

    public void delete(AppGroupUser groupUser);

}
 