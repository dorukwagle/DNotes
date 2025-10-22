package com.doruk.dnotes.MarkdownEditor.interfaces;

import java.util.stream.Stream;

import com.doruk.dnotes.MarkdownEditor.codecs.dto.ParagraphNode;
import com.doruk.dnotes.MarkdownEditor.enums.EditorColor;

import javafx.scene.Parent;

public interface IMarkdownEditor {
    void setEditorText(StringBuilder builder);
    StringBuilder getEditorText();
    void setOnClose(Runnable onClose);
    Parent getView();
    void setEditorBackground(EditorColor color);
    Enum<?>[] getCodecsValues();
    Stream<ParagraphNode> encodeAndDump();
    void close();

    void decodeAndLoad(ParagraphNode node);
    void setDisabled(boolean disabled);
}