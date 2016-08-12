/**
 * <p>Title: UserLogManager</p>
 * <p>Description:</p>
 * @author bgude
 * @version 1.0
 */

package com.businesshaps.am.server.userlog;

import com.businesshaps.am.businessobjects.AppUserLog;
import com.businesshaps.am.server.AppComponent;

public interface UserLogManager extends AppComponent {
    public AppUserLog[] getUserLog();

    public AppUserLog[] getUserLogByUsername(String username);

    public AppUserLog[] getUserLogByAccessPoint(String accessPoint);

    public AppUserLog getUserLogByUserLogId(Integer userLogId);

    public AppUserLog setUserLog(AppUserLog userLog);
}
