package com.doruk.dnotes.dataUtils;

import java.io.OutputStream;
import java.util.stream.Stream;

import com.doruk.dnotes.MarkdownEditor.codecs.dto.ParagraphNode;
import com.doruk.dnotes.interfaces.MarkdownEncoder;

public class BinaryMarkdownEncoder extends BinaryParser implements MarkdownEncoder {
    public BinaryMarkdownEncoder(String[] codecsName) {
        super(codecsName);
    }

    @Override
    public void encode(Stream<ParagraphNode> nodes, OutputStream output) {
        
    }
}
