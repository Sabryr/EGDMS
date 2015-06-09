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
import java.util.HashMap;
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
public class DeleteRecord extends HttpServlet {

    private DataSource dataSource;
//    private ArrayList<String> column_nm_l;
//    private ArrayList<ArrayList<String>> data_l;
//    private ArrayList<String> toDelete_sql_l;
    private int rowsPerPage = 5;
    private Pattern ptn_1;
    private final static String CONSTRAINT_REFERENCE_PATTERN = ".*CONSTRAINT.*FOREIGN KEY[^\\(]+\\([^A-Za-z0-9_\\s]{1}([A-Za-z0-9_\\s]+).*REFERENCES[\\s]*[^A-Za-z0-9_\\s]{1}([A-Za-z0-9_\\s]+)[^A-Za-z0-9_\\s]{1}.*\\([^A-Za-z0-9_\\s]{1}(.*)[^A-Za-z0-9_\\s]{1}\\).*";
    private final String REPLACEWITH_FALG = "_REP_";
    private final int MAX_DEPENDENT_ROWS = 2;

    /**
     * Processes requests for both HTTP
     * <code>GET</code> and
     * <code>POST</code> methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs TO Do : need to optimize
     * getDependancies
     */
    private void myInit() {
        if (ptn_1 == null) {
            ptn_1 = Pattern.compile(CONSTRAINT_REFERENCE_PATTERN);
        }
//        ptn_2 = Pattern.compile(CONSTRAINT_UNIQNESS_PATTERN);
    }

    /*
     * Method id=4
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=ISO-8859-1");
        if (ptn_1 == null) {
            myInit();
        }
        PrintWriter out = response.getWriter();
        Connection c_con = null;
        try {
            HttpSession session = request.getSession(true);
            c_con = getDatasource().getConnection();
            int user_level = 9;
            if (session.getAttribute("user_level") != null && session.getAttribute("user_level").toString().matches("[0-9]+")) {
                user_level = new Integer(session.getAttribute("user_level").toString());
            }
            if (user_level > 7) {
                response.sendRedirect(Constants.getServerName(c_con) + "errormsgs?error=403");
            } else {
                String source_page = "" + Constants.getServerName(c_con) + "";
                if (request.getParameter("source_page") != null && !request.getParameter("source_page").isEmpty()) {
                    source_page = request.getParameter("source_page");
                }
                String current_tbl_nm = request.getParameter("current_tbl_nm");

                String resethistory = request.getParameter("resethistory");
                if (resethistory != null && resethistory.equalsIgnoreCase("true")) {
                    session.removeAttribute("toDelete_sql_l");
                    session.removeAttribute("column_nm_l");
                }
                int cMinid = 0;
                int cmaxid = 0;
                String cmin = request.getParameter("cminid");
                if (cmin != null && cmin.matches("[0-9]+")) {
                    cMinid = new Integer(cmin);
                }
                String cmax = request.getParameter("cmaxid");
                if (cmax != null && cmax.matches("[0-9]+")) {
                    cmaxid = new Integer(cmax);
                }
                if (cmaxid < 1) {
                    cmaxid = rowsPerPage;
                }
                String selected_id_List = request.getParameter("selected_id_List");
                if (selected_id_List != null) {
                    selected_id_List = selected_id_List.replaceAll("',,'", "''");
                    selected_id_List = selected_id_List.replaceAll("'", "");
                }


                out.println("<!DOCTYPE html>");
                out.println("<html>");
                out.println("<head>");
                out.println("<title>Servlet DeleteRecord</title>");
                out.println("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=ISO-8859-1\">");
                out.println("<link HREF=\"" + Constants.getServerName(c_con) + "resources/egenStyler.css\"  REL=\"stylesheet\" TYPE=\"text/css\" >");
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
                out.println("<p>" + session.getAttribute("Header") + "</p>");
                out.println("<h1>Deletion confirmation</h1>");

//            HashMap<String, String[]> parameter_map = new HashMap<>();
                ArrayList<String> toDelete_sql_l = null;
                if (session.getAttribute("toDelete_sql_l") != null) {
                    toDelete_sql_l = (ArrayList<String>) session.getAttribute("toDelete_sql_l");
                }
                if (toDelete_sql_l == null) {
                    toDelete_sql_l = new ArrayList<>();
                }

                ArrayList<String> column_nm_l = null;
                if (session.getAttribute("column_nm_l") != null) {
                    column_nm_l = (ArrayList<String>) session.getAttribute("column_nm_l");
                }
                if (column_nm_l == null) {
                    column_nm_l = new ArrayList<>();
                }
                if (current_tbl_nm != null && !current_tbl_nm.isEmpty() && c_con != null && selected_id_List != null && !selected_id_List.isEmpty()) {
                    String sql = "SELECT * FROM " + current_tbl_nm + " where id in (" + selected_id_List + ")";
                    reTreiveFromDatabase(c_con, sql, session);
                    if (session.getAttribute("column_nm_l") != null) {
                        column_nm_l = (ArrayList<String>) session.getAttribute("column_nm_l");
                    }
                    if (column_nm_l != null && !column_nm_l.isEmpty()) {
                        out.println("<form accept-charset=\"ISO-8859-1\"  action=\"" + Constants.getServerName(c_con) + "Edit/DeleteRecord\" method=\"post\">");
                        out.println("<table border=\"0\" cellpadding=\"0\" cellspacing=\"1\" width=\"100%\">");
                        out.println("<thead><tr>");

                        for (int i = 0; i < column_nm_l.size(); i++) {
                            out.println("<th>" + column_nm_l.get(i) + "</th> ");
                        }
                        out.println("<th>info</th> ");
                        out.print("</tr></thead><tbody>");
                        HashMap<String, ArrayList<String>> id_map = new HashMap<>();


                        ArrayList<ArrayList<String>> data_l = null;
                        if (session.getAttribute("data_l") != null) {
                            data_l = (ArrayList<ArrayList<String>>) session.getAttribute("data_l");
                        }
                        if (data_l == null) {
                            data_l = new ArrayList<>();
                        }

                        boolean morethan_max_found = false;
                        for (int i = cMinid; i < data_l.size(); i++) {
                            ArrayList<String> tmp_l = data_l.get(i);
                            if (i <= cmaxid) {
                                out.println("<tr>");
                            }
                            for (int j = 0; j < tmp_l.size(); j++) {
                                if (i <= cmaxid) {
                                    out.println("<td>" + tmp_l.get(j) + "</td>");
                                } else {
                                    morethan_max_found = true;
                                }
                                String cKey = current_tbl_nm + "." + column_nm_l.get(j);
                                if (id_map.containsKey(cKey) && !id_map.get(cKey).contains(tmp_l.get(j))) {
                                    id_map.get(cKey).add(tmp_l.get(j));
                                } else if (!id_map.containsKey(cKey)) {
                                    ArrayList<String> tmp_id_l = new ArrayList<>(1);
                                    tmp_id_l.add(tmp_l.get(j));
                                    id_map.put(cKey, tmp_id_l);
                                }
                            }
                            if (i <= cmaxid) {
                                out.println("<td><h7>Marked for deletion</h7></td>");
                                out.println("</tr>");
                            }
                        }
                        if (morethan_max_found) {
                            out.println("<tr>");
                            out.println("<td colspan=\"" + column_nm_l.size() + "\"><warning>There were " + data_l.size() + " records to be delete and only the first " + (cmaxid + 1) + " is shown </warning></td>");
                            out.println("</tr>");
                        }
                        out.print("<tr><td colspan=\"" + column_nm_l.size() + "\"><a href=\"" + source_page + "\" > Cancel </a> | <input type=\"submit\" value=\"Delete\"> </td></tr>");
                        out.println("</tbody></table>");
                        out.print("<input type=\"hidden\" name=\"id_list\" value=\"" + selected_id_List + "\" />");
                        out.print("<input type=\"hidden\" name=\"current_tbl_nm\" value=\"" + current_tbl_nm + "\" />");
                        out.print("<input type=\"hidden\" name=\"source_page\" value=\"" + source_page + "\" />");
                        out.println("</form>");
                        ArrayList<String> dipendent_l = getDependancies(c_con, current_tbl_nm);

                        boolean warningprinted = false;

                        toDelete_sql_l = new ArrayList<>(dipendent_l.size() + 1);
                        toDelete_sql_l.add("Delete from " + current_tbl_nm + " where id in (" + selected_id_List + ")");
                        for (int i = 0; i < dipendent_l.size(); i++) {
                            ArrayList<String> id_map_key_l = new ArrayList<>(id_map.keySet());
                            String c_sql = dipendent_l.get(i);
                            for (int j = 0; j < id_map_key_l.size(); j++) {
                                String c_key = REPLACEWITH_FALG + id_map_key_l.get(j);
                                c_sql = c_sql.replaceAll(c_key, (id_map.get(id_map_key_l.get(j)).toString()).replaceAll("\\[", "").replaceAll("\\]", ""));
                            }

                            String select_sql = "SELECT * " + c_sql;
                            if (!select_sql.contains(REPLACEWITH_FALG)) {
                                String delete_sql = "DELETE " + c_sql;
                                if (!toDelete_sql_l.contains(delete_sql)) {
                                    toDelete_sql_l.add(0, delete_sql);
                                }
                                ArrayList<String> tmp_colmn_l = new ArrayList<>(1);
                                ArrayList<ArrayList<String>> tmp_data_ll = new ArrayList<>(1);
                                try {
                                    if (!c_con.isClosed()) {
                                        ResultSet r_c = c_con.createStatement().executeQuery(select_sql);
                                        ResultSetMetaData rsmd = r_c.getMetaData();
                                        int NumOfCol = rsmd.getColumnCount();
                                        String table = Constants.get_correct_table_name(c_con, rsmd.getTableName(1));
                                        for (int j = 1; j < NumOfCol + 1; j++) {
                                            String column_nm = rsmd.getColumnName(j);
                                            tmp_colmn_l.add(column_nm);
                                        }
                                        while (r_c.next()) {
                                            ArrayList<String> tmp_l = new ArrayList<>(NumOfCol);
                                            for (int j = 1; j < NumOfCol + 1; j++) {
                                                tmp_l.add(r_c.getString(j));
                                            }
                                            tmp_data_ll.add(tmp_l);
                                        }
                                        r_c.close();
                                        if (!tmp_data_ll.isEmpty()) {
                                            if (!warningprinted) {
                                                out.println("<warning>When this record is deleted, the data listed below would also be deleted</warning>");
                                                warningprinted = true;
                                            }
                                            out.println("<br /> <h4>Following rows from the table \"" + table + "\"  will be deleted</h4>");
                                            out.println("<table border=\"0\" cellpadding=\"0\" cellspacing=\"1\" width=\"100%\">");
                                            out.println("<thead><tr>");
                                            for (int j = 0; j < tmp_colmn_l.size(); j++) {
                                                out.println("<th>" + tmp_colmn_l.get(j) + "</th> ");
                                            }
                                            out.println("<th>info</th> ");
                                            out.print("</tr></thead><tbody>");

                                            for (int j = 0; j < tmp_data_ll.size(); j++) { //j < MAX_DEPENDENT_ROWS && 
                                                ArrayList<String> tmp_l = tmp_data_ll.get(j);
                                                out.println("<tr>");
                                                for (int k = 0; k < tmp_colmn_l.size(); k++) {
                                                    if (j < MAX_DEPENDENT_ROWS) {
                                                        out.println("<td>" + tmp_l.get(k) + "</td>");
                                                    }
                                                    String cKey = table + "." + tmp_colmn_l.get(k);
                                                    if (id_map.containsKey(cKey) && !id_map.get(cKey).contains(tmp_l.get(k))) {
                                                        id_map.get(cKey).add(tmp_l.get(k));
                                                    } else if (!id_map.containsKey(cKey)) {
                                                        ArrayList<String> tmp_id_l = new ArrayList<>(1);
                                                        tmp_id_l.add(tmp_l.get(k));
                                                        id_map.put(cKey, tmp_id_l);
                                                    }

                                                }
                                                if (j < MAX_DEPENDENT_ROWS) {
                                                    out.println("<td><h7>Marked for deletion</h7></td>");
                                                }
                                                out.println("</tr>");
                                            }
                                            if (tmp_data_ll.size() > MAX_DEPENDENT_ROWS) {
                                                out.println("<tr><td colspan=\"" + tmp_colmn_l.size() + "\"><warning>" + tmp_data_ll.size() + " rows will be deleted and only the first " + MAX_DEPENDENT_ROWS + " rows are shown here.</warning>To view all <a target=\"_blank\" href=\"" + Constants.getServerName(c_con) + "Search/SearchResults3?query=" + URLEncoder.encode(select_sql, "ISO-8859-1") + "&current_tbl_nm=" + table + "&reload=true&operation=nofilter\" >click here </a></td></tr>");
                                            }
                                            out.println("</tbody></table>");
                                        }
                                    }
                                } catch (SQLException ex) {
                                    ex.printStackTrace();
                                }

                            } else {
//                            System.out.println("\t 285+" + select_sql);
                            }
                        }
                    }
                    session.setAttribute("toDelete_sql_l", toDelete_sql_l);
                } else if (column_nm_l != null && !column_nm_l.isEmpty()) {
                    if (current_tbl_nm != null && toDelete_sql_l != null && !toDelete_sql_l.isEmpty()) {
                        try {
                            boolean result = true;
                            String error_sql = null;
                            for (int i = 0; i < toDelete_sql_l.size(); i++) {
                                String sql = toDelete_sql_l.get(i);
                                try {
                                    if (sql != null && c_con.createStatement().executeUpdate(sql) < 0) {
                                        result = false;
                                        error_sql = sql;
                                    }
                                } catch (SQLException ex) {
                                    out.println(" <p><error> Error 2: Deletion failed </error>  <a href=\"" + source_page + "\" > Click here to continue </a></p>");
                                    System.out.println("Error 4a: error_sql=" + error_sql + " " + ex.getMessage());
                                }
//                            Constants.
                            }
                            if (result) {
                                HashMap<String, String> config_map = new HashMap<>();
                                config_map.put("MEMORIZING_REQUESTED", "1");
                                if (Constants.setConfig(c_con, config_map) < 0) {
                                }
                                if ((current_tbl_nm.split("\\.")[current_tbl_nm.split("\\.").length - 1]).equalsIgnoreCase("person")) {
                                    Constants.loadSessionVariables(c_con, request);
                                }
                                out.println(" <p><success> Deletion successful </success> <a href=\"" + source_page + "\" > Click here to continue </a></p>");
                            } else {
                                out.println(" <p><error> There were errors during deletion, error logged </error> <a href=\"" + source_page + "\" > Click here to continue </a></p>");
                                System.out.println("ERROR_SQL " + error_sql);
                            }
                        } catch (Exception ex) {
                            out.println(" <p><error> Error 2: Deletion failed </error>  <a href=\"" + source_page + "\" > Click here to continue </a></p>");
                            ex.printStackTrace();
                        }

                    }

                } else if (selected_id_List == null || selected_id_List.isEmpty()) {
                    out.println(" <p><error>Error 1: No records were selected </error> <a href=\"" + source_page + "\" > Click here to select </a></p>");
                } else if (current_tbl_nm == null || current_tbl_nm.isEmpty()) {
                    out.println(" <p><error>Error 2: Requested table can not be accessed or it was null. </error> <a href=\"" + source_page + "\" > Click here to ry again </a></p>");

                } else {
                    out.println(" <p><error>Error 3:  </error> <a href=\"" + source_page + "\" > Click here to ry again </a></p>");

                }
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

    private HashMap<String, ArrayList<String>> create_selected_id_map(ArrayList<String> dipendent_l) {
        HashMap<String, ArrayList<String>> id_map = new HashMap<String, ArrayList<String>>();
        return id_map;
    }

    private ArrayList<String> getDependancies(Connection c_con, String in_tblnm) {
        ArrayList<String> dpdnd_sql_l = new ArrayList<>(1);
        ArrayList<String> tables_to_be_checked_l = new ArrayList<>(1);
        ArrayList<String> tables__checked_l = new ArrayList<>(1);
        tables_to_be_checked_l.add(in_tblnm);

        try {
            String sql = "select distinct table_name from  " + Constants.get_correct_table_name(c_con, "tablename2feature") + " where showinsearch=1";

            while (!tables_to_be_checked_l.isEmpty()) {
                String tblnm = tables_to_be_checked_l.remove(0);
                tables__checked_l.add(tblnm);
                if (!c_con.isClosed()) {
                    Statement st_1 = c_con.createStatement();
                    ResultSet r_1 = st_1.executeQuery(sql);
                    while (r_1.next()) {
                        String c_tbl_nm = r_1.getString("table_name");
                        c_tbl_nm = Constants.get_correct_table_name(c_con, c_tbl_nm);
                        HashMap<String, String[]> constraint_map = Constants.get_key_contraints(c_con, c_tbl_nm, tblnm, null);
                        if (constraint_map != null) {
                            ArrayList<String> constraint_ma_key_l = new ArrayList<>(constraint_map.keySet());
                            for (int i = 0; i < constraint_ma_key_l.size(); i++) {
                                String[] result_a = constraint_map.get(constraint_ma_key_l.get(i));
                                if (result_a != null) {
                                    String sql_tmp = "";
                                    if (c_tbl_nm.equals(result_a[1])) {
                                        sql_tmp = "FROM " + c_tbl_nm + " WHERE " + c_tbl_nm + "." + result_a[0] + " in (" + REPLACEWITH_FALG + result_a[1] + "." + result_a[2] + ")";
                                    } else {
                                        sql_tmp = "FROM " + c_tbl_nm + " WHERE " + c_tbl_nm + "." + result_a[0] + " in (SELECT " + result_a[2] + " from " + result_a[1] + " where " + result_a[1] + ".id in (" + REPLACEWITH_FALG + result_a[1] + "." + result_a[2] + "))";
                                    }
                                    dpdnd_sql_l.add(sql_tmp);
                                    if (!tables__checked_l.contains(c_tbl_nm)) {
                                        tables_to_be_checked_l.add(c_tbl_nm);
                                    }
                                }
                            }
                        }
                    }
                    r_1.close();
                    st_1.close();
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return dpdnd_sql_l;
    }


    /*
     * method 12
     */
    private void reTreiveFromDatabase(Connection c_con, String org_query, HttpSession session) {
        ArrayList<String> column_nm_l = new ArrayList<>(10);
        ArrayList<ArrayList<String>> data_l = new ArrayList<>(10);
        String s_1 = org_query;
        try {

            if (!c_con.isClosed()) {
                Statement st_1 = c_con.createStatement();
                ResultSet r_1 = st_1.executeQuery(s_1);
                ResultSetMetaData rsmd = r_1.getMetaData();
                int NumOfCol = rsmd.getColumnCount();
                for (int i = 1; i < NumOfCol + 1; i++) {
                    String column_nm = rsmd.getColumnName(i);
                    column_nm_l.add(column_nm);
                }
                while (r_1.next()) {
                    ArrayList<String> tmp_l = new ArrayList<>(NumOfCol);
                    for (int i = 1; i < NumOfCol + 1; i++) {
                        tmp_l.add(r_1.getString(i));
                    }
                    data_l.add(tmp_l);
                }
                r_1.close();
                st_1.close();
            }
            session.setAttribute("data_l", data_l);
            session.setAttribute("column_nm_l", column_nm_l);

        } catch (SQLException e) {
            column_nm_l = new ArrayList<>(1);
            column_nm_l.add("Error");
            session.setAttribute("column_nm_l", column_nm_l);
            data_l = new ArrayList<>(1);
            ArrayList<String> tmp = new ArrayList<>(1);
            tmp.add("Error 12a: ");
            System.out.println("Error 12a " + s_1 + " " + e.getMessage());
            data_l.add(tmp);
            session.setAttribute("data_l", data_l);

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
                dataSource = (DataSource) env.lookup(Constants.DATASOURCE_NAME_DATA_UPDATE);
                if (dataSource == null) {
                    throw new ServletException("'" + Constants.DATASOURCE_NAME_DATA_UPDATE + "' is an unknown DataSource");
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
