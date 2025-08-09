package com.doruk.dnotes.MarkdownEditor.tools;

import com.doruk.dnotes.MarkdownEditor.docstyle.TextStyle;
import com.doruk.dnotes.MarkdownEditor.interfaces.FXTextEditor;
import com.doruk.dnotes.MarkdownEditor.interfaces.ToolCmdStrategy;

public class BoldCmdStrategy extends ToolCmdStrategy {
    @Override
    public void apply(FXTextEditor editor) {
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

    @Override
    public void unapply(FXTextEditor editor) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'unapply'");
    }

    @Override
    public <T> boolean hasStyle(T style) {
        return ((TextStyle)style).bold;
    }

    @Override
    public StyleType getStyleType() {
        return StyleType.TextStyle;
    }
}
