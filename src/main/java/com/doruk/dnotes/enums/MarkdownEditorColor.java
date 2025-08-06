package com.doruk.dnotes.enums;

public enum MarkdownEditorColor
 {
    Subtle (1),
    Muted (2);

    private final int id;

    MarkdownEditorColor(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public static MarkdownEditorColor fromId(int id) {
        for (MarkdownEditorColor color : MarkdownEditorColor.values()) {
            if (color.getId() == id) {
                return color;
            }
        }
        return null;
    }
}
