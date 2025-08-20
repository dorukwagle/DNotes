package com.doruk.dnotes.MarkdownEditor;

import java.util.Arrays;
import java.util.EnumMap;
import java.util.Map;

import com.doruk.dnotes.MarkdownEditor.enums.EditorColor;
import com.doruk.dnotes.MarkdownEditor.enums.ToolName;
import com.doruk.dnotes.MarkdownEditor.interfaces.IMarkdownEditor;
import com.doruk.dnotes.MarkdownEditor.interfaces.ToolCmdStrategy;
import com.doruk.dnotes.MarkdownEditor.interfaces.View;
import com.doruk.dnotes.MarkdownEditor.utils.StyleGroupRegistry;
import com.doruk.dnotes.store.GlobalConstants;

import javafx.scene.Parent;
import javafx.scene.input.MouseEvent;

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
        // reset colors to default
        this.editorView.getControlPanel()
                .getTextColorPicker()
                .setValue(GlobalConstants.DEFAULT_FONT_COLOR);
        this.editorView.getControlPanel()
                .getHighColorPicker()
                .setValue(GlobalConstants.DEFAULT_FONT_BG_COLOR);

        // loop over each buttons, then apply each tools
        this.editorView.getControlPanel()
                .getStyleButtons()
                .stream()
                .forEach(btn -> {
                    var tool = Factory.createTool(ToolName.fromName(btn.getId()), editorView.getEditor());
                    btn.setOnAction(_ -> {
                        var area = editorView.getEditor().getArea();
                        area.requestFocus();

                        if (btn.isSelected())
                            tool.apply(editorView.getEditor());
                        else
                            tool.unapply(editorView.getEditor());
                    });
                });
        
        // loop over once again to add event filter, to resolve conflicting tools
        this.editorView.getControlPanel()
                .getStyleButtons()
                .stream()
                .forEach(btn -> {
                    btn.addEventFilter(MouseEvent.MOUSE_CLICKED, _ -> {
                        var toolName = ToolName.fromName(btn.getId());
                        var conflictingTools = StyleGroupRegistry.getConflictingTools(toolName);
                        conflictingTools.remove(toolName);
                        this.editorView.getControlPanel()
                            .getStyleButtons()
                            .stream()
                            .filter(toggle -> {
                                return conflictingTools.contains(ToolName.fromName(toggle.getId())) 
                                && toggle.isSelected();
                            })
                            .forEach(toggle -> toggle.setSelected(false));
                    });
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
