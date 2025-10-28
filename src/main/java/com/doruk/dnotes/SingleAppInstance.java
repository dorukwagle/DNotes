package com.doruk.dnotes;


import com.doruk.dnotes.utils.PathUtils;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.channels.FileLock;

public class SingleAppInstance {

    private static FileLock lock;
    private static RandomAccessFile lockFile;

    public static boolean lockInstance() {
        try {
            File file = new File(PathUtils.join(PathUtils.getDatabaseDir(), ".dNotes.lock"));
            lockFile = new RandomAccessFile(file, "rw");
            lock = lockFile.getChannel().tryLock();
            return lock != null;
        } catch (IOException e) {
            return false;
        }
    }

    public static void releaseLock() {
        try {
            if (lock != null) lock.release();
            if (lockFile != null) lockFile.close();
        } catch (IOException ignored) {}
    }
}
