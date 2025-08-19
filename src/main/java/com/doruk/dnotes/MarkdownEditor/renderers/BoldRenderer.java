package com.doruk.dnotes.MarkdownEditor.renderers;

import org.fxmisc.richtext.TextExt;

import com.doruk.dnotes.MarkdownEditor.docstyle.TextStyle;
import com.doruk.dnotes.MarkdownEditor.interfaces.Renderer;

public class BoldRenderer implements Renderer<TextExt, TextStyle> {
    
    @Override
    public void render(TextExt textExt, TextStyle style) {
        if (style.bold)
            textExt.setStyle(textExt.getStyle() + "-fx-font-weight: bold;");
        else
            textExt.setStyle(textExt.getStyle() + "-fx-font-weight: normal;");
    }
}
