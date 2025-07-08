package com.doruk.dnotes.views;

import java.util.Stack;

import org.kordamp.ikonli.javafx.FontIcon;

import com.doruk.dnotes.interfaces.IBookView;
import com.doruk.dnotes.views.components.Sidebar;

import atlantafx.base.theme.Styles;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class BookPage implements IBookView {

    private Button backButton, editorButton;
    private BorderPane root;
    private VBox editorContainer;

    public BookPage() {
        root = new BorderPane();
        editorContainer = new VBox();

        // add sidebar to the left
        Sidebar sidebar = new Sidebar();
        root.setLeft(sidebar.getView());

        // create a main container
        StackPane mainContainer = new StackPane();
        root.setCenter(mainContainer);

        // create a fab button
        Button fabButton = new Button();
        fabButton.setTooltip(new Tooltip("New Page"));
        fabButton.getStyleClass().addAll(Styles.ACCENT, Styles.ELEVATED_3);
        fabButton.setStyle(
                "-fx-background-radius: 28;\n" +
                        "-fx-min-width: 56;\n" +
                        "-fx-min-height: 56;\n" +
                        "-fx-max-width: 56;\n" +
                        "-fx-max-height: 56;\n" +
                        "-fx-cursor: hand;\n" +
                        "-fx-scale-x: 1;\n" +
                        "-fx-scale-y: 1;\n" +
                        "-fx-transition: all 1s ease;\n" +
                        "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.2), 8, 0, 0, 2);");
        FontIcon icon = new FontIcon("mdi2f-file-plus");
        icon.setIconSize(24);
        icon.setScaleX(2.3);
        icon.setScaleY(2.3);
        fabButton.setGraphic(icon);

        StackPane.setAlignment(fabButton, Pos.BOTTOM_RIGHT);
        StackPane.setMargin(fabButton, new Insets(0, 50, 50, 0));
        mainContainer.getChildren().addAll(editorContainer, fabButton);

        VBox.setVgrow(editorContainer, Priority.ALWAYS);

        // shrink, expang fab while hover
        fabButton.hoverProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal)
                fabButton.setStyle(fabButton.getStyle() + "-fx-scale-x: 0.9; -fx-scale-y: 0.9;");
            else
                fabButton.setStyle(fabButton.getStyle().replace("-fx-scale-x: 0.9; -fx-scale-y: 0.9;", ""));
        });

        Text bookText = new Text("Click on a page to view/edit");
        bookText.setFont(new Font(24));
        editorContainer.getChildren().add(bookText);
        BorderPane.setAlignment(bookText, Pos.CENTER);

        backButton = new Button("Back");
        editorContainer.getChildren().add(backButton);
        BorderPane.setAlignment(backButton, Pos.BOTTOM_LEFT);

        editorButton = new Button("Editor");
        editorContainer.getChildren().add(editorButton);
        BorderPane.setAlignment(editorButton, Pos.BOTTOM_RIGHT);
    }

    @Override
    public Parent getView() {
        return root;
    }

    @Override
    public Button getBackButton() {
        return backButton;
    }

    @Override
    public Button getEditorButton() {
        return editorButton;
    }

    @Override
    public void displayEditor(Parent editorView) {
        this.editorContainer.getChildren().clear();
        VBox.setVgrow(editorView, Priority.ALWAYS);
        this.editorContainer.getChildren().add(editorView);
    }
}
