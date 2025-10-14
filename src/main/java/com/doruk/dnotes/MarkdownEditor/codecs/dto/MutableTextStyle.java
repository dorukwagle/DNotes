package com.doruk.dnotes.MarkdownEditor.codecs.dto;

import com.doruk.dnotes.MarkdownEditor.docstyle.TextStyle;
import com.doruk.dnotes.store.GlobalConstants;
import javafx.scene.paint.Color;

public class MutableTextStyle {
    public boolean bold;
    public boolean italic;
    public boolean underline;
    public boolean strikethrough;
    public Color textColor;
    public Color backgroundColor;
    public int fontSize;

    public MutableTextStyle() {
        this.bold = false;
        this.italic = false;
        this.underline = false;
        this.strikethrough = false;
        this.textColor = GlobalConstants.DEFAULT_FONT_COLOR;
        this.backgroundColor = GlobalConstants.DEFAULT_FONT_BG_COLOR;
        this.fontSize = GlobalConstants.DEFAULT_FONT_SIZE;
    }
}
