package no.ntnu.medisn.egenvar;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
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
public class FiledownloadHandler extends HttpServlet {

    private DataSource dataSource;
//    private PrintWriter out;
    private Pattern ptn_1;
//    private String parameters;

    private void myInit() {
        if (ptn_1 == null) {
            ptn_1 = Pattern.compile(Constants.DIRECTORY_PATTERN);
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
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=ISO-8859-1");
        PrintWriter out = response.getWriter();
        myInit();
        String current_tbl_nm = request.getParameter("current_tbl_nm");
        String selected_id_List = request.getParameter("selected_id_List");
        String operation = request.getParameter("operation");
        String parameters = request.getParameter("parameters");
        String server = request.getParameter("server");
        Connection c_con = null;
        try {
            c_con = getDatasource().getConnection();
            /*
             * TODO output your page here. You may use following sample code.
             */
            out.println("<!DOCTYPE HTML>");
            out.println("<html>");
            out.println("<head>");
            out.println("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=ISO-8859-1\">");
            out.println("<link HREF=\"" + Constants.getServerName(c_con) + "resources/egenStyler.css\"  REL=\"stylesheet\" TYPE=\"text/css\" >");
            out.println("<title>Results</title>");
            out.println("</head>");
            out.println("<body>");
            HttpSession session = request.getSession(true);
            out.println("<p>" + session.getAttribute("Header") + "</p>");
            out.println("<h1>Download</h1>");
            if (operation == null || operation.equalsIgnoreCase("load")) {
                if (current_tbl_nm != null && !current_tbl_nm.isEmpty() && selected_id_List != null && !selected_id_List.isEmpty() && !selected_id_List.equals("-1")) {
                    getFromDb(c_con, current_tbl_nm, selected_id_List, out);
                } else {
                    out.println("<error>Error: Table name null or id list empty </error>");
                }
            } else if (operation.equalsIgnoreCase("download")) {
                if (parameters != null && !parameters.isEmpty() && server != null && !server.isEmpty()) {
                    String code = "no.ntnu.medisin.egenvar.Downloader";
                    String archive = "../../eGenVarDownloader.jar";
                    String version = "1.6";
                    launchApplet(code, archive, parameters, server, version, out);
                } else {
                    out.println("<error>Error: Failed to retrieve file list or server name </error>");
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

    private void getFromDb(Connection c_con, String current_tbl_nm, String selected_id_List, PrintWriter out) {
        if (current_tbl_nm == null || current_tbl_nm.isEmpty()) {
            out.println("<error>Error: Table name was null</error>");
        } else {
            String server = "";
            ArrayList<String> selectedFile_l = new ArrayList<String>(5);
            ArrayList<String> selected_l = new ArrayList<String>(5);
            ArrayList<String> selectedDisplay_l = new ArrayList<String>(5);
            ArrayList<String> deselectedDisplay_l = new ArrayList<String>(5);
            try {
                if (!c_con.isClosed()) {
                    String s_1 = "SELECT name,filesize,checksum  from " + current_tbl_nm + " WHERE id in (" + selected_id_List + ")";
                    Statement st_1 = c_con.createStatement();
                    ResultSet r_1 = st_1.executeQuery(s_1);
                    boolean multiple_servers_found = false;
                    while (r_1.next()) {
                        String filepath = r_1.getString("name");
                        if (filepath != null) {
                            filepath = filepath.trim();
                        }
                        long filesize = r_1.getLong("filesize");
                        String checksum = r_1.getString("checksum");
                        if (checksum != null) {
                            checksum = checksum.trim();
                        }
                        if (filepath != null) {
                            Matcher m_1 = ptn_1.matcher(filepath);
                            if (m_1.matches()) {
                                if (server.isEmpty()) {
                                    server = m_1.group(1).trim();
                                } else if (!server.equals(m_1.group(1))) {
                                    multiple_servers_found = true;
                                }
                                if (!selectedFile_l.contains(m_1.group(2))) {
                                    selectedFile_l.add(m_1.group(2));
                                }
                                String linktodownloader = m_1.group(2) + "|" + checksum + "|" + filesize;
                                if (!selected_l.contains(linktodownloader)) {
                                    selected_l.add(linktodownloader);
                                }
                                selectedDisplay_l.add("<tr><td>" + filepath + "</td></tr>");

                            } else {
                                deselectedDisplay_l.add("<tr><td>" + filepath + "</td><td><error>Can not use SCP to download this file</error></td></tr>");
                            }
                        } else {
                            out.println("<error>Error: File path was null </error>");
                        }

                    }
                    out.println("<table>");
                    out.println("<tbody><tr>");
                    String parameters;
                    if (!multiple_servers_found) {
                        if (!selectedDisplay_l.isEmpty()) {
                            parameters = selected_l.get(0);
                            for (int i = 1; i < selected_l.size(); i++) {
                                parameters = parameters + "," + selected_l.get(i);
                            }
                            try {
                                out.print("<td> <a href=\"" + Constants.getServerName(c_con) + "Search/SearchResults3?searchvalue=" + URLEncoder.encode("(" + selected_id_List + ")", "ISO-8859-1") + "&current_tbl_nm=" + current_tbl_nm + "&searchoperator=in&serachType=id&reload=true&operation=" + SearchResults3.SEARCH_OP_FLAG + "\">Modify List </a></td>");
                            } catch (UnsupportedEncodingException ex) {
                            }
                            out.print("<td>");
                            out.print("<form accept-charset=\"ISO-8859-1\"   method=\"post\">");
                            out.println("<input type=\"hidden\" value=\"download\" name=\"operation\" />");
                            out.println("<input type=\"hidden\" value=\"" + parameters + "\" name=\"parameters\" />");
                            out.println("<input type=\"hidden\" value=\"" + server + "\" name=\"server\" />");

                            out.print("<input type=\"submit\" value=\"Downlaod\"/></td>");
                            out.print("</form>");
                            out.print("</td>");
                        } else {
                            out.print("<td> <a href=\"" + Constants.getServerName(c_con) + "Search/SearchResults3?current_tbl_nm=" + current_tbl_nm + "&reload=true&operation=" + SearchResults3.SEARCH_OP_FLAG + "\">Modify List </a></td>");

                        }

                        out.println("</tr></tbody></table>");
                        if (!deselectedDisplay_l.isEmpty()) {
                            out.println("<table border=\"1\" >");
                            out.println("<thead><tr><th >There were files where SCP could not be used for downloadig. (No of ommited files=" + deselectedDisplay_l.size() + ") </th></tr></thead>");
                            out.println("</table>");
                        }
                        if (!selectedDisplay_l.isEmpty()) {
                            out.println("<table border=\"1\" >");
                            out.println("<thead><tr><th >Following files were add to the downlaod queue (" + selectedDisplay_l.size() + " files) </th></tr></thead>");
                            out.println("<tbody>");
                            for (int i = 0; i < selectedDisplay_l.size(); i++) {
                                out.println(selectedDisplay_l.get(i));
                            }
                            out.println("</tbody>");
                            out.println("</table>");
                        } else {
                            out.println("<table border=\"1\" >");
                            out.println("<thead><tr><th>Following files were add to the downlaod queue </th></tr></thead>");
                            out.println("<tbody>");
                            out.println("<tr><td>Empty list</td></tr>");

                            out.println("</tbody>");
                            out.println("</table>");
                        }
                    } else {
                        out.println("<error>Error: There were files from more than one server. All files selected must be located on the same server</error>");
                    }

                    close(null, st_1, r_1);
                } else {
                    out.println("<error>Error: Failed to stablish connection</error>");
                }
            } catch (SQLException ex) {
                out.println("<error>Error: table name was null. " + ex.getMessage() + "</error>");
            }
        }



    }

    private void launchApplet(String code, String archive, String parameters, String remote_loc, String version, PrintWriter out) {
        String out_txt = "<script src=\"http://java.com/js/deployJava.js\"></script> \n"
                + "<script> \n"
                + " document.body.style.backgroundColor=\"#f0f9ff\"; \n"
                + " var attributes = { \n"
                + " code: \"" + code + "\",  \n"
                + " archive: \"" + archive + "\",  \n"
                + " width:720, \n"
                + " height:600 \n"
                + "}; \n"
                + " var parameters = {jnlp_href:\"../launch.jnlp\",remote_loc:\"" + remote_loc + "\" , filelist:\"" + parameters + "\"}; \n"
                + " var version = \"" + version + "\";  \n"
                + " deployJava.runApplet(attributes, parameters, version);\n"
                + " </script>";
        out.println(out_txt);
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
