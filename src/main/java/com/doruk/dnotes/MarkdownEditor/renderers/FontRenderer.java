package com.doruk.dnotes.MarkdownEditor.renderers;

import org.fxmisc.richtext.TextExt;

import com.doruk.dnotes.MarkdownEditor.interfaces.Renderer;

public class FontRenderer implements Renderer<TextExt> {
    private int fontSize;
    
    public FontRenderer(int fontSize) {
        this.fontSize = fontSize;
    }
    
    @Override
    public void render(TextExt textExt) {
        textExt.setStyle(textExt.getStyle() + "-fx-font-size: " + fontSize + "px;");
    }
}
