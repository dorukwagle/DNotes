package com.doruk.dnotes.dataUtils.parser;

import java.io.InputStream;
import java.util.function.Consumer;

import com.doruk.dnotes.MarkdownEditor.codecs.dto.ParagraphNode;
import com.doruk.dnotes.interfaces.MarkdownDecoder;

public class BinaryMarkdownDecoder extends BinaryParser implements MarkdownDecoder {
    public BinaryMarkdownDecoder(String[] codecsName) {
        super(codecsName);
    }

    @Override
    public void decode(InputStream input, Consumer<ParagraphNode> consumer) {
        
    }
}
