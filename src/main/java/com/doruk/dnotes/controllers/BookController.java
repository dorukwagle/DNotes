package com.doruk.dnotes.controllers;

import com.doruk.dnotes.ControllerFactory;
import com.doruk.dnotes.dto.CollectionDto;
import com.doruk.dnotes.enums.ViewPage;
import com.doruk.dnotes.interfaces.IBookView;
import com.doruk.dnotes.interfaces.IController;
import com.doruk.dnotes.interfaces.IEditorController;
import com.doruk.dnotes.interfaces.INavigationController;
import com.doruk.dnotes.store.BookStore;

import javafx.application.Platform;
import javafx.scene.Parent;

public class BookController implements IController {

    private final IBookView view;
    private final INavigationController navigationController;
    private IEditorController editorController;

    public BookController(IBookView view, INavigationController navigationController) {
        this.view = view;
        this.navigationController = navigationController;
        setupActions();
        Platform.runLater(() -> {
            this.view.setSelectedSidebarItem(new CollectionDto("5", "Work Tasks", "2025-07-14"));
        });

        // open the book
        var book = BookStore.getSelectedBook();
        if (!book.isPresent()) {
            System.out.println("No book selected, not opening anything");
            return;
        }

        System.out.println("Opening book: " + book.get().getTitle());

        this.view.setPlaceholder("Click on a page to view/edit.");
    }

    @Override
    public Parent getView() {
        return this.view.getView();
    }

    private void setupActions() {
        this.view.getBackButton().setOnAction(_ -> {
            this.navigationController.goToHomePage();
        });

        this.view.setSidebarItemOnSelect(collectionDto -> {
            System.out.println("page selected: " + collectionDto.getName());

            // gracefully close the existing editor
            if (this.editorController != null)
                this.editorController.close();
            
            this.editorController = (IEditorController) ControllerFactory.create(ViewPage.EDITOR, this.navigationController);
            this.view.displayEditor(this.editorController.getView());
            return null;
        });
    }
}
