/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package no.ntnu.medisin.egenvar.cmd;

import java.io.File;
import java.io.FilenameFilter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author sabryr
 */
public class EgenFileFilter implements FilenameFilter {

    private String pattern;

    public EgenFileFilter(String pattern) {
        this.pattern = pattern;
    }

    @Override
    public boolean accept(File dir, String name) {
        Pattern p1 = Pattern.compile(pattern);
        Matcher m = p1.matcher(name);
        if (m.matches()) {
            return true;
        }
        return false;
    }
}
