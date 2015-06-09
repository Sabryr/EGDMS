package no.ntnu.medisn.egenvar;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.CallableStatement;
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
public class ResetPassword extends HttpServlet {

    private final static String DATASOURCE_NAME = "egen_userManagement_resource"; //egen_userManagement_pool
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
        String method = request.getMethod();
        response.setContentType("text/html;charset=ISO-8859-1");
        PrintWriter out = response.getWriter();
        HttpSession session = request.getSession(true);

        Mailer mailer = new Mailer();
        if (method.equalsIgnoreCase("GET")) {
            session.removeAttribute("code_sent");
            session.removeAttribute("code");
        }
        Connection c_con = null;
        try {
            c_con = getDatasource().getConnection();
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\">");
            out.println("<link HREF=\"" + Constants.getServerName(c_con) + "resources/egenStyler.css\"  REL=\"stylesheet\" TYPE=\"text/css\" >");
            out.println("<title>Servlet Submit_UseraccountRequest</title>");
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
            if (session.getAttribute("Header") != null) {
                out.println("<p>" + session.getAttribute("Header") + "</p>");
            } else {
                out.println("<a href=\"" + Constants.getServerName(c_con) + "\" >Home</a>");
            }
            out.println("<h2>Account request</h2>");
            out.println("<form accept-charset=\"ISO-8859-1\"  method=\"POST\">");
            String code = null;
            if (session.getAttribute("code") != null) {
                code = (String) session.getAttribute("code");
            }
            if (code == null) {
                code = randomstring_num();
                session.setAttribute("code", code);
            }
            String code_sent = null;
            if (session.getAttribute("code_sent") != null) {
                code_sent = (String) session.getAttribute("code_sent");
            }


            String email = request.getParameter("email");
            String entered_code = request.getParameter("entered_code");
            if (email == null || email.isEmpty() || !email.matches("[A-z0-9_\\-\\.]+@[A-z0-9_\\-\\.]+")) {
                out.println("<h3>Invalid email please try again </h3>");
            } else {
                if (code_sent == null || !code_sent.equalsIgnoreCase("true")) {
                    mailer.sendUserCodeConfirmation(c_con,email, email, code, request);
                    code_sent = "true";
                    session.setAttribute("code_sent", code_sent);
                    out.println("<p><input type=\"text\" name=\"email\" value=\"" + email + "\"></p>");
                    out.println("<p> Enter confirmation code <input type=\"text\" name=\"entered_code\" size=\"20\"> </p> ");
                    out.println("<p> (A confirmation code was sent to the email address " + email + ")</p> ");
                    out.println("<input type=\"submit\" value=\"submit\">");
                }
            }
            out.println("</form>");
            if (entered_code != null) {
                String pass = randomstring();
                if (code.equals(entered_code.trim())) {
                    session.removeAttribute("code_sent");
                    session.removeAttribute("code");
                    String result = "NA";
                    try {
                        getDatasource();
                        Connection ncon = dataSource.getConnection();
                        System.out.println("106 " + email);
                        if (!ncon.isClosed()) {
                            String simpleProc = "{call " + Constants.RESET_USER_PROCEDURE + "(?,?,?,?)}";
                            CallableStatement cs = ncon.prepareCall(simpleProc);
                            cs.setString(1, Constants.DATABASE_USERS + ".useraccounts");
                            cs.setString(2, email);
                            cs.setString(3, pass);
                            cs.registerOutParameter(4, java.sql.Types.VARCHAR);
                            cs.execute();
                            result = cs.getString(4);
                            out.println("<p> <font color='green'>Code accepted</font></p> ");
                            if (!result.equalsIgnoreCase("OK")) {
                                out.println("<p><font color='red'>  Sorry!, there was an error while creating the user account. ERROR:</p><p> " + result + ". Please inform this error to the administrator</font></p> ");
                            } else {
                                mailer.sendUserPassword(c_con,email, email, email, pass, request);
                                out.println("<p> Email was sent to " + email + " with your new password</p> ");
                            }
                        }

                    } catch (SQLException ex) {
                        out.println("<p><font color='red'>  Sorry!, there was an error while creating the user account. ERROR: " + ex.getMessage() + ". Please inform this error to the administrator </font></p> ");
                    }
                    out.println("<p> Click <a href=\"index\" >here</a> to continue</p> ");
                    code = null;

                } else {
                    out.println("<p> <font color='red'> The code " + code + " is invalid.</font> </p> ");
                }
            }
            out.println("</body>");
            out.println("</html>");
            close(c_con, null, null);
        } catch (SQLException ex) {
        } finally {
            out.close();
            close(c_con, null, null);
        }
    }

    private String randomstring() {
        int n = 8;
        byte[] b = new byte[n];
        for (int i = 0; i < n; i++) {
            int c_val = rand('A', 'z');
            int c_val2 = rand('0', '9');
            if (c_val % 2 == 0) {
                b[i] = (byte) c_val2;
            } else {
                b[i] = (byte) c_val;
            }

        }
        String pass = new String(b);


        return pass;
    }

    private String randomstring_num() {
        int n = 6;
        byte[] b = new byte[n];
        for (int i = 0; i < n; i++) {
            int c_val2 = rand('0', '9');
            b[i] = (byte) c_val2;

        }
        String pass = new String(b);


        return pass;
    }

    private int rand(int lo, int hi) {
        java.util.Random rn = new java.util.Random();
        int cval = 0;
        int n = hi - lo + 1;
        int i = rn.nextInt(n);
        if (i < 0) {
            i = -i;
        }
        cval = lo + i;
//        if (cval == 96 || cval == 59|| cval == 63) {
//            rand(lo, hi);
//        }

        return cval;
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
