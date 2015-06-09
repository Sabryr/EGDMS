/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package webservice.medisin.ntnu.no;

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
import java.io.StreamCorruptedException;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Formatter;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sql.DataSource;

/**
 *
 * @author sabryr
 */
public class GetResults implements Runnable {

    private String msges = null;
    private DataSource dataSource_data;
    private String docroot = null;
    private String servername = null;
    private String server_root = null;
    private String[] sql_a;
    private boolean checkifexists;
    private boolean exapandforeignKeys;
    private boolean addcolumnnames;
    private boolean done;
    //todo:make this a hash set  from list
    private ArrayList<String> result_l;
    private Set result_s;
    private long safety = 100000000;
    private int timout;
    private HashMap<String, ArrayList<String>> connection_map;
    private ArrayList<String> all_table_l;
    private boolean getforquery;
    private boolean singlecolumn;
    private Statement st_1;
    private String caller;
    private String user_email;
    private boolean sendEmail;
    private String query_id;
    private static int q_ciunter;
//    private int q_type = 0;
    private boolean childen_at_the_end;
    private boolean shrink_to_parent;
    private Authenticate_service auth_service;
    public static final String TABLE_TO_USE_FLAG = "TABLETOUSE_";
    public final static String SERCH_OP_FLAG = "search";
    public static HashMap<String, LinkedHashMap<String, ArrayList<Object[]>>> uer_searches_map;
    private final String SEARCH_HISTORY_FILE_NAME = "user_searches_map";
    private String username;
    private int person_id;
    private boolean advanced_results;
    private String last_sql = "NA";
    private ArrayList<ArrayList<String>> sql_L = null;
    public static final String[] metadata_types = {"TABLE"};

    public GetResults(Authenticate_service auth_service, DataSource dataSource_data, String servername, String docroot,
            boolean checkifexists, boolean exapandforeignKeys, boolean addcolumnnames, int timout,
            HashMap<String, ArrayList<String>> connection_map, ArrayList<String> all_table_l,
            String username, String server_root, int person_id, boolean childen_at_the_end,
            boolean shrink_to_parent, boolean advanced_results, String... sql_a) {
        this.auth_service = auth_service;
        this.dataSource_data = dataSource_data;
        this.servername = servername;
        this.server_root = server_root;
        this.docroot = docroot;
        this.username = username;
        this.person_id = person_id;
        this.sql_a = sql_a;
        this.checkifexists = checkifexists;
        this.exapandforeignKeys = exapandforeignKeys;
        this.addcolumnnames = addcolumnnames;
        this.timout = timout;
        this.connection_map = connection_map;
        this.all_table_l = all_table_l;
        this.user_email = username;
        this.childen_at_the_end = childen_at_the_end;
        this.advanced_results = advanced_results;
        this.shrink_to_parent = shrink_to_parent;
//        if (q_type < 1) {
//            q_type = 1;
//        }
//        this.q_type = q_type;
        sendEmail = false;
        q_ciunter++;
        query_id = user_email + "_" + q_ciunter;
    }

    public GetResults(Authenticate_service auth_service, DataSource dataSource_data, String servername, String docroot,
            boolean checkifexists, boolean exapandforeignKeys, boolean addcolumnnames, int timout,
            HashMap<String, ArrayList<String>> connection_map, ArrayList<String> all_table_l,
            String username, String server_root, int person_id, boolean childen_at_the_end,
            boolean shrink_to_parent, boolean advanced_results, ArrayList<ArrayList<String>> sql_L) {
        this.auth_service = auth_service;
        this.dataSource_data = dataSource_data;
        this.servername = servername;
        this.server_root = server_root;
        this.docroot = docroot;
        this.username = username;
        this.person_id = person_id;
        this.checkifexists = checkifexists;
        this.exapandforeignKeys = exapandforeignKeys;
        this.addcolumnnames = addcolumnnames;
        this.timout = timout;
        this.connection_map = connection_map;
        this.all_table_l = all_table_l;
        this.user_email = username;
        this.childen_at_the_end = childen_at_the_end;
        this.advanced_results = advanced_results;
        this.shrink_to_parent = shrink_to_parent;
        this.sql_L = sql_L;
//        if (q_type < 1) {
//            q_type = 1;
//        }
//        this.q_type = q_type;
        sendEmail = false;
        q_ciunter++;
        query_id = user_email + "_" + q_ciunter;
    }

    public GetResults(Authenticate_service auth_service, boolean singlecolumn,
            Statement st_1, String caller, int timout, String user_email, String... sql_a) {
        this.auth_service = auth_service;
        this.sql_a = sql_a;
        this.singlecolumn = singlecolumn;
        this.st_1 = st_1;
        this.caller = caller;
        getforquery = true;
        this.timout = timout;
        this.user_email = user_email;
        sendEmail = false;
        q_ciunter++;
        query_id = user_email + "_" + q_ciunter;
        childen_at_the_end = false;
        advanced_results = false;
    }

    @Override
    public void run() {
        done = false;
        if (!advanced_results) {
            result_l = getforQuery(singlecolumn, st_1, caller, sql_a);
        } else {
            Connection ncon = null;
            try {
                ncon = dataSource_data.getConnection();
                if (sql_L == null) {
                    getAdvanced_Results(childen_at_the_end, shrink_to_parent, ncon);
                } else {
                    if (sql_L.isEmpty()) {
                        result_l.clear();
                        result_l.add(0, "NA");
                    } else {
                        ArrayList<String> final_l = new ArrayList<>();
                        sql_a = sql_L.get(0).toArray(new String[sql_L.get(0).size()]);
                        ArrayList<String> tmp_l = getAdvanced_Results(childen_at_the_end, shrink_to_parent, ncon);
                        final_l.addAll(tmp_l.subList(1, tmp_l.size()));
                        for (int i = 1; (!final_l.isEmpty() && i < sql_L.size()); i++) {
                            sql_a = sql_L.get(i).toArray(new String[sql_L.get(i).size()]);
                            tmp_l = getAdvanced_Results(childen_at_the_end, shrink_to_parent, ncon);
                            final_l.retainAll(tmp_l.subList(1, tmp_l.size()));
                        }
                        result_l.clear();
                        result_l.add(0, "NA");
                        if (!final_l.isEmpty()) {
                            result_l.addAll(final_l);
                        }
                    }

                }
            } catch (SQLException ex) {
                result_l.clear();
                result_l.add(0, "Error: Wrong query format. Debuginfo:" + ex.getMessage());
                System.out.println("Error GT21a: creating connections or error in query " + ex.getMessage() + "\nsql=" + last_sql);
                msges = "Error GT21a: creating connections or error in query " + ex.getMessage() + "\nsql=" + last_sql;
            } catch (Exception ex) {
                result_l.clear();
                result_l.add(0, "Error" + ex.getMessage());
                System.out.println("Error GT21a: creating connections or error in query " + ex.getMessage() + "\nsql=" + last_sql);
                msges = "Error GT21a: creating connections or error in query " + ex.getMessage() + "\nsql=" + last_sql;
            } finally {
                close(ncon, null, null);
            }
        }
        if (sendEmail && user_email != null) {
            String encripted_nm = encript("RESULT_URL_" + query_id);
            String file_nm = docroot + encripted_nm;
            if (writeResultsToFile(result_l, file_nm)) {
                String meta = result_l.remove(0);
                String url = null;
                if (meta != null) {
                    if (meta.toUpperCase().startsWith("URL")) {
                        String[] url_a = meta.split("\\|\\|");
                        url = "\n" + url_a[0] + "\n";
                        for (int i = 1; i < url_a.length; i++) {
                            url = url + url_a[i] + "\n";
                        }
                    }
                }
                if (url == null) {
                    Authenticate_service.sendMessage(user_email, user_email, "Reslts ready for query_id=" + query_id + " is ready. Use egenv -search " + Authenticate_service.CODE_RESULT_FLAG + "=" + encripted_nm + " to get the results from command line\n", "eGEnVar process monitor");
                } else {
                    Authenticate_service.sendMessage(user_email, user_email, "Reslts ready for query_id=" + query_id + " is ready. Use egenv -search " + Authenticate_service.CODE_RESULT_FLAG + "=" + encripted_nm + " to get the results from command line\n"
                            + "or use the links " + url, "eGEnVar process monitor");
                }
            } else {
                System.out.println("54 sending to " + user_email);
                Authenticate_service.sendMessage(user_email, user_email, "Sorry, there was an error during quary, query_id=" + query_id, "eGEnVar process monitor");
            }
        }
        done = true;
    }

    public boolean isPostpondable(int limit) {
        while (!done && limit > 0) {
            limit--;
            try {
                Thread.sleep(100);
            } catch (InterruptedException ex) {
            }

        }
        return done;
    }

    public String getQuery_Id() {
        return query_id;
    }

    public void sendMail() {
        sendEmail = true;
    }

    public boolean isDone() {
        while (!done && safety > 0) {
            safety--;
            if (safety < timout) {
                try {
                    Thread.sleep(timout);
                } catch (InterruptedException ex) {
                }
            } else if (safety < 10000) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException ex) {
                }
            }
        }
        return done;
    }

    public ArrayList<String> getResults() {
        if (result_l == null) {
            result_l = new ArrayList<>(1);
        }
        return result_l;
    }

    private ArrayList<String> getHirarchical_Results() {
        result_l = new ArrayList<>(1);

        return result_l;
    }

    private ArrayList<String> getAdvanced_Results(boolean exapnd_to_children, boolean shrink_to_parent,
            Connection c_con) throws SQLException, Exception {
//        System.out.println("239 exapnd_to_children=" + exapnd_to_children);
//        boolean error_rable_used=false;  
        result_l = new ArrayList<>(1);
        result_s = new HashSet<>();
        ResultSet r_1 = null;
//        Connection ncon = null;
        boolean add_report = false;
        String c_target = "NA";
        String file_url = docroot + SEARCH_HISTORY_FILE_NAME;
        Object ob = readResultsFromFile(file_url);
        if (ob != null && ob instanceof HashMap) {
            uer_searches_map = (HashMap<String, LinkedHashMap<String, ArrayList<Object[]>>>) ob;
        } else {
            uer_searches_map = new HashMap<>();
        }
        LinkedHashMap<String, ArrayList<Object[]>> new_result_map = new LinkedHashMap<>();
        HashMap<String, ArrayList<Integer>> table2id_map = new HashMap<>();
        HashMap<String, StringBuilder> table2columns_map = new HashMap<>();
        if (!c_con.isClosed()) {
            st_1 = c_con.createStatement();
            st_1.setQueryTimeout(Authenticate_service.TIME_OUT_LIMIT);
            result_l.add("NA");
            int error = -1;
            for (int s = 0; (error < 0 && s < sql_a.length); s++) {//
                last_sql = sql_a[s];
//                System.out.println("329 "+last_sql);
                r_1 = st_1.executeQuery(last_sql.substring(0, last_sql.lastIndexOf("|")));
                if (sql_a.length > 1) {
                    c_target = "ALL";
                }
                if (checkifexists) {
                    result_l.clear();
                    if (r_1 != null && r_1.next()) {
                        result_l.add("1");
                    } else {
                        result_l.add("0");
                    }
                    result_l.add(0, "OK");
                } else if (r_1 != null) {
                    ArrayList<String> table_nm_l = new ArrayList<>(table2columns_map.keySet());
                    ArrayList<Integer> id_col_pos_l = new ArrayList<>();
                    ArrayList<String> loaded_col_l = new ArrayList<>();
                    ArrayList<Integer> columns_to_include_pos_l = new ArrayList<>();
                    HashMap<String, Integer> table2_id_col_count_map = new HashMap<>();
                    ResultSetMetaData rsmd = r_1.getMetaData();
                    int NumOfCol = rsmd.getColumnCount();
                    for (int i = 1; i <= NumOfCol; i++) {
                        String c_tbl = rsmd.getTableName(i).toUpperCase();

                        if (i == 1 && c_target == null) {
                            c_target = c_tbl;
                        }
                        if (!table2columns_map.containsKey(c_tbl)) {
                            table2columns_map.put(c_tbl, new StringBuilder());
                            table2columns_map.get(c_tbl).append(rsmd.getColumnName(i));
                        } else {
                            table2columns_map.get(c_tbl).append("," + rsmd.getColumnName(i));
                        }
                        String c_col = (auth_service.get_correct_table_name(c_tbl)) + "." + rsmd.getColumnName(i);
                        if (Authenticate_service.column2autoincrement_l != null && Authenticate_service.column2autoincrement_l.contains(c_col)) {
                            id_col_pos_l.add(i);
                            String c_col_name_to_use = /*auth_service.get_correct_table_name(c_tbl)*/ c_tbl + "." + rsmd.getColumnName(i);
                            c_col_name_to_use = c_col_name_to_use.toUpperCase();
                            if (!table2_id_col_count_map.containsKey(c_col_name_to_use)) {
                                table2_id_col_count_map.put(c_col_name_to_use, 1);
                            } else {
                                table2_id_col_count_map.put(c_col_name_to_use, table2_id_col_count_map.get(c_col_name_to_use) + 1);
                            }
                        } else if (rsmd.getColumnName(i).toUpperCase().equals("ID")) {//c_tbl.endsWith("_TAGSOURCE") &&
                            id_col_pos_l.add(i);
                            String c_col_name_to_use = c_tbl + "." + rsmd.getColumnName(i);
                            c_col_name_to_use = c_col_name_to_use.toUpperCase();
                            if (!table2_id_col_count_map.containsKey(c_col_name_to_use)) {
                                table2_id_col_count_map.put(c_col_name_to_use, 1);
                            } else {
                                table2_id_col_count_map.put(c_col_name_to_use, table2_id_col_count_map.get(c_col_name_to_use) + 1);
                            }
                        }
                        String clmn_lable = rsmd.getColumnLabel(i);
                        String cq_tbl = rsmd.getTableName(i);
                        if (!table2id_map.containsKey(cq_tbl)) {
                            table2id_map.put(cq_tbl, new ArrayList<Integer>());
                        }
                        String ful_col_nm = cq_tbl + "." + clmn_lable;
                        ful_col_nm = ful_col_nm.toUpperCase();
                        if (!loaded_col_l.contains(ful_col_nm)) {
                            loaded_col_l.add(ful_col_nm);
                            columns_to_include_pos_l.add(i);
                        }
                    }
                    boolean print_id = true;
                    for (int i = 0; i < columns_to_include_pos_l.size(); i++) {
                        if (id_col_pos_l.contains(columns_to_include_pos_l.get(i))) {
                            String clmn_lable = rsmd.getColumnLabel(columns_to_include_pos_l.get(i));
                            String cq_tbl = rsmd.getTableName(columns_to_include_pos_l.get(i));
                            String ful_col_nm = cq_tbl + "." + clmn_lable;
                            if (table2_id_col_count_map.get(ful_col_nm) <= 1) {
                                print_id = false;
                            }
                        }
                    }
                    ArrayList<String> foreing_key_clmns_l = new ArrayList<>(1);
                    HashMap<String, String[]> constraint_map = null;
                    if (exapandforeignKeys && table_nm_l.size() == 1) {
                        constraint_map = Constants.get_key_contraints(table_nm_l.get(0), dataSource_data, Authenticate_service.DATABASE_NAME_DATA);
                        String[] foreing_key_clmns_a = constraint_map.get(Constants.FOREIGN_KEY_COLUMNS_FLAG);
                        if (foreing_key_clmns_a != null && foreing_key_clmns_a.length > 0) {
                            foreing_key_clmns_l = new ArrayList<>(Arrays.asList(foreing_key_clmns_a));
                        }
                    }
                    HashMap<String, String> table2uniques_map = new HashMap<>();
                    while (r_1.next()) {
//                        System.out.println("148 "+columns_to_include_pos_l+"\t"+id_col_pos_l);
                        ArrayList<String> result_tmp_l = new ArrayList<>();
                        for (int k = 0; k < columns_to_include_pos_l.size(); k++) {
//                                boolean skip = false;
                            String clmn_lable = rsmd.getColumnLabel(columns_to_include_pos_l.get(k));
                            String cq_tbl = rsmd.getTableName(columns_to_include_pos_l.get(k));
                            String c_val = null;
                            if (id_col_pos_l.contains(columns_to_include_pos_l.get(k))) {
                                int c_id = r_1.getInt(columns_to_include_pos_l.get(k));
                                if (print_id) {
                                    c_val = c_id + "";
                                }
                                table2id_map.get(cq_tbl).add(c_id);
                                if (add_report) { //must improve this code
                                    String report_tbl = Constants.get_correct_table_name("report", dataSource_data,
                                            Authenticate_service.DATABASE_NAME_DATA, auth_service.getTables());
                                    String connecting_tbl = Constants.get_correct_table_name("files2report",
                                            dataSource_data, Authenticate_service.DATABASE_NAME_DATA, auth_service.getTables());
//                                        String repo_sql = "SELECT name from " + report_tbl + " WHERE id in (SELECT report_id from " + connecting_tbl + " where files_id=" + cid + ")";

                                    last_sql = "SELECT name from " + report_tbl + " WHERE id in (SELECT report_id from " + connecting_tbl + " where files_id=" + c_id + ")";

                                    List result_expand_l = getforQuery(false, st_1, caller + 1, last_sql);
                                    if (result_expand_l != null && !result_expand_l.isEmpty()) {
                                        for (int j = 0; j < result_expand_l.size(); j++) {
                                            c_val = c_val + "||" + result_expand_l.get(j);
                                        }
                                    }
                                    //hard corded only for files
//                                        for (int j = 0; j < tag_tables.size(); j++) {
//                                            String tag_sql = "select * from " + tag_tables.get(j) + " where files_id=" + cid;
//                                            List result_tag_l = getforQuery(tag_sql, false, null, caller + 2);
//                                            if (result_tag_l != null && !result_tag_l.isEmpty()) {
//                                                for (int k = 0; k < result_tag_l.size(); k++) {
//                                                    c_val = c_val + "||" + result_tag_l.get(k);
//                                                }
//                                            }
//                                        }
                                }
//                                    } else {
//                                        if (!table2id_map.get(cq_tbl).contains(-1)) {
//                                            table2id_map.get(cq_tbl).add(-1);
//                                        }
//                                    }
                            } else {
                                c_val = r_1.getString(columns_to_include_pos_l.get(k));
                            }
                            //must improe this code
                            if (table_nm_l.size() == 1 && exapandforeignKeys && foreing_key_clmns_l.contains(clmn_lable)
                                    && constraint_map != null && !(constraint_map.get(clmn_lable)[1].toUpperCase().equals(table_nm_l.get(0)))) {
//                                System.out.println("237 expanding "+clmn_lable);
                                String forgn_clmn = "name";
                                String corrected_forng_tbl = Constants.get_correct_table_name(constraint_map.get(clmn_lable)[1],
                                        dataSource_data, Authenticate_service.DATABASE_NAME_DATA, auth_service.getTables());
                                if (table2uniques_map.containsKey(corrected_forng_tbl)) {
                                    forgn_clmn = table2uniques_map.get(corrected_forng_tbl);
                                } else {
                                    ArrayList<String> tm_l = Constants.getUniqueList(corrected_forng_tbl, dataSource_data, Authenticate_service.DATABASE_NAME_DATA);
                                    if (tm_l != null && !tm_l.isEmpty()) {
                                        table2uniques_map.put(corrected_forng_tbl, tm_l.toString().replace("[", "").replace("]", ""));
                                    }
                                    forgn_clmn = table2uniques_map.get(corrected_forng_tbl);
                                }

                                last_sql = "SELECT " + forgn_clmn + " from " + corrected_forng_tbl + " where "
                                        + corrected_forng_tbl + "." + constraint_map.get(clmn_lable)[2] + "=" + c_val;
                                List result_expand_l = getforQuery(false, st_1, caller + 3, last_sql);
                                if (result_expand_l != null && !result_expand_l.isEmpty()) {
                                    c_val = constraint_map.get(clmn_lable)[1].split("\\.")[constraint_map.get(clmn_lable)[1].split("\\.").length - 1] + "." + constraint_map.get(clmn_lable)[2] + "=" + c_val;
                                    for (int j = 0; j < result_expand_l.size(); j++) {
                                        c_val = c_val + "||" + result_expand_l.get(j);
                                    }
                                    clmn_lable = "";
                                }
                            } else {
                                clmn_lable = rsmd.getTableName(columns_to_include_pos_l.get(k)) + "." + clmn_lable;
                            }
//                            System.out.println("495 "+c_val);
                            if (c_val != null) {
                                if (addcolumnnames) {
                                    if (clmn_lable.isEmpty()) {
                                        result_tmp_l.add(c_val);
                                    } else {
                                        result_tmp_l.add(clmn_lable + "=" + c_val);
                                    }
                                } else {
                                    result_tmp_l.add(c_val);
                                }
                            }
                        }
                        if (!result_tmp_l.isEmpty()) {
                            StringBuilder result_tmp_s = new StringBuilder();
                            result_tmp_s.append(result_tmp_l.get(0));
                            for (int i = 1; i < result_tmp_l.size(); i++) {
                                result_tmp_s.append("||");
                                result_tmp_s.append(result_tmp_l.get(i));
                            }
//                                 System.out.println("\n322 "+last_sql);
                            result_s.add(result_tmp_s.toString());
                            result_tmp_l.clear();
//                            System.out.println("503 result_tmp_s=" + result_tmp_s + "\tresult_s=" + result_s);
                        }
                    }
                    r_1.close();
                }
//                    System.out.println("505 " + Timing.convert(Timing.getFromlastPointer2()));
            }

//            System.out.println("520 "+table2id_map+"  c_target="+c_target+" "+result_l);
//            System.out.println("525 "+result_s);
            if (!shrink_to_parent) {
                result_l.addAll(result_s);
            }
            result_s.clear();
            if (!shrink_to_parent && result_l.size() == 1) {
                result_l.add(0, c_target + "=NA");
            } else {
                if (c_target == null) {
                    result_l.add(0, "Error" + result_l.toString());
                } else {
                    ArrayList<String> table2id_map_kl = new ArrayList<>(table2id_map.keySet());
                    StringBuilder url_b = new StringBuilder();
                    for (int i = 0; i < table2id_map_kl.size(); i++) {
                        String c_tbl = table2id_map_kl.get(i).trim();
                        if (c_tbl.toUpperCase().startsWith("ERROR")) {
                            url_b.append(c_tbl);
                            url_b.append(":");
                            url_b.append(table2id_map.get(c_tbl));
                        } else {
                            if (exapnd_to_children) {
                                if (c_tbl.toUpperCase().endsWith("_TAGSOURCE")) {
                                    String c_sql = "select id from " + auth_service.get_correct_table_name(c_tbl) + " where parent_id in (" + table2id_map.get(c_tbl).toString().replace("[", "").replace("]", "") + ")";
                                    String ch_sql = get_id_sql(st_1, auth_service.get_correct_table_name(c_tbl), c_sql, "parent_id", table2columns_map.get(c_tbl).toString(),
                                            auth_service.get_correct_table_name(c_tbl), "id");
                                    if (ch_sql != null) {
                                        ArrayList<String> expanded_expand_l = getforQuery(false, st_1, caller + 1, ch_sql);
                                        if (!expanded_expand_l.isEmpty()) {
                                            for (int k = 0; k < expanded_expand_l.size(); k++) {
                                                if (!result_l.contains(expanded_expand_l.get(k))) {
                                                    result_l.add(expanded_expand_l.get(k));
                                                }
                                            }
                                            expanded_expand_l.clear();
                                        }
                                    }
                                } else {
                                    String hir_tbl = auth_service.get_correct_table_name(c_tbl + "_hierarchy");
                                    if (hir_tbl != null && table2id_map.containsKey(c_tbl) && !table2id_map.get(c_tbl).isEmpty()) {
                                        String parent_col = Constants.get_Parent_ref_column(hir_tbl, dataSource_data, Authenticate_service.DATABASE_NAME_DATA);
                                        String child_col = Constants.get_Child_ref_column(hir_tbl, dataSource_data, Authenticate_service.DATABASE_NAME_DATA);
                                        String c_sql = "select " + child_col + " from " + hir_tbl + " where " + parent_col + " in (" + table2id_map.get(c_tbl).toString().replace("[", "").replace("]", "") + ")";
                                        String ch_sql = get_id_sql(st_1, hir_tbl, c_sql, parent_col, table2columns_map.get(c_tbl).toString(),
                                                auth_service.get_correct_table_name(c_tbl), child_col);
                                        if (ch_sql != null) {
                                            ArrayList<String> expanded_expand_l = getforQuery(false, st_1, caller + 1, ch_sql);
                                            if (!expanded_expand_l.isEmpty()) {
                                                for (int k = 0; k < expanded_expand_l.size(); k++) {
                                                    if (!result_l.contains(expanded_expand_l.get(k))) {
                                                        result_l.add(expanded_expand_l.get(k));
                                                    }
                                                }
                                                expanded_expand_l.clear();
                                            }
                                        }
                                    }
                                }
                            } else if (shrink_to_parent) {
                                if (c_tbl.toUpperCase().endsWith("_TAGSOURCE")) {
                                    String c_sql = "select parent_id from " + auth_service.get_correct_table_name(c_tbl) + " where id in (" + table2id_map.get(c_tbl).toString().replace("[", "").replace("]", "") + ")";
                                    String ch_sql = get_id_sql(st_1, auth_service.get_correct_table_name(c_tbl), c_sql, "id", table2columns_map.get(c_tbl).toString(),
                                            auth_service.get_correct_table_name(c_tbl), "id");
                                    if (ch_sql != null) {
                                        ArrayList<String> expanded_expand_l = getforQuery(false, st_1, caller + 1, ch_sql);
                                        if (!expanded_expand_l.isEmpty()) {
                                            for (int k = 0; k < expanded_expand_l.size(); k++) {
                                                if (!result_l.contains(expanded_expand_l.get(k))) {
                                                    result_l.add(expanded_expand_l.get(k));
                                                }
                                            }
                                            expanded_expand_l.clear();
                                        }
                                    }
                                } else {
                                    String hir_tbl = auth_service.get_correct_table_name(c_tbl + "_hierarchy");
                                    if (hir_tbl != null && table2id_map.containsKey(c_tbl) && !table2id_map.get(c_tbl).isEmpty()) {
                                        String parent_col = Constants.get_Parent_ref_column(hir_tbl, dataSource_data, Authenticate_service.DATABASE_NAME_DATA);
                                        String child_col = Constants.get_Child_ref_column(hir_tbl, dataSource_data, Authenticate_service.DATABASE_NAME_DATA);
                                        String c_sql = "select " + parent_col + " from " + hir_tbl + " where " + child_col + " in ("
                                                + table2id_map.get(c_tbl).toString().replace("[", "").replace("]", "") + ")";
                                        String ch_sql = get_id_sql(st_1, hir_tbl, c_sql, child_col, table2columns_map.get(c_tbl).toString(),
                                                auth_service.get_correct_table_name(c_tbl), parent_col);
//                                        System.out.println("501 ch_sql=" + ch_sql);
                                        if (ch_sql != null) {
                                            ArrayList<String> expanded_expand_l = getforQuery(false, st_1, caller + 1, ch_sql);
                                            if (!expanded_expand_l.isEmpty()) {
                                                for (int k = 0; k < expanded_expand_l.size(); k++) {
                                                    if (!result_l.contains(expanded_expand_l.get(k))) {
                                                        result_l.add(expanded_expand_l.get(k));
                                                    }
                                                }
                                                expanded_expand_l.clear();
                                            }
                                        }
                                    }
                                }
                            }
                            if (!table2id_map.get(c_tbl).isEmpty()) {
                                url_b.append("URL(");
                                url_b.append(c_tbl);
                                url_b.append(")=");
                                String static_link = servername + "Search/SearchResults3?" + Authenticate_service.TABLE_TO_USE_FLAG + c_tbl + "=" + "" + makeSTATIC_URL(table2id_map.get(c_tbl), c_tbl, docroot);
//                                System.out.println("623 "+static_link);
                                url_b.append(static_link);
                                url_b.append("||");
                                Object[] ret_a = new Object[10];
                                ret_a[0] = c_tbl;
                                ret_a[1] = table2id_map.get(c_tbl).size();
                                ret_a[2] = makeSTATIC_URL(table2id_map.get(c_tbl), c_tbl, docroot);
                                ret_a[3] = c_tbl;
                                ret_a[4] = "Last_CMD_query " + " | Result=" + static_link;
                                ret_a[5] = static_link;
                                String expan_out_str = servername + "Search/SearchResults3?" + TABLE_TO_USE_FLAG + c_tbl + "=" + ret_a[2] + "&reset=true&operation=" + SERCH_OP_FLAG + "&current_tbl_nm=" + c_tbl;

                                ret_a[6] = makeQR(expan_out_str);
                                ret_a[7] = "0";
                                ret_a[8] = table2id_map.get(c_tbl);
//                                new_result_map.put(query_to_include + "(" + split + ") " + table_nm, table_nm + "||" + spliter_l.size() + "||" + makeSTATIC_URL(spliter_l, table_nm, Constants.getDocRoot()));
                                ArrayList<Object[]> tmp_l = new ArrayList<>();
                                tmp_l.add(ret_a);
                                new_result_map.put("Last_CMD_query on " + c_tbl, tmp_l);
                            }
                        }
                    }
//                    System.out.println("639 result_l="+result_l);
                    result_l.remove(0);
                    url_b.append("Results derived from database version: ");
                    url_b.append(get_last_memorised(c_con));
                    result_l.add(0, url_b.toString());//
                    url_b.setLength(0);
                }
            }
            close(null, st_1, r_1);
        }
//        } catch (SQLException ex) {
//            System.out.println("Error GT21a: creating connections or error in query " + ex.getMessage() + "\nsql=" + last_sql);
//            msges = "Error GT21a: creating connections or error in query " + ex.getMessage() + "\nsql=" + last_sql;
//            ex.printStackTrace();
//        } finally {
//            close(null, null, r_1);
//        }

        if (person_id > 0) {
            if (uer_searches_map.containsKey(person_id + "")) {
                uer_searches_map.get(person_id + "").putAll(new_result_map);
            } else {
                uer_searches_map.put(person_id + "", new_result_map);
            }
        }
        writeResultsToFile(uer_searches_map, docroot + SEARCH_HISTORY_FILE_NAME);
//         System.out.println("\t665 result_l="+result_l);
        return result_l;
    }

    public String get_last_memorised(Connection c_con) {
        String last_updated = " Unknown";
        if (c_con != null) {
            try {
                if (!c_con.isClosed()) {
                    Statement st_1 = c_con.createStatement();
                    String config_tbl = auth_service.get_correct_table_name("config");
                    if (config_tbl != null) {
                        ResultSet r_1 = st_1.executeQuery("select param_value from " + config_tbl + " where name='LAST_MEMORIZED'");
                        while (r_1.next()) {
                            last_updated = r_1.getString("param_value");
                        }
                    }
                }
            } catch (SQLException ex) {
//                    ex.printStackTrace();
            }
        }
        return last_updated;
    }
//    public String makeSTATIC_URL(ArrayList<Integer> id_l, String current_tbl_nm, String doc_root) {
//        String encripted_nm = makeSTATIC_URL(id_l, current_tbl_nm);
//        File file = new File(doc_root + encripted_nm);
//        if (file.isFile()) {
//        } else {
//            writeResultsToFile(current_tbl_nm + "||" + id_l.toString().replaceAll("[^0-9,]", ""), Constants.getDocRoot() + encripted_nm);
//        }
//        return encripted_nm;
//    }

    private String get_id_sql(Statement st_1, String hir_tbl, String sql,
            String parent_id_colnm, String child_search_colmns, String orig_tbl,
            String child_col_nm) {
        ArrayList<Integer> c_id_l = new ArrayList<>();
        try {
            int safetey = 100;
            while (sql != null && safetey > 0) {
                safetey--;
                ResultSet r_1 = st_1.executeQuery(sql);
//                String table_nm = r_1.getMetaData().getTableName(1);
                ArrayList<Integer> tmp_id_l = new ArrayList<>();
                while (r_1.next()) {
                    tmp_id_l.add(r_1.getInt(1));
                }
                r_1.close();
                if (!tmp_id_l.isEmpty()) {
                    c_id_l.removeAll(tmp_id_l);
                    c_id_l.addAll(tmp_id_l);
//                    System.out.println("515 " + c_id_l);
                    sql = "select " + child_col_nm + "  from " + hir_tbl + " where " + parent_id_colnm + " in (" + tmp_id_l.toString().replace("[", "").replace("]", "") + ")";
                } else {
                    sql = null;
                }
            }
        } catch (SQLException ex) {
        }
        if (c_id_l.isEmpty()) {
            return null;
        } else {
            return "select " + child_search_colmns + " from " + orig_tbl + " where ID in (" + c_id_l.toString().replace("[", "").replace("]", "") + ")";
        }

    }

    public String makeQR(String texttoencode) { //, String doc_root, int caller

        String url = null;
        File file = new File(docroot + Constants.encript(texttoencode) + ".png");
        //       File  file = new File(file.getAbsoluteFile().getParentFile().getParent() + File.separatorChar + "docroot" + File.separatorChar + session_id);

        if (file.isFile()) {
            url = server_root + file.getName();
        } else {
            Charset charset = Charset.forName("UTF-8");
            CharsetEncoder encoder = charset.newEncoder();
            byte[] b = null;
            try {
                ByteBuffer bbuf = encoder.encode(CharBuffer.wrap(texttoencode));
                b = bbuf.array();
            } catch (CharacterCodingException e) {
                System.out.println("Error GR_qra:" + e.getMessage());
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
                    System.out.println("Error GR_qrb:" + e.getMessage());
                }
                try {
                    if (matrix != null) {
                        MatrixToImageWriter.writeToFile(matrix, "PNG", file);
                        url = server_root + file.getName();
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
//    private ArrayList<String> getAdvanced_Results_orig() {
//        result_l = new ArrayList<>(1);
//        ResultSet r_1 = null;
//        Connection ncon = null;
//        boolean add_report = false;
//        String c_target = "NA";
//        try {
//            ArrayList<String> tag_tables = new ArrayList<>();
//            ncon = dataSource_data.getConnection();
//            Statement st_1 = null;
//            if (!ncon.isClosed()) {
//                st_1 = ncon.createStatement();
//                try {
//                    st_1.setQueryTimeout(Authenticate_service.TIME_OUT_LIMIT);
//                    String[] sql_a = sql.split("\\|");
//                    r_1 = st_1.executeQuery(sql_a[0]);
//                    if (sql_a.length > 1) {
//                        c_target = sql_a[1];
//                    }
//                } catch (SQLException ex) {
//                    String er_sql = sql.subSequence(0, ((sql.length() > 100) ? 100 : sql.length())) + "";
//                    System.out.println("Error GR21f: creating connections or error in query " + ex.getMessage() + "\nsql=" + er_sql);
//                    msges = "Error GT21f: creating connections or error in query " + ex.getMessage() + "\nsql=" + er_sql;
//                }
//
//                if (checkifexists) {
//                    if (r_1 != null && r_1.next()) {
//                        result_l.add("1");
//                    } else {
//                        result_l.add("0");
//                    }
//                    result_l.add(0, "OK");
//                } else if (r_1 != null) {
//                    ResultSetMetaData rsmd = r_1.getMetaData();
//                    ArrayList<String> table_nm_l = new ArrayList<>();
//                    int NumOfCol = rsmd.getColumnCount();
//                    for (int i = 1; i <= NumOfCol; i++) {
//                        String c_tbl = rsmd.getTableName(i).toUpperCase();
//                        if (!table_nm_l.contains(c_tbl)) {
//                            table_nm_l.add(c_tbl);
//                        }
//
//                    }
//                    String id_table = null;
////                    String corrected_table_nm = Constants.get_correct_table_name(table_nm, dataSource_data, Authenticate_service.DATABASE_NAME_DATA, getTables());
////                    String potential_tag_tbl = (corrected_table_nm + "2tags").toUpperCase();
//                    //include tag informmation in the report
////                    if (table_nm_l.contains("FILES")) {
////                        String potential_tag_tbl = (Constants.get_correct_table_name("FILES", dataSource_data, Authenticate_service.DATABASE_NAME_DATA, getTables()) + "2tags").toUpperCase();
////                        for (int i = 0; i < all_table_l.size(); i++) {
////                            if (all_table_l.get(i).toUpperCase().equals(potential_tag_tbl)) {
////                                tag_tables.add(all_table_l.get(i));
////                            }
////                        }
////                    }
//
//                    int id_col_pos = -1;
//                    for (int i = 1; (i <= NumOfCol && id_col_pos < 0); i++) {
//                        for (int j = 0; (j < table_nm_l.size() && id_col_pos < 0); j++) {
//                            String c_tbl = table_nm_l.get(j);
//                            if (Authenticate_service.column2autoincrement_l != null && Authenticate_service.column2autoincrement_l.contains(Constants.get_correct_table_name(c_tbl, dataSource_data, Authenticate_service.DATABASE_NAME_DATA, getTables()) + "." + rsmd.getColumnName(i))) {
//                                id_col_pos = i;
//                                id_table = Constants.get_correct_table_name(c_tbl, dataSource_data, Authenticate_service.DATABASE_NAME_DATA, getTables());
//                            }
//                        }
//                    }
//                    if (addcolumnnames && table_nm_l.contains("FILES")) {
//                        add_report = true;
//                    }
//                    ArrayList<String> foreing_key_clmns_l = new ArrayList<>(1);
//                    HashMap<String, String[]> constraint_map = null;
//                    if (exapandforeignKeys && table_nm_l.size() == 1) {
//                        constraint_map = Constants.get_key_contraints(table_nm_l.get(0), dataSource_data, Authenticate_service.DATABASE_NAME_DATA);
//                        String[] foreing_key_clmns_a = constraint_map.get(Constants.FOREIGN_KEY_COLUMNS_FLAG);
//                        if (foreing_key_clmns_a != null && foreing_key_clmns_a.length > 0) {
//                            foreing_key_clmns_l = new ArrayList<>(Arrays.asList(foreing_key_clmns_a));
//                        }
//                    }
//
//                    HashMap<String, String> table2uniques_map = new HashMap<>();
//                    ArrayList<Integer> id_list = new ArrayList<>();
//                    while (r_1.next()) {
//                        ArrayList<String> result_tmp_l = new ArrayList<>();
//                        for (int i = 1; i <= NumOfCol; i++) {
//                            String clmn_lable = rsmd.getColumnLabel(i);
//                            String c_val = r_1.getString(i);
//                            if (id_col_pos == i) {
//                                if (c_val != null && c_val.matches("[0-9]+")) {
//                                    int cid = new Integer(c_val);
//                                    id_list.add(cid);
//                                    if (add_report) { //must improve this code
//                                        String report_tbl = Constants.get_correct_table_name("report", dataSource_data, Authenticate_service.DATABASE_NAME_DATA, getTables());
//                                        String connecting_tbl = Constants.get_correct_table_name("files2report", dataSource_data, Authenticate_service.DATABASE_NAME_DATA, getTables());
//                                        String repo_sql = "SELECT name from " + report_tbl + " WHERE id in (SELECT report_id from " + connecting_tbl + " where files_id=" + cid + ")";
//                                        List result_expand_l = getforQuery(repo_sql, false, null, caller + 1);
//                                        if (result_expand_l != null && !result_expand_l.isEmpty()) {
//                                            for (int j = 0; j < result_expand_l.size(); j++) {
//                                                c_val = c_val + "||" + result_expand_l.get(j);
//                                            }
//                                        }
//                                        //hard corded only for files
////                                        for (int j = 0; j < tag_tables.size(); j++) {
////                                            String tag_sql = "select * from " + tag_tables.get(j) + " where files_id=" + cid;
////                                            List result_tag_l = getforQuery(tag_sql, false, null, caller + 2);
////                                            if (result_tag_l != null && !result_tag_l.isEmpty()) {
////                                                for (int k = 0; k < result_tag_l.size(); k++) {
////                                                    c_val = c_val + "||" + result_tag_l.get(k);
////                                                }
////                                            }
////                                        }
//                                    }
//
//                                } else {
//                                    id_list.add(-1);
//                                }
//
//                            }
//                            //must improe this code
//                            if (table_nm_l.size() == 1 && exapandforeignKeys && foreing_key_clmns_l.contains(clmn_lable) && constraint_map != null && !(constraint_map.get(clmn_lable)[1].toUpperCase().equals(table_nm_l.get(0)))) {
////                                System.out.println("237 expanding "+clmn_lable);
//                                String forgn_clmn = "name";
//                                String corrected_forng_tbl = Constants.get_correct_table_name(constraint_map.get(clmn_lable)[1], dataSource_data, Authenticate_service.DATABASE_NAME_DATA, getTables());
//                                if (table2uniques_map.containsKey(corrected_forng_tbl)) {
//                                    forgn_clmn = table2uniques_map.get(corrected_forng_tbl);
//                                } else {
//                                    ArrayList<String> tm_l = Constants.getUniqueList(corrected_forng_tbl, dataSource_data, Authenticate_service.DATABASE_NAME_DATA);
//                                    if (tm_l != null && !tm_l.isEmpty()) {
//                                        table2uniques_map.put(corrected_forng_tbl, tm_l.toString().replace("[", "").replace("]", ""));
//                                    }
//                                    forgn_clmn = table2uniques_map.get(corrected_forng_tbl);
//                                }
//                                List result_expand_l = getforQuery("SELECT " + forgn_clmn + " from " + corrected_forng_tbl + " where "
//                                        + corrected_forng_tbl + "." + constraint_map.get(clmn_lable)[2] + "=" + c_val, false, null, caller + 3);
//                                if (result_expand_l != null && !result_expand_l.isEmpty()) {
//                                    c_val = constraint_map.get(clmn_lable)[1].split("\\.")[constraint_map.get(clmn_lable)[1].split("\\.").length - 1] + "." + constraint_map.get(clmn_lable)[2] + "=" + c_val;
//                                    for (int j = 0; j < result_expand_l.size(); j++) {
//                                        c_val = c_val + "||" + result_expand_l.get(j);
//                                    }
//                                    clmn_lable = "";
//                                }
//                            } else {
//                                clmn_lable = rsmd.getTableName(i) + "." + clmn_lable;
//                            }
//
//                            if (addcolumnnames) {
//                                if (clmn_lable.isEmpty()) {
//                                    result_tmp_l.add(c_val);
//                                } else {
//                                    result_tmp_l.add(clmn_lable + "=" + c_val);
//                                }
//                            } else {
//                                result_tmp_l.add(c_val);
//                            }
//                        }
//                        if (!result_tmp_l.isEmpty()) {
//                            StringBuilder result_tmp_s = new StringBuilder();
//                            result_tmp_s.append(result_tmp_l.get(0));
//                            for (int i = 1; i < result_tmp_l.size(); i++) {
//                                result_tmp_s.append("||");
//                                result_tmp_s.append(result_tmp_l.get(i));
//
//                            }
//                            String result_tmp_l_s = result_tmp_s.toString();
//                            if (!result_l.contains(result_tmp_l_s)) {
//                                result_l.add(result_tmp_l_s);
//                            }
//                            result_tmp_l.clear();
//                        }
//                    }
//                    if (result_l.isEmpty()) {
//                        result_l.add(0, "URL(" + c_target + ")=NA");
//                    } else {
//                        if (!id_list.isEmpty()) {
//                            if (id_table != null) {
//                                String static_link = servername + "Search/SearchResults3?" + Authenticate_service.TABLE_TO_USE_FLAG + id_table + "=" + "" + makeSTATIC_URL(id_list, id_table, docroot);
//                                result_l.add(0, "URL(" + c_target + ")=" + static_link);
//                            } else {
//                                result_l.add(0, "URL(" + c_target + ")=NA");
//                            }
//                        } else {
//                            result_l.add(0, "URL(" + c_target + ")=NA");
//                        }
//                    }
//                }
//                close(ncon, st_1, r_1);
//            }
//        } catch (SQLException ex) {
//            System.out.println("Error GT21a: creating connections or error in query " + ex.getMessage() + "\nsql=" + sql);
//            msges = "Error GT21a: creating connections or error in query " + ex.getMessage() + "\nsql=" + sql;
////                    result_l.add(0, "Error GT21a: creating connections or error in query " + ex.getMessage());
//
//        } finally {
//            close(ncon, null, r_1);
//        }
//        return result_l;
//    }

    /*
     MethodID=GR10
     */
    private ArrayList<String> getforQuery(boolean singlecolumn, Statement st_1, String caller, String... sql_a) {
        ArrayList<String> final_result_l = new ArrayList<>();
        Connection ncon = null;
        String last_query = "NA";

        try {
            if (st_1 == null) {
                ncon = dataSource_data.getConnection();
                if (ncon != null && !ncon.isClosed()) {
                    st_1 = ncon.createStatement();
                }
            }
            if (st_1 != null) {
                for (int i = 0; i < sql_a.length; i++) {
                    last_query = sql_a[i];
                    ResultSet r_1 = st_1.executeQuery(last_query);
                    ResultSetMetaData rsmd = r_1.getMetaData();
                    String table_nm = rsmd.getTableName(1);
                    int numOfCol = rsmd.getColumnCount();
                    ArrayList<String> c_result_l = new ArrayList<>(1);
                    HashMap<String, Integer> table2_id_col_count_map = new HashMap<>();
                    for (int j = 1; j <= numOfCol; j++) {
                        String c_col_name_to_use = table_nm + "." + rsmd.getColumnName(j);
                        String c_col = (auth_service.get_correct_table_name(table_nm)) + "." + rsmd.getColumnName(j);
                        if (Authenticate_service.column2autoincrement_l != null && Authenticate_service.column2autoincrement_l.contains(c_col)) {
                            c_col_name_to_use = c_col_name_to_use.toUpperCase();
                            if (!table2_id_col_count_map.containsKey(c_col_name_to_use)) {
                                table2_id_col_count_map.put(c_col_name_to_use, 1);
                            } else {
                                table2_id_col_count_map.put(c_col_name_to_use, table2_id_col_count_map.get(c_col_name_to_use) + 1);
                            }
                        }
                    }
                    while (r_1.next()) {
                        if (singlecolumn) {
                            c_result_l.add(r_1.getString(1));
                        } else {
                            if (numOfCol > 0) {
                                String result_tmp = null;
                                ArrayList<String> loaded_col_l = new ArrayList<>();
                                for (int j = 0; j < numOfCol; j++) {
                                    String c_col_name_to_use = table_nm + "." + rsmd.getColumnLabel(j + 1);
                                    c_col_name_to_use = c_col_name_to_use.toUpperCase();
                                    boolean skip = false;
                                    if (table2_id_col_count_map.containsKey(c_col_name_to_use) && table2_id_col_count_map.get(c_col_name_to_use) == 1) {
                                        skip = true;
                                    }
                                    String clmn_lable = rsmd.getColumnLabel(j + 1);
                                    String ful_col_nm = table_nm + "." + clmn_lable;
                                    if (!loaded_col_l.contains(ful_col_nm)) {
                                        loaded_col_l.add(ful_col_nm);
                                    } else {
                                        skip = true;
                                    }
                                    if (!skip) {
                                        if (result_tmp == null) {
                                            if (addcolumnnames) {
                                                result_tmp = table_nm + "." + clmn_lable + "=" + r_1.getString(j + 1);
                                            } else {
                                                result_tmp = r_1.getString(j + 1);
                                            }
                                        } else {
                                            if (addcolumnnames) {
                                                result_tmp = result_tmp + "||" + table_nm + "." + clmn_lable + "=" + r_1.getString(j + 1);
                                            } else {
                                                result_tmp = result_tmp + "||" + r_1.getString(j + 1);
                                            }
                                        }

                                    }
                                }
                                c_result_l.add(result_tmp);
                            }

                        }
                    }
                    if (!c_result_l.isEmpty()) {
//                        String[] c_reslt = c_result_l.remove(0).split("=");
//                        if (c_reslt.length == 2) {
//                            if (table2id_map.containsKey(c_reslt[0])) {
//                                table2id_map.put(c_reslt[0], new ArrayList<String>());
//                            }
//                            table2id_map.get(c_reslt[0]).add(c_reslt[1]);
//                        } else {
//                            if (table2id_map.containsKey("Error")) {
//                                table2id_map.put("Error", new ArrayList<String>());
//                            }
//                            table2id_map.get("Error").add(c_reslt[0]);
//                        }
                        final_result_l.addAll(c_result_l);

                    }
                    r_1.close();
                }
//                ArrayList<String> table2id_map_kl = new ArrayList<>(table2id_map.keySet());
//                StringBuilder url_b = new StringBuilder();
//                for (int i = 0; i < table2id_map_kl.size(); i++) {
//                    String c_tbl = table2id_map_kl.get(i).trim();
//                    if (c_tbl.toUpperCase().startsWith("ERROR")) {
//                        url_b.append(c_tbl);
//                        url_b.append(":");
//                        url_b.append(table2id_map.get(c_tbl));
//                    } else {
//                        url_b.append("URL(");
//                        url_b.append(table2id_map_kl.get(i));
//                        url_b.append(")||=");
//                        String static_link = servername + "Search/SearchResults3?" + Authenticate_service.TABLE_TO_USE_FLAG + table2id_map_kl.get(i) + "=" + "" + makeSTATIC_URL(table2id_map.get(c_tbl), c_tbl, docroot);
//
//                        String string = table2id_map_kl.get(i);
//                    }
//                }
//                result_l.add(0, url_l.toString().replace("[", "").replace("[", "").replaceAll(" , ", "||"));
                if (ncon != null && !ncon.isClosed()) {
                    ncon.close();
                }
            } else {
                msges = "Error GR10b: Conneciton failed | caller=" + caller;
//                if (table2id_map.containsKey("Error")) {
//                    table2id_map.put("Error", new ArrayList<String>());
//                }
//                table2id_map.get("Error").add(msges);

                System.out.println("Error GR10b: Conneciton failed " + last_query + "| caller=" + caller);
            }

        } catch (Exception ex) {
            msges = "Error GR10a: creating connections " + ex.getMessage();
            System.out.println("{Error GR10a: creating connections " + ex.getMessage() + "\t" + last_query + "| caller=" + caller + "}\n");
//            if (table2id_map.containsKey("Error")) {
//                table2id_map.put("Error", new ArrayList<String>());
//            }
//            table2id_map.get("Error").add(msges);
        } finally {
            try {
                if (ncon != null && !ncon.isClosed()) {
                    ncon.close();
                }
            } catch (SQLException ex) {
            }
        }
        return final_result_l;
    }

    public static String makeSTATIC_URL(ArrayList<Integer> id_l, String current_tbl_nm, String doc_root) {
        Collections.sort(id_l);
        String idlist = id_l.toString().replaceAll("[^0-9,]", "");
        String encripted_nm = encript("STATIC_URL_" + current_tbl_nm + "_" + idlist);
        File file = new File(doc_root + encripted_nm);
        if (file.isFile()) {
        } else {
            writeResultsToFile(current_tbl_nm + "||" + idlist, doc_root + encripted_nm);
        }

        return encripted_nm;
    }

    public static String makeSTATIC_URL(ArrayList<Integer> id_l, String current_tbl_nm) {
        ArrayList<Integer> sorted_l = new ArrayList<>(id_l.size());
        sorted_l.addAll(id_l);
        Collections.sort(sorted_l);
        String idlist = sorted_l.toString().replaceAll("[^0-9,]", "");
        sorted_l.clear();
        String encripted_nm = encript("STATIC_URL_" + current_tbl_nm + "_" + idlist);
        return encripted_nm;
    }

    private static boolean writeResultsToFile(Object indata, String file_nm) {
        boolean c_result = false;
        if (indata != null && file_nm != null) {
            try {
                FileOutputStream fos = new FileOutputStream(file_nm);
                ObjectOutputStream os;
                try {
                    os = new ObjectOutputStream(fos);
                    os.writeObject(indata);
                    os.close();
                    c_result = true;
                } catch (IOException ex) {
                    System.out.println("Error: " + ex.getMessage());
                }
            } catch (FileNotFoundException ex) {
                System.out.println("Error: " + ex.getMessage());
            }
        }
        return c_result;
    }

    private static String encript(String intext) {
        try {
            MessageDigest sha1 = MessageDigest.getInstance("SHA1");
            byte[] pass_bytes = intext.trim().getBytes();
            sha1.reset();
            sha1.update(pass_bytes);
            byte[] pass_digest = sha1.digest();
            Formatter formatter = new Formatter();
            for (byte b : pass_digest) {
                formatter.format("%02x", b);
            }
            String encript_pass = formatter.toString();
            return encript_pass;
        } catch (NoSuchAlgorithmException ex) {
        }
        return null;
    }

//    private ArrayList<ArrayList<String>> getpaths(String source, String target) {
//        ArrayList<ArrayList<String>> result = new ArrayList<>(1);
//        if (source.equals(target)) {
//            ArrayList<String> tmp = new ArrayList<>(2);
//            tmp.add(source);
//            tmp.add(source);
//            result.add(tmp);
//        } else {
//            if (connection_map == null) {
//                connection_map = new HashMap<>();
//                ArrayList<String> c_table_l = getTables();
//                for (int i = 0; i < c_table_l.size(); i++) {
//                    String c_taable = c_table_l.get(i);
////                    if(c_taable.toUpperCase().contains(TAGSOURCE_FLAG)){
////                        
////                    }
//                    HashMap<String, String[]> relat_map = Constants.get_key_contraints(c_taable, dataSource_data, Authenticate_service.DATABASE_NAME_DATA);
//                    String[] link_clmn_a = relat_map.get(Constants.FOREIGN_KEY_COLUMNS_FLAG);
//                    if (link_clmn_a != null) {
//                        for (int j = 0; j < link_clmn_a.length; j++) {
//                            String[] ref_a = relat_map.get(link_clmn_a[j]);
//                            if (ref_a != null && ref_a.length >= 3) {
//                                String foreing_tbl = ref_a[1];
//                                if (!c_taable.equals(foreing_tbl)) {
//                                    if (connection_map.containsKey(c_taable)) {
//                                        connection_map.get(c_taable).add(foreing_tbl);
//                                    } else {
//                                        ArrayList<String> tmp = new ArrayList<>(1);
//                                        tmp.add(foreing_tbl);
//                                        connection_map.put(c_taable, tmp);
//                                    }
//
//                                    if (!connection_map.containsKey(foreing_tbl)) {
//                                        ArrayList<String> tmp = new ArrayList<>(1);
//                                        connection_map.put(foreing_tbl, tmp);
//                                    }
//                                }
//                            }
//                        }
//                    }
//                }
//
//            }
//            PathFinder10 path = new PathFinder10();
//            String source_key = getRealKey(connection_map.keySet(), source);
//            String target_key = getRealKey(connection_map.keySet(), target);
//            if (target_key != null && source_key != null) {
//                result = path.getPaths(connection_map, getRealKey(connection_map.keySet(), source), getRealKey(connection_map.keySet(), target), 999);
//            }
//        }
//        return result;
//    }
//    public ArrayList<String> getTables() {
//        if (all_table_l == null) {
//            Connection ncon = null;
//            Statement st_1 = null;
//            ResultSet r_1 = null;
//            try {
//                all_table_l = new ArrayList<>(5);
//                String database_nm = getDB_name();
//                ncon = dataSource_data.getConnection();
//                if (!ncon.isClosed()) {
//                    DatabaseMetaData metaData = ncon.getMetaData();
//                    r_1 = metaData.getTables(null, null, null, new String[]{"TABLE"});
//                    while (r_1.next()) {
//                        String table_nm = r_1.getString("TABLE_NAME");
//                        String schema_nm = r_1.getString("TABLE_SCHEM");
//                        if (schema_nm.equals(database_nm)) {
//                            all_table_l.add(schema_nm + "." + table_nm);
//                        }
//                    }
//                }
//                close(ncon, st_1, r_1);
//            } catch (SQLException ex) {
//                System.out.println("Error AS23a" + ex.getMessage());
//            } finally {
//                close(ncon, st_1, r_1);
//            }
//        }
//        return all_table_l;
//    }
    private Object readResultsFromFile(String filename) {
        File test = new File(filename);
        if (test.isFile() && test.canRead()) {
            Object auth_ob = null;
            try {
                FileInputStream in = new FileInputStream(filename);
                ObjectInputStream obin = new ObjectInputStream(in);
                auth_ob = obin.readObject();
                obin.close();
            } catch (StreamCorruptedException ex) {
                System.out.println("Error s7A: Security exception, the authentication file is corrupted or has been modified by third party");
            } catch (ClassNotFoundException ex) {
                System.out.println("Error s7e: accessing authentication file");
            } catch (IOException ex) {
                System.out.println("Error s7d: accessing authentication file " + ex.getMessage());
            }
            try {
                if (auth_ob != null) {
                    return auth_ob;
                }
            } catch (Exception ex) {
                System.out.println("Error s7B accessing authentication file");
            }
        }
        return null;
    }

    private String getRealKey(Set keys, String key) {
        ArrayList<String> key_l = new ArrayList<>(keys);
        key = key.split("\\.")[key.split("\\.").length - 1].toUpperCase();
        String realKey = null;
        for (int i = 0; (i < key_l.size() && realKey == null); i++) {
            String c_key = key_l.get(i).split("\\.")[key_l.get(i).split("\\.").length - 1].toUpperCase();
            if (c_key.equalsIgnoreCase(key)) {
                realKey = key_l.get(i);
            }
        }
        return realKey;
    }

//    public String get_correct_table_name(String in_name) {
//        if (in_name != null) {
//            in_name = in_name.toUpperCase();
//            if (Constants.corect_tbl_nm_map == null || !Constants.corect_tbl_nm_map.containsKey(in_name)) {
//                Constants.corect_tbl_nm_map = new HashMap<>();
//                ArrayList<String> getTables = getTables();
//                if (getTables.contains(in_name)) {
//                    Constants.corect_tbl_nm_map.put(in_name, in_name);
//                } else {
//                    String tr_in_name = in_name.split("\\.")[in_name.split("\\.").length - 1];
//                    for (int i = 0; i < getTables.size(); i++) {
//                        String c_tbl = getTables.get(i).toUpperCase();
//                        String striped_ctbl = c_tbl.split("\\.")[c_tbl.split("\\.").length - 1];
//                        if (c_tbl.equals(tr_in_name)
//                                || striped_ctbl.equals(tr_in_name)
//                                || striped_ctbl.equals(tr_in_name + "S")
//                                || (striped_ctbl + "S").equals(tr_in_name)) {
//                            Constants.corect_tbl_nm_map.put(in_name, getTables.get(i));
//                        }
//                    }
//                }
//
//            }
//            return Constants.corect_tbl_nm_map.get(in_name);
//        }
//        return null;
//
//    }
//    public String getDB_name() {
//        if (Authenticate_service.DATABASE_NAME_DATA == null) {
//            Connection ncon = null;
//            try {
//                ncon = dataSource_data.getConnection();
//                String c_url = ncon.getMetaData().getURL();
//                ncon.close();
//                Authenticate_service.DATABASE_NAME_DATA = c_url.split("/")[c_url.split("/").length - 1];
//            } catch (SQLException ex) {
//                System.out.println("Error " + ex.getMessage());
//            } finally {
//                close(ncon, null, null);
//            }
//            if (Authenticate_service.DATABASE_NAME_DATA == null) {
//                Authenticate_service.DATABASE_NAME_DATA = "EGEN_DATAENTRY";
//            }
//        }
//        return Authenticate_service.DATABASE_NAME_DATA;
//    }
////
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
}
