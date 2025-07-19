package com.doruk.dnotes.utils;

import atlantafx.base.theme.NordDark;
import atlantafx.base.theme.NordLight;
import atlantafx.base.theme.PrimerDark;
import atlantafx.base.theme.PrimerLight;
import atlantafx.base.theme.Theme;
import java.util.Map;
import atlantafx.base.theme.CupertinoDark;
import atlantafx.base.theme.CupertinoLight;
import atlantafx.base.theme.Dracula;
import javafx.application.Application;

import com.doruk.dnotes.DIFactory;
import com.doruk.dnotes.enums.Preference;
import com.doruk.dnotes.enums.Themes;
import com.doruk.dnotes.interfaces.IPreference;


public class ThemeManager {
    
    private static ThemeManager instance;
    private static Map<Themes, Theme> themeCollection;
    private static IPreference prefs;

    private ThemeManager() {
        prefs = DIFactory.createGlobalPreference();

        themeCollection = Map.of(
            Themes.CupertinoDark, new CupertinoDark(),
            Themes.CupertinoLight, new CupertinoLight(),
            Themes.PrimerDark, new PrimerDark(),
            Themes.PrimerLight, new PrimerLight(),
            Themes.NordDark, new NordDark(),
            Themes.NordLight, new NordLight(),
            Themes.Dracula, new Dracula()
        );

        // observe the preferance changes
        prefs.addListener(Preference.Theme, (Integer id) -> {
            var theme = Themes.fromId(id);
            var style = themeCollection.get(theme);
            Application.setUserAgentStylesheet(style.getUserAgentStylesheet());
        });
    }

    public static ThemeManager getInstance() {
        if (instance == null) instance = new ThemeManager();
        return instance;
    }

    public void applyGlobalTheme() {
        var savedTheme = prefs.loadLong(Preference.Theme, Themes.CupertinoDark.getId());
        var theme = Themes.fromId((int)savedTheme);
        var style = themeCollection.get(theme);
        Application.setUserAgentStylesheet(style.getUserAgentStylesheet());
    }
}
