package com.doruk.dnotes.controllers;

import javafx.scene.Parent;

import com.doruk.dnotes.DIFactory;
import com.doruk.dnotes.enums.EditorColor;
import com.doruk.dnotes.enums.Preference;
import com.doruk.dnotes.enums.Themes;
import com.doruk.dnotes.interfaces.IController;
import com.doruk.dnotes.interfaces.INavigationController;
import com.doruk.dnotes.interfaces.IPreference;
import com.doruk.dnotes.interfaces.IPreferenceView;

public class PreferenceController implements IController {
    private final IPreferenceView view;
    private final INavigationController navigationController;
    private final IPreference preference;

    public PreferenceController(IPreferenceView view, INavigationController navigationController) {
        this.view = view;
        this.navigationController = navigationController;
        this.preference = DIFactory.createGlobalPreference();

        // load preferences view
        this.view.setSelectedTheme(
                Themes.fromId(
                        (int) this.preference.loadLong(Preference.Theme, 0)));
        this.view.setSelectedEditorColor(
                EditorColor.fromId(
                        (int) this.preference.loadLong(Preference.EditorColor, 0)));

        // add listeners
        setupActions();
    }

    @Override
    public Parent getView() {
        return this.view.getView();
    }

    private void setupActions() {
        this.view.getBackButton().setOnAction(_ -> {
            this.navigationController.goToHomePage();
        });

        this.view.setThemeOnSelect(id -> {
            this.preference.saveLong(Preference.Theme, id);
        });

        this.view.setEditorColorOnSelect(id -> {
            this.preference.saveLong(Preference.EditorColor, id);
        });
    }
}
