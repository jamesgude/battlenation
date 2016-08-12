/**
 * <p>Title: AuthManager</p>
 * <p>Description:</p>
 * @author bgude
 * @version 1.0
 */

package com.businesshaps.am.server.auth;

import com.businesshaps.am.businessobjects.AppUser;
import com.businesshaps.am.server.user.UserManager;
import com.businesshaps.am.tools.GEncrypt;

import java.io.File;
import java.util.Date;
import java.util.Hashtable;

public class AuthManager {
    private Hashtable<String, AppUser> user_tokens = new Hashtable<String, AppUser>();
    //private Hashtable<String, AppUser> users = new Hashtable<String, AppUser>();
    private UserManager userManager;
    private File homedir;
    public AuthManager(UserManager userManager, File homedir) {
//        System.out.println("###   Access Manager: Initiated AuthManager");
//        this.load_users();
        this.homedir = new File(homedir.getAbsolutePath() + "\\tokens");
        this.userManager = userManager;
        if (!this.homedir.exists()) {
            this.homedir.mkdirs();
        }
        String username;
        AppUser user;
        for (File token_dir : this.homedir.listFiles()) {
            username = new File(token_dir.getAbsolutePath() + "\\user\\").listFiles()[0].getName();
            user = userManager.getUserByUsername(username);
            if (token_dir!=null&&user!=null) {
                user_tokens.put(token_dir.getName(), user);
            }
        }
    }

    public AppUser getUserByUserToken(String user_token) {
        AppUser user = null;
        if (user_tokens.get(user_token)!=null) {
            user = userManager.getUserByUserId(user_tokens.get(user_token).getId());
        } else {
            user = user_tokens.get(user_token);
        }
        return user;
    }

    private String create_user_token(String username) {
        long date = new Date().getTime();
        String user_token;
        user_token = date + "_" + username.replaceAll("@", "x").replaceAll("\\.", "x");
        //System.out.println("token:" + user_token);
        user_tokens.put(user_token, userManager.getUserByUsername(username));
        File token_dir = new File(homedir.getAbsolutePath() + "\\" + user_token + "\\user\\" + username);
        if (!token_dir.exists()) {
            token_dir.mkdirs();
        }

//        System.out.println("AuthManager.create_user_token(" + user_token + ");");
        return user_token;
    }

    public String authenticateUser(String username, String password) {
        String spassword = GEncrypt.encrypt(password);
        //System.out.println("AuthManager.authenticateUser(" + spassword + ");");
        username = username.toLowerCase();
        String user_token = null;
        AppUser user = userManager.getUserByUsername(username);
        if (user==null) {
//            user = userManager.getUserByEmail(username);
        } else {
        }
        if (user != null) {
            if (user.getPassword()!=null&& user.getPassword().equals(password)) {
                user_token = create_user_token(username);
            }
        } else {
            user_token = null;
        }
        return user_token;
    }

    public String authenticateUser(String username) {
//        System.out.println("AuthManager.authenticateUser(" + username + ");");
        username = username.toLowerCase();
        String user_token = null;
        AppUser user = userManager.getUserByUsername(username);
        if (user!=null) {
            user_token = create_user_token(username);
        } else {
            user_token = null;
        }
        return user_token;
    }

    public boolean checkUserToken(String user_token) {
//        System.out.println("AuthManager.checkUserToken(" + user_token + ");");
        boolean hasToken = false;
        AppUser user;
        if (user_tokens.containsKey(user_token)) {
            user = userManager.getUserByUserId(user_tokens.get(user_token).getId());
            if (user.isActive()) {
                hasToken = true;
            }
        }
        return hasToken;
    }

    public void logout(String user_token) {
//        System.out.println("AuthManager.logout(" + user_token + ");");
        remove_user_token(user_token);
    }

    private void remove_user_token(String user_token) {
        File token_dir = new File(homedir.getAbsolutePath() + "\\" + user_token);
        if (token_dir.exists()) {
            deleteDirectory(token_dir);
        }
        if (user_tokens.containsKey(user_token)) {
            user_tokens.remove(user_token);
        }
    }

    static public boolean deleteDirectory(File path) {
        if (path.exists()) {
            File[] files = path.listFiles();
            for (int i = 0; i < files.length; i++) {
                if (files[i].isDirectory()) {
                    deleteDirectory(files[i]);
                } else {
                    files[i].delete();
                }
            }
        }
        return (path.delete());
    }

    public Hashtable<String, AppUser> getUser_tokens() {
        return user_tokens;
    }
}
