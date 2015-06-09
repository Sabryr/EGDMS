/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package no.ntnu.medisn.egenvar;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.LinkedHashMap;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;

/**
 *
 * @author sabryr
 */
public class Search_expanded extends HttpServlet {

    private DataSource dataSource;
//    private Connection ncon;
    private String refreshrate = "2";
    private final int MAX_RESULTS_TO_KEEP = 20;
    private HashMap<String, ArrayList<String>> cat2table_map;
    private ArrayList<String> category_l;
//    private final String E = "_E_";
    private HashMap<String, LinkedHashMap<String, ArrayList<Object[]>>> uer_searches_map;

    public Search_expanded() {
        super();
    }

    private void pageReload(HttpSession ses, HttpServletResponse response, String url, int caller) {
        try {
            if (!response.isCommitted()) {
                response.sendRedirect(url);
            } else {
                System.out.println("43 " + caller + " Refresh failed");
            }
        } catch (IOException ex) {
        }
    }

    /**
     * Processes requests for both HTTP
     * <code>GET</code> and
     * <code>POST</code> methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=ISO-8859-1");
        PrintWriter out = response.getWriter();
          Connection c_con = null;
        try {
            c_con = getDatasource().getConnection();
        int server_mode = Constants.getServer_mode(c_con);
      
        Enumeration<String> en = request.getParameterNames();
        HttpSession ses = request.getSession(true);

        if (uer_searches_map == null) {
            uer_searches_map = new HashMap<>();
        }

     

            if (ses.getAttribute("ATRB_LOAD") == null) {
                pageReload(ses, response, Constants.getServerName(c_con) + "index", 70);
                Constants.loadSessionVariables(c_con,request);
            } else {
                String last_search_val = "";
                if (request.getParameter("query_used") != null) {
                    last_search_val = request.getParameter("query_used");
                } else if (ses.getAttribute(Constants.LAST_SEARCH_VALUE_SESSION_FLAG) != null) {
                    last_search_val = (String) ses.getAttribute(Constants.LAST_SEARCH_VALUE_SESSION_FLAG);
                }
                ArrayList<String> tag_source_l;
                if (ses.getAttribute("tag_sources") != null) {
                    tag_source_l = (ArrayList<String>) ses.getAttribute("tag_sources");
                } else {
                    tag_source_l = get_tagsourceNames(c_con);
                    ses.setAttribute("tag_sources", tag_source_l);
                }
                HashMap<String, String> table2txtbx_map = new HashMap<>();
                int colspan = 3;

                out.println("<!DOCTYPE html>");
                out.println("<html>");
                out.println("<head>");
                out.println("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=ISO-8859-1\">");
                out.println("<link HREF=\"" + Constants.getServerName(c_con) + "resources/egenStyler.css\"  REL=\"stylesheet\" TYPE=\"text/css\" >");
                out.println("<link rel=\"stylesheet\" href=\"../resources/base/jquery.ui.all.css\">\n"
                        + "	<script src=\"" + Constants.getServerName(c_con) + "js/jquery-1.10.2.js\"></script>\n"
                        + "	<script src=\"" + Constants.getServerName(c_con) + "js/ui/jquery.ui.core.js\"></script>\n"
                        + "	<script src=\"" + Constants.getServerName(c_con) + "js/ui/jquery.ui.widget.js\"></script>\n"
                        + "	<script src=\"" + Constants.getServerName(c_con) + "js/ui/jquery.ui.position.js\"></script>\n"
                        + "	<script src=\"" + Constants.getServerName(c_con) + "js/ui/jquery.ui.menu.js\"></script>\n"
                        + "	<script src=\"" + Constants.getServerName(c_con) + "js/ui/jquery.ui.autocomplete.js\"></script>\n"
                        + "	");
                for (int i = 0; i < tag_source_l.size(); i++) {
                    String c_tbl = tag_source_l.get(i);
                    String c_txt_bx = c_tbl;
                    table2txtbx_map.put(c_tbl, c_txt_bx);
                    out.println(getAutoCompleteScript(c_con,c_tbl, c_txt_bx));
                }

//                out.println("<script src=\"https://ajax.googleapis.com/ajax/libs/jquery/1.11.0/jquery.min.js\"></script>");
//                out.println("	<link href=\"//ajax.googleapis.com/ajax/libs/jqueryui/1.9.2/themes/ui-darkness/jquery-ui.css\" rel=\"stylesheet\">\n"
//                        + "	<script src=\"//ajax.googleapis.com/ajax/libs/jquery/1.10.2/jquery.min.js\"></script>\n"
//                        + "	<script src=\"//ajax.googleapis.com/ajax/libs/jqueryui/1.9.2/jquery-ui.min.js\"></script>");


//         + "jQuery.getJSON(\'" + Constants.getServerName() + "Jason_response\',function(result){\n"
//                        + "	source: result\n"
////                        + "    });"
//                out.println("<script>\n"
//                        + "	jQuery(function() {\n"
//                        + "		jQuery( \"#tags\").autocomplete({\n"
//                        + "                     delay: 1000,\n"
//                        + "			minLength: 3,\n"
//                        + "                     source: function(request, response) {\n"
//                        + "                         jQuery.getJSON(\"" + Constants.getServerName() + "Jason_response?\","
//                        + "                         {table: \"files\","
//                        + "                             field: \"name\","
//                        + "                             term: request.term},"
//                        + "                         function(data) {\n"
//                        + "                             var results = [];\n"
//                        + "                             $.each(data, function( key, val ) {\n"
//                        + "                                 results.push(val.name);\n"
//                        + "                             });"
//                        + "                             response(results);"
//                        + "                         })"
//                        //                        + "                             .success(function() { alert(\"second success\"); })\n"
//                        + "                             .fail(function(jqXHR, status, error) { alert(error); })\n"
//                        //                        + "                             .complete(function() { alert(\"complete\"); }\n);"
//                        + "                     }\n" //end source             
//                        + "             });\n" //end Jquery                
//                        + "     });\n"
//                        + "	</script>");
//                https://api.github.com/users/yui?callback=handleJSONP
//                out.println("<script>"
////                            + "$.getJSON(\""+Constants.getServerName()+"test.txt\", function() {\n"
//                        + "$.getJSON(\""+Constants.getServerName()+"Jason_response\", function() {\n"
////                         + "$.getJSON(\"https://api.github.com/users/yui?callback=handleJSONP\", function() {\n"
//                        + "  alert(\"success\");\n"
//                        + "})\n"
//                        + ".success(function() { alert(\"second success\"); })\n"
//                        + ".fail(function(jqXHR, status, error) { alert(error); })\n"
//                        + ".complete(function() { alert(\"complete\"); }\n);"
//                        + "</script>");
//                out.println("$(document).ready(function(){\n"
//                        + "  $(\"button\").click(function(){\n"
//                        + "    $.ajax({url:\"demo_test.txt\",success:function(result){\n"
//                        + "      $(\"#div1\").html(result);\n"
//                        + "    }});\n"
//                        + "  });\n"
//                        + "});");
//                out.println("<script>\n"
//                        + "$(document).ready(function(){\n"
//                        + "  $(\"button\").click(function(){\n"
//                        + "    $(\"#div1\").load(\""+Constants.getServerName()+"Jason_response\",function(responseTxt,statusTxt,xhr){\n"
//                        + "      if(statusTxt==\"success\")\n"
//                        + "        alert(\"External content loaded successfully!\");\n"
//                        + "      if(statusTxt==\"error\")\n"
//                        + "        alert(\"Error: \"+xhr.status+\": \"+xhr.statusText);\n"
//                        + "    });\n"
//                        + "  });\n"
//                        + "});\n"
//                        + "</script>");
                out.println("</head>");
                out.println("<body>");
                out.println(ses.getAttribute("Header"));

//                out.println("<div class=\"ui-widget\">\n"
//                        + "	<label for=\"tags\">Tags: </label>\n"
//                        + "	<input id=\"tags\">\n"
//                        + "</div>");

                out.println("<form accept-charset=\"ISO-8859-1\"  accept-charset=\"ISO-8859-1\">"); 
                out.println("   <table border=\"1\">");
                out.println("    <thead>");
                out.println("       <tr><th colspan=\"" + colspan + "\">");
                out.println("        Avaialable types");
                out.println("       </th></tr>");
                out.println("    </thead>");
                out.println("       <tbody>");
                out.println("           <tr><td colspan=\"" + colspan + "\">");
                out.println("               <input type=\"text\" style=\"background-color:#fefeff; border:1px inset;\" value=\"" + last_search_val + "\" size=\"100\" name=\"searchvalue\" />");
                out.println("                <input type=\"Checkbox\" name=\"exact\" value=\"exact\"  /> Only exact matches");
                out.println("               <input type=\"submit\" value=\"Search\" name=\"search_btn\"  onclick='setProgress(\"div_progress\", \"visible\")' />");
                out.println("           </td></tr>");
                out.println("           <tr><td>");
                out.println("           </td></tr>");
                for (int i = 0; i < tag_source_l.size(); i++) {
                    String c_tbl = tag_source_l.get(i);
                    if (i == 0) {
                        out.println("          <tr><td>");
                    } else if (i % 3 == 0) {
                        out.println("          </tr><tr><td>");
                    } else {
                        out.println("          <td>");
                    }
                    out.println(c_tbl.replace(Constants.TAGSOURCE_TABLE, "") + " " + "<input type=\"text\" name=\"" + table2txtbx_map.get(c_tbl) + "\" id=\"" + table2txtbx_map.get(c_tbl) + "\" />");
                    out.println("          </td>");
                }
                out.println("          </tr>");
                out.println("           <tr><td></td></tr>");
                out.println("           <tr><td></td></tr>");
                out.println("           <tr><td></td></tr>");
                out.println("       </tbody>");
                out.println("   </table>");
                out.println("</form>");
               
                out.println("<footer>" + ses.getAttribute("footer") + "</footer>");
                out.println("</html>");
            }
         close(c_con, null, null);
        } catch (SQLException ex) {
        } finally {
            out.close();
            close(c_con, null, null);
        }
    }

    private ArrayList<String> get_tagsourceNames(Connection c_con) {
        ArrayList<String> tag_source_l = new ArrayList<>();
        ArrayList<String> full_tag_source_l = Constants.getTagsourceList(c_con);
        for (int i = 0; i < full_tag_source_l.size(); i++) {
            String c_s = full_tag_source_l.get(i);
            tag_source_l.add(c_s.split("\\.")[c_s.split("\\.").length - 1].toUpperCase()); //.replace(Constants.TAGSOURCE_TABLE, "")
        }
        return tag_source_l;
    }

    private ArrayList<String> get_files(Connection c_con) {
        ArrayList<String> tag_source_l = new ArrayList<>();
        ArrayList<String> full_tag_source_l = Constants.getTagsourceList(c_con);
        for (int i = 0; i < full_tag_source_l.size(); i++) {
            String c_s = full_tag_source_l.get(i);
            tag_source_l.add(c_s.split("\\.")[c_s.split("\\.").length - 1].toUpperCase().replace(Constants.TAGSOURCE_TABLE, ""));
        }
        return tag_source_l;
    }

    private String getAutoCompleteScript(Connection c_con,String table_nm, String textbox) {
        String result = "";
        result = "<script>\n"
                + "	jQuery(function() {\n"
                + "		jQuery( \"#" + textbox + "\").autocomplete({\n"
                + "                     delay: 1000,\n"
                + "			minLength: 3,\n"
                + "                     source: function(request, response) {\n"
                + "                         jQuery.getJSON(\"" + Constants.getServerName(c_con) + "Jason_response?\",\n"
                + "                         {table: \"" + table_nm + "\",\n"
                + "                             field: \"PATH\",\n"
                + "                             term: request.term},\n"
                + "                         function(data) {\n"
                + "                             var results = [];\n"
                + "                             $.each(data, function( key, val ) {\n"
                + "                                 results.push(val.name);\n"
                + "                             });"
                + "                             response(results);\n"
                + "                         })"
                //                        + "                             .success(function() { alert(\"second success\"); })\n"
                + "                             .fail(function(jqXHR, status, error) { alert(error); })\n"
                //                        + "                             .complete(function() { alert(\"complete\"); }\n);"
                + "                     }\n" //end source             
                + "             });\n" //end Jquery                
                + "     });\n"
                + "	</script>";
        return result;
    }

    public static void close(Connection connection, Statement statement, ResultSet resultSet) {
        try {
            if (resultSet != null && !resultSet.isClosed()) {
                try {
                    resultSet.close();
                } catch (SQLException e) {
                }
            }
        } catch (SQLException e) {
        }
        try {
            if (statement != null && !statement.isClosed()) {
                try {
                    statement.close();
                } catch (SQLException e) {
                }
            }
        } catch (SQLException e) {
        }
        try {
            if (connection != null && !connection.isClosed()) {
                try {
                    connection.close();
                } catch (SQLException e) {
                }
            }
        } catch (SQLException e) {
        }
    }

      private DataSource getDatasource() throws ServletException {
        if (dataSource == null) {
            try {
                Context env = (Context) new InitialContext().lookup("java:comp/env");
                dataSource = (DataSource) env.lookup(Constants.DATASOURCE_NAME_DATA_CREATE);
                if (dataSource == null) {
                    throw new ServletException("`" + Constants.DATASOURCE_NAME_DATA_CREATE + "' is an unknown DataSource");
                }
            } catch (NamingException e) {
                throw new ServletException(e);
            }
        }
        return dataSource;
    }
    @Override
    public void destroy() {
//        try {
//            if (ncon != null) {
//                ncon.close();
//            }
//        } catch (SQLException ex) {
//        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP
     * <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP
     * <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>
}
