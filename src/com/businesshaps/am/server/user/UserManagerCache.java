/**
 * <p>Title: UserManagerCache</p>
 * <p>Description:</p>
 * @author bgude
 * @version 1.0
 */

package com.businesshaps.am.server.user;

import com.businesshaps.am.businessobjects.AppUser;

import java.util.*;

public class UserManagerCache implements UserManager {
    private Hashtable<Integer, AppUser> users;
    private Hashtable<String, AppUser> userByUsernames;
    private Hashtable<String, AppUser> userByEmails;
    private Hashtable<String, ArrayList<AppUser>> userBySupervisorUsernames;

    public UserManagerCache() {
        this.reload();
    }

    public AppUser[] getUser() {
        AppUser[] rtns = users.values().toArray(new AppUser[users.values().size()]);
        Arrays.sort(rtns, new Comparator<AppUser>() {
            public int compare(AppUser o1, AppUser o2) {
                return o1.getDisplayName().compareTo(o2.getDisplayName());
            }
        });


        return rtns;
    }

    public AppUser setUser(AppUser user) {
        if (users.containsKey(user.getId())) {
            users.remove(user.getId());
        }
        if (userByUsernames.containsKey(user.getUsername())) {
            userByUsernames.remove(user.getUsername());
        }
        if (userByEmails.containsKey(user.getEmail())) {
            userByEmails.remove(user.getEmail());
        }

        users.put(user.getId(), user);
        if (user.getUsername() != null) {
            userByUsernames.put(user.getUsername(), user);
        }
        if (user.getEmail() != null) {
            userByEmails.put(user.getEmail(), user);
        }
        if (user.getSupervisorUsername() != null) {
            ArrayList<AppUser> superlists;
            if (userBySupervisorUsernames.containsKey(user.getSupervisorUsername())) {
                superlists = userBySupervisorUsernames.get(user.getSupervisorUsername());
            } else {
                superlists = new ArrayList<AppUser>();
            }
            for (AppUser u : superlists) {
                if (u.getId()==user.getId()) {
                    superlists.remove(u);
                    break;
                }
            }

            superlists.add(user);

            userBySupervisorUsernames.put(user.getSupervisorUsername(), superlists);
        }
        return user;
    }

    public AppUser getUserByUserId(Integer userId) {
        return users.get(new Integer(userId));
    }

    public AppUser getUserByUsername(String username) {
        return userByUsernames.get(username);
    }

    public AppUser getUserByEmail(String email) {
        return userByEmails.get(email);
    }

    public void reload() {
        users = new Hashtable<Integer, AppUser>();
        userByUsernames = new Hashtable<String, AppUser>();
        userByEmails = new Hashtable<String, AppUser>();
        userBySupervisorUsernames = new Hashtable<String, ArrayList<AppUser>>();
    }

    public AppUser[] getUserBySupervisorUsername(String username) {
        ArrayList<AppUser> superlists;
        if (userBySupervisorUsernames.containsKey(username)) {
            superlists = userBySupervisorUsernames.get(username);
        } else {
            superlists = new ArrayList<AppUser>();
        }
        return superlists.toArray(new AppUser[superlists.size()]);
    }


    public AppUser[] getUserByFilter(String departments, String groupAlphaIds, String jobTitles, String softwareAccessGroupAlphaIds, String supervisorUsernames, String textSearch, boolean beingProvisioned) {
        ArrayList<AppUser> searches = new ArrayList<AppUser>();
        boolean exclude;
        boolean textInclude;

        for (AppUser user : users.values()) {
            exclude = false;
            if (departments != null && !departments.equals("")) {
                if (departments.indexOf(user.getDepartment() != null ? user.getDepartment() : "null") == -1) {
                    exclude = true;
                }
            }

            if (!exclude&&jobTitles != null && !jobTitles.equals("")) {
                if (jobTitles.indexOf(user.getJobTitle() != null ? user.getJobTitle() : "null") == -1) {
                    exclude = true;
                }
            }

            if (!exclude &&supervisorUsernames != null && !supervisorUsernames.equals("")) {
                if (supervisorUsernames.indexOf(user.getSupervisorUsername() != null ? user.getSupervisorUsername() : "null") == -1) {
                    exclude = true;
                }
            }

            if (!exclude &&textSearch!= null && !textSearch.equals("")) {
                textInclude = false;
                if (!textInclude && new String(user.getDisplayName() != null ? user.getDisplayName().toLowerCase() : "").indexOf(textSearch.toLowerCase())!=-1) {
                    textInclude = true;
                }

                if (!textInclude && new String(user.getUsername() != null ? user.getUsername().toLowerCase() : "").indexOf(textSearch.toLowerCase()) != -1) {
                    textInclude = true;
                }
                if (!textInclude && new String(user.getEmail() != null ? user.getEmail().toLowerCase() : "").indexOf(textSearch.toLowerCase()) != -1) {
                    textInclude = true;
                }
                if (!textInclude && new String(user.getLandPhone() != null ? user.getLandPhone().toLowerCase() : "").indexOf(textSearch.toLowerCase()) != -1) {
                    textInclude = true;
                }
                if (!textInclude && new String(user.getCellPhone() != null ? user.getCellPhone().toLowerCase() : "").indexOf(textSearch.toLowerCase()) != -1) {
                    textInclude = true;
                }

                exclude = (!textInclude);

            }

            if (groupAlphaIds != null && !groupAlphaIds.equals("") && !exclude) {
            }

            if (!exclude) {
                searches.add(user);
            }
        }

        AppUser[] rtns = searches.toArray(new AppUser[searches.size()]);
        Arrays.sort(rtns, new Comparator<AppUser>() {
            public int compare(AppUser o1, AppUser o2) {
                return o1.getDisplayName().compareTo(o2.getDisplayName());
            }
        });

        return rtns;
    }

    public void delete(AppUser user) {
        users.remove(user.getId());

    }
}
