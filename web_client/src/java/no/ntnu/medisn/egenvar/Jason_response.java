/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package no.ntnu.medisn.egenvar;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import static no.ntnu.medisn.egenvar.Errormsgs.close;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author sabryr
 */
public class Jason_response extends HttpServlet {

    private DataSource dataSource;
    private Connection ncon;
    private final int MAX_LIMIT = 10;

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
        response.setContentType("application/json;charset=UTF-8");

        PrintWriter out = response.getWriter();
//        Connection c_con = null;
        try {
//            Timing.setPointer();
            if (ncon == null || ncon.isClosed()) {
                ncon = getDatasource().getConnection();
            }         
            String query = "";
            JSONArray jasonArrray = new JSONArray();
            String error = null;
            String table = request.getParameter("table");
            if (table != null) {
                String correct_tbl_nm = Constants.get_correct_table_name(ncon, table);
                String field = request.getParameter("field");
                String term = request.getParameter("term");
                if (correct_tbl_nm != null) {
                    if (field == null || term == null) {
                        error = "Not enough details";
                    } else {
                        query = "Select " + field + " from " + correct_tbl_nm + " where " + field + " like '%" + term + "%'";
                        //cache results in a hasmap
                        jasonArrray = getRsults(ncon, query);

                    }
                } else {
                    error = "Table not found " + table;
                }
            } else {
                error = "Table was null";
            }

            if (error != null) {
                try {
                    JSONObject erro_jasonob = new JSONObject();
                    erro_jasonob.put("name", error);
                    jasonArrray.put(0, erro_jasonob);
                } catch (JSONException ex) {
                }
            }
            out.println(jasonArrray.toString());
//            close(c_con, null, null);
//            System.out.println("91 " + Timing.convert(Timing.getFromlastPointer()));
        } catch (SQLException ex) {
        } finally {
            out.close();
//            close(c_con, null, null);
        }
    }

    private JSONArray getRsults(Connection ncon, String query) {
        JSONArray jasonArrray = new JSONArray();
        Statement st_1 = null;
        ResultSet r_1 = null;
        int pos = 0;
        try {
            if (!ncon.isClosed()) {
                st_1 = ncon.createStatement();
                st_1.setMaxRows(MAX_LIMIT);
                r_1 = st_1.executeQuery(query);
                int count = 0;
                while (r_1.next() && (count < MAX_LIMIT)) {
                    JSONObject jasonob = new JSONObject();
                    count++;
                    String nm_touse;
                    String[] cnm = r_1.getString(1).split(">");
                    if (cnm.length >= 3) {
                        nm_touse = cnm[cnm.length - 3].trim() + " (" + cnm[cnm.length - 2].trim() + ")" + cnm[cnm.length - 1].trim();
                    } else if (cnm.length >= 2) {
                        nm_touse = cnm[cnm.length - 2].trim() + "," + cnm[cnm.length - 1].trim();
                    } else {
                        nm_touse = cnm[0];
                    }
                    jasonob.put("name", nm_touse);
                    jasonArrray.put(pos, jasonob);
                    pos++;
                }
                if (count >= MAX_LIMIT) {
                    JSONObject jasonob = new JSONObject();
                    jasonob.put("name", "...Displaying only the first " + MAX_LIMIT + " results..");
                    jasonArrray.put(pos, jasonob);
                }
                r_1.close();
                st_1.close();
            } else {
                System.out.println("181 connection closed");
            }
        } catch (SQLException ex) {
        } catch (Exception ex) {
        } finally {
            close(null, st_1, r_1);
        }
        return jasonArrray;
    }

    private DataSource getDatasource() throws ServletException {
        if (dataSource == null) {
            try {
                Context env = (Context) new InitialContext().lookup("java:comp/env");
                dataSource = (DataSource) env.lookup(Constants.DATASOURCE_NAME_DATA_VIEW);
                if (dataSource == null) {
                    throw new ServletException("`" + Constants.DATASOURCE_NAME_DATA_VIEW + "' is an unknown DataSource");
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
