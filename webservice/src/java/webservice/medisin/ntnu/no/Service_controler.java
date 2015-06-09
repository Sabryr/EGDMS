/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package webservice.medisin.ntnu.no;

import java.util.Calendar;
import java.util.GregorianCalendar;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

/**
 *
 * @author sabryr service status delete code
 */
public class Service_controler implements Runnable {

    public static int mem_loop_c = 8760;
//    public static int last_checked_hour = 0;
//    private Authenticate_service caller;
//    public static boolean done = false;
    public static int sleep_time = 3600000;
    public static int counter = 0;
    public static boolean refreshnow = false;

    public Service_controler() {
//        this.caller = caller;
    }

    @Override
    public void run() {
        timer();
    }

//    public boolean isDone() {
//        return done;
//    }
    public static void reset() {
        counter = 0;
        mem_loop_c=8760;
    }

    public static int getCounter() {
        return counter;
    }

    private void timer() {
        while (mem_loop_c > 0) {
            System.out.println("50 @" + Timing.getDateTime() + " counter=" + counter+"  mem_loop_c="+mem_loop_c);
            mem_loop_c--;
            counter++;
            try {
                Thread.sleep(sleep_time);
            } catch (InterruptedException ex) {
                mem_loop_c = -1;
                System.out.println("Warning SC24a:" + ex.getMessage());
            }
        }
    }
//    /*
//     MethodID=SC4
//     */
//    private void commit() {
//        while (mem_loop_c > 0) {
//            mem_loop_c--;
//            GregorianCalendar c_cal = new GregorianCalendar();
//            int c_hr = c_cal.get(Calendar.HOUR);
//            if (last_checked_hour != c_hr) {
//                last_checked_hour = c_hr;
//                try {
//                    Thread.sleep(sleep_time);
//                } catch (InterruptedException ex) {
//                    mem_loop_c = -1;
//                    System.out.println("Warning SC24a:" + ex.getMessage());
//                }
//            }else {
//                try {
//                    Thread.sleep(600000);
//                } catch (InterruptedException ex) {
//                    mem_loop_c = -1;
//                    System.out.println("Warning SC24a:" + ex.getMessage());
//                }
//            }
//        }
//    }
//    /*
//     MethodID=SC4
//     */
//    private void commit(boolean now) {
//        while (mem_loop_c > 0) {
//            mem_loop_c--;
//            GregorianCalendar c_cal = new GregorianCalendar();
//            int c_hr = c_cal.get(Calendar.HOUR);
////            System.out.println("40 c_hr=" + c_hr + "  last_checked_hour=" + last_checked_hour+" @"+Timing.getDateTime());
//            if (now || last_checked_hour != c_hr) {
//                last_checked_hour = c_hr;
////                System.out.println("Reseting delete code ");
////                boolean intitated = false;
////                try {
////                    Context env = (Context) new InitialContext().lookup("java:comp/env");
//////                    System.out.println(" \n\n ..54 " + env.getNameInNamespace()+"\n\n");
////                    Object env_ob = env.lookup(caller.DATASOURCE_NAME_data_update);
////                    intitated = true;
////                } catch (Exception e) {
////                    System.out.println("57 " + e.getMessage());
////                }
//
////                    caller.getDeleteCode();
//
//                try {
//                    Thread.sleep(sleep_time);
//                } catch (InterruptedException ex) {
//                    mem_loop_c = -1;
//                    System.out.println("Warning SC24a:" + ex.getMessage());
//                }
//                now = false;
//                sleep_time = 600000;//43200000;//;
////                done = true;
//            } else {
//                try {
//                    Thread.sleep(600000);
//                } catch (InterruptedException ex) {
//                    mem_loop_c = -1;
//                    System.out.println("Warning SC24a:" + ex.getMessage());
//                }
//            }
//        }
//        System.out.println("Warning SC4a: mem_loop inadequate. Please infrom developers (egenvar@gmail.com) ");
//    }
}
