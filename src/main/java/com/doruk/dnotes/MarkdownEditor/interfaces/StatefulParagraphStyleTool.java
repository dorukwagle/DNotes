package com.doruk.dnotes.MarkdownEditor.interfaces;

public abstract class StatefulParagraphStyleTool extends ParagraphStyleTool {
    public StatefulParagraphStyleTool(FXTextEditor editor) {
        super(editor);
    }

    public abstract void applyWithUpdatedState(
        FXTextEditor editor, int paragraphIndex);
}
