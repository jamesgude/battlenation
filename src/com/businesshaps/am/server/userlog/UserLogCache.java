/**
 * <p>Title: UserLogCache</p>
 * <p>Description:</p>
 * @author bgude
 * @version 1.0
 */

package com.businesshaps.am.server.userlog;

import com.businesshaps.am.businessobjects.AppUserLog;

public class UserLogCache implements UserLogManager{
    public UserLogCache() {

    }

    public AppUserLog[] getUserLog() {
        return new AppUserLog[0];
    }

    public AppUserLog[] getUserLogByAccessPoint(String accessPoint) {
        return new AppUserLog[0];
    }

    public AppUserLog getUserLogByUserLogId(Integer userLogId) {
        return null;
    }

    public AppUserLog[] getUserLogByUsername(String username) {
        return new AppUserLog[0];
    }

    public AppUserLog setUserLog(AppUserLog userLog) {
        return null;
    }

    public void reload() {

    }

}
