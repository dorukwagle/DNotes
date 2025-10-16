package com.doruk.dnotes.views.components;

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
        this.dialog = new Stage();
        this.dialog.initModality(autoClose ? Modality.NONE : Modality.APPLICATION_MODAL);
        this.dialog.initStyle(StageStyle.UNDECORATED);


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

        var scene = new Scene(stackPane);
        this.dialog.setScene(scene);

        this.dialog.setMinHeight(200);
        this.dialog.setMinWidth(400);
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
