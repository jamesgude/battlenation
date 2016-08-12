/**
 * <p>Title: GroupTypeManager</p>
 * <p>Description:</p>
 * @author bgude
 * @version 1.0
 */

package com.businesshaps.am.server.group.grouptype;

import com.businesshaps.am.businessobjects.AppGroupType;
import com.businesshaps.am.server.AppComponent;

public interface GroupTypeManager extends AppComponent {
    public AppGroupType[] getGroupType();

    public AppGroupType getGroupTypeByGroupTypeAlphaId(String groupTypeAlphaId);

    public AppGroupType getGroupTypeByGroupTypeId(Integer groupTypeId);

    public AppGroupType setGroupType(AppGroupType groupType);
}
