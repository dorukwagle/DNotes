package com.doruk.dnotes.interfaces;

public interface IOptionsModal {
    void showAndWait();
    void setOnDeleteAction(Runnable action);
    void setOnUpdateAction(Runnable action);
    String getInputText();
    void setInputText(String text);
    boolean isConfirmationChecked();
}
