/**
 * <p>Title: UserManagerDAO</p>
 * <p>Description:</p>
 * @author bgude
 * @version 1.0
 */

package com.businesshaps.am.server.user;

import com.businesshaps.am.businessobjects.AppUser;
import com.businesshaps.am.server.AppProperties;
import com.businesshaps.am.tools.GEncrypt;
import com.businesshaps.oi.ObInject;

import java.util.Arrays;
import java.util.Comparator;

public class UserManagerDAO implements UserManager {
    private AppProperties props;
    private ObInject obInject;

    public UserManagerDAO(AppProperties props, String jdbc) {
        this.props = props;
        obInject = ObInject.getInstance(jdbc);
    }

    public AppUser[] getUser() {
        AppUser[] rtns = (AppUser[]) obInject.get(AppUser.class, "");
        for (AppUser u : rtns) {
            u.setPassword(GEncrypt.decrypt(u.getPassword()));
        }
        Arrays.sort(rtns, new Comparator<AppUser>() {
            public int compare(AppUser o1, AppUser o2) {
            	return new Integer(o2.getId()).compareTo(o1.getId());
            }
        });
        return rtns;
    }

    public AppUser setUser(AppUser user) {
        user.setPassword(GEncrypt.encrypt(user.getPassword()));
        user = (AppUser) obInject.set(user);
        user.setPassword(GEncrypt.decrypt(user.getPassword()));
        return user;
    }

    public AppUser getUserByUserId(Integer userId) {
        return null;
    }

    public AppUser getUserByUsername(String username) {
        return null;
    }

    public AppUser getUserByEmail(String email) {
        return null;
    }


    public void reload() {

    }

    public AppUser[] getUserBySupervisorUsername(String username) {
        return new AppUser[0];
    }


    public AppUser[] getUserByFilter(String departments, String groupAlphaIds, String jobTitles, String softwareAccessGroupAlphaIds, String supervisorUsernames, String textSearch, boolean beingProvisioned) {
        return new AppUser[0];
    }

    public void delete(AppUser user) {

    }
}
