package com.doruk.dnotes.MarkdownEditor;

import org.fxmisc.flowless.VirtualizedScrollPane;

import com.doruk.dnotes.MarkdownEditor.interfaces.View;

import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;


public class EditorView implements View {
    
    private VBox root;
    private Button closeButton;

    public EditorView() {
        root = new VBox();
        root.setPrefHeight(10);

        // add control panel
        var panel = new ControlPanelView().getView();
        root.getChildren().add(panel);
        VBox.setMargin(panel, new Insets(15, 10, 0, 10));

        // add close button and text editor
        closeButton = new Button("Close");
        root.getChildren().add(closeButton);
        
        // add markdown editor
        var editor = new RichTextFX();
        var scrollPane = new VirtualizedScrollPane<>(editor.getArea());
        scrollPane.setPrefHeight(10);
        
        VBox.setVgrow(scrollPane, Priority.ALWAYS);
        root.getChildren().add(scrollPane);

        editor.setText("hello world...");
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
