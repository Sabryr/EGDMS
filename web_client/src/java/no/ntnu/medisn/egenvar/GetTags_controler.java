/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package no.ntnu.medisn.egenvar;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

/**
 *
 * @author sabryr
 */
public class GetTags_controler implements Runnable {

    private String pmids;
    private StringBuilder buffer;
    private boolean isdone;
    private String server_root;
    private int isDoneAttepmts=100;

    public GetTags_controler(String pmids, String server_root) {
        this.pmids = pmids;
        this.server_root = server_root;
        buffer = new StringBuilder();
    }

    @Override
    public void run() {
        isdone = false;
        investigateTag(pmids);
        isdone = true;
    }

    public boolean isDone() {
        isDoneAttepmts--;
        if(isDoneAttepmts<0){
            isdone=true;
            buffer.append("Error: Timed out");
            
        }
        return isdone;
    }

    public StringBuilder getResults() {
        while (!isdone) {
        }
        return buffer;
    }

    private void investigateTag(String url) {
        HashMap<String, String> name2hash = Constants.get_name2hash();
        HashMap<String, String> hash2table = Constants.get_hash2table();

        String[] url_a = url.split("\\|");
        buffer = new StringBuilder();
        for (int i = 0; i < url_a.length; i++) {
            url = "http://www.ncbi.nlm.nih.gov/pubmed/" + url_a[i] + "?report=abstract&format=text";

            buffer.append(htmlet(url_a[i], url, name2hash, hash2table));
        }
    }

    private StringBuilder htmlet(String refid, String newurl,
            HashMap<String, String> name2hash, HashMap<String, String> hash2table) {
        HashMap<String, ArrayList<String>> taf_map = new HashMap<>();
        taf_map.put(refid, new ArrayList<String>());
        StringBuilder abstr = new StringBuilder();
        try {
            try {
                URL url = new URL(newurl);
                try {
                    Scanner scan = new Scanner(url.openStream());
                    boolean auth_info_found = false;
                    boolean abstr_found = false;
                    abstr.append("<p>");
                    while (scan.hasNext()) {
                        String line = scan.nextLine();
                        if (abstr_found) {
                            String[] line_a = line.split("\\s");
                            HashMap<Integer, String> tags_map = new HashMap<>();
                            for (int i = 0; i < (line_a.length - 3); i++) {
                                String current = line_a[i].toUpperCase();
                                String next1 = line_a[i + 1].toUpperCase();
                                String next2 = line_a[i + 2].toUpperCase();
                                if (name2hash.containsKey(current)) {
                                    String chash = name2hash.get(current);
                                    tags_map.put(i, hash2table.get(chash) + "|HASH=" + chash);

                                }
                                if (name2hash.containsKey(next1)) {
                                    String chash = name2hash.get(next1);
                                    tags_map.put(i + 1, hash2table.get(chash) + "|HASH=" + chash);
                                }
                                if (name2hash.containsKey(next2)) {
                                    String chash = name2hash.get(next2);
                                    tags_map.put(i + 2, hash2table.get(chash) + "|HASH=" + chash);
                                }
                                if (name2hash.containsKey(current + " " + next1)) {
                                    String chash = name2hash.get(current + " " + next1);
                                    tags_map.put(i, hash2table.get(chash) + "|HASH=" + chash);
                                    tags_map.put(i + 1, hash2table.get(chash) + "|HASH=" + chash);
                                }
                                if (name2hash.containsKey(current + " " + next1 + " " + next2)) {
                                    String chash = name2hash.get(current + " " + next1 + " " + next2);
                                    tags_map.put(i, hash2table.get(chash) + "|HASH=" + chash);
                                    tags_map.put(i + 1, hash2table.get(chash) + "|HASH=" + chash);
                                    tags_map.put(i + 2, hash2table.get(chash) + "|HASH=" + chash);
                                }
                            }
//https://ans-180230.stolav.ntnu.no:8185/eGenVar_web/Search/SearchResults3?TABLETOUSE_SOFTWARE_ONTOLOGY_TAGSOURCE|HASH=40d30d675bc65998802a1b65e87d9181046e8a7d
//                            
                            for (int i = 0; i < line_a.length; i++) {
                                if (tags_map.containsKey(i)) {
                                    abstr.append("<h8><a href=\"" + server_root + "Search/SearchResults3?TABLETOUSE_" + tags_map.get(i) + "\">");
                                    abstr.append(line_a[i]);
                                    abstr.append("</a></h8>");
                                    abstr.append(" ");
                                } else {
                                    abstr.append(line_a[i]);
                                    abstr.append(" ");
                                }
                            }
                            abstr.append("\n");

                        } else if (!auth_info_found && line.startsWith("AUTHOR INFORMATIONS")) {
                            auth_info_found = true;
                        } else if (line.trim().isEmpty()) {
                            abstr_found = true;
                        }
                    }
                    scan.close();
                    abstr.append("</p>");
                } catch (IOException ex) {
                    System.out.println("Error 33.2a:" + ex.getMessage());
                }
            } catch (MalformedURLException ex) {
                System.out.println("Error 33.2b:" + ex.getMessage());
            }
        } catch (Exception ex) {
//            ex.printStackTrace();
            System.out.println("Error 33.2c:" + ex.getMessage());
        }
        return abstr;
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
//                    System.out.println("2037 connection closed");
                } catch (SQLException e) {
                }
            }
        } catch (SQLException e) {
        }
    }
}
