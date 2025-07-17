package com.doruk.dnotes.prefs;

import java.util.prefs.Preferences;

public class EditorPreference extends APreference {
    private static EditorPreference instance = new EditorPreference();

    private EditorPreference() {
        prefs = Preferences.userRoot().node("com/doruk/dnotes/prefs/editor");
    }

    public static EditorPreference getInstance() {
        return instance;
    }
}
