package com.doruk.dnotes.MarkdownEditor.tools;

import com.doruk.dnotes.MarkdownEditor.interfaces.FXTextEditor;
import com.doruk.dnotes.MarkdownEditor.interfaces.ToolCmdStrategy;

public class BoldCmdStrategy implements ToolCmdStrategy {
    @Override
    public void applyToggle(FXTextEditor editor) {
        // editor.toggleBold();
    }

    @Override
    public boolean isApplied(FXTextEditor editor) {
        // return editor.isBold();
        return false;
    }

    @Override
    public boolean isAppliedOnSelection(FXTextEditor editor) {
        // return editor.isBold();
        return false;
    }
}
