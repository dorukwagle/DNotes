package com.doruk.dnotes.MarkdownEditor.interfaces;

public interface ToolCmdStrategy {
    void applyToggle(FXTextEditor editor);    
    boolean isApplied(FXTextEditor editor);
    boolean isAppliedOnSelection(FXTextEditor editor);
}
