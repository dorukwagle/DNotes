package com.doruk.dnotes.dataUtils.readWrite;

import com.doruk.dnotes.DIFactory;
import com.doruk.dnotes.dataUtils.StreamDataUtil;
import com.doruk.dnotes.dataUtils.Markers;
import com.doruk.dnotes.dataUtils.MetaReader;
import com.doruk.dnotes.dto.BookPageDto;
import com.doruk.dnotes.exceptions.ProcessingStageException;
import com.doruk.dnotes.utils.FileAccessManager;
import com.doruk.dnotes.utils.NumberUtils;
import com.doruk.dnotes.utils.PathUtils;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;

public class SharedReader {
    public static BookPageDto read(File file) throws ProcessingStageException {
        var contentId = PathUtils.generateFileId();
        var outPath = Path.of(PathUtils.getNoteFilename(contentId));
        try (
                var inFile = new BufferedInputStream(FileAccessManager.getInstance().openFileForRead(file.toPath()));
                var outFile = new BufferedOutputStream(FileAccessManager.getInstance().openFileForWrite(outPath))
                ) {
            var metaReader = new MetaReader(inFile);
            metaReader.parse();

            if (metaReader.getFileType() != Markers.FileType.SHARED)
                throw new IllegalArgumentException("Invalid file type. Please select a shared file.");

            var decipher = DIFactory.createObfuscator(inFile, metaReader.getObfuscationSeed());
            return loadDocument(decipher, outFile, metaReader, contentId);
        } catch (IOException e) {
            throw new ProcessingStageException(e.getMessage(), e);
        }
    }

    private static boolean verifyMarker(InputStream stream, byte rhs) throws IOException {
        byte[] marker = new byte[1];
        return stream.read(marker) != -1 && marker[0] == rhs;
    }

    private static BookPageDto loadDocument(InputStream in, OutputStream out, MetaReader meta, String contentId) {
        var errMsg = "Invalid byte found while reading document start";
        try {
            if (!verifyMarker(in, Markers.DOC_START))
                throw new ProcessingStageException(errMsg);

            // read doc name
            if (!verifyMarker(in, Markers.Keys.DOC_NAME_KEY))
                throw new ProcessingStageException(errMsg);

            var nameLength = (int)NumberUtils.continuousBytesToLong(in);
            if (nameLength < 1) throw new ProcessingStageException("Invalid name length byte");

            var name = new byte[nameLength];
            StreamDataUtil.readFully(in, name);

            var docName = new String(name, StandardCharsets.UTF_8);

            byte[] m = {0};
            in.read(m);
            if (m[0] != Markers.DOC_DATA_START && m[0] != Markers.Keys.DOC_INTERNAL_PASSWORD)
                throw new ProcessingStageException(errMsg);

            // create a note dto
            var note = new BookPageDto(null, null, meta.getSharedBy() + " - " + docName, contentId, meta.getCreatedAt().toString());

            var hasPassword = m[0] == Markers.Keys.DOC_INTERNAL_PASSWORD;
            if (hasPassword) {
                // read password hash
                var len = (int)NumberUtils.continuousBytesToLong(in);
                if (len < 1) throw new ProcessingStageException("Invalid password hash length byte");

                var passwordHash = new byte[len];
                StreamDataUtil.readFully(in, passwordHash);

                note.setPassword(new String(passwordHash, StandardCharsets.UTF_8));
                note.setIsLocked(true);

                // also consume the next marker ( i.e. DOC_DATA_START )
                if (!verifyMarker(in, Markers.DOC_DATA_START))
                    throw new ProcessingStageException("Invalid byte found while reading document start");
            }

            // read the rest of the document
            byte[] chunk = new byte[1024];
            int bytesRead;
            while ((bytesRead = in.read(chunk)) != -1)
                out.write(chunk, 0, bytesRead);

            return note;
        } catch (IOException e) {
            throw new ProcessingStageException(e.getMessage(), e);
        }
    }
}
