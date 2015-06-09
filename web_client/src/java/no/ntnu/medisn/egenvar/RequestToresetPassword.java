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
public class RequestToresetPassword extends HttpServlet {

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
        response.setContentType("text/html;charsetISO-8859-1");
        PrintWriter out = response.getWriter();
        Connection c_con = null;
        try {
            c_con = getDatasource().getConnection();
            /* TODO output your page here. You may use following sample code. */
            String user_email = "";
            if (request.getUserPrincipal() != null && request.getUserPrincipal().getName() != null) {
                user_email = request.getUserPrincipal().getName();
            }
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println(" <link HREF=\"resources/egenStyler.css\"  REL=\"stylesheet\" TYPE=\"text/css\" >");
            out.println("<title>GenVar Datamangement System</title>");
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
            out.println("<a href=\"" + Constants.getServerName(c_con) + "\" >Home</a>");
            out.println("<h1>Reset password</h1>");
            out.println(""
                    + "<form accept-charset=\"ISO-8859-1\"  action=\"ResetPassword\" >\n"
                    + "            <table class=\"table3\">\n"
                    + "                <thead>\n"
                    + "                    <tr>\n"
                    + "                        <th>Field</th>\n"
                    + "                        <th>Value</th>\n"
                    + "                    </tr>\n"
                    + "                </thead>\n"
                    + "                <tbody>\n"
                    + "                    <tr>\n"
                    + "                        <td>Email</td>\n"
                    + "                        <td>\n"
                    + "                            <input type=\"text\" name=\"email\"  value=\"" + user_email + "\" size=\"20\" >\n"
                    + "                        </td>\n"
                    + "                    </tr>\n"
                    + "                    <tr>\n"
                    + "                        <td></td>\n"
                    + "                        <td><input type=\"submit\" value=\"Send request\"></td>\n"
                    + "                    </tr>\n"
                    + "                </tbody>\n"
                    + "            </table>\n"
                    + "        </form>");
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
