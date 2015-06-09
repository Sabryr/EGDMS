/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package no.ntnu.medisn.egenvar;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLEncoder;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
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
public class Update2 extends HttpServlet {

    private DataSource dataSource_update;
    private DataSource dataSource;

    private void pageReload(HttpSession ses, HttpServletResponse response, String url, int caller) {
        try {
            if (!response.isCommitted()) {
                response.sendRedirect(url);
            } else {
                System.out.println("26 " + caller + " Refresh failed");
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
        PrintWriter out = response.getWriter();
        String current_tbl_nm = null;
        String id_list = null;
        Map<String, String> entered_values_map = null;
        Connection c_con = null;
        Connection c_con_update = null;
        try {
            c_con = getDatasource().getConnection();
            if (ses.getAttribute("ATRB_LOAD") == null) {
                pageReload(ses, response, Constants.getServerName(c_con) + "index", 70);
            } else {
                if (ses.getAttribute("entered_values_map") != null) {
                    entered_values_map = (Map<String, String>) ses.getAttribute("entered_values_map");
                } else {
                    entered_values_map = new HashMap<>();
                }
                Map<String, String[]> entered_values_map_tmp = request.getParameterMap();
                if (entered_values_map_tmp != null && !entered_values_map_tmp.isEmpty()) {
                    for (String c_var : entered_values_map_tmp.keySet()) {
//                        System.out.println("80 c_var=" + c_var + "  " + Arrays.deepToString(entered_values_map_tmp.get(c_var)));
                        if (c_var != null && !c_var.isEmpty() && c_var.startsWith(Constants.TABLE_TO_USE_FLAG)) {
                            id_list = entered_values_map_tmp.get(c_var)[0];
                            if (id_list.matches("[0-9\\,]+")) {
                                current_tbl_nm = Constants.get_correct_table_name(c_con, c_var.replace(Constants.TABLE_TO_USE_FLAG, ""));
                                ses.setAttribute("update_current_tbl_nm", current_tbl_nm);
                                ses.setAttribute("update_id_list", id_list);
                                entered_values_map.clear();
                            }
                        } else {
                            entered_values_map.put(c_var, entered_values_map_tmp.get(c_var)[0]);
                        }
                    }
                }
                String value_returned_to_col = null;
                if (ses.getAttribute(Constants.VALUES_RETURN_TO_COLUMN_FLAG) != null) {
                    value_returned_to_col = (String) ses.getAttribute(Constants.VALUES_RETURN_TO_COLUMN_FLAG);
                }
//                System.out.println("100 value_returned_to_col=" + value_returned_to_col);
                ses.setAttribute("entered_values_map", entered_values_map);
                if (current_tbl_nm == null && ses.getAttribute("update_current_tbl_nm") != null) {
                    current_tbl_nm = (String) ses.getAttribute("update_current_tbl_nm");
                }
                if (id_list == null && ses.getAttribute("update_id_list") != null) {
                    id_list = (String) ses.getAttribute("update_id_list");
                }
                out.println("<!DOCTYPE html>");
                out.println("<html>");
                out.println("<head>");
                out.println("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=ISO-8859-1\">");
                out.println(" <link HREF=\"" + Constants.getServerName(c_con) + "resources/egenStyler.css\"  REL=\"stylesheet\" TYPE=\"text/css\" >");
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
                out.println("<h1>Update</h1>");
                out.println(ses.getAttribute("Header"));
                c_con_update = getDatasource_update().getConnection();
                reTreiveFromDatabase(c_con, c_con_update, ses, out, current_tbl_nm, id_list, entered_values_map, value_returned_to_col);
                out.println("</body>");
                out.println("</html>");
            }

            close(c_con, null, null);
            close(c_con_update, null, null);
        } catch (SQLException ex) {
        } finally {
            out.close();
            close(c_con_update, null, null);
            close(c_con, null, null);
        }
    }

    private void reTreiveFromDatabase(Connection c_con, Connection c_con_update, HttpSession ses, PrintWriter out, String current_tbl_nm, String id_list,
            Map<String, String> entered_values_map, String value_returned_to_col) {
        try {
            current_tbl_nm = Constants.get_correct_table_name(c_con_update, current_tbl_nm);
            if (out != null && current_tbl_nm != null && id_list != null) {
                id_list = id_list.replaceAll("\\s", "");
                ArrayList<String> column_l = Constants.getColumn_names(c_con,current_tbl_nm);
                HashMap<String, String[]> frgn_key_map = Constants.get_key_contraints(c_con,current_tbl_nm);
                HashMap<String, String> cur_val_map = new HashMap<>();
                out.println("<form accept-charset=\"ISO-8859-1\"  action=\"" + Constants.getServerName(c_con) + "Upload/SubmitToDb\" method=\"post\">");
                out.println("  <table  border=\"1\">");
                if (id_list.matches("[0-9]+")) {
                    String details_q = "SELECT * FROM " + current_tbl_nm + " WHERE id=" + id_list;
                    Statement st = null;
                    ResultSet rs = null;
                    try {
                        c_con_update = getDatasource_update().getConnection();
                        st = c_con_update.createStatement();
                        rs = st.executeQuery(details_q);
                        if (rs.next()) {
                            for (int i = 0; i < column_l.size(); i++) {
                                String cln_nm = column_l.get(i);
                                String c_val = rs.getString(cln_nm);
                                if (c_val != null) {
                                    cur_val_map.put(cln_nm, c_val);
                                }
                            }
                        }
                        rs.close();
                        st.close();

                    } catch (SQLException ex) {
                    } catch (Exception ex) {
                    } finally {
                        close(null, st, rs);
                    }
                }

                if (!column_l.isEmpty()) {
                    for (int i = 0; i < column_l.size(); i++) {
                        String clmnnm = column_l.get(i);
                        String value = "";
                        if (cur_val_map.containsKey(clmnnm)) {
                            value = cur_val_map.get(clmnnm);
                        }
                        out.println("<tr>");
                        if (clmnnm.equalsIgnoreCase("id")) {
                            out.println("<td>" + clmnnm + "</td><td colspan=\"2\"><input type=\"text\" readonly=\"readonly\" size=\"100\" name=\"" + clmnnm + "\" value=\"" + id_list + "\"></td>");
                        } else if (Constants.getType(c_con,current_tbl_nm, clmnnm) == java.sql.Types.TIMESTAMP) {
                            out.println("<td>" + clmnnm + "</td><td colspan=\"2\"><input type=\"text\" readonly=\"readonly\" size=\"75\" name=\"" + clmnnm + "\" value=\"" + value + "\"></td>");
                        } else if (frgn_key_map.containsKey(clmnnm)) {
                            System.out.println("181 value_returned_to_col=" + value_returned_to_col);
                            String ref_tbl = frgn_key_map.get(clmnnm)[1];
                            String key2mathch = getKey(entered_values_map.keySet(), ref_tbl);

                            if (key2mathch != null && (value_returned_to_col == null || value_returned_to_col.equalsIgnoreCase(clmnnm))) {
                                value = entered_values_map.get(key2mathch);
                            }
                            String value_retunr_link = Constants.getServerName(c_con) + "Edit/Update2";
                            value_retunr_link = URLEncoder.encode(value_retunr_link, "ISO-8859-1");

                            String link = " (<a href=\"" + Constants.getServerName(c_con) + "Search/SearchResults3?" + Constants.TABLE_TO_USE_FLAG + ref_tbl + "=" + Constants.GET_ALL_FLAG + "&" + Constants.VALUES_RETURN_TO_COLUMN_FLAG + "=" + clmnnm + "&return_to_link=" + value_retunr_link + "&reset=true\">Change</a>. If no value spesified, the old value will be kept unchanged)";

                            out.println("<td>" + clmnnm + "</td><td colspan=\"2\">" + "<input readonly=\"readonly\" type=\"text\" size=\"10\" name=\"" + clmnnm + "\" value=\"" + value + "\">" + link + "</td>");
                        } else {
                            out.println("<td>" + clmnnm + "</td><td colspan=\"2\"><input type=\"text\" size=\"75\" name=\"" + clmnnm + "\" value=\"" + value + "\"></td>");
                        }
                    }

                } else {
                    out.println("<tr><td><error>Empty result for " + current_tbl_nm + " table </error></td><td>");
                }

                out.println("<tr><td></td><td>");
                out.println("<a href=\"" + Constants.getServerName(c_con) + "Edit/table_selection?operation=update&value_return_to_page=" + Constants.getServerName(c_con) + "Edit/Update\">Cancel</a> ");
                out.println("<input type=\"hidden\" name=\"operation\" value=\"update_batch\" />");
                out.println("<input type=\"hidden\" name=\"current_tbl_nm\" value=\"" + current_tbl_nm + "\" />");
                out.println("<input type=\"hidden\" name=\"id_list\" value=\"" + id_list + "\" />");
                out.println(" | <input type=\"submit\" name=\"submitbtn\" value=\"Submit changes\">");
                out.println("</td></tr>");

                out.println("  </table>");
                out.println("</form>");

            } else {
                System.out.println("Error opening connection or current table was null current_tbl_nm=" + current_tbl_nm);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getKey(Set in_set, String key) {
        String result = null;
        if (in_set.contains(key)) {
            result = key;
        } else {
            ArrayList<String> tmp_l = new ArrayList<>(in_set);
            for (int i = 0; (i < tmp_l.size() && result == null); i++) {
                if (tmp_l.get(i).equalsIgnoreCase(key)) {
                    result = tmp_l.get(i);
                }
            }
            for (int i = 0; (i < tmp_l.size() && result == null); i++) {
                if ((tmp_l.get(i).toUpperCase()).endsWith("." + key.toUpperCase())) {
                    result = tmp_l.get(i);
                }
            }
            for (int i = 0; (i < tmp_l.size() && result == null); i++) {
                if ((key.toUpperCase()).endsWith("." + tmp_l.get(i).toUpperCase())) {
                    result = tmp_l.get(i);
                }
            }
        }
        return result;

    }

    private DataSource getDatasource_update() throws ServletException {

        if (dataSource_update == null) {
            try {
                Context env = (Context) new InitialContext().lookup("java:comp/env");
                dataSource_update = (DataSource) env.lookup(Constants.DATASOURCE_NAME_DATA_UPDATE);
                if (dataSource_update == null) {
                    throw new ServletException("`" + Constants.DATASOURCE_NAME_DATA_UPDATE + "' is an unknown DataSource");
                }
            } catch (NamingException e) {
                throw new ServletException(e);
            }
        }
        try {
            System.out.println("270 " + dataSource_update.getConnection().getMetaData().getURL());
        } catch (SQLException ex) {
        }
        return dataSource_update;

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
