package com.doruk.dnotes.MarkdownEditor.interfaces;

import java.util.Set;

import com.doruk.dnotes.MarkdownEditor.enums.ToolName;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

public interface KeyEventHandler {
    public void handle(
        FXTextEditor editor,
        Set<ToolName> enabledTools, 
        KeyCode action, 
        KeyEvent event
    );
}
