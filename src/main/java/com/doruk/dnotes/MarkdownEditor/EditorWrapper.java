package com.doruk.dnotes.MarkdownEditor;

import org.fxmisc.flowless.VirtualizedScrollPane;

import com.doruk.dnotes.MarkdownEditor.enums.EditorColor;
import com.doruk.dnotes.MarkdownEditor.interfaces.FXTextEditor;
import com.doruk.dnotes.MarkdownEditor.interfaces.View;

import atlantafx.base.theme.Styles;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;


public class EditorWrapper implements View {
    private final VBox root;
    private final ControlPanelView controlPanel;
    private final HBox reference;
    private final FXTextEditor editor;

    public EditorWrapper() {
        root = new VBox();
        root.setPrefHeight(10);

        controlPanel = new ControlPanelView();
        // add control panel
        var panel = controlPanel.getView();
        root.getChildren().add(panel);
        VBox.setMargin(panel, new Insets(15, 10, 0, 10));
        
        // add markdown editor
        editor = Factory.getFXTextEditor();
        var scrollPane = new VirtualizedScrollPane<>(editor.getArea());
        scrollPane.setPrefHeight(10);

        VBox.setVgrow(scrollPane, Priority.ALWAYS);
        root.getChildren().add(scrollPane);

        // var emojiTextArea = new TextArea( "Test: ⚾, \u2028Testing 1 2 3 😷 Chess:♕♔ Faces:😀😃😄" ); // "😀😃😄" "😷" "♕♔"
        // emojiTextArea.setPrefHeight(10);
        // root.getChildren().add(emojiTextArea);

        // var emojiTextLabel = new Label("Test: ⚾, gap\u2028Testing 1 2 3 😷 Chess:♕♔ Faces:😀😃😄");
        // emojiTextLabel.setPrefHeight(10);
        // root.getChildren().add(emojiTextLabel);
        // emojiTextArea.setFont(Font.font("Magnolia Script", 16));
        
        // just a color reference for area
        reference = new HBox();
        reference.setMaxHeight(0.0001);
        root.getChildren().add(reference);
        editor.getArea().backgroundProperty().bind(reference.backgroundProperty());
        
        VBox.setMargin(scrollPane, new Insets(11, 0, 0, 0));
    }

    @Override
    public Parent getView() {
        return root;
    }

    @Override
    public Button getCloseButton() {
        return controlPanel.getBackButton();
    }

    @Override
    public void setEditorBackground(EditorColor color) {
        reference.getStyleClass().remove(Styles.BG_NEUTRAL_SUBTLE);
        reference.getStyleClass().remove(Styles.BG_NEUTRAL_MUTED);

        var selected = color == EditorColor.Muted ? Styles.BG_NEUTRAL_MUTED : Styles.BG_NEUTRAL_SUBTLE;
        reference.getStyleClass().add(selected);
    }

    @Override
    public FXTextEditor getEditor() {
        return editor;
    }

    @Override
    public ControlPanelView getControlPanel() {
        return controlPanel;
    }
}
