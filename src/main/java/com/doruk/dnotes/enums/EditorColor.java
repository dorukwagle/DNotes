package com.doruk.dnotes.enums;

public enum EditorColor
 {
    Subtle (1),
    Muted (2);

    private final int id;

    EditorColor(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public static EditorColor fromId(int id) {
        for (EditorColor color : EditorColor.values()) {
            if (color.getId() == id) {
                return color;
            }
        }
        return null;
    }
}
