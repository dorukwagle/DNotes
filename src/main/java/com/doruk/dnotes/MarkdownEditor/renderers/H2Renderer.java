package com.doruk.dnotes.MarkdownEditor.renderers;

import com.doruk.dnotes.MarkdownEditor.docstyle.ParagraphStyle;
import com.doruk.dnotes.MarkdownEditor.enums.ParagraphType;
import com.doruk.dnotes.MarkdownEditor.interfaces.Renderer;
import com.doruk.dnotes.MarkdownEditor.utils.StyleGroupRegistry;
import com.doruk.dnotes.store.GlobalConstants;

import javafx.scene.text.TextFlow;

public class H2Renderer implements Renderer<TextFlow, ParagraphStyle> {
    @Override
    public void render(TextFlow textFlow, ParagraphStyle style) {
        var appliedStyle = style.getStyle(StyleGroupRegistry.getGroup(ParagraphType.H2));
        
        if (!(appliedStyle.isPresent() && appliedStyle.get() == ParagraphType.H2))
            return;

        textFlow.setStyle(textFlow.getStyle() + 
            "-fx-font-size: " + GlobalConstants.H2_FONT_SIZE + "px; -fx-font-weight: bold; -fx-padding: 8px 0 4px 0;");
    }
}
