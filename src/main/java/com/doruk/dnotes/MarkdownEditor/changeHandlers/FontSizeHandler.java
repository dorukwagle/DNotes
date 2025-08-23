package com.doruk.dnotes.MarkdownEditor.changeHandlers;

import com.doruk.dnotes.MarkdownEditor.ControlPanelView;
import com.doruk.dnotes.MarkdownEditor.Factory;
import com.doruk.dnotes.MarkdownEditor.enums.ToolName;
import com.doruk.dnotes.MarkdownEditor.interfaces.FXTextEditor;
import com.doruk.dnotes.MarkdownEditor.interfaces.StatefulTextStyleTool;

import javafx.application.Platform;

public class FontSizeHandler {
    private FXTextEditor editor;
    
    public FontSizeHandler(FXTextEditor editor, ControlPanelView controlPanel) {
        this.editor = editor;
        
        controlPanel.getFontSizeCombo()
            .valueProperty()
            .subscribe(this::updateState);
    }

    private void updateState(String value) {
        int size = Integer.parseInt(value);

        @SuppressWarnings("unchecked")
        var tool = (StatefulTextStyleTool<Integer>)Factory.createTool(ToolName.Font, null);

        tool.setState(size);

        Platform.runLater(() -> {
            tool.apply(editor);
            editor.getArea().requestFocus();
        });
    }

}
