package no.ntnu.medisn.egenvar;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
import java.io.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormatSymbols;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
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
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;
import org.apache.commons.net.ftp.FTPSClient;

/**
 * ftps://129.241.180.232/
 *
 * @author sabryr
 */
@WebServlet(name = "FTPLister", urlPatterns = {"/Upload/FTPLister"})
public class FTPLister extends HttpServlet {

    /**
     * Processes requests for both HTTP
     * <code>GET</code> and
     * <code>POST</code> methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     *
     */
    private final static String FTP_SITE_PATTERN = "(ftp://[^/]+)(.*)";
    private DataSource dataSource;
    private final int MAXIMUM_TO_DISPLAY = 200;

    /*
     * // out.println("<p>" + filepath + "</p> ");
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=ISO-8859-1");
        getDatasource();
        PrintWriter out = response.getWriter();
        Pattern p_1 = Pattern.compile(FTP_SITE_PATTERN);
        Connection c_con = null;
        try {
            c_con = getDatasource().getConnection();
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\">");
            out.println("<link HREF=\"" + Constants.getServerName(c_con) + "resources/egenStyler.css\"  REL=\"stylesheet\" TYPE=\"text/css\" >");

            out.println("<title>Servlet FTPLister</title>");
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
            HttpSession session = request.getSession(true);
            out.println("<p>" + session.getAttribute("Header") + "</p>");
            HashMap<String, String> fileToId_map = new HashMap<String, String>();
            ArrayList<String> files_in_db_l = new ArrayList<String>(100);
            if (c_con != null) {
                try {
                    if (!c_con.isClosed()) {
                        Statement st_1 = c_con.createStatement();
                        String s_1 = "select id,location from files";
                        ResultSet r_1 = st_1.executeQuery(s_1);
                        while (r_1.next()) {
                            String location = r_1.getString(2).trim();
                            Matcher m_1 = p_1.matcher(location);
                            if (m_1.matches()) {
                                location = m_1.group(2);
                            }
                            if (location != null && !location.isEmpty()) {
                                files_in_db_l.add(location);
                                if (fileToId_map.containsKey(location)) {
                                    fileToId_map.put(location, fileToId_map.get(location) + "|" + r_1.getString(1));
                                } else {
                                    fileToId_map.put(location, r_1.getString(1));
                                }
                            }
                        }
                        close(null, st_1, r_1);
                    }

                } catch (SQLException e) {
                    throw new ServletException(e);
                }
            }

            String user = request.getParameter("ftp_username");
            String pass = request.getParameter("ftp_password");
            String site = request.getParameter("ftp_site");
            String ftpdir = request.getParameter("ftp_folder");
            String server_port = request.getParameter("Virtual_server_port").trim();
            String protocol = "TLS";
            boolean isimplicit = false; // explicit connection
            int port = 21;
            if (server_port.matches("[0-9]+")) {
                port = new Integer(server_port);
            }
            site = site.replace("ftp://", "");
            FTPSClient ftps = new FTPSClient(protocol, isimplicit);
            ftps.connect(site, port);
            ftps.execAUTH("TLS");
            ftps.execPBSZ(0);
            ftps.execPROT("P");
            ftps.login(user, pass);
            ftps.enterLocalPassiveMode();
            ftps.setFileTransferMode(FTP.STREAM_TRANSFER_MODE);
            int repl = ftps.getReplyCode();
            if (!FTPReply.isPositiveCompletion(repl)) {
                ftps.disconnect();
                out.println("<h3>Connection failed to " + site + "</h3>");
            } else {
//                Locale locale = new Locale(Locale.ENGLISH.getLanguage());
//                DateFormatSymbols format = new DateFormatSymbols(locale);
                String DATE_FORMAT_1 = "yyyy-MM-dd HH:mm:ss"; // 
                java.text.SimpleDateFormat sdf_2 = new java.text.SimpleDateFormat(DATE_FORMAT_1);
                sdf_2.setTimeZone(TimeZone.getDefault());
                ftps.changeWorkingDirectory(ftpdir);

                out.println("<h2>Following files were found with access permission at  " + site + "</h2>");
                FTPFile[] dirs = ftps.listFiles();
                ArrayList<String> ftpdirsPath_l = new ArrayList<String>(dirs.length);
                String curentDir = ftps.printWorkingDirectory();
                for (int i = 0; i < dirs.length; i++) {
                    if (dirs[i].hasPermission(FTPFile.USER_ACCESS, FTPFile.READ_PERMISSION) && dirs[i].isDirectory()) {
                        ftpdirsPath_l.add(curentDir + "/" + dirs[i].getName() + "/");
                    }
                }
                ArrayList<String> ftpdirsPath_final_l = new ArrayList<String>(ftpdirsPath_l.size());
                ftpdirsPath_final_l.add(curentDir + "/");
                ftpdirsPath_final_l.addAll(ftpdirsPath_l);
                boolean done = false;
                int safety = 1000;
                while (!done && safety > 0) {
                    safety--;
                    ArrayList<String> ftpdirsPath_New_l = new ArrayList<String>(ftpdirsPath_l.size());
                    for (int i = 0; i < ftpdirsPath_l.size(); i++) {
                        ftps.changeWorkingDirectory(ftpdirsPath_l.get(i));
                        FTPFile[] dirs_tmp = ftps.listFiles();
                        ftps.changeToParentDirectory();
                        for (int j = 0; j < dirs_tmp.length; j++) {
                            if (dirs_tmp[j].hasPermission(FTPFile.USER_ACCESS, FTPFile.READ_PERMISSION) && dirs_tmp[j].isDirectory()) {
                                String cPath = ftpdirsPath_l.get(i) + dirs_tmp[j].getName() + "/";
                                if (!ftpdirsPath_l.contains(cPath)) {
                                    ftpdirsPath_New_l.add(cPath);
                                }
                            }
                        }
                    }
                    if (ftpdirsPath_New_l.isEmpty()) {
                        done = true;
                    }
                    ftpdirsPath_l.clear();
                    ftpdirsPath_l.addAll(ftpdirsPath_New_l);
                    ftpdirsPath_final_l.addAll(ftpdirsPath_New_l);
                    if (safety == 0) {
                        out.println("<p><font color='red'>Warning !</font>, more than 1000 folders found only the first 1000 liwsted below </p> ");
                    }
                }
                int display_limit = ftpdirsPath_final_l.size();
                if (display_limit > MAXIMUM_TO_DISPLAY) {
                    display_limit = MAXIMUM_TO_DISPLAY;
                    out.println("<p><font color='red'>Warning !, more than " + MAXIMUM_TO_DISPLAY + " entries found and only the first " + MAXIMUM_TO_DISPLAY + " listed below. Please specify subfolders to avoid this.</font></p> ");
                }
                out.println("<table  border=\"1\">");
                for (int i = 0; i < display_limit; i++) {
                    ftps.changeWorkingDirectory(ftpdirsPath_final_l.get(i));
                    FTPFile[] files = ftps.listFiles();
                    ftps.changeToParentDirectory();
                    for (int j = 0; j < files.length; j++) {
                        FTPFile cFile = files[j];
                        String fileOrFolder = "";
                        String filenm = cFile.getName();
                        String fileType = "NA";
                        if (filenm.contains(".")) {
                            String[] split = filenm.split("\\.");
                            fileType = split[split.length - 1];
                        }

                        if (cFile.isFile()) {
                            fileOrFolder = " <font color=#800000>File</font>";
                        } else {
                            fileOrFolder = " <font color=#008080>Folder</font>";
                        }
                        String cFile_location_path = "/.." + ftpdirsPath_final_l.get(i) + files[j].getName();
                        String cFile_Full_path = "ftp://" + site + cFile_location_path;
                        String status = "<font color=#667C26>New " + fileOrFolder + " </font>";
                        if (files_in_db_l.contains(cFile_location_path)) {
                            String ids = fileToId_map.get(cFile_location_path);
                            String[] ids_a = ids.split("\\|");
                            String id_url = "";

                            for (int k = 0; k < ids_a.length; k++) {
                                id_url = id_url + "<a href=../Search/FileDetails.jsp?searchdetislFor=SELECT&nbsp;*&nbsp;FROM&nbsp;files&nbsp;where&nbsp;id=" + ids_a[k] + ">" + ids_a[k] + ",</a>";
                            }
                            status = "<font color='#7D2252'>Already submited  " + fileOrFolder + "file_id=" + id_url + "</font>";
                        }
                        String timestp = sdf_2.format(cFile.getTimestamp().getTime());
                        timestp = timestp.replaceAll("\\s", "&nbsp;");
                        String parmeters = "?selectedFTPURL=" + cFile_Full_path + "&selectedFilenm=" + files[j].getName() + "&selectedlastmodified=" + timestp + "&selectedGroup=" + cFile.getGroup() + "&selectedType=" + fileType;
                        out.println("<tr><td>" + cFile_Full_path + "</td><td>" + timestp + "</td><td>" + status + "</td><td>" + "<a href=InsertFile.jsp" + parmeters + ">Select</a></td></tr>");

                    }
                }
                out.println("</table>");
            }
            out.println("<p><font color='gray'>----------------------------------------END----------------------------------------</font></p> ");
            out.println("</body>");
            out.println("</html>");
            ftps.logout();
            close(c_con, null, null);
        } catch (SQLException ex) {
            out.println("<p><font color='red'>Error !</font> " + ex.getMessage() + " </p> ");
        } catch (Exception ex) {
            out.println("<p><font color='red'>Error !</font> " + ex.getMessage() + " </p> ");
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
