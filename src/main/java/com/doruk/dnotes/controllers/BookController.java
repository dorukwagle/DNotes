package com.doruk.dnotes.controllers;

import com.doruk.dnotes.ControllerFactory;
import com.doruk.dnotes.enums.ViewPage;
import com.doruk.dnotes.interfaces.IBookView;
import com.doruk.dnotes.interfaces.IController;
import com.doruk.dnotes.interfaces.IEditorController;
import com.doruk.dnotes.interfaces.INavigationController;

import javafx.scene.Parent;

public class BookController implements IController {

    private final IBookView view;
    private final INavigationController navigationController;
    private IEditorController editorController;

    public BookController(IBookView view, INavigationController navigationController) {
        this.view = view;
        this.navigationController = navigationController;
        setupActions();
    }

    @Override
    public Parent getView() {
        return this.view.getView();
    }

    private void setupActions() {
        this.view.getBackButton().setOnAction(event -> {
            this.navigationController.goToHomePage();
        });

        this.view.getEditorButton().setOnAction(event -> {
            // close editorController gracefully
            if (this.editorController != null)
                this.editorController.close();

            this.editorController = (IEditorController) ControllerFactory.create(ViewPage.EDITOR, this.navigationController);
            this.view.displayEditor(this.editorController.getView());
        });
    }
}
