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
import java.util.ArrayList;
import java.util.HashMap;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletConfig;
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
public class DirectoryLister2 extends HttpServlet implements Runnable {

//    Thread directry_lister;
    private ArrayList<String> avoided_cheksum_l;
//    private Connection ncon;
    private DataSource dataSource;
    public final static String CHEKBX_PREFX = "selectfilechkbx_";
//    public final static String DESCRIPT_PREFX = "Description";
    private final int MAXIMUM_TO_DISPLAY = 100;
    private final int MAXIMUM_TO_CONSIDER = 500;
    private final int MINIMUM_FILESIZE = 100;
    private final long MAXIMUM_FILESIZE = 107374182400l;//102410;//107374182400l;
    public final static String LAST_ACTIVE_TIME_FALG = "LASTUTIME";

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);                  // always!
//        directry_lister = new Thread(this);
//        directry_lister.setPriority(Thread.MIN_PRIORITY);  // be a good citizen
//        directry_lister.start();
    }

    @Override
    public void run() {
    }

    private void myInit() {
        if (avoided_cheksum_l == null) {
            avoided_cheksum_l = new ArrayList<>();
            avoided_cheksum_l.add("Not_calculated");
            avoided_cheksum_l.add("Not a regular file");
            avoided_cheksum_l.add("Checksum_not_available");
            avoided_cheksum_l.add("Read access denied");
            avoided_cheksum_l.add(GetChecksum.STILL_HASHING);
            avoided_cheksum_l.add(GetChecksum.INTERUPTED_HASHING);
        }

    }

    private void pageReload(HttpServletResponse response, String url, int caller) {
        try {
            if (!response.isCommitted()) {
                response.sendRedirect(url);
            } else {
                System.out.println("120 did not redirect");
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
        boolean loading_complete = true;
        Getfromremote get = null;
        GetChecksum getcheksum = null;
        ArrayList<String> found_file_l = new ArrayList<>(10);
        boolean metaReload_printed = false;
        PrintWriter out = response.getWriter();
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String site = request.getParameter("site");
        if (ses.getAttribute("getcheksum_runner2") != null) {
            getcheksum = (GetChecksum) ses.getAttribute("getcheksum_runner2");
        }
        String dirtouse = request.getParameter("dirtouse");
        if (dirtouse != null && !dirtouse.isEmpty()) {
            if (getcheksum != null) {
                getcheksum.cancel();
                ses.removeAttribute("getcheksum_runner2");
                getcheksum = null;
            }
            ses.removeAttribute("loading_complete2");
            ses.removeAttribute("found_file_l2");
            ses.removeAttribute(Constants.REMOTE_EXTERACTION_SESSION_FLAG2);

        }

        Connection c_con = null;
        try {
            c_con = getDatasource().getConnection();
            int user_level = 9;
            if (ses.getAttribute("user_level") != null && ses.getAttribute("user_level").toString().matches("[0-9]+")) {
                user_level = new Integer(ses.getAttribute("user_level").toString());
            }
            if (user_level > 7) {
                response.sendRedirect(Constants.getServerName(c_con) + "errormsgs?error=403");
            } else if (ses.getAttribute("ATRB_LOAD") == null) {
                pageReload(response, Constants.getServerName(c_con) + "index", 1);
            } else {
                if (ses.getAttribute("found_file_l2") != null) {
                    found_file_l = (ArrayList<String>) ses.getAttribute("found_file_l2");
                }


                if (ses.getAttribute("loading_complete2") != null && ses.getAttribute("loading_complete2").toString().toUpperCase().equals("FALSE")) {
                    loading_complete = false;
                }
                if (ses.getAttribute("getfromremote_directory_runner") != null) {
                    get = (Getfromremote) ses.getAttribute("getfromremote_directory_runner");
                }
                if (site == null) {
                    if (ses.getAttribute("site") != null) {
                        site = (String) ses.getAttribute("site");
                    }
                } else {
                    ses.setAttribute("site", site);
                }

                if (username == null) {
                    if (ses.getAttribute("username") != null) {
                        username = (String) ses.getAttribute("username");
                    }
                } else {
                    ses.setAttribute("username", username);
                }

                if (ses.getAttribute("password") != null) {
                    password = (String) ses.getAttribute("password");
                }

//                System.out.println("99 site=" + site + "  username=" + username + "  dirtouse=" + dirtouse + "  password=" + password);
                HashMap<String, String[]> file2properties_aMap = null;
                if (ses.getAttribute(Constants.REMOTE_EXTERACTION_SESSION_FLAG2) != null) {
                    file2properties_aMap = (HashMap<String, String[]>) ses.getAttribute(Constants.REMOTE_EXTERACTION_SESSION_FLAG2);
                }
                String tbl_heading = "Processing completed";
                out.println("<!DOCTYPE html>");
                out.println("<html>");
                out.println("<head>");
                out.println("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=ISO-8859-1\">");
                if (found_file_l != null && !found_file_l.isEmpty()) {
                    metaReload_printed = true;
                    if (getcheksum == null) {
                        getcheksum = new GetChecksum(c_con, found_file_l, site, username, password, ses);
                        getcheksum.start();
                        ses.setAttribute("getcheksum_runner2", getcheksum);
                        out.println("<META HTTP-EQUIV=\"refresh\" CONTENT=\"1\">");
                        tbl_heading = "<warning>Still processing, please wait</warning>";
                    } else {
                        if (getcheksum.isHshing()) {
                            tbl_heading = "<warning>Still hashing, please wait</warning>";
                            out.println("<META HTTP-EQUIV=\"refresh\" CONTENT=\"5\">");
                        }
                    }
                }


                out.println(" <link HREF=\"" + Constants.getServerName(c_con) + "resources/egenStyler.css\"  REL=\"stylesheet\" TYPE=\"text/css\" >");
                out.println("<title>GenVar Datamangement System</title>");
                out.println(" <script language = JavaScript>     \n"
                        + "            function setProgress(target, value){\n"
                        + "                if(document.getElementById(target)!=null){\n"
                        + "                    document.getElementById(target).style.visibility = value; \n"
                        + "              \n"
                        + "                }\n"
                        + "                \n"
                        + "            } \n"
                        + "        </script>");

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
                out.println(ses.getAttribute("Header"));
                out.println("<h1>Directory Lister</h1>");
                out.println("  <div id=\"div_progress\">Please wait .. <img src=\"../progress.gif\" width=\"200\" height=\"10\" alt=\"progress\"  id=\"progressanim\"/></div>");

                if (dirtouse != null && !dirtouse.isEmpty()) {
                    if (get == null) {
                        ses.setAttribute("getfromremote_directory_runner", startExtraction(c_con, dirtouse, site, username, password));
                        ses.setAttribute("loading_complete2", "false");
                        ses.removeAttribute("found_file_l2");
                        pageReload(response, Constants.getServerName(c_con) + "Upload/DirectoryLister2", 176);
                    }
                }

                if (get != null && !loading_complete) {
                    ses.setAttribute(Constants.REMOTE_EXTERACTION_SESSION_FLAG2, getResults(get, ses));
                    ses.setAttribute("loading_complete2", "true");
                    pageReload(response, Constants.getServerName(c_con) + "Upload/DirectoryLister2", 183);

                } else if (file2properties_aMap != null) {
                    if (file2properties_aMap.containsKey("ERRORS")) {
                        out.println("" + file2properties_aMap.remove("ERRORS")[0]);
                    }
                    HashMap<String, String[]> path2checksum_map = new HashMap<>();
                    if (getcheksum != null) {
                        path2checksum_map = getcheksum.getfile2Hash();
                    }
                    out.println("<form accept-charset=\"ISO-8859-1\"   method=\"post\" action=\"" + Constants.getServerName(c_con) + "Upload/InsertFiles\">");
                    out.println("<table>");
                    out.println("<thead>");
                    out.println("<tr>");
                    out.println("<th colspan=\"7\">");
                    out.println("<input type=\"submit\" value=\"Submit selection\" name=\"submit_selection\" />");
                    if (metaReload_printed) {
                        out.println(tbl_heading);
                    }
                    out.println("<th>");
                    out.println("</tr>");
                    out.println("</thead>");
                    out.println("<tbody>");
                    ArrayList<String> key_l = new ArrayList<>(file2properties_aMap.keySet());

                    for (int i = 0; i < key_l.size(); i++) {
                        String[] val_a = file2properties_aMap.get(key_l.get(i));
                        if (val_a != null && val_a.length > 0) {
                            String c_path = val_a[0];
                            found_file_l.add(c_path);
                            if (path2checksum_map.containsKey(c_path)) {
                                file2properties_aMap.get(key_l.get(i))[5] = path2checksum_map.get(c_path)[0];
                                file2properties_aMap.get(key_l.get(i))[3] = path2checksum_map.get(c_path)[1];
                                file2properties_aMap.get(key_l.get(i))[6] = path2checksum_map.get(c_path)[2];
                            }
                        }
                    }
                    if (!metaReload_printed && found_file_l != null && !found_file_l.isEmpty()) {
                        ses.setAttribute("found_file_l2", found_file_l);
                        pageReload(response, Constants.getServerName(c_con) + "Upload/DirectoryLister2", 235);
                    }
                    for (int i = 0; i < key_l.size(); i++) {
                        String[] val_a = file2properties_aMap.get(key_l.get(i));
                        if (val_a != null && val_a.length >= 13) {
                            String checksumtoprint = val_a[5];
                            if (checksumtoprint != null && (checksumtoprint.equalsIgnoreCase(GetChecksum.STILL_HASHING) || checksumtoprint.equalsIgnoreCase(GetChecksum.INTERUPTED_HASHING))) {
                                checksumtoprint = "<warning><blinkytext>" + checksumtoprint + "</blinkytext></warning>";
                            }
                            String parmeters = "Filepath=" + val_a[0] + "|Name=" + val_a[10] + "|Timestamp=" + val_a[16] + "|Group=" + val_a[8] + "|FileType=" + val_a[11] + "|Checksum=" + val_a[5] + "|Size=" + val_a[13] + "|Report_name=" + val_a[14] + "|batch_name=" + val_a[15] + "|server_name=" + val_a[18];
                            out.println("<tr>");
                            String selectedlastmodified = "<input type=\"text\" readonly=\"readonly\" name=\"" + i + "_selectedlastmodified\" value=\"" + val_a[4] + "\"/>";
                            String filesize = "<input type=\"text\" readonly=\"readonly\" name=\"" + i + "_filesize\" value=\"" + val_a[2] + "\"/>";
                            String select_box = "<input type=\"checkbox\" name=\"" + CHEKBX_PREFX + i + "\" value=\"" + parmeters + "\"/>";
                            out.println("<td>" + select_box + "</td><td>" + val_a[0] + " </td><td>" + selectedlastmodified + " </td><td>" + filesize + "</td><td>" + checksumtoprint + "</td><td>" + val_a[3] + "</td><td>" + val_a[6] + "</td>"); //<td><input type=\"text\" value=\"" + val_a[10] + "," + val_a[14] + "," + val_a[15] + "\" name=\"" +  i + "\"/></td>
                            out.println("</tr>");
                        }
                    }


                    out.println("</tbody>");
                    out.println("</table>");
                    out.println("</form>");

                } else {
                }

                out.println("  <script type=\"text/javascript\">\n"
                        + "            setProgress(\"div_progress\", \"hidden\");\n"
                        + "    </script>");
                out.println("<footer>" + ses.getAttribute("footer") + "</footer>");
                out.println("</body>");
                out.println("</html>");

            }
            if (c_con != null && !c_con.isClosed()) {
                c_con.close();
            }
        } catch (SQLException ex) {
        } finally {
            out.close();
            close(c_con, null, null);
        }
    }

    private void checkWithDB(HashMap<String, String[]> file2properties_aMap) {
    }

    private Getfromremote startExtraction(Connection c_con, String dirtouse, String site, String user, String pass) {
        Getfromremote get = new Getfromremote(c_con, site, user, pass, dirtouse, true);
        (new Thread(get)).start();
        return get;

    }

    private HashMap<String, String[]> fillinChecksum(HashMap<String, String[]> file2properties_aMap, GetChecksum getcheksum) {

        return file2properties_aMap;
    }

    private HashMap<String, String[]> getResults(Getfromremote get, HttpSession ses) {

        HashMap<String, String[]> file2properties_aMap = null;
        if (get != null) {
            ses.removeAttribute("getfromremote_directory_runner");
            file2properties_aMap = get.getDirectoryListerResult();
        }

        return file2properties_aMap;
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
