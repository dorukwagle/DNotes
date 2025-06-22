package com.doruk.dnotes.controllers;

import com.doruk.dnotes.interfaces.IBookView;
import com.doruk.dnotes.interfaces.IController;
import com.doruk.dnotes.interfaces.IEditorController;
import com.doruk.dnotes.interfaces.INavigationController;
import javafx.scene.Parent;

public class BookController implements IController {

    private final IBookView bookPageView;
    private final INavigationController navigationController;
    private IEditorController editorController;

    public BookController(IBookView view, INavigationController navigationController) {
        this.bookPageView = view;
        this.navigationController = navigationController;
        setupActions();
    }

    @Override
    public Parent getView() {
        return bookPageView.getView();
    }

    private void setupActions() {
        bookPageView.getBackButton().setOnAction(event -> {
            this.navigationController.goToHomePage();
        });

        bookPageView.getEditorButton().setOnAction(event -> {
            // this.navigationController.goToEditorPage();
            System.out.println("Editor button clicked");
        });
    }
}
