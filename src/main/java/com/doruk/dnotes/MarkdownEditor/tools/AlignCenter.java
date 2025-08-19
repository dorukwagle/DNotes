package com.doruk.dnotes.MarkdownEditor.tools;

import com.doruk.dnotes.MarkdownEditor.interfaces.FXTextEditor;
import com.doruk.dnotes.MarkdownEditor.interfaces.ParagraphStyleTool;
import com.doruk.dnotes.MarkdownEditor.utils.ParagraphStyleHelper;
import com.doruk.dnotes.MarkdownEditor.docstyle.ParagraphStyle;
import com.doruk.dnotes.MarkdownEditor.enums.ParagraphType;
import com.doruk.dnotes.MarkdownEditor.enums.ToolName;
import com.doruk.dnotes.MarkdownEditor.Factory;

public class AlignCenter extends ParagraphStyleTool {
    
    public AlignCenter(FXTextEditor editor) {
        super(editor);
    }

    @Override
    protected ParagraphType getParagraphType() {
        return ParagraphType.ALIGN_CENTER;
    }

    @SuppressWarnings("unchecked")
    @Override
    protected <T> T getStyle(T currentStyle, boolean apply) {
        return (T) ParagraphStyleHelper.withAlignCenter((ParagraphStyle)currentStyle);
    }

    @Override
    protected void addRenderer(FXTextEditor editor) {
        editor.addParagraphRenderer(ToolName.AlignCenter,
                Factory.createParagraphRenderer(ToolName.AlignCenter));
    }    
}
