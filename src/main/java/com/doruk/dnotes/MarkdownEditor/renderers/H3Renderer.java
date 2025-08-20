package com.doruk.dnotes.MarkdownEditor.renderers;

import com.doruk.dnotes.MarkdownEditor.docstyle.ParagraphStyle;
import com.doruk.dnotes.MarkdownEditor.enums.ParagraphType;
import com.doruk.dnotes.MarkdownEditor.interfaces.Renderer;
import com.doruk.dnotes.MarkdownEditor.utils.StyleGroupRegistry;
import com.doruk.dnotes.store.GlobalConstants;

import javafx.scene.text.TextFlow;

public class H3Renderer implements Renderer<TextFlow, ParagraphStyle> {
    @Override
    public void render(TextFlow textFlow, ParagraphStyle style) {
        var appliedStyle = style.getStyle(StyleGroupRegistry.getGroup(ParagraphType.H3));
        
        if (!(appliedStyle.isPresent() && appliedStyle.get() == ParagraphType.H3))
            return;

        textFlow.setStyle(textFlow.getStyle() + "-fx-line-spacing: " + GlobalConstants.H3_FONT_SIZE + "px;");
        textFlow.getChildren()
            .forEach(child -> child.setStyle(child.getStyle() + "-fx-font-size: " + GlobalConstants.H3_FONT_SIZE + "px; -fx-font-weight: bold;"));
    }
}
