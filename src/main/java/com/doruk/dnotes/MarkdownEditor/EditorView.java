package com.doruk.dnotes.MarkdownEditor;

import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class EditorView implements IEditorView {
    
    private BorderPane root;
    private Button closeButton;

    public EditorView() {
        root = new BorderPane();

        // welcome text
        Text welcomeText = new Text("Welcome to DNotes");
        welcomeText.setFont(new Font(24));
        root.setCenter(welcomeText);

        closeButton = new Button("Close");
        root.setBottom(closeButton);
    }

    @Override
    public Parent getView() {
        return root;
    }

    @Override
    public Button getCloseButton() {
        return closeButton;
    }
}
