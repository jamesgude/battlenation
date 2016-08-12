/**
 * <p>Title: AccessManagerController</p>
 * <p>Description:</p>
 * @author bgude
 * @version 1.0
 */

package com.businesshaps.am.client;

import com.businesshaps.am.businessobjects.AppGroup;
import com.businesshaps.am.businessobjects.AppUser;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Hashtable;

public class AccessManagerController implements AccessManagerInstanceController {
    private static AccessManagerController instance = null;
    private String applicationURL = null;
    private String realm = "";
    private String username = null;

    public static AccessManagerController getInstance(String realm) {
        if (instance == null) {
            instance = new AccessManagerController(realm);
        }
        return instance;
    }

    public static AccessManagerController getInstance() {
        if (instance == null) {
            instance = new AccessManagerController("Unknown");
        }
        return instance;
    }

    private AccessManagerController(String realm) {
    }

    public String getApplicationURL() {
        return applicationURL;
    }

    public void setApplicationURL(String applicationURL) {
        this.applicationURL = applicationURL;
    }
    private String[] getWebData(String url) {
        Hashtable<Integer, String> data = new Hashtable<Integer, String>();
        try {
            URL authurl = new URL(url);
            BufferedReader authin = new BufferedReader(new InputStreamReader(authurl.openStream()));
            String authInputLine;
            while ((authInputLine = authin.readLine()) != null) {
                data.put(data.size(), authInputLine);
            }
            authin.close();
        } catch (Exception error) {
            System.out.println(error);
        }
        String[] rtn = new String[data.size()];
        for (int i=0;i<data.size();i++) {
            rtn[i] = data.get(i);
        }
        return rtn;
    }

    public boolean isUserTokenActive(String user_token) {
        Boolean active = new Boolean(false);
        active = new Boolean(getWebData(applicationURL + "/service/isUserTokenActive?user_token=" + user_token)[0]);
        /*
        try {
            URL authurl = new URL(applicationURL + "/service/isUserTokenActive?user_token=" + user_token);
            BufferedReader authin = new BufferedReader(new InputStreamReader(authurl.openStream()));
            String authInputLine;
            while ((authInputLine = authin.readLine()) != null) {
                if (authInputLine.trim().length() > 0) {
                    active = new Boolean(authInputLine);
                }
            }
            authin.close();
        } catch (Exception error) {
        }
        */
        return active.booleanValue();
    }

    public boolean hasAuthorization(String user_token, String url) {
        Boolean active = new Boolean(false);
        active = new Boolean(getWebData(applicationURL + "/service/hasAuthorization?user_token=" + user_token + "&url=" + url)[0]);
        /*try {
            URL authurl = new URL(applicationURL + "/service/hasAuthorization?user_token=" + user_token + "&url=" + url);
            BufferedReader authin = new BufferedReader(new InputStreamReader(authurl.openStream()));
            String authInputLine;
            while ((authInputLine = authin.readLine()) != null) {
                if (authInputLine.trim().length() > 0) {
                    active = new Boolean(authInputLine);
                }
            }
            authin.close();
        } catch (Exception error) {
        }
        */
        return active.booleanValue();
    }

    public boolean isGroupMember(String username, String groupAlphaId) {
        Boolean active = new Boolean(false);
        active = new Boolean(getWebData(applicationURL + "/service/isGroupMember?username=" + username + "&group_alpha_id=" + groupAlphaId)[0]);
        /*
        try {
            URL authurl = new URL(applicationURL + "/service/isGroupMember?username=" + username + "&group_alpha_id=" + groupAlphaId);
            BufferedReader authin = new BufferedReader(new InputStreamReader(authurl.openStream()));
            String authInputLine;
            while ((authInputLine = authin.readLine()) != null) {
                if (authInputLine.trim().length() > 0) {
                    active = new Boolean(authInputLine);
                }
            }
            authin.close();
        } catch (Exception error) {
        }
        */
        return active.booleanValue();
    }

    public String getUsernameByToken(String user_token) {
        String username = null;
        if (user_token!=null) {
            String[] usernames = getWebData(applicationURL + "/service/getUsernameByToken?user_token=" + user_token);
            if (usernames.length>0) {
                username = usernames[0];
            }
        }
        /*
        try {
            URL authurl = new URL(applicationURL + "/service/getUsernameByToken?user_token=" + user_token);
            BufferedReader authin = new BufferedReader(new InputStreamReader(authurl.openStream()));
            String authInputLine;
            while ((authInputLine = authin.readLine()) != null) {
                if (authInputLine.trim().length() > 0) {
                    username = authInputLine;
                }
            }
            authin.close();
        } catch (Exception error) {
        }
        */
        return username;
    }

    public void logUser(final String user_token, final String access_point, final String access_point_addr, final String domain, final String user_ip, final boolean authorized, final String browser, String queryString) {
        Thread thread = (new Thread() {
            public void run() {
                getWebData(applicationURL + "/service/logUser?user_token=" + user_token + "&access_point=" + access_point + "&access_point_addr=" + access_point_addr + "&user_ip=" + user_ip + "&authorized=" + authorized);
                /*
                try {
                    URL authurl = new URL(applicationURL + "/service/logUser?user_token=" + user_token + "&access_point=" + access_point + "&access_point_addr=" + access_point_addr + "&user_ip=" + user_ip + "&authorized=" + authorized);
                    BufferedReader authin = new BufferedReader(new InputStreamReader(authurl.openStream()));
                    authin.close();

                } catch (Exception error) {
                    System.out.println(error);
                }
                */
            }
        });
        thread.start();
        thread = null;
    }

    public AppGroup getGroupByURL(String url) {
//        System.out.println(user_token + " - " + url);
        AppGroup group = new AppGroup();
        String[] groupData = getWebData(applicationURL + "/service/getGroupByURL?url=" + url);
        String field;
        String value;
        String[] vsplit;
        for (String data : groupData) {
            vsplit = data.split(":::");
            field = vsplit[0];
            value = vsplit[1];
            value = value.trim(); 

            if (field.toLowerCase().equals("getgroupalphaid")) {
              group.setGroupAlphaId(value);
            } else if (field.toLowerCase().equals("getgroupname")) {
                group.setGroupName(value);
            } else if (field.toLowerCase().equals("getdomain")) {
                group.setDomain(value);
            } else if (field.toLowerCase().equals("getwebsite")) {
                group.setWebsite(value);
            } else if (field.toLowerCase().equals("getgrouptypealphaid")) {
                group.setGroupTypeAlphaId(value);
            } else if (field.toLowerCase().equals("isactive")) {
                group.setActive(new Boolean(value));
            } else if (field.toLowerCase().equals("isallowAnonAccess")) {
                group.setAllowAnonAccess(new Boolean(value));
            } else if (field.toLowerCase().equals("isrestricted")) {
                group.setRestricted(new Boolean(value));
            } else if (field.toLowerCase().equals("isrestrictlayout")) {
                group.setRestrictLayout(new Boolean(value));
            } else if (field.toLowerCase().equals("isminimumlayout")) {
                group.setMinimumLayout(new Boolean(value));
            }

            //group.setGroupAlphaId(data);
        }

        /*
        try {
            URL authurl = new URL(applicationURL + "/service/getGroupByURL?url=" + url);
            BufferedReader authin = new BufferedReader(new InputStreamReader(authurl.openStream()));
            String authInputLine;
            while ((authInputLine = authin.readLine()) != null) {
                if (authInputLine.trim().length() > 0) {
                    //username = authInputLine;
                    group.setGroupAlphaId(authInputLine);
                }
            }
            authin.close();
        } catch (Exception error) {
            System.out.println(error);
        }
        */
        return group;
    }

    public AppUser[] getUserByURL(String url) {
        AppUser[] users = new AppUser[0];
        String[] groupData = getWebData(applicationURL + "/service/getUserByURL?url=" + url);
        String id;
        String field;
        String value;
        String[] vsplit;
        Hashtable<String, AppUser> appusers = new Hashtable<String, AppUser>();
        AppUser appuser;
        for (String data : groupData) {
            vsplit = data.split(":::");
            id = vsplit[0];
            field = vsplit[1];
            value = vsplit[2];
            value = value.trim();
            if (appusers.containsKey(id)) {
                appuser = appusers.get(id);
            } else {
                appuser = new AppUser();
            }

            if (field.toLowerCase().equals("getid")) {
                appuser.setId(new Integer(value));
//                group.setGroupAlphaId(value);
            } else if (field.toLowerCase().equals("getusername")) {
                appuser.setUsername(value);
            } else if (field.toLowerCase().equals("getemail")) {
                appuser.setEmail(value);
            } else if (field.toLowerCase().equals("getdesknumber")) {
                appuser.setDeskNumber(value);
            } else if (field.toLowerCase().equals("getdisplayname")) {
                appuser.setDisplayName(value);
//                group.setGroupName(value);
            }
            appusers.put(id, appuser);
            //group.setGroupAlphaId(data);
        }
        return appusers.values().toArray(new AppUser[appusers.size()]);
    }

    public AppUser getUser(String username) {
        AppUser user = new AppUser();
        String[] groupData = getWebData(applicationURL + "/service/getUser?username=" + username);
        String field;
        String value;
        String[] vsplit;
        for (String data : groupData) {
            vsplit = data.split(":::");
            field = vsplit[0];
            value = vsplit[1];
            value = value.trim();
            if (value!=null&&value.equals("null")) {
                value = null;
            }
            if (field.toLowerCase().equals("getemail")) {
                user.setEmail(value);
            } else if (field.toLowerCase().equals("getactive")) {
                user.setActive(new Boolean(value));
            } else if (field.toLowerCase().equals("getcellphone")) {
                user.setCellPhone(value);
            } else if (field.toLowerCase().equals("getemail")) {
                user.setEmail(value);
            } else if (field.toLowerCase().equals("getdatecreated")) {
//                user.setDateCreated(value);
            } else if (field.toLowerCase().equals("getdesknumber")) {
                user.setDeskNumber(value);
            } else if (field.toLowerCase().equals("getdisplayname")) {
                user.setDisplayName(value);
            } else if (field.toLowerCase().equals("getemailphone")) {
                user.setEmailPhone(value);
            } else if (field.toLowerCase().equals("getfirstname")) {
                user.setFirstName(value);
            } else if (field.toLowerCase().equals("getid")) {
                user.setId(new Integer(value));
            } else if (field.toLowerCase().equals("getlandphone")) {
                user.setLandPhone(value);
            } else if (field.toLowerCase().equals("getlastname")) {
                user.setLastName(value);
            } else if (field.toLowerCase().equals("getmiddlename")) {
                user.setMiddleName(value);
            } else if (field.toLowerCase().equals("getofficeaddress")) {
                user.setOfficeAddress(value);
            } else if (field.toLowerCase().equals("getpagerphone")) {
                user.setPagerPhone(value);
            } else if (field.toLowerCase().equals("getpending")) {
                user.setPending(new Boolean(value));
            } else if (field.toLowerCase().equals("getregion")) {
                user.setRegion(value);
            } else if (field.toLowerCase().equals("getbusinessdesc")) {
                user.setBusinessDesc(value);
            } else if (field.toLowerCase().equals("getuserdesc")) {
                user.setUserDesc(value);
            } else if (field.toLowerCase().equals("getusername")) {
                user.setUsername(value);
            } else if (field.toLowerCase().equals("getwebsite")) {
                user.setWebsite(value);
            } else if (field.toLowerCase().equals("getofficecity")) {
                user.setOfficeCity(value);
            } else if (field.toLowerCase().equals("getofficestate")) {
                user.setOfficeState(value);
            } else if (field.toLowerCase().equals("getofficezip")) {
                user.setOfficeZip(value);
            } else if (field.toLowerCase().equals("getcompanyname")) {
                user.setCompanyName(value);
            } else if (field.toLowerCase().equals("getimage")) {
                user.setImage(value);
            } else if (field.toLowerCase().equals("getfaxphone")) {
                user.setFaxPhone(value);
            } else if (field.toLowerCase().equals("gettype")) {
                user.setType(value);
            } else if (field.toLowerCase().equals("getbusinesstype")) {
                user.setBusinessType(value);
            } else if (field.toLowerCase().equals("gettwitter")) {
                user.setTwitter(value);
            } else if (field.toLowerCase().equals("getfacebook")) {
                user.setFacebook(value);
            } else if (field.toLowerCase().equals("getyoutube")) {
                user.setYoutube(value);
            } else if (field.toLowerCase().equals("getlinkedin")) {
                user.setLinkedin(value);
            } else if (field.toLowerCase().equals("ispending")) {
                user.setPending(new Boolean(value));
            }
            //group.setGroupAlphaId(data);
        }
        if (user.getUsername()==null||user.getUsername().equals("null")||user.getUsername().equals("")) {
            user = null;
        }
        return user;
    }

    public String getRealm() {
        return realm;
    }

    public void setRealm(String realm) {
        this.realm = realm;
    }


}
