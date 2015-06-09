/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package no.ntnu.medisin.egenvar.server;

import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author sabryr
 */
public class EGENV_Json {

    private HashMap<String, String> details_map;
    private ArrayList<EGENV_Json> child_l;
//    private ArrayList<String> grand_child_l;
    private ArrayList<String> child__id_l;
    private String id;
    private EGENV_Json parent;
    private int max_name_size = 1024;

    public EGENV_Json(String id, HashMap<String, String> new_details_map) {
        this.id = id;
        details_map = new HashMap<>();
        child_l = new ArrayList<>();
        child__id_l = new ArrayList<>();
        details_map = new HashMap<>(new_details_map);

//        details_map.put("name", name);
        details_map.put("id", id);
//        details_map.put("url", url);
//        grand_child_l = new ArrayList<>();
    }

    public void setParent(EGENV_Json parent) {
        this.parent = parent;
    }

    public EGENV_Json getParent() {
        return parent;
    }

    public void addChild(EGENV_Json child) {
        if (!isaChild(child)) {
            child_l.add(child);
            child__id_l.add(child.getId());
            child.setParent(this);
        } else {
        }
    }

//    public void setGrandChild(String child_id) {
//        if (!grand_child_l.contains(child_id)) {
//            grand_child_l.add(child_id);
//        }
//    }
    public void removeChild(EGENV_Json tocheck) {
        boolean result = false;
        for (int i = 0; (i < child_l.size() && !result); i++) {
            if (child_l.get(i).getId().equals(tocheck.getId())) {
                result = true;
                child__id_l.remove(child_l.remove(i).getId());
            }
        }
    }

    public String getId() {
        return id;
    }

    public HashMap<String, String> getDetails() {
        return details_map;
    }

    public EGENV_Json parent() {
        return parent;
    }

    public String getName() {
        return details_map.get("name");
    }

    public String setName(String new_name) {
        return details_map.put("name", new_name);
    }

    public ArrayList<EGENV_Json> getchildren() {
        return child_l;
    }

//    public ArrayList<String> getGrandchildren() {
//        return grand_child_l;
//    }
    public boolean isaChild(EGENV_Json tocheck) {
        boolean result = false;
        for (int i = 0; (i < child_l.size() && !result); i++) {
            if (child_l.get(i).getId().equals(tocheck.getId())) {
                result = true;
            }
        }
        return result;
    }

    public boolean isaChild(String tocheck) {
        return child__id_l.contains(tocheck);
    }

    public boolean isaChild(ArrayList<EGENV_Json> parents_l, EGENV_Json tocheck) {
        boolean result = false;
        for (int i = 0; (i < parents_l.size() && !result); i++) {
            if (parents_l.get(i).getId().equals(tocheck.getId())) {
                result = true;
            }
        }
        return result;
    }

    public boolean isaChild(ArrayList<EGENV_Json> parents_l, String child_id) {
        boolean result = false;
        for (int i = 0; (i < parents_l.size() && !result); i++) {
            if (parents_l.get(i).getId().equals(getId())) {
                result = true;
            }
        }
        return result;
    }

    public EGENV_Json selectParent(ArrayList<EGENV_Json> parents_l, String tocheck) {
        EGENV_Json result = null;
        for (int i = 0; (i < parents_l.size() && result == null); i++) {
            if (parents_l.get(i).getId().equals(tocheck)) {
                result = parents_l.get(i);
            }
        }
        return result;
    }

    public EGENV_Json getParentFromChildren(String target_id) {
        EGENV_Json result = null;
        boolean complete = false;
        ArrayList<EGENV_Json> gc_l = new ArrayList<>();
//        for (int i = 0; i < child_l.size(); i++) {
//            EGENV_Json c_Json = child_l.get(i);
//            if (c_Json.isaGrandChild(child_id)) {
//                gc_l.add(c_Json);
//            }
//        }
        gc_l.addAll(child_l);
        int cc = 10000;
        while (!complete && !gc_l.isEmpty() && cc > 0) {
            cc--;
            result = selectParent(gc_l, target_id);
            if (result == null) {
                ArrayList<EGENV_Json> new_gc_l = new ArrayList<>();
                for (int i = 0; (result == null && i < gc_l.size()); i++) {
//                    ArrayList<EGENV_Json> c_children_l = gc_l.get(i).getchildren();
//                    for (int j = 0; j < c_children_l.size(); j++) {
//                        EGENV_Json c_Json = c_children_l.get(j);
//                        if (c_Json.isaGrandChild((child_id))) {
//                            new_gc_l.add(c_Json);
//                        }
//                    }
//                    result = selectParent(gc_l.get(i).getchildren(), target_id);
//                    if (result == null) {
//                        ArrayList<EGENV_Json> grand_child_l = gc_l.get(i).getchildren();
//                        for (int j = 0; j < grand_child_l.size(); j++) {
//                            new_gc_l.addAll(grand_child_l.get(j).getchildren());
//                        }
//                    }
                    new_gc_l.addAll(gc_l.get(i).getchildren());

                }
                gc_l.clear();
                gc_l.addAll(new_gc_l);
            } else {
//                System.out.println("80 " + cc + " child_id=" + child_id + " result=" + result.getId());
                complete = true;
            }
        }
        if (cc <= 0) {
            System.out.println("Warning Not all children investigated, terminated after 10000 iteration");
        }
        return result;
    }

    public EGENV_Json getParentFromChildren2(String target_id) {
        EGENV_Json result = null;
        boolean complete = false;
        ArrayList<EGENV_Json> gc_l = new ArrayList<>();
        gc_l.addAll(child_l);
        int cc = 10000;
        while (!complete && !gc_l.isEmpty() && cc > 0) {
            cc--;
            result = selectParent(gc_l, target_id);
            if (result == null) {
                ArrayList<EGENV_Json> new_gc_l = new ArrayList<>();
                for (int i = 0; (result == null && i < gc_l.size()); i++) {
                    new_gc_l.addAll(gc_l.get(i).getchildren());
                }
                gc_l.clear();
                gc_l.addAll(new_gc_l);
            } else {
                complete = true;
            }
        }
        if (cc <= 0) {
            System.out.println("Warning Not all children investigated, terminated after 10000 iteration");
        }
        return result;
    }

    public boolean isaGrandChild(EGENV_Json tocheck) {
        boolean result = false;
        boolean complete = false;
        ArrayList<EGENV_Json> gc_l = new ArrayList<>();
        gc_l.addAll(child_l);
        while (!complete || !gc_l.isEmpty()) {
            if (isaChild(gc_l, tocheck)) {
                complete = true;
                result = true;
            } else {
                ArrayList<EGENV_Json> new_gc_l = new ArrayList<>();
                for (int i = 0; (i < gc_l.size() && !gc_l.isEmpty()); i++) {
                    new_gc_l.addAll(gc_l.get(i).getchildren());
                }
                gc_l.clear();
                gc_l.addAll(new_gc_l);
            }

        }
        return result;
    }

    public boolean isaGrandChild(String child_id) {
        boolean result = false;
        boolean complete = false;
        ArrayList<EGENV_Json> gc_l = new ArrayList<>();
        gc_l.addAll(child_l);
        while (!complete || !gc_l.isEmpty()) {
            if (isaChild(gc_l, child_id)) {
                complete = true;
                result = true;
            } else {
                ArrayList<EGENV_Json> new_gc_l = new ArrayList<>();
                for (int i = 0; (i < gc_l.size() && !gc_l.isEmpty()); i++) {
                    new_gc_l.addAll(gc_l.get(i).getchildren());
                }
                gc_l.clear();
                gc_l.addAll(new_gc_l);
            }

        }
        return result;
    }

    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        try {
            if (details_map != null && !details_map.isEmpty()) {

                StringBuilder lineage = new StringBuilder();
                if (getParent() != null ) { //&& getParent().id.matches("[0-9]+")
//                    EGENV_Json c_p = getParent();
                    lineage.append(id);
                    EGENV_Json  c_p = getParent();
                    while (c_p != null) {
                        if (c_p.id.matches("[0-9]+")) {
                            lineage.append(",");
                            lineage.append(c_p.id);
                            c_p = c_p.getParent();
                        } else {
                            c_p = null;
                        }
                    }

                } else {
                    lineage.append("-1");
                }
                details_map.put("lineage", lineage.toString());

//                if (getParent() != null && getParent().id.matches("[0-9]+")) {
//
//                    details_map.put("parent_id", getParent().id);
//                } else {
//                    details_map.put("parent_id", "-1");
//                }
                ArrayList<String> keys_l = new ArrayList<>(details_map.keySet());
                String c_val = details_map.get(keys_l.get(0));
                if (c_val != null && c_val.length() > max_name_size) {
                    c_val = c_val.substring(0, max_name_size) + "..";
                }
                str.append("{\"" + keys_l.get(0) + "\":\"" + c_val + "\"");
                for (int i = 1; i < keys_l.size(); i++) {
                    c_val = details_map.get(keys_l.get(i));
                    if (c_val != null && c_val.length() > max_name_size) {
                        c_val = c_val.substring(0, max_name_size) + "..";
                        str.append(",\"" + keys_l.get(i) + "\":\"" + c_val + "\"");
                    } else if (c_val != null) {
                        str.append(",\"" + keys_l.get(i) + "\":\"" + c_val + "\"");
                    } else {
                        str.append(",\"" + keys_l.get(i) + "\":\"NA\"");
                    }
                }
                if (!child_l.isEmpty()) {
                    str.append(",\"children\":[");
                    if (child_l.get(0) != null) {
                        str.append(child_l.get(0).toString());
                    } else {
                        str.append("NULL");
                    }
                    for (int i = 1; i < child_l.size(); i++) {
                        if (!getId().equals(child_l.get(i).getId())) {
                            str.append("," + child_l.get(i).toString());
                        } else {
                            System.out.println("Create json, Looping error " + getId() + " Parent=Child");
                        }
//                      
                    }
                    str.append("]");
                }
                str.append("}");
            }
        } catch (Exception ex) {
            str.append("NULL");
            ex.printStackTrace();
            System.exit(1);
        }
        return str.toString();
    }
}
