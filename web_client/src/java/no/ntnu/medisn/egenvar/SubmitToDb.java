package no.ntnu.medisn.egenvar;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
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
public class SubmitToDb extends HttpServlet {

    private final static String DATASOURCE_UPDATE_NAME = "egen_dataUpdate_resource";
    private final static String DATASOURCE_INSERT_NAME = "egen_dataEntry_resource";
    private DataSource dataSource_update;
    private DataSource dataSource_entry;
    private DataSource dataSource;
    private ArrayList<String> char_l;
    private static HashMap<String, String> chartosymb_map;
    private HashMap<String, String[]> suplVal_probps_map;
    public static HashMap<String, String> config_map;

//    private PrintWriter out;
//    private String value_return_to_page;
//    private String recent_home = ""+Constants.getServerName()+"";
    private void egen_init() {
        if (config_map == null) {
            config_map = new HashMap<>();
            config_map.put("MEMORIZING_REQUESTED", "1");
        }

        if (chartosymb_map == null || chartosymb_map.isEmpty()) {
            chartosymb_map = Constants.getchar2symbolMap();
        }
        if (char_l == null) {
            char_l = new ArrayList(chartosymb_map.keySet());
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
    protected void processRequest(HttpServletRequest request, HttpServletResponse response, String cmethod) throws ServletException, IOException {
        response.setContentType("text/html;charset=ISO-8859-1");
        egen_init();
        PrintWriter out = response.getWriter();
        Connection c_con_update = null;
        Connection c_con_entry = null;
        Connection c_con = null;

        try {
            c_con = getDatasource().getConnection();
            Map<String, String[]> entered_values_map_tmp = request.getParameterMap();
            String current_tbl_nm = request.getParameter("current_tbl_nm");
            String operation = request.getParameter("operation");
            String priviois_tbl_nm = request.getParameter("priviois_tbl_nm");
            String value_return_to_page = request.getParameter("value_return_to_page");
            String id_list = request.getParameter("id_list");
            if (value_return_to_page == null || value_return_to_page.equalsIgnoreCase("null") || value_return_to_page.isEmpty()) {
                value_return_to_page = "" + Constants.getServerName(c_con) + "";
            }
            String method = request.getMethod();
//        ArrayList<String> _k = new ArrayList<String>(entered_values_map_tmp.keySet());

            getDatasource_upate();
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\">");
            out.println("<link HREF=\"" + Constants.getServerName(c_con) + "resources/egenStyler.css\"  REL=\"stylesheet\" TYPE=\"text/css\" >");
            out.println("<title>Servlet Submit</title>");
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
            out.println("<h1> Submit </h1>");
            if (method.equalsIgnoreCase("GET")) {
                if (operation != null && operation.equalsIgnoreCase("update")) {
                    c_con_update = getDatasource_upate().getConnection();
                    updaterecord(c_con_update, out, current_tbl_nm, entered_values_map_tmp, false, session);
                } else {
                    out.println("<p>Error at get: Undefined operation , Click <a href=\"" + value_return_to_page + "\"> here </a>to go back </p>");
                }

            } else if (method.equalsIgnoreCase("POST")) {
                if (operation != null && operation.equalsIgnoreCase("update")) {
                    c_con_update = getDatasource_upate().getConnection();
                    updaterecord(c_con_update, out, current_tbl_nm, entered_values_map_tmp, true, session);
                } else if (operation != null && operation.equalsIgnoreCase("update_batch")) {
                    c_con_update = getDatasource_upate().getConnection();
                    updaterecord_batch(c_con_update, out, current_tbl_nm, entered_values_map_tmp, true, true, id_list, value_return_to_page);
                } else if (operation != null && operation.equalsIgnoreCase("insert")) {
                    c_con_entry = getDatasource_entry().getConnection();
                    insertRecord(c_con_entry, out, current_tbl_nm, priviois_tbl_nm, entered_values_map_tmp, true, value_return_to_page, session, request);
                    if ((current_tbl_nm.split("\\.")[current_tbl_nm.split("\\.").length - 1]).equalsIgnoreCase("person")) {
                        Constants.loadSessionVariables(c_con, request);
                    }
                } else if (operation != null && operation.equalsIgnoreCase("insert_batch")) {
                    insertBatch(c_con_entry, out, current_tbl_nm, entered_values_map_tmp);
                    if ((current_tbl_nm.split("\\.")[current_tbl_nm.split("\\.").length - 1]).equalsIgnoreCase("person")) {
                        Constants.loadSessionVariables(c_con, request);
                    }
                } else {
                    out.println("<p>Error at post: Undefined operation , Click <a href=\"" + value_return_to_page + "\"> here </a>to go back </p>");
                }
            }
            out.println("</body>");
            out.println("</html>");
            close(c_con, null, null);
            close(c_con_update, null, null);
            close(c_con_entry, null, null);
        } catch (SQLException ex) {
        } finally {
            out.close();
            close(c_con, null, null);
            close(c_con_update, null, null);
            close(c_con_entry, null, null);
        }
//        System.out.println("SubmitToDb " + current_tbl_nm + "  -- " + value_return_to_page+" value_return_to_column="+value_return_to_column);
    }

    private String refineentry(String intext, boolean decod) {
        if (intext != null) {
        } else {
            if (decod) {
                try {
                    intext = URLDecoder.decode(intext, "ISO-8859-1");
                } catch (UnsupportedEncodingException ex) {
                    ex.printStackTrace();
                }
            }

            if (intext != null) {
                intext = intext.trim();
                if (intext.contains("\n")) {
                    String[] lineSplit = intext.split("\n");
                    intext = lineSplit[0].trim();
                    for (int i = 1; i < lineSplit.length; i++) {
                        lineSplit[i] = lineSplit[i].trim();
                        if (!lineSplit[i].isEmpty()) {
                            intext = intext + " " + lineSplit[i];
                        }
                    }
                }
                if (intext != null && !intext.isEmpty() && !intext.matches("[0-9A-Za-z_\\-)\\(\\s\\,\\./@:;#\\]\\[\\*\\^\\+]+")) {
                    for (int i = 0; i < char_l.size(); i++) {
                        intext = intext.replaceAll(char_l.get(i), chartosymb_map.get(char_l.get(i)));
                    }
                    String to_log_intext = intext.replaceAll("[0-9A-Za-z_\\-)\\(\\s\\,\\./@:;#\\]\\[\\*\\^\\+\\&]+", "");
                    if (!to_log_intext.isEmpty()) {
                        char[] split = to_log_intext.toCharArray();
                        for (int i = 0; i < split.length; i++) {
                            System.out.println("Warning 1. Following non standard character was not transformed   <" + split[i] + ">   in " + intext + " \n");
                        }
                    }
                }
            }
        }

        return intext;

    }

    /*
     MethodId=SD2
     */
    private void updaterecord(Connection c_con_update, PrintWriter out, String current_tbl_nm, Map<String, String[]> entered_values_map_tmp,
            boolean commit, HttpSession session) {
        String update_stmt = "update " + current_tbl_nm;
        String params = "";
        String condition = " WHERE ";
        out.println("<form accept-charset=\"ISO-8859-1\"  method=\"post\">");
        out.println("<table border=\"1\" cellpadding=\"0\" cellspacing=\"1\" width=\"100%\">");
        String table_txt = "";
        try {
//            Connection ncon = getDatasource_upate().getConnection();
            if (current_tbl_nm != null) {
                String change_url = "" + Constants.getServerName(c_con_update) + "Edit/Update?";
//                Statement st_1 = ncon.createStatement();
//                String s_1 = "select * from " + current_tbl_nm + " limit 1";
//                ResultSet r_1 = st_1.executeQuery(s_1);
//                ResultSetMetaData rsmd = r_1.getMetaData();
                ArrayList<String> column_names = Constants.getColumn_names(c_con_update, current_tbl_nm);


//                st_1.close();
                int valuestoAdd = 0;

                for (int i = 0; i < column_names.size(); i++) {
                    table_txt = table_txt + "<tr>";
                    String col_nm = column_names.get(i);
                    String cvalue = null;
                    if (entered_values_map_tmp.containsKey(col_nm) && entered_values_map_tmp.get(col_nm) != null) {
                        cvalue = entered_values_map_tmp.get(col_nm)[0];
                    }
                    cvalue = refineentry(cvalue, true);
                    if (!col_nm.equalsIgnoreCase("id")) {
                        valuestoAdd++;
                        table_txt = table_txt + "<td>" + col_nm + "</td><td><input type=\"text\" readonly=\"readonly\" name=\"col_nm \" value=\"" + cvalue + "\"> </td>";
                        if (commit) {
                            if (cvalue == null || cvalue.equalsIgnoreCase("null")) {
                                if (valuestoAdd == 1) {
                                    params = params + " " + col_nm + "=null";
                                } else {
                                    params = params + "," + col_nm + "=null";
                                }
                            } else {
                                String quat = "'";
                                if (!Constants.shoulditbequated(c_con_update, current_tbl_nm, col_nm)) {
                                    quat = "";
                                }
                                if (valuestoAdd == 1) {
                                    params = params + " " + col_nm + "=" + quat + cvalue + quat;
                                } else {
                                    params = params + "," + col_nm + "=" + quat + cvalue + quat;
                                }
                            }
                        }
                    } else {
                        condition = condition + " " + col_nm + "=" + cvalue + "";
                    }
                    table_txt = table_txt + "</tr>";
                }
                ArrayList<String> key_l = new ArrayList<>(entered_values_map_tmp.keySet());

                for (int i = 0; i < key_l.size(); i++) {
                    if (entered_values_map_tmp.get(key_l.get(i)) != null) {
                        change_url = change_url + "&" + key_l.get(i) + "=" + entered_values_map_tmp.get(key_l.get(i))[0];
                    }
                }

                if (params != null) {
                    params = " SET " + params;
                    if (commit) {
                        update_stmt = update_stmt + params + condition;
                        try {
                            Statement st_1 = c_con_update.createStatement();
                            st_1.executeUpdate(update_stmt);
                            out.println("<tr><td colspan=\"2\"><h8>Update was successful. </h8><a href=\"" + Constants.getServerName(c_con_update)
                                    + "\"> Click here to continue</a> </td></tr>");
                            st_1.close();
                            session.removeAttribute(current_tbl_nm + "entered_values_map");

                            if (Constants.setConfig(c_con_update, config_map) < 0) {
                                System.out.println("277  membing set");
                            }
                        } catch (SQLException e) {
                            System.out.println("Error SD2a " + update_stmt);
                            out.println("<tr><td colspan=\"2\"> <error>Data insert failed SD2a</error> : " + e.getMessage() + "</p> <a href=\""
                                    + Constants.getServerName(c_con_update) + "\">Cancel</a> | <a href=\"" + change_url + "\">Change_values</a></td></tr>");
                        }
                    } else {
                        out.println("<tr><td colspan=\"2\"> <a href=\"" + Constants.getServerName(c_con_update)
                                + "\">Cancel</a> | <a href=\"" + change_url + "\">Change_values</a> | <input type=\"submit\" name=\"submitbtn\" value=\"Submit\"></td></tr>");
                    }
                } else {
                    out.println("<error>Error SD2c</error>");
                }

                out.println(table_txt);
            }
//            ncon.close();
//        } catch (SQLException e) {
//            out.println("<error>Error SD2e:" + e.getMessage() + "</error>");
        } catch (Exception e) {
            out.println("<error>Error SD2f:" + e.getMessage() + "</error>");
        }
        out.println("</table>");
        out.println("</form>");
    }

    /*
     MethodID=SD3
     */
    private void updaterecord_batch(Connection c_con_update, PrintWriter out, String current_tbl_nm, Map<String, String[]> entered_values_map_tmp,
            boolean commit, boolean replace, String id_list, String value_return_to_page) {
        String update_stmt = "update " + current_tbl_nm;
        String params = "";
        String condition = " WHERE ";
        out.println("<form accept-charset=\"ISO-8859-1\"  method=\"post\">");
        out.println("<table border=\"1\" cellpadding=\"0\" cellspacing=\"1\" width=\"100%\">");
        String table_txt = "";
//        try {
//            Connection ncon = getDatasource_upate().getConnection();
        if (current_tbl_nm != null) {
            String change_url = "" + Constants.getServerName(c_con_update) + "Edit/Update2?";
            ArrayList<String> column_names = Constants.getColumn_names(c_con_update, current_tbl_nm);
            int valuestoAdd = 0;
            for (int i = 0; i < column_names.size(); i++) {
                table_txt = table_txt + "<tr>";
                String col_nm = column_names.get(i);
                String cvalue = null;
                if (entered_values_map_tmp.containsKey(col_nm) && entered_values_map_tmp.get(col_nm) != null) {
                    cvalue = entered_values_map_tmp.get(col_nm)[0];
                }
                cvalue = refineentry(cvalue, false);
                if (cvalue != null && !cvalue.isEmpty()) {
                    if (!col_nm.equalsIgnoreCase("id")) {
                        valuestoAdd++;
                        table_txt = table_txt + "<td>" + col_nm + "</td><td><input type=\"text\" readonly=\"readonly\" name=\"col_nm \" value=\"" + cvalue + "\"> </td>";
                        if (commit) {
                            if (cvalue.isEmpty() || cvalue.equalsIgnoreCase("null")) {
                                if (valuestoAdd == 1) {
                                    params = col_nm + "=null";
                                } else {
                                    params = params + "," + col_nm + "=null";
                                }
                            } else {
                                String quat = "'";
                                if (!Constants.shoulditbequated(c_con_update, current_tbl_nm, col_nm)) {
                                    quat = "";
                                }
                                if (valuestoAdd == 1) {
                                    params = col_nm + "=" + quat + cvalue + quat;
                                } else {
                                    params = params + "," + col_nm + "=" + quat + cvalue + quat;
                                }
                            }
                        }
                    } else {
                        id_list = id_list.replaceAll("'", "");
                        condition = condition + " " + col_nm + " in (" + id_list + ")";
                    }
                }
                table_txt = table_txt + "</tr>";
            }
            ArrayList<String> key_l = new ArrayList<>(entered_values_map_tmp.keySet());

            for (int i = 0; i < key_l.size(); i++) {
                if (entered_values_map_tmp.get(key_l.get(i)) != null) {
                    change_url = change_url + "&" + key_l.get(i) + "=" + entered_values_map_tmp.get(key_l.get(i))[0];
                }
            }

            if (params != null) {
                params = " SET " + params;
                if (commit) {
                    update_stmt = update_stmt + params + condition;
                    try {
                        Statement st_1 = c_con_update.createStatement();
                        st_1.executeUpdate(update_stmt);
                        out.println("<tr><td colspan=\"2\"><h8>Update was successful. </h8><a href=\"" + value_return_to_page + "\"> Click here to continue</a> </td></tr>");
                        st_1.close();
                        if (Constants.setConfig(c_con_update, config_map) < 0) {
                            System.out.println("378  membing set");
                        }
                    } catch (SQLException e) {
                        System.out.println("Error SD3a " + update_stmt);
                        out.println("<tr><td colspan=\"2\"> <error>Data insert failed SD3a</error> : " + e.getMessage() + "</p> <a href=\""
                                + Constants.getServerName(c_con_update) + "\">Cancel</a> | <a href=\"" + change_url + "\">Change_values</a></td></tr>");
                    }
                } else {
                    out.println("<tr><td colspan=\"2\"> <a href=\"" + Constants.getServerName(c_con_update) + "\">Cancel</a> | <a href=\"" + change_url
                            + "\">Change_values</a> | <input type=\"submit\" name=\"submitbtn\" value=\"Submit\"></td></tr>");
                }
            } else {
                out.println("<error>Error SD3c</error>");
            }

            out.println(table_txt);
        }
//            if (ncon != null && !ncon.isClosed()) {
//                ncon.close();
//            }
//        } catch (SQLException e) {
//            out.println("<error>Error SD3e:" + e.getMessage() + "</error>");
//        } catch (ServletException e) {
//            out.println("<error>Error SD3f:" + e.getMessage() + "</error>");
//        }
        out.println("</table>");
        out.println("</form>");
    }

    /*
     MethodIs=SD1
     */
    private void insertRecord(Connection c_con, PrintWriter out, String current_tbl_nm, String previous_tbl_nm,
            Map<String, String[]> entered_values_map_tmp, boolean commit, String value_return_to_page,
            HttpSession session, HttpServletRequest request) {
        String insert_stmt = "insert into " + current_tbl_nm + "(";
        String refetch_stmt = "select * from " + current_tbl_nm + " where ";
        String params = " (";
        String refetch_param = "";

        Connection ncon = null;
        out.println("<table border=\"1\" cellpadding=\"0\" cellspacing=\"1\" width=\"100%\">");
        try {
            current_tbl_nm = Constants.get_correct_table_name(c_con, current_tbl_nm);
            ncon = getDatasource_entry().getConnection();
            if (current_tbl_nm != null) {
                ArrayList<String> auto_increment_col_l = Constants.getAutoIncrementolumn_names(c_con, current_tbl_nm);
                ArrayList<String> column_l = Constants.getColumn_names(c_con, current_tbl_nm);
//                Statement st_1 = ncon.createStatement();
//                String s_1 = "select * from " + current_tbl_nm;
//                ResultSet r_1 = st_1.executeQuery(s_1);
//                ResultSetMetaData rsmd = r_1.getMetaData();
//                int NumOfCol = rsmd.getColumnCount();
                int valuestoAdd = 0;
                for (int i = 0; i < column_l.size(); i++) {
                    String col_nm = column_l.get(i);
                    String quat = "'";
                    if (!Constants.shoulditbequated(c_con, current_tbl_nm, col_nm)) {
                        quat = "";
                    }
                    String col_nm_tr = getRealKey(entered_values_map_tmp.keySet(), col_nm);
                    if (col_nm_tr == null) {
                    } else if (col_nm_tr == null) {
                    } else if (getIgnoreCase(auto_increment_col_l, col_nm) != null) {
                    } else {
                        valuestoAdd++;
                        String cvalue = null;
                        if (entered_values_map_tmp.containsKey(col_nm_tr) && entered_values_map_tmp.get(col_nm_tr) != null) {
                            cvalue = entered_values_map_tmp.get(col_nm_tr)[0];
                        }
                        cvalue = refineentry(cvalue, true);
                        if (commit) {
                            if (cvalue == null || cvalue.isEmpty() || cvalue.equalsIgnoreCase("null")) {
                                if (valuestoAdd == 1) {
                                    insert_stmt = insert_stmt + col_nm;
                                    params = params + "null";
                                    refetch_param = col_nm + " is null";
                                } else {
                                    insert_stmt = insert_stmt + "," + col_nm;
                                    params = params + ",null";
                                    refetch_param = refetch_param + " and  " + col_nm + " is null ";
                                }
                            } else {
                                if (valuestoAdd == 1) {
                                    insert_stmt = insert_stmt + col_nm;
                                    params = params + quat + cvalue + quat;
                                    refetch_param = col_nm + "=" + quat + cvalue + quat;
                                } else {
                                    insert_stmt = insert_stmt + "," + col_nm;
                                    params = params + "," + quat + cvalue + quat;
                                    refetch_param = refetch_param + " and  " + col_nm + "=" + quat + cvalue + quat;
                                }
                            }

                        }
                    }
                }
//                r_1.close();
//                st_1.close();
                ArrayList<String> key_l = new ArrayList<>(entered_values_map_tmp.keySet());
                String change_url = "" + Constants.getServerName(c_con) + "Upload/CreateNew?current_tbl_nm=" + current_tbl_nm + "";

                for (int i = 0; i < key_l.size(); i++) {
                    if (entered_values_map_tmp.get(key_l.get(i)) != null) {
                        change_url = change_url + "&" + key_l.get(i) + "=" + entered_values_map_tmp.get(key_l.get(i))[0];
                    }
                }
                if (commit) {
                    params = params + ")";
                    insert_stmt = insert_stmt + ") values" + params + "";
                    try {
                        if (ncon.isClosed()) {
                            ncon = getDatasource_entry().getConnection();
                        }
                        Statement st_1 = ncon.createStatement();
                        int result = st_1.executeUpdate(insert_stmt);
                        st_1.close();
                        if (result > 0) {
                            session.removeAttribute("id2expandDetails_map" + current_tbl_nm);
                            out.println("<tr><td>");
                            out.println("<font color=\"green\">Record inserted</font>");
                            out.println("</td></tr>");
                            refetch_stmt = refetch_stmt + refetch_param;
                            if (ncon.isClosed()) {
                                ncon = getDatasource_entry().getConnection();
                            }
                            st_1 = ncon.createStatement();
                            ResultSet r_refetch = st_1.executeQuery(refetch_stmt);
                            int id = -1;
                            while (r_refetch.next()) {
                                id = r_refetch.getInt("id");
                            }
                            st_1.close();
                            ncon.close();
//                            System.out.println("1 value_return_to_page=" + value_return_to_page + " current_tbl_nm=" + current_tbl_nm + "\tprevious_tbl_nm=" + previous_tbl_nm);
                            out.println("<tr><td>");

                            if (previous_tbl_nm != null && !previous_tbl_nm.isEmpty() && !previous_tbl_nm.equalsIgnoreCase("null")) {
                                out.println("<form accept-charset=\"ISO-8859-1\"  method=\"post\" action=\"" + Constants.getServerName(c_con) + "Search/SearchResults3\">");
                                out.println("<input type=\"hidden\" value=\"" + id + "\" name=\"" + current_tbl_nm + "\" />");
                                out.println("<input type=\"hidden\" value=\"" + previous_tbl_nm + "\" name=\"current_tbl_nm\" />");
                                out.println("<input type=\"hidden\" value=\"true\" name=\"reload\" />");
                                out.println("<input type=\"hidden\" value=\"select\" name=\"operation\" />");
                                out.println("<input type=\"hidden\" value=\"" + id + "\" name=\"searchvalue\" />");

                                out.println("<input type=\"submit\" name=\"submitbtn\" value=\"Use new record\">");
                                out.println("</form>");
                            } else {
                                out.println("<a href=\"" + Constants.getServerName(c_con) + "\">Go back to home page</a> ");
                            }

                            out.println("</td></tr>");
                            if (Constants.setConfig(ncon, config_map) < 0) {
                            }
                        } else {
                            out.println("<tr><td>");
                            out.println("<error>Data insert failed</error>");
                            out.println("</td></tr>");
                        }



//                        out.println("<tr><td colspan=\"2\"><font color='green'>Data inserted.</font><a href=\"" + Constants.getServerName() + "Search/ResultsDisplay?query=" + refetch_stmt + "&reload=true&reconstructquery=false&current_tbl_nm=" + current_tbl_nm + "&operation=" + operation + "&priviois_tbl_nm=" + priviois_tbl_nm + "\"> Click here to view</a> </td></tr>");
                    } catch (SQLException e) {
                        System.out.println("423 " + insert_stmt);
//                        out.println("<p><font color='red'>Data insert failed</font> : " + e.getMessage() + "</p>");
                        out.println("<tr><td colspan=\"2\"> <error>Data insert failed 2 </error> : " + e.getMessage() + "</p> <a href=\""
                                + Constants.getServerName(c_con) + "\">Cancel</a> | <a href=\"" + change_url + "\">Change_values</a></td></tr>");

                    } finally {
                        ncon.close();
                    }
                } else {
                    out.println("<tr><td colspan=\"2\"> <a href=\"" + Constants.getServerName(c_con) + "\">Cancel</a> | <a href=\""
                            + change_url + "\">Change_values</a> | <input type=\"submit\" name=\"submitbtn\" value=\"Submit\"></td></tr>");
                }
            }
            ncon.close();
        } catch (SQLException e) {
            out.println("<error>Error: SD1a " + e.getMessage() + "</error>");
        } catch (ServletException ex) {
            out.println("<error>Error: SD1b " + ex.getMessage() + "</error>");
        } finally {
            try {
                if (ncon != null) {
                    ncon.close();
                }
            } catch (SQLException e) {
            }
        }
        out.println("</table>");
//        out.println("</form>");


    }

    private String getIgnoreCase(ArrayList<String> key_l, String key) {
        String realKey = null;
        for (int i = 0; (i < key_l.size() && realKey == null); i++) {
            if (key_l.get(i).equalsIgnoreCase(key)) {
                realKey = key_l.get(i);
            }
        }
        return realKey;
    }

    /*
     * Mthod id=STD58
     */
    private void insertBatch(Connection c_con_insert, PrintWriter out, String current_tbl_nm, Map<String, String[]> entered_values_map) {
        try {
            HashMap<String, ArrayList<String>> data_map = new HashMap<>();
            Connection ncon = getDatasource_entry().getConnection();
            String sql_with_values = null;
            if (current_tbl_nm != null) {
                String insert_sql = "INSERT INTO " + current_tbl_nm + " (";
                String column_names = null;
                ArrayList<String> autoincr_column_l = Constants.getAutoIncrementolumn_names(c_con_insert, current_tbl_nm);
                ArrayList<String> column_names_l = Constants.getColumn_names(c_con_insert, current_tbl_nm);
                for (int i = 0; i < column_names_l.size(); i++) {
                    if (getRealName(autoincr_column_l, column_names_l.get(i)) == null) {
                        String c_clmn = column_names_l.get(i);
                        data_map.put(c_clmn, new ArrayList<String>(1));
                        if (column_names == null) {
                            column_names = c_clmn;
                        } else {
                            column_names = column_names + "," + c_clmn;
                        }

                    } else {
                        column_names_l.remove(i);
                        i--;
                    }
                }
                insert_sql = insert_sql + column_names + ") values";
                int max_length = 0;
                if (!data_map.isEmpty() && !column_names_l.isEmpty()) {
                    ArrayList<String> entered_values_map_key_l = new ArrayList<>(entered_values_map.keySet());
                    for (int i = 0; i < column_names_l.size(); i++) {
                        String real_clmn = getRealName(entered_values_map_key_l, column_names_l.get(i));
                        if (real_clmn != null && entered_values_map.get(real_clmn) != null) {
                            String[] c_val_a = entered_values_map.get(real_clmn);
                            if (c_val_a != null && c_val_a.length > 0) {
                                c_val_a = c_val_a[0].split(Constants.BATCH_INSERT_SPLITER);
                            }
                            data_map.get(column_names_l.get(i)).addAll(Arrays.asList(c_val_a));
                            if (data_map.get(column_names_l.get(i)).size() > max_length) {
                                max_length = data_map.get(column_names_l.get(i)).size();
                            }
                        }
                    }
                    for (int i = 0; i < column_names_l.size(); i++) {
                        int fill_val = max_length - data_map.get(column_names_l.get(i)).size();
                        for (int j = 1; j <= fill_val; j++) {
                            if (data_map.get(column_names_l.get(i)) == null || data_map.get(column_names_l.get(i)).isEmpty()) {
                                data_map.get(column_names_l.get(i)).add(null);
                            } else {
                                data_map.get(column_names_l.get(i)).add(data_map.get(column_names_l.get(i)).get(0));
                            }
                        }
                    }

                    StringBuilder str = new StringBuilder();
                    str.ensureCapacity(4096);
                    for (int i = 0; i < max_length; i++) {
                        String quates = "'";
                        int type = Constants.getType(c_con_insert, current_tbl_nm, column_names_l.get(0));
                        if (type == Types.INTEGER || type == Types.DOUBLE) {
                            quates = "";
                        }
                        String c_val = "(" + quates + data_map.get(column_names_l.get(0)).get(i) + quates;

                        for (int j = 1; j < column_names_l.size(); j++) {
                            quates = "'";
                            type = Constants.getType(c_con_insert, current_tbl_nm, column_names_l.get(j));
                            if (type == Types.INTEGER || type == Types.DOUBLE) {
                                quates = "";
                            }
                            c_val = c_val + "," + quates + data_map.get(column_names_l.get(j)).get(i) + quates;
                        }
                        c_val = c_val + ")";
                        if (i == 0) {
                            str.append(c_val);
                        } else {
                            str.append("," + c_val);
                        }
                    }
                    sql_with_values = insert_sql + " " + str.toString();
                }
            }

            try {
                if (sql_with_values != null) {
                    if (ncon == null || ncon.isClosed()) {
                        ncon = getDatasource_entry().getConnection();
                    }
                    Statement st_1 = ncon.createStatement();
                    if (st_1.executeUpdate(sql_with_values) > 0) {
                        out.println("<success>All updates committed <a href=\"" + Constants.getServerName(c_con_insert) + "\"> click here to continue</a><success>");
                        if (Constants.setConfig(ncon, config_map) < 0) {
                        }
                    } else {
                        out.println("<error>Error STD58f: Failed to update table <error>");
                    }
                    st_1.close();
                } else {
                    out.println("<error>Error STD58g: Failed construct insert statement <error>");
                }

                if (ncon != null && !ncon.isClosed()) {
                    ncon.close();
                }
            } catch (SQLException ex) {
                out.println("<error>Error 58e: SQL error " + ex.getMessage() + " <error>");
            }
        } catch (SQLException e) {
            out.println("<error>Error 58k: SQL error " + e.getMessage() + " <error>");
        } catch (ServletException ex) {
            out.println("<error>Error 58f:Connection error " + ex.getMessage() + " <error>");
        } catch (Exception ex) {
            out.println("<error>Error 58g: " + ex.getMessage() + " <error>");
        }

    }

    private String getRealName(ArrayList<String> in_l, String in) {
        String result = null;
        if (in_l != null) {
            if (in_l.contains(in)) {
                result = in;
            }
            for (int i = 0; ((result == null) && i < in_l.size()); i++) {
                if (in_l.get(i).equalsIgnoreCase(in.trim())) {
                    result = in_l.get(i);
                }
            }
        }

        return result;
    }

    private String getRealKey(Set keys, String key) {
        ArrayList<String> key_l = new ArrayList<String>(keys);
        key = key.trim().toUpperCase();
        String realKey = null;
        for (int i = 0; (i < key_l.size() && realKey == null); i++) {
            String c_key = key_l.get(i).toUpperCase();
            if (c_key.equalsIgnoreCase(key)) {
                realKey = key_l.get(i);
            }
        }
        return realKey;
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

    private DataSource getDatasource_upate() throws ServletException {
        if (dataSource_update == null) {
            try {
                Context env = (Context) new InitialContext().lookup("java:comp/env");
                dataSource_update = (DataSource) env.lookup(DATASOURCE_UPDATE_NAME);
                if (dataSource_update == null) {
                    throw new ServletException("`" + DATASOURCE_UPDATE_NAME + "' is an unknown DataSource");
                }
            } catch (NamingException e) {
                throw new ServletException(e);
            }
        }
//        try {
//            System.out.println("844 " + dataSource_update.getConnection().getMetaData().getURL());
//        } catch (SQLException ex) {
//        }
        return dataSource_update;
    }

    private DataSource getDatasource_entry() throws ServletException {
        if (dataSource_entry == null) {
            try {
                Context env = (Context) new InitialContext().lookup("java:comp/env");
                dataSource_entry = (DataSource) env.lookup(DATASOURCE_INSERT_NAME);
                if (dataSource_entry == null) {
                    throw new ServletException("`" + DATASOURCE_INSERT_NAME + "' is an unknown DataSource");
                }
            } catch (NamingException e) {
                throw new ServletException(e);
            }
        }
//        try {
//            System.out.println("865 " + dataSource_entry.getConnection().getMetaData().getURL());
//        } catch (SQLException ex) {
//        }
        return dataSource_entry;
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
        processRequest(request, response, "get");


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
        processRequest(request, response, "post");


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
