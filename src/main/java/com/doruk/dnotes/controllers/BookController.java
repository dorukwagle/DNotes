package com.doruk.dnotes.controllers;

import com.doruk.dnotes.ControllerFactory;
import com.doruk.dnotes.DIFactory;
import com.doruk.dnotes.dto.BookPageDto;
import com.doruk.dnotes.dto.PaginationParams;
import com.doruk.dnotes.dto.SearchControlsDto;
import com.doruk.dnotes.enums.*;
import com.doruk.dnotes.interfaces.*;
import com.doruk.dnotes.store.BookStore;
import com.doruk.dnotes.utils.PathUtils;
import javafx.application.Platform;
import javafx.scene.Parent;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.input.MouseEvent;
import org.kordamp.ikonli.javafx.FontIcon;
import org.kordamp.ikonli.materialdesign2.MaterialDesignF;
import org.kordamp.ikonli.materialdesign2.MaterialDesignS;

import java.util.List;
import java.util.stream.Collectors;

public class BookController implements IController {

    private final IBookView view;
    private final INavigationController navigationController;
    private IEditorController editorController;
    private final IPreference preference;
    private final IModel<BookPageDto> noteModel;
    private List<BookPageDto> notes;
    private PaginationParams noteParams = new PaginationParams();
    private BookPageDto currentEditingNote;
    private static boolean isStartup = true;
    private static boolean isSearchProgress = false;
    private final NoteType noteType;

    private ContextMenu contextMenu;
    private MenuItem update;
    private MenuItem security;
    private MenuItem share;

    private enum StateAction {
        Create,
        Delete,
        Update
    }

    // always normal note
    public BookController(IBookView view, INavigationController navigationController) {
        this.view = view;
        this.navigationController = navigationController;

        this.preference = DIFactory.createGlobalPreference();
        this.noteModel = DIFactory.createNoteModel();

        this.noteType = NoteType.NORMAL;

        // update app title
        navigationController.updateAppTitle("Notes");

        init();
    }

    // this can be either shared note or quick note
    public BookController(IBookView view, INavigationController navigationController, IModel<BookPageDto> noteModel) {
        this.view = view;
        this.navigationController = navigationController;
        this.noteModel = noteModel;
        this.preference = DIFactory.createGlobalPreference();

        // now read the BookStore
        this.noteType = BookStore.getNoteType();
        var addNew = BookStore.isAddNewPage();

        init();

        // if it's shared, disable creation
        if (this.noteType == NoteType.SHARED) {
            this.view.getNewNoteButton().setVisible(false);
            return;
        }

        if (!addNew)
            return;

        // add new quick note
        // run on next pulse, let the UI initialize first
        Platform.runLater(() -> {
            var note = this.createNewNote();
            if (note != null)
                this.openNote(note);
        });
    }

    private void init() {
        this.setupActions();

        // initialize context menu
        this.initContextMenu();

        this.openBook();

        // check if last opened note belongs to this book, and also isn't deleted
        // then only open the note if in preferences
        if (!isStartup || this.noteType != NoteType.NORMAL) {
            isStartup = false; // if its not NORMAL note
            return;
        }

        // if its app startup, i.e. first time the book controller is opened.
        // try to resume from the last position as per preference

        var lastNoteId = preference.loadString(Preference.LastOpenedNoteId, "");
        var exists = !lastNoteId.isEmpty() &&
                this.notes.stream().anyMatch(n -> n.getId().equals(lastNoteId));
        if (exists)
            Platform.runLater(this::openLastNote);

        isStartup = false;
    }

    private void initContextMenu() {
        FontIcon icon;
        contextMenu = new ContextMenu();

        update = new MenuItem("Manage");
        icon = new FontIcon(MaterialDesignF.FILE_EDIT);
        icon.setScaleX(1.5);
        icon.setScaleY(1.5);
        update.setGraphic(icon);

        security = new MenuItem("Security");
        icon = new FontIcon(MaterialDesignS.SECURITY);
        icon.setScaleX(1.5);
        icon.setScaleY(1.5);
        security.setGraphic(icon);

        share = new MenuItem("Share");
        icon = new FontIcon(MaterialDesignS.SHARE_VARIANT);
        icon.setScaleX(1.5);
        icon.setScaleY(1.5);
        share.setGraphic(icon);

        contextMenu.getItems().addAll(update, security, share);

        contextMenu.setHideOnEscape(true);
        contextMenu.setAutoHide(true); // hide when clicked outside
    }

    private void openLastNote() {
        // check preference, whether to open last note
        var openLastNote = preference.loadBoolean(Preference.RememberEditor, false);
        var lastNoteId = preference.loadString(Preference.LastOpenedNoteId, "");

        // if disabled, or no last note found: just return
        if (!openLastNote || lastNoteId.isEmpty())
            return;

        var lastNote = this.noteModel.get(lastNoteId);
        this.view.setSelectedSidebarItem(lastNote);
        this.openNote(lastNote);
    }

    private void openBook() {
        var book = BookStore.getSelectedBook();
        if (this.noteType == NoteType.NORMAL && (book.isEmpty() || book.get().getId().isEmpty())) {
            this.view.setPlaceholder("Selected book doesn't exist!");
            return;
        }

        this.notes = this.noteModel.ofParentId(this.noteType == NoteType.NORMAL ? book.get().getId() : null)
                .getAll(this.noteParams);

        var feed = this.notes.isEmpty() ? "No pages found! Create one to get started..."
                : "Click on a Note to view/edit.";
        var whileSearch = "Search in Progress!. Click on a result to view...";
        this.view.setPlaceholder(isSearchProgress ? whileSearch : feed);

        this.view.setSidebarItems(this.notes);

        // save last opened book if it's normal book
        if (this.noteType != NoteType.NORMAL)
            return;

        this.preference.saveString(Preference.LastOpenedBookId, book.get().getId());
    }

    private void openNote(BookPageDto note) {
        if (note == null)
            return;
        
        // gracefully cleanup the existing editor
        if (this.editorController != null) {
            this.editorController.close();
            this.editorController = null; // remove reference
        }

        this.editorController = (IEditorController) ControllerFactory.create(ViewPage.EDITOR,
                this.navigationController);
        // load the note into markdown editor
        this.editorController.loadEditorDocument(note);

        this.view.displayEditor(this.editorController.getView());

        this.currentEditingNote = note;
        this.preference.saveString(Preference.LastOpenedNoteId, note.getId());

        // when invoked internally without user clicking the item, it's still unselected, so
        // select it
        this.view.setSelectedSidebarItem(note);
    }

    private BookPageDto createNewNote() {
        var model = DIFactory.createPromptModal("New Note", "Create a new note", "Name:");
        var res = model.showAndWait();

        if (res.isEmpty() || res.get().trim().isEmpty())
            return null;

        // create a new note, with the fileId as content
        var note = this.noteModel.add(new BookPageDto(
                "",
                this.noteType == NoteType.NORMAL ? BookStore.getSelectedBook().get().getId() : null,
                res.get(),
                PathUtils.generateFileId(),
                "")
            );

        this.updateSidebarState(note, StateAction.Create);
        return note;
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
                this.noteType == NoteType.NORMAL ? BookStore.getSelectedBook().get().getId() : null,
                updatedName,
                note.getContentId(),
                "")
            );

        this.updateSidebarState(updatedNote, StateAction.Update);
    }

    private void updateOrDelete(BookPageDto note) {
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

    private void sidebarItemOnRightClick(MouseEvent event, BookPageDto note) {

        update.setOnAction(_ -> this.updateOrDelete(note));
        security.setOnAction(_ -> new SecurityController(note));
//        share.setOnAction(_ -> new ShareController(note));

        contextMenu.show(event.getPickResult().getIntersectedNode(), event.getScreenX(), event.getScreenY());
    }

    @Override
    public Parent getView() {
        return this.view.getView();
    }

    private void searchNotes(SearchControlsDto controls) {
        isSearchProgress = !controls.getSearchField().getText().trim().isEmpty();

        // if searching, cleanup the editor
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
