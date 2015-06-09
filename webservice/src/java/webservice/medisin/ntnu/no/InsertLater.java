/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package webservice.medisin.ntnu.no;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sql.DataSource;

/**
 *
 * @author sabryr
 */
public class InsertLater implements Runnable {
    
    private Statement st;
    private ArrayList<String> sql_l;
    private int[] result_a;
    private boolean done;
    private long safety = 100000000;
    private int timout;
    private boolean sendEmail;
    private String user_email;
    private boolean allOk;
    private boolean cancel;
    private DataSource dataSource_data;
    
    public InsertLater(DataSource dataSource_data, ArrayList<String> sql_l, int timout, String user_email) {
        this.dataSource_data = dataSource_data;
        this.sql_l = sql_l;
        this.timout = timout;
        sendEmail = false;
        this.user_email = user_email;
        
    }
    
    public int[] getResults() {
        if (isDone()) {
        }
        return result_a;
    }
    /*
     MethodID=IL1
     */
    
    @Override
    public void run() {
        Connection ncon = null;
        try {
            ncon = dataSource_data.getConnection();
            st = ncon.createStatement();
            cancel = false;
            allOk = true;
            done = false;
            commit();
            try {
                if (st != null && !st.isClosed()) {
                    st.close();
                }
                ncon.close();
            } catch (SQLException ex) {
            }
            done = true;
            if (sendEmail) {
                System.out.println("54 sending to " + user_email);
                if (allOk) {
                    Authenticate_service.sendMessage(user_email, user_email, "All updates committed (Total updates:" + result_a.length + ")", "eGEnVar process monitor");
                } else {
                    Authenticate_service.sendMessage(user_email, user_email, "ERROR IL1a:There were errors during processing","eGEnVar process monitor");
                }
            }
        } catch (SQLException ex) {
            System.out.println("Error IL1a:" + ex.getMessage());
        } finally {
            try {
                if (st != null && !st.isClosed()) {
                    st.close();
                }
                if (ncon != null && !ncon.isClosed()) {
                    ncon.close();
                }
            } catch (SQLException ex) {
            }
        }
    }
    
    public void sendMail() {
        sendEmail = true;
    }
    
    public void cancel() {
        cancel = true;
    }
    
    public boolean getStautus() {
        return done;
    }
    
    public boolean isDone() {
        while (!done && !cancel && safety > 0) {
            safety--;
            if (safety < 10000) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException ex) {
                }
            } else if (safety < timout) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ex) {
                }
            }
        }
        return done;
    }
    
    public boolean isPostpondable(int limit) {
        while (!done && limit > 0) {
            limit--;
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ex) {
            }
            
        }
        return done;
    }
    
    private void commit() {
        try {
            if (st != null && !st.isClosed()) {
                if (sql_l != null && !sql_l.isEmpty()) {
                    result_a = new int[sql_l.size()];
                    for (int i = 0; (allOk && !cancel && (i < sql_l.size())); i++) {
                        String c_sql = sql_l.get(i);
                        System.out.print("Creating :" + (i + 1) + " of " + sql_l.size());
                        String[] sql_a = c_sql.split("\\|\\|");
                        ArrayList<Integer> exiting_l = new ArrayList<>();
                        exiting_l.add(-1);
                        ArrayList<Integer> id_l = null;
                        if (sql_a.length == 3) {
                            String q_get_exiting = sql_a[0];
                            if (q_get_exiting != null && !q_get_exiting.isEmpty()) {
                                exiting_l = get_ID_forQuery(q_get_exiting, st, true, null);
                            }
                            System.out.print(" Existing " + exiting_l.size());
//                            System.out.print(" (153) " + exiting_l.size() + "  q_get_exiting=" + q_get_exiting);
//                                sql_a[j] = sql_a[j].replace("<ID_L_E>", exiting_l.toString().replaceAll("\\s", "").replace("[", "").replace("]", ""));
                         
                            id_l = get_ID_forQuery(sql_a[1], st, false, exiting_l);
                            if (id_l.isEmpty()) {
                                id_l.add(-1);
                            }
                            System.out.print(" New=" + id_l.size());
                            sql_a[2] = sql_a[2].replace("<ID_L_E>", id_l.toString().replaceAll("\\s", "").replace("[", "").replace("]", ""));
                            
                        }
                        
                        if (sql_a.length == 1) {
                            result_a[i] = st.executeUpdate(c_sql);
                        } else if (id_l != null && !id_l.isEmpty()) {
                            String f_sql = sql_a[sql_a.length - 1];
                            System.out.print(" size=" + id_l.size());
                            Collections.sort(id_l);
                            int limit = 512;
                            if (id_l.size() > limit) {
                                while (!id_l.isEmpty()) {
                                    System.out.print(".");
                                    ArrayList<Integer> reslt__tmp_l = new ArrayList<>();
                                    int count = 0;
                                    for (int j = 0; (!id_l.isEmpty() && count < limit); j++) {
                                        reslt__tmp_l.add(id_l.remove(j));
                                        j--;
                                        count++;
                                    }
                                    if (!reslt__tmp_l.isEmpty()) {
                                        String f_t_sql = f_sql.replace("<ID_L>", reslt__tmp_l.toString().replaceAll("\\s", "")).replace("[", "").replace("]", "");
//                                        int limi = 265;
//                                        if (f_t_sql.length() < limi) {
//                                            limi = f_t_sql.length();
//                                        }
//                                        System.out.println("189 " + f_t_sql); //.substring(0, limi)
                                        result_a[i] = st.executeUpdate(f_t_sql);
                                    }
                                }
                            } else {
//                                int limi = 256;
//                                if (f_sql.length() < limi) {
//                                    limi = f_sql.length();
//                                }
//                                System.out.println("198 " + f_sql.substring(0, limi));
                                f_sql = f_sql.replace("<ID_L>", id_l.toString().replaceAll("\\s", "")).replace("[", "").replace("]", "");
                                result_a[i] = st.executeUpdate(f_sql);
                            }
                        }
                        System.out.println(".. Updated");
                    }
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            try {
                st.close();
            } catch (SQLException ex) {
            }
        }
    }
    
    public ArrayList<Integer> get_ID_forQuery(String query, Statement st_1, boolean unique, ArrayList<Integer> exclude_l) {
        ArrayList<Integer> result_l = new ArrayList<>(1);
        ResultSet r_1 = null;
        try {
            if (st_1 != null && !st_1.isClosed()) {
                r_1 = st.executeQuery(query);
                while (r_1.next()) {
                    int c_val = r_1.getInt(1);
                    if (exclude_l != null && !exclude_l.isEmpty()) {
                        int excl = r_1.getInt(2);
                        if (!exclude_l.contains(excl)) {
                            if (unique && !result_l.contains(c_val)) {
                                result_l.add(c_val);
                            } else {
                                result_l.add(c_val);
                            }
                        }
                    } else {
                        if (unique && !result_l.contains(c_val)) {
                            result_l.add(c_val);
                        } else {
                            result_l.add(c_val);
                        }
                    }
                }
                r_1.close();
            }
        } catch (Exception ex) {
            System.out.println("Error Ila: creating connections " + ex.getMessage());
        } finally {
            try {
                if (r_1 != null && !r_1.isClosed()) {
                    r_1.close();
                }
            } catch (SQLException ex) {
            }
        }
        return result_l;
    }
}
