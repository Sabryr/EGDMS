/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package no.ntnu.medisin.egenvar.cmd;

/**
 *
 * @author sabryr
 */
public class ProgressMonitor implements Runnable {

    private boolean cancel = false;
    private long interval = 5000;
    private long starting_interval = 1000;
    private String progressString = ".";
    private int counter = 0;
    private int max_counter = 50;
    private int caller;
//    private String waiter = "";

    public ProgressMonitor(long interval, int caller) { //, String new_waiter
        this.interval = interval;
        this.caller = caller;
    }

    @Override
    public void run() {
        set(caller);
        print_progeress();
    }
  

    public void set(int caller) {
        this.caller = caller;
        cancel = false;
        counter = 0;
        progressString = ".";
    }
    /*
     MthodID=PR1
     */

    public void print_progeress() {
        boolean msgprinted = false;
        while (!cancel) {
//            System.out.println("41 counter=" + counter + " max_counter=" + max_counter + "  starting_interval=" + starting_interval + "  interval=" + interval + " caller=" + caller);
            if (counter > max_counter) {
                System.out.println("Maximum waiting time exceeded " + counter);
                System.out.println("Increase the timeout limit by overiding -interval if you want to force the waiting time. Current value is " + interval);
                System.exit(counter);
            } else if (counter > 0) {
                if (!msgprinted) {
                    System.out.println("Waiting for the server " + progressString);
                    msgprinted = true;
                } else {
//                    System.out.println("Waiting for the server " + progressString);
                }
            }
            System.out.print(".");
//            System.out.print("  "+caller);
            if (counter > 10) {
                starting_interval = interval;
            }
            progressString = progressString + ".";
            try {
                Thread.sleep(starting_interval);
            } catch (InterruptedException ex) {
//                System.out.println("Warning PR1a: " + ex.getMessage());
            }
            if (max_counter - counter < 5) {
                System.out.println("Warning Long running process, the process will be terminated in " + ((max_counter - counter) * starting_interval) / 1000 + " seconds.");
            }
            counter++;
        }
    }

    public void cancel(int caller) {
        this.caller = caller;
        counter = 0;
        cancel = true;
    }
}
