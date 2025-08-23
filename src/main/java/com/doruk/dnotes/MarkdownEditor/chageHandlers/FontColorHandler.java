package com.doruk.dnotes.MarkdownEditor.chageHandlers;

import com.doruk.dnotes.MarkdownEditor.ControlPanelView;
import com.doruk.dnotes.MarkdownEditor.EditorToolsMediator;
import com.doruk.dnotes.MarkdownEditor.enums.EditorToolsEvent;
import com.doruk.dnotes.MarkdownEditor.enums.ToolName;
import com.doruk.dnotes.MarkdownEditor.Factory;
import com.doruk.dnotes.MarkdownEditor.interfaces.FXTextEditor;
import com.doruk.dnotes.MarkdownEditor.interfaces.StatefulTextStyleTool;

import javafx.application.Platform;
import javafx.scene.paint.Color;

public class FontColorHandler {
    public FontColorHandler(FXTextEditor editor, ControlPanelView controlPanel) {
        EditorToolsMediator.subscribe(EditorToolsEvent.FONT_COLOR_CHANGE, () -> {
            @SuppressWarnings("unchecked")
            var tool = (StatefulTextStyleTool<Color>)Factory.createTool(ToolName.FontColor, null);
            
            Platform.runLater(() -> editor.getArea().requestFocus());

            tool.setState(controlPanel.getTextColorPicker().getValue());
            if (controlPanel.getStyleButtons().stream()
                .anyMatch(btn -> btn.getId().equals(ToolName.FontColor.name()) && btn.isSelected()))
                    Platform.runLater(() -> tool.apply(editor));
        });
    }
}
