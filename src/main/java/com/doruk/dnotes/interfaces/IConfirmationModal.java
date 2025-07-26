package com.doruk.dnotes.interfaces;

public interface IConfirmationModal {
    void setOnOk(Runnable onOk);
    void setOnCancel(Runnable onCancel);
    void showAndWait();
}
