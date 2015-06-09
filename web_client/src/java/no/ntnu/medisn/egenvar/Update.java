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
public class Update extends HttpServlet {
//private DataSource dataSource;

    private DataSource dataSource_update;
    private DataSource dataSource;
    private HashMap<String, String> forign_key_to_table_m;
//    private final static String CONSTRAINT_REFERENCE_PATTERN = ".*CONSTRAINT.*FOREIGN KEY[^\\(]+\\([^A-Za-z0-9_\\s]{1}([A-Za-z0-9_\\s]+).*REFERENCES[\\s]*[^A-Za-z0-9_\\s]{1}([A-Za-z0-9_\\s]+)[^A-Za-z0-9_\\s]{1}.*\\([^A-Za-z0-9_\\s]{1}(.*)[^A-Za-z0-9_\\s]{1}\\).*";
//    private Pattern ptn_1;
    private ArrayList<String> atributes_to_clear_l;

    /**
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
//        ptn_1 = Pattern.compile(CONSTRAINT_REFERENCE_PATTERN);


    }

    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=ISO-8859-1");
        PrintWriter out = response.getWriter();
        init_Update();
        Connection c_con = null;
        Connection c_con_update = null;
        try {
            c_con = getDatasource().getConnection();
            HttpSession session = request.getSession(true);

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
            String serachType = null;
            if (request.getParameter("serachType") != null) {
                serachType = request.getParameter("serachType");
                session.setAttribute(current_tbl_nm + "up_serachType", serachType);
            } else {
                if (session.getAttribute(current_tbl_nm + "up_serachType") != null) {
                    serachType = (String) session.getAttribute(current_tbl_nm + "up_serachType");
                }
            }
            String updatesearchvalue = null;
            if (request.getParameter("updatesearchvalue") != null) {
                updatesearchvalue = request.getParameter("updatesearchvalue");
                session.setAttribute(current_tbl_nm + "up_updatesearchvalue", updatesearchvalue);
            } else {
                if (session.getAttribute(current_tbl_nm + "up_updatesearchvalue") != null) {
                    updatesearchvalue = (String) session.getAttribute(current_tbl_nm + "up_updatesearchvalue");
                }
            }
            String value_return_to_page = Constants.getServerName(c_con) + "Edit/table_selection?operation=update&value_return_to_page="
                    + Constants.getServerName(c_con) + "Edit/Update";
            if (request.getParameter("value_return_to_page") != null) {
                value_return_to_page = request.getParameter("value_return_to_page");
                session.setAttribute(current_tbl_nm + "value_return_to_page", value_return_to_page);
            }
            HashMap<String, String[]> entered_values_map = new HashMap<>();
            if (session.getAttribute(current_tbl_nm + "entered_values_map") != null) {
                entered_values_map = (HashMap<String, String[]>) session.getAttribute(current_tbl_nm + "entered_values_map");
            }

            entered_values_map.putAll(request.getParameterMap());
            session.setAttribute(current_tbl_nm + "entered_values_map", entered_values_map);

            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=ISO-8859-1\">");
            out.println("<link HREF=\"../resources/egenStyler.css\"  REL=\"stylesheet\" TYPE=\"text/css\" >");
            out.println("<title>Edit and update </title>");
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
            out.println("<h1> Edit and update </h1>");
            c_con_update = getDatasource_update().getConnection();
            reTreiveFromDatabase(c_con, c_con_update, out, entered_values_map, current_tbl_nm,
                    serachType, updatesearchvalue, value_return_to_page, session);
            out.println("</body>");
            out.println("</html>");
            close(c_con, null, null);
            close(c_con_update, null, null);
        } catch (SQLException ex) {
        } finally {
            out.close();
            close(c_con, null, null);
            close(c_con_update, null, null);
        }
    }

    private void reTreiveFromDatabase(Connection c_con, Connection c_update, PrintWriter out, HashMap<String, String[]> entered_values_map, String current_tbl_nm,
            String serachType, String searchvalue, String value_return_to_page, HttpSession session) {
        try {
//            Connection ncon = dataSource_update.getConnection();
            current_tbl_nm = Constants.get_correct_table_name(c_con, current_tbl_nm);

            if (!c_update.isClosed() && out != null && current_tbl_nm != null && searchvalue != null) {
                out.println("<form accept-charset=\"ISO-8859-1\"  action=\"" + Constants.getServerName(c_con) + "Upload/SubmitToDb\" method=\"post\">");
                out.println("<table border=\"1\" cellpadding=\"0\" cellspacing=\"1\" width=\"100%\">");
                Statement st_1 = c_update.createStatement();
                searchvalue = searchvalue.replaceAll("'", "");
                String quat = "'";
                if (!Constants.shoulditbequated(c_con,current_tbl_nm, serachType)) {
                    quat = "";
                }
                String s_1 = "SELECT * from " + current_tbl_nm + " WHERE " + serachType + "=" + quat + searchvalue + quat;
                ResultSet r_1 = st_1.executeQuery(s_1);
                ArrayList<String> column_names = Constants.getColumn_names(c_con,current_tbl_nm);
//                ResultSetMetaData rsmd = r_1.getMetaData();
//                int NumOfCol = rsmd.getColumnCount();
//                int noOfRecs = 0;
                out.println("<tbody><tr>");
                out.println("<td>Table updated</td><td colspan=\"2\"><input type=\"text\" readonly=\"readonly\" name=\"table_nm\" value=\"" + current_tbl_nm + "\"></td>");
                out.println("</tr>");
                HashMap<String, String[]> frgn_key_map = Constants.get_key_contraints(c_con,current_tbl_nm);
                while (r_1.next()) {
//                    noOfRecs++;
                    for (int i = 0; i < column_names.size(); i++) {
                        out.println("<tr>");
                        String clmnnm = column_names.get(i);
                        String value = r_1.getString(clmnnm);
                        if (clmnnm.equalsIgnoreCase("id")) {
                            out.println("<td>" + clmnnm + "</td><td colspan=\"2\"><input type=\"text\" readonly=\"readonly\" size=\"10\" name=\"" + clmnnm + "\" value=\"" + value + "\"></td>");

                        } else {
//                            String name_to_look_in_return_prms = clmnnm;
//                            if (frgn_key_map.containsKey(clmnnm) && frgn_key_map.get(clmnnm) != null) {
//                                name_to_look_in_return_prms = frgn_key_map.get(clmnnm)[1];
//                            }
                            if (entered_values_map != null && entered_values_map.containsKey("value_returned_to_col") && entered_values_map.get("value_returned_to_col") != null && entered_values_map.get("value_returned_to_col")[0].equals(clmnnm)) {
                                if (entered_values_map.containsKey("searchvalue")) {
                                    value = entered_values_map.get("searchvalue")[0];
                                }

                            } else if (!clmnnm.equalsIgnoreCase("id") && entered_values_map != null && entered_values_map.containsKey(clmnnm)) {
                                value = entered_values_map.get(clmnnm)[0];
                            }
//                            else {
//                                if (!clmnnm.equalsIgnoreCase("id") && entered_values_map != null && entered_values_map.containsKey(clmnnm)) {
//                                    value = entered_values_map.get(clmnnm)[0];
//                                } else if (!name_to_look_in_return_prms.equals(current_tbl_nm) && entered_values_map != null && entered_values_map.containsKey(name_to_look_in_return_prms) && entered_values_map.get(name_to_look_in_return_prms) != null) {
//                                    value = entered_values_map.get(name_to_look_in_return_prms)[0];
//                                }
//                            }


//                            if (frgn_key_map.containsKey(clmnnm) && value != null) {
//                                String ref_tbl = frgn_key_map.get(clmnnm)[1];
//                                String sql = "SELECT * FROM " + ref_tbl;//+ " WHERE " + ref_clmn + "='" + value + "'";
//                                sql = URLEncoder.encode(sql, "ISO-8859-1");
//                                String value_str = " (<a href=\"" + Constants.getServerName() + "Search/SearchResults?query=" + sql + "&current_tbl_nm=" + ref_tbl + "&recent_home=set&value_return_to_page=../Edit/Update&reload=true&operation=select\">Change</a>)";
//                                out.println("<td>" + clmnnm + "</td><td colspan=\"2\">" + "<input readonly=\"readonly\" type=\"text\" size=\"10\" name=\"" + clmnnm + "\" value=\"" + value + "\">" + value_str + "</td>");
//
//                            } else 
                            entered_values_map.put(clmnnm, new String[]{value});
                            if (frgn_key_map.containsKey(clmnnm)) {
                                String ref_tbl = frgn_key_map.get(clmnnm)[1];
                                String sql = "SELECT * FROM " + ref_tbl;
                                sql = URLEncoder.encode(sql, "ISO-8859-1");
                                String value_str = " (<a href=\"" + Constants.getServerName(c_con) + "Search/SearchResults3?query=" + sql
                                        + "&current_tbl_nm=" + ref_tbl + "&recent_home=set&value_return_to_page=../Edit/Update&reload=true&operation=select&previous_tbl_nm=" + current_tbl_nm + "&value_returned_to_col=" + clmnnm + "\">Change</a>)";
                                out.println("<td>" + clmnnm + "</td><td colspan=\"2\">" + "<input readonly=\"readonly\" type=\"text\" size=\"10\" name=\"" + clmnnm + "\" value=\"" + value + "\">" + value_str + "</td>");

                            } else {
                                out.println("<td>" + clmnnm + "</td><td colspan=\"2\"><input type=\"text\" size=\"75\" name=\"" + clmnnm + "\" value=\"" + value + "\"></td>");

                            }
                        }

                        out.println("</tr>");
                    }
                }
                session.setAttribute(current_tbl_nm + "entered_values_map", entered_values_map);
                out.println("<tr><td></td><td>");
                out.println("<a href=\"" + Constants.getServerName(c_con) + "Edit/table_selection?operation=update&value_return_to_page="
                        + Constants.getServerName(c_con) + "Edit/Updatep\">Cancel</a> ");
                out.println("<input type=\"hidden\" name=\"operation\" value=\"update\" />");
                out.println("<input type=\"hidden\" name=\"current_tbl_nm\" value=\"" + current_tbl_nm + "\" />");
                out.println("<input type=\"hidden\" name=\"value_return_to_page\" value=\"" + value_return_to_page + "\" />");
                out.println(" | <input type=\"submit\" name=\"submitbtn\" value=\"Submit changes\">");
                out.println("</td></tr>");
                out.println("  </tbody></table>");
                out.println("</form>");

            } else {
                System.out.println("Error opening connection or current table was null current_tbl_nm=" + current_tbl_nm + "| searchvalue=" + searchvalue);
            }
//            ncon.close();
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
                dataSource_update = (DataSource) env.lookup(Constants.DATASOURCE_NAME_DATA_UPDATE);
                if (dataSource_update == null) {
                    throw new ServletException("`" + Constants.DATASOURCE_NAME_DATA_UPDATE + "' is an unknown DataSource");
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
