package com.doruk.dnotes.enums;

public enum ViewPage {
    HOME (1),
    BOOK (2),
    EDITOR (3),
    PREFERENCE (4);

    private final int id;

    ViewPage(int id) {
        this.id = id;
    }

    public int getId() {
        return this.id;
    }

    public static ViewPage fromId(int id) {
        for (ViewPage page : ViewPage.values()) {
            if (page.getId() == id) {
                return page;
            }
        }
        return null;
    }
}
