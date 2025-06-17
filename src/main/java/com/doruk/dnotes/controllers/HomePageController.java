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
        setupBackButtonAction();
    }

    @Override
    public Parent getView() {
        return homePageView.getView();
    }

    private void setupBackButtonAction() {
        // homePageView.getBackButton().setOnAction(event -> {
        //     // TODO: Implement actual back navigation using navigationController
        //     System.out.println("Back button clicked in HomePageController");
        //     // Example: navigationController.goBack(); or navigationController.goToPreviousPage();
        // });
    }
}
