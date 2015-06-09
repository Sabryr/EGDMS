/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package no.ntnu.medisin.egenvar.server;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Formatter;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.ws.BindingProvider;
import static no.ntnu.medisin.egenvar.server.Start_EgenVar1.WSDL_URL_FLAG;
import static no.ntnu.medisin.egenvar.server.Start_EgenVar1.split_limit;

/**
 *
 * @author sabryr
 */
public class Syc_manager  {

    private Connection conn = null;
    private HashMap<String, Integer> column2type_map;
    private ArrayList<String> column2autoincrement_l;
    private ArrayList<String> all_table_l;
    private String databse_NAME_DATA = null;
    private HashMap<String, ArrayList<String>> table2Columns_map;
    private String dbURL_dataEntry;
//    public static ArrayList<String> foreign_key_column_l;
    private HashMap<String, ArrayList<String>> table2forgn_column_map;
    private HashMap<String, String> table2autoincremental_column_map;
    private HashMap<String, String> instruct_map;
    private Path outfolder;
    public int split_limit = 500;
    public final int PROGRESS_MONT_INTERVAL = 30000;
    private ArrayList<String[]> authentic_host_al;
//    private String target_server_name;
//    private boolean start_now = false;
    public static boolean active = false;
    private Start_EgenVar1 caller;

    /*
     MethodID=sym1
     */
    public Syc_manager(Start_EgenVar1 caller, ArrayList<String[]> authentic_host_al, String dbURL_dataEntry,
            HashMap<String, String> instruct_map, Path outfolder, int split_limit) {
        this.caller = caller;
        this.dbURL_dataEntry = dbURL_dataEntry;
        this.instruct_map = instruct_map;
        this.outfolder = outfolder;
        this.split_limit = split_limit;
//        this.start_now = start_now;
        this.authentic_host_al = authentic_host_al;
    }

    public static void deActivate() {
        active = false;
    }
    /*
     MethodID=sym2
     */

    public void commit() {
        System.out.println("84 sync");
        if (active) {
            System.out.println("Another sync operation is active, try again later");
        } else {
//            if (start_now) {
                if (authentic_host_al != null) {
                    for (int i = 0; i < authentic_host_al.size(); i++) {
                        String wsdl = authentic_host_al.get(i)[0];
                        int c_split_limit = split_limit;
                        if (wsdl.equals(instruct_map.get(WSDL_URL_FLAG)) && c_split_limit < 2000) {
                            c_split_limit = 2000;
                        }
                        String user_name = authentic_host_al.get(i)[1];
                        String authentication_string = authentic_host_al.get(i)[2];
                        String target_server_name = authentic_host_al.get(i)[3];
                        sycwithCentral(wsdl, user_name, authentication_string, target_server_name, c_split_limit);
                    }
                } else {
                    System.out.println("Not enough details to connect to remote");
                }

//            } 
//            else {
//                if (authentic_host_al != null) {
//                    int syc_hour = 24;
//                    for (int i = 0; i < authentic_host_al.size(); i++) {
//                        String wsdl = authentic_host_al.get(i)[0];
//                        int c_split_limit = split_limit;
//                        if (wsdl.equals(instruct_map.get(WSDL_URL_FLAG)) && c_split_limit < 2000) {
//                            c_split_limit = 2000;
//                        }
//                        String user_name = authentic_host_al.get(i)[1];
//                        String authentication_string = authentic_host_al.get(i)[2];
//                        String target_server_name = authentic_host_al.get(i)[3];
//                        GregorianCalendar c_cal = new GregorianCalendar();
//                        int c_h = c_cal.get(Calendar.HOUR_OF_DAY);
//                        int midnightin = syc_hour - c_h;//15 - c_h; //24
//                        if (midnightin < 0) {
//                            midnightin = 24 + midnightin;
//                        }
//                        c_cal.add(Calendar.HOUR, midnightin);
//                        System.out.println("Sync scheduled @" + c_cal.getTime().toString());
//                        long wating_time = c_cal.getTimeInMillis() - (new GregorianCalendar()).getTimeInMillis();
//                        boolean intrrupted = false;
//                        try {
//                            Thread.sleep(wating_time);
//                        } catch (InterruptedException ex) {
//                            intrrupted = true;
//                            System.out.println("Warning sym2: " + ex.getMessage());
//                        }
//                        if (!intrrupted) {
//                            System.out.println("Starting auto-sync@" + Timing.getDateTime());
//                            sycwithCentral(wsdl, user_name, authentication_string, target_server_name, c_split_limit);
//                        }
//                    }
//                } else {
//                    System.out.println("Not enough details to connect to remote");
//                }
//
//            }
            active = false;
            caller.getConnection_memory(true, 23);
        }
    }

    public boolean isActive() {
        while (active) {
            try {
                Thread.sleep(60000);
            } catch (InterruptedException ex) {
                System.out.println("Warning EC1a: " + ex.getMessage());
            }
        }
        return active;
    }

    /*
     MethodID=sym4
     * else if (c_table_l.get(i).split("\\.")[c_table_l.get(i).split("\\.").length - 1].toUpperCase().startsWith("TAGS2")) {
     target_tag_table_l.add(c_table_l.get(i));
     }
     HashMap<String, ArrayList<String>> target_tag2col_map = new HashMap();
     for (int i = 0; i < target_tag_table_l.size(); i++) {
     ArrayList<String> col_l = getColumns(target_tag_table_l.get(i));
     for (int j = 0; j < col_l.size(); j++) {
     String c_col = col_l.get(j);
     if (isAutoincremental(conn, target_tag_table_l.get(i), c_col) || isForegnKey(conn, target_tag_table_l.get(i), c_col)) {
     System.out.println("\t 90 sipp " + c_col);
     col_l.remove(c_col);
     j--;
     } 
     }
     target_tag2col_map.put(target_tag_table_l.get(i), col_l);
     }
     System.out.println("89 target_tag2col_map="+target_tag2col_map);
     */
    private int sycwithCentral(String wsdl, String user_name,
            String authentication_string, String target_server_name, int c_split_limit) {
        active = true;
        int result = 0;
        if (Files.exists(outfolder)) {
            try {
                Files.createDirectories(outfolder);
            } catch (IOException ex) {
                System.out.println("Error sys3e: Failed to initiate synv directory " + outfolder + ". Try creating this directory manually and try again.\n" + ex.getMessage());
            }
        }
        if (Files.exists(outfolder)) {
            if (Files.isWritable(outfolder)) {
                Path sync_file = Paths.get(outfolder.toAbsolutePath().toString() + File.separator + "SYNC__tags2host.sync");
                System.out.println("#" + sync_file);
                FileWriter out = null;
                try {
                    out = new FileWriter(sync_file.toFile(), false);
                    out.append("##TAGS2HOST.NAME\tTAGS2HOST.TAGTYPE\tTAGS2HOST.URL\tTAGS2HOST.HOST\tTAGS2HOST.LINK_TO_HOST"); // also add QR info
                    out.close();
                } catch (IOException ex) {
                    System.out.println("Error sym4g:  " + ex.getMessage());
                } finally {
                    try {
                        out.close();
                    } catch (IOException ex) {
                        System.out.println("Error sysm4h: " + ex.getMessage());
                    }
                }
                if (Files.isRegularFile(sync_file)) {
                    if (createConnection()) {
                        try {
                            if (!conn.isClosed()) {
                                Statement stm = conn.createStatement();
                                try {
                                    ArrayList<String> c_table_l = getCurrentTables();
                                    ArrayList<String> tag2_table_l = new ArrayList<>();
                                    for (int i = 0; i < c_table_l.size(); i++) {
                                        if (c_table_l.get(i).toUpperCase().endsWith("2TAGS")) {
                                            tag2_table_l.add(c_table_l.get(i));
                                        }
                                    }
                                    for (int i = 0; i < tag2_table_l.size(); i++) {
                                        String c_tbl = tag2_table_l.get(i);
                                        String tagtype = c_tbl.split("\\.")[c_tbl.split("\\.").length - 1].toUpperCase().replace("2TAGS", "");
                                        long startId = 0;
                                        long lastId = 0;
                                        //ToDo:Make sure the table has all the extracting columns!
                                        StringBuilder out_str = new StringBuilder();
                                        while (startId >= 0) {
                                            lastId = lastId + 10000;
                                            String sql = "select * from " + c_tbl + " where id>=" + startId + " and id<" + lastId;
                                            ResultSet r_1 = stm.executeQuery(sql);
                                            boolean found = false;
                                            while (r_1.next()) {
                                                found = true;
                                                int id = r_1.getInt("id");
                                                String name = r_1.getString("name");
                                                String URL = r_1.getString("URI");
                                                String link_to_host = target_server_name + "Search/SearchResults3?TABLETOUSE_" + c_tbl + "=" + id + "";
                                                out_str.append("\n" + name + "\t" + tagtype + "\t" + URL + "\t" + target_server_name + "\t" + link_to_host);
                                            }
                                            if (found) {
                                                startId = lastId;
                                            } else {
                                                startId = -1;
                                            }

                                        }
                                        try {
                                            out = new FileWriter(sync_file.toFile(), true);
                                            out.append(out_str);
                                            out.close();
                                        } catch (IOException ex) {
                                            System.out.println("Error sym4g:  " + ex.getMessage());
                                        } finally {
                                            try {
                                                out.close();
                                            } catch (IOException ex) {
                                                System.out.println("Error sysm4h: " + ex.getMessage());
                                            }
                                        }
                                    }
                                    System.out.println("Sync files created @" + Timing.getDateTime());
                                    if (wsdl == null || user_name == null || authentication_string == null) {
                                        System.out.println("Manual mode: use \n\tegenv -template " + sync_file + " \nto update the central server");
                                    } else {
                                        System.out.println(resetTags2host(user_name, authentication_string, target_server_name, wsdl));
                                        send(authentication_string, wsdl, user_name, c_split_limit);
                                    }
                                } catch (Exception ex) {
                                    result = -1;
                                }
                                stm.close();

                            } else {
                                System.out.println("Error sym4b: failed to stablish connection");
                            }
                        } catch (SQLException ex) {
                            System.out.println("Error sym4a " + ex.getMessage());
                        }

                    } else {
                        System.out.println("Error sym4b: failed to stablish connection");
                    }
                } else {
                    System.out.println("Error sym4j: There was an error when using the file  " + sync_file);
                }
            } else {
                System.out.println("Error sys3f: Failed to access synv directory " + outfolder + ". Try creating this directory manually and set write permission and try again");
            }
        } else {
            System.out.println("Error sys3d: Failed to initiate synv directory " + outfolder + ". Try creating this directory manually and try again");

        }

        return result;
    }

    /*
     MethodID=sym14
     */
    private void send(String authentication_string, String wsdl, String user_name, int c_split_limit) {
        boolean tag_source_found = false;
        if (Files.exists(outfolder)) {
            if (Files.isReadable(outfolder)) {
                if (createConnection()) {
                    all_table_l = getTables(conn);
                    HashMap<String, ArrayList<Path>> table2path_map = new HashMap<>();
                    try {
                        DirectoryStream<Path> ds = Files.newDirectoryStream(outfolder, "*.sync");
                        Iterator<Path> path_l = ds.iterator();
                        while (path_l.hasNext()) {
                            Path c_path = path_l.next();
                            String[] path_str = c_path.getFileName().toString().split("__");
                            if (path_str.length == 2) {
                                String db_table = path_str[1].split("\\.")[0];
                                String existing_db_table = get_correct_table_name(db_table);
                                if (existing_db_table != null && containsIgnoreCase(all_table_l, existing_db_table)) {
                                    if (!table2path_map.containsKey(existing_db_table)) {
                                        table2path_map.put(existing_db_table, new ArrayList<Path>());
                                    }
                                    table2path_map.get(existing_db_table).add(c_path);
                                } else {
                                    System.out.println("Warning (@sym14a).  Not using " + c_path + " (incompatible file name format or table not found " + db_table + ")");
                                }
                            } else {
                                System.out.println("Warning (@sym14b). Not using " + c_path + " (incompatible file name format)");
                            }
                        }
                        if (table2path_map.isEmpty()) {
                            System.out.println(" Nothing to process");
                        } else {
                            ArrayList<String> table_to_use_l = new ArrayList<>(table2path_map.keySet());
                            for (int i = 0; i < table_to_use_l.size(); i++) {
                                Collections.sort(table2path_map.get(table_to_use_l.get(i)));
                            }
                            for (int k = 0; k < table_to_use_l.size(); k++) {
                                String table = table_to_use_l.get(k);
                                ArrayList<Path> c_path_l = table2path_map.get(table);
                                for (int i = 0; i < c_path_l.size(); i++) {
                                    Path c_path = c_path_l.get(i);
                                    System.out.println("_______________\n" + c_path.toAbsolutePath());
                                    ArrayList<String> result_l = readTemplate_new(c_path.toAbsolutePath().toString(), user_name,
                                            authentication_string, wsdl, c_split_limit);
                                    List remote_result_l = setUsingTemplate(result_l, user_name, authentication_string, wsdl);
                                    for (int j = 0; j < remote_result_l.size(); j++) {
                                        Object result = remote_result_l.get(j);
                                        if (result != null && !result.toString().isEmpty()) {
                                            System.out.println(" " + result);
                                        }
                                    }
                                }
                            }
                        }
                    } catch (IOException ex) {
                        System.out.println("Error RS3b : " + ex.getMessage() + " ");
                    }
                }
            }
        }
        if (tag_source_found) {
            System.out.println("Restart server to install the tagsources !");
        }
    }
    /*
     MethodID=sym25
     */

    private String resetTags2host(String username, String authentication_string, String host_id, String wsdl) {
        String result = null;
        ProgressMonitor p = new ProgressMonitor(PROGRESS_MONT_INTERVAL, "15");
        (new Thread(p)).start();
        if (authentication_string != null) {
            try {
                HashMap<String, String> out_map = new HashMap<>();
                no.ntnu.medisin.egenvar.web_service.AuthenticateService_Service service = new no.ntnu.medisin.egenvar.web_service.AuthenticateService_Service();
                no.ntnu.medisin.egenvar.web_service.AuthenticateService port = service.getAuthenticateServicePort();
                BindingProvider bp = (BindingProvider) port;
                bp.getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, wsdl);
                System.out.println("username" + username + " authentication_string=<hidden>"); //authentication_string
                result = port.resetTags2Host(username, authentication_string, host_id);
                p.cancel();

            } catch (Exception ex) {
//                ex.printStackTrace();
                System.out.println("Error 16c:  resetTags2host " + ex.getMessage());
                p.cancel();
            }
        } else {
            System.out.println("Error 16d: Authentication failed for " + wsdl);
            p.cancel();
        }
        p.cancel();
        return result;
    }

    /*
     MethodID=sym24
     */
    private List setUsingTemplate(ArrayList<String> param_list_l, String user_name,
            String authentication_string, String wsdl) {
        List result = null;
        ProgressMonitor p = new ProgressMonitor(PROGRESS_MONT_INTERVAL, "15");
        (new Thread(p)).start();
        if (authentication_string != null) {
            try {
                no.ntnu.medisin.egenvar.web_service.AuthenticateService_Service service = new no.ntnu.medisin.egenvar.web_service.AuthenticateService_Service();
                no.ntnu.medisin.egenvar.web_service.AuthenticateService port = service.getAuthenticateServicePort();
                BindingProvider bp = (BindingProvider) port;
                bp.getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, wsdl);
                System.out.println("394  user_name=" + user_name + "\tauthentication_string=<hidden>");//authentication_string
                result = port.setUsingTemplate(user_name, authentication_string, param_list_l);
                p.cancel();
                if ((result == null || result.isEmpty() || result.get(0) == null)) {
                    System.out.println("Error 16a: Communication failie");
                } else {
                    if (result.get(0).toString().startsWith("Error ")) {
                        System.out.println("Error 16b: " + result.get(0));
                        result = new ArrayList<>(0);
                    }
                }
            } catch (Exception ex) {
//                ex.printStackTrace();
                System.out.println("Error sym24 :Failed setUsingTemplate " + ex.getMessage());
                p.cancel();
            }
        } else {
            System.out.println("Error 16d: Authentication failed for " + wsdl);
            p.cancel();
        }
        p.cancel();
        return result;
    }

    /**
     * MethodID=sym15
     *
     */
    public boolean containsIgnoreCase(ArrayList<String> in_l, String matchit) {
        boolean found = false;
        if (in_l == null || in_l.isEmpty() || matchit == null || matchit.isEmpty() || matchit.trim().isEmpty()) {
        } else {
            try {
                for (int i = 0; (i < in_l.size() && !found); i++) {
                    String cval = in_l.get(i);
                    if (cval.equalsIgnoreCase(matchit)) {
                        found = true;
                    }
                }
                if (!found) {
                    for (int i = 0; (i < in_l.size() && !found); i++) {
                        String cval = in_l.get(i);
                        if (cval.toUpperCase().endsWith(("." + matchit).toUpperCase())) {
                            found = true;
                        } else if (matchit.toUpperCase().endsWith(("." + cval).toUpperCase())) {
                            found = true;
                        }
                    }
                }
            } catch (Exception ex) {
                System.out.println("Error sym15: creating connections " + ex.getMessage());
            }
        }
        return found;
    }

    /*
     MethodID=sym16
     */
    private ArrayList<String> readTemplate_new(String filenm, String user_name,
            String authentication_string, String wsdl, int c_split_limit) {
        System.out.println("Reading template = " + filenm);
        boolean committodb = true;
//        ArrayList<String> content_l = null;
        File tmpl_file = new File(filenm);
        int nuofcolmns = 0;
        int rownum = 0;
        boolean allok = true;
        String relationship_type = null;
        HashMap<String, ArrayList<String>> vlaue_map = new HashMap<>();
        ArrayList<String> column_nms = new ArrayList<>(2);
        int c_count = 0;
        if (tmpl_file.isFile() && tmpl_file.canRead()) {
            try {
                Scanner scan = new Scanner(tmpl_file);
                int tot_rows = 0;
                while (scan.hasNext()) {
                    scan.nextLine();
                    tot_rows++;
                }
                scan.close();
                scan = new Scanner(tmpl_file);
                rownum = 0;
                while (scan.hasNext() && allok) {
                    String line = scan.nextLine();
                    rownum++;
                    c_count++;
                    if (line.startsWith("##")) {
                        line = line.replaceAll("#", "").replaceAll("\\*", "").trim();
                        String[] colnm_a = line.split("\t");
                        nuofcolmns = colnm_a.length;
                        for (int i = 0; i < colnm_a.length; i++) {
                            String[] colnm_a_split = colnm_a[i].split("\\.");
                            if (colnm_a_split.length > 1) {//Column names with Table.column format
                                vlaue_map.put(colnm_a[i], new ArrayList<String>(1));
                                column_nms.add(colnm_a[i]);
                            } else {
                                vlaue_map.put(colnm_a[i], null);
                                column_nms.add(colnm_a[i]);
                            }
                        }

                    } else if (!line.isEmpty()) {
                        line = line.replaceAll("\\*", "").trim();
                        String[] values_a = line.split("\t");
                        if (nuofcolmns != values_a.length) {
                            allok = false;
                            System.out.println("Error 12a: The row " + rownum + " has different column number than"
                                    + "the title. Expected= " + nuofcolmns + ". Found=" + values_a.length + "\n\t" + line);
                        } else {
                            for (int i = 0; i < values_a.length; i++) {
                                if (vlaue_map.get(column_nms.get(i)) == null) {
                                } else {
                                    vlaue_map.get(column_nms.get(i)).add(values_a[i]);
                                }
                            }
                        }
                    }
                    if (allok && committodb && (c_count > c_split_limit || !scan.hasNext())) {
                        System.out.println("Sending " + c_count + " records.  Total queued=" + rownum + " of about " + tot_rows);
                        c_count = 0;
                        for (int i = 0; i < column_nms.size(); i++) {
                            if (vlaue_map.containsKey(column_nms.get(i)) && vlaue_map.get(column_nms.get(i)) == null) {
                                vlaue_map.remove(column_nms.get(i));
                                System.out.println("Not processing " + column_nms.get(i));
                            }
                        }
                        ArrayList<String> content_l = map2list(vlaue_map);
                        if (content_l.isEmpty()) {
                            System.out.println("2. Nothing to update");
                        } else {
                            List result_l = setUsingTemplate(content_l, user_name, authentication_string, wsdl);
                            if (result_l == null) {
                                System.out.println("Error 12d: Unknown error");
                                allok = false;
                            } else if (result_l.size() != 3) {
                                System.out.println("Error 12e: Connection to the server failed");
                                allok = false;
                            } else {
                                String result = (String) result_l.get(0);
                                String undo = (String) result_l.get(1);
                                String messages = (String) result_l.get(2);
                                System.out.println(result);
                                if (!messages.isEmpty()) {
                                    System.out.println("messages=" + messages);
                                }
                                if (undo != null) {
//                                    createUndoPoint(undo, dir_read_from_prep_files);
                                }

                                if (result_l.size() > 3) {
                                    String report_batches_used = result_l.get(3).toString();
                                    String reports_used = result_l.get(4).toString();
                                    String suc = result_l.get(5).toString();
                                    String suc_link = result_l.get(6).toString();
                                    System.out.println("3278 report_batches_used=" + report_batches_used + " reports_used=" + reports_used + " suc=" + suc + "  suc_link=" + suc_link);

                                }
                            }
                        }
                        for (int i = 0; i < column_nms.size(); i++) {
                            if (vlaue_map.containsKey(column_nms.get(i))) {
                                vlaue_map.get(column_nms.get(i)).clear();
                            }
                        }

                    }
                }
                scan.close();
            } catch (FileNotFoundException ex) {
                System.out.println("Error 12c: Failed to read template file " + filenm + ": " + ex.getMessage());
            }
        } else {
            System.out.println("Error 12d: Failed to read template file " + filenm);
        }
        return null;

    }
    /*
     MethodId=sym18*/

    private List setUsingTemplate(String wsdl, String username, String password, String authentication_string, ArrayList<String> param_list_l) {
        List result = null;
        ProgressMonitor p = new ProgressMonitor(PROGRESS_MONT_INTERVAL, "15");
        (new Thread(p)).start();
//        if (authenticate(wsdl,  username,  password)) {
        try {
//                HashMap<String, String> out_map = new HashMap<>();
            no.ntnu.medisin.egenvar.web_service.AuthenticateService_Service service = new no.ntnu.medisin.egenvar.web_service.AuthenticateService_Service();
            no.ntnu.medisin.egenvar.web_service.AuthenticateService port = service.getAuthenticateServicePort();
            BindingProvider bp = (BindingProvider) port;
            bp.getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, wsdl);
            result = port.setUsingTemplate(username, authentication_string, param_list_l);
            p.cancel();
            if ((result == null || result.isEmpty() || result.get(0) == null)) {
                System.out.println("Error 16a: Communication failier");
            } else {
                if (result.get(0).toString().startsWith("Error ")) {
                    System.out.println("Error 16b: " + result.get(0));
                    result = new ArrayList<>(0);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            System.out.println("Error sym18a :Failed setUsingTemplate " + ex.getMessage());
        }
//        } else {
//            System.out.println("Error 16d: Authentication failed. use -diagnose get more information");
//        }
        p.cancel();
        return result;
    }

    /*
     * MethidId=sym20
     */
    private String getHash(String intext) {
        try {
            if (intext != null) {
                MessageDigest sha1 = MessageDigest.getInstance("SHA1");
                byte[] pass_bytes = intext.getBytes();
                sha1.reset();
                sha1.update(pass_bytes);
                byte[] pass_digest = sha1.digest();
                Formatter formatter = new Formatter();
                for (byte b : pass_digest) {
                    formatter.format("%02x", b);
                }
                return formatter.toString();
            }
        } catch (NoSuchAlgorithmException ex) {
            System.out.println("Error 56 :" + ex.getMessage());
        }
        return null;
    }
//    /*
//     MethodID=sym16
//     */
//    private ArrayList<String> readTemplate(String filenm) {
//        System.out.println("Cheking = " + filenm);
////        HashMap<Long, HashMap<String, HashMap<String, String>>> data_map = new HashMap<>();
//        ArrayList<String> content_l = null;
//        File tmpl_file = new File(filenm);
//        int nuofcolmns = 0;
//        boolean limited = false;
//        boolean allok = true;
//        if (tmpl_file.isFile() && tmpl_file.canRead()) {
//            try {
//                Scanner scan = new Scanner(tmpl_file);
//                long rownum = 0;
//                ArrayList<String[]> pos_l = new ArrayList<>();
//                while (!limited && scan.hasNext() && allok) {
//                    String line = scan.nextLine();
//                    if (line.startsWith("##")) {
//                        line = line.replaceAll("#", "").replaceAll("\\*", "").trim();
//                        String[] colnm_a = line.split("\t");
//                        nuofcolmns = colnm_a.length;
//                        for (int i = 0; i < colnm_a.length; i++) {
//                            String c_tbl = null;
//                            String c_col = null;
//                            String[] c_col_a = colnm_a[i].split("\\.");
//                            if (c_col_a.length >= 2) {
//                                c_tbl = get_correct_table_name(c_col_a[c_col_a.length - 2]);
//                                c_col = c_col_a[c_col_a.length - 1];
//                                if (c_tbl != null) {
//                                    String[] tmp_a = new String[2];
//                                    tmp_a[0] = c_tbl;
//                                    tmp_a[1] = c_col;
//                                    pos_l.add(tmp_a);
//                                } else {
//                                    System.out.println("Error RS12g: Invalid table " + c_col_a[c_col_a.length - 2]);
//                                }
//                            } else {
//                                System.out.println("Error RS12f: The column name incorrect. expected DATABSE_NAME.COLUMNNAME found " + colnm_a[i]);
//                            }
//                        }
//                    } else if (line.startsWith("#")) {
//                        if (line.contains(Start_EgenVar1.RELATIONSHIP_TYPE_FLAG)) {
//                            allok = false;
//                        }
//                    } else if (!line.isEmpty()) {
//                        rownum++;
//                        String[] values_a = line.split("\t");
//                        if (nuofcolmns != values_a.length) {
//                            allok = false;
//                            System.out.println("Error RS12i: The row " + rownum + " has different column number than the title. Expected= " + nuofcolmns + ". Found=" + values_a.length);
//                        } else {
//                            if (pos_l.size() == values_a.length) {
//                                HashMap<String, HashMap<String, String>> template_map_map = new HashMap<>();
//                                for (int i = 0; i < pos_l.size(); i++) {
//                                    String[] c_pos_a = pos_l.get(i);
//                                    if (!template_map_map.containsKey(c_pos_a[0])) {
//                                        HashMap<String, String> template_map = new HashMap<>();
//                                        template_map.put(c_pos_a[1], values_a[i]);
//                                        template_map_map.put(c_pos_a[0], template_map);
//                                    } else {
//                                        if (!template_map_map.get(c_pos_a[0]).containsKey(c_pos_a[1])) {
//                                            template_map_map.get(c_pos_a[0]).put(c_pos_a[1], values_a[i]);
//                                        } else {
//                                            System.out.println("Error RS12j: Mismatch error ");
//                                        }
//                                    }
//                                }
////                                data_map.put(rownum, template_map_map);
//                                map2list(template_map_map);
//                            } else {
//                                System.out.println("Error RS12h: Column number mismatch " + line);
//                            }
//                        }
//                    }
//                }
//                scan.close();
//                System.out.print(rownum + " rows to be processed  ... ");
//            } catch (FileNotFoundException ex) {
//                System.out.println("Error 12c: Failed to read template file " + filenm + ": " + ex.getMessage());
//            }
//        } else {
//            System.out.println("Error 12d: Failed to read template file " + filenm);
//        }
//        if (allok) {
//            System.out.println("Reading template = " + filenm);
//            System.out.println("300 " + data_map);
////            String[] result_a = pupolateFromTemplate(data_map);    
//            return map2list(data_map);
//        } else {
//            System.out.println("Error 12h: Failed to process " + filenm);
//            return null;
//        }
//    }

    /*
     MethodID=sym16
     */
    private ArrayList<String> map2list(HashMap<String, ArrayList<String>> vlaue_map) {
        ArrayList<String> retuen_l = new ArrayList<>(vlaue_map.size());
        ArrayList<String> mapkeys_l = new ArrayList<>(vlaue_map.keySet());
        for (int i = 0; i < mapkeys_l.size(); i++) {
            ArrayList<String> c_val_l = vlaue_map.get(mapkeys_l.get(i));
            if (c_val_l != null && !c_val_l.isEmpty()) {
                String valuse = c_val_l.get(0);
                for (int j = 1; j < c_val_l.size(); j++) {
                    valuse = valuse + ";;" + c_val_l.get(j);
                }
                retuen_l.add(mapkeys_l.get(i) + "==" + valuse);
            }
        }
        return retuen_l;
    }
    /*
     MethodID=sym13
     */

    private boolean isAutoincremental(Connection c_con, String tbl_nm, String col_nm) {
        boolean result = false;
        tbl_nm = get_correct_table_name(tbl_nm);
        String c_col_full_nm = (tbl_nm + "." + col_nm).toUpperCase();
        if (table2autoincremental_column_map == null) {
            table2autoincremental_column_map = new HashMap<>();
        }
        if (!table2autoincremental_column_map.containsKey(tbl_nm)) {
            String autoincre_col = "-1";
            try {
                DatabaseMetaData metaData = c_con.getMetaData();
                ArrayList<String> c_table_l = getTables(conn);
                for (int i = 0; i < c_table_l.size(); i++) {
                    String c_table = c_table_l.get(i);
                    String tablenm4metadata = c_table;
                    if (c_table.contains(".")) {
                        tablenm4metadata = c_table.split("\\.")[1];
                    }
                    ResultSet key_result = metaData.getColumns(null, getDB_name(c_con), tablenm4metadata, null);
                    if (!key_result.next()) {
                        key_result = metaData.getColumns(null, getDB_name(c_con).toUpperCase(), tablenm4metadata.toUpperCase(), null);
                    } else {
                        String autoincrmnt = key_result.getString("IS_AUTOINCREMENT");
                        String clmname = key_result.getString("COLUMN_NAME");
                        if (autoincrmnt.equalsIgnoreCase("YES")) {
                            autoincre_col = tbl_nm + "." + clmname;
                        }
                    }
                    while (key_result.next()) {
                        String autoincrmnt = key_result.getString("IS_AUTOINCREMENT");
                        String clmname = key_result.getString("COLUMN_NAME");
                        if (autoincrmnt.equalsIgnoreCase("YES")) {
                            autoincre_col = tbl_nm + "." + clmname;
                        }
                    }
                }

            } catch (SQLException ex) {
                System.out.println("Error sym12a: " + ex.getMessage());
            }
            table2autoincremental_column_map.put(tbl_nm, autoincre_col);
        }
        if (table2autoincremental_column_map.containsKey(tbl_nm) && table2autoincremental_column_map.get(tbl_nm).equalsIgnoreCase(c_col_full_nm)) {
            result = true;
        }
        return result;
    }
    /*
     MethodID=sym5
     */

    private Integer getColumnType(String full_column_nm) {
        int type = -999;
        full_column_nm = full_column_nm.toUpperCase();
        if (createConnection()) {
            try {
                if (!conn.isClosed()) {
                    if (column2type_map == null) {
                        column2type_map = new HashMap<>();
                        column2autoincrement_l = new ArrayList<>(5);

                        try {
                            DatabaseMetaData metaData = conn.getMetaData();
                            ArrayList<String> c_table_l = getTables(conn);
                            for (int i = 0; i < c_table_l.size(); i++) {
                                String c_table = c_table_l.get(i);
                                String tablenm4metadata = c_table;
                                if (c_table.contains(".")) {
                                    tablenm4metadata = c_table.split("\\.")[1];
                                }
                                ResultSet key_result = metaData.getColumns(null, getDB_name(conn), tablenm4metadata, null);
                                if (!key_result.next()) {
                                    key_result = metaData.getColumns(null, getDB_name(conn).toUpperCase(), tablenm4metadata.toUpperCase(), null);
                                } else {
                                    int typename = key_result.getInt("DATA_TYPE");
                                    String autoincrmnt = key_result.getString("IS_AUTOINCREMENT");
                                    String clmname = key_result.getString("COLUMN_NAME");
                                    String key_name = getDB_name(conn) + "." + tablenm4metadata + "." + clmname;
                                    key_name = key_name.toUpperCase();
                                    column2type_map.put(key_name, typename);
                                    if (autoincrmnt.equalsIgnoreCase("YES")) {
                                        column2autoincrement_l.remove(key_name.toUpperCase());
                                        column2autoincrement_l.add(key_name.toUpperCase());
                                    }
                                }
                                while (key_result.next()) {
                                    int typename = key_result.getInt("DATA_TYPE");
                                    String autoincrmnt = key_result.getString("IS_AUTOINCREMENT");
                                    String clmname = key_result.getString("COLUMN_NAME");
                                    String key_name = getDB_name(conn) + "." + tablenm4metadata + "." + clmname;
                                    key_name = key_name.toUpperCase();
                                    column2type_map.put(key_name, typename);
                                    if (autoincrmnt.equalsIgnoreCase("YES")) {
                                        column2autoincrement_l.remove(key_name.toUpperCase());
                                        column2autoincrement_l.add(key_name.toUpperCase());
                                    }
                                }
                                key_result.close();
                            }

                        } catch (SQLException e) {
                            System.out.println("Error C1a" + e.getMessage());
                        }
                    }
                } else {
                    System.out.println("Error : connection closed");
                }
            } catch (SQLException ex) {
                System.out.println("Error 59g: " + ex.getMessage());
            }

        } else {
            System.out.println("Error: connection failed");
        }

        if (column2type_map.containsKey(full_column_nm)) {
            type = column2type_map.get(full_column_nm);
        }
        return type;
    }

    /*
     * 
     MethodID=sym12
     */
    private boolean isForegnKey(Connection c_con, String tbl_nm, String col_nm) {
        boolean result = false;
        tbl_nm = get_correct_table_name(tbl_nm);
        String c_col_full_nm = (tbl_nm + "." + col_nm).toUpperCase();
        if (table2forgn_column_map == null) {
            table2forgn_column_map = new HashMap<>();
        }
        if (!table2forgn_column_map.containsKey(tbl_nm)) {
            ArrayList<String> foreign_key_column_l = new ArrayList<>();
            try {
                DatabaseMetaData metaData = c_con.getMetaData();
                String tablenm4metadata = tbl_nm.split("\\.")[tbl_nm.split("\\.").length - 1];
                ResultSet key_result_forgn = metaData.getImportedKeys(c_con.getCatalog(), getDB_name(c_con), tablenm4metadata);
                if (!key_result_forgn.next()) {
                    key_result_forgn = metaData.getImportedKeys(c_con.getCatalog(), getDB_name(c_con), tablenm4metadata.toUpperCase());
                } else {
                    String c_col = (tbl_nm + "." + key_result_forgn.getString("FKCOLUMN_NAME")).toUpperCase();//column_name
                    if (!foreign_key_column_l.contains(c_col)) {
                        foreign_key_column_l.add(c_col);
                    }
                }
                while (key_result_forgn.next()) {
                    String c_col = (tbl_nm + "." + key_result_forgn.getString("FKCOLUMN_NAME")).toUpperCase();//column_name
                    if (!foreign_key_column_l.contains(c_col)) {
                        foreign_key_column_l.add(c_col);
                    }
                }
                key_result_forgn.close();
            } catch (SQLException ex) {
                System.out.println("Error sym12a: " + ex.getMessage());
            }
            table2forgn_column_map.put(tbl_nm, foreign_key_column_l);
        }
        if (table2forgn_column_map.containsKey(tbl_nm) && table2forgn_column_map.get(tbl_nm).contains(c_col_full_nm)) {
            result = true;
        }
        return result;
    }

    /*
     MethodID=sym6
     */
    public ArrayList<String> getTables(Connection c_con) {
        if (all_table_l == null) {

            Statement st_1 = null;
            ResultSet r_1 = null;
            try {
                all_table_l = new ArrayList<>(5);
                String database_nm = getDB_name(c_con);
                if (!c_con.isClosed()) {
                    DatabaseMetaData metaData = c_con.getMetaData();
                    r_1 = metaData.getTables(null, null, null, new String[]{"TABLE"});
                    while (r_1.next()) {
                        String table_nm = r_1.getString("TABLE_NAME");
                        String schema_nm = r_1.getString("TABLE_SCHEM");
                        if (schema_nm.equals(database_nm)) {
                            all_table_l.add(schema_nm + "." + table_nm);
                        }
                    }

                }
                close(null, st_1, r_1);
            } catch (SQLException ex) {
                System.out.println("Error AS23a " + ex.getMessage());
            } finally {
                close(null, st_1, r_1);
            }
        }
        return all_table_l;
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

    /*
     MethodID=sym8
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
            System.out.println("Error sym8a: " + error);
        }
        return result;
    }

    /**
     * MethodID=sym10
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

    /**
     * MethodID=sym11
     *
     */
    private ArrayList<String> getColumns(String current_tbl_nm) {
        ArrayList<String> columns_l = new ArrayList<>(2);
        if (table2Columns_map == null && createConnection()) {
            table2Columns_map = new HashMap<>();
            try {
                DatabaseMetaData metaData = conn.getMetaData();
                if (!conn.isClosed()) {
                    ArrayList<String> c_table_l = getCurrentTables();
                    for (int i = 0; i < c_table_l.size(); i++) {
                        ArrayList<String> tmp_columns_l = new ArrayList<>(2);
                        String c_table = c_table_l.get(i);
                        String tablenm4metadata = c_table;
                        if (c_table.contains(".")) {
                            tablenm4metadata = c_table.split("\\.")[1];
                        }

                        ResultSet key_result = metaData.getColumns(conn.getCatalog(), null, tablenm4metadata, null);
                        if (!key_result.next()) {
                            key_result = metaData.getColumns(null, getDB_name(conn), tablenm4metadata.toUpperCase(), null);
                        } else {
                            String clmname = key_result.getString("COLUMN_NAME");
                            if (!tmp_columns_l.contains(clmname)) {
                                tmp_columns_l.add(clmname);
                            }
                        }
                        while (key_result.next()) {
                            String clmname = key_result.getString("COLUMN_NAME");
                            if (!tmp_columns_l.contains(clmname)) {
                                tmp_columns_l.add(clmname);
                            }
                        }
                        table2Columns_map.put(c_table, tmp_columns_l);
                        key_result.close();
                    }
                }
            } catch (SQLException e) {
                System.out.println("Error C1a" + e.getMessage());
            }
        }
        current_tbl_nm = get_correct_table_name(current_tbl_nm);
        if (current_tbl_nm != null && table2Columns_map.containsKey(current_tbl_nm)) {
            columns_l = table2Columns_map.get(current_tbl_nm);
        } else if (table2Columns_map.containsKey(current_tbl_nm.toUpperCase())) {
            columns_l = table2Columns_map.get(current_tbl_nm.toUpperCase());
        }
        return columns_l;
    }

    /**
     * MethodID=sym11
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
     * MethodID=sym9
     *
     */
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
