package com.doruk.dnotes.MarkdownEditor.interfaces;

public abstract class ToolCmdStrategy {
    public abstract void apply(FXTextEditor editor);   
    public abstract void unapply(FXTextEditor editor);

    // receives either TextStyle or ParagraphStyle, instances should compute its presence
    // in the given style
    // e.g. bold tool: (style) { return style.bold; } // boolean property, can have multi styles
    // e.g. h1 tool: (style) { return style.type == ParagraphType.H1; } // has only one p style
    protected abstract <T> boolean hasStyle(T style);

    public boolean isApplied(FXTextEditor editor) {
        return false;
    }

    public boolean isAppliedOnSelection(FXTextEditor editor) {
        return false;
    }
}
