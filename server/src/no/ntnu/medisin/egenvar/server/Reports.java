/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package no.ntnu.medisin.egenvar.server;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Formatter;
import java.util.HashMap;

/**
 *
 * @author sabryr
 */
public class Reports implements Runnable {

    private Connection conn = null;
    private String dbURL_dataEntry;
//    private String databse_NAME_DATA = null;
    private String docroot;
    private String service_root;
    private String files_tbl = "EGEN_DATAENTRY.FILES";
    private String files2path_tbl = "EGEN_DATAENTRY.FILES2PATH";

    public Reports(String files_tbl, String files2path_tbl, String dbURL_dataEntry, String docroot, String service_root) {
        this.dbURL_dataEntry = dbURL_dataEntry;
        this.docroot = docroot;
        this.service_root = service_root;
        this.files2path_tbl = files2path_tbl;
        this.files_tbl = files_tbl;
    }

    @Override
    public void run() {
        getDirDuplocates();
    }

    /*
     MethodID=R2
     */
    private void getDirDuplocates() {
        FileWriter dup_file = null;
        String dupl_file_name = "duplicate_folders_report.txt";
        Path file_loc = Paths.get(docroot);
        try {
            if (Files.exists(file_loc) && Files.isWritable(file_loc)) {
                file_loc = Paths.get(file_loc.getParent().toString(), "applications", "eGenVar_web", "Search", dupl_file_name);
                dup_file = new FileWriter(file_loc.toFile(), false);
                dup_file.append("#Still processing. \n#Last updated  " + Timing.getDateTime() + "\n");
                dup_file.close();
            }

        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            try {
                if (dup_file != null) {
                    dup_file.close();
                }
            } catch (IOException ex) {
            }
        }

//        String sql = "SELECT " + files2path_tbl + ".FILEPATH," + files_tbl + ".CHEKSUM FROM "
//                + "" + files_tbl + "," + files2path_tbl + " WHERE " + files_tbl + ".ID=" + files2path_tbl + ".FILES_ID GROUP BY "+ files_tbl + ".CHEKSUM ";
//            String sql = "SELECT " + files2path_tbl + ".FILEPATH," + files_tbl + ".CHEKSUM FROM " + files_tbl + " INNER JOIN "+ files2path_tbl + " ON " + files_tbl + ".ID="+ files2path_tbl + ".FILES_ID";
        String sql = "SELECT FILEPATH,CHECKSUM FROM " + files_tbl + " INNER JOIN " + files2path_tbl + " ON " + files_tbl + ".ID=" + files2path_tbl + ".FILES_ID";
        System.out.println("Started creating report on duplicts @" + Timing.getDateTime());
        HashMap<Path, ArrayList<String>> details_map = new HashMap<>();
        HashMap<String, String> path_to_full_hash_map = new HashMap<>();
        if (createConnection()) {
            try {
                Statement stm = conn.createStatement();
                ResultSet r_1 = stm.executeQuery(sql);
                while (r_1.next()) {
                    if (r_1.getString(1) != null && r_1.getString(2) != null) {
                        Path c_parent = Paths.get(r_1.getString(1)).getParent();
                        if (!details_map.containsKey(c_parent)) {
                            details_map.put(c_parent, new ArrayList<String>());
                        }
                        details_map.get(c_parent).add(r_1.getString(2));
                    }
                }
            } catch (SQLException ex) {
                System.out.println("Error R2b: " + ex.getMessage());
            }
        }
        ArrayList<Path> path_l = new ArrayList<>(details_map.keySet());
        System.out.println("Total locations to be checked " + path_l.size());
        for (int i = 0; i < path_l.size(); i++) {
            Path c_path = path_l.get(i);
            if (c_path != null) {
                path_to_full_hash_map.put(c_path.toString(), getHash(details_map.get(c_path)));
            } else {
                System.out.println("Warning Files path missing :" + c_path + "  " + details_map.get(c_path));
            }
        }
        path_l.clear();
        details_map.clear();
        ArrayList<String> hash_L = new ArrayList<>(path_to_full_hash_map.values());
        ArrayList<String> path_L = new ArrayList<>(path_to_full_hash_map.keySet());
        HashMap<String, ArrayList<String>> hash2paths_map = new HashMap<>();
        while (!hash_L.isEmpty()) {
            String c_hash = hash_L.remove(0);
            if (!hash2paths_map.containsKey(c_hash)) {
                hash2paths_map.put(c_hash, new ArrayList<String>());
            }
            for (int j = 0; j < path_L.size(); j++) {
                if (path_to_full_hash_map.get(path_L.get(j)).equals(c_hash)) {
                    hash2paths_map.get(c_hash).add(path_L.remove(j));
                    j--;
                }
            }
        }

        try {
            if (Files.exists(file_loc) && Files.isWritable(file_loc)) {
                dup_file = new FileWriter(file_loc.toFile(), false);
                dup_file.append("#Results of Folder level duplcation check.\n#Last updated  " + Timing.getDateTime() + "\n");
                ArrayList<String> final_hash_L = new ArrayList<>(hash2paths_map.keySet());
                for (int i = 0; i < final_hash_L.size(); i++) {
                    if (hash2paths_map.get(final_hash_L.get(i)).size() > 2) {
                        dup_file.append("#" + final_hash_L.get(i) + "________________________________\n");
                        ArrayList<String> tmp_l = hash2paths_map.get(final_hash_L.get(i));
                        for (int j = 0; j < tmp_l.size(); j++) {
                            dup_file.append(tmp_l.get(j) + "\n");
                        }
                    }
                }
                dup_file.close();
            }

        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            try {
                if (dup_file != null) {
                    dup_file.close();
                }
            } catch (IOException ex) {
            }
        }
        System.out.println("Report created. Results available from " + service_root + "Search/" + dupl_file_name);
    }

    /*
     MethodID=R6
     */
    public String getHash(ArrayList<String> in_l) {
        String encripted_nm = "-1";
        if (in_l == null || in_l.isEmpty()) {
        } else {
            Collections.sort(in_l);
            String idlist = in_l.toString().replaceAll("\\[", "").replaceAll("\\]", "").replaceAll("\\s", "");
            encripted_nm = encript(idlist);
        }

        return encripted_nm;
    }

    /*
     MethodID=R5
     */
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
    /*
     MethodID=R7
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
            System.out.println("Error JM3a: " + error);
        }
        return result;
    }
}
