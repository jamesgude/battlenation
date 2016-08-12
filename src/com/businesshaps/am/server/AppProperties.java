/**
 * <p>Title: AppProperties</p>
 * <p>Description:</p>
 * @author bgude
 * @version 1.0
 */

package com.businesshaps.am.server;

import java.io.File;

public class AppProperties {
    private String databaseDriver;
    private String jdbc;
    private File homeDirectory;
    private String applicationURL;
    private boolean syncActiveDirectory = false;
    private String ldapServer;
    private String realm;
    private boolean server;
    private String domain;


    public AppProperties() {

    }

    public String getDatabaseDriver() {
        return databaseDriver;
    }

    public void setDatabaseDriver(String databaseDriver) {
        this.databaseDriver = databaseDriver;
    }

    public String getJdbc() {
        return jdbc;
    }

    public void setJdbc(String jdbc) {
        this.jdbc = jdbc;
    }

    public File getHomeDirectory() {
        return homeDirectory;
    }

    public void setHomeDirectory(File homeDirectory) {
        this.homeDirectory = homeDirectory;
    }

    public String getApplicationURL() {
        return applicationURL;
    }

    public void setApplicationURL(String applicationURL) {
        this.applicationURL = applicationURL;
    }

    public boolean isSyncActiveDirectory() {
        return syncActiveDirectory;
    }

    public void setSyncActiveDirectory(boolean syncActiveDirectory) {
        this.syncActiveDirectory = syncActiveDirectory;
    }

    public String getLdapServer() {
        return ldapServer;
    }

    public void setLdapServer(String ldapServer) {
        this.ldapServer = ldapServer;
    }


    public String getRealm() {
        return realm;
    }

    public void setRealm(String realm) {
        this.realm = realm;
    }

    public String getRoot() {
        String rtn = null;
        if (realm.equals("ROOT")) {
            rtn = "";
        } else {
            rtn = "/" + realm + "/";
        }
        if (rtn.endsWith("/")) {
            rtn = rtn.substring(0, rtn.length()-1);
        }
        return rtn;
    }

    public boolean isServer() {
        return server;
    }

    public void setServer(boolean server) {
        this.server = server;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }
}
