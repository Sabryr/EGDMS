/**
 *
 *
 */
package webservice.medisin.ntnu.no;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StreamCorruptedException;
import java.io.UnsupportedEncodingException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.DatabaseMetaData;
import java.sql.Savepoint;
import java.sql.ResultSetMetaData;
import java.sql.PreparedStatement;
import java.sql.Types;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

/**
 * *
 * errors 201,202,203,204,220, 221. 250 ToDo : When the client cancel a process,
 * make sure the server acts acordingly
 *
 * @author sabryr
 */
@WebService(serviceName = "Authenticate_service")
public class Authenticate_service {

    private String errors = "";
    private String msges = "";
    public static final String QR_FLAG = "quick_response_code";
    public static HashMap<String, ArrayList<String>> table2Columns_map;
    public static HashMap<String, ArrayList<String>> table2Columns_full_name_map;
    public static ArrayList<String> all_table_l;
    public static final String ALL_COLUMNS_FLAG = "ALL_COLUMNS";
    public static String DATABASE_NAME_DATA = null;//"EGEN_DATAENTRY";
    public static final String DATABASE_USERS = "EGEN_USERS";
    public final String UNIQUE_ID_INDEX_NAME_PREFIX = "UNIQUE_IDENTIFICATION";// 
    public final String SUPER_PARENT_REF_FLAG = "super_parent_ref";
    public HashMap<String, String> auth_map;
    public final static int AUTHSTRINGLENGTH = 32;
    public static int QUERY_TIME_OUT = -1;
    public static double CURRENT_CMD_VERSION = -1;
//    public static double CURRENT_WES_VERSION = -1;
//    public static boolean version_loaded = false;
    public String update_link = null;//"http://localhost:8085/egenv_new.jar";
//    public final String VERSION_LINK = "http://localhost:8080/egenv_version.txt";
//    public final String UPDATELINK = "https://ans-180228.stolav.ntnu.no:8181/egenv.jar.gz";
    private final int MINIMUM_VALID_CHESUM_LENGTH = 30;
    private boolean temporarolyunavilable = false;
    private final String SERVICE_UNAVAILABLE_MESSAGE = "Error 0a: service temporarily unavailable";
    private final String REPLACEWITH_FALG = "_REP_";
    private final String ID_COLUMN_FALG = "ID";
    private HashMap<String, String> code2message_map;
//    private final static String CONSTRAINT_COLUMNS_PATTERN = "`(.*)`(.*)";
//    private final static String ID_COLULMN_PATTERN = "(.+)\\.id";
//    private final static String ALL_COLULMN_PATTERN = "(.+)\\.(.+)";
//    private final static String CONSTRAINT_REFERENCE_PATTERN = ".*CONSTRAINT.*FOREIGN KEY[^\\(]+\\([^A-Za-z0-9_\\s]{1}([A-Za-z0-9_\\s]+).*REFERENCES[\\s]*[^A-Za-z0-9_\\s]{1}([A-Za-z0-9_\\s]+)[^A-Za-z0-9_\\s]{1}.*\\([^A-Za-z0-9_\\s]{1}(.*)[^A-Za-z0-9_\\s]{1}\\).*";
//    private final static String CONSTRAINT_UNIQNESS_PATTERN = ".*UNIQUE KEY.*unique_identification[^\\(]+\\((.*)\\).*";
//    private Pattern ptn_1;
//    private Pattern ptn_2;
    private Pattern ptn_3;
//    private Pattern ptn_idcolumn;
//    private Pattern ptn_allcolumn;
    public static HashMap<String, ArrayList<String>> connection_map;
    public static HashMap<String, ArrayList<String>> resrved_names_map;
//    private HashMap<String, ArrayList<String>> table_to_dipendancies_map;
    public static ArrayList<Integer> numerical_types;
    public static HashMap<String, Integer> column2type_map;
    public static ArrayList<String> column2autoincrement_l;
//    public static HashMap<String,String> table2DateCpl_map;
//    private HashMap<String, ArrayList<String>> dipendancy_map;
//    private DataSource dataSource_data_for_keyconstraints;
    private static DataSource dataSource_data;
    private static DataSource dataSource_data_update;
    private static DataSource dataSource_users;
    public static ArrayList<String> tables_l;
    private final static String MAILER_DATASOURCE_NAME = "mail/egenvar";
    private static Session mail_session;
    private final static String DATASOURCE_NAME_USERS = "egen_userManagement_resource";
    private final static String DATASOURCE_NAME_data = "egen_dataView_resource";
    public final static String DATASOURCE_NAME_data_update = "egen_dataUpdate_resource";
    private final static String AUTH_LIST_FILE_NM = "auth.txt";
    public static final int TIME_OUT_LIMIT = 60000;
//    private final String TAGSOURCE_FLAG = "_TAGSOURCE";
    private static String doc_root = null;
    private static String wsdl_URL;
    public static final String TABLE_TO_USE_FLAG = "TABLETOUSE_";
    private static final String CODE_SEARCH_FLAG = "CODE";
//    public static final String SEARCH_CHILDREN_FLAG = "CHILDREN";
    public static final String CODE_RESULT_FLAG = "RESULT_CODE";
    public static HashMap<String, ArrayList<String>> table2uniqs_l_map;
    public static ArrayList<String> hierarchy_tables_l;
    private static final String HIERARCHY_FLAG = "_HIERARCHY";
    public static HashMap<String, String> tag2id;
    public static HashMap<String, Integer> user2level_map;
    private final int MINIMUM_LEVEL_TO_CREATE_TAGS = 1;
    public final static String ALL_INDEX_NAMES_FLAG = "ALL_INDICES";
    public static final String TAGSOURCE_FLAG = "_TAGSOURCE";
    public static final String TO_TAG_FLAG = "2TAGS";
    public static final String TAG_FLAG = "TAG";
    public static final String TAG_FLAG2 = "TAGS";
    public static HashMap<String, String> user_pin_map;
    public static HashMap<String, Integer> user_pin_attempts;
    public static ArrayList<String> blocke_mail_l;
    public static HashMap<String, GregorianCalendar> email_to_request_time_map;
    private HashMap<Integer, String> id2result_map;
    private HashMap<String, HashMap<String, String>> connecting_map;
    private HashMap<String, ArrayList<String[]>> reverse_contrants_map;
    private ArrayList<String> query_adjective_l;
//    private static int c_count = 0;
    final public String LINK_TO_FEATURE = "LINK_TO_FEATURE";
    public static ArrayList<String> tagable_tables_l;
    public static ArrayList<String> all_tagsource_tables_l;
    public static HashMap<String, String> all_tagable_to_tagged_map;
    public static ArrayList<String> tag_connection_tables_to_search_l;
    public static String delete_request_code = null;
    public static String delete_request_code_max_validity = "0";
    public final static String DELETE_REQUEST_CODE_FLAG = "DELETE_REQUEST_CODE";
    public static Service_controler contrl;
    public static final String SPLIT_BLOCK_FLAG = "SPLIT_BLOCK|";

    /**
     * MethodID=AS1
     * https://localhost:8181/eGenVar_WebService/Authenticate_service?Tester
     */
    @WebMethod(exclude = true)
    private void setMesages() {
        if (code2message_map == null) {
            code2message_map = new HashMap<>();
            code2message_map.put("", "");
        }
        if (resrved_names_map == null) {
            resrved_names_map = new HashMap<>();
        }

    }

    /**
     * MethodID=AS2
     *
     */
    @WebMethod(exclude = true)
    private int isAuthentic(String username, String password) {
        int result = -100;
        if (username != null && !username.isEmpty() && password != null) {
            try {
                if (getDatasource_users() != null) {
                    try {
                        Connection ncon = getDatasource_users().getConnection();
                        Statement st_1 = null;
                        ResultSet r_1 = null;
                        if (!ncon.isClosed()) {
                            try {
                                st_1 = ncon.createStatement();
                                String s_1 = "select 1 from " + DATABASE_USERS + ".useraccounts where email='" + username + "'  and password='" + password + "'";
                                r_1 = st_1.executeQuery(s_1);
                                while (r_1.next()) {
                                    result = 1;
                                }
                            } finally {
                                close(ncon, st_1, r_1);
                            }
                        } else {
                            result = -6;
                        }
                    } catch (SQLException e) {
                        result = -2;
                    }
                } else {
                    result = -4;
                }
            } catch (Exception ex) {
                result = -3;
            }
        } else {
            result = -5;
        }
        return result;
    }

    /**
     * MethodID=AS5
     *
     */
    @WebMethod(exclude = true)
    private int emailInUse(String email) {
        int result = -100;
        if (email != null && !email.isEmpty()) {
            try {
                if (getDatasource_users() != null) {
                    try {
                        Connection ncon = getDatasource_users().getConnection();
                        Statement st_1 = null;
                        ResultSet r_1 = null;
                        if (!ncon.isClosed()) {
                            try {
                                st_1 = ncon.createStatement();
                                String s_1 = "select 1 from " + DATABASE_USERS + ".useraccounts where email='" + email + "'";
                                r_1 = st_1.executeQuery(s_1);
                                if (r_1.next()) {
                                    result = 1;
                                } else {
                                    result = 0;
                                }
                            } finally {
                                close(ncon, st_1, r_1);
                            }
                        } else {
                            result = -6;
                        }
                    } catch (SQLException e) {
                        result = -2;
                    }
                } else {
                    result = -4;
                }
            } catch (Exception ex) {
                result = -3;
            }
        } else {
            result = -15;
        }
        return result;
    }

    /**
     * MethodID=AS3
     *
     */
    @WebMethod(exclude = true)
    private void setAuthenticatedCleintlist(String username, String clientid) {
        if (username != null && !username.isEmpty() && clientid != null && clientid.length() == AUTHSTRINGLENGTH) {
            try {
                FileWriter authfile = new FileWriter(AUTH_LIST_FILE_NM, true);
                authfile.append(username + "=" + clientid + "\n");
                authfile.close();
                try {
                    Runtime.getRuntime().exec("chmod 600 " + AUTH_LIST_FILE_NM);
                } catch (Exception e) {
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

//    /**
//     * MethodID=AS4
//     *
//     */
//    @WebMethod(exclude = true)
//    private double loadveriosn(boolean reload) {
//        if (CURRENT_WES_VERSION < 0) {
//            CURRENT_WES_VERSION = 0;
//            if (getDatasource_data() != null) {
//                try {
//                    Connection ncon = getDatasource_data().getConnection();
//                    Statement st_1 = null;
//                    ResultSet r_1 = null;
//                    if (!ncon.isClosed()) {
//                        try {
//                            st_1 = ncon.createStatement();
//                            String s_1 = "select PARAM_VALUE from " + get_correct_table_name("config") + " where name='WES_VERSION'";
//                            r_1 = st_1.executeQuery(s_1);
//                            while (r_1.next()) {
//                                CURRENT_WES_VERSION = r_1.getDouble("PARAM_VALUE");
//                            }
//                        } finally {
//                            close(ncon, st_1, r_1);
//                        }
//                    }
//                } catch (SQLException e) {
//                    System.out.println("Error AS4a:" + e.getMessage());
//                }
//            }
//        }
//
//        return CURRENT_WES_VERSION;
//    }
//    /**
//     * MethodID=AS4
//     *
//     */
//    @WebMethod(exclude = true)
//    private void loadveriosn(boolean reload) {
//        if (reload || !version_loaded) {
//            String version_url = null;
//            version_loaded = true;
//            try {
//                Connection ncon = null;
//                Statement st_1 = null;
//                ResultSet r_1 = null;
//                try {
//                    if (getDatasource_data() != null) {
//                        String sql = "select param_value, name from " + get_correct_table_name("config");
//                        ncon = getDatasource_data().getConnection();
//                        if (!ncon.isClosed()) {
//                            st_1 = ncon.createStatement();
//                            r_1 = st_1.executeQuery(sql);
//                            while (r_1.next()) {
//                                String name = r_1.getString("name");
//                                String val = r_1.getString("param_value");
//                                if (name.equalsIgnoreCase("CMD_VERSION_URL")) {
//                                    version_url = val.trim();
//                                } else if (name.equalsIgnoreCase("CMD_JAR")) {
//                                    update_link = val.trim();
//                                }
//                            }
//                            ncon.close();
//                        }
//                        close(ncon, null, null);
//                    }
//                } catch (SQLException ex) {
//                    ex.printStackTrace();
//                } finally {
//                    close(ncon, null, null);
//                }
//
//
//                if (version_url != null) {
//                    URL url = new URL(version_url);
//                    Scanner scan = new Scanner(url.openStream());
//                    while (scan.hasNext()) {
//                        String line = scan.nextLine().trim();
//                        if (line.matches("[0-9.]+")) {
//                            try {
//                                CURRENT_VERSION = new Double(line);
//                            } catch (Exception ex) {
//                            }
//                        }
//                    }
//                    scan.close();
//
//                }
//            } catch (Exception ex) {
//                ex.printStackTrace();
//            }
//        }
//    }
    /**
     * MethodID=AS5
     *
     */
    @WebMethod(exclude = true)
    private String getauthId(String username) {
//        if (delete_request_code == null) {
//            getDeleteCode();
//            System.out.println("Reload delete code ");
//            if (contrl == null) {
//                contrl = new Service_controler();
//                Thread server_control_thread = new Thread(contrl);
//                server_control_thread.start();
//            }
////            int limit = 60;
////            while (!contrl.isDone() && limit > 0) {
////                limit--;
////                try {
////                    Thread.sleep(1000);
////                } catch (InterruptedException ex) {
////                    System.out.println("Warning SC24a:" + ex.getMessage());
////                }
////            }
//        }
        if (username != null) {
            username = username.trim().replace("\n", "");
            if (auth_map == null) {
                auth_map = new HashMap<>();
                try {
                    File authfile = new File(AUTH_LIST_FILE_NM);
                    if (authfile.isFile()) {
                        Scanner scan = new Scanner(authfile);
                        while (scan.hasNext()) {
                            String line = scan.nextLine().trim();
                            if (!line.isEmpty() && line.contains("=")) {
                                auth_map.put(line.split("=")[0].trim(), line.split("=")[1].trim());
                            }
                        }
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                    return "Error 23 " + ex.getMessage();
                }
            }
            return auth_map.get(username);
        } else {
            return null;
        }

    }

    /**
     * MethodID=AS6
     *
     */
    @WebMethod(exclude = true)
    private int getReporterID(String username) {
        int userid = -1;
        Connection ncon = null;
        Statement st_1 = null;
        ResultSet r_1 = null;
        try {
            if (getDatasource_data_view() != null) {
                String sql = "select id from " + get_correct_table_name("person") + " where email='" + username + "'";
                ncon = getDatasource_data_view().getConnection();
                if (!ncon.isClosed()) {
                    st_1 = ncon.createStatement();
                    r_1 = st_1.executeQuery(sql);
                    while (r_1.next()) {
                        userid = r_1.getInt("id");
                    }
                }
                close(ncon, st_1, r_1);
            }
        } catch (SQLException ex) {
            System.out.println("Error A56a:" + ex.getMessage());
        } finally {
            close(ncon, st_1, r_1);
        }
        return userid;
    }

//    /**
//     * MethodID=AS7
//     *
//     */
//    @WebMethod(exclude = true)
//    private ArrayList<String> getEmails(String username) {
//        ArrayList<String> email_l = new ArrayList<>();
//        Connection ncon = null;
//        Statement st_1 = null;
//        ResultSet r_1 = null;
//        try {
//            String sql = "select email from person;";
//            ncon = getDatasource_data_view().getConnection();
//            if (!ncon.isClosed()) {
//                st_1 = ncon.createStatement();
//                r_1 = st_1.executeQuery(sql);
//                while (r_1.next()) {
//                    email_l.add(r_1.getString(1));
//                }
//            }
//            close(ncon, st_1, r_1);
//        } catch (SQLException ex) {
//            System.out.println("Error A57a:" + ex.getMessage());
//        } finally {
//            close(ncon, st_1, r_1);
//        }
//        return email_l;
//    }
    /**
     * MethodID=AS8
     *
     */
    @WebMethod(exclude = true)
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

    /**
     * MethodID=AS9
     *
     */
    @WebMethod(exclude = true)
    private int rand(int lo, int hi) {
        java.util.Random rn = new java.util.Random();
        int cval = 0;
        int n = hi - lo + 1;
        int i = rn.nextInt(n);
        if (i < 0) {
            i = -i;
        }
        cval = lo + i;
        return cval;
    }

    /**
     * MethodID=AS11
     *
     */
    @WebMethod(exclude = true)
    private int getOrAdd(DataSource dataSource, boolean donotcreate, boolean donotselect,
            Statement st_1, String table, HashMap<String, String> valu_map,
            HashMap<String, ArrayList<Integer>> table2id_map, ArrayList<String> overide_quating,
            boolean ifmorethanone_error, HashMap<String, String> add_specific_value_map) {
        int result = -1;
        table = get_correct_table_name(table);
        if (table == null) {
            msges = msges + "Error AS11e: Failed to locate the table requestd. May be this is due to a existing lock. Try again later or contact the administrator";
            result = -1;
        } else {
            if (valu_map == null || valu_map.isEmpty()) {
                msges = msges + "\nError 14d: Not enough information for " + table;
            } else {
                try {
                    String select_sql = createSql(table, valu_map, "id", "select", dataSource, overide_quating);
//                    System.out.println("504 " + select_sql);
                    String sql = select_sql;
                    int id = -1;
                    if (st_1 != null && !st_1.isClosed() && select_sql != null) {
                        try {
                            if (!donotselect) {
                                ResultSet r_1 = st_1.executeQuery(select_sql);
                                if (r_1.next()) {
                                    id = r_1.getInt(1);
                                    if (r_1.wasNull()) {
                                        id = -1;
                                    }
                                }
                                if (ifmorethanone_error && r_1.next()) {
                                    id = -2;
                                }
                                r_1.close();
                            }

                            if (id <= 0 && !donotcreate) {
                                if (add_specific_value_map == null) {
                                    add_specific_value_map = valu_map;
                                }
                                String insert_sql = createSql(table, add_specific_value_map, "id", "insert", dataSource, overide_quating);
                                sql = insert_sql;
//                                System.out.println("527 " + insert_sql);
                                int resutl = st_1.executeUpdate(insert_sql);
                                if (resutl > 0) {
                                    ResultSet r_2 = st_1.executeQuery(select_sql);
                                    if (r_2.next()) {
                                        id = r_2.getInt(1);
                                        if (id >= 0) {
                                            if (table2id_map != null) {
                                                if (table2id_map.containsKey(table)) {
                                                    table2id_map.get(table).add(id);
                                                } else {
                                                    ArrayList<Integer> tmp_set = new ArrayList<>();
                                                    tmp_set.add(id);
                                                    table2id_map.put(table, tmp_set);
                                                }
                                            }
                                        }
                                    }
                                    r_2.close();
                                }
                            }
                            result = id;
                        } catch (SQLException e) {
                            msges = msges + "Error AS11f: " + e.getMessage() + "\n for query =" + sql + " \n This error could also be due to the table being used not registered yet,"
                                    + " if this is the case server restart is required before proceeding";
                        }
                    } else {
                        msges = msges + "Error AS11b: Selection failed, the query must have atleast one of the key columns";
                    }
                } catch (SQLException ex) {
                    msges = msges + "\n Error AS11c" + ex.getMessage();
                }
            }
        }
        return result;
    }

    /**
     * MethodID=AS11.2
     *
     */
    @WebMethod(exclude = true)
    private String getOrAdd_fileded(DataSource dataSource, boolean donotcreate, boolean donotselect,
            Statement st_1, String table, HashMap<String, String> valu_map, ArrayList<String> overide_quating,
            boolean ifmorethanone_error, String selcolumn) {
        String result = null;
        table = get_correct_table_name(table);
        if (table == null) {
            msges = msges + "Error AS11e: Failed to locate the table requestd. May be this is due to a existing lock. Try again later or contact the administrator";
        } else {
            if (valu_map == null || valu_map.isEmpty()) {
                msges = msges + "\nError 14d: Not enough information for " + table;
            } else {
                try {

                    String select_sql = createSql(table, valu_map, selcolumn, "select", dataSource, overide_quating);
//                    System.out.println("582 " + select_sql);
                    String sql = select_sql;
                    String result_val = null;
                    if (st_1 != null && !st_1.isClosed() && select_sql != null) {
                        try {
                            if (!donotselect) {
                                ResultSet r_1 = st_1.executeQuery(select_sql);
                                if (r_1.next()) {
                                    result_val = r_1.getString(1);
                                    if (r_1.wasNull()) {
                                        result_val = null;
                                    }
                                }
                                if (ifmorethanone_error && r_1.next()) {
                                    result_val = null;
                                }
                                r_1.close();
                            }

                            if (result_val != null && !donotcreate) {
                                String insert_sql = createSql(table, valu_map, selcolumn, "insert", dataSource, overide_quating);
                                sql = insert_sql;
//                                System.out.println("527 " + insert_sql);
                                int resutl = st_1.executeUpdate(insert_sql);
                                if (resutl > 0) {
                                    ResultSet r_2 = st_1.executeQuery(select_sql);
                                    if (r_2.next()) {
                                        result_val = r_2.getString(1);
                                    }
                                    r_2.close();
                                }
                            }
                            result = result_val;
                        } catch (SQLException e) {
                            msges = msges + "Error AS11.2f: " + e.getMessage() + "\n for query =" + sql + " \n This error could also be due to the table being used not registered yet,"
                                    + " if this is the case server restart is required before proceeding";
                        }
                    } else {
                        msges = msges + "Error AS11.2b: Selection failed, the query must have atleast one of the key columns";
                    }
                } catch (SQLException ex) {
                    msges = msges + "\n Error AS11.2c" + ex.getMessage();
                }
            }
        }
        return result;
    }

    /**
     * MethodID=AS12
     *
     */
    @WebMethod(exclude = true)
    private int getFile4chekcsum(String checksum, Statement st_1) {
        int fileid = -1;
        if (checksum != null && checksum.length() > MINIMUM_VALID_CHESUM_LENGTH) {
            try {
                String sql = "select id from " + get_correct_table_name("files") + " where checksum='" + checksum + "'";
                ResultSet r_1 = st_1.executeQuery(sql);
                while (r_1.next()) {
                    fileid = r_1.getInt(1);
                }
                r_1.close();
            } catch (SQLException ex) {
            }
        }
        return fileid;
    }

    /**
     * MethodID=AS13
     *
     */
    @WebMethod(exclude = true)
    private String refineentry(String intext) {
        intext = intext.replaceAll("'", "\"");
//        if (intext != null && !intext.isEmpty() && !intext.matches("[0-9A-Za-z_\\-)\\(\\s\\,\\.]+")) {
//            HashMap<String, String> char2symbolMap = Constants.getchar2symbolMap();
//            ArrayList<String> char_l = new ArrayList(char2symbolMap.keySet());
//            for (int i = 0; i < char_l.size(); i++) {
//                intext = intext.replaceAll(char_l.get(i), char2symbolMap.get(char_l.get(i)));
//            }
//        }
        return intext;
    }

    /**
     * MethodID=AS14
     *
     */
    @WebMethod(exclude = true)
    private String createSql(String table, HashMap<String, String> value_map,
            String selectFiled, String type, DataSource dataSource, ArrayList<String> overide_quating) {
        table = get_correct_table_name(table);
        if (type.equalsIgnoreCase("select")) {
            ArrayList<String> value_l = new ArrayList<>(value_map.keySet());
            String sql = null;
            HashMap<String, String[]> constraint_map = get_key_contraints(table, dataSource);
//            if (column2autoincrement_l == null || table2DateCpl_map == null) {
//                column2autoincrement_l = new ArrayList<>();
//                table2DateCpl_map = new HashMap<>();
//            }

            if (constraint_map.containsKey(Constants.UNIQUE_TO_USER_COLUMNS_FLAG)) {
                /*onstants.ALL_KEY_COLUMNS_FLAG chaged from Constants.ALL_KEY_COLUMNS_FLAG as confuses the add template*/
                String[] uniques_a = constraint_map.get(Constants.UNIQUE_TO_USER_COLUMNS_FLAG);
                ArrayList<String> uniques_l = new ArrayList<>();
                if (uniques_a != null) {
                    uniques_l.addAll(Arrays.asList(uniques_a));
                }
                for (int i = 0; i < value_l.size(); i++) {
                    if (!containsIgnoreCase(uniques_l, value_l.get(i))) {
                        value_l.remove(i);
                        i--;
                    } else {
                    }
                }
            }


            if (!value_l.isEmpty()) {
                for (int i = 0; i < value_l.size(); i++) {
                    String c_key = value_l.get(i);
                    String value = value_map.get(c_key);
                    String qt = "'";
                    if (overide_quating.contains(c_key) || !shoulditbequated(table, c_key, dataSource)) {
                        qt = "";
                    }
                    value = refineentry(value);
                    if (qt.isEmpty() && value.equals("-1")) {
                        //skip -1 as id
                    } else {
                        if (sql == null) {
                            sql = "SELECT " + selectFiled + " from " + table + " where " + c_key + "=" + qt + value + qt;
                        } else {
                            sql = sql + " AND " + c_key + "=" + qt + value + qt;
                        }
                    }

                }
                return sql;
            } else {
                return null;
            }

        } else if (type.equalsIgnoreCase("insert")) {
            String sql = "INSERT INTO " + table + "(";
            ArrayList<String> value_l = new ArrayList<>(value_map.keySet());
            String fields = "";
            String values = "";
            for (int i = 0; i < value_l.size(); i++) {
                String c_key = value_l.get(i);
                String qt = "'";
                if (!shoulditbequated(table, c_key, dataSource)) {
                    qt = "";
                }
                if (!column2autoincrement_l.contains((table + "." + c_key).toUpperCase())) { //!c_key.equalsIgnoreCase("id")
                    String value = value_map.get(c_key);
                    //column2DateType_l
                    value = refineentry(value);
                    if (fields.isEmpty()) {
                        fields = c_key;
                        values = qt + value + qt;
                    } else {
                        fields = fields + "," + c_key;
                        values = values + "," + qt + value + qt;
                    }
                }
            }
            sql = sql + fields + ") values (" + values + ")";
            return sql;
        } else if (type.equalsIgnoreCase("update")) {
            String sql = "UPDATE " + table + " SET ";
            ArrayList<String> value_l = new ArrayList<>(value_map.keySet());
            for (int i = 0; i < value_l.size(); i++) {
                String c_key = value_l.get(i);
                String value = value_map.get(c_key);
                String qt = "'";
                if (!shoulditbequated(table, c_key, dataSource)) {
                    qt = "";
                }
                value = refineentry(value);
                if (i == 0) {
                    sql = sql + " c_key=" + qt + value + qt;
                } else {
                    sql = sql + ",c_key=" + qt + value + qt;
                }
            }
            return sql;
        } else {
            return null;
        }
    }

    /**
     * MethodID=AS15
     *
     */
    @WebMethod(exclude = true)
    public boolean shoulditbequated(String current_tbl_nm, String column_nm, DataSource dataSource) {
        boolean result = true;
        if (column_nm != null && current_tbl_nm != null) {
            column_nm = column_nm.split("\\.")[column_nm.split("\\.").length - 1];
            int type = getColumnType(current_tbl_nm, column_nm, dataSource);

            if (type > -999) {
                if (numerical_types == null) {
                    numerical_types = new ArrayList<>(3);
                    numerical_types.add(Types.INTEGER);
                    numerical_types.add(Types.BIGINT);
                    numerical_types.add(Types.DOUBLE);

                }
                if (numerical_types.contains(type)) {
                    result = false;
                }
            }
        }
//        System.out.println("926 "+current_tbl_nm+"."+column_nm+" type="+type +" result="+result);
        return result;
    }

    /**
     * MethodID=AS72
     *
     */
    @WebMethod(exclude = true)
    public boolean shoulditbequated(String correct_column_nm, DataSource dataSource) {
        boolean result = true;
        String[] c_split = correct_column_nm.split("\\.", 2);
        int type = getColumnType(c_split[0], c_split[1], dataSource);
//        System.out.println("868 correct_column_nm=" + correct_column_nm + "\t" + c_split[0] + "\t" + c_split[1]+"  type="+type);
        if (type > -999) {
            if (numerical_types == null) {
                numerical_types = new ArrayList<>(3);
                numerical_types.add(Types.INTEGER);
                numerical_types.add(Types.BIGINT);
                numerical_types.add(Types.DOUBLE);

            }
            if (numerical_types.contains(type)) {
                result = false;
            }
        }

        return result;
    }

    /**
     * MethodID=AS16
     *
     */
    @WebMethod(exclude = true)
    private Integer getColumnType(String current_tbl_nm, String column_nm, DataSource dataSource) {
        int type = -999;
        String reqest_keynm = current_tbl_nm + "." + column_nm;
        reqest_keynm = reqest_keynm.toUpperCase();
        if (column2type_map == null) {
            column2type_map = new HashMap<>();
            column2autoincrement_l = new ArrayList<>(5);

            Connection ncon = null;
            try {
                ncon = dataSource.getConnection();
                DatabaseMetaData metaData = ncon.getMetaData();
                if (!ncon.isClosed()) {
                    ArrayList<String> c_table_l = getTables();
                    for (int i = 0; i < c_table_l.size(); i++) {
                        String c_table = c_table_l.get(i);
                        String tablenm4metadata = c_table;
                        if (c_table.contains(".")) {
                            tablenm4metadata = c_table.split("\\.")[1];
                        }
                        ResultSet key_result = metaData.getColumns(null, DATABASE_NAME_DATA, tablenm4metadata, null);
                        if (!key_result.next()) {
                            key_result = metaData.getColumns(null, DATABASE_NAME_DATA.toUpperCase(), tablenm4metadata.toUpperCase(), null);
                        } else {
                            int typename = key_result.getInt("DATA_TYPE");
                            String autoincrmnt = key_result.getString("IS_AUTOINCREMENT");
                            String clmname = key_result.getString("COLUMN_NAME");
                            String key_name = DATABASE_NAME_DATA + "." + tablenm4metadata + "." + clmname;
                            key_name = key_name.toUpperCase();
                            column2type_map.put(key_name, typename);
                            if (autoincrmnt.equalsIgnoreCase("YES")) {
                                column2autoincrement_l.remove(key_name);
                                column2autoincrement_l.add(key_name);
                            }
//                            else if (typename == Types.TIMESTAMP) {
//                                column2DateType_l.remove(key_name);
//                                column2DateType_l.add(key_name);
//                            }

                        }
                        while (key_result.next()) {
                            int typename = key_result.getInt("DATA_TYPE");
                            String autoincrmnt = key_result.getString("IS_AUTOINCREMENT");
                            String clmname = key_result.getString("COLUMN_NAME");
                            String key_name = DATABASE_NAME_DATA + "." + tablenm4metadata + "." + clmname;
                            key_name = key_name.toUpperCase();
                            column2type_map.put(key_name, typename);
                            if (autoincrmnt.equalsIgnoreCase("YES")) {
                                column2autoincrement_l.remove(key_name.toUpperCase());
                                column2autoincrement_l.add(key_name.toUpperCase());
                            }
//                            else if (typename == Types.TIMESTAMP) {
//                                column2DateType_l.remove(key_name);
//                                column2DateType_l.add(key_name);
//                            }
                        }
                        key_result.close();
                    }

                    ncon.close();
                }
            } catch (SQLException e) {
                System.out.println("Error C1a" + e.getMessage());
            } finally {
                close(ncon, null, null);
            }
        }
        if (column2type_map.containsKey(reqest_keynm)) {
            type = column2type_map.get(reqest_keynm);
        }
        return type;
    }

    /**
     * MethodID=AS17
     *
     */
    @WebMethod(exclude = true)
    public HashMap<String, String[]> get_key_contraints(String current_tbl_nm, DataSource dataSource) {
        if (Constants.key_constraint_map == null) {
            Constants.key_constraint_map = new HashMap<>();
        }
        HashMap<String, String[]> returning_map = new HashMap<>(1);
        String primary_key_column = null;
        current_tbl_nm = get_correct_table_name(current_tbl_nm);
        if (current_tbl_nm != null && !current_tbl_nm.isEmpty() && !current_tbl_nm.equalsIgnoreCase("null") && !Constants.key_constraint_map.containsKey(current_tbl_nm)) {
            try {
                if (dataSource != null) {
                    String[] unique_a = get_key_contraints_unique_identification(current_tbl_nm, dataSource);
                    Connection ncon = null;
                    try {
                        ncon = dataSource.getConnection();
                        if (!ncon.isClosed()) {
                            String[] parent_child_a = null;
                            ArrayList<String> foreign_columns = new ArrayList<>(1);
                            ArrayList<String> foreign_key_names_columns = new ArrayList<>(1);
                            ArrayList<String> foreign_tables = new ArrayList<>(1);
                            String[] primary_k_a = null;
                            DatabaseMetaData metaData = ncon.getMetaData();
                            String tablenm4metadata = current_tbl_nm.split("\\.")[current_tbl_nm.split("\\.").length - 1];
                            ResultSet result_prim_keys = metaData.getPrimaryKeys(ncon.getCatalog(), DATABASE_NAME_DATA, tablenm4metadata);
                            if (!result_prim_keys.next()) {
                                result_prim_keys = metaData.getPrimaryKeys(ncon.getCatalog(), DATABASE_NAME_DATA.toUpperCase(), tablenm4metadata);
                                if (result_prim_keys.next()) {
                                    int col_c = result_prim_keys.getMetaData().getColumnCount();
                                    primary_k_a = new String[]{result_prim_keys.getString(4)};
                                }
                            } else {
                                int col_c = result_prim_keys.getMetaData().getColumnCount();
                                primary_k_a = new String[]{result_prim_keys.getString(4)};
                            }
                            result_prim_keys.close();
                            ResultSet key_result = metaData.getImportedKeys(ncon.getCatalog(), DATABASE_NAME_DATA, tablenm4metadata);
                            if (!key_result.next()) {
                                key_result = metaData.getImportedKeys(ncon.getCatalog(), DATABASE_NAME_DATA.toUpperCase(), tablenm4metadata.toUpperCase());
                            } else {
                                String[] tmp_a = new String[4];
                                tmp_a[0] = key_result.getString("FKCOLUMN_NAME");//column_name
                                tmp_a[1] = get_correct_table_name(key_result.getString("PKTABLE_NAME"));//ref_tblm
                                tmp_a[2] = key_result.getString("PKCOLUMN_NAME");//ref_tbl_clm_nm 
                                tmp_a[3] = key_result.getString("FK_NAME");//Key name
                                primary_key_column = key_result.getString("PKCOLUMN_NAME");//primary key
                                foreign_columns.add(tmp_a[0]);
                                returning_map.put(tmp_a[0], tmp_a);
                                if (!foreign_tables.contains(tmp_a[1])) {
                                    foreign_tables.add(tmp_a[1]);
                                }
                                foreign_key_names_columns.add(tmp_a[3]);
                            }
                            while (key_result.next()) {
                                String[] tmp_a = new String[4];
                                tmp_a[0] = key_result.getString("FKCOLUMN_NAME");//column_name
                                tmp_a[1] = get_correct_table_name(key_result.getString("PKTABLE_NAME"));//ref_tblm
                                tmp_a[2] = key_result.getString("PKCOLUMN_NAME");//ref_tbl_clm_nm 
                                tmp_a[3] = key_result.getString("FK_NAME");//Key name
                                primary_key_column = key_result.getString("PKCOLUMN_NAME");//primary key                               
                                foreign_columns.add(tmp_a[0]);
                                returning_map.put(tmp_a[0], tmp_a);
                                if (!foreign_tables.contains(tmp_a[1])) {
                                    foreign_tables.add(tmp_a[1]);
                                }
                                foreign_key_names_columns.add(tmp_a[3]);
                            }
                            if (!foreign_columns.isEmpty()) {
                                String[] tmp_a = new String[foreign_columns.size()];
                                HashMap<String, String> parent_child_map = new HashMap<>();
                                for (int i = 0; i < foreign_columns.size(); i++) {
                                    tmp_a[i] = foreign_columns.get(i);
                                    if (!parent_child_map.containsKey(returning_map.get(tmp_a[i])[1])) {
                                        parent_child_map.put(returning_map.get(tmp_a[i])[1], returning_map.get(tmp_a[i])[0]);
                                    } else {
                                        if (returning_map.get(tmp_a[i])[0].toUpperCase().contains("PARENT")) {
                                            parent_child_a = new String[2];
                                            parent_child_a[0] = returning_map.get(tmp_a[i])[0];
                                            parent_child_a[1] = parent_child_map.get(returning_map.get(tmp_a[i])[1]);
                                        } else if (returning_map.get(tmp_a[i])[0].toUpperCase().contains("CHILD")) {
                                            parent_child_a = new String[2];
                                            parent_child_a[0] = parent_child_map.get(returning_map.get(tmp_a[i])[1]);
                                            parent_child_a[1] = returning_map.get(tmp_a[i])[0];
                                        }
                                    }
                                }
                                returning_map.put(Constants.FOREIGN_KEY_COLUMNS_FLAG, tmp_a);
                                parent_child_map.clear();
                            }
                            if (!foreign_tables.isEmpty()) {
                                String[] tmp_a = new String[foreign_tables.size()];
                                for (int i = 0; i < tmp_a.length; i++) {
                                    tmp_a[i] = foreign_tables.get(i);
                                }
                                returning_map.put(Constants.FOREIGN_TABLE_FLAG, tmp_a);
                            }
                            if (!foreign_key_names_columns.isEmpty()) {
                                String[] tmp_a = new String[foreign_key_names_columns.size()];
                                for (int i = 0; i < tmp_a.length; i++) {
                                    tmp_a[i] = foreign_key_names_columns.get(i);
                                }
                                returning_map.put(Constants.FOREIGN_KEY_NAMES_FLAG, tmp_a);
                            }

                            if (unique_a != null) {
                                returning_map.put(Constants.UNIQUE_TO_USER_COLUMNS_FLAG, unique_a);

                            }
                            if (primary_key_column != null) {
                                returning_map.put(Constants.PRIMARY_KEY_COLUMNS_FLAG, new String[]{primary_key_column});
                            }

                            if (parent_child_a != null) {
                                returning_map.put(Constants.PARENT_CHILD_KEY_COLUMNS, parent_child_a);
                            }
                            if (unique_a != null || primary_k_a != null) {
                                if (primary_k_a == null) {
                                    returning_map.put(Constants.ALL_KEY_COLUMNS_FLAG, unique_a);
                                } else if (unique_a == null) {
                                    returning_map.put(Constants.ALL_KEY_COLUMNS_FLAG, primary_k_a);
                                } else {
                                    String[] x_uniqies_a = new String[primary_k_a.length + unique_a.length];
                                    for (int i = 0; i < primary_k_a.length; i++) {
                                        x_uniqies_a[i] = primary_k_a[i];
                                    }
                                    int pos = primary_k_a.length - 1;
                                    for (int i = 0; i < unique_a.length; i++) {
                                        pos++;
                                        x_uniqies_a[pos] = unique_a[i];
                                    }
                                    returning_map.put(Constants.ALL_KEY_COLUMNS_FLAG, x_uniqies_a);
                                }
                            }
                        }
                        ncon.close();
                    } catch (SQLException e) {
                        System.out.println("Error AS17a" + e.getMessage());
                    } finally {
                        close(ncon, null, null);
                    }
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            Constants.key_constraint_map.put(current_tbl_nm, returning_map);
//            return returning_map;
        } else if (Constants.key_constraint_map.containsKey(current_tbl_nm)) {
            returning_map = Constants.key_constraint_map.get(current_tbl_nm);
        }
        return returning_map;
    }

    /**
     * MethodID=AS17.1
     *
     */
    @WebMethod(exclude = true)
    private HashMap<String, String[]> get_key_contraint_names(String current_tbl_nm, DataSource dataSource) {
        HashMap<String, String[]> returning_map = new HashMap<>();

        if (dataSource != null && current_tbl_nm != null && !current_tbl_nm.isEmpty() && !current_tbl_nm.equalsIgnoreCase("null")) {
            try {

                try {
                    DatabaseMetaData metaData = dataSource.getConnection().getMetaData();
                    String tablenm4metadata = current_tbl_nm;
                    if (current_tbl_nm.contains(".")) {
                        tablenm4metadata = current_tbl_nm.split("\\.")[1];
                    }
                    ResultSet key_result = metaData.getIndexInfo(null, DATABASE_NAME_DATA, tablenm4metadata, false, false);
                    ArrayList<String> uniqs_list = new ArrayList<>(1);
                    ArrayList<String> all_list = new ArrayList<>(2);
                    if (!key_result.next()) {
                        key_result = metaData.getIndexInfo(null, DATABASE_NAME_DATA, tablenm4metadata.toUpperCase(), false, false);//.getImportedKeys(ncon.getCatalog(), Constants.DATABASE_NAME_DATA, current_tbl_nm);
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
                        returning_map.put(Constants.UNIQUE_TO_USER_COLUMNS_FLAG, uniq_to_user_a);
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

            } catch (Exception ex) {
                ex.printStackTrace();
            }

        }
        return returning_map;
    }

    /**
     * MethodID=AS18
     *
     */
    @WebMethod(exclude = true)
    private String[] get_key_contraints_unique_identification(String current_tbl_nm, DataSource dataSource) {
        String[] uniq_to_user_a = new String[1];
        if (current_tbl_nm != null && !current_tbl_nm.isEmpty() && !current_tbl_nm.equalsIgnoreCase("null") && !Constants.key_constraint_map.containsKey(current_tbl_nm)) {
            try {
                Connection ncon = null;
                try {
                    ncon = dataSource.getConnection();
                    if (!ncon.isClosed()) {
                        DatabaseMetaData metaData = ncon.getMetaData();
                        String tablenm4metadata = current_tbl_nm;
                        if (current_tbl_nm.contains(".")) {
                            tablenm4metadata = current_tbl_nm.split("\\.")[1];
                        }
                        ResultSet key_result = metaData.getIndexInfo(null, DATABASE_NAME_DATA, tablenm4metadata, false, false);//.getImportedKeys(ncon.getCatalog(), Constants.DATABASE_NAME_DATA, current_tbl_nm);
                        ArrayList<String> uniqs_list = new ArrayList<>(1);
                        if (!key_result.next()) {
                            key_result = metaData.getIndexInfo(null, DATABASE_NAME_DATA.toUpperCase(), tablenm4metadata.toUpperCase(), false, false);//.getImportedKeys(ncon.getCatalog(), Constants.DATABASE_NAME_DATA, current_tbl_nm);
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
                        if (!uniqs_list.isEmpty()) {
                            uniq_to_user_a = new String[uniqs_list.size()];
                            for (int i = 0; i < uniq_to_user_a.length; i++) {
                                uniq_to_user_a[i] = uniqs_list.get(i);
                            }
                        }
                        key_result.close();
                    }
                    ncon.close();
                } catch (SQLException e) {
                    System.out.println("Error 8767" + e.getMessage());
                } finally {
                    close(ncon, null, null);
                }

            } catch (Exception ex) {
                ex.printStackTrace();
            }

        }
        return uniq_to_user_a;
    }

    /**
     * MethodID=AS19
     *
     */
    @WebMethod(exclude = true)
    public HashMap<String, String[]> get_key_contraints(String current_tbl_nm, String target_tbl_nm, String index_name, DataSource dataSource) {
        HashMap<String, String[]> final_returning_map = new HashMap<>(1);
        HashMap<String, String[]> returning_map = new HashMap<>(1);
        HashMap<String, String[]> returning_all_map = get_key_contraints(current_tbl_nm, dataSource);

        target_tbl_nm = get_correct_table_name(target_tbl_nm);
        if (target_tbl_nm != null) {
            if (returning_all_map != null && returning_all_map.containsKey(Constants.FOREIGN_KEY_COLUMNS_FLAG)) {
                String[] frn_keys_a = returning_all_map.get(Constants.FOREIGN_KEY_COLUMNS_FLAG);
                for (int i = 0; i < frn_keys_a.length; i++) {
                    if (returning_all_map.get(frn_keys_a[i]).length >= 3 && returning_all_map.get(frn_keys_a[i])[1].equals(target_tbl_nm)) {
                        returning_map.put(returning_all_map.get(frn_keys_a[i])[0], returning_all_map.get(frn_keys_a[i]));
                    }
                }
            }
        } else {
            returning_map.putAll(returning_all_map);
        }

        if (index_name != null) {
            if (returning_map.containsKey(Constants.FOREIGN_KEY_NAMES_FLAG)) {
                ArrayList<String> return_keys = new ArrayList<>(returning_map.keySet());
                for (int i = 0; i < return_keys.size(); i++) {
                    if (returning_map.get(return_keys.get(i)).length >= 4) {
                        String c_index_name = returning_map.get(return_keys.get(i))[3].toUpperCase();
                        if (c_index_name.startsWith(index_name.toUpperCase())) {
                            final_returning_map.put(returning_map.get(return_keys.get(i))[0], returning_map.get(return_keys.get(i)));
                        }
                    }
                }
            }

        } else {
            final_returning_map.putAll(returning_map);
        }
        return final_returning_map;
    }

    /**
     * MethodID=AS20
     *
     */
    @WebMethod(exclude = true)
    private HashMap<String, HashMap<String, String>> get_connecting_tables(ArrayList<String> tables_l, DataSource dataSource) {
        if (connecting_map == null) {
            connecting_map = new HashMap<>();
            ArrayList<String> dipendents_l = new ArrayList<>(1);
            ArrayList<String> all_tabes_l = getTables();
            for (int i = 0; i < all_tabes_l.size(); i++) {
                String c_t = all_tabes_l.get(i);
                if (!containsIgnoreCase(tables_l, c_t)) {
                    HashMap<String, String[]> constra_map = get_key_contraints(c_t, dataSource);
                    if (constra_map.containsKey(Constants.FOREIGN_TABLE_FLAG)) {
                        String[] fortbl_a = constra_map.get(Constants.FOREIGN_TABLE_FLAG);
                        if (fortbl_a.length > 1) {
                            boolean found_none_satis = false;
                            for (int j = 0; (j < fortbl_a.length && !found_none_satis); j++) {
                                if (!containsIgnoreCase(tables_l, fortbl_a[j])) {
                                    found_none_satis = true;
                                }
                            }
                            if (!found_none_satis) {
                                for (int j = 0; j < fortbl_a.length; j++) {
                                    String c_tc = fortbl_a[j];
                                    if (containsIgnoreCase(tables_l, c_tc)) {
                                        if (!dipendents_l.contains(c_t)) {
                                            dipendents_l.add(c_t);
                                            String[] fkey_colsl_a = constra_map.get(Constants.FOREIGN_KEY_COLUMNS_FLAG);
                                            for (int k = 0; k < fkey_colsl_a.length; k++) {
                                                HashMap<String, String> tmp_map = new HashMap<>();
                                                tmp_map.put(constra_map.get(fkey_colsl_a[k])[0], "-1");
                                                connecting_map.put(c_t, tmp_map);
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return connecting_map;
    }

    /**
     * MethodID=AS21
     *
     */
    @WebMethod(exclude = true)
    private ArrayList< String[]> get_reverse_key_contraints(String current_tbl_nm, DataSource dataSource) {
        ArrayList<String[]> returning_list = new ArrayList<>(1);
        current_tbl_nm = get_correct_table_name(current_tbl_nm);
        if (reverse_contrants_map == null) {
            reverse_contrants_map = new HashMap<>();
        }
        if (current_tbl_nm != null && !current_tbl_nm.isEmpty() && !current_tbl_nm.equalsIgnoreCase("null")) {
            if (!reverse_contrants_map.containsKey(current_tbl_nm)) {
                ArrayList<String> all_tables = getTables();
                ArrayList<String[]> c_returning_list = new ArrayList<>(1);
                for (int k = 0; k < all_tables.size(); k++) {
                    String c_tbl_nm = all_tables.get(k);
                    if ((!c_tbl_nm.equals(current_tbl_nm))) {
                        HashMap<String, String[]> constraint_map = get_key_contraints(c_tbl_nm, current_tbl_nm, SUPER_PARENT_REF_FLAG, dataSource);

                        if (!constraint_map.isEmpty()) {
                            ArrayList<String> tmp_keys = new ArrayList<>(constraint_map.keySet());
                            for (int i = 0; i < tmp_keys.size(); i++) {
                                if (constraint_map.get(tmp_keys.get(i)).length >= 3) {
                                    String[] tmp_array = new String[3];
                                    tmp_array[0] = c_tbl_nm;
                                    tmp_array[1] = constraint_map.get(tmp_keys.get(i))[0];
                                    tmp_array[2] = constraint_map.get(tmp_keys.get(i))[2];
                                    c_returning_list.add(tmp_array);
                                }
                            }
                        }
                    }

                }
//                Connection ncon = null;
//                Statement st_1 = null;
//                ResultSet r_1 = null;
//                try {
//                    String sql = "select distinct table_name from  " + get_correct_table_name("tablename2feature") + " where showinsearch=1";
//                    ncon = dataSource.getConnection();
//                    st_1 = ncon.createStatement();
//                    r_1 = st_1.executeQuery(sql);

//                    while (r_1.next()) {
//                        String c_tbl_nm = get_correct_table_name(r_1.getString(1));
////                    if ((!c_tbl_nm.equals(current_tbl_nm)) && previous_tbl_nm_list != null && previous_tbl_nm_list.contains(current_tbl_nm)) {
//                        if ((!c_tbl_nm.equals(current_tbl_nm))) {
//                            HashMap<String, String[]> constraint_map = get_key_contraints(c_tbl_nm, current_tbl_nm, SUPER_PARENT_REF_FLAG, dataSource);
//                            if (!constraint_map.isEmpty()) {
//                                ArrayList<String> tmp_keys = new ArrayList<>(constraint_map.keySet());
//                                for (int i = 0; i < tmp_keys.size(); i++) {
//                                    if (constraint_map.get(tmp_keys.get(i)).length >= 3) {
//                                        String[] tmp_array = new String[3];
//                                        tmp_array[0] = c_tbl_nm;
//                                        tmp_array[1] = constraint_map.get(tmp_keys.get(i))[0];
//                                        tmp_array[2] = constraint_map.get(tmp_keys.get(i))[2];
//                                        c_returning_list.add(tmp_array);
//                                    }
//                                }
//                            }
//                        }
//                    }
//                    close(ncon, st_1, r_1);
//                } catch (SQLException ex) {
//                    System.out.println("Error AS21a:" + ex.getMessage());
//                } finally {
//                    close(ncon, st_1, r_1);
//                }
                reverse_contrants_map.put(current_tbl_nm, c_returning_list);
            }
        }
        if (current_tbl_nm != null && !current_tbl_nm.isEmpty() && !current_tbl_nm.equalsIgnoreCase("null")) {
            returning_list = reverse_contrants_map.get(current_tbl_nm);
        }

        return returning_list;
    }

    /**
     * MethodID=AS22
     *
     */
    @WebMethod(exclude = true)
    private ArrayList<String> getColumns(String current_tbl_nm, DataSource dataSource) {
        ArrayList<String> columns_l = new ArrayList<>(2);
        current_tbl_nm = get_correct_table_name(current_tbl_nm);
        if (table2Columns_map == null && current_tbl_nm != null) {
            table2Columns_map = new HashMap<>();
            Connection ncon = null;
            try {
                if (dataSource == null) {
                    dataSource = getDatasource_data_view();
                }
                ncon = dataSource.getConnection();
                DatabaseMetaData metaData = ncon.getMetaData();
                if (!ncon.isClosed()) {
                    ArrayList<String> c_table_l = getTables();
                    for (int i = 0; i < c_table_l.size(); i++) {
                        ArrayList<String> tmp_columns_l = new ArrayList<>(2);
                        String c_table = c_table_l.get(i);
                        String tablenm4metadata = c_table;
                        if (c_table.contains(".")) {
                            tablenm4metadata = c_table.split("\\.")[1];
                        }
                        ResultSet key_result = metaData.getColumns(null, DATABASE_NAME_DATA, tablenm4metadata, null);
                        if (!key_result.next()) {
                            key_result = metaData.getColumns(null, DATABASE_NAME_DATA.toUpperCase(), tablenm4metadata.toUpperCase(), null);
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
     * MethodID=AS22
     *
     */
    @WebMethod(exclude = true)
    private HashMap<String, ArrayList<String>> getColumns(String current_tbl_nm, DataSource dataSource, boolean map) {
        if (table2Columns_full_name_map == null) {
            table2Columns_full_name_map = new HashMap<>();
        }
        current_tbl_nm = get_correct_table_name(current_tbl_nm);
        if (!table2Columns_full_name_map.containsKey(current_tbl_nm)) {
            table2Columns_full_name_map.put(current_tbl_nm, new ArrayList<String>());
            ArrayList<String> col_l = getColumns(current_tbl_nm, dataSource);
            if (col_l != null && !col_l.isEmpty()) {
                for (int i = 0; i < col_l.size(); i++) {
                    table2Columns_full_name_map.get(current_tbl_nm).add(current_tbl_nm + "." + col_l.get(i));
                }
            }
        }


        return table2Columns_full_name_map;
    }

    /**
     * MethodID=AS23
     *
     */
    @WebMethod(exclude = true)
    public ArrayList<String> getTables() {
        if (all_table_l == null) {
            Connection ncon = null;
            Statement st_1 = null;
            ResultSet r_1 = null;
            try {
                all_table_l = new ArrayList<>(5);
                String database_nm = getDB_name();
//                System.out.println("1375 database_nm=" + database_nm);
                ncon = getDatasource_data_view().getConnection();
                if (!ncon.isClosed()) {
                    DatabaseMetaData metaData = ncon.getMetaData();
//                    System.out.println("1379 " + metaData.getURL());
                    r_1 = metaData.getTables(null, null, null, new String[]{"TABLE"});
                    while (r_1.next()) {
                        String table_nm = r_1.getString("TABLE_NAME");
                        String schema_nm = r_1.getString("TABLE_SCHEM");
//                        System.out.println("1383 " + table_nm + " schema_nm=" + schema_nm);3615
                        if (schema_nm.equals(database_nm)) {
                            all_table_l.add((schema_nm + "." + table_nm).toUpperCase());
                        }
                    }
                }
                close(ncon, st_1, r_1);
            } catch (SQLException ex) {
                System.out.println("Error AS23a " + ex.getMessage());
            } finally {
                close(ncon, st_1, r_1);
            }
        }
        return all_table_l;
    }

    /**
     * MethodID=AS4
     *
     */
    @WebMethod(exclude = true)
    public String getDB_name() {
        if (DATABASE_NAME_DATA == null) {
            Connection ncon = null;
            try {
                ncon = getDatasource_data_view().getConnection();
                String c_url = ncon.getMetaData().getURL();
                ncon.close();
                DATABASE_NAME_DATA = c_url.split("/")[c_url.split("/").length - 1];
            } catch (SQLException ex) {
                System.out.println("Error AS4a" + ex.getMessage());
            } finally {
                close(ncon, null, null);
            }
            if (DATABASE_NAME_DATA == null) {
                DATABASE_NAME_DATA = "EGEN_DATAENTRY";
            } else if (DATABASE_NAME_DATA.contains(":")) {
                DATABASE_NAME_DATA = DATABASE_NAME_DATA.split(":")[1];
            }
        }
//        System.out.println("1719 "+DATABASE_NAME_DATA);
        return DATABASE_NAME_DATA;
    }

    /**
     * MethodID=AS25
     *
     */
    @WebMethod(exclude = true)
    private List deleteRecords(String undos, String username, String authenticationid) {
        List results = new ArrayList<>(1);
        if (undos != null && !undos.isEmpty()) {
            Savepoint save2 = null;
            HashMap<String, String> tabl2id_map = new HashMap<>();
            String[] undo_a = undos.split("¤");
            for (int i = 0; i < undo_a.length; i++) {
                String[] c_undo = undo_a[i].split("\\|");
                if (c_undo.length == 2) {
                    String c_table = get_correct_table_name(c_undo[0]);
                    if (tabl2id_map.containsKey(c_table)) {
                        tabl2id_map.put(c_table, tabl2id_map.get(c_table) + "," + c_undo[1].replaceAll("[^0-9\\,]", ""));
                    } else {
                        tabl2id_map.put(c_table, c_undo[1].replaceAll("[^0-9\\,]", ""));
                    }
                }
            }
            if (!tabl2id_map.isEmpty()) {
                ArrayList<String> table2id_key_l = new ArrayList<>(tabl2id_map.keySet());
                if (table2id_key_l.size() > 1) {
                    table2id_key_l = decide_table_order(table2id_key_l, true);
                }
                results = new ArrayList<>(undo_a.length);
                Connection ncon = null;
                Statement st_1 = null;
                try {
                    ncon = getDatasource_data_update().getConnection();
                    if (ncon != null && !ncon.isClosed()) {
                        ncon.setAutoCommit(false);
                        save2 = ncon.setSavepoint();
                        st_1 = ncon.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                    } else {
                        results.add("Error 2a: Creating sql statement 0");
                    }
                } catch (SQLException ex) {
                    results.add("Error 2b: Creating sql statement 1." + ex.getMessage());
                }
                boolean allok = true;
                boolean alldeleted = true;
                boolean someldeleted = false;
                if (st_1 != null) {
                    for (int i = 0; i < table2id_key_l.size(); i++) {
                        String c_table = table2id_key_l.get(i);
                        String sql = "DELETE FROM " + c_table + " WHERE ID IN (" + tabl2id_map.get(c_table) + ")";
                        if (getDoesitExist("SELECT 1 from " + c_table + " WHERE ID IN (" + tabl2id_map.get(c_table) + ")", username, authenticationid)) {
                            try {
                                if (!st_1.isClosed()) {
                                    st_1.executeUpdate(sql);
                                    someldeleted = true;
                                    results.add("Marking for deletion ids " + tabl2id_map.get(c_table).substring(0, ((tabl2id_map.get(c_table).length() <= 15) ? tabl2id_map.get(c_table).length() : 15)) + " .... from " + c_table);
                                } else {
                                    results.add("Error 1d : Unrecoverable error, statement was closed");
                                    allok = false;
                                }

                            } catch (SQLException ex) {
                                alldeleted = false;
                                String msg = ex.getMessage();
                                if (msg.contains("foreign key constraint fails") || msg.contains("violation of foreign key constraint")) {
                                    results.add("Warning : in undo request for " + c_table + ". The items can not be deleted as used by others");
                                } else {
                                    results.add("Error 2f: in undo request for " + c_table + ". A sql error:" + msg);
                                    allok = false;
                                }


                            }
                        } else {
                            alldeleted = false;
                            results.add("Not found in database. ids " + tabl2id_map.get(c_table).substring(0, ((tabl2id_map.get(c_table).length() <= 15) ? tabl2id_map.get(c_table).length() : 15)) + ".... from " + c_table);
                        }
                    }
                } else {
                    results.add("Error: Creating sql statement 2");
                }
                try {
                    if (allok && ncon != null && !ncon.isClosed()) {
                        ncon.commit();
                        if (alldeleted) {
                            results.add("All marked records were permanently deleted");
                        } else if (someldeleted) {
                            results.add("Some marked records were permanently deleted");
                        } else {
                            results.add("No records were found to be deleted");
                        }
                        HashMap<String, String> config_map = new HashMap<>();
                        config_map.put("MEMORIZING_REQUESTED", "1");
                        setConfig(ncon, config_map);
                        ncon.close();
                    } else if (ncon != null && !ncon.isClosed()) {
                        if (save2 != null) {
                            results.add("Rolback started");
                            ncon.rollback(save2);
                            results.add("Roleback ended. No changes were made to the database");
                            close(ncon, st_1, null);
                        }
                    } else {
                        results.add("Error. connection was prematurely closed");
                    }
                } catch (SQLException ex) {
                    System.out.println("Error AS25a" + ex.getMessage());
                } finally {
                    close(ncon, st_1, null);
                }

            }

        } else {
            results.add("Error: null or an empty request");
        }

        return results;
    }

    /**
     * MethodID=AS26
     *
     */
    @WebMethod(exclude = true)
    private ArrayList<Integer> getID(Statement st_1, String table, String filed, String value, String operator) {
        ArrayList<Integer> result_l = new ArrayList<>(1);
        String sql = null;
        ResultSet r_1 = null;
        try {
            sql = "select id from " + table + " where " + filed + " " + operator + " '" + value + "'";
            r_1 = st_1.executeQuery(sql);
            while (r_1.next()) {
                result_l.add(r_1.getInt(1));
            }

            r_1.close();
            if (result_l.isEmpty()) {
                result_l.add(-1);
            }
            return result_l;
        } catch (SQLException ex) {
            ex.printStackTrace();
            result_l.clear();
            result_l.add(-2);
            msges = msges + "\n" + sql + " error AS26 " + ex.getMessage();
        } finally {
            close(null, null, r_1);
        }
        return result_l;
    }

    /**
     * MethodID=AS72
     *
     */
    @WebMethod(exclude = true)
    private ArrayList<String[]> getHierarchyDetials(Statement st_1, String table, String filed, String value, String operator) {
        ArrayList<String[]> result_l = new ArrayList<>(1);
        String sql = null;
//        Statement st_1 = null;
        ResultSet r_1 = null;
        try {
            HashMap<String, String[]> constraint_map = get_key_contraints(table, getDatasource_data_view());
            String[] frn_columns = constraint_map.get(Constants.FOREIGN_KEY_COLUMNS_FLAG);
            String frn_refs = "";
            if (frn_columns != null && frn_columns.length > 0) {
                frn_refs = "," + Arrays.deepToString(frn_columns).replace("[", "").replace("]", "");
            }
            sql = "select id" + frn_refs + " from " + table + " where " + filed + " " + operator + " '" + value + "'";
//              sql = "select * from " + table + " where " + filed + " " + operator + " '" + value + "'";
//            System.out.println("1677 "+sql);
//            st_1 = con.createStatement();
            r_1 = st_1.executeQuery(sql);
            int num_col = r_1.getMetaData().getColumnCount();
            while (r_1.next()) {
                String[] c_a = new String[num_col];
                c_a[0] = r_1.getString((1));
                boolean null_in_key = false;
                for (int i = 1; (!null_in_key && i < num_col); i++) {
                    String c_val = r_1.getString(i + 1);
                    if (c_val != null) {
                        c_a[i] = r_1.getMetaData().getColumnName(i + 1) + "=" + r_1.getString((i + 1));
                    } else {
                        null_in_key = true;
                    }
                }
                if (!null_in_key) {
                    result_l.add(c_a);
                }
            }

            r_1.close();
//            st_1.close();
//            if (result_l.isEmpty()) {
//                String[] c_a = new String[1];
//                c_a[0] = null;
//                result_l.add(c_a);
//            }

//            return result_l;
        } catch (SQLException ex) {
            result_l.clear();
            String[] c_a = new String[1];
            c_a[0] = null;
            result_l.add(c_a);
            msges = msges + "\n" + sql + " error AS72" + ex.getMessage();
        } finally {
            close(null, null, r_1);
        }
        return result_l;
    }

    /**
     * MethodID=AS27
     *
     */
    @WebMethod(exclude = true)
    private HashMap<String, String> getfiledHelp() {
        HashMap<String, String> help_map = new HashMap<>();
        Connection ncon = null;
        Statement st_1 = null;
        ResultSet r_1 = null;
        try {
            ncon = getDatasource_data_view().getConnection();
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

    /**
     * MethodID=AS28
     *
     * Todo: Need to remove the database name from column names
     */
    @WebMethod(exclude = true)
    private ArrayList<String> getParamAndDependancies(String table, String username) {
        HashMap<String, String> overides_map = new HashMap<>();
        String correct_tbl_nm = get_correct_table_name(table);
        Connection ncon = null;
        Statement stm_1 = null;
        try {
            ncon = getDatasource_data_view().getConnection();
            stm_1 = ncon.createStatement();
            overides_map.put("EMAIL", username);
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
        HashMap<String, String[]> constraints_map = get_key_contraints(correct_tbl_nm, getDatasource_data_view());
//          HashMap<String, String[]> constraints_map = get_key_contraints(table, new ArrayList<String>(overides_map.keySet()));
        ArrayList<String> allcoulmns_l = getColumns(correct_tbl_nm, getDatasource_data_view());//constraints_map.get(ALL_COLUMNS_FLAG);
        String[] unique_a = constraints_map.get(Constants.UNIQUE_TO_USER_COLUMNS_FLAG);

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
                if (c_column_nm.equals(ID_COLUMN_FALG) || (c_column_nm.endsWith("_" + ID_COLUMN_FALG) && !overides_map.containsKey(c_column_nm))) {
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
            constraints_map = get_key_contraints(tablestocheck_l.get(j), getDatasource_data_view());
            allcoulmns_l = getColumns(tablestocheck_l.get(j), getDatasource_data_view());// constraints_map.get(ALL_COLUMNS_FLAG);
            unique_a = constraints_map.get(Constants.UNIQUE_TO_USER_COLUMNS_FLAG);
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
                        if (c_column_nm.equalsIgnoreCase(ID_COLUMN_FALG) || (c_column_nm.endsWith("_" + ID_COLUMN_FALG) && !overides_map.containsKey(c_column_nm))) {
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
//            foreign_table_l.removeAll(tablestocheck_l);
//            tablestocheck_l.addAll(foreign_table_l);
        }

        return all_columns;
    }

    /**
     * MethodID=AS29
     *
     */
    @WebMethod(exclude = true)
    private String getOneForQuery(String query, Statement st_1) {
        String result = null;
        ResultSet r_1 = null;

        try {
            r_1 = st_1.executeQuery(query);
            if (r_1.next()) {
                result = r_1.getString(1);
            }
            r_1.close();
        } catch (SQLException ex) {
            msges = msges + "\n Error 201: " + ex.getMessage();
        } finally {
            close(null, null, r_1);
        }

        return result;
    }

    /**
     * MethodID=AS30
     *
     */
    @WebMethod(exclude = true)
    public ArrayList<String> decide_table_order(ArrayList<String> table_l, boolean reverse) {
        ArrayList<String> tables_orderd_l = new ArrayList<>(10);
        HashMap<String, ArrayList<String>> dipendancy_map = new HashMap<>();
        if (table_l != null) {
            for (int i = 0; i < table_l.size(); i++) {
                HashMap<String, String[]> constraint_result_map = get_key_contraints(table_l.get(i), getDatasource_data_view());
                if (constraint_result_map.containsKey(Constants.FOREIGN_TABLE_FLAG)) {
                    ArrayList<String> tmp_dipendancy_l = new ArrayList<>(Arrays.asList(constraint_result_map.get(Constants.FOREIGN_TABLE_FLAG)));
                    ArrayList<String> tmp_dipendancy_l_2 = new ArrayList<>(tmp_dipendancy_l);
                    if (tmp_dipendancy_l_2.removeAll(table_l)) {
                        dipendancy_map.put(table_l.get(i), tmp_dipendancy_l);
                    } else {
                        tables_orderd_l.add(table_l.get(i));
                    }
                } else {
                    tables_orderd_l.add(table_l.get(i));
                }
            }
            if (tables_orderd_l.isEmpty()) {
                System.out.println("Error RS30: Cyclic dependency can not be handlled with the current implementaiotn. Please inform this error to the developers.");
            } else {
                if (!dipendancy_map.isEmpty()) {
                    ArrayList<String> dependent_tables_l = new ArrayList<>(dipendancy_map.keySet());
                    int safty = 100;
                    while (safty > 0 && !dependent_tables_l.isEmpty()) {
                        safty--;
                        for (int i = 0; ((!dependent_tables_l.isEmpty()) && i < dependent_tables_l.size()); i++) {
                            String c_tbl = dependent_tables_l.remove(i);
                            ArrayList<String> c_depnds_l = dipendancy_map.get(c_tbl);
                            if (tables_orderd_l.containsAll(c_depnds_l)) {
                                tables_orderd_l.add(c_tbl);
                                i--;
                            } else {
                                dependent_tables_l.add(c_tbl);
                            }
                        }
                    }
                    if (safty <= 0) {
                        System.out.println("Error AS30a:Not all tables evaluated. ");
                    }
                }
            }
        }
        if (reverse) {
            ArrayList<String> tables_orderd_reverse_l = new ArrayList<>();
            for (int i = 0; i < tables_orderd_l.size(); i++) {
                tables_orderd_reverse_l.add(0, tables_orderd_l.get(i));
            }
            return tables_orderd_reverse_l;
        } else {
            return tables_orderd_l;
        }
    }

//    public ArrayList<String> decide_table_order(ArrayList<String> table_l, boolean reverse) {
//        ArrayList<String> tables_orderd_l = new ArrayList<>(10);
//        HashMap<String, ArrayList<String>> dipendancy_map = new HashMap<>();
//        if (table_l != null) {
//            table_l.removeAll(dipendancy_map.entrySet());
//            for (int i = 0; i < table_l.size(); i++) {
//                HashMap<String, String[]> constraint_result_map = get_key_contraints(table_l.get(i), getDatasource_data());
//                if (constraint_result_map.containsKey(Constants.FOREIGN_TABLE_FLAG)) {
//                    ArrayList<String> tmp_dipendancy_l = new ArrayList<>(Arrays.asList(constraint_result_map.get(Constants.FOREIGN_TABLE_FLAG)));
//                    dipendancy_map.put(table_l.get(i), tmp_dipendancy_l);
//                } else {
//                    dipendancy_map.put(table_l.get(i), new ArrayList<String>());
//                }
//            }
//            tables_orderd_l.add(table_l.remove(0));
//            boolean done = false;
//            int safety = 20;
//            while (!done && safety > 0) {
//                safety--;
//                int pos_to_add = 0;
//                if (table_l.isEmpty()) {
//                    done = true;
//                } else {
//                    String table_to_add = table_l.remove(0);
//                    for (int i = 0; i < tables_orderd_l.size(); i++) {
//                        String current_in_order_table = tables_orderd_l.get(i);
//                        ArrayList<String> current_in_order_dependancy_l = dipendancy_map.get(current_in_order_table);
//                        ArrayList<String> table_to_add_dipendancy_l = dipendancy_map.get(table_to_add);
//                        if (current_in_order_dependancy_l.contains(table_to_add)) {
//                            if (pos_to_add > i) {
//                                pos_to_add = i;
//                            }
//                        } else if (table_to_add_dipendancy_l.contains(current_in_order_table)) {
//                            if (pos_to_add <= i) {
//                                pos_to_add = i + 1;
//                            }
//                        }
//                    }
//                    tables_orderd_l.add(pos_to_add, table_to_add);
//                }
//            }
//        }
//        if (reverse) {
//            ArrayList<String> tables_orderd_reverse_l = new ArrayList<>();
//            for (int i = 0; i < tables_orderd_l.size(); i++) {
//                tables_orderd_reverse_l.add(0, tables_orderd_l.get(i));
//            }
//            return tables_orderd_reverse_l;
//        } else {
//            return tables_orderd_l;
//        }
//
//    }
    /**
     * MethodID=AS31
     *
     */
    @WebMethod(exclude = true)
    private ArrayList<String> getTablesUsed(HashMap<String, HashMap<String, String>> data_map_in) {
        ArrayList<String> tables_used_l = new ArrayList<>(1);
        ArrayList<String> key_l = new ArrayList<>(data_map_in.keySet());
        for (int i = 0; i < key_l.size(); i++) {
            if (!tables_used_l.contains(key_l.get(i))) {
                tables_used_l.add(key_l.get(i));
            }
        }
        return tables_used_l;
    }

    /**
     * MethodID=AS32
     *
     */
    @WebMethod(exclude = true)
    private String[] pupolateFromTemplate(List<String> parameter_list) {
        String result = "";
        String[] report_a = new String[3];
        HashMap<String, ArrayList<Integer>> table2id_map = new HashMap<>();
        Savepoint save1 = null;
        Connection ncon = null;
        Statement stm_1 = null;
        boolean allok = true;
        try {
            ncon = getDatasource_data_update().getConnection();
            ncon.setAutoCommit(false);
            save1 = ncon.setSavepoint();
            stm_1 = ncon.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
        } catch (Exception ex) {
            msges = "Error ASW32a: cre4ating connections " + ex.getMessage();
            close(ncon, stm_1, null);
        }
        HashMap<Integer, HashMap<String, HashMap<String, String>>> data_map = list2map(parameter_list, stm_1);
//        System.out.println("20796 "+ data_map );
        if (data_map != null && !data_map.isEmpty()) {
            ArrayList<Integer> top_key = new ArrayList<>(data_map.keySet());
            ArrayList<String> used_table_l = getTablesUsed(data_map.get(top_key.get(0)));
            HashMap<String, HashMap<String, String>> supli_map = get_connecting_tables(used_table_l, getDatasource_data_update());
            for (int k = 0; k < top_key.size(); k++) {
                data_map.get(top_key.get(k)).putAll(supli_map);
            }
            ArrayList<String> tablse_list = new ArrayList<>(data_map.get(top_key.get(0)).keySet());
            HashMap<String, HashMap<String, String>> table2ForeingConnection_map = new HashMap<>();
            ArrayList<String> new_tablse_list = new ArrayList<>();
            for (int j = 0; j < tablse_list.size(); j++) {
                ArrayList<String[]> reverse_key_map = get_reverse_key_contraints(tablse_list.get(j), getDatasource_data_update());
                for (int i = 0; i < reverse_key_map.size(); i++) {
                    if (reverse_key_map.get(i).length >= 3) {
                        HashMap<String, String[]> sub_constraints_map = get_key_contraints(reverse_key_map.get(i)[0], getDatasource_data_update());
                        String[] unique_a = sub_constraints_map.get(Constants.UNIQUE_TO_USER_COLUMNS_FLAG);
                        HashMap<String, String> tp_map = new HashMap<>();
                        if (unique_a != null) {
                            boolean addthis = true;
                            for (int k = 0; (k < unique_a.length && addthis); k++) {
                                if (sub_constraints_map.containsKey(unique_a[k])) {
                                    if (!tablse_list.contains(sub_constraints_map.get(unique_a[k])[1])) {
                                        addthis = false;
                                    } else {
                                        tp_map.put(unique_a[k], null);
                                    }
                                } else {
                                    addthis = false;
                                }
                            }
                            if (addthis) {
                                for (int k = 0; k < top_key.size(); k++) {
                                    data_map.get(top_key.get(k)).put(reverse_key_map.get(i)[0], tp_map);
                                    new_tablse_list.remove(reverse_key_map.get(i)[0]);
                                    new_tablse_list.add(reverse_key_map.get(i)[0]);
                                }
                            }
                        }
                    }
                }
            }
            tablse_list.addAll(new_tablse_list);
            top_key = new ArrayList<>(data_map.keySet());
            for (int j = 0; j < tablse_list.size(); j++) {
                HashMap<String, String[]> constraints_map = get_key_contraints(tablse_list.get(j), getDatasource_data_update());
                String[] foreingKeyColumns_a = constraints_map.get(Constants.FOREIGN_KEY_COLUMNS_FLAG);
                if (foreingKeyColumns_a != null) {
                    HashMap<String, String> foriegnKey2Table = new HashMap<>();
                    for (int k = 0; k < foreingKeyColumns_a.length; k++) {
                        String[] foreingDetails_a = constraints_map.get(foreingKeyColumns_a[k]);
                        if (foreingDetails_a != null && foreingDetails_a.length > 0) {
                            foriegnKey2Table.put(foreingDetails_a[0], foreingDetails_a[1]);
                        }
                    }
                    table2ForeingConnection_map.put(tablse_list.get(j), foriegnKey2Table);
                }
            }
            ArrayList<String> orderd_table_list = new ArrayList<>();
            orderd_table_list.addAll(tablse_list);
            if (tablse_list.size() > 1) {
                orderd_table_list = decide_table_order(orderd_table_list, false);
            }
            HashMap<String, Integer> already_checked_map = new HashMap<>();
            if (orderd_table_list.isEmpty()) {
                result = result + "Error:  Failed to decide order of processing";
                allok = false;
            } else {
                for (int j = 0; (j < orderd_table_list.size() && allok); j++) {
                    result = result + "\nPopulating  " + orderd_table_list.get(j) + " ...... ";
                    int useexsiting = 0;
                    int createnew = 0;
                    for (int i = 0; (i < top_key.size() && allok); i++) {
                        HashMap<String, HashMap<String, String>> table_value_map = data_map.get(top_key.get(i));
                        HashMap<String, String> c_value_map = table_value_map.get(orderd_table_list.get(j));
                        if (table2ForeingConnection_map.containsKey(orderd_table_list.get(j))) {
                            HashMap<String, String> foriegnKey2Table = table2ForeingConnection_map.get(orderd_table_list.get(j));
                            ArrayList<String> foriegnKey2Table_key_l = new ArrayList<>(foriegnKey2Table.keySet());
                            for (int k = 0; k < foriegnKey2Table_key_l.size(); k++) {
                                String targetid = null;
                                if (data_map.get(top_key.get(i)).get(foriegnKey2Table.get(foriegnKey2Table_key_l.get(k))) != null) {
                                    targetid = data_map.get(top_key.get(i)).get(foriegnKey2Table.get(foriegnKey2Table_key_l.get(k))).get("id");
                                }
                                if (targetid != null && targetid.matches("[0-9]+") && data_map.get(top_key.get(i)).containsKey(orderd_table_list.get(j))) {
                                    data_map.get(top_key.get(i)).get(orderd_table_list.get(j)).put(foriegnKey2Table_key_l.get(k), targetid);
                                }
                            }
                        }
                        if (c_value_map != null) {
                            int created_id = -1;
                            created_id = getOrAdd(getDatasource_data_update(), true, false, stm_1, orderd_table_list.get(j),
                                    c_value_map, table2id_map, new ArrayList<String>(), false, null);
                            if (created_id < 0) {
                                created_id = getOrAdd(getDatasource_data_update(), false, true, stm_1, orderd_table_list.get(j),
                                        c_value_map, table2id_map, new ArrayList<String>(), false, null);
                                createnew++;
                            } else {
                                useexsiting++;
                            }
                            if (created_id >= 0) {
                                data_map.get(top_key.get(i)).get(orderd_table_list.get(j)).put("id", created_id + "");
                            } else {
                                allok = false;
                                result = result + " Failed:" + orderd_table_list.get(j);
                            }
                        } else {
                            result = result + " Failed: " + orderd_table_list.get(j);
                        }
                    }
                    if (allok) {
                        result = result + " OK. New reacords created=" + createnew + "| existing records used=" + useexsiting;
                    } else {
                        result = result + " Failed";
                    }
                }
            }
            already_checked_map.clear();
        }
        try {
            if (allok && ncon != null && !ncon.isClosed()) {
                ncon.commit();
                HashMap<String, String> config_map = new HashMap<>();
                config_map.put("MEMORIZING_REQUESTED", "1");
                setConfig(ncon, config_map);
                ArrayList<String> table2id_keyl = new ArrayList<>(table2id_map.keySet());
                String undos = "";
                for (int i = 0; i < table2id_keyl.size(); i++) {
                    undos = undos + table2id_keyl.get(i) + "|" + table2id_map.get(table2id_keyl.get(i)) + "\n";
                }
                result = result + "\n All updates committed ";
                report_a[1] = undos;
            } else {
                if (ncon != null && save1 != null) {
                    result = result + "\n Rolback started";
                    ncon.rollback(save1);
                    result = result + "\n No changes were made to the database";
                }
            }
            close(ncon, stm_1, null);
        } catch (Exception ex) {
            msges = "Error ASW32b: creating connections " + ex.getMessage();
        } finally {
            close(ncon, stm_1, null);
        }
        report_a[0] = result;
        report_a[2] = msges;

        return report_a;

    }

    /**
     * MethodID=AS33
     *
     */
    @WebMethod(exclude = true)
    private HashMap<Integer, HashMap<String, HashMap<String, String>>> list2map(List<String> parameter_list, Statement st_1) {
        HashMap<Integer, HashMap<String, HashMap<String, String>>> data_map = new HashMap<>();
        if (parameter_list != null) {
            if (tag2id == null) {
                tag2id = new HashMap<>();
            }
            boolean local_errors = false;
            for (int i = 0; (i < parameter_list.size() && !local_errors); i++) {
                boolean isatag = false;
                String[] table_column__value_a = parameter_list.get(i).split("==", 2);//Ad hoc untill all methods changed
                if (table_column__value_a.length == 1) {
                    table_column__value_a = parameter_list.get(i).split("=", 2);
                }
                if (table_column__value_a.length == 2 && table_column__value_a[0] != null) {
                    String clmn_nm = table_column__value_a[0];
                    if (clmn_nm.split("\\.")[clmn_nm.split("\\.").length - 1].toUpperCase().equals("LINK_TO_FEATURE")) {
                        isatag = true;
                    }
                    clmn_nm = get_Correct_Column_name(clmn_nm, null);
                    if (clmn_nm != null) {
                        String[] table_column_a = clmn_nm.split("\\.");
                        if (table_column_a.length == 3) {
                            table_column_a[1] = get_correct_table_name(table_column_a[1]);
                            if (table_column_a[1] != null) {
                                String table_nm = table_column_a[1];
                                String[] values_a = table_column__value_a[1].split(";;");
                                int j = 0;
                                for (j = 0; (j < values_a.length && !local_errors); j++) {
                                    String c_val = values_a[j];
                                    if (isatag && st_1 != null && c_val != null) {
                                        if (!c_val.contains("=")) {
                                            if (tag2id.containsKey(c_val)) {
                                                c_val = tag2id.get(c_val);
                                            } else {
                                                String[] tag_split = c_val.split("/");
                                                if (tag_split[0].trim().isEmpty()) {
                                                    tag_split = Arrays.copyOfRange(tag_split, 1, tag_split.length - 1);
                                                }
                                                if (tag_split.length > 1) {
                                                    c_val = getidforTagpath(st_1, tag_split, true, false);
                                                    if (c_val != null) {
                                                        tag2id.put(values_a[j], c_val);
                                                    } else {
                                                        local_errors = true;
                                                    }
                                                } else {
                                                    msges = msges + " Error: incorrect tag format " + c_val;
                                                    local_errors = true;
                                                }
                                            }
                                        }
                                    }
                                    if (!local_errors) {
                                        if (data_map.containsKey(j)) {
                                            if (data_map.get(j).containsKey(table_nm)) {
                                                data_map.get(j).get(table_nm).put(table_column_a[2], c_val);
                                            } else {
                                                HashMap<String, String> tmp_map = new HashMap<>();
                                                tmp_map.put(table_column_a[2], c_val);
                                                data_map.get(j).put(table_nm, tmp_map);
                                            }
                                        } else {
                                            HashMap<String, String> tmp_map = new HashMap<>();
                                            tmp_map.put(table_column_a[2], c_val);
                                            HashMap<String, HashMap<String, String>> new_map = new HashMap<>();
                                            new_map.put(table_nm, tmp_map);
                                            data_map.put(j, new_map);
                                        }
                                    } else {
                                        msges = msges + "  Error AS55d: invalid tag format " + values_a[j];
                                    }
                                }
                            } else {
                                msges = "Error AS55a: Table not found in allowed list for this operation " + clmn_nm;
                            }
                        } else {
                            msges = "Error AS55b: format mismatch in " + parameter_list.get(i);
                        }
                    } else {
                        msges = "Error AS55c: format mismatch in " + parameter_list.get(i);
                    }
                } else {
                    msges = "Error AS55e: format mismatch in " + parameter_list.get(i);
                }
            }
        }
        return data_map;
    }

//    private String getidforTagpath(Statement st_1, String[] tag_split) {
//        String c_val = null;
//        if (tag_split != null) {
//            String tag_table = get_correct_table_name(tag_split[0]);
//            if (tag_table != null) {
//                for (int i = (tag_split.length - 2); i > 0; i--) {
//                    String c_parnt = tag_split[i - 1];
//                    String c_child = tag_split[i];
//                    String sql ="SELECT ID FROM "+tag_table+" WHERE NAME="+c_child+" AND PARENT_ID=(SELECT ID FROM "+;
//
//                }
//            } else {
//                msges = msges + " Error: tag source not defined. Define the source " + tag_split[0] + " before using it.";
//            }
//
//        } else {
//            msges = msges + " Error: incorrect tag format " + c_val;
//        }
//
//        return c_val;
//    }
    /**
     * MethodID=AS74
     *
     */
    @WebMethod(exclude = true)
    private HashSet<String> getidforTagpath_search(Statement st_1, String[] tag_split,
            boolean create, boolean parent_request) {
        HashSet<String> c_val_l = new HashSet<>();
        if (tag_split != null && tag_split.length > 1) {
            String[] tables_a = tag_split[0].split("\\|");
//                System.out.println("2379 " + Arrays.deepToString(tag_split)+" tables_a="+Arrays.deepToString(tables_a)+" parent_request="+parent_request);
            for (int i = 0; i < tables_a.length; i++) {
                String[] tag_split_copy_a = new String[tag_split.length];
                tag_split_copy_a[0] = tables_a[i];
                for (int j = 1; j < tag_split_copy_a.length; j++) {//use system arraycopy
                    tag_split_copy_a[j] = tag_split[j];
                }
                String c_val = getidforTagpath(st_1, tag_split_copy_a, create, parent_request);
//                System.out.println("2392 " + c_val);
                if (c_val != null) {
                    c_val_l.add(c_val);
                    c_val_l.add(c_val.replace("|ID=", "|PARENT_ID="));
                    c_val_l.add(c_val.replace("|PARENT_ID=", "|ID="));
                }
//                System.out.println("2397 " + c_val_l);
//                System.out.println("2307 " + Arrays.deepToString(tag_split_copy_a) + "=" + c_val_l);
            }
        } else {
            String[] tables_a = tag_split[0].split("\\|");
            if (tables_a.length == 2) {
                for (int i = 0; i < (tables_a.length - 1); i++) {
                    c_val_l = getidforTagpath_search_singleterm(st_1, tables_a[tables_a.length - 1], tables_a[i],
                            create, parent_request);
                }
            }

        }
//        System.out.println("2412 c_val_l=" + c_val_l);
        return c_val_l;
    }

    /**
     * MethodID=AS74_2
     *
     */
    @WebMethod(exclude = true)
    private HashSet<String> getidforTagpath_search_singleterm(Statement st_1, String tag_term, String tag_table,
            boolean create, boolean parent_request) {
        HashSet<String> c_val_l = new HashSet<>();
        try {
            if (tag_table != null && tag_term != null) {
                System.out.println("2426 " + "SELECT ID,HASH FROM " + tag_table + " WHERE NAME='" + tag_term + "'");
                String sql = "SELECT ID,HASH FROM " + tag_table + " WHERE NAME='" + tag_term + "'";
                ResultSet r_1 = st_1.executeQuery(sql);
                String table_nm = r_1.getMetaData().getTableName(1);
                while (r_1.next()) {
                    if (parent_request) {
                        c_val_l.add(table_nm + "|ID=" + r_1.getInt(1));
                        c_val_l.add(table_nm + "|PARENT_ID=" + r_1.getInt(1));
                    } else {
                        c_val_l.add(table_nm + "|HASH=" + r_1.getString(2));
                    }
                }
                r_1.close();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            msges = "Error AS74_2a: creating connections " + ex.getMessage();
        }
        return c_val_l;
    }

    /**
     * MethodID=AS74
     *
     */
    @WebMethod(exclude = true)
    private String getidforTagpath(Statement st_1, String[] tag_split,
            boolean create, boolean parent_request) {
        String c_val = null;
        boolean local_errors = false;
        if (tag_split != null && tag_split.length > 1) {
            if (!tag_split[0].endsWith("_TAGSOURCE")) {
                tag_split[0] = tag_split[0] + "_TAGSOURCE";
            }

            String tag_table = get_correct_table_name(tag_split[0]);
            if (tag_table != null) {
                if (tag_split.length == 2) {
                    HashMap<String, String> c_value_map = new HashMap<>();
                    c_value_map.put("name", tag_split[1]);
                    if (parent_request) {
                        int id = getOrAdd(getDatasource_data_update(), true,
                                false, st_1, tag_table, c_value_map, null,
                                new ArrayList<String>(), true, null);
                        if (id > 0) {
                            c_val = tag_table.split("\\.")[tag_table.split("\\.").length - 1] + "|ID=" + id;
                        } else {
                            msges = msges + " Error: can not tag with root element, must have atleast one parent " + c_val;
                            local_errors = true;
                        }
                    } else {
                        String hashval = getOrAdd_fileded(getDatasource_data_update(), true, false, st_1, tag_table, c_value_map, new ArrayList<String>(), true, "hash");
                        if (hashval != null) {
                            c_val = tag_table.split("\\.")[tag_table.split("\\.").length - 1] + "|HASH=" + hashval;
                        } else {
                            msges = msges + " Error: can not tag with root element, must have atleast one parent " + c_val;
                            local_errors = true;
                        }
                    }

                } else {
                    HashMap<String, String> c_value_map = new HashMap<>();
                    c_value_map.put("name", tag_split[1]);
                    c_value_map.put("parent_id", "0");
                    int parent_id = getOrAdd(getDatasource_data_update(), true,
                            false, st_1, tag_table, c_value_map, null,
                            new ArrayList<String>(), true, null);
                    if (parent_id == -1) {
                        c_value_map.clear();
                        c_value_map.put("name", tag_split[1]);
                        parent_id = getOrAdd(getDatasource_data_update(), true,
                                false, st_1, tag_table, c_value_map, null,
                                new ArrayList<String>(), true, null);
                    }
                    if (create && parent_id < 0) {
                        c_value_map.put("id", "(select max(id)+1 from " + get_correct_table_name(tag_table) + ")");
                        parent_id = getOrAdd(getDatasource_data_update(), false,
                                true, st_1, tag_table, c_value_map, null,
                                new ArrayList<String>(), true, null);
                    }
                    if (parent_id >= 0) {
                        for (int k = 2; (!local_errors && k < tag_split.length && parent_id >= 0); k++) {
                            c_value_map = new HashMap<>();
                            c_value_map.put("name", tag_split[k]);
                            c_value_map.put("parent_id", parent_id + "");
                            parent_id = getOrAdd(getDatasource_data_update(),
                                    true, false, st_1, tag_table, c_value_map,
                                    null, new ArrayList<String>(), true, null);
                            if (create && parent_id <= 0) {
                                c_value_map.put("id", "(select max(id)+1 from " + get_correct_table_name(tag_table) + ")");
                                parent_id = getOrAdd(getDatasource_data_update(), false, true, st_1, tag_table,
                                        c_value_map, null,
                                        new ArrayList<String>(), true, null);
                            }
                        }
                    } else {
                        local_errors = true;
                        msges = msges + "ERROR: parent not found " + tag_split[0];
                    }
                    if (parent_request) {
                        c_val = tag_table.split("\\.")[tag_table.split("\\.").length - 1] + "|PARENT_ID=" + parent_id;
                    } else {
                        c_value_map.clear();
                        c_value_map.put("id", parent_id + "");
                        String hashval = getOrAdd_fileded(getDatasource_data_update(), true, false, st_1, tag_table, c_value_map, new ArrayList<String>(), true, "hash");
                        if (hashval != null) {
                            c_val = tag_table.split("\\.")[tag_table.split("\\.").length - 1] + "|HASH=" + hashval;
                        } else {
                            msges = msges + " Error: can not tag with root element, must have atleast one parent " + c_val;
                            local_errors = true;
                        }
                    }


                }
            } else {
                msges = msges + " Error: tag source not defined. Define the source " + tag_split[0] + " before using it.";
                local_errors = true;
            }

        } else {
            msges = msges + " Error: incorrect tag format " + c_val;
            local_errors = true;
        }
        if (local_errors) {
            c_val = null;
        }
        return c_val;
    }

    /**
     * MethodID=AS34
     *
     */
    @WebMethod(exclude = true)
    public boolean getDoesitExistID(String table_nm, String id, Statement stm_1) {
        boolean result = false;
        try {
            String tale_to_use = get_correct_table_name(table_nm);
            if (tale_to_use != null) {
                tale_to_use = get_correct_table_name(tale_to_use);
                String q = "SELECT 1 FROM " + tale_to_use + " WHERE id=" + id;
                if (getOneForQuery(q, stm_1) != null) {
                    result = true;
                }
            }
        } catch (Exception ex) {
            msges = "Error AS34a: creating connections " + ex.getMessage();
        }
        return result;
    }

    /**
     * MethodID=AS35
     *
     */
    @WebMethod(exclude = true)
    public boolean getDoesitExistParam(String table_nm, String id, Statement stm_1) {
        boolean result = false;
        try {
            String tale_to_use = get_correct_table_name(table_nm);
            if (tale_to_use != null) {
                tale_to_use = get_correct_table_name(tale_to_use);
                String q = "SELECT 1 FROM " + tale_to_use + " WHERE " + id;
                if (getOneForQuery(q, stm_1) != null) {
                    result = true;
                }
            }
        } catch (Exception ex) {
            msges = "Error AS35a: creating connections " + ex.getMessage();
        }
        return result;
    }

    /**
     * MethodID=AS36
     *
     */
    @WebMethod(exclude = true)
    public boolean addToDataBase(String table_nm, String param, String conditions, Statement stm_1) {
        boolean result = false;
        try {
            String tale_to_use = get_correct_table_name(table_nm);
            if (tale_to_use != null) {
                String q = "Update " + tale_to_use + " SET " + param + " WHERE " + conditions;
                int rsult_int = stm_1.executeUpdate(q);
                if (rsult_int >= 0) {
                    result = true;
                }
            }
        } catch (Exception ex) {
            msges = "Error AS34a: creating connections " + ex.getMessage();
        }
        return result;
    }

    /**
     * MethodID=AS55
     *
     */
    @WebMethod(exclude = true)
    public String get_Correct_Column_name(String column_nm, DataSource dataSource) {
        if (column_nm.contains(".")) {
            String correct_column = null;
            String[] split_a = column_nm.split("\\.");
            String table = split_a[split_a.length - 2];
            String column = split_a[split_a.length - 1];
            table = get_correct_table_name(table);
            ArrayList<String> column_l = getColumns(table, dataSource);
            for (int i = 0; (i < column_l.size() && correct_column == null); i++) {
                if (column_l.get(i).equalsIgnoreCase(column)) {
                    correct_column = table + "." + column_l.get(i);
                }
            }
            return correct_column;
        } else {
            return null;
        }
    }

    /**
     * MethodID=AS37 This method obtains the correct table name (with schema) to
     * be used in queries. As the implementation is DBMS independent, This
     * method is important to avoid break when the DBMS used is very specific
     * about the way tables are referred to.
     */
    @WebMethod(exclude = true)
    public String get_correct_table_name(String in_name) {
        if (in_name != null) {
            in_name = in_name.toUpperCase();
            if (Constants.corect_tbl_nm_map == null || !Constants.corect_tbl_nm_map.containsKey(in_name)) {
                Constants.corect_tbl_nm_map = new HashMap<>();
                ArrayList<String> getTables = getTables();
                if (getTables.contains(in_name)) {
                    Constants.corect_tbl_nm_map.put(in_name, in_name);
                } else {
                    String tr_in_name = in_name.split("\\.")[in_name.split("\\.").length - 1];
                    for (int i = 0; i < getTables.size(); i++) {
                        String c_tbl = getTables.get(i).toUpperCase();
                        String striped_ctbl = c_tbl.split("\\.")[c_tbl.split("\\.").length - 1];
                        if (c_tbl.equals(tr_in_name)
                                || striped_ctbl.equals(tr_in_name)
                                || striped_ctbl.equals(tr_in_name + "S")
                                || (striped_ctbl + "S").equals(tr_in_name)) {
                            Constants.corect_tbl_nm_map.put(in_name, getTables.get(i));
                        }
                    }
                }

            }
            return Constants.corect_tbl_nm_map.get(in_name);
        }
        return null;

    }

    /**
     * MethodID=AS38
     *
     */
    @WebMethod(exclude = true)
    public boolean containsIgnoreCase(ArrayList<String> in_l, String matchit) {
        boolean found = false;
        if (in_l == null || in_l.isEmpty() || matchit == null || matchit.isEmpty() || matchit.trim().isEmpty()) {
        } else {
            try {
                for (int i = 0; (i < in_l.size() && !found); i++) {
                    String cval = in_l.get(i);
                    if (cval != null && cval.equalsIgnoreCase(matchit)) {
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
        }
        return found;
    }

    /**
     * MethodID=AS39
     *
     */
    @WebMethod(exclude = true)
    private ArrayList<ArrayList<String>> getpaths(String source, String target) {
        ArrayList<ArrayList<String>> result = new ArrayList<>(1);
        if (source.equals(target)) {
            ArrayList<String> tmp = new ArrayList<>(2);
            tmp.add(source);
            tmp.add(source);
            result.add(tmp);
        } else {
            getConnectionmap();
//            HashMap<String, HashMap<String, String[]>> relationship_map = new HashMap<String, HashMap<String, String[]>>();

            //Add all tagsource tables and connect them to totag tables manually
            PathFinder10 path = new PathFinder10();
//            System.out.println("2057 "+connection_map.keySet());
//            System.out.println("2058 source="+source+" target="+target);
            String source_key = getRealKey(connection_map.keySet(), source);
            String target_key = getRealKey(connection_map.keySet(), target);
            if (target_key != null && source_key != null) {
                result = path.getPaths(connection_map, getRealKey(connection_map.keySet(), source), getRealKey(connection_map.keySet(), target), 999);
            }
        }
//        System.out.println("2065 result="+result);
        return result;
    }

    /**
     * MethodID=AS40
     *
     */
    @WebMethod(exclude = true)
    private ArrayList<String> advancedQuaryHandler__create(Statement st, String source, String target,
            boolean onlyexactmatch, boolean childen_durng) {
//        System.out.println("2750 source=" + source + "\t target=" + target + "\tchilden_durng=" + childen_durng);
//        boolean split_lock=false;
//        if(source!=null && source.indexOf( SPLIT_BLOCK_FLAG)>1){
//           split_lock
//        }
        ArrayList<String> out_result_l = null;
        source = source.trim();
        if (target != null) {
            target = target.trim();
        }
        int error = -1;
//        String target_columns = null;
        ArrayList<String> target_tbl_l = new ArrayList<>();
        ArrayList<String> source_tbl_l = new ArrayList<>();
        ArrayList<String> source_columns_l = null;
        boolean orig_onlyexactmatch = onlyexactmatch;
        boolean negative = false;

        if (!source.isEmpty()) {
            String[] source_a = source.split("&&");
            for (int i = 0; (i < source_a.length && error < 0); i++) {
                String matcher = null;
                if (source_a[i].contains("!=")) {
                    matcher = "!=";
                    negative = true;
                } else if (source_a[i].contains("=>")) {
                    matcher = "=>";
                } else if (source_a[i].contains("<=")) {
                    matcher = "<=";
                } else if (source_a[i].contains("<")) {
                    matcher = "<";
                } else if (source_a[i].contains(">")) {
                    matcher = ">";
                } else if (source_a[i].contains("=")) {
                    matcher = "=";
                }
//                System.out.println("2854 matcher="+matcher);
                if (matcher != null) {
                    String[] col_val_a = source_a[i].split(matcher, 2);
                    String c_source_tbl;
                    String c_cor_col;
                    String intermediate_expanded_table = null;
                    if (col_val_a[0].contains(".")) {
                        String[] tbl_col_a = col_val_a[0].split("\\.");
//                        System.out.println("2832 "+Arrays.deepToString(tbl_col_a)+" "+tbl_col_a[tbl_col_a.length - 2]);
                        c_source_tbl = tbl_col_a[tbl_col_a.length - 2];
                        c_cor_col = tbl_col_a[tbl_col_a.length - 1];
                    } else {
                        c_source_tbl = col_val_a[0];
                        c_cor_col = "name";
                    }
//                    System.out.println("2839 "+c_source_tbl);
                    c_source_tbl = get_correct_table_name(c_source_tbl);
//                    System.out.println("2846 "+c_source_tbl);
                    if (c_source_tbl != null) {
                        if (childen_durng) {
                            intermediate_expanded_table = get_correct_table_name(c_source_tbl.split("2")[0]);
                        }
//                        System.out.println("2806 intermediate_expanded_table="+intermediate_expanded_table);
                        c_cor_col = get_Correct_Column_name(c_source_tbl + "." + c_cor_col, null);
                        if (c_cor_col != null) {
//                            source_tbl_l.remove(c_source_tbl);
//                            source_tbl_l.add(c_source_tbl);
                            String q = "'";
                            boolean isint = false;
                            onlyexactmatch = orig_onlyexactmatch;
                            if (!shoulditbequated(c_cor_col, getDatasource_data_view())) {
                                q = "";
                                onlyexactmatch = true;
                                isint = true;
                            }
                            String operator = " like ";
                            if (negative) {
                                operator = " not like ";
                            }
                            if (onlyexactmatch) {
                                if (negative) {
                                    operator = "!=";
                                } else {
                                    operator = "=";
                                }
                            }
//                            System.out.println("2875 " + col_val_a[1]);
                            String[] quary_split = col_val_a[1].split("\\|\\|");
                            ArrayList<String> modified_query_l = new ArrayList<>();
//                        if (quary_split.length > 1) {
//                            onlyexactmatch = true;
//                        }

                            if (quary_split[0] != null && !quary_split[0].isEmpty()) {
                                if (!onlyexactmatch) {
                                    quary_split[0] = quary_split[0].replaceAll("\\*", "%");
                                    quary_split[0] = quary_split[0].replaceAll("\\?", "_");

                                    if (quary_split[0].startsWith("^")) {
                                        quary_split[0] = quary_split[0].replace("^", "");
                                    } else if (!quary_split[0].startsWith("%")) {
                                        quary_split[0] = "%" + quary_split[0];
                                    }
                                    if (quary_split[0].endsWith("$")) {
                                        quary_split[0] = quary_split[0].replace("$", "");
                                    } else if (!quary_split[0].endsWith("%")) {
                                        quary_split[0] = quary_split[0] + "%";
                                    }
                                }
                                modified_query_l.add(quary_split[0]);
                            }
//                            System.out.println("2900 " + modified_query_l);
                            for (int k = 1; k < quary_split.length; k++) {
                                String c_query = quary_split[k].trim();
                                String c_matcher = null;
                                if (!c_cor_col.toUpperCase().endsWith(".LINK_TO_FEATURE")) {
//                                    System.out.println("2877 " + c_query + "\t  c_cor_col=" + c_cor_col);                                   
                                    if (c_query == null) {
                                    } else if (c_query.contains("!=")) {
                                        c_matcher = "!=";
//                                    negative = true;
                                    } else if (c_query.contains("=>")) {
                                        c_matcher = "=>";
                                    } else if (c_query.contains("<=")) {
                                        c_matcher = "<=";
                                    } else if (c_query.contains("<")) {
                                        c_matcher = "<";
                                    } else if (c_query.contains(">")) {
                                        c_matcher = ">";
                                    } else if (c_query.contains("=")) {
                                        c_matcher = "=";
                                    }
                                }
                                if (c_matcher != null && c_query != null) {
                                    String[] c_col_val_a = c_query.split(c_matcher, 2);
//                                    System.out.println("2898 "+Arrays.deepToString(c_col_val_a));
                                    if (c_col_val_a[0].contains(".")) {
                                        String[] tbl_col_a = c_col_val_a[0].split("\\.");
                                        c_source_tbl = tbl_col_a[tbl_col_a.length - 2];
                                        c_cor_col = tbl_col_a[tbl_col_a.length - 1];
                                    } else {
                                        c_source_tbl = c_col_val_a[0];
                                        c_cor_col = "name";
                                    }
                                    c_query = c_col_val_a[c_col_val_a.length - 1];
                                    c_source_tbl = get_correct_table_name(c_source_tbl);
                                }

//                                System.out.println("\t2937 " + c_query + " c_source_tbl =" + c_source_tbl);
                                if (c_source_tbl != null && c_query != null && !c_query.isEmpty()) {
                                    if (!onlyexactmatch) {
                                        c_query = c_query.replaceAll("\\*", "%");
                                        c_query = c_query.replaceAll("\\?", "_");

                                        if (c_query.startsWith("^")) {
                                            c_query = c_query.replace("^", "");
                                        } else if (!c_query.startsWith("%")) {
                                            c_query = "%" + c_query;
                                        }
                                        if (c_query.endsWith("$")) {
                                            c_query = c_query.replace("$", "");
                                        } else if (!c_query.endsWith("%")) {
                                            c_query = c_query + "%";
                                        }
                                    }
                                    modified_query_l.add(c_query);
                                }
                            }
//                            System.out.println("2893 " + modified_query_l);
                            StringBuffer modified_quary = new StringBuffer();
                            if (modified_query_l.size() == 1) {
                                if (isint && modified_query_l.get(0).equals("*")) {
                                    modified_quary.append("ALL");
                                } else if (isint) {
                                    modified_quary.append("(" + c_cor_col + matcher + modified_query_l.get(0) + ")");
                                } else {
                                    String c_operator = operator;
                                    if (!modified_query_l.get(0).contains("%")) {
                                        if (negative) {
                                            c_operator = "!=";
                                        } else {
                                            c_operator = "=";
                                        }
                                    }
                                    modified_quary.append("(" + c_cor_col + c_operator + q + modified_query_l.get(0) + q + ")");
//                                    System.out.println("2947 "+modified_quary);
                                }
                            } else if (modified_query_l.size() > 1 && !modified_quary.equals("ALL")) {
                                if (onlyexactmatch) {
                                    StringBuilder tmp_q = new StringBuilder();
                                    tmp_q.append(c_cor_col + " in (" + q + modified_query_l.get(0) + q);
                                    for (int l = 1; l < modified_query_l.size(); l++) {
                                        tmp_q.append("," + q + modified_query_l.get(l) + q);
                                    }
                                    modified_quary.append(tmp_q.toString() + ")");

                                } else {
                                    String c_operator = operator;
                                    if (!modified_query_l.get(0).contains("%")) {
                                        if (negative) {
                                            c_operator = "!=";
                                        } else {
                                            c_operator = "=";
                                        }
                                    }
                                    String tmp_q = c_cor_col + " " + c_operator + " " + q + modified_query_l.get(0) + q;
                                    for (int l = 1; l < modified_query_l.size(); l++) {
                                        if (!modified_query_l.get(l).contains("%")) {
                                            c_operator = "=";
                                        }
                                        tmp_q = tmp_q + " OR " + c_cor_col + " " + c_operator + " " + q + modified_query_l.get(l) + q;
                                    }
                                    modified_quary.append(tmp_q);
                                }
                            } else if (!modified_quary.equals("ALL")) {
//                                System.out.println("2975");
                                out_result_l = new ArrayList<>(1);
                                out_result_l.add("SELECT message FROM " + get_correct_table_name("errormsgs") + " WHERE ID=4");
//                                out_result = "SELECT message FROM " + get_correct_table_name("errormsgs") + " WHERE ID=4";
                            } else {
//                                System.out.println("2979");
                            }

                            if (!modified_quary.toString().equals("ALL")) {
                                if (source_columns_l == null || source_columns_l.isEmpty()) {
                                    source_columns_l = new ArrayList<>();
                                    source_columns_l.add(modified_quary.toString());
                                } else {
                                    source_columns_l.add(source_columns_l.remove(0) + " AND " + modified_quary);
                                }
                            }
//                            System.out.println("2974 source_columns_l=" + source_columns_l);
//                            2930 select ID from EGEN_DATAENTRY.FILES2TAGS where EGEN_DATAENTRY.FILES2TAGS.LINK_TO_FEATURE in ('DISEASE_ONTOLOGY_TAGSOURCE|HASH=bda1fc4f5ab82e2f9215397101ad41327b629c90','HUMAN_DISEASE_ONTOLOGY_TAGSOURCE|HASH=f2a5e5b634bfd926d04b157eb93a6c1b9400091a')

//                            System.out.println("2914 c_source_tbl=" + c_source_tbl);
//                            System.out.println("3912 childen_durng="+childen_durng +"  intermediate_expanded_table="+intermediate_expanded_table+" source_columns_l="+source_columns_l);
                            if (c_source_tbl != null && childen_durng && intermediate_expanded_table != null && !source_columns_l.isEmpty()) {
                                String hierarchi_table = get_correct_table_name(intermediate_expanded_table + "_HIERARCHY");
//                                System.out.println("2916 hierarchi_table=" + hierarchi_table + "\tsource_columns_l=" + source_columns_l);
                                if (hierarchi_table != null) {
                                    if (c_source_tbl.indexOf("2") > 0) {
                                        HashMap<String, String[]> key_constraint_map = get_key_contraints(hierarchi_table, getDatasource_data_view());
                                        if (key_constraint_map.containsKey(Constants.PARENT_CHILD_KEY_COLUMNS)) {
                                            String[] col_nm = key_constraint_map.get(Constants.PARENT_CHILD_KEY_COLUMNS);
                                            HashMap<String, String[]> source_key_constraint_map = get_key_contraints(c_source_tbl, getDatasource_data_view());
//                                        System.out.println("2923 " + source_key_constraint_map.keySet() + "  " + c_source_tbl);
//                                        System.out.println("2924  " + source_key_constraint_map.containsKey((Constants.FOREIGN_KEY_COLUMNS_FLAG)));
                                            if (source_key_constraint_map.containsKey((Constants.FOREIGN_KEY_COLUMNS_FLAG))) {
                                                String sql = "select " + source_key_constraint_map.get(Constants.FOREIGN_KEY_COLUMNS_FLAG)[0] + "," + source_key_constraint_map.get(Constants.FOREIGN_KEY_COLUMNS_FLAG)[0] + " from " + c_source_tbl + " where " + source_columns_l.remove(0);
//                                                System.out.println("2931 " + sql);
                                                ArrayList<Integer> all_id_l = get_children_hierarchy(st, hierarchi_table, sql, col_nm[0], col_nm[1]);
                                                if (!all_id_l.isEmpty()) {
                                                    int limit = 50;
                                                    while (!all_id_l.isEmpty()) {
                                                        ArrayList<Integer> c_id_l = new ArrayList<>();
                                                        for (int j = 0; (!all_id_l.isEmpty() && j < limit); j++) {
                                                            c_id_l.add(all_id_l.remove(0));
                                                        }
                                                        if (!c_id_l.isEmpty()) {
                                                            source_columns_l.add(intermediate_expanded_table + ".ID IN (" + c_id_l.toString().replace("[", "").replace("]", "").replaceAll("\\s", "") + ")");
                                                        }
                                                    }
                                                }
                                                source_tbl_l.remove(intermediate_expanded_table);
                                                source_tbl_l.add(intermediate_expanded_table);
                                            }
                                        }
                                    } else {
                                        HashMap<String, String[]> key_constraint_map = get_key_contraints(hierarchi_table, getDatasource_data_view());
//                                    System.out.println("2919 key_constraint_map=" + key_constraint_map.keySet());
                                        if (key_constraint_map.containsKey(Constants.PARENT_CHILD_KEY_COLUMNS)) {
                                            String[] col_nm = key_constraint_map.get(Constants.PARENT_CHILD_KEY_COLUMNS);
//                                        HashMap<String, String[]> source_key_constraint_map = get_key_contraints(c_source_tbl, getDatasource_data_view());
//                                        System.out.println("2923 " + source_key_constraint_map.keySet() + "  " + c_source_tbl);
//                                        System.out.println("2924  " + source_key_constraint_map.containsKey((Constants.FOREIGN_KEY_COLUMNS_FLAG)));
//                                        if (source_key_constraint_map.containsKey((Constants.FOREIGN_KEY_COLUMNS_FLAG))) {

//                                           String sql = "select " + source_key_constraint_map.get(Constants.FOREIGN_KEY_COLUMNS_FLAG)[0] + "," + source_key_constraint_map.get(Constants.FOREIGN_KEY_COLUMNS_FLAG)[0] + " from " + c_source_tbl + " where " + source_columns_l.remove(0);

//                                        System.out.println("2929 "+source_columns_l);

                                            String sql = "select ID from " + c_source_tbl + " where " + source_columns_l.remove(0);
//                                            System.out.println("2965 " + sql);
                                            ArrayList<Integer> all_id_l = get_children_hierarchy(st, hierarchi_table, sql, col_nm[0], col_nm[1]);
                                            if (!all_id_l.isEmpty()) {
                                                int limit = 50;
                                                while (!all_id_l.isEmpty()) {
                                                    ArrayList<Integer> c_id_l = new ArrayList<>();
                                                    for (int j = 0; (!all_id_l.isEmpty() && j < limit); j++) {
                                                        c_id_l.add(all_id_l.remove(0));
                                                    }
                                                    if (!c_id_l.isEmpty()) {
                                                        source_columns_l.add(intermediate_expanded_table + ".ID IN (" + c_id_l.toString().replace("[", "").replace("]", "").replaceAll("\\s", "") + ")");
                                                    }
                                                }
                                            }
                                            source_tbl_l.remove(intermediate_expanded_table);
                                            source_tbl_l.add(intermediate_expanded_table);
//                                        }
//                                        System.out.println("2587 " + source_columns);
//                                        System.out.println("2581 " + intermediate_expanded_table + "=>" + all_id_l);
                                        }
                                    }

                                } else {
//                                    System.out.println("3099 "+c_source_tbl);
                                    source_tbl_l.remove(c_source_tbl);
                                    source_tbl_l.add(c_source_tbl);
                                }
                            } else {
//                                System.out.println("3098 "+c_source_tbl);
                                source_tbl_l.remove(c_source_tbl);
                                source_tbl_l.add(c_source_tbl);
                            }
                        } else {
                            error = 5;
                            out_result_l = new ArrayList<>(1);
                            out_result_l.add("SELECT 'invalid table in  " + source_a[i] + ", use -avaialble to see what can be searched' FROM "
                                    + get_correct_table_name("errormsgs") + " WHERE ID=1");
//                            source_columns = " WHERE " + get_Correct_Column_name(c_source[c_source.length - 2] + "." + col_val_a, null);
                        }
                    } else {
                        error = 9;
                        out_result_l = new ArrayList<>(1);
                        out_result_l.add("SELECT MESSAGE FROM " + get_correct_table_name("errormsgs") + " WHERE ID=10");
//                        out_result_l.add("SELECT 'invalid column in " + source_a[i] + ", use -avaialble to see what can be searched' FROM "
//                                + get_correct_table_name("errormsgs") + " WHERE ID=1");
                    }
//                    } else {
//                        error = 6;
//                    }
                } else {
                    error = 7;
                }
            }
        }
//        System.out.println("3086 target="+target+"\ttarget_tbl_l="+target_tbl_l+"\tsource_tbl_l="+source_tbl_l);


        if (target != null && (target.toUpperCase().startsWith(TAG_FLAG) || target.toUpperCase().startsWith(TAG_FLAG2))) {
            if (source_tbl_l.size() == 1) {
                String target_columns = "*";
                int dot_ind = target.indexOf(".");
                if (dot_ind > 2) {
                    target_columns = target.substring(dot_ind + 1);
                }
                String tag_tble = get_correct_table_name(source_tbl_l.get(0) + "2TAGS");
                if (tag_tble != null) {
                    HashMap<String, String[]> constr_map = get_key_contraints(tag_tble, source_tbl_l.get(0), null, getDatasource_data_view());
//                    System.out.println("3063 "+constr_map.keySet()+"  "+source_columns_l);
                    if (error < 0 && source_columns_l != null && !constr_map.isEmpty()) {
                        for (int i = 0; i < source_columns_l.size(); i++) {
//                            System.out.println("3071 " + source_columns_l.get(i));
                            String[] source_columns_a = source_columns_l.get(i).split(" AND ");
                            String final_q = null;
                            if (source_columns_a[0].equals("ALL")) {
                                final_q = "select " + target_columns + " from " + tag_tble + " where " + tag_tble + "."
                                        + constr_map.keySet().iterator().next() + " in (from SELECT id FROM " + source_tbl_l.get(0) + ")";
                                if (out_result_l == null) {
                                    out_result_l = new ArrayList<>(1);
                                }
                                out_result_l.add(final_q);
                            } else if (source_columns_a.length > 1) {
                                String intemed_q = "select LINK_TO_FEATURE from " + tag_tble + " where " + tag_tble + "."
                                        + constr_map.keySet().iterator().next() + " in (SELECT id FROM " + source_tbl_l.get(0) + " WHERE " + source_columns_a[0] + ")";
                                ArrayList<String> final_list = getforQuery(intemed_q, true, st, "AS49", getQueryTimeOut(), null);
                                for (int j = 1; (j < source_columns_a.length && !final_list.isEmpty()); j++) {
                                    intemed_q = "select LINK_TO_FEATURE from " + tag_tble + " where " + tag_tble + "."
                                            + constr_map.keySet().iterator().next() + " in (SELECT id FROM " + source_tbl_l.get(0) + " WHERE " + source_columns_a[j] + ")";
//                                   ArrayList<String> tmp=getforQuery(intemed_q, true, st, "AS49", getQueryTimeOut(), null);
                                    final_list.retainAll(getforQuery(intemed_q, true, st, "AS49", getQueryTimeOut(), null));
//                                    System.out.println("3084 " + final_list);
                                }
                                if (out_result_l == null) {
                                    out_result_l = new ArrayList<>(final_list.size());
                                }
                                for (int j = 0; j < final_list.size(); j++) {
                                    if (final_list.get(j) != null) {
                                        out_result_l.add(0, "SELECT * FROM EGEN_DATAENTRY." + final_list.get(j).replace("|HASH=", " where HASH='") + "'");
                                    }
                                }
                            } else {
//                                if (source_columns_l.get(i).contains(" OR ")) {
                                String intemed_q = "select LINK_TO_FEATURE from " + tag_tble + " where " + tag_tble + "." + constr_map.keySet().iterator().next() + " in (SELECT id FROM " + source_tbl_l.get(0) + " WHERE " + source_columns_l.get(i) + ")";
//                                    System.out.println("3098 " +intemed_q);
                                ArrayList<String> final_list = getforQuery(intemed_q, true, st, "AS49", getQueryTimeOut(), null);
                                if (out_result_l == null) {
                                    out_result_l = new ArrayList<>(final_list.size());
                                }
                                for (int j = 0; j < final_list.size(); j++) {
//                                        System.out.println("3105 "+final_list.get(j));
                                    if (final_list.get(j) != null) {
                                        out_result_l.add("SELECT * FROM EGEN_DATAENTRY." + final_list.get(j).replace("|HASH=", " where HASH='") + "'");
                                    }
                                }
//                                }else{
//                                    
//                                }


//                            
                            }
                        }
                    } else if (out_result_l == null) {
                        out_result_l = new ArrayList<>(1);
                        out_result_l.add("SELECT message FROM " + get_correct_table_name("errormsgs") + " WHERE ID=2");
                    }
                }

//                String target_columns = null;
//                if (error < 0 && source_columns_l != null) {
//                    for (int i = 0; i < source_columns_l.size(); i++) {
//                        String source_columns = source_columns_l.get(i);
//                        String final_q = null;
//                        if (source_columns.equals("ALL")) {
//                            final_q = "SELECT " + target_columns + " FROM " + path_l.get(0);
//                        } else {
//                            final_q = "SELECT " + target_columns + " FROM " + path_l.get(0) + " WHERE " + source_columns;
//                        }
//                        if (out_result_l == null) {
//                            out_result_l = new ArrayList<>(1);
//                        }
//                        out_result_l.add(final_q);
//                    }
//                } else if (out_result_l == null) {
//                    out_result_l = new ArrayList<>(1);
//                    out_result_l.add("SELECT message FROM " + get_correct_table_name("errormsgs") + " WHERE ID=2");
//                }
            }

        } else {
            String target_columns = getTargetColumns(target, target_tbl_l, source_tbl_l);
            if (target_columns == null) {
                error = 9;
                if (out_result_l == null) {
                    out_result_l = new ArrayList<>(1);
                }
                out_result_l.add("SELECT 'invalid target column' FROM " + get_correct_table_name("errormsgs") + " WHERE ID=1");
            }
//        System.out.println("3087 "+target_columns);
//            System.out.println("3226 " + source_tbl_l);

            target_tbl_l.removeAll(source_tbl_l);
            target_tbl_l.addAll(source_tbl_l);

            ArrayList<String> path_l = new ArrayList<>();
            if (target_tbl_l.size() == 1) {
                path_l.add(target_tbl_l.get(0));
            } else {
                for (int i = 0; i < target_tbl_l.size(); i++) {
                    String tb_i = target_tbl_l.get(i);
                    for (int j = i; j < target_tbl_l.size(); j++) {
                        String tb_j = target_tbl_l.get(j);
                        if (!tb_i.equals(tb_j)) {
//                        System.out.println("\n2979 tb_i="+tb_i+"\ttb_j="+tb_j);
                            ArrayList<ArrayList<String>> result = getpaths(tb_i, tb_j);
                            if (result == null || result.isEmpty()) {
                                error = 8;
                                //No path ditected, retun a better warning
                            } else {
                                path_l.removeAll(result.get(0));
                                path_l.addAll(result.get(0));
                            }
                        }
                    }
                }
            }
//        System.out.println("2802 source_columns_l=" + source_columns_l + " error=" + error);
            if (error < 0 && source_columns_l != null) {
                for (int i = 0; i < source_columns_l.size(); i++) {
                    String source_columns = source_columns_l.get(i);
//                System.out.println("3093 "+source_columns );
                    String final_q = null;
                    if (path_l.size() == 1) {
                        if (source_columns.equals("ALL")) {
                            final_q = "SELECT " + target_columns + " FROM " + path_l.get(0);
                        } else {
                            final_q = "SELECT " + target_columns + " FROM " + path_l.get(0) + " WHERE " + source_columns;
                        }
                    } else {
                        final_q = construct_Path_query(path_l, target_columns, source_columns);
                    }
                    if (out_result_l == null) {
                        out_result_l = new ArrayList<>(1);
                    }
//                System.out.println("3127 "+final_q);
                    out_result_l.add(final_q);

                }



//            out_result = final_q;
//            System.out.println("2643 " + out_result_l);
            } else if (out_result_l == null) {
                out_result_l = new ArrayList<>(1);
                out_result_l.add("SELECT message FROM " + get_correct_table_name("errormsgs") + " WHERE ID=2");
            }
        }
//        HashMap<String, HashMap<String, String>> r_map = get_connecting_tables(target_tbl_l, getDatasource_data());
//        System.out.println("2596 r_map=" + r_map);

//        get_connecting_tables(
//        ArrayList<ArrayList<String>> result = getpaths(source_tble, target_tble);
//        System.out.println("2555 target_columns=" + target_columns);
//        System.out.println("2555 source_columns=" + source_columns);
//        System.out.println("2641 path_l=" + path_l);
//        System.out.println("2642 error=" + error+"\n");


//        for (int i = 0; i < out_result_l.size(); i++) {
//            System.out.println("3156 " + out_result_l.get(i));
//
//        }
        return out_result_l;
    }

    /**
     * MethodID=AS40.1
     *
     */
    @WebMethod(exclude = true)
    private String getTargetColumns(String target, ArrayList<String> target_tbl_l, ArrayList<String> source_tbl_l) {
        if (query_adjective_l == null) {
            query_adjective_l = new ArrayList<>();
            query_adjective_l.add("COUNT");
            query_adjective_l.add("MIN");
            query_adjective_l.add("MAX");
        }
        String target_columns = null;
        int error = -1;

        if (target == null || target.isEmpty() || target.equals("*") || !target.contains(".")) {
            ArrayList<String> targte_tbl = new ArrayList<>();
            if (target == null || target.isEmpty() || target.equals("*")) {
            } else {
                String[] tagtet_split_a = target.split(",");
                for (int i = 0; i < tagtet_split_a.length; i++) {
                    String c_tbl = get_correct_table_name(tagtet_split_a[i]);
                    if (c_tbl != null && !targte_tbl.contains(c_tbl)) {
                        targte_tbl.add(c_tbl);
                    }
                }
            }
            error = -1;
            if (targte_tbl.isEmpty()) {
                for (int i = 0; i < source_tbl_l.size(); i++) {
                    String c_s = source_tbl_l.get(i);
                    ArrayList<String> col_l = getColumns(c_s, getDatasource_data_view());
                    col_l.add("ID");
                    for (int j = 0; j < col_l.size(); j++) {
                        if (target_columns == null) {
                            target_columns = c_s + "." + col_l.get(j);
                        } else {
                            target_columns = target_columns + "," + c_s + "." + col_l.get(j);
                        }
                    }
                }
            } else {
                source_tbl_l.removeAll(targte_tbl);
                source_tbl_l.addAll(targte_tbl);
                for (int i = 0; i < targte_tbl.size(); i++) {
                    String c_s = targte_tbl.get(i);
                    ArrayList<String> col_l = getColumns(c_s, getDatasource_data_view());
                    col_l.add("ID");
                    for (int j = 0; j < col_l.size(); j++) {
                        if (target_columns == null) {
                            target_columns = c_s + "." + col_l.get(j);
                        } else {
                            target_columns = target_columns + "," + c_s + "." + col_l.get(j);
                        }
                    }
                }
            }

//            target_columns = "*";
//        } else if (target.toUpperCase().startsWith(SEARCH_CHILDREN_FLAG)) {

//            String[] colnm_a = target.split("\\.");
//            if (colnm_a.length == 1) {
//            } else {
//                target_columns ="ID";
//                for (int i = 1; i < colnm_a.length; i++) {
//                    target_columns = target_columns + "," + colnm_a[i];
//                }
//            }
        } else {
            String[] target_a = null;
            if (target.contains("&")) {
                target_a = target.split("&&");
            } else {
                target_a = target.split(",");
            }
            ArrayList<String> target_column_l = new ArrayList(Arrays.asList(target_a));
//            ArrayList<String> id_col_l = new ArrayList<>();
            for (int i = 0; (i < target_column_l.size() && error < 0); i++) {
                String[] c_target_a = target_column_l.get(i).split("\\.");
                if (c_target_a.length == 1) {
                    String c_tbl = get_correct_table_name(c_target_a[0]);
                    if (c_tbl != null) {
                        if (!target_tbl_l.contains(c_tbl)) {
                            target_tbl_l.add(c_tbl);
                        }
                        ArrayList<String> c_col_l = getColumns(c_tbl, getDatasource_data_view());
                        for (int j = 0; j < c_col_l.size(); j++) {
                            if (target_columns == null) {
                                target_columns = c_tbl + "." + c_col_l.get(j);
                            } else {
                                target_columns = target_columns + "," + c_tbl + "." + c_col_l.get(j);
                            }
                        }
                    } else {
                        error = 1;
                    }
                } else {
                    String c_tbl = get_correct_table_name(c_target_a[c_target_a.length - 2]);
                    String id_col = get_Correct_Column_name(c_tbl + ".ID", null);
                    if (!target_column_l.contains(id_col)) {
//                        id_col_l.add(id_col);
                        target_column_l.add(id_col);
                    }
                    if (c_tbl != null) {
                        if (!target_tbl_l.contains(c_tbl)) {
                            target_tbl_l.add(c_tbl);
                        }
                        String[] c_col_a = c_target_a[c_target_a.length - 1].split("\\s");
                        String adjective = null;
                        String barketed_adjective = null;
                        if (c_col_a.length == 2) {
                            if (query_adjective_l.contains(c_col_a[0].toUpperCase())) {
                                barketed_adjective = c_col_a[0];
                            } else {
                                adjective = c_col_a[0];
                            }
                        }
                        String cor_col = get_Correct_Column_name(c_tbl + "." + c_col_a[c_col_a.length - 1], null);
                        if (cor_col != null) {
                            if (cor_col.endsWith(id_col)) {
                                id_col = null;
                            }
                            if (target_columns == null) {
                                if (adjective != null) {
                                    target_columns = adjective + " " + cor_col;
                                } else if (barketed_adjective != null) {
                                    target_columns = barketed_adjective + " (" + cor_col + ")";
                                } else {
                                    target_columns = cor_col;
                                }
                            } else {
                                String c_target_columns = null;
                                if (adjective != null) {
                                    c_target_columns = adjective + " " + cor_col;
                                } else if (barketed_adjective != null) {
                                    c_target_columns = barketed_adjective + " (" + cor_col + ")";
                                } else {
                                    c_target_columns = cor_col;
                                }
                                target_columns = target_columns + "," + c_target_columns;
                            }
                        } else {
                            error = 3;
                        }
                    } else {
                        error = 4;
                    }
                }
            }
        }

        return target_columns;
    }

    /**
     * MethodID=AS41
     *
     */
    @WebMethod(exclude = true)
    private String advancedQuaryHandler__createMinMax(String source) {
        String out_result = "SELECT message FROM " + get_correct_table_name("errormsgs") + "  WHERE ID=2";
        source = source.trim();
        String source_tbl = source;
        String source_clm = "id";
        String function = "min";
        String[] source_split = source.split("\\.");
        if (source_split.length == 3) {
            source_tbl = source_split[0];
            source_clm = source_split[1];
            function = source_split[2];
            out_result = "select " + function + " (" + source_clm + ") from " + get_correct_table_name(source_tbl);
        }
        return out_result;
    }

    /**
     * MethodID=AS66.1 This method is used to handle tag based searches.
     *
     * @source is the tag. This could take several forms e.g. TAG="ARC syndrome"
     * - Search all tag sources for the term
     * TAG="Human_Disease_Ontology/disease/syndrome/ARC syndrome" - search the
     * Human_Disease_Ontology_TAGSOURCE the term syndrome, then the term ARC
     * syndrome among its children
     * TAG.name="Human_Disease_Ontology/disease/syndrome/ARC syndrome" TAG.URI=
     * "http://purl.obolibrary.org/obo/DOID_225"
     *
     * Test: -exact -search "TAG.NAME=syndrome" -exact -search
     * "TAG.NAME=syndrome" files -exact -search -children_before
     * "TAG.NAME=syndrome" files -children_before -search
     * "TAG=Human_Disease_Ontology/disease/syndrome/" files -children_before
     * -search "TAG=Human_Disease_Ontology/disease/syndrome/" -search
     * "TAG=disease/syndrome/ARC syndrome" -search "TAG=/disease/syndrome/"
     *
     */
    @WebMethod(exclude = true)
    private ArrayList<String> getMatchingTag(DataSource dtsrc, String source, String in_target,
            boolean exact, boolean childen_before, boolean childen_during,
            String username, boolean checkifexists,
            boolean exapandforeignKeys, boolean addcolumnnames,
            boolean childen_at_the_end, boolean parent_at_the_end) {
//        System.out.println("3283 source=" + source + "\tin_target=" + in_target);
        ArrayList<String> final_sql_l = new ArrayList<>();
        ArrayList<String> curnt_tag_connection_tables_to_search_l = new ArrayList<>();
        Connection ncon = null;
        Statement st = null;
        int error = 0;

//        ArrayList<String> tagsource_tables_l = new ArrayList<>();
        HashMap<String, String> tables_toCol = new HashMap<>();
//        HashMap<String, String> tagable_to_tagged_map = new HashMap<>();
        if (tagable_tables_l == null) {
            all_tagsource_tables_l = new ArrayList<>();
            all_tagable_to_tagged_map = new HashMap<>();
            tag_connection_tables_to_search_l = new ArrayList<>();
            ArrayList<String> all_tbl_l = getTables();
            tagable_tables_l = new ArrayList<>();
            for (int i = 0; i < all_tbl_l.size(); i++) {
                if (all_tbl_l.get(i).endsWith(TAGSOURCE_FLAG)) {
                    all_tagsource_tables_l.add(all_tbl_l.get(i));
                }
                if (all_tbl_l.get(i).endsWith(TO_TAG_FLAG)) {
                    tag_connection_tables_to_search_l.add(all_tbl_l.get(i));
                    String c_tb = get_correct_table_name(all_tbl_l.get(i).replace(TO_TAG_FLAG, ""));
                    if (c_tb != null) {
                        tagable_tables_l.add(c_tb);
                        all_tagable_to_tagged_map.put(c_tb, all_tbl_l.get(i));
                    }
                }
            }
        } else if (all_tagsource_tables_l == null || all_tagable_to_tagged_map == null) {
            all_tagsource_tables_l = new ArrayList<>();
            all_tagable_to_tagged_map = new HashMap<>();
            ArrayList<String> all_tbl_l = getTables();
            for (int i = 0; i < all_tbl_l.size(); i++) {
                if (all_tbl_l.get(i).endsWith(TAGSOURCE_FLAG)) {
                    all_tagsource_tables_l.add(all_tbl_l.get(i));
                }
                if (all_tbl_l.get(i).endsWith(TO_TAG_FLAG)) {
                    curnt_tag_connection_tables_to_search_l.add(all_tbl_l.get(i));
                    String c_tb = get_correct_table_name(all_tbl_l.get(i).replace(TO_TAG_FLAG, ""));
                    if (c_tb != null) {
                        tagable_tables_l.add(c_tb);
                        all_tagable_to_tagged_map.put(c_tb, all_tbl_l.get(i));
                    }
                }
            }
        }
        ArrayList<String> currnt_tagsource_tables_l = new ArrayList<>();
        currnt_tagsource_tables_l.addAll(all_tagsource_tables_l);
        HashMap<String, String> currnt_tagable_to_tagged_map = new HashMap<>();
        currnt_tagable_to_tagged_map.putAll(all_tagable_to_tagged_map);
        curnt_tag_connection_tables_to_search_l.addAll(tag_connection_tables_to_search_l);
        String[] targte_info_a = process_result_request(in_target,
                curnt_tag_connection_tables_to_search_l, currnt_tagable_to_tagged_map);
        String tagged_table = targte_info_a[0];
        String final_result_tbl = targte_info_a[1];
        String final_target_column = targte_info_a[2];
//        String additional_instruct = targte_info_a[3];
//        System.out.println("\n3178 " + Arrays.deepToString(targte_info_a) + " " + tag_connection_tables_to_search_l);
        if (final_target_column != null) {
            try {
                String matcher = null;
                boolean numeric_request = true;
                int matcher_type = 0;
                int min_match = Integer.MAX_VALUE;
                ArrayList<String> match_key_l = new ArrayList<>();
                match_key_l.add("=");
                match_key_l.add("!=");
                match_key_l.add(">");
                match_key_l.add("<");
                match_key_l.add(">=");
                match_key_l.add("<=");
                for (int i = 0; i < match_key_l.size(); i++) {
                    int c_index = source.indexOf(match_key_l.get(i));
                    /*if it is =, > or < add 3 (so if the split 
                     * really is >= then it is compansated)*/
                    if (c_index > 0 && (i == 0 || i == 2 || i == 3)) {
                        c_index = c_index + 1;
                    }
                    /*Consider only the fist split when if 
                     * there are more, so the search query it 
                     * elf can have this split characters*/
                    if (c_index > 0 && c_index < min_match) {
                        min_match = c_index;
                        matcher = match_key_l.get(i);
                        matcher_type = i;
                    }
                }
                if (matcher_type <= 1) {/*use string matching for = and !=*/
                    numeric_request = false;
                }
                /*
                 The search type and search column provided
                 * e.g. tag.uri=/disease/cancer
                 */
                String[] source_split_a = source.split(matcher, 2);
                String tag_column = "NAME";
                String[] source_0_split_a = source_split_a[0].split("\\.");
                if (source_0_split_a.length > 1) {
                    tag_column = source_0_split_a[source_0_split_a.length - 1];
                }
//                int col_ind = source_split_a[0].lastIndexOf(".");
//                if (col_ind > 0) {
//                    tag_column = source_split_a[0].substring(col_ind + 1);
//                }
                tag_column = tag_column.toUpperCase();
                HashSet<String> sub_parent_tag_vals_s = new HashSet<>();
                /*
                 * A search type and term provided e.g tag=cancer
                 */
                if (source_split_a.length == 2) {
                    ncon = dtsrc.getConnection();
                    st = ncon.createStatement();
                    source_split_a[1] = source_split_a[1].trim();
//                    boolean ok = true;
                    HashSet<String> parent_tag_vals_s = null;
                    String query = null;
                    if (tag_column.equals("NAME")) {
                        String[] query_split_a = source_split_a[1].split("/");
                        if (query_split_a[0].trim().isEmpty()) {
                            query_split_a = Arrays.copyOfRange(query_split_a, 1, query_split_a.length);
                        }
                        if (query_split_a.length > 2) {
                            String tag_source_tbl = query_split_a[0].toUpperCase();
                            if (!tag_source_tbl.endsWith("_TAGSOURCE")) {
                                tag_source_tbl = tag_source_tbl + "_TAGSOURCE";
                            }
                            tag_source_tbl = get_correct_table_name(tag_source_tbl);
                            if (tag_source_tbl != null) {
                                query_split_a[0] = tag_source_tbl;
                            }
                            if (childen_before) {
                                parent_tag_vals_s = getParentFromURI(st, query_split_a, currnt_tagsource_tables_l, true);
                                query = query_split_a[query_split_a.length - 1];
                            } else {
                                String[] parents_a = new String[query_split_a.length - 1];
                                System.arraycopy(query_split_a, 0, parents_a, 0, parents_a.length);
                                parent_tag_vals_s = getParentFromURI(st, parents_a, currnt_tagsource_tables_l, true);
                                query = query_split_a[query_split_a.length - 1];
                            }
                        } else if (query_split_a.length == 2) {
                            String tag_source_tbl = query_split_a[0].toUpperCase();
                            if (!tag_source_tbl.endsWith("_TAGSOURCE")) {
                                tag_source_tbl = tag_source_tbl + "_TAGSOURCE";
                            }
                            tag_source_tbl = get_correct_table_name(tag_source_tbl);
                            if (tag_source_tbl != null) {
                                query = query_split_a[1];
                                query_split_a[0] = tag_source_tbl;
                                currnt_tagsource_tables_l.clear();
                                currnt_tagsource_tables_l.add(query_split_a[0]);
                                if (exact) {
                                    sub_parent_tag_vals_s.add("SELECT ID,PARENT_ID,HASH FROM " + tag_source_tbl + " WHERE NAME ='" + query_split_a[1] + "'");
                                } else {
                                    sub_parent_tag_vals_s.add("SELECT ID,PARENT_ID,HASH FROM " + tag_source_tbl + " WHERE NAME  like '%" + query_split_a[1] + "%'");
                                }
                            } else {
                                query = query_split_a[1];
                                if (parent_tag_vals_s == null) {
                                    parent_tag_vals_s = new HashSet<>();
                                }
                                for (int i = 0; i < currnt_tagsource_tables_l.size(); i++) {
                                    String sql = "select id from " + currnt_tagsource_tables_l.get(i) + " where name='" + query_split_a[0] + "'";

                                    ArrayList<String> id_l = getforQuery(sql, true, st, "tagger", getQueryTimeOut(), username);
                                    for (int j = 0; j < id_l.size(); j++) {
                                        sub_parent_tag_vals_s.add("select id,parent_id,hash from " + currnt_tagsource_tables_l.get(i) + " where PARENT_ID=" + id_l.get(j));

                                    }
                                }
                            }
                        } else {
//                            System.out.println("3369 " + source_split_a[1]);
                            query = source_split_a[1];
                            parent_tag_vals_s = findParentForTerm(st, currnt_tagsource_tables_l, "NAME", source_split_a[1], exact, childen_before);
                        }
                    } else if (tag_column.equals("URI")) {
                        exact = true;
                        int tagspurce_ind = source_split_a[1].indexOf("/");
                        if (tagspurce_ind == 0) {
                            source_split_a[1] = source_split_a[1].replaceFirst("/", "");
                            tagspurce_ind = source_split_a[1].indexOf("/");
                        }
                        if (tagspurce_ind > 0) {
                            String tagsource = get_correct_table_name(source_split_a[1].substring(0, tagspurce_ind));
                            if (tagsource != null && currnt_tagsource_tables_l.contains(tagsource)) {
                                currnt_tagsource_tables_l.clear();
                                currnt_tagsource_tables_l.add(tagsource);
                                query = source_split_a[1].substring(tagspurce_ind + 1);
                            } else {
                                query = source_split_a[1];
                            }

                        } else {
                            query = source_split_a[1];
                        }
                        parent_tag_vals_s = findParentForTerm(st, currnt_tagsource_tables_l, "URI", query, exact, childen_before);
                    } else {
                        if (tag_column.equals("HASH")) {
                            exact = true;
                        }
                        if (source_0_split_a.length > 2) {
                            currnt_tagsource_tables_l.clear();
                            currnt_tagsource_tables_l.add(source_0_split_a[source_0_split_a.length - 2]);
                        }
//                        System.out.println("3493 currnt_tagsource_tables_l="+currnt_tagsource_tables_l);
//                        parent_tag_vals_s = findParentForTerm(st, currnt_tagsource_tables_l, tag_column, source_split_a[1], exact, childen_before);
////                            System.out.println("3415 " + parent_tag_vals_s);
//                        System.out.println("3493 childen_before=" + childen_before + " final_result_tbl=" + final_result_tbl + " final_target_column=" + final_target_column);
                        query = source_split_a[1];
//                         String table_source_split_a[0].re
                        if (childen_before) {
                            parent_tag_vals_s = findParentForTerm(st, currnt_tagsource_tables_l, tag_column, source_split_a[1], exact, childen_before);
//                            System.out.println("3415 " + parent_tag_vals_s);
                            query = source_split_a[1];
                        } else {
                            if (final_result_tbl == null) {
                                for (int i = 0; i < currnt_tagsource_tables_l.size(); i++) {
                                    String cor_ctbl = get_correct_table_name(currnt_tagsource_tables_l.get(i));
//                                    System.out.println("3506 cor_ctbl="+cor_ctbl);
                                    String final_target_column_topuse = final_target_column;
                                    if (final_target_column_topuse.equals("*")) {
                                        if (!tables_toCol.containsKey(cor_ctbl)) {
                                            final_target_column_topuse = cor_ctbl + ".ID";
                                            ArrayList<String> columns_l = getColumns(cor_ctbl, getDatasource_data_view());
                                            for (int j = 0; j < columns_l.size(); j++) {
                                                final_target_column_topuse = final_target_column_topuse + "," + cor_ctbl + "." + columns_l.get(j);
                                            }
                                            tables_toCol.put(cor_ctbl, final_target_column_topuse);
                                        }
                                        final_target_column_topuse = tables_toCol.get(cor_ctbl);
                                    } else {
                                        final_target_column_topuse = final_target_column_topuse + "," + cor_ctbl + ".ID";
                                    }

                                    String q = "'";
                                    if (!shoulditbequated(currnt_tagsource_tables_l.get(i), tag_column, getDatasource_data_view())) {
                                        q = "";
                                        exact = true;
                                    } else {
                                        source_split_a[1] = source_split_a[1].replaceAll(q, q + q);
                                    }

                                    if (exact) {
                                        final_sql_l.add("SELECT " + final_target_column_topuse + " FROM  " + get_correct_table_name(currnt_tagsource_tables_l.get(i))
                                                + " WHERE " + tag_column + "=" + q + source_split_a[1] + q + "|" + currnt_tagsource_tables_l.get(i));
                                    } else {
                                        final_sql_l.add("SELECT " + final_target_column_topuse + " FROM  " + get_correct_table_name(currnt_tagsource_tables_l.get(i))
                                                + " WHERE " + tag_column + " like " + q + "%" + source_split_a[1] + "%" + q + "|" + currnt_tagsource_tables_l.get(i));
                                    }

                                }
                            } else {
                                parent_tag_vals_s = findParentForTerm(st, currnt_tagsource_tables_l, tag_column, source_split_a[1], exact, childen_before);
                                query = source_split_a[1];
//                                System.out.println("3508" + parent_tag_vals_s);
                            }
                        }
//               
                    }


//                    System.out.println("3526 parent_tag_vals_s ="+parent_tag_vals_s );
//                    System.out.println("3405 " + sub_parent_tag_vals_s);

                    if (!sub_parent_tag_vals_s.isEmpty()) {
                        Iterator<String> sub_it = sub_parent_tag_vals_s.iterator();
                        while (sub_it.hasNext()) {
                            if (parent_tag_vals_s == null) {
                                parent_tag_vals_s = new HashSet<>();
                            }
                            String tmp = sub_it.next();
                            ResultSet sub_r = st.executeQuery(tmp);
                            String ctbl = sub_r.getMetaData().getTableName(1);
                            while (sub_r.next()) {
                                parent_tag_vals_s.add(ctbl + "|ID=" + sub_r.getInt(1));
                                parent_tag_vals_s.add(ctbl + "|PARENT_ID=" + sub_r.getInt(2));
                                parent_tag_vals_s.add(ctbl + "|HASH=" + sub_r.getString(3));
                            }
                            sub_r.close();
                        }
                    }
//                    System.out.println("3548 " + parent_tag_vals_s + "  query=" + query + " matcher_type=" + matcher_type);
                    if (parent_tag_vals_s != null) {
                        double num_search_term = -1;
                        boolean invalid_num = true;
                        if (numeric_request && query != null) {
                            if (query.matches("[0-9\\.]+")) {
                                num_search_term = new Double(query);
                                invalid_num = false;
                            }
                        }
//                        System.out.println("3793 source" + source + "\n\t" + query);
                        final_sql_l = translateToTags(st, parent_tag_vals_s, source, query, tagged_table,
                                final_result_tbl, final_target_column, exact, tables_toCol,
                                tag_column, curnt_tag_connection_tables_to_search_l,
                                childen_during, numeric_request, matcher, matcher_type, invalid_num,
                                num_search_term);
//                        System.out.println("\t38799 "+final_sql_l);
                    }
                }
//                for (int i = 0; i < final_sql_l.size(); i++) {
//                    System.out.println("3417 " + final_sql_l.get(i) + "\n");
//                }
                close(ncon, st, null);
            } catch (SQLException ex) {
                msges = msges + "\n Error AS66a: " + ex.getMessage();
//                ex.printStackTrace();
            } finally {
                close(ncon, st, null);
            }
        }
        ArrayList<ArrayList<String>> sql_l = new ArrayList<>();
        sql_l.add(final_sql_l);
        ArrayList<String> result_l = getForList(sql_l, username, checkifexists,
                exapandforeignKeys, addcolumnnames, childen_at_the_end, parent_at_the_end);

        if (result_l == null || result_l.isEmpty()) {
            result_l.clear();
            result_l.add("NA");
            result_l.add("NA");
        }
        return result_l;
    }

    /**
     * MethodID=AS66.1
     *
     */
    @WebMethod(exclude = true)
    private ArrayList<String> translateToTags(Statement st, HashSet<String> parent_tag_vals_s,
            String source, String query, String tagged_table, String final_result_tbl,
            String final_target_column, boolean exact, HashMap<String, String> tables_toCol,
            String tag_column, ArrayList<String> tag_connection_tables_to_search_l,
            boolean childen_during, boolean numeric_request, String matcher,
            int matcher_type, boolean invalid_num, double num_search_term) {
        ArrayList<String> final_sql_l = new ArrayList<>();
        try {
            HashSet<String> result_s = new HashSet<>();
            if (parent_tag_vals_s != null) {
                Iterator<String> parent_tag_vals_i = parent_tag_vals_s.iterator();
                while (parent_tag_vals_i.hasNext()) {
                    String c_parent_val = parent_tag_vals_i.next();
                    if (!numeric_request || !invalid_num) {
                        if (c_parent_val != null && c_parent_val.contains("=")) {
                            String[] c_val_a = c_parent_val.split("=", 2);
                            String ctbl = null;
                            String ccol = null;
                            int col_indx = c_val_a[0].indexOf("|");
                            if (col_indx > 0) {
                                ctbl = c_val_a[0].substring(0, col_indx);
                                ccol = c_val_a[0].substring(col_indx + 1);
                            }
                            String cor_ctbl = get_correct_table_name(ctbl);
//                            System.out.println("3351 cor_ctbl=" + cor_ctbl + " ccol=" + ccol + "\ttagged_table=" + tagged_table + "\t final_result_tbl=" + final_result_tbl);
                            if (cor_ctbl != null && ccol != null) {
                                String q = "'";
                                if (!shoulditbequated(cor_ctbl, tag_column, dataSource_data)) {
                                    q = "";
                                    exact = true;
                                }
                                String final_target_column_topuse = final_target_column;
//                                System.out.println("3563 final_target_column_topuse=" + final_target_column_topuse);
                                if (tagged_table == null && final_result_tbl == null) {
//                                    boolean reset_final_col = false;
                                    if (ccol.toUpperCase().equals("PARENT_ID")) {
                                        if (final_target_column_topuse.equals("*")) {
//                                            reset_final_col = true;
                                            if (!tables_toCol.containsKey(cor_ctbl)) {
                                                final_target_column_topuse = cor_ctbl + ".ID";
                                                ArrayList<String> columns_l = getColumns(cor_ctbl, getDatasource_data_view());
                                                for (int j = 0; j < columns_l.size(); j++) {
                                                    final_target_column_topuse = final_target_column_topuse + "," + cor_ctbl + "." + columns_l.get(j);
                                                }
                                                tables_toCol.put(cor_ctbl, final_target_column_topuse);
                                            }
                                            final_target_column_topuse = tables_toCol.get(cor_ctbl);
                                        } else {
                                            final_target_column_topuse = final_target_column_topuse + ",ID";
                                        }

                                        if (exact) {
                                            final_sql_l.add("SELECT " + final_target_column_topuse + " FROM " + cor_ctbl + " WHERE (id="
                                                    + c_val_a[1] + " OR parent_id=" + c_val_a[1] + ")  AND " + tag_column + "=" + q + query + q + "|" + get_correct_table_name(ctbl));
                                        } else {
                                            final_sql_l.add("SELECT " + final_target_column_topuse + " FROM " + cor_ctbl + " WHERE (id="
                                                    + c_val_a[1] + " OR parent_id=" + c_val_a[1] + ") AND " + tag_column + " like '%" + query + "%'" + "|" + get_correct_table_name(ctbl));
                                        }
//                                        if (reset_final_col) {
//                                            final_target_column = "*"; //This ad hoc change was made for a resaon
//                                        }
                                    }
                                } else if (query != null) {
//                                            System.out.println("3358 ccol=" + ccol + "  " + ccol.toUpperCase().equals("PARENT_ID") + " query=" + query);
                                    if (ccol.toUpperCase().equals("PARENT_ID")) {
//                                                System.out.println("3374 "+"SELECT HASH," + tag_column + " FROM " + cor_ctbl + " WHERE PARENT_ID=" + c_val_a[1]
//                                                + " OR ID=" + c_val_a[1]);
                                        ResultSet r_all = st.executeQuery("SELECT HASH," + tag_column + " FROM " + cor_ctbl + " WHERE PARENT_ID=" + c_val_a[1]
                                                + " OR ID=" + c_val_a[1]);
                                        while (r_all.next()) {
                                            String c_tagsrc_val = r_all.getString(2);
//                                                    System.out.println("3380 "+c_tagsrc_val);
                                            if (numeric_request) {
                                                if (c_tagsrc_val.matches("[0-9\\.]+")) {
                                                    Double numval = new Double(c_tagsrc_val);
                                                    /*
                                                     Decide the match depending on the requested match type
                                                     */
                                                    if (matcher_type == 0 && numval == num_search_term) {
                                                        result_s.add(ctbl + "|HASH=" + r_all.getString(1));
                                                    } else if (matcher_type == 1 && numval != num_search_term) {
                                                        result_s.add(ctbl + "|HASH=" + r_all.getString(1));
                                                    } else if (matcher_type == 2 && numval > num_search_term) {
                                                        result_s.add(ctbl + "|HASH=" + r_all.getString(1));
                                                    } else if (matcher_type == 3 && numval < num_search_term) {
                                                        result_s.add(ctbl + "|HASH=" + r_all.getString(1));
                                                    } else if (matcher_type == 4 && numval >= num_search_term) {
                                                        result_s.add(ctbl + "|HASH=" + r_all.getString(1));
                                                    } else if (matcher_type == 5 && numval <= num_search_term) {
                                                        result_s.add(ctbl + "|HASH=" + r_all.getString(1));
                                                    }
                                                }
                                            } else {
                                                if (matcher_type == 0 && query != null) {
                                                    c_tagsrc_val = c_tagsrc_val.toUpperCase();
//                                                    query = query.toUpperCase();
                                                    if (exact && c_tagsrc_val.equals(query.toUpperCase())) {
                                                        result_s.add(ctbl + "|HASH=" + r_all.getString(1));
                                                    } else if (c_tagsrc_val.contains(query.toUpperCase())) {
                                                        result_s.add(ctbl + "|HASH=" + r_all.getString(1));
                                                    }

                                                } else if (matcher_type == 1 && !c_tagsrc_val.equals(query)) {
                                                    result_s.add(ctbl + "|HASH=" + r_all.getString(1));
                                                }
                                            }
                                        }
                                        r_all.close();
                                    }
                                } else {
                                    if (ccol.toUpperCase().equals("HASH")) {
                                        result_s.add(ctbl + "|HASH=" + c_val_a[1]);
                                    }
//                                            System.out.println("3418 " + query);
                                }
                            }

                            if (query == null) {
                            } else {
                                if (cor_ctbl != null && ccol != null) {
                                    String sql_all = "select hash, " + tag_column + " from " + cor_ctbl + " where " + ccol + "=" + c_val_a[1];
                                }
                            }
                        }
                    }
                }
            }
            if (!result_s.isEmpty() && !tag_connection_tables_to_search_l.isEmpty()) {
                for (int i = 0; i < tag_connection_tables_to_search_l.size(); i++) {
                    String s_source = tag_connection_tables_to_search_l.get(i) + "." + LINK_TO_FEATURE + "="
                            + result_s.toString().replace("[", "").replace("]", "").replaceAll(", ", "||");
//                    System.out.println("\n3421 s_source=" + s_source + "  final_target_column=" + final_target_column + "  " + final_result_tbl);
                    ArrayList<String> sql_cl = advancedQuaryHandler__create(st, s_source, final_target_column, true, childen_during);
                    if (sql_cl != null) {
                        for (int j = 0; j < sql_cl.size(); j++) {
                            final_sql_l.add(sql_cl.get(j) + "|" + final_result_tbl);
                        }
                    }

                }
            }

        } catch (SQLException ex) {
            msges = msges + "\n Error AS66a: " + ex.getMessage();
            ex.printStackTrace();
        }
        return final_sql_l;
    }

    /**
     * MethodID=AS66.1
     *
     */
    @WebMethod(exclude = true)
    private HashSet<String> findParentForTerm(Statement st,
            ArrayList<String> tagsource_tables_l,
            String searchcol, String searchterm, boolean exact, boolean childen_before) {
        HashSet<String> result_s = new HashSet<>();
        HashSet<String> sql_l = new HashSet<>();
        try {
            for (int i = 0; i < tagsource_tables_l.size(); i++) {
                String q = "'";
                if (!shoulditbequated(tagsource_tables_l.get(i), searchcol, getDatasource_data_view())) {
                    q = "";
                    exact = true;
                } else {
                    searchterm = searchterm.replaceAll(q, q + q);
                }
                if (childen_before) {
                    String sql_all;
                    String c_tbl = get_correct_table_name(tagsource_tables_l.get(i));
                    if (exact) {
                        sql_all = "select ID from "
                                + c_tbl + " where " + searchcol + "=" + q + searchterm + q;
                    } else {
                        sql_all = "select  ID from "
                                + c_tbl + " where " + searchcol + " like '%" + searchterm + "%'";
                    }
                    ResultSet r_1 = st.executeQuery(sql_all);
                    while (r_1.next()) {
                        sql_l.add("SELECT ID,PARENT_ID,HASH FROM  " + c_tbl + " WHERE PARENT_ID=" + r_1.getInt(1) + " OR ID=" + r_1.getInt(1));
                    }
                    r_1.close();

                } else {
                    String sql_all;
                    String c_tbl = get_correct_table_name(tagsource_tables_l.get(i));
                    if (c_tbl != null) {
                        if (exact) {
                            sql_all = "select ID,PARENT_ID,HASH from "
                                    + c_tbl + " where " + searchcol + "=" + q + searchterm + q;
                        } else {
                            sql_all = "select  ID,PARENT_ID,HASH from "
                                    + c_tbl + " where " + searchcol + " like '%" + searchterm + "%'";
                        }
                        sql_l.add(sql_all);
                    } else {
//                        System.out.println("3559 " + tagsource_tables_l);
                    }
                }

            }
            Iterator<String> sql_i = sql_l.iterator();
            while (sql_i.hasNext()) {
                String sql = sql_i.next();
                ResultSet r = st.executeQuery(sql);
                String c_tbl = r.getMetaData().getTableName(1);
                while (r.next()) {
                    result_s.add(c_tbl + "|ID=" + r.getInt(1));
                    result_s.add(c_tbl + "|PARENT_ID=" + r.getInt(2));
                    result_s.add(c_tbl + "|HASH=" + r.getString(3));
                }
                r.close();
            }

        } catch (SQLException ex) {
            msges = msges + "\n Error AS66a: " + ex.getMessage();
//            ex.printStackTrace();
        }

        return result_s;
    }

    /**
     * MethodID=AS66.1
     *
     */
    @WebMethod(exclude = true)
    private String[] process_result_request(String target,
            ArrayList<String> tag_connection_tables_to_search_l,
            HashMap<String, String> tagable_to_tagged_map) {
        String[] out_a = new String[4];
        /*
         * 0- The tagged table
         * 1- The table to produce the final results from
         * 2- The columns to display in the final results
         * 3- Additional instructions like, DISTINCT, MIN ,MAX
         */
        if (target != null) {
            target = target.trim().toUpperCase();
            String[] target_split_a = target.toUpperCase().split("\\|", -1);
            String tagged_table = null;
            String additional_instruct = null;
            String final_result_tbl = null;
            boolean taget_is_tag = false;
            String final_traget_to_coma_split = null;
            /*Determine the tagged table*/
            if (target.startsWith(TAG_FLAG) || target.startsWith(TAG_FLAG2)) {
                taget_is_tag = true;
                final_traget_to_coma_split = target;
            } else if (target_split_a.length == 1 || !target_split_a[0].trim().isEmpty()) {
                int dot_indx = target_split_a[0].indexOf(".");
                if (dot_indx > 0) {
                    tagged_table = get_correct_table_name(target_split_a[0].substring(0, dot_indx));
                } else {
                    tagged_table = get_correct_table_name(target_split_a[0]);
                }
                if (tagged_table == null && tagable_to_tagged_map.containsKey(tagged_table)) {
                    tag_connection_tables_to_search_l.clear();
                    tag_connection_tables_to_search_l.add(tagable_to_tagged_map.get(tagged_table));
                    if (target_split_a.length == 1) {
                        final_traget_to_coma_split = target_split_a[0];
                    } else {
                        final_traget_to_coma_split = target_split_a[target_split_a.length - 1];
                    }
                } else {
                    if (target_split_a.length == 1) {
                        final_traget_to_coma_split = target;
                    } else {
                        if (target_split_a[target_split_a.length - 1].trim().isEmpty()) {
                            final_traget_to_coma_split = target_split_a[0];
                        } else {
                            final_traget_to_coma_split = target_split_a[target_split_a.length - 1];
                        }
                    }
                }
            } else {
                final_traget_to_coma_split = target_split_a[1];
            }

            String final_target_column = null;
//            if (tagged_table == null && target_split_a.length == 1) {
//                final_taget_is_tag = true;
//            }
            String[] coma_split_a = final_traget_to_coma_split.split(",");
            coma_split_a[0] = coma_split_a[0].trim();
            int space_indx = coma_split_a[0].indexOf(" ");
            if (space_indx > 1) {
                additional_instruct = coma_split_a[0].substring(0, space_indx);
                coma_split_a[0] = coma_split_a[0].substring(space_indx + 1);
            }
            for (int i = 0; i < coma_split_a.length; i++) {
                int dot_index = coma_split_a[i].indexOf(".");
                if (taget_is_tag) {
                    final_result_tbl = null;
                    if (dot_index < 0) {
                        final_target_column = "*";
                    } else {
                        if (final_target_column == null) {
                            final_target_column = coma_split_a[i].substring(dot_index + 1);
                        } else {
                            final_target_column = final_target_column + "," + coma_split_a[i].substring(dot_index + 1);
                        }
                    }
                } else {
                    if (dot_index < 0) {
                        final_result_tbl = get_correct_table_name(coma_split_a[i]);
                        if (final_result_tbl != null) {
                            ArrayList<String> columns_l = getColumns(final_result_tbl, getDatasource_data_view());
                            for (int j = 0; j < columns_l.size(); j++) {
                                if (final_target_column == null) {
                                    final_target_column = final_result_tbl + ".ID";
                                }
                                final_target_column = final_target_column + "," + final_result_tbl + "." + columns_l.get(j);

                            }
                        }
                    } else {
                        final_result_tbl = get_correct_table_name(coma_split_a[i].substring(0, dot_index));
                        if (final_target_column == null) {
                            final_target_column = final_result_tbl + ".ID";
                        }
                        final_target_column = final_target_column + "," + final_result_tbl + "." + coma_split_a[i].substring(dot_index + 1);

                    }
                }
            }
            out_a[0] = tagged_table;
            out_a[1] = final_result_tbl;
            out_a[2] = final_target_column;
            out_a[3] = additional_instruct;

        }
        return out_a;
    }

//    /**
//     * MethodID=AS66.1
//     *
//     */
//    @WebMethod(exclude = true)
//    private ArrayList<String> getMatchingTag2(String source, String in_target,
//            boolean exact, boolean childen_before, boolean childen_during,
//            String username) {
//        ArrayList<String> final_sql_l = new ArrayList<>();
//        ArrayList<String> tagsource_tables_l = new ArrayList();
//        Connection ncon = null;
//        Statement st = null;
////        String final_target = null;
//        StringBuilder final_target_column = new StringBuilder();
//        int error = 0;
//        if (tagsource_tables_l == null) {
//            ArrayList<String> all_tbl_l = getTables();
//            tagsource_tables_l = new ArrayList<>();
//            tagable_tables_l = new ArrayList<>();
//            for (int i = 0; i < all_tbl_l.size(); i++) {
//                if (all_tbl_l.get(i).endsWith(TAGSOURCE_FLAG)) {
//                    tagsource_tables_l.add(all_tbl_l.get(i));
//                }
//                if (all_tbl_l.get(i).endsWith(TO_TAG_FLAG)) {
//                    String c_tb = get_correct_table_name(all_tbl_l.get(i).replace(TO_TAG_FLAG, ""));
//                    if (c_tb != null) {
//                        tagable_tables_l.add(c_tb);
//                    }
//                }
//            }
//        }
//        String results_table = null;
//        String additional_instruct = "";//distinct, min, max etc..
//        /*Full table names in uppcase */
//        String tagged_table_tbl = null;
//        error = 1;
//        if (in_target != null) {
//            in_target = in_target.toUpperCase();
//            String[] tarfet_split_a = in_target.toUpperCase().split("\\|", -1);
//
//            if (in_target.startsWith(TAG_FLAG) || in_target.startsWith(TAG_FLAG2)) {
//                error = 0;
//                if (tarfet_split_a.length == 1 || (tarfet_split_a.length > 1 && tarfet_split_a[1].trim().isEmpty())) {
//                    int dot_indx = tarfet_split_a[0].lastIndexOf(".");
//                    if (dot_indx > 0) {
//                        final_target_column.append(tarfet_split_a[0].substring(dot_indx + 1));
//                    }
//                }
//            } else {
//                if (!tarfet_split_a[0].trim().isEmpty()) {
//                    int dot_indx = tarfet_split_a[0].indexOf(".");
//                    if (dot_indx > 0) {
//                        tagged_table_tbl = get_correct_table_name(tarfet_split_a[0].substring(0, dot_indx));
//                        if (tagged_table_tbl != null) {
//                            if (!tagable_tables_l.contains(tagged_table_tbl)) {
//                                String[] tarfet_split_a_mod = new String[2];
//                                tarfet_split_a_mod[0] = tagged_table_tbl;
//                                tarfet_split_a_mod[1] = tarfet_split_a[0].trim();
//                                tarfet_split_a = tarfet_split_a_mod;
//                                tagged_table_tbl = null;
//                            }
//                            error = 0;
//                        }
//                    } else {
//                        tagged_table_tbl = get_correct_table_name(tarfet_split_a[0]);
//                        if (tagged_table_tbl != null) {
//                            if (!tagable_tables_l.contains(tagged_table_tbl)) {
//                                String[] tarfet_split_a_mod = new String[2];
//                                tarfet_split_a_mod[0] = tagged_table_tbl;
//                                tarfet_split_a_mod[1] = tarfet_split_a[0].trim();
//                                tarfet_split_a = tarfet_split_a_mod;
//                                tagged_table_tbl = null;
//                            }
//                            error = 0;
//                        }
//                    }
//                }
//            }
////            System.out.println("3662 final_target_column=" + final_target_column);
//            if (tarfet_split_a.length >= 2) {
//                String[] coma_split_a = tarfet_split_a[1].split(",");
//                coma_split_a[0] = coma_split_a[0].trim();
//                int space_indx = coma_split_a[0].indexOf(" ");
//                if (space_indx > 1) {
//                    additional_instruct = coma_split_a[0].substring(0, space_indx);
//                    coma_split_a[0] = coma_split_a[0].substring(space_indx + 1);
//                }
//                for (int i = 0; i < coma_split_a.length; i++) {
//                    int dot_index = coma_split_a[i].indexOf(".");
//                    if (dot_index < 0) {
//                        results_table = get_correct_table_name(coma_split_a[i]);
//                        if (results_table != null) {
//                            error = 0;
//                            String tbal_nm_for_col = results_table.substring(results_table.indexOf("."));
//                            ArrayList<String> columns_l = getColumns(results_table, getDatasource_data_view());
//                            for (int j = 0; j < columns_l.size(); j++) {
//                                if (final_target_column.length() == 0) {
//                                    final_target_column.append(tbal_nm_for_col + "." + columns_l.get(j));
//                                } else {
//                                    final_target_column.append("," + tbal_nm_for_col + "." + columns_l.get(j));
//                                }
//                            }
//                        }
//                    } else if (dot_index > 0) {
//                        results_table = get_correct_table_name(coma_split_a[i].substring(0, dot_index));
//                        if (results_table != null) {
//                            error = 0;
//                            final_target_column.append(results_table + "." + coma_split_a[i].substring(dot_index + 1));
//                        }
//                    }
//                }
//            }
//        }
////        System.out.println("3500 tagged_table_tbl=" + tagged_table_tbl + " final_target=" + final_target_column + "   error=" + error);
//        if (error == 0) {
//            String tag_column = "NAME"; /*Default is the name column*/
//            ArrayList<String> tag_tables_l = new ArrayList<>();
//            ArrayList<String> all_tbl_l = getTables();
//            for (int i = 0; i < all_tbl_l.size(); i++) {
//                if (all_tbl_l.get(i).endsWith(TO_TAG_FLAG)) {
//                    String toTag_table_relat = all_tbl_l.get(i).replace(TO_TAG_FLAG, "").toUpperCase();
//                    if (tagged_table_tbl == null) {/*No tagged table specified*/
//                        tag_tables_l.add(all_tbl_l.get(i).toUpperCase());
//                    } else if (tagged_table_tbl.equals(toTag_table_relat)) {/*tagged table use to narrow the search, faster query*/
//                        tag_tables_l.add(all_tbl_l.get(i).toUpperCase());
//                    } else if (all_tbl_l.get(i).toUpperCase().equals(tagged_table_tbl)) {/*A 2tag table is specified as the tagged table,*/
//                        tag_tables_l.add(all_tbl_l.get(i).toUpperCase());
//                    }
//                }
//            }
//            try {
//                HashSet<String> result_s = new HashSet<>();
//                String matcher = null;
////                boolean negative = false;
//                boolean numeric_request = true;
//                int matcher_type = 0;
//                //modify this as if the serach term contain <= this wioll breake
//                int min_match = Integer.MAX_VALUE;
//                ArrayList<String> match_key_l = new ArrayList<>();
//                match_key_l.add("=");
//                match_key_l.add("!=");
//                match_key_l.add(">");
//                match_key_l.add("<");
//                match_key_l.add(">=");
//                match_key_l.add("<=");
//                for (int i = 0; i < match_key_l.size(); i++) {
//                    int c_index = source.indexOf(match_key_l.get(i));
//                    /*if it is =, > or < add 3 (so if the split 
//                     * really is >= then it is compansated)*/
//                    if (c_index > 0 && (i == 0 || i == 2 || i == 3)) {
//                        c_index = c_index + 1;
//                    }
//                    /*Consider only the fist split when if 
//                     * there are more, so the search query it 
//                     * elf can have this split characters*/
//                    if (c_index > 0 && c_index < min_match) {
//                        min_match = c_index;
//                        matcher = match_key_l.get(i);
//                        matcher_type = i;
//                    }
//                }
////                System.out.println("3597 matcher="+matcher );
//                if (matcher_type <= 1) {/*use string matching for = and !=*/
//                    numeric_request = false;
//                }
//                /*
//                 The search type and search column provided
//                 * e.g. tag.uri=/disease/cancer
//                 */
//                String[] source_split_a = source.split(matcher, 2);
////                System.out.println("3606 "+Arrays.deepToString(source_split_a));
//                int col_ind = source_split_a[0].lastIndexOf(".");
//                if (col_ind > 0) {
//                    tag_column = source_split_a[0].substring(col_ind + 1);
//                }
//                tag_column = tag_column.toUpperCase();
//
//                /*
//                 * A search type and term provided e.g tag=cancer
//                 */
//                if (source_split_a.length == 2) {
//                    ncon = getDatasource_data_view().getConnection();
//                    st = ncon.createStatement();
//                    source_split_a[1] = source_split_a[1].trim();
//                    boolean ok = true;
//                    ArrayList<String> parent_tag_vals_l = null;
//                    if (tag_tables_l.isEmpty()) {
//                        //tag details tables notfound 
//                        msges = msges + "\n Error AS66f: Source table/s not found " + tagged_table_tbl;
//                    } else {
////                        String sql = null;
//                        String query = null;
//                        //split using the parth seperator (URI expected)
////                    if (true) {
//
//                        if (tag_column.equals("NAME")) {
//                            String[] query_split_a = source_split_a[1].split("/");
//                            if (query_split_a[0].trim().isEmpty()) {
//                                query_split_a = Arrays.copyOfRange(query_split_a, 1, query_split_a.length);
//                            }
////                            System.out.println("3634 " + Arrays.deepToString(query_split_a));
//                            if (query_split_a.length > 2) {
//                                query_split_a[0] = query_split_a[0].trim().toUpperCase();
//                                if (!query_split_a[0].endsWith("_TAGSOURCE")) {
//                                    query_split_a[0] = query_split_a[0] + "_TAGSOURCE";
//                                }
////                                String tag_tbl = get_correct_table_name(query_split_a[0]);
////                                if (tag_tbl != null) {
//
//                                if (childen_before) {
////                                    parent_tag_vals_l = getParentFromURI(st, query_split_a, tagsource_tables_l, true);
//                                    query = query_split_a[query_split_a.length - 1];
//                                } else {
//                                    String[] parents_a = new String[query_split_a.length - 1];
//                                    System.arraycopy(query_split_a, 0, parents_a, 0, parents_a.length);
////                                    parent_tag_vals_l = getParentFromURI(st, parents_a, tagsource_tables_l, true);
//                                    query = query_split_a[query_split_a.length - 1];
//                                }
////                                } else {
////                                    //Erro wrong tagsource
////                                }
//
//                            } else if (query_split_a.length == 2) {
//                                query_split_a[0] = query_split_a[0].trim().toUpperCase();
//                                if (!query_split_a[0].endsWith("_TAGSOURCE")) {
//                                    query_split_a[0] = query_split_a[0] + "_TAGSOURCE";
//                                }
//                                parent_tag_vals_l = new ArrayList<>(1);
//                                parent_tag_vals_l.add(query_split_a[0] + "|HASH=*");
//                                query = query_split_a[1];
//
//                            } else {
//                                query = query_split_a[0];
//                            }
//                        } else if (tag_column.equals("URI")) {
//                            exact = true;
//                            int tagspurce_ind = source_split_a[1].indexOf("/");
//                            if (tagspurce_ind == 0) {
//                                source_split_a[1] = source_split_a[1].replaceFirst("/", "");
//                                tagspurce_ind = source_split_a[1].indexOf("/");
//                            }
//                            if (tagspurce_ind > 0) {
//                                String tagsource = get_correct_table_name(source_split_a[1].substring(0, tagspurce_ind));
//                                if (tagsource != null && tagsource_tables_l.contains(tagsource)) {
//                                    tagsource_tables_l.clear();
//                                    tagsource_tables_l.add(tagsource);
//                                    query = source_split_a[1].substring(tagspurce_ind + 1);
//                                } else {
//                                    query = source_split_a[1];
//                                }
//
//                            } else {
//                                query = source_split_a[1];
//                            }
//                        } else {
//                            if (tag_column.equals("HASH")) {
//                                exact = true;
//                            }
//                            query = source_split_a[1];
//                        }
////                        System.out.println("3650 query=" + query + " " + tagsource_tables_l + "  " + parent_tag_vals_l);
//                        /*Check whether the search query is numerical*/
////                        Double num_search_term = new Double("-1");
//                        double num_search_term = -1;
//                        boolean invalid_num = true;
//                        if (numeric_request && query != null) {
//                            if (query.matches("[0-9\\.]+")) {
//                                num_search_term = new Double(query);
//                                invalid_num = false;
//                            }
//                        }
////                        System.out.println("3645 parent_tag_vals_l=" + parent_tag_vals_l + "\t" + tagged_table_tbl);
//                        /*Find parents and narow the search space*/
//                        if (parent_tag_vals_l != null) {
//                            for (int i = 0; i < parent_tag_vals_l.size(); i++) {
//                                String c_parent_val = parent_tag_vals_l.get(i);
//                                if (!numeric_request || !invalid_num) {
//                                    if (c_parent_val != null && c_parent_val.contains("=")) {
//                                        String[] c_val_a = c_parent_val.split("=", 2);
//                                        String ctbl = c_val_a[0];
//                                        String ccol = "parent_id";
//                                        int col_indx = c_val_a[0].indexOf("|");
//                                        if (col_indx > 0) {
//                                            ctbl = c_val_a[0].substring(0, col_indx);
//                                            ccol = c_val_a[0].substring(col_indx + 1);
//                                        }
//                                        String sql_all = null;
//                                        if (c_val_a[1].equals("*")) {
//                                            String c_tbl = get_correct_table_name(ctbl);
//                                            if (c_tbl != null) {
//                                                if (final_target_column.length() == 0) {
//                                                    final_target_column.append("*");
//                                                }
//                                                String q = "'";
//                                                if (!shoulditbequated(ctbl, ccol, getDatasource_data_view())) {
//                                                    q = "";
//                                                    exact = true;
//                                                }
//                                                String sub_sql_all;//
//                                                if (exact) {
//                                                    sub_sql_all = "select parent_id from "
//                                                            + c_tbl + " where " + tag_column + "=" + q + query + q;
//                                                } else {
//                                                    sub_sql_all = "select  parent_id from "
//                                                            + c_tbl + " where " + tag_column + " like '%" + query + "%'";
//                                                }
//                                                ResultSet r_1 = st.executeQuery(sub_sql_all);
//                                                HashSet<Integer> prent_id_s = new HashSet<>();
//                                                while (r_1.next()) {
//                                                    prent_id_s.add(r_1.getInt(1));
//                                                }
//                                                r_1.close();
//                                                if (!prent_id_s.isEmpty()) {
//                                                    sql_all = "select hash, " + tag_column + " from " + get_correct_table_name(ctbl) + " where parent_id in ("
//                                                            + prent_id_s.toString().replace("[", "").replace("]", "").replaceAll("\\s", "") + ")";
//                                                }
//                                            }
//                                        } else {
//                                            sql_all = "select hash, " + tag_column + " from " + get_correct_table_name(ctbl) + " where " + ccol + "=" + c_val_a[1];
//                                        }
//                                        System.out.println("3855 sql_all=" + sql_all + " " + tagged_table_tbl + "  " + results_table);
//                                        ResultSet r_all = st.executeQuery(sql_all);
//                                        if (tagged_table_tbl == null && results_table == null) {
//                                            while (r_all.next()) {
//                                                String c_tagsrc_val = r_all.getString(2);
//                                                String correct_ctbl = get_correct_table_name(ctbl);
//                                                if (final_target_column.length() == 0) {
//                                                    ArrayList<String> columns_l = getColumns(ctbl, getDatasource_data_view());
//                                                    for (int j = 0; j < columns_l.size(); j++) {
//                                                        if (final_target_column.length() == 0) {
//                                                            final_target_column.append(correct_ctbl + "." + columns_l.get(j));
//                                                        } else {
//                                                            final_target_column.append("," + correct_ctbl + "." + columns_l.get(j));
//                                                        }
//                                                        if (columns_l.get(j).toUpperCase().equals("ID")) {
//                                                            final_target_column.append("," + correct_ctbl + "." + columns_l.get(j));
//                                                        }
//                                                    }
//                                                }
//                                                if (childen_before) {
//                                                    final_sql_l.add("select " + final_target_column + " from " + correct_ctbl + " where HASH='" + r_all.getString(1) + "'|" + ctbl);
//                                                } else {
//                                                    if (numeric_request) {
//                                                        if (c_tagsrc_val.matches("[0-9\\.]+")) {
//                                                            Double numval = new Double(c_tagsrc_val);
//                                                            /*
//                                                             Decide the match depending on the requested match type
//                                                             */
//                                                            if (matcher_type == 0 && numval == num_search_term) {
//                                                                final_sql_l.add("select " + final_target_column + " from " + correct_ctbl + " where HASH='" + r_all.getString(1) + "'|" + ctbl);
//                                                            } else if (matcher_type == 1 && numval != num_search_term) {
//                                                                final_sql_l.add("select " + final_target_column + " from " + correct_ctbl + " where HASH='" + r_all.getString(1) + "'|" + ctbl);
//                                                            } else if (matcher_type == 2 && numval > num_search_term) {
//                                                                final_sql_l.add("select " + final_target_column + " from " + correct_ctbl + " where HASH='" + r_all.getString(1) + "'|" + ctbl);
//                                                            } else if (matcher_type == 3 && numval < num_search_term) {
//                                                                final_sql_l.add("select " + final_target_column + " from " + correct_ctbl + " where HASH='" + r_all.getString(1) + "'|" + ctbl);
//                                                            } else if (matcher_type == 4 && numval >= num_search_term) {
//                                                                final_sql_l.add("select " + final_target_column + " from " + correct_ctbl + " where HASH='" + r_all.getString(1) + "'|" + ctbl);
//                                                            } else if (matcher_type == 5 && numval <= num_search_term) {
//                                                                final_sql_l.add("select " + final_target_column + "  from " + correct_ctbl + " where HASH='" + r_all.getString(1) + "'|" + ctbl);
//                                                            }
//                                                        }
//                                                    } else {
//                                                        if (matcher_type == 0 && query != null) {
//                                                            c_tagsrc_val = c_tagsrc_val.toUpperCase();
//                                                            query = query.toUpperCase();
//                                                            if (exact && c_tagsrc_val.equals(query)) {
//                                                                final_sql_l.add("select " + final_target_column + " from " + correct_ctbl + " where HASH='" + r_all.getString(1) + "'|" + ctbl);
//                                                            } else if (c_tagsrc_val.contains(query)) {
//                                                                final_sql_l.add("select " + final_target_column + " from " + correct_ctbl + " where HASH='" + r_all.getString(1) + "'|" + ctbl);
//                                                            }
//                                                        } else if (matcher_type == 1 && !c_tagsrc_val.equals(query)) {
//                                                            final_sql_l.add("select " + final_target_column + " from " + correct_ctbl + " where HASH='" + r_all.getString(1) + "'|" + ctbl);
//                                                        }
//                                                    }
//                                                }
//                                            }
//                                            r_all.close();
//                                        } else {
//                                            while (r_all.next()) {
//                                                String c_tagsrc_val = r_all.getString(2);
//                                                if (childen_before) {
//                                                    if (tagged_table_tbl == null) {
//                                                        final_sql_l.add("select * from " + get_correct_table_name(ctbl) + " where HASH='" + r_all.getString(1) + "'|" + ctbl);
//                                                    } else {
//                                                        result_s.add(ctbl + "|HASH=" + r_all.getString(1));
//                                                    }
//                                                } else {
//                                                    if (numeric_request) {
//                                                        if (c_tagsrc_val.matches("[0-9\\.]+")) {
//                                                            Double numval = new Double(c_tagsrc_val);
//                                                            /*
//                                                             Decide the match depending on the requested match type
//                                                             */
//                                                            if (matcher_type == 0 && numval == num_search_term) {
//                                                                result_s.add(ctbl + "|HASH=" + r_all.getString(1));
//                                                            } else if (matcher_type == 1 && numval != num_search_term) {
//                                                                result_s.add(ctbl + "|HASH=" + r_all.getString(1));
//                                                            } else if (matcher_type == 2 && numval > num_search_term) {
//                                                                result_s.add(ctbl + "|HASH=" + r_all.getString(1));
//                                                            } else if (matcher_type == 3 && numval < num_search_term) {
//                                                                result_s.add(ctbl + "|HASH=" + r_all.getString(1));
//                                                            } else if (matcher_type == 4 && numval >= num_search_term) {
//                                                                result_s.add(ctbl + "|HASH=" + r_all.getString(1));
//                                                            } else if (matcher_type == 5 && numval <= num_search_term) {
//                                                                result_s.add(ctbl + "|HASH=" + r_all.getString(1));
//                                                            }
//                                                        }
//                                                    } else {
//                                                        if (matcher_type == 0 && query != null) {
//                                                            c_tagsrc_val = c_tagsrc_val.toUpperCase();
//                                                            query = query.toUpperCase();
//                                                            if (exact && c_tagsrc_val.equals(query)) {
//                                                                result_s.add(ctbl + "|HASH=" + r_all.getString(1));
//                                                            } else if (c_tagsrc_val.contains(query)) {
//                                                                result_s.add(ctbl + "|HASH=" + r_all.getString(1));
//                                                            }
//
//                                                        } else if (matcher_type == 1 && !c_tagsrc_val.equals(query)) {
//                                                            result_s.add(ctbl + "|HASH=" + r_all.getString(1));
//                                                        }
//                                                    }
//                                                }
//                                            }
//                                            r_all.close();
//                                        }
////                                        }
//                                    }
//                                } else {
//                                    //error
//                                }
//                            }
//                        } else if (tagged_table_tbl == null) {
//                            if (final_target_column.length() == 0) {
//                                final_target_column.append("*");
//                            }
//                            for (int i = 0; i < tagsource_tables_l.size(); i++) {
//                                String q = "'";
//                                if (!shoulditbequated(tagsource_tables_l.get(i), tag_column, getDatasource_data_view())) {
//                                    q = "";
//                                    exact = true;
//                                }
//                                if (childen_before) {
//                                    String sql_all;
//                                    String c_tbl = get_correct_table_name(tagsource_tables_l.get(i));
//                                    if (exact) {
//                                        sql_all = "select ID from "
//                                                + c_tbl + " where " + tag_column + "=" + q + query + q;
//                                    } else {
//                                        sql_all = "select  ID from "
//                                                + c_tbl + " where " + tag_column + " like '%" + query + "%'";
//                                    }
//                                    ResultSet r_1 = st.executeQuery(sql_all);
//                                    while (r_1.next()) {
//                                        final_sql_l.add("SELECT " + final_target_column + " FROM  " + c_tbl + " WHERE PARENT_ID=" + r_1.getInt(1) + " OR ID=" + r_1.getInt(1) + "|" + c_tbl);
//                                    }
//                                    r_1.close();
//
//                                } else {
//                                    String sql_all;
//                                    String c_tbl = get_correct_table_name(tagsource_tables_l.get(i));
//                                    if (exact) {
//                                        sql_all = "select " + final_target_column + " from "
//                                                + c_tbl + " where " + tag_column + "=" + q + query + q;
//                                    } else {
//                                        sql_all = "select  " + final_target_column + " from "
//                                                + c_tbl + " where " + tag_column + " like '%" + query + "%'";
//                                    }
////                                      System.out.println("4036 final_target_column="+final_target_column);
////                                    System.out.println("4036 sql_all="+sql_all);
//                                    final_sql_l.add(sql_all + "|" + c_tbl);
//                                }
//
//                            }
//                        } else {
//                            if (final_target_column.length() == 0) {
//                                results_table = tagged_table_tbl;
//                            }
//                            for (int i = 0; i < tagsource_tables_l.size(); i++) {
//                                String q = "'";
//                                if (!shoulditbequated(tagsource_tables_l.get(i), tag_column, getDatasource_data_view())) {
//                                    q = "";
//                                    exact = true;
//                                }
//                                if (childen_before) {
//                                    String sql_all;
//                                    String c_tbl = get_correct_table_name(tagsource_tables_l.get(i));
//                                    if (exact) {
//                                        sql_all = "select ID,HASH from "
//                                                + c_tbl + " where " + tag_column + "=" + q + query + q;
//                                    } else {
//                                        sql_all = "select  ID,HASH from "
//                                                + c_tbl + " where " + tag_column + " like '%" + query + "%'";
//                                    }
//                                    HashSet<String> hash_s = new HashSet<>();
//                                    String table_for_link = c_tbl.substring(c_tbl.lastIndexOf(".") + 1);
//                                    ResultSet r_1 = st.executeQuery(sql_all);
//                                    while (r_1.next()) {
//                                        hash_s.add(r_1.getString(1));
//                                        result_s.add(table_for_link + "|HASH=" + r_1.getString(2));
//                                    }
//                                    r_1.close();
//
//                                    if (!hash_s.isEmpty()) {
//                                        String sub_sql = "SELECT HASH FROM " + c_tbl + " WHERE PARENT_ID IN (" + hash_s.toString().replace("[", "").replace("]", "").replaceAll("\\s", "") + ")";
//                                        ResultSet r_2 = st.executeQuery(sub_sql);
//
//                                        while (r_2.next()) {
//                                            result_s.add(table_for_link + "|HASH=" + r_2.getString(1));
//                                        }
//                                        r_2.close();
//                                    }
//                                } else {
//                                    String sql_all;
//                                    String c_tbl = get_correct_table_name(tagsource_tables_l.get(i));
//                                    if (exact) {
//                                        sql_all = "select hash from "
//                                                + c_tbl + " where " + tag_column + "=" + q + query + q;
//                                    } else {
//                                        sql_all = "select  hash from "
//                                                + c_tbl + " where " + tag_column + " like '%" + query + "%'";
//                                    }
//                                    ResultSet r_all = st.executeQuery(sql_all);
//                                    c_tbl = c_tbl.substring(c_tbl.lastIndexOf(".") + 1);
//                                    while (r_all.next()) {
//                                        result_s.add(c_tbl + "|HASH=" + r_all.getString(1));
//                                    }
//                                    r_all.close();
//
//                                }
//
//                            }
//                        }
//                    }
//                } else {
//                    msges = msges + "\n Error AS66c: incorrect query format";
//                }
//
//                if (!result_s.isEmpty()) {
//                    for (int i = 0; i < tag_tables_l.size(); i++) {
//                        if (final_target_column.length() == 0) {
//                            final_target_column.append(tag_tables_l.get(i).replace(TO_TAG_FLAG, ""));
//                        }
//                        if (results_table == null) {
//                            results_table = tag_tables_l.get(i).replace(TO_TAG_FLAG, "");
//                        }
//                        String s_source = tag_tables_l.get(i) + "." + LINK_TO_FEATURE + "=" + result_s.toString().replace("[", "").replace("]", "").replaceAll(", ", "||");
//
//                        ArrayList<String> sql_cl = advancedQuaryHandler__create(st, s_source, final_target_column.toString(), true, childen_during);
//                        for (int j = 0; j < sql_cl.size(); j++) {
//                            final_sql_l.add(sql_cl.get(j) + "|" + results_table);
//                        }
//                    }
//                }
//                close(ncon, st, null);
//            } catch (SQLException ex) {
//                msges = msges + "\n Error AS66a: " + ex.getMessage();
////                ex.printStackTrace();
//            } finally {
//                close(ncon, st, null);
//            }
//
//        }
//        if (error > 0) {
////            System.out.println("3788 error");
//            final_sql_l.clear();
//        }
//////        else {
////        for (int i = 0; i < final_sql_l.size(); i++) {
////            System.out.println(i + ") 4018 " + final_sql_l.get(i) + "\n");
////
////        }
////        }
//
//        return final_sql_l;
//    }
//
    /**
     * MethodID=AS66.1
     *
     */
    @WebMethod(exclude = true)
    private HashSet<String> getParentFromURI(Statement st, String[] search_term_a,
            ArrayList<String> tagsource_tables_l, boolean parent_request) {
        HashSet<String> tag_val_l = null;
        if (search_term_a.length > 1) {
            String tagsource_tbl = null;
            if (search_term_a[0].indexOf("|") < 0) {
                tagsource_tbl = get_correct_table_name(search_term_a[0]);
            }
            String[] parent_a;
            if (tagsource_tbl != null) {
                if (search_term_a.length >= 2) {
                    parent_a = new String[search_term_a.length - 1];
                    System.arraycopy(search_term_a, 1, parent_a, 0, parent_a.length);
                    parent_a[0] = tagsource_tbl + "|" + parent_a[0];
                } else {
                    parent_a = new String[search_term_a.length];
                    parent_a[0] = tagsource_tbl + "|" + parent_a[0];
                }
            } else {
                parent_a = new String[search_term_a.length];
                System.arraycopy(search_term_a, 1, parent_a, 1, (parent_a.length - 1));
                StringBuilder sourcetbls = new StringBuilder();
                sourcetbls.append(tagsource_tables_l.get(0));
                for (int i = 1; i < tagsource_tables_l.size(); i++) {
                    sourcetbls.append("|");
                    sourcetbls.append(tagsource_tables_l.get(i));
                }
                parent_a[0] = sourcetbls.toString();
            }
            if (parent_a.length > 1) {
                tag_val_l = getidforTagpath_search(st, parent_a, false, parent_request);
            } else {
                tag_val_l = getidforTagpath_search(st, parent_a, false, parent_request);
            }
//            System.out.println("4050 " + Arrays.deepToString(parent_a) + "  tag_val_l=" + tag_val_l);
        }
        return tag_val_l;
    }

//    /**
//     * MethodID=AS66.1
//     *
//     */
//    @WebMethod(exclude = true)
//    private String get_children(Statement st_1, String hir_tbl, String sql,
//            String parent_id_colnm, String child_search_colmns, String orig_tbl,
//            String child_col_nm) {
//        ArrayList<Integer> c_id_l = new ArrayList<>();
//        try {
//            int safetey = 100;
//            while (sql != null && safetey > 0) {
//                safetey--;
//                ResultSet r_1 = st_1.executeQuery(sql);
//                ResultSetMetaData r_md = r_1.getMetaData();
//                int id_col_pos = -1;
//                for (int i = 0; (id_col_pos < 0 && i < r_md.getColumnCount()); i++) {
//                    if (r_md.isAutoIncrement(i + 1)) {
//                        id_col_pos = i + 1;
//                    }
//                }
//                if (id_col_pos < 0) {
//                    for (int i = 0; (id_col_pos < 0 && i < r_md.getColumnCount()); i++) {
//                        if (r_md.getColumnName(i + 1).toUpperCase().equals("ID")) {
//                            id_col_pos = i + 1;
//                        }
//                    }
//                }
//                ArrayList<Integer> tmp_id_l = new ArrayList<>();
//                if (id_col_pos > 0) {
//                    while (r_1.next()) {
//                        tmp_id_l.add(r_1.getInt(id_col_pos));
//                    }
//                }
//                r_1.close();
//                if (!tmp_id_l.isEmpty()) {
//                    c_id_l.removeAll(tmp_id_l);
//                    c_id_l.addAll(tmp_id_l);
//                    sql = "select " + child_col_nm + "  from " + hir_tbl + " where " + parent_id_colnm + " in (" + tmp_id_l.toString().replace("[", "").replace("]", "") + ")";
//                } else {
//                    sql = null;
//                }
//            }
//        } catch (SQLException ex) {
//        }
//        if (c_id_l.isEmpty()) {
//            return null;
//        } else {
//            return "select " + child_search_colmns + " from " + orig_tbl + " where ID in (" + c_id_l.toString().replace("[", "").replace("]", "") + ")";
//        }
//    }
    /**
     * MethodID=AS66.2
     *
     */
    @WebMethod(exclude = true)
    private ArrayList<Integer> get_children_hierarchy(Statement st_1, String hir_tbl, String sql,
            String parent_id_colnm, String child_col_nm) {
//        ArrayList<Integer> c_id_l = new ArrayList<>();
        ArrayList<Integer> all_id_l = new ArrayList<>();
        Connection ncon = null;
        boolean new_statment = false;
        try {
            if (st_1 == null) {
                ncon = getDatasource_data_view().getConnection();
                st_1 = ncon.createStatement();
                new_statment = true;
            }
            int safetey = 100;
            while (sql != null && safetey > 0) {
                safetey--;
                ResultSet r_1 = st_1.executeQuery(sql);
                ResultSetMetaData r_md = r_1.getMetaData();
                int id_col_pos = -1;
                for (int i = 0; (id_col_pos < 0 && i < r_md.getColumnCount()); i++) {
                    if (r_md.isAutoIncrement(i + 1)) {
                        id_col_pos = i + 1;
                    }
                }
                if (id_col_pos < 0) {
                    for (int i = 0; (id_col_pos < 0 && i < r_md.getColumnCount()); i++) {
                        if (r_md.getColumnName(i + 1).toUpperCase().equals("ID")) {
                            id_col_pos = i + 1;
                        }
                    }
                }
                ArrayList<Integer> tmp_id_l = new ArrayList<>();
//                if (id_col_pos > 0) {
                while (r_1.next()) {
                    if (id_col_pos > 0) {
                        tmp_id_l.add(r_1.getInt(id_col_pos));
                        if (!all_id_l.contains(r_1.getInt(id_col_pos))) {
                            all_id_l.add(r_1.getInt(id_col_pos));
                        }

                    } else {
                        tmp_id_l.add(r_1.getInt(1));
                        if (!all_id_l.contains(r_1.getInt(2))) {
                            all_id_l.add(r_1.getInt(2));
                        }
                    }

                }
//                }
                r_1.close();
                if (!tmp_id_l.isEmpty()) {
                    all_id_l.removeAll(tmp_id_l);
                    all_id_l.addAll(tmp_id_l);
//                    System.out.println("515 " + c_id_l);
                    sql = "select " + child_col_nm + "," + parent_id_colnm + "  from " + hir_tbl + " where " + parent_id_colnm + " in (" + tmp_id_l.toString().replace("[", "").replace("]", "") + ")";
                } else {
                    sql = null;
                }
            }
        } catch (Exception ex) {
            msges = "Error AS44a: creating connections " + ex.getMessage();
            System.out.println("Error AS44a: creating connections " + ex.getMessage());
        } finally {
            try {
                if (ncon != null && !ncon.isClosed()) {
                    ncon.close();
                }
                if (new_statment) {
                    st_1.close();
                }
            } catch (SQLException ex) {
            }
        }
        return all_id_l;
    }
//
//    /**
//     * MethodID=AS66
//     *
//     */
//    @WebMethod(exclude = true)
//    private String advancedQuaryHandle_getForTag(String searchterm, String target, boolean exact) {
////        HashMap<String, String> result_map = new HashMap<>();
//        String result = "SELECT message FROM " + get_correct_table_name("errormsgs") + "  WHERE ID=2";
//        boolean ok = true;
//        String col_nm = "name";
//        String val = "*";
//        String q = "'";
//        String[] searchterm_a = searchterm.split("=");
//        boolean int_only = false;
//        if (searchterm_a.length == 2) {
//            if (searchterm_a[0].contains(".")) {
//                col_nm = searchterm_a[0].split("\\.")[1];
//                val = searchterm_a[1];
//            } else {
//                col_nm = searchterm_a[0];
//                val = searchterm_a[1];
//            }
//        } else {
//            System.out.println("Error AS65a: Query format incorrect");
//        }
//        String target_tbl = target;
//        if (target_tbl != null) {
//            String[] t_s = target_tbl.split("\\.");
//            if (t_s.length == 2) {
//                target_tbl = t_s[0];
//            } else if (t_s.length == 3) {
//                target_tbl = t_s[1];
//            }
//            target_tbl = get_correct_table_name(target_tbl);
//        }
////        System.out.println("2591 col_nm=" + col_nm + "  searchterm=" + searchterm);
//        Connection ncon = null;
//        Statement st = null;
//        try {
//            ncon = getDatasource_data().getConnection();
//            st = ncon.createStatement();
//
//            ArrayList<String> tables_to_search_l = new ArrayList<>();
//            ArrayList<String> to_tag_l = new ArrayList<>();
////            HashMap<String,ArrayList<String>> target_table_map = new HashMap<>();
//            ArrayList<String> all_tbl_l = getTables();
//            for (int i = 0; i < all_tbl_l.size(); i++) {
//                if (all_tbl_l.get(i).toLowerCase().endsWith(TAGSOURCE_FLAG)) {
//                    tables_to_search_l.add(all_tbl_l.get(i));
//                }
//                if (all_tbl_l.get(i).toLowerCase().endsWith(TO_TAG_FLAG)) {
//                    to_tag_l.add(all_tbl_l.get(i));
//                }
//            }
//
//            for (int i = 0; (i < tables_to_search_l.size() && ok); i++) {
//                String c_tbl = tables_to_search_l.get(i);
//                if (!shoulditbequated(c_tbl, col_nm, getDatasource_data())) {
//                    q = "";
//                    int_only = true;
//                }
//                String sql = "select message from  " + get_correct_table_name("errormsgs") + " where id=8";
//                if (!int_only || val.matches("[0-9]+")) {
//                    if (exact || int_only) {
//                        sql = "select id from " + c_tbl + " where " + c_tbl + "." + col_nm + "=" + q + val + q;
//                    } else {
//                        sql = "select id from " + c_tbl + " where " + c_tbl + "." + col_nm + " like " + q + "%" + val + "%" + q;
//                    }
//                } else {
//                    ok = false;
//                }
////                ArrayList<String> found_l = getforQuery(sql, true, st);
////                ArrayList<String> found_l =getforQuery_tag(sql, st);
////
////                if (!found_l.isEmpty()) {
////                    String link_to_fes = "'" + found_l.toString().replaceAll("\\s", "").replace("[", "").replace("]", "").replaceAll(",", "','") + "'";
////                    for (int j = 0; j < to_tag_l.size(); j++) {
////                        String c_tag_tbl = to_tag_l.get(j);
////                        String taget_pre = c_tag_tbl.split("2")[0];
////                        String taget_tbl_col = null;
////                        if (taget_pre.contains(".")) {
////                            taget_tbl_col = taget_pre.split("\\.")[1] + "_id";
////                        } else {
////                            taget_tbl_col = taget_pre + "_id";
////                        }
////
////                        if (target_tbl != null && target_tbl.equalsIgnoreCase(taget_pre)) {
////                            String source = c_tag_tbl + ".LINK_TO_FEATURE=" + link_to_fes;
////
//////                        String get_id_sql = "SELECT " + taget_tbl_col + " FROM " + c_tag_tbl + " WHERE LINK_TO_FEATURE in (" + link_to_fes + ")";
////                            result = advancedQuaryHandler__create(source, c_tag_tbl.split("\\.")[c_tag_tbl.split("\\.").length - 1], true);
////                        }
////
////
//////                        ResultSet r_g = st.executeQuery(get_id_sql);
//////                        ArrayList<String> tmp_l = new ArrayList<>();
//////
//////                        while (r_g.next()) {
//////                            tmp_l.add(r_g.getString(1));
//////                        }
//////                                             if (!tmp_l.isEmpty()) {
////////                            result_map.put(c_tag_tbl, tmp_l.toString());
//////                            String source = taget_pre + ".id=" + tmp_l.toString().replaceAll("\\s", "").replaceAll("\\[", "").replaceAll("\\]", "").replaceAll("\\,", ";");
//////                            String n_q = advancedQuaryHandler__create(source, taget_pre.split("\\.")[taget_pre.split("\\.").length-1], true);
//////                            result = n_q;
//////                        }
////                    }
////                }
//
////                for (int j = 0; j < found_l.size(); j++) {
////                    System.out.println("2716 "+ found_l.get(j));
////
////                }
//            }
//            st.close();
//            ncon.close();
//        } catch (SQLException ex) {
//            msges = msges + "\n Error 201: " + ex.getMessage();
//        } finally {
//            close(ncon, st, null);
//        }
//
//        return result;
//    }
//
//    /**
//     * MethodID=AS42
//     *
//     */
//    @WebMethod(exclude = true)
//    private String construct_advanced_query(ArrayList<String> path, String query, String target_column, String source_column, String source_tble) {
//        String sql = "";
//        if (path != null && !path.isEmpty()) {
//            boolean error = false;
//            HashMap<Integer, String[]> constructer_map = new HashMap<>();
//            for (int i = (path.size() - 1); (i > 0 && !error); i--) {
//                String c_tbl = path.get(i);
//                String c_table_clm = null;
//                String c_previous_tbl = path.get(i - 1);
//                String c_previous_tbl_clm = null;
//                HashMap<String, String[]> constraint_map = get_key_contraints(c_tbl, getDatasource_data());
//                if (constraint_map == null || constraint_map.isEmpty() || !constraint_map.containsKey(Constants.FOREIGN_TABLE_FLAG) || !arrayMatch(c_previous_tbl, constraint_map.get(Constants.FOREIGN_TABLE_FLAG))) {
//                    constraint_map = get_key_contraints(c_previous_tbl, getDatasource_data());
//                    String[] forgn_colmn = constraint_map.get(Constants.FOREIGN_KEY_COLUMNS_FLAG);
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
//                    String[] forgn_colmn = constraint_map.get(Constants.FOREIGN_KEY_COLUMNS_FLAG);
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
//                    new_construct[0] = c_tbl;
//                    new_construct[1] = c_table_clm;
//                    new_construct[2] = c_previous_tbl;
//                    new_construct[3] = c_previous_tbl_clm;
//                    constructer_map.put(i, new_construct);
//                }
//            }
//            int construct_size = constructer_map.keySet().size();
//            for (int i = construct_size; i > 0; i--) {
//                String[] c_constructs = constructer_map.get(i);
//                String[] next_c_constructs = constructer_map.get(i - 1);
//                String sql_tmp = null;
//                if (next_c_constructs != null) {
//                    if (i == construct_size) {
////                        if(source_tble!=null && source_column!=null){
////                                  sql_tmp = "SELECT "+source_tble+"."+source_column +","+ target_column + " FROM " + c_constructs[0] + " WHERE " + c_constructs[0] + "." + c_constructs[1] + " IN (SELECT " + c_constructs[3] + " FROM " + c_constructs[2] + " WHERE " + c_constructs[2] + "." + next_c_constructs[1] + " IN <EXPAND_REPLACER>)";
////                 
////                        }else{
//                        sql_tmp = "SELECT " + target_column + " FROM " + c_constructs[0] + " WHERE " + c_constructs[0] + "." + c_constructs[1] + " IN (SELECT " + c_constructs[3] + " FROM " + c_constructs[2] + " WHERE " + c_constructs[2] + "." + next_c_constructs[1] + " IN <EXPAND_REPLACER>)";
//
////                        }
//                    } else {
//                        sql_tmp = " (SELECT " + c_constructs[3] + " FROM " + c_constructs[2] + " WHERE " + c_constructs[2] + "." + next_c_constructs[1] + " IN <EXPAND_REPLACER>)";
//                    }
//                } else {
//                    if (query.equalsIgnoreCase("ALL")) {
//                        if (construct_size == 1) {
//                            sql_tmp = " SELECT " + target_column + " FROM " + c_constructs[0] + " WHERE " + c_constructs[0] + "." + c_constructs[1] + " in (SELECT " + c_constructs[3] + " FROM " + c_constructs[2] + ")";
//                        } else {
//                            sql_tmp = " (SELECT " + c_constructs[3] + " FROM " + c_constructs[2] + ")";
//                        }
//                    } else {
//                        if (construct_size == 1) {
//                            sql_tmp = " SELECT " + target_column + " FROM " + c_constructs[0] + " WHERE " + c_constructs[0] + "." + c_constructs[1] + " in (SELECT " + c_constructs[3] + " FROM " + c_constructs[2] + " WHERE " + query + ")";
//                        } else {
//                            sql_tmp = " (SELECT " + c_constructs[3] + " FROM " + c_constructs[2] + " WHERE " + query + ")";
//                        }
//                    }
//
//
//                }
//                if (sql.isEmpty()) {
//                    sql = sql_tmp;
//                } else {
//                    sql = sql.replace("<EXPAND_REPLACER>", sql_tmp);
//                }
//            }
//        } else {
//            //print error
//        }
//        return sql;
//    }

    /**
     * MethodID=AS40
     *
     */
    @WebMethod(exclude = true)
    private String construct_Path_query(ArrayList<String> path, String target_columns, String query) {
        String sql = "";
        int error = -1;
        if (path != null && !path.isEmpty()) {
//            System.out.println("3605 " + path);
            HashMap<Integer, String[]> constructer_map = new HashMap<>();
            for (int i = (path.size() - 1); (i > 0 && error < 0); i--) { //     for (int i = (path.size() - 2); (i >= 0 && error < 0); i--) {//
                String c_tbl = path.get(i - 1);
                String c_table_clm = null;
                String c_previous_tbl = path.get(i);
                String c_previous_tbl_clm = null;
//                System.out.println("3612 c_previous_tbl=" + c_previous_tbl + " c_tbl=" + c_tbl);
                HashMap<String, String[]> constraint_map = get_key_contraints(c_tbl, getDatasource_data_view());
                if (constraint_map == null || constraint_map.isEmpty() || !constraint_map.containsKey(Constants.FOREIGN_TABLE_FLAG)
                        || !arrayMatch(c_previous_tbl, constraint_map.get(Constants.FOREIGN_TABLE_FLAG))) {
                    constraint_map = get_key_contraints(c_previous_tbl, getDatasource_data_view());
                    String[] forgn_colmn = constraint_map.get(Constants.FOREIGN_KEY_COLUMNS_FLAG);
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
                    String[] forgn_colmn = constraint_map.get(Constants.FOREIGN_KEY_COLUMNS_FLAG);
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
                    new_construct[0] = get_correct_table_name(c_tbl);
                    new_construct[1] = c_table_clm;
                    new_construct[2] = get_correct_table_name(c_previous_tbl);
                    new_construct[3] = c_previous_tbl_clm;
                    constructer_map.put(i, new_construct);
                }
            }
            ArrayList<Integer> constructer_key_l = new ArrayList<>(constructer_map.keySet());
            Collections.sort(constructer_key_l);

            ArrayList<String> use_tbl_l = new ArrayList<>();
            String join = null;
            for (int i = 0; i < constructer_key_l.size(); i++) {
                Integer c_k = constructer_key_l.get(i);
                String[] c_a = constructer_map.get(c_k);
//                System.out.println("3661 " + Arrays.deepToString(c_a));
                String c_join = c_a[0] + "." + c_a[1] + "=" + c_a[2] + "." + c_a[3];
                use_tbl_l.remove(c_a[0]);
                use_tbl_l.add(c_a[0]);
                use_tbl_l.remove(c_a[2]);
                use_tbl_l.add(c_a[2]);
                if (join == null) {
                    join = c_join;
                } else {
                    join = c_join + " AND " + join;
                }

            }
//            sql = "SELECT " + getColumns(taget_tbl, null, true).get(taget_tbl).toString().replace("[", "").replace("]", "").replaceAll("\\s", "") + " FROM " + use_tbl_l.toString().replace("[", "").replace("]", "").replaceAll("\\s", "") + " WHERE " + join + " AND " + query;
            if (query.equals("ALL")) {
                sql = "SELECT " + target_columns + " FROM " + use_tbl_l.toString().replace("[", "").replace("]", "").replaceAll("\\s", "") + " WHERE " + join;
            } else {
                sql = "SELECT " + target_columns + " FROM " + use_tbl_l.toString().replace("[", "").replace("]", "").replaceAll("\\s", "") + " WHERE " + query + " AND " + join;
            }
        } else {
            sql = "SELECT message FROM " + get_correct_table_name("errormsgs") + " WHERE ID=6";
        }
        return sql;
    }

//    private String construct_advanced_query(ArrayList<String> path, String target_column, String query) {
//        //  out_result = construct_advanced_query(path, target_clm, final_modified_quary);
//        String sql = "";
//        if (path != null && !path.isEmpty()) {
//            boolean error = false;
//            HashMap<Integer, String[]> constructer_map = new HashMap<>();
//            for (int i = (path.size() - 1); (i > 0 && !error); i--) {
//                String c_tbl = path.get(i);
//                String c_table_clm = null;
//                String c_previous_tbl = path.get(i - 1);
//                String c_previous_tbl_clm = null;
//                HashMap<String, String[]> constraint_map = get_key_contraints(c_tbl, getDatasource_data());
//                if (constraint_map == null || constraint_map.isEmpty() || !constraint_map.containsKey(Constants.FOREIGN_TABLE_FLAG) || !arrayMatch(c_previous_tbl, constraint_map.get(Constants.FOREIGN_TABLE_FLAG))) {
//                    constraint_map = get_key_contraints(c_previous_tbl, getDatasource_data());
//                    String[] forgn_colmn = constraint_map.get(Constants.FOREIGN_KEY_COLUMNS_FLAG);
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
//                    String[] forgn_colmn = constraint_map.get(Constants.FOREIGN_KEY_COLUMNS_FLAG);
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
//                    new_construct[0] = c_tbl;
//                    new_construct[1] = c_table_clm;
//                    new_construct[2] = c_previous_tbl;
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
//            sql = "SELECT " + target_column + " FROM " + use_tbl_l.toString().replace("[", "").replace("]", "").replaceAll("\\s", "") + " WHERE " + join + " AND " + query;
//
////            int construct_size = constructer_map.keySet().size();
////            for (int i = construct_size; i > 0; i--) {
////                String[] c_constructs = constructer_map.get(i);
////                String[] next_c_constructs = constructer_map.get(i - 1);
////                String sql_tmp = null;
////                if (next_c_constructs != null) {
////                    if (i == construct_size) {
////                        sql_tmp = "SELECT " + target_column + " FROM " + c_constructs[0] + " WHERE " + c_constructs[0] + "." + c_constructs[1] + " IN (SELECT " + c_constructs[3] + " FROM " + c_constructs[2] + " WHERE " + c_constructs[2] + "." + next_c_constructs[1] + " IN <EXPAND_REPLACER>)";
////                    } else {
////                        sql_tmp = " (SELECT " + c_constructs[3] + " FROM " + c_constructs[2] + " WHERE " + c_constructs[2] + "." + next_c_constructs[1] + " IN <EXPAND_REPLACER>)";
////                    }
////                } else {
////                    if (query.equalsIgnoreCase("ALL")) {
////                        if (construct_size == 1) {
////                            sql_tmp = " SELECT " + target_column + " FROM " + c_constructs[0] + " WHERE " + c_constructs[0] + "." + c_constructs[1] + " in (SELECT " + c_constructs[3] + " FROM " + c_constructs[2] + ")";
////                        } else {
////                            sql_tmp = " (SELECT " + c_constructs[3] + " FROM " + c_constructs[2] + ")";
////                        }
////                    } else {
////                        if (construct_size == 1) {
////                            sql_tmp = " SELECT " + target_column + " FROM " + c_constructs[0] + " WHERE " + c_constructs[0] + "." + c_constructs[1] + " in (SELECT " + c_constructs[3] + " FROM " + c_constructs[2] + " WHERE " + query + ")";
////                        } else {
////                            sql_tmp = " (SELECT " + c_constructs[3] + " FROM " + c_constructs[2] + " WHERE " + query + ")";
////                        }
////                    }
////
////
////                }
////                if (sql.isEmpty()) {
////                    sql = sql_tmp;
////                } else {
////                    sql = sql.replace("<EXPAND_REPLACER>", sql_tmp);
////                }
////            }
//        } else {
//            //print error
//        }
//        return sql;
//    }
    /**
     * MethodID=AS43
     *
     */
    @WebMethod(exclude = true)
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

    /**
     * MethodID=AS44
     *
     */
    @WebMethod(exclude = true)
    public ArrayList<String> getforQuery(String query, boolean singlecolumn,
            Statement st_1, String caller, int timout, String username) {
        ArrayList<String> result_l = new ArrayList<>(1);
        Connection ncon = null;
        boolean new_statment = false;
//        System.out.println("5245 " + query);
        try {
            if (st_1 == null) {
                ncon = getDatasource_data_view().getConnection();
                st_1 = ncon.createStatement();
                new_statment = true;
            }

            GetResults get = new GetResults(this, singlecolumn, st_1, caller, timout, username, query);
            //HashMap<String,Thread> ...
            //keep the thread and cancell it if the pipe is broken
            (new Thread(get)).start();
            if (get.isDone()) {
                result_l = get.getResults();
            } else {
                result_l.add(0, "Error ASW21e: query timed out. Terminated as it was taking too long.");
            }

            if (ncon != null && !ncon.isClosed()) {
                ncon.close();
            }
            if (new_statment) {
                st_1.close();
            }
        } catch (Exception ex) {
            msges = "Error AS44a: creating connections " + ex.getMessage() + " caller=" + caller;
            System.out.println("Error AS44a: creating connections " + ex.getMessage() + "\n" + query + "| caller=" + caller);
        } finally {
            try {
                if (ncon != null && !ncon.isClosed()) {
                    ncon.close();
                }
                if (new_statment) {
                    st_1.close();
                }
            } catch (SQLException ex) {
            }
        }
        return result_l;
    }

    /**
     * MethodID=AS67
     *
     */
    @WebMethod(exclude = true)
    public ArrayList<String[]> getforQuery_tag(String query, Statement st_1) {
        ArrayList<String[]> result_l = new ArrayList<>();
        ResultSet r_1 = null;
        try {
            r_1 = st_1.executeQuery(query);
            ResultSetMetaData rsmd = r_1.getMetaData();
            String table_nm = rsmd.getTableName(1);
//            if (r_1.next()) {
//               
//                String[] val_a = new String[2];
//                val_a[0] = table_nm + "|HASH=" + r_1.getString(3);
////                val_a[0] = table_nm + "=" + r_1.getInt(1);
//                val_a[1] = r_1.getString(2);
//                result_l.add(val_a);
//            }
            while (r_1.next()) {
                String[] val_a = new String[2];
                val_a[0] = table_nm + "|HASH=" + r_1.getString(3);
//                val_a[0] = table_nm + "=" + r_1.getInt(1);
                val_a[1] = r_1.getString(2);
                result_l.add(val_a);
            }
            r_1.close();

        } catch (Exception ex) {
            ex.printStackTrace();
            msges = "Error 26a: creating connections " + ex.getMessage();
        } finally {
            close(null, null, r_1);
        }
        return result_l;
    }

    /**
     * MethodID=AS45
     *
     */
    @WebMethod(exclude = true)
    public HashMap<String, ArrayList<String>> getforQuery_asmap(String query, boolean distinctvalues, Statement st_1) {
        HashMap<String, ArrayList<String>> result_map = new HashMap<>();
        Connection ncon = null;
        ResultSet r_1 = null;
        try {
            r_1 = st_1.executeQuery(query);
            ResultSetMetaData rsmd = r_1.getMetaData();
            int NumOfCol = rsmd.getColumnCount() + 1;
            while (r_1.next()) {
                for (int i = 1; i < NumOfCol; i++) {
                    String table_nm = rsmd.getTableName(i);
                    String col = get_correct_table_name(table_nm) + "." + rsmd.getColumnName(i);
                    String value = r_1.getString(i);
                    if (result_map.containsKey(col)) {
                        if (distinctvalues) {
                            result_map.get(col).remove(value);
                        }
                        result_map.get(col).add(value);
                    } else {
                        ArrayList<String> tmp_l = new ArrayList<>(1);
                        tmp_l.add(value);
                        result_map.put(col, tmp_l);
                    }
                }
            }
            r_1.close();

        } catch (Exception ex) {
            ex.printStackTrace();
            msges = "Error 26a: creating connections " + ex.getMessage();
        } finally {
            close(null, null, r_1);
        }
        return result_map;
    }

    /**
     * MethodID=AS46
     *
     */
    @WebMethod(exclude = true)
    private ArrayList<String> getDeleteList(String c_id_table, HashMap<String, HashMap<String, String>> c_line_map, Statement st_1) {
        HashMap<String, ArrayList<String>> found_id_map = transform_map(c_line_map);
        ArrayList<String> delete_l = new ArrayList<>();
        ArrayList<String> dipendancies_l = getDependancies(c_id_table);
        boolean complete = false;
        int safet = 100;
        while (!dipendancies_l.isEmpty() && safet > 0) {
            safet--;
            ArrayList<String> found_id_key_l = new ArrayList<>(found_id_map.keySet());
            for (int i = 0; (i < found_id_key_l.size() && !dipendancies_l.isEmpty()); i++) {
                String cur_k = (REPLACEWITH_FALG + found_id_key_l.get(i)).toUpperCase();
                String cur_ids = found_id_map.get(found_id_key_l.get(i)).toString().replace("[", "").replace("]", "");
                if (cur_ids.matches("[0-9,\\s]+")) {
                    ArrayList<String> new_dipendancies_l = new ArrayList<>(dipendancies_l.size());
                    for (int l = 0; l < dipendancies_l.size(); l++) {
                        String c_dipendancy = dipendancies_l.get(l);
                        if (c_dipendancy.contains(cur_k)) {
                            c_dipendancy = c_dipendancy.replaceAll(cur_k, cur_ids);
                            if (c_dipendancy.contains(REPLACEWITH_FALG)) {
                                new_dipendancies_l.add(c_dipendancy);
                            } else {
                                HashMap<String, ArrayList<String>> result_map = getforQuery_asmap("SELECT * " + c_dipendancy.substring(0, c_dipendancy.indexOf("|")), true, st_1);
                                if (!result_map.isEmpty()) {
                                    delete_l.add(0, "DELETE " + c_dipendancy);
                                }
                                ArrayList<String> rsult_key_l = new ArrayList<>(result_map.keySet());
                                for (int k = 0; k < rsult_key_l.size(); k++) {
                                    if (found_id_map.containsKey(rsult_key_l.get(k))) {
                                        found_id_map.get(rsult_key_l.get(k)).addAll(result_map.get(rsult_key_l.get(k)));
                                    } else {
                                        found_id_map.put(rsult_key_l.get(k), result_map.get(rsult_key_l.get(k)));
                                    }
                                }
                            }
                        } else {
                            new_dipendancies_l.add(c_dipendancy);
                        }
                    }
                    dipendancies_l.clear();
                    dipendancies_l.addAll(new_dipendancies_l);
                }
            }
        }
        return delete_l;
    }

    /**
     * MethodID=AS47
     *
     */
    @WebMethod(exclude = true)
    private HashMap<String, ArrayList<String>> transform_map(HashMap<String, HashMap<String, String>> c_line_map) {
        HashMap<String, ArrayList<String>> found_id_map = new HashMap<String, ArrayList<String>>();
        ArrayList<String> table_nm_l = new ArrayList<String>(c_line_map.keySet());
        for (int i = 0; i < table_nm_l.size(); i++) {
            ArrayList<String> column_nm_l = new ArrayList<String>(c_line_map.get(table_nm_l.get(i)).keySet());
            for (int j = 0; j < column_nm_l.size(); j++) {
                String c_key = table_nm_l.get(i) + "." + column_nm_l.get(j);
                if (found_id_map.containsKey(c_key)) {
                    found_id_map.get(c_key).remove(c_line_map.get(table_nm_l.get(i)).get(column_nm_l.get(j)));
                    found_id_map.get(c_key).add(c_line_map.get(table_nm_l.get(i)).get(column_nm_l.get(j)));
                } else {
                    ArrayList<String> tmp_l = new ArrayList<String>(5);
                    tmp_l.add(c_line_map.get(table_nm_l.get(i)).get(column_nm_l.get(j)));

                    found_id_map.put(c_key, tmp_l);
                }
            }
        }
        return found_id_map;
    }

    /**
     * MethodID=AS48
     *
     */
    @WebMethod(exclude = true)
    private ArrayList<String> getDependancies(String in_tblnm) {
        HashMap<String, ArrayList<String>> dependancy_l_map = new HashMap<>();
        if (!dependancy_l_map.containsKey(in_tblnm)) {
            ArrayList<String> dpdnd_sql_l = new ArrayList<>(1);
            ArrayList<String> tables_to_be_checked_l = new ArrayList<>(1);
            ArrayList<String> tables__checked_l = new ArrayList<>(1);
            tables_to_be_checked_l.add(in_tblnm);
            Connection ncon = null;
            Statement st_1 = null;
            ResultSet r_1 = null;
            try {
                String sql = "select distinct table_name from  " + get_correct_table_name("tablename2feature") + " where showinsearch=1";
                ncon = getDatasource_data_view().getConnection();
                in_tblnm = get_correct_table_name(in_tblnm);
                in_tblnm = get_correct_table_name(in_tblnm);
                while (!tables_to_be_checked_l.isEmpty()) {
                    String tblnm = tables_to_be_checked_l.remove(0);
                    tables__checked_l.add(tblnm);
                    if (ncon.isClosed()) {
                        ncon = getDatasource_data_view().getConnection();
                    }
                    if (!ncon.isClosed()) {
                        st_1 = ncon.createStatement();
                        r_1 = st_1.executeQuery(sql);
                        while (r_1.next()) {
                            String c_tbl_nm = r_1.getString("table_name");
                            c_tbl_nm = get_correct_table_name(c_tbl_nm);
                            HashMap<String, String[]> constraint_map = get_key_contraints(c_tbl_nm, tblnm, null, getDatasource_data_view());
                            if (constraint_map != null) {
                                ArrayList<String> constraint_ma_key_l = new ArrayList<String>(constraint_map.keySet());
                                for (int i = 0; i < constraint_ma_key_l.size(); i++) {
                                    String[] result_a = constraint_map.get(constraint_ma_key_l.get(i));
                                    if (result_a != null) {
                                        String sql_tmp = "";
                                        if (c_tbl_nm.equals(result_a[1])) {
                                            sql_tmp = "FROM " + c_tbl_nm + " WHERE " + c_tbl_nm + "." + result_a[0] + " in (" + (REPLACEWITH_FALG + result_a[1]).toUpperCase() + "." + result_a[2].toUpperCase() + ")|" + c_tbl_nm;
                                        } else {
                                            sql_tmp = "FROM " + c_tbl_nm + " WHERE " + c_tbl_nm + "." + result_a[0] + " in (SELECT " + result_a[2] + " from " + result_a[1] + " where " + result_a[1] + ".id in (" + (REPLACEWITH_FALG + result_a[1]).toUpperCase() + "." + result_a[2].toUpperCase() + "))|" + c_tbl_nm;
                                        }
                                        dpdnd_sql_l.add(sql_tmp);
                                        if (!tables__checked_l.contains(c_tbl_nm)) {
                                            tables_to_be_checked_l.add(c_tbl_nm);
                                        }
                                    }
                                }
                            }
                        }
                        close(ncon, st_1, r_1);
                    }
                }
                dpdnd_sql_l.add(0, "FROM " + in_tblnm + " WHERE id in (" + (REPLACEWITH_FALG + in_tblnm + ".id").toUpperCase() + ")|" + in_tblnm);
                dependancy_l_map.put(in_tblnm, dpdnd_sql_l);
                if (!ncon.isClosed()) {
                    ncon.close();
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            } finally {
                close(ncon, st_1, r_1);
            }
        }

        return dependancy_l_map.get(in_tblnm);
    }

//    /**
//     * MethodID=AS49
//     *
//     */
//    @WebMethod(exclude = true)
//    private void create_backup() {
//        ArrayList<String> result_l = new ArrayList<String>(1);
//        Savepoint save1 = null;
//        Connection ncon = null;
//        Statement st_1 = null;
//        try {
//
//            ncon = getDatasource_data_view().getConnection();
//            ncon.setAutoCommit(false);
//            save1 = ncon.setSavepoint();
//            st_1 = ncon.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
//        } catch (SQLException ex) {
//            result_l.add(0, "Error 19a: Database connectivity problem " + ex.getMessage());
//            msges = msges + ex.getMessage();
//        } catch (Exception ex) {
//            result_l.add(0, "Error 19b: Database connectivity problem " + ex.getMessage());
//            msges = msges + ex.getMessage();
//        }
//
//        try {
//            String sql = "SHOW tables";
//            HashMap<String, HashMap<String, ArrayList<String>>> c_map_map = new HashMap<String, HashMap<String, ArrayList<String>>>();
//            if (ncon != null && !ncon.isClosed() && st_1 != null) {
//                ResultSet r_1 = st_1.executeQuery(sql);
//
//                while (r_1.next()) {
//                    String c_tbl_nm = r_1.getString(1);
//                    HashMap<String, ArrayList<String>> c_map = new HashMap<String, ArrayList<String>>();
//                    String sql_2 = "select * from " + c_tbl_nm;
//                    ResultSet r_2 = st_1.executeQuery(sql_2);
//                    int column_count = r_2.getMetaData().getColumnCount();
//                    while (r_2.next()) {
//                        for (int i = 0; i < column_count; i++) {
//                            String c_name = r_2.getMetaData().getColumnName(i + 1);
//                            String c_val = r_2.getString(i + 1);
//                            if (c_map.containsKey(c_name)) {
//                                c_map.get(c_name).add(c_val);
//                            } else {
//                                ArrayList<String> tmp = new ArrayList<String>(5);
//                                tmp.add(c_val);
//                                c_map.put(c_name, tmp);
//                            }
//                        }
//                    }
//                    c_map_map.put(c_tbl_nm, c_map);
//                }
//            }
//            close(ncon, st_1, null);
//            ArrayList<String> c_map_map_key_l = new ArrayList<String>(c_map_map.keySet());
//            if (c_map_map_key_l.size() > 1) {
//                c_map_map_key_l = decide_table_order(c_map_map_key_l, false);
//            }
//            for (int i = 0; i < c_map_map_key_l.size(); i++) {
//                String c_tbl = c_map_map_key_l.get(i);
//                HashMap<String, String[]> c_key_constraints = get_key_contraints(c_tbl, getDatasource_data_view());
//                HashMap<String, ArrayList<String>> c_map = c_map_map.get(c_tbl);
//                ArrayList<String> c_map_key_l = new ArrayList<String>(c_map.keySet());
//                for (int j = 0; j < c_map_key_l.size(); j++) {
//                    String c_clmn_nm = c_map_key_l.get(j);
//                    ArrayList<String> valus_to_expand = c_map.remove(c_clmn_nm);
//                    if (c_key_constraints.containsKey(c_clmn_nm)) {
//                        String forgn_tbl = c_key_constraints.get(c_clmn_nm)[1];
//                        String forgn_clmn = c_key_constraints.get(c_clmn_nm)[2];
//
//
//                    } else {
//                        c_map.put(c_tbl + "." + c_clmn_nm, valus_to_expand);
//                    }
//
//                }
//
//            }
//
//
//        } catch (SQLException ex) {
//            ex.printStackTrace();
//        } finally {
//            close(ncon, st_1, null);
//        }
//    }
    /**
     * MethodID=AS50
     *
     */
    @WebMethod(exclude = true)
    private String getRealKey(Set keys, String key) {
        if (keys != null && key != null) {
            ArrayList<String> key_l = new ArrayList<>(keys);
            key = key.split("\\.")[key.split("\\.").length - 1].toUpperCase();
            String realKey = null;
            for (int i = 0; (i < key_l.size() && realKey == null); i++) {
                String c_key = key_l.get(i).split("\\.")[key_l.get(i).split("\\.").length - 1].toUpperCase();
                if (c_key.equalsIgnoreCase(key)) {
                    realKey = key_l.get(i);
                }
            }

            return realKey;
        } else {
            return null;
        }
    }

    /**
     * MethodID=AS51
     *
     */
    @WebMethod(exclude = true)
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
        try {
            if (connection != null && !connection.isClosed()) {
                try {
                    connection.close();
                } catch (SQLException e) {
                }
            }
        } catch (SQLException e) {
        }
    }

    /**
     * MethodID=AS52
     *
     */
    @WebMethod(exclude = true)
    private DataSource getDatasource_data_view() {
        if (dataSource_data == null) {
            try {
                Context env = (Context) new InitialContext().lookup("java:comp/env");
                dataSource_data = (DataSource) env.lookup(DATASOURCE_NAME_data);
            } catch (NamingException e) {
                System.out.println("Error AS52a: " + e.getMessage());
            }
        }
        return dataSource_data;
    }

    /**
     * MethodID=AS63 javax.naming.CommunicationException: Communication
     *
     */
    @WebMethod(exclude = true)
    private DataSource getDatasource_data_update() {
        try {
            if (dataSource_data_update == null) {
                try {
                    Context env = (Context) new InitialContext().lookup("java:comp/env");
                    dataSource_data_update = (DataSource) env.lookup(DATASOURCE_NAME_data_update);
                } catch (NamingException e) {
                    StackTraceElement[] s_a = e.getStackTrace();
                    System.out.println("Error AS63: " + e.getMessage() + "\n ");
                    for (int i = 0; (i < s_a.length && i < 5); i++) {
                        System.out.println("Error AS63.a " + s_a[i]);

                    }
                }
            }
        } catch (Exception ex) {
            StackTraceElement[] s_a = ex.getStackTrace();
            System.out.println("Error AS63: " + ex.getMessage() + "\n ");
            for (int i = 0; (i < s_a.length && i < 5); i++) {
                System.out.println("Error AS63.b " + s_a[i]);

            }
        }

        return dataSource_data_update;
    }

    /**
     * MethodID=AS53
     *
     */
    @WebMethod(exclude = true)
    private DataSource getDatasource_users() {
        if (dataSource_users == null) {
            try {
                Context env = (Context) new InitialContext().lookup("java:comp/env");
                dataSource_users = (DataSource) env.lookup(DATASOURCE_NAME_USERS);

            } catch (NamingException e) {
                System.out.println("Error AS53: " + e.getMessage());
            }
        }
        return dataSource_users;
    }

    /**
     * MethodID=AS54
     *
     */
    @WebMethod(exclude = true)
    private static Session getDatasource_email() {
        if (mail_session == null) {
            try {
                InitialContext ctx = new InitialContext();
                mail_session = (Session) ctx.lookup(MAILER_DATASOURCE_NAME);
            } catch (NamingException e) {
                System.out.println("Error AS54: " + e.getMessage());
            }
        }
        return mail_session;
    }

    /**
     * MethodID=AS55
     *
     */
    @WebMethod(exclude = true)
    public ArrayList<String> getUniqueList(String table) {
        table = get_correct_table_name(table);
        ArrayList<String> uniquet_col_l_l = new ArrayList<>(1);
        if (table2uniqs_l_map == null || !table2uniqs_l_map.containsKey(table)) {
            table2uniqs_l_map = new HashMap<>();
            HashMap<String, String[]> const_map = get_key_contraints(table, getDatasource_data_view());
            String[] uniq_a = const_map.get(Constants.UNIQUE_TO_USER_COLUMNS_FLAG);
            if (uniq_a != null) {
                uniquet_col_l_l.addAll(Arrays.asList(uniq_a));
            }
            table2uniqs_l_map.put(table, uniquet_col_l_l);
        }
        return table2uniqs_l_map.get(table);
    }

    /**
     * MethodID=AS56
     *
     */
    @WebMethod(exclude = true)
    public String makeSTATIC_URL(ArrayList<Integer> id_l, String current_tbl_nm, String doc_root) {
        Collections.sort(id_l);
        String idlist = id_l.toString().replaceAll("[^0-9,]", "");
        String encripted_nm = encript_sha1("STATIC_URL_" + current_tbl_nm + "_" + idlist);
        File file = new File(doc_root + encripted_nm);
        if (file.isFile()) {
        } else {
            writeResultsToFile(current_tbl_nm + "||" + idlist, getDocRoot() + encripted_nm);
        }

        return encripted_nm;
    }

    /**
     * MethodID=AS57
     *
     */
    @WebMethod(exclude = true)
    public String encript_sha1(String intext) {
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

    /**
     * MethodID=AS58
     *
     */
    @WebMethod(exclude = true)
    public String getDocRoot() {
        if (doc_root == null) {
            try {
                try {
                    Connection ncon = getDatasource_data_view().getConnection();
                    if (!ncon.isClosed()) {
                        Statement st_1 = ncon.createStatement();
                        ResultSet r_1 = st_1.executeQuery("select param_value from " + get_correct_table_name("config") + " where name='SERVER_DOCROOT'");
                        while (r_1.next()) {
                            doc_root = r_1.getString("param_value");
                        }
                    }
                    if (!ncon.isClosed()) {
                        ncon.close();
                    }
                } catch (SQLException ex) {
                    System.out.println("Error ASS58a " + ex.getMessage());
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return doc_root;
    }

    /**
     * MethodID=AS59
     *
     */
    @WebMethod(exclude = true)
    private void writeResultsToFile(Object indata, String file_nm) {
        if (indata != null && file_nm != null) {
            try {
                FileOutputStream fos = new FileOutputStream(file_nm);
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

    /**
     * MethodID=AS60
     *
     */
    @WebMethod(exclude = true)
    private Object readResultsFromFile(String file_nm) {
        File test = new File(file_nm);
        if (test.isFile() && test.canRead()) {
            Object auth_ob = null;
            try {
                FileInputStream in = new FileInputStream(file_nm);
                ObjectInputStream obin = new ObjectInputStream(in);
                auth_ob = obin.readObject();
                obin.close();
            } catch (StreamCorruptedException ex) {
                System.out.println("Error 37: Security exception, the authentication file is corrupted or has been modified by third party");
            } catch (ClassNotFoundException ex) {
                System.out.println("Error 38: accessing authentication file");
            } catch (IOException ex) {
                System.out.println("Error 38: accessing authentication file");
            }

            try {
                if (auth_ob != null) {
                    return auth_ob;
                }
            } catch (Exception ex) {
                System.out.println("Error 38: accessing authentication file");
            }
        }
        return null;
    }

    /**
     * MethodID=AS61
     *
     */
    @WebMethod(exclude = true)
    private String getWebServerName() {
        if (Constants.server_name == null) {
            try {
                try {
                    Connection ncon = getDatasource_data_view().getConnection();
                    if (!ncon.isClosed()) {
                        Statement st_1 = ncon.createStatement();
                        String config_tbl = get_correct_table_name("config");
                        if (config_tbl != null) {
                            ResultSet r_1 = st_1.executeQuery("select param_value from " + config_tbl + " where name='S_SERVER_NAME'");
                            while (r_1.next()) {
                                Constants.server_name = r_1.getString("param_value");
                            }
                        }
                    }
                    if (!ncon.isClosed()) {
                        ncon.close();
                    }
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return Constants.server_name;
    }

    /**
     * MethodID=AS61.1
     *
     */
    @WebMethod(exclude = true)
    private String getServerRoot() {
        if (Constants.server_root == null) {
            try {
                try {
                    Connection ncon = getDatasource_data_view().getConnection();
                    if (!ncon.isClosed()) {
                        Statement st_1 = ncon.createStatement();
                        String config_tbl = get_correct_table_name("config");
                        if (config_tbl != null) {
                            ResultSet r_1 = st_1.executeQuery("select param_value from " + config_tbl + " where name='S_SERVER_ROOT'");
                            while (r_1.next()) {
                                Constants.server_root = r_1.getString("param_value");
                            }
                        }
                    }
                    if (!ncon.isClosed()) {
                        ncon.close();
                    }
                } catch (SQLException ex) {
                    System.out.println("Error AS61.1.a " + ex.getMessage());
                }


            } catch (Exception ex) {
                System.out.println("Error AS61.1.b " + ex.getMessage());
            }
        }

        return Constants.server_root;
    }

    /**
     * MethodID=AS61.8
     *
     */
    @WebMethod(exclude = true)
    private String getAdminEmail() {
        if (Constants.admin_email == null) {
            try {
                try {
                    Connection ncon = getDatasource_data_update().getConnection();
                    if (!ncon.isClosed()) {
                        Statement st_1 = ncon.createStatement();
                        String config_tbl = get_correct_table_name("config");
                        if (config_tbl != null) {
                            ResultSet r_1 = st_1.executeQuery("select param_value from " + config_tbl + " where name='ADMIN_EMAIL'");
                            while (r_1.next()) {
                                Constants.admin_email = r_1.getString("param_value");
                            }
                        }
                    }
                    if (!ncon.isClosed()) {
                        ncon.close();
                    }
                } catch (SQLException ex) {
                    System.out.println("Error AS61.8.a " + ex.getMessage());
                }


            } catch (Exception ex) {
                System.out.println("Error AS61.8.b " + ex.getMessage());
            }
        }

        return Constants.admin_email;
    }

    /**
     * MethodID=AS61.3
     *
     */
    @WebMethod(exclude = true)
    public String[] getDeleteCode() {
        String[] delete_info = new String[2];
        try {
            try {
                Connection ncon = getDatasource_data_update().getConnection();
                if (!ncon.isClosed()) {
                    Statement st_1 = ncon.createStatement();
                    String config_tbl = get_correct_table_name("config");
                    if (config_tbl != null) {
                        ResultSet r_1 = st_1.executeQuery("select param_value from " + config_tbl + " where name='" + DELETE_REQUEST_CODE_FLAG + "'");
                        while (r_1.next()) {
                            String c_val = r_1.getString("param_value");
                            if (c_val != null) {
                                if (c_val.indexOf("|") > 2) {
                                    delete_info = c_val.trim().split("\\|");
                                    delete_request_code = delete_info[0];
                                    if (delete_info[1].matches("[0-9]+")) {
                                        Date d = new Date(new Long(delete_info[1]));
                                        delete_request_code_max_validity = d.toString();
                                    }
                                } else {
                                    delete_request_code = delete_info[0];
                                    delete_info[0] = c_val;
                                }
                            }
                        }
                    }
                }
                if (!ncon.isClosed()) {
                    ncon.close();
                }
            } catch (SQLException ex) {
                System.out.println("Error AS61.3.a " + ex.getMessage());
            }
        } catch (Exception ex) {
            System.out.println("Error AS61.3.b " + ex.getMessage());
        }

        return delete_info;
    }

    /**
     * MethodID=AS61.2
     *
     */
    @WebMethod(exclude = true)
    private int get_person_id(String current_user) {
//        if (Constants.server_root == null) {
        int user_id = -1;
        try {
            try {
                Connection ncon = getDatasource_data_view().getConnection();
                if (!ncon.isClosed()) {
                    Statement st_1 = ncon.createStatement();
                    String person_tbl = get_correct_table_name("person");
                    if (person_tbl != null) {
                        ResultSet r_1 = st_1.executeQuery("select id from " + person_tbl + " where  email='" + current_user + "'");
                        while (r_1.next()) {
                            user_id = r_1.getInt(1);
                        }
                    }
                }
                if (!ncon.isClosed()) {
                    ncon.close();
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }


        } catch (Exception ex) {
            ex.printStackTrace();
        }
//        }

        return user_id;
    }

    /**
     * MethodID=AS62
     *
     */
    @WebMethod(exclude = true)
    private String getWebWSDLlocName() {
        if (wsdl_URL == null) {
            try {
                try {
                    Connection ncon = getDatasource_data_view().getConnection();
                    if (!ncon.isClosed()) {
                        Statement st_1 = ncon.createStatement();
                        String config_tbl = get_correct_table_name("config");
                        if (config_tbl != null) {
                            ResultSet r_1 = st_1.executeQuery("select param_value from " + config_tbl + " where name='WSDL'");
                            while (r_1.next()) {
                                wsdl_URL = r_1.getString("param_value");
                            }
                        }
                    }
                    if (!ncon.isClosed()) {
                        ncon.close();
                    }
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }


            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        return wsdl_URL;
    }

    /**
     * MethodID=AS62
     *
     */
    @WebMethod(exclude = true)
    private int get_userlevel(String current_user) {
        int level = 0;
        if (current_user != null) {
            if (user2level_map == null) {
                user2level_map = new HashMap<>();
            }
            if (user2level_map.containsKey(current_user)) {
                level = user2level_map.get(current_user);
            } else {
                try {
                    getDatasource_users();
                    if (dataSource_users != null) {
                        try {
                            Connection ncon = dataSource_users.getConnection();
                            if (!ncon.isClosed()) {
                                Statement st_1 = ncon.createStatement();
                                ResultSet r_1 = st_1.executeQuery("select level from " + DATABASE_USERS + ".useraccounts" + " where  email='" + current_user + "'");
                                while (r_1.next()) {
                                    level = r_1.getInt("level");
                                }


                            }
                            if (!ncon.isClosed()) {
                                ncon.close();
                            }
                        } catch (SQLException ex) {
                            ex.printStackTrace();
                        }
                    }

                } catch (Exception ex) {
                }
            }
        }

        user2level_map.put(current_user, level);
        return level;
    }

    /**
     * MethodID=AS63
     *
     */
    @WebMethod(exclude = true)
    private String index_tagsources(String table_nm, DataSource datasource) {
        String result_msg = "";
        boolean table_ok = false;
        Connection conn = null;
        try {
            try {
                if (datasource != null) {
                    conn = datasource.getConnection();
                    if (table_nm != null) {
                        boolean key_ref_found = false;
                        boolean parent_id_ref_found = false;
                        boolean parent_id_constraint_found = false;
                        boolean unique_identification_found = false;
                        boolean unique_identification_contraints_found = false;
                        HashMap<String, String[]> constraints_map = get_key_contraint_names(table_nm, getDatasource_users());
                        String[] fr_keynm_a = null;
                        String[] keynm_a = null;
                        if (constraints_map.containsKey(Constants.FOREIGN_KEY_NAMES_FLAG)) {
                            fr_keynm_a = constraints_map.get(Constants.FOREIGN_KEY_NAMES_FLAG);
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
                                    result_msg = result_msg + "key_ref_" + table_nm_for_indices + "... OK\n";
                                    String post_porc_sql = "CREATE INDEX parent_id_ref_" + table_nm_for_indices + " ON " + table_nm + "(parent_id)";
                                    result = conn.createStatement().executeUpdate(post_porc_sql);
                                    if (result >= 0) {
                                        result_msg = result_msg + "index  parent_id_ref_" + table_nm_for_indices + "... OK\n";
                                    }
                                }
                                if (!parent_id_constraint_found) {
                                    if (result >= 0) {
                                        String post_porc_sql = "ALTER TABLE " + table_nm + " add CONSTRAINT parent_id_ref_" + table_nm_for_indices + "  FOREIGN KEY (parent_id) REFERENCES " + table_nm + "(id)";
                                        result = conn.createStatement().executeUpdate(post_porc_sql);
                                        if (result >= 0) {
                                            result_msg = result_msg + "Constaint parent_id_ref_" + table_nm_for_indices + " ... OK\n";
                                        }
                                    } else {
                                        result_msg = result_msg + "Error 59a: could not add constaint 1, this table may not be usable\n";
                                    }
                                }
                                if (result >= 0) {
                                    if (!unique_identification_found) {
                                        String post_porc_sql = "CREATE INDEX unique_identification_" + table_nm_for_indices + " ON " + table_nm + "(name,parent_id)";
                                        result = conn.createStatement().executeUpdate(post_porc_sql);
                                        if (result >= 0) {
                                            result_msg = result_msg + "unique_identification_" + table_nm_for_indices + " ... OK\n";
                                        }
                                    }

                                    if (result >= 0) {
                                        if (!unique_identification_contraints_found) {
                                            String post_porc_sql = "ALTER table " + table_nm + " add CONSTRAINT unique_identification_" + table_nm_for_indices + " UNIQUE(name,parent_id)";
                                            result = conn.createStatement().executeUpdate(post_porc_sql);
                                            if (result >= 0) {
                                                table_ok = true;
                                                result_msg = result_msg + "Constraint unique_identification_" + table_nm_for_indices + " ... OK\n";
                                            } else {
                                                result_msg = result_msg + "Error 59b: could not add constraint2, this table may not be usable\n";
                                            }
                                        } else {
                                            table_ok = true;
                                        }
                                    } else {
                                        result_msg = result_msg + "Error 59c: could not add constaint 1, this table may not be usable\n";
                                    }
                                } else {
                                    result_msg = result_msg + "Error 49d: could not add unique identification, this table may not be usable\n";
                                }

                            } else {
                                result_msg = result_msg + "Error 59e: could not add primary key, this table may not be usable\n";
                            }
                            if (!table_ok) {
                                result_msg = result_msg + " Removing table " + table_nm + ". There were errors\n";
                                conn.createStatement().executeUpdate("DROP TABLE " + table_nm);
                            } else {
                                result_msg = result_msg + "Indexing table " + table_nm + " ... OK\n";
                            }
                        }
                    } else {
                        result_msg = result_msg + "Erorr: table null\n";
                    }
                    conn.close();
                } else {
                }
            } catch (SQLException ex) {
                result_msg = result_msg + "Error 58h: " + ex.getMessage() + "\n";
            } finally {
                close(conn, null, null);
            }

        } catch (Exception ex) {
            result_msg = result_msg + "Error 59h: " + ex.getMessage() + "\n";
        }
        return result_msg;

    }

    /**
     * MethodID=AS64
     *
     */
    @WebMethod(exclude = true)
    private int refreshTableTOFeatures(DataSource datasource) {
        int result = 0;
        ArrayList<String> sql_l = new ArrayList<>();
        sql_l.add("DROP TABLE " + DATABASE_NAME_DATA + ".tablename2feature");
        sql_l.add("CREATE TABLE " + DATABASE_NAME_DATA + ".tablename2feature(id int NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1)  PRIMARY KEY,table_name varchar(128),column_nm varchar(128) default 'name',description varchar(256),category varchar(128),simplename varchar(256),table_description varchar(256) default 'NA',search_description varchar(128) default 'NA',usageinfo varchar(256) default null,showinsearch int default 0,taggable int default 0,avialable int default 0)");
        sql_l.add("insert into " + DATABASE_NAME_DATA + ".tablename2feature(table_name,column_nm) (select (SELECT TABLENAME from  SYS.SYSTABLES where TABLEID=SYS.SYSCOLUMNS.REFERENCEID), COLUMNNAME from SYS.SYSCOLUMNS WHERE (SELECT TABLENAME from SYS.SYSTABLES where CAST(SYS.SYSTABLES.TABLETYPE AS CHAR(1))='T' and SYS.SYSTABLES.SCHEMAID=(select SCHEMAID from SYS.SYSSCHEMAS where CAST(SYS.SYSSCHEMAS.SCHEMANAME AS VARCHAR(128))='" + DATABASE_NAME_DATA + "') and TABLEID=SYS.SYSCOLUMNS.REFERENCEID) is not null )");
        sql_l.add("update " + DATABASE_NAME_DATA + ".tablename2feature set description='Personal information, such as name or email' where table_name='person' and column_nm='name' ");
        sql_l.add("update " + DATABASE_NAME_DATA + ".tablename2feature set description='file name or the location path' where table_name='files' and column_nm='name'");
        sql_l.add("update " + DATABASE_NAME_DATA + ".tablename2feature set description='report details (report is a collection of files)' where table_name='report' and  column_nm='name'");
        sql_l.add("update " + DATABASE_NAME_DATA + ".tablename2feature set description='sample name' where table_name='sampledetails' and column_nm='name' ");
        sql_l.add("update " + DATABASE_NAME_DATA + ".tablename2feature set description='sample id' where table_name='sampledetails' and column_nm='sample_id' ");
        sql_l.add("update " + DATABASE_NAME_DATA + ".tablename2feature set description='donor details (cell line, donor id, tissue etc.)'  where table_name='donordetails' and column_nm='name'");
        sql_l.add("update " + DATABASE_NAME_DATA + ".tablename2feature set description='Report batch is a collection of reports. This is the unique name of it'  where table_name='report_batch' and column_nm='name'");
        sql_l.add("update " + DATABASE_NAME_DATA + ".tablename2feature set description='The parent of the donor. E.g. if the sample was from a tissue, then the organ where the tissue was obtained was the parent ' where table_name='donordetails' and column_nm='parent_id' ");
        sql_l.add("update " + DATABASE_NAME_DATA + ".tablename2feature set description='Description of donor' where table_name='donordetails' and column_nm='description' ");
        sql_l.add("update " + DATABASE_NAME_DATA + ".tablename2feature set description='Internal identifier for the donor (e.g. An internal  identifier referring to patient)' where table_name='donordetails' and column_nm='internal_identifier' ");
        sql_l.add("update " + DATABASE_NAME_DATA + ".tablename2feature set description='A serial number for the donor (e.g. identifier used by the company where the donor was purchased)' where table_name='donordetails' and column_nm='serial_number' ");
        sql_l.add("update " + DATABASE_NAME_DATA + ".tablename2feature set description='A ' where table_name='donordetails' and column_nm='name' ");
        sql_l.add("update " + DATABASE_NAME_DATA + ".tablename2feature set description='The size of the file in bytes' where table_name='files' and column_nm='filesize' ");
        sql_l.add("update " + DATABASE_NAME_DATA + ".tablename2feature set description='A description about the file and its content' where table_name='files' and column_nm='description' ");
        sql_l.add("update " + DATABASE_NAME_DATA + ".tablename2feature set description='The MD5 check-sum value for the file' where table_name='files' and column_nm='checksum' ");
        sql_l.add("update " + DATABASE_NAME_DATA + ".tablename2feature set description='The absolute path for the file' where table_name='files' and column_nm='location' ");
        sql_l.add("update " + DATABASE_NAME_DATA + ".tablename2feature set description='The date last modification was done' where table_name='files' and column_nm='lastmodified' ");
        sql_l.add("update " + DATABASE_NAME_DATA + ".tablename2feature set description='The ownership information as reported by the filesystem' where table_name='files' and column_nm='ownergroup' ");
        sql_l.add("update " + DATABASE_NAME_DATA + ".tablename2feature set description='An identifier referring to a type description in the filetype table ' where table_name='files' and column_nm='filetype_id' ");
        sql_l.add("update " + DATABASE_NAME_DATA + ".tablename2feature set description='A unique name for the file, including the server name, and absolute path' where table_name='files' and column_nm='name' ");
        sql_l.add("update " + DATABASE_NAME_DATA + ".tablename2feature set description='This table used in the tagging process. The files_id refers to an entry in the files table' where table_name='files2tags' and column_nm='files_id' ");
        sql_l.add("update " + DATABASE_NAME_DATA + ".tablename2feature set description='This table used in the tagging process. A description about the tag' where table_name='files2tags' and column_nm='description' ");
        sql_l.add("update " + DATABASE_NAME_DATA + ".tablename2feature set description='This table used in the tagging process. a unique name for the tag, usually generated automatically' where table_name='files2tags' and column_nm='name' ");
        sql_l.add("update " + DATABASE_NAME_DATA + ".tablename2feature set description='A link to more descriptions about the tag.' where table_name='files2tags' and column_nm='link_to_feature' ");
        sql_l.add("update " + DATABASE_NAME_DATA + ".tablename2feature set description='The connection between the files and the reports, this is the report identifier' where table_name='files2report' and column_nm='report_id' ");
        sql_l.add("update " + DATABASE_NAME_DATA + ".tablename2feature set description='The connection between the files and the reports, this is the files identifier' where table_name='files2report' and column_nm='files_id' ");
        sql_l.add("update " + DATABASE_NAME_DATA + ".tablename2feature set description='The connection between the files and the samples, this is a short description about the sample and the file' where table_name='files2sampledetails' and column_nm='description' ");
        sql_l.add("update " + DATABASE_NAME_DATA + ".tablename2feature set description='The connection between the files and the samples, this is the files id' where table_name='files2sampledetails' and column_nm='files_id' ");
        sql_l.add("update " + DATABASE_NAME_DATA + ".tablename2feature set description='The connection between the files and the samples, this is the sample detail id' where table_name='files2sampledetails' and column_nm='sampledetails_id' ");
        sql_l.add("update " + DATABASE_NAME_DATA + ".tablename2feature set description='The connection between the files, sample and the samples , this is the donor where the sample is coming from' where table_name='files2sampledetails' and column_nm='donordetails_id' ");
        sql_l.add("update " + DATABASE_NAME_DATA + ".tablename2feature set description='The connection between the files, a unique name for this relationship between the file and the sample' where table_name='files2sampledetails' and column_nm='name' ");
        sql_l.add("update " + DATABASE_NAME_DATA + ".tablename2feature set description='The file extension of the files type' where table_name='filetype' and column_nm='fileextension' ");
        sql_l.add("update " + DATABASE_NAME_DATA + ".tablename2feature set description='A online resource showing how to use the file' where table_name='filetype' and column_nm='linktoSoftware' ");
        sql_l.add("update " + DATABASE_NAME_DATA + ".tablename2feature set description='A short description about the file type' where table_name='filetype' and column_nm='description' ");
        sql_l.add("update " + DATABASE_NAME_DATA + ".tablename2feature set description='Type of the file, e.g. text, XML, binary' where table_name='filetype' and column_nm='type' ");
        sql_l.add("update " + DATABASE_NAME_DATA + ".tablename2feature set description='A unique name for the file type' where table_name='filetype' and column_nm='name' ");
        sql_l.add("update " + DATABASE_NAME_DATA + ".tablename2feature set description='A unique name for the relationship (optional)' where table_name='file_hierarchy' and column_nm='name' ");
        sql_l.add("update " + DATABASE_NAME_DATA + ".tablename2feature set description='The id of the parent file from the files table' where table_name='file_hierarchy' and column_nm='parentfile_id' ");
        sql_l.add("update " + DATABASE_NAME_DATA + ".tablename2feature set description='The id of the child file from the files table' where table_name='file_hierarchy' and column_nm='childfile_id' ");
        sql_l.add("update " + DATABASE_NAME_DATA + ".tablename2feature set description='The id of the address from the address table' where table_name='person' and column_nm='address_id' ");
        sql_l.add("update " + DATABASE_NAME_DATA + ".tablename2feature set description='Where do you work' where table_name='person' and column_nm='Organizationinfo' ");
        sql_l.add("update " + DATABASE_NAME_DATA + ".tablename2feature set description='A unique name, usually the email' where table_name='person' and column_nm='name' ");
        sql_l.add("update " + DATABASE_NAME_DATA + ".tablename2feature set description='email' where table_name='person' and column_nm='email' ");
        sql_l.add("update " + DATABASE_NAME_DATA + ".tablename2feature set description='First name' where table_name='person' and column_nm='lastnm' ");
        sql_l.add("update " + DATABASE_NAME_DATA + ".tablename2feature set description='Last name' where table_name='person' and column_nm='firstnm' ");
        sql_l.add("update " + DATABASE_NAME_DATA + ".tablename2feature set description='A free text description about the report' where table_name='report' and column_nm='description' ");
        sql_l.add("update " + DATABASE_NAME_DATA + ".tablename2feature set description='The type of the report, an identifier referring to the reportType table' where table_name='report' and column_nm='reporttype_id' ");
        sql_l.add("update " + DATABASE_NAME_DATA + ".tablename2feature set description='The person reporting, an identifier referring to the person table' where table_name='report' and column_nm='reporter_id' ");
        sql_l.add("update " + DATABASE_NAME_DATA + ".tablename2feature set description='The batch this report belongs to, an identifier referring to the report_batch table' where table_name='report' and column_nm='report_batch_id' ");
        sql_l.add("update " + DATABASE_NAME_DATA + ".tablename2feature set description='When was the report created. Usually added automatically' where table_name='report' and column_nm='entryDate' ");
        sql_l.add("update " + DATABASE_NAME_DATA + ".tablename2feature set description='A unique name for the report, usually the directory name where the files are stored' where table_name='report' and column_nm='name' ");
        sql_l.add("update " + DATABASE_NAME_DATA + ".tablename2feature set description='The id of the report which the properties is attached to' where table_name='report2tags' and column_nm='report_id' ");
        sql_l.add("update " + DATABASE_NAME_DATA + ".tablename2feature set description='A free text description of this property' where table_name='report2tags' and column_nm='description' ");
        sql_l.add("update " + DATABASE_NAME_DATA + ".tablename2feature set description='An optional unique name the property of the report' where table_name='report2tags' and column_nm='name' ");
        sql_l.add("update " + DATABASE_NAME_DATA + ".tablename2feature set description='A link pointing to a feature or a additional information table, this would normally be automatically generated ' where table_name='report2tags' and column_nm='link_to_feature' ");
        sql_l.add("update " + DATABASE_NAME_DATA + ".tablename2feature set description='A description about the report sample relationship' where table_name='report2sampledetails' and column_nm='description' ");
        sql_l.add("update " + DATABASE_NAME_DATA + ".tablename2feature set description='The id of the report, pointing to a entry in the report table' where table_name='report2sampledetails' and column_nm='report_id' ");
        sql_l.add("update " + DATABASE_NAME_DATA + ".tablename2feature set description='The id of the sampledetails table, where the sample details are described ' where table_name='report2sampledetails' and column_nm='sampledetails_id' ");
        sql_l.add("update " + DATABASE_NAME_DATA + ".tablename2feature set description='The id of the donor_detials, where the donor of the sample is described' where table_name='report2sampledetails' and column_nm='donordetails_id' ");
        sql_l.add("update " + DATABASE_NAME_DATA + ".tablename2feature set description='An optional unique name for this relationship' where table_name='report2sampledetails' and column_nm='name' ");
        sql_l.add("update " + DATABASE_NAME_DATA + ".tablename2feature set description='A unique name for the report batch, a report batch is a collection of reports' where table_name='report_batch' and column_nm='name' ");
        sql_l.add("update " + DATABASE_NAME_DATA + ".tablename2feature set description='The report batch which the property is attached to' where table_name='report_batch2tags' and column_nm='report_batch_id' ");
        sql_l.add("update " + DATABASE_NAME_DATA + ".tablename2feature set description='A free text description of this property' where table_name='report_batch2tags' and column_nm='description' ");
        sql_l.add("update " + DATABASE_NAME_DATA + ".tablename2feature set description='An optional name for this relationship' where table_name='report_batch2tags' and column_nm='name' ");
        sql_l.add("update " + DATABASE_NAME_DATA + ".tablename2feature set description='A link pointing to a feature or a additional information table, this would normally be automatically generated' where table_name='report_batch2tags' and column_nm='link_to_feature' ");
        sql_l.add("update " + DATABASE_NAME_DATA + ".tablename2feature set description='The id of the child report_batch. This is the report batch which has inherited some property for the parent' where table_name='report_batch_hierarchy' and column_nm='childreport_batch_id' ");
        sql_l.add("update " + DATABASE_NAME_DATA + ".tablename2feature set description='The id of the parent report_batch. This is the report batch which is inheriting some property for the child' where table_name='report_batch_hierarchy' and column_nm='parentreport_batch_id' ");
        sql_l.add("update " + DATABASE_NAME_DATA + ".tablename2feature set description='An optional unique name for this relationship' where table_name='report_batch_hierarchy' and column_nm='name' ");
        sql_l.add("update " + DATABASE_NAME_DATA + ".tablename2feature set description='The id of the child report. This is the report which has inherited some property for the parent' where table_name='report_hierarchy' and column_nm='childreport_id' ");
        sql_l.add("update " + DATABASE_NAME_DATA + ".tablename2feature set description='The id of the parent report. This is the report which is inheriting some property for the child' where table_name='report_hierarchy' and column_nm='parentreport_id' ");
        sql_l.add("update " + DATABASE_NAME_DATA + ".tablename2feature set description='An optional unique name for this relationship' where table_name='report_hierarchy' and column_nm='name' ");
        sql_l.add("update " + DATABASE_NAME_DATA + ".tablename2feature set description='A short description about the sample' where table_name='sampledetails' and column_nm='description' ");
        sql_l.add("update " + DATABASE_NAME_DATA + ".tablename2feature set description='An internal identifier for the sample' where table_name='sampledetails' and column_nm='sample_id' ");
        sql_l.add("update " + DATABASE_NAME_DATA + ".tablename2feature set description='A unique name for the sample, this could be same as the sample_id' where table_name='sampledetails' and column_nm='name' ");
        sql_l.add("update " + DATABASE_NAME_DATA + ".tablename2feature set description='An optional name for this relationship' where table_name='donordetails_hierarchy' and column_nm='name'");
        sql_l.add("update " + DATABASE_NAME_DATA + ".tablename2feature set description='The parent of the donor. This could literally be the parent of the person donated the sample, the cell line the current donor is from, the parent tissue etc..  ' where table_name='donordetails_hierarchy' and column_nm='parentdonordetails_id'");
        sql_l.add("update " + DATABASE_NAME_DATA + ".tablename2feature set description='The child component of this relationship' where table_name='donordetails_hierarchy' and column_nm='childdonordetails_id'");
        sql_l.add("update " + DATABASE_NAME_DATA + ".tablename2feature set description='An optional name for this relationship' where table_name='files_hierarchy' and column_nm='name'");
        sql_l.add("update " + DATABASE_NAME_DATA + ".tablename2feature set description='The parent file id, refer to a id in the files table.' where table_name='files_hierarchy' and column_nm='parentfiles_id'");
        sql_l.add("update " + DATABASE_NAME_DATA + ".tablename2feature set description='The child file id, refer to a id in the files table' where table_name='files_hierarchy' and column_nm='childfiles_id'");
        sql_l.add("update " + DATABASE_NAME_DATA + ".tablename2feature set description='The unique name for the authority assigning this id' where table_name='idsource' and column_nm='name'");
        sql_l.add("update " + DATABASE_NAME_DATA + ".tablename2feature set description='The unique name for the authority assigning this id.  This could be as same as the name' where table_name='idsource' and column_nm='databasename'");
        sql_l.add("update " + DATABASE_NAME_DATA + ".tablename2feature set description='The report component of this relationship' where table_name='report2idsource' and column_nm='report_id'");
        sql_l.add("update " + DATABASE_NAME_DATA + ".tablename2feature set description='The reference to the idsource table where the assigning authority is defined' where table_name='report2idsource' and column_nm='idsource_id'");
        sql_l.add("update " + DATABASE_NAME_DATA + ".tablename2feature set description='The identifier' where table_name='report2idsource' and column_nm='name'");
        sql_l.add("update " + DATABASE_NAME_DATA + ".tablename2feature set description='A short description about this report batch' where table_name='report_batch' and column_nm='description'");
        sql_l.add("update " + DATABASE_NAME_DATA + ".tablename2feature set description='A unique name for this relationship (optional)' where table_name='sampledetails_hierarchy' and column_nm='name'");
        sql_l.add("update " + DATABASE_NAME_DATA + ".tablename2feature set description='The parent sample' where table_name='sampledetails_hierarchy' and column_nm='parentsampledetails_id'");
        sql_l.add("update " + DATABASE_NAME_DATA + ".tablename2feature set description='The child sample' where table_name='sampledetails_hierarchy' and column_nm='childsampledetails_id'");
        sql_l.add("update " + DATABASE_NAME_DATA + ".tablename2feature set description='biological_ontologies' where table_name='biological_ontologies' and column_nm='table_name'");
        sql_l.add("update " + DATABASE_NAME_DATA + ".tablename2feature set description='Connecting samples with standard tags' where table_name='sampledetails2tags' and column_nm='description'");
        sql_l.add("update " + DATABASE_NAME_DATA + ".tablename2feature set description='Connecting samples with standard tags, the link to the  feature ' where table_name='sampledetails2tags' and column_nm='link_to_feature'");
        sql_l.add("update " + DATABASE_NAME_DATA + ".tablename2feature set description='Connecting samples with standard tags, the sample which is tagged' where table_name='sampledetails2tags' and column_nm='sampledetails2tags_id'");
        sql_l.add("update " + DATABASE_NAME_DATA + ".tablename2feature set description='Connecting samples with standard tags, optional unique name' where table_name='sampledetails2tags' and column_nm='name'");
        sql_l.add("update " + DATABASE_NAME_DATA + ".tablename2feature set description='Absolute path of the file' where table_name='FILES2PATH' and column_nm='FILEPATH'");

        sql_l.add("update " + DATABASE_NAME_DATA + ".tablename2feature set showinsearch=1 where table_name not in ('tablename2feature','advancedqueryconstructor','errors','fieldhelp','queryExpander','tabledescription','tmp')");
        sql_l.add("update " + DATABASE_NAME_DATA + ".tablename2feature set taggable=1 where table_name in ('files', 'report','report_batch','sampledetails')");
        sql_l.add("update " + DATABASE_NAME_DATA + ".tablename2feature set avialable=1 where table_name not in ('tablename2feature','advancedqueryconstructor','errors','fieldhelp','queryExpander','tabledescription','tmp')");
        sql_l.add("update " + DATABASE_NAME_DATA + ".tablename2feature set category='Other'");

        sql_l.add("update " + DATABASE_NAME_DATA + ".tablename2feature set category='Tags' where table_name like '%2tags%'");
        sql_l.add("update " + DATABASE_NAME_DATA + ".tablename2feature set category='File name or location' where table_name in ('files', 'files2path')");
        sql_l.add("update " + DATABASE_NAME_DATA + ".tablename2feature set category='Name of a Reports or a Report batch' where table_name in ('report', 'report_batch')");
        sql_l.add("update " + DATABASE_NAME_DATA + ".tablename2feature set category='Name of a donors or a sample' where table_name in ('sampledetails', 'donordetails')");

        sql_l.add("update " + DATABASE_NAME_DATA + ".tablename2feature set simplename=table_name");
        sql_l.add("update " + DATABASE_NAME_DATA + ".tablename2feature set simplename='File metadata' where table_name='files'");
        sql_l.add("update " + DATABASE_NAME_DATA + ".tablename2feature set simplename='File tags'  where table_name='files2tags'");
        Connection conn = null;
        try {
            if (datasource != null) {
                conn = datasource.getConnection();
                if (!conn.isClosed()) {
                    Statement stm = conn.createStatement();
                    try {
                        stm.executeUpdate(sql_l.get(0));
                    } catch (Exception ex) {
                    }
                    for (int i = 1; (i < sql_l.size() && result >= 0); i++) {
                        try {
                            result = stm.executeUpdate(sql_l.get(i));
                        } catch (SQLException ex) {
                            result = -1;
                            System.out.println("Error AS64a: " + sql_l.get(i) + "\n" + ex.getMessage());
                        }
                    }
                    stm.close();
                }
                HashMap<String, String> config_map = new HashMap<>();
                config_map.put("MEMORIZING_REQUESTED", "1");
                setConfig(conn, config_map);
                conn.close();
            }

        } catch (SQLException ex) {
            System.out.println("Error 59g: " + ex.getMessage());
        } finally {
            close(conn, null, null);
        }


        return result;
    }

    /**
     * MethodID=AS65
     *
     */
    @WebMethod(exclude = true)
    private String randomstring_num() {
        int n = 6;
        byte[] b = new byte[n];
        for (int i = 0; i < n; i++) {
            int c_val2 = rand('0', '9');
            b[i] = (byte) c_val2;

        }
        String pass = new String(b);
        return pass;
    }

    /**
     * MethodID=AS66
     *
     */
    @WebMethod(exclude = true)
    private static int sendmail(String recipient_mail, String subject, String bodytext) {
        int result = -1;
        try {
            Message msg = new MimeMessage(getDatasource_email());
            msg.setSubject(subject);
            msg.setRecipient(Message.RecipientType.TO, new InternetAddress(recipient_mail, recipient_mail));
            msg.setFrom(new InternetAddress(Constants.admin_email, "eGenVar admin"));
//            instruct_map.get("--fromaddress");
            BodyPart messageBodyPart = new MimeBodyPart();
            messageBodyPart.setText(bodytext);
            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(messageBodyPart);
            msg.setContent(multipart);
            Transport.send(msg);
            result = 1;
        } catch (UnsupportedEncodingException ex) {
            result = -4;
        } catch (MessagingException ex) {
            result = -5;
        }
        return result;
    }

    /**
     * MethodID=AS67
     *
     */
    @WebMethod(exclude = true)
    private String randomstring() {
        int n = 8;
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

    /**
     * MethodID=AS67
     *
     */
    @WebMethod(exclude = true)
    private int sendUserCodeConfirmation(String email, String confirmationcode) {
        int result = -1;
        try {
            
            String message = "Dear  " + email + ",\n" + "  "
                    + "Thank you for registering with the eGenVar data-management system. \n"
                    + "Please use the confirmation code below to confirm your registration. \n"
                    + "If you did not try to register with the eGenVar data-management system, \n"
                    + "please report this to "+getAdminEmail()+". \n"
                    + "\n\nYour confirmation code : " + confirmationcode + "\n\n"
                    + "Web interface URL: " + getWebServerName() + "\n"
                    + "WSDL URL for egenvar tool " + getWebWSDLlocName() + "\n\n"
                    + "Regards,\n"
                    + "eGenVar team\n";
            result = sendmail(email, "eGenVar user authentication", message);
        } catch (Exception ex) {
        }
        return result;
    }

    /**
     * MethodID=AS68
     *
     */
    @WebMethod(exclude = true)
    public int sendUserPassword(String email, String name, String username,
            String password) {
        int result = -1;
        try {
                getAdminEmail();
            String message = "Dear " + name + ",\n" + " Thank you for registering with the eGenVar data-management System. "
                    + "Your account will be activated shortly. \n"
                    + "Please contact "+getAdminEmail()+" if your account is not active within 24h. After the activation , The user name and password given below"
                    + " can be used to access; the eGenVar data-management system, Galaxy instance and the FTP upload system.\n"
                    + "However, you can not use this username to gain SSH access at this moment.\n "
                    + "\n  User Name: " + username + "\n  Password: " + password + "\n\n"
                    + "Web interface URL: " + getWebServerName() + "\n"
                    + "WSDL URL for egenvar tool " + getWebWSDLlocName() + "\n\n"
                    + "Regards,\neGenVar team ";

            result = sendmail(email, "eGenVar user authentication", message);

        } catch (Exception ex) {
        }
        return result;
    }

    /**
     * MethodID=AS73
     *
     */
    @WebMethod(exclude = true)
    public static int sendMessage(String email, String name, String messsage, String sender) {
        int result = -1;
        try {
            if (sender == null) {
                sender = "eGenVar user authentication";
            }
            result = sendmail(email, sender, messsage);// 
        } catch (Exception ex) {
        }
        return result;
    }

    /**
     * MethodID=AS69
     *
     */
    @WebMethod(exclude = true)
    private int insert_user_account(HashMap<String, String> acouunt_map) {
        int result = -1;
        Connection conn_users = null;
        try {
            conn_users = getDatasource_users().getConnection();
            if (conn_users != null) {
                String proc = "call " + DATABASE_USERS + ".insert_user(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
                CallableStatement cs = conn_users.prepareCall("{" + proc + "}");
                cs.setString(1, DATABASE_USERS + ".spliter_SplitValues");
                cs.setString(2, DATABASE_USERS + ".useraccounts");
                cs.setString(3, DATABASE_USERS + ".groupnmtoGID");
                cs.setString(4, DATABASE_USERS + ".groups");
                cs.setString(5, acouunt_map.get("email"));
                cs.setString(6, acouunt_map.get("email"));
                cs.setString(7, acouunt_map.get("email"));
                cs.setString(8, acouunt_map.get("password"));
                cs.setString(9, ",");
                cs.setString(10, "admin,Uploader,Editor,Deletor");
                cs.setInt(11, 1);
                cs.setInt(12, 9);
                cs.setString(13, acouunt_map.get(acouunt_map.get("ip")));
                cs.setString(14, "99999");
                cs.setString(15, DATABASE_USERS + ".SHA1");
                cs.registerOutParameter(16, Types.VARCHAR);
                result = cs.executeUpdate();
                cs.close();
            }
            if (conn_users != null && !conn_users.isClosed()) {
                conn_users.close();
            }
        } catch (SQLException ex) {
        } finally {
            close(conn_users, null, null);
        }
        return -1;
    }

    /**
     * MethodID=AS71
     *
     */
    @WebMethod(exclude = true)
    public int getQueryTimeOut() {
        if (QUERY_TIME_OUT < 0) {
            QUERY_TIME_OUT = 300;
            if (getDatasource_data_view() != null) {
                try {
                    Connection ncon = getDatasource_data_view().getConnection();
                    Statement st_1 = null;
                    ResultSet r_1 = null;
                    if (!ncon.isClosed()) {
                        try {
                            st_1 = ncon.createStatement();
                            String s_1 = "select PARAM_VALUE from " + get_correct_table_name("config") + " where name='WEBSERVICE_TIMEOUT'";
                            r_1 = st_1.executeQuery(s_1);
                            while (r_1.next()) {
                                QUERY_TIME_OUT = r_1.getInt("PARAM_VALUE");
                            }
                        } finally {
                            close(ncon, st_1, r_1);
                        }
                    }
                } catch (SQLException e) {
                    System.out.println("Error A71a:" + e.getMessage());
                }
            }
        }
        return QUERY_TIME_OUT;
    }

    /**
     * MethodID=AS39
     *
     */
    @WebMethod(exclude = true)
    private HashMap<String, ArrayList<String>> getConnectionmap() {
        if (connection_map == null) {
            connection_map = new HashMap<>();
            ArrayList<String> c_table_l = getTables();
            for (int i = 0; i < c_table_l.size(); i++) {
                String c_taable = c_table_l.get(i);
                HashMap<String, String[]> relat_map = get_key_contraints(c_taable, getDatasource_data_view());//relationship_map.get(table_l.get(i));                    
                String[] link_clmn_a = relat_map.get(Constants.FOREIGN_KEY_COLUMNS_FLAG);
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
        return connection_map;
    }

    /**
     * MethodID=AS40
     *
     */
    @WebMethod(exclude = true)
    public boolean getDoesitExist(String query, Connection con) {
        boolean result = false;
        Statement stm_1 = null;
        try {
            stm_1 = con.createStatement();
            if (getOneForQuery(query, stm_1) != null) {
                result = true;
            }
            close(null, stm_1, null);
        } catch (Exception ex) {
            msges = "Error AS40a: creating connections " + ex.getMessage();
        } finally {
            close(null, stm_1, null);
        }
        return result;
    }

    /**
     * MethodID=AS41
     *
     */
    @WebMethod(exclude = true)
    private int setConfig(Connection c_conn, HashMap<String, String> instruct_map) {
        int result = 0;
        try {
            if (c_conn != null && !c_conn.isClosed()) {
                PreparedStatement p_1 = c_conn.prepareStatement("update " + get_correct_table_name("config") + " set param_value=? where name=?");
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

    /**
     * MethodID=AS41
     *
     */
    @WebMethod(exclude = true)
    private int create_userprofile(String email) {
        int result = -1;
        Connection c_con = null;
        try {
            c_con = getDatasource_data_update().getConnection();
            if (c_con != null && !c_con.isClosed()) {
                String insert_stmt = "insert into " + get_correct_table_name("person") + "(name, lastnm, email) values('NA','NA','" + email + "')";
                result = c_con.createStatement().executeUpdate(insert_stmt);
                HashMap<String, String> config_map = new HashMap<>();
                config_map.put("MEMORIZING_REQUESTED", "1");
                if (setConfig(c_con, config_map) < 0) {
                }
                c_con.close();
            }
        } catch (SQLException ex) {
        } finally {
            close(c_con, null, null);
        }
        return result;
    }

//    /**
//     * MethodID=AS40
//     *
//     */
//    @WebMethod(exclude = true)
//    public String getDidYouMeanTable(String table) {
//        String result = null;
//        ArrayList<String> tables_L = getTables();
//        tables_L.add(CODE_RESULT_FLAG);
//        tables_L.add(CODE_SEARCH_FLAG);
//        if(table.length()>5){
//            
//        }
//
//        return result;
//    }
    /**
     * ______________________WEBSERVICES__________________________________
     *
     * /
     *
     * /**
     * MethodID=ASW1
     *
     */
    @WebMethod(operationName = "Authenticate")
    public String authenticate(@WebParam(name = "username") String username, @WebParam(name = "password") String password) {
        String result = "0";
        try {
            int auth_result = isAuthentic(username, password);
            if (auth_result == 1) {
                result = getauthId(username);
                if (result == null) {
                    result = randomstring(AUTHSTRINGLENGTH);
                    setAuthenticatedCleintlist(username, result);
                }
            }
        } catch (Exception ex) {
            result = "Error: " + ex.getMessage();
        }
        return result;
    }

    /**
     * MethodID=ASW2
     *
     */
    @WebMethod(operationName = "Encript")
    public String encript(@WebParam(name = "intext") String intext) {
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
                String encript_pass = formatter.toString();
                return encript_pass;
            }
        } catch (NoSuchAlgorithmException ex) {
            errors = errors + ex.getMessage();
        }
        return null;
    }

    /**
     * MethodID=ASW3
     *
     */
    @WebMethod(operationName = "listFromTable")
    public java.lang.String[] listFromTable(@WebParam(name = "username") String username, @WebParam(name = "authenticationid") String authenticationid,
            @WebParam(name = "table") String table, @WebParam(name = "field") String field, @WebParam(name = "value") String value, @WebParam(name = "selectval") String selectval, @WebParam(name = "matchOP") String matchOP) {
        String auth = getauthId(username);
        String[] result = new String[1];
        result[0] = "No match";
        if (temporarolyunavilable) {
            result[0] = SERVICE_UNAVAILABLE_MESSAGE;
        } else if (auth != null && authenticationid != null && !authenticationid.isEmpty() && authenticationid.equals(encript(auth))) {
            try {
                if (getDatasource_data_view() != null) {
                    ArrayList<String> resuklt_l = new ArrayList<>(10);
                    Connection ncon = null;
                    Statement st_1 = null;
                    ResultSet r_1 = null;
                    try {
                        ncon = getDatasource_data_view().getConnection();
                        if (!ncon.isClosed()) {
                            st_1 = ncon.createStatement();
                            value = value.replaceAll("'", "\"");
                            if (matchOP.equalsIgnoreCase("like")) {
                                value = "%" + value + "%";
                            }
                            value = value.replace("*", "%");
                            String s_1 = "SELECT " + selectval + "  from " + table + " WHERE " + field + " " + matchOP + " '" + value + "';";
                            r_1 = st_1.executeQuery(s_1);
                            while (r_1.next()) {
                                resuklt_l.add(r_1.getString(1));
                            }
                            close(ncon, st_1, r_1);
                        }
                    } catch (SQLException e) {
                        result[0] = "Error ASW3a: " + e.getMessage();
                    } finally {
                        close(ncon, st_1, r_1);
                    }
                    if (!resuklt_l.isEmpty()) {
                        result = new String[resuklt_l.size()];
                        for (int i = 0; i < result.length; i++) {
                            result[i] = resuklt_l.get(i);

                        }
                        return result;
                    }
                }
                return null;
            } catch (Exception ex) {
                result[0] = "Error ASW3b: " + ex.getMessage();
            }
        } else {
            result[0] = "Authentication failed. The local authentication key did not match the server lock for " + username;
        }
        return result;
    }

    /**
     * MethodID=ASW4
     *
     */
    @WebMethod(operationName = "listFromTableFrosubqury")
    public java.lang.String[] listFromTableFrosubqury(@WebParam(name = "username") String username, @WebParam(name = "authenticationid") String authenticationid, @WebParam(name = "query") String query, @WebParam(name = "value") String value) {
        String auth = getauthId(username);
        String[] result = new String[1];
        result[0] = "No match";
        value = value.replaceAll("\\?", "_");
        value = refineentry(value);
        value = value.replaceAll("\\*", "%");

        if (temporarolyunavilable) {
            result[0] = SERVICE_UNAVAILABLE_MESSAGE;
        } else if (auth != null && authenticationid != null && !authenticationid.isEmpty() && authenticationid.equals(encript(auth))) {
            try {
                if (getDatasource_data_view() != null) {
                    ArrayList<String> resuklt_l = new ArrayList<String>(10);
                    Connection ncon = null;
                    Statement st_1 = null;
                    ResultSet r_1 = null;
                    try {
                        ncon = getDatasource_data_view().getConnection();
                        if (!ncon.isClosed()) {
                            value = refineentry(value);
                            PreparedStatement p_1 = ncon.prepareStatement(query);
                            p_1.setString(1, value);
                            r_1 = p_1.executeQuery();
                            while (r_1.next()) {
                                resuklt_l.add(r_1.getString(1));
                            }
                            r_1.close();
                            p_1.close();
                        }
                        close(ncon, st_1, r_1);
                    } catch (SQLException e) {
                        String errmsg = e.getMessage();
                        if (errmsg.contains("Subquery returns more than 1 row")) {
                            result[0] = "Error 252: The source query matched more than one row ";
                        } else {
                            result[0] = "Error 250: " + errmsg;
                        }
                    } finally {
                        close(ncon, st_1, r_1);
                    }
                    if (!resuklt_l.isEmpty()) {
                        result = new String[resuklt_l.size()];
                        for (int i = 0; i < result.length; i++) {
                            result[i] = resuklt_l.get(i);

                        }
                        return result;
                    }
                }
            } catch (Exception ex) {
                result[0] = "Error 251: " + ex.getMessage();
            }
        } else {
            result[0] = "Authentication failed. The local authentication key did not match the server lock for " + username;
        }

        return result;
    }

    /**
     * MethodID=ASW6
     *
     */
    @WebMethod(operationName = "getLatestVersion")
    public double getLatestVersion() {
        if (CURRENT_CMD_VERSION < 0) {
            CURRENT_CMD_VERSION = 1;
            if (getDatasource_data_view() != null) {
                try {
                    Connection ncon = getDatasource_data_view().getConnection();
                    Statement st_1 = null;
                    ResultSet r_1 = null;
                    if (!ncon.isClosed()) {
                        try {
                            st_1 = ncon.createStatement();
                            String s_1 = "select PARAM_VALUE from " + get_correct_table_name("config") + " where name='CMD_VERSION'";
                            r_1 = st_1.executeQuery(s_1);
                            while (r_1.next()) {
                                CURRENT_CMD_VERSION = r_1.getDouble("PARAM_VALUE");

                            }
                        } finally {
                            close(ncon, st_1, r_1);
                        }
                    }
                } catch (SQLException e) {
                    System.out.println("Error Asw6:" + e.getMessage());
                }
            }
        }
        return CURRENT_CMD_VERSION;
    }

    /**
     * MethodID=ASW7
     *
     */
    @WebMethod(operationName = "getUpdateLink")
    public String getUpdateLink() {
        if (update_link == null) {
            update_link = "http://localhost:8085/egenv_new.jar";
            if (getDatasource_data_view() != null) {
                try {
                    Connection ncon = getDatasource_data_view().getConnection();
                    Statement st_1 = null;
                    ResultSet r_1 = null;
                    if (!ncon.isClosed()) {
                        String sql = "select param_value, name from " + get_correct_table_name("config") + " where name='CMD_JAR'";
                        ncon = getDatasource_data_view().getConnection();
                        if (!ncon.isClosed()) {
                            st_1 = ncon.createStatement();
                            r_1 = st_1.executeQuery(sql);
                            while (r_1.next()) {
                                update_link = r_1.getString("param_value");
                            }
                            ncon.close();
                        }
                        close(ncon, null, null);
                    }
                } catch (SQLException e) {
                    System.out.println("Error ASW7a:" + e.getMessage());
                }
            }
        }

        return update_link;
    }

    /**
     * MethodID=ASW8
     *
     */
    @WebMethod(operationName = "deleteRecord")
    public List deleteRecord(@WebParam(name = "username") String username, @WebParam(name = "authenticationid") String authenticationid, @WebParam(name = "undos") String undos) {
        String auth = getauthId(username);
        if (auth != null && authenticationid != null && !authenticationid.isEmpty() && authenticationid.equals(encript(auth))) {
            return deleteRecords(undos, username, authenticationid);
        } else {
            ArrayList<String> tmp = new ArrayList<>();
            tmp.add("Error ASW8a: Authentication failed. The local authentication key did not match the server lock for " + username);
            return tmp;
        }

    }

    /**
     * MethodID=ASW9
     *
     */
    @WebMethod(operationName = "getFileDetials4download")
    public java.lang.String[] getFileDetials4download(@WebParam(name = "username") String username, @WebParam(name = "authenticationid") String authenticationid, @WebParam(name = "filelocationlist") java.lang.String[] filelocationlist) {
        String auth = getauthId(username);
        if (auth != null && authenticationid != null && !authenticationid.isEmpty() && authenticationid.equals(encript(auth))) {
            String[] file_details_a = listFromTable(username, authenticationid, "files", "id", "%", "concat(name, '|',checksum,'|',(select name from report where report.id in (select report_id from files2report where files2report.files_id=files.id)), '|',(select name from report_batch where report_batch.id in (select report_batch_id from report where report.id in (select id from report where report.id in (select report_id from files2report where files2report.files_id=files.id)) )))", "like");
            for (int i = 0; i < filelocationlist.length; i++) {
                for (int j = 0; j < file_details_a.length; j++) {
                    String c_file = file_details_a[j];
                    if (c_file.startsWith(filelocationlist[i])) {
                        filelocationlist[i] = c_file;
                    }
                }
            }
            return filelocationlist;
        } else {
            String[] tmp = new String[1];
            tmp[0] = "Error ASW9a: Authentication failed. The local authentication key did not match the server lock for " + username;
            return tmp;
        }
    }

    /**
     * MethodID=ASW10
     *
     */
    @WebMethod(operationName = "addRelationships")
    public String addRelationships(@WebParam(name = "username") String username, @WebParam(name = "authenticationid") String authenticationid,
            @WebParam(name = "relationships") java.lang.String[] relationships_a, @WebParam(name = "addparent") boolean addparent, @WebParam(name = "type") String type) {
        String result = "Attemting to add " + relationships_a.length + " relationships\n";
        String auth = getauthId(username);
        ArrayList<String> result_msg_l = new ArrayList<>();
        ArrayList<Integer> ok_l = new ArrayList<>();
        ok_l.add(1);
        ok_l.add(2);
        ok_l.add(3);
        ok_l.add(4);
        ok_l.add(11);
        ok_l.add(12);
        ok_l.add(16);
        if (id2result_map == null) {
            id2result_map = new HashMap();
            id2result_map.put(1, "The relationship already exists");
            id2result_map.put(2, "Existing records used to create a new relationship");
            id2result_map.put(3, "New source record created and relationship established");
            id2result_map.put(4, "Attmpting to create new source failed, A relationship was created anyway");
            id2result_map.put(5, "Error: Source found but failed to create relationship ");
            id2result_map.put(6, "Error: New source created but failed to create relationships");
            id2result_map.put(7, "Erroe: creating new source failed and creating relationship failed");
            id2result_map.put(8, "The source and the target were the same, not creating relationship ");
            id2result_map.put(9, "Error: Either the source or the target could not be located in the database");
            id2result_map.put(10, "\nError more than one possible target. Select a different name and try again");
            id2result_map.put(11, "Error more than one possible source. Not processing");
            id2result_map.put(12, "Error: failed to locate source");
            id2result_map.put(13, "Error: technical error, lost connection with the database");
            id2result_map.put(14, "Error: SQL error");
            id2result_map.put(15, "Error: Rearrangement faild, transfer table was null");
            id2result_map.put(16, "A new relationship created earlier was used as the source");
            id2result_map.put(17, "Relationship already exists, so not created");
        }


        if (auth != null && authenticationid != null && !authenticationid.isEmpty() && authenticationid.equals(encript(auth))) {
            if (hierarchy_tables_l == null) {
                List hier_tables_l = advancedQuaryHandler(authenticationid, username, "tablename2feature.table_name=hierarchy",
                        "tablename2feature.distinct table_name", false, false, false, false, false, -1);
                if (hier_tables_l != null) {
                    hier_tables_l.remove(0);
                    hierarchy_tables_l = new ArrayList<>(hier_tables_l);
                }
            }
//            System.out.println("6778 "+type);
            String[] type_s_a = type.split("\\|\\|");
            String source_table = null;
            String relationship_using = null;
            String tarnsfer_col_nm = null;
            String tarnsfer_tbl = null;
            String connecting_table = null;
            ArrayList<String> etc_param_map_kl = new ArrayList<>();
//            System.out.println("6764 type=" + type + " " + Arrays.deepToString(type_s_a));
            if (type_s_a.length > 1) {
                source_table = type_s_a[0];
                relationship_using = type_s_a[1].substring(type_s_a[1].lastIndexOf(".") + 1);//.split("\\.")[type_s_a[1].split("\\.").length - 1];
                if (type_s_a.length > 2) {
                    tarnsfer_col_nm = type_s_a[2];
                    if (tarnsfer_col_nm != null) {
                        String[] tmp_a = tarnsfer_col_nm.split("\\.");
                        if (tmp_a.length > 1) {
                            tarnsfer_col_nm = tmp_a[tmp_a.length - 1];
                            tarnsfer_tbl = tmp_a[tmp_a.length - 2];
                            tarnsfer_tbl = get_correct_table_name(tarnsfer_tbl);
                            connecting_table = tarnsfer_tbl.substring(tarnsfer_tbl.lastIndexOf(".") + 1) + "2" + source_table.substring(source_table.lastIndexOf(".") + 1);
                            connecting_table = get_correct_table_name(connecting_table);
                            if (connecting_table == null) {
                                connecting_table = source_table.substring(source_table.lastIndexOf(".") + 1) + "2" + tarnsfer_tbl.substring(tarnsfer_tbl.lastIndexOf(".") + 1);
                                connecting_table = get_correct_table_name(connecting_table);
                            }
//                            System.out.println("6802 "+connecting_table);
                        }
                    }
//                    System.out.println("6806 "+Arrays.deepToString(type_s_a));
                    for (int i = 3; i < type_s_a.length; i++) {
                        etc_param_map_kl.add(type_s_a[i].substring(type_s_a[i].lastIndexOf(".") + 1));
                    }
                }
            } else {
                List key_l = getUniqueList(type);
                if (result != null) {
                    for (int i = 0; (i < key_l.size() && i >= 0); i++) {
                        if (key_l.get(i).toString().toUpperCase().endsWith("_ID")) {
                            key_l.remove(i);
                            i--;
                        }
                    }
                    if (result.isEmpty()) {
                        key_l.add("name");
                    }
                    relationship_using = type + "." + key_l.get(0);
                    source_table = type;
                }
            }

            String hierarchy_table = source_table + HIERARCHY_FLAG;
            HashMap<String, String[]> constraint_map = get_key_contraints(hierarchy_table, source_table, null, dataSource_data);
            ArrayList<String> key_l = new ArrayList<>(constraint_map.keySet());

            String parent_column_nm = null;
            String child_column_nm = null;
            Set key_nms = constraint_map.keySet();
            if (key_nms != null) {
                for (int i = 0; i < key_l.size(); i++) {
                    if (key_l.get(i).toUpperCase().contains("PARENT")) {
                        parent_column_nm = key_l.get(i);
                    } else if (key_l.get(i).toUpperCase().contains("CHILD")) {
                        child_column_nm = key_l.get(i);
                    }
                }
            }
//            System.out.println("5827 " + hierarchy_tables_l + " " + hierarchy_table + " parent_column_nm=" + parent_column_nm + " child_column_nm=" + child_column_nm+"\nsource_table="+source_table);
            if (source_table == null || !containsIgnoreCase(hierarchy_tables_l, hierarchy_table)) {
                result = "The raltionship type was not undestood. Allowed types=" + hierarchy_tables_l;
            } else {
//                System.out.println("6825");
                ArrayList<String> curr_hierarchy_l = new ArrayList<>();
                Connection n_con = null;
                Statement n_st = null;
                try {
                    try {
                        n_con = getDatasource_data_update().getConnection();
                        n_st = n_con.createStatement();
                    } catch (Exception ex) {
                        result = result + " Error 201" + ex.getMessage() + "\n";
                    }
                    boolean allok = true;
                    ArrayList<String> trasfer_sql_l = new ArrayList<>();
                    HashMap<String, ArrayList<String[]>> created_s_map = new HashMap<>();
                    if (n_con != null && !n_con.isClosed() && n_st != null) {
                        hierarchy_table = get_correct_table_name(hierarchy_table);
                        String pre_sql = "select " + parent_column_nm + "," + child_column_nm + " from " + hierarchy_table;
//                        System.out.println("6863 "+pre_sql);
                        Statement st_pre = n_con.createStatement();
                        ResultSet r_pre = st_pre.executeQuery(pre_sql);
                        while (r_pre.next()) {
                            int parent = r_pre.getInt(1);
                            int child = r_pre.getInt(2);
                            curr_hierarchy_l.add(parent + "=" + child);
                        }
//                        System.out.println("6847 " + curr_hierarchy_l+" "+Arrays.deepToString(relationships_a)+" allok="+allok);
                        r_pre.close();
                        st_pre.close();
                        for (int i = 0; (i < relationships_a.length && allok); i++) {
                            String[] c_relationship_a = relationships_a[i].split("\\|\\|");
//                            System.out.println("6875 "+Arrays.deepToString( c_relationship_a ));
                            if (c_relationship_a.length == 2) {
                                String s = c_relationship_a[0];
                                String t = c_relationship_a[1];
//                                System.out.println("6857 " + Arrays.deepToString(c_relationship_a) + "\t source_table=" + source_table + "\trelationship_using=" + relationship_using + "|");
                                int c_result = addRelationships(n_st, source_table, relationship_using, hierarchy_table, parent_column_nm,
                                        child_column_nm, tarnsfer_col_nm, tarnsfer_tbl, null, connecting_table, addparent, s, t, curr_hierarchy_l,
                                        trasfer_sql_l, result_msg_l, created_s_map, null);

                                if (ok_l.contains(c_result)) {
                                } else {
                                    allok = false;
                                }
//                                if (c_result.equalsIgnoreCase("OK") || c_result.startsWith("Relationship already found between") || c_result.startsWith("source not found")) {
//                                } else {
//                                    allok = false;
//                                }

                                result = result + " Using=" + relationship_using + "\t" + "->" + t + "  ... " + id2result_map.get(c_result) + "\n";
                            } else if (c_relationship_a.length >= 3) {
                                String s = c_relationship_a[0];
                                String t = c_relationship_a[1];
                                String[] new_entry_details_a = null;
//                                String c_result = addRelationships(st_1, source, target, type_m, set_using, addparent, username, authenticationid);

                                if (c_relationship_a[2] != null && !c_relationship_a[2].isEmpty()) {
                                    new_entry_details_a = c_relationship_a[2].split(",");
                                }
//                                System.out.println("6905 "+Arrays.deepToString(c_relationship_a)+"  "+etc_param_map_kl);
                                HashMap<String, String> etc_param_map = new HashMap<>();
                                for (int j = 3; j < c_relationship_a.length; j++) {
                                    String pram_val = c_relationship_a[j];
                                    etc_param_map.put(etc_param_map_kl.get(j - 3), pram_val);
                                }
                                int c_result = addRelationships(n_st, source_table, relationship_using, hierarchy_table, parent_column_nm,
                                        child_column_nm, tarnsfer_col_nm, tarnsfer_tbl, new_entry_details_a, connecting_table, addparent, s, t, curr_hierarchy_l,
                                        trasfer_sql_l, result_msg_l, created_s_map, etc_param_map);


//                                if (c_result.equalsIgnoreCase("OK") || c_result.startsWith("Relationship already found between")
//                                        || c_result.startsWith("source not found") || c_result.startsWith("Targets not found")) {
//                                } else {
//                                    allok = false;
//                                }
                                if (ok_l.contains(c_result)) {
                                } else {
                                    allok = false;
                                }

                                result = result + " Using=" + relationship_using + "\t" + s + "->" + t + "  ... " + id2result_map.get(c_result) + "\n";
                            }
                        }
                    } else {
                        result = result + "Error :The database connection was lost\n";
                    }
//                    System.out.println("5760 Emtying trasfer_sql_l");
//                    trasfer_sql_l.clear();
//                    if (allok && ncon != null && !ncon.isClosed()) {
//                        Statement st = ncon.createStatement();
                    if (trasfer_sql_l.size() > 0) {
                        InsertLater iL = new InsertLater(getDatasource_data_update(), trasfer_sql_l, (getQueryTimeOut() + 500), username);
                        (new Thread(iL)).start();
                        int limit = 5;
                        boolean done = false;
                        while (!done && limit > 0) {
                            if (iL.isPostpondable(60)) {
                                done = true;
                            } else {
                                try {
                                    Thread.sleep(1000);
                                } catch (InterruptedException ex) {
                                }
                            }
                            limit--;
                        }

                        if (done) {
                            int cr = 0;
                            int[] result_a = iL.getResults();
                            if (result_a != null) {
                                for (int i = 0; (cr >= 0 && i < result_a.length); i++) {
                                    cr = result_a[i];
                                }
                            } else {
                            }

                            if (cr >= 0) {
//                                ncon.commit();
                                result = result + "All updates committed to the database";
                            } else {
                                result = result + " Record rearangement failed";
//                                if (save1 != null) {
//                                    ncon.rollback(save1);
                                result = result + "\n No changes were made to the database\n";
//                                }
                            }
                        } else {
//                            ncon.commit();
                            iL.sendMail();
                            result = result + "Some updates  were committed to the database.\n An email will be sent to " + username + " when the rest of the updates are done.";
                        }
                    } else {
//                        ncon.commit();
                        result = result + "Updates were committed to the database. Nothing was rearranged as this information was abscent";
                    }
//                        for (int i = 0; (cr >= 0 && i < trasfer_sql_l.size()); i++) {
//                            int len = 150;
//                            if (trasfer_sql_l.get(i).length() < len) {
//                                len = trasfer_sql_l.get(i).length();
//                            }
//                 
//                            cr = st.executeUpdate(trasfer_sql_l.get(i));
//                        }
//                        if (cr >= 0) {
//                            ncon.commit();
//                            result = result + "All updates committed to the database";
//                        } else {
//                            result = result + " Record rearangement failed";
//                            if (save1 != null) {
//                                ncon.rollback(save1);
//                                result = result + "\n No changes were made to the database\n";
//                            }
//                        }

//                    } else {
//                        if (save1 != null) {
//                            ncon.rollback(save1);
//                            result = result + "\n No changes were made to the database\n";
//                        }
//                    }
                    curr_hierarchy_l.clear();
                    HashMap<String, String> config_map = new HashMap<>();
                    config_map.put("MEMORIZING_REQUESTED", "1");
                    setConfig(n_con, config_map);
                    close(n_con, n_st, null);
                } catch (SQLException ex) {
                    result = result + "Error ASW10a:" + ex.getMessage() + "\n";

                } finally {
                    curr_hierarchy_l.clear();
                    close(n_con, n_st, null);
//                    close(null, st_2, null);
//                    close(ncon, st_1, r_1);
                }
            }

        } else {
            result = "Authentication failed. The local authentication key did not match the server lock for " + username;
        }
        //preserve the results
//        String filename = getDocRoot() + username + "_" + Timing.getDateTimeForFileName();
//        writeResultsToFile(result, filename);

        result = result_msg_l.toString() + "\n" + result;
        return result;
    }

    /**
     * MethodID=ASW11
     *
     */
    @WebMethod(exclude = true)
    private int addRelationships(Statement n_st, String source_table, String source_column, String hierarchy_table,
            String parent_column_nm, String child_column_nm, String transfer_col, String tarnsfer_tbl, String[] new_entry_details_a,
            String connecting_table, boolean addparent, String s, String t, ArrayList<String> curr_hierarchy_l,
            ArrayList<String> trasfer_sql_l, ArrayList<String> result_msg_l, HashMap<String, ArrayList<String[]>> created_s_map,
            HashMap<String, String> etc_param_map) {
        int result = 0;
        String last_query = null;
//        System.out.println("7018 tarnsfer_tbl=" + tarnsfer_tbl+" source_column="+source_column);
        try {
            if (n_st != null && !n_st.isClosed()) {
                int new_created = 0;
                s = s.replaceAll("'", " ");
                t = t.replaceAll("'", " ");
                source_table = get_correct_table_name(source_table);
                hierarchy_table = get_correct_table_name(hierarchy_table);
                ArrayList<String[]> source_details;

                if (created_s_map.containsKey(s)) {
                    source_details = created_s_map.get(s);
                } else {
                    source_details = getHierarchyDetials(n_st, source_table, source_column, s, "=");
                }
//                System.out.println("7443 "+source_table+"\t"+source_column+"\t"+ t);
                ArrayList<String[]> target_details = getHierarchyDetials(n_st, source_table, source_column, t, "=");
                if (!source_details.isEmpty()) {
                    if (source_details.size() == 1 && target_details.isEmpty()) {
                        String[] c_val_a = source_details.get(0);
//                        System.out.println("7063 "+Arrays.deepToString(c_val_a )+" "+etc_param_map);
                        String col = "";
                        String val = "";
                        for (int i = 1; i < c_val_a.length; i++) {
                            col = col + "," + c_val_a[i].split("=")[0];
                            val = val + "," + c_val_a[i].split("=")[1];
                        }
                        if (etc_param_map != null) {
                            ArrayList<String> k_l = new ArrayList<>(etc_param_map.keySet());
                            for (int i = 0; i < k_l.size(); i++) {
                                col = col + "," + k_l.get(i);
                                val = val + ",'" + etc_param_map.get(k_l.get(i)) + "'";
                            }
                        }
//                        System.out.println("7076 col=" + col);
//                        Statement st_2 = ncon_2.createStatement();
                        String sql_inst = "insert into " + source_table + " (" + source_column + col + ") values('" + t + "'" + val + ")";
//                        System.out.println("5847 " + sql_inst);
                        if (n_st.executeUpdate(sql_inst) >= 0) {
                            target_details = getHierarchyDetials(n_st, source_table, source_column, t, "=");
                            created_s_map.put(t, target_details);
                            new_created = 1;
                        } else {
                            new_created = 2;
                        }
//                        if (!st_2.isClosed()) {
//                            st_2.close();
//                        }
                    } else if (!target_details.isEmpty()) {
                        boolean matched = false;
                        for (int i = 0; (i < target_details.size() && !target_details.isEmpty() && !matched); i++) {
                            String[] target_a = target_details.get(i);
                            for (int j = 0; (j < source_details.size() && !matched); j++) {
                                String[] source_a = source_details.get(j);
                                if (target_a.length == source_a.length) {
                                    matched = true;
                                    for (int k = 1; k < target_a.length; k++) {
                                        if (!target_a[k].equals(source_a[k])) {
                                            matched = false;
                                        }
                                    }
                                }
                            }
                            if (!matched) {
                                target_details.remove(i);
                                i--;
                            }
                        }
                    }
                    if (source_details.size() == 1 && target_details.isEmpty()) {
                        String[] c_val_a = source_details.get(0);
                        String col = "";
                        String val = "";
                        for (int i = 1; i < c_val_a.length; i++) {
                            col = col + "," + c_val_a[i].split("=")[0];
                            val = val + "," + c_val_a[i].split("=")[1];
                        }
                        if (etc_param_map != null) {
                            ArrayList<String> k_l = new ArrayList<>(etc_param_map.keySet());
                            for (int i = 0; i < k_l.size(); i++) {
                                col = col + "," + k_l.get(i);
                                val = val + ",'" + etc_param_map.get(k_l.get(i)) + "'";
                            }
                        }
//                        Statement st_2 = ncon_2.createStatement();
                        String sql_inst = "insert into " + source_table + " (" + source_column + col + ") values('" + t + "'" + val + ")";
                        last_query = sql_inst;
                        if (n_st.executeUpdate(sql_inst) >= 0) {
                            target_details = getHierarchyDetials(n_st, source_table, source_column, t, "=");
                            created_s_map.put(t, target_details);
                            new_created = 1;
                        } else {
                            new_created = 2;
                        }
//                        if (!st_2.isClosed()) {
//                            st_2.close();
//                        }
                    }
                    if (!target_details.isEmpty()) {
                        boolean matched = false;
                        for (int i = 0; (i < target_details.size() && !target_details.isEmpty() && !matched); i++) {
                            String[] target_a = target_details.get(i);
                            for (int j = 0; (j < source_details.size() && !matched); j++) {
                                String[] source_a = source_details.get(j);
                                if (target_a.length == source_a.length) {
                                    matched = true;
                                    for (int k = 1; k < target_a.length; k++) {
                                        if (!target_a[k].equals(source_a[k])) {
                                            matched = false;
                                        }
                                    }
                                }
                            }
                            if (!matched) {
                                target_details.remove(i);
                                i--;
                            }
                        }
                    }
                    if (source_details.size() == 1) {
                        if (target_details.size() == 1) {
//                            if (tarnsfer_tbl != null) {
                            String source_id = source_details.get(0)[0];
                            String target_id = target_details.get(0)[0];
//                            System.out.println("7140 " + source_details.get(0)[0] + "=>" + source_id + "\t" + target_details.get(0)[0] + "=>" + target_id);
                            if (source_id != null && target_id != null) {
                                if (curr_hierarchy_l.contains(source_id + "=" + target_id) || curr_hierarchy_l.contains(target_id + "=" + source_id)) {
                                    result = 1;
                                    if (tarnsfer_tbl == null) {
//                                        result = 15;
                                    } else {
                                        for (int i = 0; i < new_entry_details_a.length; i++) {
                                            String c_prefix = new_entry_details_a[i];
                                            String trans_target_col = tarnsfer_tbl.split("\\.")[tarnsfer_tbl.split("\\.").length - 1] + "_ID";//+ transfer_col;
                                            String relater_col = source_table.split("\\.")[source_table.split("\\.").length - 1] + "_ID";
                                            String sql_existing = "select " + trans_target_col + " from " + connecting_table + " where  " + relater_col + "=" + target_id;//+ " and " + trans_target_col + " in (select id from " + tarnsfer_tbl + " where " + tarnsfer_tbl + "." + transfer_col + " like '" + c_prefix + "%'" + ") ";
                                            String sql_sel = "select id," + trans_target_col + " from " + connecting_table + " where  " + relater_col + "=" + source_id + " and " + trans_target_col + " in (select id from " + tarnsfer_tbl + " where " + tarnsfer_tbl + "." + transfer_col + " like '" + c_prefix + "%'" + ") ";
                                            String sql_update = "UPDATE " + connecting_table + " set " + relater_col + "=" + target_id + " where id in (<ID_L>)";
                                            trasfer_sql_l.add(sql_existing + "||" + sql_sel + "||" + sql_update);
                                            result_msg_l.add("\nTransfering " + c_prefix + "% from " + s + " to " + t);
                                        }

                                    }

                                } else {
                                    if (!target_id.equals(source_id)) {
                                        String sql = null;
                                        String sel_sql = null;
                                        if (addparent) {
                                            sql = "insert into " + hierarchy_table + " (" + child_column_nm + "," + parent_column_nm + ") values(" + source_id + "," + target_id + ")";
                                            sel_sql = "select 1 from " + hierarchy_table + " where " + child_column_nm + "=" + source_id + " AND " + parent_column_nm + "=" + target_id;
                                        } else {
                                            sql = "insert into " + hierarchy_table + " (" + parent_column_nm + "," + child_column_nm + ") values(" + source_id + "," + target_id + ")";
                                            sel_sql = "select 1 from " + hierarchy_table + "  where " + parent_column_nm + "=" + source_id + " AND " + child_column_nm + "=" + target_id;
                                        }
//                                            System.out.println("5978 " + sql);
                                        if (sel_sql != null) {
                                            ResultSet sel_r = n_st.executeQuery(sel_sql);
                                            if (sel_r.next()) {
                                                sql = null;
                                                result = 17;
                                            }
                                            sel_r.close();
                                        }

                                        if (sql != null) {
                                            last_query = sql;
                                            if (n_st.executeUpdate(sql) >= 0) {
                                                result = 2 + new_created;  //2,3,4 
                                                if (tarnsfer_tbl == null) {
//                                                    result = 15;
                                                } else {
                                                    if (new_entry_details_a != null && connecting_table != null) {
                                                        for (int i = 0; i < new_entry_details_a.length; i++) {
                                                            String c_prefix = new_entry_details_a[i];
                                                            String trans_target_col = tarnsfer_tbl.split("\\.")[tarnsfer_tbl.split("\\.").length - 1] + "_ID";//+ transfer_col;
                                                            String relater_col = source_table.split("\\.")[source_table.split("\\.").length - 1] + "_ID";
                                                            String sql_existing = "select " + trans_target_col + " from " + connecting_table + " where  " + relater_col + "=" + target_id;// + " and " + trans_target_col + " in (select id from " + tarnsfer_tbl + " where " + tarnsfer_tbl + "." + transfer_col + " like '" + c_prefix + "%'" + ") ";
                                                            String sql_sel = "select id," + trans_target_col + " from " + connecting_table + " where  " + relater_col + "=" + source_id + " and " + trans_target_col + " in (select id from " + tarnsfer_tbl + " where " + tarnsfer_tbl + "." + transfer_col + " like '" + c_prefix + "%'" + ") ";
                                                            String sql_update = "UPDATE " + connecting_table + " set " + relater_col + "=" + target_id + " where id in (<ID_L>)";
                                                            trasfer_sql_l.add(sql_existing + "||" + sql_sel + "||" + sql_update);
                                                            result_msg_l.add("\nTransfering " + c_prefix + "% from " + s + " to " + t);
                                                        }
                                                    }
                                                }
                                            } else {
                                                result = 5 + new_created;//5,6,7
                                            }

                                        }
                                    } else {
                                        result = 8;
                                    }
                                }
                            } else {
                                result = 9;
                            }
//                            } else {
//                                result = 15;
//                            }
                        } else {
                            result = 10;
                        }

                    } else {
                        result = 11;
                    }

                } else {
                    result = 12;
                }
            } else {
                result = 13;
            }
        } catch (SQLException ex) {
            result = 14;
            System.out.println("Error ASW11c: " + ex.getMessage() + " " + last_query);
        }
        return result;

    }

    /**
     * MethodID=ASW12
     *
     */
    @WebMethod(operationName = "check_authentication")
    public boolean check_authentication(@WebParam(name = "username") String username, @WebParam(name = "authenticationid") String authenticationid) {
        String auth = getauthId(username);
        if (auth != null && authenticationid != null && !authenticationid.isEmpty() && authenticationid.equals(encript(auth))) {
            return true;
        }
        return false;
    }

    /**
     * MethodID=ASW13
     *
     */
    @WebMethod(operationName = "resetauthnticationkey")
    public String resetauthnticationkey(@WebParam(name = "username") String username, @WebParam(name = "password") String password) {
        String result = "";
        int auth_result = isAuthentic(username, password);
        if (auth_result == 1) {
            result = result + "username=" + username + " auth_result=" + auth_result + " ";
            String auth = getauthId(username);
            if (auth != null) {
                if (auth_map != null) {
                    auth_map.remove(username);
                    ArrayList<String> key_l = new ArrayList(auth_map.keySet());
                    try {
                        FileWriter authfile = new FileWriter(AUTH_LIST_FILE_NM, false);
                        for (int i = 0; i < key_l.size(); i++) {
                            try {
                                authfile.append(key_l.get(i) + "=" + auth_map.get(key_l.get(i)) + "\n");
                            } catch (IOException ex) {
                                result = result + "-7 " + ex.getMessage();
                            }
                        }
                        authfile.close();
                        result = "1";
                    } catch (IOException ex) {
                        result = result + "-8 " + ex.getMessage();
                    }

                } else {
                    result = result + "Error: getauthId returned null";
                }
            } else {
                result = result + "-1";
            }
        } else {
            result = result + "Error :" + auth_result;
        }
        return result;
    }

    /**
     * MethodID=ASW14
     *
     */
    @WebMethod(operationName = "getfileTypeDescription")
    public java.util.List getfileTypeDescription(@WebParam(name = "username") String username, @WebParam(name = "authenticationid") String authenticationid, @WebParam(name = "extentionmap") java.util.List<String> extention_list) {
        String auth = getauthId(username);

        HashMap<String, String> extentionmap = new HashMap<String, String>();
        if (auth != null && authenticationid != null && !authenticationid.isEmpty() && authenticationid.equals(encript(auth))) {
//            for (int i = 0; i < extention_list.size(); i++) {
//                extentionmap.put(extention_list.get(i).toString(), "extension=" + extention_list.get(i) + ",c_description=No Description available");
//            }
            String sql = "select name, fileextension,concat('extension=',fileextension,',description=',(ifnull((description),('No Description available'))) ) as filetype from " + get_correct_table_name("filetype");
            Statement st_1 = null;;
            Connection ncon = null;;
            ResultSet r_1 = null;
            try {
                ncon = getDatasource_data_view().getConnection();
                st_1 = ncon.createStatement();
                r_1 = st_1.executeQuery(sql);
                while (r_1.next()) {
                    String name = r_1.getString(1);
                    String extention = r_1.getString(2);
                    String type = r_1.getString(3);
                    if (extention_list.contains(name) || extention_list.contains(extention)) {
                        extentionmap.put(name, type);
                    }
//                    else if (extention_list.contains(extention)) {
//                        extentionmap.put(extention, type);
//                    }
                }
                close(ncon, st_1, r_1);
            } catch (Exception ex) {
                extentionmap.clear();
                extentionmap.put("Error", "220 " + ex.getMessage());
            } finally {
                close(ncon, st_1, r_1);
            }
        } else {
            extentionmap.clear();
            extentionmap.put("Error", "221, authentication failed ");
        }
        ArrayList<String> out_l = new ArrayList<String>(extentionmap.keySet());
        for (int i = 0; i < out_l.size(); i++) {
            String c_key = out_l.remove(i);
            out_l.add(0, c_key + "==" + extentionmap.get(c_key));
        }
        return out_l;
    }

    /**
     * MethodID=ASW16
     *
     */
    @WebMethod(operationName = "submitError")
    public int submitError(@WebParam(name = "error") String error, @WebParam(name = "controler") int controler) {
        int result = -1;
        try {
            String recipient_mail = getAdminEmail();
            String subject = "Error report";
            String recipient_name = "Admin";
            if (error != null && !error.isEmpty() && getDatasource_data_update() != null) {
                Connection ncon = null;
                Statement st_1 = null;
                try {
                    ncon = getDatasource_data_update().getConnection();
                    if (ncon != null) {
                        String sql = "insert into errors(error) values('" + refineentry(error) + "'); ";
                        st_1 = ncon.createStatement();
                        result = st_1.executeUpdate(sql);
                    }
                    close(ncon, st_1, null);
                } catch (SQLException ex) {
                    ex.printStackTrace();
                    result = -2;
                } finally {
                    close(ncon, st_1, null);
                }
            } else {
                result = -7;
            }
            if (controler == 1) {

                if (getDatasource_email() != null && recipient_mail != null && recipient_mail.matches("[A-z0-9_\\-\\.]+@[A-z0-9_\\-]+[\\.]{1}[A-z0-9_\\-\\.]+")) {
                    try {
                        Message msg = new MimeMessage(getDatasource_email());
                        msg.setSubject(subject);
                        msg.setRecipient(Message.RecipientType.TO, new InternetAddress(recipient_mail, recipient_name));
                        msg.setFrom(new InternetAddress(getAdminEmail(), "eGenVar admin"));
                        BodyPart messageBodyPart = new MimeBodyPart();
                        messageBodyPart.setText(error);
                        Multipart multipart = new MimeMultipart();
                        multipart.addBodyPart(messageBodyPart);
                        msg.setContent(multipart);
                        Transport.send(msg);
                    } catch (UnsupportedEncodingException ex) {
                        result = -4;
                    } catch (MessagingException ex) {
                        result = -5;
                    }

                } else {
                    System.out.println("Error ASW16: Mailer =" + recipient_mail);
                    result = -6;
                }
            }

        } catch (Exception ex) {
            System.out.println("Error ASW16: Mailer");
            result = -7;
        }
        return result;
    }

    /**
     * MethodID=ASW17
     *
     */
    @WebMethod(operationName = "getParametersForPrepare")
    public List getParametersForPrepare(@WebParam(name = "tablenm") String tablenm, @WebParam(name = "username") String username, @WebParam(name = "authenticationid") String authenticationid) {
        String auth = getauthId(username);
        if (auth != null && authenticationid != null && !authenticationid.isEmpty() && authenticationid.equals(encript(auth))) {
            if (tablenm == null || tablenm.isEmpty()) {
                msges = "Error AS2a: table name null or empty ";
            } else {
                tablenm = get_correct_table_name(tablenm);
                if (tablenm == null) {
                    ArrayList<String> error_l = new ArrayList<>(1);
                    error_l.add("Error AS2c: Table not found " + tablenm);
                } else {
                    return getParamAndDependancies(tablenm, username);
                }

            }


        } else {
            msges = "Error AS2b: Authentication failed ";
        }
        return null;
    }

    /**
     * MethodID=ASW18
     *
     */
    @WebMethod(operationName = "setUsingTemplate")
    public String[] setUsingTemplate(@WebParam(name = "username") String username, @WebParam(name = "authenticationid") String authenticationid,
            @WebParam(name = "extentionmap") java.util.List<String> parameter_list) {
        String auth = getauthId(username);
        if (auth != null && authenticationid != null && !authenticationid.isEmpty() && authenticationid.equals(encript(auth))) {
            return pupolateFromTemplate(parameter_list);
        } else {
            String[] tmp = new String[1];
            tmp[0] = "Error 18a: Authentication failed";
            return tmp;
        }
    }

    /**
     * MethodID=ASW10
     *
     */
    @WebMethod(operationName = "relate")
    public String[] relate(@WebParam(name = "authenticationid") String authenticationid, @WebParam(name = "username") String username,
            @WebParam(name = "parameter_list") java.util.List<String> parameter_list) {
//        System.out.println("7757 "+Arrays.deepToString(relationships_a));
//return null;
//        String[] relationships_a = new String[1];
        ArrayList<String> result_l = new ArrayList<>();
        if (parameter_list != null) {
            for (int i = 0; i < parameter_list.size(); i++) {
                if (parameter_list.get(i) != null) {
                    String[] c_val_a = parameter_list.get(i).split("->", 2);
                    if (c_val_a.length == 2) {
                        String[] sorce_a = c_val_a[0].split("=", 2);
                        String[] target_a = c_val_a[1].split("=", 2);
                        if (sorce_a.length == 2 && target_a.length == 2) {
//                            System.out.println("7770 " + c_val_a[0] + "\t" + c_val_a[1] + "  " + sorce_a[0].toUpperCase().equals(target_a[0].toUpperCase()));
                            if (sorce_a[1].toUpperCase().equals(target_a[1].toUpperCase())) {
                                System.out.println("7770 " + c_val_a[0] + "\t" + c_val_a[1]);
                                result_l.add(addRelationships(username, authenticationid, new String[]{sorce_a[0] + "||" + target_a[0]}, false, sorce_a[1].split("\\.")[0] + "||" + sorce_a[1]));
                            } else {
                                String[] c_result = add(authenticationid, username, Arrays.asList(c_val_a[0], c_val_a[1]));
                                for (int j = 0; j < c_result.length; j++) {
                                    result_l.add(c_result[j]);
                                }

                            }
                        }

                    }
                }

            }
        }

//        return addRelationships(username, authenticationid, relationships_a, false, type);
        return result_l.toArray(new String[result_l.size()]);

    }

    /**
     * MethodID=ASW18.1
     *
     */
    @WebMethod(operationName = "add")
    public String[] add(@WebParam(name = "authenticationid") String authenticationid, @WebParam(name = "username") String username,
            @WebParam(name = "parameter_list") java.util.List<String> parameter_list) {
//        System.out.println("7769 " + parameter_list);
        String auth = getauthId(username);
//        System.out.println("7800 auth="+auth+"\tauthenticationid="+authenticationid+"\tusername="+username);
        if (auth != null && authenticationid != null && !authenticationid.isEmpty() && authenticationid.equals(encript(auth))) {
            return pupolateFromTemplate(parameter_list);
        } else {
            String[] tmp = new String[1];
            tmp[0] = "Error 18.1a: Authentication failed";
            return tmp;
        }
//return null;
    }

    /**
     * MethodID=ASW19
     *
     */
    @WebMethod(operationName = "getDoesitExist")
    public boolean getDoesitExist(@WebParam(name = "query") String query, @WebParam(name = "username") String username, @WebParam(name = "authenticationid") String authenticationid) {
        boolean result = false;
        String auth = getauthId(username);
        if (auth != null && authenticationid != null && !authenticationid.isEmpty() && authenticationid.equals(encript(auth))) {
            Connection ncon = null;
            Statement stm_1 = null;
            try {
                ncon = getDatasource_data_update().getConnection();
                stm_1 = ncon.createStatement();
                if (getOneForQuery(query, stm_1) != null) {
                    result = true;
                }
                close(ncon, stm_1, null);
            } catch (Exception ex) {
                msges = "Error ASW19a: creating connections " + ex.getMessage();
            } finally {
                close(ncon, stm_1, null);
            }
        } else {
            msges = "Error ASW5b: Authentication failed";
        }

        return result;
    }

    /**
     * MethodID=ASW20
     *
     */
    @WebMethod(operationName = "getTableRelationship")
    public List getTableRelationship(@WebParam(name = "authenticationid") String authenticationid, @WebParam(name = "username") String username, @WebParam(name = "source") String source, @WebParam(name = "target") String target) {
        String auth = getauthId(username);
        if (auth != null && authenticationid != null && !authenticationid.isEmpty() && authenticationid.equals(encript(auth))) {
            ArrayList<ArrayList<String>> result = getpaths(source, target);
            if (result == null || result.isEmpty()) {
                return new ArrayList<>(1);
            } else {
                return result.get(0);
            }
        } else {
            msges = "Error 233: Authentication failed";
        }
        return null;
    }

    /**
     * MethodID=ASW21.1
     *
     */
    @WebMethod(operationName = "search")
    public List search(@WebParam(name = "authenticationid") String authenticationid,
            @WebParam(name = "username") String username,
            @WebParam(name = "source") String source,
            @WebParam(name = "target") String target,
            @WebParam(name = "onlyexactmatch") boolean onlyexactmatch,
            @WebParam(name = "type") int type) {
        return advancedQuaryHandler(authenticationid, username, source, target, onlyexactmatch,
                false, false, false, false, type);
    }

    /**
     * MethodID=ASW21
     *
     */
    @WebMethod(operationName = "advancedQuaryHandler")
    public List advancedQuaryHandler(@WebParam(name = "authenticationid") String authenticationid, @WebParam(name = "username") String username,
            @WebParam(name = "source") String source, @WebParam(name = "target") String target, @WebParam(name = "onlyexactmatch") boolean onlyexactmatch,
            @WebParam(name = "includequery") boolean includequery, @WebParam(name = "addcolumnnames") boolean addcolumnnames,
            @WebParam(name = "exapandforeignKeys") boolean exapandforeignKeys, @WebParam(name = "checkifexists") boolean checkifexists,
            @WebParam(name = "type") int type) {
        String auth = getauthId(username);
        String sql = null;
//        System.out.println("7584 type="+type);
//        System.out.println("7724 authenticationid=" + authenticationid + " auth=" + auth + "  " + encript(auth));
//        System.out.println("7930 source="+source+" target="+target);
        ArrayList<String> result_l = new ArrayList<>(1);
        if (auth != null && authenticationid != null && !authenticationid.isEmpty() && authenticationid.equals(encript(auth))) {
            boolean childen_before = false;
            boolean childen_during = false;
            boolean childen_at_the_end = false;
            boolean parent_at_the_end = false;
            if (type != 0 && type <= 5) {
                if (type >= 4) {
                    type = 0;
                    parent_at_the_end = true;
                }
                if (type >= 3) {
                    type = type % 4;
                    childen_at_the_end = true;
                }
                if (type >= 2) {
                    type = type % 4;
                    childen_during = true;
                }
                if (type == 1) {
                    childen_before = true;
                }
                type = 0;
            }

//            System.out.println("7281 childen_before=" + childen_before + " childen_at_the_end=" + childen_at_the_end + " childen_during=" + childen_during + " parent_at_the_end=" + parent_at_the_end);

            if (source != null) {
                try {
                    boolean minmax = false;
                    if (minmax == false) {
                        String[] tmp_a = source.split("\\.");
                        if ((tmp_a.length > 2) && ((tmp_a[2].equalsIgnoreCase("min")) || (tmp_a[2].equalsIgnoreCase("max")))) {
                            minmax = true;
                        }
                    }
                    ArrayList<ArrayList<String>> sql_L = new ArrayList<>();
                    sql_L.add(new ArrayList<String>(1));
                    if ((source.toUpperCase().toUpperCase().startsWith(CODE_RESULT_FLAG + "=")
                            || source.toUpperCase().startsWith(CODE_RESULT_FLAG + "."))) {
                        Object result_ob = readResultsFromFile(getDocRoot() + "" + source.split("=")[1]);
                        if (result_ob != null) {
                            if (result_ob instanceof ArrayList) {
                                result_l.clear();
                                result_l.addAll((ArrayList<String>) result_ob);
                            } else {
                                result_l.add(0, "Error AWS21l: Retrieving results failed for " + source.split("=")[1]);
                            }
                        } else {
                            result_l.add(0, "Error AWS21k: Retrieving results failed for " + source.split("=")[1]);
                        }
                    } else {
                        if (target == null || target.toUpperCase().equals(CODE_SEARCH_FLAG)) {
                            target = "*";
                        }
                        boolean tagSearch = false;
//                        System.out.println("7205 "+source.toUpperCase()+"  "+source.toUpperCase().matches("^" + TAG_FLAG + "[S]*[=|!|<|>]+.*"));
                        Pattern p = Pattern.compile("(^" + TAG_FLAG + "[S]*.*[=|!|<|>]+)(.*)");
                        Matcher m = p.matcher(source.toUpperCase());
                        if (m.matches()) {//source.toUpperCase().matches("^" + TAG_FLAG + "[S]*.*[=|!|<|>]+.*")
                            tagSearch = true;
                            if (source.indexOf("&&") > 2) {
                                String[] suf_a = m.group(2).split("&&");
//                                System.out.println("8126 "+ m.group(1) + suf_a[0]);
                                result_l = getMatchingTag(getDatasource_data_view(), m.group(1) + suf_a[0], target, onlyexactmatch,
                                        childen_before, childen_during,
                                        username, checkifexists, exapandforeignKeys, addcolumnnames, childen_at_the_end, parent_at_the_end);
                                if (!result_l.isEmpty()) {
                                    result_l.remove(0);
                                }
                                for (int i = 1; (i < suf_a.length && !result_l.isEmpty()); i++) {
//                                      System.out.println("\t8131 "+ m.group(1) + suf_a[i]);
                                    ArrayList<String> new_result_l = getMatchingTag(getDatasource_data_view(), m.group(1) + suf_a[i], target,
                                            onlyexactmatch, childen_before, childen_during,
                                            username, checkifexists, exapandforeignKeys, addcolumnnames, childen_at_the_end, parent_at_the_end);
                                    if (!new_result_l.isEmpty()) {
                                        result_l.retainAll(new_result_l);
                                    } else {
                                        result_l.clear();
                                    }
                                }
                                if (!result_l.isEmpty()) {
                                    result_l.add(0, "NA");

                                }
                            } else {
                                result_l = getMatchingTag(getDatasource_data_view(), source, target, onlyexactmatch,
                                        childen_before, childen_during, username, checkifexists, exapandforeignKeys, addcolumnnames, childen_at_the_end, parent_at_the_end);

                            }
                        } else {
                            if (source.split("=")[0].equalsIgnoreCase(CODE_SEARCH_FLAG)) {
                                if (source.split("=").length > 1) {
                                    Object result_ob = readResultsFromFile(getDocRoot() + "" + source.split("=")[1]);
                                    if (result_ob != null) {
                                        String id_list = result_ob.toString();
                                        String[] split_a = id_list.split("\\|\\|");
                                        if (split_a.length > 1) {
                                            String current_tbl_nm = get_correct_table_name(split_a[0]);
                                            id_list = split_a[1];
                                            if (current_tbl_nm != null) {
                                                source = current_tbl_nm + ".id=" + id_list.replaceAll(",", "||");
                                            }
                                        }
                                    }
                                }
                            }

                            if (minmax) {
                                sql = advancedQuaryHandler__createMinMax(source);
                                sql_L.get(0).add(sql + "|" + target);
                            } else {
                                ArrayList<String> sources_l = new ArrayList<>();
                                if (source.length() > 256 && !source.contains("&&")) {
                                    String[] source_split_a = source.split("=");
                                    if (source_split_a.length == 2) {
                                        String[] q_val_split_a = source_split_a[1].split("\\|\\|", -1);
                                        int last = 0;
                                        int split_limit = 100;
                                        int safety = 1000;
                                        int i = 0;
                                        while ((last < q_val_split_a.length) && safety > 0) {
                                            safety--;
                                            StringBuilder sub_val = new StringBuilder();
                                            for (i = last; (i < q_val_split_a.length && i < (split_limit + last)); i++) {
                                                if (sub_val.length() < 1) {
                                                    sub_val.append(q_val_split_a[i].trim());
                                                } else {
                                                    sub_val.append("||" + q_val_split_a[i].trim());
                                                }
                                            }
                                            last = i;
                                            if (sub_val.length() > 0) {
                                                sources_l.add(source_split_a[0].trim() + "=" + sub_val);
                                            }
                                        }
                                    } else {
                                        sources_l.add(source);
                                    }
                                } else {
                                    sources_l.add(source);
                                }
                                for (int i = 0; i < sources_l.size(); i++) {
                                    ArrayList<String> sql_cl = advancedQuaryHandler__create(null, sources_l.get(i), target,
                                            onlyexactmatch, childen_during);
                                    if (target.toUpperCase().startsWith(TAG_FLAG) || target.toUpperCase().startsWith(TAG_FLAG2)) {
                                        target = "*";
                                    }
                                    if (sql_cl != null) {
                                        for (int l = 0; l < sql_cl.size(); l++) {
                                            sql_L.get(0).add(sql_cl.get(l) + "|" + target);

                                        }
                                    }
                                }
                            }
                        }
                        if (!tagSearch) {
                            if (sql_L.isEmpty()) {
                                sql_L = new ArrayList<>(1);
                                sql_L.get(0).add("SELECT MESSAGE FROM " + get_correct_table_name("ERRORMSGS") + " WHERE ID=1|" + get_correct_table_name("ERRORMSGS"));
                                result_l.clear();
                                result_l.add("NA");
                                result_l.add("NA");
                            } else {
//                                for (int i = 0; i < sql_L.size(); i++) {
//                                    System.out.println("7312 "+ sql_L.get(i));
//                                    
//                                }
                                result_l = getForList(sql_L, username, checkifexists, exapandforeignKeys, addcolumnnames, childen_at_the_end, parent_at_the_end);
                            }
                        }
                    }
                } catch (Exception ex) {
//                    ex.printStackTrace();
                    System.out.println("Error ASW21a: creating connections or error in query " + ex.getMessage() + "\nsql=" + sql);
                    msges = "Error ASW21a: creating connections or error in query " + ex.getMessage() + "\nsql=" + sql;
                    result_l.clear();
                    result_l.add(0, "Error ASW21a: creating connections or error in query " + ex.getMessage());
                } finally {
                    close(null, null, null);
                }
            } else {
                result_l.clear();
                result_l.add(0, "Error ASW21Ae: query format incorrect");
            }
        } else {
            result_l.clear();
            result_l.add(0, "Error ASW21Ab: Authentication against server failed");
        }
        return result_l;
    }

    /**
     * MethodID=ASxx
     *
     */
    @WebMethod(exclude = true)
    private ArrayList<String> getForList(ArrayList<ArrayList<String>> sql_L, String username,
            boolean checkifexists, boolean exapandforeignKeys, boolean addcolumnnames,
            boolean childen_at_the_end, boolean parent_at_the_end) {
        ArrayList<String> result_l = new ArrayList<>(1);
//        HashMap<String, ArrayList<String>> table2id_map = new HashMap<>();
        if (!sql_L.isEmpty() && !sql_L.get(0).isEmpty()) {
            if (sql_L.size() == 1) {
                GetResults get = new GetResults(this, getDatasource_data_view(), getWebServerName(), getDocRoot(), checkifexists,
                        exapandforeignKeys, addcolumnnames, getQueryTimeOut(), getConnectionmap(), getTables(),
                        username, getServerRoot(), get_person_id(username), childen_at_the_end, parent_at_the_end,
                        true, sql_L.get(0).toArray(new String[sql_L.get(0).size()]));
                (new Thread(get)).start();
                int wait_limit = 5;
                boolean done = false;
                while (!done && wait_limit > 0) {
                    if (get.isPostpondable(600)) {
                        done = true;
                    }
                    wait_limit--;
                }
                if (done) {
                    result_l.addAll(get.getResults());
                } else {
                    result_l.add("Slow query. I will email you on " + username + " when it is done. QueryID=" + get.getQuery_Id());
                    get.sendMail();
                }
            } else {
                GetResults get = new GetResults(this, getDatasource_data_view(), getWebServerName(), getDocRoot(), checkifexists,
                        exapandforeignKeys, addcolumnnames, getQueryTimeOut(), getConnectionmap(), getTables(),
                        username, getServerRoot(), get_person_id(username), childen_at_the_end, parent_at_the_end,
                        true, sql_L);
                (new Thread(get)).start();
                int wait_limit = 5;
                boolean done = false;
                while (!done && wait_limit > 0) {
                    if (get.isPostpondable(600)) {
                        done = true;
                    }
                    wait_limit--;
                }
                if (done) {
                    result_l.addAll(get.getResults());
                } else {
                    result_l.add("Slow query. I will email you on " + username + " when it is done. QueryID=" + get.getQuery_Id());
                    get.sendMail();
                }
            }
        } else {
            result_l.clear();
            result_l.add("NA");
            result_l.add("NA");
        }
        return result_l;
    }

    /**
     * MethodID=ASW22
     *
     */
    @WebMethod(operationName = "getforQuery")
    public List getforQuery(@WebParam(name = "authenticationid") String authenticationid, @WebParam(name = "username") String username, @WebParam(name = "query") String query, @WebParam(name = "singlecolumn") boolean singlecolumn) {
        String auth = getauthId(username);
        List result_l = new ArrayList<>(1);
        if (auth != null && authenticationid != null && !authenticationid.isEmpty() && authenticationid.equals(encript(auth))) {
            result_l = getforQuery(query, singlecolumn, null, "ASW22", getQueryTimeOut(), username);
        } else {
            result_l.add("Error 26b: Authentication failed");
        }
        return result_l;
    }

    /**
     * MethodID=ASW23
     *
     */
    @WebMethod(operationName = "getErrors")
    public String getErrors(@WebParam(name = "input") String input) {
//        System.out.println("msges=" + msges);
        return msges;
    }

    /**
     * MethodID=ASW24
     *
     */
    @WebMethod(operationName = "test")
    public Boolean test() {
        return true;
    }

    /**
     * MethodID=ASW24
     *
     */
    @WebMethod(operationName = "test2")
    public String test2(@WebParam(name = "intext") String inintext) {
        return inintext;
    }

    /**
     * MethodID=ASW25
     *
     */
    @WebMethod(operationName = "populateTables")
    public String populateTables(@WebParam(name = "authenticationid") String authenticationid, @WebParam(name = "username") String username, @WebParam(name = "query_l") List query_l) {
        String result = "OK";
        String auth = getauthId(username);
        Savepoint save1 = null;
        boolean allok = true;
        if (auth != null && authenticationid != null && !authenticationid.isEmpty() && authenticationid.equals(encript(auth))) {
            if (query_l != null || query_l.isEmpty()) {
                result = "Query list was empty";
            } else {
                Connection ncon = null;
                Statement st_1 = null;
                try {
                    ncon = getDatasource_data_update().getConnection();
                    ncon.setAutoCommit(false);
                    save1 = ncon.setSavepoint();
                    st_1 = ncon.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);

                    for (int i = 0; i < query_l.size(); i++) {
                        if (query_l.get(i) != null) {
                            st_1.addBatch(query_l.get(i).toString());
                        }
                    }
                    int[] db_r = st_1.executeBatch();
                    for (int i = 0; i < db_r.length; i++) {
                        if (db_r[i] < 0) {
                            result = "Error 75D " + query_l.get(i);
                        }
                    }
                } catch (Exception ex) {
                    msges = "Error 75A: creating connections " + ex.getMessage();
                }

                try {
                    if (allok && ncon != null && !ncon.isClosed()) {
                        ncon.commit();
                        result = "OK";
                    } else {
                        if (save1 != null) {
                            result = result + "\n Rolback started";
                            ncon.rollback(save1);
                            result = result + "\n No changes were made to the database";
                        }
                    }
                } catch (Exception ex) {
                    msges = "Error 75B: creating connections " + ex.getMessage();
                }
            }
        } else {
            result = "authentication failed";
        }
        return result;
    }

    /**
     * MethodID=ASW26
     *
     */
    @WebMethod(operationName = "update")
    public List update(@WebParam(name = "authenticationid") String authenticationid,
            @WebParam(name = "username") String username,
            @WebParam(name = "inList") java.util.List<String> inList) {
        ArrayList<String> result_l = new ArrayList<>(1);
        int reporter_id = getReporterID(username);

        Savepoint save1 = null;
        HashMap<Integer, HashMap<String, HashMap<String, String>>> data_map = list2map(inList, null);
//        System.out.println("8239 "+data_map+"  "+inList);
        ArrayList<String> table_l = null;
        if (temporarolyunavilable) {
            result_l.add(SERVICE_UNAVAILABLE_MESSAGE);
        } else {
            if (data_map != null && !data_map.isEmpty()) {
                ArrayList<Integer> line_l = new ArrayList<>(data_map.keySet());
                if (reporter_id >= 0) {
                    String auth = getauthId(username);
                    if (auth != null && authenticationid != null && !authenticationid.isEmpty() && authenticationid.equals(encript(auth))) {
                        Connection ncon = null;
                        Statement st_1 = null;
                        try {
                            ncon = getDatasource_data_update().getConnection();
                            ncon.setAutoCommit(false);
                            save1 = ncon.setSavepoint();
                            st_1 = ncon.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                        } catch (SQLException ex) {
                            result_l.add(0, "Error 54c: Database connectivity problem " + ex.getMessage());
                            msges = msges + ex.getMessage();
                        } catch (Exception ex) {
                            result_l.add(0, "Error 54d: Database connectivity problem " + ex.getMessage());
                            msges = msges + ex.getMessage();
                        }
                        boolean allok = true;
                        try {
                            if (ncon == null || ncon.isClosed() || st_1 == null || st_1.isClosed()) {
                                result_l.add(0, "Error 54f: failed to establish connection to database, this is s server side issue. ");
                                allok = false;
                            } else {
                                HashMap<String, ArrayList<String>> table_to_columns_map = new HashMap<>();
                                for (int i = 0; (i < line_l.size() && allok); i++) {
                                    HashMap<String, HashMap<String, String>> c_line_map = data_map.get(line_l.get(i));
                                    table_l = new ArrayList<>(c_line_map.keySet());
                                    for (int m = 0; m < table_l.size(); m++) {
                                        String c_id_table = table_l.get(m);
                                        ArrayList<String> columns_to_update_l;
                                        HashMap<String, String> filed_to_val_map = c_line_map.get(c_id_table);
                                        if (table_to_columns_map.containsKey(c_id_table)) {
                                            columns_to_update_l = table_to_columns_map.get(c_id_table);
                                        } else {
                                            columns_to_update_l = getColumns(c_id_table, getDatasource_data_update());
                                            table_to_columns_map.put(c_id_table, columns_to_update_l);
                                        }
                                        if (c_id_table != null && filed_to_val_map != null && filed_to_val_map.containsKey(ID_COLUMN_FALG)) {
                                            if (filed_to_val_map.get(ID_COLUMN_FALG).matches("[0-9]+")) {
                                                if (getDoesitExistID(c_id_table, filed_to_val_map.get(ID_COLUMN_FALG), st_1)) {
                                                    ArrayList<String> filed_to_val_map_key_l = new ArrayList<>(filed_to_val_map.keySet());
                                                    if (!filed_to_val_map_key_l.isEmpty()) {
                                                        String update_sql = null;
                                                        String select_sql = "";
                                                        for (int j = 0; j < filed_to_val_map_key_l.size(); j++) {
                                                            String c_field = filed_to_val_map_key_l.get(j);
                                                            String qt = "'";
                                                            if (!shoulditbequated(c_id_table, c_field, getDatasource_data_update())) {
                                                                qt = "";
                                                            }
                                                            if (select_sql.isEmpty()) {
                                                                select_sql = c_field + "=" + qt + filed_to_val_map.get(c_field) + qt;
                                                            } else {
                                                                select_sql = select_sql + " AND " + c_field + "=" + qt + filed_to_val_map.get(c_field) + qt;
                                                            }
                                                            if (!c_field.equals(ID_COLUMN_FALG)) {
                                                                if (columns_to_update_l.contains(c_field.toUpperCase())) {
                                                                    if (update_sql == null) {
                                                                        update_sql = c_field + "=" + qt + filed_to_val_map.get(c_field.toUpperCase()) + qt;
                                                                    } else {
                                                                        update_sql = update_sql + "," + c_field + "=" + qt + filed_to_val_map.get(c_field.toUpperCase()) + qt;
                                                                    }

                                                                } else {
                                                                }
                                                            }
                                                        }
                                                        if (getDoesitExistParam(c_id_table, select_sql, st_1)) {
                                                            result_l.add(c_id_table + ".id=" + filed_to_val_map.get(ID_COLUMN_FALG) + " No changes");
                                                        } else {
                                                            if (!st_1.isClosed()) {
                                                                if (addToDataBase(c_id_table, update_sql, ID_COLUMN_FALG + "=" + filed_to_val_map.get(ID_COLUMN_FALG), st_1)) {
                                                                    result_l.add(c_id_table + ".id=" + filed_to_val_map.get(ID_COLUMN_FALG) + " Update OK");
                                                                } else {
                                                                    allok = false;
                                                                }
                                                            }
                                                        }
                                                    } else {
                                                        result_l.add("ID=" + filed_to_val_map.get(ID_COLUMN_FALG) + " No updatabale information found");
                                                        allok = false;
                                                    }
                                                } else {
                                                    result_l.add("ID=" + filed_to_val_map.get(ID_COLUMN_FALG) + " Not found in database ");
                                                    allok = false;
                                                }
                                            } else {
                                                result_l.add("Error 54k " + i + ". id value was not an integer  " + filed_to_val_map.get(ID_COLUMN_FALG) + ". Not processing");
                                                allok = true;
                                            }
                                        } else {
                                            result_l.add("Error 54g " + i + ". Table name or id column missing  " + filed_to_val_map);
//                                            allok = false;
                                        }
                                    }
                                }
                            }
                        } catch (SQLException ex) {
                            result_l.add(0, "Error 54e: Failed to establish connection" + ex.getMessage());
                            msges = msges + ex.getMessage();
                            allok = false;
                        }

                        try {
                            if (allok && ncon != null && !ncon.isClosed()) {
                                ncon.commit();
                                try {
                                    if (table_l != null) {
                                        for (int i = 0; i < table_l.size(); i++) {
                                            String c_tbl = get_correct_table_name(table_l.get(i));
                                            String[] relat_a = get_key_contraints(c_tbl, dataSource_data).get(Constants.FOREIGN_KEY_COLUMNS_FLAG);
                                            if (relat_a != null) {
                                                for (int j = 0; j < relat_a.length; j++) {
                                                    String clean_sql = "DELETE  FROM " + c_tbl + " WHERE " + relat_a[j] + " IS NULL";
                                                    if (st_1 != null && !st_1.isClosed() && st_1.executeUpdate(clean_sql) >= 0) {
                                                        result_l.add("Clean up complete. DELETE ALL WITH NULL FOR " + relat_a[j]);
                                                    }
                                                }
                                            }
//                                            if (c_tbl.equals(get_correct_table_name("FILES2PATH"))) {
//                                                ArrayList<String> id_l = update_id_map.get(c_tbl);
//                                                String f_tbl = get_correct_table_name("FILES");
//                                                if (id_l != null && !id_l.isEmpty()) {
//                                                    String ids = id_l.toString().replace("[", "").replace("]", "").replaceAll("\"", "").replaceAll("\\s", "");
//                                                    String u_sql = "update " + f_tbl + " SET name=(SELECT filepath FROM " + c_tbl + " where " + c_tbl + ".files_id=" + f_tbl + ".id AND "
//                                                            + "" + c_tbl + ".id IN (" + ids + ") )";
//                                                    System.out.println("6750 " + u_sql);
//                                                    if (st_1 != null && !st_1.isClosed() && st_1.executeUpdate(u_sql) >= 0) {
//                                                        result_l.add(" Data integrity check. Table=" + f_tbl + " update as required");
//                                                    }
////                                                String u_sql="update "+f_tbl+" SET name=(SELECT path FROM "+c_tbl+" WHERE "+c_tbl+".FILES.ID=ID AND"+c_tbl+".ID IN "+;
//                                                }
//                                            }
//                                        update_id_map
                                        }
                                    }
                                } catch (SQLException ex) {
                                    result_l.add(0, "Error 54m: Data integrity enforcement failed " + ex.getMessage());
                                }
                                if (st_1 != null && !st_1.isClosed()) {
                                    st_1.close();
                                }

                                ncon.close();
                            } else if (ncon != null && !ncon.isClosed()) {
                                if (save1 != null) {
                                    result_l.add(0, "Error 54n: There were errors , roleback started \n" + msges);
                                    ncon.rollback(save1);
                                    result_l.add(0, result_l.get(0) + " Roleback ended. No changes were made to the database");
                                    st_1.close();
                                    HashMap<String, String> config_map = new HashMap<>();
                                    config_map.put("MEMORIZING_REQUESTED", "1");
                                    setConfig(ncon, config_map);
                                    ncon.close();
                                }
                            } else {
                                result_l.add(0, "Error 54m: connection was prematurely closed");
                            }
                        } catch (SQLException ex) {
                            result_l.add(0, "Error 54l: There were errors connecting to the database,");
                        } finally {
                            close(ncon, st_1, null);
                        }
                        close(ncon, st_1, null);
                    } else {
                        result_l.add(0, "Error 54a: Authentication failed");
                    }
                } else {
                    result_l.add(0, "Error 54b: Failed retrieve current user");
                }
            } else {
                result_l.add(0, "Error 54o: Failed resolve the input data. Please check the input data or file for correct format");
            }
        }
        return result_l;
    }

    /**
     * MethodID=ASW27 Request a session code getDeleteCode()
     */
    @WebMethod(operationName = "delete")
    public List delete(@WebParam(name = "authenticationid") String authenticationid,
            @WebParam(name = "username") String username,
            @WebParam(name = "inList") java.util.List<String> inList) {
        ArrayList<String> result_l = new ArrayList<>(1);
        int reporter_id = getReporterID(username);
        String auth = getauthId(username);
        Savepoint save1 = null;
        if (contrl == null) {
            getDeleteCode();
            contrl = new Service_controler();
            Thread server_control_thread = new Thread(contrl);
            server_control_thread.start();
        }
        if (Service_controler.getCounter() > 6) {
            Service_controler.reset();
            getDeleteCode();
//            System.out.println("8413 cheking @" + Timing.getDateTime());
        } else {
//            System.out.println("8415 cless than 6 cycles @" + Timing.getDateTime() + " current=" + Service_controler.getCounter());
        }
        if (inList == null || inList.size() == 1 || inList.get(0) == null || (!inList.get(0).equals(delete_request_code))) {
            result_l.add("Error: The first element must be the delete request code. "
                    + "current code=" + delete_request_code + " valid until "
                    + delete_request_code_max_validity);
        } else {
            inList.remove(0);
            HashMap<Integer, HashMap<String, HashMap<String, String>>> data_map = list2map(inList, null);
            ArrayList<Integer> line_l = new ArrayList<>(data_map.keySet());
            if (temporarolyunavilable) {
                result_l.add(SERVICE_UNAVAILABLE_MESSAGE);
            } else {
                if (reporter_id >= 0) {
                    if (auth != null && authenticationid != null && !authenticationid.isEmpty() && authenticationid.equals(encript(auth))) {
                        Connection ncon = null;
                        Statement st_1 = null;
                        try {
                            ncon = getDatasource_data_update().getConnection();
                            ncon.setAutoCommit(false);
                            save1 = ncon.setSavepoint();
                            st_1 = ncon.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                        } catch (SQLException ex) {
                            result_l.add(0, "Error ASW27c: Database connectivity problem " + ex.getMessage());
                            msges = msges + ex.getMessage();
                        } catch (Exception ex) {
                            result_l.add(0, "Error ASW27d: Database connectivity problem " + ex.getMessage());
                            msges = msges + ex.getMessage();
                        }
                        boolean allok = true;
                        try {
                            if (ncon == null || ncon.isClosed() || st_1 == null || st_1.isClosed()) {
                                result_l.add(0, "Error ASW27f: failed to establish connection to database, this is s server side issue. ");
                                allok = false;
                            } else {
                                for (int i = 0; (i < line_l.size() && allok); i++) {
                                    HashMap<String, HashMap<String, String>> c_line_map = data_map.get(line_l.get(i));
                                    ArrayList<String> c_table_l = new ArrayList<>(c_line_map.keySet());
                                    if (c_table_l.size() > 1) {
                                        c_table_l = decide_table_order(c_table_l, true);
                                    }
                                    for (int m = 0; m < c_table_l.size(); m++) {
                                        String c_id_table = c_table_l.get(m);
                                        ArrayList<String> delete_l = getDeleteList(c_id_table, c_line_map, st_1);
                                        if (!delete_l.isEmpty()) {
                                            for (int j = 0; (j < delete_l.size() && allok); j++) {
                                                String c_q = delete_l.get(j).substring(0, delete_l.get(j).indexOf("|"));
                                                if (st_1.executeUpdate(c_q) < 0) {
                                                    allok = false;
                                                    result_l.add("Removing dependency " + delete_l.get(j).substring(delete_l.get(j).indexOf("|") + 1) + ".... Failed");
                                                } else {
                                                    result_l.add("Removing dependency " + delete_l.get(j).substring(delete_l.get(j).indexOf("|") + 1) + "....  OK");
                                                }
                                            }
                                        } else {
                                            result_l.add("The requested record was not found");
                                        }
                                    }
                                }
                            }
                        } catch (SQLException ex) {
                            result_l.add(0, "Error ASW27a: Failed to establish connection" + ex.getMessage());
                            msges = msges + ex.getMessage();
                            allok = false;
                        }
                        try {
                            if (ncon != null && !ncon.isClosed()) {
                                if (allok) {
                                    ncon.commit();
                                    result_l.add("All updates committed to the database");
                                    HashMap<String, String> config_map = new HashMap<>();
                                    config_map.put("MEMORIZING_REQUESTED", "1");
                                    setConfig(ncon, config_map);
                                    close(ncon, st_1, null);
                                } else {
                                    if (save1 != null) {
                                        result_l.add(0, "Error: There were errors , roleback started ");
                                        ncon.rollback(save1);
                                        result_l.add(0, result_l.get(0) + " Roleback ended. No changes were made to the database");
                                        close(ncon, st_1, null);
                                    }
                                }
                            } else {
                                result_l.add(0, "Error ASW27n: connection was prematurely closed");
                            }
                        } catch (SQLException ex) {
                            result_l.add(0, "Error ASW27o: " + ex.getMessage());
                        } finally {
                            close(ncon, st_1, null);
                        }
                        close(ncon, st_1, null);
                    } else {
                        result_l.add(0, "Error ASW27a: Authentication failed");
                    }
                } else {
                    result_l.add(0, "Error ASW27b: Failed retrieve current user");
                }

            }
        }
        return result_l;
    }

    /**
     * MethodID=ASW28
     *
     */
//    @WebMethod(operationName = "delete2")
//    public List delete2(@WebParam(name = "username") String username, @WebParam(name = "authenticationid") String authenticationid, @WebParam(name = "inList") List inList) {
//        ArrayList<String> result_l = new ArrayList<String>(1);
//        int reporter_id = getReporterID(username);
//        Savepoint save1 = null;
//        HashMap<Integer, HashMap<String, HashMap<String, String>>> data_map = list2map(inList, null);
//        ArrayList<Integer> line_l = new ArrayList<>(data_map.keySet());
//
////        if (ptn_idcolumn == null) {
////            ptn_idcolumn = Pattern.compile(ID_COLULMN_PATTERN);
////        }
////        if (ptn_allcolumn == null) {
////            ptn_allcolumn = Pattern.compile(ALL_COLULMN_PATTERN);
////        }
//        if (temporarolyunavilable) {
//            result_l.add(SERVICE_UNAVAILABLE_MESSAGE);
//        } else {
//            if (reporter_id >= 0) {
//                String auth = getauthId(username);
//                if (auth != null && authenticationid != null && !authenticationid.isEmpty() && authenticationid.equals(encript(auth))) {
//                    Connection ncon = null;
//                    Statement st_1 = null;
//                    try {
//                        ncon = getDatasource_data_update().getConnection();
//                        ncon.setAutoCommit(false);
//                        save1 = ncon.setSavepoint();
//                        st_1 = ncon.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
//                    } catch (SQLException ex) {
//                        result_l.add(0, "Error 55c: Database connectivity problem " + ex.getMessage());
//                        msges = msges + ex.getMessage();
//                    } catch (Exception ex) {
//                        result_l.add(0, "Error 55d: Database connectivity problem " + ex.getMessage());
//                        msges = msges + ex.getMessage();
//                    }
//                    boolean allok = true;
//                    try {
//                        if (ncon == null || ncon.isClosed() || st_1 == null || st_1.isClosed()) {
//                            result_l.add(0, "Error 55f: failed to establish connection to database, this is s server side issue. ");
//                            allok = false;
//                        } else {
//                            for (int i = 0; (i < line_l.size() && allok); i++) {
//                                HashMap<String, HashMap<String, String>> c_line_map = data_map.get(line_l.get(i));
//                                ArrayList<String> c_table_l = new ArrayList<>(c_line_map.keySet());
//                                for (int m = 0; m < c_table_l.size(); m++) {
//                                    String c_id_table = c_table_l.get(m);
//                                    String relace_flag = REPLACEWITH_FALG + get_correct_table_name(c_id_table) + "." + ID_COLUMN_FALG;
//                                    relace_flag = relace_flag.toUpperCase();
//                                    HashMap<String, String> filed_to_val_map = c_line_map.get(c_id_table);
//                                    if (c_id_table != null && filed_to_val_map.containsKey(ID_COLUMN_FALG)) {
//                                        if (filed_to_val_map.get(ID_COLUMN_FALG).matches("[0-9]+")) {
//                                            if (getDoesitExistID(c_id_table, filed_to_val_map.get(ID_COLUMN_FALG), st_1)) {
//                                                boolean dependancies_cleared = true;
//                                                ArrayList<String> dipendancies_l = getDependancies(c_id_table);
////                                                System.out.println("3629 ");
////                                                for (int j = 0; j < dipendancies_l.size(); j++) {
////                                                    System.out.println(dipendancies_l.get(j));
////                                                }
//                                                for (int j = 0; (j < dipendancies_l.size() && dependancies_cleared); j++) {
//                                                    String sub_sql = dipendancies_l.get(j);
//                                                    sub_sql = "Delete " + sub_sql.replaceAll(relace_flag, filed_to_val_map.get(ID_COLUMN_FALG));
//                                                    int sublen = 100;
//                                                    if (sub_sql.length() < sublen) {
//                                                        sublen = sub_sql.length();
//                                                    }
//                                                    if (st_1.executeUpdate(sub_sql) < 0) {
//                                                        dependancies_cleared = false;
//                                                    } else {
//                                                        result_l.add("removing dependency " + j + " " + sub_sql.substring(0, sublen) + ".... Update OK");
//                                                    }
//                                                }
//                                                if (dependancies_cleared) {
//                                                    ArrayList<String> filed_to_val_map_key_l = new ArrayList<String>(filed_to_val_map.keySet());
//                                                    if (!filed_to_val_map_key_l.isEmpty()) {
//                                                        String delete_sql = "Delete from  " + c_id_table + " WHERE " + c_id_table + ".id=" + filed_to_val_map.get(ID_COLUMN_FALG);
//                                                        if (!st_1.isClosed()) {
//                                                            if (st_1.executeUpdate(delete_sql) >= 0) {
//                                                                result_l.add(c_id_table + ".id=" + filed_to_val_map.get(ID_COLUMN_FALG) + " Update OK");
//                                                            } else {
//                                                                allok = false;
//                                                            }
//                                                        }
//                                                    } else {
//                                                        result_l.add("ID=" + filed_to_val_map.get(ID_COLUMN_FALG) + " No delete information found");
//                                                        allok = false;
//                                                    }
//                                                } else {
//                                                    result_l.add("Error 55m " + i + ". id  " + filed_to_val_map.get(ID_COLUMN_FALG) + " can not be as the attempt to removing dependent entries failed");
//                                                }
//
//                                            } else {
//                                                result_l.add("ID=" + filed_to_val_map.get(ID_COLUMN_FALG) + " Not found in database ");
//                                            }
//                                        } else {
//                                            result_l.add("Error ASW28i " + i + ". id value was not an integer  " + filed_to_val_map.get(ID_COLUMN_FALG));
//                                            allok = false;
//                                        }
//                                    } else {
//                                        result_l.add("Error ASW28j " + i + ". Table name missing  ");
//                                        allok = false;
//                                    }
//                                }
//                            }
//                        }
//                    } catch (SQLException ex) {
//                        result_l.add(0, "Error =ASW28a: Failed to establish connection" + ex.getMessage());
//                        msges = msges + ex.getMessage();
//                        allok = false;
//                    }
//
//                    try {
//                        if (allok && ncon != null && !ncon.isClosed()) {
//                            ncon.commit();
//                            HashMap<String, String> config_map = new HashMap<>();
//                            config_map.put("MEMORIZING_REQUESTED", "1");
//                            setConfig(ncon, config_map);
//                            ncon.close();
//                        } else if (ncon != null && !ncon.isClosed()) {
//                            if (save1 != null) {
//                                result_l.add(0, "Error: There were errors , roleback started ");
//                                ncon.rollback(save1);
//                                result_l.add(0, result_l.get(0) + " Roleback ended. No changes were made to the database");
//                                ncon.close();
//                            }
//                        } else {
//                            result_l.add(0, "Error ASW28n: connection was prematurely closed");
//                        }
//                    } catch (SQLException ex) {
//                        result_l.add(0, "Error ASW28o: " + ex.getMessage());
//                    }
//
//                    try {
//                        if (ncon != null && !ncon.isClosed()) {
//                            ncon.close();
//                        }
//                    } catch (SQLException ex) {
//                    }
//                } else {
//                    result_l.add(0, "Error ASW28a: Authentication failed");
//                }
//            } else {
//                result_l.add(0, "Error ASW28b: Failed retrieve current user");
//            }
//
//        }
//        return result_l;
//    }
//
    /**
     *
     * MethodID=ASW29
     *
     */
    @WebMethod(operationName = "backup")
    public String[] backup(@WebParam(name = "username") String username, @WebParam(name = "authenticationid") String authenticationid) {
        String auth = getauthId(username);
        if (auth != null && authenticationid != null && !authenticationid.isEmpty() && authenticationid.equals(encript(auth))) {
        } else {
            String[] tmp = new String[1];
            tmp[0] = "Error ASW30a: Authentication failed";
        }
        return null;
    }

    /**
     * MethodID=ASW31
     *
     */
    @WebMethod(operationName = "getUniqueKeys")
    public List getUniqueKeys(@WebParam(name = "username") String username, @WebParam(name = "authenticationid") String authenticationid, @WebParam(name = "table") String table) {
        ArrayList<String> result_l = new ArrayList<>(1);
        String auth = getauthId(username);
        if (auth != null && authenticationid != null && !authenticationid.isEmpty() && authenticationid.equals(encript(auth))) {
            result_l = getUniqueList(table);
        } else {
            result_l.add(0, "Error ASW31a: Authentication failed");
        }
        return result_l;
    }

    /**
     * MethodID=ASW32
     *
     */
    @WebMethod(operationName = "createTagSource")
    public String createTagSource(@WebParam(name = "username") String username, @WebParam(name = "authenticationid") String authenticationid, @WebParam(name = "tagsource_table") String tagsource_table) {
        String result = "Error: unknown error";
        String auth = getauthId(username);
        if (auth != null && authenticationid != null && !authenticationid.isEmpty() && authenticationid.equals(encript(auth))) {
            int level = get_userlevel(username);
            if (level < MINIMUM_LEVEL_TO_CREATE_TAGS) {
                if (get_correct_table_name(tagsource_table) == null) {
                    tagsource_table = tagsource_table.split("\\.")[tagsource_table.split("\\.").length - 1];
                    tagsource_table = tagsource_table.toUpperCase().replaceAll("[^A-Za-z0-9_]", "_");
                    String top_parent = tagsource_table.replace("_TAGSOURCE", "");
                    if (!tagsource_table.toUpperCase().endsWith("_TAGSOURCE")) {
                        tagsource_table = tagsource_table + "_TAGSOURCE";
                    }
                    tagsource_table = DATABASE_NAME_DATA + "." + tagsource_table;

                    if (get_correct_table_name(tagsource_table) == null) {
                        Connection ncon = null;
                        Statement st_1 = null;
                        try {
                            ncon = getDatasource_data_update().getConnection();
                            st_1 = ncon.createStatement();
                            st_1.executeUpdate("CREATE TABLE " + tagsource_table + "(id int, parent_id int,name varchar(256), URL varchar(1024) )");
                            st_1.executeUpdate("INSERT INTO " + tagsource_table + "(id,parent_id,name, URL) values(0,0,'" + top_parent + "','NA')");
                            st_1.close();
                            HashMap<String, String> config_map = new HashMap<>();
                            config_map.put("MEMORIZING_REQUESTED", "1");
                            setConfig(ncon, config_map);
                            ncon.close();
                            result = index_tagsources(tagsource_table, getDatasource_data_update());
                            result = result + "\nTag source created " + tagsource_table;
                            int n_result = refreshTableTOFeatures(getDatasource_data_update());
                            if (n_result > 0) {
                                result = result + " Tag source ready to use. This will be available for web interface after a server restart. ";
                            } else {
                                result = result + " Automatic refresh failed. Server restart required";
                            }
                            Constants.corect_tbl_nm_map = null;
                            all_table_l = null;
                        } catch (SQLException ex) {
                            result = "Error AWW32b: Database connectivity problem " + ex.getMessage();
                        } catch (Exception ex) {
                            result = "Error AWW32c:  Database connectivity problem " + ex.getMessage();
                        } finally {
                            close(ncon, st_1, null);
                        }
                    } else {
                        result = "The tag source " + tagsource_table + " already threre, nothing changed";
                    }

                } else {
                    result = "The tag source " + tagsource_table + " already threre, nothing changed";
                }
//     
//            
            } else {
                result = "Error: this operation requires level " + MINIMUM_LEVEL_TO_CREATE_TAGS + " or above clearance. Your level =" + level;
            }

        } else {
            result = "Error ASW32a: Authentication failed";
        }
        return result;
    }

    /**
     * MethodID=ASW32
     *
     */
    @WebMethod(operationName = "createuseraccount")
    public String createuseraccount(@WebParam(name = "email") String email,
            @WebParam(name = "pin") String pin, @WebParam(name = "control_info") String control_info) {
        String result = "Error: unknown error";
        if (user_pin_map == null) {
            user_pin_map = new HashMap<>();
        }
        if (user_pin_attempts == null) {
            user_pin_attempts = new HashMap<>();
        }
        if (blocke_mail_l == null) {
            blocke_mail_l = new ArrayList<>();
        }
        if (email_to_request_time_map == null) {
            email_to_request_time_map = new HashMap<>();
        }
        if (email == null || email.isEmpty()) {
            result = "Error ASW32a: empty or null email";
        } else {
            email = email.toLowerCase();
            if (email.matches("[a-z0-9_\\-\\.]+@[A-z0-9_\\-]+[\\.]{1}[a-z0-9_\\-\\.]+")) {
                if (blocke_mail_l.contains(email)) {
                    result = "Error ASW32e: The " + email + " is blocked. Please contact the administrator";
                } else {
                    if (pin == null) {
                        if (user_pin_map.containsKey(email)) {
                            GregorianCalendar new_cal = new GregorianCalendar();
                            if (email_to_request_time_map.get(email) != null) {
                                int mint_dif = (new_cal.get(Calendar.MINUTE) - email_to_request_time_map.get(email).get(Calendar.MINUTE));
//                                System.out.println("8819 mint_dif=" + mint_dif + " ");
                                if (mint_dif > 5 || mint_dif < 0) {
                                    result = "ERRORCODE: A veryfication code was sent to " + email + ".On  " + email_to_request_time_map.get(email).getTime()
                                            + " now reset as it expired. Please try again";
                                    user_pin_map.remove(email);
                                    email_to_request_time_map.remove(email);
                                } else {
                                    result = "ERRORCODE: A veryfication code was already sent to " + email + ". On  " + email_to_request_time_map.get(email).getTime()
                                            + ". This will be reset in " + Math.abs(6 - mint_dif) + " minutes";
                                }
                            } else {
                                result = "There was an error please try again in 5 minutes";
                                user_pin_map.remove(email);
                            }

                        } else {
                            int email_check = emailInUse(email);
                            if (email_check == 1) {
                                result = "The email " + email + " is already in use";
                            } else if (email_check == 0) {
                                String code = randomstring_num();
                                user_pin_map.put(email, encript_sha1(code));
                                if (sendUserCodeConfirmation(email, code) == 1) {
                                    email_to_request_time_map.put(email, new GregorianCalendar());
                                    result = "CODE: A veryfication code was sent to " + email + "";
                                } else {
                                    result = "ERROR ASW32f: mail to  " + email + " failed, please try again later";
                                }
                            } else {
                                result = "Error ASW32b: when checking againt the database with " + email + " the error " + email_check + " occured.";
                            }
                        }
                        ///home/sabryr/NetBeansProjects/eGenVar_WebService/web/WEB-INF/wsit-webservice.medisin.ntnu.no.Authenticate_service.xml
                    } else {
                        if (user_pin_map.containsKey(email) && user_pin_map.get(email).equals(encript_sha1(pin))) {
                            String pass = randomstring();
                            HashMap<String, String> acouunt_map = new HashMap<>();
                            acouunt_map.put("email", email);
                            acouunt_map.put("password", pass);
                            if (control_info != null && control_info.matches("[0-9\\.]+")) {
                                acouunt_map.put("ip", control_info);
                            } else {
                                acouunt_map.put("ip", "0.0.0.0");
                            }
                            insert_user_account(acouunt_map);
                            if (sendUserPassword(email, email, email, pass) < 0) {
                                result = "Error ASW32i: Sending password faild to " + email + " Please contact the administratior with this error code.";
                            } else {
                                result = "CREARTED:An account was created and login details mailed to " + email;
                                sendMessage(getAdminEmail(), "Admin", "New user: " + email, "Admin");
                            }
                            create_userprofile(email);
                        } else {
                            if (!user_pin_attempts.containsKey(email)) {
                                user_pin_attempts.put(email, 1);
                            } else {
                                user_pin_attempts.put(email, user_pin_attempts.get(email) + 1);
                            }
                            if (user_pin_attempts.get(email) > 3) {
                                result = "Error ASW32b: Invalid code and maximum attempts reached. Please contact the administrator";
                                blocke_mail_l.add(email);
                            } else {
                                result = "ERROR: !Invalid pin " + (4 - user_pin_attempts.get(email)) + " attemts left.\nA veryfication code was sent to " + email + ".";
                            }
                        }
                    }
                }
            } else {
                result = "Error ASW32c: Invalid email  " + email;
            }
        }
        return result;
    }

    /**
     * MethodID=ASW33
     *
     */
    @WebMethod(operationName = "get_servername")
    public String get_servername(@WebParam(name = "intext") String intext) {
        String name = null;
        try {
            String sql = "select name from " + get_correct_table_name("serveridentity");
            Statement st_1 = null;;
            Connection ncon = null;;
            ResultSet r_1 = null;
            try {
                ncon = getDatasource_data_view().getConnection();
                st_1 = ncon.createStatement();
                r_1 = st_1.executeQuery(sql);
                while (r_1.next()) {
                    name = r_1.getString(1);
                }
                close(ncon, st_1, r_1);
            } catch (Exception ex) {
                errors = errors + ex.getMessage();
            } finally {
                close(ncon, st_1, r_1);
            }
        } catch (Exception ex) {
            errors = errors + ex.getMessage();
        }
        return name;
    }

    /**
     * MethodID=ASW33
     *
     */
    @WebMethod(operationName = "reset_tags2host")
    public String reset_tags2host(@WebParam(name = "username") String username, @WebParam(name = "authenticationid") String authenticationid, @WebParam(name = "host_id") String host_id) {
        String auth = getauthId(username);
        String name = null;
        if (auth != null && authenticationid != null && !authenticationid.isEmpty() && authenticationid.equals(encript(auth))) {
            try {
                String sql = "delete from " + get_correct_table_name("tags2host") + " where host='" + host_id + "'";
                Statement st_1 = null;
                Connection ncon = null;
                try {
                    ncon = getDatasource_data_view().getConnection();
                    st_1 = ncon.createStatement();
                    int updates = st_1.executeUpdate(sql);
                    name = +updates + " Removed from tags2host";
                    HashMap<String, String> config_map = new HashMap<>();
                    config_map.put("MEMORIZING_REQUESTED", "1");
                    setConfig(ncon, config_map);
                    close(ncon, st_1, null);
                } catch (Exception ex) {
                    errors = errors + ex.getMessage();
                } finally {
                    close(ncon, st_1, null);
                }
            } catch (Exception ex) {
                errors = errors + ex.getMessage();
            }
        } else {
            errors = errors + ". Authentication failed ";
        }

        return name + "\n" + errors;
    }

    /**
     * MethodID=ASW34 else {//consider it as a sample name table_target =
     * get_correct_table_name("SAMPLEDETAILS"); column_target = "NAME";
     * value_target = target_a[0]; }
     *
     * Possible return values 1 : success without errors (either tagged or tag
     * already exists) -1: Target was null or empty -2: Failed to establish
     * connection to database on server side -3:Database connection error when
     * looking for the target -4:Invalid target table name -5: Tag not provided
     * -6: Database communication issues on the server side -7: Requested tag
     * does not exist and creating new one failed -8: More than one matching
     * tag, be more specific by providing full path, URI or hash -9: Error
     * during processing the tag label -1x: Tagging failed. -21: Recording new
     * tag failed.32=column name was null
     */
    @WebMethod(operationName = "tag")
    public int tag(@WebParam(name = "username") String username,
            @WebParam(name = "authenticationid") String authenticationid,
            @WebParam(name = "target") String target,
            @WebParam(name = "tag") String tag) {
        int result = -1;

        if (target != null && !target.isEmpty()) {
            result = 1;
            Statement stm_1 = null;
            Savepoint save1 = null;
            Connection ncon = null;
//            String c_val = null;
//            HashMap<String, String> val_map = null;
            ArrayList<HashMap<String, String>> val_map_map = new ArrayList<>();
            try {
                ncon = getDatasource_data_update().getConnection();
                ncon.setAutoCommit(false);
                save1 = ncon.setSavepoint();
                stm_1 = ncon.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            } catch (Exception ex) {
                msges = "Error ASW32a: creating connections " + ex.getMessage();
                close(ncon, stm_1, null);
                result = -2;
            }
            if (result == 1) {
                String[] targtes_a = target.split("\\|\\|");
                String[] first_target_a = targtes_a[0].split("=", 2);
                String table_target = null;
                String column_target = null;
//                String value_target = null;

                if (first_target_a.length == 1) {
                    first_target_a[0] = first_target_a[0].trim();
                    if (first_target_a[0].length() == 40) {//hash
                        table_target = get_correct_table_name("FILES");
                        column_target = "CHECKSUM";
                        targtes_a[0] = first_target_a[0];
                    } else {
                        table_target = get_correct_table_name("FILES");
                        column_target = "NAME";
                        targtes_a[0] = first_target_a[0];
                    }
                } else {
                    String[] sub_target_a = first_target_a[0].split("\\.", 2);
                    if (sub_target_a.length == 1) {
                        table_target = get_correct_table_name(sub_target_a[0]);
                        column_target = "NAME";
                    } else {
                        table_target = get_correct_table_name(sub_target_a[0]);
                        column_target = sub_target_a[1];
                    }
                    targtes_a[0] = first_target_a[1];
                }

                /*Find the target to be tagged*/
//                int targte_id = -1;
                HashSet<Integer> taergte_id_s = new HashSet<>();
                try {
                    if (stm_1 != null) {
                        /*Create search query in a way specific to the current DBMS used*/
                        String q = "'";
                        if (!shoulditbequated(table_target, column_target, getDatasource_data_update())) {
                            q = "";
                        }
                        ResultSet r_1 = null;

                        if (targtes_a.length == 1) {
                            r_1 = stm_1.executeQuery("Select id from " + table_target + " where " + column_target + "=" + q + targtes_a[0].trim() + q);
                        } else {
                            String tagrget_modfid = q + targtes_a[0].trim() + q;
                            for (int i = 1; i < targtes_a.length; i++) {
                                tagrget_modfid = tagrget_modfid + "," + q + targtes_a[i].trim() + q;
                            }
                            r_1 = stm_1.executeQuery("Select id from " + table_target + " where " + column_target + " in  (" + tagrget_modfid + ")");
                        }
//                    System.out.println("8725 " + "Select id from " + table_target + " where " + column_target + "=" + q + value_target.trim() + q);
//                      

                        if (r_1.next()) {
                            taergte_id_s.add(r_1.getInt(1));
                        } else {
                            result = -12;
                        }
                        while (r_1.next() && result > 0) {
                            if (targtes_a.length == 1) {
                                result = -13;
                            } else {
                                taergte_id_s.add(r_1.getInt(1));
                            }
                        }
                        r_1.close();
                    }
                } catch (Exception ex) {
                    msges = "Error ASW32a: creating connections " + ex.getMessage();
                    close(ncon, stm_1, null);
                    result = -3;
                }
                String tag_table = get_correct_table_name(table_target + TO_TAG_FLAG);
//                System.out.println("8742 tag_table="+tag_table+" "+table_target +" tag="+tag+"\tresult="+result+"\t"+Timing.convert(Timing.getFromlastPointer2()));
//                   Timing.setPointer2();
                if (result < 0) {
                } else if (tag_table == null) {
                    result = -4;
                } else {
                    if (tag == null) {
                        result = -5;
                    } else if (stm_1 != null) {
                        String[] tag_a = tag.split("&&");
//                        System.out.println("9289 " + Arrays.deepToString(tag_a));
                        String column = null;
                        for (int k = 0; (k < tag_a.length && result == 1); k++) {
                            HashMap<String, String> val_map = null;
                            tag = tag_a[k].trim();
                            if (!tag.isEmpty()) {
                                String processed_tag = tag;
                                int dot_index = tag.indexOf(".");
                                int equal_index = tag.indexOf("=");
                                if (equal_index > 0) {
                                    processed_tag = tag.substring(equal_index + 1);
                                    if (dot_index > 0) {
                                        column = tag.substring(0, equal_index).substring(dot_index + 1);
                                    }
                                }
                                if (column != null) {
                                    tag = "TAG." + column + "=" + processed_tag;
                                } else {
                                    tag = "TAG=" + processed_tag;
                                }
                                List<String> val_l = getMatchingTag(getDatasource_data_update(), tag, "TAG.HASH,TAG.NAME,TAG.PATH,TAG.URI",
                                        true, false, false, username, false, false, true, false, false); //
//                                System.out.println("5204 " + tag + "  " + val_l+"\n");
                                if (val_l == null) {
                                    result = -7;
                                } else {
                                    val_l.remove(0);//remove metadata
                                    if (val_l.size() == 1) {
                                        val_map = new HashMap<>();
                                        String[] val_a = val_l.get(0).split("\\|\\|");
                                        for (int i = 0; i < val_a.length; i++) {
                                            if (val_a[i].toUpperCase().indexOf("HASH") > 0) {
                                                val_map.put("LINK_TO_FEATURE", val_a[i].replace(".HASH=", "|HASH="));
                                            } else {
                                                if (val_a[i].indexOf("=") > 2 && val_a[i].indexOf(".") > 2) {
                                                    String c_val = val_a[i].substring(val_a[i].indexOf("=") + 1);
                                                    String c_col = val_a[i].substring(val_a[i].indexOf(".") + 1, val_a[i].indexOf("="));
                                                    if (c_col.equals("PATH")) {
                                                        val_map.put("DESCRIPTION", c_val);
                                                    } else {
                                                        val_map.put(c_col, c_val);
                                                    }
                                                } else {
                                                    result = -15;
                                                }

                                            }
                                        }
                                    } else if (val_l.size() > 1) {
                                        if (column != null) {
                                            String uper_column = column.toUpperCase();
                                            if (uper_column.equals("HASH") || uper_column.equals("URI")) {
                                                val_map = new HashMap<>();
                                                String[] val_a = val_l.get(0).split("\\|\\|");
                                                for (int i = 0; i < val_a.length; i++) {
                                                    if (val_a[i].toUpperCase().indexOf("HASH") > 0) {
                                                        val_map.put("LINK_TO_FEATURE", val_a[i].replace(".HASH=", "|HASH="));
                                                    } else {
                                                        try {
                                                            if (val_a[i].indexOf("=") > 2 && val_a[i].indexOf(".") > 2) {
                                                                String c_val = val_a[i].substring(val_a[i].indexOf("=") + 1);
                                                                String c_col = val_a[i].substring(val_a[i].indexOf(".") + 1, val_a[i].indexOf("="));
                                                                if (c_col.equals("PATH")) {
                                                                    val_map.put("DESCRIPTION", c_val);
                                                                } else {
                                                                    val_map.put(c_col, c_val);
                                                                }
                                                            } else {
                                                                result = -15;
                                                            }
                                                        } catch (StringIndexOutOfBoundsException ex) {
                                                            System.out.println("Error:ASW34c: " + ex.getMessage() + " " + val_a[i]);
                                                        }
                                                    }
                                                }
//                                        }
                                            } else {
                                                result = -8;
                                            }
                                        } else {
                                            result = -31;
                                        }

                                    } else {
                                        result = -9;
                                    }
                                }
                            }
                            if (val_map != null) {
                                val_map_map.add(val_map);
                            }
                        }
                    } else {
                        result = -6;
                    }
                }
                if (taergte_id_s.isEmpty()) {
                    result = -30;
                } else {
                    if (result > 0 && !val_map_map.isEmpty()) {
                        result = 1;
                        String target_col_ref = table_target.substring(table_target.indexOf(".") + 1) + "_ID"; // modify this to use foreing keys

                        for (int i = 0; (i < val_map_map.size() && result > 0); i++) {
                            HashMap<String, String> val_map = val_map_map.get(i);
//                            System.out.println("\n9349 " + val_map + "\n");
                            Iterator<Integer> ids_i = taergte_id_s.iterator();
                            while (ids_i.hasNext() && result > 0) {
                                HashMap<String, String> c_value_map = new HashMap<>();
                                c_value_map.put(target_col_ref, ids_i.next() + "");
                                c_value_map.putAll(val_map);
//                                System.out.println("9352 " + c_value_map);
                                if (getOrAdd(getDatasource_data_update(), false, false, stm_1,
                                        tag_table, c_value_map, null,
                                        new ArrayList<String>(), true, null) < 0) {
                                    result = -21;
                                }
                            }
                        }

                    }
                }
                try {
                    /*Todo: check if added or already existed*/
                    if (result >= 0 && ncon != null && !ncon.isClosed()) {
                        ncon.commit();
                        HashMap<String, String> config_map = new HashMap<>();
                        config_map.put("MEMORIZING_REQUESTED", "1");
                        setConfig(ncon, config_map);
                        ncon.close();
                    } else if (ncon != null && !ncon.isClosed()) {
                        if (save1 != null) {
                            ncon.rollback(save1);
                            ncon.close();
                        }
                    }
                } catch (SQLException ex) {
                    System.out.println("Error:ASW34b: " + ex.getMessage());
                }
            }
        }
//        System.out.println("8851 "+Timing.convert(Timing.getFromlastPointer2())+"\n\n");
        return result;
    }
}
