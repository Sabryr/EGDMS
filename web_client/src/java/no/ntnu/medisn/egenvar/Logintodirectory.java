/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package no.ntnu.medisn.egenvar;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.LinkedHashMap;
import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
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
public class Logintodirectory extends HttpServlet {

    private final int MAX_RESULTS_TO_KEEP = 10000;
    public static String encript_password = null;//"12345678";//genwerate this for each session
    private ArrayList<String> atributes_to_clear_l;
        private DataSource dataSource;

    private void myinit() {
        if (encript_password == null) {
            encript_password = getDecriptKey() + "";
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
        PrintWriter out = response.getWriter();
        myinit();
        Connection c_con = null;
        try {
            c_con = getDatasource().getConnection();
            Getfromremote get = null;
            HttpSession ses = request.getSession(true);
            if (ses.getAttribute("ATRB_LOAD") == null) {
                String redirectURL = Constants.getServerName(c_con) + "index";
                response.sendRedirect(redirectURL);
            } else {
                LinkedHashMap<String, ArrayList<String>> resutl_map = null;
                if (ses.getAttribute(Constants.REMOTE_EXTERACTION_SESSION_FLAG) != null) {
                    resutl_map = (LinkedHashMap<String, ArrayList<String>>) ses.getAttribute(Constants.REMOTE_EXTERACTION_SESSION_FLAG);
                }
                String reload = request.getParameter("reload");
                String site = request.getParameter("site");
                String username = request.getParameter("username");
                String dirtouse = request.getParameter("dirtouse");

                if (dirtouse == null || dirtouse.isEmpty()) {
                    dirtouse = "."; //.
                }
                if (ses.getAttribute("getfromremote_runner") != null) {
                    get = (Getfromremote) ses.getAttribute("getfromremote_runner");
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

                String password = request.getParameter("password");
                if (password != null) {
                    password = encrypt(encript_password, password);
                } else {
                    if (ses.getAttribute("password") != null) {
                        password = (String) ses.getAttribute("password");
                    }
                }


//                if (ses.getAttribute("site") != null) {
//                    dirtouse = (String) ses.getAttribute("dirtouse");
//                }
                out.println("<!DOCTYPE html>");
                out.println("<html>");
                out.println("<head>");
                out.println("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=ISO-8859-1\">");
                out.println(" <link HREF=\"" + Constants.getServerName(c_con) + "resources/egenStyler.css\"  REL=\"stylesheet\" TYPE=\"text/css\" >");
                out.println("<script language = JavaScript>\n"
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
                out.println("<title>GenVar Datamangement System</title>");
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
                out.println("<h1>Login details to access remote directory</h1>");
               

                out.println("<form accept-charset=\"ISO-8859-1\"  method=\"POST\" action=\"" + Constants.getServerName(c_con) + "Upload/logintodirectory\">\n"
                        + "        <table>\n"
                        + "            <thead>\n"
                        + "                <tr>\n"
                        + "                    <th>Attribute</th>\n"
                        + "                    <th>Value</th>\n"
                        + "                    <th>Comments</th>\n"
                        + "                </tr>\n"
                        + "            </thead>\n"
                        + "            <tbody>\n"
                        + "                <tr>\n"
                        + "                    <td>Server:</td>\n"
                        + "                    <td >\n"
                        + "                        <input type=\"text\" value=\"\" id=\"site\" name=\"site\" required=\"required\" title=\"Use the full name of the file hosting server\">\n"
                        + "                    </td>\n"
                        + "                    <td>\n"
                        + "                        Use the full name of the file hosting server.  <h7>E.g. <a onclick='setValue(\"site\", \"" + Constants.getServerBase(c_con) + "\");' style=\"cursor:pointer;\" onmouseover=\"this.style.textDecoration = 'underline';\" onmouseout=\"this.style.textDecoration = 'none';\" >" + Constants.getServerBase(c_con) + "</a></h7>\n"
                        + "            </td>\n"
                        + "            </tr>\n"
                        + "            <tr>\n"
                        + "                <td>Username:</td>              \n");
                if (ses.getAttribute("current_user") != null) {
                    out.println("<td><input type=\"text\" name=\"username\" value=\"" + ses.getAttribute("current_user").toString().replaceAll("@[A-Za-z.]+", "") + "\"></td>");
                } else {
                    out.println("<td><input type=\"text\" name=\"username\" value=\"\"></td>");
                }

                out.println("            </tr>\n"
                        + "            <tr>\n"
                        + "                <td>Password:</td>\n"
                        + "                <td><input type=\"password\" name=\"password\"  onkeydown='setProgress(\"div\", \"hidden\")'></td>\n"
                        + "                <td><div id=\"div\">Please wait .. <img src=\"../progress.gif\" width=\"200\" height=\"10\" alt=\"progress\"  id=\"progressanim\"/></div></td>\n"
                        + "\n"
                        + "            </tr>\n"
                        + "            <tr>\n"
                        + "                <td><input type=\"hidden\" name=\"reload\"  value=\"true\"></td>\n"
                        + "                <td><input type=\"submit\"  onclick='setProgress(\"div\", \"visible\")' value=\"Login\"></td>\n"
                        + "            </tr>\n"
                        + "            <tr>\n"
                        + "                <td colspan=\"3\">Please note that you can only store files located on a server accessible via Internet or an intranet. Use the full name of the file hosting server. Can not use localhost as server name here as the files will not be accessible from outside. </td>\n"
                        + "\n"
                        + "            </tr>\n"
                        + "            </tbody>\n"
                        + "        </table>\n"
                        + "    </form>");


                if (reload != null && site != null && username != null && password != null) {
                    ses.setAttribute("username", username);
                    ses.setAttribute("password", password);
//                    ses.setAttribute("dirtouse", dirtouse);
                    ses.setAttribute("site", site);


                    ses.setAttribute("getfromremote_runner", startExtraction(c_con,dirtouse, site, username, password));
                    resutl_map = null;
                }

                if (resutl_map != null) {

                    out.println("<p></p>");
                    out.println("<table>\n"
                            + "            <thead>\n"
                            + "                <tr>\n"
                            + "                    <th>Select directory</th>\n"
                            + "                </tr>\n"
                            + "            </thead>\n"
                            + "            <tbody>\n");
                    if (resutl_map.containsKey("ERRORS")) {
                        if (!resutl_map.get("ERRORS").isEmpty()) {
                            out.println("                <tr><td>\n");
                            out.println("<error>Errors" + resutl_map.get("ERRORS") + "</error>");
                            out.println("                </td></tr>\n");
                        }
                        resutl_map.remove("ERRORS");
                    }
                    if (resutl_map.containsKey(Constants.FROM_REPMOTE_PARENT_FOLDER_FLAG)) {
                        if (!resutl_map.get(Constants.FROM_REPMOTE_PARENT_FOLDER_FLAG).isEmpty()) {
                            String parent_folder = resutl_map.get(Constants.FROM_REPMOTE_PARENT_FOLDER_FLAG).get(0);
                            out.println("                <tr><td>\n");
                            out.println("<a href=\"" + Constants.getServerName(c_con) + "Upload/logintodirectory?&dirtouse=" + parent_folder + "&reload=true\">Go up one level</a>");
                            out.println("                </td></tr>\n");
                        }
//                        resutl_map.remove(Constants.FROM_REPMOTE_PARENT_FOLDER_FLAG);
                    }
                    ArrayList<String> dir_l = new ArrayList<>(resutl_map.keySet());
                    if (!dir_l.isEmpty()) {
                        for (int i = 0; i < dir_l.size(); i++) {
                            if (!dir_l.get(i).equals(Constants.FROM_REPMOTE_PARENT_FOLDER_FLAG)) {
                                int file_count = 0;
                                if (resutl_map.get(dir_l.get(i)).get(0).matches("[0-9]+")) {
                                    file_count = new Integer(resutl_map.get(dir_l.get(i)).get(0));
                                }
                                if (file_count > 100) {
                                    out.println("                <tr><td>\n");
                                    out.println(dir_l.get(i) + "(" + resutl_map.get(dir_l.get(i)).get(0) + " files) " + " (<warning>To many files, split or use command line</warning> | <a href=\"" + Constants.getServerName(c_con) + "Upload/logintodirectory=true&dirtouse=" + dir_l.get(i) + "\">Expand</a>))" + "\n");
                                    out.println("                </td></tr>\n");
                                } else {
                                    out.println("                <tr><td>\n");
                                    out.println(dir_l.get(i) + "(" + resutl_map.get(dir_l.get(i)).get(0) + " files) " + " (<a href=\"" + Constants.getServerName(c_con) + "Upload/DirectoryLister2?dirtouse=" + dir_l.get(i) + "&reload=true\">Use this location</a> | <a href=\"" + Constants.getServerName(c_con) + "Upload/logintodirectory?reload=true&dirtouse=" + dir_l.get(i) + "\">Expand</a>))" + "\n");
                                    out.println("                </td></tr>\n");
                                }
                            }


                        }
                    }

                    out.println("            <tbody>\n"
                            + " </table>");
                }

                out.println("   <script type=\"text/javascript\">\n"
                        + "            setProgress(\"div\", \"hidden\");\n"
                        + "    </script>");
                               out.println("<footer>" + ses.getAttribute("footer") + "</footer>");
                out.println("</body>");
                out.println("</html>");

                if (reload != null) {
                    if (!response.isCommitted()) {
                        String redirectURL = Constants.getServerName(c_con) + "Upload/logintodirectory";
                        response.sendRedirect(redirectURL);
                    }

                } else if (get != null) {
                    resutl_map = getResults(get, resutl_map, ses);
                    if (!response.isCommitted()) {
                        String redirectURL = Constants.getServerName(c_con) + "Upload/logintodirectory";
                        response.sendRedirect(redirectURL);
                    }
                }
                ses.setAttribute(Constants.REMOTE_EXTERACTION_SESSION_FLAG, resutl_map);
            }

          close(c_con, null, null);
        } catch (SQLException ex) {
        } finally {
            out.close();
            close(c_con, null, null);
        }
    }

    private Getfromremote startExtraction(Connection c_con,String dirtouse, String site, String user, String pass) {
        Getfromremote get = new Getfromremote(c_con,site, user, pass, dirtouse, false);
        (new Thread(get)).start();
        return get;

    }

    private LinkedHashMap<String, ArrayList<String>> getResults(Getfromremote get, LinkedHashMap<String, ArrayList<String>> result_map, HttpSession ses) {

        if (get != null) {
            ses.removeAttribute("getfromremote_runner");
            LinkedHashMap<String, ArrayList<String>> new_result_map = get.getResults();

            if (result_map == null) {
                result_map = new LinkedHashMap<>();
            }
            ArrayList<String> key_l = new ArrayList<>(result_map.keySet());
            while (key_l.size() > MAX_RESULTS_TO_KEEP) {
                result_map.remove(key_l.remove(0));
            }

            ArrayList<String> new_key_l = new ArrayList<>(new_result_map.keySet());
            for (int i = 0; i < new_key_l.size(); i++) {
                result_map.put(new_key_l.get(i), new_result_map.get(new_key_l.get(i)));
            }
        }
        return result_map;
    }

    public String encrypt(String in_key, String value) {
        try {
            byte key[] = in_key.getBytes();
            DESKeySpec desKeySpec = new DESKeySpec(key);
            SecretKeyFactory keyFactory;
            keyFactory = SecretKeyFactory.getInstance("DES");
            SecretKey secretKey = keyFactory.generateSecret(desKeySpec);
            Cipher cipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            return new String(cipher.doFinal(value.getBytes("ISO-8859-1")), "ISO-8859-1");
        } catch (NoSuchAlgorithmException ex) {
        } catch (InvalidKeyException ex) {
        } catch (InvalidKeySpecException ex) {
        } catch (NoSuchPaddingException ex) {
        } catch (GeneralSecurityException ex) {
        } catch (UnsupportedEncodingException ex) {
        }
        return value;
    }

    public String decrypt(String in_key, String encrypted) {
        try {
            byte key[] = in_key.getBytes();
            DESKeySpec desKeySpec = new DESKeySpec(key);
            SecretKeyFactory keyFactory;
            keyFactory = SecretKeyFactory.getInstance("DES");
            SecretKey secretKey = keyFactory.generateSecret(desKeySpec);
            Cipher cipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, secretKey);
            return new String(cipher.doFinal(encrypted.getBytes("ISO-8859-1")), "ISO-8859-1");
        } catch (NoSuchAlgorithmException ex) {
        } catch (InvalidKeyException ex) {
        } catch (InvalidKeySpecException ex) {
        } catch (NoSuchPaddingException ex) {
        } catch (GeneralSecurityException ex) {
        } catch (UnsupportedEncodingException ex) {
        }
        return encrypted;
    }

    private int getDecriptKey() {

        int lo = 10000000;
        int hi = 99999999;
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
