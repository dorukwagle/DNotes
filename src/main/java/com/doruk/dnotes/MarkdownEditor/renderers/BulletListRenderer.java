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

public class BulletListRenderer implements Renderer<TextFlow, ParagraphStyle> {
    private static final String[] labels = {
        "mdi2r-rhombus-split", 
        "mdi2a-arrow-right-bold", 
        "mdi2r-rhombus",
        "mdi2c-circle"
    };

    private boolean isApplied(ParagraphStyle style) {
        var appliedStyle = style.getStyle(StyleGroupRegistry.getGroup(ParagraphType.BULLET_LIST_ITEM));
        return appliedStyle.isPresent() && appliedStyle.get() == ParagraphType.BULLET_LIST_ITEM;
    }
    
    @Override
    public Node renderParagraphGraphic(ParagraphStyle style) {
        if (!isApplied(style))
            return null;
        
        var appliedStyle = style.getStyle(StyleGroupRegistry.getGroup(ParagraphType.BULLET_LIST_ITEM));
        if (!(appliedStyle.isPresent() && appliedStyle.get() == ParagraphType.BULLET_LIST_ITEM))
            return null;
        
        int indent = style.level;
        String bullet = labels[indent % labels.length]; // cycle if deeper
        var flowInset = (style.level + 1) * GlobalConstants.DEFAULT_LIST_ITEM_INSET;

        Label bulletNode = new Label();
        bulletNode.setGraphic(new FontIcon(bullet));
        bulletNode.setPadding(new Insets(0, 0, 0, flowInset));
        bulletNode.setAlignment(Pos.BASELINE_CENTER);
        return bulletNode;
    }
    
    @Override
    public void render(TextFlow textFlow, ParagraphStyle style) {
        if (!isApplied(style))
            return;
        
        textFlow.setLineSpacing(10);
        // var appliedStyle = style.getStyle(StyleGroupRegistry.getGroup(ParagraphType.BULLET_LIST_ITEM));
        // if (!(appliedStyle.isPresent() && appliedStyle.get() == ParagraphType.BULLET_LIST_ITEM))
        //     return;
        
        // int indent = style.level;
        // String bullet = labels[indent % labels.length]; // cycle if deeper
        // var labelInset = 20;
        // var flowInset = (style.level + 1) * labelInset + labelInset;

        // Label bulletNode = new Label();
        // bulletNode.setGraphic(new FontIcon(bullet));
        
        // bulletNode.setStyle("-fx-font-size: 12px; -fx-padding: 0px 0px 0px -" + labelInset + "px;");
        // // Insert at the start of the flow
        // textFlow.getChildren().add(0, bulletNode); 

        // // padding so text aligns nicely
        
        
        // textFlow.setStyle(textFlow.getStyle() + 
        //     "-fx-border-insets: 0px 0px 0px " + flowInset + "px;" +
        //     "-fx-background-insets: 0px 0px 0px " + flowInset + "px;"
        // );
    }
}
