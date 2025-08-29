package com.doruk.dnotes.MarkdownEditor.interfaces;

import com.doruk.dnotes.MarkdownEditor.dto.ToolState;

public abstract class StatefulParagraphStyleTool extends ParagraphStyleTool {
    public StatefulParagraphStyleTool(FXTextEditor editor) {
        super(editor);
    }

    public abstract void applyWithUpdatedState(
        FXTextEditor editor, ToolState state);
}
