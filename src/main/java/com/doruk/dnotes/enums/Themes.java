package com.doruk.dnotes.enums;

public enum Themes {
    CupertinoDark (1),
    CupertinoLight (2),
    PrimerDark (3),
    PrimerLight (4),
    NordDark (5),
    NordLight (6),
    Dracula (7);

    private final int id;

    Themes(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public static Themes fromId(int id) {
        for (Themes theme : Themes.values()) {
            if (theme.getId() == id) {
                return theme;
            }
        }
        return null;
    }
}
