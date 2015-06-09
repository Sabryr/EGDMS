/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package no.ntnu.medisin.egenvar.server;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;

/**
 *
 * @author sabryr
 */
public class LoadRecursive extends SimpleFileVisitor<Path> {

    ArrayList<Path> ok_l;
    ArrayList<Path> avoid_l;
    private final long MEM_terminate = 8388608;
    private boolean skip_hidden;

    public LoadRecursive(boolean skip_hidden) {
        this.skip_hidden = skip_hidden;
        ok_l = new ArrayList<>(1000);
        avoid_l = new ArrayList<>(1000);
    }

    public ArrayList<Path> getOKlist() {
        return ok_l;
    }

    public ArrayList<Path> avoidOKlist() {
        return avoid_l;
    }

    public ArrayList<String> avoidOKlist_string() {
        ArrayList<String> avoid_s_l = new ArrayList<>(avoid_l.size());
        for (int i = 0; i < avoid_l.size(); i++) {
            avoid_s_l.add(avoid_l.get(i).toString());
        }
        return avoid_s_l;
    }

    @Override
    public FileVisitResult visitFile(Path file, BasicFileAttributes attr) {
        try {
            if (Files.isHidden(file)) {
                avoid_l.add(file);
            } else {
                if (attr.isRegularFile() && attr.size()>1) {
                    try {
                        ok_l.add(file.toRealPath());
                    } catch (IOException ex) {
                        ok_l.add(file);
                    }
                    int files_analysed_1 = ok_l.size();
                    if (files_analysed_1 % 50 == 0) {
                        System.out.print(".");
                        if (files_analysed_1 % 1000 == 0) {
                            long freemem = Runtime.getRuntime().freeMemory();
                            String warn = "";
                            if (Start_EgenVar1.MIN_MEM > freemem) {
                                warn = "Free Memory geeting low !";
                                if (MEM_terminate > freemem) {
                                    System.out.println("Error: not enough memory to continue, avoiding subdirectories");

                                }
                            }
                            System.out.println(files_analysed_1 + " files found " + (freemem / 1048576) + " Mb free. Current=" + file.toString() + warn);
//                        System.out.println("4914 OK=" + ok_l.size() + "  AVOID=" + avoid_l.size());
                        }
                    }
                } else {
                    avoid_l.add(file);
                }
            }
        } catch (IOException ex) {         
            avoid_l.add(file);
        }
        return FileVisitResult.CONTINUE;
    }

    // Print each directory visited.
    @Override
    public FileVisitResult postVisitDirectory(Path dir, IOException exc) {
        try {
            if (skip_hidden && Files.isHidden(dir)) {
                avoid_l.add(dir);
                return FileVisitResult.SKIP_SUBTREE;
            } else {
                if (!Files.isReadable(dir)) {
                    avoid_l.add(dir);
                    return FileVisitResult.SKIP_SUBTREE;
                }
            }
        } catch (IOException ex) {
            System.out.println("Error: " + ex.getMessage());
        }
        return FileVisitResult.CONTINUE;
    }

    // Print each directory visited.
    @Override
    public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attr) {
        try {
            if (skip_hidden && Files.isHidden(dir)) {
                avoid_l.add(dir);
                return FileVisitResult.SKIP_SUBTREE;
            } else {
                if (!Files.isReadable(dir)) {
                    avoid_l.add(dir);
                    return FileVisitResult.SKIP_SUBTREE;
                }
            }
        } catch (IOException ex) {
            System.out.println("Error: " + ex.getMessage());
        }
        return FileVisitResult.CONTINUE;
    }

    // If there is some error accessing
    // the file, let the user know.
    // If you don't override this method
    // and an error occurs, an IOException 
    // is thrown.
    @Override
    public FileVisitResult visitFileFailed(Path file, IOException exc) {
        avoid_l.add(file);
        return FileVisitResult.CONTINUE;
    }
}
