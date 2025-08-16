package com.doruk.dnotes.MarkdownEditor.tools;

import com.doruk.dnotes.MarkdownEditor.interfaces.FXTextEditor;
import com.doruk.dnotes.MarkdownEditor.interfaces.ParagraphStyleTool;
import com.doruk.dnotes.MarkdownEditor.docstyle.ParagraphStyle;
import com.doruk.dnotes.MarkdownEditor.enums.ParagraphType;
import com.doruk.dnotes.MarkdownEditor.enums.ToolName;
import com.doruk.dnotes.MarkdownEditor.Factory;

public class AlignCenter extends ParagraphStyleTool {
    
    public AlignCenter(FXTextEditor editor) {
        super(editor);
    }

    @Override
    protected <T> boolean hasStyle(T style) {
        return ((ParagraphStyle) style).type == ParagraphType.ALIGN_CENTER;
    }

    @SuppressWarnings("unchecked")
    @Override
    protected <T> T getStyle(T currentStyle, boolean apply) {
        return (T) new ParagraphStyle(ParagraphType.ALIGN_CENTER, false);
    }

    @Override
    protected void addRenderer(FXTextEditor editor) {
        editor.addParagraphRenderer(ToolName.AlignCenter,
                Factory.createParagraphRenderer(ToolName.AlignCenter));
    }    
}
