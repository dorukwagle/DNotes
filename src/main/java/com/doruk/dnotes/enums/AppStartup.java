package com.doruk.dnotes.enums;

public enum AppStartup {
    FromLastOpened (1),
    StartFresh (2);

    private final int id;

    AppStartup(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public static AppStartup fromId(int id) {
        for (AppStartup startup : AppStartup.values()) {
            if (startup.getId() == id) {
                return startup;
            }
        }
        return null;
    }
}
