package com.doruk.dnotes.views.components;

import javafx.scene.control.TextInputDialog;

public class PromptModal {
    private TextInputDialog dialog;

    public PromptModal(String title, String message, String field) {
        this.dialog = new TextInputDialog("text input");
        dialog.setTitle(title);
        dialog.setHeaderText(message);
        dialog.setContentText(field);

    }

    public void show() {
        this.dialog.showAndWait();
    }
}
