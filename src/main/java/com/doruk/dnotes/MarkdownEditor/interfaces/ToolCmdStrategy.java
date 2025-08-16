package com.doruk.dnotes.MarkdownEditor.interfaces;

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
    protected abstract void processOnSelection(FXTextEditor editor, int start, int end, boolean apply);

    // apply on insertion
    protected abstract void processOnInsertion(FXTextEditor editor, int pos, boolean apply);

    // add renderer to the editor
    protected abstract void addRenderer(FXTextEditor editor);

    // check if style is applied in given caret position
    public abstract boolean isApplied(FXTextEditor editor);

    // check if style is applied in selected text region
    public abstract boolean isAppliedOnSelection(FXTextEditor editor);
}
