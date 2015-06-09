/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package no.ntnu.medisin.egenvar.cmd;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.TimeZone;

/**
 *
 * @author sabryr
 */
public class GetFileDetails implements Runnable {

    private String filenm;
//    private static String checksum = null;
    public static HashMap<String, HashMap<String, String>> file2name_map;
    private String server_name = null;
    private SimpleDateFormat sdf_2 = null;
    private String batch=null;
    private String report=null;
    
//    public static String name_flag = "FILES.NAME";

    public GetFileDetails(String filenm, String server_name,SimpleDateFormat sdf_2,
            String batch, String report) {
        this.filenm = filenm;
        if (file2name_map == null) {
            file2name_map = new HashMap<>();
        }
        this.server_name= server_name;
        this.sdf_2=sdf_2;
       
        this.report=report;
        this.batch=batch;
//        if (sdf_2 == null) {
//            String DATE_FORMAT_1 = "yyyy-MM-dd HH:mm:ss";
//            sdf_2 = new java.text.SimpleDateFormat(DATE_FORMAT_1);
//            sdf_2.setTimeZone(TimeZone.getDefault());
//        }
        file2name_map.put(filenm, null);
    }

    @Override
    public void run() {
        getFileDetails_map(filenm);
    }

    public static boolean isFinished() {
        if (file2name_map == null || file2name_map.isEmpty()) {
            return true;
        } else {
            if (file2name_map.values().contains(null)) {
                return false;
            } else {
                return true;
            }
        }
    }

    public static HashMap<String, HashMap<String, String>> getDetailsMap() {
        while (!isFinished()) {  
        }
        return file2name_map;
    }

    public static HashMap<String, HashMap<String, String>> getcompletedDetailsap() {
        HashMap<String, HashMap<String, String>> newfile2hash_map = new HashMap<>();
        ArrayList<String> c_keys_l = new ArrayList<>(file2name_map.keySet());
        for (int i = 0; i < c_keys_l.size(); i++) {
            if (file2name_map.get(c_keys_l.get(i)) != null) {
                newfile2hash_map.put(c_keys_l.get(i), file2name_map.remove(c_keys_l.get(i)));
            }
        }
        return newfile2hash_map;
    }

    public static int getDetailsMap_size() {
        if (file2name_map == null) {
            return 0;
        }
        return file2name_map.size();
    }

    public static void clear() {
        if (file2name_map != null) {
            file2name_map.clear();
        }
    }

    /*
     MEthodID=GD1
     */
    private HashMap<String, String> getFileDetails_map(String filenm) {
        HashMap<String, String> details_map = new HashMap<>();
        
        try {
            Path f = Paths.get(filenm);
            if (Files.exists(f)) {
//                 Files.probeContentType(file)//get type
//                BasicFileAttributes attr = Files.readAttributes(f, BasicFileAttributes.class);
                String timestp = sdf_2.format(new Date(Files.getLastModifiedTime(f).toMillis()));
                details_map.put("FILES2PATH.LASTMODIFIED", timestp);
                details_map.put("FILES2PATH.OWNERGROUP", Files.getOwner(f).getName());
                details_map.put("FILES.FILESIZE", Files.size(f) + "");
//                if (server_name == null) {
//                    server_name = getServerName();
//                    if (server_name == null) {
//                        server_name = "localhost";
//                    }
//                }
                details_map.put("HOSTNAME", server_name);//server_name
                details_map.put("FILES2PATH.LOCATION", server_name);//
                details_map.put("FILES2PATH.FILEPATH",f.toRealPath().toString());
                details_map.put("FILES.NAME", filenm);
                
                if(report!=null){
                    details_map.put(EGenVAR_CMD2.REPORT_NAME, report);
                }
                if(batch!=null){
                    details_map.put(EGenVAR_CMD2.REPORT_BATCH_NAME, batch);
                }
//                details_map.put(name_flag, "SCP:" + server_name + ":" + filenm);                
                file2name_map.put(filenm, details_map);
            }
        } catch (Exception ex) {
            file2name_map.remove(filenm);
            System.out.println("Error GD1a: " + ex.getMessage() + " " + filenm);
        }
        return details_map;
    }


}
