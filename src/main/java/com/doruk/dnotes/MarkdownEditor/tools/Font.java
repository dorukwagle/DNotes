package com.doruk.dnotes.MarkdownEditor.tools;

import com.doruk.dnotes.MarkdownEditor.Factory;
import com.doruk.dnotes.MarkdownEditor.docstyle.TextStyle;
import com.doruk.dnotes.MarkdownEditor.enums.ToolName;
import com.doruk.dnotes.MarkdownEditor.interfaces.FXTextEditor;
import com.doruk.dnotes.MarkdownEditor.interfaces.StatefulTextStyleTool;
import com.doruk.dnotes.MarkdownEditor.utils.StyleHelper;
import com.doruk.dnotes.store.GlobalConstants;

public class Font extends StatefulTextStyleTool<Integer> {
    
    public Font(FXTextEditor editor) {
        super(editor);
    }

    @Override
    protected void loadState(TextStyle style) {
        this.setState(style.fontSize);
    }

    @SuppressWarnings("unchecked")
    @Override
    protected <T> T getStyle(T currentStyle, boolean apply) {
        return (T) StyleHelper.textWithFontSize((TextStyle)currentStyle, this.getState());
    }

    @Override
    protected void addRenderer(FXTextEditor editor) {
        // set default font size for the first time
        this.setState(GlobalConstants.DEFAULT_FONT_SIZE);
        
        editor.addTextRenderer(ToolName.Font,
                Factory.createTextRenderer(ToolName.Font));
    }
}
