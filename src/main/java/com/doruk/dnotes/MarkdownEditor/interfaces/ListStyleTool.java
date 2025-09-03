package com.doruk.dnotes.MarkdownEditor.interfaces;

import org.fxmisc.richtext.model.TwoDimensional.Bias;

import com.doruk.dnotes.MarkdownEditor.dto.ParagraphListItemInfo;
import com.doruk.dnotes.MarkdownEditor.lists.ListManager;
import com.doruk.dnotes.MarkdownEditor.utils.ParagraphStyleHelper;

public abstract class ListStyleTool extends ParagraphStyleTool {
    public ListStyleTool(FXTextEditor editor) {
        super(editor);
    }

    // this method isn't needed for ListStyleTool
    @SuppressWarnings("unchecked")
    @Override
    protected <T> T getStyle(T currentStyle, boolean apply) {
        return null;
    }

    // apply paragraph style on selection
    @Override
    protected void processOnSelection(FXTextEditor editor, int start, int end, boolean apply) {
        var area = editor.getArea();

        int startPar = area.offsetToPosition(start, Bias.Forward).getMajor();
        int endPar = area.offsetToPosition(end, Bias.Backward).getMajor();

        for (int i = startPar; i <= endPar; i++) {
            var currentStyle = area.getParagraph(i).getParagraphStyle();
            var newStyle = this.getStyle(currentStyle, apply);
            area.setParagraphStyle(i, newStyle);
        }
    }

    // apply paragraph style on insertion (current paragraph only)
    @Override
    protected void processOnInsertion(FXTextEditor editor, int pos, boolean apply) {
        var area = editor.getArea();

        int parIndex = area.offsetToPosition(pos, Bias.Forward).getMajor();
        var currentStyle = area.getParagraph(parIndex).getParagraphStyle();

        // only provide paragraph type is applied, else remove: supply null
        var state = new ParagraphListItemInfo(currentStyle, parIndex, apply ? this.getParagraphType() : null);

        ListManager.getInstance().createOrRemoveListNode(state);

        System.out.println("applied list style on insertion");
    }

    public void applyWithUpdatedState(FXTextEditor editor, ParagraphListItemInfo state) {
        var style = editor.getArea().getParagraph(state.paragraphIndex).getParagraphStyle();

        var newStyle = ParagraphStyleHelper.withNumberList(style, state.level, 
            state.lineCount, 
            true);
        editor.getArea().setParagraphStyle(state.paragraphIndex, newStyle);
    }   
}
