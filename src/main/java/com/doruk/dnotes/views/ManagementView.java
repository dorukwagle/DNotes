package com.doruk.dnotes.views;

import com.doruk.dnotes.interfaces.IView;
import com.doruk.dnotes.views.components.BackButton;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class ManagementView implements IView {
    private VBox view;
    private Button closeButton;

    public ManagementView() {
        this.view = new VBox();
        this.view.setSpacing(10);

        var navBar = new BorderPane();
        navBar.getStyleClass().add("top-bar");
        navBar.setStyle(
                "-fx-background-color: -color-bg-subtle;" +
                        "-fx-padding: 10 20;" +
                        "-fx-border-color: -color-border-default;" +
                        "-fx-border-width: 0 0 1 0;"
        );
        this.view.getChildren().add(navBar);

        Label sourceLabel = new Label("Source");
        sourceLabel.getStyleClass().add("info-label");
        sourceLabel.setStyle("-fx-font-size: 25px; -fx-font-weight: bold;");
        sourceLabel.setPadding(new Insets(0, 0, 0, 100));
        navBar.setLeft(sourceLabel);

        var rightContainer = new HBox(100);

        Label destinationLabel = new Label("Destination");
        destinationLabel.getStyleClass().add("info-label");
        destinationLabel.setStyle("-fx-font-size: 25px; -fx-font-weight: bold;");
        rightContainer.getChildren().add(destinationLabel);

        // add cross button
        closeButton = new BackButton();
        closeButton.setMinWidth(50);
        rightContainer.getChildren().add(closeButton);

        // Add button to top right
        navBar.setRight(rightContainer);
        BorderPane.setAlignment(closeButton, Pos.CENTER_RIGHT);
        BorderPane.setMargin(closeButton, new Insets(0, 0, 0, 10));
    }

    @Override
    public Parent getView() {
        return this.view;
    }

    @Override
    public void setPlaceholder(String txt) {
        // no placeholder needed here
    }
}
