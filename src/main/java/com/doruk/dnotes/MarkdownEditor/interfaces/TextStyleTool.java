package com.doruk.dnotes.MarkdownEditor.interfaces;

public abstract class TextStyleTool extends ToolCmdStrategy {
    
    public TextStyleTool(FXTextEditor editor) {
        super(editor);
    }

    @Override
    protected StyleType getStyleType() {
        return StyleType.TextStyle;
    }

    // apply on selection
    @Override
    protected void processOnSelection(FXTextEditor editor, int start, int end, boolean apply) {
        var area = editor.getArea();
        var spans = area.getStyleSpans(start, end);

        var newSpans = spans.mapStyles(style -> this.getStyle(style, apply));
        area.setStyleSpans(start, newSpans);
    }

    // apply on insertion
    @Override
    protected void processOnInsertion(FXTextEditor editor, int pos, boolean apply) {
        editor.getArea().setTextInsertionStyle(
            this.getStyle(editor.getArea().getTextStyleForInsertionAt(pos), apply));
    }

    @Override
    public boolean isApplied(FXTextEditor editor) {
        var area = editor.getArea();
        var pos = area.getCaretPosition();
        
        return this.hasStyle(area.getTextStyleForInsertionAt(pos));
    }

    @Override
    public boolean isAppliedOnSelection(FXTextEditor editor) {
        var area = editor.getArea();
        var selection = area.getSelection();
        var spans =
            area.getStyleSpans(selection.getStart(), selection.getEnd());

        return spans.stream()
            .allMatch(span -> this.hasStyle(span.getStyle()));
    }
}
