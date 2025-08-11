package com.doruk.dnotes.MarkdownEditor.renderers;

import com.doruk.dnotes.MarkdownEditor.interfaces.Renderer;

import javafx.scene.text.TextFlow;

public class AlignCenterRenderer implements Renderer<TextFlow> {
    @Override
    public void render(TextFlow textFlow) {
        textFlow.setStyle(textFlow.getStyle() + 
            "-fx-padding: 2px 0 2px 20px;");
    }
}
