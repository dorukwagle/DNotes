package com.doruk.dnotes.interfaces;

import java.io.OutputStream;
import java.util.stream.Stream;

import com.doruk.dnotes.MarkdownEditor.codecs.dto.ParagraphNode;
import com.doruk.dnotes.exceptions.ProcessingStageException;

public interface MarkdownEncoder {
    void encode(Stream<ParagraphNode> nodes, OutputStream out) throws ProcessingStageException;
}
