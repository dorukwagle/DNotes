package com.doruk.dnotes.MarkdownEditor.interfaces;

import org.fxmisc.richtext.GenericStyledArea;
import org.fxmisc.richtext.TextExt;
import org.reactfx.SuspendableYes;

import com.doruk.dnotes.MarkdownEditor.docstyle.ParagraphStyle;
import com.doruk.dnotes.MarkdownEditor.docstyle.TextStyle;
import com.doruk.dnotes.MarkdownEditor.enums.ToolName;

import javafx.scene.text.TextFlow;

public interface FXTextEditor {
    GenericStyledArea<ParagraphStyle, String, TextStyle> getArea();
    void addTextRenderer(ToolName tool, Renderer<TextExt, TextStyle> renderer);
    void addParagraphRenderer(ToolName tool, Renderer<TextFlow, ParagraphStyle> renderer);
    void removeRenderer(ToolName tool);
    int getParagraphIndexAtPos(int pos);
    SuspendableYes getSuspendableUndo();

    void cleanup();
}
