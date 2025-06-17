package com.doruk.dnotes.views;

import com.doruk.dnotes.interfaces.IHomeView;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class HomePage implements IHomeView {

    private Button bookButton;
    private BorderPane root;

    public HomePage() {
        root = new BorderPane();
        Text welcomeText = new Text("Welcome to DNotes!");
        welcomeText.setFont(new Font(24));
        root.setCenter(welcomeText);
        BorderPane.setAlignment(welcomeText, Pos.CENTER);

        bookButton = new Button("Book");
        root.setBottom(bookButton);
        BorderPane.setAlignment(bookButton, Pos.CENTER);
    }

    @Override
    public Parent getView() {
        return root;
    }


    public Button getBookButton() {
        return bookButton;
    }
}
