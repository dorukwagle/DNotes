package com.doruk.dnotes.views.components;

import atlantafx.base.theme.Styles;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class GenericModal {
    private final Stage dialog;

    public GenericModal(Parent parent, boolean autoClose) {
        this(parent, autoClose, 400, 200);
    }

    public GenericModal(Parent parent, boolean autoClose, int width, int height) {
        this.dialog = new Stage();
        this.dialog.initModality(autoClose ? Modality.NONE : Modality.APPLICATION_MODAL);
        this.dialog.initStyle(StageStyle.TRANSPARENT);


        var closeBtn = new Button("Close");
        closeBtn.setOnAction(event -> this.close());
        closeBtn.setCancelButton(true);
        closeBtn.setOpacity(0);

        var stackPane = new StackPane();
        stackPane.getChildren().addAll(closeBtn, parent);

        // listen for clicks outside the modal
        dialog.focusedProperty().addListener((_, _, isFocused) -> {
            if (!isFocused && autoClose)
                this.close();
        });

        stackPane.setStyle(
                "-fx-background-color: -color-bg-default;" +
                "-fx-background-radius: 12;" +
                "-fx-border-color: -color-border-muted;" +
                "-fx-border-radius: 12;" +
                "-fx-border-width: 3px;" +
                "-fx-effect: dropshadow(three-pass-box, rgba(0, 0, 0, 0.2), 24, 0, 0, 0);" +
                "-fx-padding: 8px;"
        );

        var scene = new Scene(stackPane);
        this.dialog.setScene(scene);

        this.dialog.setMinHeight(height);
        this.dialog.setMinWidth(width);
    }

    public void showAndWait() {
        this.dialog.showAndWait();

        this.dialog.setAlwaysOnTop(true);
        this.dialog.setResizable(false);
        this.dialog.centerOnScreen();
    }

    public void close() {
        this.dialog.close();
    }
}
