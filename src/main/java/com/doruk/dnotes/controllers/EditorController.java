package com.doruk.dnotes.controllers;

import com.doruk.dnotes.interfaces.IEditorController;
import com.doruk.dnotes.interfaces.INavigationController;
import com.doruk.dnotes.interfaces.IEditorView;
import javafx.scene.Parent;

public class EditorController implements IEditorController {

    private final IEditorView editorPageView;
    private final INavigationController navigationController;

    public EditorController(IEditorView editorPageView, INavigationController navigationController) {
        this.editorPageView = editorPageView;
        this.navigationController = navigationController;
        setupActions();
    }

    private void setupActions() {
        editorPageView.getBackButton().setOnAction(event -> {
            this.navigationController.goToBooksPage();
        });
    }

    @Override
    public Parent getView() {
        return editorPageView.getView();
    }

    @Override
    public void openPage(String pageId) {
        
    }

    @Override
    public void close() {
        
    }
}
