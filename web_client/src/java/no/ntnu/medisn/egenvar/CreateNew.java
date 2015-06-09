package no.ntnu.medisn.egenvar;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
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
public class CreateNew extends HttpServlet {

    private final String CURRENT_TABLE_NAME_FLAG = "<CURRENT_TABLE>";
    private DataSource dataSource;
    private final int MAXIMUM_TO_DISPLAY = 200;
//    private String current_tbl;
    private final static String CONSTRAINT_REFERENCE_PATTERN = ".*CONSTRAINT.*FOREIGN KEY[^\\(]+\\([^A-Za-z0-9_\\s]{1}([A-Za-z0-9_\\s]+).*REFERENCES[\\s]*[^A-Za-z0-9_\\s]{1}([A-Za-z0-9_\\s]+)[^A-Za-z0-9_\\s]{1}.*\\([^A-Za-z0-9_\\s]{1}(.*)[^A-Za-z0-9_\\s]{1}\\).*";
    private final static String EMAILTONAMEPATTERN = "([^.]+).([^@]+)@(.*)";
    private Pattern ptn_1;
    private Pattern ptn_2;
    private HashMap<String, HashMap<String, Integer>> column_info_map;
    private final String SPLT = "|";
    private HashMap<String, String> special_pages_map;
    private HashMap<String, String> presetvalues_map;
    private HashMap<String, ArrayList<String>> multiple_foreign_key_map;
//    private HashMap<String, HashMap<String, String>> user_info_map;
    private HashMap<String, HashMap<String, String>> help_content_map;
    private HashMap<String, String> getTAGS_map;
    private ArrayList<String> atributes_to_clear_l;
    private ArrayList<String> qr_column_name_l;

    /**
     * Processes requests for both HTTP
     * <code>GET</code> and
     * <code>POST</code> methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs out.println("");
     */
    private void init_CreateNew(Connection c_con) {
        try {
            ptn_1 = Pattern.compile(CONSTRAINT_REFERENCE_PATTERN);
            ptn_2 = Pattern.compile(EMAILTONAMEPATTERN);
            if (atributes_to_clear_l == null) {
                atributes_to_clear_l = new ArrayList<>();
                atributes_to_clear_l.add("session_values_map");
                atributes_to_clear_l.add("entered_values_map");
            }
            if (special_pages_map == null) {
                special_pages_map = new HashMap<>();
                special_pages_map.put(Constants.get_correct_table_name(c_con, "files"), Constants.getServerName(c_con) + "Upload/logintodirectory");
            }
            if (presetvalues_map == null) {
                presetvalues_map = new HashMap<>();
                presetvalues_map.put("parent_id", "0");
            }
            if (help_content_map == null) {
                help_content_map = new HashMap<>();
                HashMap<String, String> help_map_mibbi_user_content = new HashMap<>();
                help_map_mibbi_user_content.put("parent_id", "If this extends a previous entry, use search to locate what is extended, otherwise use 0");
                help_content_map.put(Constants.get_correct_table_name(c_con, "mibbi_user_content"), help_map_mibbi_user_content);

            }
            getTAGS_map = null;
            multiple_foreign_key_map = null;
            if (getTAGS_map == null) {
                getTAGS_map = new HashMap<>();
                getTAGS_map = getLinkToFeature_types(c_con);
            }
            if (qr_column_name_l == null) {
                qr_column_name_l = new ArrayList<>();
                qr_column_name_l.add("QUICK_RESPONSE_CODE");
            }
        } catch (Exception ex) {
        }
    }

    protected void processRequest(HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=ISO-8859-1");
        Connection c_con = null;
        PrintWriter out = null;
        try {
            c_con = getDatasource().getConnection();
            out = response.getWriter();
            HttpSession session = request.getSession(true);
            int user_level = 9;
            if (session.getAttribute("user_level") != null && session.getAttribute("user_level").toString().matches("[0-9]+")) {
                user_level = new Integer(session.getAttribute("user_level").toString());
            }
            if (user_level < 8) {
                init_CreateNew(c_con);
                String resethistory = request.getParameter("resethistory");
                if (resethistory != null && resethistory.equalsIgnoreCase("true")) {
                    Enumeration sess_var_nms = session.getAttributeNames();
                    while (sess_var_nms.hasMoreElements()) {
                        String tmp_nm = (String) sess_var_nms.nextElement();
                        if (tmp_nm.matches("[vrp|vrt]+\\|cn\\|.*")) {
                            session.removeAttribute(tmp_nm);
                        } else if (atributes_to_clear_l.contains(tmp_nm)) {
                            session.removeAttribute(tmp_nm);
                        }
                    }
                }
                Map<String, String[]> entered_values_map_tmp = request.getParameterMap();
//            System.out.println("121 " + entered_values_map_tmp.keySet());
                HashMap<String, String[]> entered_values_map = new HashMap<>();
                if (session.getAttribute("entered_values_map_tmp") != null) {
                    entered_values_map_tmp = (Map<String, String[]>) session.getAttribute("entered_values_map_tmp");
//                Set<String> tmp_set = entered_values_map_tmp.keySet();
//                Iterator<String> tmp_i = tmp_set.iterator();
//                while (tmp_i.hasNext()) {
//                    entered_values_map_old.remove(tmp_i.next());
//                }
//                entered_values_map_tmp.putAll(entered_values_map_old);
                }

                entered_values_map.putAll(entered_values_map_tmp);
                if (session.getAttribute("entered_values_map_old") != null) {
                    Map<String, String[]> entered_values_map_old = (Map<String, String[]>) session.getAttribute("entered_values_map_old");
                    Set<String> tmp_set = entered_values_map_old.keySet();
                    Iterator<String> tmp_i = tmp_set.iterator();
                    while (tmp_i.hasNext()) {
                        String ck = tmp_i.next();
                        if (!entered_values_map.containsKey(ck)) {
                            entered_values_map.put(ck, entered_values_map_old.get(ck));
                        }
                    }

                }
                session.setAttribute("entered_values_map_old", entered_values_map);
//            System.out.println("127 " + entered_values_map.keySet());
                HashMap<String, HashMap<String, String>> session_values_map = new HashMap<>();
                if (session.getAttribute("session_values_map") != null) {
                    session_values_map = (HashMap<String, HashMap<String, String>>) session.getAttribute("session_values_map");
                }
                String current_user = "";
                if (request.getUserPrincipal() != null) {
                    String current_user_val = request.getUserPrincipal().getName();
                    if (current_user != null) {
                        current_user = current_user_val;
                        Object person_id = session.getAttribute("person_id");
                        if (person_id != null) {
                            presetvalues_map.put("reporter_id", person_id.toString());
                        }
                    }
                }
                String value_return_to_page = request.getParameter("value_return_to_page");
                String current_tbl_nm = request.getParameter("current_tbl_nm");
                if (current_tbl_nm != null) {
                    session.setAttribute("createnew_current_tbl_nm", current_tbl_nm);
                } else if (session.getAttribute("createnew_current_tbl_nm") != null) {
                    current_tbl_nm = (String) session.getAttribute("createnew_current_tbl_nm");
                }
                String result_from_submit = request.getParameter("result_from_submit");
                String priviois_tbl_nm = request.getParameter("priviois_tbl_nm");
                String operation = request.getParameter("operation");

                if (operation == null) {
                    if (session.getAttribute("createnew_operation") != null) {
                        operation = (String) session.getAttribute("createnew_operation");
                    } else {
                        operation = "insert";
                    }
                } else {
                    session.setAttribute("createnew_operation", operation);
                }


//        String use_template_field="mibbi_structure_component_id";//request.getParameter("use_template_field");

                String retunrtoPage_var = "vrp" + SPLT + "cn" + SPLT + current_tbl_nm;
                if (value_return_to_page == null) {
                    if (session.getAttribute(retunrtoPage_var) != null) {
                        value_return_to_page = (String) session.getAttribute(retunrtoPage_var);
                    } else {
                        value_return_to_page = Constants.getServerName(c_con) + "Upload/CreateNew";
                    }
                } else {
                    if (session.getAttribute(retunrtoPage_var) == null) {
                        session.setAttribute(retunrtoPage_var, value_return_to_page);
                    }

                }
                String retunrtoTable_var = "vrt" + SPLT + "cn" + SPLT + current_tbl_nm;
                if (priviois_tbl_nm == null) {
                    if (session.getAttribute(retunrtoTable_var) != null) {
                        priviois_tbl_nm = (String) session.getAttribute(retunrtoTable_var);
                    }
                } else {
                    if (session.getAttribute(retunrtoTable_var) == null) {
                        session.setAttribute(retunrtoTable_var, priviois_tbl_nm);
                    }

                }

                String current_tbl = null;
                if (current_tbl_nm != null && !current_tbl_nm.isEmpty()) {
                    current_tbl = current_tbl_nm;
                }

                if (priviois_tbl_nm == null) {
                    priviois_tbl_nm = current_tbl;
                }
                String value_returned_to_col = request.getParameter("value_returned_to_col");


                if (!presetvalues_map.containsKey("reporter_id")) {
                    if (request.getUserPrincipal() != null) {
                        String current_user_email = request.getUserPrincipal().getName();
                        if (current_user_email != null) {
                            int reporter_id = user_has_person_record(c_con, current_user);
                            if (reporter_id >= 0) {
                                presetvalues_map.put("reporter_id", reporter_id + "");
                            }
                        }
                    }
                }



                out.println("<!DOCTYPE html>");
                out.println("<html>");
                out.println("<head>");
                out.println("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\">");
                out.println("<link HREF=\"" + Constants.getServerName(c_con) + "resources/egenStyler.css\"  REL=\"stylesheet\" TYPE=\"text/css\" >");
                out.println("<title>CreateNew</title>");
                out.println(" <script language = JavaScript> ");
                out.println("function setStatus(target, value){ "
                        + "    if(document.getElementById(target)!=null){"
                        + "      document.getElementById(target).disabled= value;"
                        + "     }"
                        + "  }");
                out.println("</script>");
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
                out.println("<h1>CreateNew " + current_tbl + "</h1>");
                boolean fist_time_profile = false;
                if (!presetvalues_map.containsKey("reporter_id")) {
                    current_tbl = Constants.get_correct_table_name(c_con, "person");
                    out.println("<h4>" + current_user + " does not have a user profile associated with it. Please create one to use the eGenVar datamanagement system</h4>");
                    fist_time_profile = true;
                }
                if (special_pages_map.containsKey(current_tbl_nm)) {
                    out.println("<h4>Please consider using the advaced mode to populate this table <a href=\"" + special_pages_map.get(current_tbl_nm) + "\"> (go to advaced mode) </a></h4>");
                }

                if (current_tbl != null && (result_from_submit == null || result_from_submit.equalsIgnoreCase("false"))) {
                    reTreiveFromDatabase(c_con, current_tbl, priviois_tbl_nm, value_return_to_page, entered_values_map,
                            session_values_map, session, current_user, value_returned_to_col, out,
                            operation, fist_time_profile);
                } else if (result_from_submit != null && !result_from_submit.isEmpty()) {
//                String sql = result_from_submit;
                    out.println("<p><error>Error: Undefinedm operaration</<error></p>");
                } else {
                    out.println("<p><<error>Error: the requested table was not found or you do not have permission to modify it</<error></p>");
                }
                out.println("</body>");
                out.println("</html>");
                c_con.close();
            } else {
                response.sendRedirect(Constants.getServerName(c_con) + "errormsgs?error=403");
            }



        } catch (SQLException ex) {
            out.println("<p><<error>Error: CN1a:" + ex.getMessage() + "</<error></p>");
        } finally {
            close(c_con, null, null);
            if (out != null) {
                out.close();
            }
        }
    }

    /*
     MethodID=CN2
     * http://fibula.medisin.ntnu.no/hunt-db/
     */
    private HashMap<String, String> getLinkToFeature_types(Connection c_con) {
        if (multiple_foreign_key_map == null) {
            multiple_foreign_key_map = new HashMap<>();
        }
        try {
            String table_name_tag = "<TABLE_NM>";
            String title_tag = "<TITLE>";
            String link_url = "<a href=\"" + Constants.getServerName(c_con) + "Upload/CreateTempalte?function=reset&previous_id=0&value_return_to_page=" + Constants.getServerName(c_con) + "Upload/CreateNew&previous_tbl_nm=" + table_name_tag + "&previous_id=0&value_returned_to_col=link_to_feature&value_returned_to_table=" + CURRENT_TABLE_NAME_FLAG + "\" >" + title_tag + "</a>";
            if (c_con != null) {
                try {
                    String links = "";
                    if (!c_con.isClosed()) {
                        ResultSet r_1 = c_con.createStatement().executeQuery("select distinct table_name from " + Constants.get_correct_table_name(c_con, "tablename2feature"));

                        ArrayList<String> ref_tbl_l = new ArrayList<>(1);
                        ArrayList<String> tagable_tbl_l = new ArrayList<>(1);
                        int count = 0;
                        while (r_1.next()) {
                            String c_tbl_nm = r_1.getString(1);
                            if (c_tbl_nm.toUpperCase().endsWith(Constants.TAGSOURCE_TABLE)) {
                                count++;
                                c_tbl_nm = Constants.get_correct_table_name(c_con, c_tbl_nm);
                                String tag_name = (c_tbl_nm.split("\\.")[c_tbl_nm.split("\\.").length - 1]).replace(Constants.TAGSOURCE_TABLE, "");
                                ref_tbl_l.add(c_tbl_nm);
                                String new_link = link_url.replace(table_name_tag, c_tbl_nm).replace(title_tag, tag_name);
                                if (links.isEmpty()) {
                                    links = count + ") " + new_link;
                                } else {
                                    links = links + " <br />" + count + ") " + new_link;
                                }
                            } else if (c_tbl_nm.toUpperCase().endsWith(Constants.TAGABLE_TABLE)) {
                                c_tbl_nm = Constants.get_correct_table_name(c_con, c_tbl_nm);
                                tagable_tbl_l.add(c_tbl_nm);

                            }
                        }
                        for (int i = 0; i < tagable_tbl_l.size(); i++) {
                            multiple_foreign_key_map.put(tagable_tbl_l.get(i), ref_tbl_l);
                        }

//                        Constants.get_correct_table_name(DataSource dataSource,String in_name);
                    }
                    getTAGS_map.put("link_to_feature", links);
                    getTAGS_map.put("LINK_TO_FEATURE", links);
                } catch (SQLException ex) {
                    System.out.println("CN2b " + ex.getMessage());
                }
            }
        } catch (Exception ex) {
            System.out.println("CN2a " + ex.getMessage());
        }

        return getTAGS_map;
    }


    /*
     MethodID=CN1
     * 
     */
    private int user_has_person_record(Connection c_con, String current_user) {
        int person_id = -1;
        try {
            String sql = "SELECT id from " + Constants.get_correct_table_name(c_con, "person") + " WHERE email='" + current_user + "'";
            if (c_con != null) {
                Connection ncon;
                try {
                    if (!c_con.isClosed()) {
                        ResultSet r_1 = c_con.createStatement().executeQuery(sql);
                        while (r_1.next()) {
                            person_id = r_1.getInt(1);
                        }
                    }
                } catch (SQLException ex) {
                    System.out.println("CN1b " + ex.getMessage());
                }
            }
        } catch (Exception ex) {
            System.out.println("CN1a " + ex.getMessage());
        }
        return person_id;
    }

    private void reTreiveFromDatabase(Connection c_con, String current_tbl_nm, String priviois_tbl_nm, String value_return_to_page,
            HashMap<String, String[]> entered_values_map, HashMap<String, HashMap<String, String>> session_values_map,
            HttpSession session, String current_user, String value_returned_to_col,
            PrintWriter out, String operation, boolean fisttime_profile) {
        try {
            String messages = null;
            current_tbl_nm = Constants.get_correct_table_name(c_con, current_tbl_nm);
            ArrayList<String> auto_increment_col_l = Constants.getAutoIncrementolumn_names(c_con, current_tbl_nm);
            ArrayList<String> uniquet_col_l = Constants.getUniqueList(current_tbl_nm, c_con);
//            System.out.println("368 current_tbl_nm="+current_tbl_nm);
            if (!c_con.isClosed() && out != null && current_tbl_nm != null) {
                HashMap<String, String> help_map = help_content_map.get(current_tbl_nm);
                if (help_map == null) {
                    help_map = new HashMap<>();
                }
                HashMap<String, String[]> key_contraints_map = Constants.get_key_contraints(c_con, current_tbl_nm, 2);

                out.println("<form accept-charset=\"ISO-8859-1\"  method=\"post\" action=\"" + Constants.getServerName(c_con) + "Upload/SubmitToDb\">");
                out.println("<table border=\"1\" cellpadding=\"0\" cellspacing=\"1\" width=\"100%\">");
                ArrayList<String> column_names = Constants.getColumn_names(c_con, current_tbl_nm);
                ArrayList<String> manual_data_fileds_l = new ArrayList<>(column_names.size());
                boolean search_option_added = false;
                out.println("<tr>");
                out.println("<td>Table updated</td><td colspan=\"2\" ><input type=\"text\" size=\"80\"  readonly=\"readonly\" name=\"current_tbl_nm\" value=\"" + current_tbl_nm + "\">(Columns with * requires unique values)</td>");
                out.println("</tr>");
                HashMap<String, Integer> colulmn_inf = get_key_contraints_column_info(c_con, current_tbl_nm);
                for (int i = 0; i < column_names.size(); i++) {
                    String col_nm = column_names.get(i);
                    String u = "";
                    if (getIgnoreCase(uniquet_col_l, col_nm) != null) {
                        u = "*";
                    }
                    String col_nm_tr = getRealKey(entered_values_map.keySet(), col_nm);
//                    System.out.println("427 "+col_nm+"\t"+col_nm_tr);
                    if (col_nm_tr == null) {
                        col_nm_tr = col_nm;
                    }
                    String col_nm_keynm = getRealKey(key_contraints_map.keySet(), col_nm);
                    int colum_max_length = colulmn_inf.get(col_nm);
                    int coumn_display_size = 40;
                    if (colum_max_length > 10) {
                        coumn_display_size = colum_max_length;
                    }
                    if (qr_column_name_l.contains(col_nm.toUpperCase())) {
                        out.println("<tr><td>" + col_nm + "</td><td><boldx>For this record, QR details will be generated after submitting</boldx></td></tr>");
                    } else if (getTAGS_map.containsKey(col_nm)) {
                        String cval = null;
                        String tr_current_tbl_nm = getRealKey(multiple_foreign_key_map.keySet(), current_tbl_nm);
                        if (tr_current_tbl_nm != null) {
                            ArrayList<String> multy_foreign_key_list = multiple_foreign_key_map.get(tr_current_tbl_nm);
//                            ArrayList<String> tmp = new ArrayList<>(entered_values_map.keySet());
//                            for (int j = 0; j < tmp.size(); j++) {
//                                System.out.println("411 "+tmp.get(j)+"=>"+Arrays.deepToString(entered_values_map.get(tmp.get(j))));
//                                
//                            }
//                            for (int j = 0; (j < multy_foreign_key_list.size() && cval == null); j++) {
//                                String c_foreing_tbl = multy_foreign_key_list.get(j);
//                                String c_foreing_tbl_tr = getRealKey(entered_values_map.keySet(), c_foreing_tbl);
//                                if (c_foreing_tbl_tr == null) {
//                                    c_foreing_tbl_tr = c_foreing_tbl;
//                                }
//                                if (entered_values_map.get(c_foreing_tbl_tr) != null && value_returned_to_col != null && value_returned_to_col.equalsIgnoreCase(col_nm)) {
//                                    cval = c_foreing_tbl.split("\\.")[c_foreing_tbl.split("\\.").length - 1] + "=" + entered_values_map.get(c_foreing_tbl_tr)[0];
//                                    if (session_values_map.containsKey(c_foreing_tbl)) {
//                                        session_values_map.get(c_foreing_tbl).put(col_nm, cval);
//                                    } else {
//                                        HashMap<String, String> tmp_map = new HashMap<>();
//                                        tmp_map.put(col_nm, cval);
//                                        session_values_map.put(c_foreing_tbl, tmp_map);
//                                    }
//                                }
//                            }
                            if (entered_values_map.containsKey("LINK_TO_FEATURE")) {
                                cval = entered_values_map.get("LINK_TO_FEATURE")[0].split("\\.")[entered_values_map.get("LINK_TO_FEATURE")[0].split("\\.").length - 1];
                                String c_foreing_tbl = cval.split("=")[0];
                                if (session_values_map.containsKey(c_foreing_tbl)) {
                                    session_values_map.get(c_foreing_tbl).put(col_nm, cval);
                                } else {
                                    HashMap<String, String> tmp_map = new HashMap<>();
                                    tmp_map.put(col_nm, cval);
                                    session_values_map.put(c_foreing_tbl, tmp_map);
                                }
                            }

                            if (cval == null) {
                                for (int j = 0; (j < multy_foreign_key_list.size() && cval == null); j++) {
                                    String c_foreing_tbl = multy_foreign_key_list.get(j);
                                    if (session_values_map.containsKey(c_foreing_tbl) && session_values_map.get(c_foreing_tbl).containsKey(col_nm) && session_values_map.get(c_foreing_tbl).get(col_nm) != null) {
                                        cval = session_values_map.get(c_foreing_tbl).get(col_nm);
                                    }
                                }
                            }
                        }
//                        System.out.println("433 "+col_nm+"=>"+cval);
                        if (getTAGS_map.containsKey(col_nm)) {
                            if (cval == null) {
                                cval = "This value can only be filled using the wizard (Click on the tag type to start)";
                            }
                            manual_data_fileds_l.add("<tr><td>" + col_nm + u + "</td><td><input type=\"text\" name=\"" + col_nm + "\" size=\"60\" readonly=\"readonly\" required=\"required\" title=\"Please select the type if tag (Click on the tag type to start)\" value=\"" + cval + "\"></td><td>" + getTAGS_map.get(col_nm).replaceAll(CURRENT_TABLE_NAME_FLAG, current_tbl_nm) + "</td></tr>"); //onchange='setStatus(\"div\",\"false\")'
                        } else {
                            if (entered_values_map.containsKey(col_nm_tr)) {
                                cval = entered_values_map.get(col_nm_tr)[0];
                                if (session_values_map.containsKey(col_nm)) { // shouldnt this be table
                                    session_values_map.get(col_nm).put(col_nm, cval);
                                } else {
                                    HashMap<String, String> tmp_map = new HashMap<>();
                                    tmp_map.put(col_nm, cval);
                                    session_values_map.put(col_nm, tmp_map);
                                }
                            } else if (session_values_map.containsKey(col_nm) && session_values_map.get(col_nm).containsKey(col_nm) && session_values_map.get(col_nm).get(col_nm) != null) {
                                cval = session_values_map.get(col_nm).get(col_nm);
                            }
                            if (cval == null) {
                                cval = "This value will be filled automatically";
                            }
                            manual_data_fileds_l.add("<td>" + col_nm + u + "</td><td colspan=\"1\"><input type=\"text\" name=\"" + col_nm + "\" readonly=\"readonly\" size=\"40\" value=\"" + cval + "\"></td>");
                        }
                    } else if (key_contraints_map.containsKey(col_nm_keynm) && key_contraints_map.get(col_nm_keynm) != null) {
                        String[] frgn_atrb_a = key_contraints_map.get(col_nm_keynm);
                        String foreing_key_target_table = frgn_atrb_a[1];
                        String foreing_key_target_table_tr = getRealKey(entered_values_map.keySet(), foreing_key_target_table);
                        if (foreing_key_target_table_tr == null) {
                            foreing_key_target_table_tr = foreing_key_target_table;
                        }
                        out.println("<tr>");
                        if (foreing_key_target_table != null) {
                            out.println("<td>" + col_nm + u + "</td>");
                            String cval = null;
//                            if (session_values_map.containsKey(foreing_key_target_table)) {
//                            }
                            if (foreing_key_target_table_tr != null && entered_values_map.get(foreing_key_target_table_tr) != null && value_returned_to_col != null && value_returned_to_col.equalsIgnoreCase(col_nm_tr)) {
                                cval = entered_values_map.get(foreing_key_target_table_tr)[0];
                                if (session_values_map.containsKey(foreing_key_target_table)) {
                                    session_values_map.get(foreing_key_target_table).put(col_nm, cval);
                                } else {
                                    HashMap<String, String> tmp_map = new HashMap<>();
                                    tmp_map.put(col_nm, cval);
                                    session_values_map.put(foreing_key_target_table, tmp_map);
                                }
                            } else if (session_values_map.containsKey(foreing_key_target_table) && session_values_map.get(foreing_key_target_table).containsKey(col_nm) && session_values_map.get(foreing_key_target_table).get(col_nm) != null) {
                                cval = session_values_map.get(foreing_key_target_table).get(col_nm);
                            }
                            if (Constants.getType(c_con, current_tbl_nm, column_names.get(i)) == Types.TIMESTAMP) {
                                String DATE_FORMAT_1 = "yyyy-MM-dd HH:mm:ss"; // 
                                java.text.SimpleDateFormat sdf_2 = new java.text.SimpleDateFormat(DATE_FORMAT_1);
                                sdf_2.setTimeZone(TimeZone.getDefault());
                                java.util.Date today = new java.util.Date();
                                String timestp = sdf_2.format(today.getTime());
                                cval = timestp;
                            }
                            if (cval == null || cval.isEmpty()) {
                                if (column_names.get(i).equalsIgnoreCase("email")) {
                                    cval = current_user;
                                } else {
                                    if (presetvalues_map.containsKey(column_names.get(i))) {
                                        cval = presetvalues_map.get(column_names.get(i));
                                    } else {
                                        cval = "";
                                    }
                                }
                            }
                            //textarea rows="10" cols="20"
//                            out.println("<td><input type=\"text\" size=\"40\" readonly=\"readonly\" name=\"" + col_nm + "\" value=\"" + cval + "\" required=\"required\" pattern=\"[0-9]+\" title=\"Needs to be filled by selecting a value from another table. If you do not find the value you want, please create that first and then return to this step\">");
//                            String query = "select * from " + foreing_key_target_table;
//                            if (operation.equalsIgnoreCase("insert_batch")) {
//                                query = "select * from " + foreing_key_target_table + " WHERE id in (" + "'" + cval.replaceAll("\\[", "").replaceAll("\\]", "").replaceAll("'", "").replaceAll(",", "','") + "'" + ")|" + foreing_key_target_table;
//                            }
//                            try {
//                                query = URLEncoder.encode(query, "ISO-8859-1");
//                            } catch (UnsupportedEncodingException ex) {
//                                ex.printStackTrace();
//                            }
                            out.println("<td colspan=\"2\">");
                            //    link = "  <h8>(" + t_split_a[1] + " matches)</h8> </td>" + "<td colspan=\"2\"> <a target=\"_blank\" href=\"" + Constants.getServerName() + "Search/SearchResults3?" + Constants.TABLE_TO_USE_FLAG + t_split_a[0] + "=" + t_split_a[2] + "&reset=true\">" + t_split_a[2] + "</a></td>";

//                            out.println("<input type=\"text\" size=\"40\"  name=\"" + col_nm + "\" value=\"" + cval + "\" required=\"required\" pattern=\"[0-9]+\" title=\"Needs to be filled by selecting a value from another table. If you do not find the value you want, please create that first and then return to this step\">");
                            if (operation.equalsIgnoreCase("insert_batch")) {
                                out.println("<input type=\"text\" size=\"40\" readonly=\"readonly\"  name=\"" + col_nm + "\" value=\"" + cval + "\" required=\"required\" pattern=\"[0-9]+\" title=\"Needs to be filled by selecting a value from another table. If you do not find the value you want, please create that first and then return to this step\">");
                                out.println("<a target=\"_blank\" title=\"Opens in new Window\" href=\"" + Constants.getServerName(c_con) + "Search/SearchResults3?TABLETOUSE_" + foreing_key_target_table + "=" + cval + "&value_returned_to_col=" + col_nm + "&value_return_to_table=" + current_tbl_nm + "&reset=true\" >View details</a>");
                            } else if (foreing_key_target_table.equalsIgnoreCase(current_tbl_nm)) {
                                if (current_tbl_nm.toUpperCase().contains(Constants.TAGSOURCE_TABLE)) {
                                    String link_url = "<a href=\"" + Constants.getServerName(c_con) + "Upload/CreateTempalte?function=reset&previous_id=0&value_return_to_page=" + Constants.getServerName(c_con) + "Upload/CreateNew&previous_tbl_nm=" + current_tbl_nm + "&value_returned_to_col=" + col_nm + "&value_returned_to_table=" + foreing_key_target_table + "\" >Select a parent</a>";
                                    out.println("<input type=\"text\" size=\"40\"  name=\"" + col_nm + "\" value=\"" + cval + "\" required=\"required\" pattern=\"[0-9]+\" title=\"Needs to be filled by selecting a value from another table. If you do not find the value you want, please create that first and then return to this step\">");
                                    out.println(link_url);

                                } else {//batch_mode
                                    out.println("<inpformut type=\"text\" size=\"80\"  name=\"" + col_nm + "\" value=\"" + cval + "\" required=\"required\" pattern=\"[0-9]+\" title=\"Needs to be filled by selecting a value from another table. If you do not find the value you want, please create that first and then return to this step\">");
//                                    out.println("<a href=\"" + Constants.getServerName() + "Search/SearchResults3?serachType=id&current_tbl_nm=" + foreing_key_target_table + "&value_return_to_page=" + Constants.getServerName() + "Upload/CreateNew&reload=true&allowSelection=true&previous_tbl_nm=" + current_tbl_nm + "&operation=select&creation_allowed=false&value_returned_to_col=" + column_names.get(i) + "\" >Select one </a>");
//                                    out.println("<a href=\"" + Constants.getServerName() + "Search/SearchResults3?serachType=id&current_tbl_nm=" + foreing_key_target_table + "&value_return_to_page=" + Constants.getServerName() + "Upload/CreateNew&reload=true&allowSelection=true&previous_tbl_nm=" + current_tbl_nm + "&operation=" + Constants.MULTISELECT_OP_FLAG + "&creation_allowed=false&value_returned_to_col=" + column_names.get(i) + "\" > Select meny</a>");
                                    out.println("<a href=\"" + Constants.getServerName(c_con) + "Search/SearchResults3?" + Constants.TABLE_TO_USE_FLAG + foreing_key_target_table + "=" + Constants.GET_ALL_FLAG + "&operation=" + Constants.SELECT_OP_FLAG + "&value_returned_to_col=" + col_nm + "&value_return_to_table=" + foreing_key_target_table + "&reset=true\" > Select meny</a>");
                                    out.println("<a href=\"" + Constants.getServerName(c_con) + "Search/SearchResults3?" + Constants.TABLE_TO_USE_FLAG + foreing_key_target_table + "=" + Constants.GET_ALL_FLAG + "&operation=" + Constants.SELECT_OP_FLAG + "&value_returned_to_col=" + col_nm + "&value_return_to_table=" + foreing_key_target_table + "&batch_mode=true&reset=true\" > Select meny</a>");
                                }

                            } else {
                                if (fisttime_profile) {
                                    out.println("<input type=\"text\" size=\"40\"  name=\"" + col_nm + "\" value=\"1\" disabled=\"disabled\"  pattern=\"[0-9]+\" title=\"This value could be eddited later\"> This value could be eddited later");
                                } else {//pattern=\"[0-9]+\"                                   
                                    out.println("<input type=\"text\" size=\"40\"  name=\"" + col_nm + "\" value=\"" + cval + "\"   title=\"Needs to be filled by selecting a value from another table. If you do not find the value you want, please create that first and then return to this step (Tip: you could do this in a different window)\">");//required=\"required\"
//                                    out.println("<a href=\"" + Constants.getServerName() + "Search/SearchResults3?serachType=id&current_tbl_nm=" + foreing_key_target_table + "&value_return_to_page=" + Constants.getServerName() + "Upload/CreateNew&reload=true&allowSelection=true&previous_tbl_nm=" + current_tbl_nm + "&operation=select&creation_allowed=true&value_returned_to_col=" + column_names.get(i) + "\" >Select one </a>");
//                                    out.println("<a href=\"" + Constants.getServerName() + "Search/SearchResults3?serachType=id&current_tbl_nm=" + foreing_key_target_table + "&value_return_to_page=" + Constants.getServerName() + "Upload/CreateNew&reload=true&allowSelection=true&previous_tbl_nm=" + current_tbl_nm + "&operation=" + Constants.MULTISELECT_OP_FLAG + "&creation_allowed=true&value_returned_to_col=" + column_names.get(i) + "\" > |Select meny</a>");
                                    out.println("<a href=\"" + Constants.getServerName(c_con) + "Search/SearchResults3?" + Constants.TABLE_TO_USE_FLAG + foreing_key_target_table + "=" + Constants.GET_ALL_FLAG + "&operation=" + Constants.SELECT_OP_FLAG + "&value_returned_to_col=" + col_nm + "&value_return_to_table=" + foreing_key_target_table + "&reset=true\" >Select one </a>");
                                    out.println("<a href=\"" + Constants.getServerName(c_con) + "Search/SearchResults3?" + Constants.TABLE_TO_USE_FLAG + foreing_key_target_table + "=" + Constants.GET_ALL_FLAG + "&operation=" + Constants.SELECT_OP_FLAG + "&value_returned_to_col=" + col_nm + "&value_return_to_table=" + foreing_key_target_table + "&batch_mode=true&reset=true\" >Select meny </a>");

                                }
                            }
                            if (getTAGS_map.containsKey(col_nm)) {
                                out.println(getTAGS_map.get(col_nm).replaceAll(CURRENT_TABLE_NAME_FLAG, current_tbl_nm));
//                                out.println("<a href=\"" + Constants.getServerName() + "Upload/CreateTempalte?value_return_to_page=" + Constants.getServerName() + "Upload/CreateNew&previous_tbl_nm=mibbi_module&value_returned_to_col=" + rsmd.getColumnName(i) + "&value_returned_to_table=" + current_tbl_nm + "\" > | Get help from wizard</a>");
                            }
                            if (help_map.containsKey(col_nm)) {
                                out.println("<br /><tips>(Tip : " + help_map.get(col_nm) + ")</tips>");
                            }
                            out.println("</td>");
                            search_option_added = true;
                        } else {
                            out.println("<td><font color=\"red\">Error</font></td>" + "<td><input type=\"text\" name=\"" + col_nm + "\" value=\"Error resolving table  for " + foreing_key_target_table + "\"></td>");
                        }
                        out.println("</tr>");

                    } else if (col_nm.equalsIgnoreCase("id")) {
                        out.println("<td><input type=\"hidden\" name=\"" + col_nm + "\" value=\" (select max(" + col_nm + ")+1 from " + current_tbl_nm + ") \"></td>"); //" + operation + "'

//                   } else if (false) {
//                        System.out.println("538 " + current_tbl_nm + "  " + col_nm);
////                        if (current_tbl_nm.equalsIgnoreCase(Constants.get_correct_table_name(dataSource, "person"))) {
////                            out.println("<tr><td>User name</td><td><input type=\"text\" size=\"40\" disabled=\"disabled\" name=\"" + col_nm + "\" value=\"" + current_user + "\"  title=\"This is the user name and filled automatically\"></td></tr>");
////                        } else {
//                        String cval = "";
//                        if (entered_values_map != null && entered_values_map.get(col_nm_tr) != null) {
//                            cval = entered_values_map.get(col_nm_tr)[0];
//                            if (session_values_map.containsKey(col_nm)) {
//                                session_values_map.get(col_nm).put(col_nm, cval);
//                            } else {
//                                HashMap<String, String> tmp_map = new HashMap<String, String>();
//                                tmp_map.put(col_nm, cval);
//                                session_values_map.put(col_nm, tmp_map);
//                            }
//                        } else if (session_values_map.containsKey(col_nm) && session_values_map.get(col_nm).containsKey(col_nm) && session_values_map.get(col_nm).get(col_nm) != null) {
//                            cval = session_values_map.get(col_nm).get(col_nm);
//                        } else if (cval == null || cval.isEmpty()) {
//                            if (column_names.get(i).equalsIgnoreCase("email")) {
//                                cval = current_user;
//                            } else {
//                                if (presetvalues_map.containsKey(column_names.get(i))) {
//                                    cval = presetvalues_map.get(column_names.get(i));
//                                } else {
//                                    cval = "";
//                                }
//                            }
//                        }
//                        out.println("<tr><td>Unique name</td><td colspan=\"2\"><input type=\"text\" size=\"40\"  name=\"" + col_nm_tr + "\" value=\"" + cval + "\" required=\"required\" pattern=\".+\" title=\"Include a unique name\"></tr>");
////                        }

                    } else if (getIgnoreCase(auto_increment_col_l, col_nm) != null) {
                    } else {
                        String cval = null;
                        if (entered_values_map.get(col_nm_tr) != null) {
                            cval = entered_values_map.get(col_nm_tr)[0];
                        }
                        if (cval == null) {
                            if (Constants.getType(c_con, current_tbl_nm, column_names.get(i)) == Types.TIMESTAMP) {
                                String DATE_FORMAT_1 = "yyyy-MM-dd HH:mm:ss"; // 
                                java.text.SimpleDateFormat sdf_2 = new java.text.SimpleDateFormat(DATE_FORMAT_1);
                                sdf_2.setTimeZone(TimeZone.getDefault());
                                java.util.Date today = new java.util.Date();
                                String timestp = sdf_2.format(today.getTime());
                                cval = timestp;
                            } else {
                                if (column_names.get(i).equalsIgnoreCase("email")) {
                                    cval = current_user;
                                } else {
                                    if (presetvalues_map.containsKey(column_names.get(i))) {
                                        cval = presetvalues_map.get(column_names.get(i));
                                    } else {
                                        Matcher mu = ptn_2.matcher(current_user);
                                        if (mu.matches()) {
                                            if (column_names.get(i).equalsIgnoreCase("name")) {
                                                cval = mu.group(1);
                                            } else if (column_names.get(i).equalsIgnoreCase("lastnm")) {
                                                cval = mu.group(2);
                                            } else {
                                                cval = "";
                                            }
                                        } else {
                                            System.out.println("no match");
                                            cval = "";
                                        }
                                    }

                                }
                            }
                        }
//                        if (cval != null) {
//                            if (session_values_map.containsKey(col_nm)) {
//                                session_values_map.get(col_nm).put(col_nm, cval);
//                            } else {
//                                HashMap<String, String> tmp_map = new HashMap<String, String>();
//                                tmp_map.put(col_nm, cval);
//                                session_values_map.put(col_nm, tmp_map);
//                            }
//                        }
                        if (coumn_display_size > 100) {
                            int row_count = (coumn_display_size / 100);
                            if (row_count > 20) {
                                row_count = 20;
                            }
                            manual_data_fileds_l.add("<tr><td>" + col_nm + u + "</td><td colspan=\"2\"><textarea name=\"" + col_nm + "\"  id=\"" + col_nm + "\"  cols=\"100\" rows=\"" + row_count + "\"   >" + cval + "</textarea></td></tr>"); //required=\"required\"

                        } else {
                            manual_data_fileds_l.add("<tr><td>" + col_nm + u + "</td><td colspan=\"2\"><input type=\"text\" size=\"" + coumn_display_size + "\" name=\"" + col_nm + "\" value=\"" + cval + "\" ></td></tr>"); //required=\"required\"

                        }

                    }

                }
                session.setAttribute("session_values_map", session_values_map);
                if (search_option_added && manual_data_fileds_l.size() > 0) {
                    out.println("<tr><td colspan=\"3\"><h8>The follwing fields may be reset when leaving this page, so change them last</h8></td></tr>");
                }
                for (int i = 0; i < manual_data_fileds_l.size(); i++) {
                    out.println(manual_data_fileds_l.get(i));
                }

                out.println("<tr><td colspan=\"3\" align=\"center\"> ");
                out.println("<input type=\"hidden\" name=\"value_return_to_page\" value=\"" + value_return_to_page + "\">");
//                if (operation.equalsIgnoreCase("insert_batch")) {
                out.println("<input type=\"hidden\" name=\"operation\" value=\"" + "insert_batch" + "\">"); //" + operation + "'
//                } else {
//                    out.println("<input type=\"hidden\" name=\"operation\" value=\"" + "insert" + "\">"); //" + operation + "'
//                }

                //This was changed from operation variable to static text insert as the fist time use account creation caursed problems
                out.println("<input type=\"hidden\" name=\"priviois_tbl_nm\" value=\"" + priviois_tbl_nm + "\">");
                out.println("<div id=\"div\"><input type=\"submit\" name=\"submitbtn\" value=\"Submit\" ></div>"); //disabled=\"disabled\"

                out.println("</td></tr>");
                out.println("</table>");
                out.println("</form>");
                if (messages != null) {
                    out.println("<table border=\"1\" cellpadding=\"0\" cellspacing=\"1\" width=\"100%\">");
                    out.println("<tr><td>");
                    out.println(" * " + messages);
                    out.println("</td></tr>");
                    out.println("</table>");
                }

            } else {
                out.println("<p><font color='red'>Error: the requested table was not found or you do not have permission to modify it</font></p>");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private HashMap<String, Integer> get_key_contraints_column_info(Connection c_con, String current_tbl_nm) {
        HashMap<String, Integer> column_details = new HashMap<>();
        if (column_info_map == null) {
            column_info_map = new HashMap<>();
        }
        if (current_tbl_nm != null && !current_tbl_nm.isEmpty() && !current_tbl_nm.equalsIgnoreCase("null") && !column_info_map.containsKey(current_tbl_nm)) {
            try {

                if (!c_con.isClosed()) {
                    DatabaseMetaData metaData = c_con.getMetaData();
                    String tablenm4metadata = current_tbl_nm;
                    if (current_tbl_nm.contains(".")) {
                        tablenm4metadata = current_tbl_nm.split("\\.")[1];
                    }
                    ResultSet key_result = metaData.getColumns(null, Constants.getDB_name(c_con), tablenm4metadata, null);
                    if (!key_result.next()) {
                        key_result = metaData.getColumns(null, Constants.getDB_name(c_con).toUpperCase(), tablenm4metadata.toUpperCase(), null);
                    } else {
                        String typename = key_result.getString("TYPE_NAME");
                        String name = key_result.getString("COLUMN_NAME");
                        if (typename.equalsIgnoreCase("VARCHAR")) {
                            int size = key_result.getInt("COLUMN_SIZE");
                            column_details.put(name, size);
                        } else {
                            column_details.put(name, -1);
                        }
                    }

                    while (key_result.next()) {
                        String typename = key_result.getString("TYPE_NAME");
                        String name = key_result.getString("COLUMN_NAME");
                        if (typename.equalsIgnoreCase("VARCHAR")) {
                            int size = key_result.getInt("COLUMN_SIZE");
                            column_details.put(name, size);
                        } else {
                            column_details.put(name, -1);
                        }
                    }
                }
            } catch (SQLException e) {
                System.out.println("Error 8767" + e.getMessage());
            }
            column_info_map.put(current_tbl_nm, column_details);
        } else {
            column_details = column_info_map.get(current_tbl_nm);
        }
        return column_details;
    }

    private String getRealKey(Set keys, String key) {
        ArrayList<String> key_l = new ArrayList<>(keys);
        key = key.trim();
        String realKey = null;
        for (int i = 0; (i < key_l.size() && realKey == null); i++) {
            if (key_l.get(i).equalsIgnoreCase(key)) {
                realKey = key_l.get(i);
            }
        }
        if (realKey == null) {
            for (int i = 0; (i < key_l.size() && realKey == null); i++) {
                if (key_l.get(i).toUpperCase().endsWith("." + key.toUpperCase())) {
                    realKey = key_l.get(i);
                }
            }
        }
        return realKey;
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
