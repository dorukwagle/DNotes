package com.doruk.dnotes.MarkdownEditor.renderers;

import org.fxmisc.richtext.TextExt;

import com.doruk.dnotes.MarkdownEditor.interfaces.Renderer;

public class StrikethroughRenderer implements Renderer<TextExt> {
    
    @Override
    public void render(TextExt textExt) {
        textExt.setStrikethrough(true);
    }
}
