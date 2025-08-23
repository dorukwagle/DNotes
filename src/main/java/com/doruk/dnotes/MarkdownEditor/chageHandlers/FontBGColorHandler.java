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

public class FontBGColorHandler {
    public FontBGColorHandler(FXTextEditor editor, ControlPanelView controlPanel) {
        EditorToolsMediator.subscribe(EditorToolsEvent.FONT_BG_COLOR_CHANGE, () -> {
            
            @SuppressWarnings("unchecked")
            var tool = (StatefulTextStyleTool<Color>)Factory.createTool(ToolName.FontBG, null);

            Platform.runLater(() -> editor.getArea().requestFocus());

            tool.setState(controlPanel.getHighColorPicker().getValue());

            if (controlPanel.getStyleButtons().stream()
                .anyMatch(btn -> btn.getId().equals(ToolName.FontBG.name()) && btn.isSelected()))
                    Platform.runLater(() -> tool.apply(editor));
        });
    }
}
