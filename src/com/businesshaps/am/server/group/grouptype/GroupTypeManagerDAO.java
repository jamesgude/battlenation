/**
 * <p>Title: GroupTypeManagerDAO</p>
 * <p>Description:</p>
 * @author bgude
 * @version 1.0
 */

package com.businesshaps.am.server.group.grouptype;

import com.businesshaps.am.businessobjects.AppGroupType;
import com.businesshaps.am.server.AppProperties;
import com.businesshaps.oi.ObInject;

import java.util.Arrays;
import java.util.Comparator;

public class GroupTypeManagerDAO implements GroupTypeManager{
    private AppProperties props;
    private ObInject obInject;

    public GroupTypeManagerDAO(AppProperties props, String jdbc) {
        this.props = props;
        obInject = ObInject.getInstance(jdbc);
    }


    public AppGroupType[] getGroupType() {
        AppGroupType[] rtns = (AppGroupType[]) obInject.get(AppGroupType.class, "");
        Arrays.sort(rtns, new Comparator<AppGroupType>() {
            public int compare(AppGroupType o1, AppGroupType o2) {
            	return new Integer(o2.getId()).compareTo(o1.getId());
            } 
        });
        return rtns;
    }

    public AppGroupType getGroupTypeByGroupTypeAlphaId(String groupTypeAlphaId) {
        return null;
    }

    public AppGroupType getGroupTypeByGroupTypeId(Integer groupTypeId) {
        return null; 
    }

    public AppGroupType setGroupType(AppGroupType groupType) {
        groupType = (AppGroupType) obInject.set(groupType);
        return groupType;
    }

    public void reload() {

    }
}
