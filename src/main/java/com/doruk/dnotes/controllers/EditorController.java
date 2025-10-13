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
import com.doruk.dnotes.interfaces.IShutdownListener;
import javafx.application.Platform;
import javafx.scene.Parent;

import java.io.IOException;

public class EditorController implements IEditorController {

    private final IMarkdownEditor markdownEditor;
    private final INavigationController navigationController;
    private final IPreference preference;
    private final byte[] seed;

    private static IShutdownListener onShutdown;

    public EditorController(IMarkdownEditor markdownEditor, INavigationController navigationController) {
        this.markdownEditor = markdownEditor;
        this.navigationController = navigationController;
        this.preference = DIFactory.createGlobalPreference();

        var selectedColor = preference.loadLong(Preference.EditorColor, 0);
        var color = MarkdownEditorColor.fromId((int) selectedColor) == MarkdownEditorColor.Subtle ? EditorColor.Subtle
                : EditorColor.Muted;
        markdownEditor.setEditorBackground(color);

        if (onShutdown == null)
            onShutdown = this::saveEditorDocument;

        seed = new byte[32];
        for (int i = 1; i < 33; i++)
            seed[i - 1] = (byte) i;

        setupActions();

        Platform.runLater(this::loadEditorDocument);
    }

    private void setupActions() {
        this.markdownEditor.setOnClose(() -> {
            this.close();
            this.navigationController.goToBooksPage();
        });
        DIFactory.createShutdownManager().register(onShutdown);
    }

    @Override
    public Parent getView() {
        return this.markdownEditor.getView();
    }

    @Override
    public void close() {
        // save the texts and notes
        saveEditorDocument();

        // close editor gracefully
        this.markdownEditor.close();

        // remove the shutdown listener
        DIFactory.createShutdownManager().unregister(onShutdown);
        onShutdown = null;
    }

    private void saveEditorDocument() {
        try {
            DIFactory.createNoteWriter(markdownEditor)
                    .write("test");
        } catch (IOException | ProcessingStageException e) {
            throw new ProcessingStageException(
                    e instanceof IOException ? "Failed to create output file" : e.getMessage(), e);
        }
    }

    private void loadEditorDocument() {
        this.loadEditorDocument("test");
    }

    private void loadEditorDocument(String fileId) {
        try {
            DIFactory.createNoteReader(markdownEditor)
                    .read(fileId);
        } catch (IOException | ProcessingStageException e) {
            throw new ProcessingStageException(
                    e instanceof IOException ? "Failed to load input file" : e.getMessage(), e);
        }
    }
}
