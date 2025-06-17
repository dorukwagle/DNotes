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
        setupBackButtonAction();
    }

    @Override
    public Parent getView() {
        return editorPageView.getView();
    }

    private void setupBackButtonAction() {
        editorPageView.getBackButton().setOnAction(event -> {
            // TODO: Implement actual back navigation using navigationController
            System.out.println("Back button clicked in EditorController");
            // Example: navigationController.goBack(); or navigationController.goToBooksPage();
        });
    }
}
