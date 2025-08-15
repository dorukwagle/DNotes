package com.doruk.dnotes.MarkdownEditor.tools;

import com.doruk.dnotes.MarkdownEditor.Factory;
import com.doruk.dnotes.MarkdownEditor.docstyle.TextStyle;
import com.doruk.dnotes.MarkdownEditor.enums.ToolName;
import com.doruk.dnotes.MarkdownEditor.interfaces.FXTextEditor;
import com.doruk.dnotes.MarkdownEditor.interfaces.StatefulToolCmdStrategy;
import com.doruk.dnotes.MarkdownEditor.utils.StyleHelper;

import javafx.scene.paint.Color;

public class FontColor extends StatefulToolCmdStrategy<Color> {

    public FontColor(FXTextEditor editor) {
        super(editor);
    }

    @Override
    protected <U> void loadState(U style) {
        this.setState(((TextStyle) style).textColor);
    }

    @Override
    protected StyleType getStyleType() {
        return StyleType.TextStyle;
    }

    @SuppressWarnings("unchecked")
    @Override
    protected <T> T getStyle(T currentStyle, boolean apply) {
        return (T) StyleHelper.textWithColor((TextStyle)currentStyle, this.getState());
    }

    @Override
    protected void addRenderer(FXTextEditor editor) {
        editor.addTextRenderer(ToolName.FontColor,
                Factory.createStatefulTextRenderer(ToolName.FontColor, this.getState()));
    }
}
