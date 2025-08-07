package com.doruk.dnotes.MarkdownEditor.tools;

import com.doruk.dnotes.MarkdownEditor.RichTextFX;
import com.doruk.dnotes.MarkdownEditor.interfaces.ToolCmdStrategy;

public class BoldCmdStrategy implements ToolCmdStrategy {
    @Override
    public void applyToggle(RichTextFX editor) {
        editor.toggleBold();
    }

    @Override
    public boolean isApplied(RichTextFX editor) {
        // return editor.isBold();
        return false;
    }
}
