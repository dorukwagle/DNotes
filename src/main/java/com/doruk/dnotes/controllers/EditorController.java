package com.doruk.dnotes.controllers;

import com.doruk.dnotes.MarkdownEditor.IEditorView;
import com.doruk.dnotes.interfaces.IEditorController;
import com.doruk.dnotes.interfaces.IMarkdownEditor;
import com.doruk.dnotes.interfaces.INavigationController;

import javafx.scene.Parent;

public class EditorController implements IEditorController {

    private final IMarkdownEditor markdownEditor;
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
    public void openPage(String pageId) {
        
    }

    @Override
    public void close() {
        
    }
}
