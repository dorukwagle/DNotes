package com.doruk.dnotes.MarkdownEditor.utils;

import com.doruk.dnotes.MarkdownEditor.docstyle.TextStyle;

import javafx.scene.paint.Color;

public class StyleHelper {
    public static String toRgba(Color color) {
        return String.format("rgba(%d, %d, %d, %.2f)",
                (int) (color.getRed() * 255),
                (int) (color.getGreen() * 255),
                (int) (color.getBlue() * 255),
                color.getOpacity());
    }

    public static TextStyle textWithBold(TextStyle style, boolean bold) {
        return new TextStyle(bold, style.italic, style.underline, style.strikethrough, style.textColor, style.backgroundColor, style.fontSize);
    }

    public static TextStyle textWithItalic(TextStyle style, boolean italic) {
        return new TextStyle(style.bold, italic, style.underline, style.strikethrough, style.textColor, style.backgroundColor, style.fontSize);
    }

    public static TextStyle textWithUnderline(TextStyle style, boolean underline) {
        return new TextStyle(style.bold, style.italic, underline, style.strikethrough, style.textColor, style.backgroundColor, style.fontSize);
    }

    public static TextStyle textWithStrikethrough(TextStyle style, boolean strikethrough) {
        return new TextStyle(style.bold, style.italic, style.underline, strikethrough, style.textColor, style.backgroundColor, style.fontSize);
    }

    public static TextStyle textWithColor(TextStyle style, Color color) {
        return new TextStyle(style.bold, style.italic, style.underline, style.strikethrough, color, style.backgroundColor, style.fontSize);
    }

    public static TextStyle textWithBackgroundColor(TextStyle style, Color color) {
        return new TextStyle(style.bold, style.italic, style.underline, style.strikethrough, style.textColor, color, style.fontSize);
    }

    public static TextStyle textWithFontSize(TextStyle style, int fontSize) {
        return new TextStyle(style.bold, style.italic, style.underline, style.strikethrough, style.textColor, style.backgroundColor, fontSize);
    }
}
