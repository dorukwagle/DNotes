package com.doruk.dnotes.controllers;

import java.util.List;

import com.doruk.dnotes.ControllerFactory;
import com.doruk.dnotes.DIFactory;
import com.doruk.dnotes.dto.BookPageDto;
import com.doruk.dnotes.dto.PaginationParams;
import com.doruk.dnotes.enums.Preference;
import com.doruk.dnotes.enums.ViewPage;
import com.doruk.dnotes.interfaces.IBookView;
import com.doruk.dnotes.interfaces.IController;
import com.doruk.dnotes.interfaces.IEditorController;
import com.doruk.dnotes.interfaces.IModel;
import com.doruk.dnotes.interfaces.INavigationController;
import com.doruk.dnotes.interfaces.IPreference;
import com.doruk.dnotes.store.BookStore;

import javafx.scene.Parent;

public class BookController implements IController {

    private final IBookView view;
    private final INavigationController navigationController;
    private IEditorController editorController;
    private IPreference preference;
    private boolean editorLock = false; // prevent editor opening on right click
    private IModel<BookPageDto> noteModel;
    private List<BookPageDto> notes;
    private PaginationParams noteParams = new PaginationParams();

    public BookController(IBookView view, INavigationController navigationController) {
        this.view = view;
        this.navigationController = navigationController;

        this.preference = DIFactory.createGlobalPreference();
        this.noteModel = DIFactory.createNoteModel();

        this.setupActions();

        this.openBook();
    }
    
    private void openBook() {
        if (this.editorLock)
            return; // locked: do not open it

        var book = BookStore.getSelectedBook();
        if (book.isEmpty()) {
            this.view.setPlaceholder("Selected book doesn't exist!");
            return;
        }

        this.notes = this.noteModel.ofParentId(book.get().getId())
            .getAll(this.noteParams);
        
        var feed = this.notes.isEmpty() ? "No pages found! Create one to get started..." : "Click on a Note to view/edit.";
        this.view.setPlaceholder(feed);

        this.view.setSidebarItems(this.notes);

        // save last opened book
        this.preference.saveString(Preference.LastOpenedBookId, book.get().getId());
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
        });
    }
}
