package com.doruk.dnotes.dataUtils.readWrite;

import com.doruk.dnotes.DIFactory;
import com.doruk.dnotes.MarkdownEditor.interfaces.IMarkdownEditor;
import com.doruk.dnotes.dataUtils.Markers;
import com.doruk.dnotes.dataUtils.MetaReader;
import com.doruk.dnotes.exceptions.ProcessingStageException;
import com.doruk.dnotes.interfaces.IReader;
import com.doruk.dnotes.store.GlobalConstants;
import com.doruk.dnotes.utils.FileAccessManager;
import com.doruk.dnotes.utils.PathUtils;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.zip.GZIPInputStream;

public class NoteReader implements IReader {
    private final IMarkdownEditor editor;
    private final String password;
    private boolean encrypted = false;

    public NoteReader(IMarkdownEditor editor) {
        this.editor = editor;
        this.password = null;
    }

    public NoteReader(IMarkdownEditor editor, String password) {
        this.editor = editor;
        this.password = password;
        this.encrypted = true;
    }

    private void validateMeta(MetaReader metaReader) throws ProcessingStageException {
        if (metaReader.getFileType() != Markers.FileType.NORMAL)
            throw new ProcessingStageException("Invalid file type");

        if (metaReader.getObfuscationSeed().length != GlobalConstants.OBFUSCATION_SEED_LENGTH)
            throw new ProcessingStageException("Invalid File transformation or obfuscation seed");

        // when file is encrypted, but no encryption password is given
        if (metaReader.isDocEncrypted() && !encrypted)
            throw new ProcessingStageException("File is encrypted, but no encryption password is given");
    }

    @Override
    public void read(String fileId) throws IOException {
        // convert fileId to full path
        var filePath = Path.of(PathUtils.getNoteFilename(fileId));

        // check if the file exists
        if (!Files.exists(filePath))
            return;

        // create decoder to decode binary file and process data
        var decoder = DIFactory.createMarkdownDecoder(editor.getCodecsValues());

        // create a file input stream
        InputStream fileIn = new BufferedInputStream(FileAccessManager.getInstance().openFileForRead(filePath));

        try {
            // read the file metadata
            var metaReader = new MetaReader(fileIn);
            metaReader.parse();

            // validate if the file meta is correct for processing notes.
            this.validateMeta(metaReader);

            // get the obfuscation seed
            var seed = metaReader.getObfuscationSeed();

            // create file input stream pipeline
            if (metaReader.isDocEncrypted()) // if file is encrypted, apply decryptor
                fileIn = DIFactory.createCryptoInputStream(fileIn, password);

            try (var stream = new GZIPInputStream(DIFactory.createObfuscator(
                    fileIn, seed))) {
                // pass the stream to the decoder to decode and load the data
                decoder.decode(stream, editor::decodeAndLoad);
            }
        } finally {
            fileIn.close();
        }
    }
}
