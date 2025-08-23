package com.doruk.dnotes.MarkdownEditor.chageHandlers;

import com.doruk.dnotes.MarkdownEditor.ControlPanelView;
import com.doruk.dnotes.MarkdownEditor.EditorToolsMediator;
import com.doruk.dnotes.MarkdownEditor.Factory;
import com.doruk.dnotes.MarkdownEditor.enums.EditorToolsEvent;
import com.doruk.dnotes.MarkdownEditor.enums.ToolName;
import com.doruk.dnotes.MarkdownEditor.interfaces.FXTextEditor;
import com.doruk.dnotes.MarkdownEditor.interfaces.StatefulTextStyleTool;

import javafx.application.Platform;

public class FontSizeHandler {
    public FontSizeHandler(FXTextEditor editor, ControlPanelView controlPanel) {
        EditorToolsMediator.subscribe(EditorToolsEvent.FONTSIZE_CHANGE, () -> {
            int size = Integer.parseInt(controlPanel.getFontSizeCombo().getValue());

            @SuppressWarnings("unchecked")
            var tool = (StatefulTextStyleTool<Integer>)Factory.createTool(ToolName.Font, null);

            tool.setState(size);

            Platform.runLater(() -> {
                tool.apply(editor);
                editor.getArea().requestFocus();
            });
        });
    }
}
