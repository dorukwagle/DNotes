package com.doruk.dnotes.MarkdownEditor;

import javafx.scene.Parent;
import javafx.scene.control.Button;

public interface IEditorView {
    Button getCloseButton();
    Parent getView();
}
