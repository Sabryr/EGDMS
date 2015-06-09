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
import java.util.Arrays;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
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
public class Public_search extends HttpServlet {

    private DataSource dataSource;
//    private Connection ncon;
    private final int MAX_RESULTS_TO_KEEP = 20;
    private HashMap<String, ArrayList<String>> cat2table_map;
    private ArrayList<String> category_l;
//    private final String E = "_E_";
//    private HashMap<String, LinkedHashMap<String, ArrayList<Object[]>>> uer_searches_map;

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
        try {
            c_con = getDatasource().getConnection();
            int server_mode = Constants.getServer_mode(c_con);
            Enumeration<String> en = request.getParameterNames();
            HttpSession ses = request.getSession(true);
            if (ses.getAttribute("ATRB_LOAD") == null) {
                Constants.loadSessionVariables(c_con,request);
            }
            GetFromDb get = null;
            LinkedHashMap<String, ArrayList<Object[]>> resutl_map = null;
            boolean reload = false;
            boolean refresh_active = false;
            boolean simple_mode = true;
            String suc = null;
//        if (uer_searches_map == null) {
//            uer_searches_map = new HashMap<>();
//        }

            Object current_tbl_nm = request.getParameter("current_tbl_nm");
            Object searchvalue = request.getParameter("searchvalue");
            Object searchoperator = request.getParameter("searchoperator");
            Object searchcategory = request.getParameter("Categories");
            if (request.getParameter("clearhistory") != null) {
                resutl_map = new LinkedHashMap<>();
                ses.removeAttribute(Constants.PUBLICSERCHRESULTS_SESSION_FLAG);
//                    if (uer_searches_map.containsKey(ses.getAttribute("person_id").toString())) {
//                        uer_searches_map.remove(ses.getAttribute("person_id").toString());
//                    }
            } else {
                if (ses.getAttribute(Constants.PUBLICSERCHRESULTS_SESSION_FLAG) != null) {
                    resutl_map = (LinkedHashMap<String, ArrayList<Object[]>>) ses.getAttribute(Constants.PUBLICSERCHRESULTS_SESSION_FLAG);
                }

            }
            if (request.getParameter("suc") != null) {
                suc = request.getParameter("suc");
            }
            if (ses.getAttribute("GetFromDb_runner") != null) {
                get = (GetFromDb) ses.getAttribute("GetFromDb_runner");
            }


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
                    simple_mode = true;
                    if (ses.getAttribute("public_mode") != null && ses.getAttribute("public_mode").toString().equalsIgnoreCase("advanced_mode")) {
                        simple_mode = false;
                    }
                } else if (request.getParameter("public_mode").equalsIgnoreCase("advanced_mode")) {
                    simple_mode = false;
                    ses.setAttribute("public_mode", "advanced_mode");
                } else {
                    ses.setAttribute("public_mode", "simple_mode");
                }

                if (request.getParameter("getfromdb") != null) {
                    getDatasource();
                    if (c_con != null) {
//                        if (ncon == null) {
//                            try {
//                                ncon = dataSource.getConnection();
//                            } catch (SQLException ex) {
//                            }
//                        }
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
                            ses.setAttribute(Constants.LAST_SEARCH_TYPE_SESSION_FLAG, current_tbl_nm);
                            ses.setAttribute(Constants.LAST_SEARCH_VALUE_SESSION_FLAG, searchvalue);
                            if (searchcategory != null) {
                                ses.setAttribute(Constants.LAST_SEARCH_CATEGORY_SESSION_FLAG, searchcategory.toString());
                            }
                            if (searchColumn != null) {
                                ses.setAttribute(Constants.LAST_SEARCH_COUMN_SESSION_FLAG, searchColumn);
                            }
//                            try {
//                                if (ncon.isClosed()) {
//                                    ncon = dataSource.getConnection();
//                                }
//                            } catch (Exception ex) {
//                            }
                            boolean expand_to_get_tagged = false;
                            if (simple_mode) {
                                expand_to_get_tagged = true;
                            }
                            if (searchColumn != null) {
                                ses.setAttribute("GetFromDb_runner", startDbSearch(server_mode, ses, c_con, current_tbl_nm.toString(),
                                        searchvalue.toString(), searchoperator.toString(), searchColumn.toString(),
                                        exactmaych_only, 10, expand_to_get_tagged, request.getParameterMap()));
                            } else {
                                ses.setAttribute("GetFromDb_runner", startDbSearch(server_mode, ses, c_con, current_tbl_nm.toString(),
                                        searchvalue.toString(), searchoperator.toString(), null, exactmaych_only, 10, expand_to_get_tagged, request.getParameterMap()));
                            }
                            reload = true;
                        }
                    }

                } else {
                }
            } catch (ServletException ex) {
            }
            if (get != null) {
                if (!refresh_active && get.hasFinished()) {
                    resutl_map = getFromDB(get, resutl_map, ses);
                    ses.setAttribute(Constants.PUBLICSERCHRESULTS_SESSION_FLAG, resutl_map);
                    reload = true;
                } else {
                    reload = false;
                }
//                if (get.hasFinished() && finished_refreshing) {
//                    resutl_map = getFromDB(get, resutl_map, ses);
////                        if (resutl_map.get(Constants.LAST_SEARCH_TYPE_SESSION_FLAG) != null && !resutl_map.get(Constants.LAST_SEARCH_TYPE_SESSION_FLAG).isEmpty()) {
////                            ses.setAttribute(Constants.LAST_SEARCH_TYPE_SESSION_FLAG, resutl_map.get(Constants.LAST_SEARCH_TYPE_SESSION_FLAG).get(0));
////                            resutl_map.remove(Constants.LAST_SEARCH_TYPE_SESSION_FLAG);
////                        }
//                    ses.setAttribute(Constants.PUBLICSERCHRESULTS_SESSION_FLAG, resutl_map);
//                    reload = true;
//                }
            }
            if (reload) {
                pageReload(ses, response, Constants.getServerName(c_con) + "Public_search", 171);
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
            if (!simple_mode) {
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
            }
            out.println("</head>");

            out.println("<body>");
            out.println("<table  class=\"table_text\" >\n"
                    + " <thead>\n"
                    + "     <tr>\n"
                    + "         <th>\n"
                    + "                <img  height=\"150px\" width=\"100%\" src=\"Banner.gif\"/>\n"
                    + "         </th>\n"
                    + "     </tr>\n"
                    + " </thead>\n"
                    + "</table>\n");
            out.println(ses.getAttribute("Header_public"));
            out.println("<h1>Search</h1>");

            out.println("<div id=\"simple_mode\">");
            out.println("<form accept-charset=\"ISO-8859-1\">");
            out.println("   <table border=\"1\">"); //class=\"table3\"
            out.println("       <thead>");
            int colspan = 3;
            String last_search_type = "";
            if (simple_mode) {
                last_search_type = "TAGS2HOST";
                if (request.getParameter("last_search_type") != null) {
                    last_search_type = request.getParameter("last_search_type");
                } else if (ses.getAttribute(Constants.LAST_SEARCH_TYPE_SESSION_FLAG) != null) {
                    last_search_type = (String) ses.getAttribute(Constants.LAST_SEARCH_TYPE_SESSION_FLAG);
                }

//                out.println("      <tr><th colspan=\"" + colspan + "\">");
//                out.println("              <h8>Basic search mode</h8> (<a href=\"" + Constants.getServerName() + "Public_search?mode=advanced_mode\">Use advanced mode</a>) ");
//                out.println("      </th></tr>");
                out.println("       <tr><th  colspan=\"" + colspan + "\">");
                if (last_search_type.equalsIgnoreCase("TAGS2HOST")) {
//                    out.println("<h3>Search tags</h3> ");
                    out.println("<input type=\"hidden\"  name=\"current_tbl_nm\" value=\"TAGS2HOST\"  />");
                }
                out.println("      </th></tr>");
            } else {
                colspan = 3;
                out.println("      <tr><th colspan=\"" + colspan + "\">");
                out.println("        <h8>Advanced mode</h8> (<a href=\"" + Constants.getServerName(c_con) + "Public_search?mode=simple_mode\">Use basic mode</a>)");
                out.println("      </th></tr>");
                out.println("      <tr>");
                out.println("<th>Category  :" + createDropDown(getCategories(c_con), "Categories", "setValues") + " </th>");
                out.println("<th>Table name:   <div_tbl id=\"div_tbl\">Select a category to get the tables</div_tbl> </th>");
                out.println("<th>Column name:  <div_tbl id=\"div_col\">Select a table to get the columns</div_tbl> </th>");
//                    out.println("<th>Column name:  <div_tbl id=\"div_msg\">XX</div_tbl> </th>");

                out.println("      </tr>");
            }
            String last_search_val = "";
            if (request.getParameter("query_used") != null) {
                last_search_val = request.getParameter("query_used");
            } else if (ses.getAttribute(Constants.LAST_SEARCH_VALUE_SESSION_FLAG) != null) {
                last_search_val = (String) ses.getAttribute(Constants.LAST_SEARCH_VALUE_SESSION_FLAG);
            }

            out.println("       </thead>");
            out.println("       <tbody><tr><td  colspan=\"" + colspan + "\">");
            out.println("           <input type=\"text\" style=\"background-color:#fefeff; border:1px inset;\" value=\"" + last_search_val + "\" size=\"100\" name=\"searchvalue\" />");
            out.println("           <input type=\"hidden\" value=\"true\" name=\"getfromdb\"/>");
            out.println("           <input type=\"hidden\" value=\"like\" name=\"searchoperator\"/>");
            out.println("           <input type=\"Checkbox\" name=\"exact\" value=\"exact\"  /> Only exact matches");
            if (!refresh_active) {
                out.println("       <input type=\"submit\" value=\"Search\" name=\"search_btn\"  onclick='setProgress(\"div_progress\", \"visible\")' />");
            } else {
                out.println("       <input type=\"submit\" disabled value=\"Search_in_progress..\"  name=\"search_btn\"  onclick='setProgress(\"div_progress\", \"visible\")' />");
            }
            out.println("<div id=\"div_progress\">Please wait .. <img src=\"progress.gif\" width=\"200\" height=\"10\" alt=\"progress\"  id=\"progressanim\"/></div>");

            out.println("          </td></tr>");

            if (resutl_map != null && !resutl_map.isEmpty()) {
                out.println("<tr><th>Search Results</th><th>Details link</th><th>QR</th></tr>");
                out.println("<tr><th colspan=\"" + colspan + "\"><a href=\"" + Constants.getServerName(c_con) + "Public_search?clearhistory\">Clear results</a></th></tr>");

                out.println(getCurrentResults(c_con, resutl_map, suc));

            }
//            Use the full name of the file hosting server.  <h7>E.g. <a onclick='setValue(\"site\", \"tang.medisin.ntnu.no\");' style=\"cursor:pointer;\" onmouseover=\"this.style.textDecoration = 'underline';\" onmouseout=\"this.style.textDecoration = 'none';\" >tang.medisin.ntnu.no</a>, <a style=\"cursor:pointer;\" onclick='setValue(\"site\", \"tingeling.medisin.ntnu.no\");'  onmouseover=\"this.style.textDecoration = 'underline';\" onmouseout=\"this.style.textDecoration = 'none';\">tingeling.medisin.ntnu.no</a>, <a style=\"cursor:pointer;\" onclick='setValue(\"site\", \"ans-180228.stolav.ntnu.no\");'  onmouseover=\"this.style.textDecoration = 'underline';\" onmouseout=\"this.style.textDecoration = 'none';\">ans-180228.stolav.ntnu.no</a> </h7>\n"            
            out.println("</tbody>");
            out.println("</table>");
            out.println(" </form>");
            out.println("</div>");

            out.println("  <script type=\"text/javascript\">\n");
            if (!refresh_active) {
                out.println("   setProgress(\"div_progress\", \"hidden\");");
                if (!simple_mode) {
                    out.println("setValues(document.getElementById(\"Categories\").options[document.getElementById(\"Categories\").selectedIndex].text);");
                    out.println("setValues_col(document.getElementById(\"current_tbl_nm\").options[document.getElementById(\"current_tbl_nm\").selectedIndex].text);");
                }
            }
            if (!simple_mode) {
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
            out.println("<footer>" + ses.getAttribute("footer_public") + "</footer>");
            out.println("</body>");
            out.println("</html>");

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
                if (c_con != null) {
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
                        r_1.close();
                        stm_1.close();
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
//                    else if( t_split_a[7]!=null &&  t_split_a[7].equals("2")){
//                        
//                    }else{
//                        
//                    }

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
                                    link = "<td>" + s + cq + sc + "  <h8>(" + t_split_a[1] + "_matches)</h8> <warning> *** </warning></td>" + "<td> <a href=\""
                                            + Constants.getServerName(c_con) + "Public_SearchResults?" + Constants.TABLE_TO_USE_FLAG + t_split_a[0] + "=" + t_split_a[2] + "&query_used=" + t_split_a[4] + "&last_search_type=" + t_split_a[3] + "&reset=true&operation=search\">" + s + "" + t_split_a[2] + "" + sc + "</a></td><td>" + qrimage_url + "</td>";
                                } else {
                                    link = "<td>" + s + cq + sc + "  <h8>(" + t_split_a[1] + "_matches)</h8> </td>" + "<td> <a href=\""
                                            + Constants.getServerName(c_con) + "Public_SearchResults?" + Constants.TABLE_TO_USE_FLAG + t_split_a[0] + "=" + t_split_a[2] + "&query_used=" + t_split_a[4] + "&last_search_type=" + t_split_a[3] + "&reset=true&operation=search\">" + s + "" + t_split_a[2] + "" + sc + "</a></td><td>" + qrimage_url + "</td>";
                                }
//                                link = "  <h8>(" + t_split_a[1] + " matches)</h8> </td>" + "<td > <a target=\"_blank\" title=\"Opens in new Window\" href=\"" + Constants.getServerName() + "Public_SearchResults?" + Constants.TABLE_TO_USE_FLAG + t_split_a[0] + "=" + t_split_a[2] + "&query_used=" + last_search_val + "&last_search_type="+last_search_type+"\">" + t_split_a[2] + "</a></td>";

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
            String searchoperator, String searchColumn, boolean exactmaych_only, int user_level, boolean expand_to_get_tagged, Map<String, String[]> entered_values_map_tmp) {
        GetFromDb get = null;
        try {
            get = new GetFromDb(server_mode, getDatasource(), current_tbl_nm, searchvalue, searchoperator,
                    searchColumn, exactmaych_only, Constants.SERCH_OP_FLAG, user_level, expand_to_get_tagged, entered_values_map_tmp, 3);
            (new Thread(get)).start();
        } catch (ServletException ex) {
            System.out.println("Error Maina:1 " + ex.getMessage());
        }
        return get;

    }

    private LinkedHashMap<String, ArrayList<Object[]>> getFromDB(GetFromDb get, LinkedHashMap<String, ArrayList<Object[]>> resutl_map, HttpSession ses) {
        if (get != null) {
//            ses.removeAttribute("GetFromDb_runner");
            ses.setAttribute("GetFromDb_runner_reset", "true");
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
                    if (!Arrays.deepEquals(new_result_map.get(new_key_l.get(i)), old_val)) {
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
                        if (resutl_map.containsKey(new_key_l.get(i))) {
                            new_found_map.put(new_key_l.get(i), resutl_map.remove(new_key_l.get(i)));
                        }
                    }
                }
            }

            if (!new_found_map.isEmpty()) {
                resutl_map.putAll(new_found_map);
            }
        }

        return resutl_map;

    }

    private LinkedHashMap<String, ArrayList<Object[]>> getFromDB_old(GetFromDb get, LinkedHashMap<String, ArrayList<Object[]>> resutl_map, HttpSession ses) {
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
            ArrayList<String> new_key_l = new ArrayList<>(new_result_map.keySet());
            for (int i = (new_key_l.size() - 1); i >= 0; i--) {
                if (!resutl_map.containsKey(new_key_l.get(i))) {
                    resutl_map.put(new_key_l.get(i), new ArrayList<Object[]>());
                    resutl_map.get(new_key_l.get(i)).add(new_result_map.get(new_key_l.get(i)));
                } else {
                    Object[] old_val = resutl_map.get(new_key_l.get(i)).get(0);
                    if (!Arrays.deepEquals(new_result_map.get(new_key_l.get(i)), old_val)) {
                        String[] tarsformed_val = new String[10];
                        resutl_map.put(new_key_l.get(i), new ArrayList<Object[]>());
                        Object[] new_val_a = new_result_map.get(new_key_l.get(i));

                        for (int j = 0; j < new_val_a.length; j++) {
                            if (j == 0) {
                                tarsformed_val[j] = new_val_a[j].toString();
                            } else if (j == 1) {
                                tarsformed_val[j] = new_val_a[j] + " (Updated) ";
                            } else {
                                tarsformed_val[j] = new_val_a[j].toString();
                            }

                        }
                        resutl_map.get(new_key_l.get(i)).add(tarsformed_val);
                    } else {
                        new_found_map.put(new_key_l.get(i), resutl_map.remove(new_key_l.get(i)));
                    }
                }


//                resutl_map.get(new_key_l.get(i)).remove(new_result_map.get(new_key_l.get(i))); "<h2>Updated! </h2>"+


            }

            if (!new_found_map.isEmpty()) {
                resutl_map.putAll(new_found_map);
            }
        }

        return resutl_map;

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
