package com.doruk.dnotes.MarkdownEditor.renderers;

import com.doruk.dnotes.MarkdownEditor.docstyle.ParagraphStyle;
import com.doruk.dnotes.MarkdownEditor.enums.ParagraphType;
import com.doruk.dnotes.MarkdownEditor.interfaces.Renderer;
import com.doruk.dnotes.MarkdownEditor.utils.StyleGroupRegistry;

import javafx.scene.text.TextFlow;

public class AlignLeftRenderer implements Renderer<TextFlow, ParagraphStyle> {
    @Override
    public void render(TextFlow textFlow, ParagraphStyle style) {
        var appliedStyle = style.getStyle(StyleGroupRegistry.getGroup(ParagraphType.ALIGN_LEFT));

        if (appliedStyle.isPresent() && appliedStyle.get() != ParagraphType.ALIGN_LEFT)
            return;

        textFlow.setStyle(textFlow.getStyle() +
            "-fx-text-alignment: left;"
        );
    }
}
