package com.doruk.dnotes.MarkdownEditor.keyActionHandlers;

import com.doruk.dnotes.MarkdownEditor.enums.ParagraphType;

public class BulletListKeyHandler extends ListKeyHandler {
    @Override
    protected ParagraphType getParagraphListType() {
        return ParagraphType.BULLET_LIST_ITEM;
    }
}
