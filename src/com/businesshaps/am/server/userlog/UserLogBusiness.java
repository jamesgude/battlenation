/**
 * <p>Title: UserLogBusiness</p>
 * <p>Description:</p>
 * @author bgude
 * @version 1.0
 */

package com.businesshaps.am.server.userlog;

import com.businesshaps.am.businessobjects.AppUserLog;
import com.businesshaps.am.server.AppProperties;

public class UserLogBusiness implements UserLogManager {
    private AppProperties props;
    private UserLogDAO dao;
    private UserLogManager cache;

    public UserLogBusiness(AppProperties props) {
        this.props = props;
        this.reload();
    }

    public AppUserLog[] getUserLog() {
        return dao.getUserLog();
    }

    public AppUserLog[] getUserLogByAccessPoint(String accessPoint) {
        return dao.getUserLogByAccessPoint(accessPoint);
    }

    public AppUserLog getUserLogByUserLogId(Integer userLogId) {
        return dao.getUserLogByUserLogId(userLogId);
    }

    public AppUserLog[] getUserLogByUsername(String username) {
        return dao.getUserLogByUsername(username);
    }

    public AppUserLog setUserLog(AppUserLog userLog) {
        userLog = dao.setUserLog(userLog);
//        cache.setUserLog(userLog);
        return userLog;
    }

    public void reload() {
        dao = new UserLogDAO(props, props.getJdbc());
        cache = new UserLogCache();

        for (AppUserLog userLog : dao.getUserLog()) {
//            cache.setUserLog(userLog);
        }

    }
}
