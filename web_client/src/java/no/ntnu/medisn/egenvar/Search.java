/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package no.ntnu.medisn.egenvar;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.io.StreamCorruptedException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import java.util.Collections;
import java.util.Enumeration;
import java.util.Formatter;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;

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
public class Search extends HttpServlet {

    private DataSource dataSource;
//    private Connection ncon;
    private String refreshrate = "2";
    private final int MAX_RESULTS_TO_KEEP = 20;
    private HashMap<String, ArrayList<String>> cat2table_map;
    private ArrayList<String> category_l;
    private final String SEARCH_HISTORY_FILE_NAME = "user_searches_map";
//    private final String E = "_E_";
    private HashMap<String, LinkedHashMap<String, ArrayList<Object[]>>> uer_searches_map;

    private void pageReload(HttpServletResponse response, String url, int caller) {
        try {
            if (!response.isCommitted()) {
                response.sendRedirect(url);
            } else {
                System.out.println("43 " + caller + " Refresh failed");
            }
        } catch (IOException ex) {
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
        response.setContentType("text/html;charset=ISO-8859-1");
        PrintWriter out = response.getWriter();
        Connection c_con = null;
        String data_height = "600";
        try {
            c_con = getDatasource().getConnection();
            int server_mode = Constants.getServer_mode(c_con);
            Enumeration<String> en = request.getParameterNames();
            HttpSession ses = request.getSession(true);
            GetFromDb get = null;
            LinkedHashMap<String, ArrayList<Object[]>> resutl_map = null;
            boolean reload = false;
            boolean refresh_active = false;
            int search_mode = 0;
            int refresh_attempts = 0;

            String suc = null;
//        if (uer_searches_map == null) {
            String file_url = Constants.getDocRoot(c_con) + SEARCH_HISTORY_FILE_NAME;
            Object ob = readResultsFromFile(file_url);
            if (ob != null && ob instanceof HashMap) {
                uer_searches_map = (HashMap<String, LinkedHashMap<String, ArrayList<Object[]>>>) ob;
            } else {
                uer_searches_map = new HashMap<>();
            }
//        }   

            if (ses.getAttribute("ATRB_LOAD") == null) {
                pageReload(response, Constants.getServerName(c_con) + "index", 70);
            } else {
                Object current_tbl_nm = request.getParameter("current_tbl_nm");
                Object searchvalue = request.getParameter("searchvalue");
                Object searchoperator = request.getParameter("searchoperator");
                Object searchcategory = request.getParameter("Categories");
                String person_id = null;

                if (ses.getAttribute("person_id") != null) {
                    person_id = ses.getAttribute("person_id").toString();
                }

                if (request.getParameter("clearhistory") != null && person_id != null) {
                    resutl_map = new LinkedHashMap<>();
                    ses.removeAttribute(Constants.SERCHRESULTS_SESSION_FLAG);
                    if (uer_searches_map.containsKey(person_id)) {
                        uer_searches_map.remove(person_id);
                        writeResultsToFile(uer_searches_map, Constants.getDocRoot(c_con) + SEARCH_HISTORY_FILE_NAME);
                    }
                } else {
//                    if (ses.getAttribute(Constants.SERCHRESULTS_SESSION_FLAG) != null) {
//                        resutl_map = (LinkedHashMap<String, ArrayList<Object[]>>) ses.getAttribute(Constants.SERCHRESULTS_SESSION_FLAG);
//                    } else if (person_id != null) {
                    if (uer_searches_map.containsKey(person_id)) {
                        resutl_map = uer_searches_map.get(person_id);
                        ses.setAttribute(Constants.SERCHRESULTS_SESSION_FLAG, resutl_map);
                    } else {
                        if (person_id == null) {
                            if (resutl_map == null) {
                                resutl_map = new LinkedHashMap<>();
                            }
                            Object[] ret_a = new Object[10];
                            ret_a[0] = "NA";//+ " ("++")";
                            ret_a[1] = "0";
                            ret_a[2] = "NA";
                            ret_a[3] = "NA";
                            ret_a[4] = "NA";
                            ret_a[5] = "NA";
                            ret_a[6] = "NA";
                            ret_a[7] = "0";
                            ret_a[8] = new HashSet<String>();
                            ret_a[9] = search_mode;
                            ArrayList<Object[]> tmp_l = new ArrayList<>();
                            tmp_l.add(ret_a);
                            resutl_map.put("Error: User profile missing. Please <a href=\"" + Constants.getServerName(c_con) + "Upload/CreateNew\">Create one</a> ", tmp_l);
                            ses.setAttribute(Constants.SERCHRESULTS_SESSION_FLAG, resutl_map);
                        }
                    }
//                    }
                }

                if (request.getParameter("data_height") != null && request.getParameter("data_height").matches("[0-9]+")) {
                    data_height= request.getParameter("data_height");
                    
                }
                ArrayList<String> tag_source_l;
                if (ses.getAttribute("tag_sources") != null) {
                    tag_source_l = (ArrayList<String>) ses.getAttribute("tag_sources");
                } else {
                    tag_source_l = get_tagsourceNames(c_con);
                    ses.setAttribute("tag_sources", tag_source_l);
                }
                if (request.getParameter("suc") != null) {
                    suc = request.getParameter("suc");
                }
                if (ses.getAttribute("GetFromDb_runner") != null) {
                    get = (GetFromDb) ses.getAttribute("GetFromDb_runner");
                }

                if (ses.getAttribute("refresh_attempts") != null && ses.getAttribute("refresh_attempts").toString().matches("[0-9]+")) {
                    refresh_attempts = new Integer(ses.getAttribute("refresh_attempts").toString());
                    refresh_attempts++;
                    ses.setAttribute("refresh_attempts", refresh_attempts);
                } else {
                    refresh_attempts++;
                    ses.setAttribute("refresh_attempts", refresh_attempts);
                }

                HashMap<String, String> table2txtbx_map = new HashMap<>();
                out.println("<!DOCTYPE html>");
                out.println("<html>");
                out.println("<head>");
                out.println("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=ISO-8859-1\">");
                if (get == null) {
                    refresh_active = false;
                } else if (!get.hasFinished()) {
                    out.println("<META HTTP-EQUIV=\"refresh\" CONTENT=\"1\">");
                    refresh_active = true;
                } else {
                    if (ses.getAttribute("GetFromDb_runner_reset") != null) {
                        ses.removeAttribute("GetFromDb_runner");
                        ses.removeAttribute("GetFromDb_runner_reset");
                    }
                    refresh_active = false;
                }
                try {
                    if (request.getParameter("mode") == null) {
                        search_mode = 0;
                        if (ses.getAttribute("mode") != null) { //ses.getAttribute("mode").toString().equalsIgnoreCase("advanced_mode")
                            if (ses.getAttribute("mode").toString().matches("[0-9]+")) {
                                search_mode = new Integer(ses.getAttribute("mode").toString());
                            }
                        }
                    } else if (request.getParameter("mode").matches("[0-9]+")) { //if (request.getParameter("mode").equalsIgnoreCase("2"))
                        search_mode = new Integer(request.getParameter("mode"));
                        ses.setAttribute("mode", search_mode);
                    }
//                    else {
//                        ses.setAttribute("mode", "simple_mode");
//                    }
//                    System.out.println("144 "+request.getParameter("getfromdb"));
                    //   System.out.println("173 current_tbl_nm=" + current_tbl_nm + " searchvalue=" + searchvalue + " searchoperator=" + searchoperator + " searchColumn=" + searchColumn);

                    if (request.getParameter("getfromdb") != null) {
                        getDatasource();
                        if (!c_con.isClosed()) {
//                            if (ncon == null) {
//                                try {
//                                    ncon = dataSource.getConnection();
//                                } catch (SQLException ex) {
//                                }
//                            }
                            boolean exactmaych_only = false;
                            String searchColumn = null;
                            if (request.getParameter("exact") != null) {
                                exactmaych_only = true;
                            }
                            if (request.getParameter("searchColumn") != null) {
                                searchColumn = request.getParameter("searchColumn").toString();
                            }

                            if (searchColumn == null && current_tbl_nm != null) {
                                String[] tbl_split = current_tbl_nm.toString().split("\\|");
                                if (tbl_split.length > 1) {
                                    current_tbl_nm = tbl_split[0];
                                    searchColumn = tbl_split[1];
                                }
                            }
                            if (current_tbl_nm != null && searchvalue != null && searchoperator != null) {
                                int user_level = 2;
                                if (ses.getAttribute("user_level") != null && ses.getAttribute("user_level").toString().matches("[0-9]+")) {
                                    user_level = new Integer(ses.getAttribute("user_level").toString());
                                }
                                ses.setAttribute(Constants.LAST_SEARCH_TYPE_SESSION_FLAG, current_tbl_nm);
                                ses.setAttribute(Constants.LAST_SEARCH_VALUE_SESSION_FLAG, searchvalue.toString().trim());
                                if (searchcategory != null) {
                                    ses.setAttribute(Constants.LAST_SEARCH_CATEGORY_SESSION_FLAG, searchcategory.toString());
                                }
                                if (searchColumn != null) {
                                    ses.setAttribute(Constants.LAST_SEARCH_COUMN_SESSION_FLAG, searchColumn);
                                }
//                                try {
//                                    if (ncon.isClosed()) {
//                                        ncon = dataSource.getConnection();
//                                    }
//                                } catch (Exception ex) {
//                                }
                                boolean expand_to_get_tagged = false;
                                if (search_mode == 0) {
                                    expand_to_get_tagged = true;
                                }
                                if (searchColumn != null) {
                                    ses.setAttribute("GetFromDb_runner", startDbSearch(server_mode, ses, c_con, current_tbl_nm.toString(),
                                            searchvalue.toString().trim(), searchoperator.toString(), searchColumn.toString(), exactmaych_only,
                                            user_level, expand_to_get_tagged, request.getParameterMap(), search_mode));
                                } else {
                                    ses.setAttribute("GetFromDb_runner", startDbSearch(server_mode, ses, c_con, current_tbl_nm.toString(),
                                            searchvalue.toString().trim(), searchoperator.toString(), null, exactmaych_only, user_level,
                                            expand_to_get_tagged, request.getParameterMap(), search_mode));
                                }
                                reload = true;
                            }
                        }

                    } else {
                    }
                } catch (ServletException ex) {
                }
                if (get != null) {

//                    if (refresh_attempts > 10) { //warn of delay and send email ath this stage
//                        ses.setAttribute("refresh_attempts", 0);
//                        if (get.hasFinished() && finished_refreshing) {
//                            ses.setAttribute("refresh_attempts", 0);
//                            resutl_map = getFromDB(get, resutl_map, ses);
//                            ses.setAttribute(Constants.SERCHRESULTS_SESSION_FLAG, resutl_map);
//                            if (ses.getAttribute("person_id") != null) {
//                                uer_searches_map.put(ses.getAttribute("person_id").toString(), resutl_map);
//                            }
//                            reload = true;
//                        } else {
//                            ses.setAttribute(Constants.SERCHRESULTS_SESSION_FLAG, null);
//                        }
//                    } else {
                    if (!refresh_active && get.hasFinished()) {
//                        System.out.println("276");
                        ses.setAttribute("refresh_attempts", 0);
                        if (!get.wasRetrieved()) {
                            resutl_map = getFromDB(get, resutl_map, ses);
//                        if (resutl_map.get(Constants.LAST_SEARCH_TYPE_SESSION_FLAG) != null && !resutl_map.get(Constants.LAST_SEARCH_TYPE_SESSION_FLAG).isEmpty()) {
//                            ses.setAttribute(Constants.LAST_SEARCH_TYPE_SESSION_FLAG, resutl_map.get(Constants.LAST_SEARCH_TYPE_SESSION_FLAG).get(0));
//                            resutl_map.remove(Constants.LAST_SEARCH_TYPE_SESSION_FLAG);
//                        }
//                        ses.setAttribute(Constants.SERCHRESULTS_SESSION_FLAG, resutl_map);               
                            if (person_id != null) {
                                uer_searches_map.put(person_id, resutl_map);
                                writeResultsToFile(uer_searches_map, Constants.getDocRoot(c_con) + SEARCH_HISTORY_FILE_NAME);
                            }
                        }
                        reload = true;
                    } else if (!refresh_active && !get.hasFinished()) {
                        reload = true;
                        System.out.println("Warning 264!");
                    } else {
                        reload = false;
                    }
                }

                if (reload) {
                    pageReload(response, Constants.getServerName(c_con) + "Search/search", 171);
                }

                out.println(" <link HREF=\"" + Constants.getServerName(c_con) + "resources/egenStyler.css\"  REL=\"stylesheet\" TYPE=\"text/css\" >");
//                out.println("<div id=\"floatdiv\" style=\"  \n"
//                        + "    position:absolute;  \n"
//                        + "    width:200px;height:50px;top:10px;right:10px;  \n"
//                        + "    padding:16px;background:#FFFFFF;  \n"
//                        + "    border:2px solid #2266AA;  \n"
//                        + "    z-index:100\">  \n"
//                        + "This is a floating javascript menu.  \n"
//                        + "</div> ");
                out.println(" <script language = JavaScript>     \n"
                        + "            function setProgress(target, value){\n"
                        + "                if(document.getElementById(target)!=null){\n"
                        + "                    document.getElementById(target).style.visibility = value; \n"
                        + "                }\n"
                        + "                \n"
                        + "            } \n"
                        + "        </script>");
//                out.println("    <script type=\"text/javascript\" src=\"https://ans-180230.stolav.ntnu.no:8185/eGenVar_web/js/floating-1.12.js\">  \n"
//                        + "    </script>  ");
//                out.println("    <script type=\"text/javascript\">  \n"
//                        + "        floatingMenu.add('floatdiv',  \n"
//                        + "            {  \n"
//                        + "                // Represents distance from left or right browser window  \n"
//                        + "                // border depending upon property used. Only one should be  \n"
//                        + "                // specified.  \n"
//                        + "                // targetLeft: 0,  \n"
//                        + "                targetRight: 10,  \n"
//                        + "      \n"
//                        + "                // Represents distance from top or bottom browser window  \n"
//                        + "                // border depending upon property used. Only one should be  \n"
//                        + "                // specified.  \n"
//                        + "                targetTop: 10,  \n"
//                        + "                // targetBottom: 0,  \n"
//                        + "      \n"
//                        + "                // Uncomment one of those if you need centering on  \n"
//                        + "                // X- or Y- axis.  \n"
//                        + "                // centerX: true,  \n"
//                        + "                // centerY: true,  \n"
//                        + "      \n"
//                        + "                // Remove this one if you don't want snap effect  \n"
//                        + "                snap: true  \n"
//                        + "            });  \n"
//                        + "    </script>  \n"
//                        + "");
                out.println("<title>GenVar Datamangement System</title>");
                if (search_mode == 1) {
                    out.println("<script language = JavaScript>\n");
                    out.println("function setVisible(target, message, value) {\n"
                            + "                if (document.getElementById(target) != null) {\n"
                            + "                    document.getElementById(target).style.visibility = value;\n"
                            + "                }\n"
                            + "                if (document.getElementById(message) != null) {\n"
                            + "                    if (document.getElementById(message).style.visibility == \"visible\") {\n"
                            + "                     document.getElementById(message).style.visibility = \"hidden\";\n"
                            + "                     document.getElementById(\"advancedsearch\").style.visibility = \"visible\";\n"
                            + "                    } else {\n"
                            + "                     document.getElementById(message).style.visibility = \"visible\";\n"
                            + "                     document.getElementById(\"advancedsearch\").style.visibility = \"hidden\";\n"
                            + "                    }\n"
                            + "                }\n"
                            + "}\n");
                    out.println(" function setDropDown(category, divid){\n"
                            + "      document.getElementById(divid).innerHTML=category;\n"
                            + "      setValues_col(document.getElementById(\"current_tbl_nm\").options[document.getElementById(\"current_tbl_nm\").selectedIndex].text+\"\"); \n"
                            + " }\n");
                    out.println(" function setDropDown2(category, divid, cat){\n"
                            + "      document.getElementById(divid).innerHTML=category;\n"
                            + " }\n");
                    out.println(" function setValues(category){\n");
                    out.println("       " + fabricateFunction(c_con, "div_tbl", "setValues_col") + "\n");
//                    out.println("      document.getElementById(\"div_msg\").innerHTML=\"B\";\n");
                    out.println(" }\n");
                    out.println(" function setValues_col(category){\n");
                    out.println("       " + fabricateFunction_col(c_con, "div_col") + "\n");
                    out.println(" }\n");

                    out.println(" function setLast_category(list_name,last_cat){\n");
                    out.println("  var sel = document.getElementById(list_name);\n"
                            + "     if(sel!=null){\n"
                            + "            for(var i = 0; i < sel.options.length; i++) {\n"
                            + "                  if(sel.options[i].innerHTML.trim() == last_cat){\n"
                            + "                     sel.selectedIndex = i;\n"
                            + "                     i=sel.options.length; \n"
                            + "                   }\n"
                            + "             }\n"
                            + "     }\n");
                    out.println(" }\n");

                    out.println("\n"
                            + "		var reader = new FileReader();\n"
                            + "		function readText(that){\n"
                            + "			if(that.files && that.files[0]){\n"
                            + "				var reader = new FileReader();\n"
                            + "				reader.onload = function (e) {  \n"
                            + "					var output=e.target.result;\n"
                            + "					document.getElementById('searchvalue').innerHTML= output;\n"
                            + "				};//end onload()\n"
                            + "				reader.readAsText(that.files[0]);\n"
                            + "			}//end if html5 filelist support\n"
                            + "		} \n"
                            + "");
                    out.println("</script>");
                } else if (search_mode == 2) {
                    out.println("<link rel=\"stylesheet\" href=\"../resources/base/jquery.ui.all.css\">\n"
                            + "	<script src=\"" + Constants.getServerName(c_con) + "js/jquery-1.10.2.js\"></script>\n"
                            + "	<script src=\"" + Constants.getServerName(c_con) + "js/ui/jquery.ui.core.js\"></script>\n"
                            + "	<script src=\"" + Constants.getServerName(c_con) + "js/ui/jquery.ui.widget.js\"></script>\n"
                            + "	<script src=\"" + Constants.getServerName(c_con) + "js/ui/jquery.ui.position.js\"></script>\n"
                            + "	<script src=\"" + Constants.getServerName(c_con) + "js/ui/jquery.ui.menu.js\"></script>\n"
                            + "	<script src=\"" + Constants.getServerName(c_con) + "js/ui/jquery.ui.autocomplete.js\"></script>\n"
                            + "	");
                    for (int i = 0; i < tag_source_l.size(); i++) {
                        String c_tbl = tag_source_l.get(i);
                        String c_txt_bx = c_tbl;
                        table2txtbx_map.put(c_tbl, c_txt_bx);
                        out.println(getAutoCompleteScript(c_con, c_tbl, c_txt_bx));
                    }
                }
                out.println("</head>");

                out.println("<body>");
                out.println("<table  class=\"table_text\" >\n"
                        + " <thead>\n"
                        + "     <tr>\n"
                        + "         <th>\n"
                        + "              <img  height=\"150\" width=\"90%\" src=\"../Banner.gif\"/>\n"
                        + "         </th>\n"
                        + "     </tr>\n"
                        + " </thead>\n"
                        + "</table>\n");
                out.println(ses.getAttribute("Header"));
                out.println("<h1>Search</h1>");

//                out.println("<div id=\"simple_mode\">");
                out.println("<form accept-charset=\"ISO-8859-1\"  method=\"post\" >");
                out.println("   <table border=\"1\">"); //class=\"table3\"
                out.println("       <thead>");
                int colspan = 3;
                String last_search_type = "";
                if (search_mode == 0) {
                    last_search_type = "tags";
                    if (request.getParameter("last_search_type") != null) {
                        last_search_type = request.getParameter("last_search_type");
                    } else if (ses.getAttribute(Constants.LAST_SEARCH_TYPE_SESSION_FLAG) != null) {
                        last_search_type = (String) ses.getAttribute(Constants.LAST_SEARCH_TYPE_SESSION_FLAG);
                    }
                    out.println("      <tr><th>");
                    out.println("              Using database version: <h8>" + Constants.get_last_memorised(c_con) + "</h8>");
                    out.println("      </th>");

                    out.println("      <th colspan=\"" + 2 + "\">");
                    out.println("              <h8>Basic mode</h8> | <a href=\"" + Constants.getServerName(c_con) + "Search/search?mode=1\">Advanced mode</a>  ");
                    out.println("        | <a href=\"" + Constants.getServerName(c_con) + "Search/search?mode=2\">Auto-fill mode</a>");
                    out.println("      </th></tr>");
                    out.println("       <tr><th  colspan=\"" + colspan + "\">");

                    if (last_search_type.equalsIgnoreCase("tags")) {
                        out.println("<input type=\"radio\" checked=\"checked\" name=\"current_tbl_nm\" value=\"tags\"  />Used tags ");
                    } else {
                        out.println("<input type=\"radio\" name=\"current_tbl_nm\" value=\"tags\"  />Used tags ");
                    }
                    if (last_search_type.equalsIgnoreCase("NAME4")) {
                        out.println("<input type=\"radio\" checked=\"checked\"  name=\"current_tbl_nm\" value=\"NAME4\" />All tags");
                    } else {
                        out.println("<input type=\"radio\" name=\"current_tbl_nm\" value=\"NAME4\" />All tags");
                    }
                    if (last_search_type.equalsIgnoreCase("NAME1")) {
                        out.println("<input type=\"radio\" checked=\"checked\" name=\"current_tbl_nm\" value=\"NAME1\"/> File name or location ");
                    } else {
                        out.println("<input type=\"radio\" name=\"current_tbl_nm\" value=\"NAME1\"/> File name or location ");
                    }
                    if (last_search_type.equalsIgnoreCase("NAME2")) {
                        out.println("<input type=\"radio\" checked=\"checked\"  name=\"current_tbl_nm\" value=\"NAME2\"  /> Report or batch name ");
                    } else {
                        out.println("<input type=\"radio\" name=\"current_tbl_nm\" value=\"NAME2\"  /> Report or batch name ");
                    }
                    if (last_search_type.equalsIgnoreCase("NAME3")) {
                        out.println("<input type=\"radio\" checked=\"checked\" name=\"current_tbl_nm\" value=\"NAME3\"  /> Sample or donor name");
                    } else {
                        out.println("<input type=\"radio\" name=\"current_tbl_nm\" value=\"NAME3\"  /> Sample or donor name");
                    }

                    if (last_search_type.equalsIgnoreCase("NAME6")) {
                        out.println("<input type=\"radio\" checked=\"checked\" name=\"current_tbl_nm\" value=\"NAME6\"/> People ");
                    } else {
                        out.println("<input type=\"radio\" name=\"current_tbl_nm\" value=\"NAME6\"/> People ");
                    }
//                    out.println("                 <input type=\"radio\" name=\"current_tbl_nm\" value=\"NAME5\" />Used tags including parents*");           
                    out.println("      </th></tr>");
//                    out.println("      <th><tr>");
//                    out.println("*Find tag usage ....");
//                    out.println("      </th></tr>");

                } else if (search_mode == 1) {
                    colspan = 3;
                    out.println("      <tr><th colspan=\"" + colspan + "\">");
                    out.println("        <a href=\"" + Constants.getServerName(c_con) + "Search/search?mode=0\">Basic mode</a> | <h8>Advanced mode</h8>");
                    out.println("        |<a href=\"" + Constants.getServerName(c_con) + "Search/search?mode=2\">Auto-fill mode</a>");
                    out.println("      </th></tr>");
                    out.println("      <tr>");
                    out.println("<th>Category  :" + createDropDown(getCategories(c_con), "Categories", "setValues") + " </th>");
                    out.println("<th>Table name:   <div_tbl id=\"div_tbl\">Select a category to get the tables</div_tbl> </th>");
                    out.println("<th>Column name:  <div_tbl id=\"div_col\">Select a table to get the columns</div_tbl> </th>");

//                    out.println("<th>Column name:  <div_tbl id=\"div_msg\">XX</div_tbl> </th>");
                    out.println("      </tr>");

                } else if (search_mode == 2) {
                    out.println("      <tr><th colspan=\"" + colspan + "\">");
                    out.println("         <a href=\"" + Constants.getServerName(c_con) + "Search/search?mode=0\">Basic mode</a>");
                    out.println("       | <a href=\"" + Constants.getServerName(c_con) + "Search/search?mode=1\">Advanced mode</a> | <h8>Auto-fill mode</h8>");
                    out.println("      </th></tr>");
//                    out.println("      <tr>"); 
//                    out.println("       <tr><th colspan=\"" + colspan + "\">");
//                    out.println("        Avaialable types");
//                    out.println("       </th></tr>");
                }
                String last_search_val = "";
                if (request.getParameter("query_used") != null) {
                    last_search_val = request.getParameter("query_used");
                } else if (ses.getAttribute(Constants.LAST_SEARCH_VALUE_SESSION_FLAG) != null) {
                    last_search_val = (String) ses.getAttribute(Constants.LAST_SEARCH_VALUE_SESSION_FLAG);
                }
                out.println("       </thead>");
                out.println("       <tbody><tr><td  colspan=\"" + colspan + "\">");

//                out.println("<input type=\"hidden\" name=\"mode\"  id=\"mode\" value=\"" + search_mode + "\" />");
                if (search_mode == 2) {
                    for (int i = 0; i < tag_source_l.size(); i++) {
                        String c_tbl = tag_source_l.get(i);
                        if (i == 0) {
                            out.println("          <tr><td>");
                        } else if (i % 3 == 0) {
                            out.println("          </tr><tr><td>");
                        } else {
                            out.println("          <td>");
                        }
                        out.println(c_tbl.replace(Constants.TAGSOURCE_TABLE, "") + " " + "<input type=\"text\" name=\"" + table2txtbx_map.get(c_tbl) + "\" id=\"" + table2txtbx_map.get(c_tbl) + "\" />");
                        out.println("          </td>");
                    }
                    out.println("          </tr> <tr> <td>");
                    out.println("<div id=\"div_progress\">Please wait .. <img src=\"../progress.gif\" width=\"200\" height=\"10\" alt=\"progress\"  id=\"progressanim\"/></div>");
                    out.println("          </tr> </td>");
                    out.println("          </tr>");
                    out.println("           <tr><th colspan=\"" + colspan + "\">");
                    out.println("               Additional search terms <input type=\"text\" style=\"background-color:#fefeff; border:1px inset;\" value=\"" + last_search_val + "\" size=\"40\" name=\"searchvalue\" />");
                    out.println("                <input type=\"Checkbox\" name=\"exact\" value=\"exact\"  /> Only exact matches");
                    out.println("               <input type=\"submit\" value=\"Start Search\" name=\"search_btn\"  onclick='setProgress(\"div_progress\", \"visible\")' />");

                    out.println("<input type=\"hidden\" name=\"current_tbl_nm\" value=\"AUTOFILL\" />");
                    out.println("<input type=\"hidden\" name=\"searchColumn\" value=\"path\" />");
                    out.println("<input type=\"hidden\" name=\"searchoperator\" value=\"=\" />");
                    out.println("           <input type=\"hidden\" value=\"true\" name=\"getfromdb\"/>");

                    out.println("           </th></tr>");
                } else {
                    if (search_mode == 1) {
                        out.println("       <tbody><tr><td  colspan=\"" + colspan + "\">");
                        out.println("Load query from file: <input type=\"file\" onchange='readText(this)' />");
                        out.println("          </td>");
                        out.println("       <tbody><tr><td  colspan=\"" + colspan + "\">");
                        out.println("       <textarea style=\"background-color:#fefeff; border:1px inset;\" value=\"" + last_search_val + "\" rows=\"4\" cols=\"100\" id=\"searchvalue\" name=\"searchvalue\"></textarea>");
//                        out.println("           <input type=\"hidden\" value=\"in\" name=\"searchoperator\"/>");
                        out.println("           <input type=\"hidden\" value=\"like\" name=\"searchoperator\"/>");
                    } else {
                        out.println("       <tbody><tr><td  colspan=\"" + colspan + "\">");
                        out.println("       <input type=\"text\" style=\"background-color:#fefeff; border:1px inset;\" value=\"" + last_search_val + "\" size=\"100\" name=\"searchvalue\" />");
                        out.println("           <input type=\"hidden\" value=\"like\" name=\"searchoperator\"/>");
                    }
                    out.println("           <input type=\"hidden\" value=\"true\" name=\"getfromdb\"/>");

                    if (search_mode == 1) {
                        out.println("           <input type=\"Checkbox\" name=\"exact\" value=\"exact\" checked=\"true\"  /> Only exact matches");

                    } else {
                        out.println("           <input type=\"Checkbox\" name=\"exact\" value=\"exact\"  /> Only exact matches");

                    }

                    if (!refresh_active) {
                        out.println("       <input type=\"submit\" value=\"Search\" name=\"search_btn\"  onclick='setProgress(\"div_progress\", \"visible\")' />");
                    } else {
                        out.println("       <input type=\"submit\" disabled value=\"Search_in_progress..\"  name=\"search_btn\"  onclick='setProgress(\"div_progress\", \"visible\")' />");
                    }
                    out.println("<div id=\"div_progress\">Please wait .. <img src=\"../progress.gif\" width=\"200\" height=\"10\" alt=\"progress\"  id=\"progressanim\"/></div>");

                    out.println("          </td></tr>");
                }

                out.println("</tbody>");
                out.println("</table>");
                out.println(" </form>");
                out.println(" <div id=\"data\" style=\"overflow:scroll; height:" + data_height + "px;\">");
                out.println(" <table border=\"1\">");
                out.println("<tbody>");
                if (resutl_map != null && !resutl_map.isEmpty()) {
                    out.println("<tr><th>Search Results</th><th>Details link</th><th>QR</th></tr>");
                    out.println("<tr><th colspan=\"" + colspan + "\"><a href=\"" + Constants.getServerName(c_con) + "Search/search?clearhistory\">Clear results</a></th></tr>");
                    out.println(getCurrentResults(c_con, resutl_map, suc));
                }

                out.println("</tbody>");
                out.println("</table>");
                out.println(" </div>");
//                else {
//                    out.println("<tr><td>....</td></tr>");
//                }
//            Use the full name of the file hosting server.  <h7>E.g. <a onclick='setValue(\"site\", \"tang.medisin.ntnu.no\");' style=\"cursor:pointer;\" onmouseover=\"this.style.textDecoration = 'underline';\" onmouseout=\"this.style.textDecoration = 'none';\" >tang.medisin.ntnu.no</a>, <a style=\"cursor:pointer;\" onclick='setValue(\"site\", \"tingeling.medisin.ntnu.no\");'  onmouseover=\"this.style.textDecoration = 'underline';\" onmouseout=\"this.style.textDecoration = 'none';\">tingeling.medisin.ntnu.no</a>, <a style=\"cursor:pointer;\" onclick='setValue(\"site\", \"ans-180228.stolav.ntnu.no\");'  onmouseover=\"this.style.textDecoration = 'underline';\" onmouseout=\"this.style.textDecoration = 'none';\">ans-180228.stolav.ntnu.no</a> </h7>\n"            

                out.println("<footer>" + ses.getAttribute("footer") + "</footer>");

                out.println("  <script type=\"text/javascript\">\n");
                if (!refresh_active) {
                    out.println("   setProgress(\"div_progress\", \"hidden\");");
                    if (search_mode == 1) {
                        out.println("setValues(document.getElementById(\"Categories\").options[document.getElementById(\"Categories\").selectedIndex].text);");
                        out.println("setValues_col(document.getElementById(\"current_tbl_nm\").options[document.getElementById(\"current_tbl_nm\").selectedIndex].text);");
                    }

                }
                if (search_mode == 1) {
                    searchcategory = ses.getAttribute(Constants.LAST_SEARCH_CATEGORY_SESSION_FLAG);
                    if (searchcategory != null) {
                        out.println("setLast_category(\"Categories\",\"" + searchcategory.toString().trim() + "\");");
                        out.println("setValues(\"" + searchcategory.toString().trim() + "\");");
                        Object last_table = ses.getAttribute(Constants.LAST_SEARCH_TYPE_SESSION_FLAG);
                        if (last_table != null) {
                            out.println("setLast_category(\"current_tbl_nm\",\"" + last_table.toString().trim() + "\");");
                            out.println("setValues_col(\"" + last_table.toString().trim() + "\");");
                            Object last_col = ses.getAttribute(Constants.LAST_SEARCH_COUMN_SESSION_FLAG);
                            if (last_col != null) {
                                out.println("setLast_category(\"searchColumn\",\"" + last_col.toString().trim() + "\");");
                            }
                        }

                    }
                }
                out.println("    </script>");
                out.println("</body>");
                out.println("</html>");
            }

            close(c_con, null, null);
        } catch (SQLException ex) {
        } finally {
            out.close();
            close(c_con, null, null);
        }
    }

    private String fabricateFunction(Connection c_con, String divid, String functioname) {
        String options = "";
        ArrayList<String> key_l = getCategories(c_con);
        for (int i = 0; i < key_l.size(); i++) {
            String c_cat = key_l.get(i);
            ArrayList<String> val_l = loadData(c_con).get(c_cat);
            Collections.sort(val_l);
            String c_drop = createDropDown2(val_l, "current_tbl_nm", functioname);
            if (i == 0) {
                options = " if(category.match(\"" + c_cat + "\")!=null) {setDropDown(\"" + c_drop + "\", \"" + divid + "\")}\n";
            } else {
                options = options + "else if(category.match(\"" + c_cat + "\")!=null) {setDropDown(\"" + c_drop + "\",\"" + divid + "\")}\n";
            }
        }
        return options;
    }

    private ArrayList<String> get_tagsourceNames(Connection c_con) {
        ArrayList<String> tag_source_l = new ArrayList<>();
        ArrayList<String> full_tag_source_l = Constants.getTagsourceList(c_con);
        for (int i = 0; i < full_tag_source_l.size(); i++) {
            String c_s = full_tag_source_l.get(i);
            tag_source_l.add(c_s.split("\\.")[c_s.split("\\.").length - 1].toUpperCase()); //.replace(Constants.TAGSOURCE_TABLE, "")
        }
        return tag_source_l;
    }

    private String getAutoCompleteScript(Connection c_con, String table_nm, String textbox) {
        String result = "";
        result = "<script>\n"
                + "	jQuery(function() {\n"
                + "		jQuery( \"#" + textbox + "\").autocomplete({\n"
                + "                     delay: 1000,\n"
                + "			minLength: 3,\n"
                + "                     source: function(request, response) {\n"
                + "                         jQuery.getJSON(\"" + Constants.getServerName(c_con) + "Jason_response?\",\n"
                + "                         {table: \"" + table_nm + "\",\n"
                + "                             field: \"PATH\",\n"
                + "                             term: request.term},\n"
                + "                         function(data) {\n"
                + "                             var results = [];\n"
                + "                             $.each(data, function( key, val ) {\n"
                + "                                 results.push(val.name);\n"
                + "                             });"
                + "                             response(results);\n"
                + "                         })"
                //                        + "                             .success(function() { alert(\"second success\"); })\n"
                + "                             .fail(function(jqXHR, status, error) { alert(error); })\n"
                //                        + "                             .complete(function() { alert(\"complete\"); }\n);"
                + "                     }\n" //end source             
                + "             });\n" //end Jquery                
                + "     });\n"
                + "	</script>";
        return result;
    }
//    private String fabricateFunction(String divid, String functioname) {
//        String options = "";
////         options ="      document.getElementById(\"div_msg\").innerHTML="category\";";
//        ArrayList<String> key_l = getCategories();
//        for (int i = 0; i < key_l.size(); i++) {
//            String c_cat = key_l.get(i);
//            ArrayList<String> val_l = loadData().get(c_cat);
//            Collections.sort(val_l);
//            String c_drop = createDropDown2(val_l, "current_tbl_nm", functioname);
//            if (i == 0) {
//                options = " if(category.match(\"" + E + c_cat + E + "\")!=null) {setDropDown(\"" + c_drop + "\", \"" + divid + "\")}\n";
////                  options ="      document.getElementById(\"div_msg\").innerHTML=\"A\";";
//            } else {
////                   options = options +" document.getElementById(\"div_msg\").innerHTML=\""+i+"\";";
//                options = options + "else if(category.match(\"" + E + c_cat + E + "\")!=null) {setDropDown(\"" + c_drop + "\",\"" + divid + "\")}\n";
//            }
//        }
//
////           String var = "";
////            for (int i = 0; i < all_values_l.size(); i++) {
////                var = var+"\nvar "+all_values_l.get(i) + "=\"" + all_values_l.get(i) + "\";";
////            }       
//        //bd01225fd6f6499f2994f5a096466cb642568a39&reset=true&operation=search&current_tbl_nm=SAMPLEDETAILS
//
//        return options;
//    }

    private String fabricateFunction_col(Connection c_con, String divid) {
        String options = "";
        ArrayList<String> table_l = Constants.getTables(c_con);
        Collections.sort(table_l);
        for (int i = 0; i < table_l.size(); i++) {
            String c_cat = table_l.get(i).split("\\.")[table_l.get(i).split("\\.").length - 1];
            ArrayList<String> col_l = Constants.getColumn_names(c_con, c_cat);
            String c_drop = createDropDown(col_l, "searchColumn", null);
            if (i == 0) {
                options = " if(category.match(\"^" + c_cat + "$\")!=null) {setDropDown2(\"" + c_drop + "\", \"" + divid + "\", \"" + c_cat + "\" )}\n";
            } else {
                options = options + "else if(category.match(\"^" + c_cat + "$\")!=null) {setDropDown2(\"" + c_drop + "\", \"" + divid + "\" ,\"" + c_cat + "\")}\n";
            }
        }
        return options;
    }

    private String createDropDown(ArrayList<String> data_l, String name, String addfunction) {
        String out = null;
        if (addfunction == null) {
            out = "<select name='" + name + "' id='" + name + "' >";
        } else {
            out = "<select name='" + name + "' id='" + name + "'  onchange='" + addfunction + "(this.value);'>";
        }
//        String out = "<select name='" + name + "' id='" + name + "' onchange='"+ addfunction + "(\"" + E + "\"+this.value+\"" + E + "\");'>"; 
//        String out = "<select name='" + name + "' id='" + name + "'  onchange='" + addfunction + "(this.value);'>";
        for (int i = 0; i < data_l.size(); i++) {
            if (addfunction != null) {
                out = out + " <option value=\"" + data_l.get(i) + "\">" + data_l.get(i) + " </option>";
            } else {
                out = out + "<option  value='" + data_l.get(i) + "'>" + data_l.get(i) + "</option>";
            }
        }
        out = out + " </select>";
        return out;
    }

//    private String createDropDown(ArrayList<String> data_l, String name, String addfunction) {
//        String out = "<select name='" + name + "' id='" + name + "'>";
//        for (int i = 0; i < data_l.size(); i++) {
//            if (addfunction != null) {
//                out = out + " <option onclick='" + addfunction + "(\"" + E + data_l.get(i) + E + "\");' value=\"" + data_l.get(i) + "\">" + data_l.get(i) + " </option>";
//            } else {
//                out = out + "<option  value='" + E + data_l.get(i) + E + "'>" + data_l.get(i) + "</option>";
//            }
//        }
//        out = out + " </select>";
//        return out;
//    }
    private String createDropDown2(ArrayList<String> data_l, String name, String addfunction) {
        String out = null;
        if (addfunction == null) {
            out = "<select name='" + name + "' id='" + name + ">";
        } else {
            out = "<select name='" + name + "' id='" + name + "' onchange='" + addfunction + "(this.value);' >";
        }

        for (int i = 0; i < data_l.size(); i++) {
            if (addfunction != null) {
                out = out + " <option  value='" + data_l.get(i) + "'>" + data_l.get(i) + " </option>";
            } else {
                out = out + "<option  value='" + data_l.get(i) + "'>" + data_l.get(i) + "</option>";
            }
        }
        out = out + " </select>";
        return out;
    }
//    private String createDropDown2(ArrayList<String> data_l, String name, String addfunction) {
//        String out = "<select name='" + name + "' id='" + name + "'>";
//        for (int i = 0; i < data_l.size(); i++) {
//            if (addfunction != null) {
//
//                out = out + " <option onclick='" + addfunction + "(\\\"" + E + data_l.get(i) + E + "\\\");\' value='" + data_l.get(i) + "'>" + data_l.get(i) + " </option>";
//            } else {
//                out = out + "<option  value='" + E + data_l.get(i) + E + "'>" + data_l.get(i) + "</option>";
//            }
//        }
//        out = out + " </select>";
//        return out;
//    }

    private ArrayList<String> getForCategory(Connection c_con, String category) {
        return loadData(c_con).get(category);
    }

    private ArrayList<String> getCategories(Connection c_con) {
        return new ArrayList<>(loadData(c_con).keySet());
    }

    private HashMap<String, ArrayList<String>> loadData(Connection c_con) {
        if (cat2table_map == null) {
            cat2table_map = new HashMap<>();
            String sql = "select category,table_name,simplename from " + Constants.get_correct_table_name(c_con, "tablename2feature");
            try {
                getDatasource();
//                if (dataSource != null) {
//                    Connection ncon;
                try {
//                        ncon = dataSource.getConnection();
                    Statement stm_1 = c_con.createStatement();
                    ResultSet r_1 = stm_1.executeQuery(sql);
                    while (r_1.next()) {
                        String c_cat = r_1.getString("category");
                        String c_tbl = r_1.getString("table_name");
                        if (!cat2table_map.containsKey(c_cat)) {
                            cat2table_map.put(c_cat, new ArrayList<String>(5));
                        }
                        cat2table_map.get(c_cat).remove(c_tbl);
                        cat2table_map.get(c_cat).add(c_tbl);
                    }
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
//                }
            } catch (ServletException ex) {
                ex.printStackTrace();
            }
        }
        return cat2table_map;
    }

    private String getCurrentResults(Connection c_con, LinkedHashMap<String, ArrayList<Object[]>> resutl_map, String suc) {
        StringBuilder str = new StringBuilder();
        if (resutl_map != null) {
            ArrayList<String> keys_l = new ArrayList<>(resutl_map.keySet());
            for (int i = (keys_l.size() - 1); i >= 0; i--) {
                String cq = keys_l.get(i);
                str.append("<tr>");
//                str.append("<td>Query=" +  + " | ");
                String s = "<boldx>";
                String sc = "</boldx>";
                ArrayList<Object[]> tmp_l = resutl_map.get(keys_l.get(i));

                for (int j = 0; j < tmp_l.size(); j++) {
                    Object[] t_split_a = tmp_l.get(j);
                    if (t_split_a[7] != null && t_split_a[7].equals("1")) {
                        s = "<faded>";
                        sc = "</faded>";
                    } else {
                        s = "<boldx>";
                        sc = "</boldx>";
                    }
                    String link = s + "Link not available" + sc;

                    if (t_split_a != null) {
                        if (t_split_a[1].equals("0")) {
                            link = "<td>" + s + cq + " (0 matches)" + sc + "</td><td>" + s + "NA" + sc + "</td><td>" + s + "NA" + sc + "</td>";
                            str.append(link);
                        } else {
                            String qrimage_url = "NA";
                            if (t_split_a[6] != null) {
                                qrimage_url = "<a href='" + t_split_a[6] + "'><img  height='50' width='50' alt='QR' border='1' src='" + t_split_a[6] + "'/></a>";
                            }
                            String query_to_transmit = "NA";
                            if (t_split_a[4] != null && t_split_a[4].toString().length() < 128) {
                                query_to_transmit = t_split_a[4].toString();
                            }
                            if (!t_split_a[2].toString().equalsIgnoreCase("NA") && !t_split_a[2].toString().equalsIgnoreCase("SPLIT")) {
                                if (suc != null && suc.equals(t_split_a[2])) {
                                    link = "<td>" + s + cq + sc + "  <h8>(" + t_split_a[1] + "_matches)</h8> <warning> *** </warning></td>" + "<td> <a href=\""
                                            + Constants.getServerName(c_con) + "Search/SearchResults3?" + Constants.TABLE_TO_USE_FLAG + t_split_a[0] + "=" + t_split_a[2] + "&query_used=" + query_to_transmit + "&last_search_type=" + t_split_a[3] + "&reset=true&operation=search\">" + s + "" + t_split_a[2] + "" + sc + "</a></td><td>" + qrimage_url + "</td>";
                                } else {
                                    link = "<td>" + s + cq + sc + "  <h8>(" + t_split_a[1] + "_matches)</h8> </td>" + "<td> <a href=\""
                                            + Constants.getServerName(c_con) + "Search/SearchResults3?" + Constants.TABLE_TO_USE_FLAG + t_split_a[0] + "=" + t_split_a[2] + "&query_used=" + query_to_transmit + "&last_search_type=" + t_split_a[3] + "&reset=true&operation=search\">" + s + "" + t_split_a[2] + "" + sc + "</a></td><td>" + qrimage_url + "</td>";
                                }
//                                link = "  <h8>(" + t_split_a[1] + " matches)</h8> </td>" + "<td > <a target=\"_blank\" title=\"Opens in new Window\" href=\"" + Constants.getServerName() + "Search/SearchResults3?" + Constants.TABLE_TO_USE_FLAG + t_split_a[0] + "=" + t_split_a[2] + "&query_used=" + last_search_val + "&last_search_type="+last_search_type+"\">" + t_split_a[2] + "</a></td>";

                                str.append(link);
                            } else if (t_split_a[2].toString().equalsIgnoreCase("SPLIT")) {
                                link = "<td>" + cq + "  <h8>(" + t_split_a[1] + "_matches)</h8> </td>" + "<td> " + s + "" + t_split_a[5] + " " + sc + "<warning> (Use_the_links_for_each_split_!_) </warning> </td><td>" + qrimage_url + "</td>";
                                str.append(link);
                            }
                        }
                    }
//                    else {
//                        System.out.println("208 " + tmp_l.get(j));
//
//                    }
                }
                str.append("</tr>");
            }
        }
        return str.toString();
    }

    private GetFromDb startDbSearch(int server_mode, HttpSession session, Connection ncon, String current_tbl_nm, String searchvalue,
            String searchoperator, String searchColumn, boolean exactmaych_only, int user_level, boolean expand_to_get_tagged,
            Map<String, String[]> entered_values_map_tmp, int search_mode) {
        GetFromDb get = null;
        try {
            get = new GetFromDb(server_mode, getDatasource(), current_tbl_nm, searchvalue, searchoperator, searchColumn, exactmaych_only,
                    Constants.SERCH_OP_FLAG, user_level, expand_to_get_tagged, entered_values_map_tmp, search_mode);
            (new Thread(get)).start();
        } catch (ServletException ex) {
            System.out.println("Error sea1: " + ex.getMessage());
        }
        return get;

    }

    private LinkedHashMap<String, ArrayList<Object[]>> getFromDB(GetFromDb get, LinkedHashMap<String, ArrayList<Object[]>> resutl_map, HttpSession ses) {
        if (get != null) {
//            ses.removeAttribute("GetFromDb_runner");
            ses.setAttribute("GetFromDb_runner_reset", "true");
            LinkedHashMap<String, Object[]> new_result_map = get.getResultsMap();//new LinkedHashMap();
//            new_result_map.putAll(get.getResultsMap());
//            System.out.println("950 " + get.getResultsMap().keySet());
//            System.out.println("948 " + new_result_map.keySet().iterator().next());
//              get.clear();
            LinkedHashMap<String, ArrayList<Object[]>> new_found_map = new LinkedHashMap<>();
            if (resutl_map == null) {
                resutl_map = new LinkedHashMap<>();
            }
            ArrayList<String> key_l = new ArrayList<>(resutl_map.keySet());
            while (key_l.size() > MAX_RESULTS_TO_KEEP) {
                resutl_map.remove(key_l.remove(0));
            }
            key_l = new ArrayList<>(resutl_map.keySet());
            for (int i = 0; i < key_l.size(); i++) {
                ArrayList<Object[]> key_2_l = resutl_map.get(key_l.get(i));
                for (int j = 0; j < key_2_l.size(); j++) {
                    key_2_l.get(j)[7] = "1";
                }
            }
            ArrayList<String> new_key_l = new ArrayList<>(new_result_map.keySet());
            for (int i = (new_key_l.size() - 1); i >= 0; i--) {
                new_result_map.get(new_key_l.get(i))[7] = "0";
                if (!resutl_map.containsKey(new_key_l.get(i))) {
                    resutl_map.put(new_key_l.get(i), new ArrayList<Object[]>());
                    resutl_map.get(new_key_l.get(i)).add(new_result_map.get(new_key_l.get(i)));
                } else {
                    Object[] old_val = resutl_map.get(new_key_l.get(i)).get(0);
//                    if (!Arrays.deepEquals(new_result_map.get(new_key_l.get(i)), old_val)) {
                    if (hasValuesChanged(new_result_map.get(new_key_l.get(i)), old_val)) {
                        String[] tarsformed_val = new String[10];
                        new_found_map.put(new_key_l.get(i), new ArrayList<Object[]>());
                        Object[] new_val_a = new_result_map.get(new_key_l.get(i));
                        new_val_a[7] = "2";
                        for (int j = 0; j < new_val_a.length; j++) {
                            if (new_val_a[j] == null) {
                                new_val_a[j] = "NA";
                            }
                            if (j == 0) {
                                tarsformed_val[j] = new_val_a[j].toString();
                            } else if (j == 1) {
                                tarsformed_val[j] = new_val_a[j] + " (Updated) ";
                            } else {
                                tarsformed_val[j] = new_val_a[j].toString();
                            }

                        }
                        new_found_map.get(new_key_l.get(i)).add(tarsformed_val);
                    } else {
//                        if (resutl_map.containsKey(new_key_l.get(i))) {
//                            new_found_map.put(new_key_l.get(i), resutl_map.remove(new_key_l.get(i)));
//                        }
                    }
                }
            }
            if (!new_found_map.isEmpty()) {
                resutl_map.putAll(new_found_map);
            }
        }
        return resutl_map;
    }

    private boolean hasValuesChanged(Object[] new_val, Object[] old_val) {
        boolean res = true;
        if (new_val != null && old_val != null && new_val.length > 2 && old_val.length > 2) {
            if (new_val[2].toString().equals(old_val[2].toString())) {
                res = false;
            }
        }


        return res;
    }

    private static String encript(String intext) {
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

    private static boolean writeResultsToFile(Object indata, String file_nm) {
        boolean c_result = false;
        if (indata != null && file_nm != null) {
            try {
                FileOutputStream fos = new FileOutputStream(file_nm);
                ObjectOutputStream os;
                try {
                    os = new ObjectOutputStream(fos);
                    os.writeObject(indata);
                    os.close();
                    c_result = true;
                } catch (IOException ex) {
                    System.out.println("Error: " + ex.getMessage());
                }
            } catch (FileNotFoundException ex) {
                System.out.println("Error: " + ex.getMessage());
            }
        }
        return c_result;
    }

    /*
     MethodID=s7
     */
    private Object readResultsFromFile(String filename) {
        File test = new File(filename);
        if (test.isFile() && test.canRead()) {
            Object auth_ob = null;
            try {
                FileInputStream in = new FileInputStream(filename);
                ObjectInputStream obin = new ObjectInputStream(in);
                auth_ob = obin.readObject();
                obin.close();
            } catch (StreamCorruptedException ex) {
                System.out.println("Error s7A: Security exception, the authentication file is corrupted or has been modified by third party");
            } catch (ClassNotFoundException ex) {
                System.out.println("Error s7e: accessing authentication file");
            } catch (IOException ex) {
                System.out.println("Error s7d: accessing authentication file");
            }
            try {
                if (auth_ob != null) {
                    return auth_ob;
                }
            } catch (Exception ex) {
                System.out.println("Error s7B accessing authentication file");
            }
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
                dataSource = (DataSource) env.lookup(Constants.DATASOURCE_NAME_DATA_VIEW);
                if (dataSource == null) {
                    throw new ServletException("`" + Constants.DATASOURCE_NAME_DATA_VIEW + "' is an unknown DataSource");
                }
            } catch (NamingException e) {
                throw new ServletException(e);
            }
        }
        return dataSource;
    }

    @Override
    public void destroy() {
//        try {
//            if (ncon != null) {
//                ncon.close();
//            }
//        } catch (SQLException ex) {
//        }
    }

    /*
     * 
     //                String link_style = "\"cursor:pointer;\" onmouseover=\"this.style.textDecoration='underline';\" onmouseout=\"this.style.textDecoration='none';\" >tang.medisin.ntnu.no</a>, <a style=\"cursor:pointer;\" onclick='setValue(\"ftp_site\", \"tingeling.medisin.ntnu.no\");'  onmouseover=\"this.style.textDecoration='underline';\" onmouseout=\"this.style.textDecoration='none';\">tingeling.medisin.ntnu.no</a>, <a style=\"cursor:pointer;\" onclick='setValue(\"ftp_site\", \"ans-180228.stolav.ntnu.no\");'  onmouseover=\"this.style.textDecoration='underline';\" onmouseout=\"this.style.textDecoration='none';\"";
     //                String file = "<a onclick='setValue(\"Files\", \"files\");' style=" + link_style + " >File name or location</a>";
     //                String tags = "<a onclick='setValue(\"tags\", \"tags\");' style=" + link_style + " >tags</a>";
     //                String report = "<a onclick='setValue(\"report\", \"report\");' style=" + link_style + " >report</a>";
     //                String batch = "<a onclick='setValue(\"batch\", \"batch\");' style=" + link_style + " >report batch</a>"
     */
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
