package com.doruk.dnotes.controllers;

import com.doruk.dnotes.DIFactory;
import com.doruk.dnotes.MarkdownEditor.enums.EditorColor;
import com.doruk.dnotes.MarkdownEditor.interfaces.IMarkdownEditor;
import com.doruk.dnotes.enums.MarkdownEditorColor;
import com.doruk.dnotes.enums.Preference;
import com.doruk.dnotes.exceptions.ProcessingStageException;
import com.doruk.dnotes.interfaces.IEditorController;
import com.doruk.dnotes.interfaces.INavigationController;
import com.doruk.dnotes.interfaces.IPreference;
import com.doruk.dnotes.store.GlobalConstants;
import javafx.scene.Parent;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import com.doruk.dnotes.interfaces.IEventManager.InternalEvent;

public class EditorController implements IEditorController {

    private IMarkdownEditor markdownEditor;
    private final INavigationController navigationController;
    private final IPreference preference;
    private String currentFileId;
    private ScheduledExecutorService scheduler;
    private boolean isNotesLoaded;

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
            DIFactory.createNoteWriter(markdownEditor)
                    .write(this.currentFileId);
        } catch (IOException | ProcessingStageException e) {
            throw new ProcessingStageException(
                    e instanceof IOException ? "Failed to create output file" : e.getMessage(), e);
        }
    }

    @Override
    public void loadEditorDocument(String fileId) {
        if (fileId == null)
            throw new IllegalArgumentException("Expected fileId: null received...");
        this.currentFileId = fileId;
        try {
            DIFactory.createNoteReader(markdownEditor)
                    .read(fileId);
            this.isNotesLoaded = true; // notes loaded completely
        } catch (IOException | ProcessingStageException e) {
            this.isNotesLoaded = false;
            throw new ProcessingStageException(
                    e instanceof IOException ? "Failed to load input file" : e.getMessage(), e);
        }
    }
}
