package com.doruk.dnotes.MarkdownEditor.docstyle;

import com.doruk.dnotes.store.GlobalConstants;

import javafx.scene.paint.Color;

public class TextStyle {
    public final boolean bold;
    public final boolean italic;
    public final boolean underline;
    public final boolean strikethrough;
    public final Color textColor;
    public final Color backgroundColor;
    public final int fontSize;

    public static final TextStyle EMPTY = new TextStyle(false, false, false, false, GlobalConstants.DEFAULT_FONT_COLOR, GlobalConstants.DEFAULT_FONT_BG_COLOR,
            GlobalConstants.DEFAULT_FONT_SIZE);

    public TextStyle(boolean bold, boolean italic, boolean underline, boolean strikethrough,
            Color textColor, Color backgroundColor, int fontSize) {
        this.bold = bold;
        this.italic = italic;
        this.underline = underline;
        this.strikethrough = strikethrough;
        this.textColor = textColor;
        this.backgroundColor = backgroundColor;
        this.fontSize = fontSize;
    }
}
