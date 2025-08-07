package com.doruk.dnotes.MarkdownEditor;

import org.fxmisc.richtext.GenericStyledArea;
import org.fxmisc.richtext.TextExt;
import org.fxmisc.richtext.model.SegmentOps;

import com.doruk.dnotes.MarkdownEditor.docstyle.ParagraphStyle;
import com.doruk.dnotes.MarkdownEditor.docstyle.TextStyle;
import com.doruk.dnotes.MarkdownEditor.enums.ParagraphType;
import com.doruk.dnotes.MarkdownEditor.interfaces.FXTextEditor;

import atlantafx.base.theme.Styles;
import javafx.scene.paint.Color;
import javafx.scene.text.TextFlow;

public class EditorFX implements FXTextEditor {
    private final GenericStyledArea<ParagraphStyle, String, TextStyle> area;

    public EditorFX() {
        area = new GenericStyledArea<>(
                ParagraphStyle.EMPTY,
                (textFlow, style) -> applyParagraphStyle(textFlow, style),
                TextStyle.EMPTY,
                SegmentOps.styledTextOps(),
                segment -> {
                    var text = new TextExt(segment.getSegment());
                    var style = segment.getStyle();
                    StringBuilder css = new StringBuilder();

                    if (style.bold)
                        css.append("-fx-font-weight: bold;");
                    if (style.italic)
                        css.append("-fx-font-style: italic;");
                    if (style.underline)
                        text.setUnderline(true);
                    if (style.strike)
                        css.append("-fx-strikethrough: true;");
                    if (style.fontSize > 0)
                        css.append("-fx-font-size: ").append(style.fontSize).append("px;");
                    if (style.backgroundColor != null)
                        css.append("-rtfx-background-color: ").append(toRgba(style.backgroundColor)).append(";");
                    
                    // set text color if given
                    css.append("-fx-fill: ").append(style.textColor != null ? toRgba(style.textColor) : "-color-fg-default").append(";");

                    text.setStyle(css.toString());
                    return text;
                });

        area.setWrapText(true);
        area.setPrefSize(Double.MAX_VALUE, Double.MAX_VALUE);
        area.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);

        area.getStyleClass().add(Styles.ACCENT);
        
        area.getStylesheets().add(getClass().getResource("/styles.scss").toExternalForm());
    }

    private static void applyParagraphStyle(TextFlow flow, ParagraphStyle style) {
        StringBuilder css = new StringBuilder();

        // default padding
        css.append("-fx-padding: 8px;");

        switch (style.type) {
            case H1 -> css.append("-fx-font-size: 24px; -fx-font-weight: bold; -fx-padding: 10px 0 5px 0;");
            case H2 -> css.append("-fx-font-size: 22px; -fx-font-weight: bold; -fx-padding: 8px 0 4px 0;");
            case H3 -> css.append("-fx-font-size: 20px; -fx-font-weight: bold; -fx-padding: 6px 0 3px 0;");
            case H4 -> css.append("-fx-font-size: 18px; -fx-font-weight: bold; -fx-padding: 4px 0 2px 0;");
            case BLOCKQUOTE -> css.append(
                    "-fx-border-color: #ccc; -fx-border-width: 0 0 0 4px; -fx-padding: 5px 0 5px 10px;");
            case UL_ITEM, OL_ITEM, CHECKBOX_ITEM -> css.append("-fx-padding: 2px 0 2px 20px;");
            case NORMAL -> css.append("");
        }

        if (style.type == ParagraphType.CHECKBOX_ITEM && style.checked) {
            css.append("-fx-opacity: 0.6;");
        }

        flow.setStyle(css.toString());
    }

    private static String toRgba(Color color) {
        return String.format("rgba(%d, %d, %d, %.2f)",
                (int) (color.getRed() * 255),
                (int) (color.getGreen() * 255),
                (int) (color.getBlue() * 255),
                color.getOpacity());
    }

    @Override
    public GenericStyledArea<ParagraphStyle, String, TextStyle> getArea() {
        return area;
    }
}
