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
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
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
public class CreateTempalte extends HttpServlet {

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
    private DataSource dataSource;
    private final static String CONSTRAINT_REFERENCE_PATTERN = ".*CONSTRAINT.*FOREIGN KEY[^\\(]+\\([^A-Za-z0-9_\\s]{1}([A-Za-z0-9_\\s]+).*REFERENCES[\\s]*[^A-Za-z0-9_\\s]{1}([A-Za-z0-9_\\s]+)[^A-Za-z0-9_\\s]{1}.*\\([^A-Za-z0-9_\\s]{1}(.*)[^A-Za-z0-9_\\s]{1}\\).*";
    private final static String CONSTRAINT_PARENT_REFERENCE_PATTERN = ".*CONSTRAINT.*parentref.*FOREIGN KEY[^\\(]+\\([^A-Za-z0-9_\\s]{1}([A-Za-z0-9_\\s]+).*REFERENCES[\\s]*[^A-Za-z0-9_\\s]{1}([A-Za-z0-9_\\s]+)[^A-Za-z0-9_\\s]{1}.*\\([^A-Za-z0-9_\\s]{1}(.*)[^A-Za-z0-9_\\s]{1}\\).*";
    private Pattern ptn_1;
    private Pattern ptn_2;
    private ArrayList<String> atributes_to_clear_l;
    private final String ID_FLAG = "id";
//    private final String PARENT_ID_FLAG = "parent_id";
    private final String REPLACEWITH_ID_FALG = "'000'";
    private final String REPLACEWITH_ID_ENCODED_FALG = "%27000%27";
    private final String DISPLAY_CLM_NM = "NAME";
    private final String ID_CLMN_NM = "ID";
    private HashMap<String, HashMap<String, String[]>> key_constraint_map;
//         private ArrayList<String> previous_tbl_nm_list;
//    private final String MIBBI_USERCONTENT_TABLE = "mibbi_user_content";
//    private final String MIBBI_USERCONTENT_REF_COLUMN_NAME = "mibbi_structure_component_id";

    public void my_init() {
        ptn_1 = Pattern.compile(CONSTRAINT_REFERENCE_PATTERN);
        ptn_2 = Pattern.compile(CONSTRAINT_PARENT_REFERENCE_PATTERN);
        if (atributes_to_clear_l == null) {
            atributes_to_clear_l = new ArrayList<>();
            atributes_to_clear_l.add("");
        }
    }
    
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        my_init();
        response.setContentType("text/html;charset=ISO-8859-1");
        PrintWriter out = response.getWriter();
        Connection c_con = null;
        try {
            c_con = getDatasource().getConnection();
            String previous_tbl_nm = request.getParameter("previous_tbl_nm");
            if (previous_tbl_nm == null) {
                previous_tbl_nm = "mibbi";
            }
            String previous_id = request.getParameter("previous_id");
            if (previous_id == null) {
                previous_id = "-1";
            }
            HttpSession session = request.getSession(true);
            String operation = request.getParameter("function");
            ArrayList<String> history_list = null;
            if (operation != null && operation.equals("reset")) {
                session.removeAttribute("history_list");
            } else if (session.getAttribute("history_list") != null) {
                history_list = (ArrayList<String>) session.getAttribute("history_list");
            }
            if (history_list == null) {
                history_list = new ArrayList<String>(1);
            }
            ArrayList<String> previous_tbl_nm_list = new ArrayList<String>();
            if (session.getAttribute("previous_tbl_nm_list") != null) {
                previous_tbl_nm_list = (ArrayList<String>) session.getAttribute("previous_tbl_nm_list");
            }
            if (!previous_tbl_nm_list.contains(previous_tbl_nm)) {
                previous_tbl_nm_list.add(previous_tbl_nm);
            }
            String value_return_to_page = request.getParameter("value_return_to_page");
            if (value_return_to_page == null) {
                if (session.getAttribute("value_return_to_page") != null) {
                    value_return_to_page = (String) session.getAttribute("value_return_to_page");
                }
            } else {
                session.setAttribute("value_return_to_page", value_return_to_page);
            }
            String value_returned_to_col = request.getParameter("value_returned_to_col");
            if (value_returned_to_col == null) {
                if (session.getAttribute("value_returned_to_col") != null) {
                    value_returned_to_col = (String) session.getAttribute("value_returned_to_col");
                }
            } else {
                session.setAttribute("value_returned_to_col", value_returned_to_col);
            }
            
            
            
            String value_returned_to_table = request.getParameter("value_returned_to_table");
            if (value_returned_to_table == null) {
                if (session.getAttribute("value_returned_to_table") != null) {
                    value_returned_to_table = (String) session.getAttribute("value_returned_to_table");
                }
            } else {
                session.setAttribute("value_returned_to_table", value_returned_to_table);
            }
            
            session.setAttribute("previous_tbl_nm_list", previous_tbl_nm_list);
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<script language = JavaScript>");
            out.println("function displayDetails(sourceelement){"
                    + " while (document.getElementById(\"divmain\").firstChild) { "
                    + "  document.getElementById(\"divmain\").removeChild(document.getElementById(\"divmain\").firstChild); "
                    + " }"
                    + "   if(document.getElementById(sourceelement)!=null){"
                    + "      var targetid=document.getElementById(sourceelement).value;"
                    + "      if(document.getElementById(\"div\"+targetid)!=null){"
                    + "         var copy=document.getElementById(\"div\"+targetid).firstChild.cloneNode(true);"
                    + "         document.getElementById(\"divmain\").appendChild(copy);"
                    + ""
                    + "      }"
                    + "   }"
                    + "  }");
            out.println("function setVisibility(target, value){"
                    + " if(document.getElementById(target)!=null){"
                    + "   document.getElementById(target).style.visibility = value;"
                    + " }"
                    + "}");
            
            out.println("</script>");
            out.println("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=ISO-8859-1\">");
            
            out.println("<link HREF=\"" + Constants.getServerName(c_con) + "resources/egenStyler.css\"  REL=\"stylesheet\" TYPE=\"text/css\" >");
            out.println("<title>From Template</title>");
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
            out.println("<h1>Created from template </h1>");
            String c_seelction_name = null;
            if (value_returned_to_table == null || value_returned_to_table.isEmpty()) {
                out.println("<error>This page was not meant to be invoked in this way. Please contact administrator with error id 345687ss</error>");
            } else {
//                String path = null;
                String quary = createQuery(c_con, previous_tbl_nm, previous_id, session, previous_tbl_nm_list);
                String used_query = "";
                String[] result = null;
                if (previous_id.matches("[0-9]+")) {
                    used_query = "Select * from " + previous_tbl_nm + " Where id=" + previous_id;
                    result = getVal(c_con, used_query, new String[]{"name", "URI", "path", "hash"});
//                    System.out.println("186 "+Arrays.deepToString(result));
                    c_seelction_name = result[0];
                    history_list.add(c_seelction_name);
                    session.setAttribute("history_list", history_list);
                    
                }
//                String url_query = "SELECT * from " + previous_tbl_nm + " where id=" + previous_id;
                String url = "NA";
                String path = "NA";
                String hash = "NA";
                if (result != null) {
                    url = result[1];
                    path = result[2];
                    hash = result[3];
                }



//                path =;
                if (url != null) {
                    url = URLEncoder.encode(url, "ISO-8859-1");
                } else {
                    url = "NA";
                }
//                String path = "";
                String name = "";
                if (history_list.size() > 1) {
                    name = history_list.get(history_list.size() - 1);
                    if (name.length() > 64) {
                        name = name.substring(0, 63);
                    }
//                    path = history_list.get(history_list.size() - 1);
//                    for (int i = (history_list.size() - 2); (i > 0 && path.length() < 512); i--) {
//                        path = history_list.get(i) + "/" + path;
//                    }
//                    path = "This entry was reached following the path: " + path + ". <Please describe the " + history_list.get(history_list.size() - 1) + " details here>";
                    if (session.getAttribute("person_id") != null) {
//                        if (!name.endsWith("_")) {
//                            name = name + "_";
//                        }
//                        name = name + session.getAttribute("person_id");
                    }
                }
//                else {
//                    path = "The table " + previous_tbl_nm + " does not contain hierarchical relationships";
//                }
//                path = URLEncoder.encode(path, "ISO-8859-1");
                name = URLEncoder.encode(name, "ISO-8859-1");
                String new_link = value_return_to_page + "?current_tbl_nm=" + value_returned_to_table + "&" + previous_tbl_nm + "=" + previous_id + "&value_returned_to_col=" + value_returned_to_col + "&description=" + path + "&name=" + name + "&URI=" + url + "&LINK_TO_FEATURE=" + previous_tbl_nm + "|HASH=" + hash;
//                System.out.println("236 "+new_link);
                if (getDataFromDB(c_con, quary, session) > 0) {
                    printControls(c_con, out, false, c_seelction_name, new_link, session);
                } else {
//                    response.sendRedirect(value_return_to_page + "?current_tbl_nm=" + value_returned_to_table + "&" + previous_tbl_nm + "=" + previous_id + "&value_returned_to_col=" + value_returned_to_col + "&description=" + path + "&name=" + name + "&URL=" + url);
                    response.sendRedirect(new_link);
                    
                }
            }
            out.println("</body>");
            out.println("</html>");
            c_con.close();
        } catch (SQLException ex) {
        } finally {
            out.close();
            close(c_con, null, null);
        }
    }

//    private String getURL4tag(String table_nm, String id){
//       String query="SELECT URL from "+table_nm+" where id="+id;
//        return null ;
//    }
    private String createQuery(Connection c_con, String previous_tbl_nm, String previous_id, HttpSession session, ArrayList<String> previous_tbl_nm_list) {
        String query = null;
        try {
            query = "SELECT * FROM " + previous_tbl_nm + " where id=0 order by name";
            
            if (!previous_id.equals("-1") && previous_id.matches("[0-9]+")) {
                query = "SELECT * FROM " + previous_tbl_nm + " WHERE parent_id=" + previous_id + " and id>0 order by name";
            }
            try {
                getDatasource();
            } catch (ServletException ex) {
                Logger.getLogger(CreateTempalte.class.getName()).log(Level.SEVERE, null, ex);
            }
            if (!c_con.isClosed()) {
                Statement st_1 = c_con.createStatement();
                ResultSet r_1 = null;
                try {
                    r_1 = st_1.executeQuery(query);
                } catch (Exception ex) {
                }
                if (r_1 != null) {
                    ArrayList<String[]> reverse_key_list = Constants.get_reverse_key_contraints(c_con, previous_tbl_nm, previous_tbl_nm_list);
                    if (!r_1.next()) {//if the current query does not return anything jump to the next table
                        // only first table used, consider rivision
                        if (!reverse_key_list.isEmpty() && reverse_key_list.get(0).length >= 3 && reverse_key_list.get(0)[2].equalsIgnoreCase("id")) {
                            String new_table = reverse_key_list.get(0)[0];
                            String new_column = reverse_key_list.get(0)[1];
                            query = "SELECT * FROM " + Constants.get_correct_table_name(c_con, new_table) + " WHERE " + new_column + "=" + previous_id + " and parent_id=0 and id>0 order by name";
                            session.setAttribute("previous_tbl_nm", new_table);
                        }
                    } else {
                        session.setAttribute("previous_tbl_nm", previous_tbl_nm);
                    }
                } else {
                    query = null;
                }
                
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return query;
    }
    
    private void printControls(Connection c_con, PrintWriter out, boolean createnew, String c_seelction_name, String new_link, HttpSession session) {
        String current_tbl_nm = "";
        if (session.getAttribute("previous_tbl_nm") != null) {
            current_tbl_nm = (String) session.getAttribute("previous_tbl_nm");
        }
        ArrayList<String> resultsDisplay_column_nm_l = new ArrayList<>(1);
        resultsDisplay_column_nm_l.add("No results");
        ArrayList<ArrayList<String>> resultsDisplay_data_l = new ArrayList<>(1);
        resultsDisplay_data_l.add(resultsDisplay_column_nm_l);
        if (session.getAttribute("resultsDisplay_column_nm_l") != null) {
            resultsDisplay_column_nm_l = (ArrayList<String>) session.getAttribute("resultsDisplay_column_nm_l");
            if (session.getAttribute("resultsDisplay_data_l") != null) {
                resultsDisplay_data_l = (ArrayList<ArrayList<String>>) session.getAttribute("resultsDisplay_data_l");
            }
        }
        
        String comboList = " <select name=\"previous_id\"  id=\"previous_id\" onchange='displayDetails(\"previous_id\")'>";
        int displaying_coulmn_possition = 1;
        int id_coulmn_possition = 0;
        for (int i = 0; i < resultsDisplay_column_nm_l.size(); i++) {
            if (resultsDisplay_column_nm_l.get(i).toUpperCase().equals(DISPLAY_CLM_NM)) {
                displaying_coulmn_possition = i;
            } else if (resultsDisplay_column_nm_l.get(i).toUpperCase().equals(ID_CLMN_NM)) {
                id_coulmn_possition = i;
            }
        }

//make the load to option faster

//        Timing.setPointer();
        StringBuffer displayDetials = new StringBuffer();
        ArrayList<String> id_list = new ArrayList<>(resultsDisplay_data_l.size());
        for (int i = 0; i < resultsDisplay_data_l.size(); i++) {
            ArrayList<String> content_l = resultsDisplay_data_l.get(i);
            id_list.add(content_l.get(id_coulmn_possition));
            displayDetials.append( "<div id=\"div" + content_l.get(id_coulmn_possition) + "\"><table  class=\"table\">");
            for (int j = 0; j < content_l.size(); j++) {
               displayDetials.append("<tr><td>" + resultsDisplay_column_nm_l.get(j) + "</td><td>" + content_l.get(j) + "</tr>");
            }
            displayDetials.append( "</table></div>");
            comboList = comboList + "<option value=\"" + content_l.get(id_coulmn_possition) + "\">" + content_l.get(displaying_coulmn_possition) + "</option>";
        }
        comboList = comboList + "</select>";
//        System.out.println("325 " + Timing.convert(Timing.getFromlastPointer()));
        ArrayList<String> history_list = new ArrayList<>(1);
        if (session.getAttribute("history_list") != null) {
            history_list = (ArrayList<String>) session.getAttribute("history_list");
        }
        if (!history_list.isEmpty()) {
            boolean headingPrinted = false;
            for (int i = 0; i < history_list.size(); i++) {
                if (!headingPrinted) {
                    headingPrinted = true;
                    out.println("<table class=\"table2\" >");
                    out.println("<tr>");
                    out.println("<th >Path</th>");
                    out.println("<tr>");
                }
                
                out.println("<tr>");
                out.println("<td>");
                out.println(history_list.get(i));
                if (c_seelction_name.equals(history_list.get(i))) {
                    out.println("<a href=\"" + new_link + "\">Use this value </a> ");
                }
                out.println("</td>");
                out.println("</tr>");
            }
            if (headingPrinted) {
                out.println(" </table>");
            }
            
        }
        if (!createnew) {
            out.println("<table class=\"table2\" id=\"mainTable\">");
            out.println("<tr>");
            out.println("<td>");
            out.println("<form accept-charset=\"ISO-8859-1\"  method=\"post\" action=\"" + Constants.getServerName(c_con) + "/Upload/CreateTempalte\">");
            out.println("Select: " + comboList + " ");
            out.println(" <input type=\"hidden\"  name=\"previous_tbl_nm\" value=\"" + current_tbl_nm + "\"> ");
            out.println("<input type=\"submit\" value=\"Next\"/>");
            out.println("</form>");
            out.println("</td>");
            out.println("<td>");
            out.println("</td>");
            out.println("</tr>");
            out.println(" </table>");
        } else {
            out.println("<form accept-charset=\"ISO-8859-1\"  method=\"post\" action=\"" + Constants.getServerName(c_con) + "/Upload/SubmitToDb\">");
            out.println("<table class=\"table\" id=\"insertTable\">");
            out.println("<tr>");
            out.println("<td>");
            out.println(" <input type=\"hidden\"  name=\"current_tbl_nm\" value=\"" + current_tbl_nm + "\"> ");
            out.println(" <input type=\"hidden\"  name=\"operation\" value=\"insert\"> ");
            out.println(" <input type=\"hidden\"  name=\"value_return_to_page\" value=\"" + Constants.getServerName(c_con) + "/Upload/CreateTempalte\"> ");
            out.println(" <input type=\"hidden\"  name=\"priviois_tbl_nm\" value=\"" + current_tbl_nm + "\"> ");
            out.println("</td>");
            out.println("</tr>");
            for (int i = 0; i < resultsDisplay_column_nm_l.size(); i++) {
                out.println("<tr>");
                out.println("<td>");
                out.println(resultsDisplay_column_nm_l.get(i));
                out.println("</td>");
                out.println("<td>");
                out.println(" <input type=\"text\" size=\"100\"  name=\"" + resultsDisplay_column_nm_l.get(i) + "\" value=\"" + resultsDisplay_data_l.get(0).get(i) + "\"> ");
                out.println("</td>");
                out.println("</tr>");
            }
            out.println("<tr>");
            out.println("<td>");
            out.println("<input type=\"submit\" value=\"Create\"/>");
            out.println("</td>");
            out.println("</tr>");
            out.println("</table>");
            out.println("</form>");
        }
        out.println("<div id=\"divmain\">");
        out.println("</div>");
        out.println(displayDetials.toString());
        out.println("<script type=\"text/javascript\">");
        for (int i = 0; i < id_list.size(); i++) {
            out.println(" setVisibility(\"div" + id_list.get(i) + "\",\"hidden\");");
        }
        out.println("var copy=document.getElementById(\"div\"+" + id_list.get(0) + ").firstChild.cloneNode(true);"
                + "document.getElementById(\"divmain\").appendChild(copy);");
        out.println(" </script>");
    }

//    private String getName(String query) {
//        String name = "NA";
//        try {
//            Connection ncon = dataSource.getConnection();
//            if (!ncon.isClosed() && query != null && !query.isEmpty()) {
//                Statement st_1 = ncon.createStatement();
//                ResultSet r_1 = st_1.executeQuery(query);
//                while (r_1.next()) {
//                    name = r_1.getString("name");
//                }
//            }
//            if (!ncon.isClosed()) {
//                ncon.close();
//            }
//        } catch (SQLException ex) {
//            ex.printStackTrace();
//        }
//        return name;
//    }
    /*
     MethodID=CT2
     */
    private String[] getVal(Connection c_con, String query, String[] column_name_a) {
        String[] val = new String[column_name_a.length];
        for (int i = 0; i < val.length; i++) {
            val[i] = "NA";
        }
        try {
            if (!c_con.isClosed() && query != null && !query.isEmpty()) {
                Statement st_1 = c_con.createStatement();
                ResultSet r_1 = st_1.executeQuery(query);
                int c_count = r_1.getMetaData().getColumnCount() + 1;
                int[] column_name_pos = new int[column_name_a.length];
                for (int i = 0; i < column_name_pos.length; i++) {
                    column_name_pos[i] = -1;
                    
                }
                for (int i = 1; i < c_count; i++) {
                    for (int j = 0; j < column_name_a.length; j++) {
                        if (r_1.getMetaData().getColumnName(i).equalsIgnoreCase(column_name_a[j])) {
                            column_name_pos[j] = i;
                        }
                    }
                    
                }
                if (r_1.next()) { // && column_name_pos > 0
                    for (int i = 0; i < column_name_pos.length; i++) {
                        if (column_name_pos[i] > 0) {
                            val[i] = r_1.getString(column_name_pos[i]);
                        }
                        
                    }
//                    val = r_1.getString(column_name_pos);
                }
                r_1.close();
                st_1.close();
            }
        } catch (SQLException ex) {
            val[0] = "Error CT2a";
//            ex.printStackTrace();
            System.out.println("Error CT2a:" + ex.getMessage());
        }
        return val;
    }
//     
//    private ArrayList<String> getNameList(ArrayList<String> query_l) {
//        ArrayList<String> name_list = new ArrayList<String>();
//        for (int i = 0; i < query_l.size(); i++) {
//            name_list.addAll(getNameList(query_l.get(i)));
//        }
//        return name_list;
//    }
//
//    private ArrayList<String> getNameList(String query) {
//        ArrayList<String> name_list = new ArrayList<String>();
//        try {
//            Connection ncon = dataSource.getConnection();
//            if (!ncon.isClosed() && query != null && !query.isEmpty()) {
//                Statement st_1 = ncon.createStatement();
//                ResultSet r_1 = st_1.executeQuery(query);
//                while (r_1.next()) {
//                    String c_nm = r_1.getString("name");
//                    if (!name_list.contains(c_nm)) {
//                        name_list.add(c_nm);
//                    }
//                }
//            }
//            if (!ncon.isClosed()) {
//                ncon.close();
//            }
//        } catch (SQLException ex) {
//            ex.printStackTrace();
//        }
//
//        return name_list;
//    }

    private String getParentIdList(Connection c_con, String query, String parent_clmn_name) {
        String id_list = "";
        try {
            
            ArrayList<String> id_l = new ArrayList<>(1);
            if (!c_con.isClosed() && query != null && !query.isEmpty()) {
                Statement st_1 = c_con.createStatement();
                ResultSet r_1 = st_1.executeQuery(query);
                while (r_1.next()) {
                    String parent_id = r_1.getString(parent_clmn_name);
                    if (parent_id != null && !id_l.contains(parent_id)) {
                        id_l.add(parent_id);
                    }
                }
                r_1.close();
                st_1.close();
            }
            if (!id_l.isEmpty()) {
                id_list = id_l.get(0);
            }
            for (int i = 1; i < id_l.size(); i++) {
                String string = id_l.get(i);
                id_list = id_list + "," + id_l.get(i);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        
        return id_list;
    }

//    private String getIdList(Connection String query) {
//        String id_list = "";
//        try {
//            Connection ncon = dataSource.getConnection();
//            if (!ncon.isClosed() && query != null && !query.isEmpty()) {
//                Statement st_1 = ncon.createStatement();
//                ResultSet r_1 = st_1.executeQuery(query);
//                while (r_1.next()) {
//                    if (id_list.isEmpty()) {
//                        id_list = r_1.getInt("id") + "";
//                    } else {
//                        id_list = id_list + "," + r_1.getInt("id");
//                    }
//                }
//            }
//            if (!ncon.isClosed()) {
//                ncon.close();
//            }
//        } catch (SQLException ex) {
//            ex.printStackTrace();
//        }
//
//        return id_list;
//    }
    private String refineentry(String intext) {
        if (intext != null && !intext.isEmpty() && !intext.matches("[0-9A-Za-z_-]+")) {
            HashMap<String, String> char2symbolMap = Constants.getchar2symbolMap();
            ArrayList<String> char_l = new ArrayList(char2symbolMap.keySet());
            for (int i = 0; i < char_l.size(); i++) {
                intext = intext.replaceAll(char_l.get(i), char2symbolMap.get(char_l.get(i)));
            }
        }
        
        return intext;
    }

    /*
     MethodId=CT1
     */
    private int getDataFromDB(Connection c_con, String query, HttpSession session) {
        int numbr_of_records_found = 0;
        try {
//            Timing.setPointer();
//            
            ArrayList<ArrayList<String>> resultsDisplay_data_l = new ArrayList<>();
            ArrayList<String> resultsDisplay_column_nm_l = new ArrayList<>(10);
            if (!c_con.isClosed() && query != null && !query.isEmpty()) {
                Statement st_1 = c_con.createStatement();
                ResultSet r_1 = st_1.executeQuery(query);
                ResultSetMetaData rsmd = r_1.getMetaData();
                int NumOfCol = rsmd.getColumnCount();
                if (NumOfCol > 0) {
                    String table_in_query = Constants.get_correct_table_name(c_con, rsmd.getTableName(1));
                    for (int i = 1; i < NumOfCol + 1; i++) {
                        resultsDisplay_column_nm_l.add(rsmd.getColumnName(i));
                    }
                    String parent_id_column = null;
                    HashMap<String, String[]> constraint_map = Constants.get_key_contraints(c_con, table_in_query, table_in_query, Constants.PARENT_REF_FLAG);
                    if (constraint_map != null && !constraint_map.isEmpty()) {
                        String[] indx_info_map = constraint_map.get(constraint_map.keySet().iterator().next());
                        parent_id_column = indx_info_map[0];
                    }
                    int histry_id = -1;
                    if (parent_id_column != null) {
                        while (r_1.next()) {
                            numbr_of_records_found++;
                            histry_id = r_1.getInt(parent_id_column);
                            ArrayList<String> tmp_s = new ArrayList<>();
                            for (int i = 1; i < NumOfCol + 1; i++) {
                                String valp = null;
                                try {
                                    valp = r_1.getString(i);
                                } catch (Exception ex) {
                                }
                                if (valp != null) {
                                    String val_tmp = valp;
                                    if (val_tmp.startsWith(Constants.SCP_URL_FLAG)) {
                                    } else if (val_tmp.toLowerCase().startsWith("http://") || val_tmp.toLowerCase().startsWith("https://")) {
                                        valp = "<a href=\"" + valp + "\">" + valp + " </a>";
                                    } else if (val_tmp.toLowerCase().startsWith("ftp://") || val_tmp.toLowerCase().startsWith("ftps://")) {
                                        valp = "<a href=\"" + valp + "\">" + valp + " </a>";
                                    }
                                }
                                tmp_s.add(valp);
                            }
                            resultsDisplay_data_l.add(tmp_s);
                        }
                    } else {
                        resultsDisplay_column_nm_l.clear();
                        resultsDisplay_column_nm_l.add("Result");
                        ArrayList<String> tmp_l = new ArrayList<String>(NumOfCol);
                        tmp_l.add("The table " + table_in_query + " does not contain hierarchical relationships");
                    }
                    session.setAttribute("resultsDisplay_column_nm_l", resultsDisplay_column_nm_l);
//                    ArrayList<ArrayList<String>> resultsDisplay_data_l = new ArrayList<>();
//                    resultsDisplay_data_l.addAll(resultsDisplay_data_s);
//                    resultsDisplay_data_s.clear();
                    session.setAttribute("resultsDisplay_data_l", resultsDisplay_data_l);
                }
                r_1.close();
                st_1.close();
            } else {
                System.out.println("failed to establish connection or the query was null");
            }
//            System.out.println("690 "+Timing.convert(Timing.getFromlastPointer()));
        } catch (SQLException ex) {
            System.out.println("Error 548: sql " + ex.getMessage());
            
        }
        
        return numbr_of_records_found;
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
