package com.doruk.dnotes.interfaces;

import javafx.scene.Scene;

public interface INavigationController {
    void goToHomePage();
    void goToBooksPage();
    void goToPreferencePage();
    INavigationController reCreateScene(double w, double h);
    INavigationController reCreateScene();

    void goToQuickNotePage();

    void goToSharedNotePage();

    Scene getScene();
}
