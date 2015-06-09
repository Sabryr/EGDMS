/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package no.ntnu.medisn.egenvar;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
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
public class Main extends HttpServlet {

    private DataSource dataSource;
//    private Connection ncon;
    private String refreshrate = "2";
    private final int MAX_RESULTS_TO_KEEP = 20;
    private HashMap<String, ArrayList<String>> cat2table_map;
    private ArrayList<String> category_l;
    private HashMap<String, LinkedHashMap<String, ArrayList<Object[]>>> uer_searches_map;

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
    private void pageReload(HttpSession ses, HttpServletResponse response, String url, int caller) {
        try {
            if (!response.isCommitted()) {
                response.sendRedirect(url);
            } else {
                System.out.println("43 " + caller + " Refresh failed");
            }
        } catch (IOException ex) {
        }
    }

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=ISO-8859-1");
        PrintWriter out = response.getWriter();
        HttpSession ses = request.getSession(true);
        GetFromDb get = null;
        boolean finished_refreshing = true;
        int search_mode = 0;
        Connection c_con = null;
        try {
            c_con = getDatasource().getConnection();
            Object searchcategory = request.getParameter("Categories");
            if (get != null && !get.hasFinished()) {
                out.println("<META HTTP-EQUIV=\"refresh\" CONTENT=\"1\">");
                finished_refreshing = false;
            }
            String c_tab = request.getParameter("tab");
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=ISO-8859-1\">");
            out.println("<title>Servlet Main</title>");
            out.println(" <link HREF=\"" + Constants.getServerName(c_con) + "resources/egenStyler.css\"  REL=\"stylesheet\" TYPE=\"text/css\" >");
            out.println(" <style type=\"text/css\">\n"
                    //  + "      body { font-size: 80%; font-family: 'Lucida Grande', Verdana, Arial, Sans-Serif; }\n"
                    + "      ul#tabs { list-style-type: none; margin: 30px 0 0 0; padding: 0 0 0.3em 0; }\n"
                    + "      ul#tabs li { display: inline; }\n"
                    + "      ul#tabs li a { color: #42454a; background-color: #dedbde; border: 1px solid #c9c3ba; border-bottom: none; padding: 0.3em; text-decoration: none; }\n"
                    + "      ul#tabs li a:hover { background-color: #f1f0ee; }\n"
                    + "      ul#tabs li a.selected { color: #000; background-color: #f1f0ee; font-weight: bold; padding: 0.7em 0.3em 0.38em 0.3em; }\n"
                    + "      div.tabContent { border: 1px solid #c9c3ba; padding: 0.5em; background-color: #f1f0ee; }\n"
                    + "      div.tabContent.hide { display: none; }\n"
                    + "    </style>\n"
                    + "\n"
                    + "    <script type=\"text/javascript\">");

            out.println("   var tabLinks = new Array();\n"
                    + "     var contentDivs = new Array();\n"
                    + "     function init() {\n"
                    + "      // Grab the tab links and content divs from the page\n"
                    + "      var tabListItems = document.getElementById('tabs').childNodes;\n"
                    + "      for ( var i = 0; i < tabListItems.length; i++ ) {\n"
                    + "        if ( tabListItems[i].nodeName == \"LI\" ) {\n"
                    + "          var tabLink = getFirstChildWithTagName( tabListItems[i], 'A' );\n"
                    + "          var id = getHash( tabLink.getAttribute('href') );\n"
                    + "          tabLinks[id] = tabLink;\n"
                    + "          contentDivs[id] = document.getElementById( id );\n"
                    + "        }\n"
                    + "      }\n"
                    + "      // Assign onclick events to the tab links, and\n"
                    + "      // highlight the first tab\n"
                    + "      var i = 0;\n"
                    + "      for ( var id in tabLinks ) {\n"
                    + "        tabLinks[id].onclick = showTab;\n"
                    + "        tabLinks[id].onfocus = function() { this.blur() };\n"
                    + "        if ( i == 0 ) tabLinks[id].className = 'selected';\n"
                    + "        i++;\n"
                    + "      }\n"
                    + "      // Hide all content divs except the first\n"
                    + "      var i = 0;\n"
                    + "      for ( var id in contentDivs ) {\n"
                    + "        if ( i != 0 ) contentDivs[id].className = 'tabContent hide';\n"
                    + "        i++;\n"
                    + "      }\n"
                    + "    }");
            out.println("function showTab() {\n"
                    + "      var selectedId = getHash( this.getAttribute('href') );\n"
                    + "      // Highlight the selected tab, and dim all others.\n"
                    + "      // Also show the selected content div, and hide all others.\n"
                    + "      for ( var id in contentDivs ) {\n"
                    + "        if ( id == selectedId ) {\n"
                    + "          tabLinks[id].className = 'selected';\n"
                    + "          contentDivs[id].className = 'tabContent';\n"
                    + "        } else {\n"
                    + "          tabLinks[id].className = '';\n"
                    + "          contentDivs[id].className = 'tabContent hide';\n"
                    + "        }\n"
                    + "      }\n"
                    + "      // Stop the browser following the link\n"
                    + "      return false;\n"
                    + "    }");
            out.println("  function getFirstChildWithTagName( element, tagName ) {\n"
                    + "      for ( var i = 0; i < element.childNodes.length; i++ ) {\n"
                    + "        if ( element.childNodes[i].nodeName == tagName ) return element.childNodes[i];\n"
                    + "      }\n"
                    + "    }");
            out.println("    function getHash( url ) {\n"
                    + "      var hashPos = url.lastIndexOf ( '#' );\n"
                    + "      return url.substring( hashPos + 1 );\n"
                    + "    }");
            out.println("</script>");
            out.println("</head>");
            out.println("<body  onload=\"init()\">");

            out.println("<ul id=\"tabs\"> "
                    + " <li><a href=\"#search\">Search</a></li>  "
                    + "<li><a href=\"#results\">Search Results</a></li> "
                    + "</ul>");

            out.println("<div class=\"tabContent\" id=\"search\">  ");
            getSearch(c_con, out, request, response, finished_refreshing, search_mode, searchcategory);
            out.println("<div>");
            out.println("Search");
            out.println("</div>");
            out.println("</div>");

            out.println("<div class=\"tabContent\" id=\"results\">  ");
            out.println("<div>");
            out.println("resuklts");
            out.println("</div>");
            out.println("</div>");

            out.println("<footer>" + ses.getAttribute("footer") + "</footer>");

            out.println("  <script type=\"text/javascript\">\n");
            if (finished_refreshing) {
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

            close(c_con, null, null);
        } catch (SQLException ex) {
        } finally {
            out.close();
            close(c_con, null, null);
        }
    }

    private void getSearch(Connection c_con, PrintWriter out, HttpServletRequest request,
            HttpServletResponse response, boolean finished_refreshing,
            int search_mode, Object searchcategory) {
        int server_mode = Constants.getServer_mode(c_con);
        Enumeration<String> en = request.getParameterNames();
        HttpSession ses = request.getSession(true);
        GetFromDb get = null;
        LinkedHashMap<String, ArrayList<Object[]>> resutl_map = null;
        boolean reload = false;
        String suc = null;
        if (uer_searches_map == null) {
            uer_searches_map = new HashMap<>();
        }

        if (ses.getAttribute("ATRB_LOAD") == null) {
            pageReload(ses, response, Constants.getServerName(c_con) + "index", 70);
        } else {
            Object current_tbl_nm = request.getParameter("current_tbl_nm");
            Object searchvalue = request.getParameter("searchvalue");
            Object searchoperator = request.getParameter("searchoperator");

//            if (request.getParameter("clearhistory") != null) {
//                resutl_map = new LinkedHashMap<>();
//                ses.removeAttribute(Constants.SERCHRESULTS_SESSION_FLAG);
//                if (uer_searches_map.containsKey(ses.getAttribute("person_id").toString())) {
//                    uer_searches_map.remove(ses.getAttribute("person_id").toString());
//                }
//            } else {
//                if (ses.getAttribute(Constants.SERCHRESULTS_SESSION_FLAG) != null) {
//                    resutl_map = (LinkedHashMap<String, ArrayList<Object[]>>) ses.getAttribute(Constants.SERCHRESULTS_SESSION_FLAG);
//                } else if (ses.getAttribute("person_id") != null) {
//                    if (uer_searches_map.containsKey(ses.getAttribute("person_id").toString())) {
//                        resutl_map = uer_searches_map.get(ses.getAttribute("person_id").toString());
//                        ses.setAttribute(Constants.SERCHRESULTS_SESSION_FLAG, resutl_map);
//                    }
//                }
//            }

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

            HashMap<String, String> table2txtbx_map = new HashMap<>();


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
                    if (c_con != null) {
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
                if (get.hasFinished() && finished_refreshing) {
                    resutl_map = getFromDB(get, resutl_map, ses);

//                        if (resutl_map.get(Constants.LAST_SEARCH_TYPE_SESSION_FLAG) != null && !resutl_map.get(Constants.LAST_SEARCH_TYPE_SESSION_FLAG).isEmpty()) {
//                            ses.setAttribute(Constants.LAST_SEARCH_TYPE_SESSION_FLAG, resutl_map.get(Constants.LAST_SEARCH_TYPE_SESSION_FLAG).get(0));
//                            resutl_map.remove(Constants.LAST_SEARCH_TYPE_SESSION_FLAG);
//                        }
//                    ses.setAttribute(Constants.SERCHRESULTS_SESSION_FLAG, resutl_map);
                    if (ses.getAttribute("person_id") != null) {
                        uer_searches_map.put(ses.getAttribute("person_id").toString(), resutl_map);
                    }
                    reload = true;
                }
            }
            if (reload) {
                pageReload(ses, response, Constants.getServerName(c_con) + "Main", 171);
            }

            out.println(" <link HREF=\"" + Constants.getServerName(c_con) + "resources/egenStyler.css\"  REL=\"stylesheet\" TYPE=\"text/css\" >");
            out.println(" <script language = JavaScript>     \n"
                    + "            function setProgress(target, value){\n"
                    + "                if(document.getElementById(target)!=null){\n"
                    + "                    document.getElementById(target).style.visibility = value; \n"
                    + "                }\n"
                    + "                \n"
                    + "            } \n"
                    + "        </script>");
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
                out.println("       " + fabricateFunction(c_con,"div_tbl", "setValues_col") + "\n");
//                    out.println("      document.getElementById(\"div_msg\").innerHTML=\"B\";\n");
                out.println(" }\n");
                out.println(" function setValues_col(category){\n");
                out.println("       " + fabricateFunction_col(c_con,"div_col") + "\n");
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
            out.println("<h1>Search</h1>");
            out.println(ses.getAttribute("Header"));
//                out.println("<div id=\"simple_mode\">");
            out.println("<form accept-charset=\"ISO-8859-1\"  accept-charset=\"ISO-8859-1\">");
            out.println("   <table border=\"1\">"); //class=\"table3\"
            out.println("       <thead>");
            int colspan = 3;
            String last_search_type = "";
            if (search_mode == 0) {
                last_search_type = "NAME1";
                if (request.getParameter("last_search_type") != null) {
                    last_search_type = request.getParameter("last_search_type");
                } else if (ses.getAttribute(Constants.LAST_SEARCH_TYPE_SESSION_FLAG) != null) {
                    last_search_type = (String) ses.getAttribute(Constants.LAST_SEARCH_TYPE_SESSION_FLAG);
                }

                out.println("      <tr><th colspan=\"" + colspan + "\">");
                out.println("              <h8>Basic mode</h8> | <a href=\"" + Constants.getServerName(c_con) + "Main?mode=1\">Advanced mode</a>  ");
                out.println("        | <a href=\"" + Constants.getServerName(c_con) + "Main?mode=2\">Extended mode</a>");
                out.println("      </th></tr>");
                out.println("       <tr><th  colspan=\"" + colspan + "\">");
                if (last_search_type.equalsIgnoreCase("NAME1")) {
                    out.println("<input type=\"radio\" checked=\"checked\" name=\"current_tbl_nm\" value=\"NAME1\"/> File name or location ");
                } else {
                    out.println("<input type=\"radio\" name=\"current_tbl_nm\" value=\"NAME1\"/> File name or location ");
                }
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

//                    out.println("                 <input type=\"radio\" name=\"current_tbl_nm\" value=\"NAME5\" />Used tags including parents*");           
                out.println("      </th></tr>");
//                    out.println("      <th><tr>");
//                    out.println("*Find tag usage ....");
//                    out.println("      </th></tr>");

            } else if (search_mode == 1) {
                colspan = 3;
                out.println("      <tr><th colspan=\"" + colspan + "\">");
                out.println("        <a href=\"" + Constants.getServerName(c_con) + "Main?mode=0\">Basic mode</a> | <h8>Advanced mode</h8>");
                out.println("        |<a href=\"" + Constants.getServerName(c_con) + "Main?mode=2\">Extended mode</a>");
                out.println("      </th></tr>");
                out.println("      <tr>");
                out.println("<th>Category  :" + createDropDown(getCategories(c_con), "Categories", "setValues") + " </th>");
                out.println("<th>Table name:   <div_tbl id=\"div_tbl\">Select a category to get the tables</div_tbl> </th>");
                out.println("<th>Column name:  <div_tbl id=\"div_col\">Select a table to get the columns</div_tbl> </th>");
//                    out.println("<th>Column name:  <div_tbl id=\"div_msg\">XX</div_tbl> </th>");
                out.println("      </tr>");
            } else if (search_mode == 2) {
                out.println("      <tr><th colspan=\"" + colspan + "\">");
                out.println("         <a href=\"" + Constants.getServerName(c_con) + "Main?mode=0\">Basic mode</a>");
                out.println("       | <a href=\"" + Constants.getServerName(c_con) + "Main?mode=1\">Advanced mode</a> | <h8>Extended mode</h8>");
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
                out.println("       <tbody><tr><td  colspan=\"" + colspan + "\">");
                out.println("           <input type=\"text\" style=\"background-color:#fefeff; border:1px inset;\" value=\"" + last_search_val + "\" size=\"100\" name=\"searchvalue\" />");
                out.println("           <input type=\"hidden\" value=\"true\" name=\"getfromdb\"/>");
                out.println("           <input type=\"hidden\" value=\"like\" name=\"searchoperator\"/>");
                out.println("           <input type=\"Checkbox\" name=\"exact\" value=\"exact\"  /> Only exact matches");
                if (finished_refreshing) {
                    out.println("       <input type=\"submit\" value=\"Search\" name=\"search_btn\"  onclick='setProgress(\"div_progress\", \"visible\")' />");
                } else {
                    out.println("       <input type=\"submit\" disabled value=\"Search_in_progress..\"  name=\"search_btn\"  onclick='setProgress(\"div_progress\", \"visible\")' />");
                }
                out.println("<div id=\"div_progress\">Please wait .. <img src=\"../progress.gif\" width=\"200\" height=\"10\" alt=\"progress\"  id=\"progressanim\"/></div>");

                out.println("          </td></tr>");
            }


            if (resutl_map != null && !resutl_map.isEmpty()) {
                out.println("<tr><th>Search Results</th><th>Details link</th><th>QR</th></tr>");
                out.println("<tr><th colspan=\"" + colspan + "\"><a href=\"" + Constants.getServerName(c_con) + "Main?clearhistory\">Clear results</a></th></tr>");
                out.println(getCurrentResults(c_con, resutl_map, suc));
            }
//            Use the full name of the file hosting server.  <h7>E.g. <a onclick='setValue(\"site\", \"tang.medisin.ntnu.no\");' style=\"cursor:pointer;\" onmouseover=\"this.style.textDecoration = 'underline';\" onmouseout=\"this.style.textDecoration = 'none';\" >tang.medisin.ntnu.no</a>, <a style=\"cursor:pointer;\" onclick='setValue(\"site\", \"tingeling.medisin.ntnu.no\");'  onmouseover=\"this.style.textDecoration = 'underline';\" onmouseout=\"this.style.textDecoration = 'none';\">tingeling.medisin.ntnu.no</a>, <a style=\"cursor:pointer;\" onclick='setValue(\"site\", \"ans-180228.stolav.ntnu.no\");'  onmouseover=\"this.style.textDecoration = 'underline';\" onmouseout=\"this.style.textDecoration = 'none';\">ans-180228.stolav.ntnu.no</a> </h7>\n"            
            out.println("</tbody>");
            out.println("</table>");
            out.println(" </form>");

        }
    }

    private ArrayList<String> getCategories(Connection c_con) {
        return new ArrayList<>(loadData(c_con).keySet());
    }

    private String fabricateFunction(Connection c_con,String divid, String functioname) {
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

    private HashMap<String, ArrayList<String>> loadData(Connection c_con) {
        if (cat2table_map == null) {
            cat2table_map = new HashMap<>();
            String sql = "select category,table_name,simplename from " + Constants.get_correct_table_name(c_con, "tablename2feature");
            try {
                getDatasource();
                if (dataSource != null) {
                    Connection ncon;
                    try {
                        ncon = dataSource.getConnection();
                        Statement stm_1 = ncon.createStatement();
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
                }
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
                            if (!t_split_a[2].toString().equalsIgnoreCase("NA") && !t_split_a[2].toString().equalsIgnoreCase("SPLIT")) {
                                if (suc != null && suc.equals(t_split_a[2])) {
                                    link = "<td>" + s + cq + sc + "  <h8>(" + t_split_a[1] + "_matches)</h8> <warning> *** </warning></td>"
                                            + "<td> <a href=\"" + Constants.getServerName(c_con) + "Search/SearchResults3?" + Constants.TABLE_TO_USE_FLAG + t_split_a[0] + "=" + t_split_a[2] + "&query_used=" + t_split_a[4] + "&last_search_type=" + t_split_a[3] + "&reset=true&operation=search\">" + s + "" + t_split_a[2] + "" + sc + "</a></td><td>" + qrimage_url + "</td>";
                                } else {
                                    link = "<td>" + s + cq + sc + "  <h8>(" + t_split_a[1] + "_matches)</h8> </td>" + "<td> <a href=\""
                                            + Constants.getServerName(c_con) + "Search/SearchResults3?" + Constants.TABLE_TO_USE_FLAG + t_split_a[0] + "=" + t_split_a[2] + "&query_used=" + t_split_a[4] + "&last_search_type=" + t_split_a[3] + "&reset=true&operation=search\">" + s + "" + t_split_a[2] + "" + sc + "</a></td><td>" + qrimage_url + "</td>";
                                }
//                                link = "  <h8>(" + t_split_a[1] + " matches)</h8> </td>" + "<td > <a target=\"_blank\" title=\"Opens in new Window\" href=\"" + Constants.getServerName() + "Search/SearchResults3?" + Constants.TABLE_TO_USE_FLAG + t_split_a[0] + "=" + t_split_a[2] + "&query_used=" + last_search_val + "&last_search_type="+last_search_type+"\">" + t_split_a[2] + "</a></td>";

                                str.append(link);
                            } else if (t_split_a[2].toString().equalsIgnoreCase("SPLIT")) {
                                link = "<td>" + cq + "  <h8>(" + t_split_a[1] + "_matches)</h8> </td>" + "<td> " + s + "" + t_split_a[5] + " " + sc + "<warning> (Use_the_links_for_each_split_!_) </warning> </td><td>" + qrimage_url + "</td>";
                                str.append(link);
                            }
                        }
                    } else {
                        System.out.println("208 " + tmp_l.get(j));

                    }
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
            System.out.println("Error Maina:1 " + ex.getMessage());
        }
        return get;

    }

    private LinkedHashMap<String, ArrayList<Object[]>> getFromDB(GetFromDb get, LinkedHashMap<String, ArrayList<Object[]>> resutl_map, HttpSession ses) {
        if (get != null) {
            ses.removeAttribute("GetFromDb_runner");
            LinkedHashMap<String, Object[]> new_result_map = get.getResultsMap();
            HashMap<String, ArrayList<Object[]>> new_found_map = new HashMap<>();
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

    private String fabricateFunction_col(Connection c_con,String divid) {
        String options = "";
        ArrayList<String> table_l = Constants.getTables(c_con);
        Collections.sort(table_l);
        for (int i = 0; i < table_l.size(); i++) {
            String c_cat = table_l.get(i).split("\\.")[table_l.get(i).split("\\.").length - 1];
            ArrayList<String> col_l = Constants.getColumn_names(c_con,c_cat);
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

    @Override
    public void destroy() {
//        try {
//            if (ncon != null) {
//                ncon.close();
//            }
//        } catch (SQLException ex) {
//        }
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
