package com.doruk.dnotes.MarkdownEditor.changeHandlers;

import java.util.function.Consumer;

import com.doruk.dnotes.MarkdownEditor.ToolsMediator;
import com.doruk.dnotes.MarkdownEditor.enums.ToolsEvent;
import com.doruk.dnotes.MarkdownEditor.interfaces.FXTextEditor;

public class CheckboxClickHandler implements Consumer<Object[]> {
    private FXTextEditor editor;
    public CheckboxClickHandler(FXTextEditor editor) {
        this.editor = editor;
        ToolsMediator.subscribe(ToolsEvent.CHECKBOX_CLICKED, this);
    }

    @Override
    public void accept(Object[] args) {
        int index = (int) args[0];
        var area = editor.getArea();
        var style = area.getParagraph(index).getParagraphStyle();
    }
}
