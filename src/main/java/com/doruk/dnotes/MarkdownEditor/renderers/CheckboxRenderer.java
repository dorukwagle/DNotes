package com.doruk.dnotes.MarkdownEditor.renderers;

import org.kordamp.ikonli.javafx.FontIcon;

import com.doruk.dnotes.MarkdownEditor.docstyle.ParagraphStyle;
import com.doruk.dnotes.MarkdownEditor.enums.ParagraphType;
import com.doruk.dnotes.MarkdownEditor.interfaces.Renderer;
import com.doruk.dnotes.MarkdownEditor.utils.StyleGroupRegistry;
import com.doruk.dnotes.store.GlobalConstants;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.text.TextFlow;

public class CheckboxRenderer implements Renderer<TextFlow, ParagraphStyle> {
    private static final String CHECKED = "mdi2s-sticker-check";
    private static final String UNCHECKED = "mdi2s-sticker-outline";
    
    private boolean isApplied(ParagraphStyle style) {
        var appliedStyle = style.getStyle(StyleGroupRegistry.getGroup(ParagraphType.CHECK_LIST_ITEM));
        return appliedStyle.isPresent() && appliedStyle.get() == ParagraphType.CHECK_LIST_ITEM;
    }
    
    @Override
    public void render(TextFlow textFlow, ParagraphStyle style) {
        if (!isApplied(style))
            return;
        
        textFlow.setLineSpacing(10);
    }

    @Override
    public Node renderParagraphGraphic(ParagraphStyle style) {
        if (!isApplied(style))
            return null;
        
        Label bulletNode = new Label();
        bulletNode.setGraphic(new FontIcon(style.isItemChecked ? CHECKED : UNCHECKED));
        bulletNode.setPadding(new Insets(0, 0, 0, GlobalConstants.DEFAULT_LIST_ITEM_INSET));
        bulletNode.setAlignment(Pos.BASELINE_CENTER);
        
        return bulletNode;        
    }
}
