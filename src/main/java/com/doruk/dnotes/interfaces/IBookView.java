package com.doruk.dnotes.interfaces;

import javafx.scene.control.Button;
import javafx.scene.Parent;

public interface IBookView extends IView {
    Button getBackButton();
    Button getEditorButton();
    void displayEditor(Parent editorView);
}
