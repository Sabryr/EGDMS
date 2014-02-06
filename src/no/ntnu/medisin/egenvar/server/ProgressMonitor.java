/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package no.ntnu.medisin.egenvar.server;

/**
 *
 * @author sabryr
 */
public class ProgressMonitor implements Runnable {

    private static boolean cancel = false;
    private static long interval = 500;
    private static long starting_interval = 1000;
    private static String progressString = ".";
    private static int counter = 0;
    private static int max_counter = 100;
    private static String waiter = "";

    public ProgressMonitor(long interval, String new_waiter) {
        ProgressMonitor.interval = interval;
        waiter = new_waiter;
    }

    @Override
    public void run() {
        set();
        print_progeress();
    }

    public static void set() {
        cancel = false;
        counter = 0;
        progressString = ".";
        Timing.setPointer2();
    }

    public static void print_progeress() {        
        while (!ProgressMonitor.cancel) {
            if (counter > max_counter) {
                ProgressMonitor.cancel = true;
                System.out.println("Maximum waiting time exceeded " + counter+" "+Timing.getFromlastPointer2());
                System.exit(counter);
            } else if (counter > 80) {
                System.out.println("");
//                System.out.println(progressString);
            }
            System.out.print(".");
            if (counter > 10) {
                starting_interval = ProgressMonitor.interval ;
            }
//            progressString = progressString + ".";
            try {
                Thread.sleep(starting_interval);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
            counter++;           
        }
    }

    public static void cancel() {
        ProgressMonitor.cancel = true;
    }

    public static void start() {
        ProgressMonitor.cancel = false;
        set();
        print_progeress();
    }

    public static void reset() {
        Timing.setPointer2();
    }
}
