package com.doruk.dnotes.views;

import com.doruk.dnotes.interfaces.IEditorView;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class EditorPage implements IEditorView {

    private Button backButton;
    private BorderPane root;

    public EditorPage() {
        root = new BorderPane();
        Text editorText = new Text("Note Editor");
        editorText.setFont(new Font(24));
        root.setCenter(editorText);
        BorderPane.setAlignment(editorText, Pos.CENTER);

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
