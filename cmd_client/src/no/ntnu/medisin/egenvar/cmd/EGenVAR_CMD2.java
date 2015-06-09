/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package no.ntnu.medisin.egenvar.cmd;

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
import java.io.StreamCorruptedException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.NetworkInterface;
import java.net.URL;
import java.net.URLConnection;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.charset.Charset;
import java.nio.file.DirectoryStream;
import java.nio.file.FileVisitOption;

import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.*;
import java.util.regex.Pattern;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLException;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.xml.ws.BindingProvider;

/**
 *
 * #!/bin/bash md5sum $1 alias md5=’md5 -r’ alias md5sum=’md5 -r’
 *
 * @author sabryr Error codes 1 : 2: 3: 4: 5: 6:getFileDetails 7:createUndoPoint
 * 8: Fail to retrieve current working directory 9: 10: 11: 12: 13: 14: 15:25
 *
 * 16:22:23 17: 30 #!/bin/bash java -Xms512m -Xmx2g -jar "`dirname
 * $0`/"egenv.jar $@
 */
public class EGenVAR_CMD2 {

    public final static double REQUIRED_JAVA_VERSION = 1.7;
    public String defualt_host_wsdl = null;//"http://localhost:8086/eGenVar_WebService/Authenticate_service?wsdl";
//    public String host_wsdl = "https://ans-180228.stolav.ntnu.no:8181/eGenVar_WebService/Authenticate_service?wsdl";
//    public String host_wsdl = "http://localhost:8086/eGenVar_WebService/Authenticate_service?wsdl";
//    public String host_wsdl = "http://localhost:8080/eGenVar_WebService/Authenticate_service?wsdl";
//    public String host_wsdl = "http://localhost:8085/eGenVar_WebService/Authenticate_service?wsdl"; 
    public final static double version = 4.000;
    public final static String RELEASE_DATE = "18-Feb-2015";
    public String DATABASE_NAME_DATA = "EGEN_DATAENTRY";
    public int PROGRESS_MONT_INTERVAL = 15000;
    public final String PROGRESS_MONT_INTERVAL_FLAG = "-interval";
    public int split_limit = 500;
    private int op_type = 0;
//    private static CharSequence ADAVNCED_RELATE_FLAG = "search";
//    private static CharSequence ADAVNCED_RELATE_EXACT_FLAG = "search_exact";
    private static String TO_CHECK_AGAINST_DB_FLAG = "<tocheckwithdb>";
    private static String NAME_TO_CHECK = "<NAME_TO_CHECK>";
    private final String authfilenm = "egenvarauth.aut";
    private static final String PREPARE_TO_ADD_CUSTOM_TABLE_FILENM = ".egenvar_describe_<TABLE>.txt";
    private static final String PREPARE_TO_ADD_RELATIIONSHIPS_TABLE_FILENM = ".egenvar_describe_relate_<TABLE>.txt";
    private static final String EGENVAR_UNDO_FILENM = "_egenvundo.txt";
    private static final String ID_COLUMN_COMMENT_FLAG = "****";
    private final String DATE_FORMAT_1 = "yyyy-MM-dd HH:mm:ss";
    private java.text.SimpleDateFormat sdf_2;
    private String user_email;
    private static final String VERSION_FLAG = "-v";
    private static final String VERSION_REFRESH_FLAG = "-vv";
    private static final String VERSION_FLAG2 = "-version";
    private static final String HELP_FLAG = "-help";
    private static final String HELP_FLAG2 = "-h";
    private static final String AUTHENTICATE_FLAG = "-authenticate";
    private static final String REAUTHENTICATE_FLAG = "-reauthenticate";
    private static final String LOCAL_FOLDER_AUTH_FLAG = "-local";
    private static final String DIAGNOSTICS_FLAG = "-diagnose";
    private static final String FORCE_FLAG = "-force";
    private static final String UPGARE_FLAG = "-upgrade";
    private static final String VERBOSE_FLAG = "-verbose";
    private static final String PREPARE_TO_ADD_FILES_FLAG = "-prepare";
    private static final String PREPARE_TO_ADD_FILES_RECURSIVLY_FLAG = "-recurse";
    private static final String PREPARE_TO_ADD_USING_CURRENT_AS_BACTH = "-current_as_batch";
    private static final String PREPARE_TO_ADD_USING_CURRENT_AS_BACTH_LIST = "-current_as_batch_list";
    private static final String UPDATE_CHANGE_SERVER = "-changehost";
    private static final String CLEAR__FLAG = "-clean";
    private static final String TEMPLATE_FLAG = "-template";
    private static final String OMIIT_LABLES_FLAG = "-nolables";
    private static final String OMIIT_LABLES_FLAG2 = "-nolable";
    private static final String PREPARE_TAGS_FLAG = "-tag";
    private static final String PREPARE_TAGS_FLAG2 = "-tags";
    private static final String CREATE_TAG_SOURCE = "-create_tag_source";
    private static final String SEARCH_FLAG = "-search";
    private static final String SEARCH_FLAG2 = "-find";
    private static final String USE_ALL_FLAG = "-all";
    private static final String SORT_FLAG = "-sort";
    private static final String GROUP_RESULTS = "-group";
    private static final String GET_AVAILABLE_TABLES_FLAG = "-available";
    private static final String SKIP_DB_TEST_FLAG = "-skipdb";
    private static final String SKIP_files_FLAG = "-skipfile";
    private static final String SKIP_LARGE_FILES_FLAG = "-skipsize";
    private static final String SKIP_SMALL_FILES_FLAG = "-skipsmaller";
    private static final String SKIP_archives_FLAG = "-skipzip";
    private static final String SKIP_CHECKUSM = "-skipcheksum";
    private static final String FORCE_CONFIG_OUT_PUT_LOCATION_FLAG = "-configout";
    private static final String INVESTIGATE = "-investigate";
    private static final String SPLIT_LIMIT_FLAG = "-split_limit";
    private static final String USE_ALLDEFAULT_VALUES_FLAG = "-default";
    private static final String UPDATE_CURRENT = "-current";
    private static final String RENAMING_FOLDERS_REQUEST = "-renametomatch";
    private static final String FORCE_CONFIG_IN_PUT_LOCATION_FLAG = "-configin";
    private static final String NOW = "-now";
    private static final String FORCE_IN_PUT_LOCATION_FLAG = "-in";
    private static final String UPDATE_FLAG = "-update";
    private static final String SKIP_FORMATING_FLAG = "-dontformat";
    private static final String SKIP_PROGRESS = "-skipprogress";
    private static final String SKIP_NEW_LINE_FLAG = "-sameline";
    private static final String SEARCH_DISTINCT_FLAG = "-unique";
    private static final String SEARCH_EXACT_FLAG = "-exact";
    private static final String EXISTS_FLAG = "-exists";
    private static final String CHILDREN_BEFORE = "-expand_query";//1
    private static final String CHILDREN_AT_THE_END = "-expand_results";
    private static final String CHILDREN_AT_DUING = "-expand_intermediate";
//    private static final String CHILDREN_BEFORE = "-children_before";//1
//    private static final String CHILDREN_AT_THE_END = "-children_end";
    private static final String CHILDREN_AT_THE_END2 = "-children_after";
//    private static final String CHILDREN_AT_DUING = "-children_during";
    private static final String PARENT_AT_END = "-parent";
//    private static final String CONSIDER_AT_THEEND = "-children_end";
//    private static final String CONSIDER_PARENTS = "-extend_to_parent";
    private static final String HTML_OUT = "-html";
    private static final String EXPAND_TO_FOREING_FLAG = "-expand";
//    private static final String SEARCH_MAX_FLAG = "-max";
//    private static final String SEARCH_MIN_FLAG = "-min";
    private static final String ADD_FILES_FLAG = "-add";
    private static final String RELATE_FILES_FLAG = "-relate";
    private static final String FROM_FILE_FLAG = "-file";
    private static final String FROM_FILEX_FLAG = "-filex";
    private static final String NOGROUP = "-dontgroup";
    private static final String OUT_PUT_FILE_FLAG = "-out";
//    private static final String ADD_OR_PREPARE_RELATIONSHIP_FLAG = "-relate";
    private static final String UNDO_FLAG = "-undo";
    private static final String DOWNLOAD_FILES_FLAG = "-download";
    private static final String DELETE_FLAG = "-delete";
    private static final String OVERIDE_FLAG = "-override";
    private static final String MEERGE_FLAG = "-merge";
    private static final String RELATIONSHIP_TYPE_FLAG = "relationship_type=";
    private static final String RELATIONSHIP_USIGN_FLAG = "relationship_using=";
    private static final String DIFF_FLAG = "-diff";
    private static final String REQUEST_ACCOUNT = "-request_account";
    private static final String CREATE_HIERARCHY = "-hierarchy";
    private static final String DEPTH = "-depth";
    private String authentication_string;
    private static final String FILES_TABLE_FLAG = "files";
    private static final String FILES_TABLE_NAME_FLAG = "FILES.NAME";
    private static final String FILES_TABLE_CHECKSUM_FLAG = "FILES.CHECKSUM";
    private static final String HIERARCHY_FLAG = "_HIERARCHY";
    private final String AUTH_FLAG = "authenticID=";
    private ArrayList<String> allowed_dummy_alues;
    private boolean do_not_use_any;
    private boolean use_all;
    private static String commnetstext = "";
    private int comment_line_length = 80;
    private int results_line_length = 256;
    private final int MINIMUM_DESCRIPTION_LENGTH = 4;
    private final static int MINIMUM_SPLIT_FOR_ADDING_FILES = 500;
//    private static ArrayList<String> hierarchy_tables_l;
    private static ArrayList<String> force_template_tables_tables_l;
    //SCP:dmed4929:/home/sabryr/tmp/rand/batch_8/uy/genrated_2287.txt
    private final String ACCEPTED_FILE_NAME_PATTERN = "(.{3}):(.+):(.+)";
    private Pattern pttern_file_name_1;
    private int files_analysed = 0;
    private boolean authenticated = false;
//      rm /home/sabryr/glassfish-3.1.1/glassfish/domains/domain1/docroot/egenv.jar.gz;
//     cp /home/sabryr/NetBeansProjects/egenv/dist/egenv.jar /home/sabryr/glassfish-3.1.1/glassfish/domains/domain1/docroot/;
//     gzip /home/sabryr/glassfish-3.1.1/glassfish/domains/domain1/docroot/egenv.jar
//    **Crearte the egenv.tar.gz
//    rm  /home/sabryr/NetBeansProjects/egenv/egenv/egenv.jar
//    cp /home/sabryr/NetBeansProjects/egenv/dist/egenv.jar  /home/sabryr/NetBeansProjects/egenv/egenv/
//    cd /home/sabryr/NetBeansProjects/egenv/
//    tar czf egenv.tar.gz egenv
//     rm  /home/sabryr/glassfish-3.1.1/glassfish/domains/domain1/docroot/egenv.tar.gz; 
////    cp /home/sabryr/NetBeansProjects/egenv/egenv.tar.gz /home/sabryr/glassfish-3.1.1/glassfish/domains/domain1/docroot/; 
//     chmod  644  /home/sabryr/glassfish-3.1.1/glassfish/domains/domain1/docroot/egenv.tar.gz
//    
//    private String READFROM_FILE_FLAG = "-f";
    private static HashMap<String, String> Helpmap;
    private String server_name = null;
    private static final String FILES_NAME = "FILES.NAME";
    public static final String REPORT_NAME = "REPORT.NAME";
    public static final String REPORT_BATCH_NAME = "REPORT_BATCH.NAME";
    public static final String FILES_DESCRIPTION = "FILES.DESCRIPTION";
//    private static final String FILESTYPE_DESCRIPTION_NAME = "FILETYPE.NAME";
//    private static final String FILESTYPE_DESCRIPTION_DESCRIPTION = "FILETYPE.DESCRIPTION";
    private static final String REPORT_DESCRIPTION = "REPORT.DESCRIPTION";//report.description
    private static final String REPORT_BATCH_DESCRIPTION = "REPORT_BATCH.DESCRIPTION";//"report_batch.description"
//    private static final String PERSON_NAME = "PERSON.EMAIL";
//    private static final String REPORT_TYPE_NAME = "REPORTTYPE.NAME";
    private static final String REPORT_ENTRYDATE = "REPORT.ENTRYDATE";
//    private static final String FILESTYPE_DESCRIPTION_TYPE = "FILETYPE.TYPE";
//    private static final String FILESTYPE_DESCRIPTION_FILEEXTENSION = "FILETYPE.FILEEXTENSION";
    private static boolean localfolderauth = false;
    public static final String FILES_CHECKSUM = "FILES.CHECKSUM";
    public static final String FILES_ID = "FILES.ID";
    public static final String FILEPATH_FILES_ID = "FILES2PATH.FILES_ID";
    public static final String FILES_LASTMODIFIED = "FILES2PATH.LASTMODIFIED";
    public static final String FILES_SIZE = "FILES.FILESIZE";
    public static final String FILES_OWNERGROUP = "FILES2PATH.OWNERGROUP";
    public static final String FILES_LOCATION = "FILES2PATH.LOCATION";
    public static final String FILES2PATH_FILEPATH = "FILES2PATH.FILEPATH";
    public static final String HOSTNAME = "HOSTNAME";
    public static final String SAMPLEDETAILS_NAME = "SAMPLEDETAILS.NAME";
    public static final String PERSON_EMAIL = "PERSON.EMAIL";
    public static final String DONORDETAILS_NAME = "DONORDETAILS.NAME";
    private final String RESULT_SPLITER = "\\|\\|";
//    private static final String CODE_SEARCH_FLAG = "CODE";
    public static final long warning_limit = new Long("536870912");
    public static final String DIRECTORY_MARKER_REPORT = ".EGENVAR_REPPORT_MARKER";
    public static final String DIRECTORY_MARKER_REPORT_BATCH = ".EGENVAR_REPPORT_BATCH_MARKER";
    public final static long MEM_WARN = 268435456;
    private final long MEM_terminate = 8388608;
    private final String UPDATE_FILE_FLAG = "UPDATE";
    private final String ADD_FILE_FLAG = "ADD";
    private static int opmode = 0;

//
//  public EGenVAR_CMD2(String a) {
//      
//  }
    /*
     * MethodID= 0
     */
    public EGenVAR_CMD2(int opmode) {
        this.opmode = opmode;

        sdf_2 = new java.text.SimpleDateFormat(DATE_FORMAT_1);
        sdf_2.setTimeZone(TimeZone.getDefault());
        Helpmap = new HashMap<>();
        Helpmap.put("-v", "(or -version) print version and check for updates");
        Helpmap.put("-authenticate", "Authenticate user against server. This will create a local certificate for tha current user");
        Helpmap.put("-prepare", "Prepare for adding. This will create description files for batch, report and files (e.g." + PREPARE_TO_ADD_CUSTOM_TABLE_FILENM + ")."
                + "¤-prepare (Prepare for adding files, reports, report_batches all at the same time)."
                + "¤-prepare -clean (clean all previous prepare inforamtion before preparing)."
                + "¤-prepare -update (prepare for updating).¤-prepare -template (prepare a template to enter data)."
                + "¤-prepare -clean -skipdb (check the local files without comparing with the database, can be used to find duplicate files)"
                + "¤-prepare -tag (this will start a step by step process to tag entities)");
        Helpmap.put("-add", "-add <files> (add files, create reports, report_batches and file type descriptions as described in the description files created during prepare step).¤-add -all(add all files in the current directory).¤-add *sample1* (add all files with sample1 in their name).¤-add -file <file name> (add the files included in the file with the name <file name>).¤-add -template <template_file_name> (add the templete content).¤-add -relationship <relationship file> (add the relationships described in the file)");
        Helpmap.put("-help", " (or -h) Printhelp");
        Helpmap.put("?", " Print help for a specific command.¤E.g. to get help for the command -add use -?add");
        Helpmap.put("-diagnose", "Check for problems in authentication");
        Helpmap.put("-undo", "Undo committed updates. Format: -undo<undo_point_file> ");
        Helpmap.put("-diff", "Check whether there were any changes to the diposited files compared to the information in the database.¤Format: -diff ");

        Helpmap.put("-search", "-search <search_term>:Find files which contains the search term.¤-search -all: List all files in the database.¤"
                + "-search -available: List tables, fileds and a short description that can be searched.¤"
                + "-search  <table name>=<search term> :Find entries from the table with name equals to search term.¤"
                + "-search <table name>.<table field>=<search text>  <target_table>: find entries in the target table, which are connected to the entries "
                + "returned from the source table. (e.g. use this to get all files (target) containg data from a certian donor (source))."
                + "¤§¤*To find entities matching any condition,seperate multiple conditions with || and enclose the entire query in quotes (e.g. -search files.name=\"genrated_341.txt||genrated_813.txt\")¤"
                + "¤§¤*To find entities matching all conditions seperate the multiple conditions with a && and enclose the query in quates (e.g. -search \"files.name=genrated_341.txt&&files.OWNERGROUP=lab1\")¤"
                + "*To get only exact matches use the additional flag -exact.¤§¤Examples:¤-search files.name=exp1¤*This will return all details of files with exp1"
                + " in their name.¤-search report.name=rep1 files¤*This will return all details of files belongs to the report with the name rep1.¤-search -exact "
                + "report.name=rep1_exp34¤*This will return all details for the report with the exact name rep1_exp34.");

        Helpmap.put("-template", "To create templates or add content from a template.¤-prepare -template (to create templete).¤ -add -template <template_file-name> (to add the contents of a templete file)");
        Helpmap.put("-update", "Issue -update without any arguments to check the current directory against database.To create update templates or commit updates.¤-prepare -update (to create update templete).¤ -prepare -update <update_template_file_name> (to commit the updates in the templete file)");
        Helpmap.put("-undo", "To undo a add operation.¤format -undo <undo_point_file_name> (to undo all committed unpdates (if possible) to the database described in the undo point file)");
        Helpmap.put("-configout", "Use with the -prepare to overide the default output location for description files. This argument should be followed by a location, which is directory (not a file)");
        Helpmap.put("-configin", "Use with the -add or -update to overide the default input location for description files. This argument should be followed by a location, which is directory (not a file)");
        Helpmap.put("-in", "Use with the -prepare , -update or -delete to overide the default processing location");
        Helpmap.put("-delete", "To delete content. use -delete -prepare to create templete to use. Then use -delete -<template_file> to execute the deletion");
        Helpmap.put("-skipdb", "Used with -prepare");
        Helpmap.put("-clean", "Used with -prepare");
        Helpmap.put("-available", "Used with -search");
        Helpmap.put("-exact", "Used with -search");
        Helpmap.put("-teamplate", "Used with -prepare or -add");
        Helpmap.put("Redirect_input_or_output", "Use " + FORCE_CONFIG_OUT_PUT_LOCATION_FLAG + " or " + FORCE_CONFIG_IN_PUT_LOCATION_FLAG);
        Helpmap.put(FORCE_IN_PUT_LOCATION_FLAG, "Overide_location_to_process, Used with -add");
        Helpmap.put("-skipdbcheck", "Used with -prepare to avoid comparing against the database. Used to find local duplicates");
        Helpmap.put("-skipzip", "Used with -prepare to avoid processing zip , gzip and tar files");
        Helpmap.put("-all", "Used with -add , same as using the -add instruction without any arguments");
        Helpmap.put("WSDL", "This is the location of the Web Service Definition Language xml file. This file contains details about the available services on the server. The index page of the web interface will have the correct link generated for your server.If you are running the tool "
                + "from the same machine where the egenvar server is installed and the port of the server is 8085, then the address of the WASDL would be ¤http://localhost:8085/eGenVar_WebService/Authenticate_service?wsdl"
                + ". ");
        Helpmap.put(USE_ALLDEFAULT_VALUES_FLAG, "used with -prepare. This will trigger the usage of default values without first confiming with the user.");
        Helpmap.put(PREPARE_TO_ADD_USING_CURRENT_AS_BACTH, "Used with -recurs -prepare. To use the current directory as the bacth name and the direcotries or simbolic links in it as report names");
        Helpmap.put(PREPARE_TO_ADD_FILES_RECURSIVLY_FLAG, "Used with  -prepare.  Add all files by performing a directory walk, assign report and report batch names using folder names");
        Helpmap.put(OMIIT_LABLES_FLAG, "Used with  -search.  to avoid printing column headers");
        Helpmap.put(OMIIT_LABLES_FLAG2, "Used with  -search.  to avoid printing column headers");


        allowed_dummy_alues = new ArrayList<>(3);
        allowed_dummy_alues.add("NA");
        ArrayList<String> commnet_l = new ArrayList<>(5);
        commnet_l.add("#The fields with " + ID_COLUMN_COMMENT_FLAG + " are identifiers assigned by the database. Positive value indicates that the "
                + "entry already there in the database and none of the values in the same row should be edited. Only rows which has a negative value "
                + "for this field should be edited. However, for intentional change of associations, values could be edited, i.e. attach"
                + " a file to multiple reports");
        commnet_l.add("#The fields with *   are compulsory and can not be left blank.");
        commnet_l.add("#The fields with **  are compulsory and value are pre-selected.These values should not be modified");
        commnet_l.add("#The fields with *** are compulsory and refer to existing entries in the database. Please make sure the entry exist in the database. If a value entered here was not found, an attempt would be made to first create an entry with the value.");
        commnet_l.add("#For non compulsory fields ,use NA when you do not a have value.");

//        commnetstext = commnetstext + "\n#_______________________________";
        commnetstext = printformater_file(commnet_l, comment_line_length);
//        hierarchy_tables_l = new ArrayList<String>(5);
//        hierarchy_tables_l.add("files_hierarchy");
//        hierarchy_tables_l.add("report_hierarchy");
//        hierarchy_tables_l.add("report_batch_hierarchy");
//        hierarchy_tables_l.add("sampledetails_hierarchy");
//        hierarchy_tables_l.add("donordetails_hierarchy");

        force_template_tables_tables_l = new ArrayList<>(1);
        force_template_tables_tables_l.add("files");
        force_template_tables_tables_l.add("FILES");
    }

    /**
     * MethodID=1
     *
     * @param args the command line arguments Method 2 /*to use secure port
     * System.setProperty("javax.net.ssl.trustStore", "<Path-Of-Cacerts>");
     * System.setProperty("javax.net.ssl.trustStorePassword", "changeit");
     */
    public static void main(String[] args) {

        boolean[] instructs = new boolean[2];
        long start = Timing.setPointer();
        double java_version = 0;
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
        if (java_version > 0 && java_version < REQUIRED_JAVA_VERSION) {
            printout(1, null, "Error 2b: Your JAVA version " + full_java_version + " is older than the minimum (" + REQUIRED_JAVA_VERSION + ") requirement.\nTerminating..\n");
        } else {
            if (!(java_version > 0)) {
                printout(1, null, "Error 2c: Failed to ditect JAVA version. This software may not function normally in this environment.");
            }
            EGenVAR_CMD2 cmd = new EGenVAR_CMD2(0);
            instructs = cmd.start(args);
        }
        if (!instructs[1]) {
            printout(0, null, "Ended in  " + Timing.convert(Timing.getFromlastPointer(start)));
        }
    }

//    private void keyRing(String filenm, char[] storePass, String alias, char[] entryPass_in) {
//        try {
//            ProtectionParameter entryPass = new KeyStore.PasswordProtection(entryPass_in);
//            KeyStore store = KeyStore.getInstance("SunX509");
//            InputStream input = new FileInputStream(filenm);
//            store.load(input, storePass);
//            KeyStore.Entry entry;
//            entry = store.getEntry(alias, entryPass);
//             printout(0,null, entry);
//        } catch (UnrecoverableEntryException ex) {
//            Logger.getLogger(EGenVAR_CMD2.class.getName()).log(Level.SEVERE, null, ex);
//
//        } catch (KeyStoreException ex) {
//            ex.printStackTrace();
//        } catch (FileNotFoundException ex) {
//            Logger.getLogger(EGenVAR_CMD2.class.getName()).log(Level.SEVERE, null, ex);
//        } catch (IOException ex) {
//            Logger.getLogger(EGenVAR_CMD2.class.getName()).log(Level.SEVERE, null, ex);
//        } catch (NoSuchAlgorithmException ex) {
//            Logger.getLogger(EGenVAR_CMD2.class.getName()).log(Level.SEVERE, null, ex);
//        } catch (CertificateException ex) {
//            Logger.getLogger(EGenVAR_CMD2.class.getName()).log(Level.SEVERE, null, ex);
//        }
//    }
//    http://www.mayrhofer.eu.org/create-x509-certs-in-java
    /*
     MethodID=1
     */
    private boolean[] start(String[] args) {
        boolean[] instructs = new boolean[2];
        boolean verbosemode = false;
        boolean skip_formating = false;
//        String cacerts_jks_file = "cacerts.jks";
//        String keystore_file = "keystore.jks";
//        System.setProperty("javax.net.ssl.keyStore", cacerts_jks_file);
//        System.setProperty("javax.net.ssl.trustStore", keystore_file);
//        System.setProperty("javax.net.ssl.trustStorePassword", "changeit");
//        System.setProperty("javax.net.ssl.keyStorePassword", "changeit");

        if (args != null && args.length > 0 && (args[0].startsWith("-") || args[0].startsWith("?"))) {
            String first_arg = args[0].trim();
            int upgradeDecideder = rand(1, 4);
            if (upgradeDecideder == 3 && !first_arg.equalsIgnoreCase(VERSION_REFRESH_FLAG)) {
                if (test(null, 1)) {
                    double latest_version = getLatestVersion(null);
                    if (latest_version > version) {
                        printout(0, null, "\nA newer version is available. Latest version=" + latest_version + " Please update (egenv " + UPGARE_FLAG + ")");
                    }
                }
            }
            ArrayList<String> paramList = new ArrayList<>(args.length);
            for (int i = 0; i < args.length; i++) {
                String c_arg = args[i].trim();
                if (!c_arg.isEmpty()) {
                    paramList.add(c_arg.replaceAll("\\\\s", " "));
                }
            }
            if (paramList.remove(LOCAL_FOLDER_AUTH_FLAG)) {
                localfolderauth = true;
            }

            if (first_arg.equalsIgnoreCase("-test")) {
//                 printout(0,null, "355 " + paramList);
//                createCertificate(paramList.get(1), cacerts_jks_file, new Integer(paramList.get(3)), "changeit".toCharArray());
//                System.setProperty("javax.net.ssl.keyStore", cacerts_jks_file);
//                System.setProperty("javax.net.ssl.trustStore", keystore_file);
//                System.setProperty("javax.net.ssl.trustStorePassword", "changeit");
//                System.setProperty("javax.net.ssl.keyStorePassword", "changeit");
//                 printout(0,null, "355 " + paramList);
//                createCertificate(paramList.get(1), paramList.get(2), new Integer(paramList.get(3)), "changeit".toCharArray());
//                 printout(0,null, "329 " + getLatestVersion(null));
//                 printout(0,null, "330 " + getUniqueKeys(args[1]));
//                try {
//                    GetCertificate png = new GetCertificate();
////                    png.main(args);
//                                      
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
                //                ct.getChannel(args[1], args[2], args[3], new Integer(args[4]));
//                keyRing(args[1], "changeit".toCharArray(), "smtp.gmail.com", null);
//                String[] r_a = getFileDetails(args[1], true);
//                for (int i = 0; i < r_a.length; i++) {
//                     printout(0,null, r_a[i]);
//
//                }
            } else if (first_arg.equalsIgnoreCase("-API")) {
                paramList.remove("-API");
                if (paramList.remove("-search")) {
                    boolean exact = false;
                    int qType = 0;
                    for (int i = 0; i < paramList.size(); i++) {
                        if (paramList.get(i).equalsIgnoreCase("TRUE")) {
                            exact = true;
                            paramList.remove(i);
                            i--;
                        } else if (paramList.get(i).equalsIgnoreCase("FALSE")) {
                            exact = false;
                            paramList.remove(i);
                            i--;
                        } else if (paramList.get(i).matches("[0-9]{1}")) {
                            if (paramList.get(i).matches("[0-5]{1}")) {
                                qType = new Integer(paramList.get(i));
                            } else {
                                qType = 0;
                            }
                            paramList.remove(i);
                            i--;
                        }
                    }
                    if (paramList.isEmpty()) {
                        printout(0, null, "Error:invalid query");
                    } else {
                        if (paramList.size() > 1) {
                            String[] result = search(paramList.get(0), paramList.get(1), exact, qType);
                            for (int i = 0; i < result.length; i++) {
                                printout(0, null, result[i]);
                            }
                        } else {
                            String[] result = search(paramList.get(0), "TAG", exact, qType);
                            for (int i = 0; i < result.length; i++) {
                                printout(0, null, result[i]);
                            }
                        }
                    }

                } else if (paramList.remove("-tag") && paramList.size() >= 2) {
                    String target = paramList.get(0);
                    if (!paramList.get(0).contains("=")) {
                        Path p = Paths.get(paramList.get(0));
                        if (Files.exists(p) && Files.isReadable(p)) {
                            ArrayList<String> tmp_l = new ArrayList<>();
                            tmp_l.add(p.toAbsolutePath().toString());
                            HashMap<String, String> getHashes_map = getHashes(tmp_l, -1, -1, false);
                            target = "FILES.CHECKSUM=" + getHashes_map.get(tmp_l.get(0));
                        }
                    }
                    printout(0, null, "475 result=" + tag(target, paramList.get(1)));
                } else if (paramList.remove("-add") && paramList.size() >= 1) {
//                    printout(0, null, "475 " + paramList);
                    System.out.println("Result = " + add(paramList.get(0)));
//                    Path p = Paths.get(paramList.get(0));
//                    if (Files.exists(p) && Files.isReadable(p)) {
//                        ArrayList<String> tmp_l = new ArrayList<>();
//                        tmp_l.add(p.toAbsolutePath().toString());
//                        HashMap<String, String> getHashes_map = getHashes(tmp_l, -1, -1, false);
//                        HashMap<String, HashMap<String, String>> files2cdetails_map = (getFileDetails(tmp_l, null, null));
//                        getHashes_map.put("FILES.CHECKSUM", getHashes_map.remove(tmp_l.get(0)));
//                        getHashes_map.putAll(files2cdetails_map.get(tmp_l.get(0)));
//                        getHashes_map.remove("HOSTNAME");
//                        System.out.println("488 getHashes_map=" + getHashes_map);
//                        System.out.println("489 " + map2list(getHashes_map, true));
//                        List result_l = setUsingTemplate(map2list(getHashes_map, true));
//                        if (result_l == null || result_l.size() != 3) {
//                            printout(1, null, "Error 198: Connection to the server failed");
//                        } else {
//                            String result = (String) result_l.get(0);
//                            String undo = (String) result_l.get(1);
//                            String messages = (String) result_l.get(2);
//                            printout(0, null, result);
//                            if (!messages.isEmpty()) {
//                                printout(0, null, "messages=" + messages);
//                            }
////                            if (undo != null) {
////                                printout(0, null, "UNDO info: " + undo);
////                            }
//                        }
//                    }
                } else if (paramList.remove(RELATE_FILES_FLAG) && !paramList.isEmpty()) {
                    //egenv.sh -API -relate "8252f6dfac96c0a2c46f58480e032073fead0a35||2dc643a644622d49765d3e6b18e6cddb6de8fd74" "FILES" "FILES.CHECKSUM"
//                    Stri<ng[] relationships_a = paramList.get(0).trim().split(",");
//                    ArrayList<String> relationships_l = new ArrayList<>(Arrays.asList(relationships_a));
//                    String type = paramList.get(1).trim();
//                    String using_type = paramList.get(2).trim();

//                    if (relationships_a != null && !relationships_l.isEmpty() && type != null && using_type != null) {
//                        String result = call_AddRelationshipFromFile(relationships_l, type, using_type, null, false);
                    if (paramList.size() == 1) {
                        System.out.println(relate(paramList.get(0), null, null));
                    } else if (paramList.size() == 2) {
                        System.out.println(relate(paramList.get(0), paramList.get(1).trim(), null));
                    } else if (paramList.size() > 2) {
                        System.out.println(relate(paramList.get(0), paramList.get(1).trim(), paramList.get(2).trim()));
                    }

//                    } else {
//                        System.out.println("Error: needs exactly three parameter");
//                    }

                } else if (paramList.remove("-update") && !paramList.isEmpty()) {
                    System.out.println(update(paramList));
                } else if (paramList.remove("-delete") && !paramList.isEmpty()) {
                    String[] result_a = null;
                    if (paramList.size() == 1) {
                        result_a = delete(paramList.get(0), "NA");
                    } else {
                        result_a = delete(paramList.get(0), paramList.get(1));
                    }
                    if (result_a != null) {
                        for (int i = 0; i < result_a.length; i++) {
                            System.out.println(result_a[i]);

                        }
                    } else {
                        System.out.println("Error 577");
                    }

                } else {
                    printout(0, null, "-API No match");
                }

            } else if (first_arg.equalsIgnoreCase(REQUEST_ACCOUNT)) {
                paramList.remove(REQUEST_ACCOUNT);
                String email = null;
                String code = null;
                String wsdl = null;
                if (paramList.isEmpty()) {
                    String ans = getuserInputSameLine("What is you email address (this will be your user name) ", "(press C to cancel or provide email)");
                    email = analyseUserResponse(ans, "[A-z0-9_\\-\\.]+@[A-z0-9_\\-]+[\\.]{1}[A-z0-9_\\-\\.]+");
                } else if (paramList.size() == 1) {
                    email = paramList.get(0);
                    String ans = getuserInputSameLine("What is the WSDL file URL (unique to the server your are connecting) ?", "(press C to cancel or provide a URL)");
                    wsdl = analyseUserResponse(ans, ".+:[0-9]+.+wsdl");
                } else if (paramList.size() == 2) {
                    email = paramList.get(0);
                    if (paramList.get(1).toUpperCase().startsWith("WSDL=")) {
                        wsdl = paramList.get(1).substring(paramList.get(1).indexOf("=") + 1);
                    } else {
                        code = paramList.get(1);
                    }
                } else {
                    email = paramList.get(0);
                    for (int i = 1; i < paramList.size(); i++) {
                        if (paramList.get(i).toUpperCase().startsWith("WSDL=")) {
                            wsdl = paramList.get(i).substring(paramList.get(i).indexOf("=") + 1);
                        } else {
                            code = paramList.get(i);
                        }
                    }
                }
                if (email != null) {
                    createuseraccount(email, code, wsdl);
                } else {
                    printout(0, null, "Error: email was null");
                }
            } else if (first_arg.equalsIgnoreCase("-random")) {
                String ans = getuserInputSameLine("This will create a set of renadom files in the current directory for testing puposes. Do you want to continue? ", "[Y-yes|N-no]");
                if (analyseUserResponse(ans, true, false) == 0) {
                    Random rand = new Random();
                    int dir_count = rand.nextInt(10);
                    ans = getuserInputSameLine("Maximum number of files per directory (there will be " + dir_count + " directories) ", "");
                    if (ans.matches("[0-9]+")) {
                        String num = getuserInputSameLine("Maximum size in MB ", "");
                        if (ans.matches("[0-9]+")) {
                            createRandom(new Integer(ans), new Integer(num));
                        } else {
                            printout(0, null, "Invalid integer");
                        }
                    } else {
                        printout(0, null, "Invalid integer");
                    }

                }
            } else if (args.length == 1 && first_arg.equalsIgnoreCase(CLEAR__FLAG)) {
                String ans = getuserInputSameLine("Do you really want to delete all egenvar generated files (this action can not be undone)? ", "[Y-yes|N-no]");
                if (analyseUserResponse(ans, true, false) == 0) {
                    String c_dir = getPWD();
                    if (c_dir != null) {
                        File c_dir_f = new File(c_dir);
                        if (c_dir_f.isDirectory() && c_dir_f.canWrite()) {
                            File[] file_a = c_dir_f.listFiles();
                            for (int i = 0; i < file_a.length; i++) {
                                String c_nm = file_a[i].getName();
                                if (c_nm.contains(".egenvar_describe")) {
                                    printout(0, null, "Deleting " + c_nm);
                                    file_a[i].delete();
                                } else if (c_nm.contains("_egenvundo")) {
                                    printout(0, null, "Deleting " + c_nm);
                                    file_a[i].delete();
                                }
                            }
                        } else {
                            printout(0, null, "Error: no write access for " + c_dir);
                        }
                    }
                } else {
                    printout(0, null, "Exiting on user request. No changes performed");
                }

            } else if (first_arg.contains("??")) {
                if (args.length > 1) {
                    first_arg = args[1];
                } else {
                    first_arg = first_arg.replaceAll("\\?", "");
                }
                printout(0, null, first_arg + "____\n" + printformater_terminal(getHelp(Helpmap, first_arg), 80));
            } else if (first_arg.contains("?")) {
                if (args.length > 1) {
                    first_arg = args[1];
                } else {
                    first_arg = first_arg.replace("?", "");
                }
                if (!first_arg.startsWith("-")) {
                    first_arg = "-" + first_arg;
                }
                String help_txt = getIgnoreCase(Helpmap, first_arg);

                if (help_txt != null) {
                    printout(0, null, "___Help_for__" + first_arg + "____\n" + printformater_terminal(help_txt, 80));
                } else {
                    printout(0, null, "No help found for " + first_arg + ". Available options:\n" + printformater_terminal(Helpmap.keySet().toString(), 80));

                }
            } else if (first_arg.equalsIgnoreCase(HELP_FLAG) || first_arg.equalsIgnoreCase(HELP_FLAG2)) {
                printValidArguments(false);
            } else if (first_arg.equalsIgnoreCase(VERSION_FLAG) || first_arg.equals(VERSION_FLAG2)) { //
                printout(0, null, "eGenVar command line interface version " + version + " released on=" + RELEASE_DATE);
                if (test(null, 2)) {
                    double latest_version = getLatestVersion(null);
                    if (latest_version < 0) {
                        printout(0, null, "Error 1A: Communication error, getting latest version failed");
                    } else if (latest_version > version) {
                        String ans = getuserInputSameLine("A newer version is available. Latest version=" + latest_version, " Upgrade now [Y-yes|N-no]:");
                        if (analyseUserResponse(ans, true, true) == 0) {
                            updateSoftware(false);
                        }
                    } else {
                        printout(0, null, "Your software is uptodate !");
                    }
                } else {
                    printout(0, null, "Error: webservices not available or the address of the registerd server has changed, use egenv.sh -diagnose");
                }
            } else if (first_arg.equalsIgnoreCase(VERSION_REFRESH_FLAG)) {
//                refresh_version();
                printout(0, null, "Depricated, use the -v instruction");
            } else if (first_arg.equalsIgnoreCase(AUTHENTICATE_FLAG)) {
                if (authenticate(false, null)) {
                    printout(0, null, "Local authentication success. ");
                    if (check_authentication()) {
                        printout(0, null, "Authentication aginst server success.");
                    } else {
                        String ans = getuserInputSameLine("Authentication aginst server failed. Do you want to run diagnostics?", "[Y-yes|N-no]");
                        if (analyseUserResponse(ans, true, false) == 0) {
                            diagnostics(1, null);
                        }
                        if (!check_authentication()) {
                            printout(0, null, "\nError : Authentication aginst server failed. Run " + DIAGNOSTICS_FLAG + "\n");
                        }

                    }
                } else {
                    printout(0, null, "Authentication failed");
                }

            } else if (first_arg.equalsIgnoreCase(REAUTHENTICATE_FLAG)) {
                if (authenticate(true, null)) {
                    printout(0, null, "Local authentication success. ");
                    if (check_authentication()) {
                        printout(0, null, "Authentication aginst server success.");
                    } else {
                        String ans = getuserInputSameLine("Authentication aginst server failed. Do you want to run diagnostics?", "[Y-yes|N-no]");
                        if (analyseUserResponse(ans, true, false) == 0) {
                            diagnostics(1, null);
                        }
                        if (!check_authentication()) {
                            printout(0, null, "\nError : Authentication aginst server failed. Run " + DIAGNOSTICS_FLAG + "\n");
                        }

                    }
                } else {
                    printout(0, null, "Authentication failed");
                }

            } else if (first_arg.equalsIgnoreCase(DIAGNOSTICS_FLAG)) {
                diagnostics(1, null);
            } else if (first_arg.equalsIgnoreCase(UPGARE_FLAG)) {
                boolean force = false;
                if (args.length > 1 && args[1].equals(FORCE_FLAG)) {
                    force = true;
                }
                if (authenticate(false, null)) {
                    updateSoftware(force);
                }
            } else {
                if (authenticate(false, null)) {
                    if (!paramList.isEmpty()) {
                        if (paramList.remove(VERBOSE_FLAG)) {
                            verbosemode = true;
                        }
                        if (paramList.contains(SPLIT_LIMIT_FLAG)) {
                            int out_loc = paramList.indexOf(SPLIT_LIMIT_FLAG);
                            if (paramList.size() > (out_loc + 1)) {
                                String new_limit = paramList.remove(out_loc + 1);
                                if (new_limit.matches("[0-9]+")) {
                                    split_limit = new Integer(new_limit);
                                }
                            }
                            paramList.remove(SPLIT_LIMIT_FLAG);
                        }

                        if (paramList.contains(PROGRESS_MONT_INTERVAL_FLAG)) {
                            int out_loc = paramList.indexOf(PROGRESS_MONT_INTERVAL_FLAG);
                            if (paramList.size() > (out_loc + 1)) {
                                String new_limit = paramList.remove(out_loc + 1);
                                if (new_limit.matches("[0-9]+")) {
                                    PROGRESS_MONT_INTERVAL = new Integer(new_limit);
                                }
                            }
                            paramList.remove(PROGRESS_MONT_INTERVAL_FLAG);
                        }


                        if (paramList.remove("-dir")) {
                            printout(0, null, "188\n" + commnetstext);
//                            ArrayList<File> resukt_l = getAllinSubFolders(new File(paramList.get(1)), new ArrayList<String>(1), new ArrayList<File>(1), true);
//                            for (int i = 0; i < resukt_l.size(); i++) {
//                                try {
//                                     printout(0,null, resukt_l.get(i).getCanonicalPath());
//                                } catch (Exception e) {
//                                    e.printStackTrace();
//                                }
//                            }
                        } else if (paramList.remove(PREPARE_TO_ADD_FILES_FLAG)) {
                            boolean recursive = false;
                            boolean skipdots = true;
                            boolean current_as_batch = false;
                            boolean current_as_batch_list = false;
                            boolean hierarchy = false;
                            boolean skip_checksum = false;
                            int depth = 1;
                            if (!paramList.isEmpty()) {
                                if (paramList.remove(PREPARE_TO_ADD_FILES_RECURSIVLY_FLAG)) {
                                    recursive = true;
                                    printout(0, null, "Warning 383 -recurse, recursive mode not fully implemented yet, use at your own risk");
                                }
                            }
                            String batch_nm = null;
                            String report_nm = null;
                            String person_email = null;
                            for (int i = 0; i < paramList.size(); i++) {
                                String[] c_parm_a = paramList.get(i).split("=");
                                if (c_parm_a.length == 2) {
                                    if (c_parm_a[0].equalsIgnoreCase("REPORT_BATCH")) {
                                        batch_nm = c_parm_a[1];
                                        paramList.remove(i);
                                        i--;
                                    } else if (c_parm_a[0].equalsIgnoreCase("REPORT")) {
                                        report_nm = c_parm_a[1];
                                        paramList.remove(i);
                                        i--;
                                    } else if (c_parm_a[0].equalsIgnoreCase("PERSON")) {
                                        person_email = c_parm_a[1];
                                        paramList.remove(i);
                                        i--;
                                    }
                                }
                            }
                            if (paramList.remove(CREATE_HIERARCHY)) {
                                hierarchy = true;
                            }

                            if (paramList.remove(SKIP_CHECKUSM)) {
                                skip_checksum = true;
                            }
                            if (paramList.contains(DEPTH)) {
                                int out_loc = paramList.indexOf(DEPTH);
                                if (paramList.size() > (out_loc + 1)) {
                                    String depth_str = paramList.remove(out_loc + 1).trim();
                                    if (depth_str.matches("[0-9]+")) {
                                        depth = new Integer(depth_str);
                                    }
                                }
                                paramList.remove(DEPTH);
                            }


                            if (!paramList.isEmpty()) {
                                if (paramList.remove(PREPARE_TO_ADD_USING_CURRENT_AS_BACTH)) {
                                    current_as_batch = true;
                                    printout(0, null, "Using current folder as report batch name");
                                }
                            }
                            if (!paramList.isEmpty()) {
                                if (paramList.remove(PREPARE_TO_ADD_USING_CURRENT_AS_BACTH_LIST)) {
                                    current_as_batch_list = true;
                                    printout(0, null, "Using the subfolders as report batch names");
                                }
                            }

                            if (!paramList.isEmpty()) {
                                if (paramList.remove("-usedots")) {
                                    skipdots = false;
                                }
                            }
                            String dir_for_placing_prep_files = null;
                            if (paramList.contains(FORCE_CONFIG_OUT_PUT_LOCATION_FLAG)) {
                                int out_loc = paramList.indexOf(FORCE_CONFIG_OUT_PUT_LOCATION_FLAG);
                                if (paramList.size() > (out_loc + 1)) {
                                    dir_for_placing_prep_files = paramList.remove(out_loc + 1);
                                }
                                paramList.remove(FORCE_CONFIG_OUT_PUT_LOCATION_FLAG);
                                printout(0, null, "Output location =" + dir_for_placing_prep_files);
                            }

                            if (recursive) {
                                if (dir_for_placing_prep_files == null) {
                                    dir_for_placing_prep_files = getPWD();
                                }
                                long skip_larger = -1;
                                if (paramList.contains(SKIP_LARGE_FILES_FLAG)) {
                                    int out_loc = paramList.indexOf(SKIP_LARGE_FILES_FLAG);
                                    if (paramList.size() > (out_loc + 1)) {
                                        String skipsize = paramList.get(out_loc + 1).trim();
                                        if (skipsize.matches("[0-9]+")) {
                                            skip_larger = new Long(skipsize);
                                            printout(0, null, "Not processing files larger than " + skipsize + " Mb");
                                        }
                                    }
                                }
                                long skip_smaller = -1;
                                if (paramList.contains(SKIP_SMALL_FILES_FLAG)) {
                                    int out_loc = paramList.indexOf(SKIP_SMALL_FILES_FLAG);
                                    if (paramList.size() > (out_loc + 1)) {
                                        String skipsize = paramList.get(out_loc + 1).trim();
                                        if (skipsize.matches("[0-9]+")) {
                                            skip_smaller = new Long(skipsize);
                                            printout(0, null, "Not processing files smaller than " + skipsize + " Mb");
                                        }
                                    }
                                }

                                skip_larger = skip_larger * 1048576;
                                skip_smaller = skip_smaller * 1048576;
                                paramList.add(USE_ALLDEFAULT_VALUES_FLAG);
                                String prepfilenm_files = PREPARE_TO_ADD_CUSTOM_TABLE_FILENM.replace("<TABLE>", "files");
                                String prepfilenm_files_warings = PREPARE_TO_ADD_CUSTOM_TABLE_FILENM.replace("<TABLE>", "files_warnings");
                                String prepfilenm_files_avoided = PREPARE_TO_ADD_CUSTOM_TABLE_FILENM.replace("<TABLE>", "files_avoided");
//                                HashMap<String, ArrayList<Path>> result_map = getAllinSubFolders_new(getPWD(), new ArrayList<String>(),
//                                        new HashMap<String, ArrayList<Path>>(), true, 0, 0, true);

                                HashMap<String, ArrayList<String>> error_map = new HashMap<>();
                                ArrayList<String> check_with_db_file_l = new ArrayList<>();
                                HashMap<String, HashMap<String, String>> files2cdetails_map = new HashMap<>();
                                HashMap<String, String> files2checksum_map = new HashMap<>();
                                ArrayList<String> files_to_aviod_l = new ArrayList<>();
                                ArrayList<Path> refined_file_l = new ArrayList<>();
                                HashMap<String, ArrayList<String>> duplicate_hash_l_map = new HashMap<>();
                                String report_batch_root = null;

                                try {
//                                    HashMap<String, ArrayList<Path>> files_with__Rportname_map = new HashMap<>();     
                                    if (current_as_batch_list) {
                                        Path pwd = getPWD(true);
                                        DirectoryStream<Path> ds = null;
                                        DirectoryStream<Path> sub_ds = null;
                                        try {
                                            ds = Files.newDirectoryStream(pwd);
                                            Iterator<Path> path_l = ds.iterator();
                                            while (path_l.hasNext()) {
                                                Path c_batch = path_l.next();
                                                if (batch_nm == null) {
                                                    batch_nm = c_batch.getFileName().toString();
                                                }
                                                if (Files.isDirectory(c_batch, LinkOption.NOFOLLOW_LINKS) || Files.isSymbolicLink(c_batch)) {
                                                    sub_ds = Files.newDirectoryStream(c_batch);
                                                    Iterator<Path> sub_path_l = sub_ds.iterator();
                                                    while (sub_path_l.hasNext()) {
                                                        Path c_file = sub_path_l.next();
                                                        if (report_nm == null) {
                                                            report_nm = c_file.getFileName().toString();
                                                        }
                                                        printout(0, null, "Preparing batch: " + batch_nm);
                                                        LoadRecursive pf = new LoadRecursive(skipdots);
                                                        Files.walkFileTree(c_file.toRealPath(), EnumSet.of(FileVisitOption.FOLLOW_LINKS), Integer.MAX_VALUE, pf);
                                                        ArrayList<Path> files_to_use_l = pf.getOKlist();
                                                        files_to_aviod_l.addAll(pf.avoidOKlist_string());
                                                        if (files_to_use_l == null || files_to_use_l.isEmpty()) {
                                                            printout(0, null, "No files matched the criteria. in " + c_file);
                                                        } else {
//                                                        files_with__Rportname_map.put(reoprt_name, files_to_use_l);

                                                            recursive_prepare(files_to_use_l, files_to_aviod_l, paramList.remove(SKIP_DB_TEST_FLAG),
                                                                    paramList.remove(SKIP_archives_FLAG), skip_larger,
                                                                    skip_smaller, prepfilenm_files_warings, error_map, check_with_db_file_l,
                                                                    files2cdetails_map, files2checksum_map, refined_file_l, batch_nm, report_nm,
                                                                    duplicate_hash_l_map, skip_checksum);
                                                        }
                                                    }
                                                    sub_ds.close();

                                                }
                                            }
                                            ds.close();
                                        } catch (IOException ex) {
                                            ex.printStackTrace();
                                        } finally {
                                            try {
                                                if (ds != null) {
                                                    ds.close();
                                                }
                                            } catch (IOException ex) {
                                            }
                                        }
                                    } else if (current_as_batch) {
                                        printout(0, null, "771");

                                        Path pwd = getPWD(true);
                                        if (batch_nm == null) {
                                            batch_nm = pwd.getFileName().toString();
                                        }
                                        printout(0, null, "Report_batch name=" + batch_nm);
                                        DirectoryStream<Path> ds = null;
                                        try {
                                            ds = Files.newDirectoryStream(pwd);
                                            Iterator<Path> path_l = ds.iterator();
                                            int cc = 0;
                                            while (path_l.hasNext()) {
                                                cc++;
                                                Path c_file = path_l.next();
                                                if (Files.isDirectory(c_file, LinkOption.NOFOLLOW_LINKS) || Files.isSymbolicLink(c_file)) {
                                                    if (report_nm == null) {
                                                        report_nm = c_file.getFileName().toString();
                                                    }
                                                    LoadRecursive pf = new LoadRecursive(skipdots);
                                                    Files.walkFileTree(c_file.toRealPath(), EnumSet.of(FileVisitOption.FOLLOW_LINKS), Integer.MAX_VALUE, pf);
                                                    ArrayList<Path> files_to_use_l = pf.getOKlist();
                                                    files_to_aviod_l.addAll(pf.avoidOKlist_string());
                                                    if (files_to_use_l == null || files_to_use_l.isEmpty()) {
                                                        printout(0, null, "No files matched the criteria. in " + c_file);
                                                    } else {
//                                                        files_with__Rportname_map.put(reoprt_name, files_to_use_l);

                                                        recursive_prepare(files_to_use_l, files_to_aviod_l, paramList.remove(SKIP_DB_TEST_FLAG),
                                                                paramList.remove(SKIP_archives_FLAG), skip_larger,
                                                                skip_smaller, prepfilenm_files_warings, error_map, check_with_db_file_l,
                                                                files2cdetails_map, files2checksum_map, refined_file_l, batch_nm, report_nm,
                                                                duplicate_hash_l_map, skip_checksum);

                                                    }
                                                }
                                            }
                                            ds.close();
                                        } catch (IOException ex) {
                                            ex.printStackTrace();
                                        } finally {
                                            try {
                                                if (ds != null) {
                                                    ds.close();
                                                }
                                            } catch (IOException ex) {
                                            }
                                        }
                                    } else {
                                        LoadRecursive pf = new LoadRecursive(skipdots);
                                        Files.walkFileTree(getPWD(true), pf);
                                        ArrayList<Path> files_to_use_l = pf.getOKlist();
                                        files_to_aviod_l.addAll(pf.avoidOKlist_string());
                                        if (files_to_use_l == null || files_to_use_l.isEmpty()) {
                                            printout(0, null, "No files matched the criteria. nothing to process");
                                        } else {
//                                        recursive_prepare(files_to_use_l, new ArrayList<String>(),
//                                                paramList.remove(SKIP_DB_TEST_FLAG), paramList.remove(SKIP_archives_FLAG), skip_larger, skip_smaller,
//                                                prepfilenm_files, prepfilenm_files_warings);

                                            recursive_prepare(files_to_use_l, files_to_aviod_l, paramList.remove(SKIP_DB_TEST_FLAG),
                                                    paramList.remove(SKIP_archives_FLAG), skip_larger,
                                                    skip_smaller, prepfilenm_files_warings, error_map, check_with_db_file_l,
                                                    files2cdetails_map, files2checksum_map, refined_file_l,
                                                    null, null, duplicate_hash_l_map, skip_checksum);
                                        }

                                    }

                                    writeWarningfile(prepfilenm_files_warings, dir_for_placing_prep_files, error_map);
                                    boolean addmark = false;
                                    printout(0, null, "\nSending metadata for " + refined_file_l.size() + " files");
                                    createContent_file_recurse(refined_file_l,
                                            check_with_db_file_l, files2cdetails_map, files2checksum_map,
                                            prepfilenm_files, dir_for_placing_prep_files, "#Creating files\n" + commnetstext,
                                            paramList.remove(SKIP_DB_TEST_FLAG), addmark, 0, report_batch_root);

//                                    Files.walkFileTree(getPWD(true), pf);
//                                    ArrayList<Path> files_to_use_l = pf.getOKlist();
//                                    if (files_to_use_l == null || files_to_use_l.isEmpty()) {
//                                         printout(0,null, "No files matched the criteria. nothing to process");
//                                    } else {



//                                    }
                                    printout(0, null, "\nAvoided " + files_to_aviod_l.size() + " files");
                                    if (!files_to_aviod_l.isEmpty()) {
                                        printout(0, null, "Some files and folders had access problems. Thus not included in the processing ");
//                                        HashMap<String, ArrayList<String>> errro_map = new HashMap<>();
                                        ArrayList<String> tmp_l = new ArrayList<>();
                                        for (int i = 0; i < files_to_aviod_l.size(); i++) {
                                            tmp_l.add(files_to_aviod_l.get(i).toString());
                                        }
                                        HashMap<String, ArrayList<String>> avoided_map = new HashMap<>();
                                        avoided_map.put("FILES_AVOIDED(" + tmp_l.size() + ")", tmp_l);
                                        writeWarningfile(prepfilenm_files_avoided, dir_for_placing_prep_files, avoided_map);
                                    }
//                                    writeWarningfile(String filename, String directory, HashMap<String, ArrayList<String>> errro_map)

                                    ArrayList<String> tmp_l = error_map.remove("FILES_WITH_THE_SAME_NAME");
                                    if (tmp_l != null) {
                                        printout(0, null, "Number of files with the same name as atleast one other files=" + tmp_l.size());
                                    }
                                    ArrayList<String> tmp_l2 = error_map.remove("FILES_FOUND_TO_BE_DUPLICATE_USING_CHECKSUM");
                                    if (tmp_l != null) {
                                        printout(0, null, "Number of files with the same checksum as atleast one other file=" + tmp_l2.size());
                                    }

                                    ArrayList<String> tmp_l3 = new ArrayList<>(error_map.keySet());
                                    for (int i = 0; i < tmp_l3.size(); i++) {
                                        printout(0, null, tmp_l3.get(i) + "=" + error_map.get(tmp_l3.get(i)).size());

                                    }


                                } catch (IOException ex) {
                                    ex.printStackTrace();
                                }

//                                HashMap<String, ArrayList<File>> result_map =.getFiles(paramList, verbosemode);// getFolders(new File(getPWD()), new ArrayList<String>(), new HashMap<String, ArrayList<File>>(), true, 0, 0, true);
//                                ArrayList<Path> files_to_use_l = result_map.get("OK");
//                                if (files_to_use_l == null || files_to_use_l.isEmpty()) {
//                                     printout(0,null, "No files matched the criteria. nothing to process");
//                                } else {
//                                    recursive_add(dir_for_placing_prep_files, files_to_use_l, new ArrayList<String>(),
//                                            paramList.remove(SKIP_DB_TEST_FLAG), paramList.remove(SKIP_archives_FLAG), skip_larger,
//                                            prepfilenm_files, prepfilenm_files_warings);
//                                }

                            } else if (hierarchy) {
                                split_To_hierarchy(null, dir_for_placing_prep_files, depth, true);

                            } else {
                                paramList.remove(PREPARE_TO_ADD_FILES_FLAG);
                                prepareForAdding_controller(paramList,
                                        dir_for_placing_prep_files,
                                        skip_checksum, batch_nm, report_nm, person_email);
                            }
                        } else if (paramList.contains(ADD_FILES_FLAG)) {
                            if (!verbosemode) {
                                printout(0, null, "Use " + VERBOSE_FLAG + " (verbose mode) to get more details on changes and warnings");
                            }
                            paramList.remove(ADD_FILES_FLAG);
                            String report_batch = null;
                            String report = null;
                            String person_email = null;
                            for (int i = 0; i < paramList.size(); i++) {
                                String[] c_parm_a = paramList.get(i).split("=");
                                if (c_parm_a.length == 2) {
                                    if (c_parm_a[0].equalsIgnoreCase("REPORT_BATCH")) {
                                        report_batch = c_parm_a[1];
                                        paramList.remove(i);
                                        i--;
                                    } else if (c_parm_a[0].equalsIgnoreCase("REPORT")) {
                                        report = c_parm_a[1];
                                        paramList.remove(i);
                                        i--;
                                    } else if (c_parm_a[0].equalsIgnoreCase("PERSON")) {
                                        person_email = c_parm_a[1];
                                        paramList.remove(i);
                                        i--;
                                    }
                                }
                            }

                            if (paramList.remove(NOW)) {
                                if (paramList.remove(PREPARE_TAGS_FLAG) || paramList.remove(PREPARE_TAGS_FLAG2)) {
                                    String target = paramList.get(0);
                                    if (!paramList.get(0).contains("=")) {
                                        Path p = Paths.get(paramList.get(0));
                                        if (Files.exists(p) && Files.isReadable(p)) {
                                            ArrayList<String> tmp_l = new ArrayList<>();
                                            tmp_l.add(p.toAbsolutePath().toString());
                                            HashMap<String, String> getHashes_map = getHashes(tmp_l, -1, -1, false);
                                            target = "FILES.CHECKSUM=" + getHashes_map.get(tmp_l.get(0));
                                        }
                                    }
                                    printout(0, null, "475 result=" + tag(target, paramList.get(1)));
                                } else {
                                    createFileEntries_direct(paramList, report_batch,
                                            report, person_email, verbosemode);
                                }
                            } else {
                                String dir_read_from_prep_files = null;
                                String input_location = null;
//                            boolean overide = false;
//                            int skip_larger = -1;

//                            if (!paramList.isEmpty()) {
                                if (paramList.contains(FORCE_CONFIG_IN_PUT_LOCATION_FLAG)) {
                                    int out_loc = paramList.indexOf(FORCE_CONFIG_IN_PUT_LOCATION_FLAG);
                                    if (paramList.size() > (out_loc + 1)) {
                                        dir_read_from_prep_files = paramList.remove(out_loc + 1);
                                    }
                                    paramList.remove(FORCE_CONFIG_IN_PUT_LOCATION_FLAG);
                                    printout(0, null, "Reading configurations from =" + dir_read_from_prep_files);
                                }
//                            }
//                            boolean usedefault = false;
//                            if (!paramList.isEmpty()) {
//                                if (paramList.contains(USE_ALLDEFAULT_VALUES_FLAG)) {
////                                    usedefault = true;
//                                    paramList.remove(USE_ALLDEFAULT_VALUES_FLAG);
//                                }
//                            }
//                            if (!paramList.isEmpty()) {
//                                if (paramList.contains(SKIP_LARGE_FILES_FLAG)) {
//                                    int out_loc = paramList.indexOf(SKIP_LARGE_FILES_FLAG);
//                                    if (paramList.size() > (out_loc + 1)) {
//                                        String skipsize = paramList.remove(out_loc + 1).trim();
//                                        if (skipsize.matches("[0-9]+")) {
////                                            skip_larger = new Integer(skipsize);
//                                            printout(0, null, "Not processing files larger than " + skipsize + " Mb");
//                                        }
//                                    }
//                                    paramList.remove(SKIP_LARGE_FILES_FLAG);
//                                }
//                            }

                                if (!paramList.isEmpty()) {
                                    if (paramList.contains(FORCE_IN_PUT_LOCATION_FLAG)) {
                                        int out_loc = paramList.indexOf(FORCE_IN_PUT_LOCATION_FLAG);
                                        if (paramList.size() > (out_loc + 1)) {
                                            input_location = paramList.remove(out_loc + 1);
                                        }
                                        paramList.remove(FORCE_IN_PUT_LOCATION_FLAG);
                                        printout(0, null, "Input location =" + input_location);
                                    }
                                }
                                if (!paramList.isEmpty()) {
                                    if (paramList.contains(OVERIDE_FLAG)) {
//                                    overide = true;
                                        paramList.remove(OVERIDE_FLAG);
                                        printout(0, null, "Overide active");
                                    }
                                }
                                if (paramList.contains(FROM_FILE_FLAG)) {
                                    paramList.remove(FROM_FILE_FLAG);
                                    if (paramList.size() >= 1) {
                                        createFileEntries(dir_read_from_prep_files, verbosemode);
                                    } else {
                                        printout(0, null, "Error 1F: " + ADD_FILES_FLAG + " expected a file after the argument " + FROM_FILE_FLAG + ", but did not find it");
                                    }

                                } else if (paramList.contains(TEMPLATE_FLAG)) {
                                    paramList.remove(TEMPLATE_FLAG);
                                    if (paramList.size() >= 1) {
                                        for (int i = 0; i < paramList.size(); i++) {
                                            readTemplate_new(paramList.get(i), verbosemode, true, dir_read_from_prep_files);
                                        }
                                    } else {
                                        printout(0, null, "Error 1G: " + TEMPLATE_FLAG + " expecteds table name after the argument " + TEMPLATE_FLAG + ", but did not find it");
                                    }
                                } else if (paramList.remove(PREPARE_TAGS_FLAG) || paramList.remove(PREPARE_TAGS_FLAG2)) {
                                    if (paramList.size() >= 1) {
                                        readTemplate_new(paramList.get(0), verbosemode, true, dir_read_from_prep_files);
                                    } else {
                                        printout(0, null, "Error 1p: " + PREPARE_TAGS_FLAG + " expecteds table name after the argument " + TEMPLATE_FLAG + ", "
                                                + "but did not find it");
                                    }

                                } else {
                                    createFileEntries(dir_read_from_prep_files, verbosemode);
                                }
                            }

                        } else if (paramList.remove(RELATE_FILES_FLAG)) {
                            if (paramList.size() == 1) {
                                System.out.println(relate(paramList.get(0), null, null));
                            } else if (paramList.size() == 2) {
                                System.out.println(relate(paramList.get(0), paramList.get(1).trim(), null));
                            } else if (paramList.size() > 2) {
                                System.out.println(relate(paramList.get(0), paramList.get(1).trim(), paramList.get(2).trim()));
                            }
                        } else if (paramList.contains(UNDO_FLAG)) {
                            paramList.remove(UNDO_FLAG);
                            if (paramList.size() >= 1) {
                                undo(paramList.get(0), true, verbosemode);
                            } else {
                                printout(0, null, "Error 1H: " + UNDO_FLAG + " expected a recod file for undo, but did not find it");
                            }

                        } else if (paramList.contains(SEARCH_FLAG) || paramList.contains(SEARCH_FLAG2)) {
                            paramList.remove(SEARCH_FLAG);
                            paramList.remove(SEARCH_FLAG2);
                            List result_l = null;
                            String outputfile_name = null;
                            boolean create_template = false;
                            boolean html = false;
                            boolean no_group = false;
                            boolean addcolumnnames = true;
                            boolean sameline = false;
                            boolean check_ifExists = false;
                            boolean skip_progress = false;

                            boolean distinct = false;
                            boolean sort = false;
                            if (!paramList.isEmpty()) {
                                if (paramList.remove(GET_AVAILABLE_TABLES_FLAG)) {
                                    printAvailable();
                                } else {
                                    if (paramList.remove(USE_ALL_FLAG)) {
                                        result_l = advancedQueryHandler(FILES_NAME + "=*", "files", false, false, true, false, false, true, false, 0, true);
                                    } else {
                                        boolean exactmach = false;
                                        boolean expand = false;
                                        boolean from_file = false;
                                        boolean from_filex = false;

                                        if (paramList.contains(OUT_PUT_FILE_FLAG)) {
                                            int out_loc = paramList.indexOf(OUT_PUT_FILE_FLAG);
                                            if (paramList.size() > (out_loc + 1)) {
                                                outputfile_name = paramList.remove(out_loc + 1);
                                                if (outputfile_name.toLowerCase().endsWith(".html")) {
                                                    html = true;
                                                }
                                            } else {
                                                printout(0, null, "Error: expecting file name after " + OUT_PUT_FILE_FLAG);
                                                System.exit(1116);
                                            }
                                            paramList.remove(OUT_PUT_FILE_FLAG);
                                            printout(0, null, "#Output file =" + outputfile_name);
                                        }
                                        if (paramList.remove(SEARCH_EXACT_FLAG)) {
                                            exactmach = true;
                                        }
                                        if (paramList.remove(EXISTS_FLAG)) {
                                            check_ifExists = true;
                                        }
                                        int querytype = 0;
                                        if (paramList.remove(CHILDREN_BEFORE)) {
                                            querytype = 1;//earlier 2
                                        }

                                        if (paramList.remove(CHILDREN_AT_DUING)) {
                                            querytype = 2;
////                                            if (querytype == 0) {
////                                                querytype = 3;
////                                            } else if (querytype == 2) {
////                                                querytype = 4;
////                                            } else {
//                                                querytype = 3;
////                                            }
                                        }

                                        if (paramList.remove(CHILDREN_AT_THE_END) || paramList.remove(CHILDREN_AT_THE_END2)) {
//                                            querytype = querytype + 4;
//                                            if (querytype == 0) {
//                                                querytype = 3;
//                                            } else if (querytype == 2) {
//                                                querytype = 4;
//                                            } else {
                                            querytype = 3;
//                                            }
                                        } else if (paramList.remove(PARENT_AT_END)) {
//                                            querytype = querytype + 5;
                                            querytype = 4;
                                        }

                                        if (paramList.remove(HTML_OUT) && outputfile_name != null) {
                                            html = true;
                                            if (!outputfile_name.toLowerCase().endsWith(".html")) {
                                                outputfile_name = outputfile_name + ".html";
                                            }
                                        }
                                        if (paramList.remove(NOGROUP)) {
                                            no_group = true;
                                        }
                                        if (paramList.remove(FROM_FILE_FLAG)) {
                                            from_file = true;
                                        }
                                        if (paramList.remove(FROM_FILEX_FLAG)) {
                                            from_filex = true;
                                        }
                                        if (paramList.remove(EXPAND_TO_FOREING_FLAG)) {
                                            expand = true;
                                        }
//                                        if (paramList.remove(GROUP_RESULTS)) {
//                                            group = true;
//                                        }
                                        if (paramList.remove(TEMPLATE_FLAG)) {
                                            create_template = true;
                                        }
                                        if (paramList.remove(OMIIT_LABLES_FLAG) || paramList.remove(OMIIT_LABLES_FLAG2)) {
                                            addcolumnnames = false;
                                        }

                                        if (paramList.remove(SKIP_FORMATING_FLAG)) {
                                            skip_formating = true;
                                            addcolumnnames = false;
                                            no_group = true;
                                        }
                                        if (paramList.remove(SKIP_PROGRESS)) {
                                            skip_progress = true;

                                        }

                                        if (paramList.remove(SKIP_NEW_LINE_FLAG)) {
                                            sameline = true;
                                        }

                                        if (paramList.remove(SEARCH_DISTINCT_FLAG)) {
                                            distinct = true;
                                        }
                                        if (paramList.remove(SORT_FLAG)) {
                                            sort = true;
                                        }


                                        String invalid_fond = null;
                                        for (int i = 0; (invalid_fond == null && i < paramList.size()); i++) {
                                            if (paramList.get(i).trim().startsWith("-")) {
                                                invalid_fond = paramList.get(i);
                                            }
                                        }

                                        if (invalid_fond != null) {
                                            printout(0, null, "Error 1F: invalid argument " + invalid_fond);
                                        } else {
                                            if (paramList.size() > 0 && paramList.size() < 4) {
                                                String table_nm = paramList.get(0).split("\\.")[0];
                                                if ((from_file || from_filex) && paramList.get(0) != null && paramList.get(0).split("=").length == 2) {
                                                    String file_nm = paramList.get(0).split("=")[1];
                                                    String table_column = paramList.remove(0).split("=")[0];
                                                    if (!from_filex) {
                                                        exactmach = true;
                                                    }
                                                    printout(0, null, "#Exact match only=" + exactmach);
                                                    String query = getFileContent(file_nm, "#");

                                                    paramList.add(0, table_column + "=" + query);
                                                } else if (from_file || from_filex) {
                                                    printout(0, null, "Error: Query format incorrect.");
                                                    System.exit(1);
                                                }
                                                if (paramList.size() > 2) {
                                                    paramList.add(1, paramList.get(1) + " " + paramList.get(2));
                                                }
                                                String directory = null;
                                                String source = null;
                                                String targte = null;
//                                                String extara_concat_str="\\+.+\\+";
                                                if (paramList.size() == 1) {
                                                    source = paramList.get(0);
                                                    targte = paramList.get(0).split("=")[0];
                                                    targte = targte.split("\\.")[0];
                                                } else {
                                                    source = paramList.get(0);
//                                                    String[] tagtet_a = paramList.get(1).split("");
                                                    targte = paramList.get(1);
                                                }
                                                if (create_template) {
                                                    if (table_nm.toUpperCase().endsWith(HIERARCHY_FLAG)) {
                                                        printout(0, null, "This operation is not allowed for hierarchy tables");
                                                    } else {
                                                        if (paramList.size() == 1) {
                                                            result_l = advancedQueryHandler(source, targte, exactmach, false, true, expand, false, false, true, 0, !skip_progress);
                                                        } else {
                                                            result_l = advancedQueryHandler(source, targte, exactmach, false, true, expand, false, false, true, 0, !skip_progress);
                                                        }

                                                        if (result_l == null || result_l.isEmpty()) {
                                                            printout(0, null, "(M) No match");
                                                        } else {
                                                            createContent_FromSearch(PREPARE_TO_ADD_CUSTOM_TABLE_FILENM.replace("<TABLE>", table_nm), directory, table_nm, result_l, new HashMap<String, ArrayList<String>>());
                                                        }
                                                    }
                                                } else {
                                                    if (skip_formating && outputfile_name == null) {
                                                        skip_progress = true;
                                                    }
                                                    if (paramList.size() == 1) {
                                                        result_l = advancedQueryHandler(source, targte.split("\\.")[0], exactmach, false,
                                                                addcolumnnames, expand, check_ifExists, false, true, querytype, !skip_progress);
                                                    } else {
                                                        result_l = advancedQueryHandler(source, targte, exactmach, false,
                                                                addcolumnnames, expand, check_ifExists, false, true, querytype, !skip_progress);
                                                    }
                                                }
                                            } else {
                                                printout(0, null, "Error 1E: " + SEARCH_FLAG + " format incorrect. Maximum of two arguemnts are allowed");
                                            }
                                        }
                                    }
//https://ans-180230.stolav.ntnu.no:8180/eGenVar_web/Search/SearchResults3?TABLETOUSE_EGEN_DATAENTRY.ADDRESS_TAGSOURCE=455d6a4811bc399381f2c93cba7e1f415400646e
//                               printout(0,null, "1216 "+result_l);
                                    if (result_l != null && !result_l.isEmpty() && !create_template) {
                                        Object meta = result_l.remove(0);
//                                        String url_link = null;
                                        String url = null;
                                        if (meta != null) {
                                            String meta_s = meta.toString().toUpperCase();
                                            String meta_s_org = meta.toString();
                                            if (meta_s.toUpperCase().startsWith("ERROR")) {
                                                printout(1, null, "Error : " + meta);
                                            } else {
                                                String[] url_a = meta_s_org.split("\\|\\|");
                                                url = "\n#" + url_a[0] + "\n";
                                                for (int i = 1; i < url_a.length; i++) {
                                                    url = url + "#" + url_a[i] + "\n";
                                                }
                                            }
                                        }
                                        ArrayList<String> result_str_l = new ArrayList<>(result_l.size());
                                        Set<String> result_str_sl = new HashSet<>(result_l.size());
                                        boolean null_found = false;
                                        for (int i = 0; i < result_l.size(); i++) {
                                            if (result_l.get(i) != null) {
                                                String c_result = result_l.get(i).toString();
                                                if (distinct) {
                                                    result_str_sl.add(c_result);
//                                                    if (!result_str_l.contains(c_result)) {
//                                                        result_str_l.add(c_result);
//                                                    }
                                                } else {
                                                    result_str_l.add(c_result);
                                                }
                                            } else {
                                                null_found = true;
                                            }
                                        }

                                        result_str_l.addAll(result_str_sl);
                                        if (sort) {
                                            Collections.sort(result_str_l);
                                        }
                                        result_l.clear();
                                        if (null_found && result_str_l.isEmpty()) {
                                            result_str_l.add("#Error: null result encountered");
                                        }
                                        if (!skip_formating) {
                                            printout(1, null, "");
                                        }
                                        if (outputfile_name == null) {
                                            if (url != null && !skip_formating) {
                                                printout(1, null, "#URL for this result:" + url);
                                            }
                                            for (int i = 0; i < result_str_l.size(); i++) {
                                                if (!no_group) {
                                                    System.out.print((i + 1) + ")");
                                                }
                                                String[] re_split_a = result_str_l.get(i).split("\\|\\|");
                                                for (int j = 0; j < re_split_a.length; j++) {
                                                    String[] cr_a = new String[]{re_split_a[j]};//re_split_a[j].split(","); this was spliting the AGSOURCE.LINEAGE
                                                    for (int k = 0; k < cr_a.length; k++) {
                                                        String cr = cr_a[k];
                                                        if (!skip_formating) {
                                                            if (!cr.startsWith("URL") && cr.length() > results_line_length) {
                                                                cr = cr.substring(0, results_line_length) + "...";
                                                            }
                                                        }
                                                        if (!no_group) {
                                                            System.out.print("\t");
                                                        }
                                                        if (sameline) {
                                                            System.out.print(cr.trim() + "\t");
                                                        } else {
                                                            printout(1, null, cr.trim());
                                                        }
                                                    }
                                                }
                                                if (sameline) {
                                                    printout(1, null, "");
                                                }
                                                if (!no_group) {
                                                    printout(1, null, "");
                                                }

                                            }
                                        } else {
//                                        Charset charset = Charset.forName("ISO-8859-4");
                                            PrintWriter out = null;
                                            try {
                                                out = new PrintWriter(Paths.get(outputfile_name).toFile(), "ISO-8859-4");
                                                if (html) {
                                                    out.append("<!DOCTYPE html>\n<html>\n<head></head>\n<body>\n<table>");
                                                    if (url != null) {
                                                        out.append("\n");
                                                        String[] url_split_a = url.split("\\|\\|");
                                                        for (int i = 0; i < url_split_a.length; i++) {
                                                            String[] url_link = url_split_a[i].split("=");
                                                            if (url_link.length == 2 && url_link[1].toUpperCase().startsWith("HTTP")) {
                                                                String link = "<a href=\"" + url_link[1] + "\">" + url_link[1] + "</a>";
                                                                out.append("<tr><th bgcolor=\"#f1f1fc\">#" + url_link[0] + ":</th><td>" + link + "</td></tr>\n");
                                                            } else {
                                                                out.append("<tr><th bgcolor=\"#f1f1fc\">#URLs for this result:</th><td>" + url_split_a[i] + "</td></tr>\n");
                                                            }
                                                        }
                                                        out.append("\n");
                                                    }
                                                    for (int i = 0; i < result_str_l.size(); i++) {
                                                        if (!no_group) {
                                                            out.append("<tr><th colspan=\"2\" bgcolor=\"#f1f1fc\">" + i + "</th></tr>\n");
                                                        }
                                                        String[] re_split_a = result_str_l.get(i).split("\\|\\|");
                                                        for (int j = 0; j < re_split_a.length; j++) {
                                                            String[] cr_a = re_split_a[j].split(",");
                                                            for (int k = 0; k < cr_a.length; k++) {
                                                                String cr = cr_a[k];
                                                                if (cr.contains("=")) {
                                                                    String[] spli_a = cr.split("=", 2);
                                                                    out.append("<tr><td  bgcolor=\"#eaeaef\">" + spli_a[0] + "</td><td>" + spli_a[1] + "</td></tr>\n");
                                                                } else {
                                                                    out.append("<tr><td>" + cr + "</td></tr>");
                                                                }
                                                            }
                                                        }
                                                    }
                                                    out.append("\n</table></body>\n</html>");
                                                    out.flush();
                                                    out.close();
                                                } else {
                                                    if (addcolumnnames && url != null) {
                                                        String[] url_split_a = url.split("\\|\\|");
                                                        for (int i = 0; i < url_split_a.length; i++) {
//                                                            if (sameline) {
//                                                                out.append("#" + url_split_a[i] + "\t");
//                                                            } else {
                                                            out.append("#" + url_split_a[i] + "\n");
//                                                            }

                                                        }
                                                        out.append("\n");
                                                    }
                                                    for (int i = 0; i < result_str_l.size(); i++) {
                                                        String[] re_split_a = result_str_l.get(i).split("\\|\\|");
                                                        for (int j = 0; j < re_split_a.length; j++) {
                                                            String[] cr_a = re_split_a[j].split(",");
                                                            for (int k = 0; k < cr_a.length; k++) {
                                                                String cr = cr_a[k];
                                                                if (sameline) {
                                                                    out.append(cr + "\t");
                                                                } else {
                                                                    out.append(cr + "\n");
                                                                }

                                                            }
                                                        }
                                                        if (sameline) {
                                                            out.append("\n");
                                                        }
                                                        if (!no_group) {
                                                            out.append("\n");
                                                        }

                                                    }
                                                    out.close();
                                                }
                                            } catch (IOException ex) {
                                                ex.printStackTrace();
                                            } finally {
                                                if (out != null) {
                                                    out.close();
                                                }
                                            }

                                        }

                                    } else {
                                        if (!create_template) {
                                            printout(0, null, "No match");
                                        }
                                    }
                                }
                            } else {
                                printout(0, null, "Error 1B: " + SEARCH_FLAG + " requires atleast on more argument.");
                            }
                        } else if (paramList.get(0).equalsIgnoreCase(GET_AVAILABLE_TABLES_FLAG)) {
                            printAvailable();
                        } else if (paramList.contains(TEMPLATE_FLAG)) {
                            paramList.remove(TEMPLATE_FLAG);
                            String dir_for_placing_prep_files = null;
                            if (paramList.contains(FORCE_CONFIG_OUT_PUT_LOCATION_FLAG)) {
                                int out_loc = paramList.indexOf(FORCE_CONFIG_OUT_PUT_LOCATION_FLAG);
                                if (paramList.size() > out_loc) {
                                    dir_for_placing_prep_files = paramList.remove(out_loc + 1);
                                }
                                paramList.remove(FORCE_CONFIG_OUT_PUT_LOCATION_FLAG);
                                printout(0, null, "Output location =" + dir_for_placing_prep_files);
                            }
                            if (!dir_for_placing_prep_files.endsWith(File.separator)) {
                                dir_for_placing_prep_files = dir_for_placing_prep_files + File.separator;
                            }
                            if (!paramList.isEmpty()) {
                                if (paramList.get(0).startsWith("-")) {
                                    printout(0, null, "The instruction " + paramList.get(0) + " was not found");
                                } else {
                                    prepareTemplate(paramList.get(0).trim(), dir_for_placing_prep_files, false, false, null, null);
                                }

                            } else {
                                prepareTemplate(null, dir_for_placing_prep_files, false, false, null, null);
                            }
                        } else if (paramList.contains(DOWNLOAD_FILES_FLAG)) {
                            paramList.remove(DOWNLOAD_FILES_FLAG);
                            getFiles(paramList, verbosemode);
                        } else if (paramList.contains(DIFF_FLAG)) {
                            boolean erorr = false;
                            String dir_for_placing_prep_files = null;
                            long skip_larger = -1;
                            paramList.remove(DIFF_FLAG);
                            boolean skip_checksum = false;
                            String old_host = null;
                            if (paramList.remove(SKIP_CHECKUSM)) {
                                skip_checksum = true;
                            }
                            boolean recursive = false;
                            if (!paramList.isEmpty()) {
                                if (paramList.remove(PREPARE_TO_ADD_FILES_RECURSIVLY_FLAG)) {
                                    recursive = true;
                                }
                            }
                            boolean usedefault = false;
                            if (!paramList.isEmpty()) {
                                if (paramList.contains(USE_ALLDEFAULT_VALUES_FLAG)) {
                                    usedefault = true;
                                    paramList.remove(USE_ALLDEFAULT_VALUES_FLAG);
                                }
                            }
                            long skip_smaller = -1;
                            if (paramList.contains(SKIP_SMALL_FILES_FLAG)) {
                                int out_loc = paramList.indexOf(SKIP_SMALL_FILES_FLAG);
                                if (paramList.size() > (out_loc + 1)) {
                                    String skipsize = paramList.get(out_loc + 1).trim();
                                    if (skipsize.matches("[0-9]+")) {
                                        skip_smaller = new Long(skipsize);
                                        printout(0, null, "Not processing files smaller than " + skipsize + " Mb");
                                    }
                                }
                            }
                            if (paramList.contains(FORCE_CONFIG_OUT_PUT_LOCATION_FLAG)) {
                                int out_loc = paramList.indexOf(FORCE_CONFIG_OUT_PUT_LOCATION_FLAG);
                                if (paramList.size() > out_loc) {
                                    dir_for_placing_prep_files = paramList.remove(out_loc + 1);
                                }
                                paramList.remove(FORCE_CONFIG_OUT_PUT_LOCATION_FLAG);
                                printout(0, null, "Output location =" + dir_for_placing_prep_files);
                            }

                            if (paramList.contains(UPDATE_CHANGE_SERVER)) {
                                int out_loc = paramList.indexOf(UPDATE_CHANGE_SERVER);
                                if ((paramList.size() + 1) > out_loc) {
                                    old_host = paramList.remove(out_loc + 1);
                                } else {
                                    printout(0, null, "Error : After " + UPDATE_CHANGE_SERVER + " the servername should be provided");
                                    erorr = true;
                                }
                                paramList.remove(UPDATE_CHANGE_SERVER);
                                printout(0, null, "Output location =" + dir_for_placing_prep_files);
                            }


                            if (dir_for_placing_prep_files == null) {
                                printout(0, null, "Error : outout location missing , specify this with -configout");
                            } else {
                                if (!erorr) {
                                    if (paramList.isEmpty()) {
                                        getDiff(null, verbosemode, skip_larger, skip_smaller, skip_checksum, dir_for_placing_prep_files, recursive, old_host);
                                    } else {
                                        getDiff(paramList.get(0), verbosemode, skip_larger, skip_smaller, skip_checksum, dir_for_placing_prep_files, recursive, old_host);
                                    }
                                }
                            }

                        } else if (paramList.contains(MEERGE_FLAG)) {
                            paramList.remove(MEERGE_FLAG);
                            printout(0, null, "Merge two files");
                        } else if (paramList.contains(DELETE_FLAG)) {
                            paramList.remove(DELETE_FLAG);
                            int skip_larger = -1;
                            boolean updatecurrent = false;
                            boolean usedefault = false;
                            long skip_smaller = -1;
                            boolean skip_checksum = false;
                            if (paramList.remove(SKIP_CHECKUSM)) {
                                skip_checksum = true;
                            }
                            if (paramList.contains(SKIP_SMALL_FILES_FLAG)) {
                                int out_loc = paramList.indexOf(SKIP_SMALL_FILES_FLAG);
                                if (paramList.size() > (out_loc + 1)) {
                                    String skipsize = paramList.get(out_loc + 1).trim();
                                    if (skipsize.matches("[0-9]+")) {
                                        skip_smaller = new Long(skipsize);
                                        printout(0, null, "Not processing files smaller than " + skipsize + " Mb");
                                    }
                                }
                            }
                            if (!paramList.isEmpty()) {
                                if (paramList.contains(SKIP_LARGE_FILES_FLAG)) {
                                    int out_loc = paramList.indexOf(SKIP_LARGE_FILES_FLAG);
                                    if (paramList.size() > (out_loc + 1)) {
                                        String skipsize = paramList.remove(out_loc + 1).trim();
                                        if (skipsize.matches("[0-9]+")) {
                                            skip_larger = new Integer(skipsize);
                                            printout(0, null, "Not processing files larger than " + skipsize + " Mb");
                                        }
                                    }
                                    paramList.remove(SKIP_LARGE_FILES_FLAG);
                                }
                            }
                            if (!paramList.isEmpty()) {
                                if (paramList.contains(USE_ALLDEFAULT_VALUES_FLAG)) {
                                    usedefault = true;
                                    paramList.remove(USE_ALLDEFAULT_VALUES_FLAG);
                                }
                            }
                            if (!paramList.isEmpty() && paramList.remove(UPDATE_CURRENT)) {
                                updatecurrent = true;
                            }
                            String dir_for_placing_prep_files = null;
                            String dir_to_use = null;
                            if (paramList.contains(FORCE_CONFIG_OUT_PUT_LOCATION_FLAG)) {
                                int out_loc = paramList.indexOf(FORCE_CONFIG_OUT_PUT_LOCATION_FLAG);
                                if (paramList.size() > out_loc) {
                                    dir_for_placing_prep_files = paramList.remove(out_loc + 1);
                                }
                                paramList.remove(FORCE_CONFIG_OUT_PUT_LOCATION_FLAG);
                                printout(0, null, "Output location =" + dir_for_placing_prep_files);
                            }
                            if (!paramList.isEmpty() && paramList.contains(FORCE_IN_PUT_LOCATION_FLAG)) {
                                int out_loc = paramList.indexOf(FORCE_IN_PUT_LOCATION_FLAG);
                                if (paramList.size() > (out_loc + 1)) {
                                    dir_to_use = paramList.remove(out_loc + 1);
                                }
                                paramList.remove(FORCE_IN_PUT_LOCATION_FLAG);
                            }
                            if (paramList.size() > 0) {
                                printout(0, null, "Delete using " + paramList.get(0));
                                delete(paramList.get(0), false, null, skip_larger, skip_smaller, skip_checksum);
                            } else {
                                printout(0, null, "Preparing for deletion.");
                                create_temaplate_for_update(null, null, dir_to_use,
                                        dir_for_placing_prep_files, null, usedefault, skip_larger, skip_smaller, updatecurrent, skip_checksum);
//                                 printout(0,null, "Error 1n: " + DELETE_FLAG + " requires atleast on more argument (a file with record to be deleted).");
                            }
                        } else if (paramList.contains(UPDATE_FLAG)) {
                            long skip_larger = -1;
                            paramList.remove(UPDATE_FLAG);
                            String dir_read_from_prep_files = null;
                            String dir_for_placing_prep_files = null;
                            boolean usedefault = false;
                            boolean updatecurrent = false;
                            long skip_smaller = -1;
                            boolean skip_checksum = false;

                            String old_host = null;
                            if (paramList.remove(SKIP_CHECKUSM)) {
                                skip_checksum = true;
                            }
                            boolean recursive = false;
                            if (!paramList.isEmpty()) {
                                if (paramList.remove(PREPARE_TO_ADD_FILES_RECURSIVLY_FLAG)) {
                                    recursive = true;
                                }
                            }
                            if (paramList.contains(SKIP_SMALL_FILES_FLAG)) {
                                int out_loc = paramList.indexOf(SKIP_SMALL_FILES_FLAG);
                                if (paramList.size() > (out_loc + 1)) {
                                    String skipsize = paramList.get(out_loc + 1).trim();
                                    if (skipsize.matches("[0-9]+")) {
                                        skip_smaller = new Long(skipsize);
                                        printout(0, null, "Not processing files smaller than " + skipsize + " Mb");
                                    }
                                }
                            }
                            if (!paramList.isEmpty()) {
                                if (paramList.contains(SKIP_LARGE_FILES_FLAG)) {
                                    int out_loc = paramList.indexOf(SKIP_LARGE_FILES_FLAG);
                                    if (paramList.size() > (out_loc + 1)) {
                                        String skipsize = paramList.remove(out_loc + 1).trim();
                                        if (skipsize.matches("[0-9]+")) {
                                            skip_larger = new Integer(skipsize);
                                            printout(0, null, "Not processing files larger than " + skipsize + " Mb");
                                        }
                                    }
                                    paramList.remove(SKIP_LARGE_FILES_FLAG);
                                }
                            }
                            if (!paramList.isEmpty() && paramList.remove(UPDATE_CURRENT)) {
                                updatecurrent = true;
                            }
                            if (!paramList.isEmpty()) {
                                if (paramList.contains(USE_ALLDEFAULT_VALUES_FLAG)) {
                                    usedefault = true;
                                    paramList.remove(USE_ALLDEFAULT_VALUES_FLAG);
                                }
                            }
                            if (!paramList.isEmpty()) {
                                if (paramList.contains(FORCE_IN_PUT_LOCATION_FLAG)) {
                                    int out_loc = paramList.indexOf(FORCE_IN_PUT_LOCATION_FLAG);
                                    if (paramList.size() > (out_loc + 1)) {
                                        dir_read_from_prep_files = paramList.remove(out_loc + 1);
                                    }
                                    paramList.remove(FORCE_IN_PUT_LOCATION_FLAG);
                                    printout(0, null, "Reading from =" + dir_read_from_prep_files);
                                }
                            }
                            if (paramList.contains(FORCE_CONFIG_OUT_PUT_LOCATION_FLAG)) {
                                int out_loc = paramList.indexOf(FORCE_CONFIG_OUT_PUT_LOCATION_FLAG);
                                if (paramList.size() > out_loc) {
                                    dir_for_placing_prep_files = paramList.remove(out_loc + 1);
                                }
                                paramList.remove(FORCE_CONFIG_OUT_PUT_LOCATION_FLAG);
                                printout(0, null, "Output location =" + dir_for_placing_prep_files);
                            }

                            if (paramList.contains(UPDATE_CHANGE_SERVER)) {
                                int out_loc = paramList.indexOf(UPDATE_CHANGE_SERVER);
                                if (paramList.size() > out_loc) {
                                    old_host = paramList.remove(out_loc + 1);
                                }
                                paramList.remove(UPDATE_CHANGE_SERVER);
                                printout(0, null, "Output location =" + dir_for_placing_prep_files);
                            }
                            if (paramList.size() > 0) {
                                update(dir_read_from_prep_files, dir_for_placing_prep_files,
                                        paramList.get(0), false, usedefault, updatecurrent, recursive, skip_larger, skip_smaller, skip_checksum, old_host);
                            } else {
                                if (updatecurrent) {
                                    if (dir_for_placing_prep_files == null) {
                                        printout(0, null, "Output location was null, this operation requires a temporary location to place the templates. specify this with " + FORCE_CONFIG_OUT_PUT_LOCATION_FLAG);
                                    } else {
                                        update(dir_read_from_prep_files, dir_for_placing_prep_files,
                                                null, false, usedefault, updatecurrent, recursive, skip_larger, skip_smaller, skip_checksum, old_host);
                                    }
                                } else {
                                    create_temaplate_for_update(null, null, dir_read_from_prep_files,
                                            dir_for_placing_prep_files, null, usedefault, skip_larger, skip_smaller, updatecurrent, skip_checksum);
                                }
                            }
//                            printout(0,null, "Synchronize ");
////                            synchronize();
//                            ArrayList<String> tmp_l = new ArrayList<String>(1);
//                            tmp_l.add("files.id=157||files.name=SCP:dmed4929:/home/sabryr/tmp/rand/batch_3/report_11/file_list.txt||filetype.name=35||");
//                            call_synchronize(tmp_l);
                        } else if (paramList.remove(PREPARE_TAGS_FLAG) || paramList.remove(PREPARE_TAGS_FLAG2)) {
                            //Handle when more than one file                       
                            if (paramList.remove(NOW)) {
                                boolean getChecksum = false;
                                if (paramList.size() > 1) {
                                    String tag = paramList.remove(paramList.size() - 1);
                                    String target = null;
                                    for (int i = 0; i < paramList.size(); i++) {
                                        String c_file = paramList.get(i);
                                        if (!c_file.contains("=") || getChecksum) {
                                            getChecksum = true;
                                            Path p = Paths.get(c_file);
                                            if (Files.exists(p) && Files.isReadable(p)) {
                                                ArrayList<String> tmp_l = new ArrayList<>();
                                                tmp_l.add(p.toAbsolutePath().toString());
                                                HashMap<String, String> getHashes_map = getHashes(tmp_l, -1, -1, false);
                                                if (target == null) {
                                                    target = "FILES.CHECKSUM=" + getHashes_map.get(tmp_l.get(0));
                                                } else {
                                                    if (i == 0) {
                                                        target = target + "||FILES.CHECKSUM=" + getHashes_map.get(tmp_l.get(0));
                                                    } else {
                                                        target = target + "||" + getHashes_map.get(tmp_l.get(0));
                                                    }
                                                }
                                            }
                                        } else {
                                            if (target == null) {
                                                target = c_file;
                                            } else {
                                                target = target + "||" + c_file;
                                            }
                                        }

                                    }
                                    printout(0, null, "result=" + tag(target, tag));
                                } else {
                                    printout(0, null, "Error: requires atleast one file and a tag");
                                }
                            } else {
                                String dir_for_placing_prep_files = getPWD();
                                if (paramList.contains(FORCE_CONFIG_OUT_PUT_LOCATION_FLAG)) {
                                    int out_loc = paramList.indexOf(FORCE_CONFIG_OUT_PUT_LOCATION_FLAG);
                                    if (paramList.size() > out_loc) {
                                        dir_for_placing_prep_files = paramList.remove(out_loc + 1);
                                    }
                                    paramList.remove(FORCE_CONFIG_OUT_PUT_LOCATION_FLAG);
                                    printout(0, null, "Output location =" + dir_for_placing_prep_files);
                                }
                                if (!dir_for_placing_prep_files.endsWith(File.separator)) {
                                    dir_for_placing_prep_files = dir_for_placing_prep_files + File.separator;
                                }
                                if (paramList.remove(INVESTIGATE) && !paramList.isEmpty()) {
                                    investigateTag(paramList.get(paramList.size() - 1), dir_for_placing_prep_files);
                                } else {
                                    if (paramList.isEmpty()) {
                                        createTag(dir_for_placing_prep_files);
                                    }
                                }
                            }

                        } else if (paramList.remove(CREATE_TAG_SOURCE)) {
                            if (paramList.size() == 1) {
                                createTagSource(paramList.get(0));
                            } else {
                                printout(0, null, "Error: exactly one agrgument, which is the tag source name allowed, after the instruction " + CREATE_TAG_SOURCE);
                            }
                        } else {
                            printout(0, null, "Error 1k: invalid argument use -help to print valid arguments");
                        }

                    } else {
                        printout(0, null, "Error 1L: invalid argument use -help to print valid arguments");
                    }
                } else {
                    printout(0, null, "Error 1m: authentication failed");
                }

            }
        } else {
            printout(0, null, "Error 1q: invalid argument use -help to print valid arguments");
        }
        instructs[0] = verbosemode;
        instructs[1] = skip_formating;
        return instructs;

    }
//    private void commit(String[] args) throw EgenVarException{
//        
//    }

    /*
     MethodID=2
     */
    private void printValidArguments(boolean argumentmissing) {
        if (argumentmissing) {
            printout(0, null, "Arguments missing !");
        } else {
            printout(0, null, "Allowed arguments:");

        }
        ArrayList<String> help_keys_l = new ArrayList<>(Helpmap.keySet());
        String help_text = "";
        for (int i = 0; i < help_keys_l.size(); i++) {
            help_text = help_text + "\n" + help_keys_l.get(i) + "\n" + printformater_terminal(Helpmap.get(help_keys_l.get(i)), 80);
        }
        printout(0, null, help_text);
    }

    /*
     MethodID=3
     */
    private void printAvailable() {
        List result_l = advancedQueryHandler("tablename2feature.showinsearch=1", "tablename2feature.table_name,tablename2feature.column_nm,tablename2feature.description",
                true, false, false, false, false, true, false, 0, true);
        if (result_l != null) {
            Collections.sort(result_l);
            ArrayList<String[]> to_dsiply = new ArrayList<>(result_l.size());
            int len_table_name = 0;
            if (!result_l.isEmpty()) {
                to_dsiply.add(new String[]{"Search_format", "Description\n"});
                for (int i = 0; i < result_l.size(); i++) {
                    String[] c_result = result_l.get(i).toString().split("\\|\\|");
                    if (c_result.length > 2) {
                        String table_name = c_result[0].split("=")[c_result[0].split("=").length - 1];
                        String column_nm = c_result[1].split("=")[c_result[0].split("=").length - 1];
                        String description = c_result[2].split("=")[c_result[0].split("=").length - 1];
                        if (description != null && !description.equalsIgnoreCase("NULL")) {
                            String[] _a = new String[2];
                            _a[0] = table_name + "." + column_nm;
                            _a[1] = description;
                            to_dsiply.add(_a);
                            if (len_table_name < _a[0].length()) {
                                len_table_name = _a[0].length();
                            }
                        }
                    }
                }
                len_table_name = len_table_name + 2;
                StringBuilder str = new StringBuilder();
                for (int i = 0; i < to_dsiply.size(); i++) {
                    String[] _a = to_dsiply.get(i);
                    String table_name = _a[0];
                    String ts = "";
                    for (int j = 0; j < ((len_table_name - table_name.length())); j++) {
                        ts = ts + "_";
                    }
                    str.append(table_name);
                    str.append(ts);
                    str.append(_a[1] + "\n");
                }
                printout(0, null, str.toString());
            } else {
                printout(0, null, "Empty result");
            }
        } else {
            printout(0, null, "Error: Server did not respond");
        }

    }

    /*
     MethodID=4
     */
    private String printformater_file(ArrayList<String> commnet_l, int comment_line_length) {
        String comment = "";
        for (int i = 0; i < commnet_l.size(); i++) {
            String c_commnet = commnet_l.get(i);
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
                    if (!new_comment.startsWith("#")) {
                        new_comment = "#  " + new_comment;
                    }
                    if (!comment.isEmpty()) {
                        comment = comment + "\n";
                    }
                    comment = comment + new_comment;
                    new_comment = "";
                }
            }
            if (!new_comment.isEmpty()) {
                if (!new_comment.startsWith("#")) {
                    new_comment = "#  " + new_comment;
                }
                if (!comment.isEmpty()) {
                    comment = comment + "\n";
                }
                comment = comment + new_comment;
            }
        }

        String line = "#_";
        for (int i = 0; i < comment_line_length; i++) {
            line = line + "_";

        }
        comment = comment + "\n" + line;
        return comment;
    }

    /*
     MethodID=5
     */
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

    /*
     MethodID=6
     */
    private void printResults(List result, boolean printlinenumber, boolean verbosemode) {
        if (result != null) {
            for (int i = 0; i < result.size(); i++) {
                if (printlinenumber) {
                    printout(0, null, i + ") " + result.get(i));
                } else {
                    printout(0, null, result.get(i));
                }
            }

        } else {
            printout(0, null, "No results found");
        }

    }

    /*
     MethodId=7;
     */
    private String updateSoftware(boolean force) {
        printout(0, null, "Starting Update.. @ " + Timing.getDateTime());
        double latest_version = getLatestVersion(null);
        if (latest_version < 0) {
            printout(0, null, "Automatic update failed as the server could not find the correct file. Please update manually");
        } else if (force || latest_version > version) {
            if (latest_version > version) {
                printout(0, null, "A newer version is available. Latest version=" + latest_version + ". Installing updates.");
            } else {
                printout(0, null, "Force upgrading");
            }
            String targetdir = getClassPath().toAbsolutePath().toString() + File.separatorChar;
            try {
//                String classpath = System.getProperty("java.class.path");
//                if (classpath != null && !classpath.isEmpty() && classpath.endsWith("jar")) {
//                    try {
//                        File path_file = new File(classpath);
//                        targetdir = path_file.getParentFile().getAbsolutePath() + File.separatorChar;
//                    } catch (Exception ex) {
//                        printout(0, null, "Warning: " + ex.getMessage());
//                    }
                if (targetdir != null) {
                    String new_link = getUpdateLink();
                    printout(0, null, "926 " + new_link);
                    if (new_link != null && !new_link.isEmpty() && new_link.endsWith("jar")) {
                        Files.deleteIfExists(Paths.get(targetdir + "egenv_new.jar"));
                        printout(0, null, "Downloading new version started @" + Timing.getDateTime());
                        if (download(new_link, targetdir + "egenv_new.jar")) {
                            printout(0, null, "Downloading ended");
                            printout(0, null, "Installation started @" + Timing.getDateTime());
                            System.out.println("");
                            Files.copy(Paths.get(targetdir + "egenv_new.jar"), Paths.get(targetdir + "egenv.jar"), StandardCopyOption.REPLACE_EXISTING);
//                                Files.deleteIfExists(Paths.get(targetdir + "egenv_new.jar"));
                            printout(0, null, "New version intalled (" + latest_version + ")  on " + Timing.getDateTime() + ")");
                            return "OK";
                        } else {
                            printout(0, null, "Error 6a: downloading new version failed ");
                        }
                    } else {
                        printout(0, null, "Automatic update failed as the server could not find the correct file. Please update manually");
                    }
                } else {
                    printout(0, null, "Automatic update failed as resolving the classpath got complicated. Please update manually");
                }
//                } else {
//                    printout(0, null, "Automatic update failed as resolving the classpath got complicated. Please update manually");
//                }
            } catch (Exception ex) {
                printout(0, null, "Warning 6e: " + ex.getMessage());
                ex.printStackTrace();
            }
        } else {
            printout(0, null, "Your software is uptodate !. No changes made.");
        }
        return null;
    }

    /*
     MethodID=8
     */
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
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return result;
    }


    /*
     MethodID=9
     * type
     * 0=normal search
     * 1=children of tags before finding results
     * 2=Children during ?
     * 3=childen_at_the_end
     * 4=parent_at_the_end 
     **/
    public String[] search(String source, String target, boolean exact, int type) {
//        System.out.println("2244 source="+source+" target="+target);
        List result_l = advancedQueryHandler(source, target, exact, false, true, false, false, false, false, type, false);
        String[] results_a = new String[1];
        if (result_l == null || result_l.isEmpty()) {
            results_a[0] = "NA";
        } else {
            results_a = new String[result_l.size()];
            for (int i = 0; i < result_l.size(); i++) {
                results_a[i] = result_l.get(i).toString();
            }
        }
        return results_a;
    }

    /*
     MethodID=10
     * Possible return values 
     
     **/
    public int tag(String target, String tag) {
        int result = -1;
//        ProgressMonitor p = new ProgressMonitor(PROGRESS_MONT_INTERVAL, "EGM16");
//        (new Thread(p)).start();
        Object[] ob_a = startProgress(1);
        if (authenticate(false, null)) {
            try {
                no.ntnu.medisin.egenvar.webservice.AuthenticateService_Service service = new no.ntnu.medisin.egenvar.webservice.AuthenticateService_Service();
                no.ntnu.medisin.egenvar.webservice.AuthenticateService port = service.getAuthenticateServicePort();
                BindingProvider bp = (BindingProvider) port;
                bp.getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, getWSDL(false));
                result = port.tag(user_email, authentication_string, target, tag);
                stopProgress(ob_a);
            } catch (Exception ex) {
                ex.printStackTrace();
                printout(0, null, "Error 16c:Failed to send error report " + ex.getMessage());
            }
        } else {
            printout(0, null, "Error 16d: Authentication failed. use -diagnose get more information");
        }
//        p.cancel("EGM16");
//        stopProgress(ob_a);

//        List result_l = advancedQueryHandler(source, target, exact, false, true, false, false, false, false, 0, false);

//        if (result_l != null) {
//        String[] results_a = result_l.toArray(new String[result_l.size()]);
//            results_a = new String[result_l.size()];
//            for (int i = 0; i < result_l.size(); i++) {
//                results_a[i] = result_l.get(i).toString();
//            }
//        }
        return result;
    }

    /*
     MethodID=25
     Possible return values 
     * 1: Adding new record  was a success or the records already existed. 
     * The parameter was treated as a file path
     * 2: : Adding new record  was a success or the records already existed. 
     * The parameter was treated as set of values
     * -1: Parameters missing 
     * -2: The parameter was treated as a file path, but could not find the file
     * or the file did not have read access
     * -3 :Creating new records failed or the server did not respond
     *-4 :Creating new records failed. 
     * 
     
     */
    public int add(String param) {
        int result = -1;
        if (param != null && !param.isEmpty()) {
            String[] param_a = param.split("\\|\\|");
            if (param_a.length == 1 && param_a[0].indexOf("=") < 0) {
                Path p = Paths.get(param_a[0]);
                if (Files.exists(p) && Files.isReadable(p)) {
                    ArrayList<String> tmp_l = new ArrayList<>();
                    tmp_l.add(p.toAbsolutePath().toString());
                    HashMap<String, String> getHashes_map = getHashes(tmp_l, -1, -1, false);
                    HashMap<String, HashMap<String, String>> files2cdetails_map = (getFileDetails(tmp_l, null, null));
                    getHashes_map.put("FILES.CHECKSUM", getHashes_map.remove(tmp_l.get(0)));
                    getHashes_map.putAll(files2cdetails_map.get(tmp_l.get(0)));
                    getHashes_map.remove("HOSTNAME");
//                    System.out.println("488 getHashes_map=" + getHashes_map);
//                    System.out.println("489 " + map2list(getHashes_map, true));
                    List result_l = setUsingTemplate(map2list(getHashes_map, true));
                    if (result_l == null || result_l.size() != 3) {
                        result = -3;
                        printout(1, null, "Error 198: Connection to the server failed");
                    } else {
                        String p_result = (String) result_l.get(0);
                        String undo = (String) result_l.get(1);
                        String messages = (String) result_l.get(2);
                        printout(0, null, result);
                        if (!messages.isEmpty()) {
                            result = -4;
                            printout(0, null, "messages=" + messages);
                        } else {
                            printout(0, null, p_result);
                            result = 1;
                        }
                    }
                } else {
                    result = -2;
                }
            } else {
                HashMap<String, String> getHashes_map = new HashMap<>();
                for (int i = 0; i < param_a.length; i++) {
                    int eq_indx = param_a[i].indexOf("=");
                    if (eq_indx > 0) {
                        getHashes_map.put(param_a[i].substring(0, eq_indx).trim(), param_a[i].substring(eq_indx + 1).trim());
                    } else {
                        System.out.println("Error in " + param_a[i]);
                    }
                }
                List result_l = setUsingTemplate(map2list(getHashes_map, true));

                if (result_l == null || result_l.size() != 3) {
                    result = -3;
                } else {
                    String p_result = (String) result_l.get(0);
                    String undo = (String) result_l.get(1);
                    String messages = (String) result_l.get(2);
                    if (!messages.isEmpty()) {
                        printout(0, null, "messages=" + messages);
                        result = -4;
                    } else {
                        printout(0, null, p_result);

                        result = 2;
                    }
                }
            }
        }

        return result;
    }

    /*
     MethodID=9
     * The list should always contain the ID of the record to be updated
     * it should take the form 
     * :8180 [FILES.NAME==/med/010/14458_ex/i.csv_3, FILES.ID==1]
     * Return values
     * 
     **/
    public List update(List parma_l) {
        List result_l = new ArrayList<>();
        if (parma_l != null) {
            result_l = call_update(parma_l, false);
        }
        return result_l;
    }

    /*
     MethodID=9
     * The list should always contain the ID of the record to be updated
     * it should take the form 
     * :8180 [FILES.NAME==/med/010/14458_ex/i.csv_3, FILES.ID==1]
     * Return values
     * 
     **/
//    public List delete(String... param_a) {
//        List parma_l = new ArrayList<>();
//        for (int i = 0; i < param_a.length; i++) {
//            parma_l.add(param_a[i]);
//        }
//        List result_l = new ArrayList<>();
//        if (parma_l == null || parma_l.size() == 1) {
//        } else {
//            parma_l.remove(0);
//            result_l = call_delete(parma_l, false);
//
//        }
//
//        return result_l;
//    }

    /*
     MethodID=9
     * The list should always contain the ID of the record to be updated
     * it should take the form 
     * :8180 [FILES.NAME==/med/010/14458_ex/i.csv_3, FILES.ID==1]
     * Return values
     * 
     **/
    public String[] delete(String drc, String todlete) {
        List parma_l = new ArrayList<>();
        parma_l.add(drc);
        parma_l.add(todlete);
        List result_l = call_delete(parma_l, false);
        String[] result_a = new String[result_l.size()];
        for (int i = 0; i < result_l.size(); i++) {
            result_a[i] = result_l.get(i).toString();

        }
        return result_a;
    }
    /*
     MethodID=11
     */

    private void prepareForAdding_controller(ArrayList<String> paramList,
            String dir_for_placing_prep_files, boolean skip_checksum,
            String batch_nm, String report_nm, String person_emial) {
        if (dir_for_placing_prep_files == null) {
            dir_for_placing_prep_files = getPWD();
        }
        if (!dir_for_placing_prep_files.endsWith(File.separator)) {
            dir_for_placing_prep_files = dir_for_placing_prep_files + File.separator;
        }
        String dir_to_use = null;
        boolean skipdbcheck = false;
        boolean skipdzip = false;
        int skip_larger = -1;
        int skip_smaller = -1;
        if (!paramList.isEmpty()) {
            if (paramList.remove(SKIP_DB_TEST_FLAG)) {
                skipdbcheck = true;
                printout(0, null, "Skip the check against database");
            }
        }
        if (!paramList.isEmpty()) {
            if (paramList.remove(SKIP_archives_FLAG)) {
                skipdzip = true;
                printout(0, null, "Skiping gz, tar and zip files");
            }
        }

        if (!paramList.isEmpty()) {
            if (paramList.contains(SKIP_LARGE_FILES_FLAG)) {
                int out_loc = paramList.indexOf(SKIP_LARGE_FILES_FLAG);
                if (paramList.size() > (out_loc + 1)) {
                    String skipsize = paramList.remove(out_loc + 1).trim();
                    if (skipsize.matches("[0-9]+")) {
                        skip_larger = new Integer(skipsize);
                        printout(0, null, "Not processing files larger than " + skipsize + " Mb");
                    }
                }
                paramList.remove(SKIP_LARGE_FILES_FLAG);
            }
        }
        if (!paramList.isEmpty()) {
            boolean usedefault = false;
            boolean canrenameFolders = false;
            if (paramList.remove(USE_ALLDEFAULT_VALUES_FLAG)) {
                usedefault = true;
            }
            boolean updatecurrent = false;
            if (!paramList.isEmpty() && paramList.remove(UPDATE_CURRENT)) {
                updatecurrent = true;
            }
            if (paramList.remove(RENAMING_FOLDERS_REQUEST)) {
                canrenameFolders = true;
            }

            if (!paramList.isEmpty() && paramList.contains(FORCE_IN_PUT_LOCATION_FLAG)) {
                int out_loc = paramList.indexOf(FORCE_IN_PUT_LOCATION_FLAG);
                if (paramList.size() > (out_loc + 1)) {
                    dir_to_use = paramList.remove(out_loc + 1);
                }
                paramList.remove(FORCE_IN_PUT_LOCATION_FLAG);
            }
            if (!paramList.isEmpty()) {
                if (paramList.contains(GET_AVAILABLE_TABLES_FLAG)) {
                    printAvailable();
                } else if (paramList.contains(CLEAR__FLAG)) {
                    prepare_to_add(true, false, dir_for_placing_prep_files, dir_to_use, skipdbcheck,
                            skipdzip, skip_larger, skip_smaller, usedefault,
                            canrenameFolders, false, skip_checksum, batch_nm, report_nm, person_emial);
                } else if (paramList.contains(TEMPLATE_FLAG)) {
                    paramList.remove(TEMPLATE_FLAG);

                    if (!paramList.isEmpty()) {
                        if (paramList.contains(UPDATE_FLAG) || paramList.contains(DELETE_FLAG)) {
                            create_temaplate_for_update(null, null, dir_to_use,
                                    dir_for_placing_prep_files, null, usedefault,
                                    skip_larger, skip_larger, updatecurrent, skip_checksum);
                        } else {
                            prepareTemplate(paramList.get(0).trim(), dir_for_placing_prep_files, false, false, null, null);
                        }
                    } else {
                        prepareTemplate(null, dir_for_placing_prep_files, false, false, null, null);
                    }
                } else if (paramList.contains(UPDATE_FLAG) || paramList.contains(DELETE_FLAG)) {
                    create_temaplate_for_update(null, null, dir_to_use,
                            dir_for_placing_prep_files, null, usedefault, skip_larger, skip_larger, updatecurrent, skip_checksum);
                } else if (paramList.contains(PREPARE_TAGS_FLAG) || paramList.contains(PREPARE_TAGS_FLAG2)) {
                    paramList.remove(PREPARE_TAGS_FLAG);
                    paramList.remove(PREPARE_TAGS_FLAG2);
                    if (paramList.remove(INVESTIGATE) && !paramList.isEmpty()) {
                        investigateTag(paramList.get(paramList.size() - 1), dir_for_placing_prep_files);
                    } else {
                        createTag(dir_for_placing_prep_files);
                    }
                } else {
                    printout(0, null, "Error 1P: invalid argument use -help to print valid arguments");
                }
            } else {
                prepare_to_add(true, false, dir_for_placing_prep_files, dir_to_use, skipdbcheck,
                        skipdzip, skip_larger, skip_smaller,
                        usedefault, canrenameFolders, false, skip_checksum,
                        batch_nm, report_nm, person_emial);
            }

        } else {
            prepare_to_add(true, false, dir_for_placing_prep_files, dir_to_use, skipdbcheck,
                    skipdzip, skip_larger, skip_smaller, false, false, false,
                    skip_checksum, batch_nm, report_nm, person_emial);
        }
    }

    /*
     MethodId=
     * @relationships
     * @type
     * @using_type
     * e.g. relate "parent_hash||child_hash" "FILES", "FILES.CHECKSUM";
     */
    public int relate(String relationships, String table, String use_column) {
        if (table == null) {
            table = "FILES";
        }
        table = table.toUpperCase();
        if (use_column == null) {
            if (table.indexOf(".") > 2) {
                use_column = table;
                table = table.substring(0, table.indexOf("."));
            } else {
                if (table.equals("FILES")) {
                    use_column = "FILES.CHECKSUM";
                } else {
                    use_column = "NAME";
                }
            }
        }

//        System.out.println("2544 use_column ="+use_column +" table="+table);
        int result = -1;
//        String[] relationships_a = relationships.trim().split(",");
        if (relationships != null && table != null) {
            ArrayList<String> relationships_l = new ArrayList<>();
            relationships_l.add(relationships);
            String result_s = call_AddRelationshipFromFile(relationships_l, table, use_column, null, false);
            result = 1;
        } else {
            System.out.println("Error: needs exactly three parameter");
        }
        return result;
    }

    private boolean checkDescriptionValidity(String description) {
        if (description == null || description.isEmpty()) {
            return false;
        } else if (allowed_dummy_alues.contains(description)) {
            return true;
        } else if (description.equalsIgnoreCase("NA") || description.equalsIgnoreCase("No Description available") || description.length() < MINIMUM_DESCRIPTION_LENGTH) {
            return false;
        } else {
            description = description.toUpperCase();
            HashMap<Character, Double> charpercentage_map = new HashMap<Character, Double>();
            char[] split = description.toCharArray();
            int none_space_length = 0;
            for (int i = 0; i < split.length; i++) {
                if (split[i] != ' ') {
                    none_space_length++;
                    if (charpercentage_map.containsKey(split[i])) {
                        charpercentage_map.put(split[i], charpercentage_map.get(split[i]) + 1);
                    } else {
                        charpercentage_map.put(split[i], 1.0);
                    }
                }
            }
            ArrayList<Character> char_Key_l = new ArrayList<Character>(charpercentage_map.keySet());
            int number_of_over_30s = 0;
            boolean over70_found = false;
            for (int i = 0; i < char_Key_l.size(); i++) {
                double percetage = (charpercentage_map.get(char_Key_l.get(i)) * 100) / none_space_length;
                if (percetage > 70) {
                    over70_found = true;
                }
                if (percetage > 30) {
                    number_of_over_30s++;
                }
            }
            if (char_Key_l.size() < 2 || over70_found || number_of_over_30s > 2) {
                printout(0, null, "Error 67: The description " + description + " seems to be random text, please describe your entries.");
                return false;
            } else {
                return true;
            }
        }
    }
    /*MethodID=14 */

    private void createFileEntries_direct(ArrayList<String> files_l, String report_batch,
            String report, String person_email, boolean verbose) {
        int result = 0;
        for (int i = 0; i < files_l.size(); i++) {
            if (!Files.exists(Paths.get(files_l.get(i)))) {
                System.out.println("Error : file not found " + files_l.remove(i));
                i--;
            } else if (!Files.isReadable(Paths.get(files_l.get(i)))) {
                System.out.println("Error : file has no read acceess " + files_l.remove(i));
                i--;
            } else if (!Files.isRegularFile(Paths.get(files_l.get(i))) && !Files.isSymbolicLink(Paths.get(files_l.get(i)))) {
                System.out.println("File is not a regula file " + files_l.remove(i));
                i--;
            }
        }
        if (report_batch == null) {
            report_batch = getParent();
        }
        if (report == null) {
            report = getPWD(true).getFileName().toString();
        }
        if (!files_l.isEmpty()) {
            HashMap<String, String> getHashes_map = getHashes(files_l, -1, -1, false);

            HashMap<String, HashMap<String, String>> files2cdetails_map = (getFileDetails(files_l, report_batch, report));
            for (int i = 0; i < files_l.size(); i++) {
//                System.out.println("2563 " + files_l.get(i));
                if (getHashes_map.containsKey(files_l.get(i)) && files2cdetails_map.containsKey(files_l.get(i))) {
                    HashMap<String, String> details_map = new HashMap<>();
                    details_map.put("FILES.CHECKSUM", getHashes_map.get(files_l.get(i)));
                    details_map.put("PERSON.EMAIL", user_email);
                    details_map.putAll(files2cdetails_map.get(files_l.get(i)));
                    details_map.remove("HOSTNAME");
//                    System.out.println("2599 " + details_map);
                    List result_l = setUsingTemplate(map2list(details_map, true));
                    if (result_l == null || result_l.size() != 3) {
                        result = -3;
                        printout(1, null, "Error 198: Connection to the server failed");
                    } else {
                        String p_result = (String) result_l.get(0);
                        String undo = (String) result_l.get(1);
                        String messages = (String) result_l.get(2);
                        printout(0, null, result);
                        if (!messages.isEmpty()) {
                            result = -4;
                            printout(0, null, "messages=" + messages);
                        } else {
                            printout(0, null, p_result);
                            result = 1;
                        }
                    }
                } else {
                    System.out.println("Error: failed to process " + files_l.get(i));
                }
            }


        }

    }

    /*MethodID=14 */
    private void createFileEntries(String dir_read_from_prep_files, boolean verbose) {
        String filenm = null;
        if (dir_read_from_prep_files != null) {
            if (Files.isRegularFile(Paths.get(dir_read_from_prep_files))) {
                filenm = dir_read_from_prep_files;
                dir_read_from_prep_files = null;
            }
        }
        String report_directory = getPWD();
        if ((filenm == null && report_directory == null) || report_directory.isEmpty()) {
            printout(0, null, "Error 14l: Failed to get current working directory");
        } else {
            if (filenm == null) {
                String report_to_read_from_directory = report_directory;
                if (dir_read_from_prep_files != null) {
                    report_to_read_from_directory = dir_read_from_prep_files;
                }
                if (!report_to_read_from_directory.endsWith(File.separator)) {
                    report_to_read_from_directory = report_to_read_from_directory + File.separatorChar;
                }
                filenm = report_to_read_from_directory + PREPARE_TO_ADD_CUSTOM_TABLE_FILENM.replace("<TABLE>", "files");
            }
            readTemplate_new(filenm, verbose, true, dir_read_from_prep_files);
//            DIRECTORY_MARKER_REPORT
        }
    }

    private void recursive_prepare(ArrayList<Path> files_to_use_l, ArrayList<String> avoid_l,
            boolean skipdbcheck, boolean skipdzip, long skip_larger, long skip_smaller,
            String prepfilenm_files_warings, HashMap<String, ArrayList<String>> error_map,
            ArrayList<String> check_with_db_file_l, HashMap<String, HashMap<String, String>> files2cdetails_map,
            HashMap<String, String> files2checksum_map, ArrayList<Path> refined_file_l,
            String batch_nm, String report_nm, HashMap<String, ArrayList<String>> duplicate_hash_l_map, boolean skip_checksum) {
        printout(0, null, "Number of files to be processed " + files_to_use_l.size());
        if (skipdzip) {
            avoid_l.add(".*gz");
            avoid_l.add(".*zip");
            avoid_l.add(".*tar.*");
        }
//        HashMap<String, ArrayList<String>> duplicate_name_map = new HashMap<>();
        printout(0, null, "Analysing targets ..");
//        ArrayList<Path> refined_file_l = new ArrayList<>(files_to_use_l.size());
        ArrayList<String> full_file_names_l = new ArrayList<>(files_to_use_l.size());
        HashMap<String, ArrayList<String>> duplicate_name_map = new HashMap<>();
        int last_progress = 0;
        for (int j = 0; j < files_to_use_l.size(); j++) {
            int progrss = ((j * 100) / files_to_use_l.size());
            if (progrss > last_progress) {
                if (progrss % 10 == 0) {
                    System.out.print(progrss + "%");
                }
                if (progrss % 2 == 0) {
                    System.out.print(".");
                }
                last_progress = progrss;
            }
            Path file = files_to_use_l.get(j);
            if (Files.isReadable(file)) {
                refined_file_l.add(file);
                try {
                    String path = file.toAbsolutePath().toString();
                    String c_file_nm = file.getFileName().toString();
                    full_file_names_l.add(path);
                    if (duplicate_name_map.containsKey(c_file_nm)) {
                        duplicate_name_map.get(c_file_nm).add(path);
                    } else {
                        ArrayList<String> tmp = new ArrayList<>(1);
                        tmp.add(path);
                        duplicate_name_map.put(c_file_nm, tmp);
                    }
                } catch (Exception ex) {
                    printout(0, null, "Error 3a: Failed to retrieve directory path");
                    System.exit(20);
                }

            }
        }
        printout(0, null, "\nSelected files to be processed " + full_file_names_l.size() + "\n");
        printout(0, null, "..100%");
        ArrayList<String> duplicate_list = new ArrayList<>(duplicate_name_map.keySet());
        ArrayList<String> duplicate_name_l = new ArrayList<>(10);
        printout(0, null, "Cheking for duplicates ");
        for (int i = 0; i < duplicate_list.size(); i++) {
            int progrss = ((i * 100) / duplicate_list.size());
            if (progrss > last_progress) {
                if (progrss % 10 == 0) {
                    System.out.print(".");
                }
                last_progress = progrss;
            }
            if (duplicate_name_map.get(duplicate_list.get(i)).size() > 1) {
                duplicate_name_l.add(duplicate_name_map.get(duplicate_list.get(i)).toString());
            }
        }
        printout(0, null, "");
        duplicate_name_map.clear();
        duplicate_list.clear();

//        Path[] file_a = refined_file_l.toArray(new Path[refined_file_l.size()]);

        printout(0, null, "Analysing files.(" + full_file_names_l.size() + ") started @" + Timing.getDateTime());
//        HashMap<String, ArrayList<String>> duplicate_hash_l_map = new HashMap<>();
        ArrayList<String> error_hash_l = new ArrayList<>(full_file_names_l.size());
//        ArrayList<String> check_with_db_file_l = new ArrayList<>(500);
        last_progress = -1;
//        HashMap<String, String> files2checksum_map = getHashes(full_file_names_l, skip_larger, skip_smaller);
//        HashMap<String, HashMap<String, String>> files2cdetails_map = getFileDetails(full_file_names_l);
        files2checksum_map.putAll(getHashes(full_file_names_l, skip_larger, skip_smaller, skip_checksum));
        printout(0, null, " " + batch_nm + "\t" + report_nm);
        files2cdetails_map.putAll(getFileDetails(full_file_names_l, batch_nm, report_nm));

        int file_list_size = full_file_names_l.size();
        printout(0, null, "Finished calculating " + files2checksum_map.keySet().size() + " checksums  for " + file_list_size + " files  @" + Timing.getDate());
        printout(0, null, "Starting checksum comparisen " + Timing.getDateTime());
        for (int i = 0; i < file_list_size; i++) {
            String c_loc = full_file_names_l.get(i);
            int progrss = ((i * 100) / file_list_size);
            if (progrss > last_progress) {
                if (progrss % 10 == 0) {
                    System.out.print(progrss + "%");
                }
                if (progrss % 2 == 0) {
                    System.out.print(".");
                }
                last_progress = progrss;
            }
            String c_hash = files2checksum_map.get(c_loc);
            if (!skipdbcheck) {
                check_with_db_file_l.add(c_hash);
            }
            if (c_hash == null || c_hash.isEmpty() || c_hash.equalsIgnoreCase("NA")) {
                error_hash_l.add(c_loc);
            } else {
                if (duplicate_hash_l_map.containsKey(c_hash)) {
                    duplicate_hash_l_map.get(c_hash).add(c_loc);
                } else {
                    ArrayList<String> tmp = new ArrayList<>(1);
                    tmp.add(c_loc);
                    duplicate_hash_l_map.put(c_hash, tmp);
                }
            }
        }

        printout(0, null, "..75%");
        printout(0, null, "Reporting duplicates started @" + Timing.getDateTime());
        ArrayList<String> duplicate_hash_list = new ArrayList<>(duplicate_hash_l_map.keySet());
        ArrayList<String> real_duplicate_hash_l = new ArrayList<>(10);
        last_progress = 0;
        for (int i = 0; i < duplicate_hash_list.size(); i++) {
//             printout(0,null, "1465 "+duplicate_hash_list.get(i)+" => "+duplicate_hash_l_map.get(duplicate_hash_list.get(i)));
            int progrss = ((i * 100) / duplicate_hash_list.size());
            if (progrss > last_progress) {
                if (progrss % 10 == 0) {
                    System.out.print(progrss + "%");
                }
                if (progrss % 2 == 0) {
                    System.out.print(".");
                }
                last_progress = progrss;
            }
            if (duplicate_hash_l_map.get(duplicate_hash_list.get(i)).size() > 1) {
                real_duplicate_hash_l.add(duplicate_hash_l_map.get(duplicate_hash_list.get(i)).toString().replaceAll("\\][\\s]+\\[", "]\n["));
            }
        }

        last_progress = 0;
        printout(0, null, "..100%");
        printout(0, null, "Creating content started @" + Timing.getDateTime());
//        HashMap<String, ArrayList<String>> error_map = new HashMap<>();

        if (error_hash_l.isEmpty() && real_duplicate_hash_l.isEmpty() && duplicate_name_l.isEmpty()) {
        } else {
            printout(0, null, "\nWarning! there were issues needing attention.");
            if (!error_hash_l.isEmpty()) {
                error_map.put("FILES_FAILED_IN_CHECKSUM_CALCULATION", error_hash_l);
                printout(0, null, "There were " + error_hash_l.size() + " files where the checksum calculation failed or avoided");
            }
            if (!real_duplicate_hash_l.isEmpty()) {
                error_map.put("FILES_FOUND_TO_BE_DUPLICATE_USING_CHECKSUM", real_duplicate_hash_l);
                printout(0, null, "There were " + real_duplicate_hash_l.size() + " instances duplicates found using checksum (more than one file having the same checksum)");
            }
            if (!duplicate_name_l.isEmpty()) {
                error_map.put("FILES_WITH_THE_SAME_NAME", duplicate_name_l);
                printout(0, null, "There were " + duplicate_name_l.size() + " duplications in the file names (This is just information, will not cause any problem)");
            }
            printout(0, null, "Please refer the file " + prepfilenm_files_warings + " for full discription of the warnings");
        }
//        writeWarningfile(prepfilenm_files_warings, directoryfor_output_files, error_map);
//        boolean addmark = false;
//        createContent_file_recurse(file_a,
//                check_with_db_file_l, files2cdetails_map, files2checksum_map,
//                prepfilenm_files, directoryfor_output_files, "#Creating files\n" + commnetstext,
//                skipdbcheck, addmark, 0, report_batch_root);


    }
    /*
     * MethodID=20
     * should modify this method to get report and batch information inaddtion
     * to file type, from the server This method prepares the description files
     * used by the -add command. There are four description files <br /> 1. For
     * batch - egenvar_describe_batch.txt <br /> 2. For report -
     * .egenvar_describe_report.txt<br /> 3. For files -
     * .egenvar_describe_file.txt <br /> 4. For fule types -
     * .egenvar_describe_filetypes.txt<br />
     *
     * @recreate : if is this true, all previous description files will be
     * deleted before creating new ones. Otherwise all new infromation are
     * appended to existing description files.
     */

    private void prepare_to_add(boolean recreate, boolean update, String directoryfor_output_files, String currentdir,
            boolean skipdbcheck, boolean skipdzip, long skip_larger, long skip_smaller, boolean usedefault,
            boolean canrenameFolders, boolean recursive, boolean skip_checksum,
            String batch_nm, String report_nm, String person_emial) {
        boolean forceoutloc = true;

        if (currentdir == null) {
            currentdir = getPWD();
        }
        printout(0, null, "Processing location =" + currentdir);
        if (currentdir != null && !currentdir.isEmpty()) {
            if (directoryfor_output_files == null) {
                forceoutloc = false;
                directoryfor_output_files = currentdir;
            }

            File c_dir = new File(currentdir);
            File out_dir = new File(directoryfor_output_files);
            if (c_dir.isDirectory() && c_dir.canRead() && out_dir.isDirectory() && out_dir.canWrite()) {
                if (report_nm == null) {
                    report_nm = c_dir.getName();
                }
                try {
                    File batch_dir = null;
                    try {
                        batch_dir = new File(c_dir.getParentFile().getCanonicalPath());
                        if (batch_dir.isDirectory() && batch_dir.canRead()) {
                        } else {
                            batch_dir = new File(directoryfor_output_files);
                        }
                    } catch (IOException ex) {
                    }
                    if (batch_dir == null) {
                        batch_dir = new File(directoryfor_output_files);
                    }
                    if (batch_dir.isDirectory()) {
                        if (batch_dir.isDirectory()) {

                            String batch_folder_location = null;
                            String batch_Parent_folder_location = null;
                            String report_folder_location = null;
                            String report_Parent_folder_location = null;
                            try {
                                batch_folder_location = batch_dir.getCanonicalPath() + File.separatorChar;
                                batch_Parent_folder_location = batch_dir.getParentFile().getCanonicalPath() + File.separatorChar;
                                report_folder_location = c_dir.getCanonicalPath() + File.separatorChar;
                                report_Parent_folder_location = c_dir.getParentFile().getCanonicalPath() + File.separatorChar;
                            } catch (IOException ex) {
                                printout(0, null, "Error 20a: Failed to retrieve directory path " + ex.getMessage());
                                System.exit(20);
                            }

                            if (batch_folder_location != null && batch_Parent_folder_location != null
                                    && report_folder_location != null && report_Parent_folder_location != null) {
                                String[] new_batch_location_a;
                                if (batch_nm == null) {
                                    batch_nm = batch_dir.getName();
                                    new_batch_location_a = getUserResponseToLocation(batch_folder_location, batch_Parent_folder_location,
                                            batch_nm, "Batch", "report_batch.name", batch_nm, "report_batch.id", false, usedefault, canrenameFolders);
                                } else {
                                    new_batch_location_a = getUserResponseToLocation(batch_folder_location, batch_Parent_folder_location,
                                            batch_nm, "Batch", "report_batch.name", batch_nm, "report_batch.id", false, true, canrenameFolders);
                                }
                                batch_nm = new_batch_location_a[1];
                                if (!new_batch_location_a[0].equals(batch_folder_location)) {
                                    try {
                                        batch_dir = new File(new_batch_location_a[0]);
                                        File new_batch_loc = new File(new_batch_location_a[0]);
                                        if (new_batch_loc.isDirectory() && new_batch_loc.canRead()) {
                                            report_folder_location = new_batch_loc.getCanonicalPath() + File.separatorChar + report_nm;
                                            report_Parent_folder_location = new_batch_loc.getCanonicalPath() + File.separatorChar;
                                        }
                                    } catch (IOException ex) {
                                        printout(0, null, "Error 20b: Failed to retrieve directory path " + ex.getMessage());
                                        System.exit(20);
                                    }
                                }
                                String ifexists_q = "";
                                if (new_batch_location_a[2] != null) {
                                    ifexists_q = report_nm + ";&&report.report_batch_id=" + new_batch_location_a[2] + "";
                                } else {
                                    ifexists_q = report_nm;
                                }
//                                    String[] new_report_location = getUserResponseToLocation(report_folder_location, report_Parent_folder_location, report_nm, "Report", "SELECT 1 from report where name='" + NAME_TO_CHECK + "' and report_batch_id in (select id from report_batch where report_batch.name='" + batch_nm + "') limit 1");

                                String[] new_report_location;
                                if (report_nm == null) {
                                    new_report_location = getUserResponseToLocation(report_folder_location, report_Parent_folder_location,
                                            report_nm, "Report", "report.name", ifexists_q, "report.id", false, usedefault, canrenameFolders);
                                } else {
                                    new_report_location = getUserResponseToLocation(report_folder_location, report_Parent_folder_location,
                                            report_nm, "Report", "report.name", ifexists_q, "report.id", false, true, canrenameFolders);
                                }
                                report_nm = new_report_location[1];
                                File newReport_loc = new File(new_report_location[0]);
                                printout(0, null, "Batch name= " + batch_nm + "\tReport name= " + report_nm);
                                String prepfilenm_batch = PREPARE_TO_ADD_CUSTOM_TABLE_FILENM.replace("<TABLE>", "report_batch");
                                String prepfilenm_reports = PREPARE_TO_ADD_CUSTOM_TABLE_FILENM.replace("<TABLE>", "report");
//                                String prepfilenm_fyletypes = PREPARE_TO_ADD_CUSTOM_TABLE_FILENM.replace("<TABLE>", "filetype");
                                String prepfilenm_files = PREPARE_TO_ADD_CUSTOM_TABLE_FILENM.replace("<TABLE>", "files");
                                String prepfilenm_files_warings = PREPARE_TO_ADD_CUSTOM_TABLE_FILENM.replace("<TABLE>", "files_warnings");
                                if (directoryfor_output_files == null) {
                                    directoryfor_output_files = new_report_location[0];
                                }
                                if (!directoryfor_output_files.endsWith(File.separator)) {
                                    directoryfor_output_files = directoryfor_output_files + File.separatorChar;

                                }
                                if (!directoryfor_output_files.endsWith(File.separator)) {
                                    directoryfor_output_files = directoryfor_output_files + File.separatorChar;
                                }
//                                        if (new_batch_location_a[0].endsWith(File.separatorChar + "")) {
//                                            prepfilenm_reports = new_batch_location_a[0] + PREPARE_TO_ADD_CUSTOM_TABLE_FILENM.replace("<TABLE>", "report");
//                                        } else {
//                                            prepfilenm_reports = new_batch_location_a[0] + File.separatorChar + PREPARE_TO_ADD_CUSTOM_TABLE_FILENM.replace("<TABLE>", "report");
//                                        }
                                boolean proceed = false;
                                if (!usedefault && recreate) {
                                    File r_file = new File(directoryfor_output_files + prepfilenm_reports);
                                    File rb_file = new File(directoryfor_output_files + prepfilenm_batch);
//                                    File f_file = new File(directoryfor_output_files + prepfilenm_fyletypes);
                                    File ft_file = new File(directoryfor_output_files + prepfilenm_files);
                                    if (r_file.isFile() || rb_file.isFile() || ft_file.isFile()) {
                                        String ans = getuserInputSameLine("Recreate mode. All changes to \n" + PREPARE_TO_ADD_CUSTOM_TABLE_FILENM.replace("<TABLE>", "report") + ",\n" + PREPARE_TO_ADD_CUSTOM_TABLE_FILENM.replace("<TABLE>", "files") + ",\n" + PREPARE_TO_ADD_CUSTOM_TABLE_FILENM.replace("<TABLE>", "filetype") + "\n" + PREPARE_TO_ADD_CUSTOM_TABLE_FILENM.replace("<TABLE>", "report_batch") + "\n files will be lost.", "Press, Y to confirm|N to cancel ");
                                        if (analyseUserResponse(ans, true, false) == 0) {
                                            proceed = true;
                                        }
                                    } else {
                                        proceed = true;
                                    }
                                } else {
                                    proceed = true;
                                }
                                if (proceed) {
                                    ArrayList<String> new_updatable_l = new ArrayList<>(3);
                                    new_updatable_l.add(FILES_CHECKSUM);
                                    new_updatable_l.add(FILES_SIZE);
                                    new_updatable_l.add(FILES_LASTMODIFIED);

                                    if (forceoutloc) {
                                        HashMap<String, ArrayList<String>> found_in_db_reports_and_batches_map =
                                                prepare_to_add_files(newReport_loc, recreate, update, directoryfor_output_files,
                                                report_folder_location, person_emial, report_nm, batch_nm, prepfilenm_files, prepfilenm_files_warings,
                                                new_updatable_l, skipdbcheck, skipdzip, skip_larger, skip_smaller, usedefault, recursive, skip_checksum);
                                    } else {
                                        HashMap<String, ArrayList<String>> found_in_db_reports_and_batches_map =
                                                prepare_to_add_files(newReport_loc, recreate, update, directoryfor_output_files,
                                                report_folder_location, person_emial, report_nm, batch_nm, prepfilenm_files, prepfilenm_files_warings,
                                                new_updatable_l, skipdbcheck, skipdzip, skip_larger, skip_smaller, usedefault, recursive, skip_checksum);
                                    }
                                } else {
                                    printout(0, null, "Operation terminated by user");
                                    System.exit(20);
                                }
                            } else {
                                printout(0, null, "Error 20c: Failed to retrieve directory information ");
                                System.exit(20);
                            }
//                            } else {
//                                 printout(0,null, "Error 20e: Communication error with the server");
//                            }
                        } else {
                            printout(0, null, "Error 20f: Failed to access parent directory");
                            System.exit(6);
                        }
                    } else {
                        printout(0, null, "Error 20g: Failed to access parent directory");
                        System.exit(4);
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                    printout(0, null, "Error 20h: Failed to retrieve directory path " + ex.getMessage());
                    System.exit(20);

                }
            } else {
                printout(0, null, "Error 20i: Failed to access current directory");
                System.exit(3);
            }

        } else {
            printout(0, null, "Error 20j: Failed to determine current directory");
            System.exit(2);
        }
//        } 
    }
    /*
     MethodID=3
     */

    private HashMap<String, ArrayList<String>> prepare_to_add_files(File newReport_loc, boolean recreate, boolean update, String dir_out_put,
            String report_folder_location, String person_emial, String report_nm, String batch_nm, String prepfilenm_files, String prepfilenm_files_warings,
            ArrayList<String> new_updatable_l, boolean skipdbcheck, boolean skipdzip, long skip_larger, long skip_smaller,
            boolean usedefault, boolean recursive, boolean skip_checksum) {
        if (dir_out_put == null) {
            dir_out_put = report_folder_location;
        }
        ArrayList<String> avoid_l = new ArrayList<>(1);
        if (skipdzip) {
            avoid_l.add(".*gz");
            avoid_l.add(".*zip");
            avoid_l.add(".*tar.*");
        }
        printout(0, null, "Retrieving file information from " + newReport_loc);
        HashMap<String, ArrayList<File>> result_map = getAllinSubFolders(newReport_loc, avoid_l, new HashMap<String, ArrayList<File>>(), true, 0, 0, usedefault);
//         printout(0,null, "2363 newReport_loc="+newReport_loc+" result_map="+result_map );
        ArrayList<File> expanded_file_l = new ArrayList<>(1);
        ArrayList<File> avoided_file_l = new ArrayList<>(1);
        if (result_map != null && !result_map.isEmpty()) {
            expanded_file_l = result_map.get("OK");
            avoided_file_l = result_map.get("AVOID");
        }

        if (expanded_file_l == null) {
            expanded_file_l = new ArrayList<>(1);
        }
        ArrayList<File> full_file_l = new ArrayList<>(expanded_file_l.size());
        ArrayList<String> full_file_names_l = new ArrayList<>(expanded_file_l.size());
        HashMap<String, ArrayList<String>> duplicate_name_map = new HashMap<>();
        printout(0, null, "Analysing targets ..");
        int last_progress = 0;
        for (int j = 0; j < expanded_file_l.size(); j++) {
            int progrss = ((j * 100) / expanded_file_l.size());
            if (progrss > last_progress) {
                if (progrss % 10 == 0) {
                    System.out.print(progrss + "%");
                }
                if (progrss % 2 == 0) {
                    System.out.print(".");
                }
                last_progress = progrss;
            }

            File file = expanded_file_l.get(j);
            if (file.canRead()) {
                String c_file_nm = file.getName();
                full_file_l.add(file);
                try {
                    String path = file.getCanonicalPath();
                    full_file_names_l.add(path);
                    if (duplicate_name_map.containsKey(c_file_nm)) {
                        duplicate_name_map.get(c_file_nm).add(path);
                    } else {
                        ArrayList<String> tmp = new ArrayList<>(1);
                        tmp.add(path);
                        duplicate_name_map.put(c_file_nm, tmp);
                    }
                } catch (IOException ex) {
                    printout(0, null, "Error 3a: Failed to retrieve directory path");
                    System.exit(20);
                }

            }
        }
        printout(0, null, "..100%");
        printout(0, null, "\nSelected files to be processed " + full_file_names_l.size() + "\n");

        ArrayList<String> duplicate_list = new ArrayList<>(duplicate_name_map.keySet());
        ArrayList<String> duplicate_name_l = new ArrayList<>(10);
        printout(0, null, "Cheking for duplicates ");
        for (int i = 0; i < duplicate_list.size(); i++) {
            int progrss = ((i * 100) / duplicate_list.size());
            if (progrss > last_progress) {
                if (progrss % 10 == 0) {
                    System.out.print(".");
                }
                last_progress = progrss;
            }
            if (duplicate_name_map.get(duplicate_list.get(i)).size() > 1) {
                duplicate_name_l.add(duplicate_name_map.get(duplicate_list.get(i)).toString());
            }
        }
        printout(0, null, "");
        duplicate_name_map.clear();
        duplicate_list.clear();

        File[] file_a = full_file_l.toArray(new File[full_file_l.size()]);

        printout(0, null, "Analysing files.(" + full_file_names_l.size() + ") started @" + Timing.getDateTime());
        HashMap<String, ArrayList<String>> duplicate_hash_l_map = new HashMap<>();
        ArrayList<String> error_hash_l = new ArrayList<>(full_file_names_l.size());
        ArrayList<String> check_with_db_file_l = new ArrayList<>(500);
        last_progress = -1;
        HashMap<String, String> files2checksum_map = getHashes(full_file_names_l, skip_larger, skip_smaller, skip_checksum);
        HashMap<String, HashMap<String, String>> files2cdetails_map = getFileDetails(full_file_names_l, null, null);

        int file_list_size = full_file_names_l.size();
        printout(0, null, "Finished calculating " + files2checksum_map.keySet().size() + " checksums  for " + file_list_size + " files  @" + Timing.getDate());
        printout(0, null, "Starting checksum comparisen " + Timing.getDateTime());
        for (int i = 0; i < file_list_size; i++) {
            String c_loc = full_file_names_l.get(i);
            int progrss = ((i * 100) / file_list_size);
            if (progrss > last_progress) {
                if (progrss % 10 == 0) {
                    System.out.print(progrss + "%");
                }
                if (progrss % 2 == 0) {
                    System.out.print(".");
                }
                last_progress = progrss;
            }
            String c_hash = files2checksum_map.get(c_loc);
            if (!skipdbcheck) {
                check_with_db_file_l.add(c_hash);
            }
            if (c_hash == null || c_hash.isEmpty() || c_hash.equalsIgnoreCase("NA")) {
                error_hash_l.add(c_loc);
            } else {
                if (duplicate_hash_l_map.containsKey(c_hash)) {
                    duplicate_hash_l_map.get(c_hash).add(c_loc);
                } else {
                    ArrayList<String> tmp = new ArrayList<>(1);
                    tmp.add(c_loc);
                    duplicate_hash_l_map.put(c_hash, tmp);
                }
            }
        }
        printout(0, null, "..75%");
        printout(0, null, "Reporting duplicates started @" + Timing.getDateTime());
        ArrayList<String> duplicate_hash_list = new ArrayList<>(duplicate_hash_l_map.keySet());
        ArrayList<String> real_duplicate_hash_l = new ArrayList<>(10);
        last_progress = 0;
        for (int i = 0; i < duplicate_hash_list.size(); i++) {
            int progrss = ((i * 100) / duplicate_hash_list.size());
            if (progrss > last_progress) {
                if (progrss % 10 == 0) {
                    System.out.print(progrss + "%");
                }
                if (progrss % 2 == 0) {
                    System.out.print(".");
                }
                last_progress = progrss;
            }
            if (duplicate_hash_l_map.get(duplicate_hash_list.get(i)).size() > 1) {
                real_duplicate_hash_l.add(duplicate_hash_l_map.get(duplicate_hash_list.get(i)).toString().replaceAll("\\][\\s]+\\[", "]\n["));
            }
        }
        printout(0, null, "..100%");
        printout(0, null, "Creating content started @" + Timing.getDateTime());
        duplicate_name_map.clear();
        HashMap<String, String> new_files_overide_map = new HashMap<>();

        new_files_overide_map.put(REPORT_NAME, report_nm);
        new_files_overide_map.put(REPORT_BATCH_NAME, batch_nm);
        HashMap<String, ArrayList<String>> error_map = new HashMap<>();
        if (error_hash_l.isEmpty() && real_duplicate_hash_l.isEmpty() && duplicate_name_l.isEmpty()) {
        } else {
            printout(0, null, "\nWarning! there were issues needing attention.");
            if (!error_hash_l.isEmpty()) {
                error_map.put("FILES_FAILED_IN_CHECKSUM_CALCULATION", error_hash_l);
                printout(0, null, "There were " + error_hash_l.size() + " files where the checksum calculation failed or avoided");
            }
            if (!real_duplicate_hash_l.isEmpty()) {
                error_map.put("FILES_FOUND_TO_BE_DUPLICATE_USING_CHECKSUM", real_duplicate_hash_l);
                printout(0, null, "There were " + real_duplicate_hash_l.size() + " cheksum duplicates");
            }
            if (!duplicate_name_l.isEmpty()) {
                error_map.put("FILES_WITH_THE_SAME_NAME", duplicate_name_l);
                printout(0, null, "There were " + duplicate_name_l.size() + " duplications in the file names (This is just information, will not cause any problem)");
            }
            printout(0, null, "Please refer the file " + prepfilenm_files_warings + " for full discription of the warnings");
        }
        if (avoided_file_l != null && !avoided_file_l.isEmpty()) {
            ArrayList<String> avoided_file_loc_l = new ArrayList<>(avoided_file_l.size());
            for (int i = 0; i < avoided_file_l.size(); i++) {
                try {
                    avoided_file_loc_l.add(avoided_file_l.get(i).getCanonicalPath());
                } catch (IOException e) {
                }
            }

            error_map.put("FILES_NOT_EVALUATED", avoided_file_loc_l);
            printout(0, null, "Some files were avoided during processing (" + avoided_file_l.size() + " files) for details refer " + prepfilenm_files_warings + "");
        }

        writeWarningfile(prepfilenm_files_warings, dir_out_put, error_map);
        boolean addmark = false;
        HashMap<String, ArrayList<String>> found_in_db_reports_and_batches_map = createContent_file(file_a, person_emial, report_nm, batch_nm,
                check_with_db_file_l, files2cdetails_map, files2checksum_map,
                prepfilenm_files, dir_out_put, "#Creating files\n" + commnetstext,
                skipdbcheck, addmark, 0, recursive);
        return found_in_db_reports_and_batches_map;
    }
    /*
     trouble /media/raid/hunt_biobank/backup/rsa_keys
     */

    private void split_To_hierarchy(Path current_dir, String dir_for_placing_prep_files, int depth, boolean transfer_members) {
        String temp_type = "REPORT";
        if (current_dir == null) {
            current_dir = getPWD(true);
        }
        int limit = 100;
        LinkedHashMap<String, String> path2name_map = new LinkedHashMap();
        LinkedHashMap<String, ArrayList<Path>> parent2child_map = new LinkedHashMap<>();
        parent2child_map.put(current_dir.toString(), null);
        path2name_map.put(current_dir.toString(), current_dir.getFileName().toString());
        DirectoryStream<Path> ds = null;
        DirectoryStream.Filter<Path> filter = new DirectoryStream.Filter<Path>() {
            @Override
            public boolean accept(Path file) throws IOException {
                return (Files.isReadable(file) && Files.isDirectory(file));

            }
        };
        try {
            while (parent2child_map.values().contains(null) && depth > 0) {
                printout(0, null, "." + depth);
                depth--;
                ArrayList<String> parent_dir_l = new ArrayList<>(parent2child_map.keySet());
                for (int i = 0; (i < parent_dir_l.size() && !parent_dir_l.isEmpty()); i++) {
                    if (parent2child_map.get(parent_dir_l.get(i)) != null) {
                        parent_dir_l.remove(i);
                        i--;
                    }
                }
                for (int i = 0; i < parent_dir_l.size(); i++) {
                    System.out.print(".");
                    String c_p_d = parent_dir_l.get(i);
                    ds = Files.newDirectoryStream(Paths.get(c_p_d), filter);
                    ArrayList<Path> c_p_l = new ArrayList<>();
                    Iterator<Path> path_l = ds.iterator();
                    int count = 0;
                    while (path_l.hasNext() && count < limit) {
                        System.out.print(".");
                        Path c_file = path_l.next();
                        if (Files.isDirectory(c_file) && Files.isReadable(c_file)) {
                            count++;
                        }
                    }
                    ds.close();
                    if (count < limit) {
                        ds = Files.newDirectoryStream(Paths.get(c_p_d), filter);
                        path_l = ds.iterator();
                        count = 0;
                        while (path_l.hasNext()) {
                            System.out.print(".");
                            Path c_file = path_l.next();
                            if (Files.isDirectory(c_file) && Files.isReadable(c_file)) {
                                count++;
                                c_p_l.add(c_file);
                                parent2child_map.put(c_file.toString(), null);
                                decideName(path2name_map, c_file);
                            }
                        }
                        ds.close();
                    } else {
                        printout(0, null, "Warning ! : " + c_p_d + " contains " + count + " subfolders, "
                                + "this is more than the limit (" + limit + ") that could be "
                                + "handlled automatically. Not processing this directory");
                    }
                    parent2child_map.put(c_p_d, c_p_l);
                    decideName(path2name_map, Paths.get(c_p_d));


                }
            }
            ArrayList<String> parent_dir_l = new ArrayList<>(parent2child_map.keySet());
            for (int i = 0; i < parent_dir_l.size(); i++) {
                if (parent2child_map.get(parent_dir_l.get(i)) == null) {
                    parent2child_map.remove(parent_dir_l.get(i));
                }
            }
            ds.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            try {
                if (ds != null) {
                    ds.close();
                }
            } catch (IOException ex) {
            }
        }


        ArrayList<String> parent_dir_l = new ArrayList<>(parent2child_map.keySet());
        StringBuilder str = new StringBuilder();
        str.append("\n");
        for (int i = 0; i < parent_dir_l.size(); i++) {
            Path p_p = Paths.get(parent_dir_l.get(i));
            ArrayList<Path> c_p_l = parent2child_map.get(parent_dir_l.get(i));
            //progerss monitor
            for (int j = 0; j < c_p_l.size(); j++) {
                String parent_nm = path2name_map.get(p_p.toString());
                String child_nm = path2name_map.get(c_p_l.get(j).toString());
                if (parent_nm != null && child_nm != null) {
                    str.append(parent_nm);
                    str.append("\t");
                    str.append(child_nm);
                    String file_ids_link = "-1";
                    try {
                        file_ids_link = c_p_l.get(j).toRealPath().toString() + File.separator;
                    } catch (IOException ex) {
                    }
                    str.append("\t");
                    str.append(file_ids_link);
                    str.append("\t");
                    String mod_time = Timing.getDateTimeForFileName();
                    try {
                        mod_time = sdf_2.format(new Date(Files.getLastModifiedTime(c_p_l.get(j)).toMillis()));

                    } catch (IOException ex) {
                    }
                    str.append(mod_time);
                    str.append("\n");
                } else {
                    printout(0, null, "2652 parent_nm=" + parent_nm + " => "
                            + path2name_map.get(p_p.toString()) + " => " + p_p.toString());
                    printout(0, null, "2652  child_nm=" + child_nm + " => "
                            + path2name_map.get(c_p_l.get(j).toString()) + " => " + c_p_l.get(j).toString());
                }

            }

        }
        prepareTemplateRelationships(temp_type, str.toString(), dir_for_placing_prep_files, "\tREPORT.ENTRYDATE", true);

    }

    private void decideName(LinkedHashMap<String, String> path2name_map, Path new_path) {
        String name = null;
        if (path2name_map == null) {
            path2name_map = new LinkedHashMap<>();
        }
        if (new_path != null) {
            name = new_path.getFileName().toString();
            String path = new_path.toString();
            if (!path2name_map.containsKey(path)) {
                ArrayList<String> path2name_map_k_l = new ArrayList<>(path2name_map.keySet());
                boolean found = false;
                for (int i = 0; (!found && i < path2name_map_k_l.size()); i++) {
                    String c_nm = path2name_map.get(path2name_map_k_l.get(i));
                    if (c_nm.equalsIgnoreCase(name)) {
                        found = true;
                    }
                }
                if (found) {
                    if (new_path.getNameCount() > 1) {
                        name = new_path.getName(new_path.getNameCount() - 2) + "_" + new_path.getName(new_path.getNameCount() - 1);
                    } else {
                        name = path2name_map.size() + "_" + new_path.getFileName().toString();
                    }
                }
                path2name_map.put(path, name);
            }
        }
    }
//
//    private void prepare_hierarchy_REPORT(Path current_dir, String dir_for_placing_prep_files, int depth, boolean transfer_members) {
//        String type = "REPORT";
//        if (current_dir == null) {
//            current_dir = getPWD(true);
//        }
//        HashMap<String, ArrayList<Path>> parent2child_map = new HashMap<>();
//        parent2child_map.put(current_dir.toString(), null);
//        DirectoryStream<Path> ds = null;
//        HashMap<String, ArrayList<String>> dir2fileHash_map = new HashMap<>();
//        try {
//            while (parent2child_map.values().contains(null) && depth > 0) {
//                depth--;
//                ArrayList<String> parent_dir_l = new ArrayList<>(parent2child_map.keySet());
//                for (int i = 0; (i < parent_dir_l.size() && !parent_dir_l.isEmpty()); i++) {
//                    if (parent2child_map.get(parent_dir_l.get(i)) != null) {
//                        parent_dir_l.remove(i);
//                        i--;
//                    }
//                }
//                for (int i = 0; i < parent_dir_l.size(); i++) {
//                    String c_p_d = parent_dir_l.get(i);
//                    ds = Files.newDirectoryStream(Paths.get(c_p_d));
//                    ArrayList<Path> c_p_l = new ArrayList<>();
//                    Iterator<Path> path_l = ds.iterator();
//                    while (path_l.hasNext()) {
//                        Path c_file = path_l.next();
//                        if (Files.isDirectory(c_file, LinkOption.NOFOLLOW_LINKS) && Files.isReadable(c_file)) {
//                            c_p_l.add(c_file);
//                            parent2child_map.put(c_file.toString(), null);
//                        }
//                    }
//                    ds.close();
//                    parent2child_map.put(c_p_d, c_p_l);
//                }
//
//            }
//            ArrayList<String> parent_dir_l = new ArrayList<>(parent2child_map.keySet());
//            for (int i = 0; i < parent_dir_l.size(); i++) {
//                if (parent2child_map.get(parent_dir_l.get(i)) == null) {
//                    parent2child_map.remove(parent_dir_l.get(i));
//                }
//            }
//            ds.close();
//
//        } catch (IOException ex) {
//            ex.printStackTrace();
//        } finally {
//            try {
//                if (ds != null) {
//                    ds.close();
//                }
//            } catch (IOException ex) {
//            }
//        }
//
//        ArrayList<String> parent_dir_l = new ArrayList<>(parent2child_map.keySet());
//        StringBuilder str = new StringBuilder();
//        str.append("\n");
//        for (int i = 0; i < parent_dir_l.size(); i++) {
//            Path p_p = Paths.get(parent_dir_l.get(i));
//            ArrayList<Path> c_p_l = parent2child_map.get(parent_dir_l.get(i));
//            //progerss monitor
//
//            for (int j = 0; j < c_p_l.size(); j++) {
//                str.append(p_p.getFileName());
//                str.append("\t");
//                str.append(c_p_l.get(j).getFileName().toString());
//                String file_ids_link = "-1";
//                try {
//                    LoadRecursive pf = new LoadRecursive(true);
//                    Files.walkFileTree(c_p_l.get(j), EnumSet.of(FileVisitOption.FOLLOW_LINKS), Integer.MAX_VALUE, pf);
//                    HashMap<String, String> tmp_map = getHashes(pf.getOKlist(), -1, -1, true);
//                    if (tmp_map != null && !tmp_map.isEmpty()) {
//                        Collection hash_cl = tmp_map.values();
////                        List links_l = advancedQueryHandler("files.checksum=", "files.id", true, false, false, false, false, false, false);
////                        List res_l = advancedQueryHandler("FILES.CHECKSUM=" + hash_cl.toString().replace("[", "").replace("]", "").replaceAll("\\s", "").replaceAll(",", "||"), "files.id", true, false, false, false, false, false, false);
//                        List res_l = advancedQueryHandler("FILES.CHECKSUM=" + hash_cl.toString().replace("[", "").replace("]", "").replaceAll("\\s", "").replaceAll(",", "||"), "files.id", true, false, false, false, false, false, false);
//
//                        file_ids_link = res_l.toString().replace("[", "").replace("]", "").replaceAll("\\s", "");
//                    }
//
//                } catch (Exception ex) {
//                    ex.printStackTrace();
//                } finally {
//                }
//
//                str.append("\t");
//                str.append(file_ids_link);
//                str.append("\n");
//            }
//
//        }
//        prepareTemplateRelationships(type, str.toString(), dir_for_placing_prep_files, true);
//
//    }

    /*
     MethodID=80
     * 
     */
    private HashMap<String, String> getHashes(ArrayList<String> full_file_names_l, long maxsize, long skip_smaller, boolean skip_checksum) {
        printout(0, null, "Calculating checksum started @" + Timing.getDateTime());
        int last_progress = 0;
        HashMap<String, String> file2hash = new HashMap<>();

        long lastadded = 0;
        boolean printed = false;
        System.out.print("..");
        for (int i = 0; i < full_file_names_l.size(); i++) {
            int progrss = ((i * 100) / full_file_names_l.size());
            if (progrss > last_progress) {
                if (progrss % 10 == 0) {
                    System.out.print(progrss + "%");
                }
                if (progrss % 2 == 0) {
                    System.out.print(".");
                }
                last_progress = progrss;
            }
            if (GetChecksum.getChecksumMap_size() < 100) {
                (new Thread(new GetChecksum(full_file_names_l.get(i), maxsize, skip_smaller, skip_checksum))).start();
                printed = false;
                lastadded = (new GregorianCalendar()).getTimeInMillis();
            } else {
                i--;
                long current = (new GregorianCalendar()).getTimeInMillis();
                if (!printed && ((current - lastadded) > 30000)) {
                    printout(0, null, "Heavy traffic ! waiting until large files are processed. Current Progress=" + progrss + "%");
                    printed = true;
                    lastadded = (new GregorianCalendar()).getTimeInMillis();
                }
            }
            try {
                file2hash.putAll(GetChecksum.getcompletedChecksumMap());
            } catch (Exception ex) {
                printout(0, null, "Error 80a : " + ex.getMessage());
                try {
                    Thread.sleep(300000);
                } catch (InterruptedException ex1) {
                    printout(0, null, "Error 80b: " + ex1.getMessage());
                }

            }

        }
        HashMap<String, String> tmp_map = GetChecksum.getChecksumMap();
        if (tmp_map != null && !tmp_map.isEmpty()) {
            file2hash.putAll(tmp_map);
        }
        GetChecksum.clear();
        printout(0, null, "..100%");
        return file2hash;
    }
//    /*
//     MethodID=80.1
//     * 
//     */
//
//    private HashMap<String, String> getHashes(ArrayList<Path> full_file_Path_l, long maxsize, long skip_smaller, boolean paths) {
//         printout(0,null, "Calculating checksum started @" + Timing.getDateTime());
//        int last_progress = 0;
//        HashMap<String, String> file2hash = new HashMap<>();
//
//        long lastadded = 0;
//        boolean printed = false;
//        System.out.print("..");
//        for (int i = 0; i < full_file_Path_l.size(); i++) {
//            int progrss = ((i * 100) / full_file_Path_l.size());
//            if (progrss > last_progress) {
//                if (progrss % 10 == 0) {
//                    System.out.print(progrss + "%");
//                }
//                if (progrss % 2 == 0) {
//                    System.out.print(".");
//                }
//                last_progress = progrss;
//            }
//            if (GetChecksum.getChecksumMap_size() < 100) {
//                (new Thread(new GetChecksum(full_file_Path_l.get(i).toAbsolutePath().toString(), maxsize, skip_smaller))).start();
//                printed = false;
//                lastadded = (new GregorianCalendar()).getTimeInMillis();
//            } else {
//                i--;
//                long current = (new GregorianCalendar()).getTimeInMillis();
//                if (!printed && ((current - lastadded) > 30000)) {
//                     printout(0,null, "Heavy traffic ! waiting until large files are processed. Current Progress=" + progrss + "%");
//                    printed = true;
//                    lastadded = (new GregorianCalendar()).getTimeInMillis();
//                }
//            }
//            try {
//                file2hash.putAll(GetChecksum.getcompletedChecksumMap());
//            } catch (Exception ex) {
//                 printout(0,null, "Error 80a : " + ex.getMessage());
//                try {
//                    Thread.sleep(300000);
//                } catch (InterruptedException ex1) {
//                     printout(0,null, "Error 80b: " + ex1.getMessage());
//                }
//
//            }
//
//        }
//        HashMap<String, String> tmp_map = GetChecksum.getChecksumMap();
//        if (tmp_map != null && !tmp_map.isEmpty()) {
//            file2hash.putAll(tmp_map);
//        }
//        GetChecksum.clear();
//         printout(0,null, "..100%");
//        return file2hash;
//    }
    /*
     MethodID=81
     * 
     */

    private HashMap<String, HashMap<String, String>> getFileDetails(ArrayList<String> full_file_names_l, String batch, String report) {
        int last_progress = 0;
        printout(0, null, "Collecting file details, permission, date created etc..");
        HashMap<String, HashMap<String, String>> files2cdetails_map = new HashMap<>();
        long freemem = Runtime.getRuntime().freeMemory();

        if (MEM_WARN > freemem) {
            printout(0, null, "Free Memory getting low !");
            if (MEM_terminate > freemem) {
                printout(0, null, "Error: not enough memory to continue, Exiting");
                System.exit(81);
            }
        }
        if (sdf_2 == null) {
//            String DATE_FORMAT_1 = "yyyy-MM-dd HH:mm:ss";
            sdf_2 = new java.text.SimpleDateFormat(DATE_FORMAT_1);
            sdf_2.setTimeZone(TimeZone.getDefault());
        }
//        GetFileDetails.name_flag = FILES_NAME;
        for (int i = 0; i < full_file_names_l.size(); i++) {
            int progrss = ((i * 100) / full_file_names_l.size());
            if (progrss > last_progress) {
                if (progrss % 10 == 0) {
                    System.out.print(progrss + "%");
                }
                if (progrss % 2 == 0) {
                    System.out.print(".");
                }
                last_progress = progrss;
            }
            if (GetFileDetails.getDetailsMap_size() < 100) {
                (new Thread(new GetFileDetails(full_file_names_l.get(i), getServerName(), sdf_2, batch, report))).start();
            } else {
                i--;
            }
            files2cdetails_map.putAll(GetFileDetails.getcompletedDetailsap());
        }
        HashMap<String, HashMap<String, String>> tmp_map = GetFileDetails.getDetailsMap();
        if (tmp_map != null && !tmp_map.isEmpty()) {
            files2cdetails_map.putAll(tmp_map);
        }
        GetFileDetails.clear();
        printout(0, null, "..100%");
        return files2cdetails_map;
    }

    private String getServerName() {

        if (server_name == null) {
            try {
                Enumeration net_interfaces = NetworkInterface.getNetworkInterfaces();
                while (net_interfaces.hasMoreElements() && server_name == null) {
                    NetworkInterface n = (NetworkInterface) net_interfaces.nextElement();
                    if (n.isVirtual() || n.isLoopback()) {
                    } else {
                        Enumeration ee = n.getInetAddresses();
                        while (ee.hasMoreElements() && server_name == null) {
                            InetAddress inetadd = (InetAddress) ee.nextElement();
                            if (inetadd instanceof Inet4Address) {
                                server_name = inetadd.getCanonicalHostName();
                            }
                        }
                    }
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return server_name;
    }

    private String getIP() {
        String ip = "0.0.0.0";
        try {
            Enumeration net_interfaces = NetworkInterface.getNetworkInterfaces();
            while (net_interfaces.hasMoreElements() && server_name == null) {
                NetworkInterface n = (NetworkInterface) net_interfaces.nextElement();
                if (n.isVirtual() || n.isLoopback()) {
                } else {
                    Enumeration ee = n.getInetAddresses();
                    while (ee.hasMoreElements() && server_name == null) {
                        InetAddress inetadd = (InetAddress) ee.nextElement();
                        if (inetadd instanceof Inet4Address) {
                            ip = inetadd.getHostAddress();
                        }
                    }
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return ip;
    }
//        private String getServerName() {
//        if (server_name == null) {
//            try {
//                Enumeration net_interfaces = NetworkInterface.getNetworkInterfaces();
//                while (net_interfaces.hasMoreElements() && server_name == null) {
//                    NetworkInterface n = (NetworkInterface) net_interfaces.nextElement();
//                    if (n.isVirtual() || n.isLoopback()) {
//                    } else {
//                        Enumeration ee = n.getInetAddresses();
//                        while (ee.hasMoreElements() && server_name == null) {
//                            InetAddress inetadd = (InetAddress) ee.nextElement();
//                            if (inetadd instanceof Inet4Address) {
//                                server_name = inetadd.getCanonicalHostName();
//                            }
//                        }
//                    }
//                }
//            } catch (Exception ex) {
//                ex.printStackTrace();
//            }
//        }
//        return server_name;
//    }
    /*
     MethodID=4
     */

    private void prepare_to_add_report(String directoryfor_prep, boolean recreate, boolean update, String report_nm, String batch_nm, File batch_dir, String prepfilenm_reports, ArrayList<String> additional_reports_l, ArrayList<String> new_updatable_l, boolean skipdbcheck) {
        File[] file_reports_a = new File[0];// batch_dir.listFiles();
        HashMap<String, String> new_report_overide_map = new HashMap<>();
        new_report_overide_map.put(REPORT_BATCH_NAME, batch_nm);
        new_report_overide_map.put(REPORT_NAME, report_nm);
        new_report_overide_map.put(PERSON_EMAIL, user_email);
//        new_report_overide_map.put(REPORT_TYPE_NAME, reporttype);
        new_report_overide_map.put(REPORT_DESCRIPTION, "Describe report here");
        new_report_overide_map.put(DONORDETAILS_NAME, "NA");
        new_report_overide_map.put(SAMPLEDETAILS_NAME, "NA");

        String timestp = sdf_2.format(new Date((System.currentTimeMillis())));
        new_report_overide_map.put(REPORT_ENTRYDATE, timestp);
        //        String tocheckwithdb_report = null;
        ArrayList<File> selected_reports_l = new ArrayList<File>(file_reports_a.length);
        ArrayList<String> check_with_db_l = new ArrayList<String>(file_reports_a.length);
        for (int i = 0; i < file_reports_a.length; i++) {
            if (file_reports_a[i].canRead() && file_reports_a[i].isDirectory() && !file_reports_a[i].getName().startsWith(".") && !file_reports_a[i].getName().startsWith("~") && !file_reports_a[i].getName().equals(".") && !file_reports_a[i].getName().equals("..")) {
                selected_reports_l.add(file_reports_a[i]);
                if (additional_reports_l != null) {
                    additional_reports_l.remove(file_reports_a[i].getName());
                }
                check_with_db_l.add(file_reports_a[i].getName());
            }
        }
        if (additional_reports_l != null) {
            check_with_db_l.removeAll(additional_reports_l);
            check_with_db_l.addAll(additional_reports_l);
        }
        file_reports_a = selected_reports_l.toArray(new File[selected_reports_l.size()]);
    }

    /*
     MethodID=5
     */
    private void prepare_to_add_report_batch(String directoryfor_prep, boolean recreate, boolean update, File batch_dir, String batch_nm, String prepfilenm_batch, ArrayList<String> additional_batch_l, ArrayList<String> new_updatable_l, boolean skipdbcheck) {

        HashMap<String, String> new_batch_overide_map = new HashMap<String, String>();
        new_batch_overide_map.put(REPORT_BATCH_DESCRIPTION, "NA");
        new_batch_overide_map.put(REPORT_BATCH_NAME, batch_nm);
//        String tocheckwithdb_batch = null;

        File[] file_a_for_batch = new File[]{batch_dir};
        ArrayList<String> check_with_db_batch_l = new ArrayList<String>(file_a_for_batch.length);
        for (int i = 0; i < file_a_for_batch.length; i++) {
            if (file_a_for_batch[i].canRead() && file_a_for_batch[i].isDirectory() && !file_a_for_batch[i].getName().startsWith(".") && !file_a_for_batch[i].getName().startsWith("~") && !file_a_for_batch[i].getName().equals(".") && !file_a_for_batch[i].getName().equals("..")) {
                if (file_a_for_batch[i].getName() != null) {
                    if (additional_batch_l != null) {
                        additional_batch_l.remove(file_a_for_batch[i].getName());
                    }
                    check_with_db_batch_l.add(file_a_for_batch[i].getName());
                }
            }
        }
        if (additional_batch_l != null) {
            for (int i = 0; i < additional_batch_l.size(); i++) {
                if (additional_batch_l.get(i) != null && !additional_batch_l.get(i).equalsIgnoreCase("null")) {
                    check_with_db_batch_l.add(additional_batch_l.get(i));

                }
            }
        }
    }

    /*
     MethodId=76
     */
    private String[] getUserResponseToLocation(String location, String parentlocation,
            String name, String type, String search_criteria_part_1,
            String search_criteria_part_2, String select_column,
            boolean checkifexists, boolean usedefaults, boolean canrenameFolders) {
        String[] result_a = new String[3];
        boolean aproved = false;
        boolean namecahnged = false;
        String new_location = location;
        String alias_to_use = name;
        String original_name = name;
        while (!aproved) {
            name = original_name;
            if (!usedefaults) {
                String name_to_use = getuserInputSameLine("Name selected for the " + type + " was: " + name, "Y-to accept this|N -cancel| or enter a new name");
                int ans = analyseUserResponse(name_to_use, true, true);
                if (ans == 1) {
                    name = name_to_use;
                    namecahnged = true;
                }
            }


//            List existing_details = getforQuery(query_existings.replace(NAME_TO_CHECK, name), false, "76");
            //boolean excatonly, boolean restrictforuser, boolean addcolumnnames, boolean exapandforeignKeys

            List existing_details = advancedQueryHandler(search_criteria_part_1 + "=" + name, select_column, true,
                    false, false, false, checkifexists, false, false, 0, true);

            if (!usedefaults) {
                if (existing_details != null && !existing_details.isEmpty()) {
                    result_a[2] = existing_details.toString().replace("[", "").replace("]", "").replaceAll("\\s", "");
                    if (result_a[2].matches("[0-9,]+")) {
                        String user_response = getuserInputSameLine("The name " + name + " is already used in the database.", "Y -To append to this reoprt|N -Cancel|To change the name type the new name.");
                        if (analyseUserResponse(user_response, true, true) == 1) {
                            name = user_response;
                            namecahnged = true;
                        }
                    } else {
                        result_a[2] = null;
                    }

                } else {
                    printout(0, null, "New " + type + "=" + name + " will be created");
                }
            }

            alias_to_use = name;

            if (namecahnged && canrenameFolders) {
                String ans_ren = getuserInputSameLine("You have selected a new name. Do you want to change the folder name " + location + " to  " + parentlocation + name + File.separatorChar + " as well ?", "[Y- yes rename it | N-Do not re-name]");
                if (analyseUserResponse(ans_ren, false, false) == 0) {
                    new_location = renameFolder(location, parentlocation + name + File.separatorChar);
                    if (new_location != null) {
                        aproved = true;
                    } else {
                        printout(0, null, "Error 17: Renaming directory failed.");
                        System.exit(17);
                    }
                } else {
                    aproved = true;
                }
            } else {
                aproved = true;
            }
        }
        result_a[0] = new_location;
        result_a[1] = alias_to_use;
        return result_a;
    }

    private void createContent_FromSearch(String prepfilenm, String directory, String table, List details_from_DB_file_l,
            HashMap<String, ArrayList<String>> new_values_overide_map) {
        HashMap<String, ArrayList<String>> found_in_db_map = new HashMap<>();
        List from_db_l = getParametersForPrepare(table);
        if (from_db_l != null) {
            ArrayList<String> column_constraints_l = new ArrayList<>(from_db_l);
            ArrayList<String> compulsory_not_null_l = new ArrayList<>(1);
            ArrayList<String> compulsory_preselected_l = new ArrayList<>(1);
            ArrayList<String> compulsory_existing_l = new ArrayList<>(1);
            for (int i = 0; i < column_constraints_l.size(); i++) {
                String c_column = column_constraints_l.get(i);
                String field_nam = c_column;
                String value = "NA";
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
            if (details_from_DB_file_l != null) {
                for (int i = 0; i < details_from_DB_file_l.size(); i++) {
                    if (details_from_DB_file_l.get(i) != null) {
                        String line = details_from_DB_file_l.get(i).toString();
                        String[] line_split = line.split("\\|\\|");
                        for (int j = 0; j < line_split.length; j++) {
                            if (line_split[j].contains("=")) {
                                String name = line_split[j].split("=")[0].trim();
                                if (name.split("\\.").length == 3) {
                                    name = name.replace(name.split("\\.")[0] + ".", "");
                                }
                                name = name.toUpperCase();
                                //                                name = name.replace("_DOT_", ".");
                                String value = line_split[j].split("=")[1].trim();
                                if (found_in_db_map.containsKey(name)) {
                                    found_in_db_map.get(name).add(value);
                                } else {
                                    ArrayList<String> tmp_l = new ArrayList<String>(5);
                                    tmp_l.add(value);
                                    found_in_db_map.put(name, tmp_l);
                                }
                            }
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
            String updated_file_nm = writeToDescriptFile2(prepfilenm, directory, commnetstext, found_in_db_map, compulsory_not_null_l,
                    compulsory_preselected_l, compulsory_existing_l, false, false);
            printout(0, null, "Processing complete. Use  \"-update " + updated_file_nm + "\"  to commit the chages to the database.\nOr Use  \"-delete " + updated_file_nm + "\"  to delete these from the database \n");
        } else {
            printout(0, null, "Error 4657: Communication error.Server was not available. Please try again later.");
            System.exit(4657);
        }
    }

    private HashMap<String, ArrayList<String>> createContent_file_recurse(
            ArrayList<Path> file_l, ArrayList<String> check_against_db,
            HashMap<String, HashMap<String, String>> files2cdetails_map,
            HashMap<String, String> files2checksum_map,
            String prepfilenm, String directory, String comments,
            boolean skipdbcheck, boolean addmark, int last_progress, String report_batch_root) {
        ArrayList<String> compulsory_not_null_l = new ArrayList<>(1);
        ArrayList<String> compulsory_preselected_l = new ArrayList<>(1);
        ArrayList<String> compulsory_existing_l = new ArrayList<>(1);
        List col_infofrom_db_l = getParametersForPrepare("FILES");
        ArrayList<String> column_constraints_l = new ArrayList<>(col_infofrom_db_l);
        col_infofrom_db_l = getParametersForPrepare("FILES2PATH");
        column_constraints_l.addAll(col_infofrom_db_l);
        col_infofrom_db_l = getParametersForPrepare("PERSON");
        column_constraints_l.addAll(col_infofrom_db_l);
        col_infofrom_db_l = getParametersForPrepare("REPORT");
        column_constraints_l.addAll(col_infofrom_db_l);
        col_infofrom_db_l = getParametersForPrepare("REPORT_BATCH");
        column_constraints_l.addAll(col_infofrom_db_l);
        for (int i = 0; i < column_constraints_l.size(); i++) {
            String field_nam = column_constraints_l.get(i);
            String[] column_split_a = field_nam.split("==");
            for (int j = 0; j < column_split_a.length; j++) {
                String c_col = column_split_a[j];
                String name_touse = field_nam;
                String[] tmp_a = field_nam.replaceAll("\\*", "").split("\\.");
                if (tmp_a.length > 1) {
                    name_touse = tmp_a[tmp_a.length - 2] + "." + tmp_a[tmp_a.length - 1];
                }
                if (c_col.startsWith("COLUMN=")) {
                    field_nam = c_col.replaceFirst("COLUMN=", "");
                    if (field_nam.endsWith("***")) {
                        compulsory_existing_l.add(name_touse);
                    } else if (field_nam.endsWith("**")) {
                        compulsory_preselected_l.add(name_touse);
                    } else if (field_nam.endsWith("*")) {
                        compulsory_not_null_l.add(name_touse);
                    }
                }
            }
        }

        HashMap<String, ArrayList<String>> found_in_db_reports_and_batches_map = new HashMap<>();
        found_in_db_reports_and_batches_map.put(REPORT_NAME, new ArrayList<String>(1));
        found_in_db_reports_and_batches_map.put(REPORT_BATCH_NAME, new ArrayList<String>(1));
        List from_db_l = getParametersForPrepare(FILES_TABLE_FLAG);
        String timestp = sdf_2.format(new Date((System.currentTimeMillis())));

        if (from_db_l != null) {
            HashMap<String, String> found_in_db_checksum2id_map = new HashMap();
            HashMap<String, String> found_in_db_file_id2report_map = new HashMap();
            if (check_against_db != null && !check_against_db.isEmpty()) {
                long total = check_against_db.size();
                if (!skipdbcheck) {
                    while (!check_against_db.isEmpty()) {
                        int limit = split_limit;
                        if (limit >= check_against_db.size()) {
                            limit = check_against_db.size();
                        }
                        ArrayList<String> tmp_l = new ArrayList<>(limit);
                        for (int k = 0; (k < limit && !check_against_db.isEmpty()); k++) {
                            tmp_l.add(check_against_db.remove(0));
                        }
                        ArrayList<String> found_id_l = new ArrayList<>();
                        printout(0, null, "Checking for duplicates with database. " + tmp_l.size() + " entries." + total + " more left");
                        total = total - tmp_l.size();
                        List res_l = advancedQueryHandler("FILES.CHECKSUM=" + tmp_l.toString().replace("[", "").replace("]", "").replaceAll("\\s", "").replaceAll(",", "||"),
                                "files.id,files.name,files.checksum", true, false, true, false, false, false, false, 0, true);
                        if (res_l != null) {
                            for (int i = 0; i < res_l.size(); i++) {
                                if (res_l.get(i) != null) {
                                    String line = res_l.get(i).toString();
                                    String[] line_split = line.split("\\|\\|");
                                    String checsum = null;
                                    String id = null;
                                    for (int j = 0; j < line_split.length; j++) {
                                        String[] line_split_splt = line_split[j].split("=");
                                        if (line_split_splt.length > 1) {
                                            String name = line_split_splt[0].trim();
                                            String value = line_split_splt[1].trim();
                                            if (name.equalsIgnoreCase(FILES_CHECKSUM)) {
                                                checsum = value.trim();
                                            } else if (name.equalsIgnoreCase(FILES_ID)) {
                                                id = value.trim();
                                                found_id_l.add(id);
                                            }
                                        }
                                    }
                                    if (checsum != null && id != null) {
                                        found_in_db_checksum2id_map.put(checsum, id);
                                    }
                                }
                            }
                            res_l.clear();
                            if (!found_id_l.isEmpty()) {
                                List res_report_l = advancedQueryHandler("FILES2REPORT.FILES_ID=" + found_id_l.toString().replace("[", "").replace("]", "").replaceAll("\\s", "").replaceAll(",", "||"), "FILES2REPORT",
                                        true, false, true, true, false, false, false, 0, true);
                                if (res_report_l != null) {
                                    for (int i = 0; i < res_report_l.size(); i++) {
                                        if (res_report_l.get(i) != null) {
                                            String line = res_report_l.get(i).toString();
                                            String[] line_split = line.split("\\|\\|");
                                            String db_report_nm = "NA";
                                            String file_checksum = null;
                                            for (int j = 0; j < line_split.length; j++) {
                                                String[] line_split_splt = line_split[j].split("=");
                                                if (line_split_splt.length > 1) {
                                                    String name = line_split_splt[0].trim();
                                                    String value = line_split_splt[1].trim();
                                                    if (name.equalsIgnoreCase(REPORT_NAME)) {
                                                        db_report_nm = value.trim();
                                                    } else if (name.equalsIgnoreCase(FILES_CHECKSUM)) {
                                                        file_checksum = value.trim();
                                                    }
                                                }
                                            }

                                            if (file_checksum != null) {
                                                found_in_db_file_id2report_map.put(file_checksum, db_report_nm);
                                            }
                                        }

                                    }
                                }
                            }
                        }

                    }
                }
            }
            HashMap<String, ArrayList<String>> found_in_FAT_map = new HashMap();
            ArrayList<String> c_files2cdetails_keys = null;
            for (int i = 0; i < file_l.size(); i++) {
                String report_nm = "NA";
                String batch_nm = "NA";
                int progrss = ((i * 100) / file_l.size());
                if (progrss > last_progress) {
                    if (progrss % 10 == 0) {
                        System.out.print(progrss + "%");
                    }
                    if (progrss % 2 == 0) {
                        System.out.print(".");
                    }
                    last_progress = progrss;
                }
                Path file = file_l.get(i);
                String c_file_path = null;
                if (Files.isRegularFile(file)) {
                    try {
//                        String report_batch_root//                             
                        c_file_path = file.toAbsolutePath().toString();
                        if (report_batch_root == null) {
                            report_nm = file.toAbsolutePath().getParent().getFileName().toString();
                            batch_nm = file.toAbsolutePath().getParent().getParent().getFileName().toString();
                        } else {
                            batch_nm = report_batch_root;
//                            Path reped=Paths.get(c_file_path.replace(timestp, report_nm));
//                            file.subpath(i, progrss);
//                            file.getName(i);
                        }

                    } catch (Exception ex) {
                    }
                }


                if (c_file_path != null) {
                    HashMap<String, String> c_files2cdetails_map = files2cdetails_map.get(c_file_path);
                    if (c_files2cdetails_map != null) {
                        if (files2checksum_map.containsKey(c_file_path)) {
                            String c_fat_checksum = files2checksum_map.get(c_file_path);
                            if (!c_fat_checksum.equalsIgnoreCase("NA")) {
                                c_files2cdetails_map.put(FILES_CHECKSUM, c_fat_checksum);
                                if (!c_files2cdetails_map.containsKey(REPORT_NAME)) {
                                    c_files2cdetails_map.put(REPORT_NAME, report_nm);
                                }
                                if (!c_files2cdetails_map.containsKey(REPORT_BATCH_NAME)) {
                                    c_files2cdetails_map.put(REPORT_BATCH_NAME, batch_nm);
                                }
//                                c_files2cdetails_map.put(SAMPLEDETAILS_NAME, "Sample deatils not available");
                                c_files2cdetails_map.put(PERSON_EMAIL, user_email);
                                c_files2cdetails_map.put(REPORT_ENTRYDATE, timestp);
                                String c_db_id = "-1";

                                if (!c_fat_checksum.equalsIgnoreCase("NA") && found_in_db_checksum2id_map.containsKey(c_fat_checksum)) {
                                    c_db_id = found_in_db_checksum2id_map.get(c_fat_checksum);
                                }
                                c_files2cdetails_map.put(FILES_ID, c_db_id);

                                if (found_in_db_file_id2report_map.containsKey(c_fat_checksum)) {
                                    c_files2cdetails_map.put(REPORT_NAME, found_in_db_file_id2report_map.get(c_fat_checksum));
                                }
                                if (i == 0) {
                                    c_files2cdetails_keys = new ArrayList<>(c_files2cdetails_map.keySet());
                                    for (int j = 0; j < c_files2cdetails_keys.size(); j++) {
                                        String c_key = c_files2cdetails_keys.get(j);
                                        if (i == 0) {
                                            found_in_FAT_map.put(c_key, new ArrayList<String>(10));
                                        }
                                    }
                                }
                                if (c_files2cdetails_keys != null) {
                                    for (int j = 0; j < c_files2cdetails_keys.size(); j++) {
                                        String c_key = c_files2cdetails_keys.get(j);
                                        if (found_in_FAT_map.containsKey(c_key)) {
                                            found_in_FAT_map.get(c_key).add(c_files2cdetails_map.get(c_key));
                                        }

                                    }
                                }
                            } else {
                                printout(0, null, "Error: Skipping as checksum is missing " + c_file_path);
                            }
                        } else {
                            printout(0, null, "Error: Skipping as checksum is missing " + c_file_path);
                        }

                    }
                }
            }
            writeToDescriptFile2(prepfilenm, directory, comments, found_in_FAT_map, compulsory_not_null_l,
                    compulsory_preselected_l, compulsory_existing_l, addmark, false);
        } else {
            printout(0, null, "Error 4657: Communication error.Server was not available. Please try again later.");
            System.exit(4657);
        }
        return found_in_db_reports_and_batches_map;
    }

    private HashMap<String, ArrayList<String>> createContent_file(
            File[] file_a, String person_emial, String report_nm, String batch_nm,
            ArrayList<String> check_against_db,
            HashMap<String, HashMap<String, String>> files2cdetails_map,
            HashMap<String, String> files2checksum_map,
            String prepfilenm, String directory, String comments,
            boolean skipdbcheck, boolean addmark, int last_progress, boolean recursive) {
        if (person_emial == null) {
            person_emial = user_email;
        }
        ArrayList<String> compulsory_not_null_l = new ArrayList<>(1);
        ArrayList<String> compulsory_preselected_l = new ArrayList<>(1);
        ArrayList<String> compulsory_existing_l = new ArrayList<>(1);
        List col_infofrom_db_l = getParametersForPrepare("FILES");
        ArrayList<String> column_constraints_l = new ArrayList<>(col_infofrom_db_l);
        col_infofrom_db_l = getParametersForPrepare("FILES2PATH");
        column_constraints_l.addAll(col_infofrom_db_l);
        col_infofrom_db_l = getParametersForPrepare("PERSON");
        column_constraints_l.addAll(col_infofrom_db_l);
        col_infofrom_db_l = getParametersForPrepare("REPORT");
        column_constraints_l.addAll(col_infofrom_db_l);
        col_infofrom_db_l = getParametersForPrepare("REPORT_BATCH");
        column_constraints_l.addAll(col_infofrom_db_l);
        for (int i = 0; i < column_constraints_l.size(); i++) {
            String field_nam = column_constraints_l.get(i);
            String[] column_split_a = field_nam.split("==");
            for (int j = 0; j < column_split_a.length; j++) {
                String c_col = column_split_a[j];
                String name_touse = field_nam;
                String[] tmp_a = field_nam.replaceAll("\\*", "").split("\\.");
                if (tmp_a.length > 1) {
                    name_touse = tmp_a[tmp_a.length - 2] + "." + tmp_a[tmp_a.length - 1];
                }
                if (c_col.startsWith("COLUMN=")) {
                    field_nam = c_col.replaceFirst("COLUMN=", "");
                    if (field_nam.endsWith("***")) {
                        compulsory_existing_l.add(name_touse);
                    } else if (field_nam.endsWith("**")) {
                        compulsory_preselected_l.add(name_touse);
                    } else if (field_nam.endsWith("*")) {
                        compulsory_not_null_l.add(name_touse);
                    }
                }
            }
        }

        HashMap<String, ArrayList<String>> found_in_db_reports_and_batches_map = new HashMap<>();
        found_in_db_reports_and_batches_map.put(REPORT_NAME, new ArrayList<String>(1));
        found_in_db_reports_and_batches_map.put(REPORT_BATCH_NAME, new ArrayList<String>(1));
        List from_db_l = getParametersForPrepare(FILES_TABLE_FLAG);
        String timestp = sdf_2.format(new Date((System.currentTimeMillis())));

        if (from_db_l != null) {
            HashMap<String, String> found_in_db_checksum2id_map = new HashMap();
            HashMap<String, String> found_in_db_file_id2report_map = new HashMap();

            if (check_against_db != null && !check_against_db.isEmpty()) {
                long total = check_against_db.size();
                if (!skipdbcheck) {
                    while (!check_against_db.isEmpty()) {
                        int limit = split_limit;
                        if (limit >= check_against_db.size()) {
                            limit = check_against_db.size();
                        }
                        ArrayList<String> tmp_l = new ArrayList<>(limit);
                        for (int k = 0; (k < limit && !check_against_db.isEmpty()); k++) {
                            tmp_l.add(check_against_db.remove(0));
                        }
                        ArrayList<String> found_id_l = new ArrayList<>();
                        printout(0, null, "Checking for duplicates with database. " + tmp_l.size() + " entries." + total + " more left");
                        total = total - tmp_l.size();
                        List res_l = advancedQueryHandler("FILES.CHECKSUM=" + tmp_l.toString().replace("[", "").replace("]", "").replaceAll("\\s", "").replaceAll(",", "||"), "files.id,name,checksum",
                                true, false, true, false, false, false, false, 0, true);
                        if (res_l != null) {
                            for (int i = 0; i < res_l.size(); i++) {
                                if (res_l.get(i) != null) {
                                    String line = res_l.get(i).toString();
                                    String[] line_split = line.split("\\|\\|");
                                    String checsum = null;
                                    String id = null;
                                    for (int j = 0; j < line_split.length; j++) {
                                        String[] line_split_splt = line_split[j].split("=");
                                        if (line_split_splt.length > 1) {
                                            String name = line_split_splt[0].trim();
                                            String value = line_split_splt[1].trim();
                                            if (name.equalsIgnoreCase(FILES_CHECKSUM)) {
                                                checsum = value.trim();
                                            } else if (name.equalsIgnoreCase(FILES_ID)) {
                                                id = value.trim();
                                                found_id_l.add(id);
                                            }
                                        }
                                    }
                                    if (checsum != null && id != null) {
                                        found_in_db_checksum2id_map.put(checsum, id);
                                    }
                                }
                            }

                            res_l.clear();
                            if (!found_id_l.isEmpty()) {
                                List res_report_l = advancedQueryHandler("FILES2REPORT.FILES_ID=" + found_id_l.toString().replace("[", "").replace("]", "").replaceAll("\\s", "").replaceAll(",", "||"), "FILES2REPORT",
                                        true, false, true, true, false, false, false, 0, true);
                                if (res_report_l != null) {
                                    for (int i = 0; i < res_report_l.size(); i++) {
                                        if (res_report_l.get(i) != null) {
                                            String line = res_report_l.get(i).toString();
                                            String[] line_split = line.split("\\|\\|");
                                            String db_report_nm = "NA";
                                            String file_checksum = null;
                                            for (int j = 0; j < line_split.length; j++) {
                                                String[] line_split_splt = line_split[j].split("=");
                                                if (line_split_splt.length > 1) {
                                                    String name = line_split_splt[0].trim();
                                                    String value = line_split_splt[1].trim();
                                                    if (name.equalsIgnoreCase(REPORT_NAME)) {
                                                        db_report_nm = value.trim();
                                                    } else if (name.equalsIgnoreCase(FILES_CHECKSUM)) {
                                                        file_checksum = value.trim();
                                                    }
                                                }
                                            }

                                            if (file_checksum != null) {
                                                found_in_db_file_id2report_map.put(file_checksum, db_report_nm);
                                            }
                                        }

                                    }
                                }
                            }
                        }

                    }
                }
            }
            HashMap<String, ArrayList<String>> found_in_FAT_map = new HashMap();
            ArrayList<String> c_files2cdetails_keys = null;
            for (int i = 0; i < file_a.length; i++) {
                int progrss = ((i * 100) / file_a.length);
                if (progrss > last_progress) {
                    if (progrss % 10 == 0) {
                        System.out.print(progrss + "%");
                    }
                    if (progrss % 2 == 0) {
                        System.out.print(".");
                    }
                    last_progress = progrss;
                }
                File file = file_a[i];
                String c_file_path = null;

                try {
                    c_file_path = file.getCanonicalPath();

                } catch (IOException ex) {
                }

                if (c_file_path != null) {
                    HashMap<String, String> c_files2cdetails_map = files2cdetails_map.get(c_file_path);
                    if (c_files2cdetails_map != null) {
                        if (files2checksum_map.containsKey(c_file_path)) {
                            String c_fat_checksum = files2checksum_map.get(c_file_path);
                            if (!c_fat_checksum.equalsIgnoreCase("NA")) {
                                c_files2cdetails_map.put(FILES_CHECKSUM, c_fat_checksum);
                                c_files2cdetails_map.put(REPORT_NAME, report_nm);
                                c_files2cdetails_map.put(REPORT_BATCH_NAME, batch_nm);
//                                c_files2cdetails_map.put(SAMPLEDETAILS_NAME, "Sample deatils not available");
                                c_files2cdetails_map.put(PERSON_EMAIL, person_emial);
                                c_files2cdetails_map.put(REPORT_ENTRYDATE, timestp);
                                String c_db_id = "-1";

                                if (!c_fat_checksum.equalsIgnoreCase("NA") && found_in_db_checksum2id_map.containsKey(c_fat_checksum)) {
                                    c_db_id = found_in_db_checksum2id_map.get(c_fat_checksum);
                                }
                                c_files2cdetails_map.put(FILES_ID, c_db_id);

                                if (found_in_db_file_id2report_map.containsKey(c_fat_checksum)) {
                                    c_files2cdetails_map.put(REPORT_NAME, found_in_db_file_id2report_map.get(c_fat_checksum));
                                }
                                if (i == 0) {
                                    c_files2cdetails_keys = new ArrayList<>(c_files2cdetails_map.keySet());
                                    for (int j = 0; j < c_files2cdetails_keys.size(); j++) {
                                        String c_key = c_files2cdetails_keys.get(j);
                                        if (i == 0) {
                                            found_in_FAT_map.put(c_key, new ArrayList<String>(10));
                                        }
                                    }
                                }
                                if (c_files2cdetails_keys != null) {
                                    for (int j = 0; j < c_files2cdetails_keys.size(); j++) {
                                        String c_key = c_files2cdetails_keys.get(j);
                                        if (found_in_FAT_map.containsKey(c_key)) {
                                            found_in_FAT_map.get(c_key).add(c_files2cdetails_map.get(c_key));
                                        }

                                    }
                                }
                            } else {
                                printout(0, null, "Error: Skipping as checksum is missing " + c_file_path);
                            }
                        } else {
                            printout(0, null, "Error: Skipping as checksum is missing " + c_file_path);
                        }

                    }
                }
            }
            writeToDescriptFile2(prepfilenm, directory, comments, found_in_FAT_map, compulsory_not_null_l,
                    compulsory_preselected_l, compulsory_existing_l, addmark, recursive);
        } else {
            printout(0, null, "Error 4657: Communication error.Server was not available. Please try again later.");
            System.exit(4657);
        }
        return found_in_db_reports_and_batches_map;
    }

    /*
     MethodID=25
     */
    private HashMap<String, ArrayList<String>> remove_redundents(HashMap<String, ArrayList<String>> found_in_db_map, String name_column) {
        printout(0, null, "Removing duplicates started @" + Timing.getDateTime());
        if (found_in_db_map.containsKey(name_column)) {
            ArrayList<String> key_list = new ArrayList<String>(found_in_db_map.keySet());
            ArrayList<String> name_l = found_in_db_map.get(name_column);
            for (int i = 0; i < name_l.size(); i++) {
                String c_name = name_l.get(i);
                int last_index = name_l.lastIndexOf(c_name);
                while (last_index > i) {
                    for (int j = 0; j < key_list.size(); j++) {
                        found_in_db_map.get(key_list.get(j)).remove(last_index);
                    }
                    last_index = name_l.lastIndexOf(c_name);
                }
            }
        }
        return found_in_db_map;
    }

    /*
     * MethodID=8
     */
    private void prepareTemplate(String table, String dir_to_place_files, boolean force_templete, boolean forcecrateone,
            HashMap<String, String> overide_map, HashMap<String, ArrayList<String>> fromSearch_map) {
        if (table == null) {
            List tables_l = advancedQueryHandler("tablename2feature.showinsearch=1", "tablename2feature.distinct table_name",
                    true, false, false, false, false, false, false, 0, true);
            if (tables_l != null) {
//                if (!tables_l.isEmpty()) {
//                    tables_l.remove(0);
//                }
                table = getUserChoice(tables_l, "Select the table to create template.");
            }
        }

        if (table != null && table.toUpperCase().endsWith(HIERARCHY_FLAG)) {
            printout(0, null, "Switching to hierarchy mode");
            prepareTemplateRelationships(table.replace(HIERARCHY_FLAG, ""), null, dir_to_place_files, "", false);

        } else {
            if (force_template_tables_tables_l.contains(table)) {
                force_templete = true;
                forcecrateone = false;
            }

            String unique_name = null;
            if (overide_map != null && overide_map.containsKey(getRealKey(overide_map.keySet(), "name"))) {
                unique_name = overide_map.get("name");
            }
            HashMap<String, String> presetvalaue_map = new HashMap<>();
            boolean createone = false;
            if (forcecrateone) {
                createone = true;
            } else {
                String ans = "T";
                if (!force_templete) {
                    ans = getuserInputSameLine("Do you want to create one entry in the " + table + " table or create a template to enter many entries?", "Y-create one entry | T-create template file | C- cancel and quit");
                }
                if (force_templete || ans.equalsIgnoreCase("T")) {
                    printout(0, null, "Creating template started @" + Timing.getDateTime());
                    List from_db_l = getParametersForPrepare(table);
                    if (from_db_l == null || from_db_l.isEmpty()) {
                        printout(0, null, "Error 8A: Error table not found.");
                    } else {
                        ArrayList<String> column_l = new ArrayList<>(from_db_l);
                        String columns = "##";
                        String values = "";
                        for (int i = 0; i < column_l.size(); i++) {
                            String c_column = column_l.get(i);
                            boolean override = false;
                            String cleaned_c_column = c_column.replaceAll("\\*", "").replaceFirst("COLUMN=", "");
                            if (fromSearch_map != null && fromSearch_map.containsKey(getRealKey(fromSearch_map.keySet(), cleaned_c_column))) {
                                override = true;
                            }
                            String filed_nam = c_column;
                            String help = "Help not avialable";
                            String value = "NA";
                            if (overide_map != null && overide_map.containsKey(getRealKey(overide_map.keySet(), cleaned_c_column))) {
                                value = overide_map.get(getRealKey(overide_map.keySet(), cleaned_c_column).replaceAll("\\*", ""));
                            } else {
//                                 printout(0,null, "2472");
                            }
                            String[] column_split_a = c_column.split("==");
                            for (int j = 0; j < column_split_a.length; j++) {
                                String c_col = column_split_a[j];
                                if (c_col.startsWith("COLUMN=")) {
                                    filed_nam = c_col.replaceFirst("COLUMN=", "");
                                } else if (c_col.startsWith("HELP=")) {
                                    help = c_col.replaceFirst("HELP=", "");
                                } else if (c_col.startsWith("VALUE=")) {
                                    value = c_col.replaceFirst("VALUE=", "");
                                }
                            }
                            if (!override) {
                                if (columns.length() == 2) {
                                    columns = columns + filed_nam;
                                    values = value;
                                } else {
                                    columns = columns + "\t" + filed_nam;
                                    values = values + "\t" + value;
                                }
                            }
                        }
                        ArrayList<String> values_l = new ArrayList<>(10);
                        values_l.add(values);
                        int last_progress = 0;
                        if (fromSearch_map != null && !fromSearch_map.isEmpty()) {
                            ArrayList<String> fromsearch_key_l = new ArrayList<>(fromSearch_map.keySet());
                            for (int i = 0; i < fromsearch_key_l.size(); i++) {
                                int progrss = ((i * 100) / fromsearch_key_l.size());
                                if (progrss > last_progress) {
                                    if (progrss % 20 == 0) {
                                        System.out.print(".");
                                    }
                                    last_progress = progrss;
                                }
                                if (columns.length() == 2) {
                                    columns = columns + fromsearch_key_l.get(i) + "***";
                                } else {
                                    columns = columns + "\t" + fromsearch_key_l.get(i) + "***";
                                }
                                ArrayList<String> c_val_l = fromSearch_map.get(fromsearch_key_l.get(i));
                                ArrayList<String> new_values_l = new ArrayList<>();

                                for (int k = 0; k < values_l.size(); k++) {
                                    if (((k * 100) / values_l.size()) % 10 == 0) {
                                        System.out.print(".");
                                    }
                                    String expanded_val = values_l.get(k);
                                    int last_dot = 0;
                                    for (int j = 0; j < c_val_l.size(); j++) {
                                        int c_prog = ((j * 100) / c_val_l.size());
                                        if (c_prog > last_dot && c_prog % 5 == 0) {
                                            System.out.print(".");
                                            last_dot = c_prog;
                                        }

                                        String transformed_val = null;
                                        if (expanded_val.isEmpty()) {
                                            transformed_val = c_val_l.get(j);
                                        } else {
                                            transformed_val = expanded_val + "\t" + c_val_l.get(j);
                                        }
                                        new_values_l.remove(transformed_val);
                                        new_values_l.add(transformed_val);
                                    }
                                }
                                printout(0, null, "");
                                values_l.clear();
                                values_l.addAll(new_values_l);

                            }
                        }
                        StringBuilder str = new StringBuilder();
                        str.ensureCapacity(values_l.size());
                        int last_dot = 0;
                        if (!values_l.isEmpty()) {
                            str.append(values_l.get(0));
                        }
                        for (int i = 1; i < values_l.size(); i++) {
                            int c_prog = ((i * 100) / values_l.size());
                            if (c_prog > last_dot && c_prog % 5 == 0) {
                                System.out.print(".");
                                last_dot = c_prog;
                            }
                            str.append("\n" + values_l.get(i));
                        }
                        printout(0, null, ".");
//                        String content = values_l.get(0);
//                        for (int i = 1; i < values_l.size(); i++) {
//                            content = content + "\n" + values_l.get(i);
//
//                        }

                        String titletext = commnetstext + "\n" + columns + "\n";
                        String filenm = PREPARE_TO_ADD_CUSTOM_TABLE_FILENM.replace("<TABLE>", table);
                        if (dir_to_place_files != null) {
                            if (!dir_to_place_files.endsWith(File.separator)) {
                                dir_to_place_files = File.separatorChar + "" + dir_to_place_files;
                            }
                            filenm = dir_to_place_files + filenm;
                        }
                        File existing_f = new File(filenm);
                        boolean makeit = false;
                        if (existing_f.isFile()) {
                            ans = getuserInputSameLine("The file " + filenm + " already exists, do you want to ovewrite it?", " C- cancel and quit |Y-create file(the content of existing file will be lost)");
                            if (ans.equalsIgnoreCase("Y")) {
                                try {
                                    String parent_folder = existing_f.getParentFile().getCanonicalPath();
                                    String backup_name = existing_f.getName() + "~";
                                    File bakup_file = new File(parent_folder + File.separatorChar + backup_name);
                                    existing_f.renameTo(bakup_file);
                                } catch (IOException ex) {
                                } catch (Exception ex) {
                                }
                                makeit = true;
                            }
                        } else {
                            makeit = true;
                        }
                        if (makeit) {
                            int repeats = 1;
//                        String ans_type = getuserInputSameLine("How many rows do you want in the file (default 1)?:", "Selection").trim();
//                        if (ans_type.matches("[0-9]+")) {
//                            repeats = new Integer(ans_type);
//                        }                          
                            wirteToDescriptFile(filenm, titletext, str.toString(), true, new ArrayList<String>(1), repeats);
                        } else {
                            printout(0, null, "File creation interupted by user");
                        }

                    }
                } else if (ans.equalsIgnoreCase("C")) {
                    System.exit(8);
                } else if (ans.equalsIgnoreCase("Y")) {
                    createone = true;
                } else {
                    printout(0, null, "Error 8a: invalid option");
                }

            }
            if (createone) {
                HashMap<String, ArrayList<String>> vlaue_map = new HashMap<>();
                boolean ok = false;
                if (unique_name == null) {
                    while (!ok) {
                        String ans = getuserInputSameLine("Pleasae Enter a unique name (e.g. email when creating a personal profile) for  " + table + " (or press N-quit,H-help)", null);
                        ans = ans.trim();
                        if (ans.equalsIgnoreCase("N")) {
                            System.exit(1);
                        } else if (ans.equalsIgnoreCase("H")) {
                            printout(0, null, "This is a unique identifier given to the " + table);
                        } else if (ans.isEmpty() || !checkDescriptionValidity(ans)) {
                            printout(0, null, "Error 169: Invalid value. Please try again ");

                        } else {
                            List result = advancedQueryHandler(table + ".name=" + ans.replaceAll("'", ""), table + ".id",
                                    true, false, false, false, true, false, false, 0, true);
                            if (result == null || result.isEmpty() || result.get(0).equals("0")) {
                                presetvalaue_map.put(table + ".name", ans);
                                ok = true;
                            } else {
                                printout(0, null, "The name specified is in use. Please try again ");
                            }
//                            if (!isItfound("select 1 from " + table + " where name='" + ans.replaceAll("'", "") + "'")) {
//                                presetvalaue_map.put(table + ".name", ans);
//                                ok = true;
//                            } else {
//                                 printout(0,null, "The name specified is in use. Please try again ");
//
//                            }
                        }
                    }
                } else {
                    presetvalaue_map.put(table + ".name", unique_name);
                }

                List from_db_l = getParametersForPrepare(table);
                if (from_db_l == null || from_db_l.isEmpty()) {
                    printout(0, null, "Error 4658: Error table name incorrect or the database failed to respond");
                } else {
                    ArrayList<String> column_l = new ArrayList<>(from_db_l);
                    for (int i = 0; i < column_l.size(); i++) {
                        String c_column = column_l.get(i);
                        String fieled_name = c_column;
                        String help = "Help not avialable";
                        String value = "";
                        String[] column_split_a = c_column.split("==");
                        for (int j = 0; j < column_split_a.length; j++) {
                            String c_col = column_split_a[j];
                            if (c_col.startsWith("COLUMN=")) {
                                fieled_name = c_col.replaceFirst("COLUMN=", "");
                            } else if (c_col.startsWith("HELP=")) {
                                help = c_col.replaceFirst("HELP=", "");
                            } else if (c_col.startsWith("VALUE=")) {
                                value = c_col.replaceFirst("VALUE=", "");
                            }
                        }
                        boolean mustCrosscheck = false;
                        if (fieled_name.contains("***")) {
                            mustCrosscheck = true;
                        }
                        String user_value = presetvalaue_map.get(fieled_name.replaceAll("\\*", ""));
                        if (user_value == null) {
                            if (overide_map != null && overide_map.containsKey(getRealKey(overide_map.keySet(), fieled_name.replaceAll("\\*", "")))) {
                                user_value = overide_map.get(getRealKey(overide_map.keySet(), fieled_name.replaceAll("\\*", "")));
                            }
                        }
                        if (user_value != null) {
                            printout(0, null, "Value Selected " + fieled_name.replaceAll("\\*", "") + "=" + user_value);
                            ArrayList<String> tmp_list = new ArrayList<String>(1);
                            tmp_list.add(user_value);
                            vlaue_map.put(fieled_name.replaceAll("\\*", ""), tmp_list);
                        } else if (!value.isEmpty()) {
                            printout(0, null, "Value for (Selected automatically) " + fieled_name + "=" + value);
                            ArrayList<String> tmp_list = new ArrayList<String>(1);
                            tmp_list.add(value);
                            vlaue_map.put(fieled_name.replaceAll("\\*", ""), tmp_list);
                        } else {
                            String ans = getuserInputSameLine("Pleasae Enter a value for  " + fieled_name + " (or press N-quit,H-help, NA-to add this later)", null);
                            if (ans.trim().equalsIgnoreCase("N")) {
                                System.exit(1);
                            } else if (ans.trim().equalsIgnoreCase("H")) {
                                printout(0, null, help);
                                i--;
                            } else if (ans == null || ans.isEmpty() || (fieled_name.contains("*") && !checkDescriptionValidity(ans))) {
                                printout(0, null, "Error 172: Invalid value. Please try again ");
                                i--;
                            } else {
                                if (mustCrosscheck) {
                                    String table_name = fieled_name;
                                    String[] filed_split_a = fieled_name.split("\\,");
                                    if (filed_split_a.length == 2) {
                                        table_name = filed_split_a[0];
                                    } else if (filed_split_a.length == 3) {
                                        table_name = filed_split_a[1];
                                    }

                                    String filed_name = fieled_name.replaceAll("\\*", "");
                                    String sql = "select 1 from " + table_name + " where " + filed_name + "='" + ans + "'";
                                    List result = advancedQueryHandler(table_name + "." + filed_name + "=" + ans, table_name + ".id",
                                            true, false, false, false, true, false, false, 0, true);
                                    if (result == null || result.isEmpty() || result.get(0).equals("0")) {
                                        printout(0, null, "Failed to locate " + filed_name + " (exact match) in the database. Please, first create and entry in " + table_name + "");
                                        i--;
                                    } else {
                                        ArrayList<String> tmp_list = new ArrayList<>(1);
                                        tmp_list.add(ans);
                                        vlaue_map.put(fieled_name.replaceAll("\\*", ""), tmp_list);
                                    }

                                }

                            }
                        }
                    }

                    List result_l = setUsingTemplate(map2list(vlaue_map));
                    if (result_l == null || result_l.size() != 3) {
                        printout(0, null, "Error 198: Connection to the server failed");
                    } else {
                        String result = (String) result_l.get(0);
                        String undo = (String) result_l.get(1);
                        String messages = (String) result_l.get(2);
                        printout(0, null, result);
                        if (!messages.isEmpty()) {
                            printout(0, null, "messages=" + messages);
                        }
                        if (undo != null) {
                            createUndoPoint(undo, dir_to_place_files);
                        }
                    }
                }

            }
        }


    }

    /*
     MethodID=27
     */
    private String getRealKey(Set keys, String key) {
        if (keys != null && key != null) {
            ArrayList<String> key_l = new ArrayList<>(keys);
            key = key.toUpperCase();
            String realKey = null;
            for (int i = 0; (i < key_l.size() && realKey == null); i++) {
                if (key_l.get(i).toUpperCase().endsWith(key)) {
                    realKey = key_l.get(i);
                }
            }
            return realKey;
        } else {
            return null;
        }

    }

    /*
     MethodID=12
     */
    private ArrayList<String> readTemplate_new(String filenm, boolean verbose, boolean committodb, String dir_read_from_prep_files) {
        printout(0, null, "(12) Reading template = " + filenm);
        long start = Timing.setPointer();
//        ProgressMonitor p = new ProgressMonitor(PROGRESS_MONT_INTERVAL, "EGM12");
//        (new Thread(p)).start();
        Object[] ob_a = startProgress(2);
        File tmpl_file = new File(filenm);
        int nuofcolmns = 0;
        long rownum = 0;
        long tot_rows = 0;
        boolean allok = true;
        String relationship_type = null;
        HashMap<String, ArrayList<String>> vlaue_map = new HashMap<>();
        ArrayList<String> column_nms = new ArrayList<>(2);
        int c_count = 0;
        if (tmpl_file.isFile() && tmpl_file.canRead()) {
            try {
                Scanner scan = new Scanner(tmpl_file);
                tot_rows = 0;
                while (scan.hasNext()) {
                    scan.nextLine();
                    tot_rows++;
                    if (tot_rows % 10000 == 0) {
                        System.out.print(".");
                    }
                }
                scan.close();
                stopProgress(ob_a);
                printout(0, null, "");
                scan = new Scanner(tmpl_file);
                rownum = 0;
                long lastprog = 0;
                while (scan.hasNext() && allok) {
                    String line = scan.nextLine();
                    rownum++;
                    c_count++;
                    long progr = (rownum * 100) / tot_rows;
                    if (progr > lastprog) {
                        lastprog = progr;
                        if (progr % 2 == 0) {
                            System.out.print(".");
                            if (progr % 10 == 0) {
                                System.out.print(progr + "%");
                            }
                        }
                    }
                    if (line.startsWith("##")) {
                        line = line.replaceAll("#", "").replaceAll("\\*", "").trim();
                        String[] colnm_a = line.split("\t");
                        nuofcolmns = colnm_a.length;
                        for (int i = 0; i < colnm_a.length; i++) {
                            String[] colnm_a_split = colnm_a[i].split("\\.");
                            if (colnm_a_split.length > 1) {//Column names with Table.column format
                                vlaue_map.put(colnm_a[i], new ArrayList<String>(1));
                                column_nms.add(colnm_a[i]);
                            } else {
                                vlaue_map.put(colnm_a[i], null);
                                column_nms.add(colnm_a[i]);
                            }
                        }

                    } else if (line.startsWith("#")) {
                        if (line.contains(RELATIONSHIP_TYPE_FLAG)) {
                            allok = false;
                            line = line.replace(RELATIONSHIP_TYPE_FLAG, "").replace("#", "").trim();
                            if (!line.isEmpty()) {
                                relationship_type = line;
                            }
                        }
                    } else if (!line.isEmpty()) {
                        line = line.replaceAll("\\*", "").trim();
                        String[] values_a = line.split("\t");
                        if (nuofcolmns != values_a.length) {
                            allok = false;
                            printout(0, null, "Error 12a: The row " + rownum + " has different column number than the title. Expected= " + nuofcolmns + ". Found=" + values_a.length + "\n\t" + line);
                        } else {
                            for (int i = 0; i < values_a.length; i++) {
                                if (vlaue_map.get(column_nms.get(i)) == null) {
                                } else {
                                    vlaue_map.get(column_nms.get(i)).add(values_a[i]);
                                }
                            }
                        }
                    }

                    if (allok && committodb && (c_count > split_limit || !scan.hasNext())) {
                        printout(0, null, "Sending " + c_count + " records.  Total queued=" + rownum + " of about " + tot_rows + " (" + ((rownum * 100) / tot_rows) + "% complete). Time elapsed=" + Timing.convert(Timing.getFromlastPointer(start)));
                        c_count = 0;
                        for (int i = 0; i < column_nms.size(); i++) {
                            if (vlaue_map.containsKey(column_nms.get(i)) && vlaue_map.get(column_nms.get(i)) == null) {
                                vlaue_map.remove(column_nms.get(i));
                                printout(0, null, "Not processing " + column_nms.get(i));
                            }
                        }
                        ArrayList<String> content_l = map2list(vlaue_map);
                        if (content_l.isEmpty()) {
                            printout(0, null, "2. Nothing to update");
                        } else {
                            printout(0, null, "");
                            List result_l = setUsingTemplate(content_l);
                            if (result_l == null) {
                                printout(0, null, "Error 12d: Unknown error");
                                allok = false;
                            } else if (result_l.size() != 3) {
                                printout(0, null, "Error 12e: Connection to the server failed");
                                allok = false;
                            } else {
                                String result = (String) result_l.get(0);
                                String undo = (String) result_l.get(1);
                                String messages = (String) result_l.get(2);
                                printout(0, null, result);
                                if (!messages.isEmpty()) {
                                    printout(0, null, "messages=" + messages);
                                }
                                if (undo != null) {
                                    createUndoPoint(undo, dir_read_from_prep_files);
                                }
                                if (result_l.size() > 3) {
                                    String report_batches_used = result_l.get(3).toString();
                                    String reports_used = result_l.get(4).toString();
                                    String suc = result_l.get(5).toString();
                                    String suc_link = result_l.get(6).toString();
                                    printout(0, null, "3278 report_batches_used=" + report_batches_used + " reports_used=" + reports_used + " suc=" + suc + "  suc_link=" + suc_link);
                                }
                            }
                        }
                        for (int i = 0; i < column_nms.size(); i++) {
                            if (vlaue_map.containsKey(column_nms.get(i))) {
                                vlaue_map.get(column_nms.get(i)).clear();
                            }
                        }
                    }
                }
                scan.close();
            } catch (FileNotFoundException ex) {
                printout(0, null, "Error 12c: Failed to read template file " + filenm + ". If you want to force a different file use it together with " + FORCE_CONFIG_IN_PUT_LOCATION_FLAG + " instruction" + ": " + ex.getMessage());
//                p.cancel("12");
                stopProgress(ob_a);
            }
        } else {
            printout(0, null, "Error 12d: Failed to read template file " + filenm + ". If you want to force a different file use it together with " + FORCE_CONFIG_IN_PUT_LOCATION_FLAG + " instruction");
//            p.cancel("12");
            stopProgress(ob_a);
        }
        if (allok) {
//            p.cancel("EGM12");
            stopProgress(ob_a);
            return null;//content_l;
        } else {
            stopProgress(ob_a);
//            p.cancel("EGM12");
            if (relationship_type != null) {
                prepare_AddRelationshipFromFile(filenm, tot_rows);
            } else {
                printout(0, null, "Error 12h: Failed to process " + filenm);
            }
            return null;
        }
    }


    /*
     MethodID=35
     */
    private ArrayList<String> readTemplate(String filenm, boolean verbose, boolean committodb,
            String dir_read_from_prep_files, boolean overide_existance,
            long skip_larger, long skip_smaller, boolean toDelete, int from, int to, boolean skip_checksum) {
        printout(0, null, "(35) Reading template = " + filenm);
//        ProgressMonitor p = new ProgressMonitor(PROGRESS_MONT_INTERVAL, "EGM35");
//        (new Thread(p)).start();
        Object[] ob_a = startProgress(3);
        ArrayList<String> content_l = null;
        File tmpl_file = new File(filenm);
        int nuofcolmns = 0;
        boolean limited = false;
        long rownum = 0;
//        int maxlimit = 10000;
        boolean allok = true;
        ArrayList<String> c_name_l = new ArrayList<>(1);
        String relationship_type = null;
        HashMap<String, ArrayList<String>> vlaue_map = new HashMap<>();
        ArrayList<String> column_nms = new ArrayList<>(2);

        if (tmpl_file.isFile() && tmpl_file.canRead()) {
            try {
                Scanner scan = new Scanner(tmpl_file);
                long idColumns = 0;
                while (!limited && scan.hasNext() && allok) {
                    String line = scan.nextLine();
                    rownum++;
                    if (line.startsWith("##")) {
                        line = line.replaceAll("#", "").replaceAll("\\*", "").trim();
                        String[] colnm_a = line.split("\t");
                        nuofcolmns = colnm_a.length;
                        for (int i = 0; i < colnm_a.length; i++) {
                            if (colnm_a[i].contains(".")) {
                                vlaue_map.put(colnm_a[i], new ArrayList<String>(1));
                                column_nms.add(colnm_a[i]);
                                if (colnm_a[i].toUpperCase().endsWith(".NAME")) {
                                    c_name_l.add(colnm_a[i]);
                                } else if (colnm_a[i].toUpperCase().endsWith(".ID")) {
                                    idColumns++;
                                }
                            } else {
                                vlaue_map.put(colnm_a[i], null);
                                column_nms.add(colnm_a[i]);
                            }
                        }
                    } else if (line.startsWith("#")) {
                        if (line.contains(RELATIONSHIP_TYPE_FLAG)) {
                            allok = false;
                            line = line.replace(RELATIONSHIP_TYPE_FLAG, "").replace("#", "").trim();
                            if (!line.isEmpty()) {
                                relationship_type = line;
                            }
                        }
                    } else if (!line.isEmpty()) {
                        if (rownum >= from && rownum < to) {
//                            if (rownum == 1 || rownum == from) {
//                                 printout(0,null, "4095 From " + line);
//                            }
                            line = line.replaceAll("\\*", "").trim();
                            String[] values_a = line.split("\t");
                            if (nuofcolmns != values_a.length) {
                                allok = false;
                                printout(0, null, "Error 12a: The row " + rownum + " has different column number than the title. Expected= " + nuofcolmns + ". Found=" + values_a.length + "\n\t" + line);
                            } else {
                                for (int i = 0; i < values_a.length; i++) {
                                    if (vlaue_map.get(column_nms.get(i)) == null) {
//                                    allok = false;
//                                     printout(0,null, "Error 12b: Error in " + rownum + ".Failed to find value for " + values_a[i]);
                                    } else {
                                        vlaue_map.get(column_nms.get(i)).add(values_a[i]);
                                    }
                                }
                            }
                        } else if (rownum > to) {
                            limited = true;
                        }
//                    
                    }
                }
                scan.close();
                if (toDelete) {
                    if (idColumns == 0) {
                        printout(0, null, "Error 35a: ID column not found in " + filenm + ".\nUse -templte -exact -search TABLE.COLUMN=valuse to create delete template");
                        System.exit(35);
                    } else if (idColumns > 1) {
                        printout(0, null, "Error 35b: Wrong format " + filenm);
                        printout(0, null, "There are more than one ID column in the template. If you made this using search avoid using the -expand instruction.");
                        System.exit(35);
                    }
                }
            } catch (FileNotFoundException ex) {
//                p.cancel("12");
                stopProgress(ob_a);
                printout(0, null, "Error 35c: Failed to read template file " + filenm + ". If you want to force a different file use it together with " + FORCE_CONFIG_IN_PUT_LOCATION_FLAG + " instruction" + ": " + ex.getMessage());
            }
        } else {
            printout(0, null, "Error 35d: Failed to read template file " + filenm + ". If you want to force a different file use it together with " + FORCE_CONFIG_IN_PUT_LOCATION_FLAG + " instruction");
//            p.cancel("12");
            stopProgress(ob_a);
        }
//        if (maxlimit <= 0) {
//             printout(0,null, "Error 12f: The " + filenm + " contains more than the maximum number of raws. Please split this file");
//            return null;
//        } else {


        if (allok) {
            for (int i = 0; i < column_nms.size(); i++) {
                if (vlaue_map.containsKey(column_nms.get(i)) && vlaue_map.get(column_nms.get(i)) == null) {
                    vlaue_map.remove(column_nms.get(i));
                    printout(0, null, "Not processing " + column_nms.get(i));
                }
            }
            int format_mis = 0;
            if (!overide_existance) {
                String c_files_name = getRealKey(vlaue_map.keySet(), FILES_TABLE_NAME_FLAG);
                String c_files_checksum = getRealKey(vlaue_map.keySet(), FILES_TABLE_CHECKSUM_FLAG);
                if (!c_name_l.isEmpty() && c_name_l.contains(c_files_name) && vlaue_map.containsKey(c_files_name)) {
                    ArrayList<String> name_l = vlaue_map.get(c_files_name);
                    ArrayList<String> file_full_name_l = new ArrayList<>(name_l.size());
                    ArrayList<String> checksum_l = new ArrayList<>(name_l.size());
                    for (int j = 0; j < name_l.size(); j++) {
                        file_full_name_l.add(name_l.get(j));
                    }
                    HashMap<String, String> hases_map = getHashes(file_full_name_l, skip_larger, skip_smaller, skip_checksum);
                    for (int j = 0; j < name_l.size(); j++) {
                        String c_file_full_nm = name_l.get(j);
                        File tmp_test = new File(c_file_full_nm);
                        if (tmp_test.isFile() && tmp_test.canRead()) {
//                                String[] details_a = getFileDetails(name_l.get(j));
                            if ((!hases_map.containsKey(c_file_full_nm)) || (hases_map.get(c_file_full_nm) == null) || (hases_map.get(c_file_full_nm).equalsIgnoreCase("NA"))) {
                                format_mis++;
                                printout(0, null, "\nWarning 35e: Checksum creation failed for " + c_file_full_nm + " this file will not be processed. ");
                                vlaue_map = remove_value_at_pos(vlaue_map, j);
                            }
                        } else {
                            format_mis++;
                            printout(0, null, "\nWarning 35g: The file " + c_file_full_nm + " could not be found in this system or read access failed. This will not be processed.");
                            vlaue_map = remove_value_at_pos(vlaue_map, j);
                        }
                    }
                    vlaue_map.put(c_files_checksum, checksum_l);
                }
            }
            if (format_mis > 0) {
                printout(0, null, "\nWarning: There were 35f" + format_mis + " incorrect records (use " + OVERIDE_FLAG + " to ignore this). The correct format for filename \"protocol:server:full_path_to_file\". E.g. SCP:server.no:/home/files/genrated_296.bng");
            }
            content_l = map2list(vlaue_map);

            if (content_l.isEmpty()) {
                printout(0, null, "1. Nothing to update");
            } else {
                if (committodb) {
                    List result_l = setUsingTemplate(content_l);
                    if (result_l == null || result_l.size() != 3) {
                        printout(0, null, "Error 35g: Connection to the server failed");
                    } else {
                        String result = (String) result_l.get(0);
                        String undo = (String) result_l.get(1);
                        String messages = (String) result_l.get(2);
                        printout(0, null, result);
                        if (!messages.isEmpty()) {
                            printout(0, null, "messages=" + messages);
                        }
                        if (undo != null) {
                            createUndoPoint(undo, dir_read_from_prep_files);
                        }
                    }
                }
            }
//            p.cancel("EGM35");
            stopProgress(ob_a);

            return content_l;
        } else {
            if (relationship_type != null) {
                prepare_AddRelationshipFromFile(filenm, rownum);
            } else {
                printout(0, null, "Error 35h: Failed to process " + filenm);
            }
//            p.cancel("EGM35");
            stopProgress(ob_a);
            return null;
        }
//        }


    }

    private ArrayList<String> map2list(HashMap<String, ArrayList<String>> vlaue_map) {
        ArrayList<String> retuen_l = new ArrayList<>(vlaue_map.size());
        ArrayList<String> mapkeys_l = new ArrayList<>(vlaue_map.keySet());
        for (int i = 0; i < mapkeys_l.size(); i++) {
            ArrayList<String> c_val_l = vlaue_map.get(mapkeys_l.get(i));
            if (c_val_l != null && !c_val_l.isEmpty()) {
//                String val = c_val_l.toString().replace("]", "").replace("[", "").replaceAll(",", ";;");
                StringBuilder valuse = new StringBuilder();//c_val_l.get(0);
                valuse.append(c_val_l.get(0));
                for (int j = 1; j < c_val_l.size(); j++) {
                    valuse.append(";;" + c_val_l.get(j));
                }
                retuen_l.add(mapkeys_l.get(i) + "==" + valuse.toString());
            }
        }
        return retuen_l;
    }

    private ArrayList<String> map2list(HashMap<String, String> vlaue_map, boolean single) {
        ArrayList<String> retuen_l = new ArrayList<>(vlaue_map.size());
        ArrayList<String> mapkeys_l = new ArrayList<>(vlaue_map.keySet());
        for (int i = 0; i < mapkeys_l.size(); i++) {
            String c_val = vlaue_map.get(mapkeys_l.get(i));
            if (c_val != null && !c_val.isEmpty()) {
                StringBuilder valuse = new StringBuilder();//c_val_l.get(0);
                valuse.append(c_val);
                retuen_l.add(mapkeys_l.get(i) + "==" + valuse.toString());
            }
        }
        return retuen_l;
    }

    private HashMap<String, ArrayList<String>> remove_value_at_pos(HashMap<String, ArrayList<String>> vlaue_map, int pos) {
        ArrayList<String> key_l = new ArrayList<String>(vlaue_map.keySet());
        for (int i = 0; i < key_l.size(); i++) {
            vlaue_map.get(key_l.get(i)).remove(pos);
        }
        return vlaue_map;
    }

    private String getFileExtention4name(HashMap<String, String> filetypeName_map, String name) {
        String details = filetypeName_map.get(name);
        if (details != null) {
            String[] details_a = details.split(",");
            for (int i = 0; i < details_a.length; i++) {
                if (details_a[i].startsWith("extention=")) {
                    return details_a[i].replace("extention=", "").trim();
                }

            }
        }
        return null;
    }

    /*
     MethodID=33
     */
    private String renameFolder(String sourcename, String targetname) {
        String newDirectoynm = null;
        File target_dir = new File(targetname);
        File source_dir = new File(sourcename);
        if (source_dir.isDirectory()) {
            if (target_dir.isDirectory()) {
                String ans = getuserInputSameLine("There is already directory named " + targetname + " do you want to move the files from " + sourcename + " to " + targetname + " ?", "Y for yes|N for no and exit");
                if (analyseUserResponse(ans, true, false) == 0) {
                    File[] file_a = source_dir.listFiles();
                    for (int i = 0; i < file_a.length; i++) {
                        try {
                            File target_file = new File(target_dir.getCanonicalPath() + File.separatorChar + file_a[i].getName());
                            if (!target_file.isFile()) {
                                file_a[i].renameTo(target_file);
                            } else {
                                printout(0, null, "Not moving " + file_a[i].getName() + " as there is already a file with the same name on the target");
                            }
                        } catch (IOException ex) {
                        }
                    }
                    try {
                        newDirectoynm = target_dir.getCanonicalPath();
                    } catch (IOException ex) {
                    }
                } else {
                    System.exit(33);
                }
            } else {
                boolean result = source_dir.renameTo(target_dir);
                if (result) {
                    File newDirectory = new File(targetname);
                    if (newDirectory.isDirectory()) {
                        try {
                            newDirectoynm = newDirectory.getCanonicalPath() + File.separatorChar;
                        } catch (IOException ex) {
                        }
                    }
                }
            }
        } else {
            printout(0, null, "Error 18: Error reading from source directory " + sourcename);
        }
        return newDirectoynm;
    }

    /*
     * Gets the content of the description file. This is used to decide the new
     * entries and append them
     */
    private HashMap<String, ArrayList<String>> getcontent(String filename, String name_clm_nm, ArrayList<String> existing_name_l) {
        File file = new File(filename);
        if (existing_name_l == null) {
            existing_name_l = new ArrayList<String>(1);
        }
        HashMap<String, ArrayList<String>> content_map = new HashMap<String, ArrayList<String>>();
        ArrayList<String> heading_l = new ArrayList<String>(5);
        if (file.isFile()) {
            try {
                Scanner scan = new Scanner(file, "ISO-8859-4");
                boolean formatcorrect = true;
                int linenum = 0;
                int name_pos = -1;
                while (scan.hasNext() && formatcorrect) {
                    String line = scan.nextLine().trim();
                    linenum++;
                    if (line.startsWith("##") && !line.isEmpty()) {
                        line = line.replaceAll("#", "").replaceAll("\\*", "");
                        String[] headings = line.split("\t");
                        for (int i = 0; i < headings.length; i++) {
                            content_map.put(headings[i].trim(), new ArrayList<String>(5));
                            heading_l.add(headings[i].trim());
                            if (headings[i].trim().equals(name_clm_nm)) {
                                name_pos = i;
                            }
                        }

                    } else if (!line.startsWith("#") && !heading_l.isEmpty()) {
                        String[] values = line.split("\\t");
                        String c_name = null;
                        if (heading_l.size() == values.length) {
                            if (name_pos > 0 && values.length > (name_pos - 1)) {
                                c_name = values[name_pos];
                            }
                            if (c_name == null || !existing_name_l.contains(c_name)) {
                                existing_name_l.add(c_name);
                                for (int i = 0; i < values.length; i++) {
                                    content_map.get((heading_l.get(i))).add(values[i]);
                                }
                            }

                        } else {
                            formatcorrect = false;
                            printout(0, null, "Error 2589: File format for desciption incorrect. (" + filename + ")");
                        }
                    }
                }
                scan.close();
            } catch (FileNotFoundException ex) {
            }
        }
        return content_map;
    }
    /*
     methodID=6
     *    if (addmark) {
     //                        if (column_nm_l.get(i).equals("report_batch.name") && !report_batches_l.contains(c_cntent)) {
     //                            report_batches_l.remove(c_cntent);
     //                            report_batches_l.add(c_cntent);
     //                        } else if (column_nm_l.get(i).equals("report.name") && !report_l.contains(c_cntent)) {
     //                            report_l.remove(c_cntent);
     //                            report_l.add(c_cntent);
     //                        }
     //                    }
     */

    private String writeToDescriptFile2(String filename, String directory, String comments, HashMap<String, ArrayList<String>> content_map,
            ArrayList<String> compulsory_not_null_l, ArrayList<String> compulsory_preselected_l, ArrayList<String> compulsory_existing_l,
            boolean addmark, boolean recursive) {

        ArrayList<String> column_nm_l = new ArrayList<>(content_map.keySet());
        String heading = "##";
        for (int i = 0; i < column_nm_l.size(); i++) {
            String sepcial = "";
            String c_nm = column_nm_l.get(i);
            if (c_nm.endsWith(".id")) {
                sepcial = ID_COLUMN_COMMENT_FLAG;
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
                heading = heading + "\t" + c_nm + sepcial;
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
                printout(0, null, "Error 6a: Can not write to " + directory + ". Please veryfy you have write access.");
            }
        }
        File c_file = new File(filename);
        String parent_folder = null;
        if (c_file.isFile()) {
            try {
                parent_folder = c_file.getAbsoluteFile().getParentFile().getCanonicalPath();
                String backup_name = c_file.getName() + Timing.getDateTimeForFileName() + "~";
                File bakup_file = new File(parent_folder + File.separatorChar + backup_name);
                c_file.renameTo(bakup_file);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        try {
            egen_description_file = new FileWriter(filename, false);
            //if recursive then append
            egen_description_file.append(comments + "\n");
            egen_description_file.append(heading + "\n");
        } catch (IOException ex) {
            ex.printStackTrace();
        }
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
                            if (i == 0) {
                                c_content = c_content + content_map.get(c_k).get(j);
                            } else {
                                c_content = c_content + "\t" + content_map.get(c_k).get(j);
                            }
                        } else {
                            printout(0, null, "\tError : ");
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
        printout(0, null, "");
        if (egen_description_file != null) {
            try {
                egen_description_file.close();
                printout(0, null, "\n(2) Crating file " + filename + " @" + Timing.getDateTime());
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
            printout(0, null, "Error 6a: file creation failed " + filename);
        }
        return filename;
    }

    private void wirteToDescriptFile(String filename, String heading, String content, boolean recreate,
            ArrayList<String> overides_l, int content_repeats) {
        FileWriter egen_description_file;
        File c_file = new File(filename);
        String parent_folder = null;
        if (c_file.isFile()) {
            try {
                parent_folder = c_file.getAbsoluteFile().getParentFile().getCanonicalPath();
                String backup_name = c_file.getName() + Timing.getDateTimeForFileName() + "~";
                File bakup_file = new File(parent_folder + File.separatorChar + backup_name);
                c_file.renameTo(bakup_file);
            } catch (Exception ex) {
            }
        }
        try {
            File file = new File(filename);
            if (file.isFile() && !recreate) {
                String old_heading = "";
                String old_content = "";
                Scanner scan = new Scanner(file);
                while (scan.hasNext()) {
                    String line = scan.nextLine().trim();
                    if (!line.isEmpty()) {
                        if (line.startsWith("#")) {
                            old_heading = line + "\n";
                        } else {
                            String c_name = line.split("\\t")[0];
                            if (!overides_l.contains(c_name)) {
                                if (!old_content.isEmpty()) {
                                    old_content = old_content + "\n";
                                }
                                old_content = old_content + line;
                            }
                        }
                    }
                }

                egen_description_file = new FileWriter(filename, false);
                if (old_heading != null && !old_heading.isEmpty()) {
                    egen_description_file.append(old_heading);
                }
                if (old_content != null && !old_content.isEmpty()) {
                    egen_description_file.append(old_content);
                    if (content != null && !content.isEmpty()) {
                        egen_description_file.append("\n" + content);
                        for (int i = 1; i < content_repeats; i++) {
                            egen_description_file.append("\n" + content);
                        }

                    }
                } else {
                    if (content != null && !content.isEmpty()) {
                        egen_description_file.append(content);
                        for (int i = 1; i < content_repeats; i++) {
                            egen_description_file.append("\n" + content);
                        }
                    }
                }
                egen_description_file.close();
                printout(0, null, filename + " already exists and apending data.");
            } else {
                egen_description_file = new FileWriter(filename, false);
                egen_description_file.append(heading);
                if (content != null && !content.isEmpty()) {
                    egen_description_file.append(content);
                    for (int i = 1; i < content_repeats; i++) {
                        egen_description_file.append("\n" + content);
                    }
                }
                egen_description_file.close();
                printout(0, null, "\n" + filename + " -- created");
            }
        } catch (IOException ex) {
            printout(0, null, "Error 15: error creating description file " + filename + " " + ex.getMessage());
        }
    }

    private void writeWarningfile(String filename, String directory, HashMap<String, ArrayList<String>> errro_map) {
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
                printout(0, null, "Error 6a: Can not write to " + directory + ". Please veryfy you have write access.");
            }
        }
        File c_file = new File(filename);
        String parent_folder = null;
        if (c_file.isFile()) {
            try {
                parent_folder = c_file.getAbsoluteFile().getParentFile().getCanonicalPath();
                String backup_name = c_file.getName() + Timing.getDateTimeForFileName() + "~";
                File bakup_file = new File(parent_folder + File.separatorChar + backup_name);
                c_file.renameTo(bakup_file);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        try {
            printout(0, null, "(1) Crating file " + filename + " @" + Timing.getDateTime());
            egen_description_file = new FileWriter(filename, false);
            ArrayList<String> key_l = new ArrayList<>(errro_map.keySet());
            for (int i = 0; i < key_l.size(); i++) {
                egen_description_file.append(key_l.get(i) + "\n");
                ArrayList<String> tmp = errro_map.get(key_l.get(i));
                for (int j = 0; j < tmp.size(); j++) {
                    egen_description_file.append(tmp.get(j) + "\n");
                }
                egen_description_file.append("\n______________________________\n");
            }
//            egen_description_file.append(comments + "\n");
//            egen_description_file.append(heading + "\n");
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        if (egen_description_file != null) {
            try {
                egen_description_file.close();
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
            printout(0, null, "Error 6a: file creation failed " + filename);
        }
    }

    private boolean shouldthisbeallowed(Path test_path, ArrayList<String> avoid_l) {
        boolean allow = true;
        if (Files.isReadable(test_path)) {
            String name = test_path.getFileName().toString();
            if (name.startsWith(".") || name.endsWith("~")) {
                allow = false;
            } else {
                String location = test_path.toAbsolutePath().toString();
                for (int i = 0; (i < avoid_l.size() && allow); i++) {
                    if (location.equals(avoid_l.get(i)) || name.matches(avoid_l.get(i))) {
                        allow = false;
                    }
                }
            }

        } else {
            allow = false;
        }
        return allow;
    }

    private boolean shoudthisbeallowed(String c_file_nm, String file_location, ArrayList<String> avoid_l) {
        for (int i = 0; i < avoid_l.size(); i++) {
            if (file_location.equals(avoid_l.get(i)) || c_file_nm.matches(avoid_l.get(i))) {
                return false;
            }
        }
        return true;
    }

    /*
     MethodId=CMD1;
    
     * 10 : Missing locally, and in server ERROR
     * 20 : missing locally, found one in server
     * 30 : missing locally, found many in server 
     * 11 : One locall, none in server
     * 12 : Many locally, none in server
     * 
     * 110 : X
     * 111 : X
     * 112 : X
     * 
     * 120 : X
     * 121 : One local one remote equal  -- 0 
     * 122 : X
     * 
     * 130 : X
     * 131 : X
     * 132 : Many mocal many remote all equal --0
     * 
     * 210 : Not fund any where : Error 
     * 211 : Local only (one) --1 
     * 212 : Lcaol only  (many)--1 
     * 
     * 220 : Remote only (one) --1
     * 221 : X
     * 222 : X
     * 
     * 230 : Only remote (many)--1
     * 231 : X
     * 232 : X
     * 
     * 310 : No local no remote ERROR
     * 311 : Only local (one) -- 1
     * 312 : Only local (many) --1
     * 
     * 320 :  X
     * 321 :  One local one remote non equal -- 2
     * 322 :  Many local one remote, local not in remote --2 
     * 
     * 330 : X
     * 331 : One local many remnote, local not in remote --2 
     * 332 : many local many remote no equals -- 2
     * 
     * 410 : X
     * 411 : X
     * 412 : X
     * 
     * 420 : X
     * 421 : X 
     * 422 :  Many local, one remote, remote found locally -- 3
     * 
     * 430 : X
     * 431 : One local , many remote, local found remotly  -- 3 
     * 432 : Many local , many remote , some equal (hardest to handle) --3 
     *                         
     *
                            
     */
    private String[] getDiff(String current_dir, boolean verbose,
            long skip_larger, long skip_smaller, boolean skip_checksum,
            String output_dir, boolean recurse, String old_host) {
        int error = -1;
        FileWriter fos = null;
        String[] out_a = null;
        try {
            HashMap<String, String> loc2remt_flag = new HashMap<>();
            ArrayList<String> key_l = new ArrayList<>();
            String f_id = "FILES.ID";
            String f_name = "FILES.NAME";
            String f_chksm = "FILES.CHECKSUM";
            String f2p_id = "FILES2PATH.ID";
            String f2p_path = "FILES2PATH.FILEPATH";
            String f2p_loc = "FILES2PATH.LOCATION";
            String f2p_fid = "FILES2PATH.FILES_ID";
            String f2p_date = "FILES2PATH.LASTMODIFIED";
            String f2p_owner = "FILES2PATH.OWNERGROUP";
            String f_size = "FILES.FILESIZE";
            key_l.add(f_id);
            key_l.add(f_name);
            key_l.add(f_chksm);
            key_l.add(f2p_id);
            key_l.add(f2p_path);
            key_l.add(f2p_loc);
            key_l.add(f2p_fid);
            key_l.add(f2p_date);
            key_l.add(f2p_owner);
            key_l.add(f_size);

            for (int i = 0; i < key_l.size(); i++) {
                loc2remt_flag.put(key_l.get(i), null);
            }
            String r_f_id = "R_FILES.ID";
            String r_f_name = "R_FILES.NAME";
            String r_f_chksm = "R_FILES.CHECKSUM";
            String r_f2p_id = "R_FILES2PATH.ID";
            String r_f2p_path = "R_FILES2PATH.FILEPATH";
            String r_f2p_loc = "R_FILES2PATH.LOCATION";
            String r_f2p_fid = "R_FILES2PATH.FILES_ID";
            String r_f2p_date = "R_FILES2PATH.LASTMODIFIED";
            String r_f2p_owner = "R_FILES2PATH.OWNERGROUP";
            String r_f_size = "R_FILES.FILESIZE";
            key_l.add(r_f_id);
            key_l.add(r_f_name);
            key_l.add(r_f_chksm);
            key_l.add(r_f2p_id);
            key_l.add(r_f2p_path);
            key_l.add(r_f2p_loc);
            key_l.add(r_f2p_fid);
            key_l.add(r_f2p_date);
            key_l.add(r_f2p_owner);
            key_l.add(r_f_size);
            for (int i = 0; i < key_l.size(); i++) {
                String c_key = (key_l.get(i));
                if (!c_key.startsWith("R_")) {
                    String r_key = "R_" + c_key;
                    if (key_l.contains(r_key)) {
                        loc2remt_flag.put(c_key, r_key);
                    }
                }
            }
            if (output_dir == null) {
                printout(0, null, "Error: output location is null");
            } else {
                Path out_dir_p = Paths.get(output_dir);
                if (Files.exists(out_dir_p) && Files.isWritable(out_dir_p)) {
                    try {
                        output_dir = out_dir_p.toRealPath().toString();
                    } catch (IOException ex) {
                        printout(0, null, "Error : " + ex.getMessage());
                    }
                    if (!output_dir.endsWith(File.separator)) {
                        output_dir = output_dir + File.separator;
                    }
                    fos = new FileWriter(output_dir + ".diff_scores.txt", false);
                    fos.append("#Score report, generated on  " + Timing.getDateTime() + "\n");
                    fos.append("#0-All equal, 1-either in server or locally, not both, 2-Some are equal, 3-All different\n");
                    fos.append("#SHA-1_checksum\tChecksum_score\tPath_score\tHost_score\n\n");
                    if (current_dir == null) {
                        current_dir = getPWD();
                    }

                    ArrayList<String> local_avoid_l = new ArrayList<>();
                    ArrayList<String> local_fileList = new ArrayList<>();
                    if (recurse) {
                        local_fileList = getFiles_recursive(Paths.get(current_dir), true, local_avoid_l);
                    } else {
                        local_fileList = getFiles_no_recurse(current_dir);
                    }
                    if (local_fileList.isEmpty()) {
                        printout(0, null, "Files list was emty, nothing to evaluate. Use -recurse option to check subfolders");
                    } else {
                        String curreent_host = getServerName();
                        HashMap<String, HashMap<String, ArrayList<String>>> hash2details_map = new HashMap<>();
                        HashMap<String, HashMap<Integer, HashMap<String, String>>> remote_all_hash2details_map = new HashMap<>();
                        HashMap<String, HashMap<Integer, HashMap<String, String>>> local_all_hash2details_map = new HashMap<>();
                        ArrayList<String> only_on_local_paths_l = new ArrayList<>();
                        ArrayList<String> all_evaluated_paths_l = new ArrayList<>();
                        HashMap<String, String> local_path2hash_map = new HashMap<>();
                        //From Local

                        if (!local_fileList.isEmpty()) {
                            local_path2hash_map = getHashes(local_fileList, skip_larger, skip_smaller, skip_checksum);
                            HashMap<String, HashMap<String, String>> new_local_path2details_map = getFileDetails(local_fileList, null, null);
                            only_on_local_paths_l = new ArrayList<>(new_local_path2details_map.keySet()); //check only the new path against DB to incrase profomance
                            all_evaluated_paths_l = new ArrayList<>(new_local_path2details_map.keySet());
                            hash2details_map = new HashMap<>();
                            ArrayList<String> local_path2hash_key_l = new ArrayList<>(local_path2hash_map.keySet());
                            for (int i = 0; i < local_path2hash_key_l.size(); i++) {
                                String c_path = local_path2hash_key_l.get(i);
                                String c_hash = local_path2hash_map.get(local_path2hash_key_l.get(i));
                                if (!hash2details_map.containsKey(c_hash)) {
                                    hash2details_map.put(c_hash, new HashMap<String, ArrayList<String>>());
                                    for (int j = 0; j < key_l.size(); j++) {
                                        hash2details_map.get(c_hash).put(key_l.get(j), new ArrayList<String>());
                                    }
                                }
                                int c_all_pos = 0;
                                if (!local_all_hash2details_map.containsKey(c_hash)) {
                                    local_all_hash2details_map.put(c_hash, new HashMap<Integer, HashMap<String, String>>());
                                    local_all_hash2details_map.get(c_hash).put(c_all_pos, new HashMap<String, String>());
                                } else {
                                    c_all_pos = local_all_hash2details_map.get(c_hash).size();
                                    local_all_hash2details_map.get(c_hash).put(c_all_pos, new HashMap<String, String>());
                                }
                                new_local_path2details_map.get(c_path).put(f_chksm, c_hash);
                                HashMap<String, String> tmp_p2d_map = new_local_path2details_map.get(c_path);
                                ArrayList<String> tmp_key_l = new ArrayList<>(tmp_p2d_map.keySet());
                                for (int j = 0; j < tmp_key_l.size(); j++) {
                                    String c_key = tmp_key_l.get(j).toUpperCase();
                                    if (tmp_key_l.get(j) != null && hash2details_map.get(c_hash).containsKey(c_key) && !hash2details_map.get(c_hash).get(c_key).contains(tmp_p2d_map.get(tmp_key_l.get(j)))) {
                                        hash2details_map.get(c_hash).get(c_key).add(tmp_p2d_map.get(tmp_key_l.get(j)));
                                    }
                                    local_all_hash2details_map.get(c_hash).get(c_all_pos).put(c_key, tmp_p2d_map.get(tmp_key_l.get(j)));
                                }
                            }
                            new_local_path2details_map.clear();
                        } else {
                            printout(0, null, "File list was empty");
                        }
                        //From remote using hash
                        ArrayList<String> hashes_l = new ArrayList<>(hash2details_map.keySet());
                        if (!hashes_l.isEmpty()) {
                            int last_progress = 0;
                            StringBuilder checksums = new StringBuilder();
                            for (int i = 0; i < hashes_l.size(); i++) {
                                int progrss = ((i * 100) / hashes_l.size());
                                if (progrss > last_progress) {
                                    if (progrss % 10 == 0) {
                                        System.out.print(".");
                                    }
                                    last_progress = progrss;
                                }
                                checksums.append(hashes_l.get(i) + "||");
                                if (((i > split_limit) && (i % split_limit == 0)) || ((i + 1) == hashes_l.size())) {
                                    List part_matches_l = advancedQueryHandler("files.checksum=" + checksums.toString(), "FILES2PATH.FILEPATH,FILES2PATH.ID,FILES.CHECKSUM,FILES.ID,FILES2PATH.LOCATION,FILES.NAME",
                                            true, false, true, false, false, false, false, 0, true);
                                    checksums.setLength(0);
                                    if (part_matches_l != null) {
                                        for (int j = 0; j < part_matches_l.size(); j++) {
                                            if (part_matches_l.get(j) != null) {
                                                String[] c_det_a = part_matches_l.get(j).toString().split("\\|\\|");
                                                String c_path = null;
                                                String c_check = null;
                                                HashMap<String, ArrayList<String>> fromdb_map = new HashMap<>();
                                                HashMap<Integer, HashMap<String, String>> full_fromdb_map = new HashMap<>();
                                                full_fromdb_map.put(0, new HashMap<String, String>());
                                                for (int k = 0; k < c_det_a.length; k++) {
                                                    String[] c_sub_a = c_det_a[k].split("=");
                                                    String r_key = loc2remt_flag.get(c_sub_a[0].toUpperCase());
                                                    if (c_sub_a.length == 2) {
                                                        c_sub_a[1] = c_sub_a[1].trim();
                                                        full_fromdb_map.get(0).put(c_sub_a[0].toUpperCase(), c_sub_a[1]);
                                                        if (c_sub_a[0].toUpperCase().equals(f_chksm)) {
                                                            c_check = c_sub_a[1];
                                                        } else if (c_sub_a[0].toUpperCase().equals(f2p_path)) {
                                                            c_path = c_sub_a[1];
                                                        }
                                                        if (fromdb_map.containsKey(r_key) && !fromdb_map.get(r_key).contains(c_sub_a[1])) {
                                                            fromdb_map.get(r_key).add(c_sub_a[1]);
                                                        } else {
                                                            fromdb_map.put(r_key, new ArrayList<String>());
                                                            fromdb_map.get(r_key).add(c_sub_a[1]);
                                                        }
                                                    } else {
                                                        printout(0, null, "Error CMD1d: Results from webservice not of expected format");
                                                    }

                                                }

                                                if (c_path != null && c_check != null) {
                                                    if (remote_all_hash2details_map.containsKey(c_check)) {
                                                        remote_all_hash2details_map.get(c_check).put(remote_all_hash2details_map.get(c_check).size(), full_fromdb_map.get(0));
                                                    } else {
                                                        remote_all_hash2details_map.put(c_check, full_fromdb_map);
                                                    }

                                                    if (local_path2hash_map.containsKey(c_path) && local_path2hash_map.get(c_path).equals(c_check)) {
                                                        only_on_local_paths_l.remove(c_path); //reduce the number of paths to be check against db
                                                    }

                                                    if (!all_evaluated_paths_l.contains(c_path)) {
                                                        all_evaluated_paths_l.add(c_path);
                                                    }
                                                    if (!hash2details_map.containsKey(c_check)) {
                                                        hash2details_map.put(c_check, new HashMap<String, ArrayList<String>>());
                                                        for (int k = 0; k < key_l.size(); k++) {
                                                            hash2details_map.get(c_check).put(key_l.get(k), new ArrayList<String>());
                                                        }
                                                    }

                                                    for (int k = 0; k < key_l.size(); k++) {
                                                        if (fromdb_map.get(key_l.get(k)) != null) {
                                                            fromdb_map.get(key_l.get(k)).removeAll(hash2details_map.get(c_check).get(key_l.get(k)));
                                                            hash2details_map.get(c_check).get(key_l.get(k)).addAll(fromdb_map.get(key_l.get(k)));
                                                        }
                                                    }

                                                } else {
                                                    printout(0, null, "Error CMD1e: path or checksum null c_path=" + c_path + " c_check=" + c_check);
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        } else {
                            printout(0, null, "Error CMD1b: evaluating checksum failed");
                        }
                        //From remote get using path
                        if (!only_on_local_paths_l.isEmpty()) {
                            StringBuilder paths = new StringBuilder();
                            int last_progress = 0;
                            for (int i = 0; i < only_on_local_paths_l.size(); i++) {
                                int progrss = ((i * 100) / hashes_l.size());
                                if (progrss > last_progress) {
                                    if (progrss % 10 == 0) {
                                        System.out.print(".");
                                    }
                                }

                                paths.append(only_on_local_paths_l.get(i) + "||");
                                if (((i > split_limit) && (i % split_limit == 0)) || ((i + 1) == only_on_local_paths_l.size())) {
                                    List part_matches_l = advancedQueryHandler("FILES2PATH.FILEPATH=" + paths.toString(), "FILES2PATH.FILEPATH&&FILES2PATH.ID&&FILES2PATH.FILES_ID&&FILES.CHECKSUM&&FILES2PATH.LOCATION&&FILES2PATH.LASTMODIFIED",
                                            true, false, true, false, false, false, false, 0, true);
                                    paths.setLength(0);
                                    if (part_matches_l != null) {
                                        for (int j = 0; j < part_matches_l.size(); j++) {
                                            if (part_matches_l.get(j) != null) {
                                                String[] c_det_a = part_matches_l.get(j).toString().split("\\|\\|");
                                                String c_path = null;
                                                String c_check = null;
                                                HashMap<Integer, HashMap<String, String>> full_fromdb_map = new HashMap<>();
                                                full_fromdb_map.put(j, new HashMap<String, String>());
                                                HashMap<String, ArrayList<String>> fromdb_map = new HashMap<>();
                                                for (int k = 0; k < c_det_a.length; k++) {
                                                    String[] c_sub_a = c_det_a[k].split("=");
                                                    String r_key = loc2remt_flag.get(c_sub_a[0].toUpperCase());
                                                    if (c_sub_a.length == 2) {
                                                        c_sub_a[1] = c_sub_a[1].trim();
                                                        full_fromdb_map.get(j).put(c_sub_a[0].toUpperCase(), c_sub_a[1]);
                                                        if (c_sub_a[0].toUpperCase().equals(f_chksm)) {
                                                            c_check = c_sub_a[1];
                                                        } else if (c_sub_a[0].toUpperCase().equals(f2p_path)) {
                                                            c_path = c_sub_a[1];
                                                        }
                                                        if (fromdb_map.containsKey(r_key) && !fromdb_map.get(r_key).contains(c_sub_a[1])) {
                                                            fromdb_map.get(r_key).add(c_sub_a[1]);
                                                        } else {
                                                            fromdb_map.put(r_key, new ArrayList<String>());
                                                            fromdb_map.get(r_key).add(c_sub_a[1]);
                                                        }
                                                    } else {
                                                        printout(0, null, "Error CMD1d: Results from webservice not of expected format");
                                                    }
                                                }
                                                if (c_check != null && c_path != null) {
                                                    if (remote_all_hash2details_map.containsKey(c_check)) {
                                                        remote_all_hash2details_map.get(c_check).put(remote_all_hash2details_map.get(c_check).size(), full_fromdb_map.get(0));
                                                    } else {
                                                        remote_all_hash2details_map.put(c_check, full_fromdb_map);
                                                    }
                                                    only_on_local_paths_l.remove(c_path); //as a test for later                                                                                 
                                                    HashMap<String, ArrayList<String>> tarns_ahsh_fromdb_map = null;
                                                    String trasn_hash = null;
                                                    if (local_path2hash_map.containsKey(c_path)) {
                                                        trasn_hash = local_path2hash_map.get(c_path);
                                                        if (hash2details_map.containsKey(trasn_hash)) {
                                                            tarns_ahsh_fromdb_map = new HashMap<>();
                                                            for (int k = 0; k < key_l.size(); k++) {
                                                                if (fromdb_map.get(key_l.get(k)) != null) {
                                                                    tarns_ahsh_fromdb_map.put(key_l.get(k), fromdb_map.get(key_l.get(k)));
                                                                }
                                                            }
                                                        }
                                                    }
                                                    if (hash2details_map.containsKey(trasn_hash)) {
                                                        for (int k = 0; k < key_l.size(); k++) {
                                                            if (tarns_ahsh_fromdb_map != null && tarns_ahsh_fromdb_map.containsKey(key_l.get(k))) {
                                                                if (tarns_ahsh_fromdb_map.containsKey(key_l.get(k))) {
                                                                    tarns_ahsh_fromdb_map.get(key_l.get(k)).removeAll(hash2details_map.get(trasn_hash).get(key_l.get(k)));
                                                                    hash2details_map.get(trasn_hash).get(key_l.get(k)).addAll(tarns_ahsh_fromdb_map.get(key_l.get(k)));
                                                                }
                                                            }
                                                        }
                                                    } else {
                                                        printout(0, null, "Effor CMD1k: trasn_hash missing");
                                                    }

                                                } else {
                                                    printout(0, null, "Error CMD1f: path or checksum null c_path=" + c_path + " c_check=" + c_check);
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        } else {
                            printout(0, null, "Not checking the EGDBM using paths, as all paths were validate.");
                        }

                        //evauations                

                        String FILEPATH_CHANGED_LOCATION = "FILES2PATH_UPDATE_HOST";
                        String FILESPATH_NEW_PATH = "FILES2PATH_ADD_PATH";
                        String FILEPATH_CHANGED_PATH = "FILES2PATH_UPDATE_PATH";
                        String FILEPATH_MISSING_PATH = "FILES2PATH_MISSING_PATH";
                        String FILE_CHANGED_CHECKSUM = "FILE_UPDATE_CHECKSUM";
                        String FILES2PATH_NO_CHANGE = "FILES2PATH_NO_CHANGE";
                        String FILEPATH_REMOVED_FILEPATH = "FILES2PATH_DELETE_FILEPATH";
                        String FILEPATH_NOTCHECKED_PATH = "FILES2PATH_NOTCHECKED_PATH";
//                        String FILEPATH_DELETE_PATH = "FILES2PATH_DELETE_PATH";
//                        String FILE_CHANGED_NAME = "FILE_CHANGED_NAME";
                        LinkedHashMap<String, HashMap<String, HashMap<Integer, HashMap<String, String>>>> summary_map = new LinkedHashMap<>();


                        //Duplicates found elsewhere using checksum
                        HashMap<String, String> key_descript_map = new HashMap<>();
                        key_descript_map.put(FILEPATH_CHANGED_LOCATION, "Found in server using checksum, but the current location is different from the recoreded host");
                        key_descript_map.put(FILESPATH_NEW_PATH, "Found in server using checksum, but the current path is not found in server");
                        key_descript_map.put(FILEPATH_CHANGED_PATH, "Found in server using checksum, but the current path is not found in server ");
                        key_descript_map.put(FILEPATH_MISSING_PATH, "Duplicated elsewhere. Files found in other locations, with the same checksum");
                        key_descript_map.put(FILES2PATH_NO_CHANGE, "Files not changed. i.e. Found locally and on the server, with the same checksum");
                        key_descript_map.put(FILE_CHANGED_CHECKSUM, "Found locally and on the server, but checksum different (file modified after dipositting)");
                        key_descript_map.put(FILEPATH_REMOVED_FILEPATH, "Files deleted after reporting");
                        key_descript_map.put(FILEPATH_NOTCHECKED_PATH, "Files or directories not evaluated (e.g. hidden files)");
//                        key_descript_map.put(FILEPATH_DELETE_PATH, "Files deleted after reporting");
//                        key_descript_map.put(FILE_CHANGED_NAME, ". File name needs to be updated according to the change in path requested");
//                    HashMap<String, HashMap<Integer, HashMap<String, String>>> location_changed_map = new HashMap<>();
//                    HashMap<String, HashMap<Integer, HashMap<String, String>>> new_filepaths_map = new HashMap<>();
//                    HashMap<String, HashMap<Integer, HashMap<String, String>>> filepaths_changed_map = new HashMap<>();
//                    HashMap<String, HashMap<Integer, HashMap<String, String>>> locally_missing_path_map = new HashMap<>();
//                    HashMap<String, HashMap<Integer, HashMap<String, String>>> not_changed_map = new HashMap<>();
//                    HashMap<String, HashMap<Integer, HashMap<String, String>>> checksum_cganged_map = new HashMap<>();
//                    HashMap<String, HashMap<Integer, HashMap<String, String>>> removed_paths_map = new HashMap<>();

                        summary_map.put(FILEPATH_CHANGED_LOCATION, new HashMap<String, HashMap<Integer, HashMap<String, String>>>());
                        summary_map.put(FILESPATH_NEW_PATH, new HashMap<String, HashMap<Integer, HashMap<String, String>>>());
                        summary_map.put(FILEPATH_CHANGED_PATH, new HashMap<String, HashMap<Integer, HashMap<String, String>>>());
                        summary_map.put(FILEPATH_MISSING_PATH, new HashMap<String, HashMap<Integer, HashMap<String, String>>>());
                        summary_map.put(FILES2PATH_NO_CHANGE, new HashMap<String, HashMap<Integer, HashMap<String, String>>>());
                        summary_map.put(FILE_CHANGED_CHECKSUM, new HashMap<String, HashMap<Integer, HashMap<String, String>>>());
                        summary_map.put(FILEPATH_REMOVED_FILEPATH, new HashMap<String, HashMap<Integer, HashMap<String, String>>>());
                        summary_map.put(FILEPATH_REMOVED_FILEPATH, new HashMap<String, HashMap<Integer, HashMap<String, String>>>());
                        summary_map.get(FILEPATH_REMOVED_FILEPATH).put("NA", new HashMap<Integer, HashMap<String, String>>());
//                        summary_map.put(FILE_CHANGED_NAME, new HashMap<String, HashMap<Integer, HashMap<String, String>>>());
                        summary_map.put(FILEPATH_NOTCHECKED_PATH, new HashMap<String, HashMap<Integer, HashMap<String, String>>>());
                        summary_map.get(FILEPATH_NOTCHECKED_PATH).put("NA", new HashMap<Integer, HashMap<String, String>>());


                        HashMap<String, ArrayList<String>> keys_to_keep_map = new HashMap<>();
                        keys_to_keep_map.put(FILEPATH_CHANGED_LOCATION, new ArrayList<>(Arrays.asList(new String[]{f2p_id, f2p_loc, f2p_path, f2p_date})));
                        keys_to_keep_map.put(FILESPATH_NEW_PATH, new ArrayList<>(Arrays.asList(new String[]{f2p_id, f2p_fid, f2p_loc, f2p_path, f2p_date})));
                        keys_to_keep_map.put(FILEPATH_CHANGED_PATH, new ArrayList<>(Arrays.asList(new String[]{f2p_id, f2p_path, f2p_date, f_id, f_name})));
                        keys_to_keep_map.put(FILEPATH_MISSING_PATH, new ArrayList<>(Arrays.asList(new String[]{f2p_path})));
                        keys_to_keep_map.put(FILES2PATH_NO_CHANGE, new ArrayList<>(Arrays.asList(new String[]{f2p_path})));
                        keys_to_keep_map.put(FILE_CHANGED_CHECKSUM, new ArrayList<>(Arrays.asList(new String[]{f_id, f_name, f_chksm})));
//                        keys_to_keep_map.put(FILE_CHANGED_NAME, new ArrayList<>(Arrays.asList(new String[]{f_id, f_name})));
                        keys_to_keep_map.put(FILEPATH_REMOVED_FILEPATH, new ArrayList<>(Arrays.asList(new String[]{f2p_id, f2p_path})));
                        keys_to_keep_map.put(FILEPATH_NOTCHECKED_PATH, new ArrayList<>(Arrays.asList(new String[]{f2p_path})));
//                        keys_to_keep_map.put(FILEPATH_DELETE_PATH, new ArrayList<>(Arrays.asList(new String[]{f2p_path})));

                        ArrayList<String> hash2details_map_key_l = new ArrayList<>(hash2details_map.keySet());

                        for (int i = 0; i < hash2details_map_key_l.size(); i++) {
                            String c_hash = hash2details_map_key_l.get(i);
                            HashMap<String, ArrayList<String>> c_details_map = hash2details_map.get(c_hash);
                            int[] scores_a = new int[key_l.size()];
                            for (int j = 0; j < key_l.size(); j++) {
                                String c_key = key_l.get(j);
                                if (loc2remt_flag.containsKey(c_key)) {
                                    if (c_details_map.containsKey(c_key) && !c_details_map.get(c_key).isEmpty()) {
                                        if (c_details_map.get(c_key).size() == 1) {
                                            scores_a[j] = 1;//only one path found locally
                                        } else {
                                            scores_a[j] = 2;//more than one path found locally
                                        }
                                    } else {
                                        scores_a[j] = 0;//path not found locally
                                    }
                                    String r_key = loc2remt_flag.get(c_key);
                                    if (c_details_map.containsKey(r_key) && !c_details_map.get(r_key).isEmpty()) {
                                        if (c_details_map.get(r_key).size() == 1) {
                                            scores_a[j] = scores_a[j] + 20;//only one path found locally
                                        } else {
                                            scores_a[j] = scores_a[j] + 30;//more than one path found locally
                                        }
                                    } else {
                                        scores_a[j] = scores_a[j] + 10;//path not found locally
                                    }
                                    if ((c_details_map.get(c_key).size() == c_details_map.get(loc2remt_flag.get(c_key)).size())
                                            && c_details_map.get(loc2remt_flag.get(c_key)).containsAll(c_details_map.get(c_key))) {
                                        scores_a[j] = scores_a[j] + 100;
                                    } else if (scores_a[j] == 10 || scores_a[j] == 11 || scores_a[j] == 12 || scores_a[j] == 20 || scores_a[j] == 30) {
                                        scores_a[j] = scores_a[j] + 200;
                                    } else if (scores_a[j] == 21 || scores_a[j] == 31 || scores_a[j] == 22 || scores_a[j] == 32) {
//                                         printout(0,null, "5529 " + c_hash + " c_details_map.get(c_key)=" + c_details_map.get(c_key) + "\n" + c_details_map.get(loc2remt_flag.get(c_key)));
                                        if (Collections.disjoint(c_details_map.get(c_key), c_details_map.get(loc2remt_flag.get(c_key)))) {
                                            scores_a[j] = scores_a[j] + 300;
                                        } else {
                                            scores_a[j] = scores_a[j] + 400;
                                        }
                                    } else {
                                        printout(0, null, "Warning , undedefined score " + scores_a[j]);
                                    }
                                }
                            }

                            //Group scors
                            for (int j = 0; j < scores_a.length; j++) {
                                if (scores_a[j] == 121 || scores_a[j] == 132) {
                                    scores_a[j] = 0;  // All equal
                                } else if (scores_a[j] == 211 || scores_a[j] == 212 || scores_a[j] == 220 || scores_a[j] == 230) {
                                    scores_a[j] = 1;  // only in one location
                                } else if (scores_a[j] == 422 || scores_a[j] == 431 || scores_a[j] == 432) {
                                    scores_a[j] = 2;  // some equal
                                } else if (scores_a[j] == 321 || scores_a[j] == 322 || scores_a[j] == 331 || scores_a[j] == 332) {
                                    scores_a[j] = 3;  // all different 
                                } else {
                                    scores_a[j] = 4;  // undefined
                                }
                            }

                            int hash_pos = key_l.indexOf(f_chksm);
                            int path_pos = key_l.indexOf(f2p_path);
                            int loc_pos = key_l.indexOf(f2p_loc);

                            HashMap<Integer, HashMap<String, String>> local_full_map = local_all_hash2details_map.get(c_hash);
                            HashMap<Integer, HashMap<String, String>> remote_full_map = remote_all_hash2details_map.get(c_hash);
                            fos.append(c_hash + "\t" + scores_a[hash_pos] + "\t" + scores_a[path_pos] + "\t" + scores_a[loc_pos] + "\n");

                            if (scores_a[hash_pos] == 0 && scores_a[path_pos] == 0 && scores_a[loc_pos] == 0) {//0     000
                                //no change
                                ArrayList<String> path_l = c_details_map.get(f2p_path);
                                if (!path_l.isEmpty()) {
                                    summary_map.get(FILES2PATH_NO_CHANGE).put(c_hash, new HashMap<Integer, HashMap<String, String>>());
                                    summary_map.get(FILES2PATH_NO_CHANGE).get(c_hash).putAll(getAllSubling_l(remote_full_map, f2p_path, path_l, null, null, keys_to_keep_map.get(FILES2PATH_NO_CHANGE)));
                                } else {
                                    error = 0;
                                }
                            } else if (scores_a[hash_pos] == 0 && scores_a[path_pos] == 0 && scores_a[loc_pos] == 1) {//1     001
                                //if location missing in server update it
                                ArrayList<String> loc_l = new ArrayList<>();
                                loc_l.addAll(c_details_map.get(f2p_loc));
                                loc_l.removeAll(c_details_map.get(r_f2p_loc));
                                if (!loc_l.isEmpty()) {
                                    HashMap<String, String> additionals_map = new HashMap<>();
                                    String path_id = remote_all_hash2details_map.get(c_hash).get(0).get(f2p_id);
                                    additionals_map.put(f2p_id, path_id);
                                    summary_map.get(FILEPATH_CHANGED_LOCATION).put(c_hash, new HashMap<Integer, HashMap<String, String>>());
                                    summary_map.get(FILEPATH_CHANGED_LOCATION).get(c_hash).putAll(getAllSubling_l(local_full_map, f2p_loc, loc_l, additionals_map, null, keys_to_keep_map.get(FILEPATH_CHANGED_LOCATION)));
                                }
                            } else if (scores_a[hash_pos] == 0 && scores_a[path_pos] == 0 && (scores_a[loc_pos] == 2 || scores_a[loc_pos] == 3)) {//2     002
                                //Atleast some locations changed
                                ArrayList<String> r_loc_l = new ArrayList<>();
                                r_loc_l.addAll(c_details_map.get(r_f2p_loc));
                                r_loc_l.removeAll(c_details_map.get(f2p_loc));
                                ArrayList<String> r_path_l = new ArrayList<>();
                                r_path_l.addAll(c_details_map.get(r_f2p_path));
                                HashMap<String, String> path2newLoc_map = new HashMap<>();
                                if (old_host != null && (r_loc_l.contains(old_host))) {
                                    for (int j = 0; j < r_path_l.size(); j++) {
                                        String c_path = r_path_l.get(j);
                                        HashMap<String, String> condition_map = new HashMap<>();
                                        condition_map.put(f2p_path, c_path);
                                        String c_r_host = getSubling(remote_full_map, condition_map, f2p_loc);
                                        String local_host = getSubling(local_full_map, condition_map, f2p_loc);
                                        if (c_r_host.equals(old_host)) {
                                            path2newLoc_map.put(old_host, local_host);
                                            r_path_l.remove(j);
                                            j--;
                                        }
                                    }
                                }
                                if (!path2newLoc_map.isEmpty()) {
                                    HashMap<String, HashMap<String, String>> overide_map = new HashMap<>();
                                    overide_map.put(f2p_loc, path2newLoc_map);
                                    summary_map.get(FILEPATH_CHANGED_LOCATION).put(c_hash, new HashMap<Integer, HashMap<String, String>>());
                                    summary_map.get(FILEPATH_CHANGED_LOCATION).get(c_hash).putAll(getAllSubling(remote_full_map, f2p_loc, old_host, null, overide_map, keys_to_keep_map.get(FILEPATH_CHANGED_LOCATION)));
                                }
                                if (!r_path_l.isEmpty()) {
                                    HashMap<String, String> additionals_map = new HashMap<>();
                                    String files_id = remote_all_hash2details_map.get(c_hash).get(0).get(f2p_fid);
                                    additionals_map.put(f2p_id, files_id);
                                    summary_map.get(FILESPATH_NEW_PATH).put(c_hash, new HashMap<Integer, HashMap<String, String>>());
                                    summary_map.get(FILESPATH_NEW_PATH).get(c_hash).putAll(getAllSubling_l(local_full_map, f2p_path, r_path_l, additionals_map, null, keys_to_keep_map.get(FILESPATH_NEW_PATH)));
                                }
                            } else if (scores_a[hash_pos] == 0 && scores_a[path_pos] == 0 && scores_a[loc_pos] == 3) {//3     003
                                //REF:002
                            } else if (scores_a[hash_pos] == 0 && scores_a[path_pos] == 1 && scores_a[loc_pos] == 0) {//4     010
                                //Entry deleted in location 
                                ArrayList<String> l_path_l = new ArrayList<>();
                                l_path_l.addAll(c_details_map.get(f2p_path));
                                l_path_l.removeAll(c_details_map.get(r_f2p_loc));
                                if (!l_path_l.isEmpty()) {
                                    summary_map.get(FILEPATH_REMOVED_FILEPATH).put(c_hash, new HashMap<Integer, HashMap<String, String>>());
                                    summary_map.get(FILEPATH_REMOVED_FILEPATH).get(c_hash).putAll(getAllSubling_l(remote_full_map, f2p_loc, l_path_l, null, null, keys_to_keep_map.get(FILEPATH_REMOVED_FILEPATH)));
                                }
                            } else if (scores_a[hash_pos] == 0 && scores_a[path_pos] == 1 && scores_a[loc_pos] == 1) {//5     011
                                error = 5; //No path found, may be ophen entry
                            } else if (scores_a[hash_pos] == 0 && scores_a[path_pos] == 1 && scores_a[loc_pos] == 2) {//6     012
                                error = 6; //No path found, may be ophen entry
                            } else if (scores_a[hash_pos] == 0 && scores_a[path_pos] == 1 && scores_a[loc_pos] == 3) {//7     013
                                error = 7; //No path found, may be ophen entry
                            } else if (scores_a[hash_pos] == 0 && scores_a[path_pos] == 2 && scores_a[loc_pos] == 1) {//9     021
                                error = 9;
                            } else if (scores_a[hash_pos] == 0 && (scores_a[path_pos] == 2 || scores_a[path_pos] == 3)
                                    && (scores_a[loc_pos] == 0 || scores_a[loc_pos] == 2 || scores_a[loc_pos] == 3)) {//10    022                                    
                                ArrayList<String> remote_only_path_l = new ArrayList<>();
                                remote_only_path_l.addAll(c_details_map.get(r_f2p_path));
                                remote_only_path_l.removeAll(c_details_map.get(f2p_path));

                                ArrayList<String> local_only_path_l = new ArrayList<>();
                                local_only_path_l.addAll(c_details_map.get(f2p_path));
                                local_only_path_l.removeAll(c_details_map.get(r_f2p_path));

                                ArrayList<String> remote_and_ocal_only_path_l = new ArrayList<>();
                                remote_and_ocal_only_path_l.addAll(c_details_map.get(f2p_path));
                                remote_and_ocal_only_path_l.retainAll(c_details_map.get(r_f2p_path));

                                if (!remote_and_ocal_only_path_l.isEmpty()) {
                                    for (int j = 0; j < remote_and_ocal_only_path_l.size(); j++) {
                                        String c_path = remote_and_ocal_only_path_l.get(j);
                                        HashMap<String, String> condition_map = new HashMap<>();
                                        condition_map.put(f2p_path, c_path);
                                        String remote_host = getSubling(remote_full_map, condition_map, f2p_loc);
                                        String local_host = getSubling(local_full_map, condition_map, f2p_loc);
                                        if (remote_host.equals(local_host)) {
                                            summary_map.get(FILES2PATH_NO_CHANGE).put(c_hash, new HashMap<Integer, HashMap<String, String>>());
                                            summary_map.get(FILES2PATH_NO_CHANGE).get(c_hash).putAll(getAllSubling(remote_full_map, f2p_path, c_path, null, null, keys_to_keep_map.get(FILES2PATH_NO_CHANGE)));
                                            remote_and_ocal_only_path_l.remove(j);
                                            j--;
                                        } else if (old_host != null && remote_host.equals(old_host)) {
                                            HashMap<String, String> additionals_map = new HashMap<>();
                                            additionals_map.put(f2p_loc, local_host);
                                            summary_map.get(FILEPATH_CHANGED_LOCATION).put(c_hash, new HashMap<Integer, HashMap<String, String>>());
                                            summary_map.get(FILEPATH_CHANGED_LOCATION).get(c_hash).putAll(getAllSubling(remote_full_map, f2p_path, c_path, additionals_map, null, keys_to_keep_map.get(FILEPATH_CHANGED_LOCATION)));
                                        } else {
                                            String files_id = remote_all_hash2details_map.get(c_hash).get(0).get(f2p_fid);
                                            HashMap<String, String> additionals_map = new HashMap<>();
                                            additionals_map.put(f2p_fid, files_id);
                                            summary_map.get(FILESPATH_NEW_PATH).put(c_hash, new HashMap<Integer, HashMap<String, String>>());
                                            summary_map.get(FILESPATH_NEW_PATH).get(c_hash).putAll(getAllSubling(local_full_map, f2p_path, c_path, additionals_map, null, keys_to_keep_map.get(FILESPATH_NEW_PATH)));
                                        }
                                    }
                                }

                                String files_id = remote_all_hash2details_map.get(c_hash).get(0).get(f_id);
                                if (!remote_only_path_l.isEmpty() && !local_only_path_l.isEmpty()) {
                                    for (int j = 0; j < local_only_path_l.size(); j++) {
                                        String local = local_only_path_l.get(j);
                                        String maped_path = null;
                                        for (int k = 0; (k < remote_only_path_l.size() && maped_path == null); k++) {
                                            if (Paths.get(local).getFileName().equals(Paths.get(remote_only_path_l.get(k)).getFileName())) {
                                                maped_path = remote_only_path_l.get(k);
                                            }
                                        }
                                        if (maped_path != null) {
                                            HashMap<String, String> condition_map = new HashMap<>();
                                            condition_map.put(f2p_path, local);
                                            String c_l_host = getSubling(local_full_map, condition_map, f2p_loc);
                                            condition_map.clear();
                                            condition_map.put(f2p_path, maped_path);
                                            String c_r_host = getSubling(remote_full_map, condition_map, f2p_loc);
                                            if (c_l_host != null && c_r_host != null && c_r_host.equals(c_l_host)) {
                                                summary_map.get(FILEPATH_CHANGED_PATH).put(c_hash, new HashMap<Integer, HashMap<String, String>>());
                                                HashMap<String, String> additionals_map = new HashMap<>();
                                                additionals_map.put(f2p_fid, files_id);
                                                String path_id = remote_all_hash2details_map.get(c_hash).get(0).get(f2p_id);
                                                additionals_map.put(f2p_id, path_id);
                                                additionals_map.put(f_id, files_id);
                                                summary_map.get(FILEPATH_CHANGED_PATH).get(c_hash).putAll(getAllSubling(local_full_map, f2p_path, local, additionals_map, null, keys_to_keep_map.get(FILEPATH_CHANGED_PATH)));
                                            } else if (old_host != null && old_host.equals(c_r_host)) {
                                                HashMap<String, String> additionals_map = new HashMap<>();
                                                additionals_map.put(f2p_fid, files_id);
                                                condition_map.put(f2p_loc, old_host);
                                                String path_id = getSubling(remote_full_map, condition_map, f2p_id);
                                                additionals_map.put(f2p_id, path_id);
                                                summary_map.get(FILEPATH_CHANGED_LOCATION).put(c_hash, new HashMap<Integer, HashMap<String, String>>());
                                                summary_map.get(FILEPATH_CHANGED_LOCATION).get(c_hash).putAll(getAllSubling(local_full_map, f2p_path, local, additionals_map, null, keys_to_keep_map.get(FILEPATH_CHANGED_LOCATION)));
                                            } else {
                                                HashMap<String, String> additionals_map = new HashMap<>();
                                                additionals_map.put(f2p_fid, files_id);
                                                summary_map.get(FILESPATH_NEW_PATH).put(c_hash, new HashMap<Integer, HashMap<String, String>>());
                                                summary_map.get(FILESPATH_NEW_PATH).get(c_hash).putAll(getAllSubling(local_full_map, f2p_path, local, additionals_map, null, keys_to_keep_map.get(FILESPATH_NEW_PATH)));

                                            }
                                        } else {
                                            HashMap<String, String> additionals_map = new HashMap<>();
                                            additionals_map.put(f2p_fid, files_id);
                                            summary_map.get(FILESPATH_NEW_PATH).put(c_hash, new HashMap<Integer, HashMap<String, String>>());
                                            summary_map.get(FILESPATH_NEW_PATH).get(c_hash).putAll(getAllSubling(local_full_map, f2p_path, local, additionals_map, null, keys_to_keep_map.get(FILESPATH_NEW_PATH)));
                                        }

                                    }
                                } else if (!remote_only_path_l.isEmpty()) {
                                    summary_map.get(FILEPATH_MISSING_PATH).put(c_hash, new HashMap<Integer, HashMap<String, String>>());
                                    summary_map.get(FILEPATH_MISSING_PATH).get(c_hash).putAll(getAllSubling_l(remote_full_map, f2p_path, remote_only_path_l, null, null, keys_to_keep_map.get(FILEPATH_MISSING_PATH)));//remote_full_map.get(remote_full_map_k_l.get(j)));
                                } else if (!local_only_path_l.isEmpty()) {
                                    HashMap<String, String> additionals_map = new HashMap<>();
                                    additionals_map.put(f2p_fid, files_id);
                                    summary_map.get(FILESPATH_NEW_PATH).put(c_hash, new HashMap<Integer, HashMap<String, String>>());
                                    summary_map.get(FILESPATH_NEW_PATH).get(c_hash).putAll(getAllSubling_l(local_full_map, f2p_path, local_only_path_l, additionals_map, null, keys_to_keep_map.get(FILESPATH_NEW_PATH)));
                                }
                            } else if (scores_a[hash_pos] == 0 && scores_a[path_pos] == 2 && scores_a[loc_pos] == 3) {//11    023
//                            REF:022
                            } else if (scores_a[hash_pos] == 0 && scores_a[path_pos] == 3 && scores_a[loc_pos] == 0) {//12     030
//                             REF:022
                                //TODO: check for path changes 
//                            String files_id = remote_all_hash2details_map.get(c_hash).get(0).get(f2p_fid);
//                            HashMap<String, String> additionals_map = new HashMap<>();
//                            additionals_map.put(f2p_id, files_id);
//                            ArrayList<String> tmp_local_l = new ArrayList<>();
//                            tmp_local_l.addAll(c_details_map.get(f2p_path));
//                            if (!tmp_local_l.isEmpty()) {
//                                new_filepaths_map.put(c_hash, new HashMap<Integer, HashMap<String, String>>());
//                                new_filepaths_map.get(c_hash).putAll(getAllSubling_l(local_full_map, f2p_path, tmp_local_l, additionals_map, null));  //, local_full_map.get(local_full_map_k_l.get(j)));
//                            }
//                            ArrayList<String> tmp_remote_l = new ArrayList<>();
//                            tmp_remote_l.addAll(c_details_map.get(r_f2p_path));
//                            if (!tmp_remote_l.isEmpty()) {
//                                locally_missing_path_map.put(c_hash, new HashMap<Integer, HashMap<String, String>>());
//                                locally_missing_path_map.get(c_hash).putAll(getAllSubling_l(remote_full_map, f2p_path, tmp_remote_l, additionals_map, null));//remote_full_map.get(remote_full_map_k_l.get(j)));
//                            }
                            } else if (scores_a[hash_pos] == 0 && scores_a[path_pos] == 3 && scores_a[loc_pos] == 1) {//13    031
                                ArrayList<String> remote_and_ocal_only_path_l = new ArrayList<>();
                                remote_and_ocal_only_path_l.addAll(c_details_map.get(f2p_path));
                                remote_and_ocal_only_path_l.retainAll(c_details_map.get(r_f2p_path));
                                if (!remote_and_ocal_only_path_l.isEmpty()) {
                                    for (int j = 0; j < remote_and_ocal_only_path_l.size(); j++) {
                                        String c_r_path = remote_and_ocal_only_path_l.get(j);
                                        HashMap<String, String> condition_map = new HashMap<>();
                                        condition_map.put(f2p_path, c_r_path);
                                        String c_r_host = getSubling(remote_full_map, condition_map, f2p_loc);
                                        if (old_host != null && old_host.equals(c_r_host)) {
                                            String files_id = remote_all_hash2details_map.get(c_hash).get(0).get(f2p_fid);
                                            HashMap<String, String> additionals_map = new HashMap<>();
                                            additionals_map.put(f_id, files_id);
                                            summary_map.get(FILE_CHANGED_CHECKSUM).put(c_hash, new HashMap<Integer, HashMap<String, String>>());
                                            summary_map.get(FILE_CHANGED_CHECKSUM).get(c_hash).putAll(getAllSubling(local_full_map, f2p_path, c_r_path, additionals_map, null, keys_to_keep_map.get(FILE_CHANGED_CHECKSUM)));
                                            condition_map.put(f2p_loc, old_host);
                                            String path_id = getSubling(remote_full_map, condition_map, f2p_id);
                                            additionals_map.put(f2p_id, path_id);
                                            summary_map.get(FILEPATH_CHANGED_LOCATION).put(c_hash, new HashMap<Integer, HashMap<String, String>>());
                                            summary_map.get(FILEPATH_CHANGED_LOCATION).get(c_hash).putAll(getAllSubling(local_full_map, f2p_path, c_r_path, additionals_map, null, keys_to_keep_map.get(FILEPATH_CHANGED_LOCATION)));
                                        }
                                    }
                                }
                            } else if (scores_a[hash_pos] == 0 && scores_a[path_pos] == 3 && scores_a[loc_pos] == 2) {//14     032
                                //REF:022
//                                ArrayList<String> remote_only_path_l = new ArrayList<>();
//                            remote_only_path_l.addAll(c_details_map.get(r_f2p_path));
//                            remote_only_path_l.removeAll(c_details_map.get(f2p_path));
//                            ArrayList<String> local_only_path_l = new ArrayList<>();
//                            local_only_path_l.addAll(c_details_map.get(f2p_path));
//                            local_only_path_l.removeAll(c_details_map.get(r_f2p_path));
//                            String files_id = remote_all_hash2details_map.get(c_hash).get(0).get(f2p_fid);
//                            if (!remote_only_path_l.isEmpty() && !local_only_path_l.isEmpty()) {
//                                for (int j = 0; j < local_only_path_l.size(); j++) {
//                                    String local = local_only_path_l.get(j);
//                                    String maped_path = null;
//                                    for (int k = 0; (k < remote_only_path_l.size() && maped_path == null); k++) {
//                                        if (Paths.get(local).getFileName().equals(Paths.get(remote_only_path_l.get(k)).getFileName())) {
//                                            maped_path = remote_only_path_l.get(k);
//                                        }
//                                    }
//                                    if (maped_path != null) {
//                                        HashMap<String, String> condition_map = new HashMap<>();
//                                        condition_map.put(f2p_path, local);
//                                        String c_l_host = getSubling(local_full_map, condition_map, f2p_loc);
//                                        condition_map.clear();
//                                        condition_map.put(f2p_path, maped_path);
//                                        String c_r_host = getSubling(remote_full_map, condition_map, f2p_loc);
//                                        if (c_l_host != null && c_r_host != null && c_r_host.equals(c_l_host)) {
//                                            filepaths_changed_map.put(c_hash, new HashMap<Integer, HashMap<String, String>>());
//                                            HashMap<String, String> additionals_map = new HashMap<>();
//                                            additionals_map.put(f2p_fid, files_id);
//                                            filepaths_changed_map.get(c_hash).putAll(getAllSubling(local_full_map, f2p_path, local, additionals_map, null));
//                                        } else if (old_host != null && old_host.equals(c_r_host)) {
//                                            HashMap<String, String> additionals_map = new HashMap<>();
//                                            additionals_map.put(f2p_fid, files_id);
//                                            condition_map.put(f2p_loc, old_host);
//                                            String path_id = getSubling(remote_full_map, condition_map, f2p_id);
//                                            additionals_map.put(f2p_id, path_id);
//                                            location_changed_map.put(c_hash, new HashMap<Integer, HashMap<String, String>>());
//                                            location_changed_map.get(c_hash).putAll(getAllSubling(local_full_map, f2p_path, local, additionals_map, null));
//                                        } else {
//                                            //report
//                                        }
//                                    } else {
//                                        HashMap<String, String> additionals_map = new HashMap<>();
//                                        additionals_map.put(f2p_fid, files_id);
//                                        new_filepaths_map.put(c_hash, new HashMap<Integer, HashMap<String, String>>());
//                                        new_filepaths_map.get(c_hash).putAll(getAllSubling(local_full_map, f2p_path, local, additionals_map, null));
//                                    }
//
//                                }
//                            } else if (!remote_only_path_l.isEmpty()) {
//                                locally_missing_path_map.put(c_hash, new HashMap<Integer, HashMap<String, String>>());
//                                locally_missing_path_map.get(c_hash).putAll(getAllSubling_l(remote_full_map, f2p_path, remote_only_path_l, null, null));//remote_full_map.get(remote_full_map_k_l.get(j)));
//                            } else if (!local_only_path_l.isEmpty()) {
//                                HashMap<String, String> additionals_map = new HashMap<>();
//                                additionals_map.put(f2p_fid, files_id);
//                                new_filepaths_map.put(c_hash, new HashMap<Integer, HashMap<String, String>>());
//                                new_filepaths_map.get(c_hash).putAll(getAllSubling_l(local_full_map, f2p_path, local_only_path_l, additionals_map, null));
//                            }
                            } else if (scores_a[hash_pos] == 0 && scores_a[path_pos] == 3 && scores_a[loc_pos] == 3) {//15    033
//                            REF:022
                            } else if ((scores_a[hash_pos] == 1 || scores_a[hash_pos] == 2) && scores_a[path_pos] == 0 && scores_a[loc_pos] == 0) {//16     100
                                //checksum changed, all paths and locations equal
                                ArrayList<String> remote_path_l = new ArrayList<>();
                                remote_path_l.addAll(c_details_map.get(f2p_path));
                                String files_id = remote_all_hash2details_map.get(c_hash).get(0).get(f2p_fid);
                                HashMap<String, String> additionals_map = new HashMap<>();
                                additionals_map.put(f_id, files_id);
                                summary_map.get(FILE_CHANGED_CHECKSUM).put(c_hash, new HashMap<Integer, HashMap<String, String>>());
                                summary_map.get(FILE_CHANGED_CHECKSUM).get(c_hash).putAll(getAllSubling_l(local_full_map, f2p_path, remote_path_l, additionals_map, null, keys_to_keep_map.get(FILE_CHANGED_CHECKSUM)));
                            } else if (scores_a[hash_pos] == 1 && scores_a[path_pos] == 0 && scores_a[loc_pos] == 1) {//17     101
                                //Checksum and location changed
                                ArrayList<String> remote_path_l = new ArrayList<>();
                                remote_path_l.addAll(c_details_map.get(f2p_path));
                                String files_id = remote_all_hash2details_map.get(c_hash).get(0).get(f2p_fid);
                                HashMap<String, String> additionals_map = new HashMap<>();
                                additionals_map.put(f_id, files_id);
                                summary_map.get(FILE_CHANGED_CHECKSUM).put(c_hash, new HashMap<Integer, HashMap<String, String>>());
                                summary_map.get(FILE_CHANGED_CHECKSUM).get(c_hash).putAll(getAllSubling_l(local_full_map, f2p_path, remote_path_l, additionals_map, null, keys_to_keep_map.get(FILE_CHANGED_CHECKSUM)));
                                //update location changes
                                for (int j = 0; j < remote_path_l.size(); j++) {
                                    String c_r_path = remote_path_l.get(j);
                                    HashMap<String, String> condition_map = new HashMap<>();
                                    condition_map.put(f2p_path, c_r_path);
                                    String c_l_host = getSubling(local_full_map, condition_map, f2p_loc);
                                    String c_r_host = getSubling(remote_full_map, condition_map, f2p_loc);
                                    if (c_l_host != null && c_r_host != null && c_r_host.equals(c_l_host)) {
                                        //nochnage 
                                    } else if (old_host != null && old_host.equals(c_r_host)) {
                                        condition_map.put(f2p_loc, old_host);
                                        String path_id = getSubling(remote_full_map, condition_map, f2p_id);
                                        additionals_map.put(f2p_id, path_id);
                                        summary_map.get(FILEPATH_CHANGED_LOCATION).put(c_hash, new HashMap<Integer, HashMap<String, String>>());
                                        summary_map.get(FILEPATH_CHANGED_LOCATION).get(c_hash).putAll(getAllSubling(local_full_map, f2p_path, c_r_path, additionals_map, null, keys_to_keep_map.get(FILEPATH_CHANGED_LOCATION)));
                                    } else {
                                        //report
                                    }
                                }
                            } else if (scores_a[hash_pos] == 1 && scores_a[path_pos] == 0 && (scores_a[loc_pos] == 2 || scores_a[loc_pos] == 3)) {//18    102
                                //checksum changed , some paths chaged 
                                ArrayList<String> remote_path_l = new ArrayList<>();
                                remote_path_l.addAll(c_details_map.get(f2p_path));
                                String files_id = remote_all_hash2details_map.get(c_hash).get(0).get(f2p_fid);
                                HashMap<String, String> additionals_map = new HashMap<>();
                                additionals_map.put(f_id, files_id);
                                summary_map.get(FILE_CHANGED_CHECKSUM).put(c_hash, new HashMap<Integer, HashMap<String, String>>());
                                summary_map.get(FILE_CHANGED_CHECKSUM).get(c_hash).putAll(getAllSubling_l(local_full_map,
                                        f2p_path, remote_path_l, additionals_map, null, keys_to_keep_map.get(FILE_CHANGED_CHECKSUM)));

                                for (int j = 0; j < remote_path_l.size(); j++) {
                                    String c_r_path = remote_path_l.get(j);
                                    HashMap<String, String> condition_map = new HashMap<>();
                                    condition_map.put(f2p_path, c_r_path);
                                    String c_l_host = getSubling(local_full_map, condition_map, f2p_loc);
                                    String c_r_host = getSubling(remote_full_map, condition_map, f2p_loc);
                                    if (c_l_host != null && c_r_host != null && c_r_host.equals(c_l_host)) {
                                        //nochnage 
                                    } else if (old_host != null && old_host.equals(c_r_host)) {
                                        condition_map.put(f2p_loc, old_host);
                                        String path_id = getSubling(remote_full_map, condition_map, f2p_id);
                                        additionals_map.put(f2p_id, path_id);
                                        summary_map.get(FILEPATH_CHANGED_LOCATION).put(c_hash, new HashMap<Integer, HashMap<String, String>>());
                                        summary_map.get(FILEPATH_CHANGED_LOCATION).get(c_hash).putAll(getAllSubling(local_full_map, f2p_path, c_r_path,
                                                additionals_map, null, keys_to_keep_map.get(FILEPATH_CHANGED_LOCATION)));
                                    } else {
                                        //report
                                    }
                                }
                            } else if (scores_a[hash_pos] == 1 && scores_a[path_pos] == 0 && scores_a[loc_pos] == 3) {//19    109
                                //REF: 102
                            } else if (scores_a[hash_pos] == 1 && scores_a[path_pos] == 1 && scores_a[loc_pos] == 0) {//20     110
                                error = 20;
                            } else if (scores_a[hash_pos] == 1 && scores_a[path_pos] == 1 && scores_a[loc_pos] == 1) {//21    111
                                ArrayList<String> local_only_path_l = new ArrayList<>();
                                local_only_path_l.addAll(c_details_map.get(f2p_path));
                                local_only_path_l.removeAll(c_details_map.get(r_f2p_path));
                                for (int k = 0; k < local_only_path_l.size(); k++) {
                                    HashMap<String, String> tmp_map = new HashMap<>();
                                    tmp_map.put(f2p_path, local_only_path_l.get(k));
                                    summary_map.get(FILEPATH_NOTCHECKED_PATH).get("NA").put(i, tmp_map);
                                }
                            } else if (scores_a[hash_pos] == 1 && scores_a[path_pos] == 1 && scores_a[loc_pos] == 2) {//22     112
                                error = 22;
                            } else if (scores_a[hash_pos] == 1 && scores_a[path_pos] == 1 && scores_a[loc_pos] == 3) {//23    113
                                error = 23;
                            } else if (scores_a[hash_pos] == 1 && scores_a[path_pos] == 2 && scores_a[loc_pos] == 0) {//24     120
                                //chekcsum chamged some paths equal, all hosts match
                                ArrayList<String> remote_and_ocal_only_path_l = new ArrayList<>();
                                remote_and_ocal_only_path_l.addAll(c_details_map.get(f2p_path));
                                remote_and_ocal_only_path_l.retainAll(c_details_map.get(r_f2p_path));
                                if (!remote_and_ocal_only_path_l.isEmpty()) {
                                    String files_id = remote_all_hash2details_map.get(c_hash).get(0).get(f2p_fid);
                                    HashMap<String, String> additionals_map = new HashMap<>();
                                    additionals_map.put(f_id, files_id);
                                    summary_map.get(FILE_CHANGED_CHECKSUM).put(c_hash, new HashMap<Integer, HashMap<String, String>>());
                                    summary_map.get(FILE_CHANGED_CHECKSUM).get(c_hash).putAll(getAllSubling_l(local_full_map, f2p_path,
                                            remote_and_ocal_only_path_l, additionals_map, null, keys_to_keep_map.get(FILE_CHANGED_CHECKSUM)));
                                }
                            } else if (scores_a[hash_pos] == 1 && scores_a[path_pos] == 2 && scores_a[loc_pos] == 1) {//25    121
                                ArrayList<String> remote_and_ocal_only_path_l = new ArrayList<>();
                                remote_and_ocal_only_path_l.addAll(c_details_map.get(f2p_path));
                                remote_and_ocal_only_path_l.retainAll(c_details_map.get(r_f2p_path));
                                if (!remote_and_ocal_only_path_l.isEmpty()) {
                                    for (int j = 0; j < remote_and_ocal_only_path_l.size(); j++) {
                                        String c_r_path = remote_and_ocal_only_path_l.get(j);
                                        HashMap<String, String> condition_map = new HashMap<>();
                                        condition_map.put(f2p_path, c_r_path);
                                        String c_r_host = getSubling(remote_full_map, condition_map, f2p_loc);
                                        if (old_host != null && old_host.equals(c_r_host)) {
                                            String files_id = remote_all_hash2details_map.get(c_hash).get(0).get(f2p_fid);
                                            HashMap<String, String> additionals_map = new HashMap<>();
                                            additionals_map.put(f_id, files_id);
                                            summary_map.get(FILE_CHANGED_CHECKSUM).put(c_hash, new HashMap<Integer, HashMap<String, String>>());
                                            summary_map.get(FILE_CHANGED_CHECKSUM).get(c_hash).putAll(getAllSubling(local_full_map, f2p_path, c_r_path, additionals_map, null, keys_to_keep_map.get(FILE_CHANGED_CHECKSUM)));
                                            condition_map.put(f2p_loc, old_host);
                                            String path_id = getSubling(remote_full_map, condition_map, f2p_id);
                                            additionals_map.put(f2p_id, path_id);
                                            summary_map.get(FILEPATH_CHANGED_LOCATION).put(c_hash, new HashMap<Integer, HashMap<String, String>>());
                                            summary_map.get(FILEPATH_CHANGED_LOCATION).get(c_hash).putAll(
                                                    getAllSubling(local_full_map, f2p_path, c_r_path, additionals_map, null, keys_to_keep_map.get(FILEPATH_CHANGED_LOCATION)));
                                        }
                                    }
                                }
                            } else if ((scores_a[hash_pos] == 1 || scores_a[hash_pos] == 3) && scores_a[path_pos] == 2 && (scores_a[loc_pos] == 2 || scores_a[loc_pos] == 3)) {//26    122
                                ArrayList<String> remote_and_ocal_only_path_l = new ArrayList<>();
                                remote_and_ocal_only_path_l.addAll(c_details_map.get(f2p_path));
                                remote_and_ocal_only_path_l.retainAll(c_details_map.get(r_f2p_path));
                                if (!remote_and_ocal_only_path_l.isEmpty()) {
                                    for (int j = 0; j < remote_and_ocal_only_path_l.size(); j++) {
                                        String c_r_path = remote_and_ocal_only_path_l.get(j);
                                        HashMap<String, String> condition_map = new HashMap<>();
                                        condition_map.put(f2p_path, c_r_path);
                                        String c_r_host = getSubling(remote_full_map, condition_map, f2p_loc);
                                        String c_l_host = getSubling(local_full_map, condition_map, f2p_loc);
                                        boolean change_chksum = false;
                                        if (c_r_host != null && c_l_host != null && c_r_host.equals(c_l_host)) {
                                            change_chksum = true;
                                        } else if (old_host != null && old_host.equals(c_r_host)) {
                                            change_chksum = true;
                                            String files_id = remote_all_hash2details_map.get(c_hash).get(0).get(f2p_fid);
                                            HashMap<String, String> additionals_map = new HashMap<>();
                                            additionals_map.put(f_id, files_id);
                                            summary_map.get(FILE_CHANGED_CHECKSUM).put(c_hash, new HashMap<Integer, HashMap<String, String>>());
                                            summary_map.get(FILE_CHANGED_CHECKSUM).get(c_hash).putAll(
                                                    getAllSubling(local_full_map, f2p_path, c_r_path, additionals_map, null, keys_to_keep_map.get(FILE_CHANGED_CHECKSUM)));
                                            condition_map.put(f2p_loc, old_host);
                                            String path_id = getSubling(remote_full_map, condition_map, f2p_id);
                                            additionals_map.put(f2p_id, path_id);
                                            summary_map.get(FILEPATH_CHANGED_LOCATION).put(c_hash, new HashMap<Integer, HashMap<String, String>>());
                                            summary_map.get(FILEPATH_CHANGED_LOCATION).get(c_hash).putAll(
                                                    getAllSubling(local_full_map, f2p_path, c_r_path, additionals_map, null, keys_to_keep_map.get(FILEPATH_CHANGED_LOCATION)));
                                        }
                                        if (change_chksum) {
                                            String files_id = remote_all_hash2details_map.get(c_hash).get(0).get(f2p_fid);
                                            HashMap<String, String> additionals_map = new HashMap<>();
                                            additionals_map.put(f_id, files_id);
                                            summary_map.get(FILE_CHANGED_CHECKSUM).put(c_hash, new HashMap<Integer, HashMap<String, String>>());
                                            summary_map.get(FILE_CHANGED_CHECKSUM).get(c_hash).putAll(
                                                    getAllSubling(local_full_map, f2p_path, c_r_path, additionals_map, null, keys_to_keep_map.get(FILE_CHANGED_CHECKSUM)));
                                        }
                                    }
                                }
                            } else if (scores_a[hash_pos] == 1 && scores_a[path_pos] == 2 && scores_a[loc_pos] == 3) {//27    123
                                //REF: 122
                            } else if (scores_a[hash_pos] == 1 && scores_a[path_pos] == 3 && scores_a[loc_pos] == 0) {//28     130
                                error = 28;
                            } else if (scores_a[hash_pos] == 1 && scores_a[path_pos] == 3 && scores_a[loc_pos] == 1) {//29    131
                                error = 29;
                            } else if (scores_a[hash_pos] == 1 && scores_a[path_pos] == 3 && scores_a[loc_pos] == 2) {//30    132
                                error = 30;
                            } else if (scores_a[hash_pos] == 1 && scores_a[path_pos] == 3 && scores_a[loc_pos] == 3) {//31    133
                                error = 31;
                            } else if (scores_a[hash_pos] == 2 && scores_a[path_pos] == 0 && scores_a[loc_pos] == 0) {//32    200
                                //REF:100
                            } else if (scores_a[hash_pos] == 2 && scores_a[path_pos] == 0 && scores_a[loc_pos] == 1) {//33    201
                                error = 33;
                            } else if (scores_a[hash_pos] == 2 && scores_a[path_pos] == 0 && scores_a[loc_pos] == 2) {//34    202
                                error = 34;
                            } else if (scores_a[hash_pos] == 2 && scores_a[path_pos] == 0 && scores_a[loc_pos] == 3) {//35    203
                            } else if (scores_a[hash_pos] == 2 && scores_a[path_pos] == 1 && scores_a[loc_pos] == 0) {//36    210
                                error = 36;
                            } else if (scores_a[hash_pos] == 2 && scores_a[path_pos] == 1 && scores_a[loc_pos] == 1) {//37    211
                                error = 37;
                            } else if (scores_a[hash_pos] == 2 && scores_a[path_pos] == 1 && scores_a[loc_pos] == 2) {//38    212
                                error = 38;
                            } else if (scores_a[hash_pos] == 2 && scores_a[path_pos] == 1 && scores_a[loc_pos] == 3) {//39    213
                            } else if (scores_a[hash_pos] == 2 && scores_a[path_pos] == 2 && scores_a[loc_pos] == 0) {//40    220
                                error = 40;
                            } else if (scores_a[hash_pos] == 2 && scores_a[path_pos] == 2 && scores_a[loc_pos] == 1) {//41    221
                                error = 41;
                            } else if (scores_a[hash_pos] == 2 && scores_a[path_pos] == 2 && scores_a[loc_pos] == 2) {//42    222
                                error = 42;
                            } else if (scores_a[hash_pos] == 2 && scores_a[path_pos] == 2 && scores_a[loc_pos] == 3) {//43    223
                                error = 43;
                            } else if (scores_a[hash_pos] == 2 && scores_a[path_pos] == 3 && scores_a[loc_pos] == 0) {//44    230
                                error = 44;
                            } else if (scores_a[hash_pos] == 2 && scores_a[path_pos] == 3 && scores_a[loc_pos] == 1) {//45    231
                                error = 45;
                            } else if (scores_a[hash_pos] == 2 && scores_a[path_pos] == 3 && scores_a[loc_pos] == 2) {//46    232
                                error = 46;
                            } else if (scores_a[hash_pos] == 2 && scores_a[path_pos] == 3 && scores_a[loc_pos] == 3) {//47    233
                                error = 47;
                            } else if (scores_a[hash_pos] == 3 && scores_a[path_pos] == 0 && scores_a[loc_pos] == 0) {//48    300
                                if (c_details_map.containsKey(r_f_chksm)) {
                                    ArrayList<String> r_chksm_l = c_details_map.get(r_f_chksm);
                                    for (int j = 0; j < r_chksm_l.size(); j++) {
                                        String c_r_chksm = r_chksm_l.get(j);
                                        String files_id = remote_all_hash2details_map.get(c_r_chksm).get(0).get(f2p_fid);
                                        HashMap<String, String> additionals_map = new HashMap<>();
                                        additionals_map.put(f_id, files_id);
                                        HashMap<String, String> condition_map = new HashMap<>();
                                        condition_map.put(f_chksm, c_hash);
                                        String c_path = getSubling(local_full_map, condition_map, f2p_path);
                                        summary_map.get(FILE_CHANGED_CHECKSUM).put(c_hash, new HashMap<Integer, HashMap<String, String>>());
                                        summary_map.get(FILE_CHANGED_CHECKSUM).get(c_hash).putAll(
                                                getAllSubling(local_full_map, f2p_path, c_path, additionals_map, null, keys_to_keep_map.get(FILE_CHANGED_CHECKSUM)));//                                
                                    }
                                } else {
                                    printout(0, null, "Error CMD1h: revers hash failed @300");
                                }
                            } else if (scores_a[hash_pos] == 3 && scores_a[path_pos] == 0 && scores_a[loc_pos] == 1) {//49    301
                                if (c_details_map.containsKey(r_f_chksm)) {
                                    ArrayList<String> r_chksm_l = c_details_map.get(r_f_chksm);
                                    for (int j = 0; j < r_chksm_l.size(); j++) {
                                        String c_r_chksm = r_chksm_l.get(j);
                                        String files_id = remote_all_hash2details_map.get(c_r_chksm).get(0).get(f2p_fid);
                                        HashMap<String, String> additionals_map = new HashMap<>();
                                        additionals_map.put(f_id, files_id);
                                        HashMap<String, String> condition_map = new HashMap<>();
                                        condition_map.put(f_chksm, c_hash);
                                        String c_path = getSubling(local_full_map, condition_map, f2p_path);
                                        summary_map.get(FILE_CHANGED_CHECKSUM).put(c_hash, new HashMap<Integer, HashMap<String, String>>());
                                        summary_map.get(FILE_CHANGED_CHECKSUM).get(c_hash).putAll(
                                                getAllSubling(local_full_map, f2p_path, c_path, additionals_map, null, keys_to_keep_map.get(FILE_CHANGED_CHECKSUM)));
                                        condition_map.clear();
                                        condition_map.put(f2p_path, c_path);
                                        String c_l_host = getSubling(local_full_map, condition_map, f2p_loc);
                                        String c_r_host = getSubling(remote_full_map, condition_map, f2p_loc);
                                        if (old_host != null && old_host.equals(c_r_host)) {
                                            condition_map.put(f2p_loc, old_host);
                                            String path_id = getSubling(remote_full_map, condition_map, f2p_id);
                                            additionals_map.put(f2p_id, path_id);
                                            summary_map.get(FILEPATH_CHANGED_LOCATION).put(c_hash, new HashMap<Integer, HashMap<String, String>>());
                                            summary_map.get(FILEPATH_CHANGED_LOCATION).get(c_hash).putAll(
                                                    getAllSubling(local_full_map, f2p_path, c_path, additionals_map, null, keys_to_keep_map.get(FILEPATH_CHANGED_LOCATION)));
                                        } else if (c_l_host != null && c_r_host != null && c_r_host.equals(c_l_host)) {
                                            //nochnage 
                                        } else {
                                            //nochnage 
                                        }
                                    }
                                } else {
                                    printout(0, null, "Error CMD1k: revers hash failed @300");
                                }
                            } else if (scores_a[hash_pos] == 3 && scores_a[path_pos] == 0 && scores_a[loc_pos] == 2) {//50    302
                                error = 50;
                            } else if (scores_a[hash_pos] == 3 && scores_a[path_pos] == 0 && scores_a[loc_pos] == 3) {//51    303
                                error = 51;
                            } else if (scores_a[hash_pos] == 3 && scores_a[path_pos] == 1 && scores_a[loc_pos] == 0) {//52    310
                                error = 52;
                            } else if (scores_a[hash_pos] == 3 && scores_a[path_pos] == 1 && scores_a[loc_pos] == 1) {//53    311
                                error = 53;
                            } else if (scores_a[hash_pos] == 3 && scores_a[path_pos] == 1 && scores_a[loc_pos] == 2) {//54    312
                                error = 54;
                            } else if (scores_a[hash_pos] == 3 && scores_a[path_pos] == 1 && scores_a[loc_pos] == 3) {//55    313
                                error = 55;
                            } else if (scores_a[hash_pos] == 3 && scores_a[path_pos] == 2 && scores_a[loc_pos] == 0) {//56    320
                                error = 56;
                            } else if (scores_a[hash_pos] == 3 && scores_a[path_pos] == 2 && scores_a[loc_pos] == 1) {//57    321
                                error = 57;
                            } else if (scores_a[hash_pos] == 3 && scores_a[path_pos] == 2 && scores_a[loc_pos] == 2) {//58    322
                                // REF:122
                            } else if (scores_a[hash_pos] == 3 && scores_a[path_pos] == 2 && scores_a[loc_pos] == 3) {//59    323
                                // REF:122
                            } else if (scores_a[hash_pos] == 3 && scores_a[path_pos] == 3 && scores_a[loc_pos] == 0) {//60    330
                                error = 60;
                            } else if (scores_a[hash_pos] == 3 && scores_a[path_pos] == 3 && scores_a[loc_pos] == 1) {//61    331
                                error = 61;
                            } else if (scores_a[hash_pos] == 3 && scores_a[path_pos] == 3 && scores_a[loc_pos] == 2) {//62    332
                                error = 62;
                            } else if (scores_a[hash_pos] == 3 && scores_a[path_pos] == 3 && scores_a[loc_pos] == 3) {//63    333
                                error = 63;
                            } else if (scores_a[hash_pos] == 0 && (scores_a[path_pos] == 2 || scores_a[path_pos] == 3) && scores_a[loc_pos] == 0) {//8      020      
                                //REF:022
                                //                            String files_id = remote_all_hash2details_map.get(c_hash).get(0).get(f2p_fid);
//                            HashMap<String, String> additionals_map = new HashMap<>();
//                            additionals_map.put(f2p_id, files_id);
//                            ArrayList<String> tmp_local_l = new ArrayList<>();
//                            tmp_local_l.addAll(c_details_map.get(f2p_path));
//                            tmp_local_l.removeAll(c_details_map.get(r_f2p_path));
//                            if (!tmp_local_l.isEmpty()) {
//                                new_filepaths_map.put(c_hash, new HashMap<Integer, HashMap<String, String>>());
//                                new_filepaths_map.get(c_hash).putAll(getAllSubling_l(local_full_map, f2p_path, tmp_local_l, additionals_map, null));  //, local_full_map.get(local_full_map_k_l.get(j)));
//                            }
//                            ArrayList<String> tmp_remote_l = new ArrayList<>();
//                            tmp_remote_l.addAll(c_details_map.get(r_f2p_path));
//                            tmp_remote_l.removeAll(c_details_map.get(f2p_path));
//                            if (!tmp_remote_l.isEmpty()) {
//                                locally_missing_path_map.put(c_hash, new HashMap<Integer, HashMap<String, String>>());
//                                locally_missing_path_map.get(c_hash).putAll(getAllSubling_l(remote_full_map, f2p_path, tmp_remote_l, additionals_map, null));//remote_full_map.get(remote_full_map_k_l.get(j)));
//                            }
                            } else {
                                printout(0, null, "Error CMD1m: undefined score:" + scores_a[hash_pos] + "\t" + scores_a[path_pos] + "\t" + scores_a[loc_pos] + "\t");
                            }
                        }
//                     printout(0,null, "\n5745 Location changed " + summary_map.get(FILEPATH_CHANGED_LOCATION));
//                     printout(0,null, "\n5771 new paths " + summary_map.get(FILESPATH_NEW_PATH));
//                     printout(0,null, "\n5790 filepaths to update " + summary_map.get(FILEPATH_CHANGED_PATH));
//                     printout(0,null, "\n5824 locally missing " + summary_map.get(FILEPATH_MISSING_PATH));
//                     printout(0,null, "\n5824 Not changed " + summary_map.get(FILEPATH_NO_CHANGE));
//                     printout(0,null, "\n5884 Modified after dipositing " + summary_map.get(FILE_CHANGED_CHECKSUM));
//                       all_evaluated_paths_l
                        for (int i = 0; i < local_avoid_l.size(); i++) {
                            HashMap<String, String> tmp_map = new HashMap<>();
                            tmp_map.put(f2p_path, local_avoid_l.get(i));
                            summary_map.get(FILEPATH_NOTCHECKED_PATH).get("NA").put(i, tmp_map);
                        }
                        if (recurse) {
                            ArrayList<String> locally_deleted_l = new ArrayList<>();
                            List allPaths_l = advancedQueryHandler("files2path.location=^" + curreent_host + "$&&files2path.filepath=^" + current_dir,
                                    "FILES2PATH.FILEPATH,FILES2PATH.ID", false, false, true, false, false, false, false, 0, true);
                            if (allPaths_l != null) {
                                for (int j = 0; j < allPaths_l.size(); j++) {
                                    if (allPaths_l.get(j) != null) {
                                        String[] c_det_a = allPaths_l.get(j).toString().split("\\|\\|");
                                        String c_path = null;
                                        String c_id = null;
                                        for (int k = 0; k < c_det_a.length; k++) {
                                            String[] c_sub_a = c_det_a[k].split("=");
                                            if (c_sub_a.length == 2) {
                                                c_sub_a[1] = c_sub_a[1].trim();
                                                if (c_sub_a[0].toUpperCase().equals(f2p_path)) {
                                                    c_path = c_sub_a[1];
                                                } else if (c_sub_a[0].toUpperCase().equals(f2p_id)) {
                                                    c_id = c_sub_a[1];
                                                }
                                            }
                                        }
                                        if (!all_evaluated_paths_l.contains(c_path)) {
                                            locally_deleted_l.add(c_path);
                                            HashMap<String, String> tmp_map = new HashMap<>();
                                            tmp_map.put(f2p_id, c_id);
                                            tmp_map.put(f2p_path, c_path);
                                            tmp_map.put(f2p_loc, curreent_host);
                                            summary_map.get(FILEPATH_REMOVED_FILEPATH).get("NA").put(j, tmp_map);
                                        }
                                    }
                                }
                            }
                        }
                        out_a = printDiff(summary_map, key_descript_map, output_dir);
                        if (error >= 0) {
                            printout(0, null, "5899 error=" + error);
                        }
                    }
                }
            }
        } catch (IOException ex) {
        } finally {
            try {
                if (fos != null) {
                    fos.close();
                }
            } catch (IOException ex) {
            }
        }
        return out_a;
    }

    private String getSubling(HashMap<Integer, HashMap<String, String>> full_map, HashMap<String, String> condition_map, String targ_nm) {
        String result = null;
        ArrayList<Integer> k_l = new ArrayList<>(full_map.keySet());
        for (int i = 0; (i < k_l.size() && result == null); i++) {
            if (full_map.get(k_l.get(i)).entrySet().containsAll(condition_map.entrySet())) {
                if (full_map.get(k_l.get(i)).containsKey(targ_nm)) {
                    result = full_map.get(k_l.get(i)).get(targ_nm);
                }
            }
        }
        return result;
    }

    private HashMap<Integer, HashMap<String, String>> getAllSubling(HashMap<Integer, HashMap<String, String>> full_map,
            String src_nm, String src_val, HashMap<String, String> additionals_map,
            HashMap<String, HashMap<String, String>> overide_map, ArrayList<String> key_l) {
        HashMap<Integer, HashMap<String, String>> result_map = new HashMap<>();
        ArrayList<Integer> fmk_l = new ArrayList<>(full_map.keySet());
        for (int i = 0; i < fmk_l.size(); i++) {
            if (full_map.get(fmk_l.get(i)).containsKey(src_nm) && full_map.get(fmk_l.get(i)).get(src_nm).equals(src_val)) {
                result_map.put(fmk_l.get(i), full_map.get(fmk_l.get(i)));
                if (additionals_map != null) {
                    result_map.get(fmk_l.get(i)).putAll(additionals_map);
                }
                if (overide_map != null) {
                    ArrayList<String> ord_kl = new ArrayList<>(overide_map.keySet());
                    for (int j = 0; j < ord_kl.size(); j++) {
                        if (result_map.get(fmk_l.get(i)).containsKey(ord_kl.get(j)) && overide_map.get(ord_kl.get(j)).containsKey(result_map.get(fmk_l.get(i)).get(ord_kl.get(j)))) { //&& result_map.get(fmk_l.get(i)).get(ord_kl.get(j))
                            result_map.get(fmk_l.get(i)).put(ord_kl.get(j), overide_map.get(ord_kl.get(j)).get(result_map.get(fmk_l.get(i)).get(ord_kl.get(j))));
                        }
                    }
                }
            }
            if (key_l != null && !key_l.isEmpty() && result_map.containsKey(fmk_l.get(i))) {
                ArrayList<String> remove_l = new ArrayList<>(result_map.get(fmk_l.get(i)).keySet());
                remove_l.removeAll(key_l);
                for (int j = 0; j < remove_l.size(); j++) {
                    result_map.get(fmk_l.get(i)).remove(remove_l.get(j));
                }
            }

        }
        return result_map;
    }

    private HashMap<Integer, HashMap<String, String>> getAllSubling_l(HashMap<Integer, HashMap<String, String>> full_map,
            String src_nm, ArrayList<String> src_val_l, HashMap<String, String> additionals_map,
            HashMap<String, HashMap<String, String>> overide_map, ArrayList<String> key_l) {
        HashMap<Integer, HashMap<String, String>> result_map = new HashMap<>();
        ArrayList<Integer> fmk_l = new ArrayList<>(full_map.keySet());
        for (int i = 0; (i < fmk_l.size() && result_map.isEmpty()); i++) {
            if (full_map.get(fmk_l.get(i)).containsKey(src_nm) && src_val_l.contains(full_map.get(fmk_l.get(i)).get(src_nm))) {
                result_map.put(fmk_l.get(i), full_map.get(fmk_l.get(i)));
                if (additionals_map != null) {
                    result_map.get(fmk_l.get(i)).putAll(additionals_map);
                }
                if (overide_map != null) {
                    ArrayList<String> ord_kl = new ArrayList<>(overide_map.keySet());
                    for (int j = 0; j < ord_kl.size(); j++) {
                        if (result_map.get(fmk_l.get(i)).containsKey(ord_kl.get(j)) && overide_map.get(ord_kl.get(j)).containsKey(result_map.get(fmk_l.get(i)).get(ord_kl.get(j)))) { //&& result_map.get(fmk_l.get(i)).get(ord_kl.get(j))
                            result_map.get(fmk_l.get(i)).put(ord_kl.get(j), overide_map.get(ord_kl.get(j)).get(result_map.get(fmk_l.get(i)).get(ord_kl.get(j))));
                        }
                    }
                }
            }
            if (key_l != null && !key_l.isEmpty() && result_map.containsKey(fmk_l.get(i))) {
                ArrayList<String> remove_l = new ArrayList<>(result_map.get(fmk_l.get(i)).keySet());
                remove_l.removeAll(key_l);
                for (int j = 0; j < remove_l.size(); j++) {
                    result_map.get(fmk_l.get(i)).remove(remove_l.get(j));
                }
            }

        }
        return result_map;
    }
    /*
     * MethosID=CMD2
     */

    private String[] printDiff(HashMap<String, HashMap<String, HashMap<Integer, HashMap<String, String>>>> summary_map,
            HashMap<String, String> key_descript_map, String output_dir) {
        String[] out_a = new String[1];
        ArrayList<String> key_l = new ArrayList<>(summary_map.keySet());
        StringBuilder output_build = new StringBuilder();
        printout(0, null, "");
        FileWriter fos = null;
        if (!output_dir.endsWith(File.separator)) {
            output_dir = output_dir + File.separator;
        }
        try {
            out_a = new String[key_l.size()];
            for (int i = 0; i < key_l.size(); i++) {
                String titles_key = key_l.get(i);
                StringBuilder c_output_build = new StringBuilder();
                StringBuilder current_build = new StringBuilder();
                ArrayList<String> hash_key_l = new ArrayList<>(summary_map.get(titles_key).keySet());
                int count = 0;
                for (int j = 0; j < hash_key_l.size(); j++) {
                    ArrayList<Integer> pos_key_l = new ArrayList<>(summary_map.get(titles_key).get(hash_key_l.get(j)).keySet());
                    if (!pos_key_l.isEmpty()) {
                        ArrayList<String> header_l = new ArrayList<>(summary_map.get(titles_key).get(hash_key_l.get(j)).get(pos_key_l.get(0)).keySet());
                        if (current_build.length() < 1) {
                            current_build.append("##");
                            for (int k = 0; k < header_l.size(); k++) {
                                current_build.append(header_l.get(k));
                                current_build.append("\t");
                            }
                            current_build.append("\n");
                        }
                        boolean details_missing = false;
                        for (int k = 0; k < pos_key_l.size(); k++) {
                            for (int l = 0; l < header_l.size(); l++) {
                                current_build.append(summary_map.get(titles_key).get(hash_key_l.get(j)).get(pos_key_l.get(k)).get(header_l.get(l)));
                                current_build.append("\t");
                            }
                            current_build.append("\n");
                            if (summary_map.get(titles_key).get(hash_key_l.get(j)).get(pos_key_l.get(k)).containsKey("FILES2PATH.FILEPATH")) {
                                c_output_build.append(summary_map.get(titles_key).get(hash_key_l.get(j)).get(pos_key_l.get(k)).get("FILES2PATH.FILEPATH"));
                                c_output_build.append("\n");
                            } else if (summary_map.get(titles_key).get(hash_key_l.get(j)).get(pos_key_l.get(k)).containsKey("FILES.NAME")) {
                                c_output_build.append(summary_map.get(titles_key).get(hash_key_l.get(j)).get(pos_key_l.get(k)).get("FILES.NAME"));
                                c_output_build.append("\n");
                            } else {
                                details_missing = true;
                            }
                            count++;
                        }
                        if (details_missing) {
                            c_output_build.append("#No details available");
                            c_output_build.append("\n");
                        }
                    }
                }
                if (current_build.length() > 0) {
                    printout(0, null, "#" + titles_key + ": " + key_descript_map.get(titles_key) + ". " + count + " files");
                    FileWriter c_writer = null;
                    try {
                        c_writer = new FileWriter(output_dir + titles_key + ".txt", false);
                        c_writer.append(current_build);
                        current_build.setLength(0);
                        c_writer.close();
                        out_a[i] = titles_key + "|" + output_dir + titles_key + ".txt";
                        printout(0, null, "Results written to " + output_dir + titles_key + ".txt\n");
                    } catch (IOException ex) {
                        printout(0, null, "Error CMD2b :" + ex.getMessage());
                    } finally {
                        try {
                            if (c_writer != null) {
                                c_writer.close();
                            }
                        } catch (IOException ex) {
                            printout(0, null, "Error CMD2b :" + ex.getMessage());
                        }
                    }
                }

                output_build.append("\n#__________________________________\n");
                String p_line = "#" + titles_key + ": " + key_descript_map.get(titles_key) + ". Number of files=" + count + "\n";
                output_build.append(p_line);
                output_build.append(c_output_build.toString());
                c_output_build.setLength(0);
            }
            fos = new FileWriter(output_dir + ".diff.txt", false);
            fos.append("#Summary of diff@" + Timing.getDateTime() + "\n");
            fos.append("#Refer the .diff_scores.txt for details of scoring\n");
            if (output_build.length() > 1) {
                fos.append(output_build);
            } else {
                fos.append("The location details are identical to what is recorded in the EGDMS (no changes ditected) ");
            }
            fos.close();
            printout(0, null, "Results written to " + output_dir + ".diff.txt");
        } catch (IOException ex) {
            printout(0, null, "Error CMD2b :" + ex.getMessage());
        } finally {
            try {
                if (fos != null) {
                    fos.close();
                }
            } catch (IOException ex) {
                printout(0, null, "Error CMD2a :" + ex.getMessage());
            }
        }
        return out_a;
    }

    private ArrayList<String> getFiles_no_recurse(String current_dir) {
        ArrayList<String> local_fileList = new ArrayList<>();
        DirectoryStream<Path> ds = null;
        try {
            ds = Files.newDirectoryStream(Paths.get(current_dir));
            Iterator<Path> path_l = ds.iterator();
            while (path_l.hasNext()) {
                Path c_file = path_l.next();
                if (!Files.isHidden(c_file) && Files.size(c_file) > 0 && (Files.isRegularFile(c_file) || Files.isSymbolicLink(c_file))) {
                    local_fileList.add(c_file.toRealPath().toString());
                }
            }
            ds.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            try {
                if (ds != null) {
                    ds.close();
                }
            } catch (IOException ex) {
            }
        }
        return local_fileList;
    }

    private boolean mapIntergirtyCheck(HashMap<String, ArrayList<String>> result_map) {
        int lenth = -1;
        boolean allok = true;
        ArrayList<ArrayList<String>> values_to_tes_l = new ArrayList<>(result_map.values());
        for (int i = 0; (i < values_to_tes_l.size() && allok); i++) {
            if (lenth < 0) {
                lenth = values_to_tes_l.get(i).size();
            } else if (values_to_tes_l.get(i).size() != lenth) {
                allok = false;
            }
        }
        return allok;
    }

    /*
     MethodID=6;
     */
    private ArrayList<String> getFiles_recursive(Path location, boolean skip_hidden, ArrayList<String> in_avoid_l) {
        ArrayList<String> files_to_use_l = new ArrayList<>();
        try {
            if (Files.isReadable(location)) {
                if (Files.isRegularFile(location, LinkOption.NOFOLLOW_LINKS)) {
                    files_to_use_l.add(location.toRealPath().toString());
                } else {
                    LoadRecursive pf = new LoadRecursive(skip_hidden);
                    Files.walkFileTree(location.toRealPath(), EnumSet.of(FileVisitOption.FOLLOW_LINKS), Integer.MAX_VALUE, pf);
                    ArrayList<Path> files_to_use_l_paths = pf.getOKlist();
                    in_avoid_l.addAll(pf.avoidOKlist_string());
                    if (files_to_use_l_paths.isEmpty()) {
                    } else {
                        for (int i = 0; i < files_to_use_l_paths.size(); i++) {
                            files_to_use_l.add(files_to_use_l_paths.get(i).toRealPath().toString());
                        }
                    }
                }

            } else {
                printout(0, null, "Error 6a: locaiotn=" + location + " not accessible");
            }
        } catch (IOException ex) {
            printout(0, null, "Error 6b: locaiotn=" + ex.getMessage());
            ex.printStackTrace();
        }
        return files_to_use_l;
    }
    /*
     MethodID=8;
     */

    private HashMap<String, ArrayList<File>> getAllinSubFolders(File location, ArrayList<String> in_avoid_l,
            HashMap<String, ArrayList<File>> files_l_map, boolean first_call, int level, int iteration, boolean usedefault) {
        if (first_call) {
            in_avoid_l.add("\\..*");
            in_avoid_l.add(".*~");
        }
        if (usedefault) {
            use_all = true;
        }
        files_analysed++;
        if (files_analysed % 50 == 0) {
            System.out.print(".");
            if (files_analysed % 1000 == 0) {
                printout(0, null, files_analysed + " files found");
            }
        }
        boolean directory = false;
//        long skip_larger_bites = skip_larger * 1048576;
        boolean use_only_this = false;
        if (location.isFile()) {
            try {
//                boolean skip_large_file = false;
//                if (skip_larger > 0) {
//                    long c_size = -1;
//                    String file_size = getFile_size(location.getCanonicalPath());
//                    if (file_size.matches("[0-9]+")) {
//                        c_size = new Long(file_size);
//                        if (c_size > skip_larger_bites) {
//                            skip_large_file = true;
//                        }
//                    }
//                }
                if (!shoudthisbeallowed(location.getName(), location.getCanonicalPath(), in_avoid_l)) {
                    if (files_l_map.containsKey("AVOID")) {
                        files_l_map.get("AVOID").add(location);
                    } else {
                        ArrayList<File> tmp_l = new ArrayList<>(1);
                        tmp_l.add(location);
                        files_l_map.put("AVOID", tmp_l);
                    }
                } else {
                    if (files_l_map.containsKey("OK")) {
                        files_l_map.get("OK").add(location);
                    } else {
                        ArrayList<File> tmp_l = new ArrayList<>(1);
                        tmp_l.add(location);
                        files_l_map.put("OK", tmp_l);
                    }
                }
//                    if (files_l_map.containsKey("OK")) {
//                        files_l_map.get("OK").add(location);
//                    } else {
//                        ArrayList<File> tmp_l = new ArrayList<File>(1);
//                        tmp_l.add(location);
//                        files_l_map.put("OK", tmp_l);
//                    }
//                } else {
//                    if (files_l_map.containsKey("AVOID")) {
//                        files_l_map.get("AVOID").add(location);
//                    } else {
//                        ArrayList<File> tmp_l = new ArrayList<File>(1);
//                        tmp_l.add(location);
//                        files_l_map.put("AVOID", tmp_l);
//                    }
//                }
            } catch (IOException ex) {
            }
        } else if (location.isDirectory() && !(location.getName().startsWith("."))) {
            directory = true;
            if (!use_all && !first_call && !do_not_use_any) {
                String ans = getuserInputSameLine("Sub directory found:'" + location.getAbsolutePath() + "', do you want to use all files in this sub directory ?", " Y (yes)| N (no)| all (use_all_subdirectories| no-all (skipp_all_subdirectories)| ");
                int ana_rsponse = analyseUserResponse(ans, false, false);
                if (ana_rsponse == 0) {
                    use_only_this = true;
                } else if (ana_rsponse == 1) {
                    use_only_this = false;
                } else if (ana_rsponse == 2) {
                    use_all = true;
                } else if (ana_rsponse == 3) {
                    do_not_use_any = true;
                }
            } else if (first_call) {
                use_all = false;
                do_not_use_any = false;
                use_only_this = true;
            }

            if (use_all || use_only_this) {
                File[] dir_cintent_a = location.listFiles();
                if (dir_cintent_a != null) {
                    ArrayList<File> directory_l = new ArrayList<>(1);
                    for (int i = 0; i < dir_cintent_a.length; i++) {
                        File c_file = dir_cintent_a[i];
                        if (c_file.isFile()) {
                            try {
//                                boolean skip_large_file = false;
//                                if (skip_larger > 0) {
//                                    long c_size = -1;
//                                    String file_size = getFile_size(c_file.getCanonicalPath());
//                                    if (file_size.matches("[0-9]+")) {
//                                        c_size = new Long(file_size);
//                                        if (c_size > skip_larger_bites) {
//                                            skip_large_file = true;
//                                        }
//                                    }
//                                }
                                if (shoudthisbeallowed(c_file.getName(), location.getCanonicalPath(), in_avoid_l)) {
                                    if (files_l_map.containsKey("OK")) {
                                        files_l_map.get("OK").add(c_file);
                                    } else {
                                        ArrayList<File> tmp_l = new ArrayList<File>(1);
                                        tmp_l.add(c_file);
                                        files_l_map.put("OK", tmp_l);
                                    }
                                } else {
                                    if (files_l_map.containsKey("AVOID")) {
                                        files_l_map.get("AVOID").add(c_file);
                                    } else {
                                        ArrayList<File> tmp_l = new ArrayList<File>(1);
                                        tmp_l.add(c_file);
                                        files_l_map.put("AVOID", tmp_l);
                                    }
                                }
                            } catch (IOException ex) {
                            }
                        } else if (c_file.isDirectory() && !do_not_use_any) {
                            directory_l.add(c_file);
                        }
                    }

                    if (use_all || use_only_this) {
                        level++;
                        for (int i = 0; i < directory_l.size(); i++) {
                            getAllinSubFolders(directory_l.get(i), in_avoid_l, files_l_map, false, level, iteration++, usedefault);
                        }
                    }
                } else {
                    if (files_l_map.containsKey("AVOID")) {
                        files_l_map.get("AVOID").add(location);
                    }
                }
            }
        } else {
            printout(0, null, "Not processing " + location.getAbsolutePath());
        }
        if (directory && level < 3) {
            if (files_l_map.containsKey("OK")) {
                printout(0, null, location + " (total:" + files_l_map.get("OK").size() + ") files or folders to be processed");
            }
        }
        return files_l_map;
    }

    /*
     MethodID 28
     */
    private HashMap<String, HashMap<String, String>> get_descriptions(String filename, String name_clm, ArrayList<String> override_l) {
        name_clm = name_clm.toUpperCase();
        HashMap<String, HashMap<String, String>> descript_map = new HashMap<>();
        File desct_file = new File(filename);
        if (desct_file.isFile() && desct_file.canRead()) {
            try {
                Scanner scan = new Scanner(desct_file, "ISO-8859-4");
                ArrayList<String> column_nms_l = new ArrayList<>(2);
                int nuofcolmns = -1;
                int line_no = 0;
                HashMap<String, String> c_map = new HashMap<>();
                while (scan.hasNext()) {
                    line_no++;
                    String line = scan.nextLine();
                    HashMap<String, String> new_c_map = new HashMap<>();
                    new_c_map.putAll(c_map);
                    if (line.startsWith("##")) {
                        line = line.replaceAll("#", "").replaceAll("\\*", "").trim();
                        String[] colnm_a = line.split("\t");
                        nuofcolmns = colnm_a.length;
                        for (int i = 0; i < colnm_a.length; i++) {
                            c_map.put(colnm_a[i].toUpperCase(), null);
                            column_nms_l.add(colnm_a[i].toUpperCase());
                        }
                    } else if (!line.startsWith("#") && !line.isEmpty()) {
                        String[] value_a = line.split("\t");
                        if (value_a.length == nuofcolmns && !c_map.isEmpty()) {
                            for (int i = 0; i < value_a.length; i++) {
                                if (new_c_map.containsKey(column_nms_l.get(i))) {
                                    new_c_map.put(column_nms_l.get(i), value_a[i]);
                                }
                            }
                        } else {
                            printout(0, null, "Error 28a: Error in file format, the value count does not match column count. file=" + filename + "|line=" + line_no);
                            System.exit(28);
                            scan.close();
                        }
                    }
                    if (name_clm != null && new_c_map.containsKey(name_clm)) {
                        descript_map.put(new_c_map.get(name_clm), new_c_map);
                    }

                    //override_l
                }
                scan.close();
            } catch (Exception ex) {
                ex.printStackTrace();
                printout(0, null, "Error 28b:" + ex.getMessage());
            }

        } else {
            printout(0, null, "Error 28c: Description files not found \"" + filename + "\". Please perform prepare before adding. (egenv -" + PREPARE_TO_ADD_FILES_FLAG + ")\n if the config files are placed in a diferent location sepcify this with the -configin instruction ");
            System.exit(25);
        }
        return descript_map;
    }

    private ArrayList<String> undo(String undosfilenm, boolean printlinenumber, boolean verbosemode) {
        ArrayList<String> result_l = new ArrayList<>();
        File file = new File(undosfilenm);
        if (file.isFile() && file.canRead()) {
            try {
                Scanner scan = new Scanner(file);
                String undolines = "";
                while (scan.hasNext()) {
                    String line = scan.nextLine().replaceAll("[\\s]", "").replaceAll("\\[", "").replaceAll("\\]", "");
                    if (!line.isEmpty()) {
                        if (undolines.isEmpty()) {
                            undolines = line;
                        } else {
                            undolines = undolines + "¤" + line;
                        }
                    }
                }
                if (!undolines.isEmpty()) {
                    List result = call_deleteRecords(undolines);
                    printResults(result, printlinenumber, verbosemode);
                } else {
                    printout(0, null, "No undo information was found in =" + undosfilenm);
                }
            } catch (FileNotFoundException ex) {
                printout(0, null, "Error: accessing undo point " + undosfilenm + ". " + ex.getMessage());
            }
        } else {
            printout(0, null, "Error: accessing undo point " + undosfilenm);
        }
        return result_l;
    }

    private void createUndoPoint(String undos, String out_dir) {
        if (undos != null && !undos.isEmpty()) {
            if (out_dir == null) {
                out_dir = "";
            } else if (!out_dir.endsWith(File.separator)) {
                out_dir = out_dir + File.separator;
            }
            try {
                FileWriter undo = new FileWriter(out_dir + "." + Timing.getDateTimeForFileName() + EGENVAR_UNDO_FILENM, false);
                undo.append(undos);
                undo.close();
                String[] split = undos.split("\\|");
//                if(split[0].toUpperCase().endsWith(".REPORT")){
//                   FileWriter marker = new FileWriter(out_dir + "." + Timing.getDateTimeForFileName() + EGENVAR_UNDO_FILENM, false);
//                undo.append(undos);
//                undo.close();
//                }
            } catch (IOException ex) {
                printout(0, null, "Error 7: creating undo point : " + ex.getMessage());
            }
        }
    }

//    private String getFull_name(String filenm) {
//        if (server_name == null) {
//            server_name = getServerName();
//            if (server_name == null) {
//                server_name = getServerName();
//            }
//        }
//        return "SCP:" + server_name + ":" + filenm;
//    }
    private String getFile_size(String filenm) {
        Path f = Paths.get(filenm);
        String size = "-1";
        if (!Files.notExists(f)) {
            try {
                size = Files.size(f) + "";
            } catch (IOException ex) {
            }
        }
        return size;
//        return runCommnad("stat", "-c", "%s", filenm);
    }

    private long getFile_size(Path f) {
        long size = -1;
        if (!Files.notExists(f)) {
            try {
                size = Files.size(f);
            } catch (IOException ex) {
            }
        }
        return size;
//        return runCommnad("stat", "-c", "%s", filenm);
    }

    private String getPWD() {
        String pwd = ".";
        Path currentRelativePath = Paths.get("");
        pwd = currentRelativePath.toAbsolutePath().toString();
        return pwd;
    }

    private String getParent() {
        return getPWD(true).getParent().getFileName().toString();
    }

    private Path getPWD(boolean path) {
        Path currentRelativePath = Paths.get("");
        return currentRelativePath.toAbsolutePath();

    }

//    /*
//     MethodID=15
//     */
//    private String runCommnad(String command, String file) {
//        String result = "";
//         printout(0,null, "3690 " + command + "  " + file + "|");
//        try {
//            Process output = null;
//            if (file != null) {
//                output = new ProcessBuilder(command, "\"" + file + "\"").start();
//            } else {
//                output = new ProcessBuilder(command).start();
//            }
//            Scanner scan = new Scanner(output.getInputStream());
//            while (scan.hasNext()) {
//                result = scan.nextLine();
//            }
//            scan.close();
//            output.destroy();
//            return result.replaceAll("\"", "");
//        } catch (IOException ex) {
//             printout(0,null, "Error 15a :" + ex.getMessage());
//        }
//        return null;
//    }
//    /*
//     MethodID=15
//     */
//
//    private String runCommnad(String... command_l) {
//        String result = "";
//        try {
//            ProcessBuilder p_build = new ProcessBuilder(command_l);
//            Process output = p_build.start();
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
//             printout(0,null, "Error 15a :" + ex.getMessage());
//            return null;
//        }
//    }
//    /*
//     MethodID=30
//     */
//
//    private String runCommnad_runtime(String command) {
//        String result = "";
//        try {
//
//            Runtime runtime = Runtime.getRuntime();
//            Process output = runtime.exec(command);
//
//            InputStream in = output.getInputStream();
//            Scanner scan = new Scanner(in);
//            while (scan.hasNext()) {
//                result = scan.nextLine();
//            }
//            try {
//                output.waitFor();
//            } catch (InterruptedException ex) {
//            }
//            scan.close();
//            in.close();
//            if (output.exitValue() == 0) {
//                output.destroy();
//                return result;
//            } else {
//                output.destroy();
//                return null;
//            }
//        } catch (IOException ex) {
//             printout(0,null, "Error GC2a :" + ex.getMessage());
//        }
//        return null;
//    }
    /*
     MethidID=20
     */
    private String getWSDL(boolean reload) {
        if (reload) {
            String authfile_nm = getPWD() + File.separatorChar + "." + System.getProperty("user.name") + "_" + authfilenm;
            if (!localfolderauth) {
                try {
//                String classpath = System.getProperty("java.class.path");
//                if (classpath != null && !classpath.isEmpty()) {
//                    File path_file = new File(classpath);
//                    authfile_nm = path_file.getAbsoluteFile().getParentFile().getCanonicalPath() + File.separatorChar + "." + System.getProperty("user.name") + "_" + authfilenm;
//                }
                    authfile_nm = getClassPath().toAbsolutePath().toString() + File.separatorChar + "." + System.getProperty("user.name") + "_" + authfilenm;
                } catch (Exception ex) {
                    printout(0, null, "Warning: " + ex.getMessage());
                }
            }
            File su_file = new File(authfile_nm);
            if (su_file.isFile() && su_file.canRead()) {
                try {
                    FileInputStream in = new FileInputStream(su_file);
                    Object auth_ob = null;
                    try {
                        ObjectInputStream obin = new ObjectInputStream(in);
                        auth_ob = obin.readObject();
                        obin.close();
                        if (auth_ob != null) {
                            String[] auth_a = (String[]) auth_ob;
                            if (auth_a.length >= 5) {
                                defualt_host_wsdl = auth_a[4].trim().replace("host_wsdl=", "");
                            }
                        }
                    } catch (StreamCorruptedException ex) {
                        printout(0, null, "Error 37: Security exception, the authentication file is corrupted or has been modified by third party");
                    } catch (ClassNotFoundException ex) {
                        printout(0, null, "Error 38: accessing authentication file");
                    }
                } catch (IOException ex) {
                    printout(0, null, "Error: " + ex.getMessage());
                }
            }

            if (defualt_host_wsdl != null) {
                Object[] ob_a = startProgress(4);
                try {
                    no.ntnu.medisin.egenvar.webservice.AuthenticateService_Service service = new no.ntnu.medisin.egenvar.webservice.AuthenticateService_Service();
                    no.ntnu.medisin.egenvar.webservice.AuthenticateService port = service.getAuthenticateServicePort();
                    BindingProvider bp = (BindingProvider) port;
                    bp.getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, defualt_host_wsdl);
                    if (!port.test()) {
                        defualt_host_wsdl = null;
                    }
                    stopProgress(ob_a);
                } catch (Exception ex) {
                    printout(0, null, "Error 25a:" + ex.getMessage());
//                p.cancel("EGM25");
                    stopProgress(ob_a);
                }
            }
//            if (test(defualt_host_wsdl, 23)) {
            return defualt_host_wsdl;
//            } else {
//                return null;
//            }
        } else {
            return defualt_host_wsdl;
        }
    }

    /*
     MethidID=21
     */
    private boolean authenticate(boolean reauthenticate, String wsdl_to_use) {
        if (opmode == 9) {
            reauthenticate = true;
            opmode = 0;
        }

        if (!authenticated || reauthenticate) {
            if (wsdl_to_use == null) {
                wsdl_to_use = getWSDL(true);
            }
            String system_username = System.getProperty("user.name");
            String authfile_nm = getClassPath().toAbsolutePath().toString() + File.separatorChar + "." + system_username + "_" + authfilenm;
            File su_file = new File(authfile_nm);

            if (system_username == null) {
                printout(0, null, "Error 35: Failed to retrieve system user.");
            } else if (system_username.equalsIgnoreCase("root")) {
                printout(0, null, "Error: 36 Security exception, this program should not be used as root");
            } else {
                if (su_file.isFile() && su_file.canRead()) {
                    if (reauthenticate) {
                        try {
                            Files.delete(Paths.get(su_file.getCanonicalPath()));
                            su_file = null;
                            authenticated = false;
                        } catch (IOException ex) {
                        }
                    } else {
                        try {
                            authfile_nm = su_file.getCanonicalPath();
                        } catch (Exception e) {
                        }
                        try {
                            FileInputStream in = new FileInputStream(su_file);
                            Object auth_ob = null;
                            try {
                                try {
                                    ObjectInputStream obin = new ObjectInputStream(in);
                                    auth_ob = obin.readObject();
                                    obin.close();
                                } catch (StreamCorruptedException ex) {
                                    printout(0, null, "Error 37: Security exception, the authentication file is corrupted or has been modified by third party");
                                } catch (ClassNotFoundException ex) {
                                    printout(0, null, "Error 38: accessing authentication file");
                                }
                                if (auth_ob != null) {
                                    String[] auth_a = (String[]) auth_ob;
                                    if (auth_a.length >= 5) {
                                        authentication_string = auth_a[0].replace(AUTH_FLAG, "").trim();
                                        user_email = auth_a[1].trim().replace("email=", "");
                                        String current_user = auth_a[2].trim().replace("username=", "");
                                        String existing_suth_loc = null;
                                        existing_suth_loc = auth_a[3].trim().replace("authfile_loc=", "");
                                        if (auth_a[4] != null) {
                                            wsdl_to_use = auth_a[4].trim().replace("host_wsdl=", "");
                                            if (authfile_nm == null || existing_suth_loc == null || authfile_nm.equals(existing_suth_loc)) {
                                                if (authentication_string != null && !authentication_string.isEmpty() && system_username.equals(current_user)) {
                                                    authenticated = true;
                                                }
                                            } else {
                                                printout(0, null, "Error 21a: (ismatch=" + authfile_nm.equals(existing_suth_loc) + " The authentication certificate may have been tampered with, moved or corrupted");
                                            }
                                        } else {
                                            authenticated = false;
                                        }
                                    } else {
                                        printout(0, null, "Error 21d: There was a problem with the authentication certificate. Please obtain a new one");
                                    }
                                }
                            } catch (IOException ex) {
                                printout(0, null, "Error: " + ex.getMessage());
                            }
                        } catch (FileNotFoundException ex) {
                            printout(0, null, "Error: " + ex.getMessage());
                        }
                    }
                }
                if (!authenticated) {
//                    Console cons = System.console();
//                    if (cons != null) {
                    boolean wsdl_ok = false;
//                    if (test(wsdl_to_use, 3)) {
//                        wsdl_ok = true;
//                    } else {
                    String new_wsdl = getCorrectWSDL(true);
                    if (new_wsdl == null) {
                        wsdl_ok = false;
                    } else {
                        wsdl_to_use = new_wsdl;
                        wsdl_ok = true;
                    }
//                    }
                    printout(0, null, "Is WSDL ok -- " + wsdl_ok + " (" + wsdl_to_use + ")");
                    if (wsdl_ok) {
                        defualt_host_wsdl = wsdl_to_use;
//                            System.out.print("Please enter user name for the eGenVar datamanagement system: ");
                        String username = getAuthentication_input("Please enter user name for the eGenVar datamanagement system", false);//cons.readLine();
                        printout(0, null, "");
                        if (username != null && !username.isEmpty() && !username.matches("[.]+@[.]+")) {
//                            System.out.print("Please enter password: ");
                            char[] pass = getAuthentication_input_hidden("Please enter password");//cons.readPassword();
                            printout(0, null, "");
                            String password = "";
                            for (int i = 0; i < pass.length; i++) {
                                password = password + pass[i];
                            }
                            if (password != null && !password.isEmpty()) {
                                String result = call_AuthenticateWebService(username, password);
                                authentication_string = getHash(result);
                                if (result != null && result.length() == 32) {
                                    FileOutputStream fos = null;
                                    user_email = username;
                                    printout(0, null, "");
                                    try {
                                        boolean location_ok = false;
                                        String expected_full_lock = null;
                                        try {
                                            File auth_test = new File(authfile_nm);
                                            expected_full_lock = auth_test.getCanonicalPath();
                                            location_ok = true;
                                        } catch (IOException ex) {
                                            printout(0, null, "Error 6489: Authentication maintannace error" + ex.getMessage());
                                        }
                                        if (location_ok) {
                                            //ToDo encript this 
                                            authenticated = true;
                                            String[] auth_a = new String[5];
                                            auth_a[0] = AUTH_FLAG + getHash(result);//+ getHash(result);
                                            auth_a[1] = "email=" + username;
                                            auth_a[2] = "username=" + system_username;
                                            if (expected_full_lock != null) {
                                                auth_a[3] = "authfile_loc=" + expected_full_lock;
                                            }

                                            auth_a[4] = "host_wsdl=" + wsdl_to_use;
                                            fos = new FileOutputStream(authfile_nm);
                                            ObjectOutputStream os = new ObjectOutputStream(fos);
                                            os.writeObject(auth_a);
                                            os.close();
                                        } else {
                                            printout(0, null, "Error 6490: An error has prevented the maintanance of authentication credentials. Please contact egenvar@gmail.com to report this");
                                        }

                                    } catch (IOException ex) {
                                        printout(0, null, "Error 21b: could not locate certificate directory " + ex.getMessage());
                                        authenticated = false;
                                    } finally {
                                        try {
                                            if (fos != null) {
                                                fos.close();
                                            }
                                        } catch (IOException ex) {
                                            printout(0, null, "Error 51" + ex.getMessage());
                                        }
                                    }
                                } else {
                                    printout(0, null, "Authentication failed against " + wsdl_to_use);
                                }
                            }
                        } else {
                            printout(0, null, "invalid user name. Usually the user name is the email you used during registration");
                        }
                    } else {
                        printout(0, null, "Error :Requires valid WSDL location. Make sure the server is running and run diagnostics (egenv -" + DIAGNOSTICS_FLAG + ")");
                    }
//                        cons.flush();
//                    } else {
//                        printout(0, null, "Error : Failled invoking interactive console.");
//                    }
                }
            }
            defualt_host_wsdl = wsdl_to_use;
        }

        return authenticated;
    }

    private Path getClassPath() {
        Path class_p = getPWD(true);
//        String system_username = System.getProperty("user.name");
//        String current_directory = getPWD();
//        String authfile_nm = current_directory + File.separatorChar + "." + system_username + "_" + authfilenm;
        if (!localfolderauth) {
            try {
                String classpath = System.getProperty("java.class.path");
                if (classpath != null && !classpath.isEmpty()) {
                    if (classpath.contains(":")) {
                        String[] split_a = classpath.split(":");
                        boolean found = false;
                        for (int i = 0; (i < split_a.length && !found); i++) {
                            Path c_path = Paths.get(split_a[i]);
                            if (c_path.getFileName().toString().equalsIgnoreCase("egenv.jar")) {
                                classpath = c_path.toString();
                                found = true;
                            }
                        }
                        if (!found) {
                            if (opmode != 1) {//R type
                                printout(0, null, "Locating Jar location failed..");
                            }
                            for (int i = 0; (i < split_a.length && !found); i++) {
                                Path c_path = Paths.get(split_a[i]);
                                if (Files.exists(c_path) && Files.isWritable(c_path)) {
                                    classpath = c_path.toString();
                                    found = true;
                                }
                            }
                            if (found) {
                                printout(0, null, "Using " + classpath + " to keep encripted credentials");
                            }
                        } else {
//                            printout(0, null, "Using the Jar location to keep encriptedauthentication credentials");
                        }
                    }
                    File path_file = new File(classpath);
                    class_p = path_file.getAbsoluteFile().getParentFile().toPath();
                }
            } catch (Exception ex) {
                printout(0, null, "Warning: " + ex.getMessage());
            }
        }
        return class_p;
    }

    private String getAuthentication_input(String msg, boolean samleine) {
        String result = null;
        try {
            Console cons = System.console();
            if (cons != null) {
                if (samleine) {
                    System.out.print(msg + ": ");
                } else {
                    System.out.println(msg + ": ");
                }

                result = cons.readLine();
                cons.flush();
            } else {
                printout(0, null, "Error : Failled invoking interactive console. Trying with option pane");
                result = JOptionPane.showInputDialog(msg);
            }
        } catch (Exception ex) {
            printout(0, null, "Error :" + ex.getMessage());
        }
        return result;
    }

    private char[] getAuthentication_input_hidden(String msg) {
        char[] pass = null;
        try {
            Console cons = System.console();
            if (cons != null) {
                System.out.println(msg + ": ");
                pass = cons.readPassword();
            } else {
                JPasswordField pf = new JPasswordField();
                JOptionPane.showConfirmDialog(null, pf, msg, JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
                pass = pf.getPassword();
            }
        } catch (Exception ex) {
            printout(0, null, "Error :" + ex.getMessage());
        }
        return pass;
    }

    private String getAuthentication_input(String msg, String[] option) {
        String result = null;
        try {
            Console cons = System.console();
            if (cons != null) {
                System.out.print(msg + "\n" + Arrays.deepToString(option) + " :");
                result = cons.readLine();
                cons.flush();
            } else {
                printout(0, null, "Error : Failled invoking interactive console. Trying with option pane");
                int int_result = JOptionPane.showOptionDialog(null, msg, "SELECT", JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, option, null);
                result = option[int_result];
                int bra_index = result.indexOf("-");
                if (bra_index > 0) {
                    result = result.substring(bra_index + 1);
                }
            }
        } catch (Exception ex) {
            printout(0, null, "Error :" + ex.getMessage());
        }
        return result;
    }
    /*
     MethodID=29
     */

    private String getuserInputSameLine(String message, String options) {
        Console cons = System.console();
        if (cons != null) {
            printout(0, null, "_______________________________________");
            if (options == null) {
                return getAuthentication_input(message + " ", true);
//                System.out.print(message + ": ");
            } else {
                return getAuthentication_input(message + "\n" + options + " ", true);
//                System.out.print(message + "\n" + options + ": ");
            }

//            cons.readLine();
        } else {
            printout(0, null, "Failed to invloke console. ");
            return JOptionPane.showInputDialog(message + " " + options);

        }
//        return null;
    }

    /*
     MethodID=30
     */
    private String getuserInput(String message, String options) {
        Console cons = System.console();
        if (cons != null) {
            printout(0, null, message + ":");
            return cons.readLine();
        } else {
            printout(0, null, "Failed to invloke console. ");
            System.exit(30);
        }
        return null;
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
                    printout(0, null, "Invalid choice, try again");
                }
            }
        }
        return result;
    }

    /*
     MethodID=10
     */
    private int analyseUserResponse(String response, boolean exitifNO, boolean freetext_allowed) {
        boolean ok = false;
        int safety = 7;
        int result = -1;
        while (!ok) {
            safety--;
            ok = true;
            if (safety > 1) {
                if (response == null || response.isEmpty()) {
                    ok = false;
                    response = getuserInputSameLine("Invalid choice, please try agin", "");
                } else if (response.equalsIgnoreCase("N")) {
                    if (exitifNO) {
                        printout(0, null, "Terminating all operations and exiting..");
                        System.exit(10);
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
                    response = getuserInputSameLine("Invalid choice, please try agin", "");
                }
            } else {
                printout(0, null, "Maximum attempts exceeded. Terminating all operations and exiting.");
                System.exit(10);
            }

        }

        return result;
    }

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
            printout(0, null, "Error 56 :" + ex.getMessage());
        }
        return null;
    }

    private String getHash(byte[] pass_bytes) {
        try {
            if (pass_bytes != null) {
                MessageDigest sha1 = MessageDigest.getInstance("SHA1");
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
            printout(0, null, "Error 56 :" + ex.getMessage());
        }
        return null;
    }

    private String getFiles(ArrayList<String> file_location_list, boolean verbosemode) {
        List file_location_detials = call_GetFileDetials4Download(file_location_list, verbosemode);
        if (file_location_detials != null && !file_location_detials.isEmpty()) {
            ArrayList<String> file_location_detials_l = new ArrayList(file_location_detials);
            HashMap<String, String> location2Hash_map = new HashMap<>();
            HashMap<String, String> location2directory_map = new HashMap<>();
            for (int i = 0; i < file_location_detials_l.size(); i++) {
                String c_details = file_location_detials_l.get(i);
                if (c_details != null && !c_details.isEmpty() && c_details.contains("|")) {
                    String[] c_details_a = c_details.split("\\|");
                    if (c_details_a.length >= 2) {
                        location2Hash_map.put(c_details_a[0].trim(), c_details_a[1].trim());
                    }
                    if (c_details.length() >= 4) {
                        location2directory_map.put(c_details_a[0].trim(), c_details_a[3].trim() + File.separatorChar + c_details_a[2] + File.separatorChar);
                    }
                }
            }
            DownloadManager download = new DownloadManager(location2Hash_map, location2directory_map);
            String result = download.start(".");
            if (result == null) {
                printout(0, null, "Dowload failed");
            } else {
                printout(0, null, "\n#Summary\n" + result + "\n#Summary_end\n");
            }
        } else {
            return "(X3) No match";
        }
        return null;
    }

    private void getFileDetials4download() {
    }

//    private void addRelationshipsFromFile(String source) {
//        if (source != null) {
//            String result = call_AddRelationshipFromFile(source);
//             printout(0,null, "result=" + result);
//        } else {
//            printValidArguments(false);
//        }
//    }
    /*
     MethodID=31
     */
    private void diagnostics(int attempts, String overide_wsdl) {
        printout(0, null, "Attempt = " + attempts);
        String wsdl_to_use = getWSDL(true);
        printout(0, null, "7280 " + wsdl_to_use);
        boolean fullyauthenticated = false;
        double latest_version = -1;
        if (attempts > 2) {
            printout(0, null, "Maximum allowed attempts exceeded ");
            System.exit(attempts);
        } else {
            try {
                printout(0, null, "Checking server availability");
//                Console cons = System.console();
//                if (cons == null) {
//                    printout(0, null, "Error : invoking console. Can not get user input. This terminal is not compatible with egenv");
//                    System.exit(1);
//                }
                boolean wsdl_ok = false;
                if (test(overide_wsdl, 4)) {
                    wsdl_ok = true;
                    latest_version = getLatestVersion(wsdl_to_use);
                } else {
                    String new_wsdl = getCorrectWSDL(true);
                    if (new_wsdl == null) {
                        wsdl_ok = false;
                    } else {
                        wsdl_to_use = new_wsdl;
                        wsdl_ok = true;
                    }
                }
                printout(0, null, "7307 " + wsdl_to_use);
                if (wsdl_ok) {
                    printout(0, null, "Location of webservice : " + wsdl_to_use);//                    
                    if (test(wsdl_to_use, 5)) {
                        printout(0, null, "Connected to server. Latest version available= " + latest_version + ". Your version=" + version);
                        System.out.print("Testing local authentication key .. ");
                        String system_username = System.getProperty("user.name");
                        printout(0, null, "System user = " + system_username);
                        if (system_username == null || system_username.isEmpty()) {
                            printout(0, null, "Failed to retrieve system user");
                            System.exit(attempts);
                        } else {
                            if (authenticate(true, wsdl_to_use)) {
                                printout(0, null, "Local authentication succsess");
                                printout(0, null, "Authenticating against the server..");
                                boolean new_authenticated = call_checkServerAuth(user_email);
                                if (new_authenticated) {
                                    printout(0, null, "Server authentication succsess");
                                    fullyauthenticated = true;
                                } else {
                                    printout(0, null, "Authenticating against the server faild");
                                    printout(0, null, "Resetting key");
                                    restServerAuthFile(system_username);
                                }
                            } else {
                                printout(0, null, "Local authentication failed, clearing local and server keys");
//                                if (cons == null) {
//                                    printout(0, null, "Error : invoking console. Can not get user input ");
//                                    System.exit(31);
//                                } else {
                                printout(0, null, "1 Resetting server key");
                                if (user_email == null) {
                                    printout(0, null, "Please enter username (usually the email) for the eGenVar datamanagement system: ");
                                    user_email = getAuthentication_input("Please enter username "
                                            + "usually the email) for the eGenVar datamanagement system", false);// cons.readLine();
                                }
                                if (user_email != null && !user_email.matches("[.]+@[.]+")) {
//                                        System.out.print("Please enter password: ");
                                    char[] pass = getAuthentication_input_hidden("Please enter password");// cons.readPassword();
                                    printout(0, null, "");
                                    String password = "";
                                    for (int i = 0; i < pass.length; i++) {
                                        password = password + pass[i];
                                    }
                                    if (password != null && !password.isEmpty()) {
                                        password = getHash(password);
                                        String reseted = call_resetServerAuth(user_email, password);
                                        printout(0, null, "7354 " + reseted);
                                        if (reseted.equals("1")) {
                                            printout(0, null, "Resetting was a success");
                                        } else {
                                            printout(0, null, "Reseting failed. Please informe this to the administrtor with Error 34 and return code=" + reseted);
                                        }
                                    } else {
                                        printout(0, null, "Invalid user name");
                                    }
                                } else {
                                    printout(0, null, "Invalid password");
                                }
//                                }
                                restServerAuthFile(system_username);
                            }
                        }
                    } else {
                        printout(0, null, "Obtaining version failed. Server unavialalbe. Please inform this to egenvar@gmail.com");
                    }
                } else {
                    printout(0, null, printformater_terminal(Helpmap.get("WSDL"), 80));
                    printout(0, null, "\n\nError :" + printformater_terminal("This tool requires the location of webservice to function. The location is provided as the URL of the WSDL file. If you are not the administrator of the egenvar server, please contact the administrator to get help. If you installed the server your self and could not find this URL at all, then re-run the installation and the WSDL URLl will be printed at the end of the installation.", 80));
                    printout(0, null, printformater_terminal("Also make sure the server is running before attemting this again, In the server manager, the instruction flag <A> and <D> would print the application server and database server status.", 80));
                }
            } catch (Exception ex) {
                printout(0, null, "Error 32:");
                StackTraceElement[] err = ex.getStackTrace();
                for (int i = 0; i < err.length; i++) {
                    printout(0, null, i + ") " + err[i].getMethodName() + " " + err[i].getLineNumber() + " " + err[i].toString());
                }
            }
        }
        if (!fullyauthenticated) {
            diagnostics(attempts + 1, wsdl_to_use);
        } else {
            printout(0, null, "Everything seems to be working now. If you still have problems please contact egenvar@gmail.com");
        }
    }

    /*
     MethodID=32
     */
    private String getCorrectWSDL(boolean wsdl_already_tested) {
        String new_wsdl = getWSDL(true);
        boolean use_new_WSDL = false;
//        Console cons = System.console();
//        if (cons == null) {
//            printout(0, null, "Error : invoking console. Can not get user input. This terminal is not compatible with egenv.\nUse the ");
//            System.exit(32);
//        } else {
        if (!wsdl_already_tested && test(null, 6)) {
            String ans = getuserInputSameLine("Tests OK for : " + new_wsdl + "\n Do you want to use this", "[Y -yes|N -no use a different one|C -cancel] ");
            if (analyseUserResponse(ans, false, true) == 0) {
                use_new_WSDL = true;
            } else {
                use_new_WSDL = false;
            }
        } else {
            use_new_WSDL = false;
        }
//        }
        boolean do_1 = true;
        boolean do_2 = true;
        boolean do_3 = true;
        if (!use_new_WSDL) {
            boolean loop1 = true;
            while (loop1) {
                boolean help = true;
                if (do_1) {
//                    Progress
//                    String ans = getuserInputSameLine("Do you have the URL for the webservice WSDL", "[Y -yes|N -no|C -cancel|H -I do not know what a WSDL is] ");
                    String ans = getAuthentication_input("Do you have the URL for the webservice WSDL", new String[]{"Yes-Y", "No-N", "Cancel-C", "Do not know what a WSDL is-I"});
                    if (analyseUserResponse(ans, false, false) == 0) {
                        ans = getAuthentication_input("Type or paste the WSDL URL", false);// getuserInputSameLine("Type or paste the WSDL URL: ", null).trim();
                        if (test(ans, 7)) {
                            use_new_WSDL = true;
                            new_wsdl = ans;
                            loop1 = false;
                            printout(0, null, "\nTesting WSDL " + use_new_WSDL + "  -- OK");
                        } else {
                            printout(0, null, "\nInvalid WSDL " + use_new_WSDL + ", please try again ");
                        }
                        help = false;
                    } else if (analyseUserResponse(ans, false, false) == -1) {
                        help = true;
                    } else if (analyseUserResponse(ans, false, false) == 4) {
                        printout(0, null, "Help mode");
                        printout(0, null, printformater_terminal(Helpmap.get("WSDL"), 80));
                        help = true;
                    } else if (analyseUserResponse(ans, false, false) == 5) {
                        printout(0, null, "Bye");
                        use_new_WSDL = false;
                        help = false;
                        loop1 = false;
                        System.exit(668);
                    }
                }
                if (help) {
                    do_1 = false;
                    String ans = getAuthentication_input("Do you know the address of the server where egenvar is installed ?", new String[]{"Yes-Y", "No-N", "Autodetect-A", "Cancel-C"});
                    //getuserInputSameLine("Do you know the address of the server where egenvar is installed ?.", "[Y -yes|N -no|A -autodetect|C -cancel]");
                    if (analyseUserResponse(ans, false, false) == 0) {
                        String test_server_name = getAuthentication_input("Type or paste the server name (e.g. server1.ntnu.no)", true);// getuserInputSameLine("Type or paste the server name (e.g. server1.ntnu.no) ?", null);
                        if (test_server_name.contains("//")) {
                            test_server_name = test_server_name.split("//")[1];
                        }
                        if (test_server_name.contains("/")) {
                            test_server_name = test_server_name.split("//")[0];
                        }
                        if (!test_server_name.contains(":")) {
                            String port = getAuthentication_input("Enter the port in " + test_server_name + ",\n"
                                    + "where egenvar webservice is available (e.g. 8085)"
                                    + " or enter 0 to test all recomended ports", true);
                            //getuserInputSameLine("Enter the port in " + test_server_name + ",\nwhere egenvar webservice is available (e.g. 8085) or enter 0 to test all recomended ports ? ", null);
                            if (port.matches("808[0-9]{1}")) {
                                new_wsdl = "http://" + test_server_name + ":" + port + "/eGenVar_WebService/Authenticate_service?wsdl";
                                if (test(new_wsdl, 8)) {
                                    use_new_WSDL = true;
                                    loop1 = false;
                                } else {
                                    printout(0, null, "The WSDL was invalid. (" + new_wsdl + ")");
                                }
                            } else if (port.equals("0")) {
                                new_wsdl = chechkAllPorts(test_server_name);
                                if (new_wsdl == null) {
                                    printout(0, null, "Error : auto detect WSDL failed");
                                    use_new_WSDL = false;
                                } else {
                                    use_new_WSDL = true;
                                    loop1 = false;
                                }
                            } else {
                                printout(0, null, "Invalid port , recomended port 8080 to 8089, if you are using non standard ports then exit this and entrer the full WSDL location");

                            }
                        } else {
                            new_wsdl = "http://" + test_server_name + "/eGenVar_WebService/Authenticate_service?wsdl";
                            if (test(new_wsdl, 9)) {
                                use_new_WSDL = true;
                                use_new_WSDL = true;
                                loop1 = false;
                            } else {
                                printout(0, null, "\nThe WSDL was invalid. (" + new_wsdl + ")");
                                use_new_WSDL = false;
                                loop1 = false;
                            }
                        }
                    } else if ((analyseUserResponse(ans, false, false) == -1) || (analyseUserResponse(ans, false, false) == 6)) {
                        new_wsdl = chechkAllPorts("localhost");
                        if (new_wsdl == null) {
                            printout(0, null, "Error : auto detect WSDL failed");
                            use_new_WSDL = false;
                            loop1 = false;
                        } else {
                            use_new_WSDL = true;
                            loop1 = false;
//                            use_new_WSDL = false;
//                            loop1 = false;
                        }
                    } else if (analyseUserResponse(ans, false, false) == 5) {
                        printout(0, null, "Bye");
                        use_new_WSDL = false;
                        loop1 = false;
                    }
                }
            }
        }
        if (use_new_WSDL) {
            return new_wsdl;
        } else {
            printout(0, null, "Error : failed to locate WSDL");
            return null;
        }

    }

    private String chechkAllPorts(String test_server_name) {
        printout(0, null, "Scanning ports from 8080 to 8089 for a valid egenvar WSDL");
        String new_wsdl = null;
        int starting_port = 8080;
        int port = 8085;
        boolean wsdl_ok = false;
        int i = 0;
        boolean use_wsdl = false;
        while (!wsdl_ok && i < 9) {
            new_wsdl = "http://" + test_server_name + ":" + port + "/eGenVar_WebService/Authenticate_service?wsdl";
            if (test(new_wsdl, 10)) {
                printout(0, null, new_wsdl + " --scan OK ");
                wsdl_ok = true;
                use_wsdl = true;
            }
            for (i = 0; ((i < 10) && !wsdl_ok); i++) {
                port = starting_port + i;
                new_wsdl = "http://" + test_server_name + ":" + port + "/eGenVar_WebService/Authenticate_service?wsdl";
                if (test(new_wsdl, 11)) {
                    printout(0, null, new_wsdl + " --scan OK ");
                    wsdl_ok = true;
                    use_wsdl = true;
                } else {
                    try {
                        printout(0, null, new_wsdl + " --scan failed ");
                        Thread.sleep(1000);
                    } catch (InterruptedException ex) {
                    }
                }
            }
        }
        if (use_wsdl) {
            return new_wsdl;
        } else {
            return null;
        }


    }

    private boolean restServerAuthFile(String system_username) {
        try {
            String authfile_nm = getClassPath().toAbsolutePath().toString() + File.separatorChar + "." + system_username + "_" + authfilenm;//"." + system_username + "_" + authfilenm;
//            String classpath = System.getProperty("java.class.path");
//            if (classpath != null && !classpath.isEmpty()) {
//                File path_file = new File(classpath);
//                authfile_nm = path_file.getParent() + File.separatorChar + authfile_nm;
//            }
            Path authFile = Paths.get(authfile_nm);
            Files.deleteIfExists(authFile);
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

//    private void subitError(String error, int controler) {
//         printout(0,null, "Creating error report .. please wait");
//        error = "(User=" + user_email + "|@" + Timing.getDateTime() + ") Erro=" + error;
//        call_errorReport(error, controler);
//    }
    private boolean call_checkServerAuth(String username) {
        try {
            if (username != null) {
                no.ntnu.medisin.egenvar.webservice.AuthenticateService_Service service = new no.ntnu.medisin.egenvar.webservice.AuthenticateService_Service();
                no.ntnu.medisin.egenvar.webservice.AuthenticateService port = service.getAuthenticateServicePort();
                BindingProvider bp = (BindingProvider) port;
                bp.getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, getWSDL(true));
                return port.checkAuthentication(username, authentication_string);

            }
        } catch (Exception ex) {
            printout(0, null, "The service is temporarily not available, please try again later");
        }

        return false;

    }

    private String call_resetServerAuth(String username, String password) {
        try {
            if (username != null) {
                no.ntnu.medisin.egenvar.webservice.AuthenticateService_Service service = new no.ntnu.medisin.egenvar.webservice.AuthenticateService_Service();
                no.ntnu.medisin.egenvar.webservice.AuthenticateService port = service.getAuthenticateServicePort();
                BindingProvider bp = (BindingProvider) port;
                bp.getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, getWSDL(true));
                return port.resetauthnticationkey(username, password);
            }
        } catch (Exception ex) {
            printout(0, null, "The service is temporarily not available, please try again later");
        }

        return "Error connecting to server";

    }

    /*
     * MethidId=2
     */
    private String call_AuthenticateWebService(String username, String password) {
        password = getHash(password);
        try {
            if (password != null) {
                no.ntnu.medisin.egenvar.webservice.AuthenticateService_Service service = new no.ntnu.medisin.egenvar.webservice.AuthenticateService_Service();
                no.ntnu.medisin.egenvar.webservice.AuthenticateService port = service.getAuthenticateServicePort();
                BindingProvider bp = (BindingProvider) port;
                bp.getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, getWSDL(true));
                return port.authenticate(username, password);
            }
        } catch (Exception ex) {
            printout(0, null, "Error 2a: Communication failier " + ex.getMessage());
            ex.printStackTrace();
        }
        return null;
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

//    private List call_listFromTable(String table, String field, String value, String selectval, String matchOP, boolean verbosemode) {
//        if (verbosemode) {
//             printout(0,null, "Connecting to the server and retrieving data. Started at " + Timing.getDateTime());
//        }
//        if (authentication_string != null && !authentication_string.isEmpty() && user_email != null && !user_email.isEmpty()) {
//            try {
//                if (table != null && field != null && value != null) {
//                    if (selectval == null || selectval.isEmpty()) {
//                        selectval = "*";
//                    }
//                    if (matchOP == null || matchOP.isEmpty()) {
//                        matchOP = " like ";
//                    }
//
//                    no.ntnu.medisin.egenvar.webservice.AuthenticateService_Service service = new no.ntnu.medisin.egenvar.webservice.AuthenticateService_Service();
//                     no.ntnu.medisin.egenvar.webservice.AuthenticateService port = service.getAuthenticateServicePort();                BindingProvider bp = (BindingProvider) port;                bp.getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, getWSDL());
//                    return port.listFromTable(user_email, authentication_string, table, field, value, selectval, matchOP);
//                }
//            } catch (Exception ex) {
//                 printout(0,null, "Error 30" + ex.getMessage());
//            }
//        }
//        return null;
//    }
//    private List call_listFromTableForQuery(String query, String value, boolean verbosemode) {
//        if (verbosemode) {
//             printout(0,null, "Connecting to the server and retrieving data. Started at " + Timing.getDateTime());
//        }
//        if (authentication_string != null && !authentication_string.isEmpty() && user_email != null && !user_email.isEmpty()) {
//            try {
//                if (query != null && query != null && value != null) {
//                    no.ntnu.medisin.egenvar.webservice.AuthenticateService_Service service = new no.ntnu.medisin.egenvar.webservice.AuthenticateService_Service();
//                    no.ntnu.medisin.egenvar.webservice.AuthenticateService port = service.getAuthenticateServicePort();
//                    BindingProvider bp = (BindingProvider) port;
//                    bp.getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, getWSDL());
//                    return port.listFromTableFrosubqury(user_email, authentication_string, query, value);
//
//                }
//            } catch (Exception ex) {
//                 printout(0,null, "Error 31" + ex.getMessage());
//            }
//        }
//
//
//        return null;
//
//    }
    private List call_GetFileDetials4Download(ArrayList file_locations_l, boolean verbosemode) {
        if (verbosemode) {
            printout(0, null, "Connecting to the server and retrieving data. Started at " + Timing.getDateTime());
        }
//        if (authentication_string != null && !authentication_string.isEmpty() && user_email != null && !user_email.isEmpty()) {
        if (authenticate(false, null)) {
            try {
                if (file_locations_l != null) {
                    no.ntnu.medisin.egenvar.webservice.AuthenticateService_Service service = new no.ntnu.medisin.egenvar.webservice.AuthenticateService_Service();
                    no.ntnu.medisin.egenvar.webservice.AuthenticateService port = service.getAuthenticateServicePort();
                    BindingProvider bp = (BindingProvider) port;
                    bp.getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, getWSDL(false));
                    List result = port.getFileDetials4Download(user_email, authentication_string, file_locations_l);
                    if (result.get(0) != null && result.get(0).toString().startsWith("Error ")) {
                        printout(0, null, "Error : " + result.get(0));
                    } else {
                        return result;
                    }
                }
            } catch (Exception ex) {
                printout(0, null, "Error 32" + ex.getMessage());
            }
        }
        return null;

    }

    private double getLatestVersion(String overide_WSDL) {
        double latestversion = -1;
        if (authenticate(false, overide_WSDL)) {
            try {
                no.ntnu.medisin.egenvar.webservice.AuthenticateService_Service service = new no.ntnu.medisin.egenvar.webservice.AuthenticateService_Service();
                no.ntnu.medisin.egenvar.webservice.AuthenticateService port = service.getAuthenticateServicePort();
                BindingProvider bp = (BindingProvider) port;
                if (overide_WSDL == null) {
                    bp.getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, getWSDL(false));
                } else {
                    bp.getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, overide_WSDL);
                }
                return port.getLatestVersion();
            } catch (Exception ex) {
            }
        }

        return latestversion;

    }

    private String getUpdateLink() {
        String new_link = null;
        try {
            no.ntnu.medisin.egenvar.webservice.AuthenticateService_Service service = new no.ntnu.medisin.egenvar.webservice.AuthenticateService_Service();
            no.ntnu.medisin.egenvar.webservice.AuthenticateService port = service.getAuthenticateServicePort();
            BindingProvider bp = (BindingProvider) port;
            bp.getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, getWSDL(false));
            return port.getUpdateLink();
        } catch (Exception ex) {
            printout(0, null, "Error 34 " + ex.getMessage());
        }
        return new_link;

    }

    private List call_deleteRecords(String undos) {
        try {
            no.ntnu.medisin.egenvar.webservice.AuthenticateService_Service service = new no.ntnu.medisin.egenvar.webservice.AuthenticateService_Service();
            no.ntnu.medisin.egenvar.webservice.AuthenticateService port = service.getAuthenticateServicePort();
            BindingProvider bp = (BindingProvider) port;
            bp.getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, getWSDL(false));
            return port.deleteRecord(user_email, authentication_string, undos);
        } catch (Exception ex) {
            printout(0, null, "Error 35 " + ex.getMessage());
        }
        return null;

    }


    /*
     MethodID=11
     * @filenm: the filled temlate with relationship information
     */
    private String prepare_AddRelationshipFromFile(String filenm, long tot_rows) {
        String result = null;
        Object[] ob_a = startProgress(5);
        File file = new File(filenm);
        ArrayList<String> entry_l = null;
        boolean add_parent = false;
        String type = null;
        String using_type = null;
        String tarnsfer_col_nm = null;
        if (file.isFile() && file.canRead()) {
            try {
                Scanner scan = new Scanner(file);
                entry_l = new ArrayList<>(10);
                int linecount = 0;
                long lastprog = 0;
                while (scan.hasNext()) {
                    linecount++;
                    long progr = ((linecount * 100) / tot_rows);
                    if (progr > lastprog) {
                        lastprog = progr;
                        if (progr % 5 == 0) {
                            System.out.print(".");
                        }
                    }
                    String line = scan.nextLine().trim();
                    if (!line.isEmpty() && line.startsWith("##")) {
                        line = line.replaceAll("#", "").trim();
                        if (!line.isEmpty()) {
                            String[] split = line.split("\t");
                            if (split[0].toUpperCase().startsWith("Child_")) {
                                add_parent = true;
                            }
                            for (int i = 2; i < split.length; i++) {
                                if (tarnsfer_col_nm == null) {
                                    tarnsfer_col_nm = split[i];
                                } else {
                                    tarnsfer_col_nm = tarnsfer_col_nm + "||" + split[i];
                                }

                            }
//                            if (split.length > 2) {
//                                tarnsfer_col_nm = split[2].trim();
//                            }
                        }
                    } else if (line.isEmpty() || line.startsWith("#")) {
                        if (line.contains(RELATIONSHIP_TYPE_FLAG)) {
                            line = line.replace(RELATIONSHIP_TYPE_FLAG, "").replace("#", "").trim();
                            if (!line.isEmpty()) {
                                type = line.substring(line.lastIndexOf(".") + 1);// .split("\\.")[line.split("\\.").length - 1];
                            }
                        } else if (line.contains(RELATIONSHIP_USIGN_FLAG)) {
                            //if not found use defaults
                            line = line.replace(RELATIONSHIP_USIGN_FLAG, "").replace("#", "").trim();
                            if (!line.isEmpty()) {
                                using_type = line;
                            }
                        }
                    } else if (!line.isEmpty()) {
                        if (line.indexOf("\t") > 0) {
                            entry_l.add(line.replace("\t", "||"));
                        } else {
                            printout(0, null, "Error 11a: invalid format. File:" + filenm + "| line:" + linecount + ". " + line);
                        }
                    }
                }
                scan.close();
                entry_l.trimToSize();
                stopProgress(ob_a);
                if (entry_l != null && !entry_l.isEmpty() && type != null && using_type != null) {
                    result = call_AddRelationshipFromFile(entry_l, type, using_type, tarnsfer_col_nm, add_parent);
                } else {
                    stopProgress(ob_a);
                    printout(0, null, "Error 145: Failed to extract enough information from " + filenm);
                }


            } catch (FileNotFoundException ex) {
                printout(0, null, "Error 11b " + ex.getMessage());
            }
        } else {
//            p.cancel("11");
            stopProgress(ob_a);
            return "Error 11c: Can not access file " + filenm;
        }
        return result;
    }
    /*
     MethodID=11
     */

    private String call_AddRelationshipFromFile(ArrayList<String> entry_l,
            String type, String using_type, String tarnsfer_col_nm, boolean add_parent) {
//        ProgressMonitor p = new ProgressMonitor(PROGRESS_MONT_INTERVAL, "EGM12");
//        Thread t = new Thread(p);
//        System.out.println("|8561 " + entry_l);
        Object[] ob_a = startProgress(6);
        String result = null;
        try {
            if (authenticate(false, null)) {
                if (entry_l != null && !entry_l.isEmpty() && type != null && using_type != null) {
                    no.ntnu.medisin.egenvar.webservice.AuthenticateService_Service service = new no.ntnu.medisin.egenvar.webservice.AuthenticateService_Service();
                    no.ntnu.medisin.egenvar.webservice.AuthenticateService port = service.getAuthenticateServicePort();
                    BindingProvider bp = (BindingProvider) port;
                    bp.getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, getWSDL(false));
                    result = port.addRelationships(user_email, authentication_string, entry_l, add_parent, type + "||" + using_type + "||" + tarnsfer_col_nm);
                    printout(0, null, "result=" + result);
                    stopProgress(ob_a);
                } else {
//                    if (t != null && t.isAlive()) {
//                        t.interrupt();
//                    }
//                    p.cancel("11");
                }
            }
        } catch (Exception ex) {
            result = "-5";
//            if (t != null && t.isAlive()) {
//                t.interrupt();
//            }
//            p.cancel("11");
            stopProgress(ob_a);
            printout(0, null, "Error 36 " + ex.getMessage());

        }
        stopProgress(ob_a);
        return result;

    }

    private int call_errorReport(String error, int controler) {
        int result = -3;
        try {
            if (authenticate(false, null)) {
                no.ntnu.medisin.egenvar.webservice.AuthenticateService_Service service = new no.ntnu.medisin.egenvar.webservice.AuthenticateService_Service();
                no.ntnu.medisin.egenvar.webservice.AuthenticateService port = service.getAuthenticateServicePort();
                BindingProvider bp = (BindingProvider) port;
                bp.getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, getWSDL(false));
                result = port.submitError(error, controler);
                if (result < 0) {
                    printout(0, null, "Error 170:Failed to send error report. Code=" + result);
                }
            }
        } catch (Exception ex) {
            printout(0, null, "Error 171:Failed to send error report " + ex.getMessage());

        }
        return result;

    }
    /*
     MethodId=36
     */

    private List getParametersForPrepare(String tablenm) {
        try {
//            ProgressMonitor p = new ProgressMonitor(PROGRESS_MONT_INTERVAL, "EGM36");
//            (new Thread(p)).start();
            Object[] ob_a = startProgress(7);
            if (authenticate(false, null)) {
                no.ntnu.medisin.egenvar.webservice.AuthenticateService_Service service = new no.ntnu.medisin.egenvar.webservice.AuthenticateService_Service();
                no.ntnu.medisin.egenvar.webservice.AuthenticateService port = service.getAuthenticateServicePort();
                BindingProvider bp = (BindingProvider) port;
                bp.getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, getWSDL(false));
                List result = port.getParametersForPrepare(tablenm, user_email, authentication_string);
//                p.cancel("EGM36");
                stopProgress(ob_a);
                return result;
            }
        } catch (Exception ex) {
            printout(0, null, "Error 172:Failed to send error report " + ex.getMessage());

        }

        return null;
    }

    /*
     MethodID=16
     */
    private List setUsingTemplate(ArrayList<String> param_list_l) {
//        System.out.println("8447 param_list_l="+param_list_l);
        List result = null;
//        ProgressMonitor p = new ProgressMonitor(PROGRESS_MONT_INTERVAL, "EGM16");
//        (new Thread(p)).start();
        Object[] ob_a = startProgress(8);
        if (authenticate(false, null)) {
            try {
                HashMap<String, String> out_map = new HashMap<>();
                no.ntnu.medisin.egenvar.webservice.AuthenticateService_Service service = new no.ntnu.medisin.egenvar.webservice.AuthenticateService_Service();
                no.ntnu.medisin.egenvar.webservice.AuthenticateService port = service.getAuthenticateServicePort();
                BindingProvider bp = (BindingProvider) port;
                bp.getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, getWSDL(false));
//                System.out.println("8485 " + param_list_l);
//                System.out.println("8530 "+user_email+"\t"+ authentication_string+"\t"+ param_list_l);
                result = port.add(authentication_string, user_email, param_list_l);//setUsingTemplate(user_email, authentication_string, param_list_l);
//                p.cancel("EGM16");
                stopProgress(ob_a);
                if ((result == null || result.isEmpty() || result.get(0) == null)) {
                    printout(0, null, "Error 16a: Communication failier");
                } else {
                    if (result.get(0).toString().startsWith("Error ")) {
                        printout(0, null, "Error 16b: " + result.get(0));
                        result = new ArrayList<>(0);
                    }
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                printout(0, null, "Error 16c:Failed to send error report " + ex.getMessage());
            }
        } else {
            printout(0, null, "Error 16d: Authentication failed. use -diagnose get more information");
        }
//        p.cancel("EGM16");
        stopProgress(ob_a);
        return result;
    }

//    private ArrayList<String> advancedQueryHandler_tableList(String source, String target, boolean excatonly, boolean restrictforuser,
//            boolean addcolumnnames, boolean exapandforeignKeys, boolean checkifexists, boolean printnoresultmsg) {
//        ArrayList<String> new_l = new ArrayList<>();
//        List tables_l = advancedQueryHandler(source, target, excatonly, restrictforuser, addcolumnnames, exapandforeignKeys, checkifexists, printnoresultmsg);
//        for (int i = 1; i < tables_l.size(); i++) {
//            if (tables_l.get(i) != null) {
//                String[] cval = tables_l.get(i).toString().split("\\|\\|");
//                new_l.add(cval[0]);
//            }
//        }        
//        return new_l;
//    }
    /*
     MethodID=19
     */
    private List advancedQueryHandler(String source, String target, boolean excatonly, boolean restrictforuser,
            boolean addcolumnnames, boolean exapandforeignKeys, boolean checkifexists,
            boolean printnoresultmsg, boolean keepurl, int querytype, boolean print_progress) {
        List result = null;
//         printout(0,null, "8141 "+source+" "+target+" ");
//        ProgressMonitor p = new ProgressMonitor(PROGRESS_MONT_INTERVAL, "EGM19");
        Object[] ob_a = null;
        if (print_progress) {
//            t = new Thread(p);
            ob_a = startProgress(9);
//            t.start();
        }

        if (authenticate(false, null)) {
            try {
                no.ntnu.medisin.egenvar.webservice.AuthenticateService_Service service = new no.ntnu.medisin.egenvar.webservice.AuthenticateService_Service();
                no.ntnu.medisin.egenvar.webservice.AuthenticateService port = service.getAuthenticateServicePort();
                BindingProvider bp = (BindingProvider) port;
                bp.getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, getWSDL(false));
                result = port.advancedQuaryHandler(authentication_string, user_email, source, target, excatonly,
                        restrictforuser, addcolumnnames, exapandforeignKeys, checkifexists, querytype);

//                p.cancel("EGM19");
                stopProgress(ob_a);
                if (result == null || result.isEmpty()) {
//                 printout(0,null, "No Results");
                } else {
                    Object meta = null;
                    if (keepurl) {
                        meta = result.get(0);
                    } else {
                        meta = result.remove(0);
                    }
                    if (meta != null) {
                        String meta_s = meta.toString().toUpperCase();
                        if (meta_s.startsWith("ERROR")) {
                            printout(0, null, "Error : " + meta);
                            result = null;
                        }
                        if (printnoresultmsg) {
                            if (meta_s.startsWith("NOMETA")) {
                            } else if (meta_s.startsWith("URL") && !meta_s.equals("URL=NA")) {
                                printout(0, null, "\nStatic URL:" + meta + "\n");
                            } else {
                                printout(0, null, meta);
                            }
                        }

                    }
                }
            } catch (Exception ex) {
                printout(0, null, "Error 19: " + ex.getMessage() + " use mode 9 or delete authentication file (xxx_egenvarauth.aut)to re-autneticate");
//                p.cancel("EGM19");
                stopProgress(ob_a);
            }
        } else {
            result = new ArrayList<>(1);
            result.add("Error: Authentication failed or server not available. use -diagnose to get more information");
            printout(0, null, "Error: Authentication failed. use -diagnose get more information");
//            p.cancel("EGM19");
            stopProgress(ob_a);
        }
//        p.cancel("EGM19");
        stopProgress(ob_a);
//        if (t != null && t.isAlive()) {
//            t.interrupt();
//        }
        return result;
    }

    /*
     MethodID=21
     */
    private boolean check_authentication() {
        boolean result = false;
//        ProgressMonitor p = new ProgressMonitor(PROGRESS_MONT_INTERVAL, "EGM21");
        Object[] ob_a = startProgress(10);
        try {
            no.ntnu.medisin.egenvar.webservice.AuthenticateService_Service service = new no.ntnu.medisin.egenvar.webservice.AuthenticateService_Service();
            no.ntnu.medisin.egenvar.webservice.AuthenticateService port = service.getAuthenticateServicePort();
            BindingProvider bp = (BindingProvider) port;
            bp.getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, getWSDL(false));
            result = port.checkAuthentication(user_email, authentication_string);
//            p.cancel("EGM31");
            stopProgress(ob_a);

        } catch (Exception ex) {
            printout(0, null, "Error 21:Failed to send error report " + ex.getMessage());

        }
//        p.cancel("EGM21");
        stopProgress(ob_a);
        return result;
    }

    /*
     MethodID=22
     */
    private String getErros() {
        String result = "NA";
        Object[] ob_a = startProgress(11);
//        ProgressMonitor p = new ProgressMonitor(PROGRESS_MONT_INTERVAL, "EGM22");
//        (new Thread(p)).start();
        if (authenticate(false, null)) {
            try {
                no.ntnu.medisin.egenvar.webservice.AuthenticateService_Service service = new no.ntnu.medisin.egenvar.webservice.AuthenticateService_Service();
                no.ntnu.medisin.egenvar.webservice.AuthenticateService port = service.getAuthenticateServicePort();
                BindingProvider bp = (BindingProvider) port;
                bp.getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, getWSDL(false));
                result = port.getErrors("");
//                p.cancel("EGM22");
                stopProgress(ob_a);
                if (result == null || result.isEmpty()) {
                    return result;
                } else {
                    return result;
                }
            } catch (Exception ex) {
                printout(0, null, "Error 22:Failed to send error report " + ex.getMessage());
//                p.cancel("EGM22");
                stopProgress(ob_a);

            }
        }

//        p.cancel("EGM22");
        stopProgress(ob_a);
        return result;
    }

    private ArrayList<String> getFilesFromFile(String infilenm, boolean verbose) {
        ArrayList<String> files_nm_l = new ArrayList<String>(10);
        File file = new File(infilenm);
        if (file.isFile() && file.canRead()) {
            try {
                Scanner scan = new Scanner(file, "ISO-8859-4");
                while (scan.hasNext()) {
                    String line = scan.nextLine().trim();
                    if (!line.isEmpty() && !line.startsWith("#")) {
                        File tmp = new File(line);
                        if (tmp.isFile() && tmp.canRead()) {
                            files_nm_l.add(line);
                        } else {
                            printout(0, null, "Skipping " + line + " as file verification failed");
                        }

                    }
                }
            } catch (FileNotFoundException ex) {
                printout(0, null, "Error accessing user file " + infilenm);
            }
        }

        return files_nm_l;
    }

    private void prepareTemplateRelationships(String table, String content, String output_dir, String etc_heading, boolean rearrange_content) {
        if (table == null) {
            List tables_l = advancedQueryHandler("tablename2feature.table_name=hierarchy", "tablename2feature.distinct table_name",
                    false, false, false, false, false, false, false, 0, true);
            if (tables_l != null) {
                table = getUserSelection(new ArrayList<String>(tables_l), null, "What type of relationship is this ?");
            }
        }
        if (table != null) {
            String source_table = table.toUpperCase().replace(HIERARCHY_FLAG, "");
            List result = getUniqueKeys(source_table);
            if (result != null) {
                for (int i = 0; (i < result.size() && i >= 0); i++) {
                    if (result.get(i).toString().toUpperCase().endsWith("_ID")) {
                        result.remove(i);
                        i--;
                    }
                }
                if (result.isEmpty()) {
                    result.add("name");
                }
                String relationship_using = source_table + "." + result.get(0);
                String filenm = PREPARE_TO_ADD_RELATIIONSHIPS_TABLE_FILENM.replace("<TABLE>", table);
                if (output_dir != null) {
                    if (!output_dir.endsWith(File.separator)) {
                        output_dir = output_dir + File.separatorChar;
                    }
                    filenm = output_dir + filenm;
                }
                StringBuilder heading_b = new StringBuilder();
                heading_b.append("#" + RELATIONSHIP_TYPE_FLAG + source_table + "\n");
                heading_b.append("#" + RELATIONSHIP_USIGN_FLAG + relationship_using + "\n");
                if (rearrange_content) {
                    heading_b.append("#There are three columns seperated by a single tab character each\n");
                } else {
                    heading_b.append("#There are two columns seperated by a single tab character each\n");
                }

                heading_b.append("#The first column is the parent and and the second column is the child.\n");
                heading_b.append("#If there are many children for a parent, the parent will be repeated many times in the first column\n");
                heading_b.append("#Please try to edit using plain text editors (UTF-8), If edited using word processors or as a ");
                heading_b.append("#spreadsheet (e.g. Excel) remember to save it as plain text\n");
                heading_b.append("#The names used should exactly match what is in the database, wild cards are not allowed\n");
                heading_b.append("#For file relationships the complete files name with the format  SCP:<server_name><canonical_location> should be used\n");
                if (rearrange_content) {
                    heading_b.append("#The third column is the content of the current parent (if confirmed to be the owner during execution),\n");
                    heading_b.append("#that will be distributed to the child\n");
                }
                heading_b.append("#_________________________________________________________\n");
                if (rearrange_content) {
                    heading_b.append("##Parent_" + relationship_using + "\tChild_" + relationship_using + "\tFILES.NAME" + etc_heading);
                } else {
                    heading_b.append("##Parent_" + relationship_using + "\tChild_" + relationship_using + etc_heading);
                }



                File existing_f = new File(filenm);
                boolean makeit = false;
                if (existing_f.isFile()) {
                    String ans = getuserInputSameLine("The file " + filenm + " already exists, do you want to ovewrite it?", " C- cancel and quit |Y-create file(the content of existing file will be lost)");
                    if (ans.equalsIgnoreCase("Y")) {
                        try {
                            String backup_name = existing_f.getName() + "~";
                            File bakup_file = new File(backup_name);
                            existing_f.renameTo(bakup_file);
                        } catch (Exception ex) {
                        }
                        makeit = true;
                    }
                } else {
                    makeit = true;
                }
                if (makeit) {
                    wirteToDescriptFile(filenm, heading_b.toString(), content, true, new ArrayList<String>(1), 1);
                } else {
                    printout(0, null, "File creation intrupted by user");
                }
            } else {
                printout(0, null, "Error : geting unique columns from database");
            }


        } else {
            printout(0, null, "Error:" + table + ". Invalid type");
        }
    }

    private void prepareTemplateRelationships_withTransfer(String table, String content, String output_dir) {
        if (table == null) {
            List tables_l = advancedQueryHandler("tablename2feature.table_name=hierarchy", "tablename2feature.distinct table_name",
                    false, false, false, false, false, false, false, 0, true);
            if (tables_l != null) {
//                ArrayList<String> refined_l = new ArrayList<>();
//                for (int i = 0; i < tables_l.size(); i++) {
//                    refined_l.add(tables_l.get(i).toString().replace(HIERARCHY_FLAG, ""));
//                }
                table = getUserSelection(new ArrayList<String>(tables_l), null, "What type of relationship is this ?");
            }
        }
        if (table != null) {
            String source_table = table.toUpperCase().replace(HIERARCHY_FLAG, "");
            List result = getUniqueKeys(source_table);
            if (result != null) {
                for (int i = 0; (i < result.size() && i >= 0); i++) {
                    if (result.get(i).toString().toUpperCase().endsWith("_ID")) {
                        result.remove(i);
                        i--;
                    }
                }
                if (result.isEmpty()) {
                    result.add("name");
                }
                String relationship_using = source_table + "." + result.get(0);
                String filenm = PREPARE_TO_ADD_RELATIIONSHIPS_TABLE_FILENM.replace("<TABLE>", table);
                if (output_dir != null) {
                    if (!output_dir.endsWith(File.separator)) {
                        output_dir = output_dir + File.separatorChar;
                        filenm = output_dir + filenm;
                    }
                }
                String heading = "#" + RELATIONSHIP_TYPE_FLAG + source_table + "\n"
                        + "#" + RELATIONSHIP_USIGN_FLAG + relationship_using + "\n"
                        + "#There are two columns seperated by a single tab character\n"
                        + "#The first column is the parent and and the second column is the child.\n"
                        + "#If there are many children for a parent, the parent will be repeated many times in the first column\n"
                        + "#Please try to edit using plain text editors (UTF-8), If edited using word processors or as a "
                        + "#spreadsheet (e.g. Excel) remember to save it as plain text\n"
                        + "#The names used should exactly match what is in the database, wild cards are not allowed\n"
                        + "#For file relationships the complete files name with the format  SCP:<server_name><canonical_location> should be used\n"
                        + "#_________________________________________________________\n"
                        + "##Parent_" + relationship_using + "\tChild_" + relationship_using;


                File existing_f = new File(filenm);
                boolean makeit = false;
                if (existing_f.isFile()) {
                    String ans = getuserInputSameLine("The file " + filenm + " already exists, do you want to ovewrite it?", " C- cancel and quit |Y-create file(the content of existing file will be lost)");
                    if (ans.equalsIgnoreCase("Y")) {
                        try {
                            String backup_name = existing_f.getName() + "~";
                            File bakup_file = new File(backup_name);
                            existing_f.renameTo(bakup_file);
                        } catch (Exception ex) {
                        }
                        makeit = true;
                    }
                } else {
                    makeit = true;
                }
                if (makeit) {
                    wirteToDescriptFile(filenm, heading, content, true, new ArrayList<String>(1), 1);
                } else {
                    printout(0, null, "File creation intrupted by user");
                }
            } else {
                printout(0, null, "Error : geting unique columns from database");
            }


        } else {
            printout(0, null, "Error:" + table + ". Invalid type");
        }
    }

    /*
     MethodID=33
     */
    private void createTag(String dir_for_placing_prep_files) {
        List tables = advancedQueryHandler("tablename2feature.table_name=tagsource", "tablename2feature.distinct table_name",
                false, false, false, false, false, false, false, 0, true);
        boolean tag_created = false;
        if (tables != null) {
            ArrayList<String> tables_l = new ArrayList<>(tables);
            String tag_table = getUserSelection(tables_l, null, "What type of tag do you want to create?");
            List tmp_from_db_list = advancedQueryHandler(tag_table + ".parent_id.min", tag_table + ".parent_id," + tag_table + ".hash",
                    false, false, false, false, false, false, false, 0, true);

            if (tmp_from_db_list == null || tmp_from_db_list.isEmpty()) {
                printout(0, null, "Error: failed to get the starting parent_id");
            } else {
                boolean complete = false;
//                String[] ts_a = tmp_from_db_list.get(0).toString().split("\\|\\|");
                String starting_parent_id = tmp_from_db_list.get(0).toString();
                String last_link2feature = tag_table + "=" + starting_parent_id;
                String path = tag_table;
                String selected_name = "NA";
                while (!complete) {
                    HashMap<String, String> result_map = new HashMap<>();
                    HashMap<String, String> id2hash_map = new HashMap<>();
                    if (starting_parent_id.equalsIgnoreCase("x")) {
                        complete = true;
                    } else if (starting_parent_id.matches("[0-9]+")) {
                        List round_1_list = advancedQueryHandler(tag_table + ".parent_id=" + starting_parent_id, tag_table + ".id," + tag_table + ".name," + tag_table + ".hash",
                                true, false, true, false, false, false, false, 0, true);
                        if (round_1_list == null || (round_1_list.size() == 1 && round_1_list.get(0).equals("NA"))) {
                            complete = true;
                        } else {
//                             printout(0,null, "8480 " + round_1_list);
                            for (int i = 0; i < round_1_list.size(); i++) {
                                String[] split_1 = ((String) round_1_list.get(i)).split(RESULT_SPLITER);
                                String name = null;
                                String id = null;
                                String hash = null;
                                for (int j = 0; j < split_1.length; j++) {
                                    String[] split2_a = split_1[j].split("=");
                                    if (split2_a.length == 2) {
                                        String[] split3_a = split2_a[0].split("=")[0].split("\\.");
                                        String column_nm = split3_a[split3_a.length - 1];
                                        String value = split2_a[1].trim();
                                        if (column_nm.equalsIgnoreCase("id") && value.matches("[0-9]+")) {
                                            id = value;
                                        } else if (column_nm.equalsIgnoreCase("name")) {
                                            name = value;
                                        } else if (column_nm.equalsIgnoreCase("HASH")) {
                                            hash = value;
                                        }
                                    } else {
                                        printout(0, null, "Error : unexpected result " + split_1[j]);
                                    }
                                }

                                if (name == null || id == null) {
                                    printout(0, null, "Error : Name or id not found");
                                } else {
                                    result_map.put(name, id);
                                    id2hash_map.put(id, hash);
                                }
                            }

                        }
                        if (!result_map.isEmpty()) {
                            HashMap<String, String> override_map = new HashMap<>();
                            override_map.put("X", "None of the above or more than one possibilities, use the parent value(" + last_link2feature + ")");
                            String next_user_selection = getUserSelection(new ArrayList<>(result_map.keySet()), override_map, "What type of tag do you want to create?");
                            if (result_map.containsKey(next_user_selection)) {
                                starting_parent_id = result_map.get(next_user_selection);
                                path = path + "->" + next_user_selection;
                                last_link2feature = tag_table + "|HASH=" + id2hash_map.get(result_map.get(next_user_selection));
                                tag_created = true;
                                selected_name = next_user_selection;
                            } else if (override_map.containsKey(next_user_selection)) {
                                starting_parent_id = next_user_selection;
                            }

                        } else {
                            complete = true;
                        }
                    } else {
                        printout(0, null, "Error: failed to get the starting parent_id. The parent id was not an integer");
                        complete = true;
                    }
                }
                if (tag_created) {
                    List result_l = advancedQueryHandler("tablename2feature.taggable=1", "tablename2feature.distinct table_name",
                            false, false, false, false, false, false, false, 0, true);
//                    ArrayList<String> tag_tables = new ArrayList<String>(result_l.size());
//                    for (int i = 0; i < result_l.size(); i++) {
//                        String c_nm = (String) result_l.get(i);
//                        tag_tables.add(c_nm.replace("2tags", "").replace("2TAGS", ""));
//                    }

                    if (result_l == null && result_l.isEmpty()) {
                        printout(0, null, "Error : Failed to find any taggable tables");
                    } else {
                        String target_table = getUserSelection(new ArrayList<String>(result_l), null, "Please select the target table (e.g. when tagging files select files2tags)");
                        String selected_table = target_table + "2tags";
                        HashMap<String, String> overide_map = new HashMap<>();
//                        int name_length = 32;
//                         printout(0,null, "7682 selected_name=" + selected_name);
//                         printout(0,null, "7683 path=" + path);
                        if (selected_name.length() > 256) {
                            selected_name = selected_name.substring(0, 255);
                        }
//                        if (path.length() - name_length < 0) {
//                            name_length = 0;
//                        }
                        overide_map.put("COLUMN=" + selected_table + ".link_to_feature", last_link2feature);
                        overide_map.put("COLUMN=" + selected_table + ".name", selected_name.replaceAll("\\s", "_").replaceAll(">", "_").replaceAll("=", ""));
//                        String ans = getuserInputSameLine("Type a short description and press enter, type NA or just press enter to describe this later.", "Description: ");
//                        if (ans == null || ans.trim().isEmpty() || ans.trim().equalsIgnoreCase("NA")) {
                        String ans = " <add your discription here>";
//                        }

                        overide_map.put("COLUMN=" + selected_table + ".description", path + " " + ans);
                        overide_map.put("name", last_link2feature);
                        overide_map.put(selected_table + ".link_to_feature", last_link2feature);
                        overide_map.put(selected_table + ".name", selected_name.replaceAll("\\s", "_").replaceAll(">", "_").replaceAll("=", ""));
                        overide_map.put(selected_table + ".description", path + " " + ans);

                        ArrayList<String> option_l = new ArrayList<>(3);

                        option_l.add("Perform a search on " + target_table + " ");
                        option_l.add("Use a SUC (static URL code) to select the " + target_table + " to be tagged");
                        option_l.add("Do not search. (Add the " + target_table + " values manually)");
                        option_l.add("Get me out of here, do not want to make any changes");
                        int option = getUserChoice(option_l, "How do you want to select the " + selected_table + " to be tagged ?");
//                        String user_selection_do_search = getUserSelection(option_l, null, "Do you want to search and add the " + selected_table + " now ?");
                        HashMap<String, ArrayList<String>> fromsearch_map = new HashMap<>();
                        if (option == 0) {
                            boolean exit_search = false;
                            while (!exit_search) {
                                String seach_term = getuserInputSameLine("Enter the name/names of the " + target_table, "Search for: ");
                                //get unique columns
                                String columns_to_get = "name";
                                List unq_key_l = getUniqueKeys(target_table);
                                if (unq_key_l != null) {
                                    columns_to_get = columns_to_get + "," + unq_key_l.toString().replaceAll("\\s", "").replace("[", "").replace("]", "");
                                }
                                List search_results_l = advancedQueryHandler(target_table + ".name=" + seach_term, target_table + "." + columns_to_get,
                                        false, false, true, false, false, false, false, 0, true);
                                if (search_results_l == null || search_results_l.isEmpty()) {
                                    if (!getuserInputSameLine("(X1) No match for  " + seach_term + " Do you want to search with a nother term ", " Y (search again) | N (do not search) ").equalsIgnoreCase("Y")) {
                                        exit_search = true;
                                    }
                                } else {
                                    for (int i = 0; i < search_results_l.size(); i++) {
                                        if (search_results_l.get(i) != null) {
                                            String[] split_1_a = search_results_l.get(i).toString().split("\\|\\|");
                                            for (int j = 0; j < split_1_a.length; j++) {
                                                String[] split_1_2_a = split_1_a[j].split("=");
                                                if (split_1_2_a.length == 2) {
                                                    if (!fromsearch_map.containsKey(split_1_2_a[0])) {
                                                        fromsearch_map.put(split_1_2_a[0], new ArrayList<String>());
                                                    }
                                                    fromsearch_map.get(split_1_2_a[0]).add(split_1_2_a[1]);
                                                }
                                            }
                                        }
                                    }
                                    exit_search = true;
                                }
                            }
                        } else if (option == 1) {
                            boolean exit_search = false;
                            while (!exit_search) {
                                String columns_to_get = "name";
                                List unq_key_l = getUniqueKeys(target_table);

                                if (unq_key_l != null) {
                                    columns_to_get = columns_to_get + "," + unq_key_l.toString().replaceAll("\\s", "").replace("[", "").replace("]", "");
                                }
                                String seach_term = getuserInputSameLine("Enter the code (SUC)", ": ");
                                List search_results_l = advancedQueryHandler("CODE=" + seach_term, target_table + "." + columns_to_get,
                                        false, false, true, false, false, false, false, 0, true);
                                if (search_results_l == null || search_results_l.isEmpty()) {
                                    if (!getuserInputSameLine("(X2) No match for  " + seach_term + " Do you want to search with a nother term ", " Y (search again) | N (do not search) ").equalsIgnoreCase("Y")) {
                                        exit_search = true;
                                    }
                                } else {
                                    for (int i = 0; i < search_results_l.size(); i++) {
                                        if (search_results_l.get(i) != null) {
                                            String[] split_1_a = search_results_l.get(i).toString().split("\\|\\|");
                                            for (int j = 0; j < split_1_a.length; j++) {
                                                String[] split_1_2_a = split_1_a[j].split("=");
                                                if (split_1_2_a.length == 2) {
                                                    if (!fromsearch_map.containsKey(split_1_2_a[0])) {
                                                        fromsearch_map.put(split_1_2_a[0], new ArrayList<String>());
                                                    }
                                                    fromsearch_map.get(split_1_2_a[0]).add(split_1_2_a[1]);
                                                }
                                            }
                                        }
                                    }
                                    exit_search = true;
                                }
                            }
                        } else if (option == 2) {
                            fromsearch_map.put(target_table + ".name", new ArrayList<>(Arrays.asList(new String[]{"NA"})));
                        } else if (option == 3) {
                            System.exit(33);
                        }

                        prepareTemplate(selected_table, dir_for_placing_prep_files, false, false, overide_map, fromsearch_map);
                    }
                }
            }
        } else {
            printout(0, null, "Error 75A: Communication failer. Can not connect to server.");
        }


    }
    /*
     MethodID=33.1
     */

    private void investigateTag(String url, String dir_for_placing_prep_files) {
        List result_l = advancedQueryHandler("alltags.id>0", "alltags.hash,alltags.name,alltags.TABLE_NM",
                false, false, false, false, false, false, false, 0, true);

//        System.out.println("9240 " + result_l.size());
        HashMap<String, String> name2hash = new HashMap<>();
        HashMap<String, String> hash2table = new HashMap<>();
        for (int i = 0; i < result_l.size(); i++) {
            String[] c_val = result_l.get(i).toString().split("\\|\\|");
            if (c_val.length == 3) {
                name2hash.put(c_val[1].toUpperCase(), c_val[0]);
                hash2table.put(c_val[0], c_val[2]);
            }
        }
        String[] url_a = url.split("\\|");
        for (int i = 0; i < url_a.length; i++) {
            url = "http://www.ncbi.nlm.nih.gov/pubmed/" + url_a[i] + "?report=abstract&format=text";
            htmlet(url_a[i], url, name2hash, hash2table);
        }
    }
    /*
     MethodID=33.2
     */

    private void htmlet(String refid, String newurl,
            HashMap<String, String> name2hash, HashMap<String, String> hash2table) {
        HashMap<String, ArrayList<String>> taf_map = new HashMap<>();
        taf_map.put(refid, new ArrayList<String>());
        HashSet<String> link2fet_s = new HashSet<>();
        try {
            try {
                System.out.println("9357 " + newurl);
                URL url = new URL(newurl);
                try {
                    Scanner scan = new Scanner(url.openStream());
                    boolean auth_info_found = false;
                    boolean abstr_found = false;
                    while (scan.hasNext()) {
                        String line = scan.nextLine().toUpperCase();
//                        System.out.println("9275 auth_info_found="+auth_info_found+" abstr_found="+abstr_found);
                        if (abstr_found) {
                            String[] line_a = scan.nextLine().trim().toUpperCase().split("\\s");
                            for (int i = 0; i < line_a.length; i++) {
                                if (name2hash.containsKey(line_a[i])) {
                                    String chash = name2hash.get(line_a[i]);
                                    link2fet_s.add(hash2table.get(chash) + "|HASH=" + chash);
                                }
                            }
                        } else if (!auth_info_found && line.startsWith("AUTHOR INFORMATIONS")) {
                            auth_info_found = true;
                        } else if (line.trim().isEmpty()) {
                            abstr_found = true;

                        }

                    }
                    scan.close();
                } catch (IOException ex) {
                    System.out.println("Error 33.2a:" + ex.getMessage());
                }
            } catch (MalformedURLException ex) {
                System.out.println("Error 33.2b:" + ex.getMessage());
            }
        } catch (Exception ex) {
            System.out.println("Error 33.2c:" + ex.getMessage());
        }

        Iterator it = link2fet_s.iterator();
        while (it.hasNext()) {
            System.out.println(" " + it.next());
        }

    }
    /*
     MethodId=92
     */

    private void synchronize() {
        String report_directory = getPWD();
        String batch_directory_path = null;
        File report_dir = new File(report_directory);
        if (report_dir.isDirectory()) {
            try {
                batch_directory_path = report_dir.getParentFile().getCanonicalPath();
            } catch (IOException ex) {
                printout(0, null, "Error 92a: Error geting the batch directory " + ex.getMessage());
            }
        }
        if (batch_directory_path == null || batch_directory_path.isEmpty()) {
            printout(0, null, "Error 92b: Failed to get batch directory");
        } else {
            HashMap<String, HashMap<String, String>> file_descriptio_map = get_descriptions(report_directory + File.separatorChar + PREPARE_TO_ADD_CUSTOM_TABLE_FILENM.replace("<TABLE>", "files"), "files.name", new ArrayList<String>(0));
//            HashMap<String, HashMap<String, String>> report_descript_map = get_descriptions(batch_directory_path + File.separatorChar + PREPARE_TO_ADD_CUSTOM_TABLE_FILENM.replace("<TABLE>", "report"), "report.name");
//            HashMap<String, HashMap<String, String>> batch_descripti_map = get_descriptions(batch_directory_path + File.separatorChar + PREPARE_TO_ADD_CUSTOM_TABLE_FILENM.replace("<TABLE>", "report_batch"), "report_batch.name");
//            HashMap<String, HashMap<String, String>> filetype_descripti_map = get_descriptions(report_directory + File.separatorChar + PREPARE_TO_ADD_CUSTOM_TABLE_FILENM.replace("<TABLE>", "filetype"), "filetype.name");
            printout(0, null, "file_descriptio_map=" + file_descriptio_map);
        }


    }

    /*
     Method_id=19;
     */
    private void create_temaplate_for_update(List matches_l, String table_nm, String inout_dir, String directoryfor_output,
            HashMap<String, ArrayList<String>> overide_map, boolean usedefault, long skip_larger,
            long skip_smaller, boolean current, boolean skip_checksum) {

        String searchterm = "_diff_";
        boolean ok = false;
        if (matches_l == null) {
            matches_l = new ArrayList<>(100);
        }
        if (overide_map == null) {
            overide_map = new HashMap<>();
        }

        if (current) {
            ok = true;
            table_nm = FILES_TABLE_FLAG;
            if (inout_dir == null) {
                inout_dir = getPWD();
            }
            ArrayList<File> expanded_file_l = new ArrayList<>(1);
            if (inout_dir != null) {
                File tmp = new File(inout_dir);
                if (tmp.isFile() || tmp.isDirectory()) {
                    expanded_file_l = getAllinSubFolders(tmp, new ArrayList<String>(1), new HashMap<String, ArrayList<File>>(), true, 0, 0, usedefault).get("OK");
                } else {
                    printout(0, null, "Error 19c: invalid location " + inout_dir);
                }
                if (expanded_file_l != null && !expanded_file_l.isEmpty()) {
                    ArrayList<String> full_file_names_l = new ArrayList<>(expanded_file_l.size());
                    for (int i = 0; i < expanded_file_l.size(); i++) {
                        try {
                            String canonon_name = expanded_file_l.get(i).getCanonicalPath();
                            full_file_names_l.add(canonon_name);

                        } catch (IOException ex) {
                        }
                    }
                    HashMap<String, String> files2checksum_map = getHashes(full_file_names_l, skip_larger, skip_smaller, skip_checksum);
                    HashMap<String, HashMap<String, String>> files2cdetails_map = getFileDetails(full_file_names_l, null, null);
                    for (int i = 0; i < full_file_names_l.size(); i++) {
                        matches_l.add(FILES_NAME + "=" + full_file_names_l.get(i));
                        HashMap<String, String> file_deta_map = files2cdetails_map.remove(full_file_names_l.get(i));// getFileDetails_map(full_file_names_l.get(i), FILES_NAME);
                        file_deta_map.put(FILES_CHECKSUM, files2checksum_map.get(full_file_names_l.get(i)));
                        ArrayList<String> file_deta_map_key_l = new ArrayList<>(file_deta_map.keySet());
                        for (int j = 0; j < file_deta_map_key_l.size(); j++) {
                            if (overide_map.containsKey(file_deta_map_key_l.get(j))) {
                                overide_map.get(file_deta_map_key_l.get(j)).add(file_deta_map.get(file_deta_map_key_l.get(j)));
                            } else {
                                ArrayList<String> tmp_l = new ArrayList<>(1);
                                tmp_l.add(file_deta_map.get(file_deta_map_key_l.get(j)));
                                overide_map.put(file_deta_map_key_l.get(j), tmp_l);
                            }

                        }
                    }


                    if (!files2checksum_map.isEmpty()) {
                        String source = FILES_CHECKSUM + "=\"" + files2checksum_map.values().toString().replace("[", "").replace("]", "").replaceAll("\\s", "").replaceAll(",", "||") + "\"";
                        String target = table_nm;
                        matches_l = advancedQueryHandler(source, target, true, false, true, false, false, false, false, 0, true);
                    }
                }
            }

        } else if (matches_l.isEmpty()) {
            while (!ok) {
                ok = true;
                List tables_l = advancedQueryHandler("tablename2feature.showinsearch=1", "tablename2feature.distinct table_name",
                        true, false, false, false, false, false, false, 0, true);
                table_nm = getUserChoice(tables_l, "Select the table to update.");
                List column_l = advancedQueryHandler("tablename2feature.table_name=" + table_nm, "tablename2feature.column_nm",
                        true, false, false, false, false, false, false, 0, true);
                String column_nm = getUserChoice(column_l, "Select the column in the table " + table_nm + " to search (to find the records to be updated) ");
                searchterm = getuserInputSameLine("Enter a search term for the column in " + column_nm + " the table " + table_nm + " find the record to be updated.\n (type * to get all, close query within quotes (\") to perform exact match)", null);
                boolean exactonly = false;
                if (searchterm.startsWith("\"") && searchterm.endsWith("\"")) {
                    exactonly = true;
                }
                String source = table_nm + "." + column_nm + "=" + searchterm;
                String target = table_nm;
                matches_l = advancedQueryHandler(source, target, exactonly, false, true, false, false, false, false, 0, true);
                if (matches_l == null || matches_l.size() < 1) {
                    String ans = getuserInputSameLine("The query " + searchterm + " with exact_match_only=" + exactonly + " did not return any results. Do you want to try again?", "[Yes-y | No -n]");
                    if (analyseUserResponse(ans, true, false) == 0) {
                        matches_l = null;
                        ok = false;
                    }
                }
            }
        }


        int file_nm_query_trim = 10;
        if (searchterm.length() < 10) {
            file_nm_query_trim = searchterm.length();
        }
        if (table_nm != null) {
            String file_nm = PREPARE_TO_ADD_CUSTOM_TABLE_FILENM.replace("<TABLE>", table_nm + "_" + searchterm.replaceAll("[^a-zA-Z0-9]", "_").substring(0, file_nm_query_trim));
            if (ok) {
                if (overide_map == null) {
                    overide_map = new HashMap<>();
                }
                printout(0, null, "Number of matches = " + matches_l.size());
                if (matches_l == null || matches_l.isEmpty() || matches_l.get(0).toString().toUpperCase().contains("ERRORMSGS")) {
                    printout(0, null, "4. Nothing to update");
                } else {
                    createContent_FromSearch(file_nm, directoryfor_output, table_nm, matches_l, overide_map);
                }

//                 printout(0,null, "Number of matches = " + matches_l.size() + ". Written to file: " + file_nm);
            } else {
                printout(0, null, "Error 19b: Unknown error");
            }
        } else {
            printout(0, null, "Error 19a: Failed get table name");
        }


//        }
    }

    /*
     
     Method_id=26;
     */
    private void update(String inout_dir, String directoryfor_output, String upedateinfo_file, boolean verbose, boolean usedefault,
            boolean updatecurrent, boolean recursive, long skip_larger, long skip_smaller, boolean skip_checksum, String old_host) {
        if (directoryfor_output == null) {
            directoryfor_output = getPWD();
        }
        if (updatecurrent) {
            String[] fiel_nm_a = getDiff(inout_dir, true, skip_larger, skip_smaller, skip_checksum, directoryfor_output, recursive, old_host);
            for (int i = 0; i < fiel_nm_a.length; i++) {
                String c_file = fiel_nm_a[i];
                if (c_file != null) {
                    String[] instr_a = c_file.split("\\|");
                    if (instr_a.length == 2) {
                        String op = instr_a[0];
                        String templ_file = instr_a[1];
                        if (op.toUpperCase().contains("_ADD_")) {
                            readTemplate_new(templ_file, verbose, true, directoryfor_output);
                        } else if (op.toUpperCase().contains("_UPDATE_")) {
                            update(null, directoryfor_output, templ_file, verbose, true, false, recursive, skip_larger,
                                    skip_smaller, skip_checksum, old_host);
                        } else {
                            printout(0, null, "Not processing " + templ_file);
                        }
                    }
                }

            }
        } else {
            if (upedateinfo_file == null) {
                HashMap<String, ArrayList<String>> overide_map = new HashMap<>();
                if (inout_dir == null) {
                    inout_dir = getPWD();
                }
                ArrayList<File> expanded_file_l = new ArrayList<>(1);
                if (inout_dir != null) {
                    File tmp = new File(inout_dir);
                    if (tmp.isFile() || tmp.isDirectory()) {
                        expanded_file_l = getAllinSubFolders(tmp, new ArrayList<String>(1), new HashMap<String, ArrayList<File>>(), true, 0, 0, usedefault).get("OK");
                    } else {
                        printout(0, null, "Error 19c: invalid location " + inout_dir);
                    }
                    if (expanded_file_l != null && !expanded_file_l.isEmpty()) {
                        ArrayList<String> full_file_names_l = new ArrayList<>(expanded_file_l.size());
                        for (int i = 0; i < expanded_file_l.size(); i++) {
                            try {
                                full_file_names_l.add(expanded_file_l.get(i).getCanonicalPath());
                            } catch (IOException ex) {
                            }
                        }
                        HashMap<String, String> files2checksum_map = getHashes(full_file_names_l, skip_larger, skip_smaller, skip_checksum);
                        HashMap<String, HashMap<String, String>> files2cdetails_map = getFileDetails(full_file_names_l, null, null);
                        for (int i = 0; i < full_file_names_l.size(); i++) {
                            HashMap<String, String> file_deta_map = files2cdetails_map.remove(full_file_names_l.get(i));//getFileDetails_map(full_file_names_l.get(i), FILES_NAME);
                            file_deta_map.put(FILES_CHECKSUM, files2checksum_map.get(full_file_names_l.get(i)));
                            ArrayList<String> file_deta_map_key_l = new ArrayList<>(file_deta_map.keySet());
                            for (int j = 0; j < file_deta_map_key_l.size(); j++) {
                                if (overide_map.containsKey(file_deta_map_key_l.get(j))) {
                                    overide_map.get(file_deta_map_key_l.get(j)).add(file_deta_map.get(file_deta_map_key_l.get(j)));
                                } else {
                                    ArrayList<String> tmp_l = new ArrayList<>(1);
                                    tmp_l.add(file_deta_map.get(file_deta_map_key_l.get(j)));
                                    overide_map.put(file_deta_map_key_l.get(j), tmp_l);
                                }
                            }
                        }
                    }
                }
            } else {
                File file = new File(upedateinfo_file);
                if (file.isFile() && file.canRead()) {
                    try {
                        int tot = 0;
                        Scanner scan = new Scanner(file);
                        while (scan.hasNext()) {
                            scan.nextLine();
                            tot++;
                        }
                        if (tot == 0) {
                            printout(0, null, "Nothing to process in " + upedateinfo_file);
                        } else if (tot > split_limit) {
                            printout(0, null, "The file " + upedateinfo_file + "(" + tot + " rows ) requires more than " + split_limit + " processing steps.\nSplitting the job to avoid system overload...");
                            int splits = tot / split_limit;
                            for (int i = 0; i <= splits; i++) {
                                printout(0, null, "From " + (i * split_limit) + " to " + ((i * split_limit) + split_limit));
                                ArrayList<String> result_l = readTemplate(upedateinfo_file, verbose, false,
                                        directoryfor_output, true, skip_larger, skip_smaller, false, (i * split_limit), (i * split_limit) + split_limit, skip_checksum);
//                                call_delete(result_l, true);
                                call_update(result_l, true);
//                                } else {
//                                     printout(0,null, "Error 26c: Failed to get data from file:" + upedateinfo_file);
//                                }
                            }
                        } else {
                            ArrayList<String> result_l = readTemplate(upedateinfo_file, verbose, false,
                                    directoryfor_output, true, skip_larger, skip_smaller, false, -1, Integer.MAX_VALUE, skip_checksum);
                            if (result_l != null) {
                                call_update(result_l, true);
                            } else {
                                printout(0, null, "Error 26a: Failed to get data from file:" + upedateinfo_file);
                            }
                        }
                    } catch (IOException e) {
                        printout(0, null, "Error 26b: Failed to get data from file:" + upedateinfo_file);
                    }
                } else {
                    printout(0, null, "Error 20b: Failed to read file " + upedateinfo_file);
                }
            }
        }
    }

    /*
     Method_id=02;
     */
    private void delete(String delete_info_file, boolean verbose, String dir_read_from_prep_files,
            long skip_larger, long skip_smaller, boolean skip_checksum) {
        File file = new File(delete_info_file);
        if (file.isFile() && file.canRead()) {
            try {
                int tot = 0;
                Scanner scan = new Scanner(file);
                while (scan.hasNext()) {
                    scan.nextLine();
                    tot++;
                }
                if (tot == 0) {
                    printout(0, null, "nothing to process in " + delete_info_file);
                } else if (tot > split_limit) {
                    printout(0, null, "The file " + delete_info_file + "(" + tot + " rows ) requires more than " + split_limit + " processing steps.\nSplitting the job to avoid system overload...");
                    int splits = tot / split_limit;
                    for (int i = 0; i <= splits; i++) {
                        printout(0, null, "From " + (i * split_limit) + " to " + ((i * split_limit) + split_limit) + " of about " + tot);
                        ArrayList<String> result_l = readTemplate(delete_info_file, verbose, false,
                                dir_read_from_prep_files, true, skip_larger, skip_smaller,
                                true, (i * split_limit), (i * split_limit) + split_limit, skip_checksum);
                        call_delete(result_l, true);
                    }
                } else {
                    ArrayList<String> result_l = readTemplate(delete_info_file, verbose, false,
                            dir_read_from_prep_files, true, skip_larger, skip_smaller, true, -1, Integer.MAX_VALUE, skip_checksum);
                    call_delete(result_l, true);
                }

            } catch (FileNotFoundException ex) {
                printout(0, null, "Error 62a:" + ex.getMessage());
            }
        } else {
            printout(0, null, "Error 02b: Failed to read file " + delete_info_file);
        }
    }

    /*
     MethodID=34
     */
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
            choices = choices + (tables_a.length + 1) + ") Cancel\n";
            boolean ok = false;
            while (!ok) {
                String ans = getuserInputSameLine(msg, choices);
                if (ans != null && ans.trim().matches("[0-9]+")) {
                    int coince = new Integer(ans);
                    if (coince >= 0 && tables_a.length > coince) {
                        ok = true;
                        result = tables_a[coince];
                    } else if (coince == (tables_a.length + 1)) {
                        System.exit(34);
                    }
                }
                if (!ok) {
                    printout(0, null, "Invalid choice, try again");
                }
            }
        }
        return result;
    }

    /*
     MethodID=23
     */
    private List call_update(List listToSync_l, boolean printresults) {
        List result;
//        System.out.println("9396 " + listToSync_l);
//        ProgressMonitor p = new ProgressMonitor(PROGRESS_MONT_INTERVAL, "EGM23");
//        (new Thread(p)).start();
        Object[] ob_a = startProgress(12);
        if (authenticate(false, null)) {
            try {
                no.ntnu.medisin.egenvar.webservice.AuthenticateService_Service service = new no.ntnu.medisin.egenvar.webservice.AuthenticateService_Service();
                no.ntnu.medisin.egenvar.webservice.AuthenticateService port = service.getAuthenticateServicePort();
                BindingProvider bp = (BindingProvider) port;
                bp.getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, getWSDL(false));
                result = port.update(authentication_string, user_email, listToSync_l);
//                p.cancel("EGM23");
                stopProgress(ob_a);
                if (result == null || result.isEmpty()) {
                    return null;
                } else {
                    if (result.get(0) != null && result.get(0).toString().startsWith("Error ")) {
                        printout(0, null, "Error : " + result.get(0));
                    } else {
                        if (printresults) {
                            for (int i = (result.size() - 1); i >= 0; i--) {
                                printout(0, null, i + ") " + result.get(i));

                            }
                        }
                        return result;
                    }
                }
            } catch (Exception ex) {
                printout(0, null, "Error 23:Failed to send error report " + ex.getMessage());
//                p.cancel("EGM24");
                stopProgress(ob_a);
            }
        } else {
            printout(0, null, "Error: Authentication failed. use -diagnose get more information");
        }
//        p.cancel("EGM24");
        stopProgress(ob_a);
        return null;
    }

    /*
     MethodID=24
     */
    private List call_delete(List listToSync_l, boolean printresults) {
        List result;
//        ProgressMonitor p = new ProgressMonitor(PROGRESS_MONT_INTERVAL, "EGM24");
//        (new Thread(p)).start();
        Object[] ob_a = startProgress(13);
        if (authenticate(false, null)) {
            try {
                no.ntnu.medisin.egenvar.webservice.AuthenticateService_Service service = new no.ntnu.medisin.egenvar.webservice.AuthenticateService_Service();
                no.ntnu.medisin.egenvar.webservice.AuthenticateService port = service.getAuthenticateServicePort();
                BindingProvider bp = (BindingProvider) port;
                bp.getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, getWSDL(false));
                result = port.delete(authentication_string, user_email, listToSync_l);
//                p.cancel("EGM24");
                stopProgress(ob_a);
                if (result == null || result.isEmpty()) {
                    printout(0, null, "(24) No match");
                    result = null;
                } else {
                    if (result.get(0) != null && result.get(0).toString().startsWith("Error ")) {
                        printout(0, null, "Error : " + result.get(0));
                    } else {
                        if (printresults) {
                            printout(0, null, "8885 excutions=" + result.size());
//                            for (int i = 0; i < result.size(); i++) {
//                                 printout(0,null, i + ") " + result.get(i));
//                            }
                        }
                        return result;
                    }
                }
            } catch (Exception ex) {
                printout(0, null, "Error 24:Failed to send error report " + ex.getMessage());
//                p.cancel("EGM24");
                stopProgress(ob_a);
            }
        } else {
            printout(0, null, "Error: Authentication failed. use -diagnose get more information");
        }

//        p.cancel("EGM24");
        stopProgress(ob_a);
        return null;
    }

    /*
     MethodID=25
     */
    private boolean test(String overide_wsdl, int caller) {
        boolean result = false;
//        ProgressMonitor p = new ProgressMonitor(PROGRESS_MONT_INTERVAL, "EGM25");
        Object[] ob_a = null;
        URL wsdl_url = null;
        try {
            result = true;
            if (overide_wsdl != null) {
                wsdl_url = new URL(overide_wsdl);
            } else {
                wsdl_url = new URL((getWSDL(true)));
            }
        } catch (Exception ex) {
//            ex.printStackTrace();
            result = false;
        }
//        System.out.println(caller + ") 9547  wsdl_url=" + wsdl_url + "\n\toveride_wsdl=" + overide_wsdl);
        if (result) {
            try {
                URLConnection uc = wsdl_url.openConnection();
                uc.setConnectTimeout(1000);
                uc.connect();
            } catch (IOException ex) {
                printout(0, null, "Error " + ex.getMessage() + ". Possible reasons:");
                printout(0, null, "\n\t1.Wrong WSDL " + overide_wsdl + "\n\t"
                        + "2.Not yet authenticated (use -authenticate)\n\t"
                        + "3.Server may be down\n\t"
                        + "4.Connection is very slow\n\t"
                        + "5.The server address has changed");
                result = false;
            }
        }
        if (result) {
//            (new Thread(p)).start();
            ob_a = startProgress(14);
            try {
                no.ntnu.medisin.egenvar.webservice.AuthenticateService_Service service = new no.ntnu.medisin.egenvar.webservice.AuthenticateService_Service();
                no.ntnu.medisin.egenvar.webservice.AuthenticateService port = service.getAuthenticateServicePort();
                BindingProvider bp = (BindingProvider) port;
                if (overide_wsdl == null) {
                    bp.getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, getWSDL(true));
                } else {
                    bp.getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, overide_wsdl);
                }
                result = port.test();
            } catch (Exception ex) {
                printout(0, null, "Error 25a:" + ex.getMessage());
//                p.cancel("EGM25");
                stopProgress(ob_a);
            }
        }
//        p.cancel("EGM25");
        stopProgress(ob_a);
        return result;
    }

    /*
     MethodID=35
     */
    private String getUserSelection(ArrayList<String> options_l, HashMap<String, String> override_map, String message) {
        String result = "Quit";
        if (options_l != null && !options_l.isEmpty()) {
            String tmp_disp_msg = "";
            if (options_l.size() == 1) {
                tmp_disp_msg = "\n" + 0 + ") " + options_l.get(0);
            } else {
                int max_length = 0;
                for (int i = 0; i < options_l.size(); i++) {
                    if (options_l.get(i).length() > max_length) {
                        max_length = options_l.get(i).length();
                    }
                }
                for (int i = 0; i < (options_l.size()); i = i + 2) {
                    tmp_disp_msg = tmp_disp_msg + "\n" + i + ") " + options_l.get(i);
                    for (int j = 0; j < (max_length - options_l.get(i).length()); j++) {
                        tmp_disp_msg = tmp_disp_msg + " ";
                    }
                    if (options_l.size() > (i + 1)) {
                        tmp_disp_msg = tmp_disp_msg + "\t" + (i + 1) + ") " + options_l.get(i + 1);
                    }
                }
            }
            if (override_map != null) {
                ArrayList<String> overide_keys = new ArrayList<>(override_map.keySet());
                int max_length = 0;
                if (overide_keys.size() == 1) {
                    tmp_disp_msg = tmp_disp_msg + "\n" + overide_keys.get(0) + ") " + override_map.get(overide_keys.get(0));
                } else {
                    for (int i = 0; i < overide_keys.size(); i++) {
                        if (overide_keys.get(i).length() > max_length) {
                            max_length = overide_keys.get(i).length();
                        }
                    }
                    for (int i = 0; i < (overide_keys.size() - 1); i = i + 2) {
                        tmp_disp_msg = tmp_disp_msg + "\n" + overide_keys.get(i) + ") " + override_map.get(overide_keys.get(i));
                        for (int j = 0; j < (max_length - overide_keys.get(i).length()); j++) {
                            tmp_disp_msg = tmp_disp_msg + " ";
                        }
                        tmp_disp_msg = tmp_disp_msg + "\t" + overide_keys.get(i + 1) + ") " + override_map.get(overide_keys.get(i + 1));

                    }
                }
            }
            tmp_disp_msg = tmp_disp_msg + "\nQ) Quit ";
            tmp_disp_msg = tmp_disp_msg + "\n";
            boolean ok = false;
            int selection = -1;
            while (!ok) {
                String ans_type = getuserInputSameLine(message + tmp_disp_msg, "Selection: ").trim();
                if (ans_type.matches("[0-9]+")) {
                    selection = new Integer(ans_type);
                    if (selection < options_l.size()) {
                        ok = true;
                        result = options_l.get(selection);
                    } else {
                        printout(0, null, "Invaild entry. ");
                    }
                } else if (ans_type.equalsIgnoreCase("q")) {
                    System.exit(35);
                } else {
                    String real_key = null;
                    if (override_map != null) {
                        real_key = getRealKey(override_map.keySet(), ans_type);
                    }
                    if (real_key != null) {
                        result = real_key;
                        ok = true;
                    } else {
                        printout(0, null, "Invaild entry. ");
                    }
                }
            }

        } else {
            printout(0, null, "Error : no options specified");
        }

        return result;
    }

//    /*
//     MethodID=30
//     */
//    private void refresh_version() {
//        ProgressMonitor p = new ProgressMonitor(PROGRESS_MONT_INTERVAL, "15");
//        (new Thread(p)).start();
//        if (authenticate(false, null)) {
//            try {
//                no.ntnu.medisin.egenvar.webservice.AuthenticateService_Service service = new no.ntnu.medisin.egenvar.webservice.AuthenticateService_Service();
//                no.ntnu.medisin.egenvar.webservice.AuthenticateService port = service.getAuthenticateServicePort();
//                BindingProvider bp = (BindingProvider) port;
//                bp.getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, getWSDL());
//                port.refreshVersion();
//                p.cancel();
//
//            } catch (Exception ex) {
//                 printout(0,null, "Error 30:Failed to send error report " + ex.getMessage());
//                p.cancel();
//            }
//        }
//        p.cancel();
//    }

    /*
     MethodID=31
     */
    private List getUniqueKeys(String table) {
        List result = null;
        Object[] ob_a = startProgress(15);
//        ProgressMonitor p = new ProgressMonitor(PROGRESS_MONT_INTERVAL, "EGM31");
//        (new Thread(p)).start();
        if (authenticate(false, null)) {
            try {
                no.ntnu.medisin.egenvar.webservice.AuthenticateService_Service service = new no.ntnu.medisin.egenvar.webservice.AuthenticateService_Service();
                no.ntnu.medisin.egenvar.webservice.AuthenticateService port = service.getAuthenticateServicePort();
                BindingProvider bp = (BindingProvider) port;
                bp.getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, getWSDL(false));
                result = port.getUniqueKeys(user_email, authentication_string, table);
                if (result == null || result.isEmpty()) {
                    printout(0, null, "(31) No match ");
                    result = null;
                } else {
                    if (result.get(0) != null && result.get(0).toString().startsWith("Error ")) {
                        printout(0, null, "Error : " + result.get(0));
                        result = null;
                    }
                }
//                p.cancel("EGM31");
                stopProgress(ob_a);

            } catch (Exception ex) {
                ex.printStackTrace();
                printout(0, null, "Error 31: Failed to send error report " + ex.getMessage());
//                p.cancel("EGM31");
                stopProgress(ob_a);
            }
        } else {
            printout(0, null, "Error: authentication failed");
        }

        return result;
    }
    /*
     MethodID=37
     */

    private String createuseraccount(String email, String code, String wsdl) {
        String result = null;
//        ProgressMonitor p = new ProgressMonitor(PROGRESS_MONT_INTERVAL, "EGM37");
//        (new Thread(p)).start();
        Object[] ob_a = startProgress(16);
        try {
            if (wsdl != null) {
                no.ntnu.medisin.egenvar.webservice.AuthenticateService_Service service = new no.ntnu.medisin.egenvar.webservice.AuthenticateService_Service();
                no.ntnu.medisin.egenvar.webservice.AuthenticateService port = service.getAuthenticateServicePort();
                BindingProvider bp = (BindingProvider) port;
                bp.getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, wsdl);
                result = port.createuseraccount(email, code, getIP());
            } else {
                System.out.println("Error invalid WSDL " + wsdl);
            }


//            p.cancel("EGM37");
            stopProgress(ob_a);
            if (result != null) {
                if (result.toUpperCase().startsWith("CODE")) {
                    String ans = getuserInputSameLine("Please enter the verification code (sent to the email " + email + ") ", "(press C to cancel or provide email)");
                    code = analyseUserResponse(ans, "[0-9]+");
                    createuseraccount(email, code, wsdl);
                } else {
                    printout(0, null, " " + result);
                }
            } else {
                printout(0, null, "Error 37a: Unknown error");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            printout(0, null, "Error 37b: Failed to send error report " + ex.getMessage());
//            p.cancel("EGM37");
            stopProgress(ob_a);
        }
        return result;
    }

    /*
     MethodID=32
     */
    private String createTagSource(String table) {
        String result = null;
//        ProgressMonitor p = new ProgressMonitor(PROGRESS_MONT_INTERVAL, "EGM32");
//        (new Thread(p)).start();
        Object[] ob_a = startProgress(17);
        if (authenticate(false, null)) {
            try {
                no.ntnu.medisin.egenvar.webservice.AuthenticateService_Service service = new no.ntnu.medisin.egenvar.webservice.AuthenticateService_Service();
                no.ntnu.medisin.egenvar.webservice.AuthenticateService port = service.getAuthenticateServicePort();
                BindingProvider bp = (BindingProvider) port;
                bp.getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, getWSDL(false));
                result = port.createTagSource(user_email, authentication_string, table);
//                p.cancel("EGM32");
                stopProgress(ob_a);
                printout(0, null, "Result :" + result);

            } catch (Exception ex) {
                printout(0, null, "Error 31: Failed to send error report " + ex.getMessage());
//                p.cancel("EGM32");
                stopProgress(ob_a);
            }
        } else {
            printout(0, null, "Error: authentication failed");
        }

        return result;
    }

    /*
     MethodID=36
     */
    private String getFileContent(String file_nm, String ignorlines) {
        Path p = Paths.get(file_nm);
        String query = "";
        if (Files.isRegularFile(p) && Files.isReadable(p)) {
            Charset charset = Charset.forName("ISO-8859-4");
            try (BufferedReader reader = Files.newBufferedReader(p, charset)) {
                int c = 0;
                String line = null;
//                StringBuilder str = new StringBuilder();
                ArrayList<String> content_l = new ArrayList<>();
                while ((line = reader.readLine()) != null) {
                    line = line.trim();
                    if (!line.startsWith(ignorlines) && !content_l.contains(line.trim())) {
                        content_l.add(line.trim());
//                        str.append(line.trim());
//                        str.append("||");
                    }
                }

                query = content_l.toString().replaceAll(",", "||").replaceAll("','", "||");
                query = query.replaceAll(" ||", "").replaceAll("|| ", "").replace("[", "").replace("]", "");//str.toString();
            } catch (IOException x) {
                System.err.format("IOException: %s%n", x);
                System.exit(36);
            }
        } else {
            printout(0, null, "Error: File not found or failed to gain read access  " + file_nm);
            System.exit(36);
        }
        return query;
    }

    private void tagBatch(String file_nm) {
        //need getParentList(tag)
        //getTagLinkId(parentlist, tag)
        Path p = Paths.get(file_nm);
        ArrayList<String> data_l = new ArrayList<>(10);
        int c = 0;
        if (Files.exists(p) && Files.isReadable(p)) {
            Charset charset = Charset.forName("ISO-8859-4");
            try (BufferedReader reader = Files.newBufferedReader(p, charset)) {
                c++;
                String line = null;
                String[] head_split = null;
                while ((line = reader.readLine()) != null) {
                    line = line.trim();
                    if (line.startsWith("##")) {
                        head_split = line.split("\t");
                    } else if (head_split != null) {
                        String[] data_split = line.split("\t");
                        if (data_split.length == 3) {
                            String taget = data_split[0].trim();
                            String tag_prent_list = data_split[1].trim();
                            String tag = data_split[2].trim();

                        } else {
                            printout(0, null, "Error: Only three columns, seperated by tab character is allowed. The line " + line + "(@row=" + c + ") does not follwo this rule");
                        }
                        data_l.add(line);
                    }
                }
            } catch (IOException x) {
                System.err.format("IOException: %s%n", x);
            }
        }
    }
    /*
     MethodID=32
     */

    private void createRandom(int max_no_of_files, int max_file_size) {
        Random rand = new Random();
        String location = getPWD();
        ArrayList<String> directories_to_construct = new ArrayList(2);
        ArrayList<String> new_locations_list = new ArrayList(2);
        int numberofDir = 3;
        for (int i = 0; i < numberofDir; i++) {
            int new_int = rand.nextInt(i + 100);
            String new_batch = "eGenVar_batch" + new_int;
            String new_report = "report" + new_int + "_" + rand.nextInt(5);
            directories_to_construct.add(File.separatorChar + new_batch + File.separatorChar + new_report + File.separatorChar);
            new_report = "report" + new_int + "_" + rand.nextInt(5);
            directories_to_construct.remove(File.separatorChar + new_batch + File.separatorChar + new_report + File.separatorChar);
            directories_to_construct.add(File.separatorChar + new_batch + File.separatorChar + new_report + File.separatorChar);
            new_report = "report" + new_int + "_" + rand.nextInt(5);
            directories_to_construct.remove(File.separatorChar + new_batch + File.separatorChar + new_report + File.separatorChar);
            directories_to_construct.add(File.separatorChar + new_batch + File.separatorChar + new_report + File.separatorChar);
        }


        int min_num_of_file = 1;
        max_file_size = max_file_size * 1048576;
        int min_file_size = 128;
//        int capacity_multiplier = 10;
        printout(0, null, "Min= " + min_file_size + " bytes Max=" + (max_file_size / 1048576) + "Mb");
        File loc = new File(location);
        if (loc.isDirectory() && loc.canWrite()) {
            for (int i = 0; i < directories_to_construct.size(); i++) {
                File dir_to_make = new File(location + File.separatorChar + directories_to_construct.get(i));
                if (!dir_to_make.isDirectory()) {
                    dir_to_make.mkdirs();
                    try {
                        new_locations_list.add(dir_to_make.getCanonicalPath());
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                } else if (dir_to_make.isDirectory()) {
                    try {
                        new_locations_list.add(dir_to_make.getCanonicalPath());
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
            }

            for (int i = 0; i < new_locations_list.size(); i++) {
                String c_loc = new_locations_list.get(i);
                int number_of_files = rand.nextInt(max_no_of_files) + min_num_of_file;
                printout(0, null, c_loc + " | " + number_of_files + " files");
                for (int j = 0; j < number_of_files; j++) {
                    File new_f = new File(c_loc + File.separatorChar + "genrated_" + rand.nextInt(10000) + "_" + rand.nextInt(10000) + ".txt");
                    if (!new_f.isFile()) {
                        try {
                            FileWriter out = new FileWriter(new_f.getCanonicalPath(), true);
                            long c_capacity = rand.nextInt(max_file_size) + min_file_size;
                            for (long k = 0; k < c_capacity; k++) {
                                out.append("" + rand.nextInt(9));
                            }
                            out.close();
                        } catch (IOException ex) {
                            ex.printStackTrace();
                        }
                    }
                }
            }

        } else {
            printout(0, null, "Location does not exist or can not wrote to it");
        }
    }

    /*
     MethodID=33
     */
    private String getIgnoreCase(HashMap<String, String> in_map, String key) {
        if (in_map.containsKey(key)) {
            return in_map.get(key);
        } else {
            key = key.trim();
            String val = null;
            ArrayList<String> key_l = new ArrayList<>(in_map.keySet());
            for (int i = 0; ((i < key_l.size()) && val == null); i++) {
                if (key_l.get(i).equalsIgnoreCase(key)) {
                    val = in_map.get(key_l.get(i));
                }
            }
            return val;
        }
    }

    public String createCertificate(String cacsert_file_nm, String host, int port, char[] passward) {
        printout(0, null, "Attempting to optain and trust certificates from Gmail");
        String result = null;
        String error = null;
        try {
//             printout(0,null, "javax.net.ssl.keyStore " + System.getProperty("javax.net.ssl.keyStore"));
//             printout(0,null, "4206 cacsert_file_nm=" + cacsert_file_nm);
            //load keystroe
            KeyStore keyStrore = KeyStore.getInstance("JKS");
            FileInputStream fis;
            fis = new FileInputStream(cacsert_file_nm);
            keyStrore.load(fis, passward);
            fis.close();
//            String alias = "smtp.gmail.com";           
            X509Certificate[] certs = getServerCertificates(cacsert_file_nm, host, port);
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

            error = ex.getMessage();

        } catch (NoSuchAlgorithmException ex) {

            error = ex.getMessage();
        } catch (CertificateException ex) {

            error = ex.getMessage();
        } catch (FileNotFoundException ex) {

            error = ex.getMessage();
        } catch (IOException ex) {

            error = ex.getMessage();
        } catch (Exception ex) {

            error = ex.getMessage();
        }
        if (error != null) {
            printout(0, null, "Error : " + error);
            printout(0, null, "\n There were some errors and attempting to rectify them by obtaining new certificates \n");
        }
        return result;
    }

    public X509Certificate[] getServerCertificates(String cacsert_file_nm, String host, int port) {
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
            printout(0, null, "Loading KeyStore " + file + "...");
            InputStream in = new FileInputStream(file);
            KeyStore ks = KeyStore.getInstance(KeyStore.getDefaultType());
            ks.load(in, passphrase);
            in.close();
            SSLContext context = SSLContext.getInstance("TLS");
            TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            tmf.init(ks);
            printout(0, null, "Number of trustmanagers (using only the first one) " + tmf.getTrustManagers().length);
            X509TrustManager defaultTrustManager = (X509TrustManager) tmf.getTrustManagers()[0];
            SavingTrustManager tm = new SavingTrustManager(defaultTrustManager);
            context.init(null, new TrustManager[]{tm}, null);
            SSLSocketFactory factory = context.getSocketFactory();

            printout(0, null, "Opening connection to " + host + ":" + port + "...");
            SSLSocket socket = (SSLSocket) factory.createSocket(host, port);
            socket.setSoTimeout(10000);
            try {
                printout(0, null, "Starting SSL handshake...");
                socket.startHandshake();
                socket.close();
                printout(0, null, "No errors, certificate is already trusted");
            } catch (SSLException ex) {
                error = ex.getMessage();
            }

            java.security.cert.X509Certificate[] chain = tm.chain;
            if (chain == null) {
                printout(0, null, "Could not obtain server certificate chain");
//                return certs;
            } else {
                BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
                printout(0, null, "Server sent " + chain.length + " certificate(s):");
                cert = chain;
//                String alias = host;
//                ks.setCertificateEntry(alias, cert);
            }
        } catch (SSLException ex) {
            error = ex.getMessage();
        } catch (IOException ex) {
            error = ex.getMessage();
        } catch (Exception ex) {
            error = ex.getMessage();
        }
        if (error != null) {
            printout(0, null, "Error : " + error);
            printout(0, null, "\n There were some errors and attempting to rectify them by obtaining new certificates ");
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
    /*
     MethodID=34
     */

    private String getHelp(HashMap<String, String> in_map, String key) {
        String help = "";
        ArrayList<String> match_val_l = new ArrayList<>(5);
        ArrayList<String> match_key_l = new ArrayList<>(5);
        ArrayList<String> key_l = new ArrayList<>(in_map.keySet());
        ArrayList<String> val_l = new ArrayList<String>(in_map.values());
        for (int i = 0; i < val_l.size(); i++) {
            if (val_l.get(i).toUpperCase().contains(key.toUpperCase())) {
                match_val_l.remove(val_l.get(i));
                match_val_l.add(val_l.get(i));
            }
        }
        for (int i = 0; i < key_l.size(); i++) {
            String c_key = key_l.get(i);
            if (c_key.toUpperCase().contains(key.toUpperCase()) || key.toUpperCase().contains(c_key.toUpperCase()) || match_val_l.contains(in_map.get(c_key))) {
                match_key_l.add(c_key);
            }
        }

        for (int i = 0; i < match_key_l.size(); i++) {
            help = help + "¤_____________________________¤" + match_key_l.get(i) + ":¤" + in_map.get(match_key_l.get(i));
        }
        if (help.isEmpty()) {
            help = "Not found";
        }
        return help;
    }

    /*
     MethodID=37
     */
    private String analyseUserResponse(String response, String pattern) {
        boolean ok = false;
        int safety = 3;
        String result = null;
        while (!ok) {
            safety--;
            ok = true;
            if (safety > 1) {
                if (response == null || response.isEmpty()) {
                    ok = false;
                    response = getuserInputSameLine("Invalid choice, please try again", "");
                } else {
                    if (response.equalsIgnoreCase("C")) {
                        printout(0, null, "Terminating all operations and exiting..");
                        System.exit(37);
                    } else if (pattern != null) {
                        if (response.matches(pattern)) {
                            result = response;
                        } else {
                            ok = false;
                            response = getuserInputSameLine("Invalid input, please try again", "");
                        }
                    } else {
                        ok = false;
                        response = getuserInputSameLine("Invalid choice, please try again", "");
                    }
                }
            } else {
                printout(0, null, "Maximum attempts exceeded. Terminating all operations and exiting.");
                System.exit(37);
            }

        }

        return result;
    }

    private Object[] startProgress(int caller) {
        if (opmode == 1) {//R type
            return null;
        } else {//Regular types
            ProgressMonitor p = new ProgressMonitor(PROGRESS_MONT_INTERVAL, caller);
            Thread t = new Thread(p);
            t.start();
            Object[] out_o = new Object[2];
            out_o[0] = p;
            out_o[1] = t;
            return out_o;
        }

    }

    private void stopProgress(Object[] in_ob_a) {
        ProgressMonitor p = null;
        Thread t = null;
        if (in_ob_a != null && in_ob_a.length == 2) {
            if (in_ob_a[0] != null && in_ob_a[0] instanceof ProgressMonitor) {
                p = (ProgressMonitor) in_ob_a[0];
            }
            if (in_ob_a[1] != null && in_ob_a[1] instanceof Thread) {
                t = (Thread) in_ob_a[1];
            }
        }
        if (p != null) {
            p.cancel(1);
        }
        if (t != null) {
            if (t.isAlive()) {
                t.interrupt();
            }
        }
    }

    private static void printout(int level, String pref, Object msg) {
        if (level == 1 || opmode != 1) {
            if (pref != null) {
                System.out.print(pref + "\t");
            }
            System.out.println(msg);
        }
    }
}
