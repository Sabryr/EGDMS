/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package no.ntnu.medisin.egenvar.server;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import javax.sql.DataSource;

/**
 *
 * @author sabryr
 */
public class Tests {

    private static Connection ncon = null;

    public Tests() {
    }

    /*
     MethodID=T2
     */
    public static HashMap<String, String[]> get_key_contraints(String dbname, String current_tbl_nm, String dbURL) {
        System.out.println("28 current_tbl_nm=" + current_tbl_nm + "  dbname=" + dbname);
        HashMap<String, HashMap<String, String[]>> key_constraint_map = new HashMap<String, HashMap<String, String[]>>();
        HashMap<String, String[]> returning_map = new HashMap<String, String[]>(1);
        key_constraint_map.clear();
        if (current_tbl_nm != null && !current_tbl_nm.isEmpty() && !current_tbl_nm.equalsIgnoreCase("null")) {
            try {
                createConnection(dbURL);
                if (ncon != null) {
                    String[] unique_a = get_key_contraints_unique_identification(dbname, current_tbl_nm, dbURL);
                    try {
                        System.out.println("37");
                        createConnection(dbURL);
                        if (!ncon.isClosed()) {
                            ArrayList<String> foreign_columns = new ArrayList<String>(1);
                            ArrayList<String> foreign_key_names_columns = new ArrayList<String>(1);
                            ArrayList<String> foreign_tables = new ArrayList<String>(1);
                            DatabaseMetaData metaData = ncon.getMetaData();
                            String tablenm4metadata = current_tbl_nm;
                            if (current_tbl_nm.contains(".")) {
                                tablenm4metadata = current_tbl_nm.split("\\.")[1];
                            }
                            System.out.println("74 tablenm4metadata=" + tablenm4metadata);
                            System.out.println("48 " + ncon.getCatalog());
                            ResultSet key_result = metaData.getImportedKeys(null, null, tablenm4metadata);
                            if (!key_result.next()) {
                                System.out.println("50");
                                key_result = metaData.getImportedKeys(null, null, tablenm4metadata.toUpperCase());
                            } else {
                                System.out.println("52");
                                String[] tmp_a = new String[4];
                                tmp_a[0] = key_result.getString("FKCOLUMN_NAME");//column_name                                 
                                tmp_a[1] = dbname + "." + key_result.getString("PKTABLE_NAME");//ref_tblm
                                tmp_a[2] = key_result.getString("PKCOLUMN_NAME");//ref_tbl_clm_nm 
                                tmp_a[3] = key_result.getString("FK_NAME");//Key name
                                foreign_columns.add(tmp_a[0]);
                                returning_map.put(tmp_a[0], tmp_a);
                                foreign_tables.add(tmp_a[1]);
                                foreign_key_names_columns.add(tmp_a[3]);
                                System.out.println("61 " + tmp_a[0]);
                            }
                            while (key_result.next()) {
                                String[] tmp_a = new String[4];
                                tmp_a[0] = key_result.getString("FKCOLUMN_NAME");//column_name
                                tmp_a[1] = dbname + "." + key_result.getString("PKTABLE_NAME");//ref_tblm
                                tmp_a[2] = key_result.getString("PKCOLUMN_NAME");//ref_tbl_clm_nm 
                                tmp_a[3] = key_result.getString("FK_NAME");//Key name
                                foreign_columns.add(tmp_a[0]);
                                returning_map.put(tmp_a[0], tmp_a);
                                foreign_tables.add(tmp_a[1]);
                                foreign_key_names_columns.add(tmp_a[3]);
                                System.out.println("73 " + tmp_a[0]);

                            }
                            System.out.println("76 foreign_columns=" + foreign_columns);
                            if (!foreign_columns.isEmpty()) {
                                String[] tmp_a = new String[foreign_columns.size()];
                                for (int i = 0; i < foreign_columns.size(); i++) {
                                    tmp_a[i] = foreign_columns.get(i);
                                }
                                returning_map.put("FOREIGN_COLUMNS", tmp_a);
                            }

                            System.out.println("85 foreign_tables=" + foreign_tables);
                            if (!foreign_tables.isEmpty()) {
                                String[] tmp_a = new String[foreign_tables.size()];
                                for (int i = 0; i < tmp_a.length; i++) {
                                    tmp_a[i] = foreign_tables.get(i);
                                }
                                returning_map.put("FOREIGN_TABLES", tmp_a);
                            }
                            System.out.println("93 foreign_key_names_columns=" + foreign_key_names_columns);
                            if (!foreign_key_names_columns.isEmpty()) {
                                String[] tmp_a = new String[foreign_key_names_columns.size()];
                                for (int i = 0; i < tmp_a.length; i++) {
                                    tmp_a[i] = foreign_key_names_columns.get(i);
                                }
                                returning_map.put("FOREIGN_KES", tmp_a);
                            }
                            if (unique_a != null) {
                                returning_map.put("UNIQUE_COLMNS", unique_a);
                            }
                        } else {
                            System.out.println("Error T2b: Connection closed");
                        }
                        ncon.close();
                    } catch (SQLException e) {
                        System.out.println("Error 8767" + e.getMessage());
                    }
                } else {
                    System.out.println("Error T2a: Connection was null");
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            key_constraint_map.put(current_tbl_nm, returning_map);
            return returning_map;
        }
        System.out.println("115 " + returning_map.keySet());
        return returning_map;
    }

    /*
     MethodID=T3
     */
    private static String[] get_key_contraints_unique_identification(String dbname, String current_tbl_nm, String dbURL) {
        String[] uniq_to_user_a = null;
        if (current_tbl_nm != null && !current_tbl_nm.isEmpty() && !current_tbl_nm.equalsIgnoreCase("null")) {
            try {
                createConnection(dbURL);
                if (ncon != null) {
                    try {
                        if (!ncon.isClosed()) {
                            DatabaseMetaData metaData = ncon.getMetaData();
                            String tablenm4metadata = current_tbl_nm;
                            if (current_tbl_nm.contains(".")) {
                                tablenm4metadata = current_tbl_nm.split("\\.")[1];
                            }
                            System.out.println("132 tablenm4metadata=" + tablenm4metadata);
                            ResultSet key_result = metaData.getIndexInfo(null, dbname, tablenm4metadata, false, false);//.getImportedKeys(ncon.getCatalog(), Constants.DATABASE_NAME_DATA, current_tbl_nm);
                            ArrayList<String> uniqs_list = new ArrayList<String>(1);
                            if (!key_result.next()) {
                                key_result = metaData.getIndexInfo(null, dbname, tablenm4metadata.toUpperCase(), false, false);//.getImportedKeys(ncon.getCatalog(), Constants.DATABASE_NAME_DATA, current_tbl_nm);
                            } else {
                                String index_name = key_result.getString("INDEX_NAME");
                                index_name = index_name.toUpperCase();
                                if (index_name.startsWith("UNIQUE_IDENTIFICATION")) {
                                    uniqs_list.add(key_result.getString("COLUMN_NAME"));
                                }
                                System.out.println("144 " + index_name);
                            }
                            while (key_result.next()) {
                                String index_name = key_result.getString("INDEX_NAME");
                                if (index_name != null) {
                                    index_name = index_name.toUpperCase();
                                    if (index_name.startsWith("UNIQUE_IDENTIFICATION")) {
                                        uniqs_list.add(key_result.getString("COLUMN_NAME"));
                                    }
                                }
                                System.out.println("154 " + index_name);
                            }
                            System.out.println("156 " + uniqs_list);
                            if (!uniqs_list.isEmpty()) {
                                uniq_to_user_a = new String[uniqs_list.size()];
                                for (int i = 0; i < uniq_to_user_a.length; i++) {
                                    uniq_to_user_a[i] = uniqs_list.get(i);
                                }
                            }
                            key_result.close();
                            ncon.close();
                        }

                    } catch (SQLException e) {
                        System.out.println("Error T3a" + e.getMessage());
                    }
                } else {
                    System.out.println("Error T3b: connection was null");
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return uniq_to_user_a;
    }

    private static void createConnection(String dbURL) {
        try {
            if (ncon == null || ncon.isClosed()) {
                Class.forName("org.apache.derby.jdbc.ClientDriver").newInstance();
                ncon = DriverManager.getConnection(dbURL);
            }
        } catch (Exception except) {
            except.printStackTrace();
        }
    }
}
