package com.doruk.dnotes.MarkdownEditor.keyActionHandlers;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.doruk.dnotes.MarkdownEditor.enums.ToolName;
import com.doruk.dnotes.MarkdownEditor.interfaces.KeyEventHandler;

import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

public class KeyEventDispatcher {
    private static List<KeyEventHandler> handlers = new ArrayList<>();

    public static void addHandler(KeyEventHandler handler) {
        handlers.add(handler);
    }

    public static void dispatch(Set<ToolName> enabledTools, KeyCode action, KeyEvent e) {
        handlers.forEach(handler -> handler.handle(enabledTools, action, e));
    }
}
