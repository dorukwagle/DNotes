package com.doruk.dnotes.MarkdownEditor.renderers;

import com.doruk.dnotes.MarkdownEditor.docstyle.ParagraphStyle;
import com.doruk.dnotes.MarkdownEditor.enums.ParagraphType;
import com.doruk.dnotes.MarkdownEditor.interfaces.Renderer;
import com.doruk.dnotes.MarkdownEditor.utils.StyleGroupRegistry;
import com.doruk.dnotes.store.GlobalConstants;

import javafx.scene.text.TextFlow;

public class H1Renderer implements Renderer<TextFlow, ParagraphStyle> {
    @Override
    public void render(TextFlow textFlow, ParagraphStyle style) {
        var appliedStyle = style.getStyle(StyleGroupRegistry.getGroup(ParagraphType.H1));
        
        System.out.println("checking to apply H1");
        if (!(appliedStyle.isPresent() && appliedStyle.get() == ParagraphType.H1))
            return;

        System.out.println("applying H1");
        textFlow.setStyle(textFlow.getStyle() + 
            "-fx-font-size: " + GlobalConstants.H1_FONT_SIZE + "px; -fx-font-weight: bold; -fx-padding: 10px 0 5px 0;");
    }
}
