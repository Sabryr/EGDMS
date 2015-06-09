package no.ntnu.medisn.egenvar;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
import java.io.IOException;
import java.io.PrintWriter;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Formatter;
import java.util.HashMap;
import java.util.Set;
import java.util.TimeZone;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;

/**
 * +
 *
 * @author sabryr
 */
public class Constants extends HttpServlet {

    /**
     * Processes requests for both HTTP
     * <code>GET</code> and
     * <code>POST</code> methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error
     * occursans-180234.stolav.ntnu.no
     * @throws IOException if an I/O error occurs
     */
    public static String VERSION = null;
    private static int server_mode = -2;
    private static HashMap<String, String> forign_key_to_table_m;
    private static HashMap<String, String> chartosymb_map;
//    public final static String server_root = "https://ans-180228.stolav.ntnu.no:8185/";
//    public final static String servernm = server_root + "eGenVar_datamanagement4/";
//    public final static String servernm = server_root + "eGenVar_web/";
//    public final static String servernm = "https://tingeling:8181/eGenVar_datamanagement2/";| <a href=\"" + Constants.getServerName() + "Search/search.jsp\" >Advanced search</a> 
//    public final static String servernm = "https://localhost:8181/eGenVar_datamanagement4/";
//    public final static String servernm = "https://localhost:8185/eGenVar_datamanagement4/";
    //scp /home/sabryr/NetBeansProjects/eGenVar_datamanagement2/dist/eGenVar_datamanagement2.war   sabryr@tingeling.medisin.ntnu.no:~/glassfish3/glassfish/diploy/
    // scp /home/sabryr/NetBeansProjects/eGenVar_datamanagement2/dist/eGenVar_datamanagement2.war  sabryr@tingeling.medisin.ntnu.no:~/glassfish3/glassfish/diploy/
    //./asadmin start-domain egenvar
    //| <a href=\"" + Constants.getServerName() + "Upload/InsertFile.jsp\" >Create new file report</a> | <a href=\"" + Constants.getServerName() + "Upload/BacthFileUpload.jsp\">Batch insert</a> 
//    private static final String menu = "<p><a href=\"" + Constants.getServerName() + "\" >Home</a> | "
//            + "<a href=\"" + Constants.getServerName() + "logout\" >Login/Logout</a> | "
//            + "<a href=\"" + Constants.getServerName() + "Search/search\" >Search</a> | "
//            + "<a href=\"" + Constants.getServerName() + "Public_search\" >Public search</a> | "
//            + "<a href=\"" + Constants.getServerName() + "Edit/table_selection?operation=update\"> Update</a> | "
//            + "<a href=\"" + Constants.getServerName() + "Edit/table_selection?operation=delete&value_return_to_page=" + Constants.getServerName() + "Edit/Delete\" >Delete</a> | "
//            + "<a href=\"" + Constants.getServerName() + "Edit/table_selection?operation=insert&value_return_to_page=" + Constants.getServerName() + "Upload/CreateNew&resethistory=true\" >Create</a> | "
//            + "<a href=\"" + Constants.getServerName() + "Upload/logintodirectory\" >Auto Create</a> | "
//            + "<a href=\"" + Constants.getServerName() + "visualizer\" >Tag graphs</a> | "
//            + "<a href=\"" + Constants.getServerName() + "Search/duplicate_folders_report.txt\" >Reports</a> | "
//            + "<a href=\"" + getSecureServerRoot() + "webinterface_tutorial.pdf\">Help</a></p>";
//    private static final String menu_public = "<p><a href=\"" + Constants.getServerName() + "\" >Home</a> | "
//            + "<a href=\"" + Constants.getServerName() + "logout\" >Login/Logout</a> | "
//            + "<a href=\"" + Constants.getServerName() + "Public_search\" >Public search</a> | "
//            + "<a href=\"" + getSecureServerRoot() + "webinterface_tutorial.pdf\">Help</a></p>";
//    public static final String menu_login = "<a href=\"" + Constants.getServerName() + "\" >Home</a> | "
//            + "<a href=\"" + Constants.getServerName() + "login\" >Login</a> | "
//            + "<a href=\"" + Constants.getServerName() + "Search/search\" >Search</a> |"
//            + "<a href=\"" + Constants.getServerName() + "Edit/table_selection?operation=update&value_return_to_page=" + Constants.getServerName() + "Edit/Update\"> Update</a>| "
//            + "<a href=\"" + Constants.getServerName() + "Edit/table_selection?operation=delete\" >Delete</a> |"
//            + "<a href=\"" + Constants.getServerName() + "Edit/table_selection?operation=insert&value_return_to_page=" + Constants.getServerName() + "Upload/CreateNew&resethistory=true\" >Create</a> |"
//            + "<a href=\"" + Constants.getServerName() + "Search/search\" >Graph search</a> |";
    public static final String SCP_URL_FLAG = "SCP:";
    public final static String DIRECTORY_PATTERN = "" + Constants.SCP_URL_FLAG + "([^/]+):(.*)";
    public final static String UNIQUE_ID_INDEX_NAME_PREFIX = "UNIQUE_IDENTIFICATION";//   "unique_identification";
    private static String DATABASE_NAME_DATA = null;
    public static String DATABASE_USERS = "EGEN_USERS";
    public static final String DATASOURCE_NAME_USERMANAGEMENT_RESOURCE = "egen_userManagement_resource";
    public static final String DATASOURCE_NAME_DATA_VIEW = "egen_dataView_resource";
    public static final String DATASOURCE_NAME_DATA_CREATE = "egen_dataEntry_resource";
    public static final String DATASOURCE_NAME_DATA_UPDATE = "egen_dataUpdate_resource";
    private static DataSource dataSource_data;
    private static DataSource dataSource_users;
//    public static final String USER_TABLE = DATABASE_USERS + ".useraccounts";
    public static final String SMS_TABLE = DATABASE_USERS + ".sms";
    public static final String INSERT_USER_PROCEDURE = DATABASE_USERS + ".INSERT_USER";
    public static final String RESET_USER_PROCEDURE = DATABASE_USERS + ".RESET_PASSWORD";
    public static final String SHA1_PROCEDURE = DATABASE_USERS + ".SHA1";
    public final static String UNIQUE_COLUMNS_FLAG = "UNIQUE_COLMNS";
    public final static String FOREIGN_TABLE_FLAG = "FOREIGN_TABLES";
    public final static String FOREIGN_KEY_COLUMNS_FLAG = "FOREIGN_COLUMNS";
    public final static String FOREIGN_KEY_NAMES_FLAG = "FOREIGN_KES";
    public final static String ALL_COLUMNS_FLAG = "ALL_COLUMNS";
    public final static String SUPER_PARENT_REF_FLAG = "super_parent_ref";
    public final static String PARENT_REF_FLAG = "parent_id_ref";
    private static HashMap<String, HashMap<String, String[]>> key_constraint_map;
    public static final String TAGABLE_TABLE = "2TAGS";
    public static final String TAGSOURCE_TABLE = "_TAGSOURCE";
    public static final String TABLE_TO_USE_FLAG = "TABLETOUSE_";
    public static final String VALUES_RETURN_TO_COLUMN_FLAG = "value_returned_to_col";
    public static final String QUERY_TO_USE_FLAG = "QUERYTOUSE_";
//    public static final String QR_FLAG = "quick_response_code";
    private static HashMap<String, Integer> column2type_map;
    private static ArrayList<String> column2autoincrement_l;
    private static HashMap<String, ArrayList<String>> table2Columns_map;
    private static HashMap<String, Integer> table2levle_map;
    public static ArrayList<Integer> numerical_types;
    private static java.text.SimpleDateFormat sdf_2;
    private static ArrayList<String> table_l;
    private static String server_name;
    private static String server_mode_flag = "server_mode";
    private static String server_base;
    private static String doc_root = null;
    private static String server_root;
    public static final String ATTRIB_LODED_FLAG = "ATRB_LOAD";
    private static String displaying_configs = "";
    public static String server_URL = "";
    public static String wsdl_URL = "";
    public static String LINK_TO_FEATURE_FLAG = "LINK_TO_FEATURE";
    public static String LINK_TO_FEATURE_ID_FLAG = "LINK_TO_FEATURE_ID";
    public static final String BATCH_INSERT_SPLITER = ",_,";
    public static final String MULTISELECT_OP_FLAG = "multysearch";
    public static final String FROM_REPMOTE_PARENT_FOLDER_FLAG = "_PARENT_FOLDER_";
    public final String REPLACEWITH_ID_FALG = "_000_";
    /**
     * The Session variable name for the search result map
     */
    public final static String SERCHRESULTS_SESSION_FLAG = "result_map";
    public final static String PUBLICSERCHRESULTS_SESSION_FLAG = "result_map_public";
    public final static String LAST_SEARCH_TYPE_SESSION_FLAG = "last_search_type";
    public final static String LAST_SEARCH_VALUE_SESSION_FLAG = "last_search_value";
    public final static String LAST_SEARCH_CATEGORY_SESSION_FLAG = "last_search_category";
    public final static String LAST_SEARCH_COUMN_SESSION_FLAG = "last_search_column";
    public final static String FROMDB_RESULTS_SESSION_FLAG = "fromdb_result_map";
    public final static String REMOTE_EXTERACTION_SESSION_FLAG = "REMOTE_result_map";
    public final static String REMOTE_EXTERACTION_SESSION_FLAG2 = "REMOTE_result_map1";
    public static final String GET_ALL_FLAG = "GET_ALL";
    public final static String DETAILS_FLAG = "MOREDETAILS";
    public final static String DELETE_OP_FLAG = "delete";
    public final static String SELECT_OP_FLAG = "select";
    public final static String UPDATE_OP_FLAG = "update";
    public final static String SERCH_OP_FLAG = "search";
    public final static String HIDDEN_ROW_PREFIX = "detalisrow_";
    public final static String CCONTORLER_PLUS_MINUS_PREFIX = "PLUSMINUS_";
    public final static long MEM_WARN = 268435456;
    public final static long MEM_terminate = 33554432;
    public static HashMap<String, String> name2hash;
    public static HashMap<String, String> hash2table;

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=ISO-8859-1");
        PrintWriter out = response.getWriter();
        try {
            /*
             * TODO output your page here. You may use following sample code.
             */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Constant definer</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Constants</h1>");
            out.println("</body>");
            out.println("</html>");

        } finally {
            out.close();
        }
    }

    public static HashMap<String, String> get_name2hash() {
        fillTagDictionary();
        return name2hash;
    }

    public static HashMap<String, String> get_hash2table() {
        fillTagDictionary();
        return hash2table;
    }

    public static void loadSessionVariables(Connection c_con, HttpServletRequest request) {
        try {
//            Connection con = null;
//            try {
//                con = getDatasource_DATA().getConnection();
//            } catch (ServletException ex) {
//                ex.printStackTrace();
//            }

            HttpSession session = request.getSession();
            if (request.getUserPrincipal() != null) {
                session.setAttribute("current_user", request.getUserPrincipal().getName());
                session.setAttribute("person_id", get_person_id(c_con, request.getUserPrincipal().getName()));
                session.setAttribute("user_level", get_userlevel(request.getUserPrincipal().getName()));
                session.setAttribute(ATTRIB_LODED_FLAG, "1");
                session.setAttribute("person_tbl", Constants.get_correct_table_name(c_con, "person"));
                session.setAttribute("report_batch_tbl", Constants.get_correct_table_name(c_con, "report_batch"));
//                session.setAttribute("reporttype_tbl", Constants.get_correct_table_name(dataSource_data, "reporttype"));
                session.setAttribute("report_tbl", Constants.get_correct_table_name(c_con, "report"));
                session.setAttribute("donordetails_tbl", Constants.get_correct_table_name(c_con, "donordetails"));
                session.setAttribute("sampledetails_tbl", Constants.get_correct_table_name(c_con, "sampledetails"));
                session.setAttribute("report2sampledetails_tbl", Constants.get_correct_table_name(c_con, "report2sampledetails"));
                session.setAttribute("filetype_tbl", Constants.get_correct_table_name(c_con, "filetype"));
                session.setAttribute("files_tbl", Constants.get_correct_table_name(c_con, "files"));
                session.setAttribute("files2report_tbl", Constants.get_correct_table_name(c_con, "files2report"));
                session.setAttribute("tablename2feature_tbl", Constants.get_correct_table_name(c_con, "tablename2feature"));
                session.setAttribute("Header", getPrivateMenue(c_con, false));
                session.setAttribute("footer", "<p>__</p>");
            }
//            session.setAttribute("json_last_updated", getJason_last_updated());
            session.setAttribute("Header_public", getPrivateMenue(c_con, true));
            session.setAttribute("displying_config", get_config(c_con));
            session.setAttribute("servernmr", Constants.getServerName(c_con));
            session.setAttribute("server_root", Constants.getServerRoot(c_con));
            session.setAttribute("version", getwEBvERSION(c_con));
            session.setAttribute("database_name_data", Constants.getDB_name(c_con));
            session.setAttribute(server_mode_flag, Constants.getServer_mode(c_con));
            session.setAttribute("footer_public", "<p>__</p>");
//            if (con != null && !con.isClosed()) {
//                con.close();
//            }
        } catch (Exception ex) {
            System.out.println("Error C4a" + ex.getMessage());
        }
    }

    public static String getPrivateMenue(Connection c_con, boolean public_menu) {
        String bg_clor = "#dfdfdf";//"#f6f3f3";
//        String button_bg_color = "#cddcdb";//"fd";
        String mentxt_color = "#000000";
        ArrayList<String> menue_l = new ArrayList<>();
        if (public_menu) {
            menue_l.add("<a href=\"" + Constants.getServerName(c_con) + "\" >Home</a>");
            menue_l.add("<a href=\"" + Constants.getServerName(c_con) + "Public_search\" >Public search</a>");
            menue_l.add("<a href=\"" + Constants.getServerName(c_con) + "visualizer\" >Tag graphs</a>");
            menue_l.add("<a href=\"" + Constants.getServerName(c_con) + "logout\" >Login/Logout</a>");
            menue_l.add("<a href=\"" + getSecureServerRoot(c_con) + "webinterface_tutorial.pdf\">Help</a>");
        } else {
            menue_l.add("<a href=\"" + Constants.getServerName(c_con) + "\" >Home</a>");
            menue_l.add("<a href=\"" + Constants.getServerName(c_con) + "Search/search\" >Search</a>");
            menue_l.add("<a href=\"" + Constants.getServerName(c_con) + "Public_search\" >Public search</a>");
            menue_l.add("<a href=\"" + Constants.getServerName(c_con) + "Edit/table_selection?operation=update\"> Update</a>");
            menue_l.add("<a href=\"" + Constants.getServerName(c_con) + "Edit/table_selection?operation=delete&value_return_to_page=" + Constants.getServerName(c_con) + "Edit/Delete\" >Delete</a>");
            menue_l.add("<a href=\"" + Constants.getServerName(c_con) + "Edit/table_selection?operation=insert&value_return_to_page=" + Constants.getServerName(c_con) + "Upload/CreateNew&resethistory=true\" >Create</a>");
            menue_l.add("<a href=\"" + Constants.getServerName(c_con) + "Upload/logintodirectory\" >Auto Create</a>");
            menue_l.add("<a href=\"" + Constants.getServerName(c_con) + "visualizer\" >Tag graphs</a>");
             menue_l.add("<a href=\"" + Constants.getServerName(c_con) + "Search/GetTags\">Pubmed2Tag</a>");
            menue_l.add("<a href=\"" + Constants.getServerName(c_con) + "Search/duplicate_folders_report.txt\" >Reports</a>");
            menue_l.add("<a href=\"" + Constants.getServerName(c_con) + "logout\" >Login/Logout</a>");
            menue_l.add("<a href=\"" + getSecureServerRoot(c_con) + "webinterface_tutorial.pdf\">Help</a>");
        }

        String menu_box = "<div  align=\"center\" class=\"div_style\" >"
                + "<div class=\"div_style\">"
                + "<div style=\"color:" + bg_clor + ";\">  ";

        StringBuilder menu_build = new StringBuilder();
        menu_build.append(menu_box);
        for (int i = 0; i < menue_l.size(); i++) {
            String c_menubg_color = "#efefff";// "#cddcdb";//"#efeffe";
            String c_menuline_color = "#afafaf";//"#efeffe";
            //style=\"align:center;width:20px;font-family:verdana;padding:0px;border-radius:2px;border-style:double;border:2px solid " + c_menuline_color + ";background-color:" + c_menubg_color + ";opacity:1;\">"
            String c_mn = ""
                    + "<span  style=\"display:inline-block;width:120px;line-height:50px ;vertical-align: middle;"
                    + "background:radial-gradient( #ECF8E0,#CED8F6);border:2px solid " + c_menuline_color + ";font-weight: bold;font-size: 12pt;\"> "
                    //                    + " <span style=\"display:inline-block;width:10; height:30px ;background-color:" + c_menubg_color + ";" + mentxt_color + ";\">  </span> "
                    //                    + " <span style=\"display:inline-block;width:150;height:30px ;background-color:" + c_menubg_color + ";" + mentxt_color + ";\"> " + menue_l.get(i) + " </span>\n"
                    //                    + " <span style=\"display:inline-block;width:10;height:30px ;background-color:" + c_menubg_color + ";" + mentxt_color + ";\">  </span> "
                    + menue_l.get(i)
                    + "</span>"
                    + "\n";

            if (i > 0 && (i < menue_l.size() - 1) && i % 6 == 0) {
                c_mn = c_mn + "</div></div> "
                        + "<div class=\"div_style\">"
                        + "<div style=\"color:" + bg_clor + ";\">";
            }
            menu_build.append(c_mn);
        }
        menu_build.append("</div></div></div><p></p>");
        return menu_build.toString();
    }

    /*
     MethodID=
     */
    public static String getServerName(Connection c_con) {
        if (server_name == null) {
            try {
//                getDatasource_DATA();
//                if (dataSource_data != null) {
                try {
//                        Connection ncon = dataSource_data.getConnection();
                    if (!c_con.isClosed()) {
                        Statement st_1 = c_con.createStatement();
                        String config_tbl = get_correct_table_name(c_con, "config");
                        if (config_tbl != null) {
                            ResultSet r_1 = st_1.executeQuery("select param_value from " + config_tbl + " where name='S_SERVER_NAME'");
                            while (r_1.next()) {
                                server_name = r_1.getString("param_value");
                            }
                        }
                    }
//                        if (!ncon.isClosed()) {
//                            ncon.close();
//                        }
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
//                }

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return server_name;
    }

    public static int getServer_mode(Connection c_con) {
        if (server_mode < -1) {
            try {
//                getDatasource_DATA();
                if (c_con != null) {
                    try {
//                        Connection ncon = dataSource_data.getConnection();
                        if (!c_con.isClosed()) {
                            Statement st_1 = c_con.createStatement();
                            String config_tbl = get_correct_table_name(c_con, "config");
                            if (config_tbl != null) {
                                ResultSet r_1 = st_1.executeQuery("select param_value from " + config_tbl + " where name='MODE'");
                                while (r_1.next()) {
                                    server_mode = r_1.getInt("param_value");
                                }
                            }
                        }
//                        if (!ncon.isClosed()) {
//                            ncon.close();
//                        }
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    }
                }

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return server_mode;
    }

    /*
     MethodID=
     */
    public static String getServerBase(Connection c_con) {
        if (server_base == null) {
            try {
//                getDatasource_DATA();
//                if (dataSource_data != null) {
                try {
//                        Connection ncon = dataSource_data.getConnection();
                    if (!c_con.isClosed()) {
                        Statement st_1 = c_con.createStatement();
                        String config_tbl = get_correct_table_name(c_con, "config");
                        if (config_tbl != null) {
                            ResultSet r_1 = st_1.executeQuery("select param_value from " + config_tbl + " where name='SERVER_BASE'");
                            while (r_1.next()) {
                                server_base = r_1.getString("param_value");
                            }
                        }
                    }
//                        if (!ncon.isClosed()) {
//                            ncon.close();
//                        }
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
//                }

            } catch (Exception ex) {
                ex.printStackTrace();
            }
            if (server_base == null) {
                server_base = "localhost";
            }
        }
        return server_base;
    }

    /*
     MethodID=
     */
    public static String getServerRoot(Connection c_con) {
        if (server_root == null) {
            try {
//                getDatasource_DATA();
                if (c_con != null) {
                    try {
//                        Connection ncon = dataSource_data.getConnection();
                        if (!c_con.isClosed()) {
                            Statement st_1 = c_con.createStatement();
                            ResultSet r_1 = st_1.executeQuery("select param_value from " + get_correct_table_name(c_con, "config") + " where name='SERVER_ROOT'");
                            while (r_1.next()) {
                                server_root = r_1.getString("param_value");
                            }
                        }
//                        if (!ncon.isClosed()) {
//                            ncon.close();
//                        }
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    }
                }

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        return server_root;

    }

    /*
     MethodID=
     */
    public static String getSecureServerRoot(Connection c_con) {
        if (server_root == null) {
            try {
                boolean con_was_null = false;
                if (c_con == null) {
                    con_was_null = true;
                    try {
                        c_con = getDatasource_DATA().getConnection();
                    } catch (ServletException ex) {
                        ex.printStackTrace();
                    }
                }
//                getDatasource_DATA();
                if (c_con != null) {
                    try {
//                        Connection ncon = dataSource_data.getConnection();
                        if (!c_con.isClosed() && get_correct_table_name(c_con, "config") != null) {
                            Statement st_1 = c_con.createStatement();
                            if (st_1 != null) {
                                ResultSet r_1 = st_1.executeQuery("select param_value from " + get_correct_table_name(c_con, "config") + " where name='S_SERVER_ROOT'");
                                while (r_1.next()) {
                                    server_root = r_1.getString("param_value");
                                }
                                r_1.close();
                                st_1.close();
                            }
                        }
//                        if (!ncon.isClosed()) {
//                            ncon.close();
//                        }
                        if (con_was_null) {
                            c_con.close();
                        }
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    }
                }


            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        return server_root;

    }

    /*
     MethodID=
     */
    public static String getwEBvERSION(Connection c_con) {
        if (VERSION == null) {
            try {
//                getDatasource_DATA();
//                if (dataSource_data != null) {
                try {
//                    Connection ncon = dataSource_data.getConnection();
                    if (!c_con.isClosed()) {
                        Statement st_1 = c_con.createStatement();
                        ResultSet r_1 = st_1.executeQuery("select param_value from " + get_correct_table_name(c_con, "config") + " where name='WEB_VERSION'");
                        while (r_1.next()) {
                            VERSION = r_1.getString("param_value");
                        }
                    }
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
//                }

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        return VERSION;

    }

    public static String getDocRoot(Connection c_con) {
        if (doc_root == null) {
            try {
//                getDatasource_DATA();
                if (c_con != null) {
                    try {
//                        Connection ncon = dataSource_data.getConnection();
                        if (!c_con.isClosed()) {
                            Statement st_1 = c_con.createStatement();
                            ResultSet r_1 = st_1.executeQuery("select param_value from " + get_correct_table_name(c_con, "config") + " where name='SERVER_DOCROOT'");
                            while (r_1.next()) {
                                doc_root = r_1.getString("param_value");
                            }
                        }
//                        if (!ncon.isClosed()) {
//                            ncon.close();
//                        }
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    }
                }

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return doc_root;
    }

    private static String get_person_id(Connection c_con, String current_user) {
        String person_id = null;

        if (current_user != null) {
            try {
//                getDatasource_DATA();
                if (c_con != null) {
                    try {
//                        Connection ncon = dataSource_data.getConnection();
                        if (!c_con.isClosed()) {
                            Statement st_1 = c_con.createStatement();
                            String person_tbl = get_correct_table_name(c_con, "person");
                            if (person_tbl != null) {
                                ResultSet r_1 = st_1.executeQuery("select id from " + person_tbl + " where  email='" + current_user + "'");
                                while (r_1.next()) {
                                    person_id = r_1.getString("id");
                                }
                            }
                        }
//                        if (!ncon.isClosed()) {
//                            ncon.close();
//                        }
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    }
                }

            } catch (Exception ex) {
                ex.printStackTrace();
            }

        }

        return person_id;
    }

    private static int get_userlevel(String current_user) {
        int level = 0;
        if (current_user != null) {
            try {
                if (getDatasource_users() != null) {
                    try {
                        Connection ncon = getDatasource_users().getConnection();
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
            } catch (ServletException ex) {
                ex.printStackTrace();
            }

        }

        return level;
    }

    private static String get_config(Connection c_con) {
        try {
//            getDatasource_DATA();
            if (c_con != null) {
                try {
//                    Connection ncon = dataSource_data.getConnection();
                    if (!c_con.isClosed()) {
                        displaying_configs = "";
                        Statement st_1 = c_con.createStatement();
//                        System.out.println("614 " + c_con.getMetaData().getURL());
                        ResultSet r_1 = st_1.executeQuery("select name,description,param_value from " + get_correct_table_name(c_con, "config") + " where display=1");
                        while (r_1.next()) {
                            String c_key = r_1.getString("name");
                            String param = r_1.getString("param_value");
                            if (c_key.equals("S_SERVER_NAME")) {
                                server_URL = param;
                            } else if (c_key.equals("WSDL")) {
                                wsdl_URL = param;
                            }
                            displaying_configs = displaying_configs + "<tr><td>" + r_1.getString("description") + " </td><td>" + "<a href=\"" + param + "\">" + param + "</a></td></tr>";
//                    
                        }
                    }
//                    if (!ncon.isClosed()) {
//                        ncon.close();
//                    }
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return displaying_configs;
    }

    public static String getErrorMessageTable(Connection c_con) {
        String err_tbl = get_correct_table_name(c_con, "errormsgs");
        if (err_tbl == null) {
            err_tbl = "errormsgs";
        }
        return err_tbl;
    }
    /*
     *
     MethodID=C1

     */

    private static Integer getColumnType(String current_tbl_nm, String column_nm, Connection c_con) {
        int type = -1;
        if (column2type_map == null) {
            column2type_map = new HashMap<>();
            column2autoincrement_l = new ArrayList<>(5);
            try {
                DatabaseMetaData metaData = c_con.getMetaData();
                if (!c_con.isClosed()) {
                    ArrayList<String> table_l = getTables(c_con);
                    for (int i = 0; i < table_l.size(); i++) {
                        String c_table = table_l.get(i);
                        String tablenm4metadata = c_table;
                        if (c_table.contains(".")) {
                            tablenm4metadata = c_table.split("\\.")[1];
                        }
                        ResultSet key_result = metaData.getColumns(null, getDB_name(c_con), tablenm4metadata, null);
                        if (!key_result.next()) {
                            key_result = metaData.getColumns(null, getDB_name(c_con).toUpperCase(), tablenm4metadata.toUpperCase(), null);
                        } else {
                            int typename = key_result.getInt("DATA_TYPE");
                            String autoincrmnt = key_result.getString("IS_AUTOINCREMENT");
                            String clmname = key_result.getString("COLUMN_NAME");
                            String key_name = get_correct_table_name(c_con, tablenm4metadata) + "." + clmname;
                            key_name = key_name.toUpperCase();
                            column2type_map.put(key_name, typename);
                            if (autoincrmnt.equalsIgnoreCase("YES")) {
                                column2autoincrement_l.add(key_name);
                            }
                        }
                        while (key_result.next()) {
                            int typename = key_result.getInt("DATA_TYPE");
                            String autoincrmnt = key_result.getString("IS_AUTOINCREMENT");
                            String clmname = key_result.getString("COLUMN_NAME");
                            String key_name = get_correct_table_name(c_con, tablenm4metadata) + "." + clmname;
                            key_name = key_name.toUpperCase();
                            column2type_map.put(key_name, typename);
                            if (autoincrmnt.equalsIgnoreCase("YES")) {
                                column2autoincrement_l.add(key_name);
                            }
                        }
                        key_result.close();
                    }
//                    ncon.close();
                }
            } catch (SQLException e) {
                System.out.println("Error C1a" + e.getMessage());
            }
        }
        if (column_nm != null) {
            String reqest_keynm = current_tbl_nm + "." + column_nm;
            reqest_keynm = reqest_keynm.toUpperCase();
            if (column2type_map.containsKey(reqest_keynm)) {
                type = column2type_map.get(reqest_keynm);
            }
        }
        return type;
    }

    /*
     MethodID=C5
     * 
     */
    private static ArrayList<String> getColumns(String current_tbl_nm, Connection c_con) {
        ArrayList<String> columns_l = new ArrayList<>(2);
        if (table2Columns_map == null) {
            table2Columns_map = new HashMap<>();
            try {
                DatabaseMetaData metaData = c_con.getMetaData();
                if (!c_con.isClosed()) {
                    ArrayList<String> c_table_l = getTables(c_con);
                    for (int i = 0; i < c_table_l.size(); i++) {
                        ArrayList<String> tmp_columns_l = new ArrayList<>(2);
                        String c_table = c_table_l.get(i);
                        String tablenm4metadata = c_table;
                        if (c_table.contains(".")) {
                            tablenm4metadata = c_table.split("\\.")[1];
                        }

                        ResultSet key_result = metaData.getColumns(c_con.getCatalog(), null, tablenm4metadata, null);
                        if (!key_result.next()) {
                            key_result = metaData.getColumns(null, getDB_name(c_con), tablenm4metadata.toUpperCase(), null);
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
//                    ncon.close();
                }
            } catch (SQLException e) {
                System.out.println("Error C1a" + e.getMessage());
            }
        }
        current_tbl_nm = get_correct_table_name(c_con, current_tbl_nm);
        if (current_tbl_nm != null && table2Columns_map.containsKey(current_tbl_nm)) {
            columns_l = table2Columns_map.get(current_tbl_nm);
        } else if (table2Columns_map.containsKey(current_tbl_nm.toUpperCase())) {
            columns_l = table2Columns_map.get(current_tbl_nm.toUpperCase());
        }
        return columns_l;
    }

    /*
     Method=C6
     * 
     */
    public static java.text.SimpleDateFormat getTimeStampFormat() {
        if (sdf_2 == null) {
            String DATE_FORMAT_1 = "yyyy-MM-dd HH:mm:ss"; // 
            sdf_2 = new java.text.SimpleDateFormat(DATE_FORMAT_1);
            sdf_2.setTimeZone(TimeZone.getDefault());
        }
        return sdf_2;
    }

    /*
     Method=C7
     * 
     */
    public static ArrayList<String> getUniqueList(String table, Connection c_con) {
        ArrayList<String> uniquet_col_l_l = new ArrayList<>(1);
        HashMap<String, String[]> const_map = Constants.get_key_contraints(c_con, table);
        String[] uniq_a = const_map.get(Constants.UNIQUE_COLUMNS_FLAG);
        if (uniq_a != null) {
            uniquet_col_l_l.addAll(Arrays.asList(uniq_a));
        }
        return uniquet_col_l_l;
    }

    /*
     Method=C8
     * 
     */
    public static ArrayList<String> getTables(Connection c_con) {
        if (table_l == null || table_l.isEmpty()) {
            try {
                table_l = new ArrayList<>(5);
                String database_nm = getDB_name(c_con);
                if (!c_con.isClosed()) {
                    DatabaseMetaData metaData = c_con.getMetaData();
                    ResultSet r_1 = metaData.getTables(null, null, null, new String[]{"TABLE"});
                    while (r_1.next()) {
                        String table_nm = r_1.getString("TABLE_NAME");
                        String schema_nm = r_1.getString("TABLE_SCHEM");
                        if (schema_nm.equals(database_nm)) {
                            table_l.add(schema_nm + "." + table_nm);
                        }
                    }
                    r_1.close();
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }

        }
        return table_l;
    }

    /*
     Method=c9
     */
    public static int getTable_level(Connection c_con, String table_nm, int caller) {
        int level = 1;
        if (table_nm != null) {
            table_nm = table_nm.split("\\.")[table_nm.split("\\.").length - 1].toUpperCase();
            if (table2levle_map == null) {
                table2levle_map = new HashMap<>(5);
                try {
//                    if (dataSource == null) {
//                        try {
//                            dataSource = getDatasource_DATA();
//                        } catch (ServletException ex) {
//                        }
//                    }
//                    Connection ncon = dataSource.getConnection();
                    if (!c_con.isClosed()) {
                        ResultSet r_1 = c_con.createStatement().executeQuery("select TABLE_NAME,ACCESS_LEVEL from  " + get_correct_table_name(c_con, "TABLENAME2FEATURE"));
                        while (r_1.next()) {
                            String c_tbl = r_1.getString("TABLE_NAME").toUpperCase();
                            level = r_1.getInt("ACCESS_LEVEL");
                            if (!table2levle_map.containsKey(c_tbl) || table2levle_map.get(c_tbl) < level) {
                                table2levle_map.put(c_tbl, level);
                            }
                        }
                        r_1.close();
//                        ncon.close();
                    }
                } catch (SQLException e) {
                    System.out.println("Error C2a" + e.getMessage());
                }
            }
            if (table2levle_map.containsKey(table_nm)) {
                level = table2levle_map.get(table_nm);
            }
        }
        return level;
    }

    /*
     Method=C10
     * 
     */
    public static boolean shoulditbequated(Connection c_con, String current_tbl_nm, String column_nm) {
        boolean result = true;
        int type = getColumnType(current_tbl_nm, column_nm, c_con);
        if (type > 0) {
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

    /*
     Method=C11
     * 
     */
    public static int getType(Connection c_con, String current_tbl_nm, String column_nm) {
        return getColumnType(current_tbl_nm, column_nm, c_con);
    }

    /*
     Method=C12
     * 
     */
    public static int getColumnCount(Connection c_con, String current_tbl_nm) {
        return getColumns(current_tbl_nm, c_con).size();
    }

    /*
     Method=C13
     * 
     */
    public static ArrayList<String> getColumn_names(Connection c_con, String current_tbl_nm) {
        return getColumns(current_tbl_nm, c_con);
    }

    /*
     Method=C14
     * 
     */
    public static ArrayList<String> getAutoIncrementolumn_names(Connection c_con, String current_tbl_nm) {
        if (column2autoincrement_l == null) {
            getColumnType(current_tbl_nm, null, c_con);
        }
        String table = get_correct_table_name(c_con, current_tbl_nm);
        ArrayList<String> return_l = new ArrayList<>(1);
        for (int i = 0; i < column2autoincrement_l.size(); i++) {
            if (column2autoincrement_l.get(i).startsWith(table + ".")) {
                return_l.add(column2autoincrement_l.get(i).replace(table + ".", ""));
            }

        }
        return return_l;
    }

    /*
     Method=C15
     * 
     */
    public static String get_correct_column_name(Connection c_con, String current_tbl_nm, String column) {
        String corr_name = null;
        ArrayList<String> column_L = getColumn_names(c_con, current_tbl_nm);
        for (int i = 0; (i < column_L.size() && (corr_name == null)); i++) {
            if (column_L.get(i).equalsIgnoreCase(column)) {
                corr_name = column_L.get(i);
            }
        }
        return corr_name;
    }

    /*
     Method=C16
     * ToDo:This method is different from the on in webservice responce, make it the same
     */
    public static HashMap<String, String[]> get_key_contraints(Connection c_con, String current_tbl_nm, String target_tbl_nm, String index_name) {
        HashMap<String, String[]> final_returning_map = new HashMap<>(1);
        HashMap<String, String[]> returning_map = new HashMap<>(1);
        HashMap<String, String[]> returning_all_map = get_key_contraints(c_con, current_tbl_nm, 1);
        if (target_tbl_nm != null) {
            if (returning_all_map != null && returning_all_map.containsKey(Constants.FOREIGN_KEY_COLUMNS_FLAG)) {
                ArrayList<String> return_keys = new ArrayList<>(returning_all_map.keySet());
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
            if (returning_all_map != null && returning_all_map.containsKey(Constants.FOREIGN_KEY_NAMES_FLAG)) {
                ArrayList<String> return_keys = new ArrayList<>(returning_map.keySet());
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
        return final_returning_map;
    }

    /*
     MethodID=C17
     * 
     */
    public static HashMap<String, String[]> get_key_contraints(Connection c_con, String current_tbl_nm) {
        return get_key_contraints(c_con, current_tbl_nm, 3);
    }

    /*
     MethodID=C18
     * 
     */
    public static HashMap<String, String[]> get_key_contraints(Connection c_con, String current_tbl_nm, int s) {
        if (key_constraint_map == null) {
            key_constraint_map = new HashMap<>();
        }
//        if (dataSource == null) {
//            try {
//                dataSource = getDatasource_DATA();
//            } catch (ServletException ex) {
//            }
//        }
        HashMap<String, String[]> returning_map = new HashMap<>(1);
//        key_constraint_map.clear();
        if (current_tbl_nm != null && !current_tbl_nm.isEmpty() && !current_tbl_nm.equalsIgnoreCase("null") && !key_constraint_map.containsKey(current_tbl_nm)) {
            try {
                if (c_con != null) {
                    String[] unique_a = get_key_contraints_unique_identification(c_con, current_tbl_nm);
                    try {
//                        Connection ncon = dataSource.getConnection();
                        if (!c_con.isClosed()) {
                            ArrayList<String> foreign_columns = new ArrayList<>(1);
                            ArrayList<String> foreign_key_names_columns = new ArrayList<>(1);
                            ArrayList<String> foreign_tables = new ArrayList<>(1);
                            DatabaseMetaData metaData = c_con.getMetaData();
                            String tablenm4metadata = current_tbl_nm;
                            if (current_tbl_nm.contains(".")) {
                                tablenm4metadata = current_tbl_nm.split("\\.")[1];
                            }
//                            System.out.println("1021 "+ tablenm4metadata);
                            ResultSet key_result = metaData.getImportedKeys(c_con.getCatalog(), null, tablenm4metadata);
                            if (!key_result.next()) {
                                key_result = metaData.getImportedKeys(c_con.getCatalog(), getDB_name(c_con).toUpperCase(), tablenm4metadata.toUpperCase());
                            } else {
                                String[] tmp_a = new String[4];
                                tmp_a[0] = key_result.getString("FKCOLUMN_NAME");//column_name                                 
                                tmp_a[1] = get_correct_table_name(c_con, key_result.getString("PKTABLE_NAME"));//ref_tblm
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
                                tmp_a[1] = get_correct_table_name(c_con, key_result.getString("PKTABLE_NAME"));//ref_tblm
                                tmp_a[2] = key_result.getString("PKCOLUMN_NAME");//ref_tbl_clm_nm 
                                tmp_a[3] = key_result.getString("FK_NAME");//Key name
                                foreign_columns.add(tmp_a[0]);
                                returning_map.put(tmp_a[0], tmp_a);
                                foreign_tables.add(tmp_a[1]);
                                foreign_key_names_columns.add(tmp_a[3]);

                            }
//                            System.out.println("1047 "+foreign_key_names_columns);
                            if (!foreign_columns.isEmpty()) {
                                String[] tmp_a = new String[foreign_columns.size()];
                                for (int i = 0; i < foreign_columns.size(); i++) {
                                    tmp_a[i] = foreign_columns.get(i);
                                }
                                returning_map.put(Constants.FOREIGN_KEY_COLUMNS_FLAG, tmp_a);
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
                                returning_map.put(Constants.UNIQUE_COLUMNS_FLAG, unique_a);
                            }
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
            key_constraint_map.put(current_tbl_nm, returning_map);
            return returning_map;
        } else if (key_constraint_map.containsKey(current_tbl_nm)) {
            return key_constraint_map.get(current_tbl_nm);
        }
        return returning_map;
    }

    /*
     MethodID=C19
     * 
     */
    private static String[] get_key_contraints_unique_identification(Connection c_con, String current_tbl_nm) {
        String[] uniq_to_user_a = null;
        if (current_tbl_nm != null && !current_tbl_nm.isEmpty() && !current_tbl_nm.equalsIgnoreCase("null")) {
            try {
                if (c_con != null) {
                    try {
//                        Connection ncon = dataSource.getConnection();
                        if (!c_con.isClosed()) {
                            DatabaseMetaData metaData = c_con.getMetaData();
                            String tablenm4metadata = current_tbl_nm;
                            if (current_tbl_nm.contains(".")) {
                                tablenm4metadata = current_tbl_nm.split("\\.")[1];
                            }
                            ResultSet key_result = metaData.getIndexInfo(null, getDB_name(c_con), tablenm4metadata, false, false);//.getImportedKeys(ncon.getCatalog(), Constants.DATABASE_NAME_DATA, current_tbl_nm);
                            ArrayList<String> uniqs_list = new ArrayList<>(1);
                            if (!key_result.next()) {
                                key_result = metaData.getIndexInfo(null, getDB_name(c_con).toUpperCase(), tablenm4metadata.toUpperCase(), false, false);//.getImportedKeys(ncon.getCatalog(), Constants.DATABASE_NAME_DATA, current_tbl_nm);
                            } else {
                                String index_name = key_result.getString("INDEX_NAME");
                                index_name = index_name.toUpperCase();
                                if (index_name.startsWith(Constants.UNIQUE_ID_INDEX_NAME_PREFIX)) {
                                    uniqs_list.add(key_result.getString("COLUMN_NAME"));
                                }
                            }
                            while (key_result.next()) {
                                String index_name = key_result.getString("INDEX_NAME");
                                if (index_name != null) {
                                    index_name = index_name.toUpperCase();
                                    if (index_name.startsWith(Constants.UNIQUE_ID_INDEX_NAME_PREFIX)) {
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
//                        ncon.close();
                    } catch (SQLException e) {
                        System.out.println("Error 8767" + e.getMessage());
                    }
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }

        }
        return uniq_to_user_a;
    }

    /*
     MethodID=C20
     * 
     */
    public static ArrayList< String[]> get_reverse_key_contraints(Connection c_con, String current_tbl_nm, ArrayList<String> previous_tbl_nm_list) {
        ArrayList<String[]> returning_list = new ArrayList<>(1);
        if (current_tbl_nm != null && !current_tbl_nm.isEmpty() && !current_tbl_nm.equalsIgnoreCase("null")) {
            try {
                String sql = "select distinct table_name from  " + get_correct_table_name(c_con, "tablename2feature") + " where showinsearch=1";
//                Connection ncon = dataSource.getConnection();
                Statement st_1 = c_con.createStatement();
                ResultSet r_1 = st_1.executeQuery(sql);
                while (r_1.next()) {
                    String c_tbl_nm = get_correct_table_name(c_con, r_1.getString(1));
                    if ((!c_tbl_nm.equals(current_tbl_nm)) && previous_tbl_nm_list != null && previous_tbl_nm_list.contains(current_tbl_nm)) {
                        HashMap<String, String[]> constraint_map = Constants.get_key_contraints(c_con, c_tbl_nm, current_tbl_nm, Constants.SUPER_PARENT_REF_FLAG);
                        if (!constraint_map.isEmpty()) {
                            ArrayList<String> tmp_keys = new ArrayList<String>(constraint_map.keySet());
                            for (int i = 0; i < tmp_keys.size(); i++) {
                                if (constraint_map.get(tmp_keys.get(i)).length >= 3) {
                                    String[] tmp_array = new String[3];
                                    tmp_array[0] = c_tbl_nm;
                                    tmp_array[1] = constraint_map.get(tmp_keys.get(i))[0];
                                    tmp_array[2] = constraint_map.get(tmp_keys.get(i))[2];
                                    returning_list.add(tmp_array);
                                }
                            }
                        }
//                        ArrayList<String> foreifn_clm_nms_l = new ArrayList<String>(1);
//                        if (constraint_map.get(Constants.FOREIGN_KEY_COLUMNS_FLAG) != null) {
//                            foreifn_clm_nms_l = new ArrayList<String>(Arrays.asList(constraint_map.get(Constants.FOREIGN_KEY_COLUMNS_FLAG)));
//                        }
//                        if (constraint_map != null && !constraint_map.isEmpty()) {
//                                System.out.println("761 c_tbl_nm=" + c_tbl_nm + " current_tbl_nm=" + current_tbl_nm + " " + constraint_map.keySet()+" "+foreifn_clm_nms_l);
//                            ArrayList<String> tmp_keys = new ArrayList<String>(constraint_map.keySet());
//                            for (int i = 0; i < tmp_keys.size(); i++) {
//                                if (foreifn_clm_nms_l.contains(tmp_keys.get(i)) && constraint_map.get(tmp_keys.get(i)).length > 1 && constraint_map.get(tmp_keys.get(i))[1].equals(current_tbl_nm)) {
//                            //                                    String[] tmp_array = new String[3];
//                                    tmp_array[0] = c_tbl_nm;
//                                    tmp_array[1] = constraint_map.get(tmp_keys.get(i))[0];
//                                    tmp_array[2] = constraint_map.get(tmp_keys.get(i))[2];
//                                    returning_map_list.add(tmp_array);
//                                }
//                            }
//                        }
                    }
                }
                r_1.close();
//                if (!ncon.isClosed()) {
//                    ncon.close();
//                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }

        }
        return returning_list;
    }

    /*
     MethodID=C21
     * 
     */
    private static DataSource getDatasource_DATA() throws ServletException {
        if (dataSource_data == null) {
            try {
                Context env = (Context) new InitialContext().lookup("java:comp/env");
                dataSource_data = (DataSource) env.lookup(DATASOURCE_NAME_DATA_VIEW);
                if (dataSource_data == null) {
                    throw new ServletException("`" + DATASOURCE_NAME_DATA_VIEW + "' is an unknown DataSource");
                }
            } catch (NamingException e) {
                throw new ServletException(e);
            }
        }
        return dataSource_data;
    }

    /*
     MethodID=C22
     * 
     */
    public static String get_correct_table_name(Connection c_con, String in_name) {
        if (in_name != null) {
//            boolean con_was_null = false;
//            if (c_con == null) {
//                con_was_null = true;
//                try {
//                    c_con = getDatasource_DATA().getConnection();
//                } catch (ServletException ex) {
//                    ex.printStackTrace();
//                } catch (SQLException ex) {
//                    ex.printStackTrace();
//                }
//            }
            ArrayList<String> getTables = getTables(c_con);
//            try {
//                if (con_was_null) {
//                    c_con.close();
//                }
//            } catch (SQLException ex) {
//                ex.printStackTrace();
//            }
            if (getTables.contains(in_name)) {
                return in_name;
            } else {
                in_name = in_name.split("\\.")[in_name.split("\\.").length - 1];
                for (int i = 0; (i < getTables.size()); i++) {
                    String c_tbl = getTables.get(i).toUpperCase();
                    if (c_tbl.equalsIgnoreCase(in_name) || (c_tbl.split("\\.")[c_tbl.split("\\.").length - 1]).equalsIgnoreCase(in_name)) {
                        return getTables.get(i);
                    }
                }
            }
        }
        return null;
    }

    /*
     MethodID=C23
     * 
     */
    public static ArrayList<String> getTagsourceList(Connection c_con) {
        ArrayList<String> tag_source_l = new ArrayList<>();
        ArrayList<String> all_tbl_l = Constants.getTables(c_con);
        for (int i = 0; i < all_tbl_l.size(); i++) {
            if (all_tbl_l.get(i).toUpperCase().endsWith(Constants.TAGSOURCE_TABLE)) {
                tag_source_l.add(all_tbl_l.get(i));
            }
        }
        return tag_source_l;
    }

    /*
     MethodID=C24
     * 
     */
    public static String makeSTATIC_URL(ArrayList<Integer> id_l, String current_tbl_nm) {
        ArrayList<Integer> sorted_l = new ArrayList<>(id_l.size());
        sorted_l.addAll(id_l);
        Collections.sort(sorted_l);
        String idlist = sorted_l.toString().replaceAll("[^0-9,]", "");
        sorted_l.clear();
        String encripted_nm = encript("STATIC_URL_" + current_tbl_nm + "_" + idlist);
        return encripted_nm;
    }

    public static String makeSTATIC_URL(Set<Integer> id_l, String current_tbl_nm) {
        ArrayList<Integer> sorted_l = new ArrayList<>(id_l.size());
        sorted_l.addAll(id_l);
        Collections.sort(sorted_l);
        String idlist = sorted_l.toString().replaceAll("[^0-9,]", "");
        sorted_l.clear();
        String encripted_nm = encript("STATIC_URL_" + current_tbl_nm + "_" + idlist);
        return encripted_nm;
    }

    public static String makeSTATIC_URL(ArrayList<String> id_l, String current_tbl_nm, boolean is_int) {
        ArrayList<String> sorted_l = new ArrayList<>(id_l.size());
        sorted_l.addAll(id_l);
        Collections.sort(sorted_l);
        String idlist = sorted_l.toString().replaceAll("[^0-9,]", "");
        sorted_l.clear();
        String encripted_nm = encript("STATIC_URL_" + current_tbl_nm + "_" + idlist);
        return encripted_nm;
    }

    public static String makeQUERY_URL(String query, String current_tbl_nm) {
        String encripted_nm = encript("QUERY_URL_" + current_tbl_nm + "_" + query);
        return encripted_nm;
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

    public static String toTitleCase(String intext) {
        if (intext != null) {
            StringBuilder out = new StringBuilder();
            try {

                intext = intext.toLowerCase();
                boolean nextCap = true;
                char[] split_a = intext.toCharArray();
                for (int i = 0; i < split_a.length; i++) {
                    char c = split_a[i];
                    if (Character.isSpaceChar(c)) {
                        nextCap = true;
                    } else if (nextCap) {
                        c = Character.toTitleCase(c);
                        nextCap = false;
                    }
                    out.append(c);
                }
            } catch (Exception ex) {
            }
            return out.toString();
        } else {
            return null;
        }

    }

    /*
     MethodID=C25
     * 
     */
    public static String getJason_last_updated(Connection c_con) {
        String last_updated = "Unknown";
        try {
//            getDatasource_DATA();
            if (c_con != null) {
                try {
//                    Connection ncon = dataSource_data.getConnection();
                    if (!c_con.isClosed()) {
                        Statement st_1 = c_con.createStatement();
                        String config_tbl = get_correct_table_name(c_con, "config");
                        if (config_tbl != null) {
                            ResultSet r_1 = st_1.executeQuery("select param_value from " + config_tbl + " where name='JSON_LAST_UPDATED'");
                            while (r_1.next()) {
                                last_updated = r_1.getString("param_value");
                            }
                            r_1.close();
                        }
                    }
//                    if (!ncon.isClosed()) {
//                        ncon.close();
//                    }
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return last_updated;
    }

    /*
     MethodID=C26
     */
    public static int setConfig(Connection c_conn, HashMap<String, String> instruct_map) {
        int result = 0;
        try {
            if (c_conn != null && !c_conn.isClosed()) {
                PreparedStatement p_1 = c_conn.prepareStatement("update " + get_correct_table_name(c_conn, "config") + " set param_value=? where name=?");
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
     MethodID=C27
     * 
     */
    public static String get_last_memorised(Connection c_con) {
        String last_updated = " Unknown";
        try {
//            getDatasource_DATA();
            if (c_con != null) {
                try {
//                    Connection ncon = dataSource_data.getConnection();
                    if (!c_con.isClosed()) {
                        Statement st_1 = c_con.createStatement();
                        String config_tbl = get_correct_table_name(c_con, "config");
                        if (config_tbl != null) {
                            ResultSet r_1 = st_1.executeQuery("select param_value from " + config_tbl + " where name='LAST_MEMORIZED'");
                            while (r_1.next()) {
                                last_updated = r_1.getString("param_value");
                            }
                            r_1.close();
                        }

                    }
//                    if (!ncon.isClosed()) {
//                        ncon.close();
//                    }
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return last_updated;
    }

    /*
     MethodID=C28
     * 
     */
    public static boolean isDbUpdated(Connection c_con) {
        boolean isupdated = false;
        try {
            getDatasource_DATA();
            if (dataSource_data != null) {
                try {
                    Connection ncon = dataSource_data.getConnection();
                    if (!ncon.isClosed()) {
                        Statement st_1 = ncon.createStatement();
                        String config_tbl = get_correct_table_name(c_con, "config");
                        if (config_tbl != null) {
                            ResultSet r_1 = st_1.executeQuery("select param_value from " + config_tbl + " where name='MEMORIZING_REQUESTED'");
                            while (r_1.next()) {
                                if (r_1.getString("param_value").equals("1")) {
                                    isupdated = true;
                                }
                            }
                            r_1.close();
                        }
                    }
                    if (!ncon.isClosed()) {
                        ncon.close();
                    }
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        } catch (ServletException ex) {
            ex.printStackTrace();
        }

        return isupdated;
    }

    /*
     MethodID=C32
     * 
     */
    private static void fillTagDictionary() {
        if (name2hash == null || hash2table == null) {
            name2hash = new HashMap<>();
            hash2table = new HashMap<>();
            try {
                getDatasource_DATA();
                if (dataSource_data != null) {
                    try {
                        Connection ncon = dataSource_data.getConnection();
                        if (!ncon.isClosed()) {
                            Statement st_1 = ncon.createStatement();
                            String tagDic_tbl = get_correct_table_name(ncon, "alltags");
                            if (tagDic_tbl != null) {
                                ResultSet r_1 = st_1.executeQuery("SELECT hash,name,TABLE_NM FROM " + tagDic_tbl);
                                while (r_1.next()) {
                                    name2hash.put(r_1.getString(2).toUpperCase(), r_1.getString(1));
                                    hash2table.put(r_1.getString(1), r_1.getString(3));
                                }
                                r_1.close();
                            }
                        }
                        if (!ncon.isClosed()) {
                            ncon.close();
                        }
                    } catch (SQLException ex) {
                        System.out.println("Error C32a" + ex.getMessage());
                    }
                }
            } catch (ServletException ex) {
                System.out.println("Error C32b" + ex.getMessage());
            }
        }
    }
    /*
     MethodID=C29
     * 
     */

    private static DataSource getDatasource_users() throws ServletException {
        if (dataSource_users == null) {
            try {
                Context env = (Context) new InitialContext().lookup("java:comp/env");
                dataSource_users = (DataSource) env.lookup(Constants.DATASOURCE_NAME_USERMANAGEMENT_RESOURCE);
                if (dataSource_users == null) {
                    throw new ServletException("`" + Constants.DATASOURCE_NAME_USERMANAGEMENT_RESOURCE + "' is an unknown DataSource");
                }
            } catch (NamingException e) {
                throw new ServletException(e);
            }
        }
        return dataSource_users;
    }

    /*
     MethodID=C30
     * 
     */
    public static String getDB_name(Connection c_con) {
        if (DATABASE_NAME_DATA == null) {
            try {
                if (c_con != null && !c_con.isClosed()) {
                    String c_url = c_con.getMetaData().getURL();
                    DATABASE_NAME_DATA = c_url.split("/")[c_url.split("/").length - 1];
                    DATABASE_NAME_DATA = DATABASE_NAME_DATA.split(":")[DATABASE_NAME_DATA.split(":").length - 1];
                }
            } catch (SQLException ex) {
            }
            if (DATABASE_NAME_DATA == null) {
                DATABASE_NAME_DATA = "EGEN_DATAENTRY";
            }
        }
        return DATABASE_NAME_DATA;
    }

    public static HashMap<String, String> getchar2symbolMap() {
        if (chartosymb_map == null || chartosymb_map.isEmpty()) {
            chartosymb_map = new HashMap<>();
            chartosymb_map.put("\"", "&#34;");
            chartosymb_map.put("'", "&#39;");
            chartosymb_map.put("&", "&#38;");
            chartosymb_map.put("<", "&#60;");
            chartosymb_map.put(">", "&#62;");
            chartosymb_map.put("¡", "&#161;");
            chartosymb_map.put("¢", "&#162;");
            chartosymb_map.put("£", "&#163;");
            chartosymb_map.put("¤", "&#164;");
            chartosymb_map.put("¥", "&#165;");
            chartosymb_map.put("¦", "&#166;");
            chartosymb_map.put("§", "&#167;");
            chartosymb_map.put("¨", "&#168;");
            chartosymb_map.put("©", "&#169;");
            chartosymb_map.put("ª", "&#170;");
            chartosymb_map.put("«", "&#171;");
            chartosymb_map.put("¬", "&#172;");
            chartosymb_map.put("­­", "&#173;");
            chartosymb_map.put("®", "&#174;");
            chartosymb_map.put("¯", "&#175;");
            chartosymb_map.put("°", "&#176;");
            chartosymb_map.put("±", "&#177;");
            chartosymb_map.put("²", "&#178;");
            chartosymb_map.put("³", "&#179;");
            chartosymb_map.put("´", "&#180;");
            chartosymb_map.put("µ", "&#181;");
            chartosymb_map.put("¶", "&#182;");
            chartosymb_map.put("·", "&#183;");
            chartosymb_map.put("¸", "&#184;");
            chartosymb_map.put("¹", "&#185;");
            chartosymb_map.put("º", "&#186;");
            chartosymb_map.put("»", "&#187;");
            chartosymb_map.put("¼", "&#188;");
            chartosymb_map.put("½", "&#189;");
            chartosymb_map.put("¾", "&#190;");
            chartosymb_map.put("¿", "&#191;");
            chartosymb_map.put("×", "&#215;");
            chartosymb_map.put("÷", "&#247;");
            chartosymb_map.put("À", "&#192;");
            chartosymb_map.put("Á", "&#193;");
            chartosymb_map.put("Â", "&#194;");
            chartosymb_map.put("Ã", "&#195;");
            chartosymb_map.put("Ä", "&#196;");
            chartosymb_map.put("Å", "&#197;");
            chartosymb_map.put("Æ", "&#198;");
            chartosymb_map.put("Ç", "&#199;");
            chartosymb_map.put("È", "&#200;");
            chartosymb_map.put("É", "&#201;");
            chartosymb_map.put("Ê", "&#202;");
            chartosymb_map.put("Ë", "&#203;");
            chartosymb_map.put("Ì", "&#204;");
            chartosymb_map.put("Í", "&#205;");
            chartosymb_map.put("Î", "&#206;");
            chartosymb_map.put("Ï", "&#207;");
            chartosymb_map.put("Ð", "&#208;");
            chartosymb_map.put("Ñ", "&#209;");
            chartosymb_map.put("Ò", "&#210;");
            chartosymb_map.put("Ó", "&#211;");
            chartosymb_map.put("Ô", "&#212;");
            chartosymb_map.put("Õ", "&#213;");
            chartosymb_map.put("Ö", "&#214;");
            chartosymb_map.put("Ø", "&#216;");
            chartosymb_map.put("Ù", "&#217;");
            chartosymb_map.put("Ú", "&#218;");
            chartosymb_map.put("Û", "&#219;");
            chartosymb_map.put("Ü", "&#220;");
            chartosymb_map.put("Ý", "&#221;");
            chartosymb_map.put("Þ", "&#222;");
            chartosymb_map.put("ß", "&#223;");
            chartosymb_map.put("à", "&#224;");
            chartosymb_map.put("á", "&#225;");
            chartosymb_map.put("â", "&#226;");
            chartosymb_map.put("ã", "&#227;");
            chartosymb_map.put("ä", "&#228;");
            chartosymb_map.put("å", "&#229;");
            chartosymb_map.put("æ", "&#230;");
            chartosymb_map.put("ç", "&#231;");
            chartosymb_map.put("è", "&#232;");
            chartosymb_map.put("é", "&#233;");
            chartosymb_map.put("ê", "&#234;");
            chartosymb_map.put("ë", "&#235;");
            chartosymb_map.put("ì", "&#236;");
            chartosymb_map.put("í", "&#237;");
            chartosymb_map.put("î", "&#238;");
            chartosymb_map.put("ï", "&#239;");
            chartosymb_map.put("ð", "&#240;");
            chartosymb_map.put("ñ", "&#241;");
            chartosymb_map.put("ò", "&#242;");
            chartosymb_map.put("ó", "&#243;");
            chartosymb_map.put("ô", "&#244;");
            chartosymb_map.put("õ", "&#245;");
            chartosymb_map.put("ö", "&#246;");
            chartosymb_map.put("ø", "&#248;");
            chartosymb_map.put("ù", "&#249;");
            chartosymb_map.put("ú", "&#250;");
            chartosymb_map.put("û", "&#251;");
            chartosymb_map.put("ü", "&#252;");
            chartosymb_map.put("ý", "&#253;");
            chartosymb_map.put("þ", "&#254;");
            chartosymb_map.put("ÿ", "&#255;");
            chartosymb_map.put("\n", " ");
            chartosymb_map.put("ﬁ", "fi");
            chartosymb_map.put("ﬂ", "fl");
            chartosymb_map.put("—", "-");
            chartosymb_map.put("‘", "&#39;");
            chartosymb_map.put("’", "&#39;");
            chartosymb_map.put("\\?", ".");
            chartosymb_map.put("\\%", "&#37;");
//            chartosymb_map.put("=", "&#61;");
            chartosymb_map.put("–", "-");
//            chartosymb_map.put("–", "-");
        }
        return chartosymb_map;
    }

    public static HashMap<String, String> getForeingKeyMap() {
        if (forign_key_to_table_m == null) {
            forign_key_to_table_m = new HashMap<String, String>();
            forign_key_to_table_m.put("reporter", "person");
            forign_key_to_table_m.put("parentreport", "report");
            forign_key_to_table_m.put("childreport", "report");
            forign_key_to_table_m.put("parentfile", "files");
            forign_key_to_table_m.put("childfile", "files");
            forign_key_to_table_m.put("genomicreferenceReport", "report");
            forign_key_to_table_m.put("genomicReferenceReport", "report");
            forign_key_to_table_m.put("entryinfo", "entryinfo");;
            forign_key_to_table_m.put("files", "files");
            forign_key_to_table_m.put("report_batch", "report_batch");
//            forign_key_to_table_m.put("reporttype", "reportType");
//            forign_key_to_table_m.put("reportType", "reportType");
            forign_key_to_table_m.put("report_squence_modifications", "report_squence_modifications");
            forign_key_to_table_m.put("suplimetaryData", "suplimetaryData");
            forign_key_to_table_m.put("xref", "xref");
            forign_key_to_table_m.put("databaseinf", "databaseinf");
            forign_key_to_table_m.put("dbSNP_RS_NV", "dbSNP_RS_NV");
            forign_key_to_table_m.put("sequenceinfo", "sequenceinfo");
            forign_key_to_table_m.put("dbinfo", "dbinfo");
            forign_key_to_table_m.put("cel_location", "cel_location");
            forign_key_to_table_m.put("molecule", "molecule");
            forign_key_to_table_m.put("processed", "processed");
            forign_key_to_table_m.put("miniml", "miniml");
            forign_key_to_table_m.put("platform", "platform");
            forign_key_to_table_m.put("distributionType", "distributionType");
            forign_key_to_table_m.put("sourceDb", "sourceDb");
            forign_key_to_table_m.put("contributor", "contributor");
            forign_key_to_table_m.put("series", "series");
            forign_key_to_table_m.put("manufacturer", "manufacturer");
            forign_key_to_table_m.put("chipDescription", "chipDescription");
            forign_key_to_table_m.put("channel", "channel");
            forign_key_to_table_m.put("status", "status");
            forign_key_to_table_m.put("Organizationinfo", "Organizationinfo");
            forign_key_to_table_m.put("technologyType", "technologyType");
            forign_key_to_table_m.put("organism", "organism");
            forign_key_to_table_m.put("protocol", "protocol");
            forign_key_to_table_m.put("characteristics", "characteristics");
            forign_key_to_table_m.put("databaseinfo", "databaseinfo");
            forign_key_to_table_m.put("dataprocessing", "dataprocessing");
            forign_key_to_table_m.put("fastaq", "fastaq");
            forign_key_to_table_m.put("fileReference", "fileReference");
            forign_key_to_table_m.put("instrument", "instrument");
            forign_key_to_table_m.put("int_db", "int_db");
            forign_key_to_table_m.put("libraryInfo", "libraryInfo");
            forign_key_to_table_m.put("cel_entry", "cel_entry");
            forign_key_to_table_m.put("cel_mesures", "cel_mesures");
            forign_key_to_table_m.put("tagseq", "tagseq");
            forign_key_to_table_m.put("batch", "batch");
            forign_key_to_table_m.put("experiment", "experiment");
            forign_key_to_table_m.put("entryDate", "entryDate");
            forign_key_to_table_m.put("technology", "");
            forign_key_to_table_m.put("file_hierarchy", "file_hierarchy");
            forign_key_to_table_m.put("report_hierarchy", "report_hierarchy");
            forign_key_to_table_m.put("report", "report");
            forign_key_to_table_m.put("person", "person");
            forign_key_to_table_m.put("address", "address");
            forign_key_to_table_m.put("donordetails", "donorDetails");
            forign_key_to_table_m.put("donorDetails", "donorDetails");

        }
        return forign_key_to_table_m;
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP
     * <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP
     * <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>
}
