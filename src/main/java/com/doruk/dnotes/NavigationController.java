package com.doruk.dnotes;

import com.doruk.dnotes.enums.ViewPage;
import com.doruk.dnotes.interfaces.INavigationController;
import com.doruk.dnotes.utils.ControllerFactory;

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

    private NavigationController(Stage stage) {

        // calculate screen size
        double screenWidth = Screen.getPrimary().getVisualBounds().getWidth();
        double screenHeight = Screen.getPrimary().getVisualBounds().getHeight();
        // scale app to 70%, 80% of screen size
        this.defaultW = screenWidth * 0.7;
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

    public void goToHomePage() {
        scene.setRoot(ControllerFactory.create(ViewPage.HOME, this).getView());
    }

    public void goToBooksPage() {
        scene.setRoot(ControllerFactory.create(ViewPage.BOOK, this).getView());
    }

    public void goToEditorPage() {
        scene.setRoot(ControllerFactory.create(ViewPage.EDITOR, this).getView());
    }

    public void goToPreferencePage() {
        scene.setRoot(ControllerFactory.create(ViewPage.PREFERENCE, this).getView());
    }
}
