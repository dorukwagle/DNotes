package com.doruk.dnotes.prefs;

import java.util.prefs.Preferences;

public class GlobalPreference extends APreference {
    private static GlobalPreference instance = new GlobalPreference();

    private GlobalPreference() {
        prefs = Preferences.userRoot().node("com/doruk/dnotes/prefs/global");
    }

    public static GlobalPreference getInstance() {
        return instance;
    }
}
