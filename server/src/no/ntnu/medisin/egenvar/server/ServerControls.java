/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package no.ntnu.medisin.egenvar.server;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.KeyStore;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.Properties;
import javax.mail.Address;
import javax.mail.Flags;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Store;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import org.apache.derby.drda.NetworkServerControl;
import org.glassfish.embeddable.Deployer;
import org.glassfish.embeddable.GlassFish;

/**
 *
 * @author sabryr
 */
public class ServerControls implements Runnable {

    private GlassFish glassfish;
    private NetworkServerControl db_server;
    private String warsource;
//    private long start_time;
//    private long lifetime = 259200000; //3 days
    private static boolean ok;
    private Start_EgenVar1 caller;
    int count = 0;
//    private static String next_restart = "No scheduled restarts";
//    private static boolean reset;
//    private boolean overide_restart_schedule;
    private static Thread backup_t;
    private Path docroot;
    private static int mem_loop_c = 8760;
    public static int last_checked_hour = 0;
    public static int last_checked_minut = 0;
    public static int last_delete_code_refesh_hr = 0;
    public static int restart_mail_headsup = 5;
    private final long HOURS_IN_MIL_SEC = 18000000;
    private static GregorianCalendar midnight;
    private int midnight_hour = 23;//23
    private int midnight_min = 59;//59
    private int midnight_sec = 0;
    private int midnight_milsec = 0;

    public ServerControls(GlassFish glassfish, NetworkServerControl db_server,
            String warsource, Start_EgenVar1 caller,
            boolean overide_restart_schedule, Path docroot) {
        midnight = new GregorianCalendar();
        this.glassfish = glassfish;
        this.db_server = db_server;
        this.warsource = warsource;
        this.caller = caller;
//        reset = false;
//        this.overide_restart_schedule = overide_restart_schedule;
        this.docroot = docroot;

    }

    @Override
    public void run() {//new Integer(caller.instruct_map.get("mail.smtp.port")
//        if (type == 0) {
        commit5();
//        } 
//else if (type == 1) {
//            re_memeorize();
//        } else {
//            commit5();
//        }
    }

    /*
     * MethodId=SC1
     Check also for updates
     * 1. Re-deiply services /restart server if memory is too low
     * 2. Regualr restart at midnight everyday
     * 3. Regular backup midnigt everyday
     * 4. Delete old files midnight everyday
     *  // redeploy if records updated 
     */
    private void commit5() {
        midnight.set(Calendar.HOUR_OF_DAY, midnight_hour);
        midnight.set(Calendar.MINUTE, midnight_min);
        midnight.set(Calendar.SECOND, midnight_sec);
        midnight.set(Calendar.MILLISECOND, midnight_milsec);
        long sleep_time = 60000;
        boolean restarted = false;
        boolean restart_now = false;
        int cycles = 0;

        boolean regular_restarte_mail_sent = false;
        ok = true;
        while (ok) {
            monitorLockRemove();
            cycles++;
            String[] email_message = null;
            GregorianCalendar c_time = new GregorianCalendar();
            long milli_seconds_midnight = midnight.getTimeInMillis() - c_time.getTimeInMillis();
            if (!restarted && milli_seconds_midnight < 0) {
                midnight.set(Calendar.DAY_OF_YEAR, midnight.get(Calendar.DAY_OF_YEAR) + 1);
                restart_now = true;
                cleanOldFile();
                if (!Backup.auto_backup_schesuled) {
                    Backup new_backup = new Backup(caller.instruct_map, null, null, null, true, 2);
                    backup_t = new Thread(new_backup);
                    backup_t.start();
                }
            }
            long mem = Runtime.getRuntime().freeMemory();
            if (mem < Start_EgenVar1.MIN_MEM) {
                restart_now = true;
                email_message = new String[2];
                email_message[0] = "Urgent: EGDMS Server memory low,restarting";
                email_message[1] = "Restarting in 5 minutes. Current time " + c_time.getTime();
                sleep_time = 300000;
            } else if (!regular_restarte_mail_sent && (milli_seconds_midnight / HOURS_IN_MIL_SEC) < 1) {
                email_message = new String[2];
                email_message[0] = "EGDMS server restart required";
                email_message[1] = "The EGDMS server requires restart and an automatic restart is scheduled "
                        + " @" + midnight.getTime() + " +/- 10 minutes depending on the load "
                        + "\n The services will be unavailable for about 30 seconds during the restart";
                regular_restarte_mail_sent = true;
            }

            if (email_message != null) {
                if (caller.sendmail_quick(email_message[0], email_message[1])) {
                } else {
                    System.out.println("Error: Inform admin of system restart : failed");
                }
                email_message = null;
                regular_restarte_mail_sent = true;
                sleep_time = 60000;
            }
            try {
                if (restart_now) {
                    //send warnings to users
                }
                Thread.sleep(sleep_time);
            } catch (InterruptedException ex) {
                System.out.println("Warning EC1a: " + ex.getMessage());
            }
            int adminMailInstructs = readAdminMail();

            if (adminMailInstructs == 2) {
                stop();
                caller.stopController(glassfish, db_server);
                ok = false;
            } else if (restart_now || adminMailInstructs == 1) {
                diploy_services();
                caller.sync_with_central(false);
                System.out.println("137 restart");
                restart_now = false;
                restarted = true;
                midnight.set(Calendar.HOUR_OF_DAY, midnight_hour);
                midnight.set(Calendar.MINUTE, midnight_min);
                midnight.set(Calendar.SECOND, midnight_sec);
                midnight.set(Calendar.MILLISECOND, midnight_milsec);
            } else if ((cycles % 5 == 0)) {
                if (caller.resetConnectionIfUpdated()) {
                    System.out.println("188 Reload memory db");
                    restart_now = false;
                    restarted = true;
                }
            }
        }
    }

    private void monitorLockRemove() {
        if (!Files.exists(Paths.get(".egenvar_lok"))) {
            System.out.println("Lock removed, shutting down initiated");
            caller.stopController(glassfish, db_server);
        }
    }

    /*
     To restart = EGDMS_ADMIN|123|RESTART|0|0
     */
    private int readAdminMail() {
        int result = -1;
        if (Start_EgenVar1.adminbyemail_code > 0) {
            ArrayList<String[]> command_a = readMail(caller.instruct_map.get(caller.TRUST_STORE_FILE_NM), "changeit", caller.instruct_map.get("--mailhost"), new Integer(caller.instruct_map.get("mail.imap.port")),
                    caller.instruct_map.get("--mailuser"), caller.instruct_map.get("mail.smtp.password"));
            StringBuilder stb = new StringBuilder();

            for (int i = 0; (i < command_a.size() && result < 0); i++) {
                String[] c_a = command_a.get(i);
                if (c_a[1].matches("[0-9]+") && (new Integer(c_a[1])) == Start_EgenVar1.adminbyemail_code) {
                    if (c_a[2].equals("SET_USER_LEVEL")) {
                        if (caller.setUserLevel(c_a[4], c_a[3])) {
                            stb.append("User level  " + c_a[3] + " set for " + c_a[4]);
                        } else {
                            stb.append("Setting user level failed for " + c_a[4]);
                        }
                    } else if (c_a[2].equals("BLOCK_USER")) {
                        if (caller.deleteUserLevel(c_a[3])) {
                            stb.append("User " + c_a[3] + " blocked");
                        } else {
                            stb.append("Bloking failed for user " + c_a[3]);
                        }
                    } else if (c_a[2].equals("ACTIVATE_USER")) {
                        if (caller.activate_users(c_a[3])) {
                            stb.append("User " + c_a[3] + " activated");
                        } else {
                            stb.append("Activation failed for " + c_a[3]);
                        }
                    } else if (c_a[2].equals("RESTART")) {
                        result = 1;
                        stb.append("Restart request executed ");
                    } else if (c_a[2].equals("STOP")) {
                        result = 2;

                    }
                    if (stb.length() > 1) {
                        caller.sendmail_quick("Admin request via email executed : ", stb.toString());
                    }
                    if (result != 2) {
                        try {
                            Thread.sleep(10000);
                        } catch (InterruptedException ex) {
                            System.out.println("Warning EC1a: " + ex.getMessage());
                        }
                    }
                    System.out.println("Email request by admin : " + Arrays.deepToString(c_a));
                } else {
                    System.out.println("Warning ,Invalid email administrater request, wrong code=" + c_a[1]);
                }
            }
        }
        return result;
    }

    private void cleanOldFile() {
        if (Files.exists(docroot) && Files.isWritable(docroot)) {
            deleteTmpFiles(docroot);
        } else {
            System.out.println("Error: docroot inaccible " + docroot);
        }
        if (caller.instruct_map.containsKey(Start_EgenVar1.BAKSUP_INSTRUCT_FLAG)) {
            String[] instruct_a = caller.instruct_map.get(Start_EgenVar1.BAKSUP_INSTRUCT_FLAG).split("\\|");
            if (instruct_a[0].equalsIgnoreCase("CP")) {
                if (instruct_a.length > 1) {
                    Path bakup_loc = Paths.get(instruct_a[1]);
                    if (Files.exists(bakup_loc) && Files.isDirectory(bakup_loc)) {
                        deleteTmpFiles(bakup_loc);
                    }
                }
            }
        }
    }

    public static String getNextRestart() {
        return midnight.getTime().toString();
    }

    public static void stop() {
        ok = false;
        mem_loop_c = -1;
        if (backup_t != null) {
            backup_t.interrupt();
        }
    }

    public static void reseteDefault() {
        midnight.set(Calendar.DAY_OF_YEAR, ((new GregorianCalendar()).get(Calendar.DAY_OF_YEAR)));
    }

    public static void reset() {
        if (midnight.get(Calendar.DAY_OF_YEAR) - ((new GregorianCalendar()).get(Calendar.DAY_OF_YEAR)) < 30) {
            midnight.set(Calendar.DAY_OF_YEAR, midnight.get(Calendar.DAY_OF_YEAR) + 1);
        }
    }

    private boolean diploy_services() {
        System.out.println("265 rediploy @" + (new GregorianCalendar()).getTime());
        try {
            if (warsource != null) {
                Deployer deployer = glassfish.getDeployer();
                Path war_path = Paths.get(warsource);
                if (Files.exists(war_path) && Files.isDirectory(war_path)) {
                    System.out.println("Starting the diployment of web applications");
                    DirectoryStream<Path> ds = Files.newDirectoryStream(war_path, "*.war");
                    Iterator<Path> path_l = ds.iterator();
                    while (path_l.hasNext()) {
                        Path c_path = path_l.next();
                        String[] c_det = c_path.getFileName().toString().split("\\.");
                        System.out.println("Deploying " + c_path.toAbsolutePath());
                        try {
                            deployer.deploy(c_path.toAbsolutePath().toUri(), "--name=" + c_det[0], "--contextroot=" + c_det[0], "--force=true");
                        } catch (Exception ex) {
                            System.out.println("Error : " + ex.getMessage() + " \n Server restart required");
                        }
                    }
                }
            }
        } catch (Exception ex) {
            System.out.println("1328 " + ex.getMessage());
        }

        return true;
    }

    private void deleteTmpFiles(Path in_Path) {
        //also delete backup
//        if (count % 24 == 0) {
        long delete_limt = 604800000;
        DirectoryStream<Path> ds = null;
        GregorianCalendar c_cal = new GregorianCalendar();
        Long c_time = c_cal.getTimeInMillis();
        try {
            ds = Files.newDirectoryStream(in_Path);
            Iterator<Path> path_l = ds.iterator();
            int cc = 0;
            while (path_l.hasNext()) {
                Path c_file = path_l.next();
                if (Files.isRegularFile(c_file, LinkOption.NOFOLLOW_LINKS)) {
                    if (c_time - Files.getLastModifiedTime(c_file).toMillis() > delete_limt) {
                        cc++;
                        Files.delete(c_file);
                    }
                }
            }
            System.out.println(cc + " user generated Files older than " + Timing.convert(delete_limt) + " were deleted @" + Timing.getDateTime());
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
//        }
    }


    /*
     MethodID=16
     */
    private ArrayList<String[]> readMail(String trustore, String store_password,
            String host, int port, String user, String password) {
        ArrayList<String[]> command_a = new ArrayList<>();
//        System.out.println("350 \n\n\n\n\n\n");
//        System.out.println("553 trustore="+trustore+"\tstore_password="+store_password+"\thost="+host+"\t port="+ port+"\tuser"+ user);
        //                   
        boolean result = false;
//        HashMap<String, String> email_com_map = new HashMap<>();
        long yest_t = (new GregorianCalendar()).getTimeInMillis();
        yest_t = yest_t - 172800000;
        try {
            InitialContext ic = new InitialContext();
            Properties props = new Properties();
            props.put("mail.transport.protocol", caller.instruct_map.get("mail.transport.protocol"));
            props.put("mail.smtps.host", caller.instruct_map.get("mail.smtps.host"));
            props.put("mail.smtps.auth", caller.instruct_map.get("mail.smtps.auth"));
            props.put("mail.smtps.socketFactory", createSSLTestConfig(trustore, store_password));
            props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
            props.put("mail.smtp.debug", "true");
            props.put("mail.smtp.port", port);
            props.put("mail.smtp.socketFactory.fallback", "false");
            Session mailSession = Session.getDefaultInstance(props);
            Store store = mailSession.getStore("imaps");

            store.connect(host, port, user, password);
            Folder folder = store.getFolder("INBOX");
            folder.open(Folder.READ_WRITE);
            Message[] messages = folder.getMessages();
            for (int i = 0; i < messages.length; i++) {
                Message message = messages[i];
                String subject = message.getSubject();
//                System.out.println("857 subject=" + subject);
                if (subject.startsWith("EGDMS")) {
                    Address[] from_a = message.getFrom();
                    boolean from_admin = false;
                    for (int j = 0; (j < from_a.length && !from_admin); j++) {
                        if (from_a[j].toString().equalsIgnoreCase(caller.instruct_map.get("--fromaddress"))) {
                            from_admin = true;
                        }
                    }
                    boolean isseen = false;
                    if (from_admin) {
                        Flags.Flag[] sf = message.getFlags().getSystemFlags();
                        if (sf.length == 0) {
                        } else {
                            for (int k = 0; (k < sf.length && !isseen); k++) {
                                if (sf[k].equals(Flags.Flag.SEEN)) {
                                    isseen = true;
                                }
                            }
                        }
                    }
//                    System.out.println("878 isseen=" + isseen);
                    if (!isseen) {
                        message.setFlag(Flags.Flag.SEEN, true);
                        if (subject.equalsIgnoreCase("EGDMS server restart required")) {
                            if (message.getSentDate().getTime() < yest_t) {
                                message.setFlag(Flags.Flag.DELETED, true);
                            }
                        } else if (subject.startsWith("EGDMS_ADMIN")) {
                            message.setFlag(Flags.Flag.DELETED, true);
                            String[] c_command_a = subject.split("\\|");
                            if (c_command_a.length > 4) {
                                if (c_command_a[1] != null) {
                                    command_a.add(c_command_a);
                                }
                            }
                        }
                    }
                }
            }
            store.close();
        } catch (NamingException ex) {
            System.out.println("\nFaild to read mail. Error 16a: " + ex.getMessage() + "\n Try again in 5 minute. If still get this error recreate certificates "
                    + "(sh start.sh -fromlast -recreate_mail_certificates)");
            ProgressMonitor.cancel();
        } catch (Exception ex) {
            System.out.println("\nFaild to read mail. Error 16b: " + ex.getMessage() + "\n Try again in 5 minute. If still get this error recreate certificates "
                    + "(sh start.sh -fromlast -recreate_mail_certificates)");

            ex.printStackTrace();
        }
//        System.out.println("603 \n\n\n\n\n\n");
        return command_a;
    }

    private SSLSocketFactory createSSLTestConfig(String trustore, String password) {
        SSLSocketFactory sf = null;
        try {
            KeyStore trustStore = KeyStore.getInstance("JKS");
            trustStore.load(new FileInputStream(trustore), password.toCharArray());
            KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509");
            kmf.init(trustStore, password.toCharArray());
            TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance("PKIX");
            trustManagerFactory.init(trustStore);
            TrustManager[] trustManagers = trustManagerFactory.getTrustManagers();
            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, trustManagers, null);

            sf = sslContext.getSocketFactory();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return sf;
    }
}
