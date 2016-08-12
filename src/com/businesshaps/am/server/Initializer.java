/**
 * <p>Title: Initializer</p>
 * <p>Description:</p>
 * @author Bradley Gude
 * @version 1.0
 */

package com.businesshaps.am.server;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class Initializer extends HttpServlet {
    public static boolean loaded = false;
    public Initializer() {
        if (loaded) {
            return;
        }
        loaded = true;

    }
    public void doGet(HttpServletRequest req,
                      HttpServletResponse res)
            throws ServletException, IOException {
        //PrintWriter out = res.getWriter();

//        System.out.close();
    }
}
