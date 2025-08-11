package com.doruk.dnotes.MarkdownEditor.renderers;

import javafx.scene.paint.Color;
import org.fxmisc.richtext.TextExt;

import com.doruk.dnotes.MarkdownEditor.interfaces.Renderer;
import com.doruk.dnotes.MarkdownEditor.utils.StyleHelper;

public class FontColorRenderer implements Renderer<TextExt> {
    private Color color;
    
    public FontColorRenderer(Color color) {
        this.color = color;
    }
    
    @Override
    public void render(TextExt textExt) {
        textExt.setStyle(textExt.getStyle() + "-fx-fill: " + StyleHelper.toRgba(color) + ";");
    }
}
