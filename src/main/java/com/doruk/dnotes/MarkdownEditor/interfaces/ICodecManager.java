package com.doruk.dnotes.MarkdownEditor.interfaces;

import java.util.stream.Stream;

import com.doruk.dnotes.MarkdownEditor.codecs.dto.ParagraphNode;

public interface ICodecManager {
    Stream<ParagraphNode> dumpEditorDocument(FXTextEditor editor);
    void loadEditorDocument(FXTextEditor editor);
    Enum<?>[] getCodecsValues();
}
