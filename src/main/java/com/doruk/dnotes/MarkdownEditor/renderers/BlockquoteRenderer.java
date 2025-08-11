package com.doruk.dnotes.MarkdownEditor.renderers;

import com.doruk.dnotes.MarkdownEditor.interfaces.Renderer;

import javafx.scene.text.TextFlow;

public class BlockquoteRenderer implements Renderer<TextFlow> {
    @Override
    public void render(TextFlow textFlow) {
        textFlow.setStyle(textFlow.getStyle() + 
            "-fx-border-color: #ccc; -fx-border-width: 0 0 0 4px; -fx-padding: 5px 0 5px 10px;");
    }
}
