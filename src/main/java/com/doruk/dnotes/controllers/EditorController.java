package com.doruk.dnotes.controllers;

import com.doruk.dnotes.interfaces.IEditorController;
import com.doruk.dnotes.interfaces.IMarkdownEditor;
import com.doruk.dnotes.interfaces.INavigationController;

import javafx.scene.Parent;

public class EditorController implements IEditorController {

    private IMarkdownEditor markdownEditor;
    private final INavigationController navigationController;

    public EditorController(IMarkdownEditor markdownEditor, INavigationController navigationController) {
        this.markdownEditor = markdownEditor;
        this.navigationController = navigationController;
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
        // this.navigationController.goToBooksPage();
        this.markdownEditor.getView().setManaged(false);
        this.markdownEditor.getView().setVisible(false);
        this.markdownEditor = null; // remove reference
    }
}
