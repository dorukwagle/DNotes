package com.doruk.dnotes.controllers;

import com.doruk.dnotes.DIFactory;
import com.doruk.dnotes.enums.AppStartup;
import com.doruk.dnotes.enums.Preference;
import com.doruk.dnotes.views.ContextView;
import com.doruk.dnotes.views.components.GenericModal;

public class ContextMenuController {
    private final ContextView view;
    private final GenericModal modal;

    public ContextMenuController() {
        this.view = DIFactory.createContextView();
        this.modal = DIFactory.createGenericModal(this.view.getView(), true, 550, 400);
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
