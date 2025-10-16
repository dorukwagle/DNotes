package com.doruk.dnotes.views;

import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class ContextView {
    private final VBox parent;

    public ContextView() {
        this.parent = new VBox();

        this.parent.setSpacing(15);
        this.parent.setAlignment(Pos.CENTER);
        this.parent.setMaxWidth(600);
        this.parent.setStyle(
            "-fx-background-color: -color-bg-default;" +
            "-fx-background-radius: 8;" +
            "-fx-padding: 20;" +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 10, 0, 0, 2);"
        );

        this.parent.getChildren().add(new Label("Context Menu"));
    }

    public Parent getView() {
        return parent;
    }
}
