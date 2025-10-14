package com.doruk.dnotes.MarkdownEditor.renderers;

import org.fxmisc.richtext.TextExt;

import com.doruk.dnotes.MarkdownEditor.docstyle.TextStyle;
import com.doruk.dnotes.MarkdownEditor.interfaces.Renderer;
import com.doruk.dnotes.MarkdownEditor.utils.StyleHelper;

public class FontBGRenderer implements Renderer<TextExt, TextStyle> {
    @Override
    public void render(TextExt textExt, TextStyle style) {
        if (style.backgroundColor == null)
            return;

        textExt.setStyle(textExt.getStyle() + 
            "-rtfx-background-color: " + 
            StyleHelper.toRgba(style.backgroundColor) + ";");
    }
}
