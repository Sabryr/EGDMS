/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package no.ntnu.medisn.egenvar;

import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.io.StreamCorruptedException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
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
import static no.ntnu.medisn.egenvar.Errormsgs.close;

/**
 *
 * @author sabryr
 */
public class SearchResults3 extends HttpServlet { //implements Runnable

    private DataSource dataSource_disk;
    private DataSource dataSource_mem;
//    private Connection ncon_mem;
//    private Connection ncon_disk;
//    private String refreshrate = "2";
//    private final int MAX_RESULTS_TO_KEEP = 10;
//    private HashMap<String, String> table2descripton_map;
    private HashMap<String, ArrayList<String>> queryExpander_map;
    private final String ID_FLAG = "id"; //modify with the auto increment primary key
    private final String IDFORDIV_SPLIT_FLAG = "DIVID";
    private final String EXPANSION_SPLIT_TOKEN = "===";
    public static final String SEARCH_OP_FLAG = "search";
    Thread searchresults;
    int counter = 0;
//    private final long MEM_terminate = 33554432;
    public final static int MIN_LEVEL_PATH = 2;
    public final static int MIN_LEVEL_DATE = 2;
    public static final String CODE_RESULT_FLAG = "RESULT_CODE";

    private void pageReload(HttpServletResponse response, String url, int caller) {
        try {
            if (!response.isCommitted()) {
                response.sendRedirect(url);
            } else {
                System.out.println("Error:  " + caller + " Refresh failed");
            }
        } catch (IOException ex) {
        }
    }

//    @Override
//    public void init(ServletConfig config) throws ServletException {
//        super.init(config);                  // always!
//        searchresults = new Thread(this);
//        searchresults.setPriority(Thread.MIN_PRIORITY);  // be a good citizen
//        searchresults.start();
//    }
//
//    @Override
//    public void run() {
//    }
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
//        Enumeration<String> en = request.getParameterNames();
        HttpSession session = request.getSession(true);
        GetFromDb get = null;
        LinkedHashMap<String, Object> new_result_map = null;
        boolean reload = false;
//        boolean reseting = false;
        boolean filter_active = false;
        int page = 0; //make this -1 and do not display if less than zero
//        String return_to_table = null;
        String return_to_link = null;
        String current_tbl_nm = null;
        String static_url = null;
        String qr = null;
        String graph = null;
//        boolean finished_refreshing = true;
        String operation = null;
        boolean batch_mode = false;
        String value_return_to_page = null;
        String value_return_to_table = null;
        String value_returned_to_col = null;
        ArrayList<String> history_SR_l = null;
        String static_info_varnm = null;
        String query_used = null;
        String last_search_type = null;
        String suc = null;
        boolean refresh_active = false;
        boolean use_meem_db = false;
   
        Connection c_con = null;
        Connection c_con_op = null;
        try {
            c_con = getDatasource_operational(true, 1).getConnection();
            String query = null;
            if (session.getAttribute("ATRB_LOAD") == null) {
                pageReload(response, Constants.getServerName(c_con) + "index", 131);
            } else {
                     String showSearch_link = "<a style=\"background-color: #0174DF; color: white;\n"
                + "display: block; height: 20px; line-height: 20px; text-decoration:\n"
                + "none; width: 300px; cursor:pointer;\" onmouseover=\"this.style.textDecoration = 'underline';\" onmouseout=\"this.style.textDecoration = 'none';\" "
                + "onclick=\"setVisible('search_results','" + Constants.getServerName(c_con) + "Search/search?data_height=200 #data');\"  >"
                + " Show / Hide search results </a>";
                if (queryExpander_map == null) {
                    getQueryExpander(c_con);
                }
                Map<String, String[]> entered_values_map_tmp = request.getParameterMap();
                if (entered_values_map_tmp != null && !entered_values_map_tmp.isEmpty()) {
                    String c_var = entered_values_map_tmp.keySet().iterator().next();
                    if (c_var != null && !c_var.isEmpty() && c_var.startsWith(Constants.TABLE_TO_USE_FLAG)) {
                        static_info_varnm = c_var;
                        operation = Constants.SERCH_OP_FLAG;
                    } else if (c_var != null && c_var.startsWith(Constants.QUERY_TO_USE_FLAG)) {
                        static_info_varnm = c_var;
                        operation = Constants.SERCH_OP_FLAG;
                    }
                }
                if (operation != null && operation.equals(Constants.SERCH_OP_FLAG)) {
                    use_meem_db = true;
                }
                int server_mode = Constants.getServer_mode(c_con);
                if (request.getParameter("page") != null && request.getParameter("page").matches("[0-9]+")) {
                    page = new Integer(request.getParameter("page"));
                    session.setAttribute("page", page);
                } else {
                    if (session.getAttribute("page") != null) {
                        page = (int) session.getAttribute("page");
                    }
                }

                if (session.getAttribute("isareset") == null && (request.getParameter("reset") != null || (static_info_varnm != null && page < 0))) {
                    session.removeAttribute("split_links_l");
                    session.removeAttribute("filter_split_links_l");
                    session.removeAttribute("query_used");
                    session.removeAttribute("last_search_type");
                    filter_active = false;
                    session.removeAttribute("filter_active");
                    session.removeAttribute(Constants.FROMDB_RESULTS_SESSION_FLAG);
                    session.removeAttribute("query2");
                    value_return_to_page = request.getHeader("Referer");
                    if (value_return_to_page != null) {
                        session.setAttribute("last_Referer", value_return_to_page);
                    } else if (session.getAttribute("last_Referer") != null) {
                        value_return_to_page = session.getAttribute("last_Referer").toString();
                    }

                    session.setAttribute("search_value_return_to_page", value_return_to_page);
                    session.removeAttribute("page");
                    page = 0;
//                    reseting = true;
                }

//                System.out.prin1tln("178 value_return_to_page=" + value_return_to_page + " " + session.getAttribute("search_value_return_to_page") + "  reset)=" + request.getParameter("reset"));
                if (value_return_to_page == null && session.getAttribute("search_value_return_to_page") != null) {
                    value_return_to_page = (String) session.getAttribute("search_value_return_to_page");
                }

                if (value_return_to_page != null && value_return_to_page.contains("?")) {
                    value_return_to_page = value_return_to_page.split("\\?")[0];
                }
//                System.out.println("185 value_return_to_page=" + value_return_to_page);
                if (request.getParameter("query_used") != null) {
                    query_used = request.getParameter("query_used");
                    session.setAttribute("query_used", query_used);
                } else {
                    if (session.getAttribute("query_used") != null) {
                        query_used = (String) session.getAttribute("query_used");
                    }
                }
                if (request.getParameter("last_search_type") != null) {
                    last_search_type = request.getParameter("last_search_type");
                    session.setAttribute("last_search_type", last_search_type);
                } else {
                    if (session.getAttribute("last_search_type") != null) {
                        last_search_type = (String) session.getAttribute("last_search_type");
                    }
                }

                if (query_used == null) {
                    query_used = "NA";
                }
                if (request.getParameter("value_return_to_table") != null) {
                    value_return_to_table = request.getParameter("value_return_to_table");
                    session.setAttribute("value_return_to_table", value_return_to_table);
                } else {
                    if (session.getAttribute("value_return_to_table") != null) {
                        value_return_to_table = (String) session.getAttribute("value_return_to_table");
                    }
                }
                if (request.getParameter(Constants.VALUES_RETURN_TO_COLUMN_FLAG) != null) {
                    value_returned_to_col = request.getParameter(Constants.VALUES_RETURN_TO_COLUMN_FLAG);
                    session.setAttribute(Constants.VALUES_RETURN_TO_COLUMN_FLAG, value_returned_to_col);
                } else {
                    if (session.getAttribute(Constants.VALUES_RETURN_TO_COLUMN_FLAG) != null) {
                        value_returned_to_col = (String) session.getAttribute(Constants.VALUES_RETURN_TO_COLUMN_FLAG);
                    }
                }

                if (request.getParameter("operation") != null) {
                    operation = request.getParameter("operation");
                    session.setAttribute("operation", operation);
                } else {
                    if (session.getAttribute("operation") != null) {
                        operation = (String) session.getAttribute("operation");
                    }
                }
                if (operation != null && operation.equals(Constants.SERCH_OP_FLAG)) {
                    use_meem_db = true;
                }

                if (request.getParameter("batch_mode") != null && request.getParameter("batch_mode").equalsIgnoreCase("TRUE")) {
                    batch_mode = true;
                    session.setAttribute("batch_mode", "TRUE");
                } else {
                    if (session.getAttribute("batch_mode") != null) {
                        if (((String) session.getAttribute("batch_mode")).equalsIgnoreCase("TRUE")) {
                            batch_mode = true;
                        }
                    }
                }

                if (session.getAttribute("current_tbl_nm") != null) {
                    current_tbl_nm = (String) session.getAttribute("current_tbl_nm");
                }

                if (request.getParameter("current_tbl_nm") != null) {
                    current_tbl_nm = request.getParameter("current_tbl_nm");
                    session.setAttribute("current_tbl_nm", current_tbl_nm);
                } else {
                    if (session.getAttribute("current_tbl_nm") != null) {
                        current_tbl_nm = (String) session.getAttribute("current_tbl_nm");
                    }
                }

                if (request.getParameter("return_to_link") != null) {
                    return_to_link = URLDecoder.decode(request.getParameter("return_to_link"), "ISO-8859-1");
                    session.setAttribute("return_to_link", return_to_link);
                }
                if (return_to_link == null) {
                    if (session.getAttribute("return_to_link") != null) {
                        return_to_link = (String) session.getAttribute("return_to_link");
                    }
                }
//                if (request.getParameter("return_to_table") != null) {
//                    return_to_table = request.getParameter("return_to_table");
//                    if (return_to_table == null) {
//                        if (session.getAttribute("return_to_table") == null) {
//                            return_to_table = (String) session.getAttribute("return_to_table");
//                        }
//                    }
//                }
//                System.out.println("187 query2=" + session.getAttribute("query2") + "  request.getParameter(reset)=" + request.getParameter("reset"));
                String last_query = null;
                if (session.getAttribute("query2") != null) {
                    last_query = (String) session.getAttribute("query2");
                }

                String filter_column = null;
                String filter_value = null;
                String filter_table = null;
                if (request.getParameter("filter_column") != null) {
                    filter_column = request.getParameter("filter_column");
                }

                if (request.getParameter("filter_value") != null) {
                    filter_value = request.getParameter("filter_value");
                }

                if (request.getParameter("filter_table") != null) {
                    filter_table = request.getParameter("filter_table");
                }

                if (filter_column != null && filter_value != null) {
                    query = last_query;
                    filter_active = true;
                    session.setAttribute("filter_active", filter_active);
//                    session.setAttribute("page", 1);
                }

                if (request.getParameter("filter_active") != null && request.getParameter("filter_active").toUpperCase().equals("FALSE")) {
                    filter_active = false;
                    session.removeAttribute("filter_active");
                } else {
                    if (session.getAttribute("filter_active") != null) {
                        filter_active = true;
                    }
                }
//         System.out.println("311 filter_table=" + filter_table + "\tfilter_column=" + filter_column + "\tfilter_value=" + filter_value+ " filter_active="+filter_active);

// System.out.println("315 filter_active="+filter_active+" query="+query);
                int user_level = 0;
                if (session.getAttribute("user_level") != null && session.getAttribute("user_level").toString().matches("[0-9]+")) {
                    user_level = new Integer(session.getAttribute("user_level").toString());
                }

                String id_list = "-1";
//                boolean direct_loading = false;
                boolean batch_editable = false;

//                int static_url_id_size = 0;
                boolean make_qr = false;

                boolean creation_allowed = false;


                if (session.getAttribute(Constants.FROMDB_RESULTS_SESSION_FLAG) != null) {
                    new_result_map = (LinkedHashMap<String, Object>) session.getAttribute(Constants.FROMDB_RESULTS_SESSION_FLAG);
                }


                String id_list_fil_nm = "-1";
                if (query == null) {
                    if (static_info_varnm != null) {
                        if (!static_info_varnm.isEmpty() && static_info_varnm.startsWith(Constants.TABLE_TO_USE_FLAG)) {
                            session.removeAttribute("current_tbl_nm");
                            current_tbl_nm = Constants.getErrorMessageTable(c_con);
                            id_list_fil_nm = entered_values_map_tmp.get(static_info_varnm)[0];
                            suc = id_list_fil_nm;
                            if (id_list_fil_nm.matches("[0-9\\,]+") || static_info_varnm.contains("|")) {
//                                System.out.println("360 " + static_info_varnm);
                                batch_editable = true;
                                if (operation == null) {
                                    operation = SEARCH_OP_FLAG;
                                    use_meem_db = true;
                                }
                                current_tbl_nm = static_info_varnm.replace(Constants.TABLE_TO_USE_FLAG, "");
                                String column = "id";
                                if (current_tbl_nm.contains("|")) {
                                    String[] c_split_a = current_tbl_nm.split("\\|");
                                    current_tbl_nm = c_split_a[0];
                                    column = c_split_a[1];
                                }

                                current_tbl_nm = Constants.get_correct_table_name(c_con, current_tbl_nm);

                                if (current_tbl_nm != null) {
                                    session.setAttribute("current_tbl_nm", current_tbl_nm);
                                    if (id_list_fil_nm.matches("[0-9\\,]+")) { //Use typ.sql.integer insteaD
                                        query = "select * from " + Constants.get_correct_table_name(c_con, current_tbl_nm) + " where " + column + " in (" + id_list_fil_nm + ")";
                                    } else {
                                        query = "select * from " + Constants.get_correct_table_name(c_con, current_tbl_nm) + " where " + column + " in ('" + id_list_fil_nm.replaceAll(",", "','") + "')";
                                    }

                                } else {
                                    query = "select message from " + Constants.getErrorMessageTable(c_con) + " where id=1";
                                }
//                                System.out.println("398 "+query);
                            } else if (id_list_fil_nm.toUpperCase().equals(Constants.GET_ALL_FLAG)) {
                                make_qr = true;
//                                getDatasource(use_meem_db);
                                current_tbl_nm = Constants.get_correct_table_name(c_con, static_info_varnm.replace(Constants.TABLE_TO_USE_FLAG, ""));
                                if (current_tbl_nm != null) {
                                    session.setAttribute("current_tbl_nm", current_tbl_nm);
                                    query = "select * from " + Constants.get_correct_table_name(c_con, current_tbl_nm);
                                } else {
                                    query = "select * from " + Constants.get_correct_table_name(c_con, current_tbl_nm) + " where id=1";
                                }
                            } else {
                                String file_url = Constants.getDocRoot(c_con) + id_list_fil_nm;
                                Object id_ob = readResultsFromFile(file_url);
                                if (id_ob != null) {
                                    id_list = id_ob.toString();
                                    String[] split_a = id_list.split("\\|\\|");
                                    if (split_a.length > 1) {
                                        current_tbl_nm = split_a[0];
                                        id_list = split_a[1];
                                    }

                                    String colmn = null;
                                    id_list = id_list.replace("[", "").replace("]", "").replaceAll("\\s", "").trim();
                                    String[] tmp_s = id_list.split("\\(");
                                    if (tmp_s.length > 1) {
                                        colmn = tmp_s[0].trim();
                                        id_list = "(" + tmp_s[1].trim();
                                    }
                                    if (colmn == null || colmn.isEmpty()) {
                                        colmn = "id";
                                    }

                                    batch_editable = true;
                                    if (operation == null) {
                                        operation = SEARCH_OP_FLAG;
                                        use_meem_db = true;
                                    }
                                    if (id_list != null && !id_list.isEmpty() && (id_list.matches("[0-9\\-]+") || id_list.matches("[0-9\\,\\-]+") || id_list.matches("[0-9\\,\\)\\(\\-]+"))) {
//                                    static_url_id_size = (id_list.length() - (id_list.replaceAll(",", "")).length()) + 1;
                                        if (!id_list.startsWith("(")) {
                                            id_list = "(" + id_list;
                                        }
                                        if (!id_list.endsWith(")")) {
                                            id_list = id_list + ")";
                                        }
                                        make_qr = true;
                                        current_tbl_nm = Constants.get_correct_table_name(c_con, current_tbl_nm.replace(Constants.TABLE_TO_USE_FLAG, ""));
                                        if (current_tbl_nm != null) {
                                            session.setAttribute("current_tbl_nm", current_tbl_nm);
                                            query = "select * from " + Constants.get_correct_table_name(c_con, current_tbl_nm) + " where " + colmn + " in " + id_list;
                                        } else {
                                            query = "select message from " + Constants.getErrorMessageTable(c_con) + " where id=1";
                                        }

                                    } else if (id_list != null && id_list.toUpperCase().equals(Constants.GET_ALL_FLAG)) {
                                        make_qr = true;
                                        current_tbl_nm = current_tbl_nm.replace(Constants.TABLE_TO_USE_FLAG, "");
                                        session.setAttribute("current_tbl_nm", current_tbl_nm);
                                        query = "select * from " + Constants.get_correct_table_name(c_con, current_tbl_nm);
                                    } else {
                                        query = "select message from " + Constants.getErrorMessageTable(c_con) + " where id=1";
                                    }
                                } else {
                                    query = "select message from " + Constants.getErrorMessageTable(c_con) + " where id=1";
                                }
                            }

                        } else if (static_info_varnm.startsWith(Constants.QUERY_TO_USE_FLAG)) {
                            session.removeAttribute("current_tbl_nm");
                            current_tbl_nm = Constants.getErrorMessageTable(c_con);
                            String query_fil_nm = entered_values_map_tmp.get(static_info_varnm)[0];
                            suc = query_fil_nm;
                            String file_url = Constants.getDocRoot(c_con) + query_fil_nm;
                            Object query_ob = readResultsFromFile(file_url);
                            if (query_ob != null) {
                                String info = query_ob.toString();
                                String[] split_a = info.split("\\|\\|");
                                if (split_a.length > 1) {
                                    current_tbl_nm = split_a[0];
                                    current_tbl_nm = Constants.get_correct_table_name(c_con, current_tbl_nm);
                                    if (current_tbl_nm != null) {
                                        session.setAttribute("current_tbl_nm", current_tbl_nm);
                                    }
                                    query = split_a[1];
                                }
                            } else {
                                query = "select message from " + Constants.getErrorMessageTable(c_con) + " where id=1";
                            }
                        }
                    }
                    if (query == null && entered_values_map_tmp != null && !entered_values_map_tmp.isEmpty()) {
                        if (current_tbl_nm != null && (operation == null || operation.equalsIgnoreCase("update") || operation.equalsIgnoreCase("delete"))) {
                            query = "select * from " + Constants.get_correct_table_name(c_con, current_tbl_nm) + "";
                        }
                    }
                }

                if (suc == null) {
                    suc = (String) session.getAttribute("suc");
                } else {
                    session.setAttribute("suc", suc);
                }
                out.println("<!DOCTYPE html>");
                out.println("<html>");
                out.println("<head>");

                String link_msg = " Load selected ";
                String url_baese = Constants.getServerName(c_con) + "Search/SearchResults3?";

                if (operation == null) {
                } else if (operation.equalsIgnoreCase("update")) {
                    link_msg = " update selected ";
                    url_baese = Constants.getServerName(c_con) + "Edit/Update2?";
                } else if (operation.equalsIgnoreCase("delete")) {
                    link_msg = " delete selected ";
                }
//   System.out.println("468 url_baese="+url_baese+"  operation="+operation);


                String split_link_prefix = url_baese + "&" + Constants.TABLE_TO_USE_FLAG + current_tbl_nm + "=";
//                System.out.println("480 value_return_to_page=" + value_return_to_page);
                String split_link_prefix2 = null;
                String batch_spliter = Constants.BATCH_INSERT_SPLITER;

                if (operation == null) {
                } else if (operation.equalsIgnoreCase(Constants.UPDATE_OP_FLAG)) {
                    if (return_to_link != null) {
                        split_link_prefix = return_to_link + "?" + current_tbl_nm + "=";
                    } else {
                        split_link_prefix = url_baese + "&" + Constants.TABLE_TO_USE_FLAG + current_tbl_nm + "=";
                    }

                    batch_spliter = ",";
//                        split_link_prefix = Constants.getServerName() + "Edit/Update_batch?reload=true&serachType=id&current_tbl_nm=" + current_tbl_nm + "&id_list=";
//                        split_link_prefix2 = Constants.getServerName() + "Edit/Update?reload=true&serachType=id&current_tbl_nm=" + current_tbl_nm + "&updatesearchvalue=";
                } else if (operation.equalsIgnoreCase(Constants.DELETE_OP_FLAG)) {
                    batch_spliter = ",";
                    split_link_prefix = Constants.getServerName(c_con) + "Edit/DeleteRecord?current_tbl_nm=" + Constants.get_correct_table_name(c_con, current_tbl_nm) + "&selected_id_List=";
                } else if (operation.equalsIgnoreCase(Constants.SELECT_OP_FLAG)) {
                    split_link_prefix2 = null;
                    split_link_prefix = value_return_to_page + "?value_returned_to_col=" + value_returned_to_col + "&" + value_return_to_table + "=";
                }
                if (session.getAttribute("GetFromDb_runner2") != null) {
                    get = (GetFromDb) session.getAttribute("GetFromDb_runner2");
                }
//     System.out.println("504 url_baese="+url_baese+"  operation="+operation+"\n\t"+split_link_prefix);
                String error_msg = null;
                if (get == null) {
                    try {
//                        if (getDatasource(use_meem_db, 7) != null) {
                        if (query != null) {
                            if (!filter_active) {
                                session.setAttribute("query2", query);
                            }
                            long freemem = Runtime.getRuntime().freeMemory();
                            if (Constants.MEM_WARN > freemem) {
                                System.out.println("Free Memory geeting low, users will experience slow response and data loss may occure during updates! " + (freemem / 1048576) + "Mb available");
                            }
//                                try {
//                                    if (ncon == null || ncon.isClosed()) {
//                                        try {
//                                            ncon = getDatasource(use_meem_db).getConnection();
//                                        } catch (SQLException ex) {
//                                        }
//                                    }
//                                } catch (SQLException ex) {
//                                }
//                                if (getConnection(use_meem_db) != null) {
                            if (filter_value != null && filter_table != null) {
                                String correct_current_tbl_nm = Constants.get_correct_table_name(c_con, current_tbl_nm);
                                String history_url = "filter_table=" + filter_table + "&filter_column=" + filter_column + "&filter_value=" + filter_value;
                                if (session.getAttribute(correct_current_tbl_nm + "_history_SR_l") != null) {
                                    history_SR_l = (ArrayList<String>) session.getAttribute(correct_current_tbl_nm + "_history_SR_l");
                                }
                                if (history_SR_l == null) {
                                    history_SR_l = new ArrayList<>();
                                }
                                if (!history_SR_l.contains(history_url)) {
                                    history_SR_l.add(history_url);
                                }
                                if (history_SR_l.size() > 10) {
                                    history_SR_l.remove(0);
                                }
                                session.setAttribute(correct_current_tbl_nm + "_history_SR_l", history_SR_l);
                                reload = true;
                            }

                            GetFromDb new_get = startDbSearch(server_mode, c_con, query, operation, batch_editable,
                                    make_qr, filter_table, filter_column, filter_value, filter_active, user_level, use_meem_db);
                            session.setAttribute("GetFromDb_runner2", new_get);
                            reload = true;
//                                } else {
//                                    //print error
//                                }
                        } else {
//                               out.println("<error>Null query</error>");
                        }
//                        }
//                    } catch (ServletException ex) {
//                        error_msg = "Error SR3a:" + ex.getMessage();
//                        System.out.println("Error SR3a:" + ex.getMessage());
                    } catch (Exception ex) {
                        error_msg = "Error SR3a:" + ex.getMessage();
                        System.out.println("Error SR3a:" + ex.getMessage());
                    }
                }
                if (session.getAttribute("GetFromDb_runner2") != null) {
                    get = (GetFromDb) session.getAttribute("GetFromDb_runner2");
                }

                out.println(getJavaScripts(c_con, batch_mode, split_link_prefix,
                        split_link_prefix2, link_msg, batch_spliter));
                out.println("<script src=\"" + Constants.getServerName(c_con) + "js/jquery-1.10.2.js\"></script>");
                out.append("<script>\n"
                        + "     function oprniInNew(url) {\n"
                        + "         window.open(url, \"_blank\", \"toolbar=no, scrollbars=yes, resizable=yes, top=500, left=500, width=400, height=800\");\n"
                        + "     }\n"
                        + "</script>");
                out.println("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=ISO-8859-1\">");
                if (get == null) {
                    refresh_active = false;
                    session.removeAttribute("isareset");
                } else if (!get.hasFinished()) {
                    session.setAttribute("isareset", "TRUE");
                    out.println("<META HTTP-EQUIV=\"refresh\" CONTENT=\"1\">");
                    refresh_active = true;
                } else {
                    session.removeAttribute("isareset");
                    refresh_active = false;
                }

                if (refresh_active) {
                    out.println("<META HTTP-EQUIV=\"refresh\" CONTENT=\"1\">");
                }

                out.println(" <link HREF=\"" + Constants.getServerName(c_con) + "resources/egenStyler.css\"  REL=\"stylesheet\" TYPE=\"text/css\" >");
                out.println("<title>GenVar Datamangement System</title>");


                if (get != null) {
                    if (!refresh_active && get.hasFinished()) {
                        session.setAttribute(Constants.FROMDB_RESULTS_SESSION_FLAG, getFromDB(get, null, session));
//                        try {
//                            close(ncon_mem, null, null);
//                            close(ncon_disk, null, null);
//                        } catch (Exception ex) {
//                        }
                        reload = true;
                    } else {
                        reload = false;
                    }
                }

                if (reload) {
                    pageReload(response, Constants.getServerName(c_con) + "Search/SearchResults3", 171);
                }
                out.println("</head>");
                out.println("<body>");
                if (error_msg != null) {
                    out.println("<error> Error:" + error_msg + "</error>");
                }
//                out.println("<div id=\"details_pos\">Details</div>");
                out.println("<table  class=\"table_text\" >\n"
                        + " <thead>\n"
                        + "     <tr>\n"
                        + "         <th>\n"
                        + "                <img  height=\"150\" width=\"90%\" src=\"../Banner.gif\"/>\n"
                        + "         </th>\n"
                        + "     </tr>\n"
                        + " </thead>\n"
                        + "</table>\n");
                out.println(session.getAttribute("Header"));
                out.println("  <div id=\"div_progress\">Please wait .. <img src=\"../progress.gif\" "
                        + "width=\"200\" height=\"10\" alt=\"progress\"  id=\"progressanim\"/></div>");

                ArrayList<Integer> id_created_list = null;

                if (new_result_map != null && !new_result_map.isEmpty()) {
                    Object data_ob = new_result_map.get("resultsDisplay_data_l");
                    Object column_nm_ob = new_result_map.get("resultsDisplay_column_nm_l");
                    Object current_tbl_nm_ob = new_result_map.get("current_tbl_nm");

                    Object split_link_ob = new_result_map.get("split_links_l");
                    Object static_url_ob = new_result_map.get("STATIC_URL");
                    Object qr_ob = new_result_map.get("QR");
                    Object graph_ob = new_result_map.get("GRAPH");
                    HashMap<Integer, String> id2expandDetails_map = (HashMap<Integer, String>) new_result_map.get("id2expandDetails_map");

                    if (static_url_ob != null) {
                        static_url = (String) static_url_ob;
                        session.setAttribute("static_url", static_url);
                    }
                    if (qr_ob != null) {
                        qr = (String) qr_ob;
                        session.setAttribute("qr", qr);
                    }
                    if (graph_ob != null) {
                        graph = (String) graph_ob;
                        session.setAttribute("graph", graph);
                    }

                    if ((data_ob != null) && column_nm_ob != null && current_tbl_nm_ob != null) {
                        current_tbl_nm = current_tbl_nm_ob.toString(); //not considered in javascript functions 
                        ArrayList<ArrayList<String>> resultsDisplay_data_l = (ArrayList<ArrayList<String>>) data_ob;
                        ArrayList<String> resultsDisplay_column_nm_l = (ArrayList<String>) column_nm_ob;
                        if (current_tbl_nm != null) {
                            out.println("<h1>Results from " + current_tbl_nm.split("\\.")[current_tbl_nm.split("\\.").length - 1] + "</h1>");
                        } else {
                            out.println("<h1>Results from: Error</h1>");
                        }
                        ArrayList<String> split_links_l = new ArrayList<>();
                        ArrayList<String> filter_split_links_l = new ArrayList<>();

                        if (filter_active) {
                            if (page <= 1) {
                                if (split_link_ob != null) {
                                    filter_split_links_l = (ArrayList<String>) split_link_ob;
                                }
                                session.setAttribute("filter_split_links_l", split_links_l);
                            } else {
                                if (session.getAttribute("filter_split_links_l") != null) {
                                    filter_split_links_l = (ArrayList<String>) session.getAttribute("filter_split_links_l");
                                }
                                if (session.getAttribute("qr") != null) {
                                    qr = (String) session.getAttribute("qr");
                                }
                                if (session.getAttribute("graph") != null) {
                                    graph = (String) session.getAttribute("graph");
                                }
                            }
                        } else {
                            if (page <= 1) {
                                if (split_link_ob != null) {
                                    split_links_l = (ArrayList<String>) split_link_ob;
                                    session.setAttribute("split_links_l", split_links_l);
                                }
                            } else {
                                if (session.getAttribute("split_links_l") != null) {
                                    split_links_l = (ArrayList<String>) session.getAttribute("split_links_l");
                                }
                                if (session.getAttribute("qr") != null) {
                                    qr = (String) session.getAttribute("qr");
                                }
                                if (session.getAttribute("graph") != null) {
                                    graph = (String) session.getAttribute("graph");
                                }
                            }
                        }
                        if (filter_split_links_l != null && !filter_split_links_l.isEmpty()) {
                            split_links_l.clear();
                            split_links_l.addAll(filter_split_links_l);
                        }
                        printControls(c_con, session, out, current_tbl_nm, resultsDisplay_column_nm_l, current_tbl_nm,
                                creation_allowed, operation, filter_active, last_query, query_used, last_search_type, suc, use_meem_db);
                        id_created_list = printResults(c_con, resultsDisplay_data_l, resultsDisplay_column_nm_l, split_links_l, id2expandDetails_map,
                                current_tbl_nm, out, page, operation, refresh_active, value_returned_to_col, session, use_meem_db,showSearch_link);
                        session.setAttribute("id_created_list", id_created_list);
                    } else {
                        //print error
                    }
                }

                out.println("<script type=\"text/javascript\">");

                if (!refresh_active) {
                    out.println("   setProgress(\"div_progress\", \"hidden\");");
                }

                if (session.getAttribute("static_url") != null) {
                    static_url = (String) session.getAttribute("static_url");
                }

                if (static_url != null) {
                    out.println("setURL(\"" + static_url + "\");");
                }
                if (qr != null) {
                    out.println("setQR(\"" + qr + "\");");
                }
                if (graph != null) {
                }
                out.println("setVisible('search_results','');");
                out.println("</script>\n");
                out.println("<footer>" + session.getAttribute("footer") + "</footer>");
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

    /*
     MethodID=SR7
     */
    private Object readResultsFromFile(String sessionID) {
        File test = new File(sessionID);
        if (test.isFile() && test.canRead()) {
            Object auth_ob = null;
            try {
                FileInputStream in = new FileInputStream(sessionID);
                ObjectInputStream obin = new ObjectInputStream(in);
                auth_ob = obin.readObject();
                obin.close();
            } catch (StreamCorruptedException ex) {
                System.out.println("Error 37: Security exception, the authentication file is corrupted or has been modified by third party");
            } catch (ClassNotFoundException ex) {
                System.out.println("Error 38: accessing authentication file");
            } catch (IOException ex) {
                System.out.println("Error 38: accessing authentication file");
            }

            try {
                if (auth_ob != null) {
                    return auth_ob;
                }
            } catch (Exception ex) {
                System.out.println("Error 38: accessing authentication file");
            }
        }
        return null;
    }

    /*
     MethodID=SR9
     * 
     */
    private String getJavaScripts(Connection c_con, boolean batch_selection_allowed, String base_url,
            String base_url2, String link_msg, String batch_spliter) {
        StringBuilder str = new StringBuilder();
        str.append("<script language =JavaScript>");
        str.append("\n"
                + "function setProgress(target, value){\n"
                + " if(document.getElementById(target)!=null){\n"
                + "     document.getElementById(target).style.visibility = value; \n"
                + " }\n"
                + "} ");
        str.append("\n"
                + "function setVisible(target, url){\n"
                + " if(document.getElementById(target)!=null){\n"
                + "     if(document.getElementById(target).style.visibility==\"hidden\"){\n"
                + "         document.getElementById(target).style.visibility = \"visible\"; \n"
                + "         $('#'+target).load(url);    \n"
                + "     }else{\n"
                + "        document.getElementById(target).style.visibility = \"hidden\"; \n"
                + "        document.getElementById(target).innerHTML = \"<br >\"; \n"
                + "     }\n"
                + " }\n"
                + "} ");
        str.append("\n");
//        out.println("<div id=\"search_results_msgs\">");
//        out.append("<a style=\"background-color: #0174DF; color: white;\n"
//                + "display: block; height: 100%; line-height: 20px; text-decoration:\n"
//                + "none; width: 80%; cursor:pointer;\" onmouseover=\"this.style.textDecoration = 'underline';\" onmouseout=\"this.style.textDecoration = 'none';\" "
//                + "onclick=\"setVisible('search_results','" + Constants.getServerName(c_con) + "Search/search?data_height=200 #data');\"  >"
//                + " Show search results </a>");

//                                        valp ="<a onclick=\"$('#details_pos').load('"+Constants.getServerName(c_con) + "Search/SearchResults3?"
//                                        + Constants.TABLE_TO_USE_FLAG + valp_a[0] + "=" + valp_a[1] + " #data_tbl')\" "
//                                        + "style=\"cursor:pointer;\" "
//                                        + "onmouseover=\"this.style.textDecoration = 'underline';\" "
//                                        + "onmouseout=\"this.style.textDecoration = 'none';\" >" + valp + "</a>";
//        str.append("function setVisibility(target, value){\n"
//                + "  if(document.getElementById(target)!=null){\n"
//                + "    document.getElementById(target).style.display = value;\n"
//                + "  }\n"
//                + " }\n");
        str.append("function setVisibleRow(target, rowid){\n"
                + "  if(document.getElementById(target).style.display==='none'){"
                + "      if(document.getElementById(rowid)!=null){\n"
                + "          document.getElementById(rowid).innerHTML = \"<boldb>-<boldb>\";\n"
                + "     }\n"
                + "     if(document.getElementById(target)!=null){\n"
                + "         document.getElementById(target).style.display = 'table-row';\n"
                + "     }\n"
                + " }else{"
                + "      if(document.getElementById(rowid)!=null){\n"
                + "          document.getElementById(rowid).innerHTML = \"<boldb>+<boldb>\";\n"
                + "     }\n"
                + "     if(document.getElementById(target)!=null){\n"
                + "         document.getElementById(target).style.display = 'none';\n"
                + "     }\n"
                + "}"
                + " }\n");

        str.append("function setVisibleRow_all(rowid){\n"
                + "     var rows = document.getElementsByName('" + Constants.HIDDEN_ROW_PREFIX + "[]');\n"
                + "     var hide=true;\n"
                + "     for (var i=0; i<rows.length; i++) {\n"
                + "         if(i==0){"
                + "             if(rows[i].style.display==='none'){"
                + "                hide=false; "
                + "                 if(document.getElementById(rowid)!=null){\n"
                + "                     document.getElementById(rowid).innerHTML = \"<boldbb>-<boldbb>\";\n"
                + "                 }\n"
                + "             }else{"
                + "                 if(document.getElementById(rowid)!=null){\n"
                + "                     document.getElementById(rowid).innerHTML = \"<boldbb>+<boldbb>\";\n"
                + "                 }\n"
                + "             }\n"
                + "         }"
                + "             if(hide){"
                + "                 rows[i].style.display = 'none';\n"
                + "             }else{"
                + "               rows[i].style.display = 'table-row';\n"
                + "             }"
                + "     }\n"
                + "    var plusminus=document.getElementsByName('" + Constants.CCONTORLER_PLUS_MINUS_PREFIX + "[]');"
                + "     for (var i=0; i<plusminus.length; i++) {\n"
                + "         if(hide){"
                + "             plusminus[i].innerHTML = \"<boldb>+<boldb>\";\n"
                + "         }else{"
                + "             plusminus[i].innerHTML = \"<boldb>-<boldb>\";\n"
                + "         }"
                + "   }\n"
                + "\n"
                + "}\n");
//         //

        str.append("\n");
        str.append("\n"
                + "function setAll(clear){\n"
                //                + " document.getElementById(\"selected_id_List\").value=\"-1\"; \n"
                + " var selected_id_a=\"\";\n"
                + " var selected_count=0;\n"
                + " var checkboxes = document.getElementsByName('" + GetFromDb.USER_SELECT_CHKBX_PREFIX + "[]');\n"
                + " for (var i=0; i<checkboxes.length; i++) {\n"
                + "     if(clear==null || typeof clear === 'undefined'){\n"
                + "       checkboxes[i].checked=false;\n"
                + "     }else{\n"
                + "         if(selected_id_a.length==0){\n"
                + "             selected_id_a=checkboxes[i].id;  \n"
                + "          }else{\n"
                + "             selected_id_a=selected_id_a+\"" + batch_spliter + "\"+checkboxes[i].id;  \n"
                + "          }\n"
                + "       checkboxes[i].checked=true;\n"
                + "       selected_count=selected_count+1;\n"
                + "     }\n"
                + " }\n"
                //                + " document.getElementById(\"selected_id_List\").value=selected_id_a; \n"
                + " document.getElementById(\"div_selected\").innerHTML=selected_count+\" selected\"; \n");
        if (base_url2 == null) {
            str.append("if(selected_count>0){\n"
                    + "     document.getElementById(\"div_useSelected\").innerHTML=\"<a href=\\\"" + base_url + "\"+selected_id_a+\"\\\">>> " + link_msg + " >>></a>\" \n"
                    + " }else{\n"
                    + "     document.getElementById(\"div_useSelected\").innerHTML=\"Select row using check boxes to proceed\"; \n"
                    + " }\n");
        } else {
            str.append("if(selected_count==1){\n"
                    + "     document.getElementById(\"div_useSelected\").innerHTML=\"<a href=\\\"" + base_url + "\"+selected_id_a+\"\\\">>> " + link_msg + " >>></a>\" \n"
                    + " }else if (selected_count>0){\n"
                    + "     document.getElementById(\"div_useSelected\").innerHTML=\"<a href=\\\"" + base_url2 + "\"+selected_id_a+\"\\\">>> " + link_msg + " >>></a>\" \n"
                    + " }else{\n"
                    + "     document.getElementById(\"div_useSelected\").innerHTML=\"Select row using check boxes to proceed\"; \n"
                    + " }\n");

        }
        str.append("}\n");

        if (!batch_selection_allowed) {
            str.append("\n"
                    + "function setSingle(chkbx){\n"
                    //                    + " document.getElementById(\"selected_id_List\").value=\"-1\"; \n"
                    + " var selected_id_a=\"\";\n"
                    + " var selected_count=0;\n"
                    + " var checkboxes = document.getElementsByName('" + GetFromDb.USER_SELECT_CHKBX_PREFIX + "[]');\n"
                    + " var checkboxesChecked = [];\n"
                    + " var msg=\"__\";\n"
                    + "     if(chkbx==null || typeof chkbx === 'undefined'){\n"
                    + "     }else{\n"
                    + "         for (var i=0; i<checkboxes.length; i++) {\n"
                    + "             if(chkbx.id!=checkboxes[i].id){\n"
                    + "                 checkboxes[i].checked=false;\n"
                    + "                 if(chkbx.checked){\n"
                    + "                     selected_count=1;\n"
                    + "                 }\n"
                    + "             }else{\n"
                    + "                 selected_id_a=checkboxes[i].id; \n "
                    + "                 selected_count=1;\n"
                    + "             }\n"
                    + "         }\n"
                    + "    }\n"
                    //                    + " document.getElementById(\"selected_id_List\").value=selected_id_a; \n"
                    + " document.getElementById(\"div_selected\").innerHTML=selected_count+\" selected\"; \n"
                    + " if(selected_count>0){\n"
                    + "     document.getElementById(\"div_useSelected\").innerHTML=\"<h9><a href=\\\"" + base_url + "\"+selected_id_a+\"\\\">Use selected values</a></h9>\" \n"
                    + " }else{\n"
                    + "     document.getElementById(\"div_useSelected\").innerHTML=\"<h9>Select using check-box</h9>\"; \n"
                    + " }\n"
                    + "}\n");

        } else {
            str.append("\n"
                    + "function setSingle(chkbx){\n"
                    //                    + " document.getElementById(\"selected_id_List\").value=\"-1\"; \n"
                    + " var selected_id_a=\"\";\n"
                    + " var selected_count=0;\n"
                    + " var checkboxes = document.getElementsByName('" + GetFromDb.USER_SELECT_CHKBX_PREFIX + "[]');\n"
                    + " for (var i=0; i<checkboxes.length; i++) {\n"
                    + "     if(checkboxes[i].checked){"
                    + "         if(selected_id_a.length==0){\n"
                    + "             selected_id_a=checkboxes[i].id;  \n"
                    + "          }else{\n"
                    + "             selected_id_a=selected_id_a+\"" + batch_spliter + "\"+checkboxes[i].id;  \n"
                    + "          }\n"
                    + "       selected_count=selected_count+1;  \n"
                    + "     }\n"
                    + " }\n"
                    //                    + " document.getElementById(\"selected_id_List\").value=selected_id_a; \n"
                    + " document.getElementById(\"div_selected\").innerHTML=selected_count+\" selected\"; \n"
                    + " if(selected_count>0){\n"
                    + "     document.getElementById(\"div_useSelected\").innerHTML=\"<h9><a href=\\\"" + base_url + "\"+selected_id_a+\"\\\">Use selected values</a></h9>\" \n"
                    + " }else{\n"
                    + "     document.getElementById(\"div_useSelected\").innerHTML=\"<h9>Select using check-box</h9>\"; \n"
                    + " }\n"
                    + "}\n");
        }
        str.append("function setQR(image_url){"
                + "document.getElementById(\"div_bl2\").innerHTML=image_url;"
                + ""
                + "}");
        str.append("function setURL(result_url){"
                + "document.getElementById(\"div_bl3\").innerHTML=result_url;"
                + ""
                + "}");
        str.append("function setGraph(result_url){"
                + "document.getElementById(\"div_bl4\").innerHTML=result_url;"
                + ""
                + "}");
        str.append("</script>");
        return str.toString();
    }

    private void printControls(Connection c_con, HttpSession session, PrintWriter out,
            String current_tbl_nm, ArrayList<String> resultsDisplay_column_nm_l,
            String previous_tbl_nm, boolean creation_allowed, String operation, boolean filter_active, String last_query,
            String query_used, String last_search_type, String suc, boolean use_meem_db) {
        if (resultsDisplay_column_nm_l != null) {
            ArrayList<String> resultsDisplay_column_nm_drop_down_l = new ArrayList<>();
            resultsDisplay_column_nm_drop_down_l.addAll(resultsDisplay_column_nm_l);
            String comboList = "";
            String expanded_search = "";
            try {
//                HashMap<String, String[]> frgn_key_map = Constants.get_key_contraints(current_tbl_nm, getDatasource(use_meem_db));
                comboList = " <select name=\"filter_column\">";

                if (resultsDisplay_column_nm_drop_down_l.remove("name")) {
                    resultsDisplay_column_nm_drop_down_l.add(0, "name");
                }
                for (int i = 0; i < resultsDisplay_column_nm_drop_down_l.size(); i++) {
                    String tmpType = resultsDisplay_column_nm_drop_down_l.get(i);
                    comboList = comboList + "<option  value=\"" + tmpType + "\">" + tmpType + "</option>";
//                if ((frgn_key_map == null || !frgn_key_map.containsKey(tmpType))) {
//                } else if (frgn_key_map.containsKey(tmpType)) {
//                    String foreign_tbl = frgn_key_map.get(tmpType)[1];
//                    if (foreign_tbl != null) {
////                        if (expanded_search.isEmpty()) {
////                            expanded_search = "Search using  ";
////                        }
////                        String display_table_nm = foreign_tbl;
////                        if (foreign_tbl.equals(current_tbl_nm)) {
////                            display_table_nm = tmpType;
////                        }
////                        String tble_lable = display_table_nm.split("\\.")[display_table_nm.split("\\.").length - 1];
////                        try {
////                            expanded_search = expanded_search + " | <a  href=\"" + Constants.getServerName() + "Search/SearchResults?current_tbl_nm=" + foreign_tbl + "&value_returned_to_col=" + tmpType + "&reload=true&operation=" + Constants.SELECT_OP_FLAG + "&previous_tbl_nm=" + current_tbl_nm + "&filterserachType=" + tmpType + "&searchvalue=" + URLEncoder.encode("%", "ISO-8859-1") + "\" >" + tble_lable + "</a>";
////                        } catch (Exception e) {
////                        }
//                    }
//                }
                }
                if (!comboList.isEmpty()) {
                    comboList = comboList + "</select>";
                    out.println("<table class=\"table2\" border=\"1\">");
                    out.println("<tr>");
                    out.println("<td>");
                    if (filter_active) {
                        String last_query_url = makeQUERY_URL(c_con, last_query, current_tbl_nm, Constants.getDocRoot(c_con), use_meem_db);
                        out.print("<a href=\"" + Constants.getServerName(c_con) + "Search/SearchResults3?" + Constants.QUERY_TO_USE_FLAG + "=" + last_query_url + "&filter_active=false\">Reset Filter</a>");
                    } else {
                        out.print("<form accept-charset=\"ISO-8859-1\"  method=\"post\" action=\"" + Constants.getServerName(c_con) + "Search/SearchResults3?operation=" + operation + "&page=0\">");
                        out.println("Filter: <input type=\"text\" name=\"filter_value\"> " + comboList + " ");
                        out.println("<input type=\"hidden\"  name=\"filter_table\" value=\"" + current_tbl_nm + "\"> ");
                        out.print("<input type=\"submit\" value=\"Apply filter\"/>");
                    }

                    out.print("</form>");

                    out.println("</td>");
                    String correct_current_tbl_nm = Constants.get_correct_table_name(c_con, current_tbl_nm);
                    if (!filter_active && session.getAttribute(correct_current_tbl_nm + "_history_SR_l") != null) {
                        out.print("<td>Filter_History: ");
                        ArrayList<String> history_SR_l = (ArrayList<String>) session.getAttribute(correct_current_tbl_nm + "_history_SR_l");
                        for (int i = 0; i < history_SR_l.size(); i++) {
                            if (i == 0) {
                                out.print("<a href=\"" + Constants.getServerName(c_con) + "Search/SearchResults3?" + history_SR_l.get(i) + "\">" + i + "</a>");
                            } else {
                                out.print(",<a href=\"" + Constants.getServerName(c_con) + "Search/SearchResults3?" + history_SR_l.get(i) + "\">" + i + "</a>");
                            }
                        }
                        out.print("</td>");
                    }

                    if (!expanded_search.isEmpty()) {
                        out.println("<td>");
                        out.println(expanded_search);
                        out.println("</td>");
                    }

                    if (current_tbl_nm != null && creation_allowed) {
                        out.println("<td>");
                        out.print("<form accept-charset=\"ISO-8859-1\"  method=\"post\" action=\"" + Constants.getServerName(c_con) + "Upload/CreateNew\">");
                        out.println("<input type=\"hidden\" value=\"" + current_tbl_nm + "\" name=\"current_tbl_nm\" />");
                        out.println("<input type=\"hidden\" value=\"true\" name=\"reload\" />");
                        out.println("<input type=\"hidden\" value=\"" + Constants.getServerName(c_con) + "Search/SearchResults3\" name=\"value_return_to_page\" />");
                        out.println("<input type=\"submit\" value=\"Create new " + current_tbl_nm + "\" />");
                        out.print("</form>");
                        out.println("</td>");
                    }
                    out.println("<td style=\"text-align:center\">");
//                    out.println("<a href=\"" + Constants.getServerName(c_con) + "Search/search?last_search_type="
//                            + last_search_type + "&query_used=" + query_used.split("\\|")[0] + "&suc=" + suc + "\">Back to search page</a> <h8>(Query=" + query_used + ")</h8>");
//                            out.println("<p onclick=\"setVisible('search_results');\" \">"
//                            + " Show search results </p>"); //text-align: center
                    out.append("<a style=\"background-color: #088A85; color: white;\n"
                            + "display: block; height: 100%; line-height: 20px; text-decoration:\n"
                            + "none; width: 80%; ;\"  href=\"" + Constants.getServerName(c_con) + "Search/search?last_search_type="
                            + last_search_type + "&query_used=" + query_used.split("\\|")[0] + "&suc=" + suc + "\"  >"
                            + " Search results : Query=" + query_used + ") </a>");
//                    out.append("<a style=\"background-color: #0174DF; color: white;\n"
//                            + "display: block; height: 100%; line-height: 20px; text-decoration:\n"
//                            + "none; width: 80%; cursor:pointer;\" onmouseover=\"this.style.textDecoration = 'underline';\" onmouseout=\"this.style.textDecoration = 'none';\" "
//                            + "onclick=\"setVisible('search_results','" + Constants.getServerName(c_con) + "Search/search?data_height=200 #data');\"  >"
//                            + " Show/hide search results </a>");

                    //                                valp ="<a onclick=\"$('#details_pos').load('"+Constants.getServerName(c_con) + "Search/SearchResults3?"
//                                        + Constants.TABLE_TO_USE_FLAG + valp_a[0] + "=" + valp_a[1] + " #data_tbl')\" "
//                                        + "style=\"cursor:pointer;\" "
//                                        + "onmouseover=\"this.style.textDecoration = 'underline';\" "
//                                        + "onmouseout=\"this.style.textDecoration = 'none';\" >" + valp + "</a>";
                    out.println("</td>");
                    out.println("</tr>");
                    out.println(" </table>");
                    out.println("<br /> ");
                }
//            } catch (ServletException ex) {
//                out.println("<p><error>Error 11: " + ex.getMessage() + "<error></p>");
            } catch (Exception ex) {
                out.println("<p><error>Error 11: " + ex.getMessage() + "<error></p>");
            }
        } else {
            out.println("<p><error>Error 10: Error retrieving data<error></p>");
        }

    }

    private ArrayList<Integer> printResults(Connection c_con, ArrayList<ArrayList<String>> resultsDisplay_data_l, ArrayList<String> resultsDisplay_column_nm_l,
            ArrayList<String> split_links_l, HashMap<Integer, String> id2expandDetails_map, String current_tbl_nm, PrintWriter out,
            int page, String operation, boolean refresh_active, String value_returned_to_col, HttpSession ses, boolean use_meem_db, String showSearch_link) {
        ArrayList<Integer> id_created_list = new ArrayList<>(10);

//        boolean restricted_display = false;
        if (page < 1) {
            page = 1;
        }
        try {
//        if ((user_level > 1) && current_tbl_nm.equals(Constants.get_correct_table_name(dataSource, "files2path"))) {
//            restricted_display = true;
//        }

            if (resultsDisplay_column_nm_l != null) {
                if (resultsDisplay_data_l != null) {
                    if (!resultsDisplay_column_nm_l.isEmpty() && !resultsDisplay_data_l.isEmpty()) {
                        if (split_links_l != null && !split_links_l.isEmpty() && !refresh_active) {
                            out.println("<table class=\"table2\" border=\"1\" ><thead>");
                            out.print("<tr>");
                            out.print("<td>");
                            out.print("Current_page=<h8>" + page + "</h8> ");
                            out.print("</td >");
                            out.print("<td colspan=\"2\">");
                            String more_results = " More results:";
                            if (!split_links_l.isEmpty()) {
                                String[] c_link_a = split_links_l.get(0).split("=", 2);
                                if (c_link_a.length == 2 && c_link_a[0].matches("[0-9]+")) {
                                    more_results = c_link_a[1] + c_link_a[0] + "</a>";
                                }
                                for (int i = 1; i < split_links_l.size(); i++) {
                                    c_link_a = split_links_l.get(i).split("=", 2);
                                    if (c_link_a.length == 2 && c_link_a[0].matches("[0-9]+")) {
                                        int c = new Integer(c_link_a[0]);
                                        boolean print_orig = false;
                                        if (c < 10 || c == page) {
                                            print_orig = true;
                                        } else if (Math.abs(c - page) < 5) {
                                            print_orig = true;
                                        } else if ((split_links_l.size() - c) < 5) {
                                            print_orig = true;
                                        } else {
                                        }
                                        if (print_orig) {
                                            more_results = more_results + "," + c_link_a[1] + c_link_a[0] + "</a>";
                                        } else {
                                            if (Math.abs(c - page) < 20) {
                                                more_results = more_results + c_link_a[1] + ".</a>";
                                            }
                                        }
                                    } else {
                                        more_results = more_results + c_link_a[0] + "=" + c_link_a[1];
                                    }

                                }
                            }


                            if (split_links_l.size() > 1) {
                                out.print(more_results);
                            }
                            out.print("</td>");
//                        out.print("</tr >");

                            out.print("<td>");
                            out.println("<div_bl3 id=\"div_bl3\">NA</div_bl3>");
                            out.print("</td><td>");
                            out.println("<div_bl4 id=\"div_bl4\"></div_bl4>");
                            out.println("<div_bl2 id=\"div_bl2\"></div_bl2>");
                            out.print("</td></tr>");

                            if (operation == null || operation.equals(Constants.SERCH_OP_FLAG)) {
                            } else {
                                out.print("<tr>");
                                out.print("<th>");
                                out.print(" <slink><div id=\"clear_all\"><a style=\"cursor:pointer;\" onclick=\" setAll(null);\"  onmouseover=\"this.style.textDecoration = 'underline';\" onmouseout=\"this.style.textDecoration = 'none';\"> Clear_all</a>");
                                out.print("<a style=\"cursor:pointer;\" onclick=\" setAll('ALL');\"  onmouseover=\"this.style.textDecoration = 'underline';\" onmouseout=\"this.style.textDecoration = 'none';\">  Select_all</a></div></slink>");
                                out.print("</th >");
                                out.print("</tr>");
                                out.print("<tr>");
                                out.print("<th>");
                                out.print("<div id=\"div_selected\">0 selected</div>");
                                out.print("</th>");
                                out.print("</tr>");
                            }


//                        out.print("<tr><td>");
//                        out.print("<input id=\"selected_id_List\" name=\"selected_id_List\" value=\"\"  size=\"50\" readonly=\"readonly\" />");
//                        out.print("</td>");
                            out.println("   ");

                            out.print("</table>");
                        } else {
                            out.println("<table class=\"table2\" border=\"1\" >");
                            out.print("<thead><tr><th>");
                            out.print("Loading..... ");
                            out.print("</th><tr></thead>");
                            out.print("</table>");
                        }
                        out.println("<table class=\"table2\" border=\"1\" ><thead>");
                        out.print("<tr>");
                        out.print("<td>");
                        out.println("<div id=\"search_results_msgs\" style=\"text-align:center\" >");
                        out.append(showSearch_link);
                        out.println("</div>");
                        out.print("</td>");
                        out.print("</tr>");
                        out.print("</table>");
                        out.println("<div id=\"search_results\" style=\"width:100%\">");
                        out.println("</div>");
                        out.println("<p></p>");
                        out.println("<table id=\"data_tbl\" align=\"left\" border=\"1\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\"><thead>");

                        out.print("<tr>");
                        out.print("<td colspan=\"" + resultsDisplay_column_nm_l.size() + 1 + "\">");
                        if (operation == null) {
                            out.print("<div id=\"div_useSelected\"></div>");
                        } else if (operation.equalsIgnoreCase(Constants.UPDATE_OP_FLAG)) {
                            out.print("<div id=\"div_useSelected\"><warning>Select row to update</warning></div>");
                        } else if (operation.equalsIgnoreCase(Constants.DELETE_OP_FLAG)) {
                            out.print("<div id=\"div_useSelected\"><warning>Select row to delete</warning></div>");
                        } else if (operation.equalsIgnoreCase(Constants.SELECT_OP_FLAG)) {
                            out.print("<div id=\"div_useSelected\"><h9>Select using check-boxes</h9></div>");
                        } else {
                            out.print("<div id=\"div_useSelected\"></div>");
                        }
                        out.print("</td>");
                        out.print("</tr>");
                        out.print("<tr>");
                        int id_column_pos = -1;
//                        int name_column_pos = -1;
//                        int ownergroup_column_pos = -1;
//                        int location_column_pos = -1;
//                        int last_modified_date_pos = -1;
                        out.println("<th id=\"headerrow_\" title=\"Click to expand all\" onclick=\"setVisibleRow_all('headerrow_');\"><boldbb>+</boldbb></th> ");
                        for (int i = 0; i < resultsDisplay_column_nm_l.size(); i++) {
                            if (resultsDisplay_column_nm_l.get(i).equalsIgnoreCase(ID_FLAG)) {
                                id_column_pos = i;
                            }
//                        else if (restricted_display) {
//                            if (resultsDisplay_column_nm_l.get(i).equalsIgnoreCase("FILEPATH")) {
//                                name_column_pos = i;
//                            } else if (resultsDisplay_column_nm_l.get(i).equalsIgnoreCase("OWNERGROUP")) {
//                                ownergroup_column_pos = i;
//                            } else if (resultsDisplay_column_nm_l.get(i).equalsIgnoreCase("LASTMODIFIED")) {
//                                last_modified_date_pos = i;
//                            }
//                        }
                            out.println("<th>" + resultsDisplay_column_nm_l.get(i) + "</th> ");
                        }

                        current_tbl_nm = Constants.get_correct_table_name(c_con, current_tbl_nm);

                        out.print("</tr><tbody>");

                        for (int i = 0; i < resultsDisplay_data_l.size(); i++) {
                            ArrayList<String> tmp_l = resultsDisplay_data_l.get(i);
                            String dataLisne = "";
                            int row_id = -1;
                            boolean datarow = true;
                            int expnad_size = resultsDisplay_column_nm_l.size();
                            if (expnad_size % 2 != 0) {
                                expnad_size = expnad_size + 1;
                            }

                            if (!tmp_l.isEmpty() && tmp_l.get(0) != null) {
//                                System.out.println("1253 "+ tmp_l);
                                if (tmp_l.get(0).startsWith(Constants.DETAILS_FLAG)) {
                                    String print_details = tmp_l.get(0).replace(Constants.DETAILS_FLAG, "");
                                    String[] tmp_split = print_details.split(IDFORDIV_SPLIT_FLAG);
                                    if (tmp_split.length == 1) {
                                        if (tmp_split[0].matches("[0-9]+")) {
                                            row_id = new Integer(tmp_split[0]);
                                            id_created_list.add(row_id);
                                        }

                                        String[] expansion_a = new String[1];
                                        expansion_a[0] = "No details available";

                                        if (id2expandDetails_map.containsKey(row_id)) {
                                            expansion_a = id2expandDetails_map.get(row_id).split(EXPANSION_SPLIT_TOKEN);
                                        }
                                        int c_colspan = (expnad_size / expansion_a.length);
                                        for (int j = 0; j < expansion_a.length; j++) {
                                            if (j > 3) {
                                                dataLisne = dataLisne + "<td colspan=\"" + c_colspan + "\">" + expansion_a[j] + "</td>";

                                            } else {
                                                dataLisne = dataLisne + "<td colspan=\"" + c_colspan + "\">" + expansion_a[j] + "</td>";
                                            }
                                        }

                                    } else {
                                        dataLisne = "<td  colspan=\"" + resultsDisplay_column_nm_l.size() + "\">" + print_details + "</td>";
                                    }
                                    datarow = false;
                                } else {
                                    for (int j = 0; j < tmp_l.size(); j++) {
                                        if (j == id_column_pos) {
                                            String[] tmp_split = tmp_l.get(j).split("\\s");
                                            if (tmp_split[0].matches("[0-9]+")) {
                                                row_id = new Integer(tmp_split[0]);
                                            }
                                        }//
                                        if (j != id_column_pos) {
                                            String dsply_tsxt = "NA";
//                                        if (restricted_display) {
//                                            if (j == name_column_pos) {
//                                                dsply_tsxt = getDsiplayText(user_level, tmp_l.get(j));
//                                            } else if (j == ownergroup_column_pos) {
//                                                dsply_tsxt = getDsiplayText(user_level, tmp_l.get(j));
//                                            } else if (j == location_column_pos) {
//                                                dsply_tsxt = getDsiplayText(user_level, tmp_l.get(j));
//                                            } else if (user_level < 3 && j == last_modified_date_pos) {
//                                                dsply_tsxt = getDsiplayText(user_level, tmp_l.get(j));
//                                            } else {
//                                                dsply_tsxt = tmp_l.get(j);
//                                            }
//                                        } else {
                                            dsply_tsxt = tmp_l.get(j);
//                                        }
                                            dataLisne = dataLisne + "<td  ondblclick=\"setVisibleRow('rowid" + row_id + "','row_" + row_id + "');\">" + dsply_tsxt + "</td>";
                                        } else {
                                            dataLisne = dataLisne + "<td ondblclick=\"setVisibleRow('rowid" + row_id + "','row_" + row_id + "');\">" + tmp_l.get(j) + "</td>";

                                        }
                                    }
                                    dataLisne = "<td name=\"" + Constants.CCONTORLER_PLUS_MINUS_PREFIX + "[]\" id=\"row_" + row_id
                                            + "\" title=\"Click to display expanded results\" onclick=\"setVisibleRow('rowid" + row_id + "','row_" + row_id + "');\" ><boldb>+</boldb></td>" + dataLisne;
                                }
                            }
                            out.println("<tr>");
                            if (datarow) {
                                out.println("<tr id=\"datarowid" + row_id + "\" onmouseover=\"this.style.backgroundColor='#e5f4fa';\" onmouseout=\"this.style.backgroundColor='#f8f8ff';\" >");
                            } else {
                                out.println("<tr name=\"" + Constants.HIDDEN_ROW_PREFIX + "[]\"  style=\"display:none\" id=\"rowid" + row_id + "\" onmouseover=\"this.style.backgroundColor='#fff4ef';\" onmouseout=\"this.style.backgroundColor='#f8f8ff';\"  >");
                            }
                            out.println(dataLisne);
                            out.println("</tr>");
                        }
//                    out.print("<input type=\"hidden\" name=\"current_tbl_nm\" value=\"" + current_tbl_nm + "\" />");
                        out.print("</tbody></table>");
                    } else {
                        out.println("<table align=\"left\" border=\"1\" cellpadding=\"0\" cellspacing=\"1\" width=\"100%\"><thead><tr>");
                        if (ses.getAttribute("LAST_LOADED_ID") != null) {
                            out.println("<td>No matching results.  <a href=\"" + Constants.getServerName(c_con) + "Search/SearchResults3?" + ses.getAttribute("LAST_LOADED_ID") + "&reset=true&operation=search\"> Go back to last page</a></td>");
                        } else {
                            out.println("<td>No matching results. Click <a href=\"" + Constants.getServerName(c_con) + "Search/search\">here</a> to continue</td>");
                        }
                        out.print("</tr></thead></table>");

                    }

                } else {
                    out.println("<p><error>Error 15: Error retrieving data<error></p>");
                }

            } else {
                out.println("<p><error>Error 12: Error retrieving data<error></p>");
            }
//        } catch (ServletException ex) {
//            out.println("<p><error>Error 20: " + ex.getMessage() + "<error></p>");
        } catch (Exception ex) {
            out.println("<p><error>Error 20: " + ex.getMessage() + "<error></p>");
        }

        if (!id_created_list.isEmpty()) {
            ses.setAttribute("LAST_LOADED_ID", Constants.TABLE_TO_USE_FLAG + current_tbl_nm + "=" + id_created_list.toString().replaceAll("\\s", "").replaceAll("\\[", "").replaceAll("\\]", ""));
        }
        return id_created_list;
    }

//    private String getCurrentResults(HashMap<String, ArrayList<String>> resutl_map) {
//        StringBuilder str = new StringBuilder();
//        if (resutl_map != null) {
//            ArrayList<String> keys_l = new ArrayList<>(resutl_map.keySet());
//            for (int i = (keys_l.size() - 1); i >= 0; i--) {
//                str.append("<tr><td>");
//                str.append("Query=" + keys_l.get(i) + " | ");
//                ArrayList<String> tmp_l = resutl_map.get(keys_l.get(i));
//                for (int j = 0; j < tmp_l.size(); j++) {
//                    String[] t_split_a = tmp_l.get(j).split("\\|\\|");
//                    String link = "Link not available";
//                    if (t_split_a.length >= 3) {
//                        if (t_split_a[1].equals("0")) {
//                            link = " (0 matches)";
//                        } else {
//                            if (!t_split_a[2].equalsIgnoreCase("NA") && !t_split_a[2].equalsIgnoreCase("SPLIT")) {
//                                link = " <h8>(" + t_split_a[1] + " matches)</h8> " + "Results_link=<a target=\"_blank\" href=\"" + Constants.getServerName() + "Search/SearchResults3?" + Constants.TABLE_TO_USE_FLAG + t_split_a[0] + "=" + t_split_a[2] + "\">" + t_split_a[2] + "</a>";
//                            }
//                        }
//                    } else {
//                        System.out.println("208 " + tmp_l.get(j));
//                    }
//                    str.append(link);
//                }
//                str.append("</td></tr>");
//            }
//        }
//        return str.toString();
//    }
    private GetFromDb startDbSearch(int server_mode, Connection ncon, String query, String operation, boolean batch_editable, boolean make_qr,
            String filter_table, String filter_column, String filter_value, boolean filter_active, int user_level, boolean from_memory) {
        String filter_q = null;
        filter_table = Constants.get_correct_table_name(ncon, filter_table);

        if (filter_active && filter_value != null) {
            if (Constants.shoulditbequated(ncon, filter_table, filter_column)) {
                filter_q = filter_table + "." + filter_column + " like '%" + filter_value.replaceAll("'", "") + "%'";
            } else {
                filter_q = filter_table + "." + filter_column + "=" + filter_value.replaceAll("'", "");
            }
            if (query.toUpperCase().contains(" WHERE ")) {
                query = query + " and " + filter_q;
            } else {
                query = query + " where " + filter_q;
            }
        }

        String link_prefix = Constants.getServerName(ncon) + "Search/SearchResults3?" + Constants.TABLE_TO_USE_FLAG;
        GetFromDb get = null;
        try {
            get = new GetFromDb(server_mode, getDatasource_operational(from_memory, 2),
                    query, operation, batch_editable, make_qr,
                    queryExpander_map, user_level, link_prefix);
            (new Thread(get)).start();
        } catch (ServletException ex) {
            System.out.println("Error SR31: " + ex.getMessage());
        }
        return get;

    }

    private LinkedHashMap<String, Object> getFromDB(GetFromDb get, LinkedHashMap<String, Object> new_result_map, HttpSession ses) {
        if (get != null) {
            ses.removeAttribute("GetFromDb_runner2");
            new_result_map = get.getFromDbsMap();
        }

        return new_result_map;

    }

    public String makeSTATIC_URL(Connection c_con, ArrayList<Integer> id_l, String current_tbl_nm, String doc_root, boolean use_meem_db) {
        String encripted_nm = Constants.makeSTATIC_URL(id_l, current_tbl_nm);
        File file = new File(doc_root + encripted_nm);
//        try {
        if (file.isFile()) {
        } else {
            writeResultsToFile(current_tbl_nm + "||" + id_l.toString().replaceAll("[^0-9,]", ""),
                    Constants.getDocRoot(c_con) + encripted_nm);
        }
//        } catch (SQLException ex) {
//        } catch (ServletException ex) {
//        }
        return encripted_nm;
    }

    public String makeQUERY_URL(Connection c_con, String query, String current_tbl_nm, String doc_root, boolean use_meem_db) {
        String encripted_nm = Constants.makeQUERY_URL(query, current_tbl_nm);
        File file = new File(doc_root + encripted_nm);
//        try {
        if (file.isFile()) {
        } else {
            writeResultsToFile(current_tbl_nm + "||" + query, Constants.getDocRoot(c_con) + encripted_nm);
        }
//        } catch (SQLException ex) {
//        } catch (ServletException ex) {
//        }
        return encripted_nm;
    }

    public String makeQR(Connection c_con, String texttoencode, String doc_root, boolean use_meem_db) {
        String url = null;
//        try {
        File file = new File(doc_root + Constants.encript(texttoencode) + ".png");
//       File  file = new File(file.getAbsoluteFile().getParentFile().getParent() + File.separatorChar + "docroot" + File.separatorChar + session_id);
        if (file.isFile()) {
            url = Constants.getSecureServerRoot(c_con) + file.getName();
        } else {
            Charset charset = Charset.forName("UTF-8");
            CharsetEncoder encoder = charset.newEncoder();
            byte[] b = null;
            try {
                ByteBuffer bbuf = encoder.encode(CharBuffer.wrap(texttoencode));
                b = bbuf.array();
            } catch (CharacterCodingException e) {
                System.out.println(e.getMessage());
            }

            String data;
            try {
                data = new String(b, "UTF-8");
                BitMatrix matrix = null;
                int h = 500;
                int w = 500;
                com.google.zxing.Writer writer = new MultiFormatWriter();
                try {
                    HashMap<EncodeHintType, String> hints = new HashMap<>(2);
                    hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
                    matrix = writer.encode(data, com.google.zxing.BarcodeFormat.QR_CODE, w, h, hints);
                } catch (com.google.zxing.WriterException e) {
                    System.out.println(e.getMessage());
                }

                try {
                    if (matrix != null) {
                        MatrixToImageWriter.writeToFile(matrix, "PNG", file);
                        url = Constants.getSecureServerRoot(c_con) + file.getName();
                    }

                } catch (IOException ex) {
                    System.out.println("Error SR123a: " + ex.getMessage());
                } catch (java.lang.UnsatisfiedLinkError ex) {
                    System.out.println("Error SR123g: " + ex.getMessage());
                } catch (Exception ex) {
                    System.out.println("Error SR123d: " + ex.getMessage());
                }
            } catch (UnsupportedEncodingException e) {
                System.out.println("Error SR123b: " + e.getMessage());
            } catch (java.lang.NoClassDefFoundError ex) {
                System.out.println("Error SR123c: " + ex.getMessage());
            } catch (Exception ex) {
                System.out.println("Error SR123f: " + ex.getMessage());
            }
        }

//        } catch (SQLException ex) {
//        } catch (ServletException ex) {
//        }
        return url;
    }

    private void writeResultsToFile(Object indata, String sessionID) {
        if (indata != null && sessionID != null) {
            try {
                FileOutputStream fos = new FileOutputStream(sessionID);
                ObjectOutputStream os;
                try {
                    os = new ObjectOutputStream(fos);
                    os.writeObject(indata);
                    os.close();
                } catch (IOException ex) {
                    System.out.println("Error: " + ex.getMessage());
                }
            } catch (FileNotFoundException ex) {
                System.out.println("Error: " + ex.getMessage());
            }
        }

    }

    private void getQueryExpander(Connection c_con) {
        queryExpander_map = new HashMap<>();
        try {
            if (!c_con.isClosed()) {
                Statement st_1 = c_con.createStatement();
                String s_1 = "Select table_name,search_sql from " + Constants.get_correct_table_name(c_con, "queryexpander");
                ResultSet r_1 = st_1.executeQuery(s_1);
                while (r_1.next()) {
                    String table_nm = Constants.get_correct_table_name(c_con, r_1.getString("table_name"));
                    String query = r_1.getString("search_sql");
                    if (table_nm != null) {
                        if (queryExpander_map.containsKey(table_nm)) {
                            queryExpander_map.get(table_nm).add(query);
                        } else {
                            ArrayList<String> query_list = new ArrayList<>();
                            query_list.add(query);
                            queryExpander_map.put(table_nm, query_list);
                        }
                    }
                }
                r_1.close();
                st_1.close();
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
//        catch (ServletException ex) {
//        }
    }

//    private Connection getConnection(boolean from_memory, int caller) throws ServletException, SQLException {
//        if (from_memory) {
//            if (ncon_mem == null || ncon_mem.isClosed()) {
//                ncon_mem = getDatasource_operational(true, 12).getConnection();
//            }
//            return ncon_mem;
//        } else {
//            if (ncon_disk == null || ncon_disk.isClosed()) {
//                ncon_disk = getDatasource_operational(false, 13).getConnection();
//            }
//            return ncon_disk;
//        }
//    }
    private DataSource getDatasource_operational(boolean from_memory, int caller) throws ServletException {
        if (from_memory) {
            if (dataSource_mem == null) {
                try {
                    Context env = (Context) new InitialContext().lookup("java:comp/env");
                    dataSource_mem = (DataSource) env.lookup(Constants.DATASOURCE_NAME_DATA_VIEW);
                    if (dataSource_mem == null) {
                        throw new ServletException("`" + Constants.DATASOURCE_NAME_DATA_VIEW + "' is an unknown DataSource");
                    }
                } catch (NamingException e) {
                    throw new ServletException(e);
                }
            }
//            try {
//                System.out.println("1548 Meme (" + caller + ") " + dataSource_mem.getConnection().getMetaData().getURL());
//            } catch (SQLException ex) {
//            }
            return dataSource_mem;
        } else {
            if (dataSource_disk == null) {
                try {
                    Context env = (Context) new InitialContext().lookup("java:comp/env");
                    dataSource_disk = (DataSource) env.lookup(Constants.DATASOURCE_NAME_DATA_UPDATE);
                    if (dataSource_disk == null) {
                        throw new ServletException("`" + Constants.DATASOURCE_NAME_DATA_UPDATE + "' is an unknown DataSource");
                    }
                } catch (NamingException e) {
                    throw new ServletException(e);
                }
            }
            try {
                System.out.println("1546 Disk   (" + caller + ") " + dataSource_disk.getConnection().getMetaData().getURL());
            } catch (SQLException ex) {
            }
            return dataSource_disk;
        }


    }

//    private DataSource getDatasource() throws ServletException {
//        if (dataSource == null) {
//            try {
//                Context env = (Context) new InitialContext().lookup("java:comp/env");
//                dataSource = (DataSource) env.lookup(Constants.DATASOURCE_NAME_DATA_CREATE);
//                if (dataSource == null) {
//                    throw new ServletException("`" + Constants.DATASOURCE_NAME_DATA_CREATE + "' is an unknown DataSource");
//                }
//            } catch (NamingException e) {
//                throw new ServletException(e);
//            }
//        }
//        return dataSource;
//    }
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

    @Override
    public void destroy() {
        //also distroy getfromdb thread
//        searchresults.stop();             
//        close(ncon_mem, null, null);
//        close(ncon_disk, null, null);
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
