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
import java.util.Collections;
import java.util.HashMap;
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
public class Table_selection extends HttpServlet {

    private DataSource dataSource;
    private HashMap<String, ArrayList<String>> cat2table_map;
    private ArrayList<String> category_l;

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
        Connection c_con = null;
        try {
            c_con = getDatasource().getConnection();
            HttpSession ses = request.getSession(true);
            if (ses.getAttribute("ATRB_LOAD") == null) {
                String redirectURL = Constants.getServerName(c_con) + "index";
                response.sendRedirect(redirectURL);
            } else {
                /*Reset session variables affecting other pages*/
                ses.removeAttribute("batch_mode");
                ses.removeAttribute("return_to_link");
                String operation = request.getParameter("operation");
                String value_return_to_page = request.getParameter("value_return_to_page");
                out.println("<!DOCTYPE html>");
                out.println("<html>");
                out.println("<head>");
                out.println("<script language = JavaScript>\n");
                out.println("function setVisible(target, message, value) {\n"
                        + "                if (document.getElementById(target) != null) {\n"
                        + "                    document.getElementById(target).style.visibility = value;\n"
                        + "                }\n"
                        + "                if (document.getElementById(message) != null) {\n"
                        + "                    if (document.getElementById(message).style.visibility == \"visible\") {\n"
                        + "                     document.getElementById(message).style.visibility = \"hidden\";\n"
                        + "                     document.getElementById(\"advancedsearch\").style.visibility = \"visible\";\n"
                        + "                    } else {\n"
                        + "                     document.getElementById(message).style.visibility = \"visible\";\n"
                        + "                     document.getElementById(\"advancedsearch\").style.visibility = \"hidden\";\n"
                        + "                    }\n"
                        + "                }\n"
                        + "}\n");
                out.println(" function setDropDown(category){\n"
                        + "      document.getElementById(\"div_tbl\").innerHTML=category;\n"
                        + " }\n");
                out.println(" function setValues(category){\n");
//                out.println("   var historySelectList = $('select#history');");
//                 out.println("   setValues(document.getElementById(\"Categories\").options[document.getElementById(\"Categories\").selectedIndex].text);");
                out.println("   var selectedValue =category.options[category.selectedIndex].text;");
                out.println("   " + fabricateFunction(c_con) + "\n");
                out.println(" }\n");
                out.println("</script>");
                out.println(" <link HREF=\"" + Constants.getServerName(c_con) + "resources/egenStyler.css\"  REL=\"stylesheet\" TYPE=\"text/css\" >");
                out.println("<title>GenVar Datamangement System</title>");
                out.println("</head>");
                out.println("<body>");
                out.println("<table  class=\"table_text\" >\n"
                        + " <thead>\n"
                        + "     <tr>\n"
                        + "         <th>\n"
                        + "                <img  height=\"50%\" width=\"100%\" src=\"../Banner.gif\"/>\n"
                        + "         </th>\n"
                        + "     </tr>\n"
                        + " </thead>\n"
                        + "</table>\n");
                out.println(ses.getAttribute("Header"));
                out.println("<h1>Select a table to " + operation + "</h1>");

                String button_text = "Find records to " + operation;

                if (operation.toUpperCase().equals("INSERT")) {
                    out.println("<form accept-charset=\"ISO-8859-1\"  action=\"" + Constants.getServerName(c_con) + "Upload/CreateNew\" method=\"post\">");
                    button_text = "Create new record";
                } else if (operation.toUpperCase().equals("UPDATE")) {
                    out.println("<form accept-charset=\"ISO-8859-1\"  action=\"" + Constants.getServerName(c_con) + "Search/SearchResults3\" method=\"post\">");
                    button_text = "Select record to update";
                } else if (operation.toUpperCase().equals("DELTE")) {
                    out.println("<form accept-charset=\"ISO-8859-1\"  action=\"" + Constants.getServerName(c_con) + "Search/SearchResults3\" method=\"post\">");
                    button_text = "Select record to Delete";
                } else {
                    out.println("<form accept-charset=\"ISO-8859-1\"  action=\"" + Constants.getServerName(c_con) + "Search/SearchResults3\" method=\"post\">");
                }

                out.println("<table class=\"table3\" >\n"
                        + "    <tbody>"
                        + "     <tr>"
                        + "         <td>Table catagory</td><td>\n");
                out.println(createDropDown(getCategories(c_con), "Categories", true));
                out.println("       </td>"
                        + "     </tr>");
                out.println("   <tr><td>Table name</td><td>"
                        + "         <div_tbl id=\"div_tbl\">Select a category to get the tables</div_tbl>"
                        + "     </td></tr>");
                out.println("   <tr><td></td><td>");
                if (operation.toUpperCase().equals("UPDATE") || operation.toUpperCase().equals("DELETE")) {
                    out.println("         <input type=\"hidden\" name=\"batch_mode\" value=\"TRUE\"  />\n");
                }
                if (operation.toUpperCase().equals("UPDATE")) {
                    out.println("       <input type=\"hidden\" name=\"" + Constants.TABLE_TO_USE_FLAG + "\" value=\"GET_ALL\"  />\n"
                            + "         <input type=\"hidden\" name=\"reset\" value=\"true\" />\n"
                            + "         <input type=\"hidden\" name=\"searchvalue\" value=\"%\" />\n"
                            + "         <input type=\"hidden\" name=\"value_return_to_page\" value=\"" + value_return_to_page + "\" width=\"10\" />\n");
                } else if (operation.toUpperCase().equals("DELETE")) {
                    out.println("       <input type=\"hidden\" name=\"" + Constants.TABLE_TO_USE_FLAG + "\" value=\"GET_ALL\"  />\n"
                            + "         <input type=\"hidden\" name=\"reset\" value=\"true\" />\n"
                            + "         <input type=\"hidden\" name=\"searchvalue\" value=\"%\" />\n");
                    out.println("       <input type=\"hidden\" name=\"value_return_to_page\" value=\"" + value_return_to_page + "\" width=\"10\" />\n");
                }
                out.println("         <input type=\"hidden\" name=\"operation\" value=\"" + operation + "\" />\n");
                out.println("         <input type=\"submit\" value=\"" + button_text + "\" name=\"submitbtn\" />    ");
                out.println("     </td></tr>");
                out.println("</tbody></table>");
                out.println("</form>");
                out.println("<script type=\"text/javascript\">");
                out.println("   setValues(document.getElementById(\"Categories\"));");//.options[document.getElementById(\"Categories\").selectedIndex].text);
                out.println("</script>");

                out.println("</body>");
                out.println("</html>");
            }
            close(c_con, null, null);
        } catch (SQLException ex) {
        } finally {
            out.close();
            close(c_con, null, null);
        }
    }

      private String fabricateFunction(Connection c_con) {
        String options = "";
        ArrayList<String> key_l = getCategories(c_con);
        for (int i = 0; i < key_l.size(); i++) {
            String c_cat = key_l.get(i);
            ArrayList<String> val_l = loadData(c_con).get(c_cat);
            String c_drop = createDropDown(val_l, "current_tbl_nm", false);          
            if (i == 0) {
                options = " if(selectedValue.match(\"" + c_cat + "\")!=null) {setDropDown(\"" + c_drop + "\")}\n";
            } else {
                options = options + "else if(selectedValue.match(\"" + c_cat + "\")!=null) {setDropDown(\"" + c_drop + "\")}\n";
            }
        }
        options = options + "else  {setDropDown(selectedValue)}\n";       
        return options;
    }

//    private String fabricateFunction(Connection c_con) {
//        String options = "";
//        ArrayList<String> key_l = getCategories(c_con);
//        for (int i = 0; i < key_l.size(); i++) {
//            String c_cat = key_l.get(i);
//            ArrayList<String> val_l = loadData(c_con).get(c_cat);
//            String c_drop = createDropDown(val_l, "current_tbl_nm", false);
//            if (i == 0) {
//                options = " if(category.match(\"" + c_cat + "\")!=null) {setDropDown(\"" + c_drop + "\")}\n";
//            } else {
//                options = options + "else if(category.match(\"" + c_cat + "\")!=null) {setDropDown(\"" + c_drop + "\")}\n";
//            }
//
//        }
//        options = options + "else  {setDropDown(\"Error\")}\n";       
//        return options;
//    }

    private String createDropDown(ArrayList<String> data_l, String name, boolean addfunction) {
        String out = "<select name='" + name + "' id='" + name + "'>";
        if (addfunction) {
            out = "<select name='" + name + "' id='" + name + "' onchange='setValues(this);'>";
        }
        for (int i = 0; i < data_l.size(); i++) {
//            if (addfunction) {
//                out = out + " <option  value=\"" + data_l.get(i) + "\">" + data_l.get(i) + " </option>"; //onclick='setValues(\"" + data_l.get(i)+ "\");'
//            } else {
                out = out + "<option  value='" + data_l.get(i) + "'>" + data_l.get(i) + "</option>";
//            }
        }
        out = out + " </select>";      
        return out;
    }

//    private ArrayList<String> getForCategory(String category) {
//        return loadData().get(category);
//    }
    private ArrayList<String> getCategories(Connection c_con) {
        return new ArrayList<>(loadData(c_con).keySet());
    }

    private HashMap<String, ArrayList<String>> loadData(Connection c_con) {
        if (cat2table_map == null) {
            cat2table_map = new HashMap<>();
            String sql = "select category,table_name,simplename from " + Constants.get_correct_table_name(c_con, "tablename2feature");
//            try {

//                if (dataSource != null) {
            Connection ncon;
            try {
//                        ncon = dataSource.getConnection();
                Statement stm_1 = c_con.createStatement();
                ResultSet r_1 = stm_1.executeQuery(sql);
                while (r_1.next()) {
                    String c_cat = r_1.getString("category");
                    String c_tbl = r_1.getString("table_name");
                    if (!cat2table_map.containsKey(c_cat)) {
                        cat2table_map.put(c_cat, new ArrayList<String>(5));
                    }
                    cat2table_map.get(c_cat).remove(c_tbl);
                    cat2table_map.get(c_cat).add(c_tbl);
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
//                }
//            } catch (ServletException ex) {
//                ex.printStackTrace();
//            }
            ArrayList<String> key_l = new ArrayList<>(cat2table_map.keySet());
            for (int i = 0; i < key_l.size(); i++) {
                Collections.sort(cat2table_map.get(key_l.get(i)));
            }


        }
        return cat2table_map;
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
