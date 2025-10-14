package com.doruk.dnotes.dataUtils.readWrite;

import com.doruk.dnotes.DIFactory;
import com.doruk.dnotes.MarkdownEditor.interfaces.IMarkdownEditor;
import com.doruk.dnotes.dataUtils.MetaWriter;
import com.doruk.dnotes.dataUtils.obfuscator.ObfuscatorOutputStream;
import com.doruk.dnotes.interfaces.IWriter;
import com.doruk.dnotes.utils.KeyUtil;
import com.doruk.dnotes.utils.PathUtils;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Date;
import java.util.zip.GZIPOutputStream;

public class NoteWriter implements IWriter {
    private final IMarkdownEditor editor;

    public NoteWriter(IMarkdownEditor editor) {
        this.editor = editor;
    }

    @Override
    public void write(String filename) throws IOException {
        // convert fileId to full path
        var filePath = Path.of(PathUtils.getNoteFilename(filename));

        // encode the markdown data into nodes, then create encoder to further encode them
        var nodes = editor.encodeAndDump();
        var encoder = DIFactory.createMarkdownEncoder(editor.getCodecsValues());

        // generate seed to obfuscate data
        var seed = KeyUtil.generateSeed();

        // create file output stream
        var fileOut = Files.newOutputStream(filePath);

        // create meta writer
        var metaWriter = new MetaWriter(fileOut);
        // write default metadata
        metaWriter.writeDefaultsMeta(new Date(), seed);
        metaWriter.commit();

        // create a pipeline of streams for processing the data
        var stream = new BufferedOutputStream(
                new GZIPOutputStream(
                        new ObfuscatorOutputStream(
                                fileOut,
                                seed
                        )
                )
        );

        encoder.encode(nodes, stream);
        stream.flush();
        stream.close();
    }
}
