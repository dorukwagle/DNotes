package com.doruk.dnotes.controllers;

import com.doruk.dnotes.DIFactory;
import com.doruk.dnotes.enums.AppStartup;
import com.doruk.dnotes.enums.Preference;
import com.doruk.dnotes.interfaces.INavigationController;
import com.doruk.dnotes.views.ContextView;
import com.doruk.dnotes.views.components.GenericModal;

import java.util.stream.Stream;

public class ContextMenuController {
    private final ContextView view;
    private final GenericModal modal;
    private final INavigationController navController;

    public ContextMenuController(INavigationController navController) {
        this.view = DIFactory.createContextView();
        this.modal = DIFactory.createGenericModal(this.view.getView(), true, 550, 400);
        this.navController = navController;

        // setup actions
        this.view.getCollectionsButton().setOnAction(_ -> this.navController.goToHomePage());
//        this.view.getSharedWithMeButton().setOnAction(_ -> this.navController.goToBooksPage());
//        this.view.getAddQuickNoteButton().setOnAction(_ -> this.navController.goToEditorPage());
//        this.view.getViewQuickNotesButton().setOnAction(_ -> this.navController.goToBooksPage());

        // close dialog while any btn clicked
        Stream.of(
                this.view.getCollectionsButton(),
                this.view.getSharedWithMeButton(),
                this.view.getAddQuickNoteButton(),
                this.view.getViewQuickNotesButton()
        ).forEach(btn -> btn.setOnMouseClicked(_ -> this.modal.close()));
    }

    public void showContextMenu() {
        this.modal.showAndWait();
    }

    public void showContextMenuAtStartup() {
        if (!DIFactory.createGlobalPreference().loadBoolean(Preference.ShowContextMenuAtStartup, true))
            return;
        this.modal.showAndWait();
    }
}
