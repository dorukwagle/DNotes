package com.doruk.dnotes.MarkdownEditor;

import com.doruk.dnotes.MarkdownEditor.interfaces.View;

import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class EditorView implements View {
    
    private BorderPane root;
    private Button closeButton;

    public EditorView() {
        root = new BorderPane();

        // welcome text
        Text welcomeText = new Text("Welcome to DNotes");
        welcomeText.setFont(new Font(24));
        root.setCenter(welcomeText);

        // add control panel
        var panel = new ControlPanelView();
        root.setTop(panel.getView());

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
