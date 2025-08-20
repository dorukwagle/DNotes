package com.doruk.dnotes.MarkdownEditor.renderers;

import org.fxmisc.richtext.TextExt;

import com.doruk.dnotes.MarkdownEditor.docstyle.TextStyle;
import com.doruk.dnotes.MarkdownEditor.interfaces.Renderer;
import com.doruk.dnotes.MarkdownEditor.utils.StyleHelper;

public class FontColorRenderer implements Renderer<TextExt, TextStyle> {
    @Override
    public void render(TextExt textExt, TextStyle style) {
        if (style.textColor == null)
            return;

        textExt.setStyle(textExt.getStyle() + "-fx-fill: " + StyleHelper.toRgba(style.textColor) + ";");
    }
}
