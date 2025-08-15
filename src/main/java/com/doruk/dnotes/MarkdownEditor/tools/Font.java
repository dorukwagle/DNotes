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

    @SuppressWarnings("unchecked")
    @Override
    protected <T> T getStyle(T currentStyle, boolean apply) {
        return (T) StyleHelper.textWithFontSize((TextStyle)currentStyle, this.getState());
    }

    @Override
    protected void addRenderer(FXTextEditor editor) {
        editor.addTextRenderer(ToolName.Font,
                Factory.createStatefulTextRenderer(ToolName.Font, this.getState()));
    }
}
