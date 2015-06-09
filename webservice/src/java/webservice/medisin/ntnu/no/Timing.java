/*
 * Timing.java
 *
 * Created on June 3, 2007, 4:32 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package webservice.medisin.ntnu.no;

import java.text.DateFormatSymbols;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;

/**
 *
 * @author sabryr
 */
public class Timing {

    static Locale locale = new Locale(Locale.ENGLISH.getLanguage());
    static DateFormatSymbols format = new DateFormatSymbols(locale);
    private static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
    private static final String DATE_FORMAT_FILE_NAME = "yyyy_MM_dd_HH_mm_ss";
    private static final String DATE_FORMAT_2 = "MM-dd-yyyy";
    private static final String HOUR_FORMAT = "HH";
    private static final String WEEK_DAY = "E";
    private static final java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat(DATE_FORMAT);
    private static final java.text.SimpleDateFormat sdf_2 = new java.text.SimpleDateFormat(DATE_FORMAT_2);
    private static final java.text.SimpleDateFormat sdf_3 = new java.text.SimpleDateFormat(DATE_FORMAT_FILE_NAME);
    private static final java.text.SimpleDateFormat sdf_H = new java.text.SimpleDateFormat(HOUR_FORMAT);
    private static final java.text.SimpleDateFormat sdf_D = new java.text.SimpleDateFormat(WEEK_DAY, format);
    private static long start_time2;
    private static long start_time;

    public Timing() {
    }

    public static String getDateTime() {
        sdf.setTimeZone(TimeZone.getDefault());
        Calendar cal = Calendar.getInstance(TimeZone.getDefault());
        return sdf.format(cal.getTime());
    }

    public static String getDateTimeForFileName() {
        sdf_3.setTimeZone(TimeZone.getDefault());
        Calendar cal = Calendar.getInstance(TimeZone.getDefault());
        return sdf_3.format(cal.getTime());
    }

    public static String getDate() {
        sdf.setTimeZone(TimeZone.getDefault());
        Calendar cal = Calendar.getInstance(TimeZone.getDefault());
        return sdf.format(cal.getTime());
    }

    public static String getDate_compact_USA() {
        sdf_2.setTimeZone(TimeZone.getDefault());
        Calendar cal = Calendar.getInstance(TimeZone.getDefault());
        return sdf_2.format(cal.getTime());
    }

    public static int getHour() {
        sdf_H.setTimeZone(TimeZone.getDefault());
        Calendar cal = Calendar.getInstance(TimeZone.getDefault());
        int hour = -1;
        try {
            hour = new Integer(sdf_H.format(cal.getTime()));
        } catch (NumberFormatException ex) {
            System.out.println("Error hour deciding error");
        }
        return hour;
    }

    public static String getDay() {
        sdf_D.setTimeZone(TimeZone.getDefault());
        Calendar cal = Calendar.getInstance(TimeZone.getDefault());
        return sdf_D.format(cal.getTime());
    }

    public static void setPointer() {
        start_time = (new GregorianCalendar()).getTimeInMillis();
    }

    public static void setPointer2() {
        start_time2 = (new GregorianCalendar()).getTimeInMillis();
    }

    public static long getFromlastPointer() {
        long end_time = (new GregorianCalendar()).getTimeInMillis();
        return (end_time - start_time);

    }

    public static long getFromlastPointer2() {
        long end_time = (new GregorianCalendar()).getTimeInMillis();
        return (end_time - start_time2);

    }

    public static String convert(long milliseconds) {
        long seconds = milliseconds / 1000;
        String out = "";
        if (seconds >= (86400)) {
            int days = (int) (seconds / 86400);
            out = days + " days";
            int hours = (int) ((seconds % 86400) / 3600);
            out = out + " :" + hours + " hours";
            int minutes = (int) (((seconds % 86400) % 3600) / 60);
            out = out + " :" + minutes + " minutes";
            seconds = (int) (((seconds % 86400) % 3600) % 60);
            out = out + ":" + seconds + " seconds";
        } else if (seconds >= (3600)) {
            int hours = (int) ((seconds % 86400) / 3600);
            out = hours + " hours";
            int minutes = (int) ((seconds % 3600) / 60);
            out = out + " :" + minutes + " minutes";
            seconds = (int) ((seconds % 3600) % 60);
            out = out + " :" + seconds + " seconds";
        } else if (seconds >= (60)) {
            int minutes = (int) ((seconds % 3600) / 60);
            out = minutes + " minutes";
            seconds = (int) ((seconds % 3600) % 60);
            out = out + " :" + seconds + " seconds";
        } else if (seconds >= 1) {
            out = seconds + " seconds";
        } else {
            out = milliseconds + " milliseconds";
        }
        return out;
    }
}
