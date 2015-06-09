package no.ntnu.medisn.egenvar;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Map;
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
public class Codes extends HttpServlet {

    private DataSource dataSource;

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
        try {

            Map<String, String[]> entered_values_map_tmp = request.getParameterMap();
            String current_tbl_nm = null;
            String id_list = null;
            if (entered_values_map_tmp != null && !entered_values_map_tmp.isEmpty()) {
                current_tbl_nm = entered_values_map_tmp.keySet().iterator().next();
                if (current_tbl_nm != null && !current_tbl_nm.isEmpty() && current_tbl_nm.startsWith(Constants.TABLE_TO_USE_FLAG)) {
                    id_list = entered_values_map_tmp.get(current_tbl_nm)[0];
                }
                current_tbl_nm = current_tbl_nm.replace(Constants.TABLE_TO_USE_FLAG, "");
            }

            current_tbl_nm = Constants.get_correct_table_name(getDatasource().getConnection(), current_tbl_nm);

            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Codes</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Codes</h1>");
            if (current_tbl_nm != null && id_list != null) {
                if (!id_list.startsWith("(")) {
                    id_list = "(" + id_list;
                }
                if (!id_list.endsWith(")")) {
                    id_list = id_list + ")";
                }
                String query = "select * from " + current_tbl_nm + " where id in " + id_list;
                out.println("<p>" + query + "</p>");
            } else {
                out.println("<error>URL format not undestood</error>");
            }
            out.println("</body>");
            out.println("</html>");
        } catch (SQLException ex) {
            System.out.println("Error CODEs1a: " + ex.getMessage());
        } finally {
            out.close();
        }
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
