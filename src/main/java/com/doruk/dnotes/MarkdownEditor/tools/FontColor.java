package com.doruk.dnotes.MarkdownEditor.tools;

import com.doruk.dnotes.MarkdownEditor.Factory;
import com.doruk.dnotes.MarkdownEditor.docstyle.TextStyle;
import com.doruk.dnotes.MarkdownEditor.enums.ToolName;
import com.doruk.dnotes.MarkdownEditor.interfaces.FXTextEditor;
import com.doruk.dnotes.MarkdownEditor.interfaces.StatefulTextStyleTool;
import com.doruk.dnotes.MarkdownEditor.utils.StyleHelper;
import com.doruk.dnotes.store.GlobalConstants;

import javafx.scene.paint.Color;

public class FontColor extends StatefulTextStyleTool<Color> {

    public FontColor(FXTextEditor editor) {
        super(editor);
    }

    @Override
    protected void loadState(TextStyle style) {
        this.setState(style.textColor);
    }

    @SuppressWarnings("unchecked")
    @Override
    protected <T> T getStyle(T currentStyle, boolean apply) {
        return (T) StyleHelper.textWithColor((TextStyle)currentStyle, apply ? this.getState() : null);
    }

    @Override
    protected void addRenderer(FXTextEditor editor) {
        // set default font color for the first time
        this.setState(GlobalConstants.DEFAULT_FONT_COLOR);
        editor.addTextRenderer(ToolName.FontColor,
                Factory.createTextRenderer(ToolName.FontColor));
    }
}
