package com.doruk.dnotes.MarkdownEditor.renderers;

import org.fxmisc.richtext.TextExt;

import com.doruk.dnotes.MarkdownEditor.interfaces.Renderer;
import com.doruk.dnotes.MarkdownEditor.docstyle.TextStyle;

public class FontRenderer implements Renderer<TextExt, TextStyle> {
    @Override
    public void render(TextExt textExt, TextStyle style) {
        textExt.setStyle(textExt.getStyle() + "-fx-font-size: " + style.fontSize + "px;");
    }
}
