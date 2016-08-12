/**
 * <p>Title: Includer</p>
 * <p>Description:</p>
 * @author Bradley Gude
 * @version 1.0
 */

package com.businesshaps.am.tools;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;

public class Includer {
    public Includer() {

    }
	private static String fileSep = System.getProperty("file.separator");

    public static IncluderResponse handleInclude(HttpServletRequest request, HttpServletResponse response, String filename) {
        boolean approot = (request.getParameter("approot") != null && Boolean.parseBoolean(request.getParameter("approot")));
        String sp = request.getServletPath();
        
        if (sp.endsWith("/")) {
            sp += "index.page.jsp";
        }
        String[] dirs = sp.split("/");
        String fpath = request.getRealPath("") + fileSep;
        String wpath = "/";
        String d;
        IncluderResponse r = new IncluderResponse();
        r.setExists(false);
        for (int j = 0; j < 5; j++) {
            fpath = request.getRealPath("") + fileSep;
            wpath = "/";
            d = "";
            for (int i = 0; i < (dirs.length - j) - (approot&&dirs[dirs.length-1].equals("")?0:1); i++) {
                d = dirs[i];
                if (i == 1 && approot) {
                    d = "approot";
                }
                //if (d.length()>0) {
                    fpath += d + fileSep;
                    wpath += d + "/";
                //}
            }
            //System.out.println("wpath: " + wpath + " - fpath: " + fpath);
            String inc = wpath + filename;
            String fileCheck = new String(fpath + filename).replace("\\\\", "/");
            File file = new File(fileCheck);

            if (file.exists()) {
                r.setFilePath(file.getAbsolutePath());
                r.setWebPath(inc);
                r.setExists(true);
                break;

            }
        }
        return r;
    }

    public static boolean WebFileExists(String filename, HttpServletRequest request, boolean approot) {

        String fpath = request.getRealPath("");
        String wpath = "";
        String d;
        IncluderResponse r = new IncluderResponse();
        r.setExists(false);

        String[] dirs = filename.split("/");

        fpath = request.getRealPath("");
        wpath = "";
        d = "";
        for (int i = 1; i < (dirs.length); i++) {
            d = dirs[i];

            fpath += fileSep + d;
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
}
