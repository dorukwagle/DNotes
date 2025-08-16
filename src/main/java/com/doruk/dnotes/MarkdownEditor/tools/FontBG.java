package com.doruk.dnotes.MarkdownEditor.tools;

import com.doruk.dnotes.MarkdownEditor.Factory;
import com.doruk.dnotes.MarkdownEditor.docstyle.TextStyle;
import com.doruk.dnotes.MarkdownEditor.enums.ToolName;
import com.doruk.dnotes.MarkdownEditor.interfaces.FXTextEditor;
import com.doruk.dnotes.MarkdownEditor.interfaces.StatefulTextStyleTool;
import com.doruk.dnotes.MarkdownEditor.utils.StyleHelper;

import javafx.scene.paint.Color;

public class FontBG extends StatefulTextStyleTool<Color> {
    
    public FontBG(FXTextEditor editor) {
        super(editor);
    }

    @Override
    protected void loadState(TextStyle style) {
        this.setState(style.backgroundColor);
    }

    @SuppressWarnings("unchecked")
    @Override
    protected <T> T getStyle(T currentStyle, boolean apply) {
        return (T) StyleHelper.textWithBackgroundColor((TextStyle)currentStyle, this.getState());
    }

    @Override
    protected void addRenderer(FXTextEditor editor) {
        editor.addTextRenderer(ToolName.FontBG,
                Factory.createStatefulTextRenderer(ToolName.FontBG, this.getState()));
    }
}
