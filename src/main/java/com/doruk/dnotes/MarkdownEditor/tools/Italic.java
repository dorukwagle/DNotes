package com.doruk.dnotes.MarkdownEditor.tools;

import com.doruk.dnotes.MarkdownEditor.Factory;
import com.doruk.dnotes.MarkdownEditor.docstyle.TextStyle;
import com.doruk.dnotes.MarkdownEditor.enums.ToolName;
import com.doruk.dnotes.MarkdownEditor.interfaces.FXTextEditor;
import com.doruk.dnotes.MarkdownEditor.interfaces.TextStyleTool;
import com.doruk.dnotes.MarkdownEditor.utils.StyleHelper;

public class Italic extends TextStyleTool {
    
    public Italic(FXTextEditor editor) {
        super(editor);
    }

    @Override
    protected void addRenderer(FXTextEditor editor) {
        editor.addTextRenderer(ToolName.Italic, Factory.createTextRenderer(ToolName.Italic));
    }

    @Override
    protected <T> boolean hasStyle(T style) {
        return ((TextStyle)style).italic;
    }

    @SuppressWarnings("unchecked")
    @Override
    protected <T> T getStyle(T currentStyle, boolean apply) {
        return (T) StyleHelper.textWithItalic((TextStyle)currentStyle, apply);
    }
}
