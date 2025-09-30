package com.doruk.dnotes.MarkdownEditor.utils;

import com.doruk.dnotes.MarkdownEditor.codecs.dto.MutableTextStyle;
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

    public static int colorToInteger(Color color) {
        var alpha = (int) (color.getOpacity() * 255);
        var red = (int) (color.getRed() * 255);
        var green = (int) (color.getGreen() * 255);
        var blue = (int) (color.getBlue() * 255);

        return (alpha << 24) | (red << 16) | (green << 8) | blue;
    }

    public static Color colorFromInteger(int color) {
        var alpha = (color >> 24) & 0xFF;
        var red = (color >> 16) & 0xFF;
        var green = (color >> 8) & 0xFF;
        var blue = color & 0xFF;

        return Color.color(red / 255.0, green / 255.0, blue / 255.0, alpha / 255.0);
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

    public static TextStyle convertToTextStyle(MutableTextStyle mutableStyle) {
        return new TextStyle(mutableStyle.bold, mutableStyle.italic, mutableStyle.underline, mutableStyle.strikethrough,
                colorFromInteger(mutableStyle.textColor),
                colorFromInteger(mutableStyle.backgroundColor),
                mutableStyle.fontSize
        );
    }
}
