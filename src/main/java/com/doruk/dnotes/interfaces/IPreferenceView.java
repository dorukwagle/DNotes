package com.doruk.dnotes.interfaces;

import java.util.function.Consumer;

import com.doruk.dnotes.enums.EditorColor;
import com.doruk.dnotes.enums.Themes;

import javafx.scene.control.Button;

public interface IPreferenceView extends IView {
    Button getBackButton();
    void setThemeOnSelect(Consumer<Integer> themeOnSelect);
    void setEditorColorOnSelect(Consumer<Integer> editorColorOnSelect);
    void setSelectedTheme(Themes theme);
    void setSelectedEditorColor(EditorColor color);
}
