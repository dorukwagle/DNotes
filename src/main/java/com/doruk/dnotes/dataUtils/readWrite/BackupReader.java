package com.doruk.dnotes.dataUtils.readWrite;

import com.doruk.dnotes.DIFactory;
import com.doruk.dnotes.dataUtils.DataReader;
import com.doruk.dnotes.dataUtils.Markers;
import com.doruk.dnotes.dataUtils.MetaReader;
import com.doruk.dnotes.exceptions.ProcessingStageException;
import com.doruk.dnotes.utils.*;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public class BackupReader {
    private static void removeCurrentData() throws IOException {
        var dbPath = Path.of(DatabaseConnector.getDbPath());
        var notesDir = Path.of(PathUtils.getNotesDir());

        Files.deleteIfExists(dbPath);

        try (var stream = Files.newDirectoryStream(notesDir)) {
            for (Path note : stream)
                Files.deleteIfExists(note);
        }
    }

    public static MetaReader readMetadata(File backup) throws IOException {
        try (var backupIn = new BufferedInputStream(FileAccessManager.getInstance().openFileForRead(backup.toPath()))) {
            var metaReader = new MetaReader(backupIn);
            metaReader.parse();

            return metaReader;
        }
    }

    private static boolean invalidMarker(InputStream stream, byte marker) throws IOException {
        byte[] markerBytes = new byte[1];
        var read = stream.read(markerBytes);
        return read == -1 || markerBytes[0] != marker;
//        return stream.read(markerBytes) == -1 || markerBytes[0] != marker;
    }

    public static void readAndRestore(File backup, String password, boolean encrypted) throws IOException {
        if (encrypted && password == null)
            throw new IllegalArgumentException("Password cannot be null if encryption is applied");

        try (InputStream backupIn = new BufferedInputStream(FileAccessManager.getInstance().openFileForRead(backup.toPath()))) {
            // read metadata
            var metaReader = new MetaReader(backupIn);
            metaReader.parse();

            InputStream streamIn = backupIn;
            try {
                if (encrypted) {
                    var passwordHash = readPasswordHash(DIFactory.createObfuscator(streamIn, metaReader.getObfuscationSeed()));
                    if (!HashUtil.compareHash(password, passwordHash))
                        throw new IllegalArgumentException("The password you entered is incorrect!");

                    // create decryption stream
                    streamIn = DIFactory.createCryptoInputStream(streamIn, password);
                }

                // reset current data
                removeCurrentData();

                // create obfuscator stream
                streamIn = DIFactory.createObfuscator(streamIn, metaReader.getObfuscationSeed());

                // restore database
                restoreDatabase(streamIn);

                // restore files
                restoreFiles(streamIn);
            } finally {
                streamIn.close();
            }
        }
    }

    private static void restoreDatabase(InputStream stream) throws IOException {
        if (invalidMarker(stream, Markers.DATABASE_START))
            throw new ProcessingStageException("Invalid byte found while reading database");

        var dbPath = Path.of(DatabaseConnector.getDbPath());
        var dbLength = NumberUtils.continuousBytesToLong(stream);
        try (OutputStream out = new BufferedOutputStream(FileAccessManager.getInstance().openFileForWrite(dbPath))) {
            writeFile(stream, out, dbLength);
        }
    }

    private static void restoreFiles(InputStream stream) throws IOException {
        byte[] marker = new byte[1];
        while (stream.read(marker) != -1) {
            if (marker[0] != Markers.DOC_START)
                throw new ProcessingStageException("Invalid byte found while reading files");

            if (invalidMarker(stream, Markers.Keys.DOC_NAME_KEY))
                throw new ProcessingStageException("Invalid byte found for document name");

            int nameLength = (int) NumberUtils.continuousBytesToLong(stream);
            byte[] nameBytes = new byte[nameLength];
            DataReader.readFully(stream, nameBytes);

            String name = new String(nameBytes, StandardCharsets.UTF_8);
            var docPath = Path.of(PathUtils.join(PathUtils.getNotesDir(), name));

            if (invalidMarker(stream, Markers.DOC_DATA_START))
                throw new ProcessingStageException("Invalid byte found for document data");

            int dataLength = (int) NumberUtils.continuousBytesToLong(stream);
            try (OutputStream out = new BufferedOutputStream(FileAccessManager.getInstance().openFileForWrite(docPath))) {
                writeFile(stream, out, dataLength);
            }
        }
    }

    private static String readPasswordHash(InputStream stream) throws IOException {
        byte[] marker = new byte[1];
        if (stream.read(marker) == -1 || marker[0] != Markers.Keys.DOC_INTERNAL_PASSWORD)
            throw new ProcessingStageException("Invalid byte found while reading password hash");

        int length = (int) NumberUtils.continuousBytesToLong(stream);
        byte[] passwordHashBytes = new byte[length];
        DataReader.readFully(stream, passwordHashBytes);

        return new String(passwordHashBytes, StandardCharsets.UTF_8);
    }

    private static void writeFile(InputStream in, OutputStream out, long fileLength) throws IOException {
        var remainingBytes = fileLength;
        var chunkSize = 2048; // 2kb

        byte[] chunk = new byte[chunkSize];
        int expectedBytes = (int) Math.min(remainingBytes, chunkSize);

        while (true) {
            DataReader.readFully(in, chunk, expectedBytes);

            out.write(chunk, 0, expectedBytes);

            remainingBytes -= expectedBytes;

            if (remainingBytes == 0)
                break;
            if (remainingBytes < 0)
                throw new ProcessingStageException("File length, and expected length mismatch");
        }
    }
}
