/*
 Sabry Razick
 * TODO 
 * .Mar 18, 2015 4:09:54 PM com.sun.grizzly.http.KeepAliveThreadAttachment timedOut
 WARNING: GRIZZLY0023: Interrupting idle Thread: http-thread-pool-8185(2).
 ..Mar 18, 2015 4:09:55 PM com.sun.grizzly.http.KeepAliveThreadAttachment timedOut
 WARNING: GRIZZLY0023: Interrupting idle Thread: http-thread-pool-8185(2).
 .Mar 18, 2015 4:09:56 PM com.sun.grizzly.http.KeepAliveThreadAttachment timedOut
 WARNING: GRIZZLY0023: Interrupting idle Thread: http-thread-pool-8185(2).
 ...
 Initiating...
 Saving changes to config..
 Is there any active searches=NO
 Is there any active transaction=NO
 Stoping, please wait ...
 Warning EC1a: sleep interrupted
 Warning SC24a:sleep interrupted
 Warning bk16: sleep interrupted
 Cleaning trace
 Backup completed @2015-03-18 16:09:57
 ......................................Mar 18, 2015 4:09:57 PM com.sun.grizzly.http.KeepAliveThreadAttachment timedOut
 WARNING: GRIZZLY0023: Interrupting idle Thread: http-thread-pool-8185(2).
 ............................................................................................................................................................Mar 18, 2015 4:09:59 PM com.sun.grizzly.http.KeepAliveThreadAttachment timedOut
 WARNING: GRIZZLY0023: Interrupting idle Thread: http-thread-pool-8185(2).
 ...................................................................................................................................................................................................................................................................................................Mar 18, 2015 4:10:00 PM com.sun.grizzly.http.KeepAliveThreadAttachment timedOut
 WARNING: GRIZZLY0023: Interrupting idle Thread: http-thread-pool-8185(2).
 ...........................................................................................................................................................................................................................................Mar 18, 2015 4:10:01 PM com.sun.grizzly.http.KeepAliveThreadAttachment timedOut
 WARNING: GRIZZLY0023: Interrupting idle Thread: http-thread-pool-8185(2).
 ....................................................................................
 * 
 * ps -ef | grep "Start_EgenVar1.jar" | grep -v "grep"  |awk '{print "kill -9" $2}' > .$(date +%Y%m%d)_egenvar_lok
 */
package no.ntnu.medisin.egenvar.server;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.Console;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.lang.management.ManagementFactory;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.NetworkInterface;
import java.net.ServerSocket;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.charset.Charset;
import java.nio.file.DirectoryStream;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.nio.file.attribute.PosixFilePermission;
import java.nio.file.attribute.PosixFilePermissions;
import java.security.InvalidKeyException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.security.spec.InvalidKeySpecException;
import java.sql.CallableStatement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
//import org.apache.derby.drda.NetworkServerControl;
import org.glassfish.embeddable.BootstrapProperties;
import org.glassfish.embeddable.CommandResult;
import org.glassfish.embeddable.CommandRunner;
import org.glassfish.embeddable.GlassFish;
import org.glassfish.embeddable.GlassFishException;
import org.glassfish.embeddable.GlassFishProperties;
import org.glassfish.embeddable.GlassFishRuntime;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Formatter;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.Scanner;
import java.util.Set;
import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLException;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

import javax.swing.JOptionPane;
import javax.xml.ws.BindingProvider;
import org.apache.derby.drda.NetworkServerControl;
import org.glassfish.embeddable.CommandResult.ExitStatus;

import org.glassfish.embeddable.Deployer;
//-XX:NewSize=256m -XX:MaxNewSize=356m -XX:PermSize=128m -XX:MaxPermSize=512m
//01234560

/**
 * Select * from Biological_Ontologies into outfile 'Biological_Ontologies'
 * FIELDS TERMINATED BY '||' LINES TERMINATED BY '\n' How to use dns lookup dig
 * +short -x 129.241.180.228 System.out.println("74 " +
 * java.net.InetAddress.getLocalHost().getHostName());
 *
 *
 * 1. Copy JSTL jar to lib folder of application server
 *
 * inmemory table : declare global temporary table session.t_preserve (id int)
 * on commit
 *
 * Todo: investigate other server instances
 *
 * @author sabryr
 */
//cd /home/sabryr/NetBeansProjects/Start_EgenVar1/
//mkdir /home/sabryr/NetBeansProjects/Start_EgenVar1/dist/data;/cp  /home/sabryr/NetBeansProjects/Start_EgenVar1/data/*  /home/sabryr/NetBeansProjects/Start_EgenVar1/dist/data/; cp /home/sabryr/NetBeansProjects/Start_EgenVar1/Dependencies/*  /home/sabryr/NetBeansProjects/Start_EgenVar1/dist/lib/;
//mkdir dist/war; cp /home/sabryr/NetBeansProjects/eGenVar_web/dist/eGenVar_web.war dist/war/;cp /home/sabryr/NetBeansProjects/eGenVar_WebService/dist/eGenVar_WebService.war dist/war/
//
//cp /home/sabryr/NetBeansProjects/egenv/dist/egenv_new.jar.gz /home/sabryr/NetBeansProjects/Start_EgenVar1/Dependencies/;
//cp /home/sabryr/NetBeansProjects/egenv/egenv.tar.gz /home/sabryr/NetBeansProjects/Start_EgenVar1/Dependencies/;
//cp /home/sabryr/NetBeansProjects/Start_EgenVar1/Dependencies/* /home/sabryr/NetBeansProjects/Start_EgenVar1/dist/lib/
public class Start_EgenVar1 {

    private String encript_password = "12345678";//01234560//10235460
    public final static long MIN_MEM = 67108864;
    public final static long MIN_MEM_MEMDB = 536870912;
    public static int adminbyemail_code = -1;
    private final String UPDATE_CHECK_URL = "http://tang.medisin.ntnu.no/~sabryr/server_version2.txt";
    private final String UPDATE_SERVER_URL = "UPDATE_SERVER=";
    private final String UPDATE_WEB_URL = "UPDATE_WEB=";
    private final String UPDATE_WEBSERVICE_URL = "UPDATE_WEBSER=";
    private final String UPDATE_CMD_URL = "UPDATE_CMD=";
    private final String SYS_MESSAGES = "SYS_MESSAGE=";
    private HashMap<String, String> updates_map;
    private final double SERVER_VERSION = 4.4440;
    private final String COMMAND_LINE_TOOL_VERSION = "4.000";
    private final String WEB_INTERFACE_VERSION = "4.000";
    private final String WEB_SERVICE_VERSION = "4.000";
    private final String MODE_FLAG = "MODE";
    private int current_mode = 0;
    private final String COMMAND_LINE_TOOL_VERSION_FLAG = "CMD_VERSION";
    private final String WEB_INTERFACE_VERSION_FLAG = "WEB_VERSION";
    private final String WEB_SERVICE_VERSION_FLAG = "WES_VERSION";
    private final String SERVER_VERSION_FLAG = "SERVER_VERSION";
//    private final String COMMAND_LINE_TOOL_VERSION_URL_FLAG = "CMD_VERSION_URL";
    public final static double REQUIRED_JAVA_VERSION = 1.7;
    private static final String GMAIL_CERTIGFICATE_REFRESHER = "gmail_certificate_refresh.sh";
    private static final int SMTP_HOST_PORT = 465;
    private static final int IMAP_HOST_PORT = 993;
    public static final int PROGRESS_MONT_INTERVAL = 3000;
    public HashMap<String, String> instruct_map;
    private Connection conn_users = null;
    private Connection conn = null;
    private Connection conn2 = null;
    private Connection conn_memory = null;
    private Connection conn_mysql = null;
    public static final String TAGSOURCE_FLAG = "_tagsource";
    public static final String TAGSOURCE_PATHCOL_FLAG = "path";
    public static final String LINEAGE_HASHCOL_FLAG = "lineage";
    public static final String TAGSOURCE_HASHCOL_FLAG = "hash";
    public static final String TAGSOURCE_URICOL_FLAG = "URI";
    public static final String DATABASE_NAME_USERS_FLAG = "DB_USERS";
    public static final String DATABASE_NAME_DATA_FLAG = "DB_DATAENTRY";
    private boolean new_tag_created = false;
//    public static final String DATABASE_NAME_DATA_MEM_FLAG = "DB_DATAENTRY_MEM";
    public static final String SERVER_SECURE_PORT_FLAG = "S_PORT";
    public static final String BAKSUP_INSTRUCT_FLAG = "BACKUP";
    public static final String ADMIN_EMAIL_FLAG  = "ADMIN_EMAIL";
    public static String SERVER_PORT_FLAG = "PORT";
    public static String derby_port_flag = "DB_PORT";
    public static String derby_port_MEM_flag = "DB_PORT_MEM";
    public static String unm_usermanage_flag = "USER_MANAGE_ACCOUNT_NAME";
    public static String unm_usermanage_pass_flag = "USER_MANAGE_ACCOUNT_PASSWORD";
    public static String unm_gendataentry_flag = "DATAENTRY_ACCOUNT_NAME";
    public static String unm_gendataentry_pass_flag = "DATAENTRY_ACCOUNT_PASSWORD";
    public static String unm_gendataview_flag = "DATAVIEW_ACCOUNT_NAME";
    public static String unm_gendataview_pass_flag = "DATAVIEW_ACCOUNT_PASSWORD";
    public static String unm_gendataUpdate_flag = "DATAUPDATE_ACCOUNT_NAME";
    public static String unm_gendataUpdate_pass_flag = "DATAVUPDATE_ACCOUNT_PASSWORD";
    public static String db_host_flag = "DBHOST";
    public static final String SERVER_INSTALATION_ROOT_FLAG = "SERVER_INSTALL_ROOT";
    public static final String SRC_ROOT_FLAG = "SRC_ROOT";
    public static final String SERVER_ROOT_FLAG = "SERVER_ROOT";
    public static final String SERVER_NAME_FLAG = "SERVER_NAME";
    public static final String S_SERVER_ROOT_FLAG = "S_SERVER_ROOT";
    public static final String S_SERVER_NAME_FLAG = "S_SERVER_NAME";
    public static final String SERVER_ID_FLAG = "SERVER_IDENTITY";
    public static final String WSDL_URL_FLAG = "WSDL";
    public static final String S_WSDL_URL_FLAG = "S_WSDL";
    public static final String DB_AUTHENTICATION_ALGORITHM_FLAG = "CREATE_DB";
    public static final String DB_AUTHENTICATION_ALGORITHM = "SHA-1";
    public static final String SERVER_IP_FLAG = "IP";
    public static final String DB_LOG_FILENM_FLAG = "DB_LOG";
    public static final String CREATE_DB_FILE_DERBY_FLAG = "FILE_DB_CREATE_DERBY";
    public static final String CREATE_DB_FILE_DERBY_DEFAULT = "data/derby_create.sql";
    public static final String CREATE_TABLE_FROM_FILE_FLAG = "FILE_SINGLE_TABLE_CREATE";
    public static final String DATASOURCECLASSNAME_FLAG = "DATASOURCECLASSNAME";
    public static final String DRIVERCLASSNAME_FLAG = "DRIVERCLASSNAME";
    public static final String SOURCE_LIB_DIR_FLAG = "SOURCE_LIB";
    public static final String SOURCE_SYNC_DIR_FLAG = "SOURCE_SYNC";
    public static final String SOURCE_DATA_DIR_FLAG = "SOURCE_DATA";
    public static final String SOURCE_WAR_DIR_FLAG = "SOURCE_WAR";
    public static final String SERVER_LIB_DIR_FLAG = "SERVER__LIB";
    public static final String SERVER_DATA_DIR_FLAG = "SERVER_DATA";
    public static final String SERVER_CONF_DIR_FLAG = "SERVER_CONFIG";
    public static final String SERVER_DOCROOT_DIR_FLAG = "SERVER_DOCROOT";
    public static final String SERVER_BASE_FLAG = "SERVER_BASE";
    public static final String SERVER_TITLE = "SERVER_TITLE";
    public static final String SERVER_METRO_DIR = "SERVER_METRO";
    public static final String JSON_LAST_UPDATED = "JSON_LAST_UPDATED";
    public static final String LAST_MEMORIZED = "LAST_MEMORIZED";
    public static final String MEMORIZING_REQUESTED = "MEMORIZING_REQUESTED";
    public static final String CMD_TAR_NAME = "egenv.tar.gz";
    public static final String CMD_ZIP_NAME = "egenv.zip";
    public static final String CMD_JAR_NAME = "egenv_new.jar";
    public static final String PROCEDURE_JAR_NAME = "Procedures_eGenv_JAVADB.jar";
    public static final String CMD_TAR_LOC_FLAG = "CMD_TAR";
    public static final String CMD_ZIP_LOC_FLAG = "CMD_ZIP";
    public static final String CMD_JAR_LOC_FLAG = "CMD_JAR";
    public static final String PROCEDURE_JAr_LOC_FLAG = "PRC_JAR";
    public static final String EGENR_FLAG = "eGenR.R";
    public static final String EGENPY_FLAG = "eGenPy.py";
    public static final String EGENR_LOC_FLAG = "EGENR";
    public static final String EGENPY_LOC_FLAG = "EGENPY";
    public static final String USE_LOCAL_HOST_FOR_MAIL = "USE_LOCALHOST";
    private final String SERVER_DOC_ROOT_FLAG = "DOC_ROOT";
    private String dbURL_users = null;
    public static String dbURL_dataEntry = null;
    private static String dbURL_dataEntry_memory = null;
    private static String dbURL_dataEntry_memory_drop = null;
    public String TRUST_STORE_FILE_NM = "TRUST_STORE";
//    private String dbURL_dataEntry_mysql = "jdbc:mysql://localhost:3306/" + database_name_dataEntry + ";user=egendataentry;password=k2prrr.N";
    private String dbURL_dataEntry_mysql = "jdbc:mysql://localhost:3306/tmp_test?user=java&password=123.E&generateSimpleParameterMetadata=true";
    private String login_config_info = "/*Copyright (c) 2004-2010 Oracle and/or its affiliates. All rights reserved.Content below may be bound by the License at https://glassfish.dev.java.net/public/CDDL+GPL_1_1.html*/\n"
            + "fileRealm {\n    com.sun.enterprise.security.auth.login.FileLoginModule required;\n};\n\n"
            + "ldapRealm {\n    com.sun.enterprise.security.auth.login.LDAPLoginModule required;\n};\n\n"
            + "solarisRealm {\n com.sun.enterprise.security.auth.login.SolarisLoginModule required;\n};\n\n"
            + "jdbcRealm {\n    com.sun.enterprise.security.auth.login.JDBCLoginModule required;\n};\n"
            + "jdbcDigestRealm {\n  com.sun.enterprise.security.auth.login.JDBCDigestLoginModule required;\n};\n"
            + "pamRealm {\n com.sun.enterprise.security.auth.login.PamLoginModule required;\n};";
    private String certificate_script = "keytool -genkey -alias s1as -keyalg RSA  -keypass changeit  -validity 365 -storepass changeit -keystore keystore.jks -dname \"cn=egenvar2, ou=NA, o=NA, c=NO\";\n"
            + "keytool -export -alias s1as -file server.cer  -keystore keystore.jks  -storepass changeit  ;\n"
            + "keytool -import -v -trustcacerts -alias s1as  -file server.cer -keystore cacerts.jks  -keypass changeit <<EOF\n"
            + "changeit\n"
            + "changeit\n"
            + "yes\n"
            + "EOF\n"
            + "keytool -genkey -alias glassfish-instance  -keyalg RSA  -keypass changeit  -validity 365 -storepass changeit -keystore keystore.jks -dname \"cn=egenvar3, ou=NA, o=NA, c=NO\";\n"
            + "keytool -export -alias glassfish-instance  -storepass changeit  -file server.cer  -keystore keystore.jks;\n"
            + "keytool -import -v -trustcacerts -alias glassfish-instance  -file server.cer -keystore cacerts.jks  -keypass changeit<<EOF\n"
            + "changeit\n"
            + "yes\n"
            + "EOF";
    private String gmail_certificate_script = "echo |openssl s_client  -CApath . -connect smtp.gmail.com:465 > tmp.txt;\n"
            + "sed -n \"/-----BEGIN CERTIFICATE-----/,/-----END CERTIFICATE-----/p\" < tmp.txt > gmail.txt;\n"
            + "keytool -delete -alias smtp.gmail.com -keystore cacerts.jks -storepass changeit <<EOF\n"
            + "changeit\n"
            + "EOF\n"
            + "keytool -import -alias smtp.gmail.com -keystore cacerts.jks -file gmail.txt<<EOF\n"
            + "changeit\n"
            + "yes\n"
            + "EOF";
    private final String DERBY_CONNECTION_PARAM = "DERBY_CONNECTION_PARAM";
    private final static String MAIL_SOURCE_NAME = "mail/egenvar";
    private String[] create_mailer_a;
    private final String CONFIG_FILE_FLAG = "CONFIG";
    public final static String UNIQUE_TO_USER_COLUMNS_FLAG = "UNIQUE_COLMNS";
    public final static String ALL_INDEX_NAMES_FLAG = "ALL_INDICES";
    public final static String FOREIGN_TABLE_FLAG = "FOREIGN_TABLES";
    public final static String FOREIGN_KEY_COLUMNS_FLAG = "FOREIGN_COLUMNS";
    public final static String FOREIGN_KEY_NAMES_FLAG = "FOREIGN_KES";
    public final static String ALL_COLUMNS_FLAG = "ALL_COLUMNS";
    public final static String UNIQUE_ID_INDEX_NAME_PREFIX = "UNIQUE_IDENTIFICATION";
    public final static String SUPER_PARENT_REF_FLAG = "super_parent_ref";
    private HashMap<String, HashMap<String, String[]>> key_constraint_map;
    private final String REPLACEWITH_ID_FALG = "_000_";
    public static final String TABLE_TO_USE_FLAG = "TABLETOUSE_";
    public static final String QUERY_TO_USE_FLAG = "QUERYTOUSE_";
    private HashMap<String, ArrayList<String>> table2Columns_map;
    private final String LINK_TO_FEATURE_ID_FLAG = "LINK_TO_FEATURE_ID";
    private final static String LAST_CONFIG_DETAILS_FILE_FLAG = ".last_config.enc";
    private Thread server_control_thread;
//    private Thread server_control_thread2;
//    private Thread sync_thread;
    public static final String ID_COLUMN_COMMENT_FLAG = "****";
    public final static String ID_COLUMN_FALG = "ID";
    public static int max_before_spliting = 100000;
    public static final String RELATIONSHIP_TYPE_FLAG = "relationship_type=";
    public static final String EGENVAR_UNDO_FILENM = "_egenvundo.txt";
    public static int split_limit = 500;
    public static final String SYNC_REMOTE_FLAG = "SYNC_REMOTE";
    private static Path current_config_file;
    public final static String IN_ID_FALG = "_IN_";
    private static boolean recreate_tag_source_paths = false;
    public final static String UNIQUE_TO_COLUMNS_FLAG = "UNIQUE_COLMNS";
    public static boolean tagsource_refreshing = false;
    public static boolean verbose = false;
    public static String delete_request_code = "-1|0";
    public final static String DELETE_REQUEST_CODE_FLAG = "DELETE_REQUEST_CODE";
    private Syc_manager syc_manager;
    private boolean server_active;
//    private Syc_manager syc_manager;
//    private HashMap<String, Integer> column2type_map;
//    private ArrayList<String> column2autoincrement_l;
//    private ArrayList<String> all_table_l;
////    private String DATABASE_NAME_DATA = "EGEN_DATAENTRY";

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
//         Files.deleteIfExists(Paths.get(".egenvar_lok"));
        if (Files.exists(Paths.get(".egenvar_lok"))) {
            System.out.println("Error: 1. Another instance of the server is running. Stop this before starting."
                    + "\n2. If you can not stop it as you have lost the terminal then try sending a email to the server"
                    + "requesting a stop. "
                    + "\n3. If everything else fails delete the file " + Paths.get(".egenvar_lok").toAbsolutePath() + " "
                    + "\n and wait 10 minutes. "
                    + "\n As a final resort kill the process with the process id given below.");
            Path kp = Paths.get(".egenvar_lok");
            if (Files.isRegularFile(kp) && Files.isReadable(kp)) {
                Charset charset = Charset.forName("UTF-8");
                try (BufferedReader reader = Files.newBufferedReader(kp, charset)) {
                    int c = 0;
                    String line = null;
                    while ((line = reader.readLine()) != null) {
                        line = line.trim();
                        System.out.println(" " + line);
                    }
                } catch (IOException x) {
                    System.err.format("IOException: %s%n", x);
                }
            }
        } else {
            Timing.setPointer();
            Start_EgenVar1 main = new Start_EgenVar1();
            String classpath = System.getProperty("java.class.path");
            if (classpath != null && !classpath.isEmpty() && classpath.endsWith("jar")) {
                try {
                    File path_file = new File(classpath);
                    String source_dir = path_file.getAbsoluteFile().getParent() + File.separatorChar;
                    String key_file_name = source_dir + "key.file";
                    Path kp = Paths.get(key_file_name);
                    if (Files.isRegularFile(kp) && Files.isReadable(kp)) {
                        Charset charset = Charset.forName("UTF-8");
                        try (BufferedReader reader = Files.newBufferedReader(kp, charset)) {
                            int c = 0;
                            String line = null;
                            String new_key = null;
                            while ((line = reader.readLine()) != null) {
                                line = line.trim();
                                if (!line.startsWith("#")) {
                                    new_key = line;
                                }
                            }
                            if (new_key.length() == 8) {//new_key.matches("[0-9]+") && 
                                main.encript_password = new_key;
                            } else {
                                System.out.println("Error: Key file " + key_file_name + " found. But the provided key " + new_key + " not of valid format.\nThe length should be exactly 8. e.g. if you want to use 246, then include 00000246 in the file");
                                System.exit(883);
                            }
                        } catch (IOException x) {
                            System.err.format("IOException: %s%n", x);
                        }
                    }
                } catch (Exception ex) {
                    System.out.println("Warning: " + ex.getMessage());
                }
            } else {
                System.out.println("Error: failed to resolve classpath");
            }

            //TpDo: create a unique encription key getDecriptKey()
            boolean forcelocalhost = false;
            boolean reset_forcelocalhost = false;

            ArrayList<String> arg_l = new ArrayList<>(1);
            if (args != null) {
                arg_l = new ArrayList<>(Arrays.asList(args));
            }
//       
            boolean no_mail = false;
            boolean withdefault = false;
            String config_file_loc = null;
            String log_level = null;
            boolean use_default = false;
            boolean find_free_port = false;
//        boolean securemail = false;
            boolean force = false;
            boolean restserver = false;
            boolean recreate_Json = true;
            boolean overide_restart_schedule = false;
            boolean recreate_mail_cert = false;
            boolean recreate_serverl_cert = false;
            boolean recreate_tag_dict = false;
            HashMap<String, String> overide_config_map = new HashMap<>();
            //LAST_CONFIG_DETAILS_FILE_FLAG
            if (arg_l.remove("-nomail")) {
                no_mail = true;
            }
            if (arg_l.remove("-localhost")) {
                forcelocalhost = true;
            }
            if (arg_l.remove("-recreate_tag_dictionary")) {
                recreate_tag_dict = true;
            }
            if (arg_l.remove("-resethost")) {
                restserver = true;
            }
            if (arg_l.remove("-recreate_mail_certificates")) {
                recreate_mail_cert = true;
            }
            if (arg_l.remove("-recreate_server_certificates")) {
                recreate_serverl_cert = true;
            }
            if (arg_l.remove("-find_free_port")) {
                find_free_port = true;
            }
            if (arg_l.remove("-resetlocalhost")) {
                reset_forcelocalhost = true;
            }
            if (arg_l.remove("-force")) {
                force = true;
            }
            if (arg_l.remove("-nojson")) {
                recreate_Json = false;
            }
            if (arg_l.remove("-overidrestart")) {
                overide_restart_schedule = true;
                System.out.println("Overiding auto restart");
            }
            if (arg_l.remove("-reset_tag_paths")) {
                recreate_tag_source_paths = true;
                System.out.println("Resetting tag paths, requested");
            }


            for (int i = 0; i < arg_l.size(); i++) {
                if (arg_l.get(i).startsWith("--") && arg_l.get(i).contains("=")) {
                    String[] split_a = arg_l.get(i).split("=");
                    overide_config_map.put(split_a[0].replace("--", "").toUpperCase(), split_a[1]);
                }
            }
            if (arg_l.remove("-hack")) {
                hackability_test();
            } else if (arg_l.remove("-update") || arg_l.remove("-upgrade")) {
                main.updateSoftware(force);
            } else if (arg_l.remove("-h") || arg_l.remove("-help")) {
                System.out.println("-d:use defaults\n"
                        + "-nomail: configure email but do not test it\n"
                        + "-v: verbose mode\n"
                        + "-recover: password recovery"
                        + "-version: get software versions"
                        + "-stop:stop the server");

            } else if (arg_l.remove("-test")) {
                main.mailTest(forcelocalhost, reset_forcelocalhost,
                        find_free_port, restserver, recreate_mail_cert, recreate_serverl_cert);
            } else if (arg_l.remove("-resetkey")) {
                main.changeEncriptionKey();
            } else if (arg_l.remove("-version")) {
                System.out.println("Checking for updates...");
                (new Thread(new ProgressMonitor(PROGRESS_MONT_INTERVAL, "15"))).start();
                double latest = main.checkUpdates();
                System.out.println("\n\nServer version " + main.SERVER_VERSION + "| Latest version available=" + latest);
                System.out.println("Web inrterface version " + main.WEB_INTERFACE_VERSION);
                System.out.println("Webservices version " + main.WEB_SERVICE_VERSION);
                System.out.println("Command line tool version " + main.COMMAND_LINE_TOOL_VERSION + "\n\n");
                if (latest > main.SERVER_VERSION) {
                    System.out.println("Updates available");
                } else {
                    System.out.println("You are using the latest version");
                }
                ProgressMonitor.cancel();
            } else if (arg_l.remove("-stop")) {
                if (arg_l.isEmpty()) {
                    System.out.println("Error: process id missing Usage  Start_EgenVar1 -stop <fpid>. E.g. Start_EgenVar1 -stop 7745@myhost.com");
                } else {
                    String pid = arg_l.get(0);
                    String[] spl_a = pid.split("@");
                    System.out.println("Error, stopping failed use kill -9 " + spl_a[0] + " to kill the main process");
                }
            } else if (arg_l.remove("-recover")) {
                System.out.println("Recovery mode");
                Console cons = System.console();
                if (cons != null) {
                    System.out.println("Please enter encription key (8 characters)");
                    String inst = cons.readLine();
                    HashMap<String, String> config_map = main.readFromFileEncripted(LAST_CONFIG_DETAILS_FILE_FLAG, inst);
                    ArrayList<String> conf_l = new ArrayList<>(config_map.keySet());
                    for (int i = 0; i < conf_l.size(); i++) {
                        System.out.println(conf_l.get(i) + "=" + config_map.get(conf_l.get(i)));
                    }
                } else {
                    System.out.println("Error: Failed to invoke console");
                }
            } else if (arg_l.remove("-fromlast")) {
                if (arg_l.remove("-v")) {
                    verbose = true;
                }
                Path conf_path = Paths.get(LAST_CONFIG_DETAILS_FILE_FLAG);
                if (Files.isRegularFile(conf_path, LinkOption.NOFOLLOW_LINKS) && Files.isReadable(conf_path)) {
                    current_config_file = conf_path;
                    if (main.init(config_file_loc, true, no_mail, forcelocalhost,
                            reset_forcelocalhost, find_free_port, restserver, recreate_mail_cert, recreate_serverl_cert, overide_config_map)) {
                        main.commit(log_level, true, forcelocalhost, recreate_Json, overide_restart_schedule, recreate_tag_dict);
                        ProgressMonitor.cancel();
                    } else {
                        System.out.println("Error: init failed");
                    }
                } else {
                    System.out.println("Error 0a: cannot locate or file canot be read : " + LAST_CONFIG_DETAILS_FILE_FLAG);
                }
            } else {
                if (arg_l.remove("-v")) {
                    verbose = true;
                }
                if (arg_l.remove("-d")) {
                    withdefault = true;
                }
                if (arg_l.contains("-c")) {
                    if (arg_l.size() > arg_l.indexOf("-c") + 1) {
                        config_file_loc = arg_l.remove(arg_l.indexOf("-c") + 1);
                        arg_l.remove("-c");
                    } else {
                        System.out.println("Error: Config file missing. You should provide the full path for the config file after the -c flag");
                    }
                }

                if (arg_l.contains("-l")) {
                    if (arg_l.size() > arg_l.indexOf("-l") + 1) {
                        log_level = arg_l.remove(arg_l.indexOf("-l") + 1);
                        arg_l.remove("-l");
                    } else {
                        System.out.println("Error: Config file missing. You should provide the full path for the config file after the -c flag");
                    }
                }


                if (config_file_loc == null && arg_l.isEmpty() && !withdefault) {
                    String ans = main.getuserInputSameLine("No config file specified, Do you want to use default configuration options. The options used including passwords "
                            + "will be emailed to you at the end of the instalation", "[Y :yes use default configurations| N :exit and specify a configuration file]");
                    if (main.analyseUserResponse(ans, true, false, null) == 0) {
                        use_default = true;
                    }
                } else if (!arg_l.isEmpty()) {
                    config_file_loc = arg_l.remove(0).trim();
                }
                if (main.init(config_file_loc, false, no_mail, forcelocalhost,
                        reset_forcelocalhost, find_free_port, restserver, recreate_mail_cert, recreate_serverl_cert, overide_config_map)) {
                    main.commit(log_level, no_mail, forcelocalhost, recreate_Json, overide_restart_schedule, recreate_tag_dict);
                    ProgressMonitor.cancel();
                } else {
                    System.out.println("Error 2: init failed");
                }
            }
        }
    }

    private static boolean hackability_test() {
        int start = 10000000;
        boolean ok = false;
        String file_nm = LAST_CONFIG_DETAILS_FILE_FLAG;
        while (!ok) {
            try {
                byte key[] = (start + "").getBytes();
                DESKeySpec desKeySpec = new DESKeySpec(key);
                SecretKeyFactory keyFactory;
                keyFactory = SecretKeyFactory.getInstance("DES");
                SecretKey secretKey = keyFactory.generateSecret(desKeySpec);
                Cipher desCipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
                desCipher.init(Cipher.DECRYPT_MODE, secretKey);
                FileInputStream fis = new FileInputStream(file_nm);
                BufferedInputStream bis = new BufferedInputStream(fis);
                CipherInputStream cis = new CipherInputStream(bis, desCipher);
                ObjectInputStream ois = new ObjectInputStream(cis);
                Object tmp_ob = ois.readObject();
                if (tmp_ob != null && tmp_ob instanceof HashMap) {
                    System.out.println("\n469 " + start);
                    ok = true;
                }
            } catch (NoSuchAlgorithmException | InvalidKeyException | InvalidKeySpecException | NoSuchPaddingException | ClassNotFoundException ex) {
            } catch (FileNotFoundException ex) {
            } catch (IOException ex) {
            }
            start++;
            if (start % 1000000 == 0) {
                System.out.print(".");
            }
        }
        return false;
    }

    private void status(GlassFish glassfish, NetworkServerControl db_server,
            boolean overide_restart_schedule, boolean recreate_Json) {

        configure_glassfish(glassfish);
        String interface_details = "______________________\n\negenvar server started ports used " + SERVER_SECURE_PORT_FLAG + "=" + instruct_map.get(SERVER_SECURE_PORT_FLAG)
                + "|" + SERVER_PORT_FLAG + "=" + instruct_map.get(SERVER_PORT_FLAG)
                + "|" + derby_port_flag + "=" + instruct_map.get(derby_port_flag) + ""
                + "|" + derby_port_MEM_flag + "=" + instruct_map.get(derby_port_MEM_flag) + ""
                + "\n\nWeb interface:\n\t" + instruct_map.get(S_SERVER_NAME_FLAG) + ""
                + "\nWSDL for webservices:\n\t" + instruct_map.get(WSDL_URL_FLAG) + ""
                + "\nTo get the egenv tool:\n\twget  " + instruct_map.get(CMD_TAR_LOC_FLAG) + ""
                + "\nTo get the R cleient tool:\n\twget  " + instruct_map.get(EGENR_LOC_FLAG) + ""
                + "\nTo get the Python cleint :\n\twget  " + instruct_map.get(EGENPY_LOC_FLAG) + ""
                + "\nto stop the server from other terminals:\n\tStart_EgenVar -stop " + ManagementFactory.getRuntimeMXBean().getName() + "\n";
        FileWriter out;
        try {
            out = new FileWriter(".egenvar_lok");
            out.append(ManagementFactory.getRuntimeMXBean().getName());
            out.append("\n" + Timing.getDateTime());
            out.close();
        } catch (IOException ex) {
            System.out.println("Error saving lok falied ");
        }
        System.out.println(interface_details);

        try {
            System.out.println(glassfish.getStatus());
        } catch (GlassFishException ex) {
            ex.printStackTrace();
        }

        try {
            System.out.println("Optimising connections");
            if (getConnection() != null && !getConnection().isClosed()) {
                getConnection().close();
            }
            if (getConnection_memory(false, 1) != null && !getConnection_memory(false, 1).isClosed()) {
                getConnection_memory(false, 1).close();
            }
            if (conn_users != null && !conn_users.isClosed()) {
                conn_users.close();
            }
        } catch (Exception rx) {
            rx.printStackTrace();
        }

        ProgressMonitor.cancel();

        System.out.println("Server is ready. (Time taken: " + Timing.convert(Timing.getFromlastPointer()) + ")");
        Timing.setPointer();
        setdelete_request_code();
        server_control_thread = new Thread(new ServerControls(glassfish, db_server,
                instruct_map.get(SOURCE_WAR_DIR_FLAG), this,
                overide_restart_schedule, Paths.get(instruct_map.get(SERVER_DOCROOT_DIR_FLAG))));
//        (new Thread(new ServerControls(glassfish, instruct_map.get(SOURCE_WAR_DIR_FLAG), this))).start();
        server_control_thread.start();
//        server_control_thread2 = new Thread(new ServerControls(this, overide_restart_schedule));
//        (new Thread(new ServerControls(glassfish, instruct_map.get(SOURCE_WAR_DIR_FLAG), this))).start();
//        server_control_thread2.start();
        if (recreate_Json) {
            System.out.println("\nUpdating duplicate details report.. ");
            Reports reports = new Reports(get_correct_table_name(getConnection(),
                    "files", instruct_map.get(DATABASE_NAME_DATA_FLAG), 100),
                    get_correct_table_name(getConnection(), "files2path",
                    instruct_map.get(DATABASE_NAME_DATA_FLAG), 200),
                    dbURL_dataEntry, instruct_map.get(SERVER_DOCROOT_DIR_FLAG), instruct_map.get(S_SERVER_NAME_FLAG));
            (new Thread(reports)).start();
            makeJsonTbl(getConnection_memory(false, 2), instruct_map.get(DATABASE_NAME_DATA_FLAG));
        }
//        sync_with_central(false);
//        System.out.println("563 Auto sync blocked");
//        boolean restart = false;
        double latest_version = checkUpdates();
        if (SERVER_VERSION < latest_version) {
            System.out.println("\n\n Updates are avaialalbe. Your version=" + SERVER_VERSION + "| Latest version=" + latest_version + " \nVisit http://bigr.medisin.ntnu.no/data/eGenVar/ for more details ");
        }
        server_active = true;
        while (server_active) {
            int upgradeDecideder = rand(1, 3);
            if (upgradeDecideder == 2) {
                latest_version = checkUpdates();
                if (SERVER_VERSION < latest_version) {
                    System.out.println("\n\n Updates are avaialalbe. Your version=" + SERVER_VERSION + "| Latest version=" + latest_version + " \nVisit http://bigr.medisin.ntnu.no/data/eGenVar/ for more details ");
                }
            }
            Console cons = System.console();
            if (cons != null) {
                System.out.print("\n(STOP -to stop server| M -more commands):");
                String inst = cons.readLine();
                if (inst.equalsIgnoreCase("stop")) {
//                    ServerControls.stop();
//                    if (server_control_thread != null && server_control_thread.isAlive()) {
//                        server_control_thread.interrupt();
//                    }
//                    if (server_control_thread2 != null && server_control_thread2.isAlive()) {
//                        server_control_thread2.interrupt();
//                    }
//                    if (sync_thread != null && sync_thread.isAlive()) {
//                        sync_thread.interrupt();
//                    }
//                    System.out.println("Stoping, please wait ...");
//                    try {
//                        Files.deleteIfExists(Paths.get("*trace"));
//                        try {
//                            DirectoryStream<Path> ds = Files.newDirectoryStream(FileSystems.getDefault().getPath("."));
//                            Iterator<Path> path_l = ds.iterator();
//                            System.out.println("Cleaning trace");
//                            int c = 0;
//                            while (path_l.hasNext()) {
//                                Path cp = path_l.next();
//                                if (cp.getFileName().toString().toLowerCase().endsWith(".trace")) {
//                                    if (c % 10 == 0) {
//                                        System.out.print(".");
//                                    }
//                                    Files.deleteIfExists(cp);
//                                }
//                                c++;
//                            }
//                            System.out.println("Cleaning trace -- Done");
//                            ds.close();
//                        } catch (IOException e) {
//                            e.printStackTrace();
//                        }
//                        glassfish.stop();
//                        db_server.shutdown();
                    stopController(glassfish, db_server);
                    server_active = false;
//                    } catch (Exception ex) {
//                        ex.printStackTrace();
//                    }
                } else if (inst.equalsIgnoreCase("m")) {
                    try {
                        ArrayList<String> cmd_l = new ArrayList<>(10);
                        cmd_l.add("Stop server");//0
                        cmd_l.add("Get status messages");//1
                        cmd_l.add("User management");//2
                        cmd_l.add("Server controls and backup management");//3               
                        cmd_l.add("Database operations");//4            
                        cmd_l.add("Version");//5
                        cmd_l.add("Refresh JSON");//6
                        cmd_l.add("Get interface details (server address, WSDL etc..");//7  get interface detila
                        cmd_l.add("Refresh_connection_resources");//8 //
                        cmd_l.add("Activate administer by email");//8 //        
                        cmd_l.add("Exit this menu");//9
//                        cmd_l.add("Test");//22//  test

                        int ans = getUserChoice(cmd_l, "Select a function");
                        switch (ans) {
                            case 0: {
                                stopController(glassfish, db_server);
//                                ServerControls.stop();
//                                if (server_control_thread != null && server_control_thread.isAlive()) {
//                                    server_control_thread.interrupt();
//                                }
//                                if (server_control_thread2 != null && server_control_thread2.isAlive()) {
//                                    server_control_thread2.interrupt();
//                                }
//                                if (sync_thread != null && sync_thread.isAlive()) {
//                                    sync_thread.interrupt();
//                                }
//                                System.out.println("Stoping, please wait ...");
//                                try {
////                                    glassfish.stop();
////                                    db_server.shutdown();
//                                    stopController(glassfish, db_server);
                                server_active = false;
//                                } catch (Exception ex) {
//                                    ex.printStackTrace();
//                                }
                            }
                            break;
                            case 1: {
                                ArrayList<String> sub_cmd_l = new ArrayList<>();
                                sub_cmd_l.add("Get uptime");//1
                                sub_cmd_l.add("Get database status");//2
                                sub_cmd_l.add("Get application server status");//3
                                sub_cmd_l.add("Get egenvar status");//4
                                sub_cmd_l.add("List current transactions");//5
                                sub_cmd_l.add("Exit menu");
                                int sub_ans = getUserChoice(sub_cmd_l, "Select a function");
                                if (sub_ans == 0) {
                                    System.out.println(Timing.convert(Timing.getFromlastPointer()));
                                } else if (sub_ans == 1) {
                                    try {
                                        System.out.println("Oracle JAVA DB (Apache Derby)\n" + db_server.getSysinfo());
                                    } catch (Exception ex) {
                                        ex.printStackTrace();
                                    }
                                    System.out.println("______________________\negenvar server started ports used " + SERVER_SECURE_PORT_FLAG + "=" + instruct_map.get(SERVER_SECURE_PORT_FLAG) + "|"
                                            + SERVER_PORT_FLAG + "=" + instruct_map.get(SERVER_PORT_FLAG) + "|"
                                            + derby_port_flag + "=" + instruct_map.get(derby_port_flag)
                                            + derby_port_MEM_flag + "=" + instruct_map.get(derby_port_MEM_flag) + ""
                                            + "\n\nWeb interface: " + instruct_map.get(S_SERVER_NAME_FLAG) + ""
                                            + "\nWSDL for webservices: " + instruct_map.get(WSDL_URL_FLAG) + "\n"
                                            + "\nUp time " + Timing.convert(Timing.getFromlastPointer()) + "\n");
                                    try {
                                        System.out.println("Server glassfish embedded " + glassfish.getStatus());
                                    } catch (GlassFishException ex) {
                                        ex.printStackTrace();
                                    }
                                } else if (sub_ans == 2) {
                                    try {
                                        System.out.println(glassfish.getStatus());
                                    } catch (GlassFishException ex) {
                                        ex.printStackTrace();
                                    }
                                } else if (sub_ans == 3) {
                                    try {
                                        System.out.println(db_server.getRuntimeInfo());
                                    } catch (Exception ex) {
                                        ex.printStackTrace();
                                    }
                                } else if (sub_ans == 4) {
                                    try {
                                        ArrayList<String> re_l = executeQuery(getConnection_memory(false, 3), "SELECT * FROM SYSCS_DIAG.TRANSACTION_TABLE");

                                        for (int i = 0; i < re_l.size(); i++) {
                                            System.out.println(re_l.get(i));
                                        }
                                    } catch (Exception ex) {
                                        ex.printStackTrace();
                                    }
                                }
                            }
                            break;
                            case 2: {
                                ArrayList<String> sub_cmd_l = new ArrayList<>();
                                sub_cmd_l.add("Display user details");//0
                                sub_cmd_l.add("Add a new user");//1
                                sub_cmd_l.add("Set access level for users");//2
                                sub_cmd_l.add("Block a user");//3
                                sub_cmd_l.add("Delete a user");//4   
                                sub_cmd_l.add("List activated users");//5  
                                sub_cmd_l.add("List user accounts not yet activated");//6  
                                sub_cmd_l.add("Activate all users");//7
                                sub_cmd_l.add("Activate selected users");//8
                                sub_cmd_l.add("Exit menu");
                                int sub_ans = getUserChoice(sub_cmd_l, "Select a function");
                                if (sub_ans == 0) {
                                    System.out.print("\nList of user name (emails) of the user/s or * to get all (seperated by a ,):");
                                    String email = cons.readLine();
                                    if (email != null && !email.isEmpty()) {
                                        System.out.print("\nLevel (0-9):");
                                        String condition = "";
                                        if (email.equals("*")) {
                                            condition = "";
                                        } else {
                                            email = email.replaceAll(",", "','");
                                            email = "'" + email + "'";
                                            condition = " WHERE username in (" + email + ")";
                                        }
                                        displayUserDetails(" * ", condition);
                                    } else {
                                        System.out.println("Invalid email");
                                    }
                                } else if (sub_ans == 1) {
                                    int user_creat = createUser();
                                    if (user_creat >= 0) {
                                        System.out.println("User account created");
                                    } else {
                                        System.out.println("Error: User account creation failed");
                                    }
                                } else if (sub_ans == 2) {
                                    System.out.print("\nList of username(emails) of the user (seperated by a ,):");
                                    String email = cons.readLine();
                                    if (email != null && !email.isEmpty()) {
                                        System.out.print("\nLevel (0-9):");
                                        String level = cons.readLine();
                                        if (level.matches("[0-9]")) {
                                            if (setUserLevel(level, email)) {
                                                System.out.println("Level updated for " + email);
                                            } else {
                                                System.out.println("Error: update failed");
                                            }
                                        } else {
                                            System.out.println("Invalid level ");
                                        }
                                    } else {
                                        System.out.println("Invalid email");
                                    }
                                } else if (sub_ans == 3) {
                                    System.out.print("\nList of username(emails) of the user (seperated by a ,) to block:");
                                    String email = cons.readLine();
                                    if (email != null && !email.isEmpty()) {
                                        if (blockUser(email)) {
                                            System.out.println("Level updated for " + email);
                                        } else {
                                            System.out.println("Error: update failed");
                                        }
                                    } else {
                                        System.out.println("Invalid email");
                                    }
                                } else if (sub_ans == 4) {
                                    System.out.print("\nList of username(emails) of the user (seperated by a ,) to delete:");
                                    String email = cons.readLine();
                                    if (email != null && !email.isEmpty()) {
                                        if (deleteUserLevel(email)) {
                                            System.out.println("USer deleted " + email);
                                        } else {
                                            System.out.println("Error: update failed");
                                        }
                                    } else {
                                        System.out.println("Invalid email");
                                    }
                                } else if (sub_ans == 5) {
                                    System.out.print("Acitvated users:");
                                    String condition = " WHERE LEVEL<9";
                                    displayUserDetails(" USERNAME,VALIDATED,ALLOWED_GROUPS,LEVEL ", condition);
                                } else if (sub_ans == 6) {
                                    String condition = " WHERE LEVEL >=9";
                                    displayUserDetails(" USERNAME,VALIDATED,ALLOWED_GROUPS,LEVEL ", condition);
                                } else if (sub_ans == 7) {
                                    System.out.println("Result=" + activate_users(null));
                                } else if (sub_ans == 8) {
                                    System.out.print("\nList of username(emails) of the user (seperated by a ,) to activate:");
                                    String email = cons.readLine();
                                    if (email != null && !email.isEmpty()) {
                                        email = email.replaceAll(",", "','");
                                        email = "'" + email + "'";
                                        System.out.println("Result=" + activate_users(email));
                                    } else {
                                        System.out.println("Invalid email");
                                    }
                                }
                            }
                            break;
                            case 3: {
                                ArrayList<String> sub_cmd_l = new ArrayList<>();
                                sub_cmd_l.add("Restart now (Re-deploy services, reset maintenance schedules)");//0
                                sub_cmd_l.add("asadmin");//1
                                sub_cmd_l.add("view restart schedule");//2
                                sub_cmd_l.add("Reset restart schedule");//3
                                sub_cmd_l.add("Backup");//17
//                                sub_cmd_l.add("Selective Restore");//4
                                sub_cmd_l.add("Restore");//4
                                sub_cmd_l.add("Sync tags with other registered servers");//5
                                sub_cmd_l.add("Set/Reset verbose mode");//6

                                sub_cmd_l.add("Exit menu");
                                int sub_ans = getUserChoice(sub_cmd_l, "Select a function");
                                if (sub_ans == 0) {
                                    //cpnsider asadmin> restart-domain mydomain4
                                    System.out.println("Re-deploying initiating");//    
                                    diploy_services(glassfish);
                                    ServerControls.reseteDefault();
                                } else if (sub_ans == 1) {
                                    System.out.println("Enter command");
                                    String command = cons.readLine();
                                    if (command != null) {
                                        command = command.replaceAll("\\s+", "\\s");
                                        String[] split = command.split("\\s");
                                        if (split.length > 1) {
                                            String[] parm = new String[split.length - 1];
                                            for (int i = 1; i < split.length; i++) {
                                                parm[i - 1] = split[i];
                                            }
                                            System.out.println("635 " + Arrays.deepToString(parm));
                                            asadmin(glassfish, split[0], parm);
                                        } else {
                                            asadmin(glassfish, split[0], null);
                                        }
                                    }
                                } else if (sub_ans == 2) {
                                    System.out.println(ServerControls.getNextRestart());
                                } else if (sub_ans == 3) {
                                    System.out.println("Current schedule " + ServerControls.getNextRestart());
                                    ServerControls.reset();
                                    System.out.println("Reset complete. Next Restart@ " + ServerControls.getNextRestart());//              
                                } else if (sub_ans == 4) {
                                    backupcontroller();
                                } else if (sub_ans == 9) {
//                                    System.out.println("Please enter the encription key:");
//                                    String ck = cons.readLine();
//                                    if (ck.equals(encript_password)) {
//                                        if (getConnection() != null) {
//                                            Path indir = null;//Paths.get("/home/sabryr/tmp/rand/out");// null;
//                                            while (indir == null) {
//                                                System.out.print("\nBackup directory:");
//                                                String usein = cons.readLine();
//                                                if (Files.exists(Paths.get(usein)) && Files.isDirectory(Paths.get(usein))) {
//                                                    indir = Paths.get(usein);
//                                                } else {
//                                                    System.out.println("Invalid location");
//                                                }
//                                            }
//                                            Restore restore = new Restore(instruct_map, indir, ck);
//                                            (new Thread(restore)).start();
//                                        } else {
//                                            System.out.println("Error: failed to stablish connection");
//                                        }
//                                    } else {
//                                        System.out.println("Invalid encription key");
//                                    }
                                } else if (sub_ans == 5) {
                                    server_active = false;
                                    if (stopController(glassfish, db_server)) {//                                                                               System.out.println("\nStopping server before restore..");
                                        Backup backup = new Backup(instruct_map, null, encript_password, null, true, 3);
                                        (new Thread(backup)).start();
                                        int safety = 100000;
                                        while (!backup.hasFinished()) {
                                            safety--;
                                        }
                                        if (backup.hasFinished()) {
                                            System.out.println("Restore result = " + backup.getOutCome());
                                        }
                                    } else {
                                        System.out.println("Stopping the server failed");
                                    }
                                } else if (sub_ans == 6) {
                                    System.out.println("Sync");//              
                                    sync_with_central(true);
                                } else if (sub_ans == 7) {
                                    if (verbose) {
                                        verbose = false;
                                        System.out.println("Verbose mode set to:" + verbose);//              
                                    } else {
                                        verbose = true;
                                        System.out.println("Verbose mode set to:" + verbose);//              
                                    }
                                }
                            }
                            break;
                            case 4: {
                                Connection c_con = null;
                                ArrayList<String> sub_cmd_l = new ArrayList<>();
                                sub_cmd_l.add("View only database");//0
                                sub_cmd_l.add("Updatable database");//1
                                sub_cmd_l.add("Exit menu");//2   
                                int sub_ans = getUserChoice(sub_cmd_l, "Select type");
                                if (sub_ans == 0) {
                                    c_con = getConnection(true);
                                } else if (sub_ans == 1) {
                                    c_con = getConnection(false);
                                }
                                if (c_con != null) {
                                    System.out.println("Connecting to: " + c_con.getMetaData().getURL());
                                    ArrayList<String> sub2_cmd_l = new ArrayList<>();
                                    sub2_cmd_l.add("Drop table (Warning! this action cannot be undone");//10
                                    sub2_cmd_l.add("Select query");//12                   
                                    sub2_cmd_l.add("Update query");//12    
                                    sub2_cmd_l.add("Show index");//12 
                                    sub2_cmd_l.add("Changes to tables");//12 
                                    sub2_cmd_l.add("Exit menu");//12    
                                    int sub2_ans = getUserChoice(sub2_cmd_l, "Select a function");
                                    Timing.setPointer2();
                                    if (sub2_ans == 0) {
                                        if (droptable(c_con) >= 0) {
                                            System.out.println("Table droped");
                                        } else {
                                            System.out.println("Nothing changed");
                                        }
                                    } else if (sub2_ans == 1) {
                                        System.out.println("Type in SELECT query:");
                                        String query = cons.readLine();
                                        ArrayList<String> re_l = executeQuery(c_con, query);
                                        for (int i = 0; i < re_l.size(); i++) {
                                            System.out.println(re_l.get(i));
                                        }
                                    } else if (sub2_ans == 2) {
                                        System.out.println("Type in UPDATE query :");
                                        String query = cons.readLine();
                                        System.out.println("Result=" + executeUpdate(c_con, query));
                                    } else if (sub2_ans == 3) {
                                        System.out.println("Type in a table name:");
                                        String table = cons.readLine();
                                        print_indices(c_con, table);
                                    } else if (sub2_ans == 4) {
                                        System.out.println("Type in ALTERquery :");
                                        String query = cons.readLine();
                                        System.out.println("Result=" + execute(c_con, query));
                                    } else if (sub2_ans == 5) {
                                    }
                                    c_con.close();
                                    System.out.println("Time taken:" + Timing.convert(Timing.getFromlastPointer2()));
                                }

                            }
                            break;
                            case 5: {
                                System.out.println("Server version= " + SERVER_VERSION);
                                System.out.println("Command line tool (egenv)= " + COMMAND_LINE_TOOL_VERSION);
                                System.out.println("Web interface (eGenVar_web)= " + WEB_INTERFACE_VERSION);
                                System.out.println("Web services= " + WEB_SERVICE_VERSION);
                            }
                            break;
                            case 6: {
                                makeJsonTbl(getConnection_memory(false, 4), instruct_map.get(DATABASE_NAME_DATA_FLAG));
                                Reports reports = new Reports(get_correct_table_name(getConnection(),
                                        "files", instruct_map.get(DATABASE_NAME_DATA_FLAG), 300),
                                        get_correct_table_name(getConnection(), "files2path",
                                        instruct_map.get(DATABASE_NAME_DATA_FLAG), 400),
                                        dbURL_dataEntry, instruct_map.get(SERVER_DOCROOT_DIR_FLAG), instruct_map.get(S_SERVER_NAME_FLAG));
                                (new Thread(reports)).start();
                            }
                            break;
                            case 7: {
                                System.out.println(interface_details);
                            }
                            break;
                            case 8: {
                                getConnection_memory(true, 5);
                                ResultSet r = getConnection_memory(false, 6).
                                        createStatement().executeQuery("select count(id) as report_count from "
                                        + get_correct_table_name(getConnection_memory(false, 7),
                                        "report", instruct_map.get(DATABASE_NAME_DATA_FLAG), 500));
                                while (r.next()) {
                                    System.out.println(" REPORT.COUNT " + r.getInt(1));
                                }
                            }
                            break;
                            case 9: {
                                ArrayList<String> sub2_cmd_l = new ArrayList<>();
                                sub2_cmd_l.add("Activate administer by email and generate security code ");//10
                                sub2_cmd_l.add("Deactivate administer by email");//12    
                                sub2_cmd_l.add("Exit (do not change anything)");//12  
                                int sub2_ans = getUserChoice(sub2_cmd_l, "Select a function");
                                if (sub2_ans == 0) {
                                    adminbyemail_code = rand(10000, 99000);
                                    System.out.println("Security code is :" + adminbyemail_code);
                                } else if (sub2_ans == 1) {
                                    adminbyemail_code = -1;
                                }
                            }
                            break;
                            default: {
                                System.out.println("Exit");//     
                            }
                            break;

                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                } else {
                    System.out.println("______________________\negenvar server started ports used " + SERVER_SECURE_PORT_FLAG + "=" + instruct_map.get(SERVER_SECURE_PORT_FLAG)
                            + "|" + SERVER_PORT_FLAG + "=" + instruct_map.get(SERVER_PORT_FLAG)
                            + "|" + derby_port_flag + "=" + instruct_map.get(derby_port_flag)
                            + "|" + derby_port_MEM_flag + "=" + instruct_map.get(derby_port_MEM_flag) + ""
                            + "\n\nWeb interface: " + instruct_map.get(SERVER_NAME_FLAG) + ""
                            + "\nWSDL for webservices: " + instruct_map.get(WSDL_URL_FLAG) + "\n"
                            + "\nUp time " + Timing.convert(Timing.getFromlastPointer()) + "\n");
                }

//                int menucode = -1;
//                switch (menucode) {
//                    case 0: {
//                        stopController(glassfish, db_server);
//                        completed = true;
//
//                    }
//                    break;
//                }


            }
        }
        System.out.println("Waiting for server controller..");
        ServerControls.stop();
        if (server_control_thread != null && server_control_thread.isAlive()) {
            server_control_thread.interrupt();
        }
//        if (server_control_thread2 != null && server_control_thread2.isAlive()) {
//            server_control_thread2.interrupt();
//        }

//        if (sync_thread != null && sync_thread.isAlive()) {
//            Syc_manager.deActivate();
//            sync_thread.interrupt();
//        }
        ProgressMonitor.cancel();
        System.out.println("All services stopped");

//        if (restart) {
//            System.out.println("917 ");
//            ProgressMonitor.cancel();
//            boolean verbose = false;
//            if (Files.isRegularFile(current_config_file, LinkOption.NOFOLLOW_LINKS) && Files.isReadable(current_config_file)) {
//                       System.out.println("921 "+current_config_file);
//                if (init(current_config_file.toAbsolutePath().toString(), true, verbose, true, false, false, false, false)) {
//                    System.out.println("923");
//                    commit(null, false, true, false, false);
//                    System.out.println("926");
//                    ProgressMonitor.cancel();
//                } else {
//                    System.out.println("Error: init failed");
//                }
//            } else {
//                System.out.println("Error 0a: cannot locate or file canot be read : " + LAST_CONFIG_DETAILS_FILE_FLAG);
//            }
//        }
    }

    public boolean stopController(GlassFish glassfish, NetworkServerControl db_server) {
        sync_tagsources();
        System.out.println("Initiating...");
//        boolean completed = false;
        try {
            int max_wait_loop = 100;
            try {
                System.out.println("Saving changes to config..");
                writeResultsToFileEncripted(instruct_map, LAST_CONFIG_DETAILS_FILE_FLAG);
                if (getConnection_memory(false, 9) != null && !getConnection_memory(false, 8).isClosed()) {
                    while ((isActive(getConnection_memory(false, 10)) || tagsource_refreshing) && max_wait_loop > 0) {
                        max_wait_loop--;
                        System.out.println("Waiting untill a search operation is finnished (attempt " + (100 - max_wait_loop) + ")");
                        try {
                            Thread.sleep(5000);
                        } catch (InterruptedException ex) {
                        }
                    }
                    System.out.println("Is there any active searches=NO");
                } else {
                    System.out.println("Warning 1033");
                }
                max_wait_loop = 100;
                if (getConnection() != null && !getConnection().isClosed()) {
                    while (isActive(getConnection()) && max_wait_loop > 0) {
                        max_wait_loop--;
                        System.out.println("Waiting untill a transaction is finnished (attempt " + (100 - max_wait_loop) + ")");
                        try {
                            Thread.sleep(5000);
                        } catch (InterruptedException ex) {
                        }
                    }
                    System.out.println("Is there any active transaction=NO");
                } else {
                    System.out.println("Warning 1047");
                }
            } catch (Exception ex) {
                System.out.println("Warning , database droppped from memory " + ex.getMessage());
            }
            System.out.println("Stoping, please wait ...");
            ServerControls.stop();
            if (server_control_thread != null && server_control_thread.isAlive()) {
                server_control_thread.interrupt();
            }
//            if (server_control_thread2 != null && server_control_thread2.isAlive()) {
//                server_control_thread2.interrupt();
//            }
//            if (sync_thread != null && sync_thread.isAlive()) {
//                Syc_manager.deActivate();
//                sync_thread.interrupt();
//            }

            Files.deleteIfExists(Paths.get("*trace"));
            try {
                DirectoryStream<Path> ds = Files.newDirectoryStream(FileSystems.getDefault().getPath("."));
                Iterator<Path> path_l = ds.iterator();
                System.out.println("Cleaning trace");
                int c = 0;
                while (path_l.hasNext()) {
                    Path cp = path_l.next();
                    if (cp.getFileName().toString().toLowerCase().endsWith(".trace")) {
                        if (c % 10 == 0) {
                            System.out.print(".");
                        }
                        Files.deleteIfExists(cp);
                    }
                    c++;
                }
                System.out.println("Cleaning trace -- Done");
                ds.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            glassfish.stop();
            db_server.shutdown();
            Files.deleteIfExists(Paths.get(".egenvar_lok"));
            server_active = false;          
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return server_active;
    }
    /*
     do this as part of backup
     *        //re_memeorize() cals this
     */

    public void setdelete_request_code() {
        String[] c_det_a = getDeleteCode();
//        if (c_det_a != null) {
//            System.out.println("1238 " + Arrays.deepToString(c_det_a));
//        }
        long delete_time = 0;
        if (c_det_a[1] != null && c_det_a[1].matches("[0-9]+")) {
            delete_time = new Long(c_det_a[1]);
        }
        GregorianCalendar c_cal = new GregorianCalendar();
        long current = c_cal.getTimeInMillis();
        if (delete_time < current) {
//            System.out.println("\n1233 delete_time=" + delete_time + "  current=" + current);
            c_cal.add(Calendar.HOUR, 12);
            delete_request_code = rand(10000, 99999) + "|" + (c_cal.getTimeInMillis());
            HashMap<String, String> config_map = new HashMap<>();
            config_map.put(DELETE_REQUEST_CODE_FLAG, delete_request_code + "");
//            System.out.println("1239 " + config_map);
            if (setConfig(getConnection(), config_map, instruct_map.get(DATABASE_NAME_DATA_FLAG)) < 0) {
            }
            config_map.clear();
        }


    }

    public void sync_tagsources() {
        tagsource_refreshing = true;
        Connection c_con = null;
        try {
            String data_folder_nm = instruct_map.get(SOURCE_DATA_DIR_FLAG);
            File data_folder = new File(data_folder_nm);
            if (data_folder.isDirectory() && data_folder.canWrite()) {
                ArrayList<String> k_l = new ArrayList<>(instruct_map.keySet());
                c_con = getConnection2();
                ArrayList<String> tag_source_tables_l = getCurrentTables(c_con, instruct_map.get(DATABASE_NAME_DATA_FLAG), 1);
                for (int i = 0; i < tag_source_tables_l.size(); i++) {
                    if (!tag_source_tables_l.get(i).toLowerCase().endsWith(TAGSOURCE_FLAG)) {
                        tag_source_tables_l.remove(i);
                        i--;
                    }
                }
//                System.out.println("1308 " + tag_source_tables_l);
                Statement st = c_con.createStatement();
                ArrayList<String> processed_l = new ArrayList<>();
                for (int i = 0; i < k_l.size(); i++) {
                    String c_val = k_l.get(i);
                    if (!processed_l.contains(c_val.toUpperCase())) {
                        processed_l.add(c_val.toUpperCase());
                        if (c_val.toLowerCase().endsWith(TAGSOURCE_FLAG)) {
                            String table_nm = get_correct_table_name(c_con, c_val,
                                    instruct_map.get(DATABASE_NAME_DATA_FLAG), 600);
                            if (table_nm != null) {
                                if (!tag_source_tables_l.remove(table_nm)) {
                                    for (int j = 0; j < tag_source_tables_l.size(); j++) {
                                        if (tag_source_tables_l.get(j).equalsIgnoreCase(table_nm)) {
                                            tag_source_tables_l.remove(j);
                                            j--;
                                        }
                                    }
                                }
//                            System.out.println("Syncing " + table_nm + " with " + instruct_map.get(c_val));
                                ResultSet r_1 = st.executeQuery("SELECT * from " + table_nm + " order by parent_id");
                                ResultSetMetaData md = r_1.getMetaData();
                                int col_c = md.getColumnCount();
                                StringBuffer stb = new StringBuffer();
                                stb.append("#");
                                stb.append(md.getColumnName(1));
                                for (int j = 2; j <= col_c; j++) {
                                    stb.append("||" + md.getColumnName(j));
                                }
                                stb.append("\n");
                                int c = 0;
                                while (r_1.next()) {
                                    c++;
                                    if ((c % 5000) == 0) {
                                        System.out.print(".");
                                    }
                                    stb.append(r_1.getString(1));
                                    for (int j = 2; j <= col_c; j++) {
                                        stb.append("||" + r_1.getString(j));
                                    }
                                    stb.append("\n");
                                }

                                FileWriter out;
                                try {
                                    out = new FileWriter(instruct_map.get(c_val));
                                    out.append(stb);
                                    out.close();
                                } catch (IOException ex) {
                                    System.out.println("Error saving tagsource changes. Possible data loss ");
                                }
                            }
                        }
                    }
                }
                System.out.println(".");
//                System.out.println("1361 lefts " + tag_source_tables_l);
                for (int i = 0; i < tag_source_tables_l.size(); i++) {
                    String table_nm = tag_source_tables_l.get(i);
                    Path file_nm = Paths.get(data_folder.getAbsolutePath(), table_nm.split("\\.")[table_nm.split("\\.").length - 1] + ".txt");
                    System.out.println("Syncing " + table_nm + " with " + file_nm);
                    ResultSet r_1 = st.executeQuery("SELECT * from " + table_nm);
                    ResultSetMetaData md = r_1.getMetaData();
                    int col_c = md.getColumnCount();
                    StringBuffer stb = new StringBuffer();
                    stb.append("#");
                    stb.append(md.getColumnName(1));
                    for (int j = 2; j <= col_c; j++) {
                        stb.append("||" + md.getColumnName(j));
                    }
                    stb.append("\n");
                    int c = 0;
                    while (r_1.next()) {
                        c++;
                        if ((c % 1000) == 0) {
                            System.out.print(".");
                        }
                        stb.append(r_1.getString(1));
                        for (int j = 2; j <= col_c; j++) {
                            stb.append("||" + r_1.getString(j));
                        }
                        stb.append("\n");
                    }
                    System.out.println(".");
                    FileWriter out;
                    try {
                        out = new FileWriter(file_nm.toString());
                        out.append(stb);
                        out.close();
                    } catch (IOException ex) {
                        System.out.println("Error saving tagsource changes. Possible data loss ");
                    }
                }
            } else {
                System.out.println("Error saving tagsource changes. Possible data loss, directory inaccible  " + data_folder_nm);
            }
            c_con.close();
        } catch (SQLException ex) {
//            ex.printStackTrace();
            System.out.println("Error saving tagsource changes. Possible data loss ");
        } finally {
            try {
                if (c_con != null) {
                    c_con.close();
                }
            } catch (SQLException ex) {
                System.out.println("Error saving tagsource changes. Possible data loss ");
            }
        }
        tagsource_refreshing = false;
    }

    private void mailTest(boolean forcelocalhost, boolean reset_forcelocalhost,
            boolean find_free_port, boolean restserver, boolean recreate_mail_cert, boolean recreate_serverl_cert) {
        if (init(null, false, false, forcelocalhost, reset_forcelocalhost,
                find_free_port, restserver, recreate_mail_cert, recreate_serverl_cert, new HashMap<String, String>())) {
            String mail_subject = "test";
            String mail_message = "test";
            if (instruct_map.get("USE_LOCAL_HOST_FOR_MAIL") != null && instruct_map.get("USE_LOCAL_HOST_FOR_MAIL").equalsIgnoreCase("TRUE")) {
                sendEmail(true, instruct_map.get("--mailhost"), new Integer(instruct_map.get("mail.smtp.port")),
                        instruct_map.get("--mailuser"), instruct_map.get("mail.smtp.password"),
                        mail_subject, mail_message, instruct_map.get("--fromaddress"), instruct_map.get(TRUST_STORE_FILE_NM), "changeit");
            } else {
                sendEmail(false, instruct_map.get("--mailhost"), new Integer(instruct_map.get("mail.smtp.port")),
                        instruct_map.get("--mailuser"), instruct_map.get("mail.smtp.password"),
                        mail_subject, mail_message, instruct_map.get("--fromaddress"), instruct_map.get(TRUST_STORE_FILE_NM), "changeit");
            }
        } else {
            System.out.println("Error3: init failed");
        }
    }

    private void init_map(boolean forcelocalhost) {
//        instruct_map = new HashMap<>();
        //         new_instruct_map.put("mail.smtp.port", "587");
//         new_instruct_map.put("mail.smtp.socketFactory.port", "587");
        //         new_instruct_map.put(DATABASE_NAME_DATA_MEM_FLAG, "EGEN_DATAENTRY_MEM");
        HashMap<String, String> new_instruct_map = new HashMap<>();
        new_instruct_map.put(MODE_FLAG, current_mode + "");
        new_instruct_map.put(SERVER_BASE_FLAG, "localhost");
        new_instruct_map.put(SERVER_TITLE, "UNKNOWN");
        new_instruct_map.put(SERVER_SECURE_PORT_FLAG, "8185");
        new_instruct_map.put(SERVER_PORT_FLAG, "8085");
        new_instruct_map.put(derby_port_flag, "1557");
        new_instruct_map.put(derby_port_MEM_flag, "1567");

        new_instruct_map.put(unm_usermanage_flag, "usermanage");
        new_instruct_map.put(unm_usermanage_pass_flag, randomstring(10));
        new_instruct_map.put(unm_gendataentry_flag, "egendataentry");
        new_instruct_map.put(unm_gendataentry_pass_flag, randomstring(10));
        new_instruct_map.put(unm_gendataview_flag, "egendataview");
        new_instruct_map.put(unm_gendataUpdate_pass_flag, randomstring(10));
        new_instruct_map.put(unm_gendataUpdate_flag, "egendataupdate");
        new_instruct_map.put(unm_gendataview_pass_flag, randomstring(10));
        new_instruct_map.put(db_host_flag, "derby://localhost");
        new_instruct_map.put(DATABASE_NAME_USERS_FLAG, "EGEN_USERS");
        new_instruct_map.put(DATABASE_NAME_DATA_FLAG, "EGEN_DATAENTRY");


        new_instruct_map.put(DB_LOG_FILENM_FLAG, "db_log.log");
        new_instruct_map.put(CREATE_DB_FILE_DERBY_FLAG, CREATE_DB_FILE_DERBY_DEFAULT);
        new_instruct_map.put(DATASOURCECLASSNAME_FLAG, "org.apache.derby.jdbc.ClientDataSource");
        new_instruct_map.put(DRIVERCLASSNAME_FLAG, "org.apache.derby.jdbc.ClientDriver");
        new_instruct_map.put(DB_AUTHENTICATION_ALGORITHM_FLAG, DB_AUTHENTICATION_ALGORITHM);

        new_instruct_map.putAll(getNetInfo(forcelocalhost));
        new_instruct_map.put("--fromaddress", null);
        new_instruct_map.put("--mailuser", null);
        new_instruct_map.put("--mailhost", "smtp.gmail.com");
        new_instruct_map.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        new_instruct_map.put("mail.smtp.password", null);
        new_instruct_map.put("mail.smtp.auth", "true");
        new_instruct_map.put("mail.smtp.socketFactory.fallback", "false");
        new_instruct_map.put("mail.imap.port", IMAP_HOST_PORT + "");
        new_instruct_map.put("mail.smtp.socketFactory.port", SMTP_HOST_PORT + "");
        new_instruct_map.put("mail.smtp.port", SMTP_HOST_PORT + "");
        new_instruct_map.put("mail.transport.protocol", "smtps");
        new_instruct_map.put("mail.smtps.host", "smtp.gmail.com");
        new_instruct_map.put("mail.smtps.auth", "true");
        new_instruct_map.put("USE_LOCAL_HOST_FOR_MAIL", null);

        new_instruct_map.put(TRUST_STORE_FILE_NM, null);
        new_instruct_map.put(CMD_TAR_LOC_FLAG, null);
        new_instruct_map.put(CMD_ZIP_LOC_FLAG, null);
        new_instruct_map.put(CMD_JAR_LOC_FLAG, null);
        new_instruct_map.put(EGENR_LOC_FLAG, null);
        new_instruct_map.put(EGENPY_LOC_FLAG, null);
        new_instruct_map.put(PROCEDURE_JAr_LOC_FLAG, null);
        new_instruct_map.put(SERVER_DOC_ROOT_FLAG, "docroot");


        new_instruct_map.put(COMMAND_LINE_TOOL_VERSION_FLAG, COMMAND_LINE_TOOL_VERSION);
        new_instruct_map.put(WEB_INTERFACE_VERSION_FLAG, WEB_INTERFACE_VERSION);
        new_instruct_map.put(WEB_SERVICE_VERSION_FLAG, WEB_SERVICE_VERSION);

//         new_instruct_map.put(DERBY_CONNECTION_PARAM, "territory=en_US;collation=TERRITORY_BASED");
        new_instruct_map.put(DERBY_CONNECTION_PARAM, "territory=en_US;collation=TERRITORY_BASED:PRIMARY");
        new_instruct_map.put(SERVER_INSTALATION_ROOT_FLAG, null);
        new_instruct_map.put(SRC_ROOT_FLAG, null);
        new_instruct_map.put(BAKSUP_INSTRUCT_FLAG, "CP|AUTO_BACKUP");
        if (instruct_map == null) {
            instruct_map = new HashMap<>();
        }
        Iterator<String> key_s = new_instruct_map.keySet().iterator();
        while (key_s.hasNext()) {
            String key = key_s.next();
            if (!instruct_map.containsKey(key) || instruct_map.get(key) == null) {
                if (new_instruct_map.get(key) != null) {
                    instruct_map.put(key, new_instruct_map.get(key));
                }
            }
        }
    }

    /*
     MethodID=2
     * TODO: implement restserver
     * 
     * 
     *    instruct_map.put(SERVER_LIB_DIR_FLAG, instruct_map.get(SERVER_INSTALATION_ROOT_FLAG) + "lib" + File.separatorChar);
     instruct_map.put(SERVER_CONF_DIR_FLAG, instruct_map.get(SERVER_INSTALATION_ROOT_FLAG) + "config" + File.separatorChar);
     instruct_map.put(SERVER_DOCROOT_DIR_FLAG, instruct_map.get(SERVER_INSTALATION_ROOT_FLAG) + "docroot" + File.separatorChar);

     */
    private boolean init(String config_file_nm, boolean fromlast, boolean nomail, boolean forcelocalhost,
            boolean reset_forcelocalhost, boolean find_free_port, boolean restserver,
            boolean recreating_mail_cert, boolean recreate_serverl_cert, HashMap<String, String> overide_config_map) {
        (new Thread(new ProgressMonitor(PROGRESS_MONT_INTERVAL, "15"))).start();
        init_map(forcelocalhost);
        if (!fromlast) {
        } else {
            System.out.println("Loading pre-configured parameters from previous session.");
            instruct_map = readFromFileEncripted(LAST_CONFIG_DETAILS_FILE_FLAG, encript_password);
            instruct_map.putAll(getNetInfo(forcelocalhost));
            if (verbose) {
                ArrayList<String> conf_l = new ArrayList<>(instruct_map.keySet());
                for (int i = 0; i < conf_l.size(); i++) {
                    System.out.println(conf_l.get(i) + " => " + instruct_map.get(conf_l.get(i)));
                }
            }
        }
        ArrayList<String> overide_config_kl = new ArrayList<>(overide_config_map.keySet());
        for (int i = 0; i < overide_config_kl.size(); i++) {
            if (instruct_map.containsKey(overide_config_kl.get(i))) {
                instruct_map.put(overide_config_kl.get(i), overide_config_map.get(overide_config_kl.get(i)));
                System.out.println("Overiding configuration for " + overide_config_kl.get(i) + " with " + overide_config_map.get(overide_config_kl.get(i)));
            }
        }
        if (forcelocalhost) {
            instruct_map.putAll(getNetInfo(forcelocalhost));
        } else if (reset_forcelocalhost) {
            instruct_map.putAll(getNetInfo(false));
        }
        Set<PosixFilePermission> perms = new HashSet<>();
        try {
            perms = PosixFilePermissions.fromString("rw-rw-r--");
        } catch (Exception ex) {
            System.out.println("Error: Error setting permission PosixFilePermissions rw-rw-r--");
        }
        init_map(forcelocalhost);//to refil any missing vaøues
//        Set<PosixFilePermission> docroot_file_perm_set = new HashSet<>();
//        docroot_file_perm_set.add(PosixFilePermission.OTHERS_READ);
//        docroot_file_perm_set.add(PosixFilePermission.OTHERS_WRITE);
//        docroot_file_perm_set.add(PosixFilePermission.OTHERS_EXECUTE);

        boolean init_result = false;
        double java_version = -1;
        Properties p = System.getProperties();
        ArrayList<String> k_l = new ArrayList<>();
        k_l.add("os.name");
        k_l.add("os.version");
        k_l.add("java.specification.version");
        k_l.add("java.home");
        k_l.add("java.ext.dirs");
        k_l.add("java.library.path");
        k_l.add("sun.boot.library.path");
        k_l.add("sun.boot.class.path");

        if (verbose) {
            for (int i = 0; i < k_l.size(); i++) {
                System.out.println("575 " + k_l.get(i) + " => " + p.getProperty(k_l.get(i).toString()));
                k_l.get(i);
            }

        }
        String full_java_version = System.getProperty("java.version");
        if (full_java_version != null) {
            String[] java_versio_a = full_java_version.split("\\.");
            if (java_versio_a.length > 1) {
                String c_java_version = java_versio_a[0] + "." + java_versio_a[1];
                if (c_java_version.matches("[0-9\\.]+")) {
                    java_version = new Double(c_java_version);
                }
            }
        }
        if (java_version < REQUIRED_JAVA_VERSION) {
            System.out.println("Error 2c: Your JAVA version " + full_java_version + " is older than the minimum (" + REQUIRED_JAVA_VERSION + ") requirement.");
            System.out.println("EGDMS requires full JDK 1.7 or higher (not just the JRE), see the admin manual for more help.");
            System.exit(1);
        } else {
            if (!(java_version > 0)) {
                System.out.println("Error 2d: Failed to ditect JAVA version. This software may not function normally in this environment.");
            } else {
                System.out.println("Java version check. " + full_java_version + " >= " + REQUIRED_JAVA_VERSION + " -- OK");
            }
            String classpath = System.getProperty("java.class.path");
            if (classpath != null && !classpath.isEmpty() && classpath.endsWith("jar")) {
                try {
                    File path_file = new File(classpath);
                    String source_dir = path_file.getAbsoluteFile().getParent() + File.separatorChar;
                    instruct_map.put(SRC_ROOT_FLAG, source_dir);
                } catch (Exception ex) {
                    System.out.println("Warning: " + ex.getMessage());
                }
            }
            boolean config_file_ok = false;
            if (config_file_nm != null) {
                File cFile = new File(config_file_nm);
                if (cFile.isFile() && cFile.canRead()) {
                    instruct_map.put(CONFIG_FILE_FLAG, cFile.getAbsolutePath());
                    try {
                        int count = 0;
                        Scanner scan = new Scanner(cFile);
                        while (scan.hasNext()) {
                            count++;
                            String line = scan.nextLine().trim();
                            if (line.isEmpty() || line.startsWith("#") || !line.contains("=")) {
                            } else {
                                String[] instr_a = line.split("=", 2);
                                if (instr_a[0].startsWith(CREATE_TABLE_FROM_FILE_FLAG)) {
                                    String instr = instr_a[1].trim();
                                    if (!instr.isEmpty()) {
                                        instruct_map.put(CREATE_TABLE_FROM_FILE_FLAG + count, instr);
                                    }
                                }
                                String key = getRealKey(instruct_map.keySet(), instr_a[0]);
                                if (key != null) {
                                    instruct_map.put(key, instr_a[1].trim());
                                }
                            }
                        }
                        config_file_ok = true;
                    } catch (FileNotFoundException ex) {
                        ex.printStackTrace();
                    }
                } else {
                    System.out.println("Error 2a: invalid config file (or read access denied) " + config_file_nm + ". To use default values invoke without arguments");
                    config_file_ok = false;
                }
            } else {
                config_file_ok = true;
                System.out.println("No config file specified, using default values or the values used last time\n\n");
            }

            if (config_file_ok) {
                if (instruct_map.get(SRC_ROOT_FLAG) != null) {
                    File src_dir = new File(instruct_map.get(SRC_ROOT_FLAG));
                    if (src_dir.isDirectory() && src_dir.canRead()) {
                        try {
                            instruct_map.put(src_dir.getCanonicalPath() + File.separator, null);
                        } catch (IOException ex) {
                            ex.printStackTrace();
                        }
                        instruct_map.put(SOURCE_DATA_DIR_FLAG, instruct_map.get(SRC_ROOT_FLAG) + "data" + File.separatorChar);
                        instruct_map.put(SOURCE_WAR_DIR_FLAG, instruct_map.get(SRC_ROOT_FLAG) + "war" + File.separatorChar);
                        instruct_map.put(SOURCE_SYNC_DIR_FLAG, instruct_map.get(SRC_ROOT_FLAG) + "sync" + File.separatorChar);
                        if (instruct_map.get(SOURCE_LIB_DIR_FLAG) == null) {
                            instruct_map.put(SOURCE_LIB_DIR_FLAG, instruct_map.get(SRC_ROOT_FLAG) + "lib" + File.separatorChar);
                        }


                        if (instruct_map.get(SERVER_INSTALATION_ROOT_FLAG) == null) {
                            instruct_map.put(SERVER_INSTALATION_ROOT_FLAG, instruct_map.get(SRC_ROOT_FLAG) + "egenvar");
                        } else {
//                            File server_root = new File(instruct_map.get(SERVER_INSTALATION_ROOT_FLAG));
//                            if (!fromlast) {
//                                try {
//                                    instruct_map.put(SERVER_INSTALATION_ROOT_FLAG, server_root.getAbsoluteFile().getCanonicalPath() + File.separatorChar + "egenvar");
//                                } catch (IOException ex) {
//                                    instruct_map.put(SERVER_INSTALATION_ROOT_FLAG, null);
//                                    System.out.println("Error: Can not access server root (" + SERVER_INSTALATION_ROOT_FLAG + ") " + instruct_map.get(SERVER_INSTALATION_ROOT_FLAG));
//                                }
//                            }
                        }
                        File server_root = new File(instruct_map.get(SERVER_INSTALATION_ROOT_FLAG));
                        if (!server_root.isDirectory()) {
                            server_root.mkdirs();
                        } else {
//                            File[] c_file_a = server_root.listFiles();
//                            for (int i = 0; i < c_file_a.length; i++) {
//                                String c_nm = c_file_a[i].getName();
//                                if (c_nm.endsWith("column_nm_l") || c_nm.endsWith("data_l") || c_nm.endsWith(".trace") || c_nm.startsWith("derby")) {
//                                    c_file_a[i].delete();
//                                }
//
//                            }
                        }
                        if (server_root.isDirectory() && server_root.canWrite()) {
                            try {
                                instruct_map.put(SERVER_INSTALATION_ROOT_FLAG, server_root.getAbsoluteFile().getCanonicalPath() + File.separatorChar);
                            } catch (IOException ex) {
                                instruct_map.put(SERVER_INSTALATION_ROOT_FLAG, null);
                                System.out.println("Error: Can not access server root (" + SERVER_INSTALATION_ROOT_FLAG + ") " + instruct_map.get(SERVER_INSTALATION_ROOT_FLAG));
                            }
                        } else {
                            System.out.println("Error: Can not access server root (" + SERVER_INSTALATION_ROOT_FLAG + ") " + instruct_map.get(SERVER_INSTALATION_ROOT_FLAG));
                        }
                        if (instruct_map.get(SERVER_INSTALATION_ROOT_FLAG) != null) {
                            instruct_map.put(SERVER_LIB_DIR_FLAG, instruct_map.get(SERVER_INSTALATION_ROOT_FLAG) + "lib" + File.separatorChar);
                            instruct_map.put(SERVER_CONF_DIR_FLAG, instruct_map.get(SERVER_INSTALATION_ROOT_FLAG) + "config" + File.separatorChar);
                            instruct_map.put(SERVER_DOCROOT_DIR_FLAG, instruct_map.get(SERVER_INSTALATION_ROOT_FLAG) + "docroot" + File.separatorChar);


                            File serv_lib = new File(instruct_map.get(SERVER_LIB_DIR_FLAG));
                            if (!serv_lib.isDirectory()) {
                                serv_lib.mkdirs();
                            }

                            if (serv_lib.isDirectory() && serv_lib.canWrite()) {
                                File serv_metro = new File(instruct_map.get(SERVER_LIB_DIR_FLAG) + "install/applications/metro/");
                                if (!serv_metro.isDirectory()) {
                                    serv_metro.mkdirs();
                                }
                                instruct_map.put(SERVER_METRO_DIR, instruct_map.get(SERVER_LIB_DIR_FLAG) + "install/applications/metro/");

                                File serv_cof = new File(instruct_map.get(SERVER_CONF_DIR_FLAG));

                                if (!serv_cof.isDirectory()) {
                                    serv_cof.mkdirs();
                                }

                                if (serv_cof.isDirectory() && serv_cof.canWrite()) {
                                    try {
                                        FileWriter out = new FileWriter(instruct_map.get(SERVER_CONF_DIR_FLAG) + "certify.sh", false);
                                        out.append(certificate_script);
                                        out.close();
                                        out = new FileWriter(instruct_map.get(SERVER_CONF_DIR_FLAG) + "certify_gmail.sh", false);
                                        out.append(gmail_certificate_script);
                                        out.close();
                                    } catch (IOException ex) {
                                        System.out.println("Error : " + ex.getMessage());
                                    }

                                    File serv_docroot = new File(instruct_map.get(SERVER_DOCROOT_DIR_FLAG));
                                    if (!serv_docroot.isDirectory()) {
                                        serv_docroot.mkdirs();
                                    }
                                    if (serv_docroot.isDirectory() && serv_docroot.canWrite()) {
                                        if (init_port(config_file_nm, find_free_port)) {
                                            instruct_map.put(SERVER_ROOT_FLAG, "http://" + instruct_map.get(SERVER_BASE_FLAG) + ":" + instruct_map.get(SERVER_PORT_FLAG) + "/");
                                            instruct_map.put(SERVER_NAME_FLAG, instruct_map.get(SERVER_ROOT_FLAG) + "eGenVar_web/");
                                            instruct_map.put(S_SERVER_ROOT_FLAG, "https://" + instruct_map.get(SERVER_BASE_FLAG) + ":" + instruct_map.get(SERVER_SECURE_PORT_FLAG) + "/");
                                            instruct_map.put(S_SERVER_NAME_FLAG, instruct_map.get(S_SERVER_ROOT_FLAG) + "eGenVar_web/");
                                            instruct_map.put(WSDL_URL_FLAG, "http://" + instruct_map.get(SERVER_BASE_FLAG) + ":" + instruct_map.get(SERVER_PORT_FLAG) + "/eGenVar_WebService/" + "Authenticate_service?wsdl");
                                            instruct_map.put(S_WSDL_URL_FLAG, "https://" + instruct_map.get(SERVER_BASE_FLAG) + ":" + instruct_map.get(SERVER_SECURE_PORT_FLAG) + "/eGenVar_WebService/" + "Authenticate_service?wsdl");

                                            System.out.println("1258 " + SERVER_ROOT_FLAG + "=" + instruct_map.get(SERVER_ROOT_FLAG));
                                            System.out.println("1258 " + SERVER_NAME_FLAG + "=" + instruct_map.get(SERVER_NAME_FLAG));
                                            System.out.println("1258 " + S_SERVER_ROOT_FLAG + "=" + instruct_map.get(S_SERVER_ROOT_FLAG));
                                            System.out.println("1258 " + S_SERVER_NAME_FLAG + "=" + instruct_map.get(S_SERVER_NAME_FLAG));
                                            System.out.println("1258 " + WSDL_URL_FLAG + "=" + instruct_map.get(WSDL_URL_FLAG));
                                            System.out.println("1258 " + S_WSDL_URL_FLAG + "=" + instruct_map.get(S_WSDL_URL_FLAG));


                                            dbURL_users = "jdbc:" + instruct_map.get(db_host_flag) + ":" + instruct_map.get(derby_port_flag) + "/" + instruct_map.get(DATABASE_NAME_USERS_FLAG) + ";create=true;user=" + instruct_map.get(unm_usermanage_flag) + ";password=" + instruct_map.get(unm_usermanage_pass_flag);
                                            dbURL_dataEntry = "jdbc:" + instruct_map.get(db_host_flag) + ":" + instruct_map.get(derby_port_flag) + "/" + instruct_map.get(DATABASE_NAME_DATA_FLAG) + ";create=true;" + instruct_map.get(DERBY_CONNECTION_PARAM) + ";user=" + instruct_map.get(unm_gendataentry_flag) + ";password=" + instruct_map.get(unm_gendataentry_pass_flag);

                                            try {
                                                System.out.println("1205 " + instruct_map.get(SOURCE_LIB_DIR_FLAG) + "  ");
                                                System.out.println("1206 " + "  " + FileSystems.getDefault());

                                                DirectoryStream<Path> ds = Files.newDirectoryStream(FileSystems.getDefault().getPath(instruct_map.get(SOURCE_LIB_DIR_FLAG)));
                                                if (ds != null) {
                                                    Iterator<Path> path_l = ds.iterator();
                                                    while (path_l.hasNext()) {
                                                        Path c_path = path_l.next();
                                                        Files.copy(c_path, Paths.get((instruct_map.get(SERVER_LIB_DIR_FLAG) + c_path.getFileName())), StandardCopyOption.REPLACE_EXISTING);
                                                    }
                                                    ds.close();
                                                } else {
                                                    System.out.println("Error:  " + instruct_map.get(SOURCE_LIB_DIR_FLAG));
                                                    System.exit(1);
                                                }

                                            } catch (IOException e) {
                                            }
                                            boolean cmd_found = false;
                                            boolean egenr_found = false;
                                            boolean egenpy_found = false;
                                            String keystore_file = null;
                                            String server_cer_file = null;
                                            String cacerts_jks_file = null;
                                            File[] docroot_a = serv_docroot.listFiles();
                                            for (int i = 0; i < docroot_a.length; i++) {
                                                if (docroot_a[i].getName().equals(CMD_JAR_NAME)) {
                                                    cmd_found = true;
                                                    instruct_map.put(CMD_JAR_LOC_FLAG, instruct_map.get(SERVER_ROOT_FLAG) + docroot_a[i].getName());
                                                } else if (docroot_a[i].getName().equals(CMD_TAR_NAME)) {
                                                    instruct_map.put(CMD_TAR_LOC_FLAG, instruct_map.get(S_SERVER_ROOT_FLAG) + docroot_a[i].getName());
                                                } else if (docroot_a[i].getName().equals(CMD_ZIP_NAME)) {
                                                    instruct_map.put(CMD_ZIP_LOC_FLAG, instruct_map.get(S_SERVER_ROOT_FLAG) + docroot_a[i].getName());
                                                } else if (docroot_a[i].getName().equals(EGENR_FLAG)) {
                                                    instruct_map.put(EGENR_LOC_FLAG, instruct_map.get(S_SERVER_ROOT_FLAG) + docroot_a[i].getName());
                                                    egenr_found = true;
                                                } else if (docroot_a[i].getName().equals(CMD_ZIP_NAME)) {
                                                    instruct_map.put(EGENPY_LOC_FLAG, instruct_map.get(S_SERVER_ROOT_FLAG) + docroot_a[i].getName());
                                                    egenpy_found = true;
                                                }
                                            }
                                            File[] config_ents_a = serv_cof.listFiles();
                                            if (!recreate_serverl_cert) {
                                                for (int i = 0; i < config_ents_a.length; i++) {
                                                    if (config_ents_a[i].getName().equals("keystore.jks")) {
                                                        keystore_file = config_ents_a[i].getAbsolutePath();
                                                    } else if (config_ents_a[i].getName().equals("cacerts.jks")) {
                                                        cacerts_jks_file = config_ents_a[i].getAbsolutePath();
                                                    } else if (config_ents_a[i].getName().equals("server.cer")) {
                                                        server_cer_file = config_ents_a[i].getAbsolutePath();
                                                    }
                                                }
                                            }
                                            try {
                                                DirectoryStream<Path> ds = Files.newDirectoryStream(FileSystems.getDefault().
                                                        getPath(instruct_map.get(SOURCE_LIB_DIR_FLAG)));
                                                Iterator<Path> path_l = ds.iterator();
                                                File new_Cacert_keyStrore_file = null;
                                                while (path_l.hasNext()) {
                                                    Path c_path = path_l.next();
                                                    String c_name = c_path.getFileName().toString();
//                                                    System.out.println("1774 "+c_name);
                                                    boolean docrooted = false;
                                                    if (Files.isRegularFile(c_path, LinkOption.NOFOLLOW_LINKS) && Files.isReadable(c_path)) {
                                                        if (cacerts_jks_file == null && c_name.equals("cacerts.jks")) {
                                                            new_Cacert_keyStrore_file = new File(instruct_map.get(SERVER_CONF_DIR_FLAG) + c_name);
                                                            Files.copy(c_path, Paths.get(instruct_map.get(SERVER_CONF_DIR_FLAG) + c_name), StandardCopyOption.REPLACE_EXISTING);
                                                            cacerts_jks_file = new_Cacert_keyStrore_file.getCanonicalPath();
                                                            if (new_Cacert_keyStrore_file.isFile() && new_Cacert_keyStrore_file.canWrite()) {
//                                                                cacerts_jks_file = createCertificate(new_Cacert_keyStrore_file.getCanonicalPath(), "changeit".toCharArray());
                                                                recreating_mail_cert = true;
                                                            }
                                                        } else if (keystore_file == null && c_name.equals("keystore.jks")) {
                                                            Path new_keyStrore_file = Paths.get(instruct_map.get(SERVER_CONF_DIR_FLAG) + c_name);
                                                            Files.copy(c_path, new_keyStrore_file, StandardCopyOption.REPLACE_EXISTING);
                                                            keystore_file = new_keyStrore_file.toString();
                                                        } else if (server_cer_file == null && c_name.equals("server.cer")) {
                                                            Path target_path = Paths.get(instruct_map.get(SERVER_CONF_DIR_FLAG) + c_name);
                                                            Files.copy(c_path, target_path, StandardCopyOption.REPLACE_EXISTING);
                                                            server_cer_file = target_path.toString();
                                                        } else if (c_name.equals(GMAIL_CERTIGFICATE_REFRESHER)) {
                                                            Files.copy(c_path, Paths.get(instruct_map.get(SERVER_CONF_DIR_FLAG) + c_name), StandardCopyOption.REPLACE_EXISTING);
                                                        } else if (c_name.equals(CMD_TAR_NAME)) {
                                                            Files.copy(c_path, Paths.get(instruct_map.get(SERVER_DOCROOT_DIR_FLAG) + c_name), StandardCopyOption.REPLACE_EXISTING);
                                                            instruct_map.put(CMD_TAR_LOC_FLAG, instruct_map.get(SERVER_ROOT_FLAG) + c_name);
                                                            docrooted = true;
                                                        } else if (c_name.equals(CMD_ZIP_NAME)) {
                                                            Files.copy(c_path, Paths.get(instruct_map.get(SERVER_DOCROOT_DIR_FLAG) + c_name), StandardCopyOption.REPLACE_EXISTING);
                                                            instruct_map.put(CMD_ZIP_LOC_FLAG, instruct_map.get(SERVER_ROOT_FLAG) + c_name);
                                                            docrooted = true;
                                                        } else if (c_name.equals(CMD_JAR_NAME) && !cmd_found) {
                                                            Files.copy(c_path, Paths.get(instruct_map.get(SERVER_DOCROOT_DIR_FLAG) + c_name), StandardCopyOption.REPLACE_EXISTING);
                                                            instruct_map.put(CMD_JAR_LOC_FLAG, instruct_map.get(SERVER_ROOT_FLAG) + c_name);
                                                            docrooted = true;
                                                        } else if (c_name.toUpperCase().endsWith(".PDF")) {
                                                            Files.copy(c_path, Paths.get(instruct_map.get(SERVER_DOCROOT_DIR_FLAG) + c_name), StandardCopyOption.REPLACE_EXISTING);
                                                            docrooted = true;
                                                        } else if (c_name.equals(PROCEDURE_JAR_NAME)) {
                                                            instruct_map.put(PROCEDURE_JAr_LOC_FLAG, c_path.toAbsolutePath().toString());
                                                        } else if (c_name.equals("wstx-services.war")) {
                                                            if (instruct_map.get(SERVER_METRO_DIR) != null) {
                                                                System.out.println("821 Copying to " + instruct_map.get(SERVER_METRO_DIR) + c_name);
                                                                Files.copy(c_path, Paths.get(instruct_map.get(SERVER_METRO_DIR) + c_name), StandardCopyOption.REPLACE_EXISTING);
                                                            }
                                                        } else if (c_name.equals(EGENR_FLAG) && !egenr_found) {
                                                            Files.copy(c_path, Paths.get(instruct_map.get(SERVER_DOCROOT_DIR_FLAG) + c_name), StandardCopyOption.REPLACE_EXISTING);
                                                            instruct_map.put(EGENR_LOC_FLAG, instruct_map.get(SERVER_ROOT_FLAG) + c_name);
                                                            egenr_found = true;
                                                        }
//                                                        else if (c_name.equals(EGENPY_FLAG) && !egenpy_found) {
//                                                            Files.copy(c_path, Paths.get(instruct_map.get(SERVER_DOCROOT_DIR_FLAG) + c_name), StandardCopyOption.REPLACE_EXISTING);
//                                                            instruct_map.put(EGENPY_LOC_FLAG, instruct_map.get(SERVER_ROOT_FLAG) + c_name);
//                                                            egenpy_found = true;
                                                        // }
                                                        if (docrooted) {
                                                            try {
                                                                Files.setPosixFilePermissions(Paths.get(instruct_map.get(SERVER_DOCROOT_DIR_FLAG) + c_name), perms);
                                                            } catch (Exception ex) {
                                                                System.out.println("Error setting permission" + ex.getMessage());
                                                            }

                                                        }
                                                    }
                                                }
                                                ds.close();
                                                Path out = Paths.get(instruct_map.get(SERVER_DOCROOT_DIR_FLAG)).resolve(EGENPY_FLAG);
                                                out.toFile().deleteOnExit();
                                                Files.createFile(out);
                                                try {
                                                    FileWriter writer = new FileWriter(out.toFile());
                                                    PrintWriter printWriter = new PrintWriter(writer);
                                                    printWriter.append(Scripts.getPython(instruct_map.get(WSDL_URL_FLAG)));
                                                    printWriter.close();
                                                    writer.close();
                                                    egenpy_found = true;
                                                    instruct_map.put(EGENPY_LOC_FLAG, instruct_map.get(SERVER_ROOT_FLAG) + EGENPY_FLAG);
                                                } catch (IOException ex) {
                                                    System.out.println("Error :creating creating python script");
                                                }
                                            } catch (IOException e) {
                                                e.printStackTrace();
                                            }
                                            System.out.println("server_cer_found=" + server_cer_file);
                                            System.out.println("keystore_found=" + keystore_file);
                                            System.out.println("cacerts_jks_found=" + cacerts_jks_file);
                                            if (server_cer_file != null && keystore_file != null && cacerts_jks_file != null) {
                                                try {
                                                    System.setProperty("javax.net.ssl.keyStore", cacerts_jks_file);
                                                    System.setProperty("javax.net.ssl.trustStore", keystore_file);
                                                    System.setProperty("javax.net.ssl.trustStorePassword", "changeit");
                                                    System.setProperty("javax.net.ssl.keyStorePassword", "changeit");
                                                    instruct_map.put(TRUST_STORE_FILE_NM, cacerts_jks_file);
                                                    if (recreating_mail_cert) {
                                                        createCertificate(cacerts_jks_file, instruct_map.get("--mailhost"), "changeit".toCharArray());
                                                    }
                                                    FileWriter out = new FileWriter(instruct_map.get(SERVER_CONF_DIR_FLAG) + "login.confg", false);
                                                    out.append(login_config_info);
                                                    out.close();
                                                } catch (IOException e) {
                                                    e.printStackTrace();
                                                }

                                                if (instruct_map.get(PROCEDURE_JAr_LOC_FLAG) == null) {
                                                    System.out.println("Error 2b: the procedure file required for user management was not found (" + PROCEDURE_JAR_NAME + ")");
                                                    ProgressMonitor.cancel();
                                                } else if (nomail) {
                                                    System.out.println("Warning! Not setting up an email account. User regsitration will fail. Server configurations will not be mailed to the administrator.");
                                                    ProgressMonitor.cancel();
                                                    init_result = true;
                                                } else {
                                                    ProgressMonitor.cancel();
                                                    if (!fromlast) {
                                                        System.out.println(printformater_terminal("\nThe eGenVar system is setting up a "
                                                                + "email account. We recommend to use a gmail (Google) account "
                                                                + "for this. If you do not have one please create before proceeding "
                                                                + "(https://accounts.google.com/SignUp?service=mail) You could manually "
                                                                + "provide configuration for other email accounts in " + instruct_map.get(CONFIG_FILE_FLAG) + " "
                                                                + "file. However,  default settings will only work with a gmail account\n", 100));
                                                        String ans1 = getuserInputSameLine("Do you want to use either Gmail or mail"
                                                                + " configuration provided in the config file?", "[Y/N]");
                                                        if (analyseUserResponse(ans1, false, false, null) == 0) {
                                                        } else {
                                                            String ans0 = getuserInputSameLine("Do you want to try using the system mail on localhost", "[Y | N]");
                                                            if (ans0.equalsIgnoreCase("Y")) {
                                                                instruct_map.put(USE_LOCAL_HOST_FOR_MAIL, "TRUE");
                                                                instruct_map.put("--mailhost", "localhost");
                                                                instruct_map.put("mail.smtp.port", "25");
                                                                instruct_map.put("--mailuser", null);
                                                                instruct_map.put("mail.smtp.password", null);
                                                                instruct_map.put("--fromaddress", "localhost");
                                                                init_result = true;
                                                                ProgressMonitor.cancel();
                                                            } else {
                                                                System.out.println("Please use a config file to sepecify mail"
                                                                        + " acounts other than gmail. Refer the advanced section "
                                                                        + "of the admin manual for more details");
                                                            }
                                                        }
                                                    }
                                                    if (instruct_map.get("--fromaddress") == null || instruct_map.get("--fromaddress").isEmpty()
                                                            || !instruct_map.get("--fromaddress").matches("[A-z0-9_\\-\\.]+@[A-z0-9_\\-]+[\\.]{1}[A-z0-9_\\-\\.]+")) {
                                                        boolean ok = false;
                                                        while (!ok) {
                                                            String ans2 = getuserInputSameLine("Email address (C -skip this)", "");
                                                            if (analyseUserResponse(ans2, true, true, null) == 5) {
                                                                ok = true;
                                                                System.exit(1);
                                                            } else if (analyseUserResponse(ans2, true, true,
                                                                    "[A-z0-9_\\-\\.]+@[A-z0-9_\\-]+[\\.]{1}[A-z0-9_\\-\\.]+") == 1) {
                                                                ok = true;
                                                                instruct_map.put("--fromaddress", ans2);
                                                            }
                                                        }

                                                    }
                                                    if (instruct_map.get("--mailuser") == null || instruct_map.get("--mailuser").isEmpty()) {
                                                        boolean ok = false;
                                                        while (!ok) {
                                                            String ans = getuserInputSameLine("User name for "
                                                                    + instruct_map.get("--fromaddress") + "(C -skip this)", "");
                                                            if (analyseUserResponse(ans, true, true, null) == 1) {
                                                                ok = true;
                                                                instruct_map.put("--mailuser", ans);
                                                            } else if (analyseUserResponse(ans, true, true, null) == 5) {
                                                                ok = true;
                                                                System.exit(1);
                                                            }
                                                        }
                                                    }

                                                    if (instruct_map.get("mail.smtp.password") == null || instruct_map.get("mail.smtp.password").isEmpty()) {
                                                        System.out.println("Please enter password for " + instruct_map.get("--mailuser"));
                                                        boolean ok = false;
                                                        String password = "";
                                                        Console cons = System.console();
                                                        if (cons != null) {
                                                            char[] pass = cons.readPassword();
                                                            System.out.println("");
                                                            for (int i = 0; i < pass.length; i++) {
                                                                password = password + pass[i];
                                                            }
                                                        } else {
                                                            password = JOptionPane.showInputDialog("Please enter password for " + instruct_map.get("--mailuser"));
                                                        }
                                                        instruct_map.put("mail.smtp.password", password);
                                                    }
                                                    create_mailer_a = new String[6];
                                                    create_mailer_a[0] = "--fromaddress=" + instruct_map.get("--fromaddress");
                                                    create_mailer_a[1] = "--mailuser=" + instruct_map.get("--mailuser");
                                                    create_mailer_a[2] = "--mailhost=" + instruct_map.get("--mailhost");
                                                    create_mailer_a[3] = "--property";
                                                    if (recreating_mail_cert) {
                                                        createCertificate(cacerts_jks_file, instruct_map.get("--mailhost"), "changeit".toCharArray());
                                                    }

                                                    String mail_propaties = "";
                                                    if (instruct_map.containsKey("mail.smtp.socketFactory.class")) {
                                                        mail_propaties = "mail.smtp.socketFactory.class=" + instruct_map.get("mail.smtp.socketFactory.class") + ":";
                                                    }

                                                    if (instruct_map.containsKey("mail.smtp.socketFactory.class")) {
                                                        mail_propaties = mail_propaties + "mail.smtp.socketFactory.class=" + instruct_map.get("mail.smtp.socketFactory.class") + ":";
                                                    }
                                                    if (instruct_map.containsKey("mail.smtp.auth")) {
                                                        mail_propaties = mail_propaties + "mail.smtp.auth=" + instruct_map.get("mail.smtp.auth") + ":";
                                                    }
                                                    if (instruct_map.containsKey("mail.smtp.socketFactory.fallback")) {
                                                        mail_propaties = mail_propaties + "mail.smtp.socketFactory.fallback=" + instruct_map.get("mail.smtp.socketFactory.fallback") + ":";
                                                    }
                                                    if (instruct_map.containsKey("mail.smtp.port")) {
                                                        mail_propaties = mail_propaties + "mail.smtp.port=" + instruct_map.get("mail.smtp.port") + ":";
                                                    }
                                                    if (instruct_map.containsKey("mail.smtp.socketFactory.port")) {
                                                        mail_propaties = mail_propaties + "mail.smtp.socketFactory.port=" + instruct_map.get("mail.smtp.socketFactory.port") + ":";
                                                    }
                                                    if (instruct_map.containsKey("mail.smtp.password")) {
                                                        mail_propaties = mail_propaties + "mail.smtp.password=" + instruct_map.get("mail.smtp.password") + ":";
                                                    }
                                                    create_mailer_a[4] = mail_propaties;
                                                    create_mailer_a[5] = MAIL_SOURCE_NAME;
                                                    init_result = true;
                                                    ProgressMonitor.cancel();
                                                }
                                            } else {
                                                System.out.println("Error 1030: Certificate creation failed");
                                                ProgressMonitor.cancel();
                                            }
                                        }
                                    } else {
                                        System.out.println("Error: creating server docroot directory failed, try creating this manually with write access" + instruct_map.get(SERVER_DOCROOT_DIR_FLAG));
                                        ProgressMonitor.cancel();
                                    }

                                } else {
                                    System.out.println("Error: creating server lib directory failed, try creating this manually with write access" + instruct_map.get(SERVER_LIB_DIR_FLAG));
                                    ProgressMonitor.cancel();
                                }
                            } else {
                                System.out.println("Error: creating server lib directory failed, try creating this manually with write access" + instruct_map.get(SERVER_CONF_DIR_FLAG));
                                ProgressMonitor.cancel();
                            }

                        }

                    } else {
                        System.out.println("Error: can not access source directory. Provide this value in the config file with SRC_ROOT flag");
                        ProgressMonitor.cancel();

                    }

                } else {
                    System.out.println("Error: failed to find, source using classpath " + classpath + ". Provide this value in the config file with SRC_ROOT flag or remove that value from the config file to use the default location");
                    ProgressMonitor.cancel();
                }
            } else {
                System.out.println("Error: Failed to load configuration settings");
                ProgressMonitor.cancel();
            }
        }
        System.out.println("Source root=" + instruct_map.get(SRC_ROOT_FLAG));
        System.out.println("Server root= " + instruct_map.get(SERVER_INSTALATION_ROOT_FLAG));
        ProgressMonitor.cancel();
        return init_result;
    }

    /*
     MethodID=5
     */
    private boolean init_port(String config_file_nm, boolean find_free_port) {
        boolean allports_ok = false;
        String error_msg = "  -this port is occupied. Please change the port in the config file or stop the application using this port. "
                + "Also check whether another instance of egenv server is running. Use <egenv_server -stop instance_id> to stop other instances of egenv server";
        if (instruct_map.get(SERVER_SECURE_PORT_FLAG).matches("818[0-9]{1}")) {
            if (isPortAvailable(new Integer(instruct_map.get(SERVER_SECURE_PORT_FLAG)))) {
                allports_ok = true;
                System.out.println("Port " + instruct_map.get(SERVER_SECURE_PORT_FLAG) + " --free.");
            } else {
                System.out.println("Port:" + instruct_map.get(SERVER_SECURE_PORT_FLAG) + " occupied, looking for alternatives");
//                if (find_free_port) {
                int new_port = findFreePOrt(8180, 8189);
                if (new_port > 0) {
                    System.out.println("The port " + instruct_map.get(SERVER_SECURE_PORT_FLAG) + " was not available, using " + new_port + " instead");
                    instruct_map.put(SERVER_SECURE_PORT_FLAG, new_port + "");
                    allports_ok = true;
                } else {
                    System.out.println("Error: failed to find a free port in the range 8180 - 8189, please free a port in this range and try again");
                    allports_ok = false;
                }
//                } else {
//                    System.out.println(printformater_terminal("Error 5a:" + instruct_map.get(SERVER_SECURE_PORT_FLAG) + error_msg, 80) + "\n Use -find_free_port to scan for free ports");
//                    allports_ok = false;
//                }

            }
        } else {
            System.out.println("Invalid port " + instruct_map.get(SERVER_SECURE_PORT_FLAG) + ". Valid ports for " + SERVER_SECURE_PORT_FLAG + " from 8180 to 8189 Correct this in " + config_file_nm);
        }
        if (instruct_map.get(SERVER_PORT_FLAG).matches("808[0-9]{1}")) {
            if (isPortAvailable(new Integer(instruct_map.get(SERVER_PORT_FLAG)))) {
                allports_ok = true;
                System.out.println("Port " + instruct_map.get(SERVER_PORT_FLAG) + " -- free.");
            } else {
//                if (find_free_port) {
                System.out.println("Port:" + instruct_map.get(SERVER_PORT_FLAG) + " occupied, looking for alternatives");
                int new_port = findFreePOrt(8080, 8089);
                if (new_port > 0) {
                    System.out.println("The port " + instruct_map.get(SERVER_PORT_FLAG) + " was not available, using " + new_port + " instead");
                    instruct_map.put(SERVER_PORT_FLAG, new_port + "");
                    allports_ok = true;
                } else {
                    System.out.println("Error: failed to find a free port in the range 8080 - 8089, please free a port in this range and try again");
                    allports_ok = false;
                }
//                } else {
//                    System.out.println(printformater_terminal("Error 5b:" + instruct_map.get((SERVER_PORT_FLAG)) + error_msg, 80) + "\n Use -find_free_port to scan for free ports");
//                    allports_ok = false;
//                }
            }
        } else {
            System.out.println("Invalid port " + instruct_map.get(SERVER_PORT_FLAG) + ". Valid ports for " + SERVER_PORT_FLAG + " from 8080 to 8089 Correct this in " + config_file_nm);
        }
        if (instruct_map.get(derby_port_flag).matches("155[0-9]{1}")) {
            if (isPortAvailable(new Integer(instruct_map.get(derby_port_flag)))) {
                allports_ok = true;
                System.out.println("Port " + instruct_map.get(derby_port_flag) + " -- free.");
            } else {
                System.out.println("Port:" + instruct_map.get(derby_port_flag) + " occupied, looking for alternatives");
//                if (find_free_port) {
                int new_port = findFreePOrt(1550, 1559);
                if (new_port > 0) {
                    System.out.println("The port " + instruct_map.get(derby_port_flag) + " was not available, using " + new_port + " instead");
                    instruct_map.put(derby_port_flag, new_port + "");
                    allports_ok = true;
                } else {
                    System.out.println("Error: failed to find a free port in the range 1550 - 1559, please free a port in this range and try again");
                    allports_ok = false;
                }
//                } else {
//                    System.out.println(printformater_terminal("Error 5c:" + instruct_map.get(derby_port_flag) + error_msg, 80) + "\n Use -find_free_port to scan for free ports");
//                    allports_ok = false;
//                }
            }
        } else {
            System.out.println("Invalid port " + instruct_map.get(derby_port_flag) + ". Valid ports for " + derby_port_flag + " from 1550 to 1559 Correct this in " + config_file_nm);
        }

        if (instruct_map.get(derby_port_MEM_flag) == null) {
            instruct_map.put(derby_port_MEM_flag, "1567");
        }
        if (instruct_map.get(derby_port_MEM_flag).matches("156[0-9]{1}")) {
            if (isPortAvailable(new Integer(instruct_map.get(derby_port_MEM_flag)))) {
                allports_ok = true;
                System.out.println("Port " + instruct_map.get(derby_port_MEM_flag) + " -- free.");
            } else {
                System.out.println("Port:" + instruct_map.get(derby_port_MEM_flag) + " occupied, looking for alternatives");
//                if (find_free_port) {
                int new_port = findFreePOrt(1560, 1569);
                if (new_port > 0) {
                    System.out.println("The port " + instruct_map.get(derby_port_MEM_flag) + " was not available, using " + new_port + " instead");
                    instruct_map.put(derby_port_MEM_flag, new_port + "");
                    allports_ok = true;
                } else {
                    System.out.println("Error: failed to find a free port in the range 1550 - 1559, please free a port in this range and try again");
                    allports_ok = false;
                }
//                } else {
//                    System.out.println(printformater_terminal("Error 5c:" + instruct_map.get(derby_port_MEM_flag) + error_msg, 80) + "\n Use -find_free_port to scan for free ports");
//                    allports_ok = false;
//                }
            }
        } else {
            System.out.println("Invalid port " + instruct_map.get(derby_port_MEM_flag) + ". Valid ports for " + derby_port_flag + " from 1550 to 1559 Correct this in " + config_file_nm);
        }

        return allports_ok;
    }

    /*
     *  MethodID=1
     */
    private void commit(String loglevel_nm, boolean no_mail,
            boolean forcelocalhost, boolean recreate_Json, boolean overide_restart_schedule,
            boolean recreate_tag_dict) {
        long mem = Runtime.getRuntime().freeMemory();
        if (mem < MIN_MEM) {
            System.out.println("Error 1a: available memory too low (requires atleast " + (MIN_MEM / (1048576)) + ")");
        }
        System.out.println("\n Free memory=" + (mem / (1048576)) + " Mb");
        System.out.println(" MIN =" + (MIN_MEM / (1048576)));
        Level loglevel = null;
        if (loglevel_nm != null) {
            try {
                loglevel = Level.parse(loglevel_nm);
            } catch (IllegalArgumentException ex) {
                loglevel = null;
            }
        }
        if (loglevel == null) {
            loglevel = Level.WARNING;
        }
        try {
            boolean uselocalhostformail = false;
            if (instruct_map.get("USE_LOCAL_HOST_FOR_MAIL") != null && instruct_map.get("USE_LOCAL_HOST_FOR_MAIL").equalsIgnoreCase("TRUE")) {
                uselocalhostformail = true;
            }
//            Path config_loc = Paths.get(instruct_map.get(SERVER_INSTALATION_ROOT_FLAG) + "config" + File.separator);
//            if (!Files.exists(config_loc)) {
//                try {
//                    Files.createDirectories(config_loc);
//                } catch (IOException ex) {
//                    System.out.println("Error : permission error :" + ex.getMessage() + "\n I could not rectify this automatically. Please create this directory manually and try again");
//                }
//            }
            BootstrapProperties bootstrapProperties = new BootstrapProperties();
            GlassFishProperties glassfishProperties = new GlassFishProperties();
            glassfishProperties.setPort("http-listener", new Integer(instruct_map.get(SERVER_PORT_FLAG)));
            glassfishProperties.setPort("https-listener", new Integer(instruct_map.get(SERVER_SECURE_PORT_FLAG)));
            glassfishProperties.setInstanceRoot(instruct_map.get(SERVER_INSTALATION_ROOT_FLAG));
            GlassFishRuntime glassfishRuntime = GlassFishRuntime.bootstrap(bootstrapProperties);
            GlassFish glassfish = glassfishRuntime.newGlassFish(glassfishProperties);

            System.setProperty("java.security.auth.login.config", System.getProperty("com.sun.aas.instanceRoot") + File.separator + "config" + File.separator + "login.confg");
            System.setProperty("java.awt.headless", "true");
            Logger.getLogger("").getHandlers()[0].setLevel(loglevel);
            Logger.getLogger("javax.enterprise.system.tools.deployment").setLevel(loglevel);
            Logger.getLogger("javax.enterprise.system").setLevel(loglevel);
            System.out.println("Starting application server port=" + instruct_map.get(SERVER_PORT_FLAG) + ": secure_port=" + instruct_map.get(SERVER_SECURE_PORT_FLAG));
            System.out.println("Server root=" + instruct_map.get(SERVER_INSTALATION_ROOT_FLAG));
            glassfish.start();
            System.out.println("Server started @" + Timing.getDateTime());
            System.out.println("Starting database server.." + glassfish.getStatus());
            mem = Runtime.getRuntime().freeMemory();
            if (mem < MIN_MEM) {
                System.out.println("Error 1b: available memory too low (requires atleast " + (MIN_MEM / (1048576)) + ")");
            }
            if (no_mail || test_mail(uselocalhostformail)) {
                NetworkServerControl db_server = startDerby(instruct_map.get(DB_LOG_FILENM_FLAG), false);
                if (db_server != null) {
                    System.out.println("Database server started @" + Timing.getDateTime());
                    (new Thread(new ProgressMonitor(PROGRESS_MONT_INTERVAL, "15"))).start();
                    System.out.println("Setting up database user authentication");
                    if (setAuthentication(instruct_map)) {
                        System.out.println("Creating user management");
                        createuserTable_usermanagement();
                        installProcedurejar();
                        register_Procedures();
                        ProgressMonitor.cancel();
                        mem = Runtime.getRuntime().freeMemory();
                        if (mem < MIN_MEM) {
                            System.out.println("Error 1c: available memory too low (requires atleast " + (MIN_MEM / (1048576)) + ")");
                        }
                        (new Thread(new ProgressMonitor(PROGRESS_MONT_INTERVAL, "15"))).start();
                        if (createTablesFromFile(getConnection(), instruct_map.get(CREATE_DB_FILE_DERBY_FLAG),
                                verbose, instruct_map.get(DATABASE_NAME_DATA_FLAG)) >= 0) {
                            System.out.println("Checking  integrity");
                            ArrayList<String> result_l = executeQuery(getConnection(), "select distinct table_name from  " + instruct_map.get(DATABASE_NAME_DATA_FLAG) + ".tablename2feature where showinsearch=1");
                            if (!result_l.isEmpty()) {
                                ProgressMonitor.cancel();
                                if (instruct_map.containsKey(SOURCE_DATA_DIR_FLAG)
                                        && instruct_map.get(SOURCE_DATA_DIR_FLAG) != null
                                        && !instruct_map.get(SOURCE_DATA_DIR_FLAG).isEmpty()) {
                                    String data_folder_nm = instruct_map.get(SOURCE_DATA_DIR_FLAG);
                                    File data_folder = new File(data_folder_nm);
                                    if (data_folder.isDirectory() && data_folder.canRead()) {
                                        File[] data_file_a = data_folder.listFiles();
                                        for (int i = 0; i < data_file_a.length; i++) {
                                            if (data_file_a[i].isFile() && data_file_a[i].canRead()) {
                                                String data_file_nm = data_file_a[i].getName();
                                                if (data_file_nm.toLowerCase().contains(TAGSOURCE_FLAG)) {
                                                    ArrayList<String> create_sql_L = crateTagFromFile(getConnection(), data_file_a[i], instruct_map.get(DATABASE_NAME_DATA_FLAG), null);
                                                } else {
                                                    (new Thread(new ProgressMonitor(10000, "15"))).start();
                                                    ArrayList<String> create_sql_L = addDataFromFile(getConnection(), data_file_a[i], instruct_map.get(DATABASE_NAME_DATA_FLAG));
                                                    ProgressMonitor.cancel();
                                                }
                                            }
                                        }
                                        if (recreate_tag_dict || new_tag_created) {
                                            getAllTags(getConnection(), instruct_map.get(DATABASE_NAME_DATA_FLAG));
                                        }
                                    }
                                    boolean table_ok = index_tagsources(getConnection(), instruct_map.get(DATABASE_NAME_DATA_FLAG)); //true;//
                                    if (table_ok) {
                                        System.out.println("Indexing tag sources was a success");
                                    } else {
                                        System.out.println("\nError: Indexing tag sources failed");
                                    }
                                }
                                System.out.println("Refresh available tables result=" + refreshTableTOFeatures(getConnection(), instruct_map.get(DATABASE_NAME_DATA_FLAG)));
                                HashMap<String, String> acouunt_map = getFirstUseraccount(forcelocalhost);
                                mem = Runtime.getRuntime().freeMemory();
                                if (mem < MIN_MEM) {
                                    System.out.println("Error 1d: available memory too low (requires atleast " + (MIN_MEM / (1048576)) + ")");
                                }
                                setServerIdentity(getConnection(), instruct_map.get(DATABASE_NAME_DATA_FLAG));
                                createQueryExpansion(getConnection(), instruct_map.get(DATABASE_NAME_DATA_FLAG));
                                if (acouunt_map != null) {
                                    (new Thread(new ProgressMonitor(PROGRESS_MONT_INTERVAL, "15"))).start();
                                    if (instruct_map.get(derby_port_MEM_flag) == null) {
                                        instruct_map.put(derby_port_MEM_flag, "1567");
                                    }
                                    if (create_user_connections_derby(glassfish)) {
                                        System.out.println("User management activated");
                                        if (create_dataEntry_connections_derby(glassfish)) {
                                            System.out.println("Dataentry connection acitvated");
                                            HashMap<String, String> config_map = new HashMap<>();//                                            ll;
                                            config_map.put(SERVER_DOCROOT_DIR_FLAG, instruct_map.get(SERVER_DOCROOT_DIR_FLAG));
                                            config_map.put(SERVER_ROOT_FLAG, instruct_map.get(SERVER_ROOT_FLAG));
                                            config_map.put(SERVER_NAME_FLAG, instruct_map.get(SERVER_NAME_FLAG));
                                            config_map.put(S_SERVER_ROOT_FLAG, instruct_map.get(S_SERVER_ROOT_FLAG));
                                            config_map.put(S_SERVER_NAME_FLAG, instruct_map.get(S_SERVER_NAME_FLAG));
                                            config_map.put(SERVER_ID_FLAG, instruct_map.get(SERVER_ID_FLAG));
                                            config_map.put(WSDL_URL_FLAG, instruct_map.get(WSDL_URL_FLAG));
                                            config_map.put(S_WSDL_URL_FLAG, instruct_map.get(S_WSDL_URL_FLAG));
                                            config_map.put(WSDL_URL_FLAG, instruct_map.get(WSDL_URL_FLAG));
                                            config_map.put(S_WSDL_URL_FLAG, instruct_map.get(S_WSDL_URL_FLAG));
                                            config_map.put(CMD_TAR_LOC_FLAG, instruct_map.get(CMD_TAR_LOC_FLAG));
                                            config_map.put(CMD_ZIP_LOC_FLAG, instruct_map.get(CMD_ZIP_LOC_FLAG));
                                            config_map.put(CMD_JAR_LOC_FLAG, instruct_map.get(CMD_JAR_LOC_FLAG));
                                            config_map.put(EGENR_LOC_FLAG, instruct_map.get(EGENR_LOC_FLAG));
                                            config_map.put(EGENPY_LOC_FLAG, instruct_map.get(EGENPY_LOC_FLAG));
                                            config_map.put(SERVER_DOC_ROOT_FLAG, instruct_map.get(SERVER_DOC_ROOT_FLAG));
                                            config_map.put(SERVER_BASE_FLAG, instruct_map.get(SERVER_BASE_FLAG));
                                            config_map.put(SERVER_TITLE, instruct_map.get(SERVER_TITLE));
                                            config_map.put(COMMAND_LINE_TOOL_VERSION_FLAG, COMMAND_LINE_TOOL_VERSION);
                                            config_map.put(WEB_INTERFACE_VERSION_FLAG, WEB_INTERFACE_VERSION);
                                            config_map.put(WEB_SERVICE_VERSION_FLAG, WEB_SERVICE_VERSION);
                                            config_map.put(MODE_FLAG, current_mode + "");
                                            config_map.put(BAKSUP_INSTRUCT_FLAG, instruct_map.get(BAKSUP_INSTRUCT_FLAG));
                                            config_map.put(ADMIN_EMAIL_FLAG, instruct_map.get("--fromaddress"));

                                            if (setConfig(getConnection(), config_map, instruct_map.get(DATABASE_NAME_DATA_FLAG)) < 0) {
                                                System.out.println("Error 2e: Failed to save configuration information");
                                            }
                                            /* 
                                             Place a welcome and redict page here.
                                             File web = new File("/home/sabryr/NetBeansProjects/eGenVar_web/dist/eGenVar_web.war");
                                             deployer.deploy(web, "--name=eGenVar_web", "--contextroot=/", "--force=true");//eGenVar_web                              
                                             **/
                                            mem = Runtime.getRuntime().freeMemory();
                                            if (mem < MIN_MEM) {
                                                System.out.println("Error 1f: available memory too low (requires atleast " + (MIN_MEM / (1048576)) + ")");
                                            }
                                            boolean memorised = false;
                                            System.out.println("Free MEM=" + (mem / 11048576) + " Mb");
                                            if (mem > MIN_MEM_MEMDB) {
                                                dbURL_dataEntry_memory = "jdbc:" + instruct_map.get(db_host_flag) + ":" + instruct_map.get(derby_port_MEM_flag) + "/memory:" + instruct_map.get(DATABASE_NAME_DATA_FLAG) + ";restoreFrom=" + getDataEntryDb_loc().toString() + ";" + instruct_map.get(DERBY_CONNECTION_PARAM) + ";user=" + instruct_map.get(unm_gendataentry_flag) + ";password=" + instruct_map.get(unm_gendataentry_pass_flag);
                                                dbURL_dataEntry_memory_drop = "jdbc:" + instruct_map.get(db_host_flag) + ":" + instruct_map.get(derby_port_MEM_flag) + "/memory:" + instruct_map.get(DATABASE_NAME_DATA_FLAG) + ";shutdown=true;" + instruct_map.get(DERBY_CONNECTION_PARAM) + ";user=" + instruct_map.get(unm_gendataentry_flag) + ";password=" + instruct_map.get(unm_gendataentry_pass_flag);
                                                NetworkServerControl db_server2 = startDerby(instruct_map.get(DB_LOG_FILENM_FLAG), true);
                                                if (replicateDb(true)) {
                                                    if (create_dataEntry_connections_derby_memory(glassfish)) {
                                                        memorised = true;
                                                        System.out.println("\nIn-memory replication sucess..\n");
                                                    }
                                                }
                                            }

                                            if (!memorised) {
                                                System.out.println("\nIn-memory replication failed and using disk instead (reason: not enough free memory)..\n");
                                                memorised = create_dataEntry_connections_derby_disk(glassfish);
                                            }
                                            if (memorised) {
                                                diploy_services(glassfish);
                                            }

                                        }
                                    }


                                    mem = Runtime.getRuntime().freeMemory();
                                    if (mem < MIN_MEM) {
                                        System.out.println("Error 1h: available memory too low (requires atleast " + (MIN_MEM / (1048576)) + ")");
                                    }
                                    String mail_subject = "egenvar admin account information";
                                    String mail_message = "Instalation on " + instruct_map.get(SERVER_ROOT_FLAG) + " was successful\nPlease keep the following configuration details for future reference"
                                            + "\n";
                                    mail_message = mail_message + "\nEncription key:" + encript_password + ". This is very important!."
                                            + " You will require this key to recover configuration options including passwords. If you loose this, there is NO way to recover passwords. (everything else below can be recoverd using this))\n\n";

                                    ArrayList<String> config_key_l = new ArrayList<String>(instruct_map.keySet());
                                    for (int i = 0; i < config_key_l.size(); i++) {
                                        if (!config_key_l.get(i).equalsIgnoreCase("mail.smtp.password")) {
                                            mail_message = mail_message + "\n" + config_key_l.get(i) + "=" + instruct_map.get(config_key_l.get(i));
                                        } else {
                                        }
                                    }
                                    mail_message = mail_message + "\n The server identifier :" + instruct_map.get(SERVER_ID_FLAG);
                                    mail_message = mail_message + "\n\n The web interface is accessible from :" + instruct_map.get(S_SERVER_NAME_FLAG);
                                    mail_message = mail_message + "\n The WSDL for the web service :" + instruct_map.get(WSDL_URL_FLAG);

                                    if (no_mail) {
                                        writeResultsToFileEncripted(instruct_map, LAST_CONFIG_DETAILS_FILE_FLAG);
                                        status(glassfish, db_server, overide_restart_schedule, recreate_Json);
                                    } else if (instruct_map.get("--fromaddress") != null
                                            && sendEmail(uselocalhostformail, instruct_map.get("--mailhost"), new Integer(instruct_map.get("mail.smtp.port")),
                                            instruct_map.get("--mailuser"), instruct_map.get("mail.smtp.password"),
                                            mail_subject, mail_message, instruct_map.get("--fromaddress"), instruct_map.get(TRUST_STORE_FILE_NM), "changeit")) {
                                        if (!acouunt_map.isEmpty()) {
                                            mail_subject = "egenvar user account information";
                                            mail_message = "welcome to egenvar data management service";
                                            ArrayList<String> acouunt_map_l = new ArrayList<>(acouunt_map.keySet());
                                            for (int i = 0; i < acouunt_map_l.size(); i++) {
                                                mail_message = mail_message + "\n" + acouunt_map_l.get(i) + "=" + acouunt_map.get(acouunt_map_l.get(i));
                                            }
                                            mail_message = mail_message + "\n WSDL url for the egenvar tool: " + instruct_map.get(WSDL_URL_FLAG) + "\n"
                                                    + "Web interface URL: " + instruct_map.get(S_SERVER_NAME_FLAG);
                                            sendEmail(uselocalhostformail, instruct_map.get("--mailhost"),
                                                    new Integer(instruct_map.get("mail.smtp.port")),
                                                    instruct_map.get("--mailuser"),
                                                    instruct_map.get("mail.smtp.password"),
                                                    mail_subject, mail_message, acouunt_map.get("email"), instruct_map.get(TRUST_STORE_FILE_NM), "changeit");
                                        }
                                        writeResultsToFileEncripted(instruct_map, LAST_CONFIG_DETAILS_FILE_FLAG);
                                        mem = Runtime.getRuntime().freeMemory();
                                        if (mem < MIN_MEM) {
                                            System.out.println("Error 1g: available memory too low (requires atleast " + (MIN_MEM / (1048576)) + ")");
                                        } else {
                                            System.out.println("Warning memory too low, In-memory database not creating. Disk database wiill be used and operations will be slower");
                                        }
                                        status(glassfish, db_server, overide_restart_schedule, recreate_Json);
                                        System.out.println("2106");
                                    } else {
                                        System.out.println("Error 1a: the email account settings were incorrect or the account is not accessible."
                                                + "\nPlease correct this in the config file " + instruct_map.get(CONFIG_FILE_FLAG) + "\n Refer the section about certificates in the "
                                                + "adminstrator manual for more technical details ");
                                        stopDerby(new Integer(instruct_map.get(derby_port_flag)));
                                        glassfish.stop();
                                    }
                                    ProgressMonitor.cancel();
                                } else {
                                    System.out.println("Error 1c: Creating tables failed, Check the config file " + instruct_map.get(CREATE_DB_FILE_DERBY_FLAG) + " for errors");
                                    stopDerby(new Integer(instruct_map.get(derby_port_flag)));
                                    glassfish.stop();
                                }
                            } else {
                                System.out.println("Error 1f: Accessing user accounts failed, Check the config file and run the process again");
                                System.exit(1);
                            }
                        } else {
                            System.out.println("Error 1b: Creating tables failed, Check the config file " + instruct_map.get(CREATE_DB_FILE_DERBY_FLAG) + " for errors");
                            stopDerby(new Integer(instruct_map.get(derby_port_flag)));
                            glassfish.stop();
                        }
                    } else {
                        System.out.println("Error 1c: failed to create or access user accounts. Delete old databse (folder, eg. rm -r EGEN_USERS) before recreating");
                        stopController(glassfish, db_server);
                    }
                    ProgressMonitor.cancel();
                } else {
                    System.out.println("Error failed to start database server.");
                    try {
                        if (glassfish != null) {
                            glassfish.stop();
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            } else {
                System.out.println("Error 1e: mail set up failed. Check the user name and password is correct. \nIf you are repeatedly facing this error see the section on certificate relate issue in the admin manual for help");
                try {
                    if (glassfish != null) {
                        glassfish.stop();
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                ProgressMonitor.cancel();
            }

        } catch (GlassFishException ex) {
            ex.printStackTrace();
        }
    }

    /*
     *  MethodID=1.1
     */
    private boolean replicateDb(boolean verbose) {
        boolean result = true;
        getCurrentTables(getConnection_memory(false, 11), instruct_map.get(DATABASE_NAME_DATA_FLAG), 2);
        return result;
    }

    /*
     *  MethodID=1.2
     */
    private Path getDataEntryDb_loc() {
        Path result = null;
        try {
            String classpath = System.getProperty("java.class.path");
            if (classpath != null && !classpath.isEmpty() && classpath.endsWith("jar")) {
                try {
                    File path_file = new File(classpath);
                    classpath = path_file.getAbsoluteFile().toString();
                } catch (Exception ex) {
                    System.out.println("Warning: " + ex.getMessage());
                }
            }
            if (classpath != null) {
                Path source_path = Paths.get(classpath).getParent();
                if (Files.exists(source_path) && Files.isReadable(source_path)) {
                    if (instruct_map.containsKey(Start_EgenVar1.DATABASE_NAME_DATA_FLAG)) {
                        Path source = Paths.get(source_path.toString(), instruct_map.get(Start_EgenVar1.DATABASE_NAME_DATA_FLAG));
                        if (Files.exists(source) && Files.isDirectory(source) && Files.isReadable(source)) {
                            result = source.toAbsolutePath();
                        } else {
                            System.out.println("Error BK11e backup  data failed: : failed to get source folder " + source);
                        }

                    } else {
                        System.out.println("Error BK11d backup data failed: : failed to get source to backup");
                    }
                }
            }
        } catch (Exception ex) {
            System.out.println("Error 1.1a: Error suring in-memorising drivedb");
        }
        return result;
    }

//    /*
//     *  MethodID=1.1
//     */
//    private boolean replicateDb(boolean verbose) {
//        boolean result = false;
//        try {
//            System.out.println("2008 \n" + dbURL_dataEntry_memory + "\n");
//            try {
//                if (getConnection_memory() != null && getConnection() != null) {
//                    ArrayList<String> source_tables_l = getCurrentTables(getConnection(), instruct_map.get(DATABASE_NAME_DATA_FLAG));
//                    ArrayList<String> target_tables_l = getCurrentTables(getConnection_memory(), instruct_map.get(DATABASE_NAME_DATA_MEM_FLAG));
//                    ArrayList<String> common_tables_l = new ArrayList<>();
//                    for (int i = 0; i < source_tables_l.size(); i++) {
//                        String c_source = source_tables_l.get(i).split("[\\.]")[source_tables_l.get(i).split("[\\.]").length - 1];
//                        boolean found = false;
//                        for (int j = 0; (!found && j < target_tables_l.size()); j++) {
//                            String c_taget = target_tables_l.get(j).split("[\\.]")[target_tables_l.get(j).split("[\\.]").length - 1];
//                            if (c_source.equals(c_taget)) {
//                                common_tables_l.add(c_taget);
//                                target_tables_l.remove(j);
//                                j--;
//                                source_tables_l.remove(i);
//                                i--;
//                                found = true;
//                            }
//                        }
//                    }
//                    if (!common_tables_l.isEmpty()) {
//
//                        Statement s_s = getConnection().createStatement();
//                        Statement t_s = getConnection_memory().createStatement();
//                        for (int i = 0; i < common_tables_l.size(); i++) {
////                            String c_tbl = common_tables_l.get(i);
////                            t_s.executeQuery("DELETE FROM " + instruct_map.get(DATABASE_NAME_DATA_MEM_FLAG) + "." + c_tbl);
////                            ResultSet s_r = s_s.executeQuery("SELECT * FROM " + instruct_map.get(DATABASE_NAME_DATA_FLAG) + "." + c_tbl);
////                            int col_count = s_r.getMetaData().getColumnCount();
////                            String insert_sql = "INSERT INTO " + instruct_map.get(DATABASE_NAME_DATA_FLAG) + "." + c_tbl + "(" + s_r.getMetaData().getColumnName(1);
////                            String values = "?";
////                            for (int j = 1; j < col_count; j++) {
////                                insert_sql = insert_sql + "," + s_r.getMetaData().getColumnName(i);
////                                values = values + ",?";
////                            }
////                            s_r.getMetaData().getc
////                            insert_sql = insert_sql + ") values(" + values + ")";
////                            PreparedStatement t_p = getConnection_memory().prepareStatement(insert_sql);
////                            while (s_r.next()) {
////                                for (int j = 1; j <= col_count; j++) {
//////                                    s_r.get
////                                    
////                                }
////                            }
//                        }
//                        s_s.close();
//                        t_s.close();
//                    }
//
//                    System.out.println("2052 Common " + common_tables_l);
//                    System.out.println("2053 target_tables_l=" + target_tables_l);
//                    System.out.println("2054 source_tables_l=" + source_tables_l);
//                } else {
//                    System.out.println("Error 1.1a: error creating connection. Database not memorised");
//                }
//            } catch (Exception ex) {
//                ex.printStackTrace();
//                String error = ex.getMessage();
//                if (error.contains("password invalid")) {
//                    System.out.println("Error 1.1b: \n\n" + printformater_terminal("Error : Make sure the user name and the password is the same one used during creating this database. If you have lost the password, then use a different location to install the server or "
//                            + "delete the database folders and try again.  For password recovery please refer the administrators manual"
//                            + "The passwords are usually included in the config (e.g. " + instruct_map.get(CONFIG_FILE_FLAG) + " file and the default passwaord when non selected is 0000 (four zeros)", 80));
//                }
//                System.out.println("Error 1.1c: " + error);
//            }
//        } catch (Exception ex) {
//            System.out.println("Error 1.1d: Error suring in-memorising drivedb");
//        }
//        return result;
//    }
//    /*
//     *  MethodID=1.2
//     */
//    private Path getDataEntryDb_loc() {
//        Path result = null;
//        try {
//            String classpath = System.getProperty("java.class.path");
//            if (classpath != null && !classpath.isEmpty() && classpath.endsWith("jar")) {
//                try {
//                    File path_file = new File(classpath);
//                    classpath = path_file.getAbsoluteFile().toString();
//                } catch (Exception ex) {
//                    System.out.println("Warning: " + ex.getMessage());
//                }
//            }
//            if (classpath != null) {
//                Path source_path = Paths.get(classpath).getParent();
//                if (Files.exists(source_path) && Files.isReadable(source_path)) {
//                    if (instruct_map.containsKey(Start_EgenVar1.DATABASE_NAME_DATA_FLAG)) {
//                        Path source = Paths.get(source_path.toString(), instruct_map.get(Start_EgenVar1.DATABASE_NAME_DATA_FLAG));
//                        if (Files.exists(source) && Files.isDirectory(source) && Files.isReadable(source)) {
//                            result = source.toAbsolutePath();
//                        } else {
//                            System.out.println("Error BK11e backup  data failed: : failed to get source folder " + source);
//                        }
//
//                    } else {
//                        System.out.println("Error BK11d backup data failed: : failed to get source to backup");
//                    }
//                }
//            }
//        } catch (Exception ex) {
//            System.out.println("Error 1.1a: Error suring in-memorising drivedb");
//        }
//        return result;
//    }
//    /*
//     *  MethodID=1.3
//     */
//    private boolean test_memDb(String dbURL_dataEntry_memory) {
//        boolean result = false;
//        try {
//            try {
//                if (conn_memory == null || conn_memory.isClosed()) {
//                    Class.forName("org.apache.derby.jdbc.ClientDriver").newInstance();
//                    conn_memory = DriverManager.getConnection(dbURL_dataEntry_memory);
//                    conn_memory.createStatement().execute("CREATE TABLE " + instruct_map.get(DATABASE_NAME_DATA_FLAG) + "_MEM.TEST (ID INT)");
//                    ResultSet r_1 = conn_memory.getMetaData().getSchemas();
//                    while (r_1.next()) {
//                        for (int i = 0; i < r_1.getMetaData().getColumnCount(); i++) {
//                            System.out.print(r_1.getMetaData().getColumnName(i + 1) + "=" + r_1.getString(i + 1) + "\t");
//                        }
//                        System.out.println("");
//                    }
//
//                    ResultSet r = conn_memory.createStatement().executeQuery("select * from " + instruct_map.get(DATABASE_NAME_DATA_FLAG) + ".report_batch");
//                    while (r.next()) {
//                        System.out.println("2015 " + r.getString("name"));
//                    }
//                    result = true;
//                }
//            } catch (Exception ex) {
//                ex.printStackTrace();
//                String error = ex.getMessage();
//                if (error.contains("password invalid")) {
//                    System.out.println("\n\n" + printformater_terminal("Error : Make sure the user name and the password is the same one used during creating this database. If you have lost the password, then use a different location to install the server or "
//                            + "delete the database folders and try again.  For password recovery please refer the administrators manual"
//                            + "The passwords are usually included in the config (e.g. " + instruct_map.get(CONFIG_FILE_FLAG) + " file and the default passwaord when non selected is 0000 (four zeros)", 80));
//                }
//                System.out.println("Error 8a: " + error);
//            }
//
//
//        } catch (Exception ex) {
//            System.out.println("Error 1.1a: Error suring in-memorising drivedb");
//        }
//        return result;
//    }
//    private boolean diploy_services(GlassFish glassfish) {
//        try {
//            if (instruct_map.containsKey(SOURCE_WAR_DIR_FLAG)) {
//                Deployer deployer = glassfish.getDeployer();
//                String war_dir_nm = instruct_map.get(SOURCE_WAR_DIR_FLAG);
//                File war_dir = new File(war_dir_nm);
//                if (war_dir.isDirectory() && war_dir.canRead()) {
//                    System.out.println("Starting the diployment of web applications");
//                    File[] war_a = war_dir.listFiles();
//                    for (int i = 0; i < war_a.length; i++) {
//                        File file = war_a[i];
//                        System.out.println("Deploying " + file.getAbsolutePath());
//                        String[] c_det = file.getName().split("\\.");
//                        if (c_det.length > 1 && c_det[c_det.length - 1].equalsIgnoreCase("war")) {
//                            File web = new File(file.getAbsolutePath());
//                            try {
//                                deployer.deploy(web, "--name=" + c_det[0], "--contextroot=" + c_det[0], "--force=true");
//                            } catch (Exception ex) {
//                                System.out.println("Error : " + ex.getMessage() + " \n Server restart required");
//                            }
//
//                        }
//                    }
//                }
//            }
//        } catch (Exception ex) {
//            System.out.println("1328 " + ex.getMessage());
//        }
//        return true;
//    }
    private boolean diploy_services(GlassFish glassfish) {
        int max_wait_loop = 100;
        try {
            if (getConnection_memory(false, 12) != null && !getConnection_memory(false, 13).isClosed()) {
                while (isActive(getConnection_memory(false, 14)) && max_wait_loop > 0) {
                    max_wait_loop--;
                    System.out.println("Waiting untill a search operation is finnished (attempt " + (100 - max_wait_loop) + ")");
                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException ex) {
                    }
                }
                System.out.println("Is there any active searches=NO");
            } else {
                System.out.println("Warning 1033");
            }
            max_wait_loop = 100;
            if (getConnection() != null && !getConnection().isClosed()) {
                while (isActive(getConnection()) && max_wait_loop > 0) {
                    max_wait_loop--;
                    System.out.println("Waiting untill a transaction is finnished (attempt " + (100 - max_wait_loop) + ")");
                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException ex) {
                    }
                }
                System.out.println("Is there any active transaction=NO");
            } else {
                System.out.println("Warning 1047");
            }
        } catch (Exception ex) {
            System.out.println("Warning , database droppped from memory " + ex.getMessage());
        }
        try {
            if (instruct_map.containsKey(SOURCE_WAR_DIR_FLAG)) {
                Deployer deployer = glassfish.getDeployer();
                Path war_path = Paths.get(instruct_map.get(SOURCE_WAR_DIR_FLAG));
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
            getConnection_memory(true, 5);
            ResultSet r = getConnection_memory(false, 6).createStatement().
                    executeQuery("select count(id) as report_count from "
                    + get_correct_table_name(getConnection_memory(false, 7),
                    "report", instruct_map.get(DATABASE_NAME_DATA_FLAG), 700));
            while (r.next()) {
                System.out.println(" REPORT.COUNT " + r.getInt(1));
            }
        } catch (Exception ex) {
            System.out.println("1328 " + ex.getMessage());
        }
        return true;
    }

    private boolean asadmin(GlassFish glassfish, String command, String[] param) {
        try {
            CommandResult r = null;
            if (param == null) {
                r = glassfish.getCommandRunner().run(command);
            } else {
                r = glassfish.getCommandRunner().run(command, param);
            }

            if (r.getExitStatus() == ExitStatus.FAILURE) {
                print_connection_error(r.getFailureCause(), "xa");
            } else {
                System.out.println("1648 " + r.getOutput());
            }

        } catch (Exception ex) {
            System.out.println("1328 " + ex.getMessage());
        }
        return true;
    }

    private boolean test_mail(boolean uselocalhostformail) {
        (new Thread(new ProgressMonitor(PROGRESS_MONT_INTERVAL, "15"))).start();
        String mail_subject = "egenvar test";

        int code = rand(1000, 9999);
        System.out.println("Attempting to send mail ...port" + instruct_map.get("mail.smtp.port"));

        if ((instruct_map.get("mail.smtp.port").matches("[0-9]+")) && sendEmail(uselocalhostformail, instruct_map.get("--mailhost"),
                new Integer(instruct_map.get("mail.smtp.port")),
                instruct_map.get("--mailuser"),
                instruct_map.get("mail.smtp.password"),
                mail_subject, ("" + code), instruct_map.get("--fromaddress"), instruct_map.get(TRUST_STORE_FILE_NM), "changeit")) {
            boolean ok = false;
            int i = 5;
            ProgressMonitor.cancel();
            while (!ok && i > 0) {
                i--;
                String ans = getuserInputSameLine("An email was sent to " + instruct_map.get("--fromaddress") + " with a validation code,", "Validation code[C-cancel]");
                if (analyseUserResponse(ans, true, true, null) == 1) {
                    if ((code + "").equals(ans.trim())) {
                        ok = true;
                        System.out.println("Code ... OK");
                    } else {
                        System.out.println("Invalid code, please try again (" + i + " atempts left)");
                    }
                } else if (analyseUserResponse(ans, true, true, null) == 5) {
                    System.out.println("The egenvar system can not function without an email account");
                    System.exit(1);
                }
            }
            if (i <= 0) {
                System.out.println("The egenvar system can not function without an email account");
            }
            ProgressMonitor.cancel();
            return ok;
        } else {
            System.out.println("Sending mail failed. Host=" + instruct_map.get("--mailhost") + "|port=" + instruct_map.get("mail.smtp.port") + "|User=" + instruct_map.get("--mailuser"));
        }
        ProgressMonitor.cancel();
        return false;

    }

    private int rand(int lo, int hi) {
        java.util.Random rn = new java.util.Random();
        int n = hi - lo + 1;
        int i = rn.nextInt(n);
        if (i < 0) {
            i = -i;
        }
        int cval = lo + i;
        return cval;
    }

    /*
     MethodID=7
     */
    private NetworkServerControl startDerby(String dblogfilenm, boolean memorize) {
        (new Thread(new ProgressMonitor(PROGRESS_MONT_INTERVAL, "7"))).start();
        NetworkServerControl db_server = null;
        try {
            if (memorize) {
                System.out.println("Memory Database server port=" + instruct_map.get(derby_port_MEM_flag) + " log:" + dblogfilenm + " Inet::" + InetAddress.getByName("localhost"));
                db_server = new NetworkServerControl(InetAddress.getByName("localhost"), new Integer(instruct_map.get(derby_port_MEM_flag)));
                PrintWriter log2 = new PrintWriter(new File(dblogfilenm + ".mem"));
                db_server.start(log2);
            } else {
                System.out.println("Database server port=" + instruct_map.get(derby_port_flag) + " log:" + dblogfilenm + " Inet::" + InetAddress.getByName("localhost"));
                db_server = new NetworkServerControl(InetAddress.getByName("localhost"), new Integer(instruct_map.get(derby_port_flag)));
                PrintWriter log = new PrintWriter(new File(dblogfilenm));
                db_server.start(log);
            }
        } catch (Exception ex) {
            System.out.println("Error 7a:" + ex.getMessage() + ". Will rectify this later");
            ex.printStackTrace();
        }
        try {
            if (db_server != null) {
                db_server.trace(true);
            }
        } catch (Exception ex) {
            System.out.println("Error 7b: Warning setting trace failed. Will rectify this later");
        }
        ProgressMonitor.cancel();
        return db_server;
    }

//    private void optimizeDB() {
    //To restric access : derby.storage.useDefaultFilePermissions=true
//        try {
//            System.out.println("1387");
//            createConnection();
//            Statement s = conn.createStatement();
////            s.executeUpdate("CALL SYSCS_UTIL.SYSCS_SET_DATABASE_PROPERTY('derby.storage.pageCacheSize', '100')");
////            ResultSet r =s.executeQuery("values SYSCS_UTIL.SYSCS_GET_DATABASE_PROPERTY('derby.storage.pageCacheSize')");
//            s.executeUpdate("CALL SYSCS_UTIL.SYSCS_SET_RUNTIMESTATISTICS(1)");
//            ResultSet r_2 = s.executeQuery("Select * from EGEN_DATAENTRY.files where name like '%genrated_3110_22%'");
//            while (r_2.next()) {
//                String sh = r_2.getString(2);
//                System.out.println("1396 " + sh);
//            }
//            ResultSet r = s.executeQuery("VALUES SYSCS_UTIL.SYSCS_GET_RUNTIMESTATISTICS()");
//            while (r.next()) {
//                System.out.println("1362 " + r.getString(1));
//            }
//        } catch (SQLException ex) {
//            ex.printStackTrace();
//        }
//    }
    /*
     MethodID=
     */
    private int droptable(Connection c_conn) {
        int result = -1;
        try {
            if (c_conn != null && !c_conn.isClosed()) {
                ArrayList<String> table_l = getCurrentTables(c_conn, instruct_map.get(DATABASE_NAME_DATA_FLAG), 2);
                Collections.sort(table_l);
                table_l.add("All Tags  (loose all tags!)");
                table_l.add("All Tag-sources (loose all tag references!)");
                table_l.add("Cancel (exit without making any changes)");
                int ans = getUserChoice(table_l, "Select the table to drop");
                System.out.println("1349 " + ans);
                if (ans == table_l.size() - 1) {
                } else if (ans == table_l.size() - 2) {
                    ArrayList<String> todrop_l = new ArrayList<>();
                    for (int i = 0; i < table_l.size(); i++) {
                        if (table_l.get(i).toLowerCase().endsWith(TAGSOURCE_FLAG)) {
                            todrop_l.add(table_l.get(i));
                        }
                    }
                    ArrayList<String> sel_l = new ArrayList<>(Arrays.asList(new String[]{"Yes drop the tables " + todrop_l.toString() + "?"}));
                    sel_l.add("Do not drop and get out safely");
                    if (getUserChoice(sel_l, "Select the table to drop") == 0) {
                        for (int i = 0; i < todrop_l.size(); i++) {
                            System.out.println("Droping " + todrop_l.get(i));
                            result = c_conn.createStatement().executeUpdate("Drop table " + todrop_l.get(i));
                        }

                    }
                } else if (ans == table_l.size() - 3) {
                    ArrayList<String> todrop_l = new ArrayList<>();
                    for (int i = 0; i < table_l.size(); i++) {
                        if (table_l.get(i).toLowerCase().endsWith("2tags")) {
                            todrop_l.add(table_l.get(i));
                        }
                    }
                    ArrayList<String> sel_l = new ArrayList<>(Arrays.asList(new String[]{"Yes drop the tables " + todrop_l.toString() + "?"}));
                    sel_l.add("Do not drop and get out safely");
                    if (getUserChoice(sel_l, "Select the table to drop") == 0) {
                        for (int i = 0; i < todrop_l.size(); i++) {
                            System.out.println("Droping " + todrop_l.get(i));
                            result = c_conn.createStatement().executeUpdate("Drop table " + todrop_l.get(i));
                        }

                    }
                } else {
                    String table = table_l.get(ans);
                    ArrayList<String> sel_l = new ArrayList<>(Arrays.asList(new String[]{"Yes drop the table " + table + "?"}));
                    sel_l.add("Do not drop and get out safely");
                    if (getUserChoice(sel_l, "Select the table to drop") == 0) {
                        System.out.println("Droping " + table);
                        result = c_conn.createStatement().executeUpdate("Drop table " + table);
                    }
                }
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return result;
    }
    /*
     MethodID=
     */

    private int setConfig(Connection c_conn, HashMap<String, String> instruct_map, String schema_name) {
        int result = 0;
        try {
            if (c_conn != null && !c_conn.isClosed()) {
                PreparedStatement p_1 = c_conn.prepareStatement("update "
                        + get_correct_table_name(c_conn, "config", schema_name, 800)
                        + " set param_value=? where name=?");
                ArrayList<String> key_l = new ArrayList<>(instruct_map.keySet());
                for (int i = 0; i < key_l.size(); i++) {
                    p_1.setString(2, key_l.get(i));
                    p_1.setString(1, instruct_map.get(key_l.get(i)));
                    p_1.addBatch();
                }
                int[] results = p_1.executeBatch();
                for (int i = 0; (i < results.length && (result >= 0)); i++) {
                    if (results[i] < 0) {
                        result = results[i];
                    }
                }
                p_1.close();
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return result;
    }

//    /*
//     MethodID=
//     */
//    private int getConfig(Connection c_conn, String schema_name) {
//        HashMap<String, String> instruct_map = new HashMap<>();
//        int result = 0;
//        try {
//            if (c_conn != null && !c_conn.isClosed()) {
//                PreparedStatement p_1 = c_conn.prepareStatement("select  " + get_correct_table_name(c_conn, "config", schema_name) + " set param_value=? where name=?");
//                ResultSet r = p_1.executeQuery();
//                while (r.next()) {
//                    
//                }
//                r.close();
//                p_1.close();
//            }
//        } catch (SQLException ex) {
//            ex.printStackTrace();
//        }
//        return result;
//    }
    /*
     MethodID=6
     */
    private boolean setAuthentication(HashMap<String, String> instruct_map) {
        boolean result = false;
        try {
            if (getConnection_usermanage() != null) {
                Statement usermanage_stmt = conn_users.createStatement();
                usermanage_stmt.executeUpdate("CALL SYSCS_UTIL.SYSCS_SET_DATABASE_PROPERTY('derby.authentication.builtin.algorithm', '" + instruct_map.get(DB_AUTHENTICATION_ALGORITHM_FLAG) + "')");
                usermanage_stmt.executeUpdate("CALL SYSCS_UTIL.SYSCS_SET_DATABASE_PROPERTY('derby.connection.requireAuthentication', 'true')");
                usermanage_stmt.executeUpdate("CALL SYSCS_UTIL.SYSCS_SET_DATABASE_PROPERTY('derby.authentication.provider', 'BUILTIN')");
                usermanage_stmt.executeUpdate("CALL SYSCS_UTIL.SYSCS_SET_DATABASE_PROPERTY('derby.user.usermanage', '" + instruct_map.get(unm_usermanage_pass_flag) + "')");
                usermanage_stmt.executeUpdate("CALL SYSCS_UTIL.SYSCS_SET_DATABASE_PROPERTY('derby.database.defaultConnectionMode', 'noAccess')");
                usermanage_stmt.executeUpdate("CALL SYSCS_UTIL.SYSCS_SET_DATABASE_PROPERTY('derby.database.fullAccessUsers', 'usermanage')");
                usermanage_stmt.executeUpdate("CALL SYSCS_UTIL.SYSCS_SET_DATABASE_PROPERTY('derby.database.propertiesOnly', 'false')");
                usermanage_stmt.close();
                System.out.println("Registering use accounts");
                result = true;
            }

            if (result && getConnection() != null) {
                Statement datacontrol_stmt = getConnection().createStatement();
                datacontrol_stmt.executeUpdate("CALL SYSCS_UTIL.SYSCS_SET_DATABASE_PROPERTY('derby.authentication.builtin.algorithm', '" + instruct_map.get(DB_AUTHENTICATION_ALGORITHM_FLAG) + "')");
                datacontrol_stmt.executeUpdate("CALL SYSCS_UTIL.SYSCS_SET_DATABASE_PROPERTY('derby.connection.requireAuthentication', 'true')");
                datacontrol_stmt.executeUpdate("CALL SYSCS_UTIL.SYSCS_SET_DATABASE_PROPERTY('derby.authentication.provider', 'BUILTIN')");
                datacontrol_stmt.executeUpdate("CALL SYSCS_UTIL.SYSCS_SET_DATABASE_PROPERTY('derby.user." + instruct_map.get(unm_gendataentry_flag) + "','" + instruct_map.get(unm_gendataentry_pass_flag) + "')");
                datacontrol_stmt.executeUpdate("CALL SYSCS_UTIL.SYSCS_SET_DATABASE_PROPERTY('derby.user." + instruct_map.get(unm_gendataUpdate_flag) + "','" + instruct_map.get(unm_gendataUpdate_pass_flag) + "')");
                datacontrol_stmt.executeUpdate("CALL SYSCS_UTIL.SYSCS_SET_DATABASE_PROPERTY('derby.user." + instruct_map.get(unm_gendataview_flag) + "','" + instruct_map.get(unm_gendataview_pass_flag) + "')");
                datacontrol_stmt.executeUpdate("CALL SYSCS_UTIL.SYSCS_SET_DATABASE_PROPERTY('derby.database.defaultConnectionMode', 'noAccess')");
                datacontrol_stmt.executeUpdate("CALL SYSCS_UTIL.SYSCS_SET_DATABASE_PROPERTY('derby.database.fullAccessUsers', '" + instruct_map.get(unm_gendataentry_flag) + "," + instruct_map.get(unm_gendataUpdate_flag) + "')");
                datacontrol_stmt.executeUpdate("CALL SYSCS_UTIL.SYSCS_SET_DATABASE_PROPERTY('derby.database.readOnlyAccessUsers', '" + instruct_map.get(unm_gendataview_flag) + "')");
                datacontrol_stmt.executeUpdate("CALL SYSCS_UTIL.SYSCS_SET_DATABASE_PROPERTY('derby.database.propertiesOnly', 'false')");
                datacontrol_stmt.close();
                result = true;
            } else {
                result = false;
            }

//            if (result && getConnection_memory() != null) {
//                Statement datacontrol_stmt = getConnection_memory().createStatement();
//                datacontrol_stmt.executeUpdate("CALL SYSCS_UTIL.SYSCS_SET_DATABASE_PROPERTY('derby.authentication.builtin.algorithm', '" + instruct_map.get(DB_AUTHENTICATION_ALGORITHM_FLAG) + "')");
//                datacontrol_stmt.executeUpdate("CALL SYSCS_UTIL.SYSCS_SET_DATABASE_PROPERTY('derby.connection.requireAuthentication', 'true')");
//                datacontrol_stmt.executeUpdate("CALL SYSCS_UTIL.SYSCS_SET_DATABASE_PROPERTY('derby.authentication.provider', 'BUILTIN')");
//                datacontrol_stmt.executeUpdate("CALL SYSCS_UTIL.SYSCS_SET_DATABASE_PROPERTY('derby.user." + instruct_map.get(unm_gendataentry_flag) + "','" + instruct_map.get(unm_gendataentry_pass_flag) + "')");
//                datacontrol_stmt.executeUpdate("CALL SYSCS_UTIL.SYSCS_SET_DATABASE_PROPERTY('derby.user." + instruct_map.get(unm_gendataUpdate_flag) + "','" + instruct_map.get(unm_gendataUpdate_pass_flag) + "')");
//                datacontrol_stmt.executeUpdate("CALL SYSCS_UTIL.SYSCS_SET_DATABASE_PROPERTY('derby.user." + instruct_map.get(unm_gendataview_flag) + "','" + instruct_map.get(unm_gendataview_pass_flag) + "')");
//                datacontrol_stmt.executeUpdate("CALL SYSCS_UTIL.SYSCS_SET_DATABASE_PROPERTY('derby.database.defaultConnectionMode', 'noAccess')");
//                datacontrol_stmt.executeUpdate("CALL SYSCS_UTIL.SYSCS_SET_DATABASE_PROPERTY('derby.database.fullAccessUsers', '" + instruct_map.get(unm_gendataentry_flag) + "," + instruct_map.get(unm_gendataUpdate_flag) + "')");
//                datacontrol_stmt.executeUpdate("CALL SYSCS_UTIL.SYSCS_SET_DATABASE_PROPERTY('derby.database.readOnlyAccessUsers', '" + instruct_map.get(unm_gendataview_flag) + "')");
//                datacontrol_stmt.executeUpdate("CALL SYSCS_UTIL.SYSCS_SET_DATABASE_PROPERTY('derby.database.propertiesOnly', 'false')");
//                datacontrol_stmt.close();
//                result = true;
//            } else {
//                result = false;
//            }
        } catch (SQLException ex) {
            System.out.println("Error 6a:" + ex.getMessage());
            result = false;
        }
        return result;
    }

    /*
     MethodID=9
     */
    private void createuserTable_usermanagement() {
        ArrayList<String> table_l = getCurrentTables_usermange();
        if (!table_l.contains("USERACCOUNTS")) {
            String create_sql = "CREATE TABLE " + instruct_map.get(DATABASE_NAME_USERS_FLAG) + ".useraccounts (id int NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1)  PRIMARY KEY"
                    + ",create_time TIMESTAMP DEFAULT NULL"
                    + ",update_time TIMESTAMP DEFAULT NULL"
                    + ",email varchar(255)"
                    + ",password varchar(40) NOT NULL"
                    + ",username varchar(255) DEFAULT NULL"
                    + ",active_username varchar(255) DEFAULT NULL"
                    + ",userIP varchar(16) DEFAULT 'NA'"
                    + ",validated int DEFAULT 0"
                    + ",allowed_groups varchar(256) DEFAULT ''"
                    + ",shell varchar(16) DEFAULT '/bin/bash'"
                    + ",gid int DEFAULT 9999"
                    + ",uid int DEFAULT 9999"
                    + ",level int DEFAULT 0"
                    + ",mobile varchar(16) DEFAULT NULL"
                    + ",homedir varchar(512) default '/home/galaxy/galaxy-dist/database/files'"
                    + ",constraint unique_identification unique(username)"
                    + ")";
            createTable_users(getConnection_usermanage(), create_sql);
        }
        if (!table_l.contains("GROUPS")) {
            String create2_sql = "CREATE TABLE " + instruct_map.get(DATABASE_NAME_USERS_FLAG) + ".groups (id int NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1)  PRIMARY KEY"
                    + ",gid int DEFAULT 9999"
                    + ",groupname varchar(256) DEFAULT NULL"
                    + ",username varchar(256) DEFAULT NULL"
                    + ",uid int DEFAULT 9999"
                    + ")";
            createTable_users(getConnection_usermanage(), create2_sql);
        }
        if (!table_l.contains("GROUPNMTOGID")) {
            String create3_sql = "CREATE TABLE " + instruct_map.get(DATABASE_NAME_USERS_FLAG) + ".groupnmtoGID( id int NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1)  PRIMARY KEY, "
                    + "gid int , "
                    + "groupname varchar(512), "
                    + "constraint unique_identification2 unique(groupname),"
                    + "constraint unique_id UNIQUE(gid))";
            ArrayList<HashMap<String, String>> data_map_l = new ArrayList<>();
            createTable_users(getConnection_usermanage(), create3_sql);
            HashMap<String, String> groupnmtoGID_data_map = new HashMap<>();
            groupnmtoGID_data_map.put("groupname", "Admin");
            groupnmtoGID_data_map.put("gid", "1000");
            data_map_l.clear();
            data_map_l.add(groupnmtoGID_data_map);
            insertData(getConnection_usermanage(), instruct_map.get(DATABASE_NAME_USERS_FLAG) + ".groupnmtoGID", data_map_l);

            groupnmtoGID_data_map.clear();
            groupnmtoGID_data_map.put("groupname", "Uploader");
            groupnmtoGID_data_map.put("gid", "2000");
            data_map_l.clear();
            data_map_l.add(groupnmtoGID_data_map);
            insertData(getConnection_usermanage(), instruct_map.get(DATABASE_NAME_USERS_FLAG) + ".groupnmtoGID", data_map_l);

            groupnmtoGID_data_map.clear();
            groupnmtoGID_data_map.put("groupname", "Editor");
            groupnmtoGID_data_map.put("gid", "2001");
            data_map_l.clear();
            data_map_l.add(groupnmtoGID_data_map);
            insertData(getConnection_usermanage(), instruct_map.get(DATABASE_NAME_USERS_FLAG) + ".groupnmtoGID", data_map_l);

            groupnmtoGID_data_map.clear();
            groupnmtoGID_data_map.put("groupname", "Deletor");
            groupnmtoGID_data_map.put("gid", "2003");
            data_map_l.clear();
            data_map_l.add(groupnmtoGID_data_map);
            insertData(getConnection_usermanage(), instruct_map.get(DATABASE_NAME_USERS_FLAG) + ".groupnmtoGID", data_map_l);

            groupnmtoGID_data_map.clear();
            groupnmtoGID_data_map.put("groupname", "Search");
            groupnmtoGID_data_map.put("gid", "300");
            data_map_l.clear();
            data_map_l.add(groupnmtoGID_data_map);
            insertData(getConnection_usermanage(), instruct_map.get(DATABASE_NAME_USERS_FLAG) + ".groupnmtoGID", data_map_l);
        }

        if (!table_l.contains("SMS")) {
            String create2_sql = "CREATE TABLE " + instruct_map.get(DATABASE_NAME_USERS_FLAG) + ".sms (id int NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1)  PRIMARY KEY"
                    + ",username varchar(256)"
                    + ",password varchar(256)"
                    + ")";
            createTable_users(getConnection_usermanage(), create2_sql);
//            ArrayList<HashMap<String, String>> data_map_l = new ArrayList<HashMap<String, String>>();
//            HashMap<String, String> groupnmtoGID_data_map = new HashMap<String, String>();
//            groupnmtoGID_data_map.put("username", "0");
//            groupnmtoGID_data_map.put("password", "0");
//            data_map_l.clear();
//            data_map_l.add(groupnmtoGID_data_map);
//            insertData(instruct_map.get(DATABASE_NAME_USERS_FLAG) + ".sms", data_map_l);
        }
//        ArrayList<HashMap<String, String>> data_map_l = new ArrayList<HashMap<String, String>>();
//        HashMap<String, String> data_map = new HashMap<String, String>();
//        data_map.put("email", "sabryr@gmail.com");
//        data_map.put("password", "0afbf93cf7560b64b9cb28f2d57baa4159273f44");
//        data_map.put("username", "sabryr@gmail.com");
//        data_map.put("active_username", "sabryr@gmail.com");
//        data_map.put("validated", "1");
//        data_map.put("allowed_groups", ",2003,2001,3000,2000,");
//        data_map.put("gid", "10012");
//        data_map.put("uid", "10012");
//        data_map_l.add(data_map);
//        insertData(database_name_users + ".useraccounts", data_map_l);
    }
//

    private void register_Procedures() {
        Statement stmt = null;
        String query_SplitValues =
                "CREATE PROCEDURE  " + instruct_map.get(DATABASE_NAME_USERS_FLAG) + ".spliter_SplitValues(in table_nm VARCHAR(32),in tosplit VARCHAR(2048), in delimiter VARCHAR(8), out  VARCHAR(2048)) "
                + "PARAMETER STYLE JAVA "
                + "LANGUAGE JAVA "
                + "DYNAMIC RESULT SETS 0 "
                + "EXTERNAL NAME 'derby.egenv.medisin.ntnu.no.Procedures_eGenv_JAVADB.spliter_SplitValues'";
        String query_insert_user =
                "CREATE PROCEDURE  " + instruct_map.get(DATABASE_NAME_USERS_FLAG) + ".insert_user("
                + "spliter_SplitValues_proc_name varchar(64),"
                + "useraaccounts_table varchar(255),"
                + "groupnmtoGID_tbl varchar(255),"
                + "group_table varchar(255),"
                + "username_inactive varchar(255),"
                + "username varchar(255),"
                + "email varchar(255),"
                + "new_password varchar(40),"
                + "delimiter varchar(8),"
                + "allowed_groups varchar(512), "
                + "validated int, "
                + "level int, "
                + "userIP varchar(16),"
                + "mobile varchar(16),"
                + "shaprocedure_name varchar(255),"
                + "OUT result varchar(128)) "
                + "PARAMETER STYLE JAVA "
                + "LANGUAGE JAVA "
                + "DYNAMIC RESULT SETS 0 "
                + "EXTERNAL NAME 'derby.egenv.medisin.ntnu.no.Procedures_eGenv_JAVADB.insert_user'";


        String query_reset_password =
                "CREATE PROCEDURE  " + instruct_map.get(DATABASE_NAME_USERS_FLAG) + ".reset_password(in useraccounts_tbl VARCHAR(255),in email VARCHAR(255),in new_password VARCHAR(40), out  VARCHAR(512)) "
                + "PARAMETER STYLE JAVA "
                + "LANGUAGE JAVA "
                + "DYNAMIC RESULT SETS 0 "
                + "EXTERNAL NAME 'derby.egenv.medisin.ntnu.no.Procedures_eGenv_JAVADB.reset_password'";

        String sha1_function = "CREATE FUNCTION  " + instruct_map.get(DATABASE_NAME_USERS_FLAG) + ".sha1(intext VARCHAR(64)) RETURNS CHAR(40)"
                + " PARAMETER STYLE JAVA NO SQL LANGUAGE JAVA"
                + " EXTERNAL NAME  'derby.egenv.medisin.ntnu.no.Procedures_eGenv_JAVADB.sha1'";
        try {
            if (getConnection_usermanage() != null) {
                stmt = conn_users.createStatement();
                stmt.execute("DROP PROCEDURE " + instruct_map.get(DATABASE_NAME_USERS_FLAG) + ".SPLITER_SPLITVALUES");
                stmt.execute("DROP PROCEDURE " + instruct_map.get(DATABASE_NAME_USERS_FLAG) + ".INSERT_USER");
                stmt.execute("DROP PROCEDURE " + instruct_map.get(DATABASE_NAME_USERS_FLAG) + ".reset_password");
                stmt.execute("DROP FUNCTION " + instruct_map.get(DATABASE_NAME_USERS_FLAG) + ".sha1");
                stmt.close();
            }
        } catch (SQLException e) {
//            e.printStackTrace();
        }
        try {
            if (getConnection_usermanage() != null) {
                stmt = conn_users.createStatement();
                stmt.execute(sha1_function);
                stmt.execute(query_SplitValues);
                stmt.execute(query_insert_user);
                stmt.execute(query_reset_password);
                stmt.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

    public void installProcedurejar() {
        if (getConnection_usermanage() != null) {
            try {
                CallableStatement cs2 = null;
                CallableStatement cs3 = null;
                String query1 = "CALL sqlj.install_jar('" + instruct_map.get(PROCEDURE_JAr_LOC_FLAG) + "','" + instruct_map.get(DATABASE_NAME_USERS_FLAG) + ".Procedures_eGenv_JAVADB',0)";
                String query2 = "CALL sqlj.replace_jar('" + instruct_map.get(PROCEDURE_JAr_LOC_FLAG) + "','" + instruct_map.get(DATABASE_NAME_USERS_FLAG) + ".Procedures_eGenv_JAVADB')";
                try {
                    cs2 = conn_users.prepareCall(query1);
                    cs2.execute();
                } catch (SQLException e2) {
                    System.out.println("Warning 982: " + e2.getMessage());
                }
                try {
                    cs2 = conn_users.prepareCall(query2);
                    cs2.execute();
                } catch (SQLException e2) {
                    e2.printStackTrace();
                } finally {
                    if (cs2 != null) {
                        cs2.close();
                    }
                }
                String procedure_name = "Procedures_eGenv_JAVADB";
                String query3 = "CALL syscs_util.syscs_set_database_property('derby.database.classpath','" + instruct_map.get(DATABASE_NAME_USERS_FLAG) + ".Procedures_eGenv_JAVADB')";
                try {
                    cs3 = conn_users.prepareCall(query3);
                    cs3.execute();
                } catch (SQLException e) {
                    e.printStackTrace();
                } finally {
                    if (cs3 != null) {
                        cs3.close();
                    }
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }

    /*
     MethodID=3
     */
    private int createTablesFromFile(Connection c_conn, String filename,
            boolean verbose, String schema_name) {
        int result = 0;
        ArrayList<String> table_l = getCurrentTables(c_conn, schema_name, 3);
        if (verbose) {
            System.out.println("Current tables " + table_l);
        }

        ArrayList<String> found_tables_l = new ArrayList<>(table_l.size());
        try {
            File file = new File(filename);
            Scanner scan = new Scanner(file);
            while (scan.hasNext() && result >= 0) {
                String line = scan.nextLine().trim();
                if (!line.isEmpty() && line.length() > 12) {
                    line = line.replaceAll("EGEN_DATAENTRY", schema_name);
                    if (line.startsWith("TABLENAME=") && line.contains("==")) {
                        String table_nm = line.split("==")[0].replace("TABLENAME=", "").trim();
                        line = line.split("==")[1].trim();
                        line = line.replace(";", "");
                        if (verbose) {
                            int subemd = line.length();
                            if (subemd > 100) {
                                subemd = 100;
                            }
                            System.out.print("Precessing. Table=" + table_nm + "| " + line.substring(0, subemd));
                        }
                        if (!table_l.contains(table_nm.toUpperCase()) && !line.startsWith("DROP")) {
                            try {
                                result = createTable(c_conn, line);
                            } catch (SQLException ex) {
                                ex.printStackTrace();
                                result = -1;
                            }
                            if (result >= 0) {
                                if (verbose) {
                                    System.out.print("\t\t Table updated");
                                }
                            } else {
                                System.out.print("Error: Table " + table_nm + " creation failed");
                            }
                        } else {
                            if (table_l.contains(table_nm.toUpperCase()) && line.startsWith("DROP")) {
                                table_l.remove(table_nm.toUpperCase());
                                try {
                                    result = createTable(c_conn, line);
                                    if (result >= 0) {
                                        if (verbose) {
                                            System.out.print("\t\t Table dropped");
                                        }
                                    } else {
                                        System.out.print("Error: Table " + table_nm + " creation failed");
                                    }
                                } catch (SQLException ex) {
                                    ex.printStackTrace();
                                }
                            }
                            if (!found_tables_l.contains(table_nm)) {
                                found_tables_l.add(table_nm);
                            } else {
                                if (verbose) {
                                    System.out.print("\t\t... Table exists.");
                                }
                            }
                        }
                        if (verbose) {
                            System.out.println("");
                        }

                    } else {
//                        System.out.println("no match " + line);
                    }
                } else {
//                    System.out.println("no match " + line);
                }
            }
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        }
        if (!found_tables_l.isEmpty()) {
//            System.out.println("Following table were already there and was not altered this time " + found_tables_l + "");
        }
//        System.out.println("\n\n\n 2766 "+getCurrentTables(c_conn, schema_name));
        return result;
    }

    private HashMap<String, String> getFirstUseraccount(boolean forcelocalhost) {
        HashMap<String, String> acouunt_map = null;
        if (getConnection_usermanage() != null) {
            if (executeQuery_user("select * from " + get_correct_table_name_USERS("useraccounts")).isEmpty()) {
                System.out.println("\n\nPlease create a user account to use the system");
                acouunt_map = new HashMap<>();
                acouunt_map.put("email", instruct_map.get("--fromaddress"));
                acouunt_map.put("First_name", null);
                acouunt_map.put("Last_name", null);
                String password = randomstring(8);
                acouunt_map.put("Password", password);
                acouunt_map.put(SERVER_IP_FLAG, getNetInfo(forcelocalhost).get(SERVER_IP_FLAG));
                ArrayList<String> key_l = new ArrayList<>(acouunt_map.keySet());
                for (int i = 0; i < key_l.size(); i++) {
                    String c_key = key_l.get(i);
                    if (acouunt_map.get(c_key) == null) {
                        String user_in = null;
                        if (c_key.equalsIgnoreCase("EMAIL")) {
                            user_in = getuserInputSameLine("Enter " + c_key + " of the user (this will be the username)", null);
                        } else {
                            user_in = getuserInputSameLine("Enter " + c_key + " of the user", null);
                        }

                        while (user_in == null || user_in.isEmpty()) {
                            user_in = getuserInputSameLine("Enter " + c_key + " of the user", null);
                        }
                        acouunt_map.put(c_key, user_in);
                    } else {
                        System.out.println("Assigned values for " + c_key + ":" + acouunt_map.get(c_key));
                    }
                }
                if (insert_user_account(acouunt_map, 0) < 0) {
                    acouunt_map = null;
                } else {
                    if (addData(getConnection(), "insert into "
                            + get_correct_table_name(getConnection(),
                            "person", instruct_map.get(DATABASE_NAME_DATA_FLAG), 900)
                            + " (name, lastnm, email) values('" + acouunt_map.get("First_name")
                            + "','" + acouunt_map.get("Last_name") + "','"
                            + acouunt_map.get("email") + "')") >= 0) {
                        System.out.println("New user created");

                    } else {
                        System.out.println("\n\nError :Creating new user profile failed \n");
                    }
                }
            } else {
                acouunt_map = new HashMap<>();
            }
        }
        return acouunt_map;
    }

    public boolean setUserLevel(String level, String email) {
        boolean result = false;
        email = email.replaceAll(",", "','");
        email = "'" + email + "'";
        String sql = "update " + instruct_map.get(DATABASE_NAME_USERS_FLAG) + ".useraccounts set level=" + level + " where EMAIL in (" + email + ")";
        if (createTable_users(getConnection_usermanage(), sql) >= 0) {
            result = true;
        }
        return result;
    }

    public boolean activate_users(String email) {
        boolean result = false;
        String sql = "";
        if (email == null) {
            sql = "update " + instruct_map.get(DATABASE_NAME_USERS_FLAG) + ".useraccounts set level=7 where level>7";
        } else {
            email = email.replaceAll(",", "','");
            email = email.replaceAll("'", "");
            email = "'" + email + "'";
            sql = "update " + instruct_map.get(DATABASE_NAME_USERS_FLAG) + ".useraccounts set level=7 where level>7 and EMAIL in (" + email + ")";
        }

        if (createTable_users(getConnection_usermanage(), sql) >= 0) {
            result = true;
        }
        return result;
    }

    public boolean deleteUserLevel(String email) {
        boolean result = false;
        email = email.replaceAll(",", "','");
        email = "'" + email + "'";
        String sql = "delete from  " + instruct_map.get(DATABASE_NAME_USERS_FLAG) + ".useraccounts where EMAIL in (" + email + ")";       
        if (createTable_users(getConnection_usermanage(), sql) >= 0) {
            result = true;
        }
        return result;
    }

    private boolean blockUser(String email) {
        boolean result = false;
        email = email.replaceAll(",", "','");
        email = "'" + email + "'";
        String sql = "update " + instruct_map.get(DATABASE_NAME_USERS_FLAG) + ".useraccounts set username='" + randomstring(10) + "' where EMAIL in (" + email + ")";
        if (createTable_users(getConnection_usermanage(), sql) >= 0) {
            result = true;
        }
        return result;
    }

    private boolean displayUserDetails(String columns, String condition) {
        boolean result = false;
        String sql = null;
//        if (email.equals("*")) {
        sql = "Select " + columns + " from  " + instruct_map.get(DATABASE_NAME_USERS_FLAG) + ".useraccounts" + condition;
//        } else {
//            email = email.replaceAll(",", "','");
//            email = "'" + email + "'";
//            sql = "Select * from  " + instruct_map.get(DATABASE_NAME_USERS_FLAG) + ".useraccounts";//((username in (" + email + ")"
//        }
        ArrayList<String> result_l = executeQuery_user(sql);
        if (result_l == null || result_l.isEmpty()) {
            System.out.println("user not found");
        } else {
            System.out.println("_____________________");
            for (int i = 0; i < result_l.size(); i++) {
                String[] c_user = result_l.get(i).split("\\|\\|");
                System.out.print(i + 1);
                for (int j = 0; j < c_user.length; j++) {
                    System.out.print("\t" + c_user[j]);
                }
                System.out.println("");
            }
        }
        return result;
    }

    private int createUser() {
        boolean uselocalhostformail = false;
        if (instruct_map.get("USE_LOCAL_HOST_FOR_MAIL") != null && instruct_map.get("USE_LOCAL_HOST_FOR_MAIL").equalsIgnoreCase("TRUE")) {
            uselocalhostformail = true;
        }
        int result = -1;
        HashMap<String, String> acouunt_map = new HashMap<>();
        acouunt_map.put("email", null);
        acouunt_map.put("First_name", null);
        acouunt_map.put("Last_name", null);
        String password = randomstring(8);
        acouunt_map.put("Password", password);
        acouunt_map.put(SERVER_IP_FLAG, "0.0.0.0");
        ArrayList<String> key_l = new ArrayList<>(acouunt_map.keySet());
        for (int i = 0; i < key_l.size(); i++) {
            String c_key = key_l.get(i);
            if (acouunt_map.get(c_key) == null) {
                String user_in = getuserInputSameLine("Enter " + c_key, null);
                while (user_in == null || user_in.isEmpty()) {
                    user_in = getuserInputSameLine("Enter " + c_key, null);
                }
                acouunt_map.put(c_key, user_in);
            } else {
                System.out.println("Assigned values for " + c_key + ":" + acouunt_map.get(c_key));
            }
        }
        if (insert_user_account(acouunt_map, 9) < 0) {
            acouunt_map = null;
        } else {
            addData(getConnection(), "insert into "
                    + get_correct_table_name(getConnection(), "person",
                    instruct_map.get(DATABASE_NAME_DATA_FLAG), 1000)
                    + " (name, lastnm, email) values('" + acouunt_map.get("First_name")
                    + "','" + acouunt_map.get("Last_name") + "','"
                    + acouunt_map.get("email") + "')");
            result = 1;
            String mail_subject = "egenvar user account information";
            String mail_message = "welcome to egenvar data management service";
            mail_message = mail_message + "\n Login details for  :" + acouunt_map.get("First_name") + "  " + acouunt_map.get("Last_name");
            mail_message = mail_message + "\n User name :" + acouunt_map.get("email");
            mail_message = mail_message + "\n Password :" + acouunt_map.get("Password");
            mail_message = mail_message + "\n WSDL url for the egenvar tool: " + instruct_map.get(WSDL_URL_FLAG) + "\n"
                    + "Web interface URL: " + instruct_map.get(S_SERVER_NAME_FLAG);
            boolean mail_sent = sendEmail(uselocalhostformail, instruct_map.get("--mailhost"),
                    new Integer(instruct_map.get("mail.smtp.port")),
                    instruct_map.get("--mailuser"),
                    instruct_map.get("mail.smtp.password"),
                    mail_subject, mail_message, acouunt_map.get("email"), instruct_map.get(TRUST_STORE_FILE_NM), "changeit");
            if (mail_sent) {
                System.out.println("Account information with passward sent to user");
            } else {
                System.out.println("Error: mailing the user failed");
            }
//            } else {
//                result = -1;
//            }

        }
        return result;
    }

    public String get_correct_table_name(Connection c_conn,
            String table_nm, String schema_name, int caller) {
        String result = null;
        try {
            table_nm = table_nm.split("\\.")[table_nm.split("\\.").length - 1];
            ArrayList<String> c_tables_l = getCurrentTables(c_conn, schema_name, (caller + 4));
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

    public String get_correct_table_name_USERS(String table_nm) {
        String result = null;
        try {
            if (!table_nm.contains(instruct_map.get(DATABASE_NAME_USERS_FLAG))) {
                table_nm = instruct_map.get(DATABASE_NAME_USERS_FLAG) + "." + table_nm;
            }
            ArrayList<String> c_tables_l = getCurrentTables_USERS();
            if (c_tables_l.contains(table_nm)) {
                result = table_nm;
            } else {
                boolean not_found = true;
                for (int i = 0; (i < c_tables_l.size() && not_found); i++) {
                    String c_nm = c_tables_l.get(i);
                    if (c_nm.equalsIgnoreCase(table_nm)) {
                        result = c_nm;
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
     MethodID=58
     */

    private ArrayList<String> crateTagFromFile(Connection c_conn, File in_file, String schema_name, String mem_schema_name) {
        ArrayList<String> reate_sql_l = new ArrayList<>();
        System.out.println("Investigating " + in_file.getAbsolutePath() + " started @" + Timing.getDateTime());
        String splitter = "\\|\\|";
        int max_allowd_filed_size = 4096;
        if (c_conn != null) {
            String file_nm = in_file.getName().split("\\.")[0];
            if (file_nm.length() > 64) {
                file_nm = file_nm.substring(0, 63);
            }
            String table_nm = get_correct_table_name(c_conn, file_nm,
                    schema_name, 1100);

//            String table_nm_for_indices = file_nm;
            boolean table_created = false;
            boolean table_found = false;
            if (table_nm == null) {
                table_nm = schema_name + "." + file_nm;
            } else {
                table_found = true;
            }

            instruct_map.put(table_nm, in_file.getAbsolutePath());
//            String sql_create = "create table " + table_nm + " (";
            String sql_create = "create table <TABLE_NAME> (";
            String sql_insert = "insert into " + table_nm + " (";
            String sql_insert_param = "(";
            int result = 0;

            PreparedStatement p_1 = null;
            int col_count = 0;
            int rec_count = 0;
            try {
                Scanner scan = new Scanner(in_file, "ISO-8859-1");
                int line_count = 0;
                while (scan.hasNext()) {
                    line_count++;
                    scan.nextLine();
                }
                scan.close();
                scan = new Scanner(in_file, "ISO-8859-1");
                try {
                    if (!c_conn.isClosed()) {
                        int id_pos = -1;
                        int name_pos = -0;
                        int parent_id_pos = 0;
                        int last_completion = 0;
                        boolean skip_table = false;

                        while (scan.hasNext() && !skip_table && result >= 0) {
                            rec_count++;
                            int completion = ((rec_count * 100) / line_count);
                            if ((completion % 10 == 0) && (completion > last_completion)) {
                                System.out.println(" " + completion + "% ");
                                last_completion = completion;
                            } else if ((completion % 2 == 0) && (completion > last_completion)) {
                                System.out.print(".");
                                last_completion = completion;
                            }
                            String line = scan.nextLine().trim();
                            if (line.startsWith("*")) {
                            } else if (line.startsWith("#")) {
                                line = line.replaceAll("#", "");
                                if (line.equalsIgnoreCase("recreate")) {
                                    if (table_found) {
                                        c_conn.createStatement().execute("drop table " + table_nm);
                                        table_found = false;
                                    }
                                } else {
                                    String[] split = line.split(splitter);
                                    String seperator = "";
                                    for (int i = 0; i < split.length; i++) {
                                        if (i > 0) {
                                            seperator = ",";
                                        }
                                        split[i] = split[i].toLowerCase();

                                        if (split[i].equals("name")) {
                                            sql_create = sql_create + seperator + split[i] + " varchar(256) ";
                                            sql_insert = sql_insert + seperator + split[i];
                                            sql_insert_param = sql_insert_param + seperator + "?";
                                            name_pos = i;
                                        } else if (split[i].equals("parent_id")) {
                                            parent_id_pos = i;
                                            sql_create = sql_create + seperator + split[i] + " int ";
                                            sql_insert = sql_insert + seperator + split[i];
                                            sql_insert_param = sql_insert_param + seperator + "?";
                                        } else if (split[i].equals("id")) {
                                            id_pos = i;
                                            sql_create = sql_create + seperator + split[i] + " int ";
                                            sql_insert = sql_insert + seperator + split[i];
                                            sql_insert_param = sql_insert_param + seperator + "?";
                                        } else if (!split[i].isEmpty()) {
                                            sql_create = sql_create + seperator + split[i] + " varchar(4096) ";
                                            sql_insert = sql_insert + seperator + split[i];
                                            sql_insert_param = sql_insert_param + seperator + "?";
                                        }
                                    }
                                    if (name_pos >= 0 && parent_id_pos >= 0 && id_pos >= 0) {
                                        sql_create = sql_create + ")";
                                        if (table_found) {
                                            System.out.println("Table found " + table_nm + " so no action taken as a recreate was not requested");
                                            skip_table = true;
                                        } else {
                                            c_conn.createStatement().execute(sql_create.replace("<TABLE_NAME>", table_nm));
                                            new_tag_created = true;
                                        }
                                        if (mem_schema_name != null) {
                                            reate_sql_l.add(sql_create.replace("<TABLE_NAME>", mem_schema_name + file_nm));
                                        }
                                        sql_insert_param = sql_insert_param + ")";
                                        sql_insert = sql_insert + ")";
                                        sql_insert = sql_insert + " values" + sql_insert_param;
                                        p_1 = c_conn.prepareStatement(sql_insert);
                                        col_count = p_1.getParameterMetaData().getParameterCount();
                                        table_found = true;
                                        table_created = true;
                                    } else {
                                        System.out.println("Error @" + rec_count + ": For " + table_nm + ".\nMandatory columns id,name and parent_id missing for " + table_nm + " (name column=" + name_pos + " parent_id column=" + parent_id_pos + " id column=" + id_pos + ")");
                                        skip_table = true;
                                    }
//                                    }
                                }
                            } else {
                                if (table_found && p_1 != null) {
                                    String[] split = line.trim().split(splitter);
                                    if (split.length == col_count) {
                                        for (int i = 0; (i < split.length); i++) {
                                            if (split[i].length() > max_allowd_filed_size) {
                                                split[i] = split[i].substring(0, max_allowd_filed_size);
                                                System.out.println("@" + rec_count + " size=" + split[i].length() + ") Data truncation " + split[i].substring(0, 20) + " ....max_allowed=" + max_allowd_filed_size);
                                            }
                                            if (i == id_pos) {
                                                if (split[i].matches("[0-9]+")) {
                                                    p_1.setInt(i + 1, new Integer(split[i]));
                                                } else {
                                                    System.out.println("Error @" + rec_count + ": invalid integer for id " + split[i]);
                                                    skip_table = true;
                                                }
                                            } else if (i == parent_id_pos) {
                                                if (split[i].matches("[0-9]+")) {
                                                    p_1.setInt(i + 1, new Integer(split[i]));
                                                } else {
                                                    System.out.println("Error @" + rec_count + ": invalid integer for id " + split[i]);
                                                    skip_table = true;
                                                }
                                            } else {
                                                p_1.setString(i + 1, split[i]);
                                            }
                                        }
                                        p_1.addBatch();
                                        if (!scan.hasNext() || (rec_count % 10000 == 0)) {
                                            int[] result_a = p_1.executeBatch();
                                            p_1.clearBatch();
                                            for (int i = 0; i < result_a.length; i++) {
                                                if (result_a[i] < 0) {
                                                    result = result_a[i];
                                                }
                                            }
                                        }
                                    } else {
                                        System.out.println("Error @" + rec_count + ": For " + table_nm + ". Data column count and heading column count mismatch for " + line + " Split.length=" + split.length + " Hedings=" + col_count);
                                        c_conn.createStatement().execute("drop table " + table_nm);
                                        skip_table = true;
                                    }
                                } else {
                                    System.out.println("Table " + table_nm + " not found and creation failed");
                                    skip_table = true;
                                }
                            }
                        }
                        System.out.println("Checking integrity of " + table_nm);
                        String test = "Select id,name,parent_id  from " + table_nm;
                        ResultSet r_1 = c_conn.createStatement().executeQuery(test);

                        boolean conflict_found = false;
                        Set<String> confict_s = new HashSet<>();
                        Set<Integer> id_s = new HashSet<>();
                        while (r_1.next()) {
                            String key = r_1.getInt("parent_id") + "_" + r_1.getString("name");
                            int cid = r_1.getInt("id");
                            if (!id_s.contains(cid)) {
                                id_s.add(cid);
                            } else {
                                System.out.println("Key violation. Duplicate id: " + cid + " in " + table_nm + " " + key);
                                conflict_found = true;
                            }
                            if (confict_s.contains(key)) {
                                System.out.println("Error duplicate : id=" + r_1.getInt("id") + "\tname=" + "\tparent=" + r_1.getInt("parent_id"));
                                conflict_found = true;
                            } else {
                                confict_s.add(key);
                            }
                        }
                        r_1.close();
                        scan.close();
                        String post_porc_sql = "";
                        confict_s.clear();

                        try {
                            if (conflict_found) {
                                System.out.println(" Removing table " + table_nm + ". Due to conflict in keys");
                                c_conn.createStatement().executeUpdate("DROP TABLE " + table_nm);
                            } else if (table_created) {
//                                boolean table_ok = false;
//                                post_porc_sql = "ALTER TABLE " + table_nm + " add CONSTRAINT key_ref_" + table_nm_for_indices + " UNIQUE (id)";
//                                result = new_con.createStatement().executeUpdate(post_porc_sql);
//                                if (result >= 0) {
//                                    System.out.println("key_ref_" + table_nm_for_indices + "... OK");
//                                    post_porc_sql = "CREATE INDEX parent_id_ref_" + table_nm_for_indices + " ON " + table_nm + "(parent_id)";
//                                    result = new_con.createStatement().executeUpdate(post_porc_sql);
//                                    if (result >= 0) {
//                                        System.out.println("index  parent_id_ref_" + table_nm_for_indices + "... OK");
//                                        post_porc_sql = "ALTER TABLE " + table_nm + " add CONSTRAINT parent_id_ref_" + table_nm_for_indices + "  FOREIGN KEY (parent_id) REFERENCES " + table_nm + "(id)";
//                                        result = new_con.createStatement().executeUpdate(post_porc_sql);
//                                    } else {
//                                        System.out.println("Error : could not add constaint 1, this table may not be usable");
//                                    }
//                                    if (result >= 0) {
//                                        System.out.println("Constaint parent_id_ref_" + table_nm_for_indices + " ... OK");
//                                        post_porc_sql = "CREATE INDEX unique_identification_" + table_nm_for_indices + " ON " + table_nm + "(name,parent_id)";
//                                        result = new_con.createStatement().executeUpdate(post_porc_sql);
//                                        if (result >= 0) {
//                                            System.out.println("unique_identification_" + table_nm_for_indices + " ... OK");
//                                            post_porc_sql = "ALTER table " + table_nm + " add CONSTRAINT unique_identification_" + table_nm_for_indices + " UNIQUE(name,parent_id)";
//                                            result = new_con.createStatement().executeUpdate(post_porc_sql);
//                                            if (result >= 0) {
//                                                table_ok = true;
//                                                System.out.println("Constraint unique_identification_" + table_nm_for_indices + " ... OK");
//                                                refreshTableTOFeatures();
//                                            } else {
//                                                System.out.println("Error : could not add constraint2, this table may not be usable");
//                                            }
//                                        } else {
//                                            System.out.println("Error : could not add constaint 1, this table may not be usable");
//                                        }
//                                    } else {
//                                        System.out.println("Error : could not add unique identification, this table may not be usable");
//                                    }
//
//                                } else {
//                                    System.out.println("Error : could not add primary key, this table may not be usable");
//                                }
//                                if (!table_ok) {
//                                    System.out.println(" Removing table " + table_nm + ". There were errors");
//                                    new_con.createStatement().executeUpdate("DROP TABLE " + table_nm);
//                                } else {
//                                    System.out.println("Indexing table " + table_nm + " ... OK");
//                                }
//
                            }
                        } catch (SQLException ex) {
                            System.out.println("Error 58p: " + post_porc_sql + "\n" + ex.getMessage());
                        }
                        System.out.println("Finished adding from " + in_file.getAbsolutePath() + " ended @" + Timing.getDateTime());

                    }
                } catch (SQLException ex) {
                    ProgressMonitor.cancel();
                    System.out.println("Error 58g: " + ex.getMessage());
                    StackTraceElement[] stac_a = ex.getStackTrace();
                    for (int i = 0; i < stac_a.length; i++) {
                        System.out.println("3700  " + stac_a[i].getClassName() + " => " + stac_a[i].getLineNumber());

                    }
                }
            } catch (FileNotFoundException ex) {
                ProgressMonitor.cancel();
                System.out.println("Error 58c: " + ex.getMessage());
            }
//            try {
//                if (c_conn != null && !c_conn.isClosed()) {
//                    c_conn.close();
//                }
//            } catch (SQLException ex) {
//                ProgressMonitor.cancel();
//                System.out.println("Error 68h: " + ex.getMessage());
//            }
        } else {
            System.out.println("Error 58o: failed to stablish connection");
        }
        return reate_sql_l;
    }
    /*
     MethodID=59
     */

    private boolean index_tagsources(Connection c_conn, String schema_name) {
        boolean table_ok = false;
        String last_sql = null;
//        ArrayList<String> parents_and_names_l = new ArrayList<>();
        try {
            if (c_conn != null) {
                ArrayList<String> table_l = getCurrentTables(c_conn, schema_name, 5);
                populateReportLineage(c_conn, instruct_map.get(DATABASE_NAME_DATA_FLAG));
                for (int i = 0; i < table_l.size(); i++) {
                    try {
                        String table_nm = get_correct_table_name(c_conn,
                                table_l.get(i), schema_name, 1200);
                        if (table_nm != null) {
                            Statement st = c_conn.createStatement();
                            String table_nm_for_indices = table_nm.split("\\.")[table_nm.split("\\.").length - 1];
                            if (table_nm_for_indices.toLowerCase().endsWith(TAGSOURCE_FLAG)) {
                                boolean key_ref_found = false;
//                                boolean parent_id_ref_found = false;
                                boolean parent_id_constraint_found = false;
                                boolean unique_identification_found = false;
                                boolean unique_identification_contraints_found = false;
                                boolean id_unique_identification_contraints_found = false;
                                boolean path_column_found = false;
                                boolean hash_column_found = false;
                                boolean lineagecol_found = false;
                                boolean uri_column_found = false;
                                String path_column_index_name = null;
                                String uri_column_index_name = null;
                                String hash_column_index_name = null;
                                String lin_column_index_name = null;

//                                String[] fr_keynm_a = null;
                                String[] keynm_a = null;
                                HashMap<String, String[]> constraints_map = get_key_contraints(c_conn, schema_name, table_nm);
//                                if (constraints_map.containsKey(FOREIGN_KEY_NAMES_FLAG)) {
//                                    fr_keynm_a = constraints_map.get(FOREIGN_KEY_NAMES_FLAG);
//                                    for (int j = 0; j < fr_keynm_a.length; j++) {
//                                        String c_key = fr_keynm_a[j];
//                                        if (c_key.toLowerCase().startsWith("parent_id_ref")) {
//                                            parent_id_ref_found = true;
//                                        }
//                                    }
//                                }
                                if (constraints_map.containsKey(ALL_INDEX_NAMES_FLAG)) {
                                    keynm_a = constraints_map.get(ALL_INDEX_NAMES_FLAG);
                                    for (int j = 0; j < keynm_a.length; j++) {
                                        String c_key = keynm_a[j];
                                        if (c_key.toLowerCase().startsWith("key_ref_")) {
                                            key_ref_found = true;
                                        } else if (c_key.toUpperCase().startsWith(UNIQUE_ID_INDEX_NAME_PREFIX)) {
                                            unique_identification_found = true;
                                            unique_identification_contraints_found = true;
                                        } else if (c_key.toUpperCase().startsWith(UNIQUE_ID_INDEX_NAME_PREFIX + "_ID_")) {
                                            id_unique_identification_contraints_found = true;
                                        } else if (c_key.toLowerCase().startsWith("parent_id_ref")) {
                                            parent_id_constraint_found = true;
                                        } else if (c_key.toLowerCase().startsWith("path_ref")) {
                                            path_column_index_name = table_nm.split("\\.")[0] + "." + c_key;
                                        } else if (c_key.toLowerCase().startsWith("hash_ref")) {
                                            hash_column_index_name = table_nm.split("\\.")[0] + "." + c_key;
                                        } else if (c_key.toLowerCase().startsWith("uri_ref")) {
                                            uri_column_index_name = table_nm.split("\\.")[0] + "." + c_key;
                                        } else if (c_key.toLowerCase().startsWith("lineage_ref")) {
                                            lin_column_index_name = table_nm.split("\\.")[0] + "." + c_key;
                                        }

                                    }
                                }


                                if (containsIgnoreCase(getColumns(c_conn, schema_name, table_nm), TAGSOURCE_PATHCOL_FLAG)) {
                                    path_column_found = true;
                                }

                                if (containsIgnoreCase(getColumns(c_conn, schema_name, table_nm), TAGSOURCE_HASHCOL_FLAG)) {
                                    hash_column_found = true;
                                }
                                if (containsIgnoreCase(getColumns(c_conn, schema_name, table_nm), TAGSOURCE_URICOL_FLAG)) {
                                    uri_column_found = true;
                                }
                                if (containsIgnoreCase(getColumns(c_conn, schema_name, table_nm), LINEAGE_HASHCOL_FLAG)) {
                                    lineagecol_found = true;
                                }
                                int result = 0;
                                if (!key_ref_found) {
                                    String post_porc_sql = "CREATE INDEX  key_ref_" + table_nm_for_indices + " ON " + table_nm + "(id)";
                                    last_sql = post_porc_sql;
                                    result = st.executeUpdate(post_porc_sql);
                                    try {
                                        post_porc_sql = "ALTER TABLE " + table_nm + " add CONSTRAINT key_ref_" + table_nm_for_indices + " UNIQUE(id)";
                                        last_sql = post_porc_sql;
                                        result = st.executeUpdate(post_porc_sql);
                                    } catch (SQLException ex) {
                                        System.out.println("Error 60h: " + ex.getMessage());
                                    }

                                }

                                if (result >= 0) {
//                                    if (!parent_id_ref_found) {
//                                        System.out.println("key_ref_" + table_nm_for_indices + "... OK");
//                                        String post_porc_sql = "CREATE INDEX parent_id_ref_" + table_nm_for_indices + " ON " + table_nm + "(parent_id)";
//                                        System.out.println("3104 "+post_porc_sql);
//                                        result = conn.createStatement().executeUpdate(post_porc_sql);
//                                        if (result >= 0) {
//                                            System.out.println("index  parent_id_ref_" + table_nm_for_indices + "... OK");
//                                        }
//                                    }
                                    //select parent_id, count(distinct name) as nmc from EGEN_DATAENTRY.efo_tagsource group by parent_id order by nmc

                                    if (!parent_id_constraint_found) {
//                                        parents_and_names_l.clear();
                                        System.out.println("Verifying parent_id references..");
                                        String check_parents_sql = "select id, parent_id from  " + table_nm + "";
                                        ResultSet ooft_r = c_conn.createStatement().executeQuery(check_parents_sql);
                                        ArrayList<Integer> id_l = new ArrayList<>();
                                        ArrayList<Integer> parent_id_l = new ArrayList<>();
                                        while (ooft_r.next()) {
                                            id_l.add(ooft_r.getInt(1));
                                            int parent_id = ooft_r.getInt(2);
                                            parent_id_l.add(parent_id);
//                                            String parent_nm = ooft_r.getString(3);//                                           
//                                            parent_nm =  parent_id+parent_nm;
//                                            parent_nm=parent_nm.toUpperCase();
//                                             if (parent_nm.length() > 128) {
//                                                parent_nm = parent_nm.substring(0, 128);
//                                            }
//                                            if (!parents_and_names_l.contains(parent_nm)) {
//                                                parents_and_names_l.add(parent_nm);
//                                            } else {
//                                                System.out.println("Duplicate parent+name combination " + parent_nm+" "+ooft_r.getString(3)+"\n");
//                                            }
                                        }
                                        ooft_r.close();
                                        parent_id_l.removeAll(id_l);
                                        boolean zero_found = id_l.contains(0);
                                        id_l.clear();
                                        if (!parent_id_l.isEmpty()) {
                                            System.out.println("Warning , there are records with non existing parents, trying to rectify this by creating psuedoparents...");
                                            if (!zero_found) {
                                                last_sql = "insert into " + table_nm + "(id, parent_id,NAME) values(0,0,'NA')";
                                                result = st.executeUpdate(last_sql);
                                            } else {
                                                result = 0;
                                            }
                                            last_sql = "update " + table_nm + " set parent_id=0 where id in (" + parent_id_l.toString().replace("]", "").replace("[", "") + ")";
                                            result = result = st.executeUpdate(last_sql);
                                        } else {
                                            result = 0;
                                        }

                                        if (result >= 0) {
                                            System.out.println("key_ref_" + table_nm_for_indices + "... OK");
                                            String post_porc_sql_1 = "CREATE INDEX parent_id_ref_" + table_nm_for_indices + " ON " + table_nm + "(parent_id)";
                                            last_sql = post_porc_sql_1;
//                                            System.out.println("3104 " + post_porc_sql_1);
                                            result = st.executeUpdate(post_porc_sql_1);
                                            if (result >= 0) {
                                                System.out.println("index  parent_id_ref_" + table_nm_for_indices + ".... OK");
                                            }
                                            if (result >= 0) {
                                                String intergity_sql = "select id,parent_id from " + table_nm;
                                                Set<Integer> f_id_s = new HashSet<>();
                                                Set<Integer> f_pid_s = new HashSet<>();
                                                last_sql = intergity_sql;
                                                ResultSet r = st.executeQuery(intergity_sql);
                                                while (r.next()) {
                                                    f_id_s.add(r.getInt(1));
                                                    f_pid_s.add(r.getInt(2));
                                                }
                                                r.close();
                                                f_pid_s.removeAll(f_id_s);
                                                f_id_s.clear();
                                                if (f_pid_s.isEmpty()) {
                                                    String post_porc_sql = "ALTER TABLE " + table_nm + " add CONSTRAINT parent_id_ref_" + table_nm_for_indices + "  FOREIGN KEY (parent_id) REFERENCES " + table_nm + "(id)";

                                                    last_sql = post_porc_sql;
                                                    result = st.executeUpdate(post_porc_sql);
                                                    if (result >= 0) {
                                                        System.out.println("Constaint parent_id_ref_" + table_nm_for_indices + " ... OK");
                                                    }
                                                } else {
                                                    System.out.println("Error 59k: intergirty check failed. The following parent ids are invalid " + f_pid_s);
                                                    f_pid_s.clear();
                                                }
                                            } else {
                                                System.out.println("Error 59a: could not add constaint 1, this table may not be usable");
                                            }
                                        } else {
                                            System.out.println("Error 59k: could not create hierarchical relationship when not all referencing parent_id s are valid");
                                        }
                                    }
                                    if (result >= 0) {
                                        if (!unique_identification_found) {
                                            String post_porc_sql = "CREATE INDEX unique_identification_" + table_nm_for_indices + " ON " + table_nm + "(name,parent_id,id)";
                                            last_sql = post_porc_sql;
                                            result = st.executeUpdate(post_porc_sql);
                                            if (result >= 0) {
                                                System.out.println("unique_identification_" + table_nm_for_indices + " ... OK");
                                            }
                                        }

                                        if (result >= 0) {
                                            if (!unique_identification_contraints_found) {
                                                String dup_check = "select parent_id, name, count(id) as id_c from " + table_nm + " group by parent_id,name";
                                                ResultSet dup_r = st.executeQuery(dup_check);
                                                boolean duplicate_found = false;
//                                                ArrayList<String> rectify_l = new ArrayList<>();
                                                int rect_id = 0;
                                                while (dup_r.next()) {
                                                    rect_id++;
                                                    if (dup_r.getInt(3) > 1) {
                                                        System.out.println("3882 duplicate " + dup_r.getString(1) + "\t" + dup_r.getString(2) + "\t" + dup_r.getInt(3));
                                                        duplicate_found = true;
//                                                        rectify_l.add("UPDATE " + table_nm + " set name='" + dup_r.getString(2).replaceAll("\\s", "_") + "_'+id where parent_id=" + dup_r.getInt(1) + " and name='" + dup_r.getString(2) + "'");
                                                    }
                                                }
                                                dup_r.close();
                                                if (!duplicate_found) {
                                                    String post_porc_sql = "ALTER table " + table_nm + " add CONSTRAINT unique_identification_" + table_nm_for_indices + " UNIQUE(name,parent_id,id)";
                                                    last_sql = post_porc_sql;
                                                    result = st.executeUpdate(post_porc_sql);
                                                    if (result >= 0) {
//                                                    table_ok = true; //new step added
                                                        System.out.println("Constraint unique_identification_" + table_nm_for_indices + " ... OK");
                                                    } else {
                                                        System.out.println("Error 59b: could not add constraint2, this table may not be usable");
                                                    }
                                                } else {
                                                    result = -1;
                                                    table_ok = false;
                                                }
                                            } else {
//                                                table_ok = true; //new step added
                                            }
//                                            if (result >= 0 && !id_unique_identification_contraints_found) {
//                                                String dup_check = "select id  from " + table_nm + " ";
//                                                ResultSet dup_r = st.executeQuery(dup_check);
//                                                boolean duplicate_found = false;
//                                                HashSet<Integer> id_l = new HashSet<Integer>();
//                                                while (dup_r.next()) {
//                                                    int sid = dup_r.getInt(1);
//                                                    if (id_l.contains(sid)) {
//                                                        System.out.println("3882 duplicate " + dup_r.getString(1) + "\t" + dup_r.getString(2) + "\t" + dup_r.getInt(3));
//                                                        duplicate_found = true;
//                                                    } else {
//                                                        id_l.add(sid);
//                                                    }
//                                                }
//                                                dup_r.close();
//                                                if (!duplicate_found) {
//                                                    String post_porc_sql = "ALTER table " + table_nm + " add CONSTRAINT unique_identification_id_" + table_nm_for_indices + " UNIQUE(ID)";
//                                                    last_sql = post_porc_sql;
//                                                    result = st.executeUpdate(post_porc_sql);
//                                                    if (result >= 0) {
////                                                    table_ok = true; //new step added
//                                                        System.out.println("Constraint unique_identification_" + table_nm_for_indices + " ... OK");
//                                                    } else {
//                                                        System.out.println("Error 59k: could not add constraint2, this table may not be usable");
//                                                    }
//                                                } else {
//                                                    result = -1;
//                                                    table_ok = false;
//                                                }
//                                            }
                                        } else {
                                            System.out.println("Error 59c: could not add constaint 1, this table may not be usable");
                                        }

                                        if (result >= 0) {
                                            if (!path_column_found) {
                                                String add_path_col = "ALTER table " + table_nm + " add COLUMN " + TAGSOURCE_PATHCOL_FLAG + " VARCHAR(2048)";
                                                last_sql = add_path_col;
                                                result = st.executeUpdate(add_path_col);
                                                if (result >= 0) {
                                                    System.out.println("Column " + TAGSOURCE_PATHCOL_FLAG + "  ... Added");
//                                                    result = populateTagPaths(c_conn, table_nm);
                                                }
                                            }
                                            if (!uri_column_found) {
                                                String add_path_col = "ALTER table " + table_nm + " add COLUMN " + TAGSOURCE_URICOL_FLAG + " VARCHAR(2048)";
                                                last_sql = add_path_col;
                                                result = st.executeUpdate(add_path_col);
                                                if (result >= 0) {
                                                    System.out.println("Column " + TAGSOURCE_URICOL_FLAG + "  ... Added");
//                                                    result = populateTagPaths(c_conn, table_nm);
                                                }
                                            }

                                        }
//                                        System.out.println("3916 result=" + result);
                                        if (result >= 0 && (path_column_index_name == null || uri_column_index_name == null)) {
                                            if (path_column_index_name == null) {
                                                String add_path_col_indx = "CREATE INDEX path_ref_" + table_nm_for_indices + " ON " + table_nm + "(" + TAGSOURCE_PATHCOL_FLAG + ")";
                                                System.out.println("Indexing " + table_nm);
                                                last_sql = add_path_col_indx;
                                                result = st.executeUpdate(add_path_col_indx);
                                                if (result >= 0) {
                                                    System.out.println("path_ref_" + table_nm_for_indices + "  ... Added");
                                                    table_ok = true;
                                                }
                                            }
                                            if (uri_column_index_name == null) {
                                                String add_path_col_indx = "CREATE INDEX uri_ref_" + table_nm_for_indices + " ON " + table_nm + "(" + TAGSOURCE_URICOL_FLAG + ")";
                                                System.out.println("Indexing " + table_nm);
                                                last_sql = add_path_col_indx;
                                                result = st.executeUpdate(add_path_col_indx);
                                                if (result >= 0) {
                                                    System.out.println("URI_ref_" + table_nm_for_indices + "  ... Added");
                                                    table_ok = true;
                                                } else {
                                                    table_ok = false;
                                                }
                                            }

                                        } else if (result >= 0 && (path_column_index_name != null)) {
                                            table_ok = true;
                                        } else {
                                            System.out.println("Error 49k: could not index path column");
                                        }

                                        if (result >= 0) {
                                            if (!hash_column_found) {
                                                String add_path_col = "ALTER table " + table_nm + " add COLUMN " + TAGSOURCE_HASHCOL_FLAG + " CHAR(40)";
                                                last_sql = add_path_col;
                                                result = st.executeUpdate(add_path_col);
                                                if (result >= 0) {
                                                    System.out.println("Column " + TAGSOURCE_HASHCOL_FLAG + "  ... Added");
//                                                    result = populateTagPaths(c_conn, table_nm);
                                                }
                                            }
                                        }

                                        if (result >= 0 && hash_column_index_name == null) {
                                            String add_hash_col_indx = "CREATE INDEX hash_ref_" + table_nm_for_indices + " ON " + table_nm + "(" + TAGSOURCE_HASHCOL_FLAG + ")";
                                            System.out.println("Indexing " + table_nm);
                                            last_sql = add_hash_col_indx;
                                            result = st.executeUpdate(add_hash_col_indx);
                                            if (result >= 0) {
                                                System.out.println("hash_ref_" + table_nm_for_indices + "  ... Added");
                                                table_ok = true;
                                            }
                                        } else if (result >= 0 && (hash_column_index_name != null)) {
                                            table_ok = true;
                                        } else {
                                            System.out.println("Error 49k: could not index path column");
                                        }

                                        if (result >= 0) {
                                            if (!lineagecol_found) {
                                                String add_lin_col = "ALTER table " + table_nm + " add COLUMN " + LINEAGE_HASHCOL_FLAG + " VARCHAR(128)";
                                                last_sql = add_lin_col;
                                                result = st.executeUpdate(add_lin_col);
                                                if (result >= 0) {
                                                    System.out.println("Column " + LINEAGE_HASHCOL_FLAG + "  ... Added");
                                                }
                                            }
                                        }

                                        if (result >= 0 && lin_column_index_name == null) {
                                            String add_hash_col_indx = "CREATE INDEX lineage_ref_" + table_nm_for_indices + " ON " + table_nm + "(" + LINEAGE_HASHCOL_FLAG + ")";
                                            System.out.println("Indexing " + table_nm);
                                            last_sql = add_hash_col_indx;
                                            result = st.executeUpdate(add_hash_col_indx);
                                            if (result >= 0) {
                                                System.out.println("lineage_ref_" + table_nm_for_indices + "  ... Added");
                                                table_ok = true;
                                            }
                                        } else if (result >= 0 && (lin_column_index_name != null)) {
                                            table_ok = true;
                                        } else {
                                            System.out.println("Error 49k: could not index lin_column_index_name");
                                        }
                                    } else {
                                        System.out.println("Error 49d: could not add unique identification, this table may not be usable");
                                    }
                                } else {
                                    System.out.println("Error 59e: could not add primary key, this table may not be usable");
                                }
                                if (!table_ok) {
                                    System.out.println(" Removing table " + table_nm + ". There were errors");
                                    last_sql = "DROP TABLE " + table_nm;
                                    st.executeUpdate(last_sql);
                                } else {
                                    System.out.println("Indexing table " + table_nm + " ... OK");
                                }

//                                if (table_ok && path_column_index_name != null) {
//                                    last_sql = "DROP INDEX " + path_column_index_name;
//                                    st.executeUpdate("DROP INDEX " + path_column_index_name);
//                                }

                                result = populateTagPaths(c_conn, table_nm, recreate_tag_source_paths);
                                if (result >= 0) {
                                    System.out.println("\nRecreating paths for " + table_nm + " ... OK");
//                                    String add_path_col_indx = "CREATE INDEX path_ref_" + table_nm_for_indices + " ON " + table_nm + "(" + TAGSOURCE_PATHCOL_FLAG + ")";
//                                    System.out.println(" Indexing "+ table_nm_for_indices +".. Please wait..");
//                                    result = conn.createStatement().executeUpdate(add_path_col_indx);
//                                    if (result >= 0) {
//                                        System.out.println("path_ref_" + table_nm_for_indices + "  ... Added");
//                                    }
                                } else {
                                    System.out.println("Recreating paths for " + table_nm + " ... Failed");
                                }
//                                } else {
//                                    result = populateTagPaths(table_nm);
//                                    if (result >= 0) {
//                                        System.out.println("Recreating paths for " + table_nm + " ... OK");
//                                    }
//                                }
                            }
                            st.close();
                        } else {
                            System.out.println("Erorr: table null ");
                        }
                    } catch (SQLException ex) {
                        ProgressMonitor.cancel();
                        System.out.println("Error 58h: " + ex.getMessage() + " last=" + last_sql);
                        ex.printStackTrace();
                    }
                }
                System.out.println("(2)Refresh available tables result=" + refreshTableTOFeatures(getConnection(), instruct_map.get(DATABASE_NAME_DATA_FLAG)));
                refreshErroressages(getConnection(), instruct_map.get(DATABASE_NAME_DATA_FLAG));
            } else {
                System.out.println("Error: failed to create connection");
            }
        } catch (Exception ex) {
            ProgressMonitor.cancel();
            System.out.println("Error 59h: " + ex.getMessage());
            ex.printStackTrace();
        }
        return table_ok;
    }

    /*
     REPORT_HIERARCHY
     */
//    private HashMap<Integer, ArrayList<Integer>> getReports(Connection c_conn, String schema_name) {
//        HashMap<Integer, Integer> id2parent = new HashMap<>();
//        HashMap<Integer, Integer> report2batch = new HashMap<>();
//        HashMap<Integer, ArrayList<Integer>> id2lin = new HashMap<>();
//        int result = 0;
//        if (c_conn != null) {
//            Statement stmt = null;
//            try {
//                stmt = c_conn.createStatement();
//                String rep_hier_tbl = get_correct_table_name(c_conn, "REPORT_HIERARCHY", schema_name, 85);
//                String report_tbl = get_correct_table_name(c_conn, "REPORT", schema_name, 85);
//                ResultSet r = stmt.executeQuery("select PARENTREPORT_ID ,CHILDREPORT_ID from " + rep_hier_tbl);
//                while (r.next()) {
//                    id2parent.put(r.getInt(2), r.getInt(1));
//                }
//                r.close();
//                r = stmt.executeQuery("select ID,REPORT_BATCH_ID from " + report_tbl);
//                while (r.next()) {
//                    if (!id2parent.containsKey(r.getInt(1))) {
//                        id2parent.put(r.getInt(1), r.getInt(2));
//                    }
//                }
//                r.close();
//
//                if (!id2parent.isEmpty()) {
//                    try {
//                        ArrayList<Integer> id_l = new ArrayList<>(id2parent.keySet());
//                        for (int i = 0; i < id_l.size(); i++) {
//                            Integer cid = id_l.get(i);
//                            ArrayList<Integer> line_l = new ArrayList<>();
//                            line_l.add(cid);
//                            cid = id2parent.get(cid);
//                            if (id2parent.containsKey(cid)) {
//                                line_l.add(cid);
//                                boolean more = true;
//                                while (more && id2parent.get(cid) != null) {
//                                    int g_pid = id2parent.get(cid);
//                                    if (line_l.contains(g_pid)) {
//                                        more = false;
//                                    } else {
//                                        cid = id2parent.get(cid);
//                                        line_l.add(g_pid);
//                                    }
//                                }
//                                id2lin.put(id_l.get(i), line_l);
//                            } else {
//                                line_l.add(report2batch.get(id_l.get(i)));
//                                id2lin.put(id_l.get(i), line_l);
//                            }
//                        }
//                        id2parent.clear();
//                    } catch (Exception ex) {
//                        result = -1;
//                        System.out.println("Error: " + ex.getMessage());
//                    }
//                }
//                stmt.close();
//            } catch (SQLException ex) {
//                result = -1;
//                System.out.println("Error: " + ex.getMessage());
//            } finally {
//                try {
//                    if (stmt != null && !stmt.isClosed()) {
//                        stmt.close();
//                    }
//                } catch (SQLException ex) {
//                    result = -1;
//                    System.out.println("Error: " + ex.getMessage());
//                }
//            }
//        }
////        System.out.println("4457  " + id2lin.get(464));
//        return id2lin;
//    }
    private HashMap<Integer, ArrayList<Integer>> getLineage(Connection c_conn, String tabl_nm) {
        int result = 0;
        HashMap<Integer, Integer> id2parent = new HashMap<>();
        HashMap<Integer, ArrayList<Integer>> id2lin = new HashMap<>();
        if (c_conn != null) {
            Statement stmt = null;
            try {
                stmt = c_conn.createStatement();
                ResultSet r = stmt.executeQuery("select id,PARENT_ID from " + tabl_nm);
                while (r.next()) {
                    id2parent.put(r.getInt(1), r.getInt(2));
                }
                r.close();

                if (!id2parent.isEmpty()) {
                    try {
                        ArrayList<Integer> id_l = new ArrayList<>(id2parent.keySet());
                        for (int i = 0; i < id_l.size(); i++) {
                            Integer cid = id_l.get(i);
                            ArrayList<Integer> line_l = new ArrayList<>();
                            line_l.add(cid);
                            line_l.add(id2parent.get(cid));
                            cid = id2parent.get(cid);
                            boolean more = true;
                            while (more && id2parent.get(cid) != null) {
                                int g_pid = id2parent.get(cid);
                                if (line_l.contains(g_pid)) {
                                    more = false;
                                } else {
                                    cid = id2parent.get(cid);
                                    line_l.add(g_pid);
                                }
                            }
                            id2lin.put(id_l.get(i), line_l);
                        }
                        id2parent.clear();
                    } catch (Exception ex) {
                        result = -1;
                        System.out.println("Error: " + ex.getMessage());
                    }
                }
                stmt.close();
            } catch (SQLException ex) {
                result = -1;
                System.out.println("Error: " + ex.getMessage());
            } finally {
                try {
                    if (stmt != null && !stmt.isClosed()) {
                        stmt.close();
                    }
                } catch (SQLException ex) {
                    result = -1;
                    System.out.println("Error: " + ex.getMessage());
                }
            }
        }

        return id2lin;
    }

    private int populateReportLineage(Connection c_conn, String schema_name) {
        int result = 0;

        if (c_conn != null) {
            Statement stmt = null;
            try {
                stmt = c_conn.createStatement();
                String repor_tbl = get_correct_table_name(c_conn, "report", schema_name, 85);
//                System.out.println("\n\n\n\n 4514 " + repor_tbl + "\n\n\n");
                if (repor_tbl != null) {
                    if (!containsIgnoreCase(getColumns(c_conn, schema_name, repor_tbl), LINEAGE_HASHCOL_FLAG)) {
                        String add_lin_col = "ALTER table " + repor_tbl + " add COLUMN " + LINEAGE_HASHCOL_FLAG + " VARCHAR(128)";
                        result = stmt.executeUpdate(add_lin_col);
                        if (result >= 0) {
                            System.out.println("Column " + LINEAGE_HASHCOL_FLAG + "  ... Added");
                        } else {
                            System.out.println("Column " + LINEAGE_HASHCOL_FLAG + "  ... Adding failed -- ERROR");
                        }
                    }
//                    System.out.println("4527 result=" + result);
                    HashMap<String, String[]> constraints_map = get_key_contraints(c_conn, schema_name, "REPORT");
                    boolean key_found = false;
                    if (constraints_map.containsKey(ALL_INDEX_NAMES_FLAG)) {
                        String[] keynm_a = constraints_map.get(ALL_INDEX_NAMES_FLAG);
                        for (int j = 0; j < keynm_a.length; j++) {
                            if (keynm_a[j].toLowerCase().startsWith("lineage_ref")) {
                                key_found = true;
                            }

                        }
                    }
                    if (result >= 0 && !key_found) {
                        String add_hash_col_indx = "CREATE INDEX lineage_ref_report ON " + repor_tbl + "(" + LINEAGE_HASHCOL_FLAG + ")";
                        System.out.println("Indexing REPORT");
                        result = stmt.executeUpdate(add_hash_col_indx);
                        if (result >= 0) {
                            System.out.println("lineage_ref_REPORT  ... Added");

                        }
                    }
//                    HashMap<Integer, ArrayList<Integer>> lienage_map = getReports(c_conn, schema_name);
//                    PreparedStatement p_set_Cpath = c_conn.prepareStatement("update " + repor_tbl + " SET " + LINEAGE_HASHCOL_FLAG + "=? where id=?");
//
//                    ArrayList<Integer> lin_key_l = new ArrayList<>(lienage_map.keySet());
//                    for (int i = 0; (i < lin_key_l.size() && result >= 0); i++) {
//                        p_set_Cpath.setString(1, "\"" + lienage_map.get(lin_key_l.get(i)).toString().
//                                replace("[", "").replace("]", "").
//                                replaceAll("\\s", "").replaceAll(",", "\",\"") + "\"");
//                        p_set_Cpath.setInt(2, lin_key_l.get(i));
//                        p_set_Cpath.addBatch();
//                        if (result > -2 && (i % 500 == 0 || (i == (lin_key_l.size() - 1)))) {
//                            int[] tmp_result_a = p_set_Cpath.executeBatch();
//                            for (int j = 0; (j < tmp_result_a.length && result >= 0); j++) {
//                                if (tmp_result_a[j] < 0) {
//                                    result = tmp_result_a[j];
//                                }
//                            }
//                            p_set_Cpath.clearParameters();
//                        }
//                    }
                }

            } catch (SQLException ex) {
                result = -1;
                System.out.println("Error: " + ex.getMessage());
//                ex.printStackTrace();
            } finally {
                try {
                    if (stmt != null && !stmt.isClosed()) {
                        stmt.close();
                    }
                } catch (SQLException ex) {
                    result = -1;
                    System.out.println("Error: " + ex.getMessage());
                }
            }
        }
        return result;
    }

    private int populateTagPaths(Connection c_conn, String tabl_nm, boolean restpaths) {
        System.out.println("Creating paths in " + tabl_nm);
        int result = 0;
        if (c_conn != null) {
            Statement stmt = null;
            try {
                HashMap<Integer, ArrayList<Integer>> lienage_map = getLineage(c_conn, tabl_nm);
                stmt = c_conn.createStatement();
                ResultSet r_1 = stmt.executeQuery("select id, parent_id, name, path,uri from " + tabl_nm + "");
                PreparedStatement p_set_Cpath = c_conn.prepareStatement("update "
                        + "" + tabl_nm + " set " + TAGSOURCE_PATHCOL_FLAG + "=?,"
                        + TAGSOURCE_HASHCOL_FLAG + "=?," + TAGSOURCE_URICOL_FLAG + "=?, " + LINEAGE_HASHCOL_FLAG + "=? "
                        + "where id=?");
//                PreparedStatement p_set_Chash = c_conn.prepareStatement("update " + tabl_nm + " set " + TAGSOURCE_HASHCOL_FLAG + "=? where id=?");
                HashMap<Integer, String[]> id2info_map = new HashMap();
//                HashMap<Integer, String> id2current_path_map=new HashMap<>();
                int count = 0;
                while (r_1.next()) {
                    count++;
                    if (count % 1000 == 0) {
                        System.out.print(".");
                        if (count % 80000 == 0) {
                            System.out.println("");
                        }
                    }
                    String[] info_a = new String[]{r_1.getString(2), r_1.getString(3), r_1.getString(4), r_1.getString(5)};
                    id2info_map.put(r_1.getInt(1), info_a);
                }
                r_1.close();
                System.out.println("#rows=" + count);
                ArrayList<Integer> id_l = new ArrayList<>(id2info_map.keySet());
                int lastp = 0;
//                if (tabl_nm.toUpperCase().contains("BIOASSAY_ONTOLOGY_TAGSOURCE")) {
//                    System.out.println("3825 +" + id_l);
//                }
                for (int i = 0; (i < id_l.size() && result > -1); i++) {
                    int prog = (i * 100) / id_l.size();
                    if (prog > lastp) {
                        lastp = prog;
                        if (prog % 10 == 0) {
                            System.out.print(prog + "%");
                        }
                        if (prog % 2 == 0) {
                            System.out.print(".");
                        }
                    }

//                    if (tabl_nm.toUpperCase().contains("BIOASSAY_ONTOLOGY_TAGSOURCE")) {
//                        System.out.println("3837 c_id=" + c_id);
//                    }
                    int c_id = id_l.get(i);
                    int p_id = 0;
                    String[] info_a = id2info_map.get(c_id);
                    if (info_a[0].matches("[0-9]+")) {
                        p_id = new Integer(info_a[0]);
                    }
//                    if(p_id==0)
//                        System.out.println("4140 "+Arrays.deepToString(info_a));
                    String parents = info_a[1];
                    ArrayList<String> hash_path_l = new ArrayList<>();
                    hash_path_l.add(info_a[1]);
                    count = Integer.MAX_VALUE;
//                    if (tabl_nm.toUpperCase().contains("BIOASSAY_ONTOLOGY_TAGSOURCE")) {
//                        count = 999999;//Integer.MAX_VALUE;
//                    }

                    while (p_id >= 0 && count > 0) {
                        count--;
                        if (count == 0) {
                            System.out.println("\n Error. The tag source did not have a root "
                                    + "(atleast one element with it self as the parent). "
                                    + "This I can not process. ");
                            result = -2;
                        }
                        if (id2info_map.containsKey(p_id)) {
                            String[] new_info_a = id2info_map.get(p_id);
                            if (new_info_a[0].matches("[0-9]+")) {
                                int n_p_id = new Integer(new_info_a[0]);
                                if (n_p_id != p_id) {
                                    p_id = n_p_id;
                                } else {
                                    p_id = -1;
                                }
                                parents = new_info_a[1] + "/" + parents;
                                hash_path_l.add(new_info_a[1]);
                            } else {
                                p_id = -1;
                            }
                        } else {
                            p_id = -1;
                        }
//                        if (tabl_nm.toUpperCase().contains("BIOASSAY_ONTOLOGY_TAGSOURCE")) {
//                            System.out.println("3853 " + p_id + " " + Arrays.deepToString(id3info_map.get(p_id)) + " " + parents);
//                        }
                    }
                    String tbl_local_name = tabl_nm.split("\\.")[tabl_nm.split("\\.").length - 1];

                    Collections.sort(hash_path_l);
                    String hash_path = hash_path_l.get(0);
                    for (int j = 1; j < hash_path_l.size(); j++) {
                        hash_path = hash_path + "/" + hash_path_l.get(j);
                    }
                    hash_path_l.clear();
//                    String hash_path = parents;//tbl_local_name.split("_TAGSOURCE")[0] + "/" +
//                    parents = parents + "(" + tbl_local_name + "=" + c_id + ")";
//                    if (tabl_nm.toUpperCase().contains("BIOASSAY_ONTOLOGY_TAGSOURCE")) {
//                        System.out.println("\n\n\t3880" + parents);
//                    }
                    String old_path = info_a[2];
                    if (restpaths || old_path == null || !old_path.equalsIgnoreCase(parents)) {
                        p_set_Cpath.setString(1, parents);
                        if (info_a[3] == null || info_a[3].equalsIgnoreCase("NA") || info_a[3].equalsIgnoreCase("NULL")) {
                            info_a[3] = parents;
                        }
                        p_set_Cpath.setString(2, encript(info_a[3]));
                        p_set_Cpath.setString(3, info_a[3]);
                        if (lienage_map.containsKey(c_id)) {
                            p_set_Cpath.setString(4, "\"" + lienage_map.get(c_id).toString().replace("[", "").replace("]", "").replaceAll("\\s", "").replaceAll(",", "\",\"") + "\"");
                        } else {
                            p_set_Cpath.setString(4, "-1");
                        }
                        p_set_Cpath.setInt(5, c_id);
                        p_set_Cpath.addBatch();
                    }
//                    if (i < 2) {
//                        System.out.println("4153 " + hash_path);
//                    }
                    if (result > -2 && (i % 500 == 0 || (i == (id_l.size() - 1)))) {
                        int[] tmp_result_a = p_set_Cpath.executeBatch();
                        for (int j = 0; (j < tmp_result_a.length && result >= 0); j++) {
                            if (tmp_result_a[j] < 0) {
                                result = tmp_result_a[j];
                            }
                        }
                        p_set_Cpath.clearParameters();
                    }

                }
                id2info_map.clear();
                stmt.close();
            } catch (SQLException ex) {
                result = -1;
                System.out.println("Error: " + ex.getMessage());
            } finally {
                try {
                    if (stmt != null && !stmt.isClosed()) {
                        stmt.close();
                    }
                } catch (SQLException ex) {
                    result = -1;
                    System.out.println("Error: " + ex.getMessage());
                }
            }
        }
        return result;
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

    private ArrayList<String> addDataFromFile(Connection c_conn, File in_file, String schema_name) {
        ArrayList<String> create_sql_l = new ArrayList<>();
        System.out.println("Adding from " + in_file.getAbsolutePath() + " started @" + Timing.getDateTime());
        if (c_conn != null) {
            String splitter = "\\|\\|";
//            Connection new_con = conn;
            String c_table = null;
            ArrayList<String> table_l = getCurrentTables(c_conn, schema_name, 6);
            try {
                Scanner scan = new Scanner(in_file, "ISO-8859-1");
                int result = 0;
                try {
                    int line_count = 0;
                    while (scan.hasNext()) {
                        line_count++;
                        scan.nextLine();
                    }
                    scan.close();
                    scan = new Scanner(in_file, "ISO-8859-1");
                    if (!c_conn.isClosed()) {
                        PreparedStatement p_1 = null;
                        int col_count = 0;
                        int rec_count = 0;
                        boolean insert_active = false;
                        boolean skip_table = false;
                        int last_completion = 0;
                        while (scan.hasNext() && !skip_table && result >= 0) {
                            rec_count++;
                            int completion = ((rec_count * 100) / line_count);
                            if (completion % 10 == 0 && completion > last_completion) {
                                System.out.print(" " + completion + "% ");
                                last_completion = completion;
                            }

                            String line = scan.nextLine().trim();
                            if (line.startsWith("#")) {
                                line = line.replaceAll(splitter, "");
                                if (insert_active && p_1 != null) {
                                    try {
                                        int[] re = p_1.executeBatch();
                                        p_1.clearParameters();
                                        insert_active = false;
                                    } catch (SQLException ex) {
                                        System.out.println("Error 4h: " + ex.getMessage());
                                    }
                                }
                                String command = line.replaceAll("#", "").split(splitter)[0];
                                if (command.startsWith("TABLE=")) {
                                    c_table = command.replace("TABLE=", "").trim();
                                    String table_name_in_db = get_correct_table_name(
                                            c_conn, c_table, schema_name, 1300);
                                    if (table_name_in_db == null || !table_l.contains(table_name_in_db)) {
                                        skip_table = false;
                                    } else {
                                        skip_table = true;
                                        c_table = table_name_in_db;
                                    }
                                } else if (command.startsWith("INSERT")) {
                                    System.out.println("236 " + command);
                                    try {
                                        p_1 = c_conn.prepareStatement(command);
                                        col_count = p_1.getParameterMetaData().getParameterCount();
                                    } catch (SQLException ex) {
                                        System.out.println("Error 4i: " + ex.getMessage());
                                    }

                                } else if (command.startsWith("SPLITER")) {
                                    splitter = command.split("SPLITER")[1];
                                } else if (command.startsWith("CREATE")) {
                                    try {
                                        create_sql_l.add(command);
                                        c_conn.createStatement().execute(command);
                                    } catch (SQLException ex) {
                                        System.out.println("Error 4e: " + ex.getMessage() + " c_table=" + c_table);
                                    }
                                } else if (command.startsWith("ALTER")) {
                                    System.out.println("1063 " + command);
                                    try {
                                        c_conn.createStatement().execute(command);
                                    } catch (SQLException ex) {
                                        System.out.println("Error 4e: " + ex.getMessage() + " c_table=" + c_table);
                                    }
                                }
                            } else if (p_1 != null && c_table != null && !skip_table) {
                                insert_active = true;
                                String[] data_a = line.split(splitter);
                                if (data_a.length == col_count) {
                                    try {
                                        for (int i = 0; i < data_a.length; i++) {
                                            boolean is_integer = false;
                                            try {
                                                if (p_1.getParameterMetaData() != null && p_1.getParameterMetaData().getParameterType(i + 1) == Types.INTEGER) {
                                                    is_integer = true;
                                                }
                                            } catch (SQLException ex) {
                                            }
                                            if (is_integer) {
                                                data_a[i] = data_a[i].trim();
                                                if (data_a[i].matches("[0-9]+")) {
                                                    p_1.setInt(i + 1, new Integer(data_a[i]));
                                                } else {
                                                    System.out.println("Error 4b: " + (i + 1) + " Integer expecting and found String " + line);
                                                    result = -1;
                                                }
                                            } else {
                                                p_1.setString(i + 1, data_a[i]);
                                            }
                                        }
                                        p_1.addBatch();
                                        if ((rec_count % 50000 == 0)) {
                                            int[] re = p_1.executeBatch();
                                            result = 1;
                                            for (int i = 0; i < re.length; i++) {
                                                if (re[i] < 0) {
                                                    result = -1;
                                                }
                                            }
                                            p_1.clearParameters();
                                        }
                                    } catch (SQLException ex) {
                                        System.out.println("Error 4j: " + ex.getMessage());
                                    }

                                } else {
                                    System.out.println("Error 4a: split lenght mismatch " + line);
                                    result = -1;
                                }
                            } else if (c_table != null && table_l.contains(c_table)) {
                                skip_table = true;
                                System.out.println("1093 SKipping " + c_table);
                            }
                        }
                        scan.close();
//                        if (c_table == null) {
//                            System.out.println("Error 4m: Table name was not found. Specify this with TABLE=<NAME_OF_THE_TABLE>");
//                        }
                        if (p_1 != null) {
                            try {
                                p_1.close();
                            } catch (SQLException ex) {
                                System.out.println("Error 4h: " + ex.getMessage());
                            }
                        }
//                    else {
//                        System.out.println("Error 4l: Insert statement not found");
//                    }
//                        try {
//                            c_conn.close();
//                        } catch (SQLException ex) {
//                            ProgressMonitor.cancel();
//                            System.out.println("Error 4k: " + ex.getMessage());
//                        }
                    }
                } catch (SQLException ex) {
                    ProgressMonitor.cancel();
                    System.out.println("Error 4g: " + ex.getMessage());
                }
            } catch (FileNotFoundException ex) {
                ProgressMonitor.cancel();
                System.out.println("Error 4c: " + ex.getMessage());
            }
        }
        return create_sql_l;
    }

    /*
     MethodID=4
     */
    private boolean create_user_connections_derby(GlassFish glassfish) {
        try {
            //egen_userManagement_resource  
            String[] create_pool_usermanage_a = new String[10];
            create_pool_usermanage_a[0] = "--datasourceclassname=" + instruct_map.get(DATASOURCECLASSNAME_FLAG);
            create_pool_usermanage_a[1] = "--restype=javax.sql.DataSource";
            create_pool_usermanage_a[2] = "--driverclassname=" + instruct_map.get(DRIVERCLASSNAME_FLAG);
            create_pool_usermanage_a[3] = "--maxpoolsize=64";
            create_pool_usermanage_a[4] = "--pooling=true";
            create_pool_usermanage_a[5] = "--steadypoolsize=10";
            create_pool_usermanage_a[6] = "--idletimeout=60";
            create_pool_usermanage_a[7] = "--property";
            create_pool_usermanage_a[8] = "databasename=" + instruct_map.get(DATABASE_NAME_USERS_FLAG) + ":user=" + instruct_map.get(unm_usermanage_flag) + ":portnumber="
                    + instruct_map.get(derby_port_flag) + ":port=" + instruct_map.get(derby_port_flag) + ":password=" + instruct_map.get(unm_usermanage_pass_flag) + ":url=\"jdbc:" + instruct_map.get(db_host_flag) + ":"
                    + instruct_map.get(derby_port_flag) + "/" + instruct_map.get(DATABASE_NAME_USERS_FLAG) + "\":";

            create_pool_usermanage_a[9] = "egen_userManagement_pool";
            CommandRunner commandRunner = glassfish.getCommandRunner();
            CommandResult commandResult = commandRunner.run("create-jdbc-connection-pool", create_pool_usermanage_a);
            print_connection_error(commandResult.getFailureCause(), "4a");
            String[] create_resource_usermanage_a = new String[2];
            create_resource_usermanage_a[0] = "--connectionpoolid=egen_userManagement_pool";
            create_resource_usermanage_a[1] = "egen_userManagement_resource";
            commandResult = commandRunner.run("create-jdbc-resource", create_resource_usermanage_a);
            print_connection_error(commandResult.getFailureCause(), "4b");

            commandResult = commandRunner.run("ping-connection-pool", "egen_userManagement_pool");
            print_connection_error(commandResult.getFailureCause(), "5c");
            System.out.println("Connection to egen_userManagement_pool was " + commandResult.getExitStatus());

            String[] create_realm_usermanage_a = new String[4];
            create_realm_usermanage_a[0] = "--classname=com.sun.enterprise.security.auth.realm.jdbc.JDBCRealm";
            create_realm_usermanage_a[1] = "--property";
            create_realm_usermanage_a[2] = "digest-algorithm=\"" + instruct_map.get(DB_AUTHENTICATION_ALGORITHM_FLAG) + "\":user-name-column=username:password-column=password:group-name-column=groupname:group-table="
                    + instruct_map.get(DATABASE_NAME_USERS_FLAG) + ".groups:user-table=" + instruct_map.get(DATABASE_NAME_USERS_FLAG)
                    + ".useraccounts:datasource-jndi=egen_userManagement_resource:jaas-context=\"jdbcRealm\"";
            create_realm_usermanage_a[3] = "UsermangeRealm";
            commandResult = commandRunner.run("create-auth-realm", create_realm_usermanage_a);
            print_connection_error(commandResult.getFailureCause(), "4c");

            //JAVA mail
            if (create_mailer_a != null) {
                commandResult = commandRunner.run("create-javamail-resource", create_mailer_a);
                System.out.println("Connection to create-javamail-resource was " + commandResult.getExitStatus());
                print_connection_error(commandResult.getFailureCause(), "4d");
            } else {
                System.out.println("Error: mail source setup failed");
            }
        } catch (GlassFishException ex) {
            return false;
        }
        return true;
    }

    /*
     MethodID=5
     */
    private boolean create_dataEntry_connections_derby(GlassFish glassfish) {
        try {
            //max-queue-size="256"
            /*
             * --pooling
             * --
             */
            /*Create egen_dataEntry_resource*/
            String[] create_pool_dataAccess_a = new String[9];
            create_pool_dataAccess_a[0] = "--datasourceclassname=org.apache.derby.jdbc.ClientDataSource";
            create_pool_dataAccess_a[1] = "--restype=javax.sql.DataSource";
            create_pool_dataAccess_a[2] = "--driverclassname=org.apache.derby.jdbc.ClientDriver";
            create_pool_dataAccess_a[3] = "--maxpoolsize=64";
            create_pool_dataAccess_a[4] = "--pooling=true";
            create_pool_dataAccess_a[5] = "--steadypoolsize=10";
            create_pool_dataAccess_a[6] = "--property";
            create_pool_dataAccess_a[7] = "databasename=" + instruct_map.get(DATABASE_NAME_DATA_FLAG)
                    + ":user=" + instruct_map.get(unm_gendataentry_flag)
                    + ":portnumber=" + instruct_map.get(derby_port_flag)
                    + ":port=" + instruct_map.get(derby_port_flag)
                    + ":password=" + instruct_map.get(unm_gendataentry_pass_flag) + ""
                    + ":create=false";
            create_pool_dataAccess_a[8] = "egen_dataEntry_pool";
            CommandRunner commandRunner = glassfish.getCommandRunner();
            CommandResult commandResult = commandRunner.run("create-jdbc-connection-pool", create_pool_dataAccess_a);
            print_connection_error(commandResult.getFailureCause(), "5a");
            String[] create_resource_dataAccess_a = new String[2];
            create_resource_dataAccess_a[0] = "--connectionpoolid=egen_dataEntry_pool";
            create_resource_dataAccess_a[1] = "egen_dataEntry_resource";
            commandRunner = glassfish.getCommandRunner();
            commandResult = commandRunner.run("create-jdbc-resource", create_resource_dataAccess_a);
            print_connection_error(commandResult.getFailureCause(), "5b");
            commandRunner = glassfish.getCommandRunner();
            commandResult = commandRunner.run("ping-connection-pool", "egen_dataEntry_pool");
            print_connection_error(commandResult.getFailureCause(), "5c");
            System.out.println("Connection to egen_dataEntry_pool was " + commandResult.getExitStatus());

            /*Create egen_dataUpdate_resource   list-jdbc-connection-pools*/
            String[] update_pool = new String[9];
            update_pool[0] = "--datasourceclassname=org.apache.derby.jdbc.ClientDataSource";
            update_pool[1] = "--restype=javax.sql.DataSource";
            update_pool[2] = "--driverclassname=org.apache.derby.jdbc.ClientDriver";
            update_pool[3] = "--maxpoolsize=64";
            update_pool[4] = "--pooling=true";
            update_pool[5] = "--steadypoolsize=10";
            update_pool[6] = "--property";
            update_pool[7] = "databasename=" + instruct_map.get(DATABASE_NAME_DATA_FLAG)
                    + ":user=" + instruct_map.get(unm_gendataUpdate_flag) + ""
                    + ":portnumber=" + instruct_map.get(derby_port_flag) + ""
                    + ":port=" + instruct_map.get(derby_port_flag) + ""
                    + ":password=" + instruct_map.get(unm_gendataUpdate_pass_flag) + ""
                    + ":create=false";
            update_pool[8] = "egen_dataUpdate_pool";
            CommandRunner update_commandRunner = glassfish.getCommandRunner();
            CommandResult update_commandResult = update_commandRunner.run("create-jdbc-connection-pool", update_pool);
            print_connection_error(update_commandResult.getFailureCause(), "5g");
            String[] update_resource_a = new String[2];
            update_resource_a[0] = "--connectionpoolid=egen_dataUpdate_pool";
            update_resource_a[1] = "egen_dataUpdate_resource";
            update_commandRunner = glassfish.getCommandRunner();
            update_commandResult = update_commandRunner.run("create-jdbc-resource", update_resource_a);
            print_connection_error(update_commandResult.getFailureCause(), "5h");
            update_commandRunner = glassfish.getCommandRunner();
            update_commandResult = update_commandRunner.run("ping-connection-pool", "egen_dataUpdate_pool");
            System.out.println("Connection egen_dataUpdate_pool was " + update_commandResult.getExitStatus());
        } catch (GlassFishException ex) {
            ex.printStackTrace();
            System.out.println("Error 5j " + ex.getMessage());
            return false;
        } catch (Exception ex) {
            ex.printStackTrace();
            System.out.println("Error 5i " + ex.getMessage());
            return false;
        }
        return true;
    }

    /*
     MethodID=5.1
     */
    private boolean create_dataEntry_connections_derby_memory(GlassFish glassfish) {
        try {
            /*Create egen_dataView_resource and pool*/
            String[] view_pool = new String[11];
            view_pool[0] = "--datasourceclassname=org.apache.derby.jdbc.ClientDataSource";
            view_pool[1] = "--restype=javax.sql.DataSource";
            view_pool[2] = "--driverclassname=org.apache.derby.jdbc.ClientDriver";
            view_pool[3] = "--maxpoolsize=64";
            view_pool[4] = "--pooling=true";
            view_pool[5] = "--steadypoolsize=10";

            view_pool[6] = "--isconnectvalidatereq=true";
            view_pool[7] = "--validationmethod=table";
            view_pool[7] = "--validationtable="
                    + get_correct_table_name(getConnection_memory(false, 15),
                    "CONFIG", instruct_map.get(DATABASE_NAME_DATA_FLAG), 1400);

            view_pool[8] = "--property";
            view_pool[9] = "databasename=memory\\:" + instruct_map.get(DATABASE_NAME_DATA_FLAG)
                    + ":user=" + instruct_map.get(unm_gendataview_flag) + ""
                    + ":portnumber=" + instruct_map.get(derby_port_MEM_flag) + ""
                    + ":port=" + instruct_map.get(derby_port_MEM_flag) + ""
                    + ":password=" + instruct_map.get(unm_gendataview_pass_flag) + ""
                    + ":create=false";
//                    + ":url=\"" + dbURL_dataEntry_url + "\":";
            view_pool[10] = "egen_dataView_pool";
            CommandRunner view_commandRunner = glassfish.getCommandRunner();
            CommandResult view_commandResult = view_commandRunner.run("create-jdbc-connection-pool", view_pool);
            print_connection_error(view_commandResult.getFailureCause(), "5d");
            String[] view_resource_a = new String[2];
            view_resource_a[0] = "--connectionpoolid=egen_dataView_pool";
            view_resource_a[1] = "egen_dataView_resource";
            view_commandResult = view_commandRunner.run("create-jdbc-resource", view_resource_a);
            print_connection_error(view_commandResult.getFailureCause(), "5e");
            view_commandRunner = glassfish.getCommandRunner();
            view_commandResult = view_commandRunner.run("ping-connection-pool", "egen_dataView_pool");
            print_connection_error(view_commandResult.getFailureCause(), "5f");
            System.out.println("Connection to egen_dataView_pool was " + view_commandResult.getExitStatus());
        } catch (GlassFishException ex) {
            ex.printStackTrace();
            System.out.println("Error 5j " + ex.getMessage());
            return false;
        } catch (Exception ex) {
            ex.printStackTrace();
            System.out.println("Error 5i " + ex.getMessage());
            return false;
        }
        return true;
    }
    /*
     MethodID=5.1
     */

    private boolean create_dataEntry_connections_derby_disk(GlassFish glassfish) {
        try {
            /*Create egen_dataView_resource and pool*/
            String[] view_pool = new String[11];
            view_pool[0] = "--datasourceclassname=org.apache.derby.jdbc.ClientDataSource";
            view_pool[1] = "--restype=javax.sql.DataSource";
            view_pool[2] = "--driverclassname=org.apache.derby.jdbc.ClientDriver";
            view_pool[3] = "--maxpoolsize=64";
            view_pool[4] = "--pooling=true";
            view_pool[5] = "--steadypoolsize=10";

            view_pool[6] = "--isconnectvalidatereq=true";
            view_pool[7] = "--validationmethod=table";
            view_pool[7] = "--validationtable="
                    + get_correct_table_name(getConnection(), "CONFIG",
                    instruct_map.get(DATABASE_NAME_DATA_FLAG), 1500);

            view_pool[8] = "--property";
            view_pool[9] = "databasename=" + instruct_map.get(DATABASE_NAME_DATA_FLAG)
                    + ":user=" + instruct_map.get(unm_gendataview_flag) + ""
                    + ":portnumber=" + instruct_map.get(derby_port_flag) + ""
                    + ":port=" + instruct_map.get(derby_port_flag) + ""
                    + ":password=" + instruct_map.get(unm_gendataview_pass_flag) + ""
                    + ":create=false";
//                    + ":url=\"" + dbURL_dataEntry_url + "\":";
            view_pool[10] = "egen_dataView_pool";
            CommandRunner view_commandRunner = glassfish.getCommandRunner();
            CommandResult view_commandResult = view_commandRunner.run("create-jdbc-connection-pool", view_pool);
            print_connection_error(view_commandResult.getFailureCause(), "5d");
            String[] view_resource_a = new String[2];
            view_resource_a[0] = "--connectionpoolid=egen_dataView_pool";
            view_resource_a[1] = "egen_dataView_resource";
            view_commandResult = view_commandRunner.run("create-jdbc-resource", view_resource_a);
            print_connection_error(view_commandResult.getFailureCause(), "5e");
            view_commandRunner = glassfish.getCommandRunner();
            view_commandResult = view_commandRunner.run("ping-connection-pool", "egen_dataView_pool");
            print_connection_error(view_commandResult.getFailureCause(), "5f");
            System.out.println("Connection to egen_dataView_pool was " + view_commandResult.getExitStatus());
        } catch (GlassFishException ex) {
            ex.printStackTrace();
            System.out.println("Error 5j " + ex.getMessage());
            return false;
        } catch (Exception ex) {
            ex.printStackTrace();
            System.out.println("Error 5i " + ex.getMessage());
            return false;
        }
        return true;
    }
//    /*
//     MethodID=5
//     */
//    private boolean create_dataEntry_connections_derby(GlassFish glassfish) {
//        try {
//            //max-queue-size="256"
//            /*
//             * --pooling
//             * --
//             */
//            //Create egen_dataEntry_resource
//            String[] create_pool_dataAccess_a = new String[9];
//            create_pool_dataAccess_a[0] = "--datasourceclassname=org.apache.derby.jdbc.ClientDataSource";
//            create_pool_dataAccess_a[1] = "--restype=javax.sql.DataSource";
//            create_pool_dataAccess_a[2] = "--driverclassname=org.apache.derby.jdbc.ClientDriver";
//            create_pool_dataAccess_a[3] = "--maxpoolsize=64";
//            create_pool_dataAccess_a[4] = "--pooling=true";
//            create_pool_dataAccess_a[5] = "--steadypoolsize=10";
//            create_pool_dataAccess_a[6] = "--property";
////                create_pool_dataAccess_a[7] = "databasename=" + instruct_map.get(DATABASE_NAME_DATA_FLAG) + ":user=" + instruct_map.get(unm_gendataentry_flag) + ":portnumber="
////                    + instruct_map.get(derby_port_flag) + ":port=" + instruct_map.get(derby_port_flag) + ":password=" + instruct_map.get(unm_gendataentry_pass_flag) + ":create=false:url=\"jdbc:" + instruct_map.get(db_host_flag) + ":"
////                    + instruct_map.get(derby_port_flag) + "/" + instruct_map.get(DATABASE_NAME_DATA_FLAG) + "\":";
////            String dbURL_dataEntry_url = "jdbc:" + instruct_map.get(db_host_flag) + ":" + instruct_map.get(derby_port_flag) + "/" + instruct_map.get(DATABASE_NAME_DATA_FLAG) + ":";
////            String dbURL_dataEntry_url = "jdbc:derby:memory:" + instruct_map.get(db_host_flag) + ":" + instruct_map.get(derby_port_flag) + "/" + instruct_map.get(DATABASE_NAME_DATA_FLAG) + "_MEM:";
//
////        
////            Path source = getDataEntryDb_loc();
////            if (source != null) {
////                dbURL_dataEntry_url = "jdbc:derby:memory:" + instruct_map.get(db_host_flag) + ":" + instruct_map.get(derby_port_flag) + ":" + instruct_map.get(DATABASE_NAME_DATA_FLAG) + ";restoreFrom=" + source.toString() + ";" + instruct_map.get(DERBY_CONNECTION_PARAM) + ";user=" + instruct_map.get(unm_gendataentry_flag) + ";password=" + instruct_map.get(unm_gendataentry_pass_flag);
////            }
////            dbURL_dataEntry_url = "jdbc:derby:" + instruct_map.get(db_host_flag) + ":" + instruct_map.get(derby_port_flag) + "/memory:" + instruct_map.get(DATABASE_NAME_DATA_FLAG) + "_MEM1:";
//
//
//            create_pool_dataAccess_a[7] = "databasename=memory\\:" + instruct_map.get(DATABASE_NAME_DATA_FLAG)
//                    + ":user=" + instruct_map.get(unm_gendataentry_flag)
//                    + ":portnumber=" + instruct_map.get(derby_port_MEM_flag)
//                    + ":port=" + instruct_map.get(derby_port_flag)
//                    + ":password=" + instruct_map.get(unm_gendataentry_pass_flag) + ""
//                    + ":create=false"; /*changed from true*/;
////                    + ":url=\"" +dbURL_dataEntry+ "\":";
//
//            create_pool_dataAccess_a[8] = "egen_dataEntry_pool";
//            CommandRunner commandRunner = glassfish.getCommandRunner();
//            CommandResult commandResult = commandRunner.run("create-jdbc-connection-pool", create_pool_dataAccess_a);
//            print_connection_error(commandResult.getFailureCause(), "5a");
//            String[] create_resource_dataAccess_a = new String[2];
//            create_resource_dataAccess_a[0] = "--connectionpoolid=egen_dataEntry_pool";
//            create_resource_dataAccess_a[1] = "egen_dataEntry_resource";
//            commandRunner = glassfish.getCommandRunner();
//            commandResult = commandRunner.run("create-jdbc-resource", create_resource_dataAccess_a);
//            print_connection_error(commandResult.getFailureCause(), "5b");
//            commandRunner = glassfish.getCommandRunner();
//            commandResult = commandRunner.run("ping-connection-pool", "egen_dataEntry_pool");
//            print_connection_error(commandResult.getFailureCause(), "5c");
//            System.out.println("Connection to egen_dataEntry_pool was " + commandResult.getExitStatus());
//
//
//            //Create egen_dataView_resource
////            create_pool_dataAccess_a[7] = "databasename=" + instruct_map.get(DATABASE_NAME_DATA_FLAG) + ":user=" + instruct_map.get(unm_gendataview_flag) + ":portnumber="
////                    + instruct_map.get(derby_port_flag) + ":port=" + instruct_map.get(derby_port_flag) + ":password=" + instruct_map.get(unm_gendataview_pass_flag) + ":create=false:url=\"jdbc:" + instruct_map.get(db_host_flag) + ":"
////                    + instruct_map.get(derby_port_flag) + "/" + instruct_map.get(DATABASE_NAME_DATA_FLAG) + "\":";
////            dbURL_dataEntry_url = "jdbc:derby:memory:" + instruct_map.get(db_host_flag) + ":" + instruct_map.get(derby_port_flag) + "/" + instruct_map.get(DATABASE_NAME_DATA_FLAG) + "_MEM2:";
//
//            create_pool_dataAccess_a[7] = "databasename=" + instruct_map.get(DATABASE_NAME_DATA_FLAG)
//                    + ":user=" + instruct_map.get(unm_gendataview_flag) + ""
//                    + ":portnumber=" + instruct_map.get(derby_port_flag) + ""
//                    + ":port=" + instruct_map.get(derby_port_flag) + ""
//                    + ":password=" + instruct_map.get(unm_gendataview_pass_flag) + ""
//                    + ":create=false";
////                    + ":url=\"" + dbURL_dataEntry_url + "\":";
//            create_pool_dataAccess_a[8] = "egen_dataView_pool";
//            commandRunner = glassfish.getCommandRunner();
//            commandResult = commandRunner.run("create-jdbc-connection-pool", create_pool_dataAccess_a);
//            print_connection_error(commandResult.getFailureCause(), "5d");
//            create_resource_dataAccess_a[0] = "--connectionpoolid=egen_dataView_pool";
//            create_resource_dataAccess_a[1] = "egen_dataView_resource";
//            commandRunner = glassfish.getCommandRunner();
//            commandResult = commandRunner.run("create-jdbc-resource", create_resource_dataAccess_a);
//            print_connection_error(commandResult.getFailureCause(), "5e");
//            commandRunner = glassfish.getCommandRunner();
//            commandResult = commandRunner.run("ping-connection-pool", "egen_dataView_pool");
//            print_connection_error(commandResult.getFailureCause(), "5f");
//            System.out.println("Connection to egen_dataView_pool was " + commandResult.getExitStatus());
//
//            //Create egen_dataUpdate_resource   list-jdbc-connection-pools
//            create_pool_dataAccess_a[7] = "databasename=" + instruct_map.get(DATABASE_NAME_DATA_FLAG) + ":user=" + instruct_map.get(unm_gendataUpdate_flag) + ":portnumber="
//                    + instruct_map.get(derby_port_flag) + ":port=" + instruct_map.get(derby_port_flag) + ":password=" + instruct_map.get(unm_gendataUpdate_pass_flag) + ":create=true:url=\"jdbc:" + instruct_map.get(db_host_flag) + ":"
//                    + instruct_map.get(derby_port_flag) + "/" + instruct_map.get(derby_port_flag) + "\":";
//            create_pool_dataAccess_a[8] = "egen_dataUpdate_pool";
//            commandRunner = glassfish.getCommandRunner();
//            commandResult = commandRunner.run("create-jdbc-connection-pool", create_pool_dataAccess_a);
//            print_connection_error(commandResult.getFailureCause(), "5g");
//            create_resource_dataAccess_a[0] = "--connectionpoolid=egen_dataUpdate_pool";
//            create_resource_dataAccess_a[1] = "egen_dataUpdate_resource";
//            commandRunner = glassfish.getCommandRunner();
//            commandResult = commandRunner.run("create-jdbc-resource", create_resource_dataAccess_a);
//            print_connection_error(commandResult.getFailureCause(), "5h");
//            commandRunner = glassfish.getCommandRunner();
//            commandResult = commandRunner.run("ping-connection-pool", "egen_dataUpdate_pool");
//            System.out.println("Connection egen_dataUpdate_pool was " + commandResult.getExitStatus());
//
//            /*             
//             //egen_dataUpdate_resource
//             create_pool_usermanage_a[4] = "user=egendataentry:port=3306:password=k2prrr.N:url=\"jdbc:mysql://localhost:3306/egen_dataEntry\":";
//             create_pool_usermanage_a[5] = "egen_dataUpdate_pool";
//             commandRunner = glassfish.getCommandRunner();
//             commandResult = commandRunner.run("create-jdbc-connection-pool", create_pool_usermanage_a);
//             System.out.println("157 " + commandResult.getFailureCause());
//             System.out.println("158 " + commandResult.getOutput());
//
//             create_resource_usermanage_a[0] = "--connectionpoolid=egen_dataUpdate_pool";
//             create_resource_usermanage_a[1] = "egen_dataUpdate_resource";
//             commandResult = commandRunner.run("create-jdbc-resource", create_resource_usermanage_a);
//             System.out.println("150" + commandResult.getFailureCause());
//             System.out.println("151 " + commandResult.getOutput());
//             
//             */
//
//        } catch (GlassFishException ex) {
//            ex.printStackTrace();
//            System.out.println("Error 5j " + ex.getMessage());
//            return false;
//        } catch (Exception ex) {
//            ex.printStackTrace();
//            System.out.println("Error 5i " + ex.getMessage());
//            return false;
//        }
//        return true;
//    }

    /*
     MethodID=5
     */
    private boolean configure_glassfish(GlassFish glassfish) {
        boolean result = true;
        try {
//            String[] create_pool_dataAccess_a = new String[1];
//            create_pool_dataAccess_a[0] = "http-thread-pool";
//            CommandRunner commandRunner = glassfish.getCommandRunner();
//            CommandResult commandResult = commandRunner.run("delete-threadpool", create_pool_dataAccess_a);
//            System.out.println("3823 " + commandResult.getOutput());
//            print_connection_error(commandResult.getFailureCause(), "5a");
//
//            create_pool_dataAccess_a = new String[1];
//            create_pool_dataAccess_a[0] = "thread-pool-1";
//            commandRunner = glassfish.getCommandRunner();
//            commandResult = commandRunner.run("delete-threadpool", create_pool_dataAccess_a);
//            System.out.println("3823 " + commandResult.getOutput());
//            print_connection_error(commandResult.getFailureCause(), "5a");
//commandRunner.run("set",
//      "server.network-config.network-listeners.network-listener.my-http-listener.thread-pool=my-thread-pool")
            String[] create_pool_dataAccess_a = new String[4];
            create_pool_dataAccess_a[0] = "--maxthreadpoolsize=100";
            create_pool_dataAccess_a[1] = "--minthreadpoolsize=20";
            create_pool_dataAccess_a[2] = "--idletimeout=20";
//            create_pool_dataAccess_a[3] = "--workqueues=100";
            create_pool_dataAccess_a[3] = "egenvar-thread-pool";
            CommandRunner commandRunner = glassfish.getCommandRunner();
            CommandResult commandResult = commandRunner.run("create-threadpool", create_pool_dataAccess_a);
            System.out.println("3827 " + commandResult.getOutput());
            print_connection_error(commandResult.getFailureCause(), "5a");

            commandResult = commandRunner.run("set", "server.network-config.network-listeners.network-listener.http-listener.thread-pool=egenvar-thread-pool");
            System.out.println("3847 " + commandResult.getOutput());
            print_connection_error(commandResult.getFailureCause(), "5a");

        } catch (GlassFishException ex) {
            System.out.println("Error 5j " + ex.getMessage());
            return false;
        }


        return result;
    }
    /*
     MethodID=361
     */

    private void print_connection_error(Throwable error, String code) {
        if (error != null) {
            System.out.println("Error " + code + ": " + error.toString());
        }

    }
    /*
     MethodID=6
     */
//
//    private void create_db_connections(GlassFish glassfish) {
//        try {
//            //egen_userManagement_resource
//
//            String[] create_pool_usermanage_a = new String[6];
//            create_pool_usermanage_a[0] = "--datasourceclassname=com.mysql.jdbc.jdbc2.optional.MysqlConnectionPoolDataSource";
//            create_pool_usermanage_a[1] = "--restype=javax.sql.ConnectionPoolDataSource";
//            create_pool_usermanage_a[2] = "--driverclassname=com.mysql.jdbc.Driver";
//            create_pool_usermanage_a[3] = "--property";
//            create_pool_usermanage_a[4] = "user=usermanage:port=3306:password=VhC6OeDS:url=\"jdbc:mysql://localhost:3306/" + instruct_map.get(DATABASE_NAME_USERS_FLAG) + "\":";
//            create_pool_usermanage_a[5] = "egen_userManagement_pool";
//            CommandRunner commandRunner = glassfish.getCommandRunner();
//            CommandResult commandResult = commandRunner.run("create-jdbc-connection-pool", create_pool_usermanage_a);
//            print_connection_error(commandResult.getFailureCause(), "6a");
//            String[] create_resource_usermanage_a = new String[2];
//            create_resource_usermanage_a[0] = "--connectionpoolid=egen_userManagement_pool";
//            create_resource_usermanage_a[1] = "egen_userManagement_resource";
//            commandResult = commandRunner.run("create-jdbc-resource", create_resource_usermanage_a);
//            print_connection_error(commandResult.getFailureCause(), "6b");
//            String[] create_realm_usermanage_a = new String[4];
//            create_realm_usermanage_a[0] = "--classname=com.sun.enterprise.security.auth.realm.jdbc.JDBCRealm";
//            create_realm_usermanage_a[1] = "--property";
//            create_realm_usermanage_a[2] = "digest-algorithm=\"" + instruct_map.get(DB_AUTHENTICATION_ALGORITHM_FLAG) + "\":user-name-column=username:password-column=password:group-name-column=groupname:group-table="
//                    + instruct_map.get(DATABASE_NAME_USERS_FLAG) + ".groups:user-table=" + instruct_map.get(DATABASE_NAME_USERS_FLAG)
//                    + ".useraccounts:datasource-jndi=egen_userManagement_resource:jaas-context=\"jdbcRealm\"";
//            create_realm_usermanage_a[3] = "UsermangeRealm";
//            commandResult = commandRunner.run("create-auth-realm", create_realm_usermanage_a);
//            print_connection_error(commandResult.getFailureCause(), "6c");
//
////            //egen_dataEntry_resource
//            create_pool_usermanage_a[4] = "user=egendataentry:port=3306:password=k2prrr.N:url=\"jdbc:mysql://localhost:3306/" + instruct_map.get(DATABASE_NAME_DATA_FLAG) + "\":";
//            create_pool_usermanage_a[5] = "egen_dataEntry_pool";
//            commandRunner = glassfish.getCommandRunner();
//            commandResult = commandRunner.run("create-jdbc-connection-pool", create_pool_usermanage_a);
//            print_connection_error(commandResult.getFailureCause(), "6d");
//            create_resource_usermanage_a[0] = "--connectionpoolid=egen_dataEntry_pool";
//            create_resource_usermanage_a[1] = "egen_dataEntry_resource";
//            commandResult = commandRunner.run("create-jdbc-resource", create_resource_usermanage_a);
//            print_connection_error(commandResult.getFailureCause(), "6e");
//
//            //egen_dataView_resource
//            create_pool_usermanage_a[4] = "user=egendataview:port=3306:password=k2prrr.N:url=\"jdbc:mysql://localhost:3306/" + instruct_map.get(DATABASE_NAME_DATA_FLAG) + "\":";
//            create_pool_usermanage_a[5] = "egen_dataView_pool";
//            commandRunner = glassfish.getCommandRunner();
//            commandResult = commandRunner.run("create-jdbc-connection-pool", create_pool_usermanage_a);
//            print_connection_error(commandResult.getFailureCause(), "6f");
//
//            create_resource_usermanage_a[0] = "--connectionpoolid=egen_dataView_pool";
//            create_resource_usermanage_a[1] = "egen_dataView_resource";
//            commandResult = commandRunner.run("create-jdbc-resource", create_resource_usermanage_a);
//            print_connection_error(commandResult.getFailureCause(), "6g");
//
//            //egen_dataUpdate_resource
//            create_pool_usermanage_a[4] = "user=egendataentry:port=3306:password=k2prrr.N:url=\"jdbc:mysql://localhost:3306/" + instruct_map.get(DATABASE_NAME_DATA_FLAG) + ":";
//            create_pool_usermanage_a[5] = "egen_dataUpdate_pool";
//            commandRunner = glassfish.getCommandRunner();
//            commandResult = commandRunner.run("create-jdbc-connection-pool", create_pool_usermanage_a);
//            print_connection_error(commandResult.getFailureCause(), "6h");
//
//            create_resource_usermanage_a[0] = "--connectionpoolid=egen_dataUpdate_pool";
//            create_resource_usermanage_a[1] = "egen_dataUpdate_resource";
//            commandResult = commandRunner.run("create-jdbc-resource", create_resource_usermanage_a);
//            print_connection_error(commandResult.getFailureCause(), "6i");
//
//
//            //JAVA mail
//            if (create_mailer_a != null) {
//                commandResult = commandRunner.run("create-javamail-resource", create_mailer_a);
//                print_connection_error(commandResult.getFailureCause(), "6a");
//            } else {
//                System.out.println("Error: mail source setup failed");
//            }
//
//
//        } catch (GlassFishException ex) {
//            ex.printStackTrace();
//        }
//    }

    /*
     MethodID=8
     */
    private void stopDerby(int port) {
        try {
            NetworkServerControl server = new NetworkServerControl(InetAddress.getByName("localhost"), port);
            server.shutdown();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private boolean pingDerby(int port) {
        boolean ping_ok = false;
        try {
            NetworkServerControl server = new NetworkServerControl(InetAddress.getByName("localhost"), port);
            String msg = server.getRuntimeInfo();
            System.out.println("1469 " + msg);
        } catch (Exception ex) {
            System.out.println("1471 " + ex.getMessage());
        }
        return ping_ok;
    }
    /*
     MethodID=10
     */

    private int createTable(Connection c_conn, String sql) throws SQLException {
        if (c_conn != null) {
            Statement stmt = c_conn.createStatement();
            return stmt.executeUpdate(sql);
        }
        return -1;
    }

    /*
     MethodID=11
     */
    private int addData(Connection c_conn, String sql) {
        int result = -1;
        try {
            if (c_conn != null) {
                Statement stmt = c_conn.createStatement();
                result = stmt.executeUpdate(sql);
            }
        } catch (SQLException ex) {
            result = -1;
            System.out.println("Error 11a " + ex.getMessage() + "\n\t" + sql);
        }
        return result;
    }

    private int createTable_users(Connection c_conn, String sql) {
        int result = -1;
        try {
            if (c_conn != null) {
                Statement stmt = conn_users.createStatement();
                result = stmt.executeUpdate(sql);
            }
        } catch (SQLException ex) {
            System.out.println("Error 552 " + ex.getMessage() + "\n\t" + sql);
        }
        return result;
    }

    private void insertData(Connection c_conn, String table, ArrayList<HashMap<String, String>> data_map) {
        if (c_conn != null) {
            try {
                Statement stmt = conn_users.createStatement();
                for (int i = 0; i < data_map.size(); i++) {
                    String insert_sql = "INSERT INTO " + table + "";
                    String columnnms = "";
                    String values = "";
                    HashMap<String, String> c_data_map = data_map.get(i);
                    ArrayList<String> clm_nm_l = new ArrayList<String>(c_data_map.keySet());
                    for (int j = 0; j < clm_nm_l.size(); j++) {
                        String c_nm = clm_nm_l.get(j);
                        String c_val = c_data_map.get(c_nm);
                        if (!columnnms.isEmpty()) {
                            columnnms = columnnms + ",";
                        }
                        if (!values.isEmpty()) {
                            values = values + ",";
                        }
                        if (c_val.matches("[0-9]+")) {
                            values = values + c_val;
                        } else {
                            values = values + "'" + c_val + "'";
                        }
                        columnnms = columnnms + c_nm;
                    }
                    insert_sql = insert_sql + "(" + columnnms + ") values(" + values + ")";
                    int result = stmt.executeUpdate(insert_sql);
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }

    /*
     MethodID=7
     */
    private Connection getConnection_usermanage() {
        try {
            if (conn_users == null || conn_users.isClosed()) {
                Class.forName("org.apache.derby.jdbc.ClientDriver").newInstance();
                conn_users = DriverManager.getConnection(dbURL_users);
            }
        } catch (Exception ex) {
            String error = ex.getMessage();
            if (error.contains("password invalid")) {
                System.out.println("\n\n" + printformater_terminal("Error : Make sure the user name and the password is the same one used during creating this database. If you have lost the password, then use a different location to install the server or "
                        + "delete the database folders and try again. For password recovery please refer the administrators manual"
                        + "The passwords are usually included in the config (e.g. " + instruct_map.get(CONFIG_FILE_FLAG) + " file and a password is generated when one is not provided by the user (this will be sent via email to administrator). ", 80));

            }
            System.out.println("Error 7a: " + error);
        }
        return conn_users;
    }

    private Connection getConnection(boolean ismemory) {
        if (ismemory) {
            return getConnection_memory(false, 14);
        } else {
            return getConnection();
        }
    }
    /*
     MethodID=8
     */

    private Connection getConnection() {
        try {
            if (conn == null || conn.isClosed()) {
                Class.forName("org.apache.derby.jdbc.ClientDriver").newInstance();
                conn = DriverManager.getConnection(dbURL_dataEntry);
            }
        } catch (Exception ex) {
            String error = ex.getMessage();
            if (error.contains("password invalid")) {
                System.out.println("\n\n" + printformater_terminal("Error : Make sure the user name and the password is the same one used during creating this database. If you have lost the password, then use a different location to install the server or "
                        + "delete the database folders and try again.  For password recovery please refer the administrators manual"
                        + "The passwords are usually included in the config (e.g. " + instruct_map.get(CONFIG_FILE_FLAG) + " file and the default passwaord when non selected is 0000 (four zeros)", 80));
            }
            System.out.println("Error 8a: " + error);
        }
        return conn;
    }

    /*
     MethodID=8.1
     */
    private Connection getConnection2() {
//        System.out.println("5225 con2 blocked");   
        try {
            if (conn2 == null || conn2.isClosed()) {
                Class.forName("org.apache.derby.jdbc.ClientDriver").newInstance();
                conn2 = DriverManager.getConnection(dbURL_dataEntry);
            }
        } catch (Exception ex) {
            String error = ex.getMessage();
            if (error.contains("password invalid")) {
                System.out.println("\n\n" + printformater_terminal("Error : Make sure the user name and the password is the same one used during creating this database. If you have lost the password, then use a different location to install the server or "
                        + "delete the database folders and try again.  For password recovery please refer the administrators manual"
                        + "The passwords are usually included in the config (e.g. " + instruct_map.get(CONFIG_FILE_FLAG) + " file and the default passwaord when non selected is 0000 (four zeros)", 80));
            }
            System.out.println("Error 8a: " + error);
        }
        return conn2;
    }
    /*
     MethodID=8.1
     */

    public Connection getConnection_memory(boolean reset, int caller) {
        if (verbose) {
            System.out.println("\n5116 tagsource_refreshing=" + tagsource_refreshing + "\n");
        }
        try {
            int max_wait_loop = 100;
            if (reset && dbURL_dataEntry_memory_drop != null) {
                try {
                    if (conn_memory != null && !conn_memory.isClosed()) {
                        while ((isActive(conn_memory) && max_wait_loop > 0) || tagsource_refreshing) {
                            max_wait_loop--;
                            try {
                                Thread.sleep(5000);
                            } catch (InterruptedException ex) {
                            }
                        }
                    }
                    DriverManager.getConnection(dbURL_dataEntry_memory_drop);
                    Class.forName("org.apache.derby.jdbc.ClientDriver").newInstance();
                } catch (SQLException | ClassNotFoundException | InstantiationException | IllegalAccessException ex) {
                    conn_memory = null;
                    System.out.println("Warning , database droppped from memory " + ex.getMessage());
                }
                conn_memory = null;
            }
            if (conn_memory == null || conn_memory.isClosed() || reset) {
                if (dbURL_dataEntry_memory == null) {
                    Class.forName("org.apache.derby.jdbc.ClientDriver").newInstance();
                    conn_memory = DriverManager.getConnection(dbURL_dataEntry);
                } else {
                    HashMap<String, String> config_map = new HashMap<>();
                    config_map.put(LAST_MEMORIZED, Timing.getDateTime());
                    config_map.put(MEMORIZING_REQUESTED, "0");
                    if (setConfig(getConnection(), config_map, instruct_map.get(DATABASE_NAME_DATA_FLAG)) < 0) {
                    }
                    Class.forName("org.apache.derby.jdbc.ClientDriver").newInstance();
                    conn_memory = DriverManager.getConnection(dbURL_dataEntry_memory);
                }
//                System.out.println("5151 caller=" + caller + "  reset=" + reset);
                System.out.println("Connections refreshed.");
            }
        } catch (Exception ex) {
            String error = ex.getMessage();
            if (error.contains("password invalid")) {
                System.out.println("\n\n" + printformater_terminal("Error : Make sure the user name and the password is the same one used during creating this database. If you have lost the password, then use a different location to install the server or "
                        + "delete the database folders and try again.  For password recovery please refer the administrators manual"
                        + "The passwords are usually included in the config (e.g. " + instruct_map.get(CONFIG_FILE_FLAG) + " file and the default passwaord when non selected is 0000 (four zeros)", 80));
            }
            System.out.println("Error 8.1a: " + error + " for " + caller);
        }
        return conn_memory;
    }

    public boolean resetConnectionIfUpdated() {
        boolean isupdated = false;
        if (isDbUpdated()) {
            isupdated = true;
            getConnection_memory(true, 16);
            makeJsonTbl(getConnection_memory(false, 2), instruct_map.get(DATABASE_NAME_DATA_FLAG));
        }
        return isupdated;
    }

    //select * from EGEN_dataentry.config where name='MEMORIZING_REQUESTED'
    private boolean isDbUpdated() {
        boolean isupdated = false;
        if (getConnection() != null) {
            try {
                Connection ncon = getConnection();
                Statement st_1 = ncon.createStatement();
                String config_tbl = get_correct_table_name(ncon,
                        "config", instruct_map.get(DATABASE_NAME_DATA_FLAG), 1600);
                if (config_tbl != null) {
                    ResultSet r_1 = st_1.executeQuery("select param_value from " + config_tbl + " where name='MEMORIZING_REQUESTED'");
                    if (r_1.next()) {
                        if (r_1.getString("param_value").equals("1")) {
                            isupdated = true;
                        }
                    }
                    r_1.close();
                }
                st_1.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
        return isupdated;
    }

    public String[] getDeleteCode() {
        String[] delete_info = new String[2];
        if (getConnection() != null) {
            try {
                Connection ncon = getConnection();
                Statement st_1 = ncon.createStatement();
                String config_tbl = get_correct_table_name(ncon,
                        "config", instruct_map.get(DATABASE_NAME_DATA_FLAG), 1700);
                if (config_tbl != null) {
                    ResultSet r_1 = st_1.executeQuery("select param_value from " + config_tbl + " where name='" + DELETE_REQUEST_CODE_FLAG + "'");
                    if (r_1.next()) {
                        String c_val = r_1.getString("param_value");
                        if (c_val != null) {
                            if (c_val.indexOf("|") > 1) {
                                delete_info = c_val.split("|");
                            } else {
                                delete_info[0] = c_val;
                            }
                        }
                    }
                    r_1.close();
                }
                st_1.close();
//                ncon.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
        return delete_info;
    }

    /*
     MethodID=8.2
     */
    private boolean isActive(Connection c_con) {
        boolean active = false;
        try {
            String sql = "SELECT STATUS FROM SYSCS_DIAG.TRANSACTION_TABLE";
            ResultSet r = c_con.createStatement().executeQuery(sql);
            active = false;
            while (!active && r.next()) {
                String status = r.getString(1);
                if (status != null) {
                    if (status.toUpperCase().equals("ACTIVE")) {
                        active = true;
                    }
                }
            }
            r.close();
        } catch (SQLException ex) {
            conn_memory = null;
            System.out.println("Warning , database droppped from memory " + ex.getMessage());
        }
        return active;
    }
//    /*
//     MethodID=9
//     */
//
//    private boolean createConnection_mysql() {
//        boolean result = false;
//        try {
//            if (conn_mysql == null || conn_mysql.isClosed()) {
//                Class.forName("com.mysql.jdbc.Driver").newInstance();
//                conn_mysql = DriverManager.getConnection(dbURL_dataEntry_mysql);
//                result = true;
//            } else {
//                result = true;
//            }
//        } catch (Exception ex) {
//            String error = ex.getMessage();
//            if (error.contains("password invalid")) {
//                System.out.println("\n\n" + printformater_terminal("Error : Make sure the user name and the password is the same one used during creating this database. If you have lost the password, then use a different location to install the server or "
//                        + "delete the database folders and try again. There is no password recovery option due to security reasons"
//                        + "The passwords are usually included in thr config (e.g. " + instruct_map.get(CONFIG_FILE_FLAG) + " file and the default passwaord when non selected is 0000 (four zeros)", 80) + "\n\n");
//
//            }
//            System.out.println("Error 9a: " + error);
//        }
//        return result;
//    }

    private ArrayList<String> getCurrentTables(Connection c_con_b, String schema_name, int caller) {
        if (verbose) {
            System.out.println("5249 getCurrentTables caller=" + caller);
        }
        ArrayList<String> result_l = new ArrayList<>(20);
        Connection c_con = getConnection();
        if (c_con != null) {
            try {
//                Statement stmt = c_con.createStatement();
//                //                String q = "select TABLENAME from sys.systables WHERE UPPER(CAST(TABLETYPE AS CHAR(1))) = 'T' and SCHEMAID=(select SCHEMAID from SYS.SYSSCHEMAS where UPPER(CAST(SCHEMANAME AS VARCHAR(128)))=UPPER('" + instruct_map.get(DATABASE_NAME_DATA_FLAG).toUpperCase() + "'))";
////              String q = "select TABLENAME from sys.systables WHERE UPPER(CAST(TABLETYPE AS CHAR(1))) = 'T' ";
//                ResultSet r_1 = stmt.executeQuery(q);
//                while (r_1.next()) {
//                    result_l.add(instruct_map.get(DATABASE_NAME_DATA_FLAG).toUpperCase() + "." + r_1.getString(1));
//                }         

                Statement stmt = c_con.createStatement();
                String q = "select TABLENAME from sys.systables WHERE UPPER(CAST(TABLETYPE AS CHAR(1))) = 'T' "
                        + "and SCHEMAID=(select SCHEMAID from SYS.SYSSCHEMAS where UPPER(CAST(SCHEMANAME AS VARCHAR(128)))=UPPER('" + schema_name.toUpperCase() + "'))";
                ResultSet r_1 = stmt.executeQuery(q);
                if (r_1.isClosed() || stmt.isClosed()) {
                    System.out.println("\n\nxxxx5371Closed During " + caller + "\n\n");
                }
                while (r_1.next()) {
                    if (r_1.isClosed() || stmt.isClosed()) {
                        System.out.println("\n\nxxxx5373Closed During " + caller + "\n\n");
                    }
                    result_l.add(schema_name.toUpperCase() + "." + r_1.getString(1));
                }
                stmt.close();
                r_1.close();
//                c_con.close();
            } catch (SQLException ex) {
                System.out.println("xxxx5339 caller=" + caller + "  " + schema_name);
                ex.printStackTrace();
            }
        }
        return result_l;
    }
//    private ArrayList<String> getCurrentTables_mem() {
//        ArrayList<String> result_l = new ArrayList<>(20);
//        if (createConnection_memory()) {
//            try {
//                Statement stmt = conn_memory.createStatement();
//                String q = "select TABLENAME from sys.systables WHERE UPPER(CAST(TABLETYPE AS CHAR(1))) = 'T' and SCHEMAID=(select SCHEMAID from SYS.SYSSCHEMAS where UPPER(CAST(SCHEMANAME AS VARCHAR(128)))=UPPER('" + instruct_map.get(DATABASE_NAME_DATA_FLAG).toUpperCase() + "'))";
////              String q = "select TABLENAME from sys.systables WHERE UPPER(CAST(TABLETYPE AS CHAR(1))) = 'T' ";
//                ResultSet r_1 = stmt.executeQuery(q);
//                while (r_1.next()) {
//                    result_l.add(instruct_map.get(DATABASE_NAME_DATA_FLAG).toUpperCase() + "." + r_1.getString(1));
//                }
//                r_1.close();
//            } catch (SQLException ex) {
//                ex.printStackTrace();
//            }
//        }
//        return result_l;
//    }

    private ArrayList<String> getCurrentTables_USERS() {
        ArrayList<String> result_l = new ArrayList<>(20);
        if (getConnection_usermanage() != null) {
            try {
                Statement stmt = conn_users.createStatement();
                String q = "select TABLENAME from sys.systables WHERE UPPER(CAST(TABLETYPE AS CHAR(1))) = 'T' and SCHEMAID=(select SCHEMAID from SYS.SYSSCHEMAS where UPPER(CAST(SCHEMANAME AS VARCHAR(128)))=UPPER('" + instruct_map.get(DATABASE_NAME_USERS_FLAG).toUpperCase() + "'))";
                ResultSet r_1 = stmt.executeQuery(q);
                while (r_1.next()) {
                    result_l.add(instruct_map.get(DATABASE_NAME_USERS_FLAG).toUpperCase() + "." + r_1.getString(1));
                }
                r_1.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
        return result_l;
    }

    private ArrayList<String> getCurrentTables_usermange() {
        ArrayList<String> result_l = new ArrayList<>(20);
        if (getConnection_usermanage() != null) {
            try {
                Statement stmt = conn_users.createStatement();
                String q = "select TABLENAME from SYS.SYSTABLES where TABLETYPE='T' and SCHEMAID=(select SCHEMAID from sys.sysschemas where SCHEMANAME='" + instruct_map.get(DATABASE_NAME_USERS_FLAG).toUpperCase() + "')";
                ResultSet r_1 = stmt.executeQuery(q);
                while (r_1.next()) {
                    result_l.add(r_1.getString(1));
                }
                r_1.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
        return result_l;
    }

    private ArrayList<String> executeQuery(Connection c_con, String query) {
        ArrayList<String> result_l = new ArrayList<>(20);
        if (c_con != null) {
            try {
                Statement stmt = c_con.createStatement();
                ResultSet r_1 = stmt.executeQuery(query);
                int clm_count = r_1.getMetaData().getColumnCount();
                while (r_1.next()) {
                    String c_r = "";
                    for (int i = 1; i <= clm_count; i++) {
                        c_r = c_r + "," + r_1.getMetaData().getColumnName(i) + "=" + r_1.getString(i);
                    }
                    result_l.add(c_r);
                }
                r_1.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
        return result_l;
    }

    private int executeUpdate(Connection c_con, String query) {
        int result = -1;
        if (c_con != null) {
            try {
                Statement stmt = c_con.createStatement();
                result = stmt.executeUpdate(query);
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
        return result;
    }

    private boolean execute(Connection c_con, String query) {
        boolean result = false;
        if (c_con != null) {
            try {
                Statement stmt = c_con.createStatement();
                result = stmt.execute(query);
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
        return result;
    }

    private ArrayList<String> executeQuery_user(String query) {
        ArrayList<String> result_l = new ArrayList<String>(20);
        if (getConnection_usermanage() != null) {
            try {
                Statement stmt = getConnection_usermanage().createStatement();
                ResultSet r_1 = stmt.executeQuery(query);
                int clm_count = r_1.getMetaData().getColumnCount();
                while (r_1.next()) {
                    String c_r = "";
                    for (int i = 1; i <= clm_count; i++) {
                        c_r = c_r + "||" + r_1.getMetaData().getColumnName(i) + "=" + r_1.getString(i);
                    }
                    result_l.add(c_r);
                }
                r_1.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
        return result_l;
    }

//    private void executeUpdateQuery(String query) {
//        try {
//            if (getConnection() != null) {
//                Statement stmt = getConnection().createStatement();
//                stmt.executeUpdate(query);
//                stmt.close();
//            }
//        } catch (SQLException ex) {
//            ex.printStackTrace();
//        }
//
//    }
    public int insert_user_account(HashMap<String, String> acouunt_map, int level) {
        int result = -1;
        try {
            if (getConnection_usermanage() != null) {
                String proc = "call " + instruct_map.get(DATABASE_NAME_USERS_FLAG) + ".insert_user(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
                CallableStatement cs = conn_users.prepareCall("{" + proc + "}");
                cs.setString(1, instruct_map.get(DATABASE_NAME_USERS_FLAG) + ".spliter_SplitValues");
                cs.setString(2, instruct_map.get(DATABASE_NAME_USERS_FLAG) + ".useraccounts");
                cs.setString(3, instruct_map.get(DATABASE_NAME_USERS_FLAG) + ".groupnmtoGID");
                cs.setString(4, instruct_map.get(DATABASE_NAME_USERS_FLAG) + ".groups");
                cs.setString(5, acouunt_map.get("email"));
                cs.setString(6, acouunt_map.get("email"));
                cs.setString(7, acouunt_map.get("email"));
                cs.setString(8, acouunt_map.get("Password"));
                cs.setString(9, ",");
                cs.setString(10, "admin, Uploader,Editor,Deletor");
                cs.setInt(11, 1);
                cs.setInt(12, level);
                cs.setString(13, acouunt_map.get(SERVER_IP_FLAG));
                cs.setString(14, "99999");
                cs.setString(15, instruct_map.get(DATABASE_NAME_USERS_FLAG) + ".SHA1");
                cs.registerOutParameter(16, Types.VARCHAR);
                result = cs.executeUpdate();
                cs.close();
            } else {
                System.out.println("Error 3266: Connection failed");
            }

        } catch (SQLException ex) {

            System.out.println("Error 3228: " + ex.getMessage());
        }
        return result;
    }

//    /*
//     MethodID=10
//     */
//    private void load_Biological_Ontologies(String filenm) {
//        ///home/sabryr/Documents/DataIntergration/Config/Biological_Ontologies.txt
//        //TABLENAME=egen_dataEntry.Biological_Ontologies==
//        if (getConnection() != null) {
//            ArrayList<String> ctables_l = getCurrentTables(getConnection());
//            if (!ctables_l.contains("" + instruct_map.get(DATABASE_NAME_DATA_FLAG) + ".BIOLOGICAL_ONTOLOGIES")) {
//                File file = new File(filenm);
//                if (file.isFile() && file.canRead()) {
//                    try {
//
//                        try {
//                            if (!getConnection().isClosed()) {
//                                int result = getConnection().createStatement().executeUpdate("CREATE TABLE " + instruct_map.get(DATABASE_NAME_DATA_FLAG) + ".Biological_Ontologies (id  int NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1)  PRIMARY KEY,name varchar(256),obo_id varchar(256),parent_id int,  namespace varchar(256),is_a varchar(256) ,definition varchar(2048), relationship  varchar(2048))");
//                                String sql = "insert into " + instruct_map.get(DATABASE_NAME_DATA_FLAG) + ".Biological_Ontologies(name,obo_id,parent_id,namespace,is_a,definition,relationship) values(?,?,?,?,?,?,?)";
//                                PreparedStatement p_1 = getConnection().prepareStatement(sql);
//                                Scanner scan = new Scanner(file);
//                                int saftey = 0;
//                                while (scan.hasNext() && result >= 0 && saftey < 10) {
//                                    saftey++;
//                                    String line = scan.nextLine().trim();
//
//                                    if (!line.isEmpty() && !line.startsWith("#")) {
//                                        String[] split = line.split("\\|\\|");
//                                        if (split.length == 8 && split[3].trim().matches("[0-9\\-]+")) {
//                                            p_1.setString(1, split[1].trim());
//                                            p_1.setString(2, split[2].trim());
//                                            p_1.setInt(3, new Integer(split[3].trim()));
//                                            p_1.setString(4, split[4].trim());
//                                            p_1.setString(5, split[5].trim());
//                                            p_1.setString(6, split[6].trim());
//                                            p_1.setString(7, split[7].trim());
//                                            result = p_1.executeUpdate();
//                                        }
//                                    }
//                                }
//                                result = getConnection().createStatement().executeUpdate("CREATE INDEX parent_indx1 ON " + instruct_map.get(DATABASE_NAME_DATA_FLAG) + ".BIOLOGICAL_ONTOLOGIES(parent_id)");
//                                if (result >= 0) {
//                                    System.out.println("1058 indexing success");
//                                } else {
//                                    System.out.println("1060 indexing failed");
//                                }
//                            }
//                        } catch (SQLException ex) {
//                            ex.printStackTrace();
//                        }
//
//                    } catch (FileNotFoundException ex) {
//                        ex.printStackTrace();
//                    }
//                } else {
//                    System.out.println("Error 10a: can not read " + filenm);
//                }
//            }
//        }
//    }

    /*
     MethodID=27
     */
    private String getRealKey(Set keys, String key) {
        ArrayList<String> key_l = new ArrayList<String>(keys);
        key = key.trim().toUpperCase();
        String realKey = null;
        for (int i = 0; (i < key_l.size() && realKey == null); i++) {
            String c_key = key_l.get(i).toUpperCase();
            if (c_key.equalsIgnoreCase(key)) {
                realKey = key_l.get(i);
            }
        }
        return realKey;
    }

//    /*
//     MethodID=
//     */
//    private String runCommnad(File directory, String... command_l) {
//        String result = "";
//        try {
//            ProcessBuilder pbuidl = new ProcessBuilder(command_l);
//            if (directory != null) {
//                pbuidl = pbuidl.directory(directory);
//            }
//            
//            Process output = pbuidl.start();
//            Scanner scan = new Scanner(output.getInputStream());
//            while (scan.hasNext()) {
//                result = scan.nextLine();
//            }
//            try {
//                output.waitFor();
//            } catch (InterruptedException ex) {
//            }
//            scan.close();
//            if (output.exitValue() == 0) {
//                output.destroy();
//                return result.replaceAll("\"", "");
//            } else {
//                output.destroy();
//                return null;
//            }
//            
//        } catch (IOException ex) {
//            System.out.println("Error 15a :" + ex.getMessage());
//            return null;
//        }
//    }
//    private void test() {
//        if (createConnection_usermanage()) {
//            ArrayList<String> table_l = executeQuery_user("select * from " + instruct_map.get(DATABASE_NAME_USERS_FLAG) + ".useraccounts");
//            for (int i = 0; i < table_l.size(); i++) {
//                System.out.println("102 " + table_l.get(i));
//            }
//        }
    //                                result_l = executeQuery(" SELECT * FROM EGEN_DATAENTRY.biological_ontologies WHERE parent_id=0 and id>0");
//                        System.out.println("89 " + result_l.size());
//                        for (int i = 0; i < result_l.size(); i++) {
//                            System.out.println("90 " + result_l.get(i));
//                        }
    //                                    ArrayList<String> configresult_l = executeQuery("Select name,PARAM_VALUE from " + geget_correct_table_name("config"));
//                                    for (int i = 0; i < configresult_l.size(); i++) {
//                                        System.out.println(i + ") " + configresult_l.get(i));
//
//                      HashMap<String, String[]> result_map = Tests.get_key_contraints(instruct_map.get(DATABASE_NAME_DATA_FLAG), geget_correct_table_name("biological_ontologies"), dbURL_dataEntry);
//                                System.out.println("\n\n505 " + result_map + "\n\n");
//                                ArrayList<String> key_l = new ArrayList<String>(result_map.keySet());
//                                for (int i = 0; i < key_l.size(); i++) {
//                                    System.out.println("507 " + key_l.get(i) + "--------------");
//                                    String[] _a = result_map.get(key_l.get(i));
//                                    for (int j = 0; j < _a.length; j++) {
//                                        System.out.println("\t" + _a[j]);
//
//                                    }
//                                }
//                                result_map = Tests.get_key_contraints(instruct_map.get(DATABASE_NAME_DATA_FLAG), geget_correct_table_name("person"), dbURL_dataEntry);
//                                System.out.println("\n\n505 "+result_map +"\n\n");
//                                key_l = new ArrayList<String>(result_map.keySet());
//                                for (int i = 0; i < key_l.size(); i++) {
//                                    System.out.println("507 " + key_l.get(i) + "--------------");
//                                    String[] _a = result_map.get(key_l.get(i));
//                                    for (int j = 0; j < _a.length; j++) {
//                                        System.out.println("\t" + _a[j]);
//
//                                    }
//                                }
//    }
    private HashMap<String, String> getNetInfo(boolean forcelocalhost) {
        HashMap<String, String> result_map = new HashMap<>();
        if (instruct_map == null) {
            instruct_map = new HashMap<>();
            instruct_map.put(SERVER_SECURE_PORT_FLAG, "8185");
            instruct_map.put(SERVER_PORT_FLAG, "8085");
            instruct_map.put(derby_port_flag, "1557");
        }

        result_map.put(SERVER_IP_FLAG, "0.0.0.0");
        result_map.put(SERVER_ROOT_FLAG, "http://localhost:" + instruct_map.get(SERVER_SECURE_PORT_FLAG) + "/");
        result_map.put(SERVER_NAME_FLAG, "http://localhost:" + instruct_map.get(SERVER_SECURE_PORT_FLAG) + "/eGenVar_web/");
        result_map.put(S_SERVER_ROOT_FLAG, "https://localhost:" + instruct_map.get(SERVER_SECURE_PORT_FLAG) + "/");
        result_map.put(S_SERVER_NAME_FLAG, "https://localhost:" + instruct_map.get(SERVER_SECURE_PORT_FLAG) + "/eGenVar_web/");
        result_map.put(WSDL_URL_FLAG, "http://localhost:" + instruct_map.get(SERVER_SECURE_PORT_FLAG) + "/eGenVar_WebService/Authenticate_service?wsdl");
        result_map.put(S_WSDL_URL_FLAG, "https://localhost:" + instruct_map.get(SERVER_SECURE_PORT_FLAG) + "/eGenVar_WebService/Authenticate_service?wsdl");
        result_map.put(SERVER_BASE_FLAG, "localhost");

//        result_map.put(COMMAND_LINE_TOOL_VERSION_URL_FLAG, "http://localhost:8085/egenv_version.txt");
        if (!forcelocalhost) {
            try {
                Enumeration net_interfaces = NetworkInterface.getNetworkInterfaces();
                while (net_interfaces.hasMoreElements()) {
                    NetworkInterface n = (NetworkInterface) net_interfaces.nextElement();
                    if (n.isVirtual() || n.isLoopback()) {
                    } else {
                        Enumeration ee = n.getInetAddresses();
                        while (ee.hasMoreElements()) {
                            InetAddress inetadd = (InetAddress) ee.nextElement();
                            if (inetadd instanceof Inet4Address) {
                                String hostaddress = inetadd.getHostAddress();
                                String hostname = inetadd.getCanonicalHostName();
                                result_map.put(SERVER_IP_FLAG, hostaddress);
                                result_map.put(SERVER_BASE_FLAG, hostname);
                                result_map.put(SERVER_ROOT_FLAG, "http://" + hostname + ":" + instruct_map.get(SERVER_PORT_FLAG) + "/");
                                result_map.put(SERVER_NAME_FLAG, result_map.get(SERVER_ROOT_FLAG) + "eGenVar_web/");
                                result_map.put(S_SERVER_ROOT_FLAG, "https://" + hostname + ":" + instruct_map.get(SERVER_SECURE_PORT_FLAG) + "/");
                                result_map.put(S_SERVER_NAME_FLAG, result_map.get(S_SERVER_ROOT_FLAG) + "eGenVar_web/");
                                result_map.put(WSDL_URL_FLAG, "http://" + hostname + ":" + instruct_map.get(SERVER_PORT_FLAG) + "/eGenVar_WebService/" + "Authenticate_service?wsdl");
                                result_map.put(S_WSDL_URL_FLAG, "https://" + hostname + ":" + instruct_map.get(SERVER_SECURE_PORT_FLAG) + "/eGenVar_WebService/" + "Authenticate_service?wsdl");

//                            result_map.put(COMMAND_LINE_TOOL_VERSION_URL_FLAG, "http://" + inetadd.getCanonicalHostName() + ":" + instruct_map.get(SERVER_PORT_FLAG) + "/egenv_version.txt");
                            }
                        }
                    }
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return result_map;
    }

//    private String getUserChoice(List tables_l, String msg) {
//        String result = null;
//        if (tables_l != null) {
//            String[] tables_a = new String[tables_l.size()];
//            for (int i = 0; i < tables_l.size(); i++) {
//                tables_a[i] = tables_l.get(i).toString();
//            }
//            Arrays.sort(tables_a);
//            String choices = "";
//            for (int i = 0; i < tables_a.length; i++) {
//                choices = choices + i + ") " + tables_a[i] + "\n";
//            }
//            boolean ok = false;
//            while (!ok) {
//                String ans = getuserInputSameLine(msg, choices);
//                if (ans != null && ans.trim().matches("[0-9]+")) {
//                    int coince = new Integer(ans);
//                    if (coince >= 0 && tables_a.length > coince) {
//                        ok = true;
//                        result = tables_a[coince];
//                    }
//                }
//                if (!ok) {
//                    System.out.println("Invalid choice, try again");
//                }
//            }
//        }
//        return result;
//    }
    public static String getuserInputSameLine(String message, String options) {
        Console cons = System.console();
        if (cons != null) {
            System.out.println("\n_______________________________________");
            if (options == null) {
                System.out.print(message + ": ");
            } else {
                System.out.print(message + "\n" + options + ": ");
            }
            return cons.readLine();
        } else {
            System.out.println("Failed to invloke console. ");
            System.exit(1);
        }
        return null;
    }

    private int analyseUserResponse(String response, boolean exitifNO, boolean freetext_allowed, String pattern) {
        boolean ok = false;
        int safety = 7;
        int result = -1;
        while (!ok) {
            safety--;
            ok = true;
            if (safety > 1) {
                if (response == null || response.isEmpty()) {
                    ok = false;
                    response = getuserInputSameLine("Invalid choice, please try again", "");
                } else {
                    if (pattern != null) {
                        if (response.matches(pattern)) {
                            result = 1;
                        } else {
                            ok = false;
                            response = getuserInputSameLine("Invalid input, please try again", "");
                        }
                    } else if (response.equalsIgnoreCase("N")) {
                        if (exitifNO) {
                            System.out.println("Terminating all operations and exiting..");
                            System.exit(1);
                        } else {
                            result = -1;
                        }
                    } else if (response.equalsIgnoreCase("Y")) {
                        result = 0;
                    } else if (response.equalsIgnoreCase("all")) {
                        result = 2;
                    } else if (response.equalsIgnoreCase("no-all")) {
                        result = 3;
                    } else if (response.equalsIgnoreCase("H")) {
                        result = 4;
                    } else if (response.equalsIgnoreCase("C")) {
                        result = 5;
                    } else if (response.equalsIgnoreCase("A")) {
                        result = 6;
                    } else if (freetext_allowed) {
                        result = 1;
                    } else {
                        ok = false;
                        response = getuserInputSameLine("Invalid choice, please try again", "");
                    }
                }
            } else {
                System.out.println("Maximum attempts exceeded. Terminating all operations and exiting.");
                System.exit(10);
            }

        }

        return result;
    }

    /*
     MethodID=20
     */
    private void createQueryExpansion(Connection c_con, String schema_name) {
        System.out.println("Re-generating query expansions..");
        ArrayList<String> special_tbl_list = new ArrayList<>(10);

        ArrayList<String> insert_sql = new ArrayList<>(10);
        ArrayList<String> table_l = getCurrentTables(c_con, schema_name, 7);
        HashMap<String, String> table2colmn_map = new HashMap<>();
        HashMap<String, String> table2name_colmn_map = new HashMap<>();
        for (int i = 0; i < table_l.size(); i++) {
            ArrayList<String> col_l = getColumns(c_con, schema_name, table_l.get(i));
            if (!col_l.isEmpty()) {
                int name_col_pos = -1;
                String col_nms = table_l.get(i) + "." + col_l.get(0);
                if (col_l.get(0).toUpperCase().equals("NAME")) {
                    name_col_pos = 0;
                } else if (!col_l.get(0).toUpperCase().equals("ID")) {
                    name_col_pos = 0;
                }
                for (int j = 1; j < col_l.size(); j++) {
                    if (col_l.get(j).toUpperCase().equals("NAME")) {
                        name_col_pos = j;
                    } else if (name_col_pos < 0 && !col_l.get(j).toUpperCase().equals("ID")) {
                        name_col_pos = j;
                    }
                    col_nms = col_nms + "," + table_l.get(i) + "." + col_l.get(j);
                }
                table2colmn_map.put(table_l.get(i), col_nms);
                if (name_col_pos > 0) {
                    table2name_colmn_map.put(table_l.get(i), col_l.get(name_col_pos));
                }
            } else {
                table2colmn_map.put(table_l.get(i), "id");

            }
        }
        insert_sql.add("delete from " + get_correct_table_name(c_con,
                "queryexpander", schema_name, 1800));
        for (int i = 0; i < table_l.size(); i++) {
            System.out.print(".");
            String c_tbl = table_l.get(i);
            c_tbl = get_correct_table_name(c_con, c_tbl, schema_name, 1900);
            if (c_tbl.toUpperCase().endsWith(".FILES")) {
                special_tbl_list.add(c_tbl);
            } else if (c_tbl.toUpperCase().endsWith(".REPORT")) {
                special_tbl_list.add(c_tbl);
            } else if (c_tbl.toUpperCase().endsWith(".REPORT_BATCH")) {
                special_tbl_list.add(c_tbl);
            } else if (c_tbl.toUpperCase().endsWith(".SAMPLEDETAILS")) {
                special_tbl_list.add(c_tbl);
            } else if (c_tbl.toUpperCase().endsWith(".DONORDETAILS")) {
                special_tbl_list.add(c_tbl);
            }
//            ArrayList<String> columns_l = getColumns(c_tbl);
            HashMap<String, String[]> constraints_map = get_key_contraints(c_con, schema_name, c_tbl);
            if (constraints_map.containsKey(FOREIGN_KEY_COLUMNS_FLAG)) {
                String[] forign_nm_a = constraints_map.get(FOREIGN_KEY_COLUMNS_FLAG);
                for (int j = 0; j < forign_nm_a.length; j++) {
                    if (constraints_map.containsKey(forign_nm_a[j])) {
                        String[] foreing_k_a = constraints_map.get(forign_nm_a[j]);
                        String col_nm = foreing_k_a[0];
                        String target_tbl = get_correct_table_name(c_con, foreing_k_a[1], schema_name, 2000);
                        String target_tbl_col = foreing_k_a[2];
                        if (!target_tbl.equalsIgnoreCase(c_tbl)) {
//                            String tag_sql = "select * from " + target_tbl + " where " + target_tbl_col + "=(select " + col_nm + " from " + c_tbl + " where id=?)";
//                            String tag_sql = "select " + table2name_colmn_map.get(target_tbl) + "," + target_tbl_col + ", id as ref_id from " + target_tbl + " where " + target_tbl_col + " in (select " + col_nm + " from " + c_tbl + " where id in (" + IN_ID_FALG + "))";
                            String forgn_key_sql = "select " + table2colmn_map.get(target_tbl) + "," + c_tbl + ".id as ref_id from " + c_tbl + "," + target_tbl + " where " + c_tbl + ".ID  in (" + IN_ID_FALG + ") and " + c_tbl + "." + col_nm + "=" + target_tbl + "." + target_tbl_col;
                            String statrev_url = TABLE_TO_USE_FLAG + target_tbl + "=" + target_tbl_col + "(" + REPLACEWITH_ID_FALG + ")";
                            String inset_sql1 = "insert into " + get_correct_table_name(c_con,
                                    "queryexpander", schema_name, 2000)
                                    + "(table_name,column_nm,search_sql,static_url) values('" + c_tbl
                                    + "','" + col_nm + "','" + forgn_key_sql
                                    + "','" + statrev_url + "')";
                            insert_sql.add(inset_sql1);
                        }
                    }
                }
            }

            String tag_table = get_correct_table_name(c_con, c_tbl + "2tags", schema_name, 2100);
            if (tag_table != null) {
                HashMap<String, String[]> tag_constraints_maop = get_key_contraints(c_con, schema_name, tag_table);
                if (tag_constraints_maop.containsKey(FOREIGN_KEY_COLUMNS_FLAG)) {
                    String[] forign_nm_a = tag_constraints_maop.get(FOREIGN_KEY_COLUMNS_FLAG);
                    for (int j = 0; j < forign_nm_a.length; j++) {
                        if (tag_constraints_maop.containsKey(forign_nm_a[j])) {
                            String[] foreing_k_a = tag_constraints_maop.get(forign_nm_a[j]);
                            String col_nm = foreing_k_a[0];
                            String target_tbl = get_correct_table_name(c_con, foreing_k_a[1],
                                    schema_name, 2200);
                            String target_tbl_col = foreing_k_a[2];
                            String tag_sql = "select id, " + table2name_colmn_map.get(tag_table) + "," + col_nm + " as ref_id from "
                                    + "" + tag_table + " where " + col_nm + " in (select " + target_tbl_col + " from " + target_tbl + " where  id in (" + IN_ID_FALG + "))";
//                            String tag_sql = "select * from " + tag_table + " where " + col_nm + "=(select " + target_tbl_col + " from " + target_tbl + " where  id=?)";
                            String statrev_sql = TABLE_TO_USE_FLAG + target_tbl + "=" + target_tbl_col + "(" + REPLACEWITH_ID_FALG + ")";
                            String inset_sql1 = "insert into "
                                    + get_correct_table_name(c_con, "queryexpander", schema_name, 2300)
                                    + "(table_name,column_nm,search_sql,static_url) values('"
                                    + c_tbl + "','" + col_nm + "','" + tag_sql + "','" + statrev_sql + "')";
                            insert_sql.add(inset_sql1);

                        }
                    }
                }
            }

            String hierarchy_table = get_correct_table_name(c_con, c_tbl
                    + "_hierarchy", schema_name, 2400);
            if (hierarchy_table != null) {
                HashMap<String, String[]> hierar_constraints_maop =
                        get_key_contraints(c_con, schema_name, hierarchy_table);
                ArrayList<String> tmp = new ArrayList<>(hierar_constraints_maop.keySet());
                String[] colmn_a = hierar_constraints_maop.get("FOREIGN_COLUMNS");
                String parent_col = null;
                String child_col = null;
                if (colmn_a != null) {
                    for (int j = 0; j < colmn_a.length; j++) {
                        if (colmn_a[j].toUpperCase().startsWith(("CHILD"))) {
                            child_col = colmn_a[j];
                        } else if (colmn_a[j].toUpperCase().startsWith("PARENT")) {
                            parent_col = colmn_a[j];
                        }
                    }
                }
                if (parent_col != null && child_col != null) {
//                    String tag_sql = "select * from " + c_tbl + " where id in (select " + parent_col + " from " + hierarchy_table + " where " + child_col + "=?)";
                    String tag_sql = "select id, id as ref_id, " + table2name_colmn_map.get(c_tbl) + " from " + c_tbl + " where id in (select " + parent_col + " from " + hierarchy_table + " where " + child_col + " in (" + IN_ID_FALG + "))";
                    String statrev_sql = TABLE_TO_USE_FLAG + c_tbl + "=id" + "(" + REPLACEWITH_ID_FALG + ")";
                    String inset_sql1 = "insert into "
                            + get_correct_table_name(c_con, "queryexpander", schema_name, 2500)
                            + "(table_name,column_nm,search_sql,static_url) values('"
                            + c_tbl + "','id','" + tag_sql + "','" + statrev_sql + "')";
                    insert_sql.add(inset_sql1);
                }
            }


            if ((c_tbl.split("_")[c_tbl.split("_").length - 1]).equalsIgnoreCase("tagsource")) {
                for (int j = 0; j < table_l.size(); j++) {
                    String c_string_tagged_tbl = get_correct_table_name(c_con, table_l.get(j)
                            + "2tags", schema_name, 2600);
                    if (c_string_tagged_tbl != null) {
                        HashMap<String, String[]> tag_constraints_maop = get_key_contraints(c_con, schema_name, c_string_tagged_tbl);
                        if (tag_constraints_maop.containsKey(FOREIGN_KEY_COLUMNS_FLAG)) {
                            String[] forign_nm_a = tag_constraints_maop.get(FOREIGN_KEY_COLUMNS_FLAG);
                            for (int k = 0; k < forign_nm_a.length; k++) {
                                String[] foreing_k_a = tag_constraints_maop.get(forign_nm_a[k]);
                                String col_nm = foreing_k_a[0];
//                                String target_tbl = get_correct_table_name(c_con, foreing_k_a[1], schema_name);
                                String target_tbl_col = foreing_k_a[2];
//                                  String get_file_sql = "select id,LINK_TO_FEATURE from " + table_l.get(j) + " where " + target_tbl_col + " in (select " + col_nm + " from " + c_string_tagged_tbl + " where LINK_TO_FEATURE IN (" + LINK_TO_FEATURE_ID_FLAG + "))|" + c_tbl.split("\\.")[c_tbl.split("\\.").length - 1]+"";
//                                String get_file_sql = "select  " + table_l.get(j) + ".ID,LINK_TO_FEATURE from " + table_l.get(j) + "," + c_string_tagged_tbl + " where " + c_string_tagged_tbl + "." + col_nm + "=" + table_l.get(j) + "." + target_tbl_col + " AND " + c_string_tagged_tbl + ".LINK_TO_FEATURE=" + LINK_TO_FEATURE_ID_FLAG + " |" + c_tbl.split("\\.")[c_tbl.split("\\.").length - 1] + "|HASH=";
                                String get_file_sql = "select  " + table_l.get(j) + ".ID from " + table_l.get(j) + "," + c_string_tagged_tbl + " where " + c_string_tagged_tbl + "." + col_nm + "=" + table_l.get(j) + "." + target_tbl_col + " AND " + c_string_tagged_tbl + ".LINK_TO_FEATURE=" + LINK_TO_FEATURE_ID_FLAG + " |" + c_tbl.split("\\.")[c_tbl.split("\\.").length - 1] + "|HASH=";

//                                String get_file_sql = "select id from " + table_l.get(j) + " where " + target_tbl_col + " in (select " + col_nm + " from " + c_string_tagged_tbl + " where LINK_TO_FEATURE=''" + c_tbl.split("\\.")[c_tbl.split("\\.").length - 1] + "=" + LINK_TO_FEATURE_ID_FLAG + "'')";
                                String inset_sql1 = "insert into " + get_correct_table_name(c_con,
                                        "queryexpander", schema_name, 2700)
                                        + "(table_name,column_nm,search_sql) values('"
                                        + c_tbl + "','LINK_TO_FEATURE','" + get_file_sql + "')";
                                insert_sql.add(inset_sql1);
//                                   String get_file_4children_sql = "select id from " + table_l.get(j) + " where " + target_tbl_col + " in (select " + col_nm + " from " + c_string_tagged_tbl + " where LINK_TO_FEATURE=''" + c_tbl.split("\\.")[c_tbl.split("\\.").length - 1] + "=" + LINK_TO_FEATURE_ID_FLAG + "'')";

                            }
                        }
                    }
                }
            } else {
                ArrayList< String[]> reverse_constraints_l = get_reverse_key_contraints(c_con, schema_name, c_tbl, null);
                if (reverse_constraints_l != null && !reverse_constraints_l.isEmpty()) {
                    for (int j = 0; j < reverse_constraints_l.size(); j++) {
                        String[] c_rv_a = reverse_constraints_l.get(j);
                        if (c_rv_a != null && c_rv_a.length >= 4) {
                            String tagtet_tbl = get_correct_table_name(c_con, c_rv_a[3], schema_name, 2800);
                            String tagtet_col = c_rv_a[0];
                            String source_colm = c_rv_a[2];
                            if (tagtet_tbl != null && !tagtet_tbl.toUpperCase().endsWith("2TAGS")) {
                                String rev_sql = "Select id, " + tagtet_col + " as ref_id, " + table2name_colmn_map.get(tagtet_tbl) + " from " + tagtet_tbl + " where " + tagtet_col + " in (" + IN_ID_FALG + ")";
                                String statrev_sql = TABLE_TO_USE_FLAG + tagtet_tbl + "=" + tagtet_col + "(" + REPLACEWITH_ID_FALG + ")";
                                String inset_sql1 = "insert into "
                                        + get_correct_table_name(c_con, "queryexpander", schema_name, 2900)
                                        + "(table_name,column_nm,search_sql,static_url) values('" + c_tbl + "','" + source_colm + "','" + rev_sql + "','" + statrev_sql + "')";
                                insert_sql.add(inset_sql1);
                            }
                        }
                    }
                }
            }
        }

        for (int i = 0; i < table_l.size(); i++) {
            System.out.print(".");
            long mem = Runtime.getRuntime().freeMemory();
            if (mem < MIN_MEM) {
                System.out.println("Error 20a:Available memory too low (requires atleast " + (MIN_MEM / (1048576)) + ")");
            }
            String c_tbl = table_l.get(i);
            for (int j = 0; j < special_tbl_list.size(); j++) {
                String cs_tbl = special_tbl_list.get(j);
                if (!c_tbl.equals(cs_tbl)) {
                    ArrayList<ArrayList<String>> path_ll = getpaths(c_con, schema_name, c_tbl, cs_tbl, table_l);
                    if (!path_ll.isEmpty() && !path_ll.get(0).isEmpty()) {
                        String path_q = cs_tbl + "|" + construct_Path_query(c_con, schema_name, path_ll.get(0), table2colmn_map.get(cs_tbl), " " + c_tbl + ".ID=" + REPLACEWITH_ID_FALG + "");
                        String inset_sql1 = "insert into "
                                + get_correct_table_name(c_con, "queryexpander", schema_name, 3000)
                                + "(table_name,column_nm,search_sql) values('" + c_tbl + "','ID','" + path_q + "')";
                        insert_sql.add(inset_sql1);
                    }
                }
            }
        }
        System.out.println("");
        if (addToDb(getConnection(), insert_sql) >= 0) {
            System.out.println("\nAll query expansions were successfully registered");
        } else {
            System.out.println("\nError 20b: Query expansions registering failed\n");
        }
    }

    private ArrayList<ArrayList<String>> getpaths(Connection c_conn, String schema_name, String source, String target, ArrayList<String> table_l) {
        ArrayList<ArrayList<String>> result = new ArrayList<>(1);
        HashMap<String, HashMap<String, String[]>> relationship_map = new HashMap<>();
        HashMap<String, ArrayList<String>> connection_map = new HashMap<>();

        for (int i = 0; i < table_l.size(); i++) {
            String c_tbl = table_l.get(i);
            HashMap<String, String[]> constraint_map = get_key_contraints(c_conn, schema_name, c_tbl);
            if (constraint_map != null && !constraint_map.isEmpty()) {
                if (relationship_map.containsKey(c_tbl)) {
                    relationship_map.get(c_tbl).putAll(constraint_map);
                } else {
                    relationship_map.put(c_tbl, constraint_map);
                }
            }
        }
        for (int i = 0; i < table_l.size(); i++) {
            HashMap<String, String[]> relat_map = relationship_map.get(table_l.get(i));
            String c_taable = table_l.get(i);
            if (relat_map != null && relat_map.containsKey("FOREIGN_COLUMNS")) {
                String[] link_clmn_a = relat_map.get("FOREIGN_COLUMNS");
                if (link_clmn_a != null) {
                    for (int j = 0; j < link_clmn_a.length; j++) {
                        String[] ref_a = relat_map.get(link_clmn_a[j]);
                        if (ref_a != null && ref_a.length >= 3) {
                            String foreing_tbl = ref_a[1];
                            if (!c_taable.equals(foreing_tbl)) {
                                if (connection_map.containsKey(c_taable)) {
                                    connection_map.get(c_taable).add(foreing_tbl);
                                } else {
                                    ArrayList<String> tmp = new ArrayList<>(1);
                                    tmp.add(foreing_tbl);
                                    connection_map.put(c_taable, tmp);
                                }
                                if (!connection_map.containsKey(foreing_tbl)) {
                                    ArrayList<String> tmp = new ArrayList<>(1);
                                    connection_map.put(foreing_tbl, tmp);
                                }
                            }
                        }
                    }
                }
            }
        }

        PathFinder10 path = new PathFinder10();
        source = getRealKey(connection_map.keySet(), source);
        target = getRealKey(connection_map.keySet(), target);
        if (source != null && target != null) {
            result = path.getPaths(connection_map, source, target, 999);
        }
        return result;
    }

    private String construct_Path_query(Connection c_conn, String schema_name, ArrayList<String> path, String target_columns,
            String query) {
//   String construct_advanced_query(ArrayList<String> path, String query, String target_column, String source_column, String source_tble) {
        String sql = "";
        int error = -1;
        if (path != null && !path.isEmpty()) {
            HashMap<Integer, String[]> constructer_map = new HashMap<>();
            for (int i = (path.size() - 1); (i > 0 && error < 0); i--) {
                String c_tbl = path.get(i);
                String c_table_clm = null;
                String c_previous_tbl = path.get(i - 1);
                String c_previous_tbl_clm = null;
                HashMap<String, String[]> constraint_map = get_key_contraints(c_conn, schema_name, c_tbl);
                if (constraint_map == null || constraint_map.isEmpty() || !constraint_map.containsKey(FOREIGN_TABLE_FLAG)
                        || !arrayMatch(c_previous_tbl, constraint_map.get(FOREIGN_TABLE_FLAG))) {
                    constraint_map = get_key_contraints(c_conn, schema_name, c_previous_tbl);
                    String[] forgn_colmn = constraint_map.get(FOREIGN_KEY_COLUMNS_FLAG);
                    if (forgn_colmn != null) {
                        boolean found = false;
                        for (int j = 0; (j < forgn_colmn.length && !found); j++) {
                            String c_clm = forgn_colmn[j];
                            if (constraint_map.containsKey(c_clm) && constraint_map.get(c_clm)[1].equals(c_tbl)) {
                                c_table_clm = constraint_map.get(c_clm)[2];
                                c_previous_tbl_clm = c_clm;
                                found = true;
                            }
                        }
                    } else {
                        error = 1;
                    }
                } else {
                    String[] forgn_colmn = constraint_map.get(FOREIGN_KEY_COLUMNS_FLAG);
                    boolean found = false;
                    for (int j = 0; (j < forgn_colmn.length && !found); j++) {
                        String c_clm = forgn_colmn[j];

                        if (constraint_map.containsKey(c_clm) && constraint_map.get(c_clm)[1].equals(c_previous_tbl)) {
                            c_table_clm = c_clm;
                            c_previous_tbl_clm = constraint_map.get(c_clm)[2];
                            found = true;
                        }
                    }
                }
                if (c_table_clm == null || c_previous_tbl_clm == null) {
//                    error = 2;
                } else {
                    String[] new_construct = new String[4];
                    new_construct[0] = get_correct_table_name(c_conn, c_tbl, schema_name, 4000);
                    new_construct[1] = c_table_clm;
                    new_construct[2] = get_correct_table_name(c_conn, c_previous_tbl, schema_name, 4100);
                    new_construct[3] = c_previous_tbl_clm;
                    constructer_map.put(i, new_construct);
                }
            }
            ArrayList<Integer> constructer_key_l = new ArrayList<>(constructer_map.keySet());
            ArrayList<String> use_tbl_l = new ArrayList<>();
            String join = null;
            for (int i = 0; i < constructer_key_l.size(); i++) {
                Integer c_k = constructer_key_l.get(i);
                String[] c_a = constructer_map.get(c_k);
                String c_join = c_a[0] + "." + c_a[1] + "=" + c_a[2] + "." + c_a[3];
                use_tbl_l.remove(c_a[0]);
                use_tbl_l.add(c_a[0]);
                use_tbl_l.remove(c_a[2]);
                use_tbl_l.add(c_a[2]);
                if (join == null) {
                    join = c_join;
                } else {
                    join = join + " AND " + c_join;
                }

            }
//            sql = "SELECT " + getColumns(taget_tbl, null, true).get(taget_tbl).toString().replace("[", "").replace("]", "").replaceAll("\\s", "") + " FROM " + use_tbl_l.toString().replace("[", "").replace("]", "").replaceAll("\\s", "") + " WHERE " + join + " AND " + query;
            if (query.equals("ALL")) {
                sql = "SELECT " + target_columns + " FROM " + use_tbl_l.toString().replace("[", "").replace("]", "").replaceAll("\\s", "") + " WHERE " + join;
            } else {
                sql = "SELECT " + target_columns + " FROM " + use_tbl_l.toString().replace("[", "").replace("]", "").replaceAll("\\s", "") + " WHERE " + query + " AND " + join;
            }
        } else {
            sql = "SELECT message FROM " + get_correct_table_name(c_conn, "errormsgs", schema_name, 4200) + " WHERE ID=6";
        }
        return sql;
    }
//    private String construct_Path_query(ArrayList<String> path, HashMap<String, ArrayList<String>> table2colmn_map, String taget_tbl, String query) {
//        String sql = "";
//        if (path != null && !path.isEmpty()) {
//            boolean error = false;
//            HashMap<Integer, String[]> constructer_map = new HashMap<>();
//            for (int i = (path.size() - 1); (i > 0 && !error); i--) {
//                String c_tbl = path.get(i);
//                String c_table_clm = null;
//                String c_previous_tbl = path.get(i - 1);
//                String c_previous_tbl_clm = null;
//                HashMap<String, String[]> constraint_map = get_key_contraints(c_tbl);
//                if (constraint_map == null || constraint_map.isEmpty() || !constraint_map.containsKey(FOREIGN_TABLE_FLAG) || !arrayMatch(c_previous_tbl, constraint_map.get(FOREIGN_TABLE_FLAG))) {
//                    constraint_map = get_key_contraints(c_previous_tbl);
//                    String[] forgn_colmn = constraint_map.get(FOREIGN_KEY_COLUMNS_FLAG);
//                    if (forgn_colmn != null) {
//                        boolean found = false;
//                        for (int j = 0; (j < forgn_colmn.length && !found); j++) {
//                            String c_clm = forgn_colmn[j];
//                            if (constraint_map.containsKey(c_clm) && constraint_map.get(c_clm)[1].equals(c_tbl)) {
//                                c_table_clm = constraint_map.get(c_clm)[2];
//                                c_previous_tbl_clm = c_clm;
//                                found = true;
//                            }
//                        }
//                    } else {
//                        error = true;
//                    }
//                } else {
//                    String[] forgn_colmn = constraint_map.get(FOREIGN_KEY_COLUMNS_FLAG);
//                    boolean found = false;
//                    for (int j = 0; (j < forgn_colmn.length && !found); j++) {
//                        String c_clm = forgn_colmn[j];
//                        if (constraint_map.containsKey(c_clm) && constraint_map.get(c_clm)[1].equals(c_previous_tbl)) {
//                            c_table_clm = c_clm;
//                            c_previous_tbl_clm = constraint_map.get(c_clm)[2];
//                            found = true;
//                        }
//                    }
//                }
//                if (c_table_clm == null || c_previous_tbl_clm == null) {
//                    error = true;
//                } else {
//                    String[] new_construct = new String[4];
//                    new_construct[0] = get_correct_table_name(c_tbl);
//                    new_construct[1] = c_table_clm;
//                    new_construct[2] = get_correct_table_name(c_previous_tbl);
//                    new_construct[3] = c_previous_tbl_clm;
//                    constructer_map.put(i, new_construct);
//                }
//            }
//            ArrayList<Integer> constructer_key_l = new ArrayList<>(constructer_map.keySet());
//            ArrayList<String> use_tbl_l = new ArrayList<>();
//            String join = null;
//            for (int i = 0; i < constructer_key_l.size(); i++) {
//                Integer c_k = constructer_key_l.get(i);
//                String[] c_a = constructer_map.get(c_k);
//                String c_join = c_a[0] + "." + c_a[1] + "=" + c_a[2] + "." + c_a[3];
//                use_tbl_l.remove(c_a[0]);
//                use_tbl_l.add(c_a[0]);
//                use_tbl_l.remove(c_a[2]);
//                use_tbl_l.add(c_a[2]);
//                if (join == null) {
//                    join = c_join;
//                } else {
//                    join = join + " AND " + c_join;
//                }
//            }
//            sql = "SELECT " + table2colmn_map.get(taget_tbl).toString().replace("[", "").replace("]", "").replaceAll("\\s", "") + " FROM " + use_tbl_l.toString().replace("[", "").replace("]", "").replaceAll("\\s", "") + " WHERE " + join + " AND " + query;
//
//
//
//        } else {
//            //print error
//        }
//        return sql;
//    }
//
//    private String construct_Path_query2(ArrayList<String> path, String query, String schema_name) {
//        String sql = "";
//
//        if (path != null && !path.isEmpty()) {
//            boolean error = false;
//            HashMap<Integer, String[]> constructer_map = new HashMap<>();
//            for (int i = (path.size() - 1); (i > 0 && !error); i--) {
//                String c_tbl = path.get(i);
//                String c_table_clm = null;
//                String c_previous_tbl = path.get(i - 1);
//                String c_previous_tbl_clm = null;
//                HashMap<String, String[]> constraint_map = get_key_contraints(c_tbl);
//                if (constraint_map == null || constraint_map.isEmpty() || !constraint_map.containsKey(FOREIGN_TABLE_FLAG) || !arrayMatch(c_previous_tbl, constraint_map.get(FOREIGN_TABLE_FLAG))) {
//                    constraint_map = get_key_contraints(c_previous_tbl);
//                    String[] forgn_colmn = constraint_map.get(FOREIGN_KEY_COLUMNS_FLAG);
//                    if (forgn_colmn != null) {
//                        boolean found = false;
//                        for (int j = 0; (j < forgn_colmn.length && !found); j++) {
//                            String c_clm = forgn_colmn[j];
//                            if (constraint_map.containsKey(c_clm) && constraint_map.get(c_clm)[1].equals(c_tbl)) {
//                                c_table_clm = constraint_map.get(c_clm)[2];
//                                c_previous_tbl_clm = c_clm;
//                                found = true;
//                            }
//                        }
//                    } else {
//                        error = true;
//                    }
//                } else {
//                    String[] forgn_colmn = constraint_map.get(FOREIGN_KEY_COLUMNS_FLAG);
//                    boolean found = false;
//                    for (int j = 0; (j < forgn_colmn.length && !found); j++) {
//                        String c_clm = forgn_colmn[j];
//                        if (constraint_map.containsKey(c_clm) && constraint_map.get(c_clm)[1].equals(c_previous_tbl)) {
//                            c_table_clm = c_clm;
//                            c_previous_tbl_clm = constraint_map.get(c_clm)[2];
//                            found = true;
//                        }
//                    }
//                }
//                if (c_table_clm == null || c_previous_tbl_clm == null) {
//                    error = true;
//                } else {
//                    String[] new_construct = new String[4];
//                    new_construct[0] = get_correct_table_name(getConnection(), c_tbl,schema_name);
//                    new_construct[1] = c_table_clm;
//                    new_construct[2] = get_correct_table_name(getConnection(), c_previous_tbl,schema_name);
//                    new_construct[3] = c_previous_tbl_clm;
//                    constructer_map.put(i, new_construct);
//                }
//            }
//
//            int construct_size = constructer_map.keySet().size();
//            String select_val_to_use = "*";
//            for (int i = construct_size; i > 0; i--) {
//                String[] c_constructs = constructer_map.get(i);
//                String[] next_c_constructs = constructer_map.get(i - 1);
//                String sql_tmp = null;
//                if (next_c_constructs != null) {
//                    if (i == construct_size) {
//                        sql_tmp = "SELECT " + select_val_to_use + " FROM " + c_constructs[0] + " WHERE " + c_constructs[0] + "." + c_constructs[1] + " IN (SELECT " + c_constructs[3] + " FROM " + c_constructs[2] + " WHERE " + c_constructs[2] + "." + next_c_constructs[1] + "  <EXPAND_REPLACER>)";
//                    } else {
//                        sql_tmp = " (SELECT " + c_constructs[3] + " FROM " + c_constructs[2] + " WHERE " + c_constructs[2] + "." + next_c_constructs[1] + "  <EXPAND_REPLACER>)";
//                    }
//                    if (sql.isEmpty()) {
//                        sql = sql_tmp;
//                    } else {
//                        sql = sql.replace("<EXPAND_REPLACER>", " in " + sql_tmp);
//                    }
//                } else {
//                    if (construct_size == 1) {
//                        if (c_constructs[3].equalsIgnoreCase("ID")) {
//                            sql_tmp = " SELECT " + select_val_to_use + " FROM " + c_constructs[0] + " WHERE " + c_constructs[0] + "." + c_constructs[1] + " = " + query + "";
//                        } else {
//                            sql_tmp = " SELECT " + select_val_to_use + " FROM " + c_constructs[0] + " WHERE " + c_constructs[0] + "." + c_constructs[1] + " in (SELECT " + c_constructs[3] + " FROM " + c_constructs[2] + " where id=" + query + ")";
//                        }
//                    } else {
//                        if (c_constructs[3].equalsIgnoreCase("ID")) {
//                            sql_tmp = "=" + query;
//                        } else {
//                            sql_tmp = " (SELECT " + c_constructs[3] + " FROM " + c_constructs[2] + " where id=" + query + ")";
//                        }
//                    }
//                    if (sql.isEmpty()) {
//                        sql = sql_tmp;
//                    } else {
//                        sql = sql.replace("<EXPAND_REPLACER>", sql_tmp);
//                    }
//                }
//
//            }
//        } else {
//            //print error
//        }
//        return sql;
//    }

    private boolean arrayMatch(String target, String[] arry_a) {
        boolean found = false;
        if (arry_a != null) {
            for (int i = 0; (i < arry_a.length && !found); i++) {
                if (arry_a[i].equals(target)) {
                    found = true;
                }
            }
        }

        return found;
    }

    private int addToDb(Connection c_conn, ArrayList<String> insert_sql_l) {
        int result = 0;
        System.out.println("Updating database");
        String c_sql = null;
        try {
            if (!c_conn.isClosed()) {
                Statement stm_1 = c_conn.createStatement();
                for (int i = 0; (i < insert_sql_l.size() && result >= 0); i++) {
                    if (i % 10 == 0) {
                        System.out.print(".");
                    }
                    c_sql = insert_sql_l.get(i);
                    result = stm_1.executeUpdate(insert_sql_l.get(i));
                }
                stm_1.close();
            }
        } catch (SQLException ex) {
            System.out.println("Error: " + ex.getMessage() + " for " + c_sql);
        }
        System.out.println("");
        return result;
    }

    private HashMap<String, String[]> get_key_contraints(Connection c_con, String schema_name, String current_tbl_nm) {
        if (key_constraint_map == null) {
            key_constraint_map = new HashMap<>();
        }
        HashMap<String, String[]> returning_map = new HashMap<>(1);
        if (current_tbl_nm != null && !current_tbl_nm.isEmpty() && !current_tbl_nm.equalsIgnoreCase("null") && !key_constraint_map.containsKey(current_tbl_nm)) {
            try {
                if (!c_con.isClosed()) {
                    returning_map.putAll(get_key_contraint_names(getConnection(), schema_name, current_tbl_nm));
                    try {
                        ArrayList<String> foreign_columns = new ArrayList<>(1);
                        ArrayList<String> foreign_key_names_columns = new ArrayList<>(1);
                        ArrayList<String> foreign_tables = new ArrayList<>(1);
                        DatabaseMetaData metaData = c_con.getMetaData();
                        String tablenm4metadata = current_tbl_nm.split("\\.")[current_tbl_nm.split("\\.").length - 1];

                        ResultSet key_result = metaData.getImportedKeys(c_con.getCatalog(), null, tablenm4metadata);
                        if (!key_result.next()) {
                            key_result = metaData.getImportedKeys(c_con.getCatalog(), schema_name, tablenm4metadata.toUpperCase());
                        } else {
                            String[] tmp_a = new String[4];
                            tmp_a[0] = key_result.getString("FKCOLUMN_NAME");//column_name                                 
                            tmp_a[1] = get_correct_table_name(c_con, key_result.getString("PKTABLE_NAME"), schema_name, 3100);//ref_tblm
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
                            tmp_a[1] = get_correct_table_name(c_con, key_result.getString("PKTABLE_NAME"), schema_name, 3200);//ref_tblm
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
     MethodID=19
     * 
     */
    private ArrayList<String> get_key_contraints_unique_identification(Connection c_con, String schema_name, String current_tbl_nm) {
        ArrayList<String> uniqs_list = new ArrayList<>(1);
        if (current_tbl_nm != null && !current_tbl_nm.isEmpty() && !current_tbl_nm.equalsIgnoreCase("null")) {
            try {
                if (c_con != null) {
                    try {
                        if (!c_con.isClosed()) {
                            DatabaseMetaData metaData = c_con.getMetaData();
                            String tablenm4metadata = current_tbl_nm;
                            if (current_tbl_nm.contains(".")) {
                                tablenm4metadata = current_tbl_nm.split("\\.")[1];
                            }
                            ResultSet key_result = metaData.getIndexInfo(null, schema_name, tablenm4metadata, false, false);//.getImportedKeys(ncon.getCatalog(), Constants.DATABASE_NAME_DATA, current_tbl_nm);

                            if (!key_result.next()) {
                                key_result = metaData.getIndexInfo(null, schema_name.toUpperCase(), tablenm4metadata.toUpperCase(), false, false);//.getImportedKeys(ncon.getCatalog(), Constants.DATABASE_NAME_DATA, current_tbl_nm);
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
//                            if (!uniqs_list.isEmpty()) {
//                                uniq_to_user_a = new String[uniqs_list.size()];
//                                for (int i = 0; i < uniq_to_user_a.length; i++) {
//                                    uniq_to_user_a[i] = uniqs_list.get(i);
//                                }
//                            }
                            key_result.close();
                        }
//                        ncon.close();
                    } catch (SQLException e) {
                        System.out.println("Error 8767" + e.getMessage());
                    }
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }

        }
        return uniqs_list;
    }

    private HashMap<String, String[]> get_key_contraint_names(Connection c_conn, String schema_name, String current_tbl_nm) {
        HashMap<String, String[]> returning_map = new HashMap<>();

        if (current_tbl_nm != null && !current_tbl_nm.isEmpty() && !current_tbl_nm.equalsIgnoreCase("null")) {
            try {
                if (!c_conn.isClosed()) {
                    try {
                        DatabaseMetaData metaData = c_conn.getMetaData();
                        String tablenm4metadata = current_tbl_nm;
                        if (current_tbl_nm.contains(".")) {
                            tablenm4metadata = current_tbl_nm.split("\\.")[1];
                        }
                        ResultSet key_result = metaData.getIndexInfo(null, schema_name, tablenm4metadata, false, false);//.getImportedKeys(ncon.getCatalog(), Constants.DATABASE_NAME_DATA, current_tbl_nm);
                        ArrayList<String> uniqs_list = new ArrayList<>(1);
                        ArrayList<String> all_list = new ArrayList<>(2);
                        if (!key_result.next()) {
                            key_result = metaData.getIndexInfo(null, schema_name.toUpperCase(), tablenm4metadata.toUpperCase(), false, false);//.getImportedKeys(ncon.getCatalog(), Constants.DATABASE_NAME_DATA, current_tbl_nm);
                        } else {
                            String index_name = key_result.getString("INDEX_NAME");
                            all_list.add(index_name);
                            index_name = index_name.toUpperCase();
                            if (index_name.startsWith(UNIQUE_ID_INDEX_NAME_PREFIX)) {
                                uniqs_list.add(key_result.getString("COLUMN_NAME"));
                            }
                        }
                        while (key_result.next()) {
                            String index_name = key_result.getString("INDEX_NAME");
                            if (index_name != null) {
                                all_list.add(index_name);
                                index_name = index_name.toUpperCase();
                                if (index_name.startsWith(UNIQUE_ID_INDEX_NAME_PREFIX)) {
                                    uniqs_list.add(key_result.getString("COLUMN_NAME"));
                                }
                            }
                        }
                        if (!uniqs_list.isEmpty()) {
                            String[] uniq_to_user_a = new String[uniqs_list.size()];
                            for (int i = 0; i < uniq_to_user_a.length; i++) {
                                uniq_to_user_a[i] = uniqs_list.get(i);
                            }
                            returning_map.put(UNIQUE_TO_USER_COLUMNS_FLAG, uniq_to_user_a);
                        }
                        if (!all_list.isEmpty()) {
                            String[] all_a = new String[all_list.size()];
                            for (int i = 0; i < all_a.length; i++) {
                                all_a[i] = all_list.get(i);
                            }
                            returning_map.put(ALL_INDEX_NAMES_FLAG, all_a);
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

    public boolean containsIgnoreCase(ArrayList<String> in_l, String matchit) {
        boolean found = false;
        if (in_l == null || in_l.isEmpty() || matchit == null || matchit.isEmpty() || matchit.trim().isEmpty()) {
        } else {
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
                System.out.println("Error AS19: creating connections " + ex.getMessage());
            }
        }

        return found;
    }
//    private void print_explain_query(String query){
//           if (query != null && !query.isEmpty() && !query.equalsIgnoreCase("null")) {
//            try {
//                if (createConnection()) {
//                    Statement st = conn.createStatement();
//                   ResultSet r = st.e
//                   
//                    
//                }
//            } catch (SQLException e) {
//                        System.out.println("Error 8767" + e.getMessage());
//                    }
//           }
//    }

    private void print_indices(Connection c_conn, String current_tbl_nm) {
        if (current_tbl_nm != null && !current_tbl_nm.isEmpty() && !current_tbl_nm.equalsIgnoreCase("null")) {
            try {
                if (c_conn != null) {
                    try {
                        DatabaseMetaData metaData = c_conn.getMetaData();
                        String tablenm4metadata = current_tbl_nm;
                        if (current_tbl_nm.contains(".")) {
                            tablenm4metadata = current_tbl_nm.split("\\.")[1];
                        }
                        ResultSet key_result = metaData.getIndexInfo(null, instruct_map.get(DATABASE_NAME_DATA_FLAG), tablenm4metadata, false, false);//.getImportedKeys(ncon.getCatalog(), Constants.DATABASE_NAME_DATA, current_tbl_nm);

                        if (!key_result.next()) {
                            key_result = metaData.getIndexInfo(null, instruct_map.get(DATABASE_NAME_DATA_FLAG).toUpperCase(), tablenm4metadata.toUpperCase(), false, false);//.getImportedKeys(ncon.getCatalog(), Constants.DATABASE_NAME_DATA, current_tbl_nm);
                        } else {
                            int columns = key_result.getMetaData().getColumnCount();
                            for (int i = 1; i < columns + 1; i++) {
                                String val = key_result.getString(i);
                                if (val != null && !val.isEmpty()) {
                                    System.out.println(key_result.getMetaData().getColumnName(i) + "=> " + val);
                                }

                            }
//                            System.out.println("TABLE_CAT => " + key_result.getString("TABLE_CAT"));
//                            System.out.println("TABLE_SCHEM => " + key_result.getString("TABLE_SCHEM"));
//                            System.out.println("NON_UNIQUE => " + key_result.getString("NON_UNIQUE"));
//                            System.out.println("INDEX_QUALIFIER => " + key_result.getString("INDEX_QUALIFIER"));
//                            System.out.println("INDEX_NAME => " + key_result.getString("INDEX_NAME"));
//                            System.out.println("TYPE => " + key_result.getString("TYPE"));
//                            System.out.println("tableIndexStatistic => " + key_result.getString("tableIndexStatistic"));
//                            System.out.println("tableIndexClustered => " + key_result.getString("tableIndexClustered"));
//                            System.out.println("tableIndexHashed => " + key_result.getString("tableIndexHashed"));
//                            System.out.println("tableIndexOther => " + key_result.getString("tableIndexOther"));
//                            System.out.println("ORDINAL_POSITION => " + key_result.getString("ORDINAL_POSITION"));
//                            System.out.println("COLUMN_NAME => " + key_result.getString("COLUMN_NAME"));
//                            System.out.println("ASC_OR_DESC=> " + key_result.getString("ASC_OR_DESC"));
//                            System.out.println("CARDINALITY => " + key_result.getString("CARDINALITY"));
//                            System.out.println("PAGES => " + key_result.getString("PAGES"));
//                            System.out.println("FILTER_CONDITION=> " + key_result.getString("FILTER_CONDITION"));
                            System.out.println("__________________________________________\n");
                        }
                        while (key_result.next()) {
                            int columns = key_result.getMetaData().getColumnCount();
                            for (int i = 1; i < columns + 1; i++) {
                                String val = key_result.getString(i);
                                if (val != null && !val.isEmpty()) {
                                    System.out.println(key_result.getMetaData().getColumnName(i) + "=> " + val);
                                }

                            }
//                            System.out.println("TABLE_CAT => " + key_result.getString("TABLE_CAT"));
//                            System.out.println("TABLE_SCHEM => " + key_result.getString("TABLE_SCHEM"));
//                            System.out.println("NON_UNIQUE => " + key_result.getString("NON_UNIQUE"));
//                            System.out.println("INDEX_QUALIFIER => " + key_result.getString("INDEX_QUALIFIER"));
//                            System.out.println("INDEX_NAME => " + key_result.getString("INDEX_NAME"));
//                            System.out.println("TYPE => " + key_result.getString("TYPE"));
//                            System.out.println("tableIndexStatistic => " + key_result.getString("tableIndexStatistic"));
//                            System.out.println("tableIndexClustered => " + key_result.getString("tableIndexClustered"));
//                            System.out.println("tableIndexHashed => " + key_result.getString("tableIndexHashed"));
//                            System.out.println("tableIndexOther => " + key_result.getString("tableIndexOther"));
//                            System.out.println("ORDINAL_POSITION => " + key_result.getString("ORDINAL_POSITION"));
//                            System.out.println("COLUMN_NAME => " + key_result.getString("COLUMN_NAME"));
//                            System.out.println("ASC_OR_DESC=> " + key_result.getString("ASC_OR_DESC"));
//                            System.out.println("CARDINALITY => " + key_result.getString("CARDINALITY"));
//                            System.out.println("PAGES => " + key_result.getString("PAGES"));
//                            System.out.println("FILTER_CONDITION=> " + key_result.getString("FILTER_CONDITION"));
                            System.out.println("__________________________________________\n");
                        }
                    } catch (SQLException e) {
                        System.out.println("Error 8767" + e.getMessage());
                    }
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }

        }
    }

    private ArrayList< String[]> get_reverse_key_contraints(Connection c_con, String schema_name, String current_tbl_nm,
            ArrayList<String> previous_tbl_nm_list) {

        ArrayList<String[]> returning_list = new ArrayList<String[]>(1);
        if (current_tbl_nm != null && !current_tbl_nm.isEmpty() && !current_tbl_nm.equalsIgnoreCase("null")) {
            try {
                if (c_con != null) {
                    current_tbl_nm = get_correct_table_name(c_con, current_tbl_nm, schema_name, 3300);
                    if (current_tbl_nm != null) {
                        ArrayList<String> table_l = getCurrentTables(c_con, schema_name, 8);
                        for (int i = 0; i < table_l.size(); i++) {
                            String c_tbl_nm = get_correct_table_name(c_con, table_l.get(i), schema_name, 400);
                            if ((c_tbl_nm != null && !c_tbl_nm.equals(current_tbl_nm)) && (previous_tbl_nm_list == null || previous_tbl_nm_list.contains(current_tbl_nm))) {
                                HashMap<String, String[]> constraint_map = get_key_contraints(c_con, schema_name, c_tbl_nm);
                                String[] key_cols = constraint_map.get(FOREIGN_KEY_COLUMNS_FLAG);
                                if (key_cols != null) {
                                    for (int k = 0; k < key_cols.length; k++) {
                                        String c_f_col = key_cols[k];
                                        String[] tmp_array = constraint_map.get(c_f_col);
                                        if (tmp_array != null && tmp_array.length >= 3) {
                                            String fr_tbl = get_correct_table_name(c_con, tmp_array[1], schema_name, 3500);
                                            if (fr_tbl.equals(current_tbl_nm)) {
                                                String[] result_a = new String[4];
                                                for (int j = 0; j < tmp_array.length; j++) {
                                                    result_a[j] = tmp_array[j];
                                                }
                                                result_a[3] = c_tbl_nm;
                                                returning_list.add(result_a);
                                            }
                                        }
                                    }
                                }

                            }

                        }

//                        }
//                        r_1.close();
                    }

                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }

        }
        return returning_list;
    }

//    public HashMap<String, String[]> get_key_contraints(Connection c_conn, String schema_name, String current_tbl_nm, String target_tbl_nm, String index_name) {
//        HashMap<String, String[]> final_returning_map = new HashMap<>(1);
//        HashMap<String, String[]> returning_map = new HashMap<>(1);
//        HashMap<String, String[]> returning_all_map = get_key_contraints(c_conn, schema_name, current_tbl_nm);
//        if (target_tbl_nm != null) {
//            if (returning_all_map != null && returning_all_map.containsKey(FOREIGN_KEY_COLUMNS_FLAG)) {
//                ArrayList<String> return_keys = new ArrayList<>(returning_all_map.keySet());
//                for (int i = 0; i < return_keys.size(); i++) {
//                    if (returning_all_map.get(return_keys.get(i)).length >= 3 && returning_all_map.get(return_keys.get(i))[1].equals(target_tbl_nm)) {
//                        returning_map.put(returning_all_map.get(return_keys.get(i))[0], returning_all_map.get(return_keys.get(i)));
//                    }
//                }
//            }
//        } else {
//            returning_all_map.putAll(returning_map);
//        }
//        if (index_name != null) {
//            if (returning_all_map != null && returning_all_map.containsKey(FOREIGN_KEY_NAMES_FLAG)) {
//                ArrayList<String> return_keys = new ArrayList<String>(returning_map.keySet());
//                for (int i = 0; i < return_keys.size(); i++) {
//                    if (returning_map.get(return_keys.get(i)).length >= 4) {
//                        String c_index_name = returning_map.get(return_keys.get(i))[3].toUpperCase();
//                        if (c_index_name.startsWith(index_name.toUpperCase())) {
//                            final_returning_map.put(returning_all_map.get(return_keys.get(i))[0], returning_all_map.get(return_keys.get(i)));
//                        }
//                    }
//                }
//            }
//
//        } else {
//            final_returning_map.putAll(returning_map);
//        }
////        System.out.println("3138 "+final_returning_map.keySet());
//        return final_returning_map;
//    }
    private ArrayList<String> getColumns(Connection c_con, String schema_name, String current_tbl_nm) {
        ArrayList<String> columns_l = new ArrayList<>(2);
        if (table2Columns_map == null && c_con != null) {
            table2Columns_map = new HashMap<>();
            try {
                DatabaseMetaData metaData = c_con.getMetaData();
                if (!c_con.isClosed()) {
                    ArrayList<String> c_table_l = getCurrentTables(c_con, schema_name, 9);
                    for (int i = 0; i < c_table_l.size(); i++) {
                        ArrayList<String> tmp_columns_l = new ArrayList<>(2);
                        String c_table = c_table_l.get(i);
                        String tablenm4metadata = c_table;
                        if (c_table.contains(".")) {
                            tablenm4metadata = c_table.split("\\.")[1];
                        }

                        ResultSet key_result = metaData.getColumns(c_con.getCatalog(), null, tablenm4metadata, null);
                        if (!key_result.next()) {
                            key_result = metaData.getColumns(null, schema_name, tablenm4metadata.toUpperCase(), null);
                        } else {
                            String clmname = key_result.getString("COLUMN_NAME");
                            if (!tmp_columns_l.contains(clmname)) {
                                tmp_columns_l.add(clmname);
                            }
                        }
                        while (key_result.next()) {
                            String clmname = key_result.getString("COLUMN_NAME");
                            if (!tmp_columns_l.contains(clmname)) {
                                tmp_columns_l.add(clmname);
                            }
                        }
                        table2Columns_map.put(c_table, tmp_columns_l);
                        key_result.close();
                    }
                }
            } catch (SQLException e) {
                System.out.println("Error C1a" + e.getMessage());
            }
        }
        current_tbl_nm = get_correct_table_name(c_con, current_tbl_nm, schema_name, 3600);
        if (current_tbl_nm != null && table2Columns_map.containsKey(current_tbl_nm)) {
            columns_l = table2Columns_map.get(current_tbl_nm);
        } else if (table2Columns_map.containsKey(current_tbl_nm.toUpperCase())) {
            columns_l = table2Columns_map.get(current_tbl_nm.toUpperCase());
        }
        return columns_l;
    }

    private String randomstring(int n) {
        byte[] b = new byte[n];
        for (int i = 0; i < n; i++) {
            int c_val = rand('A', 'z');
            int c_val2 = rand('0', '9');
            if (c_val % 2 == 0) {
                b[i] = (byte) c_val2;
            } else {
                b[i] = (byte) c_val;
            }
        }
        String pass = new String(b);
        return pass;
    }

    private boolean isPortAvailable(int port) {
        boolean portAvailable = false;
        ServerSocket socket = null;
        try {
            socket = new ServerSocket(port);
            socket.close();
            portAvailable = true;
        } catch (IOException e) {
        }
        socket = null;
        return portAvailable;
    }

    private int findFreePOrt(int start, int stop) {
        int found_port = -1;
        for (int i = start; (i <= stop && found_port < 0); i++) {
            if (isPortAvailable(i)) {
                found_port = i;
            }
        }
        return found_port;
    }

    private String printformater_terminal(String intext, int comment_line_length) {
        String comment = "";
        String[] intext_a = intext.split("¤");
        for (int i = 0; i < intext_a.length; i++) {
            String c_commnet = intext_a[i];

            if (c_commnet.equalsIgnoreCase("§")) {
                comment = comment + "\n";
            } else {
                String[] split_a = c_commnet.split("\\s");
                String new_comment = "";
                for (int j = 0; j < split_a.length; j++) {
                    split_a[j] = split_a[j].trim();
                    if (!new_comment.isEmpty()) {
                        new_comment = new_comment + " ";
                    }
                    if (!split_a[j].isEmpty()) {
                        new_comment = new_comment + split_a[j];
                    }
                    if (new_comment.length() > comment_line_length) {
                        if (!comment.isEmpty()) {
                            comment = comment + "\n";
                        }
                        comment = comment + new_comment;
                        new_comment = "";
                    }
                }
                if (!new_comment.isEmpty()) {
                    if (!comment.isEmpty()) {
                        comment = comment + "\n";
                    }
                    comment = comment + new_comment;
                }
            }

        }
        String line = "";
        for (int i = 0; i < comment_line_length; i++) {
            line = line + "_";

        }
        comment = comment.replaceAll("\\*", "\t*");
        comment = comment + "\n" + line;
        return comment;
    }

    public static int getUserChoice(ArrayList<String> tables_l, String msg) {
        int result = -1;
        if (tables_l != null) {
            String choices = "";
            for (int i = 0; i < tables_l.size(); i++) {
                choices = choices + i + ") " + tables_l.get(i) + "\n";
            }

            boolean ok = false;
            while (!ok) {
                String ans = getuserInputSameLine(msg, choices);
                if (ans != null && ans.trim().matches("[0-9]+")) {
                    int coince = new Integer(ans);
                    if (coince >= 0 && tables_l.size() > coince) {
                        ok = true;
                        result = coince;
                    }
                }
                if (!ok) {
                    System.out.println("Invalid choice, try again");
                }
            }
        }
        return result;
    }

    private int getUserChoice(String[] tables_a, String msg) {
        int result = -1;
        if (tables_a != null) {
            String choices = "";
            for (int i = 0; i < tables_a.length; i++) {
                choices = choices + i + ") " + tables_a[i] + "\n";
            }

            boolean ok = false;
            while (!ok) {
                String ans = getuserInputSameLine(msg, choices);
                if (ans != null && ans.trim().matches("[0-9]+")) {
                    int coince = new Integer(ans);
                    if (coince >= 0 && tables_a.length > coince) {
                        ok = true;
                        result = coince;
                    }
                }
                if (!ok) {
                    System.out.println("Invalid choice, try again");
                }
            }
        }
        return result;
    }

    /*
     MethodID=15
     */
    private boolean sendEmail_LocalHost(String host, int port, String user, String password, String subject, String message_text, String send_to) {
        boolean result = false;
        try {
            Properties props = new Properties();
            props.put("mail.transport.protocol", "smtp");
            props.put("mail.smtp.host", "localhost");

            Session mailSession = Session.getDefaultInstance(props);
            Transport transport = mailSession.getTransport();
            MimeMessage message = new MimeMessage(mailSession);
            message.setSubject(subject);
            message.setText(message_text);
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(send_to));
            transport.connect(host, port, user, password);
            transport.sendMessage(message, message.getRecipients(Message.RecipientType.TO));
            transport.close();
            result = true;
        } catch (MessagingException ex) {
            result = false;
            String msg = ex.getMessage();
            System.out.println("Error 15a: " + ex.getMessage());

        } catch (NullPointerException ex) {
            result = false;
            String msg = ex.getMessage();
            System.out.println("Error 15b: " + ex.getMessage());

        } catch (Exception ex) {
            result = false;
            String msg = ex.getMessage();
            System.out.println("Error 15c: " + ex.getMessage());

        }
        return result;
    }

    public boolean sendmail_quick(String subject, String message) {
        boolean result = false;
        return sendEmail(false, instruct_map.get("--mailhost"), new Integer(instruct_map.get("mail.smtp.port")),
                instruct_map.get("--mailuser"), instruct_map.get("mail.smtp.password"),
                subject, message + "\n From " + instruct_map.get(SERVER_ROOT_FLAG), instruct_map.get("--fromaddress"), instruct_map.get(TRUST_STORE_FILE_NM), "changeit");
    }

    /*
     MethodID=16
     */
    private boolean sendEmail(boolean uselocalhost, String host, int port, String user, String password, String subject, String message_text,
            String send_to, String trustore, String store_password) {
        boolean result = false;
        if (uselocalhost) {
            result = sendEmail_LocalHost(host, port, user, password, subject, message_text, send_to);
        } else {
            try {
                Properties props = new Properties();
                props.put("mail.transport.protocol", instruct_map.get("mail.transport.protocol"));
                props.put("mail.smtps.host", instruct_map.get("mail.smtps.host"));
                props.put("mail.smtps.auth", instruct_map.get("mail.smtps.auth"));
                props.put("mail.smtps.socketFactory", createSSLTestConfig(trustore, store_password));
                props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
                props.put("mail.smtp.debug", "true");
                props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
                props.put("mail.smtp.socketFactory.fallback", "false");
                Session mailSession = Session.getDefaultInstance(props);
                Transport transport = mailSession.getTransport();
                MimeMessage message = new MimeMessage(mailSession);
                message.setSubject(subject);
                message.setText(message_text);
                message.addRecipient(Message.RecipientType.TO, new InternetAddress(send_to));
                transport.connect(host, port, user, password);
                transport.sendMessage(message, message.getRecipients(Message.RecipientType.TO));
                transport.close();
                result = true;
            } catch (MessagingException ex) {
                System.out.println("\nFaild to send mail. Error 16a: " + ex.getMessage() + "\n Try again in 5 minute. If still get this error recreate certificates "
                        + "(sh start.sh -fromlast -recreate_mail_certificates)");
//                StackTraceElement[] error_a = ex.getStackTrace();
//                for (int i = 0; i < error_a.length; i++) {
//                    System.out.println(error_a[i]);
//                }
                ProgressMonitor.cancel();
            } catch (Exception ex) {
                System.out.println("\nFaild to send mail. Error 16a: " + ex.getMessage() + "\n Try again in 5 minute. If still get this error recreate certificates "
                        + "(sh start.sh -fromlast -recreate_mail_certificates)");
//                for (int i = 0; i < error_a.length; i++) {
//                    System.out.println(error_a[i]);
//                }
                ProgressMonitor.cancel();
            }
        }
        return result;
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

    private int setServerIdentity(Connection c_conn, String schema_name) {
        String idenitty = instruct_map.get(S_SERVER_NAME_FLAG);
        int result = 0;
        String id_table = get_correct_table_name(c_conn, "serveridentity", schema_name, 3700);
        if (c_conn != null) {
            if (id_table == null) {
                try {
                    if (!c_conn.isClosed()) {
                        Statement stm = c_conn.createStatement();
                        String sql = null;
                        try {
                            sql = "CREATE TABLE " + schema_name + ".serveridentity(id int NOT NULL GENERATED ALWAYS AS "
                                    + "IDENTITY (START WITH 1, INCREMENT BY 1) PRIMARY KEY,name varchar(256),ip char(16),host varchar(256), hashed_name char(64))";
                            result = stm.executeUpdate(sql);
                        } catch (SQLException ex) {
                            result = -1;
                            System.out.println("Error 59b: " + sql + "\n" + ex.getMessage());
                        }
                        stm.close();
                    }
                } catch (SQLException ex) {
                    System.out.println("Error 59g: " + ex.getMessage());
                }

            }
//        long c_time =  (new GregorianCalendar()).getTimeInMillis();//instruct_map.get(SERVER_ID_FLAG);

            try {
                if (!c_conn.isClosed()) {
                    Statement stm = c_conn.createStatement();
                    String sel_sql = null;
                    try {
                        sel_sql = "Select 1 from " + get_correct_table_name(c_conn, "serveridentity", schema_name, 3800)
                                + " where name='" + idenitty + "'";
                        ResultSet r_1 = stm.executeQuery(sel_sql);
                        if (!r_1.next()) {
                            String inst_sql = "Insert into  "
                                    + get_correct_table_name(c_conn, "serveridentity", schema_name, 3900) + "(name,ip,host,hashed_name) "
                                    + "values('" + idenitty + "','" + instruct_map.get(SERVER_IP_FLAG) + "','"
                                    + instruct_map.get(SERVER_BASE_FLAG) + "','" + getHash(idenitty) + "')";
                            stm.executeUpdate(inst_sql);
                        }
                    } catch (SQLException ex) {
                        result = -1;
                        System.out.println("Error 59b: " + sel_sql + "\n" + ex.getMessage());
                    }
                    stm.close();
                }
            } catch (SQLException ex) {
                System.out.println("Error 59g: " + ex.getMessage());
            }
        } else {
            System.out.println("Error 59h: Error setting server identity ");
        }

        System.out.println("Server identitity URL: " + idenitty);
        return result;
    }

    /*
     ADD A ISKEY COLUMN
     */
    private int refreshTableTOFeatures(Connection c_conn, String schema_name) {
        int result = 0;
        if (c_conn != null) {
            ArrayList<String> sql_l = new ArrayList<>();
            sql_l.add("DROP TABLE " + schema_name + ".tablename2feature");
            sql_l.add("CREATE TABLE " + schema_name + ".tablename2feature(id int NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1)  PRIMARY KEY,"
                    + "table_name varchar(128),"
                    + "column_nm varchar(128) default 'name',"
                    + "isunique int default 0,"
                    + "description varchar(256),"
                    + "category varchar(128),"
                    + "simplename varchar(256),"
                    + "table_description varchar(256) default 'NA',"
                    + "search_description varchar(128) default 'NA',"
                    + "usageinfo varchar(256) default null,"
                    + "showinsearch int default 0,"
                    + "taggable int default 0,"
                    + "avialable int default 0,"
                    + "access_level int default 1)");
            sql_l.add("insert into " + schema_name + ".tablename2feature(table_name,column_nm) (select (SELECT TABLENAME from  SYS.SYSTABLES where TABLEID=SYS.SYSCOLUMNS.REFERENCEID), COLUMNNAME from SYS.SYSCOLUMNS WHERE (SELECT TABLENAME from SYS.SYSTABLES where CAST(SYS.SYSTABLES.TABLETYPE AS CHAR(1))='T' and SYS.SYSTABLES.SCHEMAID=(select SCHEMAID from SYS.SYSSCHEMAS where CAST(SYS.SYSSCHEMAS.SCHEMANAME AS VARCHAR(128))='" + schema_name + "') and TABLEID=SYS.SYSCOLUMNS.REFERENCEID) is not null )");

            sql_l.add("update " + schema_name + ".tablename2feature set category=table_name");
            sql_l.add("update " + schema_name + ".tablename2feature set simplename=table_name");
            sql_l.add("update " + schema_name + ".tablename2feature set showinsearch=1 where  table_name not in ('tablename2feature','advancedqueryconstructor','errors','fieldhelp','queryExpander','tabledescription','tmp')");
            sql_l.add("update " + schema_name + ".tablename2feature set taggable=1 where  table_name in ('files', 'report','report_batch','sampledetails')");
            sql_l.add("update " + schema_name + ".tablename2feature set avialable=1 where  table_name not in ('tablename2feature','advancedqueryconstructor','errors','fieldhelp','queryExpander','tabledescription','tmp')");
            sql_l.add("update " + schema_name + ".tablename2feature set description='Auto increment key , should not attempt to alter' where  column_nm='ID'");
            sql_l.add("update " + schema_name + ".tablename2feature set description='Personal information, such as name or email' where table_name='person' and column_nm='name' ");
            sql_l.add("update " + schema_name + ".tablename2feature set description='file name or the location path' where table_name='files' and column_nm='name'");
            sql_l.add("update " + schema_name + ".tablename2feature set description='report details (report is a collection of files)' where table_name='report' and  column_nm='name'");
            sql_l.add("update " + schema_name + ".tablename2feature set description='sample name' where table_name='sampledetails' and column_nm='name' ");
            sql_l.add("update " + schema_name + ".tablename2feature set description='sample id' where table_name='sampledetails' and column_nm='sample_id' ");
            sql_l.add("update " + schema_name + ".tablename2feature set description='donor details (cell line, donor id, tissue etc.)'  where table_name='donordetails' and column_nm='name'");
            sql_l.add("update " + schema_name + ".tablename2feature set description='The name of the Report_batch. Report batch is a collection of reports.'  where table_name='report_batch' and column_nm='name'");
            sql_l.add("update " + schema_name + ".tablename2feature set description='The parent of the donor. E.g. if the sample was from a tissue, then the organ where the tissue was obtained was the parent ' where table_name='donordetails' and column_nm='parent_id' ");
            sql_l.add("update " + schema_name + ".tablename2feature set description='Description of donor' where table_name='donordetails' and column_nm='description' ");
            sql_l.add("update " + schema_name + ".tablename2feature set description='Internal identifier for the donor (e.g. An internal  identifier referring to patient)' where table_name='donordetails' and column_nm='internal_identifier' ");
            sql_l.add("update " + schema_name + ".tablename2feature set description='A serial number for the donor (e.g. identifier used by the company where the donor was purchased)' where table_name='donordetails' and column_nm='serial_number' ");
            sql_l.add("update " + schema_name + ".tablename2feature set description='A ' where table_name='donordetails' and column_nm='name' ");
            sql_l.add("update " + schema_name + ".tablename2feature set description='The size of the file in bytes' where table_name='files' and column_nm='filesize' ");
            sql_l.add("update " + schema_name + ".tablename2feature set description='A description about the file and its content' where table_name='files' and column_nm='description' ");
            sql_l.add("update " + schema_name + ".tablename2feature set description='The MD5 check-sum value for the file' where table_name='files' and column_nm='checksum' ");
            sql_l.add("update " + schema_name + ".tablename2feature set description='The absolute path for the file' where table_name='files' and column_nm='location' ");
            sql_l.add("update " + schema_name + ".tablename2feature set description='The date last modification was done' where table_name='files' and column_nm='lastmodified' ");
            sql_l.add("update " + schema_name + ".tablename2feature set description='The ownership information as reported by the filesystem' where table_name='files' and column_nm='ownergroup' ");
            sql_l.add("update " + schema_name + ".tablename2feature set description='The name for the file. This is usually the absolute path. level 2 or higher privilege can view this. ' where table_name='files' and column_nm='name' ");
            sql_l.add("update " + schema_name + ".tablename2feature set description='This table used in the tagging process. The files_id refers to an entry in the files table' where table_name='files2tags' and column_nm='files_id' ");
            sql_l.add("update " + schema_name + ".tablename2feature set description='This table used in the tagging process. A description about the tag' where table_name='files2tags' and column_nm='description' ");
            sql_l.add("update " + schema_name + ".tablename2feature set description='This table used in the tagging process. The name for the tag, refering to an entry in the tag source.' where table_name='files2tags' and column_nm='name' ");
            sql_l.add("update " + schema_name + ".tablename2feature set description='A link to more descriptions about the tag.' where table_name='files2tags' and column_nm='link_to_feature' ");
            sql_l.add("update " + schema_name + ".tablename2feature set description='The connection between the files and the reports, this is the report identifier' where table_name='files2report' and column_nm='report_id' ");
            sql_l.add("update " + schema_name + ".tablename2feature set description='The connection between the files and the reports, this is the files identifier' where table_name='files2report' and column_nm='files_id' ");
            sql_l.add("update " + schema_name + ".tablename2feature set description='The connection between the files and the samples, this is a short description about the sample and the file' where table_name='files2sampledetails' and column_nm='description' ");
            sql_l.add("update " + schema_name + ".tablename2feature set description='The connection between the files and the samples, this is the files id' where table_name='files2sampledetails' and column_nm='files_id' ");
            sql_l.add("update " + schema_name + ".tablename2feature set description='The connection between the files and the samples, this is the sample detail id' where table_name='files2sampledetails' and column_nm='sampledetails_id' ");
            sql_l.add("update " + schema_name + ".tablename2feature set description='The connection between the files, sample and the samples , this is the donor where the sample is coming from' where table_name='files2sampledetails' and column_nm='donordetails_id' ");
            sql_l.add("update " + schema_name + ".tablename2feature set description='The connection between the files, The name for this relationship between the file and the sample' where table_name='files2sampledetails' and column_nm='name' ");
            sql_l.add("update " + schema_name + ".tablename2feature set description='The name for the relationship (optional)' where table_name='file_hierarchy' and column_nm='name' ");
            sql_l.add("update " + schema_name + ".tablename2feature set description='The id of the parent file from the files table' where table_name='file_hierarchy' and column_nm='parentfile_id' ");
            sql_l.add("update " + schema_name + ".tablename2feature set description='The id of the child file from the files table' where table_name='file_hierarchy' and column_nm='childfile_id' ");
            sql_l.add("update " + schema_name + ".tablename2feature set description='The id of the address from the address table' where table_name='person' and column_nm='address_id' ");
            sql_l.add("update " + schema_name + ".tablename2feature set description='Where do you work' where table_name='person' and column_nm='Organizationinfo' ");
            sql_l.add("update " + schema_name + ".tablename2feature set description='A unique name, usually the email' where table_name='person' and column_nm='name' ");
            sql_l.add("update " + schema_name + ".tablename2feature set description='email' where table_name='person' and column_nm='email' ");
            sql_l.add("update " + schema_name + ".tablename2feature set description='First name' where table_name='person' and column_nm='lastnm' ");
            sql_l.add("update " + schema_name + ".tablename2feature set description='Last name' where table_name='person' and column_nm='name' ");
            sql_l.add("update " + schema_name + ".tablename2feature set description='A free text description about the report' where table_name='report' and column_nm='description' ");
            sql_l.add("update " + schema_name + ".tablename2feature set description='The type of the report, an identifier referring to the reportType table' where table_name='report' and column_nm='reporttype_id' ");
            sql_l.add("update " + schema_name + ".tablename2feature set description='The person reporting, an identifier referring to the person table' where table_name='report' and column_nm='reporter_id' ");
            sql_l.add("update " + schema_name + ".tablename2feature set description='The batch this report belongs to, an identifier referring to the report_batch table' where table_name='report' and column_nm='report_batch_id' ");
            sql_l.add("update " + schema_name + ".tablename2feature set description='When was the report created. Usually added automatically' where table_name='report' and column_nm='entryDate' ");
            sql_l.add("update " + schema_name + ".tablename2feature set description='The name of the report, usually the directory name where the files are stored' where table_name='report' and column_nm='name' ");
            sql_l.add("update " + schema_name + ".tablename2feature set description='The id of the report which the properties is attached to' where table_name='report2tags' and column_nm='report_id' ");
            sql_l.add("update " + schema_name + ".tablename2feature set description='A free text description of this property' where table_name='report2tags' and column_nm='description' ");
            sql_l.add("update " + schema_name + ".tablename2feature set description='An optional name property of the report' where table_name='report2tags' and column_nm='name' ");
            sql_l.add("update " + schema_name + ".tablename2feature set description='A link pointing to a feature or a additional information table, this would normally be automatically generated ' where table_name='report2tags' and column_nm='link_to_feature' ");
            sql_l.add("update " + schema_name + ".tablename2feature set description='A description about the report sample relationship' where table_name='report2sampledetails' and column_nm='description' ");
            sql_l.add("update " + schema_name + ".tablename2feature set description='The id of the report, pointing to a entry in the report table' where table_name='report2sampledetails' and column_nm='report_id' ");
            sql_l.add("update " + schema_name + ".tablename2feature set description='The id of the sampledetails table, where the sample details are described ' where table_name='report2sampledetails' and column_nm='sampledetails_id' ");
            sql_l.add("update " + schema_name + ".tablename2feature set description='The id of the donor_detials, where the donor of the sample is described' where table_name='report2sampledetails' and column_nm='donordetails_id' ");
            sql_l.add("update " + schema_name + ".tablename2feature set description='An optional name for this relationship' where table_name='report2sampledetails' and column_nm='name' ");
            sql_l.add("update " + schema_name + ".tablename2feature set description='The name for the report batch, a report batch is a collection of reports' where table_name='report_batch' and column_nm='name' ");
            sql_l.add("update " + schema_name + ".tablename2feature set description='The report batch which the property is attached to' where table_name='report_batch2tags' and column_nm='report_batch_id' ");
            sql_l.add("update " + schema_name + ".tablename2feature set description='A free text description of this property' where table_name='report_batch2tags' and column_nm='description' ");
            sql_l.add("update " + schema_name + ".tablename2feature set description='An optional name for this relationship' where table_name='report_batch2tags' and column_nm='name' ");
            sql_l.add("update " + schema_name + ".tablename2feature set description='A link pointing to a feature or a additional information table, this would normally be automatically generated' where table_name='report_batch2tags' and column_nm='link_to_feature' ");
            sql_l.add("update " + schema_name + ".tablename2feature set description='The id of the child report_batch. This is the report batch which has inherited some property for the parent' where table_name='report_batch_hierarchy' and column_nm='childreport_batch_id' ");
            sql_l.add("update " + schema_name + ".tablename2feature set description='The id of the parent report_batch. This is the report batch which is inheriting some property for the child' where table_name='report_batch_hierarchy' and column_nm='parentreport_batch_id' ");
            sql_l.add("update " + schema_name + ".tablename2feature set description='An optional name for this relationship' where table_name='report_batch_hierarchy' and column_nm='name' ");
            sql_l.add("update " + schema_name + ".tablename2feature set description='The id of the child report. This is the report which has inherited some property for the parent' where table_name='report_hierarchy' and column_nm='childreport_id' ");
            sql_l.add("update " + schema_name + ".tablename2feature set description='The id of the parent report. This is the report which is inheriting some property for the child' where table_name='report_hierarchy' and column_nm='parentreport_id' ");
            sql_l.add("update " + schema_name + ".tablename2feature set description='An optional name for this relationship' where table_name='report_hierarchy' and column_nm='name' ");
            sql_l.add("update " + schema_name + ".tablename2feature set description='A short description about the sample' where table_name='sampledetails' and column_nm='description' ");
            sql_l.add("update " + schema_name + ".tablename2feature set description='The name of the sample.' where table_name='sampledetails' and column_nm='name' ");
            sql_l.add("update " + schema_name + ".tablename2feature set description='An optional name for this relationship' where table_name='donordetails_hierarchy' and column_nm='name'");
            sql_l.add("update " + schema_name + ".tablename2feature set description='The parent of the donor. This could literally be the parent of the person donated the sample, the cell line the current donor is from, the parent tissue etc..  ' where table_name='donordetails_hierarchy' and column_nm='parentdonordetails_id'");
            sql_l.add("update " + schema_name + ".tablename2feature set description='The child component of this relationship' where table_name='donordetails_hierarchy' and column_nm='childdonordetails_id'");
            sql_l.add("update " + schema_name + ".tablename2feature set description='An optional name for this relationship' where table_name='files_hierarchy' and column_nm='name'");
            sql_l.add("update " + schema_name + ".tablename2feature set description='The parent file id, refer to a id in the files table.' where table_name='files_hierarchy' and column_nm='parentfiles_id'");
            sql_l.add("update " + schema_name + ".tablename2feature set description='The child file id, refer to a id in the files table' where table_name='files_hierarchy' and column_nm='childfiles_id'");
            sql_l.add("update " + schema_name + ".tablename2feature set description='A short description about this report batch' where table_name='report_batch' and column_nm='description'");
            sql_l.add("update " + schema_name + ".tablename2feature set description='A name for this relationship (optional)' where table_name='sampledetails_hierarchy' and column_nm='name'");
            sql_l.add("update " + schema_name + ".tablename2feature set description='The parent sample' where table_name='sampledetails_hierarchy' and column_nm='parentsampledetails_id'");
            sql_l.add("update " + schema_name + ".tablename2feature set description='The child sample' where table_name='sampledetails_hierarchy' and column_nm='childsampledetails_id'");
            sql_l.add("update " + schema_name + ".tablename2feature set description='biological_ontologies' where table_name='biological_ontologies' and column_nm='table_name'");
            sql_l.add("update " + schema_name + ".tablename2feature set description='Connecting samples with standard tags' where table_name='sampledetails2tags' and column_nm='description'");
            sql_l.add("update " + schema_name + ".tablename2feature set description='Connecting samples with standard tags, the link to the  feature ' where table_name='sampledetails2tags' and column_nm='link_to_feature'");
            sql_l.add("update " + schema_name + ".tablename2feature set description='Connecting samples with standard tags, the sample which is tagged' where table_name='sampledetails2tags' and column_nm='sampledetails2tags_id'");
            sql_l.add("update " + schema_name + ".tablename2feature set description='Connecting samples with standard tags, optional name' where table_name='sampledetails2tags' and column_nm='name'");
            sql_l.add("update " + schema_name + ".tablename2feature set description='Absolute path of the file' where table_name='FILES2PATH' and column_nm='FILEPATH'");
            sql_l.add("update " + schema_name + ".tablename2feature set description='Link local servers with remote or public servers' where table_name='tags2host' and column_nm='name'");

            /* Set category  */
            sql_l.add("update " + schema_name + ".tablename2feature set showinsearch=1 where table_name not in ('tablename2feature','advancedqueryconstructor','errors','fieldhelp','queryExpander','tabledescription','tmp')");
            sql_l.add("update " + schema_name + ".tablename2feature set taggable=1 where table_name in ('files', 'report','report_batch','sampledetails')");
            sql_l.add("update " + schema_name + ".tablename2feature set avialable=1 where table_name not in ('tablename2feature','advancedqueryconstructor','errors','fieldhelp','queryExpander','tabledescription','tmp')");
            sql_l.add("update " + schema_name + ".tablename2feature set category='Other'");


            sql_l.add("update " + schema_name + ".tablename2feature set category='Tags' where table_name like '%2tags%' or table_name like '%tags2%'");
            sql_l.add("update " + schema_name + ".tablename2feature set category='People/Groups/companies' where table_name in ('person')");
            sql_l.add("update " + schema_name + ".tablename2feature set category='File name or location' where table_name in ('files', 'files2path')");
            sql_l.add("update " + schema_name + ".tablename2feature set category='Reports or a Report batch' where table_name in ('report', 'report_batch')");
            sql_l.add("update " + schema_name + ".tablename2feature set category='Donors or samples' where table_name in ('sampledetails', 'donordetails')");
            sql_l.add("update " + schema_name + ".tablename2feature set category='Relationships and data lineage' where  category='Other' and (table_name like '%HIERARCHY%'  or table_name like '%2%')");
            sql_l.add("update " + schema_name + ".tablename2feature set category='Tag sources' where  category='Other' and table_name like '%tagsource%'");
            sql_l.add("update " + schema_name + ".tablename2feature set category='Public Tags' where table_name in ('tags2hos')");
            sql_l.add("update " + schema_name + ".tablename2feature set simplename=table_name");

            /*Access level */

            sql_l.add("update " + schema_name + ".tablename2feature set access_level=8 where  category in ('Tags','Reports or a Report batch','Relationships and data lineage','Tag sources') ");
            sql_l.add("update " + schema_name + ".tablename2feature set access_level=7 where  category in ('File name or location','Donors or samples') ");
            sql_l.add("update " + schema_name + ".tablename2feature set access_level=6 where  category='People/Groups/companies' ");
            sql_l.add("update " + schema_name + ".tablename2feature set access_level=0 where  category='other' ");
            sql_l.add("update " + schema_name + ".tablename2feature set access_level=10 where table_name in ('tags2host')");
            try {
                if (!c_conn.isClosed()) {
                    Statement stm = c_conn.createStatement();
                    for (int i = 0; (i < sql_l.size() && result >= 0); i++) {
                        try {
                            result = stm.executeUpdate(sql_l.get(i));
                        } catch (SQLException ex) {
                            result = -1;
                            System.out.println("Error 59b: " + sql_l.get(i) + "\n" + ex.getMessage());
                        }
                    }

//                    String sql_sel = "select id, table_name from  " + schema_name + ".tablename2feature where  category='Tags' group by table_name";
//                    ResultSet r = stm.executeQuery(sql_sel);
//                    while (r.next()) {
//                        String tbl = r.getString(1);
//                        String clm = r.getString(2);
//                        ArrayList<String> uniq_l = get_key_contraints_unique_identification(c_conn, schema_name, tbl);
//                        System.out.println("7115 " + tbl + "=>" + uniq_l);
//                    }
                    stm.close();
                }

            } catch (SQLException ex) {
                System.out.println("Error 59g: " + ex.getMessage());
            }

        } else {
            System.out.println("Error 59h: failed to stablish connection");
        }
        return result;
    }

    private int refreshErroressages(Connection c_conn, String schema_name) {
        int result = 0;
        if (c_conn != null) {
            ArrayList<String> sql_l = new ArrayList<>();
            sql_l.add("DROP TABLE " + schema_name + ".errormsgs");
            sql_l.add("CREATE TABLE " + schema_name + ".errormsgs(id int NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1)  PRIMARY KEY,message varchar(512))");
            sql_l.add("insert into " + schema_name + ".errormsgs(message) values('NA')");
            sql_l.add("insert into " + schema_name + ".errormsgs(message) values('Error:Unknown error')");
            sql_l.add("insert into " + schema_name + ".errormsgs(message) values('Error:All the source columns should be from the same table')");
            sql_l.add("insert into " + schema_name + ".errormsgs(message) values('Error: Invalid query')");
            sql_l.add("insert into " + schema_name + ".errormsgs(message) values('Error: Query format not undestood')");
            sql_l.add("insert into " + schema_name + ".errormsgs(message) values('Error: Table not found')");
            sql_l.add("insert into " + schema_name + ".errormsgs(message) values('Error: Query was null and constructing query failed' )");
            sql_l.add("insert into " + schema_name + ".errormsgs(message) values('Error: Trying to find strings in a integer only column' )");
            sql_l.add("insert into " + schema_name + ".errormsgs(message) values('Error: Attempting to access restricted content. If yu are a new user you access levell is not set yet."
                    + "Please contact the administrator if it has been more than 24 hours since you registered.')");
            sql_l.add("insert into " + schema_name + ".errormsgs(message) values('Error: invalid column in quert use -avaialble to see what can be searched')");
            sql_l.add("insert into " + schema_name + ".errormsgs(message) values('Error: NA11' )");
            sql_l.add("insert into " + schema_name + ".errormsgs(message) values('Error: NA12' )");
            sql_l.add("insert into " + schema_name + ".errormsgs(message) values('Error: NA13' )");
            sql_l.add("insert into " + schema_name + ".errormsgs(message) values('Error: NA14' )");
            sql_l.add("insert into " + schema_name + ".errormsgs(message) values('Error: NA15' )");
            sql_l.add("insert into " + schema_name + ".errormsgs(message) values('Error: NA16' )");
            try {
                if (!c_conn.isClosed()) {
                    Statement stm = c_conn.createStatement();
                    for (int i = 0; (i < sql_l.size() && result >= 0); i++) {
                        try {
                            result = stm.executeUpdate(sql_l.get(i));
                        } catch (SQLException ex) {
                            result = -1;
                            System.out.println("Error 59b: " + sql_l.get(i) + "\n" + ex.getMessage());
                        }
                    }
                    stm.close();
                }
            } catch (SQLException ex) {
                System.out.println("Error 59g: " + ex.getMessage());
            }

        } else {
            System.out.println("Error 59h: failed to stablish connection");
        }
        return result;
    }
    /*
     keytool -genkey -alias s1as -keyalg RSA  -keypass changeit  -validity 365 -storepass changeit -keystore keystore.jks -dname "cn=egenvar2, ou=NA, o=NA, c=NO";
     keytool -export -alias s1as -file server.cer  -keystore keystore.jks  -storepass changeit  ;
     keytool -import -v -trustcacerts -alias s1as  -file server.cer -keystore cacerts.jks  -keypass changeit
     keytool -genkey -alias glassfish-instance  -keyalg RSA  -keypass changeit  -validity 365 -storepass changeit -keystore keystore.jks -dname "cn=egenvar3, ou=NA, o=NA, c=NO" 
     keytool -export -alias glassfish-instance  -storepass changeit  -file server.cer  -keystore keystore.jks
     keytool -import -v -trustcacerts -alias glassfish-instance  -file server.cer -keystore cacerts.jks  -keypass changeit;
     1. Go to the directory via the .cert file is
     2. openssl s_client  -CApath . -connect smtp.gmail.com:465
     3. Copy and save the lines between (including the beginning and end, otherwise the error:”keytool error: java.lang.Exception: Input not an X.509 certificate”),"-----BEGIN CERTIFICATE-----" and "-----END CERTIFICATE-----" into a file, say, gmail.cert:  
     4.  Delete exsting entry:
     keytool -delete -alias smtp.gmail.com -keystore cacerts.jks -storepass changeit
     5.  keytool -import -alias smtp.gmail.com -keystore cacerts.jks -file gmail.txt
     Importing gmail certificate
     */

    public String createCertificate(String cacsert_file_nm, String host, char[] passward) {
        System.out.println("Attempting to optain and trust certificates from Gmail");
        String result = null;
        String error = null;
        try {
//            System.out.println("javax.net.ssl.keyStore " + System.getProperty("javax.net.ssl.keyStore"));
//            System.out.println("4206 cacsert_file_nm=" + cacsert_file_nm);
            //load keystroe
            KeyStore keyStrore = KeyStore.getInstance("JKS");
            FileInputStream fis;
            fis = new FileInputStream(cacsert_file_nm);
            keyStrore.load(fis, passward);
            fis.close();
//            String alias = "smtp.gmail.com";

            int port = 465;
            X509Certificate[] certs = getGmailCertificates(cacsert_file_nm, host, port);
            if (certs != null) {
                keyStrore.setCertificateEntry(host, certs[0]);
                for (int i = 1; i < certs.length; i++) {
                    keyStrore.setCertificateEntry(host + "_" + i, certs[i]);
                }
            }
            FileOutputStream out = new FileOutputStream(cacsert_file_nm);
            keyStrore.store(out, passward);
            out.close();
            result = cacsert_file_nm;

        } catch (KeyStoreException ex) {
            ProgressMonitor.cancel();
            error = ex.getMessage();

        } catch (NoSuchAlgorithmException ex) {
            ProgressMonitor.cancel();
            error = ex.getMessage();
        } catch (CertificateException ex) {
            ProgressMonitor.cancel();
            error = ex.getMessage();
        } catch (FileNotFoundException ex) {
            ProgressMonitor.cancel();
            error = ex.getMessage();
        } catch (IOException ex) {
            ProgressMonitor.cancel();
            error = ex.getMessage();
        } catch (Exception ex) {
            ProgressMonitor.cancel();
            error = ex.getMessage();
        }
        if (error != null) {
            System.out.println("Error : " + error);
            System.out.println("\n There were some errors and attempting to rectify them by obtaining new certificates \n");
        }
        return result;
    }

    public X509Certificate[] getGmailCertificates(String cacsert_file_nm, String host, int port) {
// Downloaded 1/1/2011 from http://blogs.sun.com/andreas/resource/InstallCert.java

        /*
         * Copyright 2006 Sun Microsystems, Inc.  All Rights Reserved.
         *
         * Redistribution and use in source and binary forms, with or without
         * modification, are permitted provided that the following conditions
         * are met:
         *
         *   - Redistributions of source code must retain the above copyright
         *     notice, this list of conditions and the following disclaimer.
         *
         *   - Redistributions in binary form must reproduce the above copyright
         *     notice, this list of conditions and the following disclaimer in the
         *     documentation and/or other materials provided with the distribution.
         *
         *   - Neither the name of Sun Microsystems nor the names of its
         *     contributors may be used to endorse or promote products derived
         *     from this software without specific prior written permission.
         *
         * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS
         * IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
         * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
         * PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL THE COPYRIGHT OWNER OR
         * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
         * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
         * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
         * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
         * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
         * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
         * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
         */
        String error = null;
        X509Certificate[] cert = null;
        char[] passphrase = "changeit".toCharArray();
        try {
            File file = new File(cacsert_file_nm);
            System.out.println("Loading KeyStore " + file + "...");
            InputStream in = new FileInputStream(file);
            KeyStore ks = KeyStore.getInstance(KeyStore.getDefaultType());
            ks.load(in, passphrase);
            in.close();
            SSLContext context = SSLContext.getInstance("TLS");
            TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            tmf.init(ks);
            System.out.println("Number of trustmanagers (using only the first one) " + tmf.getTrustManagers().length);
            X509TrustManager defaultTrustManager = (X509TrustManager) tmf.getTrustManagers()[0];
            SavingTrustManager tm = new SavingTrustManager(defaultTrustManager);
            context.init(null, new TrustManager[]{tm}, null);
            SSLSocketFactory factory = context.getSocketFactory();

            System.out.println("Opening connection to " + host + ":" + port + "...");
            SSLSocket socket = (SSLSocket) factory.createSocket(host, port);
            socket.setSoTimeout(10000);
            try {
                System.out.println("Starting SSL handshake...");
                socket.startHandshake();
                socket.close();
                System.out.println();
                System.out.println("No errors, certificate is already trusted");
            } catch (SSLException ex) {
                error = ex.getMessage();
            }

            java.security.cert.X509Certificate[] chain = tm.chain;
            if (chain == null) {
                System.out.println("Could not obtain server certificate chain");
//                return certs;
            } else {
                BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
                System.out.println();
                System.out.println("Server sent " + chain.length + " certificate(s):");
                System.out.println();
                cert = chain;
//                String alias = host;
//                ks.setCertificateEntry(alias, cert);
            }
        } catch (SSLException ex) {
            ProgressMonitor.cancel();
            error = ex.getMessage();
        } catch (IOException ex) {
            ProgressMonitor.cancel();
            error = ex.getMessage();
        } catch (Exception ex) {
            ProgressMonitor.cancel();
            error = ex.getMessage();
        }
        if (error != null) {
            System.out.println("Error : " + error);
            System.out.println("\n There were some errors and attempting to rectify them by obtaining new certificates ");
        }
        return cert;

    }

    private static class SavingTrustManager implements X509TrustManager {

        private final X509TrustManager tm;
        private java.security.cert.X509Certificate[] chain;

        SavingTrustManager(X509TrustManager tm) {
            this.tm = tm;
        }

        @Override
        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
            return new X509Certificate[0];
        }

        @Override
        public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType)
                throws CertificateException {
            throw new UnsupportedOperationException();
        }

        @Override
        public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType)
                throws CertificateException {
            this.chain = chain;
            tm.checkServerTrusted(chain, authType);
        }
    }

//    public byte[] getGmailCertificates2(String cacsert_file_nm) {
//        byte[] certs = null;
//        try {
////            File f = new File("/home/sabryr/tmp/server2/egenvar/egenvar/config/keystore.jks");
////            f.delete()
//            char SEP = File.separatorChar;
//            InstallCert.commit("smtp.gmail.com", 465, cacsert_file_nm);
//
//            String host = "smtp.gmail.com";
//            int port = 465;
////            System.out.println("4261 "+HttpsURLConnection.getDefaultAllowUserInteraction()+"  "+HttpsURLConnection.getDefaultHostnameVerifier());    
//
//            SSLSocketFactory factory = HttpsURLConnection.getDefaultSSLSocketFactory();
//            SSLSocket socket = (SSLSocket) factory.createSocket(host, port);
//
//            socket.startHandshake();
//            SSLSession session = socket.getSession();
//            java.security.cert.Certificate[] servercerts = session.getPeerCertificates();
//            certs = new byte[servercerts.length];
//            certs = servercerts[0].getEncoded();
//        } catch (SSLException ex) {
//            ProgressMonitor.cancel();
//            ex.printStackTrace();
//        } catch (IOException ex) {
//            ProgressMonitor.cancel();
//            ex.printStackTrace();
//        } catch (Exception ex) {
//            ProgressMonitor.cancel();
//            ex.printStackTrace();
//        }
//        return certs;
//    }

    /*
     Part of this methid from
     * http://www.java2s.com/Code/Java/Security/EncryptingandDecryptingwiththeJCE.htm
     * //consider asymatric encription
     * or encript it twise
     */
    private void writeResultsToFileEncripted(Object indata, String file_nm) {
        boolean result = false;
        String error = null;
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
                result = true;
            } catch (NoSuchAlgorithmException | InvalidKeyException |
                    InvalidKeySpecException | NoSuchPaddingException ex) {
                error = ex.getMessage();
            } catch (FileNotFoundException ex) {
                error = ex.getMessage();
            } catch (IOException ex) {
                error = ex.getMessage();
            }
        }
        if (!result) {
            System.out.println("Error ST encript : Encription faild: " + error);
        }
    }

    /*
     Part of this methid from
     * http://www.java2s.com/Code/Java/Security/EncryptingandDecryptingwiththeJCE.htm
     */
    private HashMap<String, String> readFromFileEncripted(String file_nm, String enckey) {

        HashMap<String, String> config_map = new HashMap<>();
        if (file_nm != null) {
            try {
                byte key[] = enckey.getBytes();
                DESKeySpec desKeySpec = new DESKeySpec(key);
                SecretKeyFactory keyFactory;
                keyFactory = SecretKeyFactory.getInstance("DES");
                SecretKey secretKey = keyFactory.generateSecret(desKeySpec);
                Cipher desCipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
                desCipher.init(Cipher.DECRYPT_MODE, secretKey);

                // Create stream
                FileInputStream fis = new FileInputStream(file_nm);
                BufferedInputStream bis = new BufferedInputStream(fis);
                CipherInputStream cis = new CipherInputStream(bis, desCipher);
                ObjectInputStream ois = new ObjectInputStream(cis);
                Object tmp_ob = ois.readObject();
                if (tmp_ob != null && tmp_ob instanceof HashMap) {
                    config_map = (HashMap<String, String>) tmp_ob;
                }
            } catch (NoSuchAlgorithmException | InvalidKeyException | InvalidKeySpecException | NoSuchPaddingException | ClassNotFoundException ex) {
                System.out.println("Error: Decrypting file failed, specify correct key in the file key.file." + ex.getMessage());
                System.exit(5371);
            } catch (FileNotFoundException ex) {
                System.out.println("Error: Decrypting file failed, specify correct key in the file key.file. " + ex.getMessage());
                System.exit(5374);
            } catch (IOException ex) {
                System.out.println("Error: Decrypting file failed, specify correct key in the file key.file. " + ex.getMessage());
                System.exit(5346);
            }
        }
        return config_map;
    }

    private void changeEncriptionKey() {
        String classpath = System.getProperty("java.class.path");
        if (classpath != null && !classpath.isEmpty() && classpath.endsWith("jar")) {
            try {
                File path_file = new File(classpath);
                String source_dir = path_file.getAbsoluteFile().getParent() + File.separatorChar;
                String key_file_name = source_dir + "key.file";
                Path kp = Paths.get(key_file_name);
                if (Files.isRegularFile(kp) && Files.isReadable(kp)) {
                    String new_key = null;
                    Charset charset = Charset.forName("UTF-8");
                    try (BufferedReader reader = Files.newBufferedReader(kp, charset)) {
                        int c = 0;
                        String line = null;
                        while ((line = reader.readLine()) != null) {
                            line = line.trim();
                            if (!line.startsWith("#") && line.length() == 8) { //line.matches("[0-9]+") &&
                                new_key = line;
                            }
                        }
                        if (new_key != null) {
                            String c_key = getuserInputSameLine("Enter current encryption key", null);
                            if (Files.exists(Paths.get(LAST_CONFIG_DETAILS_FILE_FLAG))) {
                                HashMap<String, String> config_map = readFromFileEncripted(LAST_CONFIG_DETAILS_FILE_FLAG, c_key);
                                if (config_map != null) {
                                    writeResultsToFileEncripted(config_map, LAST_CONFIG_DETAILS_FILE_FLAG);
                                    System.out.println("Encryption key reset was successful. New key is in " + key_file_name);
                                }

                            } else {
                                System.out.println("Nothing to reset. Start the server and the key provided in the " + key_file_name + " file will be used");
                            }

                        } else {
                            System.out.println("Error: Key file " + key_file_name + " found. But the provided key " + new_key + " not of valid format.\nThe length should be exactly 8. e.g. if you want to use 246, then include 00000246 in the file");
                            System.exit(883);
                        }
                    } catch (IOException x) {
                        System.err.format("IOException: %s%n", x);
                    }
                } else {
                    System.out.println("Error: Key file  not found  " + source_dir + "key.file.\nCreate this file with the new key (integers only, length exactly 8) and try again ");
                }
            } catch (Exception ex) {
                System.out.println("Warning: " + ex.getMessage());
            }
        }
    }

    private int getDecriptKey() {
        int lo = 10000000;
        int hi = 99999999;
        java.util.Random rn = new java.util.Random();
        int cval = 0;
        int n = hi - lo + 1;
        int i = rn.nextInt(n);
        if (i < 0) {
            i = -i;
        }
        cval = lo + i;
//        if (cval == 96 || cval == 59|| cval == 63) {
//            rand(lo, hi);
//        }

        return cval;
    }

    private double checkUpdates() {
        if (updates_map == null) {
            updates_map = new HashMap<>();
        }
        double latest = SERVER_VERSION;
        try {
            if (UPDATE_CHECK_URL != null) {
                URL url = new URL(UPDATE_CHECK_URL);
                Scanner scan = new Scanner(url.openStream());
                while (scan.hasNext()) {
                    String line = scan.nextLine().trim();
                    if (line.matches("[0-9.]+")) {
                        try {
                            latest = new Double(line);
                        } catch (Exception ex) {
                            System.out.println("Check for updates failed. " + ex.getMessage());
                        }
                    } else if (line.startsWith(UPDATE_SERVER_URL)) {
                        updates_map.put(UPDATE_SERVER_URL, line.replace(UPDATE_SERVER_URL, "").trim());
                    } else if (line.startsWith(UPDATE_WEB_URL)) {
                        updates_map.put(UPDATE_WEB_URL, line.replace(UPDATE_WEB_URL, "").trim());
                    } else if (line.startsWith(UPDATE_WEBSERVICE_URL)) {
                        updates_map.put(UPDATE_WEBSERVICE_URL, line.replace(UPDATE_WEBSERVICE_URL, "").trim());
                    } else if (line.startsWith(UPDATE_CMD_URL)) {
                        updates_map.put(UPDATE_CMD_URL, line.replace(UPDATE_CMD_URL, "").trim());
                    } else if (line.startsWith(SYS_MESSAGES)) {
                        System.out.println("Important message from update server " + line.replace(SYS_MESSAGES, ""));
                    }
                }

                scan.close();
            }

        } catch (Exception ex) {
            System.out.println("Check for updates failed. " + ex.getMessage());
        }
        return latest;
    }

    /*
     MethodId=6;
     */
    private String updateSoftware(boolean force) {
        System.out.println("Starting Update.. @ " + Timing.getDateTime());
        boolean error = false;
        double latest_version = checkUpdates();
        if (latest_version < 0) {
            System.out.println("Automatic update failed as the server could not find the correct file. Please update manually");
        } else if (force || latest_version > SERVER_VERSION) {
            if (latest_version > SERVER_VERSION) {
                System.out.println("A newer version is available. Latest version=" + latest_version + ". Installing updates.");
            } else {
                System.out.println("Force upgrading");
            }
            String targetdir = null;
            try {
                String classpath = System.getProperty("java.class.path");
                Path path_p = null;
                if (classpath != null && !classpath.isEmpty() && classpath.endsWith("jar")) {
                    try {
                        path_p = Paths.get(classpath).toAbsolutePath();
                        targetdir = path_p.getParent().toAbsolutePath().toString() + File.separatorChar;
                    } catch (Exception ex) {
                        System.out.println("Warning: " + ex.getMessage());
                    }
                    if (targetdir != null) {
                        if (updates_map != null && !updates_map.isEmpty()) {
                            ArrayList<String> up_key_l = new ArrayList<>(updates_map.keySet());
                            for (int i = 0; i < up_key_l.size(); i++) {
                                String new_link = updates_map.get(up_key_l.get(i));
                                if (up_key_l.get(i).equals(UPDATE_SERVER_URL)) {
                                    if (download(new_link, targetdir + "new_Start_EgenVar1.jar")) {
                                        System.out.println("Download OK, installing started...");
                                        Files.copy(Paths.get(targetdir + "new_Start_EgenVar1.jar"), Paths.get(targetdir + "Start_EgenVar1.jar"), StandardCopyOption.REPLACE_EXISTING);
                                        Files.deleteIfExists(Paths.get(targetdir + "new_Start_EgenVar1.jar"));
                                        System.out.println("New version of server installed, please restart.");
                                    } else {
                                        System.out.println("Error: DownLoad Failed " + new_link);
                                        error = true;
                                    }
                                } else if (up_key_l.get(i).equals(UPDATE_WEB_URL)) {
                                    Path war_path = Paths.get(targetdir + "war");
                                    if (Files.exists(path_p)) {
                                        String war_target = war_path.toString() + File.separatorChar;
                                        if (download(new_link, war_target + "new_eGenVar_web.war")) {
                                            System.out.println("Download OK, installing started...");
                                            Files.copy(Paths.get(war_target + "new_eGenVar_web.war"), Paths.get(war_target + "eGenVar_web.war"), StandardCopyOption.REPLACE_EXISTING);
                                            Files.deleteIfExists(Paths.get(war_target + "new_eGenVar_web.war"));
                                            System.out.println("New version of web interface installed, please restart.");
                                        } else {
                                            System.out.println("Error: DownLoad Failed " + new_link);
                                            error = true;
                                        }
                                    }
                                } else if (up_key_l.get(i).equals(UPDATE_WEBSERVICE_URL)) {
                                    Path war_path = Paths.get(targetdir + "war");
                                    if (Files.exists(path_p)) {
                                        String war_target = war_path.toString() + File.separatorChar;
                                        if (download(new_link, war_target + "new_eGenVar_WebService.war")) {
                                            System.out.println("Download OK, installing started...");
                                            Files.copy(Paths.get(war_target + "new_eGenVar_WebService.war"), Paths.get(war_target + "eGenVar_WebService.war"), StandardCopyOption.REPLACE_EXISTING);
                                            Files.deleteIfExists(Paths.get(war_target + "new_eGenVar_WebService.war"));
                                            System.out.println("New version of web services installed, please restart.");
                                        } else {
                                            System.out.println("Error: DownLoad Failed " + new_link);
                                            error = true;
                                        }
                                    }
                                } else if (up_key_l.get(i).equals(UPDATE_CMD_URL)) {
                                    Path docroot_path = Paths.get(targetdir + "egenvar" + File.separatorChar + "docroot");
                                    if (Files.exists(path_p)) {
                                        String docroot_target = docroot_path.toString() + File.separatorChar;
                                        if (download(new_link, docroot_target + "new_egenv_new.jar")) {
                                            System.out.println("Download OK, installing started...");
                                            Files.copy(Paths.get(docroot_target + "new_egenv_new.jar"), Paths.get(docroot_target + "egenv_new.jar"), StandardCopyOption.REPLACE_EXISTING);
                                            Files.deleteIfExists(Paths.get(docroot_target + "new_egenv_new.jar"));
                                            System.out.println("New version of egenv tool was installed, please restart.");
                                        } else {
                                            System.out.println("Error: DownLoad Failed " + new_link);
                                            error = true;
                                        }
                                    }
                                }
                            }
                        }
                    } else {
                        System.out.println("Automatic update failed as resolving the classpath got complicated. Please update manually");
                    }
                } else {
                    System.out.println("Automatic update failed as resolving the classpath got complicated. Please update manually");
                }
            } catch (Exception ex) {
                System.out.println("Warning 6e: " + ex.getMessage());
                ex.printStackTrace();
            }
        } else {
            System.out.println("Your software is uptodate !. No changes made.");
        }
        if (error) {
            System.out.println("Warning: There were errors, update failed");
        }
        return null;
    }

    public boolean sync_with_central(boolean manual_mode) {
        boolean result = false;
        String sync_loc = instruct_map.get(SOURCE_SYNC_DIR_FLAG);
        if (sync_loc == null) {
            sync_loc = "sync";
        }
        Path sync_path = Paths.get(sync_loc);
        if (!Files.isDirectory(sync_path)) {
            try {
                Files.createDirectories(sync_path);
            } catch (IOException ex) {
                System.out.println("Error : " + ex.getMessage());
            }
        }
        ArrayList<String[]> host_al = new ArrayList<>();
        boolean host_removed = false;
        if (Files.isDirectory(sync_path)) {
            if (manual_mode) {
                String[] hostlist_a = new String[3];
                if (instruct_map.containsKey(SYNC_REMOTE_FLAG)) {
                    hostlist_a = instruct_map.get(SYNC_REMOTE_FLAG).split("\\|\\|");
                }
                String[] option_a = new String[hostlist_a.length + 4];
                for (int i = 0; i < hostlist_a.length; i++) {
                    option_a[i] = hostlist_a[i];
                }
                //TODO Delete tag sopurce
                option_a[option_a.length - 4] = "Sync all";
                option_a[option_a.length - 3] = "Add new host";
                option_a[option_a.length - 2] = "Remove a host";
                option_a[option_a.length - 1] = "Cancel";
                int ans = getUserChoice(option_a, "Select a target host:");
                if (ans == option_a.length - 1) {
                    System.out.println("Exiting, without changing anything");
                } else if (ans == option_a.length - 2) {
                    String[] d_option_a = new String[hostlist_a.length + 3];
                    for (int i = 0; i < hostlist_a.length; i++) {
                        d_option_a[i] = hostlist_a[i];
                    }
                    int del_ans = getUserChoice(d_option_a, "Select a host to delete:");
                    if (del_ans < d_option_a.length - 1) {
                        instruct_map.remove(d_option_a[del_ans]);
                        String current_hosts = instruct_map.get(SYNC_REMOTE_FLAG);
                        current_hosts = current_hosts.replace(d_option_a[del_ans], "");
                        current_hosts = current_hosts.replaceAll("\\|\\|\\|\\|", "||");
                        instruct_map.put(SYNC_REMOTE_FLAG, current_hosts);
                        host_removed = true;
                    }
                } else if (ans == option_a.length - 3) {
                    Console cons = System.console();
                    if (cons != null) {
                        ArrayList<String> option_l = new ArrayList<>();
                        option_l.add("This host(" + instruct_map.get(WSDL_URL_FLAG) + ")");
                        option_l.add("Add a host");
                        ans = getUserChoice(option_l, "Do you want to sync the public search for this host or send data to a remote host ?");
                        String wsdl = null;
                        String user_nm = null;
                        String password = null;
                        if (ans == 0) {
                            wsdl = instruct_map.get(WSDL_URL_FLAG);
                            System.out.println("WSDL: " + wsdl);
                            System.out.println("Provide a administrator level user name :" + wsdl);
                            user_nm = cons.readLine();
                            System.out.println("What is the password :" + user_nm);
                            password = getPassword();
                        } else {
                            System.out.println("What is WSDL URL for the host server :");
                            wsdl = cons.readLine();
                            System.out.println("What is the user name for :" + wsdl);
                            user_nm = cons.readLine();
                            System.out.println("Please enter Password for :" + user_nm);
                            password = getPassword();
                        }
                        String auth_result = call_AuthenticateWebService(wsdl, user_nm, password);
                        if (auth_result != null && auth_result.length() == 32) {
                            String[] host_details_a = new String[3];
                            host_details_a[0] = wsdl;
                            host_details_a[1] = user_nm;
                            host_details_a[2] = password;
                            host_al.add(host_details_a);
                        } else {
                            System.out.println("Error: authentication failed");
                        }
                    }

                } else if (ans == option_a.length - 4) {
                    for (int i = 0; i < hostlist_a.length; i++) {
                        if (instruct_map.containsKey(hostlist_a[i].trim())) {
                            String[] hostDetails_a = instruct_map.get(hostlist_a[i].trim()).split("\\|\\|");
                            if (hostDetails_a.length >= 3) {
                                host_al.add(hostDetails_a);
                            }
                        }
                    }
                } else {
                    String host = option_a[ans];
                    if (instruct_map.containsKey(host)) {
                        host_al.add(instruct_map.get(host).split("\\|\\|"));
                    }
                }

            } else {
                if (instruct_map.containsKey(SYNC_REMOTE_FLAG)) {
                    String[] hostlist_a = instruct_map.get(SYNC_REMOTE_FLAG).split("\\|\\|");
                    for (int i = 0; i < hostlist_a.length; i++) {
                        if (instruct_map.containsKey(hostlist_a[i].trim())) {
                            String[] hostDetails_a = instruct_map.get(hostlist_a[i].trim()).split("\\|\\|");
                            if (hostDetails_a.length >= 3) {
                                host_al.add(hostDetails_a);
//                                System.out.println("6570 " + Arrays.deepToString(hostDetails_a));
                            }
                        }
                    }
                } else {
                    System.out.println("Error: sync not configured, please register atleast one target");
                }

            }

            if (!host_al.isEmpty()) {
                ArrayList<String[]> authentic_host_al = new ArrayList<>();
                for (int j = 0; j < host_al.size(); j++) {
                    String wsdl = host_al.get(j)[0];
                    String user_nm = host_al.get(j)[1];
                    String password = host_al.get(j)[2];
                    String host_name = call_getHostname(wsdl);
                    String auth_result = call_AuthenticateWebService(wsdl, user_nm, password);
                    if (auth_result != null && auth_result.length() == 32) {
                        auth_result = getHash(auth_result);
                        if (host_name == null) {
                            System.out.println("Error: failed to get server name, try again later or contact the administrator");
                        } else {
                            host_al.get(j)[2] = auth_result;
                            String[] authenticated_a = new String[4];
                            authenticated_a[0] = host_al.get(j)[0];
                            authenticated_a[1] = host_al.get(j)[1];
                            authenticated_a[2] = auth_result;
                            authenticated_a[3] = host_name;
                            authentic_host_al.add(authenticated_a);
                            if (instruct_map.containsKey(SYNC_REMOTE_FLAG)) {
                                String[] host_l = instruct_map.get(SYNC_REMOTE_FLAG).split("\\|\\|");
                                String new_host_l = null;
                                boolean found = false;
                                for (int i = 0; i < host_l.length; i++) {
                                    host_l[i] = host_l[i].trim();
                                    if (!host_l[i].isEmpty()) {
                                        if (host_l[i].equalsIgnoreCase(host_name)) {
                                            found = true;
                                        }
                                        if (new_host_l == null) {
                                            new_host_l = host_l[i];
                                        } else {
                                            new_host_l = new_host_l + "||" + host_l[i];
                                        }
                                    }
                                }

                                if (!found) {
                                    if (new_host_l == null) {
                                        new_host_l = host_name;
                                    } else {
                                        new_host_l = new_host_l + "||" + host_name;
                                    }
                                }
                                instruct_map.put(SYNC_REMOTE_FLAG, new_host_l);
                            } else {
                                instruct_map.put(SYNC_REMOTE_FLAG, host_name);
                            }
                            instruct_map.put(host_name, wsdl + "||" + user_nm + "||" + password + "||");
                        }
                    } else {
                        System.out.println("Error: Authentication failed");
                    }
                }
                writeResultsToFileEncripted(instruct_map, LAST_CONFIG_DETAILS_FILE_FLAG);
                if (!authentic_host_al.isEmpty()) {
                    try {
                        if (syc_manager == null) {
                            syc_manager = new Syc_manager(this, authentic_host_al, dbURL_dataEntry, instruct_map,
                                    sync_path, split_limit);
                        }
                        syc_manager.commit();
//                        if (syc_manager == null) {
//                            syc_manager = new Syc_manager(this, authentic_host_al, dbURL_dataEntry, instruct_map,
//                                    sync_path, split_limit);
//                        } else {
//                            Syc_manager.deActivate();
//                            sync_thread.interrupt();
//                            syc_manager = new Syc_manager(this, authentic_host_al, dbURL_dataEntry, instruct_map,
//                                    sync_path, split_limit);
//                        }
//                        sync_thread = new Thread(syc_manager);
//                        sync_thread.setPriority(Thread.MIN_PRIORITY);
//                        sync_thread.start();

                    } catch (Exception ex) {
                        System.out.println("Error : " + ex.getMessage());
                    }
                } else {
                    System.out.println("Error : failed to connect to target or autheication faild");
                }
            } else {
                if (!host_removed) {
                    System.out.println("Not enough details to connect to target.");
                }
                result = false;
            }
        } else {
            System.out.println("Error : creating local sync buffer location failed, try creating this folder manually \n\t" + sync_loc);
        }
        return result;
    }


    /*
     MethidID=sym21
     */
    private boolean authenticate(String wsdl, String username, String password) {
        boolean result = false;
        String auth_result = call_AuthenticateWebService(wsdl, username, password);


        return result;
    }

    /*
     * MethidId=19
     */
    private String call_AuthenticateWebService(String wsdl, String username, String password) {
        password = getHash(password);
        try {
            if (password != null) {
                no.ntnu.medisin.egenvar.web_service.AuthenticateService_Service service = new no.ntnu.medisin.egenvar.web_service.AuthenticateService_Service();
                no.ntnu.medisin.egenvar.web_service.AuthenticateService port = service.getAuthenticateServicePort();
                BindingProvider bp = (BindingProvider) port;
                bp.getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, wsdl);
                return port.authenticate(username, password);
            }
        } catch (Exception ex) {
            System.out.println("Error 19a: Communication failure with sync server-" + wsdl + "\t" + ex.getMessage());
//            ex.printStackTrace();
        }
        return null;
    }

    /*
     * MethidId=23
     */
    private String call_getHostname(String wsdl) {
        String result = null;
        try {
            no.ntnu.medisin.egenvar.web_service.AuthenticateService_Service service = new no.ntnu.medisin.egenvar.web_service.AuthenticateService_Service();
            no.ntnu.medisin.egenvar.web_service.AuthenticateService port = service.getAuthenticateServicePort();
            BindingProvider bp = (BindingProvider) port;
            bp.getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, wsdl);
            return port.getServername(null);
        } catch (Exception ex) {
            System.out.println("Error 23a: Communication failier (Server down or wrong credentails) " + ex.getMessage());
        }
        return null;
    }

    /*
     * MethidId=20
     */
    private String getHash(String intext) {
        try {
            if (intext != null) {
                MessageDigest sha1 = MessageDigest.getInstance("SHA1");
                byte[] pass_bytes = intext.getBytes();
                sha1.reset();
                sha1.update(pass_bytes);
                byte[] pass_digest = sha1.digest();
                Formatter formatter = new Formatter();
                for (byte b : pass_digest) {
                    formatter.format("%02x", b);
                }
                return formatter.toString();
            }
        } catch (NoSuchAlgorithmException ex) {
            System.out.println("Error 20a :" + ex.getMessage());
        }
        return null;
    }

    private boolean download(String in_url, String out_nm) {
        boolean result = false;
        try {
            URL website = new URL(in_url);
            ReadableByteChannel rbc = Channels.newChannel(website.openStream());
            FileOutputStream fos = new FileOutputStream(out_nm);
            fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
            rbc.close();
            fos.close();
            Path test_f = Paths.get(out_nm);
            if (Files.exists(test_f)) {
                result = true;
            }
        } catch (MalformedURLException ex) {
            System.out.println("Error: " + ex.getMessage());
        } catch (IOException ex) {
            System.out.println("Error: " + ex.getMessage());
        }
        return result;
    }

    private void makeJsonTbl(Connection c_conn, String schema_name) {
        try {
            ProgressMonitor p = new ProgressMonitor(30000, "15");
            new Thread(p).start();
            JsonMaker jm = new JsonMaker(dbURL_dataEntry,
                    instruct_map.get(SERVER_DOCROOT_DIR_FLAG),
                    instruct_map.get(S_SERVER_NAME_FLAG),
                    instruct_map.get(S_SERVER_ROOT_FLAG));
            Thread t = new Thread(jm);
            t.start();
            if (jm.isDone()) {
            }
            HashMap<String, String> config_map = new HashMap<>();
            config_map.put(JSON_LAST_UPDATED, Timing.getDateTime());
            if (setConfig(c_conn, config_map, schema_name) < 0) {
            }
            ProgressMonitor.cancel();
        } catch (Exception ex) {
            System.out.println("Error : " + ex.getMessage());
        }

    }

    private void backupcontroller() {
        System.out.println("Backup started @" + Timing.getDateTime());
        Console cons = System.console();
        ArrayList<String> sub_cmd_l = new ArrayList<>();
        sub_cmd_l.add("Backup as temlpate set");
        sub_cmd_l.add("Full backup of database folder as archive");
        sub_cmd_l.add("Cancel");
        int sub_ans = getUserChoice(sub_cmd_l, "Select a function");
        if (sub_ans == 0) {
            System.out.println("Please enter the current encription key:");
            String ck = cons.readLine();
            if (ck.equals(encript_password)) {
//                if (c_conn != null) {
                Path outdir = null; //Paths.get("/home/sabryr/tmp/rand/out");//
                while (outdir == null) {
                    System.out.print("\nOutput directory:");
                    String usein = cons.readLine();
                    if (Files.exists(Paths.get(usein)) && Files.isDirectory(Paths.get(usein))) {
                        outdir = Paths.get(usein);
                    } else {
                        System.out.println("Invalid location");
                    }
                }
                Backup backup = new Backup(instruct_map, outdir, encript_password, null, true, 0);
                (new Thread(backup)).start();
//                } else {
//                    System.out.println("Error: failed to stablish connection");
//                }
            } else {
                System.out.println("Invalid encription key");
            }
        } else if (sub_ans == 1) {
            String current_bk_settings = instruct_map.get(Start_EgenVar1.BAKSUP_INSTRUCT_FLAG);
            System.out.println("do you want to use current settings : " + current_bk_settings + " [Y/N] ?");
            String ck = cons.readLine();
            if (ck.equalsIgnoreCase("Y")) {
                Backup backup = new Backup(instruct_map, null, encript_password, null, true, 1);
                (new Thread(backup)).start();
            } else {
                ArrayList<String> sub1_cmd_l = new ArrayList<>();
                sub1_cmd_l.add("CP");
                sub1_cmd_l.add("SCP");
                sub1_cmd_l.add("SFTP");
                int sub1_ans = getUserChoice(sub1_cmd_l, "Select type");
                if (sub1_ans == 0) {
                    System.out.println("Full path of Directory to place the backup files: ");
                    String out_dir = cons.readLine();
                    if (out_dir != null && !out_dir.isEmpty()) {
                        Path outdir = Paths.get(out_dir);
                        if (Files.exists(outdir) && Files.isWritable(outdir)) {
                            instruct_map.put(Start_EgenVar1.BAKSUP_INSTRUCT_FLAG, sub1_cmd_l.get(sub1_ans) + "|" + outdir);
                            Backup backup = new Backup(instruct_map, outdir, encript_password, null, true, 1);
                            (new Thread(backup)).start();
                            writeResultsToFileEncripted(instruct_map, LAST_CONFIG_DETAILS_FILE_FLAG);
                        } else {
                            System.out.println("Invalid path " + outdir + ". Please create this first");
                        }

                    } else {
                        System.out.println("Invalid location");
                    }

                } else {
                    System.out.println("This is not implemented yet !");
                }
            }
        }
        System.out.println("Backup ended @" + Timing.getDateTime());
    }

    private void getTagTemplates(Connection c_conn, String schema_name) {
//  "id int ,table_name ,column_nm,description ,category ,simplename ,table_description ,search_description ,usageinfo ,showinsearch ,taggable avialable ,access_level 

        String sql = "SELECT table_name ,column_nm FROM " + schema_name + ".tablename2feature WHERE category='Tags'";
    }

    private void getAllTags(Connection c_conn, String schema_name) {
        System.out.println("Collecting tags and indexing .. started ");
        int depric = 0;
        try {
            String all_tag_tbl = "alltags";
            boolean tbl_found = false;
            ArrayList<String> t_l = getCurrentTables(c_conn, schema_name, (25));
            for (int i = 0; (i < t_l.size()); i++) {
                if (t_l.get(i).toUpperCase().endsWith(all_tag_tbl.toUpperCase())) {
                    tbl_found = true;
                    System.out.println("waiting@ 8497 " + tbl_found + "\t" + t_l.get(i).toUpperCase());
                }
                if (!t_l.get(i).toUpperCase().endsWith("_TAGSOURCE")) {
                    t_l.remove(i);
                    i--;
                }
            }
            Statement st = c_conn.createStatement();
            HashMap<String, String> hash2name_m = new HashMap<>();
            HashMap<String, String> hash2tbl_m = new HashMap<>();
            for (int i = 0; i < t_l.size(); i++) {
                String sql = "SELECT hash,name,path FROM " + t_l.get(i);
                ResultSet r = st.executeQuery(sql);
                String tbl_nm = t_l.get(i).substring(t_l.get(i).indexOf(".") + 1);

                while (r.next()) {
                    String path = r.getString(3).toUpperCase();
                    if (path.contains("OBSOLETE") || path.contains("DEPRECATED")) {
                        depric++;
                    } else {
                        if (!hash2name_m.values().contains(r.getString(2))) {
                            hash2name_m.put(r.getString(1), r.getString(2));
                            hash2tbl_m.put(r.getString(1), tbl_nm);
                        }
                    }

                }
                r.close();
            }
            if (tbl_found) {
                st.executeUpdate("DROP TABLE " + schema_name + "." + all_tag_tbl);
            }
            String sql = "CREATE TABLE " + schema_name + "." + all_tag_tbl + "(id int NOT NULL GENERATED ALWAYS AS "
                    + "IDENTITY (START WITH 1, INCREMENT BY 1) PRIMARY KEY,"
                    + "name varchar(512),hash char(40), table_nm varchar(128))";
            if (st.executeUpdate(sql) >= 0) {
                PreparedStatement p_1 = getConnection().prepareStatement(
                        "INSERT INTO " + schema_name + "." + all_tag_tbl + "(hash,name,table_nm) VALUES(?,?,?)");
                Iterator<String> it = hash2name_m.keySet().iterator();

                int cc = 0;
                while (it.hasNext()) {
                    cc++;
                    String c_hash = it.next();
                    p_1.setString(1, c_hash);
                    p_1.setString(2, hash2name_m.get(c_hash));
                    p_1.setString(3, hash2tbl_m.get(c_hash));
                    p_1.addBatch();
                    if (cc > 50000) {
                        p_1.executeBatch();
                        p_1.clearParameters();
                        System.out.print(".");
                        cc = 0;
                    }
                }
                if (cc > 0) {
                    p_1.executeBatch();
                }
                p_1.close();
//                TABLENAME=EGEN_DATAENTRY.report_batch==CREATE INDEX unique_identification6 ON EGEN_DATAENTRY.report_batch(name);
//TABLENAME=EGEN_DATAENTRY.report_batch==ALTER TABLE EGEN_DATAENTRY.report_batch add constraint unique_identification6 UNIQUE(name);
                String sql_indx = "CREATE INDEX all_tag_name_index ON " + schema_name + "." + all_tag_tbl + "(name)";
//                st.executeUpdate(sql_indx);
//                String sql_indx = "ALTER TABLE " + schema_name + "." + all_tag_tbl + " add constraint all_tag_name_index UNIQUE(name)";
                st.executeUpdate(sql_indx);
//                              
            }
            st.close();
        } catch (SQLException ex) {
            System.out.println("8435 " + ex.getMessage());
        }
        System.out.println(" " + depric + " Deprecated terms avoided");

    }

    private String getPassword() {
        String password = "";
        Console cons = System.console();
        if (cons != null) {
            char[] pass = cons.readPassword();
            System.out.println("");
            for (int i = 0; i < pass.length; i++) {
                password = password + pass[i];
            }
        } else {
            password = JOptionPane.showInputDialog("Please enter password for " + instruct_map.get("--mailuser"));
        }
        return password;
    }
}