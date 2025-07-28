package com.doruk.dnotes.views.components;

import com.doruk.dnotes.interfaces.IConfirmationModal;

import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.ButtonBar.ButtonData;

public class ConfirmationModal implements IConfirmationModal {
    private Dialog<ButtonType> dialog;
    private ButtonType okBtn;
    private Runnable onOk;
    private Runnable onCancel;

    public ConfirmationModal(String title, String message) {
        this.dialog = new Dialog<>();
        dialog.setTitle(title);
        dialog.setContentText(" " + message);

        this.okBtn = new ButtonType("Okay", ButtonData.OK_DONE);
        this.dialog.getDialogPane().getButtonTypes().add(okBtn);
    }

    @Override
    public void setOnOk(Runnable onOk) {
        this.onOk = onOk;
    }

    @Override
    public void setOnCancel(Runnable onCancel) {
        var btn = new ButtonType("Cancel", ButtonData.CANCEL_CLOSE);
        this.dialog.getDialogPane().getButtonTypes().add(btn);
        this.onCancel = onCancel;
    }

    @Override
    public void showAndWait() {
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
