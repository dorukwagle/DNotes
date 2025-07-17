package com.doruk.dnotes;

import com.doruk.dnotes.interfaces.IPreference;
import com.doruk.dnotes.prefs.EditorPreference;
import com.doruk.dnotes.prefs.GlobalPreference;

public class DIFactory {
    public static IPreference createGlobalPreference() {
        return GlobalPreference.getInstance();
    }

    public static IPreference createEditorPreference() {
        return EditorPreference.getInstance();
    }
}
