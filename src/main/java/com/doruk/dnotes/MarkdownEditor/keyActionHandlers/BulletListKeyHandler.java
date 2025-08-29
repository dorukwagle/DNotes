package com.doruk.dnotes.MarkdownEditor.keyActionHandlers;

import java.util.Set;

import com.doruk.dnotes.MarkdownEditor.enums.ToolName;
import com.doruk.dnotes.MarkdownEditor.interfaces.KeyEventHandler;

import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

public class BulletListKeyHandler implements KeyEventHandler {
    @Override
    public void handle(Set<ToolName> enabledTools, KeyCode action, KeyEvent event) {
        if (!enabledTools.contains(ToolName.BulletList))
            return;
        
        switch (action) {
            case ENTER -> handleEnter();
            case TAB -> handleTab();
            case BACK_SPACE -> handleBackspace();
            default -> {}
        }
    }

    private void handleEnter() {
        
    }

    private void handleTab() {
        
    }

    private void handleBackspace() {
        
    }
}
