package com.doruk.dnotes.controllers;

import com.doruk.dnotes.DIFactory;
import com.doruk.dnotes.views.ContextView;
import com.doruk.dnotes.views.components.GenericModal;

public class ContextMenuController {
    private final ContextView view;
    private final GenericModal modal;

    public ContextMenuController(ContextView view) {
        this.view = view;
        this.modal = DIFactory.createGenericModal(this.view.getView(), true);
    }

    public void showContextMenu() {
        this.modal.showAndWait();
    }
}
