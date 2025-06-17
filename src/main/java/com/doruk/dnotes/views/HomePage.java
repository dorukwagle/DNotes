package com.doruk.dnotes.views;

import com.doruk.dnotes.interfaces.IHomeView;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class HomePage implements IHomeView {

    private Button backButton;
    private BorderPane root;

    public HomePage() {
        root = new BorderPane();
        Text welcomeText = new Text("Welcome to DNotes!");
        welcomeText.setFont(new Font(24));
        root.setCenter(welcomeText);
        BorderPane.setAlignment(welcomeText, Pos.CENTER);

        backButton = new Button("Back");
        root.setBottom(backButton);
        BorderPane.setAlignment(backButton, Pos.CENTER);
    }

    @Override
    public Parent getView() {
        return root;
    }

    public Button getBackButton() {
        return backButton;
    }

    public BorderPane getRoot() {
        return root;
    }
}
