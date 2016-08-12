/**
 * <p>Title: SessionHandler</p>
 * <p>Description:</p>
 * @author Bradley Gude
 * @version 1.0
 */

package com.businesshaps.am;

import com.businesshaps.am.businessobjects.AppUser;
import com.businesshaps.am.server.AppServer;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.servlet.FilterConfig;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.util.Date;
import java.util.Properties;

public class SessionHandler {
    private static String jdbc;
    private static String orig_jdbc;
    public static String companyTitle = "";
    public static FilterConfig filterConfig = null;

    public static Properties getBeanProperties(File file, String bean, Properties props) {
        //File file = new File(config.getServletContext().getRealPath("") + sep + "WEB-INF" + sep + "obinject.xml");
        if (props == null) {
            props = new Properties();
        }
        
        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(file);
            doc.getDocumentElement().normalize();
            NodeList nodeLst = doc.getElementsByTagName(bean);
//            System.out.println("nodelist: " + nodeLst.toString());
            Node fstNode;
            Element fstElmnt;
            NodeList fstNmElmntLst;
            Element fstNmElmnt;
            NodeList fstNm;
            String name, value;
            for (int s = 0; s < nodeLst.getLength(); s++) {
                fstElmnt = (Element) nodeLst.item(s);
                fstNmElmntLst = fstElmnt.getElementsByTagName("property");
                for (int i = 0; i < fstNmElmntLst.getLength(); i++) {
                    fstNmElmnt = (Element) fstNmElmntLst.item(i);
                    fstNm = fstNmElmnt.getChildNodes();
                    name = fstNmElmnt.getAttribute("name");
                    value = ((Node) fstNm.item(0)).getNodeValue();
                    props.setProperty(name, value);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } 
        return props;

    }

    public static String getRootPath() {
        return filterConfig.getServletContext().getRealPath("");
    }

    public static String getJdbc() {
        return jdbc;
    }

    public static void setJdbc(String ijdbc) {
        jdbc = ijdbc;
    }

    public static String getOrigJdbc(HttpServletRequest req) {
        return orig_jdbc.replaceAll("#org#", getOrgRoot(req));
    }

    public static void setOrigJdbc(String ijdbc) {
        orig_jdbc = ijdbc;
    }

    public static AppUser getSessionUser(HttpSession session) {
        AppUser user = new AppUser();
        user.setUsername("anon");
        user.setDisplayName("Anonymous User");

        AppUser tryuser = null;

        String user_token = (String) session.getAttribute("user_token");
        AppServer server = AppServer.getInstance();
        user_token = (user_token == null || user_token.equals("") || user_token.equals("null") ? null : user_token);
        if (user_token != null) {
            tryuser = server.getAuthManager().getUserByUserToken(user_token);
        }
        if (tryuser != null) {
            user = tryuser;
        }
        return user;

    }

    public static String getOrgRoot(HttpServletRequest req) {

        String url = req.getRequestURL() + "?" + req.getQueryString();
        url = url.replaceAll("\\?null", "");

        int cStart = 0;
        String org = null;
        if (req.getContextPath().toString().equals("")) {
            String[] urls = req.getRequestURL().toString().split("/");
            if (urls.length > 3) {
                org = urls[3];
            } else {
                org = urls[urls.length - 1];
            }
        } else {
            cStart = url.indexOf(req.getContextPath()) + req.getContextPath().length() + 1;
            org = url.substring(cStart, url.length()).split("/")[0];
        }
        return org;

    }

    public static String handleSession(HttpServletRequest request, HttpServletResponse response) {
        String session_id = null;
        boolean from_cookie = false;
        if (request.getCookies() == null) {
            return null;
        }
        for (Cookie cookie : request.getCookies()) {
            if (cookie.getName().toUpperCase().equals("SESSION_ID")) {
                session_id = cookie.getValue();
                from_cookie = true;
                //break;
            }
        }


        if (session_id == null) {
            session_id = "session_" + new Date().getTime();
            Cookie cookie = new Cookie("SESSION_ID", session_id);
            cookie.setMaxAge(365 * 24 * 60 * 60);
            cookie.setPath("/");
            response.addCookie(cookie);

        }
        System.out.println("SESSION_ID: " + session_id + " - FROM_COOKIE = " + from_cookie);
        return session_id;
    }

    public static String getBaseURL(HttpServletRequest request) {
        String baseURL = request.getRequestURL().toString().replace(request.getRequestURI().substring(1), request.getContextPath());
        return baseURL;
    }

    public static boolean isCustomer(HttpServletRequest request) {
        return false;
    }

    public static boolean isPartner(HttpServletRequest request) {
        return false;
    }

    public SessionHandler() {

    }
}
