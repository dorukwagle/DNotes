package com.doruk.dnotes.MarkdownEditor.docstyle;

import javafx.scene.paint.Color;

public class TextStyle {
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
}
