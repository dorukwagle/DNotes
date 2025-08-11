package com.doruk.dnotes.MarkdownEditor.renderers;

import com.doruk.dnotes.MarkdownEditor.interfaces.Renderer;

import javafx.scene.text.TextFlow;

public class H1Renderer implements Renderer<TextFlow> {
    @Override
    public void render(TextFlow textFlow) {
        textFlow.setStyle(textFlow.getStyle() + 
            "-fx-font-size: 24px; -fx-font-weight: bold; -fx-padding: 10px 0 5px 0;");
    }
}
