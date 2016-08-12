/**
 * <p>Title: NoAccessManager</p>
 * <p>Description:</p>
 * @author Bradley Gude
 * @version 1.0
 */

package com.businesshaps.am.client;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class NoAccessManager {
    public NoAccessManager(HttpServletRequest request, HttpServletResponse response, PrintWriter out) throws IOException {
        out.println("<html><body><span style='font-family:tahoma;'>");
        out.println("<br>");
        out.println("<h3><img src=/images/lock.gif hspace=3 align=absmiddle> This area is restricted.</h3>");
        out.println("You do not have access to this area.");
        out.println("<a href='javascript:history.back();'>Click here to go back.</a>");
        out.println("  </span>");
        out.println("</body>");
        out.println("</html>");
        out.println("");
    }
}
