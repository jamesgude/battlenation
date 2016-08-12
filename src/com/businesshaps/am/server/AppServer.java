/**
 * <p>Title: AppServer</p>
 * <p>Description:</p>
 * @author bgude
 * @version 1.0
 */

package com.businesshaps.am.server;

import com.businesshaps.am.businessobjects.*;
import com.businesshaps.am.server.auth.AuthManager;
import com.businesshaps.am.server.group.GroupManager;
import com.businesshaps.am.server.group.GroupManagerBusiness;
import com.businesshaps.am.server.group.grouptype.GroupTypeManager;
import com.businesshaps.am.server.group.grouptype.GroupTypeManagerBusiness;
import com.businesshaps.am.server.group.groupuser.GroupUserManager;
import com.businesshaps.am.server.group.groupuser.GroupUserManagerBusiness;
import com.businesshaps.am.server.user.UserManager;
import com.businesshaps.am.server.user.UserManagerBusiness;
import com.businesshaps.am.server.userlog.UserLogBusiness;
import com.businesshaps.am.server.userlog.UserLogManager;
import com.businesshaps.oi.ObInject;

import java.io.File;

public class AppServer {
    private static AppServer instance = null;

    public static AppServer getInstance() {
        if (instance == null) {
            instance = new AppServer();
        }
        return instance;
    }

    private UserManager userManager = null;
    private GroupManager groupManager = null;
    private GroupUserManager groupUserManager = null;
    private GroupTypeManager groupTypeManager = null;
    private AuthManager authManager = null;
    private UserLogManager userLogManager = null;
    private AppProperties properties = null;

    private AppServer() {

    }

    public void setProperties(AppProperties properties) {
        try {
            this.properties = properties;
            if (!properties.isServer()) {
                return;
            }

//        this.properties.setSyncActiveDirectory(true);
            ObInject obInject = ObInject.getInstance(properties.getJdbc());
            obInject.syncTable(AppGroup.class);
            obInject.syncTable(AppGroupType.class);
            obInject.syncTable(AppGroupUser.class);
            obInject.syncTable(AppUser.class);
            obInject.syncTable(AppUserLog.class);
            obInject.syncTable(AppOrganization.class);

            if (userManager == null) {

                userManager = new UserManagerBusiness(this.properties);
                if (userManager.getUser().length == 0) {
                    DefaultValues.setDefaultUser(userManager, this.properties);
                }

                groupManager = new GroupManagerBusiness(this.properties);
                if (groupManager.getGroup().length == 0) {
                    DefaultValues.setDefaultGroup(groupManager, this.properties);
                }

                groupTypeManager = new GroupTypeManagerBusiness(this.properties);
                if (groupTypeManager.getGroupType().length == 0) {
                    DefaultValues.setDefaultGroupType(groupTypeManager, this.properties);
                }

                groupUserManager = new GroupUserManagerBusiness(this.properties);
                if (groupUserManager.getGroupUser().length == 0) {
                    DefaultValues.setDefaultGroupUser(groupUserManager, this.properties);
                }

                authManager = new AuthManager(userManager, new File(properties.getHomeDirectory().getAbsolutePath()));

                userLogManager = new UserLogBusiness(this.properties);
                if (this.properties.isSyncActiveDirectory()) {
                } else {
                    Thread thread = (new Thread() {
                        public void run() {
                            while (true) {
                                try {
                                    Thread.sleep(60000);
                                } catch (Exception e) {

                                }
                                getGroupUserManager().reload();
                                getGroupManager().reload();
                                getUserManager().reload();
                            }

                        }
                    });
                    thread.start();
                }
            }
        } catch (Exception error) {
            error.printStackTrace();
        }
    }

    public AppProperties getAppProperties() {
        return this.properties;
    }

    public AuthManager getAuthManager() {
        return authManager;
    }

    public UserManager getUserManager() {
        return this.userManager;
    }

    public GroupManager getGroupManager() {
        return groupManager;
    }

    public GroupTypeManager getGroupTypeManager() {
        return groupTypeManager;
    }

    public GroupUserManager getGroupUserManager() {
        return groupUserManager;
    }

    public UserLogManager getUserLogManager() {
        return userLogManager;
    }


}
