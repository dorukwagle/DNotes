package com.doruk.dnotes;

import com.doruk.dnotes.controllers.ContextMenuController;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;

public class GlobalShortcutListener {
    private final Scene scene;

    public GlobalShortcutListener(Scene scene) {
        this.scene = scene;
        this.registerContextMenuShortcut();
    }

    private void registerContextMenuShortcut() {
        // register global shortcut for context menu i.e. Ctrl + K
        scene.setOnKeyPressed(event -> {
            if (event.isControlDown() && event.getCode() == KeyCode.K) {
                new ContextMenuController().showContextMenu();
            }
        });
    }
}
