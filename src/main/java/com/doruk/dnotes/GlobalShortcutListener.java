package com.doruk.dnotes;

import com.doruk.dnotes.controllers.ContextMenuController;
import com.doruk.dnotes.interfaces.INavigationController;
import javafx.scene.input.KeyCode;

public class GlobalShortcutListener {
    private final INavigationController navigationController;

    public GlobalShortcutListener(INavigationController navigationController) {
        this.navigationController = navigationController;
        this.registerContextMenuShortcut();
    }

    private void registerContextMenuShortcut() {
        // register global shortcut for context menu i.e. Ctrl + K
        this.navigationController.getScene().setOnKeyPressed(event -> {
            if (event.isControlDown() && event.getCode() == KeyCode.K) {
                new ContextMenuController(this.navigationController).showContextMenu();
            }
        });
    }
}
