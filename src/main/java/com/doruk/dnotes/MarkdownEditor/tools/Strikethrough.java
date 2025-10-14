package com.doruk.dnotes.MarkdownEditor.tools;

import com.doruk.dnotes.MarkdownEditor.Factory;
import com.doruk.dnotes.MarkdownEditor.docstyle.TextStyle;
import com.doruk.dnotes.MarkdownEditor.enums.ToolName;
import com.doruk.dnotes.MarkdownEditor.interfaces.FXTextEditor;
import com.doruk.dnotes.MarkdownEditor.interfaces.TextStyleTool;
import com.doruk.dnotes.MarkdownEditor.utils.StyleHelper;

public class Strikethrough extends TextStyleTool {

    public Strikethrough(FXTextEditor editor) {
        super(editor);
    }

    @Override
    protected <T> boolean hasStyle(T style) {
        return ((TextStyle)style).strikethrough;
    }

    @SuppressWarnings("unchecked")
    @Override
    protected <T> T getStyle(T currentStyle, boolean apply) {
        return (T) StyleHelper.textWithStrikethrough((TextStyle)currentStyle, apply);
    }

    @Override
    protected void addRenderer(FXTextEditor editor) {
        editor.addTextRenderer(ToolName.Strikethrough, 
            Factory.createTextRenderer(ToolName.Strikethrough));
    }
}
