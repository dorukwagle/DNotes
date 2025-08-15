package com.doruk.dnotes.MarkdownEditor.interfaces;

import org.fxmisc.richtext.model.TwoDimensional.Bias;

public abstract class ToolCmdStrategy {
    protected enum StyleType {
        TextStyle,
        ParagraphStyle
    }

    public ToolCmdStrategy(FXTextEditor editor) {
        this.addRenderer(editor);
    }

    public void apply(FXTextEditor editor) {
        var area = editor.getArea();
        var selection = area.getSelection();

        if (selection.getLength() > 0)
            this.processOnSelection(editor, selection.getStart(), selection.getEnd(), true);
        else
            this.processOnInsertion(editor, area.getCaretPosition(), true);
    }

    public void unapply(FXTextEditor editor) {
        var area = editor.getArea();
        var selection = area.getSelection();

        if (selection.getLength() > 0)
            this.processOnSelection(editor, selection.getStart(), selection.getEnd(), false);
        else
            this.processOnInsertion(editor, area.getCaretPosition(), false);
    }

    // receives either TextStyle or ParagraphStyle, instances should compute its presence
    // in the given style
    // e.g. bold tool: (style) { return style.bold; } // boolean property, can have multi styles
    // e.g. h1 tool: (style) { return style.type == ParagraphType.H1; } // has only one p style
    protected abstract <T> boolean hasStyle(T style);

    // should return the type of style the tool is
    protected abstract StyleType getStyleType();

    // should return the actual TextStyle/ParagraphStyle for rendering
    protected abstract <T> T getStyle(T currentStyle, boolean apply);

    // apply on selection
    protected void processOnSelection(FXTextEditor editor, int start, int end, boolean apply) {
        var area = editor.getArea();
        var spans = area.getStyleSpans(start, end);

        var newSpans = spans.mapStyles(style -> this.getStyle(style, apply));
        area.setStyleSpans(start, newSpans);
    }

    // apply on insertion
    protected void processOnInsertion(FXTextEditor editor, int pos, boolean apply) {
        editor.getArea().setTextInsertionStyle(
            this.getStyle(editor.getArea().getTextStyleForInsertionAt(pos), apply));
    }

    // add renderer to the editor
    protected abstract void addRenderer(FXTextEditor editor);

    public boolean isApplied(FXTextEditor editor) {
        var area = editor.getArea();
        var pos = area.getCaretPosition();
        
        var style = this.getStyleType() == StyleType.TextStyle ?
            area.getTextStyleForInsertionAt(pos) :
            area.getParagraphStyleForInsertionAt(pos);

        return this.hasStyle(style);
    }

    public boolean isAppliedOnSelection(FXTextEditor editor) {
        return this.getStyleType() == StyleType.TextStyle ?
            this.isTextStylePresent(editor) :
            this.isParagraphStylePresent(editor);
    }

    private boolean isTextStylePresent(FXTextEditor editor) {
        var area = editor.getArea();
        var selection = area.getSelection();
        var spans =
            area.getStyleSpans(selection.getStart(), selection.getEnd());

        return spans.stream()
            .allMatch(span -> this.hasStyle(span.getStyle()));
    }

    private boolean isParagraphStylePresent(FXTextEditor editor) {
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
