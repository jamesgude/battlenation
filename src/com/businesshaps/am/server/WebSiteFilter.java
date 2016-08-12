/**
 * <p>Title: WebSiteFilter</p>
 * <p>Description:</p>
 * @author bgude
 * @version 1.0
 */

package com.businesshaps.am.server;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;

public class WebSiteFilter implements Filter {
    public WebSiteFilter() {
        System.out.println("###   Access Manager: Web Site Filter");

    }

    protected FilterConfig filterConfig = null;
    private String homedirectory = null;
    private File homedir;


    public void init(FilterConfig config) throws ServletException {
        this.filterConfig = config;
    }
    
    public void destroy() {
    }

    public void doFilter(ServletRequest request, ServletResponse response,
                         FilterChain chain) throws ServletException, IOException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;

        String servletPath = req.getServletPath();
        if (servletPath.split("/").length==2 && servletPath.indexOf("navigation.jsp") == -1 && servletPath.indexOf("logout.jsp") == -1) {
//            System.out.println(servletPath.split("/").length);
            resp.sendRedirect(req.getContextPath() + "/secure/website/?site=" + servletPath.split("/")[1]);
        } else {
            try {
                chain.doFilter(request, response);
            } catch (Exception error) {
                error.printStackTrace();
            }
        }
    }

    public FilterConfig getFilterConfig() {
        return this.filterConfig;
    }

}
