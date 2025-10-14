package com.doruk.dnotes.MarkdownEditor.changeHandlers;

import java.util.function.Consumer;

import com.doruk.dnotes.MarkdownEditor.ToolsMediator;
import com.doruk.dnotes.MarkdownEditor.dto.ParagraphListItemInfo;
import com.doruk.dnotes.MarkdownEditor.enums.ParagraphType;
import com.doruk.dnotes.MarkdownEditor.enums.ToolsEvent;
import com.doruk.dnotes.MarkdownEditor.interfaces.FXTextEditor;
import com.doruk.dnotes.MarkdownEditor.utils.ParagraphStyleHelper;


public class CheckboxClickHandler implements Consumer<Object[]> {
    private FXTextEditor editor;
    public CheckboxClickHandler(FXTextEditor editor) {
        this.editor = editor;
        ToolsMediator.subscribe(ToolsEvent.CHECKBOX_CLICKED, this);
    }

    @Override
    public void accept(Object[] args) {
        int parIndex = (int) args[0];
        var area = editor.getArea();
        
        var curStyle = area.getParagraph(parIndex).getParagraphStyle();
        var itemInfo = new ParagraphListItemInfo(curStyle, parIndex, curStyle.level, curStyle.lineCount, !curStyle.isItemChecked, 
            ParagraphType.CHECK_LIST_ITEM);
        
        var style = ParagraphStyleHelper.withListNode(itemInfo, curStyle.numberListId, true);
        area.setParagraphStyle(parIndex, style);
    }
}
