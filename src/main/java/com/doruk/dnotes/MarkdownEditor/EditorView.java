package com.doruk.dnotes.MarkdownEditor;

import org.fxmisc.flowless.VirtualizedScrollPane;
import com.doruk.dnotes.MarkdownEditor.interfaces.View;

import atlantafx.base.theme.Styles;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;


public class EditorView implements View {
    
    private VBox root;
    private ControlPanelView controlPanel;

    public EditorView() {
        root = new VBox();
        root.setPrefHeight(10);

        controlPanel = new ControlPanelView();
        // add control panel
        var panel = controlPanel.getView();
        root.getChildren().add(panel);
        VBox.setMargin(panel, new Insets(15, 10, 0, 10));
        
        // add markdown editor
        var editor = new RichTextFX();
        var scrollPane = new VirtualizedScrollPane<>(editor.getArea());
        scrollPane.setPrefHeight(10);

        VBox.setVgrow(scrollPane, Priority.ALWAYS);
        root.getChildren().add(scrollPane);
        
        // just a color reference for area
        var reference = new HBox();
        reference.getStyleClass().add(Styles.BG_NEUTRAL_MUTED);
        reference.setMaxHeight(0.0001);
        root.getChildren().add(reference);
        editor.getArea().backgroundProperty().bind(reference.backgroundProperty());
        
        VBox.setMargin(scrollPane, new Insets(11, 0, 0, 0));
        
        var txt = "this is bold text";

        editor.applyFontSize(16);

        editor.setText("hello world...");
        // editor.applyBold();
        editor.appendText(txt);
        editor.setSelection(15, 15 + txt.length());
        editor.applyBold();
        editor.applyItalic();
        editor.applyFontSize(20);
        editor.applyTextColor(Color.RED);
        editor.applyBackgroundColor(Color.YELLOW);

        editor.appendText("normal text");
    }

    @Override
    public Parent getView() {
        return root;
    }

    @Override
    public Button getCloseButton() {
        return controlPanel.getBackButton();
    }
}
