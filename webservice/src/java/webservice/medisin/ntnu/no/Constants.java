/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package webservice.medisin.ntnu.no;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Formatter;
import java.util.HashMap;
import javax.sql.DataSource;

/**
 *
 * @author sabryr
 */
public class Constants {

    public final static String UNIQUE_TO_USER_COLUMNS_FLAG = "UNIQUE_COLMNS";
    public final static String ALL_KEY_COLUMNS_FLAG = "ALL_KEY_COLMNS";
    public final static String PRIMARY_KEY_COLUMNS_FLAG = "PRIMARY_KEY";
    public final static String FOREIGN_TABLE_FLAG = "FOREIGN_TABLES";
    public final static String FOREIGN_KEY_COLUMNS_FLAG = "FOREIGN_COLUMNS";
    public final static String FOREIGN_KEY_NAMES_FLAG = "FOREIGN_KES";
    public final static String DATABASE_NAME_DATA = "EGEN_DATAENTRY";
    public final static String UNIQUE_ID_INDEX_NAME_PREFIX = "UNIQUE_IDENTIFICATION";// 
    public final static String PARENT_CHILD_KEY_COLUMNS = "PARENT_CHILD_KEY_COLUMNS";
    private static HashMap<String, String> chartosymb_map;
    public static HashMap<String, HashMap<String, String[]>> key_constraint_map;
    public static HashMap<String, String> corect_tbl_nm_map;
    public static ArrayList<String> table_l;
    public static String server_name;
    public static String server_root;
    public final static String PARENT_REF_FLAG = "parent_id_ref";
    public  static String admin_email  = null;

    public static String get_Parent_ref_column(String current_tbl_nm, DataSource dataSource, String databasename) {
        String parent_rf_col = null;
        HashMap<String, String[]> constraint_map = get_key_contraints(current_tbl_nm, dataSource, databasename, null, "PARENT");
        if (constraint_map != null) {
            ArrayList<String> k_l = new ArrayList<>(constraint_map.keySet());
            if (!k_l.isEmpty()) {
                parent_rf_col = k_l.get(0);//using only the first one
            }
        }
        return parent_rf_col;
    }

    public static String get_Child_ref_column(String current_tbl_nm, DataSource dataSource, String databasename) {
        String parent_rf_col = null;
        HashMap<String, String[]> constraint_map = get_key_contraints(current_tbl_nm, dataSource, databasename, null, "CHILD");
        if (constraint_map != null) {
            ArrayList<String> k_l = new ArrayList<>(constraint_map.keySet());
            if (!k_l.isEmpty()) {
                parent_rf_col = k_l.get(0);//using only the first one
            }
        }
        return parent_rf_col;
    }

    public static HashMap<String, String[]> get_key_contraints(String current_tbl_nm, DataSource dataSource, String databasename, String target_tbl_nm, String index_name) {
        HashMap<String, String[]> final_returning_map = new HashMap<>(1);
        HashMap<String, String[]> returning_map = new HashMap<>(1);
        HashMap<String, String[]> returning_all_map = get_key_contraints(current_tbl_nm, dataSource, databasename);
        if (target_tbl_nm != null) {
            if (returning_all_map != null && returning_all_map.containsKey(Constants.FOREIGN_KEY_COLUMNS_FLAG)) {
                ArrayList<String> return_keys = new ArrayList<>(returning_all_map.keySet());
                for (int i = 0; i < return_keys.size(); i++) {
                    if (returning_all_map.get(return_keys.get(i)).length >= 3 && returning_all_map.get(return_keys.get(i))[1].equals(target_tbl_nm)) {
                        returning_map.put(returning_all_map.get(return_keys.get(i))[0], returning_all_map.get(return_keys.get(i)));
                    }
                }
            }
        } else {
            returning_map.putAll(returning_all_map);
        }

        if (index_name != null) {
            if (returning_all_map != null && returning_all_map.containsKey(Constants.FOREIGN_KEY_NAMES_FLAG)) {
                ArrayList<String> return_keys = new ArrayList<>(returning_map.keySet());
                for (int i = 0; i < return_keys.size(); i++) {
                    if (returning_map.get(return_keys.get(i)).length >= 4) {
                        String c_index_name = returning_map.get(return_keys.get(i))[3].toUpperCase();
                        if (c_index_name.startsWith(index_name.toUpperCase())) {
                            final_returning_map.put(returning_all_map.get(return_keys.get(i))[0], returning_all_map.get(return_keys.get(i)));
                        }
                    }
                }
            }
        } else {
            final_returning_map.putAll(returning_map);
        }
        return final_returning_map;
    }

    public static HashMap<String, String[]> get_key_contraints(String current_tbl_nm, DataSource dataSource, String databasename) {
        if (key_constraint_map == null) {
            key_constraint_map = new HashMap<>();
        }
        HashMap<String, String[]> returning_map = new HashMap<>(1);
        String primary_key_column = null;
        current_tbl_nm = get_correct_table_name(current_tbl_nm, dataSource, databasename, getTables(dataSource, databasename));
        if (current_tbl_nm != null && !current_tbl_nm.isEmpty() && !current_tbl_nm.equalsIgnoreCase("null") && !key_constraint_map.containsKey(current_tbl_nm)) {
            try {
                if (dataSource != null) {
                    String[] unique_a = get_key_contraints_unique_identification(current_tbl_nm, dataSource);
                    Connection ncon = null;
                    try {
                        ncon = dataSource.getConnection();
                        if (!ncon.isClosed()) {
                            ArrayList<String> foreign_columns = new ArrayList<>(1);
                            ArrayList<String> foreign_key_names_columns = new ArrayList<>(1);
                            ArrayList<String> foreign_tables = new ArrayList<>(1);
                            ArrayList<String> all_list = new ArrayList<>(2);
                            DatabaseMetaData metaData = ncon.getMetaData();
                            String tablenm4metadata = current_tbl_nm;
                            tablenm4metadata = current_tbl_nm.split("\\.")[current_tbl_nm.split("\\.").length - 1];
                            ResultSet key_result = metaData.getImportedKeys(ncon.getCatalog(), DATABASE_NAME_DATA, tablenm4metadata);
                            if (!key_result.next()) {
                                key_result = metaData.getImportedKeys(ncon.getCatalog(), DATABASE_NAME_DATA.toUpperCase(), tablenm4metadata.toUpperCase());
                            } else {
                                String[] tmp_a = new String[4];
                                tmp_a[0] = key_result.getString("FKCOLUMN_NAME");//column_name
                                tmp_a[1] = get_correct_table_name(key_result.getString("PKTABLE_NAME"), dataSource, databasename, getTables(dataSource, databasename));//ref_tblm
                                tmp_a[2] = key_result.getString("PKCOLUMN_NAME");//ref_tbl_clm_nm 
                                tmp_a[3] = key_result.getString("FK_NAME");//Key name
                                primary_key_column = key_result.getString("PKCOLUMN_NAME");//primary key
                                foreign_columns.add(tmp_a[0]);
                                returning_map.put(tmp_a[0], tmp_a);
                                if (!foreign_tables.contains(tmp_a[1])) {
                                    foreign_tables.add(tmp_a[1]);
                                }
                                foreign_key_names_columns.add(tmp_a[3]);
                            }

                            while (key_result.next()) {
                                String[] tmp_a = new String[4];
                                tmp_a[0] = key_result.getString("FKCOLUMN_NAME");//column_name
                                tmp_a[1] = get_correct_table_name(key_result.getString("PKTABLE_NAME"), dataSource, databasename, getTables(dataSource, databasename));//ref_tblm
                                tmp_a[2] = key_result.getString("PKCOLUMN_NAME");//ref_tbl_clm_nm 
                                tmp_a[3] = key_result.getString("FK_NAME");//Key name
                                primary_key_column = key_result.getString("PKCOLUMN_NAME");//primary key
                                foreign_columns.add(tmp_a[0]);
                                returning_map.put(tmp_a[0], tmp_a);
                                if (!foreign_tables.contains(tmp_a[1])) {
                                    foreign_tables.add(tmp_a[1]);
                                }
                                foreign_key_names_columns.add(tmp_a[3]);

                            }
                            if (!foreign_columns.isEmpty()) {
                                String[] tmp_a = new String[foreign_columns.size()];
                                for (int i = 0; i < foreign_columns.size(); i++) {
                                    tmp_a[i] = foreign_columns.get(i);
                                }
                                returning_map.put(FOREIGN_KEY_COLUMNS_FLAG, tmp_a);
                            }

                            if (!foreign_tables.isEmpty()) {
                                String[] tmp_a = new String[foreign_tables.size()];
                                for (int i = 0; i < tmp_a.length; i++) {
                                    tmp_a[i] = foreign_tables.get(i);
                                }
                                returning_map.put(FOREIGN_TABLE_FLAG, tmp_a);
                            }
                            if (!foreign_key_names_columns.isEmpty()) {
                                String[] tmp_a = new String[foreign_key_names_columns.size()];
                                for (int i = 0; i < tmp_a.length; i++) {
                                    tmp_a[i] = foreign_key_names_columns.get(i);
                                }
                                returning_map.put(FOREIGN_KEY_NAMES_FLAG, tmp_a);
                            }
                            if (unique_a != null) {
                                returning_map.put(UNIQUE_TO_USER_COLUMNS_FLAG, unique_a);
                            }
                            if (primary_key_column != null) {
                                returning_map.put(PRIMARY_KEY_COLUMNS_FLAG, new String[]{primary_key_column});
                            }
                        }
                        ncon.close();
                    } catch (SQLException e) {
                        System.out.println("Error AS17a" + e.getMessage());
                    } finally {
                        close(ncon, null, null);
                    }
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            key_constraint_map.put(current_tbl_nm, returning_map);
            return returning_map;
        } else if (key_constraint_map.containsKey(current_tbl_nm)) {
            return key_constraint_map.get(current_tbl_nm);
        }
        return returning_map;
    }

    private static String[] get_key_contraints_unique_identification(String current_tbl_nm, DataSource dataSource) {
        String[] uniq_to_user_a = new String[1];
        if (current_tbl_nm != null && !current_tbl_nm.isEmpty() && !current_tbl_nm.equalsIgnoreCase("null") && !Constants.key_constraint_map.containsKey(current_tbl_nm)) {
            try {
                Connection ncon = null;
                try {
                    ncon = dataSource.getConnection();
                    if (!ncon.isClosed()) {
                        DatabaseMetaData metaData = ncon.getMetaData();
                        String tablenm4metadata = current_tbl_nm;
                        if (current_tbl_nm.contains(".")) {
                            tablenm4metadata = current_tbl_nm.split("\\.")[1];
                        }
                        ResultSet key_result = metaData.getIndexInfo(null, DATABASE_NAME_DATA, tablenm4metadata, false, false);//.getImportedKeys(ncon.getCatalog(), Constants.DATABASE_NAME_DATA, current_tbl_nm);
                        ArrayList<String> uniqs_list = new ArrayList<String>(1);
                        if (!key_result.next()) {
                            key_result = metaData.getIndexInfo(null, DATABASE_NAME_DATA.toUpperCase(), tablenm4metadata.toUpperCase(), false, false);//.getImportedKeys(ncon.getCatalog(), Constants.DATABASE_NAME_DATA, current_tbl_nm);
                        } else {
                            String index_name = key_result.getString("INDEX_NAME");
                            index_name = index_name.toUpperCase();
                            if (index_name.startsWith(UNIQUE_ID_INDEX_NAME_PREFIX)) {
                                uniqs_list.add(key_result.getString("COLUMN_NAME"));
                            }
                        }
                        while (key_result.next()) {
                            String index_name = key_result.getString("INDEX_NAME");
                            if (index_name != null) {
                                index_name = index_name.toUpperCase();
                                if (index_name.startsWith(UNIQUE_ID_INDEX_NAME_PREFIX)) {
                                    uniqs_list.add(key_result.getString("COLUMN_NAME"));
                                }
                            }
                        }
                        if (!uniqs_list.isEmpty()) {
                            uniq_to_user_a = new String[uniqs_list.size()];
                            for (int i = 0; i < uniq_to_user_a.length; i++) {
                                uniq_to_user_a[i] = uniqs_list.get(i);
                            }
                        }
                        key_result.close();
                    }
                    ncon.close();
                } catch (SQLException e) {
                    System.out.println("Error 8767" + e.getMessage());
                } finally {
                    close(ncon, null, null);
                }

            } catch (Exception ex) {
                ex.printStackTrace();
            }

        }
        return uniq_to_user_a;
    }

    public static String get_correct_table_name(String in_name, DataSource dataSource_data, String databasename, ArrayList<String> getTables) {
        if (in_name != null) {
            if (corect_tbl_nm_map == null || !corect_tbl_nm_map.containsKey(in_name)) {
                corect_tbl_nm_map = new HashMap<>();
//                ArrayList<String> getTables = getTables(dataSource_data, databasename);
                if (getTables.contains(in_name)) {
                    corect_tbl_nm_map.put(in_name, in_name);
                } else {
                    String tr_in_name = in_name.split("\\.")[in_name.split("\\.").length - 1];
                    for (int i = 0; i < getTables.size(); i++) {
                        String c_tbl = getTables.get(i).toUpperCase();
                        if (c_tbl.equalsIgnoreCase(tr_in_name) || (c_tbl.split("\\.")[c_tbl.split("\\.").length - 1]).equalsIgnoreCase(tr_in_name)) {
                            corect_tbl_nm_map.put(in_name, getTables.get(i));
                        }
                    }
                }
            }
            return corect_tbl_nm_map.get(in_name);
        }
        return null;

    }

    public static ArrayList<String> getUniqueList(String table, DataSource dataSource_data, String databasename) {
        table = get_correct_table_name(table, dataSource_data, databasename, getTables(dataSource_data, databasename));
        ArrayList<String> uniquet_col_l_l = new ArrayList<>(1);
        if (Authenticate_service.table2uniqs_l_map == null || !Authenticate_service.table2uniqs_l_map.containsKey(table)) {
            Authenticate_service.table2uniqs_l_map = new HashMap<>();

            HashMap<String, String[]> const_map = get_key_contraints(table, dataSource_data, databasename);
            String[] uniq_a = const_map.get(Constants.UNIQUE_TO_USER_COLUMNS_FLAG);
            if (uniq_a != null) {
                uniquet_col_l_l.addAll(Arrays.asList(uniq_a));
            }
            Authenticate_service.table2uniqs_l_map.put(table, uniquet_col_l_l);
        }
        return Authenticate_service.table2uniqs_l_map.get(table);
    }

    public static String getWebServerName(DataSource dataSource_data, String databasename) {
        if (server_name == null) {
            try {
                try {
                    Connection ncon = dataSource_data.getConnection();
                    if (!ncon.isClosed()) {
                        Statement st_1 = ncon.createStatement();
                        String config_tbl = get_correct_table_name("config", dataSource_data, databasename, getTables(dataSource_data, databasename));
                        if (config_tbl != null) {
                            ResultSet r_1 = st_1.executeQuery("select param_value from " + config_tbl + " where name='S_SERVER_NAME'");
                            while (r_1.next()) {
                                server_name = r_1.getString("param_value");
                            }
                        }
                    }
                    if (!ncon.isClosed()) {
                        ncon.close();
                    }
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }


            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        return server_name;
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

    public static String encript(String intext) {
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

    private static void writeResultsToFile(Object indata, String file_nm) {
        if (indata != null && file_nm != null) {
            try {
                FileOutputStream fos = new FileOutputStream(file_nm);
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

    public static ArrayList<String> getTables(DataSource dataSource_data, String databasename) {
        if (table_l == null) {
            Connection ncon = null;
            Statement st_1 = null;
            ResultSet r_1 = null;
            try {
                table_l = new ArrayList<>(5);
                String database_nm = databasename;
                ncon = dataSource_data.getConnection();
                if (!ncon.isClosed()) {
                    DatabaseMetaData metaData = ncon.getMetaData();
                    r_1 = metaData.getTables(null, null, null, new String[]{"TABLE"});
                    while (r_1.next()) {
                        String table_nm = r_1.getString("TABLE_NAME");
                        String schema_nm = r_1.getString("TABLE_SCHEM");
                        if (schema_nm.equals(database_nm)) {
                            table_l.add(schema_nm + "." + table_nm);
                        }
                    }
                }
                close(ncon, st_1, r_1);
            } catch (SQLException ex) {
                System.out.println("Error AS23a" + ex.getMessage());
            } finally {
                close(ncon, st_1, r_1);
            }
        }
        return table_l;
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
//    public static HashMap<String, String> getchar2symbolMap() {
//        if (chartosymb_map == null || chartosymb_map.isEmpty()) {
//            chartosymb_map = new HashMap<>();
//            chartosymb_map.put("\"", "&#34;");
//            chartosymb_map.put("'", "&#39;");
//            chartosymb_map.put("&", "&#38;");
//            chartosymb_map.put("<", "&#60;");
//            chartosymb_map.put(">", "&#62;");
//            chartosymb_map.put("¡", "&#161;");
//            chartosymb_map.put("¢", "&#162;");
//            chartosymb_map.put("£", "&#163;");
//            chartosymb_map.put("¤", "&#164;");
//            chartosymb_map.put("¥", "&#165;");
//            chartosymb_map.put("¦", "&#166;");
//            chartosymb_map.put("§", "&#167;");
//            chartosymb_map.put("¨", "&#168;");
//            chartosymb_map.put("©", "&#169;");
//            chartosymb_map.put("ª", "&#170;");
//            chartosymb_map.put("«", "&#171;");
//            chartosymb_map.put("¬", "&#172;");
//            chartosymb_map.put("­­", "&#173;");
//            chartosymb_map.put("®", "&#174;");
//            chartosymb_map.put("¯", "&#175;");
//            chartosymb_map.put("°", "&#176;");
//            chartosymb_map.put("±", "&#177;");
//            chartosymb_map.put("²", "&#178;");
//            chartosymb_map.put("³", "&#179;");
//            chartosymb_map.put("´", "&#180;");
//            chartosymb_map.put("µ", "&#181;");
//            chartosymb_map.put("¶", "&#182;");
//            chartosymb_map.put("·", "&#183;");
//            chartosymb_map.put("¸", "&#184;");
//            chartosymb_map.put("¹", "&#185;");
//            chartosymb_map.put("º", "&#186;");
//            chartosymb_map.put("»", "&#187;");
//            chartosymb_map.put("¼", "&#188;");
//            chartosymb_map.put("½", "&#189;");
//            chartosymb_map.put("¾", "&#190;");
//            chartosymb_map.put("¿", "&#191;");
//            chartosymb_map.put("×", "&#215;");
//            chartosymb_map.put("÷", "&#247;");
//            chartosymb_map.put("À", "&#192;");
//            chartosymb_map.put("Á", "&#193;");
//            chartosymb_map.put("Â", "&#194;");
//            chartosymb_map.put("Ã", "&#195;");
//            chartosymb_map.put("Ä", "&#196;");
//            chartosymb_map.put("Å", "&#197;");
//            chartosymb_map.put("Æ", "&#198;");
//            chartosymb_map.put("Ç", "&#199;");
//            chartosymb_map.put("È", "&#200;");
//            chartosymb_map.put("É", "&#201;");
//            chartosymb_map.put("Ê", "&#202;");
//            chartosymb_map.put("Ë", "&#203;");
//            chartosymb_map.put("Ì", "&#204;");
//            chartosymb_map.put("Í", "&#205;");
//            chartosymb_map.put("Î", "&#206;");
//            chartosymb_map.put("Ï", "&#207;");
//            chartosymb_map.put("Ð", "&#208;");
//            chartosymb_map.put("Ñ", "&#209;");
//            chartosymb_map.put("Ò", "&#210;");
//            chartosymb_map.put("Ó", "&#211;");
//            chartosymb_map.put("Ô", "&#212;");
//            chartosymb_map.put("Õ", "&#213;");
//            chartosymb_map.put("Ö", "&#214;");
//            chartosymb_map.put("Ø", "&#216;");
//            chartosymb_map.put("Ù", "&#217;");
//            chartosymb_map.put("Ú", "&#218;");
//            chartosymb_map.put("Û", "&#219;");
//            chartosymb_map.put("Ü", "&#220;");
//            chartosymb_map.put("Ý", "&#221;");
//            chartosymb_map.put("Þ", "&#222;");
//            chartosymb_map.put("ß", "&#223;");
//            chartosymb_map.put("à", "&#224;");
//            chartosymb_map.put("á", "&#225;");
//            chartosymb_map.put("â", "&#226;");
//            chartosymb_map.put("ã", "&#227;");
//            chartosymb_map.put("ä", "&#228;");
//            chartosymb_map.put("å", "&#229;");
//            chartosymb_map.put("æ", "&#230;");
//            chartosymb_map.put("ç", "&#231;");
//            chartosymb_map.put("è", "&#232;");
//            chartosymb_map.put("é", "&#233;");
//            chartosymb_map.put("ê", "&#234;");
//            chartosymb_map.put("ë", "&#235;");
//            chartosymb_map.put("ì", "&#236;");
//            chartosymb_map.put("í", "&#237;");
//            chartosymb_map.put("î", "&#238;");
//            chartosymb_map.put("ï", "&#239;");
//            chartosymb_map.put("ð", "&#240;");
//            chartosymb_map.put("ñ", "&#241;");
//            chartosymb_map.put("ò", "&#242;");
//            chartosymb_map.put("ó", "&#243;");
//            chartosymb_map.put("ô", "&#244;");
//            chartosymb_map.put("õ", "&#245;");
//            chartosymb_map.put("ö", "&#246;");
//            chartosymb_map.put("ø", "&#248;");
//            chartosymb_map.put("ù", "&#249;");
//            chartosymb_map.put("ú", "&#250;");
//            chartosymb_map.put("û", "&#251;");
//            chartosymb_map.put("ü", "&#252;");
//            chartosymb_map.put("ý", "&#253;");
//            chartosymb_map.put("þ", "&#254;");
//            chartosymb_map.put("ÿ", "&#255;");
//        }
//
//        return chartosymb_map;
//    }
}
