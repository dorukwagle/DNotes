package com.doruk.dnotes.MarkdownEditor.tools;


import com.doruk.dnotes.MarkdownEditor.Factory;
import com.doruk.dnotes.MarkdownEditor.docstyle.ParagraphStyle;
import com.doruk.dnotes.MarkdownEditor.dto.ToolState;
import com.doruk.dnotes.MarkdownEditor.enums.ParagraphType;
import com.doruk.dnotes.MarkdownEditor.enums.ToolName;
import com.doruk.dnotes.MarkdownEditor.interfaces.FXTextEditor;
import com.doruk.dnotes.MarkdownEditor.interfaces.StatefulParagraphStyleTool;
import com.doruk.dnotes.MarkdownEditor.utils.ParagraphStyleHelper;

public class CheckList extends StatefulParagraphStyleTool {
    public CheckList(FXTextEditor editor) {
        super(editor);
    }

    @Override
    protected ParagraphType getParagraphType() {
        return ParagraphType.CHECK_LIST_ITEM;
    }

    @SuppressWarnings("unchecked")
    @Override
    protected <T> T getStyle(T currentStyle, boolean apply) {
        return (T) ParagraphStyleHelper.withCheckList((ParagraphStyle)currentStyle,
                false, apply);
    }

    @Override
    protected void addRenderer(FXTextEditor editor) {
        editor.addParagraphRenderer(ToolName.CheckList, 
                Factory.createParagraphRenderer(ToolName.CheckList));
    }

    @Override
    public void applyWithUpdatedState(FXTextEditor editor, ToolState state) {
        var style = editor.getArea().getParagraph(state.getParagraphIndex()).getParagraphStyle();

        var newStyle = ParagraphStyleHelper.withCheckList(style, !style.isItemChecked, true);
        editor.getArea().setParagraphStyle(state.getParagraphIndex(), newStyle);
    }
}
