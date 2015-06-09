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
import java.util.Arrays;
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
public class Visualizer extends HttpServlet {

    private Connection ncon;
    private DataSource dataSource;
    private String json_url;

    /**
     * Processes requests for both HTTP
     * <code>GET</code> and
     * <code>POST</code> methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs Json must have a root element
     * named ROOT
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=ISO-8859-1");
        PrintWriter out = response.getWriter();
        HttpSession ses = request.getSession(true);
        String last_updated = "unknown";
        int height = 1250;
        int width = 4000;

        Connection c_con = null;
        String hashurl = null;
        String tag_table = null;
//        boolean selectiveLoad = false;
        String parents_lin = null;

        try {
            c_con = getDatasource().getConnection();
            if (ses.getAttribute("ATRB_LOAD") == null) {
                Constants.loadSessionVariables(c_con, request);
            }
            json_url = request.getParameter("json_url");//"../test2.json";
//            System.out.println("65 json_url=" + json_url);
            if (json_url == null) {
                hashurl = request.getParameter("hashurl");
                if (ses.getAttribute("json_url") != null) {
                    json_url = (String) ses.getAttribute("json_url");
                }
            } else if (json_url != null) {
                ses.setAttribute("json_url", json_url);
            }

//            hashurl = request.getParameter("hashurl");
//            if (hashurl == null && ses.getAttribute("hashurl") != null) {
//                hashurl = (String) ses.getAttribute("hashurl");
//            } else if (json_url != null) {
//                ses.setAttribute("hashurl", hashurl);
//            }
//            System.out.println("76 json_url=" + json_url);
//            System.out.println("77  hashurl=" + hashurl);
            if (hashurl != null) {
                String[] rsult_a = getLineage(c_con, hashurl);
                if (rsult_a != null) {
                    tag_table = rsult_a[0];
                    parents_lin = rsult_a[1];
                    if (parents_lin != null) {
                        int depth = parents_lin.length() - parents_lin.replace(",", "").length();
                        if (width < (depth * 250)) {
                            width = depth * 250;
                        }
                    }
                }
            }
            boolean load_expanded = false;
            if (request.getParameter("expand") != null) {
                load_expanded = true;
            }
            if (request.getParameter("height") != null && request.getParameter("height").matches("[0-9]+")) {
                height = new Integer(request.getParameter("height"));
            }
            if (request.getParameter("width") != null && request.getParameter("width").matches("[0-9]+")) {
                width = new Integer(request.getParameter("width"));
            }

            last_updated = Constants.getJason_last_updated(c_con);

            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=ISO-8859-1\">");
//            out.println("<META HTTP-EQUIV=\"refresh\" CONTENT=\"1\">");
            out.println(" <link HREF=\"" + Constants.getServerName(c_con)
                    + "resources/egenStyler_public.css\"  REL=\"stylesheet\" TYPE=\"text/css\" >");
            out.println("<style>\n"
                    + "            .node {\n"
                    + "                cursor: pointer;\n"
                    + "            }\n"
                    + "            .node circle {\n"
                    + "                fill: #fff;\n"
                    + "                stroke: steelblue;\n"
                    + "                stroke-width: 1.5px;\n"
                    + "            }\n"
                    + "            .node text {\n"
                    + "                font: 12px sans-serif;\n"
                    + "            }\n"
                    + "            .link {\n"
                    + "                fill: none;\n"
                    + "                stroke: #ccc;\n"
                    + "                stroke-width: 1.5px;\n"
                    + "            }\n"
                    + "            div.tooltip {\n"
                    + "                position: absolute;\n"
                    + "                text-align: center;\n"
                    + "                width: 100px;\n"
                    + "                height: 14px;\n"
                    + "                padding: 0px;\n"
                    + "                font: 10px sans-serif;\n"
                    + "                border: 0px;\n"
                    + "                border-radius: 8px;\n"
                    + "               background: #fcfffc;\n"
                    //                    + "               background: #000000;\n"
                    //                    + "                pointer-events: none;\n"
                    + "            }\n"
                    + "\n"
                    + "        </style>");
            out.println(" <script language = JavaScript>     ");
            out.println("            function setProgress(target, value){");
            out.println("                if(document.getElementById(target)!=null){");
            out.println("                    document.getElementById(target).style.visibility = value; ");
            out.println("                }");
            out.println("                ");
            out.println("            } ");
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
            out.println(" function lastselection(list_name,last_cat){\n");
            out.println("  var sel = document.getElementById(list_name);\n"
                    + "     if(sel!=null){\n"
                    + "            for(var i = 0; i < sel.options.length; i++) {\n"
                    + "                  if(sel.options[i].value == last_cat){\n"
                    + "                     sel.selectedIndex = i;\n"
                    + "                     i=sel.options.length; \n"
                    + "                   }\n"
                    + "             }\n"
                    + "     }\n");
            out.println(" }\n");
            out.println("        </script>");
            out.println("<title>GenVar Datamangement System</title>");
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
            if (ses.getAttribute("Header") != null) {
                out.println(ses.getAttribute("Header"));
            } else if (ses.getAttribute("Header_public") != null) {
                out.println(ses.getAttribute("Header_public"));
            }
            out.println("<h1>Visualizer</h1>");

//            out.println("<table class=\"table_msg\" >\n"
//                    + "    <tbody>\n"
//                    + "        <tr>\n"
//                    + "            <td>\n"
//                    + "                Data for this page was last updated:<h8> " + last_updated + "</h8>\n"
//                    + "            </td>\n"
//                    + "        </tr>"
//                    + "     </tbody>"
//                    + "</table>");
            out.println(" Data for this page was last updated:<h8> " + last_updated + "</h8>\n");
            out.println("<p>Use this page to navigate the EGDMS using the hierarchical data arrangemt. </p>");
            out.println("<form accept-charset=\"ISO-8859-1\"  method=\"post\"  action=\"" + Constants.getServerName(c_con) + "/visualizer\">");
            out.println("            <table border=\"1\">");
            out.println("                <tbody>");
            out.println("                    <tr>");
            out.println("                    <td>");

            out.println("Type  ");
            out.println(createDropdown(c_con, tag_table));
            out.println("                            <input type=\"submit\" value=\"Load\" name=\"search_btn\"  onclick='setVisible(\"div_progress\", \"visible\");'/>  ");
            out.println("                        </td>");
            out.println("                    <td>");
            out.println("           <input type=\"Checkbox\" name=\"expand\"  id=\"expand\" value=\"exapnd\" /> Load expanded (warning, browser may crash for large graphs!)");
            out.println("<p>Height:<input type=\"text\" id=\"height\" name=\"height\" style=\"background-color:#fefeff; border:1px inset;\" value=\"" + height + "\" size=\"4\"/></p>");
            out.println("<p>Width:<input type=\"text\" id=\"width\" name=\"width\" style=\"background-color:#fefeff; border:1px inset;\" value=\"" + width + "\" size=\"4\"/></p>");
            out.println("                            <div id=\"div_progress\">Please wait .. <img src=\"progress.gif\" width=\"200\" height=\"10\" alt=\"progress\"  id=\"progressanim\"/></div>");

            out.println("                        </td>");
            out.println("                    <td>");
            out.println("Tips: "
                    + "<font color=\"steelblue\" font-weight=\"bold\"> Blue stroke, Blue fill </font>=A parent(there are nodes under this, click to expand). "
                    + "<font color=\"steelblue\" font-weight=\"bold\"> Blue stroke, white fill</font>=fully expanded node. "
                    + "<font color=\"#5FB404\" font-weight=\"bold\"> Green stroke, Green fill</font>=Has tag information or at least one of the children nodes has tag information. "
                    + "<font color=\"#5FB404\" font-weight=\"bold\"> Green stroke, White fill</font>=Fully expanded node with tag information or a parent of such a node   "
                    + "<font color=\"red\" font-weight=\"bold\"> Red </font>=Highlighted, show a section to focus. "
                    + "");
            out.println("                    </td>");
            out.println("                    </tr>");
            out.println("                    <tr><td colspan=\"3\">");
            out.println(" <span id=\"nRadius-value\">Current selection: Not selected</span>");

            out.println("                    </td></tr>");
            out.println("                </tbody>");
            out.println("            </table>        ");
            out.println("        </form>");
//            out.println("<div id=\"grap_loc\" style=\"height: " + height + "; width: " + width + ";border-style: solid; border-width: 5px;  \">...</div>");
//            out.println("<div id=\"grap_loc\" style=\"min-height: "+div_height+" ; min_width: "+div_width+" ;border-style: solid; border-width: 5px;  \">...</div>");
            out.println("<script src=\"js/d3.v3.min.js\"></script> ");
            out.println("        <script>");
            out.println("            var margin = {top: 20, right: 20, bottom: 20, left: 220},");
            out.println("            width =" + width + "- margin.right - margin.left,");
            out.println("            height = " + height + "- margin.top - margin.bottom;");
//            out.println("            width = document.getElementById(\"grap_loc\").offsetWidth- margin.right - margin.left;");
//            out.println("            height =document.getElementById(\"grap_loc\").offsetheight- margin.top - margin.bottom;");
//               out.println("            width = $(document).width() - margin.right - margin.left,");
//            out.println("            height = " + height + "- margin.top - margin.bottom;");
            out.println("            var i = 0,duration = 750,root;");
            out.println("            var tree = d3.layout.tree().size([height, width]);");
            out.println("");
            out.println("            var diagonal = d3.svg.diagonal()");
            out.println("                    .projection(function(d) {");
            out.println("                return [d.y, d.x];");
            out.println("            });");
            out.println("");
//            out.println(";");
            out.println("            var select_a=[" + parents_lin + "];");
//             out.println("            var select_a=[\"177\",\"4039\",\"4024\",\"0\"];");
            out.println("            var svg = d3.select(\"body\").append(\"svg\")");
            out.println("                    .attr(\"width\",width + margin.right + margin.left)");
            out.println("                    .attr(\"height\", height + margin.top + margin.bottom )");// 
            out.println("                    .append(\"g\")");
            out.println("                    .attr(\"transform\", \"translate(\" + margin.left + \",\" + margin.top + \")\");");
            out.println("");
//            System.out.println("250 " + json_url);
//            out.println("function findlineage(url) {");
//            out.println("   var xmlhttp = new XMLHttpRequest();");
//            out.println("   xmlhttp.overrideMimeType(\"application/json\");");
//            out.println("   xmlhttp.onreadystatechange = function() {");
//            out.println("       if(xmlhttp.readyState == 4 && xmlhttp.status == 200) {");
//            out.println("           var myArr = JSON.parse(xmlhttp.responseText);"); //
//            out.println("           myFunction(myArr);");
//            out.println("       }");
//            out.println("   }");
//            out.println("   xmlhttp.open(\"GET\", url, true);");
//            out.println("   xmlhttp.send();");
//            out.println("}");
//            out.println("d3.select(\"#nRadius-value\").text(obj.children.length);");
//            out.println("}");


            out.println("            d3.json(\"" + json_url + "\", function(error, rootel) {");
            out.println("                root = rootel;");
            out.println("                root.x0 = margin.top;");//height / 2; //
            out.println("                root.y0 = margin.left;");
            out.println("                var panSpeed = 200;");
            out.println("                var panBoundary = 20;");
            out.println("");

//            out.println("                function collapse(d) {");
//            out.println("                    if (d.children) {");  //&& d.id!=2958
//            out.println("                        d._children = d.children;");
//            out.println("                        d._children.forEach(collapse);");
//            out.println("                        d.children = null;");
//            out.println("                    }");
////            out.println("                }");
//                   out.println("      anlysed.push(children[i].id)");
//            out.println("function getLineage(children,id,lineage) {");
//            out.println("   for(i=0;(i<children.length && !lineage);i++){");
//            out.println("       if (children[i].id === id) {");
//            out.println("           lineage=children[i].lineage.split(\",\");");
//            out.println("       }else if(children[i].children){");
//            out.println("           lineage=getLineage(children[i].children,id,lineage);");
//            out.println("       }");
//            out.println("   }");
//            out.println("   if(lineage){");
//            out.println("       return lineage;");
//            out.println("   }else{");
////            out.println("   var rtn=[\"-1\"];");
//            out.println("     return null;");
//            out.println("   }");
//            out.println(" };");


//            out.println("               function loadLineage(children,id,lineage) {");
////            out.println("                   var anlysed=[\"-1\"]");
//            out.println("                  select_a=getLineage(children,id,lineage);");
//            out.println("                   return select_a;");
//            out.println("               }");
//            System.out.println("278 selected_node=" + selected_node);
            out.println("                function collapse(d) {");
            out.println("                    if (d.children ) {");
            if (parents_lin == null) {
                out.println("                        d._children = d.children;");
                out.println("                        d._children.forEach(collapse);");
                out.println("                        d.children = null;");
            } else {
                out.println("                    if (select_a.indexOf(d.id)>-1) {");
                out.println("                        d.children.forEach(collapse);");
                out.println("                        d.used=-3;");
                out.println("                       ");
                out.println("                    }else{");
                out.println("                        d._children = d.children;");
                out.println("                        d._children.forEach(collapse);");
                out.println("                        d.children = null;");
                out.println("                    }");
            }

            out.println("                    }else if(select_a.indexOf(d.id)>-1){");
            out.println("                       d.used=-3;");
            out.println("                    }");
//            out.println("              centerNode(d);");
            out.println("                }");



            //            out.println("                       if (d.children ) {");  //&& d.id!=2958         
//            out.println("                           for(i=0;(i<d.children.length);i++){");
//            out.println("                               clickbyID(d.children[i], parent_a);");
//            out.println("                           }}else");
//            out.println("                var parent_a=ids.split(,); ");
//            out.println("                function findparent(d,results) {");
//            out.println("                    results.push(d.id);");
//            out.println("                    if (d.children) {");  //&& d.id!=2958          
//            out.println("                        d.children.forEach(findparent,results);");
//            out.println("                    }");
//            out.println("                }");

//            out.println("                function collapse(d) {");
//            out.println("                    if (d.children) {");
//            out.println("                      d3.select(\"#nRadius-value\").text(hasID(d.children, 5));");
//            out.println("                    }");
//            out.println("                    if (d.children) {");  //&& d.id!=2958
//            out.println("                        d._children = d.children;");
//            out.println("                        d._children.forEach(collapse);");
//            out.println("                        d.children = null;");
//            out.println("                    }");
//            out.println("                }");

//            out.println("               function hasIDaschild(children, id) {");
//            out.println("                 result_has=false;");
//            out.println("                 for(i=0;(i<children.length && !result_has);i++){");
//            out.println("                   if(children[i].id==id){result_has=true;}");
//            out.println("                  }");
//            out.println("                return result_has;");
//            out.println("               }");

//            out.println("function getParent(d) {");
//            out.println("   var parent_id=-1;");
//            out.println("   parent_id=d.parent.id;");
//            out.println("");
//            out.println("");
//            out.println("");
//            out.println("");
//            out.println("");
//            out.println("   return parent_id;");
//            out.println("}");

//            out.println("function getNodeWithId(children,id,foundnode) {\n");
//            out.println("   for(i=0;(i<children.length && !foundnode);i++){");
//            out.println("       if (children[i].id === id) {foundnode=children[i];\n");
//            out.println("       }else if(children[i].children){");
//            out.println("           foundnode=getNodeWithId(children[i].children,id,foundnode);");
//            out.println("       }");
//            out.println("   }");
//            out.println("   if(foundnode){return foundnode;}");
//            out.println("   else{return -1};");
//            out.println("return -89;");
//            out.println(";}");

//            out.println("               function getParent(children, id) {");
//            out.println("                 p_rsult=[];");
//            out.println("                 result_has=false;");
//            out.println("                 for(j=0;(j<p_rsult.length && !result_has);j++){");
//            out.println("                   for(i=0;(i<children.length && !result_has);i++){");
//            out.println("                       if(children[i].id==id){result_has=true;}");
//            out.println("                   }");
//            out.println("                  }");
//            out.println("                return p_rsult;");
//            out.println("               }");

//            out.println("function expand4ID(node_id) {\n"
//                    + "        if (d._children) {\n"
//                    + "            d.children = d._children;\n"
//                    + "            d.children.forEach(expand);\n"
//                    + "            d._children = null;\n"
//                    + "        }\n"
//                    + "    }");
//
//            out.println("var key = function(obj){\n"
//                    + "  // some unique object-dependent key\n"
//                    + "  return obj.id+\"\"; // just an example\n"
//                    + "};");


//            out.println("                function collapse(d) {");
//            out.println("                   var results = [1456];");
//            out.println("                    if (d.children) {");  //&& d.id!=2958
//            out.println("                       d._children = d.children;");
//            out.println("                       var c_child_a = d._children;");
//            out.println("                       for (i=0; i<c_child_a.length; i++){");
//            out.println("                           if(c_child_a[i].parentNode && results.indexOf(c_child_a[i].parentNode.id)>-1){");
//            out.println("                               results.push(c_child_a[i].id);");
//            out.println("                           }");
//            out.println("                           collapse(c_child_a[i]);");
//            out.println("                       }");
////            out.println("                       d._children.forEach(collapse);");
//            out.println("                       d.children = null;");
//            out.println("                    }");
//            out.println("   d3.select(\"#nRadius-value\").text(results);");
//            out.println("                }");


//            out.println(" var done=2;"
//                    + "   var results = [];\n"
//                    + "   var r_ch =root.children;"
//                    + "     while(done>1){"
//                    + "         for (i = 0; i < results.length; i++) { "
//                    + "             for (j = 0; j < r_ch.length; j++) { "
//                    + "                 "
//       
//                    + "             }"
//                    + "         }"
//                    + "         var "
//                    + "     }");
//            out.println(" d3.select(\"#nRadius-value\").text(loadLineage(root.children,\"" + selected_node.replaceAll("\"", "") + "\",null));");
//              out.println(" d3.select(\"#nRadius-value\").text(root.children.length);");
            if (!load_expanded) {
//                if (parents_lin!=null) {
//                    out.println("loadLineage(root.children,\"" + selected_node.replaceAll("\"", "") + "\",null);");
//                }
                out.println("           root.children.forEach(collapse);");
            }

//            out.println("// pick a node\n"
//                    + "var child = d3.selectAll(\".node\").data()[1];\n"
//                    + "\n"
//                    + "// get the parent data\n"
//                    + "var parent = child.parent;\n"
//                    + "\n"
//                    + "// highlight the node, red\n"
//                    + "d3.selectAll(\".node\").filter(function(d) { return d == child; })\n"
//                    + ".select(\"circle\").style(\"stroke\", \"red\")\n"
//                    + "\n"
//                    + "// highlight the parent, pink\n"
//                    + "d3.selectAll(\".node\").filter(function(d) { return d == parent; })\n"
//                    + ".select(\"circle\").style(\"stroke\", \"pink\") ");
//            out.println("click(root.children[0]);");
//            out.println("click(root.children[0].children[0]);");
//            out.println("click(root.children[0].children[0].children[1]);");
//            out.println("   d3.select(\"#nRadius-value\").text(root.children[0].children[0].children[1].name);");
//            out.println("  toggle(root.children[1].children[2]);\n");
//                    + "  toggle(root.children[9]);\n"
//                    + "  toggle(root.children[9].children[0]);");
//            out.println("                expand(2) ;");
//            out.println(" d3.select(\"#nRadius-value\").text(hasIDaschild(root.children," + selected_node + "));");
//              out.println("            var foundnode=");getNodeWithId(root.children," + selected_node + "), -1);

            out.println("                update(root);");
            out.println("            });");
            out.println("");
            out.println("            function update(source) {");
            out.println("                // Compute the new tree layout.");
            out.println("                var nodes = tree.nodes(root).reverse(),");
            out.println("                        links = tree.links(nodes);");
            out.println("                // Normalize for fixed-depth.");
            out.println("                nodes.forEach(function(d) {");
            out.println("                    d.y = d.depth * 300;");
            out.println("                });");
            out.println("                // Update the nodes…");
            out.println("                var node = svg.selectAll(\"g.node\")");
            out.println("                        .data(nodes, function(d) {");
            out.println("                    return d.id || (d.id = ++i);");
            out.println("                });");
            out.println("                var div = d3.select(\"body\")");
            out.println("                        .append(\"div\")  // declare the tooltip div ");
            out.println("                        .attr(\"class\", \"tooltip\")");
            out.println("                        .style(\"opacity\", 0);");
            // Enter any new nodes at the parent's previous position.
            out.println("                var nodeEnter = node.enter().append(\"g\").attr(\"class\", \"node\").");
            out.println("                        attr(\"transform\", function(d) {");
            out.println("                    return \"translate(\" + source.y0 + \",\" + source.x0 + \")\";");
            out.println("                }");
            out.println("                ).on(\"click\", click).on(\"mouseover\", function(d) {");
            out.println("                    div.transition()");
            out.println("                            .duration(200)");
            out.println("                            .style(\"opacity\", .9)");
            out.println("                    div.html(\"<a href='\"+d.url+\"' >More details[\"+d.count+\"]</a>\")");
            out.println("                            .style(\"left\", (d3.event.pageX+5) + \"px\")");
            out.println("                            .style(\"top\", (d3.event.pageY+5) + \"px\");");
            out.println("                })");
            out.println("                        .on(\"mouseout\", function(d) {");
            out.println("                    div.transition()");
            out.println("                            .duration(2000)");
            out.println("                            .style(\"opacity\", 0);");
            out.println("                });");
            out.println("");
            out.println("");
            out.println("                nodeEnter.append(\"circle\")");
            out.println("                        .attr(\"r\", 1e-6)");
            out.println("                        .style(\"fill\", function(d) {");
//            out.println("                    return d._children ? d.used>-4 ? \"#FF0000\" : \"lightsteelblue\" : d.used>0 ? \"#D0F5A9\": \"#fff\";"); //\"#fff\"
//            out.println("                    return d._children ? \"lightsteelblue\" : \"#fff\";");
            out.println("                    return d._children ? d.used==-3 ? \"#c11111\": d.used>0 ? \"#5FB404\" : \"lightsteelblue\" : d.used==-3 ?  \"#FFC0CB\"  : d.used>0 ? \"#D0F5A9\": \"#fff\";");
//            out.println("                   return d.used>0 ? \"#5FB404\" : d._children ? \"lightsteelblue\" : \"#fff\";");
//          out.println("                    return d._children ? d.used>0 ? \"#5FB404\" : \"lightsteelblue\" : d.used>0 ? \"#D0F5A9\": \"#fff\";"); //\"#fff\"
//            out.println("                    return d._children ? d.used<-3 ? \"black\": d.used>0 ? \"#5FB404\" : \"lightsteelblue\" : d.used>0 ? \"#D0F5A9\": \"#fff\";"); //\"#fff\"
            out.println("                });");
            out.println("");
            out.println("                nodeEnter.append(\"text\")");
            out.println("                        .attr(\"x\", function(d) {");
            out.println("                    return d.children || d._children ? -15 : 15;");
            out.println("                })");
            out.println("                        .attr(\"dy\", \".35em\")");
            out.println("                        .attr(\"text-anchor\", function(d) {");
            out.println("                    return d.children || d._children ? \"end\" : \"start\";");
            out.println("                })");
            out.println("                        .text(function(d) {");
            out.println("                    return d.name;");
            out.println("                })");
            out.println("                        .style(\"fill-opacity\", 1e-6);");
            out.println("");
            out.println("                // Transition nodes to their new position.");
            out.println("                var nodeUpdate = node.transition()");
            out.println("                        .duration(duration)");
            out.println("                        .attr(\"transform\", function(d) {");
            out.println("                    return \"translate(\" + d.y + \",\" + d.x + \")\";");
            out.println("                });");
            out.println("");
            out.println("                nodeUpdate.select(\"circle\")");
            out.println("                        .attr(\"r\", 10)");
            out.println("                        .style(\"fill\", function(d) {");
//            out.println("                    return d._children ? \"lightsteelblue\" : \"#fff\";");
//            out.println("                    return d.used>0 ? \"#5FB404\" : d._children ? \"lightsteelblue\" : \"#fff\";"); //\"#fff\"
//            out.println("                    return d._children ? d.used>0 ? \"#5FB404\" : \"lightsteelblue\" : d.used>0 ? \"#D0F5A9\": \"#fff\";"); //\"#fff\" 
            out.println("                    return d._children ? d.used==-3 ? \"#c11111\" : d.used>0 ? \"#5FB404\" : \"lightsteelblue\" : d.used==-3 ? \"#FFC0CB\" :d.used>0 ? \"#D0F5A9\": \"#fff\";"); //\"#fff\" 
            out.println("                });");
            out.println("");
            out.println("                nodeUpdate.select(\"text\")");
            out.println("                        .style(\"fill-opacity\", 1);");
            out.println("");
            out.println("                // Transition exiting nodes to the parent's new position.");
            out.println("                var nodeExit = node.exit().transition()");
            out.println("                        .duration(duration)");
            out.println("                        .attr(\"transform\", function(d) {");
            out.println("                    return \"translate(\" + source.y + \",\" + source.x + \")\";");
            out.println("                })");
            out.println("                        .remove();");
            out.println("");
            out.println("                nodeExit.select(\"circle\")");
            out.println("                        .attr(\"r\", 1e-6);");
            out.println("");
            out.println("                nodeExit.select(\"text\")");
            out.println("                        .style(\"fill-opacity\", 1e-6);");
            out.println("");
            out.println("                // Update the links…");
            out.println("                var link = svg.selectAll(\"path.link\")");
            out.println("                        .data(links, function(d) {");
            out.println("                    return d.target.id;");
            out.println("                });");
            out.println("");
            out.println("                // Enter any new links at the parent's previous position.");
            out.println("                link.enter().insert(\"path\", \"g\")");
            out.println("                        .attr(\"class\", \"link\")");
            out.println("                        .attr(\"d\", function(d) {");
            out.println("                    var o = {x: source.x0, y: source.y0};");
            out.println("                    return diagonal({source: o, target: o});");
            out.println("                });");
            out.println("");
            out.println("                // Transition links to their new position.");
            out.println("                link.transition()");
            out.println("                        .duration(duration)");
            out.println("                        .attr(\"d\", diagonal);");
            out.println("");
            out.println("                // Transition exiting nodes to the parent's new position.");
            out.println("                link.exit().transition()");
            out.println("                        .duration(duration)");
            out.println("                        .attr(\"d\", function(d) {");
            out.println("                    var o = {x: source.x, y: source.y};");
////            out.println("                    return diagonal({source: o, target: o});");
            out.println("                })");
            out.println("                        .remove();");
            out.println("");
            out.println("                // Stash the old positions for transition.");
            out.println("                nodes.forEach(function(d) {");
            out.println("                    d.x0 = d.x;");
            out.println("                    d.y0 = d.y;");
            out.println("                });");
            out.println("            }");
            out.println("");

//            out.println("function zoom() {\n"
//                    + "        svgGroup.attr(\"transform\", \"translate(\" + d3.event.translate + \")scale(\" + d3.event.scale + \")\");\n"
//                    + "    }");
//            out.println("var zoomListener = d3.behavior.zoom().scaleExtent([0.1, 3]).on(\"zoom\", zoom);");
//
//            out.println("function centerNode(source) {");
//            out.println("   scale = zoomListener.scale();");
//            out.println("   x = -source.y0;");
//            out.println("   y = -source.x0;");
//            out.println("   x = x * scale + width;");//x * scale + width 
//            out.println("   y = y * scale + height;");//y * scale + height
//            out.println("    d3.select('g').transition()\n"
//                    + "            .duration(duration)\n"
//                    + "            .attr(\"transform\", \"translate(\" + x + \",\" + y + \")scale(\" + scale + \")\");");
//            out.println("   zoomListener.scale(scale);");
//            out.println("   zoomListener.translate([x, y]);");
//            out.println("}");

            out.println("// Toggle children on click.");
            out.println("            function click(d) {");
            out.println("                d3.select(\"#nRadius-value\").html(\"Current selection:<h8>\"+d.name+\" (used:\"+d.count+\")</h8>  (<a href=\\\"\"+d.url+\"\\\"> More details</a>)\");\n");
            out.println("                if (d.children) {");
            out.println("                    d._children = d.children;");
            out.println("                    d.children = null;");
            out.println("                } else {");
            out.println("                    d.children = d._children;");
            out.println("                    d._children = null;");
            out.println("                }");
            out.println("                update(d);");
//            out.println("              centerNode(d);");

            out.println("            }");
//            out.println("            function selective_expand(d) {");
//            out.println("                if (d.parent) {");
//            out.println("                   click(d.parentNode);");
//            out.println("                   update(d.parentNode);");
//            out.println("                } else {");
//            out.println("                }");

//            out.println("            }");
            out.println("            // Toggle popup.");
            out.println("            function popup(d) {");
            out.println("                var div = d3.select(\"body\").append(\"div\")");
            out.println("                        .attr(\"class\", \"tooltip\")");
            out.println("                        .style(\"opacity\", 0);");
            out.println("                div.transition().duration(200).style(\"opacity\", .7);");
            out.println("                div.html(d.url)");
            out.println("                        .style(\"left\", (d3.event.pageX) + \"px\")");
            out.println("                        .style(\"top\", (d3.event.pageY - 100) + \"px\");");
            out.println("");
            out.println("                //update(d);");
            out.println("            }");
            out.println("            // Toggle popup.");
            out.println("            function popout(d) {");
            out.println("                var div = d3.select(\"body\").append(\"div\")");
            out.println("                div.transition()");
            out.println("                        .duration(500)");
            out.println("                        .style(\"opacity\", 0);");
            out.println("            }");

//            out.println("d3Click = function () {\n"
//                    + "  this.each(function (i, e) {\n"
//                    + "    var evt = document.createEvent(\"MouseEvents\");\n"
//                    + "    evt.initMouseEvent(\"click\", true, true, window, 0, 0, 0, 0, 0, false, false, false, false, 0, null);\n"
//                    + "\n"
//                    + "    e.dispatchEvent(evt);\n"
//                    + "  });\n"
//                    + "};");

            out.println("        </script>");

            out.println("  <script type=\"text/javascript\">\n");
            out.println("   setProgress(\"div_progress\", \"hidden\");");
//            out.println("   findlineage(\"" + json_url + "\");");
//            System.out.println("699 " + json_url);
            out.println("   lastselection(\"json_url\",\"" + json_url + "\");");
//            out.println("$(\"#Data integration\").d3Click();");
            out.println("  </script>");

            if (ses.getAttribute("footer") != null) {
                out.println("<footer>" + ses.getAttribute("footer") + "</footer>");
            } else if (ses.getAttribute("footer_public") != null) {
                out.println("<footer>" + ses.getAttribute("footer_public") + "</footer>");
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

    /*
     SOFTWARE_ONTOLOGY_TAGSOURCE|HASH=dc2bd19a2579751a412f231543f67662fafa9209
     */
    private String[] getLineage(Connection c_con, String hashurl) {
        String[] out_a = null;
        String[] split_q = hashurl.replaceAll("\"", "").split("\\|"); //HASH=
        if (split_q.length == 2) {
            String table = split_q[0];
            String[] hash = split_q[1].split("=", 2);
            String sql = null;
            if (hash.length == 2) {         
                if (hash[0].toUpperCase().equals("ID")) {
                    String sub_sql = "SELECT LINK_TO_FEATURE FROM " + Constants.get_correct_table_name(c_con, table) + " where " + hash[0] + "=" + hash[1] + "";
                    Statement stm = null;
                    try {
                        stm = c_con.createStatement();
                        ResultSet r = stm.executeQuery(sub_sql);
                        if (r.next()) {
                            if (r.getString(1) != null && r.getString(1).split("\\|").length == 2) {
                                String[] info=r.getString(1).split("\\|");
                                sql = "select LINEAGE from " + Constants.get_correct_table_name(c_con, info[0]) + " where " + info[1].replace("=", "='") + "'";
                                table=info[0];
                            }
                        }
                        stm.close();
                    } catch (SQLException ex) {
                    } finally {
                        close(null, stm, null);
                    }

                } else {
                    sql = "select LINEAGE from " + Constants.get_correct_table_name(c_con, table) + " where " + hash[0] + "='" + hash[1] + "'";
                }

            }     
            if (sql != null) {
                Statement stm = null;
                try {
                    stm = c_con.createStatement();
                    ResultSet r = stm.executeQuery(sql);
                    if (r.next()) {
                        out_a = new String[2];
                        out_a[0] = table.substring(table.indexOf(".") + 1);
                        out_a[1] = r.getString(1);
                    }
                    stm.close();
                } catch (SQLException ex) {
                } finally {
                    close(null, stm, null);
                }
            }
        }
        return out_a;
    }

    private String createDropdown(Connection c_con, String tag_table) {
        StringBuilder drop_down = new StringBuilder();//      
        drop_down.append("<select name='json_url' id='json_url'> ");
        drop_down.append("\n");
        String json_list_tbl = Constants.get_correct_table_name(c_con, "json_list");
        if (json_list_tbl != null) {
            String sql = "select name, url from " + json_list_tbl + " order by name";
            ResultSet r = null;
            if (tag_table != null) {
                tag_table = tag_table.toUpperCase().replace("_TAGSOURCE", "");
            } else {
                tag_table = "NA";
            }
            try {
                if (!c_con.isClosed()) {
                    r = c_con.createStatement().executeQuery(sql);
                    while (r.next()) {
                        String name = r.getString(1);
                        String url = r.getString(2);
                        if (name.toUpperCase().equals(tag_table)) {
                            json_url = url;
                        }
                        if (json_url == null) {
                            json_url = url;
                        }
                        drop_down.append("<option value=\"" + url + "\">group by " + name + " </option>");
                        drop_down.append("\n");
                    }
                    r.close();
                }
            } catch (SQLException ex) {
            } finally {
                close(null, null, r);
            }
        }
        drop_down.append("</select> ");
        return drop_down.toString();
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
