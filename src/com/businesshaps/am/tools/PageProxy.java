/**
 * <p>Title: PageProxy</p>
 * <p>Description:</p>
 * @author Bradley Gude
 * @version 1.0
 */

package com.businesshaps.am.tools;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.JspWriter;
import java.io.*;
import java.net.URL;
import java.util.Hashtable;

public class PageProxy {
    public PageProxy() {

    }

    public static boolean WebFileExists(String filename, HttpServletRequest request, boolean approot) {

        String fpath = request.getRealPath("");
        String wpath = "";
        String d;

        String[] dirs = filename.split("/");

        fpath = request.getRealPath("");
        wpath = "";
        d = "";
        for (int i = 1; i < (dirs.length); i++) {
            d = dirs[i];
            if (i == 1 && approot) {
                d = "approot";
            }

            fpath += "\\" + d;
            wpath += "/" + d;
        }
        /*
        System.out.println("filename: " + filename);
        System.out.println("path: " + fpath);
        System.out.println("real path: " + request.getRealPath(""));
        System.out.println("is approot: " + approot);
        */
        File file = new File(fpath);
        return file.exists();
    }

    public static String fetchPage(String url) {
        StringBuffer out = new StringBuffer();
        url = url.replace("https:", "http:");
        url = url.replace(":8443", ":8080");
        url = url.replace(":443", ":80");
        try {
            URL authurl = new URL(url);
            BufferedReader webpage = new BufferedReader(new InputStreamReader(authurl.openStream()));
            String html;
            while ((html = webpage.readLine()) != null) {
                out.append(html);
            }
            webpage.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return out.toString();

    }
    public static void fetchPage(String url, JspWriter out) {
        url = url.replace("https:", "http:");
        url = url.replace(":8443", ":8080");
        url = url.replace(":443", ":80");
        try {
        URL authurl = new URL(url);
        BufferedReader webpage = new BufferedReader(new InputStreamReader(authurl.openStream()));
        String html;
        while ((html = webpage.readLine()) != null) {
            out.println(html);
        }
        webpage.close();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    private static Hashtable<String, String> cache = new Hashtable<String, String>();
    public static void doProxy(String url, HttpServletRequest request, HttpServletResponse response) {
        url = url.replace("https:", "http:");
        url = url.replace(":8443", ":8080");
        url = url.replace(":443", ":80");

        String item;
        String qs = request.getQueryString();
        if (qs != null && !qs.equals("")&&!url.contains("?")) {
            qs = "?" + qs;
        } else {
            qs = "";
        }
        item = url + qs;
        item = item.replaceAll(" ", "%20");
        String[] files = url.split("/");
        String file = files[files.length - 1];
        file = file.split("\\?")[0];
        String[] exts = file.split("\\.");
        String ext = exts[exts.length - 1];
        String type = "text/html";
        boolean doattach = true;
        if (ext.startsWith("gif")) {
            type = "image/gif";
            doattach = false;
        } else if (ext.startsWith("jpg")) {
            type = "image/jpg";
            doattach = false;
        } else if (ext.startsWith("png")) {
            type = "image/png";
            doattach = false;
        } else if (ext.startsWith("htm")) {
            doattach = false;
        } else if (ext.startsWith("jsp")) {
            doattach = false;
        }

        if (type.contains("image")) {
            try {
                response.sendRedirect(item);
                return;
            } catch(Exception e) {
                e.printStackTrace();
            }
        }
        try {

            OutputStream os = response.getOutputStream();
            URL authurl = new URL(item);
            InputStream is = authurl.openStream();
            response.setContentType(type);
            if (doattach) {
                response.setHeader("Content-Disposition", "attachment;filename=" + file.split("\\?")[0]);
            }


            int j = 0;
            while ((j = is.read()) != -1) {
                os.write(j);
            }
            os.flush();
            os.close();
        } catch (Exception e) {
            System.out.println("File not found: " + item);
        }
    }
}
