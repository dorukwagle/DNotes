package com.doruk.dnotes.MarkdownEditor.changeHandlers;

import com.doruk.dnotes.MarkdownEditor.ControlPanelView;
import com.doruk.dnotes.MarkdownEditor.EditorToolsMediator;
import com.doruk.dnotes.MarkdownEditor.enums.EditorToolsEvent;
import com.doruk.dnotes.MarkdownEditor.interfaces.FXTextEditor;

public class CaretSelectionHandler {
    private FXTextEditor editor;
    private ControlPanelView controlPanel;
    private int lastCaretPosition;

    public CaretSelectionHandler(FXTextEditor editor, ControlPanelView controlPanel) {
        this.editor = editor;
        this.controlPanel = controlPanel;

        EditorToolsMediator.subscribe(EditorToolsEvent.CARET_POS_CHANGE, this::onCaretPosChange);
        EditorToolsMediator.subscribe(EditorToolsEvent.SELECTION_CHANGE, this::onSelectionChange);
    }

    private void onCaretPosChange() {
        var selected = editor.getArea().getSelection().getLength() > 0;
        if (selected)
            return;

        var pos = editor.getArea().getCaretPosition();
        if (pos == lastCaretPosition)
            return;

        lastCaretPosition = pos;
        
        System.out.println("Caret position changed" + pos);
    }

    private void onSelectionChange() {
        System.out.println("Selection changed" + editor.getArea().getSelection().getLength());
    }
}
