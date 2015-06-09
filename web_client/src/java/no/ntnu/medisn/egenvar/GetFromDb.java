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
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import javax.servlet.ServletException;
import javax.sql.DataSource;

/**
 *
 * @author sabry
 *
 */
public class GetFromDb implements Runnable {

    private int server_mode;
    private Connection ncon;
    private DataSource datasource;
    private boolean done = false;
    private String current_tbl_nm;
    private String searchvalue;
    private String searchoperator;
    private String searchColumn;
    private String query;
    private String operation;
//    private boolean batch_editable;
    private boolean make_qr;
    private LinkedHashMap<String, Object[]> result_map;
    private int saftey = Integer.MAX_VALUE;
    private static final int split_limit = 1000;
    public static int max_limit_to_read_from_db = 50;
    private ArrayList<String> special_foreign_key_l;
    private final String ID_FLAG = "ID"; //modify with the auto increment primary key
    public final static String REPLACEWITH_ID_FALG = "_000_";
    public final static String IN_ID_FALG = "_IN_";
    public final static String USER_SELECT_CHKBX_PREFIX = "chkbx_";
    public static ArrayList<String> qr_column_name_l;
    private HashMap<String, ArrayList<String>> queryExpander_map;
    private final String DETAILS_FLAG = "MOREDETAILS";
    private final String IDFORDIV_SPLIT_FLAG = "DIVID";
    private final String EXPANSION_SPLIT_TOKEN = "===";
    private LinkedHashMap<String, Object> fromDbMap;
//    private String filter_column;
//    private String filter_value;
//    private HttpSession session;
    private boolean exactmaych_only;
    private int user_level;
    private boolean expand_to_get_tagged;
    private String filter_link_prefix;
    private Map<String, String[]> entered_values_map_tmp;
    private int search_mode;
    private boolean retireved = false;
//    public static HashMap<String, String> table2col_map;
    public static HashMap<String, String> tag_taget_map;
    public static ArrayList<String> tagable_table_list;

    public GetFromDb(int server_mode, DataSource datasource, String current_tbl_nm, String searchvalue, String searchoperator,
            String searchColumn, boolean exactmaych_only, String operation, int user_level, boolean expand_to_get_tagged,
            Map<String, String[]> entered_values_map_tmp, int search_mode) {
        this.datasource = datasource;
        this.current_tbl_nm = current_tbl_nm;
        this.searchvalue = searchvalue;
        this.searchoperator = searchoperator;
        this.searchColumn = searchColumn;
//        this.session = session;
        this.exactmaych_only = exactmaych_only;
        this.operation = operation;
        this.user_level = user_level;
        this.server_mode = server_mode;
        this.expand_to_get_tagged = expand_to_get_tagged;
        this.entered_values_map_tmp = entered_values_map_tmp;
        this.search_mode = search_mode;
    }

    public GetFromDb(int server_mode, DataSource datasource, String query, String operation, boolean batch_editable,
            boolean make_qr, HashMap<String, ArrayList<String>> queryExpander_map, int user_level, String filter_link_prefix) {
        this.datasource = datasource;
        this.query = query;
        this.make_qr = make_qr;
        this.queryExpander_map = queryExpander_map;
        this.operation = operation;
        this.user_level = user_level;
        this.server_mode = server_mode;
        this.filter_link_prefix = filter_link_prefix;

    }

    @Override
    public void run() {
        try {
            saftey = Integer.MAX_VALUE;
            result_map = new LinkedHashMap<>();
            if (qr_column_name_l == null) {
                qr_column_name_l = new ArrayList<>();
                qr_column_name_l.add("QUICK_RESPONSE_CODE");
            }
            if (getConnection(true) != null && current_tbl_nm != null && searchvalue != null && searchoperator != null) {
//                System.out.println("121 START " + Timing.getDateTime());
                makeSql_thenGetFromDb(getConnection(true));
//                System.out.println("124 STOP " + Timing.getDateTime());
            } else if (query != null) {
//                System.out.println("124 START " + Timing.getDateTime());
                getDataFromDB(getConnection(true), query, operation, filter_link_prefix);
//                System.out.println("127 STOP " + Timing.getDateTime());
            }
            close(getConnection(false), null, null);
        } catch (SQLException | ServletException ex) {
            try {
                close(getConnection(false), null, null);
            } catch (SQLException | ServletException e) {
            }
        } finally {
            try {
                close(getConnection(false), null, null);
            } catch (SQLException | ServletException e) {
            }
        }
        done = true;
    }

    public boolean hasFinished() {
        return done;
    }

    public int getSearchMode() {
        return search_mode;
    }

    public void clear() {
        result_map.clear();
    }

    public boolean wasRetrieved() {
        return retireved;
    }

    public LinkedHashMap<String, Object[]> getResultsMap() {
        while (!done && saftey > 0) {
            saftey--;
            try {
                Thread.sleep(100);
            } catch (InterruptedException ex) {
            }
        }
        retireved = true;
        return result_map;
    }

    public LinkedHashMap<String, Object> getFromDbsMap() {
        while (!done && saftey > 0) {
            saftey--;
            try {
                Thread.sleep(100);
            } catch (InterruptedException ex) {
            }
        }
        retireved = true;
        return fromDbMap;
    }

    private void makeSql_thenGetFromDb(Connection c_con) {
//        System.out.println("194 expand_to_get_tagged=" + expand_to_get_tagged + "  current_tbl_nm=" + current_tbl_nm + " searchvalue=" + searchvalue);
        //  expand_to_get_tagged=true  current_tbl_nm=tags searchvalue=hepatitis
        if (expand_to_get_tagged && current_tbl_nm.toUpperCase().endsWith("TAGS2HOST")) {
            boolean getall = false;
            if (searchvalue != null && (searchvalue.equals("%") || searchvalue.equals("*"))) {
                getall = false;
                query = "select * from " + Constants.get_correct_table_name(c_con, "tags2host");
            } else {
                if (exactmaych_only) {
                    query = "select * from " + Constants.get_correct_table_name(c_con, "tags2host") + " where name='" + searchvalue + "'";
                } else {
                    query = "select * from " + Constants.get_correct_table_name(c_con, "tags2host") + " where name like '%" + searchvalue + "%'";
                }
            }
            if (query != null) {
                query = query.replace(";", "");
            }
//            LinkedHashMap<String, Object[]> tmp_result_map = getRsults(searchvalue, query, getall);
//            result_map.putAll(tmp_result_map);
            result_map.putAll(getRsults(c_con, searchvalue, query, getall));
        } else if (expand_to_get_tagged && current_tbl_nm.toUpperCase().endsWith("TAGS")) {//!
            if (tag_taget_map == null || tagable_table_list == null) {
                tagable_table_list = new ArrayList<>();
//                table2col_map = new HashMap<>();
                tag_taget_map = new HashMap<>();
                ArrayList<String> all_tbl_l = Constants.getTables(c_con);
                for (int i = 0; i < all_tbl_l.size(); i++) {
                    if (all_tbl_l.get(i).toUpperCase().endsWith(Constants.TAGABLE_TABLE)) {
                        tagable_table_list.add(all_tbl_l.get(i));
//                        table2col_map.put(all_tbl_l.get(i), "name");
                        String tag_target = all_tbl_l.get(i).replace(Constants.TAGABLE_TABLE, "");
                        tag_target = Constants.get_correct_table_name(c_con, tag_target);
                        if (tag_target != null) {
                            tag_taget_map.put(all_tbl_l.get(i), tag_target);
                        }
                    }
                }
            }
            HashMap<String, String> table2col_map = new HashMap<>();
            for (int i = 0; i < tagable_table_list.size(); i++) {
                table2col_map.put(tagable_table_list.get(i), "name");

            }

            boolean getall = false;
            for (int i = 0; i < tagable_table_list.size(); i++) {
                String c_tbl = tagable_table_list.get(i);
                c_tbl = Constants.get_correct_table_name(c_con, c_tbl);
                if (c_tbl != null) {
                    if (searchvalue != null && searchvalue.contains("&&")) {
                        String[] searchvalue_a = searchvalue.split("&&");
                        LinkedHashMap<String, Object[]> last_result_map = null;
                        boolean matched = true;
                        for (int j = 0; (j < searchvalue_a.length && matched); j++) {
                            String c_searchvalue = searchvalue_a[j];
                            if (tag_taget_map.containsKey(c_tbl)) {
                                String tag_target = tag_taget_map.get(c_tbl);
                                query = "SELECT id FROM " + tag_target + " WHERE " + tag_target + ".id in (SELECT "
                                        + tag_target.split("\\.")[tag_target.split("\\.").length - 1] + "_id FROM " + c_tbl
                                        + expandQuery(c_con, c_tbl, c_searchvalue, searchoperator, table2col_map.get(tagable_table_list.get(i)), exactmaych_only) + ")";
                            } else {
                                query = "SELECT id FROM " + c_tbl + expandQuery(c_con, c_tbl, c_searchvalue, searchoperator,
                                        table2col_map.get(tagable_table_list.get(i)), exactmaych_only);
                            }
                            LinkedHashMap<String, Object[]> tmp_result_map = getRsults(c_con, searchvalue, query, getall);
//                            System.out.println("260 "+tmp_result_map.size());
//                            if (c_tbl.contains("FILES")) {
//                                System.out.println("253 " + query + "  ");
//                            }
                            LinkedHashMap<String, Object[]> tmp_tmp_result_map = new LinkedHashMap();

                            ArrayList<String> key_l = new ArrayList<>(tmp_result_map.keySet());
                            for (int k = 0; k < key_l.size(); k++) {
                                if (tmp_result_map.get(key_l.get(k))[1] != null && !tmp_result_map.get(key_l.get(k))[1].equals("0")) {
                                    tmp_tmp_result_map.put(key_l.get(k), tmp_result_map.get(key_l.get(k)));
//                                    for (int l = 0; l < tmp_result_map.get(key_l.get(k)).length; l++) {
//                                    System.out.println("260 " + tmp_result_map.get(key_l.get(k))[8]);

//                                    }
                                }
                            }

                            if (!tmp_tmp_result_map.isEmpty()) {
                                if (last_result_map == null) {
                                    last_result_map = new LinkedHashMap<>();
                                    last_result_map.putAll(tmp_tmp_result_map);
                                } else {

                                    ArrayList<String> last_map_key_l = new ArrayList<>(last_result_map.keySet());
                                    ArrayList<String> current_map_key_l = new ArrayList<>(tmp_tmp_result_map.keySet());
                                    for (int k = 0; k < last_map_key_l.size(); k++) {
                                        Object[] lat_vals = last_result_map.remove(last_map_key_l.get(k));
                                        for (int l = 0; l < current_map_key_l.size(); l++) {
                                            Object[] cur_vals = tmp_tmp_result_map.get(current_map_key_l.get(l));
//                                            System.out.println("276 cur_vals[0]=" + cur_vals[0] + " lat_vals[0]=" + lat_vals[0]);
                                            if (cur_vals[0] != null && lat_vals[0] != null && cur_vals[0].equals(lat_vals[0])) {
//                                                System.out.println("276 cur_vals[8]=" + cur_vals[8] + " lat_vals[8]=" + lat_vals[8]);
                                                if (cur_vals[8] != null && lat_vals[8] != null) {
                                                    Set<Integer> last_s = (Set<Integer>) lat_vals[8];
                                                    Set<Integer> curr_s = (Set<Integer>) cur_vals[8];
                                                    last_s.retainAll(curr_s);
                                                    if (!last_s.isEmpty()) {
                                                        Object[] ret_a = new Object[10];
                                                        ret_a[0] = cur_vals[0];
                                                        ret_a[1] = last_s.size() + "";
                                                        ret_a[2] = makeSTATIC_URL(c_con, last_s, cur_vals[0].toString(), Constants.getDocRoot(c_con));
                                                        ret_a[3] = current_tbl_nm;
                                                        ret_a[4] = searchvalue + " | Result=" + ret_a[2];
                                                        ret_a[5] = ret_a[2];
                                                        ret_a[6] = makeQR(c_con, ret_a[2].toString());
                                                        ret_a[7] = "0";
                                                        ret_a[8] = last_s;
                                                        last_result_map.put(last_map_key_l.get(k), ret_a);
                                                    }
                                                }
                                            }
                                        }

                                    }
                                }
                            } else {
                                if (last_result_map != null) {
                                    last_result_map.clear();
                                    Object[] ret_a = new Object[10];
                                    ret_a[0] = c_tbl;
                                    ret_a[1] = "0";
                                    ret_a[2] = "NA";
                                    ret_a[3] = current_tbl_nm;
                                    ret_a[4] = searchvalue;
                                    ret_a[7] = "0";
                                    ret_a[8] = null;
                                    last_result_map.put(searchvalue, ret_a);
                                    result_map.clear();
                                }
                                matched = false;
                                //clear last_result_map ?
                            }
                            if (last_result_map != null) {
                                result_map.putAll(last_result_map);
                            }
                        }
//                        System.out.println("300 query=" + query);
                    } else {
                        if (searchvalue != null && (searchvalue.equals("%") || searchvalue.equals("*"))) {
                            if (tag_taget_map.containsKey(c_tbl)) {
                                String tag_target = tag_taget_map.get(c_tbl);
                                query = "SELECT id FROM " + tag_target + " WHERE " + tag_target + ".id in (SELECT " + tag_target.split("\\.")[tag_target.split("\\.").length - 1] + "_id FROM " + c_tbl + ")";
                            } else {
                                query = "SELECT id FROM " + c_tbl;
                            }

                            getall = true;
                        } else {
                            if (searchvalue != null && !searchvalue.isEmpty()) {
                                if (tag_taget_map.containsKey(c_tbl)) {
                                    String tag_target = tag_taget_map.get(c_tbl);
                                    query = "SELECT id FROM " + tag_target + " WHERE " + tag_target + ".id in (SELECT "
                                            + tag_target.split("\\.")[tag_target.split("\\.").length - 1] + "_id FROM " + c_tbl
                                            + expandQuery(c_con, c_tbl, searchvalue, searchoperator, table2col_map.get(tagable_table_list.get(i)), exactmaych_only) + ")";
                                } else {
                                    query = "SELECT id FROM " + c_tbl + expandQuery(c_con, c_tbl, searchvalue, searchoperator,
                                            table2col_map.get(tagable_table_list.get(i)), exactmaych_only);
                                }
//                                System.out.println("359 " + query);
//                            query = "SELECT id FROM " + c_tbl + expandQuery(c_tbl, searchvalue, searchoperator, table2col_map.get(tabl_l.get(i)), exactmaych_only);
                            } else {
//                            query = "SELECT id FROM " + c_tbl;
//                            getall = true;
                                if (tag_taget_map.containsKey(c_tbl)) {
                                    String tag_target = tag_taget_map.get(c_tbl);
                                    query = "SELECT id FROM " + tag_target + " WHERE " + tag_target + ".id in (SELECT " + tag_target.split("\\.")[tag_target.split("\\.").length - 1] + "_id FROM " + c_tbl + ")";
                                } else {
                                    query = "SELECT id FROM " + c_tbl;
                                }
                                getall = true;
                            }
                        }
                        if (query != null) {
                            query = query.replace(";", "");
                        }

                        LinkedHashMap<String, Object[]> tmp_result_map = getRsults(c_con, searchvalue, query, getall);
//                        System.out.println("373 "+tmp_result_map.keySet()+"  "+query);
                        result_map.putAll(tmp_result_map);
                    }
                } else {
                    query = "select message from  " + Constants.get_correct_table_name(c_con, "errormsgs") + " where id=7";
                    if (query != null) {
                        query = query.replace(";", "");
                    }
                    LinkedHashMap<String, Object[]> tmp_result_map = getRsults(c_con, searchvalue, query, getall);
                    result_map.putAll(tmp_result_map);
                }
            }

        } else if (current_tbl_nm.toUpperCase().equals("AUTOFILL")) {
            HashMap<String, String> search_map = new HashMap<>();
            HashMap<String, String> table2col_map = new HashMap<>();
            String search_q = null;
            if (entered_values_map_tmp != null && !entered_values_map_tmp.isEmpty()) {
                ArrayList<String> key_l = new ArrayList<>(entered_values_map_tmp.keySet());
                for (int i = 0; i < key_l.size(); i++) {
                    String c_var = key_l.get(i);
                    if (c_var != null && !c_var.isEmpty() && c_var.toUpperCase().endsWith(Constants.TAGSOURCE_TABLE)) {
                        String[] vla_a = entered_values_map_tmp.get(c_var);
                        if (vla_a != null && vla_a.length > 0) {
                            String[] tmp_a = vla_a[0].split(c_var + "=");//[0].replaceAll("[^0-9]", "");
                            if (tmp_a.length == 2) {
                                tmp_a[1] = tmp_a[1].replaceAll("[^0-9]", "");
                                search_map.put(c_var, c_var + "=" + tmp_a[1]); //
                                String link = Constants.getServerName(c_con) + "Search/SearchResults3?" + Constants.TABLE_TO_USE_FLAG + c_var + "=" + tmp_a[1];
                                if (search_q == null) {
                                    search_q = "<a href=\"" + link + "\">" + c_var.replace(Constants.TAGSOURCE_TABLE, "") + "=" + tmp_a[1] + "</a>";
                                } else {
                                    search_q = search_q + "+" + "<a href=\"" + link + "\">" + c_var.replace(Constants.TAGSOURCE_TABLE, "") + "=" + tmp_a[1] + "</a>";
                                }
                            }
                        }
                    }
                }
            }
            if (search_q == null) {
                search_q = "NA";
            }
            LinkedHashMap<String, Object[]> tmp_result_map = getForAutoCompletion(c_con, search_map, table2col_map, search_q);
            result_map.putAll(tmp_result_map);
        } else {
            ArrayList<String> tabl_l = new ArrayList<>();
            HashMap<String, String> table2col_map = new HashMap<>();
            if (current_tbl_nm.toUpperCase().endsWith("NAME1")) {
//            last_search_type = "NAME1";
                tabl_l.add("FILES2PATH");
//                tabl_l.add("FILES");
                table2col_map.put("FILES2PATH", "FILEPATH");
//                table2col_map.put("FILES2PATH", "FILEPATH,LOCATION");
//                table2col_map.put("FILES", "name");
            } else if (current_tbl_nm.toUpperCase().endsWith("NAME2")) {
//            last_search_type = "NAME2";
                tabl_l.add("report");
                tabl_l.add("report_batch");
                table2col_map.put("report", "name");
                table2col_map.put("report_batch", "name");
            } else if (current_tbl_nm.toUpperCase().endsWith("NAME3")) {
//            last_search_type = "NAME3";
                tabl_l.add("donordetails");
                tabl_l.add("sampledetails");
                table2col_map.put("donordetails", "name");
                table2col_map.put("sampledetails", "name");
            } else if (current_tbl_nm.toUpperCase().endsWith("NAME4")) {
//            last_search_type = "NAME4";
                ArrayList<String> all_tbl_l = Constants.getTables(c_con);
                for (int i = 0; i < all_tbl_l.size(); i++) {
                    if (all_tbl_l.get(i).toUpperCase().endsWith(Constants.TAGSOURCE_TABLE)) {
                        tabl_l.add(all_tbl_l.get(i));
                        table2col_map.put(all_tbl_l.get(i), "name");
                    }
                }
            } else if (current_tbl_nm.toUpperCase().endsWith("NAME5")) {
//            last_search_type = "NAME5";
//                find_children = true;
                ArrayList<String> all_tbl_l = Constants.getTables(c_con);
                for (int i = 0; i < all_tbl_l.size(); i++) {
                    if (all_tbl_l.get(i).toUpperCase().endsWith(Constants.TAGABLE_TABLE)) {
                        tabl_l.add(all_tbl_l.get(i));
                        table2col_map.put(all_tbl_l.get(i), "name");
                    }
                }
            } else if (current_tbl_nm.toUpperCase().endsWith("NAME6")) {
                tabl_l.add("person");
                table2col_map.put("person", "NAME,LASTNM,EMAIL");
//                table2col_map.put("person", "LASTNM");
//                table2col_map.put("person", "EMAIL");
            } else {
                tabl_l.add(current_tbl_nm);
                table2col_map.put(current_tbl_nm, searchColumn);
            }

            boolean getall = false;
            for (int i = 0; i < tabl_l.size(); i++) {
                ArrayList<String> query_l = new ArrayList<>();
                String c_tbl = tabl_l.get(i);
                c_tbl = Constants.get_correct_table_name(c_con, c_tbl);
                if (c_tbl != null) {
                    if (searchvalue != null && (searchvalue.equals("%") || searchvalue.equals("*"))) {
                        query = "SELECT id FROM " + c_tbl;
                        getall = true;
                    } else {
                        if (searchvalue != null && !searchvalue.isEmpty()) {
//                            String[] col_a =table2col_map.get(tabl_l.get(i)).split("\\|");
                            query = "SELECT id FROM " + c_tbl + expandQuery(c_con, c_tbl, searchvalue, searchoperator,
                                    table2col_map.get(tabl_l.get(i)), exactmaych_only);
//                            System.out.println("486 " + query);
                        } else {
                            query = "SELECT id FROM " + c_tbl;
                            getall = true;
                        }
                    }
                } else {
                    query = "select message from  " + Constants.get_correct_table_name(c_con, "errormsgs") + " where id=7";
                }

                if (query != null) {
                    query = query.replace(";", "");
                }
//                System.out.println("452 "+searchvalue+"\t"+ query);
                LinkedHashMap<String, Object[]> tmp_result_map = getRsults(c_con, searchvalue, query, getall);
                result_map.putAll(tmp_result_map);
//                result_map.putAll(getRsults(searchvalue, query, getall));
            }
        }
//        System.out.println("488 " + result_map.keySet());

    }

    private LinkedHashMap<String, Object[]> getForAutoCompletion(Connection c_con, HashMap<String, String> search_map,
            HashMap<String, String> table2col_map, String user_query) {
        boolean getall = false;

        ArrayList<String> all_tbl_l = Constants.getTables(c_con);
        ArrayList<String> tabl_l = new ArrayList<>();
        ArrayList<String> key_l = new ArrayList<>(search_map.keySet());
        HashMap<String, ArrayList<Integer>> table2id_map = new HashMap<>();
        HashMap<String, String> tagtable2Target_map = new HashMap<>();
        HashMap<String, String> tagtable2TargetColumn_map = new HashMap<>();
        for (int i = 0; i < all_tbl_l.size(); i++) {
            String c_tbl = all_tbl_l.get(i);
            if (c_tbl.toUpperCase().endsWith("2TAGS")) {
                tabl_l.add(c_tbl);
                table2col_map.put(c_tbl, "LINK_TO_FEATURE");
                table2id_map.put(c_tbl, new ArrayList<Integer>());
                String target_tbl = c_tbl.toUpperCase().split("2TAGS")[0];
                target_tbl = Constants.get_correct_table_name(c_con, target_tbl);
                if (target_tbl != null) {
                    tagtable2Target_map.put(c_tbl, target_tbl);
                    tagtable2TargetColumn_map.put(c_tbl, target_tbl.split("\\.")[ target_tbl.split("\\.").length - 1] + "_id");//  
                }

            }
        }
        for (int i = 0; i < tabl_l.size(); i++) {
            String c_tbl = tabl_l.get(i);
            c_tbl = Constants.get_correct_table_name(c_con, c_tbl);
            if (search_map.isEmpty()) {
            } else if (tagtable2Target_map.containsKey(c_tbl)) {
                String c_sql = "SELECT id from " + tagtable2Target_map.get(c_tbl) + " where id  in(SELECT " + tagtable2TargetColumn_map.get(c_tbl) + " from " + c_tbl + " where " + table2col_map.get(tabl_l.get(i)) + "='" + search_map.get(key_l.get(0)) + "')";

                ArrayList<Integer> c_result_l = getIds(c_con, c_sql);
                table2id_map.get(tabl_l.get(i)).addAll(c_result_l);
                for (int j = 1; (j < key_l.size() && (!table2id_map.get(tabl_l.get(i)).isEmpty())); j++) {
                    c_sql = "SELECT id from " + tagtable2Target_map.get(c_tbl) + " where id  in(SELECT " + tagtable2TargetColumn_map.get(c_tbl) + " from " + c_tbl + " where " + table2col_map.get(tabl_l.get(i)) + "='" + search_map.get(key_l.get(j)) + "')";
                    c_result_l = getIds(c_con, c_sql);
                    table2id_map.get(tabl_l.get(i)).retainAll(c_result_l);
                }
            }
        }
        LinkedHashMap<String, Object[]> new_result_map = new LinkedHashMap<>();

        for (int i = 0; i < tabl_l.size(); i++) {
            ArrayList<Integer> c_result_l = table2id_map.get(tabl_l.get(i));
            if (c_result_l.isEmpty()) {
                Object[] ret_a = new Object[10];
                ret_a[0] = tabl_l.get(i);
                ret_a[1] = "0";
                ret_a[2] = "NA";
                ret_a[3] = current_tbl_nm;
                ret_a[4] = searchvalue;
                ret_a[7] = "0";

                new_result_map.put(user_query + " from " + tabl_l.get(i), ret_a);
            } else {
                String all_reslts_url = makeSTATIC_URL(c_con, c_result_l, tagtable2Target_map.get(tabl_l.get(i)), Constants.getDocRoot(c_con));
                int total_results = c_result_l.size();
                if (total_results > split_limit) {
                    Object[] ret_a = new Object[10];
                    ret_a[0] = tabl_l.get(i);//+ " ("++")";
                    ret_a[1] = total_results;
                    ret_a[2] = "SPLIT";
                    ret_a[3] = current_tbl_nm;
                    ret_a[4] = searchvalue;
                    ret_a[5] = all_reslts_url;
                    ret_a[6] = makeQR(c_con, all_reslts_url);
                    ret_a[7] = "0";
                    ret_a[8] = c_result_l;
                    new_result_map.put(" from " + tabl_l.get(i), ret_a);
                }

                if (!c_result_l.isEmpty()) {
                    String query_to_include = user_query;
                    if (getall) {
                        query_to_include = "Get all";
                    }
                    if (c_result_l.size() > split_limit) {
                        query_to_include = query_to_include + ", too many results, split ";
                        int split = 0;
                        while (!c_result_l.isEmpty()) {
                            split++;
                            ArrayList<Integer> spliter_l = new ArrayList<>(split_limit);
                            if (c_result_l.size() > split_limit) {
                                for (int k = 0; ((k < split_limit) && (k < c_result_l.size())); k++) {
                                    spliter_l.add(c_result_l.remove(k));
                                }
                            } else {
                                for (int k = 0; k < c_result_l.size(); k++) {
                                    spliter_l.add(c_result_l.remove(k));
                                    k--;
                                }
                            }
                            Object[] ret_a = new Object[10];
                            ret_a[0] = tabl_l.get(i);
                            ret_a[1] = spliter_l.size() + "";
                            ret_a[2] = makeSTATIC_URL(c_con, spliter_l, tagtable2Target_map.get(tabl_l.get(i)), Constants.getDocRoot(c_con));
                            ret_a[3] = current_tbl_nm;
                            ret_a[4] = searchvalue + " | Result=" + all_reslts_url;
                            ret_a[5] = all_reslts_url;
                            ret_a[6] = makeQR(c_con, ret_a[2].toString());
                            ret_a[7] = "0";
                            ret_a[8] = c_result_l;
//                                new_result_map.put(query_to_include + "(" + split + ") " + table_nm, table_nm + "||" + spliter_l.size() + "||" + makeSTATIC_URL(spliter_l, table_nm, Constants.getDocRoot()));
                            new_result_map.put(query_to_include + "(" + split + ") " + tabl_l.get(i), ret_a);

                        }
                    } else {
                        Object[] ret_a = new Object[10];
                        ret_a[0] = tabl_l.get(i);
                        ret_a[1] = c_result_l.size() + "";
                        ret_a[2] = makeSTATIC_URL(c_con, c_result_l, tagtable2Target_map.get(tabl_l.get(i)), Constants.getDocRoot(c_con));
                        ret_a[3] = current_tbl_nm;
                        ret_a[4] = searchvalue + "| Result=" + ret_a[2];
                        ret_a[5] = ret_a[2];
                        ret_a[6] = makeQR(c_con, ret_a[2].toString());
                        new_result_map.put(query_to_include + " from " + tabl_l.get(i), ret_a);
                        ret_a[7] = "0";
                        ret_a[8] = c_result_l;
                    }
                } else {
                    Object[] ret_a = new Object[10];
                    ret_a[0] = tabl_l.get(i);
                    ret_a[1] = total_results + "";
                    ret_a[2] = "NA";
                    ret_a[3] = current_tbl_nm;
                    ret_a[4] = searchvalue;
                    ret_a[7] = "0";
//                        new_result_map.put(user_query + " from " + table_nm, table_nm + "||" + total_results + "||NA");
                    new_result_map.put(user_query + " from " + tabl_l.get(i), ret_a);
                }
            }
        }



//        System.out.println("532 "+new_result_map);
        return new_result_map;
    }

    private String expandQuery(Connection c_con, String table_nm, String searchvalue, String searchoperator,
            String searchColumn, boolean exactmaych_only) {
//        System.out.println("578 table_nm=" + table_nm + "   searchoperator=" + searchoperator + "  searchColumn=" + searchColumn);
        String expanded_query = null;
        String combine_op = " OR ";
        try {
            String[] serachColmn_names_a = searchColumn.split(",");
            String[] serachValue_a = new String[1];
            searchvalue = searchvalue.replaceAll("\\r|\\n", "|");
            searchvalue = searchvalue.replaceAll("\\|\\|", "|");
            searchvalue = searchvalue.replaceAll("\\|\\|", "|");
            if (exactmaych_only && searchvalue.contains("|")) {
                searchoperator = " in ";
                serachValue_a[0] = searchvalue;
            } else if ((searchvalue.startsWith("\"") && searchvalue.endsWith("\""))) {
                if (searchoperator == null) {
                    searchoperator = "=";
                }
                serachValue_a[0] = searchvalue;
            } else {
                searchvalue = searchvalue.replaceAll("\\*", "%");
                searchvalue = searchvalue.replaceAll("\\?", "_");
                if (searchoperator != null && searchoperator.trim().toUpperCase().equals("IN")) {
                    serachValue_a[0] = searchvalue;
                } else {
//                    if (searchvalue.contains("|")) {
//                        serachValue_a = searchvalue.split("\\|");
//                    } else 
                    if (searchvalue.contains("&")) {
                        serachValue_a = searchvalue.split("&");
                        combine_op = " AND ";
                    } else {
                        serachValue_a[0] = searchvalue;
                    }

                }
                if (searchoperator != null && !searchoperator.trim().toUpperCase().equals("IN")) {
                    if (exactmaych_only) {
                        searchoperator = "=";
                    } else if (searchoperator == null && searchvalue.contains("%")) {
                        searchoperator = " like ";
                    } else if (searchoperator == null) {
                        searchoperator = "=";
                    }
                }

            }

            for (int i = 0; i < serachColmn_names_a.length; i++) {
                String quat = "'";
                boolean intonly = false;
                if (!Constants.shoulditbequated(c_con, table_nm, serachColmn_names_a[i])) {
                    quat = "";
                    intonly = true;
                    if (searchoperator != null && !searchoperator.trim().toUpperCase().equals("IN")) {
                        searchoperator = "=";
                    }
                }

                for (int j = 0; j < serachValue_a.length; j++) {
                    serachValue_a[j] = serachValue_a[j].replaceAll("'", "");
                    if (intonly) {
                        if (!serachValue_a[j].matches("[0-9\\.]+")) {
                            serachValue_a[j] = "-1";
                        }
                    }
                    String c_searchoperator = searchoperator;
                    if ((serachValue_a[j].startsWith("\"") && serachValue_a[j].endsWith("\""))) {
                        if (searchoperator != null && !searchoperator.trim().toUpperCase().equals("IN")) {
                            c_searchoperator = "=";
                            serachValue_a[j] = serachValue_a[j].replaceAll("\"", "");
                        }
                    } else if (searchoperator != null && searchoperator.trim().equalsIgnoreCase("like")) {
                        if (serachValue_a[j].isEmpty() || serachValue_a[j].matches("[\\s]+")) {
                            serachValue_a[j] = "";
                        } else {
                            if (!serachValue_a[j].startsWith("%")) {
                                serachValue_a[j] = "%" + serachValue_a[j];
                            }
                            if (!serachValue_a[j].endsWith("%")) {
                                serachValue_a[j] = serachValue_a[j] + "%";
                            }
                        }
                    }

                    if (expanded_query == null || expanded_query.isEmpty()) {
                        if (serachValue_a[j].isEmpty() || serachValue_a[j].matches("[\\s]+")) {
                            expanded_query = " ";
                        } else {
                            if (c_searchoperator != null && c_searchoperator.trim().toUpperCase().equals("IN")) {
                                serachValue_a[j] = serachValue_a[j].replaceAll("\\|", quat + "," + quat);
                                expanded_query = " WHERE " + serachColmn_names_a[i] + " " + c_searchoperator + " (" + quat + serachValue_a[j] + quat + ")";
                            } else {
                                expanded_query = " WHERE " + serachColmn_names_a[i] + " " + c_searchoperator + " " + quat + serachValue_a[j] + quat;
                            }
                        }
                    } else {
                        if (serachValue_a[j].isEmpty() || serachValue_a[j].matches("[\\s]+")) {
                        } else {
                            if (c_searchoperator != null && c_searchoperator.trim().toUpperCase().equals("IN")) {
                            } else {
                            }
                            expanded_query = expanded_query + combine_op + serachColmn_names_a[i] + " " + c_searchoperator + " " + quat + serachValue_a[j] + quat;
                        }
                    }
                }

            }

        } catch (Exception ex) {
            ex.printStackTrace();
            expanded_query = " WHERE id=-1";
        }

        return expanded_query;
    }
    /*
     ?TABLETOUSE_TAGS2HOST=31aabf2cd3c936b835b75dcb071c6430064ca76d&query_used=polymerase|%20Result=31aabf2cd3c936b835b75dcb071c6430064ca76d&last_search_type=TAGS2HOST&reset=true&operation=search
     */

    private LinkedHashMap<String, Object[]> getRsults(Connection c_con, String user_query, String sql, boolean getall) {
        if (user_query != null && user_query.length() > 100) {
            user_query = user_query.substring(0, 99) + "....";
        }
//        try {
//            System.out.println("713 query=" + query + "\n\t" + c_con.getMetaData().getURL());
//        } catch (Exception e) {
////            e.printStackTrace();
//        }

        LinkedHashMap<String, Object[]> new_result_map = new LinkedHashMap<>();
        Statement st_1 = null;
        ResultSet r_1 = null;
//        ArrayList<Integer> id_l = new ArrayList<>(10);
        Set<Integer> id_s = new HashSet<>();

        try {
            if (!c_con.isClosed()) {
                st_1 = c_con.createStatement();
//                st_1.setQueryTimeout(60);
                r_1 = st_1.executeQuery(sql);
                ResultSetMetaData rsmd = r_1.getMetaData();
                String table_nm = rsmd.getTableName(1);
                int table_access_level = Constants.getTable_level(c_con, table_nm, 1);

                if (user_level <= table_access_level) {
                } else {
                    r_1 = st_1.executeQuery("select id,message from  " + Constants.get_correct_table_name(c_con, "errormsgs") + " where id=9");
//                    System.out.println("731 Acces block table_nm=" + table_nm + " table_access_level=" + table_access_level + " user_level=" + user_level);
                }
                rsmd = r_1.getMetaData();
                table_nm = rsmd.getTableName(1);

                boolean id_col_found = false;
                for (int i = 0; (i < rsmd.getColumnCount() && !id_col_found); i++) {
                    if (rsmd.getColumnName(i + 1).toUpperCase().equals("ID")) {
                        id_col_found = true;
                    }
                }

                if (id_col_found) {
                    while (r_1.next()) {
                        id_s.add(r_1.getInt("id"));
                    }
                    String all_reslts_url = makeSTATIC_URL(c_con, id_s, table_nm, Constants.getDocRoot(c_con));
                    r_1.close();
                    int total_results = id_s.size();
                    if (total_results > split_limit) {
                        Object[] ret_a = new Object[10];
                        ret_a[0] = table_nm;//+ " ("++")";
                        ret_a[1] = total_results;
                        ret_a[2] = "SPLIT";
                        ret_a[3] = current_tbl_nm;
                        ret_a[4] = searchvalue;
                        ret_a[5] = all_reslts_url;
                        String expan_out_str = Constants.getServerName(c_con) + "Search/SearchResults3?"
                                + Constants.TABLE_TO_USE_FLAG + table_nm + "=" + all_reslts_url + "&reset=true&operation="
                                + Constants.SERCH_OP_FLAG + "&current_tbl_nm=" + table_nm;
                        ret_a[6] = makeQR(c_con, expan_out_str);
                        ret_a[7] = "0";
                        ret_a[8] = id_s;
                        ret_a[9] = search_mode;
                        new_result_map.put(user_query + " from " + table_nm, ret_a);
                    } else {
//                        new_result_map.put(user_query + " from " + table_nm, table_nm + "||" + total_results + "||NA");
                    }

                    if (!id_s.isEmpty()) {
                        String query_to_include = user_query;
                        if (getall) {
                            query_to_include = "Get all";
                        }
                        if (id_s.size() > split_limit) {
                            query_to_include = query_to_include + ", too many results, split ";
                            int split = 0;
                            Iterator<Integer> id_i = id_s.iterator();
                            while (id_i.hasNext()) {
                                split++;
                                Set<Integer> spliter_l = new HashSet<>();
                                int split_count = 0;
                                while (id_i.hasNext() && split_count < split_limit) {
                                    split_count++;
                                    spliter_l.add(id_i.next());
                                    id_i.remove();
                                }
//                                if (id_l.size() > split_limit) {
//                                    for (int i = 0; ((i < split_limit) && (i < id_l.size())); i++) {
//                                        spliter_l.add(id_l.remove(i));
//                                    }
//                                } else {
//                                    for (int i = 0; i < id_l.size(); i++) {
//                                        spliter_l.add(id_l.remove(i));
//                                        i--;
//                                    }
//                                }
                                Object[] ret_a = new Object[10];
                                ret_a[0] = table_nm;
                                ret_a[1] = spliter_l.size() + "";
                                ret_a[2] = makeSTATIC_URL(c_con, spliter_l, table_nm, Constants.getDocRoot(c_con));
                                ret_a[3] = current_tbl_nm;
                                ret_a[4] = searchvalue + " | Result=" + all_reslts_url;
                                ret_a[5] = all_reslts_url;
                                String expan_out_str = Constants.getServerName(c_con) + "Search/SearchResults3?"
                                        + Constants.TABLE_TO_USE_FLAG + table_nm + "=" + ret_a[2] + "&reset=true&operation="
                                        + Constants.SERCH_OP_FLAG + "&current_tbl_nm=" + table_nm;

                                ret_a[6] = makeQR(c_con, expan_out_str);
                                ret_a[7] = "0";
                                ret_a[8] = id_s;
//                                new_result_map.put(query_to_include + "(" + split + ") " + table_nm, table_nm + "||" + spliter_l.size() + "||" + makeSTATIC_URL(spliter_l, table_nm, Constants.getDocRoot()));
                                new_result_map.put(query_to_include + "(" + split + ") " + table_nm, ret_a);
                            }
                        } else {
                            Object[] ret_a = new Object[10];
                            ret_a[0] = table_nm;
                            ret_a[1] = id_s.size() + "";
                            ret_a[2] = makeSTATIC_URL(c_con, id_s, table_nm, Constants.getDocRoot(c_con));
                            ret_a[3] = current_tbl_nm;
                            ret_a[4] = searchvalue + "| Result=" + ret_a[2];
                            ret_a[5] = ret_a[2];
                            String expan_out_str = Constants.getServerName(c_con) + "Search/SearchResults3?" + Constants.TABLE_TO_USE_FLAG
                                    + table_nm + "=" + ret_a[2] + "&reset=true&operation=" + Constants.SERCH_OP_FLAG + "&current_tbl_nm=" + table_nm;
                            ret_a[6] = makeQR(c_con, expan_out_str);
                            new_result_map.put(query_to_include + " from " + table_nm, ret_a);
                            ret_a[7] = "0";
                            ret_a[8] = id_s;
                        }
//                        id_s.clear();
                    } else {
                        Object[] ret_a = new Object[10];
                        ret_a[0] = table_nm;
                        ret_a[1] = total_results + "";
                        ret_a[2] = "NA";
                        ret_a[3] = current_tbl_nm;
                        ret_a[4] = searchvalue;
                        ret_a[7] = "0";
                        ret_a[8] = null;
//                        new_result_map.put(user_query + " from " + table_nm, table_nm + "||" + total_results + "||NA");
                        new_result_map.put(user_query + " from " + table_nm, ret_a);
                    }
                }
                st_1.close();
            } else {
                System.out.println("181 connection closed");
            }
        } catch (SQLException ex) {
            Object[] ret_a = new Object[10];
            ret_a[0] = "Error";
            ret_a[1] = "0";
            ret_a[2] = "NA";
            ret_a[3] = current_tbl_nm;
            ret_a[4] = searchvalue;
            ret_a[7] = "0";
            ret_a[8] = null;
//            new_result_map.put(user_query + " from (0 Results splited) Error: " + ex.getMessage() + "", "Error||0||NA");
            new_result_map.put(user_query + " Error: " + ex.getMessage() + "", ret_a);

        } catch (Exception ex) {
            Object[] ret_a = new Object[10];
            ret_a[0] = "Error";
            ret_a[1] = "0";
            ret_a[2] = "NA";
            ret_a[3] = current_tbl_nm;
            ret_a[4] = searchvalue;
            ret_a[7] = "0";
            ret_a[8] = null;
            new_result_map.put(user_query + "  Error: " + ex.getMessage() + "", ret_a);
        } finally {
            close(null, st_1, r_1);
        }
//       ArrayList<String> k_l = new ArrayList<>(new_result_map.keySet());
//        for (int i = 0; i < k_l.size(); i++) {   
//            System.out.println("886 "+new_result_map.get( k_l.get(i))[8]);            
//        }

        return new_result_map;
    }

    private void getDataFromDB(Connection c_con, String query, String operation, String split_link_prefix) {

        done = false;
        retireved = false;
        fromDbMap = new LinkedHashMap<>();
        ArrayList<Integer> id_list_l = new ArrayList<>(10);
        if (split_link_prefix == null) {
            split_link_prefix = Constants.getServerName(c_con) + "Search/SearchResults3?" + Constants.TABLE_TO_USE_FLAG;
        }
        Statement st_1 = null;
        ResultSet r_1 = null;
        boolean restricted_display = false;

        ArrayList<Integer> restric_column_l = new ArrayList<>();
        boolean restrict_last_modified_inf = false;
        if (user_level > SearchResults3.MIN_LEVEL_PATH) {
            restricted_display = true;
        }
        if (user_level > SearchResults3.MIN_LEVEL_DATE) {
            restrict_last_modified_inf = true;
        }
//        System.out.println("969 user_level="+user_level+" restricted_display="+restricted_display);
        try {
            ArrayList<ArrayList<String>> resultsDisplay_data_l = new ArrayList<>();
            ArrayList<String> resultsDisplay_column_nm_l = new ArrayList<>(10);
            HashMap<Integer, String> linkExpand_map = new HashMap<>();
            ArrayList<Integer> special_foreign_keys_list = new ArrayList<>(1);
            ArrayList<Integer> qr_list = new ArrayList<>(1);
            ArrayList<Integer> timestamp_list = new ArrayList<>(1);
            ArrayList<Integer> link_to_feature_list = new ArrayList<>(1);
            ArrayList<String> split_links_l = new ArrayList<>(1);
            String table_in_query = null;

            if (!c_con.isClosed() && query != null && !query.isEmpty()) {
//                String doc_root = Constants.getDocRoot();
                st_1 = c_con.createStatement();
                r_1 = st_1.executeQuery(query);
                ResultSetMetaData rsmd = r_1.getMetaData();
                String table_nm = rsmd.getTableName(1);
                int table_access_level = Constants.getTable_level(c_con, table_nm, 2);
                if (user_level <= table_access_level) {
                } else {
                    r_1 = st_1.executeQuery("select message from  " + Constants.get_correct_table_name(c_con, "errormsgs") + " where id=9");
                }
                rsmd = r_1.getMetaData();
                int NumOfCol = rsmd.getColumnCount();
                int id_column_pos = -1;
                HashSet<Integer> dont_show_s = new HashSet<>();

                boolean is_hierarchical = false;
                HashMap<String, String[]> frgn_key_map = null;
                boolean isTagSource = false;
                String tag_link = null;
                String tag_name = null;
                String tag_uri = null;
                for (int i = 1; i < NumOfCol + 1; i++) {
                    if (i == 1) {
                        table_in_query = Constants.get_correct_table_name(c_con, rsmd.getTableName(1));
                        if (table_in_query != null) {
                            if (table_in_query.toUpperCase().endsWith("_TAGSOURCE")) {
                                isTagSource = true;
                            }
                            if (restricted_display) {
                                if (table_in_query.equals(Constants.get_correct_table_name(c_con, "files"))
                                        || table_in_query.equals(Constants.get_correct_table_name(c_con, "files2path"))) {
                                    restricted_display = true;
                                } else {
                                    restricted_display = false;
                                }
                            }
                            frgn_key_map = Constants.get_key_contraints(c_con, table_in_query);
                            if (frgn_key_map.containsKey(Constants.FOREIGN_KEY_NAMES_FLAG)) {
                                String[] tmp_a = frgn_key_map.get(Constants.FOREIGN_KEY_NAMES_FLAG);
                                for (int j = 0; j < tmp_a.length; j++) {
                                    if (tmp_a[j].toLowerCase().startsWith(Constants.PARENT_REF_FLAG)) {
                                        is_hierarchical = true;
                                    }
                                }
                            } else {
                                System.out.println("913 NO " + Constants.FOREIGN_KEY_NAMES_FLAG);
                            }
                        }
                    }
                    String column_nm = rsmd.getColumnName(i).toUpperCase(); //changed 20-03-2015
                    String column_nm_u = column_nm.toUpperCase();
                    if (restricted_display) {
                        if (column_nm_u.equals("FILEPATH") || column_nm_u.equals("OWNERGROUP") || column_nm_u.equals("NAME")) {
                            restric_column_l.add(i);
                        }
                    }
                    if (column_nm_u.equals("LINEAGE")) {
                        dont_show_s.add(i);
                    } else {
                        int column_type = Constants.getType(c_con, table_in_query, column_nm);
                        if (column_type == Types.TIMESTAMP) {
                            timestamp_list.add(i);
                        }
                        resultsDisplay_column_nm_l.add(column_nm);
                        if (frgn_key_map != null && frgn_key_map.containsKey(column_nm)) {
                            String target_table = frgn_key_map.get(column_nm)[1];
                            String serachType = frgn_key_map.get(column_nm)[2];
//                        String query_link = "<a  href=\"" + Constants.getServerName() + "Search/SearchResults3?current_tbl_nm=" + target_table + "&serachType=" + serachType + "&searchvalue=" + REPLACEWITH_ID_FALG + "&reload=true&previous_tbl_nm=" + table_in_query + "\">" + REPLACEWITH_ID_FALG + "</a>";
                            String query_link = "<a  href=\"" + Constants.getServerName(c_con) + "Search/SearchResults3?"
                                    + Constants.TABLE_TO_USE_FLAG + target_table + "|" + serachType + "=" + REPLACEWITH_ID_FALG
                                    + "&reset=true&operation=" + Constants.SERCH_OP_FLAG + "\">" + REPLACEWITH_ID_FALG + "</a>";

                            linkExpand_map.put(i, query_link);
                        } else if (getIgnoreCase(special_foreign_key_l, column_nm) != null) {
                            special_foreign_keys_list.add(i);
                        } else if (column_nm_u.equals(ID_FLAG)) { //smd.isAutoIncrement(i) will not work for tagsource
                            id_column_pos = i;
                            String query_link = REPLACEWITH_ID_FALG;
                            if (operation == null || !operation.equals(Constants.SERCH_OP_FLAG)) {
                                query_link = query_link + " <input type=\"checkbox\" name=\"" + USER_SELECT_CHKBX_PREFIX + "[]\"  id=\""
                                        + REPLACEWITH_ID_FALG + "\" value=\"" + REPLACEWITH_ID_FALG + "\" onchange='setSingle(this);' />";
                            }

                            linkExpand_map.put(i, query_link);
                            if (is_hierarchical) {
//                            query_link = query_link + "
                                query_link = query_link + " <a href=\"" + Constants.getServerName(c_con) + "Search/SearchResults3?"
                                        + Constants.TABLE_TO_USE_FLAG + table_in_query + "|PARENT_ID=" + REPLACEWITH_ID_FALG + "&reset=true&operation=" + Constants.SERCH_OP_FLAG + "\">Children</a>";

//                            query_link = query_link + " <a href=\"" + Constants.getServerName() + "Search/SearchResults3?=true&current_tbl_nm=" + table_in_query + "&serachType=parent_id&searchvalue=" + REPLACEWITH_ID_FALG + "\">Get Children</a>";
                            }
                            linkExpand_map.put(i, query_link);
                        } else if (getIgnoreCase(qr_column_name_l, column_nm_u) != null) {
                            qr_list.add(i);
                        } else if (column_nm_u.equals(Constants.LINK_TO_FEATURE_FLAG)) {
                            link_to_feature_list.add(i);
                        }
                    }
                }
                if (isTagSource) {
                    resultsDisplay_column_nm_l.add("");
                }
                ArrayList<Integer> found_id_list = new ArrayList<>();
                ArrayList<String> found_hash_list = new ArrayList<>();
                int count = 0;
                while (r_1.next() && (count <= max_limit_to_read_from_db)) {
                    count++;
                    ArrayList<String> tmp_l = new ArrayList<>(NumOfCol + 1);
                    int lastid = id_column_pos;
                    for (int i = 1; i < (NumOfCol + 1); i++) {
                        if (dont_show_s.contains(i)) {
                        } else {

                            if (i == id_column_pos) {
                                String id_string = r_1.getString(i);
                                if (id_string != null && id_string.matches("[0-9]+")) {
                                    lastid = new Integer(id_string);
                                    found_id_list.add(lastid);
                                }
                            }
                            if (rsmd.getColumnName(i).toUpperCase().equals("HASH")) {
                                found_hash_list.add(r_1.getString(i));
                                if (isTagSource) {
                                    tag_link = table_in_query + "|HASH=" + r_1.getString(i);
                                }
                            } else if (isTagSource) {
                                if (rsmd.getColumnName(i).toUpperCase().equals("NAME")) {
                                    tag_name = r_1.getString(i);
                                } else if (rsmd.getColumnName(i).toUpperCase().equals("URI")) {
                                    tag_uri = r_1.getString(i);
                                }
                            }

                            if (linkExpand_map.containsKey(i)) {
                                String id_string = r_1.getString(i);
                                if (id_string != null) {
                                    String transformed_url = linkExpand_map.get(i).replaceAll(REPLACEWITH_ID_FALG, id_string);
                                    tmp_l.add(transformed_url);
                                } else {
                                    tmp_l.add(id_string);
                                }
                            } else if (special_foreign_keys_list.contains(i)) {
                                String link_query = "Error";
                                try {
                                    String valp = r_1.getString(i);
                                    if (valp != null && !valp.isEmpty()) {
                                        valp = valp.replaceAll("&#61;", "=");
                                        String[] split = valp.split("=");
                                        if (split.length == 2) {
                                            link_query = URLEncoder.encode("SELECT * FROM "
                                                    + Constants.get_correct_table_name(c_con, split[0]) + " WHERE id=" + split[1], "ISO-8859-1");
//                                        link_query = "<a href=\"" + Constants.getServerName() + "Search/SearchResults3?query=" + link_query + "&reload=true&previous_tbl_nm=" + previous_tbl_nm + "&current_tbl_nm=" + split[0] + "\">Link to " + split[0] + "</a>";
                                        }
                                    }
                                } catch (Exception ex) {
                                    ex.printStackTrace();
                                }
                                tmp_l.add(link_query);
                            } else if (timestamp_list.contains(i)) {
                                if (restrict_last_modified_inf) {
                                    tmp_l.add("Hidden");
                                } else {
                                    String valp = "NA";
                                    String orig = "";
                                    try {
                                        valp = r_1.getString(i);
                                        orig = valp;
                                    } catch (Exception ex) {
                                    }
                                    if (valp != null) {
                                        if (valp.matches("[0-9+]")) {
                                            Long ts = new Long(valp);
                                            valp = Constants.getTimeStampFormat().format(new java.util.Date((ts) * 1000));
                                        }
                                    }
                                    tmp_l.add(valp);
                                }
                            } else if (qr_list.contains(i)) {
                                if (make_qr) {
                                    String qr_url = Constants.getServerName(c_con) + "Search/SearchResults3?" + Constants.TABLE_TO_USE_FLAG
                                            + table_in_query + "=(" + lastid + ")";
                                    String qrimage_url = makeQR(c_con, qr_url);
                                    if (qrimage_url == null) {
                                        qrimage_url = "QR generation failed";
                                    } else {
                                        qrimage_url = "<a href='" + qrimage_url + "'><img  height='50' width='50' alt='QR' border='0' src='" + qrimage_url + "'/></a>";
                                    }
                                    tmp_l.add(qrimage_url);
                                } else {
                                    tmp_l.add("Use the link <i>\"URL for this result\"</i>\" above,  to display with QR code");
                                }

                            } else if (link_to_feature_list.contains(i)) {
                                String valp = r_1.getString(i);
                                if (valp != null && valp.contains("=")) {
                                    String[] valp_a = valp.split("=", 2);
//                                  valp = "<a href=\"" + Constants.getServerName(c_con) + "Search/SearchResults3?"
//                                        + Constants.TABLE_TO_USE_FLAG + valp_a[0] + "=" + valp_a[1] + "\">" + valp + "</a>  ");
                                    valp = " <a style=\"background-color: rgb(50, 50, 153); color: white;\n"
                                            + "display: block; height: 50%; line-height: 20px; text-decoration:\n"
                                            + "none; width: 500px; text-align: center;\" target=\"_blank\" href=\"" + Constants.getServerName(c_con)
                                            + "Search/SearchResults3?" + Constants.TABLE_TO_USE_FLAG + valp_a[0] + "=" + valp_a[1] + "\">"
                                            + " Details in " + valp_a[0] + " </a> "
                                            + "<a style=\"background-color: rgb(50, 135, 50); color: white;\n"
                                            + "display: block; height: 50%; line-height: 20px; text-decoration:\n"
                                            + "none; width: 500px; text-align: center;\" target=\"_blank\" href=\"" + Constants.getServerName(c_con)
                                            + "/visualizer?hashurl=" + valp + "\">"
                                            + " Diagram </a> "
                                            + ""
                                            + ""
                                            + "";
//                                valp ="<a onclick=\"$('#details_pos').load('"+Constants.getServerName(c_con) + "Search/SearchResults3?"
//                                        + Constants.TABLE_TO_USE_FLAG + valp_a[0] + "=" + valp_a[1] + " #data_tbl')\" "
//                                        + "style=\"cursor:pointer;\" "
//                                        + "onmouseover=\"this.style.textDecoration = 'underline';\" "
//                                        + "onmouseout=\"this.style.textDecoration = 'none';\" >" + valp + "</a>";
                                    tmp_l.add(valp);
                                } else {
                                    tmp_l.add("<error>Link error</error>");
                                }
                            } else {
                                String valp = null;
                                try {
                                    valp = r_1.getString(i);
                                } catch (Exception ex) {
                                }
                                if (valp != null) {
                                    String val_tmp = valp;
                                    if (val_tmp.startsWith(Constants.SCP_URL_FLAG)) {
//                                    valp =  valp;
                                    } else if (val_tmp.toLowerCase().startsWith("http://") || val_tmp.toLowerCase().startsWith("https://")) {
                                        valp = "<a href=\"" + valp + "\">" + valp + " </a>";
                                    } else if (val_tmp.toLowerCase().startsWith("ftp://") || val_tmp.toLowerCase().startsWith("ftps://")) {
                                        valp = "<a href=\"" + valp + "\">" + valp + " </a>";
                                    }
                                    if (restricted_display) {
                                        if (restric_column_l.contains(i)) {
                                            valp = getDsiplayText(server_mode, user_level, valp);
                                        }
                                    }
                                }
                                tmp_l.add(valp);
                            }
                        }
                    }
                    if (isTagSource) {
                        StringBuilder html = new StringBuilder();         //oprniInNew(url)    
//                        html.append("<a style=\"background-color: #0174DF; color: white;\n"
//                                + "display: block; height: 20px; line-height: 20px; text-decoration:\n"
//                                + "none; width: 300px; cursor:pointer;\" onmouseover=\"this.style.textDecoration = 'underline';\" onmouseout=\"this.style.textDecoration = 'none';\" "
//                                + "onclick=\"setVisible('search_results','" + Constants.getServerName(c_con) + "/visualizer');\"  >"
//                                + " Show/Hide search results </a>");
                        html.append("<a style=\"background-color: rgb(50, 135, 50); color: white;\n"
                                + "display: block; height: 50%; line-height: 20px; text-decoration:\n"
                                + "none; width: 80%; text-align: center;\" target=\"_blank\" href=\"" + Constants.getServerName(c_con)
                                + "/visualizer?hashurl=" + tag_link + "\">"
                                + " Diagram <a>");
//                             html.append("<a style=\"background-color: rgb(50, 135, 50); color: white;\n"
//                                                + "display: block; height: 50%; line-height: 20px; text-decoration:\n"
//                                                + "none; width: 80%; text-align: center;\" onclick=\"setVisible('search_results','" + Constants.getServerName(c_con) + "/visualizer?hashurl=" + tag_link + " #grap_loc');\\\" >"
//                                                +" _View graph_ </a>");
                        //"onclick=\"setVisible('search_results','" + Constants.getServerName(c_con) + "Search/search?data_height=200 #data');\" 
                        html.append("<p><a href=\"" + Constants.getServerName(c_con) + "Upload/CreateNew?resethistory=true&current_tbl_nm="
                                + table_in_query + "&value_returned_to_col=parent_id&" + table_in_query + "=" + lastid + "\">Create_child</a></p>");
                        if (tag_link != null && tag_name != null) {
                            html.append("<p>Tag_with_this (");
                            html.append("<a href=\"" + Constants.getServerName(c_con) + "Upload/CreateNew?resethistory=true&current_tbl_nm=FILES2TAGS"
                                    + "&value_returned_to_col=LINK_TO_FEATURE&LINK_TO_FEATURE=" + tag_link + "&name=" + tag_name + "&uri=" + tag_uri + "\">File</a>");
                            html.append("|");

                            html.append("<a href=\"" + Constants.getServerName(c_con) + "Upload/CreateNew?resethistory=true&current_tbl_nm=PERSON2TAGS"
                                    + "&value_returned_to_col=LINK_TO_FEATURE&LINK_TO_FEATURE=" + tag_link + "&name=" + tag_name + "&uri=" + tag_uri + "\">Person</a>");
                            html.append("|");
                            html.append("<a href=\"" + Constants.getServerName(c_con) + "Upload/CreateNew?resethistory=true&current_tbl_nm=REPORT2TAGS"
                                    + "&value_returned_to_col=LINK_TO_FEATURE&LINK_TO_FEATURE=" + tag_link + "&name=" + tag_name + "&uri=" + tag_uri + "\">Report</a>");
                            html.append("|");
                            html.append("<a href=\"" + Constants.getServerName(c_con) + "Upload/CreateNew?resethistory=true&current_tbl_nm=SAMPLEDETAILS2TAGS"
                                    + "&value_returned_to_col=LINK_TO_FEATURE&LINK_TO_FEATURE=" + tag_link + "&name=" + tag_name + "&uri=" + tag_uri + "\">Sample</a>");
                            html.append(")</p>");
                        }
                        tmp_l.add(html.toString());
//                        tmp_l.add("<a href=\"" + Constants.getServerName(c_con) + "Upload/CreateNew?resethistory=true&current_tbl_nm="
//                                + table_in_query + "&value_returned_to_col=parent_id&" + table_in_query + "=" + lastid + "\">Create_child</<a>");
                    }
                    resultsDisplay_data_l.add(tmp_l);
                    ArrayList<String> blank_l = new ArrayList<>(NumOfCol);
                    blank_l.add(DETAILS_FLAG + lastid + IDFORDIV_SPLIT_FLAG);
                    resultsDisplay_data_l.add(blank_l);
                }

                String loaded_encript_name = "1=<a href=\"" + split_link_prefix + table_in_query + "="
                        + makeSTATIC_URL(c_con, found_id_list, table_in_query, Constants.getDocRoot(c_con)) + "&page=1&operation=" + operation + "\">";
                split_links_l.add(loaded_encript_name);
                HashMap<Integer, String> id2expandDetails_map = new HashMap<>();
                id_list_l.addAll(found_id_list);
                ArrayList<String> expand_query_l = null;
//                System.out.println("1156 "+found_id_list+" "+queryExpander_map.containsKey(table_in_query)+" table_in_query="+table_in_query+"\n\t "+queryExpander_map.keySet());
                if (!found_id_list.isEmpty()) {
                    expand_query_l = getIgnoreCase2(queryExpander_map, table_in_query);
                }
//                System.out.println("1160 "+ table_in_query+"\t"+expand_query_l);
                if (expand_query_l != null) {
                    id2expandDetails_map.putAll(getExpanstion(c_con, expand_query_l, found_id_list, found_hash_list));
                    fromDbMap.put("id2expandDetails_map", id2expandDetails_map);
                } else {
                    fromDbMap.put("id2expandDetails_map", new HashMap<Integer, String>());

                }
                fromDbMap.put("current_tbl_nm", table_in_query);

                ArrayList<Integer> id_l = new ArrayList<>(max_limit_to_read_from_db);
                int split_count = 1;
                while (r_1.next()) {
                    String id_string = r_1.getString(id_column_pos);
                    if (id_string != null && id_string.matches("[0-9]+")) {
                        id_l.add(new Integer(id_string));
                    }
                    if (id_l.size() > max_limit_to_read_from_db) {
                        split_count++;
                        String encript_name = split_count + "=<a href=\"" + split_link_prefix + table_in_query + "="
                                + makeSTATIC_URL(c_con, id_l, table_in_query, Constants.getDocRoot(c_con)) + "&page=" + split_count + "&operation=" + operation + "\">";
                        split_links_l.add(encript_name);
                        id_l = new ArrayList<>(max_limit_to_read_from_db);
                    }
                }
                if (!id_l.isEmpty()) {
                    String encript_name = (split_count + 1) + "=<a href=\"" + split_link_prefix + table_in_query + "="
                            + makeSTATIC_URL(c_con, id_l, table_in_query, Constants.getDocRoot(c_con)) + "&page=" + (split_count + 1) + "\">";
                    split_links_l.add(encript_name);
                }
                r_1.close();
            } else {
                System.out.println("failed to establish connection or the query was null");
            }

            fromDbMap.put("split_links_l", split_links_l);
            fromDbMap.put("resultsDisplay_column_nm_l", resultsDisplay_column_nm_l);
            fromDbMap.put("resultsDisplay_data_l", resultsDisplay_data_l);
            String id_list = id_list_l.toString().replace("[", "").replace("]", "").replaceAll("\\s", "");
            fromDbMap.put("id_list", id_list);
            if (!id_list_l.isEmpty() && table_in_query != null) {
                String static_url_file_nm = makeSTATIC_URL(c_con, id_list_l, table_in_query, Constants.getDocRoot(c_con));
                String base_url = Constants.getServerName(c_con) + "Search/SearchResults3?" + Constants.TABLE_TO_USE_FLAG + table_in_query + "=" + static_url_file_nm;
                String qrimage_url = makeQR(c_con, base_url);
                String image_url = makeFlowImage(c_con, id_list, current_tbl_nm, Constants.getDocRoot(c_con));

                if (qrimage_url == null) {
                    qrimage_url = "QR generation failed";
                } else {
                    qrimage_url = "<a href='" + qrimage_url + "'><img  height='50' width='50' alt='QR' border='0' src='" + qrimage_url + "'/></a>";
                }
                fromDbMap.put("QR", qrimage_url);
                fromDbMap.put("GRAPH", image_url);
                fromDbMap.put("STATIC_URL", "URL for this page: <a href='" + base_url + "'>" + static_url_file_nm + "<a>");

            }
        } catch (SQLException ex) {
            System.out.println("Error SR1a: " + ex.getMessage());
            getDataFromDB(c_con, "Select message from " + Constants.getErrorMessageTable(c_con) + " where id=4", Constants.SERCH_OP_FLAG, split_link_prefix);
        } finally {
            try {
                if (r_1 != null && !r_1.isClosed()) {
                    r_1.close();
                }
                if (st_1 != null && !st_1.isClosed()) {
                    st_1.close();
                }
            } catch (SQLException ex) {
            }
        }
    }

    private ArrayList<Integer> getIds(Connection c_con, String query) {
        ArrayList<Integer> result_l = new ArrayList<>();
        Statement st_1 = null;
        ResultSet r_1 = null;
        try {
            if (!c_con.isClosed()) {
                st_1 = c_con.createStatement();
                r_1 = st_1.executeQuery(query);
                while (r_1.next()) {
                    result_l.add(r_1.getInt(1));
                }
            }
            close(null, st_1, r_1);
        } catch (Exception ex) {
            result_l.add(-1);
        } finally {
            close(null, st_1, r_1);
        }
        return result_l;
    }

    /*
     MethodID GFDB5
     * Use the auto increament id to find where else the entitiy is used and what else is linked to. 
     * @c_con: the current connection object used 
     * @expand_query: All the precalculated quary expansions (e.g. using foriegn 
     * @ found_id_linst: The autoincrement ids found
     */
    private HashMap<Integer, String> getExpanstion(Connection c_con, ArrayList<String> expand_query_l,
            ArrayList<Integer> found_id_list, ArrayList<String> found_hash_list) {
//        System.out.println("1251 found_id_list=" + found_id_list + " " + found_hash_list);
        /*The map to keep the Id to details mapping, the details is a HTML table*/
        HashMap<Integer, String> id2expandDetails_map = new HashMap<>();

        ResultSet r_1 = null;
        Statement stm_1 = null;
        ArrayList<String> path_l = new ArrayList<>(expand_query_l.size());
        /*restrinct what is displayed accoding to user previledges*/
        boolean restricted_display = false;
        if (user_level > 1) {
            restricted_display = true;
        }
        try {
            if (!c_con.isClosed()) {
                if (!found_id_list.isEmpty()) {
                    ArrayList<String> set_in_query_l = new ArrayList<>(expand_query_l.size());
                    ArrayList<String> sql_l = new ArrayList<>(expand_query_l.size());
                    /*Sortout the type of expantion */
                    for (int i = 0; i < expand_query_l.size(); i++) {
                        String c_string = expand_query_l.get(i);
                        c_string = URLDecoder.decode(c_string, "ISO-8859-1");
                        if (c_string.contains(IN_ID_FALG)) {
                            set_in_query_l.add(c_string);
                        } else if (c_string.contains(REPLACEWITH_ID_FALG)) {// use foreign keys or polymorphic relationships
                            path_l.add(c_string);
                        } else if (c_string.contains(Constants.LINK_TO_FEATURE_ID_FLAG)) {// use the tag to link to the tagsource
                            sql_l.add(c_string);
                        }
                    }
                    if (!set_in_query_l.isEmpty() || !sql_l.isEmpty()) {
                        stm_1 = c_con.createStatement();
                        for (int i = 0; i < found_hash_list.size(); i++) {
                            StringBuilder str2 = new StringBuilder();
                            str2.append("<table class=\"table2\"  border=\"1\">");
                            str2.append("<tr>");
                            str2.append("<th colspan=\"2\">");
                            str2.append(" Tag usage");
                            str2.append("</th>");
                            str2.append("</tr>");
                            str2.append("<tr><td>"); //<td>Details</td>
                            boolean used = false;
                            int blue = 255;
                            for (int j = 0; j < sql_l.size(); j++) {
                                String[] sql_a = sql_l.get(j).split("\\|", 2);
                                if (sql_a.length == 2) {
                                    blue = blue - (j * 10);
                                    if (blue < 0) {
                                        blue = 0;
                                    }
                                    String sql_1 = sql_a[0];
                                    String prefix = sql_a[1];
                                    String sql_link = sql_1.replaceAll(Constants.LINK_TO_FEATURE_ID_FLAG, "'" + prefix + found_hash_list.get(i) + "'");
//                                    System.out.println("1385 "+sql_link);
                                    ResultSet r_link = stm_1.executeQuery(sql_link);
                                    Set<Integer> id_s = new HashSet<>();
                                    String link_tbl = r_link.getMetaData().getTableName(1);
                                    while (r_link.next()) {
                                        id_s.add(r_link.getInt(1));
                                    }
                                    if (!id_s.isEmpty()) {
                                        String encript_name = makeSTATIC_URL(c_con, id_s, link_tbl, Constants.getDocRoot(c_con));
                                        str2.append("<a style=\"background-color: rgb(46, 135, " + blue + "); color: white;\n"
                                                + "display: block; height: 50%; line-height: 20px; text-decoration:\n"
                                                + "none; width: 20%; text-align: center;\" href=\"" + Constants.getServerName(c_con)
                                                + "Search/SearchResults3?" + Constants.TABLE_TO_USE_FLAG + link_tbl + "="
                                                + encript_name + "&reset=true&operation=" + Constants.SERCH_OP_FLAG + "\">"
                                                + link_tbl + "<a><br>");

                                        used = true;
                                    }
                                }
                            }
                            if (!used) {
                                str2.append("This tag is not used");
                            }
                            str2.append("</td></tr>");
//                            str2.append("<tr><td>Tag usage</td><td><a href=\"" + Constants.getServerName(c_con)
//                                    + "Search/SearchResults3?" + Constants.TABLE_TO_USE_FLAG + link_tbl + "="
//                                    + encript_name + "&reset=true&operation=" + Constants.SERCH_OP_FLAG + "\">"
//                                    + " This tag is used -get more details<a></td></tr>");
                            str2.append("</table>" + EXPANSION_SPLIT_TOKEN);
                            id2expandDetails_map.put(found_id_list.get(i), str2.toString());
                        }
                        for (int j = 0; j < set_in_query_l.size(); j++) {
                            String c_q = set_in_query_l.get(j);
                            c_q = c_q.replace(IN_ID_FALG, found_id_list.toString().replace("[", "").replace("]", ""));
                            r_1 = stm_1.executeQuery(c_q);
                            ResultSetMetaData rsmd = r_1.getMetaData();
                            int NumOfCol = rsmd.getColumnCount();
                            String c_table = null;
                            int id_col_pos = -1;
                            int ref_id_col_pos = -1;
                            for (int k = 1; k < (NumOfCol + 1); k++) {
                                if (rsmd.getColumnName(k).equalsIgnoreCase("id")) {
                                    id_col_pos = k;
                                } else if (rsmd.getColumnName(k).equalsIgnoreCase("ref_id")) {
                                    ref_id_col_pos = k;
                                }
                            }
                            if (NumOfCol > 0) {
                                c_table = rsmd.getTableName(1);
                                if (restricted_display) {
                                    if (c_table.toUpperCase().equals("FILES") || c_table.toUpperCase().equals("FILES2PATH")) {
                                        restricted_display = true;
                                    } else {
                                        restricted_display = false;
                                    }
                                }
                            }
                            boolean link_graph = false;
                            if (c_table.toUpperCase().equals("REPORT") || c_table.toUpperCase().endsWith("2TAGS")) {
                                link_graph = true;
                            }

                            HashMap<Integer, HashMap<String, ArrayList<String>>> details_map = new HashMap<>();

                            if (id_col_pos > 0 && ref_id_col_pos > 0) {
//                                boolean morethan_sanctioned_added = false;
//                                boolean limit_exceeded = false;
                                HashSet<Integer> limit_exceeded_ref_s = new HashSet<>();
                                while (r_1.next()) {
                                    int ref_id = r_1.getInt(ref_id_col_pos);
                                    int id = r_1.getInt(id_col_pos);
                                    if (!details_map.containsKey(ref_id)) {
                                        HashMap<String, ArrayList<String>> tmp_m = new HashMap<>();
                                        tmp_m.put("ID", new ArrayList<String>());
                                        tmp_m.put("NAME", new ArrayList<String>());
                                        details_map.put(ref_id, tmp_m);
                                    }
                                    details_map.get(ref_id).get("ID").add(id + "");
                                    if (!limit_exceeded_ref_s.contains(ref_id)) {
                                        if (details_map.get(ref_id).get("NAME").size() < 10) {
                                            for (int k = 1; k < (NumOfCol + 1); k++) {
                                                if (rsmd.getColumnName(k).equalsIgnoreCase("NAME")) {
                                                    String name = r_1.getString(k);
                                                    details_map.get(ref_id).get("NAME").add(name);
                                                }
                                            }
                                        } else {
//                                            if (!morethan_sanctioned_added) {
//                                            morethan_sanctioned_added = true;
                                            details_map.get(ref_id).get("NAME").add("_TRUNCATE_");
//                                            limit_exceeded = true;
                                            limit_exceeded_ref_s.add(ref_id);
                                        }
                                    }
                                }
//                                System.out.println("1520 morethan_sanctioned_added="+morethan_sanctioned_added);
                                if (!r_1.isClosed()) {
                                    r_1.close();
                                }
                            }
//                            else {
//                                send a warnings
//                            }

                            ArrayList<Integer> details_id_l = new ArrayList<>(details_map.keySet());
                            for (int i = 0; i < details_id_l.size(); i++) {
                                StringBuilder str = new StringBuilder();
                                StringBuilder str_connections = new StringBuilder();
                                if (str_connections.length() == 0 && !path_l.isEmpty()) {
                                    str_connections.append("<table class=\"table2\"  border=\"1\">");
                                    str_connections.append("<tr>");
                                    str_connections.append("<th colspan=\"2\">");
                                    str_connections.append("Get connections");
                                    str_connections.append("</th>");
                                    str_connections.append("</tr>");
                                    for (int k = 0; k < path_l.size(); k++) {
                                        String c_path_q = path_l.get(k).replace(REPLACEWITH_ID_FALG, details_id_l.get(i) + "");
                                        String[] c_path_s = c_path_q.split("\\|");
                                        if (c_path_s.length == 2) {
                                            String table_nm_print = c_path_s[0].split("\\.")[c_path_s[0].split("\\.").length - 1];
                                            String query_link = makeQuery_URL(c_con, c_path_s[1], c_path_s[0], Constants.getDocRoot(c_con));
                                            String expan_out_str = "<tr><td>Relevant " + table_nm_print + "</td><td><a target=\"_blank\" title=\"Opens in new Window\" href=\""
                                                    + Constants.getServerName(c_con) + "Search/SearchResults3?" + Constants.QUERY_TO_USE_FLAG + c_path_s[0] + "=" + query_link
                                                    + "&reset=true&operation=" + Constants.SERCH_OP_FLAG + "&current_tbl_nm=" + table_nm_print + "\">Find</a></td></tr>";
                                            str_connections.append(expan_out_str);
                                        }
                                    }
                                    str_connections.append("</table>" + EXPANSION_SPLIT_TOKEN);
                                }

//                                if (details_map.containsKey(details_id_l.get(i))) {
                                HashMap<String, ArrayList<String>> c_details_map = details_map.get(details_id_l.get(i));
                                StringBuilder expan_out_str = new StringBuilder();
                                ArrayList<String> id_l = c_details_map.get("ID");
                                String encript_name = null;
                                if (id_l != null && !id_l.isEmpty()) {
                                    encript_name = makeSTATIC_URL(c_con, id_l, c_table, Constants.getDocRoot(c_con), false);
                                    expan_out_str.append("<tr><td>Load all (" + id_l.size() + ")</td><td><a href=\"" + Constants.getServerName(c_con) + "Search/SearchResults3?"
                                            + Constants.TABLE_TO_USE_FLAG + c_table + "=" + encript_name + "&reset=true&operation=" + Constants.SERCH_OP_FLAG + "\">"
                                            + c_table + "</a></td></tr>");
                                }
                                ArrayList<String> name_l = c_details_map.get("NAME");
                                if (name_l != null && !name_l.isEmpty()) {
                                    for (int k = 0; k < name_l.size(); k++) {
                                        String c_name = name_l.get(k);
                                        if (c_name.equals("_TRUNCATE_")) {
                                            expan_out_str.append("<tr>");
                                            expan_out_str.append("<td>..more avaialble</td>");
                                            expan_out_str.append("<td><a href=\"" + Constants.getServerName(c_con) + "Search/SearchResults3?" + Constants.TABLE_TO_USE_FLAG
                                                    + c_table + "=" + encript_name + "&reset=true&operation=" + Constants.SERCH_OP_FLAG + "\">Load all</a></td>");
                                            expan_out_str.append("</tr>");
                                        } else {
                                            expan_out_str.append("<tr>");
                                            if (restricted_display) {
                                                expan_out_str.append("<td>" + getDsiplayText(server_mode, user_level, name_l.get(k)) + "</td>");
                                            } else {
                                                expan_out_str.append("<td>" + name_l.get(k) + "</td>");
                                            }
                                            if (link_graph) {
                                                expan_out_str.append("<td><a href=\"" + Constants.getServerName(c_con) + "Search/SearchResults3?"
                                                        + Constants.TABLE_TO_USE_FLAG + c_table + "=" + id_l.get(k) + "&reset=true&operation=" + Constants.SERCH_OP_FLAG + "\">Details</a>/"
                                                        + "<a target=\"_blank\" href=\"" + Constants.getServerName(c_con) + "/visualizer?hashurl=" + c_table + "|ID=" + id_l.get(k) + "\">"
                                                        + " Diagram </a>  </td>");
                                            } else {
                                                expan_out_str.append("<td><a href=\"" + Constants.getServerName(c_con) + "Search/SearchResults3?"
                                                        + Constants.TABLE_TO_USE_FLAG + c_table + "=" + id_l.get(k) + "&reset=true&operation=" + Constants.SERCH_OP_FLAG + "\">Details</a></td>");
                                            }

                                            expan_out_str.append("</tr>");
                                        }

                                    }
                                }
                                str.append("<table class=\"table2\"  border=\"1\">");
                                str.append("<tr>");
                                str.append("<th colspan=\"2\">");
                                str.append("Details for " + c_table + " ");
                                str.append("</th>");
                                str.append("</tr>");
                                str.append(expan_out_str);
                                str.append("</table>" + EXPANSION_SPLIT_TOKEN);
//                                }

                                if (id2expandDetails_map.containsKey(details_id_l.get(i))) {
                                    str.insert(0, id2expandDetails_map.get(details_id_l.get(i)));
                                    id2expandDetails_map.put(details_id_l.get(i), str.toString());
                                } else {
                                    str_connections.append(str);
                                    id2expandDetails_map.put(details_id_l.get(i), str_connections.toString());
                                }
                                str.setLength(0);
                            }
                        }

                    }

                }
            }

            if (r_1 != null && !r_1.isClosed()) {
                r_1.close();
            }
            if (stm_1 != null && !stm_1.isClosed()) {
                stm_1.close();
            }

        } catch (SQLException ex) {
            System.out.println("Error GD1b: " + ex.getMessage());
        } catch (Exception ex) {
            System.out.println("Error GD1c: " + ex.getMessage());
        } finally {
            close(null, stm_1, r_1);
        }
        return id2expandDetails_map;
    }

    private String getDsiplayText(int server_mode, int user_level, String intext) {
        String out = "";
        if (user_level == 0) {
            out = intext;
        } else if (user_level <= 2) {
            int half = intext.length() / 2;
            out = "Hidden..." + intext.substring(half, intext.length());
        } else {
            out = "Hidden";
        }
        return out;
    }

    private String getIgnoreCase(HashMap<String, String> in_map, String key) {
        if (in_map.containsKey(key)) {
            return in_map.get(key);
        } else {
            key = key.trim();
            String val = null;
            ArrayList<String> key_l = new ArrayList<>(in_map.keySet());
            for (int i = 0; ((i < key_l.size()) && val == null); i++) {
                if (key_l.get(i).equalsIgnoreCase(key)) {
                    val = in_map.get(key_l.get(i));
                }
            }
            return val;
        }
    }

    private String getIgnoreCase(ArrayList<String> key_l, String key) {
        String realKey = null;
        if (key_l != null) {
            for (int i = 0; (i < key_l.size() && realKey == null); i++) {
                if (key_l.get(i).equalsIgnoreCase(key)) {
                    realKey = key_l.get(i);
                }
            }
        }

        return realKey;
    }

    private ArrayList<String> getIgnoreCase2(HashMap<String, ArrayList<String>> in_map, String key) {
        if (in_map != null) {
            if (in_map.containsKey(key)) {
                return in_map.get(key);
            } else {
                key = key.trim();
                ArrayList<String> val = null;
                ArrayList<String> key_l = new ArrayList<>(in_map.keySet());
                for (int i = 0; ((i < key_l.size()) && val == null); i++) {
                    if (key_l.get(i).equalsIgnoreCase(key)) {
                        val = in_map.get(key_l.get(i));
                    }
                }
                return val;
            }
        }
        return null;
    }

    public String makeQR(Connection c_con, String texttoencode) { //, String doc_root, int caller

        String url = null;
        File file = new File(Constants.getDocRoot(c_con) + Constants.encript(texttoencode) + ".png");
        //       File  file = new File(file.getAbsoluteFile().getParentFile().getParent() + File.separatorChar + "docroot" + File.separatorChar + session_id);

        if (file.isFile()) {
            url = Constants.getServerRoot(c_con) + file.getName();
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
                    HashMap<EncodeHintType, String> hints = new HashMap<>(2); // was  Hashtable
                    hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
                    matrix = writer.encode(data, com.google.zxing.BarcodeFormat.QR_CODE, w, h, hints);
                } catch (com.google.zxing.WriterException e) {
                    System.out.println(e.getMessage());
                }
                try {
                    if (matrix != null) {
                        MatrixToImageWriter.writeToFile(matrix, "PNG", file);
                        url = Constants.getServerRoot(c_con) + file.getName();
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
        return url;
    }

    public String makeQuery_URL(Connection c_con, String query, String current_tbl_nm, String doc_root) {
        String encripted_nm = Constants.makeQUERY_URL(query, current_tbl_nm);
        File file = new File(doc_root + encripted_nm);
        if (file.isFile()) {
        } else {
            writeResultsToFile(current_tbl_nm + "||" + query, Constants.getDocRoot(c_con) + encripted_nm);
        }
        return encripted_nm;
    }

    public String makeSTATIC_URL(Connection c_con, ArrayList<Integer> id_l, String current_tbl_nm, String doc_root) {
        String encripted_nm = Constants.makeSTATIC_URL(id_l, current_tbl_nm);
        File file = new File(doc_root + encripted_nm);
        if (file.isFile()) {
        } else {
            writeResultsToFile(current_tbl_nm + "||" + id_l.toString().replaceAll("[^0-9,]", ""), Constants.getDocRoot(c_con) + encripted_nm);
        }
        return encripted_nm;
    }

    public String makeSTATIC_URL(Connection c_con, Set<Integer> id_l, String current_tbl_nm, String doc_root) {
        String encripted_nm = Constants.makeSTATIC_URL(id_l, current_tbl_nm);
        File file = new File(doc_root + encripted_nm);
        if (file.isFile()) {
        } else {
            writeResultsToFile(current_tbl_nm + "||" + id_l.toString().replaceAll("[^0-9,]", ""), Constants.getDocRoot(c_con) + encripted_nm);
        }
        return encripted_nm;
    }

    public String makeSTATIC_URL(Connection c_con, ArrayList<String> id_l, String current_tbl_nm, String doc_root, boolean is_int) {
        String encripted_nm = Constants.makeSTATIC_URL(id_l, current_tbl_nm, false);
        File file = new File(doc_root + encripted_nm);
        if (file.isFile()) {
        } else {
            writeResultsToFile(current_tbl_nm + "||" + id_l.toString().replaceAll("[^0-9,]", ""), Constants.getDocRoot(c_con) + encripted_nm);
        }
        return encripted_nm;
    }

    public String makeFlowImage(Connection c_con, String chkbx_list_string, String table_nm, String doc_root) {
        File file = new File(doc_root + Constants.encript(table_nm + "_" + chkbx_list_string));
        String files_tbl_nm = Constants.get_correct_table_name(c_con, "files");
        String files2report_tble_nm = Constants.get_correct_table_name(c_con, "files2report");
        String url = null;
        Statement stm_1 = null;
        ResultSet r_1 = null;
        if (file.isFile()) {
            url = Constants.getServerName(c_con) + "Search/visualizer?" + file.getName();
        } else {
            String report_batch_tbl = Constants.get_correct_table_name(c_con, "report_batch");
            String report_tbl = Constants.get_correct_table_name(c_con, "report");
            String r_s = " select report_id,(select name from " + Constants.get_correct_table_name(c_con, "report") + " where id=report_id) as repnm, (select name from " + files_tbl_nm + " where id=files_id) as filesnm  from " + files2report_tble_nm + " where files_id in (" + chkbx_list_string + ")";
            StringBuilder str = new StringBuilder();
            str.append("<DATA>");
            try {
                if (c_con != null && !c_con.isClosed() && report_batch_tbl != null && report_tbl != null) {
                    stm_1 = c_con.createStatement();
                    r_1 = stm_1.executeQuery(r_s);
                    HashMap<String, ArrayList<String>> report_map = new HashMap<>();
                    HashMap<Integer, ArrayList<String>> report_id_map = new HashMap<>();
                    HashMap<String, ArrayList<String>> report_batch_map = new HashMap<>();
                    while (r_1.next()) {
                        String c_repo = r_1.getString("repnm");
                        if (!report_map.containsKey(c_repo)) {
                            report_map.put(c_repo, new ArrayList<String>(1));
                        }
                        int rc_repo_id = r_1.getInt("report_id");
                        if (!report_id_map.containsKey(rc_repo_id)) {
                            report_id_map.put(rc_repo_id, new ArrayList<String>(1));
                        }
                        String c_v = "<r>" + r_1.getString("filesnm") + "||" + c_repo + "||''</r>";
                        str.append(c_v);
                    }
                    r_1.close();
                    ArrayList<String> repo_l = new ArrayList<>(report_map.keySet());
                    ArrayList<Integer> repo_id_l = new ArrayList<>(report_id_map.keySet());
                    if (!repo_l.isEmpty()) {
                        String rb_s = " select name as repnm, (select name from " + report_batch_tbl + "  where id=report_batch_id) as batchnm from " + report_tbl + " where id in (" + repo_id_l.toString().replace("[", "").replace("]", "") + ")";;
                        Statement stm_2 = c_con.createStatement();
                        ResultSet r_2 = stm_2.executeQuery(rb_s);
                        while (r_2.next()) {
                            String c_repo_batch = "batch_" + r_2.getString("batchnm");
                            String c_v = "<r>" + r_2.getString("repnm") + "||" + c_repo_batch + "||''</r>";
                            if (report_batch_map.containsKey(c_repo_batch)) {
                                report_batch_map.put(c_repo_batch, new ArrayList<String>(1));
                            }
                            str.append(c_v);
                        }
                        r_2.close();
                        for (int i = 0; i < repo_l.size(); i++) {
                            String c_v = "<r>" + repo_l.get(i) + "||''||''</r>";
                            str.append(c_v);

                        }
                        ArrayList<String> batch_l = new ArrayList<>(report_batch_map.keySet());
                        for (int i = 0; i < batch_l.size(); i++) {
                            String c_v = "<r>" + batch_l.get(i) + "||''||''</r>";
                            str.append(c_v);
                        }
                    }
                    if (stm_1 != null && !stm_1.isClosed()) {
                        stm_1.close();
                    }
                    if (r_1 != null && !r_1.isClosed()) {
                        r_1.close();
                    }
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            str.append("</DATA>");
            FileWriter out = null;
            String data = str.toString();//"<DATA><r>A||''||''</r><r>B||A||''</r><r>C||A||''</r><r>D||B||''<r></r>E||A||''</r></DATA>";
            try {
                out = new FileWriter(doc_root + file.getName());
                out.append(data);
                out.close();
                url = Constants.getServerName(c_con) + "Search/visualizer?" + file.getName();
            } catch (IOException ex) {
                ex.printStackTrace();
            } finally {
                try {
                    out.close();
                    close(null, stm_1, r_1);
                } catch (IOException ex) {
                }
            }
        }
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
//                    System.out.println("2037 connection closed");
                } catch (SQLException e) {
                }
            }
        } catch (SQLException e) {
        }
    }

    private Connection getConnection(boolean createIfclosed) throws ServletException, SQLException {
        if (createIfclosed && (ncon == null || ncon.isClosed())) {
            ncon = datasource.getConnection();
        }
//        System.out.println("2034 " + ncon.getMetaData().getURL());
        return ncon;
    }

    public String toString() {
        return operation + "=\t" + query;
    }
}
