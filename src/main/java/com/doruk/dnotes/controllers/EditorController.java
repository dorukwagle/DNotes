package com.doruk.dnotes.controllers;

import com.doruk.dnotes.interfaces.IEditorController;
import com.doruk.dnotes.DIFactory;
import com.doruk.dnotes.MarkdownEditor.enums.EditorColor;
import com.doruk.dnotes.MarkdownEditor.interfaces.IMarkdownEditor;
import com.doruk.dnotes.enums.MarkdownEditorColor;
import com.doruk.dnotes.enums.Preference;
import com.doruk.dnotes.interfaces.INavigationController;
import com.doruk.dnotes.interfaces.IPreference;

import javafx.scene.Parent;

public class EditorController implements IEditorController {

    private final IMarkdownEditor markdownEditor;
    private final INavigationController navigationController;
    private final IPreference preference;

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
            this.navigationController.goToBooksPage();
        });
    }

    @Override
    public Parent getView() {
        return this.markdownEditor.getView();
    }

    @Override
    public void close() {
        // close editor gracefully
        // save the texts and notes

        // then finally
        this.navigationController.goToBooksPage();
    }
}
