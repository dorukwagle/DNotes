package com.doruk.dnotes.MarkdownEditor;

import java.util.Arrays;
import java.util.EnumMap;
import java.util.Map;

import com.doruk.dnotes.MarkdownEditor.enums.EditorColor;
import com.doruk.dnotes.MarkdownEditor.enums.ToolName;
import com.doruk.dnotes.MarkdownEditor.interfaces.IMarkdownEditor;
import com.doruk.dnotes.MarkdownEditor.interfaces.ToolCmdStrategy;
import com.doruk.dnotes.MarkdownEditor.interfaces.View;


import javafx.scene.Parent;

public class MarkdownEditor implements IMarkdownEditor {
    
    private StringBuilder editorText;
    private View editorView;
    private Map<ToolName, Boolean> strategyState = new EnumMap<>(ToolName.class);
    private Map<ToolName, ToolCmdStrategy> strategies = new EnumMap<>(ToolName.class);

    public MarkdownEditor() {
        editorText = new StringBuilder();
        editorView = new EditorWrapper();

        // initialize default strategy states to false
        Arrays.stream(ToolName.values())
            .forEach(toolName -> strategyState.put(toolName, false));

        // initial setup
        initialSetup();
    }

    private void initialSetup() {
        // just for testing
        var boldbtn = this.editorView.getControlPanel()
            .getStyleButtons()
            .stream()
            .filter(btn -> btn.getId().equals(ToolName.Bold.name()))
            .findFirst()
            .orElse(null);
        boldbtn.setOnAction((_) -> {
            var editor = this.editorView.getEditor();
            var area = editor.getArea();

            var pos = area.getCaretPosition();
            // System.out.println("pos: " + pos);
            // area.selectRange(pos, pos + 1);
            editor.toggleBold();
            area.requestFocus();
            // area.selectRange(pos, pos);

            
        });            
    }

    @Override
    public void setEditorBackground(EditorColor color) {
        editorView.setEditorBackground(color);
    }

    @Override
    public void setEditorText(StringBuilder builder) {
        editorText = builder;
    }

    @Override
    public StringBuilder getEditorText() {
        return editorText;
    }

    @Override
    public Parent getView() {
        return this.editorView.getView();
    }

    @Override
    public void setOnClose(Runnable onClose) {
        this.editorView.getCloseButton()
            .setOnAction(_ -> onClose.run());
    }
}
