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
public class Index extends HttpServlet {

    private DataSource dataSource;

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
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
//            out.println(" <img  height=\"150\" width=\"1200\" src=\"Banner.gif\"/>\n");
            out.println(" <link HREF=\"resources/egenStyler.css\"  REL=\"stylesheet\" TYPE=\"text/css\" >"); //_public
            out.println("<title>GenVar Datamangement System</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<div class=\"div_style2\">");
//            out.println("<div style=\"align:center;width:95%;font-family:verdana;padding:10px;border-radius:10px;border:2px solid #888888;background-color:#dfdfdf;opacity:1;\">");
            out.println("<table  class=\"table_text\" >\n"
                    + " <thead>\n"
                    + "     <tr>\n"
                    + "         <th>\n"
                    + "<img  height=\"50%\" width=\"100%\" src=\"Banner.gif\"/>\n"
                    + "         </th>\n"
                    + "     </tr>\n"
                    + " </thead>\n"
                    + "</table>\n");
//            out.println("<div id=\"banner\">\n"
//                    + "    <div id=\"wrapper\">\n"
//                    + "        <div id=\"container\">\n"
//                    + "                <A HREF=\"http://www.google.com\"><img class=\"banner-img\" src=\"Banner.gif\" alt=\"N.A.L.A. Apparel\"/>\n"
//                    + "        </div>\n"
//                    + "    </div>\n"
//                    + "</div>");

            HttpSession ses = request.getSession(true);
            if (ses.getAttribute("ATRB_LOAD") == null) {
                Constants.loadSessionVariables(c_con, request);
            }

            Object current_user = ses.getAttribute("current_user");
            Object person_id = ses.getAttribute("person_id");
            Object person_tbl = ses.getAttribute("person_tbl"); // do something about it if null
            if (current_user == null) {
                out.println(ses.getAttribute("Header_public"));
                out.println("<table  class=\"table_msg\" >\n"
                        + " <thead>\n"
                        + "     <tr>\n"
                        + "         <th>\n"
                        + "             <h8><a href=\"login\" >Login</a></h8>  \n"
                        + "         </th>\n"
                        + "     </tr>\n"
                        + " </thead>\n"
                        + " <tbody>\n"
                        + "     <tr>\n"
                        + "             <td>Please login to access more functions.  <a href=\"" + Constants.getServerName(c_con) + "login\"> Login</a> | <a href=\"" + Constants.getServerName(c_con) + "requestAccount\">New account</a>  </td>\n"
                        + "     </tr>\n"
                        + "     <tr>\n"
                        + "         <td> \n"
                        + "          Host Server: <h8>" + Constants.getServerName(c_con) + "</h8>   (Version:" + ses.getAttribute("version") + ")\n"
                        + "         </td>\n"
                        + "         <td> \n"
                        + "          <a href=\"../webinterface_tutorial.pdf\" >Help</a> \n"
                        + "         </td>\n"
                        + "     </tr>\n"
                        + " </tbody>\n"
                        + "</table>");
            } else {
                out.println(ses.getAttribute("Header"));
                int server_mode = Constants.getServer_mode(c_con);
                out.println("<table class=\"table_msg\" >\n");

                out.println(" <tbody>");
                out.println("     <tr><td>Server_mode:</td>");
                if (server_mode < 0) {
                    out.println("    <td><h8>Error</h8></td>\n");
                } else if (server_mode == 0) {
                    out.println("    <td><h8>Local</h8>\n");
                } else if (server_mode > 0) {
                    out.println("    <h8>Central</h8>\n");
                }
                out.println("     </tr>");
                out.println("     <tr>"
                        + "         <td>You are logged in as: <h8>" + current_user + "</h8></td><td>(<a href=\"" + Constants.getServerName(c_con) + "logout\" >Logout</a>)</td>"
                        + "     </tr>");

                if (person_id == null) {
                    out.println(""
                            + "     <tr>"
                            + "         <td>No profile created yet (Search only mode)</td><td> (<a href=\"" + Constants.getServerName(c_con) + "Upload/CreateNew?current_tbl_nm=" + person_tbl + "\" >Create a profile</a>)</td>"
                            + "     </tr>");
                } else {
                    Object user_level = ses.getAttribute("user_level");
                    out.println(""
                            + "     <tr>"
                            + "         <td>User ID: <h8>" + person_id + "</h8> | Access_level:  <h8>" + user_level + "</h8></td><td>(<a href=\"" + Constants.getServerName(c_con) + "Submit_UseraccountRequest?delete_user=true\" >Delete account</a>)</td>"
                            + "     </tr>");
                }
                out.println(ses.getAttribute("displying_config"));
                out.println("</tbody>\n"
                        + "</table>");
            }


           out.println("<table class=\"table_text\" >\n"
                    + "    <tbody>\n"
                    + "        <tr>\n"
                    + "            <td colspan=\"2\"></td>\n"
                    + "        </tr>\n"
                    + "        <tr>\n"
                    + "            <td></td>\n"
                    + "            <td></td>            \n"
                    + "        </tr>\n"
                    + "        <tr>\n"
                    + "            <td></td>\n"
                    + "            <td></td>            \n"
                    + "        </tr>\n"
                    + "        <tr>\n"
                    + "            <td>\n"
                    + "                <ul>\n"
                    + "                    <li><a href=\"" + Constants.getServerName(c_con) + "logout\" >Login/Logout</a>   </li>\n"
                    + "                    <li><a href=\"" + Constants.getServerName(c_con) + "requestAccount\"> Request account</a></li>\n"
                    + "                    <li><a href=\"" + Constants.getServerName(c_con) + "Search/search\" >Search</a></li>\n"
                    + "                    <li> <a href=\"" + Constants.getServerName(c_con) + "Upload/logintodirectory\" >Create</a>  </li>\n"
                    + "                        <li> <a href=\"../webinterface_tutorial.pdf\" >Help</a>  </li>\n"
                    + "             \n"
                    + "                </ul> \n"
                    + "            </td>\n"
                    + "            <td>\n"
                    + "                <h2>Project description</h2>\n"
                    + "                <p>\n"
                    + "                    eGenVar data management system (EGDMS) is used for sharing information using a extended set of metadata, \n"
                    + "                    without compromising privacy or the security. This method could be used to create a catalogue \n"
                    + "                    of data for the biomedical research regardless of format, content or type and to describe them \n"
                    + "                    using a tagging process. This design is used to organise public data and to report the presence \n"
                    + "                    of private data making it possible to locate otherwise inaccessible information. \n"
                    + "                </p>\n"
                    + "                <p>\n"
                    + "                    The EGDMS is part of the eGenVar project, which focuses on handling, integrating and analysing anonymised \n"
                    + "                    Bio-Bank data. Still, the system has a flexible and extensible design to accommodate many other types of \n"
                    + "                    data used or generated in the biological and medical research environments. To capture the attributes of \n"
                    + "                    the data effectively, we have introducedthree extension to the basic definition of metadata. A software \n"
                    + "                    suite is available as a single installable package with all dependencies to accomplish this task. Much\n"
                    + "                    effort was taken to improve the active data collection process, where the system does much of the work \n"
                    + "                    in contrast to listing how to perform a certain task or producing large forms to be filled. Multiple user \n"
                    + "                    interfaces including a command line tool and a web portal addresses different use cases and preferences. \n"
                    + "                    All these effort was taken to encourage the use of the system so that the presence of data can be effectively\n"
                    + "                    reported. This would reduce redundancy in work, ambiguity of resources, ability to find complementing or \n"
                    + "                    opposing information and facilitates data integration.\n"
                    + "                </p>\n"
                    + "            </td>\n"
                    + "            <td></td>\n"
                    + "        </tr>\n"
                    + "        <tr>\n"
                    + "            <td></td>\n"
                    + "            <td><h2>Adding content</h2>\n"
                    + "                <ul>\n"
                    + "                    <li>\n"
                    + "                        Create an account. <a href=\"requestAccount\" > (Click here to request a new account)</a>.\n"
                    + "                    </li>\n"
                    + "                    <li>\n"
                    + "                        <a href=\"" + Constants.getServerName(c_con) + "Upload/CreateNew?current_tbl_nm=PERSON\" >Create a user profile</a>\n"
                    + "                    </li>\n"
                    + "                    <li>\n"
                    + "                        Use the <a href=\"Upload/logintodirectory\" >webinterace</a>  or the command line tool to add content.\n"
                    + "                    </li>\n"
                    + "                </ul>\n"
                    + "            </td>\n"
                    + "            <td></td>\n"
                    + "        </tr>\n"
                    + "    </tbody>\n"
                    + "</table>");
            out.println("</div>");
            out.println("</body>");
            out.println("</html>");

        } catch (SQLException ex) {
        } finally {
            out.close();
            close(c_con, null, null);
        }
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
