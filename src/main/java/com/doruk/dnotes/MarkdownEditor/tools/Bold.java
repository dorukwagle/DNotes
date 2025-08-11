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
    public void apply(FXTextEditor editor) {
        // also set textstyle to editor
        var area = editor.getArea();

        var selection = area.getSelection();
        // if selected
        if (selection.getLength() > 0) {
            area.setStyle(selection.getStart(), selection.getEnd(),
                StyleHelper.textWithBold(area.getStyleAtPosition(selection.getStart()), true));
        }
        else {
            area.setTextInsertionStyle(
                StyleHelper.textWithBold(StyleHelper.textDefault(), true));
        }
    }

    @Override
    public void unapply(FXTextEditor editor) {
        var area = editor.getArea();
        var selection = area.getSelection();
        // if selected
        if (selection.getLength() > 0) {
            area.setStyle(selection.getStart(), selection.getEnd(),
                StyleHelper.textWithBold(area.getStyleAtPosition(
                    selection.getStart()), false));
        }
        else {
            area.setTextInsertionStyle(
                StyleHelper.textWithBold(StyleHelper.textDefault(), false));
        }
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
