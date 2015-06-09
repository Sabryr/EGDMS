/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package no.ntnu.medisin.egenvar.server;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.URI;
import java.nio.file.DirectoryStream;
import java.nio.file.FileSystem;
import java.nio.file.FileSystemNotFoundException;
import java.nio.file.FileSystems;
import java.nio.file.FileVisitOption;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.StandardCopyOption;
import java.nio.file.attribute.BasicFileAttributes;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.EnumSet;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import javax.crypto.Cipher;
import javax.crypto.CipherOutputStream;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

/**
 *
 * @author sabryr
 */
public class Backup implements Runnable {

    public static HashMap<String, HashMap<String, String[]>> key_constraint_map;
    private final static int TIME_OUT_LIMIT = 100000000;
    private Connection ncon;
    private HashMap<String, String> instruct_map;
    public static HashMap<String, ArrayList<String>> table2uniqs_l_map;
    private Path outfolder;
    public static HashMap<String, Integer> column2type_map;
    public static ArrayList<String> column2autoincrement_l;
    private String msges;
    public static HashMap<String, ArrayList<String>> table2Columns_map;
    private String encript_password;
    private ArrayList<String> overide_c_tables_l;
    private boolean create_date_folders;
    private boolean ended;
    public int BUFFER_SIZE = 1536;
    private int type = 0;
    private boolean backup_succsess;
    public static boolean auto_backup_schesuled;
    /*
     MrthodID=BK1
     */

    public Backup(HashMap<String, String> instruct_map, Path outfolder, String encript_password,
            ArrayList<String> overide_c_tables_l, boolean create_date_folders, int type) {
        this.instruct_map = instruct_map;
        this.outfolder = outfolder;
        this.encript_password = encript_password;
        this.overide_c_tables_l = overide_c_tables_l;
        this.create_date_folders = create_date_folders;
        this.type = type;
    }

    /*
     MrthodID=BK2
     */
    public boolean hasFinished() {
        if (!ended) {
            try {
                Thread.sleep(5000);
            } catch (InterruptedException ex) {
                System.out.println("Warning EC1a: " + ex.getMessage());
            }
        }
        return ended;
    }

    public boolean getOutCome() {
        return backup_succsess;
    }

    /*
     MrthodID=BK2
     * ToDo: include the cinfig file, to use -fromlast
     */
    @Override
    public void run() {
        ended = false;
        if (type == 0) {
            commit();
        } else if (type == 1) {
            backup_succsess = false;
            backup_succsess = fullBakcup();
        } else if (type == 2) {
            auto_backup_schesuled = true;
            autoBackup();
            auto_backup_schesuled = false;
        } else if (type == 3) {
            backup_succsess = false;
            backup_succsess = restore(null, instruct_map.get(Start_EgenVar1.DATABASE_NAME_DATA_FLAG));
        } else {
            System.out.println("Error BK2a: Bakuptype not undestood");
        }
        ended = true;
        System.out.println("Backup completed @" + Timing.getDateTime());
    }

    /*
     MrthodID=BK16
     */
    private void autoBackup() {
        GregorianCalendar c_cal = new GregorianCalendar();
        int c_h = c_cal.get(Calendar.HOUR_OF_DAY);
        int midnightin = 24 - c_h;
        c_cal.add(Calendar.HOUR, midnightin);
        long wating_time = c_cal.getTimeInMillis() - (new GregorianCalendar()).getTimeInMillis();
        System.out.println("Auto backup scheduled @" + c_cal.getTime());
        try {
            Thread.sleep(wating_time);
            backup_succsess = false;
            backup_succsess = fullBakcup();
        } catch (InterruptedException ex) {
            System.out.println("Warning bk16: " + ex.getMessage());
        }

    }
    /*
     MrthodID=BK11
     */

    private boolean fullBakcup() {
        System.out.println("Calling backup @" + Timing.getDateTime());
        boolean result = false;
        if (instruct_map.containsKey(Start_EgenVar1.BAKSUP_INSTRUCT_FLAG)) {
            String[] instruct_a = instruct_map.get(Start_EgenVar1.BAKSUP_INSTRUCT_FLAG).split("\\|");
            if (instruct_a[0].equalsIgnoreCase("CP")) {
                if (instruct_a.length > 1) {
                    try {
                        Path bakup_loc = Paths.get(instruct_a[1]);
                        if (!Files.exists(bakup_loc)) {
                            Files.createDirectory(bakup_loc);
                        }
                        if (Files.exists(bakup_loc) && Files.isDirectory(bakup_loc) && Files.isWritable(bakup_loc)) {
                            if (getClassPath() != null) {
                                Path source_path = Paths.get(getClassPath()).getParent();
                                if (Files.exists(source_path) && Files.isReadable(source_path)) {
                                    if (instruct_map.containsKey(Start_EgenVar1.DATABASE_NAME_DATA_FLAG)) {
                                        Path source = Paths.get(source_path.toString(), instruct_map.get(Start_EgenVar1.DATABASE_NAME_DATA_FLAG));
                                        if (Files.exists(source) && Files.isDirectory(source) && Files.isReadable(source)) {
                                            result = compressFolder(source.toString(), bakup_loc.toString(), instruct_map.get(Start_EgenVar1.DATABASE_NAME_DATA_FLAG));
                                        } else {
                                            System.out.println("Error BK11e backup  data failed: : failed to get source folder " + source);
                                        }

                                    } else {
                                        System.out.println("Error BK11d backup data failed: : failed to get source to backup");
                                    }
                                    if (instruct_map.containsKey(Start_EgenVar1.DATABASE_NAME_USERS_FLAG)) {
                                        Path source = Paths.get(source_path.toString(), instruct_map.get(Start_EgenVar1.DATABASE_NAME_USERS_FLAG));
                                        if (Files.exists(source) && Files.isDirectory(source) && Files.isReadable(source)) {
                                            result = compressFolder(source.toString(), bakup_loc.toString(), instruct_map.get(Start_EgenVar1.DATABASE_NAME_USERS_FLAG));
                                        } else {
                                            System.out.println("Error BK11f backup user_dbfailed: : failed to get source folder " + source);
                                        }

                                    } else {
                                        System.out.println("Error BK11g backup user_db failed: : failed to get source to backup");
                                    }
                                } else {
                                    System.out.println("Error BK11c backup failed: failed to resolve classpath");
                                }
                            } else {
                                System.out.println("Error bk11f: failed to obtain classpath");
                            }
                        } else {
                            System.out.println("Error BK11b backup failed: Location for backup invalid or inaccessible, " + instruct_a[1]);
                        }

                    } catch (IOException ex) {
                        System.out.println("Error BK11l backup failed: Location for backup invalid or inaccessible," + ex.getMessage());
                    }
                } else {
                    System.out.println("Error BK11a backup failed:: Location for backup missing, provide this with " + Start_EgenVar1.BAKSUP_INSTRUCT_FLAG + "=CP|<LOCATION>");
                }
            } else {
                System.out.println("Error BK11gbackup failed:: Location for backup missing, provide this with " + Start_EgenVar1.BAKSUP_INSTRUCT_FLAG + "=CP|<LOCATION>");
            }
        } else {
            System.out.println("Error BK11g backup failed:: Location for backup missing, provide this with " + Start_EgenVar1.BAKSUP_INSTRUCT_FLAG + "=CP|<LOCATION>");

        }


        return result;
    }

    private String getClassPath() {
        String classpath = System.getProperty("java.class.path");
        if (classpath != null && !classpath.isEmpty() && classpath.endsWith("jar")) {
            try {
                File path_file = new File(classpath);
                classpath = path_file.getAbsoluteFile().toString();
            } catch (Exception ex) {
                System.out.println("Warning: " + ex.getMessage());
            }
        }
        return classpath;
    }
    /*
     MrthodID=BK3
     */

    private void commit() {
        int max_bfore_spliting = Start_EgenVar1.max_before_spliting;
        if (outfolder!=null && Files.exists(outfolder)) {
            if (Files.isWritable(outfolder)) {
                try {
                    String new_outfolder = outfolder.toAbsolutePath().toString();
                    if (create_date_folders) {
                        new_outfolder = outfolder.toAbsolutePath().toString() + File.separator + Timing.getDateTimeForFileName();
                    } else {
                        max_bfore_spliting = Integer.MAX_VALUE;
                    }
                    outfolder = Paths.get(new_outfolder);
                    if (!Files.isDirectory(outfolder)) {
                        outfolder = Files.createDirectories(outfolder);
                    }
                    if (Files.isDirectory(outfolder)) {
                        try {
                            String out_file_nm = outfolder.toAbsolutePath().toString() + File.separatorChar;
                            ArrayList<String> c_tables_l = new ArrayList<>();
                            if (overide_c_tables_l == null) {
                                c_tables_l = getCurrentTables();
                            } else {
                                c_tables_l.addAll(overide_c_tables_l);
                            }
                            //                            ArrayList<String> c_tables_l = new ArrayList<>();
//                            c_tables_l.add("EGEN_DATAENTRY.REPORT");
                            for (int i = 0; i < c_tables_l.size(); i++) {
                                String c_tbl = c_tables_l.get(i);
                                System.out.println(c_tbl + " -- " + (i + 1) + " of " + c_tables_l.size());
//                                ArrayList<Integer> id_l = getId_l_Results("select id from " + c_tbl);
                                System.out.println("Limited to 100");
                                ArrayList<Integer> id_l = getId_l_Results("select id from " + c_tbl + " where id<100");
                                if (id_l.isEmpty()) {
                                    System.out.println("Nothing to process in " + c_tbl);
                                } else {
                                    Collections.sort(id_l);
                                    int size = id_l.size();
//                                if (id_l.size() > Start_EgenVar1.max_before_spliting) {
                                    int splits = (size / max_bfore_spliting) + 1;
                                    for (int j = 0; j < splits; j++) {
                                        int min = j * max_bfore_spliting;
                                        int max = (j + 1) * max_bfore_spliting;
                                        int min_pos = 0;
                                        int max_pos = id_l.size() - 1;
                                        if (max_pos < 0) {
                                            max_pos = 0;
                                        }
                                        if (min < max_pos) {
                                            min_pos = min;
                                        }
                                        if (max_pos > max) {
                                            max_pos = max;
                                        }
//                                        System.out.println("109 min_pos=" + min_pos + " max_pos=" + max_pos + " size=" + size);
                                        String c_out_file = out_file_nm + "__" + c_tbl.split("\\.")[c_tbl.split("\\.").length - 1] + ".egen";
                                        if (create_date_folders) {
                                            c_out_file = out_file_nm + j + "_" + Timing.getDateTimeForFileName() + "__" + c_tbl.split("\\.")[c_tbl.split("\\.").length - 1] + ".egen";
                                        }
                                        String sql = null;
                                        if (min_pos == max_pos) {
                                            sql = "select * from " + c_tbl + " ";
                                        } else {
                                            sql = "select * from " + c_tbl + " where id >" + (id_l.get(min_pos)) + " AND id<= " + (id_l.get(max_pos));
                                        }
                                        HashMap<String, ArrayList<String>> result_map = getAdvanced_Results(sql, true);
                                        if (result_map.isEmpty()) {
                                            System.out.println("Nothing to process in " + c_tbl);
                                        } else {
                                            if (c_tbl.toUpperCase().contains("TAGSOURCE")) {
                                                System.out.println("Creating tagsource");
                                                createContent_FromSearch(c_out_file, null, c_tbl, result_map, null, "||");
                                            } else {
                                                createContent_FromSearch(c_out_file, null, c_tbl, result_map, null, "\t");
                                            }

                                        }
                                    }
                                }


                            }
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    } else {
                        System.out.println("Error BK3b: access failed " + new_outfolder);
                    }

                } catch (IOException ex) {
                    System.out.println("Error BK3a:" + ex.getMessage());
                }
            } else {
                System.out.println("Error bk3c: Write access denied for " + outfolder);
            }
        } else {
            System.out.println("Error bk3d: could not access " + outfolder);
        }

    }
//    /*
//     MrthodID=BK3
//     */
//    private void commit() {
//        if (Files.exists(outfolder)) {
//            if (Files.isWritable(outfolder)) {
//                try {
//                    String new_outfolder = outfolder.toAbsolutePath().toString() + File.separator + Timing.getDateTimeForFileName();
//                    outfolder = Files.createDirectories(Paths.get(new_outfolder));
//                    if (Files.isDirectory(outfolder)) {
////                        FileWriter out_file = null;
//                        try {
//                            String out_file_nm = outfolder.toAbsolutePath().toString() + File.separatorChar + Timing.getDateTimeForFileName();
//                            ArrayList<String> c_tables_l = getCurrentTables(); // new ArrayList<>();//
////                               c_tables_l.add(instruct_map.get(Start_EgenVar1.DATABASE_NAME_DATA_FLAG) + ".REPORT");
////                            c_tables_l.add(instruct_map.get(Start_EgenVar1.DATABASE_NAME_DATA_FLAG) + ".REPORT_HIERARCHY");
////                            c_tables_l.add(instruct_map.get(Start_EgenVar1.DATABASE_NAME_DATA_FLAG) + ".INSTRUMENTS_TAGSOURCE");
////                              c_tables_l.add(instruct_map.get(Start_EgenVar1.DATABASE_NAME_DATA_FLAG) + ".REPORT2TAGS");
//
//                            for (int i = 0; i < c_tables_l.size(); i++) {
//                                String c_tbl = c_tables_l.get(i);
//                                String c_out_file = out_file_nm + "__" + c_tbl.split("\\.")[c_tbl.split("\\.").length - 1] + ".egen";
//                                String sql = "select * from " + c_tbl;
//                                System.out.println(c_tbl);
//                                HashMap<String, ArrayList<String>> result_map = getAdvanced_Results(sql, true);
//
//                                writeResultsToFileEncripted(result_map, c_out_file);
////                                ArrayList<String> key_l = new ArrayList<>(result_map.keySet());
////                                for (int j = 0; j < key_l.size(); j++) {
////                                    System.out.println("98 " + key_l.get(j) + "=> " + result_map.get(key_l.get(j)));
////                                }
////                                out_file.close();
////                                System.out.println("\n");
//                            }
////                            if (out_file != null) {
////                                out_file.close();
////                            }
//                        } catch (Exception ex) {
//                            ex.printStackTrace();
//                        } finally {
////                            try {
////                                if (out_file != null) {
////                                    out_file.close();
////                                }
////                            } catch (IOException ex) {
////                            }
//                        }
//                    } else {
//                        System.out.println("Error: access failed " + new_outfolder);
//                    }
//
//                } catch (IOException ex) {
//                    System.out.println("Error :" + ex.getMessage());
//                }
//            } else {
//                System.out.println("Error: Write access denied for " + outfolder);
//            }
//        } else {
//            System.out.println("Error: could not access " + outfolder);
//        }
//
//    }

    private void createContent_FromSearch(String prepfilenm, String directory, String table, HashMap<String, ArrayList<String>> found_in_db_map,
            HashMap<String, ArrayList<String>> new_values_overide_map, String column_seperator) {
        ArrayList<String> from_db_l = getParamAndDependancies(table);
        if (from_db_l != null) {
            ArrayList<String> column_constraints_l = new ArrayList<>(from_db_l);
            ArrayList<String> compulsory_not_null_l = new ArrayList<>(1);
            ArrayList<String> compulsory_preselected_l = new ArrayList<>(1);
            ArrayList<String> compulsory_existing_l = new ArrayList<>(1);
            for (int i = 0; i < column_constraints_l.size(); i++) {
                String c_column = column_constraints_l.get(i);
                String field_nam = c_column;
//                String value = "NA";
                String[] column_split_a = c_column.split("==");
                for (int j = 0; j < column_split_a.length; j++) {
                    String c_col = column_split_a[j];
                    if (c_col.startsWith("COLUMN=")) {
                        field_nam = c_col.replaceFirst("COLUMN=", "");
                        if (field_nam.endsWith("***")) {
                            compulsory_existing_l.add(field_nam.replaceAll("\\*", ""));
                        } else if (field_nam.endsWith("**")) {
                            compulsory_preselected_l.add(field_nam.replaceAll("\\*", ""));
                        } else if (field_nam.endsWith("*")) {
                            compulsory_not_null_l.add(field_nam.replaceAll("\\*", ""));
                        }
                    }
                }
            }

            String name_flag = table + ".name";
            name_flag = name_flag.toUpperCase();
            if (new_values_overide_map != null && new_values_overide_map.containsKey(name_flag) && found_in_db_map.containsKey(name_flag)) {
                ArrayList<String> overide_names_l = new_values_overide_map.get(name_flag);
                ArrayList<String> from_db_names_l = found_in_db_map.get(name_flag);
                ArrayList<String> overide_key_l = new ArrayList<>(new_values_overide_map.keySet());
                for (int j = 0; j < overide_names_l.size(); j++) {
                    int cpos = from_db_names_l.indexOf(overide_names_l.get(j));
                    if (cpos >= 0) {
                        for (int i = 0; i < overide_key_l.size(); i++) {
                            if (found_in_db_map.containsKey(overide_key_l.get(i)) && found_in_db_map.get(overide_key_l.get(i)).size() > cpos) {
                                found_in_db_map.get(overide_key_l.get(i)).set(cpos, new_values_overide_map.get(overide_key_l.get(i)).get(j));
                            }
                        }
                    }
                }
            }
            if (found_in_db_map.isEmpty()) {
                System.out.println("Nothing to process");
            } else {

                String updated_file_nm = writeToDescriptFile2(prepfilenm, directory, "", found_in_db_map, compulsory_not_null_l,
                        compulsory_preselected_l, compulsory_existing_l, column_seperator);
                System.out.println(updated_file_nm + " created \n");
//                System.out.println("Processing complete. Use  \"-update " + updated_file_nm + "\"  to commit the chages to the database.\nOr Use  \"-delete " + updated_file_nm + "\"  to delete these from the database \n");
            }
        } else {
            System.out.println("Error 4657: Communication error.Server was not available. Please try again later.");
            System.exit(4657);
        }
    }

    /*
     MrthodID=BK5
     */
    private String writeToDescriptFile2(String filename, String directory, String comments, HashMap<String, ArrayList<String>> content_map,
            ArrayList<String> compulsory_not_null_l, ArrayList<String> compulsory_preselected_l, ArrayList<String> compulsory_existing_l, String column_seperator) {
        ArrayList<String> column_nm_l = new ArrayList<>(content_map.keySet());
        String heading = "##";

        for (int i = 0; i < column_nm_l.size(); i++) {
            String sepcial = "";
            String c_nm = column_nm_l.get(i);
            if (c_nm.endsWith(".id")) {
                sepcial = Start_EgenVar1.ID_COLUMN_COMMENT_FLAG;
            } else if (compulsory_existing_l != null && compulsory_existing_l.contains(c_nm)) {
                sepcial = "***";
            } else if (compulsory_preselected_l != null && compulsory_preselected_l.contains(c_nm)) {
                sepcial = "**";
            } else if (compulsory_not_null_l != null && compulsory_not_null_l.contains(c_nm)) {
                sepcial = "*";
            }
            if (heading.matches("##")) {
                heading = heading + c_nm + sepcial;
            } else {
                heading = heading + column_seperator + c_nm + sepcial;
            }
        }
        FileWriter egen_description_file = null;
        if (directory != null) {
            File c_dir = new File(directory);
            if (c_dir.isDirectory() && c_dir.canWrite()) {
                if (!directory.endsWith(File.separator) && !filename.startsWith(File.separator)) {
                    filename = directory + File.separatorChar + filename;
                } else {
                    filename = directory + filename;
                }

            } else {
                System.out.println("Error BK5A: Can not write to " + directory + ". Please veryfy you have write access.");
            }
        }
        File c_file = new File(filename);
        String parent_folder = null;
        if (create_date_folders && c_file.isFile()) {
            try {
                parent_folder = c_file.getAbsoluteFile().getParentFile().getCanonicalPath();
                String backup_name = c_file.getName() + Timing.getDateTimeForFileName() + "~";
                File bakup_file = new File(parent_folder + File.separatorChar + backup_name);
                c_file.renameTo(bakup_file);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
//        if (c_file.isFile()) {
        try {
            egen_description_file = new FileWriter(filename, false);
            //if recursive then append
            egen_description_file.append(comments + "\n");
            egen_description_file.append(heading + "\n");
        } catch (IOException ex) {
            ex.printStackTrace();
        }
//        } else {
//            System.out.println("Error 6b invalid file " + filename);
//        }
//        String content = "";
//        System.out.println("Writing description file");

        try {
            if (column_nm_l.size() > 0) {
                int itarations = content_map.get(column_nm_l.get(0)).size();
                int last_progress = 0;
                for (int j = 0; j < itarations; j++) {
                    int progrss = ((j * 100) / itarations);
                    if (progrss > last_progress) {
                        if (progrss % 10 == 0) {
                            System.out.print(".");
                        }
                        last_progress = progrss;
                    }

                    String c_content = "";
                    for (int i = 0; i < column_nm_l.size(); i++) {
                        String c_k = column_nm_l.get(i);
                        if (!content_map.get(c_k).isEmpty() && content_map.get(c_k).size() > j) {
                            String content = content_map.get(c_k).get(j);
                            if (content == null || content.isEmpty()) {
                                content = "-1";
                            }
                            if (i == 0) {
                                c_content = content;
                            } else {
                                c_content = c_content + column_seperator + content;
                            }
                        } else {
                            System.out.println("\tError BK5B : " + content_map.get(c_k) + " for " + c_k);
                        }
                    }
                    if (!c_content.isEmpty() && egen_description_file != null) {
                        egen_description_file.append(c_content + "\n");
                    }
                }
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        System.out.println("");
        if (egen_description_file != null) {
            try {
                egen_description_file.close();
                System.out.println("Crating file " + filename + " @" + Timing.getDateTime());
            } catch (IOException ex) {
                ex.printStackTrace();
            } finally {
                try {
                    egen_description_file.close();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        } else {
            System.out.println("Error 6a: file creation failed " + filename);
        }
        return filename;
    }

    /**
     * MethodID=BK22
     *
     */
    private ArrayList<String> getColumns(String current_tbl_nm) {
        ArrayList<String> columns_l = new ArrayList<>(2);
        current_tbl_nm = get_correct_table_name(current_tbl_nm);
        if (table2Columns_map == null && current_tbl_nm != null) {
            table2Columns_map = new HashMap<>();
            Connection ncon = null;
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
                        ResultSet key_result = metaData.getColumns(null, instruct_map.get(Start_EgenVar1.DATABASE_NAME_DATA_FLAG), tablenm4metadata, null);
                        if (!key_result.next()) {
                            key_result = metaData.getColumns(null, instruct_map.get(Start_EgenVar1.DATABASE_NAME_DATA_FLAG).toUpperCase(), tablenm4metadata.toUpperCase(), null);
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
                    ncon.close();
                }
            } catch (SQLException e) {
                System.out.println("Error AS22a" + e.getMessage());
            } finally {
                close(ncon, null, null);
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
     * MethodID=RS28
     *
     * To do: Need to remove the database name from column names
     */
    private ArrayList<String> getParamAndDependancies(String table) {
        HashMap<String, String> overides_map = new HashMap<>();
        String correct_tbl_nm = get_correct_table_name(table);
        Connection ncon = null;
        Statement stm_1 = null;
        try {
            ncon = getConnection();
            stm_1 = ncon.createStatement();
            String timestp = "";
            try {
                timestp = Timing.getDate();
                overides_map.put("ENTRYDATE", timestp);
            } catch (Exception e) {
            }

            close(ncon, stm_1, null);
        } catch (Exception ex) {
            msges = "Error AS28a: creating connections " + ex.getMessage();
        } finally {
            close(ncon, stm_1, null);
        }
        HashMap<String, String[]> constraints_map = get_key_contraints(correct_tbl_nm);
//          HashMap<String, String[]> constraints_map = get_key_contraints(table, new ArrayList<String>(overides_map.keySet()));
        ArrayList<String> allcoulmns_l = getColumns(correct_tbl_nm);//constraints_map.get(ALL_COLUMNS_FLAG);
        String[] unique_a = constraints_map.get(Start_EgenVar1.UNIQUE_TO_USER_COLUMNS_FLAG);

        ArrayList<String> unique_l = new ArrayList<>(1);
        if (unique_a != null && unique_a.length > 0) {
            unique_l = new ArrayList<>(Arrays.asList(unique_a));
        }
        ArrayList<String> all_columns = new ArrayList<>();
        HashMap<String, String> helptext = getfiledHelp();
        if (allcoulmns_l != null) {
            for (int i = 0; i < allcoulmns_l.size(); i++) {
                String must = "";
                String c_column_nm = allcoulmns_l.get(i);
                if (containsIgnoreCase(unique_l, c_column_nm)) {
                    must = "*";
                }
                if (overides_map.containsKey(c_column_nm)) {
                    must = must + "*";
                }
                if (c_column_nm.equals(Start_EgenVar1.ID_COLUMN_FALG) || (c_column_nm.endsWith("_" + Start_EgenVar1.ID_COLUMN_FALG) && !overides_map.containsKey(c_column_nm))) {
                } else {
                    String modified_column_nm = "COLUMN=" + table.split("\\.")[table.split("\\.").length - 1] + "." + c_column_nm + must;
                    if (helptext.containsKey(c_column_nm)) {
                        modified_column_nm = modified_column_nm + "==HELP=" + helptext.get(c_column_nm);
                    }
                    if (overides_map.containsKey(c_column_nm)) {
                        modified_column_nm = modified_column_nm + "==VALUE=" + overides_map.get(c_column_nm);
                    }
                    if (!all_columns.contains(modified_column_nm)) {
                        all_columns.add(modified_column_nm);
                    }
                }
            }
        }
        ArrayList<String> tablestocheck_l = new ArrayList<>();

        for (int i = 0; i < unique_l.size(); i++) {
            String[] foreign_relations_a = constraints_map.get(unique_l.get(i));
            if (foreign_relations_a != null) {
                if (!tablestocheck_l.contains(foreign_relations_a[1])) {
                    tablestocheck_l.add(foreign_relations_a[1]);
                }
            }
        }

        tablestocheck_l.remove(correct_tbl_nm);

        for (int j = 0; j < tablestocheck_l.size(); j++) {
            constraints_map = get_key_contraints(tablestocheck_l.get(j));
            allcoulmns_l = getColumns(tablestocheck_l.get(j));// constraints_map.get(ALL_COLUMNS_FLAG);
            unique_a = constraints_map.get(Start_EgenVar1.UNIQUE_TO_USER_COLUMNS_FLAG);
            unique_l = new ArrayList<>(1);
            if (unique_a != null && unique_a.length > 0) {
                unique_l = new ArrayList<>(Arrays.asList(unique_a));
            }
            if (allcoulmns_l != null) {
                for (int i = 0; i < allcoulmns_l.size(); i++) {
                    String must = "";
                    String c_column_nm = allcoulmns_l.get(i);
                    if (containsIgnoreCase(unique_l, c_column_nm)) {
                        must = "*";
                        if (overides_map.containsKey(c_column_nm)) {
                            must = must + "*";
                        }
                        if (must.length() == 1) {
                            must = must + "**";
                        } else {
                            must = must + "*";
                        }
                        if (c_column_nm.equalsIgnoreCase(Start_EgenVar1.ID_COLUMN_FALG) || (c_column_nm.endsWith("_" + Start_EgenVar1.ID_COLUMN_FALG) && !overides_map.containsKey(c_column_nm))) {
                            //Ignoring all non compulsarry columns
                        } else {
                            String modified_column_nm = "COLUMN=" + tablestocheck_l.get(j).split("\\.")[tablestocheck_l.get(j).split("\\.").length - 1] + "." + c_column_nm + must;
                            if (helptext.containsKey(c_column_nm)) {
                                modified_column_nm = c_column_nm + must + "==HELP=" + helptext.get(c_column_nm);
                            }
                            if (overides_map.containsKey(c_column_nm)) {
                                modified_column_nm = modified_column_nm + "==VALUE=" + overides_map.get(c_column_nm);
                            }
                            if (!all_columns.contains(modified_column_nm)) {
                                all_columns.add(modified_column_nm);
                            }
                        }
                    }

                }
            }
        }

        return all_columns;
    }

    /**
     * MethodID=BK38
     *
     */
    public boolean containsIgnoreCase(ArrayList<String> in_l, String matchit) {
        boolean found = false;
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
            msges = "Error AS19: creating connections " + ex.getMessage();
        }
        return found;
    }

    /**
     * MethodID=BK27
     *
     */
    private HashMap<String, String> getfiledHelp() {
        HashMap<String, String> help_map = new HashMap<>();
        Connection ncon = null;
        Statement st_1 = null;
        ResultSet r_1 = null;
        try {
            ncon = getConnection();
            String sql = "select name,helptext from fieldhelp";
            r_1 = ncon.createStatement().executeQuery(sql);
            while (r_1.next()) {
                help_map.put(r_1.getString(1), r_1.getString(2));
            }
            close(ncon, st_1, r_1);
        } catch (SQLException ex) {
        } finally {
            close(ncon, st_1, r_1);
        }
        return help_map;
    }

    /*
     MrthodID=BK4
     */
    private void writeResultsToFileEncripted(Object indata, String file_nm) {
        if (indata != null && file_nm != null) {
            try {
                byte key[] = encript_password.getBytes();
                DESKeySpec desKeySpec = new DESKeySpec(key);
                SecretKeyFactory keyFactory;
                keyFactory = SecretKeyFactory.getInstance("DES");
                SecretKey secretKey = keyFactory.generateSecret(desKeySpec);
                // Create Cipher
                Cipher desCipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
                desCipher.init(Cipher.ENCRYPT_MODE, secretKey);
                // Create stream
                FileOutputStream fos = new FileOutputStream(file_nm);
                BufferedOutputStream bos = new BufferedOutputStream(fos);
                CipherOutputStream cos = new CipherOutputStream(bos, desCipher);
                ObjectOutputStream oos = new ObjectOutputStream(cos);
                oos.writeObject(indata);
//                oos.writeInt(last);
                oos.flush();
                oos.close();
            } catch (NoSuchAlgorithmException ex) {
            } catch (InvalidKeyException ex) {
            } catch (InvalidKeySpecException ex) {
            } catch (NoSuchPaddingException ex) {
            } catch (FileNotFoundException ex) {
            } catch (IOException ex) {
            }
        }

    }
    /*
     MrthodID=BK6
     */

    private ArrayList<Integer> getId_l_Results(String sql) {
        ArrayList<Integer> id_l = new ArrayList<>();

        ResultSet r_1 = null;
        Statement st_1 = null;
        try {
            st_1 = getConnection().createStatement();
            try {
                st_1.setQueryTimeout(TIME_OUT_LIMIT);
                r_1 = st_1.executeQuery(sql);
                while (r_1.next()) {
                    id_l.add(r_1.getInt(1));
                }
            } catch (SQLException ex) {
                String er_sql = sql.subSequence(0, ((sql.length() > 100) ? 100 : sql.length())) + "";
                System.out.println("Error BK6b: creating connections or error in query " + ex.getMessage() + "\nsql=" + er_sql);
            }
        } catch (SQLException ex) {
            System.out.println("Error BK6a: creating connections or error in query " + ex.getMessage() + "\nsql=" + sql);
            ex.printStackTrace();
        } finally {
            close(null, st_1, r_1);
        }
        return id_l;
    }

    /*
     MrthodID=BK5
     */
    private HashMap<String, ArrayList<String>> getAdvanced_Results(String sql, boolean addcolumnnames) {
//        ArrayList<String> result_l = new ArrayList<>(1);
        HashMap<String, ArrayList<String>> result_map = new HashMap<>();
        ResultSet r_1 = null;
        Statement st_1 = null;
        try {
            if (getConnection() != null) {
                st_1 = getConnection().createStatement();
                try {
                    st_1.setQueryTimeout(TIME_OUT_LIMIT);
                    r_1 = st_1.executeQuery(sql);
                } catch (SQLException ex) {
                    String er_sql = sql.subSequence(0, ((sql.length() > 100) ? 100 : sql.length())) + "";
                    System.out.println("Error BK5b: creating connections or error in query " + ex.getMessage() + "\nsql=" + er_sql);
                }

                if (r_1 != null) {
                    ResultSetMetaData rsmd = r_1.getMetaData();
                    String table_nm = rsmd.getTableName(1);
                    getColumnType(table_nm, rsmd.getColumnName(1));
                    int NumOfCol = rsmd.getColumnCount();
                    int id_col_pos = -1;
                    for (int i = 1; i <= NumOfCol; i++) {
                        if (column2autoincrement_l.contains(get_correct_table_name(table_nm) + "." + rsmd.getColumnName(i))) {
                            id_col_pos = i;
                        }
                    }
                    ArrayList<String> foreing_key_clmns_l = new ArrayList<>(1);
                    HashMap<String, String[]> constraint_map = get_key_contraints(table_nm);
                    if (constraint_map != null) {
                        String[] foreing_key_clmns_a = constraint_map.get(Start_EgenVar1.FOREIGN_KEY_COLUMNS_FLAG);
                        if (foreing_key_clmns_a != null && foreing_key_clmns_a.length > 0) {
                            foreing_key_clmns_l = new ArrayList<>(Arrays.asList(foreing_key_clmns_a));
                        }
                    }
                    table_nm = get_correct_table_name(table_nm);
                    HashMap<String, String> table2uniques_map = new HashMap<>();
//                    int limit = 100;
//                    System.out.println("\n\n!! limited to " + limit);
                    while (r_1.next()) { //&& limit > 0
//                        limit--;
//                        String result_tmp = "";
                        for (int i = 1; i <= NumOfCol; i++) {
                            String c_val = r_1.getString(i);
                            if (id_col_pos == i) {
                                //Skipping id
//                                if (c_val != null && c_val.matches("[0-9]+")) {
//                                    id_list.add(new Integer(c_val));
//                                } else {
//                                    id_list.add(-1);
//                                }
                            } else {
                                String clmn_lable = rsmd.getColumnLabel(i);
                                String full_clmn_lable = rsmd.getTableName(i) + "." + clmn_lable;
                                if (foreing_key_clmns_l.contains(clmn_lable) && constraint_map != null && !(constraint_map.get(clmn_lable)[1].equals(table_nm))) {
                                    String forgn_clmn = "name";
                                    String corrected_forng_tbl = get_correct_table_name(constraint_map.get(clmn_lable)[1]);
                                    if (table2uniques_map.containsKey(corrected_forng_tbl)) {
                                        forgn_clmn = table2uniques_map.get(corrected_forng_tbl);
                                    } else {
                                        ArrayList<String> tm_l = getUniqueList(corrected_forng_tbl);
                                        if (tm_l != null && !tm_l.isEmpty()) {
                                            table2uniques_map.put(corrected_forng_tbl, tm_l.toString().replace("[", "").replace("]", ""));
                                        }
                                        forgn_clmn = table2uniques_map.get(corrected_forng_tbl);
                                    }
                                    if (c_val != null && !c_val.equalsIgnoreCase("NULL")) {
                                        List result_expand_l = getforQuery("SELECT " + forgn_clmn + " from " + corrected_forng_tbl + " where " + corrected_forng_tbl + "." + constraint_map.get(clmn_lable)[2] + "=" + c_val);
                                        for (int j = 0; j < result_expand_l.size(); j++) {
                                            if (result_expand_l.get(j) != null) {
                                                String[] split_1_a = result_expand_l.get(j).toString().split("\\|\\|");
                                                for (int k = 0; k < split_1_a.length; k++) {
                                                    String c_r_a_p = split_1_a[k];
                                                    String[] c_r_a = c_r_a_p.split("=");
                                                    if (c_r_a.length == 2) {
                                                        if (!result_map.containsKey(c_r_a[0])) {
                                                            result_map.put(c_r_a[0], new ArrayList<String>());
                                                        }
                                                        result_map.get(c_r_a[0]).add(c_r_a[1]);
                                                    } else {
                                                        System.out.println("Error BK5a: for " + result_expand_l.get(j));
                                                    }
                                                }
                                            }
                                        }
                                    } else {
                                        System.out.println("Warning : " + constraint_map.get(clmn_lable)[2] + " was null in " + corrected_forng_tbl);
                                    }
                                } else if (foreing_key_clmns_l.contains(clmn_lable) && constraint_map != null) {
                                    if (!result_map.containsKey(full_clmn_lable)) {
                                        result_map.put(full_clmn_lable, new ArrayList<String>());
                                    }
                                    result_map.get(full_clmn_lable).add(c_val);
                                } else {
                                    if (!result_map.containsKey(full_clmn_lable)) {
                                        result_map.put(full_clmn_lable, new ArrayList<String>());
                                    }
                                    result_map.get(full_clmn_lable).add(c_val);
                                }
                            }
                        }
                    }
                }
                close(null, st_1, r_1);
            }
        } catch (SQLException ex) {
            System.out.println("Error ABK5a: creating connections or error in query " + ex.getMessage() + "\nsql=" + sql);
            ex.printStackTrace();
        } finally {
            close(null, null, r_1);
        }
        return result_map;
    }

    /*
     MrthodID=BK6
     */
    private HashMap<String, String[]> get_key_contraints(String current_tbl_nm) {
        if (key_constraint_map == null) {
            key_constraint_map = new HashMap<>();
        }
        HashMap<String, String[]> returning_map = new HashMap<>(1);
        if (current_tbl_nm != null && !current_tbl_nm.isEmpty() && !current_tbl_nm.equalsIgnoreCase("null") && !key_constraint_map.containsKey(current_tbl_nm)) {
            try {

                if (getConnection() != null) {
                    returning_map.putAll(get_key_contraint_names(current_tbl_nm));
                    try {
                        ArrayList<String> foreign_columns = new ArrayList<>(1);
                        ArrayList<String> foreign_key_names_columns = new ArrayList<>(1);
                        ArrayList<String> foreign_tables = new ArrayList<>(1);
                        DatabaseMetaData metaData = getConnection().getMetaData();
                        String tablenm4metadata = current_tbl_nm.split("\\.")[current_tbl_nm.split("\\.").length - 1];

                        ResultSet key_result = metaData.getImportedKeys(getConnection().getCatalog(), null, tablenm4metadata);
                        if (!key_result.next()) {
                            key_result = metaData.getImportedKeys(getConnection().getCatalog(), instruct_map.get(Start_EgenVar1.DATABASE_NAME_DATA_FLAG), tablenm4metadata.toUpperCase());
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

    /*
     MrthodID=BK7
     */
    private HashMap<String, String[]> get_key_contraint_names(String current_tbl_nm) {
        HashMap<String, String[]> returning_map = new HashMap<>();

        if (current_tbl_nm != null && !current_tbl_nm.isEmpty() && !current_tbl_nm.equalsIgnoreCase("null")) {
            try {
                if (getConnection() != null) {
                    try {
                        DatabaseMetaData metaData = getConnection().getMetaData();
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
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }

        }
        return returning_map;
    }

    /*
     MrthodID=BK8
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

    /*
     MrthodID=BK9
     */
    private ArrayList<String> getCurrentTables() {
        ArrayList<String> result_l = new ArrayList<>(20);
        Statement stmt = null;
        if (getConnection() != null) {
            try {
                stmt = getConnection().createStatement();
                String q = "select TABLENAME from sys.systables WHERE UPPER(CAST(TABLETYPE AS CHAR(1))) = 'T' and SCHEMAID=(select SCHEMAID from SYS.SYSSCHEMAS where UPPER(CAST(SCHEMANAME AS VARCHAR(128)))=UPPER('" + instruct_map.get(Start_EgenVar1.DATABASE_NAME_DATA_FLAG).toUpperCase() + "'))";
                ResultSet r_1 = stmt.executeQuery(q);
                while (r_1.next()) {
                    result_l.add(instruct_map.get(Start_EgenVar1.DATABASE_NAME_DATA_FLAG).toUpperCase() + "." + r_1.getString(1));
                }
                r_1.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            } finally {
                close(null, stmt, null);
            }
        }
        return result_l;
    }

    /*
     MrthodID=BK10
     */
    private Connection getConnection() {
        try {
            if (ncon == null || ncon.isClosed()) {
                Class.forName("org.apache.derby.jdbc.ClientDriver").newInstance();
                ncon = DriverManager.getConnection(Start_EgenVar1.dbURL_dataEntry);
            }
        } catch (Exception ex) {
            String error = ex.getMessage();
            System.out.println("Error 8a: " + error);
        }
        return ncon;
    }

    public ArrayList<String> getUniqueList(String table) {
        table = get_correct_table_name(table);
        ArrayList<String> uniquet_col_l_l = new ArrayList<>(1);
        if (table2uniqs_l_map == null || !table2uniqs_l_map.containsKey(table)) {
            table2uniqs_l_map = new HashMap<>();
            HashMap<String, String[]> const_map = get_key_contraints(table);
            String[] uniq_a = const_map.get(Start_EgenVar1.UNIQUE_TO_USER_COLUMNS_FLAG);
            if (uniq_a != null) {
                uniquet_col_l_l.addAll(Arrays.asList(uniq_a));
            }
            table2uniqs_l_map.put(table, uniquet_col_l_l);
        }
        return table2uniqs_l_map.get(table);
    }

    /*
     MrthodID=BK11
     */
    private ArrayList<String> getforQuery(String query) {
        ArrayList<String> c_result_l = new ArrayList<>(1);
        Statement st_1 = null;
        try {
            st_1 = getConnection().createStatement();
            ResultSet r_1 = st_1.executeQuery(query);
            ResultSetMetaData rsmd = r_1.getMetaData();
            String table_nm = rsmd.getTableName(1);
            int NumOfCol = rsmd.getColumnCount();
            while (r_1.next()) {
                if (NumOfCol > 0) {
                    String result_tmp = null;// table_nm + "." + rsmd.getColumnLabel(1) + "=" + r_1.getString(1);
                    for (int i = 0; i < NumOfCol; i++) {
                        if (!column2autoincrement_l.contains(get_correct_table_name(table_nm) + "." + rsmd.getColumnName(i + 1))) {
                            if (result_tmp == null) {
                                result_tmp = table_nm + "." + rsmd.getColumnLabel(1) + "=" + r_1.getString(i + 1);
                            } else {
                                result_tmp = result_tmp + "||" + table_nm + "." + rsmd.getColumnName(i + 1) + "=" + r_1.getString(i + 1);
                            }
                        }
                    }
                    if (result_tmp != null) {
                        c_result_l.add(result_tmp);
                    }
                }
            }
            r_1.close();

        } catch (Exception ex) {
            System.out.println("Error AS44a: creating connections " + ex.getMessage() + "\n" + query);

        } finally {
            close(ncon, st_1, null);
        }

        return c_result_l;
    }


    /*
     MrthodID=BK2
     */
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
            } catch (SQLException e) {
                System.out.println("Error C1a" + e.getMessage());
            }
        }
        if (column2type_map.containsKey(reqest_keynm)) {
            type = column2type_map.get(reqest_keynm);
        }
        return type;
    }

    /*
     MrthodID=BK13
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
//        try {
//            if (connection != null && !connection.isClosed()) {
//                try {
//                    connection.close();
//                } catch (SQLException e) {
//                }
//            }
//        } catch (SQLException e) {
//        }
    }

    /*
     MethodID=bk13;
     */
    private boolean compressFolder(String in, String target, String folder_name) {
        boolean result = false;
        try {
            Path in_p = Paths.get(in);
            Path target_p = Paths.get(target);
            Path zipname = Paths.get(target_p.toAbsolutePath().toString(), in_p.getFileName().toString() + "_BKUP_" + Timing.getDateTimeForFileName() + ".zip");
            ArrayList<String> file_l = getFiles_recursive(in_p, false, new ArrayList<String>());
            System.out.println("\nCompressing " + in + " to " + zipname);
            result = createZipArchive(zipname.toString(), file_l, folder_name);
        } catch (IOException ex) {
            System.out.println("Error bk13a :" + ex.getMessage());
        }
        return result;
    }

    /*
     MethodID=bk14;
     */
    public boolean createZipArchive(String zipFilename, ArrayList<String> files_l, String folder_name)
            throws IOException {
        boolean result = false;
        try (FileSystem zipFileSystem = createZipFileSystem(zipFilename, true)) {
//            final Path root = zipFileSystem.getPath(folder_name);
            for (int i = 0; i < files_l.size(); i++) {
                String filename = files_l.get(i);
                String[] name_split = filename.split(folder_name, 2);
                if (name_split.length > 1) {
                    Path src = Paths.get(filename);
                    //add a file to the zip file system
                    if (!Files.isDirectory(src)) {
                        final Path dest = zipFileSystem.getPath(folder_name, name_split[1]);
                        final Path parent = dest.getParent();
                        if (Files.notExists(parent)) {
                            Files.createDirectories(parent);
                        }
                        Files.copy(src, dest, StandardCopyOption.REPLACE_EXISTING);
                        result = true;
                    }
                }
            }
        }
        return result;
    }

    /*
     MethodID=bk15;
     */
    private FileSystem createZipFileSystem(String zipFilename, boolean create)
            throws IOException {
        FileSystem fileSystem = null;
        // convert the filename to a URI
        final Path path = Paths.get(zipFilename);
        final URI uri = URI.create("jar:file:" + path.toUri().getPath());

        final Map<String, String> env = new HashMap<>();
        if (create) {
            env.put("create", "true");
        }
        try {
            fileSystem = FileSystems.getFileSystem(uri);
        } catch (FileSystemNotFoundException ex) {
            System.out.println("Creating new archive file system");
        }
        try {
            if (fileSystem == null || !fileSystem.isOpen()) {
                fileSystem = FileSystems.newFileSystem(uri, env);
            }
        } catch (FileSystemNotFoundException ex) {
            System.out.println("Error bk15b:" + ex.getMessage());
        }
        return fileSystem;
    }
    /*
     MethodID=bk12;
     */

    private ArrayList<String> getFiles_recursive(Path location, boolean skip_hidden, ArrayList<String> in_avoid_l) {
        ArrayList<String> files_to_use_l = new ArrayList<>();
        try {
            if (Files.isReadable(location)) {
                if (Files.isRegularFile(location, LinkOption.NOFOLLOW_LINKS)) {
                    files_to_use_l.add(location.toRealPath().toString());
                } else {
                    DirectoryStream<Path> ds = null;
                    try {
                        ds = Files.newDirectoryStream(location);
                        Iterator<Path> path_l = ds.iterator();
                        int cc = 0;
                        while (path_l.hasNext()) {
                            cc++;
                            Path c_file = path_l.next();
                            if (Files.isDirectory(c_file, LinkOption.NOFOLLOW_LINKS) || Files.isSymbolicLink(c_file)) {
                                LoadRecursive pf = new LoadRecursive(skip_hidden);
                                Files.walkFileTree(c_file.toRealPath(), EnumSet.of(FileVisitOption.FOLLOW_LINKS), Integer.MAX_VALUE, pf);
                                ArrayList<Path> files_to_use_l_paths = pf.getOKlist();
                                in_avoid_l.addAll(pf.avoidOKlist_string());
                                if (files_to_use_l_paths.isEmpty()) {
//                                    System.out.println("No files matched the criteria. in " + c_file);
                                } else {
                                    for (int i = 0; i < files_to_use_l_paths.size(); i++) {
                                        files_to_use_l.add(files_to_use_l_paths.get(i).toRealPath().toString());
                                    }
                                }
                            } else if (Files.isRegularFile(c_file)) {
                                if (skip_hidden && Files.isHidden(c_file)) {
                                    in_avoid_l.add(c_file.toRealPath().toString());
                                } else {
                                    files_to_use_l.add(c_file.toRealPath().toString());
                                }

                            }
                        }
                        ds.close();
                    } catch (IOException ex) {
                        System.out.println("Error 6b: locaiotn=" + ex.getMessage());
                        ex.printStackTrace();
                    } finally {
                        try {
                            if (ds != null) {
                                ds.close();
                            }
                        } catch (IOException ex) {
                        }
                    }
                }
            } else {
                System.out.println("Error 6a: locaiotn=" + location + " not accessible");
            }
        } catch (IOException ex) {
            System.out.println("Error 6b: locaiotn=" + ex.getMessage());
            ex.printStackTrace();
        }
        return files_to_use_l;
    }

    public boolean restore(String databaseDir, final String database_name) {
        boolean result = false;
        try {
            if (databaseDir == null && instruct_map.containsKey(Start_EgenVar1.DATABASE_NAME_DATA_FLAG)) {
                Path source = Paths.get(Paths.get(getClassPath()).getParent().toString(), instruct_map.get(Start_EgenVar1.DATABASE_NAME_DATA_FLAG));
                if (Files.exists(source) && Files.isDirectory(source) && Files.isReadable(source)) {
                    databaseDir = source.toString();
                } else {
                    System.out.println("Error BK11e backup  data failed: : failed to get source folder " + source);
                }

            } else {
                System.out.println("Error BK11d backup data failed: : failed to get source to backup");
            }
            System.out.println("Restoring to: " + databaseDir);
            if (databaseDir != null) {
                final Path destDir = Paths.get(databaseDir);
                String[] instruct_a = instruct_map.get(Start_EgenVar1.BAKSUP_INSTRUCT_FLAG).split("\\|");
                Path backup_loc = null;
                if (instruct_a[0].equalsIgnoreCase("CP")) {
                    if (instruct_a.length > 1) {
                        backup_loc = Paths.get(Paths.get(getClassPath()).getParent().toString(), instruct_a[1]);
                    }
                } else {
                    backup_loc = Paths.get(Start_EgenVar1.getuserInputSameLine("Current backup configuration do not allow auto-restore. "
                            + "Please provide the folder path with backup archives", null));
                }
                ArrayList<String> path_name_l = new ArrayList<>();
                ArrayList<Path> path_fullpath_l = new ArrayList<>();
                if (Files.exists(backup_loc)) {
                    DirectoryStream<Path> ds = Files.newDirectoryStream(backup_loc, "*EGEN_DATAENTRY*zip");
                    Iterator<Path> path_l = ds.iterator();
                    ArrayList<Path> path_l_soreted = new ArrayList<>();
                    while (path_l.hasNext()) {
                        path_l_soreted.add(path_l.next());
//                        Path c_path = path_l.next();
//                        path_name_l.add(c_path.getFileName().toString());
//                        path_fullpath_l.add(c_path);
                    }
                    Collections.sort(path_l_soreted);
                    for (int i = 0; i < path_l_soreted.size(); i++) {
                        path_name_l.add(path_l_soreted.get(i).getFileName().toString());
                        path_fullpath_l.add(path_l_soreted.get(i));
                    }
                } else {
                    System.out.println("Invalid backup location : " + backup_loc);
                }
                if (path_name_l.isEmpty()) {
                    System.out.println("No EGDMS backups were found in " + backup_loc);
                } else {
                    path_name_l.add("Exit without making any changes");
                    int ans = Start_EgenVar1.getUserChoice(path_name_l, "Select the version to restore");
                    if (ans < path_fullpath_l.size()) {
                        Path bk_path = path_fullpath_l.get(ans);
                        System.out.println("Restore from " + bk_path);
                        if (Files.notExists(destDir)) {
                            System.out.println("Creating " + destDir);
                            Files.createDirectories(destDir);
                        } else {
                            System.out.println("Recreating " + destDir);
                            deleteDir(destDir);
                            Files.createDirectories(destDir);
                            System.out.println(" " + destDir + " OK");
                            if (Start_EgenVar1.getuserInputSameLine("The database will be restored to the data selected "
                                    + "and all modifications after this date will be lost. Do you want to continue ?", "YES|NO").equalsIgnoreCase("YES")) {
                                final Path destDir_to_use = destDir;//.getParent();
                                System.out.println("Creating new restore point, just incase if something goes wrong");
                                System.out.println("Destination :" + destDir_to_use);
                                if (fullBakcup()) {
                                    FileSystem zipFileSystem = createZipFileSystem(bk_path.toString(), false);
                                    Files.walkFileTree(zipFileSystem.getPath("/"), new SimpleFileVisitor<Path>() {
                                        @Override
                                        public FileVisitResult visitFile(Path file,
                                                BasicFileAttributes attrs) throws IOException {
                                            String[] name_split_a = file.toString().split(database_name, 2);
                                            if (name_split_a.length == 2) {
                                                final Path destFile = Paths.get(destDir_to_use.toString(), name_split_a[1]);
                                                System.out.print(".");
                                                Files.copy(file, destFile, StandardCopyOption.REPLACE_EXISTING);
                                            }
                                            return FileVisitResult.CONTINUE;
                                        }

                                        @Override
                                        public FileVisitResult preVisitDirectory(Path dir,
                                                BasicFileAttributes attrs) throws IOException {
                                            if (dir.getFileName() != null) {
                                                String[] name_split_a = dir.toString().split(database_name, 2);
                                                if (name_split_a.length == 2) {
                                                    final Path dirToCreate = Paths.get(destDir_to_use.toString(), name_split_a[1]);
                                                    if (Files.notExists(dirToCreate)) {
//                                                        System.out.printf("Creating directory %s\n", dirToCreate);
                                                        Files.createDirectory(dirToCreate);
                                                    }
                                                }
                                            }
                                            return FileVisitResult.CONTINUE;
                                        }
                                    });
                                    result = true;
                                } else {
                                    System.out.println("Aborting..");
                                }
                            } else {
                                System.out.println("Creating restore point failed");
                            }

                        }
                    } else {
                        System.out.println("Aborting..");
                    }
                }
            } else {
                System.out.println("Locating the taget failed ");
            }
        } catch (IOException ex) {
            ex.printStackTrace();
            System.out.println("Error " + ex.getMessage());
        }
        return result;
    }

    private void deleteDir(Path directory) {
        try {

            Files.walkFileTree(directory, new SimpleFileVisitor<Path>() {
                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                    Files.delete(file);
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                    Files.delete(dir);
                    return FileVisitResult.CONTINUE;
                }
            });
        } catch (IOException ex) {
            System.out.println("Error  " + ex.getMessage());
            ex.printStackTrace();
        }
    }
}
