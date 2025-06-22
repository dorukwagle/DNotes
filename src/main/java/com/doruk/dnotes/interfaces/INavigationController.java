package com.doruk.dnotes.interfaces;

public interface INavigationController {
    void goToHomePage();
    void goToBooksPage();
    void goToPreferencePage();
    INavigationController reCreateScene(double w, double h);
    INavigationController reCreateScene();
}
