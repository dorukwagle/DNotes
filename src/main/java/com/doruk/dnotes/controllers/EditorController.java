package com.doruk.dnotes.controllers;

import com.doruk.dnotes.interfaces.IController;
import com.doruk.dnotes.interfaces.INavigationController;
import com.doruk.dnotes.interfaces.IEditorView;
import javafx.scene.Parent;

public class EditorController implements IController {

    private final IEditorView editorPageView;
    private final INavigationController navigationController;

    public EditorController(IEditorView editorPageView, INavigationController navigationController) {
        this.editorPageView = editorPageView;
        this.navigationController = navigationController;
        setupActions();
    }

    @Override
    public Parent getView() {
        return editorPageView.getView();
    }

    private void setupActions() {
        editorPageView.getBackButton().setOnAction(event -> {
            this.navigationController.goToBooksPage();
        });
    }
}
