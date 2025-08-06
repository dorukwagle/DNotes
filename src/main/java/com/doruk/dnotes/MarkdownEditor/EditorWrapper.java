package com.doruk.dnotes.MarkdownEditor;

import org.fxmisc.flowless.VirtualizedScrollPane;

import com.doruk.dnotes.MarkdownEditor.RichTextFX.ParagraphStyle;
import com.doruk.dnotes.MarkdownEditor.enums.EditorColor;
import com.doruk.dnotes.MarkdownEditor.interfaces.View;

import atlantafx.base.theme.Styles;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;


public class EditorWrapper implements View {
    private VBox root;
    private ControlPanelView controlPanel;
    private HBox reference;
    private RichTextFX editor;

    public EditorWrapper() {
        root = new VBox();
        root.setPrefHeight(10);

        controlPanel = new ControlPanelView();
        // add control panel
        var panel = controlPanel.getView();
        root.getChildren().add(panel);
        VBox.setMargin(panel, new Insets(15, 10, 0, 10));
        
        // add markdown editor
        editor = new RichTextFX();
        var scrollPane = new VirtualizedScrollPane<>(editor.getArea());
        scrollPane.setPrefHeight(10);

        VBox.setVgrow(scrollPane, Priority.ALWAYS);
        root.getChildren().add(scrollPane);
        
        // just a color reference for area
        reference = new HBox();
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
        editor.toggleBold();
        editor.toggleItalic();
        editor.applyFontSize(20);
        editor.applyTextColor(Color.RED);
        editor.applyBackgroundColor(Color.YELLOW);

        editor.appendText("normal text");
        

        int paragraphIndex = 0;
        for (var paragraph : editor.getArea().getDocument().getParagraphs()) {
            System.out.println("Paragraph " + paragraphIndex++);

            ParagraphStyle paragraphStyle = paragraph.getParagraphStyle();
            System.out.println("  ParagraphStyle: " + paragraphStyle.toString());

            int segmentIndex = 0;
            for (var styledSegment : paragraph.getStyledSegments()) {
                String text = styledSegment.getSegment();
                var style = styledSegment.getStyle();

                System.out.println("    Segment " + segmentIndex++);
                System.out.println("      Text : \"" + text + "\"");
                System.out.println("      Style: " + style.toString());
            }
        }
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
    public RichTextFX getEditor() {
        return editor;
    }

    @Override
    public ControlPanelView getControlPanel() {
        return controlPanel;
    }
}
