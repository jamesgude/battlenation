/**
 * <p>Title: WebApplicationFilter</p>
 * <p>Description:</p>
 * @author Bradley Gude
 * @version 1.0
 */

package com.businesshaps.am.client;


import com.businesshaps.am.SessionHandler;
import com.businesshaps.am.businessobjects.AppGroup;
import com.businesshaps.am.server.AppProperties;
import com.businesshaps.am.server.AppServer;
import com.businesshaps.am.tools.PageProxy;
import com.businesshaps.oi.ObInject;
import com.businesshaps.params.PropWatcher;

import javax.servlet.*;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.Date;

public class WebApplicationFilter implements Filter {
    protected FilterConfig filterConfig = null;
    private String applicationURL = "";
    private AccessManagerInstanceController accessManager;
    private String realm = "";
    public String rootdirectory;
    public String sep;
    private boolean server = false;
    private String homedirectory = null;
    private File homedir;
    private boolean syncLdap = false;

    public WebApplicationFilter() {
    	
    }

    public void doFilter(ServletRequest request, ServletResponse response,
                         FilterChain chain, String[] includes) throws ServletException, IOException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;

        String username = "anon";

        String sp = req.getServletPath();
        String servletPath = req.getServletPath();
        servletPath = servletPath.replaceAll("index.jsp", "");
        servletPath = servletPath.replaceAll("index.page.jsp", "");
        sp = sp.replaceAll("index.jsp", "");
        sp = sp.replaceAll("index.page.jsp", "");
        sp = sp.replaceAll("/", "\\\\");
        String url = req.getRequestURL().toString();
        if (PropWatcher.getInstance().getData().isHttpsOnly()&&url.startsWith("http:")) {

        	String httpsUrl = PropWatcher.getInstance().getData().getHttpsServer() + servletPath.replaceAll("//", "/") + (req.getQueryString()!=null?"?"+req.getQueryString():"");
        	//System.out.println("wtf: "+ httpsUrl);
        	resp.sendRedirect(httpsUrl);
        	return;
        }
        
        
        String applicationURL = req.getParameter("url");
        if (servletPath.startsWith("/auth/") ) {
            AuthForm authForm = new AuthForm(req, resp, resp.getWriter());
        } else if (req.getRequestURL().toString().endsWith("logout")) {
            try {
                //chain.doFilter(request, response);
                LogoutManager logoutManager = new LogoutManager(req, resp, resp.getWriter());
            } catch (Exception error) {
                error.printStackTrace();
            }
            return;
        }

        if (server) {
            if (applicationURL != null && !applicationURL.equals("")) {
            } else {
                applicationURL = this.applicationURL;
            }
            accessManager.setApplicationURL(applicationURL);
        }
        applicationURL = this.applicationURL;

        String su = "";
        if (req.getQueryString() != null) {
        	su = req.getRequestURL().toString() + "?" + req.getQueryString();
        } else {
        	su = req.getRequestURL().toString();
        }

        AppGroup appGroup = accessManager.getGroupByURL(req.getRequestURL().toString());
        boolean allow_anon = false;
        boolean passedAuth = false;
        String user_token = null;
        boolean active;


        boolean hasAccess;
        RequestDispatcher dispatcher;

        if (appGroup != null) {
            allow_anon = appGroup.isAllowAnonAccess();
        }

        active = false;
        boolean done = (req.getParameter("done") != null && Boolean.parseBoolean(req.getParameter("done")));
        if (allow_anon) {
            hasAccess = true;
            if (done) {
                user_token = req.getParameter("user_token");
                if (user_token.equals("")) {
                    user_token = "anon";
                }

                String osu = request.getParameter("osu");
                if (req.getCookies() != null) {
                    Cookie cookie = new Cookie("user_token", user_token);
                    cookie.setMaxAge(365 * 24 * 60 * 60);
                    cookie.setPath("/");
                    resp.addCookie(cookie);
                } else {
                    osu = osu + (osu.indexOf("?") > -1 ? "&" : "?") + "ut=" + user_token;
                    req.getSession().setAttribute("user_token", user_token);
                }

                req.getSession().setAttribute("user_token", user_token);
                username = accessManager.getUsernameByToken(user_token);
                req.getSession().setAttribute("username", username);
                req.getSession().setAttribute("user_token", user_token);
                try {
                    if (!user_token.equals("anon")) {
                        if (osu.endsWith("\\?nl=true")) {
                            osu = osu.replaceAll("\\?nl=true", "");
                        } else if (osu.endsWith("\\&nl=true")) {
                            osu = osu.replaceAll("\\&nl=true", "");
                        } else {
                            osu = osu.replaceAll("nl=true", "");
                        }
                    }

                    if (user_token.equals("anon")) {
                        resp.sendRedirect(osu);
                    } else {
                        resp.sendRedirect(osu + (osu.contains("?") ? "" : "") + "");
                    }
                    return;
                } catch (Exception error) {
                    error.printStackTrace();
                    System.out.println("osu: " + request.getParameter("osu"));
                }
                return;
            } else {

                if (user_token == null || user_token.equals("a") || user_token.equals("null") || user_token.equals("") || user_token.equals("anon")) {
                    try {
                        if (req.getCookies() != null) {
                            for (Cookie cookie : req.getCookies()) {
                                if (cookie.getName().contains("user_token")) {
                                    user_token = cookie.getValue();
                                    user_token = (user_token.equals("") || user_token.equals("null") ? null : user_token);
                                    if (user_token!=null) {
                                        active = accessManager.isUserTokenActive(user_token);
                                    } else {
                                        active = false;
                                    }
                                    if (active) {
                                        break;
                                    } else {
                                        user_token = null;
                                    }
                                }
                            }
                        }
                    } catch (Exception error) {
                        error.printStackTrace();
                    }
                }
                if ((user_token != null && user_token.equals(""))) {
                    user_token = null;
                }
                if (user_token != null && (user_token.equals("null"))) {
                    user_token = null;
                }
                if (user_token != null) {
                    active = accessManager.isUserTokenActive(user_token);
                }
                if (!active) {
                    user_token = "anon";
                    req.getSession().setAttribute("user_token", "anon");
                    req.getSession().setAttribute("username", "anon");
                }

                if (user_token != null) {
                    if (!user_token.equals("anon")) {
                        username = accessManager.getUsernameByToken(user_token);
                        req.getSession().setAttribute("username", username);
                        req.getSession().setAttribute("user_token", user_token);
                    } else {
                        user_token = "anon";
                        req.getSession().setAttribute("user_token", "anon");
                        req.getSession().setAttribute("username", "anon");
                    }
                }
            }

            if (appGroup.isRestrictLayout()) {
                chain.doFilter(request, response);
                return;
            }
        } else {
            user_token = req.getParameter("user_token");
            if (user_token != null) {
                String osu = request.getParameter("osu");
                if (req.getCookies() != null) {
                    Cookie cookie = new Cookie("user_token", user_token);
                    cookie.setPath("/");
                    cookie.setMaxAge(365 * 24 * 60 * 60);
                    resp.addCookie(cookie);
                } else {
                    osu = osu + (osu.indexOf("?") > -1 ? "&" : "?") + "ut=" + user_token;
                    req.getSession().setAttribute("user_token", user_token);
                }

                req.getSession().setAttribute("user_token", user_token);
                if (!user_token.equals("anon")) {
                    username = accessManager.getUsernameByToken(user_token);
                    req.getSession().setAttribute("username", username);
                } else {
                    req.getSession().setAttribute("username", "anon");
                }
                try {
                    if (!user_token.equals("anon")) {
                        if (osu.endsWith("\\?nl=true")) {
                            osu = osu.replaceAll("\\?nl=true", "");
                        } else if (osu.endsWith("\\&nl=true")) {
                            osu = osu.replaceAll("\\&nl=true", "");
                        } else {
                            osu = osu.replaceAll("nl=true", "");
                        }
                    }

                    resp.sendRedirect(osu);
                    return;
                } catch (Exception error) {
                    error.printStackTrace();
                    System.out.println("osu: " + request.getParameter("osu"));
                }
                return;
            } else {
                user_token = null;
                user_token = (String) req.getSession().getAttribute("user_token");
                if (user_token == null || user_token.equals("")) {
                    try {
                        if (req.getCookies() != null) {
                            for (Cookie cookie : req.getCookies()) {
                                if (cookie.getName().contains("user_token")) {
                                    user_token = cookie.getValue();
                                    user_token = (user_token.equals("") || user_token.equals("null") ? null : user_token);
                                    active = accessManager.isUserTokenActive(user_token);
                                    if (active) {
                                        break;
                                    } else {
                                        user_token = null;
                                    }
                                }
                            }
                        } else {
                            user_token = (String) req.getParameter("ut");
                            if (user_token != null) {
                                user_token = (user_token.equals("") || user_token.equals("null") ? null : user_token);
                            } else {

                            }
                            if (user_token != null) {
                                active = accessManager.isUserTokenActive(user_token);
                            }
                            if (active) {
                            } else {
                                user_token = null;
                            }
                        }
                    } catch (Exception error) {
                        error.printStackTrace();
                    }
                } else {
                    active = accessManager.isUserTokenActive(user_token);
                }

                if (user_token != null) {
                    passedAuth = true;

                    if (!active) {
                        passedAuth = false;
                        user_token = null;
                    } else {
                        req.getSession().setAttribute("user_token", user_token);
                        if (!user_token.equals("anon")) {
                            username = accessManager.getUsernameByToken(user_token);
                            req.getSession().setAttribute("username", username);
                        } else {
                            req.getSession().setAttribute("username", "anon");
                        }
                    }

                } else {
                    passedAuth = false;
                }
            }

            if (!passedAuth) {
                if (!servletPath.startsWith("/auth/")) {
                    try {
                        AuthForm authForm = new AuthForm(req, resp, resp.getWriter());
                        return;
                    } catch (Exception ed) {
                        ed.printStackTrace();
                    }
                    return;
                }
            }

            req.getSession().setAttribute("user_token", user_token);
            if (!user_token.equals("anon")) {
                username = accessManager.getUsernameByToken(user_token);
                req.getSession().setAttribute("username", username);
            } else {
                req.getSession().setAttribute("username", "anon");
            }


            /**
             * TODO: Validate user_token with server
             * http://server/check_token?user_token=user_token valid : invalid
             */

            hasAccess = accessManager.hasAuthorization(user_token, req.getRequestURL().toString());
        }

        if (!hasAccess) {
            new NoAccessManager(req, resp, resp.getWriter());
        } else {
            if (user_token != null) {
                if (!user_token.equals("anon")) {
                    username = accessManager.getUsernameByToken(user_token);
                    req.getSession().setAttribute("username", username);
                } else {
                    req.getSession().setAttribute("username", "anon");
                }
                req.getSession().setAttribute("user_token", user_token);
            }
            try {
                resp.setHeader("cache-control", "no-cache"); //HTTP 1.1
                resp.setHeader("pragma", "no-cache"); //HTTP 1.0
                resp.setDateHeader("expires", 0);
                if (includes != null && includes.length > 0) {
                    for (String inc : includes) {
                        try {
                            dispatcher = request.getRequestDispatcher(inc);
                            dispatcher.include(request, response);
                        } catch (Exception e) {
                            if (!inc.endsWith("/")) {
                                System.out.println("Trying with /");
                                resp.sendRedirect(inc + "/");
                                return;
                            } else {
                                System.out.println("Page Not Found: " + inc);
                            }
                        }
                    }
                } else {
                    try {
                        chain.doFilter(request, response);
                    } catch (Exception e404) {
                        e404.printStackTrace();
                    }
                    
                }
            } catch (Exception error) {
                error.printStackTrace();
                System.out.println(error + " + " + req.getRequestURL());
            }

        }
        
        if (req.getCookies() != null) {
            Cookie cookie = new Cookie("user_token", user_token);
            cookie.setMaxAge(365 * 24 * 60 * 60);
            cookie.setPath("/");
            resp.addCookie(cookie);
        }

        String[] no_log_list = PropWatcher.getInstance().getData().getBypassDirectories().split(",");
        boolean doIgnore = false;
        for (String s : no_log_list) {
            if (servletPath.indexOf(s.trim()) > -1) {
                doIgnore = true;
                break;
            }
        }
        if (!doIgnore) {
            String logURL = req.getRequestURL().toString();
            logURL = logURL.replaceAll(SessionHandler.getBaseURL(req), "");
            String browserType = (String) req.getHeader("User-Agent");
            accessManager.logUser(user_token, "", logURL, SessionHandler.getBaseURL(req), req.getRemoteAddr(), hasAccess, browserType, req.getQueryString());
        }
    }

    public void init(FilterConfig config) throws ServletException {
        String rootpath = config.getServletContext().getRealPath("");;
        System.out.println("###APP PATH: " + rootpath);
        sep = System.getProperties().getProperty("file.separator");
        realm = config.getServletContext().getAttribute("javax.servlet.context.tempdir").toString();

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

        homedirectory = rootpath+ "" + sep + "WEB-INF" + sep + "gudeam";
        rootdirectory = rootpath + "" + sep;
        PropWatcher.homePath = rootdirectory + "WEB-INF";
       	PropWatcher instance = PropWatcher.getInstance(); 

        homedir = new File(homedirectory);
        homedir.mkdirs();

        this.filterConfig = config;

        server = PropWatcher.getInstance().getData().isServerMode();
        String domain = PropWatcher.getInstance().getData().getDomain();


        String jdbc = PropWatcher.getInstance().getData().getJdbc();
        if (jdbc != null) {
            jdbc = jdbc.replaceAll("#realm#", realm);
            jdbc = jdbc.replaceAll("#org#", "db");
            ObInject obInject = ObInject.getInstance(jdbc);
        }

        File homeFile = new File(homedirectory + "\\init.txt");
        if (homeFile.exists() && homeFile.lastModified() < new Date().getTime() - 10000) {
            homeFile.delete();
        }

        if (!homeFile.exists()) {
            AppProperties appProperties = new AppProperties();
            appProperties.setHomeDirectory(homedir);
            appProperties.setSyncActiveDirectory(syncLdap);
            appProperties.setServer(server);
            appProperties.setLdapServer("");
            appProperties.setRealm(realm);
            appProperties.setDomain(domain);

                applicationURL = "/" + realm; //this.filterConfig.getInitParameter("application.url");
                appProperties.setApplicationURL("/" + realm);
                appProperties.setJdbc(jdbc);
                try {
                    applicationURL = applicationURL.replaceAll("#ip#", java.net.InetAddress.getLocalHost().getHostAddress());
                } catch (Exception error) {
                    error.printStackTrace();
                }
                AppServer.getInstance().setProperties(appProperties);
            

            try {
                homeFile.createNewFile();
            } catch (Exception error) {
                error.printStackTrace();
            }

        } else {
            homeFile.delete();
        }
        
        if (server) {
            accessManager = AccessManagerServerController.getInstance(realm);
        } else {
            accessManager = AccessManagerController.getInstance(realm);
        }
        accessManager.setRealm(realm);

        this.filterConfig = config;

        accessManager.setApplicationURL(applicationURL);
        if (PropWatcher.getInstance().getData().getInitializeScript()!=null&&!PropWatcher.getInstance().getData().getInitializeScript().equals("")) {
	        try {
	            this.runInitializer("http://" + domain + "" + PropWatcher.getInstance().getData().getInitializeScript());
	        } catch(Exception e) {
	            e.printStackTrace();
	        }
        }
        PropWatcher.getInstance().initialized();
    }

    private void runInitializer(final String url) {
        Thread thread = (new Thread() {
            public void run() {
                System.out.println("Calling Initializer Script: " + url);
                System.out.println(PageProxy.fetchPage(url));
            }
        });
        thread.start();
    }

    public void destroy() {
 
    }
    
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws ServletException, IOException {
    	this.doFilter(request, response, chain, null);
    }
    
    public FilterConfig getFilterConfig() {

        return this.filterConfig;
    }

}
