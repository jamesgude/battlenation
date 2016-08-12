/**
 * <p>Title: LogoutManager</p>
 * <p>Description:</p>
 * @author Bradley Gude
 * @version 1.0
 */

package com.businesshaps.am.client;

import com.businesshaps.am.server.AppServer;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class LogoutManager {
    public LogoutManager(HttpServletRequest request, HttpServletResponse response, PrintWriter out) throws IOException {

        request.getSession().setAttribute("username", null);
        Cookie cookie2 = new Cookie("user_token", null);
        cookie2.setPath("/");
        cookie2.setValue("");
        cookie2.setMaxAge(0);
        response.addCookie(cookie2);
        cookie2 = new Cookie("JSESSIONID", null);
        cookie2.setPath("/");
        cookie2.setMaxAge(0);
        response.addCookie(cookie2);

        //System.out.println(applicationURL + "/logout.jsp?su=" + su.replace("/logout", ""));
        AppServer server = AppServer.getInstance();
        server.getUserManager().reload();
        String user_token = (String) request.getSession().getAttribute("user_token");
        if (user_token != null) {
            server.getAuthManager().logout(user_token);
        }
        user_token = (String) request.getParameter("user_token");
        if (user_token != null) {
            server.getAuthManager().logout(user_token);
        }
        request.getSession().setAttribute("user_token", "");
        try {
            if (request.getCookies()!=null) {
                for (Cookie cookie : request.getCookies()) {
    //                System.out.println("DELETEING COOKING = " + cookie.getValue());
                    user_token = cookie.getValue();
                    if (cookie.getName().contains("user_token")) {
                        user_token = (user_token.equals("") || user_token.equals("null") ? null : user_token);
                        if (user_token != null) {
                            server.getAuthManager().logout(user_token);
                        }
                        cookie2 = new Cookie(cookie.getName(), "");
                        cookie2.setPath("/");
                        cookie2.setValue("");
                        cookie2.setMaxAge(-1);
                        response.addCookie(cookie2);
                    }
                }
            }
        } catch (Exception error) {
            error.printStackTrace();
        }

        Cookie cookie = new Cookie(AppServer.getInstance().getAppProperties().getApplicationURL() + "application_server_user_token", "");
        cookie.setMaxAge(-1);
        cookie.setPath("/");
        response.addCookie(cookie);
        String su = request.getParameter("su");

        if (su != null) {
            response.sendRedirect(su + "");
            return;
        } else {
            response.sendRedirect("/");
        }

    }
}
