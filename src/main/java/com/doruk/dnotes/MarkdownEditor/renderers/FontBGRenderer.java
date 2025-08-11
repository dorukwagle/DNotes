package com.doruk.dnotes.MarkdownEditor.renderers;

import org.fxmisc.richtext.TextExt;

import javafx.scene.paint.Color;
import com.doruk.dnotes.MarkdownEditor.interfaces.Renderer;
import com.doruk.dnotes.MarkdownEditor.utils.StyleHelper;

public class FontBGRenderer implements Renderer<TextExt> {
    private Color color;
    
    public FontBGRenderer(Color color) {
        this.color = color;
    }
    
    @Override
    public void render(TextExt textExt) {
        textExt.setStyle(textExt.getStyle() + 
            "-rtfx-background-color: " + 
            StyleHelper.toRgba(color) + ";");
    }
}
