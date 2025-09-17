package com.doruk.dnotes.MarkdownEditor.changeHandlers;

import com.doruk.dnotes.MarkdownEditor.ControlPanelView;
import com.doruk.dnotes.MarkdownEditor.enums.ToolName;
import com.doruk.dnotes.MarkdownEditor.Factory;
import com.doruk.dnotes.MarkdownEditor.interfaces.FXTextEditor;
import com.doruk.dnotes.MarkdownEditor.interfaces.StatefulTextStyleTool;

import javafx.application.Platform;
import javafx.scene.paint.Color;

public class FontBGColorHandler {
    private FXTextEditor editor;
    private ControlPanelView controlPanel;
    
    public FontBGColorHandler(FXTextEditor editor, ControlPanelView controlPanel) {
        this.editor = editor;
        this.controlPanel = controlPanel;
        
        controlPanel.getHighColorPicker()
            .valueProperty()
            .subscribe(this::updateState);
    }

    private void updateState(Color value) {
        @SuppressWarnings("unchecked")
        var tool = (StatefulTextStyleTool<Color>)Factory.createTool(ToolName.FontBG, null);
        
        Platform.runLater(() -> editor.getArea().requestFocus());
        
        tool.setState(value);
        if (controlPanel.getStyleButtons().stream()
            .anyMatch(btn -> btn.getId().equals(ToolName.FontBG.name()) && btn.isSelected()))
                Platform.runLater(() -> tool.apply(editor));
    }
}
