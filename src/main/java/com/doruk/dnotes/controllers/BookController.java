package com.doruk.dnotes.controllers;

import java.util.List;
import java.util.stream.Collectors;

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
    private BookPageDto currentEditingNote;

    private enum StateAction {
        Create,
        Delete,
        Update
    }

    public BookController(IBookView view, INavigationController navigationController) {
        this.view = view;
        this.navigationController = navigationController;

        this.preference = DIFactory.createGlobalPreference();
        this.noteModel = DIFactory.createNoteModel();

        this.setupActions();

        this.openBook();
    }

    private void openBook() {
        var book = BookStore.getSelectedBook();
        if (book.isEmpty() || book.get().getId().isEmpty()) {
            this.view.setPlaceholder("Selected book doesn't exist!");
            return;
        }

        this.notes = this.noteModel.ofParentId(book.get().getId())
                .getAll(this.noteParams);

        var feed = this.notes.isEmpty() ? "No pages found! Create one to get started..."
                : "Click on a Note to view/edit.";
        this.view.setPlaceholder(feed);

        this.view.setSidebarItems(this.notes);

        // save last opened book
        this.preference.saveString(Preference.LastOpenedBookId, book.get().getId());
    }

    private void openNote(BookPageDto note) {
        if (this.editorLock)
            return; // locked: do not open it

        // gracefully close the existing editor
        if (this.editorController != null)
            this.editorController.close();

        this.editorController = (IEditorController) ControllerFactory.create(ViewPage.EDITOR,
                this.navigationController);
        this.view.displayEditor(this.editorController.getView());

        this.currentEditingNote = note;
        this.preference.saveString(Preference.LastOpenedNoteId, note.getId());
    }

    private void createNewNote() {
        var model = DIFactory.createPromptModal("New Note", "Create a new note", "Name:");
        var res = model.showAndWait();

        if (!res.isPresent() || res.get().trim().isEmpty())
            return;

        var note = this.noteModel.add(new BookPageDto(
                "",
                BookStore.getSelectedBook().get().getId(),
                res.get(),
                "", 
                "")
            );

        this.updateSidebarState(note, StateAction.Create);
    }

    private void updateSidebarState(BookPageDto note, StateAction action) {
        var isFirstNote = this.notes.isEmpty();

        if (action != StateAction.Create) {
            this.notes = this.notes.stream()
                    .filter(n -> !n.getId().equals(note.getId()))
                    .collect(Collectors.toList());
            }

        if (action != StateAction.Delete)
            this.notes.addFirst(note);

        this.view.setSidebarItems(this.notes);
        this.view.setSelectedSidebarItem(note);

        if (action == StateAction.Delete && this.notes.isEmpty())
            this.view.setPlaceholder("Nothing left here...");
        
        if (isFirstNote)
            this.view.setPlaceholder("Your first note is created, Click on it to continue...");
    }

    private void deleteNote(BookPageDto note) {
        // cannot delete currently editing note
        if (this.currentEditingNote != null && 
            this.currentEditingNote.getId().equals(note.getId())) {
                DIFactory.createConfirmationModal("Cannot Delete", "You cannot delete the note you are currently editing")
                .showAndWait();
                return;
            }

        this.noteModel.softDelete(note.getId());
        this.updateSidebarState(note, StateAction.Delete);
    }

    private void updateNote(BookPageDto note, String updatedName) {
        var updatedNote = this.noteModel.update(new BookPageDto(
                note.getId(),
                BookStore.getSelectedBook().get().getId(),
                updatedName,
                note.getContent(), 
                "")
            );

        this.updateSidebarState(updatedNote, StateAction.Update);
    }

    private void sidebarItemOnRightClick(BookPageDto note) {
        this.editorLock = true;
        
        var modal = DIFactory.createOptionsModal();
        modal.setInputText(note.getName());

        modal.setOnDeleteAction(() -> {
            if (!modal.isConfirmationChecked())
                return;

            this.deleteNote(note);
        });

        modal.setOnUpdateAction(() ->
            this.updateNote(note, modal.getInputText())
        );

        modal.showAndWait();
    }

    private void clickOnNote(BookPageDto note) {
        this.editorLock = false;
        this.view.setSelectedSidebarItem(note);
        this.openNote(note);
    }

    @Override
    public Parent getView() {
        return this.view.getView();
    }

    private void setupActions() {
        this.view.getBackButton().setOnAction(_ -> this.navigationController.goToHomePage());
        this.view.getNewNoteButton().setOnAction(_ -> this.createNewNote());
        this.view.setSidebarItemOnSelect(this::openNote);
        this.view.setSidebarItemOnRightClick(this::sidebarItemOnRightClick);
    }
}
