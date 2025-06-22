package com.doruk.dnotes.views;

import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import com.doruk.dnotes.interfaces.IPreferenceView;

public class PreferencePage implements IPreferenceView {
    private BorderPane root;
    private Button backButton;
    
    public PreferencePage() {
        this.root = new BorderPane();

        Label label = new Label("Preference Page");
        label.getStyleClass().add("heading");
        this.root.setCenter(label);
        BorderPane.setAlignment(label, Pos.CENTER);

        this.backButton = new Button("Back");
        this.backButton.setOnAction(e -> {
            
        });
        this.root.setLeft(this.backButton);
    }

    @Override
    public Parent getView() {
        return this.root;
    }

    @Override
    public Button getBackButton() {
        return this.backButton;
    }
}
