package com.doruk.dnotes.MarkdownEditor.keyActionHandlers;

import com.doruk.dnotes.MarkdownEditor.enums.ParagraphType;

public class CheckListKeyHandler extends ListKeyHandler {
    @Override
    protected ParagraphType getParagraphListType() {
        return ParagraphType.CHECK_LIST_ITEM;
    }
}
