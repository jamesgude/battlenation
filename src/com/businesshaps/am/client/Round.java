/**
 * <p>Title: Round</p>
 * <p>Description:</p>
 * @author Bradley Gude
 * @version 1.0
 */

package com.businesshaps.am.client;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.Hashtable;

public class Round {
    public Round(HttpServletRequest request, HttpServletResponse response, Hashtable<String, String> querystring) {
        PrintWriter out = null;
        try {
            out = response.getWriter();
        } catch(Exception e) {
            e.printStackTrace();
        }
        String outerbgcolor = querystring.get("outerbgcolor");
        String bgcolor = querystring.get("bgcolor");
        String bordercolor = querystring.get("bordercolor");
        String title = querystring.get("title");
        String titlebg = querystring.get("titlebg");
        String titlecolor = querystring.get("titlecolor");
        String titleborder = querystring.get("titleborder");
        String borderwidth = querystring.get("borderwidth");
        String padding = querystring.get("padding");
        boolean top = (querystring.get("top") != null && Boolean.parseBoolean(querystring.get("top")));
        boolean notitletop = (querystring.get("notitletop") != null && Boolean.parseBoolean(querystring.get("notitletop")));
        boolean doshadow = (querystring.get("doshadow") != null && Boolean.parseBoolean(querystring.get("doshadow")));
        boolean do100 = (querystring.get("do100") != null && Boolean.parseBoolean(querystring.get("do100")));

        if (padding == null) {
            padding = "0";
        }
        //doshadow = false;
        // ;


        if (doshadow) {
            borderwidth = "1";
        }
        if (top) {
            title = title.replaceAll("@amp@", "&");

            out.print("<div style='" + (do100 ? "height:100%;" : "") + ";' class=round1>");
            out.print("  <div style='background-color:" + bordercolor + ";;background-" + (bgcolor.indexOf("gif") > -1 ? "image" : "color" + ":" + (bgcolor.indexOf("gif") > -1 ? "url(" + bgcolor + ")" : bgcolor)) + ";' class=round2>");
            out.print("    <table cellspacing=0 cellpadding=0 width=100% height=100% border=0 style=''>");
            if (!notitletop) {
                out.print("      <tr height=1>");
                if (doshadow) {
                    out.print("        <td rowspan=5 class=round3 style='border-color: " + outerbgcolor + ";' align=right width=1 valign=top ><div style='' class=round2_0><img src=/images/spacer.gif width=1 height=1></div></td>");
                }
                out.print("        <td colspan=4 width=100% style='background-image:url(\"/images/" + titlebg + "\");' class=round2_1 valign=top>");
                out.print("          <table width=100% cellspacing=0 cellpadding=0 style='" + borderwidth != null && borderwidth.equals("0") ? "position:absolute;width:100%;" : "" + ";'>");
                out.print("            <tr>");
                out.print("              <td valign=top width=5 style='' class=round4><img src='/roundborder?type=lt&bgcolor=transparent&outerbgcolor=" + outerbgcolor.replaceAll("#", "%23") + "'></td>");
                out.print("              <td valign=top width=100% style='' class=zero>&nbsp;</td>");
                out.print("              <td  width=5 align=right nowrap valign=top sstyle='height:6px;font-size:0px;padding:0px;'><img src='/roundborder?type=rt&bgcolor=transparent&outerbgcolor=" + outerbgcolor.replaceAll("#", "%23") + "'></td>");
                out.print("            </tr>");
                if (title != null && title.length() > 0) {
                    out.print("            <tr style=''>");
                    out.print("              <td  colspan=3 width=100% height='10' class=round2_2 style='" + titleborder != null && titleborder.length() > 0 ? "bborder-bottom: 1px solid " + titleborder : "" + ";" + titlecolor != null && titlecolor.length() > 0 ? "color:" + titlecolor : "" + ";background-color:" + titlebg != null && titlebg.length() > 0 ? titlebg : bordercolor + ";'>");
                    out.print("                " + title + "");
                    out.print("              </td>");
                    out.print("            </tr>");
                }
                out.print("          </table>");
                out.print("        </td>");
                if (doshadow) {
                    out.print("        <td rowspan=5 style='border-color:" + outerbgcolor + ";border-top:8px solid " + outerbgcolor + ";border-bottom:7px solid " + outerbgcolor + ";' class=round2_3 align=right width=1 valign=top><div style='' class=round_shadow_1><img src=/images/spacer.gif width=1 height=1></div></td>");
                    out.print("        <td rowspan=5 style='border-color:" + outerbgcolor + ";border-top:10px solid " + outerbgcolor + ";border-bottom:8px solid " + outerbgcolor + ";' class=round2_4 align=right width=1 valign=top><div style='' class=round_shadow_2><img src=/images/spacer.gif width=1 height=1></div></td>");
                }
                out.print("      </tr>");
            }
            out.print("      <tr>");
            out.print("        <td colspan=4 valign=top class=round2_5 style='padding:" + padding + ";border-right:" + borderwidth == null ? "2px" : borderwidth + " solid " + bordercolor + ";border-left:" + borderwidth == null ? "2px" : borderwidth + " solid " + bordercolor + ";border:0px solid red;background-" + (bgcolor.indexOf("gif") > -1 ? "image" : "color" + ":" + (bgcolor.indexOf("gif") > -1 ? "url(" + bgcolor + ")" : bgcolor)) + ";' >");

        } else {

            out.print("        </td>");
            out.print("      </tr>");
            if (!notitletop) {
                out.print("      <tr height=2 style=''>");
                out.print("        <td colspan=4>");
                out.print("          <table width=100% cellspacing=0 cellpadding=0>");
                out.print("            <tr height=6 style='" + borderwidth != null && borderwidth.equals("0") ? "display:none;" : "" + ";'>");
                out.print("              <td valign=bottom class=zero><img src='/roundborder?type=lb&doshadow=" + doshadow + "&bgcolor=transparent&outerbgcolor=" + outerbgcolor.replaceAll("#", "%23") + "'></td>");
                out.print("          <td valign=top class='zero " + (doshadow ? "round3_1" : "") + "'  width='100%'><img src=/images/spacer.gif heigth=1 width=1></td>");

                out.print("          <td  align=right valign=bottom class=zero style='" + (doshadow ? "padding-bottom:1px;background-color:" + outerbgcolor : "" + "'><img style='" + (doshadow ? ("padding-bottom:0px;background-color:" + bordercolor + ";") : "")) + ";' src='/roundborder?type=rb&doshadow=" + doshadow + "&bgcolor=transparent&outerbgcolor=" + outerbgcolor.replaceAll("#", "%23") + "'></td>");
                out.print("            </tr>");
                out.print("          </table>");
                out.print("        </td>");
                out.print("      </tr>");

            }
            if (doshadow & 1 == 2) {
                out.print("      <tr>");
                out.print("        <td colspan=4 style='border-color:" + outerbgcolor + "' class=round2_6 align=right height=1 valign=top><div style='' style='' class=round_shadow_3><img src=/images/spacer.gif width=1 height=1></div></td>");
                out.print("      </tr>");
                out.print("      <tr>");
                out.print("        <td colspan=4 style='border-color:" + outerbgcolor + "' class=round2_7 align=right height=1 valign=top><div style='' class=round_shadow_3_1><img src=/images/spacer.gif width=1 height=1></div></td>");
                out.print("      </tr>");
                out.print("      <tr>");
                out.print("        <td colspan=4 style='border-color:" + outerbgcolor + "' class=round2_8 align=right height=1 valign=top><div style='' class=round_shadow_4><img src=/images/spacer.gif width=1 height=1></div></td>");
                out.print("      </tr>");
                out.print("      <tr>");
                out.print("        <td colspan=4 style='border-color:" + outerbgcolor + "' class=round2_9 align=right height=1 valign=top><div style='' class=round_shadow_4><img src=/images/spacer.gif width=1 height=1></div></td>");
                out.print("      </tr>");
            }
            out.print("    </table>");
            out.print("  </div>");
            out.print("</div>");
        }
    }
}
