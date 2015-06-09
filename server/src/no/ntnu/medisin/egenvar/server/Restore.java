/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package no.ntnu.medisin.egenvar.server;

import com.sun.xml.ws.org.objectweb.asm.Type;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Savepoint;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Scanner;
import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

import javax.sql.DataSource;

/**
 *
 * @author sabryr
 */
public class Restore implements Runnable {

    private Path outfolder;
    private String enckey;
//    private Connection ncon;
    private static HashMap<String, Integer> column2type_map;
    private static ArrayList<String> column2autoincrement_l;
    private HashMap<String, String> instruct_map;
    private HashMap<String, HashMap<String, String[]>> key_constraint_map;
    public static ArrayList<Integer> numerical_types;
    private String msges;
    public static HashMap<String, String> tag2id;
    public static HashMap<String, ArrayList<String>> table2Columns_map;
    private int counter;
    private ArrayList<String> table_l;
    private HashMap<String, ArrayList<String[]>> returning_list_map;
    /*
     MrthodID=BK1
     */

    public Restore(HashMap<String, String> instruct_map, Path outfolder, String enckey) {
        this.instruct_map = instruct_map;
        this.outfolder = outfolder;
        this.enckey = enckey;
    }

    /*
     MrthodID=RS2
     */
    @Override
    public void run() {
        System.out.println("Restore started @" + Timing.getDateTime());
        commit();
        System.out.println("Restore ended @" + Timing.getDateTime());
    }
    /*
     MrthodID=RS3
     */

    private void commit() {
        boolean tag_source_found = false;
        if (Files.exists(outfolder)) {
            if (Files.isReadable(outfolder)) {
                String data_folder_nm = instruct_map.get(Start_EgenVar1.SOURCE_DATA_DIR_FLAG);
                ArrayList<String> all_table_l = getCurrentTables();
                HashMap<String, ArrayList<Path>> table2path_map = new HashMap<>();
                try {
                    DirectoryStream<Path> ds = Files.newDirectoryStream(outfolder, "*.egen");
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
                            } else if (db_table != null) {
                                if (db_table.toUpperCase().contains("TAGSOURCE")) {
                                    Path tag_dir = Paths.get(data_folder_nm);
                                    if (Files.isDirectory(tag_dir) && Files.isReadable(tag_dir)) {
                                        try {
                                            String destintion = tag_dir.toString() + File.separatorChar + c_path.getFileName().toString();
                                            Path dest_path = Paths.get(destintion);
                                            if (Files.exists(dest_path)) {
                                                System.out.println("The file " + destintion + " already there. Not updated");
                                            } else {
                                                Files.copy(c_path, Paths.get(destintion));
                                                tag_source_found = true;
                                            }
                                        } catch (IOException ex) {
                                            System.out.println("Error RS3c : " + ex.getMessage() + " ");
                                            ex.printStackTrace();
                                        }

                                    } else {
                                        System.out.println("Error RS3a: Can not access " + data_folder_nm + " to place the tag sources. Tag sources will n ot be restored.");
                                    }
                                } else {
                                    System.out.println("Not using " + c_path + " (incompatible file name format or table not found " + db_table + ")");
                                }
                            } else {
                                System.out.println("Not using " + c_path + " (incompatible file name format or table not found " + db_table + ")");
                            }
//                            if (containsIgnoreCase(all_table_l, db_table)) {
//                                table2path_map.put(get_correct_table_name(db_table), c_path);
//                            } else {
//                                System.out.println("Not using " + c_path + " (incompatible file name format or table not found " + db_table + ")");
//                            }
                        } else {
                            System.out.println("Not using " + c_path + " (incompatible file name format)");
                        }
                    }
                    if (table2path_map.isEmpty()) {
                        System.out.println(" Nothing to process");
                    } else {
                        ArrayList<String> table_to_use_l = new ArrayList<>(table2path_map.keySet());
                        for (int i = 0; i < table_to_use_l.size(); i++) {
                            Collections.sort(table2path_map.get(table_to_use_l.get(i)));
                        }
                        table_to_use_l = decide_table_order(table_to_use_l, false);
                        for (int k = 0; k < table_to_use_l.size(); k++) {
                            String table = table_to_use_l.get(k);
                            ArrayList<Path> c_path_l = table2path_map.get(table);
                            for (int i = 0; i < c_path_l.size(); i++) {
                                Path c_path = c_path_l.get(i);
                                System.out.println("_______________\n" + c_path.toAbsolutePath());
                                ArrayList<String> result_l = readTemplate(c_path.toAbsolutePath().toString());
                            }

//                            HashMap<String, ArrayList<String>> result_map = readFromFileEncripted(c_path, enckey);
                        }
                    }
                } catch (IOException ex) {
                    System.out.println("Error RS3b : " + ex.getMessage() + " ");
                }
            }
        }
        if (tag_source_found) {
            System.out.println("Restart server to install the tagsources !");
        }
    }

//    /*
//     MrthodID=RS3
//     */
//    private void commit() {
//        if (Files.exists(outfolder)) {
//            if (Files.isReadable(outfolder)) {
//                try {
//                    ArrayList<String> all_table_l = getCurrentTables();
//                    HashMap<String, Path> table2path_map = new HashMap<>();
//
//                    DirectoryStream<Path> ds = Files.newDirectoryStream(outfolder, "*.egen");
//                    Iterator<Path> path_l = ds.iterator();
//                    while (path_l.hasNext()) {
//                        Path c_path = path_l.next();
//                        String[] path_str = c_path.getFileName().toString().split("__");
//                        if (path_str.length == 2) {
//                            String db_table = path_str[1].split("\\.")[0];
//                            if (containsIgnoreCase(all_table_l, db_table)) {
//                                table2path_map.put(get_correct_table_name(db_table), c_path);
//                            } else {
//                                System.out.println("Not using " + c_path + " (incompatible file name format or table not found " + db_table + ")");
//                            }
//                        } else {
//                            System.out.println("Not using " + c_path + " (incompatible file name format)");
//                        }
//
//                        try {
//                        } catch (Exception ex) {
//                            System.out.println("Error : " + ex.getMessage() + " ");
//                            ex.printStackTrace();
//                        }
//                    }
//                    if (table2path_map.isEmpty()) {
//                        System.out.println(" Nothing to process");
//                    } else {
//                        ArrayList<String> table_to_use_l = new ArrayList<>(table2path_map.keySet());
//                        table_to_use_l = decide_table_order(table_to_use_l, false);
//                        for (int k = 0; k < table_to_use_l.size(); k++) {
//                            String table = table_to_use_l.get(k);
//                            Path c_path = table2path_map.get(table);
//                            System.out.println("_______________\n" + c_path.toAbsolutePath());
//                            try {
//                                HashMap<String, ArrayList<String>> result_map = readFromFileEncripted(c_path, enckey);
////                            System.out.println("82 "+result_map );
//                                if (!result_map.isEmpty()) {
//                                    ArrayList<String> key_l = new ArrayList<>(result_map.keySet());
//                                    for (int i = 0; i < key_l.size(); i++) {
//                                        if (result_map.get(key_l.get(i)).isEmpty()) {
//                                            result_map.remove(key_l.get(i));
//                                        }
//                                    }
//                                    if (!result_map.isEmpty()) {
//                                        key_l = new ArrayList<>(result_map.keySet());
//                                        //order this according to dipendency
//                                        String first = key_l.get(0);//                               
//                                        String[] first_split = first.split("\\.");
//                                        String first_col = first_split[first_split.length - 1];
////                                        String table = first.replace("." + first_col, "");
//                                        table = get_correct_table_name(table);
//                                        String sql = "INSERT INTO " + table + "(" + first_col;
//                                        String val = "(?";
//                                        for (int i = 1; i < key_l.size(); i++) {
//                                            String c_col = key_l.get(i).split("\\.")[key_l.get(i).split("\\.").length - 1];
//                                            sql = sql + "," + c_col;
//                                            val = val + ",?";
//                                        }
//                                        sql = sql + ") values" + val + ")";
//                                        ResultSet r_1 = null;
//                                        Statement st_1 = null;
//                                        try {
//                                            if (getConnection() != null) {
//                                                PreparedStatement prep = getConnection().prepareStatement(sql);
//                                                if (st_1 == null) {
//                                                    st_1 = getConnection().createStatement();
//                                                }
//                                                boolean all_ok = false;
//                                                int more_to_proc = 0;
//                                                int tot_to_proc = 1;
//                                                int count = 0;
//                                                int new_added = 0;
//                                                int exiting_used = 0;
//                                                int last_print = -1;
//                                                while (!all_ok && more_to_proc >= 0) {
//                                                    count++;
//                                                    HashMap<String, String> value_map = new HashMap<>();
//                                                    for (int i = 0; (i < key_l.size() && !all_ok); i++) {
//                                                        String c_col = key_l.get(i).split("\\.")[key_l.get(i).split("\\.").length - 1];
//                                                        if (result_map.get(key_l.get(i)).isEmpty()) {
//                                                            all_ok = true;
//                                                            System.out.println("");
//                                                        } else {
//                                                            if (i == 0) {
//                                                                if (more_to_proc == 0) {
//                                                                    more_to_proc = result_map.get(key_l.get(i)).size();
//                                                                    tot_to_proc = result_map.get(key_l.get(i)).size();
//                                                                    System.out.println("Total to process=" + tot_to_proc);
//                                                                } else {
//                                                                    more_to_proc--;
//                                                                }
//
//                                                                int perc = (count * 100) / tot_to_proc;
//                                                                if (perc > last_print) {
//                                                                    if (perc % 10 == 0) {
//                                                                        System.out.print(perc + "%");
//                                                                    } else if (perc % 25 == 0) {
//                                                                        System.out.println(".");
//                                                                    } else {
//                                                                        System.out.print(".");
//                                                                    }
//                                                                    last_print = perc;
//                                                                }
//
//                                                            }
//                                                            String c_val = result_map.get(key_l.get(i)).remove(0);
//                                                            if (c_val != null) {
//                                                                c_val = c_val.replaceAll("[']{2,}", "'").replaceAll("[']{1}", "''");
//                                                            }
//
//                                                            value_map.put(c_col, c_val);
//                                                            int type = getColumnType(table, c_col);
//                                                            if (type == Type.INT) {
//                                                                if (c_val.matches("[0-9]+")) {
//                                                                    prep.setInt(i + 1, new Integer(c_val));
//                                                                } else {
//                                                                    prep.setInt(i + 1, -1);
//                                                                }
//                                                            } else if (type == Type.DOUBLE) {
//                                                                if (c_val.matches("[0-9\\.]+")) {
//                                                                    prep.setDouble(i + 1, new Double(c_val));
//                                                                } else {
//                                                                    prep.setDouble(i + 1, -1);
//                                                                }
//                                                            } else {
//                                                                prep.setString(i + 1, c_val);
//                                                            }
//                                                        }
//
//                                                    }
//                                                    if (!all_ok) {
//                                                        String sel_sql = createSql(table, value_map, "1", "select");
//                                                        if (sel_sql != null && st_1.executeQuery(sel_sql).next()) {
//                                                            exiting_used++;
//                                                        } else {
//                                                            prep.executeUpdate();
//                                                            prep.clearParameters();
//                                                            new_added++;
//                                                        }
//                                                    }
//                                                }
//                                                System.out.println("Records inserted:" + new_added + "\tRecords ignored to avoid duplicates:" + exiting_used);
//                                            }
//
//                                        } catch (SQLException ex) {
//                                            System.out.println("\nError RS1a: creating connections or error in query " + ex.getMessage() + "\nsql=" + sql);
////                                            ex.printStackTrace();
//                                        } finally {
//                                            close(null, null, r_1);
//                                        }
//                                    } else {
//                                        System.out.println("Nothing to update in " + c_path.toString());
//                                    }
//
//
//                                } else {
//                                    System.out.println("Nothing to update in " + c_path.toString());
//                                }
//                            } catch (Exception ex) {
//                                System.out.println("Error : " + ex.getMessage() + " ");
//                            }
//                            System.out.println(c_path.toAbsolutePath() + " -- Ended \n______________");
//                        }
//                    }
////                   
//
//
//                } catch (Exception ex) {
//                    System.out.println("Error :" + ex.getMessage());
//                }
//            } else {
//                System.out.println("Error: Write access denied for " + outfolder);
//            }
//        } else {
//            System.out.println("Error: could not access " + outfolder);
//        }
//    }
    /*
     Part of this methid from
     * http://www.java2s.com/Code/Java/Security/EncryptingandDecryptingwiththeJCE.htm
     */
    private String createSql(String table, HashMap<String, String> value_map, String selectFiled, String type) {
        table = get_correct_table_name(table);
        if (type.equalsIgnoreCase("select")) {
            ArrayList<String> value_l = new ArrayList<>(value_map.keySet());
            String sql = "SELECT " + selectFiled + " from " + table + " where ";
            HashMap<String, String[]> constraint_map = get_key_contraints(table);

            if (column2autoincrement_l == null) {
                column2autoincrement_l = new ArrayList<>();
            }

            if (constraint_map.containsKey(Start_EgenVar1.UNIQUE_TO_USER_COLUMNS_FLAG)) {
                String[] uniques_a = constraint_map.get(Start_EgenVar1.UNIQUE_TO_USER_COLUMNS_FLAG);
                ArrayList<String> uniques_l = new ArrayList<>(Arrays.asList(uniques_a));
                for (int i = 0; i < value_l.size(); i++) {
                    if (!containsIgnoreCase(uniques_l, value_l.get(i))) {
                        value_l.remove(i);
                        i--;
                    }
                }
            }
            if (!value_l.isEmpty()) {
                for (int i = 0; i < value_l.size(); i++) {
                    String c_key = value_l.get(i);
                    String value = value_map.get(c_key);
                    String qt = "'";
                    if (!shoulditbequated(table, c_key)) {
                        qt = "";
                    } else {
                        value = value.replaceAll("[']{2,}", "'").replaceAll("[']{1}", "''");
                    }
                    if (i == 0) {
                        sql = sql + c_key + "=" + qt + value + qt;

                    } else {
                        sql = sql + " AND " + c_key + "=" + qt + value + qt;
                    }
                }
                return sql;
            } else {
                return null;
            }

        } else if (type.equalsIgnoreCase("insert")) {
            String sql = "INSERT INTO " + table + "(";
            ArrayList<String> value_l = new ArrayList<>(value_map.keySet());
            String fields = "";
            String values = "";
            for (int i = 0; i < value_l.size(); i++) {
                String c_key = value_l.get(i);
                String qt = "'";
                if (!shoulditbequated(table, c_key)) {
                    qt = "";
                }
                if (!column2autoincrement_l.contains((table + "." + c_key).toUpperCase())) { //!c_key.equalsIgnoreCase("id")
                    String value = value_map.get(c_key);
                    if (fields.isEmpty()) {
                        fields = c_key;
                        values = qt + value + qt;
                    } else {
                        fields = fields + "," + c_key;
                        values = values + "," + qt + value + qt;
                    }
                }

            }
            sql = sql + fields + ") values (" + values + ")";
            return sql;
        } else if (type.equalsIgnoreCase("update")) {
            String sql = "UPDATE " + table + " SET ";
            ArrayList<String> value_l = new ArrayList<>(value_map.keySet());
            for (int i = 0; i < value_l.size(); i++) {
                String c_key = value_l.get(i);
                String value = value_map.get(c_key);
                String qt = "'";
                if (!shoulditbequated(table, c_key)) {
                    qt = "";
                }

                if (i == 0) {
                    sql = sql + " c_key=" + qt + value + qt;
                } else {
                    sql = sql + ",c_key=" + qt + value + qt;
                }
            }
            return sql;
        } else {
            return null;
        }
    }

    private HashMap<String, ArrayList<String>> readFromFileEncripted(Path file_nm, String enckey) {
        HashMap<String, ArrayList<String>> result_map = new HashMap<>();
        if (file_nm != null) {
            try {
                byte key[] = enckey.getBytes();
                DESKeySpec desKeySpec = new DESKeySpec(key);
                SecretKeyFactory keyFactory;
                keyFactory = SecretKeyFactory.getInstance("DES");
                SecretKey secretKey = keyFactory.generateSecret(desKeySpec);
                Cipher desCipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
                desCipher.init(Cipher.DECRYPT_MODE, secretKey);

                // Create stream
//                FileInputStream fis = new FileInputStream(file_nm);
                BufferedInputStream bis = new BufferedInputStream(Files.newInputStream(file_nm));
                CipherInputStream cis = new CipherInputStream(bis, desCipher);
                ObjectInputStream ois = new ObjectInputStream(cis);
                Object tmp_ob = ois.readObject();
                if (tmp_ob != null && tmp_ob instanceof HashMap) {
                    result_map = (HashMap<String, ArrayList<String>>) tmp_ob;
                }
            } catch (NoSuchAlgorithmException | InvalidKeyException | InvalidKeySpecException | NoSuchPaddingException | ClassNotFoundException ex) {
                System.out.println("Error: Decrypting file failed, specify correct key in the file key.file." + ex.getMessage());
                System.exit(5371);
            } catch (FileNotFoundException ex) {
                System.out.println("Error: Decrypting file failed, specify correct key in the file key.file. " + ex.getMessage());
                System.exit(5374);
            } catch (IOException ex) {
                System.out.println("Error: Decrypting file failed, specify correct key in the file key.file. " + ex.getMessage());
                System.exit(5346);
            }
        }
        return result_map;
    }

    /*
     MrthodID=RS10
     */
    private Connection getConnection() {
        try {
//            if (ncon == null || ncon.isClosed()) {
            Class.forName("org.apache.derby.jdbc.ClientDriver").newInstance();
            Connection ncon = DriverManager.getConnection(Start_EgenVar1.dbURL_dataEntry);
            return ncon;
//            }
        } catch (Exception ex) {
            String error = ex.getMessage();
            System.out.println("Error RS10a: " + error);
        }
        return null;
    }

    private Integer getColumnType(String current_tbl_nm, String column_nm) {
        int type = -999;
        String reqest_keynm = current_tbl_nm + "." + column_nm;
        reqest_keynm = reqest_keynm.toUpperCase();
        if (column2type_map == null) {
            column2type_map = new HashMap<>();
            column2autoincrement_l = new ArrayList<>(5);
            Connection ncon = null;
            try {
                ncon = getConnection();
                DatabaseMetaData metaData = ncon.getMetaData();
                if (!ncon.isClosed()) {
                    ArrayList<String> c_table_l = getCurrentTables();
                    for (int i = 0; i < c_table_l.size(); i++) {
                        String c_table = c_table_l.get(i);
                        String tablenm4metadata = c_table;
                        if (c_table.contains(".")) {
                            tablenm4metadata = c_table.split("\\.")[1];
                        }
                        ResultSet key_result = metaData.getColumns(null, instruct_map.get(Start_EgenVar1.DATABASE_NAME_DATA_FLAG), tablenm4metadata, null);
                        if (!key_result.next()) {
                            key_result = metaData.getColumns(null, instruct_map.get(Start_EgenVar1.DATABASE_NAME_DATA_FLAG).toUpperCase(), tablenm4metadata.toUpperCase(), null);
                        } else {
                            int typename = key_result.getInt("DATA_TYPE");
                            String autoincrmnt = key_result.getString("IS_AUTOINCREMENT");
                            String clmname = key_result.getString("COLUMN_NAME");
                            String key_name = instruct_map.get(Start_EgenVar1.DATABASE_NAME_DATA_FLAG) + "." + tablenm4metadata + "." + clmname;
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
                            String key_name = instruct_map.get(Start_EgenVar1.DATABASE_NAME_DATA_FLAG) + "." + tablenm4metadata + "." + clmname;
                            key_name = key_name.toUpperCase();
                            column2type_map.put(key_name, typename);
                            if (autoincrmnt.equalsIgnoreCase("YES")) {
                                column2autoincrement_l.remove(key_name.toUpperCase());
                                column2autoincrement_l.add(key_name.toUpperCase());
                            }
                        }
                        key_result.close();
                    }
                }
                ncon.close();
            } catch (SQLException e) {
                System.out.println("Error C1a" + e.getMessage());
            } finally {
                close(ncon, null, null);
            }
        }
        if (column2type_map.containsKey(reqest_keynm)) {
            type = column2type_map.get(reqest_keynm);
        }
        return type;
    }
    /*
     MrthodID=RS9
     */

    private ArrayList<String> getCurrentTables() {
        if (table_l == null) {
            table_l = new ArrayList<>();
            Statement stmt = null;
            Connection ncon = null;
//        if (getConnection() != null) {
            try {
                ncon = getConnection();
                stmt = ncon.createStatement();
                String q = "select TABLENAME from sys.systables WHERE UPPER(CAST(TABLETYPE AS CHAR(1))) = 'T' and SCHEMAID=(select SCHEMAID from SYS.SYSSCHEMAS where UPPER(CAST(SCHEMANAME AS VARCHAR(128)))=UPPER('" + instruct_map.get(Start_EgenVar1.DATABASE_NAME_DATA_FLAG).toUpperCase() + "'))";
                ResultSet r_1 = stmt.executeQuery(q);
                while (r_1.next()) {
                    table_l.add(instruct_map.get(Start_EgenVar1.DATABASE_NAME_DATA_FLAG).toUpperCase() + "." + r_1.getString(1));
                }
                r_1.close();
                ncon.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            } finally {
                close(ncon, stmt, null);
            }
        }

//        }
        return table_l;
    }
    /*
     MrthodID=RS8
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

    private HashMap<String, String[]> get_key_contraints(String current_tbl_nm) {
        if (key_constraint_map == null) {
            key_constraint_map = new HashMap<>();
        }
        HashMap<String, String[]> returning_map = new HashMap<>(1);
        if (current_tbl_nm != null && !current_tbl_nm.isEmpty() && !current_tbl_nm.equalsIgnoreCase("null") && !key_constraint_map.containsKey(current_tbl_nm)) {
            Connection ncon = null;
            try {
                ncon = getConnection();
                if (ncon != null) {
                    returning_map.putAll(get_key_contraint_names(current_tbl_nm));
                    try {
                        ArrayList<String> foreign_columns = new ArrayList<>(1);
                        ArrayList<String> foreign_key_names_columns = new ArrayList<>(1);
                        ArrayList<String> foreign_tables = new ArrayList<>(1);
                        DatabaseMetaData metaData = ncon.getMetaData();
                        String tablenm4metadata = current_tbl_nm.split("\\.")[current_tbl_nm.split("\\.").length - 1];

                        ResultSet key_result = metaData.getImportedKeys(ncon.getCatalog(), null, tablenm4metadata);
                        if (!key_result.next()) {
                            key_result = metaData.getImportedKeys(ncon.getCatalog(), instruct_map.get(Start_EgenVar1.DATABASE_NAME_DATA_FLAG), tablenm4metadata.toUpperCase());
                        } else {
                            String[] tmp_a = new String[4];
                            tmp_a[0] = key_result.getString("FKCOLUMN_NAME");//column_name                                 
                            tmp_a[1] = get_correct_table_name(key_result.getString("PKTABLE_NAME"));//ref_tblm
                            tmp_a[2] = key_result.getString("PKCOLUMN_NAME");//ref_tbl_clm_nm 
                            tmp_a[3] = key_result.getString("FK_NAME");//Key name
                            foreign_columns.add(tmp_a[0]);
                            returning_map.put(tmp_a[0], tmp_a);
                            foreign_tables.add(tmp_a[1]);
                            foreign_key_names_columns.add(tmp_a[3]);
                        }
                        while (key_result.next()) {
                            String[] tmp_a = new String[4];
                            tmp_a[0] = key_result.getString("FKCOLUMN_NAME");//column_name
                            tmp_a[1] = get_correct_table_name(key_result.getString("PKTABLE_NAME"));//ref_tblm
                            tmp_a[2] = key_result.getString("PKCOLUMN_NAME");//ref_tbl_clm_nm 
                            tmp_a[3] = key_result.getString("FK_NAME");//Key name
                            foreign_columns.add(tmp_a[0]);
                            returning_map.put(tmp_a[0], tmp_a);
                            foreign_tables.add(tmp_a[1]);
                            foreign_key_names_columns.add(tmp_a[3]);

                        }
                        if (!foreign_columns.isEmpty()) {
                            String[] tmp_a = new String[foreign_columns.size()];
                            for (int i = 0; i < foreign_columns.size(); i++) {
                                tmp_a[i] = foreign_columns.get(i);
                            }
                            returning_map.put(Start_EgenVar1.FOREIGN_KEY_COLUMNS_FLAG, tmp_a);
                        }

                        if (!foreign_tables.isEmpty()) {
                            String[] tmp_a = new String[foreign_tables.size()];
                            for (int i = 0; i < tmp_a.length; i++) {
                                tmp_a[i] = foreign_tables.get(i);
                            }
                            returning_map.put(Start_EgenVar1.FOREIGN_TABLE_FLAG, tmp_a);
                        }
                        if (!foreign_key_names_columns.isEmpty()) {
                            String[] tmp_a = new String[foreign_key_names_columns.size()];
                            for (int i = 0; i < tmp_a.length; i++) {
                                tmp_a[i] = foreign_key_names_columns.get(i);
                            }
                            returning_map.put(Start_EgenVar1.FOREIGN_KEY_NAMES_FLAG, tmp_a);
                        }
//                        if (unique_a != null) {
//                            returning_map.put(UNIQUE_TO_USER_COLUMNS_FLAG, unique_a);
//                        }
                        key_result.close();
                    } catch (SQLException e) {
                        System.out.println("Error 8767" + e.getMessage());
                    }
                    ncon.close();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            } finally {
                close(ncon, null, null);
            }
            key_constraint_map.put(current_tbl_nm, returning_map);
            return returning_map;
        } else if (key_constraint_map.containsKey(current_tbl_nm)) {
            return key_constraint_map.get(current_tbl_nm);
        }
        return returning_map;
    }

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
                System.out.println("Error AS19: creating connections " + ex.getMessage());
            }
        }

        return found;
    }

    public boolean shoulditbequated(String current_tbl_nm, String column_nm) {
        boolean result = true;
        column_nm = column_nm.split("\\.")[column_nm.split("\\.").length - 1];
        int type = getColumnType(current_tbl_nm, column_nm);
        if (type > -999) {
            if (numerical_types == null) {
                numerical_types = new ArrayList<>(3);
                numerical_types.add(Types.INTEGER);
                numerical_types.add(Types.BIGINT);
                numerical_types.add(Types.DOUBLE);

            }
            if (numerical_types.contains(type)) {
                result = false;
            }
        }
        return result;
    }

    private HashMap<String, String[]> get_key_contraint_names(String current_tbl_nm) {
        HashMap<String, String[]> returning_map = new HashMap<>();
        if (current_tbl_nm != null && !current_tbl_nm.isEmpty() && !current_tbl_nm.equalsIgnoreCase("null")) {
            Connection ncon = null;
            try {
                ncon = getConnection();
                if (ncon != null) {
                    try {
                        DatabaseMetaData metaData = ncon.getMetaData();
                        String tablenm4metadata = current_tbl_nm;
                        if (current_tbl_nm.contains(".")) {
                            tablenm4metadata = current_tbl_nm.split("\\.")[1];
                        }
                        ResultSet key_result = metaData.getIndexInfo(null, instruct_map.get(Start_EgenVar1.DATABASE_NAME_DATA_FLAG), tablenm4metadata, false, false);//.getImportedKeys(ncon.getCatalog(), Constants.DATABASE_NAME_DATA, current_tbl_nm);
                        ArrayList<String> uniqs_list = new ArrayList<>(1);
                        ArrayList<String> all_list = new ArrayList<>(2);
                        if (!key_result.next()) {
                            key_result = metaData.getIndexInfo(null, instruct_map.get(Start_EgenVar1.DATABASE_NAME_DATA_FLAG).toUpperCase(), tablenm4metadata.toUpperCase(), false, false);//.getImportedKeys(ncon.getCatalog(), Constants.DATABASE_NAME_DATA, current_tbl_nm);
                        } else {
                            String index_name = key_result.getString("INDEX_NAME");
                            all_list.add(index_name);
                            index_name = index_name.toUpperCase();
                            if (index_name.startsWith(Start_EgenVar1.UNIQUE_ID_INDEX_NAME_PREFIX)) {
                                uniqs_list.add(key_result.getString("COLUMN_NAME"));
                            }
                        }
                        while (key_result.next()) {
                            String index_name = key_result.getString("INDEX_NAME");
                            if (index_name != null) {
                                all_list.add(index_name);
                                index_name = index_name.toUpperCase();
                                if (index_name.startsWith(Start_EgenVar1.UNIQUE_ID_INDEX_NAME_PREFIX)) {
                                    uniqs_list.add(key_result.getString("COLUMN_NAME"));
                                }
                            }
                        }
                        if (!uniqs_list.isEmpty()) {
                            String[] uniq_to_user_a = new String[uniqs_list.size()];
                            for (int i = 0; i < uniq_to_user_a.length; i++) {
                                uniq_to_user_a[i] = uniqs_list.get(i);
                            }
                            returning_map.put(Start_EgenVar1.UNIQUE_TO_USER_COLUMNS_FLAG, uniq_to_user_a);
                        }
                        if (!all_list.isEmpty()) {
                            String[] all_a = new String[all_list.size()];
                            for (int i = 0; i < all_a.length; i++) {
                                all_a[i] = all_list.get(i);
                            }
                            returning_map.put(Start_EgenVar1.ALL_INDEX_NAMES_FLAG, all_a);
                        }


                        key_result.close();

                    } catch (SQLException e) {
                        System.out.println("Error 8767" + e.getMessage());
                    }
                    ncon.close();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            } finally {
                close(ncon, null, null);
            }

        }
        return returning_map;
    }

    /**
     * MethodID=RS30
     *
     */
    public ArrayList<String> decide_table_order(ArrayList<String> table_l, boolean reverse) {
        ArrayList<String> tables_orderd_l = new ArrayList<>(10);
        HashMap<String, ArrayList<String>> dipendancy_map = new HashMap<>();
        if (table_l != null) {
            for (int i = 0; i < table_l.size(); i++) {
                HashMap<String, String[]> constraint_result_map = get_key_contraints(table_l.get(i));
                if (constraint_result_map.containsKey(Start_EgenVar1.FOREIGN_TABLE_FLAG)) {
                    ArrayList<String> tmp_dipendancy_l = new ArrayList<>(Arrays.asList(constraint_result_map.get(Start_EgenVar1.FOREIGN_TABLE_FLAG)));
                    dipendancy_map.put(table_l.get(i), tmp_dipendancy_l);
                } else {
                    tables_orderd_l.add(table_l.get(i));
                }
            }
            if (tables_orderd_l.isEmpty()) {
                System.out.println("Error RS30: Cyclic dependency can not be handlled with the current implementaiotn. Please inform this error to the developers.");
            } else {
                if (!dipendancy_map.isEmpty()) {
                    ArrayList<String> dependent_tables_l = new ArrayList<>(dipendancy_map.keySet());
                    int safty = 100;
                    while (safty > 0 && !dependent_tables_l.isEmpty()) {
                        safty--;
                        for (int i = 0; ((!dependent_tables_l.isEmpty()) && i < dependent_tables_l.size()); i++) {
                            String c_tbl = dependent_tables_l.remove(i);
                            ArrayList<String> c_depnds_l = dipendancy_map.get(c_tbl);
                            if (tables_orderd_l.containsAll(c_depnds_l)) {
                                tables_orderd_l.add(c_tbl);
                                i--;
                            } else {
                                dependent_tables_l.add(c_tbl);
                            }
                        }
                    }
                    if (safty < 0) {
                        System.out.println("Error RS30a:Not all tables evaluated. ");
                    }
                }


            }
        }
        if (reverse) {
            ArrayList<String> tables_orderd_reverse_l = new ArrayList<>();
            for (int i = 0; i < tables_orderd_l.size(); i++) {
                tables_orderd_reverse_l.add(0, tables_orderd_l.get(i));
            }
            return tables_orderd_reverse_l;
        } else {
            return tables_orderd_l;
        }
    }

    /*
     MethodID=RS12
     */
    private ArrayList<String> readTemplate(String filenm) {
        System.out.println("Cheking = " + filenm);
        HashMap<Long, HashMap<String, HashMap<String, String>>> data_map = new HashMap<>();
        ArrayList<String> content_l = null;
        File tmpl_file = new File(filenm);
        int nuofcolmns = 0;
        boolean limited = false;
//        int maxlimit = 10000;
        boolean allok = true;
//        ArrayList<String> c_name_l = new ArrayList<>(1);
        String relationship_type = null;
//        HashMap<String, ArrayList<String>> vlaue_map = new HashMap<>();
        ArrayList<String> column_nms = new ArrayList<>(2);

        if (tmpl_file.isFile() && tmpl_file.canRead()) {
            try {
                Scanner scan = new Scanner(tmpl_file);
                long idColumns = 0;
                long rownum = 0;
                ArrayList<String[]> pos_l = new ArrayList<>();
//                LinkedHashMap<String, LinkedHashMap<String, String>> template_map_map = new LinkedHashMap<>();
                while (!limited && scan.hasNext() && allok) {
                    String line = scan.nextLine();
                    if (line.startsWith("##")) {
                        line = line.replaceAll("#", "").replaceAll("\\*", "").trim();
                        String[] colnm_a = line.split("\t");
                        nuofcolmns = colnm_a.length;
                        for (int i = 0; i < colnm_a.length; i++) {
                            String c_tbl = null;
                            String c_col = null;
                            String[] c_col_a = colnm_a[i].split("\\.");
                            if (c_col_a.length >= 2) {
                                c_tbl = get_correct_table_name(c_col_a[c_col_a.length - 2]);
                                c_col = c_col_a[c_col_a.length - 1];
                                if (c_tbl != null) {
//                                    if (!template_map_map.containsKey(c_tbl)) {
//                                        template_map_map.put(c_tbl, new LinkedHashMap<String, String>());
//                                    }
//                                    template_map_map.get(c_tbl).put(c_col, null);
                                    String[] tmp_a = new String[2];
                                    tmp_a[0] = c_tbl;
                                    tmp_a[1] = c_col;
                                    pos_l.add(tmp_a);
                                } else {
                                    System.out.println("Error RS12g: Invalid table " + c_col_a[c_col_a.length - 2]);
                                }
                            } else {
                                System.out.println("Error RS12f: The column name incorrect. expected DATABSE_NAME.COLUMNNAME found " + colnm_a[i]);
                            }

//                            if (colnm_a[i].contains(".")) {
////                                vlaue_map.put(colnm_a[i], new ArrayList<String>(1));
//                                column_nms.add(colnm_a[i]);
//                                if (colnm_a[i].toUpperCase().endsWith(".ID")) {
//                                    idColumns++;
//                                }
//                            } else {
//                                vlaue_map.put(colnm_a[i], null);
//                            column_nms.add(colnm_a[i]);
//                            }
                        }
                    } else if (line.startsWith("#")) {
                        if (line.contains(Start_EgenVar1.RELATIONSHIP_TYPE_FLAG)) {
                            allok = false;
                            line = line.replace(Start_EgenVar1.RELATIONSHIP_TYPE_FLAG, "").replace("#", "").trim();
                            if (!line.isEmpty()) {
                                relationship_type = line;
                            }
                        }
                    } else if (!line.isEmpty()) {
                        rownum++;
//                        line = line.replaceAll("\\*", "").trim();
                        String[] values_a = line.split("\t");
                        if (nuofcolmns != values_a.length) {
                            allok = false;
                            System.out.println("Error RS12i: The row " + rownum + " has different column number than the title. Expected= " + nuofcolmns + ". Found=" + values_a.length);
                        } else {
                            if (pos_l.size() == values_a.length) {
                                HashMap<String, HashMap<String, String>> template_map_map = new HashMap<>();
                                for (int i = 0; i < pos_l.size(); i++) {
                                    String[] c_pos_a = pos_l.get(i);
                                    if (!template_map_map.containsKey(c_pos_a[0])) {
                                        HashMap<String, String> template_map = new HashMap<>();
                                        template_map.put(c_pos_a[1], values_a[i]);
                                        template_map_map.put(c_pos_a[0], template_map);
                                    } else {
                                        if (!template_map_map.get(c_pos_a[0]).containsKey(c_pos_a[1])) {
                                            template_map_map.get(c_pos_a[0]).put(c_pos_a[1], values_a[i]);
                                        } else {
                                            System.out.println("Error RS12j: Mismatch error ");
                                        }
                                    }
                                }
                                data_map.put(rownum, template_map_map);
                            } else {
                                System.out.println("Error RS12h: Column number mismatch " + line);
                            }
//
//                            for (int i = 0; i < values_a.length; i++) {
////                                if (vlaue_map.get(column_nms.get(i)) == null) {
//////                                    allok = false;
//////                                    System.out.println("Error 12b: Error in " + rownum + ".Failed to find value for " + values_a[i]);
////                                } else {
////                                    vlaue_map.get(column_nms.get(i)).add(values_a[i]);
////                                }
//                            }
//                            data_map.put(rownum, c_full_map);
                        }
                    }
                }
                scan.close();
                System.out.print(rownum + " rows to be processed  ... ");
            } catch (FileNotFoundException ex) {
                System.out.println("Error 12c: Failed to read template file " + filenm + ": " + ex.getMessage());
            }
        } else {
            System.out.println("Error 12d: Failed to read template file " + filenm);
        }
//        if (maxlimit <= 0) {
//            System.out.println("Error 12f: The " + filenm + " contains more than the maximum number of raws. Please split this file");
//            return null;
//        } else {

        if (allok) {
//            System.out.println("966 "+data_map);
//            for (int i = 0; i < column_nms.size(); i++) {
//                if (vlaue_map.containsKey(column_nms.get(i)) && vlaue_map.get(column_nms.get(i)) == null) {
//                    vlaue_map.remove(column_nms.get(i));
//                    System.out.println("Not processing " + column_nms.get(i));
//                }
//            }
//            ArrayList<String> key_l = new ArrayList<>(vlaue_map.keySet());

//            HashMap<Integer, HashMap<String, HashMap<String, String>>> data_map = list2map(parameter_list, stm_1);
//            System.out.println("\n916 " + vlaue_map);
//            content_l = map2list(vlaue_map);
//            System.out.println("927 content_l=" + content_l);
//
//            System.out.print(".");
//            int splits = (content_l.size() / Start_EgenVar1.split_limit) + 1;
//            for (int j = 0; j <= splits; j++) {
//                int min = j * Start_EgenVar1.split_limit;
//                int max = (j + 1) * Start_EgenVar1.split_limit;
//                int min_pos = 0;
//                int max_pos = content_l.size() - 1;
//                if (max_pos < 0) {
//                    max_pos = 0;
//                }
//                if (min < max_pos) {
//                    min_pos = min;
//                }
//                if (max_pos > max) {
//                    max_pos = max;
//                }
//
//                ArrayList<String> tmp_l = new ArrayList<>();//;
//                tmp_l.addAll(content_l.subList(min_pos, max_pos));
//                System.out.println("948 "+tmp_l);
            System.out.println("Reading template = " + filenm);
            String[] result_a = pupolateFromTemplate(data_map);
//                tmp_l.clear();
//
//            }

//            for (int i = 1; i < result_a.length; i++) {
//                System.out.println(i + ") " + result_a[i]);
//
//            }
//            content_l = map2list(vlaue_map);           
            return content_l;
        } else {
            System.out.println("Error 12h: Failed to process " + filenm);
            return null;
        }
    }

    private void createUndoPoint(String undos, String out_dir) {
        if (undos != null && !undos.isEmpty()) {
            if (out_dir == null) {
                out_dir = "";
            } else if (!out_dir.endsWith(File.separator)) {
                out_dir = out_dir + File.separator;
            }
            try {
                FileWriter undo = new FileWriter(out_dir + "." + Timing.getDateTimeForFileName() + Start_EgenVar1.EGENVAR_UNDO_FILENM, false);
                undo.append(undos);
                undo.close();
                String[] split = undos.split("\\|");
//                if(split[0].toUpperCase().endsWith(".REPORT")){
//                   FileWriter marker = new FileWriter(out_dir + "." + Timing.getDateTimeForFileName() + EGENVAR_UNDO_FILENM, false);
//                undo.append(undos);
//                undo.close();
//                }
            } catch (IOException ex) {
                System.out.println("Error 7: creating undo point : " + ex.getMessage());
            }
        }
    }

    private ArrayList<String> map2list(HashMap<String, ArrayList<String>> vlaue_map) {
        System.out.println("");
        ArrayList<String> retuen_l = new ArrayList<>(vlaue_map.size());
        ArrayList<String> mapkeys_l = new ArrayList<>(vlaue_map.keySet());
        for (int i = 0; i < mapkeys_l.size(); i++) {
            System.out.print("\n" + (i + 1) + " of " + mapkeys_l.size());
            ArrayList<String> c_val_l = vlaue_map.get(mapkeys_l.get(i));
            if (c_val_l != null && !c_val_l.isEmpty()) {
                StringBuilder valuse = new StringBuilder();
                valuse.append(c_val_l.get(0));
                int lastprog = 0;
                for (int j = 1; j < c_val_l.size(); j++) {
                    int progr = (j * 100) / c_val_l.size();
                    if (progr > lastprog) {
                        lastprog = progr;
                        if (progr % 20 == 0) {
                            System.out.print(progr + "%");
                        } else if (progr % 5 == 0) {
                            System.out.print(".");
                        }
                    }
                    valuse.append(";;");
                    valuse.append(c_val_l.get(j));
//                    valuse = valuse + ";;" + c_val_l.get(j);
                }
                retuen_l.add(mapkeys_l.get(i) + "==" + valuse);
            }
        }
        return retuen_l;
    }

    /**
     * MethodID=RS32
     *
     */
    private String[] pupolateFromTemplate(HashMap<Long, HashMap<String, HashMap<String, String>>> data_map) {
        String result = "";
        String[] report_a = new String[3];
        HashMap<String, ArrayList<Integer>> table2id_map = new HashMap<>();
        Savepoint save1 = null;
        Connection ncon = null;
        Statement stm_1 = null;
        boolean allok = true;
        try {
            counter++;
            ncon = getConnection();
            ncon.setAutoCommit(false);
            save1 = ncon.setSavepoint();
            stm_1 = ncon.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
        } catch (Exception ex) {
            msges = "Error ASW32a: cre4ating connections " + ex.getMessage();
            close(ncon, stm_1, null);
        }
//        HashMap<Integer, HashMap<String, HashMap<String, String>>> data_map = list2map(parameter_list, stm_1);

        if (data_map != null && !data_map.isEmpty()) {
            ArrayList<Long> top_key = new ArrayList<>(data_map.keySet());
            ArrayList<String> used_table_l = getTablesUsed(data_map.get(top_key.get(0)));
            HashMap<String, HashMap<String, String>> supli_map = get_connecting_tables(used_table_l);
            for (int k = 0; k < top_key.size(); k++) {
                data_map.get(top_key.get(k)).putAll(supli_map);
            }
            ArrayList<String> tablse_list = new ArrayList<>(data_map.get(top_key.get(0)).keySet());
            HashMap<String, HashMap<String, String>> table2ForeingConnection_map = new HashMap<>();
            ArrayList<String> new_tablse_list = new ArrayList<>();
            for (int j = 0; j < tablse_list.size(); j++) {
                ArrayList<String[]> reverse_key_map_l = get_reverse_key_contraints(tablse_list.get(j));
                for (int i = 0; i < reverse_key_map_l.size(); i++) {
                    if (reverse_key_map_l.get(i).length >= 3) {
                        HashMap<String, String[]> sub_constraints_map = get_key_contraints(reverse_key_map_l.get(i)[0]);
                        String[] unique_a = sub_constraints_map.get(Start_EgenVar1.UNIQUE_TO_USER_COLUMNS_FLAG);
                        HashMap<String, String> tp_map = new HashMap<>();
                        if (unique_a != null) {
                            boolean addthis = true;
                            for (int k = 0; (k < unique_a.length && addthis); k++) {
                                if (sub_constraints_map.containsKey(unique_a[k])) {
                                    if (!tablse_list.contains(sub_constraints_map.get(unique_a[k])[1])) {
                                        addthis = false;
                                    } else {
                                        tp_map.put(unique_a[k], null);
                                    }
                                } else {
                                    addthis = false;
                                }
                            }
                            if (addthis) {
                                for (int k = 0; k < top_key.size(); k++) {
                                    data_map.get(top_key.get(k)).put(reverse_key_map_l.get(i)[0], tp_map);
                                    new_tablse_list.remove(reverse_key_map_l.get(i)[0]);
                                    new_tablse_list.add(reverse_key_map_l.get(i)[0]);
                                }
                            }
                        }
                    }
                }
            }
            tablse_list.addAll(new_tablse_list);
            top_key = new ArrayList<>(data_map.keySet());
            for (int j = 0; j < tablse_list.size(); j++) {
                String c_tbl = tablse_list.get(j);
                HashMap<String, String[]> constraints_map = get_key_contraints(c_tbl);
                String[] foreingKeyColumns_a = constraints_map.get(Start_EgenVar1.FOREIGN_KEY_COLUMNS_FLAG);
                if (foreingKeyColumns_a != null) {
                    HashMap<String, String> foriegnKey2Table = new HashMap<>();
                    for (int k = 0; k < foreingKeyColumns_a.length; k++) {
                        String[] foreingDetails_a = constraints_map.get(foreingKeyColumns_a[k]);
                        if (foreingDetails_a != null && foreingDetails_a.length > 0) {
                            foriegnKey2Table.put(foreingDetails_a[0], foreingDetails_a[1]);
                        }
                    }
                    table2ForeingConnection_map.put(c_tbl, foriegnKey2Table);
                }
            }
            ArrayList<String> orderd_table_list = decide_table_order(tablse_list, false);
            HashMap<String, Integer> already_checked_map = new HashMap<>();
            for (int j = 0; (j < orderd_table_list.size() && allok); j++) {
                result = result + "\nPopulating  " + orderd_table_list.get(j) + " ...... ";
                int useexsiting = 0;
                int createnew = 0;
                int lastprog = 0;
                System.out.println("Sending " + top_key.size() + " to " + orderd_table_list.get(j));
                for (int i = 0; (i < top_key.size() && allok); i++) {
                    int progr = (i * 100) / top_key.size();
                    if (progr > lastprog) {
                        lastprog = progr;
                        System.out.print(".");
                        if (progr % 20 == 0) {
                            System.out.println(" " + progr + "%");
                        } else if (progr == 99) {
                            System.out.println(" 100%");
                        }

                    }
                    HashMap<String, HashMap<String, String>> table_value_map = data_map.get(top_key.get(i));
                    HashMap<String, String> c_value_map = table_value_map.get(orderd_table_list.get(j));
                    if (table2ForeingConnection_map.containsKey(orderd_table_list.get(j))) {
                        HashMap<String, String> foriegnKey2Table = table2ForeingConnection_map.get(orderd_table_list.get(j));
                        ArrayList<String> foriegnKey2Table_key_l = new ArrayList<>(foriegnKey2Table.keySet());
                        for (int k = 0; k < foriegnKey2Table_key_l.size(); k++) {
                            String targetid = null;
                            if (data_map.get(top_key.get(i)).get(foriegnKey2Table.get(foriegnKey2Table_key_l.get(k))) != null) {
                                targetid = data_map.get(top_key.get(i)).get(foriegnKey2Table.get(foriegnKey2Table_key_l.get(k))).get("id");
                            }
                            if (targetid != null && targetid.matches("[0-9]+") && data_map.get(top_key.get(i)).containsKey(orderd_table_list.get(j))) {
                                data_map.get(top_key.get(i)).get(orderd_table_list.get(j)).put(foriegnKey2Table_key_l.get(k), targetid);
                            }
                        }
                    }
                    int created_id = -1;
                    created_id = getOrAdd(true, false, stm_1, orderd_table_list.get(j), c_value_map, table2id_map);
                    if (created_id < 0) {
                        created_id = getOrAdd(false, true, stm_1, orderd_table_list.get(j), c_value_map, table2id_map);
                        createnew++;
                    } else {
                        useexsiting++;
                    }
                    if (created_id >= 0) {
                        data_map.get(top_key.get(i)).get(orderd_table_list.get(j)).put("id", created_id + "");
                    } else {
                        allok = false;
                        result = result + " Failed:" + orderd_table_list.get(j);
                    }
                }

                if (allok) {
                    result = result + " OK. New reacords created=" + createnew + "| existing records used=" + useexsiting;
                } else {
                    result = result + " Failed";
                }
            }
            already_checked_map.clear();
        }


        try {
            if (allok && ncon != null && !ncon.isClosed()) {
                ncon.commit();
                ArrayList<String> table2id_keyl = new ArrayList<>(table2id_map.keySet());
                String undos = "";
                for (int i = 0; i < table2id_keyl.size(); i++) {
                    undos = undos + table2id_keyl.get(i) + "|" + table2id_map.get(table2id_keyl.get(i)) + "\n";
                }
                result = result + "\n All updates committed ";
                report_a[1] = undos;

            } else if (ncon != null) {
                if (save1 != null) {
                    result = result + "\n Rolback started";
                    ncon.rollback(save1);
                    result = result + "\n No changes were made to the database";
                }
            }
            if (stm_1 != null) {
                stm_1.close();
            }
            if (ncon != null && !ncon.isClosed()) {
                ncon.close();
            }
        } catch (SQLException ex) {
            System.out.println("\nError RS32c: " + ex.getMessage());
            msges = "Error RS32b: creating connections " + ex.getMessage();
        } finally {
            close(ncon, stm_1, null);
        }
        report_a[0] = result;
        report_a[2] = msges;
        if (msges != null) {
            System.out.println("Error RS32a:" + msges + "\n");
            msges = null;
        }

        return report_a;

    }

    /**
     * MethodID=RS11
     *
     */
    private int getOrAdd(boolean donotcreate, boolean donotselect, Statement st_1, String table, HashMap<String, String> valu_map,
            HashMap<String, ArrayList<Integer>> table2id_map) {
        table = get_correct_table_name(table);
        if (valu_map == null || valu_map.isEmpty()) {
            msges = msges + "\nError 14d: Not enough information for " + table;
        } else {
            try {
                String select_sql = createSql(table, valu_map, "id", "select");
                String sql = select_sql;
                int id = -1;
                if (st_1 != null && !st_1.isClosed() && select_sql != null) {
                    try {
                        if (!donotselect) {
                            ResultSet r_1 = st_1.executeQuery(select_sql);
                            while (r_1.next()) {
                                id = r_1.getInt(1);
                                if (r_1.wasNull()) {
                                    id = -1;
                                }
                            }
                            r_1.close();
                        }
                        if (id <= 0 && !donotcreate) {
                            String insert_sql = createSql(table, valu_map, "id", "insert");
                            sql = insert_sql;
                            int resutl = st_1.executeUpdate(insert_sql);
                            if (resutl > 0) {
                                ResultSet r_2 = st_1.executeQuery(select_sql);
                                while (r_2.next()) {
                                    id = r_2.getInt(1);
                                    if (id >= 0) {
                                        if (table2id_map != null) {
                                            if (table2id_map.containsKey(table)) {
                                                table2id_map.get(table).add(id);
                                            } else {
                                                ArrayList<Integer> tmp_set = new ArrayList<>();
                                                tmp_set.add(id);
                                                table2id_map.put(table, tmp_set);
                                            }
                                        }
                                    }
                                }
                                r_2.close();
                            }
                        }
                        return id;
                    } catch (SQLException e) {
                        msges = msges + "Error AS11e: " + e.getMessage() + "\n for query =" + sql;
                    }
                } else {
                    msges = msges + "Error AS11b: Selection failed, the query must have atleast one of the key columns";
                }
            } catch (SQLException ex) {
                msges = msges + "\n Error AS11c" + ex.getMessage();
            }
        }
        return -1;
    }

    /**
     * MethodID=RS20
     *
     */
    private HashMap<String, HashMap<String, String>> get_connecting_tables(ArrayList<String> tables_l) {
        HashMap<String, HashMap<String, String>> supli_map = new HashMap<>();
        ArrayList<String> dipendents_l = new ArrayList<>(1);
        ArrayList<String> all_tabes_l = getCurrentTables();
        for (int i = 0; i < all_tabes_l.size(); i++) {
            String c_t = all_tabes_l.get(i);
            if (!containsIgnoreCase(tables_l, c_t)) {
                HashMap<String, String[]> constra_map = get_key_contraints(c_t);
                if (constra_map.containsKey(Start_EgenVar1.FOREIGN_TABLE_FLAG)) {
                    String[] fortbl_a = constra_map.get(Start_EgenVar1.FOREIGN_TABLE_FLAG);
                    if (fortbl_a.length > 1) {
                        boolean found_none_satis = false;
                        for (int j = 0; (j < fortbl_a.length && !found_none_satis); j++) {
                            if (!containsIgnoreCase(tables_l, fortbl_a[j])) {
                                found_none_satis = true;
                            }
                        }
//                        if (!found_none_satis) {
//                            for (int j = 0; j < fortbl_a.length; j++) {
//                                String c_tc = fortbl_a[j];
//                                if (containsIgnoreCase(tables_l, c_tc)) {
//                                    if (!dipendents_l.contains(c_t)) {
//                                        dipendents_l.add(c_t);
//                                        String[] fkey_colsl_a = constra_map.get(Start_EgenVar1.FOREIGN_KEY_COLUMNS_FLAG);
//                                        for (int k = 0; k < fkey_colsl_a.length; k++) {
//                                            HashMap<String, String> tmp_map = new HashMap<>();
//                                            tmp_map.put(constra_map.get(fkey_colsl_a[k])[0], "-1");
//                                            supli_map.put(c_t, tmp_map);
//                                        }
//                                    }
//                                }
//                            }
//                        }
                    }
                }
            }
        }
        return supli_map;
    }

    /**
     * MethodID=RS21
     *
     */
    private ArrayList< String[]> get_reverse_key_contraints(String current_tbl_nm) {
        if (returning_list_map == null) {
            returning_list_map = new HashMap<>();
        }
        if (!returning_list_map.containsKey(current_tbl_nm)) {
            ArrayList<String[]> returning_list = new ArrayList<>(1);
            if (current_tbl_nm != null && !current_tbl_nm.isEmpty() && !current_tbl_nm.equalsIgnoreCase("null")) {
                Connection ncon = null;
                Statement st_1 = null;
                ResultSet r_1 = null;
                try {
                    String sql = "select distinct table_name from  " + get_correct_table_name("tablename2feature") + " where showinsearch=1";
                    ncon = getConnection();
                    st_1 = ncon.createStatement();
                    r_1 = st_1.executeQuery(sql);
                    while (r_1.next()) {
                        String c_val = r_1.getString(1);
                        String c_tbl_nm = get_correct_table_name(c_val);
//                    if ((!c_tbl_nm.equals(current_tbl_nm)) && previous_tbl_nm_list != null && previous_tbl_nm_list.contains(current_tbl_nm)) {
                        if (c_tbl_nm != null && (!c_tbl_nm.equals(current_tbl_nm))) {
                            HashMap<String, String[]> constraint_map = get_key_contraints(c_tbl_nm, current_tbl_nm, Start_EgenVar1.SUPER_PARENT_REF_FLAG);
                            if (!constraint_map.isEmpty()) {
                                ArrayList<String> tmp_keys = new ArrayList<>(constraint_map.keySet());
                                for (int i = 0; i < tmp_keys.size(); i++) {
                                    if (constraint_map.get(tmp_keys.get(i)).length >= 3) {
                                        String[] tmp_array = new String[3];
                                        tmp_array[0] = c_tbl_nm;
                                        tmp_array[1] = constraint_map.get(tmp_keys.get(i))[0];
                                        tmp_array[2] = constraint_map.get(tmp_keys.get(i))[2];
                                        returning_list.add(tmp_array);
                                    }
                                }
                            }
                        }
                    }
                    ncon.close();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                } finally {
                    close(ncon, st_1, r_1);
                }

            }
            returning_list_map.put(current_tbl_nm, returning_list);
        }

        return returning_list_map.get(current_tbl_nm);
    }

    /**
     * MethodID=RS19
     *
     */
    public HashMap<String, String[]> get_key_contraints(String current_tbl_nm, String target_tbl_nm, String index_name) {
        HashMap<String, String[]> final_returning_map = new HashMap<>(1);
        HashMap<String, String[]> returning_map = new HashMap<>(1);
        HashMap<String, String[]> returning_all_map = get_key_contraints(current_tbl_nm);
        target_tbl_nm = get_correct_table_name(target_tbl_nm);

        if (target_tbl_nm != null) {
            if (returning_all_map != null && returning_all_map.containsKey(Start_EgenVar1.FOREIGN_KEY_COLUMNS_FLAG)) {
                String[] frn_keys_a = returning_all_map.get(Start_EgenVar1.FOREIGN_KEY_COLUMNS_FLAG);
                for (int i = 0; i < frn_keys_a.length; i++) {
                    if (returning_all_map.get(frn_keys_a[i]).length >= 3 && returning_all_map.get(frn_keys_a[i])[1].equals(target_tbl_nm)) {
                        returning_map.put(returning_all_map.get(frn_keys_a[i])[0], returning_all_map.get(frn_keys_a[i]));
                    }
                }
            }
        } else {
            returning_map.putAll(returning_all_map);
        }
        if (index_name != null) {
            if (returning_map.containsKey(Start_EgenVar1.FOREIGN_KEY_NAMES_FLAG)) {
                ArrayList<String> return_keys = new ArrayList<>(returning_map.keySet());
                for (int i = 0; i < return_keys.size(); i++) {
                    if (returning_map.get(return_keys.get(i)).length >= 4) {
                        String c_index_name = returning_map.get(return_keys.get(i))[3].toUpperCase();
                        if (c_index_name.startsWith(index_name.toUpperCase())) {
                            final_returning_map.put(returning_map.get(return_keys.get(i))[0], returning_map.get(return_keys.get(i)));
                        }
                    }
                }
            }

        } else {
            final_returning_map.putAll(returning_map);
        }
        return final_returning_map;
    }

    /**
     * MethodID=RS31
     *
     */
    private ArrayList<String> getTablesUsed(HashMap<String, HashMap<String, String>> data_map_in) {
        ArrayList<String> tables_used_l = new ArrayList<>(1);
        ArrayList<String> key_l = new ArrayList<>(data_map_in.keySet());
        for (int i = 0; i < key_l.size(); i++) {
            if (!tables_used_l.contains(key_l.get(i))) {
                tables_used_l.add(key_l.get(i));
            }
        }
        return tables_used_l;
    }

    /**
     * MethodID=RS33
     *
     */
    private HashMap<Integer, HashMap<String, HashMap<String, String>>> list2map(ArrayList<String> parameter_list, Statement st_1) {
        HashMap<Integer, HashMap<String, HashMap<String, String>>> data_map = new HashMap<>();
        ArrayList<String> constructed_names_l = new ArrayList<>();
        if (parameter_list != null) {
            if (tag2id == null) {
                tag2id = new HashMap<>();
            }
            boolean local_errors = false;
            for (int i = 0; (i < parameter_list.size() && !local_errors); i++) {
                boolean isatag = false;
                String created_name = null;
                String[] table_column__value_a = parameter_list.get(i).split("==");
                if (table_column__value_a.length == 2 && table_column__value_a[0] != null) {
                    String clmn_nm = table_column__value_a[0];
                    if (clmn_nm.split("\\.")[clmn_nm.split("\\.").length - 1].equalsIgnoreCase("LINK_TO_FEATURE")) {
                        isatag = true;
                    }

                    clmn_nm = get_Correct_Column_name(clmn_nm);

                    if (clmn_nm != null) {
                        String[] table_column_a = clmn_nm.split("\\.");
                        if (table_column_a.length == 3) {
                            table_column_a[1] = get_correct_table_name(table_column_a[1]);
                            if (table_column_a[1] != null) {
                                String table_nm = table_column_a[1];
                                String[] values_a = table_column__value_a[1].split(";;");
                                int j = 0;
                                for (j = 0; (j < values_a.length && !local_errors); j++) {
                                    String c_val = values_a[j];
                                    if (isatag && st_1 != null && c_val != null) {
                                        if (!c_val.contains("=")) {
                                            if (tag2id.containsKey(c_val)) {
                                                c_val = tag2id.get(c_val);
                                            } else {
                                                String[] tag_split = c_val.split("/");
                                                if (tag_split.length > 1) {
                                                    String tag_table = get_correct_table_name(tag_split[0]);
                                                    if (tag_table != null) {
                                                        HashMap<String, String> c_value_map = new HashMap<>();
                                                        c_value_map.put("name", tag_split[1]);
                                                        c_value_map.put("parent_id", "0");
                                                        int parent_id = getOrAdd(true, false, st_1, tag_table, c_value_map, null);
                                                        if (parent_id < 0) {
                                                            c_value_map.put("id", "(select max(id)+1 from " + get_correct_table_name(tag_table) + ")");
                                                            parent_id = getOrAdd(false, true, st_1, tag_table, c_value_map, null);
                                                        }
                                                        if (parent_id >= 0) {
                                                            for (int k = 2; (k < tag_split.length && parent_id >= 0); k++) {
                                                                c_value_map = new HashMap<>();
                                                                c_value_map.put("name", tag_split[k]);
                                                                c_value_map.put("parent_id", parent_id + "");
                                                                parent_id = getOrAdd(true, false, st_1, tag_table, c_value_map, null);
                                                                if (parent_id <= 0) {
                                                                    c_value_map.put("id", "(select max(id)+1 from " + get_correct_table_name(tag_table) + ")");
                                                                    parent_id = getOrAdd(false, true, st_1, tag_table, c_value_map, null);
                                                                }
                                                            }
                                                        } else {
                                                            local_errors = true;
                                                            msges = msges + "ERROR: parent not found " + tag_split[0];
                                                        }

                                                        if (parent_id > 0) {
                                                            ArrayList<String> name_l = getforQuery("select name from " + get_correct_table_name(tag_table) + " where id=" + parent_id, isatag, st_1);
                                                            if (!name_l.isEmpty()) {
                                                                created_name = name_l.get(0);
                                                            }
                                                            c_val = tag_table.split("\\.")[tag_table.split("\\.").length - 1] + "=" + parent_id;
                                                            tag2id.put(values_a[j], c_val);
                                                        } else {
                                                            msges = msges + " Error: can not tag with root element, must have atleast one parent " + c_val;
                                                            local_errors = true;
                                                        }
                                                    } else {
                                                        msges = msges + " Error: tag source not defined. Define the source " + tag_split[0] + " before using it.";
                                                        local_errors = true;
                                                    }

                                                } else {
                                                    msges = msges + " Error: incorrect tag format " + c_val;
                                                    local_errors = true;
                                                }
                                            }
                                        }


                                    }
                                    if (!local_errors) {
                                        if (data_map.containsKey(j)) {
                                            if (data_map.get(j).containsKey(table_nm)) {
                                                data_map.get(j).get(table_nm).put(table_column_a[2], c_val);
                                            } else {
                                                HashMap<String, String> tmp_map = new HashMap<>();
                                                tmp_map.put(table_column_a[2], c_val);
                                                data_map.get(j).put(table_nm, tmp_map);
                                            }
                                        } else {
                                            HashMap<String, String> tmp_map = new HashMap<>();
                                            tmp_map.put(table_column_a[2], c_val);
                                            HashMap<String, HashMap<String, String>> new_map = new HashMap<>();
                                            new_map.put(table_nm, tmp_map);
                                            data_map.put(j, new_map);
                                        }
                                    } else {
                                        msges = msges + "  Error AS55d: invalid tag format " + values_a[j];
                                    }
                                }

                            } else {
                                msges = "Error AS55a: Table not found in allowed list for this operation " + clmn_nm;
                            }

                        } else {
                            msges = "Error AS55b: format mismatch in " + parameter_list.get(i);
                        }
                    } else {
                        msges = "Error AS55c: format mismatch in " + parameter_list.get(i);
                    }


                } else {
                    msges = "Error AS55e: format mismatch in " + parameter_list.get(i);
                }
            }
        }
        return data_map;
    }

    /**
     * MethodID=RS55
     *
     */
    private String get_Correct_Column_name(String column_nm) {
        if (column_nm.contains(".")) {
            String correct_column = null;
            String[] split_a = column_nm.split("\\.");
            String table = split_a[split_a.length - 2];
            String column = split_a[split_a.length - 1];
            table = get_correct_table_name(table);
            ArrayList<String> column_l = getColumns(table);

            for (int i = 0; (i < column_l.size() && correct_column == null); i++) {
                if (column_l.get(i).equalsIgnoreCase(column)) {
                    correct_column = table + "." + column_l.get(i);
                }
            }
            return correct_column;
        } else {
            return null;
        }
    }

    /**
     * MethodID=RS22
     *
     */
    private ArrayList<String> getColumns(String current_tbl_nm) {
        ArrayList<String> columns_l = new ArrayList<>(2);
        current_tbl_nm = get_correct_table_name(current_tbl_nm);
        if (table2Columns_map == null && current_tbl_nm != null) {
            table2Columns_map = new HashMap<>();
            Connection ncon = null;
            ResultSet key_result = null;
            try {
                ncon = getConnection();
                DatabaseMetaData metaData = ncon.getMetaData();
                if (!ncon.isClosed()) {
                    ArrayList<String> c_table_l = getCurrentTables();
                    for (int i = 0; i < c_table_l.size(); i++) {
                        ArrayList<String> tmp_columns_l = new ArrayList<>(2);
                        String c_table = c_table_l.get(i);
                        String tablenm4metadata = c_table;
                        if (c_table.contains(".")) {
                            tablenm4metadata = c_table.split("\\.")[1];
                        }
                        key_result = metaData.getColumns(null, instruct_map.get(Start_EgenVar1.DATABASE_NAME_DATA_FLAG), tablenm4metadata, null);
                        if (!key_result.next()) {
                            key_result = metaData.getColumns(null, instruct_map.get(Start_EgenVar1.DATABASE_NAME_DATA_FLAG), tablenm4metadata.toUpperCase(), null);
                        } else {
                            String clmname = key_result.getString("COLUMN_NAME").toUpperCase();
                            if (!tmp_columns_l.contains(clmname)) {
                                tmp_columns_l.add(clmname);
                            }
                        }
                        while (key_result.next()) {
                            String clmname = key_result.getString("COLUMN_NAME").toUpperCase();
                            if (!tmp_columns_l.contains(clmname)) {
                                tmp_columns_l.add(clmname);
                            }
                        }
                        table2Columns_map.put(c_table.toUpperCase(), tmp_columns_l);
                        key_result.close();
                    }

                }
                ncon.close();
            } catch (SQLException e) {
                System.out.println("Error AS22a" + e.getMessage());
            } finally {
                close(ncon, null, key_result);
            }
        }

        if (current_tbl_nm != null) {
            if (table2Columns_map.containsKey(current_tbl_nm)) {
                columns_l = table2Columns_map.get(current_tbl_nm);
            } else if (table2Columns_map.containsKey(current_tbl_nm.toUpperCase())) {
                columns_l = table2Columns_map.get(current_tbl_nm.toUpperCase());
            }
        }

        return columns_l;
    }

    /**
     * MethodID=RS44
     *
     */
    public ArrayList<String> getforQuery(String query, boolean singlecolumn, Statement st_1) {
        ArrayList<String> result_l = new ArrayList<>(1);
        Connection ncon = null;
        ResultSet r_1 = null;
        try {
            if (st_1 == null) {
                ncon = getConnection();
                st_1 = ncon.createStatement();
            }
            r_1 = st_1.executeQuery(query);
            ResultSetMetaData rsmd = r_1.getMetaData();
            String table_nm = rsmd.getTableName(1);
            int NumOfCol = rsmd.getColumnCount();
            while (r_1.next()) {
                if (singlecolumn) {
                    result_l.add(r_1.getString(1));
                } else {
                    String result_tmp = "";
                    for (int i = 0; i < NumOfCol; i++) {
                        result_tmp = result_tmp + table_nm + "." + rsmd.getColumnLabel(i + 1) + "=" + r_1.getString(i + 1) + "||";
                    }
                    result_l.add(result_tmp);
                }
            }
            r_1.close();
            ncon.close();
        } catch (Exception ex) {
            msges = "Error RS44a: creating connections " + ex.getMessage();
            System.out.println("Error AS44a: creating connections " + ex.getMessage() + "\n" + query);
        } finally {
            close(ncon, null, r_1);
        }
        return result_l;
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
}
