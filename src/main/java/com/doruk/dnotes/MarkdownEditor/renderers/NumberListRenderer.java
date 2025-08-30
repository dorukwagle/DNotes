package com.doruk.dnotes.MarkdownEditor.renderers;

import com.doruk.dnotes.MarkdownEditor.docstyle.ParagraphStyle;
import com.doruk.dnotes.MarkdownEditor.enums.ParagraphType;
import com.doruk.dnotes.MarkdownEditor.interfaces.Renderer;
import com.doruk.dnotes.MarkdownEditor.utils.NumeralUtility;
import com.doruk.dnotes.MarkdownEditor.utils.StyleGroupRegistry;
import com.doruk.dnotes.store.GlobalConstants;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.text.TextFlow;

public class NumberListRenderer implements Renderer<TextFlow, ParagraphStyle> {
    private enum LabelType {
        ARABIC,
        ROMAN,
        ALPHABETIC_UPPER,
        ALPHABETIC_LOWER,
    }
    private static final LabelType[] labels = {
        LabelType.ARABIC, 
        LabelType.ALPHABETIC_UPPER,
        LabelType.ROMAN, 
        LabelType.ALPHABETIC_LOWER
    };

    private boolean isApplied(ParagraphStyle style) {
        var appliedStyle = style.getStyle(StyleGroupRegistry.getGroup(ParagraphType.NUMBER_LIST_ITEM));
        return appliedStyle.isPresent() && appliedStyle.get() == ParagraphType.NUMBER_LIST_ITEM;
    }

    private String getLabel(LabelType type, int lineCount) {
        switch (type) {
            case ARABIC -> {
                return String.valueOf(lineCount);
            }
            case ROMAN -> {
                return NumeralUtility.toRoman(lineCount);
            }
            case ALPHABETIC_UPPER -> {
                return NumeralUtility.toAlphabeticUpper(lineCount);
            }
            case ALPHABETIC_LOWER -> {
                return NumeralUtility.toAlphabeticLower(lineCount);
            }
            default -> {
                return "";
            }
        }
    }
    
    @Override
    public Node renderParagraphGraphic(ParagraphStyle style, int index) {
        if (!isApplied(style))
            return null;
        
        LabelType labelType = labels[style.level % labels.length]; // cycle if deeper
        var flowInset = style.level * GlobalConstants.DEFAULT_LIST_ITEM_INSET;

        Label bulletNode = new Label(getLabel(labelType, style.lineCount) + ".");
        bulletNode.setStyle("-fx-font-weight: bold; -fx-font-size: 22px;");
        bulletNode.setPadding(new Insets(0, 0, 0, flowInset));
        bulletNode.setAlignment(Pos.BASELINE_CENTER);

        return bulletNode;
    }

    @Override
    public void render(TextFlow textFlow, ParagraphStyle style) {
        if (!isApplied(style))
            return;

        textFlow.setStyle(textFlow.getStyle() + 
            "-fx-padding: 2px 0 2px 20px;");
    }
}
