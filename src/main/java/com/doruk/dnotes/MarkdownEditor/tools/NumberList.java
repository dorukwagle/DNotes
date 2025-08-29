package com.doruk.dnotes.MarkdownEditor.tools;

import com.doruk.dnotes.MarkdownEditor.Factory;
import com.doruk.dnotes.MarkdownEditor.docstyle.ParagraphStyle;
import com.doruk.dnotes.MarkdownEditor.dto.ToolState;
import com.doruk.dnotes.MarkdownEditor.enums.ParagraphType;
import com.doruk.dnotes.MarkdownEditor.enums.ToolName;
import com.doruk.dnotes.MarkdownEditor.interfaces.FXTextEditor;
import com.doruk.dnotes.MarkdownEditor.interfaces.StatefulParagraphStyleTool;
import com.doruk.dnotes.MarkdownEditor.utils.ParagraphStyleHelper;

public class NumberList extends StatefulParagraphStyleTool {
    public NumberList(FXTextEditor editor) {
        super(editor);
    }

    @Override
    protected ParagraphType getParagraphType() {
        return ParagraphType.NUMBER_LIST_ITEM;
    }

    @SuppressWarnings("unchecked")
    @Override
    protected <T> T getStyle(T currentStyle, boolean apply) {
        return (T) ParagraphStyleHelper.withNumberList((ParagraphStyle)currentStyle, 0, 0, apply);
    }

    @Override
    protected void addRenderer(FXTextEditor editor) {
        editor.addParagraphRenderer(ToolName.NumberList, 
                Factory.createParagraphRenderer(ToolName.NumberList));
    }


    @Override
    public void applyWithUpdatedState(FXTextEditor editor, ToolState state) {
        var style = editor.getArea().getParagraph(state.getParagraphIndex()).getParagraphStyle();

        var newStyle = ParagraphStyleHelper.withNumberList(style, state.getLevel(), 
            state.getLineCount(), 
            true);
        editor.getArea().setParagraphStyle(state.getParagraphIndex(), newStyle);
    }   
}
