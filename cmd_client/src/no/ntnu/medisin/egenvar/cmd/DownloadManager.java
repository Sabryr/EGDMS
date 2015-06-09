/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package no.ntnu.medisin.egenvar.cmd;

import com.jcraft.jsch.*;
import java.io.*;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Formatter;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author sabryr Error: 100. 101 102 103 104 105.106.107.108,109,110.111
 *
 */
public class DownloadManager {

    private HashMap<String, String[]> location2credent_map;
    private HashMap<String, String> location2directory_map;
    private HashMap<String, ChannelSftp> filepath2channel_map;
    private final byte[] buffer = new byte[1024];
    private HashMap<String, String> location2Hash_map;
    private HashMap<String, String> filename2Hash_map;
    private int progress;
    private boolean cancel;
    public static final String SCP_URL_FLAG = "SCP:";
    public final static String DIRECTORY_PATTERN = "" + SCP_URL_FLAG + "([^/]+):(.*)";
    private Pattern ptn_1;
    private final int MAX_LOGIN_ATTMEPTS = 3;
    private final int MINIMUM_CHECKSUM_LENGTH = 30;
    private Session session;

    public DownloadManager(HashMap<String, String> location2Hash_map, HashMap<String, String> location2directory_map) {

        progress = 0;
        cancel = false;
        this.location2Hash_map = location2Hash_map;
        if (location2credent_map == null) {
            location2credent_map = new HashMap<String, String[]>();
        }
        this.location2directory_map = location2directory_map;
        if (location2directory_map == null) {
            location2directory_map = new HashMap<String, String>();
        }
        filename2Hash_map = new HashMap<String, String>();
        ptn_1 = Pattern.compile(DIRECTORY_PATTERN);
    }

    public static void main(String[] args) {
//        HashMap<String, String> loc2hash_map = new HashMap<String, String>();
//        DownloadManager down = new DownloadManager(loc2hash_map);
//        ArrayList<String> filelocation_list = new ArrayList<String>(1);
//        filelocation_list.add("SCP:dmed4929:/home/sabryr/tmp/test_batch/test_report/test_batch_report_file2.txt");
//        down.recurser("/home/sabryr/tmp/test_batch/test_report/testdown/");
//        System.out.println("ended");
//        System.exit(1);
    }

    public String start(String targetLocation) {
        return recurser(targetLocation);

    }

    private ChannelSftp stablishConnection(String fileLocation, boolean retry) {
        ChannelSftp channelSftp = null;
        if (filepath2channel_map != null) {
            channelSftp = filepath2channel_map.get(fileLocation);
        } else {
            filepath2channel_map = new HashMap<String, ChannelSftp>();
        }
        if (channelSftp == null || channelSftp.isClosed()) {
            String user = "";
            String server = "";
            String password = "";
            String path = "";
            if (fileLocation != null && !fileLocation.isEmpty()) {
                String[] details_a = getDetails(fileLocation);
                if (details_a != null) {
                    server = details_a[0];
                    path = details_a[1];
                    String[] credentials_a = null;
                    filename2Hash_map.put(path, location2Hash_map.get(fileLocation));
//                    location2directory_map.put(path, location2directory_map.remove(fileLocation));
                    if (!retry && location2credent_map.containsKey(fileLocation)) {
                        credentials_a = location2credent_map.get(fileLocation);
                        server = credentials_a[0];
                        user = credentials_a[1];
                        password = credentials_a[2];
                    } else {
                        Console cons = System.console();
                        if (cons != null) {
                            System.out.println("Please enter user name for :" + server);
                            user = cons.readLine();
                            if (user != null && !user.isEmpty()) {
                                System.out.println("Please enter password:");
                                char[] pass = cons.readPassword();
                                for (int i = 0; i < pass.length; i++) {
                                    password = password + pass[i];
                                }
                                if (password != null && !password.isEmpty()) {
                                    credentials_a = new String[4];
                                    credentials_a[0] = server;
                                    credentials_a[1] = user;
                                    credentials_a[2] = password;
                                    credentials_a[3] = path;
                                    location2credent_map.put(fileLocation, credentials_a);
                                } else {
                                    System.out.println("Invalid password");
                                }
                            } else {
                                System.out.println("invalid user name");
                            }

                        } else {
                            System.out.println("Error invoking interactive console. Check authentication (e.g. sh ./egenv -authenticate)");
                        }
                    }
                    if (user != null && !user.isEmpty() && server != null && !server.isEmpty() && password != null && !password.isEmpty()) {
                        try {
                            JSch jsch = new JSch();
                            session = jsch.getSession(user, server, 22);
                            session.setPassword(password);
                            java.util.Properties config = new java.util.Properties();
                            config.put("StrictHostKeyChecking", "no");
                            session.setConfig(config);
                            try {
                                session.connect(3000);
                            } catch (JSchException ex) {
                                System.out.println("Error 103 :" + ex.getMessage() + " diconnecting session please wait..");
                                session.disconnect();
                                jsch.removeAllIdentity();
                                return null;
                            }
                            if (session.isConnected()) {
                                Channel channel = session.openChannel("sftp");
                                channel.connect();
                                channelSftp = (ChannelSftp) channel;
                            }
                        } catch (JSchException ex) {
                            System.out.println("Error 104 :" + ex.getMessage());
                        } catch (Exception ex) {
                            System.out.println("Error 105 :" + ex.getMessage());
                        }
                    } else {
                        System.out.println("Error 106: fialed to retrieve login credentials");
                    }
                } else {
                    System.out.println("Error 102: Invalid path = " + fileLocation);
                }

            } else {
                System.out.println("Error 100: Invalid path = " + fileLocation);
            }
            try {
                if (channelSftp != null) {
                    channelSftp.pwd();
                }
            } catch (SftpException ex) {
                System.out.println("Error 107:" + ex.getMessage());
                channelSftp = null;
            }
            if (channelSftp != null) {
                filepath2channel_map.put(fileLocation, channelSftp);
            }
        }
        return channelSftp;
    }

    private String[] getDetails(String filepath) {
        String server;
        String path;
        if (filepath != null) {
            Matcher m_1 = ptn_1.matcher(filepath);
            if (m_1.matches()) {
                server = m_1.group(1).trim();
                path = m_1.group(2).trim();
                String[] result_a = new String[2];
                result_a[0] = server;
                result_a[1] = path;
                return result_a;
            } else {
                return null;
            }
        } else {
            System.out.println("file path was null");
        }
        return null;
    }

    private String commit(ChannelSftp channelSftp, String s_file, String t_file) {
        progress = 0;
        String summary = "";
        try {
            File t_file_chek = new File(t_file);
            if (t_file_chek.isFile()) {
                System.out.println("Target file found and no action taken. Delete this manully to download to this location");
            } else {
                SftpATTRS atribs = channelSftp.lstat(s_file);
                if (atribs != null && !atribs.isDir()) {
                    long downloadsize = atribs.getSize();
                    if (downloadsize > 0) {
                        System.out.println("Starting download:" + s_file + "\tFile size=" + downloadsize + " bytes (~" + (downloadsize / (1024 * 1024)) + " Mb) \n");
                        String permission = atribs.getPermissionsString();
                        if (permission.matches(".+r.{2}")) {
                            FileOutputStream fileout = null;
                            try {
                                InputStream in = null;
                                fileout = new FileOutputStream(t_file);
                                try {
                                    int len;
                                    in = channelSftp.get(s_file);
                                    try {
                                        int count = 0;
                                        int division = (int) ((downloadsize / buffer.length) / 100);
                                        if (division < 1) {
                                            division = 1;
                                        }
//                                    int currentprogress = 0;
                                        while (!cancel && (len = in.read(buffer)) > 0) {
                                            count++;
//                                        if (count % division == 0) {
//                                            currentprogress++;
//                                            progress = (currentprogress - 1);
////                                        System.out.println("Downloading: " + s_file + " (" + (count * buffer.length) + "/" + downloadsize + ")");
//                                        }
                                            fileout.write(buffer, 0, len);
                                        }
                                        in.close();
                                        fileout.close();

                                        System.out.println("Downloading:" + s_file + " ended. Creating file checksum");
                                        progress = 0;
                                        String cheksum = veryfyFile(t_file, division);
                                        String source_cehcksum = filename2Hash_map.get(s_file);//location2Hash_map.get(c_file_loc);
                                        if (source_cehcksum != null && source_cehcksum.length() > MINIMUM_CHECKSUM_LENGTH) {
                                            if (cheksum.equals(source_cehcksum)) {
                                                summary = summary + " " + t_file + " cheksum=" + cheksum + " (checksum match OK) \n";
                                            } else {
                                                summary = summary + " " + t_file + " cheksum=" + cheksum + " (Warning : checksum match fail) \n";
                                            }

                                        } else {
                                            summary = summary + " " + t_file + " cheksum=" + cheksum + " (cheksum not veryfied) \n";
                                        }
                                        progress = 100;
                                    } catch (IOException ex) {
                                        System.out.println(ex.getMessage() + "\n");
                                    }

                                } catch (SftpException ex) {
                                    System.out.println(ex.getMessage() + "\n");
                                } finally {
                                    try {
                                        in.close();
                                    } catch (IOException ex) {
                                        System.out.println(ex.getMessage() + "\n");
                                    }
                                }

                            } catch (FileNotFoundException ex) {
                                System.out.println(ex.getMessage() + "\n");
                            } finally {
                                try {
                                    if (fileout != null) {
                                        fileout.close();
                                    }
                                } catch (IOException ex) {
                                    System.out.println(ex.getMessage() + "\n");
                                }
                            }
                            System.out.println("Download ended  " + s_file + "\n");
                            System.out.println("Read access  " + permission + " (Read access granted) \n");

                        } else {
                            System.out.println("Read access  " + permission + ". Permission denied \n");
                        }

                    } else {
                        System.out.println("File size =0. May be the file is corrupted or there was a problem in the connection. Try downloading manually");
                    }


                } else {
                    System.out.println("Error accessing remote file\n");
                }
            }
        } catch (SftpException ex) {
            System.out.println(ex.getMessage() + "\n");
        }
        return summary;
    }

    private String veryfyFile(String filenm, int division) {
        String shadigist = "NA";
        MessageDigest md = null;
        FileInputStream filstr = null;
        try {
            md = MessageDigest.getInstance("md5");
            if (md != null) {
                File tmfile = new File(filenm);
                if (tmfile.isFile() && tmfile.canRead()) {
                    filstr = new FileInputStream(tmfile);
                    BufferedInputStream bis = new BufferedInputStream(filstr);
                    md.reset();
                    DigestInputStream dis = new DigestInputStream(bis, md);
                    int count = 0;
                    int currentprogress = 0;
                    while (dis.read(buffer) != -1) {
                        count++;
                        if (count % division == 0) {
                            currentprogress++;
                            progress = (currentprogress - 1);
                        }
                    }
                    byte[] hash = md.digest();
                    shadigist = byteArray2Hex(hash);
                    dis.close();
                    bis.close();
                } else {
                    System.out.println("Error creating checksum. Can not access file\n");
                }
            }
        } catch (FileNotFoundException ex) {
            System.out.println(ex.getMessage() + "\n");
        } catch (IOException ex) {
            System.out.println(ex.getMessage() + "\n");
        } catch (NoSuchAlgorithmException ex) {
            System.out.println(ex.getMessage() + "\n");
        } finally {
            try {
                filstr.close();
            } catch (IOException ex) {
                System.out.println(ex.getMessage() + "\n");
            }
        }
        System.out.println("SHA-1 checksum value for " + filenm + " is" + shadigist + "\n");
        return shadigist;
    }

    private static String byteArray2Hex(byte[] hash) {
        Formatter formatter = new Formatter();
        for (byte b : hash) {
            formatter.format("%02x", b);
        }
        return formatter.toString();
    }

    private String recurser(String tragetLocation) {
        String summary = "";
        progress = 2;
        ArrayList<String> filelocation_list = new ArrayList<String>(location2Hash_map.keySet());
        HashMap<String, String> filenm2Location_map = new HashMap<String, String>();
        if (tragetLocation != null) {
            File dir = new File(tragetLocation);
            if (dir.isDirectory() && dir.canWrite()) {
                try {
                    String targetdirectory = dir.getCanonicalPath();
                    ArrayList<String> ftpdirsPath_final_l = new ArrayList<String>(10);
                    for (int k = 0; (!cancel && k < filelocation_list.size()); k++) {
                        String s_location = filelocation_list.get(k);
                        int tries = MAX_LOGIN_ATTMEPTS;
                        ChannelSftp channelSftp = null;
                        boolean retry = false;
                        while (channelSftp == null && tries > 0) {
                            tries--;
                            channelSftp = stablishConnection(s_location, retry);
                            retry = true;
                        }
                        if (channelSftp != null && !channelSftp.isClosed()) {
                            if (filepath2channel_map.containsKey(s_location)) {
                                String s_file = location2credent_map.get(s_location)[3];
                                if (s_file != null) {
                                    filenm2Location_map.put(s_file, s_location);
                                    try {
                                        SftpATTRS cAtibs = channelSftp.lstat(s_file);
                                        if (cAtibs.isDir()) {
                                            System.out.println(s_file + " is a directory. Starting recursive listing \n");
                                            channelSftp.cd(s_file);
                                            String curentDir = channelSftp.pwd();
                                            ArrayList<ChannelSftp.LsEntry> filelist = new ArrayList(channelSftp.ls("*"));
                                            ArrayList<String> ftpdirsPath_l = new ArrayList<String>(filelist.size());
                                            for (int j = 0; j < filelist.size(); j++) {
                                                String permission = filelist.get(j).getAttrs().getPermissionsString();
                                                String c_file_nm = filelist.get(j).getFilename();
                                                if (permission.matches(".+r.{2}") && filelist.get(j).getAttrs().isDir() && !c_file_nm.equalsIgnoreCase(".") && !c_file_nm.equalsIgnoreCase("..")) {
                                                    String tmp_path = curentDir + "/" + c_file_nm + "/";
                                                    ftpdirsPath_l.add(tmp_path);
                                                }
                                            }
                                            ftpdirsPath_final_l.add(curentDir + "/");
                                            ftpdirsPath_final_l.addAll(ftpdirsPath_l);
                                            boolean done = false;
                                            int safety = 1000;
                                            while (!done && safety > 0) {
                                                safety--;
                                                ArrayList<String> ftpdirsPath_New_l = new ArrayList<String>(ftpdirsPath_l.size());
                                                for (int i = 0; i < ftpdirsPath_l.size(); i++) {
                                                    channelSftp.cd(ftpdirsPath_l.get(i));
                                                    ArrayList<ChannelSftp.LsEntry> filelist_tmp = new ArrayList(channelSftp.ls("*"));
                                                    channelSftp.cd("..");
                                                    for (int j = 0; j < filelist_tmp.size(); j++) {
                                                        String permission = filelist_tmp.get(j).getAttrs().getPermissionsString();
                                                        String c_file_nm = filelist_tmp.get(j).getFilename();
                                                        if (permission.matches(".+r.{2}") && filelist_tmp.get(j).getAttrs().isDir() && !c_file_nm.equalsIgnoreCase(".") && !c_file_nm.equalsIgnoreCase("..")) {
                                                            String cPath = ftpdirsPath_l.get(i) + c_file_nm + "/";
                                                            if (!ftpdirsPath_l.contains(cPath)) {
                                                                ftpdirsPath_New_l.add(cPath);
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
                                                if (safety == 0) {
                                                    System.out.println("Error 5: There were more then " + safety + " files, only the first 1000 was processed\n");
                                                }
                                            }
                                        } else {
                                            ftpdirsPath_final_l.add(s_file);
                                        }
                                    } catch (SftpException ex) {
                                        System.out.println("Error 3: For " + s_file + " \n " + ex.getMessage() + "\n");
                                    }
                                } else {
                                    System.out.println("Error 110: Failed to resolve locaion " + s_location);
                                }
                            } else {
                                System.out.println("Error 109: Failed to resolve locaion " + s_location);
                            }

                        } else {
                            try {
                                session.disconnect();
                            } catch (Exception ex) {
                            }
                            System.out.println("Error 108: failed to stablish connection ");
                        }
                    }
                    progress = 5;
                    for (int i = 0; (!cancel && i < ftpdirsPath_final_l.size()); i++) {
                        String c_file_loc = ftpdirsPath_final_l.get(i);
                        if (filenm2Location_map.containsKey(c_file_loc)) {
                            String c_location = filenm2Location_map.get(c_file_loc);
                            ChannelSftp channelSftp = filepath2channel_map.get(c_location);
                            if (channelSftp != null && !channelSftp.isClosed()) {
                                try {
                                    if (channelSftp.lstat(c_file_loc).isDir()) {
                                        try {
                                            ArrayList<ChannelSftp.LsEntry> filelist = new ArrayList(channelSftp.ls(c_file_loc));
                                            for (int j = 0; (!cancel && j < filelist.size()); j++) {
                                                if (!filelist.get(j).getAttrs().isDir()) {
                                                    String s_file = c_file_loc + filelist.get(j).getFilename();
                                                    String directory_to_use = targetdirectory;
                                                    String target_dir = location2directory_map.get(c_location);
                                                    if (target_dir != null && !target_dir.isEmpty()) {
                                                        File targrt_dit_test = new File(target_dir);
                                                        if (!targrt_dit_test.isDirectory()) {
                                                            targrt_dit_test.mkdirs();
                                                        }
                                                        directory_to_use = target_dir;
                                                    }
                                                    String t_file = directory_to_use + c_file_loc + filelist.get(j).getFilename();
                                                    File tomake_dir = new File(directory_to_use + c_file_loc);
                                                    if (!tomake_dir.isDirectory()) {
                                                        System.out.println("Creating directory " + tomake_dir + " for" + t_file + "\n");
                                                        tomake_dir.mkdirs();
                                                    }
                                                    if (tomake_dir.isDirectory() && tomake_dir.canWrite()) {
                                                        System.out.println("Attempting " + s_file + " \n" + "   Target  " + t_file + "\n");
                                                        summary = summary + commit(channelSftp, s_file, t_file);
                                                    } else {
                                                        System.out.println("Target not accessible, please make sure you have write permission " + tomake_dir + " \n");
                                                    }

                                                }
                                            }
                                        } catch (SftpException ex) {
                                            System.out.println("Error 6: For " + c_file_loc + " \n " + ex.getMessage() + "\n");

                                        }
                                    } else {
                                        String directory_to_use = targetdirectory;
                                        String target_dir = location2directory_map.get(c_location);
                                        if (target_dir != null && !target_dir.isEmpty()) {
                                            File targrt_dit_test = new File(target_dir);
                                            if (!targrt_dit_test.isDirectory()) {
                                                targrt_dit_test.mkdirs();
                                            }
                                            directory_to_use = target_dir;
                                        }
                                        String s_file = c_file_loc;
                                        File tmp = new File(s_file);
                                        String t_file = directory_to_use + File.separatorChar + tmp.getName();
                                        System.out.println("Attempting file " + s_file + " \n" + "   Target  " + t_file + "\n");
                                        summary = summary + commit(channelSftp, s_file, t_file);
                                    }
                                } catch (SftpException ex) {
                                    System.out.println("Error 7: For " + c_file_loc + " \n " + ex.getMessage() + "\n");

                                }
                            } else {
                                System.out.println("Error 112 : connection failed");
                            }
                        }

                        progress = (((i * 100) / ftpdirsPath_final_l.size()) - 10);
                    }
                } catch (IOException ex) {
                    System.out.println("Error accessing target location " + tragetLocation + ". Please select a different location\n " + ex.getMessage());
                }
            } else {
                System.out.println("Error accessing target location " + tragetLocation + ". Please select a different location\n");
            }
        } else {
            System.out.println("Target location is null\n");
        }

        if (filepath2channel_map != null) {
            ArrayList<String> conection_key_list = new ArrayList<String>(filepath2channel_map.keySet());
            for (int i = 0; i < conection_key_list.size(); i++) {
                if (filepath2channel_map.get(conection_key_list.get(i)) != null && !filepath2channel_map.get(conection_key_list.get(i)).isClosed()) {
                    filepath2channel_map.get(conection_key_list.get(i)).disconnect();
                }
            }
        }

        if (session != null && session.isConnected()) {
            session.disconnect();
        }
        return summary;
//        
    }
}
