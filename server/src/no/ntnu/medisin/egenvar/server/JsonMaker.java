/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package no.ntnu.medisin.egenvar.server;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Formatter;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author sabryr
 */
public class JsonMaker implements Runnable {
    
    private Connection conn = null;
    private String dbURL_dataEntry;
    private String databse_NAME_DATA = null;
    private String docroot;
    private final String CHLD_HOLDER = "CHLD_";
    private String service_name;
    private String search_result_page;
    private boolean done;
    private HashMap<String, String> name2url_map;
    private String docroot_url;
    /*
     MethodID=JM0
     */
    
    public JsonMaker(String dbURL_dataEntry, String docroot, String service_name, String docroot_url) {
        this.dbURL_dataEntry = dbURL_dataEntry;
        this.docroot = docroot;
        this.service_name = service_name;
        this.docroot_url = docroot_url;
        search_result_page = service_name + "Search/SearchResults3?" + Start_EgenVar1.TAGSOURCE_FLAG;
        
    }
    /*
     MethodID=JM1
     */
    
    @Override
    public void run() {
        System.out.println("Creating JSON started @" + Timing.getDateTime());
        done = false;
        commit();
        done = true;
    }
    
    public boolean isDone() {
        while (!done) {
            try {
                Thread.sleep(3000);
            } catch (InterruptedException ex) {
            }
        }
        return true;
    }

    /*
     MethodID=JM3
     */
    private void commit() {
        report_hierarchy();
        tag_hierarchy();
        refreshJasonList();
    }
    /*
     MethodID=JM2
     */
    
    private void report_hierarchy() {
        int incrementer = 0;
        if (createConnection()) {
            if (name2url_map == null) {
                name2url_map = new HashMap<>();
            }
            try {
                Statement stm = conn.createStatement();
                String rb = get_correct_table_name("report_batch");
                String r = get_correct_table_name("report");
                String rh = get_correct_table_name("REPORT_HIERARCHY");
                String f = get_correct_table_name("files");
                String f2r = get_correct_table_name("files2report");
                String rb_h = get_correct_table_name("REPORT_BATCH_HIERARCHY");
                String sq_rb = "Select id, name from " + rb;
                String sq_r = "Select id,name, report_batch_id from " + r;
                String sq_rh = "Select PARENTREPORT_ID,CHILDREPORT_ID from " + rh;
                String sq_rb_h = "Select PARENTREPORT_BATCH_ID,CHILDREPORT_BATCH_ID from " + rb_h;
                
                HashMap<Integer, ArrayList<String>> report_batchtaggedInfo = gettaggedInfo(stm, rb, "report_batch_id");
                HashMap<Integer, ArrayList<String>> report_taggedInfo = gettaggedInfo(stm, r, "report_id");
                
                HashMap<Integer, String> reportBatch_id2name_map = new HashMap<>();
                HashMap<Integer, String> report_id2name_map = new HashMap<>();
                HashMap<Integer, String> report2batch_map = new HashMap<>();
                ResultSet r_rb = stm.executeQuery(sq_rb);
                HashMap<String, EGENV_Json> all_map = new HashMap<>();
                while (r_rb.next()) {
                    reportBatch_id2name_map.put(r_rb.getInt(1), r_rb.getString(2));
                }
                r_rb.close();
                ResultSet r_r = stm.executeQuery(sq_r);
                int cc = 0;
                while (r_r.next()) {
                    cc++;
                    int report_id = r_r.getInt(1);
                    String rep_nm = r_r.getString(2);
                    int report_batch_id = r_r.getInt(3);
                    report_id2name_map.put(report_id, rep_nm);
                    String batch_name_name = reportBatch_id2name_map.get(report_batch_id);
                    String c_parent_key = "REPORT_BATCH=" + report_batch_id;
                    String url = makeSTATIC_URL(report_batch_id + "", "REPORT_BATCH");
                    if (!all_map.containsKey(c_parent_key)) {
                        HashMap<String, String> atrib_map = new HashMap<>();
                        atrib_map.put("name", batch_name_name);
                        atrib_map.put("url", url);
                        if (report_batchtaggedInfo.containsKey(report_batch_id)) {
                            atrib_map.put("used", "1");
                        } else {
                            atrib_map.put("used", "0");
                        }
                        if (report_batchtaggedInfo.containsKey(report_batch_id)) {
                            atrib_map.put("count", report_batchtaggedInfo.get(report_batch_id).toString().replace("[", "").replace("]", ""));
                        } else {
                            atrib_map.put("count", "");
                        }
                        
                        EGENV_Json c_batch = new EGENV_Json(c_parent_key, atrib_map);
                        all_map.put(c_parent_key, c_batch);
                    }
                    String c_child_key = "REPORT=" + report_id;
                    String child_url = makeSTATIC_URL(report_id + "", "REPORT");
                    HashMap<String, String> atrib_map = new HashMap<>();
                    atrib_map.put("name", rep_nm);
                    atrib_map.put("url", child_url);
                    if (report_taggedInfo.containsKey(report_id)) {
                        atrib_map.put("used", "1");
//                         System.out.println("157 "+report_id+"  "+report_taggedInfo.get(report_id).toString().replace("[", "").replace("]", ""));
                    } else {
                        atrib_map.put("used", "0");
                    }
                    if (report_taggedInfo.containsKey(report_id)) {
                        atrib_map.put("count", report_taggedInfo.get(report_id).toString().replace("[", "").replace("]", ""));
//                        System.out.println("163 "+report_id+"  "+report_taggedInfo.get(report_id).toString().replace("[", "").replace("]", ""));
                    } else {
                        atrib_map.put("count", "");
                    }
                    
                    EGENV_Json c_report = new EGENV_Json(c_child_key, atrib_map);
                    String sql_f = "select files_id from " + f2r + "  where " + f2r + ".report_id=" + report_id;

//                    String sql_f = "select id from " + f + " where " + f + ".id in (select files_id from " + f2r + "  where " + f2r + ".report_id=" + report_id + ") ";
                    ArrayList<Integer> fileid_l = getFileNames_links(sql_f);
                    String file_url = makeSTATIC_URL(fileid_l, "files");
                    HashMap<String, String> atrib_map2 = new HashMap<>();
                    atrib_map2.put("name", fileid_l.size() + "_files");
                    atrib_map2.put("url", file_url);
                    EGENV_Json c_files = new EGENV_Json(incrementer + "", atrib_map2);
                    incrementer++;
                    c_report.addChild(c_files);
                    all_map.get(c_parent_key).addChild(c_report);
                    report2batch_map.put(report_id, c_parent_key);
                }
                r_r.close();
                
                ResultSet r_rh = stm.executeQuery(sq_rh);
                while (r_rh.next()) {
                    cc++;
//                    if (cc % 500 == 0) {
//                        System.out.println(".");
//                    }
                    int parent_id = r_rh.getInt(1);
                    int child_id = r_rh.getInt(2);
                    String c_parent_key = "REPORT=" + parent_id;
                    String c_child_key = "REPORT=" + child_id;
                    String child_url = makeSTATIC_URL(child_id + "", "REPORT");
                    String parent_url = makeSTATIC_URL(parent_id + "", "REPORT");
                    addChild_2(parent_id, child_id, c_parent_key, c_child_key,
                            all_map, report_id2name_map, child_url, parent_url);
                }
                r_rh.close();
                
                ResultSet r_rb_h = stm.executeQuery(sq_rb_h);
                while (r_rb_h.next()) {
                    cc++;
//                    if (cc % 500 == 0) {
//                        System.out.println(".");
//                    }
                    int parent_report_batch_id = r_rb_h.getInt(1);
                    int child_report_batch_id = r_rb_h.getInt(2);
                    String c_parent_key = "REPORT_BATCH=" + parent_report_batch_id;
                    String c_child_key = "REPORT_BATCH=" + child_report_batch_id;
                    String child_url = makeSTATIC_URL(child_report_batch_id + "", "REPORT_BATCH");
                    String parent_url = makeSTATIC_URL(parent_report_batch_id + "", "REPORT_BATCH");
                    addChild_2(parent_report_batch_id, child_report_batch_id, c_parent_key,
                            c_child_key, all_map, reportBatch_id2name_map, child_url, parent_url);
                }
                r_rb_h.close();
                stm.close();
                HashMap<String, String> atrib_map2 = new HashMap<>();
                atrib_map2.put("name", "ROOT");
                atrib_map2.put("url", "Tingeling");
                EGENV_Json root = new EGENV_Json("ROOT", atrib_map2);
                ArrayList<String> key_l = new ArrayList<>(all_map.keySet());
//                int last_completion = 0;
                for (int i = 0; i < key_l.size(); i++) {
//                    int completion = ((i * 100) / key_l.size());
//                    if (completion > last_completion && completion % 20 == 0) {
//                        System.out.print("." + completion + "% ");
//                        last_completion = completion;
//                    } else if (completion > last_completion && completion % 2 == 0) {
//                        System.out.print(".");
//                    }
                    root.addChild(all_map.get(key_l.get(i)));
                }

                FileWriter jsonFile = null;
                try {
                    jsonFile = new FileWriter(docroot + "reports.json", false);
                    jsonFile.append(root.toString());
//                    System.out.println("Writing to " + docroot_url + "reports.json");
                    name2url_map.put("REPORT", docroot_url + "reports.json");
                    jsonFile.close();
                } catch (IOException ex) {
                    ex.printStackTrace();
                } finally {
                    try {
                        if (jsonFile != null) {
                            jsonFile.close();
                        }
                    } catch (IOException ex) {
                    }
                }
                
                //                System.out.println("235  \n\n\n\n\n\n " + root.getchildren().size());
                ArrayList<EGENV_Json> all_child = root.getchildren();
                HashMap<String, String> child2parent = new HashMap<>();
                HashMap<String, ArrayList<String>> child2All_parent = new HashMap<>();
                int safety = 1000000;
                while (!all_child.isEmpty() && safety > 0) {
                    safety--;
                    ArrayList<EGENV_Json> new_child = new ArrayList<>();
                    for (int i = 0; i < all_child.size(); i++) {
                        EGENV_Json c_Json = all_child.get(i);
                        new_child.addAll(c_Json.getchildren());
                        child2parent.put(c_Json.getId(), c_Json.getParent().getId());
                    }
                    all_child.clear();
                    all_child.addAll(new_child);
                }
                ArrayList<String> k_l = new ArrayList<>(child2parent.keySet());
                for (int i = 0; i < k_l.size(); i++) {
                    ArrayList<String> n_p_l = new ArrayList<>();
                    String c_p = child2parent.get(k_l.get(i));
                    while (c_p != null) {
                        if (!n_p_l.contains(c_p)) {
                            n_p_l.add(c_p);
                            c_p = child2parent.get(c_p);
                        } else {
                            c_p = null;
                        }
                    }
                    n_p_l.remove("ROOT");
                    n_p_l.add(0,k_l.get(i));
                    child2All_parent.put(k_l.get(i), n_p_l);
                }
                populateReportLineage(conn, child2All_parent);
//                for (int i = 0; i < 10; i++) {
//                    System.out.println("270 " + k_l.get(i) + " => " + child2All_parent.get(k_l.get(i)));
//                }
//                System.out.println("259 \n\n\n\n\n\n");

//                System.out.println("234 "+all_map.size());
                System.out.println("JSON processing finished.");
            } catch (SQLException ex) {
                System.out.println("Error JM2b: " + ex.getMessage());
            }
        } else {
            System.out.println("Error JM2a: connection failed ");
        }
    }
    
    private HashMap<Integer, ArrayList<String>> gettaggedInfo(Statement stm, String table_nm, String forgn_key_col) {
        HashMap<Integer, ArrayList<String>> id2tag_map = new HashMap<>();
        String tag_tbl = get_correct_table_name(table_nm + "2TAGS");
        String sql = null;
        if (tag_tbl != null) {
            try {
                sql = "select " + forgn_key_col + ", name from " + tag_tbl;
                ResultSet r = stm.executeQuery(sql);
                while (r.next()) {
                    int c_id = r.getInt(1);
                    if (!id2tag_map.containsKey(c_id)) {
                        id2tag_map.put(c_id, new ArrayList<String>());
                    }
                    id2tag_map.get(c_id).add(r.getString(2));
                }
                r.close();
            } catch (SQLException ex) {
                System.out.println("JM4 Error:" + ex.getMessage() + "\t" + sql);
            }
            
        } else {
            System.out.println("Error: tag table " + table_nm + "2TAGS" + " was not found");
        }
        return id2tag_map;
        
    }
    
    private void tag_hierarchy() {
        Timing.setPointer2();
//          EGENV_Json root = new EGENV_Json("ROOT", "name", "All_Tags");
//          id2name_map.put(0, "All_Tags");
//          all_map.put(0+"", "No ");
        if (createConnection()) {
            if (name2url_map == null) {
                name2url_map = new HashMap<>();
            }
            try {
                Statement stm = conn.createStatement();
                ArrayList<String> tag_tables_l = getCurrentTables();
                HashMap<String, HashMap<String, Integer>> feature2count_map = new HashMap<>();
//                HashMap<String, HashMap<Integer, Boolean>> isUsed_map = new HashMap<>();

                for (int i = 0; (i < tag_tables_l.size() && !tag_tables_l.isEmpty()); i++) {
                    String c_tbl = tag_tables_l.get(i);
                    if (c_tbl.toUpperCase().endsWith("2TAGS")) {//  
                        tag_tables_l.remove(i);
                        i--;
//                        if (c_target != null) {
                        String sql = "select LINK_TO_FEATURE, count(id) as idc from " + c_tbl + " group by LINK_TO_FEATURE ";
//                        System.out.println("312 " + sql);
                        ResultSet r_1 = stm.executeQuery(sql);
//                        int limit =10;
                        while (r_1.next()) {
//                            limit--;                            
                            String c_link = r_1.getString(1); //HUMAN_DISEASE_ONTOLOGY_TAGSOURCE|HASH=32CB4D15E7630AE9A42FEF357ECC7DCDB3A909AB
//if(c_tbl.toUpperCase().contains("REPORT")){
//    System.out.println("319 "+c_link);
//}
                            if (c_link != null) {
                                int eq_indx = c_link.indexOf("=");
                                int pip_indx = c_link.indexOf("|");
//                                if(limit>0) System.out.println("318 "+c_link+"  eq_indx="+eq_indx+"\t"+c_link.substring(eq_indx + 1));
                                if (pip_indx > 2) {
                                    String table = get_correct_table_name(c_link.substring(0, pip_indx));
//                                    System.out.println("327 "+table);
                                    if (table != null) {
                                        if (!feature2count_map.containsKey(table)) {
                                            feature2count_map.put(table, new HashMap<String, Integer>());
                                        }
                                        if (eq_indx > 2) {
//                                            select LINK_TO_FEATURE, id from EGEN_DATAENTRY.FILES2TAGS where LINK_TO_FEATURE like '%4C07CECE01D301DAA5A9014472A26E5934DE70AB%'
                                            feature2count_map.get(table).put(c_link.substring(eq_indx + 1), r_1.getInt(2));
//                                            if (c_link.substring(eq_indx + 1).equals("4C07CECE01D301DAA5A9014472A26E5934DE70AB")) {
//                                                System.out.println("\n\n\n331 " + feature2count_map.get(table).get(c_link.substring(eq_indx + 1)));
//                                            }
                                        } else {
                                            feature2count_map.get(table).put(c_link.substring(pip_indx + 1), r_1.getInt(2));
                                        }
                                    } else {
//                                        System.out.println("335 table null "+c_link.substring(0, pip_indx));
                                    }
                                }
//                                String[] split = c_link.split("=");
//                                if (split.length == 2) {
//                                    String table = get_correct_table_name(split[0]);
//                                    String hash = split[1].trim();
//                                    if (id_str.matches("[0-9]+")) {
//                                      
////                                        if (!feature2count_map.containsKey(table)) {
////                                            feature2count_map.put(table, new HashMap<Integer, Integer>());
////                                        }
//                                        feature2count_map.get(table).put(new Integer(id_str), r_1.getInt(2));
//                                    }
//                                }
                            }
                        }
//                        System.out.println("\n\n346 "+feature2count_map.keySet()+ "\t "+feature2count_map.get("EGEN_DATAENTRY.HUMAN_DISEASE_ONTOLOGY_TAGSOURCE"));
//                            if (!target_tables_map.containsKey(c_tbl)) {
//                                target_tables_map.put(c_tbl, new ArrayList<String>());
//                            }
//                            String link_nm = c_tbl.split("\\.")[c_tbl.split("\\.").length - 1] + "=";
//                            String ct_sql = "select count(id) from  " + c_target + " Where LINK_TO_FEATURE=" + link_nm + "?";
//                            target_tables_map.get(c_tbl).add(ct_sql);
//                        }
                    } else if (!c_tbl.toUpperCase().endsWith("_TAGSOURCE")) {//DISEASE_ONTOLOGY  BIOLOGICAL_ONTOLOGIES_TAGSOURCE
                        tag_tables_l.remove(i);
                        i--;
                    }
                }
                ArrayList<String> feature2count_k_l = new ArrayList<>(feature2count_map.keySet());
                HashMap<String, Set> evaluated_map = new HashMap<>();
                for (int i = 0; i < feature2count_k_l.size(); i++) {
                    Set evaluated_s = new HashSet();
                    String c_tbl = feature2count_k_l.get(i);
                    boolean conpleted = false;
                    ArrayList<String> hash_l = new ArrayList<>(feature2count_map.get(c_tbl).keySet());
                    String sql = "select id,parent_id from " + c_tbl + " where hash in ('"
                            + hash_l.toString().replaceAll(", ", "','").replace("[", "").replace("]", "") + "')";
//                    System.out.println("368 "+sql);
                    ArrayList<Integer> id_l = new ArrayList<>();
                    while (!conpleted) {
//                        System.out.println("295 "+sql);
                        evaluated_s.addAll(id_l);
                        id_l.clear();
                        ResultSet r_1 = stm.executeQuery(sql);
                        while (r_1.next()) {
                            evaluated_s.add(r_1.getInt(1));
                            int new_id = r_1.getInt(2);
                            if (!evaluated_s.contains(new_id)) {
                                id_l.add(new_id);
                            }
                        }
                        if (id_l.isEmpty()) {
                            conpleted = true;
                        } else {
                            sql = "select id,parent_id from " + c_tbl + " where id in (" + id_l.toString().replaceAll("\\s", "").replace("[", "").replace("]", "") + ")";
                        }
                    }
                    evaluated_map.put(c_tbl, evaluated_s);
                }
//                System.out.println("\n\n396 " + featumre2count_map.keySet() + "\t " + feature2count_map.get("EGEN_DATAENTRY.HUMAN_DISEASE_ONTOLOGY_TAGSOURCE").get("4C07CECE01D301DAA5A9014472A26E5934DE70AB") + " |");
//                for (int i = 0; i < tag_tables_l.size(); i++) {
//                    if (!tag_tables_l.get(i).toUpperCase().contains("EXPERIMENTAL")) {
//                        tag_tables_l.remove(i);
//                        i--;
//                    }
//                }

                for (int i = 0; i < tag_tables_l.size(); i++) {
                    String c_tbl = tag_tables_l.get(i);
                    Set evaluated_s = evaluated_map.get(c_tbl);
                    if (evaluated_s == null) {
                        evaluated_s = new HashSet();
                    }
                    HashMap<String, EGENV_Json> all_map = new HashMap<>();
                    HashMap<Integer, String> id2name_map = new HashMap<>();
                    HashMap<Integer, String> id2hash_map = new HashMap<>();
//                    String test ="4C07CECE01D301DAA5A9014472A26E5934DE70AB";
                    String select_sql = "select id, parent_id, name, HASH from " + c_tbl + " order by parent_id ";// order by parent_id"; //where parent_id<10 
//                    System.out.println("410 " + select_sql);
                    ResultSet r_1 = stm.executeQuery(select_sql);
                    
                    int min_id = Integer.MAX_VALUE;
                    while (r_1.next()) {
                        int c_id = r_1.getInt(1);
                        String c_name = r_1.getString(3);
                        id2name_map.put(c_id, c_name);
                        if (c_id < min_id) {
                            min_id = c_id;
                        }
                        String hash = r_1.getString(4);
                        id2hash_map.put(c_id, hash);
//                        System.out.println("422 c_id=" + c_id + " Hash=|" + hash+"|"+hash.equalsIgnoreCase(test)+" "+hash.indexOf(test));
//                        if (hash.equalsIgnoreCase(test)) {
//                            System.out.println("424 c_id=" + c_id + " Hash=" + hash);
//                        }
                    }
//                    System.out.println("386 " + id2name_map);
                    r_1.close();
                    r_1 = stm.executeQuery(select_sql);
                    
                    HashMap<String, String> atrib_map2 = new HashMap<>();
                    atrib_map2.put("name", "ALL");
                    atrib_map2.put("url", null);
                    EGENV_Json root = new EGENV_Json("ROOT", atrib_map2);
                    all_map.put("ROOT", root);
                    int limit = 10;
//                    System.out.println("443 " + evaluated_s);
                    while (r_1.next()) {
                        int c_id = r_1.getInt(1);
                        int parent_id = r_1.getInt(2);
                        String hash = r_1.getString(4);
                        String c_child_key = "" + c_id;// c_tbl + "=" + c_id
                        String c_parent_key = "" + parent_id;//c_tbl + "=" + parent_id
                        String child_url = makeSTATIC_URL(c_child_key + "", c_tbl);
                        String parent_url = makeSTATIC_URL(parent_id + "", c_tbl);
                        HashMap<String, String> atrib_parent = new HashMap<>();
                        atrib_parent.put("name", id2name_map.get(parent_id));
                        atrib_parent.put("url", parent_url);
                        if (evaluated_s.contains(parent_id)) {
                            atrib_parent.put("used", "1");
                        } else {
                            atrib_parent.put("used", "0");
                        }
                        if (id2hash_map.containsKey(parent_id)) {
                            if (feature2count_map.containsKey(c_tbl) && feature2count_map.get(c_tbl).containsKey(id2hash_map.get(parent_id))) {
                                atrib_parent.put("count", feature2count_map.get(c_tbl).get(id2hash_map.get(parent_id)) + "");
                            } else {
                                atrib_parent.put("count", "0");
                            }
                        }
//                        System.out.println("473 "+id2name_map.get(c_id));
                        HashMap<String, String> atrib_child = new HashMap<>();
                        atrib_child.put("name", id2name_map.get(c_id));
                        atrib_child.put("url", child_url);
                        if (evaluated_s.contains(c_id)) {
                            atrib_child.put("used", "1");
//                            System.out.println("\t472 used =" + c_tbl + "\t" + c_id + "  " + id2name_map.get(c_id));
                        } else {
                            atrib_child.put("used", "0");
                        }
                        if (feature2count_map.containsKey(c_tbl) && feature2count_map.get(c_tbl).containsKey(hash)) {
                            atrib_child.put("count", feature2count_map.get(c_tbl).get(hash) + "");
                        } else {
                            atrib_child.put("count", "0");
                        }
                        if (c_id == min_id) {
//                            System.out.println("431 resetting root " + id2name_map.get(c_id));
                            root.setName(id2name_map.get(c_id));
                            if (!all_map.containsKey("ROOT")) {
                                all_map.put("ROOT", root);
                            } else {
                                all_map.get("ROOT").setName(id2name_map.get(c_id));
                            }
//                                addChild(root.getId(), c_child_key, all_map, atrib_child, atrib_map2);
                        } else if (parent_id != min_id) {
                            if (atrib_child.get("used").equals("1")) {
//                    System.out.println("  565 addinfg used "+c_parent_key+" to "+ c_child_key);
                            }
                            addChild(c_parent_key, c_child_key, all_map, atrib_child, atrib_parent);
                        } else if (parent_id == min_id) {
                            addChild(root.getId(), c_child_key, all_map, atrib_child, null);
                        }
//                        if (parent_id == c_id) {
//                            System.out.println("431 resetting root " + id2name_map.get(c_id));
//                            root.setName(id2name_map.get(c_id));
//                            if (!all_map.containsKey("BROOT")) {
//                                all_map.put("BROOT", root);
//                            } else {
//                                all_map.get("BROOT").setName(id2name_map.get(c_id));
//                            }
//                        } else if (parent_id != min_id) {
//                            addChild(c_parent_key, c_child_key, all_map, atrib_child, atrib_parent);
//                        } else {
//                            addChild(root.getId(), c_child_key, all_map, atrib_child, atrib_map2);
//                        }

//                        } else {
////                            System.out.println("425 "+parent_id+"\t"+id2name_map.get(c_id));
//                        }
                    }
//                    System.out.println("429 " + all_map);
                    r_1.close();

//                    ArrayList<String> key_l = new ArrayList<>(all_map.keySet());
//                    for (int j = 0; j < key_l.size(); j++) {//
//                        if (all_map.get(key_l.get(j)).getParent() == null) {
//                            root.addChild(all_map.get(key_l.get(j)));
//                        }
////                        else {
//////                            System.out.println("440 none nukl parent " + all_map.get(key_l.get(j)) + " \t " + all_map.get(key_l.get(j)).getParent());
////                        }
//                    }
//                    HashMap<String, String> base_root_atrib_map = new HashMap<>();
//                    base_root_atrib_map.put("name", "ROOT");
//                    base_root_atrib_map.put("url", null);
//                    EGENV_Json base_root = new EGENV_Json("ROOT", base_root_atrib_map);
//                    base_root.addChild(root);
                    FileWriter jsonFile = null;
                    try {
                        c_tbl = c_tbl.split("\\.")[c_tbl.split("\\.").length - 1];
                        jsonFile = new FileWriter(docroot + c_tbl + "_tags.json", false);
                        jsonFile.append(root.toString());
//                        System.out.println("Writing to " + docroot_url + c_tbl + "_tags.json");
                        name2url_map.put(c_tbl, docroot_url + c_tbl + "_tags.json");
                        jsonFile.close();
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    } finally {
                        try {
                            if (jsonFile != null) {
                                jsonFile.close();
                            }
                        } catch (IOException ex) {
                        }
                    }
                }
                System.out.println("Finished " + Timing.convert(Timing.getFromlastPointer2()));
                stm.close();
            } catch (SQLException ex) {
                System.out.println("Error JM2b: " + ex.getMessage());
            }
        } else {
            System.out.println("Error JM2a: connection failed ");
        }
    }

    /*
     */
    private void addChild(String c_parent_key, String c_child_key,
            HashMap<String, EGENV_Json> all_map,
            HashMap<String, String> atrib_child,
            HashMap<String, String> atrib_parent) {
        if (c_parent_key.equals(c_child_key)) {
            System.out.println("Error 491 Parent=child for key=" + c_child_key + " -- avoiding");
        } else {
            EGENV_Json parent_json;
            if (all_map.containsKey(c_parent_key)) {
                parent_json = all_map.get(c_parent_key);
            } else {
//            HashMap<String, String> atrib_map2 = new HashMap<>();
//            atrib_map2.put("name", id2name_map.get(parent_id));
//            atrib_map2.put("url", child_url);
                parent_json = new EGENV_Json(c_parent_key, atrib_parent);
                all_map.put(c_parent_key, parent_json);
            }
            
            EGENV_Json child_json;
            if (all_map.containsKey(c_child_key)) {
                child_json = all_map.get(c_child_key);
            } else {
//            HashMap<String, String> atrib_map2 = new HashMap<>();
//            atrib_map2.put("name", id2name_map.get(child_id));
//            atrib_map2.put("url", child_url);               
                child_json = new EGENV_Json(c_child_key, atrib_child);
                all_map.put(c_child_key, child_json);
            }
            parent_json.addChild(child_json);
        }
        
    }
    
    private void addChild_2(int parent_id, int child_id, String c_parent_key, String c_child_key,
            HashMap<String, EGENV_Json> all_map, HashMap<Integer, String> id2name_map,
            String child_url, String parent_url) {
        EGENV_Json parent_json = null;
        if (all_map.containsKey(c_parent_key)) {
            parent_json = all_map.get(c_parent_key);
        } else {
            ArrayList<String> key_l = new ArrayList<>(all_map.keySet());
            for (int i = 0; (i < key_l.size() && parent_json == null); i++) {
                parent_json = all_map.get(key_l.get(i)).getParentFromChildren(c_parent_key);
            }
        }
        if (parent_json == null && id2name_map.containsKey(parent_id)) {
            
            HashMap<String, String> atrib_map2 = new HashMap<>();
            atrib_map2.put("name", id2name_map.get(parent_id));
            atrib_map2.put("url", parent_url);
            
            parent_json = new EGENV_Json(c_parent_key, atrib_map2);
            all_map.put(c_parent_key, parent_json);
        }
        EGENV_Json child_json = null;
        if (all_map.containsKey(c_child_key)) {
            child_json = all_map.remove(c_child_key);
        } else {
            ArrayList<String> key_l = new ArrayList<>(all_map.keySet());
            for (int i = 0; (child_json == null && i < key_l.size()); i++) {
                child_json = all_map.get(key_l.get(i)).getParentFromChildren(c_child_key);
            }
            if (child_json == null) {
                HashMap<String, String> atrib_map2 = new HashMap<>();
                atrib_map2.put("name", id2name_map.get(child_id));
                atrib_map2.put("url", child_url);
                child_json = new EGENV_Json(c_child_key, atrib_map2);
            } else {
                all_map.remove(c_child_key);
            }
        }
        if (child_json.parent() != null) {
            child_json.parent().removeChild(child_json);
        }
        if (parent_json != null) {
            parent_json.addChild(child_json);
            all_map.remove(c_child_key);
        } else {
            all_map.put(c_child_key, child_json);
        }
    }


    /*
     MethodID=JM10
     */
    private ArrayList<Integer> getFileNames_links(String sql) {
        
        ArrayList<Integer> fileid_l = new ArrayList<>();
        if (createConnection()) {
            try {
                Statement stm = conn.createStatement();
                ResultSet r = stm.executeQuery(sql);
                while (r.next()) {
                    fileid_l.add(r.getInt(1));
                }
                r.close();
                stm.close();
            } catch (SQLException ex) {
                System.out.println("Error JM10b: " + ex.getMessage());
            }
        }
//        String encript_name = \\"<a href=\\\"" + search_result_page + "FILES=" + makeSTATIC_URL(fileid_l, "FILES", docroot) + "\\\">"+fileid_l.size() + \" Files (click for details)\"+"</a>;        
//       String encript_name ="<a href=\"" + search_result_page + "FILES=" + makeSTATIC_URL(fileid_l, "FILES", docroot) + "\">"+fileid_l.size() + " Files (click for details)"+"</a>";
//        String encript_name ="<a href=\\\"" + search_result_page + "FILES=" + makeSTATIC_URL(fileid_l, "FILES", docroot) + "\\\">"+fileid_l.size() + " Files (click for details)"+"</a>";
//        String encript_name = fileid_l.size() + " Files";

        return fileid_l;
    }

    /**
     * MethodID=JM4
     *
     */
    public String get_correct_table_name(String table_nm) {
        String result = null;
        try {
            table_nm = table_nm.split("\\.")[table_nm.split("\\.").length - 1];
            ArrayList<String> c_tables_l = getCurrentTables();
            if (c_tables_l.contains(table_nm)) {
                result = table_nm;
            } else {
                boolean not_found = true;
                for (int i = 0; (i < c_tables_l.size() && not_found); i++) {
                    String c_nm = c_tables_l.get(i);
                    c_nm = c_nm.split("\\.")[c_nm.split("\\.").length - 1];
                    if (c_nm.equalsIgnoreCase(table_nm)) {
                        result = c_tables_l.get(i);
                        not_found = false;
                    }
                }
            }
        } catch (Exception ex) {
            System.out.println("218 " + ex.getMessage());
        }
        return result;
    }

    /**
     * MethodID=JM5
     *
     */
    private ArrayList<String> getCurrentTables() {
        ArrayList<String> result_l = new ArrayList<>(20);
        if (createConnection()) {
            try {
                Statement stmt = conn.createStatement();
                String q = "select TABLENAME from sys.systables WHERE UPPER(CAST(TABLETYPE AS CHAR(1))) = 'T' "
                        + "and SCHEMAID=(select SCHEMAID from SYS.SYSSCHEMAS where UPPER(CAST(SCHEMANAME AS VARCHAR(128)))=UPPER('" + getDB_name(conn) + "'))";
                ResultSet r_1 = stmt.executeQuery(q);
                while (r_1.next()) {
                    result_l.add(getDB_name(conn).toUpperCase() + "." + r_1.getString(1));
                }
                r_1.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        } else {
            System.out.println("Error sym10: Establishing connection failed");
        }
        return result_l;
    }

    /*
     MethodID=sym7
     */
    public String getDB_name(Connection c_con) {
        if (databse_NAME_DATA == null) {
            if (createConnection()) {
                try {
                    String c_url = c_con.getMetaData().getURL();
                    databse_NAME_DATA = c_url.split("/")[c_url.split("/").length - 1];
                    databse_NAME_DATA = databse_NAME_DATA.split(";")[0];
                } catch (SQLException ex) {
                    System.out.println("Error sym7a" + ex.getMessage());
                }
                if (databse_NAME_DATA == null) {
                    databse_NAME_DATA = "EGEN_DATAENTRY";
                }
            }
        }
        return databse_NAME_DATA;
    }
    
    private String makeSTATIC_URL(String idlist, String current_tbl_nm) {
        String encripted_nm = encript("STATIC_URL_" + current_tbl_nm + "_" + idlist);
        File file = new File(docroot + encripted_nm);
        if (file.isFile()) {
        } else {
            writeResultsToFile(current_tbl_nm + "||" + idlist, docroot + encripted_nm);
        }
        String static_url = service_name + "Search/SearchResults3?" + Start_EgenVar1.TABLE_TO_USE_FLAG + current_tbl_nm + "=" + encripted_nm;
        return static_url;
    }
    
    private String makeSTATIC_URL(ArrayList<Integer> id_l, String current_tbl_nm) {
        Collections.sort(id_l);
        String idlist = id_l.toString().replaceAll("[^0-9,]", "");
        String encripted_nm = encript("STATIC_URL_" + current_tbl_nm + "_" + idlist);
        File file = new File(docroot + encripted_nm);
        if (file.isFile()) {
        } else {
            writeResultsToFile(current_tbl_nm + "||" + id_l.toString().replaceAll("[^0-9,]", ""), docroot + encripted_nm);
        }
        String static_url = service_name + "Search/SearchResults3?" + Start_EgenVar1.TABLE_TO_USE_FLAG + current_tbl_nm + "=" + encripted_nm;
        return static_url;
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
    /*
     MethodID=JM7
     */
    
    private boolean createConnection() {
        boolean result = false;
        try {
            if (conn == null || conn.isClosed()) {
                Class.forName("org.apache.derby.jdbc.ClientDriver").newInstance();
                conn = DriverManager.getConnection(dbURL_dataEntry);
                result = true;
            } else {
                result = true;
            }
        } catch (Exception ex) {
            String error = ex.getMessage();
            System.out.println("Error JM7a: " + error);
        }
        return result;
    }
    /*
     MethodID=JM12
     */
    
    private int refreshJasonList() {
        int result = 0;
        if (name2url_map != null && createConnection()) {
            ArrayList<String> sql_l = new ArrayList<>();
            sql_l.add("DROP TABLE " + databse_NAME_DATA + ".json_list");
            sql_l.add("CREATE TABLE " + databse_NAME_DATA + ".json_list(id int NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1)  PRIMARY KEY,name varchar(128), url varchar(2048))");
            ArrayList<String> k_l = new ArrayList<>(name2url_map.keySet());
            for (int i = 0; i < k_l.size(); i++) {
                sql_l.add("insert into " + databse_NAME_DATA + ".json_list(name, url) values('" + k_l.get(i).replace("_TAGSOURCE", "") + "','" + name2url_map.get(k_l.get(i)) + "')");
            }
            try {
                if (!conn.isClosed()) {
                    Statement stm = conn.createStatement();
                    try {
                        result = stm.executeUpdate(sql_l.remove(0));
                    } catch (SQLException ex) {
                        System.out.println("Warning JM12a: " + ex.getMessage());
                    }
                    for (int i = 0; (i < sql_l.size() && result >= 0); i++) {
                        try {
                            result = stm.executeUpdate(sql_l.get(i));
                        } catch (SQLException ex) {
                            result = -1;
                            System.out.println("Error JM12b: " + sql_l.get(i) + "\n" + ex.getMessage());
                        }
                    }
                    stm.close();
                }
            } catch (SQLException ex) {
                System.out.println("Error 59g: " + ex.getMessage());
            }
            
        } else {
            System.out.println("Error 59h: failed to stablish connection");
        }
        return result;
    }
    
    private int populateReportLineage(Connection c_conn, HashMap<String, ArrayList<String>> lienage_map) {
        int result = 0;
        if (c_conn != null) {
            Statement stmt = null;
            try {
                stmt = c_conn.createStatement();
                String repor_tbl = get_correct_table_name("report");
//                System.out.println("\n\n\n\n 4514 " + repor_tbl + "\n\n\n");
                if (repor_tbl != null) {
                    PreparedStatement p_set_Cpath = c_conn.prepareStatement("update " + repor_tbl + " SET " + Start_EgenVar1.LINEAGE_HASHCOL_FLAG + "=? where id=?");
//                    System.out.println("926 " + "update " + repor_tbl + " SET " + Start_EgenVar1.LINEAGE_HASHCOL_FLAG + "=? where id=?");
                    ArrayList<String> lin_key_l = new ArrayList<>(lienage_map.keySet());
                    for (int i = 0; (i < lin_key_l.size() && result >= 0); i++) {
                        p_set_Cpath.setString(1, "\"" + lienage_map.get(lin_key_l.get(i)).toString().
                                replace("[", "").replace("]", "").
                                replaceAll("\\s", "").replaceAll(",", "\",\"") + "\"");
                        String[] spl_a = lin_key_l.get(i).split("=");                        
                        if (spl_a.length == 2) {
                            if (spl_a[0].toUpperCase().equals("REPORT")) {
                                p_set_Cpath.setString(2, spl_a[1]);
                                p_set_Cpath.addBatch();
                            }
                        } else {
                            p_set_Cpath.setString(2, lin_key_l.get(i));
                            p_set_Cpath.addBatch();
                        }
                        
                        if (result > -2 && (i % 500 == 0 || (i == (lin_key_l.size() - 1)))) {
                            int[] tmp_result_a = p_set_Cpath.executeBatch();
                            for (int j = 0; (j < tmp_result_a.length && result >= 0); j++) {
                                if (tmp_result_a[j] < 0) {
                                    result = tmp_result_a[j];
                                }
                            }
                            p_set_Cpath.clearParameters();
                        }
                    }
                }
                
            } catch (SQLException ex) {
                result = -1;
                System.out.println("Error: " + ex.getMessage());
                ex.printStackTrace();
            } finally {
                try {
                    if (stmt != null && !stmt.isClosed()) {
                        stmt.close();
                    }
                } catch (SQLException ex) {
                    result = -1;
                    System.out.println("Error: " + ex.getMessage());
                }
            }
        }
        return result;
    }
}
