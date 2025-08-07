package com.doruk.dnotes.MarkdownEditor.interfaces;

import com.doruk.dnotes.MarkdownEditor.RichTextFX;

public interface ToolCmdStrategy {
    void applyToggle(RichTextFX editor);    
    boolean isApplied(RichTextFX editor);
}
