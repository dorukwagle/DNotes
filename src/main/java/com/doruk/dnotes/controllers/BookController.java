package com.doruk.dnotes.controllers;

import com.doruk.dnotes.interfaces.IBookView;
import com.doruk.dnotes.interfaces.IController;
import com.doruk.dnotes.interfaces.INavigationController;
import com.doruk.dnotes.views.BookPage;
import javafx.scene.Parent;

public class BookController implements IController {

    private final IBookView bookPageView;
    private final INavigationController navigationController;

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
            this.navigationController.goToEditorPage();
        });
    }
}
