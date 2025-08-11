package com.doruk.dnotes.MarkdownEditor.renderers;

import com.doruk.dnotes.MarkdownEditor.docstyle.ParagraphStyle;
import com.doruk.dnotes.MarkdownEditor.interfaces.Renderer;

import javafx.scene.text.TextFlow;

public class H4Renderer implements Renderer<TextFlow, ParagraphStyle> {
    @Override
    public void render(TextFlow textFlow, ParagraphStyle style) {
        textFlow.setStyle(textFlow.getStyle() + 
            "-fx-font-size: 18px; -fx-font-weight: bold; -fx-padding: 4px 0 2px 0;");
    }
}
