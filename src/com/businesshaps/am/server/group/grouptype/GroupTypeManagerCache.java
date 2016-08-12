/**
 * <p>Title: GroupTypeManagerCache</p>
 * <p>Description:</p>
 * @author bgude
 * @version 1.0
 */

package com.businesshaps.am.server.group.grouptype;

import com.businesshaps.am.businessobjects.AppGroupType;

import java.util.Hashtable;
import java.util.Arrays;
import java.util.Comparator;

public class GroupTypeManagerCache implements GroupTypeManager{
    private Hashtable<Integer, AppGroupType> groupTypes;
    private Hashtable<String, AppGroupType> groupTypeByGroupTypeAlphaId;

    public GroupTypeManagerCache() {
        this.reload();
    }

    public AppGroupType[] getGroupType() {
        AppGroupType[] rtns = this.groupTypes.values().toArray(new AppGroupType[this.groupTypes.values().size()]);
        Arrays.sort(rtns, new Comparator<AppGroupType>() {
            public int compare(AppGroupType o1, AppGroupType o2) {
            	return new Integer(o2.getId()).compareTo(o1.getId());
            }
        });

        return rtns;
    }

    public AppGroupType getGroupTypeByGroupTypeAlphaId(String groupTypeAlphaId) {
        return this.groupTypeByGroupTypeAlphaId.get(groupTypeAlphaId);
    }

    public AppGroupType getGroupTypeByGroupTypeId(Integer groupTypeId) {
        return this.groupTypes.get(groupTypeId);
    }

    public AppGroupType setGroupType(AppGroupType groupType) {
        this.groupTypes.put(groupType.getId(), groupType);
        return groupType;
    }

    public void reload() {
        this.groupTypes = new Hashtable<Integer, AppGroupType>();
        this.groupTypeByGroupTypeAlphaId = new Hashtable<String, AppGroupType>();

    }
}
