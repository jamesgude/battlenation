/**
 * <p>Title: UserManager</p>
 * <p>Description:</p>
 * @author bgude
 * @version 1.0
 */

package com.businesshaps.am.server.user;

import com.businesshaps.am.businessobjects.AppUser;
import com.businesshaps.am.server.AppComponent;

public interface UserManager extends AppComponent {
    public AppUser[] getUser();

    public AppUser getUserByUsername(String username);

    public AppUser getUserByEmail(String email);

    public AppUser getUserByUserId(Integer userId);
    public AppUser setUser(AppUser user);
    public void delete(AppUser user);
    public AppUser[] getUserBySupervisorUsername(String username);

    public AppUser[] getUserByFilter(String departments
            , String groupAlphaIds
            , String jobTitles
            , String softwareAccessGroupAlphaIds
            , String supervisorUsernames
            , String textSearch
            , boolean beingProvisioned
    );

}
