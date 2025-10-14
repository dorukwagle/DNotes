package com.doruk.dnotes.MarkdownEditor.renderers;

import java.util.Map;

import org.kordamp.ikonli.javafx.FontIcon;

import com.doruk.dnotes.MarkdownEditor.ToolsMediator;
import com.doruk.dnotes.MarkdownEditor.docstyle.ParagraphStyle;
import com.doruk.dnotes.MarkdownEditor.enums.ParagraphType;
import com.doruk.dnotes.MarkdownEditor.enums.ToolsEvent;
import com.doruk.dnotes.MarkdownEditor.interfaces.Renderer;
import com.doruk.dnotes.MarkdownEditor.utils.StyleGroupRegistry;
import com.doruk.dnotes.store.GlobalConstants;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.text.TextFlow;

public class CheckboxRenderer implements Renderer<TextFlow, ParagraphStyle> {
    private enum LabelType {
        CHECKED,
        UNCHECKED
    };

    private static final Map<Integer, Map<LabelType, String>> LEVEL_LABEL = Map.of(
        0, Map.of(
            LabelType.CHECKED, "mdi2s-sticker-check", 
            LabelType.UNCHECKED, "mdi2s-sticker-outline"),
        1, Map.of(
            LabelType.UNCHECKED, "mdi2c-checkbox-blank-circle-outline", 
            LabelType.CHECKED, "mdi2c-checkbox-marked-circle"),
        2, Map.of(
            LabelType.CHECKED, "mdi2c-checkbox-multiple-marked", 
            LabelType.UNCHECKED, "mdi2c-checkbox-multiple-blank-outline"),
        3, Map.of(
            LabelType.UNCHECKED, "mdi2c-checkbox-multiple-blank-circle-outline", 
            LabelType.CHECKED, "mdi2c-checkbox-multiple-marked-circle")
    );
    
    private String getLabel(LabelType type, int level) {
        return LEVEL_LABEL.get((level - 1) % LEVEL_LABEL.size()).get(type);
    }

    private boolean isApplied(ParagraphStyle style) {
        var appliedStyle = style.getStyle(StyleGroupRegistry.getGroup(ParagraphType.CHECK_LIST_ITEM));
        return appliedStyle.isPresent() && appliedStyle.get() == ParagraphType.CHECK_LIST_ITEM;
    }
    
    @Override
    public void render(TextFlow textFlow, ParagraphStyle style) {
        if (!isApplied(style))
            return;
        
        var styleString = "-fx-padding: 6px 0px 2px 20px;";
        String nodeStyle = "-fx-fill: -color-fg-muted; -fx-strikethrough: true;";

        textFlow.setStyle(textFlow.getStyle() + styleString);

        if (style.isItemChecked)
            textFlow.getChildren().forEach(node -> node.setStyle(node.getStyle() + nodeStyle));
    }

    @Override
    public Node renderParagraphGraphic(ParagraphStyle style, int index) {
        if (!isApplied(style))
            return null;

        var flowInset = style.offset * GlobalConstants.DEFAULT_LIST_ITEM_INSET + 
            style.level * GlobalConstants.DEFAULT_LIST_ITEM_INSET;
        
        Label bulletNode = new Label();
        var icon = new FontIcon(getLabel(style.isItemChecked ? LabelType.CHECKED : LabelType.UNCHECKED, style.level));
        bulletNode.setGraphic(icon);
        icon.setScaleX(1.3);
        icon.setScaleY(1.3);
        bulletNode.setPadding(new Insets(10, 0, 0, flowInset));
        bulletNode.setCursor(Cursor.HAND);
        bulletNode.setAlignment(Pos.TOP_CENTER);

        bulletNode.hoverProperty().addListener((_, _, newVal) -> {
            if (newVal){
                icon.setScaleX(1.35);
                icon.setScaleY(1.35);
            }
            else{
                icon.setScaleX(1.3);
                icon.setScaleY(1.3);
            }
        });

        // also add event listeners
        bulletNode.setOnMouseClicked(_ -> {
            ToolsMediator.publish(ToolsEvent.CHECKBOX_CLICKED, index);
        });
        
        return bulletNode;
    }
}
