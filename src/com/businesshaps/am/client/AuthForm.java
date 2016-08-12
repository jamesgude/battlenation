/**
 * <p>Title: AuthForm</p>
 * <p>Description:</p>
 * @author Bradley Gude
 * @version 1.0
 */

package com.businesshaps.am.client;

import com.businesshaps.am.server.AppServer;
import com.businesshaps.am.tools.PageProxy;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class AuthForm {
    public AuthForm(HttpServletRequest request, HttpServletResponse response, PrintWriter out) throws IOException {
        //out.println("Login");
        AppServer server = AppServer.getInstance();

        String username = request.getParameter("username");
        String password = request.getParameter("password");
        boolean submit = (request.getParameter("submit") != null && Boolean.parseBoolean(request.getParameter("submit")));
        boolean keepsignedin = (request.getParameter("keepsignedin") != null && Boolean.parseBoolean(request.getParameter("keepsignedin")));

        String su = request.getParameter("su");
        if (su == null) {
        	su = request.getRequestURL().toString();
        }
        su = (su == null ? request.getRequestURL().toString() + (request.getQueryString() != null ? "?" + request.getQueryString() : "") : su);
        su = su.replaceAll("username", username);
        String user_token = null;
        String user_token_clean = null;
        try {
            if (request.getCookies() != null) {
                for (Cookie cookie : request.getCookies()) {
                	System.out.println("COokie Name: " + cookie.getName() + " - " + cookie.getValue());
                    if (cookie.getName().equals("user_token")) {
                        user_token = cookie.getValue();
                        user_token = (user_token.equals("") || user_token.equals("null") ? null : user_token);
                        if (server != null && server.getAuthManager() != null && user_token != null) {
                            if (!server.getAuthManager().checkUserToken(user_token)) {
                                user_token = null;
                                cookie = new Cookie("user_token", null);
                                cookie.setMaxAge(-1);
                                cookie.setPath("/");
                                response.addCookie(cookie);
                                System.out.println("Deleting cookie - " + user_token);

                            } else {
                            	user_token_clean = user_token;
                               // break;
                            }
//                            break;
                        } else {
                            user_token = null;
                        }
                        
                    }
                }
                user_token = user_token_clean;
            } else {
                user_token = (String) request.getParameter("ut");
                if (user_token != null)
                    user_token = (user_token.equals("") || user_token.equals("null") ? null : user_token);
                if (user_token != null) {
                    if (!server.getAuthManager().checkUserToken(user_token)) {
                        user_token = null;
                    }

                }
            }


        } catch (Exception error) {
            error.printStackTrace();
        }
        if (user_token == null) {
        } else {
        	su = su.replaceAll("username", username);
            if (su.indexOf("?") > -1) {
                response.sendRedirect(su + "&user_token=" + user_token + "&done=true&osu=" + su);
            } else {
                response.sendRedirect(su + "?user_token=" + user_token + "&done=true&osu=" + su);
            }
            return;
        }

        if (username != null && username.length() > 0) {
            user_token = server.getAuthManager().authenticateUser(username, password);
                        
            if (user_token != null) {
                if (request.getCookies() != null) {
                    Cookie cookie = new Cookie("user_token", user_token);
                    if (keepsignedin) {
                        cookie.setMaxAge(365 * 24 * 60 * 60);
                    } else {
                        cookie.setMaxAge(1 * 24 * 60 * 60);
                    }
                    cookie.setPath("/");
                    response.addCookie(cookie);
                } else {

                }

                request.getSession().setAttribute("username", username);

                if (su.indexOf("?") > -1) {
                    response.sendRedirect(su + "&user_token=" + user_token + "&done=true&osu=" + su);
                } else {
                    response.sendRedirect(su + "?user_token=" + user_token + "&done=true&osu=" + su);
                }
                return;
            }
        }
 
        boolean approot = (request.getParameter("approot") != null && Boolean.parseBoolean(request.getParameter("approot")));

        RequestDispatcher dispatcher;
        try {
            if (PageProxy.WebFileExists("/signin/auth.jsp", request, approot)) {
                dispatcher = request.getRequestDispatcher("/signin/auth.jsp");
                dispatcher.include(request, response);
            } else {
                if (server != null && server.getAppProperties() != null && server.getAppProperties().getDomain() != null && su.contains(server.getAppProperties().getDomain())) {
                    out.println("<form name=frmLogin action='/home/signin/submit/" + (request.getQueryString() != null ? "?" + request.getQueryString() : "") + "' method=post>");
                } else {
                    out.println("<form name=frmLogin action='/home/signin/submit/" + (request.getQueryString() != null ? "?" + request.getQueryString() : "") + "' method=post>");
                }
                out.println("<input type=hidden name=su value='" + su + "'>");
                out.println("<input type=hidden name=submit value=true>");
                if (submit) {
                    out.println("<div class='invalid-creds-field align=center>Invalid Username/Password</div>");
                }

                out.println("<div class='username-field'><input type='text' name='username' value='" + (username != null ? username : "") + "'></div>");
                out.println("<div class='password-field'><input type='password' name='password' value=''></div>");
                //out.println("<div class='keepsignedin-field'><input type=checkbox id=keepsignedin value=true " + (keepsignedin ? "checked" : "") + " name=keepsignedin> <label for=keepsignedin>Keep me signed in</label></div>");
                out.println("<div class='submit-field'><button type=submit>Sign In</button></div>");
                out.println("</form>");
            } 

        } catch (Exception e) {

        }


    }
}
