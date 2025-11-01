package com.doruk.dnotes.utils;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FilterInputStream;
import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Path;
import java.util.Optional;

public final class FileAccessManager {
    private static final FileAccessManager INSTANCE = new FileAccessManager();
    private Path openedFileRead;
    private boolean readInUse;
    private Path openedFileWrite;
    private boolean writeInUse;

    private FileAccessManager() {}

    public static FileAccessManager getInstance() {
        return INSTANCE;
    }

    private void setOpenRead(Path path) {
        readInUse = true;
        openedFileRead = path;
    }

    private void setCloseRead() {
        readInUse = false;
        openedFileRead = null;
    }

    private void setOpenWrite(Path path) {
        writeInUse = true;
        openedFileWrite = path;
    }

    private void setCloseWrite() {
        writeInUse = false;
        openedFileWrite = null;
    }

    public synchronized OutputStream openFileForWrite(Path path) throws IOException {
        ensureNoActiveWrite();
        OutputStream stream = new FileOutputStream(path.toFile());
        setOpenWrite(path);

        return new FilterOutputStream(stream) {
            @Override
            public void close() throws IOException {
                super.close();
                synchronized (FileAccessManager.this) {
                    setCloseWrite();
                }
            }
        };
    }

    public synchronized InputStream openFileForRead(Path path) throws IOException {
        ensureNoActiveRead();
        InputStream stream = new FileInputStream(path.toFile());
        setOpenRead(path);

        return new FilterInputStream(stream) {
            @Override
            public void close() throws IOException {
                super.close();
                synchronized (FileAccessManager.this) {
                    setCloseRead();
                }
            }
        };
    }

    private void ensureNoActiveRead() {
        if (readInUse && openedFileRead != null) {
            throw new IllegalStateException(
                    "Another file is currently open: " + openedFileRead
            );
        }
    }

    private void ensureNoActiveWrite() {
        if (writeInUse && openedFileWrite != null) {
            throw new IllegalStateException(
                    "Another file is currently open: " + openedFileWrite
            );
        }
    }

    public synchronized Optional<Path> getOpenedFileRead() {
        return Optional.ofNullable(openedFileRead);
    }

    public synchronized Optional<Path> getOpenedFileWrite() {
        return Optional.ofNullable(openedFileWrite);
    }
}
