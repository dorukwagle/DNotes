package com.doruk.dnotes.MarkdownEditor.interfaces;

import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

public interface KeyEventHandler {
    public void handle(
        FXTextEditor editor,
        KeyCode action, 
        KeyEvent event
    );
}
