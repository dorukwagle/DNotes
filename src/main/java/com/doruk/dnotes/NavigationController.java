package com.doruk.dnotes;

import com.doruk.dnotes.enums.Preference;
import com.doruk.dnotes.enums.ViewPage;
import com.doruk.dnotes.interfaces.INavigationController;
import com.doruk.dnotes.interfaces.IPreference;

import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Screen;
import javafx.stage.Stage;


public class NavigationController implements INavigationController {
    private static NavigationController instance;
    private static Scene scene;
    private final double defaultW;
    private final double defaultH;
    private final IPreference preference;

    private NavigationController(Stage stage) {
        this.preference = DIFactory.createGlobalPreference();

        // calculate screen size
        double screenWidth = Screen.getPrimary().getVisualBounds().getWidth();
        double screenHeight = Screen.getPrimary().getVisualBounds().getHeight();
        // scale app to 75%, 80% of screen size
        this.defaultW = screenWidth * 0.75;
        this.defaultH = screenHeight * 0.8;

        this.createScene(defaultW, defaultH);
        stage.setScene(scene);

        stage.setTitle("DNotes");
        stage.show();
        
        // center the screen once the stage is fully initialized
        Platform.runLater(() -> stage.centerOnScreen());
    }

    public static NavigationController getInstance(Stage stage) {
        if (instance == null)
            instance = new NavigationController(stage);
        return instance;
    }

    private void createScene(double w, double h) {
        scene = new Scene(new Pane(), w, h);
    }

    public NavigationController reCreateScene() {
        this.createScene(this.defaultW, this.defaultH);
        return this;
    }

    public NavigationController reCreateScene(double w, double h) {
        this.createScene(w, h);
        return this;
    }

    @Override
    public void goToHomePage() {
        this.preference.saveLong(Preference.LastVisitedPage, ViewPage.HOME.getId());
        scene.setRoot(ControllerFactory.create(ViewPage.HOME, this).getView());
    }

    @Override
    public void goToBooksPage() {
        this.preference.saveLong(Preference.LastVisitedPage, ViewPage.BOOK.getId());
        scene.setRoot(ControllerFactory.create(ViewPage.BOOK, this).getView());
    }

    @Override
    public void goToPreferencePage() {
        this.preference.saveLong(Preference.LastVisitedPage, ViewPage.PREFERENCE.getId());
        scene.setRoot(ControllerFactory.create(ViewPage.PREFERENCE, this).getView());
    }
}
