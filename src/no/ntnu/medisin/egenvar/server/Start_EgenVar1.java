/*
 Sabry Razick
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
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.Arrays;
import java.util.Enumeration;
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
import org.apache.derby.drda.NetworkServerControl;
import org.glassfish.embeddable.CommandResult.ExitStatus;

import org.glassfish.embeddable.Deployer;

/**
 * Select * from Biological_Ontologies into outfile 'Biological_Ontologies'
 * FIELDS TERMINATED BY '||' LINES TERMINATED BY '\n' How to use dns lookup dig
 * +short -x 129.241.180.228 System.out.println("74 " +
 * java.net.InetAddress.getLocalHost().getHostName());
 *
 *
 * 1. Copy JSTL jar to lib folder of application server
 *
 *
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

    private final long MIN_MEM = 67108864;
    private final String UPDATE_CHECK_URL = "http://tang.medisin.ntnu.no/~sabryr/server_version2.txt";
    private final String UPDATE_SERVER_URL = "UPDATE_SERVER=";
    private final String UPDATE_WEB_URL = "UPDATE_WEB=";
    private final String UPDATE_WEBSERVICE_URL = "UPDATE_WEBSER=";
    private final String UPDATE_CMD_URL = "UPDATE_CMD=";
    private final String SYS_MESSAGES = "SYS_MESSAGE=";
    private HashMap<String, String> updates_map;
    private final double SERVER_VERSION = 2.00;
    private final String COMMAND_LINE_TOOL_VERSION = "2.00";
    private final String WEB_INTERFACE_VERSION = "2.00";
    private final String WEB_SERVICE_VERSION = "2.00";
    private final String COMMAND_LINE_TOOL_VERSION_FLAG = "CMD_VERSION";
    private final String WEB_INTERFACE_VERSION_FLAG = "WEB_VERSION";
    private final String WEB_SERVICE_VERSION_FLAG = "WES_VERSION";
    private final String SERVER_VERSION_FLAG = "SERVER_VERSION";
//    private final String COMMAND_LINE_TOOL_VERSION_URL_FLAG = "CMD_VERSION_URL";
    public final static double REQUIRED_JAVA_VERSION = 1.7;
    private static final String GMAIL_CERTIGFICATE_REFRESHER = "gmail_certificate_refresh.sh";
    private static final int SMTP_HOST_PORT = 465;
    public static final int PROGRESS_MONT_INTERVAL = 3000;
    public HashMap<String, String> instruct_map;
    private Connection conn_users = null;
    private Connection conn = null;
    private Connection conn_mysql = null;
    public static final String TAGSOURCE_FLAG = "_tagsource";
    public static final String DATABASE_NAME_USERS_FLAG = "DB_USERS";
    public static final String DATABASE_NAME_DATA_FLAG = "DB_DATAENTRY";
    public static final String SERVER_SECURE_PORT_FLAG = "S_PORT";
    public static String SERVER_PORT_FLAG = "PORT";
    public static String derby_port_flag = "DB_PORT";
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
    public static final String WSDL_URL_FLAG = "WSDL";
    public static final String S_WSDL_URL_FLAG = "S_WSDL";
    public static final String DB_AUTHENTICATION_ALGORITHM_FLAG = "CREATE_DB";
    public static final String DB_AUTHENTICATION_ALGORITHM = "SHA-1";
    public static final String SERVER_IP_FLAG = "IP";
    public static final String DB_LOG_FILENM_FLAG = "DB_LOG";
    public static final String CREATE_DB_FILE_DERBY_FLAG = "FILE_DB_CREATE_DERBY";
    public static final String CREATE_DB_FILE_DERBY_DEFAULT = "data/create_tables_egen_dataEntry_simplified_derby.sql";
    public static final String CREATE_TABLE_FROM_FILE_FLAG = "FILE_SINGLE_TABLE_CREATE";
    public static final String DATASOURCECLASSNAME_FLAG = "DATASOURCECLASSNAME";
    public static final String DRIVERCLASSNAME_FLAG = "DRIVERCLASSNAME";
    public static final String SOURCE_LIB_DIR_FLAG = "SOURCE_LIB";
    public static final String SOURCE_DATA_DIR_FLAG = "SOURCE_DATA";
    public static final String SOURCE_WAR_DIR_FLAG = "SOURCE_WAR";
    public static final String SERVER_LIB_DIR_FLAG = "SERVER__LIB";
    public static final String SERVER_DATA_DIR_FLAG = "SERVER_DATA";
    public static final String SERVER_CONF_DIR_FLAG = "SERVER_CONFIG";
    public static final String SERVER_DOCROOT_DIR_FLAG = "SERVER_DOCROOT";
    public static final String SERVER_BASE_FLAG = "SERVER_BASE";
    public static final String SERVER_METRO_DIR = "SERVER_METRO";
    public static final String CMD_TAR_NAME = "egenv.tar.gz";
    public static final String CMD_ZIP_NAME = "egenv.zip";
    public static final String CMD_JAR_NAME = "egenv_new.jar";
    public static final String PROCEDURE_JAR_NAME = "Procedures_eGenv_JAVADB.jar";
    public static final String CMD_TAR_LOC_FLAG = "CMD_TAR";
    public static final String CMD_ZIP_LOC_FLAG = "CMD_ZIP";
    public static final String CMD_JAR_LOC_FLAG = "CMD_JAR";
    public static final String PROCEDURE_JAr_LOC_FLAG = "PRC_JAR";
    public static final String USE_LOCAL_HOST_FOR_MAIL = "USE_LOCALHOST";
    private final String SERVER_DOC_ROOT_FLAG = "DOC_ROOT";
    private String dbURL_users = null;
    private String dbURL_dataEntry = null;
    private String TRUST_STORE_FILE_NM = "TRUST_STORE";
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
    public final String TABLE_TO_USE_FLAG = "TABLETOUSE_";
    public static final String QUERY_TO_USE_FLAG = "QUERYTOUSE_";
    private HashMap<String, ArrayList<String>> table2Columns_map;
    private final String LINK_TO_FEATURE_ID_FLAG = "LINK_TO_FEATURE_ID";
    private String encript_password = "12345678";//12303321
    private final static String LAST_CONFIG_DETAILS_FILE_FLAG = ".last_config.enc";

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Timing.setPointer();
        //TpDo: create a unique encription key getDecriptKey()
        boolean forcelocalhost = false;
        boolean reset_forcelocalhost = false;
        Start_EgenVar1 main = new Start_EgenVar1();
        ArrayList<String> arg_l = new ArrayList<>(1);
        if (args != null) {
            arg_l = new ArrayList<>(Arrays.asList(args));
        }
        boolean verbose = false;
        boolean no_mail = false;
        boolean withdefault = false;
        String config_file_loc = null;
        String log_level = null;
        boolean use_default = false;
        boolean find_free_port = false;
//        boolean securemail = false;
        boolean force = false;
        boolean restserver = false;
        //LAST_CONFIG_DETAILS_FILE_FLAG
        if (arg_l.remove("-nomail")) {
            no_mail = true;
        }
        if (arg_l.remove("-localhost")) {
            forcelocalhost = true;
        }
        if (arg_l.remove("-resethost")) {
            restserver = true;
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
        if (arg_l.remove("-update") || arg_l.remove("-upgrade")) {
            main.updateSoftware(force);
        } else if (arg_l.remove("-h") || arg_l.remove("-help")) {
            System.out.println("-d:use defaults\n"
                    + "-nomail: configure email but do not test it\n"
                    + "-v: verbose mode\n"
                    + "-recover: password recovery"
                    + "-version: get software versions"
                    + "-stop:stop the server");

        } else if (arg_l.remove("-test")) {
            main.mailTest(forcelocalhost, reset_forcelocalhost, find_free_port, restserver);
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
                if (inst.equals(main.encript_password)) {
                    HashMap<String, String> config_map = main.readFromFileEncripted(LAST_CONFIG_DETAILS_FILE_FLAG);
                    ArrayList<String> conf_l = new ArrayList<>(config_map.keySet());
                    for (int i = 0; i < conf_l.size(); i++) {
                        System.out.println(conf_l.get(i) + "=" + config_map.get(conf_l.get(i)));
                    }
                } else {
                    System.out.println("Key wrong, (The key was sent to the adminstrators email during instalation)");
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
                if (main.init(config_file_loc, true, verbose, no_mail, forcelocalhost, reset_forcelocalhost, find_free_port, restserver)) {
                    main.commit(log_level, verbose, true, forcelocalhost);
                    ProgressMonitor.cancel();
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

            if (main.init(config_file_loc, false, verbose, no_mail, forcelocalhost, reset_forcelocalhost, find_free_port, restserver)) {
                main.commit(log_level, verbose, no_mail, forcelocalhost);
                ProgressMonitor.cancel();
            }

        }
    }

    private boolean status(GlassFish glassfish, NetworkServerControl db_server) {

        boolean result = false;
        boolean completed = false;
        System.out.println("______________________\n\negenvar server started ports used " + SERVER_SECURE_PORT_FLAG + "=" + instruct_map.get(SERVER_SECURE_PORT_FLAG) + "|" + SERVER_PORT_FLAG + "=" + instruct_map.get(SERVER_PORT_FLAG) + "|" + derby_port_flag + "=" + instruct_map.get(derby_port_flag) + ""
                + "\n\nWeb interface:\n\t" + instruct_map.get(S_SERVER_NAME_FLAG) + ""
                + "\nWSDL for webservices:\n\t" + instruct_map.get(WSDL_URL_FLAG) + ""
                + "\nTo get the egenv tool:\n\twget  " + instruct_map.get(CMD_TAR_LOC_FLAG) + ""
                + "\nto stop the server from other terminals:\n\tStart_EgenVar -stop " + ManagementFactory.getRuntimeMXBean().getName() + "\n");

        try {
            System.out.println(glassfish.getStatus());
        } catch (GlassFishException ex) {
            ex.printStackTrace();
        } //
        try {
            if (conn != null && !conn.isClosed()) {
                conn.close();
            }
            if (conn_users != null && !conn_users.isClosed()) {
                conn_users.close();
            }
        } catch (Exception rx) {
        }

        ProgressMonitor.cancel();
        double latest_version = checkUpdates();
        if (SERVER_VERSION < latest_version) {
            System.out.println("\n\n Updates are avaialalbe. Your version=" + SERVER_VERSION + "| Latest version=" + latest_version + " \nVisit http://bigr.medisin.ntnu.no/data/eGenVar/ for more details ");
        }
        System.out.println("Time taken: " + Timing.convert(Timing.getFromlastPointer()));
        Timing.setPointer();
        while (!completed) {
            int upgradeDecideder = rand(1, 3);
            if (upgradeDecideder == 2) {
                latest_version = checkUpdates();
                if (SERVER_VERSION < latest_version) {
                    System.out.println("\n\n Updates are avaialalbe. Your version=" + SERVER_VERSION + "| Latest version=" + latest_version + " \nVisit http://bigr.medisin.ntnu.no/data/eGenVar/ for more details ");
                }
            }

            Console cons = System.console();
            if (cons != null) {
//                System.out.print("\n(STOP -to stop server|D -get database status|A -get app server status| I -get status information| T -get uptime| N -add new user):");
                System.out.print("\n(STOP -to stop server| M -more commands):");

                String inst = cons.readLine();
                if (inst.equalsIgnoreCase("stop")) {
                    System.out.println("Stoping, please wait ...");
                    try {
                        glassfish.stop();
                        db_server.shutdown();
                        completed = true;
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                } else if (inst.equalsIgnoreCase("m")) {
                    try {
                        ArrayList<String> cmd_l = new ArrayList<>(10);
                        cmd_l.add("Stop server");//0
                        cmd_l.add("Get uptime");//1
                        cmd_l.add("Get database status");//2
                        cmd_l.add("Get application server status");//3
                        cmd_l.add("Get egenvar status");//4
                        cmd_l.add("Display user details");//5
                        cmd_l.add("Add a new user");//6
                        cmd_l.add("Set access level for users");//7
                        cmd_l.add("Block a user");//8
                        cmd_l.add("Delete a user");//9                    
                        cmd_l.add("Drop table (Warning! this action cannot be undone");//10
                        cmd_l.add("Re-diploy services");//11
                        cmd_l.add("Direct database queries (001:select query, 002:update query, 003:index check)");//12                   
                        cmd_l.add("Version");//13
                        cmd_l.add("asadmin");//14
                        cmd_l.add("Exit this menu");//15

                        int ans = getUserChoice(cmd_l, "Select a function");
                        switch (ans) {
                            case 0: {
                                System.out.println("Stoping, please wait ...");
                                try {
                                    glassfish.stop();
                                    db_server.shutdown();
                                    completed = true;
                                } catch (Exception ex) {
                                    ex.printStackTrace();
                                }
                            }
                            break;
                            case 4: {
                                try {
                                    System.out.println(db_server.getRuntimeInfo());
                                } catch (Exception ex) {
                                    ex.printStackTrace();
                                }
                            }
                            break;
                            case 3: {
                                try {
                                    System.out.println(glassfish.getStatus());
                                } catch (GlassFishException ex) {
                                    ex.printStackTrace();
                                }
                            }
                            break;
                            case 2: {
                                try {
                                    System.out.println("Oracle JAVA DB (Apache Derby)\n" + db_server.getSysinfo());
                                } catch (Exception ex) {
                                    ex.printStackTrace();
                                }

                                System.out.println("______________________\negenvar server started ports used " + SERVER_SECURE_PORT_FLAG + "=" + instruct_map.get(SERVER_SECURE_PORT_FLAG) + "|" + SERVER_PORT_FLAG + "=" + instruct_map.get(SERVER_PORT_FLAG) + "|" + derby_port_flag + "=" + instruct_map.get(derby_port_flag) + ""
                                        + "\n\nWeb interface: " + instruct_map.get(S_SERVER_NAME_FLAG) + ""
                                        + "\nWSDL for webservices: " + instruct_map.get(WSDL_URL_FLAG) + "\n"
                                        + "\nUp time " + Timing.convert(Timing.getFromlastPointer()) + "\n");
                                try {
                                    System.out.println("Server glassfish embedded " + glassfish.getStatus());
                                } catch (GlassFishException ex) {
                                    ex.printStackTrace();
                                }
                            }
                            break;
                            case 1: {
                                System.out.println(Timing.convert(Timing.getFromlastPointer()));
                            }
                            break;
                            case 6: {
                                int user_creat = createUser();
                                if (user_creat >= 0) {
                                    System.out.println("User account created");
                                } else {
                                    System.out.println("Error: User account creation failed");
                                }
                            }
                            break;
                            case 7: {
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
                            }
                            break;
                            case 8: {
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

                            }
                            break;
                            case 9: {
                                System.out.print("\nList of username(emails) of the user (seperated by a ,) to delete:");
                                String email = cons.readLine();
                                if (email != null && !email.isEmpty()) {
                                    if (deleteUserLevel(email)) {
                                        System.out.println("Level updated for " + email);
                                    } else {
                                        System.out.println("Error: update failed");
                                    }
                                } else {
                                    System.out.println("Invalid email");
                                }
                            }
                            break;
                            case 5: {
                                System.out.print("\nList of user name (emails) of the user/s or * to get all (seperated by a ,):");
                                String email = cons.readLine();
                                if (email != null && !email.isEmpty()) {
                                    System.out.print("\nLevel (0-9):");
                                    displayUserDetails(email);
                                } else {
                                    System.out.println("Invalid email");
                                }
                            }
                            break;
                            case 10: {
                                if (droptable() >= 0) {
                                    System.out.println("Table droped");
                                } else {
                                    System.out.println("Nothing changed");
                                }
                            }
                            break;
                            case 11: {
                                System.out.println("Re-diploying initiating");//    
                                diploy_services(glassfish);
                            }
                            break;
                            case 12: {
                                System.out.println("Test code:");
                                String query = cons.readLine();
                                if (query.equals("001")) {
                                    query = cons.readLine();
                                    ArrayList<String> re_l = executeQuery(query);
                                    for (int i = 0; i < re_l.size(); i++) {
                                        System.out.println("521 " + re_l.get(i));
                                    }
                                } else if (query.equals("002")) {
                                    query = cons.readLine();
                                    System.out.println("Result=" + executeUpdate(query));
                                } else if (query.equals("003")) {
                                    query = cons.readLine();
                                    print_indices(query);

                                } else {
                                    System.out.println("No tests for this code");
                                }

                            }
                            break;
                            case 13: {
                                System.out.println("Server version= " + SERVER_VERSION);
                                System.out.println("Command line tool (egenv)= " + COMMAND_LINE_TOOL_VERSION);
                                System.out.println("Web interface (eGenVar_web)= " + WEB_INTERFACE_VERSION);
                                System.out.println("Web services= " + WEB_SERVICE_VERSION);
                            }
                            break;
                            case 14: {
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

                            }
                            break;
                            case 15: {
                                System.out.println("Exit");//              
                            }
                            break;
                            default: {
                            }
                            break;
                        }//
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                } else {
                    System.out.println("______________________\negenvar server started ports used " + SERVER_SECURE_PORT_FLAG + "=" + instruct_map.get(SERVER_SECURE_PORT_FLAG) + "|" + SERVER_PORT_FLAG + "=" + instruct_map.get(SERVER_PORT_FLAG) + "|" + derby_port_flag + "=" + instruct_map.get(derby_port_flag) + ""
                            + "\n\nWeb interface: " + instruct_map.get(SERVER_NAME_FLAG) + ""
                            + "\nWSDL for webservices: " + instruct_map.get(WSDL_URL_FLAG) + "\n"
                            + "\nUp time " + Timing.convert(Timing.getFromlastPointer()) + "\n");
                }
            }
        }
        System.out.println("All services stopped");
        return result;
    }

    private void mailTest(boolean forcelocalhost, boolean reset_forcelocalhost, boolean find_free_port, boolean restserver) {
        if (init(null, false, true, false, forcelocalhost, reset_forcelocalhost, find_free_port, restserver)) {
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
            System.out.println("538 failed");
        }

    }

    private void init_map(boolean forcelocalhost) {
        instruct_map = new HashMap<>();
        instruct_map.put(SERVER_BASE_FLAG, "localhost");
        instruct_map.put(SERVER_SECURE_PORT_FLAG, "8185");
        instruct_map.put(SERVER_PORT_FLAG, "8085");
        instruct_map.put(derby_port_flag, "1557");
        instruct_map.put(unm_usermanage_flag, "usermanage");
        instruct_map.put(unm_usermanage_pass_flag, randomstring(10));
        instruct_map.put(unm_gendataentry_flag, "egendataentry");
        instruct_map.put(unm_gendataentry_pass_flag, randomstring(10));
        instruct_map.put(unm_gendataview_flag, "egendataview");
        instruct_map.put(unm_gendataUpdate_pass_flag, randomstring(10));
        instruct_map.put(unm_gendataUpdate_flag, "egendataupdate");
        instruct_map.put(unm_gendataview_pass_flag, randomstring(10));
        instruct_map.put(db_host_flag, "derby://localhost");
        instruct_map.put(DATABASE_NAME_USERS_FLAG, "EGEN_USERS");
        instruct_map.put(DATABASE_NAME_DATA_FLAG, "EGEN_DATAENTRY");
        instruct_map.put(DB_LOG_FILENM_FLAG, "db_log.txt");
        instruct_map.put(CREATE_DB_FILE_DERBY_FLAG, CREATE_DB_FILE_DERBY_DEFAULT);
        instruct_map.put(DATASOURCECLASSNAME_FLAG, "org.apache.derby.jdbc.ClientDataSource");
        instruct_map.put(DRIVERCLASSNAME_FLAG, "org.apache.derby.jdbc.ClientDriver");
        instruct_map.put(DB_AUTHENTICATION_ALGORITHM_FLAG, DB_AUTHENTICATION_ALGORITHM);

        instruct_map.putAll(getNetInfo(forcelocalhost));
        instruct_map.put("--fromaddress", null);
        instruct_map.put("--mailuser", null);
        instruct_map.put("--mailhost", "smtp.gmail.com");
        instruct_map.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        instruct_map.put("mail.smtp.password", null);
        instruct_map.put("mail.smtp.auth", "true");
        instruct_map.put("mail.smtp.socketFactory.fallback", "false");
//        instruct_map.put("mail.smtp.port", "587");
//        instruct_map.put("mail.smtp.socketFactory.port", "587");
        instruct_map.put("mail.smtp.port", SMTP_HOST_PORT + "");
        instruct_map.put("mail.smtp.socketFactory.port", SMTP_HOST_PORT + "");
        instruct_map.put("mail.transport.protocol", "smtps");
        instruct_map.put("mail.smtps.host", "smtp.gmail.com");
        instruct_map.put("mail.smtps.auth", "true");
        instruct_map.put("USE_LOCAL_HOST_FOR_MAIL", null);

        instruct_map.put(TRUST_STORE_FILE_NM, null);
        instruct_map.put(CMD_TAR_LOC_FLAG, null);
        instruct_map.put(CMD_ZIP_LOC_FLAG, null);
        instruct_map.put(CMD_JAR_LOC_FLAG, null);
        instruct_map.put(PROCEDURE_JAr_LOC_FLAG, null);
        instruct_map.put(SERVER_DOC_ROOT_FLAG, "http://localhost:8085/eGenVar_web/");


        instruct_map.put(COMMAND_LINE_TOOL_VERSION_FLAG, COMMAND_LINE_TOOL_VERSION);
        instruct_map.put(WEB_INTERFACE_VERSION_FLAG, WEB_INTERFACE_VERSION);
        instruct_map.put(WEB_SERVICE_VERSION_FLAG, WEB_SERVICE_VERSION);

//        instruct_map.put(DERBY_CONNECTION_PARAM, "territory=en_US;collation=TERRITORY_BASED");
        instruct_map.put(DERBY_CONNECTION_PARAM, "territory=en_US;collation=TERRITORY_BASED:PRIMARY");
        instruct_map.put(SERVER_INSTALATION_ROOT_FLAG, null);
        instruct_map.put(SRC_ROOT_FLAG, null);

    }

    /*
     MethodID=2
     */
    private boolean init(String config_file_nm, boolean fromlast, boolean verbose, boolean nomail, boolean forcelocalhost,
            boolean reset_forcelocalhost, boolean find_free_port, boolean restserver) {
        (new Thread(new ProgressMonitor(PROGRESS_MONT_INTERVAL, "15"))).start();


        if (!fromlast) {
            init_map(forcelocalhost);
        } else {
            System.out.println("Loading pre-configured parameters from previous session.");
            instruct_map = readFromFileEncripted(LAST_CONFIG_DETAILS_FILE_FLAG);
            if (restserver) {
                instruct_map.putAll(getNetInfo(forcelocalhost));
            }
            if (verbose) {
                ArrayList<String> conf_l = new ArrayList<>(instruct_map.keySet());
                for (int i = 0; i < conf_l.size(); i++) {
                    System.out.println(conf_l.get(i) + " => " + instruct_map.get(conf_l.get(i)));
                }
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
                        instruct_map.put(SOURCE_LIB_DIR_FLAG, instruct_map.get(SRC_ROOT_FLAG) + "lib" + File.separatorChar);

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
                                    }

                                    File serv_docroot = new File(instruct_map.get(SERVER_DOCROOT_DIR_FLAG));
                                    if (!serv_docroot.isDirectory()) {
                                        serv_docroot.mkdirs();
                                    }
                                    if (serv_docroot.isDirectory() && serv_docroot.canWrite()) {
                                        if (init_port(config_file_nm, find_free_port)) {
                                            dbURL_users = "jdbc:" + instruct_map.get(db_host_flag) + ":" + instruct_map.get(derby_port_flag) + "/" + instruct_map.get(DATABASE_NAME_USERS_FLAG) + ";create=true;user=" + instruct_map.get(unm_usermanage_flag) + ";password=" + instruct_map.get(unm_usermanage_pass_flag);
                                            dbURL_dataEntry = "jdbc:" + instruct_map.get(db_host_flag) + ":" + instruct_map.get(derby_port_flag) + "/" + instruct_map.get(DATABASE_NAME_DATA_FLAG) + ";create=true;" + instruct_map.get(DERBY_CONNECTION_PARAM) + ";user=" + instruct_map.get(unm_gendataentry_flag) + ";password=" + instruct_map.get(unm_gendataentry_pass_flag);
//                                            Path src_lib_dir = Paths.get(instruct_map.get(SOURCE_LIB_DIR_FLAG));
//                                            File[] src_lib_a = ;
                                            try {
                                                DirectoryStream<Path> ds = Files.newDirectoryStream(FileSystems.getDefault().getPath(instruct_map.get(SOURCE_LIB_DIR_FLAG)));
                                                Iterator<Path> path_l = ds.iterator();
                                                while (path_l.hasNext()) {
                                                    Path c_path = path_l.next();
                                                    Files.copy(c_path, Paths.get((instruct_map.get(SERVER_LIB_DIR_FLAG) + c_path.getFileName())), StandardCopyOption.REPLACE_EXISTING);
                                                }
                                                ds.close();
                                            } catch (IOException e) {
                                                e.printStackTrace();
                                            }

//                                            for (int i = 0; i < src_lib_a.length; i++) {
//                                                try {
//                                                    if (src_lib_a[i].isFile() && src_lib_a[i].getName().endsWith("jar")) {
////                                                        Files.copy(Paths.get(src_lib_a[i]), Paths.get(src_lib_a[i]),StandardCopyOption.REPLACE_EXISTING);
//                                                        Files.copy(Paths.get(src_lib_a[i]), Paths.get((instruct_map.get(SERVER_LIB_DIR_FLAG) + src_lib_a[i].getName())), StandardCopyOption.REPLACE_EXISTING);
//                                                    }
//                                                } catch (IOException ex) {
//                                                    ex.printStackTrace();
//                                                }
//                                            }
                                            boolean cmd_found = false;

//                                            boolean keystore_found = false;
//                                            boolean server_cer_found = false;
//                                            boolean cacerts_jks_found = false;

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
                                                }
                                            }
                                            //                                         
//                                            try {
//                                                FileWriter out = new FileWriter(instruct_map.get(SERVER_DOCROOT_DIR_FLAG) + "egenv_version.txt", false);
//                                                out.append(instruct_map.get(COMMAND_LINE_TOOL_VERSION_FLAG));
//                                                out.close();
//                                            } catch (IOException e) {
//                                                e.printStackTrace();
//                                            }
                                            File[] config_ents_a = serv_cof.listFiles();

                                            for (int i = 0; i < config_ents_a.length; i++) {
                                                if (config_ents_a[i].getName().equals("keystore.jks")) {
                                                    keystore_file = config_ents_a[i].getAbsolutePath();
                                                } else if (config_ents_a[i].getName().equals("cacerts.jks")) {
                                                    cacerts_jks_file = config_ents_a[i].getAbsolutePath();
                                                } else if (config_ents_a[i].getName().equals("server.cer")) {
                                                    server_cer_file = config_ents_a[i].getAbsolutePath();
                                                }
                                            }
                                            boolean recreating_mail_cert = false;
                                            try {
                                                DirectoryStream<Path> ds = Files.newDirectoryStream(FileSystems.getDefault().getPath(instruct_map.get(SOURCE_LIB_DIR_FLAG)));
                                                Iterator<Path> path_l = ds.iterator();
                                                File new_Cacert_keyStrore_file = null;

                                                while (path_l.hasNext()) {
                                                    Path c_path = path_l.next();
                                                    String c_name = c_path.getFileName().toString();
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
                                                            System.out.println("1040 copying " + c_name);
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
                                                        }
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
                                                        System.out.println(printformater_terminal("\nThe eGenVar system is setting up a email account. We recommend to use a gmail (Google) account for this. If you do not have one please create before proceeding (https://accounts.google.com/SignUp?service=mail) You could manually provide configuration for other email accounts in " + instruct_map.get(CONFIG_FILE_FLAG) + " file. However,  default settings will only work with a gmail account\n", 100));
                                                        String ans1 = getuserInputSameLine("Do you want to use either Gmail or mail configuration provided in the config file?", "[Y/N]");
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
                                                                System.out.println("Please use a config file to sepecify mail acounts other than gmail. Refer the advanced section of the admin manual for more details");
                                                            }
                                                        }
                                                    }
                                                    if (instruct_map.get("--fromaddress") == null || instruct_map.get("--fromaddress").isEmpty() || !instruct_map.get("--fromaddress").matches("[A-z0-9_\\-\\.]+@[A-z0-9_\\-]+[\\.]{1}[A-z0-9_\\-\\.]+")) {
                                                        boolean ok = false;
                                                        while (!ok) {
                                                            String ans2 = getuserInputSameLine("Email address (C -skip this)", "");
                                                            if (analyseUserResponse(ans2, true, true, null) == 5) {
                                                                ok = true;
                                                                System.exit(1);
                                                            } else if (analyseUserResponse(ans2, true, true, "[A-z0-9_\\-\\.]+@[A-z0-9_\\-]+[\\.]{1}[A-z0-9_\\-\\.]+") == 1) {
                                                                ok = true;
                                                                instruct_map.put("--fromaddress", ans2);
                                                            }
                                                        }

                                                    }
                                                    if (instruct_map.get("--mailuser") == null || instruct_map.get("--mailuser").isEmpty()) {
                                                        boolean ok = false;
                                                        while (!ok) {
                                                            String ans = getuserInputSameLine("User name for " + instruct_map.get("--fromaddress") + "(C -skip this)", "");
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
                if (find_free_port) {
                    int new_port = findFreePOrt(8180, 8189);
                    if (new_port > 0) {
                        System.out.println("The port " + instruct_map.get(SERVER_SECURE_PORT_FLAG) + " was not available, using " + new_port + " instead");
                        instruct_map.put(SERVER_SECURE_PORT_FLAG, new_port + "");
                    } else {
                        System.out.println("Error: failed to find a free port in the range 8180 - 8189, please free a port in this range and try again");
                        allports_ok = false;
                    }
                } else {
                    System.out.println(printformater_terminal("Error 5a:" + instruct_map.get(SERVER_SECURE_PORT_FLAG) + error_msg, 80) + "\n Use -find_free_port to scan for free ports");
                    allports_ok = false;
                }

            }
        } else {
            System.out.println("Invalid port " + instruct_map.get(SERVER_SECURE_PORT_FLAG) + ". Valid ports for " + SERVER_SECURE_PORT_FLAG + " from 8180 to 8189 Correct this in " + config_file_nm);
        }
        if (instruct_map.get(SERVER_PORT_FLAG).matches("808[0-9]{1}")) {
            if (isPortAvailable(new Integer(instruct_map.get(SERVER_PORT_FLAG)))) {
                allports_ok = true;
                System.out.println("Port " + instruct_map.get(SERVER_PORT_FLAG) + " -- free.");
            } else {
                if (find_free_port) {
                    int new_port = findFreePOrt(8080, 8089);
                    if (new_port > 0) {
                        System.out.println("The port " + instruct_map.get(SERVER_PORT_FLAG) + " was not available, using " + new_port + " instead");
                        instruct_map.put(SERVER_PORT_FLAG, new_port + "");
                    } else {
                        System.out.println("Error: failed to find a free port in the range 8080 - 8089, please free a port in this range and try again");
                        allports_ok = false;
                    }
                } else {
                    System.out.println(printformater_terminal("Error 5b:" + instruct_map.get((SERVER_PORT_FLAG)) + error_msg, 80) + "\n Use -find_free_port to scan for free ports");
                    allports_ok = false;
                }
            }
        } else {
            System.out.println("Invalid port " + instruct_map.get(SERVER_PORT_FLAG) + ". Valid ports for " + SERVER_PORT_FLAG + " from 8080 to 8089 Correct this in " + config_file_nm);
        }
        if (instruct_map.get(derby_port_flag).matches("155[0-9]{1}")) {
            if (isPortAvailable(new Integer(instruct_map.get(derby_port_flag)))) {
                allports_ok = true;
                System.out.println("Port " + instruct_map.get(derby_port_flag) + " -- free.");
            } else {
                if (find_free_port) {
                    int new_port = findFreePOrt(1550, 1559);
                    if (new_port > 0) {
                        System.out.println("The port " + instruct_map.get(derby_port_flag) + " was not available, using " + new_port + " instead");
                        instruct_map.put(derby_port_flag, new_port + "");
                    } else {
                        System.out.println("Error: failed to find a free port in the range 1550 - 1559, please free a port in this range and try again");
                        allports_ok = false;
                    }
                } else {
                    System.out.println(printformater_terminal("Error 5c:" + instruct_map.get(derby_port_flag) + error_msg, 80) + "\n Use -find_free_port to scan for free ports");
                    allports_ok = false;
                }
            }
        } else {
            System.out.println("Invalid port " + instruct_map.get(derby_port_flag) + ". Valid ports for " + derby_port_flag + " from 1550 to 1559 Correct this in " + config_file_nm);
        }
        return allports_ok;
    }

    /*
     *  MethodID=1
     */
    private void commit(String loglevel_nm, boolean verbose, boolean no_mail, boolean forcelocalhost) {
        long mem = Runtime.getRuntime().freeMemory();
        if (mem < MIN_MEM) {
            System.out.println("Error: available memory too low (requires atleast " + (MIN_MEM / (1048576)) + ")");
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
            System.out.println("Starting database server");
            mem = Runtime.getRuntime().freeMemory();
            if (mem < MIN_MEM) {
                System.out.println("Error: available memory too low (requires atleast " + (MIN_MEM / (1048576)) + ")");
            }
            if (no_mail || test_mail(uselocalhostformail)) {
                NetworkServerControl db_server = startDerby(instruct_map.get(DB_LOG_FILENM_FLAG));
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
                            System.out.println("Error: available memory too low (requires atleast " + (MIN_MEM / (1048576)) + ")");
                        }
                        (new Thread(new ProgressMonitor(PROGRESS_MONT_INTERVAL, "15"))).start();
                        if (createTablesFromFile(instruct_map.get(CREATE_DB_FILE_DERBY_FLAG), verbose) >= 0) {
                            System.out.println("Checking  integrity");
                            ArrayList<String> result_l = executeQuery("select distinct table_name from  " + instruct_map.get(DATABASE_NAME_DATA_FLAG) + ".tablename2feature where showinsearch=1");
                            if (!result_l.isEmpty()) {
                                ProgressMonitor.cancel();
                                if (instruct_map.containsKey(SOURCE_DATA_DIR_FLAG) && instruct_map.get(SOURCE_DATA_DIR_FLAG) != null && !instruct_map.get(SOURCE_DATA_DIR_FLAG).isEmpty()) {
                                    String data_folder_nm = instruct_map.get(SOURCE_DATA_DIR_FLAG);
                                    File data_folder = new File(data_folder_nm);
                                    if (data_folder.isDirectory() && data_folder.canRead()) {
                                        File[] data_file_a = data_folder.listFiles();
                                        for (int i = 0; i < data_file_a.length; i++) {
                                            if (data_file_a[i].isFile() && data_file_a[i].canRead()) {
                                                String data_file_nm = data_file_a[i].getName();
                                                if (data_file_nm.contains(TAGSOURCE_FLAG)) {
                                                    crateTagFromFile(data_file_a[i]);
                                                } else {
                                                    (new Thread(new ProgressMonitor(10000, "15"))).start();
                                                    addDataFromFile(data_file_a[i], 0);
                                                    ProgressMonitor.cancel();
                                                }
                                            }
                                        }
                                    }
                                    boolean table_ok = index_tagsources();
                                    if (table_ok) {
                                        System.out.println("Indexing tag sources was a success");
                                    } else {
                                        System.out.println("\nError: Indexing tag sources filed");
                                    }
                                }
                                System.out.println("Refresh available tables result=" + refreshTableTOFeatures());
                                HashMap<String, String> acouunt_map = getFirstUseraccount(forcelocalhost);
                                mem = Runtime.getRuntime().freeMemory();
                                if (mem < MIN_MEM) {
                                    System.out.println("Error: available memory too low (requires atleast " + (MIN_MEM / (1048576)) + ")");
                                }

                                if (acouunt_map != null) {
                                    (new Thread(new ProgressMonitor(PROGRESS_MONT_INTERVAL, "15"))).start();
                                    if (create_user_connections_derby(glassfish)) {
                                        System.out.println("User management activated");
                                        if (create_dataEntry_connections_derby(glassfish)) {
                                            System.out.println("Dataentry connection acitvated");
                                            HashMap<String, String> config_map = new HashMap<>();
                                            config_map.put(SERVER_DOCROOT_DIR_FLAG, instruct_map.get(SERVER_DOCROOT_DIR_FLAG));
                                            config_map.put(SERVER_ROOT_FLAG, instruct_map.get(SERVER_ROOT_FLAG));
                                            config_map.put(SERVER_NAME_FLAG, instruct_map.get(SERVER_NAME_FLAG));
                                            config_map.put(S_SERVER_ROOT_FLAG, instruct_map.get(S_SERVER_ROOT_FLAG));
                                            config_map.put(S_SERVER_NAME_FLAG, instruct_map.get(S_SERVER_NAME_FLAG));
                                            config_map.put(WSDL_URL_FLAG, instruct_map.get(WSDL_URL_FLAG));
                                            config_map.put(S_WSDL_URL_FLAG, instruct_map.get(S_WSDL_URL_FLAG));
                                            config_map.put(WSDL_URL_FLAG, instruct_map.get(WSDL_URL_FLAG));
                                            config_map.put(S_WSDL_URL_FLAG, instruct_map.get(S_WSDL_URL_FLAG));
                                            config_map.put(CMD_TAR_LOC_FLAG, instruct_map.get(CMD_TAR_LOC_FLAG));
                                            config_map.put(CMD_ZIP_LOC_FLAG, instruct_map.get(CMD_ZIP_LOC_FLAG));
                                            config_map.put(CMD_JAR_LOC_FLAG, instruct_map.get(CMD_JAR_LOC_FLAG));
                                            config_map.put(SERVER_DOC_ROOT_FLAG, instruct_map.get(SERVER_DOC_ROOT_FLAG));
                                            config_map.put(SERVER_BASE_FLAG, instruct_map.get(SERVER_BASE_FLAG));

                                            config_map.put(COMMAND_LINE_TOOL_VERSION_FLAG, COMMAND_LINE_TOOL_VERSION);
                                            config_map.put(WEB_INTERFACE_VERSION_FLAG, WEB_INTERFACE_VERSION);
                                            config_map.put(WEB_SERVICE_VERSION_FLAG, WEB_SERVICE_VERSION);
//                                            config_map.put(COMMAND_LINE_TOOL_VERSION_URL_FLAG, instruct_map.get(COMMAND_LINE_TOOL_VERSION_URL_FLAG));

                                            if (setConfig(config_map) < 0) {
                                                System.out.println("Error 2e: Failed to save configuration information");
                                            } else {
                                                System.out.println("Configurations saved");
//                                            result_l = executeQuery("select name,param_value from " + instruct_map.get(DATABASE_NAME_DATA_FLAG) + ".config" + "");
//                                            for (int i = 0; i < result_l.size(); i++) {
//                                                System.out.println("869 " + result_l.get(i));
//
//                                            }
                                            }
                                            /* 
                                             Place a welcome and redict page here.
                                             File web = new File("/home/sabryr/NetBeansProjects/eGenVar_web/dist/eGenVar_web.war");
                                             deployer.deploy(web, "--name=eGenVar_web", "--contextroot=/", "--force=true");//eGenVar_web                              
                                             **/
                                            mem = Runtime.getRuntime().freeMemory();
                                            if (mem < MIN_MEM) {
                                                System.out.println("Error: available memory too low (requires atleast " + (MIN_MEM / (1048576)) + ")");
                                            }
                                            diploy_services(glassfish);
//                                            if (instruct_map.containsKey(SOURCE_WAR_DIR_FLAG)) {
//                                                Deployer deployer = glassfish.getDeployer();
//                                                String war_dir_nm = instruct_map.get(SOURCE_WAR_DIR_FLAG);
//                                                File war_dir = new File(war_dir_nm);
//                                                if (war_dir.isDirectory() && war_dir.canRead()) {
//                                                    System.out.println("Starting the diployment of web applications");
//                                                    File[] war_a = war_dir.listFiles();
//                                                    for (int i = 0; i < war_a.length; i++) {
//                                                        File file = war_a[i];
//                                                        System.out.println("Diploying " + file.getAbsolutePath());
//                                                        String[] c_det = file.getName().split("\\.");
//                                                        if (c_det.length > 1 && c_det[c_det.length - 1].equalsIgnoreCase("war")) {
//                                                            File web = new File(file.getAbsolutePath());
//                                                            deployer.deploy(web, "--name=" + c_det[0], "--contextroot=" + c_det[0], "--force=true");
//                                                        }
//                                                    }
//                                                }
//                                            }

//                                        File web = new File("/home/sabryr/NetBeansProjects/eGenVar_web/dist/eGenVar_web.war");
//                                        deployer.deploy(web, "--name=eGenVar_web", "--contextroot=eGenVar_web", "--force=true");
//                                        File wab_service = new File("/home/sabryr/NetBeansProjects/eGenVar_WebService/dist/eGenVar_WebService.war");
//                                        deployer.deploy(wab_service, "--name=eGenVar_WebService", "--contextroot=eGenVar_WebService", "--force=true");
                                        }
                                    }

                                    createQueryExpansion();

                                    mem = Runtime.getRuntime().freeMemory();
                                    if (mem < MIN_MEM) {
                                        System.out.println("Error: available memory too low (requires atleast " + (MIN_MEM / (1048576)) + ")");
                                    }
                                    String mail_subject = "egenvar admin account information";
                                    String mail_message = "Instalation on " + instruct_map.get(SERVER_ROOT_FLAG) + " was successful\nPlease keep the following configuration details for future reference"
                                            + "\n";
                                    mail_message = mail_message + "\nEncription key:" + encript_password + ". This is very important!."
                                            + " You will require this key to recover configuration options including passwords. If you loose this, there is NO way to recover passwords. (everything else below can be recoverd usiing this))\n\n";

                                    ArrayList<String> config_key_l = new ArrayList<String>(instruct_map.keySet());
                                    for (int i = 0; i < config_key_l.size(); i++) {
                                        if (!config_key_l.get(i).equalsIgnoreCase("mail.smtp.password")) {
                                            mail_message = mail_message + "\n" + config_key_l.get(i) + "=" + instruct_map.get(config_key_l.get(i));
                                        } else {
                                        }
                                    }

                                    mail_message = mail_message + "\n\n The web interface is accessible from :" + instruct_map.get(S_SERVER_NAME_FLAG);
                                    mail_message = mail_message + "\n The WSDL for the web service :" + instruct_map.get(WSDL_URL_FLAG);

                                    if (no_mail) {
                                        writeResultsToFileEncripted(instruct_map, LAST_CONFIG_DETAILS_FILE_FLAG);
                                        status(glassfish, db_server);
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
                                            System.out.println("Error: available memory too low (requires atleast " + (MIN_MEM / (1048576)) + ")");
                                        }
                                        status(glassfish, db_server);
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
                                System.out.println("Error 1a: Accessing user accounts failed, Check the config file and run the process again");
                            }
                        } else {
                            System.out.println("Error 1b: Creating tables failed, Check the config file " + instruct_map.get(CREATE_DB_FILE_DERBY_FLAG) + " for errors");
                            stopDerby(new Integer(instruct_map.get(derby_port_flag)));
                            glassfish.stop();
                        }
                    } else {
                        System.out.println("Error 1b: failed to create or access user accounts");
                        try {
                            db_server.shutdown();
                            glassfish.stop();
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
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

    private boolean diploy_services(GlassFish glassfish) {
        try {
            if (instruct_map.containsKey(SOURCE_WAR_DIR_FLAG)) {
                Deployer deployer = glassfish.getDeployer();
                String war_dir_nm = instruct_map.get(SOURCE_WAR_DIR_FLAG);
                File war_dir = new File(war_dir_nm);
                if (war_dir.isDirectory() && war_dir.canRead()) {
                    System.out.println("Starting the diployment of web applications");
                    File[] war_a = war_dir.listFiles();
                    for (int i = 0; i < war_a.length; i++) {
                        File file = war_a[i];
                        System.out.println("Diploying " + file.getAbsolutePath());
                        String[] c_det = file.getName().split("\\.");
                        if (c_det.length > 1 && c_det[c_det.length - 1].equalsIgnoreCase("war")) {
                            File web = new File(file.getAbsolutePath());
                            try {
                                deployer.deploy(web, "--name=" + c_det[0], "--contextroot=" + c_det[0], "--force=true");
                            } catch (Exception ex) {
                                System.out.println("Error : " + ex.getMessage() + " \n Server restart required");
                            }

                        }
                    }
                }
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
        System.out.println("Attempting to send mail ...");

        if (sendEmail(uselocalhostformail, instruct_map.get("--mailhost"),
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
    private NetworkServerControl startDerby(String dblogfilenm) {
        NetworkServerControl db_server = null;
        try {
            System.out.println("Database server port=" + instruct_map.get(derby_port_flag));

            db_server = new NetworkServerControl(InetAddress.getByName("localhost"), new Integer(instruct_map.get(derby_port_flag)));

            PrintWriter log = new PrintWriter(new File(dblogfilenm));
            db_server.start(log);
//            Properties p = db_server.getCurrentProperties();
//            ArrayList<Object> tmp_l = new ArrayList<>(p.keySet());
//            for (int i = 0; i < tmp_l.size(); i++) {
//                System.out.println("1362 " + tmp_l.get(i) + "=>" + p.getProperty(tmp_l.get(i).toString()));
//
//            }
//            //derby.storage.pageCacheSize
//            optimizeDB();
//            System.exit(1);
        } catch (Exception ex) {
            System.out.println("Error 7a:" + ex.getMessage() + ". Will rectify this later");
            ex.printStackTrace();
        }
        try {
            db_server.trace(true);
        } catch (Exception ex) {
            System.out.println("Error 7b: Warning setting trace failed. Will rectify this later");
        }
        return db_server;
    }

    private void optimizeDB() {
        try {
            System.out.println("1387");
            createConnection();
            Statement s = conn.createStatement();
//            s.executeUpdate("CALL SYSCS_UTIL.SYSCS_SET_DATABASE_PROPERTY('derby.storage.pageCacheSize', '100')");
//            ResultSet r =s.executeQuery("values SYSCS_UTIL.SYSCS_GET_DATABASE_PROPERTY('derby.storage.pageCacheSize')");
            s.executeUpdate("CALL SYSCS_UTIL.SYSCS_SET_RUNTIMESTATISTICS(1)");
            ResultSet r_2 = s.executeQuery("Select * from EGEN_DATAENTRY.files where name like '%genrated_3110_22%'");
            while (r_2.next()) {
                String sh = r_2.getString(2);
                System.out.println("1396 " + sh);
            }
            ResultSet r = s.executeQuery("VALUES SYSCS_UTIL.SYSCS_GET_RUNTIMESTATISTICS()");
            while (r.next()) {
                System.out.println("1362 " + r.getString(1));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
    /*
     MethodID=
     */

    private int droptable() {
        int result = -1;
        try {
            if (createConnection()) {
                ArrayList<String> table_l = getCurrentTables();
                table_l.add("Cancel (exit without making any changes)");
                int ans = getUserChoice(table_l, "Select the table to drop");
                System.out.println("1349 " + ans);
                if (ans == table_l.size() - 1) {
                } else {
                    String table = table_l.get(ans);
                    result = conn.createStatement().executeUpdate("Drop table " + table);
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

    private int setConfig(HashMap<String, String> instruct_map) {
        int result = 0;

        try {
            if (createConnection()) {
                PreparedStatement p_1 = conn.prepareStatement("update " + get_correct_table_name("config") + " set param_value=? where name=?");

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
    /*
     MethodID=6
     */

    private boolean setAuthentication(HashMap<String, String> instruct_map) {
        boolean result = false;
        try {
            if (createConnection_usermanage()) {
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

            if (result && createConnection()) {
                Statement datacontrol_stmt = conn.createStatement();
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
            createTable_users(create_sql);
        }
        if (!table_l.contains("GROUPS")) {
            String create2_sql = "CREATE TABLE " + instruct_map.get(DATABASE_NAME_USERS_FLAG) + ".groups (id int NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1)  PRIMARY KEY"
                    + ",gid int DEFAULT 9999"
                    + ",groupname varchar(256) DEFAULT NULL"
                    + ",username varchar(256) DEFAULT NULL"
                    + ",uid int DEFAULT 9999"
                    + ")";
            createTable_users(create2_sql);
        }
        if (!table_l.contains("GROUPNMTOGID")) {
            String create3_sql = "CREATE TABLE " + instruct_map.get(DATABASE_NAME_USERS_FLAG) + ".groupnmtoGID( id int NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1)  PRIMARY KEY, "
                    + "gid int , "
                    + "groupname varchar(512), "
                    + "constraint unique_identification2 unique(groupname),"
                    + "constraint unique_id UNIQUE(gid))";
            ArrayList<HashMap<String, String>> data_map_l = new ArrayList<HashMap<String, String>>();
            createTable_users(create3_sql);
            HashMap<String, String> groupnmtoGID_data_map = new HashMap<String, String>();
            groupnmtoGID_data_map.put("groupname", "Admin");
            groupnmtoGID_data_map.put("gid", "1000");
            data_map_l.clear();
            data_map_l.add(groupnmtoGID_data_map);
            insertData(instruct_map.get(DATABASE_NAME_USERS_FLAG) + ".groupnmtoGID", data_map_l);

            groupnmtoGID_data_map.clear();
            groupnmtoGID_data_map.put("groupname", "Uploader");
            groupnmtoGID_data_map.put("gid", "2000");
            data_map_l.clear();
            data_map_l.add(groupnmtoGID_data_map);
            insertData(instruct_map.get(DATABASE_NAME_USERS_FLAG) + ".groupnmtoGID", data_map_l);

            groupnmtoGID_data_map.clear();
            groupnmtoGID_data_map.put("groupname", "Editor");
            groupnmtoGID_data_map.put("gid", "2001");
            data_map_l.clear();
            data_map_l.add(groupnmtoGID_data_map);
            insertData(instruct_map.get(DATABASE_NAME_USERS_FLAG) + ".groupnmtoGID", data_map_l);

            groupnmtoGID_data_map.clear();
            groupnmtoGID_data_map.put("groupname", "Deletor");
            groupnmtoGID_data_map.put("gid", "2003");
            data_map_l.clear();
            data_map_l.add(groupnmtoGID_data_map);
            insertData(instruct_map.get(DATABASE_NAME_USERS_FLAG) + ".groupnmtoGID", data_map_l);

            groupnmtoGID_data_map.clear();
            groupnmtoGID_data_map.put("groupname", "Search");
            groupnmtoGID_data_map.put("gid", "300");
            data_map_l.clear();
            data_map_l.add(groupnmtoGID_data_map);
            insertData(instruct_map.get(DATABASE_NAME_USERS_FLAG) + ".groupnmtoGID", data_map_l);
        }

        if (!table_l.contains("SMS")) {
            String create2_sql = "CREATE TABLE " + instruct_map.get(DATABASE_NAME_USERS_FLAG) + ".sms (id int NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1)  PRIMARY KEY"
                    + ",username varchar(256)"
                    + ",password varchar(256)"
                    + ")";
            createTable_users(create2_sql);
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
            if (createConnection_usermanage()) {
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
            if (createConnection_usermanage()) {
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
        if (createConnection_usermanage()) {
            try {
                CallableStatement cs2 = null;
                CallableStatement cs3 = null;
                String query1 = "CALL sqlj.install_jar('" + instruct_map.get(PROCEDURE_JAr_LOC_FLAG) + "','" + instruct_map.get(DATABASE_NAME_USERS_FLAG) + ".Procedures_eGenv_JAVADB',0)";
                String query2 = "CALL sqlj.replace_jar('" + instruct_map.get(PROCEDURE_JAr_LOC_FLAG) + "','" + instruct_map.get(DATABASE_NAME_USERS_FLAG) + ".Procedures_eGenv_JAVADB')";
                try {
                    cs2 = conn_users.prepareCall(query1);
                    cs2.execute();
                } catch (SQLException e2) {
                    System.out.println("982 " + e2.getMessage());
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
    private int createTablesFromFile(String filename, boolean verbose) {
        int result = 0;
        ArrayList<String> table_l = getCurrentTables();
        ArrayList<String> found_tables_l = new ArrayList<String>(table_l.size());
        try {
            File file = new File(filename);
            Scanner scan = new Scanner(file);
            while (scan.hasNext() && result >= 0) {
                String line = scan.nextLine().trim();
                if (!line.isEmpty() && line.length() > 12) {
                    if (line.startsWith("TABLENAME=") && line.contains("==")) {
                        String table_nm = line.split("==")[0].replace("TABLENAME=", "").trim();
                        line = line.split("==")[1].trim();
                        line = line.replace(";", "");
                        if (verbose) {
                            System.out.print("Precessing. Table=" + table_nm + "| " + line.substring(0, 15));
                        }
                        if (!table_l.contains(table_nm.toUpperCase()) && !line.startsWith("DROP")) {
                            try {
                                result = createTable(line);
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
                                    result = createTable(line);
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
        return result;
    }

    private HashMap<String, String> getFirstUseraccount(boolean forcelocalhost) {
        HashMap<String, String> acouunt_map = null;
        if (createConnection_usermanage()) {
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
                    if (addData("insert into " + get_correct_table_name("person") + " (name, lastnm, email) values('" + acouunt_map.get("First_name") + "','" + acouunt_map.get("Last_name") + "','" + acouunt_map.get("email") + "')") >= 0) {
                        System.out.println("New user created");
                    } else {
                        System.out.println("\n\nError :Creating new user profile failed \n\n");
                    }
                }
            } else {
                acouunt_map = new HashMap<>();
            }
        }


        return acouunt_map;
    }

    private boolean setUserLevel(String level, String email) {
        boolean result = false;
        email = email.replaceAll(",", "','");
        email = "'" + email + "'";
        String sql = "update " + instruct_map.get(DATABASE_NAME_USERS_FLAG) + ".useraccounts set level=" + level + " where EMAIL in (" + email + ")";
        if (createTable_users(sql) >= 0) {
            result = true;
        }
        return result;
    }

    private boolean deleteUserLevel(String email) {
        boolean result = false;
        email = email.replaceAll(",", "','");
        email = "'" + email + "'";
        String sql = "delete from  " + instruct_map.get(DATABASE_NAME_USERS_FLAG) + ".useraccounts where EMAIL in (" + email + ")";
        if (createTable_users(sql) >= 0) {
            result = true;
        }
        return result;
    }

    private boolean blockUser(String email) {
        boolean result = false;
        email = email.replaceAll(",", "','");
        email = "'" + email + "'";
        String sql = "update " + instruct_map.get(DATABASE_NAME_USERS_FLAG) + ".useraccounts set username='" + randomstring(10) + "' where EMAIL in (" + email + ")";
        if (createTable_users(sql) >= 0) {
            result = true;
        }
        return result;
    }

    private boolean displayUserDetails(String email) {
        boolean result = false;
        String sql = null;
        if (email.equals("*")) {
            sql = "Select * from  " + instruct_map.get(DATABASE_NAME_USERS_FLAG) + ".useraccounts";
        } else {
            email = email.replaceAll(",", "','");
            email = "'" + email + "'";
            sql = "Select * from  " + instruct_map.get(DATABASE_NAME_USERS_FLAG) + ".useraccounts where username in (" + email + ")";
        }
        ArrayList<String> result_l = executeQuery_user(sql);
        if (result_l == null || result_l.isEmpty()) {
            System.out.println("user not found");
        } else {
            for (int i = 0; i < result_l.size(); i++) {
                String[] c_user = result_l.get(i).split("\\|\\|");
                System.out.println(i);
                for (int j = 0; j < c_user.length; j++) {
                    System.out.print(c_user[j] + "\t");
                }
                System.out.println("______________");
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
            addData("insert into " + get_correct_table_name("person") + " (name, lastnm, email) values('" + acouunt_map.get("First_name") + "','" + acouunt_map.get("Last_name") + "','" + acouunt_map.get("email") + "')");
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

    private void crateTagFromFile(File in_file) {
        System.out.println("Adding from " + in_file.getAbsolutePath() + " started @" + Timing.getDateTime());
        String splitter = "\\|\\|";
        if (createConnection()) {
            String file_nm = in_file.getName().split("\\.")[0];
            if (file_nm.length() > 64) {
                file_nm = file_nm.substring(0, 63);
            }
            String table_nm = get_correct_table_name(file_nm);
//            String table_nm_for_indices = file_nm;
            boolean table_created = false;
            boolean table_found = false;
            if (table_nm == null) {
                table_nm = instruct_map.get(DATABASE_NAME_DATA_FLAG) + "." + file_nm;
            } else {
                table_found = true;
            }
            String sql_create = "create table " + table_nm + " (";
            String sql_insert = "insert into " + table_nm + " (";
            String sql_insert_param = "(";
            int result = 0;


            String c_table = null;
//            ArrayList<String> table_l = getCurrentTables();
            PreparedStatement p_1 = null;
            int col_count = 0;
            int rec_count = 0;
//            boolean insert_active = false;
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
                    if (!conn.isClosed()) {
                        int id_pos = -1;
                        int name_pos = -0;
                        int parent_id_pos = 0;
                        int last_completion = 0;
                        boolean skip_table = false;
//                        HashMap<String, String> test_map = new HashMap<String, String>();
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
                                        conn.createStatement().execute("drop table " + table_nm);
                                        table_found = false;
                                    }
                                } else {
                                    if (table_found) {
                                        System.out.println("Table found " + table_nm + " so no action taken as a recreate was not requested");
                                        skip_table = true;
                                    } else {
                                        String[] split = line.split(splitter);
                                        String seperator = "";
                                        for (int i = 0; i < split.length; i++) {
                                            if (i > 0) {
                                                seperator = ",";
                                            }
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
                                                sql_create = sql_create + seperator + split[i] + " varchar(2048) ";
                                                sql_insert = sql_insert + seperator + split[i];
                                                sql_insert_param = sql_insert_param + seperator + "?";
                                            }
                                        }
                                        if (name_pos >= 0 && parent_id_pos >= 0 && id_pos >= 0) {
                                            sql_create = sql_create + ")";
                                            conn.createStatement().execute(sql_create);
                                            sql_insert_param = sql_insert_param + ")";
                                            sql_insert = sql_insert + ")";
                                            sql_insert = sql_insert + " values" + sql_insert_param;
                                            p_1 = conn.prepareStatement(sql_insert);
                                            col_count = p_1.getParameterMetaData().getParameterCount();
                                            table_found = true;
                                            table_created = true;
                                        } else {
                                            System.out.println("Error @" + rec_count + ": For " + table_nm + ".\nMandatory columns id,name and parent_id missing for " + table_nm + " (name column=" + name_pos + " parent_id column=" + parent_id_pos + " id column=" + id_pos + ")");
                                            skip_table = true;
                                        }
                                    }
                                }
                            } else {
                                if (table_found && p_1 != null) {
                                    String[] split = line.trim().split(splitter);
                                    if (split.length == col_count) {
                                        for (int i = 0; (i < split.length); i++) {
                                            if (split[i].length() > 1023) {
                                                split[i] = split[i].substring(0, 1023);
                                                System.out.println("@" + rec_count + ") Data truncation " + split[i].substring(0, 20) + " ....max_allowed=2048");
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
                                        System.out.println("Error @" + rec_count + ": For " + table_nm + ". Data column count and heading column count mismatch for " + line);
                                        conn.createStatement().execute("drop table " + table_nm);
                                        skip_table = true;
                                    }
                                } else {
                                    System.out.println("Table " + table_nm + " not found and creation failed");
                                    skip_table = true;
                                }
                            }
                        }
                        System.out.println("Checking integrity of " + table_nm);
                        String test = "Select  name,parent_id, count(*) as cc  from " + table_nm + " group by name, parent_id ";
                        ResultSet r_1 = conn.createStatement().executeQuery(test);
//                        int recc = 0;
                        boolean conflict_found = false;
                        while (r_1.next()) {
//                            recc++;
                            int cc = r_1.getInt("cc");
                            if (cc > 1) {
                                conflict_found = true;
                                System.out.println("Error possible duplicate key pair " + r_1.getString("name") + "  " + r_1.getInt("parent_id") + "  " + r_1.getInt("cc") + " "
                                        + "\nplease correct this before adding. If it is not an duplicate, try removing  all non alphabetical character");
                            }
                        }
                        scan.close();
                        String post_porc_sql = "";

                        try {
                            if (conflict_found) {
                                System.out.println(" Removing table " + table_nm + ". Due to conflict in keys");
                                conn.createStatement().executeUpdate("DROP TABLE " + table_nm);
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
                }
            } catch (FileNotFoundException ex) {
                ProgressMonitor.cancel();
                System.out.println("Error 58c: " + ex.getMessage());
            }
            try {
                if (conn != null && !conn.isClosed()) {
                    conn.close();
                }
            } catch (SQLException ex) {
                ProgressMonitor.cancel();
                System.out.println("Error 68h: " + ex.getMessage());
            }
        } else {
            System.out.println("Error 58o: failed to stablish connection");
        }
    }
    /*
     MethodID=59
     */

    private boolean index_tagsources() {
        boolean table_ok = false;
        try {
            if (createConnection()) {
                ArrayList<String> table_l = getCurrentTables();
                for (int i = 0; i < table_l.size(); i++) {
                    try {
                        String table_nm = get_correct_table_name(table_l.get(i));
                        if (table_nm != null) {
                            boolean key_ref_found = false;
                            boolean parent_id_ref_found = false;
                            boolean parent_id_constraint_found = false;
                            boolean unique_identification_found = false;
                            boolean unique_identification_contraints_found = false;
                            String[] fr_keynm_a = null;
                            String[] keynm_a = null;
                            HashMap<String, String[]> constraints_map = get_key_contraints(table_nm);
                            if (constraints_map.containsKey(FOREIGN_KEY_NAMES_FLAG)) {
                                fr_keynm_a = constraints_map.get(FOREIGN_KEY_NAMES_FLAG);
                                for (int j = 0; j < fr_keynm_a.length; j++) {
                                    String c_key = fr_keynm_a[j];
                                    if (c_key.toLowerCase().startsWith("parent_id_ref")) {
                                        parent_id_ref_found = true;
                                    }
                                }
                            }
                            if (constraints_map.containsKey(ALL_INDEX_NAMES_FLAG)) {
                                keynm_a = constraints_map.get(ALL_INDEX_NAMES_FLAG);
                                for (int j = 0; j < keynm_a.length; j++) {
                                    String c_key = keynm_a[j];
                                    if (c_key.toLowerCase().startsWith("key_ref_")) {
                                        key_ref_found = true;
                                    } else if (c_key.toLowerCase().startsWith("unique_identification_")) {
                                        unique_identification_found = true;
                                        unique_identification_contraints_found = true;
                                    } else if (c_key.toLowerCase().startsWith("parent_id_ref")) {
                                        parent_id_constraint_found = true;
                                    }
                                }
                            }
                            String table_nm_for_indices = table_nm.split("\\.")[table_nm.split("\\.").length - 1];
                            if (table_nm_for_indices.toLowerCase().endsWith(TAGSOURCE_FLAG)) {
                                int result = 0;
                                if (!key_ref_found) {
                                    String post_porc_sql = "CREATE INDEX  key_ref_" + table_nm_for_indices + " ON " + table_nm + "(id)";
                                    result = conn.createStatement().executeUpdate(post_porc_sql);
                                    try {
                                        post_porc_sql = "ALTER TABLE " + table_nm + " add CONSTRAINT key_ref_" + table_nm_for_indices + " UNIQUE(id)";
                                        result = conn.createStatement().executeUpdate(post_porc_sql);
                                    } catch (SQLException ex) {
                                        System.out.println("Error 60h: " + ex.getMessage());
                                    }

                                }
                                if (result >= 0) {
                                    if (!parent_id_ref_found) {
                                        System.out.println("key_ref_" + table_nm_for_indices + "... OK");
                                        String post_porc_sql = "CREATE INDEX parent_id_ref_" + table_nm_for_indices + " ON " + table_nm + "(parent_id)";
                                        result = conn.createStatement().executeUpdate(post_porc_sql);
                                        if (result >= 0) {
                                            System.out.println("index  parent_id_ref_" + table_nm_for_indices + "... OK");
                                        }
                                    }
                                    if (!parent_id_constraint_found) {
                                        if (result >= 0) {
                                            String post_porc_sql = "ALTER TABLE " + table_nm + " add CONSTRAINT parent_id_ref_" + table_nm_for_indices + "  FOREIGN KEY (parent_id) REFERENCES " + table_nm + "(id)";
                                            result = conn.createStatement().executeUpdate(post_porc_sql);
                                            if (result >= 0) {
                                                System.out.println("Constaint parent_id_ref_" + table_nm_for_indices + " ... OK");
                                            }
                                        } else {
                                            System.out.println("Error 59a: could not add constaint 1, this table may not be usable");
                                        }
                                    }
                                    if (result >= 0) {
                                        if (!unique_identification_found) {
                                            String post_porc_sql = "CREATE INDEX unique_identification_" + table_nm_for_indices + " ON " + table_nm + "(name,parent_id)";
                                            result = conn.createStatement().executeUpdate(post_porc_sql);
                                            if (result >= 0) {
                                                System.out.println("unique_identification_" + table_nm_for_indices + " ... OK");
                                            }
                                        }

                                        if (result >= 0) {
                                            if (!unique_identification_contraints_found) {
                                                String post_porc_sql = "ALTER table " + table_nm + " add CONSTRAINT unique_identification_" + table_nm_for_indices + " UNIQUE(name,parent_id)";
                                                result = conn.createStatement().executeUpdate(post_porc_sql);
                                                if (result >= 0) {
                                                    table_ok = true;
                                                    System.out.println("Constraint unique_identification_" + table_nm_for_indices + " ... OK");
                                                } else {
                                                    System.out.println("Error 59b: could not add constraint2, this table may not be usable");
                                                }
                                            } else {
                                                table_ok = true;
                                            }
                                        } else {
                                            System.out.println("Error 59c: could not add constaint 1, this table may not be usable");
                                        }
                                    } else {
                                        System.out.println("Error 49d: could not add unique identification, this table may not be usable");
                                    }

                                } else {
                                    System.out.println("Error 59e: could not add primary key, this table may not be usable");
                                }
                                if (!table_ok) {
                                    System.out.println(" Removing table " + table_nm + ". There were errors");
                                    conn.createStatement().executeUpdate("DROP TABLE " + table_nm);
                                } else {
                                    System.out.println("Indexing table " + table_nm + " ... OK");
                                }
                            }
                        } else {
                            System.out.println("Erorr: table null ");
                        }
                    } catch (SQLException ex) {
                        ProgressMonitor.cancel();
                        System.out.println("Error 58h: " + ex.getMessage());
                    }
                }
                refreshTableTOFeatures();
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

    private void addDataFromFile(File in_file, int dbms) {
        System.out.println("Adding from " + in_file.getAbsolutePath() + " started @" + Timing.getDateTime());
        if (createConnection()) {
            String splitter = "\\|\\|";
            Connection new_con = conn;
            String c_table = null;
            ArrayList<String> table_l = getCurrentTables();
            try {
                Scanner scan = new Scanner(in_file, "ISO-8859-1");
                int result = 0;
                createConnection();
                try {
                    int line_count = 0;
                    while (scan.hasNext()) {
                        line_count++;
                        scan.nextLine();
                    }
                    scan.close();
                    scan = new Scanner(in_file, "ISO-8859-1");
                    if (!new_con.isClosed()) {
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
                                    String table_name_in_db = get_correct_table_name(c_table);
                                    if (table_name_in_db == null || !table_l.contains(table_name_in_db)) {
                                        skip_table = false;
                                    } else {
                                        skip_table = true;
                                        c_table = table_name_in_db;
                                    }
                                } else if (command.startsWith("INSERT")) {
                                    System.out.println("236 " + command);
                                    try {
                                        p_1 = new_con.prepareStatement(command);
                                        col_count = p_1.getParameterMetaData().getParameterCount();
                                    } catch (SQLException ex) {
                                        System.out.println("Error 4i: " + ex.getMessage());
                                    }

                                } else if (command.startsWith("SPLITER")) {
                                    splitter = command.split("SPLITER")[1];
                                } else if (command.startsWith("CREATE")) {
                                    try {
                                        new_con.createStatement().execute(command);
                                    } catch (SQLException ex) {
                                        System.out.println("Error 4e: " + ex.getMessage() + " c_table=" + c_table);
                                    }
                                } else if (command.startsWith("ALTER")) {
                                    System.out.println("1063 " + command);
                                    try {
                                        new_con.createStatement().execute(command);
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
                        try {
                            new_con.close();
                        } catch (SQLException ex) {
                            ProgressMonitor.cancel();
                            System.out.println("Error 4k: " + ex.getMessage());
                        }
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
            //Create egen_dataEntry_resource
            String[] create_pool_dataAccess_a = new String[9];
            create_pool_dataAccess_a[0] = "--datasourceclassname=org.apache.derby.jdbc.ClientDataSource";
            create_pool_dataAccess_a[1] = "--restype=javax.sql.DataSource";
            create_pool_dataAccess_a[2] = "--driverclassname=org.apache.derby.jdbc.ClientDriver";
            create_pool_dataAccess_a[3] = "--maxpoolsize=64";
            create_pool_dataAccess_a[4] = "--pooling=true";
            create_pool_dataAccess_a[5] = "--steadypoolsize=10";
            create_pool_dataAccess_a[6] = "--property";

            create_pool_dataAccess_a[7] = "databasename=" + instruct_map.get(DATABASE_NAME_DATA_FLAG) + ":user=" + instruct_map.get(unm_gendataentry_flag) + ":portnumber="
                    + instruct_map.get(derby_port_flag) + ":port=" + instruct_map.get(derby_port_flag) + ":password=" + instruct_map.get(unm_gendataentry_pass_flag) + ":create=false:url=\"jdbc:" + instruct_map.get(db_host_flag) + ":"
                    + instruct_map.get(derby_port_flag) + "/" + instruct_map.get(DATABASE_NAME_DATA_FLAG) + "\":";
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


            //Create egen_dataView_resource
            create_pool_dataAccess_a[7] = "databasename=" + instruct_map.get(DATABASE_NAME_DATA_FLAG) + ":user=" + instruct_map.get(unm_gendataview_flag) + ":portnumber="
                    + instruct_map.get(derby_port_flag) + ":port=" + instruct_map.get(derby_port_flag) + ":password=" + instruct_map.get(unm_gendataview_pass_flag) + ":create=false:url=\"jdbc:" + instruct_map.get(db_host_flag) + ":"
                    + instruct_map.get(derby_port_flag) + "/" + instruct_map.get(DATABASE_NAME_DATA_FLAG) + "\":";
            create_pool_dataAccess_a[8] = "egen_dataView_pool";
            commandRunner = glassfish.getCommandRunner();
            commandResult = commandRunner.run("create-jdbc-connection-pool", create_pool_dataAccess_a);
            print_connection_error(commandResult.getFailureCause(), "5d");
            create_resource_dataAccess_a[0] = "--connectionpoolid=egen_dataView_pool";
            create_resource_dataAccess_a[1] = "egen_dataView_resource";
            commandRunner = glassfish.getCommandRunner();
            commandResult = commandRunner.run("create-jdbc-resource", create_resource_dataAccess_a);
            print_connection_error(commandResult.getFailureCause(), "5e");
            commandRunner = glassfish.getCommandRunner();
            commandResult = commandRunner.run("ping-connection-pool", "egen_dataView_pool");
            print_connection_error(commandResult.getFailureCause(), "5f");
            System.out.println("Connection to egen_dataView_pool was " + commandResult.getExitStatus());

            //Create egen_dataUpdate_resource   list-jdbc-connection-pools
            create_pool_dataAccess_a[7] = "databasename=" + instruct_map.get(DATABASE_NAME_DATA_FLAG) + ":user=" + instruct_map.get(unm_gendataUpdate_flag) + ":portnumber="
                    + instruct_map.get(derby_port_flag) + ":port=" + instruct_map.get(derby_port_flag) + ":password=" + instruct_map.get(unm_gendataUpdate_pass_flag) + ":create=true:url=\"jdbc:" + instruct_map.get(db_host_flag) + ":"
                    + instruct_map.get(derby_port_flag) + "/" + instruct_map.get(derby_port_flag) + "\":";
            create_pool_dataAccess_a[8] = "egen_dataUpdate_pool";
            commandRunner = glassfish.getCommandRunner();
            commandResult = commandRunner.run("create-jdbc-connection-pool", create_pool_dataAccess_a);
            print_connection_error(commandResult.getFailureCause(), "5g");
            create_resource_dataAccess_a[0] = "--connectionpoolid=egen_dataUpdate_pool";
            create_resource_dataAccess_a[1] = "egen_dataUpdate_resource";
            commandRunner = glassfish.getCommandRunner();
            commandResult = commandRunner.run("create-jdbc-resource", create_resource_dataAccess_a);
            print_connection_error(commandResult.getFailureCause(), "5h");
            commandRunner = glassfish.getCommandRunner();
            commandResult = commandRunner.run("ping-connection-pool", "egen_dataUpdate_pool");
            System.out.println("Connection egen_dataUpdate_pool was " + commandResult.getExitStatus());

            /*             
             //egen_dataUpdate_resource
             create_pool_usermanage_a[4] = "user=egendataentry:port=3306:password=k2prrr.N:url=\"jdbc:mysql://localhost:3306/egen_dataEntry\":";
             create_pool_usermanage_a[5] = "egen_dataUpdate_pool";
             commandRunner = glassfish.getCommandRunner();
             commandResult = commandRunner.run("create-jdbc-connection-pool", create_pool_usermanage_a);
             System.out.println("157 " + commandResult.getFailureCause());
             System.out.println("158 " + commandResult.getOutput());

             create_resource_usermanage_a[0] = "--connectionpoolid=egen_dataUpdate_pool";
             create_resource_usermanage_a[1] = "egen_dataUpdate_resource";
             commandResult = commandRunner.run("create-jdbc-resource", create_resource_usermanage_a);
             System.out.println("150" + commandResult.getFailureCause());
             System.out.println("151 " + commandResult.getOutput());
             
             */

        } catch (GlassFishException ex) {
            System.out.println("Error 5j " + ex.getMessage());
            return false;
        } catch (Exception ex) {
            System.out.println("Error 5i " + ex.getMessage());
            return false;
        }
        return true;
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

    private void create_db_connections(GlassFish glassfish) {
        try {
            //egen_userManagement_resource

            String[] create_pool_usermanage_a = new String[6];
            create_pool_usermanage_a[0] = "--datasourceclassname=com.mysql.jdbc.jdbc2.optional.MysqlConnectionPoolDataSource";
            create_pool_usermanage_a[1] = "--restype=javax.sql.ConnectionPoolDataSource";
            create_pool_usermanage_a[2] = "--driverclassname=com.mysql.jdbc.Driver";
            create_pool_usermanage_a[3] = "--property";
            create_pool_usermanage_a[4] = "user=usermanage:port=3306:password=VhC6OeDS:url=\"jdbc:mysql://localhost:3306/" + instruct_map.get(DATABASE_NAME_USERS_FLAG) + "\":";
            create_pool_usermanage_a[5] = "egen_userManagement_pool";
            CommandRunner commandRunner = glassfish.getCommandRunner();
            CommandResult commandResult = commandRunner.run("create-jdbc-connection-pool", create_pool_usermanage_a);
            print_connection_error(commandResult.getFailureCause(), "6a");
            String[] create_resource_usermanage_a = new String[2];
            create_resource_usermanage_a[0] = "--connectionpoolid=egen_userManagement_pool";
            create_resource_usermanage_a[1] = "egen_userManagement_resource";
            commandResult = commandRunner.run("create-jdbc-resource", create_resource_usermanage_a);
            print_connection_error(commandResult.getFailureCause(), "6b");
            String[] create_realm_usermanage_a = new String[4];
            create_realm_usermanage_a[0] = "--classname=com.sun.enterprise.security.auth.realm.jdbc.JDBCRealm";
            create_realm_usermanage_a[1] = "--property";
            create_realm_usermanage_a[2] = "digest-algorithm=\"" + instruct_map.get(DB_AUTHENTICATION_ALGORITHM_FLAG) + "\":user-name-column=username:password-column=password:group-name-column=groupname:group-table="
                    + instruct_map.get(DATABASE_NAME_USERS_FLAG) + ".groups:user-table=" + instruct_map.get(DATABASE_NAME_USERS_FLAG)
                    + ".useraccounts:datasource-jndi=egen_userManagement_resource:jaas-context=\"jdbcRealm\"";
            create_realm_usermanage_a[3] = "UsermangeRealm";
            commandResult = commandRunner.run("create-auth-realm", create_realm_usermanage_a);
            print_connection_error(commandResult.getFailureCause(), "6c");

//            //egen_dataEntry_resource
            create_pool_usermanage_a[4] = "user=egendataentry:port=3306:password=k2prrr.N:url=\"jdbc:mysql://localhost:3306/" + instruct_map.get(DATABASE_NAME_DATA_FLAG) + "\":";
            create_pool_usermanage_a[5] = "egen_dataEntry_pool";
            commandRunner = glassfish.getCommandRunner();
            commandResult = commandRunner.run("create-jdbc-connection-pool", create_pool_usermanage_a);
            print_connection_error(commandResult.getFailureCause(), "6d");
            create_resource_usermanage_a[0] = "--connectionpoolid=egen_dataEntry_pool";
            create_resource_usermanage_a[1] = "egen_dataEntry_resource";
            commandResult = commandRunner.run("create-jdbc-resource", create_resource_usermanage_a);
            print_connection_error(commandResult.getFailureCause(), "6e");

            //egen_dataView_resource
            create_pool_usermanage_a[4] = "user=egendataview:port=3306:password=k2prrr.N:url=\"jdbc:mysql://localhost:3306/" + instruct_map.get(DATABASE_NAME_DATA_FLAG) + "\":";
            create_pool_usermanage_a[5] = "egen_dataView_pool";
            commandRunner = glassfish.getCommandRunner();
            commandResult = commandRunner.run("create-jdbc-connection-pool", create_pool_usermanage_a);
            print_connection_error(commandResult.getFailureCause(), "6f");

            create_resource_usermanage_a[0] = "--connectionpoolid=egen_dataView_pool";
            create_resource_usermanage_a[1] = "egen_dataView_resource";
            commandResult = commandRunner.run("create-jdbc-resource", create_resource_usermanage_a);
            print_connection_error(commandResult.getFailureCause(), "6g");

            //egen_dataUpdate_resource
            create_pool_usermanage_a[4] = "user=egendataentry:port=3306:password=k2prrr.N:url=\"jdbc:mysql://localhost:3306/" + instruct_map.get(DATABASE_NAME_DATA_FLAG) + ":";
            create_pool_usermanage_a[5] = "egen_dataUpdate_pool";
            commandRunner = glassfish.getCommandRunner();
            commandResult = commandRunner.run("create-jdbc-connection-pool", create_pool_usermanage_a);
            print_connection_error(commandResult.getFailureCause(), "6h");

            create_resource_usermanage_a[0] = "--connectionpoolid=egen_dataUpdate_pool";
            create_resource_usermanage_a[1] = "egen_dataUpdate_resource";
            commandResult = commandRunner.run("create-jdbc-resource", create_resource_usermanage_a);
            print_connection_error(commandResult.getFailureCause(), "6i");


            //JAVA mail
            if (create_mailer_a != null) {
                commandResult = commandRunner.run("create-javamail-resource", create_mailer_a);
                print_connection_error(commandResult.getFailureCause(), "6a");
            } else {
                System.out.println("Error: mail source setup failed");
            }


        } catch (GlassFishException ex) {
            ex.printStackTrace();
        }
    }

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

    private int createTable(String sql) throws SQLException {
        if (createConnection()) {
            Statement stmt = conn.createStatement();
            return stmt.executeUpdate(sql);
        }
        return -1;
    }

    /*
     MethodID=11
     */
    private int addData(String sql) {
        int result = -1;
        try {
            if (createConnection()) {
                Statement stmt = conn.createStatement();
                result = stmt.executeUpdate(sql);
            }

        } catch (SQLException ex) {
            result = -1;
            System.out.println("Error 11a " + ex.getMessage() + "\n\t" + sql);
        }
        return result;
    }

    private int createTable_users(String sql) {
        int result = -1;
        try {
            if (createConnection_usermanage()) {
                Statement stmt = conn_users.createStatement();
                result = stmt.executeUpdate(sql);
            }
        } catch (SQLException ex) {
            System.out.println("Error 552 " + ex.getMessage() + "\n\t" + sql);
        }
        return result;
    }

    private void insertData(String table, ArrayList<HashMap<String, String>> data_map) {
        if (createConnection_usermanage()) {
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
    private boolean createConnection_usermanage() {
        boolean result = false;
        try {
            if (conn_users == null || conn_users.isClosed()) {
                Class.forName("org.apache.derby.jdbc.ClientDriver").newInstance();
                conn_users = DriverManager.getConnection(dbURL_users);
                result = true;
            } else {
                result = true;
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
        return result;
    }

    /*
     MethodID=8
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
            if (error.contains("password invalid")) {
                System.out.println("\n\n" + printformater_terminal("Error : Make sure the user name and the password is the same one used during creating this database. If you have lost the password, then use a different location to install the server or "
                        + "delete the database folders and try again.  For password recovery please refer the administrators manual"
                        + "The passwords are usually included in the config (e.g. " + instruct_map.get(CONFIG_FILE_FLAG) + " file and the default passwaord when non selected is 0000 (four zeros)", 80));
            }
            System.out.println("Error 8a: " + error);
        }
        return result;
    }

    /*
     MethodID=9
     */
    private boolean createConnection_mysql() {
        boolean result = false;
        try {
            if (conn_mysql == null || conn_mysql.isClosed()) {
                Class.forName("com.mysql.jdbc.Driver").newInstance();
                conn_mysql = DriverManager.getConnection(dbURL_dataEntry_mysql);
                result = true;
            } else {
                result = true;
            }
        } catch (Exception ex) {
            String error = ex.getMessage();
            if (error.contains("password invalid")) {
                System.out.println("\n\n" + printformater_terminal("Error : Make sure the user name and the password is the same one used during creating this database. If you have lost the password, then use a different location to install the server or "
                        + "delete the database folders and try again. There is no password recovery option due to security reasons"
                        + "The passwords are usually included in thr config (e.g. " + instruct_map.get(CONFIG_FILE_FLAG) + " file and the default passwaord when non selected is 0000 (four zeros)", 80) + "\n\n");

            }
            System.out.println("Error 9a: " + error);
        }
        return result;
    }

    private ArrayList<String> getCurrentTables() {
        ArrayList<String> result_l = new ArrayList<>(20);
        if (createConnection()) {
            try {
                Statement stmt = conn.createStatement();
                String q = "select TABLENAME from sys.systables WHERE UPPER(CAST(TABLETYPE AS CHAR(1))) = 'T' and SCHEMAID=(select SCHEMAID from SYS.SYSSCHEMAS where UPPER(CAST(SCHEMANAME AS VARCHAR(128)))=UPPER('" + instruct_map.get(DATABASE_NAME_DATA_FLAG).toUpperCase() + "'))";
//              String q = "select TABLENAME from sys.systables WHERE UPPER(CAST(TABLETYPE AS CHAR(1))) = 'T' ";
                ResultSet r_1 = stmt.executeQuery(q);
                while (r_1.next()) {
                    result_l.add(instruct_map.get(DATABASE_NAME_DATA_FLAG).toUpperCase() + "." + r_1.getString(1));
                }
                r_1.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
        return result_l;
    }

    private ArrayList<String> getCurrentTables_USERS() {
        ArrayList<String> result_l = new ArrayList<String>(20);
        if (createConnection()) {
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
        ArrayList<String> result_l = new ArrayList<String>(20);
        if (createConnection_usermanage()) {
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

    private ArrayList<String> executeQuery(String query) {
        ArrayList<String> result_l = new ArrayList<>(20);
        if (createConnection()) {
            try {
                Statement stmt = conn.createStatement();
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

    private int executeUpdate(String query) {
        int result = -1;
        if (createConnection()) {
            try {
                Statement stmt = conn.createStatement();
                result = stmt.executeUpdate(query);
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
        return result;
    }

    private ArrayList<String> executeQuery_user(String query) {
        ArrayList<String> result_l = new ArrayList<String>(20);
        if (createConnection_usermanage()) {
            try {
                Statement stmt = conn_users.createStatement();
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

    private void executeUpdateQuery(String query) {
        try {
            if (createConnection()) {
                Statement stmt = conn.createStatement();
                stmt.executeUpdate(query);
                stmt.close();
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

    }

    public int insert_user_account(HashMap<String, String> acouunt_map, int level) {
        int result = -1;
        try {
            if (createConnection_usermanage()) {
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

    /*
     MethodID=10
     */
    private void load_Biological_Ontologies(String filenm) {
        ///home/sabryr/Documents/DataIntergration/Config/Biological_Ontologies.txt
        //TABLENAME=egen_dataEntry.Biological_Ontologies==
        if (createConnection()) {
            ArrayList<String> ctables_l = getCurrentTables();
            if (!ctables_l.contains("" + instruct_map.get(DATABASE_NAME_DATA_FLAG) + ".BIOLOGICAL_ONTOLOGIES")) {
                File file = new File(filenm);
                if (file.isFile() && file.canRead()) {
                    try {

                        try {
                            if (!conn.isClosed()) {
                                int result = conn.createStatement().executeUpdate("CREATE TABLE " + instruct_map.get(DATABASE_NAME_DATA_FLAG) + ".Biological_Ontologies (id  int NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1)  PRIMARY KEY,name varchar(256),obo_id varchar(256),parent_id int,  namespace varchar(256),is_a varchar(256) ,definition varchar(2048), relationship  varchar(2048))");
                                String sql = "insert into " + instruct_map.get(DATABASE_NAME_DATA_FLAG) + ".Biological_Ontologies(name,obo_id,parent_id,namespace,is_a,definition,relationship) values(?,?,?,?,?,?,?)";
                                PreparedStatement p_1 = conn.prepareStatement(sql);
                                Scanner scan = new Scanner(file);
                                int saftey = 0;
                                while (scan.hasNext() && result >= 0 && saftey < 10) {
                                    saftey++;
                                    String line = scan.nextLine().trim();

                                    if (!line.isEmpty() && !line.startsWith("#")) {
                                        String[] split = line.split("\\|\\|");
                                        if (split.length == 8 && split[3].trim().matches("[0-9\\-]+")) {
                                            p_1.setString(1, split[1].trim());
                                            p_1.setString(2, split[2].trim());
                                            p_1.setInt(3, new Integer(split[3].trim()));
                                            p_1.setString(4, split[4].trim());
                                            p_1.setString(5, split[5].trim());
                                            p_1.setString(6, split[6].trim());
                                            p_1.setString(7, split[7].trim());
                                            result = p_1.executeUpdate();
                                        }
                                    }
                                }
                                result = conn.createStatement().executeUpdate("CREATE INDEX parent_indx1 ON " + instruct_map.get(DATABASE_NAME_DATA_FLAG) + ".BIOLOGICAL_ONTOLOGIES(parent_id)");
                                if (result >= 0) {
                                    System.out.println("1058 indexing success");
                                } else {
                                    System.out.println("1060 indexing failed");
                                }
                            }
                        } catch (SQLException ex) {
                            ex.printStackTrace();
                        }

                    } catch (FileNotFoundException ex) {
                        ex.printStackTrace();
                    }
                } else {
                    System.out.println("Error 10a: can not read " + filenm);
                }
            }
        }
    }

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
        result_map.put(SERVER_ROOT_FLAG, "http://localhost:8085/");
        result_map.put(SERVER_NAME_FLAG, "http://localhost:8085/eGenVar_web/");
        result_map.put(S_SERVER_ROOT_FLAG, "https://localhost:8185/");
        result_map.put(S_SERVER_NAME_FLAG, "https://localhost:8185/eGenVar_web/");
        result_map.put(WSDL_URL_FLAG, "http://localhost:8085/eGenVar_WebService/Authenticate_service?wsdl");
        result_map.put(S_WSDL_URL_FLAG, "https://localhost:8185/eGenVar_WebService/Authenticate_service?wsdl");
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

    private String getUserChoice(List tables_l, String msg) {
        String result = null;
        if (tables_l != null) {
            String[] tables_a = new String[tables_l.size()];
            for (int i = 0; i < tables_l.size(); i++) {
                tables_a[i] = tables_l.get(i).toString();
            }
            Arrays.sort(tables_a);
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
                        result = tables_a[coince];
                    }
                }
                if (!ok) {
                    System.out.println("Invalid choice, try again");
                }
            }
        }
        return result;
    }

    private String getuserInputSameLine(String message, String options) {
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

    private void createQueryExpansion() {
        System.out.println("Re-generating query expansions..");
        ArrayList<String> special_tbl_list = new ArrayList<>(10);

        ArrayList<String> insert_sql = new ArrayList<>(10);
        ArrayList<String> table_l = getCurrentTables();
        HashMap<String, ArrayList<String>> table2colmn_map = new HashMap<>();
        for (int i = 0; i < table_l.size(); i++) {
            table2colmn_map.put(table_l.get(i), new ArrayList<String>());
            ArrayList<String> col_l = getColumns(table_l.get(i));
            for (int j = 0; j < col_l.size(); j++) {
                table2colmn_map.get(table_l.get(i)).add(table_l.get(i) + "." + col_l.get(j));
            }
        }
        insert_sql.add("delete from " + get_correct_table_name("queryexpander"));
        for (int i = 0; i < table_l.size(); i++) {
            System.out.print(".");
            String c_tbl = table_l.get(i);
            c_tbl = get_correct_table_name(c_tbl);
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
            HashMap<String, String[]> constraints_map = get_key_contraints(c_tbl);
            if (constraints_map.containsKey(FOREIGN_KEY_COLUMNS_FLAG)) {
                String[] forign_nm_a = constraints_map.get(FOREIGN_KEY_COLUMNS_FLAG);
                for (int j = 0; j < forign_nm_a.length; j++) {
                    if (constraints_map.containsKey(forign_nm_a[j])) {
                        String[] foreing_k_a = constraints_map.get(forign_nm_a[j]);
                        String col_nm = foreing_k_a[0];
                        String target_tbl = get_correct_table_name(foreing_k_a[1]);
                        String target_tbl_col = foreing_k_a[2];
                        if (!target_tbl.equalsIgnoreCase(c_tbl)) {
                            String tag_sql = "select * from " + target_tbl + " where " + target_tbl_col + "=(select " + col_nm + " from " + c_tbl + " where id=?)";
                            String statrev_url = TABLE_TO_USE_FLAG + target_tbl + "=" + target_tbl_col + "(" + REPLACEWITH_ID_FALG + ")";
                            String inset_sql1 = "insert into " + get_correct_table_name("queryexpander") + "(table_name,column_nm,search_sql,static_url) values('" + c_tbl + "','" + col_nm + "','" + tag_sql + "','" + statrev_url + "')";
                            insert_sql.add(inset_sql1);
                        }
                    }
                }
            }

            String tag_table = get_correct_table_name(c_tbl + "2tags");
            if (tag_table != null) {
                HashMap<String, String[]> tag_constraints_maop = get_key_contraints(tag_table);
                if (tag_constraints_maop.containsKey(FOREIGN_KEY_COLUMNS_FLAG)) {
                    String[] forign_nm_a = tag_constraints_maop.get(FOREIGN_KEY_COLUMNS_FLAG);
                    for (int j = 0; j < forign_nm_a.length; j++) {
                        if (tag_constraints_maop.containsKey(forign_nm_a[j])) {
                            String[] foreing_k_a = tag_constraints_maop.get(forign_nm_a[j]);
                            String col_nm = foreing_k_a[0];
                            String target_tbl = get_correct_table_name(foreing_k_a[1]);
                            String target_tbl_col = foreing_k_a[2];
                            String tag_sql = "select * from " + tag_table + " where " + col_nm + "=(select " + target_tbl_col + " from " + target_tbl + " where  id=?)";
                            String statrev_sql = TABLE_TO_USE_FLAG + target_tbl + "=" + target_tbl_col + "(" + REPLACEWITH_ID_FALG + ")";
                            String inset_sql1 = "insert into " + get_correct_table_name("queryexpander") + "(table_name,column_nm,search_sql,static_url) values('" + c_tbl + "','" + col_nm + "','" + tag_sql + "','" + statrev_sql + "')";
                            insert_sql.add(inset_sql1);

                        }
                    }
                }
            }

            String hierarchy_table = get_correct_table_name(c_tbl + "_hierarchy");
            if (hierarchy_table != null) {
                HashMap<String, String[]> hierar_constraints_maop = get_key_contraints(hierarchy_table);
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
                    String tag_sql = "select * from " + c_tbl + " where id in (select " + parent_col + " from " + hierarchy_table + " where " + child_col + "=?)";
                    String statrev_sql = TABLE_TO_USE_FLAG + c_tbl + "=id" + "(" + REPLACEWITH_ID_FALG + ")";
                    String inset_sql1 = "insert into " + get_correct_table_name("queryexpander") + "(table_name,column_nm,search_sql,static_url) values('" + c_tbl + "','id','" + tag_sql + "','" + statrev_sql + "')";
                    insert_sql.add(inset_sql1);
                }
            }


            if ((c_tbl.split("_")[c_tbl.split("_").length - 1]).equalsIgnoreCase("tagsource")) {
                for (int j = 0; j < table_l.size(); j++) {
                    String c_string_tagged_tbl = get_correct_table_name(table_l.get(j) + "2tags");
                    if (c_string_tagged_tbl != null) {
                        HashMap<String, String[]> tag_constraints_maop = get_key_contraints(c_string_tagged_tbl);
                        if (tag_constraints_maop.containsKey(FOREIGN_KEY_COLUMNS_FLAG)) {
                            String[] forign_nm_a = tag_constraints_maop.get(FOREIGN_KEY_COLUMNS_FLAG);
                            for (int k = 0; k < forign_nm_a.length; k++) {
                                String[] foreing_k_a = tag_constraints_maop.get(forign_nm_a[k]);
                                String col_nm = foreing_k_a[0];
                                String target_tbl = get_correct_table_name(foreing_k_a[1]);
                                String target_tbl_col = foreing_k_a[2];
                                String get_file_sql = "select id from " + table_l.get(j) + " where " + target_tbl_col + " in (select " + col_nm + " from " + c_string_tagged_tbl + " where LINK_TO_FEATURE=''" + c_tbl.split("\\.")[c_tbl.split("\\.").length - 1] + "=" + LINK_TO_FEATURE_ID_FLAG + "'')";
                                String inset_sql1 = "insert into " + get_correct_table_name("queryexpander") + "(table_name,column_nm,search_sql) values('" + c_tbl + "','LINK_TO_FEATURE','" + get_file_sql + "')";
                                insert_sql.add(inset_sql1);
//                                   String get_file_4children_sql = "select id from " + table_l.get(j) + " where " + target_tbl_col + " in (select " + col_nm + " from " + c_string_tagged_tbl + " where LINK_TO_FEATURE=''" + c_tbl.split("\\.")[c_tbl.split("\\.").length - 1] + "=" + LINK_TO_FEATURE_ID_FLAG + "'')";

                            }
                        }
                    }
                }
            } else {
                ArrayList< String[]> reverse_constraints_l = get_reverse_key_contraints(c_tbl, null);
                if (reverse_constraints_l != null && !reverse_constraints_l.isEmpty()) {
                    for (int j = 0; j < reverse_constraints_l.size(); j++) {
                        String[] c_rv_a = reverse_constraints_l.get(j);
                        if (c_rv_a != null && c_rv_a.length >= 4) {
                            String tagtet_tbl = get_correct_table_name(c_rv_a[3]);
                            String tagtet_col = c_rv_a[0];
                            String source_colm = c_rv_a[2];
                            if (tagtet_tbl != null && !tagtet_tbl.toUpperCase().endsWith("2TAGS")) {
                                String rev_sql = "Select * from " + tagtet_tbl + " where " + tagtet_col + "=?";
                                String statrev_sql = TABLE_TO_USE_FLAG + tagtet_tbl + "=" + tagtet_col + "(" + REPLACEWITH_ID_FALG + ")";
                                String inset_sql1 = "insert into " + get_correct_table_name("queryexpander") + "(table_name,column_nm,search_sql,static_url) values('" + c_tbl + "','" + source_colm + "','" + rev_sql + "','" + statrev_sql + "')";
                                insert_sql.add(inset_sql1);
                            }
                        }
                    }
                }
            }
        }
        System.out.println("\nCreating paths2");
        for (int i = 0; i < table_l.size(); i++) {
            System.out.print(".");
            long mem = Runtime.getRuntime().freeMemory();
            if (mem < MIN_MEM) {
                System.out.println("Available memory too low (requires atleast " + (MIN_MEM / (1048576)) + ")");
            }
            String c_tbl = table_l.get(i);
            for (int j = 0; j < special_tbl_list.size(); j++) {
                String cs_tbl = special_tbl_list.get(j);
                if (!c_tbl.equals(cs_tbl)) {
                    ArrayList<ArrayList<String>> path_ll = getpaths(c_tbl, cs_tbl, table_l);
                    if (!path_ll.isEmpty() && !path_ll.get(0).isEmpty()) {
                        String path_q = cs_tbl + "|" + construct_Path_query(path_ll.get(0), table2colmn_map, cs_tbl, " " + c_tbl + ".ID=" + REPLACEWITH_ID_FALG + "");
                        String inset_sql1 = "insert into " + get_correct_table_name("queryexpander") + "(table_name,column_nm,search_sql) values('" + c_tbl + "','ID','" + path_q + "')";
                        insert_sql.add(inset_sql1);
                    }
                }
            }
        }
        System.out.println("");
        if (addToDb(insert_sql) >= 0) {
            System.out.println("\nAll query expansions were successfully registered");
        } else {
            System.out.println("\nError: Query expansions registering failed\n");
        }
    }

    private ArrayList<ArrayList<String>> getpaths(String source, String target, ArrayList<String> table_l) {
        ArrayList<ArrayList<String>> result = new ArrayList<>(1);
        HashMap<String, HashMap<String, String[]>> relationship_map = new HashMap<>();
        HashMap<String, ArrayList<String>> connection_map = new HashMap<>();

        for (int i = 0; i < table_l.size(); i++) {
            String c_tbl = table_l.get(i);
            HashMap<String, String[]> constraint_map = get_key_contraints(c_tbl);
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

    private String construct_Path_query(ArrayList<String> path, HashMap<String, ArrayList<String>> table2colmn_map, String taget_tbl, String query) {
        String sql = "";
        if (path != null && !path.isEmpty()) {
            boolean error = false;
            HashMap<Integer, String[]> constructer_map = new HashMap<>();
            for (int i = (path.size() - 1); (i > 0 && !error); i--) {
                String c_tbl = path.get(i);
                String c_table_clm = null;
                String c_previous_tbl = path.get(i - 1);
                String c_previous_tbl_clm = null;
                HashMap<String, String[]> constraint_map = get_key_contraints(c_tbl);
                if (constraint_map == null || constraint_map.isEmpty() || !constraint_map.containsKey(FOREIGN_TABLE_FLAG) || !arrayMatch(c_previous_tbl, constraint_map.get(FOREIGN_TABLE_FLAG))) {
                    constraint_map = get_key_contraints(c_previous_tbl);
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
                        error = true;
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
                    error = true;
                } else {
                    String[] new_construct = new String[4];
                    new_construct[0] = get_correct_table_name(c_tbl);
                    new_construct[1] = c_table_clm;
                    new_construct[2] = get_correct_table_name(c_previous_tbl);
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
            sql = "SELECT " + table2colmn_map.get(taget_tbl).toString().replace("[", "").replace("]", "").replaceAll("\\s", "") + " FROM " + use_tbl_l.toString().replace("[", "").replace("]", "").replaceAll("\\s", "") + " WHERE " + join + " AND " + query;



        } else {
            //print error
        }
        return sql;
    }

    private String construct_Path_query2(ArrayList<String> path, String query) {
        String sql = "";

        if (path != null && !path.isEmpty()) {
            boolean error = false;
            HashMap<Integer, String[]> constructer_map = new HashMap<>();
            for (int i = (path.size() - 1); (i > 0 && !error); i--) {
                String c_tbl = path.get(i);
                String c_table_clm = null;
                String c_previous_tbl = path.get(i - 1);
                String c_previous_tbl_clm = null;
                HashMap<String, String[]> constraint_map = get_key_contraints(c_tbl);
                if (constraint_map == null || constraint_map.isEmpty() || !constraint_map.containsKey(FOREIGN_TABLE_FLAG) || !arrayMatch(c_previous_tbl, constraint_map.get(FOREIGN_TABLE_FLAG))) {
                    constraint_map = get_key_contraints(c_previous_tbl);
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
                        error = true;
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
                    error = true;
                } else {
                    String[] new_construct = new String[4];
                    new_construct[0] = get_correct_table_name(c_tbl);
                    new_construct[1] = c_table_clm;
                    new_construct[2] = get_correct_table_name(c_previous_tbl);
                    new_construct[3] = c_previous_tbl_clm;
                    constructer_map.put(i, new_construct);
                }
            }

            int construct_size = constructer_map.keySet().size();
            String select_val_to_use = "*";
            for (int i = construct_size; i > 0; i--) {
                String[] c_constructs = constructer_map.get(i);
                String[] next_c_constructs = constructer_map.get(i - 1);
                String sql_tmp = null;
                if (next_c_constructs != null) {
                    if (i == construct_size) {
                        sql_tmp = "SELECT " + select_val_to_use + " FROM " + c_constructs[0] + " WHERE " + c_constructs[0] + "." + c_constructs[1] + " IN (SELECT " + c_constructs[3] + " FROM " + c_constructs[2] + " WHERE " + c_constructs[2] + "." + next_c_constructs[1] + "  <EXPAND_REPLACER>)";
                    } else {
                        sql_tmp = " (SELECT " + c_constructs[3] + " FROM " + c_constructs[2] + " WHERE " + c_constructs[2] + "." + next_c_constructs[1] + "  <EXPAND_REPLACER>)";
                    }
                    if (sql.isEmpty()) {
                        sql = sql_tmp;
                    } else {
                        sql = sql.replace("<EXPAND_REPLACER>", " in " + sql_tmp);
                    }
                } else {
                    if (construct_size == 1) {
                        if (c_constructs[3].equalsIgnoreCase("ID")) {
                            sql_tmp = " SELECT " + select_val_to_use + " FROM " + c_constructs[0] + " WHERE " + c_constructs[0] + "." + c_constructs[1] + " = " + query + "";
                        } else {
                            sql_tmp = " SELECT " + select_val_to_use + " FROM " + c_constructs[0] + " WHERE " + c_constructs[0] + "." + c_constructs[1] + " in (SELECT " + c_constructs[3] + " FROM " + c_constructs[2] + " where id=" + query + ")";
                        }
                    } else {
                        if (c_constructs[3].equalsIgnoreCase("ID")) {
                            sql_tmp = "=" + query;
                        } else {
                            sql_tmp = " (SELECT " + c_constructs[3] + " FROM " + c_constructs[2] + " where id=" + query + ")";
                        }
                    }
                    if (sql.isEmpty()) {
                        sql = sql_tmp;
                    } else {
                        sql = sql.replace("<EXPAND_REPLACER>", sql_tmp);
                    }
                }

            }
        } else {
            //print error
        }
        return sql;
    }

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

    private int addToDb(ArrayList<String> insert_sql_l) {
        int result = 0;
        System.out.println("Updating database");
        try {
            if (!conn.isClosed()) {
                Statement stm_1 = conn.createStatement();
                for (int i = 0; (i < insert_sql_l.size() && result >= 0); i++) {
                    if (i % 10 == 0) {
                        System.out.print(".");
                    }
                    result = stm_1.executeUpdate(insert_sql_l.get(i));
                }
                stm_1.close();
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        System.out.println("");
        return result;
    }

    private HashMap<String, String[]> get_key_contraints(String current_tbl_nm) {
        if (key_constraint_map == null) {
            key_constraint_map = new HashMap<>();
        }
        HashMap<String, String[]> returning_map = new HashMap<>(1);
        if (current_tbl_nm != null && !current_tbl_nm.isEmpty() && !current_tbl_nm.equalsIgnoreCase("null") && !key_constraint_map.containsKey(current_tbl_nm)) {
            try {
                if (createConnection()) {
                    returning_map.putAll(get_key_contraint_names(current_tbl_nm));
                    try {
                        ArrayList<String> foreign_columns = new ArrayList<>(1);
                        ArrayList<String> foreign_key_names_columns = new ArrayList<>(1);
                        ArrayList<String> foreign_tables = new ArrayList<>(1);
                        DatabaseMetaData metaData = conn.getMetaData();
                        String tablenm4metadata = current_tbl_nm.split("\\.")[current_tbl_nm.split("\\.").length - 1];

                        ResultSet key_result = metaData.getImportedKeys(conn.getCatalog(), null, tablenm4metadata);
                        if (!key_result.next()) {
                            key_result = metaData.getImportedKeys(conn.getCatalog(), instruct_map.get(DATABASE_NAME_DATA_FLAG), tablenm4metadata.toUpperCase());
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

    private HashMap<String, String[]> get_key_contraint_names(String current_tbl_nm) {
        HashMap<String, String[]> returning_map = new HashMap<>();

        if (current_tbl_nm != null && !current_tbl_nm.isEmpty() && !current_tbl_nm.equalsIgnoreCase("null")) {
            try {
                if (createConnection()) {
                    try {
                        DatabaseMetaData metaData = conn.getMetaData();
                        String tablenm4metadata = current_tbl_nm;
                        if (current_tbl_nm.contains(".")) {
                            tablenm4metadata = current_tbl_nm.split("\\.")[1];
                        }
                        ResultSet key_result = metaData.getIndexInfo(null, instruct_map.get(DATABASE_NAME_DATA_FLAG), tablenm4metadata, false, false);//.getImportedKeys(ncon.getCatalog(), Constants.DATABASE_NAME_DATA, current_tbl_nm);
                        ArrayList<String> uniqs_list = new ArrayList<>(1);
                        ArrayList<String> all_list = new ArrayList<>(2);
                        if (!key_result.next()) {
                            key_result = metaData.getIndexInfo(null, instruct_map.get(DATABASE_NAME_DATA_FLAG).toUpperCase(), tablenm4metadata.toUpperCase(), false, false);//.getImportedKeys(ncon.getCatalog(), Constants.DATABASE_NAME_DATA, current_tbl_nm);
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
    private void print_indices(String current_tbl_nm) {
        if (current_tbl_nm != null && !current_tbl_nm.isEmpty() && !current_tbl_nm.equalsIgnoreCase("null")) {
            try {
                if (createConnection()) {
                    try {
                        DatabaseMetaData metaData = conn.getMetaData();
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

    private ArrayList< String[]> get_reverse_key_contraints(String current_tbl_nm, ArrayList<String> previous_tbl_nm_list) {

        ArrayList<String[]> returning_list = new ArrayList<String[]>(1);
        if (current_tbl_nm != null && !current_tbl_nm.isEmpty() && !current_tbl_nm.equalsIgnoreCase("null")) {
            try {
                if (createConnection()) {
                    current_tbl_nm = get_correct_table_name(current_tbl_nm);
                    if (current_tbl_nm != null) {
                        ArrayList<String> table_l = getCurrentTables();
                        for (int i = 0; i < table_l.size(); i++) {
                            String c_tbl_nm = get_correct_table_name(table_l.get(i));
                            if ((c_tbl_nm != null && !c_tbl_nm.equals(current_tbl_nm)) && (previous_tbl_nm_list == null || previous_tbl_nm_list.contains(current_tbl_nm))) {
                                HashMap<String, String[]> constraint_map = get_key_contraints(c_tbl_nm);
                                String[] key_cols = constraint_map.get(FOREIGN_KEY_COLUMNS_FLAG);
                                if (key_cols != null) {
                                    for (int k = 0; k < key_cols.length; k++) {
                                        String c_f_col = key_cols[k];
                                        String[] tmp_array = constraint_map.get(c_f_col);
                                        if (tmp_array != null && tmp_array.length >= 3) {
                                            String fr_tbl = get_correct_table_name(tmp_array[1]);
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

    public HashMap<String, String[]> get_key_contraints(String current_tbl_nm, String target_tbl_nm, String index_name) {
        HashMap<String, String[]> final_returning_map = new HashMap<String, String[]>(1);
        HashMap<String, String[]> returning_map = new HashMap<String, String[]>(1);
        HashMap<String, String[]> returning_all_map = get_key_contraints(current_tbl_nm);
        if (target_tbl_nm != null) {
            if (returning_all_map != null && returning_all_map.containsKey(FOREIGN_KEY_COLUMNS_FLAG)) {
                ArrayList<String> return_keys = new ArrayList<String>(returning_all_map.keySet());
                for (int i = 0; i < return_keys.size(); i++) {
                    if (returning_all_map.get(return_keys.get(i)).length >= 3 && returning_all_map.get(return_keys.get(i))[1].equals(target_tbl_nm)) {
                        returning_map.put(returning_all_map.get(return_keys.get(i))[0], returning_all_map.get(return_keys.get(i)));
                    }
                }
            }
        } else {
            returning_all_map.putAll(returning_map);
        }
        if (index_name != null) {
            if (returning_all_map != null && returning_all_map.containsKey(FOREIGN_KEY_NAMES_FLAG)) {
                ArrayList<String> return_keys = new ArrayList<String>(returning_map.keySet());
                for (int i = 0; i < return_keys.size(); i++) {
                    if (returning_map.get(return_keys.get(i)).length >= 4) {
                        String c_index_name = returning_map.get(return_keys.get(i))[3].toUpperCase();
                        if (c_index_name.startsWith(index_name.toUpperCase())) {
                            final_returning_map.put(returning_all_map.get(return_keys.get(i))[0], returning_all_map.get(return_keys.get(i)));
                        }
                    }
                }
            }

        } else {
            final_returning_map.putAll(returning_map);
        }
//        System.out.println("3138 "+final_returning_map.keySet());
        return final_returning_map;
    }

    private ArrayList<String> getColumns(String current_tbl_nm) {
        ArrayList<String> columns_l = new ArrayList<String>(2);
        if (table2Columns_map == null && createConnection()) {
            table2Columns_map = new HashMap<String, ArrayList<String>>();
            try {
                DatabaseMetaData metaData = conn.getMetaData();
                if (!conn.isClosed()) {
                    ArrayList<String> c_table_l = getCurrentTables();
                    for (int i = 0; i < c_table_l.size(); i++) {
                        ArrayList<String> tmp_columns_l = new ArrayList<String>(2);
                        String c_table = c_table_l.get(i);
                        String tablenm4metadata = c_table;
                        if (c_table.contains(".")) {
                            tablenm4metadata = c_table.split("\\.")[1];
                        }

                        ResultSet key_result = metaData.getColumns(conn.getCatalog(), null, tablenm4metadata, null);
                        if (!key_result.next()) {
                            key_result = metaData.getColumns(null, instruct_map.get(DATABASE_NAME_DATA_FLAG), tablenm4metadata.toUpperCase(), null);
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
        current_tbl_nm = get_correct_table_name(current_tbl_nm);
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

    private int getUserChoice(ArrayList<String> tables_l, String msg) {
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

    /*
     MethodID=15
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
                String msg = ex.getMessage();
                System.out.println("\nFaild to send mail. Error 15a: " + ex.getMessage());

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

    private int refreshTableTOFeatures() {
        int result = 0;
        if (createConnection()) {
            ArrayList<String> sql_l = new ArrayList<>();
            sql_l.add("DROP TABLE " + instruct_map.get(DATABASE_NAME_DATA_FLAG) + ".tablename2feature");
            sql_l.add("CREATE TABLE " + instruct_map.get(DATABASE_NAME_DATA_FLAG) + ".tablename2feature(id int NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1)  PRIMARY KEY,table_name varchar(128),column_nm varchar(128) default 'name',description varchar(256),category varchar(128),simplename varchar(256),table_description varchar(256) default 'NA',search_description varchar(128) default 'NA',usageinfo varchar(256) default null,showinsearch int default 0,taggable int default 0,avialable int default 0)");
            sql_l.add("insert into " + instruct_map.get(DATABASE_NAME_DATA_FLAG) + ".tablename2feature(table_name,column_nm) (select (SELECT TABLENAME from  SYS.SYSTABLES where TABLEID=SYS.SYSCOLUMNS.REFERENCEID), COLUMNNAME from SYS.SYSCOLUMNS WHERE (SELECT TABLENAME from SYS.SYSTABLES where CAST(SYS.SYSTABLES.TABLETYPE AS CHAR(1))='T' and SYS.SYSTABLES.SCHEMAID=(select SCHEMAID from SYS.SYSSCHEMAS where CAST(SYS.SYSSCHEMAS.SCHEMANAME AS VARCHAR(128))='" + instruct_map.get(DATABASE_NAME_DATA_FLAG) + "') and TABLEID=SYS.SYSCOLUMNS.REFERENCEID) is not null )");
            sql_l.add("update " + instruct_map.get(DATABASE_NAME_DATA_FLAG) + ".tablename2feature set description='Personal information, such as name or email' where table_name='person' and column_nm='name' ");
            sql_l.add("update " + instruct_map.get(DATABASE_NAME_DATA_FLAG) + ".tablename2feature set description='file name or the location path' where table_name='files' and column_nm='name'");
            sql_l.add("update " + instruct_map.get(DATABASE_NAME_DATA_FLAG) + ".tablename2feature set description='report details (report is a collection of files)' where table_name='report' and  column_nm='name'");
            sql_l.add("update " + instruct_map.get(DATABASE_NAME_DATA_FLAG) + ".tablename2feature set description='sample name' where table_name='sampledetails' and column_nm='name' ");
            sql_l.add("update " + instruct_map.get(DATABASE_NAME_DATA_FLAG) + ".tablename2feature set description='sample id' where table_name='sampledetails' and column_nm='sample_id' ");
            sql_l.add("update " + instruct_map.get(DATABASE_NAME_DATA_FLAG) + ".tablename2feature set description='donor details (cell line, donor id, tissue etc.)'  where table_name='donordetails' and column_nm='name'");
            sql_l.add("update " + instruct_map.get(DATABASE_NAME_DATA_FLAG) + ".tablename2feature set description='Report batch is a collection of reports. This is the unique name of it'  where table_name='report_batch' and column_nm='name'");
            sql_l.add("update " + instruct_map.get(DATABASE_NAME_DATA_FLAG) + ".tablename2feature set description='The parent of the donor. E.g. if the sample was from a tissue, then the organ where the tissue was obtained was the parent ' where table_name='donordetails' and column_nm='parent_id' ");
            sql_l.add("update " + instruct_map.get(DATABASE_NAME_DATA_FLAG) + ".tablename2feature set description='Description of donor' where table_name='donordetails' and column_nm='description' ");
            sql_l.add("update " + instruct_map.get(DATABASE_NAME_DATA_FLAG) + ".tablename2feature set description='Internal identifier for the donor (e.g. An internal  identifier referring to patient)' where table_name='donordetails' and column_nm='internal_identifier' ");
            sql_l.add("update " + instruct_map.get(DATABASE_NAME_DATA_FLAG) + ".tablename2feature set description='A serial number for the donor (e.g. identifier used by the company where the donor was purchased)' where table_name='donordetails' and column_nm='serial_number' ");
            sql_l.add("update " + instruct_map.get(DATABASE_NAME_DATA_FLAG) + ".tablename2feature set description='A ' where table_name='donordetails' and column_nm='name' ");
            sql_l.add("update " + instruct_map.get(DATABASE_NAME_DATA_FLAG) + ".tablename2feature set description='The size of the file in bytes' where table_name='files' and column_nm='filesize' ");
            sql_l.add("update " + instruct_map.get(DATABASE_NAME_DATA_FLAG) + ".tablename2feature set description='A description about the file and its content' where table_name='files' and column_nm='description' ");
            sql_l.add("update " + instruct_map.get(DATABASE_NAME_DATA_FLAG) + ".tablename2feature set description='The MD5 check-sum value for the file' where table_name='files' and column_nm='checksum' ");
            sql_l.add("update " + instruct_map.get(DATABASE_NAME_DATA_FLAG) + ".tablename2feature set description='The absolute path for the file' where table_name='files' and column_nm='location' ");
            sql_l.add("update " + instruct_map.get(DATABASE_NAME_DATA_FLAG) + ".tablename2feature set description='The date last modification was done' where table_name='files' and column_nm='lastmodified' ");
            sql_l.add("update " + instruct_map.get(DATABASE_NAME_DATA_FLAG) + ".tablename2feature set description='The ownership information as reported by the filesystem' where table_name='files' and column_nm='ownergroup' ");
            sql_l.add("update " + instruct_map.get(DATABASE_NAME_DATA_FLAG) + ".tablename2feature set description='An identifier referring to a type description in the filetype table ' where table_name='files' and column_nm='filetype_id' ");
            sql_l.add("update " + instruct_map.get(DATABASE_NAME_DATA_FLAG) + ".tablename2feature set description='A unique name for the file, including the server name, and absolute path' where table_name='files' and column_nm='name' ");
            sql_l.add("update " + instruct_map.get(DATABASE_NAME_DATA_FLAG) + ".tablename2feature set description='This table used in the tagging process. The files_id refers to an entry in the files table' where table_name='files2tags' and column_nm='files_id' ");
            sql_l.add("update " + instruct_map.get(DATABASE_NAME_DATA_FLAG) + ".tablename2feature set description='This table used in the tagging process. A description about the tag' where table_name='files2tags' and column_nm='description' ");
            sql_l.add("update " + instruct_map.get(DATABASE_NAME_DATA_FLAG) + ".tablename2feature set description='This table used in the tagging process. a unique name for the tag, usually generated automatically' where table_name='files2tags' and column_nm='name' ");
            sql_l.add("update " + instruct_map.get(DATABASE_NAME_DATA_FLAG) + ".tablename2feature set description='A link to more descriptions about the tag.' where table_name='files2tags' and column_nm='link_to_feature' ");
            sql_l.add("update " + instruct_map.get(DATABASE_NAME_DATA_FLAG) + ".tablename2feature set description='The connection between the files and the reports, this is the report identifier' where table_name='files2report' and column_nm='report_id' ");
            sql_l.add("update " + instruct_map.get(DATABASE_NAME_DATA_FLAG) + ".tablename2feature set description='The connection between the files and the reports, this is the files identifier' where table_name='files2report' and column_nm='files_id' ");
            sql_l.add("update " + instruct_map.get(DATABASE_NAME_DATA_FLAG) + ".tablename2feature set description='The connection between the files and the samples, this is a short description about the sample and the file' where table_name='files2sampledetails' and column_nm='description' ");
            sql_l.add("update " + instruct_map.get(DATABASE_NAME_DATA_FLAG) + ".tablename2feature set description='The connection between the files and the samples, this is the files id' where table_name='files2sampledetails' and column_nm='files_id' ");
            sql_l.add("update " + instruct_map.get(DATABASE_NAME_DATA_FLAG) + ".tablename2feature set description='The connection between the files and the samples, this is the sample detail id' where table_name='files2sampledetails' and column_nm='sampledetails_id' ");
            sql_l.add("update " + instruct_map.get(DATABASE_NAME_DATA_FLAG) + ".tablename2feature set description='The connection between the files, sample and the samples , this is the donor where the sample is coming from' where table_name='files2sampledetails' and column_nm='donordetails_id' ");
            sql_l.add("update " + instruct_map.get(DATABASE_NAME_DATA_FLAG) + ".tablename2feature set description='The connection between the files, a unique name for this relationship between the file and the sample' where table_name='files2sampledetails' and column_nm='name' ");
            sql_l.add("update " + instruct_map.get(DATABASE_NAME_DATA_FLAG) + ".tablename2feature set description='The file extension of the files type' where table_name='filetype' and column_nm='fileextension' ");
            sql_l.add("update " + instruct_map.get(DATABASE_NAME_DATA_FLAG) + ".tablename2feature set description='A online resource showing how to use the file' where table_name='filetype' and column_nm='linktoSoftware' ");
            sql_l.add("update " + instruct_map.get(DATABASE_NAME_DATA_FLAG) + ".tablename2feature set description='A short description about the file type' where table_name='filetype' and column_nm='description' ");
            sql_l.add("update " + instruct_map.get(DATABASE_NAME_DATA_FLAG) + ".tablename2feature set description='Type of the file, e.g. text, XML, binary' where table_name='filetype' and column_nm='type' ");
            sql_l.add("update " + instruct_map.get(DATABASE_NAME_DATA_FLAG) + ".tablename2feature set description='A unique name for the file type' where table_name='filetype' and column_nm='name' ");
            sql_l.add("update " + instruct_map.get(DATABASE_NAME_DATA_FLAG) + ".tablename2feature set description='A unique name for the relationship (optional)' where table_name='file_hierarchy' and column_nm='name' ");
            sql_l.add("update " + instruct_map.get(DATABASE_NAME_DATA_FLAG) + ".tablename2feature set description='The id of the parent file from the files table' where table_name='file_hierarchy' and column_nm='parentfile_id' ");
            sql_l.add("update " + instruct_map.get(DATABASE_NAME_DATA_FLAG) + ".tablename2feature set description='The id of the child file from the files table' where table_name='file_hierarchy' and column_nm='childfile_id' ");
            sql_l.add("update " + instruct_map.get(DATABASE_NAME_DATA_FLAG) + ".tablename2feature set description='The id of the address from the address table' where table_name='person' and column_nm='address_id' ");
            sql_l.add("update " + instruct_map.get(DATABASE_NAME_DATA_FLAG) + ".tablename2feature set description='Where do you work' where table_name='person' and column_nm='Organizationinfo' ");
            sql_l.add("update " + instruct_map.get(DATABASE_NAME_DATA_FLAG) + ".tablename2feature set description='A unique name, usually the email' where table_name='person' and column_nm='name' ");
            sql_l.add("update " + instruct_map.get(DATABASE_NAME_DATA_FLAG) + ".tablename2feature set description='email' where table_name='person' and column_nm='email' ");
            sql_l.add("update " + instruct_map.get(DATABASE_NAME_DATA_FLAG) + ".tablename2feature set description='First name' where table_name='person' and column_nm='lastnm' ");
            sql_l.add("update " + instruct_map.get(DATABASE_NAME_DATA_FLAG) + ".tablename2feature set description='Last name' where table_name='person' and column_nm='firstnm' ");
            sql_l.add("update " + instruct_map.get(DATABASE_NAME_DATA_FLAG) + ".tablename2feature set description='A free text description about the report' where table_name='report' and column_nm='description' ");
            sql_l.add("update " + instruct_map.get(DATABASE_NAME_DATA_FLAG) + ".tablename2feature set description='The type of the report, an identifier referring to the reportType table' where table_name='report' and column_nm='reporttype_id' ");
            sql_l.add("update " + instruct_map.get(DATABASE_NAME_DATA_FLAG) + ".tablename2feature set description='The person reporting, an identifier referring to the person table' where table_name='report' and column_nm='reporter_id' ");
            sql_l.add("update " + instruct_map.get(DATABASE_NAME_DATA_FLAG) + ".tablename2feature set description='The batch this report belongs to, an identifier referring to the report_batch table' where table_name='report' and column_nm='report_batch_id' ");
            sql_l.add("update " + instruct_map.get(DATABASE_NAME_DATA_FLAG) + ".tablename2feature set description='When was the report created. Usually added automatically' where table_name='report' and column_nm='entryDate' ");
            sql_l.add("update " + instruct_map.get(DATABASE_NAME_DATA_FLAG) + ".tablename2feature set description='A unique name for the report, usually the directory name where the files are stored' where table_name='report' and column_nm='name' ");
            sql_l.add("update " + instruct_map.get(DATABASE_NAME_DATA_FLAG) + ".tablename2feature set description='The id of the report which the properties is attached to' where table_name='report2tags' and column_nm='report_id' ");
            sql_l.add("update " + instruct_map.get(DATABASE_NAME_DATA_FLAG) + ".tablename2feature set description='A free text description of this property' where table_name='report2tags' and column_nm='description' ");
            sql_l.add("update " + instruct_map.get(DATABASE_NAME_DATA_FLAG) + ".tablename2feature set description='An optional unique name the property of the report' where table_name='report2tags' and column_nm='name' ");
            sql_l.add("update " + instruct_map.get(DATABASE_NAME_DATA_FLAG) + ".tablename2feature set description='A link pointing to a feature or a additional information table, this would normally be automatically generated ' where table_name='report2tags' and column_nm='link_to_feature' ");
            sql_l.add("update " + instruct_map.get(DATABASE_NAME_DATA_FLAG) + ".tablename2feature set description='A description about the report sample relationship' where table_name='report2sampledetails' and column_nm='description' ");
            sql_l.add("update " + instruct_map.get(DATABASE_NAME_DATA_FLAG) + ".tablename2feature set description='The id of the report, pointing to a entry in the report table' where table_name='report2sampledetails' and column_nm='report_id' ");
            sql_l.add("update " + instruct_map.get(DATABASE_NAME_DATA_FLAG) + ".tablename2feature set description='The id of the sampledetails table, where the sample details are described ' where table_name='report2sampledetails' and column_nm='sampledetails_id' ");
            sql_l.add("update " + instruct_map.get(DATABASE_NAME_DATA_FLAG) + ".tablename2feature set description='The id of the donor_detials, where the donor of the sample is described' where table_name='report2sampledetails' and column_nm='donordetails_id' ");
            sql_l.add("update " + instruct_map.get(DATABASE_NAME_DATA_FLAG) + ".tablename2feature set description='An optional unique name for this relationship' where table_name='report2sampledetails' and column_nm='name' ");
            sql_l.add("update " + instruct_map.get(DATABASE_NAME_DATA_FLAG) + ".tablename2feature set description='A unique name for the report batch, a report batch is a collection of reports' where table_name='report_batch' and column_nm='name' ");
            sql_l.add("update " + instruct_map.get(DATABASE_NAME_DATA_FLAG) + ".tablename2feature set description='The report batch which the property is attached to' where table_name='report_batch2tags' and column_nm='report_batch_id' ");
            sql_l.add("update " + instruct_map.get(DATABASE_NAME_DATA_FLAG) + ".tablename2feature set description='A free text description of this property' where table_name='report_batch2tags' and column_nm='description' ");
            sql_l.add("update " + instruct_map.get(DATABASE_NAME_DATA_FLAG) + ".tablename2feature set description='An optional name for this relationship' where table_name='report_batch2tags' and column_nm='name' ");
            sql_l.add("update " + instruct_map.get(DATABASE_NAME_DATA_FLAG) + ".tablename2feature set description='A link pointing to a feature or a additional information table, this would normally be automatically generated' where table_name='report_batch2tags' and column_nm='link_to_feature' ");
            sql_l.add("update " + instruct_map.get(DATABASE_NAME_DATA_FLAG) + ".tablename2feature set description='The id of the child report_batch. This is the report batch which has inherited some property for the parent' where table_name='report_batch_hierarchy' and column_nm='childreport_batch_id' ");
            sql_l.add("update " + instruct_map.get(DATABASE_NAME_DATA_FLAG) + ".tablename2feature set description='The id of the parent report_batch. This is the report batch which is inheriting some property for the child' where table_name='report_batch_hierarchy' and column_nm='parentreport_batch_id' ");
            sql_l.add("update " + instruct_map.get(DATABASE_NAME_DATA_FLAG) + ".tablename2feature set description='An optional unique name for this relationship' where table_name='report_batch_hierarchy' and column_nm='name' ");
            sql_l.add("update " + instruct_map.get(DATABASE_NAME_DATA_FLAG) + ".tablename2feature set description='The id of the child report. This is the report which has inherited some property for the parent' where table_name='report_hierarchy' and column_nm='childreport_id' ");
            sql_l.add("update " + instruct_map.get(DATABASE_NAME_DATA_FLAG) + ".tablename2feature set description='The id of the parent report. This is the report which is inheriting some property for the child' where table_name='report_hierarchy' and column_nm='parentreport_id' ");
            sql_l.add("update " + instruct_map.get(DATABASE_NAME_DATA_FLAG) + ".tablename2feature set description='An optional unique name for this relationship' where table_name='report_hierarchy' and column_nm='name' ");
            sql_l.add("update " + instruct_map.get(DATABASE_NAME_DATA_FLAG) + ".tablename2feature set description='A short description about the sample' where table_name='sampledetails' and column_nm='description' ");
            sql_l.add("update " + instruct_map.get(DATABASE_NAME_DATA_FLAG) + ".tablename2feature set description='An internal identifier for the sample' where table_name='sampledetails' and column_nm='sample_id' ");
            sql_l.add("update " + instruct_map.get(DATABASE_NAME_DATA_FLAG) + ".tablename2feature set description='A unique name for the sample, this could be same as the sample_id' where table_name='sampledetails' and column_nm='name' ");
            sql_l.add("update " + instruct_map.get(DATABASE_NAME_DATA_FLAG) + ".tablename2feature set description='An optional name for this relationship' where table_name='donordetails_hierarchy' and column_nm='name'");
            sql_l.add("update " + instruct_map.get(DATABASE_NAME_DATA_FLAG) + ".tablename2feature set description='The parent of the donor. This could literally be the parent of the person donated the sample, the cell line the current donor is from, the parent tissue etc..  ' where table_name='donordetails_hierarchy' and column_nm='parentdonordetails_id'");
            sql_l.add("update " + instruct_map.get(DATABASE_NAME_DATA_FLAG) + ".tablename2feature set description='The child component of this relationship' where table_name='donordetails_hierarchy' and column_nm='childdonordetails_id'");
            sql_l.add("update " + instruct_map.get(DATABASE_NAME_DATA_FLAG) + ".tablename2feature set description='An optional name for this relationship' where table_name='files_hierarchy' and column_nm='name'");
            sql_l.add("update " + instruct_map.get(DATABASE_NAME_DATA_FLAG) + ".tablename2feature set description='The parent file id, refer to a id in the files table.' where table_name='files_hierarchy' and column_nm='parentfiles_id'");
            sql_l.add("update " + instruct_map.get(DATABASE_NAME_DATA_FLAG) + ".tablename2feature set description='The child file id, refer to a id in the files table' where table_name='files_hierarchy' and column_nm='childfiles_id'");
            sql_l.add("update " + instruct_map.get(DATABASE_NAME_DATA_FLAG) + ".tablename2feature set description='The unique name for the authority assigning this id' where table_name='idsource' and column_nm='name'");
            sql_l.add("update " + instruct_map.get(DATABASE_NAME_DATA_FLAG) + ".tablename2feature set description='The unique name for the authority assigning this id.  This could be as same as the name' where table_name='idsource' and column_nm='databasename'");
            sql_l.add("update " + instruct_map.get(DATABASE_NAME_DATA_FLAG) + ".tablename2feature set description='The report component of this relationship' where table_name='report2idsource' and column_nm='report_id'");
            sql_l.add("update " + instruct_map.get(DATABASE_NAME_DATA_FLAG) + ".tablename2feature set description='The reference to the idsource table where the assigning authority is defined' where table_name='report2idsource' and column_nm='idsource_id'");
            sql_l.add("update " + instruct_map.get(DATABASE_NAME_DATA_FLAG) + ".tablename2feature set description='The identifier' where table_name='report2idsource' and column_nm='name'");
            sql_l.add("update " + instruct_map.get(DATABASE_NAME_DATA_FLAG) + ".tablename2feature set description='A short description about this report batch' where table_name='report_batch' and column_nm='description'");
            sql_l.add("update " + instruct_map.get(DATABASE_NAME_DATA_FLAG) + ".tablename2feature set description='A unique name for this relationship (optional)' where table_name='sampledetails_hierarchy' and column_nm='name'");
            sql_l.add("update " + instruct_map.get(DATABASE_NAME_DATA_FLAG) + ".tablename2feature set description='The parent sample' where table_name='sampledetails_hierarchy' and column_nm='parentsampledetails_id'");
            sql_l.add("update " + instruct_map.get(DATABASE_NAME_DATA_FLAG) + ".tablename2feature set description='The child sample' where table_name='sampledetails_hierarchy' and column_nm='childsampledetails_id'");
            sql_l.add("update " + instruct_map.get(DATABASE_NAME_DATA_FLAG) + ".tablename2feature set description='biological_ontologies' where table_name='biological_ontologies' and column_nm='table_name'");
            sql_l.add("update " + instruct_map.get(DATABASE_NAME_DATA_FLAG) + ".tablename2feature set description='Connecting samples with standard tags' where table_name='sampledetails2tags' and column_nm='description'");
            sql_l.add("update " + instruct_map.get(DATABASE_NAME_DATA_FLAG) + ".tablename2feature set description='Connecting samples with standard tags, the link to the  feature ' where table_name='sampledetails2tags' and column_nm='link_to_feature'");
            sql_l.add("update " + instruct_map.get(DATABASE_NAME_DATA_FLAG) + ".tablename2feature set description='Connecting samples with standard tags, the sample which is tagged' where table_name='sampledetails2tags' and column_nm='sampledetails2tags_id'");
            sql_l.add("update " + instruct_map.get(DATABASE_NAME_DATA_FLAG) + ".tablename2feature set description='Connecting samples with standard tags, optional unique name' where table_name='sampledetails2tags' and column_nm='name'");
            sql_l.add("update " + instruct_map.get(DATABASE_NAME_DATA_FLAG) + ".tablename2feature set description='Absolute path of the file' where table_name='FILES2PATH' and column_nm='FILEPATH'");

            sql_l.add("update " + instruct_map.get(DATABASE_NAME_DATA_FLAG) + ".tablename2feature set showinsearch=1 where table_name not in ('tablename2feature','advancedqueryconstructor','errors','fieldhelp','queryExpander','tabledescription','tmp')");
            sql_l.add("update " + instruct_map.get(DATABASE_NAME_DATA_FLAG) + ".tablename2feature set taggable=1 where table_name in ('files', 'report','report_batch','sampledetails')");
            sql_l.add("update " + instruct_map.get(DATABASE_NAME_DATA_FLAG) + ".tablename2feature set avialable=1 where table_name not in ('tablename2feature','advancedqueryconstructor','errors','fieldhelp','queryExpander','tabledescription','tmp')");
            sql_l.add("update " + instruct_map.get(DATABASE_NAME_DATA_FLAG) + ".tablename2feature set category='Other'");

            sql_l.add("update " + instruct_map.get(DATABASE_NAME_DATA_FLAG) + ".tablename2feature set category='Tags' where table_name like '%2tags%'");
            sql_l.add("update " + instruct_map.get(DATABASE_NAME_DATA_FLAG) + ".tablename2feature set category='People/Groups/companies' where table_name in ('person')");
            sql_l.add("update " + instruct_map.get(DATABASE_NAME_DATA_FLAG) + ".tablename2feature set category='File name or location' where table_name in ('files', 'files2path')");
            sql_l.add("update " + instruct_map.get(DATABASE_NAME_DATA_FLAG) + ".tablename2feature set category='Name of a Reports or a Report batch' where table_name in ('report', 'report_batch')");
            sql_l.add("update " + instruct_map.get(DATABASE_NAME_DATA_FLAG) + ".tablename2feature set category='Name of a donors or a sample' where table_name in ('sampledetails', 'donordetails')");
            sql_l.add("update " + instruct_map.get(DATABASE_NAME_DATA_FLAG) + ".tablename2feature set category='Relationships and data lineage' where  category='Other' and (table_name like '%HIERARCHY%'  or table_name like '%2%')");
            sql_l.add("update " + instruct_map.get(DATABASE_NAME_DATA_FLAG) + ".tablename2feature set category='Tag sources' where  category='Other' and table_name like '%tagsource%'");
            sql_l.add("update " + instruct_map.get(DATABASE_NAME_DATA_FLAG) + ".tablename2feature set simplename=table_name");
//            sql_l.add("update " + instruct_map.get(DATABASE_NAME_DATA_FLAG) + ".tablename2feature set simplename='File metadata' where category='Other' and  table_name='files'");
//            sql_l.add("update " + instruct_map.get(DATABASE_NAME_DATA_FLAG) + ".tablename2feature set simplename='File tags'  where category='Other' and  table_name='files2tags'");
            try {
                if (!conn.isClosed()) {
                    Statement stm = conn.createStatement();
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
     Part of this methid from
     * http://www.java2s.com/Code/Java/Security/EncryptingandDecryptingwiththeJCE.htm
     */
    private HashMap<String, String> readFromFileEncripted(String file_nm) {
        HashMap<String, String> config_map = new HashMap<>();
        if (file_nm != null) {
            try {
                byte key[] = encript_password.getBytes();
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
                if (tmp_ob != null) {
                    config_map = (HashMap<String, String>) tmp_ob;
                }
            } catch (NoSuchAlgorithmException | InvalidKeyException | InvalidKeySpecException | NoSuchPaddingException | ClassNotFoundException ex) {
                ex.printStackTrace();
            } catch (FileNotFoundException ex) {
                ex.printStackTrace();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return config_map;
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
}