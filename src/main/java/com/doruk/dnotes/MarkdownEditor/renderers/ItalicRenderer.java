package com.doruk.dnotes.MarkdownEditor.renderers;

import org.fxmisc.richtext.TextExt;

import com.doruk.dnotes.MarkdownEditor.interfaces.Renderer;

public class ItalicRenderer implements Renderer<TextExt> {
    
    @Override
    public void render(TextExt textExt) {
        textExt.setStyle(textExt.getStyle() + "-fx-font-style: italic;");
    }
}
