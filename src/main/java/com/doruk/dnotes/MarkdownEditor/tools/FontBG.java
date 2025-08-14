package com.doruk.dnotes.MarkdownEditor.tools;

import com.doruk.dnotes.MarkdownEditor.Factory;
import com.doruk.dnotes.MarkdownEditor.docstyle.TextStyle;
import com.doruk.dnotes.MarkdownEditor.enums.ToolName;
import com.doruk.dnotes.MarkdownEditor.interfaces.FXTextEditor;
import com.doruk.dnotes.MarkdownEditor.interfaces.StatefulToolCmdStrategy;
import com.doruk.dnotes.MarkdownEditor.utils.StyleHelper;

import javafx.scene.paint.Color;

public class FontBG extends StatefulToolCmdStrategy<Color> {
    
    public FontBG(FXTextEditor editor) {
        super(editor);
    }

    @Override
    protected <U> void loadState(U style) {
        this.setState(((TextStyle) style).backgroundColor);
    }

    @Override
    protected StyleType getStyleType() {
        return StyleType.TextStyle;
    }

    @Override
    protected void processOnSelection(FXTextEditor editor, int start, int end, boolean apply) {
        var area = editor.getArea();
        var spans = area.getStyleSpans(start, end);

        var newSpans = spans.mapStyles(style -> StyleHelper.textWithBackgroundColor(style, this.getState()));
        area.setStyleSpans(start, newSpans);
    }

    @Override
    protected void processOnInsertion(FXTextEditor editor, int pos, boolean apply) {
        editor.getArea().setTextInsertionStyle(
                StyleHelper.textWithBackgroundColor(editor.getArea().getTextStyleForInsertionAt(pos), this.getState()));
    }

    @Override
    protected void addRenderer(FXTextEditor editor) {
        editor.addTextRenderer(ToolName.FontBG,
                Factory.createStatefulTextRenderer(ToolName.FontBG, this.getState()));
    }
}
