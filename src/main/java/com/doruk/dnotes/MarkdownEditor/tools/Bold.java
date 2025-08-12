package com.doruk.dnotes.MarkdownEditor.tools;

import com.doruk.dnotes.MarkdownEditor.Factory;
import com.doruk.dnotes.MarkdownEditor.docstyle.TextStyle;
import com.doruk.dnotes.MarkdownEditor.enums.ToolName;
import com.doruk.dnotes.MarkdownEditor.interfaces.FXTextEditor;
import com.doruk.dnotes.MarkdownEditor.interfaces.ToolCmdStrategy;
import com.doruk.dnotes.MarkdownEditor.utils.StyleHelper;

public class Bold extends ToolCmdStrategy {

    public Bold(FXTextEditor editor) {
        super(editor);
    }

    @Override
    protected void addRenderer(FXTextEditor editor) {
        editor.addTextRenderer(ToolName.Bold, Factory.createTextRenderer(ToolName.Bold));
    }

    @Override
    protected void processOnSelection(FXTextEditor editor, int start, int end, boolean apply) {
        editor.getArea().setStyle(start, end,
            StyleHelper.textWithBold(editor.getArea().getStyleAtPosition(start), apply));
    }

    @Override
    protected void processOnInsertion(FXTextEditor editor, int pos, boolean apply) {
        editor.getArea().setTextInsertionStyle(
            StyleHelper.textWithBold(editor.getArea().getStyleAtPosition(pos), apply));
    }

    @Override
    protected <T> boolean hasStyle(T style) {
        return ((TextStyle)style).bold;
    }

    @Override
    protected StyleType getStyleType() {
        return StyleType.TextStyle;
    }
}
