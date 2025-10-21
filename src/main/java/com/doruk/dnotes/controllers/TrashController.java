package com.doruk.dnotes.controllers;

import com.doruk.dnotes.DIFactory;
import com.doruk.dnotes.interfaces.IController;
import com.doruk.dnotes.interfaces.INavigationController;
import com.doruk.dnotes.interfaces.ITrashModel;
import com.doruk.dnotes.interfaces.IView;
import com.doruk.dnotes.views.TrashView;
import javafx.scene.Parent;

import java.util.concurrent.CompletableFuture;

public class TrashController implements IController {
    private INavigationController nav;
    private TrashView view;
    private final ITrashModel model;

    public TrashController(INavigationController nav, IView view) {
        this.nav = nav;
        this.view = (TrashView)view;
        this.model = DIFactory.createTrashModel();

        nav.updateAppTitle("Trash");

        this.setupAction();

        this.loadNotes();
    }

    private void setupAction() {
        this.view.getCloseButton().setOnAction(_ -> this.nav.goToHomePage());

        this.view.getSearchBar().setOnAction(_ -> {
            var text = this.view.getSearchBar().getText();
            if (text.isBlank())
                return;
            this.loadNotes();
        });

        // reset search
        this.view.getSearchBar().textProperty().subscribe(text -> {
            if (text.isEmpty())
                this.loadNotes();
        });

        this.view.getDeleteButton().setOnAction(_ -> this.deleteNotes());

        this.view.getRestoreButton().setOnAction(_ -> this.restoreNotes());
    }

    private void deleteNotes() {
        this.view.displayWarningAndRun("Warning!!", "This action will permanently delete the notes.", () -> {
            this.model.deleteNotes(this.view.getNotesTable().getSelectedItems());
            this.loadNotes();

            // finally cleanup
            CompletableFuture.runAsync(this.model::cleanup);
        });
    }

    private void restoreNotes() {
        this.view.displayWarningAndRun("Are you sure ?", "This action will restore the selected notes.", () -> {
            this.model.restoreNotes(this.view.getNotesTable().getSelectedItems());
            this.loadNotes();
        });
    }

    private void loadNotes() {
        var search = this.view.getSearchBar().getText();
        this.view.getNotesTable().updateContents(
                search.isBlank() ? this.model.getDeletedNotes() : this.model.searchNotes(search));
    }

    @Override
    public Parent getView() {
        return this.view.getView();
    }
}
