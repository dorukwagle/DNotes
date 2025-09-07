package com.doruk.dnotes.MarkdownEditor.keyActionHandlers;

import com.doruk.dnotes.MarkdownEditor.enums.ParagraphType;

public class NumberListKeyHandler extends ListKeyHandler {
    @Override
    protected ParagraphType getParagraphListType() {
        return ParagraphType.NUMBER_LIST_ITEM;
    }
}
