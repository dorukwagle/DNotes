package com.doruk.dnotes.utils;

import atlantafx.base.theme.NordDark;
import atlantafx.base.theme.NordLight;
import atlantafx.base.theme.PrimerDark;
import atlantafx.base.theme.PrimerLight;
import atlantafx.base.theme.Theme;

import java.util.Map;

import com.doruk.dnotes.enums.Themes;

import atlantafx.base.theme.CupertinoDark;
import atlantafx.base.theme.CupertinoLight;
import atlantafx.base.theme.Dracula;
import javafx.application.Application;

public class ThemeManager {

    private static ThemeManager instance;
    private boolean isDark = true;  // Default theme
    private static Map<Themes, Theme> themeCollection;

    private ThemeManager() {
        themeCollection = Map.of(
            Themes.CUPERTINO_DARK, new CupertinoDark(),
            Themes.CUPERTINO_LIGHT, new CupertinoLight(),
            Themes.PRIMER_DARK, new PrimerDark(),
            Themes.PRIMER_LIGHT, new PrimerLight(),
            Themes.NORD_DARK, new NordDark(),
            Themes.NORD_LIGHT, new NordLight(),
            Themes.DRACULA, new Dracula()
        );
    }

    public static ThemeManager getInstance() {
        if (instance == null) instance = new ThemeManager();
        return instance;
    }

    public void applyGlobalTheme(Themes theme) {
        var style = themeCollection.get(theme);
        Application.setUserAgentStylesheet(style.getUserAgentStylesheet());
    }
}
