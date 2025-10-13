package com.doruk.dnotes.utils;

import com.doruk.dnotes.store.GlobalConstants;

import java.io.File;
import java.util.Date;
import java.util.UUID;

public class PathUtils {

    private static String createDirRecursive(String path) {
        File dir = new File(path);
        if (!dir.exists()) dir.mkdirs();
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

        return createDirRecursive(baseDir + File.separator + GlobalConstants.PACKAGE_NAME);
    }

    public static String getNotesDir() {
        return createDirRecursive(getDataDir() + File.separator + "notes");
    }

    public static String getDatabaseDir() {
        return createDirRecursive(getDataDir() + File.separator + "database");
    }

    public static String getLogDir() {
        return createDirRecursive(System.getProperty("user.home") + File.separator + GlobalConstants.APP_NAME + File.separator + "logs");
    }

    public static String getBackupDir() {
        return createDirRecursive(System.getProperty("user.home") + File.separator + GlobalConstants.APP_NAME + File.separator + "backups");
    }

    public static String getShareDir() {
        return createDirRecursive(System.getProperty("user.home") + File.separator + GlobalConstants.APP_NAME + File.separator + "shared");
    }

    public static String generateFileId() {
        return String.join("",
                UUID.randomUUID()
                        .toString()
                        .substring(9)
                        .split("-")
        );
    }

    public static String generateNoteFilename(String fileId) {
        var uid = String.join("",
                fileId
        );

        var dt = String.valueOf(new Date().getTime()).substring(2);

        return join(getNotesDir(), uid + dt + GlobalConstants.APP_FORMAT);
    }

    public static String name(String path) {
        return path.substring(path.lastIndexOf("/") + 1, path.lastIndexOf("."));
    }

    public static String join(String... paths) {
        return String.join(File.separator, paths);
    }
}
