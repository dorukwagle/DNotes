package com.doruk.dnotes.MarkdownEditor;

import javafx.scene.control.IndexRange;
import javafx.scene.paint.Color;
import javafx.scene.text.TextFlow;
import org.fxmisc.richtext.GenericStyledArea;
import org.fxmisc.richtext.TextExt;
import org.fxmisc.richtext.model.*;

import atlantafx.base.theme.Styles;

public class RichTextFX {

    public static class TextStyle {
        public final boolean bold;
        public final boolean italic;
        public final boolean underline;
        public final boolean strike;
        public final Color textColor;
        public final Color backgroundColor;
        public final int fontSize;
        public final String linkUrl;

        public static final TextStyle EMPTY = new TextStyle(false, false, false, false, null, Color.TRANSPARENT,
                16, null);

        public TextStyle(boolean bold, boolean italic, boolean underline, boolean strike,
                Color textColor, Color backgroundColor, int fontSize, String linkUrl) {
            this.bold = bold;
            this.italic = italic;
            this.underline = underline;
            this.strike = strike;
            this.textColor = textColor;
            this.backgroundColor = backgroundColor;
            this.fontSize = fontSize;
            this.linkUrl = linkUrl;
        }

        public TextStyle merge(TextStyle other) {
            return new TextStyle(
                    other.bold || this.bold,
                    other.italic || this.italic,
                    other.underline || this.underline,
                    other.strike || this.strike,
                    other.textColor != null ? other.textColor : this.textColor,
                    other.backgroundColor != null ? other.backgroundColor : this.backgroundColor,
                    other.fontSize > 0 ? other.fontSize : this.fontSize,
                    other.linkUrl != null ? other.linkUrl : this.linkUrl);
        }
    }

    public enum ParagraphType {
        NORMAL, H1, H2, H3, H4, BLOCKQUOTE, UL_ITEM, OL_ITEM, CHECKBOX_ITEM
    }

    public static class ParagraphStyle {
        public final ParagraphType type;
        public final boolean checked;

        public static final ParagraphStyle EMPTY = new ParagraphStyle(ParagraphType.NORMAL, false);

        public ParagraphStyle(ParagraphType type, boolean checked) {
            this.type = type;
            this.checked = checked;
        }
    }

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

    public GenericStyledArea<ParagraphStyle, String, TextStyle> getArea() {
        return area;
    }

    // Text controls
    public void setText(String text) {
        area.replaceText(text);
    }

    public String getText() {
        return area.getText();
    }

    public void appendText(String text) {
        area.insertText(area.getLength(), text);
    }

    public void insertText(int position, String text) {
        area.insertText(position, text);
    }

    public void deleteText(int start, int end) {
        area.deleteText(start, end);
    }

    public void selectAll() {
        area.selectAll();
    }

    public IndexRange getSelection() {
        return area.getSelection();
    }

    public void setSelection(int start, int end) {
        area.selectRange(start, end);
    }

    // Formatting
    public void applyBold() {
        toggleStyle(current -> new TextStyle(!current.bold, current.italic, current.underline, current.strike,
                current.textColor, current.backgroundColor, current.fontSize, current.linkUrl));
    }

    public void applyItalic() {
        toggleStyle(current -> new TextStyle(current.bold, !current.italic, current.underline, current.strike,
                current.textColor, current.backgroundColor, current.fontSize, current.linkUrl));
    }

    public void applyUnderline() {
        toggleStyle(current -> new TextStyle(current.bold, current.italic, !current.underline, current.strike,
                current.textColor, current.backgroundColor, current.fontSize, current.linkUrl));
    }

    public void applyStrikethrough() {
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

    public void applyLink(String url) {
        toggleStyle(current -> new TextStyle(current.bold, current.italic, true, current.strike,
                Color.BLUE, current.backgroundColor, current.fontSize, url));
    }

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
        if (selection.getLength() > 0) {
            TextStyle current = getCurrentTextStyle();
            TextStyle updated = mapper.apply(current);
            area.setStyle(selection.getStart(), selection.getEnd(), updated);
        }
    }

    private TextStyle getCurrentTextStyle() {
        int pos = area.getCaretPosition();
        return pos > 0 ? area.getStyleOfChar(pos - 1) : TextStyle.EMPTY;
    }

    private static void applyParagraphStyle(TextFlow flow, ParagraphStyle style) {
        StringBuilder css = new StringBuilder();

        // default padding
        css.append("-fx-padding: 8px");

        switch (style.type) {
            case H1 -> css.append("-fx-font-size: 24px; -fx-font-weight: bold; -fx-padding: 10px 0 5px 0;");
            case H2 -> css.append("-fx-font-size: 22px; -fx-font-weight: bold; -fx-padding: 8px 0 4px 0;");
            case H3 -> css.append("-fx-font-size: 20px; -fx-font-weight: bold; -fx-padding: 6px 0 3px 0;");
            case H4 -> css.append("-fx-font-size: 18px; -fx-font-weight: bold; -fx-padding: 4px 0 2px 0;");
            case BLOCKQUOTE -> css.append(
                    "-fx-border-color: #ccc; -fx-border-width: 0 0 0 4px; -fx-padding: 5px 0 5px 10px;");
            case UL_ITEM, OL_ITEM, CHECKBOX_ITEM -> css.append("-fx-padding: 2px 0 2px 20px;");
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
}
