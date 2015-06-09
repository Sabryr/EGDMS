/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package webservice.medisin.ntnu.no;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author sabryr
 */
public class ProgressMonitor implements Runnable {

    private Statement st_1 = null;
    private long maxwait = 1000;

    public ProgressMonitor(Statement st_1, int maxwait) {
        this.st_1 = st_1;
        this.maxwait = maxwait * 1000;
    }

    @Override
    public void run() {
        monitor_timout();
    }

    public void monitor_timout() {
        try {
            long start = System.currentTimeMillis();
            boolean cancel = false;
            while (!cancel) {
                try {
                    Thread.sleep(10000);
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
                if (st_1 != null && !st_1.isClosed()) {

                    if ((System.currentTimeMillis() - start) > maxwait) {
                        st_1.cancel();
                        cancel = true;
                    }
                } else {
                    cancel = true;
                }
            }
        } catch (SQLException ex) {
        }
    }
}
