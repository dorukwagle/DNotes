package com.doruk.dnotes.MarkdownEditor.renderers;

import com.doruk.dnotes.MarkdownEditor.docstyle.ParagraphStyle;
import com.doruk.dnotes.MarkdownEditor.enums.ParagraphType;
import com.doruk.dnotes.MarkdownEditor.interfaces.Renderer;
import com.doruk.dnotes.MarkdownEditor.utils.StyleGroupRegistry;

import javafx.scene.text.TextFlow;

public class AlignCenterRenderer implements Renderer<TextFlow, ParagraphStyle> {
    @Override
    public void render(TextFlow textFlow, ParagraphStyle style) {
        var appliedStyle = style.getStyle(StyleGroupRegistry.getGroup(ParagraphType.ALIGN_CENTER));
        
        if (!(appliedStyle.isPresent() && appliedStyle.get() == ParagraphType.ALIGN_CENTER))
            return;

        textFlow.setStyle(
            textFlow.getStyle() + 
            "-fx-text-alignment: center;" + 
            "-fx-translate-x: 10px;" +
            "-fx-translate-y: 5px;"   
        );
    }
}
