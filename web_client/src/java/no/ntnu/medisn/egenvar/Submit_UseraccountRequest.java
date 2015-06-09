package no.ntnu.medisn.egenvar;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
import java.io.IOException;
import java.io.PrintWriter;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Enumeration;
import java.util.Formatter;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
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
public class Submit_UseraccountRequest extends HttpServlet {

//    private String code = null;
//    private boolean code_sent = false;
//    private final static String DATASOURCE_NAME = "egen_userManagement_resource";
//    private final String DATABASE_NAME = "egen_users";
//    private final String USER_TABLE = DATABASE_NAME + ".useraccounts";
//    private final String SMS_TABLE = DATABASE_NAME + ".sms";
//    private final String INSERT_USER_PROCEDURE = DATABASE_NAME + ".INSERT_USER";
//        private String debug="";
    //    private final static String FTP_SITE_PATTERN = "(ftp://[^/]+)(.*)";
    private DataSource dataSource_usermange;
    private DataSource dataSource;
    private static final int MAX_ATEMPTS = 3;

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
    /*
     MethodID=SUR1
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String method = request.getMethod();
        response.setContentType("text/html;charset=ISO-8859-1");
        PrintWriter out = response.getWriter();
        Connection c_con = null;
        Connection c_con_usermange = null;
        try {
            c_con = getDatasource().getConnection();
            c_con_usermange = getDatasource_userManagement().getConnection();
            HttpSession session = request.getSession(true);
            boolean delete_user = false;
            boolean delete_user_confirmed = false;
            if (request.getParameter("delete_user") != null) {
                if (request.getParameter("delete_user").equalsIgnoreCase("true")) {
                    delete_user = true;
                    if (request.getParameter("delete_user_confirmed") != null) {
                        if (request.getParameter("delete_user_confirmed").equalsIgnoreCase("true")) {
                            delete_user_confirmed = true;
                        }
                    }
                }
            }
            int attempts = 0;
            boolean maxAtemptsExceeded = false;
            if (request.getParameter("reset") != null && ((String) request.getParameter("reset")).equalsIgnoreCase("true")) {
                session.removeAttribute("code_sent");
                session.removeAttribute("code");
                attempts = 0;
            } else {
                if (session.getAttribute("attempts") != null) {
                    attempts = (Integer) session.getAttribute("attempts") + 1;
                    if (MAX_ATEMPTS < attempts) {
                        attempts = 0;
                        maxAtemptsExceeded = true;
                    }
                }
            }
            session.setAttribute("attempts", attempts);
            String code = null;
            if (session.getAttribute("code") != null) {
                code = (String) session.getAttribute("code");
            }
            if (delete_user) {
                out.println("<!DOCTYPE html>");
                out.println("<html>");
                out.println("<head>");
                out.println("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\">");
                out.println("<link HREF=\"" + Constants.getServerName(c_con) + "resources/egenStyler.css\"  REL=\"stylesheet\" TYPE=\"text/css\" >");
                out.println("<title>*Delete account</title>");
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
                if (session.getAttribute("Header") != null) {
                    out.println("<p>" + session.getAttribute("Header") + "</p>");
                } else {
                    out.println("<a href=\"" + Constants.getServerName(c_con) + "\" >Home</a>");
                }
                out.println("<h2>Delete account</h2>");
                String user_name = null;
                if (session.getAttribute("current_user") != null) {
                    user_name = (String) session.getAttribute("current_user");
                    if (delete_user_confirmed) {
                        String code_sent = null;
                        if (session.getAttribute("code_sent") != null) {
                            code_sent = (String) session.getAttribute("code_sent");
                        }
                        if (code == null || code_sent == null || !code.equals(code_sent)) {
                            out.println("<warning>Invalid code</warning>");
                        } else {
                            if (deleteUser(c_con_usermange, user_name)) {
                                String redirectURL = Constants.getServerName(c_con) + "logout";
                                response.sendRedirect(redirectURL);
                            } else {
                                out.println("<warning>Deleting account failed</warning>");
                            }
                        }
                    } else {
                        String code_sent = randomstring_num();
                        session.setAttribute("code_sent", code_sent);
                        Mailer mailer = new Mailer();
                        mailer.sendUserCodeConfirmation_delete(c_con, user_name, user_name, code_sent, request);
                        out.println("<warning>Do you want to delete the account " + user_name + " ? </warning>");
                        out.println("<form accept-charset=\"ISO-8859-1\"  method=\"POST\">");
                        out.println("<table border=\"0\">");
                        out.println("<thead>");
                        out.println("<tr>");
                        out.println("<th>Entrer the security code sent to " + user_name + " <input type=\"text\" name=\"code\" value=\"\" /></th>");
                        out.println("</tr>");
                        out.println("<tr>");
                        out.println("<th colspan=\"2\"><input type=\"submit\" name=\"Confirmed\" value=\"Delete and logout\" /></th>");
                        out.println("</tr>");
                        out.println("</thead>");
//                    out.println("Entrer the security code sent to " + user_name + " ");
                        out.println("<input type=\"hidden\" name=\"delete_user\" value=\"true\" />");
                        out.println("<input type=\"hidden\" name=\"delete_user_confirmed\" value=\"true\" />");
//                    out.println("<input type=\"submit\" name=\"Confirmed\" value=\"Delete and logout\" />");
                        out.println("</form>");
                    }
                } else {
                    out.println("<error>Error locating user name</error>");
                }
                out.println("</body>");
                out.println("</html>");
            } else {
                Mailer mailer = new Mailer();
                out.println("<!DOCTYPE html>");
                out.println("<html>");
                out.println("<head>");
                out.println("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\">");
                out.println("<link HREF=\"" + Constants.getServerName(c_con) + "resources/egenStyler.css\"  REL=\"stylesheet\" TYPE=\"text/css\" >");
                out.println("<title>Servlet Submit_UseraccountRequest</title>");
                out.println("</head>");
                out.println("<body>");
                out.println("<script language = JavaScript>");
                out.println(" var count=0;");
                out.println("function setValue(target, newvalue){");
                out.println("if(document.getElementById(target)!=null){");
                out.println(" document.getElementById(target).value=newvalue;");
                out.println(" }");
                out.println("  }");
                out.println(" function setProgress(target, value){");
                out.println("   if(document.getElementById(target)!=null){");
                out.println("       document.getElementById(target).style.visibility = value; ");
                out.println("   }");
                out.println("   }");
                out.println("  </script>");
//                out.println("<p>" + session.getAttribute("Header") + "</p>");
                out.println("<a href=\"" + Constants.getServerName(c_con) + "\" >Home</a>");
                out.println("<h2>Account request</h2>");
                out.println("<form accept-charset=\"ISO-8859-1\"  method=\"POST\">");
                if (code == null) {
                    code = randomstring_num();
                    session.setAttribute("code", encript(code));
                }
                String code_sent = null;
                if (session.getAttribute("code_sent") != null) {
                    code_sent = (String) session.getAttribute("code_sent");
                }
                String user_name = request.getParameter("email");
                String mobile = request.getParameter("mobile");
                String email = request.getParameter("email");
                String user_ip = request.getParameter("userip");
//            String firstnm = request.getParameter("firstnm");
//            String lastnm = request.getParameter("lastnm");
                String user = email;//firstnm + " " + lastnm;
                String entered_code = request.getParameter("code");

                String groups = request.getParameter("groups");
                System.out.println("220 groups="+groups);
//                System.out.println("219 email="+email);
                if (email == null || email.isEmpty() || !email.matches("[A-z0-9_\\-\\.]+@[A-z0-9_\\-]+[\\.]{1}[A-z0-9_\\-\\.]+")) {
                    out.println("<h4>Invalid email please try again </h4>");
//                } 
//                else if (mobile == null || mobile.isEmpty() || !mobile.matches("\\+[0-9]+")) {
//                    out.println("<h4>Invalid mobile number (mobile number required only when using SMS based authentication) </h4>");
                } else {
                    if (user_name == null || user_name.isEmpty()) {
                        out.println("<h3>Invalid usernmae please try again </h3>");
                    } else {
                        boolean smsent = false;
                        if (code_sent == null || !code_sent.equalsIgnoreCase("true")) {
                            groups = "";
                            Enumeration parameters_e = request.getParameterNames();
                            while (parameters_e.hasMoreElements()) {
                                String c_prm = parameters_e.nextElement().toString();
                                if (request.getParameter(c_prm) != null && request.getParameter(c_prm).equalsIgnoreCase("ON")) {
                                    if (!groups.isEmpty()) {
                                        groups = groups + ",";
                                    }
                                    groups = groups + c_prm;
                                }
                            }
                            boolean userFound = userNameFound(c_con_usermange, user_name);
                            if (!userFound) {
                                String[] auth_a = getSMScredentials(c_con_usermange);

                                if (auth_a != null && auth_a.length == 2 && auth_a[0] != null && !auth_a[0].isEmpty() && auth_a[1] != null && !auth_a[1].isEmpty() && !auth_a[1].equals("0")) {
                                    smsent = mailer.sendSMS(email, getSMScredentials(c_con_usermange), mobile, code);
                                }
                                if (!smsent) {
                                    mailer.sendUserCodeConfirmation(c_con, email, user, code, request);
                                }
                                code_sent = "true";
                                session.setAttribute("code_sent", code_sent);
                            } else {
                                out.println("<p><error>  The user name " + user_name + " is already in use. <a href=\""
                                        + Constants.getServerName(c_con) + "requestAccount?email=" + email + "&mobile=" + mobile + "\" >Click here to try again<a/></error></p> ");
                                out.println("<p><a href=\"" + Constants.getServerName(c_con) + "RequestToresetPassword.jsp?email=" + email
                                        + "\">Click here reset the password  for " + email + "<a/></p> ");

                            }
                        }
                        out.println("<table border=\"0\">");
                        out.println("<thead>");
                        out.println("<tr>");
                        out.println("<th>Code confirmation for </th><th>" + user_name + "</th>");
                        out.println("</tr>");
                        out.println("</thead>");
                        out.println("<tbody>");
                        out.println("<tr>");
                        out.println("<td>user_name:</td><td> <input type=\"text\" value=\"" + user_name + "\" name=\"user_name\"  readonly=\"readonly\" size=\"35\"> </td>");
                        out.println("</tr>");
                        out.println("<tr>");
                        out.println("<tr>");
                        out.println("<td>Mobile numbers:    </td><td><input type=\"text\" value=\"" + mobile + "\" name=\"mobile\" readonly=\"readonly\" size=\"35\"> </td>");
                        out.println("</tr>");
                        out.println("<tr>");
                        out.println("<td>email:    </td><td><input type=\"text\" value=\"" + email + "\" name=\"email\" readonly=\"readonly\" size=\"35\"> </td>");
                        out.println("</tr>");
                        out.println("<tr>");
                        out.println("<td>user_ip address: </td><td><input type=\"text\" value=\"" + user_ip + "\" name=\"userip\" readonly=\"readonly\" size=\"35\"> </td>");
                        out.println("</tr>");
                        out.println("<tr>");
                        out.println("<td> Groups: </td><td><input type=\"text\" value=\"" + groups + "\" name=\"groups\" readonly=\"readonly\" size=\"35\"> </td>");
                        out.println("</tr>");
                        out.println("<tr>");
                        if (!maxAtemptsExceeded) {
                            out.println("<td><div id=\"div4\"> Enter confirmation code: </div></td><td><div id=\"div5\"><input type=\"text\" name=\"code\" size=\"20\" autofocus=\"true\" pattern=\"[0-9]+\" required=\"true\" title=\"Only numbers allowed\" > </div><div id=\"div\">Please wait .. <img src=\"progress.gif\" width=\"200\" height=\"10\" alt=\"progress\"  id=\"progressanim\"/></div></td>");
                        }
                        out.println("</tr>");
                        out.println("<tr>");
                        if (smsent) {
                            out.println("<td  colspan=\"2\"><div id=\"div2\">(A confirmation code was sent to the mobile number " + mobile + ")</div></td>");
                        } else {
//                            out.println("<td  colspan=\"2\"><div id=\"div3\">(SMS setup fialed, so the confirmation code was sent to the email " + email + ", please inform error code SUR1a to administratior)</div></td>");
                        }
                        out.println("</tr>");
                        out.println("<tr>");
                        if (!maxAtemptsExceeded) {
                            out.println("<td colspan=\"2\"><div id=\"div1\"> <input type=\"submit\" value=\"Submit\"  onclick='setProgress(\"div\",\"visible\")'></div></td>");
                        }
                        out.println("</tr>");
                        out.println("</tbody>");
                        out.println("</table>");
                    }
                }
                /*
                 * TODO output your page here. You may use following sample code.
                 */
                out.println("</form>");
                if (!maxAtemptsExceeded) {
                    if (entered_code != null && code_sent != null && code_sent.equalsIgnoreCase("true")) {
                        String pass = randomstring();
                        if (code.equals(encript(entered_code))) {
                            String result = "NA";
//                            Connection ncon = null;
                            try {
//                                getDatasource();
//                                ncon = dataSource.getConnection();                               
                                if (!c_con_usermange.isClosed()) {
                                    String simpleProc = "{call " + Constants.INSERT_USER_PROCEDURE + "(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}";
                                    CallableStatement cs = c_con_usermange.prepareCall(simpleProc);
                                    cs.setString(1, Constants.DATABASE_USERS + ".spliter_SplitValues");
                                    cs.setString(2, Constants.DATABASE_USERS + ".useraccounts");
                                    cs.setString(3, Constants.DATABASE_USERS + ".groupnmtoGID");
                                    cs.setString(4, Constants.DATABASE_USERS + ".groups");
                                    cs.setString(5, email);
                                    cs.setString(6, email);
                                    cs.setString(7, email);
                                    cs.setString(8, pass);
                                    cs.setString(9, ",");
                                    cs.setString(10, groups);
                                    cs.setInt(11, 1);
                                    cs.setInt(12, 9);
                                    cs.setString(13, user_ip);
                                    cs.setString(14, mobile);
                                    cs.setString(15, Constants.DATABASE_USERS + ".sha1");
                                    cs.registerOutParameter(16, java.sql.Types.VARCHAR);
                                    cs.execute();
                                    result = cs.getString(16);
                                    out.println("<p> Groups(" + groups + ") </p> ");
                                    out.println("<p> result " + result + " </p> ");
                                    out.println("<p> <success>Code accepted</success></p> ");
                                    if (!result.equalsIgnoreCase("OK")) {
                                        out.println("<p><error>  Sorry!, there was an error while creating the user"
                                                + " account. ERROR:</error></p><p> " + result + ". Please inform this "
                                                + "error to the administrator</p> ");
                                        out.println("<p><a href=\"" + Constants.getServerName(c_con) + "RequestToresetPassword.jsp?email=" + email + "\">Click here reset the password  for " + email + "</a></p> ");
                                        out.println("<script type=\"text/javascript\">");
                                        out.println("setProgress(\"div1\",\"hidden\");");
                                        out.println("setProgress(\"div2\",\"hidden\");");
                                        out.println("setProgress(\"div3\",\"hidden\");");
                                        out.println("setProgress(\"div4\",\"hidden\");");
                                        out.println("setProgress(\"div5\",\"hidden\");");
                                        out.println("</script>");
                                    } else {
                                        out.println("<script type=\"text/javascript\">");
                                        out.println("setProgress(\"div1\",\"hidden\");");
                                        out.println("setProgress(\"div2\",\"hidden\");");
                                        out.println("setProgress(\"div3\",\"hidden\");");
                                        out.println("setProgress(\"div4\",\"hidden\");");
                                        out.println("setProgress(\"div5\",\"hidden\");");
                                        out.println("</script>");
                                        mailer.sendUserPassword(c_con, email, user, email, pass, request);
                                        mailer.informAdmin("New user","New user created: "+email);
                                        if (create_userprofile(c_con, email) > 0) {
                                            out.println("<p> Temporary user profile created ");
                                        } else {
                                            out.println("<p> Temporary user profile creation failed");
                                        }
                                        //create a basic user profile
                                        out.println("<p> Email was sent to " + email + " with your password</p> ");
                                    }
                                    cs.close();
//                                    if (!ncon.isClosed()) {
//                                        ncon.close();
//                                    }
                                }
                            } catch (SQLException ex) {
ex.printStackTrace();
                                out.println("<p><error>  Sorry!, there was an error while creating the user account. ERROR: " + ex.getMessage() + "."
                                        + " Please inform this error to the administrator </error></p> ");
                            }
//                            try {
//                                if (ncon != null && !ncon.isClosed()) {
//                                    ncon.close();
//                                }
//                            } catch (SQLException ex) {
//                            }

                            out.println("<p> Click <a href=\"index\" >here</a> to continue</p> ");
                            code = null;

                        } else {
                            out.println("<p> <error> The code " + entered_code + " is invalid.</error> </p> ");
                        }
                    } else {
//                 out.println("<p> <error>Error 23454 </error> </p> "); 
                    }
                } else {
                    out.println("<p> <error> maximum allowed number of attempts exceeded.</error> <a href=\"" + Constants.getServerName(c_con)
                            + "\">Click here to go the main page</a> </p> ");
                }

                out.println("<script type=\"text/javascript\">");
                out.println("setProgress(\"div\",\"hidden\");");
                out.println("</script>");
                out.println("</body>");
                out.println("</html>");
            }

            close(c_con, null, null);
            close(c_con_usermange, null, null);
        } catch (SQLException ex) {
        } finally {
            out.close();
            close(c_con_usermange, null, null);
            close(c_con, null, null);
        }
    }

    private boolean userNameFound(Connection c_con_usermang, String usernm) {
        boolean result = false;
        try {
//            if (dataSource != null) {
//                Connection ncon = dataSource.getConnection();
            if (!c_con_usermang.isClosed()) {
                String sql = "select 1 from " + Constants.DATABASE_USERS + " where username='" + usernm + "'";
                ResultSet r_1 = c_con_usermang.createStatement().executeQuery(sql);
                while (r_1.next()) {
                    result = true;
                }
            }
//                if (!ncon.isClosed()) {
//                    ncon.close();
//                }
//            }

        } catch (SQLException ex) {
        }

        return result;
    }

    private boolean deleteUser(Connection c_con_usermang, String usernm) {
        boolean result = false;
        try {
//            getDatasource();
//            if (dataSource != null) {
//                Connection ncon = dataSource.getConnection();
            if (!c_con_usermang.isClosed()) {
                String sql = "DELETE from " + Constants.DATABASE_USERS + " where username='" + usernm + "'";
                if (c_con_usermang.createStatement().executeUpdate(sql) >= 0) {
                    result = true;
                }
            }
//                if (!ncon.isClosed()) {
//                    ncon.close();
//                }
//            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return result;
    }

    private int create_userprofile(Connection c_con, String email) {
        int result = -1;
        try {
            String insert_stmt = "insert into " + Constants.get_correct_table_name(c_con, "person") + "(name, lastnm, email) values('NA','NA','" + email + "')";
            result = c_con.createStatement().executeUpdate(insert_stmt);
            HashMap<String, String> config_map = new HashMap<>();
            config_map.put("MEMORIZING_REQUESTED", "1");
            if (Constants.setConfig(c_con, config_map) < 0) {
            }
        } catch (SQLException ex) {
        }
        return result;
    }

    private String[] getSMScredentials(Connection c_con_usermang) {
        String[] result = new String[2];
        result[0] = "0";
        result[1] = "0";
        try {

//            if (dataSource != null) {
//                Connection ncon = dataSource.getConnection();
            if (!c_con_usermang.isClosed()) {
                String sql = "select username, password from " + Constants.SMS_TABLE + "";
                ResultSet r_1 = c_con_usermang.createStatement().executeQuery(sql);
                if (r_1.next()) {
                    result[0] = r_1.getString("username");
                    result[1] = r_1.getString("password");
                }
            }
//                if (!ncon.isClosed()) {
//                    ncon.close();
//                }
//            }

        } catch (SQLException ex) {
//                debug=debug+"\n"+ex.getMessage()+"";            
        }
        return result;
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
//        System.out.println(cval);
        return cval;
    }

    public String encript(String intext) {
        try {
            MessageDigest sha1 = MessageDigest.getInstance("SHA1");
            byte[] pass_bytes = intext.trim().getBytes();
            sha1.reset();
            sha1.update(pass_bytes);
            byte[] pass_digest = sha1.digest();
            Formatter formatter = new Formatter();
            for (byte b : pass_digest) {
                formatter.format("%02x", b);
            }
            String encript_pass = formatter.toString();
            return encript_pass;
        } catch (NoSuchAlgorithmException ex) {
        }
        return null;
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

    private DataSource getDatasource_userManagement() throws ServletException {
        if (dataSource_usermange == null) {
            try {
                Context env = (Context) new InitialContext().lookup("java:comp/env");
                dataSource_usermange = (DataSource) env.lookup(Constants.DATASOURCE_NAME_USERMANAGEMENT_RESOURCE);
                if (dataSource_usermange == null) {
                    throw new ServletException("`" + Constants.DATASOURCE_NAME_USERMANAGEMENT_RESOURCE + "' is an unknown DataSource");
                }
            } catch (NamingException e) {
                throw new ServletException(e);
            }
        }
        return dataSource_usermange;
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
