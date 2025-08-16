package com.doruk.dnotes.MarkdownEditor.interfaces;

import org.fxmisc.richtext.model.TwoDimensional.Bias;

public abstract class ParagraphStyleTool extends ToolCmdStrategy {

    public ParagraphStyleTool(FXTextEditor editor) {
        super(editor);
    }

    @Override
    protected StyleType getStyleType() {
        return StyleType.ParagraphStyle;
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

        var newStyle = this.getStyle(currentStyle, apply);
        area.setParagraphStyle(parIndex, newStyle);
    }

    @Override
    public boolean isApplied(FXTextEditor editor) {
        var area = editor.getArea();
        var pos = area.getCaretPosition();
        
        return this.hasStyle(area.getParagraphStyleForInsertionAt(pos));
    }

    @Override
    public boolean isAppliedOnSelection(FXTextEditor editor) {
        var area = editor.getArea();

        int startPar = area.offsetToPosition(area.getSelection().getStart(), Bias.Forward).getMajor();
        int endPar = area.offsetToPosition(area.getSelection().getEnd(), Bias.Backward).getMajor();

        for (int i = startPar; i <= endPar; i++) {
            if (!this.hasStyle(area.getParagraph(i).getParagraphStyle()))
                return false;
        }
        return true;
    }
}
