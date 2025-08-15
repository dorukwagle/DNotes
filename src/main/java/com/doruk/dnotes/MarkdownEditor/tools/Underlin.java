package com.doruk.dnotes.MarkdownEditor.tools;

import com.doruk.dnotes.MarkdownEditor.Factory;
import com.doruk.dnotes.MarkdownEditor.docstyle.TextStyle;
import com.doruk.dnotes.MarkdownEditor.enums.ToolName;
import com.doruk.dnotes.MarkdownEditor.interfaces.FXTextEditor;
import com.doruk.dnotes.MarkdownEditor.interfaces.ToolCmdStrategy;
import com.doruk.dnotes.MarkdownEditor.utils.StyleHelper;

public class Underlin extends ToolCmdStrategy {

    public Underlin(FXTextEditor editor) {
        super(editor);
    }

    @Override
    protected <T> boolean hasStyle(T style) {
        return ((TextStyle)style).underline;
    }

    @Override
    protected StyleType getStyleType() {
        return StyleType.TextStyle;
    }

    @SuppressWarnings("unchecked")
    @Override
    protected <T> T getStyle(T currentStyle, boolean apply) {
        return (T) StyleHelper.textWithUnderline((TextStyle)currentStyle, apply);
    }

    @Override
    protected void addRenderer(FXTextEditor editor) {
        editor.addTextRenderer(ToolName.Underline, Factory.createTextRenderer(ToolName.Underline));
    }
    
}
