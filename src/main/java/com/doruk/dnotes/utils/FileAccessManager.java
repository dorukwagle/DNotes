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
    private Path openedFile;
    private boolean inUse;

    private FileAccessManager() {}

    public static FileAccessManager getInstance() {
        return INSTANCE;
    }

    private void setOpen(Path path) {
        inUse = true;
        openedFile = path;
    }

    private void setClose() {
        inUse = false;
        openedFile = null;
    }

    public synchronized OutputStream openFileForWrite(Path path) throws IOException {
        ensureNoActiveAccess();
        OutputStream stream = new FileOutputStream(path.toFile());
        setOpen(path);

        return new FilterOutputStream(stream) {
            @Override
            public void close() throws IOException {
                super.close();
                synchronized (FileAccessManager.this) {
                    setClose();
                }
            }
        };
    }

    public synchronized InputStream openFileForRead(Path path) throws IOException {
        ensureNoActiveAccess();
        InputStream stream = new FileInputStream(path.toFile());
        setOpen(path);

        return new FilterInputStream(stream) {
            @Override
            public void close() throws IOException {
                super.close();
                synchronized (FileAccessManager.this) {
                    setClose();
                }
            }
        };
    }

    private void ensureNoActiveAccess() {
        if (inUse && openedFile != null) {
            throw new IllegalStateException(
                    "Another file is currently open: " + openedFile
            );
        }
    }

    public synchronized Optional<Path> getOpenedFile() {
        return Optional.ofNullable(openedFile);
    }
}
