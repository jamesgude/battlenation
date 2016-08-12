/**
 * <p>Title: WebApplication</p>
 * <p>Description:</p>
 * @author Bradley Gude
 * @version 1.0
 */

package com.businesshaps.am;

import com.businesshaps.am.businessobjects.AppGroup;
import com.businesshaps.am.client.AccessManagerServerController;
import com.businesshaps.am.client.WebApplicationFilter;
import com.businesshaps.am.server.AppServer;
import com.businesshaps.am.tools.Emailer;
import com.businesshaps.am.tools.Includer;

import com.businesshaps.params.PropWatcher;

import javax.servlet.*;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Properties;

public class WebApplication extends WebApplicationFilter {
    private String orig_jdbc;
    protected FilterConfig filterConfig = null;
    public static String sep = System.getProperties().getProperty("file.separator");

    public WebApplication() {

    }

    public void init(FilterConfig config) throws ServletException {
        SessionHandler.filterConfig = config;
        super.init(config);
        Emailer.server = PropWatcher.getInstance().getData().getEmail().getServer();
        Emailer.auth = new Boolean(PropWatcher.getInstance().getData().getEmail().isAuth()).toString();
        Emailer.user = PropWatcher.getInstance().getData().getEmail().getUsername();
        Emailer.pass = PropWatcher.getInstance().getData().getEmail().getPassword();
        Emailer.port = PropWatcher.getInstance().getData().getEmail().getPort();
//        System.out.println("domain: " + props.getProperty("domain"));
        try {

            this.filterConfig = config;
            SessionHandler.companyTitle = "";
            String jdbc = PropWatcher.getInstance().getData().getJdbc();
            jdbc = jdbc.replaceAll("#realm#", AccessManagerServerController.getInstance().getRealm());
            orig_jdbc = new String(jdbc.toString());
            jdbc = orig_jdbc.replaceAll("#org#", "db");
            SessionHandler.setJdbc(jdbc);
            SessionHandler.setOrigJdbc(orig_jdbc); 
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
 
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws ServletException, IOException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;

        String servletPath = req.getServletPath();
        servletPath = servletPath.replaceAll("index.jsp", "");
        SimpleDateFormat format = new SimpleDateFormat("E MM/dd/yyyy hh:mm:ss aaa ");

        String url = req.getRequestURL() + "?" + req.getQueryString();
        url = url.replaceAll("\\?null", "");
        if (url.indexOf("www.") > -1) {
            url = url.replace("http://www.", "http://");
            url = url.replace("https://www.", "https://");
            resp.sendRedirect(url);
            return;
        }

        if (servletPath.endsWith("/process_signin/")) {
        	  String user_token = null;
        	  if (req.getCookies() != null) {
        	      for (Cookie cookie : req.getCookies()) {
//        	                                System.out.println("cookie: " + cookie.getName() + " = " + cookie.getValue());
        	          if (cookie.getName().contains("user_token")) {
        	              user_token = cookie.getValue();
        	              if (user_token!=null&&!user_token.equals("")) {
        	            System.out.println("user_token: " + user_token);
        	            break;
        	          }
        	          }
        	      }
        	  }
        	  String r = request.getParameter("url");
        	  if (r.contains("?")) {
        	    r += "&t=true&done=true&user_token=" + user_token + "&osu=" + r;
        	  } else {
        	    r += "?t=true&done=true&user_token=" + user_token + "&osu=" + r;
        	  }
        	  resp.sendRedirect(r);
        	  
            return;
        }


        //TODO: Ignore list
        String[] ignore_list = PropWatcher.getInstance().getData().getBypassDirectories().split(",");
        for (String s : ignore_list) {
            if (servletPath.indexOf(s) > -1) {
                try {
                    chain.doFilter(request, response);
                } catch (Exception error) {
                    error.printStackTrace();
                }
                return;

            }
        }
        
        request.setAttribute("jdbc", PropWatcher.getInstance().getData().getJdbc());
        request.setAttribute("server-mode", PropWatcher.getInstance().getData().isServerMode());
        request.setAttribute("domain", PropWatcher.getInstance().getData().getDomain());
        request.setAttribute("ignore-list", PropWatcher.getInstance().getData().getBypassDirectories());
        request.setAttribute("no-log-list", PropWatcher.getInstance().getData().getBypassDirectories());

        if (!PropWatcher.getInstance().getData().getDomain().equals(request.getServerName())) {
            if (request.getParameter("t") != null) {
                req.getSession().setAttribute("t", new Boolean(true));
                super.doFilter(req, resp, chain);
                return;
            } else {

                if ((Boolean) req.getSession().getAttribute("t") == null && req.getCookies() != null && !servletPath.contains("/signup/") && !servletPath.contains("/home/signin/")) {
                    req.getSession().setAttribute("t", new Boolean(true));
                    resp.sendRedirect(SessionHandler.getBaseURL(req) + "/process_signin/?url=" + req.getRequestURL() + (req.getQueryString() != null && req.getQueryString().length() > 0 ? "?" + req.getQueryString() : ""));
                    return;
                }
            } 
        }
 
        String org = null;
        org = SessionHandler.getOrgRoot(req);

        String jdbc;
        jdbc = orig_jdbc.replaceAll("#org#", "db");

        AppGroup group = AccessManagerServerController.getInstance().getGroupByURL(req.getServerName() + "/" + org + "/");
        AppGroup appGroup = AccessManagerServerController.getInstance().getGroupByURL(req.getRequestURL().toString());
        if (group != null) {
        }
        String[] includes = new String[0];
        if (group != null && group.getGroupTypeAlphaId() != null && group.getGroupTypeAlphaId().equals("GRP_TYPE_USER")) {

            jdbc = orig_jdbc.replaceAll("#org#", org);

            if (servletPath.endsWith("/")) {
                    servletPath += "/index.page.jsp";
            }

            String incpath = servletPath.replaceAll("/" + org + "/", "/approot/");
            boolean foundinc = false;
            if (Includer.WebFileExists(servletPath, req, true)) {
                incpath = servletPath.replaceAll("/" + org + "/", "/approot/");
                foundinc = true;
            } else if (Includer.WebFileExists(servletPath.replaceAll("page", "splash"), req, true)) {
                incpath = servletPath.replaceAll("/" + org + "/", "/approot/").replaceAll("page", "splash");
                servletPath = servletPath.replaceAll("page", "splash");
                foundinc = true;
            } else {
                foundinc = true;
                incpath = "/404.page.jsp?noredirect=true";
                //incpath = "/404.page.jsp";
            }
            if (foundinc) {
                includes = new String[1];
                includes[0] = incpath;
            }
        }

        try {
            super.doFilter(request, response, chain, includes);
        } catch (Exception error) {
            error.printStackTrace();
            System.out.println("404: " + req.getRequestURL());
        }
        jdbc = orig_jdbc.replaceAll("#org#", "db");

    }

    public void destroy() {
    }


    public FilterConfig getFilterConfig() {
        return this.filterConfig;
    }

}