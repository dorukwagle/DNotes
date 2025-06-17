package com.doruk.dnotes.views;

import com.doruk.dnotes.interfaces.IBookView;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class BookPage implements IBookView {

    private Button backButton, editorButton;
    private BorderPane root;

    public BookPage() {
        root = new BorderPane();
        Text bookText = new Text("Book Management");
        bookText.setFont(new Font(24));
        root.setCenter(bookText);
        BorderPane.setAlignment(bookText, Pos.CENTER);

        backButton = new Button("Back");
        root.setLeft(backButton);
        BorderPane.setAlignment(backButton, Pos.BOTTOM_LEFT);

        editorButton = new Button("Editor");
        root.setRight(editorButton);
        BorderPane.setAlignment(editorButton, Pos.BOTTOM_RIGHT);
    }

    @Override
    public Parent getView() {
        return root;
    }

    public Button getBackButton() {
        return backButton;
    }

    public Button getEditorButton() {
        return editorButton;
    }

}
