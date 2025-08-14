package com.doruk.dnotes.MarkdownEditor.tools;

import com.doruk.dnotes.MarkdownEditor.Factory;
import com.doruk.dnotes.MarkdownEditor.docstyle.TextStyle;
import com.doruk.dnotes.MarkdownEditor.enums.ToolName;
import com.doruk.dnotes.MarkdownEditor.interfaces.FXTextEditor;
import com.doruk.dnotes.MarkdownEditor.interfaces.StatefulToolCmdStrategy;
import com.doruk.dnotes.MarkdownEditor.utils.StyleHelper;

public class Font extends StatefulToolCmdStrategy<Integer> {
    
    public Font(FXTextEditor editor) {
        super(editor);
    }

    @Override
    protected <U> void loadState(U style) {
        this.setState(((TextStyle) style).fontSize);
    }

    @Override
    protected StyleType getStyleType() {
        return StyleType.TextStyle;
    }

    @Override
    protected void processOnSelection(FXTextEditor editor, int start, int end, boolean apply) {
        var area = editor.getArea();
        var spans = area.getStyleSpans(start, end);

        var newSpans = spans.mapStyles(style -> StyleHelper.textWithFontSize(style, this.getState()));
        area.setStyleSpans(start, newSpans);
    }

    @Override
    protected void processOnInsertion(FXTextEditor editor, int pos, boolean apply) {
        editor.getArea().setTextInsertionStyle(
                StyleHelper.textWithFontSize(editor.getArea().getTextStyleForInsertionAt(pos), this.getState()));
    }

    @Override
    protected void addRenderer(FXTextEditor editor) {
        editor.addTextRenderer(ToolName.Font,
                Factory.createStatefulTextRenderer(ToolName.Font, this.getState()));
    }
}
