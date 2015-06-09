/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package no.ntnu.medisn.egenvar;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
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
public class Visualizer2 extends HttpServlet {

    private Connection ncon;
    private DataSource dataSource;

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
        HttpSession ses = request.getSession(true);
        String last_updated = "unknown";
        int height = 500;
        int width = 4000;
        Connection c_con = null;
        try {
            c_con = getDatasource().getConnection();
            if (ses.getAttribute("ATRB_LOAD") == null) {
                Constants.loadSessionVariables(c_con, request);
            }
            String json_url = request.getParameter("json_url");//"../test2.json";
            if (json_url == null && ses.getAttribute("json_url") != null) {
                json_url = (String) ses.getAttribute("json_url");
            } else if (json_url != null) {
                ses.setAttribute("json_url", json_url);
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
            out.println(" <link HREF=\"" + Constants.getServerName(c_con)
                    + "resources/egenStyler_public.css\"  REL=\"stylesheet\" TYPE=\"text/css\" >");

            out.println("<title>Servlet Visualizer2</title>");
//            out.println("<script src=\"js/d3.js\"></script> ");
            out.println("<script src=\"js/d3.v3.min.js\"></script> ");
            out.println("<script src=\"js/d3.layout.js\"></script> ");
            out.println("<script src=\"js/d3.geom.js\"></script> ");

            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet Visualizer2 at " + request.getContextPath() + "</h1>");
//            out.println("<script type=\"text/javascript\" charset=\"utf-8\">\n");
            draw(out,1);
//                    + "			var w = 960, h = 500;\n"
//                    + "			var labelDistance = 0;\n"
//                    + "			var vis = d3.select(\"body\").append(\"svg:svg\").attr(\"width\", w).attr(\"height\", h);\n"
//                    + "			var nodes = [];\n"
//                    + "			var labelAnchors = [];\n"
//                    + "			var labelAnchorLinks = [];\n"
//                    + "			var links = [];\n");
//            //                    + "			for(var i = 0; i < 30; i++) {\n"
//            out.println(" 	var node = {\n"
//                    + "					label : \"node \" +1\n"
//                    + "				};\n"
//                    + "				nodes.push(node);\n"
//                    + "				labelAnchors.push({\n"
//                    + "					node : node\n"
//                    + "				});\n"
//                    + "				labelAnchors.push({\n"
//                    + "					node : node\n"
//                    + "				});\n");
//
//            for (int i = 2; i < 20; i++) {
//                out.println(" 	 node = {\n"
//                        + "					label : \"node \" +" + i + "\n"
//                        + "				};\n"
//                        + "				nodes.push(node);\n"
//                        + "				labelAnchors.push({\n"
//                        + "					node : node\n"
//                        + "				});\n"
//                        + "				labelAnchors.push({\n"
//                        + "					node : node\n"
//                        + "				});\n");
//
//            }
//
//
//            //                    + "			};\n"
//            out.println("			for(var i = 0; i < nodes.length; i++) {\n"
//                    + "				for(var j = 0; j < i; j++) {\n"
//                    + "					if(Math.random() > .95)\n"
//                    + "						links.push({\n"
//                    + "							source : i,\n"
//                    + "							target : j,\n"
//                    + "							weight : Math.random()\n"
//                    + "						});\n"
//                    + "				}\n"
//                    + "				labelAnchorLinks.push({\n"
//                    + "					source : i * 2,\n"
//                    + "					target : i * 2 + 1,\n"
//                    + "					weight : 1\n"
//                    + "				});\n"
//                    + "			};\n"
//                    + "			var force = d3.layout.force().size([w, h]).nodes(nodes).links(links).gravity(1).linkDistance(50).charge(-3000).linkStrength(function(x) {\n"
//                    + "				return x.weight * 10\n"
//                    + "			});\n"
//                    + "			force.start();\n"
//                    + "			var force2 = d3.layout.force().nodes(labelAnchors).links(labelAnchorLinks).gravity(0).linkDistance(0).linkStrength(8).charge(-100).size([w, h]);\n"
//                    + "			force2.start();\n"
//                    + "			var link = vis.selectAll(\"line.link\").data(links).enter().append(\"svg:line\").attr(\"class\", \"link\").style(\"stroke\", \"#CCC\");\n"
//                    + "			var node = vis.selectAll(\"g.node\").data(force.nodes()).enter().append(\"svg:g\").attr(\"class\", \"node\");\n"
//                    + "			node.append(\"svg:circle\").attr(\"r\", 5).style(\"fill\", \"#555\").style(\"stroke\", \"#FFF\").style(\"stroke-width\", 3);\n"
//                    + "			node.call(force.drag);\n"
//                    + "			var anchorLink = vis.selectAll(\"line.anchorLink\").data(labelAnchorLinks)//.enter().append(\"svg:line\").attr(\"class\", \"anchorLink\").style(\"stroke\", \"#999\");\n"
//                    + "			var anchorNode = vis.selectAll(\"g.anchorNode\").data(force2.nodes()).enter().append(\"svg:g\").attr(\"class\", \"anchorNode\");\n"
//                    + "			anchorNode.append(\"svg:circle\").attr(\"r\", 0).style(\"fill\", \"#FFF\");\n"
//                    + "				anchorNode.append(\"svg:text\").text(function(d, i) {\n"
//                    + "				return i % 2 == 0 ? \"\" : d.node.label\n"
//                    + "			}).style(\"fill\", \"#555\").style(\"font-family\", \"Arial\").style(\"font-size\", 12);\n"
//                    + "			var updateLink = function() {\n"
//                    + "				this.attr(\"x1\", function(d) {\n"
//                    + "					return d.source.x;\n"
//                    + "				}).attr(\"y1\", function(d) {\n"
//                    + "					return d.source.y;\n"
//                    + "				}).attr(\"x2\", function(d) {\n"
//                    + "					return d.target.x;\n"
//                    + "				}).attr(\"y2\", function(d) {\n"
//                    + "					return d.target.y;\n"
//                    + "				});\n"
//                    + "			}\n"
//                    + "			var updateNode = function() {\n"
//                    + "				this.attr(\"transform\", function(d) {\n"
//                    + "					return \"translate(\" + d.x + \",\" + d.y + \")\";\n"
//                    + "				});\n"
//                    + "			}\n"
//                    + "			force.on(\"tick\", function() {\n"
//                    + "				force2.start();\n"
//                    + "				node.call(updateNode);\n"
//                    + "				anchorNode.each(function(d, i) {\n"
//                    + "					if(i % 2 == 0) {\n"
//                    + "						d.x = d.node.x;\n"
//                    + "						d.y = d.node.y;\n"
//                    + "					} else {\n"
//                    + "						var b = this.childNodes[1].getBBox();\n"
//                    + "						var diffX = d.x - d.node.x;\n"
//                    + "						var diffY = d.y - d.node.y;\n"
//                    + "						var dist = Math.sqrt(diffX * diffX + diffY * diffY);\n"
//                    + "						var shiftX = b.width * (diffX - dist) / (dist * 2);\n"
//                    + "						shiftX = Math.max(-b.width, Math.min(0, shiftX));\n"
//                    + "						var shiftY = 5;\n"
//                    + "						this.childNodes[1].setAttribute(\"transform\", \"translate(\" + shiftX + \",\" + shiftY + \")\");\n"
//                    + "					}\n"
//                    + "				});\n"
//                    + "				anchorNode.call(updateNode);\n"
//                    + "				link.call(updateLink);\n"
//                    + "				anchorLink.call(updateLink);\n"
//                    + "			});\n"
//                   out.println("		</script>");
            out.println("</body>");
            out.println("</html>");
        } catch (SQLException ex) {
        } finally {
            out.close();
        }
    }

    private void draw(PrintWriter out, int radiuos) {
        out.println("<p>\n"
                + "  <label for=\"nRadius\" \n"
                + "         style=\"display: inline-block; width: 240px; text-align: right\">\n"
                + "         radius = <span id=\"nRadius-value\">…</span>\n"
                + "  </label>\n"
                + "  <input type=\"range\" min=\"1\" max=\"150\" id=\"nRadius\">\n"
                + "</p>\n"
                + "\n"
                + "<script src=\"http://d3js.org/d3.v3.min.js\"></script>\n"
                + "<script>\n"
                + "\n"
                + "var width = 600;\n"
                + "var height = 300;\n"
                + " \n"
                + "var holder = d3.select(\"body\")\n"
                + "      .append(\"svg\")\n"
                + "      .attr(\"width\", width)    \n"
                + "      .attr(\"height\", height); \n"
                + "\n"
                + "// draw the circle\n"
                + "holder.append(\"circle\")\n"
                + "  .attr(\"cx\", 300)\n"
                + "  .attr(\"cy\", 150) \n"
                + "  .style(\"fill\", \"none\")   \n"
                + "  .style(\"stroke\", \"blue\") \n"
                + "  .attr(\"r\", 120);\n"
                + "\n"
//                + "// when the input range changes update the circle \n"
//                + "d3.select(\"#nRadius\").on(\"input\", function() {\n"
//                + "  update(+this.value);\n"
//                + "});\n"
                + "\n"
                + "// Initial starting radius of the circle \n"
                + "update(120);\n"
                + "\n"
                + "// update the elements\n"
                + "function update(nRadius) {\n"
                + "\n"
                + "  // adjust the text on the range slider\n"
//                + "  d3.select(\"#nRadius-value\").text(nRadius);\n"
//                + "  d3.select(\"#nRadius\").property(\"value\", nRadius);\n"
                + "\n"
                + "  // update the rircle radius\n"
                + "  holder.selectAll(\"circle\") \n"
                + "    .attr(\"r\", nRadius);\n"
                + "}\n"
                + "\n"
                + "</script>");
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
