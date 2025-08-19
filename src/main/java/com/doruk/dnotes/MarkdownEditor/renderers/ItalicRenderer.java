package com.doruk.dnotes.MarkdownEditor.renderers;

import org.fxmisc.richtext.TextExt;

import com.doruk.dnotes.MarkdownEditor.docstyle.TextStyle;
import com.doruk.dnotes.MarkdownEditor.interfaces.Renderer;

public class ItalicRenderer implements Renderer<TextExt, TextStyle> {
    
    @Override
    public void render(TextExt textExt, TextStyle style) {
        if (style.italic)
            textExt.setStyle(textExt.getStyle() + "-fx-font-style: italic;");
        else
            textExt.setStyle(textExt.getStyle() + "-fx-font-style: normal;");
    }
}
