package no.ntnu.medisn.egenvar;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLEncoder;
import java.sql.*;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;

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
public class Update_batch extends HttpServlet {

    private DataSource dataSource_update;
    private DataSource dataSource;
    private HashMap<String, String> forign_key_to_table_m;
    private final static String CONSTRAINT_REFERENCE_PATTERN = ".*CONSTRAINT.*FOREIGN KEY[^\\(]+\\([^A-Za-z0-9_\\s]{1}([A-Za-z0-9_\\s]+).*REFERENCES[\\s]*[^A-Za-z0-9_\\s]{1}([A-Za-z0-9_\\s]+)[^A-Za-z0-9_\\s]{1}.*\\([^A-Za-z0-9_\\s]{1}(.*)[^A-Za-z0-9_\\s]{1}\\).*";
    private Pattern ptn_1;
    private ArrayList<String> atributes_to_clear_l;

    /**
     * To do Alow replace value block checksum change for files
     *
     * /
     * /**
     * Processes requests for both HTTP
     * <code>GET</code> and
     * <code>POST</code> methods.
     * "+Constants.getServerName()+"Edit/Update?current_tbl_nm=report&serachType=id&updatesearchvalue=7
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    private void init_Update() {
        try {
            getDatasource_update();
            if (atributes_to_clear_l == null) {
                atributes_to_clear_l = new ArrayList<String>();
                atributes_to_clear_l.add("entered_values_map");
                atributes_to_clear_l.add("current_tbl_nm");
                atributes_to_clear_l.add("serachType");
            }
            if (forign_key_to_table_m == null) {
                forign_key_to_table_m = Constants.getForeingKeyMap();
            }
        } catch (ServletException ex) {
            ex.printStackTrace();
        }
        ptn_1 = Pattern.compile(CONSTRAINT_REFERENCE_PATTERN);


    }

    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=ISO-8859-1");
        init_Update();
        HttpSession session = request.getSession(true);
        PrintWriter out = response.getWriter();
        Connection c_con = null;
        Connection c_con_update = null;
        try {
            c_con = getDatasource().getConnection();
            if (request.getParameter("reload") != null && request.getParameter("reload").equalsIgnoreCase("true")) {
                Enumeration sess_var_nms = session.getAttributeNames();
                while (sess_var_nms.hasMoreElements()) {
                    String tmp_nm = (String) sess_var_nms.nextElement();
                    if (atributes_to_clear_l.contains(tmp_nm)) {
                        session.removeAttribute(tmp_nm);
                    }
                }
            }
            String current_tbl_nm = null;
            if (request.getParameter("current_tbl_nm") != null) {
                current_tbl_nm = request.getParameter("current_tbl_nm");
                session.setAttribute("up_current_tbl_nm", current_tbl_nm);
            } else {
                if (session.getAttribute("up_current_tbl_nm") != null) {
                    current_tbl_nm = (String) session.getAttribute("up_current_tbl_nm");
                }
            }
            String id_list = "-1";
            if (request.getParameter("id_list") != null) {
                id_list = request.getParameter("id_list");
                session.setAttribute("id_list", id_list);
            } else {
                if (session.getAttribute("id_list") != null) {
                    id_list = (String) session.getAttribute("id_list");
                }
            }
            String value_return_to_page = Constants.getServerName(c_con) + "Edit/table_selection?operation=update&value_return_to_page="
                    + Constants.getServerName(c_con) + "Edit/Update";
            if (request.getParameter("value_return_to_page") != null) {
                value_return_to_page = request.getParameter("value_return_to_page");
                session.setAttribute("value_return_to_page", value_return_to_page);
            }
            HashMap<String, String[]> entered_values_map = new HashMap<>();
            if (session.getAttribute("entered_values_map") != null) {
                entered_values_map = (HashMap<String, String[]>) session.getAttribute("entered_values_map");
            }
            entered_values_map.putAll(request.getParameterMap());
            session.setAttribute("entered_values_map", entered_values_map);


            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=ISO-8859-1\">");
            out.println("<link HREF=\"" + Constants.getServerName(c_con) + "resources/egenStyler.css\"  REL=\"stylesheet\" TYPE=\"text/css\" >");
            out.println("<title>Edit and update </title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<p>" + session.getAttribute("Header") + "</p>");
            out.println("<h1> Edit and update </h1>");
            c_con_update = getDatasource_update().getConnection();
            reTreiveFromDatabase(c_con, c_con_update, out, entered_values_map, current_tbl_nm, value_return_to_page, id_list, session);
            out.println("</body>");
            out.println("</html>");
            close(c_con, null, null);
            close(c_con_update, null, null);
        } catch (SQLException ex) {
        } finally {
            out.close();
            close(c_con_update, null, null);
            close(c_con, null, null);
        }
    }

    private void reTreiveFromDatabase(Connection c_con, Connection c_con_update, PrintWriter out, HashMap<String, String[]> entered_values_map,
            String current_tbl_nm, String value_return_to_page, String id_list, HttpSession session) {
        try {

            if (c_con_update.isClosed() && out != null && current_tbl_nm != null) {
                out.println("<form accept-charset=\"ISO-8859-1\"  action=\"" + Constants.getServerName(c_con) + "Upload/SubmitToDb\" method=\"post\">");
                out.println("<table border=\"1\" cellpadding=\"0\" cellspacing=\"1\" width=\"100%\">");
                ArrayList<String> column_l = Constants.getColumn_names(c_con_update,current_tbl_nm);
                out.println("<tbody><tr>");
                out.println("<td>Table updated</td><td colspan=\"2\"><input type=\"text\" readonly=\"readonly\" name=\"table_nm\" value=\"" + current_tbl_nm + "\"></td>");
                out.println("</tr>");
                HashMap<String, String[]> frgn_key_map = Constants.get_key_contraints(c_con_update,current_tbl_nm);
                if (!column_l.isEmpty()) {
                    for (int i = 0; i < column_l.size(); i++) {
                        out.println("<tr>");
                        String clmnnm = column_l.get(i);
                        if (!clmnnm.equalsIgnoreCase("name") && Constants.getType(c_con_update,current_tbl_nm, clmnnm) != java.sql.Types.TIMESTAMP) {
                            if (clmnnm.equalsIgnoreCase("id")) {
                                out.println("<td>" + clmnnm + "</td><td colspan=\"2\"><input type=\"text\" readonly=\"readonly\" size=\"100\" name=\"" + clmnnm + "\" value=\"" + id_list + "\"></td>");
                            } else {
                                String value = "";
                                if (entered_values_map != null && entered_values_map.containsKey("value_returned_to_col") && entered_values_map.get("value_returned_to_col") != null && entered_values_map.get("value_returned_to_col")[0].equals(clmnnm)) {
                                    if (entered_values_map.containsKey("searchvalue")) {
                                        value = entered_values_map.get("searchvalue")[0];
                                    }

                                } else {
                                    if (!clmnnm.equalsIgnoreCase("id") && entered_values_map != null && entered_values_map.containsKey(clmnnm)) {
                                        value = entered_values_map.get(clmnnm)[0];
                                    }
                                }
                                entered_values_map.put(clmnnm, new String[]{value});
                                if (frgn_key_map.containsKey(clmnnm)) {
                                    String ref_tbl = frgn_key_map.get(clmnnm)[1];
//                                    String sql = "SELECT * FROM " + ref_tbl;
//                                    sql = URLEncoder.encode(sql, "ISO-8859-1");
                                    String link = " (<a href=\"" + Constants.getServerName(c_con) + "Search/SearchResults3?return_to_link="
                                            + Constants.getServerName(c_con) + "Edit/Update_batch?\">Change</a>). If no value spesified, the old value will be kept unchanged";

                                    out.println("<td>" + clmnnm + "</td><td colspan=\"2\">" + "<input readonly=\"readonly\" type=\"text\" size=\"10\" name=\"" + clmnnm + "\" value=\"" + value + "\">" + link + "</td>");
                                } else {
                                    out.println("<td>" + clmnnm + "</td><td colspan=\"2\"><input type=\"text\" size=\"75\" name=\"" + clmnnm + "\" value=\"\"></td>");

                                }
                            }
                            out.println("</tr>");
                        }

                    }
                }
                session.setAttribute("entered_values_map", entered_values_map);
                out.println("<tr><td></td><td>");
                out.println("<a href=\"" + Constants.getServerName(c_con) + "Edit/table_selection?operation=update&value_return_to_page="
                        + Constants.getServerName(c_con) + "Edit/Update\">Cancel</a> ");
                out.println("<input type=\"hidden\" name=\"operation\" value=\"update_batch\" />");
                out.println("<input type=\"hidden\" name=\"current_tbl_nm\" value=\"" + current_tbl_nm + "\" />");
                out.println("<input type=\"hidden\" name=\"id_list\" value=\"" + id_list + "\" />");
                out.println("<input type=\"hidden\" name=\"value_return_to_page\" value=\"" + value_return_to_page + "\" />");

                out.println(" | <input type=\"submit\" name=\"submitbtn\" value=\"Submit changes\">");
                out.println("</td></tr>");
                out.println("  </tbody></table>");
                out.println("</form>");

            } else {
                System.out.println("Error opening connection or current table was null current_tbl_nm=" + current_tbl_nm);
            }
        } catch (SQLException ex) {  //UnsupportedEncodingException ex
            ex.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
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

    private DataSource getDatasource_update() throws ServletException {
        if (dataSource_update == null) {
            try {
                Context env = (Context) new InitialContext().lookup("java:comp/env");
                dataSource_update = (DataSource) env.lookup(Constants.DATASOURCE_NAME_DATA_CREATE);
                if (dataSource_update == null) {
                    throw new ServletException("`" + Constants.DATASOURCE_NAME_DATA_CREATE + "' is an unknown DataSource");
                }
            } catch (NamingException e) {
                throw new ServletException(e);
            }
        }
        return dataSource_update;
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
