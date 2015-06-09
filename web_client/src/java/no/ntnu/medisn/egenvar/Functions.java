package no.ntnu.medisn.egenvar;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author sabryr
 */
public class Functions {

    public Functions() {
    }

    public static String replaceAll(String inp, String regex, String replacewith) {
        return inp.replaceAll(regex, replacewith);
    }
     public static boolean matchExcatIgnoreCase(String inp, String inp2) {
        return inp.equalsIgnoreCase(inp2);
    }
//      public static String getNumberOfusers(){
//        return inp.replaceAll( regex,replacewith);
//    }
}
