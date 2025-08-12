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

        var italicbtn = this.editorView.getControlPanel()
            .getStyleButtons()
            .stream()
            .filter(btn -> btn.getId().equals(ToolName.Italic.name()))
            .findFirst()
            .orElse(null);

        var bold = Factory.createTool(ToolName.Bold, editorView.getEditor());
        var italic = Factory.createTool(ToolName.Italic, editorView.getEditor());
        boldbtn.setOnAction((_) -> {
            var area = editorView.getEditor().getArea();
            area.requestFocus();

            if (boldbtn.isSelected())
                bold.apply(editorView.getEditor());
            else 
                bold.unapply(editorView.getEditor());
        });            

        italicbtn.setOnAction((_) -> {
            var area = editorView.getEditor().getArea();
            area.requestFocus();

            if (italicbtn.isSelected())
                italic.apply(editorView.getEditor());
            else 
                italic.unapply(editorView.getEditor());
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
