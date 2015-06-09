/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package no.ntnu.medisin.egenvar.cmd;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Formatter;
import java.util.HashMap;

//Use http://stackoverflow.com/questions/9321912/very-slow-to-generate-md5-for-large-file-using-java
/**
 * ulimit -n : fined open file limit
 *
 * @author sabryr
 */
public class GetChecksum implements Runnable {

    private String filenm;
    private long maxsize;
    private long minsize;
    public static HashMap<String, String> file2hash_map;
    private boolean skip_checksum;

    public GetChecksum(String filenm, long maxsize, long minsize, boolean skip_checksum) {
        this.filenm = filenm;
        this.maxsize = maxsize;
        this.minsize = minsize;
        if (file2hash_map == null) {
            file2hash_map = new HashMap<>();
        }
        file2hash_map.put(filenm, null);
        this.skip_checksum = skip_checksum;
    }

    @Override
    public void run() {
        cmd();
    }

    public static boolean isFinished() {
        if (file2hash_map == null || file2hash_map.isEmpty()) {
            return true;
        } else {
            if (file2hash_map.values().contains(null)) {
                return false;
            } else {
                return true;
            }
        }
    }

    public static HashMap<String, String> getChecksumMap() {
        while (!isFinished()) {
        }
        return file2hash_map;
    }

    public static HashMap<String, String> getcompletedChecksumMap() {
        HashMap<String, String> newfile2hash_map = new HashMap<>();
        if (file2hash_map != null) {
            ArrayList<String> c_keys_l = new ArrayList<>(file2hash_map.keySet());
            for (int i = 0; i < c_keys_l.size(); i++) {
                if (file2hash_map.get(c_keys_l.get(i)) != null) {
                    newfile2hash_map.put(c_keys_l.get(i), file2hash_map.remove(c_keys_l.get(i)));
                }
            }
        }       
        return newfile2hash_map;
    }

    public static int getChecksumMap_size() {
        if (file2hash_map == null) {
            return 0;
        }
        return file2hash_map.size();
    }

    public static void clear() {
        if (file2hash_map != null) {
            file2hash_map.clear();
        }
    }
//    public static String getval() {
//        return checksum;
//    }

    /*
     *MethodId=GC1
     //     */
    private void cmd() {
        Path f = Paths.get(filenm);
        String checksum = "NA";
        long f_s = 0;
        if (!Files.isDirectory(f)) {
            try {
                f_s = Files.size(f);
            } catch (IOException ex) {
            }
            boolean slow = false;
            if ((maxsize < 0) || (f_s < maxsize) && (minsize < 0) || (f_s > minsize)) {
                if (f_s > EGenVAR_CMD2.warning_limit) {
                    long freemem = Runtime.getRuntime().freeMemory();
                    System.out.println("Large file added to queue, slow processing " + filenm + " : size=" + (f_s / 1048576) + " Mb (available memory=" + (freemem / 1048576) + " Mb)");
                    slow = true;
                }
                try {
                    if (skip_checksum) {
                        checksum = "xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx";
                    } else {
                        checksum = getHash(new FileInputStream(filenm));
                    }
                    if (slow) {
                        System.out.println(filenm + "..OK");
                    }
                } catch (java.lang.OutOfMemoryError | Exception ex) {
                }
            } else {
                System.out.println("Skipping  Large file (checksum will be 'NA'), " + filenm + " : " + (f_s / 1048576) + " Mb)");
            }
        }
        if (checksum != null && ((checksum.length() >= 32) || checksum.equals("NA"))) {
            file2hash_map.put(filenm, checksum);
        } else {
            file2hash_map.remove(filenm);
            System.out.println("Error GC1: Checksum failed for " + filenm + " " + checksum);
        }

    }

//     private String getHash(FileInputStream inSm) {//
//         return "d397b63b15b70ada398f20ee336c52ea04e2502e";
    //             
//     }
    private String getHash(FileInputStream inSm) {//
        String hash = null;
        try {
            if (inSm != null) {
                MessageDigest sha1 = MessageDigest.getInstance("SHA1");
                sha1.reset();
                FileChannel channel = inSm.getChannel();
                ByteBuffer buff = ByteBuffer.allocate(2048);
                long cc = 0;
                while (channel.read(buff) != -1) {
                    buff.flip();
                    sha1.update(buff);
                    buff.clear();
                    cc++;
                    if (cc > 0 && cc % 500000 == 0) {
                        System.out.print("¤");
                    }
                }
                byte[] hashValue = sha1.digest();
                Formatter formatter = new Formatter();
                for (byte b : hashValue) {
                    formatter.format("%02x", b);
                }
                channel.close();
                inSm.close();
                hash = formatter.toString();
            }
        } catch (NoSuchAlgorithmException ex) {
            System.out.println("Error 56 :" + ex.getMessage());

        } catch (IOException ex) {
            System.out.println("Error 57 :" + ex.getMessage());
            ex.printStackTrace();
        } finally {
            try {
                if (inSm != null) {
                    inSm.close();
                }
            } catch (IOException ex) {
                System.out.println("Error 58 :" + ex.getMessage());
            }

        }
        return hash;
    }
    //    private String getHash(byte[] pass_bytes) {
//        try {
//            if (pass_bytes != null) {
//                MessageDigest sha1 = MessageDigest.getInstance("SHA1");
//                sha1.reset();
//                sha1.update(pass_bytes);
//                byte[] pass_digest = sha1.digest();
//                Formatter formatter = new Formatter();
//                for (byte b : pass_digest) {
//                    formatter.format("%02x", b);
//                }
//                return formatter.toString();
//            }
//        } catch (NoSuchAlgorithmException ex) {
//            System.out.println("Error 56 :" + ex.getMessage());
//        }
//        return null;
//    }
//    /*
//     *MethodId=GC2
//     */
//    private String runCommnad(String... command_l) {
//        String result = null;
//        try {
//            Process output = new ProcessBuilder(command_l).start();
//            try {
//                output.waitFor();
//            } catch (InterruptedException ex) {
//            }
//            Scanner scan = new Scanner(output.getInputStream());
//            while (scan.hasNext()) {
//                result = scan.nextLine();
//            }
//            scan.close();
//
//            output.destroy();
//            if (result != null) {
//                return result.replaceAll("\"", "");
//            }
//        } catch (IOException ex) {
//            System.out.println("Error GC2a :" + ex.getMessage());
//        }
//        return null;
//    }
}
