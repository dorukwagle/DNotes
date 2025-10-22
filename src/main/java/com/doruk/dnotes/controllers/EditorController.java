package com.doruk.dnotes.controllers;

import com.doruk.dnotes.DIFactory;
import com.doruk.dnotes.MarkdownEditor.enums.EditorColor;
import com.doruk.dnotes.MarkdownEditor.interfaces.IMarkdownEditor;
import com.doruk.dnotes.dto.BookPageDto;
import com.doruk.dnotes.enums.MarkdownEditorColor;
import com.doruk.dnotes.enums.Preference;
import com.doruk.dnotes.exceptions.ProcessingStageException;
import com.doruk.dnotes.interfaces.IEditorController;
import com.doruk.dnotes.interfaces.IEventManager.InternalEvent;
import com.doruk.dnotes.interfaces.INavigationController;
import com.doruk.dnotes.interfaces.IPreference;
import com.doruk.dnotes.store.GlobalConstants;
import com.doruk.dnotes.utils.HashUtil;
import com.doruk.dnotes.utils.PasswordStore;
import javafx.scene.Parent;

import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class EditorController implements IEditorController {

    private IMarkdownEditor markdownEditor;
    private final INavigationController navigationController;
    private final IPreference preference;
    private BookPageDto currentNote;
    private final ScheduledExecutorService scheduler;
    private boolean isNotesLoaded;
    private String password;

    private static Runnable onShutdown;

    public EditorController(IMarkdownEditor markdownEditor, INavigationController navigationController) {
        this.markdownEditor = markdownEditor;
        this.navigationController = navigationController;
        this.preference = DIFactory.createGlobalPreference();

        var selectedColor = preference.loadLong(Preference.EditorColor, 0);
        var color = MarkdownEditorColor.fromId((int) selectedColor) == MarkdownEditorColor.Subtle ? EditorColor.Subtle
                : EditorColor.Muted;
        markdownEditor.setEditorBackground(color);

        if (onShutdown == null)
            onShutdown = this::close;

        setupActions();

        // initiate auto save
        this.scheduler = Executors.newScheduledThreadPool(1);
        this.scheduler.scheduleAtFixedRate(this::saveEditorDocument, 1, GlobalConstants.AUTO_SAVE_INTERVAL_SEC, TimeUnit.SECONDS);
    }

    private void setupActions() {
        this.markdownEditor.setOnClose(() -> {
            this.close();
            this.navigationController.goToHomePage();
        });
        DIFactory.createEventManager().register(InternalEvent.SHUTDOWN, onShutdown);
        DIFactory.createEventManager().register(InternalEvent.CONTEXT_SWITCH, onShutdown);
    }

    @Override
    public Parent getView() {
        return this.markdownEditor.getView();
    }

    @Override
    public void close() {
        // save the texts and notes
        saveEditorDocument();

        // cleanup editor gracefully
        this.markdownEditor.close();
        this.markdownEditor = null;

        // remove the shutdown listener
        DIFactory.createEventManager().unregister(InternalEvent.SHUTDOWN, onShutdown);
        // also the context switch listener
        DIFactory.createEventManager().unregister(InternalEvent.CONTEXT_SWITCH, onShutdown);
        onShutdown = null;

        // remove the schedular
        this.scheduler.close();
    }

    private void saveEditorDocument() {
        // loading new note takes some time, don't save before document is fully loaded.
        if (!isNotesLoaded)
            return;

        try {
            var writer = this.currentNote.getIsLocked() ?
                    DIFactory.createNoteWriter(markdownEditor, this.password) :
                    DIFactory.createNoteWriter(markdownEditor);

            writer.write(this.currentNote.getContentId());
        } catch (IOException | ProcessingStageException e) {
            throw new ProcessingStageException(
                    e instanceof IOException ? "Failed to create output file" : e.getMessage(), e);
        }
    }

    @Override
    public void loadEditorDocument(BookPageDto note) {
        if (note == null || note.getContentId() == null)
            throw new IllegalArgumentException("Expected fileId: null received...");

        this.currentNote = note;
        // if note is encrypted
        if (note.getIsLocked()) {
            this.password = this.promptPassword();
            if (this.password == null) {
                this.disableEditing();
                return;
            }
        }

        try {
            var reader = note.getIsLocked() ?
                    DIFactory.createNoteReader(markdownEditor, this.password) :
                    DIFactory.createNoteReader(markdownEditor);

           reader.read(note.getContentId());
            this.isNotesLoaded = true; // notes loaded completely
        } catch (IOException | ProcessingStageException e) {
            this.disableEditing();
            throw new ProcessingStageException(e.getMessage(), e);
        }
    }

    private void disableEditing() {
        this.markdownEditor.setDisabled(true);
        this.isNotesLoaded = false;
    }

    private String promptPassword() {
        var fileId = this.currentNote.getContentId();
        final String[] userInput = {null};
        // check the store, if not found, then prompt,
        if (PasswordStore.contains(fileId))
            return PasswordStore.getPassword(fileId);

        // if prompt: then match the entered password with that of the note password hash
        var model = DIFactory.createPasswordPrompt("Password Protected!");
        model.setOnSubmitAction(() -> {
            var password = model.getPassword();
            var remember = model.isRememberPassword();
            if (password == null || password.isEmpty())
                return;
            // verify password
            if (!HashUtil.compareHash(password, this.currentNote.getPassword()))
                return;

            // password is correct
            userInput[0] = password;
            if (remember)
                PasswordStore.addPassword(fileId, password);
        });
        model.showAndWait();

        return userInput[0];
    }
}
