package com.doruk.dnotes.MarkdownEditor.renderers;

import com.doruk.dnotes.MarkdownEditor.docstyle.ParagraphStyle;
import com.doruk.dnotes.MarkdownEditor.interfaces.Renderer;

import javafx.scene.text.TextFlow;

public class ULItemRenderer implements Renderer<TextFlow, ParagraphStyle> {
    @Override
    public void render(TextFlow textFlow, ParagraphStyle style) {
        textFlow.setStyle(textFlow.getStyle() + 
            "-fx-padding: 2px 0 2px 20px;");
    }
}
