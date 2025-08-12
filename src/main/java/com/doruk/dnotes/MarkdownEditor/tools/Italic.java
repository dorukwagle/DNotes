package com.doruk.dnotes.MarkdownEditor.tools;

import com.doruk.dnotes.MarkdownEditor.Factory;
import com.doruk.dnotes.MarkdownEditor.docstyle.TextStyle;
import com.doruk.dnotes.MarkdownEditor.enums.ToolName;
import com.doruk.dnotes.MarkdownEditor.interfaces.FXTextEditor;
import com.doruk.dnotes.MarkdownEditor.interfaces.ToolCmdStrategy;
import com.doruk.dnotes.MarkdownEditor.utils.StyleHelper;

public class Italic extends ToolCmdStrategy {
    
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

    @Override
    protected StyleType getStyleType() {
        return StyleType.TextStyle;
    }

    @Override
    protected void processOnSelection(FXTextEditor editor, int start, int end, boolean apply) {
        editor.getArea().setStyle(start, end,
            StyleHelper.textWithItalic(editor.getArea().getStyleAtPosition(start), apply));
    }

    @Override
    protected void processOnInsertion(FXTextEditor editor, int pos, boolean apply) {
        editor.getArea().setTextInsertionStyle(
            StyleHelper.textWithItalic(editor.getArea().getStyleAtPosition(pos), apply));
    }
}
