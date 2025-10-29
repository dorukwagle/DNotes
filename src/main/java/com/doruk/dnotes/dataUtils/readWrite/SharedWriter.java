package com.doruk.dnotes.dataUtils.readWrite;

import com.doruk.dnotes.DIFactory;
import com.doruk.dnotes.dataUtils.Markers;
import com.doruk.dnotes.dataUtils.MetaWriter;
import com.doruk.dnotes.dto.BookPageDto;
import com.doruk.dnotes.exceptions.ProcessingStageException;
import com.doruk.dnotes.utils.KeyUtil;
import com.doruk.dnotes.utils.NumberUtils;
import com.doruk.dnotes.utils.PathUtils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Date;

public class SharedWriter {
    public static void write(BookPageDto note, String sharedBy) throws ProcessingStageException {
        var inPath = Path.of(PathUtils.getNoteFilename(note.getContentId()));

        try (var inFile = new BufferedInputStream(Files.newInputStream(inPath));
             var outFile = new BufferedOutputStream(Files.newOutputStream(Path.of(
                     PathUtils.generateSharedNoteFile(note.getName()))
             ))
        ) {
            var obfuscationSeed = KeyUtil.generateSeed();
            var metaWriter = new MetaWriter(outFile);
            metaWriter.writeSharedMeta(obfuscationSeed, sharedBy, new Date());
            metaWriter.commit();

            var obfuscator = DIFactory.createObfuscator(outFile, obfuscationSeed);

            // write document start byte
            obfuscator.write(Markers.DOC_START);

            // write note name
            var noteNameBytes = note.getName().getBytes(StandardCharsets.UTF_8);
            // write marker and length
            obfuscator.write(Markers.Keys.DOC_NAME_KEY);
            var noteNameLengthBytes = NumberUtils.toContinuationBytes(noteNameBytes.length);
            for (byte b : noteNameLengthBytes)
                obfuscator.write(b);

            // write note name
            obfuscator.write(noteNameBytes);

            if (note.getIsLocked()) writePasswordHash(note, obfuscator);

            // write note start byte
            obfuscator.write(Markers.DOC_DATA_START);

            // write note content
            byte[] chunk = new byte[1024]; // 1kb
            int bytesRead;
            while ((bytesRead = inFile.read(chunk)) != -1)
                obfuscator.write(chunk, 0, bytesRead);
        } catch (IOException e) {
            throw new ProcessingStageException("Failed to write note content", e);
        }
    }

    private static void writePasswordHash(BookPageDto note, OutputStream out) throws IOException {
        // write password hash
        var passwordHashBytes = note.getPassword().getBytes(StandardCharsets.UTF_8);
        // write marker and length
        out.write(Markers.Keys.DOC_INTERNAL_PASSWORD);
        var passwordHashLengthBytes = NumberUtils.toContinuationBytes(passwordHashBytes.length);
        for (byte b : passwordHashLengthBytes)
            out.write(b);

        // write password hash
        out.write(passwordHashBytes);
    }
}
