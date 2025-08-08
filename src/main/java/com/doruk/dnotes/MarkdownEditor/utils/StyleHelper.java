package com.doruk.dnotes.MarkdownEditor.utils;

import javafx.scene.paint.Color;

public class StyleHelper {
    public static String toRgba(Color color) {
        return String.format("rgba(%d, %d, %d, %.2f)",
                (int) (color.getRed() * 255),
                (int) (color.getGreen() * 255),
                (int) (color.getBlue() * 255),
                color.getOpacity());
    }
}
