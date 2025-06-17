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
        setupBackButtonAction();
    }

    @Override
    public Parent getView() {
        return bookPageView.getView();
    }

    private void setupBackButtonAction() {
        bookPageView.getBackButton().setOnAction(event -> {
            // TODO: Implement actual back navigation using navigationController
            System.out.println("Back button clicked in BookController");
            // Example: navigationController.goBack(); or navigationController.goToHomePage();
        });
    }
}
