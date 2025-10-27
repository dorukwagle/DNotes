package com.doruk.dnotes.dataUtils.readWrite;

import com.doruk.dnotes.DIFactory;
import com.doruk.dnotes.dataUtils.Markers;
import com.doruk.dnotes.dataUtils.MetaWriter;
import com.doruk.dnotes.dto.BookPageDto;
import com.doruk.dnotes.exceptions.ProcessingStageException;
import com.doruk.dnotes.utils.*;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Date;

public class BackupWriter {
    public static void write(String password, boolean encrypt) throws IOException {
        if (password == null && encrypt)
            throw new IllegalArgumentException("Password cannot be null if encryption is applied");

        var dbFilePath = Path.of(DatabaseConnector.getDbPath());
        var notesDir = Path.of(PathUtils.getNotesDir());
        var backupFile = Path.of(PathUtils.join(PathUtils.getBackupDir(), PathUtils.generateSharedNoteFile("backup")));

        try (
                var dbIn = new BufferedInputStream(Files.newInputStream(dbFilePath));
                var dirStream = Files.newDirectoryStream(notesDir);
                var outFile = new BufferedOutputStream(Files.newOutputStream(backupFile))
                ) {
            var seed = KeyUtil.generateSeed();
            var metaWriter = new MetaWriter(outFile);
            metaWriter.writeBackupMeta(seed, encrypt, new Date());
            metaWriter.commit();

            OutputStream stream = outFile;

            // write password hash if encrypted
            if (encrypt) writePasswordHash(password, DIFactory.createObfuscator(stream, seed));

            if (encrypt) // apply the encryption
                stream = DIFactory.createCryptoOutputStream(stream, password);

            stream = DIFactory.createObfuscator(
                            stream,
                            seed
            );

            // write database start
            stream.write(Markers.DATABASE_START);

            // write length
            writeLength(stream, Files.size(dbFilePath));

            // write database
            writeFile(dbIn, stream, Files.size(dbFilePath));

            // write notes
            writeNotes(dirStream, stream);
        }
    }

    private static void writeNotes(DirectoryStream<Path> dirStream, OutputStream stream) throws IOException {
        for (Path note : dirStream) {
            // write doc start
            stream.write(Markers.DOC_START);

            // write name
            stream.write(Markers.Keys.DOC_NAME_KEY);
            var nameBytes = note.getFileName().toString().getBytes(StandardCharsets.UTF_8);
            // write marker and length
            writeLength(stream, nameBytes.length);
            // write name bytes
            stream.write(nameBytes);

            // write data start byte
            stream.write(Markers.DOC_DATA_START);

            // write data length
            writeLength(stream, Files.size(note));

            // write data
            try (var noteIn = new BufferedInputStream(Files.newInputStream(note))) {
                writeFile(noteIn, stream, Files.size(note));
            }
        }
    }

    private static void writeLength(OutputStream stream, long length) throws IOException {
        var lengthBytes = NumberUtils.toContinuationBytes(length);
        for (byte b : lengthBytes)
            stream.write(b);
    }

    private static void writeFile(InputStream in, OutputStream out, long fileLength) throws IOException {
        var remainingBytes = fileLength;
        var chunkSize = 2048; // 2kb

        byte[] chunk = new byte[chunkSize];
        int bytesRead;
        while ((bytesRead = in.read(chunk)) != -1) {

            out.write(chunk, 0, bytesRead);

            remainingBytes -= bytesRead;

            if (remainingBytes == 0)
                break;
            if (remainingBytes < 0)
                throw new ProcessingStageException("File length, and expected length mismatch");
        }
    }

    private static void writePasswordHash(String password, OutputStream out) throws IOException {
        // write password hash
        var passwordHashBytes = HashUtil.hash(password).getBytes(StandardCharsets.UTF_8);
        // write marker and length
        out.write(Markers.Keys.DOC_INTERNAL_PASSWORD);
        var passwordHashLengthBytes = NumberUtils.toContinuationBytes(passwordHashBytes.length);
        for (byte b : passwordHashLengthBytes)
            out.write(b);

        // write password hash
        out.write(passwordHashBytes);
    }
}
