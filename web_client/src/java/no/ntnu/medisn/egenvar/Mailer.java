package no.ntnu.medisn.egenvar;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
import com.nexmo.messaging.sdk.NexmoSmsClient;
import com.nexmo.messaging.sdk.NexmoSmsClientSSL;
import com.nexmo.messaging.sdk.SmsSubmissionResult;
import com.nexmo.messaging.sdk.messages.TextMessage;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;

/**
 * Todo: implement USSD messaging
 *
 * @author sabryr
 */
@WebServlet(name = "Mailer", urlPatterns = {"/Mailer"})
public class Mailer extends HttpServlet {

    private DataSource dataSource;
    private Session mail_session;
    private final static String DATASOURCE_NAME = "mail/egenvar";
    private String admin_email;
//    private String lastmobile = "";

    /**
     * Processes requests for both HTTP
     * <code>GET</code> and
     * <code>POST</code> methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     * ${pageContext.request.localPort}
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=ISO-8859-1");
        PrintWriter out = response.getWriter();
        Connection c_con = null;
        try {
            c_con = getDatasource().getConnection();
            String email = request.getParameter("email");
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\">");
            out.println("<link HREF=\"" + Constants.getServerName(c_con) + "resources/egenStyler.css\"  REL=\"stylesheet\" TYPE=\"text/css\" >");
            out.println("<title>Servlet Mailer</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>eGenVar mail managment</h1>");
            out.println("<p> Your IP: " + request.getRemoteAddr() + "</p> ");
            out.println("<p> Emil " + email + "</p> ");
            send("From savelet ", email, email, "Test message from savelet");
            out.println("</body>");
            out.println("</html>");
            close(c_con, null, null);
        } catch (SQLException ex) {
        } catch (Exception ex) {
        } finally {
            out.close();
            close(c_con, null, null);
        }
    }

    public boolean sendSMS(String email, String[] auth_a, String toNumber, String msg) {
        boolean ok = false;
        String username = auth_a[0];
        String passward = auth_a[1];
        if (username != null && !username.isEmpty() && passward != null && !passward.isEmpty()) {
            String SMS_FROM = "eGenVar";
            NexmoSmsClient client = null;
            try {
                client = new NexmoSmsClientSSL(username, passward);
            } catch (Exception e) {
            }
            TextMessage message = new TextMessage(SMS_FROM, toNumber, msg);
            SmsSubmissionResult[] results = null;
            try {
                if (client != null) {
                    results = client.submitMessage(message);
                    for (int i = 0; i < results.length; i++) {
                        if (results[i].getStatus() == SmsSubmissionResult.STATUS_OK) {
                            ok = true;
                        }
                    }
                }
            } catch (Exception e) {
            }
        }
//        else {
//            System.out.println("Error: obtaining credentials");
//        }
        return ok;
    }

    public void sendSMS2(String email, String password, String toNumber, String msg) {
////        String value = getServletConfig().getInitParameter("password");
//        if (value != null) {
//        }

        String USERNAME = "472f9b49"; // get this from database
        String PASSWORD = "";
        String SMS_FROM = "eGenVar";
        NexmoSmsClient client = null;
        try {
            client = new NexmoSmsClientSSL(USERNAME, PASSWORD);
        } catch (Exception e) {
            System.err.println("Failed to instanciate a Nexmo Client");
            e.printStackTrace();
        }
        TextMessage message = new TextMessage(SMS_FROM, toNumber, msg);

        // Use the Nexmo client to submit the Text Message ...

        SmsSubmissionResult[] results = null;
        try {
//            results = client.submitMessage(message);
        } catch (Exception e) {
            System.err.println("Failed to communicate with the Nexmo Client");
            e.printStackTrace();
        }
//        System.out.println("... Message submitted in [ " + results.length + " ] parts");
//        for (int i = 0; i < results.length; i++) {
//            System.out.println("--------- part [ " + (i + 1) + " ] ------------");
//            System.out.println("Status [ " + results[i].getStatus() + " ] ...");
//            if (results[i].getStatus() == SmsSubmissionResult.STATUS_OK) {
//                System.out.println("SUCCESS");
//            } else if (results[i].getTemporaryError()) {
//                System.out.println("TEMPORARY FAILURE - PLEASE RETRY");
//            } else {
//                System.out.println("SUBMISSION FAILED!");
//            }
//            System.out.println("Message-Id [ " + results[i].getMessageId() + " ] ...");
//            System.out.println("Error-Text [ " + results[i].getErrorText() + " ] ...");
//
//            if (results[i].getMessagePrice() != null) {
//                System.out.println("Message-Price [ " + results[i].getMessagePrice() + " ] ...");
//            }
//            if (results[i].getRemainingBalance() != null) {
//                System.out.println("Remaining-Balance [ " + results[i].getRemainingBalance() + " ] ...");
//            }
//        }

    }

    public void sendUserCodeConfirmation(Connection c_con, String email, String name, String confirmationcode, HttpServletRequest request) {

        try {
            if (request.getSession().getAttribute("ATRB_LOAD") == null) {
                Constants.loadSessionVariables(c_con, request);
            }
            String message = "Dear  " + name + ",\n" + "  "
                    + "Thank you for registering with the eGenVar data-management system. \n"
                    + "Please use the confirmation code below to confirm your registration. \n"
                    + "If you did not try to register with the eGenVar data-management system, \n"
                    + "please report this to " + getAdminEmail() + ". \n"
                    + "\n\nYour confirmation code : " + confirmationcode + "\n\n"
                    + "Web interface URL: " + Constants.server_URL + "\n"
                    + "WSDL URL for egenvar tool " + Constants.wsdl_URL + "\n\n"
                    + "Regards,\n"
                    + "eGenVar team\n";
            send("eGenVar user authentication", email, "recipient_name", message);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void sendUserCodeConfirmation_delete(Connection c_con, String email, String name, String confirmationcode, HttpServletRequest request) {
        try {
            if (request.getSession().getAttribute("ATRB_LOAD") == null) {
                Constants.loadSessionVariables(c_con, request);
            }
            String message = "Dear  " + name + ",\n" + "  "
                    + "Thank you for using the eGenVar data-management system. \n"
                    + "Please use the confirmation code below to delete your account. \n"
                    + "If you did not try to delete eGenVar account, \n"
                    + "please report this to " + getAdminEmail() + ". \n"
                    + "\n\nYour confirmation : " + confirmationcode + "\n\n "
                    + "Web interface URL: " + Constants.server_URL + "\n"
                    + "WSDL URL for egenvar tool " + Constants.wsdl_URL + "\n\n"
                    + "Regards,\n"
                    + "eGenVar team\n";
            send("eGenVar user authentication", email, "recipient_name", message);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void sendUserPassword(Connection c_con, String email, String name, String username, String password, HttpServletRequest request) {
        try {
            if (request.getSession().getAttribute("ATRB_LOAD") == null) {
                Constants.loadSessionVariables(c_con, request);
            }
            String message = "Dear " + name + ",\n" + " Thank you for registering with the eGenVar data-management System. "
                    + "Your account will be activated shortly. \n"
                    + "Please contact " + getAdminEmail() + " if your account is not active within 24h. After the activation , The user name and password given below"
                    + " can be used to access; the eGenVar data-management system, Galaxy instance and the FTP upload system.\n"
                    + "However, you can not use this username to gain SSH access at this moment.\n "
                    + "\n  User Name: " + username + "\n  Password: " + password + "\n\n"
                    + "Web interface URL: " + Constants.server_URL + "\n"
                    + "WSDL URL for egenvar tool " + Constants.wsdl_URL + "\n\n"
                    + "Regards,\neGenVar team ";

            send("eGenVar user authentication", email, "recipient_name", message);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void send(String subject, String recipient_mail, String recipient_name, String messaage) throws Exception {
        getDatasource_mailer();
        if (mail_session != null && recipient_mail != null && recipient_mail.matches("[A-z0-9_\\-\\.]+@[A-z0-9_\\-]+[\\.]{1}[A-z0-9_\\-\\.]+")) {
            Message msg = new MimeMessage(mail_session);
            msg.setSubject(subject);
            msg.setRecipient(Message.RecipientType.TO, new InternetAddress(recipient_mail, recipient_name));
            msg.setFrom(new InternetAddress(getAdminEmail(), "eGenVar admin"));
            BodyPart messageBodyPart = new MimeBodyPart();
            messageBodyPart.setText(messaage);
            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(messageBodyPart);
            msg.setContent(multipart);
            Transport.send(msg);
        } else {
            System.out.println("Error 1123: Mailer =" + recipient_mail);
        }
    }

    public void informAdmin(String subject, String messaage) {
        try {
            getDatasource_mailer();
            if (mail_session != null && getAdminEmail() != null) {
                Message msg = new MimeMessage(mail_session);
                msg.setSubject(subject);
                msg.setRecipient(Message.RecipientType.TO, new InternetAddress(getAdminEmail(), "Admin"));
                msg.setFrom(new InternetAddress(getAdminEmail(), "eGenVar admin"));
                BodyPart messageBodyPart = new MimeBodyPart();
                messageBodyPart.setText(messaage);
                Multipart multipart = new MimeMultipart();
                multipart.addBodyPart(messageBodyPart);
                msg.setContent(multipart);
                Transport.send(msg);
            } else {
                System.out.println("Error 1123: Mailer =" + getAdminEmail());
            }
        } catch (Exception ex) {
            System.out.println("Error: informing admin failed " + ex.getMessage());
        }
    }
    /*
     MethodID=1
     */

    private void getDatasource_mailer() throws ServletException {
        if (mail_session == null) {
            try {
                InitialContext ctx = new InitialContext();
//                System.out.println("Connecting to " + DATASOURCE_NAME);
                mail_session = (Session) ctx.lookup(DATASOURCE_NAME);
//                Context env = (Context) new InitialContext().lookup("java:comp/env");
//                mail_session = (Session) env.lookup(DATASOURCE_NAME);
//                System.out.println("mail_session   " + mail_session.getDebugOut());
                if (mail_session == null) {
                    throw new ServletException("`" + DATASOURCE_NAME + "' is an unknown DataSource");
                }
            } catch (NamingException e) {
                System.out.println("Error M1a " + e.getExplanation());
            }
        }
    }
  /*
     MethodID=AS61.9     */

    private String getAdminEmail() {
        if (admin_email == null) {
            try {
                try {
                    Connection ncon = getDatasource().getConnection();
                    if (!ncon.isClosed()) {
                        Statement st_1 = ncon.createStatement();
                        String config_tbl = Constants.get_correct_table_name(ncon, "config");
                    
                        if (config_tbl != null) {
                       
                            ResultSet r_1 = st_1.executeQuery("select param_value from " + config_tbl + " where name='ADMIN_EMAIL'");
                            while (r_1.next()) {
                                admin_email = r_1.getString("param_value");
                            }
                        }
                    }
                    if (!ncon.isClosed()) {
                        ncon.close();
                    }
                } catch (SQLException ex) {
                    System.out.println("Error AS61.9.a " + ex.getMessage());
                }


            } catch (Exception ex) {
                System.out.println("Error AS61.9.b " + ex.getMessage());
            }
        }

        return admin_email;
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
