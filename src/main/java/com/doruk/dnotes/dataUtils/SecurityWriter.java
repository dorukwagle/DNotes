package com.doruk.dnotes.dataUtils;

import com.doruk.dnotes.DIFactory;
import com.doruk.dnotes.utils.FileAccessManager;
import com.doruk.dnotes.utils.PathUtils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class SecurityWriter {
    public static void applyPasswordProtection(String fileName, String password) throws IOException {
        var path = Path.of(PathUtils.getNoteFilename(fileName));
        var tempPath = Path.of(PathUtils.getNoteFilename(fileName) + ".tmp");

        // create input/output file
        try (
                var input = new BufferedInputStream(FileAccessManager.getInstance().openFileForRead(path));
                var output = new BufferedOutputStream(FileAccessManager.getInstance().openFileForWrite(tempPath))
        ) {
            var metaReader = new MetaReader(input);
            metaReader.parse();

            var metaWriter = new MetaWriter(output);
            metaWriter.writeDefaultsMeta(metaReader.getCreatedAt(), metaReader.getObfuscationSeed(), true);
            metaWriter.commit();

            // now copy file contents
            try (var outputStream = DIFactory.createCryptoOutputStream(output, password)) {
                var buffer = new byte[1024];
                int bytesRead;
                while ((bytesRead = input.read(buffer)) != -1)
                    outputStream.write(buffer, 0, bytesRead);

                outputStream.flush();
            }

            // now cleanup the temp files
            Files.deleteIfExists(path);
            Files.move(tempPath, path);
        }
    }

    public static void removePasswordProtection(String fileName, String password) throws IOException {
        var path = Path.of(PathUtils.getNoteFilename(fileName));
        var tempPath = Path.of(PathUtils.getNoteFilename(fileName) + ".tmp");

        try (
                var input = new BufferedInputStream(FileAccessManager.getInstance().openFileForRead(path));
                var output = new BufferedOutputStream(FileAccessManager.getInstance().openFileForWrite(tempPath))
        ) {
            var metaReader = new MetaReader(input);
            metaReader.parse();

            var metaWriter = new MetaWriter(output);
            metaWriter.writeDefaultsMeta(metaReader.getCreatedAt(), metaReader.getObfuscationSeed(), false);
            metaWriter.commit();

            // now copy file contents
            var decrypted = DIFactory.createCryptoInputStream(input, password);

            var buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = decrypted.read(buffer)) != -1)
                output.write(buffer, 0, bytesRead);

            output.flush();

            // now cleanup the temp files
            Files.deleteIfExists(path);
            Files.move(tempPath, path);
        }
    }
}
