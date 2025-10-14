package com.doruk.dnotes.interfaces;

import java.io.InputStream;
import java.util.function.Consumer;

import com.doruk.dnotes.MarkdownEditor.codecs.dto.ParagraphNode;

public interface MarkdownDecoder {
    public void decode(InputStream input, Consumer<ParagraphNode> consumer);
}
