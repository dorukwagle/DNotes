package com.doruk.dnotes.MarkdownEditor.tools;

import com.doruk.dnotes.MarkdownEditor.Factory;
import com.doruk.dnotes.MarkdownEditor.docstyle.TextStyle;
import com.doruk.dnotes.MarkdownEditor.enums.ToolName;
import com.doruk.dnotes.MarkdownEditor.interfaces.FXTextEditor;
import com.doruk.dnotes.MarkdownEditor.interfaces.TextStyleTool;
import com.doruk.dnotes.MarkdownEditor.utils.StyleHelper;


public class Bold extends TextStyleTool {

    public Bold(FXTextEditor editor) {
        super(editor);
    }

    @Override
    protected void addRenderer(FXTextEditor editor) {
        editor.addTextRenderer(ToolName.Bold, Factory.createTextRenderer(ToolName.Bold));
    }

    @SuppressWarnings("unchecked")
    @Override
    protected <T> T getStyle(T currentStyle, boolean apply) {
        return (T) StyleHelper.textWithBold((TextStyle)currentStyle, apply);
    }

    @Override
    protected <T> boolean hasStyle(T style) {
        return ((TextStyle)style).bold;
    }
}
