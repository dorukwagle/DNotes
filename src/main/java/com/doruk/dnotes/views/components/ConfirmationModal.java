package com.doruk.dnotes.views.components;

import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.ButtonBar.ButtonData;

public class ConfirmationModal {
    private Dialog<ButtonType> dialog;
    private ButtonType okBtn;
    private Runnable onOk;
    private Runnable onCancel;

    public ConfirmationModal(String title, String message) {
        this.dialog = new Dialog<>();
        dialog.setTitle(title);
        dialog.setContentText(" " + message);

        this.okBtn = new ButtonType("Proceed", ButtonData.OK_DONE);
        this.dialog.getDialogPane().getButtonTypes().add(okBtn);
    }

    public void setOnOk(Runnable onOk) {
        this.onOk = onOk;
    }

    public void setOnCancel(Runnable onCancel) {
        var btn = new ButtonType("Cancel", ButtonData.CANCEL_CLOSE);
        this.dialog.getDialogPane().getButtonTypes().add(btn);
        this.onCancel = onCancel;
    }

    public void show() {
        var btn = this.dialog.showAndWait();

        if (btn.isPresent() && btn.get() == this.okBtn) {
            if (this.onOk != null)
                this.onOk.run();
        }
        else {
            if (this.onCancel != null)
                this.onCancel.run();
        }
    }
}
