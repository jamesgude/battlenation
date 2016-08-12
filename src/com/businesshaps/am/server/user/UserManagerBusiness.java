/**
 * <p>Title: UserManagerBusiness</p>
 * <p>Description:</p>
 * @author bgude
 * @version 1.0
 */

package com.businesshaps.am.server.user;

import com.businesshaps.am.businessobjects.AppUser;
import com.businesshaps.am.server.AppProperties;

public class UserManagerBusiness implements UserManager {
    private AppProperties props;
    private UserManagerDAO dao;
    private UserManager cache;

    public UserManagerBusiness(AppProperties props) {
        this.props = props;
        this.reload();
    }

    public AppUser[] getUser() {
        return cache.getUser();
    }

    public AppUser setUser(AppUser user) {
//        user.setPassword(GEncrypt.encrypt(user.getPassword()));

        user = dao.setUser(user);
        cache.setUser(user);
        return user;
    }

    public AppUser getUserByUserId(Integer userId) {
        return cache.getUserByUserId(userId);
    }

    public AppUser getUserByUsername(String username) {
        return cache.getUserByUsername(username);
    }

    public AppUser getUserByEmail(String email) {
        return cache.getUserByEmail(email);
    }

    public void reload() {
        dao = new UserManagerDAO(props, props.getJdbc());
        UserManagerCache tmp_cache = new UserManagerCache();

        for (AppUser user : dao.getUser()) {
            tmp_cache.setUser(user);
//            System.out.println("Loading User [" + user.getUsername() + "]");
        }
        cache = tmp_cache;
    }

    public AppUser[] getUserBySupervisorUsername(String username) {
        return cache.getUserBySupervisorUsername(username);
    }


    public AppUser[] getUserByFilter(String departments, String groupAlphaIds, String jobTitles, String softwareAccessGroupAlphaIds, String supervisorUsernames, String textSearch, boolean beingProvisioned) {
        return cache.getUserByFilter(departments, groupAlphaIds,  jobTitles, softwareAccessGroupAlphaIds, supervisorUsernames, textSearch, beingProvisioned);
    }

    public void delete(AppUser user) {
        cache.delete(user);
        dao.delete(user);
        this.reload();
    }
}
