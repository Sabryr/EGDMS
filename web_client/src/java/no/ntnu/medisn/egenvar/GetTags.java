/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package no.ntnu.medisn.egenvar;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Scanner;
import javax.jws.WebMethod;
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
public class GetTags extends HttpServlet {

    private DataSource dataSource_data;
    private final static String DATASOURCE_NAME_data = "egen_dataView_resource";
    private DataSource dataSource;

    private void pageReload(HttpServletResponse response, String url, int caller) {
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
        HttpSession ses = request.getSession(true);
        PrintWriter out = response.getWriter();
        Connection c_con = null;
        try {
            c_con = getDatasource().getConnection();
//            String msg = "";
            GetTags_controler control;
            StringBuilder result_b = new StringBuilder();

            boolean refreshing = false;
            boolean reload = false;
            Object searchvalue = request.getParameter("searchvalue");
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=ISO-8859-1\">");
            String last_search_val = "";
            if (ses.getAttribute("GetTags_controler") != null) {
                control = (GetTags_controler) ses.getAttribute("GetTags_controler");
                if (control.isDone()) {
                    result_b.append(control.getResults());
//                    System.out.println("87 "+control.getResults());
                    ses.setAttribute("last_controler_string_builder", control.getResults().toString());
                    ses.removeAttribute("GetTags_controler");
                } else {
                    result_b.setLength(0);
                    result_b.append("<warning>Still processing</warning>");
                    out.println("<META HTTP-EQUIV=\"refresh\" CONTENT=\"5\">");
                    refreshing = true;
                }
            } else if (searchvalue != null) {
                last_search_val = searchvalue.toString();
                control = new GetTags_controler(last_search_val, Constants.getServerName(c_con));
                (new Thread(control)).start();
                ses.setAttribute("GetTags_controler", control);
                reload = true;
            } else if (ses.getAttribute("last_controler_string_builder") != null) {
                result_b.append(ses.getAttribute("last_controler_string_builder"));
            }
            if (reload) {
                pageReload(response, Constants.getServerName(c_con) + "Search/GetTags", 171);
            }
            if (ses.getAttribute("last_search_val") != null) {
                last_search_val = (String) ses.getAttribute("last_search_val");
            }
            out.println(" <link HREF=\"" + Constants.getServerName(c_con) + "resources/egenStyler.css\"  REL=\"stylesheet\" TYPE=\"text/css\" >");
            out.println(" <script language = JavaScript>     \n"
                    + "            function setProgress(target, value){\n"
                    + "                if(document.getElementById(target)!=null){\n"
                    + "                    document.getElementById(target).style.visibility = value; \n"
                    + "                }\n"
                    + "                \n"
                    + "            } \n"
                    + "        </script>");
            out.println("<title>GenVar Datamangement System</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<table  class=\"table_text\" >\n"
                    + " <thead>\n"
                    + "     <tr>\n"
                    + "         <th>\n"
                    + "                <img  height=\"150\" width=\"1000\" src=\"../Banner.gif\"/>\n"
                    + "         </th>\n"
                    + "     </tr>\n"
                    + " </thead>\n"
                    + "</table>\n");
            out.println("<p>" + ses.getAttribute("Header") + "</p>");
            out.println("<h1>Tag investigator</h1>");
            if (!refreshing) {
                out.println("<form accept-charset=\"ISO-8859-1\"   method=\"post\">");
                out.println("   <table>");
                out.println("       <thead>");
                out.println("           <tr><th>");
                out.println("               Use Pubmed identifiers to investigate tags");
                out.println("           </th></tr>");
                out.println("       </thead>");
                out.println("       <tbody>");
                out.println("          <tr>");
                out.println("           <td>");
                out.println("              PMID <input type=\"text\" "
                        + "style=\"background-color:#fefeff; border:1px inset;\" "
                        + "value=\"" + last_search_val + "\" size=\"40\" name=\"searchvalue\" />");
                out.println("               <input type=\"submit\" value=\"Start Search\" name=\"search_btn\" "
                        + "onclick='setProgress(\"div_progress\", \"visible\")'/>");
                out.println("           </td>");
                out.println("          </tr>");
            } else {
                out.println(result_b);
            }
            out.println("          <tr>");
            out.println("           <td>");
            out.println(result_b);
            out.println("           </td>");
            out.println("          </tr>");
            out.println("       </tbody>");
            out.println("   </table>");
            out.println("</form>");
            out.println("<div id=\"div_progress\">Please wait .. <img src=\"../progress.gif\" width=\"200\" height=\"10\" alt=\"progress\"  id=\"progressanim\"/></div>");
            if (refreshing || reload) {
            } else {
                out.println("  <script type=\"text/javascript\">\n");
                out.println("   setProgress(\"div_progress\", \"hidden\");");
                out.println("  </script>\n");
            }
            out.println("</body>");
            out.println("</html>");
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            out.close();
            close(c_con, null, null);
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
