package com.doruk.dnotes.interfaces;

import java.util.function.Consumer;

import com.doruk.dnotes.enums.AppStartup;
import com.doruk.dnotes.enums.MarkdownEditorColor;
import com.doruk.dnotes.enums.Themes;

import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;

public interface IPreferenceView extends IView {
    Button getBackButton();
    void setThemeOnSelect(Consumer<Integer> themeOnSelect);
    void setEditorColorOnSelect(Consumer<Integer> editorColorOnSelect);
    void setSelectedTheme(Themes theme);
    void setSelectedEditorColor(MarkdownEditorColor color);
    void setAppStartupOnSelect(Consumer<Integer> appStartupOnSelect);
    void setRememberAppStateOnSelect(Consumer<Boolean> rememberAppStateOnSelect);
    void setRememberEditorOnSelect(Consumer<Boolean> rememberEditorOnSelect);
    void setSelectedAppStartup(AppStartup appStartup);
    void setRememberAppState(Boolean rememberAppState);
    void setRememberEditor(Boolean rememberEditor);

    void setShowContextAtStartup(Boolean showContextAtStartup);

    CheckBox getShowContextAtStartup();
}
