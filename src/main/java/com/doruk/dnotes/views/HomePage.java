package com.doruk.dnotes.views;

import com.doruk.dnotes.interfaces.IHomeView;
import com.doruk.dnotes.views.components.Sidebar;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class HomePage implements IHomeView {

    private Button bookButton;
    private BorderPane root;

    public HomePage() {
        // Create main layout
        root = new BorderPane();
        
        // Create and add sidebar
        Sidebar sidebar = new Sidebar();
        root.setLeft(sidebar.getView());
        
        // Create center content
        VBox centerContent = new VBox();
        centerContent.setAlignment(Pos.CENTER);
        centerContent.setSpacing(20);
        
        // Add welcome text
        Text welcomeText = new Text("Welcome to DNotes!");
        welcomeText.setFont(new Font(24));
        
        // Add book button
        bookButton = new Button("Open Books");
        
        // Add components to center content
        centerContent.getChildren().addAll(welcomeText, bookButton);
        
        // Set center content
        root.setCenter(centerContent);
        
        // Make the center content grow to fill available space
        VBox.setVgrow(centerContent, Priority.ALWAYS);
        HBox.setHgrow(centerContent, Priority.ALWAYS);
    }

    @Override
    public Parent getView() {
        return root;
    }


    public Button getBookButton() {
        return bookButton;
    }
}
