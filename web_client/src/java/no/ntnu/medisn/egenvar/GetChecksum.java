package no.ntnu.medisn.egenvar;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
import com.jcraft.jsch.*;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.security.GeneralSecurityException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Formatter;
import java.util.HashMap;
import java.util.logging.Level;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.servlet.http.HttpSession;

/**
 *
 * @author sabryr
 */
public class GetChecksum extends Thread {

    private HashMap<String, String[]> file2hash_map;
    public final static String STILL_HASHING = "Still_calculating";
    public final static String INTERUPTED_HASHING = "calculation_interupted";
    public final static long MAX_IDLE_TIME = 30000;
    public boolean done = false;
    private String user;
    private String pass;
    private String site;
//    public static boolean cancel = false;
    public boolean cancel = false;
    private HttpSession session;
    public final static String CHECKSUM_USED = "sha1";
    private Connection ncon;

    public GetChecksum(Connection ncon, ArrayList<String> filelist_list, String site, String user, String pass, HttpSession session) {
        this.ncon = ncon;
        this.session = session;
        this.user = user;
        this.pass = pass;
        this.site = site;
        file2hash_map = new HashMap<>();
        for (int i = 0; i < filelist_list.size(); i++) {
            file2hash_map.put(filelist_list.get(i), new String[]{GetChecksum.STILL_HASHING, "FLASE", "FLASE"});
        }

    }

    public void cancel() {
        cancel = false;
    }

//    public void reset(ArrayList<String> filelist_list, String user, String pass, String site, HttpSession session) {
//        this.session = session;
//        this.user = user;
//        this.pass = pass;
//        this.site = site;
//        file2hash_map = new HashMap<>();
//        for (int i = 0; i < filelist_list.size(); i++) {
//            file2hash_map.put(filelist_list.get(i), GetChecksum.STILL_HASHING);
//        }
//    }
    public boolean isHshing() {
        if (file2hash_map == null || file2hash_map.isEmpty()) {
            return false;
        } else {
            ArrayList<String[]> tmp_l = new ArrayList(file2hash_map.values());
            boolean unfihashed_found = false;
            for (int i = 0; (i < tmp_l.size() && !unfihashed_found); i++) {
                if (tmp_l.get(i)[0].equals(GetChecksum.STILL_HASHING)) {
                    unfihashed_found = true;
                }
            }
            return unfihashed_found;
        }
    }

    @Override
    public void run() {
        cancel = false;
        calculateCheksum(ncon);
        done = true;
    }

    private void calculateCheksum(Connection c_con) {
        Statement st_1 = null;
        try {
            String sql_path = "SELECT files_id from " + Constants.get_correct_table_name(c_con, "files2path") + " where filepath=";
            String sql_checksum = "SELECT id from " + Constants.get_correct_table_name(c_con, "files") + " where CHECKSUM=";
            try {
                st_1 = ncon.createStatement();
            } catch (Exception ex) {
            }

            MessageDigest checksum = null;
            try {
                checksum = MessageDigest.getInstance(CHECKSUM_USED);
            } catch (NoSuchAlgorithmException ex) {
                ex.printStackTrace();
            }
            ChannelSftp channelSftp = getChannelSftp();
            if (checksum != null && channelSftp != null && !channelSftp.isClosed()) {
                ArrayList<String> filelist_list = new ArrayList<>(file2hash_map.keySet());
                for (int i = 0; (i < filelist_list.size() && !cancel); i++) {
                    try {
                        Object lastupdate = session.getAttribute(DirectoryLister2.LAST_ACTIVE_TIME_FALG);
                        if (lastupdate != null) {
                            long last_update_millisecond = new Long(lastupdate.toString());
                            long current_update_millisecond = Timing.getMilliseconds();
                            if ((current_update_millisecond - last_update_millisecond) > MAX_IDLE_TIME) {
                                cancel = true;
                            }
                        }
                    } catch (Exception ex) {
                    }
                    if (channelSftp == null || channelSftp.isClosed()) {
                        channelSftp = getChannelSftp();
                    }
                    String c_file = filelist_list.get(i);
                    if (file2hash_map != null && file2hash_map.containsKey(c_file) && file2hash_map.get(c_file)[0].equals(GetChecksum.STILL_HASHING)) {
                        int file_id_path = getFromDb(st_1, sql_path + "'" + c_file + "'");
                        int file_id_checksum = -4;
                        try {
                            SftpATTRS cAtibs = channelSftp.lstat(c_file);
                            if (cAtibs != null) {
                                if (file2hash_map.containsKey(filelist_list.get(i))) {
                                    if (cAtibs.isDir()) {
                                        file2hash_map.get(filelist_list.get(i))[0] = "Not a regular file";
                                    } else if (!cAtibs.getPermissionsString().matches(".+r.{2}.+")) {
                                        file2hash_map.get(filelist_list.get(i))[0] = "Read access denied";
                                    } else {
                                        try {
                                            String shadigist = getHash(channelSftp.get(c_file));
                                            file2hash_map.get(filelist_list.get(i))[0] = shadigist;
                                            file_id_checksum = getFromDb(st_1, sql_checksum + "'" + shadigist + "'");
                                        } catch (SftpException ex) {
                                            System.out.println("Error 12:" + ex.getMessage());
                                            cancel = true;
                                        } finally {
                                        }
                                    }
                                }
                            }
                        } catch (SftpException ex) {
                            System.out.println("Error 14" + ex.getMessage());
                        }
                        if (file_id_path < 0) {
                            if (file_id_path == -1) {
                                file2hash_map.get(filelist_list.get(i))[1] = "Not checked";
                            } else if (file_id_path == -2) {
                                file2hash_map.get(filelist_list.get(i))[1] = "Not checked, DB error";
                            } else if (file_id_path == -3) {
                                file2hash_map.get(filelist_list.get(i))[1] = "New";
                            } else {
                                file2hash_map.get(filelist_list.get(i))[1] = "Error";
                            }
                            if (file_id_checksum == -3) {
                                file2hash_map.get(filelist_list.get(i))[2] = "New";
                            } else if (file_id_checksum < 0) {
                                file2hash_map.get(filelist_list.get(i))[2] = "<a target=\"_blank\" href=\"" + Constants.getServerName(ncon) + "Search/SearchResults3?" + Constants.TABLE_TO_USE_FLAG + "files" + "=" + file_id_checksum + "\">Not checked</a>";
                            } else {
                                file2hash_map.get(filelist_list.get(i))[2] = "<a target=\"_blank\" href=\"" + Constants.getServerName(ncon) + "Search/SearchResults3?" + Constants.TABLE_TO_USE_FLAG + "files" + "=" + file_id_checksum + "\">Checksum found</a>";
                            }
                        } else {
                            file2hash_map.get(filelist_list.get(i))[1] = "<a target=\"_blank\" href=\"" + Constants.getServerName(ncon) + "Search/SearchResults3?" + Constants.TABLE_TO_USE_FLAG + "files2path" + "=" + file_id_path + "\">Path found</a>";
                            if (file_id_checksum == -3) {
                                file2hash_map.get(filelist_list.get(i))[2] = "New";
                            } else if (file_id_checksum < 0) {
                                file2hash_map.get(filelist_list.get(i))[2] = "<a target=\"_blank\" href=\"" + Constants.getServerName(ncon) + "Search/SearchResults3?" + Constants.TABLE_TO_USE_FLAG + "files" + "=" + file_id_checksum + "\">Not checked</a>";
                            } else {
                                file2hash_map.get(filelist_list.get(i))[2] = "<a target=\"_blank\" href=\"" + Constants.getServerName(ncon) + "Search/SearchResults3?" + Constants.TABLE_TO_USE_FLAG + "files" + "=" + file_id_checksum + "\">Checsum found</a>";
                            }
                        }
                    }

                }
            }
            try {
                if (channelSftp != null && channelSftp.isConnected()) {
                    channelSftp.disconnect();
                }
            } catch (Exception ex) {
                System.out.println("Error 15:" + ex.getMessage());
                cancel = true;
            }

            if (st_1 != null && !st_1.isClosed()) {
                st_1.close();
            }
        } catch (Exception ex) {
            System.out.println("Error 15:" + ex.getMessage());
            cancel = true;
        } finally {
            try {
                if (st_1 != null && !st_1.isClosed()) {
                    st_1.close();
                }
            } catch (SQLException ex) {
            }
        }
    }

    private int getFromDb(Statement st_1, String sql) {

        int file_id = -1;
        if (st_1 == null) {
            file_id = -2;
        } else {
            try {
                file_id = -3;
                ResultSet r_1 = st_1.executeQuery(sql);
                if (r_1.next()) {
                    file_id = r_1.getInt(1);
                }
                r_1.close();
            } catch (SQLException ex) {
            }
        }

        return file_id;
    }

    private String getHash(InputStream inSm) {
        try {
            if (inSm != null) {
                ReadableByteChannel rdc = Channels.newChannel(inSm);
                MessageDigest sha1 = MessageDigest.getInstance("SHA1");
                sha1.reset();
                ByteBuffer buff = ByteBuffer.allocate(2048);
                while (rdc.read(buff) != -1) {
                    buff.flip();
                    sha1.update(buff);
                    buff.clear();
                }
                byte[] hashValue = sha1.digest();
                Formatter formatter = new Formatter();
                for (byte b : hashValue) {
                    formatter.format("%02x", b);
                }
                return formatter.toString();
            }
        } catch (NoSuchAlgorithmException ex) {
            System.out.println("Error 56 :" + ex.getMessage());

        } catch (IOException ex) {
            System.out.println("Error 57 :" + ex.getMessage());
        }
        return null;
    }

    private String byteArray2Hex(byte[] hash) {
        Formatter formatter = new Formatter();
        for (byte b : hash) {
            formatter.format("%02x", b);
        }
        return formatter.toString();
    }

    public HashMap<String, String[]> getfile2Hash() {
        HashMap<String, String[]> file2hash_map_copy = new HashMap<>();
        if (file2hash_map != null && !file2hash_map.isEmpty()) {
            file2hash_map_copy.putAll(file2hash_map);
        }
        return file2hash_map_copy;
    }

    private ChannelSftp getChannelSftp() {
        ChannelSftp channelSftp;
        if (user != null && pass != null && site != null && !site.isEmpty()) {
            try {
                JSch jsch = new JSch();
                Session session_1 = jsch.getSession(user, site, 22);
                session_1.setPassword(decrypt(Logintodirectory.encript_password, pass));
                java.util.Properties config = new java.util.Properties();
                config.put("StrictHostKeyChecking", "no");
                session_1.setConfig(config);
                try {
                    session_1.connect(120000);
                } catch (JSchException ex) {
                    ex.printStackTrace();

                }
                if (session_1.isConnected()) {
                    Channel channel = session_1.openChannel("sftp");
                    channel.connect();
                    channelSftp = (ChannelSftp) channel;
                    return channelSftp;
                }
            } catch (JSchException ex) {
                ex.printStackTrace();
            }
        } else {
            System.out.println("User name not found");
        }
        return null;
    }

    public String decrypt(String in_key, String encrypted) {
        try {
            byte key[] = in_key.getBytes();
            DESKeySpec desKeySpec = new DESKeySpec(key);
            SecretKeyFactory keyFactory;
            keyFactory = SecretKeyFactory.getInstance("DES");
            SecretKey secretKey = keyFactory.generateSecret(desKeySpec);
            javax.crypto.Cipher cipher = javax.crypto.Cipher.getInstance("DES/ECB/PKCS5Padding");
            cipher.init(javax.crypto.Cipher.DECRYPT_MODE, secretKey);
            return new String(cipher.doFinal(encrypted.getBytes("ISO-8859-1")), "ISO-8859-1");
        } catch (NoSuchAlgorithmException ex) {
        } catch (InvalidKeyException ex) {
        } catch (InvalidKeySpecException ex) {
        } catch (NoSuchPaddingException ex) {
        } catch (GeneralSecurityException ex) {
        } catch (UnsupportedEncodingException ex) {
        }
        return encrypted;
    }

    public String toString() {
        return "All done=" + done;
    }
}
