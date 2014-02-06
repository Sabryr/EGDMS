package no.ntnu.medisin.egenvar.server;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import javax.swing.JOptionPane;

/**
 ************************************************************************
 * Copyright (C) 2008 Ian Donaldson This file is part of iRefIndex: iRefIndex: A
 * consolidated protein interaction database with provenance iRefIndex is free
 * software: you can redistribute it and/or modify it under the terms of the GNU
 * General Public License as published by the Free Software Foundation, either
 * version 3 of the License, or any later version. This program is distributed
 * in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even
 * the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU General Public License for more details. You should have received
 * a copy of the GNU General Public License along with this program. If not, see
 * <http://www.gnu.org/licenses/>. PROGRAM: iRefIndex. AUTHORS: Sabry Razick and
 * Ian Donaldson DESCRIPTION: This software collects, consolidates, analayzes
 * and distributes biomolecular interaction data. See http://irefindex.uio.no
 * for more details. REVISION/CONTRIBUTION NOTES: July 10, 2008 Release 1.1.
 * CONTACT: Ian Donaldson Biotechnology Centre of Oslo, P.O. Box 1125, Blindern,
 * 0317, Oslo, Norway. ian.donaldson@biotek.uio.no http://iRefIndex.uio.no
 * PUBLICATION TO CITE:PMID=18823568 iRefIndex: A consolidated protein
 * interaction database with provenance Sabry Razick, George Magklaras and Ian M
 * Donaldson, BMC Bioinformatics. 2008 Sep 30;9:405
 *
 */
public class PathFinder10 {

    private ArrayList<ArrayList<Integer>> selected_patth_ll;
    private ArrayList<ArrayList<Integer>> optimal_pathe_ll;
    private ArrayList<Integer> targets_nehgbours_l;
    private ArrayList<Integer> garbage_sead_l;
    int c_min = 999999999;
    private HashMap<Integer, ArrayList<Integer>> nodemap_bkup;
    public static boolean negetived = false;
    private boolean colpseHubs;
    private boolean expand;
    private int maximum_distance = 99;
    private final int MAX_SIGNIFICANT_HUB_CUT_OFF = 999;
    private int numberOfLargehubs = 0;
    private double avgOfHubs = 0;
    private final int WARNING_HUB_MARGIN = 2294;
    private final int WARNING_HUB_size = 200;
    private int ignor_bigHub_lastDecision = 0;
    public static boolean force_kill_path = false;
    private boolean revers_recomended = false;
    private static boolean firstResult_returned = false;

    public PathFinder10() {
        selected_patth_ll = new ArrayList<ArrayList<Integer>>(100);
        optimal_pathe_ll = new ArrayList<ArrayList<Integer>>(100);
        targets_nehgbours_l = new ArrayList<Integer>(100);
        garbage_sead_l = new ArrayList<Integer>();
        ignor_bigHub_lastDecision = 0;
        numberOfLargehubs = 0;
        avgOfHubs = 0;
        force_kill_path = false;
    }

    public void reSet() {
        selected_patth_ll = new ArrayList<ArrayList<Integer>>(100);
        optimal_pathe_ll = new ArrayList<ArrayList<Integer>>(100);
        targets_nehgbours_l = new ArrayList<Integer>(100);
        garbage_sead_l = new ArrayList<Integer>();
        ignor_bigHub_lastDecision = 0;
        numberOfLargehubs = 0;
        avgOfHubs = 0;
        force_kill_path = false;
    }

    public static void main(String[] args) {
    }

    public ArrayList<ArrayList<String>> getPaths(HashMap<String, ArrayList<String>> connection_map, String start, String stop, int hubColLimit) {
        ArrayList<String> key_l = new ArrayList<String>(connection_map.keySet());
        HashMap<Integer, ArrayList<Integer>> transformed_map = new HashMap<Integer, ArrayList<Integer>>();
        int start_int = -1;
        int stop_int = -1;
        for (int i = 0; i < key_l.size(); i++) {
            ArrayList<String> c_nodes_l = connection_map.get(key_l.get(i));
            if (c_nodes_l != null) {
                ArrayList<Integer> trans_nodes_l = new ArrayList<Integer>(c_nodes_l.size());
                for (int j = 0; j < c_nodes_l.size(); j++) {
                    int tr_val = key_l.indexOf(c_nodes_l.get(j));
                    if (tr_val >= 0) {
                        if (!trans_nodes_l.contains(tr_val)) {
                            trans_nodes_l.add(tr_val);
                        }
                    } else {
                        key_l.add(c_nodes_l.get(j));
                        tr_val = key_l.indexOf(c_nodes_l.get(j));
                        if (!trans_nodes_l.contains(tr_val)) {
                            trans_nodes_l.add(tr_val);
                        }
                    }
                }
                transformed_map.put(i, trans_nodes_l);
            }
        }
        for (int i = 0; i < key_l.size(); i++) {
            if (start.equals(key_l.get(i))) {
                start_int = i;
            } else if (stop.equals(key_l.get(i))) {
                stop_int = i;
            }
        }

        ArrayList<Integer> transformed_map_key_list = new ArrayList<Integer>(transformed_map.keySet());
        for (int i = 0; i < transformed_map_key_list.size(); i++) {
            ArrayList<Integer> neigb_list = transformed_map.get(transformed_map_key_list.get(i));
            for (int j = 0; j < neigb_list.size(); j++) {
                if (transformed_map.get(neigb_list.get(j)) == null) {
                    ArrayList<Integer> tmp = new ArrayList<Integer>(1);
                    tmp.add(transformed_map_key_list.get(i));
                    transformed_map.put(neigb_list.get(j), tmp);
//                    neigb_list.add(neigb_list.get(j));
                } else {
                    if (!transformed_map.get(neigb_list.get(j)).contains(transformed_map_key_list.get(i))) {
                        transformed_map.get(neigb_list.get(j)).add(transformed_map_key_list.get(i));
                    }
                }
            }
        }
        if (start.equals(stop)) {
            ArrayList<ArrayList<String>> dummy_ll = new ArrayList<ArrayList<String>>(1);
            ArrayList<String> dummy_l = new ArrayList<String>(1);
            dummy_l.add(start);
            dummy_ll.add(dummy_l);
            return dummy_ll;
        } else {
            ArrayList<Integer> avoid_l = new ArrayList<Integer>(1);
            int hub_cutoff = 999;
            boolean blok_superErode_neighbors = true;
            find_REDUNT_(transformed_map, start_int, stop_int, hubColLimit, colpseHubs, expand, avoid_l, hub_cutoff, blok_superErode_neighbors);
            ArrayList<ArrayList<String>> selected_patth_string_ll = new ArrayList<ArrayList<String>>(selected_patth_ll.size());
            for (int i = 0; i < selected_patth_ll.size(); i++) {
                ArrayList<Integer> c_list = selected_patth_ll.get(i);
                ArrayList<String> c_trans_list = new ArrayList<String>(c_list.size());
                for (int j = 0; j < c_list.size(); j++) {
                    c_trans_list.add(key_l.get(c_list.get(j)));
                }
                selected_patth_string_ll.add(c_trans_list);
            }

            return selected_patth_string_ll;

        }
    }

    public ArrayList<ArrayList<Integer>> getPaths(int[][] edges, int start, int stop, int hubColLimit, boolean negetived, boolean colpseHubs, boolean expand, ArrayList<Integer> avoid_l, int maximum_distance, int hub_cutoff, boolean blok_superErode_neighbors) {
        reSet();
        this.maximum_distance = maximum_distance;
//        this.minimum_distance = minimum_distance;
        this.colpseHubs = colpseHubs;
        this.expand = expand;
        PathFinder10.negetived = negetived;

        if (negetived) {
            start = start * (-1);
            stop = stop * (-1);
        }
        if (start == stop) {
            ArrayList<ArrayList<Integer>> dummy_ll = new ArrayList<ArrayList<Integer>>(1);
            ArrayList<Integer> dummy_l = new ArrayList<Integer>(1);
            dummy_l.add(start);
            dummy_ll.add(dummy_l);
            return dummy_ll;
        } else {
            find_REDUNT(edges, start, stop, hubColLimit, colpseHubs, expand, avoid_l, hub_cutoff, blok_superErode_neighbors);
            if (negetived) {
                ArrayList<ArrayList<Integer>> selected_patth_negated_ll = new ArrayList<ArrayList<Integer>>(selected_patth_ll.size());
                for (int i = 0; i < selected_patth_ll.size(); i++) {
                    ArrayList<Integer> tmp = selected_patth_ll.remove(i);
                    ArrayList<Integer> negatedt_l = new ArrayList<Integer>(tmp.size());
                    for (int j = 0; j < tmp.size(); j++) {
                        negatedt_l.add(tmp.get(j) * (-1));
                    }
                    selected_patth_negated_ll.add(negatedt_l);
                }
                return selected_patth_negated_ll;
            } else {
                return selected_patth_ll;
            }

        }

    }

    public HashMap<Integer, ArrayList<Integer>> find_REDUNT(int[][] edges, int start, int stop, int hub_threshld, boolean colpseHubs, boolean expand, ArrayList<Integer> avoid_l, int hub_cutoff, boolean blok_superErode_neighbors) {
        HashMap<Integer, ArrayList<Integer>> nodemap = GetNodemap(edges, true);
        return find_REDUNT_(nodemap, start, stop, hub_threshld, colpseHubs, expand, avoid_l, hub_cutoff, blok_superErode_neighbors);

    }

    public HashMap<Integer, ArrayList<Integer>> find_REDUNT_(HashMap<Integer, ArrayList<Integer>> nodemap, int start, int stop, int hub_threshld, boolean colpseHubs, boolean expand, ArrayList<Integer> avoid_l, int hub_cutoff, boolean blok_superErode_neighbors) {

        if (nodemap.containsKey(start) && nodemap.containsKey(stop)) {
            for (int i = 0; i < avoid_l.size(); i++) {
                nodemap.remove(avoid_l.get(i));
            }
            if (hub_cutoff < MAX_SIGNIFICANT_HUB_CUT_OFF) {
                ArrayList<Integer> map_keys = new ArrayList<Integer>(nodemap.keySet());
                for (int i = 0; i < map_keys.size(); i++) {
                    if (nodemap.get(map_keys.get(i)).size() >= hub_cutoff) { //&& !nodemap.get(map_keys.get(i)).contains(start) && !nodemap.get(map_keys.get(i)).contains(stop)
                        if (map_keys.get(i) != start && map_keys.get(i) != stop && !(nodemap.get(map_keys.get(i)).contains(start)) && !(nodemap.get(map_keys.get(i)).contains(stop))) {
                            nodemap.remove(map_keys.get(i));
                        }
                    }

                }
            }
            resolve(nodemap, start, stop, hub_threshld, true, colpseHubs, blok_superErode_neighbors);
            if (expand && !force_kill_path) {
                expand_collapsed(hub_threshld);
            }
            return null;
        }
        return null;
    }

    private void resolve(HashMap<Integer, ArrayList<Integer>> nodemap, Integer start, Integer stop, int hub_threshld, boolean initial, boolean colpseHubs, boolean blok_superErode_neighbors) {

        //System.out.println("Path filling initializing. size of network=" + nodemap.size() + " start=" + start + " stop=" + stop);

        revers_recomended = false;
        if (!force_kill_path) {
            //System.out.println("Path filling erosion step 1. size of network=" + nodemap.size() + " start=" + start + " stop=" + stop);

            nodemap = eroder(nodemap, start, stop);
        }
        //System.out.println("Size of sub-network after erosion step 1=" + nodemap.size() );
        if (!force_kill_path) {
            //System.out.println("Analysing for alternative starting points=" + nodemap.size() + " start=" + start + " stop=" + stop);
            nodemap = stratStopReversalCheck(nodemap, start, stop);
        }
        int original_start = start;
        int original_stop = stop;
        if (revers_recomended) {
            start = stop;
            stop = original_start;

        }

        //System.out.println("Size of sub-network after step 2=" + nodemap.size() );
        //System.out.println("number of hubs larger than 200 connections=" + numberOfLargehubs );
        if (ignor_bigHub_lastDecision == 0 && (numberOfLargehubs > WARNING_HUB_MARGIN)) {
            Object[] possibleValues = {"1) Avoid this hubs and locate a path", "2) Proceed with current operation using existing settings", "3) Quit, change parameters and search again"};
//            if (selectedValue.contains("1)")) {
//                ignor_bigHub_lastDecision = 1;
//            } else if (selectedValue.contains("2)")) {
            ignor_bigHub_lastDecision = 2;
//            } else {
//                ignor_bigHub_lastDecision = 3;
//            }
        }

        if (ignor_bigHub_lastDecision == 1) {
            ignor_bigHub_lastDecision = 2;
            ArrayList<Integer> map_keys = new ArrayList<Integer>(nodemap.keySet());
            for (int i = 0; i < map_keys.size(); i++) {
                if (nodemap.get(map_keys.get(i)).size() >= WARNING_HUB_size) {
                    if (map_keys.get(i) != start && map_keys.get(i) != stop && !(nodemap.get(map_keys.get(i)).contains(start)) && !(nodemap.get(map_keys.get(i)).contains(stop))) {
                        nodemap.remove(map_keys.get(i));
                    }
                }
            }
            nodemap = stratStopReversalCheck(nodemap, start, stop);
        }
        if (ignor_bigHub_lastDecision == 0 || ignor_bigHub_lastDecision == 1 || ignor_bigHub_lastDecision == 2) {
            if (!force_kill_path) {
                //System.out.println("Path filling supper erosion step 4. size of network=" + nodemap.size() + " start=" + start + " stop=" + stop);
                nodemap = superErode_values(nodemap);
            }

            if (!force_kill_path) {
                //System.out.println("Path filling neighbour trimming step 5. size of network=" + nodemap.size() + " start=" + start + " stop=" + stop);
                nodemap = superErode_neighbors(nodemap, start, stop);
            }

            if (initial && !force_kill_path) {
                nodemap_bkup = safecopyMap(nodemap);
            }
            if (!force_kill_path) {
                start = original_start;
                stop = original_stop;
                ArrayList<Integer> maxs = findHub(nodemap, start, stop, hub_threshld);
                boolean removed = maxs.remove(start);
                removed = maxs.remove(stop);
                ArrayList<Integer> sead_l = new ArrayList<Integer>();
                ArrayList<Integer> utilized_sead_l = new ArrayList<Integer>();
                sead_l.add(start);
                ArrayList<ArrayList<Integer>> patth_ll = new ArrayList<ArrayList<Integer>>(500);
                patth_ll.add(new ArrayList(sead_l));
                boolean complete = false;
                int rounds = 0;
                while (!complete || force_kill_path) {
                    rounds++;
                    complete = true;
                    int init_size = sead_l.size();
                    ArrayList<Integer> tmpsead_l = new ArrayList<Integer>(50);
                    if (colpseHubs && !force_kill_path) {
                        ArrayList<Integer> maxs_cl = new ArrayList<Integer>(maxs);
                        if (sead_l != null) {
                            maxs_cl.retainAll(sead_l);
                            if (maxs_cl.size() > 0) {
                                //System.out.println("Collapsing hubs . size of network=" + nodemap.size() + " Hubs =" + maxs_cl.size());
                                collapHubs(nodemap, maxs_cl, start, stop, hub_threshld);
                                maxs.removeAll(maxs_cl);
                            }
                        }
                    }

                    for (int j = 0; (j < sead_l.size() && !force_kill_path); j++) {
                        Integer c_sead = sead_l.get(j);
                        ArrayList<Integer> sead_partnn_l = null;
                        if (!force_kill_path) {
                            if (c_sead == start || c_sead == stop) {
                                sead_partnn_l = nodemap.get(c_sead);
                            } else {
                                sead_partnn_l = nodemap.remove(c_sead);
                            }
                        }

                        if (sead_partnn_l != null && sead_partnn_l.size() > 0) {
                            if (sead_partnn_l.contains(stop)) {
                                sead_partnn_l.clear();
                                sead_partnn_l.add(stop);
                            }
                            sead_partnn_l.removeAll(targets_nehgbours_l);
                            tmpsead_l.removeAll(targets_nehgbours_l);
                            tmpsead_l.removeAll(sead_partnn_l);
                            ArrayList<Integer> c_sead_part_l = new ArrayList<Integer>(10);
                            boolean usefull = false;
                            for (int k = 0; (k < sead_partnn_l.size() && !force_kill_path); k++) {
                                int c_sead_part = sead_partnn_l.get(k);
                                if ((!sead_l.contains(c_sead_part) && !garbage_sead_l.contains(c_sead_part)) || c_sead_part == stop) {
                                    c_sead_part_l.add(c_sead_part);
                                    usefull = true;
                                    tmpsead_l.add(c_sead_part);
//                                    if (c_sead_part == stop) {
//                                    }
                                }
                            }
                            if (usefull) {
                                if (!utilized_sead_l.contains(c_sead)) {
                                    utilized_sead_l.add(c_sead);
                                    //System.out.println("Distance=" + rounds + ". Expanding seed list . size of used seed list=" + utilized_sead_l.size());
                                    if ((rounds * 5 < 99)) {
                                    } else {
                                    }

                                }

                            } else {
                                if (c_sead != stop && c_sead != start) {
                                    garbage_sead_l.add(c_sead);
                                    //                            if (!utilized_sead_l.contains(c_sead)) {
                                    patth_ll = remove_garbage(patth_ll, c_sead);
                                    nodemap.keySet().remove(c_sead);
                                    //                            }
                                }

                            }
                            if (c_sead_part_l.size() > 0) {
                                patth_ll = addtopath(nodemap, patth_ll, c_sead, c_sead_part_l, start, stop);
                            }

                        } else {
                            tmpsead_l.remove(c_sead);
                        }
                    }
                    tmpsead_l.removeAll(targets_nehgbours_l);
                    sead_l.removeAll(tmpsead_l);
                    sead_l.addAll(tmpsead_l);//
                    sead_l.removeAll(targets_nehgbours_l);
                    tmpsead_l.clear();

                    if (sead_l.size() > init_size && !force_kill_path && (rounds < maximum_distance)) {
                        complete = false;
                    }
                    sead_l.removeAll(garbage_sead_l);
                    //System.out.println("Expanding paths network=" + nodemap.size() + ". Unique partial paths =" + patth_ll.size());
                }
                patth_ll = clean(patth_ll, stop);

            }

        } else {
        }
        /*
         * if (!force_kill_path) { //System.out.println("Path filling erosion
         * step 2. size of network=" + nodemap.size() + " start=" + start + "
         * stop=" + stop); taskMonitor.setPercentCompleted(-1);
         * nodemap.keySet().retainAll(superErode_keys(nodemap, start, stop)); }
         * if (!force_kill_path) { //System.out.println("Path filling erosion
         * step 3. size of network=" + nodemap.size() + " start=" + start + "
         * stop=" + stop); taskMonitor.setPercentCompleted(-1);
         * nodemap.keySet().retainAll(superErode_keys(nodemap, stop, start)); }
         */



    }

    private ArrayList<ArrayList<Integer>> addtopath(HashMap<Integer, ArrayList<Integer>> nodemap, ArrayList<ArrayList<Integer>> patth_ll, int c_sead, ArrayList<Integer> c_sead_part_l, int start, int stop) {
        ArrayList<ArrayList<Integer>> tmp_path_ll = new ArrayList<ArrayList<Integer>>(100);
        for (int i = 0; i < patth_ll.size(); i++) {
            ArrayList<Integer> path_l = patth_ll.get(i);
            for (int h = 0; h < c_sead_part_l.size(); h++) {
                Integer c_sead_part = c_sead_part_l.get(h);
                int sead_index = path_l.indexOf(c_sead);
                boolean dupe = false;
                if (sead_index > 0 || (sead_index == 0 && c_sead == start)) {
                    ArrayList<Integer> sub_path_l = new ArrayList(path_l.subList(0, sead_index + 1));
                    if (!sub_path_l.contains(c_sead_part) || c_sead_part == stop) {
                        if (!sub_path_l.contains(c_sead_part)) {
                            sub_path_l.add(c_sead_part);
                        }
                        for (int j = 0; j < tmp_path_ll.size(); j++) {
                            ArrayList<Integer> tmp_path_tmp_l = tmp_path_ll.get(j);
                            if (tmp_path_tmp_l.containsAll(sub_path_l)) {
                                dupe = true;
                                j = tmp_path_ll.size(); // exit loop
                            }
                        }
                        if (!dupe) {
                            for (int j = 0; j < patth_ll.size(); j++) {
                                ArrayList<Integer> tmp_path_tmp_l = patth_ll.remove(j);
                                if (sub_path_l.containsAll(tmp_path_tmp_l)) {
                                    j--;
                                    if (i >= j && i > 0) {
                                        i--;
                                    }
                                } else {
                                    patth_ll.add(j, tmp_path_tmp_l);
                                }
                            }
                        }
                        boolean better_exists = false;
                        for (int j = 0; j < optimal_pathe_ll.size(); j++) {
                            if (sub_path_l.containsAll(optimal_pathe_ll.get(j))) {
                                better_exists = true;
                            }
                        }
                        if (!dupe && !better_exists) {
                            tmp_path_ll.add(sub_path_l);
                            tmp_path_ll = analyze(nodemap, tmp_path_ll, start, stop);
                        }
                    }
                }
            }
        }

        patth_ll.addAll(tmp_path_ll);
        tmp_path_ll.clear();
        return patth_ll;
    }

    private ArrayList<ArrayList<Integer>> analyze(HashMap<Integer, ArrayList<Integer>> nodemap, ArrayList<ArrayList<Integer>> patth_ll, Integer start, Integer stop) {
        c_min = 999999;
        for (int i = 0; i < patth_ll.size(); i++) {
            if (patth_ll.get(i).contains(start) && patth_ll.get(i).contains(stop)) {
                ArrayList<Integer> tmp = new ArrayList<Integer>();
                tmp.addAll(patth_ll.remove(i));
                if (i > 0) {
                    i--;
                }
                boolean already_found = false;
                for (int j = 0; j < selected_patth_ll.size(); j++) {
                    if (selected_patth_ll.get(j).containsAll(tmp)) {
                        already_found = true;
                        j = selected_patth_ll.size();
                    }
                }
                if (!already_found) {
                    selected_patth_ll.add(new ArrayList<Integer>(tmp));
                    //System.out.println("Adding new complete path =" + tmp + ". Unique partial paths =" + selected_patth_ll.size());
                    //System.out.println("Path="+tmp);
                    tmp.remove(stop);
                    if (!targets_nehgbours_l.contains(tmp.get(tmp.size() - 1))) {
                        targets_nehgbours_l.add(tmp.get(tmp.size() - 1));
                    }
                    optimal_pathe_ll.add(tmp);
                }

            }
        }
        return patth_ll;
    }

    private ArrayList<ArrayList<Integer>> clean(ArrayList<ArrayList<Integer>> patth_ll, int stop) {
        for (int i = 0; i < patth_ll.size(); i++) {
            ArrayList<Integer> tmp_path_l = patth_ll.get(i);
            if (tmp_path_l.get(tmp_path_l.size() - 1) != stop) {
                tmp_path_l = patth_ll.remove(i);
                i--;
            }
        }
        return patth_ll;
    }

    private ArrayList<ArrayList<Integer>> remove_garbage(ArrayList<ArrayList<Integer>> patth_ll, int garbage) {
        for (int i = 0; i < patth_ll.size(); i++) {
            ArrayList<Integer> tmp_path_l = patth_ll.get(i);
            if (tmp_path_l.get(tmp_path_l.size() - 1) == garbage) {
                tmp_path_l = patth_ll.remove(i);
                i--;
            }
        }
        return patth_ll;

    }

    public HashMap<Integer, ArrayList<Integer>> eroder(HashMap<Integer, ArrayList<Integer>> nodemap, int start, int stop) {
        if (nodemap.containsKey(stop) && nodemap.containsKey(stop)) {
            ArrayList<Integer> map_keys = new ArrayList(nodemap.keySet());
            boolean imporve = true;

            while (imporve && !force_kill_path) {
                imporve = false;
                int init_size = nodemap.size();
                for (int i = 0; i < map_keys.size(); i++) {
                    if (i % 10 == 1) {
                        //System.out.println("Processing -> " + i);
                    }

                    Integer c_key = map_keys.remove(i);
                    i--;
                    if (c_key != start && c_key != stop && (nodemap.get(c_key) != null) && nodemap.get(c_key).size() == 1) {
                        if (nodemap.containsKey(nodemap.get(c_key).get(0))) {
                            nodemap.get(nodemap.get(c_key).get(0)).remove(c_key); //only one elment (A nodemap.get(c_key).size() == 1) so , get the first (index 0) and remove it from map
                            if (nodemap.get(nodemap.get(c_key).get(0)).size() < 2) { // if the number of links 1 or zero (less than 2) this should be lined up for elimination, in a subsequent round
                                if (!map_keys.contains(nodemap.get(c_key).get(0))) { //The reson for this to be added to the map_keys is that, this may have been cheked earlier, and then it may have had more links.
                                    map_keys.add(nodemap.get(c_key).get(0));
                                }
                            }
                        }
                        nodemap.remove(c_key);
                    } else if (c_key != start && c_key != stop && ((nodemap.get(c_key) == null) || (nodemap.get(c_key).size() == 0))) {
                        nodemap.remove(c_key);
                    } else {
                        if (nodemap.containsKey(c_key)) {
                            nodemap.get(c_key).removeAll(garbage_sead_l);
                            ArrayList<Integer> start_members = new ArrayList<Integer>(nodemap.get(start));
                            nodemap.get(c_key).retainAll(nodemap.keySet());
                            nodemap.put(start, start_members);
                        }
                    }
                }
                if (init_size > nodemap.size()) {
                    imporve = true;
                }
            }

            return nodemap;
        } else if (nodemap.containsKey(start)) {
            HashMap<Integer, ArrayList<Integer>> blank_nodemap = new HashMap<Integer, ArrayList<Integer>>(1);
            blank_nodemap.put(start, nodemap.get(start));
            return blank_nodemap;
        } else if (nodemap.containsKey(stop)) {
            HashMap<Integer, ArrayList<Integer>> blank_nodemap = new HashMap<Integer, ArrayList<Integer>>(1);
            blank_nodemap.put(stop, nodemap.get(stop));
            return blank_nodemap;
        } else {
            HashMap<Integer, ArrayList<Integer>> blank_nodemap = new HashMap<Integer, ArrayList<Integer>>(1);
            blank_nodemap.put(0, new ArrayList<Integer>(1));
            return blank_nodemap;
        }


    }

    private HashMap<Integer, ArrayList<Integer>> stratStopReversalCheck(HashMap<Integer, ArrayList<Integer>> nodemap, int start, int stop) {
        HashMap<Integer, ArrayList<Integer>> farward_nodemap = new HashMap<Integer, ArrayList<Integer>>();
        HashMap<Integer, ArrayList<Integer>> backward_nodemap = new HashMap<Integer, ArrayList<Integer>>();
//        int iniitalMapSize = nodemap.size();

        int sead_size_if_start = 0;
        int sead_size_if_stop = 0;
        ArrayList<Integer> sead_l = new ArrayList<Integer>(10);
        sead_l.add(start);
        boolean terminate = false;
        int distance_farward = 0;
        int distance_backwards = 0;
        boolean stop_found = false;
        int numberOf200Hubs_farward = 0;
        int numberOf200Hubs_backward = 0;
        int totOfHubs_farward = 0;
        int totOfHubs_backward = 0;
        double avgOfHubs_farward = 0;
        double avgOfHubs_backward = 0;
        while (!terminate && !force_kill_path) {
            distance_farward++;
            ArrayList<Integer> new_sead_l = new ArrayList<Integer>(50);
            for (int j = 0; j < sead_l.size(); j++) {
                if (nodemap.get(sead_l.get(j)) != null) {
                    new_sead_l.removeAll(nodemap.get(sead_l.get(j)));
                    new_sead_l.addAll(nodemap.get(sead_l.get(j)));
                    farward_nodemap.put(sead_l.get(j), nodemap.get(sead_l.get(j)));
                    int c_size = nodemap.get(sead_l.get(j)).size();
                    totOfHubs_farward = totOfHubs_farward + c_size;
                    avgOfHubs_farward++;
                    if (c_size > WARNING_HUB_size) {
                        numberOf200Hubs_farward++;
                    }
                }
            }
            new_sead_l.removeAll(sead_l);
            sead_l.clear();
            sead_l.addAll(new_sead_l);
            //System.out.println(" Seed list size=" + sead_l.size() + " Number of hubs larger than " + WARNING_HUB_size + " = " + numberOf200Hubs_farward);
            if (sead_l.isEmpty() || sead_l.contains(stop)) {
                if (sead_l.contains(stop)) {

                    stop_found = true;
                    farward_nodemap.put(stop, nodemap.get(stop));

                }
                terminate = true;
            }
        }
        avgOfHubs_farward = ((totOfHubs_backward) / avgOfHubs_farward);
        //System.out.println("Forward final seed list=" + sead_l.size() + " Distance= " + distance_farward + " Stop reached=" + stop_found);

        if (stop_found) {
            sead_size_if_start = sead_l.size();
            sead_l.clear();
            sead_l.add(stop);
            terminate = false;
            while (!terminate && !force_kill_path) {
                distance_backwards++;
                ArrayList<Integer> new_sead_l = new ArrayList<Integer>(50);
                for (int j = 0; j < sead_l.size(); j++) {
                    if (nodemap.get(sead_l.get(j)) != null) {
                        new_sead_l.removeAll(nodemap.get(sead_l.get(j)));
                        new_sead_l.addAll(nodemap.get(sead_l.get(j)));
                        backward_nodemap.put(sead_l.get(j), nodemap.get(sead_l.get(j)));
                        int c_size = nodemap.get(sead_l.get(j)).size();
                        totOfHubs_backward = totOfHubs_backward + c_size;
                        avgOfHubs_backward++;
                        if (c_size > WARNING_HUB_size) {
                            numberOf200Hubs_backward++;
                        }
                    }
                }
                new_sead_l.removeAll(sead_l);
                sead_l.clear();
                sead_l.addAll(new_sead_l);

                //System.out.println(" Seed list size=" + sead_l.size() + " Number of hubs larger than " + WARNING_HUB_size + " = " + numberOf200Hubs_backward + " . Current threshold=" + sead_size_if_start);
                if (sead_l.isEmpty() || sead_l.contains(start) || sead_size_if_start <= sead_l.size()) {
                    terminate = true;
                    backward_nodemap.put(start, nodemap.get(start));
                }
            }
            avgOfHubs_backward = ((totOfHubs_backward) / avgOfHubs_backward);
            sead_size_if_stop = sead_l.size();
            //System.out.println("Backwards final seed list=" + sead_l.size() + " Distance= " + distance_farward + " Start reached=" + stop_found);

            sead_l.clear();
            if (sead_size_if_start > sead_size_if_stop) {
                //System.out.println("Backward search recommended. Distance=" + distance_backwards);
                farward_nodemap = new HashMap<Integer, ArrayList<Integer>>();
                nodemap = new HashMap<Integer, ArrayList<Integer>>();
                avgOfHubs = avgOfHubs_backward;
                numberOfLargehubs = numberOf200Hubs_backward;
                //System.out.println(maximum_distance + "  Distance_backwards = " + distance_backwards );
                //System.out.println("Maximum distance=" + maximum_distance + "  reverse search recommended = " );
                if (maximum_distance >= distance_backwards) {
                    revers_recomended = true;
                    return backward_nodemap;
                } else {
                    JOptionPane.showMessageDialog(null, "No path found within the maximum allowed distance. Please change maximum_distance variable to a value higher than:" + distance_backwards);
                    nodemap = new HashMap<Integer, ArrayList<Integer>>();
                    return nodemap;
                }

            } else {
                //System.out.println("Forward search recommended. Distance=  " + distance_farward);
                backward_nodemap = new HashMap<Integer, ArrayList<Integer>>();
                nodemap = new HashMap<Integer, ArrayList<Integer>>();
                avgOfHubs = avgOfHubs_farward;
                numberOfLargehubs = numberOf200Hubs_farward;
                //System.out.println(maximum_distance + "  distance_forward =  " + distance_farward );
                //System.out.println("Maximum distance=" + maximum_distance + "  forward search recommended = " );
                if (maximum_distance >= distance_farward) {
                    revers_recomended = false;
                    return farward_nodemap;
                } else {
                    JOptionPane.showMessageDialog(null, "No parth found within the maximum allowed distance. Please change maximum_distance variable to a value higher than:" + distance_farward);
                    nodemap = new HashMap<Integer, ArrayList<Integer>>();
                    return nodemap;
                }

            }
        } else {
            nodemap = new HashMap<Integer, ArrayList<Integer>>();
            return nodemap;
        }


    }

    private ArrayList<Integer> superErode_keys(HashMap<Integer, ArrayList<Integer>> nodemap, int start, int stop) {
        ArrayList<Integer> sead_l = new ArrayList<Integer>();
        sead_l.add(start);
        boolean improved = true;
        int i = 0;
        boolean stop_found = false;
        ArrayList<Integer> current_level = new ArrayList<Integer>();
        HashMap<Integer, ArrayList<Integer>> posi_map = new HashMap<Integer, ArrayList<Integer>>();
        current_level.add(start);
        posi_map.put(1, current_level);
        Integer distance = 1;
        while ((improved && sead_l.size() > i) && !force_kill_path) {
            Integer c_sead = sead_l.get(i);
            if (nodemap.get(c_sead) != null) {
                ArrayList<Integer> tmp = new ArrayList<Integer>(nodemap.get(c_sead));
                tmp.removeAll(sead_l);
                sead_l.addAll(tmp);
                if (posi_map.get(distance).contains(c_sead)) {
                    Integer new_pos = distance + 1;
                    if (posi_map.containsKey(new_pos)) {
                        posi_map.get(new_pos).addAll(tmp);
                    } else {
                        posi_map.put(new_pos, tmp);
                    }
                } else if (posi_map.get(distance + 1) != null && (posi_map.get(distance + 1).contains(c_sead))) {
                    distance = distance + 1;
                    if (posi_map.containsKey(distance)) {
                        posi_map.get(distance).addAll(tmp);
                    } else {
                        posi_map.put(distance, tmp);
                    }
                }

                if (nodemap.get(c_sead).contains(stop)) {
                    improved = false;
                    stop_found = true;

                }
            }
            if (i % 10 == 0) {
                //System.out.println("Optimizing network ->" + i + ": SEADLIST=" + sead_l.size() + " distance=" + posi_map.keySet());
            }
            i++;
        }
        if (!stop_found) {
            return new ArrayList<Integer>(0);
        } else {
            if (!sead_l.contains(stop)) {
                sead_l.add(stop);
            }
            return sead_l;
        }


    }

    public HashMap<Integer, ArrayList<Integer>> superErode_values(HashMap<Integer, ArrayList<Integer>> nodemap) {
        ArrayList<Integer> map_keys = new ArrayList(nodemap.keySet());
        for (int i = 0; (i < map_keys.size() && !force_kill_path); i++) {
            if (i % 10 == 0) {
                //System.out.println("Optimizing network ->" + i);
            }
            nodemap.get(map_keys.get(i)).retainAll(map_keys);
        }
        return nodemap;
    }

    private HashMap<Integer, ArrayList<Integer>> superErode_neighbors(HashMap<Integer, ArrayList<Integer>> nodemap, int start, int stop) {
        ArrayList<Integer> map_keys = new ArrayList(nodemap.keySet());
        for (int i = 0; i < map_keys.size(); i++) {
            ArrayList<Integer> tmp = new ArrayList<Integer>(nodemap.get(map_keys.get(i)));
            if (!tmp.contains(start) && !tmp.contains(stop)) {
                for (int j = 0; j < tmp.size(); j++) {
                    if (tmp.get(j) != start && tmp.get(j) != stop && nodemap.containsKey(tmp.get(j))) {
                        nodemap.get(tmp.get(j)).removeAll(tmp);
                    }
                }
            }
        }
        return nodemap;

    }

    private ArrayList<Integer> findHub(HashMap<Integer, ArrayList<Integer>> nodemap, int start, int stop, int hub_threshld) {
        ArrayList<Integer> maxs = new ArrayList<Integer>(10);
        ArrayList<Integer> map_keys = new ArrayList<Integer>(nodemap.keySet());
        for (int i = 0; i < map_keys.size(); i++) {
            if (nodemap.get(map_keys.get(i)).size() >= hub_threshld) { //&& !nodemap.get(map_keys.get(i)).contains(start) && !nodemap.get(map_keys.get(i)).contains(stop)
                if (map_keys.get(i) != start && map_keys.get(i) != stop) {
                    maxs.add(map_keys.get(i));
                }
            }

        }
        map_keys.clear();
        return maxs;
    }

    private ArrayList<Integer> collapHubs(HashMap<Integer, ArrayList<Integer>> nodemap, ArrayList<Integer> maxs, Integer start, Integer stop, int hub_threshld) {
        ArrayList<Integer> newcomers_l = new ArrayList<Integer>(maxs.size());
        if (maxs.size() > 0 && hub_threshld > 2) { //&& nodemap.get(start) != null && nodemap.get(stop) != null
            for (int j = 0; j < maxs.size(); j++) {
                Integer current_hub = maxs.get(j);
                if (current_hub > 0 && current_hub != start && current_hub != stop) {
                    Integer new_key = current_hub * (-1);
                    if (nodemap.containsKey(current_hub)) {
                        ArrayList<Integer> chub_neibs = new ArrayList<Integer>(nodemap.get(current_hub));
                        if (chub_neibs.size() >= hub_threshld && !chub_neibs.contains(start) && !chub_neibs.contains(stop)) {
                            ArrayList<Integer> new_neibs = new ArrayList<Integer>(chub_neibs.size() * 5);
                            for (int i = 0; i < chub_neibs.size(); i++) {
                                if (nodemap.containsKey(chub_neibs.get(i))) {
                                    new_neibs.removeAll(nodemap.get(chub_neibs.get(i)));
                                    new_neibs.addAll(nodemap.get(chub_neibs.get(i)));
                                }
                            }
                            new_neibs.remove(new_key);
                            new_neibs.remove(current_hub);
                            new_neibs.trimToSize();
                            nodemap.put(new_key, new ArrayList<Integer>(new_neibs));
//                            newcomers_l.add(new_key);
                            new_neibs.clear();
                            nodemap.keySet().remove(current_hub);
                            nodemap.keySet().removeAll(chub_neibs);
                            maxs.removeAll(chub_neibs);
                            Iterator<ArrayList<Integer>> val_itr = nodemap.values().iterator();

                            while (val_itr.hasNext()) {
                                ArrayList<Integer> c_entrs_l = val_itr.next();
                                if (c_entrs_l.removeAll(chub_neibs)) {
                                    c_entrs_l.add(new_key);
                                }
                            }
                            nodemap.get(new_key).remove(new_key);
                        } else if (chub_neibs.size() >= hub_threshld && chub_neibs.contains(start) && !chub_neibs.contains(stop)) {
//                            ArrayList<Integer> new_neibs = new ArrayList<Integer>(chub_neibs.size() * 5);
//                            for (int i = 0; i < chub_neibs.size(); i++) {
//                                if (nodemap.containsKey(chub_neibs.get(i))) {
//                                    new_neibs.removeAll(nodemap.get(chub_neibs.get(i)));
//                                    new_neibs.addAll(nodemap.get(chub_neibs.get(i)));
//                                }
//                            }
//                            new_neibs.remove(new_key);
//                            new_neibs.remove(current_hub);
//                            new_neibs.trimToSize();
//                            nodemap.keySet().remove(current_hub);
//                            nodemap.keySet().removeAll(chub_neibs);
//                            maxs.removeAll(chub_neibs);
//                            nodemap.put(new_key, new ArrayList<Integer>(new_neibs));
//                            new_neibs.clear();
//                            Iterator<ArrayList<Integer>> val_itr = nodemap.values().iterator();
//
//                            while (val_itr.hasNext()) {
//                                ArrayList<Integer> c_entrs_l = val_itr.next();
////                                if (c_entrs_l.remove(current_hub)) { //unnessosory as all the neibours were refreshed earlier
////                                    c_entrs_l.add(new_key);
////                                }
//                                if (c_entrs_l.removeAll(chub_neibs)) {
//                                    c_entrs_l.add(new_key);
//                                }
//                            }
//                            nodemap.get(new_key).remove(new_key);
//                            nodemap.get(new_key).add(start);
//                            ArrayList<Integer> tmp = new ArrayList<Integer>(1);
//                            tmp.add(new_key);
//                            nodemap.put(start, tmp);
                        } else if (chub_neibs.size() >= hub_threshld && !chub_neibs.contains(start) && chub_neibs.contains(stop)) {
//                            //System.out.println("Special end = " + current_hub);
                        } else {
//                            //System.out.println("Special nothing " + current_hub + "  " + chub_neibs.size());
                            maxs.remove(j);
                            j--;
                        }

                    } else {
//                        //System.out.println("error hub not fond " + current_hub);
                    }

                }
            }
//            nodemap = eroder(nodemap, start, stop); // coursed errors but improves proformance, uncoment after handliing the possible errors
        }

        //System.out.println("Collapsing hubs complete . size of network=" + nodemap.size());

        return newcomers_l;
    }

    public HashMap<Integer, ArrayList<Integer>> GetNodemap(int[][] edges, boolean birectional) {
        HashMap<Integer, ArrayList<Integer>> nodemap = new HashMap<Integer, ArrayList<Integer>>();
        if (birectional) {
            for (int i = 0; i < edges.length; i++) {
                int from = 0;
                int to = 0;
                if (negetived) {
                    from = edges[i][0] * (-1);
                    to = edges[i][1] * (-1);

                } else {
                    from = edges[i][0];
                    to = edges[i][1];
                }
                if (from != to) {
                    if (nodemap.containsKey(from)) {
                        if (!nodemap.get(from).contains(to)) {
                            nodemap.get(from).add(to);
                        }
                    } else {
                        ArrayList<Integer> tmp = new ArrayList<Integer>(5);
                        tmp.add(to);
                        nodemap.put(from, tmp);
                    }

                    if (nodemap.containsKey(to)) {
                        if (!nodemap.get(to).contains(from)) {
                            nodemap.get(to).add(from);
                        }
                    } else {
                        ArrayList<Integer> tmp = new ArrayList<Integer>(5);
                        tmp.add(from);
                        nodemap.put(to, tmp);
                    }
                }

            }
            return nodemap;
        } else {
            for (int i = 0; i < edges.length; i++) {
                int from = 0;
                int to = 0;
                if (negetived) {
                    from = edges[i][0] * (-1);
                    to = edges[i][1] * (-1);
                } else {
                    from = edges[i][0];
                    to = edges[i][1];
                }
                if (from != to) {
                    if (nodemap.containsKey(from)) {
                        if (!nodemap.get(from).contains(to)) {
                            nodemap.get(from).add(to);
                        }
                    } else {
                        ArrayList<Integer> tmp = new ArrayList<Integer>(5);
                        tmp.add(to);
                        nodemap.put(from, tmp);
                    }
                }

            }

            return nodemap;
        }

    }

    public HashMap<Integer, ArrayList<Integer>> eroder_heuristic(HashMap<Integer, ArrayList<Integer>> nodemap, Integer start, Integer stop, int hub_threshld) {
        ArrayList<Integer> sead_l = new ArrayList<Integer>(1);
        sead_l.add(start);
        HashMap<Integer, ArrayList<Integer>> new_nodemap = new HashMap<Integer, ArrayList<Integer>>(nodemap.size());
        boolean found = true;
        int pos = 0;

        new_nodemap.put(pos, new ArrayList(sead_l));
        while (found) {
            pos++;
            ArrayList<Integer> colp_sead_l = new ArrayList<Integer>(1);
            for (int i = 0; i < sead_l.size(); i++) {
                Integer c_sead = sead_l.get(i);
                if (nodemap.get(c_sead) != null) {
                    ArrayList<Integer> partner_l = new ArrayList(nodemap.get(c_sead));
                    ArrayList<Integer> maxs = new ArrayList<Integer>(10);
                    if (c_sead != start && c_sead != stop) {
                        for (int j = 0; j < partner_l.size(); j++) {
                            Integer c_candi = partner_l.get(j);
                            if (c_candi != start && c_candi != stop && (nodemap.containsKey(c_candi)) && (nodemap.get(c_candi).size() > hub_threshld) && (!nodemap.get(c_candi).contains(start) && !nodemap.get(c_candi).contains(stop))) {
                                maxs.add(c_candi);
                                j = partner_l.size();
                            }
                        }
                    }
                    if (maxs.size() > 0) {
                        maxs = collapHubs(nodemap, maxs, start, stop, hub_threshld);
                        for (int j = 0; j < maxs.size(); j++) {
                            int col_c_sead = maxs.get(j) * (-1);
                            if (col_c_sead < 0) {
                                if (nodemap.containsKey(col_c_sead)) {
                                    partner_l = new ArrayList(nodemap.get(col_c_sead));
                                    new_nodemap.get(pos - 1).add(col_c_sead);
                                    colp_sead_l.add(col_c_sead);
                                    //                                    new_nodemap.get(pos - 1).removeAll(nodemap.get(partner_l.get(j)));
                                }
                            }
                            if ((sead_l.indexOf(maxs.get(j)) > i)) {
                                sead_l.remove(maxs.get(j));
                            }
                        }
                    }


                    ArrayList<ArrayList<Integer>> found_l = new ArrayList(new_nodemap.values());
                    for (int j = 0; j < found_l.size(); j++) {
                        boolean addStop = false;
                        if (partner_l.remove(stop)) {
                            addStop = true;
                        }
                        if (addStop) {
                            partner_l.clear();
                            partner_l.add(stop);
                        } else {
                            partner_l.removeAll(found_l.get(j));
                        }
                    }

                    if (partner_l.size() > 0) {
                        for (int j = 0; j < partner_l.size(); j++) {
                            if ((partner_l.get(j) != stop) && (nodemap.get(partner_l.get(j)) == null || nodemap.get(partner_l.get(j)).size() <= 1)) {
                                ArrayList<Integer> tmp = nodemap.remove(partner_l.get(j));
                                j--;
                            }
                        }
                        if (new_nodemap.containsKey(pos)) {
                            new_nodemap.get(pos).removeAll(partner_l);
                            new_nodemap.get(pos).addAll(partner_l);
                        } else {
                            new_nodemap.put(pos, new ArrayList(partner_l));
                        }
                    }
                }

            }
            sead_l.clear();
            if (new_nodemap.get(pos) != null) {
                sead_l.addAll(new_nodemap.get(pos));
            }
            sead_l.remove(start);
            sead_l.remove(stop);
            colp_sead_l.removeAll(sead_l);
            sead_l.addAll(colp_sead_l);
            if (sead_l.size() < 1) {
                found = false;
            }
        }
        HashMap<Integer, ArrayList<Integer>> procecced_nodemap = new HashMap<Integer, ArrayList<Integer>>();
        Iterator<ArrayList<Integer>> val_itr = new_nodemap.values().iterator();
        while (val_itr.hasNext()) {
            ArrayList<Integer> c_entrs_l = val_itr.next();
            for (int i = 0; i < c_entrs_l.size(); i++) {
                if (nodemap.get(c_entrs_l.get(i)) != null) {
                    procecced_nodemap.put(c_entrs_l.get(i), nodemap.get(c_entrs_l.get(i)));
                }

            }

        }
        return procecced_nodemap;
    }

    public ArrayList<Integer> addAll_inversed(ArrayList<Integer> from, ArrayList<Integer> to) {
        for (int i = 0; i < from.size(); i++) {
            if (from.get(i) > 0) {
                to.add(from.get(i) * (-1));
            }
        }
        return to;
    }

    private void expand_collapsed(int hub_threshld) {
        System.out.println("Expanding ");
        ArrayList<ArrayList<Integer>> selected_patth_ll_bkup = new ArrayList<ArrayList<Integer>>(selected_patth_ll);
        selected_patth_ll.clear();
        for (int i = 0; ((i < selected_patth_ll_bkup.size() && i > -1) && !force_kill_path); i++) {
            ArrayList<Integer> c_selected_patth_l = selected_patth_ll_bkup.remove(i);
            boolean expanded = false;
            for (int j = 0; (j < c_selected_patth_l.size() && !force_kill_path); j++) {
                Integer c_point = c_selected_patth_l.remove(j);
                if (c_point < 0 && j >= 1) {
                    Integer c_prevpoint = c_selected_patth_l.get(j - 1);
                    Integer c_nextpoint = -1;
                    if (j < c_selected_patth_l.size()) {
                        c_nextpoint = c_selected_patth_l.get(j);
                        if (c_nextpoint < 0) {
                            c_nextpoint = c_nextpoint * -1;
                        }
                    }
                    if (c_prevpoint > 0) {
                        c_point = c_point * (-1);
                        targets_nehgbours_l.clear();
                        garbage_sead_l.clear();
                        resolve(safecopyMap(nodemap_bkup), c_prevpoint, c_point, hub_threshld, false, colpseHubs, false);
                        ArrayList<ArrayList<Integer>> selected_patth_ll_new = new ArrayList<ArrayList<Integer>>(10);
                        ArrayList<ArrayList<Integer>> selected_patth_ll_prev_l = new ArrayList<ArrayList<Integer>>(selected_patth_ll);
                        selected_patth_ll.clear();
                        if (c_nextpoint > 0) {
                            targets_nehgbours_l.clear();
                            garbage_sead_l.clear();
                            resolve(safecopyMap(nodemap_bkup), c_point, c_nextpoint, hub_threshld, false, colpseHubs, false);
                        } else {
                            selected_patth_ll.add(new ArrayList((c_selected_patth_l.subList(j, c_selected_patth_l.size() - 1))));
                        }

                        for (int k = 0; k < selected_patth_ll_prev_l.size(); k++) {
                            ArrayList<Integer> c_selected_patth_new_prev_l = selected_patth_ll_prev_l.get(k);
                            boolean removed = c_selected_patth_new_prev_l.remove(c_point);
                            for (int l = 0; l < selected_patth_ll_prev_l.size(); l++) {
                                for (int m = 0; m < selected_patth_ll.size(); m++) {
                                    ArrayList<Integer> c_selected_patth_new_forward_l = selected_patth_ll.get(m);
                                    ArrayList<Integer> tmp_new = new ArrayList<Integer>(10);
                                    tmp_new.addAll(c_selected_patth_l.subList(0, j - 1));
                                    tmp_new.addAll(c_selected_patth_new_prev_l);
                                    tmp_new.addAll(c_selected_patth_new_forward_l); //.subList(j, c_selected_patth_new_forward_l.size() - 1)
                                    tmp_new.addAll(c_selected_patth_l.subList(j + 1, c_selected_patth_l.size()));
                                    selected_patth_ll_new = add_unique_onkly(selected_patth_ll_new, tmp_new);

                                }
                            }
                        }

                        expanded = true;
                        selected_patth_ll_bkup.addAll(selected_patth_ll_new);

                    } else {
                        //System.out.println("Error in expanding collapsed ");
                    }
                } else {
                    c_selected_patth_l.add(j, c_point);
                }
            }
            if (!expanded) {
                if (i > 0) {
                    selected_patth_ll_bkup.add(i - 1, c_selected_patth_l);
                } else if (i == 0) {
                    selected_patth_ll_bkup.add(0, c_selected_patth_l);
                } else {
                    //System.out.println("Error in expanding collapsed ");
                }

            }

        }

        selected_patth_ll = selected_patth_ll_bkup;
        boolean repeat = false;
        for (int i = 0; (i < selected_patth_ll.size() && !force_kill_path); i++) {
            ArrayList<Integer> tmp_selected = selected_patth_ll.get(i);
            for (int j = 0; j < tmp_selected.size(); j++) {
                if (tmp_selected.get(j) < 0) {
                    repeat = true;
                    j = tmp_selected.size();
                    i = selected_patth_ll.size();
                }
            }
        }
        if (repeat && !force_kill_path) {
            expand_collapsed(hub_threshld);
        }
    }

    private ArrayList<ArrayList<Integer>> add_unique_onkly(ArrayList<ArrayList<Integer>> selected_patth_ll_new, ArrayList<Integer> tmp_new) {

        for (int i = 0; i < selected_patth_ll_new.size(); i++) {
            if (selected_patth_ll_new.get(i).containsAll(tmp_new)) {
                return selected_patth_ll_new;
            }

        }
        selected_patth_ll_new.add(tmp_new);
        return selected_patth_ll_new;
    }

    private HashMap<Integer, ArrayList<Integer>> safecopyMap(HashMap<Integer, ArrayList<Integer>> backup_nodemap) {
        HashMap<Integer, ArrayList<Integer>> tmp_nodemap = new HashMap<Integer, ArrayList<Integer>>(backup_nodemap.size());
        ArrayList<Integer> tmp_keys = new ArrayList<Integer>(backup_nodemap.keySet());
        for (int k = 0; k < tmp_keys.size(); k++) {
            ArrayList<Integer> tmp_2 = new ArrayList<Integer>(5);
            tmp_2.addAll(backup_nodemap.get(tmp_keys.get(k)));
            tmp_nodemap.put(tmp_keys.get(k), tmp_2);
        }
        return tmp_nodemap;
    }
}
