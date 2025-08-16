package com.doruk.dnotes.MarkdownEditor.tools;

import com.doruk.dnotes.MarkdownEditor.enums.ParagraphType;
import com.doruk.dnotes.MarkdownEditor.interfaces.FXTextEditor;
import com.doruk.dnotes.MarkdownEditor.interfaces.ParagraphStyleTool;
import com.doruk.dnotes.MarkdownEditor.docstyle.ParagraphStyle;
import com.doruk.dnotes.MarkdownEditor.enums.ToolName;
import com.doruk.dnotes.MarkdownEditor.Factory;

public class H3 extends ParagraphStyleTool {
    
    public H3(FXTextEditor editor) {
        super(editor);
    }

    @Override
    protected <T> boolean hasStyle(T style) {
        return ((ParagraphStyle) style).type == ParagraphType.H3;
    }

    @SuppressWarnings("unchecked")
    @Override
    protected <T> T getStyle(T currentStyle, boolean apply) {
        return (T) new ParagraphStyle(ParagraphType.H3, false);
    }

    @Override
    protected void addRenderer(FXTextEditor editor) {
        editor.addParagraphRenderer(ToolName.H3,
                Factory.createParagraphRenderer(ToolName.H3));
    }    
}
