package com.doruk.dnotes.controllers;

import com.doruk.dnotes.interfaces.IEditorController;
import com.doruk.dnotes.DIFactory;
import com.doruk.dnotes.MarkdownEditor.enums.EditorColor;
import com.doruk.dnotes.MarkdownEditor.interfaces.IMarkdownEditor;
import com.doruk.dnotes.enums.MarkdownEditorColor;
import com.doruk.dnotes.enums.Preference;
import com.doruk.dnotes.interfaces.INavigationController;
import com.doruk.dnotes.interfaces.IPreference;
import com.doruk.dnotes.interfaces.IShutdownListener;

import javafx.scene.Parent;

public class EditorController implements IEditorController {

    private final IMarkdownEditor markdownEditor;
    private final INavigationController navigationController;
    private final IPreference preference;

    private static final IShutdownListener onShutdown = EditorController::saveEditorDocument;

    public EditorController(IMarkdownEditor markdownEditor, INavigationController navigationController) {
        this.markdownEditor = markdownEditor;
        this.navigationController = navigationController;
        this.preference = DIFactory.createGlobalPreference();

        var selectedColor = preference.loadLong(Preference.EditorColor, 0);
        var color = MarkdownEditorColor.fromId((int) selectedColor) == MarkdownEditorColor.Subtle ?
            EditorColor.Subtle : EditorColor.Muted;
        markdownEditor.setEditorBackground(color);

        setupActions();
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
    }

    private static void saveEditorDocument() {
        System.out.println("Saving editor document...");
    }
}
