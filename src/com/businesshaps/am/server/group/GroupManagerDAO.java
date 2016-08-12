/**
 * <p>Title: GroupManagerDAO</p>
 * <p>Description:</p>
 * @author bgude
 * @version 1.0
 */

package com.businesshaps.am.server.group;

import com.businesshaps.am.businessobjects.AppGroup;
import com.businesshaps.am.server.AppProperties;
import com.businesshaps.oi.ObInject;

import java.util.Arrays;
import java.util.Comparator;

public class GroupManagerDAO implements GroupManager {
    private AppProperties props;
    private ObInject obInject;

    public GroupManagerDAO(AppProperties props, String jdbc) {
        this.props = props;
        obInject = ObInject.getInstance(jdbc);
    }

    public AppGroup[] getGroup() {
        AppGroup[] rtns = (AppGroup[]) obInject.get(AppGroup.class, "");
        Arrays.sort(rtns, new Comparator<AppGroup>() {
            public int compare(AppGroup o1, AppGroup o2) {
                return new Integer(o2.getId()).compareTo(o1.getId());
            }
        });
        return rtns;
    }

    public AppGroup getGroupByAlphaId(String groupAlphaId) {
        return null;
    }

    public AppGroup getGroupByGroupId(Integer groupId) {
        return null;
    }

    public AppGroup setGroup(AppGroup group) {
        group = (AppGroup) obInject.set(group);
        return group;
    }

    public void reload() {

    }

    public AppGroup[] getGroupByGroupTypeAlphaId(String groupTypeAlphaId) {
        return null;
    }

    public AppGroup[] getGroupByFilter(String parentGroupAlphaIds, String groupTypeAlphaIds, String textSearch) {
        return new AppGroup[0];
    }

    public AppGroup getGroupByURL(String url) {
        return null;
    }

    public void delete(AppGroup group) {

    }

    public AppGroup[] getGroupByParentGroupAlphaId(String groupAlphaId) {
        return new AppGroup[0];
    }

    public AppGroup[] getChildren(String groupAlphaId) {
        return new AppGroup[0];
    }

    public AppGroup[] getGroupByOwner(String owner) {
        return new AppGroup[0];
    }
}
