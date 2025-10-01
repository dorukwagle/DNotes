package com.doruk.dnotes.controllers;

import com.doruk.dnotes.interfaces.IEditorController;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import com.doruk.dnotes.DIFactory;
import com.doruk.dnotes.MarkdownEditor.enums.EditorColor;
import com.doruk.dnotes.MarkdownEditor.enums.ToolName;
import com.doruk.dnotes.MarkdownEditor.interfaces.IMarkdownEditor;
import com.doruk.dnotes.enums.MarkdownEditorColor;
import com.doruk.dnotes.enums.Preference;
import com.doruk.dnotes.exceptions.ProcessingStageException;
import com.doruk.dnotes.interfaces.INavigationController;
import com.doruk.dnotes.interfaces.IPreference;
import com.doruk.dnotes.interfaces.IShutdownListener;

import javafx.application.Platform;
import javafx.scene.Parent;

public class EditorController implements IEditorController {

    private final IMarkdownEditor markdownEditor;
    private final INavigationController navigationController;
    private final IPreference preference;

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
        var encoded = markdownEditor.encodeAndDump();
        var encoder = DIFactory.createMarkdownEncoder(markdownEditor.getCodecsValues());
        try {
            var stream = new BufferedOutputStream(Files.newOutputStream(Path.of("test.dnt")));
            encoder.encode(encoded, stream);
            stream.flush();
            stream.close();
        } catch (IOException | ProcessingStageException e) {
            throw new ProcessingStageException(
                    e instanceof IOException ? "Failed to create output file" : e.getMessage(), e);
        }
    }

    private void loadEditorDocument() {
        var decoder = DIFactory.createMarkdownDecoder(markdownEditor.getCodecsValues());
        try {
            var stream = new BufferedInputStream(Files.newInputStream(Path.of("test.dnt")));
            decoder.decode(stream, markdownEditor::decodeAndLoad);
        } catch (IOException | ProcessingStageException e) {
            throw new ProcessingStageException(
                    e instanceof IOException ? "Failed to load input file" : e.getMessage(), e);
        }
    }
}
