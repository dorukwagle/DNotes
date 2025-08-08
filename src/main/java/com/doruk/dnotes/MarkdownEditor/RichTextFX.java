package com.doruk.dnotes.MarkdownEditor;

import javafx.scene.control.IndexRange;
import javafx.scene.paint.Color;
import javafx.scene.text.TextFlow;

import org.fxmisc.richtext.GenericStyledArea;
import org.fxmisc.richtext.TextExt;
import org.fxmisc.richtext.model.*;

import com.doruk.dnotes.MarkdownEditor.docstyle.ParagraphStyle;
import com.doruk.dnotes.MarkdownEditor.docstyle.TextStyle;
import com.doruk.dnotes.MarkdownEditor.enums.ParagraphType;
import com.doruk.dnotes.MarkdownEditor.enums.ToolName;
import com.doruk.dnotes.MarkdownEditor.interfaces.FXTextEditor;
import com.doruk.dnotes.MarkdownEditor.interfaces.Renderer;

import atlantafx.base.theme.Styles;

public class RichTextFX implements FXTextEditor {
    private final GenericStyledArea<ParagraphStyle, String, TextStyle> area;

    public RichTextFX() {
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

    @Override
    public GenericStyledArea<ParagraphStyle, String, TextStyle> getArea() {
        return area;
    }

    // Text controls
    public void setText(String text) {
        area.replaceText(text);
    }

    public void appendText(String text) {
        area.insertText(area.getLength(), text);
    }

    public IndexRange getSelection() {
        return area.getSelection();
    }

    public void setSelection(int start, int end) {
        area.selectRange(start, end);
    }

    // Formatting
    public void toggleBold() {
        toggleStyle(current -> new TextStyle(!current.bold, current.italic, current.underline, current.strike,
                current.textColor, current.backgroundColor, current.fontSize, current.linkUrl));
    }

    public void toggleItalic() {
        toggleStyle(current -> new TextStyle(current.bold, !current.italic, current.underline, current.strike,
                current.textColor, current.backgroundColor, current.fontSize, current.linkUrl));
    }

    public void toggleUnderline() {
        toggleStyle(current -> new TextStyle(current.bold, current.italic, !current.underline, current.strike,
                current.textColor, current.backgroundColor, current.fontSize, current.linkUrl));
    }

    public void toggleStrikethrough() {
        toggleStyle(current -> new TextStyle(current.bold, current.italic, current.underline, !current.strike,
                current.textColor, current.backgroundColor, current.fontSize, current.linkUrl));
    }

    public void applyFontSize(int size) {
        toggleStyle(current -> new TextStyle(current.bold, current.italic, current.underline, current.strike,
                current.textColor, current.backgroundColor, size, current.linkUrl));
    }

    public void applyTextColor(Color color) {
        toggleStyle(current -> new TextStyle(current.bold, current.italic, current.underline, current.strike,
                color, current.backgroundColor, current.fontSize, current.linkUrl));
    }

    public void applyBackgroundColor(Color color) {
        toggleStyle(current -> new TextStyle(current.bold, current.italic, current.underline, current.strike,
                current.textColor, color, current.fontSize, current.linkUrl));
    }

    // apply paragraph align center

    // apply paragraph align left

    // Paragraph styles
    public void applyHeader(int level) {
        ParagraphType type = switch (level) {
            case 1 -> ParagraphType.H1;
            case 2 -> ParagraphType.H2;
            case 3 -> ParagraphType.H3;
            case 4 -> ParagraphType.H4;
            default -> ParagraphType.NORMAL;
        };
        area.setParagraphStyle(area.getCurrentParagraph(), new ParagraphStyle(type, false));
    }

    public void applyBlockquote() {
        area.setParagraphStyle(area.getCurrentParagraph(), new ParagraphStyle(ParagraphType.BLOCKQUOTE, false));
    }

    public void applyUnorderedList() {
        area.setParagraphStyle(area.getCurrentParagraph(), new ParagraphStyle(ParagraphType.UL_ITEM, false));
    }

    public void applyOrderedList() {
        area.setParagraphStyle(area.getCurrentParagraph(), new ParagraphStyle(ParagraphType.OL_ITEM, false));
    }

    public void applyCheckboxList() {
        area.setParagraphStyle(area.getCurrentParagraph(), new ParagraphStyle(ParagraphType.CHECKBOX_ITEM, false));
    }

    public void toggleCheckbox() {
        int paragraph = area.getCurrentParagraph();
        ParagraphStyle current = area.getDocument().getParagraph(paragraph).getParagraphStyle();
        if (current.type == ParagraphType.CHECKBOX_ITEM) {
            area.setParagraphStyle(paragraph, new ParagraphStyle(current.type, !current.checked));
        }
    }

    // Helpers
    private void toggleStyle(java.util.function.Function<TextStyle, TextStyle> mapper) {
        IndexRange selection = area.getSelection();
        TextStyle current = getCurrentTextStyle();
        TextStyle updated = mapper.apply(current);

        if (selection.getLength() > 0) {
            area.setStyle(selection.getStart(), selection.getEnd(), updated);
        }
        else area.setTextInsertionStyle(updated);
    }

    private TextStyle getCurrentTextStyle() {
        var hasSelection = area.getSelection().getLength() > 0;
        int pos = hasSelection ? area.getSelection().getStart() : area.getCaretPosition();
        return hasSelection ? area.getStyleAtPosition(pos) : area.getTextStyleForInsertionAt(pos);
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
    public void addTextRenderer(ToolName tool, Renderer<TextExt> renderer) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'addTextRenderer'");
    }

    @Override
    public void addParagraphRenderer(ToolName tool, Renderer<TextFlow> renderer) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'addParagraphRenderer'");
    }

    @Override 
    public void removeRenderer(ToolName tool) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'removeRenderer'");
    }
}
