package com.doruk.dnotes.views.components;

import java.util.Optional;

import com.doruk.dnotes.interfaces.IPromptModal;

import javafx.scene.control.TextInputDialog;

public class PromptModal implements IPromptModal {
    private TextInputDialog dialog;

    public PromptModal(String title, String message, String field) {
        this.dialog = new TextInputDialog("");
        dialog.setTitle(title);
        dialog.setHeaderText(message);
        dialog.setContentText(field);
    }

    @Override
    public Optional<String> showAndWait() {
        return dialog.showAndWait();
    }
}
