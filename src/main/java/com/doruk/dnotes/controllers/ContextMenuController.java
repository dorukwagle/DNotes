package com.doruk.dnotes.controllers;

import com.doruk.dnotes.DIFactory;
import com.doruk.dnotes.enums.Preference;
import com.doruk.dnotes.interfaces.IEventManager;
import com.doruk.dnotes.interfaces.INavigationController;
import com.doruk.dnotes.views.ContextView;
import com.doruk.dnotes.views.components.GenericModal;
import javafx.scene.input.MouseEvent;

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
        this.view.getCollectionsButton().addEventHandler(MouseEvent.MOUSE_CLICKED, _ ->
                this.navController.goToHomePage());

        this.view.getSharedWithMeButton().addEventHandler(MouseEvent.MOUSE_CLICKED, _ ->
            DIFactory.createSharedNoteController(this.navController));

        this.view.getViewQuickNotesButton().addEventHandler(MouseEvent.MOUSE_CLICKED, _ ->
            DIFactory.createQuickNoteController(this.navController).open());

        this.view.getAddQuickNoteButton().addEventHandler(MouseEvent.MOUSE_CLICKED, _ ->
            DIFactory.createQuickNoteController(this.navController).addNew());

        this.view.getOpenNotesManagementButton().addEventHandler(MouseEvent.MOUSE_CLICKED, _ ->
            this.navController.goToManagementPage());

        // cleanup dialog while any btn clicked
        Stream.of(
                this.view.getCollectionsButton(),
                this.view.getSharedWithMeButton(),
                this.view.getAddQuickNoteButton(),
                this.view.getViewQuickNotesButton(),
                this.view.getOpenNoteButton(),
                this.view.getOpenNotesManagementButton()
        ).forEach(btn -> btn.addEventFilter(MouseEvent.MOUSE_CLICKED, _ -> {
            DIFactory.createEventManager().publishEvent(IEventManager.InternalEvent.CONTEXT_SWITCH);
            this.modal.close();
        }));
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
