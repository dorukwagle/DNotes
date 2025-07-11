package com.doruk.dnotes.controllers;

import com.doruk.dnotes.interfaces.IController;
import com.doruk.dnotes.interfaces.INavigationController;
import com.doruk.dnotes.interfaces.IHomeView;
import javafx.scene.Parent;

public class HomePageController implements IController {

    private final IHomeView homePageView;
    private final INavigationController navigationController;

    public HomePageController(IHomeView view, INavigationController navigationController) {
        this.homePageView = view;
        this.navigationController = navigationController;
        setupActions();
    }

    @Override
    public Parent getView() {
        return homePageView.getView();
    }

    private void setupActions() {
        // homePageView.getBookButton().setOnAction(event -> {
        //     this.navigationController.goToBooksPage();
        // });
    }
}
