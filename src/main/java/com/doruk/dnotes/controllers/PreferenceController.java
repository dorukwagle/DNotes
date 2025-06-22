package com.doruk.dnotes.controllers;

import javafx.scene.Parent;
import com.doruk.dnotes.interfaces.IController;
import com.doruk.dnotes.interfaces.INavigationController;
import com.doruk.dnotes.interfaces.IPreferenceView;

public class PreferenceController implements IController {
    private final IPreferenceView view;
    private final INavigationController navigationController;

    public PreferenceController(IPreferenceView view, INavigationController navigationController) {
        this.view = view;
        this.navigationController = navigationController;
        setupActions();
    }

    @Override
    public Parent getView() {
        return this.view.getView();
    }

    private void setupActions() {
        this.view.getBackButton().setOnAction(event -> {
            this.navigationController.goToHomePage();
        });
    }
}
