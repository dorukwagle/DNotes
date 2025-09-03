package com.doruk.dnotes.MarkdownEditor.changeHandlers;

import java.util.function.Consumer;

import com.doruk.dnotes.MarkdownEditor.Factory;
import com.doruk.dnotes.MarkdownEditor.ToolsMediator;
import com.doruk.dnotes.MarkdownEditor.dto.ParagraphListItemInfo;
import com.doruk.dnotes.MarkdownEditor.enums.ToolName;
import com.doruk.dnotes.MarkdownEditor.enums.ToolsEvent;
import com.doruk.dnotes.MarkdownEditor.interfaces.FXTextEditor;
import com.doruk.dnotes.MarkdownEditor.interfaces.ListStyleTool;

public class CheckboxClickHandler implements Consumer<Object[]> {
    private FXTextEditor editor;
    public CheckboxClickHandler(FXTextEditor editor) {
        this.editor = editor;
        ToolsMediator.subscribe(ToolsEvent.CHECKBOX_CLICKED, this);
    }

    @Override
    public void accept(Object[] args) {
        int index = (int) args[0];
        
        var checkboxTool = Factory.createTool(ToolName.CheckList, null);
        if (!(checkboxTool instanceof ListStyleTool tool))
            return;
        
        // tool.applyWithUpdatedState(editor, new ParagraphListItemInfo(
        //     index,
        //     0,
        //     0,
        //     false,
        //     null
        // ));
    }
}
