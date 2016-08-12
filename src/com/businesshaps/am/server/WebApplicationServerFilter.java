/**
 * <p>Title: WebApplicationServerFilter</p>
 * <p>Description:</p>
 * @author bgude
 * @version 1.0
 */

package com.businesshaps.am.server;

import com.businesshaps.oi.ObInject;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.Date;

public class WebApplicationServerFilter implements Filter {
    protected FilterConfig filterConfig = null;
    private String homedirectory = null;
    private File homedir;
    private boolean syncLdap = false;
    public WebApplicationServerFilter() {

    }

    public void init(FilterConfig config) throws ServletException {
        System.out.println("#############Server");

        String sep = System.getProperties().getProperty("file.separator");
        String realm = config.getServletContext().getAttribute("javax.servlet.context.tempdir").toString();
        try {
            String[] realms;
            if (sep.equals("\\")) {
                realms = realm.split(sep + sep);
            } else {
                realms = realm.split(sep);
            }
            realm = realms[realms.length - 1];
            realm = realm.toLowerCase().replaceAll(".war", "");
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (realm.equals("_")) {
            realm = "ROOT";
        }


        this.filterConfig = config;
        syncLdap = new Boolean(this.filterConfig.getInitParameter("sync.ldap"));
        String ldapServer = this.filterConfig.getInitParameter("ldap.server");

        String jdbc = this.filterConfig.getInitParameter("jdbc");
        jdbc = jdbc.replaceAll("#realm#", AppServer.getInstance().getAppProperties().getRealm());
        ObInject obInject = ObInject.getInstance(jdbc);
        //obInject.setJDBC(jdbc);

        homedirectory = System.getProperties().getProperty("catalina.base") + sep + "webapps" + sep + realm + "" + sep + "WEB-INF";// + sep + "config";

        homedir = new File(homedirectory);
        homedir.mkdirs();

        File homeFile = new File(homedirectory + "\\init.txt");
        if (homeFile.exists() && homeFile.lastModified() < new Date().getTime() - 10000) {
            homeFile.delete();
        }

        if (!homeFile.exists()) {

//        System.out.println(propFile.getAbsolutePath());
//            Properties props = new Properties();
            boolean loadedProps = false;
            try {
//                props.load(new FileInputStream(propFile));
                loadedProps = true;
            } catch (Exception error) {

            }

            boolean setupRequired = false;
            AppProperties appProperties = new AppProperties();
            appProperties.setHomeDirectory(homedir);
            appProperties.setSyncActiveDirectory(syncLdap);
            appProperties.setLdapServer(ldapServer);
            appProperties.setApplicationURL("/" + realm);
            appProperties.setRealm(realm);
            AppServer.getInstance().setProperties(appProperties);
            System.out.println("###   Access Manager: " + AppServer.getInstance().getAppProperties().getApplicationURL());

            if (loadedProps) {
//                jdbc = props.getProperty("jdbc");
//                dbdriver = props.getProperty("database.driver");
/*
                if (dbdriver == null || jdbc == null) {
                    setupRequired = true;
                } else {
                    appProperties.setDatabaseDriver(dbdriver);
                    appProperties.setJdbc(jdbc);
                    Connection conn;
                    try {
                        Class.forName(appProperties.getDatabaseDriver());
                        conn = DriverManager.getConnection(appProperties.getJdbc());
                        conn.close();
                        setupRequired = false;
                        AppServer.getInstance().setProperties(appProperties);

                    } catch (Exception error) {
                        System.out.println(error);
                        setupRequired = true;
                    }
                }
                */
            } else {
                setupRequired = true;
            }

            try {
                homeFile.createNewFile();
            } catch (Exception error) {
                error.printStackTrace();
            }

        } else {
            homeFile.delete();
        }

    }
    private boolean setupRequired = false;

    public void destroy() {
    }

    public void doFilter(ServletRequest request, ServletResponse response,
                         FilterChain chain) throws ServletException, IOException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;
        if (setupRequired) {
            //resp.getWriter().println("Setup required");
            req.getRequestDispatcher("/secure/settings/setup.page.jsp").include(request, response);
        } else {
//            AppServer.getInstance().init(); 
            try {
                chain.doFilter(request, response);
            } catch (Exception error) {
                error.printStackTrace();
            }
        }


    }

    public FilterConfig getFilterConfig(){
        return this.filterConfig;
    }

}
