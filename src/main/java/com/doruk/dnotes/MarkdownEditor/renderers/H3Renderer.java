package com.doruk.dnotes.MarkdownEditor.renderers;

import com.doruk.dnotes.MarkdownEditor.docstyle.ParagraphStyle;
import com.doruk.dnotes.MarkdownEditor.interfaces.Renderer;

import javafx.scene.text.TextFlow;

public class H3Renderer implements Renderer<TextFlow, ParagraphStyle> {
    @Override
    public void render(TextFlow textFlow, ParagraphStyle style) {
        textFlow.setStyle(textFlow.getStyle() + 
            "-fx-font-size: 20px; -fx-font-weight: bold; -fx-padding: 6px 0 3px 0;");
    }
}
