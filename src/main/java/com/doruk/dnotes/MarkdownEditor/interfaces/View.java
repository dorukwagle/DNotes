package com.doruk.dnotes.MarkdownEditor.interfaces;

import com.doruk.dnotes.enums.EditorColor;

import javafx.scene.Parent;
import javafx.scene.control.Button;

public interface View {
    Button getCloseButton();
    Parent getView();
    void setEditorBackground(EditorColor color);
}
