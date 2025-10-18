package com.doruk.dnotes;

import com.doruk.dnotes.controllers.ContextMenuController;
import com.doruk.dnotes.interfaces.IEventManager;
import com.doruk.dnotes.interfaces.INavigationController;

public class GlobalShortcutListener {
    private final INavigationController navigationController;

    public GlobalShortcutListener(INavigationController navigationController) {
        this.navigationController = navigationController;
        this.registerShortcuts();
    }

    private void registerShortcuts() {
        // register global shortcut for context menu i.e. Ctrl + K
        this.navigationController.getScene().setOnKeyPressed(event -> {
            if (!event.isControlDown())
                return;

            switch (event.getCode()) {
                // context menu
                case K -> new ContextMenuController(this.navigationController).showContextMenu();
                // add new quick note
                case Q -> {
                    DIFactory.createEventManager().publishEvent(IEventManager.InternalEvent.CONTEXT_SWITCH);
                    DIFactory.createQuickNoteController(this.navigationController).addNew();
                }
            }
        });
    }
}
