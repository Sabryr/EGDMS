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
public class Login extends HttpServlet {

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
            HttpSession ses = request.getSession(true);

            Object current_user = ses.getAttribute("current_user");
            ses.removeAttribute("ATRB_RELOAD");
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println(" <link HREF=\"" + Constants.getServerName(c_con) + "resources/egenStyler.css\"  REL=\"stylesheet\" TYPE=\"text/css\" >");
            out.println("<title>eGenVar Datamangement System</title>");
            out.println("</head>");
            out.println("<body>");

            out.println(Constants.getPrivateMenue(c_con, true));

            out.println("<h1>Login to eGenVar data-management system</h1>");


            out.println("<div align=\"center\">");
//            if (current_user == null) {
//                out.println("<h2>You are not logged in</h2>");
//            }
            out.println(""
                    + "<form accept-charset=\"ISO-8859-1\"  action=\"j_security_check\" method=post>\n"
                    + " <table class=\"table_login\">\n"
                    + "     <tbody>\n"
                    + "         <tr>\n"
                    + "             <th>Username:</th>\n"
                    + "             <th><input type=\"text\" name=\"j_username\"></th>\n"
                    + "         </tr>\n"
                    + "         <tr>\n"
                    + "             <th>Password:</th>\n"
                    + "             <th><input type=\"password\" name=\"j_password\"></th>\n"
                    + "         </tr>\n"
                    + "         <tr>\n"
                    + "             <th></th>\n"
                    + "             <th> <input type=\"submit\" value=\"Login\"></th>\n"
                    + "         </tr>\n"
                    + "         <tr>\n"
                    + "             <td></td>\n"
                    + "             <td> "
                    + "         <p>\n"
                    + "             <boldx>Lost password password, get new  <a href=\"" + Constants.getServerName(c_con) + "RequestToresetPassword\" >here!</a></boldx>\n"
                    + "         </p> \n"
                    + "         <p>\n"
                    + "             <boldx>Request new account  <a href=\"" + Constants.getServerName(c_con) + "requestAccount\" >here!</a></boldx>\n"
                    + "         </p>\n"
                    + "         <p>\n"
                    + "             <boldx>Back to main page  <a href=\"" + Constants.getServerName(c_con) + "index\" >here!</a></boldx>\n"
                    + "         </p>"
                    + "         </td>\n"
                    + "         </tr>\n"
                    + "         <tr>\n"
                    + "             <td></td>\n"
                    + "         </tr>\n"
                    + "   </tbody>\n"
                    + " </table>         \n"
                    + "</form>");
            out.println("</div>");

//            out.println(""
//                    + "<p>\n"
//                    + " <boldx>Lost password password, get new  <a href=\"" + Constants.getServerName() + "RequestToresetPassword\" >here!</a></boldx>\n"
//                    + "</p> \n"
//                    + "<p>\n"
//                    + " <boldx>Request new account  <a href=\"" + Constants.getServerName() + "requestAccount\" >here!</a></boldx>\n"
//                    + "</p>\n"
//                    + "<p>\n"
//                    + " <boldx>Back to main page  <a href=\"" + Constants.getServerName() + "index\" >here!</a></boldx>\n"
//                    + "</p>"
//                    + "");

            out.println("</body>");
            out.println("</html>");
            c_con.close();
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
