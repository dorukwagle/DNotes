package com.doruk.dnotes.MarkdownEditor.renderers;

import org.fxmisc.richtext.TextExt;

import com.doruk.dnotes.MarkdownEditor.docstyle.TextStyle;
import com.doruk.dnotes.MarkdownEditor.interfaces.Renderer;

public class StrikethroughRenderer implements Renderer<TextExt, TextStyle> {
    
    @Override
    public void render(TextExt textExt, TextStyle style) {
        if (style.strikethrough)
            textExt.setStrikethrough(true);
    }
}
