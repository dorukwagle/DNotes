package com.doruk.dnotes.MarkdownEditor.interfaces;

import com.doruk.dnotes.MarkdownEditor.RichTextFX;

public interface ToolCmdStrategy {
    void apply(RichTextFX editor);
    void unapply(RichTextFX editor);
    
    boolean isApplied(RichTextFX editor);
    
    void applyOnSelection(RichTextFX editor);
    void unapplyOnSelection(RichTextFX editor);
}
