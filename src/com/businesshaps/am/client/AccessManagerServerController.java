/**
 * <p>Title: AccessManagerServerController</p>
 * <p>Description:</p>
 * @author bgude
 * @version 1.0
 */

package com.businesshaps.am.client;

import com.businesshaps.am.businessobjects.AppGroup;
import com.businesshaps.am.businessobjects.AppGroupUser;
import com.businesshaps.am.businessobjects.AppUser;
import com.businesshaps.am.businessobjects.AppUserLog;
import com.businesshaps.am.server.AppServer;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class AccessManagerServerController implements AccessManagerInstanceController {
    private static AccessManagerServerController instance = null;
    private String applicationURL = null;
    private String realm = "";
    private String username = null;

    public static AccessManagerServerController getInstance(String realm) {
        if (instance == null) {
            instance = new AccessManagerServerController(realm);
        }
        return instance;
    }

    public static AccessManagerServerController getInstance() {
        if (instance == null) {
            instance = new AccessManagerServerController("Unknown");
        }
        return instance;
    }

    private AccessManagerServerController(String realm) {
//        System.out.println("###   Access Client: " + realm);
    }

    public String getApplicationURL() {
        return applicationURL;
    }

    public void setApplicationURL(String applicationURL) {
        this.applicationURL = applicationURL;
    }

    public boolean isUserTokenActive(String user_token) {
        Boolean active = new Boolean(false);
        AppServer server = AppServer.getInstance();
        if (user_token==null) {
            return false;
        }

        active = server.getAuthManager().checkUserToken(user_token);

        return active.booleanValue();
    }

    public boolean hasAuthorization(String user_token, String url) {
//        System.out.println(user_token + " - " + url);
        Boolean active = new Boolean(false);
        AppServer server = AppServer.getInstance();
        try {
            AppUser user = server.getAuthManager().getUserByUserToken(user_token);
            AppGroup group = null;
            if (url!=null) {
                group = server.getGroupManager().getGroupByURL(url); //TODO: This is going to loop thru every group item in the system to check for the correct url EVERY TIME a page is clicked...
            }

            for (AppGroup grp : server.getGroupManager().getGroupByGroupTypeAlphaId("GRP_TYPE_APPLICATION")) {
//      out.println(grp.getGroupId() + " - " + grp.getGroupAlphaId()+ "<br>");
            }

            if (group != null && group.isRestricted()) {
                if (user != null&&group.isActive()) {
                    //    out.println(group.getGroupAlphaId());
                    String group_alpha_id = group.getGroupAlphaId();
                    Integer user_id = user.getId();
                    if (group.getOwner()!=null&&group.getOwner().equals(user.getUsername())) {
                        active = true;
                    } else {
                        active = server.getGroupUserManager().isGroupUser(group_alpha_id, user_id, false);
                    }
                } else {
                    active = false;
                }
            } else {
                active = true;
            }
        } catch (Exception error) {
            error.printStackTrace();
        }        return active.booleanValue();
    }

    public boolean isGroupMember(String username, String groupAlphaId) {
//        System.out.println(user_token + " - " + url);
        Boolean active = new Boolean(false);
        AppServer server = AppServer.getInstance();
        try {
            AppUser user = server.getUserManager().getUserByUsername(username);
            AppGroup group = server.getGroupManager().getGroupByAlphaId(groupAlphaId.toLowerCase());

//            System.out.println("user: " + user);
//            System.out.println("group: " + group);
            if (group != null) {
                if (user != null) {
                    String group_alpha_id = group.getGroupAlphaId();
                    Integer user_id = user.getId();
                    boolean has_access = false;
                    if (user_id.equals(group.getOwner())) {
                        has_access = true;
                    } else {
                        has_access = server.getGroupUserManager().isGroupUser(group_alpha_id.toLowerCase(), user_id, false);
                    }
//                    System.out.println("has access: " + has_access);
                    active = has_access;
                } else {
                    active = false;
                }
            } else {
                active = true;
            }
        } catch (Exception error) {
            error.printStackTrace();
        }        return active.booleanValue();
    }

    public String getUsernameByToken(String user_token) {
//        System.out.println(user_token + " - " + url);
        String username = null;
        AppServer server = AppServer.getInstance();
        try {
            AppUser user = server.getAuthManager().getUserByUserToken(user_token);
            if (user!=null) {
                username = user.getUsername();
            }
        } catch (Exception error) {
            error.printStackTrace();
        }
        return username;
    }

    private SimpleDateFormat format = new SimpleDateFormat("E MM/dd/yyyy hh:mm:ss aaa ");

    public void logUser(final String user_token, final String access_point, final String access_point_addr, final String domain, final String user_ip, final boolean authorized, final String browser, final String queryString) {
        Thread thread = (new Thread() {
            public void run() {
                try {

                    AppServer server = AppServer.getInstance();

                    AppUser user = server.getAuthManager().getUserByUserToken(user_token);
                    String username = null;
                    if (user!=null&&user.getId()>0) {
                        user.setLastAccessed(new Date());
                        username = user.getUsername();
                    }
//                    user = server.getUserManager().setUser(user);
//                    server.getUserManager().reload();

                    if (!access_point_addr.contains("/layout/") && !access_point_addr.contains("robots.txt")) {
                        System.out.println("Request: " + user_ip + " " + format.format(new Date()) + " " + domain + "" + access_point_addr + (queryString!=null?"?"+queryString:"") + " - " + browser);
                    }

                    AppGroup group = server.getGroupManager().getGroupByURL(access_point_addr);
                    if (access_point_addr!=null && !access_point_addr.contains("/list") && !access_point_addr.contains("/appuserlog")) {
                        AppUserLog userLog = new AppUserLog();
                        userLog.setLogDate(new Date());
                        userLog.setWebpage(access_point_addr);
                        userLog.setDomain(domain);
                        userLog.setAccessPoint(access_point);
                        userLog.setIp(user_ip);
                        userLog.setBrowser(browser);
                        userLog.setUsername(username);
                        userLog.setAuthorized(authorized);
                        if (browser.toLowerCase().contains("bot") || browser.toLowerCase().contains("slurp")) {
                            userLog.setBot(true);
                        } else {
                            userLog.setBot(false);
                        }
                        server.getUserLogManager().setUserLog(userLog);
                        if (user != null && user.getId() > 0) {
                            AppGroupUser agu = server.getGroupUserManager().getGroupUser(group.getGroupAlphaId(), user.getId(), false);
                            if (agu==null) {
                                agu = server.getGroupUserManager().getGroupUser(group.getGroupAlphaId(), user.getEmail());
                            }
                            if (agu!=null) {
                                agu.setLastAccessed(new Date());
                                server.getGroupUserManager().setGroupUser(agu);
                            }
                        }
                    }

                } catch (Exception error) {
                    System.out.println(error);
                }
            }
        });
        thread.start();
        thread = null;
    }

    public AppGroup getGroupByURL(String url) {
        return AppServer.getInstance().getGroupManager().getGroupByURL(url);
    }

    public AppUser getUser(String username) {
        AppUser user = null;
        if (username!=null) {
            user = AppServer.getInstance().getUserManager().getUserByUsername(username);
        }
        return user;
    }

    public String getRealm() {
        return realm;
    }

    public void setRealm(String realm) {
        this.realm = realm;
    }

    public AppUser[] getUserByURL(String url) {
        AppGroup group = this.getGroupByURL(url);
        AppGroupUser[] groupUsers = null;
        ArrayList<Integer> user_ids = new ArrayList<Integer>();
        if (group!=null) {
            groupUsers = AppServer.getInstance().getGroupUserManager().getGroupUserByGroupAlphaId(group.getGroupAlphaId());
            for (AppGroupUser groupUser : groupUsers) {
                user_ids.add(groupUser.getUserId());
            }
        }
        AppUser[] rtn = new AppUser[user_ids.size()];
        int i = 0;
        for (Integer user_id : user_ids) {
            rtn[i] = AppServer.getInstance().getUserManager().getUserByUserId(user_id);
            i++;
        }
        return rtn;
    }
}
