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
public class RequestAccount extends HttpServlet {

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
            out.println(" <link HREF=\"resources/egenStyler.css\"  REL=\"stylesheet\" TYPE=\"text/css\" >");
            out.println("<title>GenVar Datamangement System</title>");
            out.println(" <script language = JavaScript>\n"
                    + "            var count = 0;\n"
                    + "            function setValue(target, newvalue) {\n"
                    + "                if (document.getElementById(target) != null) {\n"
                    + "                    document.getElementById(target).value = newvalue;\n"
                    + "                }\n"
                    + "            }\n"
                    + "            function setProgress(target, value) {\n"
                    + "                if (document.getElementById(target) != null) {\n"
                    + "                    document.getElementById(target).style.visibility = value;\n"
                    + "                }\n"
                    + "            }\n"
                    + "        </script>");
            out.println("</head>");
            out.println("<body>");
            out.println("<table  class=\"table_text\" >\n"
                    + " <thead>\n"
                    + "     <tr>\n"
                    + "         <th>\n"
                    + "                <img  height=\"50%\" width=\"100%\" src=\"Banner.gif\"/>\n"
                    + "         </th>\n"
                    + "     </tr>\n"
                    + " </thead>\n"
                    + "</table>\n");
            out.println("<h1>New account request</h1>");
            HttpSession ses = request.getSession(true);
            if (ses.getAttribute("Header") != null) {
                out.println("<p>" + ses.getAttribute("Header") + "</p>");
            } else {
                out.println("<a href=\"" + Constants.getServerName(c_con) + "\" >Home</a>");
            }

            out.println(""
                    + "<form accept-charset=\"ISO-8859-1\"  action=\"Submit_UseraccountRequest\" method=\"post\" >\n"
                    + " <table border=\"0\">\n"
                    + "     <thead>\n"
                    + "         <tr>\n"
                    + "             <th>Field</th>\n"
                    + "             <th colspan=\"2\">Value</th>                    \n"
                    + "        </tr>\n"
                    + "     </thead>\n"
                    + "     <tbody>\n"
                    + "         <tr>\n"
                    + "             <td>Email</td>\n"
                    + "             <td>\n");
            if (ses.getAttribute("email") != null) {
                out.println("<input type=\"text\"  name=\"email\" value=\"" + ses.getAttribute("email") + "\" size=\"20\" pattern=\"[A-z0-9_\\\\-\\\\.]+@[A-z0-9_\\\\-]+[\\\\.]{1}[A-z0-9_\\\\-\\\\.]+\" required=\"true\">");
            } else {
                out.println("<input type=\"text\" name=\"email\" value=\"\" size=\"20\" pattern=\"[A-z0-9_\\\\-\\\\.]+@[A-z0-9_\\\\-]+[\\\\.]{1}[A-z0-9_\\\\-\\\\.]+\"  required=\"true\">");
            }
            out.println("           "
                    + " This will be your user name</td>\n"
//                    + "             <td> This will be your user name</td>\n"
//                    + "         </tr>\n"
                    + "         <tr>\n");
//                    + "             <td>Mobile phone number</td>\n"
//                    + "             <td>\n");
//            if (ses.getAttribute("mobile") != null) {
//                out.println("<input type=\"text\"  value=\"" + ses.getAttribute("mobile") + "\" name=\"mobile\" size=\"20\" pattern=\"\\+[0-9]+\" required=\"true\" title=\"Mobile number international format, start with the + then country code then number\">");
//            } else {
//                out.println("<input type=\"text\" name=\"mobile\" value=\"\" size=\"20\" pattern=\"\\+[0-9]+\"  title=\"Mobile number international format, start with the + then country code then number\">");
//            }
            out.println("          "
//                    + " </td>\n"
//                    + "             <td>Enter mobile number in international format.  e.g. +4712345678,  +47 is the country code for Norway.</td>\n"
                    + "          </tr>\n"
                    + "          <tr>\n"
                    + "             <td>Your IP address</td>\n"
                    + "             <td colspan=\"2\"><input type=\"text\" name=\"userip\" readonly=\"readonly\" value=\"" + request.getRemoteAddr() + "\" size=\"20\">  Automatically collected for security reasons and can not be edited</td>\n"
                
                    + "          </tr>\n"
                    + "          <tr>\n"
                    + "             <td>Select Groups</td>\n"
                    + "             <td colspan=\"2\">\n"
                    + "              <p><input type=\"checkbox\" name=\"Search\" value=\"ON\" checked=\"true\"/> Search (permission to search meta-data and files)</p>\n"
                    + "              <p><input type=\"checkbox\" name=\"Uploader\" value=\"ON\" /> Upload (permission to upload records)*</p>\n"
                    + "              <p><input type=\"checkbox\" name=\"Editor\" value=\"ON\" /> Edit (permission to edit records)*</p>\n"
                    + "              <p><input type=\"checkbox\" name=\"Deletor\" value=\"ON\" /> Delete (permission to remove records)*</p>\n"
                    + "              <p>*Active only after approved</p>\n"
                    + "            <td>\n"
                    + "          </tr>\n"
                    + "          <tr>\n"
                    + "            <td colspan=\"2\"><div id=\"div\">Please wait .. <img src=\"progress.gif\" width=\"200\" height=\"10\" alt=\"progress\"  id=\"progressanim\"/></div></td>\n"
                    + "            <td><a href=\"${servernmr}\">Cancel </a> <input type=\"hidden\" value=\"true\" name=\"reset\" size=\"30\"><input type=\"submit\" value=\"Send request\" size=\"30\" onclick='setProgress(\"div\", \"visible\");'></td>\n"
                    + "          </tr>\n"
                    + "      </tbody>\n"
                    + " </table>\n"
                    + "</form>");

            out.println("   <script type=\"text/javascript\">\n"
                    + "            setProgress(\"div\", \"hidden\");\n"
                    + "    </script>");
            out.println("</body>");
            out.println("</html>");
            close(c_con, null, null);
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
