package com.doruk.dnotes.MarkdownEditor.renderers;

import com.doruk.dnotes.MarkdownEditor.docstyle.ParagraphStyle;
import com.doruk.dnotes.MarkdownEditor.enums.ParagraphType;
import com.doruk.dnotes.MarkdownEditor.interfaces.Renderer;
import com.doruk.dnotes.MarkdownEditor.utils.StyleGroupRegistry;

import javafx.scene.text.TextFlow;

public class BlockquoteRenderer implements Renderer<TextFlow, ParagraphStyle> {
    @Override
    public void render(TextFlow textFlow, ParagraphStyle style) {
        var appliedStyle = style.getStyle(StyleGroupRegistry.getGroup(ParagraphType.BLOCKQUOTE));
        if (!(appliedStyle.isPresent() && appliedStyle.get() == ParagraphType.BLOCKQUOTE))
            return;

        textFlow.setStyle(
            textFlow.getStyle() +
            "-fx-background-color: -color-accent-subtle;" +
            "-fx-background-radius: 0px 100px 100px 0px;" +
            "-fx-background-insets: 15px 15px 10px 35px;" +
            "-fx-border-insets: 15px 100px 10px 35px;" +
            "-fx-border-color: -color-accent-6;" +
            "-fx-border-width: 0 0 0 10px;" +
            "-fx-padding: 0px 0px 0px 5px;" +
            "-fx-font-family: 'Magnolia Script';"
        );
    }
}
