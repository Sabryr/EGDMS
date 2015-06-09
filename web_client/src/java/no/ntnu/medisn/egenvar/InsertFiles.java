package no.ntnu.medisn.egenvar;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.regex.Pattern;
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
 *
 * @author sabryr
 */
public class InsertFiles extends HttpServlet {

    private DataSource dataSource;
    private int rowsPerPage = 100;
    private ArrayList<String> atributes_to_clear_l;
//    private final String UNIQUE_TO_USER_COLUMNS_FLAG = "UNIQUE_COLMNS";
//    private final String FOREIGN_TABLE_FLAG = "FOREIGN_TABLES";
    private final static String CONSTRAINT_REFERENCE_PATTERN = ".*CONSTRAINT.*FOREIGN KEY[^\\(]+\\([^A-Za-z0-9_\\s]{1}([A-Za-z0-9_\\s]+).*REFERENCES[\\s]*[^A-Za-z0-9_\\s]{1}([A-Za-z0-9_\\s]+)[^A-Za-z0-9_\\s]{1}.*\\([^A-Za-z0-9_\\s]{1}(.*)[^A-Za-z0-9_\\s]{1}\\).*";
    private final static String CONSTRAINT_UNIQNESS_PATTERN = ".*UNIQUE KEY.*unique_identification[^\\(]+\\((.*)\\).*";
    private Pattern ptn_1;
    private Pattern ptn_2;
    private HashMap<String, HashMap<String, String[]>> key_constraint_map;
    private HashMap<String, PreparedStatement> table2prep_map;
    private String person_tbl = "person";
    private String report_batch_tbl = "report_batch";
//    private String reporttype_tbl = "reporttype";
    private String report_tbl = "report";
    private String donordetails_tbl = "donordetails";
    private String sampledetails_tbl = "sampledetails";
//    private String report2sampledetails_tbl = "report2sampledetails";
    private String files2path_tbl = "files2path";
    private String files_tbl = "files";
    private String files2report_tbl = "files2report";

    private void myInit() {
        if (atributes_to_clear_l == null) {
            atributes_to_clear_l = new ArrayList<String>();
            atributes_to_clear_l.add("resultsDisplay_column_nm_l");
            atributes_to_clear_l.add("resultsDisplay_data_l");
        }
    }

    /**
     * Processes requests for both HTTP
     * <code>GET</code> and
     * <code>POST</code> methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        PrintWriter out = response.getWriter();
        Connection c_con = null;
        try {
            c_con = getDatasource().getConnection();
            person_tbl = Constants.get_correct_table_name(c_con, "person");
            report_batch_tbl = Constants.get_correct_table_name(c_con, "report_batch");
//        reporttype_tbl = Constants.get_correct_table_name(dataSource, "reporttype");
            report_tbl = Constants.get_correct_table_name(c_con, "report");
            donordetails_tbl = Constants.get_correct_table_name(c_con, "donordetails");
            sampledetails_tbl = Constants.get_correct_table_name(c_con, "sampledetails");
//            report2sampledetails_tbl = Constants.get_correct_table_name(c_con, "report2sampledetails");
            files2path_tbl = Constants.get_correct_table_name(c_con, "files2path");
            files_tbl = Constants.get_correct_table_name(c_con, "files");
            files2report_tbl = Constants.get_correct_table_name(c_con, "files2report");

            response.setContentType("text/html;charset=ISO-8859-1");
            String cmin = request.getParameter("cminid");
            String cmax = request.getParameter("cmaxid");
            HttpSession session = request.getSession(true);
            String current_user = "NA";
            if (request.getUserPrincipal() != null) {
                String current_user_val = request.getUserPrincipal().getName();
                if (current_user != null) {
                    current_user = current_user_val;
                }
            }
            boolean reset = true;
            if (request.getParameter("reload") != null) {
                if (request.getParameter("reload").equalsIgnoreCase("false")) {
                    reset = false;
                }
            }
            if (reset) {
                myInit();
            }


            Enumeration sess_var_nms = session.getAttributeNames();
            while (sess_var_nms.hasMoreElements()) {
                String tmp_nm = (String) sess_var_nms.nextElement();
                if (atributes_to_clear_l.contains(tmp_nm)) {
                    session.removeAttribute(tmp_nm);
                }
            }
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=ISO-8859-1\">");
            out.println("<link HREF=\"" + Constants.getServerName(c_con) + "resources/egenStyler.css\"  REL=\"stylesheet\" TYPE=\"text/css\" >");
            out.println("<title>Servlet InsertFiles</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<table  class=\"table_text\" >\n"
                    + " <thead>\n"
                    + "     <tr>\n"
                    + "         <th>\n"
                    + "                <img  height=\"150\" width=\"1000\" src=\"../Banner.gif\"/>\n"
                    + "         </th>\n"
                    + "     </tr>\n"
                    + " </thead>\n"
                    + "</table>\n");
            out.println("<p>" + session.getAttribute("Header") + "</p>");
            out.println("<h1>Add files</h1>");
//           
            if (getfileList(request, session)) {
                int cMinid = 0;
                int cmaxid = 0;
                if (cmin != null && cmin.matches("[0-9]+")) {
                    cMinid = new Integer(cmin);
                }
                if (cmax != null && cmax.matches("[0-9]+")) {
                    cmaxid = new Integer(cmax);
                }
                if (cmaxid < 1) {
                    cmaxid = rowsPerPage;
                }
                if (processAndCreate(session, out, current_user)) {
                    printResults(c_con, out, cMinid, cmaxid, session);
                } else {
                    out.println("<error>Error IFp1: unexpected error</error>");
                }

            } else {
                out.println("<error>No files selected</error>");
            }
            out.println("</body>");
            out.println("</html>");
            close(c_con, null, null);
        } catch (SQLException ex) {
        } finally {
            out.close();
            close(c_con, null, null);
        }
    }

    private boolean getfileList(HttpServletRequest request, HttpSession session) {
        boolean found = false;
        HashMap<String, ArrayList<String>> vale_map = new HashMap<>();
        HashMap<String, String[]> parameter_map = new HashMap<>();
        parameter_map.putAll(request.getParameterMap());
        ArrayList<String> param_names_l = new ArrayList(parameter_map.keySet());
        for (int i = 0; i < param_names_l.size(); i++) {
            if (param_names_l.get(i).startsWith(DirectoryLister2.CHEKBX_PREFX)) {
//                String description_name = DirectoryLister.DESCRIPT_PREFX + param_names_l.get(i).replace(DirectoryLister.CHEKBX_PREFX, "").trim();
                String c_chk_bx_val = request.getParameter(param_names_l.get(i));
                if (c_chk_bx_val != null && !c_chk_bx_val.isEmpty()) {
                    String[] val_a = c_chk_bx_val.split("\\|");
                    for (int j = 0; j < val_a.length; j++) {
                        String c_val = val_a[j].trim();
                        String[] c_param_a = c_val.split("=");
                        if (c_param_a.length == 2) {
                            if (vale_map.containsKey(c_param_a[0])) {
                                vale_map.get(c_param_a[0]).add(c_param_a[1]);
                            } else {
                                ArrayList<String> tmp_l = new ArrayList<String>(7);
                                tmp_l.add(c_param_a[1]);
                                vale_map.put(c_param_a[0], tmp_l);
                            }
                        }

//                        String c_descript_val = request.getParameter(description_name);
//                        if (c_descript_val != null && !c_descript_val.isEmpty()) {
//                            if (vale_map.containsKey(DirectoryLister.DESCRIPT_PREFX)) {
//                                vale_map.get(DirectoryLister.DESCRIPT_PREFX).add(c_descript_val);
//                            } else {
//                                ArrayList<String> tmp_l = new ArrayList<String>(7);
//                                tmp_l.add(c_descript_val);
//                                vale_map.put(DirectoryLister.DESCRIPT_PREFX, tmp_l);
//                            }
//                        }

                    }
                }

            }
        }
        if (!vale_map.isEmpty()) {
            found = true;
            ArrayList<String> parm_key_l = new ArrayList<>(vale_map.keySet());
            ArrayList<ArrayList<String>> parm_val_l = new ArrayList<>(10);
            int size = vale_map.get(parm_key_l.get(0)).size();
            for (int i = 0; (i < size); i++) { //+ 2
                ArrayList<String> tmp = new ArrayList<>(7);
                parm_val_l.add(tmp);
            }

            for (int i = 0; i < parm_key_l.size(); i++) {
                for (int j = 0; j < size; j++) {
                    parm_val_l.get(j).add(vale_map.get(parm_key_l.get(i)).get(j));
                }
            }
            session.setAttribute("resultsDisplay_column_nm_l", parm_key_l);
            session.setAttribute("resultsDisplay_data_l", parm_val_l);
        }
        return found;
    }
    /*
     * Step 1 : get person id, if person if missing terminate Step 2 : create
     * the report batch get get the id Step 3 : get report type id. Step 4 :
     * Crette report and get id Step 5 : Create donor details and get id Step 6
     * : Crate sample details and get id Step 7 : create report2sample step 8 :
     * create filetype description and get id Step 9 create file and get id step
     * 10 : create file2report.
     *
     */

    private boolean processAndCreate(HttpSession session, PrintWriter out, String current_user) {
        try {
            if (session.getAttribute("resultsDisplay_column_nm_l") != null) {
                ArrayList<String> resultsDisplay_column_nm_l = (ArrayList<String>) session.getAttribute("resultsDisplay_column_nm_l");
                if (resultsDisplay_column_nm_l != null) {
                    if (session.getAttribute("resultsDisplay_data_l") != null) {
                        try {
                            boolean allok = true;
                            Connection ncon = null;
                            Statement st_1 = null;
                            try {
                                getDatasource();
                                ncon = dataSource.getConnection();
                                st_1 = createroleBakk_statement(ncon, session);
                            } catch (Exception ex) {
                                ex.printStackTrace();
                            }
                            ArrayList<ArrayList<String>> resultsDisplay_data_l = (ArrayList<ArrayList<String>>) session.getAttribute("resultsDisplay_data_l");
                            for (int j = 0; (j < resultsDisplay_data_l.size() && allok); j++) {
                                try {
                                    if (st_1 != null && !st_1.isClosed()) {
                                        ArrayList<String> c_l = resultsDisplay_data_l.get(j);
                                        if (c_l.size() == resultsDisplay_column_nm_l.size()) {
                                            String batch_name = "NA";
                                            String reporter_email = current_user;
//                                            String reporttype = "NA";
                                            String report_name = "NA";
                                            java.util.Date date = new java.util.Date();
                                            String entrydate = (date.getTime() / 1000) + "";
                                            String donordetails_name = "NA";
                                            String sampledetails_name = "NA";
//                                            String filetype_name = "NA";
                                            String file_nm = "NA";
                                            String file_ownergroup = "NA";
                                            String file_lastmodified = "NA";
                                            String full_file_path = "NA";
                                            String file_checksum = "NA";
                                            String file_filesize = "NA";
                                            String servername = "NA";
//                                            String file_description = "NA";
                                            for (int i = 0; i < resultsDisplay_column_nm_l.size(); i++) {
                                                String c_col = resultsDisplay_column_nm_l.get(i);
                                                if (c_col.equalsIgnoreCase("batch_name")) {
                                                    batch_name = c_l.get(i);
                                                } else if (c_col.equalsIgnoreCase("Report_name")) {
                                                    report_name = c_l.get(i);
                                                } //                                                else if (c_col.equalsIgnoreCase("FileType")) {
                                                //                                                    filetype_name = c_l.get(i);
                                                //                                                } 
                                                else if (c_col.equalsIgnoreCase("Name")) {
                                                    file_nm = c_l.get(i);
                                                } else if (c_col.equalsIgnoreCase("Group")) {
                                                    file_ownergroup = c_l.get(i);
                                                } else if (c_col.equalsIgnoreCase("Timestamp")) {
                                                    file_lastmodified = c_l.get(i);
                                                } else if (c_col.equalsIgnoreCase("Filepath")) {
                                                    full_file_path = c_l.get(i);
                                                } else if (c_col.equalsIgnoreCase("Checksum")) {
                                                    file_checksum = c_l.get(i);
                                                } else if (c_col.equalsIgnoreCase("Size")) {
                                                    file_filesize = c_l.get(i);
                                                } else if (c_col.equalsIgnoreCase("server_name")) {
                                                    servername = c_l.get(i);
                                                }
//                                                else if (c_col.equalsIgnoreCase(DirectoryLister.DESCRIPT_PREFX)) {
//                                                    file_description = c_l.get(i);
//                                                }
                                            }
                                            allok = create(session, st_1, ncon, batch_name, reporter_email, report_name, donordetails_name, sampledetails_name,
                                                    file_nm, file_ownergroup, file_lastmodified,
                                                    full_file_path, file_checksum, file_filesize, entrydate, servername);
                                        } else {
                                            //check why is this ?
                                        }
                                    } else {
                                        System.out.println("Failed to create statement");
                                    }

                                } catch (SQLException ex) {
                                    ex.printStackTrace();
                                }
                            }

                            if (allok && ncon != null && !ncon.isClosed()) {
                                out.println("<p>Started committing updates....</p>");
                                HashMap<String, String> config_map = new HashMap<>();
                                config_map.put("MEMORIZING_REQUESTED", "1");
                                if (Constants.setConfig(ncon, config_map) < 0) {
                                }
                                ncon.commit();
                                out.println("<p>All updates successful</p>");
                                return true;
                            } else {
                                if (session.getAttribute("save1") != null) {
                                    Savepoint save1 = (Savepoint) session.getAttribute("save1");
                                    out.println("<error>There was an error, rolback started....</error>");
                                    ncon.rollback(save1);
                                    out.println("<error>Roleback ended. No changes were made to the database</error>");
                                    return false;
                                }
                            }
                        } catch (SQLException ex) {
                            ex.printStackTrace();
                        }

                    }
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
        return false;
    }

    private boolean create(HttpSession session, Statement stm_1, Connection ncon, String batch_name, String reporter_email, String report_name, String donordetails_name, String sampledetails_name,
            String file_nm, String file_ownergroup, String file_lastmodified,
            String full_file_path, String file_checksum, String file_filesize, String entrydate, String servername) {
        HashMap<String, HashMap<String, String>> value_map = new HashMap<String, HashMap<String, String>>();


        HashMap<String, String> person_map = new HashMap<>();
        person_map.put("email", reporter_email);
        value_map.put(person_tbl, person_map);

        HashMap<String, String> report_batch_map = new HashMap<>();
        report_batch_map.put("name", Constants.toTitleCase(batch_name));
        value_map.put(report_batch_tbl, report_batch_map);

//        HashMap<String, String> reporttype_map = new HashMap<String, String>();
//        reporttype_map.put("name", reporttype);
//        value_map.put(reporttype_tbl, reporttype_map);

        HashMap<String, String> report_map = new HashMap<>();
        report_map.put("name", Constants.toTitleCase(report_name));
        report_map.put("entrydate", entrydate);
        value_map.put(report_tbl, report_map);

        HashMap<String, String> donordetails_map = new HashMap<>();
        donordetails_map.put("name", donordetails_name);
        value_map.put(donordetails_tbl, donordetails_map);

        HashMap<String, String> sampledetails_map = new HashMap<>();
        sampledetails_map.put("name", sampledetails_name);
        value_map.put(sampledetails_tbl, sampledetails_map);

//        HashMap<String, String> report2sampledetails_map = new HashMap<String, String>();
//        value_map.put(report2sampledetails_tbl, report2sampledetails_map);

//        HashMap<String, String> filetype_description_map = new HashMap<String, String>();
//        filetype_description_map.put("name", filetype_name);
//        value_map.put(filetype_tbl, filetype_description_map);

        HashMap<String, String> file_map = new HashMap<String, String>();
        file_map.put("name", file_nm);
        file_map.put("checksum", file_checksum);
        file_map.put("filesize", file_filesize);
        value_map.put(files_tbl, file_map);

        HashMap<String, String> file2path_map = new HashMap<String, String>();
        file2path_map.put("ownergroup", file_ownergroup);
        file2path_map.put("lastmodified", file_lastmodified);
        file2path_map.put("filepath", full_file_path);
        file2path_map.put("location", servername);

        value_map.put(files2path_tbl, file2path_map);

        HashMap<String, String> file2report_map = new HashMap<String, String>();
        value_map.put(files2report_tbl, file2report_map);
        int report_id = -1;
        int person_id = getOrAdd(session, true, stm_1, ncon, person_tbl, value_map.get(person_tbl));
        if (person_id >= 0) {
            report_map.put("reporter_id", person_id + "");
            int report_batch_id = getOrAdd(session, false, stm_1, ncon, report_batch_tbl, value_map.get(report_batch_tbl));
            if (report_batch_id >= 0) {
                report_map.put("report_batch_id", report_batch_id + "");
//                int reporttype_id = getOrAdd(session, false, stm_1, ncon, reporttype_tbl, value_map.get(reporttype_tbl));
//                if (reporttype_id >= 0) {
//                    report_map.put("reporttype_id", reporttype_id + "");
                report_id = getOrAdd(session, false, stm_1, ncon, report_tbl, value_map.get(report_tbl));
//                report2sampledetails_map.put("report_id", report_id + "");
//                } else {
//                    System.out.println("reporttype id error");
//                }
            } else {
                System.out.println("report batch id error");
            }
        } else {
            System.out.println("Person id error");
        }
        int file2report_id = -1;
        int file2path_id = -1;
//        int report2sample_id = -1;
//        if (report_id >= 0) {
//            int donordetails_id = getOrAdd(session, false, stm_1, ncon, donordetails_tbl, value_map.get(donordetails_tbl));
//            int sampledetails_id = getOrAdd(session, false, stm_1, ncon, sampledetails_tbl, value_map.get(sampledetails_tbl));
//            report2sampledetails_map.put("donordetails_id", donordetails_id + "");
//            report2sampledetails_map.put("sampledetails_id", sampledetails_id + "");
//            if (donordetails_id >= 0 && sampledetails_id >= 0) {
//                report2sample_id = getOrAdd(session, false, stm_1, ncon, report2sampledetails_tbl, value_map.get(report2sampledetails_tbl));
//            }
//        }
//        int filetype_id = getOrAdd(session, false, stm_1, ncon, filetype_tbl, value_map.get(filetype_tbl));
//        if (filetype_id >= 0 && report_id >= 0) {
//            file_map.put("filetype_id", filetype_id + "");
        int file_id = getOrAdd(session, false, stm_1, ncon, files_tbl, value_map.get(files_tbl));
        file2report_map.put("files_id", file_id + "");
        file2report_map.put("report_id", report_id + "");
        file2path_map.put("files_id", file_id + "");
        if (file_id >= 0) {
            file2report_id = getOrAdd(session, false, stm_1, ncon, files2report_tbl, value_map.get(files2report_tbl));
            if (file2report_id > 0) {
                file2path_id = getOrAdd(session, false, stm_1, ncon, files2path_tbl, value_map.get(files2path_tbl));
            }
        }
//        }
        if (file2path_id >= 0) {//report2sample_id >= 0 &&
            return true;
        } else {
            return false;
        }
    }

    /**
     * MethodID=IF2
     */
    private int getOrAdd(HttpSession session, boolean donotcreate, Statement st_1,
            Connection ncon, String table, HashMap<String, String> valu_map) {
        try {
            String select_sql = createSql(ncon, table, valu_map, "id");
//            if (table.equalsIgnoreCase(Constants.get_correct_table_name(dataSource,"files"))) {
//            }
            if (select_sql != null) {
                int id = -1;
                if (st_1 != null && !st_1.isClosed() && select_sql != null) {
                    try {
                        ResultSet r_1 = st_1.executeQuery(select_sql);
                        while (r_1.next()) {
                            id = r_1.getInt(1);
                        }
                    } catch (SQLException e) {
                        System.out.println("Error IF2a: " + e.getMessage() + "\n for query =" + select_sql);
                    }
                    try {
                        if (id < 0 && !donotcreate) {
                            PreparedStatement pre_ins = null;
                            if (table2prep_map == null) {
                                table2prep_map = new HashMap<>();
                            }
                            String insert_sql = "";
                            if (table2prep_map.containsKey(table)) {
                                pre_ins = table2prep_map.get(table);
                                pre_ins.clearParameters();
                            } else {
                                insert_sql = create4Prepared(table, valu_map, "id");
                                pre_ins = ncon.prepareStatement(insert_sql);
                                table2prep_map.put(table, pre_ins);
                            }
                            if (pre_ins != null) {
                                ArrayList<String> value_l = new ArrayList<>(valu_map.keySet());
                                for (int i = 0; i < value_l.size(); i++) {
                                    int c_type = Constants.getType(ncon, table, value_l.get(i));
                                    if (c_type == Types.TIMESTAMP) {
                                        String timstap_val = valu_map.get(value_l.get(i)).trim();
                                        if (timstap_val.matches("[0-9]+")) {
                                            Long ts = new Long(timstap_val);
                                            pre_ins.setTimestamp((i + 1), new Timestamp(ts * 1000));
                                        } else {
                                            pre_ins.setTimestamp((i + 1), new Timestamp(0));
                                        }
                                    } else {
                                        pre_ins.setObject((i + 1), valu_map.get(value_l.get(i)), c_type);
                                    }

                                }
                                try {
                                    int resutl = pre_ins.executeUpdate();
                                    if (resutl > 0) {
                                        ResultSet r_2 = st_1.executeQuery(select_sql);
                                        while (r_2.next()) {
                                            id = r_2.getInt(1);
                                        }
                                    }
                                    pre_ins.clearParameters();
                                } catch (SQLException e) {
                                    System.out.println("Error IF2h: " + e.getMessage() + "\n for query =" + insert_sql);
                                }

                            } else {
                                System.out.println("Error IF2f: Failed make prepareStatement " + insert_sql);
                            }

                        }
                        return id;
                    } catch (SQLException e) {
                        System.out.println("Error IF2e: " + e.getMessage());
                        e.printStackTrace();
                    }
                } else {
                    System.out.println("Error IF2b: Select sql null or dataSource null");
                }
            }
        } catch (SQLException ex) {
            System.out.println("Error IF2c: " + ex.getMessage());
        }
        return -1;
    }

    /*
     MethodID=IF1
     */
    private String createSql(Connection c_con, String table, HashMap<String, String> value_map, String selectFiled) {
        //use column types to find out integers ort char
//        if (type.equalsIgnoreCase("select")) {

        String sql = "SELECT " + selectFiled + " from " + table + " where ";
        ArrayList<String> value_l = new ArrayList<String>(value_map.keySet());
        HashMap<String, String[]> constraint_map = Constants.get_key_contraints(c_con, table);
        if (constraint_map.containsKey(Constants.UNIQUE_COLUMNS_FLAG)) {
            String[] uniques_a = constraint_map.get(Constants.UNIQUE_COLUMNS_FLAG);
            ArrayList<String> uniques_l = new ArrayList<String>(1);
            if (uniques_a != null) {
                uniques_l = new ArrayList<String>(Arrays.asList(uniques_a));
            }
            value_l = retainAllIgnoreCase(value_l, uniques_l);
        }
        for (int i = 0; i < value_l.size(); i++) {
            String c_key = value_l.get(i);
            String quat = "'";
            if (!Constants.shoulditbequated(c_con, table, c_key)) {
                quat = "";
            }
            if (i == 0) {
                sql = sql + c_key + "=" + quat + value_map.get(c_key) + quat;
            } else {
                sql = sql + " AND " + c_key + "=" + quat + value_map.get(c_key) + quat;
            }
        }
        if (value_l.isEmpty()) {
            return null;
        } else {
            return sql;
        }
    }

    /*
     MethodID=IF10
     */
    private String create4Prepared(String table, HashMap<String, String> value_map, String selectFiled) {
        String sql = "INSERT INTO " + table + "(";
        String param = "(";
        ArrayList<String> value_l = new ArrayList<String>(value_map.keySet());
        String fields = "";
        for (int i = 0; i < value_l.size(); i++) {
            String c_key = value_l.get(i);
            if (i == 0) {
                fields = c_key;
                param = param + "?";
            } else {
                fields = fields + "," + c_key;
                param = param + ",?";
            }
        }
        sql = sql + fields + ") values" + param + ")";
        return sql;
    }

    private ArrayList<String> retainAllIgnoreCase(ArrayList<String> original_l, ArrayList<String> control_l) {
        if (control_l == null) {
            control_l = new ArrayList<String>(1);
        }
        ArrayList<String> result_l = new ArrayList<String>(control_l.size());
        ArrayList<String> tmp_l = new ArrayList<String>(original_l.size());
        for (int i = 0; i < original_l.size(); i++) {
            tmp_l.add(original_l.get(i).toUpperCase());
        }
        for (int i = 0; i < control_l.size(); i++) {
            String c_val = control_l.get(i).toUpperCase();
            if (tmp_l.contains(c_val)) {
                result_l.add(original_l.get(tmp_l.indexOf(c_val)));
            }
        }
        tmp_l.clear();
        return result_l;
    }

    private void printResults(Connection c_con, PrintWriter out, int cMinid, int cMaxid, HttpSession session) {
        try {
            if (session.getAttribute("resultsDisplay_column_nm_l") != null) {
                ArrayList<String> resultsDisplay_column_nm_l = (ArrayList<String>) session.getAttribute("resultsDisplay_column_nm_l");
                if (resultsDisplay_column_nm_l != null) {
                    if (session.getAttribute("resultsDisplay_data_l") != null) {
                        ArrayList<ArrayList<String>> resultsDisplay_data_l = (ArrayList<ArrayList<String>>) session.getAttribute("resultsDisplay_data_l");
                        if (resultsDisplay_data_l != null) {
                            if (!resultsDisplay_column_nm_l.isEmpty() && resultsDisplay_data_l != null && !resultsDisplay_data_l.isEmpty()) {

                                Connection ncon = null;
                                Statement st_1 = null;
                                try {
                                    getDatasource();
                                    ncon = dataSource.getConnection();
                                    st_1 = ncon.createStatement();
                                } catch (Exception ex) {
                                    ex.printStackTrace();
                                }
                                String files_tbl_nm = Constants.get_correct_table_name(c_con, "files");
                                String files2tags_tbl_nm = Constants.get_correct_table_name(c_con, "files2tags");
                                String report_tbl_nm = Constants.get_correct_table_name(c_con, "report");
                                int numberOfRows = resultsDisplay_data_l.size();
                                if (numberOfRows > rowsPerPage) {
                                    numberOfRows = numberOfRows + (numberOfRows % rowsPerPage);
                                }
                                int pages = numberOfRows / rowsPerPage;
                                String nextLinks = "";
                                for (int i = 0; i < pages; i++) {
                                    if (i == 0) {
                                        nextLinks = "More results > ";
                                    }
                                    nextLinks = nextLinks + " <a href=\"" + Constants.getServerName(c_con) + "Upload/InsertFiles?&cminid=" + (rowsPerPage * i) + "&cmaxid=" + ((rowsPerPage * (i + 1)) - 1) + "\" >" + (rowsPerPage * i) + "-" + ((rowsPerPage * (i + 1)) - 1) + "</a>";
                                }
                                if (pages > 1) {
                                    out.println(" <p><font color='green'>" + nextLinks + "</font></p>");
                                }

                                out.println(" <h8>Following details were added to the database or they were already there</h8>");
                                out.println("<p> </p>");
                                out.print("<form accept-charset=\"ISO-8859-1\"  method=\"post\">");
                                out.println("<table align=\"left\" border=\"1\" cellpadding=\"1\" cellspacing=\"0\"><thead>");
                                out.print("<tr>");
                                for (int i = 0; i < resultsDisplay_column_nm_l.size(); i++) {
                                    out.println("<th>" + resultsDisplay_column_nm_l.get(i) + "</th> ");
                                }

                                out.print("</tr>");
                                out.print("</thead><tbody>");
                                String file_id_4TAG_l = "";
                                ArrayList<Integer> file_id_l = new ArrayList<>();
                                ArrayList<Integer> report_id_l = new ArrayList<>();
//                                String file_id_l = "";
//                                String sending_file_ids = "";
//                                String report_id_l = "";
                                for (int i = cMinid; (i < resultsDisplay_data_l.size() && i <= cMaxid); i++) {
                                    ArrayList<String> c_data_l = resultsDisplay_data_l.get(i);
                                    out.println("<tr>");
                                    int file_id = -1;
                                    int report_id = -1;
                                    try {
                                        if (st_1 != null && !st_1.isClosed()) {
                                            for (int j = 0; j < resultsDisplay_column_nm_l.size(); j++) {
                                                String scol_nm = resultsDisplay_column_nm_l.get(j);
                                                out.println("<td>" + c_data_l.get(j) + "</td>");
                                                if (scol_nm.equalsIgnoreCase("CHECKSUM")) {
                                                    HashMap<String, String> file_map = new HashMap<>();
                                                    file_map.put("checksum", c_data_l.get(j));
                                                    file_id = getOrAdd(session, true, st_1, ncon, files_tbl_nm, file_map);
                                                } else if (scol_nm.equalsIgnoreCase("REPORT_NAME")) {
                                                    HashMap<String, String> report_map = new HashMap<String, String>();
                                                    report_map.put("name", c_data_l.get(j));
                                                    report_id = getOrAdd(session, true, st_1, ncon, report_tbl_nm, report_map);
                                                }

                                            }
                                        }
                                    } catch (SQLException ex) {
                                        ex.printStackTrace();
                                    }
//                                    for (int j = 0; j < tmp_l.size(); j++) {
//                                        out.println("<td>" + tmp_l.get(j) + "</td>");
//                                        try {
//                                            if (st_1 != null && !st_1.isClosed()) {
////                                                if (j == 0) {
////                                                    HashMap<String, String> file_map = new HashMap<String, String>();
////                                                    file_map.put("CHECKSUM", tmp_l.get(j));
////                                                    file_id = getOrAdd(session, true, st_1, ncon, Constants.get_correct_table_name(dataSource, "files"), file_map);
////
////                                                }
//                                                if (j == 1 && file_id < 0) {
//                                                    HashMap<String, String> file_map = new HashMap<String, String>();
//                                                    file_map = new HashMap<String, String>();
//                                                    file_map.put("checksum", tmp_l.get(j));
//                                                    file_id = getOrAdd(session, true, st_1, ncon, Constants.get_correct_table_name(dataSource, "files"), file_map);
//                                                }
//                                                if (j == 7) {
//                                                    HashMap<String, String> report_map = new HashMap<String, String>();
//                                                    report_map.put("name", tmp_l.get(j));
//                                                    report_id = getOrAdd(session, true, st_1, ncon, Constants.get_correct_table_name(dataSource, "report"), report_map);
//                                                }
//                                            }
//                                        } catch (SQLException ex) {
//                                            ex.printStackTrace();
//                                        }
//                                    }
                                    if (file_id >= 0) {
                                        if (file_id_4TAG_l.isEmpty()) {
                                            file_id_4TAG_l = file_id + "";
//                                            sending_file_ids = "'" + file_id + "'";
                                        } else {
                                            file_id_4TAG_l = file_id_4TAG_l + Constants.BATCH_INSERT_SPLITER + file_id;
//                                            sending_file_ids = sending_file_ids + ",'" + file_id + "'";
                                        }
                                        if (file_id_l.isEmpty()) {
                                            file_id_l.add(file_id);
//                                            sending_file_ids = "'" + file_id + "'";
                                        } else {
                                            file_id_l.add(file_id);
//                                            file_id_l = file_id_l + Constants.BATCH_INSERT_SPLITER + file_id;
//                                            sending_file_ids = sending_file_ids + ",'" + file_id + "'";
                                        }

                                    }
                                    if (report_id >= 0) {
                                        report_id_l.add(report_id);
//                                        if (report_id_l.isEmpty()) {
//                                            report_id_l = report_id + "";
//                                        } else {
//                                            report_id_l = report_id_l + "," + report_id;
//                                        }

//                                    out.println("<td>" + "<a href=\"" + Constants.getServerName() + "Edit/Update?files=" + file_id + "&updatesearchvalue=" + file_id + "&serachType=id&current_tbl_nm=files&value_return_to_field=files&value_return_to_page=" + Constants.getServerName() + "Search/SearchResults\">File_details</a>| <a href=\"" + Constants.getServerName() + "Edit/Update?report=" + report_id + "&updatesearchvalue=" + report_id + "&serachType=id&current_tbl_nm=report&value_return_to_field=report&value_return_to_page=" + Constants.getServerName() + "Search/SearchResults\">Report</a></td>");
                                    }
//                                else {
//                                    out.println("<td>" + "<error>Error locating record failed<error></td>");
//                                }
                                    out.println("</tr>");
                                }
                                out.println("<tr>");
                                out.println("<td colspan=\"8\">");
                                if (file_id_4TAG_l.isEmpty() || report_id_l.isEmpty()) {
                                    out.println("<td>" + "<error>Error locating records failed, use the update menue to change values<error></td>");
                                } else {
//                                    &serachType=id&&reload=true&searchvalue=v
                                    String files_static_url_file_nm = makeSTATIC_URL(c_con, file_id_l, files_tbl_nm, Constants.getDocRoot(c_con));
                                    String file_ststic_url = Constants.getServerName(c_con) + "Search/SearchResults3?" + Constants.TABLE_TO_USE_FLAG + files_tbl_nm + "=" + files_static_url_file_nm + "&operation=update&batch_mode=TRUE";
//                            
                                    String report_static_url_file_nm = makeSTATIC_URL(c_con, report_id_l, report_tbl_nm, Constants.getDocRoot(c_con));
                                    String report_ststic_url = Constants.getServerName(c_con) + "Search/SearchResults3?" + Constants.TABLE_TO_USE_FLAG + report_tbl_nm + "=" + report_static_url_file_nm + "&operation=update&batch_mode=TRUE";
//                            
                                    String midify_id_list = file_id_l.toString().replace("\\s", "").replace("[", "").replace("]", "").replace(",", Constants.BATCH_INSERT_SPLITER);
                                    out.println("Modify values:  <a target=\"_blank\" href=\"" + file_ststic_url + "\">Files</a>"
                                            + " | <a target=\"_blank\" href=\"" + report_ststic_url + "\">Report</a>"
                                            + " | <a target=\"_blank\" href=\"" + Constants.getServerName(c_con) + "/Upload/CreateNew?current_tbl_nm=" + Constants.get_correct_table_name(c_con, "files2tags")
                                            + "&operation=insert_batch&resethistory=true&" + Constants.get_correct_table_name(c_con, "files") + "=" + midify_id_list + "&value_returned_to_col=files_id\">File Tags</a>");

                                }
                                out.println("</td>");
                                out.println("</tr>");
                                out.print("</tbody></table>");
//                            out.print("<input type=\"hidden\" name=\"current_tbl_nm\" value=\"" + current_tbl_nm + "\" />");
                                out.print("</form>");
                                try {
                                    ncon.close();
                                } catch (Exception ex) {
                                    ex.printStackTrace();
                                }

                            } else {
                                out.println("<table align=\"left\" border=\"1\" cellpadding=\"0\" cellspacing=\"1\" width=\"100%\"><thead><tr>");
                                out.print("</tr></thead></table>");
                            }
                        } else {
                            out.println("<p><error>Error 15: Error retrieving data<error></p>");
                        }
                    } else {
                        out.println("<p><error>Error 16: Error retrieving data<error></p>");
                    }

                } else {
                    out.println("<p><error>Error 12: Error retrieving data<error></p>");
                }
            } else {
                out.println("<p>Please select one or more files</p>");
                out.println("<p><a href=\"" + Constants.getServerName(c_con) + "/Upload/logintodirectory\">Click here to select files </a></p>");
//            
            }
        } catch (Exception ex) {
        }
    }

    public String makeSTATIC_URL(Connection c_con, ArrayList<Integer> id_l, String current_tbl_nm, String doc_root) {
        String encripted_nm = Constants.makeSTATIC_URL(id_l, current_tbl_nm);
        File file = new File(doc_root + encripted_nm);
        if (file.isFile()) {
        } else {
            writeResultsToFile(current_tbl_nm + "||" + id_l.toString().replaceAll("[^0-9,]", ""), Constants.getDocRoot(c_con) + encripted_nm);
        }
        return encripted_nm;
    }
//    public String makeSTATIC_URL(String idlist, String current_tbl_nm, String doc_root) {
//        idlist = idlist.replaceAll("[^0-9,]", "");
//        String[] tmp_a = idlist.split(",");
//        Arrays.sort(tmp_a);
//        idlist = Arrays.toString(tmp_a).replace("[", "").replace("]", "");
//        String encripted_nm = encript("STATIC_URL_" + current_tbl_nm + "_" + idlist);
//        File file = new File(doc_root + encripted_nm);
//        if (file.isFile()) {
//        } else {
//            writeResultsToFile(current_tbl_nm + "||" + idlist, Constants.getDocRoot() + encripted_nm);
//        }
//
//        return encripted_nm;
//    }
//
//    public String encript(String intext) {
//        try {
//            MessageDigest sha1 = MessageDigest.getInstance("SHA1");
//            byte[] pass_bytes = intext.trim().getBytes();
//            sha1.reset();
//            sha1.update(pass_bytes);
//            byte[] pass_digest = sha1.digest();
//            Formatter formatter = new Formatter();
//            for (byte b : pass_digest) {
//                formatter.format("%02x", b);
//            }
//            String encript_pass = formatter.toString();
//            return encript_pass;
//        } catch (NoSuchAlgorithmException ex) {
//        }
//        return null;
//    }

    private void writeResultsToFile(Object indata, String sessionID) {
        if (indata != null && sessionID != null) {
            try {
                FileOutputStream fos = new FileOutputStream(sessionID);
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

//    private String excuteUpdate(ArrayList<String> orderd_l, ArrayList<String> sqlInsert_l) {
//        String excuteUpdate_result = "<p><font color=\"red\">Error1 :Bad request, query list is empty </font></p>";
//        Connection ncon = null;
//        if (orderd_l != null && !orderd_l.isEmpty()) {
//            String table_nm = orderd_l.get(0);
//            if (sqlInsert_l != null) {
//                try {
//                    getDatasource();
//                    if (dataSource != null) {
//                        try {
//                            ncon = dataSource.getConnection();
//                            if (!ncon.isClosed()) {
//                                Statement st_1 = ncon.createStatement();
//                                for (int i = 0; i < sqlInsert_l.size(); i++) {
//                                    String s_1 = sqlInsert_l.get(i);
//                                    st_1.addBatch(s_1);
//                                }
//                                int[] result = new int[1];
//                                if (sqlInsert_l.size() > 0) {
//                                    result = st_1.executeBatch();
//                                }
//                                boolean errors = false;
//                                for (int i = 0; i < result.length; i++) {
//                                    if (result[i] < 0) {
//                                        errors = true;
//                                    }
//
//                                }
//                                if (errors) {
//                                    excuteUpdate_result = "<p><font color=\"red\">Update failed. There were errors</font></p>";
//                                } else {
////                                String table_nm = orderd_l.get(0);
//                                    excuteUpdate_result = "<p><h8>Update of table " + table_nm + " was successful!</h8> <a href=\"" + Constants.getServerName() + "Upload/BatchFileHandler?function=next\"> Click here to continue</a>  </p>";
//                                }
//                            }
//                            if (!ncon.isClosed()) {
//                                ncon.close();
//                            }
//                        } catch (SQLException e) {
//                            excuteUpdate_result = "<p><error>Error3: " + e.getMessage() + "</error><br /><a href=\"" + Constants.getServerName() + "Upload/BatchFileHandler?function=next&table_nm=" + table_nm + "\"> Click here to change values</a></p>";
//                        } finally {
//                            try {
//                                if (ncon != null && !ncon.isClosed()) {
//                                    ncon.close();
//                                }
//                            } catch (SQLException ex) {
//                            }
//                        }
//                    } else {
//                        excuteUpdate_result = "<p><error>Error2:dataSource failed</error></p>";
//                    }
//
//                } catch (ServletException ex) {
//                    excuteUpdate_result = "<p><error>Error4: " + ex.getMessage() + "</error></p>";
//                }
//                sqlInsert_l.clear();
//            }
//        } else {
//            excuteUpdate_result = "<p><error>Error14: table list empty</error></p>";
//        }
//
//
//        return excuteUpdate_result;
//    }
//
//    private int getIdifFound(String s_1) {
//        int id = -1;
//        Connection ncon = null;
//        try {
//            getDatasource();
//            if (dataSource != null) {
//                try {
//                    ncon = dataSource.getConnection();
//                    if (!ncon.isClosed()) {
//                        Statement st_1 = ncon.createStatement();
//                        ResultSet r_1 = st_1.executeQuery(s_1);
//                        while (r_1.next()) {
//                            id = r_1.getInt(1);
//                        }
//                    }
//                    ncon.close();
//                } catch (SQLException e) {
//                    System.out.println("Error 6: " + e.getMessage() + "\n for query =" + s_1);
//                    return -1;
//                } finally {
//                    try {
//                        ncon.close();
//                    } catch (SQLException e) {
//                    }
//                }
//            }
//
//        } catch (ServletException ex) {
//            ex.printStackTrace();
//        }
//        return id;
//    }
//
//    private HashMap<Integer, HashMap<String, HashMap<String, String>>> getCorrectionmap(HttpServletRequest request, String tablenm) {
//        HashMap<Integer, HashMap<String, HashMap<String, String>>> correction_map = new HashMap<Integer, HashMap<String, HashMap<String, String>>>();
//        Enumeration parameters_e = request.getParameterNames();
//        while (parameters_e.hasMoreElements()) {
//            String clmn_nm = parameters_e.nextElement().toString();
//            if (clmn_nm.matches("[0-9]+_.+")) {
//                String[] parma_split_a = clmn_nm.split("_", 2);
//                if (parma_split_a.length == 2) {
//                    String group = parma_split_a[0];
//                    String clmn = parma_split_a[1];
//                    String value = request.getParameter(clmn_nm);
//                    if (group.matches("[0-9]+") && value != null && !clmn.isEmpty()) {
//                        int group_int = new Integer(group);
//                        if (correction_map.containsKey(group_int)) {
//                            if (correction_map.get(group_int).containsKey(tablenm)) {
//                                correction_map.get(group_int).get(tablenm).put(clmn, value);
//                            } else {
//                                HashMap<String, String> tmp2 = new HashMap<String, String>(2);
//                                tmp2.put(clmn, value);
//                                correction_map.get(group_int).put(tablenm, tmp2);
//                            }
//
//
//                        } else {
//                            HashMap<String, HashMap<String, String>> tmp = new HashMap<String, HashMap<String, String>>();
//                            HashMap<String, String> tmp2 = new HashMap<String, String>(2);
//                            tmp2.put(clmn, value);
//                            tmp.put(tablenm, tmp2);
//                            correction_map.put(group_int, tmp);
//                        }
//                    }
//                }
//            }
//        }
//        return correction_map;
//    }
    private Statement createroleBakk_statement(Connection ncon, HttpSession session) {
        try {
            ncon.setAutoCommit(false);
            Savepoint save1 = ncon.setSavepoint();
            Statement stmt = ncon.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            session.setAttribute("save1", save1);
            return stmt;
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return null;
    }

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

    private DataSource getDatasource() throws ServletException {
        if (dataSource == null) {
            try {
                Context env = (Context) new InitialContext().lookup("java:comp/env");
                dataSource = (DataSource) env.lookup(Constants.DATASOURCE_NAME_DATA_CREATE);
                if (dataSource == null) {
                    throw new ServletException("`" + Constants.DATASOURCE_NAME_DATA_CREATE + "' is an unknown DataSource");
                }
            } catch (NamingException e) {
                throw new ServletException(e);
            }
        }
        return dataSource;
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
