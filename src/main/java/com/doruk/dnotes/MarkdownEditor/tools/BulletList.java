package com.doruk.dnotes.MarkdownEditor.tools;

import com.doruk.dnotes.MarkdownEditor.Factory;
import com.doruk.dnotes.MarkdownEditor.docstyle.ParagraphStyle;
import com.doruk.dnotes.MarkdownEditor.dto.ToolState;
import com.doruk.dnotes.MarkdownEditor.enums.ParagraphType;
import com.doruk.dnotes.MarkdownEditor.enums.ToolName;
import com.doruk.dnotes.MarkdownEditor.interfaces.FXTextEditor;
import com.doruk.dnotes.MarkdownEditor.interfaces.StatefulParagraphStyleTool;
import com.doruk.dnotes.MarkdownEditor.utils.ParagraphStyleHelper;

public class BulletList extends StatefulParagraphStyleTool {
    public BulletList(FXTextEditor editor) {
        super(editor);
    }

    @Override
    protected ParagraphType getParagraphType() {
        return ParagraphType.BULLET_LIST_ITEM;
    }

    @SuppressWarnings("unchecked")
    @Override
    protected <T> T getStyle(T currentStyle, boolean apply) {
        return (T) ParagraphStyleHelper.withBulletList((ParagraphStyle) currentStyle, 1, 1, apply);
    }

    @Override
    protected void addRenderer(FXTextEditor editor) {
        editor.addParagraphRenderer(ToolName.BulletList,
                Factory.createParagraphRenderer(ToolName.BulletList));
    }

    @Override
    public void applyWithUpdatedState(FXTextEditor editor, ToolState state) {
        var style = editor.getArea().getParagraph(state.getParagraphIndex()).getParagraphStyle();

        var newStyle = ParagraphStyleHelper.withBulletList(style, state.getLevel(), 
            state.getLineCount(), 
            true);
        editor.getArea().setParagraphStyle(state.getParagraphIndex(), newStyle);
    }
}
