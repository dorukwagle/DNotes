package com.doruk.dnotes.controllers;

import java.util.List;
import java.util.stream.Collectors;

import com.doruk.dnotes.ControllerFactory;
import com.doruk.dnotes.DIFactory;
import com.doruk.dnotes.dto.BookPageDto;
import com.doruk.dnotes.dto.PaginationParams;
import com.doruk.dnotes.dto.SearchControlsDto;
import com.doruk.dnotes.enums.Preference;
import com.doruk.dnotes.enums.SortBy;
import com.doruk.dnotes.enums.SortOrder;
import com.doruk.dnotes.enums.ViewPage;
import com.doruk.dnotes.interfaces.IBookView;
import com.doruk.dnotes.interfaces.IController;
import com.doruk.dnotes.interfaces.IEditorController;
import com.doruk.dnotes.interfaces.IModel;
import com.doruk.dnotes.interfaces.INavigationController;
import com.doruk.dnotes.interfaces.IPreference;
import com.doruk.dnotes.store.BookStore;

import javafx.application.Platform;
import javafx.scene.Parent;

public class BookController implements IController {

    private final IBookView view;
    private final INavigationController navigationController;
    private IEditorController editorController;
    private IPreference preference;
    private IModel<BookPageDto> noteModel;
    private List<BookPageDto> notes;
    private PaginationParams noteParams = new PaginationParams();
    private BookPageDto currentEditingNote;
    private static boolean isStartup = true;
    private static boolean isSearchProgress = false;

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

        // check if last opened note belongs to this book, and also isn't deleted
        // then only open the note if in preferences
        if (!isStartup)
            return;
        
        var lastNoteId = preference.loadString(Preference.LastOpenedNoteId, "");
        var exists = !lastNoteId.isEmpty() && 
            this.notes.stream().anyMatch(n -> n.getId().equals(lastNoteId));
        if (exists)
            Platform.runLater(this::openLastNote);
        
        isStartup = false;
    }

    private void openLastNote() {
        // check preference, whether to open last note
        var openLastNote = preference.loadBoolean(Preference.RememberEditor, false);
        var lastNoteId = preference.loadString(Preference.LastOpenedNoteId, "");

        // if disabled, or no last note found: just return
        if (!openLastNote || lastNoteId.isEmpty())
            return;
        
        var lastNote = new BookPageDto().setId(lastNoteId);
        this.view.setSelectedSidebarItem(lastNote);
        this.openNote(lastNote);
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
        var whileSearch = "Search in Progress!. Click on a result to view...";
        this.view.setPlaceholder(isSearchProgress ? whileSearch : feed);

        this.view.setSidebarItems(this.notes);

        // save last opened book
        this.preference.saveString(Preference.LastOpenedBookId, book.get().getId());
    }

    private void openNote(BookPageDto note) {
        if (note == null)
            return;
        
        // gracefully close the existing editor
        if (this.editorController != null) {
            this.editorController.close();
            this.editorController = null; // remove reference
        }

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

        // restore selection after re-rendering
        if (this.currentEditingNote != null)
            this.view.setSelectedSidebarItem(this.currentEditingNote);

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

    @Override
    public Parent getView() {
        return this.view.getView();
    }

    private void searchNotes(SearchControlsDto controls) {
        isSearchProgress = !controls.getSearchField().getText().trim().isEmpty();

        // if searching, close the editor
        if (isSearchProgress && this.editorController != null){
            this.editorController.close();
            this.editorController = null;
            this.currentEditingNote = null;
        }

        this.noteParams = new PaginationParams(
            controls.getSearchField().getText(),
            controls.getSortByToggle().isSelected() ? SortBy.Name : SortBy.Date,
            controls.getSortOrderToggle().isSelected() ? SortOrder.Ascending : SortOrder.Descending
        );

       this.openBook();

        if (!isSearchProgress)
            this.view.setSelectedSidebarItem(this.currentEditingNote);
    }

    private void setupActions() {
        this.view.getBackButton().setOnAction(_ -> this.navigationController.goToHomePage());
        this.view.getNewNoteButton().setOnAction(_ -> this.createNewNote());
        this.view.setSidebarItemOnSelect(this::openNote);
        this.view.setSidebarItemOnRightClick(this::sidebarItemOnRightClick);

        var searchControls = this.view.getSidebarSearchControls();
        searchControls.getSearchField().setOnAction(_ -> this.searchNotes(searchControls));

        searchControls.getSortByToggle().setOnAction(_ -> this.searchNotes(searchControls));
        searchControls.getSortOrderToggle().setOnAction(_ -> this.searchNotes(searchControls));
    }
}
