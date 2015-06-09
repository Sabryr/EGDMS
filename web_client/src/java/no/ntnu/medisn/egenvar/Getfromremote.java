/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package no.ntnu.medisn.egenvar;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpATTRS;
import com.jcraft.jsch.SftpException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

/**
 *
 * @author sabryr
 */
public class Getfromremote implements Runnable {

    private String site;
    private String username;
    private String password;
    private String dirtouse;
    private boolean done;
    private LinkedHashMap<String, ArrayList<String>> resutl_map;
    private int saftey = 12000000;
    private String warnings = "";
    private final int MAXIMUM_TO_DISPLAY = 100;
    private final int MAXIMUM_TO_CONSIDER = 500;
    private final int MINIMUM_FILESIZE = 100;
    private final long MAXIMUM_FILESIZE = 107374182400l;//102410;//107374182400l;
    private boolean dir_lister = false;
//    private String warnings;
    private HashMap<String, String[]> file2properties_aMap;
    private Connection c_con;

    public Getfromremote(Connection c_con,String site, String username, String password, String dirtouse, boolean dir_lister) {
        this.site = site;
        this.username = username;
        this.password = password;
        this.dirtouse = dirtouse;
        this.dir_lister = dir_lister;
        this.c_con=c_con;
    }

    @Override
    public void run() {
        warnings = "";
        saftey = 12000000;
        done = false;
        if (dir_lister) {
            getFromRemote(c_con,dirtouse, site, username, password);
        } else {
            get(dirtouse, site, username, password);
        }

    }

    public LinkedHashMap<String, ArrayList<String>> getResults() {
        while (!done && saftey > 0) {
            saftey--;
            try {
                Thread.sleep(500);
            } catch (InterruptedException ex) {
            }
        }
        if (!warnings.trim().isEmpty()) {
            resutl_map.put("ERRORS", new ArrayList<String>());
            resutl_map.get("ERRORS").add(warnings);
        }
        return resutl_map;
    }

    public HashMap<String, String[]> getDirectoryListerResult() {
        while (!done && saftey > 0) {
            saftey--;
            try {
                Thread.sleep(500);
            } catch (InterruptedException ex) {
            }
        }
        if (file2properties_aMap == null) {
            file2properties_aMap = new HashMap<>();
        }
        if (!warnings.trim().isEmpty()) {
            file2properties_aMap.put("ERRORS", new String[]{warnings});
        }
        return file2properties_aMap;
    }

    private void get(String dirtouse, String site, String user, String pass) {
        try {
            ChannelSftp channelSftp = getChannelSftp(null, user, pass, site);
            resutl_map = new LinkedHashMap<>();
            if (channelSftp != null && !channelSftp.isClosed()) {
                SftpATTRS cAtibs = null;
                boolean directory_ok = false;
                try {
                    cAtibs = channelSftp.lstat(dirtouse);
                    directory_ok = true;
                } catch (SftpException ex) {
                    warnings = warnings + "\n" + ex.getMessage();

                }
                if (cAtibs == null) {
                    warnings = warnings + "\n" + "<p> <error> Error 4013: The location '" + dirtouse + "' was not found</error></p> ";
                } else if (!directory_ok) {
                    warnings = warnings + "\n" + "<p> <error> Error 4014 The location '" + dirtouse + "' was not found</error></p> ";
                } else {
                    ArrayList<ChannelSftp.LsEntry> filelist = new ArrayList<>();
                    String curentDir = ".";
                    try {
                        channelSftp.cd(dirtouse);
                        curentDir = channelSftp.pwd();
                        filelist = new ArrayList(channelSftp.ls("*"));
                        channelSftp.cd("..");
                        resutl_map.put(Constants.FROM_REPMOTE_PARENT_FOLDER_FLAG, new ArrayList<String>());
                        resutl_map.get(Constants.FROM_REPMOTE_PARENT_FOLDER_FLAG).add(channelSftp.pwd());
                        channelSftp.cd(dirtouse);
                    } catch (SftpException ex) {
                        warnings = warnings + "<p> <error> Error 4022: " + ex.getMessage() + "</error></p> ";
                    }

//                    ArrayList<String> ftpdirsPath_l = new ArrayList<>(filelist.size());
                    for (int i = 0; i < filelist.size(); i++) {
                        String permission = filelist.get(i).getAttrs().getPermissionsString();
                        String c_file_nm = filelist.get(i).getFilename();

                        if (permission.matches(".+r.{2}.+") && filelist.get(i).getAttrs().isDir() && !c_file_nm.equalsIgnoreCase(".") && !c_file_nm.equalsIgnoreCase("..")) {
                            String tmp_path = curentDir + "/" + c_file_nm + "/";
                            ArrayList<String> new_l = new ArrayList<>();
                            channelSftp.cd(tmp_path);
                            new_l.add("" + (channelSftp.ls("*").size()));
                            channelSftp.cd("..");
//                            ftpdirsPath_l.add(tmp_path);
                            resutl_map.put(tmp_path, new_l);
                        }
                    }
//                    for (int i = 0; i < ftpdirsPath_l.size(); i++) {
//                        resutl_map.put(ftpdirsPath_l.get(i), new ArrayList<String>());
//
//                    }
                }
                if (warnings != null && !warnings.isEmpty()) {
                    warnings = warnings + "<p> <error> Error 4002: " + warnings + "</error></p> ";
                }
            }
        } catch (Exception ex) {
            warnings = warnings + "\n" + ex.getMessage();
        }


        done = true;
    }

    private void getFromRemote(Connection c_con,String dirtouse, String site, String user, String pass) {
        ChannelSftp channelSftp = getChannelSftp(null, user, pass, site);

        if (channelSftp != null && !channelSftp.isClosed()) {
            String files_table_name = Constants.get_correct_table_name(c_con, "files");
            if (file2properties_aMap == null) {
                file2properties_aMap = new HashMap<>();
            }
            SftpATTRS cAtibs = null;
            boolean matchdir = false;
            String matchpathval = dirtouse;

            try {
                cAtibs = channelSftp.lstat(dirtouse);
            } catch (SftpException ex) {
                warnings = warnings + "<p> <error> Error 40102:Loction not found " + ex.getMessage() + "</error></p> ";
                matchdir = true;
            }
            if (cAtibs == null) {
                warnings = warnings + "<p> <error> Error 4013: The location '" + dirtouse + "' was not found</error></p> ";
            } else {
                HashMap<String, ArrayList<ChannelSftp.LsEntry>> directory2content = new HashMap<>();
                HashMap<String, Long> directory2size = new HashMap<>();
                HashMap<String, ArrayList<String>> directoryName2Path = new HashMap<>();
                ArrayList<ChannelSftp.LsEntry> filelist = new ArrayList<>();
                String curentDir = ".";
                try {
                    channelSftp.cd(dirtouse);
                    curentDir = channelSftp.pwd();
                    filelist = new ArrayList(channelSftp.ls("*"));
                } catch (SftpException ex) {
                    warnings = warnings + "<p> <error> Error 4022: " + ex.getMessage() + "</error></p> ";
                }
                ArrayList<String> ftpdirsPath_l = new ArrayList<>(filelist.size());
                for (int i = 0; i < filelist.size(); i++) {
                    String permission = filelist.get(i).getAttrs().getPermissionsString();
                    String c_file_nm = filelist.get(i).getFilename();
                    if (permission.matches(".+r.{2}.+") && filelist.get(i).getAttrs().isDir() && !c_file_nm.equalsIgnoreCase(".") && !c_file_nm.equalsIgnoreCase("..")) {
                        String tmp_path = curentDir + "/" + c_file_nm + "/";
                        ftpdirsPath_l.add(tmp_path);
                    }
                }
                ArrayList<String> ftpdirsPath_final_l = new ArrayList<>(ftpdirsPath_l.size());
                ftpdirsPath_final_l.add(curentDir + "/");
                ftpdirsPath_final_l.addAll(ftpdirsPath_l);
                boolean done = false;

                while (!done && (ftpdirsPath_final_l.size() < MAXIMUM_TO_CONSIDER)) {
                    ArrayList<String> ftpdirsPath_New_l = new ArrayList<>(ftpdirsPath_l.size());
                    for (int i = 0; i < ftpdirsPath_l.size(); i++) {
                        ArrayList<ChannelSftp.LsEntry> filelist_tmp = new ArrayList<>();
                        try {
                            channelSftp.cd(ftpdirsPath_l.get(i));
                            filelist_tmp = new ArrayList(channelSftp.ls("*"));
                            if (!directory2content.containsKey(ftpdirsPath_l.get(i))) {
                                directory2content.put(ftpdirsPath_l.get(i), filelist_tmp);
                            }
                            channelSftp.cd("..");
                        } catch (SftpException ex) {
                            warnings = warnings + "<p> <error> Error 4002: " + ex.getMessage() + "</error></p> ";
                        }
                        for (int j = 0; j < filelist_tmp.size(); j++) {
                            String permission = filelist_tmp.get(j).getAttrs().getPermissionsString();
                            String c_file_nm = filelist_tmp.get(j).getFilename();
                            if (permission.matches(".+r.{2}.+") && filelist_tmp.get(j).getAttrs().isDir() && !c_file_nm.equalsIgnoreCase(".") && !c_file_nm.equalsIgnoreCase("..")) {
                                String cPath = ftpdirsPath_l.get(i) + c_file_nm + "/";
                                if (!ftpdirsPath_l.contains(cPath)) {
                                    ftpdirsPath_New_l.add(cPath);
                                }
                                if (!directoryName2Path.containsKey(c_file_nm)) {
                                    ArrayList<String> tmp = new ArrayList<>(1);
                                    tmp.add(cPath);
                                    directoryName2Path.put(c_file_nm, tmp);
                                } else {
                                    if (!directoryName2Path.get(c_file_nm).contains(cPath)) {
                                        directoryName2Path.get(c_file_nm).add(cPath);
                                    }
                                }
                            }
                        }
                    }
                    if (ftpdirsPath_New_l.isEmpty()) {
                        done = true;
                    }
                    ftpdirsPath_l.clear();
                    ftpdirsPath_l.addAll(ftpdirsPath_New_l);
                    ftpdirsPath_final_l.addAll(ftpdirsPath_New_l);
                    if (ftpdirsPath_final_l.size() >= MAXIMUM_TO_CONSIDER) {
                        warnings = warnings + "Warning !, more than " + MAXIMUM_TO_CONSIDER + " folders found and only the first " + MAXIMUM_TO_CONSIDER + " checked. Please specify subfolders to avoid this";
                    }
                }
                ArrayList<String> directory2content_key_tmp_l = new ArrayList<>(directory2content.keySet());
                boolean allexpanded = false;
                while (!allexpanded) {
                    allexpanded = true;
                    for (int k = 0; k < directory2content_key_tmp_l.size(); k++) {
                        String c_path = directory2content_key_tmp_l.get(k);
                        ArrayList<ChannelSftp.LsEntry> centry_l = directory2content.get(c_path);
                        for (int i = 0; i < centry_l.size(); i++) {
                            ChannelSftp.LsEntry tmp_lse = centry_l.remove(i);
                            if (tmp_lse.getAttrs().getPermissionsString().matches(".+r.{2}.+")) {
                                if (tmp_lse.getAttrs().isDir()) {
                                    if (directoryName2Path.containsKey(tmp_lse.getFilename())) {
                                        ArrayList<String> posible_dirs = directoryName2Path.get(tmp_lse.getFilename());
                                        for (int j = 0; j < posible_dirs.size(); j++) {
                                            if (posible_dirs.get(j).startsWith(c_path)) {
                                                if (directory2content.containsKey(posible_dirs.get(j))) {
                                                    ArrayList<ChannelSftp.LsEntry> tmp = directory2content.get(posible_dirs.get(j));
                                                    for (int l = 0; l < tmp.size(); l++) {
                                                        if (tmp.get(l).getAttrs().getPermissionsString().matches(".+r.{2}.+")) {
                                                            if (centry_l.size() > MAXIMUM_TO_CONSIDER) {
                                                                warnings = warnings + "Warning !, more than " + MAXIMUM_TO_CONSIDER + " folders found and only the first " + MAXIMUM_TO_CONSIDER + " checked. Please specify subfolders to avoid this";
                                                                allexpanded = true;
                                                            } else {
                                                                centry_l.add(centry_l.size(), tmp.get(l));
                                                                if (tmp.get(l).getAttrs().isDir()) {
                                                                    allexpanded = false;
                                                                }
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                    i--;
                                } else {
                                    centry_l.add(centry_l.size(), tmp_lse);
                                }
                            }
                        }
                        directory2content.put(c_path, centry_l);
                    }
                }
                directory2content_key_tmp_l = new ArrayList<>(directory2content.keySet());
                for (int k = 0; k < directory2content_key_tmp_l.size(); k++) {
                    String c_path = directory2content_key_tmp_l.get(k);
                    ArrayList<ChannelSftp.LsEntry> centry_l = directory2content.get(c_path);
                    long size = 0;
                    for (int i = 0; i < centry_l.size(); i++) {
                        if (!centry_l.get(i).getAttrs().isDir()) {
                            size = size + centry_l.get(i).getAttrs().getSize();
                        }
                    }
                    directory2size.put(c_path, size);
                }
                int display_limit = ftpdirsPath_final_l.size();

                if (display_limit > MAXIMUM_TO_DISPLAY) {
                    display_limit = MAXIMUM_TO_DISPLAY;
                    if (warnings.isEmpty()) {
                        warnings = warnings + " Warning !, more than " + MAXIMUM_TO_DISPLAY + " entries found and only the first " + MAXIMUM_TO_DISPLAY + " will be checked. Please specify subfolders to avoid this.";
                    }
                }
                for (int i = 0; (i < ftpdirsPath_final_l.size() && display_limit > 0); i++) {
                    ArrayList<ChannelSftp.LsEntry> filelist_tmp = new ArrayList<>();
                    try {
                        SftpATTRS tmp_cAtibs = channelSftp.lstat(ftpdirsPath_final_l.get(i));
                        if (tmp_cAtibs.getPermissionsString().matches(".+r.{2}.+")) {
                            channelSftp.cd(ftpdirsPath_final_l.get(i));
                            filelist_tmp = new ArrayList(channelSftp.ls("*"));
                            channelSftp.cd("..");
                        }
                    } catch (SftpException ex) {
                        System.out.println("238 DL " + ex.getMessage());
                    }
                    for (int j = 0; j < filelist_tmp.size(); j++) {
                        ChannelSftp.LsEntry cFile = filelist_tmp.get(j);
                        if (cFile.getAttrs().getPermissionsString().matches(".+r.{2}.+")) {
                            String fileOrFolder = "";
                            String fileType = "NA";
                            String cFile_location_path = ftpdirsPath_final_l.get(i) + cFile.getFilename();
                            String filenm = cFile_location_path;
                            boolean usefile = true;
                            if (matchdir && !cFile_location_path.contains(matchpathval)) {
                                usefile = false;
                            }
                            if (usefile) {
                                display_limit--;
                                long cFileSuizeinbytes = cFile.getAttrs().getSize();
                                boolean included = false;
                                if (!cFile.getAttrs().isDir()) {
                                    fileOrFolder = " <isafile>File</isafile>";
                                    try {
                                        long size = channelSftp.lstat(cFile_location_path).getSize();
                                        if (size >= 0 && cFileSuizeinbytes > MINIMUM_FILESIZE && cFileSuizeinbytes <= MAXIMUM_FILESIZE) {
                                            included = true;
                                        }
                                    } catch (Exception ex) {
                                        System.out.println("Error 009 : " + ex.getMessage() + "  " + filenm);
                                    }
                                } else {
                                    included = true;
                                    fileOrFolder = " <isafolder>Folder</isafolder>";
                                    cFile_location_path = cFile_location_path + File.separatorChar;
                                    if (directory2size.containsKey(cFile_location_path)) {
                                        cFileSuizeinbytes = directory2size.get(cFile_location_path);
                                    } else {
                                        cFileSuizeinbytes = 0;
                                    }
                                }

                                if (included) {
                                    if (!file2properties_aMap.containsKey(cFile_location_path)) {
                                        file2properties_aMap.put(cFile_location_path, new String[19]);
                                    }
                                    try {
                                        File loca_tmp = new File(cFile_location_path);
                                        String report_nm = loca_tmp.getParent();
                                        loca_tmp = new File(report_nm);
                                        file2properties_aMap.get(cFile_location_path)[14] = loca_tmp.getName();
                                        String batch_nm = loca_tmp.getParent();
                                        loca_tmp = new File(batch_nm);
                                        file2properties_aMap.get(cFile_location_path)[15] = loca_tmp.getName();
                                    } catch (Exception ex) {
                                    }
                                    file2properties_aMap.get(cFile_location_path)[10] = filenm;
                                    file2properties_aMap.get(cFile_location_path)[0] = cFile_location_path;//cFile_Full_path;
                                    file2properties_aMap.get(cFile_location_path)[1] = fileOrFolder;

                                    String cFileSuize = cFileSuizeinbytes + " bytes";
                                    if (cFileSuizeinbytes > 1073741824) {
                                        cFileSuize = (cFileSuizeinbytes / 1073741824) + "Gb ";
                                    } else if (cFileSuizeinbytes > 1048576) {
                                        cFileSuize = (cFileSuizeinbytes / 1048576) + "Mb ";
                                    } else if (cFileSuizeinbytes > 1024) {
                                        cFileSuize = (cFileSuizeinbytes / 1024) + "Kb ";
                                    }

                                    file2properties_aMap.get(cFile_location_path)[2] = cFileSuize;
                                    String status = "<newentry>" + fileOrFolder + "</newentry>";
                                    boolean fileFoundInDB = false;// 
                                    String file_nm_to_ids_from_db = "";

//                                    if (fileToId_map != null && fileToId_map.containsKey(cFile_location_path)) {
//                                        fileFoundInDB = true;
//                                        ArrayList<Integer> idlist = fileToId_map.get(cFile_location_path);
//                                        ;
//                                        ArrayList<String> loc_l = new ArrayList<>(1);
//                                        for (int k = 0; k < idlist.size(); k++) {
//                                            if (id2Location_map.containsKey(idlist.get(k))) {
//                                                loc_l.removeAll(id2Location_map.get(idlist.get(k)));
//                                                loc_l.addAll(id2Location_map.get(idlist.get(k)));
//                                            }
//                                        }
//                                        String static_url_file_nm = makeSTATIC_URL(idlist, files_table_name, Constants.getDocRoot());
//                                        String qr_url = Constants.getServerName() + "Search/SearchResults3?" + Constants.TABLE_TO_USE_FLAG + files_table_name + "=" + static_url_file_nm + "";
//                                        status = "<alreadysubmited>Already submited (<a target=\"_blank\" href=\"" + qr_url + "\">Link</a>) | Location=" + loc_l + "</alreadysubmited>";
//                                    }
                                    file2properties_aMap.get(cFile_location_path)[3] = status;
                                    String timestp = "";
                                    try {
                                        timestp = Constants.getTimeStampFormat().format(new Date(((long) cFile.getAttrs().getMTime()) * 1000));
                                    } catch (Exception e) {
                                    }

                                    timestp = timestp.replaceAll("\\s", "&nbsp;");
                                    file2properties_aMap.get(cFile_location_path)[4] = timestp;
                                    file2properties_aMap.get(cFile_location_path)[16] = cFile.getAttrs().getMTime() + "";
                                    file2properties_aMap.get(cFile_location_path)[5] = null;
                                    file2properties_aMap.get(cFile_location_path)[8] = cFile.getAttrs().getGId() + "";
                                    file2properties_aMap.get(cFile_location_path)[9] = fileFoundInDB + "";

                                    file2properties_aMap.get(cFile_location_path)[11] = fileType;
                                    file2properties_aMap.get(cFile_location_path)[12] = file_nm_to_ids_from_db;
                                    file2properties_aMap.get(cFile_location_path)[13] = cFileSuizeinbytes + "";
                                    file2properties_aMap.get(cFile_location_path)[18] = site;
                                }
                            }
                        }

                    }
                }

            }
            if (warnings != null && !warnings.isEmpty()) {
                warnings = warnings + "<p> <error> Error 4002: " + warnings + "</error></p> ";
            }
        }
        if (warnings != null && !warnings.isEmpty()) {
            warnings = warnings + "<p> <error> Error 4002: " + warnings + "</error></p> ";
        }

        done = true;
    }

    public String makeSTATIC_URL(ArrayList<Integer> id_l, String current_tbl_nm, String doc_root) {
        String encripted_nm = Constants.makeSTATIC_URL(id_l, current_tbl_nm);
        File file = new File(doc_root + encripted_nm);
        if (file.isFile()) {
        } else {
            writeResultsToFile(current_tbl_nm + "||" + id_l.toString().replaceAll("[^0-9,]", ""), Constants.getDocRoot(c_con) + encripted_nm);
        }
        return encripted_nm;
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

    private ChannelSftp getChannelSftp(ChannelSftp channelSftp, String user, String pass, String site) {

        if (channelSftp == null || channelSftp.isClosed()) {
            if (user != null && pass != null && site != null && !site.isEmpty()) {
                try {
                    JSch jsch = new JSch();
                    Session session = jsch.getSession(user, site, 22);

                    session.setPassword(decrypt(Logintodirectory.encript_password, pass));
                    java.util.Properties config = new java.util.Properties();
                    config.put("StrictHostKeyChecking", "no");
                    session.setConfig(config);
                    try {
                        session.connect(120000);
                    } catch (JSchException ex) {
                        String error = ex.getMessage();
                        warnings = warnings + "<p> <font color='red'> Error 4007 : " + ex.getMessage() + "</font></p> ";
                    }
                    if (session.isConnected()) {
                        Channel channel = session.openChannel("sftp");
                        channel.connect();
                        channelSftp = (ChannelSftp) channel;
                        return channelSftp;
                    }
                } catch (JSchException ex) {
                    warnings = warnings + "<p> <error> Error 4008:" + ex.getMessage() + "</error></p> ";
                }
            } else {
                warnings = warnings + "<p> <error> Error 4009: Failed to connect</error></p> ";
            }
            return channelSftp;
        } else {
            return channelSftp;
        }
    }

    public String decrypt(String in_key, String encrypted) {
        try {
            byte key[] = in_key.getBytes();
            DESKeySpec desKeySpec = new DESKeySpec(key);
            SecretKeyFactory keyFactory;
            keyFactory = SecretKeyFactory.getInstance("DES");
            SecretKey secretKey = keyFactory.generateSecret(desKeySpec);
            Cipher cipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, secretKey);
            return new String(cipher.doFinal(encrypted.getBytes("ISO-8859-1")), "ISO-8859-1");
        } catch (NoSuchAlgorithmException ex) {
            warnings = warnings + "<p> <error> Error 9001: " + warnings + ", encrypted content, accessible only during same session</error></p> ";
        } catch (InvalidKeyException ex) {
            warnings = warnings + "<p> <error> Error 9002: " + warnings + ", encrypted content, accessible only during same session</error></p> ";
        } catch (InvalidKeySpecException ex) {
            warnings = warnings + "<p> <error> Error 9003: " + warnings + ", encrypted content, accessible only during same session</error></p> ";
        } catch (NoSuchPaddingException ex) {
            warnings = warnings + "<p> <error> Error 9004: " + warnings + ", encrypted content, accessible only during same session</error></p> ";
        } catch (GeneralSecurityException ex) {
            warnings = warnings + "<p> <error> Error 9005: " + warnings + ", encrypted content, accessible only during same session</error></p> ";
        } catch (UnsupportedEncodingException ex) {
            warnings = warnings + "<p> <error> Error 9006: " + warnings + ", encrypted content, accessible only during same session</error></p> ";
        }
        return encrypted;
    }

    @Override
    public String toString() {
        if (dir_lister) {
            if (file2properties_aMap == null) {
                return "Empty file2properties_aMap";
            } else {
                return file2properties_aMap.toString();
            }

        } else {
            if (resutl_map == null) {
                return "Empty getfromremote";
            } else {
                return resutl_map.toString();
            }
        }


    }
}
