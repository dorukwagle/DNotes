package com.doruk.dnotes.utils;

import java.io.File;

public class PathUtils {
    
    private static String createDirRecursive(String path) {
        File dir = new File(path);
        if (!dir.exists())
            dir.mkdirs();
        return path;
    }

    public static String getDataDir() {
        String os = System.getProperty("os.name").toLowerCase();
        String baseDir;

        if (os.contains("win")) {
            baseDir = System.getenv("APPDATA");
        } else if (os.contains("mac")) {
            baseDir = System.getProperty("user.home") + "/Library/Application Support";
        } else {
            baseDir = System.getProperty("user.home") + "/.local/share";
        }

        return createDirRecursive(baseDir + "/com.doruk.dnotes");
    }

    public static String getNotesDir() {
        return createDirRecursive(getDataDir() + "/notes");
    }

    public static String getDatabaseDir() {
        return createDirRecursive(getDataDir() + "/database");
    }
}
